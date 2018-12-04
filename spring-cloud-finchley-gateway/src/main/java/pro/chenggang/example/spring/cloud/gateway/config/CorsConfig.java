package pro.chenggang.example.spring.cloud.gateway.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.config.GlobalCorsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.WebFilter;

/**
 * @classDesc:
 * @author: chenggang
 * @createTime: 2018/10/24
 * @version: v1.0.0
 */
//@Configuration
@Slf4j
public class CorsConfig {

    /**
     * 跨域配置资源
     * @param globalCorsProperties
     * @return
     */
    @Primary
    @Bean
    public CorsConfigurationSource corsConfigurationSource(GlobalCorsProperties globalCorsProperties){
        UrlBasedCorsConfigurationSource corsConfigurationSource =  new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.setCorsConfigurations(globalCorsProperties.getCorsConfigurations());
        log.debug("Init CorsConfigurationSource With UrlBasedCorsConfigurationSource Success,Using Properties:{}", JSON.toJSON(globalCorsProperties));
        return corsConfigurationSource;
    }

    /**
     * 跨域过滤器
     * @param corsConfigurationSource
     * @return
     */
    @RefreshScope
    @Primary
    @Bean
    public WebFilter corsFilter(CorsConfigurationSource corsConfigurationSource) {
        CorsWebFilter corsWebFilter =  new CorsWebFilter(corsConfigurationSource);
        log.debug("Init CorsWebFilter Success");
        return corsWebFilter;
    }
}
