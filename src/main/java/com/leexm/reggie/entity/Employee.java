package com.leexm.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *  公共字段自动填充
 *      MybatisPlus公共字段填充，在插入或更新的时候为指定字段赋予指定的值
 *  实现步骤:
 *      在实体类的属性上加入@TableField注解，指定自动填充的策略
 *      按照框架要求编写元数据对象处理器，在此类中统一为公共字段赋值，此类需要实现MetaObjectHandler接口
 *  ThreadLocal:
 *      ThreadLocal维护变量，当线程想要操作该变量时，会为每个线程创建一个该变量的副本。每个线程可以独立地修改副本的值而不会影响
 *      其他线程内对应的副本，ThreadLocal为每个线程单独提供一份存储空间，以达到线程隔离的效果
 */
@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}
