package pro.chenggang.example.spring.cloud.gateway.response.info;

/**
 * @classDesc:
 * @author: chenggang
 * @createTime: 2018/10/22
 * @version: v1.0.0
 */
public enum ResponseInfo implements Info {
    /**
     * 成功
     */
    SUCCESS("10000","SUCCESS"),
    /**
     * 服务不可用
     */
    SERVICE_NOT_AVAILABLE("20000","isp.service-error"),
    /**
     * 网关内部错误
     */
    GATEWAY_ERROR("20004","isp.gateway-error"),
    /**
     * Token已登出
     */
    AUTH_TOKEN_HAS_LOGOUT("20003","aop.auth-token-logout"),
    /**
     * 无效的访问令牌
     */
    INVALID_AUTH_TOKEN("20001","aop.invalid-auth-token"),
    /**
     * 令牌失效
     */
    AUTH_TOKEN_EXPIRED("20002","aop.auth-token-time-out"),
    ;

    private String code;
    private String msg;

    ResponseInfo(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
