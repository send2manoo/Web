/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.activityPurpose.model;

/**
 *
 * @author jyoti
 */
public class ActivityPurposeCategoryCSV {
    
    private String purpose;
    private String isActive;

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
    public String getPurpose() {
        return purpose;
    }

    /**
     *
     * @param purpose
     */
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    
    
}
