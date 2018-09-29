package com.bytesvc.consumer.service.impl;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytesvc.consumer.dao.TransferDao;
import com.bytesvc.consumer.service.ITransferService;

@Service("transferServiceCancel")
@Lazy(value = false)
public class TransferServiceCancel implements ITransferService, InitializingBean {

    @Autowired
    private TransferDao transferDao;

    @Transactional
    public void transfer(String sourceAcctId, String targetAcctId, double amount) {
        int value = this.transferDao.cancelIncrease(targetAcctId, amount);
        if (value != 1) {
            throw new IllegalStateException("ERROR!");
        }
        System.out.printf("exec decrease: acct= %s, amount= %7.2f%n", targetAcctId, amount);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("consumer正常启动");
    }
}
