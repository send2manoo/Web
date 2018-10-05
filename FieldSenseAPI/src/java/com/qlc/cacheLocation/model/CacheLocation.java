/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.cacheLocation.model;

import java.sql.Timestamp;

/**
 *
 * @author awaneesh
 */
public class CacheLocation {
    
    private int id;
    private double latitude;
    private double longitude;
    //private String location;
    private String address;
    private String Short_address;
    private Timestamp createdOn;
    private int isFetchFromGoogle;
    //private Timestamp lastUsedOn;
    //private int usedCount;
    private String status;
    private String errorCode;
    private int userId;
    private int accountId;
    private int countOfGoogleSuccess;
    private int countOfGoogleUnSuccess;
    private int countOfCache;

    /**
     *
     */
    public CacheLocation() {
        this.id = 0;
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.address = "";   
        this.Short_address="";
        this.createdOn = new Timestamp(0);
        this.isFetchFromGoogle = 0;
        this.status ="0";
        this.errorCode="0";
      //  this.lastUsedOn = new Timestamp(0);
       // this.usedCount = 0;
        this.userId=0;
        this.accountId=0;
        this.countOfCache=0;
        this.countOfGoogleSuccess=0;
        this.countOfGoogleUnSuccess=0;
    }
    
    public String toString(){
        return "id:"+id+" Latitude:"+latitude+" Longitude:"+longitude+" address:"+address;
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
    public int getIsFetchFromGoogle() {
        return isFetchFromGoogle;
    }

    /**
     *
     * @param isFetchFromGoogle
     */
    public void setIsFetchFromGoogle(int isFetchFromGoogle) {
        this.isFetchFromGoogle = isFetchFromGoogle;
    }

    /*public Timestamp getLastUsedOn() {
        return lastUsedOn;
    }

    public void setLastUsedOn(Timestamp lastUsedOn) {
        this.lastUsedOn = lastUsedOn;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }*/

    /**
     *
     * @return
     */
    
    
    public String getAddress() {
        return address;
    }

    /**
     *
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    public String getShort_address() {
        return Short_address;
    }

    public void setShort_address(String Short_address) {
        this.Short_address = Short_address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getCountOfGoogleSuccess() {
        return countOfGoogleSuccess;
    }

    public void setCountOfGoogleSuccess(int countOfGoogleSuccess) {
        this.countOfGoogleSuccess = countOfGoogleSuccess;
    }

    public int getCountOfGoogleUnSuccess() {
        return countOfGoogleUnSuccess;
    }

    public void setCountOfGoogleUnSuccess(int countOfGoogleUnSuccess) {
        this.countOfGoogleUnSuccess = countOfGoogleUnSuccess;
    }

    public int getCountOfCache() {
        return countOfCache;
    }

    public void setCountOfCache(int countOfCache) {
        this.countOfCache = countOfCache;
    }

    }
