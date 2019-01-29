package pro.chenggang.example.spring.cloud.gateway.aspect.factory;

import lombok.extern.slf4j.Slf4j;
import pro.chenggang.example.spring.cloud.gateway.annotation.FilterValidate;
import pro.chenggang.example.spring.cloud.gateway.aspect.strategy.AspectValidateStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * @classDesc: 切面校验策略工厂
 * @author: chenggang
 * @createTime: 2018/10/30
 * @version: v1.0.0
 */
@Slf4j
public class DefaultAspectStrategyFactory implements AspectStrategyFactory {

    private final Map<FilterValidate.ValidateType,AspectValidateStrategy> strategyContainer;

    public DefaultAspectStrategyFactory() {
        this.strategyContainer = new HashMap<>();
    }

    public void addStrategy(FilterValidate.ValidateType validateType,AspectValidateStrategy aspectValidateStrategy){
        if(!strategyContainer.containsKey(validateType)){
            strategyContainer.put(validateType, aspectValidateStrategy);
            log.debug("[DefaultAspectStrategyFactory] Add Strategy,ValidateType:{},Strategy:{}",validateType,aspectValidateStrategy);
        }
    }

    /**
     * 获取策略个数
     * @return
     */
    public int getStrategySize(){
        return strategyContainer.size();
    }

    @Override
    public AspectValidateStrategy getStrategy(FilterValidate.ValidateType validateType) {
        AspectValidateStrategy strategy =  strategyContainer.get(validateType);
        log.debug("[DefaultAspectStrategyFactory] Get Strategy,ValidateType:{},Strategy:{}",validateType,strategy.getClass().getSimpleName());
        return strategy;
    }
}
