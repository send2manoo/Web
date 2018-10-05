/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.appointments.model;

import com.qlc.fieldsense.user.model.User;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jyoti
 */
public class AppointmentRecurringList {
    
    private int id;
    private int repeatType;
    private int repeatAfterEvery;
    private List<Integer> repeatOnDay_weekly;
    private List<Integer> repeatOnDate_monthly;
    private String repeatOnDayWeeklyCSV;
    private String repeatOnDateMonthlyCSV;
    private String recurrTill_Date_startTime;
    private String recurrTill_Date_endTime;
    private Timestamp endDateStartTime;
    private Timestamp endDateEndTime;
    
    /**
     *
     */
    public AppointmentRecurringList(){
        
        this.id = 0;
        this.repeatType = 0;
        this.repeatAfterEvery = 1;
        this.repeatOnDay_weekly = new ArrayList<Integer>();
        this.repeatOnDate_monthly = new ArrayList<Integer>();
        this.recurrTill_Date_startTime = "";
        this.recurrTill_Date_endTime = "";
        this.endDateStartTime = new Timestamp(0);
        this.endDateEndTime = new Timestamp(0);
        this.repeatOnDayWeeklyCSV = "";
        this.repeatOnDateMonthlyCSV = "";
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
    public int getRepeatType() {
        return repeatType;
    }

    /**
     *
     * @param repeatType
     */
    public void setRepeatType(int repeatType) {
        this.repeatType = repeatType;
    }

    /**
     *
     * @return
     */
    public int getRepeatAfterEvery() {
        return repeatAfterEvery;
    }

    /**
     *
     * @param repeatAfterEvery
     */
    public void setRepeatAfterEvery(int repeatAfterEvery) {
        this.repeatAfterEvery = repeatAfterEvery;
    }

    /**
     *
     * @return
     */
    public List<Integer> getRepeatOnDay_weekly() {
        return repeatOnDay_weekly;
    }

    /**
     *
     * @param repeatOnDay_weekly
     */
    public void setRepeatOnDay_weekly(List<Integer> repeatOnDay_weekly) {
        this.repeatOnDay_weekly = repeatOnDay_weekly;
    }

    /**
     *
     * @return
     */
    public List<Integer> getRepeatOnDate_monthly() {
        return repeatOnDate_monthly;
    }

    /**
     *
     * @param repeatOnDate_monthly
     */
    public void setRepeatOnDate_monthly(List<Integer> repeatOnDate_monthly) {
        this.repeatOnDate_monthly = repeatOnDate_monthly;
    }

    /**
     *
     * @return
     */
    public String getRecurrTill_Date_startTime() {
        return recurrTill_Date_startTime;
    }

    /**
     *
     * @param recurrTill_Date_startTime
     */
    public void setRecurrTill_Date_startTime(String recurrTill_Date_startTime) {
        this.recurrTill_Date_startTime = recurrTill_Date_startTime;
    }

    /**
     *
     * @return
     */
    public String getRecurrTill_Date_endTime() {
        return recurrTill_Date_endTime;
    }

    /**
     *
     * @param recurrTill_Date_endTime
     */
    public void setRecurrTill_Date_endTime(String recurrTill_Date_endTime) {
        this.recurrTill_Date_endTime = recurrTill_Date_endTime;
    }

    /**
     *
     * @return
     */
    public String getRepeatOnDayWeeklyCSV() {
        return repeatOnDayWeeklyCSV;
    }

    /**
     *
     * @param repeatOnDayWeeklyCSV
     */
    public void setRepeatOnDayWeeklyCSV(String repeatOnDayWeeklyCSV) {
        this.repeatOnDayWeeklyCSV = repeatOnDayWeeklyCSV;
    }

    /**
     *
     * @return
     */
    public String getRepeatOnDateMonthlyCSV() {
        return repeatOnDateMonthlyCSV;
    }

    /**
     *
     * @param repeatOnDateMonthlyCSV
     */
    public void setRepeatOnDateMonthlyCSV(String repeatOnDateMonthlyCSV) {
        this.repeatOnDateMonthlyCSV = repeatOnDateMonthlyCSV;
    }

    /**
     *
     * @return
     */
    public Timestamp getEndDateStartTime() {
        return endDateStartTime;
    }

    /**
     *
     * @param endDateStartTime
     */
    public void setEndDateStartTime(Timestamp endDateStartTime) {
        this.endDateStartTime = endDateStartTime;
    }

    /**
     *
     * @return
     */
    public Timestamp getEndDateEndTime() {
        return endDateEndTime;
    }

    /**
     *
     * @param endDateEndTime
     */
    public void setEndDateEndTime(Timestamp endDateEndTime) {
        this.endDateEndTime = endDateEndTime;
    }
    
}
