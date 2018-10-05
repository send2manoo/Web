/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.auth.dao;

import com.qlc.fieldsense.auth.model.AuthenticationUser;
import com.qlc.fieldsense.mobile.model.LeftSliderMenu;
import java.util.HashMap;

/**
 *
 * @author Ramesh
 * @date 30-01-2014
 * @purpose This Interface contains all the methods to perform authentication .
 */
public interface AuthenticationUserDao {

    /**
     *
     * @param user
     * @return
     */
    public String selectUserToken(AuthenticationUser user);

    /**
     *
     * @param token
     * @return
     */
    public AuthenticationUser selectUserAuthDetails(String token);

    /**
     *
     * @param token
     * @return
     */
    public boolean updateTokenTime(String token);

    /**
     *
     * @param token
     * @return
     */
    public boolean deleteToken(String token);

    /**
     *
     * @param user
     * @return
     */
    public boolean insertAuthDetails(AuthenticationUser user);

    /**
     *
     * @param emailAddres
     * @return
     */
    public String getUserPassword(String emailAddres);
    
    /**
     *
     * @param mobileno
     * @return
     */
    public String getUserPasswordMobileNo(String mobileno);

    /**
     *
     * @param gcmId
     * @param deviceOS
     * @param userId
     * @return
     */
    public boolean updateGcmIdOfUser(String gcmId,int deviceOS, int userId);

    /**
     *
     * @param emailAddress
     * @return
     */
    public AuthenticationUser selectOfficeLatLong(String emailAddress);
    
    /**
     *
     * @param mobileno
     * @return
     */
    public AuthenticationUser selectOfficeLatLongMobileNo(String mobileno);

    /**
     *
     * @param firstlogin
     * @param userId
     * @return
     */
    public boolean updateUserFirstloginAndLastLoggedOn(int firstlogin, int userId);

    /**
     *
     * @param emailAddress
     * @return
     */
    public int selectUserFirstLogin(String emailAddress);
    
    /**
     *
     * @param mobileno
     * @return
     */
    public int selectUserFirstLoginMobileNo(String mobileno);

    /**
     *
     * @param userId
     * @return
     */
    public boolean updateLastLoggedOn(int userId);
    
    /**
     *
     * @param userId
     * @return
     */
    public String getUsrPassword(int userId);
    
    public AuthenticationUser selectAuthUserAllData(int userId); // Added by Jyoti, 16-dec-2017
    
    public java.util.Map lastUpdatedOfTerritoriesWithCount(int userRole,int userId,int accountId); // Added by Jyoti, 18-dec-2017
    
    public java.util.List<java.util.HashMap> assignedTerritoryList(int userId, int accountId); // Added by Jyoti, 08-01-2018
    
    public java.util.List<java.util.HashMap> expenseCategoryList(int accountId); // Added by Jyoti, 08-01-2018
    
    public java.util.List<java.util.HashMap> purposeCategoryList(int accountId); // Added by Jyoti, 08-01-2018
    
    public java.util.List<java.util.HashMap> industryCategoryList(int accountId); // Added by Jyoti, 08-01-2018
    
    public HashMap getLeftSliderMenuForOffline(String fromDate, String toDate, int userId, int accountId);
    
    public java.util.List<java.util.HashMap<String,Object>> assignedTerritoryListAfterLastSync(int userId, int accountId, String lastSyncDate); // Added by Jyoti, 02-02-2018
    
    public java.util.List<java.util.HashMap<String,Object>> expenseCategoryListAfterLastSync(int accountId, String lastSyncDate); // Added by Jyoti, 02-02-2018
    
    public java.util.List<java.util.HashMap<String,Object>> purposeCategoryListAfterLastSync(int accountId, String lastSyncDate); // Added by Jyoti, 02-02-2018
    
    public java.util.List<java.util.HashMap<String,Object>> industryCategoryListAfterLastSync(int accountId, String lastSyncDate); // Added by Jyoti, 02-02-2018
    
     public boolean update_terms_condition_agreed(AuthenticationUser authenticationUser); //added by nikhil,21-06-18;

}
