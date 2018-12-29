package pro.chenggang.example.spring.cloud.gateway.aspect.factory;


import pro.chenggang.example.spring.cloud.gateway.annotation.FilterValidate;
import pro.chenggang.example.spring.cloud.gateway.aspect.strategy.AspectValidateStrategy;

/**
 * @classDesc: 策略工厂
 * @author: chenggang
 * @createTime: 2018/10/30
 * @version: v1.0.0
 * @copyright: 北京辰森
 * @email: cg@choicesoft.com.cn
 */
public interface AspectStrategyFactory {

    /**
     * 获取策略
     * @param validateType
     * @return 返回AspectValidateStrategy,不存在返回null
     */
    AspectValidateStrategy getStrategy(FilterValidate.ValidateType validateType);
}
