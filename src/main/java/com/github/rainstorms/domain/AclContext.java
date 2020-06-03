package com.github.rainstorms.domain;

import lombok.val;

import java.util.Collection;

public class AclContext {
    private static ThreadLocal<AclUser> context = new ThreadLocal<>();


    /**
     * 获得当前登录用户。（没有登录时，返回null)
     *
     * @return FUser对象（未登录时返回null)
     */
    public static AclUser getUser() {
        return context.get();
    }

    /**
     * 是否已经登录。
     *
     * @return true 是
     */
    public static boolean isLogined() {
        return context.get() != null;
    }

    /**
     * 当前登录用户是否拥有指定角色。
     *
     * @param roles 角色名称列表。
     * @return 有任一角色。
     */
    public static boolean hasAnyRoles(String... roles) {
        val user = context.get();
        if (user == null) return false;

        val userRoles = user.getUserRoles();

        return containsAnyOf(userRoles, roles);
    }

    private static <T> boolean containsAnyOf(Collection<T> collection, T... values) {
        for (T t : values) {
            if (collection.contains(t)) return true;
        }
        return false;
    }

    public static void setUser(AclUser user) {
        context.set(user);
    }

    public static void teardown() {
        context.set(null);
    }

    public static String getUserId() {
        return getUser().getUserId();
    }
}
