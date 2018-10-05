/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.accounts.model;

/**
 *
 * @author nikhil
 */
public class User1 {
   private int id; 
   private String designation;
   private String emailAddress;
   private String firstName;
   private String gender;
   private String mobileNo;
   private String password;
   private String accountid;
   
    public User1() {
        this.id=0;
        this.designation="";
        this.emailAddress="";
        this.firstName="";
        this.gender="";
        this.mobileNo="";
        this.password="";
        this.accountid="";
        
    }

  

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
      
    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    @Override
    public String toString() {
        return "User1{" + "id=" + id + ", designation=" + designation + ", emailAddress=" + emailAddress + ", firstName=" + firstName + ", gender=" + gender + ", mobileNo=" + mobileNo + ", password=" + password + ", accountid=" + accountid + '}';
    }
    
}
