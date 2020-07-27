package com.hld.query.test.vo;


import com.hld.query.annotations.TableFiledInfo;
import com.hld.query.annotations.TableRelations;
import lombok.Data;

/**
 * @author huald huald@ibuaa.com
 * @since 1.0.0 2019-09-03
 */
@Data
@TableRelations(mRelation = "sys_user T", mTableName = "sys_user", mTableAlias = "T")
public class SysUserVO {
    private static final long serialVersionUID = 1L;

    private final static String RELATION_DEPT = " left join sys_dept A on A.id = T.dept_id";

    /**
     * 用户id
     */
    @TableFiledInfo()
    private Long id;

    /**
     * 登录用户ID
     */
    private Long loginId;

    /**
     * 用户名称
     */
    @TableFiledInfo(filedSql = "select name from login_user B where B.id = T.login_id")
    private String username;
    /**
     * 真实姓名
     */
    @TableFiledInfo()
    private String realName;

    /**
     * 用户所在部门的id
     */
    @TableFiledInfo()
    private Long deptId;

    @TableFiledInfo(relation = RELATION_DEPT, tableName = "sys_dept", tableAlias = "A", filedName = "name")
    private String deptName;

}