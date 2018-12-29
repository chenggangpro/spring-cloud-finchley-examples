package pro.chenggang.example.spring.cloud.gateway.option;

/**
 * @classDesc: 过滤器顺序
 * @author: chenggang
 * @createTime: 2018/10/23
 * @version: v1.0.0
 */
public enum FilterOrderEnum {

    /**
     * 网关上下文过滤器
     */
    GATEWAY_CONTEXT_FILTER(Integer.MIN_VALUE),
    /**
     * 灰度上下文过滤器
     */
    GREY_CONTEXT_FILTER(Integer.MIN_VALUE+1),
    /**
     * 请求日志过滤器
     */
    REQUEST_LOG_FILTER(Integer.MIN_VALUE+2),
    /**
     * 白名单过滤器
     */
    WHITE_LIST_FILTER(Integer.MIN_VALUE+100),
    ;

    private int order;

    FilterOrderEnum(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}
