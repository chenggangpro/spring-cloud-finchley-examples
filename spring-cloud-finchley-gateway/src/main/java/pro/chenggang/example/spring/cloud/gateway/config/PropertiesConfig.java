package pro.chenggang.example.spring.cloud.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pro.chenggang.example.spring.cloud.gateway.properties.GlobalWhiteListProperties;
import pro.chenggang.example.spring.cloud.gateway.properties.GreyProperties;

/**
 * @classDesc:
 * @author: chenggang
 * @createTime: 2018/11/19
 * @version: v1.0.0
 * @copyright: 北京辰森
 * @email: cg@choicesoft.com.cn
 */
@Configuration
@Slf4j
public class PropertiesConfig {

    /**
     *  全局白名单列表
     * @return
     */
    @RefreshScope
    @Bean
    public GlobalWhiteListProperties whiteListProperties(){
        GlobalWhiteListProperties globalWhiteListProperties =  new GlobalWhiteListProperties();
        log.debug("Init GlobalWhiteListProperties Success");
        return globalWhiteListProperties;
    }


    /**
     * 灰度配置
     * @return
     */
    @RefreshScope
    @Bean
    public GreyProperties grayProperties(){
        GreyProperties greyProperties = new GreyProperties();
        log.debug("Init GreyProperties Success");
        return greyProperties;
    }

}
