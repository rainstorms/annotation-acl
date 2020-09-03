package com.github.rainstorms.aclInterface;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 定义登录方式
 * 如：短信验证码，用户名密码，cookie
 */
public interface AclLogin {

    /**
     * 登录
     * 1.
     *
     * @param req
     * @param rsp
     * @return
     */
    boolean login(AclUserService userService, HttpServletRequest req, HttpServletResponse rsp);
}
