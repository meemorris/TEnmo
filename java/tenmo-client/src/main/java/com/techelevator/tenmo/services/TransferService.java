package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.TransferDetail;
import com.techelevator.tenmo.models.TransferRequest;
import com.techelevator.tenmo.models.TransferStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class TransferService {

    public static String AUTH_TOKEN = "";
    private final String API_BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();

    public TransferService(String url) {
        API_BASE_URL = url;
    }


    public TransferStatus sendBucks(String token, Long toUserID, BigDecimal transferAmount) {
        Transfer newTransfer = createNewTransfer(toUserID, transferAmount);
        TransferStatus transferStatus = new TransferStatus();
        try {
            transferStatus = restTemplate.exchange(API_BASE_URL + "account/transfer", HttpMethod.POST,
                    makeTransferEntity(newTransfer, token), TransferStatus.class).getBody();
        } catch (RestClientResponseException ex) {
            System.out.println((ex.getRawStatusCode() + " : " + ex.getStatusText()));
        } catch (ResourceAccessException ex) {
            System.out.println(ex.getMessage());
        }
        return transferStatus;
    }

    public TransferDetail[] viewTransfers(String token) {
        return restTemplate.exchange(API_BASE_URL + "account/transfer/history", HttpMethod.GET,
                makeAuthEntity(token), TransferDetail[].class).getBody();
    }

    public TransferDetail getTransferDetails(String token, long transferID) {
        return restTemplate.exchange((API_BASE_URL + "account/transfer/history/" + transferID), HttpMethod.GET,
                makeAuthEntity(token), TransferDetail.class).getBody();
    }

    public TransferStatus requestTransfer(String token, Long fromUserID, BigDecimal transferAmount) {
        TransferRequest request = createNewRequest(fromUserID, transferAmount);
        TransferStatus status = null;
        try {
            status = restTemplate.exchange(API_BASE_URL + "account/transfer/request/" + fromUserID, HttpMethod.POST,
                    makeRequestEntity(request, token), TransferStatus.class).getBody();
        } catch (RestClientResponseException ex) {
            System.out.println((ex.getRawStatusCode() + " : " + ex.getStatusText()));
        } catch (ResourceAccessException ex) {
            System.out.println(ex.getMessage());
        }
        return status;
    }

    public TransferRequest[] viewPendingRequests(String token) {
        TransferRequest[] pendingRequests = null;
        try {
            pendingRequests = restTemplate.exchange(API_BASE_URL + "account/transfer/requests", HttpMethod.GET,
                    makeAuthEntity(token), TransferRequest[].class).getBody();
        } catch (RestClientResponseException ex) {
            System.out.println((ex.getRawStatusCode() + " : " + ex.getStatusText()));
        } catch (ResourceAccessException ex) {
            System.out.println(ex.getMessage());
        }
        return pendingRequests;
    }

    public TransferStatus executeTransfer(String token, long option, TransferRequest request) {
        TransferStatus status = null;
        try {
            status = restTemplate.exchange(API_BASE_URL + "account/transfer/requests/" + option, HttpMethod.POST,
                    makeRequestEntity(request, token), TransferStatus.class).getBody();
        } catch (RestClientResponseException ex) {
            System.out.println((ex.getRawStatusCode() + " : " + ex.getStatusText()));
        } catch (ResourceAccessException ex) {
            System.out.println(ex.getMessage());
        }
        return status;
    }

    private TransferRequest createNewRequest(Long fromUserID, BigDecimal transferAmount) {
        TransferRequest request = new TransferRequest();
        request.setFromUserId(fromUserID);
        request.setTransferAmount(transferAmount);
        return request;
    }

    private Transfer createNewTransfer(Long toUserId, BigDecimal transferAmount) {
        Transfer newTransfer = new Transfer();
        newTransfer.setToUserId(toUserId);
        newTransfer.setTransferAmount(transferAmount);
        return newTransfer;
    }

    private HttpEntity makeAuthEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }

    private HttpEntity<TransferRequest> makeRequestEntity(TransferRequest request, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<TransferRequest> entity = new HttpEntity<>(request, headers);
        return entity;
    }



}
