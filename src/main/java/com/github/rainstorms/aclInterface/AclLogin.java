package com.github.rainstorms.aclInterface;

import com.github.rainstorms.domain.LoginResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 定义登录方式
 * 如：短信验证码，用户名密码，cookie
 */
public abstract class AclLogin {

    /**
     *  登录
     *      1.
     *
     * @param req
     * @param rsp
     * @param userService
     * @return
     */
    public abstract LoginResult login(HttpServletRequest req, HttpServletResponse rsp, AclUserService userService);
}
