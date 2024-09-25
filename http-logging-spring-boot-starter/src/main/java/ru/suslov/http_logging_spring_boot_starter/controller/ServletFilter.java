package ru.suslov.http_logging_spring_boot_starter.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.suslov.http_logging_spring_boot_starter.model.RequestType;
import ru.suslov.http_logging_spring_boot_starter.service.HttpRequestLogService;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ServletFilter implements Filter {

    @Autowired
    private HttpRequestLogService httpRequestLogService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();

        HttpServletRequest req = (HttpServletRequest) request;
        log.info("Начало выполнения входящего http запроса {} {} ", req.getMethod(), req.getRequestURI());

        Map<String, String> requestHeaders = Collections.list(req.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, req::getHeader));

        UUID httpRequestId = UUID.randomUUID();
        try {
            httpRequestLogService.save(req.getServerName(), req.getRequestURI(), req.getMethod(), RequestType.INCOMING, requestHeaders, null, false, null, httpRequestId);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        HttpServletResponse res = (HttpServletResponse) response;

        chain.doFilter(request, response);

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        log.info("Входящий http запрос {} {} выполнился за {} мс ", req.getMethod(), req.getRequestURI(), executionTime);

        Map<String, String> responseHeaders = res.getHeaderNames().stream().collect(Collectors.toMap(h -> h, res::getHeader));

        try {
            httpRequestLogService.save(req.getServerName(), req.getRequestURI(), req.getMethod(), RequestType.INCOMING, responseHeaders, executionTime, true, res.getStatus(), httpRequestId);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
