package pro.chenggang.example.spring.cloud.gateway.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.web.server.ServerWebExchange;
import pro.chenggang.example.spring.cloud.gateway.annotation.FilterValidate;
import pro.chenggang.example.spring.cloud.gateway.aspect.factory.AspectStrategyFactory;
import pro.chenggang.example.spring.cloud.gateway.aspect.strategy.AspectValidateStrategy;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import static pro.chenggang.example.spring.cloud.gateway.annotation.FilterValidate.ValidateRule;
import static pro.chenggang.example.spring.cloud.gateway.annotation.FilterValidate.ValidateType;

/**
 * @classDesc:
 * @author: chenggang
 * @createTime: 2018/10/29
 * @version: v1.0.0
 * @copyright: 北京辰森
 * @email: cg@choicesoft.com.cn
 */
@Aspect
@Slf4j
public class FilterAspect {

    private AspectStrategyFactory aspectStrategyFactory;

    public FilterAspect(AspectStrategyFactory aspectStrategyFactory) {
        this.aspectStrategyFactory = aspectStrategyFactory;
    }

    @Around("@annotation(pro.chenggang.example.spring.cloud.gateway.annotation.FilterValidate)&& @annotation(filterValidate)")
    public Object around(ProceedingJoinPoint joinPoint, FilterValidate filterValidate) throws Throwable {
        Class<?>[] interfaces = joinPoint.getTarget().getClass().getInterfaces();
        boolean support = false;
        for(Class clazz : interfaces){
            if(clazz.equals(GlobalFilter.class)){
                support = true;
                break;
            }
        }
        if(!support){
            throw new UnsupportedOperationException("Filter Aspect Only Support GlobalFilter's Subclass");
        }
        /**
         * get filter args
         */
        Object[] args = joinPoint.getArgs();
        ServerWebExchange exchange = (ServerWebExchange) args[0];
        GatewayFilterChain chain = (GatewayFilterChain) args[1];
        /**
         * get annotation args (validateType,validateRule,validateOrder)
         */
        ValidateType[] validateTypes = filterValidate.value();
        ValidateRule rule = filterValidate.rule();
        int[] order = filterValidate.order();
        /**
         * define tree map,contain all validateType with order
         */
        TreeMap<Integer,ValidateType> validateTypeOrderMap = new TreeMap<>((k1, k2) -> k1 - k2);
        /**
         * put validateType with order into tree map
         * if order doesn't exist just put zero as order
         */
        int orderLength = order.length;
        int typesLength = validateTypes.length;
        /**
         * copy original order array
         * get max item
         * max = max +1
         */
        int[] sortedOrder = Arrays.copyOf(order,orderLength);
        Arrays.sort(sortedOrder);
        int max = sortedOrder[sortedOrder.length-1] +1;
        for(int i =0;i<typesLength;i++){
            /**
             * order array length is less than i
             * put the map with max+i as the key
             * guarantee the given orderArray's order
             */
            if(i>=orderLength){
                validateTypeOrderMap.put(max+i,validateTypes[i]);
            }else{
                validateTypeOrderMap.put(order[i],validateTypes[i]);
            }
        }
        /**
         * according validateRule and validateStrategy validate
         */
        ValidateType validateType;
        AspectValidateStrategy strategy;
        switch (rule){
            /**
             * validateRule:Or
             * validate with order
             * any validateType success ,return filter chain
             * all validateType failed , return original method
             */
            case OR:
                for(Map.Entry<Integer,ValidateType> entry : validateTypeOrderMap.entrySet()){
                    validateType = entry.getValue();
                    strategy = aspectStrategyFactory.getStrategy(validateType);
                    if(strategy.validate(exchange)){
                        log.debug("[Validate Aspect]With Or Rule,ValidateType:{},Validate Success,Return Chain",validateType);
                        return chain.filter(exchange);
                    }
                }
                break;
            /**
             * validateRule:And
             * validate with order
             * any validateType failed,return original method
             * all validateType success,return filter chain
             */
            case AND:
                boolean validateSuccessFlag = true;
                for(Map.Entry<Integer,ValidateType> entry : validateTypeOrderMap.entrySet()){
                    validateType = entry.getValue();
                    strategy = aspectStrategyFactory.getStrategy(validateType);
                    if(!strategy.validate(exchange)){
                        validateSuccessFlag = false;
                        log.debug("[Validate Aspect]With And Rule,ValidateType:{},Validate Failed,Return Original Method",validateType);
                        break;
                    }
                }
                if(validateSuccessFlag){
                    log.debug("[Validate Aspect]With And Rule,All ValidateType Validate Success,Return Chain");
                    return chain.filter(exchange);
                }
                break;
            default:
                throw new UnsupportedOperationException("Filter Aspect Don't Support this ValidateRule :{}"+rule);
        }
        return joinPoint.proceed();
    }
}
