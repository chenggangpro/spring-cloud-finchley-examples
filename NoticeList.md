#注意事项汇总:

* 1.依赖相关
    * 1.项目中必须有`spring-boot-starter-web`依赖,否则任何功能都失效
    * 2.Eureka注册中心和SpringBootAdmin中的Web服务器推荐改为`Jetty`,`Tomcat`有时会报错
* 2.Eureka-Example中的`dev`和`test`环境为单点,`prod`环境为3台多点
* 3.Eureka启动注解
    * 1.启动注解为@SpringBootApplication否则无法向Eureka注册,官网使用注解 @Configuration和@EnableAutoConfiguration,在配置时无法注册Eureka服务
    * 2.使用@SpringBootApplication注解后正常
    * 3.由于对于SpringBoot2.0启动注解不是很了解,该问题待具体研究后给出答案
* 4.SpringBootAdmin2.0安全配置时,登陆成功后跳转页面不自动跳转到主页,添加默认跳转页面,否则登陆时个别浏览器无法跳转,若修改admin的ContextPath,则该位置也要修改
    ```java
    SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
    successHandler.setTargetUrlParameter("redirectTo");
    successHandler.setDefaultTargetUrl("/");
    ```
* 5.在配置需要SpringBootAdmin监控的应用时，由于SpringBoot2.0之后只暴露2个端点,需要配置为修改为暴露所有端点，或者根据自己的需要暴露需要的端点
```yml
management:
    endpoints:
      web:
        exposure:
          include: "*"
    endpoint:
      health:
        show-details: ALWAYS  #健康监测显示详情策略 NEVER,WHEN_AUTHORIZED,ALWAYS
```
* 6.配置Eureka注册中心地址时`eureka.client.serviceUrl.defaultZone`不能配置为`eureka.client.serviceUrl.default-zone`,否则无法注册
* 7.SpringBoot2.0-Security配置位置发生变化,原配置已弃用
```yml
spring:
    security:
        user:
          name: username
          password: password
```
* 8.配置SpringBootAdmin监控时，需要指定Admin-Server服务地址配置，旧版本无须指定
```yml
spring:
    boot:
      admin:
        client: #客户端用户名密码
          username: ${spring.security.user.name}
          password: ${spring.security.user.password}
          instance: #eureka注册中心上的AdminServer的InstanceName(该方式无须指定ip和端口,根据InstanceName自动寻址),官方手册中为url(url中必须指定ip和端口),
            name: actuator
            metadata: #eurekaInstance的用户名和密码
              user.name: ${spring.security.user.name}
              user.password: ${spring.security.user.password}
```
* 9.修改Eureka的PeerNodeReadTimeOut为1000，默认为200，当网络不好时,容易频繁报错(读取响应超时),修改为1000(1s),即可避免该问题
```yml
eureka:
    server:
      peer-node-read-timeout-ms: 1000 #peer读取超时时间,默认200,当网络不好时,容易频繁报错(读取响应超时),修改为1000(1s),即可避免该问题
```