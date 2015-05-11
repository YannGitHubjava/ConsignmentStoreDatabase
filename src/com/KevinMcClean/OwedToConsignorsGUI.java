package com.KevinMcClean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Kevin on 5/10/2015.
 */
public class OwedToConsignorsGUI extends ConsignmentStoreViewer{
    private JPanel owedToConsignorsGUIPanel;
    private JTable consignorsOwedTable;
    private JTextField paymentTextField;
    private JButton payConsignorButton;
    private JButton exitButton;

    ResultSet resultSet;

    StoreTableModel stm;

    ConsignmentStoreController storeController;

    OwedToConsignorsGUI(ConsignmentStoreController csc){
        this.storeController = csc;
        setContentPane(owedToConsignorsGUIPanel);
        pack();
        setVisible(true);

        resultSet = consignorsOwedViewer(storeController);
        stm = new StoreTableModel(storeController, resultSet);
        consignorsOwedTable.setModel(stm);
        consignorsOwedTable.setGridColor(Color.black);

        //from http://stackoverflow.com/questions/4737495/disposing-and-closing-windows-in-java.
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ConsignmentStoreViewerGUI.consignorsOwedGUIOpen = false;
                dispose();
            }
        });

        payConsignorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = consignorsOwedTable.getSelectedRow();
                boolean isRowSelected = ivIsRowSelected(row);
                if (!isRowSelected){
                    JOptionPane.showMessageDialog(owedToConsignorsGUIPanel, "Please select a consignor to pay.");
                    return;
                }

                String paymentString = paymentTextField.getText();
                double payment = ivIsPriceDouble(paymentString);
                if(payment == NOT_A_DOUBLE || paymentString.isEmpty()){
                    JOptionPane.showMessageDialog(owedToConsignorsGUIPanel, "Please enter an amount.");
                    return;
                }

                double amountOwed = NOT_A_DOUBLE;
                int column;
                Object valueAt;
                String valueString;
                try {
                    column = (resultSet.findColumn("AMOUNT_OWED")-1);
                    valueAt = consignorsOwedTable.getValueAt(row, column);
                    valueString = valueAt.toString();
                    amountOwed = Double.parseDouble(valueString);
                }
                catch (SQLException sqle){
                }

                if(payment > amountOwed || amountOwed == NOT_A_DOUBLE){
                    JOptionPane.showMessageDialog(owedToConsignorsGUIPanel, "You cannot pay more than owed.");
                }

                int consignorID = getID("consignor_id", row, consignorsOwedTable, resultSet);
                Float paymentFloat = (float) payment;
                System.out.println("ConsignorID: " + consignorID);
                System.out.println("Payment: " + paymentFloat);
                boolean updateTotal_PaidRecords = updateRecordsViewer("consignors", "total_paid", paymentFloat, "consignor_id", consignorID, storeController);
                if (!updateTotal_PaidRecords){
                    JOptionPane.showMessageDialog(owedToConsignorsGUIPanel, "Could not update total_paid.");
                    return;
                }

                Float owedReduction = (float) amountOwed - paymentFloat;
                boolean updateAmount_OwedRecords = updateRecordsViewer("consignors", "amount_owed", owedReduction, "consignor_id", consignorID, storeController);

                if (!updateAmount_OwedRecords){
                    JOptionPane.showMessageDialog(owedToConsignorsGUIPanel, "Could not update total_paid.");
                    return;
                }

                resultSet = consignorsOwedViewer(storeController);
                stm = new StoreTableModel(storeController, resultSet);
                consignorsOwedTable.setModel(stm);

            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConsignmentStoreViewerGUI.consignorsOwedGUIOpen = false;
                dispose();
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
