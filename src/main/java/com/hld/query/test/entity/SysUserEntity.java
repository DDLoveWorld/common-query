package com.hld.query.test.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
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
@EqualsAndHashCode(callSuper = false)
@TableName("sys_user")
public class SysUserEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户名称
     */
    private String username;
    /**
     * 真实姓名
     */
    private String realName;

    private String cardId;
    /**
     * 微信id
     */
    private String wxUser;
    /**
     * 用户类别（1内部员工2外部人员）
     */
    private Integer userType;
    /**
     * 用户所在部门的id
     */
    private Long deptId;
    /**
     * 员工工号
     */
    private String workNo;
    /**
     * 状态，1：正常，0：冻结状态，2：删除
     */
    private Integer status;
    /**
     * 加密后的密码
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String password;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 座机号
     */
    private String telephone;
    /**
     * 性别（1男2女）
     */
    private Integer sex;
    /**
     * 生日月
     */
    private LocalDateTime birthday;
    /**
     * 政治面貌（0未知、1党员、2预备党员、3团员、4群众）
     */
    private Integer politicStatus;
    /**
     * 民族
     */
    private String national;
    /**
     * 婚姻状态(0:未知1：已婚2：离异3：丧偶4：未婚)
     */
    private Integer marriage;
    /**
     * 国籍/籍贯
     */
    private String nationality;
    /**
     * 户口性质(0:未知1外城、2外农、3本城、4本农)
     */
    private Integer regType;
    /**
     * 户籍所在地
     */
    private String registry;
    /**
     * 毕业时间
     */
    private LocalDateTime graduateDate;
    /**
     * 最高学历(0:未知1:博士后研究生2:博士研究生3:硕士研究生4:本科5:大专6:中专7:高中8:初中9:小学)
     */
    private Integer education;
    /**
     * 学位（0:未知1:博士后2:博士3:硕士4:学士5:其他）
     */
    private Integer academicDegere;
    /**
     * 毕业院校
     */
    private String collage;
    /**
     * 专业
     */
    private String professional;
    /**
     * 入职时间
     */
    private LocalDateTime entryDate;
    /**
     * 离职时间
     */
    private LocalDateTime leaveDate;
    /**
     * 首次参加工作时间
     */
    private LocalDateTime firstWork;
    /**
     * 工龄
     */
    private Integer workAge;
    /**
     * 职位
     */
    private String position;
    /**
     * 岗位类别（0未知1技术2行政）
     */
    private Integer postType;
    /**
     * 劳动合同到期日期
     */
    private LocalDateTime expireDate;
    /**
     * 劳动合同签订日期
     */
    private LocalDateTime signDate;
    /**
     * 转正日期
     */
    private LocalDateTime regularDate;
    /**
     * 紧急联系人电话
     */
    private String emergencyContact;
    /**
     * 家庭住址
     */
    private String homeAddress;
    /**
     * 邮箱
     */
    private String mail;
    /**
     * 开户行名称
     */
    private String bankName;
    /**
     * 备注
     */
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    private String comment;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String salt;

    private Integer superAdmin;
}