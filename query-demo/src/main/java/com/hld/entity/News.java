package com.hld.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author huald
 * @date 2019/8/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@TableName("t_news")
public class News {

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String title;

    @TableField()
    private String content;

    @TableField()
    private Long editUserId;

}
