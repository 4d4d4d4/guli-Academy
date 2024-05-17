package com.cqy.educenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqy.commonutils.JwtUtils;
import com.cqy.commonutils.MD5;
import com.cqy.commonutils.Result;
import com.cqy.educenter.entity.UcenterMember;
import com.cqy.educenter.entity.vo.RegisterVo;
import com.cqy.educenter.mapper.UcenterMemberMapper;
import com.cqy.educenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqy.servicebase.exceptionhandler.GuliException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author cqy.java
 * @since 2023-10-23
 */
@Service
@Slf4j
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate redisTemplate;

    //登录时候的方法
    @Override
    public String login(UcenterMember member) {
//        System.out.println(member);
        //获取登录邮箱和密码
        String email = member.getEmail();
        String password = member.getPassword();

        //邮箱和密码非空判断
        if(StringUtils.isEmpty(email) || StringUtils.isEmpty(password)){
            throw new GuliException(20001,"登录失败!");
        }
        //判断邮箱是否正确
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("email",email);
        UcenterMember memberByEmail = baseMapper.selectOne(wrapper);
        //判断对象是否为空
        if(memberByEmail == null){
            //没有手机号
            throw new GuliException(20001,"邮箱不存在");
        }
        if(memberByEmail.getIsDisabled()){
            //账号已被禁用
            throw  new GuliException(20001,"账号已被禁用");
        }
        //先将输入的密码加密 再和数据库中的密码比较
        //使用的加密方式是 MD5
//        System.out.println(MD5.encrypt(password));
        if(!MD5.encrypt(password).equals(memberByEmail.getPassword())){
            //密码错误
            throw new GuliException(20001,"密码或者账号错误");
        }

        //登录成功
        //生成token字符串，使用jwt工具类
//        log.error("member对象是:"+memberByEmail);
        String token = JwtUtils.getJwtToken(memberByEmail.getId(), memberByEmail.getNickname());

        return token;
    }

    //用户注册
    @Override
    public void register(RegisterVo registerVo) {
        //获取注册的数据
        String code = registerVo.getCode(); //验证码
        String email = registerVo.getEmail();//邮箱号码
        String nickname = registerVo.getNickname(); //用户昵称
        String password = registerVo.getPassword(); //用户密码

        //邮箱和密码是否为空
        if(StringUtils.isEmpty(email) || StringUtils.isEmpty(password)){
            throw new GuliException(20001,"邮箱或者密码不能为空");
        }

        //判断验证码
        //获取redis中的验证码
        System.out.println(code);
//        System.out.println(email);
        String codeInRedis =(String)redisTemplate.opsForValue().get(email);
        System.out.println(codeInRedis);
        if(!code.equals(codeInRedis)){
            throw new GuliException(20001,"验证码错误");
        }

        //判断邮箱不能重复
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("email",email);
        Integer integer = baseMapper.selectCount(wrapper);  //查询数据库中该邮箱的账号数量
        System.out.println("=================="+integer);
        if(integer>0){
            throw new GuliException(20001,"邮箱已存在");
        }

        //数据添加数据库中
        UcenterMember member = new UcenterMember();
        BeanUtils.copyProperties(registerVo,member);  //拷贝
        member.setPassword(MD5.encrypt(registerVo.getPassword()));  //存入数据库前对密码进行MD5加密
        member.setIsDisabled(false);
        member.setAvatar("https://img.zcool.cn/community/01a3865ab91314a8012062e3c38ff6.png@2o.png");
        baseMapper.insert(member);
    }

    //根据openid查询用户
    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }

}
