package com.hld.query.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huald
 * @email 869701411@qq.com
 * @version 1.0.0
 * @Description: 查询封装类之排序封装
 * @date 2019年1月8日 下午1:17:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IOrderBy {

    private String orderByName;
    private OrderType orderByType;

}
