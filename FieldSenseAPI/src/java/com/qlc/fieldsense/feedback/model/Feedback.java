/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.feedback.model;

/**
 *
 * @author Ramesh
 * @date 02-02-2015
 */
public class Feedback {

    private String feedback;
    private String userName;
    private String accountName;
    private String section;
    private String emailId;
    private String feedbackTime;

    /**
     *
     */
    public Feedback() {
        this.feedback = "";
        this.userName = "";
        this.accountName = "";
        this.section = "";
        this.emailId = "";
        this.feedbackTime = "";
    }

    /**
     *
     * @return
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     *
     * @param accountName
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     *
     * @return
     */
    public String getEmailId() {
        return emailId;
    }

    /**
     *
     * @param emailId
     */
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    /**
     *
     * @return
     */
    public String getFeedback() {
        return feedback;
    }

    /**
     *
     * @param feedback
     */
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    /**
     *
     * @return
     */
    public String getFeedbackTime() {
        return feedbackTime;
    }

    /**
     *
     * @param feedbackTime
     */
    public void setFeedbackTime(String feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

    /**
     *
     * @return
     */
    public String getSection() {
        return section;
    }

    /**
     *
     * @param section
     */
    public void setSection(String section) {
        this.section = section;
    }

    /**
     *
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     *
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
