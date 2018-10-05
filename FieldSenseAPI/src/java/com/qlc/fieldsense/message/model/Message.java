/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.message.model;

import com.qlc.fieldsense.user.model.User;
import java.sql.Timestamp;

/**
 *
 * @author Ramesh
 * @date 10-03-2014
 */
public class Message {

    private int id;
    private User sender;
    private int receiverId;
    private int messageType;
    private String message;
    private int teamId;
    private int connectionId;
    private Timestamp createdOn;
    private Timestamp modifiedOn;
    private boolean isRead;
    private User receiver;
    private int unReadMsgcount;

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
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return
     */
    public int getMessageType() {
        return messageType;
    }

    /**
     *
     * @param messageType
     */
    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    /**
     *
     * @return
     */
    public int getReceiverId() {
        return receiverId;
    }

    /**
     *
     * @param receiverId
     */
    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    /**
     *
     * @return
     */
    public User getSender() {
        return sender;
    }

    /**
     *
     * @param sender
     */
    public void setSender(User sender) {
        this.sender = sender;
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
    public int getConnectionId() {
        return connectionId;
    }

    /**
     *
     * @param connectionId
     */
    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    /**
     *
     * @return
     */
    public boolean isIsRead() {
        return isRead;
    }

    /**
     *
     * @param isRead
     */
    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    /**
     *
     * @return
     */
    public Timestamp getModifiedOn() {
        return modifiedOn;
    }

    /**
     *
     * @param modifiedOn
     */
    public void setModifiedOn(Timestamp modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    /**
     *
     * @return
     */
    public User getReceiver() {
        return receiver;
    }

    /**
     *
     * @param receiver
     */
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
    
    /**
     *
     */
    public Message() {
        this.id = 0;
        this.sender = new User();
        this.receiverId = 0;
        this.messageType = 0;
        this.message = "";
        this.teamId = 0;
        this.connectionId = 0;
        this.createdOn = new Timestamp(0);
        this.modifiedOn = new Timestamp(0);
        this.isRead = false;
        this.receiver= new User();
        this.unReadMsgcount = 0;
    }

    public int getUnReadMsgcount() {
        return unReadMsgcount;
    }

    public void setUnReadMsgcount(int unReadMsgcount) {
        this.unReadMsgcount = unReadMsgcount;
    }
}
