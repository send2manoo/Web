/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.auth.model;

/**
 *
 * @author Ramesh
 * @date 30-01-2014
 * @purpouse This class used to login the user.
 */
public class UserLogin {

    private String userEmailAddress;
    private String password;
    private String key;
    private String userMobileNo;
    private int deviceOS;
    private int appVersion; // added by jyoti

    /**
     *
     */
    public UserLogin() {
        this.userEmailAddress = "";
        this.password = "";
        this.key = "";
        this.userMobileNo="";
        this.deviceOS=1;
        this.appVersion = 0;
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
    public String getKey() {
        return key;
    }

    /**
     *
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }
    
    /**
     *
     * @return
     */
    public String getUserMobileNo() {
        return userMobileNo;
    }

    /**
     *
     * @param userMobileNo
     */
    public void setUserMobileNo(String userMobileNo) {
        this.userMobileNo = userMobileNo;
    }

    /**
     *
     * @return
     */
    public int getDeviceOS() {
        return deviceOS;
    }

    /**
     *
     * @param deviceOS
     */
    public void setDeviceOS(int deviceOS) {
        this.deviceOS = deviceOS;
    }

    public int getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(int appVersion) {
        this.appVersion = appVersion;
    }
    
}
