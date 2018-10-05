/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.auth.model;

import com.qlc.fieldsense.accounts.dao.AccountSettingDao;
import com.qlc.fieldsense.attendance.dao.AttendanceDao;
import com.qlc.fieldsense.auth.dao.AuthenticationUserDao;
import com.qlc.fieldsense.memcached.model.MemcachedManager;
import com.qlc.fieldsense.mobile.dao.MobileDao;
import com.qlc.fieldsense.mobile.model.LeftSliderMenu;
import com.qlc.fieldsense.user.dao.UserDao;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.EncryptDecryptManager;
import com.qlc.fieldsense.utils.FieldSensePasswordEncryptionDecryption;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
//import net.spy.memcached.MemcachedClient;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;

/**
 *
 * @author Ramesh
 * @date 31-01-2014
 * @purpose This class is user to perform the business logic for user
 * authentication .
 */
public class AuthenticationUserManager {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("AuthenticationUserManager");
    AuthenticationUserDao authenticationUserDao = (AuthenticationUserDao) GetApplicationContext.ac.getBean("authenticationDaoImpl");
    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
    UserDao userDao = (UserDao) GetApplicationContext.ac.getBean("userDaoImpl");
    AccountSettingDao accountSettingDaoImpl = (AccountSettingDao) GetApplicationContext.ac.getBean("accountSettingDaoImpl");
    AttendanceDao attendanceDao = (AttendanceDao) GetApplicationContext.ac.getBean("attendanceDaoImpl");
    MobileDao mobileDao = (MobileDao) GetApplicationContext.ac.getBean("mobileDao"); // added by jyoti
    
