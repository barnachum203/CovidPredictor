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

    
    @GetMapping("predict")               // predicts how many positive in specific date
    public void getPredictionReportByDate(String date){
         reportService.getPredictionReportByDate(date);
    }
    


    @PostMapping("/add")
    public void createReport(@RequestBody Report report){
        reportService.addReport(report);
    }


    @PostMapping("daily/{date}/{positives}/{numberOfPCRs}")
    public void autoRecieveData(@PathVariable String date,@PathVariable int positives,@PathVariable int numberOfPCRs){

        System.out.println("Date: " + date);
        System.out.println("positives:  " + positives);
        System.out.println("numberOfPCRs: " + numberOfPCRs);
        reportService.autoRecieveData(date, positives, numberOfPCRs);
    }

    @PostMapping("report/{date}")
	public ResponseEntity<String> calculateDailyReport(@PathVariable String date) {
    	int result = reportService.calculateDailyReport(date);
    	/*     return json in http body status. */
    	Map<String , Integer> hm = new HashMap<String,Integer>();
    	hm.put(date, result);
    	String json = null;
		try {
			json = new ObjectMapper().writeValueAsString(hm);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	return new ResponseEntity<String>(json, HttpStatus.OK);


	}

 /*   @PostMapping("daily/{date}/{positives}/{numberOfPCRs}")
    public void calculateDailyReport(@PathVariable String date,@PathVariable int positives,@PathVariable int numberOfPCRs){

        System.out.println("Date: " + date);
        System.out.println("positives:  " + positives);
        System.out.println("numberOfPCRs: " + numberOfPCRs);
        reportService.calculateDailyReport(date, positives, numberOfPCRs);
    }*/
}
