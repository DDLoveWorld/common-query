@[TOC](接口参数自定义查询)


这是一款基于Mybatis-plus插件查询功能的二次开发，让你的查询变得更加简单，简洁，方便和灵活。从此告别和前端人员的频繁沟通！！

## 引言
   大家都了解一个简单完整SQL的样子是以下形式的：


```sql
SELECT ID,NAME FROM T_USER WHERE ID = 1;
```
上面是一个最简单的SQL查询语句了。（查询用户ID等于1的用户，返回用户的ID和名称）
本人把一个SQL分成了以下几个模块，那么给大家来拆分下上面的结构：

 1. **关键字**
     `SELECT、FROM 、WHERE` 上面的这三个单词都是关键字了。
  2. **返回列**
    `ID , NAME` 是对查询到的结果进行返回，标明要返回的列名数据
  3. **表间关系**
    `T_USER` 这个就是表间关系，当然这个是最简单的表间关系了，仅仅是一个单表。实际开发中肯定是有很多业务是要多表关联查询了。（这个也是在实际设计中也是最难平衡的一个点了。为什么这么说后续我会进行解释）
   4. **查询条件**
    `ID = 1  WHERE`后面都是筛选条件了，这里也是比较核心的地方（当然了，这个也不难，因为咱把这部分交给了Mybatis-plus了）

## 项目说明
 - 本项目是一款工具类，自定义查询功能。后端不在关注前端传递的参数是否合理，也不在去关心前端想要什么数据，一个业务一个查询接口，让你不在烦恼，不在有无数个查询接口。

## 项目特点
   - 支持大多数查询拼接条件
   - 支持自定义返回查询列
   - 支持多表联合查询
   - 支持分组查询

   ## 项目优势
   - 适合表单业务
   - 规范查询参数格式
   - 减少前后端沟通时间
   - 减少接口数量
   - 减少接口出错率
   - **减少开发时长**
   - 自定义查询条件
   - 自定义返回数据
   - 自定义返回数据格式

## 项目弊端
任何事物都是有弊端的，只是看你的取舍了。

- 目前还支持简单业务查询(聚合运算或者复杂统计类的接口还没有实现)
- 灵活是相对的，查询列的最大限度由后端限制
- 不同业务还是要写不同接口（也有那种各种业务都用一个接口搞定的）

