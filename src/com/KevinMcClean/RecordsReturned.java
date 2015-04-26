package com.KevinMcClean;

import java.util.Date;

/**
 * Created by Kevin on 4/26/2015.
 */
public class RecordsReturned{
    private int recordID;
    private int consignorID;
    private String artist;
    private String title;
    private Double price;
    private Date consignmentDate;

    RecordsReturned(int rid, int cid, String rar, String rti, Double pri){
        this.recordID = rid;
        this.consignorID = cid;
        this.artist = rar;
        this.title = rti;
        this.price = pri;
        this.consignmentDate = new Date();
    }
}
