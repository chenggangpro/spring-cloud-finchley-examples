package pro.chenggang.example.spring.cloud.gateway.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.niws.loadbalancer.DiscoveryEnabledNIWSServerList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import pro.chenggang.example.spring.cloud.gateway.support.grey.GreyWeightResponseRule;

/**
 * @classDesc:
 * @author: chenggang
 * @createTime: 2018/12/12
 * @version: v1.0.0
 * @copyright: 北京辰森
 * @email: cg@choicesoft.com.cn
 */
@Slf4j
@Configuration
@ConditionalOnClass(DiscoveryEnabledNIWSServerList.class)
@AutoConfigureBefore(RibbonClientConfiguration.class)
public class GreyConfig {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public IRule ribbonRule() {
        GreyWeightResponseRule greyWeightResponseRule = new GreyWeightResponseRule();
        log.debug("Init Grey Weight Response Rule Success");
        return greyWeightResponseRule;
    }

}
