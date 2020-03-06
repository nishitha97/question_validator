package com.example.demo.controller;

import com.example.demo.service.CloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@Validated
@RequestMapping("/api/cloud")
public class CloudServiceController {


    @Autowired
    CloudService cloudService;

    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping
    public void addFileToCloud() throws IOException {

        cloudService.uploadFile();

    }

    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping("/tests")
    public void testAddFilesToCloud() throws IOException {

        cloudService.runTest();

    }


}
