package com.example.school.service.ucenter.service;

import com.example.school.service.ucenter.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.school.service.ucenter.entity.vo.LoginVo;
import com.example.school.service.ucenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author SWenW
 * @since 2023-02-10
 */
public interface MemberService extends IService<Member> {

    void register(RegisterVo registerVo);

    String login(LoginVo loginVo);

    /**
     * 根据openid返回用户信息
     * @param openid
     * @return
     */
    Member getByOpenid(String openid);
}
