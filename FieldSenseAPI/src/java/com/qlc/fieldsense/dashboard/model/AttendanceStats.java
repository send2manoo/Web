/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.dashboard.model;

import com.qlc.fieldsense.user.model.User;



/**
 *
 * @author manohar
 */
public class AttendanceStats {
   
    private String userkey;
    private int keyValue;        
    private User user;
    private String punchInTime;
    private String punchOutTime;
    private String punchInLocation;
    
    public AttendanceStats() {
        this.userkey = "";
        this.keyValue = 0;
        this.user = null;
        this.punchInTime = "";
        this.punchOutTime = "";
        this.punchInLocation="";
    }

    public String getUserkey() {
        return userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPunchInTime() {
        return punchInTime;
    }

    public void setPunchInTime(String punchInTime) {
        this.punchInTime = punchInTime;
    }

//   
//    @Override
//    public String toString()
//    {
//        return userkey+" "+user+" "+attendance+" "+punchInTime;
//    
//    }

    public String getPunchInLocation() {
        return punchInLocation;
    }

    public void setPunchInLocation(String punchInLocation) {
        this.punchInLocation = punchInLocation;
    }

    public int getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(int keyValue) {
        this.keyValue = keyValue;
    }

    public String getPunchOutTime() {
        return punchOutTime;
    }

    public void setPunchOutTime(String punchOutTime) {
        this.punchOutTime = punchOutTime;
    }

}
