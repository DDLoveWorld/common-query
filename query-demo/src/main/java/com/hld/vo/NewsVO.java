package com.hld.vo;

import com.techcomer.common.mybatisplus.entity.BaseEntity;
import com.techcomer.common.mybatisplus.query.annotations.TableFiledInfo;
import com.techcomer.common.mybatisplus.query.annotations.TableRelations;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 图形展示类
 *
 * @author huald
 * @date 2019/8/20
 */
@Data
@ApiModel
@TableRelations(relation = "t_news T left join sys_user A on A.id = T.edit_user_id", mTableName = "t_news")
public class NewsVO extends BaseEntity {

    @TableFiledInfo()
    @ApiModelProperty("新闻标题")
    private String title;

    @TableFiledInfo()
    @ApiModelProperty("新闻内容")
    private String content;

    @TableFiledInfo()
    @ApiModelProperty("新闻编辑人ID")
    private Long editUserId;

    @ApiModelProperty("新闻编辑人名字")
    @TableFiledInfo(tableName = "sys_user", tableAlias = "A", filedName = "username")
    private String editUserName;

    @TableFiledInfo()
    private Long deptId;
}
