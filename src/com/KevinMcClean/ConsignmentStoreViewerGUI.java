package com.KevinMcClean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kevin on 4/21/2015.
 */
public class ConsignmentStoreViewerGUI extends ConsignmentStoreViewer{
    private JButton sellRecordsButton;
    private JButton consignorsButton;
    private JButton recordsInStoreButton;
    private JButton updateRecordsButton;
    private JButton quitButton;
    private JPanel consignmentStoreViewerGUIPanel;
    private JButton buyButton;


    ConsignmentStoreViewerGUI(ConsignmentStoreController csc){
        setContentPane(consignmentStoreViewerGUIPanel);
        pack();
        setVisible(true);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        sellRecordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SellGUI sellGUI = new SellGUI(myController);
            }
        });
        consignorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConsignorsGUI consignorsGUI = new ConsignorsGUI(myController);
            }
        });
        recordsInStoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RecordsGUI recordsGUI = new RecordsGUI(myController);
            }
        });
        updateRecordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UpdateRecordsGUI updateRecordsGUI = new UpdateRecordsGUI(myController);
            }
        });
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BuyGUI buyGUI = new BuyGUI(myController);
            }
        });
    }
}
