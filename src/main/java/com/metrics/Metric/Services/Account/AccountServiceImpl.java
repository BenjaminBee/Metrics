package com.metrics.Metric.Services.Account;

import com.metrics.Metric.DTOs.AccountDTO;
import com.metrics.Metric.Models.Account.Account;
import com.metrics.Metric.Models.Account.AccountRepository;
import com.metrics.Metric.Security.AuthenticationResponse;
import com.metrics.Metric.Security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);


    /**
     * @Functionality This method performs validation checks for whether the user exists already and then creates a new one if not
     * @param accountDTO
     * @return true or false depending on whether the account was registered successfully or not
     * @throws Exception
     */
    @Override
    public Boolean register(AccountDTO accountDTO) throws Exception {
        Optional<Account> testUser = accountRepository.findByUsername(accountDTO.getUsername());
        if (testUser.isEmpty()) {
            if(accountDTO.getPassword().equals(accountDTO.getConfirmPassword())) {
                String encodedPassword = bCryptPasswordEncoder.encode(accountDTO.getPassword());

                Account account = new Account(
                        accountDTO.getUsername(), accountDTO.getEmail(),
                        encodedPassword
                );

                accountRepository.save(account);
                return true;
            }
            throw new Exception("Your password and confirmation password must be the same");
        }
        return false;
    }

    /**
     * @Functionality This method performs validation for inputted user details and then generates a jwt token to be used to validate further api requests
     * @param accountDTO
     * @return status code and a jwt token
     * @throws Exception
     */
    public ResponseEntity<?> login(AccountDTO accountDTO) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(accountDTO.getUsername(), accountDTO.getPassword())
            );
            Account account = accountRepository.findByUsername(accountDTO.getUsername()).get();
            final String jwt = jwtUtil.generateToken(account);
            System.out.println(jwt + " is the jwt generated in login() function");
            logger.info("Successful Login");
            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        }
        catch(BadCredentialsException ex) {
            logger.error("Invalid username or password: " + ex);
            throw new Exception("Invalid user");
        }
    }

    /**
     * @Functionality This method performs validation and returns an account within the database to be used within security modules for further validation
     * @param username
     * @return account on the inputted username
     * @throws UsernameNotFoundException
     */
    @Override
    public Account loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository.findByUsername(username);
        if(account.isEmpty()) {
            throw new UsernameNotFoundException("Account could not be found");
        }
        return account.get();
    }
}
