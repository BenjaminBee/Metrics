package com.metrics.Metric.Services.Account;

import com.metrics.Metric.DTOs.AccountDTO;
import com.metrics.Metric.Models.Account.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AccountService extends UserDetailsService {
    Boolean register(AccountDTO accountDTO) throws Exception;
    ResponseEntity<?> login(AccountDTO accountDTO) throws Exception;
    Account loadUserByUsername(String cdsid) throws UsernameNotFoundException;
}