package com.metrics.Metric.Models.Metric;

import com.metrics.Metric.Models.Metric.Metric;
import com.metrics.Metric.Models.Metric.MetricID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface MetricRepository extends JpaRepository<Metric, MetricID> {
    @Query("SELECT m FROM Metric m " +
            "WHERE m.system = :system " +
            "AND (COALESCE(:name, '') = '' OR m.name = :name) " +
            "AND (COALESCE(:from, 0) = 0 OR m.date >= :from) " +
            "AND (COALESCE(:to, 0) = 0 OR m.date <= :to)")
    List<Metric> findFilteredMetrics(
            @Param("system") String system,
            @Param("name") String name,
            @Param("from") Integer from,
            @Param("to") Integer to
    );

    List<Metric> findBySystem(String system);
    Optional<Metric> findById(Integer id);

    Optional<Metric> findFirstByOrderByDateDesc();
}