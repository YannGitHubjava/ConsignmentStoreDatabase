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

        //displays the table of Consignors whom the store owes money.
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


        //pays the Consignor.
        payConsignorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //this makes sure the user has selected a consignor that is owed money.
                int row = consignorsOwedTable.getSelectedRow();
                boolean isRowSelected = ivIsRowSelected(row);
                if (!isRowSelected){
                    JOptionPane.showMessageDialog(owedToConsignorsGUIPanel, "Please select a consignor to pay.");
                    return;
                }

                //this checks to make sure that the user has entered an amount of money into the field.
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

                //this gets the amount that is owed from the column that is listed.
                try {
                    column = (resultSet.findColumn("AMOUNT_OWED")-1);
                    valueAt = consignorsOwedTable.getValueAt(row, column);
                    valueString = valueAt.toString();
                    amountOwed = Double.parseDouble(valueString);
                }
                catch (SQLException sqle){
                }

                //this check makes sure that the amount listed is not more than what is owed to the consignor.
                if(payment > amountOwed || amountOwed == NOT_A_DOUBLE){
                    JOptionPane.showMessageDialog(owedToConsignorsGUIPanel, "You cannot pay more than owed.");
                    return;
                }

                int consignorID = getID("consignor_id", row, consignorsOwedTable, resultSet);
                Float paymentFloat = (float) payment;

                //attempts to update the consignors record to show how much they have been paid.
                boolean updateTotal_PaidRecords = updateRecordsViewer("consignors", "total_paid", paymentFloat, "consignor_id", consignorID, storeController);
                if (!updateTotal_PaidRecords){
                    JOptionPane.showMessageDialog(owedToConsignorsGUIPanel, "Could not update total_paid.");
                    return;
                }

                Float owedReduction = (float) amountOwed - paymentFloat;
                boolean updateAmount_OwedRecords = updateRecordsViewer("consignors", "amount_owed", owedReduction, "consignor_id", consignorID, storeController);

                //attempts to update the record to deduct the amount paid from what is owed to the consignor.
                if (!updateAmount_OwedRecords){
                    JOptionPane.showMessageDialog(owedToConsignorsGUIPanel, "Could not update total_paid.");
                    return;
                }

                JOptionPane.showMessageDialog(owedToConsignorsGUIPanel, "Consignor payment processed.");

                //updates the table.
                resultSet = consignorsOwedViewer(storeController);
                stm = new StoreTableModel(storeController, resultSet);
                consignorsOwedTable.setModel(stm);

            }
        });

        //closes the window.
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConsignmentStoreViewerGUI.consignorsOwedGUIOpen = false;
                dispose();
            }
        });
    }
}
