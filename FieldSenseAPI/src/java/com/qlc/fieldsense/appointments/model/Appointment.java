/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.appointments.model;

import com.qlc.fieldsense.activityOutcome.model.ActivityOutcome;
import com.qlc.fieldsense.activityPurpose.model.ActivityPurpose;
import com.qlc.fieldsense.customer.model.Customer;
import com.qlc.fieldsense.customerContact.model.CustomerContacts;
import com.qlc.fieldsense.user.model.User;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ramesh
 * @date 21-02-2014
 * @modified by: anuja
 */
public class Appointment {

    private int id;
    private int mobileTempId;
    private String title;
    private Timestamp dateTime;
    private String sdateTime;
    private Timestamp endTime;
    private String sendTime;
    private Customer customer;
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
    private Timestamp createdOn;
    private String createdOnString; //by nikhil
    private User createdBy;
    private int appointmentPosition;
    private Timestamp nextAppointmentTime;
    private int nextAppointmentType;
    private boolean hasNextAppointment;
    private String outcomeDescription;

    //added by shivakrishna for newly added columns in customers table
    private Timestamp updated_on;
    private User updated_by;
    //Added by Jyoti 01-02-2017, recurring fields
    private int isRecurring;
    private AppointmentRecurringList appointmentRecurringList;
    
    //Added by jyoti
    private List<Integer> assignedToList; // for storing list of users visits to be added
    private String visitsNotAddedForUsers;
    private int recordState;
    
    public Appointment() {
        this.id = 0;
        this.title = "";
        this.dateTime = new Timestamp(0);
        this.sdateTime = "";
        this.endTime = new Timestamp(0);
        this.sendTime = "";
        this.customer = new Customer();
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
        this.mobileTempId = 0;
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
        this.assignedToList = new ArrayList<Integer>();
        this.visitsNotAddedForUsers = "";
        this.isRecurring = 0;
        this.createdOnString ="";
        this.appointmentRecurringList = new AppointmentRecurringList();
        this.recordState=0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public String getSdateTime() {
        return sdateTime;
    }

    public void setSdateTime(String sdateTime) {
        this.sdateTime = sdateTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public CustomerContacts getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(CustomerContacts customerContact) {
        this.customerContact = customerContact;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ActivityPurpose getPurpose() {
        return purpose;
    }

    public void setPurpose(ActivityPurpose purpose) {
        this.purpose = purpose;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getCheckInLat() {
        return checkInLat;
    }

    public void setCheckInLat(double checkInLat) {
        this.checkInLat = checkInLat;
    }

    public double getCheckInLong() {
        return checkInLong;
    }

    public void setCheckInLong(double checkInLong) {
        this.checkInLong = checkInLong;
    }

    public String getCheckInLocation() {
        return checkInLocation;
    }

    public void setCheckInLocation(String checkInLocation) {
        this.checkInLocation = checkInLocation;
    }

    public Timestamp getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Timestamp checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getScheckInTime() {
        return scheckInTime;
    }

    public void setScheckInTime(String scheckInTime) {
        this.scheckInTime = scheckInTime;
    }

    public double getCheckOutLat() {
        return checkOutLat;
    }

    public void setCheckOutLat(double checkOutLat) {
        this.checkOutLat = checkOutLat;
    }

    public double getCheckOutLong() {
        return checkOutLong;
    }

    public void setCheckOutLong(double checkOutLong) {
        this.checkOutLong = checkOutLong;
    }

    public String getCheckOutLocation() {
        return checkOutLocation;
    }

    public void setCheckOutLocation(String checkOutLocation) {
        this.checkOutLocation = checkOutLocation;
    }

    public Timestamp getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(Timestamp checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getScheckOutTime() {
        return scheckOutTime;
    }

    public void setScheckOutTime(String scheckOutTime) {
        this.scheckOutTime = scheckOutTime;
    }

    public int getNextAppointment() {
        return nextAppointment;
    }

    public void setNextAppointment(int nextAppointment) {
        this.nextAppointment = nextAppointment;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public int getAppointmentPosition() {
        return appointmentPosition;
    }

    public void setAppointmentPosition(int appointmentPosition) {
        this.appointmentPosition = appointmentPosition;
    }

    public Timestamp getNextAppointmentTime() {
        return nextAppointmentTime;
    }

    public void setNextAppointmentTime(Timestamp nextAppointmentTime) {
        this.nextAppointmentTime = nextAppointmentTime;
    }

    public int getNextAppointmentType() {
        return nextAppointmentType;
    }

    public void setNextAppointmentType(int nextAppointmentType) {
        this.nextAppointmentType = nextAppointmentType;
    }

    public int getOutcome() {
        return outcome;
    }

    public void setOutcome(int outcome) {
        this.outcome = outcome;
    }

    public ActivityOutcome getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(ActivityOutcome outcomes) {
        this.outcomes = outcomes;
    }

    public boolean isHasNextAppointment() {
        return hasNextAppointment;
    }

    public void setHasNextAppointment(boolean hasNextAppointment) {
        this.hasNextAppointment = hasNextAppointment;
    }

    public String getOutcomeDescription() {
        return outcomeDescription;
    }

    public void setOutcomeDescription(String outcomeDescription) {
        this.outcomeDescription = outcomeDescription;
    }

    public Timestamp getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(Timestamp updated_on) {
        this.updated_on = updated_on;
    }

    public User getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(User updated_by) {
        this.updated_by = updated_by;
    }

    public int getMobileTempId() {
        return mobileTempId;
    }

    public void setMobileTempId(int mobileTempId) {
        this.mobileTempId = mobileTempId;
    }

    public List<Integer> getAssignedToList() {
        return assignedToList;
    }

    public void setAssignedToList(List<Integer> assignedToList) {
        this.assignedToList = assignedToList;
    }

    public String getVisitsNotAddedForUsers() {
        return visitsNotAddedForUsers;
    }

    public void setVisitsNotAddedForUsers(String visitsNotAddedForUsers) {
        this.visitsNotAddedForUsers = visitsNotAddedForUsers;
    }

    public int getIsRecurring() {
        return isRecurring;
    }

    public void setIsRecurring(int isRecurring) {
        this.isRecurring = isRecurring;
    }

    public AppointmentRecurringList getAppointmentRecurringList() {
        return appointmentRecurringList;
    }

    public void setAppointmentRecurringList(AppointmentRecurringList appointmentRecurringList) {
        this.appointmentRecurringList = appointmentRecurringList;
    }

    public String getCreatedOnString() {
        return createdOnString;
    }

    public void setCreatedOnString(String createdOnString) {
        this.createdOnString = createdOnString;
    }

    public int getRecordState() {
        return recordState;
    }

    public void setRecordState(int recordState) {
        this.recordState = recordState;
    }

}
