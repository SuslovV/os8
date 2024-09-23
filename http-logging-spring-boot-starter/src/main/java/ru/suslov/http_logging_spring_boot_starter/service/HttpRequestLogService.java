package ru.suslov.http_logging_spring_boot_starter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.suslov.http_logging_spring_boot_starter.model.HttpRequestLog;
import ru.suslov.http_logging_spring_boot_starter.repository.HttpRequesLogtRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class HttpRequestLogService {

    @Autowired
    private  ServerService serverService;
    private  HttpRequesLogtRepository httpRequesLogtRepository;
    private  ResourceService resourceService;

//    @Autowired
//    public HttpRequestLogService(HttpRequesLogtRepository httpRequesLogtRepository, ServerService serverService, ResourceService resourceService) {
//        this.httpRequesLogtRepository = httpRequesLogtRepository;
//        this.serverService = serverService;
//        this.resourceService = resourceService;
//    }

    public Optional<HttpRequestLog> findById(UUID id) {
        return httpRequesLogtRepository.findById(id);
    }

    public Page<HttpRequestLog> findAll(Pageable pageable) {
//        try{
//            Thread.sleep(2000);
//        } catch (Exception e) {
//            log.error("sleep have gone wrong");
//        }
        return httpRequesLogtRepository.findAll(pageable);
    }

    public List<Object[]> getPerfomanceStatisticsGroupByMethod() {
        return httpRequesLogtRepository.getHttpRequestGroupByMethod();
    }

    public HttpRequestLog save(HttpRequestLog httpRequestLog) {
        return httpRequesLogtRepository.save(httpRequestLog);
    }

    @Async
    public CompletableFuture<HttpRequestLog> save(String serverName, String resourceName, String method, Map<String, String> headers, Long executionTime, boolean response, UUID httpRequestId) {
        var server = serverService.findByName(serverName).orElseGet(() -> serverService.add(serverName)); // todo  сразу поиск  ресурса по имени и сервер id
        var resource = resourceService.findByNameAndServer(resourceName, server).orElseGet(() -> resourceService.add(resourceName, server));

        var httpRequestLog = new HttpRequestLog();
        httpRequestLog.setResource(resource);
        httpRequestLog.setMethod(method);
        httpRequestLog.setHeaders(headers);
        httpRequestLog.setResponse(response);
        httpRequestLog.setHttpRequestId(httpRequestId);
        httpRequestLog.setExecutionTime(executionTime);
        httpRequestLog.setCreatedTime(OffsetDateTime.now());
        httpRequestLog.setLastModifiedTime(httpRequestLog.getCreatedTime());

        return CompletableFuture.completedFuture(httpRequesLogtRepository.save(httpRequestLog));
    }

    public void delete(HttpRequestLog httpRequestLog) {
        httpRequesLogtRepository.delete(httpRequestLog);
    }

}
