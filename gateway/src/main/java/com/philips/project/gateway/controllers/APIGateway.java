package com.philips.project.gateway.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    private static String report_URL = "http://localhost:8081/report/predict/";

    @Autowired
    private RestTemplate client;

    @GetMapping("/{date}")
    public String getReport(@PathVariable String date)
    {
    	Thread thread = new Thread();
    	thread.run();
        System.out.println(date);
        System.out.println(report_URL+date);
        ResponseEntity<String> result =  this.client.getForEntity(report_URL+date, String.class);
        System.out.println(result);
        return "index";
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
         
        return "users";
    }
}
