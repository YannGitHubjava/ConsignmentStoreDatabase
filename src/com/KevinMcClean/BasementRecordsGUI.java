package com.KevinMcClean;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Kevin on 5/4/2015.
 */
public class BasementRecordsGUI extends ConsignmentStoreViewer{
    private JButton exitButton;
    private JButton sellButton;

    private JTable basementRecordsTable;

    private JPanel basementRecordsGUIPanel;

    private ResultSet resultSet;

    private ConsignmentStoreController storeController;

    private StoreTableModel stm;

    Integer recordID;

    //This shows all the records that are in the basementRecords table.
    BasementRecordsGUI(ConsignmentStoreController csc){
        this.storeController = csc;
        setContentPane(basementRecordsGUIPanel);
        pack();
        setVisible(true);
        //setDefaultCloseOperation(closeOperation());

        resultSet = displayBasementRecordsViewer(storeController);
        stm = new StoreTableModel(storeController, resultSet);
        basementRecordsTable.setModel(stm);

        //from http://stackoverflow.com/questions/4737495/disposing-and-closing-windows-in-java.
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ConsignmentStoreViewerGUI.basementRecordsOpen= false;
                dispose();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConsignmentStoreViewerGUI.basementRecordsOpen = false;
                dispose();
            }
        });

        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = basementRecordsTable.getSelectedRow();
                if(row < 0) {
                    JOptionPane.showMessageDialog(basementRecordsGUIPanel, "You must select a record to sell!");
                    return;
                }
                System.out.println("Row: " + row);
                recordID = getID("RECORD_ID", row, basementRecordsTable, resultSet);
                if (recordID == NOT_AN_INT)
                {
                    JOptionPane.showMessageDialog(basementRecordsTable, "Could not sell this record. Please make sure you have selected an album.");
                }
                boolean basementSale = recordBasementSaleViewer(recordID, storeController);
                if(basementSale){
                    JOptionPane.showMessageDialog(basementRecordsGUIPanel, "Record sold.");
                }
                else{
                    JOptionPane.showMessageDialog(basementRecordsGUIPanel, "Record not sold.");
                }

                resultSet = displayBasementRecordsViewer(storeController);
                stm = new StoreTableModel(storeController, resultSet);
                basementRecordsTable.setModel(stm);
            }
        });

    }
}
