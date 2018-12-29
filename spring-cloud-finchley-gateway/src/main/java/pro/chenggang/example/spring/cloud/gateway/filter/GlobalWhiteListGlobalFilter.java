package pro.chenggang.example.spring.cloud.gateway.filter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import pro.chenggang.example.spring.cloud.gateway.option.FilterOrderEnum;
import pro.chenggang.example.spring.cloud.gateway.properties.GlobalWhiteListProperties;
import pro.chenggang.example.spring.cloud.gateway.support.GatewayContext;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * @classDesc:
 * @author: chenggang
 * @createTime: 2018/10/22
 * @version: v1.0.0
 * @copyright: 北京辰森
 * @email: cg@choicesoft.com.cn
 */
@Slf4j
@AllArgsConstructor
public class GlobalWhiteListGlobalFilter implements GlobalFilter,Ordered {

    /**
     * 白名单请求标记
     */
    public static final String WHITE_LIST_FLAG_KEY = "ignore";

    /**
     * 白名单请求标记值
     */
    public static final String WHITE_LIST_FLAG_VALUE = "TRUE";

    private GlobalWhiteListProperties globalWhiteListProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Map<String, Object> exchangeAttributes = exchange.getAttributes();
        if(exchangeAttributes.containsKey(WHITE_LIST_FLAG_KEY)){
            exchangeAttributes.remove(WHITE_LIST_FLAG_KEY);
            log.debug("[全局白名单Filter]已存在白名单标记,删除原有标记");
        }
        if(null == globalWhiteListProperties){
            return chain.filter(exchange);
        }
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        String path = gatewayContext.getPath();
        String serviceId = gatewayContext.getServiceId();
        /**
         * 白名单
         */
        List<String> whiteList = globalWhiteListProperties.getWhileListByServiceId(serviceId);
        /**
         * 前缀白名单
         */
        List<String> prefixWhiteList = globalWhiteListProperties.getPrefixWhileListByServiceId(serviceId);
        /**
         * 路径白名单
         */
        List<String> pathList = globalWhiteListProperties.getPathList();
        boolean whiteFlag = false;
        /**
         * 校验全文白名单
         */
        if(null != whiteList && !whiteList.isEmpty()){
            if(whiteList.contains(path)){
                whiteFlag = true;
            }
        }
        /**
         * 校验前缀白名单
         */
        if(!whiteFlag && null != prefixWhiteList && !prefixWhiteList.isEmpty()){
            for(String temp :prefixWhiteList){
                if(path.startsWith(temp)){
                    whiteFlag = true;
                    break;
                }
            }
        }
        /**
         * 校验全路径白名单
         */
        if(!whiteFlag && null != pathList && !pathList.isEmpty()){
            String fullPath = gatewayContext.getFullPath();
            for(String temp :pathList){
                if(fullPath.equals(temp)){
                    whiteFlag = true;
                    break;
                }
            }
        }
        if(whiteFlag){
            exchangeAttributes.put(WHITE_LIST_FLAG_KEY, WHITE_LIST_FLAG_VALUE);
            log.debug("[全局白名单Filter]添加白名单标记,ServiceId:{},Path:{}",serviceId,path);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return FilterOrderEnum.WHITE_LIST_FILTER.getOrder();
    }

}
