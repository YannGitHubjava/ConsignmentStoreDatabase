package com.KevinMcClean;

import java.util.Date;

/**
 * Created by Kevin on 4/21/2015.
 */
public class Record {
    private int recordID;
    private int consignorID;
    private String artist;
    private String title;
    private Double price;
    private Date consignmentDate;

    Record(int rid, int cid, String rar, String rti, Double pri, Date date){
        this.recordID = rid;
        this.consignorID = cid;
        this.artist = rar;
        this.title = rti;
        this.price = pri;
        this.consignmentDate = date;
    }

    public String toString(){
        String recordString = "Artist: " + this.artist + "\nTitle: " + this.title + "\nConsignorID: " + this.consignorID
                +"\nRecordID: " + this.recordID + "\nPrice: " + this.price + "\nConsignment Date: " + this.consignmentDate + "\n";
        return recordString;
    }
}
