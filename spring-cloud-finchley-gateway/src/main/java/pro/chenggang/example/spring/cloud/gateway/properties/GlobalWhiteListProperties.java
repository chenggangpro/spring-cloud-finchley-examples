package pro.chenggang.example.spring.cloud.gateway.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @classDesc: 白名单Properties
 * @author: chenggang
 * @createTime: 2018/10/22
 * @version: v1.0.0
 * @copyright: 北京辰森
 * @email: cg@choicesoft.com.cn
 */
@ConfigurationProperties(GlobalWhiteListProperties.GLOBAL_WHITE_PROPERTIES_PREFIX)
@ToString
@Slf4j
public class GlobalWhiteListProperties implements InitializingBean {

    public static final String GLOBAL_WHITE_PROPERTIES_PREFIX = "gateway.white";

    private Map<String,List<String>> WHITE_LIST_MAP;

    private Map<String,List<String>> WHITE_LIST_PREFIX_PATH_MAP;

    private List<WhiteList> list;

    private List<String> pathList;

    public List<WhiteList> getList() {
        return list;
    }

    public void setList(List<WhiteList> list) {
        this.list = list;
    }

    public List<String> getPathList() {
        return pathList;
    }

    public void setPathList(List<String> pathList) {
        this.pathList = pathList;
    }

    /**
     * 根据ServiceID获取白名单列表
     * @param serviceId
     * @return
     */
    public List<String> getWhileListByServiceId(String serviceId){
        return WHITE_LIST_MAP.get(serviceId);
    }

    /**
     * 更具serviceId获取前缀白名单
     * @param serviceId
     * @return
     */
    public List<String> getPrefixWhileListByServiceId(String serviceId){
        return WHITE_LIST_PREFIX_PATH_MAP.get(serviceId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        /**
         * 重新整理数据到Map便于查询
         */
        if(null == list || list.isEmpty()){
            WHITE_LIST_MAP = Collections.emptyMap();
            return;
        }
        WHITE_LIST_MAP = new HashMap<>(list.size(),1);
        WHITE_LIST_PREFIX_PATH_MAP = new HashMap<>(list.size(),1);
        List<String> tempList;
        List<String> originalList;
        List<String> prefixList = null;
        int index ;
        for(WhiteList whiteList :list){
            originalList = new ArrayList<>();
            tempList = whiteList.getWhiteList();
            for(String str : tempList){
                if(null == prefixList){
                    prefixList = new ArrayList<>();
                }
                index = str.lastIndexOf("*");
                if(index>0){
                    prefixList.add(str.substring(0,index));
                    continue;
                }
                index = str.lastIndexOf("**");
                if(index>0){
                    prefixList.add(str.substring(0,index));
                    continue;
                }
                originalList.add(str);
            }
            WHITE_LIST_MAP.put(whiteList.getServiceId(),originalList);
            if(null != prefixList){
                WHITE_LIST_PREFIX_PATH_MAP.put(whiteList.getServiceId(),prefixList);
            }
        }
        log.debug("[GlobalWhiteListProperties]Load White List Success,WHITE_LIST_MAP:{},WHITE_LIST_PREFIX_PATH_MAP:{}",WHITE_LIST_MAP,WHITE_LIST_PREFIX_PATH_MAP);
    }

    @Getter
    @Setter
    @ToString
    public static class WhiteList{
        private String serviceId;
        private List<String> whiteList;

    }

}
