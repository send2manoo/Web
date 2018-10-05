/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.mobile.dao;

import com.qlc.fieldsense.mobile.model.LeftSliderMenu;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Ramesh
 * @date 06-06-2014
 * @purpose This interface consists of mobile specific methods 
 */
public interface MobileDao {

    /**
     *
     * @param fromDate
     * @param toDate
     * @param userId
     * @param accountId
     * @return
     */
    public LeftSliderMenu getLeftSliderMenuData(String fromDate, String toDate, int userId, int accountId);
    
    /**
     *
     * @param fromDate
     * @param toDate
     * @param versionCode
     * @param userId
     * @param accountId
     * @return
     */
    public LeftSliderMenu getLeftSliderMenuData(String fromDate, String toDate,int versionCode, int userId, int accountId);  
    
    //Siddhesh:- requriment changed.
    //public int getSubordinates(int userId,int accountId);

    /**
     *
     * @param appType
     * @return
     */
    public HashMap selectAppVersionDetails(int appType);
 
    /**
     *
     * @param appType
     * @return
     */
    public String selectAppStoreURL(int appType);
    
    /**
     *
     * @param userRole
     * @param userId
     * @param accountId
     * @return
     */
    public Timestamp lastUpdatedOfTerritories(int userRole,int userId,int accountId);
    
    public Map lastUpdatedOfTerritoriesWithCount(int userRole,int userId,int accountId);
    
    /**
     * @param currencySymbol
     * @param userID
     * @return 
     * @Added by Jyoti
     */
    public boolean updateUserPreferences(String currencySymbol, int userID);
    
    public LeftSliderMenu getLeftSliderMenuV2(String fromDate, String toDate, int userId, int accountId);  // Added by Jyoti, 17-dec-2017
}
