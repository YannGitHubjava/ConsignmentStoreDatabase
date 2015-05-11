package com.KevinMcClean;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;

/**
 * Created by Kevin on 5/8/2015.
 */
public class SearchGUI extends ConsignmentStoreViewer{
    private JPanel searchGUIPanel;

    private JButton quitButton;
    private JButton searchButton;
    private JButton sellBasementButton;
    private JButton sellMainRoomButton;

    private JTable basementSearchTable;
    private JTable mainRoomSearchTable;

    private JTextField artistTextField;
    private JTextField titleTextField;

    private ConsignmentStoreController storeController;

    private ResultSet rsBasementSearch = null;
    private ResultSet rsMainRoomSearch = null;

    private StoreTableModel stm;

    int recordID;

    boolean recordSale;

    SearchGUI(ConsignmentStoreController csc) {
        this.storeController = csc;
        setContentPane(searchGUIPanel);
        pack();
        setVisible(true);

        //from http://stackoverflow.com/questions/4737495/disposing-and-closing-windows-in-java.
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ConsignmentStoreViewerGUI.searchRecordsGUIOpen = false;
                dispose();
            }
        });


        //exits this window.
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConsignmentStoreViewerGUI.searchRecordsGUIOpen = false;
                dispose();
            }
        });

        //searches for a record.
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //this takes the information and putt it into various search fields that produce the results for both the basement and the main room.
                String artist = artistTextField.getText();
                String title = titleTextField.getText();

                //broken out by the conditions of what fields have been filled in.
                if (!artist.isEmpty() && !title.isEmpty()){
                    artist = "%" + artist + "%";
                    title = "%" + title + "%";
                    rsBasementSearch = searchByArtistAndTitle("basementRecords", artist, title, " WHERE artist LIKE ? AND title LIKE ? ORDER BY artist, title", storeController);
                    stm = new StoreTableModel(storeController, rsBasementSearch);
                    basementSearchTable.setModel(stm);

                    rsMainRoomSearch = searchByArtistAndTitle("mainRoomRecords", artist, title, " WHERE artist LIKE ? AND title LIKE ? ORDER BY artist, title", storeController);
                    stm = new StoreTableModel(storeController, rsMainRoomSearch);
                    mainRoomSearchTable.setModel(stm);
                }
                else if(artist.isEmpty() && !title.isEmpty()){
                    title = "%" + title + "%";
                    rsBasementSearch = searchViewer("basementRecords", title, " WHERE title LIKE ? ORDER BY artist, title", storeController);
                    stm = new StoreTableModel(storeController, rsBasementSearch);
                    basementSearchTable.setModel(stm);

                    rsMainRoomSearch = searchViewer("mainRoomRecords", title, " WHERE title LIKE ? ORDER BY artist, title", storeController);
                    stm = new StoreTableModel(storeController, rsMainRoomSearch);
                    mainRoomSearchTable.setModel(stm);
                }
                else if (!artist.isEmpty() && title.isEmpty()) {
                    artist = "%" + artist + "%";
                    rsBasementSearch = searchViewer("basementRecords", artist, " WHERE artist LIKE ? ORDER BY artist, title", storeController);
                    stm = new StoreTableModel(storeController, rsBasementSearch);
                    basementSearchTable.setModel(stm);

                    rsMainRoomSearch = searchViewer("mainRoomRecords", artist, " WHERE artist LIKE ? ORDER BY artist, title", storeController);
                    stm = new StoreTableModel(storeController, rsMainRoomSearch);
                    mainRoomSearchTable.setModel(stm);
                }
                else if(artist.isEmpty() && title.isEmpty()){
                    JOptionPane.showMessageDialog(searchGUIPanel, "Please fill in at least one search criteria");
                    return;
                }
            }
        });

        //sells the selected album.
        sellBasementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = basementSearchTable.getSelectedRow();
                if(row < 0) {
                    JOptionPane.showMessageDialog(searchGUIPanel, "You must select a record to sell!");
                    return;
                }
                recordID = getID("RECORD_ID", row, basementSearchTable, rsBasementSearch);

                if (recordID == NOT_AN_INT) {
                    JOptionPane.showMessageDialog(searchGUIPanel, "Could not sell this record. Please make sure you have selected an album.");
                }
                recordSale = recordBasementSaleViewer(recordID, storeController);
                if (recordSale) {
                    JOptionPane.showMessageDialog(searchGUIPanel, "Record sold.");
                }
                else {
                    JOptionPane.showMessageDialog(searchGUIPanel, "Record not sold.");
                }
            }
        });

        //sells a record in the main room.
        sellMainRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = mainRoomSearchTable.getSelectedRow();
                if(row < 0) {
                    JOptionPane.showMessageDialog(searchGUIPanel, "You must select a record to sell!");
                    return;
                }
                recordID = getID("RECORD_ID", row, mainRoomSearchTable, rsMainRoomSearch);

                if (recordID == NOT_AN_INT) {
                    JOptionPane.showMessageDialog(searchGUIPanel, "Could not sell this record. Please make sure you have selected an album.");
                    return;
                }
                recordSale = recordMainRoomSaleViewer(recordID, storeController);
                if (recordSale) {
                    JOptionPane.showMessageDialog(searchGUIPanel, "Record sold.");
                }
                else {
                    JOptionPane.showMessageDialog(searchGUIPanel, "Record not sold.");
                }
            }
        });

    }
}
