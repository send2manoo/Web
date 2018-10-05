/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.userKey.service;

import com.qlc.fieldsense.userKey.model.UserKey;
import com.qlc.fieldsense.userKey.model.UserKeyManager;
import javax.validation.Valid;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author anuja
 */
@Controller
@RequestMapping("/userKey")
public class UserKeyService {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("UserKeyService");
    UserKeyManager userKeyManager = new UserKeyManager();

    /**
     * 
     * @param userKey
     * @param userToken
     * @return 
     * @purpose Used to create userKey 
     */
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object createUserKey(@Valid @RequestBody UserKey userKey, @RequestHeader(value = "userToken") String userToken) {
        return userKeyManager.createUserKey(userKey, userToken);
    }

    /**
     * 
     * @param userToken
     * @return list of all userkeys. 
     */
    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectUserKey(@RequestHeader(value = "userToken") String userToken) {
        return userKeyManager.selectUserKey(userToken);
    }

    /**
     * 
     * @param userKey
     * @param date
     * @param userToken
     * @return 
     * @purpose used to get details of userkeys for specific day 
     */
    @RequestMapping(value = "/{userKey}/{date}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectUserKey(@PathVariable("userKey") String userKey,@PathVariable("date") String date, @RequestHeader(value = "userToken") String userToken) {
        return userKeyManager.selectUserKey(userKey,date, userToken);
    }

    /**
     * 
     * @param userKey
     * @param userToken
     * @return 
     * @purpose Used to update userKey
     */
    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object updateUserKeys(@RequestBody UserKey userKey, @RequestHeader(value = "userToken") String userToken) {
        return userKeyManager.updateUserKey(userKey, userToken);
    }

    /**
     * 
     * @param userKey
     * @param userToken
     * @return 
     * @purpose Used to delete userKey 
     */
    @RequestMapping(value = "/{userKey}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    Object deleteUserKey(@PathVariable String userKey, @RequestHeader(value = "userToken") String userToken) {
        return userKeyManager.deleteUserKey(userKey, userToken);
    }
}
