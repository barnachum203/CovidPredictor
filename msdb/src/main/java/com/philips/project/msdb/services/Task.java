package com.philips.project.msdb.services;

import com.philips.project.msdb.beans.Person;
import com.philips.project.msdb.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class Task implements CommandLineRunner {

    @Autowired
    PersonRepository personRepository;
    @Autowired
    PersonService personService;
    @Autowired
    private RestTemplate client;
    private static String report_URL = "http://localhost:8081/report/";

    public Task() {

    }

    //Init DB with data from 15 past days
    @Override
    public void run(String... args) throws Exception {

        LocalDate date = LocalDate.now();

        for (int i = 0; i <= 14; i++) {
            List<Person> existing = (this.personRepository.findByResultDate(date.toString()));
            List<Person> personListAPI = new ArrayList<>();
            if (existing.size() == 0) {
                System.out.println("FETCH DATA FOR DATE: " + date);
                personListAPI = this.personService.fetchAPIData(date.toString());
                this.sendDailyParams(personListAPI, date.toString());
            } else {
                System.out.println("Date already exist: " + date);
            }
            date = date.minusDays(1L);
        }
        System.out.println("Done!");
    }

    private void sendDailyParams( List<Person> personList, String date){

        int positives = 0;
        int numberOfPCRs = personList.size();

        // Count the number of positive PCRs
        for (Person p : personList) {
            if (p.isBool_of_corona()) {
                positives++;
            }
        }

        String url = report_URL+"daily/"+date+"/"+positives+"/"+numberOfPCRs;

        try {
        this.client.postForEntity(url, HttpMethod.POST,String.class);
        }catch (Exception e){
            System.out.println("Error posting to analytics" + e);
        }

    }
}
