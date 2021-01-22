package com.hld.query.params;

import com.hld.query.enums.AnnotationType;
import com.hld.query.enums.SwitchType;
import com.hld.query.interfaces.IAnnotationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 需要转换数据格式的字段信息
 *
 * @author huald
 * @date 2020/10/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class FormatSwitchInfo extends AbstractInfo {

    /**
     * 分隔符，当type = STR_TO_ARRAY 时使用生效
     */
    private String separation;

    private SwitchType type;

    private Class<?> cls;

    private Boolean handleEmptyAndNull;


    @Override
    public AnnotationType getAnnotationType() {
        return AnnotationType.SWITCH_FORMAT;
    }
}
