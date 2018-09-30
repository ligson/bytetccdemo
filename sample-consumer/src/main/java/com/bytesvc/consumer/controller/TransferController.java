package com.bytesvc.consumer.controller;

import com.bytesvc.consumer.service.ITransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransferController {
    private static Logger logger = LoggerFactory.getLogger(TransferController.class);
    @Autowired
    private ITransferService transferService;

    @ResponseBody
    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public void transfer(@RequestParam String sourceAcctId, @RequestParam String targetAcctId, @RequestParam double amount) {
        transferService.transfer(sourceAcctId, targetAcctId, amount);
    }

}
