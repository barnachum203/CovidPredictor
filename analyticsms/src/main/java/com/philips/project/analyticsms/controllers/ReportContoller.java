package com.philips.project.analyticsms.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.philips.project.analyticsms.beans.Report;
import com.philips.project.analyticsms.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("report")
public class ReportContoller {

    @Autowired
    private ReportService reportService;

    public void positiveRatioByDates(Date from, Date to){
        //Check if the date exist in the DB,
        // if not - go to DBMS and bring the data to make the calculation
        //return avarage between dates
    }

    @GetMapping("")
    public List<Report> getReports(){
        return reportService.getReports();
    }

    @GetMapping("date")               // returns how many positive in specific date
    public Report getReportByDate(String date){
        return reportService.getReportByDate(date);
    }

    
    @GetMapping("predict/{startDate}/{endDate}")               // predicts how many positive in specific date
    public String getPredictionReportByDate(@PathVariable String startDate ,@PathVariable String endDate) throws JsonProcessingException{
         return reportService.getPredictionBetweenDatesReport(endDate , startDate );
    }
    


    @PostMapping("daily/{date}/{positives}/{numberOfPCRs}")
    public void autoRecieveData(@PathVariable String date,@PathVariable int positives,@PathVariable int numberOfPCRs){

        System.out.println("Date: " + date);
        System.out.println("positives:  " + positives);
        System.out.println("numberOfPCRs: " + numberOfPCRs);
        reportService.autoRecieveData(date, positives, numberOfPCRs);
    }

    @GetMapping("report/{date}")
	public String calculateDailyReport(@PathVariable String date) throws JsonProcessingException {
    	Report report = reportService.calculateDailyReport(date);
        ObjectMapper mapper = new ObjectMapper();
	    String json = mapper.writeValueAsString(report);
    	return json  ;
	}

    
    
    
    
    
 /*   @PostMapping("daily/{date}/{positives}/{numberOfPCRs}")
    public void calculateDailyReport(@PathVariable String date,@PathVariable int positives,@PathVariable int numberOfPCRs){

        System.out.println("Date: " + date);
        System.out.println("positives:  " + positives);
        System.out.println("numberOfPCRs: " + numberOfPCRs);
        reportService.calculateDailyReport(date, positives, numberOfPCRs);
    }*/
}
