package com.philips.project.gateway.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.philips.project.gateway.bean.Report;
import com.philips.project.gateway.bean.ReportList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.philips.project.gateway.bean.User;
import com.philips.project.gateway.repositories.UserRepository;
import com.philips.project.gateway.service.CustomUserDetailsService;

//@RestController
@Controller
@RequestMapping("/")
public class APIGateway {
    private static String updatedb_URL = "http://localhost:8082/person/updateResult";
    private static String report_URL = "http://localhost:8081/report/";

    @Autowired
    private RestTemplate client;


    @GetMapping("/{date}")
    public String getReport(String date)
    {
        System.out.println(date);
        System.out.println(report_URL+date);
        ResponseEntity<String> result =  this.client.getForEntity(report_URL+"/report/"+date, String.class);
        System.out.println(result);
        return result.toString();
    }

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CustomUserDetailsService service;

    @RequestMapping(value = "/")
    public String viewHomePage() {
        return "index";
    }

    @RequestMapping(value = "/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());

        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(User user) {
        boolean userExist =service.checkIfExist( user.getEmail() );

        if(userExist == true) {
            return "signup_form";
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepo.save(user);

        return "register_success";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> listUsers = userRepo.findAll();
        model.addAttribute("listUsers", listUsers);

        System.out.println(listUsers);
        return "users";
    }

    @GetMapping("/reports")
    public String listReports(Model model) {
        ResponseEntity<Object[]> respones = this.client.getForEntity(report_URL, Object[].class);
        Object[] listReports = respones.getBody();
        model.addAttribute("listReports", listReports);
        return "report";
    }
}
