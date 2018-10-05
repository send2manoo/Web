/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.mobile.model;

import com.qlc.fieldsense.attendance.model.AttendanceTimeout;
import java.sql.Time;
import java.sql.Timestamp;

/**
 *
 * @author Ramesh
 * @date 06-06-2014
 */
public class LeftSliderMenu {

    private int todayAppointments;
    private int unreadMessagesCount;
    private String punchDate;
    private String punchInTime;
    private String punchOutTime;
    private boolean attendanceStatus;
    private AttendanceTimeout attendanceTiemout;
    private int userAccuracy;
    private int checkInRadius;
    private int allowTimeout;
    private int attendanceId;
    private Timestamp territoryLastUpdated;
    private int userRole;
    private int allowOfflineOperation;
    private String locationInterval;
    private int territoryCount;
    private int hasSubordinates;
    private String lastpunchInTime;                //added by manohar
    private String lastpunchOutTime;
    private String lastcheckInTime;
    private String lastcheckOutTime;
    private int appointmentId;      //added by manohar
    private int customerId;
    private String accountName;        //Added by siddhesh
    // Added by Jyoti
    private long lastPunchInDateTime;
    private long lastPunchOutDateTime;
    private long lastCheckInDateTime;
    private long lastCheckOutDateTime;
    private int appVersion;
    private String auto_punch_out_time;
    private int auto_punch_out_type;
    private int working_hours;

