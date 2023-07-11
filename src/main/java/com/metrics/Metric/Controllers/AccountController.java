package com.metrics.Metric.Controllers;

import com.metrics.Metric.Config.AppConfig;
import com.metrics.Metric.DTOs.AccountDTO;
import com.metrics.Metric.Services.Account.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/account")
public class AccountController {
    @Autowired
    private AppConfig appConfig;

    Logger logger = LoggerFactory.getLogger(AccountController.class);

    @PostMapping(path="/register")
    public ResponseEntity<?> register(@RequestBody AccountDTO accountDTO) throws Exception {
        if(appConfig.accountService().register(accountDTO)) {
            logger.info("Account registered successfully with: " + accountDTO.getUsername() + " /" + accountDTO.getEmail() + " /" + accountDTO.getPassword() + " /" + accountDTO.getConfirmPassword());
            return ResponseEntity.ok("Registration Successful");
        }
        logger.info("Account registered unsuccessfully with " + accountDTO.getUsername() + " /" + accountDTO.getEmail() + " /" + accountDTO.getPassword() + " /" + accountDTO.getConfirmPassword());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Data formatted incorrectly or account already exists");
    }

    @PostMapping(path="/login")
    public ResponseEntity<?> login(@RequestBody AccountDTO accountDTO) throws Exception {
        logger.info("Account log in attempt with: " + accountDTO.getUsername() + " /" + accountDTO.getPassword() + " /");
        return appConfig.accountService().login(accountDTO);
    }
}
