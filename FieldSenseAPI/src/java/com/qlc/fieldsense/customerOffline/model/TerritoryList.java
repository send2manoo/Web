/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customerOffline.model;

/**
 *
 * @author siddhesh
 */
public class TerritoryList {

    @Override
    public String toString() {
        return "TerritoryList{" + "territoryId=" + territoryId + '}';
    }
    
    
    private int territoryId;
    
    /**
     *
     */
    public TerritoryList(){
    
        territoryId=0;
        
    }

    /**
     *
     * @return
     */
    public int getTerritoryId() {
        return territoryId;
    }

    /**
     *
     * @param territoryId
     */
    public void setTerritoryId(int territoryId) {
        this.territoryId = territoryId;
    }
    
    
    
    
}
