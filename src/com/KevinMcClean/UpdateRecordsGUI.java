package com.KevinMcClean;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kevin on 4/21/2015.
 */
public class UpdateRecordsGUI extends ConsignmentStoreViewer{

    //TODO this is a list of all the records that have been in the store for longer than a month still have their basement boolean set to false.
    private JTable monthOldTable;

    //TODO this is a list of all the records that have been in the story for longer than a year, but still have their Charity boolean set to false
    private JTable yearOldRecordsTable;
    private JButton updateYearOldRecordsButton;
    private JButton exitButton;
    private JButton updateMonthOldRecordsButton;
    private JPanel updateRecordsGUIPanel;

    UpdateRecordsGUI(ConsignmentStoreController csc){
        setContentPane(updateRecordsGUIPanel);
        pack();
        setVisible(true);

        updateMonthOldRecordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO allow the user to select specific records from the monthOldTable and send them to the basement (i.e. set the boolean to true).
                //TODO it then updates the table to show the new list.
            }
        });
        updateYearOldRecordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO allow the user to select specific records from the yearOldTable and send them to the basement (i.e. set the boolean to true).
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
