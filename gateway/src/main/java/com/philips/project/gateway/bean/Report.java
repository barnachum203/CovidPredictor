package com.philips.project.gateway.bean;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Report {
    private int id; // report id
    private String date; // report date
    private double positiveRatio; // ratio = positive/negative
    private int patients; // number of PCRs
    private int positivePCR; // positive PCRs
    private int accumPositives;
}
