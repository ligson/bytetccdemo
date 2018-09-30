package com.bytesvc.consumer.service.impl;

import com.bytesvc.consumer.feign.IAccountService;
import org.mengyun.tcctransaction.api.Compensable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytesvc.consumer.dao.TransferDao;
import com.bytesvc.consumer.service.ITransferService;

@Service
public class TransferServiceImpl implements ITransferService {
    private static Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);
    @Autowired
    private TransferDao transferDao;

    @Autowired
    private IAccountService acctService;

    private void increaseAmount(String acctId, double amount) {
        int value = this.transferDao.increaseAmount(acctId, amount);
        if (value != 1) {
            throw new IllegalStateException("ERROR!");
        }
        System.out.printf("exec increase: acct= %s, amount= %7.2f%n", acctId, amount);
    }

    @Transactional
    @Compensable(confirmMethod = "confirmTransfer", cancelMethod = "cancelTransfer")
    public void transfer(String sourceAcctId, String targetAcctId, double amount) {
        logger.debug("transfer try 操作.........");
        int value = this.transferDao.confirmIncrease(targetAcctId, amount);
        if (value != 1) {
            throw new IllegalStateException("ERROR!");
        }
        System.out.printf("done increase: acct= %s, amount= %7.2f%n", targetAcctId, amount);
        logger.debug("账号：{}向账号:{}转账:{}", sourceAcctId, targetAcctId, amount);
        this.acctService.decreaseAmount(sourceAcctId, amount);
        this.increaseAmount(targetAcctId, amount);
    }

    @Override
    @Transactional
    public void confirmTransfer(String sourceAcctId, String targetAcctId, double amount) {
        logger.debug("transfer confirm 操作.........");
        int value = this.transferDao.confirmIncrease(targetAcctId, amount);
        if (value != 1) {
            throw new IllegalStateException("ERROR!");
        }
        System.out.printf("done increase: acct= %s, amount= %7.2f%n", targetAcctId, amount);
    }

    @Override
    @Transactional
    public void cancelTransfer(String sourceAcctId, String targetAcctId, double amount) {
        logger.debug("transfer cancel 操作.........");
        int value = this.transferDao.cancelIncrease(targetAcctId, amount);
        if (value != 1) {
            throw new IllegalStateException("ERROR!");
        }
        System.out.printf("exec decrease: acct= %s, amount= %7.2f%n", targetAcctId, amount);
    }

}
