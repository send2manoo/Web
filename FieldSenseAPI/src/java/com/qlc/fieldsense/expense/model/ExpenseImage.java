/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.expense.model;

import com.qlc.fieldsense.user.model.User;
import java.sql.Timestamp;

/**
 *
 * @author mayank
 */
public class ExpenseImage {

    private long id;
    private int expenseId;
    private String imageURL;
    private User user;
    private Timestamp createdOn;

    /**
     *
     */
    public ExpenseImage() {
        this.id = 0;
        this.expenseId = 0;
        this.imageURL = "";
        this.user = new User();
        this.createdOn = new Timestamp(0);

    }

    /**
     *
     * @return
     */
    public Timestamp getCreatedOn() {
        return createdOn;
    }

    /**
     *
     * @param createdOn
     */
    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    /**
     *
     * @return
     */
    public int getExpenseId() {
        return expenseId;
    }

    /**
     *
     * @param expenseId
     */
    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    
    /**
     *
     * @return
     */
    public String getImageURL() {
        return imageURL;
    }

    /**
     *
     * @param imageURL
     */
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
