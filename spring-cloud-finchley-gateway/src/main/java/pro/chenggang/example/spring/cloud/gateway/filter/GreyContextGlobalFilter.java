package pro.chenggang.example.spring.cloud.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import pro.chenggang.example.spring.cloud.gateway.option.FilterOrderEnum;
import pro.chenggang.example.spring.cloud.gateway.properties.GreyProperties;
import pro.chenggang.example.spring.cloud.gateway.support.GatewayContext;
import pro.chenggang.example.spring.cloud.gateway.support.GreyContext;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @classDesc:
 * @author: chenggang
 * @createTime: 2018/12/13
 * @version: v1.0.0
 */
@Slf4j
@AllArgsConstructor
public class GreyContextGlobalFilter implements GlobalFilter,Ordered {

    private GreyProperties greyProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        /**
         * get gateway context
         */
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        String serviceId = gatewayContext.getServiceId();
        /**
         * get grey rule
         */
        GreyProperties.GreyRule greyRule = greyProperties.getGreyRule(serviceId);
        /**
         * grey rule is empty or null ,return chain
         */
        if(null == greyRule || null == greyRule.getRules() || greyRule.getRules().isEmpty()){
            log.debug("[GreyContextGlobalFilter]ServiceId:{},GreyRule Is Null, Return Chain",serviceId);
            return chain.filter(exchange);
        }
        /**
         * get contentType
         */
        MediaType contentType = exchange.getRequest().getHeaders().getContentType();
        /**
         * get grey rules and grey operation type
         */
        LinkedHashMap<String, List<String>> rules = greyRule.getRules();
        Set<String> ruleKeys = rules.keySet();
        GreyProperties.GreyRule.Operation operation = greyRule.getOperation();
        /**
         * get All request data (query params and form data params)
         */
        MultiValueMap<String, String> allRequestData = gatewayContext.getAllRequestData();
        /**
         * init filtered request data with grey rule keys
         */
        Set<Map.Entry<String, List<String>>> filteredRequestData = Collections.emptySet();
        /**
         * json contentType init
         */
        if(MediaType.APPLICATION_JSON.equals(contentType) || MediaType.APPLICATION_JSON_UTF8.equals(contentType)){
            String jsonBody = gatewayContext.getCacheBody();
            if(StringUtils.isNotBlank(jsonBody)){
                Map<String,List<String>> jsonParam = new HashMap<>();
                ruleKeys.forEach(key->{
                    Object eval = JSONPath.eval(JSONObject.parseObject(jsonBody), String.format("$..%s", key));
                    if(null != eval){
                        List value = (List) eval;
                        if(!value.isEmpty()){
                            List<String> valueList = new ArrayList<>(value.size());
                            for(Object tempValue : value){
                                valueList.add(tempValue.toString());
                            }
                            jsonParam.put(key,valueList);
                        }
                    }
                });
                if(!jsonParam.isEmpty()){
                    filteredRequestData = jsonParam.entrySet();
                }
            }
        }else if(!allRequestData.isEmpty()){
            /**
             * other contentType And AllRequestData is Not Empty
             */
            filteredRequestData = allRequestData.entrySet()
                    .stream()
                    .filter(stringListEntry -> ruleKeys.contains(stringListEntry.getKey()))
                    .collect(Collectors.toSet());
        }
        /**
         * validate requestData in different operation
         */
        boolean matched = false;
        switch (operation){
            case OR:
                matched = validateGreyOrOperation(filteredRequestData,rules);
                break;
            case AND:
                matched = validateGreyAndOperation(filteredRequestData,rules);
                break;
            default:
                break;
        }
        /**
         * cache grey context
         */
        GreyContext greyContext = new GreyContext();
        greyContext.setServiceId(serviceId);
        greyContext.setVersion(greyRule.getVersion());
        greyContext.setMatched(matched);
        exchange.getAttributes().put(GreyContext.CACHE_GREY_CONTEXT,greyContext);
        log.debug("[GreyContextGlobalFilter]ServiceId:{},Matched Grey Rules,Cache GreyContext:{}",serviceId,greyContext);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return FilterOrderEnum.GREY_CONTEXT_FILTER.getOrder();
    }

    /**
     * validate grey rule use or operation
     * @param filteredRequestData
     * @param rules
     * @return
     */
    private boolean validateGreyOrOperation(Set<Map.Entry<String, List<String>>> filteredRequestData,LinkedHashMap<String, List<String>> rules){
        if(null == filteredRequestData || filteredRequestData.isEmpty() || null == rules || rules.isEmpty()){
            log.debug("[GreyContextGlobalFilter](ValidateGreyOrOperation)RequestData Is Empty Or Rules Is Null,Return False");
            return false;
        }
        List<String> paramValue;
        String paramKey;
        for(Map.Entry<String, List<String>> entry:filteredRequestData){
            paramValue= entry.getValue();
            paramKey = entry.getKey();
            for(String ruleParam : rules.get(paramKey)){
                if(paramValue.contains(ruleParam)){
                    log.debug("[GreyContextGlobalFilter](ValidateGreyOrOperation)Match Grey Rules In RequestParam,Grey Rule Key:{},RuleValue:{},ParamValueList:{}",paramKey,ruleParam,paramValue);
                    return true;
                }
            }
        }
        log.debug("[GreyContextGlobalFilter](ValidateGreyOrOperation)Not Matched Grey Rules,Return False");
        return false;
    }

    /**
     * validate grey rule use and operation
     * @param filteredRequestData
     * @param rules
     * @return
     */
    private boolean validateGreyAndOperation(Set<Map.Entry<String, List<String>>> filteredRequestData,LinkedHashMap<String, List<String>> rules){
        if(null == filteredRequestData || filteredRequestData.isEmpty() || null == rules || rules.isEmpty()){
            log.debug("[GreyContextGlobalFilter](ValidateGreyAndOperation)RequestData Is Empty Or Rules Is Null,Return False");
            return false;
        }
        List<String> paramValue;
        String paramKey;
        int matchedSize =0;
        for(Map.Entry<String, List<String>> entry:filteredRequestData){
            paramValue= entry.getValue();
            paramKey = entry.getKey();
            for(String ruleParam : rules.get(paramKey)){
                if(paramValue.contains(ruleParam)){
                    log.debug("[GreyContextGlobalFilter](ValidateGreyAndOperation)Match Grey Rules In RequestParam,Grey Rule Key:{},RuleValue:{},ParamValueList:{}",paramKey,ruleParam,paramValue);
                    matchedSize++;
                }
            }
        }
        if(matchedSize == rules.size()){
            log.debug("[GreyContextGlobalFilter](ValidateGreyAndOperation)Matched Grey Rules,RuleSize:{},Matched Size:{},Return True",rules.size(),matchedSize);
            return true;
        }
        log.debug("[GreyContextGlobalFilter](ValidateGreyAndOperation)Not Matched Grey Rules,Return False");
        return false;
    }
}
