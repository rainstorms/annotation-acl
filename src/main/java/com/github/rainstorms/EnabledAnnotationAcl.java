package com.github.rainstorms;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({EnabledAnnotationAclScanRegistrar.class})
public @interface EnabledAnnotationAcl {
}