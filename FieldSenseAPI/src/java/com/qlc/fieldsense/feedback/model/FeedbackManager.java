/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.feedback.model;

import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.EmailNotification;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import org.apache.log4j.Logger;

/**
 *
 * @author Ramesh
 * @date 02-02-2015
 */
public class FeedbackManager {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("FeedbackManager");
    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
    EmailNotification emailNotification = new EmailNotification();

     /**
     * 
     * @param feedback
     * @param userToken
     * @return 
     * @purpose used to send feed back    
     */
    public Object sendUserFeedback(Feedback feedback, String userToken) {
        log4jLog.info("Inside FeedbackManager sendUserFeedback method");
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                int userId = fieldSenseUtils.userIdForToken(userToken);
                String userName = fieldSenseUtils.getUserFirstName(userId) + " " + fieldSenseUtils.getUserlastName(userId);
                feedback.setUserName(userName);
                feedback.setAccountName(fieldSenseUtils.getAccountName(accountId));
                feedback.setEmailId(fieldSenseUtils.userEmailIdForToken(userToken));
                if (emailNotification.userFeedback(feedback, "support@qlc.in")) {
                    log4jLog.info("Feedback sent");
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Feedback sent successfully . ", "", "");
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Feedback not sent please try again .", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
}
