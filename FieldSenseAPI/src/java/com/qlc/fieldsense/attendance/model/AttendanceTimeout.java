/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.attendance.model;

import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Date;

/**
 *
 * @author awaneesh
 */
public class AttendanceTimeout {
    
    private int id;
    private int userId;
   // private Date timeoutDate;
    private String strTimeoutDate;
    private int serialNumber;
    private Timestamp startTimeout;
    private Timestamp stopTimeout;
   // private Time intervalTime;
    private String strIntervalTime;

    private double latitude;
    private double longitude;
    private String location;
    
    private Timestamp createdOn;
    private int status;
    private String totalIntervalTime;
    private int versionCode;
    
    /**
     *
     */
    public AttendanceTimeout(){
        this.id = 0;
        this.userId = 0;
        //this.timeoutDate = new Date(0);
        this.serialNumber=0;
        this.startTimeout = new Timestamp(0);
        this.stopTimeout = new Timestamp(0);
       //this.intervalTime = new Time(0);
        this.status=0;
        this.strIntervalTime="00:00:00";
        this.strTimeoutDate="";
        this.createdOn= new Timestamp(0);
        this.totalIntervalTime="00:00:00";
        this.latitude=0.0;
        this.longitude=0.0;
        this.location="";
        this.versionCode=0;
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
    public int getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

   /* public Date getTimeoutDate() {
        return timeoutDate;
    }

    public void setTimeoutDate(Date timeoutDate) {
        this.timeoutDate = timeoutDate;
    }*/

    /**
     *
     * @return
     */
    public Timestamp getStartTimeout() {
        return startTimeout;
    }

    /**
     *
     * @param startTimeout
     */
    public void setStartTimeout(Timestamp startTimeout) {
        this.startTimeout = startTimeout;
    }

    /**
     *
     * @return
     */
    public Timestamp getStopTimeout() {
        return stopTimeout;
    }

    /**
     *
     * @param stopTimeout
     */
    public void setStopTimeout(Timestamp stopTimeout) {
        this.stopTimeout = stopTimeout;
    }

   /* public Time getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(Time intervalTime) {
        this.intervalTime = intervalTime;
    }*/

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
    public int getSerialNumber() {
        return serialNumber;
    }

    /**
     *
     * @param serialNumber
     */
    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     *
     * @return
     */
    public int getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     *
     * @return
     */
    public String getStrIntervalTime() {
        return strIntervalTime;
    }

    /**
     *
     * @param strIntervalTime
     */
    public void setStrIntervalTime(String strIntervalTime) {
        this.strIntervalTime = strIntervalTime;
    }

    /**
     *
     * @return
     */
    public String getStrTimeoutDate() {
        return strTimeoutDate;
    }

    /**
     *
     * @param strTimeoutDate
     */
    public void setStrTimeoutDate(String strTimeoutDate) {
        this.strTimeoutDate = strTimeoutDate;
    }

    /**
     *
     * @return
     */
    public String getTotalIntervalTime() {
        return totalIntervalTime;
    }

    /**
     *
     * @param totalIntervalTime
     */
    public void setTotalIntervalTime(String totalIntervalTime) {
        this.totalIntervalTime = totalIntervalTime;
    }

    /**
     *
     * @return
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     *
     * @return
     */
    public String getLocation() {
        return location;
    }

    /**
     *
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     *
     * @return
     */
    public int getVersionCode() {
        return versionCode;
    }

    /**
     *
     * @param versionCode
     */
    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

}
