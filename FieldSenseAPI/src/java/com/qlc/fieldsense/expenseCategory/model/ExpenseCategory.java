/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.expenseCategory.model;

import com.qlc.fieldsense.user.model.User;
import java.sql.Timestamp;

/**
 *
 * @author anuja
 */
public class ExpenseCategory {
    private int id;
    private String categoryName;
    private boolean isActive;
    private Timestamp cretedOn;
    private User createdBy; 
    private int totalexpenseCategory;

    /**
     *
     */
    public ExpenseCategory() {
        this.id = 0;
        this.categoryName = "";
        this.isActive = false;
        this.cretedOn = new Timestamp(0);
        this.createdBy = new User();
        this.totalexpenseCategory = 0;
    }

    /**
     *
     * @return
     */
    public int getTotalexpenseCategory() {
        return totalexpenseCategory;
    }

    /**
     *
     * @param totalexpenseCategory
     */
    public void setTotalexpenseCategory(int totalexpenseCategory) {
        this.totalexpenseCategory = totalexpenseCategory;
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
    public String getCategoryName() {
        return categoryName;
    }

    /**
     *
     * @param categoryName
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
    
    
}
