package com.video.recommend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 *    播放页面controller
 * @author zhangzl
 * @create 2018-09-01 下午9:47
 */
@Slf4j
@RestController
public class PlayPageController {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * 通过用户不同行为给视频评分
     * 用户行为中的评分 只是 该视频评分的子集
     * @param httpSession
     * @param userBehavior
     * @param videoId
     */
    @GetMapping("/score")
    public void setScore(HttpSession httpSession, String userBehavior, String videoId) {
        String userId = (String)httpSession.getAttribute("userId");
        if (StringUtils.isEmpty(userId)) {
            log.info("无登录用户");
            return;
        }
        String scoreSql = "select score from score where user_id = "+userId+" and video_id = "+videoId+" limit 1";
        List<Map<String, Object>> scores = jdbcTemplate.queryForList(scoreSql);
        double score = 0d;
        try {
            if (null != scores && scores.size() > 0) {
                //非第一次评分
                score = count((Double) scores.get(0).get("score"), userBehavior);
                String updateSql = "update score set score = "+score+" where user_id = "+userId+" and video_id = " + videoId;
                jdbcTemplate.execute(updateSql);
            }else {
                //第一次评分
                score = count(score, userBehavior);
                String insertSql = "insert into score (video_id, user_id, score) VALUES ("+videoId+", "+userId+", "+score+")";
                jdbcTemplate.execute(insertSql);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算评分
     * 视频评分满分5分，评分规则自定义
     * 搜索(search)：搜索一次增加0.1分
     * 浏览(browse)：浏览一次增加0.2分
     * 评分(mark)：设为2.5分
     * 评论(comment)：设为4分
     * 收藏(collection)：设为5分
     * 分享(share)：设为5分
     * @param score
     * @param userBehavior
     * @return
     */
    private Double count(double score, String userBehavior) {
        BigDecimal sum = new BigDecimal(0f);
        switch (userBehavior) {
            case "search":
                sum = new BigDecimal(score).add(new BigDecimal(0.1d));
                sum = sum.doubleValue() > 5d ? new BigDecimal(5d) : sum;
                break;
            case "browse":
                sum = new BigDecimal(score).add(new BigDecimal(0.2d));
                sum = sum.doubleValue() > 5d ? new BigDecimal(5d) : sum;
                break;
            case "mark":
                sum = score > 2.5d ? new BigDecimal(score) : new BigDecimal(2.5d);
                break;
            case "comment":
                sum = score > 4d ? new BigDecimal(score) : new BigDecimal(4d);
                break;
            case "collection":
                sum = new BigDecimal(5d);
                break;
            case "share":
                sum = new BigDecimal(5d);
                break;
                default:
        }
        return sum.doubleValue();
    }
}
