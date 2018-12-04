package pro.chenggang.example.spring.cloud.gateway.response.handler.factory;


import pro.chenggang.example.spring.cloud.gateway.response.handler.strategy.ExceptionHandlerStrategy;

/**
 * @classDesc: 策略工厂
 * @author: chenggang
 * @createTime: 2018/10/30
 * @version: v1.0.0
 */
public interface ExceptionHandlerStrategyFactory {

    /**
     * 获取策略
     * @param clazz
     * @return 返回ExceptionHandlerStrategy,不存在返回null
     */
    ExceptionHandlerStrategy getStrategy(Class<? extends Throwable> clazz);
}
