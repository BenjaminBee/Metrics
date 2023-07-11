package com.metrics.Metric.Services.Metric;

import com.metrics.Metric.DTOs.MetricDTO;
import org.springframework.http.ResponseEntity;

public interface MetricService {
    public ResponseEntity<?> getMetrics(String system, String name, int from, int to);
    public ResponseEntity<?> getMetric(Integer id);
    public Boolean createMetric(MetricDTO metricDTO);
    public ResponseEntity<?> updateMetric(Integer id, MetricDTO metricDTO);
    public ResponseEntity<?> getMetricSummary(String system, String name, int from, int to);
}