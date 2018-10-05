/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.usersTravelLogs.service;

import com.qlc.fieldsense.usersTravelLogs.model.UsersTravelLogs;
import com.qlc.fieldsense.usersTravelLogs.model.UsersTravelLogsManager;
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
@RequestMapping("/userTravelLog")
public class UsersTravelLogsService {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("UsersTravelLogsService");
    UsersTravelLogsManager usersTravelLogsManager = new UsersTravelLogsManager();

    /**
     * 
     * @param usersTravelLogs
     * @param userToken
     * @return 
     * @purpose Used to create user travel logs
     */
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object createUsersTravelLogs(@Valid @RequestBody UsersTravelLogs usersTravelLogs, @RequestHeader(value = "userToken") String userToken) {
        return usersTravelLogsManager.createUsersTravelLogs(usersTravelLogs, userToken);
    }

    /**
     * 
     * @param userToken
     * @return list of travels logs
     */
    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectUsersTravelLogs(@RequestHeader(value = "userToken") String userToken) {
        return usersTravelLogsManager.selectUsersTravelLogs(userToken);
    }

            /**
         * @added by nikhil
     * TimeLine Trail Purpose
     * @param userId
     * @param date
     * @param userToken
     * @return 
     * @purpose Used to get details of travels logs of specified user for specified date. Date formate is yyyy-MM-dd
     */
    @RequestMapping(value = "/user1/{userId}/{date}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectUsersTravelLog1(@PathVariable("userId") int userId, @PathVariable("date") String date, @RequestHeader(value = "userToken") String userToken) {
        return usersTravelLogsManager.selectUsersTravelLogs1(userId, date, userToken);
    }
    /**
     * 
     * @param userId
     * @param date
     * @param userToken
     * @return 
     * @purpose Used to get details of travels logs of specified user for specified date. Date formate is yyyy-MM-dd
     */
    @RequestMapping(value = "/user/{userId}/{date}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectUsersTravelLog(@PathVariable("userId") int userId, @PathVariable("date") String date, @RequestHeader(value = "userToken") String userToken) {
        return usersTravelLogsManager.selectUsersTravelLogs(userId, date, userToken);
    }

    /**
     * 
     * @param location
     * @param userToken
     * @return 
     * @purpose Used to get details of travels logs based on location
     */
    @RequestMapping(value = "/{location}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selecUsersTravelLogs(@PathVariable String location, @RequestHeader(value = "userToken") String userToken) {
        return usersTravelLogsManager.selectUsersTravelLog(location, userToken);
    }
}
