package com.example.demo.controller;

import com.example.demo.service.DataService;
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
@RequestMapping("/api/files")
public class DataServiceController {


    @Autowired
    DataService dataService;

    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping
    public void addFile() throws IOException {
        dataService.addFile();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping("/tests")
    public void addFileToCloud() throws IOException {

        dataService.runTest();

    }
}
