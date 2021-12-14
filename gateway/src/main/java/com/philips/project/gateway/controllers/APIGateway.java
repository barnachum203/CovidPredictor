package com.philips.project.gateway.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.json.simple.JSONObject;
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
    private static String report_URL = "http://localhost:8081/report/";


    @Autowired
    private RestTemplate client;


 /*   @GetMapping("/{date}")
    public String getReport(String date)
    {
        System.out.println(date);
        System.out.println(report_URL+date);
        ResponseEntity<String> result =  this.client.getForEntity(report_URL+"/report/"+date, String.class);
        System.out.println(result);
        return result.toString();
    }*/

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CustomUserDetailsService service;

    @RequestMapping(value = "/")
    public String viewHomePage() {
        return "temp";
    }
    @RequestMapping(value = "/index")
    public String showMainPage() {
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
        ResponseEntity<JSONObject[]> respones = this.client.getForEntity(report_URL, JSONObject[].class);
        List<String> date = new ArrayList<String>();
        List<String> accu = new ArrayList<String>();
//        List<String> area = new ArrayList<String>();

        for(JSONObject obj : respones.getBody()) {
            date.add(obj.get("date").toString());
            accu.add(obj.get("accumPositives").toString());

        }
        System.out.println(respones.toString());
        JSONObject[] listReports = respones.getBody();
        model.addAttribute("listReports", listReports);
        model.addAttribute("date", date);
        model.addAttribute("accu", accu);

        return "report";
    }
    
    @GetMapping("/area_reports")
    public String specificDateAreaeport(Model model) {
    	 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
         LocalDate today = LocalDate.parse(LocalDate.now().toString(),formatter);
         
        ResponseEntity<JSONObject> respone = this.client.getForEntity(report_URL+"report/"+today, JSONObject.class);
        String date = new String();
        JSONObject obj = respone.getBody();
       
        model.addAttribute("south", obj.get("southCount").toString());
        model.addAttribute("north",obj.get("northCount").toString());
        model.addAttribute("center", obj.get("centerCount").toString());   
        return "area_report";
    }
    
    
}
