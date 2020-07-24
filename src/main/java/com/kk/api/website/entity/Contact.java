package com.kk.api.website.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.kk.api.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author kk
 * @since 2020-06-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
//@TableName("t_contact")
public class Contact extends BaseEntity {

    /**
     * 公司或社区名
     */
    private String company;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系方式
     */
    private String method;

    /**
     * 备注
     */
    private String remarks;

    @TableField(exist = false)
    private LocalDateTime updateDate;
}
