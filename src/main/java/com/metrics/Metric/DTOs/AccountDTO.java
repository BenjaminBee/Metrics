package com.metrics.Metric.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Functionality The DTO for Account Information as an abstraction from the database
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
}
