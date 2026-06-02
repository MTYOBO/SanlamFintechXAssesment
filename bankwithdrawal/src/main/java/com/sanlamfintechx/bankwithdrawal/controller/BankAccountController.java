package com.sanlamfintechx.bankwithdrawal.controller;

import java.math.BigDecimal;

import com.sanlamfintechx.bankwithdrawal.service.BankAccountWithdrawService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank")
public class BankAccountController {
    private final BankAccountWithdrawService bankAccountService;

    public BankAccountController(BankAccountWithdrawService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam("accountId") Long accountId, @RequestParam("amount") BigDecimal amount) {
        return bankAccountService.withdraw(accountId, amount);
    }
}
