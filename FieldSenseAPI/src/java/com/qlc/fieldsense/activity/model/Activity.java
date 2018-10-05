/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.activity.model;

import com.qlc.fieldsense.activityPurpose.model.ActivityPurpose;
import com.qlc.fieldsense.customer.model.Customer;
import com.qlc.fieldsense.customerContact.model.CustomerContacts;
import com.qlc.fieldsense.user.model.User;
import java.sql.Timestamp;

/**
 *
 * @author anuja
 */
public class Activity {

    private int id;
    private String activity;
    private Customer customerId;
    private CustomerContacts customerContactId;
    private User ownerId;
    private User assignedId;
    private Timestamp activityDateTime;
    private String description;
    private ActivityPurpose purposeId;
    private int outComeId;
    private int status;
    private User createdBy;
    private Timestamp createdOn;
    private String activityDTime;

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
    public String getActivity() {
        return activity;
    }

    /**
     *
     * @param activity
     */
    public void setActivity(String activity) {
        this.activity = activity;
    }

    /**
     *
     * @return
     */
    public Customer getCustomerId() {
        return customerId;
    }

    /**
     *
     * @param customerId
     */
    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    /**
     *
     * @return
     */
    public CustomerContacts getCustomerContactId() {
        return customerContactId;
    }

    /**
     *
     * @param customerContactId
     */
    public void setCustomerContactId(CustomerContacts customerContactId) {
        this.customerContactId = customerContactId;
    }

    /**
     *
     * @return
     */
    public User getOwnerId() {
        return ownerId;
    }

    /**
     *
     * @param ownerId
     */
    public void setOwnerId(User ownerId) {
        this.ownerId = ownerId;
    }

    /**
     *
     * @return
     */
    public User getAssignedId() {
        return assignedId;
    }

    /**
     *
     * @param assignedId
     */
    public void setAssignedId(User assignedId) {
        this.assignedId = assignedId;
    }

    /**
     *
     * @return
     */
    public Timestamp getActivityDateTime() {
        return activityDateTime;
    }

    /**
     *
     * @param activityDateTime
     */
    public void setActivityDateTime(Timestamp activityDateTime) {
        this.activityDateTime = activityDateTime;
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
    public ActivityPurpose getPurposeId() {
        return purposeId;
    }

    /**
     *
     * @param purposeId
     */
    public void setPurposeId(ActivityPurpose purposeId) {
        this.purposeId = purposeId;
    }

    /**
     *
     * @return
     */
    public int getOutComeId() {
        return outComeId;
    }

    /**
     *
     * @param outComeId
     */
    public void setOutComeId(int outComeId) {
        this.outComeId = outComeId;
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
    public String getActivityDTime() {
        return activityDTime;
    }

    /**
     *
     * @param activityDTime
     */
    public void setActivityDTime(String activityDTime) {
        this.activityDTime = activityDTime;
    }

    /**
     *
     */
    public Activity() {
        this.id = 0;
        this.activity = "";
        this.customerId = new Customer();
        this.customerContactId = new CustomerContacts();
        this.ownerId = new User();
        this.assignedId = new User();
        this.activityDateTime = new Timestamp(0);
        this.description = "";
        this.purposeId = new ActivityPurpose();
        this.outComeId = 0;
        this.status = 0;
        this.createdBy = new User();
        this.createdOn = new Timestamp(0);
        this.activityDTime = "";
    }
}
