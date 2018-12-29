package pro.chenggang.example.spring.cloud.gateway.aspect.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import pro.chenggang.example.spring.cloud.gateway.filter.GlobalWhiteListGlobalFilter;

import java.util.Map;

/**
 * @classDesc:
 * @author: chenggang
 * @createTime: 2018/11/16
 * @version: v1.0.0
 * @copyright: 北京辰森
 * @email: cg@choicesoft.com.cn
 */
@Slf4j
public class GlobalWhiteListAspectStrategy implements AspectValidateStrategy {

    @Override
    public boolean validate(ServerWebExchange exchange) {
        Map<String, Object> exchangeAttributes = exchange.getAttributes();
        String value = String.valueOf(exchangeAttributes.get(GlobalWhiteListGlobalFilter.WHITE_LIST_FLAG_KEY));
        if(GlobalWhiteListGlobalFilter.WHITE_LIST_FLAG_VALUE.equals(value)){
            log.debug("[GlobalWhiteListAspectStrategy]校验全局白名单,校验成功");
            return true;
        }
        return false;
    }
}
