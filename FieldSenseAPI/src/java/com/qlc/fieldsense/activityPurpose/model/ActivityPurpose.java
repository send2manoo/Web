/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.activityPurpose.model;

import com.qlc.fieldsense.user.model.User;
import java.sql.Timestamp;

/**
 *
 * @author anuja
 */
public class ActivityPurpose {

    private int id;
    private String purpose;
    private boolean isActive;
    private Timestamp cretedOn;
    private User createdBy;
    private int totalActivityPurpose;

    /**
     *
     */
    public ActivityPurpose() {
        this.id = 0;
        this.purpose = "";
        this.isActive = false;
        this.cretedOn = new Timestamp(0);
        this.createdBy = new User();
        this.totalActivityPurpose = 0;
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
    public String getPurpose() {
        return purpose;
    }

    /**
     *
     * @param purpose
     */
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    /**
     *
     * @return
     */
    public boolean isIsActive() {
        return isActive;
    }

    /**
     *
     * @param isActive
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     *
     * @return
     */
    public Timestamp getCretedOn() {
        return cretedOn;
    }

    /**
     *
     * @param cretedOn
     */
    public void setCretedOn(Timestamp cretedOn) {
        this.cretedOn = cretedOn;
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
    public int getTotalActivityPurpose() {
        return totalActivityPurpose;
    }

    /**
     *
     * @param totalActivityPurpose
     */
    public void setTotalActivityPurpose(int totalActivityPurpose) {
        this.totalActivityPurpose = totalActivityPurpose;
    }
}
