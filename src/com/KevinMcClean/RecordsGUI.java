package com.KevinMcClean;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kevin on 4/21/2015.
 */
public class RecordsGUI extends ConsignmentStoreViewer {
    private JPanel recordsGUIPanel;
    private JTable recordsTable;
    private JButton buyButton;
    private JButton exitButton;
    private JButton sellButton;

    RecordsGUI(ConsignmentStoreController csc) {
        setContentPane(recordsGUIPanel);
        pack();
        setVisible(true);
        recordsTable.getRowCount();

        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BuyGUI buyGUI = new BuyGUI(myController);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recordsGUIPanel.removeAll();
                setVisible(false);
            }
        });
    }

    public int getRowCount() {
        return 5;
    }
}
