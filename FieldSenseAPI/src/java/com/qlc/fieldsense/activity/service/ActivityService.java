/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.activity.service;

import com.qlc.fieldsense.activity.model.Activity;
import com.qlc.fieldsense.activity.model.ActivityManager;
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
 * @structure changed and not in use anymore
 */
@Deprecated
@Controller
@RequestMapping("/activity")
public class ActivityService {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("ActivityService");
    ActivityManager activityManager = new ActivityManager();

    /**
     *
     * @param teamId
     * @param userToken
     * @return
     * @deprecated
     */
    @Deprecated
    @RequestMapping(value="/{teamId}", method = RequestMethod.GET)
    public @ResponseBody
    Object selectActivity(@PathVariable int teamId, @RequestHeader(value = "userToken") String userToken) {
        return activityManager.getActivity(teamId, userToken);
    }

    /**
     *
     * @param teamId
     * @param userId
     * @param userToken
     * @return
     * @deprecated
     */
    @Deprecated
    @RequestMapping(value = "/{teamId}/user/{userId}", method = RequestMethod.GET)
    public @ResponseBody
    Object selectUserActivity(@PathVariable int teamId, @PathVariable int userId, @RequestHeader(value = "userToken") String userToken) {
        return activityManager.getUserActivity(teamId, userId, userToken);
    }
    
    /**
     *
     * @param activity
     * @param userToken
     * @return
     * @deprecated
     */
    @Deprecated
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    Object createActivity(@RequestBody Activity activity,@RequestHeader(value = "userToken") String userToken){
        return activityManager.createActivity(activity, userToken);
    }
/**
 * @author Ramesh
     * @param customerId
     * @param userToken
     * @return 
 * @date 09-05-2014
 * @purpose This service is used to get the customer activity .
 *
 */
    @Deprecated
    @RequestMapping(value="customer/{customerId}", method = RequestMethod.GET)
    public @ResponseBody
    Object selectCustomerActivity(@PathVariable int customerId, @RequestHeader(value = "userToken") String userToken) {
        return activityManager.getCustomerActivity(customerId, userToken);
    }
}
