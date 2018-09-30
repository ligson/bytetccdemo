package com.bytesvc.provider.controller;

import com.bytesvc.provider.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * ByteTCC倾向于认为: 使用SpringCloud时, 直接对外提供服务的Controller应该明确规划好它是普通服务还是TCC服务.<br />
 * 因此, 0.4.x版本强制对外提供TCC服务的Controller必须加@Compensable注解(若没有实质业务, 也可以不必指定confirmableKey和cancellableKey).<br />
 * 若不加@Compensable注解, 则ByteTCC将其当成普通服务对待, 不接收Consumer端传播的事务上下文. 若它后续调用TCC服务, 则将开启新的TCC全局事务.
 */
@RestController
public class AccountController {
    @Autowired
    private IAccountService accountService;


    @RequestMapping("/")
    public String index() {
        return "ok";
    }


    @ResponseBody
    @RequestMapping(value = "/increase", method = RequestMethod.POST)
    public void increaseAmount(@RequestParam("acctId") String acctId, @RequestParam("amount") double amount) {
        accountService.increaseAmount(acctId, amount);
    }

    @ResponseBody
    @RequestMapping(value = "/decrease", method = RequestMethod.POST)
    public void decreaseAmount(@RequestParam("acctId") String acctId, @RequestParam("amount") double amount) {
        accountService.decreaseAmount(acctId, amount);
    }

}
