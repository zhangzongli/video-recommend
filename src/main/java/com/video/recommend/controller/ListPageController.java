package com.video.recommend.controller;

import com.video.recommend.model.IndexReturn;
import com.video.recommend.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 *    列表页面controller
 * @author zhangzl
 * @create 2018-09-01 下午10:55
 */
@Slf4j
@RestController
public class ListPageController {

    private String recommenderNum = "30";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private VideoService videoService;

    /**
     * 列表界面查询
     * @param comment
     * @return
     */
    @GetMapping("/list_page/search")
    public List<IndexReturn> listPageSearch(String comment) {
        List<IndexReturn> returnList = new ArrayList<IndexReturn>();
        StringBuffer stringBuffer = new StringBuffer();
        if (StringUtils.isEmpty(comment)) {
            stringBuffer.append("select id, video_name, video_pic, video_url, video_summary from video");
        }else {
            stringBuffer.append("select id, video_name, video_pic, video_url, video_summary from video where video_name like ");
            stringBuffer.append("'%");
            stringBuffer.append(comment);
            stringBuffer.append("%'");
        }
        List<Map<String, Object>> videos = jdbcTemplate.queryForList(stringBuffer.toString());
        if (null != videos && videos.size() > 0) {
            for (Map<String, Object> map : videos) {
                IndexReturn indexReturn = new IndexReturn();
                indexReturn.setVideoId((Integer) map.get("id"));
                indexReturn.setVideoName((String) map.get("video_name"));
                indexReturn.setVideoPic((String) map.get("video_pic"));
                indexReturn.setVideoSummary((String) map.get("video_summary"));
                indexReturn.setVideoUrl((String) map.get("video_url"));
                returnList.add(indexReturn);
            }
        }
        return returnList;
    }

    /**
     * 列表界面筛选接口
     * @param module
     * @param area
     * @param type
     * @param time
     * @return
     */
    @GetMapping("/list_page/filter")
    public  List<IndexReturn> getVideo(HttpSession session, String module, String area, String type, String time) {
        List<IndexReturn> returnList = new ArrayList<IndexReturn>();
        Object userId = session.getAttribute("userId");
        if (null != userId) {
            StringBuffer stringBuffer = new StringBuffer("select s.user_id, s.video_id, s.score from score s inner join video v on v.id = s.video_id where 1=1");
            if (!"all".equals(module)) {
                stringBuffer.append(" and v.video_module = " + module);
            }
            if (!"all".equals(area) && !StringUtils.isEmpty(area)) {
                stringBuffer.append(" and v.video_area = " + area);
            }
            if (!"all".equals(type) && !StringUtils.isEmpty(type)) {
                stringBuffer.append(" and v.video_type = '"+type+"'");
            }
            if (!"all".equals(time) && !StringUtils.isEmpty(time)) {
                stringBuffer.append(" and v.show_time = " + time);
            }
            String videoIds = videoService.userCF((String) userId, stringBuffer.toString(), recommenderNum);
            returnList = videoService.getIndexReturnForSql(videoIds);
        }else {
            StringBuffer sb = new StringBuffer("select id, video_name, video_pic, video_url, video_summary from video where 1=1 ");
            if (!"all".equals(module)) {
                sb.append(" and video_module = " + module);
            }
            if (!"all".equals(area) && !StringUtils.isEmpty(area)) {
                sb.append(" and video_area = " + area);
            }
            if (!"all".equals(type) && !StringUtils.isEmpty(type)) {
                sb.append(" and video_type = '"+type+"'");
            }
            if (!"all".equals(time) && !StringUtils.isEmpty(time)) {
                sb.append(" and show_time = " + time);
            }
            sb.append(" ORDER BY create_time desc limit 30");
            List<Map<String, Object>> videos = jdbcTemplate.queryForList(sb.toString());
            if (null != videos && videos.size() > 0) {
                for (Map<String, Object> map : videos) {
                    IndexReturn indexReturn = new IndexReturn();
                    indexReturn.setVideoId((Integer) map.get("id"));
                    indexReturn.setVideoName((String) map.get("video_name"));
                    indexReturn.setVideoPic((String) map.get("video_pic"));
                    indexReturn.setVideoSummary((String) map.get("video_summary"));
                    indexReturn.setVideoUrl((String) map.get("video_url"));
                    returnList.add(indexReturn);
                }
            }
        }
        return returnList;
    }

}
