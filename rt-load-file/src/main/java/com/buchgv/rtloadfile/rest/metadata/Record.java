package com.buchgv.rtloadfile.rest.metadata;

import java.time.LocalDate;

import lombok.Data;

/**
 * Describes transformed Record to be send back to FE
 * 
 */
@Data
public class Record {
    private int id;

    private String docType;

    private int companyID;

    private LocalDate date;

    private int docID;

    private String sign;

    private int amount;
}


