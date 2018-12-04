package pro.chenggang.example.spring.cloud.gateway.support;

/**
 * @classDesc: GatewayContext
 * @author: chenggang
 * @createTime: 2018/11/15
 * @version: v1.0.0
 */
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@Setter
@ToString
public class GatewayContext {

    public static final String CACHE_GATEWAY_CONTEXT = "cacheGatewayContext";

    /**
     * cache json body
     */
    private String cacheBody;
    /**
     * cache formdata
     */
    private MultiValueMap<String, String> formData;
    /**
     * cache reqeust path
     */
    private String path;
    /**
     * cache request serviceId
     */
    private String serviceId;
    /**
     * cache request full path
     */
    private String fullPath;
    /**
     * format Url [for old gateway check sign]
     */
    private String formatUrl;
    /**
     * cache all request data include:formData and queryParam
     */
    private MultiValueMap<String, String> allRequestData = new LinkedMultiValueMap<>(0);

}