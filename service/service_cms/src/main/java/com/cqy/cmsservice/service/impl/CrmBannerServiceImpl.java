package com.cqy.cmsservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqy.cmsservice.entity.CrmBanner;
import com.cqy.cmsservice.mapper.CrmBannerMapper;
import com.cqy.cmsservice.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author cqy.java
 * @since 2023-10-21
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    //查询所有banner
    @Override
    @Cacheable(value = "banner",key = "'selectIndexList'")
    public List<CrmBanner> selectAllBanner() {
        //根据id进行降序排列 显示排列后的前两条数据
        QueryWrapper<CrmBanner> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        //last方法 可以拼接sql语句
        wrapper.last(" limit 0,2");

        List<CrmBanner> list = baseMapper.selectList(null);

        return list;
    }

    @Override
    public List<CrmBanner> test() {
        return baseMapper.selectAll();
    }
}
