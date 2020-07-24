package com.kk.api.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kk.api.app.entity.User;
import com.kk.api.app.entity.vo.UserVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author kk
 * @since 2020-06-28
 */
public interface IUserService extends IService<User> {
	IPage<UserVO> pageList(Page<UserVO> page, Map<String, String> param);

	List<UserVO> list(Map<String, String> param);
}
