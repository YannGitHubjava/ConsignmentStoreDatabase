package com.KevinMcClean;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kevin on 4/21/2015.
 */
public class CharityRecordsGUI extends ConsignmentStoreViewer{
    //TODO charityRecordsTable shows all the records where the charity boolean is true.
    private JTable charityRecordsTable;
    private JPanel charityRecordsPanel;
    private JButton exitButton;

    CharityRecordsGUI(ConsignmentStoreController csc){
        setContentPane(charityRecordsPanel);
        pack();
        setVisible(true);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                charityRecordsPanel.removeAll();
                setVisible(false);
            }
        });
    }
}