## 项目结构
  ![项目结构](https://img-blog.csdnimg.cn/20200511193331469.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L015X0phY2s=,size_16,color_FFFFFF,t_70)
## 上手操作
   1. 在自有项目引用，本项目已上传到Maven中央参考



```yaml
        <dependency>
            <groupId>com.github.DDLoveWorld</groupId>
            <artifactId>common-query</artifactId>
            <version>1.2.1</version>
        </dependency>
```

  下面我就用一个用户管理的业务来给大家具体展示下如何使用：
   2. **建立一个用户管理查询控制VO类 SysUserVO**

```java
package com.hld.query.test.vo;


import com.hld.query.annotations.TableFiledInfo;
import com.hld.query.annotations.TableRelations;
import lombok.Data;

/**
 * @author huald 3107438717@qq.com
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
```
上面这个类有以下几点作用：
- **限制可查询列和可返回列**
      也就是说，在上面SysUserVO 类中有6个字段。那么对于前端人员来说。只允许前端人员用以上6个字段进行查询以及数据返回。当然具体想要用哪个字段以及用几个，完全由前端人员来自定义了。其实这样做的原因主要是为第二和第三点作用服务的。
- **用于表间关系获取及拼接**
    获取表与表，字段与字段之间的关联，最终获取需要的SQL拼接。下面我来详细说下：
    1. `@TableRelations`  类表间关系注解。它主要的作用是规定主表的表名、别名、主表表间关系
        这个注解填写完善了，那么用上面SysUserVO.class 的示例。那么在隶属于sys_user这个表内的字段就无需在字段属性上填写部分属性了。
     2. `@TableFiledInfo()` 字段表间关系注解。它主要是用于告知哪些字段可以被查询使用。同时也是为非主表字段做一定的配置。
     **relation** ：此属性是用于拼接表间关系。
                     参考示例：字段deptName是不属于用户sys_user表的字段，而是sys_dept表的字段。那么首先就要先用此属性把sys_user表和sys_dept表关联起来。
       **tableName**  ：从表表名。
       **tableAlias**  ：从表别名。
       **filedName**  ：这个属性很重要，这个属性是用于字段名称与数据库字段名称不匹配时候的配置。如果配置了则使用此值进行SQL拼接
- **用于安全控制**
    安全性一直是避不开的一个话题。引言、项目缺点中也有提及。之前也有开发过一个版本。开放性特别的高，返回列，查询参数，以及表间关系，数据库表名和字段都是需要前端人员知晓的。这种状态下不可避免的会让我们的表设计泄露出去了，甚至在SQL拼接中有SQL注入的风险（当然也会有放SQL注入的校验，但也是不太能保证全部拦截成功【道高一尺魔高一丈】）。所以本人也综合考虑之后做出一定的灵活性舍弃，做到安全性能更高一点。
    这也就是限制查询列和返回列的原因了。前端人员只知道各个业务字段，无需知道表名甚至前端知道的字段也不是数据库字段。这都是可以配置的！
3.  **建立提供给前端的业务查询接口**

```java
/**
     * 自定义查询
     *
     * @param params
     * @return
     */
    @PostMapping("commonQuery")
    public Result commonQuery(@RequestBody QueryOptions params) {
        return QueryUtils.getResult(sysUserMapper, params, SysUserVO.class);

    }
```

**特殊说明下：**
各个业务的dao层需要继承我这边基于MybatisPlus插件中实现的CommonMapper
具体可自行参考源码！

对于开发来说，上面的三步就已经算结束了！这样是不是非常nice，再加上一个代码生成器。真是能不能在爽一下了！

**有了上面的查询接口，那么对于用户管理这个业务基本就不在需要其他任何的查询接口了！我就说后端们你们happy不！**
下面来给大家仔细说下：
## QueryOptions  接口参数封装类
解释说明已经在代码中做了描述，这里就不赘述了。最后一个**firstSql**参数慎用。然后**IOrderBy**、**IFilter**和**groupBys**就去下载源码看吧！
## 已支持查询条件

```java
    EQUAL("=", 0),
    UN_EQUAL("<>", 1),
    IN("关联", 2),
    NO_IN("不关联", 3),
    LIKE("LIKE", 4),
    NO_LIKE("NOT LIKE", 5),
    GE(">=", 6),
    LE("<=", 7),
    LT("<", 8),
    GT(">", 9),
    NOT_NULL("NOT_NULL", 10),
    NULL("NULL", 11),
    BETWEEN("BETWEEN", 12),
    NOT_BETWEEN("NOT_BETWEEN", 13),
    LEFT_LIKE("LEFT_LIKE", 14),
    RIGHT_LIKE("RIGHT_LIKE", 15);
```

   ## 已支持数据库
     - MySQL
     - Oracle

## 自定义返回数据格式
什么叫自定义返回数据格式呢？当和前端打交道的时候，各位是不是有过这种场景：对于一篇新闻文章的业务，这篇文章有很多的标签。那么这些标签可能在后台数据库的存储形式是，
|id|tags  |
|--|--|
|  1| 体育,足球,欧洲杯 |

对于数据库来说就是一个字符串。自定义查询直接就返回这个字符串了。但是前端就要数组。可不是`"[体育,足球,欧洲杯]"`这样的json。而是真正的数组或者集合`[体育,足球,欧洲杯]`。可能单独写都ok，那么如何在自定义查询中加上此功能呢？请看下面道来：
只需一个注解@FormatSwitch 搞定

```java
package com.hld.query.annotations;

import com.hld.query.enums.SwitchType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 此注解用于返回数据时进行数据结构转换，定义
 *
 * @author huald
 * @date 2020/10/13
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FormatSwitch {
    /**
     * 分隔符
     * 例： 1,2,3  采用逗号分隔
     * 默认分隔符 ,
     * 分隔符，当type = STR_TO_ARRAY 时使用生效
     *
     * @return
     */
    String separation() default ",";

    /**
     * 转换类型
     *
     * @return
     */
    SwitchType type() default SwitchType.STR_TO_ARRAY;

    /**
     * 需要转换的对象类型
     * 当 type = JSON_TO_OBJ,JSON_TO_LIST 生效
     */
    Class<?> cls() default Object.class;
}

```
只需要在VO类中把需要进行数据格式转换的字段加上此注解就ok了。
当然目前可以有以下四种转换类型：
具体例子下面都有

```java
 /**
     * 字符串转数组，采用分隔符
     * 例： 1,2,3 -> [1,2,3]
     */
    STR_TO_ARRAY,

    /**
     * 字符串数据转数组
     * 例： '[1,2,3]' -> [1,2,3]
     */
    JSON_TO_ARRAY,

    /**
     * 字符串转对象
     * 例:  '{}' -> {}
     */
    JSON_TO_OBJ,

    /**
     * 字符串对象数据转集合
     * 例: '[{},{}]' -> [{},{}]
     */
    JSON_TO_LIST;
```

## QueryUtils   查询工具类
工具类没有太多说的了。后续可根据自己业务需求自己做工具类。比如返回接口集的封装格式。我这边统一由Result类返回。

## Maven配置
     项目已发布到maven中央仓库，项目可直接引用

```html
<!-- https://mvnrepository.com/artifact/com.github.DDLoveWorld/common-query -->
<dependency>
    <groupId>com.github.DDLoveWorld</groupId>
    <artifactId>common-query</artifactId>
    <version>1.3.1</version>
</dependency>

```

## 源码地址
   [源码地址](https://github.com/DDLoveWorld/common-query)

# 项目引用
 本项目已上传到Maven中央仓库

    <dependency>
        <groupId>com.github.DDLoveWorld</groupId>
        <artifactId>common-query</artifactId>
        <version>1.3.7</version>
    </dependency>

