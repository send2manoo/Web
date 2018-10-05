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
public class TeamMember {

    private int id;
    private int teamId;
    private User user;
    private int memberType;
    private int status;
    private User createdBy;
    private Timestamp createdOn;
    private boolean isOnline;
    private int type;//0-delete ,1-added,2-updated
    private boolean canTakeCalls;
    private boolean inMeeting;
    private boolean hasSubordinate;
    private int activityCount;
    private int subordinateCount;
    private List<TeamMember> listOfSubs;   
    private String punchInTime;
    private String punchOutTime;
    private String punchin_date;
    private String punchout_date;
    private String two_day_from_current_date;

    public TeamMember() {
        this.id = 0;
        this.teamId = 0;
        this.user = new User();
        this.memberType = 0;
        this.status = 0;
        this.createdBy = new User();
        this.createdOn = new Timestamp(0);
        this.isOnline = false;
        this.type = 0;
        this.canTakeCalls = false;
        this.inMeeting = false;
        this.hasSubordinate = false;
        this.activityCount = 0;
        this.listOfSubs=new ArrayList<TeamMember>();    
        this.punchInTime = "";
        this.punchOutTime = "";
        this.punchin_date = "";
        this.punchout_date = "";
        this.two_day_from_current_date="";
       
    }

    public String getPunchin_date() {
        return punchin_date;
    }

    public void setPunchin_date(String punchin_date) {
        this.punchin_date = punchin_date;
    }

    public String getPunchout_date() {
        return punchout_date;
    }

    public void setPunchout_date(String punchout_date) {
        this.punchout_date = punchout_date;
    }

    public String getPunchin() {
        return punchInTime;
    }

    public void setPunchin(String punchin) {
        this.punchInTime = punchin;
    }

    public String getPunchout() {
        return punchOutTime;
    }

    public void setPunchout(String punchout) {
        this.punchOutTime = punchout;
    }


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
    public int getTeamId() {
        return teamId;
    }

    /**
     *
     * @param teamId
     */
    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    /**
     *
     * @return
     */
    public int getMemberType() {
        return memberType;
    }

    /**
     *
     * @param memberType
     */
    public void setMemberType(int memberType) {
        this.memberType = memberType;
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

    /**
     *
     * @return
     */
    public int getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     *
     * @return
     */
    public boolean isIsOnline() {
        return isOnline;
    }

    /**
     *
     * @param isOnline
     */
    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    /**
     *
     * @return
     */
    public int getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public boolean isCanTakeCalls() {
        return canTakeCalls;
    }

    /**
     *
     * @param canTakeCalls
     */
    public void setCanTakeCalls(boolean canTakeCalls) {
        this.canTakeCalls = canTakeCalls;
    }

    /**
     *
     * @return
     */
    public boolean isInMeeting() {
        return inMeeting;
    }

    /**
     *
     * @param inMeeting
     */
    public void setInMeeting(boolean inMeeting) {
        this.inMeeting = inMeeting;
    }

    /**
     *
     * @return
     */
    public boolean isHasSubordinate() {
        return hasSubordinate;
    }

    /**
     *
     * @param hasSubordinate
     */
    public void setHasSubordinate(boolean hasSubordinate) {
        this.hasSubordinate = hasSubordinate;
    }

    /**
     *
     * @return
     */
    public int getActivityCount() {
        return activityCount;
    }

    /**
     *
     * @param activityCount
     */
    public void setActivityCount(int activityCount) {
        this.activityCount = activityCount;
    }

    /**
     *
     * @return
     */
    public int getSubordinateCount() {
        return subordinateCount;
    }

    /**
     *
     * @param subordinateCount
     */
    public void setSubordinateCount(int subordinateCount) {
        this.subordinateCount = subordinateCount;
    }

    public List<TeamMember> getListOfSubs() {
        return listOfSubs;
    }

    public void setListOfSubs(List<TeamMember> listOfSubs) {
        this.listOfSubs = listOfSubs;
    }

    public String getTwo_day_from_current_date() {
        return two_day_from_current_date;
    }

    public void setTwo_day_from_current_date(String two_day_from_current_date) {
        this.two_day_from_current_date = two_day_from_current_date;
    }

}
