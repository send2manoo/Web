/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.feedback.service;

import com.qlc.fieldsense.feedback.model.Feedback;
import com.qlc.fieldsense.feedback.model.FeedbackManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Ramesh
 * @date 02-02-2015
 */
@Controller
@RequestMapping("/feedback")
public class FeedbackService {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("FeedbackService");
    FeedbackManager feedbackManager = new FeedbackManager();

    /**
     * 
     * @param feedback
     * @param userToken
     * @return 
     * @purpose used to send feed back    
     */
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object sendUserFeedback(@RequestBody Feedback feedback, @RequestHeader(value = "userToken") String userToken) {
        log4jLog.info("FeedbackService sendUserFeedback method is called ");
        return feedbackManager.sendUserFeedback(feedback, userToken);
    }
}
