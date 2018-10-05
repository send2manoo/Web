/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.userKey.dao;

import com.qlc.fieldsense.userKey.model.UserKey;
import java.util.List;

/**
 *
 * @author anuja
 */
public interface UserKeyDao {

    /**
     *
     * @param userKey
     * @param accountId
     * @return
     */
    public int insertUserKey(UserKey userKey, int accountId);

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    public UserKey selectUserKey(int id, int accountId);

    /**
     *
     * @param userId
     * @param userKey
     * @param date
     * @param accountId
     * @return
     */
    public UserKey selectUserKey(Integer userId, String userKey, String date, int accountId);

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public List<UserKey> selectUserKey(Integer userId, int accountId);

    /**
     *
     * @param userKey
     * @param accountId
     * @return
     */
    public UserKey updateUserKeys(UserKey userKey, int accountId);

    /**
     *
     * @param userId
     * @param userKey
     * @param accountId
     * @return
     */
    public UserKey deleteUserKey(Integer userId, String userKey, int accountId);

    /**
     *
     * @param userId
     * @param userKey
     * @param accountId
     * @return
     */
    public boolean isUserKeyValid(Integer userId, String userKey, int accountId);

    /**
     *
     * @param userId
     * @param userKey
     * @param accountId
     * @return
     */
    public UserKey selectUserKey(Integer userId, String userKey, int accountId);
}
