/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.stats.model;

import java.sql.Timestamp;

/**
 *
 * @author Jyoti <singh.jyoti at QLC>
 */
public class DIYData {

    private String first_name;
    private String last_name;
    private String full_name;
    private String email_address;
    private String mobile_number;
    private int gender;
    private String designation;
    private String otp;
    private Timestamp otp_expiry;
    private String company_name;
    private String address1;
    private int region_id_fk;
    private String city;
    private String country;
    private String state;
    private String zip_code;
    private String company_phone_number1;
    private String company_website;
    private int status;
    private int user_limit;
    private Timestamp created_on;
    private Timestamp modified_on;
    private String industry;
    private String plan;
    private String country_code;
    private int is_mobile_valid, is_terms_condition_agreed, is_newsletter_opt_in, is_account_created;
    private String encryptedKey;
    private int step_number;
    private String gSource, gFeature;
    private String password;
    private int isMailValid;
    private Timestamp mail_expired_on;
    private String device;

    public DIYData() {
        this.first_name = "";
        this.last_name = "";
        this.full_name = "";
        this.email_address = "";
        this.mobile_number = "";
        this.gender = 0;
        this.designation = "";
        this.otp = "";
        this.otp_expiry = new Timestamp(0);
        this.company_name = "";
        this.address1 = "";
        this.region_id_fk = 0;
        this.city = "";
        this.country = "";
        this.state = "";
        this.zip_code = "";
        this.company_phone_number1 = "";
        this.company_website = "";
        this.status = 0;
        this.user_limit = 0;
        this.created_on = new Timestamp(0);
        this.modified_on = new Timestamp(0);
        this.mail_expired_on = new Timestamp(0);
        this.industry = "";
        this.plan = "";
        this.country_code = "91";
        this.is_mobile_valid = 2;
        this.is_newsletter_opt_in = 2;
        this.is_terms_condition_agreed = 2;
        this.step_number = 0;
        this.encryptedKey = "";
        this.gSource = "";
        this.gFeature = "";
        this.password = "";
        this.isMailValid = 3;
        this.is_account_created = 0;
        this.device = "";
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public int getRegion_id_fk() {
        return region_id_fk;
    }

    public void setRegion_id_fk(int region_id_fk) {
        this.region_id_fk = region_id_fk;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getCompany_phone_number1() {
        return company_phone_number1;
    }

    public void setCompany_phone_number1(String company_phone_number1) {
        this.company_phone_number1 = company_phone_number1;
    }

    public String getCompany_website() {
        return company_website;
    }

    public void setCompany_website(String company_website) {
        this.company_website = company_website;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUser_limit() {
        return user_limit;
    }

    public void setUser_limit(int user_limit) {
        this.user_limit = user_limit;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public Timestamp getOtp_expiry() {
        return otp_expiry;
    }

    public void setOtp_expiry(Timestamp otp_expiry) {
        this.otp_expiry = otp_expiry;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Timestamp getCreated_on() {
        return created_on;
    }

    public void setCreated_on(Timestamp created_on) {
        this.created_on = created_on;
    }

    public Timestamp getModified_on() {
        return modified_on;
    }

    public void setModified_on(Timestamp modified_on) {
        this.modified_on = modified_on;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public int getStep_number() {
        return step_number;
    }

    public void setStep_number(int step_number) {
        this.step_number = step_number;
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

    public String getEncryptedKey() {
        return encryptedKey;
    }

    public void setEncryptedKey(String encryptedKey) {
        this.encryptedKey = encryptedKey;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIsMailValid() {
        return isMailValid;
    }

    public void setIsMailValid(int isMailValid) {
        this.isMailValid = isMailValid;
    }

    public String getgSource() {
        return gSource;
    }

    public void setgSource(String gSource) {
        this.gSource = gSource;
    }

    public Timestamp getMail_expired_on() {
        return mail_expired_on;
    }

    public void setMail_expired_on(Timestamp mail_expired_on) {
        this.mail_expired_on = mail_expired_on;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getgFeature() {
        return gFeature;
    }

    public void setgFeature(String gFeature) {
        this.gFeature = gFeature;
    }

    public int getIs_account_created() {
        return is_account_created;
    }

    public void setIs_account_created(int is_account_created) {
        this.is_account_created = is_account_created;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    
}
