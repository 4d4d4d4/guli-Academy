package com.cqy.educenter.controller;


import com.cqy.commonutils.JwtUtils;
import com.cqy.commonutils.Result;
import com.cqy.educenter.entity.UcenterMember;
import com.cqy.educenter.entity.vo.RegisterVo;
import com.cqy.educenter.service.UcenterMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author cqy.java
 * @since 2023-10-23
 */
@RestController
@RequestMapping("/educenter/member")
@CrossOrigin
@Slf4j
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    //登录
    @PostMapping("/login")
    public Result loginUser(@RequestBody UcenterMember member){
        //调用service中的方法实现登录
        //返回token值，使用jwt生成
        String token = memberService.login(member);
        return Result.ok().data("token",token);

    }
    //注册
    @PostMapping("/register")
    public Result registerUser(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return Result.ok();
    }

    //TODO 后期改成切面类进行验证
    //根据token获取用户的信息
    @GetMapping("getMemberInfo")
    public Result getMemberInfo(HttpServletRequest request){
        //调用jwt工具类的方法，根据request对象获取头信息 返回客户id
        String memberIdByJwtToken = JwtUtils.getMemberIdByJwtToken(request);
        UcenterMember member = memberService.getById(memberIdByJwtToken);
        boolean flag = !StringUtils.isEmpty(member);
        return flag?Result.ok().data("member",member):Result.error().message("用户不存在");
    }

}

