package ru.suslov.http_logging_spring_boot_starter.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.suslov.http_logging_spring_boot_starter.service.HttpRequestLogService;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RequestResponseLoggingFilter implements Filter {

    @Autowired
    private HttpRequestLogService httpRequestLogService;

//    @Autowired
//    public RequestResponseLoggingFilter(HttpRequestLogService httpRequestLogService) {
//        this.httpRequestLogService = httpRequestLogService;
//    }

    private void savePerfomanceStatistics(long startTime, String server, String uri, String method, Map<String, String> headers, boolean response, UUID httpRequestId) {
        Long executionTime = null;
        if (response) {
            long endTime = System.currentTimeMillis();
            executionTime = endTime - startTime;
            log.info("Метод {} выполнился за {} мс ", uri, executionTime);
        }
        httpRequestLogService.save(server, uri, method, headers, executionTime, response, httpRequestId);
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();

        HttpServletRequest req = (HttpServletRequest) request;


        Map<String, String> requestHeaders = Collections.list(req.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, req::getHeader));

        UUID httpRequestId = UUID.randomUUID();
        try {
            savePerfomanceStatistics(startTime, req.getServerName(), req.getRequestURI(), req.getMethod(), requestHeaders, false, httpRequestId);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        HttpServletResponse res = (HttpServletResponse) response;
        log.info(
                "Logging Request  {} : {}", req.getMethod(),
                req.getRequestURI());
        chain.doFilter(request, response);

        Map<String, String> responseHeaders = res.getHeaderNames().stream().collect(Collectors.toMap(h -> h, res::getHeader));

        try {
            savePerfomanceStatistics(startTime, req.getServerName(), req.getRequestURI(), req.getMethod(), responseHeaders, true, httpRequestId);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        log.info("Logging Response :{}", res.getContentType());
    }

}
