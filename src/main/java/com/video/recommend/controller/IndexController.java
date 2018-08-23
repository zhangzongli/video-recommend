package com.video.recommend.controller;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.Rserve.RConnection;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 *    index controller
 * @author zhangzl
 * @create 2018-08-20 下午9:43
 */
@Controller
public class IndexController {

    @GetMapping("/")
    public String index()  {
        return "index";
    }

    @GetMapping("/a")
    @ResponseBody
    public void test() {
        RConnection rConnection = null;
        try {
            rConnection = new RConnection("localhost", 6311);
//            List<Double[]> dataList = new ArrayList<Double[]>() {
//                {
//                    add(new Double[]{1d, 101d, 5d});
//                    add(new Double[]{1d, 102d, 3d});
//                    add(new Double[]{1d, 103d, 2.5d});
//                    add(new Double[]{2d, 101d, 2d});
//                    add(new Double[]{2d, 102d, 2.5d});
//                    add(new Double[]{2d, 103d, 5d});
//                    add(new Double[]{2d, 104d, 2d});
//                    add(new Double[]{3d, 101d, 2.5d});
//                    add(new Double[]{3d, 104d, 4d});
//                    add(new Double[]{3d, 105d, 4.5d});
//                    add(new Double[]{3d, 107d, 5d});
//                    add(new Double[]{4d, 101d, 5d});
//                    add(new Double[]{4d, 103d, 3d});
//                    add(new Double[]{4d, 104d, 4.5d});
//                    add(new Double[]{4d, 106d, 4d});
//                    add(new Double[]{5d, 101d, 4d});
//                    add(new Double[]{5d, 102d, 3d});
//                    add(new Double[]{5d, 103d, 2d});
//                    add(new Double[]{5d, 104d, 4d});
//                    add(new Double[]{5d, 105d, 3.5d});
//                    add(new Double[]{5d, 106d, 4d});
//                }
//            };
            double[] uidArr = {1d,
                    1d,
                    1d,
                    2d,
                    2d,
                    2d,
                    2d,
                    3d,
                    3d,
                    3d,
                    3d,
                    4d,
                    4d,
                    4d,
                    4d,
                    5d,
                    5d,
                    5d,
                    5d,
                    5d,
                    5d};
            double[] itemArr = {101d,
                    102d,
                    103d,
                    101d,
                    102d,
                    103d,
                    104d,
                    101d,
                    104d,
                    105d,
                    107d,
                    101d,
                    103d,
                    104d,
                    106d,
                    101d,
                    102d,
                    103d,
                    104d,
                    105d,
                    106d};
            double[] prefArr = {5d,
                    3d,
                    2.5d,
                    2d,
                    2.5d,
                    5d,
                    2d,
                    2.5d,
                    4d,
                    4.5d,
                    5d,
                    5d,
                    3d,
                    4.5d,
                    4d,
                    4d,
                    3d,
                    2d,
                    4d,
                    4.5d,
                    4d};
            rConnection.eval("source('~/Downloads/R/xietiaoguolv.R')");

            rConnection.eval("setwd(\"/Users/zhangzl/Downloads/R\")");
            rConnection.assign("uid",uidArr);
            rConnection.assign("iid",itemArr);
            rConnection.assign("pref",prefArr);
            rConnection.eval("data <- data.frame(uid, iid, pref)");
            rConnection.eval("NeighborHodd_num <- 2");
            rConnection.eval("Recommender_num <- 3");
            rConnection.eval("myM <- FileDataModel(data)");
            rConnection.eval("myS <- EuclideanDistanceSimilarity(myM)");
            rConnection.eval("myN <- NearestNUserNeigborhood(myS,NeighborHodd_num)");
            rConnection.eval("R1 <- UserBasedRecommender(1, Recommender_num,myM,myS,myN)");

            System.out.println(rConnection.eval("R1"));

        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            rConnection.close();
        }
    }
}
