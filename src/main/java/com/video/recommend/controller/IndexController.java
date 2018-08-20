package com.video.recommend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
}
