package ru.suslov.http_logging_spring_boot_starter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.suslov.http_logging_spring_boot_starter.model.HttpRequestLog;
import ru.suslov.http_logging_spring_boot_starter.model.RequestType;
import ru.suslov.http_logging_spring_boot_starter.model.Resource;
import ru.suslov.http_logging_spring_boot_starter.model.Server;
import ru.suslov.http_logging_spring_boot_starter.repository.HttpRequestLogRepository;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class HttpRequestLogService {

    @Autowired
    private ServerService serverService;
    @Autowired
    private HttpRequestLogRepository httpRequestLogRepository;
    @Autowired
    private ResourceService resourceService;

    public Optional<HttpRequestLog> findById(UUID id) {
        return httpRequestLogRepository.findById(id);
    }

    public Page<HttpRequestLog> findAll(Pageable pageable) {
//        try{
//            Thread.sleep(2000);
//        } catch (Exception e) {
//            log.error("sleep have gone wrong");
//        }
        return httpRequestLogRepository.findAll(pageable);
    }

    public HttpRequestLog save(HttpRequestLog httpRequestLog) {
        return httpRequestLogRepository.save(httpRequestLog);
    }

    @Async
    public CompletableFuture<HttpRequestLog> save(String serverName, String resourceName, String method, RequestType requestType, Map<String, String> headers, Long executionTime, boolean response, Integer httpStatus, UUID httpRequestId) {
        var server = serverService.findByName(serverName).orElseGet(() -> {
            Server serverAdded;
            try {
                serverAdded = serverService.add(serverName);
            } catch (Throwable e) {
                // Если при первичном добавлении возникнет SQL ошибка уникальности индекса по полю 'name', попробуем найти еще раз
                try {
                    Thread.sleep(200);
                } catch (Exception ex) {
                    log.error("sleep have gone wrong");
                }
                serverAdded = serverService.findByName(serverName).orElseGet(() -> serverService.add(serverName));
            }
            return serverAdded;
        }); // todo сразу поиск  ресурса по имени и сервер id
        var resource = resourceService.findByNameAndServer(resourceName, server).orElseGet(() -> {
            Resource resourceAdded;
            try {
                resourceAdded = resourceService.add(resourceName, server);
            } catch (Throwable e) {
                // Если при первичном добавлении возникнет SQL ошибка уникальности индекса по полю 'name' и 'server_id', попробуем найти еще раз
                try {
                    Thread.sleep(200);
                } catch (Exception ex) {
                    log.error("sleep have gone wrong");
                }
                resourceAdded = resourceService.findByNameAndServer(resourceName, server).orElseGet(() -> resourceService.add(resourceName, server));
            }
            return resourceAdded;
        });

        var httpRequestLog = new HttpRequestLog();
        httpRequestLog.setResource(resource);
        httpRequestLog.setMethod(method);
        httpRequestLog.setRequestType(requestType);
        httpRequestLog.setHeaders(headers);
        httpRequestLog.setResponse(response);
        httpRequestLog.setHttpStatus(httpStatus);
        httpRequestLog.setHttpRequestId(httpRequestId);
        httpRequestLog.setExecutionTime(executionTime);
        httpRequestLog.setCreatedTime(OffsetDateTime.now());
        httpRequestLog.setLastModifiedTime(httpRequestLog.getCreatedTime());

        return CompletableFuture.completedFuture(httpRequestLogRepository.save(httpRequestLog));
    }

    public void delete(HttpRequestLog httpRequestLog) {
        httpRequestLogRepository.delete(httpRequestLog);
    }

}
