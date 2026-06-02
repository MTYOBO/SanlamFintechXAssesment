package com.sanlamfintechx.bankwithdrawal.service;

import java.math.BigDecimal;

import com.sanlamfintechx.bankwithdrawal.event.WithdrawalEvent;
import com.sanlamfintechx.bankwithdrawal.publisher.SnsPublisher;
import com.sanlamfintechx.bankwithdrawal.repository.BankAccountRepository;

import org.springframework.stereotype.Service;

@Service
public class BankAccountWithdrawService {

    public final BankAccountRepository bankAccountRepository;
    public final SnsPublisher snsPublisher;

    public BankAccountWithdrawService(BankAccountRepository bankAccountRepository, SnsPublisher snsPublisher) {
        this.bankAccountRepository = bankAccountRepository;
        this.snsPublisher = snsPublisher;
    }

    public String withdraw(Long accountId, BigDecimal amount) {
        try {
            BigDecimal currentBalance = bankAccountRepository.getCurrentBalance(accountId);
            if (currentBalance != null && currentBalance.compareTo(amount) >= 0) {
                int rowsAffected = bankAccountRepository.updateBalance(amount, accountId);
                if (rowsAffected > 0) {
                    if (snsPublisher.publishMessage(new WithdrawalEvent(accountId, amount))) {
                        return "Withdrawal successful";
                    } else {
                        return "Withdrawal failed";
                    }
                } else {
                    return "Withdrawal failed";
                }
            } else {
                // Insufficient funds
                return "Insufficient funds for withdrawal";
            }
        } catch (Exception e) {
            return "An error occurred, please check input parameters accountId: " + accountId + " amount: " + amount + 
                " against what is available in the database and try again."; 
        }
    }
}
