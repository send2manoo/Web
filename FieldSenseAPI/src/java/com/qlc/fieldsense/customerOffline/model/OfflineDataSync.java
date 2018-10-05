/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customerOffline.model;

import com.qlc.fieldsense.appointments.model.Appointment;
import com.qlc.fieldsense.formbuilder.model.CustomForm;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author siddhesh
 */
public class OfflineDataSync {
    
    private List<CustomerOffline> customer;
    private List<Appointment> appointments;
    private List<LocalData> localData;
    private String lastSyncTime;
    private String currentTime;
    private int syncState;
    private CustomForm[] formdetails;
    /**
     *
     */
    public OfflineDataSync(){

    customer=new ArrayList<CustomerOffline>();
    appointments=new ArrayList<Appointment>();
    localData=new ArrayList<LocalData>();
    lastSyncTime="";
    currentTime="";
    syncState=0;
}

    /**
     *
     * @return
     */
    public List<CustomerOffline> getCustomer() {
        return customer;
    }

    /**
     *
     * @param customer
     */
    public void setCustomer(List<CustomerOffline> customer) {
        this.customer = customer;
    }

    /**
     *
     * @return
     */
    public List<Appointment> getAppointments() {
        return appointments;
    }

    /**
     *
     * @param appointments
     */
    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    /**
     *
     * @return
     */
    public String getCurrentTime() {
        return currentTime;
    }

    /**
     *
     * @param currentTime
     */
    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    /**
     *
     * @return
     */
    public String getLastSyncTime() {
        return lastSyncTime;
    }

    /**
     *
     * @param lastSyncTime
     */
    public void setLastSyncTime(String lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    /**
     *
     * @return
     */
    public List<LocalData> getLocalData() {
        return localData;
    }

    /**
     *
     * @param localData
     */
    public void setLocalData(List<LocalData> localData) {
        this.localData = localData;
    }

    public int getSyncState() {
        return syncState;
    }

    public void setSyncState(int syncState) {
        this.syncState = syncState;
    }

    public CustomForm[] getFormdetails() {
        return formdetails;
    }

    public void setFormdetails(CustomForm[] formdetails) {
        this.formdetails = formdetails;
    }

}
