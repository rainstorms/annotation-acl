package com.github.rainstorms.aclInterfaceImpl;

import com.github.rainstorms.aclInterface.AclLogin;
import com.github.rainstorms.aclInterface.AclUserService;
import com.github.rainstorms.domain.AclContext;
import com.github.rainstorms.domain.AclCookies;
import com.github.rainstorms.domain.LoginResult;
import lombok.val;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 查找cookie，自动登录。
 */
public class CookieAclLogin extends AclLogin {

    @Override
    public LoginResult login(HttpServletRequest req, HttpServletResponse rsp, AclUserService userService) {
        val userId = AclCookies.readAuthc(req);
        if (null != userId) {
            val user = userService.findUser(userId);
            if (null != user) {
                user.setUserRoles(userService.findUserRole(userId));
                AclContext.setUser(user);
            } else AclCookies.clearAuthc(rsp);
        }

        return LoginResult.ContinueNext;
    }

}
