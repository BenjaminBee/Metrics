package com.metrics.Metric.Config;

import com.metrics.Metric.Services.Account.AccountService;
import com.metrics.Metric.Services.Account.AccountServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public AccountService accountService() {
        return new AccountServiceImpl();
    }
}
