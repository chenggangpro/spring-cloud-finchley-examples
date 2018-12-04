package pro.chenggang.example.spring.cloud.gateway.response.handler.strategy;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import pro.chenggang.example.spring.cloud.gateway.response.ResponseResult;
import pro.chenggang.example.spring.cloud.gateway.response.handler.ExceptionHandlerResult;
import pro.chenggang.example.spring.cloud.gateway.response.info.ResponseInfo;

/**
 * @classDesc:默认异常处理策略
 * @author: chenggang
 * @createTime: 2018/10/30
 * @version: v1.0.0
 */
@Slf4j
public class DefaultExceptionHandlerStrategy implements ExceptionHandlerStrategy {

    @Override
    public ExceptionHandlerResult handleException(Throwable throwable) {
        ResponseResult<String> responseResult = new ResponseResult<>(ResponseInfo.GATEWAY_ERROR);
        responseResult.setData(throwable.getMessage());
        ExceptionHandlerResult result = new ExceptionHandlerResult(HttpStatus.INTERNAL_SERVER_ERROR, JSON.toJSONString(responseResult));
        log.debug("[DefaultExceptionHandlerStrategy]Handle Exception:{},Result:{}",throwable.getMessage(),result);
        log.error("[DefaultExceptionHandlerStrategy]Log Exception In Error Level,Exception Message:{}",throwable.getMessage());
        return result;
    }
}
