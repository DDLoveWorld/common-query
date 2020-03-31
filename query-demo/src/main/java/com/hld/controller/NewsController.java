package com.hld.controller;

import com.hld.mapper.NewsMapper;
import com.hld.query.params.QueryOptions;
import com.hld.query.util.QueryUtils;
import com.hld.query.util.Result;
import com.hld.service.NewsService;
import com.hld.vo.NewsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * 新闻controller
 *
 * @author huald
 * @date 2019/8/20
 */
@RestController
@RequestMapping("news")
@Slf4j
public class NewsController {
    @Resource
    NewsMapper newsMapper;

    @Autowired
    NewsService newsService;


    @PostMapping("/commonQuery")
    public Result commonQuery(@RequestBody QueryOptions params) {
        return QueryUtils.getResult(newsMapper, params, NewsVO.class);
    }


}
