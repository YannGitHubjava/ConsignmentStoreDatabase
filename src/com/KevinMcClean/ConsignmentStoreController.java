package com.KevinMcClean;

import java.util.LinkedList;

public class ConsignmentStoreController {
    ConsignmentStoreModel model;
    LinkedList<Record> allRecords;
    LinkedList<Consignor> allConsignors;

    ConsignmentStoreController() {
        model = new ConsignmentStoreModel();
        requestUpdatedConsignorsLIst();
        requestUpdatedRecordsLIst();

    }

    public void requestBuyRecords(String artist, String title, String date, Double price, int consignorID){
        Record buyRecord = model.buyRecord(consignorID, artist, title, price, date);
        if (buyRecord != null){
            allRecords.add(buyRecord);
        }
        else{
            System.out.println("Database could not purchase record.");
        }

    }

    public void requestUpdatedRecordsLIst(){
        allRecords = model.updateRecordsList();
    }

    public void requestUpdatedConsignorsLIst(){
        allConsignors = model.updateConsignorsList();
    }

    public void requestBuyRecord(){

    }
}
