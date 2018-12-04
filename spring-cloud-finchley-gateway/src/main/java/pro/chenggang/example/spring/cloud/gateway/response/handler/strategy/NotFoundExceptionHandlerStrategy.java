package pro.chenggang.example.spring.cloud.gateway.response.handler.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import pro.chenggang.example.spring.cloud.gateway.response.converter.ResponseConverter;
import pro.chenggang.example.spring.cloud.gateway.response.handler.ExceptionHandlerResult;
import pro.chenggang.example.spring.cloud.gateway.response.info.ResponseInfo;

/**
 * @classDesc:未找到服务策略
 * @author: chenggang
 * @createTime: 2018/10/30
 * @version: v1.0.0
 */
@Slf4j
public class NotFoundExceptionHandlerStrategy extends AbstractExceptionHandlerStrategy{

    public NotFoundExceptionHandlerStrategy(ResponseConverter responseConverter) {
        super(responseConverter);
    }

    @Override
    public ExceptionHandlerResult handleException(Throwable throwable) {
        String response = convertResponseInfo(ResponseInfo.SERVICE_NOT_AVAILABLE,throwable.getMessage());
        ExceptionHandlerResult result = new ExceptionHandlerResult(HttpStatus.NOT_FOUND,response);
        log.debug("[NotFoundExceptionHandlerStrategy]Handle Exception:{},Result:{}",throwable.getMessage(),result);
        return result;
    }
}
