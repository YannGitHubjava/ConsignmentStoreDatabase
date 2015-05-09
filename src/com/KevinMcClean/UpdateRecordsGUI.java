package com.KevinMcClean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * Created by Kevin on 4/21/2015.
 */

//this shows the records that need to be either returned to their consignor, but in the bargain basement, or given to charity.
public class UpdateRecordsGUI extends ConsignmentStoreViewer{


    private JTable monthOldTable;
    //TODO this is a list of all the records that have been in the story for longer than a year, but still have their Charity boolean set to false
    private JTable yearOldRecordsTable;
    private JButton donatedToCharityButton;
    private JButton exitButton;
    private JButton sentToBargainBasementButton;
    private JPanel updateRecordsGUIPanel;
    private JButton returnedToOwnerButton;
    private ResultSet rsMonthOld;
    private ResultSet rsYearOld;
    private StoreTableModel stm;
    private ConsignmentStoreController storeController;

    UpdateRecordsGUI(ConsignmentStoreController csc){
        this.storeController = csc;
        setContentPane(updateRecordsGUIPanel);
        pack();
        setVisible(true);

        //sets up the table displays.
        rsMonthOld = displayMonthOldsRecordsViewer(storeController);
        stm = new StoreTableModel(storeController, rsMonthOld);
        monthOldTable.setModel(stm);
        monthOldTable.setGridColor(Color.BLACK);

        rsYearOld = displayYearOldRecordsViewer(storeController);
        stm = new StoreTableModel(storeController, rsYearOld);
        yearOldRecordsTable.setModel(stm);
        yearOldRecordsTable.setGridColor(Color.BLACK);

        returnedToOwnerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO this action boolean should take the recordID of the highlighted record and use it as the id to process.
                int recordID = getID("RECORD_ID", monthOldTable.getSelectedRow(), monthOldTable, rsMonthOld);
                boolean returned = returnRecordToConsignorViewer(recordID, storeController);
                    if(returned){
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
                //int row = monthOldTable.getSelectedRow();
                int recordID = getID("RECORD_ID", monthOldTable.getSelectedRow(), monthOldTable, rsMonthOld);
                boolean isInBasement = recordToBasementViewer(recordID, storeController);
                if (!isInBasement){
                    JOptionPane.showMessageDialog(updateRecordsGUIPanel, "This record was not sent to the basementRecords table.");
                }

                //TODO allow the user to select specific records from the monthOldTable and puts them in the basementRecords table.
                //TODO it then updates the table to show the new list.
            }
        });
        donatedToCharityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int recordID = getID("RECORD_ID", yearOldRecordsTable.getSelectedRow(), yearOldRecordsTable, rsYearOld);
                boolean charity = recordToCharityViewer(recordID, storeController);
                if(charity){
                    JOptionPane.showMessageDialog(updateRecordsGUIPanel, "Record given to charity.");
                    return;
                }
                else{
                    JOptionPane.showMessageDialog(updateRecordsGUIPanel, "Record not given to charity.");
                    return;
                }
                //TODO allow the user to select specific records from the yearOldTable and puts them in the charity table.
                //TODO it then updates the table to show the new list.
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
