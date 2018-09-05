package com.video.recommend.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 描述:
 *    index页面返回数据model
 * @author zhangzl
 * @create 2018-09-01 下午1:52
 */
@Getter
@Setter
public class IndexReturn {

    Integer videoId;

    String videoUrl;

    String videoSummary;

    String videoName;

    String videoPic;

}
