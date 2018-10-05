/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.user.model;

import au.com.bytecode.opencsv.CSVWriter;
import com.qlc.cacheLocation.dao.CacheLocationDao;
import com.qlc.cacheLocation.model.CacheLocation;
import com.qlc.cacheLocation.model.CacheLocationManager;
import com.qlc.fieldsense.accounts.dao.AccountRegistrationDao;
import com.qlc.fieldsense.auth.dao.AuthenticationUserDao;
import com.qlc.fieldsense.memcached.model.MemcachedManager;
import com.qlc.fieldsense.team.dao.TeamDao;
import com.qlc.fieldsense.team.model.Team;
import com.qlc.fieldsense.team.model.TeamManager;
import com.qlc.fieldsense.team.model.TeamMember;
import com.qlc.fieldsense.user.dao.UserDao;
import static com.qlc.fieldsense.user.service.UserService.log4jLog;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.EmailNotification;
import com.qlc.fieldsense.utils.FieldSensePasswordEncryptionDecryption;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import com.qlc.fieldsense.utils.MapsIconCreate;
import com.qlc.fieldsense.usersTravelLogs.dao.UsersTravelLogsDao;
import com.qlc.fieldsense.usersTravelLogs.model.UsersTravelLogs;
import com.qlc.fieldsense.utils.MessageNotifications;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Ramesh
 * @date 19-02-2014
 */
public class UserManager {

    UserDao userDao = (UserDao) GetApplicationContext.ac.getBean("userDaoImpl");
    AuthenticationUserDao authenticationUserDao = (AuthenticationUserDao) GetApplicationContext.ac.getBean("authenticationDaoImpl");
    CacheLocationDao cachelocationDao = (CacheLocationDao) GetApplicationContext.ac.getBean("cacheDaoImpl");
    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
    AccountRegistrationDao accountRegistrationDao = (AccountRegistrationDao) GetApplicationContext.ac.getBean("accountRegistrationDaoImpl");
    TeamDao teamDao = (TeamDao) GetApplicationContext.ac.getBean("teamDaoImpl");
    UsersTravelLogsDao usersTravelLogsDao = (UsersTravelLogsDao) GetApplicationContext.ac.getBean("usersTravelLogsDaoImpl");
//    MemcachedManager memCachedManager = new MemcachedManager(); // added by jyoti
    // Added by vaibhav
    int tempInt = 0;
    LinkedHashMap mapCyclic = new LinkedHashMap();
    ArrayList<Integer> arrayPositions = new ArrayList<Integer>();
    // ended by vaibhav
    
