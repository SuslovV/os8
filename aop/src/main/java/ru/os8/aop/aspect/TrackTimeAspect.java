package ru.os8.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import ru.os8.aop.service.PerfomanceStatisticsService;

import java.util.concurrent.CompletableFuture;

@Component
@Aspect
@Slf4j
public class TrackTimeAspect {

    private final PerfomanceStatisticsService perfomanceStatisticsService;

    @Autowired
    public TrackTimeAspect(PerfomanceStatisticsService perfomanceStatisticsService) {
        this.perfomanceStatisticsService = perfomanceStatisticsService;
    }

    @Pointcut("@annotation(ru.os8.aop.annotation.TrackTime)")
    public void trackTimePointcut() {
    }

    @Pointcut("@annotation(ru.os8.aop.annotation.TrackAsyncTime)")
    public void trackAsyncTimePointcut() {
    }

    public Object track(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        String className = proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = proceedingJoinPoint.getSignature().getName();
        Object[] methodArgs = proceedingJoinPoint.getArgs();

        log.info("Выполнение метода {} с аргументами {}", methodName, methodArgs);

        Object result = proceedingJoinPoint.proceed();

        long endTime = System.currentTimeMillis();

        perfomanceStatisticsService.save(className, methodName, endTime - startTime);

        log.info("Метод {} выполнился за {} мс с результатом {}", methodName, endTime - startTime, result);
        return result;
    }

    @Around("trackTimePointcut()")
    public Object trackTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return track(proceedingJoinPoint);
    }

    @Around("trackAsyncTimePointcut()")
    public Object trackAsyncTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        String className = proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = proceedingJoinPoint.getSignature().getName();
        Object[] methodArgs = proceedingJoinPoint.getArgs();

        Object proceed = proceedingJoinPoint.proceed();
        if (proceed instanceof CompletableFuture) {
            CompletableFuture future = (CompletableFuture) proceed;
            future.thenAccept(futureReturnValue -> {

                if (futureReturnValue instanceof AsyncResult) {
                    AsyncResult result = (AsyncResult) futureReturnValue;
                    if (result.isDone()) {
                        log.info("Метод {} выполнился за {} мс с результатом {}", result);
//                        monitoringService.end(result.getRunTime());
                    } else {
//                        monitoringService.processException(result.getException());
                    }
                }
            });
        } else {
            log.error("Async method does not return CompletableFuture");
        }

        long endTime = System.currentTimeMillis();

        perfomanceStatisticsService.save(className, methodName, endTime - startTime);

        return proceed;
    }
}
