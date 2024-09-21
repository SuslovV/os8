package ru.suslov.perfomance_statistics.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.suslov.perfomance_statistics.model.PerfomanceStatistics;
import ru.suslov.perfomance_statistics.repository.PerfomanceStatisticsRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class PerfomanceStatisticsService {

    private final PerfomanceStatisticsRepository perfomanceStatisticsRepository;
    private final ClassSourceCodeService classSourceCodeService;
    private final MethodService methodService;

    @Autowired
    public PerfomanceStatisticsService(PerfomanceStatisticsRepository perfomanceStatisticsRepository, ClassSourceCodeService classSourceCodeService, MethodService methodService) {
        this.perfomanceStatisticsRepository = perfomanceStatisticsRepository;
        this.classSourceCodeService = classSourceCodeService;
        this.methodService = methodService;
    }

    public Optional<PerfomanceStatistics> findById(UUID id) {
        return perfomanceStatisticsRepository.findById(id);
    }

    public Page<PerfomanceStatistics> findAll(Pageable pageable) {
//        try{
//            Thread.sleep(2000);
//        } catch (Exception e) {
//            log.error("sleep have gone wrong");
//        }
        return perfomanceStatisticsRepository.findAll(pageable);
    }

    public List<Object[]> getPerfomanceStatisticsGroupByMethod() {
        return perfomanceStatisticsRepository.getPerfomanceStatisticsGroupByMethod();
    }

    public PerfomanceStatistics save(PerfomanceStatistics perfomanceStatistics) {
        return perfomanceStatisticsRepository.save(perfomanceStatistics);
    }

    @Async
    public CompletableFuture<PerfomanceStatistics> save(String className, String methodName, long executionTime) {
        var classSourceCode= classSourceCodeService.findByName(className).orElseGet(() -> classSourceCodeService.add(className));
        var method= methodService.findByNameAndClassSourceCode(methodName, classSourceCode).orElseGet(() -> methodService.add(methodName, classSourceCode));

        var perfomanceStatistics = new PerfomanceStatistics();
        perfomanceStatistics.setMethod(method);
        perfomanceStatistics.setExecutionTime(executionTime);
        perfomanceStatistics.setCreatedTime(OffsetDateTime.now());
        perfomanceStatistics.setLastModifiedTime(perfomanceStatistics.getCreatedTime());

        return CompletableFuture.completedFuture(perfomanceStatisticsRepository.save(perfomanceStatistics));
    }

    public void delete(PerfomanceStatistics perfomanceStatistics) {
        perfomanceStatisticsRepository.delete(perfomanceStatistics);
    }

}