    /**
     * 
     * @param user
     * @param userToken
     * @return 
     * @purpose Used to create user at admin side.
     */
    public Object createNewUser(User user, String userToken) {
        
         log4jLog.info("createNewUser");
            
        if (fieldSenseUtils.isTokenValid(userToken)) {
                    if (FieldSenseUtils.isValidEmailAddress(user.getEmailAddress())) {
                        if (fieldSenseUtils.isEmailExist(user.getEmailAddress()) == 0) {
//                            System.out.println("user.getEmp_code()="+user.getEmp_code());
                            int accountIdForEmpCode= fieldSenseUtils.accountIdForToken(userToken);
                            if(user.getEmp_code().equals("")  || fieldSenseUtils.isEmpCodeExist(user.getEmp_code(),accountIdForEmpCode) == 0){    //added by manohar
                            if (fieldSenseUtils.isMobileExist(user.getMobileNo()) == 0) {
                                int accountId = user.getAccountId();       //  some id 
                                int accountId2 = user.getAccountId();      //some id 
                                if (accountId == 0) {
                                    accountId = fieldSenseUtils.accountIdForToken(userToken);
                                    user.setAccountId(accountId);
                                } else {
                                    user.setCreatedBy(fieldSenseUtils.userIdForToken(userToken));
                                }
                                int userId = fieldSenseUtils.userIdForToken(userToken);
                                int userLimitForAccount = fieldSenseUtils.userLimitForAccount(accountId);      //  200   
                                int totalUsersInAcount = fieldSenseUtils.totalUsersInAccount(accountId);      // 177 out of 200
                                if (totalUsersInAcount <= userLimitForAccount) {     //177 <= 200
                                    if (fieldSenseUtils.isUserAdminOrSupport(userId)) {    //   if its 1==ADMIN || 0==SUPPORT goahead otherwise 
                                        user.setPassword(FieldSensePasswordEncryptionDecryption.hashPassword(user.getPassword()));
                                        int userId2 = userDao.insertUser(user);   //  MAX(ID)   OF USERS
                                        try{
                                            userDao.insertUserTerritories(user.getAddTerritoryList(),user.getDeleteTerritoryList(),userId2,accountId);
                                        }catch(Exception e){
                                            log4jLog.info("createNewUser >> "+e);
//                                            e.printStackTrace();
                                        }
                                        if (userId2 != 0) {


                                            //default map marker for user .
                                            try{
                                            String bgImagePath = Constant.PROFILEPIC_UPLOAD_PATH + "FM.png";
                                            MapsIconCreate mapsIconCreate = new MapsIconCreate();
                                            BufferedImage mapMarker = mapsIconCreate.readImage(bgImagePath);
                                            mapsIconCreate.writeImage(mapMarker, Constant.PROFILEPIC_UPLOAD_PATH + accountId + "_" + userId2 + "_mm.png", "PNG");
                                            //email sending code.
                                            String userName = user.getFirstName() + " " + user.getLastName();
                                            EmailNotification emailNotification = new EmailNotification();
                                            emailNotification.registerUser(userName, user.getEmailAddress());
                                            //Message send code
                                            }catch(Exception e){
                                                e.printStackTrace();
                                                log4jLog.info("Exception while adding user >> "+e);
                                            }
                                            if (user.isMobileNotification()) {
                                                MessageNotifications messageNotifications = new MessageNotifications();
                                                String adminFirstName = fieldSenseUtils.getUserFirstName(fieldSenseUtils.userIdForToken(userToken));
                                                String adminLastName = fieldSenseUtils.getUserlastName(fieldSenseUtils.userIdForToken(userToken));
                                                messageNotifications.createUser(user.getFirstName(), user.getLastName(), adminFirstName, adminLastName, user.getMobileNo());
                                            }
                                            user = userDao.selectUser(userId2);
                                            insertTeamForNewUser(user,accountId,userToken); //create Team for it
                                            List<String> sqlQueriesList = userDao.getCreateUserDeafultQueries();
                                            if (!(sqlQueriesList == null || sqlQueriesList.isEmpty())) {
                                                userDao.executeCreateUserDeafultQueries(sqlQueriesList, userId2, accountId);
                                            }
                                            if (accountId2 != 0) {
                                                List<String> accountSqlQueriesList = accountRegistrationDao.getCreateAccountDeafultQueries();
                                                if (!(accountSqlQueriesList == null || accountSqlQueriesList.isEmpty())) {
                                                    accountRegistrationDao.executeAccountUserDeafultQueries(accountSqlQueriesList, userId2, accountId);
                                                }
                                            }
                                            
                                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.USER_CREATED_SUCCESFULLY, " user ", user);
                                        } else {
                                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " User not created . Plaese try again .", "", "");
                                        }
                                    } else {
                                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " You are not authorised to create user ", "", "");
                                    }
                                } else {
                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "You exceeds the user limit", "", "");
                                }
                            } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "User already exists with the same mobile number. Please try with different mobile number .", "", "");
                               }
                            } else {        //added by manohar
                             return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "User already exists with the same Emp Code. Please try with different emp code .", "", "");                      
                            }
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "User already exists with the same email address. Please try with different email address .", "", "");
                        }                   
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_EMAIL, "", "");
                    }   
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    /**
     * 
     * @param userToken
     * @return list of all users with details
     */
    public Object getUsers(String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<User> userList = new ArrayList<User>();
                userList = userDao.slectAllUsers(accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " user list ", userList);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * @param allRequestParams
     * @param userToken
     * @param response
     * @return details of all users with offset
     */
    public Object getUsersWithOffset( @RequestParam Map<String,String> allRequestParams, String userToken,HttpServletResponse response) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
          //  if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<User> userList = new ArrayList<User>();
                userList = userDao.slectAllUsersWithOffset(allRequestParams, accountId);
                int Total=0;
                if(!userList.isEmpty()){
                    Total=userList.get(0).getUsersCount();
                }
                return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " user list ", userList,Total);
           // }else{
              //  response.setContentType( MediaType.APPLICATION_JSON_VALUE);
               // response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
              //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
           // }
        } else {
            response.setContentType( MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    
    
    
    
    ////  added by manohar
    public Object getSuperUsers( @RequestParam Map<String,String> allRequestParams, String userToken,HttpServletResponse response) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
          //  if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<User> userList = new ArrayList<User>();
                userList = userDao.getSuperUsers(allRequestParams, accountId);
                int Total=0;
                if(!userList.isEmpty()){
                    Total=userList.get(0).getUsersCount();
//                    System.out.println("Total="+Total);
                }
                return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " super user list ", userList,Total);
           // }else{
              //  response.setContentType( MediaType.APPLICATION_JSON_VALUE);
               // response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
              //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
           // }
        } else {
            response.setContentType( MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
    /**
     * 
     * @param userId
     * @param userToken
     * @return details of user based on id
     */
    public Object getUser(int userId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (fieldSenseUtils.isUserValid(userId)) {
                    User user = new User();
                    user = userDao.selectUser(userId);
                    int parentTeamId=userDao.getParentTeamId(userId,accountId);
                    user.setParentId(parentTeamId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " user ", user);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid userId . Please try again . ", "", "");
                }   
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
    /**
     * @Added by - jyoti 22-june-2017
     * @param userId
     * @param userToken
     * @return 
     * @purpose - get id of users whose report_to = userId
     */
    public Object getReporterOfUser(int userId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            if (fieldSenseUtils.isUserValid(userId)) {
                List<Integer> listOFreporterOFUserID = userDao.getListOfReporterOfUserID(userId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " userIdList ", listOFreporterOFUserID);
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid userId . Please try again . ", "", "");
            }
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param userId
     * @param userToken
     * @return 
     * @purpose used to delete user based on id
     */
    public Object deleteUser(int userId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                if (fieldSenseUtils.isUserValid(userId)) {
                    int accountId = fieldSenseUtils.accountIdForToken(userToken);
                    if(fieldSenseUtils.isUserAdmin(userId) && fieldSenseUtils.totalAdminUserInAccount(accountId)<2){
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "There is no other Active Admin User. Before deleting this user please assign admin role to some other active user.", "", ""); 
                    }
                    if (teamDao.isUserInOrganizationChart(userId, accountId)) {
                        User user = userDao.deleteUser(userId);
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " User deleted successfully . ", " user ", user);
                    } else { 
                       // return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "User should be removed From Organization chart and then only he can be deleted.", "", "");
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "User can be deleted only when removed from the Reporting Structure.", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid userId . Please try again . ", "", "");
                }  
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

     /**
     * 
     * @param user
     * @param userToken
     * @return 
     * @purpose used to update user details 
     */
    public Object updateLastKnownDetails(User user, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                if (fieldSenseUtils.isUserValid(user.getId())) {
                    ArrayList<String> resultSet=new ArrayList<String>();
                    boolean isServerFetch=false;
                    //Added By Awaneesh for blank location
                    if(user.getLastKnownLocation().trim().equals("")){
                        //com.qlc.fieldsense.addressConverter.AddressConverter addConv = new com.qlc.fieldsense.addressConverter.AddressConverter();
                        //user.setLastKnownLocation(addConv.convertToAddressFromLatLong(String.valueOf(user.getLatitude()), String.valueOf(user.getLangitude())));

                         try {
                             CacheLocationManager cacheManager= new CacheLocationManager();
                             // nikhil ---- uncomment 
                             resultSet=cacheManager.getLocationFromLatLong(user.getLatitude(),user.getLangitude(),user.getId(),0);
                             String address=resultSet.get(0);
                           
                            // String address=cacheManager.getLocationFromLatLong(user.getLatitude(),user.getLangitude());
                             if(!address.equals("")){
                                 user.setLastKnownLocation(address);                          
                                 
                             }
                             isServerFetch=true;
                            /*GoogleResponse res = new AddressConverter().convertToAddressFromLatLong(String.valueOf(user.getLatitude()), String.valueOf(user.getLangitude()));
                             if (res.getStatus().equals("OK")) {
                                Result result = res.getResults()[0];
                                user.setLastKnownLocation(result.getFormatted_address());                          
                                isServerFetch=true;
                            }*/
                        } catch (Exception ex) {
                            log4jLog.info("updateLastKnownDetails" + ex);
//                            ex.printStackTrace();
                        }
                    }
                    //End By Awaneesh
                    if (userDao.updateUserLastKnownDetails(user)) {
                        int accountId = fieldSenseUtils.accountIdForToken(userToken);
                        UsersTravelLogs usersTravelLogs = new UsersTravelLogs();
                        usersTravelLogs.setUserId(user.getId());
                        usersTravelLogs.setLatitude(user.getLatitude());
                        usersTravelLogs.setLangitude(user.getLangitude());
                        usersTravelLogs.setLocation(user.getLastKnownLocation());
                        usersTravelLogs.setIsCustomerLocation(user.isIsCustomerLocation());
                        usersTravelLogs.setCustomerName(user.getCustomerName());
                        usersTravelLogs.setLocationIdentifier(user.getLocationIdentifier());
                        usersTravelLogs.setCreatedOn(user.getCreatedOn());
                        usersTravelLogs.setIsServerFetch(isServerFetch);
                        //
                        usersTravelLogs.setSourceValue(user.getSourceValue());
                        usersTravelLogs.setTravelDistance(user.getTravelDistance());
                        usersTravelLogs.setVersionCode(user.getVersionCode());
                        //
                        // added by jyoti
                        usersTravelLogs.setBattery_Percentage(user.getBattery_Percentage());
                        usersTravelLogs.setIsGPS(user.getIsGPS());
                        usersTravelLogs.setNetwork_type(user.getNetwork_type());
                        usersTravelLogs.setApp_version(user.getApp_version());
                        usersTravelLogs.setoS_Version(user.getoS_Version());
                        usersTravelLogs.setDevice_Name(user.getDevice_Name());
                        usersTravelLogs.setIsMockLocation(user.getIsMockLocation());
                        usersTravelLogs.setIsShowIntoReports(user.getIsShowIntoReports());
                        usersTravelLogs.setCustomerId(user.getCustomerId());
                        // ended by jyoti
                        usersTravelLogsDao.insertUsersTravelLog(usersTravelLogs, accountId);
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " User updated successfully . ", " user ", "");
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " updation failed . Please try again . ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid userId . Please try again . ", "", "");
                }  
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    
      /**
     * @param users
     * @param userToken
     * @return 
     * @purpose used to update user details 
     */
    public Object updateTravelLogsDetails(User[] users, String userToken) throws IOException {
//        System.out.println("inside updateTravelLogsDetails old api");
        if (fieldSenseUtils.isTokenValid(userToken)) {
            String address =null;
            ArrayList<String> resultSet=new ArrayList<String>();
            String[] BothAddress = null;
            String fullAdd=null;
            //String address=null;
            String created_date;
            int user_id;
            int status=3;
            CacheLocationManager cacheManager;
            List<UsersTravelLogs> listOfTravelLogs=new ArrayList<UsersTravelLogs>();
            List<UsersTravelLogs> listOfUnresolvedAddress=new ArrayList<UsersTravelLogs>();
            List<UsersTravelLogs> listResolvedAddress=new ArrayList<UsersTravelLogs>();
            int accountId = fieldSenseUtils.accountIdForToken(userToken); // doubtfull about the change maybe cause a bug.
//            System.out.println("Enter travel log");
                for(User user:users){
                    if (fieldSenseUtils.isUserValid(user.getId())) {
                       user_id=user.getId();
                        boolean isServerFetch=false;
                       
                        //Added By Awaneesh for blank location
                        if(user.getLastKnownLocation().trim().equals("")){
                            user_id = user.getId();
                            created_date = String.valueOf(user.getCreatedOn());
                            UsersTravelLogs usersTravelLogs = new UsersTravelLogs();
                            usersTravelLogs.setId(user.getId());
                            usersTravelLogs.setLatitude(user.getLatitude());
                            if(user.isIsCustomerLocation()){
                                  usersTravelLogs.setIsCustomerLocationInt(1);
                            }
                            if(isServerFetch){
                                 usersTravelLogs.setIsServerFetchInt(1);
                            }
                            usersTravelLogs.setLangitude(user.getLangitude());
                            usersTravelLogs.setLastKnownLocation(user.getLastKnownLocation());
                            usersTravelLogs.setCustomerName(user.getCustomerName());
                            usersTravelLogs.setLocationIdentifier(user.getLocationIdentifier());
                            usersTravelLogs.setCreatedOn(user.getCreatedOn());
                            //usersTravelLogs.setIsServerFetch(isServerFetch); 
                            usersTravelLogs.setVersionCode(user.getVersionCode());
//                          System.out.println("usersTravelLogs.getCreatedOn"+usersTravelLogs.getCreatedOn());
                            usersTravelLogs.setSourceValue(user.getSourceValue());
                            usersTravelLogs.setTravelDistance(user.getTravelDistance());
                            
                            // added by jyoti
                            usersTravelLogs.setBattery_Percentage(user.getBattery_Percentage());
                            usersTravelLogs.setIsGPS(user.getIsGPS());
                            usersTravelLogs.setNetwork_type(user.getNetwork_type());
                            usersTravelLogs.setApp_version(user.getApp_version());
                            usersTravelLogs.setoS_Version(user.getoS_Version());
                            usersTravelLogs.setDevice_Name(user.getDevice_Name());
                            usersTravelLogs.setIsMockLocation(user.getIsMockLocation());
                            usersTravelLogs.setIsShowIntoReports(user.getIsShowIntoReports());
                            usersTravelLogs.setCustomerId(user.getCustomerId());
                            // ended by jyoti
              
                            listOfUnresolvedAddress.add(usersTravelLogs);
//                            System.out.println("In if");
//                      
//                            //com.qlc.fieldsense.addressConverter.AddressConverter addConv = new com.qlc.fieldsense.addressConverter.AddressConverter();
//                            //user.setLastKnownLocation(addConv.convertToAddressFromLatLong(String.valueOf(user.getLatitude()), String.valueOf(user.getLangitude())));
//                            // GoogleResponse res1 = new AddressConverter().convertToAddressFromLatLong(String.valueOf( user.getLatitude()),String.valueOf(user.getLangitude()));
//                            
//                             try {
//                                  cacheManager= new CacheLocationManager();
//                                  resultSet=cacheManager.getLocationFromLatLong(user.getLatitude(),user.getLangitude(),user_id,accountId);
//                                  address=resultSet.get(0);
//                                  status=Integer.parseInt(resultSet.get(1));
//                                 if(!address.equals("")){
//                                     user.setLastKnownLocation(address);                                     
//                                 }
//                                 isServerFetch=true;
////                                  if(!shortAdd.equals("") ){
////                                    
////                                     user.setShortAddress(shortAdd);
////                                  
////                                 }
//                                /*GoogleResponse res = new AddressConverter().convertToAddressFromLatLong(String.valueOf(user.getLatitude()), String.valueOf(user.getLangitude()));
//                                 if (res.getStatus().equals("OK")) {
//                                    Result result = res.getResults()[0];
//                                    user.setLastKnownLocation(result.getFormatted_address());                          
//                                    isServerFetch=true;
//                                }*/
//                            } catch (Exception ex) {
//                                log4jLog.info("updateTravelLogsDetails" + ex);
////                                ex.printStackTrace();
//                            }
                        }else{
                         try{
                             UsersTravelLogs usersTravelLogs = new UsersTravelLogs();
                              usersTravelLogs.setLatitude(user.getLatitude());
                              usersTravelLogs.setLangitude(user.getLangitude());
                              usersTravelLogs.setLastKnownLocation(user.getLastKnownLocation());
                              listResolvedAddress.add(usersTravelLogs);
//                         int precision=Integer.parseInt(com.qlc.fieldsense.utils.Constant.CACHE_LOCATION_PRECISION);
//                         Double newLat=truncateDecimal(user.getLatitude(),precision).doubleValue();
//                         Double newLong=truncateDecimal(user.getLangitude(),precision).doubleValue();    
//                            CacheLocation cache =cachelocationDao.getShortLocationFromLatLong(newLat,newLong);
//                         if(cache==null){
//                             cache.setAddress(user.getLastKnownLocation());
//                            cache.setIsFetchFromGoogle(0);
//                            cache.setLatitude(newLat);
//                            cache.setLongitude(newLong);
//                            cache=cachelocationDao.createShortLocationInDB(cache);
//                            if(cache.getId()!=0) // id=0 means object is not created
//                            cachelocationDao.createEntryInShortStatsDB(cache); // create entry in stats DB
//                            cachelocationDao.createCacheStats(cache,3);// Added by jotiba the value 1 is for the value fetched from Google Api
//                            cache.setLatitude(user.getLatitude());
//                            cache.setLongitude(user.getLangitude());
//                            cache=cachelocationDao.createLocationInDB(cache);
//                        }
                             }catch(Exception e){
                            e.printStackTrace();
                            }
                        }
                        //End By Awaneesh
                        if (userDao.updateUserLastKnownDetails(user)) {
                            
                            user_id = user.getId();
                            created_date = String.valueOf(user.getCreatedOn());
                            UsersTravelLogs usersTravelLogs = new UsersTravelLogs();
                            usersTravelLogs.setId(user.getId());
                            usersTravelLogs.setLatitude(user.getLatitude());
                            if(user.isIsCustomerLocation()){
                                  usersTravelLogs.setIsCustomerLocationInt(1);
                            }
                            if(isServerFetch){
                                 usersTravelLogs.setIsServerFetchInt(1);
                            }
                            usersTravelLogs.setLangitude(user.getLangitude());
                            usersTravelLogs.setLastKnownLocation(user.getLastKnownLocation());
                            usersTravelLogs.setCustomerName(user.getCustomerName());
                            usersTravelLogs.setLocationIdentifier(user.getLocationIdentifier());
                            usersTravelLogs.setCreatedOn(user.getCreatedOn());
                            //usersTravelLogs.setIsServerFetch(isServerFetch); 
                            usersTravelLogs.setVersionCode(user.getVersionCode());
//                          System.out.println("usersTravelLogs.getCreatedOn"+usersTravelLogs.getCreatedOn());
                            usersTravelLogs.setSourceValue(user.getSourceValue());
                            usersTravelLogs.setTravelDistance(user.getTravelDistance());
                            Timestamp timeStamp=new Timestamp(System.currentTimeMillis());
                            //int locationId=usersTravelLogsDao.insertUsersTravelLog(usersTravelLogs, accountId);
                            
                            // added by jyoti
                            usersTravelLogs.setBattery_Percentage(user.getBattery_Percentage());
                            usersTravelLogs.setIsGPS(user.getIsGPS());
                            usersTravelLogs.setNetwork_type(user.getNetwork_type());
                            usersTravelLogs.setApp_version(user.getApp_version());
                            usersTravelLogs.setoS_Version(user.getoS_Version());
                            usersTravelLogs.setDevice_Name(user.getDevice_Name());
                            usersTravelLogs.setIsMockLocation(user.getIsMockLocation());
                            usersTravelLogs.setIsShowIntoReports(user.getIsShowIntoReports());
                            usersTravelLogs.setCustomerId(user.getCustomerId());
                            // ended by jyoti
                            
                            listOfTravelLogs.add(usersTravelLogs);
                            cacheManager= new CacheLocationManager();
                            cacheManager.insertDataInStats(timeStamp,status,0);
//                            if(usersTravelLogs.getLocation()=="" ||usersTravelLogs.getLocation().equals("")){
//                                usersTravelLogs.setId(user_id);
//                            if(usersTravelLogsDao.insertIntoLocationNotFound(usersTravelLogs, accountId)){
//                                System.out.println("Success");
//                            }else{
//                                System.out.println("Fail");
//                            }
//                            }
//                            if(locationId==0){
//                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " updation failed . Please try again . ", "", "");
//                            }
                          //  }                                                   
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " updation failed . Please try again . ", "", "");
                        }
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid userId . Please try again . ", "", "");
                    }
                }
//                System.out.println("Start Batch"+listOfTravelLogs.size());
                //userDao.updateUserLastKnownDetails(user)
                int locationNotFoundStatus=usersTravelLogsDao.insertIntoLocationNotFound(listOfUnresolvedAddress, accountId);
                usersTravelLogsDao.insertIntoCacheInsertBatch(listResolvedAddress, accountId);
                int locationId=usersTravelLogsDao.insertUsersTravelLogBatch(listOfTravelLogs, accountId);
//                System.out.println("batch End");
                if(locationId==0 && locationNotFoundStatus==0){
                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " updation failed . Please try again . ", "", "");
                            }
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " User updated successfully . ", " user ", "");
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }        
    
     /**
     * 
     * @param users
     * @param userToken
     * @return 
     * @purpose used to update user details 
     */
    @Deprecated
    public Object updateMultiUsersLastKnownDetails(UserOld[] users, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                for(UserOld user:users){
                    if (fieldSenseUtils.isUserValid(user.getId())) {
                        ArrayList<String> resultSet=new ArrayList<String>();
                        boolean isServerFetch=false;
                        //Added By Awaneesh for blank location
                        if(user.getLastKnownLocation().trim().equals("")){
                            //com.qlc.fieldsense.addressConverter.AddressConverter addConv = new com.qlc.fieldsense.addressConverter.AddressConverter();
                            //user.setLastKnownLocation(addConv.convertToAddressFromLatLong(String.valueOf(user.getLatitude()), String.valueOf(user.getLangitude())));

                             try {
                                 CacheLocationManager cacheManager= new CacheLocationManager();
                                 resultSet=cacheManager.getLocationFromLatLong(user.getLatitude(),user.getLangitude(),user.getId(),0);
                                 String address=resultSet.get(0);
                                 if(!address.equals("")){
                                     user.setLastKnownLocation(address);                          
                                     isServerFetch=true;
                                 }
                                /*GoogleResponse res = new AddressConverter().convertToAddressFromLatLong(String.valueOf(user.getLatitude()), String.valueOf(user.getLangitude()));
                                 if (res.getStatus().equals("OK")) {
                                    Result result = res.getResults()[0];
                                    user.setLastKnownLocation(result.getFormatted_address());                          
                                    isServerFetch=true;
                                }*/
                            } catch (Exception ex) {
                                log4jLog.info("updateMultiUsersLastKnownDetails" + ex);
//                                ex.printStackTrace();
                            }
                        }
                        //End By Awaneesh
                        if (userDao.updateUserLastKnownDetails(user)) {
                            int accountId = fieldSenseUtils.accountIdForToken(userToken);
                            int user_id = user.getId();
                            String created_date = String.valueOf(user.getCreatedOn());
                            UsersTravelLogs usersTravelLogs = new UsersTravelLogs();
                            usersTravelLogs.setUserId(user.getId());
                            usersTravelLogs.setLatitude(user.getLatitude());
                            usersTravelLogs.setLangitude(user.getLangitude());
                            usersTravelLogs.setLocation(user.getLastKnownLocation());
                            usersTravelLogs.setIsCustomerLocation(user.isIsCustomerLocation());
                            usersTravelLogs.setCustomerName(user.getCustomerName());
                            usersTravelLogs.setLocationIdentifier(user.getLocationIdentifier());
                            usersTravelLogs.setCreatedOn(user.getCreatedOn());
                            usersTravelLogs.setIsServerFetch(isServerFetch);
                            usersTravelLogs.setVersionCode(user.getVersionCode());
                            //
                            usersTravelLogs.setSourceValue(user.getSourceValue());
                            usersTravelLogs.setTravelDistance(user.getTravelDistance());
                            //
                            
                            
                            
                            if(usersTravelLogsDao.insertUsersTravelLog(usersTravelLogs, accountId)==0){
                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " updation failed . Please try again . ", "", "");
                            }
                          //  }                                                   
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " updation failed . Please try again . ", "", "");
                        }
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid userId . Please try again . ", "", "");
                    }
                }
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " User updated successfully . ", " user ", "");
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }        
    
     /**
     * 
     * @param file
     * @param userToken
     * @author:anuja
     * @return 
     * @purpose:import user
     */
    public Object insertUserByImport(MultipartFile file, String userToken) {
        
        log4jLog.info("insertUserByImport");
        try {
            TeamManager teamManager = new TeamManager();
//            String validStatus = Constant.VALID;
            String validStatus = "";
            List<Integer> userList = new ArrayList<Integer>();
            List<String> userEmailList = new ArrayList<String>();
            List<String[]> userErrorList = new ArrayList<String[]>();
            int userId = fieldSenseUtils.userIdForToken(userToken);
            if (fieldSenseUtils.isTokenValid(userToken)) {
                    int accountId = fieldSenseUtils.accountIdForToken(userToken);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss_SSS");
                    java.util.Date d = new java.util.Date();
                    String date = dateFormat.format(d);
                    String saveFileTo = Constant.IMPORT_ERROR_FILES;
                    String errorFileNm = accountId + "_" + date + "_UserImportError.csv";
                    String csv = saveFileTo + errorFileNm;
                    int userLimitForAccount = fieldSenseUtils.userLimitForAccount(accountId);
                    int totalUsersInAcount = fieldSenseUtils.totalUsersInAccount(accountId);
                    if (totalUsersInAcount <= userLimitForAccount) {
                        if (fieldSenseUtils.isUserAdmin(userId)) {                             
                            List<UserCSV> list = MapCSVToUser.mapJavaBean(file);
                            log4jLog.info("insertUserByImport 1");
                            for (int i = 0; i < 1; i++) {
                                 log4jLog.info("insertUserByImport 2");
                                 if ((list.get(i).getEmp_code().equalsIgnoreCase("Emp. Code"))) { //modified emp_code by manohar
                                if ((list.get(i).getFirstName().equalsIgnoreCase("First Name")) && (list.get(i).getLastName().equalsIgnoreCase("Last Name"))) {
                                    if ((list.get(i).getDesignation().equalsIgnoreCase("Designation")) && (list.get(i).getPassword().equalsIgnoreCase("Password"))) {
                                        if ((list.get(i).getMobileNo().equalsIgnoreCase("Mobile No")) && (list.get(i).getGender().equalsIgnoreCase("Gender"))) {
                                            if ((list.get(i).getActive().equalsIgnoreCase("Active/Inactive")) && (list.get(i).getAllowTimeout().toLowerCase().startsWith("allow timeout")) &&  (list.get(i).getReportingHead().equalsIgnoreCase("ReportingHead")&&  (list.get(i).getRole().startsWith("Role")))) {
                                                if (list.get(i).getEmailAddress().equalsIgnoreCase("EmailAddress") /*&& (list.get(i).getuserAccuracy().equalsIgnoreCase("userAccuracy"))*/) {
                                                } else {                                                    
                                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "CSV file not in correct format. Please download the sample file1.", "", "");
                                                }
                                            } else {
                                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "CSV file not in correct format. Please download the sample file2.", "", "");
                                            }
                                        } else {
                                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "CSV file not in correct format. Please download the sample file3.", "", "");
                                        }
                                    } else {
                                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "CSV file not in correct format. Please download the sample file4.", "", "");
                                    }
                                } else {
                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "CSV file not in correct format. Please download the sample file5.", "", "");
                                }
                            }
                            }
                            log4jLog.info("insertUserByImport 3");     //modified emp_code by manohar
                            userErrorList.add(new String[]{"Emp. Code","First Name", "Last Name", "Designation", "Password", "Mobile No", "Gender", "Active/Inactive","Allow Timeout(Yes/No)","Role(Admin,on-field,accounts)", "ReportingHead", "EmailAddress"/*, "userAccuracy"*/});
                            int totalAllowedUsers = userLimitForAccount - totalUsersInAcount;
                            int listSize = list.size();
                            int length = 0;
                            if (totalAllowedUsers < listSize - 1) {
                                length = totalAllowedUsers;
                                validStatus = "Not enough space.";
                            } else {
                                length = listSize - 1;
                            }
                            for (int i = 1; i <= length; i++) {
                                User user = new User();
                                String validStatus2 = Constant.VALID;
                                int accountIdForEmpCode= fieldSenseUtils.accountIdForToken(userToken);     //added by manohar
                                if (!(fieldSenseUtils.isEmailUnique(list.get(i).getEmailAddress()))) {
                                    if (fieldSenseUtils.isMobileExist(list.get(i).getMobileNo())==0) {       //added by manohar
                                         if (list.get(i).getEmp_code().equals("")  || fieldSenseUtils.isEmpCodeExist(list.get(i).getEmp_code(),accountIdForEmpCode) == 0) {
                                        if ((list.get(i).getActive().equals("")) || (list.get(i).getActive().equals(null))) {                                           
                                        } else if (list.get(i).getActive().trim().equalsIgnoreCase("active")) {
                                            user.setActive(true);
                                        } else if (list.get(i).getActive().equalsIgnoreCase("inactive")) {
                                            user.setActive(false);
                                        } else {
                                            validStatus = FieldSenseUtils.notvalid(validStatus);
                                            validStatus = validStatus + " Invalid active field at line:" + (i + 1) + ".";
                                            validStatus2 = validStatus;
                                        }

                                        if (list.get(i).getGender().equalsIgnoreCase("female")) {
                                            user.setGender(0);
                                        } else if (list.get(i).getGender().equalsIgnoreCase("male")) {
                                            user.setGender(1);
                                        } else {
                                            validStatus = FieldSenseUtils.notvalid(validStatus);
                                            validStatus = validStatus + "Invalid gender at line:" + (i + 1) + ".";
                                            validStatus2 = validStatus;
                                        }
                                        
                                        if(list.get(i).getAllowTimeout().trim().equalsIgnoreCase("yes")){
                                            user.setAllowTimeout(1);
                                        }else{
                                            user.setAllowTimeout(0);
                                        }
                                        
                                        if ((list.get(i).getRole().equalsIgnoreCase("admin")) || (list.get(i).getRole().equals("AD"))) {
                                            user.setRole(1);
                                        } /*else if ((list.get(i).getRole().equalsIgnoreCase("teamleader")) || (list.get(i).getRole().equals("TL"))) {
                                        user.setRole(2);
                                        } else if ((list.get(i).getRole().equalsIgnoreCase("teammember")) || (list.get(i).getRole().equals("TM"))) {
                                        user.setRole(3);
                                        } else if ((list.get(i).getRole().equalsIgnoreCase("in-office"))) {
                                        user.setRole(4);
                                        } */ else if ((list.get(i).getRole().equalsIgnoreCase("on-field"))) {
                                            user.setRole(5);
                                        }else if ((list.get(i).getRole().equalsIgnoreCase("accounts"))) {
                                            user.setRole(2);
                                        } else {
                                            validStatus = FieldSenseUtils.notvalid(validStatus);
                                            validStatus = validStatus + "Invalid role at line:" + (i + 1) + ".";
                                            validStatus2 = validStatus;
                                        }

//                                        if ((list.get(i).getFirstName().equals("")) || !(FieldSenseUtils.isContainOnlyCharacters(list.get(i).getFirstName()))) {
//                                            validStatus = FieldSenseUtils.notvalid(validStatus);
//                                            validStatus = validStatus + "Invalid first name at line:" + (i + 1) + ".";
//                                            validStatus2 = validStatus;
//                                        }
//                                        if ((list.get(i).getLastName().equals("")) || !(FieldSenseUtils.isContainOnlyCharacters(list.get(i).getLastName()))) {
//                                            validStatus = FieldSenseUtils.notvalid(validStatus);
//                                            validStatus = validStatus + "Invalid last name at line:" + (i + 1) + ".";
//                                            validStatus2 = validStatus;
//                                        }
////                                        
                                        if ((list.get(i).getFirstName().equals("")) || (FieldSenseUtils.isname(list.get(i).getFirstName()))) {
                                            validStatus = FieldSenseUtils.notvalid(validStatus);
                                            validStatus = validStatus + "Invalid first name at line:" + (i + 1) + ".";
                                            validStatus2 = validStatus;
                                        }
                                        if ((list.get(i).getLastName().equals("")) || (FieldSenseUtils.isname(list.get(i).getLastName()))) {
                                            validStatus = FieldSenseUtils.notvalid(validStatus);
                                            validStatus = validStatus + "Invalid last name at line:" + (i + 1) + ".";
                                            validStatus2 = validStatus;
                                        }
//                                      
                                        if ((list.get(i).getMobileNo().equals("")) || !(FieldSenseUtils.isContainOnlyNumbers(list.get(i).getMobileNo().replaceAll("\\s+","")))) {
                                            validStatus = FieldSenseUtils.notvalid(validStatus);
                                            validStatus = validStatus + "Invalid mobile number at line:" + (i + 1) + ".";
                                            validStatus2 = validStatus;
                                        }                                    
                                       
//                                        System.out.println("list.get(i).getEmailAddress().trim()"+list.get(i).getEmailAddress().trim());
                                        if ((list.get(i).getEmailAddress().equals("")) || !(FieldSenseUtils.isValidEmailAddress(list.get(i).getEmailAddress().trim()))) {
                                            validStatus = FieldSenseUtils.notvalid(validStatus);
                                            validStatus = validStatus + "Invalid email address at line:" + (i + 1) + ".";
                                            validStatus2 = validStatus;
                                        }
                                        /*if (!(FieldSenseUtils.isDomainValid(list.get(i).getEmailAddress()))) {
                                            validStatus = FieldSenseUtils.notvalid(validStatus);
                                            validStatus = validStatus + "Public domain is not allowed at line:" + (i + 1) + ".";
                                            validStatus2 = validStatus;
                                        }*/
                                        if ((list.get(i).getPassword().equals("")) || !(FieldSenseUtils.isPasswordValid(list.get(i).getPassword()))) {
                                            validStatus = FieldSenseUtils.notvalid(validStatus);
                                            validStatus = validStatus + "Password should contains alphabets numbers and !@#$ and it must be 6 character long at line:" + (i + 1) + ".";
                                            validStatus2 = validStatus;
                                        }
                                        if ((list.get(i).getDesignation().equals("")) || !(FieldSenseUtils.isContainOnlyCharacters(list.get(i).getDesignation()))) {
        //                                    validStatus = FieldSenseUtils.notvalid(validStatus);
        //                                    validStatus = validStatus + "Invalid designation at line:" + (i + 1) + ".";
        //                                    validStatus2 = validStatus;
                                        }
                                         if ((list.get(i).getReportingHead().equals("")) || !(fieldSenseUtils.isReportingHeadValid(list.get(i).getReportingHead(),accountId))) {
                                            // int organisationHeadId= fieldSenseUtils.getOrganisationHeadId(accountId);
                                             user.setParentId(100000); 
                                             user.setReport_to(userDao.getTopUserInHierarachy(accountId));
                                         }
                                         else{
                                              String reportingHead=list.get(i).getReportingHead();
                                              int head_id=fieldSenseUtils.getUserIdFrmFullName(reportingHead);
                                              int parentTeamId=fieldSenseUtils.selectUsersTeamId(head_id,accountId);

                                              int headRole =fieldSenseUtils.roleOfUser(head_id);
                                              int userRole=user.getRole();// since role already set
                                              boolean validReportTo=false;
                                              if(headRole==1 || headRole==userRole){
                                                  validReportTo=true;
                                              }
                                              if(head_id==0 || parentTeamId==0 || validReportTo==false){
                                                  user.setParentId(100000); 
                                                  user.setReport_to(userDao.getTopUserInHierarachy(accountId));
                                              }else{
                                                user.setParentId(parentTeamId);
                                                user.setReport_to(head_id);
                                              }
                                         }
                                         
                                         /* if ((list.get(i).getuserAccuracy().equals("")) || !(FieldSenseUtils.isContainOnlyNumbers(list.get(i).getuserAccuracy())) || ((Integer.parseInt(list.get(i).getuserAccuracy())) <= 0)) {
                                            log4jLog.info("insertUserByImport 4");
                                            validStatus = FieldSenseUtils.notvalid(validStatus);
                                            validStatus = validStatus + "Invalid Accuracy at line:" + (i + 1) + ".";
                                            validStatus2 = validStatus;
                                            log4jLog.info("insertUserByImport after 4");
                                        }
                             
                                         log4jLog.info("insertUserByImport 6 validStatus2 "+validStatus2);*/
                                        if (!(validStatus2.equals(Constant.VALID))) {
                                            log4jLog.info("insertUserByImport 7");    //modified emp_code by manohar
                                            userErrorList.add(new String[]{list.get(i).getEmp_code(),list.get(i).getFirstName(), list.get(i).getLastName(), list.get(i).getDesignation(), list.get(i).getPassword(), list.get(i).getMobileNo(), list.get(i).getGender(), list.get(i).getActive(), list.get(i).getAllowTimeout(), list.get(i).getRole(), list.get(i).getReportingHead(),list.get(i).getEmailAddress()/*,list.get(i).getuserAccuracy()*/}); // modified by manohar for error file
                                            continue;
                                        }
                                        if (validStatus2.equals(Constant.VALID)) {
                                            user.setFirstName(list.get(i).getFirstName());
                                            user.setLastName(list.get(i).getLastName());
                                            user.setEmailAddress(list.get(i).getEmailAddress().trim());
                                            user.setPassword(list.get(i).getPassword());
                                            user.setMobileNo(list.get(i).getMobileNo().replaceAll("\\s+",""));
                                            user.setEmp_code(list.get(i).getEmp_code());  //added by manohar
                                            
                                            //Added by Mayank Ramaiya
                                            user.setUserAccuracy(500);  //default value for an accuracy
                                            user.setCheckInRadius(500); //default value for an check-in radius
                                            //End by Mayank Ramaiya                                                                               
                                            
                                            user.setAccountId(accountId);
                                            String location = fieldSenseUtils.getCity(accountId);
                                            user.setLastKnownLocation(location);
                                            user.setCreatedBy(userId);
                                            user.setPassword(FieldSensePasswordEncryptionDecryption.hashPassword(user.getPassword()));
                                            user.setDesignation(list.get(i).getDesignation());          
                                            int userId2 = userDao.insertUser(user);
                                            // Added by Jyoti 30-12-2016
                                            try{
                                                int userTerritoryId = userDao.insertUserTerritoryUnknownForCSVFile(userId2, accountId);                                                
                                            }catch(Exception e){
                                                e.printStackTrace();
                                                log4jLog.info("insertUserByImport_Territory"+ e);
                                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Could not add default territory. Please try again . ", "", "");
                                            }                                            
                                            // Ended by Jyoti
                                            if (userId2 != 0) {                                                
                                                
                                                try{
                                                    // Added By jyoti, 21-06-2017
                                                    String bgImagePath = Constant.PROFILEPIC_UPLOAD_PATH + "FM.png";
                                                    MapsIconCreate mapsIconCreate = new MapsIconCreate();
                                                    BufferedImage mapMarker = mapsIconCreate.readImage(bgImagePath);
                                                    mapsIconCreate.writeImage(mapMarker, Constant.PROFILEPIC_UPLOAD_PATH + accountId + "_" + userId2 + "_mm.png", "PNG");

                                                    // for sending email notification
                                                    String userName = user.getFirstName() + " " + user.getLastName();
                                                    EmailNotification emailNotification = new EmailNotification();
                                                    emailNotification.registerUser(userName, user.getEmailAddress());

                                                    // for sending sms notification
//                                                    if (user.isMobileNotification()) {
                                                        MessageNotifications messageNotifications = new MessageNotifications();
                                                        String adminFirstName = fieldSenseUtils.getUserFirstName(fieldSenseUtils.userIdForToken(userToken));
                                                        String adminLastName = fieldSenseUtils.getUserlastName(fieldSenseUtils.userIdForToken(userToken));
                                                        messageNotifications.createUser(user.getFirstName(), user.getLastName(), adminFirstName, adminLastName, user.getMobileNo());
//                                                    }
                                                
                                                    // for creating hierarchy of the user
                                                    //create Team for it --> start
                                                    user.setId(userId2);
                                                    Team team=new Team();
                                                    team.setTeamName("Hirarchy");
                                                    team.setDescription("Hirarchy");
                                                    team.setOwnerId(user);
                                                    team.setIsActive(1);

                                                    User createdBy= new User();
                                                    createdBy.setId(userId);                                            
                                                    team.setCreatedBy(createdBy);
                                                    int parentId=fieldSenseUtils.selectUsersTeamId(user.getReport_to(),accountId);
                                                    if(parentId==0){
                                                        parentId=100000;
                                                    }
                                                    teamManager.addMemberToOrganizationChart(team,parentId , userToken);
                                                    //create Team for it --> end
                                                    
                                                    userList.add(userId2);
                                                    userEmailList.add(user.getEmailAddress()); 
                                                    
                                                    // For creating entry in user_keys table, which display the status of user i.e. inMeeting, can take call etc
                                                    List<String> sqlQueriesList = userDao.getCreateUserDeafultQueries();
                                                    if (!(sqlQueriesList == null || sqlQueriesList.isEmpty())) {
                                                        userDao.executeCreateUserDeafultQueries(sqlQueriesList, userId2, accountId);
                                                    }
                                                    
                                                    if (accountId != 0) {
                                                        List<String> accountSqlQueriesList = accountRegistrationDao.getCreateAccountDeafultQueries();
                                                        if (!(accountSqlQueriesList == null || accountSqlQueriesList.isEmpty())) {
                                                            accountRegistrationDao.executeAccountUserDeafultQueries(accountSqlQueriesList, userId2, accountId);
                                                        }
                                                    }
                                            
                                                } catch(Exception e){
                                                    e.printStackTrace();
                                                    log4jLog.info("import_user_sending_sms_email_adding_team_>>> "+e);
                                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " import user - sending sms, email, adding team  >>> ", "", e);
                                                }
                                                // Ended By jyoti, 21-06-2017                                                                                            
                                            }
                                        }
                                    } else{    //added by manohar
                                            validStatus = validStatus + " The emp code at line " + (i + 1) + " is already existed.";
                                        }
                                    } else {
                                    validStatus = validStatus + " The mobile number at line " + (i + 1) + " is already existed.";
                                    }    
                                } else {
                                    validStatus = validStatus + " The email address at line " + (i + 1) + " is already existed.";
                                }
                            }
                            
                            log4jLog.info("insertUserByImport_userErrorList"+userErrorList);
                            if ((userErrorList.size() > 1)) {
                                log4jLog.info("insertUserByImport 8");
                                CSVWriter writer = new CSVWriter(new FileWriter(csv));
                                writer.writeAll(userErrorList);
        //                        System.out.println("CSV written successfully.");
                                writer.close();
                            }
                            int totalCreated = userEmailList.size();
                            if ((userList.size() > 0) && validStatus.equals("")) {
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.USER_CREATED_SUCCESFULLY, " userEmailList ", userEmailList);
                            } else if ((userList.size() > 0) && !(validStatus.equals(Constant.VALID))) {
                                if ((userErrorList.size() > 1))
                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, totalCreated + " Users got added successfully." + validStatus, " ErrorFileName .", errorFileNm);
                                else
                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, totalCreated + " Users got added successfully." + validStatus, "No Error File", "Error file not generated");                                
                            } else {
                                if ((userErrorList.size() > 1))
                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, validStatus + " User not added . Please try again .", "ErrorFileName", errorFileNm);
                                else
                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, validStatus + " User not added . Please try again .", "No Error File", "Error file not generated");

                            }
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " You are not authorised to create user ", "", "");
                        }
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "You exceeds the user limit", "", "");
                    }   
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
            }
        } catch (NullPointerException e) {
            log4jLog.info("insertUserByImport e "+e);
            e.printStackTrace();
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Designation may not be empty. Please try again . ", "", "");
        } catch (FileNotFoundException ex) {
            log4jLog.info("insertUserByImport ex "+ex);
            ex.printStackTrace();
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "File not uploaded. Please try again . ", "", "");
        } catch (IOException ex) {
            log4jLog.info("insertUserByImport_IOException "+ex);
        }
        return null;
    }

     /**
     * 
     * @param user
     * @param userToken
     * @author:anuja
     * @return 
     * @purpose:update user
     */
    public Object updateUserDetails(User user, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
//        if (memCachedManager.isTokenValid(userToken)) { // added by jyoti
                if (fieldSenseUtils.isUserValid(user.getId())) {
                    int accountId= fieldSenseUtils.accountIdForToken(userToken);
                    if(fieldSenseUtils.isUserAdmin(user.getId()) && fieldSenseUtils.totalAdminUserInAccount(accountId)<1 && user.getRole() !=1){
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "There is no other Admin , before updating this user assign admin role to some other user.", "", ""); 
                    }
                    if ((fieldSenseUtils.isMobileExist(user.getMobileNo())==0) || ((fieldSenseUtils.getUserEmailId(user.getMobileNo()).trim().equals(user.getEmailAddress().trim())) && (fieldSenseUtils.isMobileExist(user.getMobileNo())==1))) {
                        if( user.getEmp_code().equals("")  || ((fieldSenseUtils.getUserEmpCode(user.getEmp_code(),accountId).trim().equals(user.getEmailAddress().trim())) && fieldSenseUtils.isEmpCodeExist(user.getEmp_code(),accountId)==1) || (fieldSenseUtils.isEmpCodeExist(user.getEmp_code(),accountId)==0)){ // modified by manohar
                        int userA = fieldSenseUtils.userIdForToken(userToken);
                        if (fieldSenseUtils.isUserAdmin(userA) || userA==1) {
                            String password = authenticationUserDao.getUserPassword(user.getEmailAddress());
                            if (!password.equals(user.getPassword())) {
                                user.setPassword(FieldSensePasswordEncryptionDecryption.hashPassword(user.getPassword()));
                            }
                            
                            User userFromDb= userDao.selectUser(userA);
                            int earlierReportTo=userFromDb.getReport_to();
                            int reportTo = user.getReport_to();
                          //added by nikhil 23rd june
                            if(user.getInterval().equalsIgnoreCase("Default")){
                                user.setInterval("0");
                            }
                            // ended by nikhil
                            if (userDao.updateUserDetails(user)) {
                                try{
                                    userDao.insertUserTerritories(user.getAddTerritoryList(),user.getDeleteTerritoryList(),user.getId(),accountId);
                                }catch(Exception e){
                                    log4jLog.info("updateUserDetails e "+e);
//                                    e.printStackTrace();
                                }
                                //email send code 
                                if(earlierReportTo != reportTo ){
                                    updateReportToOfUser(user,userFromDb,accountId,userToken);
                                }
                                if (user.isEmailNotification()) {
                                    String userName = user.getFirstName() + " " + user.getLastName();
                                    EmailNotification emailNotification = new EmailNotification();
                                    emailNotification.registerUser(userName, user.getEmailAddress());
                                }
                                //Message send code
                                if (user.isMobileNotification()) {
                                    MessageNotifications messageNotifications = new MessageNotifications();
                                    String adminFirstName = fieldSenseUtils.getUserFirstName(fieldSenseUtils.userIdForToken(userToken));
                                    String adminLastName = fieldSenseUtils.getUserlastName(fieldSenseUtils.userIdForToken(userToken));
                                    messageNotifications.updateUserMessage(user.getFirstName(), user.getLastName(), adminFirstName, adminLastName, user.getMobileNo());
                                }
                                // Added by Jyoti, 21-12-2017 - for push notification
                                java.util.HashMap gcmInfo = fieldSenseUtils.getGcmUserInfo(user.getId());
                                String gcmId = (String) gcmInfo.get("gcmId");
                                int deviceOS = (Integer) gcmInfo.get("deviceOS");
                                int appVersion = (Integer) gcmInfo.get("appVersion");
//                                System.out.println("updateUserDetails, appVersion : "+appVersion + " , if > "+Integer.parseInt(Constant.APP_VERSION_CHECK));
                                if(appVersion > Integer.parseInt(Constant.APP_VERSION_CHECK)){
                                    if(!(gcmId.equals("0") || gcmId.isEmpty())) {
                                        Map<String, Object> m_data = new HashMap<String, Object>();
                                        m_data.put("type", "user");
                                        m_data.put("message", "");
                                        m_data.put("id", user.getId());
                                        m_data.put("userId", user.getId());
                                        m_data.put("teamId", reportTo);
                                        m_data.put("addTerritoryList", user.getAddTerritoryList());
                                        m_data.put("deleteTerritoryList", user.getDeleteTerritoryList());
        //                                m_data.put("userToken", userToken); // amit told to add this field, commented to avoid the issue of auto logout.
                                        m_data.put("userFirstName", user.getFirstName()); 
                                        m_data.put("userLastName", user.getLastName());
                                        m_data.put("designation", user.getDesignation());
                                        m_data.put("gender", user.getGender());
                                        m_data.put("role", user.getRole());
                                        m_data.put("userEmailAddress", user.getEmailAddress());
                                        m_data.put("mobileNo", user.getMobileNo());
                                        m_data.put("imsgrRetivalPath", FieldSenseUtils.getPropertyValue("PROFILEPIC_GET_PATH"));
                                        m_data.put("accountId", accountId); 
                                        m_data.put("createdOn", user.getCreatedOn()); 
                                        m_data.put("latitude", user.getLatitude()); 
                                        m_data.put("longitude", user.getLangitude()); 
                                        m_data.put("userDesignation", user.getDesignation());
                                        m_data.put("allowOfflineOperation", user.getAllowOffline()); 
                                        m_data.put("allowTimeout", user.getAllowTimeout()); 
                                        m_data.put("checkInRadius", user.getCheckInRadius()); 
                                        m_data.put("userAccuracy", user.getUserAccuracy());
                                        m_data.put("modifiedOn", user.getUpdatedOn());
                                        m_data.put("locationPollInterval", 0);
                                        m_data.put("officelatitude", 0);
                                        m_data.put("officelangitude", 0);
                                        if(user.getCreatedOn().before(user.getUpdatedOn())) {
                                            m_data.put("isModified", "true");
                                        } else {
                                            m_data.put("isModified", "false");
                                        }
                                        com.qlc.fieldsense.utils.PushNotificationManager push= new com.qlc.fieldsense.utils.PushNotificationManager();
                                        push.addEditNotification(m_data, gcmId, deviceOS,null); // Modified by jyoti, 11-01-2018
                                    }
                                }
                                // Ended by Jyoti
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " User Data Updated Successfully . ", " user ", "");
                            } else {
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " updation failed . Please try again . ", "", "");
                            }
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " You are not authorised to create user ", "", "");
                        }
                         
                    }else {        //added by manohar
                             return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "User already exists with the same Emp Code. Please try with different emp code .", "", "");                      
                            }
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "User already exists with the same mobile number. Please try with different mobile number . ", "", "");
                    }    
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid userId . Please try again . ", "", "");
                }   
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param userToken
     * @author: anuja
     * @return 
     * @purpose: users which are not in any team
     */
    public Object selectUsersExceptTeamMembers(String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<User> userList = userDao.selectUsersExceptTeamMembers(accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " user list ", userList);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * @author Ramesh
     * @return 
     * @date 28-06-2014
     * @param user
     * @param userToken
     * @purpose used to update user
     */
    public Object updateUserDetailsForUser(User user, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                if (fieldSenseUtils.isUserValid(user.getId())) {
                     if ((fieldSenseUtils.isMobileExist(user.getMobileNo())==0) || ((fieldSenseUtils.getUserEmailId(user.getMobileNo()).trim().equals(user.getEmailAddress().trim())) && (fieldSenseUtils.isMobileExist(user.getMobileNo())==1))) {
                        if (userDao.updateUserDetailsForUser(user)) {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " User updated successfully . ", " user ", "");
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " updation failed . Please try again . ", "", "");
                        }
                    } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "User already exists with the same mobile number. Please try with different mobile number", "", "");
                    }    
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid userId . Please try again . ", "", "");
                }  
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param user
     * @param userToken
     * @author: anuja
     * @return 
     * @purpose: update home and office latlong
     */
    public Object updateHomeOfficeLocation(User user, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                if (fieldSenseUtils.isUserValid(user.getId())) {
                    if (user.getType() == 1) {
                        if (userDao.updateHomeLatlong(user)) {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Home latlong updated successfully . ", "", "");
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " updation failed . Please try again . ", "", "");
                        }
                    } else if (user.getType() == 2) {
                        if (userDao.updateOfficeLatlong(user)) {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Office latlong updated successfully . ", "", "");
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " updation failed . Please try again . ", "", "");
                        }
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid userId . Please try again . ", "", "");
                }    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
        return null;
    }

    /**
     * 
     * @param userToken
     * @return 
     * @purpose Used to get list of all TL (i.e. role=2).
     */
    public Object getTeamLeaders(String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<User> teamLeadersList = userDao.selectTeamLeaders(accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " Team leaders list ", teamLeadersList);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

     /**
     * 
     * @param userToken
     * @return 
     * @purpose Used to get list of all active non super admin users
     */
    public Object getUsersForMobile(String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<TeamMember> userList = userDao.slectAllUsersForMobile(accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " user list ", userList);      
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param user
     * @param userToken
     * @param OnlineCreation
     * @return newly added account user details
     */
    public boolean createNewUserForNewAccount(User user, String userToken , boolean OnlineCreation) {
        user.setCreatedBy(fieldSenseUtils.userIdForToken(userToken));
        user.setPassword(FieldSensePasswordEncryptionDecryption.hashPassword(user.getPassword()));
        int userId = userDao.insertUser(user);
        if (userId != 0) {                          // max id
           int accountId = user.getAccountId();
            //default map marker for user .
            try{
                String bgImagePath = Constant.PROFILEPIC_UPLOAD_PATH + "FM.png";
                MapsIconCreate mapsIconCreate = new MapsIconCreate();
                BufferedImage mapMarker = mapsIconCreate.readImage(bgImagePath);
                mapsIconCreate.writeImage(mapMarker, Constant.PROFILEPIC_UPLOAD_PATH + accountId + "_" + userId + "_mm.png", "PNG");
            }catch (Exception e) {
//                e.printStackTrace();
            }
            //email sending code.
            String userName = user.getFirstName() + " " + user.getLastName();
            EmailNotification emailNotification = new EmailNotification();
            emailNotification.createAccount(userName, user.getEmailAddress());
            if(!OnlineCreation)
            {
//                System.out.println("reset password");
            emailNotification.registerUser(userName, user.getEmailAddress());
            }
            //user = userDao.seleuserctUser(userId);
            //mobile notification
           // String adminFirstName = fieldSenseUtils.getUserFirstName(fieldSenseUtils.userIdForToken(userToken));
           // String adminLastName = fieldSenseUtils.getUserlastName(fieldSenseUtils.userIdForToken(userToken));
            if(!user.getMobileNo().trim().equals("")){
                MessageNotifications messageNotifications = new MessageNotifications();
                messageNotifications.createUser(user.getMobileNo());
            }
            //
            List<String> sqlQueriesList = userDao.getCreateUserDeafultQueries();
            if (!(sqlQueriesList == null || sqlQueriesList.isEmpty())) {
                userDao.executeCreateUserDeafultQueries(sqlQueriesList, userId, accountId);
            }
            List<String> accountSqlQueriesList = accountRegistrationDao.getCreateAccountDeafultQueries();
            if (!(accountSqlQueriesList == null || accountSqlQueriesList.isEmpty())) {
                accountRegistrationDao.executeAccountUserDeafultQueries(accountSqlQueriesList, userId, accountId);
            }
            //Added by manohar ticket no:-
            userDao.insertUserTerritoryUnknownForCSVFile(userId, accountId);   // max id from user_territory    (3593,753)
        } else {
            return false;
        }
          return true;
    }

    /**
     *
     * @param userToken
     * @return
     */
    public Object getUsersForReportTo(String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            int accountId = fieldSenseUtils.accountIdForToken(userToken);
            int topId= userDao.getTopUserInHierarachy(accountId);

            List<HashMap> userList = userDao.slectAllUsersForReportTo(accountId,topId);
            
            ArrayList reportToList = new ArrayList();
            reportToList.add(topId);
            reportToList.add(userList);
            
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " user list ", reportToList);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
    private  boolean insertTeamForNewUser(User user,int accountId,String userToken){
        Team team= new Team();
        team.setTeamName("Hirarchy");
        team.setDescription("Hirarchy");
        team.setOwnerId(user);
        team.setIsActive(1);
        
        User createdBy = new User();
        createdBy.setId(user.getCreatedBy());
        
        team.setCreatedBy(createdBy);
      
        TeamManager teamManager = new TeamManager();
        int parentId= (Integer)teamDao.selectUserPositionCsvUsingUserId(user.getReport_to(), accountId).get("id");
        teamManager.addMemberToOrganizationChart(team, parentId, userToken);
        
        return false;
    }
    
    
    private boolean updateReportToOfUser(User newUser,User earlierUser,int accountId,String userToken){
       try{ 
            int nreportTo= newUser.getReport_to();
            int userId= newUser.getId();

            int oldReportTo=earlierUser.getReport_to();

            HashMap usersOldTeamInfo=teamDao.selectUserPositionCsvUsingUserId(userId, accountId);
            if(usersOldTeamInfo.isEmpty()){
                newUser.setCreatedBy(newUser.getUpdatedBy());
                insertTeamForNewUser(newUser,accountId,userToken); //create Team for it
                return true;
            }
            int userTeamId=(Integer)usersOldTeamInfo.get("id");
            if(userTeamId==100000){
                return true; // as top user can not be edited
            }
            
            HashMap nheadExitingTeamInfo=teamDao.selectUserPositionCsvUsingUserId(nreportTo, accountId);
            String nheadsExistingCSV=(String)nheadExitingTeamInfo.get("team_position_csv");
            //String usersOldCSV=teamDao.selectUserPositionCsvUsingUserId(userId, accountId);
            String usersOldCSV=(String)usersOldTeamInfo.get("team_position_csv");
            int nheadTeamId=(Integer)nheadExitingTeamInfo.get("id");
            
            int cnt1=0;
//            System.out.println("userId > "+ userId);
            int parentTeamId=userDao.getParentTeamId(userId,accountId);
//            System.out.println("parentTeamId > "+ parentTeamId);
            if(parentTeamId==nheadTeamId){
                //it means user is already head
                return true;
            }
             
            if(nheadsExistingCSV.contains(usersOldCSV)){
                //it means user in below hirarchy to new reporting head

                
                String parentCSV =teamDao.selectUserPositionCsv(parentTeamId, accountId);
                //get teamId of newReportTo
                String nheadsNewCSV=nheadTeamId+","+parentCSV; // new CSV for reporting head
                String usersNewCSV =userTeamId+","+nheadsNewCSV; //users new CSV
                // now need to change the team members DB and all suboritinates of B and D and has_subordinate
                // update csv position 
                //First update the new heads subordinate and then users suborinate if any 

               // update teams set team_position_csv= REPLACE(team_position_csv,nheadsExistingCSV,nheadsNewCSV) where team_position_csv like  %nheadsExistingCSV
                cnt1=teamDao.updateSubOrdinatesPositionReportTo(nheadsExistingCSV,nheadsNewCSV,accountId);
                teamDao.updateHasSubordiantes(nheadTeamId, 1, accountId); // As it will have ordinates now
//                System.out.println("if updateReportToOfUser, 1st cnt1 >> "+ cnt1);
                //update users subordinate
                cnt1=teamDao.updateSubOrdinatesPositionReportTo(usersOldCSV,usersNewCSV,accountId);
//                System.out.println("if updateReportToOfUser, 2nd cnt1 >> "+ cnt1);
                if(cnt1>1) {
//                    System.out.println("if updateReportToOfUser, update hassubordinate to 1 for userteamid >>  "+userTeamId);
                    teamDao.updateHasSubordiantes(userTeamId, 1, accountId); // As it will have ordinates now
                }else{
//                    System.out.println("if updateReportToOfUser, update hassubordinate to 0 for userteamid >>  "+userTeamId);
                    teamDao.updateHasSubordiantes(userTeamId, 0, accountId); // As it wont have ordinates now
                }

                // Update Team Member table of User
                TeamMember member= new TeamMember();
                member.setTeamId(nheadTeamId);
                member.setUser(newUser);

                teamDao.updateTeamMemberId(member, accountId); // updated the parents id in team member table
                
                // Update Team member table for head
                User head= new User();
                head.setId(nreportTo);
                
                member.setTeamId(parentTeamId);
                member.setUser(head);
                teamDao.updateTeamMemberId(member, accountId); // updated the parents id in team member table

            }else{
                // this means reporting is not in hirarchy of user
                // Here simply we need to update team_position_csv of user and his subordinate and team_members table for users parentId
                // Also we need to update has_suborinate of reporting head as , he may not have suborinates earlier
                //update users subordinate
                String usersNewCSV =userTeamId+","+nheadsExistingCSV;
                cnt1=teamDao.updateSubOrdinatesPositionReportTo(usersOldCSV,usersNewCSV,accountId);
//                System.out.println("else updateReportToOfUser, 1st cnt1 >> "+ cnt1);
                teamDao.updateHasSubordiantes(nheadTeamId, 1, accountId); // As it will have subordinates now             
                           
                // Update Team Member table of User
                 TeamMember member= new TeamMember();
                member.setTeamId(nheadTeamId);
                member.setUser(newUser);
                teamDao.updateTeamMemberId(member, accountId); // updated the parents id in team member table

                // check if anyoneelse reporting to old head if not update hassubordinate to 0 .. this should be called after updating team member                
                //cnt1=teamDao.numberOfSubordinate(parentTeamId, accountId);
                parentTeamId=userDao.getParentTeamId(userId,accountId); // Added by jyoti, to fix the issue of myteam no subordinate even if present. (26-may-2018)
                cnt1=teamDao.checkSubordinatePersent(parentTeamId, accountId);
//                System.out.println(nheadTeamId + " ,else updateReportToOfUser, 2nd cnt1 >> "+ cnt1);
                if(cnt1<1){
                    teamDao.updateHasSubordiantes(nheadTeamId, 0, accountId); // As it will have subordinates now
//                    System.out.println(nheadTeamId + " ,else updateReportToOfUser, update hassubordinate to 0 for userteamid >>  "+userTeamId);
                }
                // updated subordinate
            }
            return true;
       }catch(Exception e){
           log4jLog.info("updateReportToOfUser e "+e);
           e.printStackTrace();
           return false;
       } 
    }
 private static BigDecimal truncateDecimal(double x, int numberofDecimals) {
        if (x > 0) {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_FLOOR);
        } else {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_CEILING);
        }
    } 

