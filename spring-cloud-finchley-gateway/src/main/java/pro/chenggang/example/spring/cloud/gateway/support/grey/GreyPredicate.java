package pro.chenggang.example.spring.cloud.gateway.support.grey;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateKey;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import lombok.extern.slf4j.Slf4j;
import pro.chenggang.example.spring.cloud.gateway.filter.CustomLoadBalancerClientFilter;
import pro.chenggang.example.spring.cloud.gateway.option.SystemParamConst;
import pro.chenggang.example.spring.cloud.gateway.support.GreyContext;

import java.util.Map;

/**
 * @classDesc:
 * @author: chenggang
 * @createTime: 2018/12/13
 * @version: v1.0.0
 */
@Slf4j
public class GreyPredicate extends AbstractServerPredicate {

    @Override
    public boolean apply(PredicateKey input) {
        Server server = input.getServer();
        if(server instanceof DiscoveryEnabledServer){
            /**
             * get DiscoveryEnabledServer
             */
            DiscoveryEnabledServer discoveryEnabledServer = (DiscoveryEnabledServer) server;
            GreyContext greyContext = CustomLoadBalancerClientFilter.contextThreadLocal.get();
            /**
             * greyContext is null ,return true
             */
            if(null == greyContext){
                log.debug("[GreyPredicate]GreyContext Is Null,Return True");
                return true;
            }
            /**
             * get version and metadata
             */
            String version = greyContext.getVersion();
            String serviceId = greyContext.getServiceId();
            InstanceInfo instanceInfo = discoveryEnabledServer.getInstanceInfo();
            Map<String, String> metadata = instanceInfo.getMetadata();
            /**
             * request param is matched
             */
            if(greyContext.isMatched()){
                /**
                 * version is matched return true
                 * PS:
                 * when request param is matched and version is matched
                 * the request should route to this (version) server
                 * the current server is (version) server
                 */
                if(version.equals(metadata.get(SystemParamConst.VERSION))){
                    log.debug("[GreyPredicate](GreyContext Param Matched)GreyContext Version Matched,Return True,ServiceId:{},InstanceId:{},Instance Metadata:{}",serviceId,instanceInfo.getInstanceId(),metadata);
                    return true;
                }
                /**
                 * version is not matched return false
                 * PS:
                 * when request param is matched and version is not matched
                 * the request should route to the other(un-version) server
                 * the current server is (version) server
                 */
                log.debug("[GreyPredicate](GreyContext Param Matched)GreyContext Version Not Matched,Return False,ServiceId:{},InstanceId:{},Instance Metadata:{}",serviceId,instanceInfo.getInstanceId(),metadata);
                return false;
            }else{
                /**
                 * request param is not matched
                 */
                /**
                 * version is matched return false
                 * PS:
                 * when request param is not matched and version is matched
                 * the request should route to the other(un-version) server
                 * the current server is (version) server
                 */
                if(version.equals(metadata.get(SystemParamConst.VERSION))){
                    log.debug("[GreyPredicate](GreyContext Param Matched)GreyContext Version Matched,Return False,ServiceId:{},InstanceId:{},Instance Metadata:{}",serviceId,instanceInfo.getInstanceId(),metadata);
                    return false;
                }
                /**
                 * version is not matched return true
                 * PS:
                 * when request param is not matched and version is not matched
                 * the request should route to the this(un-version) server
                 * the current server is (un-version) server
                 */
                log.debug("[GreyPredicate](GreyContext Param Matched)GreyContext Version Not Matched,Return True,ServiceId:{},InstanceId:{},Instance Metadata:{}",serviceId,instanceInfo.getInstanceId(),metadata);
                return true;
            }
        }
        log.debug("[GreyPredicate]Not DiscoveryEnabledServer,Return True ");
        return true;
    }
}
