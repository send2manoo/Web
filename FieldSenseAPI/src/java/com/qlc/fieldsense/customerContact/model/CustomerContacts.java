/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customerContact.model;

import com.qlc.fieldsense.customAnnotations.ValidDomain;
import com.qlc.fieldsense.customAnnotations.ValidEmail;
import com.qlc.fieldsense.customAnnotations.ValidSize;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author anuja
 */
public class CustomerContacts {

    private int id;
    private int customerId;
    @ValidSize(size = 50, message = "Size must be less than 50")
    private String firstName;
    @ValidSize(size = 50, message = "Size must be less than 50")
    private String middleName;
    @ValidSize(size = 50, message = "Size must be less than 50")
    private String lastName;
    @ValidSize(size = 50, message = "Size must be less than 50")
    private String phone1;
    @ValidSize(size = 50, message = "Size must be less than 50")
    private String phone2;
    @ValidSize(size = 50, message = "Size must be less than 50")
    private String phone3;
    @ValidSize(size = 50, message = "Size must be less than 50")
    private String fax1;
    @ValidSize(size = 50, message = "Size must be less than 50")
    private String fax2;
    @ValidSize(size = 50, message = "Size must be less than 50")
    private String mobile1;
    @ValidSize(size = 50, message = "Size must be less than 50")
    private String mobile2;
    @ValidSize(size = 100, message = "Size must be less than 100")
    @ValidEmail(message = "Email must be valid")
//    @ValidDomain(message = "Domain must not be public")
    private String email1;
    @ValidSize(size = 100, message = "Size must be less than 100")
    @ValidEmail(message = "Email must be valid")
//    @ValidDomain(message = "Domain must not be public")
    private String email2;
    @ValidSize(size = 100, message = "Size must be less than 100")
    private String reportTo;
    @ValidSize(size = 100, message = "Size must be less than 100")
    private String assistantName;
    @ValidSize(size = 100, message = "Size must be less than 100")
    private String spouseName;
    private Date birthDate;
    private String sBirthDate;
    private Date anniversaryDate;
    private String sAnniversaryDate;
    @ValidSize(size = 100, message = "Size must be less than 100")
    private String designation;
    private Timestamp createdOn;
    private String contactImageUrl;

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
    public int getCustomerId() {
        return customerId;
    }

    /**
     *
     * @param customerId
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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
    public String getMiddleName() {
        return middleName;
    }

    /**
     *
     * @param middleName
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
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
    public String getPhone1() {
        return phone1;
    }

    /**
     *
     * @param phone1
     */
    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    /**
     *
     * @return
     */
    public String getPhone2() {
        return phone2;
    }

    /**
     *
     * @param phone2
     */
    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    /**
     *
     * @return
     */
    public String getPhone3() {
        return phone3;
    }

    /**
     *
     * @param phone3
     */
    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    /**
     *
     * @return
     */
    public String getFax1() {
        return fax1;
    }

    /**
     *
     * @param fax1
     */
    public void setFax1(String fax1) {
        this.fax1 = fax1;
    }

    /**
     *
     * @return
     */
    public String getFax2() {
        return fax2;
    }

    /**
     *
     * @param fax2
     */
    public void setFax2(String fax2) {
        this.fax2 = fax2;
    }

    /**
     *
     * @return
     */
    public String getMobile1() {
        return mobile1;
    }

    /**
     *
     * @param mobile1
     */
    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    /**
     *
     * @return
     */
    public String getMobile2() {
        return mobile2;
    }

    /**
     *
     * @param mobile2
     */
    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    /**
     *
     * @return
     */
    public String getEmail1() {
        return email1;
    }

    /**
     *
     * @param email1
     */
    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    /**
     *
     * @return
     */
    public String getEmail2() {
        return email2;
    }

    /**
     *
     * @param email2
     */
    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    /**
     *
     * @return
     */
    public String getReportTo() {
        return reportTo;
    }

    /**
     *
     * @param reportTo
     */
    public void setReportTo(String reportTo) {
        this.reportTo = reportTo;
    }

    /**
     *
     * @return
     */
    public String getAssistantName() {
        return assistantName;
    }

    /**
     *
     * @param assistantName
     */
    public void setAssistantName(String assistantName) {
        this.assistantName = assistantName;
    }

    /**
     *
     * @return
     */
    public String getSpouseName() {
        return spouseName;
    }

    /**
     *
     * @param spouseName
     */
    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    /**
     *
     * @return
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     *
     * @param birthDate
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     *
     * @return
     */
    public Date getAnniversaryDate() {
        return anniversaryDate;
    }

    /**
     *
     * @param anniversaryDate
     */
    public void setAnniversaryDate(Date anniversaryDate) {
        this.anniversaryDate = anniversaryDate;
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
    public String getContactImageUrl() {
        return contactImageUrl;
    }

    /**
     *
     * @param contactImageUrl
     */
    public void setContactImageUrl(String contactImageUrl) {
        this.contactImageUrl = contactImageUrl;
    }

    /**
     *
     * @return
     */
    public String getsBirthDate() {
        return sBirthDate;
    }

    /**
     *
     * @param sBirthDate
     */
    public void setsBirthDate(String sBirthDate) {
        this.sBirthDate = sBirthDate;
    }

    /**
     *
     * @return
     */
    public String getsAnniversaryDate() {
        return sAnniversaryDate;
    }

    /**
     *
     * @param sAnniversaryDate
     */
    public void setsAnniversaryDate(String sAnniversaryDate) {
        this.sAnniversaryDate = sAnniversaryDate;
    }

    /**
     *
     */
    public CustomerContacts() {
        this.id = 0;
        this.customerId = 0;
        this.firstName = "";
        this.middleName = "";
        this.lastName = "";
        this.phone1 = "";
        this.phone2 = "";
        this.phone3 = "";
        this.fax1 = "";
        this.fax2 = "";
        this.mobile1 = "";
        this.mobile2 = "";
        this.email1 = "";
        this.email2 = "";
        this.reportTo = "";
        this.assistantName = "";
        this.spouseName = "";
        this.birthDate = new Date(-100,00,01);
        this.anniversaryDate = new Date(-100,00,01);
        this.designation = "";
        this.createdOn = new Timestamp(0);
        this.contactImageUrl = "";
        this.sAnniversaryDate="";
        this.sBirthDate="";
    }
}
