/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.utils;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import java.io.IOException;
import java.util.Map;
import javapns.notification.Payload;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

/**
 *
 * @author Ramesh
 * @date 05-01-2015
 * @purpose This class is used to send the push notifications to users .
 */
public class GcmPushNotifications implements PushNotifications {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("GcmPushNotifications");

    /**
     *
     * @param message
     * @param userName
     * @param gcmSenderId
     * @param unReadMsgCount
     * @param messageJsonObject
     * @param payload // added by sanchita 2018-08-24
     */
    @Override
    public void messagesNotification(String message, String userName, String gcmSenderId, int unReadMsgCount, JSONObject messageJsonObject,Payload payload) {
        try {
            Sender sender = new Sender(Constant.GOOGLE_PUSH_NOTIFICATION_SERVER_KEY);
            Message messageData = new Message.Builder().timeToLive(30).delayWhileIdle(true).
                    addData("type", "message").
                    addData("userName", userName).
                    addData("message", message).
                    addData("unReadMsgCount", String.valueOf(unReadMsgCount)). // Added by jyoti
                    addData("messageObject", messageJsonObject.toString()). // Added by jyoti
                    build();
            Result result = sender.send(messageData, gcmSenderId, 1);
            log4jLog.info(" sendMessagesNotification " + result + "  status  " + result.getErrorCodeName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param message
     * @param gcmSenderId
     */
    @Override
    public void activityNotifications(String message, String gcmSenderId) {
        try {
            Sender sender = new Sender(Constant.GOOGLE_PUSH_NOTIFICATION_SERVER_KEY);
            Message messageData = new Message.Builder().timeToLive(30).delayWhileIdle(true).addData("type", "activity").addData("message", message).build();
            Result result = sender.send(messageData, gcmSenderId, 1);
            log4jLog.info(" activityNotifications " + result + "  status  " + result.getErrorCodeName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param message
     * @param gcmSenderId
     * @param expense_id
     */
    @Override
    public void expenseNotifications(String message, String gcmSenderId, String expense_id) {
        try {
            Sender sender = new Sender(Constant.GOOGLE_PUSH_NOTIFICATION_SERVER_KEY);
            Message messageData = new Message.Builder().timeToLive(30).delayWhileIdle(true).addData("type", "expense").addData("id", expense_id).addData("message", message).build();
            Result result = sender.send(messageData, gcmSenderId, 1);
            log4jLog.info(" expenseNotifications " + result + "  status  " + result.getErrorCodeName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Added by jyoti, 10-01-2018
     * @param m_data
     * @param gcmSenderId
     */
    @Override
    public void addEditNotification(Map<String, Object> m_data, String gcmSenderId ,Payload payload) {
        try {

            Sender sender = new Sender(Constant.GOOGLE_PUSH_NOTIFICATION_SERVER_KEY);
            Message.Builder messageBuilder = new Message.Builder();
            // add multiple message data
            for (Map.Entry<String, Object> m_data1 : m_data.entrySet()) {
                messageBuilder.addData(m_data1.getKey(), m_data1.getValue().toString());
            }

//            for(Object jsonObject : jsonData.keySet()) {
//                 messageBuilder.addData((String) jsonObject, jsonData.get((String) jsonObject).toString());
//            }
            Message message = messageBuilder.build();
            Result result = sender.send(message, gcmSenderId, 1);
            log4jLog.info(" addEditNotification " + result + "  status  " + result.getErrorCodeName());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

}
