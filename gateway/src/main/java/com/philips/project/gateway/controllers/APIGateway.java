package com.philips.project.gateway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("project")
public class APIGateway {
    private static String updatedb_URL = "http://localhost:8082/person/updateResult";
    private static String report_URL = "http://localhost:8081/report/";

    @Autowired
    private RestTemplate client;

    @GetMapping("/{date}")
    public String getReport(@PathVariable String date)
    {
        System.out.println(date);
        ResponseEntity<String> result =  this.client.getForEntity(report_URL+date, String.class);
        return result.getBody();
    }

}
