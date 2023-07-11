package com.metrics.Metric.Models.Metric;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "METRICS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Metric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "`SYSTEM`", length = 125)
    private String system;

    @Column(name = "`NAME`", length = 125)
    private String name;

    @Column(name = "`DATE`")
    private Integer date;

    @Column(name = "`VALUE`")
    private Integer value;

    public Metric(String system, String name, Integer date, Integer value) {
        this.system = system;
        this.name = name;
        this.date = date;
        this.value = value;
    }
}