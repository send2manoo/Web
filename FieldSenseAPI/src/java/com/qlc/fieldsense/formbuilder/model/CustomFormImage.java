/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.formbuilder.model;

import com.qlc.fieldsense.user.model.User;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author krish
 */
public class CustomFormImage {
    
    private int id;
    private int formId;
    private int fieldId;
    private Map imageURL;
    private User user;
    private Timestamp createdOn;

    /**
     *
     */
    public CustomFormImage() {
        this.id = 0;
        this.formId = 0;
        this.fieldId = 0;
        this.imageURL = new HashMap();
        this.user = new User();
        this.createdOn = new Timestamp(0);

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
    public int getFormId() {
        return formId;
    }

    /**
     *
     * @param formId
     */
    public void setFormId(int formId) {
        this.formId = formId;
    }

    /**
     *
     * @return
     */
    public int getFieldId() {
        return fieldId;
    }

    /**
     *
     * @param fieldId
     */
    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    /**
     *
     * @return
     */
    public User getUser() {
        return user;
    }

    /**
     *
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
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
    public Map getImageURL() {
        return imageURL;
    }

    /**
     *
     * @param imageURL
     */
    public void setImageURL(Map imageURL) {
        this.imageURL = imageURL;
    }

 

}
