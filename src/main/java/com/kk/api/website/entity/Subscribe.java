package com.kk.api.website.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.kk.api.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author kk
 * @since 2020-06-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
//@TableName("t_subscribe")
public class Subscribe extends BaseEntity {

    /**
     * Email
     */
    private String email;

    @TableField(exist = false)
    private LocalDateTime updateDate;
}
