/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.notes.model;

import com.qlc.fieldsense.customAnnotations.TrimSpaceValidation;
import com.qlc.fieldsense.user.model.User;
import java.sql.Timestamp;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author anuja
 */
public class Notes {
    private int id;
    @NotNull
    private User userId;
    @NotNull
    private Integer customerId;
    @NotEmpty
    @Length(min = 1, max = 1000)
    @TrimSpaceValidation(message = "Please enter valid data")
    private String description;
    private Timestamp createdOn;

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
    public User getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     */
    public void setUserId(User userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     *
     * @param customerId
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
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
     */
    public Notes() {
        this.id = 0;
        this.userId = new User();
        this.customerId = 0;
        this.description = "";
        this.createdOn = new Timestamp(0);
    }
    
}
