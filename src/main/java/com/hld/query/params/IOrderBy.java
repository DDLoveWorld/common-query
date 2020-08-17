package com.hld.query.params;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
@Builder
@Accessors(chain = true)
public class IOrderBy {

    private String orderByName;
    private OrderType orderByType;

}
