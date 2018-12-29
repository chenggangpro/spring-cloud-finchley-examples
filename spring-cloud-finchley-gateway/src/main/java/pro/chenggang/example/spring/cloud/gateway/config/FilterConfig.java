package pro.chenggang.example.spring.cloud.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import pro.chenggang.example.spring.cloud.gateway.filter.CustomLoadBalancerClientFilter;
import pro.chenggang.example.spring.cloud.gateway.filter.GatewayContextFilter;
import pro.chenggang.example.spring.cloud.gateway.filter.GlobalWhiteListGlobalFilter;
import pro.chenggang.example.spring.cloud.gateway.filter.GreyContextGlobalFilter;
import pro.chenggang.example.spring.cloud.gateway.filter.RequestLogGlobalFilter;
import pro.chenggang.example.spring.cloud.gateway.properties.GlobalWhiteListProperties;
import pro.chenggang.example.spring.cloud.gateway.properties.GreyProperties;

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
     * 全局白名单过滤器
     * @param globalWhiteListProperties
     * @return
     */
    @Bean
    public GlobalWhiteListGlobalFilter whiteListGlobalFilter(GlobalWhiteListProperties globalWhiteListProperties){
        GlobalWhiteListGlobalFilter globalWhiteListGlobalFilter =  new GlobalWhiteListGlobalFilter(globalWhiteListProperties);
        log.debug("Init GlobalWhiteListGlobalFilter Success");
        return globalWhiteListGlobalFilter;
    }

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

    /**
     * 灰度上下文过滤器
     * @param greyProperties
     * @return
     */
    @Bean
    public GreyContextGlobalFilter greyContextGlobalFilter(GreyProperties greyProperties){
        GreyContextGlobalFilter greyContextGlobalFilter = new GreyContextGlobalFilter(greyProperties);
        log.debug("Init GreyContextFilter Success");
        return greyContextGlobalFilter;
    }

    @Primary
    @Bean
    public LoadBalancerClientFilter loadBalancerClientFilter(LoadBalancerClient client) {
        return new CustomLoadBalancerClientFilter(client);
    }

}
