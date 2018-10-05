/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customerOffline.model;

import com.qlc.fieldsense.activityOutcome.model.ActivityOutcome;
import com.qlc.fieldsense.activityPurpose.model.ActivityPurpose;
import com.qlc.fieldsense.customer.model.Customer;
import com.qlc.fieldsense.customerContact.model.CustomerContacts;
import com.qlc.fieldsense.user.model.User;
import java.sql.Timestamp;

/**
 *
 * @author siddhesh
 */
public class AppointmentOffline {
  
    private String id;
    private String title;
    private Timestamp dateTime;
    private String sdateTime;
    private Timestamp endTime;
    private String sendTime;
    private Customer customerPermanent;
    private CustomerInsertOffline customer;
    private CustomerContacts customerContact;
    private User owner;
    private User assignedTo;
    private String description;
    private int type;
    private ActivityPurpose purpose;
    private ActivityOutcome outcomes;
    private int outcome;
    private int status;     // 0 pending, 1 inprogress/check-in, 2 completed/check-out, 3 cancelled
    private double checkInLat;
    private double checkInLong;
    private String checkInLocation;
    private Timestamp checkInTime;
    private String scheckInTime;
    private double checkOutLat;
    private double checkOutLong;
    private String checkOutLocation;
    private Timestamp checkOutTime;
    private String scheckOutTime;
    private int nextAppointment;
    private String nextAppointmentStr;
    private Timestamp createdOn;
    private User createdBy;
    private int appointmentPosition;
    private Timestamp nextAppointmentTime;
    private int nextAppointmentType;
    private boolean hasNextAppointment;
    private String outcomeDescription;
    private int recordState;
    //added by shivakrishna for newly added columns in customers table
    private Timestamp updated_on;
    private User updated_by;

    /**
     *
     */
    public AppointmentOffline() {
        this.id = "";
        this.title = "";
        this.dateTime = new Timestamp(0);
        this.sdateTime = "";
        this.endTime = new Timestamp(0);
        this.sendTime = "";
        this.customerPermanent=new Customer();
        this.customer = new CustomerInsertOffline();
        this.customerContact = new CustomerContacts();
        this.owner = new User();
        this.assignedTo = new User();
        this.description = "";
        this.type = 0;
        this.purpose = new ActivityPurpose();
        this.outcomes = new ActivityOutcome();
        this.outcome = 0;
        this.status = 0;
        this.checkInLat = 0;
        this.checkInLong = 0;
        this.checkInLocation = "";
        this.checkInTime = new Timestamp(0);
        this.scheckInTime = "";
        this.checkOutLat = 0;
        this.checkOutLong = 0;
        this.checkOutLocation = "";
        this.checkOutTime = new Timestamp(0);
        this.scheckOutTime = "";
        this.nextAppointment = 0;
        this.createdOn = new Timestamp(0);
        this.createdBy = new User();
        this.appointmentPosition = 0;
        this.nextAppointmentTime = new Timestamp(0);
        this.nextAppointmentType = 0;
        this.hasNextAppointment = false;
        this.outcomeDescription = "";
        this.updated_on = new Timestamp(0);
        this.updated_by = new User();
        this.recordState=0;
        this.nextAppointmentStr="";
    }

    /**
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     */
    public Timestamp getDateTime() {
        return dateTime;
    }

    /**
     *
     * @param dateTime
     */
    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    /**
     *
     * @return
     */
    public String getSdateTime() {
        return sdateTime;
    }

    /**
     *
     * @param sdateTime
     */
    public void setSdateTime(String sdateTime) {
        this.sdateTime = sdateTime;
    }

    /**
     *
     * @return
     */
    public Timestamp getEndTime() {
        return endTime;
    }

    /**
     *
     * @param endTime
     */
    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    /**
     *
     * @return
     */
    public String getSendTime() {
        return sendTime;
    }

    /**
     *
     * @param sendTime
     */
    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    /**
     *
     * @return
     */
    public CustomerInsertOffline getCustomer() {
        return customer;
    }

    /**
     *
     * @param customer
     */
    public void setCustomer(CustomerInsertOffline customer) {
        this.customer = customer;
    }

    /**
     *
     * @return
     */
    public CustomerContacts getCustomerContact() {
        return customerContact;
    }

