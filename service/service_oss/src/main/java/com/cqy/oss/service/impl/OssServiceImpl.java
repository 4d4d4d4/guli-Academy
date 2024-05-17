package com.cqy.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.cqy.oss.utils.ConstantPropertiesUtils;
import com.cqy.oss.service.OssService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    public static final String ALi_DOMAIN ="https://cqy521.oss-cn-beijing.aliyuncs.com/"; //阿里云访问地址
    @Override
    public String uploadAvatar(MultipartFile file) throws IOException {
        String endpoint = ConstantPropertiesUtils.END_POINT;    //地域节点
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        //创建oss客户端对象
        OSS ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret); 
        
        //上传文件的输入流
        InputStream inputStream = file.getInputStream();

        //获取对象的原始名
        String originalFilename = file.getOriginalFilename();

        int index = originalFilename.lastIndexOf('.'); //获取后缀名前面那个点的位置
//        String fileName = originalFilename.substring(index);//获取文件的名称（不带后缀名）;
        //避免重复 使用uuid 作为文件的新名称
        String uuidName = UUID.randomUUID().toString().replace("-","");
        String extension = originalFilename.substring(index,originalFilename.length()); //获取扩展名
        //把文件按照日期分类
        // 2023/10/9 + /uuId + .jpg 会按照日期进行文件夹分类
        String datePath = new DateTime().toString("yyyy/MM/dd");
        String newFileName =datePath +"/"+ uuidName + extension;



//        System.out.println(extension);
//        System.out.println(bucketName);
//        System.out.println(newFileName);

        //调用oss客户端的上传方法
        ossClient.putObject(bucketName,newFileName,inputStream);

        //关闭oss客户端
        ossClient.shutdown();

        //返回访问地址
        return ALi_DOMAIN+newFileName;
    }
}
