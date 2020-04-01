package com.hld.vo;

import com.hld.query.annotations.TableFiledInfo;
import com.hld.query.annotations.TableRelations;
import lombok.Data;

/**
 * 图形展示类
 *
 * @author huald
 * @date 2019/8/20
 */
@Data
@TableRelations(relation = "t_news T left join sys_user A on A.id = T.edit_user_id", mTableName = "t_news")
public class NewsVO {

    @TableFiledInfo()
    private String title;

    @TableFiledInfo()
    private String content;

    @TableFiledInfo()
    private Long editUserId;

    @TableFiledInfo(tableName = "sys_user", tableAlias = "A", filedName = "username")
    private String editUserName;

    @TableFiledInfo()
    private Long deptId;
}
