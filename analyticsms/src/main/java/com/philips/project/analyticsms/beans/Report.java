package com.philips.project.analyticsms.beans;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // report id
    private String date; // report date
    private double positiveRatio; // ratio = positive/negative
    private int patients; // number of PCRs
    private int positivePCR; // positive PCRs
    private int accumPositives;

    public int calculatePatients(){
        //TODO: implement calculator
        return 1;
    }
}
