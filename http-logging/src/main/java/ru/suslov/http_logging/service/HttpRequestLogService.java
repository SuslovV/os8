package ru.suslov.http_logging.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.suslov.http_logging.model.HttpRequestLog;
import ru.suslov.http_logging.repository.HttpRequesLogtRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class HttpRequestLogService {

    private final ClassSourceCodeService classSourceCodeService;
    private final HttpRequesLogtRepository httpRequesLogtRepository;
    private final MethodSourceCodeService methodSourceCodeService;

    @Autowired
    public HttpRequestLogService(HttpRequesLogtRepository httpRequesLogtRepository, ClassSourceCodeService classSourceCodeService, MethodSourceCodeService methodSourceCodeService) {
        this.httpRequesLogtRepository = httpRequesLogtRepository;
        this.classSourceCodeService = classSourceCodeService;
        this.methodSourceCodeService = methodSourceCodeService;
    }

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
    public CompletableFuture<HttpRequestLog> save(String className, String methodName, String method, Long executionTime, boolean response, UUID httpRequestId) {
        var classSourceCode = classSourceCodeService.findByName(className).orElseGet(() -> classSourceCodeService.add(className)); // todo  сразу поиск метода
        var methodSourceCode = methodSourceCodeService.findByNameAndClassSourceCode(methodName, classSourceCode).orElseGet(() -> methodSourceCodeService.add(methodName, classSourceCode));

        var httpRequest = new HttpRequestLog();
        httpRequest.setMethodSourceCode(methodSourceCode);
        httpRequest.setMethod(method);
        httpRequest.setResponse(response);
        httpRequest.setHttpRequestId(httpRequestId);
        httpRequest.setExecutionTime(executionTime);
        httpRequest.setCreatedTime(OffsetDateTime.now());
        httpRequest.setLastModifiedTime(httpRequest.getCreatedTime());

        return CompletableFuture.completedFuture(httpRequesLogtRepository.save(httpRequest));
    }

    public void delete(HttpRequestLog httpRequestLog) {
        httpRequesLogtRepository.delete(httpRequestLog);
    }

}
