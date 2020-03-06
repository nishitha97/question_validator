package com.example.demo.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Arrays;
/*
 * PEARSON PROPRIETARY AND CONFIDENTIAL INFORMATION SUBJECT TO NDA
 * Copyright © 2017 Pearson Education, Inc.
 * All Rights Reserved.
 * <p>
 * NOTICE:  All information contained herein is, and remains
 * the property of Pearson Education, Inc.  The intellectual and technical concepts contained
 * herein are proprietary to Pearson Education, Inc. and may be covered by U.S. and Foreign Patents,
 * patent applications, and are protected by trade secret or copyright law.
 * Dissemination of this information, reproduction of this material, and copying or distribution of this software
 * is strictly forbidden unless prior written permission is obtained
 * from Pearson Education, Inc.
 */

/**
 * Common Rest Client for inter-service communication
 */
@Component
public class RestClient {

    @Autowired
    RestTemplate restTemplate;

    /**
     * @param authToken
     * @return
     */
    public HttpHeaders getRequestHeader(String authToken) {

        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add(Constants.Headers.X_AUTHORIZATION, authToken);
        //headers.set(Constants.Headers.X_TENANT_ID,"gw");
        //TODO when implementing the correlation id for the group service this need to be integrate with actual header value
       //headers.add(Constants.Headers.CORRELATIONID, "12345");

        return headers;
    }

    /**
     *
     * @param username
     * @param password
     * @return
     */
    public HttpHeaders getRequestHeader(String username, String password) {
        return new HttpHeaders() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
            }
        };
    }

    /**
     * Generic method for sending POST, PUT and DELETE http requests
     *
     * @param url        service url
     * @param token      auth token
     * @param entity     request payload
     * @param httpMethod GET, POST, PUT or DELETE
     * @param classObj   Response Type <T>
     * @param <T>        response
     * @return
     *///thisone
    public <T> ResponseEntity<T> restExchange(String url, String token, Object entity, HttpMethod httpMethod, Class<T> classObj) {
        return restTemplate.exchange(url, httpMethod, new HttpEntity<>(entity, this.getRequestHeader(token)), classObj);
    }

    /**
     * Generic method for sending POST, PUT and DELETE http requests
     *
     * @param url        service url
     * @param username      username
     * @param password password
     * //@param entity     request payload
     * @param httpMethod GET, POST, PUT or DELETE
     * @param classObj   Response Type <T>
     * @param <T>        response
     * @return
     */
    public <T> ResponseEntity<T> restExchange(String url, String username,String password, String xml, HttpMethod httpMethod, Class<T> classObj) {
        return restTemplate.exchange(url, httpMethod, new HttpEntity<>(xml, this.getRequestHeader(username,password)), classObj);
    }

    /**
     * Generic method for sending POST, PUT and DELETE http requests for parameterized type references
     *
     * @param url        service url
     * @param token      auth token
     * @param entity     request payload
     * @param httpMethod GET, POST, PUT or DELETE
     * @param parameterizedTypeReference Response Type <T>
     * @param <T>        response
     * @return
     */
    public <T> ResponseEntity<T> restExchange(String url, String token, Object entity, HttpMethod httpMethod, ParameterizedTypeReference<T> parameterizedTypeReference) {
        return restTemplate.exchange(url, httpMethod, new HttpEntity<>(entity, this.getRequestHeader(token)), parameterizedTypeReference);
    }


    /**
     * can be used to pass the path variables as parameters like something/{someId}
     *
     * @param url          service url
     * @param token        authentication token
     * @param entity       request pay load
     * @param httpMethod   request method
     * @param classObj     return type
     * @param uriVariables path variables as comma separated values
     * @return Response with requested type.
     */
    public <T> ResponseEntity<T> restExchange(String url, String token, Object entity, HttpMethod httpMethod, Class<T> classObj, Object... uriVariables) {
        return restTemplate.exchange(url, httpMethod, new HttpEntity<>(entity, this.getRequestHeader(token)), classObj, uriVariables);
    }
}
