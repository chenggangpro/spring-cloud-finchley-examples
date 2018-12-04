package pro.chenggang.example.spring.cloud.gateway.response.handler.strategy;


import pro.chenggang.example.spring.cloud.gateway.response.converter.ResponseConverter;
import pro.chenggang.example.spring.cloud.gateway.response.info.Info;

/**
 * @classDesc: 策略抽象类,封装转换ResponseInfo方法
 * @author: chenggang
 * @createTime: 2018/10/30
 * @version: v1.0.0
 */
public abstract class AbstractExceptionHandlerStrategy implements ExceptionHandlerStrategy{


    protected ResponseConverter responseConverter;

    public AbstractExceptionHandlerStrategy(ResponseConverter responseConverter) {
        this.responseConverter = responseConverter;
    }

    /**
     * 转换ResponseInfo
     * @param responseInfo
     * @return
     */
    protected String convertResponseInfo(Info responseInfo){
        return responseConverter.convertJsonResponse(responseInfo);
    }

    /**
     * 转换ResponseInfoWithData
     * @param responseInfo
     * @return
     */
    protected String convertResponseInfo(Info responseInfo, Object data){
        return responseConverter.covertJsonResponse(responseInfo,data);
    }
}
