package com.github.rainstorms.exception;

import com.github.rainstorms.aclInterface.AclLogin;
import com.github.rainstorms.aclInterface.AclUserService;
import com.github.rainstorms.annotation.Acl;
import com.github.rainstorms.annotation.LoginIgnore;
import com.github.rainstorms.domain.AclContext;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

@Slf4j @Data @AllArgsConstructor @NoArgsConstructor
public class AclInterceptor implements HandlerInterceptor {
    public AclLogin[] aclLogins;
    public AclUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse rsp, Object handler) {
        for (val aclLogin : aclLogins) {
            if (aclLogin.login(userService, req, rsp)) // 登录成功
                return authenticate(req, handler); // 进行权限校验
        }

        return false;
    }

    /**
     * 认证、进行权限校验
     * 处理acl注解
     *
     * @param r
     * @param h
     * @return false:不能访问也不处理，true:代表可以访问，异常:告诉不能访问的原因
     */
    private boolean authenticate(HttpServletRequest r, Object h) {
        if (r.getServletPath().startsWith("/error"))
            return false;

        if (!(h instanceof HandlerMethod)) return false;

        val handlerMethod = (HandlerMethod) h;
        Method method = handlerMethod.getMethod();
        if (method.getDeclaringClass() == BasicErrorController.class)
            return false;

        if (method.getAnnotation(LoginIgnore.class) != null) return true;

        val acl = getAcl(method);
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

//    @Autowired AclService aclService;

    private Acl getAcl(Method method) {
//        val roleNames = aclService.findAcl(method);
//        if (roleNames != null) return new Acl() {
//            @Override public Class<? extends Annotation> annotationType() {
//                return Acl.class;
//            }
//
//            @Override public String[] roles() {
//                return Iterables.toArray(splitter.split(roleNames), String.class);
//            }
//
////            @Override public Login login() {
////                return Login.登录;
////            }
//
//        };

        val acl = method.getAnnotation(Acl.class);
        if (acl != null) return acl;

        return method.getDeclaringClass().getAnnotation(Acl.class);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        AclContext.teardown();
    }
}
