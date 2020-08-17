package com.hld.query.params;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author huald
 * @email 869701411@qq.com
 * @version 1.0.0
 * @Description: 查询封装类之过滤
 * @date 2018年12月26日 下午4:51:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class IFilter {

    private String filterName;

    /**
     * 时间格式化 dateFormat 不为空则是时间类型
     */
    private String dateFormat;
    /**
     * 过滤条件 枚举 FilterType
     */
    private FilterType filterType;

    private ICondition condition;

    /**
     * 当过滤filterName多个结果
     */
    private List<Object> filterValue;

    private List<IFilter> children;

}
