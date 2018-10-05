/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.user.model;

import com.qlc.fieldsense.customAnnotations.ValidPassword;
import com.qlc.fieldsense.location.model.Location;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
/**
 *
 * @author manohar
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SuperUser {
    
      private int versionCode;
    private int id;
    private int accountId;
    @NotEmpty
    @Length(max = 50)
    private String firstName;
    private String lastName;
    private String fullname;
    @NotEmpty
    private String emailAddress;
    @NotEmpty
    @Length(min = 8, max = 20)
    @ValidPassword(message = "Password should contains alphabets numbers and !@#$. ")
    private String password;
    private String mobileNo;
    private int gender;
    private int role;
    private int parentId;
    private boolean active;
    private Timestamp lastLoggedOn;
    private Timestamp lastKnownLocationTime;
    private String lastKnownLocation;
    
    private Timestamp createdOn;
    private int createdBy;
    private double latitude;
    private double langitude;
    private int type;
    private String designation;
    private Location homeLocation;
    private Location officeLocation;
    private int usersCount;
    private String punchStatus;
    private String punchStatusTime;
    //New Params added
    private boolean isCustomerLocation;
    private String customerName;
    private String locationIdentifier;
    /**
     *  @  accountContactType : 0 - not primary contact , 1 - primary contact
     **/
    private int accountContactType;
    private boolean emailNotification;
    private boolean mobileNotification;

    //added by shivakrishna for newly added column in users table
    private int report_to;
       
    //added by shivakrishna for newly added columns in users table
    private Timestamp updatedOn;
    private int updatedBy;
    
    private int userAccuracy;
    private int checkInRadius;
    
    private int allowTimeout;
    private int allowTimeoutForAccount;
    
    // added by jyoti 21-12-2016, used for offline feature
    private int allowOffline;
    //private int allowOfflineForAccount;
     
    private double travelDistance;
    private int sourceValue; // Used for setting punchIn and punchOut Value .PunchIn=1,PunchOut=2,ResetPunchOut=3,Timeoutstart=4,timeoutstop=5 , default=0
        //added by shivakrishna to check version  coming from mobile app
   

    private String punchDate;
    private int attendanceId;
    
    private List<Integer> addTerritoryList;
    private List<Integer> deleteTerritoryList;
    
     public SuperUser() {
        this.versionCode=0;
        this.id = 0;
        this.accountId = 0;
        this.firstName = "";
        this.lastName = "";
        this.fullname="";
        this.emailAddress = "";
        this.password = "";
        this.mobileNo = "";
        this.gender = 0;
        this.role = 0; 
        this.active = false;
        this.lastLoggedOn = new Timestamp(0);
        this.lastKnownLocationTime = new Timestamp(0);
        this.lastKnownLocation = "";
        this.createdOn = new Timestamp(Calendar.getInstance().getTime().getTime());
        this.createdBy = 0;
        this.latitude = 0;
        this.langitude = 0;
        this.type = 0;
        this.designation = "";
        this.homeLocation = new Location();
        this.officeLocation = new Location();
        this.usersCount = 0;
        this.accountContactType = 1;
        this.emailNotification = false;
        this.mobileNotification = false;
        this.punchStatus="";
        this.punchStatusTime="";
        this.parentId=100000;
        this.isCustomerLocation=false;
        this.customerName="";
        this.locationIdentifier="";
        this.report_to=0;
        this.versionCode=0;
        this.updatedOn = new Timestamp(Calendar.getInstance().getTime().getTime());
        this.updatedBy = 0;
        
        this.userAccuracy = 500; //Default value is 500
        this.checkInRadius = 500; //Default value is 500
        this.allowTimeout=0;
        this.allowTimeoutForAccount=0;
        
        this.allowOffline=1;
      //  this.allowOfflineForAccount=0;
        
        this.sourceValue=0;
        
        
        this.punchDate="";
        this.attendanceId=0;
        this.addTerritoryList=new ArrayList<Integer>();
        this.deleteTerritoryList=new ArrayList<Integer>();
        
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
    public boolean isIsCustomerLocation() {
        return isCustomerLocation;
    }

    public void setIsCustomerLocation(boolean isCustomerLocation) {
        this.isCustomerLocation = isCustomerLocation;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getLocationIdentifier() {
        return locationIdentifier;
    }

    public void setLocationIdentifier(String locationIdentifier) {
        this.locationIdentifier = locationIdentifier;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        // TODO :Need to change this logic of formatting time
        if( createdOn !=null){
                this.createdOn = createdOn;
        }
   
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

    
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    
    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLangitude() {
        return langitude;
    }

    public void setLangitude(double langitude) {
        this.langitude = langitude;
    }

    public String getLastKnownLocation() {
        return lastKnownLocation;
    }

    public void setLastKnownLocation(String lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
    }

    public Timestamp getLastKnownLocationTime() {
        return lastKnownLocationTime;
    }

    public void setLastKnownLocationTime(Timestamp lastKnownLocationTime) {
        this.lastKnownLocationTime = lastKnownLocationTime;
    }

    public Timestamp getLastLoggedOn() {
        return lastLoggedOn;
    }

    public void setLastLoggedOn(Timestamp lastLoggedOn) {
        this.lastLoggedOn = lastLoggedOn;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
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

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Location getHomeLocation() {
        return homeLocation;
    }

    public void setHomeLocation(Location homeLocation) {
        this.homeLocation = homeLocation;
    }

    public Location getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(Location officeLocation) {
        this.officeLocation = officeLocation;
    }

    public int getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(int usersCount) {
        this.usersCount = usersCount;
    }

    public int getAccountContactType() {
        return accountContactType;
    }

    public void setAccountContactType(int accountContactType) {
        this.accountContactType = accountContactType;
    }

    public boolean isEmailNotification() {
        return emailNotification;
    }

    public void setEmailNotification(boolean emailNotification) {
        this.emailNotification = emailNotification;
    }

    public boolean isMobileNotification() {
        return mobileNotification;
    }

    public void setMobileNotification(boolean mobileNotification) {
        this.mobileNotification = mobileNotification;
    }

    public String getPunchStatus() {
        return punchStatus;
    }

    public void setPunchStatus(String punchStatus) {
        this.punchStatus = punchStatus;
    }

    public String getPunchStatusTime() {
        return punchStatusTime;
    }

    public void setPunchStatusTime(String punchStatusTime) {
        this.punchStatusTime = punchStatusTime;
    }

    /**
     * @return the parentId
     */
    public int getParentId() {
        return parentId;
    }

    /**
     * @param parentId the parentId to set
     */
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getReport_to() {
        return report_to;
    }

    public void setReport_to(int report_to) {
        this.report_to = report_to;
    }
    
    
    public Timestamp getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Timestamp updatedOn) {
        this.updatedOn = updatedOn;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public int getUserAccuracy() {
        return userAccuracy;
    }

    public void setUserAccuracy(int userAccuracy) {
        this.userAccuracy = userAccuracy;
    }

    public int getCheckInRadius() {
        return checkInRadius;
    }

    public void setCheckInRadius(int checkInRadius) {
        this.checkInRadius = checkInRadius;
    }

    public int getAllowTimeout() {
        return allowTimeout;
    }

    public void setAllowTimeout(int allowTimeout) {
        this.allowTimeout = allowTimeout;
    }

    public int getAllowTimeoutForAccount() {
        return allowTimeoutForAccount;
    }

    public void setAllowTimeoutForAccount(int allowTimeoutForAccount) {
        this.allowTimeoutForAccount = allowTimeoutForAccount;
    }

     public int getAllowOffline() {
        return allowOffline;
    }

    public void setAllowOffline(int allowOffline) {
        this.allowOffline = allowOffline;
    }
    
    public double getTravelDistance() {
        return travelDistance;
    }

    public void setTravelDistance(double travelDistance) {
        this.travelDistance = travelDistance;
    }

    public int getSourceValue() {
        return sourceValue;
    }

    public void setSourceValue(int sourceValue) {
        this.sourceValue = sourceValue;
    }

   

    public String getPunchDate() {
        return punchDate;
    }

    public void setPunchDate(String punchDate) {
        this.punchDate = punchDate;
    }

    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public List<Integer> getAddTerritoryList() {
        return addTerritoryList;
    }

    public void setAddTerritoryList(List<Integer> addTerritoryList) {
        this.addTerritoryList = addTerritoryList;
    }

    public List<Integer> getDeleteTerritoryList() {
        return deleteTerritoryList;
    }

    public void setDeleteTerritoryList(List<Integer> deleteTerritoryList) {
        this.deleteTerritoryList = deleteTerritoryList;
    }

//    public int getAllowOfflineForAccount() {
//        return allowOfflineForAccount;
//    }
//
//    public void setAllowOfflineForAccount(int allowOfflineForAccount) {
//        this.allowOfflineForAccount = allowOfflineForAccount;
//    }

   
    
}


