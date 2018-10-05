/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.report.model;

/**
 *
 * @author Ramesh
 * @date 21-08-2014
 * 
 */
public class Attendance {

    private int id;
    private int userId;
    private String punchDate;
    private String punchInTime;
    private String punchOutTime;
    private String punchInLocation;
    private String punchOutLocation;
    private String userName;
    private String punchInLocationNotFoundReason;
    private String punchOutLocationNotFoundReason;
    private int totalrecords;
    private String totalTimeOut;
    private String punchOutDate;
    private String WorkHr;
    private String emp_code;

    /**
     *
     */
    public Attendance() {
        this.id = 0;
        this.userId = 0;
        this.punchDate = "";
        this.punchInTime = "";
        this.punchInLocation = "";
        this.punchOutTime = "";
        this.punchOutLocation = "";
        this.userName = "";
        this.punchInLocationNotFoundReason = "";
        this.punchOutLocationNotFoundReason = "";
        this.totalrecords=0;
        this.totalTimeOut="00:00:00";
        this.punchOutDate="";
        this.WorkHr="-";
        this.emp_code="";
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
    public String getPunchInLocation() {
        return punchInLocation;
    }

    /**
     *
     * @param punchInLocation
     */
    public void setPunchInLocation(String punchInLocation) {
        this.punchInLocation = punchInLocation;
    }

    /**
     *
     * @return
     */
    public String getPunchInTime() {
        return punchInTime;
    }

    /**
     *
     * @param punchInTime
     */
    public void setPunchInTime(String punchInTime) {
        this.punchInTime = punchInTime;
    }

    /**
     *
     * @return
     */
    public String getPunchOutLocation() {
        return punchOutLocation;
    }

    /**
     *
     * @param punchOutLocation
     */
    public void setPunchOutLocation(String punchOutLocation) {
        this.punchOutLocation = punchOutLocation;
    }

    /**
     *
     * @return
     */
    public String getPunchOutTime() {
        return punchOutTime;
    }

    /**
     *
     * @param punchOutTime
     */
    public void setPunchOutTime(String punchOutTime) {
        this.punchOutTime = punchOutTime;
    }

    /**
     *
     * @return
     */
    public int getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     *
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     *
     * @return
     */
    public String getPunchInLocationNotFoundReason() {
        return punchInLocationNotFoundReason;
    }

    /**
     *
     * @param punchInLocationNotFoundReason
     */
    public void setPunchInLocationNotFoundReason(String punchInLocationNotFoundReason) {
        this.punchInLocationNotFoundReason = punchInLocationNotFoundReason;
    }

    /**
     *
     * @return
     */
    public String getPunchOutLocationNotFoundReason() {
        return punchOutLocationNotFoundReason;
    }

    /**
     *
     * @param punchOutLocationNotFoundReason
     */
    public void setPunchOutLocationNotFoundReason(String punchOutLocationNotFoundReason) {
        this.punchOutLocationNotFoundReason = punchOutLocationNotFoundReason;
    }
    
    /**
     *
     * @return
     */
    public int getTotalrecords() {
        return totalrecords;
    }

    /**
     *
     * @param totalrecords
     */
    public void setTotalrecords(int totalrecords) {
        this.totalrecords = totalrecords;
    }
    
    /**
     *
     * @return
     */
    public String getTotalTimeOut() {
        return totalTimeOut;
    }

    /**
     *
     * @param totalTimeOut
     */
    public void setTotalTimeOut(String totalTimeOut) {
        this.totalTimeOut = totalTimeOut;
    }

    /**
     *
     * @return
     */
    public String getWorkHr() {
        return WorkHr;
    }

    /**
     *
     * @param WorkHr
     */
    public void setWorkHr(String WorkHr) {
        this.WorkHr = WorkHr;
    }

    /**
     *
     * @return
     */
    public String getPunchOutDate() {
        return punchOutDate;
    }

    /**
     *
     * @param punchOutDate
     */
    public void setPunchOutDate(String punchOutDate) {
        this.punchOutDate = punchOutDate;
    }

    public String getEmp_code() {
        return emp_code;
    }

    public void setEmp_code(String emp_code) {
        this.emp_code = emp_code;
    }

}
