package com.philips.project.analyticsms.controllers;

import com.philips.project.analyticsms.beans.Report;
import com.philips.project.analyticsms.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

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

    @GetMapping("date")
    public Report getReportByDate(String date){
        return reportService.getReportByDate(date);
    }

    @PostMapping("/add")
    public void createReport(@RequestBody Report report){
        reportService.addReport(report);
    }

    @PostMapping("daily/{date}/{positives}/{numberOfPCRs}")
    public void calculateDailyReport(@PathVariable String date,@PathVariable int positives,@PathVariable int numberOfPCRs){

        System.out.println("Date: " + date);
        System.out.println("positives:  " + positives);
        System.out.println("numberOfPCRs: " + numberOfPCRs);
        reportService.calculateDailyReport(date, positives, numberOfPCRs);


    }
}
