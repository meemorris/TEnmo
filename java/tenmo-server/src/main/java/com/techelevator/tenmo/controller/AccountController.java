package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.UserDAO;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;

//starting path: http://localhost:8080
@PreAuthorize("isAuthenticated()")
@RequestMapping("/account")
@RestController
public class AccountController {

    private AccountDAO accountDAO;
    private UserDAO userDAO;

    public AccountController(AccountDAO accountDAO, UserDAO userDAO) {
        this.accountDAO = accountDAO;
        this.userDAO = userDAO;
    }

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal returnBalance(Principal principal) throws UsernameNotFoundException {
        long id = userDAO.findIdByUsername(principal.getName());
        return accountDAO.getBalance(id);
    }

}
