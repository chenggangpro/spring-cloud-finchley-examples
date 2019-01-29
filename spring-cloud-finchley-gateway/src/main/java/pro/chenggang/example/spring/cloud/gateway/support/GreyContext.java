package pro.chenggang.example.spring.cloud.gateway.support;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @classDesc:
 * @author: chenggang
 * @createTime: 2018/12/13
 * @version: v1.0.0
 */
@Setter
@Getter
@ToString
public class GreyContext {

    public static final String CACHE_GREY_CONTEXT = "cacheGreyContext";

    private String serviceId;
    private String version;
    private boolean matched;
}
