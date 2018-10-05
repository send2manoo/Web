/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.userKey.dao;

import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.userKey.model.UserKey;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author anuja
 */
public class UserKayDaoImpl implements UserKeyDao {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("UserKayDaoImpl");

    /**
     *
     * @param userKey
     * @param accountId
     * @return
     */
    @Override
    public int insertUserKey(UserKey userKey, int accountId) {
        String query = "INSERT INTO user_keys(user_id_fk,user_key,key_value,created_on,modified_on) VALUES (?,?,?,now(),now())";
        Object param[] = new Object[]{userKey.getUserId().getId(), userKey.getUserKay(), userKey.getKeyValue()};
        try {
            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    return FieldSenseUtils.getMaxIdForTable("user_keys", accountId);
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return 0;
        }
    }

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    @Override
    public UserKey selectUserKey(int id, int accountId) {
        try {
            synchronized (this) {
                String query = "SELECT id,user_id_fk,user_key,key_value,created_on,modified_on FROM user_keys WHERE id=?";
                Object[] param = new Object[]{id};
                return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, new RowMapper<UserKey>() {

                    @Override
                    public UserKey mapRow(ResultSet rs, int i) throws SQLException {
                        UserKey userKey = new UserKey();
                        User user = new User();
                        userKey.setId(rs.getInt("id"));
                        user.setId(rs.getInt("user_id_fk"));
                        userKey.setUserId(user);
                        userKey.setUserKay(rs.getString("user_key"));
                        userKey.setKeyValue(rs.getString("key_value"));
                        userKey.setCreatedOn(rs.getTimestamp("created_on"));
                        userKey.setModifiedOn(rs.getTimestamp("modified_on"));
                        return userKey;
                    }
                });
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return new UserKey();
        }
    }

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    @Override
    public List<UserKey> selectUserKey(Integer userId, int accountId) {
        String query = "SELECT id,user_id_fk,user_key,key_value,created_on,modified_on FROM user_keys WHERE user_id_fk=?";
        Object param[] = new Object[]{userId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<UserKey>() {

                @Override
                public UserKey mapRow(ResultSet rs, int i) throws SQLException {
                    UserKey userKey = new UserKey();
                    User user = new User();
                    userKey.setId(rs.getInt("id"));
                    user.setId(rs.getInt("user_id_fk"));
                    userKey.setUserId(user);
                    userKey.setUserKay(rs.getString("user_key"));
                    userKey.setKeyValue(rs.getString("key_value"));
                    userKey.setCreatedOn(rs.getTimestamp("created_on"));
                    userKey.setModifiedOn(rs.getTimestamp("modified_on"));
                    return userKey;
                }
            });
        } catch (Exception e) {
//            e.printStackTrace();
            return new ArrayList<UserKey>();
        }
    }

    /**
     *
     * @param userId
     * @param userKey
     * @param accountId
     * @return
     */
    @Override
    public UserKey selectUserKey(Integer userId, String userKey, int accountId) {
        String query = "SELECT id,user_id_fk,user_key,key_value,created_on,modified_on FROM user_keys WHERE user_id_fk=? AND user_key=?";
        Object param[] = new Object[]{userId, userKey};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, new RowMapper<UserKey>() {

                @Override
                public UserKey mapRow(ResultSet rs, int i) throws SQLException {
                    UserKey userKey = new UserKey();
                    User user = new User();
                    userKey.setId(rs.getInt("id"));
                    user.setId(rs.getInt("user_id_fk"));
                    userKey.setUserId(user);
                    userKey.setUserKay(rs.getString("user_key"));
                    userKey.setKeyValue(rs.getString("key_value"));
                    userKey.setCreatedOn(rs.getTimestamp("created_on"));
                    userKey.setModifiedOn(rs.getTimestamp("modified_on"));
                    return userKey;
                }
            });
        } catch (Exception e) {
//            e.printStackTrace();
            return new UserKey();
        }
    }

    /**
     *
     * @param userKey
     * @param accountId
     * @return
     */
    @Override
    public UserKey updateUserKeys(UserKey userKey, int accountId) {
        String query = "UPDATE user_keys SET key_value=?,modified_on=now() WHERE user_id_fk=? AND user_key=?";
        Object param[] = new Object[]{userKey.getKeyValue(), userKey.getUserId().getId(), userKey.getUserKay()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                userKey = selectUserKey(userKey.getUserId().getId(), userKey.getUserKay(), accountId);
                return userKey;
            } else {
                return new UserKey();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new UserKey();
        }
    }

    /**
     *
     * @param userId
     * @param userKey
     * @param accountId
     * @return
     */
    @Override
    public UserKey deleteUserKey(Integer userId, String userKey, int accountId) {
        UserKey userKeys = new UserKey();
        userKeys = selectUserKey(userId, userKey, accountId);
        String query = "DELETE FROM user_keys WHERE user_id_fk=? AND user_key=?";
        Object param[] = new Object[]{userId, userKey};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return userKeys;
            } else {
                return new UserKey();
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return new UserKey();
        }
    }

    /**
     *
     * @param userId
     * @param userKey
     * @param accountId
     * @return
     */
    @Override
    public boolean isUserKeyValid(Integer userId, String userKey, int accountId) {
        String query = "SELECT COUNT(id) FROM user_keys WHERE user_id_fk=? AND user_key=?";
        Object[] param = new Object[]{userId, userKey};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param userId
     * @param userKey
     * @param date
     * @param accountId
     * @return
     */
    @Override
    public UserKey selectUserKey(Integer userId, String userKey, String date, int accountId) {
        final String date1 = date;
        final int accountId1 = accountId;
        String query = "SELECT id,user_id_fk,user_key,key_value,created_on,modified_on,DATE(modified_on) modifiedDay,now() new_modified_date FROM user_keys WHERE user_id_fk=? AND user_key=?";
        Object param[] = new Object[]{userId, userKey};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, new RowMapper<UserKey>() {

                @Override
                public UserKey mapRow(ResultSet rs, int i) throws SQLException {
                    String modifiedDate = rs.getString("modifiedDay");
                    UserKey userKey = new UserKey();
                    User user = new User();
                    userKey.setId(rs.getInt("id"));
                    user.setId(rs.getInt("user_id_fk"));
                    userKey.setUserId(user);
                    userKey.setUserKay(rs.getString("user_key"));
                    userKey.setCreatedOn(rs.getTimestamp("created_on"));
                    if (date1.equals(modifiedDate)) {
                        userKey.setKeyValue(rs.getString("key_value"));
                        userKey.setModifiedOn(rs.getTimestamp("modified_on"));
                    } else {
                        userKey.setKeyValue("0");
                        updateUserKeys(userKey, accountId1);
                        userKey.setModifiedOn(rs.getTimestamp("new_modified_date"));
                    }
                    return userKey;
                }
            });
        } catch (Exception e) {
//            e.printStackTrace();
            return new UserKey();
        }
    }
}
