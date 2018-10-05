/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.accounts.model;

import java.sql.Timestamp;

/**
 *
 * @author awaneesh
 */
public class AccountSetting {
    private int id;
    private String settingName;
    private String settingValue;
    private Timestamp updatedOn;
    private int updatedBy;
    private boolean allowTimeoutForAll;
    
    //nikhil
    private String locationInterval;

    public String getLocationInterval() {
        return locationInterval;
    }

    public void setLocationInterval(String locationInterval) {
        this.locationInterval = locationInterval;
    }
     
   // private boolean allowOfflineForAll;
     
    /**
     *
     */
         
    public AccountSetting(){
        this.id=0;
        this.settingName="";
        this.settingValue="";
        this.updatedOn= new Timestamp(0);
        this.updatedBy=0;
        this.allowTimeoutForAll=false;
        this.locationInterval="";
        
    //    this.allowOfflineForAll=true;
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
    public String getSettingName() {
        return settingName;
    }

    /**
     *
     * @param settingName
     */
    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    /**
     *
     * @return
     */
    public String getSettingValue() {
        return settingValue;
    }

    /**
     *
     * @param settingValue
     */
    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }

    /**
     *
     * @return
     */
    public Timestamp getUpdatedOn() {
        return updatedOn;
    }

    /**
     *
     * @param updatedOn
     */
    public void setUpdatedOn(Timestamp updatedOn) {
        this.updatedOn = updatedOn;
    }

    /**
     *
     * @return
     */
    public int getUpdatedBy() {
        return updatedBy;
    }

    /**
     *
     * @param updatedBy
     */
    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     *
     * @return
     */
    public boolean isAllowTimeoutForAll() {
        return allowTimeoutForAll;
    }

    /**
     *
     * @param allowTimeoutForAll
     */
    public void setAllowTimeoutForAll(boolean allowTimeoutForAll) {
        this.allowTimeoutForAll = allowTimeoutForAll;
    }

//    public boolean isAllowOfflineForAll() {
//        return allowOfflineForAll;
//    }
//
//    public void setAllowOfflineForAll(boolean allowOfflineForAll) {
//        this.allowOfflineForAll = allowOfflineForAll;
//    }
    
}
