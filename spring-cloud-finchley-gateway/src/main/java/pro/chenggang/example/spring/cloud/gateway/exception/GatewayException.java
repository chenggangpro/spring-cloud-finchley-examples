package pro.chenggang.example.spring.cloud.gateway.exception;


import pro.chenggang.example.spring.cloud.gateway.response.info.Info;

/**
 * @classDesc:
 * @author: chenggang
 * @createTime: 2018/10/23
 * @version: v1.0.0
 */
public class GatewayException extends RuntimeException {

    protected Info responseInfo;

    public GatewayException(String message) {
        super(message);
    }

    public GatewayException(Info responseInfo) {
        this.responseInfo = responseInfo;
    }

    public GatewayException(String message, Info responseInfo) {
        super(message);
        this.responseInfo = responseInfo;
    }

    public GatewayException(String message, Throwable cause, Info responseInfo) {
        super(message, cause);
        this.responseInfo = responseInfo;
    }

    public GatewayException(Throwable cause, Info responseInfo) {
        super(cause);
        this.responseInfo = responseInfo;
    }

    public GatewayException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Info responseInfo) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.responseInfo = responseInfo;
    }

    public Info getResponseInfo() {
        return responseInfo;
    }
}
