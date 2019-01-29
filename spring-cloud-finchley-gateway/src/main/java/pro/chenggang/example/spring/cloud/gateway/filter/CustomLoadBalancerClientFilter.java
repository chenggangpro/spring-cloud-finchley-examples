package pro.chenggang.example.spring.cloud.gateway.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.web.server.ServerWebExchange;
import pro.chenggang.example.spring.cloud.gateway.support.GreyContext;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_SCHEME_PREFIX_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

/**
 * @classDesc:
 * @author: chenggang
 * @createTime: 2018/12/11
 * @version: v1.0.0
 */
public class CustomLoadBalancerClientFilter extends LoadBalancerClientFilter {

    private static final Log log = LogFactory.getLog(CustomLoadBalancerClientFilter.class);

    public static ThreadLocal<GreyContext> contextThreadLocal = new ThreadLocal<>();

    public CustomLoadBalancerClientFilter(LoadBalancerClient loadBalancer) {
        super(loadBalancer);
    }

    @Override
    public int getOrder() {
        return LOAD_BALANCER_CLIENT_FILTER_ORDER;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        String schemePrefix = exchange.getAttribute(GATEWAY_SCHEME_PREFIX_ATTR);
        if (url == null || (!"lb".equals(url.getScheme()) && !"lb".equals(schemePrefix))) {
            return chain.filter(exchange);
        }
        //preserve the original url
        addOriginalRequestUrl(exchange, url);

        log.trace("LoadBalancerClientFilter url before: " + url);

        /**
         * format grey context
         */
        GreyContext greyContext = exchange.getAttribute(GreyContext.CACHE_GREY_CONTEXT);
        contextThreadLocal.set(greyContext);
        final ServiceInstance instance = choose(exchange);
        contextThreadLocal.remove();
        if (instance == null) {
            throw new NotFoundException("Unable to find instance for " + url.getHost());
        }

        URI uri = exchange.getRequest().getURI();

        // if the `lb:<scheme>` mechanism was used, use `<scheme>` as the default,
        // if the loadbalancer doesn't provide one.
        String overrideScheme = null;
        if (schemePrefix != null) {
            overrideScheme = url.getScheme();
        }

        URI requestUrl = loadBalancer.reconstructURI(new DelegatingServiceInstance(instance, overrideScheme), uri);

        log.trace("LoadBalancerClientFilter url chosen: " + requestUrl);
        exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, requestUrl);
        return chain.filter(exchange);
    }

    @Override
    protected ServiceInstance choose(ServerWebExchange exchange) {
        return loadBalancer.choose(((URI) exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR)).getHost());
    }

    class DelegatingServiceInstance implements ServiceInstance {
        final ServiceInstance delegate;
        private String overrideScheme;

        DelegatingServiceInstance(ServiceInstance delegate, String overrideScheme) {
            this.delegate = delegate;
            this.overrideScheme = overrideScheme;
        }

        @Override
        public String getServiceId() {
            return delegate.getServiceId();
        }

        @Override
        public String getHost() {
            return delegate.getHost();
        }

        @Override
        public int getPort() {
            return delegate.getPort();
        }

        @Override
        public boolean isSecure() {
            return delegate.isSecure();
        }

        @Override
        public URI getUri() {
            return delegate.getUri();
        }

        @Override
        public Map<String, String> getMetadata() {
            return delegate.getMetadata();
        }

        @Override
        public String getScheme() {
            String scheme = delegate.getScheme();
            if (scheme != null) {
                return scheme;
            }
            return this.overrideScheme;
        }

    }

}
