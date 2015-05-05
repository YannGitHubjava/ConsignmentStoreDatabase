package com.KevinMcClean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

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
    private ResultSet resultSet;
    private ConsignmentStoreController storeController;
    private StoreTableModel stm;

    //sells a record.
    SellGUI(ConsignmentStoreController csc){
        this.storeController = csc;
        setContentPane(buySellGUIPanel);
        pack();
        setVisible(true);

        //shows the records in the main room.
        resultSet = displayRecordsinMainRoomViewer(storeController);
        stm = new StoreTableModel(storeController, resultSet);
        recordsInStoreTable.setModel(stm);
        recordsInStoreTable.setGridColor(Color.BLACK);


        //closes the GUI.
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        //allows the user to sell a record.
        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recordsInStoreTable.getColumnCount();
                int row = recordsInStoreTable.getSelectedRow();

                Object valueAt = recordsInStoreTable.getValueAt(row, 6);
                String valueString = valueAt.toString();
                Integer recordID = Integer.parseInt(valueString);
                recordSaleViewer(recordID);
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
