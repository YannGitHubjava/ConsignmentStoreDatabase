package com.KevinMcClean;

import java.util.Date;

/**
 * Created by Kevin on 4/26/2015.
 */
public class ConsignmentStoreStarter {
    public static void main(String[] args) {
        /*CreateDatabase cdb = new CreateDatabase();
        cdb.setupDatabase();*/
        ConsignmentStoreController csc = new ConsignmentStoreController();
        ConsignmentStoreViewer viewer = new ConsignmentStoreViewer(csc);
    }
}
