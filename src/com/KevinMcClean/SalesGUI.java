package com.KevinMcClean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;

/**
 * Created by Kevin on 5/8/2015.
 */
public class SalesGUI extends ConsignmentStoreViewer{

    private JButton exitButton;

    private JTable salesTable;

    private JPanel salesGUIPanel;

    private ConsignmentStoreController storeController;

    private ResultSet resultSet;

    private StoreTableModel stm;

    SalesGUI(ConsignmentStoreController csc){
        this.storeController = csc;
        setContentPane(salesGUIPanel);
        pack();
        setVisible(true);

        //sets up the recordsTable for display.
        resultSet = displaySalesViewer(storeController);
        stm = new StoreTableModel(storeController, resultSet);
        salesTable.setModel(stm);
        salesTable.setGridColor(Color.black);

        //from http://stackoverflow.com/questions/4737495/disposing-and-closing-windows-in-java.
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ConsignmentStoreViewerGUI.salesGUIOpen = false;
                dispose();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConsignmentStoreViewerGUI.salesGUIOpen = false;
                dispose();
            }
        });
    }
}
