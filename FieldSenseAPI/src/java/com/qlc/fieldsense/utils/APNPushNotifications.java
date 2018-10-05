/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.utils;

//import com.notnoop.apns.*;
import java.util.Map;
import javapns.Push;
import javapns.notification.PushedNotification;
import java.util.List;
import javapns.notification.Payload;
import javapns.notification.PushNotificationBigPayload;
import javapns.notification.PushNotificationPayload;
import javapns.notification.ResponsePacket;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

/**
 *
 * @author awaneesh
 */
public class APNPushNotifications implements PushNotifications {

    public static final Logger log4jLog = Logger.getLogger("APNPushNotifications");

    /**
     *
     * @param message
     * @param userName
     * @param token
     * @param unReadMsgCount // added by jyoti
     * @param messageJsonObject // added by jyoti
     * @param payload // added by sanchita 2018-08-24
     */
    @Override
    public void messagesNotification(String message, String userName, String token, int unReadMsgCount, JSONObject messageJsonObject, Payload payload) {
        try {
            String[] devices = {token};
//            
            List<PushedNotification> notifications = Push.payload(payload, Constant.APN_CERTIFICATE, Constant.APN_PASSWORD, false, devices);
                        
            for (PushedNotification notification : notifications) {
                String tokenFromResponse = notification.getDevice().getToken();
                if (notification.isSuccessful()) {
//                    System.out.println("apn success messagesNotification");
                    /* Apple accepted the notification and should deliver it */
                    log4jLog.info("APN expenseNotifications Successfully delivered to token=" + tokenFromResponse);
                } else {
//                    System.out.println("apn failed messagesNotification");
                     log4jLog.info("APN expenseNotifications invalid token:" + tokenFromResponse);
                    /* Add code here to remove invalidToken from database */
                    }
                }
        } catch (Exception e) {
            log4jLog.info(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     *
     * @param message
     * @param token
     */
    @Override
    public void activityNotifications(String message, String token) {
        try {
            String[] devices = {token};
//        List<PushedNotification> notifications = Push.alert(message, Constant.APN_CERTIFICATE, Constant.APN_PASSWORD, Constant.IOS_ENVIRONMENT_MODE, devices);
            List<PushedNotification> notifications = Push.alert(message, Constant.APN_CERTIFICATE, Constant.APN_PASSWORD, false, devices); // Bug-Fix #30360, to solve the issue of notification, if its set to true notification does not get send.

            for (PushedNotification notification : notifications) {
                String tokenFromResponse = notification.getDevice().getToken();
                if (notification.isSuccessful()) {
//                    System.out.println("apn success activityNotifications");
                    /* Apple accepted the notification and should deliver it */
                    log4jLog.info("APN expenseNotifications Successfully delivered to token=" + tokenFromResponse);
                } else {

                    log4jLog.info("APN expenseNotifications invalid token:" + tokenFromResponse);
                    /* Add code here to remove invalidToken from database */
                }
            }
        } catch (Exception e) {
            log4jLog.info(e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     *
     * @param message
     * @param token
     * @param expense_id
     */
    @Override
    public void expenseNotifications(String message, String token, String expense_id) {
        try {
            String[] devices = {token};
//            List<PushedNotification> notifications = Push.alert(message, Constant.APN_CERTIFICATE, Constant.APN_PASSWORD, Constant.IOS_ENVIRONMENT_MODE, devices);
            List<PushedNotification> notifications = Push.alert(message, Constant.APN_CERTIFICATE, Constant.APN_PASSWORD, false, devices); // Bug-Fix #30360, to solve the issue of notification, if its set to true notification does not get send.

            for (PushedNotification notification : notifications) {
                String tokenFromResponse = notification.getDevice().getToken();
                if (notification.isSuccessful()) {
//                    System.out.println("apn success expenseNotifications");
                    /* Apple accepted the notification and should deliver it */
                    log4jLog.info("APN expenseNotifications Successfully delivered to token=" + tokenFromResponse);
                } else {
                    log4jLog.info("APN expenseNotifications  invalid token:" + tokenFromResponse);
                    /* Add code here to remove invalidToken from database */
                }
            }
        } catch (Exception e) {
            log4jLog.info(e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * @Added by Jyoti, 10-01-2018
     * @param token
     */
    @Override
    public void addEditNotification(Map<String, Object> m_data, String token,Payload payload) {
        try {
            String[] devices = {token};
//            List<PushedNotification> notifications = Push.alert(m_data.toString(), Constant.APN_CERTIFICATE, Constant.APN_PASSWORD, Constant.IOS_ENVIRONMENT_MODE, devices);
            List<PushedNotification> notifications = Push.payload(payload, Constant.APN_CERTIFICATE, Constant.APN_PASSWORD, false, devices); // Bug-Fix #30360, to solve the issue of notification, if its set to true notification does not get send.

            for (PushedNotification notification : notifications) {
                String tokenFromResponse = notification.getDevice().getToken();
                if (notification.isSuccessful()) {
//                    System.out.println("apn success addEditNotification");
                    /* Apple accepted the notification and should deliver it */
                    log4jLog.info("APN expenseNotifications Successfully delivered to token=" + tokenFromResponse);
                } else {
//                    System.out.println("apn failed addEditNotification" + tokenFromResponse);
                    log4jLog.info("APN expenseNotifications invalid token:" + tokenFromResponse);
                    /* Add code here to remove invalidToken from database */
                }
            }
        } catch (Exception e) {
            log4jLog.info(e.getMessage());
            e.printStackTrace();
        }
    }

}
