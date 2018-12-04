package pro.chenggang.example.spring.cloud.gateway.response.handler.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ResponseStatusException;
import pro.chenggang.example.spring.cloud.gateway.response.converter.ResponseConverter;
import pro.chenggang.example.spring.cloud.gateway.response.handler.ExceptionHandlerResult;
import pro.chenggang.example.spring.cloud.gateway.response.info.ResponseInfo;

/**
 * @classDesc:响应码不正确策略
 * @author: chenggang
 * @createTime: 2018/10/30
 * @version: v1.0.0
 */
@Slf4j
public class ResponseStatusExceptionHandlerStrategy extends AbstractExceptionHandlerStrategy{

    public ResponseStatusExceptionHandlerStrategy(ResponseConverter responseConverter) {
        super(responseConverter);
    }

    @Override
    public ExceptionHandlerResult handleException(Throwable throwable) {
        ResponseStatusException responseStatusException = (ResponseStatusException) throwable;
        String response = convertResponseInfo(ResponseInfo.SERVICE_NOT_AVAILABLE,responseStatusException.getMessage());
        ExceptionHandlerResult result = new ExceptionHandlerResult(responseStatusException.getStatus(),response);
        log.debug("[ResponseStatusExceptionHandlerStrategy]Handle Exception:{},Result:{}",throwable.getMessage(),result);
        return result;
    }
}
