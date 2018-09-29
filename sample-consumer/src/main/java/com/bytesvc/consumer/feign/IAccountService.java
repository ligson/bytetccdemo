package com.bytesvc.consumer.feign;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class IAccountService {

    public void proxy(String url, Map<String, String> params) {
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost("http://localhost:8081" + url);
            List<BasicNameValuePair> pairs = new ArrayList<>();
            params.forEach((k, v) -> pairs.add(new BasicNameValuePair(k, v)));
            httpPost.setEntity(new UrlEncodedFormEntity(pairs));
            CloseableHttpResponse response = client.execute(httpPost);
            assert response.getStatusLine().getStatusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public void increaseAmount(@RequestParam("acctId") String accountId, @RequestParam("amount") double amount) {
        Map<String, String> params = new HashMap<>();
        params.put("accId", accountId);
        params.put("amount", amount + "");
        proxy("/increase", params);
    }

    public void decreaseAmount(@RequestParam("acctId") String accountId, @RequestParam("amount") double amount) {
        Map<String, String> params = new HashMap<>();
        params.put("accId", accountId);
        params.put("amount", amount + "");
        proxy("/decrease", params);
    }

}
