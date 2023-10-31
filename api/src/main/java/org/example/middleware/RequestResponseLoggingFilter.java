package org.example.middleware;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI()
                + (StringUtils.isEmpty(request.getQueryString()) ? "" : "?" + request.getQueryString());
        log.info("Start request {} - [{}]", request.getMethod(), uri);

        filterChain.doFilter(request, response);
        log.info("Done request [{}] with response status {}", uri, response.getStatus());
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "/".equals(request.getRequestURI()) || request.getRequestURI().contains("actuator");
    }
}
