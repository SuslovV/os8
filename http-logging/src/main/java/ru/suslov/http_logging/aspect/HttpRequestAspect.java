package ru.suslov.http_logging.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.suslov.http_logging.service.HttpRequestLogService;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@Aspect
@Slf4j
public class HttpRequestAspect {

    private final HttpRequestLogService httpRequestLogService;

    @Autowired
    public HttpRequestAspect(HttpRequestLogService httpRequestLogService) {
        this.httpRequestLogService = httpRequestLogService;
    }

    @Pointcut("execution(@(@org.springframework.web.bind.annotation.RequestMapping *) * *(..))")
    public void trackHttpRequestPointcut() {
    }

    private void savePerfomanceStatistics(long startTime, String className, String methodName, Object result, String method, boolean response, UUID httpRequestId) {
        Long executionTime = null;
        if (response) {
            long endTime = System.currentTimeMillis();
            executionTime = endTime - startTime;
            log.info("Метод {} выполнился за {} мс с результатом {}", methodName, executionTime, result);
        }
        httpRequestLogService.save(className, methodName, method, executionTime, response, httpRequestId);
    }

    private Object trackHttpRequest(ProceedingJoinPoint proceedingJoinPoint, boolean trackAsync) throws Throwable {
        long startTime = System.currentTimeMillis();

        String className = proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = proceedingJoinPoint.getSignature().getName();

        log.info("Выполнение метода {}", methodName);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        UUID httpRequestId = UUID.randomUUID();
        try {
//            if (!trackAsync) {
                savePerfomanceStatistics(startTime, className, methodName, null, request.getMethod(), false, httpRequestId);
//            } else {
//                CompletableFuture future = (CompletableFuture) result;
//                future.thenAccept(o -> savePerfomanceStatistics(startTime, className, methodName, result));
//            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        request.getMethod();

        Object result = proceedingJoinPoint.proceed();

        try {
            if (!trackAsync) {
                savePerfomanceStatistics(startTime, className, methodName, result, request.getMethod(), true, httpRequestId);
            } else {
                CompletableFuture future = (CompletableFuture) result;
                future.thenAccept(o -> savePerfomanceStatistics(startTime, className, methodName, result, request.getMethod(), true, httpRequestId));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return result;
    }

    @Around("trackHttpRequestPointcut()")
    public Object trackHttpRequest(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return trackHttpRequest(proceedingJoinPoint, false);
    }

}
