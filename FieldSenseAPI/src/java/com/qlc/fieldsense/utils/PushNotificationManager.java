/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.utils;

import java.util.Map;
import javapns.notification.Payload;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

/**
 *
 * @author awaneesh
 */
public class PushNotificationManager {
    
    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("PushNotificationManager");
    
    /**
     *
     * @param message
     * @param token
     * @param deviceOS
     * @param senderName
     * @param unReadMsgCount // added by jyoti, 2018-01-15
     * @param messageJsonObject
     * @param payload //added by sanchita 2018-08-24
     */
    public void messagesNotification(String message, String token,int deviceOS, String senderName, int unReadMsgCount, JSONObject messageJsonObject,Payload payload) {
//        System.out.println("deviceOS : "+deviceOS + " token : "+token);
        PushNotifications messagePushNotifications = null;        
        if(deviceOS==1){
            messagePushNotifications = new GcmPushNotifications();
            // commented by jyoti
//        }else if(deviceOS==2){
//            messagePushNotifications = new APNPushNotifications();
//        }
            // Added by jyoti, purpose - device_os number for ios not constant
        } else {
            messagePushNotifications = new APNPushNotifications();
        }
        if (!token.equals("0") && messagePushNotifications !=null) {
//            System.out.println("Inside send notification messagesNotification");
             messagePushNotifications.messagesNotification(message, senderName, token, unReadMsgCount, messageJsonObject, payload); // send mobile notification 
        } else {
//             System.out.println("Inside do not send notification messagesNotification");
        }
        
    }
    
    /**
     *
     * @param message
     * @param token
     * @param deviceOS
     */
    public void activityNotification(String message, String token,int deviceOS) {  
//        System.out.println("deviceOS : "+deviceOS + " token : "+token);
        PushNotifications messagePushNotifications = null;
        if(deviceOS==1){
            messagePushNotifications = new GcmPushNotifications();
           // commented by jyoti
//        }else if(deviceOS==2){
//            messagePushNotifications = new APNPushNotifications();
//        }
            // Added by jyoti, purpose - device_os number for ios not constant
        } else {
            messagePushNotifications = new APNPushNotifications();
        }
        if (!token.equals("0") && messagePushNotifications !=null) {
//            System.out.println("Inside send notification activityNotification");
            messagePushNotifications.activityNotifications(message, token); // send mobile notification 
        } else {
//             System.out.println("Inside do not send notification activityNotification");
        }
        
    }
    
    /**
     *
     * @param message
     * @param token
     * @param deviceOS
     * @param expenseId
     */
    public void expenseNotification(String message, String token,int deviceOS, String expenseId) {
//        System.out.println("deviceOS : "+deviceOS);
        PushNotifications messagePushNotifications = null;
        if(deviceOS==1){
            messagePushNotifications = new GcmPushNotifications();
           // commented by jyoti
//        }else if(deviceOS==2){
//            messagePushNotifications = new APNPushNotifications();
//        }
            // Added by jyoti, purpose - device_os number for ios not constant
        } else {
            messagePushNotifications = new APNPushNotifications();
        }
        if (!token.equals("0") && messagePushNotifications !=null) {
             messagePushNotifications.expenseNotifications(message, token, expenseId); // send mobile notification 
        }
        
    }
    
    /**
     * @Added by jyoti, 10-01-2018
     * @param m_data
     * @param token
     * @param deviceOS 
     */
    public void addEditNotification(Map<String, Object> m_data, String token,int deviceOS ,Payload payload) { 
//        System.out.println("deviceOS : "+deviceOS);
        PushNotifications messagePushNotifications = null;
        if(deviceOS==1){
            messagePushNotifications = new GcmPushNotifications();
           // commented by jyoti
//        }else if(deviceOS==2){
//            messagePushNotifications = new APNPushNotifications();
//        }
            // Added by jyoti, purpose - device_os number for ios not constant
        } else {
            messagePushNotifications = new APNPushNotifications();
        }
        if (!token.equals("0") && messagePushNotifications !=null) {
            messagePushNotifications.addEditNotification( m_data, token,payload); // send mobile notification 
        }
        
    }
}
