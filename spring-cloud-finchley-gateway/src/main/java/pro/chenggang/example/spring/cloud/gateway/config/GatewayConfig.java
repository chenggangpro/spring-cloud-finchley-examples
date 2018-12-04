package pro.chenggang.example.spring.cloud.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ResponseStatusException;
import pro.chenggang.example.spring.cloud.gateway.response.converter.FastJsonResponseConverter;
import pro.chenggang.example.spring.cloud.gateway.response.converter.ResponseConverter;
import pro.chenggang.example.spring.cloud.gateway.response.handler.JsonExceptionHandler;
import pro.chenggang.example.spring.cloud.gateway.response.handler.factory.DefaultExceptionHandlerStrategyFactory;
import pro.chenggang.example.spring.cloud.gateway.response.handler.factory.ExceptionHandlerStrategyFactory;
import pro.chenggang.example.spring.cloud.gateway.response.handler.strategy.ExceptionHandlerStrategy;
import pro.chenggang.example.spring.cloud.gateway.response.handler.strategy.NotFoundExceptionHandlerStrategy;
import pro.chenggang.example.spring.cloud.gateway.response.handler.strategy.ResponseStatusExceptionHandlerStrategy;

import java.util.Collections;
import java.util.List;

/**
 * @classDesc:
 * @author: chenggang
 * @createTime: 2018/10/22
 * @version: v1.0.0
 */
@Configuration
@Slf4j
public class GatewayConfig {

    /**
     * Response响应包装器
     * @return
     */
    @Bean
    public ResponseConverter responseConverter(){
        FastJsonResponseConverter fastJsonResponseConverter =  new FastJsonResponseConverter();
        log.debug("Init Response Converter Success");
        return fastJsonResponseConverter;
    }


    /**
     * NotFoundExceptionHandlerStrategy
     * NotFoundException处理策略
     * @param responseConverter
     * @return
     */
    @Bean
    public ExceptionHandlerStrategy notFoundExceptionHandlerStrategy(ResponseConverter responseConverter){
        NotFoundExceptionHandlerStrategy strategy = new NotFoundExceptionHandlerStrategy(responseConverter);
        log.debug("Init NotFoundExceptionHandlerStrategy Success");
        return strategy;
    }


    /**
     * ResponseStatusExceptionHandlerStrategy
     * ResponseStatusException处理策略
     * @param responseConverter
     * @return
     */
    @Bean
    public ExceptionHandlerStrategy responseStatusExceptionHandlerStrategy(ResponseConverter responseConverter){
        ResponseStatusExceptionHandlerStrategy strategy = new ResponseStatusExceptionHandlerStrategy(responseConverter);
        log.debug("Init ResponseStatusExceptionHandlerStrategy Success");
        return strategy;
    }


    /**
     * ExceptionHandlerStrategyFactory
     * 异常处理策略工厂
     * @param notFoundExceptionHandlerStrategy
     * @param responseStatusExceptionHandlerStrategy
     * @return
     */
    @Bean
    public ExceptionHandlerStrategyFactory exceptionHandlerStrategyFactory(ExceptionHandlerStrategy notFoundExceptionHandlerStrategy,
                                                                           ExceptionHandlerStrategy responseStatusExceptionHandlerStrategy){
        DefaultExceptionHandlerStrategyFactory factory = new DefaultExceptionHandlerStrategyFactory();
        factory.addStrategy(NotFoundException.class,notFoundExceptionHandlerStrategy);
        factory.addStrategy(ResponseStatusException.class,responseStatusExceptionHandlerStrategy);
        log.debug("Init DefaultExceptionHandlerStrategyFactory Success,Strategy Size:{}",factory.getStrategySize());
        return factory;
    }


    /**
     * ErrorWebExceptionHandler
     * 全局异常处理器
     * @param viewResolversProvider
     * @param serverCodecConfigurer
     * @param exceptionHandlerExceptionHandlerStrategyFactory
     * @return
     */
    @Primary
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                                                             ServerCodecConfigurer serverCodecConfigurer,
                                                             ExceptionHandlerStrategyFactory exceptionHandlerExceptionHandlerStrategyFactory) {

        JsonExceptionHandler jsonExceptionHandler = new JsonExceptionHandler(exceptionHandlerExceptionHandlerStrategyFactory);
        jsonExceptionHandler.setViewResolvers(viewResolversProvider.getIfAvailable(Collections::emptyList));
        jsonExceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        jsonExceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
        log.debug("Init Json Exception Handler Instead Default ErrorWebExceptionHandler Success");
        return jsonExceptionHandler;
    }

}
