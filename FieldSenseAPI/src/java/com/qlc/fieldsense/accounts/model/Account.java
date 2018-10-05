/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.accounts.model;


import com.qlc.fieldsense.customAnnotations.*;
import com.qlc.fieldsense.user.model.User;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Ramesh
 */
public class Account {

    private int id;
    @NotEmpty
    @Length(min = 1, max = 200)
    @TrimSpaceValidation(message = "Please enter valid data")
   // @UniqueCompanyName(message = "Company name already exist")
    private String companyName;
    @NotEmpty
    @Length(min = 1, max = 200)
    @TrimSpaceValidation(message = "Please enter valid data")
    private String address1;
    private String address2;
    private String address3;
    @NotEmpty
    @Length(min = 1, max = 100)
    @TrimSpaceValidation(message = "Please enter valid data")
    private String city;
    @NotEmpty
    @Length(min = 1, max = 100)
    @TrimSpaceValidation(message = "Please enter valid data")
    private String state;
    @NotEmpty
    @Length(min = 1, max = 100)
    @TrimSpaceValidation(message = "Please enter valid data")
    private String country;
    /* @NotEmpty
    @Length(min = 1, max = 10)
    @TrimSpaceValidation(message = "Please enter valid data")*/
    private String zipCode;
    @ValidLongSize(size = 19, message = "Size must be less than 20")
    private Long companyPhoneNumber1;
    private Long companyPhoneNumber2;
    private String companyWebsite;
    private int status;
    private Timestamp createdOn;
    private Timestamp startDate;
    private String strStartDate;
    private Timestamp endDate;
    private Timestamp expiredOn;

    private String sExpiredOn;
    private String sEnd;

    
    private String emailAddress;
    private User user;
    private List <Adminuser> adminuser;
    private List <User> allAdminUser;
    private String accountid;
    @Min(25)
    private int userLimit;
    private int regionId;
    
    private String plan;
    private String paymentCycle;
      private String industry;
      private User1 user1 ;
      
    private boolean onlineCreation;
    //by nikhil
    private String websitePath;
    //by nikhil adding details of user for DIY
    private String firstUserName;
    private String userMobileNumber;
    private String userEmailId;
    private Long TotalAmmount;
    //delete below :- salesEmailId
    private String salesEmailId;
    /**
     *
     */
    public Account() {
        this.id = 0;
        this.companyName = "";
        this.address1 = "";
        this.address2 = "";
        this.address3 = "";
        this.city = "";
        this.state = "";
        this.country = "";
        this.zipCode = "";
        this.companyPhoneNumber1 = 0L;
        this.companyPhoneNumber2 = 0L;
        this.companyWebsite = "";
        this.status = 0;
        this.createdOn = new Timestamp(0);
        this.expiredOn = new Timestamp(0);
        this.emailAddress = "";
       this.user = new User();
       this.user1 = new User1();
        this.userLimit=25;
        this.regionId=0;
        this.startDate = new Timestamp(0);
        this.endDate = new Timestamp(0); 
        
        this.plan="";
      
        this.industry="";
        this.adminuser = new ArrayList <Adminuser>();
        this.allAdminUser = new ArrayList <User>();
        this.accountid="";
        this.onlineCreation =false;
        this.websitePath="";
        this.firstUserName="";
        this.userMobileNumber="";
        this.userEmailId="";
        this.paymentCycle="";
        this.salesEmailId="";
    }

    public String getPaymentCycle() {
        return paymentCycle;
    }

    public String getSalesEmailId() {
        return salesEmailId;
    }

    public void setSalesEmailId(String salesEmailId) {
        this.salesEmailId = salesEmailId;
    }

    public void setPaymentCycle(String paymentCycle) {
        this.paymentCycle = paymentCycle;
    }

    public String getFirstUserName() {
        return firstUserName;
    }

    public void setFirstUserName(String firstUserName) {
        this.firstUserName = firstUserName;
    }

    public String getUserMobileNumber() {
        return userMobileNumber;
    }

