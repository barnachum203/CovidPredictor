package com.philips.project.gateway.bean;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;


@Component
@Data
@JsonDeserialize
public class ReportList {
    private List<Report> reports;

    public ReportList() {
        reports = new ArrayList<>();

    }



}