    /**
     *
     */
    public LeftSliderMenu() {
        this.todayAppointments = 0;
        this.unreadMessagesCount = 0;
        this.punchDate="";
        this.punchInTime = "";
        this.punchOutTime = "";
        this.attendanceStatus=false;      
        this.attendanceTiemout=new AttendanceTimeout();
        this.userAccuracy=500;
        this.checkInRadius=500;
        this.allowTimeout=0;
        this.attendanceId=0;
        this.territoryLastUpdated=new Timestamp(0);
        this.userRole=0;
        this.allowOfflineOperation=0;
        this.locationInterval="";
        this.hasSubordinates=1;
        this.territoryCount=0;
        this.lastpunchInTime="";
        this.lastpunchOutTime="";
        this.lastcheckInTime="";
        this.lastcheckOutTime="";
        this.appointmentId=0;
        this.customerId=0;
        this.accountName="";
        
        this.lastPunchInDateTime = 0;
        this.lastPunchOutDateTime = 0;
        this.lastCheckOutDateTime = 0;
        this.lastCheckInDateTime = 0;
        this.appVersion = 0;
        this.auto_punch_out_time = "48";
        this.auto_punch_out_type = 0; // 0 for hour, 1 for specific time
        this.working_hours = 0;
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
    public int getTodayAppointments() {
        return todayAppointments;
    }

    /**
     *
     * @param todayAppointments
     */
    public void setTodayAppointments(int todayAppointments) {
        this.todayAppointments = todayAppointments;
    }

    /**
     *
     * @return
     */
    public int getUnreadMessagesCount() {
        return unreadMessagesCount;
    }

    /**
     *
     * @param unreadMessagesCount
     */
    public void setUnreadMessagesCount(int unreadMessagesCount) {
        this.unreadMessagesCount = unreadMessagesCount;
    }

    /**
     *
     * @return
     */
    public boolean isAttendanceStatus() {
        return attendanceStatus;
    }

    /**
     *
     * @param attendanceStatus
     */
    public void setAttendanceStatus(boolean attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    /**
     *
     * @return
     */
    public AttendanceTimeout getAttendanceTiemout() {
        return attendanceTiemout;
    }

    /**
     *
     * @param attendanceTiemout
     */
    public void setAttendanceTiemout(AttendanceTimeout attendanceTiemout) {
        this.attendanceTiemout = attendanceTiemout;
    }

    /**
     *
     * @return
     */
    public int getUserAccuracy() {
        return userAccuracy;
    }

    /**
     *
     * @param userAccuracy
     */
    public void setUserAccuracy(int userAccuracy) {
        this.userAccuracy = userAccuracy;
    }

    /**
     *
     * @return
     */
    public int getCheckInRadius() {
        return checkInRadius;
    }

    /**
     *
     * @param checkInRadius
     */
    public void setCheckInRadius(int checkInRadius) {
        this.checkInRadius = checkInRadius;
    }

    /**
     *
     * @return
     */
    public int getAllowTimeout() {
        return allowTimeout;
    }

    /**
     *
     * @param allowTimeout
     */
    public void setAllowTimeout(int allowTimeout) {
        this.allowTimeout = allowTimeout;
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
    public int getAttendanceId() {
        return attendanceId;
    }

    /**
     *
     * @param attendanceId
     */
    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    /**
     *
     * @return
     */
    public Timestamp getTerritoryLastUpdated() {
        return territoryLastUpdated;
    }

    /**
     *
     * @param territoryLastUpdated
     */
    public void setTerritoryLastUpdated(Timestamp territoryLastUpdated) {
        this.territoryLastUpdated = territoryLastUpdated;
    }

    /**
     *
     * @return
     */
    public int getUserRole() {
        return userRole;
    }

    /**
     *
     * @param userRole
     */
    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    /**
     *
     * @return
     */
    public int getAllowOfflineOperation() {
        return allowOfflineOperation;
    }

    /**
     *
     * @param allowOfflineOperation
     */
    public void setAllowOfflineOperation(int allowOfflineOperation) {
        this.allowOfflineOperation = allowOfflineOperation;
    }

    public int getTerritoryCount() {
        return territoryCount;
    }

    public void setTerritoryCount(int territoryCount) {
        this.territoryCount = territoryCount;
    }
    public String getLocationInterval() {
        return locationInterval;
    }

    public void setLocationInterval(String locationInterval) {
        this.locationInterval = locationInterval;
    }

    public int getHasSubordinates() {
        return hasSubordinates;
    }

    public void setHasSubordinates(int hasSubordinates) {
        this.hasSubordinates = hasSubordinates;
    }
    //nikhil

    public String getLastpunchInTime() {
        return lastpunchInTime;
    }

    public void setLastpunchInTime(String lastpunchInTime) {
        this.lastpunchInTime = lastpunchInTime;
    }

    public String getLastpunchOutTime() {
        return lastpunchOutTime;
    }

    public void setLastpunchOutTime(String lastpunchOutTime) {
        this.lastpunchOutTime = lastpunchOutTime;
    }

    public String getLastcheckInTime() {
        return lastcheckInTime;
    }

    public void setLastcheckInTime(String lastcheckInTime) {
        this.lastcheckInTime = lastcheckInTime;
    }

    public String getLastcheckOutTime() {
        return lastcheckOutTime;
    }

    public void setLastcheckOutTime(String lastcheckOutTime) {
        this.lastcheckOutTime = lastcheckOutTime;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public long getLastPunchInDateTime() {
        return lastPunchInDateTime;
    }

    public void setLastPunchInDateTime(long lastPunchInDateTime) {
        this.lastPunchInDateTime = lastPunchInDateTime;
    }

    public long getLastPunchOutDateTime() {
        return lastPunchOutDateTime;
    }

    public void setLastPunchOutDateTime(long lastPunchOutDateTime) {
        this.lastPunchOutDateTime = lastPunchOutDateTime;
    }

    public long getLastCheckInDateTime() {
        return lastCheckInDateTime;
    }

    public void setLastCheckInDateTime(long lastCheckInDateTime) {
        this.lastCheckInDateTime = lastCheckInDateTime;
    }

    public long getLastCheckOutDateTime() {
        return lastCheckOutDateTime;
    }

    public void setLastCheckOutDateTime(long lastCheckOutDateTime) {
        this.lastCheckOutDateTime = lastCheckOutDateTime;
    }

    public int getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(int appVersion) {
        this.appVersion = appVersion;
    }

    public String getAuto_punch_out_time() {
        return auto_punch_out_time;
    }

    public void setAuto_punch_out_time(String auto_punch_out_time) {
        this.auto_punch_out_time = auto_punch_out_time;
    }

    public int getAuto_punch_out_type() {
        return auto_punch_out_type;
    }

    public void setAuto_punch_out_type(int auto_punch_out_type) {
        this.auto_punch_out_type = auto_punch_out_type;
    }

    public int getWorking_hours() {
        return working_hours;
    }

    public void setWorking_hours(int working_hours) {
        this.working_hours = working_hours;
    }

}
