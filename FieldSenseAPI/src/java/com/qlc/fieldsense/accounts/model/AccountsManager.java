/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.accounts.model;

import com.qlc.fieldsense.accounts.dao.AccountRegistrationDao;
import com.qlc.fieldsense.accounts.dao.AccountSettingDao;
import com.qlc.fieldsense.auth.dao.AuthenticationUserDao;
import com.qlc.fieldsense.createAccount.model.CreateAccountDB;
import com.qlc.fieldsense.team.dao.TeamDao;
import com.qlc.fieldsense.team.model.Team;
import com.qlc.fieldsense.team.model.TeamManager;
import com.qlc.fieldsense.team.model.TeamMember;
import com.qlc.fieldsense.user.dao.UserDao;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.user.model.UserManager;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.EmailNotification;
import com.qlc.fieldsense.utils.EncryptionDecryption;
import com.qlc.fieldsense.utils.FieldSensePasswordEncryptionDecryption;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import com.qlc.fieldsense.utils.MessageNotifications;
import com.qlc.fieldsense.utils.RunnableThreadJob;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Ramesh
 * @date 20-02-2014
 */
public class AccountsManager {
//    }

    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
    AccountRegistrationDao accountRegistrationDao = (AccountRegistrationDao) GetApplicationContext.ac.getBean("accountRegistrationDaoImpl");

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("AccountsManager");
    CreateAccountDB createAccountDB = new CreateAccountDB();
    UserDao userDao = (UserDao) GetApplicationContext.ac.getBean("userDaoImpl");
    TeamDao teamDao = (TeamDao) GetApplicationContext.ac.getBean("teamDaoImpl");
    AuthenticationUserDao authenticationUserDao = (AuthenticationUserDao) GetApplicationContext.ac.getBean("authenticationDaoImpl");
    AccountSettingDao accountSettingDao = (AccountSettingDao) GetApplicationContext.ac.getBean("accountSettingDaoImpl");

    private final Map<String, String> myMap2 = new HashMap() {
        {
            put("Andaman and Nicobar Islands", "East");
            put("Andhra Pradesh", "South");
            put("Arunachal Pradesh", "East");
            put("Assam", "East");
            put("Bihar", "North");
            put("Chandigarh", "North");
            put("Chhattisgarh", "East");
            put("Dadra and Nagar Haveli", "West");
            put("Daman and Diu", "West");
            put("Delhi", "North");
            put("Goa", "West");
            put("Gujarat", "South");
            put("Haryana", "North");
            put("Himachal Pradesh", "North");
            put("Jammu and Kashmir", "North");
            put("Jharkhand", "East");
            put("Karnataka", "South");
            put("Kerala", "South");
            put("Lakshadweep", "South");
            put("Madhya Pradesh", "North");
            put("Maharashtra", "Mumbai");
            put("Manipur", "East");
            put("Meghalaya", "East");
            put("Mizoram", "East");
            put("Nagaland", "East");
            put("Orissa", "East");
            put("Pondicherry", "South");
            put("Punjab", "North");
            put("Rajasthan", "North");
            put("Sikkim", "North");
            put("Tamil Nadu", "South");
            put("Tripura", "South");
            put("Uttar Pradesh", "North");
            put("Uttarakhand", "North");
            put("West Bengal", "East");

        }
    };
    private final Map<String, Integer> regionId = new HashMap() {
        {
            put("Mumbai", 1);
            put("North", 2);
            put("South", 3);
            put("West", 4);
            put("Gujarat", 5);
            put("East", 6);
            put("International", 7);
//            put("South",3);

        }
    };
    private final Map<Integer, String> regionIdSuper = new HashMap() {
        {
            put(1, "Mumbai");
            put(2, "North");
            put(3, "South");
            put(4, "West");
            put(4, "Gujarat");
            put(6, "East");
            put(7, "International");
//            put("South",3);

        }
    };

