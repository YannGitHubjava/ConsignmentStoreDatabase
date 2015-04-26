package com.KevinMcClean;

import java.math.BigDecimal;

/**
 * Created by Kevin on 4/21/2015.
 */
public class Consignor {
    private int consignorID;
    private String consignorFirstName;
    private String consignorLastName;
    private String consignorAddress;
    private String consignorCity;
    private String consignorState;
    private BigDecimal consignorPhoneNumber;
    private Double consignorTotalPaid;

    Consignor(int id, String cfn, String cln, String ca, String cc, String cs, BigDecimal cpn, Double ctp){
        this.consignorID = id;
        this.consignorFirstName = cfn;
        this.consignorLastName = cln;
        this.consignorAddress = ca;
        this.consignorCity = cc;
        this.consignorState = cs;
        this.consignorPhoneNumber = cpn;
        this.consignorTotalPaid = ctp;
    }

    public String toString(){
        String phoneNumberString = phoneNumberConversion();
        String consignorString = "ID: " + this.consignorID + "\nName: " + this.consignorFirstName + " " + this.consignorLastName + "\nAddress: " +
                this.consignorAddress + " " + this.consignorCity + ", " + this.consignorState + "\nPhone Number: " + phoneNumberString + "\nTotal Paid " + consignorTotalPaid;
        return consignorString;
    }

    public String phoneNumberConversion(){
        String phoneString = this.consignorPhoneNumber.toString();
        String returnString = "";
        for(int i = 0; i < phoneString.length(); i++){
            if (i == 3 || i == 6){
                returnString = returnString + "-";
            }
            returnString = returnString + phoneString.charAt(i);
        }
        return returnString;
    }
}
