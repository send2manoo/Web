/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.attendance.model;

/**
 *
 * @author Ramesh
 * @date 28-06-2014
 */
public class PunchDateTime {

    private String date;
    private String time;
    private String punchOutTime;
    private String punchOutDate;
    private boolean isUseInMeeting;

    /**
     *
     */
    public PunchDateTime() {
        this.date = "";
        this.time = "";
        this.punchOutTime = "";
        this.isUseInMeeting = false;
        this.punchOutDate="";
    }

    /**
     *
     * @return
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *
     * @return
     */
    public String getTime() {
        return time;
    }

    /**
     *
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
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
    public boolean isIsUseInMeeting() {
        return isUseInMeeting;
    }

    /**
     *
     * @param isUseInMeeting
     */
    public void setIsUseInMeeting(boolean isUseInMeeting) {
        this.isUseInMeeting = isUseInMeeting;
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
}
