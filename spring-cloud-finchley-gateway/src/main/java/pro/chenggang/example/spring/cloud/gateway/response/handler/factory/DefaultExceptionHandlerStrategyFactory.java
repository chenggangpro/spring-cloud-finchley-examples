package pro.chenggang.example.spring.cloud.gateway.response.handler.factory;

import lombok.extern.slf4j.Slf4j;
import pro.chenggang.example.spring.cloud.gateway.response.handler.strategy.DefaultExceptionHandlerStrategy;
import pro.chenggang.example.spring.cloud.gateway.response.handler.strategy.ExceptionHandlerStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * @classDesc: 异常处理策略工厂
 * @author: chenggang
 * @createTime: 2018/10/30
 * @version: v1.0.0
 */
@Slf4j
public class DefaultExceptionHandlerStrategyFactory implements ExceptionHandlerStrategyFactory {

    private final Map<Class<? extends Throwable>,ExceptionHandlerStrategy> strategyContainer;

    private final ExceptionHandlerStrategy defaultExceptionHandlerStrategy;

    public DefaultExceptionHandlerStrategyFactory() {
        this(new DefaultExceptionHandlerStrategy());
    }

    public DefaultExceptionHandlerStrategyFactory(ExceptionHandlerStrategy defaultExceptionHandlerStrategy) {
        this.strategyContainer = new HashMap<>();
        this.defaultExceptionHandlerStrategy = defaultExceptionHandlerStrategy;
    }

    public void addStrategy(Class<? extends Throwable> clazz,ExceptionHandlerStrategy exceptionHandlerStrategy){
        if(!strategyContainer.containsKey(clazz)){
            strategyContainer.put(clazz, exceptionHandlerStrategy);
            log.debug("[DefaultExceptionHandlerStrategyFactory] Add Strategy,Class:{},Strategy:{}",clazz,exceptionHandlerStrategy);
        }
    }

    /**
     * 获取策略个数
     * @return
     */
    public int getStrategySize(){
        return strategyContainer.size()+1;
    }


    @Override
    public ExceptionHandlerStrategy getStrategy(Class clazz) {
        ExceptionHandlerStrategy strategy =  strategyContainer.get(clazz);
        if(null == strategy && null != this.defaultExceptionHandlerStrategy){
            log.debug("[DefaultExceptionHandlerStrategyFactory]Get Target Exception Handler Strategy Is Null,Use Default Strategy");
            strategy = defaultExceptionHandlerStrategy;
        }
        log.debug("[DefaultExceptionHandlerStrategyFactory] Get Strategy,Class:{},Strategy:{}",clazz,strategy);
        return strategy;
    }
}
