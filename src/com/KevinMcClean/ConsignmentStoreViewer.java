package com.KevinMcClean;

import javax.swing.*;
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

    public void buyRecords(String artist, String title, String price){
        double doublePrice = Double.parseDouble(price);
        Date todaysDate = new Date();
        myController.requestBuyRecords(artist, title, doublePrice, todaysDate);
    }
    public void newGUI(){

    }
}
