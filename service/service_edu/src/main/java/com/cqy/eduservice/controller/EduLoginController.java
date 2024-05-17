package com.cqy.eduservice.controller;

import com.cqy.commonutils.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eduservice/user")
@CrossOrigin  //解决跨域
public class EduLoginController {
    //login
    @PostMapping("login")
    public Result login() {
        return Result.ok().data("token","admin");
    }
    //info
    @GetMapping("info")
    public Result info() {
        String avatar = "https://img.zcool.cn/community/0127c0577e00620000012e7e12da0e.gif";
        return Result.ok().data("avatar", avatar).data("roles", "[admin]").data("name", "admin");
    }
}
