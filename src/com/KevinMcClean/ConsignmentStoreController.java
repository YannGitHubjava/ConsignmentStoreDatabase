package com.KevinMcClean;

import java.util.Date;
import java.util.LinkedList;

public class ConsignmentStoreController {
    ConsignmentStoreModel model;
    LinkedList<Record> allRecords;
    LinkedList<Consignor> allConsignors;

    ConsignmentStoreController(){
        model = new ConsignmentStoreModel();
        //requestAllInStoreRecords();
        //requestAllConsignors();
    }

    public void requestBuyRecords(String artist, String title, Double price, Date date){
        System.out.println("Artist: " + artist + "\nTitle: " + title + "\nPrice: " + "\nPurchased on: " + date);
    }

    public void requestAllInStoreRecords(){
        allRecords = model.displayAllRecords();
        for(Record r: allRecords){
            System.out.println(r.toString());
        }
    }

    public void requestAllConsignors(){
        allConsignors = model.displayAllConsignors();
        for(Consignor c: allConsignors){
            System.out.println(c.toString());
        }
    }
}
