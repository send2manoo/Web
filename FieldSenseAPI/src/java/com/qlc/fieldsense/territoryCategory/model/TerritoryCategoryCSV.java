/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.territoryCategory.model;

/**
 *
 * @author jyoti
 */
public class TerritoryCategoryCSV {
    
    private String categoryName;
    private String isActive;
    private String parentCatName;

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
    public String getIsActive() {
        return isActive;
    }

    /**
     *
     * @param isActive
     */
    public void setIsActive(String isActive) {
        this.isActive = isActive;
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

}
