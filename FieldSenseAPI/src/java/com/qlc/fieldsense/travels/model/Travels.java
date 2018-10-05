/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.travels.model;

import com.qlc.fieldsense.customAnnotations.TrimSpaceValidation;
import java.sql.Timestamp;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author anuja
 */
public class Travels {
    private int id;
    @NotNull
    private Integer userId;
    private Timestamp startedOn;
    @NotEmpty
    @Length(min = 1, max = 100)
    @TrimSpaceValidation(message = "Please enter valid data")
    private String startLocation;
    @NotEmpty
    @Length(min = 1, max = 50)
    @TrimSpaceValidation(message = "Please enter valid data")
    private String startLocationLatlong;
    private Timestamp endedOn;
    @NotEmpty
    @Length(min = 1, max = 1000)
    @TrimSpaceValidation(message = "Please enter valid data")
    private String endLocation;
    @NotEmpty
    @Length(min = 1, max = 50)
    @TrimSpaceValidation(message = "Please enter valid data")
    private String endLocationLatlong;
    @NotNull
    private Integer mobileNo;
    @NotNull
    private Integer imeiNo;
    private Timestamp createdOn;

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
    public Integer getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     */
    public Timestamp getStartedOn() {
        return startedOn;
    }

    /**
     *
     * @param startedOn
     */
    public void setStartedOn(Timestamp startedOn) {
        this.startedOn = startedOn;
    }

    /**
     *
     * @return
     */
    public String getStartLocation() {
        return startLocation;
    }

    /**
     *
     * @param startLocation
     */
    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    /**
     *
     * @return
     */
    public String getStartLocationLatlong() {
        return startLocationLatlong;
    }

    /**
     *
     * @param startLocationLatlong
     */
    public void setStartLocationLatlong(String startLocationLatlong) {
        this.startLocationLatlong = startLocationLatlong;
    }

    /**
     *
     * @return
     */
    public Timestamp getEndedOn() {
        return endedOn;
    }

    /**
     *
     * @param endedOn
     */
    public void setEndedOn(Timestamp endedOn) {
        this.endedOn = endedOn;
    }

    /**
     *
     * @return
     */
    public String getEndLocation() {
        return endLocation;
    }

    /**
     *
     * @param endLocation
     */
    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    /**
     *
     * @return
     */
    public String getEndLocationLatlong() {
        return endLocationLatlong;
    }

    /**
     *
     * @param endLocationLatlong
     */
    public void setEndLocationLatlong(String endLocationLatlong) {
        this.endLocationLatlong = endLocationLatlong;
    }

    /**
     *
     * @return
     */
    public Integer getMobileNo() {
        return mobileNo;
    }

    /**
     *
     * @param mobileNo
     */
    public void setMobileNo(Integer mobileNo) {
        this.mobileNo = mobileNo;
    }

    /**
     *
     * @return
     */
    public Integer getImeiNo() {
        return imeiNo;
    }

    /**
     *
     * @param imeiNo
     */
    public void setImeiNo(Integer imeiNo) {
        this.imeiNo = imeiNo;
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
}