    /**
     *
     * @param customerContact
     */
    public void setCustomerContact(CustomerContacts customerContact) {
        this.customerContact = customerContact;
    }

    /**
     *
     * @return
     */
    public User getOwner() {
        return owner;
    }

    /**
     *
     * @param owner
     */
    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
     *
     * @return
     */
    public User getAssignedTo() {
        return assignedTo;
    }

    /**
     *
     * @param assignedTo
     */
    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     */
    public int getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public ActivityPurpose getPurpose() {
        return purpose;
    }

    /**
     *
     * @param purpose
     */
    public void setPurpose(ActivityPurpose purpose) {
        this.purpose = purpose;
    }

    /**
     *
     * @return
     */
    public int getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     *
     * @return
     */
    public double getCheckInLat() {
        return checkInLat;
    }

    /**
     *
     * @param checkInLat
     */
    public void setCheckInLat(double checkInLat) {
        this.checkInLat = checkInLat;
    }

    /**
     *
     * @return
     */
    public double getCheckInLong() {
        return checkInLong;
    }

    /**
     *
     * @param checkInLong
     */
    public void setCheckInLong(double checkInLong) {
        this.checkInLong = checkInLong;
    }

    /**
     *
     * @return
     */
    public String getCheckInLocation() {
        return checkInLocation;
    }

    /**
     *
     * @param checkInLocation
     */
    public void setCheckInLocation(String checkInLocation) {
        this.checkInLocation = checkInLocation;
    }

    /**
     *
     * @return
     */
    public Timestamp getCheckInTime() {
        return checkInTime;
    }

    /**
     *
     * @param checkInTime
     */
    public void setCheckInTime(Timestamp checkInTime) {
        this.checkInTime = checkInTime;
    }

    /**
     *
     * @return
     */
    public String getScheckInTime() {
        return scheckInTime;
    }

    /**
     *
     * @param scheckInTime
     */
    public void setScheckInTime(String scheckInTime) {
        this.scheckInTime = scheckInTime;
    }

    /**
     *
     * @return
     */
    public double getCheckOutLat() {
        return checkOutLat;
    }

    /**
     *
     * @param checkOutLat
     */
    public void setCheckOutLat(double checkOutLat) {
        this.checkOutLat = checkOutLat;
    }

    /**
     *
     * @return
     */
    public double getCheckOutLong() {
        return checkOutLong;
    }

    /**
     *
     * @param checkOutLong
     */
    public void setCheckOutLong(double checkOutLong) {
        this.checkOutLong = checkOutLong;
    }

    /**
     *
     * @return
     */
    public String getCheckOutLocation() {
        return checkOutLocation;
    }

    /**
     *
     * @param checkOutLocation
     */
    public void setCheckOutLocation(String checkOutLocation) {
        this.checkOutLocation = checkOutLocation;
    }

    /**
     *
     * @return
     */
    public Timestamp getCheckOutTime() {
        return checkOutTime;
    }

