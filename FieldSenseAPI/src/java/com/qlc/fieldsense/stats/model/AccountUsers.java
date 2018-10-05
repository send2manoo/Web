/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.stats.model;

import java.sql.Timestamp;

/**
 *
 * @author siddhesh
 * created for new super user panel 29-06-17
 */
public class AccountUsers {
    
    private String userName;
    private int active;
    private String accountName;
    private String emailId;
    private Timestamp createdDate;
    private String region;
    private int checkId;
    private String mobileNo;
    
    
  public AccountUsers(){
  this.userName="";
  this.active=1;
  this.accountName="";
  this.emailId="";
  this.createdDate=new Timestamp(0);
  this.region="";
  this.checkId=1;
  this.mobileNo=""; 
  }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getCheckId() {
        return checkId;
    }

    public void setCheckId(int checkId) {
        this.checkId = checkId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
    
}
