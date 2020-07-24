package com.kk.api.website.service.impl;

import com.kk.api.website.mapper.ContactMapper;
import com.kk.api.website.entity.Contact;
import com.kk.api.website.service.IContactService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kk
 * @since 2020-06-19
 */
@Service
public class ContactServiceImpl extends ServiceImpl<ContactMapper, Contact> implements IContactService {

}
