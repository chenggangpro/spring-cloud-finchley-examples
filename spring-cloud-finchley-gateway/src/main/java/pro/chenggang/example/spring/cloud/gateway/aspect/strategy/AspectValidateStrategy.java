package pro.chenggang.example.spring.cloud.gateway.aspect.strategy;

import org.springframework.web.server.ServerWebExchange;

/**
 * @classDesc:
 * @author: chenggang
 * @createTime: 2018/10/30
 * @version: v1.0.0
 */
@FunctionalInterface
public interface AspectValidateStrategy {

    /**
     * 校验
     * @return 校验成功返回true,失败返回false
     * 校验成功后,切面自动放行
     */
    boolean validate(ServerWebExchange exchange);

}
