package com.hld.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hld.entity.News;
import com.hld.mapper.NewsMapper;
import com.hld.query.params.QueryOptions;
import com.hld.query.util.QueryUtils;
import com.hld.query.wrapper.CommonWrapper;
import com.hld.service.NewsService;
import com.hld.vo.NewsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author huald
 * @date 2019/8/20
 */
@Service
@Slf4j
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

    public List<Map> commonQuery(QueryOptions params) {
        CommonWrapper wrapper = new CommonWrapper(QueryUtils.splitOptions(params, NewsVO.class));
        String whereSql = QueryUtils.splitSql(wrapper);
        String relation = QueryUtils.getRelation(NewsVO.class);
        log.info("common query whereSql :[{}]", whereSql);
        return this.baseMapper.commonQuery(wrapper.getSqlSelect(), whereSql, relation);
    }

}
