package pro.chenggang.example.spring.cloud.gateway.response.handler.strategy;


import pro.chenggang.example.spring.cloud.gateway.response.handler.ExceptionHandlerResult;

/**
 * @classDesc:
 * @author: chenggang
 * @createTime: 2018/10/30
 * @version: v1.0.0
 */
@FunctionalInterface
public interface ExceptionHandlerStrategy {

    /**
     * 处理异常
     * @param throwable
     * @return 返回需要响应的Body信息
     */
    ExceptionHandlerResult handleException(Throwable throwable);

}
