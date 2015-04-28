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
    private Date salesDate;
    private Date charityOrReturnDate;
    private int salesID;

    //this constructor is for records which are on the regular sale floor.
    Record(int rid, int cid, String rar, String rti, Double pri, Date date){
        this.recordID = rid;
        this.consignorID = cid;
        this.artist = rar;
        this.title = rti;
        this.price = pri;
        this.consignmentDate = date;
    }

    //this constructor is for records which are for records that have been sold.
    Record(int rid, int cid, int sid, String rar, String rti, Double pri, Date consignmentDate, Date salesDate){
        this.recordID = rid;
        this.consignorID = cid;
        this.artist = rar;
        this.title = rti;
        this.price = pri;
        this.consignmentDate = consignmentDate;
        this.salesDate = salesDate;
        this.salesID = sid;
    }

    //this constructor is for records which have been given to charity or returned to the owner
    Record(int rid, int cid, String rar, String rti, Double pri, Date consignmentDate, Date charityOrReturnDate){
        this.recordID = rid;
        this.consignorID = cid;
        this.artist = rar;
        this.title = rti;
        this.price = pri;
        this.consignmentDate = consignmentDate;
        this.charityOrReturnDate = charityOrReturnDate;
    }


    public String toString(){
        String recordString = "Artist: " + this.artist + "\nTitle: " + this.title + "\nConsignorID: " + this.consignorID
                +"\nRecordID: " + this.recordID + "\nPrice: " + this.price + "\nConsignment Date: " + this.consignmentDate + "\n";
        return recordString;
    }
}
