package com.github.rainstorms.aclInterfaceImpl;

import com.github.rainstorms.aclInterface.AclLogin;
import com.github.rainstorms.aclInterface.AclUserService;
import com.github.rainstorms.domain.AclContext;
import com.github.rainstorms.domain.AclCookies;
import lombok.val;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 查找cookie，自动登录。
 */
public class CookieAclLogin implements AclLogin {

    @Override
    public boolean login(AclUserService userService, HttpServletRequest req, HttpServletResponse rsp) {
        val userId = AclCookies.readAuthc(req);
        if (null != userId) {
            val user = userService.findUser(userId);
            if (null != user) {
                user.setUserRoles(userService.findUserRole(userId));
                AclContext.setUser(user);
            } else AclCookies.clearAuthc(rsp);
            return true;
        }

        return false;
    }

}
