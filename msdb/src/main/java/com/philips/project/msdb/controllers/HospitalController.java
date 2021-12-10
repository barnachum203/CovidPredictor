package com.philips.project.msdb.controllers;

import com.philips.project.msdb.beans.AreaEnum;
import com.philips.project.msdb.services.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hospital")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @GetMapping("/randomize")
    public void randomizeHospitals(){
        hospitalService.setRandomData();
    }


    //option = north / south / central / all
    @GetMapping("/beds/{option}")
    public ResponseEntity<Integer> sendNumOfBeds(@PathVariable String option){
        int result = hospitalService.calcNumOfBeds(option);
        return new ResponseEntity<Integer>(result,HttpStatus.OK);
    }
    
    @GetMapping("send")
    public ResponseEntity<Integer> sendWarningReports(){
    	int result = hospitalService.sendWarningReports();
        return new ResponseEntity<Integer>(result,HttpStatus.OK);
    }
    
}
