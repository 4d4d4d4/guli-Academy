package com.cqy.oss.controller;

import com.cqy.commonutils.Result;
import com.cqy.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/eduoss/fileoss")
@CrossOrigin
public class OssController {

    @Autowired
    private OssService ossService;
    //上传头像方法
    @PostMapping
    public Result uploadOssFile(MultipartFile file) throws IOException {
        //获取上传的文件
        //方法返回上传到oss的路径
        String path = ossService.uploadAvatar(file);
        return Result.ok().data("url",path);
    }
}
