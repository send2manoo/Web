/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.team.model;

import com.qlc.fieldsense.user.model.User;

/**
 *
 * @author Ramesh
 * @date 16-09-2014
 */
public class OraganizationChart {

    private int id;
    private int parentId;
    private User user;

    /**
     *
     */
    public OraganizationChart() {
        this.id = 0;
        this.parentId = 0;
        this.user = new User();
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
    public int getParentId() {
        return parentId;
    }

    /**
     *
     * @param parentId
     */
    public void setParentId(int parentId) {
        this.parentId = parentId;
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
}
