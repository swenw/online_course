package com.school.example.infrastructure.apigateway.filter;

import com.example.school.common.base.util.JwtUtils;
import com.google.gson.JsonObject;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 判断路径中是否含有 /a[i/**/auth/**， 有的话则需要鉴权
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        if (antPathMatcher.match("/api/**/auth/**", path)) {
            List<String> tokenList = request.getHeaders().get("token");
            //没有token
            if(null == tokenList) {
                ServerHttpResponse response = exchange.getResponse();
                return out(response); // 返回下面定义的out函数中的json
            }
            //token校验失败
            Boolean isCheck = JwtUtils.checkJwtTToken(tokenList.get(0));
            if(!isCheck) {
                ServerHttpResponse response = exchange.getResponse();
                return out(response);
            }
        }
        // 放行: 使用过滤器链将请求继续向下传递
        return chain.filter(exchange);
    }

    // 定义当前过滤器的优先级，值越小，优先级越高
    // 为0，永远为先访问的过滤器
    @Override
    public int getOrder() {
        return 0;
    }

    // 使用Webflux输入请求信息
    private Mono<Void> out(ServerHttpResponse response) {

        JsonObject message = new JsonObject();
        message.addProperty("success", false);
        message.addProperty("code", 28004);
        message.addProperty("data", "");
        message.addProperty("message", "鉴权失败");
        byte[] bytes = message.toString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        //输出http响应
        return response.writeWith(Mono.just(buffer));
    }
}
