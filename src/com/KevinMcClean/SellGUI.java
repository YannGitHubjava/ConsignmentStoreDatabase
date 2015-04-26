package com.KevinMcClean;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kevin on 4/21/2015.
 */
public class SellGUI extends ConsignmentStoreViewer{

    //TODO recordsinStoreTable will show every record where the boolean Charity is false.
    private JTable recordsInStoreTable;
    private JButton exitButton;
    private JButton sellButton;
    private JButton buyButton;
    private JPanel buySellGUIPanel;

    SellGUI(ConsignmentStoreController csc){
        setContentPane(buySellGUIPanel);
        pack();
        setVisible(true);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buySellGUIPanel.removeAll();
                setVisible(false);
            }
        });

        //allows the user to sell a record.
        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO have this allow the user to select a record from the table above, and press this button to sell the record.
            }
        });

        //allows the user to buy a record.
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BuyGUI buyGUI = new BuyGUI(myController);
            }
        });
    }
}
