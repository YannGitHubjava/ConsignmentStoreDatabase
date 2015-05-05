package com.KevinMcClean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * Created by Kevin on 4/21/2015.
 */
public class UpdateRecordsGUI extends ConsignmentStoreViewer{


    private JTable monthOldTable;
    //TODO this is a list of all the records that have been in the story for longer than a year, but still have their Charity boolean set to false
    private JTable yearOldRecordsTable;
    private JButton donatedToCharityButton;
    private JButton exitButton;
    private JButton sentToBargainBasementButton;
    private JPanel updateRecordsGUIPanel;
    private JButton returnedToOwnerButton;
    private ResultSet resultSet;
    private StoreTableModel stm;
    private ConsignmentStoreController storeController;

    UpdateRecordsGUI(ConsignmentStoreController csc){
        this.storeController = csc;
        setContentPane(updateRecordsGUIPanel);
        pack();
        setVisible(true);

        resultSet = displayMonthOldsRecordsViewer(storeController);
        stm = new StoreTableModel(storeController, resultSet);
        monthOldTable.setModel(stm);
        monthOldTable.setGridColor(Color.BLACK);

        resultSet = displayYearOldRecordsViewer(storeController);
        stm = new StoreTableModel(storeController, resultSet);
        yearOldRecordsTable.setModel(stm);
        yearOldRecordsTable.setGridColor(Color.BLACK);

        returnedToOwnerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*TODO this action boolean should take the recordID of the highlighted record and use it as the id to process.
                //boolean action = returnRecordViewer(recordID);
                if (!action){
                    JOptionPane.showMessageDialog(updateRecordsGUIPanel, "Was not able to return record in database.");
                }*/
            }
        });

        sentToBargainBasementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO allow the user to select specific records from the monthOldTable and puts them in the basementRecords table.
                //TODO it then updates the table to show the new list.
            }
        });
        donatedToCharityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO allow the user to select specific records from the yearOldTable and puts them in the charity table.
                //TODO it then updates the table to show the new list.
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRecordsGUIPanel.removeAll();
                setVisible(false);
            }
        });


    }
}
