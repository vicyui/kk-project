package com.okboss.api.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.okboss.api.app.entity.User;
import com.okboss.api.app.entity.vo.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author kk
 * @since 2020-06-28
 */
public interface UserMapper extends BaseMapper<User> {
	IPage<UserVO> pageList(Page<UserVO> page, @Param("param") Map<String, String> param);

	List<UserVO> list(@Param("param") Map<String, String> param);
}
