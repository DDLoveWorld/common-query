package com.hld.query.test.vo;


import com.hld.query.annotations.TableFiledInfo;
import com.hld.query.annotations.TableRelations;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author huald huald@ibuaa.com
 * @since 1.0.0 2019-09-03
 */
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableRelations(mRelation = "sys_user T", mTableName = "sys_user")
public class SysUserVO {
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableFiledInfo()
    private Long id;
    /**
     * 用户名称
     */
    @TableFiledInfo(filedSql = "select name from  sys_dept A where  A.id = T.dept_id")
    private String username;
    /**
     * 真实姓名
     */
    @TableFiledInfo()
    private String realName;
    /**
     * 微信id
     */
    @TableFiledInfo()
    private String wxUser;
    /**
     * 用户类别（1内部员工2外部人员）
     */
    @TableFiledInfo()
    private Integer userType;
    /**
     * 用户所在部门的id
     */
    @TableFiledInfo()
    private Long deptId;

    @TableFiledInfo(relation = "left join sys_dept A on A.id = T.dept_id", tableName = "sys_dept", tableAlias = "A", filedName = "name")
    private String deptName;
    /**
     * 员工工号
     */
    @TableFiledInfo(relation = "left join sys_menu B on B.id = T.dept_id", tableName = "sys_menu", tableAlias = "B", filedName = "work_num")
    private String workNo;
    /**
     * 状态，1：正常，0：冻结状态，2：删除
     */
    @TableFiledInfo()
    private Integer status;
    /**
     * 加密后的密码
     */
    private String password;
    /**
     * 手机号
     */
    @TableFiledInfo()
    private String mobile;
    /**
     * 座机号
     */
    @TableFiledInfo()
    private String telephone;
    /**
     * 性别（1男2女）
     */
    @TableFiledInfo()
    private Integer sex;
    /**
     * 生日月
     */
    @TableFiledInfo()
    private LocalDateTime birthday;
    /**
     * 政治面貌（0未知、1党员、2预备党员、3团员、4群众）
     */
    @TableFiledInfo()
    private Integer politicStatus;
    /**
     * 民族
     */
    @TableFiledInfo()
    private String national;
    /**
     * 婚姻状态(0:未知1：已婚2：离异3：丧偶4：未婚)
     */
    @TableFiledInfo()
    private Integer marriage;
    /**
     * 国籍/籍贯
     */
    @TableFiledInfo()
    private String nationality;
    /**
     * 户口性质(0:未知1外城、2外农、3本城、4本农)
     */
    @TableFiledInfo()
    private Integer regType;
    /**
     * 户籍所在地
     */
    @TableFiledInfo()
    private String registry;
    /**
     * 毕业时间
     */
    @TableFiledInfo()
    private LocalDateTime graduateDate;
    /**
     * 最高学历(0:未知1:博士后研究生2:博士研究生3:硕士研究生4:本科5:大专6:中专7:高中8:初中9:小学)
     */
    @TableFiledInfo()
    private Integer education;
    /**
     * 学位（0:未知1:博士后2:博士3:硕士4:学士5:其他）
     */
    @TableFiledInfo()
    private Integer academicDegere;
    /**
     * 毕业院校
     */
    @TableFiledInfo()
    private String collage;
    /**
     * 专业
     */
    @TableFiledInfo()
    private String professional;
    /**
     * 入职时间
     */
    @TableFiledInfo()
    private LocalDateTime entryDate;
    /**
     * 离职时间
     */
    @TableFiledInfo()
    private LocalDateTime leaveDate;
    /**
     * 首次参加工作时间
     */
    @TableFiledInfo()
    private LocalDateTime firstWork;
    /**
     * 工龄
     */
    @TableFiledInfo()
    private Integer workAge;
    /**
     * 职位
     */
    @TableFiledInfo()
    private String position;
    /**
     * 岗位类别（0未知1技术2行政）
     */
    @TableFiledInfo()
    private Integer postType;
    /**
     * 劳动合同到期日期
     */
    @TableFiledInfo()
    private LocalDateTime expireDate;
    /**
     * 劳动合同签订日期
     */
    @TableFiledInfo()
    private LocalDateTime signDate;
    /**
     * 转正日期
     */
    @TableFiledInfo()
    private LocalDateTime regularDate;
    /**
     * 紧急联系人电话
     */
    @TableFiledInfo()
    private String emergencyContact;
    /**
     * 家庭住址
     */
    @TableFiledInfo()
    private String homeAddress;
    /**
     * 邮箱
     */
    @TableFiledInfo()
    private String mail;
    /**
     * 开户行名称
     */
    @TableFiledInfo()
    private String bankName;
    /**
     * 备注
     */
    @TableFiledInfo()
    private String comment;

    @TableFiledInfo()
    private String cardId;
}