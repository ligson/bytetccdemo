package com.bytesvc.provider.service.impl;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytesvc.provider.service.IAccountService;

@Service("accountServiceCancel")
@Lazy(value = false)
public class AccountServiceCancel implements IAccountService, InitializingBean {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void increaseAmount(String acctId, double amount) {
        int value = this.jdbcTemplate.update("update tb_account_one set frozen = frozen - ? where acct_id = ?", amount, acctId);
        if (value != 1) {
            throw new IllegalStateException("ERROR!");
        }
        System.out.printf("undo increase: acct= %s, amount= %7.2f%n", acctId, amount);
    }

    @Transactional
    public void decreaseAmount(String acctId, double amount) {
        int value = this.jdbcTemplate.update(
                "update tb_account_one set amount = amount + ?, frozen = frozen - ? where acct_id = ?", amount, amount, acctId);
        if (value != 1) {
            throw new IllegalStateException("ERROR!");
        }
        System.out.printf("undo decrease: acct= %s, amount= %7.2f%n", acctId, amount);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("系统正常启动");
    }
}
