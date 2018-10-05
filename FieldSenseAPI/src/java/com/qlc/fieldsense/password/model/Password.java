/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.password.model;

/**
 *
 * @author anuja
 */
public class Password {

    private String emailId;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

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
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     *
     * @param oldPassword
     */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    /**
     *
     * @return
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     *
     * @param newPassword
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     *
     * @return
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     *
     * @param confirmPassword
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**
     *
     */
    public Password() {
    }
}
