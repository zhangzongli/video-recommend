package com.video.recommend.service;

import com.video.recommend.model.IndexReturn;
import org.rosuda.REngine.Rserve.RConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 *    video service
 * @author zhangzl
 * @create 2018-09-01 下午1:57
 */
@Service
public class VideoService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取最新的电影
     * 参数为'all'时，查询全部的最新电影
     * @param videoModule
     * @return
     */
    public List<IndexReturn> getNewVideo(String videoModule) {
        List<IndexReturn> returnList = new ArrayList<IndexReturn>();
        StringBuffer sb = new StringBuffer("select id, video_name, video_pic, video_summary, video_url from video where ");
        if ("all".equals(videoModule)) {
            sb.append(" 1=1 ");
        }else {
            sb.append(" video_module='"+videoModule+"'  ");
        }
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
                returnList.add(indexReturn);
            }
        }
        return returnList;
    }

    /**
     * 通过videoId 获取
     * @param videoIds
     * @return
     */
    public List<IndexReturn> getIndexReturnForSql(String videoIds) {
        List<IndexReturn> returnList = new ArrayList<IndexReturn>();
        String sql = "select id, video_name, video_pic, video_summary, video_url from video where id in ("+videoIds+")";
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
     * 基于用户推荐算法调用
     * @param userId
     * @param sql
     * @return
     */
    public String userCF(String userId, String sql) {
        String result = "";
        List<Map<String, Object>> dataList = jdbcTemplate.queryForList(sql);
        double[] uidArr = new double[dataList.size()];
        double[] itemArr = new double[dataList.size()];
        double[] prefArr = new double[dataList.size()];
        for (int i = 0; i < dataList.size(); i++) {
            uidArr[i] = Double.valueOf(dataList.get(i).get("user_id").toString());
            itemArr[i] = Double.valueOf(dataList.get(i).get("video_id").toString());
            prefArr[i] = Double.valueOf(dataList.get(i).get("score").toString());
        }
        RConnection rConnection = null;
        try {
            rConnection = new RConnection("localhost", 6311);

            rConnection.eval("source('~/Downloads/R/userCF.R')");

            rConnection.assign("uid",uidArr);
            rConnection.assign("iid",itemArr);
            rConnection.assign("pref",prefArr);
            rConnection.eval("data <- data.frame(uid, iid, pref)");
            // 设置最大近邻数
            rConnection.eval("NeighborHodd_num <- 2");
            // 保留最多个数推荐结果
            rConnection.eval("Recommender_num <- 5");
            rConnection.eval("M <- FileDataModel(data)");
            rConnection.eval("S <- EuclideanDistanceSimilarit( M )");
            rConnection.eval("N <- NearestNUserNeighborhood( S, NeighborHodd_num )");
            rConnection.eval("R <- UserBasedRecommender("+userId+",Recommender_num,M,S,N); R");
            String[] returnItemArr = rConnection.eval("R").asStrings();
            result = spliceResult(returnItemArr);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            rConnection.close();
        }
        return result;
    }

    /**
     * 基于物品推荐算法调用
     * @param userId
     * @param sql
     * @return video id ","分割
     */
    public String itemCF(String userId, String sql) {
        String result = "";
        List<Map<String, Object>> dataList = jdbcTemplate.queryForList(sql);
        double[] uidArr = new double[dataList.size()];
        double[] itemArr = new double[dataList.size()];
        double[] prefArr = new double[dataList.size()];
        for (int i = 0; i < dataList.size(); i++) {
            uidArr[i] = Double.valueOf(dataList.get(i).get("user_id").toString());
            itemArr[i] = Double.valueOf(dataList.get(i).get("video_id").toString());
            prefArr[i] = Double.valueOf(dataList.get(i).get("score").toString());
        }
        RConnection rConnection = null;
        try {
            rConnection = new RConnection("localhost", 6311);

            rConnection.eval("source('~/Downloads/R/itemCF.R')");

            rConnection.assign("uid",uidArr);
            rConnection.assign("iid",itemArr);
            rConnection.assign("pref",prefArr);
            rConnection.eval("data <- data.frame(uid, iid, pref)");
            // 设置最大近邻数
            rConnection.eval("NeighborHodd_num <- 2");
            // 保留最多个数推荐结果
            rConnection.eval("Recommender_num <- 5");
            rConnection.eval("M <- FileDataModel(data)");
            rConnection.eval("S <- EuclideanDistanceSimilarit( M )");
            rConnection.eval("N <- NearestNUserNeighborhood( S, NeighborHodd_num )");
            rConnection.eval("R <- ItemBasedRecommender("+userId+",Recommender_num,M,S,N); R");
            String[] returnItemArr = rConnection.eval("R").asStrings();
            result = spliceResult(returnItemArr);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            rConnection.close();
        }
        return result;
    }

    /**
     * 解析R返回的结果集
     * 拼接成 ","分割的形式
     * @param strArr
     * @return
     */
    private String spliceResult(String[] strArr) {
        String result = "";
        if (null != strArr && strArr.length > 0) {
            int i = 1;
            for (String item : strArr) {
                result += item;
                if (i != strArr.length) {
                    result += ",";
                }
                i++;
            }
        }
        return result;
    }

}
