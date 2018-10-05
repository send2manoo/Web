/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.userKey.model;

import com.qlc.fieldsense.userKey.dao.UserKeyDao;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author anuja
 */
public class UserKeyManager {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("UserKeyManager");
    UserKeyDao userKeyDao = (UserKeyDao) GetApplicationContext.ac.getBean("userKayDaoImpl");
    FieldSenseUtils util = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");

     /**
     * @param userKey
     * @param token
     * @return 
     * @purpose Used to create userKey 
     */
    public Object createUserKey(UserKey userKey, String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                int userKeyId = userKeyDao.insertUserKey(userKey, accountId);
                if (userKeyId != 0) {
                    userKey = userKeyDao.selectUserKey(userKeyId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.USERKY_CREATED, " userKey ", userKey);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.USERKY_NOT_CREATED, "", "");
                }   
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /**
     * @param token
     * @return list of all userkeys. 
     */
    public Object selectUserKey(String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                Integer userId = util.userIdForToken(token);
                List<UserKey> userKeyList = userKeyDao.selectUserKey(userId, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "userKeyList", userKeyList);   
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /**
     * 
     * @param userKey
     * @param date
     * @param token
     * @return 
     * @purpose used to get details of userkeys for specific day 
     */
    public Object selectUserKey(String userKey, String date, String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                Integer userId = util.userIdForToken(token);
                if (userKeyDao.isUserKeyValid(userId, userKey, accountId)) {
                    UserKey userKeys = userKeyDao.selectUserKey(userId, userKey, date, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "userKeys", userKeys);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_USER, "", "");
                }   
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /**
     * @param userKey
     * @param token
     * @return 
     * @purpose Used to update userKey
     */
    public Object updateUserKey(UserKey userKey, String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                UserKey userKeys = userKeyDao.updateUserKeys(userKey, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.USERKEY_UPDATED, "userKeys", userKeys);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /**
     * @param userKey
     * @param token
     * @return 
     * @purpose Used to delete userKey 
     */
    public Object deleteUserKey(String userKey, String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                Integer userId = util.userIdForToken(token);
                if (userKeyDao.isUserKeyValid(userId, userKey, accountId)) {
                    UserKey userKeys = userKeyDao.deleteUserKey(userId, userKey, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.USERKEY_DELETED, "userKeys", userKeys);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_USER, "", "");
                }   
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }
}
