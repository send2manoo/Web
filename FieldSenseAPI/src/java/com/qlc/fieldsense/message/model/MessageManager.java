/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.message.model;

import com.qlc.fieldsense.message.dao.MessageDao;
import com.qlc.fieldsense.team.model.TeamMember;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.utils.APNPushNotifications;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GenerateSequenceIdManager;
import com.qlc.fieldsense.utils.GetApplicationContext;
import com.qlc.fieldsense.utils.GcmPushNotifications;
import com.qlc.fieldsense.utils.PushNotificationManager;
import com.qlc.fieldsense.utils.PushNotifications;
import java.util.ArrayList;
import java.util.List;
import javapns.notification.PushNotificationBigPayload;
import javapns.notification.PushNotificationPayload;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

/**
 *
 * @author Ramesh
 * @date 11-03-2014
 */
public class MessageManager {

    MessageDao messageDao = (MessageDao) GetApplicationContext.ac.getBean("messageDaoImpl");
    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("MessageManager");

    /**
     * 
     * @param message
     * @param userToken
     * @return 
     * @purpose used to send message 
     */
    public Object sendMessage(Message message, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) { //check token is valid or not
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);  // retrieve accountId from token
                String status = sendMessageToUserValidation(message); // check sender is valid or not
                if (status.equals(Constant.VALID)) {
                    java.util.HashMap gcmInfo = fieldSenseUtils.getGcmUserInfo(message.getReceiverId());
                    PushNotificationManager push= new PushNotificationManager();
                    String receiverFirstNm = (String)gcmInfo.get("firstName");
                    String receiverLastName = (String)gcmInfo.get("lastName");
                    String gcmId =(String)gcmInfo.get("gcmId");
                    int deviceOS =(Integer)gcmInfo.get("deviceOS");                    
                    //String receiverLastName = fieldSenseUtils.getUserlastName(message.getReceiverId());
                    if (message.getMessageType() == 1) {
                        int connectionId = messageDao.getConnectionId(message.getSender().getId(), message.getReceiverId(), message.getMessageType(), accountId); // retrieve connection id
                        if (connectionId == 0) { // if connection id is 0, no any conversation between both users
                            connectionId = GenerateSequenceIdManager.getNewSequenceIdForAccount("message_connection_id_seq", accountId); // generate sequence id
                            if (messageDao.insertMessageConnection(message.getSender().getId(), message.getReceiverId(), connectionId, message.getMessageType(), accountId)) { // create coonection
                                if (!messageDao.insertMessageConnection(message.getReceiverId(), message.getSender().getId(), connectionId, message.getMessageType(), accountId)) {
                                    FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " ", " ", " ");
                                }
                            }
                        }
                        int messageId = GenerateSequenceIdManager.getNewSequenceIdForAccount("message_id_seq", accountId);
                        if (messageDao.insertMessage(connectionId, messageId, message.getMessage(), message.getSender().getId(), accountId)) { // insert message details
                            if (messageDao.insertMessageSpool(connectionId, messageId, accountId)) { // insert message details in message pool
                                String senderName = fieldSenseUtils.getUserFirstName(message.getSender().getId()) + " " + fieldSenseUtils.getUserlastName(message.getSender().getId());
                                //String gcmId = fieldSenseUtils.getUserGcmSenderId(message.getReceiverId()); // retrive GCM id
                                int appVersion = (Integer) gcmInfo.get("appVersion"); // added by jyoti
                                if(appVersion > Integer.parseInt(Constant.APP_VERSION_CHECK) && deviceOS == 1 ){
                                    if(!(gcmId.equals("0") || gcmId.isEmpty())) {
                                        int unReadMsgCount = messageDao.getCountUnreadMsg(message.getReceiverId(), accountId); // added by jyoti, 15-01-2018
//                                        System.out.println("if reciver id : "+message.getReceiverId() +" , senderName : "+senderName+" ,unReadMsgCount : "+ unReadMsgCount);
                                        // jsonObject send in notification, Added by Jyoti
                                        JSONObject messageJsonObject = new JSONObject();
        //                              messageJsonObject.put("connectionId", connectionId);
                                        messageJsonObject.put("id", messageId);
                                        messageJsonObject.put("receiverId", message.getReceiverId());
                                        messageJsonObject.put("senderId", message.getSender().getId());
                                        messageJsonObject.put("createdOn", message.getCreatedOn());
                                        messageJsonObject.put("modifiedOn", message.getModifiedOn());
                                        messageJsonObject.put("isRead", message.isIsRead());
                                        messageJsonObject.put("message", message.getMessage());
                                        messageJsonObject.put("messageType", 1);
                                        // ended by jyoti
                                        push.messagesNotification(message.getMessage(), gcmId, deviceOS, senderName, unReadMsgCount, messageJsonObject, null); // unReadMsgCount, messageJsonObject field added by Jyoti
                                    }
                                } 
                                //Added by sanchita   ---//optimization for ios notification
                                else if(deviceOS == 2)
                                {
                                   if(!(gcmId.equals("0") || gcmId.isEmpty())) {
                                        int unReadMsgCount = messageDao.getCountUnreadMsg(message.getReceiverId(), accountId); // added by jyoti, 15-01-2018
//                                        System.out.println("if reciver id : "+message.getReceiverId() +" , senderName : "+senderName+" ,unReadMsgCount : "+ unReadMsgCount);                                           
                                         try {                                                 
                                             JSONObject messageJsonObject = new JSONObject();  
                                             
                                             messageJsonObject.put("id", messageId);
                                             messageJsonObject.put("receiverId", message.getReceiverId());
                                             messageJsonObject.put("senderId", message.getSender().getId());
                                             messageJsonObject.put("createdOn", message.getCreatedOn());
                                             messageJsonObject.put("modifiedOn", message.getModifiedOn());
                                             messageJsonObject.put("isRead", message.isIsRead());
                                             messageJsonObject.put("message", message.getMessage());
                                             messageJsonObject.put("messageType", 1); 
                                             
                                             // payload send in notification, Added by sanchita
                                             PushNotificationPayload payload = PushNotificationBigPayload.complex();
                                             payload.addAlert(message.getMessage());   //To show message in notification panel                                           
                                             payload.addCustomDictionary("type", "message");
                                             payload.addCustomDictionary("objectData", messageJsonObject.toString());                                                                                     
                                             
//                                             System.out.println("payload >> "+payload.toString());
                                             push.messagesNotification(message.getMessage(), gcmId, deviceOS, senderName, unReadMsgCount, null,payload); // messageJsonObject,unReadMsgCount field added by Jyoti
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    }// ended by sanchita
                                else {
                                    push.messagesNotification(message.getMessage(), gcmId, deviceOS, senderName, 0, null, null);
                                }
                                message.setId(messageId);
                                User user = new User();
                                user.setId(message.getReceiverId());
                                user.setFirstName(receiverFirstNm);
                                user.setLastName(receiverLastName);
                                message.setReceiver(user);
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " message  ", message);
                            } else {
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " ", " ", " ");
                            }
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " ", " ", " ");
                        }
                    } else {
                        int connectionId = messageDao.getConnectionId(message.getSender().getId(), message.getTeamId(), message.getMessageType(), accountId);

                        if (connectionId == 0) {
                            connectionId = GenerateSequenceIdManager.getNewSequenceIdForAccount("message_connection_id_seq", accountId);
                            if (messageDao.insertMessageConnection(message.getReceiverId(), message.getReceiverId(), connectionId, message.getMessageType(), accountId)) {
                                FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " ", " ", " ");
                            }
                        }
                        int messageId = GenerateSequenceIdManager.getNewSequenceIdForAccount("message_id_seq", accountId);
                        if (messageDao.insertMessage(connectionId, messageId, message.getMessage(), message.getSender().getId(), accountId)) {
                            if (messageDao.insertMessageSpool(connectionId, messageId, accountId)) {
                                String senderName = fieldSenseUtils.getUserFirstName(message.getSender().getId()) + " " + fieldSenseUtils.getUserlastName(message.getSender().getId());
                                //String gcmId = fieldSenseUtils.getUserGcmSenderId(message.getReceiverId());
                                //log4jLog.info(" sendMessage deviceOs="+deviceOS+" gcm Id " + gcmId + " message sender name " + senderName + " message " + message.getMessage());
                                int appVersion = (Integer) gcmInfo.get("appVersion"); // added by jyoti
                                if(appVersion > Integer.parseInt(Constant.APP_VERSION_CHECK) && deviceOS == 1){
                                    if(!(gcmId.equals("0") || gcmId.isEmpty())) {
                                        int unReadMsgCount = messageDao.getCountUnreadMsg(message.getReceiverId(), accountId); // added by jyoti, 15-01-2018
//                                        System.out.println("else reciver id : "+message.getReceiverId() +" , senderName : "+senderName+" ,unReadMsgCount : "+ unReadMsgCount);
                                        // jsonObject send in notification, Added by Jyoti
                                        JSONObject messageJsonObject = new JSONObject();
        //                                messageJsonObject.put("connectionId", connectionId);
                                        messageJsonObject.put("id", messageId);
                                        messageJsonObject.put("receiverId", message.getReceiverId());
                                        messageJsonObject.put("createdOn", message.getCreatedOn());
                                        messageJsonObject.put("modifiedOn", message.getModifiedOn());
                                        messageJsonObject.put("isRead", message.isIsRead());
                                        messageJsonObject.put("message", message.getMessage());
                                        messageJsonObject.put("messageType", 1);
                                        // ended by jyoti
                                        push.messagesNotification(message.getMessage(), gcmId, deviceOS, senderName, unReadMsgCount, messageJsonObject, null); // messageJsonObject,unReadMsgCount field added by Jyoti
                                    }
                                } 
                                //Added by sanchita   //optimization for ios notification
                                else if(deviceOS == 2)
                                {
                                   if(!(gcmId.equals("0") || gcmId.isEmpty())) {
                                        int unReadMsgCount = messageDao.getCountUnreadMsg(message.getReceiverId(), accountId); // added by jyoti, 15-01-2018
//                                        System.out.println("if reciver id : "+message.getReceiverId() +" , senderName : "+senderName+" ,unReadMsgCount : "+ unReadMsgCount);                                            
                                         try {                                                 
                                             JSONObject messageJsonObject = new JSONObject();  
                                             
                                             messageJsonObject.put("id", messageId);
                                             messageJsonObject.put("receiverId", message.getReceiverId());
                                             messageJsonObject.put("createdOn", message.getCreatedOn());
                                             messageJsonObject.put("modifiedOn", message.getModifiedOn());
                                             messageJsonObject.put("isRead", message.isIsRead());
                                             messageJsonObject.put("message", message.getMessage());
                                             messageJsonObject.put("messageType", 1);
                                             
                                             // payload send in notification, Added by sanchita
                                             PushNotificationPayload payload = PushNotificationBigPayload.complex();
                                             payload.addAlert(message.getMessage());   //To show message in notification panel                                          
                                             payload.addCustomDictionary("type", "message");
                                             payload.addCustomDictionary("objectData", messageJsonObject.toString());                                                                                     
                                             
//                                             System.out.println("payload >> "+payload.toString());
                                             push.messagesNotification(message.getMessage(), gcmId, deviceOS, senderName, unReadMsgCount, null,payload); // messageJsonObject,unReadMsgCount field added by Jyoti 
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                  }  // ended by sanchita
                                else {
                                    push.messagesNotification(message.getMessage(), gcmId, deviceOS, senderName, 0, null, null);
                                }
                                message.setId(messageId);
                                User user = new User();
                                user.setId(message.getReceiverId());
                                user.setFirstName(receiverFirstNm);
                                user.setLastName(receiverLastName);
                                message.setReceiver(user);
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " message ", message);
                            } else {
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " ", " ", " ");
                            }
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " ", " ", " ");
                        }
                    }

                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " ", " ", " ");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param teamId
     * @param userToken
     * @return message details based on message id
     */
    public Object getTeamMessages(int teamId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                String status = Constant.VALID;
                if (teamId != 0 && fieldSenseUtils.isTeamValid(teamId, accountId)) { // check team id is valid
                    List<Message> messageList = new ArrayList<Message>();
                    messageList = messageDao.selectTeamMessages(teamId, accountId); // retrieve team mesage details
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "", messageList);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid teamId . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
            } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     *
     * @param message
     * @return
     */
    public String sendMessageToUserValidation(Message message) {
        String validStatus = Constant.VALID;
        if (message.getSender().getId() == 0 || !fieldSenseUtils.isUserValid(message.getSender().getId())) {
            validStatus = Constant.INVALID_MESSAGE_SENDER_ID;
            return validStatus;
        }
        if (message.getReceiverId() == 0) {
            validStatus = Constant.INVALID_MESSAGE_RECIVER_ID;
            return validStatus;
        }
        if (message.getMessage().equals("")) {
            validStatus = Constant.INVALID_MESSAGE_MESSAGE;
            return validStatus;
        }
        return validStatus;
    }

    /**
     * 
     * @param userId2
     * @param userId
     * @param userToken
     * @author :anuja
     * @return 
     * @purpose: to get message between TM and TM 
     */
    public Object getUserMessages(int userId2, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) { // check user token valid
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int userId1 = fieldSenseUtils.userIdForToken(userToken);  // retrieve user id from token
                int accountId = fieldSenseUtils.accountIdForToken(userToken); // retrieve account id for token
                String status = Constant.VALID;
                if (userId2 != 0 && fieldSenseUtils.isUserValid(userId2)) { //check user is valid
                    List<Message> messageList = messageDao.selectUserToUserMessages(userId1, userId2, accountId); // retrieve messages
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "", messageList);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid userId . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

      /**
     * 
     * @param teamId
     * @param userId2
     * @param userId
     * @param userToken
      * @author :anuja
     * @return 
     * @purpose: to get message between TL and TM from team
     */
    public Object getUsersTeamMessages(int teamId, int userId2, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int userId1 = fieldSenseUtils.userIdForToken(userToken);
    //            if (fieldSenseUtils.isUserTeamLead(userId1)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (teamId != 0 && fieldSenseUtils.isTeamValid(teamId, accountId)) { //check team is valid
                    if (userId2 != 0 && fieldSenseUtils.isUserValid(userId2)) { // check user is valid
                        if (fieldSenseUtils.isMemberInTeam(teamId, userId2, accountId)) { // check user is in team or not
                            List<Message> messageList = messageDao.selectUsersTeamMessages(teamId, userId1, userId2, accountId); //retrieve messages between TL and TM
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "", messageList);
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid userId . Please try again . ", "", "");
                        }
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid userId . Please try again . ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid teamId . Please try again . ", "", "");
                }
//            } else {
//                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " You are not authorised.", "", "");
//            }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

      /**
     * 
     * @param teamId
     * @param userToken
      * @author : anuja
     * @return 
     * @purpose : to get list of users who exchanged messages(recent messages)
     */
    public Object getTeamMemberWhoMessaged(int teamId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) { // check token is valid
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int userId = fieldSenseUtils.userIdForToken(userToken); // retrieve userId from token
                int accountId = fieldSenseUtils.accountIdForToken(userToken); // retrieve  accountid from token
                if (teamId != 0 && fieldSenseUtils.isTeamValid(teamId, accountId)) { // check team is valid
                    if (fieldSenseUtils.isMemberInTeam(teamId, userId, accountId)) { // check member is in team
                        List<Message> messageList = messageDao.selectTeamMembersWhoMessaged(userId, accountId); // retrieve list of members who messaged
                       
                        Message message = new Message();
                        for (int i = 0; i < messageList.size(); i++) {
                            message = messageDao.selectMessage(messageList.get(i), accountId);  // retrieve recent message
//                             System.out.println("messageList id : "+ messageList.get(i).getSender().getId() + " , unreadmessagecount : "+messageList.get(i).getUnReadMsgcount());
                        }
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "messageList", messageList);
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "You are not team member. Please try again . ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid teamId. Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param messageIds
     * @param userToken
     * @author : anuja
     * @return 
     * @purpose : to update messages as read 
     */
    public Object updateIsread(List<Integer> messageIds, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int length = messageIds.size();
                int count = 0;
                int accountId = fieldSenseUtils.accountIdForToken(userToken);                
                if (messageIds != null) {
                    for (int i = 0; i < messageIds.size(); i++) {
                        if (messageDao.updateIsread(messageIds.get(i), accountId)) { // update message as read
                            count++;
                        }
                    }
                }
                if (length == count) {
                    int userId = fieldSenseUtils.userIdForToken(userToken);
                    int unReadMsgCount = messageDao.getCountUnreadMsg(userId, accountId); // added by jyoti, 25-01-2018
//                    System.out.println("updateIsread unReadMsgCount : "+unReadMsgCount);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "unReadMsgcount", unReadMsgCount); // modified by jyoti, 25-01-2018
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Updation failed. Please try again .", "", "");
                }
             // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param userToken
     * @author: anuja
     * @return 
     * @purpose: get count of unread messages
     */
    public Object selectCountUnreadMsg(String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                int userId = fieldSenseUtils.userIdForToken(userToken);
                int unReadMsgcount = messageDao.getCountUnreadMsg(userId, accountId); // get count of users unread messages
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "unReadMsgcount", unReadMsgcount);
        // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }

     /**
     * @author Ramesh
     * @return 
     * @date 06-06-2014
     * @param teamId
     * @param messageId
     * @param type =1 -> next messages type=2 -> previous messages .
     * @param userToken
     * @purpose Used to get messages of team First id in URL is teamId, second id is msgId, and third id indicates we have to retrieve next msg of msgId if it is 1 or if it 2 retrieve previous messageId
     */
    public Object getTeamMessages(int teamId, int messageId, int type, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int userId = fieldSenseUtils.userIdForToken(userToken);
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (teamId != 0 && fieldSenseUtils.isTeamValid(teamId, accountId)) {
                    if (fieldSenseUtils.isMemberInTeam(teamId, userId, accountId)) {
                        List<Message> messageList = messageDao.selectTeamMessages(teamId, messageId, type, accountId);
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "messageList", messageList);
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "You are not team member. Please try again . ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid teamId. Please try again . ", "", "");
                }
           // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }

     /**
     * @author Ramesh
     * @param userId2
     * @return 
     * @date 07-06-2014
     * @param teamId
     * @param userId
     * @param messageId
     * @param type =1 -> next messages type=2 -> previous messages .
     * @param userToken
     * @purpose Used to get messages between two users. First id in URL is teamId, second id is userId, third id is msgId, and fourth id indicates we have to retrieve next msg of msgId if it is 1 or if it 2 retrieve previous messageId. This retrieves msg between login user and the userId in URL.
     */
    public Object getUserToUserMessages(int teamId, int userId2, int messageId, int type, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int userId1 = fieldSenseUtils.userIdForToken(userToken);
    //            if (fieldSenseUtils.isUserTeamLead(userId1)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
    //            if (teamId != 0 && fieldSenseUtils.isTeamValid(teamId, accountId)) {
                if (userId2 != 0 && fieldSenseUtils.isUserValid(userId2)) {
    //                        if (fieldSenseUtils.isMemberInTeam(teamId, userId2, accountId)) {
                    List<Message> messageList = messageDao.selectUserTeamMessages(teamId, userId1, userId2, messageId, type, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "", messageList);
    //                        } else {
    //                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid userId . Please try again . ", "", "");
    //                        }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid userId . Please try again . ", "", "");
                }
    //            } else {
    //                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid teamId . Please try again . ", "", "");
    //            }
    //            } else {
    //                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " You are not authorised.", "", "");
    //            }
             // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}   
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param message
     * @param userToken
     * @author: anuja
     * @return 
     * @purpose: send messages TL to all TM (one to one) 
     */
    public Object sendMessageTltoTeamMembers(Message message, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                String status = sendMessageToUserValidation(message);
                if (status.equals(Constant.VALID)) {
                    if (message.getMessageType() == 1) {
    //                    if (fieldSenseUtils.isUserTeamLead(message.getSender().getId())) {
                        List<Integer> teamMembers = fieldSenseUtils.geTeamMembers(message.getReceiverId(), accountId);
                        for (int i = 0; i < teamMembers.size(); i++) {
                            if (teamMembers.get(i) != message.getSender().getId()) {
                                message.setReceiverId(teamMembers.get(i));
                                int connectionId = messageDao.getConnectionId(message.getSender().getId(), message.getReceiverId(), message.getMessageType(), accountId);

                                if (connectionId == 0) {
                                    connectionId = GenerateSequenceIdManager.getNewSequenceIdForAccount("message_connection_id_seq", accountId);
                                    if (messageDao.insertMessageConnection(message.getSender().getId(), message.getReceiverId(), connectionId, message.getMessageType(), accountId)) {
                                        if (!messageDao.insertMessageConnection(message.getReceiverId(), message.getSender().getId(), connectionId, message.getMessageType(), accountId)) {
    //                                    FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " ", " ", " ");
                                        }
                                    }
                                }
                                int messageId = GenerateSequenceIdManager.getNewSequenceIdForAccount("message_id_seq", accountId);
                                if (messageDao.insertMessage(connectionId, messageId, message.getMessage(), message.getSender().getId(), accountId)) {
                                    if (messageDao.insertMessageSpool(connectionId, messageId, accountId)) {
    //                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " message Id  ", messageId);
                                    } else {
    //                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " ", " ", " ");
                                    }
                                } else {
    //                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " ", " ", " ");
                                }
                            }
                        }
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", "", "");
    //                    }
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " ", " ", " ");
                }
             // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //} 
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " ", " ", " ");
    }

     /*
     * @author : anuja
     * @purpose : to get list of users who exchanged messages(others-recent messages)
     * @param teamId
     * @param userToken
     */

    /**
     *
     * @param teamId
     * @param userToken
     * @return
     */
    
    public Object getUsersWhoMessaged(int teamId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int userId = fieldSenseUtils.userIdForToken(userToken);
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<Message> messageList = messageDao.selectMembersWhoMessaged(teamId, userId, accountId);
//              Message message = new Message();
//              for (int i = 0; i < messageList.size(); i++) {
//                  message = messageDao.selectMessage(messageList.get(i), accountId);
//              }
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "messageList", messageList);
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //} 
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }

     /**
     * 
     * @param senderId
     * @param userToken
     * @author : anuja
     * @return 
     * @purpose : get recived messaged ids
     */
    public Object getReceivedMessages(int senderId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int receiverId = fieldSenseUtils.userIdForToken(userToken);
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (senderId != 0 && fieldSenseUtils.isUserValid(senderId)) {
                    List<Message> messageList = messageDao.selectRecivedMembers(senderId, receiverId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "", messageList);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid userId . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

     /*
     * @author : anuja
     * @date   : 14 March.2015
     * @purpose : to get list of users who exchanged messages in last seven days
     * @param userToken
     * @param date
     */

    /**
     *
     * @param date
     * @param userToken
     * @return
     */
    
    public Object getTeamMemberWhoMessagedLastSevenDays(String date, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int userId = fieldSenseUtils.userIdForToken(userToken);
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<TeamMember> recentSender = messageDao.selectTeamMembersRecentMessaged(date, userId, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "recentSender", recentSender);
          // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //} 
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }

}
