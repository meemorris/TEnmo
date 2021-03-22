package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    public static String AUTH_TOKEN = "";
    private final String API_BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();

    public UserService(String url) {
        API_BASE_URL = url;
    }

    public User[] listUsers(String token) {
        return restTemplate.exchange(API_BASE_URL + "users/list", HttpMethod.GET, makeAuthEntity(token), User[].class).getBody();
    }


    private HttpEntity makeAuthEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

}