    /**
     *
     * @param account
     * @param userToken
     * @return
     * @purpose used to create client account
     */
    public Object createAccount(String userToken, Account account) {
        Map<String, Integer> regionMap = new AccountsManager().regionId;
        Map<Integer, String> regionMapSuperAdm = new AccountsManager().regionIdSuper;
        String region_state = "";
        if (account.isOnlineCreation() == true) {
        String state_key = account.getState();
//        System.out.println("state_key :-"+state_key);
//        System.out.println("Account object " + account.toString());
        Map<String, String> statesMap = new AccountsManager().myMap2;
        region_state = statesMap.get(state_key);
        if(region_state == null)
        {
            account.setRegionId(7);
            region_state="Mumbai";
//            System.out.println("Internation region :-"+region_state);
            
        }else
        {    
//        System.out.println("region_state " + region_state);
       
        int regionId = regionMap.get(region_state);
//        System.out.println("regionId##$%$% "+regionId);
        account.setRegionId(regionId);
        }
        }else
        {
            int regionID = account.getRegionId();
            region_state = regionMapSuperAdm.get(regionID);
//            System.out.println("region_state " + region_state);

        }

//        Iterator<String> it = statesMap.keySet().iterator();
//       while (it.hasNext()) {
//            String key = it.next();
//            System.out.println(key + "%%%% " + statesMap.get(key));
//        }
//        System.out.println("Hashmap_regions"+Constant.myMap);
//        System.out.println("OnlineCreation $ " + account.isOnlineCreation());
        log4jLog.info("AccountsManager inside createAccount");
        if (fieldSenseUtils.isTokenValid(userToken) || account.isOnlineCreation()) {
            if (fieldSenseUtils.isCompanyNameExist(account.getCompanyName().trim()) < 1) {
                // if (FieldSenseUtils.isValidEmailAddress(account.getEmailAddress())) {
                // if (fieldSenseUtils.isEmailExist(account.getEmailAddress()) == 0) {
//                            if(!account.getUser().getMobileNo().trim().equals("")){
//                                if (fieldSenseUtils.isMobileExist(account.getUser().getMobileNo()) != 0) {
//                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "User already exists with the same mobile number. Please try with different mobile number .", "", "");
//                                }
//                            }
                account.setStartDate(fieldSenseUtils.converDateToTimestamp(account.getStrStartDate()));
                account.setExpiredOn(fieldSenseUtils.converDateToTimestamp(account.getsExpiredOn()));
//                              System.out.println("above account insert");
                // System.out.println("##^^&&&%"+account.getCompanyName()+"1 "+ account.getAddress1()+"2 "+ account.getAddress2()+"3 "+ account.getAddress3()+"4 "+ account.getCity()+"5"+ account.getState()+"6 "+ account.getCountry()+" 7"+ account.getZipCode()+" 8"+ account.getCompanyPhoneNumber1()+"9 "+ account.getCompanyPhoneNumber2()+"10 "+ account.getCompanyWebsite()+"11 "+account.getExpiredOn()+"12 "+account.getUserLimit()+"13 "+account.getRegionId()+"14 "+account.getStartDate()+"15 "+account.getIndustry()+"16 "+account.getPlan()+"17");
                int accountId = 0;
                Long com_Phone_number = account.getCompanyPhoneNumber1();
                try {
                    String com_Phone_number_string = Long.toString(com_Phone_number).trim();
//                                  System.out.println("com_Phone_number_string## "+com_Phone_number_string);

                    if (fieldSenseUtils.isPhoneNumber(com_Phone_number_string) != 0) {
//                                      System.out.println("inside phone match");
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Company already exists with the '" + com_Phone_number_string + "' phone number. Please try with different phone number .", "", "");
                    };
                    accountId = accountRegistrationDao.insertAccount(account);
                } catch (Exception e) {
//                    System.out.println("insert account method " + e);
                    e.printStackTrace();
                }
//                System.out.println("accountId+++ " + accountId);
                if (accountId != 0) {
                    log4jLog.info("Inside createAccount account inserting into DB");
//                                System.out.println("boolean doAccountActivation"+createAccountDB.doAccountActivation(accountId));
                    if (createAccountDB.doAccountActivation(accountId)) {
//                                    try{
//                        System.out.println("Inside createAccount ########");
                        int loginUserId = fieldSenseUtils.userIdForToken(userToken);
                        User user = new User();
                        user.setAccountId(accountId);
                        String companyName = account.getCompanyName();
                        int userId = 0;
                        user.setFirstName(companyName);
                        user.setEmailAddress(companyName + "@fieldsense.in");
                        user.setPassword(FieldSensePasswordEncryptionDecryption.hashPassword("qlc@123"));
                        user.setCreatedBy(loginUserId);
                        user.setActive(true);

                                    //Added by Mayank Ramaiya
                        // user.setuserAccuracy(500);
                        //user.setCheckInRadius(500);
                        //End by Mayank Ramaiya
                        //user.setRole(0);
                        // System.out.println("user$$%%^^ "+user.toString());
                        userId = userDao.insertUser(user);
//                        System.out.println("userId$$$$ " + userId);

                        UserManager userManager = new UserManager();
                        // Added by nikhil bhosale 11th july 2017 :- Adminuser pojo & for loop for inserting multiple admins. 
                        Adminuser adminuser = new Adminuser();
                        User createdUser = new User();
                        boolean active = false;
                        // System.out.println("F_n"+account.getAdminuser().get(0).getFirstName());
                        // System.out.println("account.getAdminuser().size()^^^^"+account.getAdminuser().size());
                        int sizeForLoop = account.getAdminuser().size();
//                        System.out.println("Adminuser()$$% " + account.getAdminuser() + " size: " + sizeForLoop);
                        String AdminEmailAdd;
                        for (int i = 0; i < sizeForLoop; i++) {
                            account.setFirstUserName(account.getAdminuser().get(0).getFirstName());
                            account.setUserEmailId(account.getAdminuser().get(0).getEmailAddress().trim());
                            account.setUserMobileNumber(account.getAdminuser().get(0).getMobileNo().trim());
                            boolean isEmailExist = false;
                            boolean isMobile_noExist = false;
//                                        System.out.println("Start forloop "+account.getAdminuser().size());
//                                        System.out.println("In forloop "+i);
                            createdUser.setFirstName(account.getAdminuser().get(i).getFirstName());
                            createdUser.setDesignation(account.getAdminuser().get(i).getDesignation());
                            AdminEmailAdd = account.getAdminuser().get(i).getEmailAddress().trim();
                            createdUser.setEmailAddress(AdminEmailAdd);
//                            System.out.println("email address of admin " + account.getAdminuser().get(i).getEmailAddress());
//                                    System.out.println("fieldSenseUtils.isEmailExist(account.getAdminuser().get(i).getEmailAddress())@@@ "+fieldSenseUtils.isEmailExist(account.getAdminuser().get(i).getEmailAddress()));
                            if (fieldSenseUtils.isEmailExist(AdminEmailAdd) != 0) {

//                                        System.out.println("Inside email exist 1 ");
//                                        userDao.deleteAdmin(accountId);
//                                        createAccountDB.dropAccountDatabase(accountId);
//                                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "User already exists with '"+account.getAdminuser().get(i).getEmailAddress().trim()+"' emailaddress .Please try with diffrent emailaddress .", "", "");
                                isEmailExist = true;
                            }
//                            System.out.println("MobileNo @# " + account.getAdminuser().get(i).getMobileNo().trim());
                            if (!account.getAdminuser().get(i).getMobileNo().trim().equals("")) {
                                if (fieldSenseUtils.isMobileExist(account.getAdminuser().get(i).getMobileNo().trim()) != 0) {
//                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "User already exists with the same mobile number. Please try with different mobile number .", "", "");
//                                    System.out.println("Inside mobile exist 1 ");
                                    isMobile_noExist = true;
                                }
                            }
                            if (isEmailExist || isMobile_noExist) {
//                                        System.out.println("account.getAdminuser().get(i).getMobileNo().trim()&& "+account.getAdminuser().get(i).getMobileNo().trim());
//                                        System.out.println("accountId$$ "+accountId);
                                userDao.deleteAdmin(accountId);
                                boolean deleteUser_Account_punchin_stats = userDao.deleteUser_Account_punchin_stats(accountId);
//                                System.out.println("deleteUser_Account_punchin_stats## " + deleteUser_Account_punchin_stats);
                                createAccountDB.dropAccountDatabase(accountId);
                                if (isEmailExist) {
                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "User already exists with '" + account.getAdminuser().get(i).getEmailAddress().trim() + "' emailaddress .Please try with diffrent emailaddress .", "", "");
                                }
                                if (isMobile_noExist) {
                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "User already exists with the '" + account.getAdminuser().get(i).getMobileNo().trim() + "' mobile number. Please try with different mobile number .", "", "");
                                }
                            }
                            createdUser.setMobileNo(account.getAdminuser().get(i).getMobileNo());
                            createdUser.setPassword(account.getAdminuser().get(i).getPassword());
                            createdUser.setGender(Integer.parseInt(account.getAdminuser().get(i).getGender()));
                            createdUser.setRole(adminuser.getRole());
                            createdUser.setReport_to(userId);
                            if (adminuser.getActive() == 1) {
                                active = true;
                                createdUser.setActive(active);
                            } else {
                                createdUser.setActive(active);
                            }

                            createdUser.setAccountId(accountId);
                            createdUser.setAccountContactType(adminuser.getAccountContactType());
//                            System.out.println("createdUser%%%" + createdUser.toString());
                            account.setUser(createdUser);
//                            System.out.println("account.isOnlineCreation() "+account.isOnlineCreation()); 
                            boolean OnlineCreation = account.isOnlineCreation();
                            boolean NewAccountNewUser = userManager.createNewUserForNewAccount(account.getUser(), userToken,OnlineCreation);
//                            System.out.println("NewAccountNewUser " + i + NewAccountNewUser);

                            if (NewAccountNewUser) {
//                                System.out.println("NewAccountNewUser in if : " + NewAccountNewUser);
                                Team team = new Team();
                                team.setTeamName("Hirarchy");
                                team.setDescription("Hirarchy");
                                User teamOwner = new User();
                                teamOwner.setId(userId);
                                team.setOwnerId(teamOwner);
                                team.setIsActive(1);
                                User teamCreatedBy = new User();
                                teamCreatedBy.setId(loginUserId);
                                team.setCreatedBy(teamCreatedBy);
                                int teamId = 0;
                                //added by nikhil :- if condiion
                                if (i == 0) {
                                    teamId = teamDao.insertDefaultTeam(team, "100000", accountId, fieldSenseUtils);
//                                    System.out.println("teamid : " + teamId);
                                }
                                int createdUserId = fieldSenseUtils.getUserId(account.getUser().getEmailAddress());
                                Team team1 = new Team();
                                team1.setTeamName("Hirarchy");
                                team1.setDescription("Hirarchy");
                                User teamOwner1 = new User();
                                teamOwner1.setId(createdUserId);
                                team1.setOwnerId(teamOwner1);
                                team1.setIsActive(1);
                                User teamCreatedBy1 = new User();
                                teamCreatedBy1.setId(loginUserId);
                                team1.setCreatedBy(teamCreatedBy1);
                                // added by nikhil
                                String teamid = "10000" + (i + 1);
                                // int teamId1 = teamDao.insertDefaultTeam(team1, "100001,100000", accountId,fieldSenseUtils);
//                                        System.out.println("Teamids nik "+teamid);
                                String teamPosition = teamid + ",100000";
                                int teamId1 = teamDao.insertDefaultTeam(team1, teamPosition, accountId, fieldSenseUtils);
                                // ended by nikhil
//                                System.out.println("teamId1 nik " + teamId1);
                                TeamMember teamMember = new TeamMember();
                                teamMember.setTeamId(teamId);
                                teamMember.setUser(teamOwner);
                                teamMember.setMemberType(1);
                                teamMember.setCreatedBy(teamCreatedBy);
                                teamMember.setStatus(1);

                                TeamMember teamMember1 = new TeamMember();
                                teamMember1.setTeamId(teamId1);
                                teamMember1.setUser(teamOwner1);
                                teamMember1.setMemberType(2);
                                teamMember1.setCreatedBy(teamCreatedBy);
                                teamMember1.setStatus(1);

                                TeamMember teamMember2 = new TeamMember();
                                teamMember2.setTeamId(teamId);
                                teamMember2.setUser(teamOwner1);
                                teamMember2.setMemberType(3);
                                teamMember2.setCreatedBy(teamCreatedBy);
                                teamMember2.setStatus(1);

                                ArrayList<TeamMember> teamMemberList = new ArrayList<TeamMember>();
                                teamMemberList.add(teamMember);
                                teamMemberList.add(teamMember1);
                                teamMemberList.add(teamMember2);

                                teamDao.insertDefaultTeamMember(teamMemberList, userToken, accountId, fieldSenseUtils);
                                //teamDao.insertTeamMember(teamMember, accountId);
                                // System.out.println("account.toString()$%^&1 "+account.toString());
                                Account account1 = new Account();
                                account1 = accountRegistrationDao.selectAccount(accountId);
                                //System.out.println("account.toString()$%^& 2"+account.toString());
                                try {
                                    account.setId(accountId);
//                                            accountRegistrationDao.insertInAccountsPunchinStats(account); // in punchstats db
                                    boolean AccountsPunch = accountRegistrationDao.insertInAccountsPunchinStats(account1);
//                                    System.out.println("AccountsPunch$$$ " + AccountsPunch + i);
                                } catch (Exception e) {
//                                    System.out.println("line 337 ");
                                    e.printStackTrace();
                                    log4jLog.log(Level.INFO, "createAccount {0}", e);
                                }

                                account.setWebsitePath(Constant.WEBSITE_PATH);
                            } else {
                                createAccountDB.dropAccountDatabase(accountId);
                                boolean deleteUser_Account_punchin_stats = userDao.deleteUser_Account_punchin_stats(accountId);
//                                System.out.println("deleteUser_Account_punchin_stats1## " + deleteUser_Account_punchin_stats);
                                userDao.deleteUser(userId);
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Account creation Failed .", "", "");
                            }
                            // return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Account created successfully .", " account ", account);
                            //  System.out.println("Stop forloop "+account.getAdminuser().size());                                   
                        }
                        account.setId(accountId);
//                        System.out.println("account.getId()@#$" + account.getId());
//                                     try{
//                                    if(account.isOnlineCreation() == true)
//                                    {
//                                     final EmailNotification email_customer_service = new EmailNotification();
//                                     System.out.println("inside 360");
//                                     //email_customer_service.customerService();
//                                     Thread t1 = new Thread(new Runnable(){
//                                     public void run(){
//                                     email_customer_service.customerService(region_state,account);
//                                     }
//                                     });
//                                       t1.run();
//                                    }
//                                     } catch(Exception e){
//                                         System.out.println("inside 372");
//                                         e.printStackTrace();
//                                     }
                        try {
                            if (account.isOnlineCreation() == true) {
                                final EmailNotification email_customer_service = new EmailNotification();
                                //email_customer_service.customerService();
                                email_customer_service.customerService(region_state, account);
//                                System.out.println("account.getSalesEmailId() " + account.getSalesEmailId());
                            }
                        } catch (Exception e) {
//                            System.out.println("inside 372");
                            e.printStackTrace();
                        }
//                        System.out.println("Account created successfully");
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS,"Account created successfully.", " account ", account);

                        // ended by nikhil
//                                    }catch(Exception e){
//                                      System.out.println("got exception "+e);
//                                        e.printStackTrace();
//                                        return null;
//                                      }
                        // return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Account created successfully.", " account ", account);
                    } else {
//                        System.out.println("440");
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Account creation Failed .", "", "");
                    }
                } else {
//                    System.out.println("444");
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Account creation Failed .", "", "");
                }
//                        } else {
//                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "User already exists.Please try with diffrent emailaddress .", "", "");
//                        }
//                    } else {
//                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_EMAIL, "", "");
//                    }
            } else {
//                System.out.println("454");
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Company Name already exists.", "", "");
            }
        } else {
//            System.out.println("457");
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
        //delete this return statement after uncommenting above if else // by nikhil bhosale 
        // return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Account created successfully .", " account ", account); 
        //return "dont kno" ;
    }

    /**
     * @purpose used to get list of all client accounts
     * @return list of all client accounts
     */
    public Object selctAccounts() {
        List<Account> accountList = new ArrayList<Account>();
        accountList = accountRegistrationDao.selectAllAccounts();
        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " account list ", accountList);
    }

    /**
     * @param userToken
     * @param accountId
     * @purpose used to get details of particular account based on account id
     * @return details of particular account
     */
    public Object selectAccount(String userToken, int accountId) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            if (fieldSenseUtils.isAccountValid(accountId)) {
                Account account = accountRegistrationDao.selectAccount(accountId);
                //int userId=fieldSenseUtils.getOrganisationHeadId(accountId);
                // User user=userDao.selectTopUser(accountId);
                //  System.out.println("user object+++ "+user.toString());
                //added by nikhil 11th july 2017 :- to get multiple admins in list object
                List<User> adminList = userDao.selectAllAdmins(accountId);                                // account.setUser(user);
                account.setAllAdminUser(adminList);
                //ended by nikhil
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " account ", account);
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid accountId .", "", "");
            }
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * @param userToken
     * @param accountId
     * @return
     * @purpose used to delete particular account based on account id.
     */
    public Object deleteAccount(String userToken, int accountId) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            if (fieldSenseUtils.isUserSuperAdmin(fieldSenseUtils.userIdForToken(userToken))) {
                if (fieldSenseUtils.isAccountValid(accountId)) {
                    if (accountRegistrationDao.deleteAccount(accountId)) {
                        createAccountDB.dropAccountDatabase(accountId);
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Account deleted successfully .", "", "");
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Account deletion failed , please try again .", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid accountId .", "", "");
                }
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "You doesn't have authority to perform this action .", "", "");
            }
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * @param id
     * @param userToken
     * @param account
     * @return
     * @purpose used to edit particular account based on account id.
     */
    public Object editAccDetails(String userToken, int id, Account account) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            if (fieldSenseUtils.isAccountValid(id)) {
                if (fieldSenseUtils.isCompanyNameExistForEdit(account.getCompanyName().trim(), id) < 1) {
                    account.setExpiredOn(fieldSenseUtils.converDateToTimestamp(account.getsExpiredOn()));
                    account.setStartDate(fieldSenseUtils.converDateToTimestamp(account.getStrStartDate()));
                    // updated by nikhil - 11th july 2017 - to update multiple admins
                    int adminListSize = account.getAllAdminUser().size();
                    int accountId = 0;
                    int reportTo = 0;
                    int loginUserId = fieldSenseUtils.userIdForToken(userToken);
                    int j = 0;
                    ArrayList newAdminList = new ArrayList();
                    int userId;
                    String ExistEmail;
                    String ExistMobile_no;
                    Long com_Phone_number = account.getCompanyPhoneNumber1();
                    Long phone_no = fieldSenseUtils.getAccountPhoneNo(id);
                    if (!(com_Phone_number.toString()).equalsIgnoreCase(phone_no.toString())) {
                        if (fieldSenseUtils.isPhoneNumber(com_Phone_number.toString()) != 0) {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Company already exists with the '" + com_Phone_number.toString() + "' phone number. Please try with different phone number .", "", "");
                        }
                    }
                    for (int i = 0; i < adminListSize; i++) {
                        boolean isEmailExist = false;;
                        boolean isMobileExist = false;
                        String LastName = "";
                        String fullname = account.getAllAdminUser().get(i).getFirstName().trim();
                        String[] parts = fullname.split(" ");
                        String FirstName = parts[0];
                        if (!fullname.equals(FirstName)) {
                            LastName = parts[1];
                        }
                        int gender = account.getAllAdminUser().get(i).getGender();
                        String mobile_no = account.getAllAdminUser().get(i).getMobileNo().trim();
                        String designation = account.getAllAdminUser().get(i).getDesignation();
                        String email_add = account.getAllAdminUser().get(i).getEmailAddress().trim();
                        String userid = account.getAllAdminUser().get(i).getAdminUserId();
                        String adminPassword = account.getAllAdminUser().get(i).getPassword();
                        User user = new User();
                        if (userid != "") {
                            ExistEmail = fieldSenseUtils.getUserEmailAddress(Integer.parseInt(userid)).trim();
                            if (!ExistEmail.equalsIgnoreCase(email_add)) {
                                if (fieldSenseUtils.isEmailExist(email_add) != 0) {
                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "User with '" + email_add + "' emailAddress already exists.Please try with diffrent emailaddress .", "", "");
                                }
                            }
                            ExistMobile_no = fieldSenseUtils.getUserMobileNo(Integer.parseInt(userid)).trim();
                            if (!ExistMobile_no.equalsIgnoreCase(mobile_no)) {
                                if (fieldSenseUtils.isMobileExist(mobile_no) != 0) {
                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "User already exists with the '" + mobile_no + "' mobile number. Please try with different mobile number .", "", "");
                                }
                            }
                        }
                        if (userid == "") {
                            newAdminList.add(email_add);
                            if (fieldSenseUtils.isEmailExist(email_add) != 0) {
                                isEmailExist = true;
                            }
                            if (fieldSenseUtils.isMobileExist(mobile_no) != 0) {
                                isMobileExist = true;
                            }
                            if (isEmailExist || isMobileExist) {
                                for (int k = 0; k < newAdminList.size(); k++) {
                                    userId = fieldSenseUtils.getUserId(newAdminList.get(k).toString());
                                    Object li = deleteAdminUser(userId, userToken);
                                }
                                if (isEmailExist) {
                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "User with '" + email_add + "' emailAddress already exists.Please try with diffrent emailaddress .", "", "");
                                } else if (isMobileExist) {
                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "User already exists with the '" + mobile_no + "' mobile number. Please try with different mobile number .", "", "");
                                }
                            }
                            j++;
                            accountId = account.getId();
                            reportTo = accountRegistrationDao.getReportToID(accountId);
                            int newAccountID = accountRegistrationDao.insertNewAdmin(fullname, FirstName, LastName, gender, mobile_no, designation, userid, email_add, id, adminPassword, reportTo, user);
                            int maxIdFromTeams = accountRegistrationDao.getMaxIdFromTeams(accountId);
                            Team team = new Team();
                            team.setTeamName("Hirarchy");
                            team.setDescription("Hirarchy");
                            User teamOwner = new User();
                            teamOwner.setId(newAccountID);
                            team.setOwnerId(teamOwner);
                            team.setIsActive(1);
                            User teamCreatedBy = new User();
                            teamCreatedBy.setId(loginUserId);
                            team.setCreatedBy(teamCreatedBy);
                            int teamid = maxIdFromTeams + j;
                            String teamPosition = teamid + ",100000";
                            int teamId1 = teamDao.insertDefaultTeam(team, teamPosition, accountId, fieldSenseUtils);
                            TeamMember teamMember1 = new TeamMember();
                            teamMember1.setTeamId(teamId1);
                            teamMember1.setUser(teamOwner);
                            teamMember1.setMemberType(2);
                            teamMember1.setCreatedBy(teamCreatedBy);
                            teamMember1.setStatus(1);
                            TeamMember teamMember2 = new TeamMember();
                            teamMember2.setTeamId(teamId1);
                            teamMember2.setUser(teamOwner);
                            teamMember2.setMemberType(3);
                            teamMember2.setCreatedBy(teamCreatedBy);
                            teamMember2.setStatus(1);

                            ArrayList<TeamMember> teamMemberList = new ArrayList<TeamMember>();
                            teamMemberList.add(teamMember1);
                            teamMemberList.add(teamMember2);
                            int teamMember = teamDao.insertDefaultTeamMember(teamMemberList, userToken, accountId, fieldSenseUtils);
                        } else {
                            int adminID = Integer.parseInt(userid);
                            String adminExistingPassword = accountRegistrationDao.getAdminPassword(adminID);
                            if (!adminPassword.equalsIgnoreCase(adminExistingPassword)) {
                                adminPassword = FieldSensePasswordEncryptionDecryption.hashPassword(adminPassword);
                            }
                            accountRegistrationDao.updateAllAdmin(fullname, FirstName, LastName, gender, mobile_no, designation, userid, email_add, id, adminPassword, reportTo, user);
                        }
                    }
                    if (accountRegistrationDao.editAccountDetails(account) && accountRegistrationDao.updateAccountStats(account)) {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Account updated successfully .", "", "");
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Account update failed , please try again .", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Company Name already exists.", "", "");
                }
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid accountId .", "", "");
            }
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * @param userToken
     * @purpose used to get all AccountSettingValues
     * @return list of region
     */
    public Object selectAllAccountSettings(String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            int accountId = fieldSenseUtils.accountIdForToken(userToken);
            List<AccountSetting> settings = accountSettingDao.selectAllAccountSettings(accountId);
            boolean allowTimeoutForAll = fieldSenseUtils.isTimeoutAllowedForAllUsersInAccount(accountId);
            if (settings.size() > 0) {
                settings.get(0).setAllowTimeoutForAll(allowTimeoutForAll);
            }
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " account settings", settings);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * @param userToken
     * @purpose used to get AccountSettingValues Admin side
     * @return list of region
     */
    public Object selectAccountSettingValues(String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            int accountId = fieldSenseUtils.accountIdForToken(userToken);
            AccountSettingValues settingValues = new AccountSettingValues();
            List<AccountSetting> settings = accountSettingDao.selectAllAccountSettings(accountId);
            for (AccountSetting set : settings) {
                
                // Added by jyoti, #28115 Wants Auto Punch-Out, at specific configurable time.
                if (set.getSettingName().equals("auto_punch_out_time")) {
                    settingValues.setAuto_punch_out_time(set.getSettingValue());
                }
                if (set.getSettingName().equals("auto_punch_out_type")) {
                    settingValues.setAuto_punch_out_type(Integer.parseInt(set.getSettingValue()));
                }
                if (set.getSettingName().equals("working_hours")) {
                    settingValues.setWorking_hours(Integer.parseInt(set.getSettingValue()));
                }
                // ended by jyoti, #28115 Wants Auto Punch-Out, at specific configurable time.
                
                if (set.getSettingName().equals("allow_timeout") && set.getSettingValue().trim().equals("1")) {
                    settingValues.setAllowTimeout(true);
                }
                // Added by jyoti 21-12-2016
                if (set.getSettingName().equals("allow_offline") && set.getSettingValue().trim().equals("0")) {
                    settingValues.setAllowOffline(false);
                }
                // Added by nikhil 23-06-2917
                if (set.getSettingName().equals("Location_interval")) {
                    settingValues.setInterval(set.getSettingValue());
                }
                // Ended by nikhil 
                // Added by jyoti 22-08-2017
//Added by siddhesh for time zone
                if(set.getSettingName().equals("local_time_zone_code"))  {
                settingValues.setTimeZone(set.getSettingValue());
                }   
//end Siddhesh
//                    if(set.getSettingName().equals("currency_symbol")){
//                        settingValues.setCurrencySymbol(set.getSettingValue());
//                    }
            }

            boolean allowTimeoutForAll = fieldSenseUtils.isTimeoutAllowedForAllUsersInAccount(accountId);
            settingValues.setAllowTimeoutForAll(allowTimeoutForAll);
//                System.out.println("settingValues***+++"+settingValues.toString());
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " (Admin) Account Settings", settingValues);
        } else {

            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * @param id
     * @Added by jyoti 22-12-2016
     * @param userToken
     * @purpose used to get AccountSettingValues Super Admin side
     * @return list of region
     */
    public Object selectAccountSettingValuesForOffline(String userToken, int id) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            AccountSettingValues settingValues = new AccountSettingValues();
            List<AccountSetting> settings = accountSettingDao.selectAllAccountSettings(id);
            for (AccountSetting set : settings) {
                if (set.getSettingName().equals("allow_timeout") && set.getSettingValue().trim().equals("1")) {
                    settingValues.setAllowTimeout(true);
                }
                if (set.getSettingName().equals("allow_offline") && set.getSettingValue().trim().equals("0")) {
                    settingValues.setAllowOffline(false);
                }
            }
            boolean allowTimeoutForAll = fieldSenseUtils.isTimeoutAllowedForAllUsersInAccount(id);
            settingValues.setAllowTimeoutForAll(allowTimeoutForAll);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " (Super admin) Account Settings", settingValues);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * @param accountValues
     * @param userToken
     * @purpose used to update AccountSettingValues Admin side
     * @return list of region
     */
    public Object editAccountSettingValues(AccountSettingValues accountValues, String userToken) {

        if (fieldSenseUtils.isTokenValid(userToken)) {
            int accountId = fieldSenseUtils.accountIdForToken(userToken);
            if (accountValues.isAllowTimeoutForAll() == true) {
                fieldSenseUtils.allowTimeoutForAllUsersInAccount(accountId);
            }
            // Added by jyoti 21-12-2016
            if (accountValues.isAllowOffline() == true) {
                fieldSenseUtils.allowOfflineForAllUsersInAccount(accountId);
            }
//            // Added by jyoti, 26-12-2017
            List<AccountSetting> settings = accountSettingDao.selectAllAccountSettings(accountId);
            
            // modified by jyoti
            OUTER:
            for (AccountSetting set : settings) {
                switch (set.getSettingName()) { // modified by jyoti, changed if to switch
                    case "auto_punch_out_time": // added auto_punchout value
//                        System.out.println("userToken > "  + userToken + " ,db value >> " + set.getSettingValue() + " , user setting > " +accountValues.getAuto_punch_out_time());
                        if (!(set.getSettingValue().equals(accountValues.getAuto_punch_out_time()))) {
                            accountSettingDao.editAccountSettings(accountId, accountValues.getAuto_punch_out_time(), "auto_punch_out_time");
                        }
                        break;
                    case "auto_punch_out_type": // added auto_punchout value
//                        System.out.println("userToken > "  + userToken + " ,db value >> " + set.getSettingValue()+ " , user setting > " +accountValues.getAuto_punch_out_type());
                        if (!(Integer.parseInt(set.getSettingValue()) == accountValues.getAuto_punch_out_type())) {
                            accountSettingDao.editAccountSettings(accountId, Integer.toString(accountValues.getAuto_punch_out_type()), "auto_punch_out_type");
                        }
                        break;
                    case "working_hours": // added working_hours value
//                        System.out.println("userToken > "  + userToken + " ,db value >> " + set.getSettingValue() + " , user setting > " +accountValues.getWorking_hours());
                        if (!(Integer.parseInt(set.getSettingValue()) == accountValues.getWorking_hours())) {
                            accountSettingDao.editAccountSettings(accountId, Integer.toString(accountValues.getWorking_hours()), "working_hours");
                        }
                        break;
                    case "allow_timeout":
                        String allowTimeout = "0";
                        if (accountValues.isAllowTimeout()) {
                            allowTimeout = "1";
                        }
                        if (!(set.getSettingValue().equals(allowTimeout))) {
                            accountSettingDao.editAccountSettings(accountId, allowTimeout, "allow_timeout");
                        }
                        break;
                    case "allow_offline":
                        String allowOffline = "0";
                        if (accountValues.isAllowOffline()) {
                            allowOffline = "1";
                        }
                        if (!(set.getSettingValue().equals(allowOffline))) {
                            accountSettingDao.editAccountSettings(accountId, allowOffline, "allow_offline");
                        }
                        break;
                    case "Location_interval":
                        if (!(set.getSettingValue().equals(accountValues.getInterval()))) {
                            accountSettingDao.editAccountSettings(accountId, accountValues.getInterval(), "Location_interval");
                        }
                        break;
                    case "local_time_zone_code":
                        if (!(set.getSettingValue().equals(accountValues.getTimeZone()))) {
                            accountSettingDao.editAccountSettings(accountId, accountValues.getTimeZone(), "local_time_zone_code");
                        }
                        break;
                    default:
                        break OUTER;
                }
            }
            // modified by jyoti ended
            
            // for push notification
            // Added by Jyoti, 04-01-2018
            RunnableThreadJob theJob = new RunnableThreadJob();
            theJob.sendEditAccountSettingsNotificationToUsersOfAccount(accountValues, accountId);
            // Ended by Jyoti, 04-01-2018           

            // commented by jyoti
//               accountSettingDao.editAccountSettingValues(accountValues,accountId);
//               accountSettingDao.editAccountSettingValuesForOffline(accountValues,accountId); // added by jyoti 21-12-2016               
//               accountSettingDao.editAccountSettingValuesForInterval(accountValues,accountId); // added by nikhil
//               accountSettingDao.editAccountSettingValuesForCurrencySymbol(accountValues,accountId); // added by jyoti 22-08-2017
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " (Admin) Account Settings Updated Successfully.", "Account Settings Updated.", "");
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }

    }

    /**
     * @param accountValues
     * @param id
     * @Added by jyoti 22-12-2016
     * @param userToken
     * @purpose used to update flag For allow Offline Super Admin
     * @return list of region
     */
    public Object editAccountSettingValuesForOffline(AccountSettingValues accountValues, String userToken, int id) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            if (fieldSenseUtils.isAccountValid(id)) {
                accountSettingDao.editAccountSettingValuesForOffline(accountValues, id);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Account Settings Updated Successfully.", "Account Settings Updated.", "");
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid accountId .", "", "");
            }
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * @param userToken
     * @purpose used to get details of all account regions
     * @return list of region
     */
    public Object selectAllAccountRegions(String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            List<java.util.HashMap> regions = accountRegistrationDao.getAllAccountRegions();
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " account ", regions);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * @Added by siddhesh
     * @param listOfIds
     * @param userToken
     * @return
     */
    public Object deleteAdminUser(ArrayList<Integer> listOfIds, String userToken) {
        TeamManager teamManger = new TeamManager();
        boolean checkDeletedStatus = false;
        for (int i = 0; i < listOfIds.size(); i++) {
            int id = listOfIds.get(i);
            int accountId = accountRegistrationDao.getAccountId(id);
            Map<String, Object> mapOfTeamData = accountRegistrationDao.getTeamDataForUser(id, accountId);
            if (mapOfTeamData.size() != 0) {
                int teamId = Integer.parseInt(mapOfTeamData.get("id").toString());
                int parentId = Integer.parseInt(mapOfTeamData.get("teamPosition").toString());
                String emailAddress = mapOfTeamData.get("emailAddress").toString();
                teamManger.deleteMemberFromOrganizationChartForSuperAdmin(teamId, parentId, emailAddress, accountId);
                if (accountRegistrationDao.deleteUser(id)) {
                    checkDeletedStatus = true;
                } else {
                    checkDeletedStatus = false;
                }
            } else {
                checkDeletedStatus = false;
            }
        }
        if (checkDeletedStatus) {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " User deleted ", "", "");
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Deletion failed ", "", "");
        }
    }

    /**
     * @Added by siddhesh
     * @param id
     * @param userToken
     * @return
     */
    public Object deleteAdminUser(int id, String userToken) {
        TeamManager teamManger = new TeamManager();
        boolean checkDeletedStatus = false;
        int accountId = accountRegistrationDao.getAccountId(id);
        if (accountRegistrationDao.getCountOfAdmin(accountId)) {
            Map<String, Object> mapOfTeamData = accountRegistrationDao.getTeamDataForUser(id, accountId);
            if (mapOfTeamData.size() != 0) {
                int teamId = Integer.parseInt(mapOfTeamData.get("id").toString());
                int parentId = Integer.parseInt(mapOfTeamData.get("teamPosition").toString());
                String emailAddress = mapOfTeamData.get("emailAddress").toString();
                teamManger.deleteMemberFromOrganizationChartForSuperAdmin(teamId, parentId, emailAddress, accountId);
                if (accountRegistrationDao.deleteUser(id)) {
                    checkDeletedStatus = true;
                } else {
                    checkDeletedStatus = false;
                }
            } else {
                checkDeletedStatus = false;
            }

        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " There is no other Active Admin User. Before deleting this user please assign admin role to some other active user. ", "", "");
        }
        if (checkDeletedStatus) {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " User deleted ", "", "");
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Deletion failed ", "", "");
        }
    }

    /**
     * @Added by Jyoti
     * @param accountLinkActivation
     * @purpose to send email with encrypted url
     * @return
     */
    public Object sendMailWithSecureLink(AccountLinkActivation accountLinkActivation) {
        try {
            String emailAddress = accountLinkActivation.getEmailAddress();
            String mobile_no = accountLinkActivation.getMobileNo();
            String mobileOnly = accountLinkActivation.getMobileOnly();
            String Country_code = mobile_no.substring(0, 2);
//            System.out.println("Country_code :-" + Country_code + " , flag : " + accountLinkActivation.isManualSendFlag());
//            String reCaptcha = accountLinkActivation.getUserReCaptcha();
//            System.out.println("recaptcha : " + reCaptcha);
//            if (!reCaptcha.isEmpty()) {
//                GoogleReCaptcha googleReCaptcha = new GoogleReCaptcha();
//                googleReCaptcha.setUrl(Constant.GOOGLE_RECAPTCHA_URL);
//                googleReCaptcha.setDomainSecretKey(Constant.GOOGLE_RECAPTCHA_DOMAIN_SECRET_VALUE);
//                googleReCaptcha.setUserResponse(accountLinkActivation.getUserReCaptcha());
//                if (fieldSenseUtils.googleReCaptchaURLCall(googleReCaptcha)) {
            EncryptionDecryption encryptdecrypt = new EncryptionDecryption();
            if (!emailAddress.isEmpty()) {
                if (fieldSenseUtils.isEmailExist(emailAddress) == 0) {
                    if (fieldSenseUtils.isMobileExist(mobileOnly) == 0) {
                        String encryptedKey = encryptdecrypt.hashCode(emailAddress);
                        Long timestamp = System.currentTimeMillis();
                        encryptedKey = encryptedKey + timestamp;
                        java.util.Random rnd = new java.util.Random();
                        String otp = (100000 + rnd.nextInt(900000)) + "";
                        boolean mobileNoEmail_notExist = accountRegistrationDao.isMobileNumberEmailExist(mobile_no, emailAddress);
                        if (mobileNoEmail_notExist) {
                            if (accountRegistrationDao.insertLinkActivationData(encryptedKey, emailAddress, otp, mobile_no)) {
                                EmailNotification emailNotification = new EmailNotification();
                                MessageNotifications notify = new MessageNotifications();
                                boolean sendMailToCreateAccoun = emailNotification.sendMailToCreateAccountUsingLink(encryptedKey, emailAddress);
                                boolean sendsms = notify.sendOTP1(otp, mobile_no, Country_code);
                                if (sendMailToCreateAccoun && sendsms) {
                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Mail sent successfully, Please click on the link sent to you to register with Fieldsense.", "1", "1");
                                } else {
                                    log4jLog.info("sendMailWithSecureLink failed");
                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid Details, Please try again.", "", "");
                                }
                            } else {
                                log4jLog.info("sendMailWithSecureLink failed");
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid Details, Please try again.", "", "");
                            }
                        } else {
                            boolean update_otp = accountRegistrationDao.resendOTP_re_Registration(encryptedKey, mobile_no, emailAddress, otp);
                            if (update_otp) {
                                EmailNotification emailNotification = new EmailNotification();
                                MessageNotifications notify = new MessageNotifications();
                                boolean sendMailToCreateAccoun = emailNotification.sendMailToCreateAccountUsingLink(encryptedKey, emailAddress);                                
                                if (sendMailToCreateAccoun && notify.sendOTP1(otp, mobile_no, Country_code)) {
                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Mail sent successfully, Please click on the link sent to you to register with Fieldsense.", "1", "1");
                                } else {
                                    log4jLog.info("sendMailWithSecureLink failed");
                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid Details, Please try again.", "", "");
                                }
                            } else {
//                                System.out.println("mail could not send some exception");
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid Details, Please try again.", "", "");
                            }

                        }
                    } else {
                        log4jLog.info("sendMailWithSecureLink existing user");
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.ALREADY_EXIST_MOBILE, Constant.ALREADY_EXIST_MOBILE, "This mobile number is already registered with FieldSense, Please Login.", "", "");
                    }
                } else {
                    log4jLog.info("sendMailWithSecureLink existing user");
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.ALREADY_EXIST, Constant.ALREADY_EXIST, "You are already registered with FieldSense, Please Login.", "", "");
                }
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Email address can not be blank, Please try again.", "", "");
            }
