/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.message.dao;

import com.qlc.fieldsense.message.model.Message;
import com.qlc.fieldsense.team.model.TeamMember;
import java.util.List;

/**
 *
 * @author Ramesh
 * @date 10-03-2014
 */
public interface MessageDao {

    /**
     *
     * @param senderId
     * @param receiverId
     * @param connectionId
     * @param connectionType
     * @param accountId
     * @return
     */
    public boolean insertMessageConnection(int senderId, int receiverId, int connectionId, int connectionType, int accountId);

    /**
     *
     * @param connectionId
     * @param messageId
     * @param message
     * @param senderId
     * @param accountId
     * @return
     */
    public boolean insertMessage(int connectionId, int messageId, String message, int senderId, int accountId);

    /**
     *
     * @param senderId
     * @param receiverId
     * @param connectionType
     * @param account
     * @return
     */
    public int getConnectionId(int senderId, int receiverId, int connectionType, int account);

    /**
     *
     * @param teamId
     * @param accountId
     * @return
     */
    public List<Message> selectTeamMessages(int teamId, int accountId);

    /**
     *
     * @param userId1
     * @param userId2
     * @param accountId
     * @return
     */
    public List<Message> selectUserMessages(int userId1, int userId2, int accountId);

    /**
     *
     * @param userId1
     * @param userId2
     * @param accountId
     * @return
     */
    public List<Message> selectUserToUserMessages(int userId1, int userId2, int accountId);

    /**
     *
     * @param teamId
     * @param userId1
     * @param userId2
     * @param accountId
     * @return
     */
    public List<Message> selectUsersTeamMessages(int teamId, int userId1, int userId2, int accountId);

    /**
     *
     * @param connectionId
     * @param messageId
     * @param accountId
     * @return
     */
    public boolean insertMessageSpool(int connectionId, int messageId, int accountId);

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public List<Message> selectTeamMembersWhoMessaged(int userId, int accountId);

    /**
     *
     * @param message
     * @param accountId
     * @return
     */
    public Message selectMessage(Message message, int accountId);

    /**
     *
     * @param messageId
     * @param accountId
     * @return
     */
    public boolean updateIsread(int messageId, int accountId);

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public int getCountUnreadMsg(int userId, int accountId);

    /**
     *
     * @param teamId
     * @param messageId
     * @param type
     * @param accountId
     * @return
     */
    public List<Message> selectTeamMessages(int teamId, int messageId, int type, int accountId);

    /**
     *
     * @param teamId
     * @param userId1
     * @param userId2
     * @param messageId
     * @param type
     * @param accountId
     * @return
     */
    public List<Message> selectUserTeamMessages(int teamId, int userId1, int userId2, int messageId, int type, int accountId);

    /**
     *
     * @param positionId
     * @param userId
     * @param accountId
     * @return
     */
    public List<Message> selectMembersWhoMessaged(int positionId, int userId, int accountId);

    /**
     *
     * @param senderId
     * @param receiverId
     * @param accountId
     * @return
     */
    public List<Message> selectRecivedMembers(int senderId, int receiverId, int accountId);
    
    /**
     *
     * @param date
     * @param userId
     * @param accountId
     * @return
     */
    public List<TeamMember> selectTeamMembersRecentMessaged(String date, int userId, int accountId);
    
    public int getUnreadMessageCountOfUsersWhoMessaged(int senderId, int userId, int accountId); // Added by jyoti
}
