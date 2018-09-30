package com.bytesvc.provider.service;

public interface IAccountService {

    public void increaseAmount(String accountId, double amount);

    public void confirmIncreaseAmount(String accountId, double amount);

    public void cancelIncreaseAmount(String accountId, double amount);

    public void decreaseAmount(String accountId, double amount);

    public void confirmDecreaseAmount(String accountId, double amount);

    public void cancelDecreaseAmount(String accountId, double amount);

}