//            } else {
//                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.INVALID_INPUT, "reCaptcha can not be empty, Please try again.", "", "");
//            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.log(Level.INFO, "sendMailWithSecureLink exception for emailAddress : {0}{1}", new Object[]{accountLinkActivation.getEmailAddress(), e});
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Some exception occured, Please try again.", "", "");
        }

    }

    /**
     * @Added by Jyoti
     * @param encryptedKey
     * @purpose to redirect user on click of create account link
     * @return
     */
    public Object verifyLink(String encryptedKey) {
        try {
            AccountLinkActivation linkActivationData = accountRegistrationDao.selectLinkActivationData(encryptedKey);
            String emailAddress = linkActivationData.getEmailAddress();
            Timestamp createdOn = linkActivationData.getCreatedOn();
            if (!emailAddress.isEmpty()) {
                Calendar calendar = Calendar.getInstance();
                Date currentDateTime = calendar.getTime();
                calendar.setTime(createdOn);
                calendar.add(Calendar.MONTH, 1); // modified by jyoti, as new change from support team
                Date dateTimeAfterAddingHours = calendar.getTime();
                if (currentDateTime.after(dateTimeAfterAddingHours)) {
                    return "redirect:/account/redirectedUrl/" + "LINKEXPIRED" + "/errorPage.html/link";
                } else {
                    if (fieldSenseUtils.isEmailExist(emailAddress) == 0) {
                        log4jLog.info("verifyLink new user");
                        return "redirect:/account/redirectedUrl/" + encryptedKey + "/CreateAcc_user.html/link";
                    } else {
                        log4jLog.info("verifyLink existing user");
                        return "redirect:/account/redirectedUrl/" + emailAddress + "/login.html/link";
                    }
                }
            } else {
                log4jLog.info("verifyLink invalid");
                return "redirect:/account/redirectedUrl/" + "INVALIDLINK" + "/errorPage.html/link";
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info("verifyLink exception" + e);
            return "redirect:/account/redirectedUrl/" + "EXCEPTION" + "/errorPage.html/link";
        }
    }

    public Object verfiyOTP(String OTP, String mobile_no, String email_add) {

        AccountLinkActivation linkActivationData = accountRegistrationDao.validateOTP1(OTP, mobile_no, email_add);
        String otp = linkActivationData.getOTP();
        Timestamp otp_expiredOn = linkActivationData.getOTP_Expiration();
        Calendar calendar = Calendar.getInstance();
        Date currentDateTime = calendar.getTime();
        calendar.setTime(otp_expiredOn);

        Date dateTime_otpExpired = calendar.getTime();
        if (currentDateTime.after(dateTime_otpExpired)) {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " ", " ", "Expired");
        }
        if (otp.equalsIgnoreCase(OTP)) {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " ", "success");
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " ", "fail");
        }
    }

    public boolean resendOTP(String mobile_no, String email_add, String Country_code) {
        java.util.Random rnd = new java.util.Random();
        String otp = (100000 + rnd.nextInt(900000)) + "";
        boolean updateOtp = accountRegistrationDao.updateOTP(mobile_no, email_add, otp);
//        System.out.println("updateOtp" + updateOtp);
        if (updateOtp) {
            MessageNotifications notify = new MessageNotifications();
            notify.sendOTP1(otp, mobile_no, Country_code);
//        System.out.println("otp :-" + otp);
        }

        return updateOtp;
    }

    /**
     * @Added by jyoti
     * @param accountLinkActivation
     * @return
     */
    public Object verifyKeyReturnValidMailAddress(AccountLinkActivation accountLinkActivation) {
        try {
            accountLinkActivation = accountRegistrationDao.selectLinkActivationData(accountLinkActivation.getEncryptedkey());
            String emailAddress = accountLinkActivation.getEmailAddress();
            String mobileNumber = accountLinkActivation.getMobileNo();
            if (!(emailAddress.isEmpty()) & !(mobileNumber.isEmpty())) {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " accountLinkActivation   ", accountLinkActivation);
            } else {
                log4jLog.info("verifyKeyReturnValidMailAddress invalid");
                return "redirect:/account/redirectedUrl/" + "INVALIDLINK" + "/errorPage.html/link";
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.log(Level.INFO, "verifyKeyReturnValidMailAddress exception for emailAddress : {0}{1}", new Object[]{accountLinkActivation.getEmailAddress(), e});
            return "redirect:/account/redirectedUrl/" + "EXCEPTION" + "/errorPage.html/link";
        }
    }

    /**
     * @added by jyoti, remove later
     * @return 
     */
    public Object generateAuthToken(){
        List<Integer> accountIdList = fieldSenseUtils.getAllAccountIds();
        EncryptionDecryption encryptdecrypt = new EncryptionDecryption();
        
        for(int i=0; i<accountIdList.size(); i++){
//            System.out.println(accountIdList.get(i).toString());
            String encryptedKey = encryptdecrypt.hashCode(accountIdList.get(i).toString());
//            System.out.println("encryptedKey : "+encryptedKey);
            boolean flag = accountRegistrationDao.insertAuthToken(encryptedKey, accountIdList.get(i));
//            System.out.println("flag : "+flag);
            
        }
        return "success";
    }
    
    /**
     * @Added by jyoti, // remove later this method
     * @return 
     */
    public Object sendMailWithSecureLinkManually() {
        try {
            List<AccountLinkActivation> listOfAcc = accountRegistrationDao.selectListOfEmailIdsDetails();
            if (listOfAcc.size() > 0) {
                for (int i = 0; i < listOfAcc.size(); i++) {
                    String emailAddress = listOfAcc.get(i).getEmailAddress();
//                    System.out.println("emailAddress > " + emailAddress);
                    String mobile_no = listOfAcc.get(i).getMobileNo();
//                    System.out.println("mobile_no > " + mobile_no);
//                    System.out.println(emailAddress + mobile_no);
                    if (!emailAddress.isEmpty()) {
                        if (fieldSenseUtils.isEmailExist(emailAddress) == 0) {
                            if (fieldSenseUtils.isMobileExist(mobile_no) == 0) {
                                String encryptedKey = listOfAcc.get(i).getEncryptedkey();
                                boolean mobileNoEmail_notExist = accountRegistrationDao.isMobileNumberEmailExist(mobile_no, emailAddress);
                                if (mobileNoEmail_notExist) {
//                                    System.out.println("mobileNoEmail_notExist >> "+ mobileNoEmail_notExist);
                                    EmailNotification emailNotification = new EmailNotification();
                                    boolean sendMailToCreateAccoun = emailNotification.sendMailToCreateAccountUsingLinkManual(encryptedKey, emailAddress);
                                    if (sendMailToCreateAccoun) {
//                                        System.out.println(FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Mail sent successfully, Please click on the link sent to you to register with Fieldsense.", "1", "1"));
                                    } else {
                                        log4jLog.info("sendMailWithSecureLink failed");
//                                        System.out.println(FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid Details, Please try again.", "", ""));
                                    }
                                } else {
//                                    System.out.println("else mobileNoEmail_notExist >> "+ mobileNoEmail_notExist);
                                    java.util.Random rnd = new java.util.Random();
                                    String otp = (100000 + rnd.nextInt(900000)) + "";
                                    boolean update_otp = accountRegistrationDao.resendOTP_re_Registration(encryptedKey, mobile_no, emailAddress, otp);
                                    if (update_otp) {
//                                        System.out.println("if update_otp >> "+ update_otp);
                                        EmailNotification emailNotification = new EmailNotification();
                                        boolean sendMailToCreateAccoun = emailNotification.sendMailToCreateAccountUsingLinkManual(encryptedKey, emailAddress); // remove later this method

                                        if (sendMailToCreateAccoun) {
//                                            System.out.println(FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Mail sent successfully, Please click on the link sent to you to register with Fieldsense.", "1", "1"));
                                        } else {
//                                            System.out.println(FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid Details, Please try again.", "", ""));
                                        }
                                    } else {
//                                        System.out.println("else update_otp >> "+ update_otp);
//                                        System.out.println(FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid Details, Please try again.", "", ""));
                                    }

                                }
                            } else {
//                                System.out.println(FieldSenseUtils.generateFieldSenseResponse(Constant.ALREADY_EXIST_MOBILE, Constant.ALREADY_EXIST_MOBILE, "This mobile number is already registered with FieldSense, Please Login.", "", ""));
                            }
                        } else {
//                            System.out.println(FieldSenseUtils.generateFieldSenseResponse(Constant.ALREADY_EXIST, Constant.ALREADY_EXIST, "You are already registered with FieldSense, Please Login.", "", ""));
                        }
                    } else {
//                        System.out.println(FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Email address can not be blank, Please try again.", "", ""));
                    }
                }
                return "successfully sent >> "+listOfAcc.size();
            } else {
                return "no data";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Some exception occured, Please try again.", "", "");

        }
    }


    /**
     * @added by jyoti
     * @param encryptedKey
     * @return 
     */
    public Object verifyLinkOnManual(String encryptedKey) {
        try {
            AccountLinkActivation linkActivationData = accountRegistrationDao.selectLinkActivationData(encryptedKey);
            String emailAddress = linkActivationData.getEmailAddress();
            Timestamp createdOn = linkActivationData.getCreatedOn();
            String mobileNumber = linkActivationData.getMobileNo();
            String countryCode = mobileNumber.substring(0, 2);
            if (!emailAddress.isEmpty()) {
                Calendar calendar = Calendar.getInstance();
                Date currentDateTime = calendar.getTime();
                calendar.setTime(createdOn);
                calendar.add(Calendar.MONTH, 1); // modified by jyoti, as new change from support team
                Date dateTimeAfterAddingHours = calendar.getTime();
                if (currentDateTime.after(dateTimeAfterAddingHours)) {
//                    System.out.println("linkexpired");
                    return "redirect:/account/redirectedUrl/" + "LINKEXPIRED" + "/errorPage.html/link";
                } else {
                    if (fieldSenseUtils.isEmailExist(emailAddress) == 0) {
                        MessageNotifications notify = new MessageNotifications();
                        java.util.Random rnd = new java.util.Random();
                        String otp = (100000 + rnd.nextInt(900000)) + "";
//                        System.out.println(" encryptedKey > "+encryptedKey+" mobileNumber > "+mobileNumber+" emailAddress > "+emailAddress+" otp > "+otp);
                        boolean update_otp = accountRegistrationDao.resendOTP_re_Registration(encryptedKey, mobileNumber, emailAddress, otp);
//                        System.out.println("update_otp > " + update_otp);
                        if (update_otp) {
                            boolean sendsms = notify.sendOTP1(otp, mobileNumber, countryCode);
//                            System.out.println("sendsms > " + sendsms);
                        } else {
//                            System.out.println("sms not sent , update_otp > " + update_otp);
                        }

                        return "redirect:/account/redirectedUrl/" + encryptedKey + "/CreateAcc_user.html/link";
                    } else {
//                        System.out.println("isemailexist >> "+ fieldSenseUtils.isEmailExist(emailAddress));
                        return "redirect:/account/redirectedUrl/" + emailAddress + "/login.html/link";
                    }
                }
            } else {
//                System.out.println("verifyLinkOnManual invalid >> "+emailAddress);
                return "redirect:/account/redirectedUrl/" + "INVALIDLINK" + "/errorPage.html/link";
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info("verifyLinkOnManual exception" + e);
            return "redirect:/account/redirectedUrl/" + "EXCEPTION" + "/errorPage.html/link";
        }
    }


}