    public void setUserMobileNumber(String userMobileNumber) {
        this.userMobileNumber = userMobileNumber;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public String getAccountID() {
        return accountid;
    }

    public void setAccountID(String accountid) {
        this.accountid = accountid;
    }
    
    /**
     *
     * @return
     */
    public Timestamp getStartDate() {
        return startDate;
    }

    /**
     *
     * @param startDate
     */
    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    /**
     *
     * @return
     */
    public Timestamp getEndDate() {
        return endDate;
    }

    /**
     *
     * @param endDate
     */
    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    /**
     *
     * @return
     */
    public String getAddress1() {
        return address1;
    }

    /**
     *
     * @param address1
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /**
     *
     * @return
     */
    public String getAddress2() {
        return address2;
    }

    /**
     *
     * @param address2
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     *
     * @return
     */
    public String getsExpiredOn() {
        return sExpiredOn;
    }

    /**
     *
     * @param sExpiredOn
     */
    public void setsExpiredOn(String sExpiredOn) {
        this.sExpiredOn = sExpiredOn;
    }

    /**
     *
     * @return
     */
    public String getAddress3() {
        return address3;
    }

    /**
     *
     * @param address3
     */
    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    /**
     *
     * @return
     */
    public String getCity() {
        return city;
    }

    /**
     *
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     *
     * @return
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     *
     * @param companyName
     */
    public void setCompanyName(String companyName) {
//        System.out.println(" setCompanyName $$$$");
        this.companyName = companyName;
    }

    /**
     *
     * @return
     */
    public Long getCompanyPhoneNumber1() {
        return companyPhoneNumber1;
    }

    /**
     *
     * @param companyPhoneNumber1
     */
    public void setCompanyPhoneNumber1(Long companyPhoneNumber1) {
        this.companyPhoneNumber1 = companyPhoneNumber1;
    }

    /**
     *
     * @return
     */
    public Long getCompanyPhoneNumber2() {
        return companyPhoneNumber2;
    }

    /**
     *
     * @param companyPhoneNumber2
     */
    public void setCompanyPhoneNumber2(Long companyPhoneNumber2) {
        this.companyPhoneNumber2 = companyPhoneNumber2;
    }

    /**
     *
     * @return
     */
    public String getCompanyWebsite() {
        return companyWebsite;
    }

    /**
     *
     * @param companyWebsite
     */
    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    /**
     *
     * @return
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
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
    public String getState() {
        return state;
    }

    /**
     *
     * @param state
     */
    public void setState(String state) {
        this.state = state;
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
    public String getZipCode() {
        return zipCode;
    }

    /**
     *
     * @param zipCode
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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
    public User getUser() {
        return user;
    }

    /**
     *
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     *
     * @return
     */
    public Timestamp getExpiredOn() {
        return expiredOn;
    }

    /**
     * @param expiredOn the expiredOn to set
     */
    public void setExpiredOn(Timestamp expiredOn) {
        this.expiredOn = expiredOn;
    }

    /**
     *
     * @return
     */
    public int getUserLimit() {
        return userLimit;
    }

    /**
     *
     * @param userLimit
     */
    public void setUserLimit(int userLimit) {
        this.userLimit = userLimit;
    }

    /**
     *
     * @return
     */
    public int getRegionId() {
        return regionId;
    }

    /**
     *
     * @param regionId
     */
    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    /**
     *
     * @return
     */
    public String getStrStartDate() {
        return strStartDate;
    }

    /**
     *
     * @param strStartDate
     */
    public void setStrStartDate(String strStartDate) {
        this.strStartDate = strStartDate;
    }

    public String getsEnd() {
        return sEnd;
    }

    public void setsEnd(String sEnd) {
        this.sEnd = sEnd;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

      public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }
    
    public List <Adminuser> getAdminuser() {
 
        return adminuser;
    }

    public void setAdminuser(List <Adminuser> adminuser) {

        this.adminuser = adminuser;
    }
 

    public User1 getUser1() {
        return user1;
    }

    public void setUser1(User1 user1) {
        this.user1 = user1;
    }
   
    public List <User> getAllAdminUser() {
        return allAdminUser;
    }

    public void setAllAdminUser(List <User> allAdminUser) {
        this.allAdminUser = allAdminUser;
    }

    @Override
    public String toString() {
        return "Account{" + "id=" + id + ", companyName=" + companyName + ", address1=" + address1 + ", address2=" + address2 + ", address3=" + address3 + ", city=" + city + ", state=" + state + ", country=" + country + ", zipCode=" + zipCode + ", companyPhoneNumber1=" + companyPhoneNumber1 + ", companyPhoneNumber2=" + companyPhoneNumber2 + ", companyWebsite=" + companyWebsite + ", status=" + status + ", createdOn=" + createdOn + ", startDate=" + startDate + ", strStartDate=" + strStartDate + ", endDate=" + endDate + ", expiredOn=" + expiredOn + ", sExpiredOn=" + sExpiredOn + ", sEnd=" + sEnd + ", emailAddress=" + emailAddress + ", user=" + user + ", adminuser=" + adminuser + ", allAdminUser=" + allAdminUser + ", accountID=" + accountid + ", userLimit=" + userLimit + ", regionId=" + regionId + ", plan=" + plan + ", industry=" + industry + ", user1=" + user1 + '}';
    }

    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    public boolean isOnlineCreation() {
        return onlineCreation;
    }

    public void setOnlineCreation(boolean onlineCreation) {
        this.onlineCreation = onlineCreation;
    }

    public String getWebsitePath() {
        return websitePath;
    }

    public void setWebsitePath(String websitePath) {
        this.websitePath = websitePath;
    }

    
    
}
