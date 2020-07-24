package com.kk.api.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kk.api.app.entity.TradeLog;
import com.kk.api.app.entity.vo.TradeLogVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author kk
 * @since 2020-07-01
 */
public interface TradeLogMapper extends BaseMapper<TradeLog> {
	List<Map<String, Object>> selectAirdropLine();

	IPage<TradeLogVO> selectPageByUserid(Page<TradeLog> page, Long userId);

	TradeLogVO selectVOById(Long id, Long userId);

	IPage<TradeLogVO> pageList(Page<TradeLogVO> page, @Param("param") Map<String, String> param);
}
