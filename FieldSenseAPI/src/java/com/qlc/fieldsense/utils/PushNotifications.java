/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.utils;

import java.util.Map;
import javapns.notification.Payload;
import net.sf.json.JSONObject;

/**
 *
 * @author awaneesh
 */
public interface PushNotifications {
    
    /**
     *
     * @param message
     * @param userName
     * @param tokenId
     * @param unReadMsgCount // added by jyoti, 2018-01-15
     * @param messageJsonObject
     * @param payload // added by sanchita 2018-08-24
     */
    public void messagesNotification(String message, String userName, String tokenId, int unReadMsgCount, JSONObject messageJsonObject, Payload payload);
    
    /**
     *
     * @param message
     * @param tokenId
     */
    public void activityNotifications(String message, String tokenId);
    
    /**
     *
     * @param message
     * @param tokenId
     * @param expense_id
     */
    public void expenseNotifications(String message, String tokenId,String expense_id);
    
    /**
     * @Added by jyoti, 10-01-2018
     * @param m_data
     * @param tokenId 
     */
    public void addEditNotification( Map<String, Object> m_data, String tokenId,Payload payload);
    
}
