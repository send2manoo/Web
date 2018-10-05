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
public class Location {

    private int id;
    private double latitude;
    private double langitude;
    private int locationType;// 0-office ,-1 - home , other - for customer
    private Timestamp createdOn;
    private int userId;

    /**
     *
     */
    public Location() {
        this.id = 0;
        this.langitude = 0d;
        this.langitude = 0d;
        this.locationType = -2;
        this.createdOn = new Timestamp(0);
        this.userId = 0;
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
    public double getLangitude() {
        return langitude;
    }

    /**
     *
     * @param langitude
     */
    public void setLangitude(double langitude) {
        this.langitude = langitude;
    }

    /**
     *
     * @return
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return
     */
    public int getLocationType() {
        return locationType;
    }

    /**
     *
     * @param locationType
     */
    public void setLocationType(int locationType) {
        this.locationType = locationType;
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