    /**
     * @param login
     * @return 
     * @purpose used to login from mobile 
     */
    public Object authenticateUser(UserLogin login) {
        
        log4jLog.info("authenticateUser");
//        System.out.println("authenticateUser authenticateUser");
        if (fieldSenseUtils.isEmailExist(login.getUserEmailAddress()) > 0) { // check user email is exist
            if (fieldSenseUtils.isUserActive(login.getUserEmailAddress())) {// check user is active
//                if (fieldSenseUtils.getUserRole(login.getUserEmailAddress())!=2) {
                    if (loginUser(login)) {
                        int accountId = fieldSenseUtils.getUserAccountId(login.getUserEmailAddress()); // retrieve user account id
                        int userId = fieldSenseUtils.getUserId(login.getUserEmailAddress()); // retrieve user email id
                        int teamId = fieldSenseUtils.selectUsersTeamId(userId, accountId); // retrieve user team id
                
                        /*//Added by Mayank Ramaiya
                        int user_accuracy = fieldSenseUtils.getUserAccuracy(login.getUserEmailAddress()); // retrieve user accuracy
                        int user_check_in_radius = fieldSenseUtils.getCheckInRadius(login.getUserEmailAddress()); // retrieve user check-in radius
                        //End by Mayank Ramaiya
                        */
                        //Added by Awaneesh
                        String allowTimeoutValue=accountSettingDaoImpl.getValueFromAccountSetting("allow_timeout",accountId);
                        //End By Awaneesh
                        //added by Jyoti
                        String allowOfflineValue=accountSettingDaoImpl.getValueFromAccountSetting("allow_offline",accountId);                        
                        //End by Jyoti
                        if (teamId != 0) {
                            User user = userDao.selectUser(userId);
                            int user_accuracy= user.getUserAccuracy();
                            int user_check_in_radius=user.getCheckInRadius();
                            int allowTimeout= 0;
                            if(allowTimeoutValue.trim().equals("1")){
                                allowTimeout=user.getAllowTimeout();
                            }
                            
                            //Added by jyoti 23-12-2016
                            int offlineOperation = 0;
                            if(allowOfflineValue.trim().equals("1")){
                                offlineOperation = user.getAllowOffline();
                            }                            
                            // for currency_symbol, added by jyoti
//                            if(user.getCurrencySymbol().equals("0") || user.getCurrencySymbol().equals("")){
//                                String accSettingCurrencySymbol = accountSettingDaoImpl.getValueFromAccountSetting("currency_symbol",accountId);
//                                user.setCurrencySymbol(accSettingCurrencySymbol);
//                            }                            
                            // ended by jyoti
                            LeftSliderMenu leftSliderMenu=new LeftSliderMenu();
                            // added by manohar
                            List<LeftSliderMenu> punchList=new ArrayList<LeftSliderMenu>();
                            List<LeftSliderMenu> checkList=new ArrayList<LeftSliderMenu>();
                            List<LeftSliderMenu> getstatus=new ArrayList<LeftSliderMenu>();
                
                             punchList=attendanceDao.getPunchList(userId,accountId);
                             checkList=attendanceDao.getCheckList(userId,accountId);
                             getstatus=attendanceDao.getStatus(userId, accountId);
                
                            //System.out.println("CheckList"+checkList.size()+"Punchin list"+punchList.size());
                             if(getstatus.size()!=0)
                            {
                               leftSliderMenu.setAppointmentId(getstatus.get(0).getAppointmentId());
                               leftSliderMenu.setCustomerId(getstatus.get(0).getCustomerId());                              
                            }
                             if(checkList.size()!=0){
                                leftSliderMenu.setLastcheckInTime(checkList.get(0).getLastcheckInTime());
                                leftSliderMenu.setLastcheckOutTime(checkList.get(0).getLastcheckOutTime());
                              
                            }else{
                                leftSliderMenu.setLastcheckInTime("1970-01-01 00:00:00");
                                leftSliderMenu.setLastcheckOutTime("1970-01-01 00:00:00");
                              
                             }         
                             if(punchList.size()!=0){
                                    leftSliderMenu.setLastpunchInTime(punchList.get(0).getLastpunchInTime());
                                    leftSliderMenu.setLastpunchOutTime(punchList.get(0).getLastpunchOutTime());
                                if (punchList.get(0).getLastpunchOutTime().equals("00:00:00")) {
                                       leftSliderMenu.setAttendanceStatus(true);
                                    } else {
                                        leftSliderMenu.setAttendanceStatus(false);
                                        //mapOfUserInfo.put("status","PunchIn");
                                    }
                                }else{
                               leftSliderMenu.setLastpunchInTime("00:00:00");
                                leftSliderMenu.setLastpunchOutTime("00:00:00");
                                leftSliderMenu.setAttendanceStatus(false);
                                }
                             // ended by manohar         
                            
                            AuthenticationUser authenticationUser = new AuthenticationUser();
                            int firstLoginValue = authenticationUserDao.selectUserFirstLogin(login.getUserEmailAddress()); // check users first login or not
                            if (firstLoginValue == 0) {
                                authenticationUser.setIsFirstLogin(true);
                            } else {
                                authenticationUser.setIsFirstLogin(false);
                            }
                            authenticationUser.setLeftslider(leftSliderMenu); // added by manohar
                            authenticationUser.setAccountId(accountId);
                            authenticationUser.setUserId(userId);
                            authenticationUser.setUserFirstName(fieldSenseUtils.getUserFirstName(login.getUserEmailAddress())); // retrieve users first name
                            authenticationUser.setUserLastName(fieldSenseUtils.getUserLastName(login.getUserEmailAddress())); // retrieve users last name
                            authenticationUser.setAccountName(fieldSenseUtils.getAccountName(accountId)); //Added by siddhesh.
                            authenticationUser.setUserEmailAddress(login.getUserEmailAddress());
                            authenticationUser.setTeamId(teamId);
                            authenticationUser.setImsgrRetivalPath(FieldSenseUtils.getPropertyValue("PROFILEPIC_GET_PATH")); // retrieve image retrieval path
                            authenticationUser.setExpenseImageRetivalPath(FieldSenseUtils.getPropertyValue("IMAGE_GET_PATH")); // retrieve expense image retrieval path
                            byte[] key = ((EncryptDecryptManager) GetApplicationContext.ac.getBean("encryptDecryptManager")).getKey();
                            String enkriptingKey = Integer.toString(fieldSenseUtils.getUserAccountId(login.getUserEmailAddress())) + Integer.toString(fieldSenseUtils.getUserId(login.getUserEmailAddress()));
                            String token = ((EncryptDecryptManager) GetApplicationContext.ac.getBean("encryptDecryptManager")).encrypt(enkriptingKey, key); // retrieve token
                            authenticationUser.setUserToken(token);
                            authenticationUser.setCreatedOn(new Timestamp(0));
                            authenticationUser.setModifiedOn(new Timestamp(0));
                            
                            //Added by Mayank Ramaiya
                            authenticationUser.setUserAccuracy(user_accuracy);                            
                            authenticationUser.setCheckInRadius(user_check_in_radius);
                            //End by Mayank Ramaiya     
                            authenticationUser.setAllowTimeout(allowTimeout);
                            // added by jyoti
                            authenticationUser.setAllowOfflineOperation(offlineOperation);  // set offline status
//                            authenticationUser.setCurrencySymbol(user.getCurrencySymbol());
                            // ended by jyoti
                            authenticationUser.setRole(fieldSenseUtils.roleOfUser(userId)); // retrieve user role
                            authenticationUser.setUserDesignation(fieldSenseUtils.getUserDesignation(userId)); // retrieve user designation
                            authenticationUser.setGender(fieldSenseUtils.getUserGender(userId)); // retrieve user gender
                            authenticationUser.setMobileNo(fieldSenseUtils.getUserMobileNo(userId)); // retrieve user mobile 
                            
                            if (authenticationUserDao.insertAuthDetails(authenticationUser)) { // insert auth details
                                authenticationUserDao.updateGcmIdOfUser(login.getKey(),login.getDeviceOS(), userId); // update acm id
                                AuthenticationUser authenticationUser1 = authenticationUserDao.selectOfficeLatLong(login.getUserEmailAddress()); // retrieve office latlong
                                authenticationUser.setOfficelatitude(authenticationUser1.getOfficelatitude());
                                authenticationUser.setOfficelangitude(authenticationUser1.getOfficelangitude());
                                authenticationUserDao.updateUserFirstloginAndLastLoggedOn(1, userId); // update fist login flag and last logged details
                                Timestamp timeStamp=new Timestamp(System.currentTimeMillis()); 
//                                 System.out.println("End Time for login="+login.getUserEmailAddress()+"With user id="+userId+"@time="+timeStamp);                             
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Login successfully . ", " user ", authenticationUser);
                            } else {
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Login failed ", "  ", "");
                            }
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Your profile cannot be found in your organization's Reporting Structure. Please contact the account Admin.", "  ", "");
                        }
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid Password ", "  ", "");
                    }
//                } else {
//                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " You are not allowed to login through app ", "  ", "");
//                }    
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " user is inactive please contact admin . ", "  ", "");
            }
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "This username is not registered with FieldSense. Please contact the account Admin.", "  ", "");
        }
    }

    
    /**
     * 
     * @param login
     * @return 
     * @purpose used to login from mobile 
     */
    public Object authenticateUserMobileNo(UserLogin login) {
        
        log4jLog.info("authenticateUserMobileNo");
        
        if (fieldSenseUtils.isMobileExist(login.getUserMobileNo()) > 0) { // check user email is exist
            if (fieldSenseUtils.isUserActiveMobileNo(login.getUserMobileNo())) {// check user is active
//                 if (fieldSenseUtils.getUserRoleMobileNo(login.getUserMobileNo())!=2) {
                    if (loginUserMobileNo(login)) {
                        int accountId = fieldSenseUtils.getUserAccountIdMobileNo(login.getUserMobileNo()); // retrieve user account id
                        int userId = fieldSenseUtils.getUserIdMobileNo(login.getUserMobileNo()); // retrieve user email id
                        int teamId = fieldSenseUtils.selectUsersTeamId(userId, accountId); // retrieve user team id
                                            
                        /*//Added by Mayank Ramaiya
                        int user_accuracy = fieldSenseUtils.getUserAccuracy(login.getUserEmailAddress()); // retrieve user accuracy
                        int user_check_in_radius = fieldSenseUtils.getCheckInRadius(login.getUserEmailAddress()); // retrieve user check-in radius
                        //End by Mayank Ramaiya
                        */
                        String allowTimeoutValue=accountSettingDaoImpl.getValueFromAccountSetting("allow_timeout",accountId);                        
                        //added by Jyoti
                        String allowOfflineValue=accountSettingDaoImpl.getValueFromAccountSetting("allow_offline",accountId);
                        //End by Jyoti
                        
                        if (teamId != 0) {
                            
                            User user = userDao.selectUser(userId);
                            int user_accuracy= user.getUserAccuracy();
                            int user_check_in_radius=user.getCheckInRadius();
                            int allowTimeout= 0;
                            if(allowTimeoutValue.trim().equals("1")){
                                allowTimeout=user.getAllowTimeout();
                            }
                            
                            //Added by jyoti 23-12-2016
                            int offlineOperation = 0;
                            if(allowOfflineValue.trim().equals("1")){
                                offlineOperation= user.getAllowOffline();
                            }
                            // for currency_symbol, added by jyoti
//                            if(user.getCurrencySymbol().equals("0")){
//                                String accSettingCurrencySymbol = accountSettingDaoImpl.getValueFromAccountSetting("currency_symbol",accountId);
//                                user.setCurrencySymbol(accSettingCurrencySymbol);
//                            }
                            // ended by jyoti
                            
                            AuthenticationUser authenticationUser = new AuthenticationUser();
                            int firstLoginValue = authenticationUserDao.selectUserFirstLoginMobileNo(login.getUserMobileNo()); // check users first login or not
                            if (firstLoginValue == 0) {
                                authenticationUser.setIsFirstLogin(true);
                            } else {
                                authenticationUser.setIsFirstLogin(false);
                            }
                             LeftSliderMenu leftSliderMenu=new LeftSliderMenu();
//                         // added by manohar
                            List<LeftSliderMenu> punchList=new ArrayList<LeftSliderMenu>();
                            List<LeftSliderMenu> checkList=new ArrayList<LeftSliderMenu>();
                            List<LeftSliderMenu> getstatus=new ArrayList<LeftSliderMenu>();
                
                            punchList=attendanceDao.getPunchList(userId,accountId);
                            checkList=attendanceDao.getCheckList(userId,accountId);
                            getstatus=attendanceDao.getStatus(userId, accountId);
                
//                            System.out.println("CheckList"+checkList.size()+"Punchin list"+punchList.size());
                             if(getstatus.size()!=0)
                            {
                               leftSliderMenu.setAppointmentId(getstatus.get(0).getAppointmentId());
                               leftSliderMenu.setCustomerId(getstatus.get(0).getCustomerId());                            
                            }
                            if(checkList.size()!=0){
                               leftSliderMenu.setLastcheckInTime(checkList.get(0).getLastcheckInTime());
                               leftSliderMenu.setLastcheckOutTime(checkList.get(0).getLastcheckOutTime());
                             
                            }else{
                                leftSliderMenu.setLastcheckInTime("1970-01-01 00:00:00");
                                leftSliderMenu.setLastcheckOutTime("1970-01-01 00:00:00");
                            }
                            if(punchList.size()!=0)
                            {
                            leftSliderMenu.setLastpunchInTime(punchList.get(0).getLastpunchInTime());
                            leftSliderMenu.setLastpunchOutTime(punchList.get(0).getLastpunchOutTime());
                                if (punchList.get(0).getLastpunchOutTime().equals("00:00:00")) 
                                {
                                    leftSliderMenu.setAttendanceStatus(true);
                                }       
                                else {
                                         leftSliderMenu.setAttendanceStatus(false);
                        //mapOfUserInfo.put("status","PunchIn");
                                        }
                            }else{
                                leftSliderMenu.setLastpunchInTime("00:00:00");
                                 leftSliderMenu.setLastpunchOutTime("00:00:00");
                                 leftSliderMenu.setAttendanceStatus(false);
                                 }
                           // ended by manohar
                            authenticationUser.setLeftslider(leftSliderMenu); // added by manohar
                            authenticationUser.setAccountId(accountId);
                            authenticationUser.setUserId(userId);
                            authenticationUser.setUserFirstName(fieldSenseUtils.getUserFirstNameMobileNo(login.getUserMobileNo())); // retrieve users first name
                            authenticationUser.setUserLastName(fieldSenseUtils.getUserLastNameMobileNo(login.getUserMobileNo())); // retrieve users last name
                            authenticationUser.setUserEmailAddress(fieldSenseUtils.getUserEmailAddress(userId));// retrieve users email address
                            authenticationUser.setTeamId(teamId);
                            
                            //Added by Mayank Ramaiya
                            authenticationUser.setUserAccuracy(user_accuracy);                            
                            authenticationUser.setCheckInRadius(user_check_in_radius);
                            //End by Mayank Ramaiya     
                            authenticationUser.setAllowTimeout(allowTimeout);
                            // added by jyoti
                            authenticationUser.setAllowOfflineOperation(offlineOperation);  // set offline status
//                            authenticationUser.setCurrencySymbol(user.getCurrencySymbol());
                            // ended by jyoti
                            
                            authenticationUser.setImsgrRetivalPath(FieldSenseUtils.getPropertyValue("PROFILEPIC_GET_PATH")); // retrieve image retrieval path
                            authenticationUser.setExpenseImageRetivalPath(FieldSenseUtils.getPropertyValue("IMAGE_GET_PATH")); // retrieve expense image retrieval path
                            byte[] key = ((EncryptDecryptManager) GetApplicationContext.ac.getBean("encryptDecryptManager")).getKey();
                            String enkriptingKey = Integer.toString(fieldSenseUtils.getUserAccountIdMobileNo(login.getUserMobileNo())) + Integer.toString(fieldSenseUtils.getUserIdMobileNo(login.getUserMobileNo()));
                            String token = ((EncryptDecryptManager) GetApplicationContext.ac.getBean("encryptDecryptManager")).encrypt(enkriptingKey, key); // retrieve token
                            authenticationUser.setUserToken(token);
                            authenticationUser.setCreatedOn(new Timestamp(0));
                            authenticationUser.setModifiedOn(new Timestamp(0));
                            authenticationUser.setRole(fieldSenseUtils.roleOfUser(userId)); // retrieve user role
                            authenticationUser.setAccountName(fieldSenseUtils.getAccountName(accountId)); //Added by siddhesh
                            authenticationUser.setUserDesignation(fieldSenseUtils.getUserDesignation(userId)); // retrieve user designation
                            authenticationUser.setGender(fieldSenseUtils.getUserGender(userId)); // retrieve user gender
                            authenticationUser.setMobileNo(login.getUserMobileNo()); // retrieve user mobile 
                            if (authenticationUserDao.insertAuthDetails(authenticationUser)) { // insert auth details
                                authenticationUserDao.updateGcmIdOfUser(login.getKey(),login.getDeviceOS(), userId); // update acm id
                                AuthenticationUser authenticationUser1 = authenticationUserDao.selectOfficeLatLongMobileNo(login.getUserMobileNo()); // retrieve office latlong
                                authenticationUser.setOfficelatitude(authenticationUser1.getOfficelatitude());
                                authenticationUser.setOfficelangitude(authenticationUser1.getOfficelangitude());
                                authenticationUserDao.updateUserFirstloginAndLastLoggedOn(1, userId); // update fist login flag and last logged details
                                
                                log4jLog.info("authenticateUserMobileNo authenticationUser "+authenticationUser);
                                
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Login successfully . ", " user ", authenticationUser);
                            } else {
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Login failed ", "  ", "");
                            }
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Your profile cannot be found in your organization's Reporting Structure. Please contact the account Admin.", "  ", "");
                        }
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid Password ", "  ", "");
                    }
//                } else {
//                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " You are not allowed to login through app ", "  ", "");
//                }    
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " user is inactive please contact admin . ", "  ", "");
            }
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " user is  not registerd please contact admin . ", "  ", "");
        }
    }

    
     /**
     * @param login
     * @return 
     * @purpose used to login from web
     */
    public Object authenticateUserInWeb(UserLogin login) {
        if (fieldSenseUtils.isEmailExist(login.getUserEmailAddress()) > 0) { //check email id is valid
            if (fieldSenseUtils.isUserActive(login.getUserEmailAddress())) { //check user is active
//                 System.out.println("@@@@@@ "+fieldSenseUtils.CheckIfUserAccountActive(login.getUserEmailAddress()));
             if(fieldSenseUtils.CheckIfUserAccountActive(login.getUserEmailAddress())){//check user account active
//                System.out.println("@@@@@@ "+fieldSenseUtils.CheckIfUserAccountActive(login.getUserEmailAddress()));
             
                if (loginUser(login)) {
                    int accountId = fieldSenseUtils.getUserAccountId(login.getUserEmailAddress()); // retrieve user accountId
                    int userId = fieldSenseUtils.getUserId(login.getUserEmailAddress()); // retrive user id
                    int userRole = fieldSenseUtils.roleOfUser(userId);  // retrieve user role
                    int teamId = fieldSenseUtils.selectUsersTeamId(userId, accountId);  // retrieve users teamId
                    if (userRole == 0 || userRole == 1) { // check user is admin or super admin
                        AuthenticationUser authenticationUser = new AuthenticationUser();
                        int firstLoginValue = authenticationUserDao.selectUserFirstLogin(login.getUserEmailAddress());// check users first login or not
                        if (firstLoginValue == 0) {
                            authenticationUser.setIsFirstLogin(true);
                        } else {
                            authenticationUser.setIsFirstLogin(false);                        
                        }
                        authenticationUser.setAccountId(accountId);
                        authenticationUser.setUserId(userId);
                        authenticationUser.setUserFirstName(fieldSenseUtils.getUserFirstName(login.getUserEmailAddress())); // retrieve users first name
                        authenticationUser.setUserLastName(fieldSenseUtils.getUserLastName(login.getUserEmailAddress())); // retrieve users last name
                        authenticationUser.setUserEmailAddress(login.getUserEmailAddress()); // retrieve users email address
                        authenticationUser.setTeamId(teamId);
                        authenticationUser.setImsgrRetivalPath(FieldSenseUtils.getPropertyValue("PROFILEPIC_GET_PATH")); // retrieve image retrieval path
                        authenticationUser.setExpenseImageRetivalPath(FieldSenseUtils.getPropertyValue("IMAGE_GET_PATH")); // retrieve expense image retrieval path
                        authenticationUser.setImportErrorFileRetrivePath(FieldSenseUtils.getPropertyValue("IMPORT_ERROR_FILES_GET_PATH")); // retrieve import error file retrieval path
                        byte[] key = ((EncryptDecryptManager) GetApplicationContext.ac.getBean("encryptDecryptManager")).getKey();
                        String enkriptingKey = Integer.toString(fieldSenseUtils.getUserAccountId(login.getUserEmailAddress())) + Integer.toString(fieldSenseUtils.getUserId(login.getUserEmailAddress()));
                        String token = ((EncryptDecryptManager) GetApplicationContext.ac.getBean("encryptDecryptManager")).encrypt(enkriptingKey, key); // generate token
                        authenticationUser.setUserToken(token);
                        authenticationUser.setCreatedOn(new Timestamp(0));
                        authenticationUser.setModifiedOn(new Timestamp(0));
                        authenticationUser.setRole(userRole);
                         authenticationUser.setIs_terms_condition_agreed(fieldSenseUtils.getGDPRFlag(login.getUserEmailAddress())); //added by nikhil
//                            System.out.println("GdprFlag :- "+authenticationUser.getIs_terms_condition_agreed());
                        if (authenticationUserDao.insertAuthDetails(authenticationUser)) { // insert auth details in user_auth table
                            // Added by jyoti, 
//                                MemcachedClient memCachedClient = MemcachedManager.getMemCachedConnection();
//                                memCachedClient.set(token, 900, userId);
//                                System.out.println(memCachedClient.get(token));
//                                memCachedClient.shutdown();
                            // ended by jyoti
                            authenticationUserDao.updateUserFirstloginAndLastLoggedOn(1, userId); // update users first login flag and last logged time
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Login successfully . ", " user ", authenticationUser);
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Login failed ", "  ", "");
                        }
                    } else {
                        if (teamId != 0) {
                            AuthenticationUser authenticationUser = new AuthenticationUser();
                            int firstLoginValue = authenticationUserDao.selectUserFirstLogin(login.getUserEmailAddress());
                            if (firstLoginValue == 0) {
                                authenticationUser.setIsFirstLogin(true);
                            } else {
                                authenticationUser.setIsFirstLogin(false);
                            }
                            authenticationUser.setAccountId(accountId);
                            authenticationUser.setUserId(userId);
                            authenticationUser.setUserFirstName(fieldSenseUtils.getUserFirstName(login.getUserEmailAddress()));
                            authenticationUser.setUserLastName(fieldSenseUtils.getUserLastName(login.getUserEmailAddress()));
                            authenticationUser.setUserEmailAddress(login.getUserEmailAddress());
                            authenticationUser.setTeamId(teamId);
                            authenticationUser.setImsgrRetivalPath(FieldSenseUtils.getPropertyValue("PROFILEPIC_GET_PATH"));
                            authenticationUser.setExpenseImageRetivalPath(FieldSenseUtils.getPropertyValue("IMAGE_GET_PATH"));
                            authenticationUser.setImportErrorFileRetrivePath(FieldSenseUtils.getPropertyValue("IMPORT_ERROR_FILES_GET_PATH"));
                            byte[] key = ((EncryptDecryptManager) GetApplicationContext.ac.getBean("encryptDecryptManager")).getKey();
                            String enkriptingKey = Integer.toString(fieldSenseUtils.getUserAccountId(login.getUserEmailAddress())) + Integer.toString(fieldSenseUtils.getUserId(login.getUserEmailAddress()));
                            String token = ((EncryptDecryptManager) GetApplicationContext.ac.getBean("encryptDecryptManager")).encrypt(enkriptingKey, key);
                            authenticationUser.setUserToken(token);
                            authenticationUser.setCreatedOn(new Timestamp(0));
                            authenticationUser.setModifiedOn(new Timestamp(0));
                            authenticationUser.setRole(userRole);
                            authenticationUser.setIs_terms_condition_agreed(fieldSenseUtils.getGDPRFlag(login.getUserEmailAddress())); //added by nikhil
//                            System.out.println("GdprFlag :- "+authenticationUser);
                            if (authenticationUserDao.insertAuthDetails(authenticationUser)) {
                                // Added by jyoti, 
//                                MemcachedClient memCachedClient = MemcachedManager.getMemCachedConnection();
//                                memCachedClient.set(token, 900, userId);
//                                System.out.println(memCachedClient.get(token));
//                                memCachedClient.shutdown();
                                // ended by jyoti
                                authenticationUserDao.updateUserFirstloginAndLastLoggedOn(1, userId);
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Login successfully . ", " user ", authenticationUser);
                            } else {
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Login failed ", "  ", "");
                            }
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Your profile cannot be found in your organization's Reporting Structure. Please contact the account Admin.", "  ", "");
                        }
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid Password ", "  ", "");
                }
             }else{
                  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Your account has been deactivated. Please contact your administrator. ", "  ", "");
             }
                //else condition
            }else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "This username is not registered with FieldSense. Please contact the account Admin.", "  ", "");
            }
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "This username is not registered with FieldSense. Please contact the account Admin.", "  ", "");
        }
            
    }
    
    /**
     * 
     * @param login
     * @return 
     * @purpose used to login from web using mobile number
     */
    public Object authenticateUserInWebMobileNo(UserLogin login) {
        if (fieldSenseUtils.isMobileExist(login.getUserMobileNo()) > 0) { //check email id is valid
            
            if (fieldSenseUtils.isUserActiveMobileNo(login.getUserMobileNo())) { //check user is active
                
                if(fieldSenseUtils.isUserAccountActiveMobileNo(login.getUserMobileNo())){   
                if (loginUserMobileNo(login)) {
                    int accountId = fieldSenseUtils.getUserAccountIdMobileNo(login.getUserMobileNo()); // retrieve user accountId
                    int userId = fieldSenseUtils.getUserIdMobileNo(login.getUserMobileNo()); // retrive user id
                    int userRole = fieldSenseUtils.roleOfUser(userId);  // retrieve user role
                    int teamId = fieldSenseUtils.selectUsersTeamId(userId, accountId);  // retrieve users teamId
                    if (userRole == 0 || userRole == 1) { // check user is admin or super admin
                        AuthenticationUser authenticationUser = new AuthenticationUser();
                        int firstLoginValue = authenticationUserDao.selectUserFirstLoginMobileNo(login.getUserMobileNo());// check users first login or not
                        if (firstLoginValue == 0) {
                            authenticationUser.setIsFirstLogin(true);
                        } else {
                            authenticationUser.setIsFirstLogin(false);
                        }
                        authenticationUser.setAccountId(accountId);
                        authenticationUser.setUserId(userId);
                        authenticationUser.setUserFirstName(fieldSenseUtils.getUserFirstNameMobileNo(login.getUserMobileNo())); // retrieve users first name
                        authenticationUser.setUserLastName(fieldSenseUtils.getUserLastNameMobileNo(login.getUserMobileNo())); // retrieve users last name
                        authenticationUser.setMobileNo(login.getUserMobileNo());
                        authenticationUser.setUserEmailAddress(fieldSenseUtils.getUserEmailAddress(userId));// retrieve users email address
                        authenticationUser.setTeamId(teamId);
                        authenticationUser.setImsgrRetivalPath(FieldSenseUtils.getPropertyValue("PROFILEPIC_GET_PATH")); // retrieve image retrieval path
                        authenticationUser.setExpenseImageRetivalPath(FieldSenseUtils.getPropertyValue("IMAGE_GET_PATH")); // retrieve expense image retrieval path
                        authenticationUser.setImportErrorFileRetrivePath(FieldSenseUtils.getPropertyValue("IMPORT_ERROR_FILES_GET_PATH")); // retrieve import error file retrieval path
                        byte[] key = ((EncryptDecryptManager) GetApplicationContext.ac.getBean("encryptDecryptManager")).getKey();
                        String enkriptingKey = Integer.toString(fieldSenseUtils.getUserAccountIdMobileNo(login.getUserMobileNo())) + Integer.toString(fieldSenseUtils.getUserIdMobileNo(login.getUserMobileNo()));
                        String token = ((EncryptDecryptManager) GetApplicationContext.ac.getBean("encryptDecryptManager")).encrypt(enkriptingKey, key); // generate token
                        authenticationUser.setUserToken(token);
                        authenticationUser.setCreatedOn(new Timestamp(0));
                        authenticationUser.setModifiedOn(new Timestamp(0));
                        authenticationUser.setRole(userRole);
                        System.out.println("login.getUserMobileNo() "+login.getUserMobileNo());
                         authenticationUser.setIs_terms_condition_agreed(fieldSenseUtils.getGDPRFlagMobileNo(login.getUserMobileNo())); //added by nikhil
                               System.out.println("getGDPRFlagMobileNo  admin "+fieldSenseUtils.getGDPRFlagMobileNo(login.getUserMobileNo()));
                        if (authenticationUserDao.insertAuthDetails(authenticationUser)) { // insert auth details in user_auth table
                            authenticationUserDao.updateUserFirstloginAndLastLoggedOn(1, userId); // update users first login flag and last logged time
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Login successfully . ", " user ", authenticationUser);
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Login failed ", "  ", "");
                        }
                    } else {
                        if (teamId != 0) {
                            AuthenticationUser authenticationUser = new AuthenticationUser();
                            int firstLoginValue = authenticationUserDao.selectUserFirstLoginMobileNo(login.getUserMobileNo());
                            if (firstLoginValue == 0) {
                                authenticationUser.setIsFirstLogin(true);
                            } else {
                                authenticationUser.setIsFirstLogin(false);
                            }
                            authenticationUser.setAccountId(accountId);
                            authenticationUser.setUserId(userId);
                            authenticationUser.setUserFirstName(fieldSenseUtils.getUserFirstNameMobileNo(login.getUserMobileNo()));
                            authenticationUser.setUserLastName(fieldSenseUtils.getUserLastNameMobileNo(login.getUserMobileNo()));
                            authenticationUser.setMobileNo(login.getUserMobileNo());
                            authenticationUser.setUserEmailAddress(fieldSenseUtils.getUserEmailAddress(userId));// retrieve users email address
                            authenticationUser.setTeamId(teamId);
                            authenticationUser.setImsgrRetivalPath(FieldSenseUtils.getPropertyValue("PROFILEPIC_GET_PATH"));
                            authenticationUser.setExpenseImageRetivalPath(FieldSenseUtils.getPropertyValue("IMAGE_GET_PATH"));
                            authenticationUser.setImportErrorFileRetrivePath(FieldSenseUtils.getPropertyValue("IMPORT_ERROR_FILES_GET_PATH"));
                            byte[] key = ((EncryptDecryptManager) GetApplicationContext.ac.getBean("encryptDecryptManager")).getKey();
                            String enkriptingKey = Integer.toString(fieldSenseUtils.getUserAccountIdMobileNo(login.getUserMobileNo())) + Integer.toString(fieldSenseUtils.getUserIdMobileNo(login.getUserMobileNo()));
                            String token = ((EncryptDecryptManager) GetApplicationContext.ac.getBean("encryptDecryptManager")).encrypt(enkriptingKey, key);
                            authenticationUser.setUserToken(token);
                            authenticationUser.setCreatedOn(new Timestamp(0));
                            authenticationUser.setModifiedOn(new Timestamp(0));
                            authenticationUser.setRole(userRole);
                              System.out.println("login.getUserMobileNo() "+login.getUserMobileNo());
                             authenticationUser.setIs_terms_condition_agreed(fieldSenseUtils.getGDPRFlagMobileNo(login.getUserMobileNo()));
                             System.out.println("getGDPRFlagMobileNo non admin "+fieldSenseUtils.getGDPRFlagMobileNo(login.getUserMobileNo()));
                            if (authenticationUserDao.insertAuthDetails(authenticationUser)) {
                                authenticationUserDao.updateUserFirstloginAndLastLoggedOn(1, userId);
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Login successfully . ", " user ", authenticationUser);
                            } else {
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Login failed ", "  ", "");
                            }
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Your profile cannot be found in your organization's Reporting Structure. Please contact the account Admin.", "  ", "");
                        }
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid Password ", "  ", "");
                }
                }else
                {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Your account has been deactivated. Please contact your administrator. ", "  ", "");
                }
                //else condition
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " user is inactive please contact admin . ", "  ", "");
            }
              
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "This username is not registered with FieldSense. Please contact the account Admin.", "  ", "");
        }
    }
    

    /**
     * @param userToken
     * @param hasKey
     * @return 
     * @purpose used to log out. hashkey used to differentiate logout from mobile and web. 1 is for mobile and 0 is for web
     */
    public Object logoutUser(String userToken, int hasKey) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            int userId = fieldSenseUtils.userIdForToken(userToken); // retrieve user id from token
            if (deleteAuthToken(userToken)) { // delete user auth details
                if (hasKey == 1) {  // check logout from mobile or not
                    authenticationUserDao.updateGcmIdOfUser("0",0, userId); // update gcm id to 0
                }
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Logout successfully . ", "  ", "");
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Logout failed ", "  ", "");
            }
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     *
     * @param login
     * @return
     */
    public boolean loginUser(UserLogin login) {
        String password = authenticationUserDao.getUserPassword(login.getUserEmailAddress()); // retieve user password
        if (FieldSensePasswordEncryptionDecryption.hashPassword(login.getPassword()).equals(password)) { // check user password match
            return true;
        } else {
            return false;
        }
    }
    
    /**
     *
     * @param login
     * @return
     */
    public boolean loginUserMobileNo(UserLogin login) {
        String password = authenticationUserDao.getUserPasswordMobileNo(login.getUserMobileNo()); // retieve user password
        if (FieldSensePasswordEncryptionDecryption.hashPassword(login.getPassword()).equals(password)) { // check user password match
            return true;
        } else {
            return false;
        }
    }

     /**
     * 
     * @param userToken
     * @return logged in users authentication details
     */
    public Object getUserAuthDetails(String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                AuthenticationUser authenticationUser = authenticationUserDao.selectUserAuthDetails(userToken); //retrieve users auth details
                authenticationUser.setImsgrRetivalPath(FieldSenseUtils.getPropertyValue("PROFILEPIC_GET_PATH")); // retrieve image retrieval path
                authenticationUser.setExpenseImageRetivalPath(FieldSenseUtils.getPropertyValue("IMAGE_GET_PATH")); // retrieve expense image retrieval path
                authenticationUser.setImportErrorFileRetrivePath(FieldSenseUtils.getPropertyValue("IMPORT_ERROR_FILES_GET_PATH")); // retrieve iport error file retrieval path
                authenticationUser.setCustomFormImageRetivalPath(FieldSenseUtils.getPropertyValue("CUSTOM_FORM_IMAGE_GET_PATH")); //retrieve custom form images apth
                authenticationUser.setTeamId(fieldSenseUtils.selectUsersTeamId(authenticationUser.getUserId(), authenticationUser.getAccountId())); // retrieve users team id
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " authenticationUser ", authenticationUser);
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     *
     * @param token
     * @return
     */
    public boolean deleteAuthToken(String token) {
        return authenticationUserDao.deleteToken(token); // delete user auth details
    }

    /**
     * 
     * @param login
     * @return first login value 0- first login 1- not first login ,-1- user not registered
     * @purpose used check login is users first login or not
     */
    public Object userFirstLogin(UserLogin login) {
        int firstLoginValue = authenticationUserDao.selectUserFirstLogin(login.getUserEmailAddress()); // retrive user first login flag
        if (firstLoginValue == -1) {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "User is not registered .", "", firstLoginValue);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "firs login value", firstLoginValue);
        }
    }
    
    /**
     * @Added by jyoti, [for optimization]
     * @param login
     * @return 
     * @purpose used to login from mobile using emailAddress 
     */
    public Object authenticateUserEmailAddress_V2(UserLogin login) {
        String emailAddress = login.getUserEmailAddress();            
        try{
            log4jLog.info("authenticateUserEmailAddress_V2 for : "+emailAddress);
            if (fieldSenseUtils.isEmailExist(emailAddress) > 0) {
                if(fieldSenseUtils.CheckIfUserAccountActive(emailAddress)){
                if (fieldSenseUtils.isUserActive(emailAddress)) {
                    if (loginUser(login)) {
                        java.util.HashMap userHashMap = fieldSenseUtils.getUserIdAccountIdBasedOnEmailAddress(emailAddress);
                        int userId = (Integer) userHashMap.get("userId");
                        int accountId = (Integer) userHashMap.get("accountId");
                        return getAuthUserData(userId, accountId, login);                
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid Password. ", "  ", "");
                    }   
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " User is inactive, please contact admin. ", "  ", "");
                }
            }else
            {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Your account has been deactivated. Please contact your administrator. ", "  ", "");
            }
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " User is not registerd, please contact admin. ", "  ", "");
            }
        } catch(Exception e){
            log4jLog.info("authenticateUserEmailAddress_V2 for "+emailAddress+", Exception : "+e);
            e.printStackTrace();
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Exception Occured.", "  ", "");
        }
    }

     /**
     * @Added by jyoti, [for optimization]
     * @param login
     * @return 
     * @purpose used to login from mobile using mobile number
     */
    public Object authenticateUserMobileNo_V2(UserLogin login) {
        String mobileNumber = login.getUserMobileNo();
        try{            
            log4jLog.info("authenticateUserMobileNo_V2 for : "+mobileNumber);        
            if (fieldSenseUtils.isMobileExist(mobileNumber) > 0) { // check user email is exist
                 if (fieldSenseUtils.isUserAccountActiveMobileNo(mobileNumber)){
                if (fieldSenseUtils.isUserActiveMobileNo(mobileNumber)) {// check user is active
                    if (loginUserMobileNo(login)) {
                        java.util.HashMap userHashMap = fieldSenseUtils.getUserIdAccountIdBasedOnMobileNo(mobileNumber);
                        int userId = (Integer) userHashMap.get("userId");
                        int accountId = (Integer) userHashMap.get("accountId");
                        return getAuthUserData(userId, accountId, login);
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid Password. ", "  ", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " User is inactive, Please contact admin. ", "  ", "");
                }
                 }else {
                 return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Your account has been deactivated. Please contact your administrator. ", "  ", "");
                 
                 }
                 //
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " User is not registerd, please contact admin. ", "  ", "");
            }
        } catch(Exception e){
            log4jLog.info("authenticateUserMobileNo_V2 for "+mobileNumber+", Exception : "+e);
            e.printStackTrace();
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Exception Occured.", "  ", "");
        }
    }
    
    /**
     * @param login
     * @Added by Jyoti, 17-dec-2017
     * @param userId
     * @param accountId
     * @return 
     */
    public Object getAuthUserData(int userId, int accountId, UserLogin login){
        try{            
            int teamId = fieldSenseUtils.selectUsersTeamId(userId, accountId);           
//            System.out.println("getAuthUserData teamId : "+teamId+" , userId : "+userId);
            if (teamId != 0) {
                AuthenticationUser authenticationUser = authenticationUserDao.selectAuthUserAllData(userId); // set all user level data
//                if(authenticationUser.isIsFirstLogin()){
                    authenticationUser.setTeamId(teamId);
                    authenticationUser.setAssignedTerritoryList(authenticationUserDao.assignedTerritoryList(userId, accountId));
                    authenticationUser.setExpenseCategoryList(authenticationUserDao.expenseCategoryList(accountId));
                    authenticationUser.setPurposeCategoryList(authenticationUserDao.purposeCategoryList(accountId));
                    authenticationUser.setIndustryCategoryList(authenticationUserDao.industryCategoryList(accountId));
//                }
                authenticationUser.setAccountName(fieldSenseUtils.getAccountName(accountId));
                // get Account level settings
                int allowTimeoutValue = 0, allowOfflineValue = 0, location_interval = 0, auto_punch_out_type = 0, working_hours = 0;                
                String autoPunchOutTime = "";
                List<com.qlc.fieldsense.accounts.model.AccountSetting> accountSettings = accountSettingDaoImpl.selectAllAccountSettings(accountId);
                for(com.qlc.fieldsense.accounts.model.AccountSetting settings : accountSettings){
                    switch (settings.getSettingName()) { // modified by jyoti, changed if to switch
                        case "allow_timeout":
                            allowTimeoutValue = Integer.parseInt(settings.getSettingValue().trim());
                            break;
                        case "allow_offline":
                            allowOfflineValue = Integer.parseInt(settings.getSettingValue().trim());
                            break;
                        case "Location_interval":
                            location_interval = Integer.parseInt(settings.getSettingValue().trim());
                            break;
                        case "auto_punch_out_time":     // added by jyoti, auto_punch_out conf
                            autoPunchOutTime = settings.getSettingValue().trim();
                            break;
                        case "auto_punch_out_type":     // added by jyoti, auto_punch_out conf
                            auto_punch_out_type = Integer.parseInt(settings.getSettingValue().trim());
                            break;
                        case "working_hours":     // added by jyoti, auto_punch_out conf
                            working_hours = Integer.parseInt(settings.getSettingValue().trim());
                            break;
                    }
                }                
                
                if(allowTimeoutValue == 1){
                    authenticationUser.setAllowTimeout(authenticationUser.getAllowTimoutUser()); // set allowtimeout status
                }                
                if(allowOfflineValue == 1){
                    authenticationUser.setAllowOfflineOperation(authenticationUser.getAllowOfflineUser()); // set allowoffline status
                }
                if(authenticationUser.getLocationIntervalUser() == 0){ // if user locationinterval value is 0 then set account level locationinterval value
                    authenticationUser.setLocationIntervalUser(location_interval);
                }
                authenticationUser.setAuto_punch_out_type(auto_punch_out_type); // added by jyoti, Feature #29532
                authenticationUser.setAuto_punch_out_time(autoPunchOutTime); // added by jyoti, Feature #29532
                authenticationUser.setWorking_hours(working_hours); // added by jyoti, Feature #29532
                authenticationUser.setImsgrRetivalPath(FieldSenseUtils.getPropertyValue("PROFILEPIC_GET_PATH"));
                authenticationUser.setExpenseImageRetivalPath(FieldSenseUtils.getPropertyValue("IMAGE_GET_PATH"));
                byte[] key = ((EncryptDecryptManager) GetApplicationContext.ac.getBean("encryptDecryptManager")).getKey();
                String enkriptingKey = Integer.toString(accountId + userId);
                String token = ((EncryptDecryptManager) GetApplicationContext.ac.getBean("encryptDecryptManager")).encrypt(enkriptingKey, key); // retrieve token
//                System.out.println("wh >> " + working_hours + "atype >> " +auto_punch_out_type + "atime >> " +autoPunchOutTime + "getAuthUserData token : "+token);
                authenticationUser.setUserToken(token);
                authenticationUser.setCreatedOn(new Timestamp(0));
                authenticationUser.setModifiedOn(new Timestamp(0));                
                
                if (authenticationUserDao.insertAuthDetails(authenticationUser)) {
                    
                    authenticationUserDao.updateGcmIdOfUser(login.getKey(), login.getDeviceOS(), userId);
                    authenticationUserDao.updateUserFirstloginAndLastLoggedOn(1, userId);
                    // update versionCode
                    try{
                        int appVersionOfUser = authenticationUser.getAppVersion();
//                        System.out.println("loginv2 appVersionOfUser : "+appVersionOfUser);
                        if(login.getAppVersion() != appVersionOfUser){
                            if(userDao.updateUserAppVersion(userId, login.getAppVersion())){
//                                System.out.println("loginv2, version updated to : "+login.getAppVersion());
                                authenticationUser.setAppVersion(login.getAppVersion());
                            }
                        }
                    } catch(Exception e){
//                        System.out.println("loginv2");
                        e.printStackTrace();
                    }
                    
                    // added lefslider data, to fix the issue of punch out/in etc due to late or no response from leftslider api after login - 09-jul-2019
                    LeftSliderMenu leftSliderMenu = getLeftSliderData_v2(userId, accountId, allowTimeoutValue);
                    authenticationUser.setLeftslider(leftSliderMenu);
                    //
//                    log4jLog.info("getAuthUserData :  "+authenticationUser);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Login successfully . ", " user ", authenticationUser);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Login failed ", "  ", "");
                }
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Your profile is not added in the organization chart , please contact the admin .", "  ", "");
            }
        } catch(NumberFormatException e){
            log4jLog.info("getAuthUserData for userId  "+userId + ", NumberFormatException : "+e);
            e.printStackTrace();
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " NumberFormatException Occured.", "  ", "");
        } catch (BeansException e) {
            log4jLog.info("getAuthUserData for userId  "+userId + ", BeansException : "+e);
            e.printStackTrace();
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " BeansException Occured.", "  ", "");
        }
    }
    
    /**
     * @Added by JYoti
     * @param userId
     * @param accountId
     * @param allowTimeoutValue
     * @return 
     * @deprecated 
     */
    public LeftSliderMenu getLeftSliderData(int userId, int accountId, int allowTimeoutValue){
        try{
            // Adding lefsliderData
            java.util.HashMap hMap = fieldSenseUtils.getDateTime();
            String currentDateTime = (String) hMap.get("currentDateTime");
            String dateTimeAfter24Hours = (String) hMap.get("dateTimeAfter24Hours");
//            System.out.println("getLeftSliderData dates : "+ currentDateTime + ", "+dateTimeAfter24Hours);
            LeftSliderMenu leftSliderMenu = mobileDao.getLeftSliderMenuV2(currentDateTime, dateTimeAfter24Hours, userId, accountId);
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            if(leftSliderMenu.getPunchDate().equals(currentDate))
                leftSliderMenu.setAttendanceStatus(true);
            else
                leftSliderMenu.setAttendanceStatus(false);

            if(allowTimeoutValue == 1){
                leftSliderMenu.setAttendanceTiemout(attendanceDao.selectLastTimeoutOfDayForUser(userId, leftSliderMenu.getPunchDate(), accountId));
            }

            java.util.Map<String,Object> maxUpdatedOn = authenticationUserDao.lastUpdatedOfTerritoriesWithCount(0,userId,accountId);
            Timestamp createdOn=fieldSenseUtils.converDateToTimestamp(maxUpdatedOn.get("created_on").toString());
            Timestamp updateOn=fieldSenseUtils.converDateToTimestamp(maxUpdatedOn.get("updated_on").toString());
            leftSliderMenu.setTerritoryCount(Integer.parseInt(maxUpdatedOn.get("id_count").toString()));
            if(createdOn.before(updateOn)){
                leftSliderMenu.setTerritoryLastUpdated(updateOn);
            }else{
                leftSliderMenu.setTerritoryLastUpdated (createdOn);
            }

            java.util.HashMap hMap_punchinout = fieldSenseUtils.getLastPunchInOutData(userId,accountId);
            java.util.HashMap hMap_checkinout = fieldSenseUtils.getLastCheckInOutData(userId,accountId);
            java.util.HashMap hMap_visitstatus = fieldSenseUtils.getLastVisitStatus(userId, accountId);
            
            String punchInDateTime = (String) hMap_punchinout.get("secondLastPunchInDate")+" "+(String) hMap_punchinout.get("secondLastPunchIn");
            String punchOutDateTime = (String) hMap_punchinout.get("secondLastPunchOutDate")+" "+(String) hMap_punchinout.get("secondLastPunchOut");
            
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date punchInDateTimeMillis = format.parse(punchInDateTime);
            java.util.Date punchOutDateTimeMillis = format.parse(punchOutDateTime);
            java.util.Date checkInDateTimeMillis = format.parse((String) hMap_checkinout.get("secondLastCheckIn"));
            java.util.Date checkOutDateTimeMillis = format.parse((String) hMap_checkinout.get("secondLastCheckOut"));
            
            leftSliderMenu.setLastPunchInDateTime(punchInDateTimeMillis.getTime());
            leftSliderMenu.setLastPunchOutDateTime(punchOutDateTimeMillis.getTime());
            leftSliderMenu.setLastCheckInDateTime(checkInDateTimeMillis.getTime());
            leftSliderMenu.setLastCheckOutDateTime(checkOutDateTimeMillis.getTime());
            leftSliderMenu.setAppointmentId((Integer) hMap_visitstatus.get("secondLastId"));
            leftSliderMenu.setCustomerId((Integer) hMap_visitstatus.get("secondLastCustId"));
            
            return leftSliderMenu;
        } catch(Exception e){
            log4jLog.info("getLeftSliderData for userId  "+userId + ", Exception : "+e);
            e.printStackTrace();
            return new LeftSliderMenu();
        }
    } 
    
    /**
     * @Added by jyoti, to send response of leftslider data in login api 30-july-2018
     * @param userId
     * @param accountId
     * @param versionCode
     * @return 
     */
    public LeftSliderMenu getLeftSliderData_v2(int userId, int accountId, int versionCode) {
        try {
            // Adding lefsliderData                
            java.util.HashMap hMap = fieldSenseUtils.getDateTime();
            String currentDateTime = (String) hMap.get("currentDateTime");
            String dateTimeAfter24Hours = (String) hMap.get("dateTimeAfter24Hours");
//                System.out.println("selectLeftSliderMenu_V2 versioncode : "+versionCode+"leftslider called for userid : "+userId+", dates : "+ currentDateTime + ", "+dateTimeAfter24Hours);
            LeftSliderMenu leftSliderMenu = mobileDao.getLeftSliderMenuV2(currentDateTime, dateTimeAfter24Hours, userId, accountId);

            // get Account level settings
            int allowTimeoutValue = 0, allowOfflineValue = 0, auto_punch_out_type = 0, working_hours = 0;
            String autoPunchOutTime = "";
            String location_intervalAccount = "";
            List<com.qlc.fieldsense.accounts.model.AccountSetting> accountSettings = accountSettingDaoImpl.selectAllAccountSettings(accountId);
            for (com.qlc.fieldsense.accounts.model.AccountSetting settings : accountSettings) {
                switch (settings.getSettingName()) { // modified by jyoti, changed if to switch
                    case "allow_timeout":
                        allowTimeoutValue = Integer.parseInt(settings.getSettingValue().trim());
                        break;
                    case "allow_offline":
                        allowOfflineValue = Integer.parseInt(settings.getSettingValue().trim());
                        break;
                    case "Location_interval":
                        location_intervalAccount = settings.getSettingValue().trim();
                        break;
                    case "auto_punch_out_time":     // added by jyoti, Feature #29532
                        autoPunchOutTime = settings.getSettingValue().trim();
                        break;
                    case "auto_punch_out_type":     // added by jyoti, Feature #29532
                        auto_punch_out_type = Integer.parseInt(settings.getSettingValue().trim());
                        break;
                    case "working_hours":     // added by jyoti, Feature #29532
                        working_hours = Integer.parseInt(settings.getSettingValue().trim());
                        break;
                }
            }

            // user data
            java.util.HashMap hMap_userData = fieldSenseUtils.getUserDataLeftSlider(userId, accountId);
            //update versionCode
            int appVersionOfUser = Integer.parseInt(hMap_userData.get("appVersion").toString());
//                System.out.println("selectLeftSliderMenu_V2 appVersionOfUser : "+appVersionOfUser);
            if (versionCode != appVersionOfUser) {
                if (userDao.updateUserAppVersion(userId, versionCode)) {
                    leftSliderMenu.setAppVersion(versionCode);
                }
            }

            leftSliderMenu.setAuto_punch_out_type(auto_punch_out_type); // added by jyoti, Feature #29532
            leftSliderMenu.setAuto_punch_out_time(autoPunchOutTime);  // added by jyoti, Feature #29532
            leftSliderMenu.setWorking_hours(working_hours);  // added by jyoti, Feature #29532
//                System.out.println("selectLeftSliderMenu_V2 autoPunchoutType > "+ auto_punch_out_type + " , autoPunchoutTime > "+ autoPunchOutTime + " working_hours >> "+working_hours); // comment later
            leftSliderMenu.setAppVersion(appVersionOfUser);
            leftSliderMenu.setUserRole(Integer.parseInt(hMap_userData.get("role").toString()));
            leftSliderMenu.setUserAccuracy(Integer.parseInt(hMap_userData.get("userAccuracy").toString()));
            leftSliderMenu.setCheckInRadius(Integer.parseInt(hMap_userData.get("checkInRadius").toString()));
            leftSliderMenu.setAccountName(hMap_userData.get("accountName").toString());
            String locationIntervalUser = hMap_userData.get("interval").toString();
            leftSliderMenu.setHasSubordinates(Integer.parseInt(hMap_userData.get("hasSubordinates").toString()));
            if (allowTimeoutValue == 1) { // if account level allowTimeoutValue value is true then set user level value else take default '0'
                leftSliderMenu.setAllowTimeout(Integer.parseInt(hMap_userData.get("allowTimeout").toString())); // set allowtimeout status
                if (leftSliderMenu.getAllowTimeout() == 1) {
                    leftSliderMenu.setAttendanceTiemout(attendanceDao.selectLastTimeoutOfDayForUser(userId, leftSliderMenu.getPunchDate(), accountId));
                }
            }
            if (allowOfflineValue == 1) { // if account level allowOfflineValue value is true then set user level value else take default '0'
                leftSliderMenu.setAllowOfflineOperation(Integer.parseInt(hMap_userData.get("allowOffline").toString())); // set allowoffline status
            }
            if (locationIntervalUser.trim().equals("0")) { // if user locationinterval value is 0 then set account level locationinterval value, 24-jul-2018
                leftSliderMenu.setLocationInterval(location_intervalAccount);
            } else {
                leftSliderMenu.setLocationInterval(locationIntervalUser);
            }
            if (leftSliderMenu.getPunchOutTime().equals("00:00:00")) {
                leftSliderMenu.setPunchOutTime(""); // requested by pune team, as 00:00:00 was considered as midnight time
                leftSliderMenu.setAttendanceStatus(true);
            } else {
                leftSliderMenu.setAttendanceStatus(false);
            }
//                System.out.println("selectLeftSliderMenu_V2 isAttendanceStatus > "+ leftSliderMenu.isAttendanceStatus()); // comment later
            java.util.Map<String, Object> maxUpdatedOn = mobileDao.lastUpdatedOfTerritoriesWithCount(0, userId, accountId);
            Timestamp createdOn = fieldSenseUtils.converDateToTimestamp(maxUpdatedOn.get("created_on").toString());
            Timestamp updateOn = fieldSenseUtils.converDateToTimestamp(maxUpdatedOn.get("updated_on").toString());
            leftSliderMenu.setTerritoryCount(Integer.parseInt(maxUpdatedOn.get("id_count").toString()));
            if (createdOn.before(updateOn)) {
                leftSliderMenu.setTerritoryLastUpdated(updateOn);
            } else {
                leftSliderMenu.setTerritoryLastUpdated(createdOn);
            }

            return leftSliderMenu;
        } catch (Exception e) {
            log4jLog.info("getLeftSliderData for userId  " + userId + ", Exception : " + e);
            e.printStackTrace();
            return new LeftSliderMenu();
        }
    }

    public HashMap<String,String> silderDataForOfflineAPI(String fromDate, String toDate,int userId, int accountId,String userToken){
        try{
        HashMap <String,String> mapOfSliderData=authenticationUserDao.getLeftSliderMenuForOffline(fromDate, toDate, userId, accountId);
        Map <String,String> mapOfUserDetails=userDao.selectUserForOffline(userId, accountId);            
        mapOfSliderData.putAll(mapOfUserDetails);
        return mapOfSliderData;
        }catch(Exception e){
        e.printStackTrace();
        return new HashMap();
        }
    
    }
    
    /**
     * @Added by jyoti, 02-02-2018, All Master Data
     * @param fromDate
     * @param lastSyncDate
     * @param userId
     * @param accountId
     * @return 
     */
    public HashMap<String,Object> getMasterDataAfterLastSync(String fromDate, String lastSyncDate,int userId, int accountId){
        try{
            HashMap<String,Object> masterData = new HashMap<String, Object>();
                List<HashMap<String,Object>> assignedTerritoryListAfterLastSync =  authenticationUserDao.assignedTerritoryListAfterLastSync(userId, accountId, lastSyncDate);
                List<HashMap<String,Object>> deletedassignedTerritoryListAfterLastSync =  fieldSenseUtils.getRecordStateForTerritory(accountId, "territoryCategory", lastSyncDate, userId);
                if(!deletedassignedTerritoryListAfterLastSync.isEmpty()){
                    assignedTerritoryListAfterLastSync.addAll(deletedassignedTerritoryListAfterLastSync);
                }
                List<HashMap<String,Object>> expenseCategoryListAfterLastSync =  authenticationUserDao.expenseCategoryListAfterLastSync(accountId, lastSyncDate);
                List<HashMap<String,Object>> deletedexpenseCategoryListAfterLastSync =  fieldSenseUtils.getRecordStateForExpense_Industry(accountId, "expenseCategory", lastSyncDate);
                if(!deletedexpenseCategoryListAfterLastSync.isEmpty()){
                    expenseCategoryListAfterLastSync.addAll(deletedexpenseCategoryListAfterLastSync);
                }
                List<HashMap<String,Object>> purposeCategoryListAfterLastSync =  authenticationUserDao.purposeCategoryListAfterLastSync(accountId, lastSyncDate);
                List<HashMap<String,Object>> deletedpurposeCategoryListAfterLastSync =  fieldSenseUtils.getRecordStateForPurpose(accountId, "purposeCategory", lastSyncDate);
                if(!deletedpurposeCategoryListAfterLastSync.isEmpty()){
                    purposeCategoryListAfterLastSync.addAll(deletedpurposeCategoryListAfterLastSync);
                }              
                List<HashMap<String,Object>> industryCategoryListAfterLastSync =  authenticationUserDao.industryCategoryListAfterLastSync(accountId, lastSyncDate);
                List<HashMap<String,Object>> deletedindustryCategoryListAfterLastSync =  fieldSenseUtils.getRecordStateForExpense_Industry(accountId, "industryCategory", lastSyncDate);
                if(!deletedindustryCategoryListAfterLastSync.isEmpty()){
                    industryCategoryListAfterLastSync.addAll(deletedindustryCategoryListAfterLastSync);
                }
                
            masterData.put("Territory", assignedTerritoryListAfterLastSync);
            masterData.put("CategoriesForExpense", expenseCategoryListAfterLastSync);
            masterData.put("PurposeList", purposeCategoryListAfterLastSync);
            masterData.put("IndustryData", industryCategoryListAfterLastSync);
            return masterData;
        } catch(Exception e){
            e.printStackTrace();
            return new HashMap<String, Object>();
        }
    
    }
    
    /**
     * @added by nikhil
     * @param authenticationUser
     * @return 
     */
    public Object updateTermsCondition(AuthenticationUser authenticationUser) {
        if (authenticationUserDao.update_terms_condition_agreed(authenticationUser)) {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "TermsCondition updated successfully .", "", "");
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "TermsCondition updation failed .", "", "");
        }
    }
    
}
