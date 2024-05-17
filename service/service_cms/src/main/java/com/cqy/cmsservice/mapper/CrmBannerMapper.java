package com.cqy.cmsservice.mapper;

import com.cqy.cmsservice.entity.CrmBanner;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 首页banner表 Mapper 接口
 * </p>
 *
 * @author cqy.java
 * @since 2023-10-21
 */
//@Mapper
public interface CrmBannerMapper extends BaseMapper<CrmBanner> {
    List<CrmBanner> selectAll();
}
