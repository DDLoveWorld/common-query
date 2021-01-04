package com.hld.query.params;

import com.hld.query.enums.AnnotationType;
import lombok.Data;

/**
 * @author huald
 * @date 2021/1/4
 */
@Data
public abstract class AbstractInfo {
    /**
     * 字段名称
     */
    private String fieldName;


    public abstract AnnotationType getAnnotationType();
}
