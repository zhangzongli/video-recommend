package com.video.recommend.controller;

import com.alibaba.fastjson.JSONObject;
import com.video.recommend.model.IndexReturn;
import com.video.recommend.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.rosuda.REngine.Rserve.RConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 描述:
 *    index controller
 * @author zhangzl
 * @create 2018-08-20 下午9:43
 */
@Slf4j
@Controller
public class IndexController {

    private String recommenderNum = "5";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private VideoService videoService;

    /**
     * index访问
     * @return
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * 判断是否登录
     * @return
     */
    @ResponseBody
    @GetMapping("/is_login")
    public String isLogin(HttpSession session) {
        String result = "";
        Object sessionUserName = session.getAttribute("userName");
        if (null != sessionUserName) {
            result = (String) sessionUserName;
        }
        return result;
    }

    /**
     * 登录接口
     * @param loginJson
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping("/login")
    public String login(@RequestBody JSONObject loginJson, HttpSession session) {
        String result = "success";
        String userName = loginJson.getString("userName");
        String passWord = loginJson.getString("passWord");
        String sql = "select id from `user` where user_name = '"+userName+"' and `password` = '"+passWord+"' limit 1";
        List<Map<String, Object>> loginInfos = jdbcTemplate.queryForList(sql);
        if (null != loginInfos && loginInfos.size() != 0) {
            String userId = String.valueOf(loginInfos.get(0).get("id"));
            Object sessionUserId = session.getAttribute("userId");
            if (null == sessionUserId) {
                log.info("session中 userId不存在");
                session.setAttribute("userId", userId);
                session.setAttribute("userName", userName);
            }else {
                if (userId.equals(sessionUserId)) {
                    log.info("session中 userId=" + sessionUserId);
                }else {
                    session.setAttribute("userId", userId);
                    session.setAttribute("userName", userName);
                }
            }
        }else {
            result = "failed";
        }
        return result;
    }

    /**
     * 登出
     * @param session
     */
    @ResponseBody
    @GetMapping("login_out")
    public void loginOut(HttpSession session) {
        session.setAttribute("userId", null);
        session.setAttribute("userName", null);
    }

    /**
     * 播放页面
     * @return
     */
    @GetMapping("/play_page")
    public String playPage() {
        return "playback_page";
    }

    /**
     * 列表界面
     * @return
     */
    @GetMapping("/list_page")
    public String listPage() {
        return "list_page";
    }


    /**
     * 初始化index界面
     * @param session
     * @return
     */
    @ResponseBody
    @GetMapping("/initIndex")
    public Map<String, List<IndexReturn>> initIndex(HttpSession session) {
        Map<String, List<IndexReturn>> returnMap = new HashMap<String, List<IndexReturn>>();
        Object userId = session.getAttribute("userId");
        if (null == userId) {
            returnMap.put("like", videoService.getNewVideo("all"));
            returnMap.put("movie", videoService.getNewVideo("1"));
            returnMap.put("tv", videoService.getNewVideo("2"));
            returnMap.put("anime", videoService.getNewVideo("3"));
            returnMap.put("variety", videoService.getNewVideo("4"));
        }else {
            //猜你喜欢  基于用户
            String sqlForLike = "select s.user_id, s.video_id, s.score from score s inner join video v on v.id = s.video_id and v.video_module = 1";
            String videoIdsForLike = videoService.userCF((String) userId, sqlForLike, recommenderNum);
            returnMap.put("like", videoService.getIndexReturnForSql(videoIdsForLike));
            //电影 基于物品
            String sqlForMovie = "select s.user_id, s.video_id, s.score from score s inner join video v on v.id = s.video_id and v.video_module = 1" ;
            String videoIdsForMovie = videoService.itemCF((String) userId, sqlForMovie, recommenderNum);
            returnMap.put("movie", videoService.getIndexReturnForSql(videoIdsForMovie));
            //电视剧 基于物品
            String sqlForTV = "select s.user_id, s.video_id, s.score from score s inner join video v on v.id = s.video_id and v.video_module = 2" ;
            String videoIdsForTV = videoService.itemCF((String) userId, sqlForTV, recommenderNum);
            returnMap.put("tv", videoService.getIndexReturnForSql(videoIdsForTV));
            //动漫 基于物品
            String sqlForAnime = "select s.user_id, s.video_id, s.score from score s inner join video v on v.id = s.video_id and v.video_module = 3" ;
            String videoIdsForAnime = videoService.itemCF((String) userId, sqlForAnime, recommenderNum);
            returnMap.put("anime", videoService.getIndexReturnForSql(videoIdsForAnime));
            //综艺 基于物品
            String sqlForVarirty = "select s.user_id, s.video_id, s.score from score s inner join video v on v.id = s.video_id and v.video_module = 4" ;
            String videoIdsForVariety = videoService.itemCF((String) userId, sqlForVarirty, recommenderNum);
            returnMap.put("variety", videoService.getIndexReturnForSql(videoIdsForVariety));
        }
        return returnMap;
    }

    /**
     * 初始化index界面
     * @param session
     * @return
     */
    @ResponseBody
    @GetMapping("/like")
    public List<IndexReturn> getLike(HttpSession session, String module) {
        List<IndexReturn> resultList = new ArrayList<IndexReturn>();
        Object userId = session.getAttribute("userId");
        if (null != userId) {
            //猜你喜欢  基于用户
            String sqlForLike = "select s.user_id, s.video_id, s.score from score s inner join video v on v.id = s.video_id and v.video_module = " + module;
            String videoIdsForLike = videoService.userCF((String) userId, sqlForLike, recommenderNum);
            resultList = videoService.getIndexReturnForSql(videoIdsForLike);
        }else {
            StringBuffer sb = new StringBuffer("select id, video_name, video_pic, video_summary, video_url from video where ");
            sb.append(" video_module='"+module+"'  ");
            sb.append(" ORDER BY create_time desc limit 5");
            List<Map<String, Object>> videos = jdbcTemplate.queryForList(sb.toString());
            if (null != videos && videos.size() > 0) {
                for (Map<String, Object> map : videos) {
                    IndexReturn indexReturn = new IndexReturn();
                    indexReturn.setVideoId((Integer) map.get("id"));
                    indexReturn.setVideoName((String) map.get("video_name"));
                    indexReturn.setVideoPic((String) map.get("video_pic"));
                    indexReturn.setVideoSummary((String) map.get("video_summary"));
                    indexReturn.setVideoUrl((String) map.get("video_url"));
                    resultList.add(indexReturn);
                }
            }
        }
        return resultList;
    }

}
