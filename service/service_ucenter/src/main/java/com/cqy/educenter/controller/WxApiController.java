package com.cqy.educenter.controller;

import com.cqy.commonutils.JwtUtils;
import com.cqy.educenter.entity.UcenterMember;
import com.cqy.educenter.service.UcenterMemberService;
import com.cqy.educenter.utils.ConstantWXUtils;
import com.cqy.educenter.utils.HttpClientUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@Controller
@RequestMapping("/api/ucenter/wx")
@CrossOrigin
public class WxApiController {
    @Autowired
    private UcenterMemberService memberService;

    // 生成微信扫描二维码

    /**
     * 仍不不能完全理解运行机制
     * 现在的猜想是 baseUrl是设置请求微信登陆的接口 redirect_url的设置错误
     * 会导致页面显示url异常 应该是接口的服务器那边会做出判断这个回调地址是否有
     * 这个配置应该是在微信那边完成的 尚硅谷的老师应该是注册的8160这个端口以及固定的地址 所有只有这样写才可以
     * 成功调用到二维码
     * @return
     */
    @GetMapping("/login")
    public String getWxCode(){
        //固定地址 后面拼接参数 %s相当于占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect"+
                "?appid=%s" +
                 "&redirect_uri=%s" +
                 "&response_type=code" +
                 "&scope=snsapi_login" +
                 "&state=%s" +
                 "#wechat_redirect";

        //对redirect_url进行URLEncoder编码
        String redirectUrl = ConstantWXUtils.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String url = String.format(
                baseUrl,
                ConstantWXUtils.WX_OPEN_APP_ID,
                redirectUrl,
                "cqy");

        //请求微信地址
        return  "redirect:"+url;
    }

    /**
     * 二维码扫完会调用的回调地址 微信那边返回的 code是唯一值
     * @param code
     * @param state
     * @return
     */
    @GetMapping("/callback")
    public String callback(String code,String state){
        try {
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
            "?appid=%s" +
            "&secret=%s" +
            "&code=%s" +
            "&grant_type=authorization_code";
            //拼接参数 id 密钥 code
            baseAccessTokenUrl = String.format(
                    baseAccessTokenUrl,
                    ConstantWXUtils.WX_OPEN_APP_ID,
                    ConstantWXUtils.WX_OPEN_APP_SECRET,
                    code
            );
            //请求accessTokenUrl 得到返回的两个值access_token 和   openId
            //HttpClient
            String accessTokenInfo = HttpClientUtils.get(baseAccessTokenUrl);
            //从accessTokenInfo字符串获取两个值 access_token 和 openId
            Gson gson = new Gson();
            HashMap map = gson.fromJson(accessTokenInfo, HashMap.class);
            String accessToken =(String)map.get("access_token");
            String openid = (String)map.get("openid");

            //使用accessToken 和 openid 再去请求微信api 获取扫码人的信息
            String baseUserUrl = "https://api.weixin.qq.com/sns/userinfo"+
                    "?access_token=%s"+
                    "&openid=%s";
            baseUserUrl = String.format(baseUserUrl, accessToken, openid);
            String userInfo = HttpClientUtils.get(baseUserUrl);
            HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
            String nickname = (String) userInfoMap.get("nickname");
            String headimgurl = (String) userInfoMap.get("headimgurl");

            //把扫码人信息添加到数据库中
            //判断数据库表中是否存在相同的信息



            /**
             *
             {"openid":"o3_SC5yfJ21Th0_lETqHz0wTHDtc",
             "nickname":"白若星","
             sex":0,"language":"",
             "city":"","province":"",
             "country":"",
             "headimgurl":"https://thirdwx.qlogo.cn/mmopen\vi_32\rk6VQ16iatFoTFG3VnSDW14Irzp7mMZib86YmKIt0Bg8W9dYzbgIPvRjrXKsweVPbxbnXycqFXTrTeibUIExoJFbA/132",
             "privilege":[],
             "unionid":"oWgGz1GRi2unbp-bTI7hPa3fB4x0"}/////////
             *
             */
            //查询是否有相同的用户
            System.out.println(openid);
            UcenterMember selectSameMember =  memberService.getOpenIdMember(openid);
            if(selectSameMember == null){
                selectSameMember = new UcenterMember();
                selectSameMember.setOpenid(openid);
                selectSameMember.setAvatar(headimgurl);
                selectSameMember.setNickname(nickname);
                memberService.save(selectSameMember);
            }
            //使用jwt根据member对象生成token字符串
            String userInfoToken = JwtUtils.getJwtToken(selectSameMember.getId(), nickname);

            return "redirect:http://localhost:3000?token="+userInfoToken;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }



}
