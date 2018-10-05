/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.search.model;

import java.util.List;

/**
 *
 * @author Ramesh
 * @date 25-06-2014
 */
public class SearchKeysUser {

    private List<String> status;
    private List<String> trritory;
    
    /**
     *
     * @return
     */
    public List<String> getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(List<String> status) {
        this.status = status;
    }

    /**
     *
     * @return
     */
    public List<String> getTrritory() {
        return trritory;
    }

    /**
     *
     * @param trritory
     */
    public void setTrritory(List<String> trritory) {
        this.trritory = trritory;
    }
    
}