    /**
     *
     * @param checkOutTime
     */
    public void setCheckOutTime(Timestamp checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    /**
     *
     * @return
     */
    public String getScheckOutTime() {
        return scheckOutTime;
    }

    /**
     *
     * @param scheckOutTime
     */
    public void setScheckOutTime(String scheckOutTime) {
        this.scheckOutTime = scheckOutTime;
    }

    /**
     *
     * @return
     */
    public int getNextAppointment() {
        return nextAppointment;
    }

    /**
     *
     * @param nextAppointment
     */
    public void setNextAppointment(int nextAppointment) {
        this.nextAppointment = nextAppointment;
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
    public User getCreatedBy() {
        return createdBy;
    }

    /**
     *
     * @param createdBy
     */
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    /**
     *
     * @return
     */
    public int getAppointmentPosition() {
        return appointmentPosition;
    }

    /**
     *
     * @param appointmentPosition
     */
    public void setAppointmentPosition(int appointmentPosition) {
        this.appointmentPosition = appointmentPosition;
    }

    /**
     *
     * @return
     */
    public Timestamp getNextAppointmentTime() {
        return nextAppointmentTime;
    }

    /**
     *
     * @param nextAppointmentTime
     */
    public void setNextAppointmentTime(Timestamp nextAppointmentTime) {
        this.nextAppointmentTime = nextAppointmentTime;
    }

    /**
     *
     * @return
     */
    public int getNextAppointmentType() {
        return nextAppointmentType;
    }

    /**
     *
     * @param nextAppointmentType
     */
    public void setNextAppointmentType(int nextAppointmentType) {
        this.nextAppointmentType = nextAppointmentType;
    }

    /**
     *
     * @return
     */
    public int getOutcome() {
        return outcome;
    }

    /**
     *
     * @param outcome
     */
    public void setOutcome(int outcome) {
        this.outcome = outcome;
    }

    /**
     *
     * @return
     */
    public ActivityOutcome getOutcomes() {
        return outcomes;
    }

    /**
     *
     * @param outcomes
     */
    public void setOutcomes(ActivityOutcome outcomes) {
        this.outcomes = outcomes;
    }

    /**
     *
     * @return
     */
    public boolean isHasNextAppointment() {
        return hasNextAppointment;
    }

    /**
     *
     * @param hasNextAppointment
     */
    public void setHasNextAppointment(boolean hasNextAppointment) {
        this.hasNextAppointment = hasNextAppointment;
    }

    /**
     *
     * @return
     */
    public String getOutcomeDescription() {
        return outcomeDescription;
    }

    /**
     *
     * @param outcomeDescription
     */
    public void setOutcomeDescription(String outcomeDescription) {
        this.outcomeDescription = outcomeDescription;
    }

    /**
     *
     * @return
     */
    public Timestamp getUpdated_on() {
        return updated_on;
    }

    /**
     *
     * @param updated_on
     */
    public void setUpdated_on(Timestamp updated_on) {
        this.updated_on = updated_on;
    }

    /**
     *
     * @return
     */
    public User getUpdated_by() {
        return updated_by;
    }

    /**
     *
     * @param updated_by
     */
    public void setUpdated_by(User updated_by) {
        this.updated_by = updated_by;
    }

    /**
     *
     * @return
     */
    public int getRecordState() {
        return recordState;
    }

    /**
     *
     * @param recordState
     */
    public void setRecordState(int recordState) {
        this.recordState = recordState;
    }

    @Override
    public String toString() {
        return "AppointmentOffline{" + "id=" + id + ", title=" + title + ", dateTime=" + dateTime + ", sdateTime=" + sdateTime + ", endTime=" + endTime + ", sendTime=" + sendTime + ", customer=" + customer + ", customerContact=" + customerContact + ", owner=" + owner + ", assignedTo=" + assignedTo + ", description=" + description + ", type=" + type + ", purpose=" + purpose + ", outcomes=" + outcomes + ", outcome=" + outcome + ", status=" + status + ", checkInLat=" + checkInLat + ", checkInLong=" + checkInLong + ", checkInLocation=" + checkInLocation + ", checkInTime=" + checkInTime + ", scheckInTime=" + scheckInTime + ", checkOutLat=" + checkOutLat + ", checkOutLong=" + checkOutLong + ", checkOutLocation=" + checkOutLocation + ", checkOutTime=" + checkOutTime + ", scheckOutTime=" + scheckOutTime + ", nextAppointment=" + nextAppointment + ", createdOn=" + createdOn + ", createdBy=" + createdBy + ", appointmentPosition=" + appointmentPosition + ", nextAppointmentTime=" + nextAppointmentTime + ", nextAppointmentType=" + nextAppointmentType + ", hasNextAppointment=" + hasNextAppointment + ", outcomeDescription=" + outcomeDescription + ", recordState=" + recordState + ", updated_on=" + updated_on + ", updated_by=" + updated_by + '}';
    }

    /**
     *
     * @return
     */
    public Customer getCustomerPermanent() {
        return customerPermanent;
    }

    /**
     *
     * @param customerPermanent
     */
    public void setCustomerPermanent(Customer customerPermanent) {
        this.customerPermanent = customerPermanent;
    }

    public String getNextAppointmentStr() {
        return nextAppointmentStr;
    }

    public void setNextAppointmentStr(String nextAppointmentStr) {
        this.nextAppointmentStr = nextAppointmentStr;
    }

      

     
}

    
