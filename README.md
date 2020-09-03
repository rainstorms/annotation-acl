###用法：
1、在自己的项目里引入依赖   
 ```
        <dependency>
           <groupId>rainstorms</groupId>
           <artifactId>annotation-acl</artifactId>
           <version>0.0.1</version>
        </dependency>
```

2、实现 `aclUserService` ，提供查询用户和权限
 ```
    @Service
    public class TestService implements AclUserService {
    
        @Override public AclUser findUser(String userId) {
            return new AclUser();
        }
    
        @Override public List<String> findUserRole(String userId) {
            return Lists.newArrayList("1");
        }
    }
 ```

3、配置拦截器正式使用权限功能
 ``` 
@Configuration
public class Config implements WebMvcConfigurer {

    @Autowired
    TestService aclUserService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        CookieAclLogin cookieAclLogin = new CookieAclLogin();
        AclLogin[] aclLogins = {cookieAclLogin};
        AclInterceptor interceptor = new AclInterceptor(aclLogins, aclUserService);

        registry.addInterceptor(interceptor)
                .addPathPatterns("/**");
    }
}
 ```