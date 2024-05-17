package com.cqy.cmsservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqy.cmsservice.entity.CrmBanner;
import com.cqy.cmsservice.service.CrmBannerService;
import com.cqy.commonutils.Result;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author cqy.java
 * @since 2023-10-21
 */
@RestController
@RequestMapping("/cmsservice/banneradmin")
@CrossOrigin
public class BannerAdminController {

    @Autowired
    private CrmBannerService bannerService;

    //查询banner列表
    @GetMapping
    public Result getBannerList(){
        List<CrmBanner> bannerList = bannerService.list(null);
        return Result.ok().data("bannerList",bannerList);
    }

    //根据id查询banner
    @GetMapping("/{id}")
    public Result getBannerList(@PathVariable("id") String id){
        CrmBanner banner = bannerService.getById(id);
        return Result.ok().data("banner",banner);
    }

    @PostMapping("pageBanner/{current}/{size}")
    public Result pageBannerByCondition(@RequestBody(required = false) CrmBanner banner, @PathVariable("current") Long current,@PathVariable("size") Long size){
        String title = banner.getTitle();

        IPage<CrmBanner> iPage = new Page<>();
        iPage.setCurrent(current);
        iPage.setSize(size);
        QueryWrapper<CrmBanner> wrapper = new QueryWrapper<>();
        if(banner!=null) {
            wrapper.like(title != null && title != "", "title", title);
            wrapper.orderByDesc("sort");
            wrapper.orderByDesc("gmt_create");
        }
        IPage<CrmBanner> page = bannerService.page(iPage, wrapper);
        List<CrmBanner> records = page.getRecords();
        long total = page.getTotal();
        long pages = page.getPages();
        return Result.ok().data("records",records).data("total",total).data("pages",pages);
    }

    @PostMapping("/addBanner")
    public Result addBanner(@RequestBody CrmBanner banner){
        boolean save = bannerService.save(banner);
        return save?Result.ok():Result.error();
    }

    @PutMapping("/editBanner")
    public Result updateBanner(@RequestBody CrmBanner banner){
        boolean falg = bannerService.updateById(banner);
        return falg?Result.ok():Result.error();
    }

    @DeleteMapping("/deleteBanner/{id}")
    public Result deleteBannerById(@PathVariable String id){
        boolean flag = bannerService.removeById(id);
        return flag?Result.ok():Result.error();
    }

}

