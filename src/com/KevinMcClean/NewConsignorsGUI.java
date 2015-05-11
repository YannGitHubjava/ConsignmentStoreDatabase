package com.KevinMcClean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.InputMismatchException;

/**
 * Created by Kevin on 4/21/2015.
 */
//this GUI opens when someone needs to add a new consignor to the database.
public class NewConsignorsGUI extends ConsignmentStoreViewer{
    private JTextField firstNameTextField;
    private JTextField phoneNumberTextField;
    private JTextField addressTextField;
    private JButton addNewConsignorButton;
    private JButton exitButton;
    private JPanel newConsignorsGUIPanel;
    private JTextField lastNameTextField;
    private JTextField cityTextField;
    private JComboBox stateComboBox;
    private ConsignmentStoreController storeController;
    private String[] states = {"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "KS", "IA", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "OH", "ND", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"};


    NewConsignorsGUI(ConsignmentStoreController csc){
        this.storeController = csc;
        setContentPane(newConsignorsGUIPanel);
        pack();
        setVisible(true);
        int count = 0;

        for(String state: states){
            stateComboBox.addItem(state);
            count++;
        }

        System.out.println("State count: " + count);


        //from http://stackoverflow.com/questions/4737495/disposing-and-closing-windows-in-java.
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ConsignmentStoreViewerGUI.newConsignorsGUIOpen = false;
                dispose();
            }
        });

        addNewConsignorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstNameText = firstNameTextField.getText();
                String lastNameText = lastNameTextField.getText();
                String address = addressTextField.getText();
                String city = cityTextField.getText();
                Object objectState = stateComboBox.getSelectedItem();
                String state = objectState.toString();
                String phoneNumber = phoneNumberTextField.getText();

                System.out.println(state);

                String[] fields = {firstNameText, lastNameText, address, city, phoneNumber};

                for (String field : fields) {
                    if (field.isEmpty()) {
                        JOptionPane.showMessageDialog(newConsignorsGUIPanel, "Please check " + field + " and make sure it is filled in.");
                        return;
                    }
                }

                boolean phoneCheck = ivCheckPhoneNo(phoneNumber);
                if(!phoneCheck){
                    JOptionPane.showMessageDialog(newConsignorsGUIPanel, "Please enter only integers for the phone number. No dashes, parentheses, or spaces. Please make sure the phone number is 10 digits.");
                    return;
                }

                boolean isNew = newConsignorViewer(firstNameText, lastNameText, address, city, state, phoneNumber, storeController);
                if(!isNew){
                    JOptionPane.showMessageDialog(newConsignorsGUIPanel, "This consignor is already in the database.");
                    return;
                }

                firstNameTextField.setText(null);
                lastNameTextField.setText(null);
                addressTextField.setText(null);
                cityTextField.setText(null);
                phoneNumberTextField.setText(null);

            }
        });
        //closes the GUI.
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConsignmentStoreViewerGUI.newConsignorsGUIOpen = false;
                dispose();
            }
        });
    }
}
