/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.usersTravelLogs.model;

import com.qlc.fieldsense.customAnnotations.TrimSpaceValidation;
import java.sql.Timestamp;
import java.util.Calendar;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author anuja
 */
public class UsersTravelLogs {

    private int id;
    @NotNull
    private int userId;
    private double latitude;
    private double langitude;
    @NotEmpty
    @Length(min = 1, max = 1000)
    @TrimSpaceValidation(message = "Please enter valid data")
    private String location;
    private Timestamp createdOn;
    private boolean isCustomerLocation;
    private String customerName;
    private double travelDistance;
    private String locationIdentifier;
    private boolean isServerFetch;
    private int sourceValue; // Used for setting punchIn and punchOut Value .PunchIn=1,PunchOut=2,ResetPunchOut=3,Timeoutstart=4,timeoutstop=5 , default=0
    private int nullDistanceCount;//to count number of null distance_travel values for user in DB
    private int versionCode;
    private String createdonString;
    private int isCustomerLocationInt;
    private int isServerFetchInt;
    private String lastKnownLocation;
    private int accountId;
    
    //added by jyoti
    private String battery_Percentage, isGPS, network_type, app_version, oS_Version, device_Name, isMockLocation;
    private int isShowIntoReports;
    private String customerId;
    // ended by jyoti
    
    /**
     *
     */
    public UsersTravelLogs() {
        this.id = 0;
        this.userId = 0;
        this.latitude = 0;
        this.langitude = 0;
        this.location = "";
        this.createdOn = new Timestamp(Calendar.getInstance().getTime().getTime());
        this.isCustomerLocation = false;
        this.customerName = "";
        this.travelDistance = 0;
        this.locationIdentifier="";
        this.isServerFetch = false;
        this.sourceValue=0;
        this.nullDistanceCount=0;
        this.versionCode=0;
        this.createdonString="";
        this.isCustomerLocationInt=0;
        this.isServerFetchInt=0;
        this.lastKnownLocation="";
        this.accountId=0;
        
        this.battery_Percentage = "";
        this.isGPS = "";
        this.network_type = "";
        this.app_version = "";
        this.oS_Version = "";
        this.device_Name = "";
        this.isMockLocation = "";
        this.isShowIntoReports = 1;
        this.customerId = "";
        
    }

    /**
     *
     * @return
     */
    public boolean getIsServerFetch() {
        return isServerFetch;
    }

    /**
     *
     * @param isServerFetch
     */
    public void setIsServerFetch(boolean isServerFetch) {
        this.isServerFetch = isServerFetch;
    }

    /**
     *
     * @return
     */
    public String getLocationIdentifier() {
        return locationIdentifier;
    }

    /**
     *
     * @param locationIdentifier
     */
    public void setLocationIdentifier(String locationIdentifier) {
        this.locationIdentifier = locationIdentifier;
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
    public double getLangitude() {
        return langitude;
    }

    /**
     *
     * @param langitude
     */
    public void setLangitude(double langitude) {
        this.langitude = langitude;
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

    /**
     *
     * @return
     */
    public boolean isIsCustomerLocation() {
        return isCustomerLocation;
    }

    /**
     *
     * @param isCustomerLocation
     */
    public void setIsCustomerLocation(boolean isCustomerLocation) {
        this.isCustomerLocation = isCustomerLocation;
    }

    /**
     *
     * @return
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     *
     * @param customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     *
     * @return
     */
    public double getTravelDistance() {
        return travelDistance;
    }

    /**
     *
     * @param travelDistance
     */
    public void setTravelDistance(double travelDistance) {
        this.travelDistance = travelDistance;
    }

    /**
     *
     * @return
     */
    public int getSourceValue() {
        return sourceValue;
    }

    /**
     *
     * @param sourceValue
     */
    public void setSourceValue(int sourceValue) {
        this.sourceValue = sourceValue;
    }

    /**
     *
     * @return
     */
    public int getNullDistanceCount() {
        return nullDistanceCount;
    }

    /**
     *
     * @param nullDistanceCount
     */
    public void setNullDistanceCount(int nullDistanceCount) {
        this.nullDistanceCount = nullDistanceCount;
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

    public String getCreatedonString() {
        return createdonString;
    }

    public void setCreatedonString(String createdonString) {
        this.createdonString = createdonString;
    }

    public int getIsCustomerLocationInt() {
        return isCustomerLocationInt;
    }

    public void setIsCustomerLocationInt(int isCustomerLocationInt) {
        this.isCustomerLocationInt = isCustomerLocationInt;
    }

    public int getIsServerFetchInt() {
        return isServerFetchInt;
    }

    public void setIsServerFetchInt(int isServerFetchInt) {
        this.isServerFetchInt = isServerFetchInt;
    }

    public String getLastKnownLocation() {
        return lastKnownLocation;
    }

    public void setLastKnownLocation(String lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getBattery_Percentage() {
        return battery_Percentage;
    }

    public void setBattery_Percentage(String battery_Percentage) {
        this.battery_Percentage = battery_Percentage;
    }

    public String getIsGPS() {
        return isGPS;
    }

    public void setIsGPS(String isGPS) {
        this.isGPS = isGPS;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getoS_Version() {
        return oS_Version;
    }

    public void setoS_Version(String oS_Version) {
        this.oS_Version = oS_Version;
    }

    public String getDevice_Name() {
        return device_Name;
    }

    public void setDevice_Name(String device_Name) {
        this.device_Name = device_Name;
    }

    public String getNetwork_type() {
        return network_type;
    }

    public void setNetwork_type(String network_type) {
        this.network_type = network_type;
    }

    public String getIsMockLocation() {
        return isMockLocation;
    }

    public void setIsMockLocation(String isMockLocation) {
        this.isMockLocation = isMockLocation;
    }

    public int getIsShowIntoReports() {
        return isShowIntoReports;
    }

    public void setIsShowIntoReports(int isShowIntoReports) {
        this.isShowIntoReports = isShowIntoReports;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
}
