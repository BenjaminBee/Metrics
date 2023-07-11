package com.metrics.Metric;

import com.metrics.Metric.Controllers.MetricController;
import com.metrics.Metric.DTOs.MetricDTO;
import com.metrics.Metric.Models.Metric.Metric;
import com.metrics.Metric.Models.Metric.MetricRepository;
import com.metrics.Metric.Services.Metric.MetricService;
import com.metrics.Metric.Services.Metric.MetricServiceImpl;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MetricServiceTests {
    @Mock
    MetricRepository metricRepository;

    /**
     * This method for Injection works however this should change to mocking the MetricService to fully utilise
     * the medium of an Interface for running unit tests rather than using the Implementation.
     */
    @InjectMocks
    MetricServiceImpl metricService;
    int timestamp = (int) Instant.now().getEpochSecond();

    @Before
    public void setUp() {
        Metric metric = new Metric();
        for(int i = 0; i < 10; i++) {
            metric.setId(i + 1);
            metric.setSystem("testSystem" + i);
            metric.setName("testName" + i);
            metric.setDate(timestamp);
            metric.setValue(1);
            metricRepository.save(metric);
        }
    }

    @Test
    public void testGetMetricsWhenSystemParameterIsEmpty() {
        String system = "";
        String name = "test";
        int from = 0;
        int to = 1;

        ResponseEntity<?> response = metricService.getMetrics(system, name, from, to);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("A required parameter was not supplied or is invalid", response.getBody());
    }

    @Test
    public void testGetMetricsWhenSystemParameterIsPresent() {
        String system = "testSystem1";
        String name = "";
        int from = 0;
        int to = 0;

        ResponseEntity<?> response = metricService.getMetrics(system, name, from, to);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetMetricsWhenSystemAndNameParameterArePresent() {
        String system = "testSystem1";
        String name = "testName1";
        int from = 0;
        int to = 0;

        ResponseEntity<?> response = metricService.getMetrics(system, name, from, to);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetMetricsWhenSystemAndNameAndFromParameterArePresent() {
        String system = "testSystem1";
        String name = "testName1";
        int from = timestamp;
        int to = 0;

        ResponseEntity<?> response = metricService.getMetrics(system, name, from, to);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetMetricsWhenSystemAndNameAndFromAndToParameterArePresent() {
        String system = "testSystem1";
        String name = "testName1";
        int from = timestamp;
        int to = timestamp;

        ResponseEntity<?> response = metricService.getMetrics(system, name, from, to);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetMetricWhenIdIsNull() {
        Integer id = null;

        ResponseEntity<?> response = metricService.getMetric(id);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testGetMetricWhenIdIsPresent() {
        Metric metric = new Metric();
        metric.setId(1);
        metric.setSystem("testSystem");
        metric.setName("testName");
        metric.setDate(timestamp);
        metric.setValue(1);
        when(metricRepository.findById(1)).thenReturn(Optional.of(metric));
        ResponseEntity<?> response = metricService.getMetric(metric.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetMetricWhenIdIsPresentAndRepositoryIsEmpty() {
        metricRepository.deleteAll();

        List<Metric> metrics = metricRepository.findAll();
        assertTrue(metrics.isEmpty());

        ResponseEntity<?> response = metricService.getMetric(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateMetricWhenSystemAndNameAreBlank() {
        MetricDTO metricDTO = new MetricDTO();
        metricDTO.setSystem("");
        metricDTO.setName("");

        Boolean result = metricService.createMetric(metricDTO);
        assertFalse(result);
    }

    @Test
    public void testCreateMetricWhenSystemAndNameArePresent() {
        MetricDTO metricDTO = new MetricDTO();
        metricDTO.setSystem("testSystem");
        metricDTO.setName("testName");

        Boolean result = metricService.createMetric(metricDTO);
        assertTrue(result);
    }

    @Test
    public void testCreateMetricWhenSystemAndNameAndValueArePresent() {
        MetricDTO metricDTO = new MetricDTO();
        metricDTO.setSystem("testSystem");
        metricDTO.setName("testName");
        metricDTO.setValue(5);

        Boolean result = metricService.createMetric(metricDTO);
        assertTrue(result);
    }

    @Test
    public void testUpdateMetricWhenSystemAndNameAreBlank() {
        MetricDTO metricDTO = new MetricDTO();
        metricDTO.setSystem("testSystem");
        metricDTO.setName("testName");

        Metric metric = new Metric();
        BeanUtils.copyProperties(metricDTO, metric);
        metric.setId(1);

        when(metricRepository.findById(1)).thenReturn(Optional.of(metric));

        MetricDTO metricDTO2 = new MetricDTO();
        metricDTO2.setSystem("");
        metricDTO2.setName("");
        ResponseEntity<?> response = metricService.updateMetric(1, metricDTO2);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testUpdateMetricWhenSystemAndNameArePresent() {
        MetricDTO metricDTO = new MetricDTO();
        metricDTO.setSystem("testSystem");
        metricDTO.setName("testName");

        Metric metric = new Metric();
        BeanUtils.copyProperties(metricDTO, metric);
        metric.setId(1);
        metric.setValue(1);

        when(metricRepository.findById(1)).thenReturn(Optional.of(metric));

        MetricDTO metricDTO2 = new MetricDTO();
        metricDTO2.setSystem("testSystem");
        metricDTO2.setName("testSystem");
        ResponseEntity<?> response = metricService.updateMetric(1, metricDTO2);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateMetricWhenSystemAndNameArePresentButCannotFindId() {
        MetricDTO metricDTO = new MetricDTO();
        metricDTO.setSystem("testSystem");
        metricDTO.setName("testName");

        Metric metric = new Metric();
        BeanUtils.copyProperties(metricDTO, metric);
        metric.setId(2);
        metric.setValue(1);

        when(metricRepository.findById(1)).thenReturn(Optional.empty());

        MetricDTO metricDTO2 = new MetricDTO();
        metricDTO2.setSystem("testSystem");
        metricDTO2.setName("testSystem");
        ResponseEntity<?> response = metricService.updateMetric(1, metricDTO2);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetMetricSummaryWhenSystemIsPresent() {
        ResponseEntity<?> responseEntity = metricService.getMetricSummary("testSystem", "testName", 0, 0);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testGetMetricSummaryWhenSystemIsEmpty() {
        ResponseEntity<?> responseEntity = metricService.getMetricSummary("", "testName", 0, 0);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}