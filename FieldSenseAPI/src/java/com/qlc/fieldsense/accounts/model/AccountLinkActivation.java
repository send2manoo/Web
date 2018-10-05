/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.accounts.model;

import java.sql.Timestamp;

/**
 *
 * @author jyoti
 */
public class AccountLinkActivation {
    
    private String userReCaptcha;
    private String emailAddress;
    private String encryptedkey;
    private int status;
    private Timestamp createdOn;    
    private String mobileNo;
    private String OTP;
    private Timestamp OTP_Expiration;
    private String countrycode;
    private String mobileOnly;
    private boolean manualSendFlag;
    
    public AccountLinkActivation() {    
        this.emailAddress = "";
        this.encryptedkey = "";
        this.status = 0;
        this.createdOn = new Timestamp(0);
        this.userReCaptcha = "";
        this.mobileNo="";
        this.OTP="";
        this.OTP_Expiration=new Timestamp(0);
        this.countrycode="";
        this.mobileOnly="";
        this.manualSendFlag = false;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEncryptedkey() {
        return encryptedkey;
    }

    public void setEncryptedkey(String encryptedkey) {
        this.encryptedkey = encryptedkey;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }
   
    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public Timestamp getOTP_Expiration() {
        return OTP_Expiration;
    }

    public void setOTP_Expiration(Timestamp OTP_Expiration) {
        this.OTP_Expiration = OTP_Expiration;
    }
    public String getUserReCaptcha() {
        return userReCaptcha;
    }

    public void setUserReCaptcha(String userReCaptcha) {
        this.userReCaptcha = userReCaptcha;
    } 

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public String getMobileOnly() {
        return mobileOnly;
    }

    public void setMobileOnly(String mobileOnly) {
        this.mobileOnly = mobileOnly;
    }

    public boolean isManualSendFlag() {
        return manualSendFlag;
    }

    public void setManualSendFlag(boolean manualSendFlag) {
        this.manualSendFlag = manualSendFlag;
    }
  
 
}
