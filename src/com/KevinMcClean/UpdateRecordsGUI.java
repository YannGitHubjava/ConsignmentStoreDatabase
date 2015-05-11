package com.KevinMcClean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;

/**
 * Created by Kevin on 4/21/2015.
 */

//this shows the records that need to be either returned to their consignor, but in the bargain basement, or given to charity.
public class UpdateRecordsGUI extends ConsignmentStoreViewer{

    private JPanel updateRecordsGUIPanel;

    private JTable monthOldTable;
    private JTable yearOldRecordsTable;

    private JButton donatedToCharityButton;
    private JButton exitButton;
    private JButton returnedToOwnerButton;
    private JButton sentToBargainBasementButton;


    private ResultSet rsMonthOld;
    private ResultSet rsYearOld;

    private StoreTableModel stm;

    private ConsignmentStoreController storeController;

    UpdateRecordsGUI(ConsignmentStoreController csc){
        this.storeController = csc;
        setContentPane(updateRecordsGUIPanel);
        pack();
        setVisible(true);

        //these 8 lines set up the table displays.
        rsMonthOld = displayMonthOldsRecordsViewer(storeController);
        stm = new StoreTableModel(storeController, rsMonthOld);
        monthOldTable.setModel(stm);
        monthOldTable.setGridColor(Color.BLACK);

        rsYearOld = displayYearOldRecordsViewer(storeController);
        stm = new StoreTableModel(storeController, rsYearOld);
        yearOldRecordsTable.setModel(stm);
        yearOldRecordsTable.setGridColor(Color.BLACK);

        //from http://stackoverflow.com/questions/4737495/disposing-and-closing-windows-in-java.
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ConsignmentStoreViewerGUI.updateRecordsGUIOpen = false;
                dispose();
            }
        });

        returnedToOwnerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //makes sure that a record was chosen.
                int row = monthOldTable.getSelectedRow();
                if(row < 0) {
                    JOptionPane.showMessageDialog(updateRecordsGUIPanel, "Please choose a record!");
                    return;
                }


                int recordID = getID("RECORD_ID", row, monthOldTable, rsMonthOld);

                //sends the order to the table to return the record to the owner and lets them know if it succeeded.
                boolean returned = returnRecordToConsignorViewer(recordID, storeController);
                if(returned) {
                    JOptionPane.showMessageDialog(updateRecordsGUIPanel, "Record was returned to owner.");
                    return;
                }
                else{
                    JOptionPane.showMessageDialog(updateRecordsGUIPanel, "Record was not returned to owner.");
                    return;
                }
            }
        });

        sentToBargainBasementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //makes sure that a record was chosen.
                int row = monthOldTable.getSelectedRow();
                if(row < 0) {
                    JOptionPane.showMessageDialog(updateRecordsGUIPanel, "You much choose a record.");
                    return;
                }

                int recordID = getID("RECORD_ID", row, monthOldTable, rsMonthOld);

                //tells the database to put a record in the basement, then lets the user know if it succeeded.
                boolean isInBasement = recordToBasementViewer(recordID, storeController);
                if (isInBasement){
                    JOptionPane.showMessageDialog(updateRecordsGUIPanel, "This record was sent to the basementRecords table.");
                }
                else{
                    JOptionPane.showMessageDialog(updateRecordsGUIPanel, "This record was not sent to the basementRecords table.");
                }
            }
        });
        donatedToCharityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //makes sure that a record is chosen.
                int row = yearOldRecordsTable.getSelectedRow();
                if(row < 0) {
                    JOptionPane.showMessageDialog(updateRecordsGUIPanel, "You must choose a record.");
                    return;
                }

                int recordID = getID("RECORD_ID", row, yearOldRecordsTable, rsYearOld);

                //tells the database to send the record to the charityRecords table and delete if from the basementRecords...
                //...it then lets the user know if they succeeded.
                boolean charity = recordToCharityViewer(recordID, storeController);
                if(charity){
                    JOptionPane.showMessageDialog(updateRecordsGUIPanel, "Record given to charity.");
                    return;
                }
                else{
                    JOptionPane.showMessageDialog(updateRecordsGUIPanel, "Record not given to charity.");
                    return;
                }
            }
        });

        //closes the GUI.
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConsignmentStoreViewerGUI.updateRecordsGUIOpen = false;
                dispose();
            }
        });


    }
}
