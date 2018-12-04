package pro.chenggang.example.spring.cloud.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import pro.chenggang.example.spring.cloud.gateway.option.FilterOrderEnum;
import pro.chenggang.example.spring.cloud.gateway.support.GatewayContext;
import pro.chenggang.example.spring.cloud.gateway.util.GatewayUtils;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * @classDesc: RequestLog
 * @author: chenggang
 * @createTime: 2018/10/23
 * @version: v1.0.0
 */
@Slf4j
public class RequestLogGlobalFilter implements GlobalFilter,Ordered {

    private static final String START_TIME = "startTime";

    private static final String HTTP_SCHEME = "http";

    private static final String HTTPS_SCHEME = "https";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI requestURI = request.getURI();
        String scheme = requestURI.getScheme();
        /**
         * 非Http或者Https不记录
         */
        if ((!HTTP_SCHEME.equalsIgnoreCase(scheme) && !HTTPS_SCHEME.equals(scheme))) {
            log.debug("[请求记录]非Http和Https请求,不记录,Scheme:{}",scheme);
            return chain.filter(exchange);
        }
        logRequest(exchange);
        return chain.filter(exchange).then(Mono.fromRunnable(()->logResponse(exchange)));
    }

    @Override
    public int getOrder() {
        return FilterOrderEnum.REQUEST_LOG_FILTER.getOrder();
    }

    /**
     * 记录请求
     * @param exchange
     */
    private void logRequest(ServerWebExchange exchange){
        ServerHttpRequest request = exchange.getRequest();
        URI requestURI = request.getURI();
        String scheme = requestURI.getScheme();
        HttpHeaders headers = request.getHeaders();
        long startTime = System.currentTimeMillis();
        exchange.getAttributes().put(START_TIME, startTime);
        log.info("[请求记录]请求开始,开始时间:{}",startTime);
        log.info("[请求记录]Scheme:{},Path:{}",scheme,requestURI.getPath());
        log.info("[请求记录]Method:{},IP:{},Host:{}",request.getMethod().name(), GatewayUtils.getIpAddress(request),requestURI.getHost());
        headers.forEach((key,value)-> log.debug("[请求记录]Headers:Key->{},Value->{}",key,value));
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        if(!queryParams.isEmpty()){
            queryParams.forEach((key,value)-> log.debug("[请求记录]请求参数:Key->({}),Value->({})",key,value));
        }
        MediaType contentType = headers.getContentType();
        long length = headers.getContentLength();
        log.debug("[请求记录]ContentType:{},Body长度:{}",contentType,length);
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        if(length>0 && (contentType.includes(MediaType.APPLICATION_JSON)
                ||contentType.includes(MediaType.APPLICATION_JSON_UTF8))){
            log.debug("[请求记录]JsonBody:{}",gatewayContext.getCacheBody());
        }
        if(length>0 && contentType.includes(MediaType.APPLICATION_FORM_URLENCODED)){
            log.debug("[请求记录]FormData:{}",gatewayContext.getFormData());
        }
    }

    /**
     * 记录响应
     * @param exchange
     */
    private Mono<Void> logResponse(ServerWebExchange exchange){
        Long startTime = exchange.getAttribute(START_TIME);
        Long executeTime = (System.currentTimeMillis() - startTime);
        ServerHttpResponse response = exchange.getResponse();
        log.info("[响应记录]HttpStatus:{}",response.getStatusCode());
        HttpHeaders headers = response.getHeaders();
        headers.forEach((key,value)-> log.debug("[响应记录]Headers:Key->{},Value->{}",key,value));
        MediaType contentType = headers.getContentType();
        long length = headers.getContentLength();
        log.debug("[响应记录]ContentType:{},Body长度:{}",contentType,length);
        //TODO logResponse
//        if(length>0 && (contentType.includes(MediaType.APPLICATION_JSON)
//                ||contentType.includes(MediaType.APPLICATION_JSON_UTF8)
//                ||contentType.includes(MediaType.APPLICATION_FORM_URLENCODED)
//                )){
//
//        }
        log.info("[响应记录]Path:{},Cost:{} ms", exchange.getRequest().getURI().getPath(),executeTime);
        return Mono.empty();
    }

}
