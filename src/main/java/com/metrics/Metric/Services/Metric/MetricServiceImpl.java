package com.metrics.Metric.Services.Metric;

import com.metrics.Metric.DTOs.MetricDTO;
import com.metrics.Metric.Models.Metric.Metric;
import com.metrics.Metric.Models.Metric.MetricRepository;
import com.metrics.Metric.Services.Metric.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class MetricServiceImpl implements MetricService {
    private final MetricRepository metricRepository;

    int timestamp = (int) Instant.now().getEpochSecond();

    @Autowired
    public MetricServiceImpl(MetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    /**
     * @Functionality This method performs validation checks and then returns all metrics within the database as an array based upon the system and optional name, from / to parameters
     * @param system
     * @param name
     * @param from
     * @param to
     * @return status code and body
     */
    @Override
    public ResponseEntity<?> getMetrics(String system, String name, int from, int to) {
        if(system.isEmpty() || system.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A required parameter was not supplied or is invalid");
        }

        List<Metric> metricList = metricRepository.findFilteredMetrics(
                system, name, from, to
        );

        return ResponseEntity.ok(metricList);
    }

    /**
     * @Functionality This method performs validation checks and then returns a single metric based on the id param
     * @param id
     * @return status code and body
     */
    @Override
    public ResponseEntity<?> getMetric(Integer id) {
        if(id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A required parameter was not supplied or is invalid");
        }

        Optional<Metric> metric = metricRepository.findById(id);

        if(metric.isPresent()) {
            return ResponseEntity.ok(metric.get());
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The specified metric was not found");
        }
    }

    /**
     * @Functionality This method performs validation checks and then creates a new metric instance
     * @param metricDTO
     * @return true or false depending on whether the metric was successfully created
     */
    @Override
    public Boolean createMetric(MetricDTO metricDTO) {
        int value = 1;
        if(metricDTO.getSystem().isEmpty() || metricDTO.getSystem().isBlank() || metricDTO.getName().isEmpty() || metricDTO.getSystem().isBlank()) {
            return false;
        }

        if(metricDTO.getValue() != null && metricDTO.getValue() != 1) {
            value = metricDTO.getValue();
        }

        Metric metric = new Metric(
                metricDTO.getSystem(), metricDTO.getName(),
                timestamp, value
        );

        metricRepository.save(metric);
        return true;
    }

    /**
     * @Functionality This method performs validation checks and then updates an already existing metric within the database
     * @param id
     * @param metricDTO
     * @return status code and body
     */
    @Override
    public ResponseEntity<?> updateMetric(Integer id, MetricDTO metricDTO) {
        Metric metric = metricRepository.findById(id).orElse(null);

        if(metric == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The specified metric was not found");
        }

        if(metricDTO.getSystem().isEmpty() || metricDTO.getSystem().isBlank() || metricDTO.getName().isEmpty() || metricDTO.getName().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A required parameter was not supplied or is invalid, or system or name does not match the existing metric");
        }

        metric.setSystem(metricDTO.getSystem());
        metric.setName(metricDTO.getName());
        metric.setDate(timestamp);

        if(metricDTO.getValue() == null) {
            metric.setValue(metric.getValue() + 1);
        } else {
            metric.setValue(metricDTO.getValue());
        }
        metricRepository.save(metric);
        return ResponseEntity.ok("The metric was recorded");
    }

    /**
     * @Functionality This method retrieves all metrics based upon the system and optional name, from / to parameters and adds the value for a total value
     * @param system
     * @param name
     * @param from
     * @param to
     * @return status code and body
     */
    @Override
    public ResponseEntity<?> getMetricSummary(String system, String name, int from, int to) {
        int summaryValue = 0;
        if(system.isEmpty() || system.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A required parameter was not supplied or is invalid");
        }

        List<Metric> metricList = metricRepository.findFilteredMetrics(
                system, name, from, to
        );

        for (Metric value : metricList) {
            summaryValue += value.getValue();
        }

        return ResponseEntity.ok(summaryValue);
    }
}