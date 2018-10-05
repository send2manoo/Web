/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.auth.model;

import com.qlc.fieldsense.mobile.model.LeftSliderMenu;
import java.sql.Timestamp;

/**
 *
 * @author Ramesh
 * @date 30-01-2014
 * @purpose This class is model to authenticate user.
 */
public class AuthenticationUser {

    private int id;
    private int userId;
    private int accountId;
    private String userToken;
    private String userFirstName;
    private String userLastName;
    private String userEmailAddress;
    private Timestamp createdOn;
    private Timestamp modifiedOn;
    private int teamId;
    private String imsgrRetivalPath;
    private String expenseImageRetivalPath;
    private String customFormImageRetivalPath;
    private int role;   // role : 0 -super user, 1 -admin, 2 -account person, 5 -on-field user 
    private String userDesignation;
    private int gender;
    private String importErrorFileRetrivePath;
    private String mobileNo;
    private double latitude;
    private double langitude;
    private double officelatitude;
    private double officelangitude;
    private double homLatitude; // added by jyoti
    private double homeLongitude; // added by jyoti
    private boolean isFirstLogin;
    private int locationPollInterval; // Time difference in minutes in which location will be fetched

    private int userAccuracy;
    private int checkInRadius;
    private int allowTimeout;
    // Added by jyoti
    private int allowOfflineOperation; // account level
    private String currencySymbol;
    private LeftSliderMenu leftslider; // added by manohar
    private String companyName; // by nikhil
    private String accountName;//siddhesh
    // Added by jyoti
    private int allowTimoutUser; // user level
    private int allowOfflineUser; // user level
    private int locationIntervalUser;
    private int accountLocationInterval;
    private String userPassword;
    private java.util.List<java.util.HashMap> assignedTerritoryList;
    private java.util.List<java.util.HashMap> expenseCategoryList;
    private java.util.List<java.util.HashMap> purposeCategoryList;
    private java.util.List<java.util.HashMap> industryCategoryList;
    private int appVersion; // added by jyoti
    private String auto_punch_out_time;
    private int auto_punch_out_type;
    private int working_hours;
    
