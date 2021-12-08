package com.philips.project.analyticsms.services;

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


    /**
     * This function create new daily report
     * @param date
     * @param positives
     * @param numberOfPCRs
     * F()= TODAY.POSITIVES + R[date.minusDays(1)].positives
     * -0.8*(R[date.minusDays(7)].positives-R[date.minusDays(8)])
     * -0.2*(R[date.minusDays(14)]-R[date.minusDays(15)])
     */
    public void calculateDailyReport(String date, int positives, int numberOfPCRs) {
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
    }


}
