package com.example.demo.service;

import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface CloudService {

    void uploadFile() throws IOException;

    void runTest() throws IOException;
}
