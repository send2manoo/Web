/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.activity.model;

import com.qlc.fieldsense.activity.dao.ActivityDao;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import java.util.List;

/**
 *
 * @author anuja
 */
public class ActivityManager {

    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
    ActivityDao activityDao = (ActivityDao) GetApplicationContext.ac.getBean("activityDaoImpl");

    /**
     *
     * @param teamId
     * @param userToken
     * @return
     */
    public Object getActivity(int teamId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int userId = fieldSenseUtils.userIdForToken(userToken);
    //            if (fieldSenseUtils.isUserTeamLead(userId)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (teamId != 0 && fieldSenseUtils.isTeamValid(teamId, accountId)) {
                    if (fieldSenseUtils.isMemberInTeam(teamId, userId, accountId)) {
                        List<Activity> activityList = activityDao.selectActivity(teamId, accountId);
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "", activityList);
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid userId . Please try again . ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid teamId . Please try again . ", "", "");
                }
            // }else {
              //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     *
     * @param teamId
     * @param userId
     * @param userToken
     * @return
     */
    public Object getUserActivity(int teamId, int userId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int userId1 = fieldSenseUtils.userIdForToken(userToken);
    //            if (fieldSenseUtils.isUserTeamLead(userId1)) {
                    int accountId = fieldSenseUtils.accountIdForToken(userToken);
                    if (teamId != 0 && fieldSenseUtils.isTeamValid(teamId, accountId)) {
                        if (userId != 0 && fieldSenseUtils.isUserValid(userId)) {
                            if (fieldSenseUtils.isMemberInTeam(teamId, userId, accountId)) {
                                List<Activity> activityList = activityDao.selectActivityForUser(teamId, userId, accountId);
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "", activityList);
                            } else {
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid userId . Please try again . ", "", "");
                            }
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid userId . Please try again . ", "", "");
                        }
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid teamId . Please try again . ", "", "");
                    }
    //            } else {
    //                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " You are not authorised.", "", "");
    //            }
            // }else {
              //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}         
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     *
     * @param activity
     * @param userToken
     * @return
     */
    public Object createActivity(Activity activity, String userToken) {
        activity.setActivityDateTime(fieldSenseUtils.converDateToTimestamp(activity.getActivityDTime()));
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                int activityId = activityDao.creatActivity(activity, accountId);
                if (activityId != 0) {
                    activity = activityDao.selectOneActivity(activityId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Activity created successfully.", "activity", activity);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Activity is not created. Please try again. ", "", "");
                }
            // }else {
              //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }

    /**
     *
     * @param customerId
     * @param userToken
     * @return
     */
    public Object getCustomerActivity(int customerId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (customerId != 0 && fieldSenseUtils.isCustomerValid(customerId, accountId)) {
                    List<Activity> activityList = activityDao.selectActivityForCustomer(customerId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "", activityList);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid userId . Please try again . ", "", "");
                }
            // }else {
              //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
}
