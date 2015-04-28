package com.KevinMcClean;

import java.util.Date;

/**
 * Created by Kevin on 4/28/2015.
 */
public class Sales {
    private int salesID;
    private int recordID;
    private int consignorID;
    private Date salesDate;
    private double price;
    private double totalToStore;
    private double totalToConsignor;

    Sales(int sid, int rid, int cid, Date date, double price){
        this.salesID = sid;
        this. recordID = rid;
        this.consignorID = cid;
        this.salesDate = date;
        this.price = price;
        this.totalToConsignor = (price *.4);
        this.totalToStore = (this.price - this.totalToConsignor);
    }
}
