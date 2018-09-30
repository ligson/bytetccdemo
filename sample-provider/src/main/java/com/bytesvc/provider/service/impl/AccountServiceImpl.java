package com.bytesvc.provider.service.impl;

import com.bytesvc.provider.service.IAccountService;
import org.mengyun.tcctransaction.api.Compensable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountServiceImpl implements IAccountService {
    private static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    @Compensable(confirmMethod = "confirmIncreaseAmount", cancelMethod = "cancelIncreaseAmount")
    public void increaseAmount(String acctId, double amount) {
        logger.debug("increaseAmount try...........");
        int value = this.jdbcTemplate.update("update tb_account_one set frozen = frozen + ? where acct_id = ?", amount, acctId);
        if (value != 1) {
            throw new IllegalStateException("ERROR!");
        }
        System.out.printf("exec increase: acct= %s, amount= %7.2f%n", acctId, amount);
    }

    @Override
    @Transactional
    public void confirmIncreaseAmount(String accountId, double amount) {
        logger.debug("increaseAmount confirm...........");
        int value = this.jdbcTemplate.update(
                "update tb_account_one set amount = amount + ?, frozen = frozen - ? where acct_id = ?", amount, amount, accountId);
        if (value != 1) {
            throw new IllegalStateException("ERROR!");
        }
        System.out.printf("done increase: acct= %s, amount= %7.2f%n", accountId, amount);
    }

    @Override
    @Transactional
    public void cancelIncreaseAmount(String accountId, double amount) {
        logger.debug("increaseAmount cancel...........");
        int value = this.jdbcTemplate.update("update tb_account_one set frozen = frozen - ? where acct_id = ?", amount, accountId);
        if (value != 1) {
            throw new IllegalStateException("ERROR!");
        }
        System.out.printf("undo increase: acct= %s, amount= %7.2f%n", accountId, amount);
    }

    @Transactional
    @Compensable(confirmMethod = "confirmDecreaseAmount", cancelMethod = "cancelDecreaseAmount")
    public void decreaseAmount(String acctId, double amount) {
        logger.debug("decreaseAmount try...........");
        int value = this.jdbcTemplate.update(
                "update tb_account_one set amount = amount - ?, frozen = frozen + ? where acct_id = ?", amount, amount, acctId);
        if (value != 1) {
            throw new IllegalStateException("ERROR!");
        }
        System.out.printf("exec decrease: acct= %s, amount= %7.2f%n", acctId, amount);
    }

    @Override
    @Transactional
    public void confirmDecreaseAmount(String accountId, double amount) {
        logger.debug("decreaseAmount confirm...........");
        int value = this.jdbcTemplate.update("update tb_account_one set frozen = frozen - ? where acct_id = ?", amount, accountId);
        if (value != 1) {
            throw new IllegalStateException("ERROR!");
        }
        System.out.printf("done decrease: acct= %s, amount= %7.2f%n", accountId, amount);
    }

    @Override
    @Transactional
    public void cancelDecreaseAmount(String accountId, double amount) {
        logger.debug("decreaseAmount cancel...........");
        int value = this.jdbcTemplate.update(
                "update tb_account_one set amount = amount + ?, frozen = frozen - ? where acct_id = ?", amount, amount, accountId);
        if (value != 1) {
            throw new IllegalStateException("ERROR!");
        }
        System.out.printf("undo decrease: acct= %s, amount= %7.2f%n", accountId, amount);
    }

}
