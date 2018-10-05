/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.team.model;

import com.qlc.fieldsense.user.model.User;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mayank
 */
public class Team {

    private int id;
    private String teamName;
    private String description;
    private User ownerId;
    private int isActive;
    private Timestamp createdOn;
    private User createdBy;
    private List<TeamMember> teamMembers;
    private int teamMembersCount;
    private int user_id;
    private String parentCSV;   // added by manohar
    private String parent_id;
    /**
     *
     */
    public Team() {
        this.id = 0;
        this.teamName = "";
        this.description = "";
        this.ownerId = new User();
        this.isActive = 0;
        this.createdOn = new Timestamp(0);
        this.createdBy = new User();
        this.teamMembers = new ArrayList<TeamMember>();
        this.teamMembersCount = 0;
        this.user_id=0;
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
    public int getIsActive() {
        return isActive;
    }

    /**
     *
     * @param isActive
     */
    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    /**
     *
     * @return
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     *
     * @param teamName
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    /**
     *
     * @return
     */
    public User getCreatedBy() {
        return createdBy;
    }

    /**
     *
     * @param createdBy
     */
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    /**
     *
     * @return
     */
    public User getOwnerId() {
        return ownerId;
    }

    /**
     *
     * @param ownerId
     */
    public void setOwnerId(User ownerId) {
        this.ownerId = ownerId;
    }

    /**
     *
     * @return
     */
    public List<TeamMember> getTeamMembers() {
        return teamMembers;
    }

    /**
     *
     * @param teamMembers
     */
    public void setTeamMembers(List<TeamMember> teamMembers) {
        this.teamMembers = teamMembers;
    }

    /**
     *
     * @return
     */
    public int getTeamMembersCount() {
        return teamMembersCount;
    }

    /**
     *
     * @param teamMembersCount
     */
    public void setTeamMembersCount(int teamMembersCount) {
        this.teamMembersCount = teamMembersCount;
    }
    
    /**
     *
     * @return
     */
    public int getUser_id() {
        return user_id;
    }

    /**
     *
     * @param user_id
     */
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getParentCSV() {
        return parentCSV;
    }

    public void setParentCSV(String parentCSV) {
        this.parentCSV = parentCSV;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }
}
