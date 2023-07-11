package com.metrics.Metric.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Functionality The DTO for Metric information as an abstraction from the database, contains data representing the MetricSummary data for the from / to parameters
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetricDTO {
    private String system;

    private String name;

    private Integer date;

    private int from;

    private int to;

    private Integer value;
}