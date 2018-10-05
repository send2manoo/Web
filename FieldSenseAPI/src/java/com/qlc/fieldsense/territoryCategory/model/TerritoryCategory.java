/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.territoryCategory.model;

import com.qlc.fieldsense.user.model.User;
import java.sql.Timestamp;
/**
 *
 * @author awaneesh
 */
public class TerritoryCategory {
    private int id;
    private String categoryName;
    private boolean isActive;
    private Timestamp cretedOn;
    private User createdBy; 
    private int totalTerritoryCategory;
    private String parentCatName;
    private String categoryPositionCsv;
    private int parentCategory;
    private int hasChild;
    private boolean userHasTerritory;
    private Timestamp updatedOn;
    private User updatedBy; 

    /**
     *
     */
    public TerritoryCategory() {
        this.id = 0;
        this.categoryName = "";
        this.isActive = false;
        this.cretedOn = new Timestamp(0);
        this.createdBy = new User();
        this.totalTerritoryCategory = 0;
        this.parentCatName = "";
        this.categoryPositionCsv="";
        this.parentCategory=0;
        this.hasChild=0;
        this.userHasTerritory=false;
        this.updatedOn = new Timestamp(0);
        this.updatedBy = new User();
    }

    /**
     *
     * @return
     */
    public int getTotalTerritoryCategory() {
        return totalTerritoryCategory;
    }

    /**
     *
     * @param totalTerritoryCategory
     */
    public void setTotalTerritoryCategory(int totalTerritoryCategory) {
        this.totalTerritoryCategory = totalTerritoryCategory;
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
   
    /**
     *
     * @return
     */
    public String getCategoryPositionCsv() {
        return categoryPositionCsv;
    }

    /**
     *
     * @param categoryPositionCsv
     */
    public void setCategoryPositionCsv(String categoryPositionCsv) {
        this.categoryPositionCsv = categoryPositionCsv;
    }

    /**
     *
     * @return
     */
    public int getParentCategory() {
        return parentCategory;
    }

    /**
     *
     * @param parentCategory
     */
    public void setParentCategory(int parentCategory) {
        this.parentCategory = parentCategory;
    }

    /**
     *
     * @return
     */
    public int getHasChild() {
        return hasChild;
    }

    /**
     *
     * @param hasChild
     */
    public void setHasChild(int hasChild) {
        this.hasChild = hasChild;
    }

    /**
     *
     * @return
     */
    public String getParentCatName() {
        return parentCatName;
    }

    /**
     *
     * @param parentCatName
     */
    public void setParentCatName(String parentCatName) {
        this.parentCatName = parentCatName;
    }

    /**
     *
     * @return
     */
    public boolean isUserHasTerritory() {
        return userHasTerritory;
    }

    /**
     *
     * @param userHasTerritory
     */
    public void setUserHasTerritory(boolean userHasTerritory) {
        this.userHasTerritory = userHasTerritory;
    }

    /**
     *
     * @return
     */
    public Timestamp getUpdatedOn() {
        return updatedOn;
    }

    /**
     *
     * @param updatedOn
     */
    public void setUpdatedOn(Timestamp updatedOn) {
        this.updatedOn = updatedOn;
    }

    /**
     *
     * @return
     */
    public User getUpdatedBy() {
        return updatedBy;
    }

    /**
     *
     * @param updatedBy
     */
    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }

}
