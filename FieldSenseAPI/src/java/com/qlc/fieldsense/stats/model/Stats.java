/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.stats.model;

import java.sql.Timestamp;

/**
 *
 * @author Ramesh
 * @date 16-04-2015
 */
public class Stats {
    
    private String full_name;
    private String email_id;
    private int id;
    private int statDay;
    private int statWeek;
    private int statMonth;
    private int statYear;
    private int accountId;
    private String accountName;
    private int userCount;
    private int customerCount;
    private int contactCount;
    private int activityCount;
    private int messageCount;
    private int expenseCount;
    private int attendanceUsersCount;
    private Timestamp createdOn;
    private int statsCount;
    private int debugStatus;    //Added by manohar
    private int travelStatus; 
    private String startDate;
    private String endDate;
    private  String file;        //added by manohar
    private  String file_Name;
    /**
     *
     */
    public Stats() {
        this.id = 0;
        this.statDay = 0;
        this.statWeek = 0;
        this.statMonth = 0;
        this.statYear = 0;
        this.accountId = 0;
        this.accountName = "";
        this.userCount = 0;
        this.customerCount = 0;
        this.contactCount = 0;
        this.activityCount = 0;
        this.messageCount = 0;
        this.expenseCount = 0;
        this.attendanceUsersCount = 0;
        this.createdOn = new Timestamp(0);
        this.statsCount = 0;
        this.full_name ="";
        this.email_id ="";
        this.debugStatus=0; 
        this.travelStatus=0;
        this.startDate="";  
        this.endDate="";
        this.file="";              
        this.file_Name=""; 
        
    }

    /**
     *
     * @return
     */
    public int getAccountId() {
        return accountId;
    }

    /**
     *
     * @param accountId
     */
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    /**
     *
     * @return
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     *
     * @param accountName
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     *
     * @return
     */
    public int getActivityCount() {
        return activityCount;
    }

    /**
     *
     * @param activityCount
     */
    public void setActivityCount(int activityCount) {
        this.activityCount = activityCount;
    }

    /**
     *
     * @return
     */
    public int getAttendanceUsersCount() {
        return attendanceUsersCount;
    }

    /**
     *
     * @param attendanceUsersCount
     */
    public void setAttendanceUsersCount(int attendanceUsersCount) {
        this.attendanceUsersCount = attendanceUsersCount;
    }

    /**
     *
     * @return
     */
    public int getContactCount() {
        return contactCount;
    }

    /**
     *
     * @param contactCount
     */
    public void setContactCount(int contactCount) {
        this.contactCount = contactCount;
    }

    /**
     *
     * @return
     */
    public int getCustomerCount() {
        return customerCount;
    }

    /**
     *
     * @param customerCount
     */
    public void setCustomerCount(int customerCount) {
        this.customerCount = customerCount;
    }

    /**
     *
     * @return
     */
    public int getExpenseCount() {
        return expenseCount;
    }

    /**
     *
     * @param expenseCount
     */
    public void setExpenseCount(int expenseCount) {
        this.expenseCount = expenseCount;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public int getMessageCount() {
        return messageCount;
    }

    /**
     *
     * @param messageCount
     */
    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    /**
     *
     * @return
     */
    public int getStatDay() {
        return statDay;
    }

    /**
     *
     * @param statDay
     */
    public void setStatDay(int statDay) {
        this.statDay = statDay;
    }

    /**
     *
     * @return
     */
    public int getStatMonth() {
        return statMonth;
    }

    /**
     *
     * @param statMonth
     */
    public void setStatMonth(int statMonth) {
        this.statMonth = statMonth;
    }

    /**
     *
     * @return
     */
    public int getStatWeek() {
        return statWeek;
    }

    /**
     *
     * @param statWeek
     */
    public void setStatWeek(int statWeek) {
        this.statWeek = statWeek;
    }

    /**
     *
     * @return
     */
    public int getStatYear() {
        return statYear;
    }

    /**
     *
     * @param statYear
     */
    public void setStatYear(int statYear) {
        this.statYear = statYear;
    }

    /**
     *
     * @return
     */
    public int getUserCount() {
        return userCount;
    }

    /**
     *
     * @param userCount
     */
    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    /**
     *
     * @return
     */
    public Timestamp getCreatedOn() {
        return createdOn;
    }

    /**
     *
     * @param createdOn
     */
    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    /**
     *
     * @return
     */
    public int getStatsCount() {
        return statsCount;
    }

    /**
     *
     * @param statsCount
     */
    public void setStatsCount(int statsCount) {
        this.statsCount = statsCount;
    }

    public String getFirst_name() {
        return full_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public void setFirst_name(String first_name) {
        this.full_name = first_name;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public int getDebugStatus() {
        return debugStatus;
    }

    public void setDebugStatus(int debugStatus) {
        this.debugStatus = debugStatus;
    }

    public int getTravelStatus() {
        return travelStatus;
    }

    public void setTravelStatus(int travelStatus) {
        this.travelStatus = travelStatus;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFile_Name() {
        return file_Name;
    }

    public void setFile_Name(String file_Name) {
        this.file_Name = file_Name;
    }
    
}
