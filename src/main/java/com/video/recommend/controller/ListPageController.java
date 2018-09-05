package com.video.recommend.controller;

import com.video.recommend.model.IndexReturn;
import com.video.recommend.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
        String sql = "select id, video_name, video_pic, video_url, video_summary from video where video_name like \"%"+comment+"%\"";
        List<Map<String, Object>> videos = jdbcTemplate.queryForList(sql);
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
        StringBuffer sb = new StringBuffer("select id, video_name, video_pic, video_url, video_summary from video where 1=1 ");
        if (!"all".equals(module)) {
            sb.append(" and video_module = " + module);
        }
        if (!"all".equals(area)) {
            sb.append(" and video_area = " + area);
        }
        if (!"all".equals(type)) {
            sb.append(" and video_type = " + type);
        }
        if (!"all".equals(time)) {
            sb.append(" and show_time = " + time);
        }
        sb.append(" ORDER BY create_time desc limit 30");
        Object userId = session.getAttribute("userId");
        if (null != userId) {
            String videoIds = videoService.userCF((String) userId, sb.toString());
            returnList = videoService.getIndexReturnForSql(videoIds);
        }else {
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
