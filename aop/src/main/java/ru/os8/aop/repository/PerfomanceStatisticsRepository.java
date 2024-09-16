package ru.os8.aop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.os8.aop.model.PerfomanceStatistics;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PerfomanceStatisticsRepository extends JpaRepository<PerfomanceStatistics, UUID> {
    Optional<PerfomanceStatistics> findById(UUID id);

    @Query(value =
            "SELECT m.id     method_id,\n" +
                    "       csc.name class_name,\n" +
                    "       m.name   method_name,\n" +
                    "       execution_time_sum,\n" +
                    "       execution_time_avg,\n" +
                    "       execution_time_min,\n" +
                    "       execution_time_max,\n" +
                    "       count_call\n" +
                    "FROM (SELECT method_id,\n" +
                    "             CAST(SUM(execution_time) AS BIGINT) execution_time_sum,\n" +
                    "             CAST(AVG(execution_time) AS BIGINT) execution_time_avg,\n" +
                    "             CAST(MIN(execution_time) AS BIGINT) execution_time_min,\n" +
                    "             CAST(MAX(execution_time) AS BIGINT) execution_time_max,\n" +
                    "             CAST(count(*) AS BIGINT)            count_call\n" +
                    "      FROM perfomance_statistics\n" +
                    "      GROUP BY method_id) ps\n" +
                    "         LEFT JOIN method m on m.id = ps.method_id\n" +
                    "         LEFT JOIN class_source_code csc on csc.id = m.class_source_code_id"
            , nativeQuery = true)
    List<Object[]> getPerfomanceStatisticsGroupByMethod();

}
