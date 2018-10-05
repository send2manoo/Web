/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.dashboard.model;

import com.qlc.fieldsense.user.model.User;

/**
 *
 * @author root
 */
public class LeaderboardStats {
    private int visitCount;
    private User user;

    public LeaderboardStats() {
        this.visitCount = 0;
        this.user = null;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    
}
