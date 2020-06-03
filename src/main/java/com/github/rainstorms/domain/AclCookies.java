package com.github.rainstorms.domain;

import com.github.rainstorms.util.Aes;
import com.github.rainstorms.util.Cookies;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

@Slf4j
public class AclCookies {
    public static final String AUTHC = "acl-authc";
    public static final String AesKey = "8o73LQEsp3VYfdlmTLJiZQ";


    public static void login(HttpServletResponse rsp, AclUser user) {
        saveCookie(rsp, AUTHC, user.getUserId());
        AclContext.setUser(user);
    }

    public static void saveCookie(HttpServletResponse rsp, String cookieName, String value) {
        val cookie = new Cookie(cookieName, encrypt(value));
        cookie.setPath("/");
        cookie.setMaxAge(-1);
        cookie.setHttpOnly(true);
        rsp.addCookie(cookie);
    }


    public static String readAuthc(HttpServletRequest request) {
        return readCookie(request, AUTHC);
    }


    public static String readCookie(HttpServletRequest request, String cookieName) {
        val openIdCookie = Cookies.findCookie(request, cookieName);
        return openIdCookie.map(cookie -> decrypt(cookie.getValue())).orElse(null);
    }

    public static void clearAuthc(HttpServletResponse rsp) {
        Cookies.clearCookie(rsp, AUTHC);
    }

    /**
     * 加密.
     *
     * @param context 明文.
     * @return 密文.
     */
    public static String encrypt(String context) {
        val now = LocalDate.now();
        return Aes.encrypt(context + "^" + now, AesKey);
    }

    /**
     * 解密.
     *
     * @param content 加密值
     * @return 解密值.
     */
    public static String decrypt(String content) {
        try {
            val v = Aes.decrypt(content, AesKey);
            return StringUtils.substringBefore(v, "^");
        } catch (Throwable ex) {
            log.warn("unable to decrypt {}", content, ex);
            return null;
        }
    }
}
