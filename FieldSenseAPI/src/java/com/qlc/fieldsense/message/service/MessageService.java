/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.message.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qlc.fieldsense.message.model.Message;
import com.qlc.fieldsense.message.model.MessageManager;
import com.qlc.fieldsense.message.model.MessageIds;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Ramesh
 * @date 10-03-2014
 */
@Controller
@RequestMapping("/message")
public class MessageService {

    MessageManager messageManager = new MessageManager();

    /**
     * 
     * @param message
     * @param userToken
     * @return 
     * @purpose used to send message 
     */
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody
    Object sendMessage(@RequestBody Message message, @RequestHeader(value = "userToken") String userToken) {
        return messageManager.sendMessage(message, userToken);
    }

    /**
     * 
     * @param id
     * @param userToken
     * @return message details based on message id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public @ResponseBody
    Object getMessage(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return messageManager.getTeamMessages(id, userToken);
    }

    /**
     * 
     * @param userId
     * @param userToken
     * @author :anuja
     * @return 
     * @purpose: to get message between TM and TM 
     */
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public @ResponseBody
    Object getUserMessage(@PathVariable int userId, @RequestHeader(value = "userToken") String userToken) {
        return messageManager.getUserMessages(userId, userToken);
    }

    /**
     * 
     * @param teamId
     * @param userId
     * @param userToken
      * @author :anuja
     * @return 
     * @purpose: to get message between TL and TM from team
     */
    @RequestMapping(value = "/{teamId}/user/{userId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public @ResponseBody
    Object getUsersTeamMessage(@PathVariable int teamId, @PathVariable int userId, @RequestHeader(value = "userToken") String userToken) {
        return messageManager.getUsersTeamMessages(teamId, userId, userToken);
    }

    /**
     * 
     * @param teamId
     * @param userToken
      * @author : anuja
     * @return 
     * @purpose : to get list of users who exchanged messages(recent messages)
     */
    @RequestMapping(value = "/team/{teamId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public @ResponseBody
    Object getTeamMembersWhoMessaged(@PathVariable int teamId, @RequestHeader(value = "userToken") String userToken) {
        return messageManager.getTeamMemberWhoMessaged(teamId, userToken);
    }

    /**
     * 
     * @param messageIds
     * @param userToken
     * @author : anuja
     * @return 
     * @purpose : to update messages as read 
     */
    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public @ResponseBody
    Object updateIsRead(@RequestBody String messageIds, @RequestHeader(value = "userToken") String userToken) {
        Gson gsonObj = new Gson();
        java.lang.reflect.Type collectionType = new TypeToken<MessageIds>() {
        }.getType();
        MessageIds messgeId = gsonObj.fromJson(messageIds, collectionType);
        List<Integer> ids = null;
        if (messgeId.getMessageIds().length != 0) {
            ids = Arrays.asList(messgeId.getMessageIds());
        }
        return messageManager.updateIsread(ids, userToken);
    }

    /**
     * 
     * @param userToken
     * @author: anuja
     * @return 
     * @purpose: get count of unread messages
     */
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Object selectCountUnreadMsg(@RequestHeader(value = "userToken") String userToken) {
        return messageManager.selectCountUnreadMsg(userToken);
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
    @RequestMapping(value = "/team/{teamId}/{messageId}/{type}", method = RequestMethod.GET, headers = "Accept=application/json")
    public @ResponseBody
    Object getTeamMessages(@PathVariable int teamId, @PathVariable int messageId, @PathVariable int type, @RequestHeader(value = "userToken") String userToken) {
        return messageManager.getTeamMessages(teamId, messageId, type, userToken);
    }

    /**
     * @author Ramesh
     * @return 
     * @date 07-06-2014
     * @param teamId
     * @param userId
     * @param messageId
     * @param type =1 -> next messages type=2 -> previous messages .
     * @param userToken
     * @purpose Used to get messages between two users. First id in URL is teamId, second id is userId, third id is msgId, and fourth id indicates we have to retrieve next msg of msgId if it is 1 or if it 2 retrieve previous messageId. This retrieves msg between login user and the userId in URL.
     */
    @RequestMapping(value = "/team/{teamId}/user/{userId}/{messageId}/{type}", method = RequestMethod.GET, headers = "Accept=application/json")
    public @ResponseBody
    Object getUserToUserMessages(@PathVariable int teamId, @PathVariable int userId, @PathVariable int messageId, @PathVariable int type, @RequestHeader(value = "userToken") String userToken) {
        return messageManager.getUserToUserMessages(teamId, userId, messageId, type, userToken);
    }

    /**
     * 
     * @param message
     * @param userToken
     * @author: anuja
     * @return 
     * @purpose: send messages TL to all TM (one to one) 
     */
    @RequestMapping(value = "/teamMember", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody
    Object sendMessageTltoTeamMembers(@RequestBody Message message, @RequestHeader(value = "userToken") String userToken) {
        return messageManager.sendMessageTltoTeamMembers(message, userToken);
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
    
    @RequestMapping(value = "/others/team/{teamId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public @ResponseBody
    Object getUsersWhoMessaged(@PathVariable int teamId, @RequestHeader(value = "userToken") String userToken) {
        return messageManager.getUsersWhoMessaged(teamId, userToken);
    }

    /**
     * 
     * @param senderId
     * @param userToken
     * @author : anuja
     * @return 
     * @purpose : get recived messaged ids
     */
    @RequestMapping(value = "/sender/{senderId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public @ResponseBody
    Object getReceivedMessages(@PathVariable int senderId, @RequestHeader(value = "userToken") String userToken) {
        return messageManager.getReceivedMessages(senderId, userToken);
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
     * @param userToken
     * @param date
     * @return
     */
    
    @RequestMapping(value = "/recentSender/{date}", method = RequestMethod.GET, headers = "Accept=application/json")
    public @ResponseBody
    Object getTeamMemberWhoMessagedLastSevenDays(@RequestHeader(value = "userToken") String userToken,@PathVariable("date") String date) {
        return messageManager.getTeamMemberWhoMessagedLastSevenDays(date,userToken);
    }
}
