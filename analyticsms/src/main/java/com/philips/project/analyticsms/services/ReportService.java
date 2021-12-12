package com.philips.project.analyticsms.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.philips.project.analyticsms.beans.Report;
import com.philips.project.analyticsms.repository.ReportRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;


    //fet all reports
    public List<Report> getReports() {
        return reportRepository.findAll();
    }

    //Get report by date
    public Report getReportByDate(String date) {
        return reportRepository.findByDate(date);
    }

    //Add new report
    public void addReport(Report report) {
        reportRepository.save(report);
    }

    //Update the report params and re-calculate the number of patients
    //
    public void updateTodayReport(Report report) {
        Report reportToUpdate = report;
        reportToUpdate.setPatients(report.getPatients());
        reportRepository.save(reportToUpdate);
    }

//    public int calculateReport(Person[] reports){
//
//    }
    //Get prediction by date
    public  String getPredictionBetweenDatesReport(String endDate ,String startDate) throws JsonProcessingException {   // range of prediction ... need to add another date
    	
    //	String startDate  = LocalDate.parse(endDate).minusDays(14).toString();  // currently checking 15 days
    	List<Report> reports = reportRepository.getReportsBetweenDatesQuery(startDate,endDate);
    	System.out.println("start");
    	for(Report report : reports) {
    		System.out.println(report);
    	}
    	System.out.println("end");
        ObjectMapper mapper = new ObjectMapper();
 	    String json = mapper.writeValueAsString(reports); 
     	return json;
    }
    

    public void autoRecieveData(String date, int positives, int numberOfPCRs,int central, int north,int south ) {  // when db inserted to msdb, this function will be called
        Report newReport =  this.reportRepository.findByDate(date);
        System.out.println("auto called");
    	if (newReport == null) {
        	newReport = new Report();
        }
    	newReport.setPatients(numberOfPCRs);
    	newReport.setDate(date);    			
    	if(numberOfPCRs-positives != 0) {
            newReport.setPositiveRatio((double)positives/(numberOfPCRs-positives));
    	}
    	newReport.setCenterCount( central);
    	newReport.setNorthCount( north);
    	newReport.setSouthCount( south);

        newReport.setPositivePCR(positives);        
        System.out.println(newReport);
        reportRepository.save(newReport);
    
        newReport.setAccumPositives(calculateHelper(date).getAccumPositives());
        System.out.println(newReport.getAccumPositives());
        reportRepository.save(newReport);

    }
    public  Report calculateDailyReport(String date) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate oneDayAgo = LocalDate.parse(date,formatter).minusDays(1);
	    LocalDate twoWeeksAgo = LocalDate.parse(date,formatter).minusDays(15);
	    Report yesterdayDate =  this.reportRepository.findByDate(oneDayAgo.toString());
	   
	    LocalDate runOnDayes =twoWeeksAgo;
	   
	    Report runOnDayesReport = this.reportRepository.findByDate(runOnDayes.toString());
	    
	    while( yesterdayDate   == null && runOnDayesReport != null) {
	    	runOnDayes = runOnDayes.plusDays(1);
		    runOnDayesReport =  this.reportRepository.findByDate(runOnDayes.toString());
	    }
	    
	    while(yesterdayDate   == null ) {
	    	calculateHelper(runOnDayes.toString());
		    yesterdayDate =  this.reportRepository.findByDate(oneDayAgo.toString());
		    runOnDayes = runOnDayes.plusDays(1);
	    }
	    return calculateHelper(date);
    }

    private  Report calculateHelper(String date) {    //return prediction of positive patient
    	
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate oneWeekAgo = LocalDate.parse(date,formatter).minusDays(7);
        LocalDate twoWeeksAgo = LocalDate.parse(date,formatter).minusDays(14);
        LocalDate oneDayAgo = LocalDate.parse(date,formatter).minusDays(1);


        //Test
        System.out.println("yesterday: " + oneWeekAgo);
        System.out.println("twoWeeksAgo: " + twoWeeksAgo);
        
        
        Report currDateReport =  this.reportRepository.findByDate(date);
        Report yesterdayDate =  this.reportRepository.findByDate(oneDayAgo.toString());
        Report day7Report = this.reportRepository.findByDate(oneWeekAgo.toString());
        Report day14Report = reportRepository.findByDate(twoWeeksAgo.toString());      
        

        if(currDateReport  == null) {
        	currDateReport = new Report();
        	currDateReport.setDate(date);
        }
        else if (currDateReport != null && currDateReport.getAccumPositives()>0) {
        	return currDateReport;
        }
        
        
        int accumYesterday =   yesterdayDate   == null?    0:yesterdayDate.getAccumPositives();      

        int todayPositive  =   currDateReport  == null?    0:currDateReport.getPositivePCR();
        int sevenAgo       =   day7Report      == null?	 0:day7Report.getPositivePCR();
        int fourTeenAgo    =   day14Report     == null?    0:day14Report.getPositivePCR();

        int less80Percent = (int)(0.8*sevenAgo);
        int less20Percent = (int) (fourTeenAgo*0.2);
        
       
        
        int sum =(int) (todayPositive + accumYesterday - less80Percent  - less20Percent)    ;
     
        int[] arr = {0,0,1,1,2,2};   

        int accumNorthYesterday =   yesterdayDate   == null?    0:yesterdayDate.getNorthCount();      
        int accumSouthYesterday =   yesterdayDate   == null?    0:yesterdayDate.getSouthCount();      
        int accumCenterYesterday =   yesterdayDate   == null?    0:yesterdayDate.getCenterCount();      

        currDateReport.setCenterCount( currDateReport.getCenterCount() + accumCenterYesterday );
        currDateReport.setNorthCount(  currDateReport.getNorthCount() + accumNorthYesterday);
    	currDateReport.setSouthCount( currDateReport.getSouthCount() + accumSouthYesterday);
        int randomArea = new Random().nextInt(arr.length);
      
        int toReduce =  less80Percent  + less20Percent;
        if( toReduce > 0)
        	subscribe(currDateReport, toReduce , arr[randomArea]);  
    	
        
        currDateReport.setAccumPositives(sum);

//        System.out.println(currDateReport);
        reportRepository.save(currDateReport);
        return currDateReport;
    }

    private void subscribe(Report currDateReport ,int toReduce ,int randomArea) {

     	int negativeNum;
    	switch(randomArea) {
    	case 0:
        	if( currDateReport.getNorthCount()  < toReduce ) {
        		currDateReport.setNorthCount(0);
        		negativeNum = (currDateReport.getNorthCount() - toReduce)*-1;
        		if(currDateReport.getCenterCount() > negativeNum) {
        			subscribe(currDateReport, negativeNum , 2);
        		}
        		else {
        			subscribe(currDateReport, negativeNum, 1);
        		}
        	} 	
        	else
        		currDateReport.setNorthCount(currDateReport.getNorthCount() - toReduce);
    		break;
    	case 1:    		
    		if( currDateReport.getSouthCount()  < toReduce ) {
        		currDateReport.setSouthCount(0);
        		negativeNum = (currDateReport.getSouthCount() - toReduce)*-1;
        		if(currDateReport.getNorthCount() > negativeNum) {
        			subscribe(currDateReport, negativeNum , 0);
        		}
        		else {
        			subscribe(currDateReport, negativeNum, 2);
        		}
        	} 	
        	else
        		currDateReport.setSouthCount(currDateReport.getSouthCount() - toReduce);
    		break;
    	case 2:    
    		if( currDateReport.getCenterCount()  < toReduce ) {
        		currDateReport.setCenterCount(0);
        		negativeNum = (currDateReport.getCenterCount() - toReduce)*-1;
        		if(currDateReport.getSouthCount() > negativeNum) {
        			subscribe(currDateReport, negativeNum , 1);
        		}
        		else {
        			subscribe(currDateReport, negativeNum, 0);
        		}
        	} 	
        	else
        		currDateReport.setCenterCount(currDateReport.getCenterCount() - toReduce);
    		break;
    	}
    }

    
    private String createJson(Report report) throws JsonProcessingException {
    	 ObjectMapper mapper = new ObjectMapper();
  	    String json = mapper.writeValueAsString(report); 
      	return json;
    }
    
    private String createJson(List<Report> reports) throws JsonProcessingException {
   	 ObjectMapper mapper = new ObjectMapper();
 	    String json = mapper.writeValueAsString(reports); 
     	return json;
   }
    
    
 
    /**
     * This function create new daily report
     * @param date
     * @param positives
     * @param numberOfPCRs
     * F()= TODAY.POSITIVES + R[date.minusDays(1)].positives
     * -0.8*(R[date.minusDays(7)].positives-R[date.minusDays(8)])
     * -0.2*(R[date.minusDays(14)]-R[date.minusDays(15)])
     */
 /*   public void calculateDailyReport(String date, int positives, int numberOfPCRs) {
        Report newReport = new Report();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate oneWeekAgo = LocalDate.parse(date,formatter).minusDays(7);
        LocalDate twoWeeksAgo = LocalDate.parse(date,formatter).minusDays(14);


        //Test
        System.out.println("yesterday: " + oneWeekAgo);
        System.out.println("twoWeeksAgo: " + twoWeeksAgo);

        newReport.setPatients(numberOfPCRs);
        newReport.setDate(date);
        newReport.setPositiveRatio(positives/(numberOfPCRs-positives));
        System.out.println(newReport.getPositiveRatio());

        Report day7Report = this.reportRepository.findByDate(oneWeekAgo.toString());
        if (day7Report == null) {
            day7Report = new Report();
            System.out.println("DAY 7 NOT EXISTS !");
            day7Report.setPositivePCR(0);
        }
        Report day14Report = reportRepository.findByDate(twoWeeksAgo.toString());
        if (day14Report == null) {
            day14Report = new Report();
            System.out.println("DAY 14 NOT EXISTS !");
            day14Report.setPositivePCR(0);
        }
        double numberOfHospitalized = positives * 0.2 + day7Report.getPositivePCR() + 0.2 - day14Report.getPositivePCR() * 0.2;
        newReport.setPositivePCR((int) numberOfHospitalized);

        System.out.println(newReport);
        reportRepository.save(newReport);
    }*/


}
