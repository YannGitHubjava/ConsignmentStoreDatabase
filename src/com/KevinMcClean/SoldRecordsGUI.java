package com.KevinMcClean;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kevin on 4/21/2015.
 */
public class SoldRecordsGUI extends ConsignmentStoreViewer{

    //TODO displays the record sold, who the consignor was, how much the consignor and the store made off the sale.
    private JTable soldRecordsTable;
    private JButton exitButton;
    private JPanel soldRecordsPanel;

    SoldRecordsGUI(ConsignmentStoreController csc){
        setContentPane(soldRecordsPanel);
        pack();
        setVisible(true);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                soldRecordsPanel.removeAll();
                setVisible(false);
            }
        });

    }
}
