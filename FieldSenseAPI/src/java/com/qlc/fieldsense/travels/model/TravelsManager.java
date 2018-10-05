/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.travels.model;

import com.qlc.fieldsense.travels.dao.TravelsDao;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author anuja
 * @ structure changed and not in use anymore
 */
public class TravelsManager {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("TravelsManager");
    TravelsDao travelsDao = (TravelsDao) GetApplicationContext.ac.getBean("travelsDaoImpl");
    FieldSenseUtils util = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");

    /**
     *
     * @param travel
     * @param token
     * @return
     */
    public Object createTravels(Travels travel, String token) {
        if (util.isTokenValid(token)) {
            //if(util.isSessionExpired(token)){
                int accountId = util.accountIdForToken(token);
                int travelId = travelsDao.insertTravels(travel, accountId);
                if (travelId != 0) {
                    travel = travelsDao.selectTravels(travelId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.TRAVELS_CREATED, " travel ", travel);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.TRAVELS_NOT_CREATED, "", "");
                }
             // }else {
                    //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
               //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /**
     *
     * @param token
     * @return
     */
    public Object selectTravels(String token) {
        if (util.isTokenValid(token)) {
             //if(util.isSessionExpired(token)){
                int accountId = util.accountIdForToken(token);
                Integer userId = util.userIdForToken(token);
                List<Travels> travelsList = travelsDao.selectTravelsList(userId, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "travelsList", travelsList);
            // }else {
                    //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
               //} 
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /**
     *
     * @param userId
     * @param token
     * @return
     */
    public Object selectTravels(int userId, String token) {
        if (util.isTokenValid(token)) {
            //if(util.isSessionExpired(token)){
                int accountId = util.accountIdForToken(token);
                List<Travels> travelsList = travelsDao.selectTravelsList(userId, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "travelsList", travelsList);
            // }else {
                    //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
               //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }
}
