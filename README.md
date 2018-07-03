# spring-cloud-finchley-examples
Spring Cloud Finchley Examples

* 1. 该项目是SpringCloud  Finchley 版本使用示例,整合SpringBoot Admin(2.0.1)作为基础监控,项目使用SpringBoot版本:2.0.3.RELEASE
* 3. 示例项目列表如下:

|项目名称|作用|备注|
|:------:---|:---|
|spring-boot-admin|监控SpringBoot应用| |

#注意事项汇总:
* 1.依赖相关
    * 1.项目中必须有`spring-boot-starter-web`依赖,否则任何功能都失效
    * 2.Eureka注册中心和SpringBootAdmin中的Web服务器推荐改为`Jetty`,`Tomcat`有时会报错
* 2.Eureka-Example中的`dev`和`test`环境为单点,`prod`环境为3台多点