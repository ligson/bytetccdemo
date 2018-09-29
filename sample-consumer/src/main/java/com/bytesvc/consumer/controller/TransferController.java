package com.bytesvc.consumer.controller;

import org.bytesoft.compensable.Compensable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.bytesvc.consumer.feign.IAccountService;

import com.bytesvc.consumer.dao.TransferDao;
import com.bytesvc.consumer.service.ITransferService;

@Compensable(interfaceClass = ITransferService.class, confirmableKey = "transferServiceConfirm", cancellableKey = "transferServiceCancel")
@RestController
public class TransferController implements ITransferService {
    private static Logger logger = LoggerFactory.getLogger(TransferController.class);
    @Autowired
    private TransferDao transferDao;

    @Autowired
    private IAccountService acctService;

    @ResponseBody
    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    @Transactional
    public void transfer(@RequestParam String sourceAcctId, @RequestParam String targetAcctId, @RequestParam double amount) {
        logger.debug("账号：{}向账号:{}转账:{}", sourceAcctId, targetAcctId, amount);
        this.acctService.decreaseAmount(sourceAcctId, amount);
        this.increaseAmount(targetAcctId, amount);
    }

    private void increaseAmount(String acctId, double amount) {
        int value = this.transferDao.increaseAmount(acctId, amount);
        if (value != 1) {
            throw new IllegalStateException("ERROR!");
        }
        System.out.printf("exec increase: acct= %s, amount= %7.2f%n", acctId, amount);
    }

    /**
     * 本方法用于演示： TransferController中可以存在@Compensable.interfaceClass指定接口中未定义的方法. <br />
     * 需要注意的是, 未在interfaceClass接口中定义的方法, 不属于可补偿型业务操作, 不走TCC全局事务. <br />
     */
    @ResponseBody
    @RequestMapping(value = "/getAmount", method = RequestMethod.POST)
    public Double getAmount(@RequestParam String targetAcctId) {
        return this.transferDao.getAcctAmount(targetAcctId);
    }

}
