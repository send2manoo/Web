/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.message.dao;

import com.qlc.fieldsense.message.model.Message;
import com.qlc.fieldsense.team.model.TeamMember;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.userKey.dao.UserKayDaoImpl;
import com.qlc.fieldsense.userKey.model.UserKey;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 *
 * @author Ramesh
 * @date 10-03-2014
 */
public class MessageDaoImpl implements MessageDao {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("MessageDaoImpl");

    /**
     *
     * @param senderId
     * @param receiverId
     * @param connectionId
     * @param connectionType
     * @param accountId
     * @return
     */
    @Override
    public boolean insertMessageConnection(int senderId, int receiverId, int connectionId, int connectionType, int accountId) {
        String query = "INSERT INTO message_connections(user_id_fk_1,user_id_fk_2,connection_id_fk,connection_type,created_on) VALUES (?,?,?,?,now())";
        log4jLog.info(" insertMessageConnection " + query);
        Object param[] = new Object[]{senderId, receiverId, connectionId, connectionType};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" insertMessageConnection " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param connectionId
     * @param messageId
     * @param message
     * @param senderId
     * @param accountId
     * @return
     */
    @Override
    public boolean insertMessage(int connectionId, int messageId, String message, int senderId, int accountId) {
        try {
            String query = "INSERT INTO messages(id,message_connection_id_fk,message,user_id_fk,created_on,modified_on)VALUES(?,?,?,?,now(),now())";
            log4jLog.info(" insertMessage " + query);
            Object[] param = new Object[]{messageId, connectionId, message, senderId};
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" insertMessage " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param senderId
     * @param receiverId
     * @param connectionType
     * @param account
     * @return
     */
    @Override
    public int getConnectionId(int senderId, int receiverId, int connectionType, int account) {
        String query = "SELECT connection_id_fk FROM message_connections WHERE user_id_fk_1=? AND user_id_fk_2=? AND connection_type=?";
        log4jLog.info(" getConnectionId " + query);
        Object[] param = new Object[]{senderId, receiverId, connectionType};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(account).queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" getConnectionId " + e);
//            e.printStackTrace();
            return 0;
        }
    }

    /**
     *
     * @param teamId
     * @param accountId
     * @return
     */
    @Override
    public List<Message> selectTeamMessages(int teamId, int accountId) {
//        String query = "SELECT id,message,user_id_fk,message_connection_id_fk,created_on FROM messages WHERE message_connection_id_fk=(SELECT DISTINCT(connection_id_fk) FROM message_connections WHERE connection_type=2 AND user_id_fk_2=?)";
        StringBuilder query = new StringBuilder();
        query.append("SELECT messages.id,message,user_id_fk,sender.first_name,sender.last_name,sender.email_address,message_connection_id_fk,messages.created_on,is_read");
        query.append(" FROM messages INNER JOIN message_connections ON message_connection_id_fk=connection_id_fk AND connection_type=2 AND user_id_fk_1=?");
        query.append(" INNER JOIN fieldsense.users sender ON messages.user_id_fk= sender.id;");
        log4jLog.info(" selectTeamMessages " + query);
        Object param[] = new Object[]{teamId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Message>() {
                @Override
                public Message mapRow(ResultSet rs, int i) throws SQLException {
                    Message message = new Message();
                    message.setId(rs.getInt("id"));
                    message.setMessage(rs.getString("message"));
                    message.setMessageType(2);
                    message.setCreatedOn(rs.getTimestamp("created_on"));
                    message.setIsRead(rs.getBoolean("is_read"));

                    User sender = new User();
                    sender.setId(rs.getInt("user_id_fk"));
                    sender.setAccountId(0);
                    sender.setFirstName(rs.getString("first_name"));
                    sender.setLastName(rs.getString("last_name"));
                    sender.setEmailAddress(rs.getString("email_address"));
                    sender.setPassword("");
                    sender.setMobileNo("");
                    sender.setGender(0);
                    sender.setRole(0);
                    sender.setActive(false);
                    sender.setLastLoggedOn(new Timestamp(0));
                    sender.setLastKnownLocationTime(new Timestamp(0));
                    sender.setLastKnownLocation("");
                    sender.setCreatedOn(new Timestamp(0));
                    sender.setCreatedBy(0);

                    message.setSender(sender);

                    return message;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectTeamMessages " + e);
//            e.printStackTrace();
            return new ArrayList<Message>();
        }
    }

    /**
     *
     * @param userId1
     * @param userId2
     * @param accountId
     * @return
     */
    @Override
    public List<Message> selectUserMessages(int userId1, int userId2, int accountId) {
        String query = "SELECT id,message,user_id_fk,message_connection_id_fk,created_on FROM messages WHERE message_connection_id_fk=(SELECT DISTINCT(connection_id_fk) FROM message_connections WHERE connection_type=1 AND user_id_fk_2=?)";
        log4jLog.info(" selectUserMessages " + query);
        Object param[] = new Object[]{userId2};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<Message>() {
                @Override
                public Message mapRow(ResultSet rs, int i) throws SQLException {
                    Message message = new Message();
                    message.setId(rs.getInt("id"));
                    message.setMessage(rs.getString("message"));
                    message.setMessageType(2);
//                    message.setSenderId(rs.getInt("user_id_fk"));
//                    message.getTeam().setId(rs.getInt("message_connection_id_fk"));
                    return message;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectUserMessages " + e);
//            e.printStackTrace();
            return new ArrayList<Message>();
        }
    }

    /**
     *
     * @param connectionId
     * @param messageId
     * @param accountId
     * @return
     */
    @Override
    public boolean insertMessageSpool(int connectionId, int messageId, int accountId) {
        String query = "INSERT INTO message_spool(message_connection_id_fk,message_id_fk,created_on)VALUES(?,?,now())";
        log4jLog.info(" insertMessageSpool " + query);
        Object[] param = new Object[]{connectionId, messageId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" insertMessageSpool " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param teamId
     * @param userId1
     * @param userId2
     * @param accountId
     * @return
     */
    @Override
    public List<Message> selectUsersTeamMessages(int teamId, int userId1, int userId2, int accountId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT messages.id,message,user_id_fk,sender.first_name,sender.last_name,sender.email_address,message_connection_id_fk,messages.created_on,is_read");
        query.append(" FROM messages INNER JOIN message_connections ON message_connection_id_fk=connection_id_fk AND connection_type=2 AND user_id_fk_1=?");
        query.append(" INNER JOIN fieldsense.users sender ON messages.user_id_fk= sender.id AND user_id_fk IN (?,?)");
        log4jLog.info(" selectTeamMessages " + query);
        Object param[] = new Object[]{teamId, userId1, userId2};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Message>() {
                @Override
                public Message mapRow(ResultSet rs, int i) throws SQLException {
                    Message message = new Message();
                    message.setId(rs.getInt("id"));
                    message.setMessage(rs.getString("message"));
                    message.setMessageType(2);
                    message.setCreatedOn(rs.getTimestamp("created_on"));
                    message.setIsRead(rs.getBoolean("is_read"));
                    User sender = new User();
                    sender.setId(rs.getInt("user_id_fk"));
                    sender.setAccountId(0);
                    sender.setFirstName(rs.getString("first_name"));
                    sender.setLastName(rs.getString("last_name"));
                    sender.setEmailAddress(rs.getString("email_address"));
                    sender.setPassword("");
                    sender.setMobileNo("");
                    sender.setGender(0);
                    sender.setRole(0);
                    sender.setActive(false);
                    sender.setLastLoggedOn(new Timestamp(0));
                    sender.setLastKnownLocationTime(new Timestamp(0));
                    sender.setLastKnownLocation("");
                    sender.setCreatedOn(new Timestamp(0));
                    sender.setCreatedBy(0);

                    message.setSender(sender);

                    return message;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectTeamMessages " + e);
//            e.printStackTrace();
            return new ArrayList<Message>();
        }
    }

    /**
     *
     * @param userId1
     * @param userId2
     * @param accountId
     * @return
     */
    @Override
    public List<Message> selectUserToUserMessages(int userId1, int userId2, int accountId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT messages.id,message,user_id_fk,sender.first_name,sender.last_name,sender.email_address,message_connection_id_fk,messages.created_on,is_read,modified_on");
        query.append(" FROM messages INNER JOIN message_connections ON message_connection_id_fk=connection_id_fk ");
        query.append(" INNER JOIN fieldsense.users sender ON messages.user_id_fk= sender.id WHERE message_connection_id_fk=");
        query.append(" (SELECT DISTINCT(connection_id_fk) FROM message_connections WHERE connection_type=1 AND user_id_fk_2=? AND user_id_fk_1=?)");

        /*query.append("SELECT DISTINCT messages.id,message,user_id_fk,sender.first_name,sender.last_name,sender.email_address,message_connection_id_fk,messages.created_on,is_read,user_id_fk_2");
         query.append(" FROM messages INNER JOIN message_connections ON message_connection_id_fk=connection_id_fk ");
         query.append(" INNER JOIN fieldsense.users sender ON messages.user_id_fk= sender.id WHERE message_connection_id_fk=");
         query.append(" (SELECT DISTINCT(connection_id_fk) FROM message_connections WHERE connection_type=1 AND user_id_fk_2=? AND user_id_fk_1=?)");*/
        log4jLog.info(" selectUserToUserMessages " + query);
        Object param[] = new Object[]{userId2, userId1};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Message>() {
                @Override
                public Message mapRow(ResultSet rs, int i) throws SQLException {
                    Message message = new Message();
                    message.setId(rs.getInt("id"));
                    message.setMessage(rs.getString("message"));
                    message.setMessageType(1);
                    message.setCreatedOn(rs.getTimestamp("created_on"));
                    message.setModifiedOn(rs.getTimestamp("modified_on"));
                    message.setIsRead(rs.getBoolean("is_read"));

                    User sender = new User();
                    sender.setId(rs.getInt("user_id_fk"));
                    sender.setFirstName(rs.getString("first_name"));
                    sender.setLastName(rs.getString("last_name"));
                    sender.setEmailAddress(rs.getString("email_address"));

                    message.setSender(sender);
//                    message.setReceiverId(rs.getInt("user_id_fk_2"));
                    return message;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectUserToUserMessages " + e);
//            e.printStackTrace();
            return new ArrayList<Message>();
        }
    }

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    @Override
    public List<Message> selectTeamMembersWhoMessaged(final int userId, final int accountId) {
        StringBuilder query = new StringBuilder();
        /* query.append("SELECT DISTINCT user_id_fk_2,first_name,last_name,email_address,connection_id_fk FROM message_connections INNER JOIN fieldsense.users as u ON user_id_fk_2=u.id");
         query.append(" AND u.active=1 WHERE user_id_fk_1=? AND connection_type=1");*/

        //query.append("SELECT DISTINCT user_id_fk_2,first_name,last_name,email_address,connection_id_fk FROM message_connections INNER JOIN messages ON connection_id_fk=message_connection_id_fk INNER JOIN fieldsense.users as u ON user_id_fk_2=u.id");
       // query.append(" AND u.active=1 WHERE user_id_fk_1=? AND connection_type=1");
        query.append("SELECT  max(m.created_on) created_on,conn.user_id_fk_2,u.first_name,u.last_name,u.email_address,conn.connection_id_fk FROM ");
        query.append("message_connections conn INNER JOIN messages m ON conn.connection_id_fk=m.message_connection_id_fk INNER JOIN ");
        query.append("fieldsense.users as u ON conn.user_id_fk_2=u.id AND u.active=1 WHERE conn.user_id_fk_1=? AND u.id != ? AND conn.connection_type=1 group ");
        query.append("by conn.user_id_fk_2,conn.connection_id_fk order by created_on desc");
        
        Object param[] = new Object[]{userId, userId}; // Added by jyoti, self message displaying avoided
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Message>() {
                @Override
                public Message mapRow(ResultSet rs, int i) throws SQLException {
                    Message message = new Message();
                    User user = new User();
                    user.setId(rs.getInt("user_id_fk_2"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmailAddress(rs.getString("email_address"));
                    message.setSender(user);
                    message.setConnectionId(rs.getInt("connection_id_fk"));
                    message.setCreatedOn(rs.getTimestamp("created_on"));
                    message.setUnReadMsgcount(getUnreadMessageCountOfUsersWhoMessaged(user.getId(), userId, accountId)); // Added by Jyoti
                    return message;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectTeamMembersWhoMessaged " + e);
            e.printStackTrace();
            return new ArrayList<Message>();
        }
    }

    /**
     * @Added by jyoti
     * @param senderId
     * @param userId
     * @param accountId
     * @return 
     */
    @Override
    public int getUnreadMessageCountOfUsersWhoMessaged(int senderId, int userId, int accountId) {
        String query = "SELECT COUNT(m.id) AS unreadmsgcount FROM message_connections conn INNER JOIN messages m ON conn.connection_id_fk = m.message_connection_id_fk "
                + " WHERE conn.user_id_fk_1 = ? AND conn.user_id_fk_2 = ? AND conn.user_id_fk_2 != ? AND conn.connection_type = 1 AND m.is_read = 0 ";
//                + " group by conn.user_id_fk_2, conn.connection_id_fk ";
        log4jLog.info("getCountUnreadMsg:" + query);
        Object param[] = new Object[]{userId, senderId, userId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info("getCountUnreadMsg" + e);
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     *
     * @param message
     * @param accountId
     * @return
     */
    @Override
    public Message selectMessage(Message message, int accountId) {
        String query = "SELECT DISTINCT id,message,created_on,is_read,user_id_fk FROM messages WHERE id = (SELECT MAX(id) FROM messages WHERE message_connection_id_fk=?)";
        Object param[] = new Object[]{message.getConnectionId()};
        log4jLog.info("selectMessage " + query);
        try {
            SqlRowSet srs = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForRowSet(query.toString(), param);
            while (srs.next()) {
                message.setId(srs.getInt("id"));
                message.setMessage(srs.getString("message"));
                message.setCreatedOn(srs.getTimestamp("created_on"));
                message.setIsRead(srs.getBoolean("is_read"));
                message.setReceiverId(srs.getInt("user_id_fk"));
            }
            return message;
        } catch (Exception e) {
            log4jLog.info("selectMessage " + e);
            return message;
        }
    }

    /**
     *
     * @param messageId
     * @param accountId
     * @return
     */
    @Override
    public boolean updateIsread(int messageId, int accountId) {
        String query = "UPDATE messages SET is_read = 1,modified_on=now() WHERE id=?";
        log4jLog.info("updateIsread" + query);
        Object param[] = new Object[]{messageId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info("updateIsread" + e);
            return false;
        }
    }

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    @Override
    public int getCountUnreadMsg(int userId, int accountId) {
        String query = "SELECT COUNT(id) FROM messages WHERE is_read=0 AND user_id_fk != ? AND message_connection_id_fk IN (SELECT DISTINCT connection_id_fk FROM message_connections WHERE user_id_fk_1=?)";
        log4jLog.info("getCountUnreadMsg:" + query);
        Object param[] = new Object[]{userId, userId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info("getCountUnreadMsg" + e);
            return 0;
        }
    }

    /**
     *
     * @param teamId
     * @param messageId
     * @param type
     * @param accountId
     * @return
     */
    @Override
    public List<Message> selectTeamMessages(int teamId, int messageId, int type, int accountId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT m.id,m.message,user_id_fk,sender.first_name,sender.last_name,sender.email_address,message_connection_id_fk,m.created_on,is_read");
        query.append(" FROM messages m");
        query.append(" INNER JOIN message_connections ON message_connection_id_fk=connection_id_fk AND connection_type=2 AND user_id_fk_1=?");
        query.append(" INNER JOIN fieldsense.users sender ON m.user_id_fk= sender.id");
        if (type == 1) {
            query.append(" WHERE m.id > ?");
        } else if (type == 2) {
            query.append(" WHERE m.id < ?");
        }

        Object param[] = new Object[]{teamId, messageId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Message>() {
                @Override
                public Message mapRow(ResultSet rs, int i) throws SQLException {
                    Message message = new Message();
                    message.setId(rs.getInt("id"));
                    message.setMessage(rs.getString("message"));
                    message.setMessageType(2);
                    message.setCreatedOn(rs.getTimestamp("created_on"));
                    message.setIsRead(rs.getBoolean("is_read"));

                    User sender = new User();
                    sender.setId(rs.getInt("user_id_fk"));
                    sender.setAccountId(0);
                    sender.setFirstName(rs.getString("first_name"));
                    sender.setLastName(rs.getString("last_name"));
                    sender.setEmailAddress(rs.getString("email_address"));
                    sender.setPassword("");
                    sender.setMobileNo("");
                    sender.setGender(0);
                    sender.setRole(0);
                    sender.setActive(false);
                    sender.setLastLoggedOn(new Timestamp(0));
                    sender.setLastKnownLocationTime(new Timestamp(0));
                    sender.setLastKnownLocation("");
                    sender.setCreatedOn(new Timestamp(0));
                    sender.setCreatedBy(0);

                    message.setSender(sender);

                    return message;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectTeamMessages" + e);
            return new ArrayList<Message>();
        }
    }

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
    @Override
    public List<Message> selectUserTeamMessages(int teamId, int userId1, int userId2, int messageId, int type, int accountId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT messages.id,message,user_id_fk,sender.first_name,sender.last_name,sender.email_address,message_connection_id_fk,messages.created_on,is_read,modified_on");
        query.append(" FROM messages INNER JOIN message_connections ON message_connection_id_fk=connection_id_fk ");
        query.append(" INNER JOIN fieldsense.users sender ON messages.user_id_fk= sender.id WHERE message_connection_id_fk=");
        query.append(" (SELECT DISTINCT(connection_id_fk) FROM message_connections WHERE connection_type=1 AND user_id_fk_2=? AND user_id_fk_1=?)");

//        query.append("SELECT DISTINCT messages.id,message,user_id_fk,sender.first_name,sender.last_name,sender.email_address,message_connection_id_fk,messages.created_on,is_read");
//        query.append(" FROM messages");
//        query.append(" INNER JOIN message_connections ON message_connection_id_fk=connection_id_fk AND connection_type=2 AND user_id_fk_1=?");
//        query.append(" INNER JOIN fieldsense.users sender ON messages.user_id_fk= sender.id AND user_id_fk IN (?,?)");
        if (type == 1) {
//            query.append(" WHERE messages.id > ?");
            query.append(" AND messages.id > ?");
        } else if (type == 2) {
//            query.append(" WHERE messages.id < ?");
            query.append(" AND messages.id < ?");
        }
        log4jLog.info(" selectTeamMessages " + query);
//        Object param[] = new Object[]{teamId, userId1, userId2, messageId};
        Object param[] = new Object[]{userId2, userId1, messageId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Message>() {
                @Override
                public Message mapRow(ResultSet rs, int i) throws SQLException {
                    Message message = new Message();
                    message.setId(rs.getInt("id"));
                    message.setMessage(rs.getString("message"));
//                    message.setMessageType(2);
                    message.setMessageType(1);
                    message.setCreatedOn(rs.getTimestamp("created_on"));
                    message.setModifiedOn(rs.getTimestamp("modified_on"));
                    message.setIsRead(rs.getBoolean("is_read"));
                    User sender = new User();
                    sender.setId(rs.getInt("user_id_fk"));
                    sender.setFirstName(rs.getString("first_name"));
                    sender.setLastName(rs.getString("last_name"));
                    sender.setEmailAddress(rs.getString("email_address"));

                    message.setSender(sender);

                    return message;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectTeamMessages " + e);
//            e.printStackTrace();
            return new ArrayList<Message>();
        }
    }

    /**
     *
     * @param positionId
     * @param userId
     * @param accountId
     * @return
     */
    @Override
    public List<Message> selectMembersWhoMessaged(int positionId, int userId, int accountId) {
        StringBuilder query1 = new StringBuilder();
//        String query="SELECT id FROM teams WHERE user_id_fk=?";
//        Object param[] = new Object[]{userId};
        query1.append("SELECT DISTINCT user_id_fk_2,first_name,last_name,email_address,connection_id_fk FROM message_connections INNER JOIN ");
        query1.append(" messages ON connection_id_fk=message_connection_id_fk INNER JOIN fieldsense.users as u ON user_id_fk_2=u.id");
        query1.append(" AND u.active=1 WHERE user_id_fk_1=? AND connection_type=1 AND user_id_fk_2 IN (SELECT user_id_fk FROM teams WHERE team_position_csv NOT LIKE '%" + positionId + "%')");
        try {
//            int positionId = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
            Object param1[] = new Object[]{userId};

            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query1.toString(), param1, new RowMapper<Message>() {
                @Override
                public Message mapRow(ResultSet rs, int i) throws SQLException {
                    Message message = new Message();
                    User user = new User();
                    user.setId(rs.getInt("user_id_fk_2"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmailAddress(rs.getString("email_address"));
                    message.setSender(user);
                    message.setConnectionId(rs.getInt("connection_id_fk"));

                    return message;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectMembersWhoMessaged " + e);
            return new ArrayList<Message>();
        }
    }

    /**
     *
     * @param senderId
     * @param receiverId
     * @param accountId
     * @return
     */
    @Override
    public List<Message> selectRecivedMembers(int senderId, int receiverId, int accountId) {
        StringBuilder query1 = new StringBuilder();
        query1.append("SELECT DISTINCT m.id,m.is_read FROM messages as m INNER JOIN message_connections as mco ");
        query1.append(" ON message_connection_id_fk=connection_id_fk WHERE m.user_id_fk=? AND mco.user_id_fk_2=?");
        try {
            Object param1[] = new Object[]{senderId, receiverId};

            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query1.toString(), param1, new RowMapper<Message>() {
                @Override
                public Message mapRow(ResultSet rs, int i) throws SQLException {
                    Message message = new Message();
                    message.setId(rs.getInt("id"));
                    message.setIsRead(rs.getBoolean("is_read"));
                    return message;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectRecivedMembers " + e);
            return new ArrayList<Message>();
        }
    }

    /**
     *
     * @param date
     * @param userId
     * @param accountId
     * @return
     */
    @Override
    public List<TeamMember> selectTeamMembersRecentMessaged(String date, int userId, int accountId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT user_id_fk_2,first_name,last_name,");
        query.append(" IFNULL(a.punch_in,0) punchIn,IFNULL(a.punch_out,0) punchOut,IFNULL(uk.key_value,0) canTakeCalls,IFNULL(ukm.key_value,0) inMeeting,");
        query.append(" IFNULL(DATE(uk.modified_on),DATE(now())) uk_modifiedDay,now() new_uk_modified_date");
        query.append(" FROM message_connections as mco INNER JOIN messages as m ON connection_id_fk=message_connection_id_fk INNER JOIN fieldsense.users as u ON user_id_fk_2=u.id AND u.active=1");
        query.append(" LEFT OUTER JOIN attendances a ON a.user_id_fk=mco.user_id_fk_2 AND a.punch_date = CURDATE()");
        query.append(" LEFT OUTER JOIN user_keys uk ON uk.user_id_fk=u.id AND uk.user_key='CanTakeCalls'");
        query.append(" LEFT OUTER JOIN user_keys ukm ON ukm.user_id_fk=u.id AND ukm.user_key='InMeeting'");
        query.append(" WHERE user_id_fk_1=? AND u.id != ? AND connection_type=1 AND m.created_on BETWEEN DATE_SUB(now(), interval 7 day) AND now() ORDER BY m.created_on DESC");

//        Object param[] = new Object[]{userId};
//        try {
//            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Message>() {
//                @Override
//                public Message mapRow(ResultSet rs, int i) throws SQLException {
//                    Message message = new Message();
//                    User user = new User();
//                    user.setId(rs.getInt("user_id_fk_2"));
//                    user.setFirstName(rs.getString("first_name"));
//                    user.setLastName(rs.getString("last_name"));
//                    user.setEmailAddress(rs.getString("email_address"));
//                    message.setSender(user);
//                    message.setConnectionId(rs.getInt("connection_id_fk"));
//
//                    return message;
//                }
//            });
//        } catch (Exception e) {
//            log4jLog.info(" selectTeamMembersWhoMessaged " + e);
//            return new ArrayList<Message>();
//        }
        Connection connection = null;
        try {
            connection = FieldSenseUtils.getDataSourceForAccount(accountId).getConnection();
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(query.toString());
            stmt.setInt(1, userId);
            stmt.setInt(2, userId); // Added by jyoti, to avoid displaying self user in recent messages
            connection.commit();
            ResultSet rs = stmt.executeQuery();
            List<TeamMember> listOfTeams = new ArrayList<TeamMember>();
            while (rs.next()) {

                String modifiedDate = rs.getString("uk_modifiedDay");

                TeamMember teamMember = new TeamMember();
                User user = new User();
                user.setId(rs.getInt("user_id_fk_2"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                teamMember.setUser(user);
                String punchIn = rs.getString("punchIn");
                String punchOut = rs.getString("punchOut");
                if (punchIn.equals("0")) {
                    teamMember.setIsOnline(false);
                } else if (punchOut.equals("00:00:00")) {
                    teamMember.setIsOnline(true);
                } else {
                    teamMember.setIsOnline(false);
                }
//                teamMember.setCreatedOn(rs.getTimestamp("created_on"));
                if (date.equals(modifiedDate)) {
                    int canTakeCall = rs.getInt("canTakeCalls");
                    if (canTakeCall == 1) {
                        teamMember.setCanTakeCalls(true);
                    } else {
                        teamMember.setCanTakeCalls(false);
                    }
                    int inMeeting = rs.getInt("inMeeting");
                    if (inMeeting == 1) {
                        teamMember.setInMeeting(true);
                    } else {
                        teamMember.setInMeeting(false);
                    }
                } else {
                    teamMember.setCanTakeCalls(false);
                    teamMember.setInMeeting(false);

                    UserKayDaoImpl userKayDaoImpl = new UserKayDaoImpl();

                    UserKey keyCantakeCall = new UserKey();
                    keyCantakeCall.setKeyValue("0");
                    User userCanTakeCall = new User();
                    userCanTakeCall.setId(rs.getInt("user_id_fk_2"));
                    keyCantakeCall.setUserId(userCanTakeCall);
                    keyCantakeCall.setUserKay("CanTakeCalls");
                    userKayDaoImpl.updateUserKeys(keyCantakeCall, accountId);

                    UserKey keyInMeeting = new UserKey();
                    keyInMeeting.setKeyValue("0");
                    keyInMeeting.setUserId(userCanTakeCall);
                    keyInMeeting.setUserKay("InMeeting");
                    userKayDaoImpl.updateUserKeys(keyInMeeting, accountId);

                }
                listOfTeams.add(teamMember);
            }
            return listOfTeams;
        } catch (Exception e) {
            log4jLog.info("selectOraganizationChart" + e);
//            e.printStackTrace();
            return new ArrayList<TeamMember>();
        }finally{
            try{
                if(connection!=null) connection.close();               
            }catch(Exception e){
                log4jLog.info("selectOraganizationChart" + e);
//                e.printStackTrace();
            }
        }
    }
}
