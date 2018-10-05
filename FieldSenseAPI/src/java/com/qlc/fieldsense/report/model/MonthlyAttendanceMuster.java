/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.report.model;

/**
 *
 * @author vaibhav
 */
public class MonthlyAttendanceMuster {
   
   
    private int userId;
    private String punchDate;
    private String punchInTime;
    private String userName;
    private String emp_code;
    private String[] monthlyMusterArray ;
    private String reportsTo;

    
    public MonthlyAttendanceMuster(){
        this.userId = 0;
        this.punchDate = "";
        this.punchInTime = "";
        this.userName = "";
        this.emp_code = "";
        this.monthlyMusterArray = null;
        this.reportsTo = "";
    }
    
    public String[] getMonthlyMusterArray() {
        return monthlyMusterArray;
    }

    public void setMonthlyMusterArray(String[] monthlMusterArray) {
        this.monthlyMusterArray = monthlMusterArray;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPunchDate() {
        return punchDate;
    }

    public void setPunchDate(String punchDate) {
        this.punchDate = punchDate;
    }

    public String getPunchInTime() {
        return punchInTime;
    }

    public void setPunchInTime(String punchInTime) {
        this.punchInTime = punchInTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmp_code() {
        return emp_code;
    }

    public void setEmp_code(String emp_code) {
        this.emp_code = emp_code;
    }

    public String getReportsTo() {
        return reportsTo;
    }

    public void setReportsTo(String reportsTo) {
        this.reportsTo = reportsTo;
    }
    
    
}
