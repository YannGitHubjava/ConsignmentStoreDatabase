package com.KevinMcClean;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kevin on 4/21/2015.
 */
public class ConsignmentStoreViewer extends JFrame{
    ConsignmentStoreController myController;

    ConsignmentStoreViewer(){
    }

    ConsignmentStoreViewer(ConsignmentStoreController csc){
        this.myController = csc;
        ConsignmentStoreViewerGUI gui = new ConsignmentStoreViewerGUI(myController);
        //gui.newGUI();
    }

    public void buyRecords(String artist, String title, String price, int consignorID){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date consignmentDate = new Date();
        String stringDate = sdf.format(consignmentDate);
        System.out.println(stringDate);
        myController.requestBuyRecord();
    }
    public void newGUI(){

    }
}
