package com.KevinMcClean;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kevin on 4/21/2015.
 */
public class ConsignorsGUI extends ConsignmentStoreViewer{
    //TODO this table has everyone who has consigned a record to this store.
    private JTable consignorsTable;
    private JButton newConsignorsButton;
    private JButton exitButton;
    private JPanel consignorsGUIPanel;

    ConsignorsGUI(ConsignmentStoreController csc){
        this.myController = csc;
        setContentPane(consignorsGUIPanel);
        pack();
        setVisible(true);

        newConsignorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewConsignorsGUI newConsignorsGUI = new NewConsignorsGUI(myController);
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consignorsGUIPanel.removeAll();
                setVisible(false);
            }
        });
    }
}