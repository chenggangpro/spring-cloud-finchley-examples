package pro.chenggang.example.spring.cloud.gateway.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @classDesc:
 * @author: chenggang
 * @createTime: 2018/11/29
 * @version: v1.0.0
 * @copyright: 北京辰森
 * @email: cg@choicesoft.com.cn
 */
@Slf4j
@ToString
@Getter
@Setter
@ConfigurationProperties(GreyProperties.GREY_PROPERTIES_PREFIX)
public class GreyProperties implements InitializingBean {

    public static final String GREY_PROPERTIES_PREFIX = "gateway.grey";
    /**
     * 灰度规则列表
     */
    private List<GreyRule> greyRuleList = Collections.emptyList();
    /**
     * 灰度规则Map
     */
    private Map<String,GreyRule> greyRuleMap = Collections.emptyMap();

    @Override
    public void afterPropertiesSet() throws Exception {
        if(null == greyRuleList || greyRuleList.isEmpty()){
            return;
        }
        greyRuleMap = new HashMap<>(greyRuleList.size(),1);
        for(GreyRule grayRule : greyRuleList){
            greyRuleMap.put(grayRule.getServiceId(),grayRule);
        }
        log.debug("Init Grey Rule Map :{}",greyRuleMap);
    }

    /**
     * 根据ServiceId获取GreyRule
     * @param serviceId
     * @return
     */
    public GreyRule getGreyRule(String serviceId){
        if(StringUtils.isBlank(serviceId)){
            return null;
        }
        return greyRuleMap.get(serviceId.toLowerCase());
    }


    @Getter
    @Setter
    @ToString
    public static class GreyRule{
        private String serviceId;
        private String version;
        private Operation operation = Operation.OR;
        private LinkedHashMap<String,List<String>> rules = new LinkedHashMap<>();

        public enum Operation{
            /**
             * 操作规则AND
             */
            AND,
            /**
             * 操作规则OR
             */
            OR
        }

    }
}
