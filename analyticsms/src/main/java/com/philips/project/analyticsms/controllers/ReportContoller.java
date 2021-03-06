package com.philips.project.analyticsms.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.philips.project.analyticsms.beans.Report;
import com.philips.project.analyticsms.services.ReportService;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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

    @GetMapping("date")               // get date in body, returns Report (not json)
    public Report getReportByDate(String date){
        return reportService.getReportByDate(date);
    }
    
    @GetMapping("report/{date}") //get date in url,return json of report
	public String calculateDailyReport(@PathVariable String date) throws JsonProcessingException {
    	Report report = reportService.calculateDailyReport(date);
        ObjectMapper mapper = new ObjectMapper();
	    String json = mapper.writeValueAsString(report);
    	return json  ;
	}

    
    @GetMapping("predict/{startDate}/{endDate}")               // predicts how many positive in specific date
    public List<Report> getPredictionReportsBetweenDates(@PathVariable String startDate ,@PathVariable String endDate) throws JsonProcessingException{
        return reportService.getPredictionBetweenDatesReport(endDate , startDate );
    }

    /*//calculateDailyReport doing this
    @GetMapping("predict/{specificDate}")               // predicts how many positive in specific date
    public String getPredictionReportsBetweenDates(@PathVariable String date ) throws JsonProcessingException{
         return reportService.getPredictionBetweenDatesReport( date );
    }*/
    



    @PostMapping("daily")
    public void autoRecieveData(@RequestBody String data) throws ParseException{
    	JSONObject j = (JSONObject) JSONValue.parse(data);
        System.out.println("Date: " );
        String date = (String) j.get("date");
        long positives = (long) j.get("positives");
        long south = (long) j.get("south");
        long numberOfPCRs = (long) j.get("numberOfPCRs");
        long north = (long) j.get("north");
        long central = (long) j.get("central");

        System.out.println(numberOfPCRs);
       reportService.autoRecieveData((String)j.get("date"),(int)positives,(int)numberOfPCRs,(int)south,(int)north,(int)central);
    }


    

}
