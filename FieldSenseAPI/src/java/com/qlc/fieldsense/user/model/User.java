/*
 * To change this template, choose Tools | Templates
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
 *
 * @author Ramesh
 * @date 18-02-2014
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
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
    // nikhil
    private String shortAddress;
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
    private String interval;
    private double travelDistance;
    private int sourceValue; // Used for setting punchIn and punchOut Value .PunchIn=1,PunchOut=2,ResetPunchOut=3,Timeoutstart=4,timeoutstop=5 , default=0
        //added by shivakrishna to check version  coming from mobile app
    private String punchDate;
    private int attendanceId;
    private List<Integer> addTerritoryList;
    private List<Integer> deleteTerritoryList;
   private String file;
    private String file_name;
    private String file_upadted_time;
    
    //added by nikhil
    private String adminUserId;
    //ended by nikhil
    private String emp_code; //added by manohar

    //added by jyoti
    private String battery_Percentage, isGPS, network_type, app_version, oS_Version, device_Name, isMockLocation;
    private int isShowIntoReports;
    private String customerId;
    // ended by jyoti
     private int is_mobile_valid, is_terms_condition_agreed, is_newsletter_opt_in, isMailValid;
     private double officeLatitude;   // Added by manohar for dashboard
     private double officeLangitude;
    
         private double  lastKnownLat ; //by nikhil
    private double lastKnownLong;  // by nikhil
      private String officeLocationLat; //by nikhil
    private String officeLocationLong; //by nikhil
    public User() {
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
        // nikhil bhosale (short address)
        this.shortAddress="";
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
        
        //nikhil
        this.interval="";
        
        this.sourceValue=0;
        
        
        this.punchDate="";
        this.attendanceId=0;
        this.addTerritoryList=new ArrayList<Integer>();
        this.deleteTerritoryList=new ArrayList<Integer>();
        
        
        this.file="";
        this.file_name="";
        this.file_upadted_time="";
        this.adminUserId="";
        this.emp_code="";
        
        this.battery_Percentage = "";
        this.isGPS = "";
        this.network_type = "";
        this.app_version = "";
        this.oS_Version = "";
        this.device_Name = "";
        this.isMockLocation = "";
        this.is_mobile_valid = 2;
        this.is_newsletter_opt_in = 2;
        this.is_terms_condition_agreed = 2;
        this.isMailValid = 2;
        this.officeLatitude=0.0;    // Added by manohar for dashboard
        this.officeLangitude=0.0;
  
        this.isShowIntoReports = 1;
        this.customerId = "";
        
            this.officeLocationLat ="";
        this.officeLocationLong = "";
                this.lastKnownLat = 0;
        this.lastKnownLong = 0;
    }

    public String getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(String adminUserId) {
        this.adminUserId = adminUserId;
    }

   
    public String getShortAddress() {
        return shortAddress;
    }

    public void setShortAddress(String shortAddress) {
        this.shortAddress = shortAddress;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    

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
    public boolean isActive() {
        return active;
    }

    /**
     *
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     *
     * @return
     */
    public int getCreatedBy() {
        return createdBy;
    }

    /**
     *
     * @param createdBy
     */
    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
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
        // TODO :Need to change this logic of formatting time
        if( createdOn !=null){
                this.createdOn = createdOn;
        }
   
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
    public String getFullname() {
        return fullname;
    }

    /**
     *
     * @param fullname
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
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
    public String getLastKnownLocation() {
        return lastKnownLocation;
    }

    /**
     *
     * @param lastKnownLocation
     */
    public void setLastKnownLocation(String lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
    }

    /**
     *
     * @return
     */
    public Timestamp getLastKnownLocationTime() {
        return lastKnownLocationTime;
    }

    /**
     *
     * @param lastKnownLocationTime
     */
    public void setLastKnownLocationTime(Timestamp lastKnownLocationTime) {
        this.lastKnownLocationTime = lastKnownLocationTime;
    }

    /**
     *
     * @return
     */
    public Timestamp getLastLoggedOn() {
        return lastLoggedOn;
    }

    /**
     *
     * @param lastLoggedOn
     */
    public void setLastLoggedOn(Timestamp lastLoggedOn) {
        this.lastLoggedOn = lastLoggedOn;
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
    public int getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(int type) {
        this.type = type;
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
     *
     * @return
     */
    public Location getHomeLocation() {
        return homeLocation;
    }

    /**
     *
     * @param homeLocation
     */
    public void setHomeLocation(Location homeLocation) {
        this.homeLocation = homeLocation;
    }

    /**
     *
     * @return
     */
    public Location getOfficeLocation() {
        return officeLocation;
    }

    /**
     *
     * @param officeLocation
     */
    public void setOfficeLocation(Location officeLocation) {
        this.officeLocation = officeLocation;
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
    public int getAccountContactType() {
        return accountContactType;
    }

    /**
     *
     * @param accountContactType
     */
    public void setAccountContactType(int accountContactType) {
        this.accountContactType = accountContactType;
    }

    /**
     *
     * @return
     */
    public boolean isEmailNotification() {
        return emailNotification;
    }

    /**
     *
     * @param emailNotification
     */
    public void setEmailNotification(boolean emailNotification) {
        this.emailNotification = emailNotification;
    }

    /**
     *
     * @return
     */
    public boolean isMobileNotification() {
        return mobileNotification;
    }

    /**
     *
     * @param mobileNotification
     */
    public void setMobileNotification(boolean mobileNotification) {
        this.mobileNotification = mobileNotification;
    }

    /**
     *
     * @return
     */
    public String getPunchStatus() {
        return punchStatus;
    }

    /**
     *
     * @param punchStatus
     */
    public void setPunchStatus(String punchStatus) {
        this.punchStatus = punchStatus;
    }

    /**
     *
     * @return
     */
    public String getPunchStatusTime() {
        return punchStatusTime;
    }

    /**
     *
     * @param punchStatusTime
     */
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

    /**
     *
     * @return
     */
    public int getReport_to() {
        return report_to;
    }

    /**
     *
     * @param report_to
     */
    public void setReport_to(int report_to) {
        this.report_to = report_to;
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
    public int getAllowTimeoutForAccount() {
        return allowTimeoutForAccount;
    }

    /**
     *
     * @param allowTimeoutForAccount
     */
    public void setAllowTimeoutForAccount(int allowTimeoutForAccount) {
        this.allowTimeoutForAccount = allowTimeoutForAccount;
    }

    /**
     *
     * @return
     */
    public int getAllowOffline() {
        return allowOffline;
    }

    /**
     *
     * @param allowOffline
     */
    public void setAllowOffline(int allowOffline) {
        this.allowOffline = allowOffline;
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
    public String getPunchDate() {
        return punchDate;
    }

    /**
     *
     * @param punchDate
     */
    public void setPunchDate(String punchDate) {
        this.punchDate = punchDate;
    }

    /**
     *
     * @return
     */
    public int getAttendanceId() {
        return attendanceId;
    }

    /**
     *
     * @param attendanceId
     */
    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    /**
     *
     * @return
     */
    public List<Integer> getAddTerritoryList() {
        return addTerritoryList;
    }

    /**
     *
     * @param addTerritoryList
     */
    public void setAddTerritoryList(List<Integer> addTerritoryList) {
        this.addTerritoryList = addTerritoryList;
    }

    

    public List<Integer> getDeleteTerritoryList() {
        return deleteTerritoryList;
    }

    /**
     *
     * @param deleteTerritoryList
     */
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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_upadted_time() {
        return file_upadted_time;
    }

   

    public void setFile_upadted_time(String file_upadted_time) {
        this.file_upadted_time = file_upadted_time;
    }

    public String getEmp_code() {
        return emp_code;
    }

    public void setEmp_code(String emp_code) {
        this.emp_code = emp_code;
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

    @Override
    public String toString() {
        return "User{" + "versionCode=" + versionCode + ", id=" + id + ", accountId=" + accountId + ", firstName=" + firstName + ", lastName=" + lastName + ", fullname=" + fullname + ", emailAddress=" + emailAddress + ", password=" + password + ", mobileNo=" + mobileNo + ", gender=" + gender + ", role=" + role + ", parentId=" + parentId + ", active=" + active + ", lastLoggedOn=" + lastLoggedOn + ", lastKnownLocationTime=" + lastKnownLocationTime + ", lastKnownLocation=" + lastKnownLocation + ", shortAddress=" + shortAddress + ", createdOn=" + createdOn + ", createdBy=" + createdBy + ", latitude=" + latitude + ", langitude=" + langitude + ", type=" + type + ", designation=" + designation + ", homeLocation=" + homeLocation + ", officeLocation=" + officeLocation + ", usersCount=" + usersCount + ", punchStatus=" + punchStatus + ", punchStatusTime=" + punchStatusTime + ", isCustomerLocation=" + isCustomerLocation + ", customerName=" + customerName + ", locationIdentifier=" + locationIdentifier + ", accountContactType=" + accountContactType + ", emailNotification=" + emailNotification + ", mobileNotification=" + mobileNotification + ", report_to=" + report_to + ", updatedOn=" + updatedOn + ", updatedBy=" + updatedBy + ", userAccuracy=" + userAccuracy + ", checkInRadius=" + checkInRadius + ", allowTimeout=" + allowTimeout + ", allowTimeoutForAccount=" + allowTimeoutForAccount + ", allowOffline=" + allowOffline + ", interval=" + interval + ", travelDistance=" + travelDistance + ", sourceValue=" + sourceValue + ", punchDate=" + punchDate + ", attendanceId=" + attendanceId + ", addTerritoryList=" + addTerritoryList + ", deleteTerritoryList=" + deleteTerritoryList + ", file=" + file + ", file_name=" + file_name + ", file_upadted_time=" + file_upadted_time + ", adminUserId=" + adminUserId + ", emp_code=" + emp_code + ", battery_Percentage=" + battery_Percentage + ", isGPS=" + isGPS + ", network_type=" + network_type + ", app_version=" + app_version + ", oS_Version=" + oS_Version + ", device_Name=" + device_Name + ", isMockLocation=" + isMockLocation + '}';
    }

    public int getIs_mobile_valid() {
        return is_mobile_valid;
    }

    public void setIs_mobile_valid(int is_mobile_valid) {
        this.is_mobile_valid = is_mobile_valid;
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

    public int getIsMailValid() {
        return isMailValid;
    }

    public void setIsMailValid(int isMailValid) {
        this.isMailValid = isMailValid;
    }

    public double getOfficeLatitude() {
        return officeLatitude;
    }

    public void setOfficeLatitude(double officeLatitude) {
        this.officeLatitude = officeLatitude;
    }

    public double getOfficeLangitude() {
        return officeLangitude;
    }

    public void setOfficeLangitude(double officeLangitude) {
        this.officeLangitude = officeLangitude;
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

    public double getLastKnownLat() {
        return lastKnownLat;
    }

    public void setLastKnownLat(double lastKnownLat) {
        this.lastKnownLat = lastKnownLat;
    }

    public double getLastKnownLong() {
        return lastKnownLong;
    }

    public void setLastKnownLong(double lastKnownLong) {
        this.lastKnownLong = lastKnownLong;
    }

    public String getOfficeLocationLat() {
        return officeLocationLat;
    }

    public void setOfficeLocationLat(String officeLocationLat) {
        this.officeLocationLat = officeLocationLat;
    }

    public String getOfficeLocationLong() {
        return officeLocationLong;
    }

    public void setOfficeLocationLong(String officeLocationLong) {
        this.officeLocationLong = officeLocationLong;
    }

    
}
