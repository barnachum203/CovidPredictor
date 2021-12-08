package com.philips.project.analyticsms.repository;

import com.philips.project.analyticsms.beans.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report , Integer> {
    public List<Report> findAll();
    public Report findById(int id);
    public Report findByDate(String date);



}
