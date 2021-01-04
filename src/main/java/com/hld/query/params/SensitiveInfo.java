package com.hld.query.params;

import com.hld.query.enums.AnnotationType;
import com.hld.query.enums.SensitiveStrategy;
import com.hld.query.interfaces.IAnnotationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author huald
 * @date 2021/1/4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class SensitiveInfo extends AbstractInfo {

    private SensitiveStrategy sensitiveStrategy;

    @Override
    public AnnotationType getAnnotationType() {
        return AnnotationType.SENSITIVE;
    }
}
