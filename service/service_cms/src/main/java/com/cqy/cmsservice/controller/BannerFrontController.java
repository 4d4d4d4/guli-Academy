package com.cqy.cmsservice.controller;


import com.cqy.cmsservice.entity.CrmBanner;
import com.cqy.cmsservice.service.CrmBannerService;
import com.cqy.commonutils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/cmsservice/bannerfront")
@CrossOrigin
public class BannerFrontController {
    @Autowired
    private CrmBannerService bannerService;

    //查询所有的banner
    @GetMapping("getAllBanner")
    public Result getAllBanner(){
        List<CrmBanner> list = bannerService.selectAllBanner();
        return Result.ok().data("bannerList",list);
    }
    @GetMapping
    public Result test(){
        return Result.ok().data("test",bannerService.test());
    }
}

