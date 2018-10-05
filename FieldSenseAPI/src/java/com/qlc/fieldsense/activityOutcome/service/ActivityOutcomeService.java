/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.activityOutcome.service;

import com.qlc.fieldsense.activityOutcome.model.ActivityOutcomeManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author anuja
 */
@Controller
@RequestMapping("/activityOutcome")
public class ActivityOutcomeService {

    ActivityOutcomeManager aOutcomeManager = new ActivityOutcomeManager();

    /**
     * 
     * @param userToken
     * @return list of all activity outcomes
     * @purpose used to get  list of all activity outcomes
     */
    @RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
    public @ResponseBody
    Object getAllOutcomePurpose(@RequestHeader(value = "userToken") String userToken) {
        return aOutcomeManager.getAllActivityOutcomes(userToken);
    }
}
