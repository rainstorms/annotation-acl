package com.github.rainstorms;

import com.github.rainstorms.exception.AclInterceptor;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.Set;

public class AclScanner extends ClassPathBeanDefinitionScanner {
    public AclScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    public void registerFilters() {
        addIncludeFilter(new AssignableTypeFilter(AclInterceptor.class));
//        addIncludeFilter(new AssignableTypeFilter(CookieAclLogin.class));
//        addIncludeFilter(new AssignableTypeFilter(AclUserService.class));
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }

}
