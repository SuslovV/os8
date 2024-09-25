package ru.suslov.http_logging_spring_boot_starter.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import ru.suslov.http_logging_spring_boot_starter.model.RequestType;
import ru.suslov.http_logging_spring_boot_starter.service.HttpRequestLogService;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {

    private final HttpRequestLogService httpRequestLogService;

    @Autowired
    public RequestResponseLoggingInterceptor(HttpRequestLogService httpRequestLogService) {
        this.httpRequestLogService = httpRequestLogService;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        long startTime = System.currentTimeMillis();
        log.info("Начало выполнения исходящего http запроса {} {}", request.getMethod().name(), request.getURI().getPath());
        UUID httpRequestId = UUID.randomUUID();

        try {
            httpRequestLogService.save(request.getURI().getHost(), request.getURI().getPath(), request.getMethod().name(), RequestType.OUTGOING, request.getHeaders().toSingleValueMap(), null, false, null, httpRequestId);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        ClientHttpResponse response = execution.execute(request, body);

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        log.info("Исходящий http запрос {} {} выполнился за {} мс", request.getMethod().name(), request.getURI().getPath(), executionTime);

        try {
            httpRequestLogService.save(request.getURI().getHost(), request.getURI().getPath(), request.getMethod().name(), RequestType.OUTGOING, request.getHeaders().toSingleValueMap(), executionTime, true, response.getStatusCode().value(), httpRequestId);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return response;
    }
}
