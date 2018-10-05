/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.predefinedList.model;

import com.qlc.fieldsense.user.model.User;
import java.sql.Timestamp;

/**
 *
 * @author siddhesh
 */
public class PredefinedList {
    
    private int listId;
    private String listName;
    private int isActive;
    private String optionValues;
    private Timestamp createdOn;
    private User createdBy; 
    private int totalPredefinedList;

    /**
     *
     */
    public PredefinedList()
{
    listId=0;
    listName="";
    isActive=0;
    optionValues="";
    createdOn=new Timestamp(0);
    createdBy=new User();
    
}

    /**
     *
     * @return
     */
    public int getListId() {
        return listId;
    }

    /**
     *
     * @param listId
     */
    public void setListId(int listId) {
        this.listId = listId;
    }

    /**
     *
     * @return
     */
    public String getListName() {
        return listName;
    }

    /**
     *
     * @param listName
     */
    public void setListName(String listName) {
        this.listName = listName;
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
    public String getOptionValues() {
        return optionValues;
    }

    /**
     *
     * @param optionValues
     */
    public void setOptionValues(String optionValues) {
        this.optionValues = optionValues;
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

    /**
     *
     * @return
     */
    public int getTotalPredefinedList() {
        return totalPredefinedList;
    }

    /**
     *
     * @param totalPredefinedList
     */
    public void setTotalPredefinedList(int totalPredefinedList) {
        this.totalPredefinedList = totalPredefinedList;
    }

   

   
   
    
}
