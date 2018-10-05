/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.activityOutcome.model;

import com.qlc.fieldsense.activityOutcome.dao.ActivityOutcomeDao;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import java.util.List;

/**
 *
 * @author anuja
 */
public class ActivityOutcomeManager {

    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
    ActivityOutcomeDao activityOutcomeDao = (ActivityOutcomeDao) GetApplicationContext.ac.getBean("ActivityOutcomeDaoImpl");

    /**
     * 
     * @param userToken
     * @return list of all activity outcomes
     * @purpose used to get  list of all activity outcomes
     */
    public Object getAllActivityOutcomes(String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List activityOutcomeList = activityOutcomeDao.getAllActivityOutcomes(accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "List of Activity Outcomes.", "activityOutcomeList", activityOutcomeList);
            // }else {
              //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }
}
