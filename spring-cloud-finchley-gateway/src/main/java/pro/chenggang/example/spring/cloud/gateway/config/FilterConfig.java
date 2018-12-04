package pro.chenggang.example.spring.cloud.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pro.chenggang.example.spring.cloud.gateway.filter.GatewayContextFilter;
import pro.chenggang.example.spring.cloud.gateway.filter.RequestLogGlobalFilter;

/**
 * @classDesc:
 * @author: chenggang
 * @createTime: 2018/10/22
 * @version: v1.0.0
 */
@Configuration
@Slf4j
public class FilterConfig {

    /**
     * 请求记录过滤器
     * @return
     */
    @Bean
    public RequestLogGlobalFilter requestLogGlobalFilter(){
        RequestLogGlobalFilter requestLogGlobalFilter =  new RequestLogGlobalFilter();
        log.debug("Init RequestLogGlobalFilter Success");
        return requestLogGlobalFilter;
    }

    /**
     * 网关上下文过滤器
     * @return
     */
    @Bean
    public GatewayContextFilter gatewayContextFilter(){
        GatewayContextFilter gatewayContextFilter =  new GatewayContextFilter();
        log.debug("Init GatewayContextFilter Success");
        return gatewayContextFilter;
    }


}
