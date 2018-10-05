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
public class AccountSettingValues {
    private boolean allowTimeout;
    private boolean allowTimeoutForAll;
    private String currencySymbol;
    private boolean allowOffline;
    private String interval;
    private String auto_punch_out_time;
    private int auto_punch_out_type;
    private int working_hours;
    private String timeZone;

    public AccountSettingValues(){
        this.allowTimeout=false;
        this.allowTimeoutForAll=false;
        this.allowOffline=true;
        this.interval ="";
        this.currencySymbol = "";
       // this.allowOfflineForAll=true;
        this.auto_punch_out_time = "48";
        this.auto_punch_out_type = 0;
        this.working_hours = 0;
        this.timeZone="India Standard Time (GMT+05:30)";
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

    /**
     *
     * @return
     */
    public boolean isAllowTimeout() {
        return allowTimeout;
    }

    /**
     *
     * @param allowTimeout
     */
    public void setAllowTimeout(boolean allowTimeout) {
        this.allowTimeout = allowTimeout;
    }

    /**
     *
     * @return
     */
    public boolean isAllowOffline() {
        return allowOffline;
    }

    /**
     *
     * @param allowOffline
     */
    public void setAllowOffline(boolean allowOffline) {
        this.allowOffline = allowOffline;
    }
     
      //added by nikhil
    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getAuto_punch_out_time() {
        return auto_punch_out_time;
    }

    public void setAuto_punch_out_time(String auto_punch_out_time) {
        this.auto_punch_out_time = auto_punch_out_time;
    }

    public int getAuto_punch_out_type() {
        return auto_punch_out_type;
    }

    public void setAuto_punch_out_type(int auto_punch_out_type) {
        this.auto_punch_out_type = auto_punch_out_type;
    }

    public int getWorking_hours() {
        return working_hours;
    }

    public void setWorking_hours(int working_hours) {
        this.working_hours = working_hours;
    }

}
