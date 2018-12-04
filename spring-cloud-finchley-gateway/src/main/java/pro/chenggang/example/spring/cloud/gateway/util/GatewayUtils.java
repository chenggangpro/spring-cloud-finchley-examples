package pro.chenggang.example.spring.cloud.gateway.util;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @classDesc:  工具包
 * @author: chenggang
 * @createTime: 2018/10/23
 * @version: v1.0.0
 */
public class GatewayUtils {

    /**
     * path separator
     */
    public static final String PATH_SEPARATOR = "/";

    /**
     * 去除Null，Json格式ToString
     * @param object
     * @return
     */
    public static String toNotNullString(final Object object){
        ReflectionToStringBuilder builder = new ReflectionToStringBuilder(object, ToStringStyle.JSON_STYLE){
            @Override
            protected boolean accept(Field field) {
                try {
                    return Objects.nonNull(field.get(object))&&super.accept(field);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return true;
                }
            }

        };
        return builder.toString();
    }

    /**
     * 获取真实ip地址
     * @param request ServerHttpRequest
     * @return
     * @author Evans
     */
    public static String getIpAddress(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("X-Real-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getAddress().getHostAddress();
        }
        if(ip != null && ip.length() > 15 && ip.contains(",")){
            ip = ip.substring(0,ip.indexOf(","));
        }
        return ip;
    }
}
