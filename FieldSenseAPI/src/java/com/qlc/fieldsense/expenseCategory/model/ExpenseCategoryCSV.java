/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.expenseCategory.model;

/**
 *
 * @author jyoti
 */
public class ExpenseCategoryCSV {
    
    private String categoryName;
    private String isActive;

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

}