    //added by nikhil
    private int is_terms_condition_agreed ;
    private int is_newsletter_opt_in;
    private Timestamp newsletter_agreed_on;
    private Timestamp terms_condition_agreed_on;
    //ended by nikhil
    /**
     *
     */
    public AuthenticationUser() {
        this.id = 0;
        this.userId = 0;
        this.accountId = 0;
        this.userToken = "";
        this.userFirstName = "";
        this.userLastName = "";
        this.userEmailAddress = "";
        this.createdOn = new Timestamp(0);
        this.modifiedOn = new Timestamp(0);
        this.teamId = 0;
        this.imsgrRetivalPath = "";
        this.expenseImageRetivalPath = "";
        this.role = 0;
        this.userDesignation = "";
        this.gender = 0;
        this.importErrorFileRetrivePath = "";
        this.mobileNo = "";
        this.latitude = 0;
        this.langitude = 0;
        this.officelatitude = 0;
        this.officelangitude = 0;
        this.homLatitude = 0;
        this.homeLongitude = 0;
        this.isFirstLogin = false;
        this.locationPollInterval=5;        
        
        this.checkInRadius=500;
        this.userAccuracy=500;
        this.allowTimeout=0;
        this.allowOfflineOperation = 0;
        this.currencySymbol = "";
        this.leftslider=new LeftSliderMenu();  // added by manohar
        this.companyName="";
        this.accountName="";
        
        this.userPassword = "";
        this.allowTimoutUser = 0;
        this.allowOfflineUser = 0;
        this.locationIntervalUser = 0;
        this.accountLocationInterval = 0;
        this.assignedTerritoryList = new java.util.ArrayList<java.util.HashMap>();
        this.expenseCategoryList = new java.util.ArrayList<java.util.HashMap>();
        this.purposeCategoryList = new java.util.ArrayList<java.util.HashMap>();
        this.industryCategoryList = new java.util.ArrayList<java.util.HashMap>();
        this.appVersion = 0;
        this.auto_punch_out_time = "48";
        this.auto_punch_out_type = 0; // 0 for hour, 1 for specific time
        this.working_hours = 0;
        this.is_terms_condition_agreed = 2; //by nikhil
        this.is_newsletter_opt_in = 2;//by nikhil
        this.terms_condition_agreed_on = new Timestamp(0);
        this.newsletter_agreed_on = new Timestamp(0);
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
    public Timestamp getModifiedOn() {
        return modifiedOn;
    }

    /**
     *
     * @param modifiedOn
     */
    public void setModifiedOn(Timestamp modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    /**
     *
     * @return
     */
    public String getUserEmailAddress() {
        return userEmailAddress;
    }

    /**
     *
     * @param userEmailAddress
     */
    public void setUserEmailAddress(String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
    }

    /**
     *
     * @return
     */
    public String getUserFirstName() {
        return userFirstName;
    }

    /**
     *
     * @param userFirstName
     */
    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
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
    public String getUserLastName() {
        return userLastName;
    }

    /**
     *
     * @param userLastName
     */
    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    /**
     *
     * @return
     */
    public String getUserToken() {
        return userToken;
    }

    /**
     *
     * @param userToken
     */
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    /**
     *
     * @return
     */
    public int getTeamId() {
        return teamId;
    }

    /**
     *
     * @param teamId
     */
    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    /**
     *
     * @return
     */
    public String getImsgrRetivalPath() {
        return imsgrRetivalPath;
    }

    /**
     *
     * @param imsgrRetivalPath
     */
    public void setImsgrRetivalPath(String imsgrRetivalPath) {
        this.imsgrRetivalPath = imsgrRetivalPath;
    }

    /**
     *
     * @return
     */
    public int getRole() {
        return role;
    }

    /**
     *
     * @param role
     */
    public void setRole(int role) {
        this.role = role;
    }

    /**
     *
     * @return
     */
    public String getExpenseImageRetivalPath() {
        return expenseImageRetivalPath;
    }

    /**
     *
     * @param expenseImageRetivalPath
     */
    public void setExpenseImageRetivalPath(String expenseImageRetivalPath) {
        this.expenseImageRetivalPath = expenseImageRetivalPath;
    }

    /**
     *
     * @return
     */
    public int getGender() {
        return gender;
    }

    /**
     *
     * @param gender
     */
    public void setGender(int gender) {
        this.gender = gender;
    }

    /**
     *
     * @return
     */
    public String getUserDesignation() {
        return userDesignation;
    }

    /**
     *
     * @param userDesignation
     */
    public void setUserDesignation(String userDesignation) {
        this.userDesignation = userDesignation;
    }

    /**
     *
     * @return
     */
    public String getImportErrorFileRetrivePath() {
        return importErrorFileRetrivePath;
    }

    /**
     *
     * @param importErrorFileRetrivePath
     */
    public void setImportErrorFileRetrivePath(String importErrorFileRetrivePath) {
        this.importErrorFileRetrivePath = importErrorFileRetrivePath;
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
    public double getOfficelangitude() {
        return officelangitude;
    }

    /**
     *
     * @param officelangitude
     */
    public void setOfficelangitude(double officelangitude) {
        this.officelangitude = officelangitude;
    }

    /**
     *
     * @return
     */
    public double getOfficelatitude() {
        return officelatitude;
    }

    /**
     *
     * @param officelatitude
     */
    public void setOfficelatitude(double officelatitude) {
        this.officelatitude = officelatitude;
    }

    /**
     *
     * @return
     */
    public boolean isIsFirstLogin() {
        return isFirstLogin;
    }

    /**
     *
     * @param isFirstLogin
     */
    public void setIsFirstLogin(boolean isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }
    
    /**
     *
     * @return
     */
    public int getLocationPollInterval() {
        return locationPollInterval;
    }

    /**
     *
     * @param locationPollInterval
     */
    public void setLocationPollInterval(int locationPollInterval) {
        this.locationPollInterval = locationPollInterval;
    }
    
    /**
     *
     * @return
     */
    public int getAllowTimeout() {
        return allowTimeout;
    }

    /**
     *
     * @param allowTimeout
     */
    public void setAllowTimeout(int allowTimeout) {
        this.allowTimeout = allowTimeout;
    }

    /**
     *
     * @return
     */
    public int getUserAccuracy() {
        return userAccuracy;
    }

    /**
     *
     * @param userAccuracy
     */
    public void setUserAccuracy(int userAccuracy) {
        this.userAccuracy = userAccuracy;
    }

    /**
     *
     * @return
     */
    public int getCheckInRadius() {
        return checkInRadius;
    }

    /**
     *
     * @param checkInRadius
     */
    public void setCheckInRadius(int checkInRadius) {
        this.checkInRadius = checkInRadius;
    }

    /**
     *
     * @return
     */
    public String getCustomFormImageRetivalPath() {
        return customFormImageRetivalPath;
    }

    /**
     *
     * @param customFormImageRetivalPath
     */
    public void setCustomFormImageRetivalPath(String customFormImageRetivalPath) {
        this.customFormImageRetivalPath = customFormImageRetivalPath;
    }

    /**
     *
     * @return
     */
    public int getAllowOfflineOperation() {
        return allowOfflineOperation;
    }

    /**
     *
     * @param allowOfflineOperation
     */
    public void setAllowOfflineOperation(int allowOfflineOperation) {
        this.allowOfflineOperation = allowOfflineOperation;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public LeftSliderMenu getLeftslider() {
        return leftslider;
    }

    public void setLeftslider(LeftSliderMenu leftslider) {
        this.leftslider = leftslider;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getAllowTimoutUser() {
        return allowTimoutUser;
    }

    public void setAllowTimoutUser(int allowTimoutUser) {
        this.allowTimoutUser = allowTimoutUser;
    }

    public int getAllowOfflineUser() {
        return allowOfflineUser;
    }

    public void setAllowOfflineUser(int allowOfflineUser) {
        this.allowOfflineUser = allowOfflineUser;
    }

    public int getLocationIntervalUser() {
        return locationIntervalUser;
    }

    public void setLocationIntervalUser(int locationIntervalUser) {
        this.locationIntervalUser = locationIntervalUser;
    }

    public int getAccountLocationInterval() {
        return accountLocationInterval;
    }

    public void setAccountLocationInterval(int accountLocationInterval) {
        this.accountLocationInterval = accountLocationInterval;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public java.util.List<java.util.HashMap> getAssignedTerritoryList() {
        return assignedTerritoryList;
    }

    public void setAssignedTerritoryList(java.util.List<java.util.HashMap> assignedTerritoryList) {
        this.assignedTerritoryList = assignedTerritoryList;
    }

    public java.util.List<java.util.HashMap> getExpenseCategoryList() {
        return expenseCategoryList;
    }

    public void setExpenseCategoryList(java.util.List<java.util.HashMap> expenseCategoryList) {
        this.expenseCategoryList = expenseCategoryList;
    }

    public java.util.List<java.util.HashMap> getPurposeCategoryList() {
        return purposeCategoryList;
    }

    public void setPurposeCategoryList(java.util.List<java.util.HashMap> purposeCategoryList) {
        this.purposeCategoryList = purposeCategoryList;
    }

    public java.util.List<java.util.HashMap> getIndustryCategoryList() {
        return industryCategoryList;
    }

    public void setIndustryCategoryList(java.util.List<java.util.HashMap> industryCategoryList) {
        this.industryCategoryList = industryCategoryList;
    }

    public int getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(int appVersion) {
        this.appVersion = appVersion;
    }

    public double getHomLatitude() {
        return homLatitude;
    }

    public void setHomLatitude(double homLatitude) {
        this.homLatitude = homLatitude;
    }

    public double getHomeLongitude() {
        return homeLongitude;
    }

    public void setHomeLongitude(double homeLongitude) {
        this.homeLongitude = homeLongitude;
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

    public int getIs_terms_condition_agreed() {
        return is_terms_condition_agreed;
    }

    public void setIs_terms_condition_agreed(int is_terms_condition_agreed) {
        this.is_terms_condition_agreed = is_terms_condition_agreed;
    }

    public int getIs_newsletter_opt_in() {
        return is_newsletter_opt_in;
    }

    public void setIs_newsletter_opt_in(int is_newsletter_opt_in) {
        this.is_newsletter_opt_in = is_newsletter_opt_in;
    }

    public Timestamp getNewsletter_agreed_on() {
        return newsletter_agreed_on;
    }

    public void setNewsletter_agreed_on(Timestamp newsletter_agreed_on) {
        this.newsletter_agreed_on = newsletter_agreed_on;
    }

    public Timestamp getTerms_condition_agreed_on() {
        return terms_condition_agreed_on;
    }

    public void setTerms_condition_agreed_on(Timestamp terms_condition_agreed_on) {
        this.terms_condition_agreed_on = terms_condition_agreed_on;
    }
 
}
