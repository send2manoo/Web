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
public class Adminuser {
    private int id; 
   private String designation;
   private String emailAddress;
   private String firstName;
   private String gender;
   private String mobileNo;
   private String password;
   private int role;
   private int active;
   private int accountContactType;
   private String accountid;
    
//     private int id; 
//   private String Designation;
//   private String Email;
//   private String Username;
//   private String Gender;
//   private String Mobile_No;
//   private String Password;
//   private int role;
//   private int active;
//   private int accountContactType;
//   private String accountid;
//   
//    public Adminuser() {
//        this.id=0;
//        this.designation="";
//        this.emailAddress="";
//        this.firstName="";
//        this.gender="";
//        this.mobileNo="";
//        this.password="";
//        this.role=1;
//        this.active=1;
//        this.accountContactType=1;
//        this.accountid="";
//        
//    }
   public Adminuser() {
        this.id=0;
        this.designation="";
        this.emailAddress="";
        this.firstName="";
        this.gender="";
        this.mobileNo="";
        this.password="";
        this.role=1;
        this.active=1;
        this.accountContactType=1;
        this.accountid="";
        
    }
 
   
   
//    public String getAccountid() {
//        return accountid;
//    }
//
//    public void setAccountid(String accountid) {
//        this.accountid = accountid;
//    }
//
//  
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getDesignation() {
//        return designation;
//    }
//
//    public void setDesignation(String designation) {
//        this.designation = designation;
//    }
//
//    public String getEmailAddress() {
//        return emailAddress;
//    }
//
//    public void setEmailAddress(String emailAddress) {
//        this.emailAddress = emailAddress;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getGender() {
//        return gender;
//    }
//
//    public void setGender(String gender) {
//        this.gender = gender;
//    }
//
//    public String getMobileNo() {
//        return mobileNo;
//    }
//
//    public void setMobileNo(String mobileNo) {
//        this.mobileNo = mobileNo;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//      @Override
//    public String toString() {
//        return "User1{" + "id=" + id + ", designation=" + designation + ", emailAddress=" + emailAddress + ", firstName=" + firstName + ", gender=" + gender + ", mobileNo=" + mobileNo + ", password=" + password + '}';
//    }
//
//    public int getRole() {
//        return role;
//    }
//
//    public void setRole(int role) {
//        this.role = role;
//    }
//
//    public int getActive() {
//        return active;
//    }
//
//    public void setActive(int active) {
//        this.active = active;
//    }
//
//    public int getAccountContactType() {
//        return accountContactType;
//    }
//
//    public void setAccountContactType(int accountContactType) {
//        this.accountContactType = accountContactType;
//    }
//}
 public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
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
      @Override
    public String toString() {
        return "User1{" + "id=" + id + ", designation=" + designation + ", emailAddress=" + emailAddress + ", firstName=" + firstName + ", gender=" + gender + ", mobileNo=" + mobileNo + ", password=" + password + '}';
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getAccountContactType() {
        return accountContactType;
    }

    public void setAccountContactType(int accountContactType) {
        this.accountContactType = accountContactType;
    }
}
