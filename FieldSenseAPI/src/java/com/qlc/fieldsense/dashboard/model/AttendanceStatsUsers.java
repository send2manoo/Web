/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.dashboard.model;

/**
 *
 * @author root
 */
public class AttendanceStatsUsers {
    private int userCount;
    public AttendanceStatsUsers()
    {
        this.userCount=0;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }
    
}
