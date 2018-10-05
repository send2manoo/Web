/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.attendance.model;

import com.qlc.fieldsense.user.model.User;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Date;

/**
 *
 * @author mayank
 */
public class Attendance {

    private int id;
    private User user;
    private Date date; //punch_In
    private Date punchOutDate; // For Punch Out
    private Time punchInTime;
    private String punchInLocation;
    private String punchInLatitude;
    private String punchInLangitude;
    private Time punchOutTime;
    private String punchOutLocation;
    private String punchOutLatitude;
    private String punchOutLagitude;
    private Timestamp createdOn;
    private String sdate;
    private String spunchInTime;
    private String punchInLocationNotFoundReason;
    private String punchOutLocationNotFoundReason;
    private int apiResult;
    private int versionCode;
    private int attendanceStatus; // by jyoti
    private boolean autoPunchOut;
    private int timeZoneOffset;
    
    /**
     *
     */
    public Attendance() {
        this.id = 0;
        this.user = new User();
        this.date = new Date(0);
        this.punchOutDate = new Date(0); //for punchout
        this.punchInTime = new Time(0);
        this.punchInLocation = "";
        this.punchInLatitude = "";
        this.punchInLangitude = "";
        this.punchOutTime = new Time(0);
        this.punchOutLocation = "";
        this.punchOutLatitude = "";
        this.punchOutLagitude = "";
        this.createdOn = new Timestamp(0);
        this.sdate = "";
        this.spunchInTime = "";
        this.punchInLocationNotFoundReason = "";
        this.punchOutLocationNotFoundReason = "";
        this.apiResult = 0;
        this.attendanceStatus = 0;
        this.versionCode=0;
        this.autoPunchOut=false;
        this.timeZoneOffset=0;
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
    public Date getDate() {
        return date;
    }

    /**
     *
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
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
    public String getPunchInLatitude() {
        return punchInLatitude;
    }

    /**
     *
     * @param punchInLatitude
     */
    public void setPunchInLatitude(String punchInLatitude) {
        this.punchInLatitude = punchInLatitude;
    }

    /**
     *
     * @return
     */
    public String getPunchInLangitude() {
        return punchInLangitude;
    }

    /**
     *
     * @param punchInLangitude
     */
    public void setPunchInLangitude(String punchInLangitude) {
        this.punchInLangitude = punchInLangitude;
    }

    /**
     *
     * @return
     */
    public String getPunchOutLatitude() {
        return punchOutLatitude;
    }

    /**
     *
     * @param punchOutLatitude
     */
    public void setPunchOutLatitude(String punchOutLatitude) {
        this.punchOutLatitude = punchOutLatitude;
    }

    /**
     *
     * @return
     */
    public String getPunchOutLagitude() {
        return punchOutLagitude;
    }

    /**
     *
     * @param punchOutLagitude
     */
    public void setPunchOutLagitude(String punchOutLagitude) {
        this.punchOutLagitude = punchOutLagitude;
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
    public Time getPunchInTime() {
        return punchInTime;
    }

    /**
     *
     * @param punchInTime
     */
    public void setPunchInTime(Time punchInTime) {
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
    public Time getPunchOutTime() {
        return punchOutTime;
    }

    /**
     *
     * @param punchOutTime
     */
    public void setPunchOutTime(Time punchOutTime) {
        this.punchOutTime = punchOutTime;
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
    public String getPunchInLocationNotFoundReason() {
        return punchInLocationNotFoundReason;
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
     * @return
     */
    public String getSdate() {
        return sdate;
    }

    /**
     *
     * @return
     */
    public String getSpunchInTime() {
        return spunchInTime;
    }

    /**
     *
     * @return
     */
    public int getApiResult() {
        return apiResult;
    }

    /**
     *
     * @param apiResult
     */
    public void setApiResult(int apiResult) {
        this.apiResult = apiResult;
    }

    public int getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(int attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    /**
     *
     * @return
     */
    
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
    public Date getPunchOutDate() {
        return punchOutDate;
    }

    /**
     *
     * @param punchOutDate
     */
    public void setPunchOutDate(Date punchOutDate) {
        this.punchOutDate = punchOutDate;
    }

    /**
     *
     * @return
     */
    public boolean isAutoPunchOut() {
        return autoPunchOut;
    }

    /**
     *
     * @param autoPunchOut
     */
    public void setAutoPunchOut(boolean autoPunchOut) {
        this.autoPunchOut = autoPunchOut;
    }

    /**
     *
     * @return
     */
    public int getTimeZoneOffset() {
        return timeZoneOffset;
    }

    /**
     *
     * @param timeZoneOffset
     */
    public void setTimeZoneOffset(int timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }

    @Override
    public String toString() {
        return "Attendance{" + "id=" + id + ", user=" + user + ", date=" + date + ", punchOutDate=" + punchOutDate + ", punchInTime=" + punchInTime + ", punchInLocation=" + punchInLocation + ", punchInLatitude=" + punchInLatitude + ", punchInLangitude=" + punchInLangitude + ", punchOutTime=" + punchOutTime + ", punchOutLocation=" + punchOutLocation + ", punchOutLatitude=" + punchOutLatitude + ", punchOutLagitude=" + punchOutLagitude + ", createdOn=" + createdOn + ", sdate=" + sdate + ", spunchInTime=" + spunchInTime + ", punchInLocationNotFoundReason=" + punchInLocationNotFoundReason + ", punchOutLocationNotFoundReason=" + punchOutLocationNotFoundReason + ", apiResult=" + apiResult + ", versionCode=" + versionCode + ", autoPunchOut=" + autoPunchOut + ", timeZoneOffset=" + timeZoneOffset + '}';
    }
    
}