public Object UpdateTravelLogsBatch(TravelLogsData travelLogsData,String userToken){
         if (fieldSenseUtils.isTokenValid(userToken)) {
      int accountId = fieldSenseUtils.accountIdForToken(userToken);
       int locationCount=0;
       int locationNotResolvedCount=0;
      if(!travelLogsData.getAddressResolved().isEmpty()){
        CacheLocationManager cacheManager= new CacheLocationManager();
        Timestamp timeStamp=new Timestamp(System.currentTimeMillis());
        cacheManager.insertDataInStats(timeStamp,4,travelLogsData.getAddressResolved().size());   
        locationCount=usersTravelLogsDao.insertUsersTravelLogBatch(travelLogsData.getAddressResolved(),accountId);
        User user=new User();
        user.setLastKnownLocation(travelLogsData.getAddressResolved().get(travelLogsData.getAddressResolved().size()-1).getLastKnownLocation());
        user.setLatitude(travelLogsData.getAddressResolved().get(travelLogsData.getAddressResolved().size()-1).getLatitude());
        user.setLangitude(travelLogsData.getAddressResolved().get(travelLogsData.getAddressResolved().size()-1).getLangitude());
        user.setVersionCode(travelLogsData.getAddressResolved().get(travelLogsData.getAddressResolved().size()-1).getVersionCode());
        user.setId(travelLogsData.getAddressResolved().get(travelLogsData.getAddressResolved().size()-1).getId());
        userDao.updateUserLastKnownDetails(user);  
        usersTravelLogsDao.insertIntoCacheInsertBatch(travelLogsData.getAddressResolved(), accountId);
      }else{
//        System.out.println("UpdateTravelLogsBatch, else 2 ");
      locationCount=1;
      }
     if(!travelLogsData.getAddressNotResolved().isEmpty()){
//        System.out.println("UpdateTravelLogsBatch, if 3 ");
     locationNotResolvedCount=usersTravelLogsDao.insertIntoLocationNotFound(travelLogsData.getAddressNotResolved(),accountId);
//        System.out.println("UpdateTravelLogsBatch, locationNotResolvedCount >> "+locationNotResolvedCount);
         }else{
//         System.out.println("UpdateTravelLogsBatch, else 4 ");
     locationNotResolvedCount=1;
     }
     if(locationCount==0 && locationNotResolvedCount==0){
        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " updation failed . Please try again . ", "", "");
     }
     
    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " User updated successfully . ", " user ", "");
         }else{
         return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " invaild token . Please try with proper one. ", "", "");
         }
 }   
    //@Added by siddhesh, 09-01-2018
    public Object UpdateTravelLogsSingle(UsersTravelLogs user, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            int accountId = fieldSenseUtils.accountIdForToken(userToken);
        //int locationCount = usersTravelLogsDao.insertUsersTravelLogBatch(travelLogsData.getAddressResolved(), accountId);
            //int locationNotResolvedCount = usersTravelLogsDao.insertIntoLocationNotFound(travelLogsData.getAddressNotResolved(), accountId);     
            if (usersTravelLogsDao.insertTravelLogsSingle(user, accountId)) {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " User updated successfully . ", " user ", "");
            }
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " updation failed . Please try again . ", "", "");
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Token not vaild", "", "");
        }
    }
    
    /**
     * @added by vaibhav new user import
     * @param file
     * @param userToken
     * @return 
     */
    public Object importUserFromCSV(MultipartFile file, String userToken) {
        log4jLog.info("new method importUserFromCSV");
//        System.out.println("importUserFromCSV");
               
        try {
            TeamManager teamManager = new TeamManager();
//            String validStatus = Constant.VALID;
            String validStatus = "";
            List<Integer> userList = new ArrayList<Integer>();
            List<String> userEmailList = new ArrayList<String>();
            List<String[]> userErrorList = new ArrayList<String[]>();
            int userId = fieldSenseUtils.userIdForToken(userToken);
            if (fieldSenseUtils.isTokenValid(userToken)) {
                    int accountId = fieldSenseUtils.accountIdForToken(userToken);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss_SSS");
                    java.util.Date d = new java.util.Date();
                    String date = dateFormat.format(d);
                    String saveFileTo = Constant.IMPORT_ERROR_FILES;
                    String errorFileNm = accountId + "_" + date + "_UserImportError.csv";
                    String csv = saveFileTo + errorFileNm;
                    int userLimitForAccount = fieldSenseUtils.userLimitForAccount(accountId);
                    int totalUsersInAcount = fieldSenseUtils.totalUsersInAccount(accountId);
                    if (totalUsersInAcount <= userLimitForAccount) {
                        if (fieldSenseUtils.isUserAdmin(userId)) {                             
                            List<UserCSV> list = MapCSVToUser.mapJavaBean(file);
                            log4jLog.info("insertUserByImport 1");
                            for (int i = 0; i < 1; i++) {
                                 log4jLog.info("insertUserByImport 2");
                                if ((list.get(i).getEmp_code().equalsIgnoreCase("Emp. Code"))) { //modified emp_code by manohar
//                                     System.out.println("inside getEmp_code check");
                                    if ((list.get(i).getFirstName().equalsIgnoreCase("First Name")) && (list.get(i).getLastName().equalsIgnoreCase("Last Name"))) {
                                    if ((list.get(i).getDesignation().equalsIgnoreCase("Designation")) && (list.get(i).getPassword().equalsIgnoreCase("Password"))) {
                                        if ((list.get(i).getMobileNo().equalsIgnoreCase("Mobile No")) && (list.get(i).getGender().equalsIgnoreCase("Gender"))) {
                                            if ((list.get(i).getActive().equalsIgnoreCase("Active/Inactive")) && (list.get(i).getAllowTimeout().toLowerCase().startsWith("allow timeout")) &&  (list.get(i).getReportingHead().equalsIgnoreCase("ReportingHead")&&  (list.get(i).getRole().startsWith("Role")))) {
                                                if (list.get(i).getEmailAddress().equalsIgnoreCase("EmailAddress") /*&& (list.get(i).getuserAccuracy().equalsIgnoreCase("userAccuracy"))*/) {
                                                } else {                                                    
                                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "CSV file not in correct format. Please download the sample file.", "", "");
                                                }
                                            } else {
                                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "CSV file not in correct format. Please download the sample file.", "", "");
                                            }
                                        } else {
                                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "CSV file not in correct format. Please download the sample file.", "", "");
                                        }
                                    } else {
                                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "CSV file not in correct format. Please download the sample file.", "", "");
                                    }
                                } else {
                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "CSV file not in correct format. Please download the sample file.", "", "");
                                }
                             }// ended by manohar
                            }
                            log4jLog.info("insertUserByImport 3");
                            userErrorList.add(new String[]{"Emp. Code","First Name", "Last Name", "Designation", "Password", "Mobile No", "Gender", "Active/Inactive","Allow Timeout(Yes/No)","Role(Admin,on-field,accounts)", "ReportingHead", "EmailAddress","Comments"/*, "userAccuracy"*/});
                            int totalAllowedUsers = userLimitForAccount - totalUsersInAcount;
                            int listSize = list.size();
                            int length = 0;
                            if (totalAllowedUsers < listSize - 1) {
//                                System.out.println("inside getEmp_code totalAllowedUsers="+totalAllowedUsers);
                                length = totalAllowedUsers;
                                validStatus = "Not enough space.";
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, validStatus, "", "");
                            }
                            // start new logic here
                            
                            // get list of user email and rep head separately
                            ArrayList<String> emailAddrList = new ArrayList<String>();
                            for (int i = 1; i <= listSize - 1; i++) {
//                                 System.out.println("emailAddrList="+list.get(i).getEmailAddress());
                                emailAddrList.add(list.get(i).getEmailAddress());
                            }
                            
                             // get list of rep heads 
                            ArrayList<String> repHeadsList = new ArrayList<String>();
                            for (int i = 1; i <= listSize - 1; i++) {
//                                System.out.println("repHeadsList="+list.get(i).getReportingHead());
                                repHeadsList.add(list.get(i).getReportingHead());
                            }
                            
                            
                            // get users from db
                           ArrayList<User> usersListFromDB = (ArrayList)userDao.slectAllUsers(accountId);
                           length = listSize - 1;
                           String errorMsg ="";
                           boolean errorFlg = false;
                          
                           int row = 0;
                           // start outer loop for validations
                           outer: for (int i = 1; i <= length; i++) {
//                               System.out.println("start outer loop for validations");
                              boolean repHeadErrorFlg = false;
                              String email = list.get(i).getEmailAddress();
                              String mobile = list.get(i).getMobileNo();
                              String reportingHead  = list.get(i).getReportingHead();// now email_address
                              int accountIdForEmpCode= fieldSenseUtils.accountIdForToken(userToken);     //added by manohar
//                              if (fieldSenseUtils.isEmpCodeExist(list.get(i).getEmp_code(),accountIdForEmpCode) > 0) 
//                              {
//                                   errorFlg = true;
//                                           row = i;
//                                           String msg = "Duplicate Emp Code already exists in system";
//                                           if(!errorMsg .equals("")){
//                                               errorMsg = errorMsg +"," + msg;
//                                            }else{
//                                             errorMsg = msg;
//                                           }
//                              }
                              if (list.get(i).getFirstName().equals("")){
                                        errorFlg = true;
                                           row = i;
                                           String msg = "First Name cannot be empty";
                                           if(!errorMsg .equals("")){
                                               errorMsg = errorMsg +"," + msg;
                                            }else{
                                             errorMsg = msg;
                                           }
                              }
                              else if (FieldSenseUtils.isname(list.get(i).getFirstName())){
                                           errorFlg = true;
                                           row = i;
                                           String msg = "Invalid first name";
                                           if(!errorMsg .equals("")){
                                               errorMsg = errorMsg +"," + msg;
                                            }else{
                                             errorMsg = msg;
                                           }
                                           
                                           //break;
                                            
                                }
                              if(!list.get(i).getLastName().equals("")){ // last name not mandatory
                                if ((FieldSenseUtils.isname(list.get(i).getLastName()))) {
                                            row = i;
                                           errorFlg = true;
                                           String msg = "Invalid last name" ;
                                            if(!errorMsg .equals("")){
                                               errorMsg = errorMsg +"," + msg;
                                            }else{
                                             errorMsg = msg;
                                           }
                                             //break;
                                 }
                              }
                                if (list.get(i).getDesignation().equals("") ) {
                                     row = i;
                                           errorFlg = true;
                                           String msg = "Please enter designation";
                                            if(!errorMsg .equals("")){
                                               errorMsg = errorMsg +"," + msg;
                                            }else{
                                             errorMsg = msg;
                                           }  
                                    
                                    
                                }  
                                else if(!FieldSenseUtils.isContainOnlyCharacters(list.get(i).getDesignation())){
                                   row = i;
                                           errorFlg = true;
                                           String msg = "Designation invalid";
                                            if(!errorMsg .equals("")){
                                               errorMsg = errorMsg +"," + msg;
                                            }else{
                                             errorMsg = msg;
                                           }  
                                     
                                }
                                if(list.get(i).getMobileNo().equals("")){
                                   row = i;
                                            errorFlg = true;
                                            String msg = "Mobile No cannot be blank";
                                         
                                            if(!errorMsg .equals("")){
                                               errorMsg = errorMsg +"," + msg;
                                            }else{
                                             errorMsg = msg;
                                           }  
                                    
                                }
                                else if (!(FieldSenseUtils.isContainOnlyNumbers(list.get(i).getMobileNo()))) {
                                            row = i;
                                            errorFlg = true;
                                            String msg = "Invalid mobile number";
                                         
                                            if(!errorMsg .equals("")){
                                               errorMsg = errorMsg +"," + msg;
                                            }else{
                                             errorMsg = msg;
                                           }
                                            //break;
                                        }                                    
                                       //
                                       
                                       if((list.get(i).getEmailAddress().equals(""))){
                                           row = i;
                                            errorFlg = true;
                                            String msg = "Email cannot be empty";
                                            
                                            if(!errorMsg .equals("")){
                                               errorMsg = errorMsg +"," + msg;
                                            }else{
                                             errorMsg = msg;
                                           }   
                                           
                                       }
                                       else if ( !(FieldSenseUtils.isValidEmailAddress(list.get(i).getEmailAddress()))) {
                                            row = i;
                                            errorFlg = true;
                                            String msg = "Invalid email address ";
                                            
                                            if(!errorMsg .equals("")){
                                               errorMsg = errorMsg +"," + msg;
                                            }else{
                                             errorMsg = msg;
                                           }
                                            //break;
                                        }
                                       
                                       if(!list.get(i).getReportingHead() .equals("")){ // rep head will now be email address
                                           if ( !(FieldSenseUtils.isValidEmailAddress(list.get(i).getReportingHead()))){
                                                row = i;
                                                errorFlg = true;
                                                repHeadErrorFlg = true;
                                                String msg = "Invalid reporting head email address";

                                                if(!errorMsg .equals("")){
                                                   errorMsg = errorMsg +"," + msg;
                                                }else{
                                                 errorMsg = msg;
                                               }
                                           }
                                       }
                                        //
                                        if(list.get(i).getPassword().equals("")){
                                           row = i;
                                           errorFlg = true;
                                           String msg =  "Password cannot be empty";
                                           
                                            if(!errorMsg .equals("")){
                                               errorMsg = errorMsg +"," + msg;
                                            }else{
                                             errorMsg = msg;
                                           }   
                                            
                                        }
                                       else if (!(FieldSenseUtils.isPasswordValid(list.get(i).getPassword()))) {
                                             row = i;
                                            errorFlg = true;
                                           String msg =  "Password should contains alphabets numbers and !@#$ and it must be 6 character long at line:" + (i ) + ".";
                                           
                                            if(!errorMsg .equals("")){
                                               errorMsg = errorMsg +"," + msg;
                                            }else{
                                             errorMsg = msg;
                                           }
                                            //break;
                                        }
                                 
                                        if ((list.get(i).getActive().equals("")) || (list.get(i).getActive().equals(null))) {
                                        } else if (list.get(i).getActive().trim().equalsIgnoreCase("active")) {
                                            //user.setActive(true);
                                        } else if (list.get(i).getActive().equalsIgnoreCase("inactive")) {
                                            //user.setActive(false);
                                        } else {
                                           // validStatus = FieldSenseUtils.notvalid(validStatus);
                                            row = i;
                                            String msg = "Invalid active field ";//at line:" + (i) + ".";
                                            errorFlg = true;
                                          
                                            if(!errorMsg .equals("")){
                                               errorMsg = errorMsg +"," + msg;
                                            }else{
                                             errorMsg = msg;
                                           }
                                            // break;
                                        }
                                        if (list.get(i).getGender().equalsIgnoreCase("female")) {
                                            //user.setGender(0);
                                        } else if (list.get(i).getGender().equalsIgnoreCase("male")) {
                                            //user.setGender(1);
                                        } else {
                                             row = i;
                                            errorFlg = true;
                                            String msg =  "Invalid gender ";
                                            
                                            if(!errorMsg .equals("")){
                                               errorMsg = errorMsg +"," + msg;
                                            }else{
                                             errorMsg = msg;
                                           }
                                            // break;
                                        }
                                        
                                        if(list.get(i).getAllowTimeout().trim().equalsIgnoreCase("yes")){
                                            ///user.setAllowTimeout(1);
                                        }else{
                                            //user.setAllowTimeout(0);
                                        }
                                        
                                        if ((list.get(i).getRole().equalsIgnoreCase("admin")) || (list.get(i).getRole().equals("AD"))) {
                                            //user.setRole(1);
                                        } else if ((list.get(i).getRole().equalsIgnoreCase("on-field"))) {
                                            //user.setRole(5);
                                        }else if ((list.get(i).getRole().equalsIgnoreCase("accounts"))) {
                                           // user.setRole(2);
                                        } else {
                                             row = i;
                                            errorFlg = true;
                                            String msg =  "Invalid role ";
                                            
                                            if(!errorMsg .equals("")){
                                               errorMsg = errorMsg +"," + msg;
                                            }else{
                                             errorMsg = msg;
                                           }
                                            //break;
                                        }
                            inner :for(int j= i+1; j<=length;j++ ){ //inner loop check for duplicate email and mobile in current csv only
                                   if(email .equals(list.get(j).getEmailAddress())){
                                        row = i;
                                       String msg = "Duplicate email matches with row no." + j;
                                       errorFlg = true;
                                       
                                            if(!errorMsg .equals("")){
                                               errorMsg = errorMsg +"," + msg;
                                            }else{
                                             errorMsg = msg;
                                           }
                                      //  break;
				    }
                                 
                                   if(mobile .equals(list.get(j).getMobileNo())){
                                        row = i;
                                       String msg = "Duplicate mobile matches with row no." + j;
                                       errorFlg = true;
                                          if(!errorMsg .equals("")){
                                               errorMsg = errorMsg +"," + msg;
                                            }else{
                                             errorMsg = msg;
                                           }
                                        break;
                                   }
                             } // end inner loop 
                          //   if(!errorFlg){
                          // now check for email and mobile with db
                                if(dblistcontainsEmail(usersListFromDB,email)){
                                     row = i;
                                    String msg = "Duplicate email already exists in system";
                                    errorFlg = true;
                                       if(!errorMsg .equals("")){
                                               errorMsg = errorMsg +"," + msg;
                                            }else{
                                             errorMsg = msg;
                                           }

                                 }
                                if(dblistcontainsMobile(usersListFromDB, mobile)){
                                      row = i;
                                     String msg ="Duplicate mobile already exists in system";
                                     errorFlg = true;
                                        if(!errorMsg .equalsIgnoreCase("")){
                                               errorMsg = errorMsg +"," + msg;
                                            }else{
                                             errorMsg = msg;
                                           }
                                 }
                            // }       
                      //  if(!errorFlg){   // check only if error flg from above false          
                            // check for hierarchy correct(check for reportinghead present in user email in csv
                       if(!reportingHead.equals("")){
                         if(!repHeadErrorFlg){ // only if earlier rep head validation pass
                           String userEmailTobeSearched ="";
                           boolean reportingHeadPresentinCSV = false;
                           for(int j= 1 ; j<=length;j++ ){
                              if(i != j){
                                userEmailTobeSearched = list.get(j).getEmailAddress();
                                if(reportingHead.equalsIgnoreCase(userEmailTobeSearched)){
                                    reportingHeadPresentinCSV = true;
                                    break;
                                  }
                                }
                             }
                           // check if reporting head(email) present in db list
                           if(!reportingHeadPresentinCSV){
                                if(!dblistcontainsEmail(usersListFromDB,reportingHead)){
                                   row = i;
                                    errorFlg = true;
                                    String msg = "This reporting head not present in csv as well as system ";
                                    if(!errorMsg .equals("")){
                                        errorMsg = errorMsg +"," + msg;
                                     }else{
                                        errorMsg = msg;
                                     }
                               }
                           }
                         } // end check if prev vaidation of rep head not true
                       }   // end check if reporting head not blank for further validation
                        // check for cyclic rep head here
                        //boolean cyclicFound = false;
                        //for(int l = 0; l<= emailAddrList.size() - 1; l++){
                            
                            tempInt = i;
                            arrayPositions = new ArrayList<Integer>();
                            mapCyclic = new LinkedHashMap();
                            LinkedHashMap<String,String> map = buildMapForCyclicCheck(emailAddrList,repHeadsList,i-1);
                          
                            Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                            String cyclicMsg ="";
                            Map.Entry<String, String> pair = it.next();
                            String mapRootVal = pair.getKey();
                            while (it.hasNext()) {
                                pair = it.next();
                                if(mapRootVal .equals(pair.getValue())){
                                    errorFlg = true;
                                    
                                    String msg = "This reporting head is cyclic with following pairs in csv file" ;
                                    
                                    for (Map.Entry<String,String> cycPairs : map.entrySet()) {
                                        cyclicMsg += "(user " + cycPairs.getKey() + " reporting head "+ cycPairs.getValue()  + ")";
                                    }
                                    msg = msg + " " + cyclicMsg;
                                    if(!errorMsg .equals("")){
                                        errorMsg = errorMsg +"," + msg;
                                     }else{
                                        errorMsg = msg;
                                     }
                                     
                                    break;
                                }
                              
                            } // iterator for returned map
                          //if(cyclicFound) break;
                            
                        //} // end for loop for cyclic rep head
                        userErrorList.add(new String[]{list.get(i).getEmp_code(),list.get(i).getFirstName(), list.get(i).getLastName(), list.get(i).getDesignation(), list.get(i).getPassword(), list.get(i).getMobileNo(), list.get(i).getGender(), list.get(i).getActive(), list.get(i).getAllowTimeout(),list.get(i).getRole(), list.get(i).getReportingHead(),list.get(i).getEmailAddress(),errorMsg/*,list.get(i).getuserAccuracy()*/});
                        errorMsg = "";
                        cyclicMsg="";
                    }// end outer loop check for validations
                        // for whole email addr list
               
               
                           if(errorFlg){
                        
                                CSVWriter writer = new CSVWriter(new FileWriter(csv));
                                writer.writeAll(userErrorList);
                                //System.out.println("Error CSV written successfully.");
                                writer.close(); 
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Users not added . Please download error file .", "ErrorFileName", errorFileNm);
                                
                           }
       
                           // validations ok here now get new sorted list sort list as per reporting head
                            list = getSortedReportingHeadList(list);
                            length = list.size() - 1;
                            // process sorted list here
                          for (int i = 1; i <= length; i++) {
                                User user = new User();
                                
                                
                                        if ((list.get(i).getActive().equals("")) || (list.get(i).getActive().equals(null))) {
                                        } else if (list.get(i).getActive().trim().equalsIgnoreCase("active")) {
                                            user.setActive(true);
                                        } else if (list.get(i).getActive().equalsIgnoreCase("inactive")) {
                                            user.setActive(false);
                                        } 
                                        if (list.get(i).getGender().equalsIgnoreCase("female")) {
                                            user.setGender(0);
                                        } else if (list.get(i).getGender().equalsIgnoreCase("male")) {
                                            user.setGender(1);
                                        } 
                                        
                                        if(list.get(i).getAllowTimeout().trim().equalsIgnoreCase("yes")){
                                            user.setAllowTimeout(1);
                                        }else{
                                            user.setAllowTimeout(0);
                                        }
                                        
                                        if ((list.get(i).getRole().equalsIgnoreCase("admin")) || (list.get(i).getRole().equals("AD"))) {
                                            user.setRole(1);
                                        } else if ((list.get(i).getRole().equalsIgnoreCase("on-field"))) {
                                            user.setRole(5);
                                        }else if ((list.get(i).getRole().equalsIgnoreCase("accounts"))) {
                                            user.setRole(2);
                                        } 
                        
                                       if ((list.get(i).getReportingHead().equals(""))){// || !(fieldSenseUtils.isReportingHeadValid(list.get(i).getReportingHead(),accountId))) {
                                            // int organisationHeadId= fieldSenseUtils.getOrganisationHeadId(accountId);
                                             user.setParentId(100000); 
                                             user.setReport_to(userDao.getTopUserInHierarachy(accountId));
                                         }
                                         else{
                                              String reportingHead=list.get(i).getReportingHead();// reporting head will now be email address
                                              int head_id=fieldSenseUtils.getUserIdFrmEmailAddress(reportingHead); // changed from full name to email addr
                                              int parentTeamId=fieldSenseUtils.selectUsersTeamId(head_id,accountId);

                                              int headRole =fieldSenseUtils.roleOfUser(head_id);
                                              int userRole=user.getRole();// since role already set
                                              boolean validReportTo=false;
                                              if(headRole==1 || headRole==userRole){
                                                  validReportTo=true;
                                              }
                                              if(head_id==0 || parentTeamId==0 || validReportTo==false){
                                                  user.setParentId(100000); 
                                                  user.setReport_to(userDao.getTopUserInHierarachy(accountId));
                                              }else{
                                                user.setParentId(parentTeamId);
                                                user.setReport_to(head_id);
                                              }
                                         }
                                       // if (!errorFlg) {
                                            user.setFirstName(list.get(i).getFirstName());
                                            user.setLastName(list.get(i).getLastName());
                                            user.setEmailAddress(list.get(i).getEmailAddress());
                                            user.setPassword(list.get(i).getPassword());
                                            user.setMobileNo(list.get(i).getMobileNo());
                                            user.setEmp_code(list.get(i).getEmp_code());
                                            //Added by Mayank Ramaiya
                                            user.setUserAccuracy(500);  //default value for an accuracy
                                            user.setCheckInRadius(500); //default value for an check-in radius
                                            //End by Mayank Ramaiya                                                                               
                                            
                                            user.setAccountId(accountId);
                                            String location = fieldSenseUtils.getCity(accountId);
                                            user.setLastKnownLocation(location);
                                            user.setCreatedBy(userId);
                                            user.setPassword(FieldSensePasswordEncryptionDecryption.hashPassword(user.getPassword()));
                                            user.setDesignation(list.get(i).getDesignation());          
                                            int userId2 = userDao.insertUser(user);
                                            // Added by Jyoti 30-12-2016
                                            try{
                                                int userTerritoryId = userDao.insertUserTerritoryUnknownForCSVFile(userId2, accountId);                                                
                                            }catch(Exception e){
                                                log4jLog.info("insertUserByImport_Territory"+ e);
                                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Could not add default territory. Please try again . ", "", "");
                                            }                                            
                                            // Ended by Jyoti
                                            if (userId2 != 0) {                                                
                                                
                                                try{
                                                    // Added By jyoti, 21-06-2017
                                                    String bgImagePath = Constant.PROFILEPIC_UPLOAD_PATH + "FM.png";
                                                    MapsIconCreate mapsIconCreate = new MapsIconCreate();
                                                    BufferedImage mapMarker = mapsIconCreate.readImage(bgImagePath);
                                                    mapsIconCreate.writeImage(mapMarker, Constant.PROFILEPIC_UPLOAD_PATH + accountId + "_" + userId2 + "_mm.png", "PNG");

                                                    // for sending email notification
                                                    String userName = user.getFirstName() + " " + user.getLastName();
                                                    EmailNotification emailNotification = new EmailNotification();
                                                    emailNotification.registerUser(userName, user.getEmailAddress());

                                                    // for sending sms notification
//                                                    if (user.isMobileNotification()) {
                                                        MessageNotifications messageNotifications = new MessageNotifications();
                                                        String adminFirstName = fieldSenseUtils.getUserFirstName(fieldSenseUtils.userIdForToken(userToken));
                                                        String adminLastName = fieldSenseUtils.getUserlastName(fieldSenseUtils.userIdForToken(userToken));
                                                        messageNotifications.createUser(user.getFirstName(), user.getLastName(), adminFirstName, adminLastName, user.getMobileNo());
//                                                    }
                                                
                                                    // for creating hierarchy of the user
                                                    //create Team for it --> start
                                                    user.setId(userId2);
                                                    Team team=new Team();
                                                    team.setTeamName("Hirarchy");
                                                    team.setDescription("Hirarchy");
                                                    team.setOwnerId(user);
                                                    team.setIsActive(1);

                                                    User createdBy= new User();
                                                    createdBy.setId(userId);                                            
                                                    team.setCreatedBy(createdBy);
                                                    int parentId=fieldSenseUtils.selectUsersTeamId(user.getReport_to(),accountId);
                                                    if(parentId==0){
                                                        parentId=100000;
                                                    }
                                                    teamManager.addMemberToOrganizationChart(team,parentId , userToken);
                                                    //create Team for it --> end
                                                    
                                                    userList.add(userId2);
                                                    userEmailList.add(user.getEmailAddress()); 
                                                    
                                                    // For creating entry in user_keys table, which display the status of user i.e. inMeeting, can take call etc
                                                    List<String> sqlQueriesList = userDao.getCreateUserDeafultQueries();
                                                    if (!(sqlQueriesList == null || sqlQueriesList.isEmpty())) {
                                                        userDao.executeCreateUserDeafultQueries(sqlQueriesList, userId2, accountId);
                                                    }
                                                    
                                                    if (accountId != 0) {
                                                        List<String> accountSqlQueriesList = accountRegistrationDao.getCreateAccountDeafultQueries();
                                                        if (!(accountSqlQueriesList == null || accountSqlQueriesList.isEmpty())) {
                                                            accountRegistrationDao.executeAccountUserDeafultQueries(accountSqlQueriesList, userId2, accountId);
                                                        }
                                                    }
                                            
                                                } catch(Exception e){
                                                    log4jLog.info("import_user_sending_sms_email_adding_team_>>> "+e);
                                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " import user - sending sms, email, adding team  >>> ", "", e);
                                                }
                                                // Ended By jyoti, 21-06-2017                                                                                            
                                            }
                                       // }
                                    } // end new list for loop
                                    // return succ
                                  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.USERS_CREATED_SUCCESFULLY, "","" );
                       } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " You are not authorised to create user ", "", "");
                        }
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "You exceeds the user limit", "No Error File", "Error file not generated");
                    }   
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
            }
        }catch (FileNotFoundException ex) {
            log4jLog.info("insertUserByImport ex "+ex);
            ex.printStackTrace();
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "File not uploaded. Please try again . ", "", "");
        }catch (IOException ex) {
            log4jLog.info("insertUserByImport_IOException "+ex);
            ex.printStackTrace();
        }
    
        return null;
    }// end method
    
    /**
     * @Added by vaibahv
     * @param list
     * @return 
     */
    private List<UserCSV> getSortedReportingHeadList(List<UserCSV> list){
         // start after header row
         boolean reportingHeadBlankPresent = false;
           int i = 1; // reset i
            while (i < list.size() ){

                if(!list.get(i).getReportingHead().equals("")){
                    i++;

                }else{
                        reportingHeadBlankPresent = true;
                        Collections.swap(list,1,i);
                        break;
                    
                }
           }
            int k = 1;
            if(reportingHeadBlankPresent){
                k = 2; // start after header and reportinghead blank row
            }else{
                k = 1; // start after headr only
            }
        
        int l = k  + 1; 
        sortList(k,l,list); //,sortedList);
        
        return list;
   }
    
    /**
     * @Added by vaibhav
     * @param k
     * @param l
     * @param list
     * @return 
     */
    private List<UserCSV> sortList(int k , int l,List<UserCSV> list){ //,List<UserCSV> sortedList){
        
        
         String reportingHead  = list.get(k).getReportingHead(); // email addr
         String emailToBeSearched ="";
         while(l <= list.size() - 1){
            emailToBeSearched = list.get(l).getEmailAddress();
             
             if(!reportingHead.equalsIgnoreCase(emailToBeSearched)){
		l = l + 1;
               }else{
                 break;
             }
	}
        if(reportingHead.equalsIgnoreCase(emailToBeSearched)){
                    Collections.swap(list, k, l);
                          
	}else{
              k = k + 1;
        }
       
        
        if(k <= list.size() - 1 ){
            l = k + 1;
            sortList(k,l,list); //,sortedList);
        }
   
        return list;
    }
    
    /**
     * @Added by vaibhav
     * @param list
     * @param email
     * @return 
     */
    private  static boolean dblistcontainsEmail(ArrayList<User> list, String email) {
        for(User o : list) {
            if(o != null && o.getEmailAddress().equals(email)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * @Added by vaibhav
     * @param list
     * @param mobile
     * @return 
     */
    private  static boolean dblistcontainsMobile(ArrayList<User> list, String mobile) {
        for(User o : list) {
            if(o != null && o.getMobileNo().equals(mobile)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * @Added by vaibhav
     * @param listUserEmails
     * @param listRepHeads
     * @param ite
     * @return 
     */
    private LinkedHashMap<String,String> buildMapForCyclicCheck(ArrayList<String>listUserEmails,ArrayList<String>listRepHeads, int ite){
        
     
            String repHead = listRepHeads.get(ite);
            int pos = listUserEmails.indexOf(repHead);
            if(!arrayPositions.contains(pos)){
               mapCyclic.put(listUserEmails.get(ite),repHead);
            }
      
               if(listUserEmails.contains(repHead) && pos != tempInt && !arrayPositions.contains(pos)){
                    repHead = listRepHeads.get(pos);
                    mapCyclic.put(listUserEmails.get(pos),repHead);
                    if(!arrayPositions.contains(pos)){
                       arrayPositions.add(pos);
                     }
                      ite=pos;
                      buildMapForCyclicCheck(listUserEmails,listRepHeads,ite);
               }
       
        return mapCyclic;
    }
    
    
    /**
     * 
     * @param userToken
     * @return list of all users with details
     */
    public List<HashMap> getUserlist(int accountId ,int userId,String fromDate,String toDate,int start , int end) {
//        System.out.println("Helllo  hdiqjhid" );
            return userDao.getUsers(accountId,userId,fromDate,toDate,start,end);
    }
    
     /**
     * 
     * @param userToken
     * @return list of all users with details
     */
    public List<HashMap> getUserlistForSubordinates(int accountId ,int userId,String fromDate,String toDate,ArrayList subdornateList,int start ,int end) {
//        System.out.println("Helllo  In subdornates" );
            return userDao.getUsersForSubordinate(accountId,subdornateList,userId,fromDate,toDate,start,end);
    }
    
    
}
