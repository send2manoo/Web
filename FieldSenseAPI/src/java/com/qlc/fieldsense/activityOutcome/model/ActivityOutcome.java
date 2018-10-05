/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.activityOutcome.model;

import com.qlc.fieldsense.user.model.User;
import java.sql.Timestamp;

/**
 *
 * @author anuja
 */
public class ActivityOutcome {

    private int id;
    private String outcome;
    private int isPositive;
    private int isActive;
    private Timestamp createdOn;
    private User createdBy;

    /**
     *
     */
    public ActivityOutcome() {
        this.id = 0;
        this.outcome = "";
        this.isPositive = 0;
        this.isActive = 0;
        this.createdOn = new Timestamp(0);
        this.createdBy = new User();
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
    public String getOutcome() {
        return outcome;
    }

    /**
     *
     * @param outcome
     */
    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    /**
     *
     * @return
     */
    public int getIsPositive() {
        return isPositive;
    }

    /**
     *
     * @param isPositive
     */
    public void setIsPositive(int isPositive) {
        this.isPositive = isPositive;
    }

    /**
     *
     * @return
     */
    public int getIsActive() {
        return isActive;
    }

    /**
     *
     * @param isActive
     */
    public void setIsActive(int isActive) {
        this.isActive = isActive;
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

}
