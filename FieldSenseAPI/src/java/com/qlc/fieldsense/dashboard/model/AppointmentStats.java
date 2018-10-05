/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.dashboard.model;
import com.qlc.fieldsense.appointments.model.Appointment;
import com.qlc.fieldsense.customer.model.Customer;
import com.qlc.fieldsense.user.model.User;
import java.sql.Timestamp;
/**
 *
 * @author manohar
 */
public class AppointmentStats {

    private Appointment appointment;
    private Customer customer;
    private User user;
    private String subordinateName;
    private String appTime; 
    

    public AppointmentStats() {
        this.appointment=null;
        this.customer = null;
        this.user = null;
        this.subordinateName="";
        this.appTime=null;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSubordinateName() {
        return subordinateName;
    }

    public void setSubordinateName(String subordinateName) {
        this.subordinateName = subordinateName;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public String getTime() {
        return appTime;
    }

    public void setTime(String appTime) {
        this.appTime = appTime;
    }
   
}
