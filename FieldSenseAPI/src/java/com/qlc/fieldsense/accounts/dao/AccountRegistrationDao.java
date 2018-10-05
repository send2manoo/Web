/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.accounts.dao;

import com.qlc.fieldsense.accounts.model.Account;
import com.qlc.fieldsense.accounts.model.AccountLinkActivation;
import com.qlc.fieldsense.team.model.TeamMember;
import com.qlc.fieldsense.user.model.User;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ramesh
 */
public interface AccountRegistrationDao {

    /**
     *
     * @param account
     * @return
     */
    public int insertAccount(Account account);

    /**
     *
     * @return
     */
    public Account selectAccount();

    /**
     *
     * @return
     */
    public List<Account> selectAllAccounts();

    /**
     *
     * @param accountId
     * @return
     */
    public Account selectAccount(int accountId);

    /**
     *
     * @param accountId
     * @return
     */
    public boolean deleteAccount(int accountId);
    
    /**
     *
     * @param account
     * @return
     */
    public boolean editAccountDetails(Account account);

    /**
     *
     * @return
     */
    public List<String> getCreateAccountDeafultQueries();

    /**
     *
     * @param queryList
     * @param userId
     * @param accountId
     * @return
     */
    public boolean executeAccountUserDeafultQueries(List<String> queryList, int userId, int accountId);
    
    /**
     *
     * @param account
     * @return
     */
    public boolean updateAccountStats(Account account);
    
    /**
     *
     * @param user
     * @return
     */
    public boolean updateSuperAdminUserDetails(User user);
     
    /**
     *
     * @param user
     * @return
     */
    public boolean updateUserDetails(User user);
    
    /**
     *
     * @param account
     * @return
     */
    public boolean insertInAccountsPunchinStats(Account account);
    
    /**
     *
     * @return
     */
    public List<java.util.HashMap> getAllAccountRegions();

    public boolean updateAllAdmin(Account account);

    public boolean updateAllAdmin(String full_name ,String first_name,String Last_name, int gender, String mobile_no, String designation, String userid, String email_add ,int id ,String adminPassword ,int reportTo,User user);
    
    public int insertNewAdmin(String full_name ,String first_name,String Last_name, int gender, String mobile_no, String designation, String userid, String email_add ,int id ,String adminPassword ,int reportTo,User user);
    
    public int getReportToID(int accountId);
    
    public int getMaxIdFromTeams(int accountId);

    public String getAdminPassword(int adminID);
    
    public boolean deleteUser(int userId);
    
    public int getAccountId(int userId);
    
    public Map<String,Object> getTeamDataForUser(int id,int accountId);
    
    public String getUserTokenFromId(int id);
    
    public boolean getCountOfAdmin(int accountId);
    
    public boolean insertLinkActivationData(String encryptedKey, String emailAddress, String otp,String mobile_no); // Added by Jyoti, 17-Oct-2017
    
    public AccountLinkActivation selectLinkActivationData(String encryptedKey); // Added by Jyoti, 17-Oct-2017
    
    public String validateOTP(String OTP,String mobile_no,String email_address);
    
    public boolean updateOTP(String mobile_no,String email_address,String OTP) ;
    
    public boolean resendOTP_re_Registration(String encryptedKey,String mobile_no,String email_address,String OTP) ;
    
    public AccountLinkActivation validateOTP1(String OTP,String mobile_no,String email_address);
    
    public boolean isMobileNumberEmailExist(String mobile_no,String emailAddress);
    
    public boolean insertAuthToken(String encryptedKey, int accountId); // added by jyoti, remove later
    
    public List<AccountLinkActivation> selectListOfEmailIdsDetails(); // added by jyoti, remove later
    
}