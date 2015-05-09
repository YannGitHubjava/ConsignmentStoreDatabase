package com.KevinMcClean;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * Created by Kevin on 5/8/2015.
 */
public class SearchGUI extends ConsignmentStoreViewer{
    private JPanel searchGUIPanel;

    private JButton quitButton;
    private JButton searchButton;
    private JButton sellButton;

    private JTable basementSearchTable;
    private JTable mainRoomSearchTable;

    private JTextField artistTextField;
    private JTextField titleTextField;

    private ConsignmentStoreController storeController;

    private ResultSet rsBasementSearch = null;
    private ResultSet rsMainRoomSearch = null;

    private StoreTableModel stm;

    SearchGUI(ConsignmentStoreController csc){
        this.storeController = csc;
        setContentPane(searchGUIPanel);
        pack();
        setVisible(true);

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConsignmentStoreViewerGUI.searchRecordsGUIOpen = false;
                dispose();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String artist = artistTextField.getText();
                String title = titleTextField.getText();
                if (!artist.isEmpty() && !title.isEmpty()){
                    rsBasementSearch = searchByArtistAndTitleForBasementViewer(artist, title, storeController);
                    stm = new StoreTableModel(storeController, rsBasementSearch);
                    basementSearchTable.setModel(stm);

                    rsMainRoomSearch = searchByArtistAndTitleForMainRoomViewer(artist, title, storeController);
                    stm = new StoreTableModel(storeController, rsMainRoomSearch);
                    mainRoomSearchTable.setModel(stm);
                }
                else if(artist.isEmpty() && !title.isEmpty()){
                    rsBasementSearch = searchByTitleForBasementViewer(title, storeController);
                    stm = new StoreTableModel(storeController, rsBasementSearch);
                    basementSearchTable.setModel(stm);

                    rsMainRoomSearch = searchByTitleForMainRoomViewer(title, storeController);
                    stm = new StoreTableModel(storeController, rsMainRoomSearch);
                    mainRoomSearchTable.setModel(stm);
                }
                else if (!artist.isEmpty() && title.isEmpty()) {
                    rsBasementSearch = searchByArtistForBasementViewer(artist, storeController);
                    stm = new StoreTableModel(storeController, rsBasementSearch);
                    basementSearchTable.setModel(stm);

                    rsMainRoomSearch = searchByArtistForMainRoomViewer(artist, storeController);
                    stm = new StoreTableModel(storeController, rsMainRoomSearch);
                    mainRoomSearchTable.setModel(stm);
                }
                else if(artist.isEmpty() && title.isEmpty()){
                    JOptionPane.showMessageDialog(searchGUIPanel, "Please fill in at least one search criteria");
                    return;
                }
            }
        });

        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

    }
}
