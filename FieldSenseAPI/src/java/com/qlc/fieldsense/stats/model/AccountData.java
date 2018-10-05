/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.stats.model;

import java.sql.Timestamp;
import java.sql.Date;
/**
 *
 * @author Ramesh
 * @date 28-04-2015
 */
public class AccountData {

    private int id;
    private String accountName;
    private String plan;
    private int usersCount;
    private int usersLimit;
    private boolean isActive;
    private int totalAccounts;
    private Timestamp expiryDate;
    private int activityFrequency ;
    private Timestamp createdOn;
    //nikhil
    private Timestamp startdate;
    private Timestamp lastPunchInDateOfanAccount;
    private String regionName;
 
    /**
     *
     */
    public AccountData() {
        this.id = 0;
        this.accountName = "";
        this.plan = "";
        this.usersCount = 0;
        this.usersLimit = 0;
        this.isActive = true;
        this.totalAccounts = 0;
        this.activityFrequency=0;
        this.expiryDate = new Timestamp(0);
        this.createdOn = new Timestamp(0);
        this.startdate = new Timestamp(0);
        this.lastPunchInDateOfanAccount = new Timestamp(0);
        this.regionName="";
    }

    public Timestamp getStartdate() {
        return startdate;
    }

    public void setStartdate(Timestamp startdate) {
        this.startdate = startdate;
    }

    public Timestamp getLastPunchInDateOfanAccount() {
        return lastPunchInDateOfanAccount;
    }

    public void setLastPunchInDateOfanAccount(Timestamp lastPunchInDateOfanAccount) {
        this.lastPunchInDateOfanAccount = lastPunchInDateOfanAccount;
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
    public boolean isIsActive() {
        return isActive;
    }

    /**
     *
     * @param isActive
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     *
     * @return
     */
    public int getTotalAccounts() {
        return totalAccounts;
    }

    /**
     *
     * @param totalAccounts
     */
    public void setTotalAccounts(int totalAccounts) {
        this.totalAccounts = totalAccounts;
    }

    /**
     *
     * @return
     */
    public int getUsersCount() {
        return usersCount;
    }

    /**
     *
     * @param usersCount
     */
    public void setUsersCount(int usersCount) {
        this.usersCount = usersCount;
    }

    /**
     *
     * @return
     */
    public Timestamp getlastPunchInDateOfanAccount() {
        return lastPunchInDateOfanAccount;
    }

    /**
     *
     * @param lastPunchInDateOfanAccount
     */
    public void setlastPunchInDateOfanAccount(Timestamp lastPunchInDateOfanAccount) {
        this.lastPunchInDateOfanAccount = lastPunchInDateOfanAccount;
    }

    /**
     * @return the usersLimit
     */
    public int getUsersLimit() {
        return usersLimit;
    }

    /**
     * @param usersLimit the usersLimit to set
     */
    public void setUsersLimit(int usersLimit) {
        this.usersLimit = usersLimit;
    }

    /**
     * @return the ActivityFrequency
     */
    public int getActivityFrequency() {
        return activityFrequency;
    }

    /**
     * @param activityFrequency
     * @param ActivityFrequency the ActivityFrequency to set
     */
    public void setActivityFrequency(int activityFrequency) {
        this.activityFrequency = activityFrequency;
    }

    /**
     * @return the expiryDate
     */
    public Timestamp getExpiryDate() {
        return expiryDate;
    }

    /**
     * @param expiryDate the expiryDate to set
     */
    public void setExpiryDate(Timestamp expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     *
     * @return
     */
    public String getRegionName() {
        return regionName;
    }

    /**
     *
     * @param regionName
     */
    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }
}
