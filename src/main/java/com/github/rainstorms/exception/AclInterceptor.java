package com.github.rainstorms.exception;

import com.github.rainstorms.aclInterface.AclLogin;
import com.github.rainstorms.aclInterface.AclUserService;
import com.github.rainstorms.annotation.Acl;
import com.github.rainstorms.domain.AclContext;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
@Builder
public class AclInterceptor implements HandlerInterceptor {
    private AclLogin[] aclLogins;
    private AclUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse rsp, Object handler) {
        for (val aclLogin : aclLogins) {
            switch (aclLogin.login(req, rsp, userService)) {
                case ContinueNext:
                    continue;
                case BreakTrue:
                    return true;
                case BreakFalse:
                    return false;
            }
        }

        return authenticate(req, handler);
    }

    /**
     * 处理acl注解
     *
     * @param r
     * @param h
     * @return
     */
    private boolean authenticate(HttpServletRequest r, Object h) {
        if (r.getServletPath().startsWith("/error"))
            return false;

        if (!(h instanceof HandlerMethod)) return false;

        val handlerMethod = (HandlerMethod) h;
        if (handlerMethod.getMethod().getDeclaringClass() == BasicErrorController.class)
            return false;

        val acl = getAcl(handlerMethod.getMethod());
        if (acl == null) return true;

        if (!AclContext.isLogined())
            return sendError(419, "Login required for " + h);

        if (acl.roles().length > 0 && !AclContext.hasAnyRoles(splitElements(acl.roles())))
            return sendError(450, "Any role of  "
                    + Arrays.toString(acl.roles()) + " required for " + h
                    + ", current user is " + AclContext.getUser());

        return true;
    }

    private static boolean sendError(int statusCode, String msg) {
        throw new AclException(msg, statusCode);
    }

    private static boolean isAjaxRequest(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(header);
    }

    private static final Splitter splitter = Splitter.on(',').trimResults().omitEmptyStrings();

    private static String[] splitElements(String[] str) {
        ArrayList<String> strList = Lists.newArrayList();

        for (val s : str) {
            strList.addAll(splitter.splitToList(s));
        }

        return strList.toArray(new String[0]);
    }

    private Acl getAcl(Method method) {
        val acl = method.getAnnotation(Acl.class);
        if (acl != null) return acl;

        return method.getDeclaringClass().getAnnotation(Acl.class);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        AclContext.teardown();
    }
}
