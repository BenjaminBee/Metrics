package com.metrics.Metric.Controllers;

import com.metrics.Metric.DTOs.MetricDTO;
import com.metrics.Metric.Services.Metric.MetricService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Mapping /metric/metrics [GET]
 * @Mapping /metric/metrics/{id} [GET]
 * @Mapping /metric/metrics [POST]
 * @Mapping /metric/metrics/{id} [PUT]
 * @Mapping /metric/metricsummary [GET]
 */
@RestController
@RequestMapping(path = "/metric")
public class MetricController {
    private final MetricService metricService;

    @Autowired
    public MetricController(MetricService metricService) {
        this.metricService = metricService;
    }

    Logger logger = LoggerFactory.getLogger(MetricController.class);

    @GetMapping(path = "/metrics")
    public ResponseEntity<?> getMetrics(@RequestParam(name = "system") String system,
                                        @RequestParam(name = "name", required = false) String name,
                                        @RequestParam(name = "from", required = false) Integer from,
                                        @RequestParam(name = "to", required = false) Integer to){

        int fromValue = (from != null) ? from.intValue() : 0;
        int toValue = (to != null) ? to.intValue() : 0;

        logger.info(system + ": system value");
        logger.info(name + ": name value");
        logger.info(fromValue + ": fromValue value");
        logger.info(toValue + ": toValue value");

        logger.info("Metrics retrieved");
        return metricService.getMetrics(system, name, fromValue, toValue);
    }

    @GetMapping(path = "/metrics/{id}")
    public ResponseEntity<?> getMetrics(@PathVariable("id") Integer id){
        return metricService.getMetric(id);
    }

    @PostMapping(path = "/metrics")
    public ResponseEntity<?> createMetric(@RequestBody MetricDTO metricDTO) {
        if(metricService.createMetric(metricDTO)) {
            logger.info("Metric created successfully with: " + metricDTO.getSystem() + " /" + metricDTO.getName());
            return ResponseEntity.ok("The metric was recorded");
        }
        logger.error("Metric created unsuccessfully with: " + metricDTO.getSystem() + " /" + metricDTO.getName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A required parameter was not supplied or is invalid");
    }

    @PutMapping(path = "/metrics/{id}")
    public ResponseEntity<?> updateMetric(@PathVariable("id") Integer id,
                                          @RequestBody MetricDTO metricDTO) {
        logger.info("Metric update function called with: " + id + " and " + metricDTO.getSystem() + " /" + metricDTO.getName());
        return metricService.updateMetric(id, metricDTO);
    }

    @GetMapping(path = "/metricsummary")
    public ResponseEntity<?> getMetricSummary(@RequestParam("system") String system,
                                              @RequestParam(name = "name", required = false) String name,
                                              @RequestParam(name = "from", required = false) Integer from,
                                              @RequestParam(name = "to", required = false) Integer to) {

        int fromValue = (from != null) ? from.intValue() : 0;
        int toValue = (to != null) ? to.intValue() : 0;

        logger.info(system + ": system value");
        logger.info(name + ": name value");
        logger.info(fromValue + ": fromValue value");
        logger.info(toValue + ": toValue value");

        logger.info("Metric value total calculated with: " + system + " /" + name + " /" + fromValue + " /" +toValue);
        return metricService.getMetricSummary(system, name, fromValue, toValue);
    }
}
