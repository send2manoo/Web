/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.userKey.model;

import com.qlc.fieldsense.customAnnotations.TrimSpaceValidation;
import com.qlc.fieldsense.user.model.User;
import java.sql.Timestamp;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author anuja
 */
public class UserKey {

    private int id;
    @NotNull
    private User userId;
    @NotEmpty
    @Length(min = 1, max = 45)
    @TrimSpaceValidation(message = "Please enter valid data")
    private String userKay;
    @NotEmpty
    @Length(min = 1, max = 500)
    @TrimSpaceValidation(message = "Please enter valid data")
    private String keyValue;
    private Timestamp createdOn;
    private Timestamp modifiedOn;

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
    public User getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     */
    public void setUserId(User userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     */
    public String getUserKay() {
        return userKay;
    }

    /**
     *
     * @param userKay
     */
    public void setUserKay(String userKay) {
        this.userKay = userKay;
    }

    /**
     *
     * @return
     */
    public String getKeyValue() {
        return keyValue;
    }

    /**
     *
     * @param keyValue
     */
    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
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
    public Timestamp getModifiedOn() {
        return modifiedOn;
    }

    /**
     *
     * @param modifiedOn
     */
    public void setModifiedOn(Timestamp modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    /**
     *
     */
    public UserKey() {
        this.id = 0;
        this.userId = new User();
        this.userKay = "";
        this.keyValue = "";
        this.createdOn = new Timestamp(0);
        this.modifiedOn = new Timestamp(0);
    }
}
