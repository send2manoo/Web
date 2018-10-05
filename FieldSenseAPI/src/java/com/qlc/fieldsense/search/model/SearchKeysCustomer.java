/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.search.model;

import java.util.List;

/**
 *
 * @author Ramesh
 * @date 14-05-2014
 */
public class SearchKeysCustomer {

    private List<String> recentItems;
    private List<String> trritory;
    private List<String> industry;

    /**
     *
     * @return
     */
    public List<String> getRecentItems() {
        return recentItems;
    }

    /**
     *
     * @param recentItems
     */
    public void setRecentItems(List<String> recentItems) {
        this.recentItems = recentItems;
    }

    /**
     *
     * @return
     */
    public List<String> getIndustry() {
        return industry;
    }

    /**
     *
     * @param industry
     */
    public void setIndustry(List<String> industry) {
        this.industry = industry;
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
