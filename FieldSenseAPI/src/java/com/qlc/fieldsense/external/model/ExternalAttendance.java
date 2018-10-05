/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.external.model;

/**
 *
 * @author jyoti
 */
public class ExternalAttendance {

    private String punchDate;
    private String punchInTime;
    private String punchOutTime;
    private String punchInLocation;
    private String punchOutLocation;
    private String userName;
    private String punchInLocationNotFoundReason;
    private String punchOutLocationNotFoundReason;
    private String totalTimeOut;
    private String punchOutDate;
    private String WorkHr;
    private String emp_code;
    private int totalrecords;

    public ExternalAttendance() {
        this.punchDate = "";
        this.punchInTime = "";
        this.punchInLocation = "";
        this.punchOutTime = "";
        this.punchOutLocation = "";
        this.userName = "";
        this.punchInLocationNotFoundReason = "";
        this.punchOutLocationNotFoundReason = "";
        this.totalTimeOut = "00:00:00";
        this.punchOutDate = "";
        this.WorkHr = "-";
        this.emp_code = "";
        this.totalrecords=0;
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

    public String getPunchOutTime() {
        return punchOutTime;
    }

    public void setPunchOutTime(String punchOutTime) {
        this.punchOutTime = punchOutTime;
    }

    public String getPunchInLocation() {
        return punchInLocation;
    }

    public void setPunchInLocation(String punchInLocation) {
        this.punchInLocation = punchInLocation;
    }

    public String getPunchOutLocation() {
        return punchOutLocation;
    }

    public void setPunchOutLocation(String punchOutLocation) {
        this.punchOutLocation = punchOutLocation;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPunchInLocationNotFoundReason() {
        return punchInLocationNotFoundReason;
    }

    public void setPunchInLocationNotFoundReason(String punchInLocationNotFoundReason) {
        this.punchInLocationNotFoundReason = punchInLocationNotFoundReason;
    }

    public String getPunchOutLocationNotFoundReason() {
        return punchOutLocationNotFoundReason;
    }

    public void setPunchOutLocationNotFoundReason(String punchOutLocationNotFoundReason) {
        this.punchOutLocationNotFoundReason = punchOutLocationNotFoundReason;
    }

    public String getTotalTimeOut() {
        return totalTimeOut;
    }

    public void setTotalTimeOut(String totalTimeOut) {
        this.totalTimeOut = totalTimeOut;
    }

    public String getPunchOutDate() {
        return punchOutDate;
    }

    public void setPunchOutDate(String punchOutDate) {
        this.punchOutDate = punchOutDate;
    }

    public String getWorkHr() {
        return WorkHr;
    }

    public void setWorkHr(String WorkHr) {
        this.WorkHr = WorkHr;
    }

    public String getEmp_code() {
        return emp_code;
    }

    public void setEmp_code(String emp_code) {
        this.emp_code = emp_code;
    }

    public int getTotalrecords() {
        return totalrecords;
    }

    public void setTotalrecords(int totalrecords) {
        this.totalrecords = totalrecords;
    }

}
