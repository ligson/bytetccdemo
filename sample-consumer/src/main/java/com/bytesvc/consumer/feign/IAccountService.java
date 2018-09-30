package com.bytesvc.consumer.feign;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class IAccountService {
    private static Logger logger = LoggerFactory.getLogger(IAccountService.class);

    public void proxy(String url, Map<String, String> params) {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost("http://localhost:8081/provider" + url);
        List<BasicNameValuePair> pairs = new ArrayList<>();
        params.forEach((k, v) -> pairs.add(new BasicNameValuePair(k, v)));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(pairs));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        CloseableHttpResponse response = null;
        try {
            response = client.execute(httpPost);
        } catch (IOException e) {
            throw new IllegalStateException("errror........", e);
        }
        logger.debug("http statue:{}", response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new IllegalStateException("errror........");
        }
    }

    public void increaseAmount(String accountId, double amount) {
        Map<String, String> params = new HashMap<>();
        params.put("acctId", accountId);
        params.put("amount", amount + "");
        proxy("/increase", params);
    }

    public void decreaseAmount(String accountId, double amount) {
        Map<String, String> params = new HashMap<>();
        params.put("acctId", accountId);
        params.put("amount", amount + "");
        proxy("/decrease", params);
    }

}
