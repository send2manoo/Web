/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.appointments.model;

import com.qlc.fieldsense.activityPurpose.model.ActivityPurpose;
import com.qlc.fieldsense.user.model.User;
import java.sql.Timestamp;

/**
 *
 * @author siddhesh
 */
public class Appointments {
    
    private int id;
    private String title;
    private Timestamp dateTime;
    private String sdateTime;
    private Timestamp endTime;
    private String sendTime;
//    private User assignedTo;
    private String description;
    private int type;
    //private ActivityPurpose purpose;
    private int outcome;
    private int status;     // 0 pending, 1 inprogress/check-in, 2 completed/check-out, 3 cancelled
    private Timestamp checkInTime;
    private String scheckInTime;
    private Timestamp checkOutTime;
    private String scheckOutTime;
    private int nextAppointment;
    private Timestamp createdOn;
    private int appointmentPosition;
    private Timestamp nextAppointmentTime;
    private int nextAppointmentType;
    private boolean hasNextAppointment;
    private String outcomeDescription;
    private int assignedId;
    private String assignedUserFirstName;
    private String assignedUserLastName;
    private String assignedUserEmailId;
    private int purposeId;
    private String purpose;
    //added by shivakrishna for newly added columns in customers table
   
    public Appointments() {
        this.id = 0;
        this.title = "";
        this.dateTime = new Timestamp(0);
        this.sdateTime = "";
        this.endTime = new Timestamp(0);
        this.sendTime = "";
        this.description = "";
        this.type = 0;
        this.outcome = 0;
        this.status = 0;
        this.checkInTime = new Timestamp(0);
        this.scheckInTime = "";
        this.checkOutTime = new Timestamp(0);
        this.scheckOutTime = "";
        this.nextAppointment = 0;
        this.createdOn = new Timestamp(0);
        this.appointmentPosition = 0;
        this.nextAppointmentTime = new Timestamp(0);
        this.nextAppointmentType = 0;
        this.hasNextAppointment = false;
        this.outcomeDescription = "";
        this.assignedId=0;
        this.assignedUserEmailId="";
        this.assignedUserFirstName="";
        this.assignedUserLastName="";
        this.purposeId=0;
        this.purpose="";
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

    
    public int getOutcome() {
        return outcome;
    }

    public void setOutcome(int outcome) {
        this.outcome = outcome;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getAssignedId() {
        return assignedId;
    }

    public void setAssignedId(int assignedId) {
        this.assignedId = assignedId;
    }

    public String getAssignedUserFirstName() {
        return assignedUserFirstName;
    }

    public void setAssignedUserFirstName(String assignedUserFirstName) {
        this.assignedUserFirstName = assignedUserFirstName;
    }

    public String getAssignedUserLastName() {
        return assignedUserLastName;
    }

    public void setAssignedUserLastName(String assignedUserLastName) {
        this.assignedUserLastName = assignedUserLastName;
    }

    public String getAssignedUserEmailId() {
        return assignedUserEmailId;
    }

    public void setAssignedUserEmailId(String assignedUserEmailId) {
        this.assignedUserEmailId = assignedUserEmailId;
    }

    public int getPurposeId() {
        return purposeId;
    }

    public void setPurposeId(int purposeId) {
        this.purposeId = purposeId;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

}