package ru.suslov.perfomance_statistics.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.suslov.perfomance_statistics.service.PerfomanceStatisticsService;

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

    @Pointcut("@annotation(ru.suslov.perfomance_statistics.annotation.TrackTime)")
    public void trackTimePointcut() {
    }

    @Pointcut("@annotation(ru.suslov.perfomance_statistics.annotation.TrackAsyncTime)")
    public void trackAsyncTimePointcut() {
    }

    private void savePerfomanceStatistics(long startTime, String className, String methodName, Object result) {
        long endTime = System.currentTimeMillis();
        perfomanceStatisticsService.save(className, methodName, endTime - startTime);
        log.info("Метод {} выполнился за {} мс с результатом {}", methodName, endTime - startTime, result);
    }

    private Object trackTime(ProceedingJoinPoint proceedingJoinPoint, boolean trackAsync) throws Throwable {
        long startTime = System.currentTimeMillis();

        String className = proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = proceedingJoinPoint.getSignature().getName();

        log.info("Выполнение метода {}", methodName);

        Object result = proceedingJoinPoint.proceed();

        try {
            if (!trackAsync) {
                savePerfomanceStatistics(startTime, className, methodName, result);
            } else {
                CompletableFuture future = (CompletableFuture) result;
                future.thenAccept(o -> savePerfomanceStatistics(startTime, className, methodName, result));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return result;
    }

    @Around("trackTimePointcut()")
    public Object trackTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return trackTime(proceedingJoinPoint, false);
    }

    @Around("trackAsyncTimePointcut()")
    public Object trackAsyncTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return trackTime(proceedingJoinPoint, true);
    }
}
