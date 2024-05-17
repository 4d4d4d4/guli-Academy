package com.cqy.educenter.service;

import com.cqy.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqy.educenter.entity.vo.RegisterVo;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author cqy.java
 * @since 2023-10-23
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(UcenterMember member);

    //用户注册
    void register(RegisterVo registerVo);

    UcenterMember getOpenIdMember(String openid);
}
