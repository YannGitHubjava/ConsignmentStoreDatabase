package com.KevinMcClean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Kevin on 4/21/2015.
 */
//this creates the ConsignmentStoreViewerGUI. This is the main screen, which will allow the user to navigate to all the different subscreens that are available.
public class ConsignmentStoreViewerGUI extends ConsignmentStoreViewer{
    private JPanel consignmentStoreViewerGUIPanel;

    private JButton buyButton;
    private JButton consignorsButton;
    private JButton quitButton;
    private JButton recordsInBasementButton;
    private JButton recordsInStoreButton;
    private JButton recordsGivenToCharityButton;
    private JButton recordsReturnedToConsignorButton;
    private JButton soldRecordsButton;
    private JButton updateRecordsButton;
    private JButton salesButton;
    private JButton searchButton;
    private JButton newConsignorsButton;
    private JButton owedToConsignorsButton;


    private ConsignmentStoreController storeController;

    protected static boolean basementRecordsOpen = false;
    protected static boolean buyGUIOpen = false;
    protected static boolean charityGUIOpen = false;
    protected static boolean consignorsGUIOpen = false;
    protected static boolean mainRoomRecordsOpen = false;
    protected static boolean newConsignorsGUIOpen = false;
    protected static boolean returnedRecordsGUIOpen = false;
    protected static boolean searchRecordsGUIOpen = false;
    protected static boolean soldRecordsGUIOpen = false;
    protected static boolean updateRecordsGUIOpen = false;
    protected static boolean salesGUIOpen = false;
    protected static boolean consignorsOwedGUIOpen = false;



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

        //from http://stackoverflow.com/questions/4737495/disposing-and-closing-windows-in-java.
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cleanupViewer();
                System.exit(0);
            }
        });

        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!buyGUIOpen) {
                    buyGUIOpen = true;
                    BuyGUI buyGUI = new BuyGUI(storeController);
                }
            }
        });
        consignorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!consignorsGUIOpen) {
                    consignorsGUIOpen = true;
                    ConsignorsGUI consignorsGUI = new ConsignorsGUI(storeController);
                }
            }
        });

        newConsignorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!newConsignorsGUIOpen){
                    newConsignorsGUIOpen = true;
                    NewConsignorsGUI ncgui = new NewConsignorsGUI(storeController);
                }
            }
        });

        owedToConsignorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!consignorsOwedGUIOpen){
                    consignorsOwedGUIOpen = true;
                    OwedToConsignorsGUI ootcg = new OwedToConsignorsGUI(storeController);
                }
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
        recordsGivenToCharityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!charityGUIOpen) {
                    charityGUIOpen = true;
                    CharityRecordsGUI crg = new CharityRecordsGUI(storeController);
                }
            }
        });
        recordsInBasementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!basementRecordsOpen) {
                    basementRecordsOpen = true;
                    BasementRecordsGUI brg = new BasementRecordsGUI(storeController);
                }
            }
        });
        recordsInStoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!mainRoomRecordsOpen) {
                    mainRoomRecordsOpen = true;
                    RecordsInMainRoomGUI recordsInStoreGUI = new RecordsInMainRoomGUI(storeController);
                }
            }
        });
        recordsReturnedToConsignorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!returnedRecordsGUIOpen) {
                    returnedRecordsGUIOpen = true;
                    ReturnToConsignorGUI rtcgui = new ReturnToConsignorGUI(storeController);
                }
            }
        });

        salesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!salesGUIOpen){
                    salesGUIOpen = true;
                    SalesGUI salesGUI = new SalesGUI(storeController);
                }
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!searchRecordsGUIOpen) {
                    searchRecordsGUIOpen = true;
                    SearchGUI sgui = new SearchGUI(storeController);
                }
            }
        });

        soldRecordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!soldRecordsGUIOpen){
                    soldRecordsGUIOpen = true;
                    SoldRecordsGUI srgui = new SoldRecordsGUI(storeController);
                }
            }
        });

        updateRecordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!updateRecordsGUIOpen) {
                    updateRecordsGUIOpen = true;
                    UpdateRecordsGUI updateRecordsGUI = new UpdateRecordsGUI(storeController);
                }
            }
        });


    }
}
