/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.user.dao;

import com.qlc.fieldsense.password.model.Password;
import com.qlc.fieldsense.team.model.TeamMember;
import com.qlc.fieldsense.user.model.SuperUser;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.user.model.UserOld;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Ramesh
 * @date 18-02-2014
 */
public interface UserDao {

    /**
     *
     * @param accountId
     * @return
     */
    public List<User> slectAllUsers(int accountId);
        
    /**
     *
     * @param allRequestParams
     * @param accountId
     * @return
     */
    public List<User> slectAllUsersWithOffset(@RequestParam Map<String,String> allRequestParams, int accountId);


    /**
     *
     * @param userId
     * @return
     */

    public List<User> getSuperUsers(@RequestParam Map<String,String> allRequestParams, int accountId);

    public User selectUser(int userId);
    

    /**
     *
     * @param accountId
     * @return
     */

    public String selectlocationInterval(int userId);
    
    public User selectTopUser(int accountId);

     public List<User> selectAllAdmins(int accountId);

    /**
     *
     * @param user
     * @return
     */
    public int insertUser(User user);


    /**
     *
     * @param userId
     * @return
     */

    public int insertSuperUser(SuperUser user); //Added by manohar

    public User deleteUser(int userId);
    
    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public int getParentTeamId(int userId, int accountId);

    /**
     *
     * @param user
     * @return
     */
    public boolean updateUserLastKnownDetails(User user);
    
    /**
     *
     * @param user
     * @return
     * @deprecated
     */
    @Deprecated
    public boolean updateUserLastKnownDetails(UserOld user);

    /**
     *
     * @param password
     * @return
     */
    public boolean resetPassword(Password password);

    /**
     *
     * @param user
     * @return
     */
    public boolean updateUserDetails(User user);

    /**
     *
     * @param accountId
     * @return
     */
    public List<User> selectUsersExceptTeamMembers(int accountId);

    /**
     *
     * @param emailAddres
     * @return
     */
    public String getUserPassword(String emailAddres);

    /**
     *
     * @param user
     * @return
     */
    public boolean updateUserDetailsForUser(User user);

    /**
     *
     * @param user
     * @return
     */
    public boolean updateOfficeLatlong(User user);

    /**
     *
     * @param user
     * @return
     */
    public boolean updateHomeLatlong(User user);

    /**
     *
     * @return
     */
    public List<String> getCreateUserDeafultQueries();

    /**
     *
     * @param queryList
     * @param userId
     * @param accountId
     * @return
     */
    public boolean executeCreateUserDeafultQueries(List<String> queryList, int userId, int accountId);

    /**
     *
     * @param accountId
     * @return
     */
    public List<User> selectTeamLeaders(int accountId);
    
    /**
     *
     * @param accountId
     * @return
     */
    public List<TeamMember> slectAllUsersForMobile(int accountId);
    
    /**
     *
     * @param mobile
     * @return
     */
    public List getUsersFromMobileNo(String mobile);
            
    /**
     *
     * @param mobile
     * @param otp
     * @return
     */
    public int insertOTPForUser(String mobile, String otp);
    
    /**
     *
     * @param mobile
     * @return
     */
    public java.util.List getOTPAndExpiryForUser(String mobile);
    
    /**
     *
     * @param password
     * @param mobile
     * @return
     */
    public boolean resetPasswordUsingPhone(String password,String mobile);

    //public Timestamp getLastLocationUpdateTime(int id);
    
    /**
     *
     * @param accountId
     * @param topId
     * @return
     */
        
    public List<java.util.HashMap> slectAllUsersForReportTo(int accountId,int topId);
    
    /**
     *
     * @param accountId
     * @return
     */
    public int getTopUserInHierarachy( int accountId);
    
    /**
     *
     * @param addTerritoryList
     * @param deleteTerritoryList
     * @param userId
     * @param accountId
     */
    public void insertUserTerritories(List<Integer> addTerritoryList,List<Integer> deleteTerritoryList,int userId,int accountId);
    
    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public int insertUserTerritoryUnknownForCSVFile(int userId,int accountId);
    
    /**
     * added by Jyoti
     * @param userId
     * @return get id of users whose report_to = userId
     */
    public List<Integer> getListOfReporterOfUserID(int userId);

    public User deleteAdmin(int accountId);

    public User deleteAccount(int accountId);

    public boolean deleteAdminEditTemp(int userId);

    public boolean deleteUser_Account_punchin_stats(int accountId);
    
    public HashMap selectUserForOffline(int userId, int accountId);

    public boolean updateUserAppVersion(int userId, int versionCodeFromMobile); // Added by jyoti
    
    public List<HashMap> getUsers(int accountId,int userId,String fromDate,String toDate,int start , int end); //Added by siddhesh for admin travel reports
    
    public List<HashMap> getUsersForSubordinate(int accountId,ArrayList subordateList,int userId,String fromDate,String toDate , int start , int end); // Added by siddhesh for user travel reports
}
