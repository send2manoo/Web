/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.user.model;

/**
 *
 * @author: anuja
 * @purpose: for csv
 */
public class UserCSV {
    
    private String emp_code; //added by manohar
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;
    private String mobileNo;
    private String gender;
    private String active;
    private String allowTimeout;
    private String role;
    private String designation;
    private String reportingHead;
    private String userAccuracy;
    private boolean mobileNotification;
   
    /**
     *
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * @return
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     *
     * @param emailAddress
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     */
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     *
     * @param mobileNo
     */
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    /**
     *
     * @return
     */
    public String getGender() {
        return gender;
    }

    /**
     *
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     *
     * @return
     */
    public String getRole() {
        return role;
    }

    /**
     *
     * @param role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     *
     * @return
     */
    public String getActive() {
        return active;
    }

    /**
     *
     * @param active
     */
    public void setActive(String active) {
        this.active = active;
    }

    /**
     *
     * @return
     */
    public String getDesignation() {
        return designation;
    }

    /**
     *
     * @param designation
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    /**
     * @return the reportingHead
     */
    public String getReportingHead() {
        return reportingHead;
    }

    /**
     * @param reportingHead the reportingHead to set
     */
    public void setReportingHead(String reportingHead) {
        this.reportingHead = reportingHead;
    }
    
    /**
     *
     * @return
     */
    public String getuserAccuracy() {
        return userAccuracy;
    }

    /**
     *
     * @param userAccuracy
     */
    public void setuserAccuracy(String userAccuracy) {
        this.userAccuracy = userAccuracy;
    }

    /**
     *
     * @return
     */
    public String getAllowTimeout() {
        return allowTimeout;
    }

    /**
     *
     * @param allowTimeout
     */
    public void setAllowTimeout(String allowTimeout) {
        this.allowTimeout = allowTimeout;
    }

    public String getUserAccuracy() {
        return userAccuracy;
    }

    public void setUserAccuracy(String userAccuracy) {
        this.userAccuracy = userAccuracy;
    }

    public boolean isMobileNotification() {
        return mobileNotification;
    }

    public void setMobileNotification(boolean mobileNotification) {
        this.mobileNotification = mobileNotification;
    }

    public String getEmp_code() {
        return emp_code;
    }

    public void setEmp_code(String emp_code) {
        this.emp_code = emp_code;
    }
    
}
