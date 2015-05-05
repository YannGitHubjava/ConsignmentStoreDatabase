package com.KevinMcClean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kevin on 4/21/2015.
 */
//this creates the ConsignmentStoreViewerGUI. This is the main screen, which will allow the user to navigate to all the different subscreens that are available.
public class ConsignmentStoreViewerGUI extends ConsignmentStoreViewer{
    private JButton sellRecordsButton;
    private JButton consignorsButton;
    private JButton recordsInStoreButton;
    private JButton updateRecordsButton;
    private JButton quitButton;
    private JPanel consignmentStoreViewerGUIPanel;
    private JButton buyButton;
    private JButton recordsInBasementButton;
    private JButton recordsGivenToCharityButton;
    private ConsignmentStoreController storeController;


    ConsignmentStoreViewerGUI(ConsignmentStoreController csc){
        this.storeController = csc;
        setContentPane(consignmentStoreViewerGUIPanel);
        pack();
        setVisible(true);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.myController = csc;

        /*All of the Buttons are set up to open up a new GUI with all the necessary components for that GUI. This is
        so that this screen can be a basic system where the user can choose which they wish to interact with.
         */

        sellRecordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SellGUI sellGUI = new SellGUI(storeController);
            }
        });
        consignorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConsignorsGUI consignorsGUI = new ConsignorsGUI(storeController);
            }
        });
        recordsGivenToCharityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CharityRecordsGUI crg = new CharityRecordsGUI(storeController);
            }
        });
        recordsInBasementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BasementRecordsGUI brg = new BasementRecordsGUI(storeController);
            }
        });
        recordsInStoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RecordsInStoreGUI recordsInStoreGUI = new RecordsInStoreGUI(storeController);
            }
        });
        updateRecordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UpdateRecordsGUI updateRecordsGUI = new UpdateRecordsGUI(storeController);
            }
        });
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BuyGUI buyGUI = new BuyGUI(storeController);
            }
        });
        //Closes the program.
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cleanupViewer();
                System.exit(0);
            }
        });
    }
}
