/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.location.model;

import java.sql.Timestamp;

/**
 *
 * @author Ramesh
 * @date 06-03-2014
 */
public class LocationMap {

    private int id;
    private int userId;
    private int locationId;
    private Timestamp cretedOn;

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
    public int getLocationId() {
        return locationId;
    }

    /**
     *
     * @param locationId
     */
    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    /**
     *
     * @return
     */
    public int getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
