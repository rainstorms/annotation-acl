package com.github.rainstorms.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 访问控制注解。
 * <p>
 * 不加就代表不校验，代表任何角色都可以访问。
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Acl {
    /**
     * 可以哪些角色访问。
     */
    String[] roles() default {};
}
