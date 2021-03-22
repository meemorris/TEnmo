package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    public static String AUTH_TOKEN = "";
    private final String API_BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();

    public AccountService(String url) {
        API_BASE_URL = url;
    }


    public BigDecimal getBalance(String token) throws AccountServiceException {
        ResponseEntity<BigDecimal> response;
        try {
            response = restTemplate.exchange(API_BASE_URL + "account/balance", HttpMethod.GET, makeAuthEntity(token), BigDecimal.class);
        } catch (RestClientResponseException ex) {
            throw new AccountServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
        }
        return response.getBody();
    }



    private HttpEntity makeAuthEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }


}
