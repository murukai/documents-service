package com.afrikatek.documentsservice.cucumber;

import com.afrikatek.documentsservice.DocumentsserviceApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = DocumentsserviceApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
