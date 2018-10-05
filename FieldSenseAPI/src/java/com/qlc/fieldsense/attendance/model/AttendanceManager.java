/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.attendance.model;

import com.qlc.fieldsense.addressConverter.AddressConverter;
import com.qlc.fieldsense.addressConverter.GoogleResponse;
import com.qlc.fieldsense.addressConverter.Result;
import com.qlc.fieldsense.attendance.dao.AttendanceDao;
import static com.qlc.fieldsense.attendance.service.AttendanceService.log4jLog;
import com.qlc.fieldsense.user.dao.UserDao;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.userKey.dao.UserKeyDao;
import com.qlc.fieldsense.userKey.model.UserKey;
import com.qlc.fieldsense.usersTravelLogs.dao.UsersTravelLogsDao;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import com.qlc.fieldsense.auth.dao.AuthenticationUserDao;
import java.sql.Date;
import java.util.List;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javapns.notification.PushNotificationBigPayload;
import javapns.notification.PushNotificationPayload;
import net.sf.json.JSONObject;

/**
 *
 * @author mayank
 * @modified by :anuja
 */
public class AttendanceManager {

    AttendanceDao attendanceDao = (AttendanceDao) GetApplicationContext.ac.getBean("attendanceDaoImpl");
    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
    UserDao userDao = (UserDao) GetApplicationContext.ac.getBean("userDaoImpl");
    UserKeyDao userKeyDao = (UserKeyDao) GetApplicationContext.ac.getBean("userKayDaoImpl");
    UsersTravelLogsDao usersTravelLogsDao = (UsersTravelLogsDao) GetApplicationContext.ac.getBean("usersTravelLogsDaoImpl");
    AuthenticationUserDao authenticationUserDao = (AuthenticationUserDao) GetApplicationContext.ac.getBean("authenticationDaoImpl");

     /**
     * @param attendance
     * @param userToken
     * @return 
     * @purpose used to punch in or punch out
     * @deprecated 
     */
    public Object punch(Attendance attendance, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (!attendanceDao.isUsersFirstPunch(attendance.getUser().getId(), accountId)) {
                    return punchIn(attendance, userToken);
                } else {
                    return punchOut(attendance, userToken);
                }  
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again. ", "", "");
        }
    }

    private Object punchIn(Attendance attendance, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (attendanceDao.insertAttendancePunchIn(attendance, accountId)) {
                    User user = new User();
                    user.setId(attendance.getUser().getId());
                    user.setLastKnownLocation(attendance.getPunchInLocation());
                    double latitude = Double.parseDouble(attendance.getPunchInLatitude());
                    user.setLatitude(latitude);
                    double lagitude = Double.parseDouble(attendance.getPunchInLangitude());
                    user.setLangitude(lagitude);
                    userDao.updateUserLastKnownDetails(user);

                    UserKey keyCantakeCall = new UserKey();
                    keyCantakeCall.setKeyValue("1");
                    User userCanTakeCall = new User();
                    userCanTakeCall.setId(attendance.getUser().getId());
                    keyCantakeCall.setUserId(userCanTakeCall);
                    keyCantakeCall.setUserKay("CanTakeCalls");
                    userKeyDao.updateUserKeys(keyCantakeCall, accountId);

                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Successfully Punch In.", "", "");
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Please try again. [punchIn : insert punch in failed ]", "", "");
                }   
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again. ", "", "");
        }
    }

    private Object punchOut(Attendance attendance, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (attendanceDao.updateAttendancePunchOut(attendance, accountId)) {

                    UserKey keyCantakeCall = new UserKey();
                    keyCantakeCall.setKeyValue("0");
                    User userCanTakeCall = new User();
                    userCanTakeCall.setId(attendance.getUser().getId());
                    keyCantakeCall.setUserId(userCanTakeCall);
                    keyCantakeCall.setUserKay("CanTakeCalls");

                    UserKey keyInMeeting = new UserKey();
                    keyInMeeting.setKeyValue("0");
                    User userInMeeting = new User();
                    userInMeeting.setId(attendance.getUser().getId());
                    keyInMeeting.setUserId(userInMeeting);
                    keyInMeeting.setUserKay("InMeeting");

                    userKeyDao.updateUserKeys(keyCantakeCall, accountId);
                    userKeyDao.updateUserKeys(keyInMeeting, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Successfully Punch Out.", "", "");
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Please try again. [punchOut : update punch out failed ]", "", "");
                } 
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again. ", "", "");
        }
    }

    /**
     * @param userId
     * @param userToken
     * @return 
     * @ structure changed and not in use anymore
     * @deprecated
     */
    public Object getUserAttendance(int userId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<Attendance> attendances = attendanceDao.selectAttendance(userId, accountId);
                if (attendances == null || attendances.isEmpty()) {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "No data available", "", "");
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Attendance Data", "Attendance", attendances);
                }  
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again. ", "", "");
        }
    }

    /**
     * @param userId
     * @param date
     * @param userToken
     * @return 
     * @ structure changed and not in use anymore
     * @deprecated
     */
    public Object getUserAttendance(int userId, Date date, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                Attendance attendance = attendanceDao.selectAttendance(userId, date, accountId);
                if (attendance == null) {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "No data available", "", "");
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Attendance Data", "Attendance", attendance);
                }   
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again. ", "", "");
        }
    }

    /**
     * @param date
     * @param userToken
     * @return 
     * @ structure changed and not in use anymore
     * @deprecated
     */
    public Object getAttendance(Date date, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                Attendance attendance = attendanceDao.selectAttendance(date, accountId);
                if (attendance == null) {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "No data available", "", "");
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Attendance Data", "Attendance", attendance);
                }
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again. ", "", "");
        }
    }

    /**
     *
     * @param userToken
     * @return punch in time for loggedin user
     * @ author : anuja
     */
    public Object getPunchInTime(String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                int userId = fieldSenseUtils.userIdForToken(userToken);
                String punchInTime = attendanceDao.userPunchInTime(userId, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "punchInTime", punchInTime);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again. ", "", "");
        }
    }

     /**
     * @param date
     * @param userId
     * @param userToken
     * @return 
     * @purposr : get attendance details time of specific user for specific date
     * @author : anuja
     */
    public Object getPunchInTimeForUser(String userToken, int userId, String date) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                PunchDateTime punchDateTime = new PunchDateTime();
                punchDateTime = attendanceDao.userPunchInTimeDate(userId, date, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "punchInTime", punchDateTime); 
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again. ", "", "");
        }
    }
    
    /**
     * @param userToken
     * @param userId
     * @return 
     * @purposr : get attendance details time of specific user for specific date
     * @author : anuja
     */
    public Object getLastPunchDateTimeForUser(String userToken, int userId) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                PunchDateTime punchDateTime = new PunchDateTime();
                punchDateTime = attendanceDao.userLastPunchDateTime(userId, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "punchInTime", punchDateTime);  
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again. ", "", "");
        }
    }

    /**
     * @param attendance
     * @param userToken
     * @return 
     * @purpose used to punch in or punch out with LocationNotFoundReason
     */
    public Object punchWithLocationNotFoundReason(Attendance attendance, String userToken) {
//        System.out.println("punchWithLocationNotFoundReason, attendance :"+attendance.toString());
        if (fieldSenseUtils.isTokenValid(userToken)) {
                if (attendance != null) {
                    if (attendance.getUser() != null) {
                        int accountId = fieldSenseUtils.accountIdForToken(userToken);
                        String locationPunchIn = attendance.getPunchInLocation();
                        locationPunchIn = locationPunchIn.trim();
                        attendance.setPunchInLocation(locationPunchIn);
                        String locationPunchOut = attendance.getPunchOutLocation();
                        locationPunchOut = locationPunchOut.trim();
                        attendance.setPunchOutLocation(locationPunchOut);
                        boolean isFirstPunch=false;
                        /*if(attendance.getVersionCode()<22){
                            isFirstPunch = attendanceDao.isUsersFirstPunch(attendance.getUser().getId(), attendance.getSdate(), accountId);
                        }else{*/
                            String tzOffset=convertTimeZoneOffsetToStringForMobile(attendance.getTimeZoneOffset());
                            //if(attendance.getId()==0 && attendanceDao.isUsersFirstPunch(attendance.getUser().getId(), attendance.getSdate(), accountId)){
                            if(attendance.getId()==0 && attendanceDao.isUsersFirstPunch(attendance.getUser().getId(), attendance.getSdate(),attendance.getSpunchInTime(),tzOffset, accountId)){
                                int attendanceId = attendanceDao.getAttendanceId(attendance.getUser().getId(), attendance.getSdate(), accountId);
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Already Punched In.", "", attendanceId); 
                            }else{
                                isFirstPunch = attendanceDao.isUsersFirstPunchUsingId(attendance.getId(), accountId);
                            }    
                        //}
//                            System.out.println("isFirstPunch if true == punch in done else if false punch in yet to do " + isFirstPunch);
                        if (!isFirstPunch) {
//                            System.out.println("if !isFirstPunch > "+isFirstPunch);
                            if(attendance.getPunchInLatitude().trim().equals("0.0") && attendance.getPunchInLangitude().trim().equals("0.0")){
                                if(!attendance.isAutoPunchOut()){
                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid latitude ,longitude. ", "", "");
                                }
                            }    
                            if ((attendance.getPunchInLocation().trim().equals("") || attendance.getPunchInLocation().isEmpty() || attendance.getPunchInLocation().trim().equalsIgnoreCase("Unknown location")) && (!(attendance.getPunchInLatitude().trim().equals("0.0") && attendance.getPunchInLangitude().trim().equals("0.0")))) {
                                   attendance.setPunchInLocation("Unknown location");
                                try {
                                    //Commented by siddhesh for Redundant code for google
//                                    GoogleResponse res = new AddressConverter().convertToAddressFromLatLong(attendance.getPunchInLatitude(), attendance.getPunchInLangitude());
//                                        if (res.getStatus().equals("OK")) {
//                                            Result result = res.getResults()[0];
//                                            attendance.setPunchInLocation(result.getFormatted_address());
//                                            System.out.println("LocationPunchIn was blank and from google="+result.getFormatted_address());
//
//                                        }

                                        com.qlc.cacheLocation.model.CacheLocationManager cacheManager= new com.qlc.cacheLocation.model.CacheLocationManager();
                                        String address=cacheManager.getLocationFromLatLongSats(Double.parseDouble( attendance.getPunchInLatitude()), Double.parseDouble( attendance.getPunchInLangitude()),attendance.getUser().getId(),accountId);
                                        if(!address.trim().equals("")){
                                            attendance.setPunchInLocation(address);
//                                            System.out.println("setPunchInLocation > "+ attendance.getPunchInLocation());
                                        }
                                    } catch (Exception ex) {
                                        log4jLog.info("punchWithLocationNotFoundReason"+ex);
                                        ex.printStackTrace();
                                    }     
                            }
                            return punchInWithLocationNotFoundReason(attendance, userToken,accountId);
                        } else {
                            System.out.println("punch in done, so request is for punch out for attid > " + attendance.getId() + " and user id : "+attendance.getUser().getId());
                            boolean punchStatus = false;
                            /*if(attendance.getVersionCode()<22){
                                punchStatus = attendanceDao.getPunchStatus(attendance.getUser().getId(), accountId);
                            }else{*/
                                punchStatus = attendanceDao.getPunchStatusUsingId(attendance.getId(), accountId);
                            //}
//                            System.out.println("punchWithLocationNotFoundReason punchStatus if == 1 already punch out: "+punchStatus);
                            if (!punchStatus) {
//                                System.out.println("else punchStatus > "+punchStatus);
                                if(attendance.getPunchOutLatitude().trim().equals("0.0") && attendance.getPunchOutLagitude().trim().equals("0.0")){
                                    if(!attendance.isAutoPunchOut()){
                                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid latitude ,longitude. ", "", "");
                                    }
                                } 
                                if(locationPunchOut.equals("")){
//                                    System.out.println("else LocationPunchOut is blank and punchIn="+locationPunchIn);
                                    attendance.setPunchOutLocation(locationPunchIn); // if value is set in punchin instead of punchout variable
                                }
                                if ((attendance.getPunchOutLocation().trim().equals("") || attendance.getPunchOutLocation().isEmpty() || attendance.getPunchOutLocation().trim().equalsIgnoreCase("Unknown location")) && (!(attendance.getPunchOutLatitude().trim().equals("0.0") && attendance.getPunchOutLagitude().trim().equals("0.0")))) {
                                    attendance.setPunchOutLocation("Unknown location");
                                    try {
//Commented by siddhesh for Redundant code for google
//                                        GoogleResponse res = new AddressConverter().convertToAddressFromLatLong(attendance.getPunchOutLatitude(), attendance.getPunchOutLagitude());
//                                        if (res.getStatus().equals("OK")) {
//                                            Result result = res.getResults()[0];
//                                            attendance.setPunchOutLocation(result.getFormatted_address());
//                                            System.out.println("LocationPunchOut was blank and from google="+result.getFormatted_address());
//                                        }

                                        com.qlc.cacheLocation.model.CacheLocationManager cacheManager= new com.qlc.cacheLocation.model.CacheLocationManager();
                                        String address=cacheManager.getLocationFromLatLongSats(Double.parseDouble( attendance.getPunchOutLatitude()), Double.parseDouble( attendance.getPunchOutLagitude()),attendance.getUser().getId(),accountId);
                                        if(!address.trim().equals("")){
                                            attendance.setPunchOutLocation(address);
//                                            System.out.println("setPunchOutLocation > "+ attendance.getPunchOutLocation());
                                        }
                                    } catch (Exception ex) {
                                        log4jLog.info("punchWithLocationNotFoundReason"+ex);
                                        ex.printStackTrace();
                                    } 
                                }
                                return punchOutWithLocationNotFoundReason(attendance, userToken,accountId);
                            } else {
                                // Added by jyoti, to fix the issue of already punch out entry #31201
//                                if(attendance.getSdate())
                                System.out.println("already punch out, new entry updating for user > "+attendance.getUser().getId());
                                if(attendance.getPunchOutLatitude().trim().equals("0.0") && attendance.getPunchOutLagitude().trim().equals("0.0")){
                                    if(!attendance.isAutoPunchOut()){
                                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid latitude ,longitude. ", "", "");
                                    }
                                } 
                                if(locationPunchOut.equals("")){
                                    attendance.setPunchOutLocation(locationPunchIn); // if value is set in punchin instead of punchout variable
                                }
                                if ((attendance.getPunchOutLocation().trim().equals("") || attendance.getPunchOutLocation().isEmpty() || attendance.getPunchOutLocation().trim().equalsIgnoreCase("Unknown location")) && (!(attendance.getPunchOutLatitude().trim().equals("0.0") && attendance.getPunchOutLagitude().trim().equals("0.0")))) {
                                    attendance.setPunchOutLocation("Unknown location");
                                    try {
                                        com.qlc.cacheLocation.model.CacheLocationManager cacheManager= new com.qlc.cacheLocation.model.CacheLocationManager();
                                        String address=cacheManager.getLocationFromLatLongSats(Double.parseDouble( attendance.getPunchOutLatitude()), Double.parseDouble( attendance.getPunchOutLagitude()),attendance.getUser().getId(),accountId);
                                        if(!address.trim().equals("")){
                                            attendance.setPunchOutLocation(address);
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    } 
                                }
                                return punchOutWithLocationNotFoundReason(attendance, userToken,accountId);
                                // ended by jyoti, to fix the issue of already punch out entry #31201
//                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Please try again. [punchWithLocationNotFoundReason, reason : punched out entry present into attendance table]", "", ""); // commented by jyoti
                            }
                        }
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Unable to punch. Please try again. [punchWithLocationNotFoundReason, reason : attendance.getUser() == null ] ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Unable to punch. Please try again. [punchWithLocationNotFoundReason, reason : attendance == null ]", "", "");
                }
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again. ", "", "");
        }
    }

    /**
     * Called from - punchWithLocationNotFoundReason()
     * @param attendance
     * @param userToken
     * @param accountId
     * @purpose used to punch in with LocationNotFoundReason
     * @return 
     */
    private Object punchInWithLocationNotFoundReason(Attendance attendance, String userToken,int accountId) {
        int attendanceId = attendanceDao.insertAttendancePunchInWithLocationNotFoundReason(attendance, accountId);
//        System.out.println("punchInWithLocationNotFoundReason attid > "+ attendanceId + " for userToken > "+userToken);
        if (attendanceId!=0) {
                if (!attendance.getPunchInLocation().equals("Unknown location")) {
                    User user = new User();
                    Timestamp created_on = null; //Added By Mayank
                    user.setId(attendance.getUser().getId());
                    user.setLastKnownLocation(attendance.getPunchInLocation());
                    double latitude = Double.parseDouble(attendance.getPunchInLatitude());
                    user.setLatitude(latitude);
                    double lagitude = Double.parseDouble(attendance.getPunchInLangitude());
                    user.setLangitude(lagitude);
//                    user.setVersionCode(attendance.getVersionCode()); // Added by jyoti, 14-02-2018 purpose - punch out reset need version code > 46
                    userDao.updateUserLastKnownDetails(user);

                    /*if(attendance.getVersionCode()<22){
                        UsersTravelLogs usersTravelLogs = new UsersTravelLogs();
                        usersTravelLogs.setUserId(user.getId());
                        usersTravelLogs.setLatitude(user.getLatitude());
                        usersTravelLogs.setLangitude(user.getLangitude());
                        usersTravelLogs.setLocation(user.getLastKnownLocation());
                        usersTravelLogs.setSourceValue(1); //punch In 
                        usersTravelLogs.setVersionCode(attendance.getVersionCode());

                        //Added By Mayank

                        try{
                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
                                String  Date = attendance.getSdate()+" "+attendance.getSpunchInTime();
                                created_on = new Timestamp((df.parse(Date)).getTime());
//                                System.out.println("createdon Punch out "+created_on);
                         }catch(Exception e){
                                    e.printStackTrace();
                        }
                        usersTravelLogs.setCreatedOn(created_on);
                        //End By Mayank

                        usersTravelLogsDao.insertUsersTravelLog(usersTravelLogs, accountId);
                    }*/
                }

                UserKey keyCantakeCall = new UserKey();
                keyCantakeCall.setKeyValue("1");
                User userCanTakeCall = new User();
                userCanTakeCall.setId(attendance.getUser().getId());
                keyCantakeCall.setUserId(userCanTakeCall);
                keyCantakeCall.setUserKay("CanTakeCalls");
                userKeyDao.updateUserKeys(keyCantakeCall, accountId);
                if (accountId == 1) {
                    String emailAddress = fieldSenseUtils.userEmailIdForToken(userToken);
                    String url = FieldSenseUtils.getPropertyValue("ATTENDANCE_URL") + "?emailAddress=" + emailAddress + "&punchDate=" + attendancePunchDateFormate(attendance.getSdate()) + "&punchTime=" + attendancePunchTimeFormate(attendance.getSpunchInTime());
                    // Added by jyoti, as on test server attendance url giving connection timeout exception 08-08-2018
                    String checkLiveOrTestRequest = Constant.WEBSITE_PATH;
//                    System.out.println("checkLiveOrTestRequest :"+checkLiveOrTestRequest);
                    if(!checkLiveOrTestRequest.contains("test")){
                        fieldSenseUtils.fieldSenseUrlCall(url); // for attendance portal used in qlc
                    } else {
//                        System.out.println("test call, dont call attendance url");
                    }
                    // ended by jyoti
                }
                authenticationUserDao.updateLastLoggedOn(attendance.getUser().getId());
                    
                //Added by Mayank Ramaiya
                int user_accuracy = fieldSenseUtils.getUserAccuracybyId(attendance.getUser().getId());
                /*AuthenticationUser authenticationUser = new AuthenticationUser();
                authenticationUser.setuserAccuracy(user_accuracy);
                */
                int user_check_in_radius = fieldSenseUtils.getCheckInRadiusbyId(attendance.getUser().getId());
                //authenticationUser.setCheckInRadius(user_check_in_radius);
                               
                JSONObject json = new JSONObject();
                json.put("userAccuracy", user_accuracy);
                json.put("checkInRadius", user_check_in_radius);
                json.put("attendanceId", attendanceId);
                //End By Mayank Ramaiya
               
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Successfully Punch In.", "", json);
            } else {
//                System.out.println("punchInWithLocationNotFoundReason pls try agn");
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Please try again. [punchInWithLocationNotFoundReason, reason : attid zero, could not insert data into attendance table]", "", "");
            }
        
    }

    /**
     * Called from - punchWithLocationNotFoundReason()
     * @param attendance
     * @param userToken
     * @param accountId
     * @purpose used to punch out with LocationNotFoundReason
     * @return 
     */
    private Object punchOutWithLocationNotFoundReason(Attendance attendance, String userToken,int accountId) {
            String punchInTime = "";
            String punchInDate = attendance.getSdate();
            Attendance punchInAttendance=null;
            boolean oldAPIVerison=false;
            /*if(attendance.getVersionCode()<22){
                oldAPIVerison=true;
                punchInTime =  attendanceDao.getPunchInTime(attendance.getUser().getId(), attendance.getSdate(), accountId);
            }else{*/
//            System.out.println("punchOutWithLocationNotFoundReason attid > "+attendance.getId());
                punchInAttendance=attendanceDao.selectAttendanceUsingId(attendance.getId(), accountId);
                punchInTime = punchInAttendance.getPunchInTime().toString();
                punchInDate = punchInAttendance.getDate().toString();
            //}
           // String punchInTime = attendanceDao.getPunchInTime(attendance.getUser().getId(), attendance.getSdate(), accountId);
//           System.out.println("punchOutWithLocationNotFoundReason, punchInTime >> "+punchInTime);
            if (punchInTime != null) {
                if (isPunchOutValid(punchInDate,attendance.getSdate(),punchInTime, attendance.getSpunchInTime())) {
                //if (isPunchOutValid(punchInTime, attendance.getSpunchInTime())) {
                    //Added By Mayank
                    Timestamp created_on = null; 
                     try{
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
                            String  Date = attendance.getSdate()+" "+attendance.getSpunchInTime();
                            created_on = new Timestamp((df.parse(Date)).getTime());
                            //System.out.println("createdon Punch out "+created_on);
                        }catch(Exception e){
                            log4jLog.info("punchOutWithLocationNotFoundReason >> " +e);
                            e.printStackTrace();
                        }
                      //End By Mayank
//                     System.out.println("punchInDate > "+punchInDate + " ,getSdate > "+ attendance.getSdate() + " ,punchInTime > "+punchInTime + " ,getSpunchInTime > "+ attendance.getSpunchInTime());
                        boolean punchUpdate=false;
                        if(oldAPIVerison){
                            punchUpdate=attendanceDao.updateAttendancePunchOutWithLocationNotFoundReason(attendance, accountId);
//                            System.out.println("punchOutWithLocationNotFoundReason if oldAPIVerison punchInTime > "+punchInTime + " punchindate > "+punchInDate);
                        }else{ 
                            // added by jyoti
                            System.out.println("check if punch out status is 1 or 0 >> "+ punchInAttendance.getAttendanceStatus());
                            if (punchInAttendance.getAttendanceStatus() == 1) { // already punch out
                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String newPunchOutDateTime = attendance.getSdate() + " " + attendance.getSpunchInTime();
                                String oldPunchOutDateTime = punchInAttendance.getPunchOutDate() + " " + punchInAttendance.getPunchOutTime();
                                System.out.println("newPunchOutDate : " + newPunchOutDateTime);
                                System.out.println("oldPunchOutDate : " + oldPunchOutDateTime);
                                try {
                                    if(newPunchOutDateTime.equals(oldPunchOutDateTime)){
                                        punchUpdate = true; // no need to update the data if its same, so just update the punchUpdate
                                    }
                                    else if (df.parse(newPunchOutDateTime).after(df.parse(oldPunchOutDateTime))) {
                                        System.out.println("new punch out greater than old punch out time");
                                        punchUpdate=attendanceDao.updateAttendancePunchOutWithLocationNotFoundReasonUsingId(attendance, accountId);
                                    }
                                } catch (ParseException ex) {
                                    ex.printStackTrace();
                                }

                            } else{ // ended by jyoti
                                punchUpdate=attendanceDao.updateAttendancePunchOutWithLocationNotFoundReasonUsingId(attendance, accountId);
//                             System.out.println("punchOutWithLocationNotFoundReason else punchInTime > "+punchInTime + " punchindate > "+punchInDate);
                            }
                        }
//                        System.out.println("punchUpdate > "+ punchUpdate + " for attid > "+attendance.getId());
                        if(punchUpdate){
                        //if (attendanceDao.updateAttendancePunchOutWithLocationNotFoundReason(attendance, accountId)) {
                            /*String stopTimeoutFailed="";
        ;                   if(!attendanceDao.updateAttendanceTimeOutStopTime(attendance,created_on, attendance.getUser().getId(),accountId)){
                                stopTimeoutFailed="But Failed to Stop Timeout.";
                            }*/
                            if (!attendance.getPunchOutLocation().equals("Unknown location")) {

                                User user = new User();
                                user.setId(attendance.getUser().getId());
                                user.setLastKnownLocation(attendance.getPunchOutLocation());
                                double latitude = Double.parseDouble(attendance.getPunchOutLatitude());
                                user.setLatitude(latitude);
                                double lagitude = Double.parseDouble(attendance.getPunchOutLagitude());
                                user.setLangitude(lagitude);
//                                user.setVersionCode(attendance.getVersionCode()); // Added by jyoti, 14-02-2018 purpose - punch out reset need version code > 46
                                userDao.updateUserLastKnownDetails(user);
                                
                                /*if(attendance.getVersionCode()<22){
                                    UsersTravelLogs usersTravelLogs = new UsersTravelLogs();
                                    usersTravelLogs.setUserId(user.getId());
                                    usersTravelLogs.setLatitude(user.getLatitude());
                                    usersTravelLogs.setLangitude(user.getLangitude());
                                    usersTravelLogs.setLocation(user.getLastKnownLocation());
                                    usersTravelLogs.setSourceValue(2);
                                    usersTravelLogs.setVersionCode(attendance.getVersionCode());
                                    //Add by Mayank

                                    usersTravelLogs.setCreatedOn(created_on);
                                    //End By Mayank
                                    usersTravelLogsDao.insertUsersTravelLog(usersTravelLogs, accountId);
                                }*/

                            }
                            UserKey keyCantakeCall = new UserKey();
                            keyCantakeCall.setKeyValue("0");
                            User userCanTakeCall = new User();
                            userCanTakeCall.setId(attendance.getUser().getId());
                            keyCantakeCall.setUserId(userCanTakeCall);
                            keyCantakeCall.setUserKay("CanTakeCalls");

                            UserKey keyInMeeting = new UserKey();
                            keyInMeeting.setKeyValue("0");
                            User userInMeeting = new User();
                            userInMeeting.setId(attendance.getUser().getId());
                            keyInMeeting.setUserId(userInMeeting);
                            keyInMeeting.setUserKay("InMeeting");

                            userKeyDao.updateUserKeys(keyCantakeCall, accountId);
                            userKeyDao.updateUserKeys(keyInMeeting, accountId);
                            if (accountId == 1) {
                                String emailAddress = fieldSenseUtils.userEmailIdForToken(userToken);
                                String url = FieldSenseUtils.getPropertyValue("ATTENDANCE_URL") + "?emailAddress=" + emailAddress + "&punchDate=" + attendancePunchDateFormate(attendance.getSdate()) + "&punchTime=" + attendancePunchTimeFormate(attendance.getSpunchInTime());
                                // Added by jyoti, as on test server attendance url giving connection timeout exception 08-08-2018
                                String checkLiveOrTestRequest = Constant.WEBSITE_PATH;
//                                System.out.println("checkLiveOrTestRequest :" + checkLiveOrTestRequest);
                                if (!checkLiveOrTestRequest.contains("test")) {
                                    fieldSenseUtils.fieldSenseUrlCall(url); // for attendance portal used in qlc
                                } else {
//                                    System.out.println("test call, dont call attendance url");
                                }
                                // ended by jyoti
                            }
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Successfully Punch Out.", "", "");
                        } else {
//                            System.out.println("punchOutWithLocationNotFoundReason please try agn");
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Please try once again. [punchOutWithLocationNotFoundReason, reason : punch out update failed ] ", "", "");
                        }
                       
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Punch out time should not be less than punch in time.", "", "");
                }
            } else {
//                System.out.println("2 punchOutWithLocationNotFoundReason please try agn");
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Please try again. [punchOutWithLocationNotFoundReason, reason : punchInTime == null ]", "", "");
            }
       
    }
    
    /**
     *
     * @param punchInDate
     * @param punchOutDate
     * @param punchInTime
     * @param punchOutTime
     * @return
     */
    public boolean isPunchOutValid(String punchInDate,String punchOutDate,String punchInTime, String punchOutTime) {
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try{
            //java.util.Date punchIn= format.parse(punchInDate+" "+punchInTime);
            //java.util.Date punchOut= format.parse(punchOutDate+" "+punchOutTime);
            Timestamp punchInTimestamp = Timestamp.valueOf(punchInDate.trim()+" "+punchInTime.trim());
            Timestamp punchOutTimestamp = Timestamp.valueOf(punchOutDate.trim()+" "+punchOutTime.trim());
            long punchInMills = punchInTimestamp.getTime();
            long punchOutMills = punchOutTimestamp.getTime();
            if(punchInMills>punchOutMills){
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            log4jLog.info("isPunchOutValid :"+e.getMessage() +" punchIn="+punchInDate +" "+punchInTime+" punchOut="+punchOutDate+" "+punchOutTime+":::");
            return false;
        }
        return true;
    }

    /**
     *
     * @param punchInTime
     * @param punchOutTime
     * @return
     */
    public boolean isPunchOutValid(String punchInTime, String punchOutTime) {
        String[] punchInSplit = punchInTime.split(":");
        String[] punchOutSplit = punchOutTime.split(":");
        int punchInHours = Integer.parseInt(punchInSplit[0]);
        int punchOutHours = Integer.parseInt(punchOutSplit[0]);
        if (punchInHours < punchOutHours) {
            return true;
        } else if (punchInHours == punchOutHours) {
            int punchInMinutes = Integer.parseInt(punchInSplit[1]);
            int punchOutMinutes = Integer.parseInt(punchOutSplit[1]);
            if (punchInMinutes < punchOutMinutes) {
                return true;
            } else if (punchInMinutes == punchOutMinutes) {
                int punchInSeconds = Integer.parseInt(punchInSplit[2]);
                int punchOutSeconds = Integer.parseInt(punchOutSplit[2]);
                if (punchInSeconds < punchOutSeconds) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     *
     * @param date
     * @return
     */
    public String attendancePunchDateFormate(String date) {
        String[] punchDateSplit = date.split("-");
        date = punchDateSplit[0] + punchDateSplit[1] + punchDateSplit[2];
        return date;
    }

    /**
     *
     * @param time
     * @return
     */
    public String attendancePunchTimeFormate(String time) {
        String[] punchTimeSplit = time.split(":");
        int hours = Integer.parseInt(punchTimeSplit[0]) + 5;
        int minutes = Integer.parseInt(punchTimeSplit[1]) + 30;
        if (minutes > 59) {
            hours = hours + 1;
            minutes = minutes - 60;
        }
        String sHours = "" + hours;
        if (hours < 10) {
            sHours = "0" + hours;
        }
        String sMinutes = "" + minutes;
        if (minutes < 10) {
            sMinutes = "0" + minutes;
        }

        time = "" + sHours + sMinutes + punchTimeSplit[2];
        return time;
    }

    /**
     * @param userId
     * @param userToken
     * @param attendanceId
     * @return 
     * @purpose : update punchout details and status
     * @author : anuja
     * @date : 24 Jul,2015
     */
    public Object updatePunchoutStatus(String userToken, int userId,int attendanceId) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (attendanceDao.updatePunchOutStatus(userId, accountId,attendanceId)) {
                    // Added by Jyoti, 21-12-2017 - for sending push notification with last punch in time
                    try{
                        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        java.util.HashMap hMap_punchinout = fieldSenseUtils.getLastPunchInOutData(userId,accountId);
                        String punchInDateTime = (String) hMap_punchinout.get("secondLastPunchInDate")+" "+(String) hMap_punchinout.get("secondLastPunchIn");
                        java.util.Date punchInDateTimeMillis = dateTimeFormat.parse(punchInDateTime);
                        
                        java.util.HashMap gcmInfo = fieldSenseUtils.getGcmUserInfo(userId);
                        String gcmId = (String) gcmInfo.get("gcmId");
                        int deviceOS = (Integer) gcmInfo.get("deviceOS");
//                        System.out.println(punchInDateTime+ ", Punch out reset done. punch in time : "+punchInDateTimeMillis.getTime());
                        int appVersion = (Integer) gcmInfo.get("appVersion"); // added by jyoti
                        if(appVersion > Integer.parseInt(Constant.APP_VERSION_CHECK) && deviceOS == 1){
                            if(!(gcmId.equals("0") || gcmId.isEmpty())) {
                                Map<String, Object> m_data = new HashMap<String, Object>();
                                m_data.put("type", "resetPunchOut");
                                m_data.put("punchInTime", punchInDateTimeMillis.getTime());
                                m_data.put("isModified", "true"); // need to compare with created_on - updated_on, in db/modal field is missing
//                                System.out.println("m_data : "+ m_data);

                                com.qlc.fieldsense.utils.PushNotificationManager push= new com.qlc.fieldsense.utils.PushNotificationManager();
                                push.addEditNotification(m_data, gcmId, deviceOS,null);
                            }
                        }
                        //Added by sanchita   ---//optimization for ios notification
                        else if(deviceOS == 2){
                            if(!(gcmId.equals("0") || gcmId.isEmpty())) {
                                JSONObject m_data = new JSONObject();
                                try { 
                               
                                m_data.put("punchInTime", punchInDateTimeMillis.getTime());
                                m_data.put("isModified", "true"); // need to compare with created_on - updated_on, in db/modal field is missing//                               
                                
                                // payload send in notification, Added by sanchita                                
                                PushNotificationPayload payload = PushNotificationBigPayload.complex();
                                payload.addAlert("Your Punch-Out record has been updated. Please refresh your app.");  //To show message in notification panel                                                                                          
                                payload.addCustomDictionary("type", "resetPunchOut"); 
                                payload.addCustomDictionary("objectData",m_data.toString());
                               
//                                System.out.println("your punchout payload >> "+payload.toString());
                                com.qlc.fieldsense.utils.PushNotificationManager push= new com.qlc.fieldsense.utils.PushNotificationManager();
                                push.addEditNotification(m_data, gcmId, deviceOS,payload);
//                                System.out.println("sendupdatePunchoutStatusNotificationToUsersOfAccount device == "+deviceOS+", gcmId : "+gcmId);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }//ended by sanchita
                            
                        } else{
//                            System.out.println("updatePunchoutStatus, no notification");
                        }
                    
                    } catch(Exception e){
                        log4jLog.info("updatePunchoutStatus for userId : " + userId);
                        e.printStackTrace();
                    }
                    // Ended by Jyoti
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "PunchOut Status updated successfully.", "", "");
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Please try again. [updatePunchoutStatus : reset update failed]", "", "");
                }  
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again. ", "", "");
        }
    }
    
    /**
     * @param userToken
     * @return 
     * @purpose : update only punchout status
     * @author : anuja
     * @date : 24 Jul,2015
     */
    public Object updateOnlyPunchOutStatus(String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                int userId = fieldSenseUtils.userIdForToken(userToken);
                if (attendanceDao.updateOnlyPunchOutStatus(userId, accountId)) {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "PunchOut Status updated successfully.", "", "");
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Please try again. [updateOnlyPunchOutStatus : punchout update failed]", "", "");
                }
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again. ", "", "");
        }
    }
    
    /**
     *
     * @param attendanceTimeout
     * @param userToken
     * @return
     */
    public Object startTimeoutForAttendance(AttendanceTimeout attendanceTimeout,String userToken){
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                //int userId = fieldSenseUtils.userIdForToken(userToken);
                if(attendanceTimeout.getStartTimeout().toString().startsWith("1970")){
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Failed To Start Timeout , Invalid Timeout Start Time.", "", "");
                }
                
                if(attendanceTimeout.getLatitude()!=0 || attendanceTimeout.getLongitude()!=0){
                    if (attendanceTimeout.getLocation().trim().equals("") || attendanceTimeout.getLocation().trim().equalsIgnoreCase("Unknown location") ) {
                            attendanceTimeout.setLocation("Unknown location");
                            try {
                                com.qlc.cacheLocation.model.CacheLocationManager cacheManager= new com.qlc.cacheLocation.model.CacheLocationManager();
                                String address=cacheManager.getLocationFromLatLongSats(attendanceTimeout.getLatitude(), attendanceTimeout.getLongitude(),attendanceTimeout.getId(),accountId);
                                if(!address.trim().equals("")){
                                    attendanceTimeout.setLocation(address);
                                }
                            } catch (Exception ex) {
                                log4jLog.info("startTimeoutForAttendance"+ex);
//                                ex.printStackTrace();
                            }     
                    }
                    /*if(attendanceTimeout.getVersionCode()<22){
                        UsersTravelLogs usersTravelLogs = new UsersTravelLogs();
                        usersTravelLogs.setUserId(attendanceTimeout.getUserId());
                        usersTravelLogs.setLatitude(attendanceTimeout.getLatitude());
                        usersTravelLogs.setLangitude(attendanceTimeout.getLongitude());
                        usersTravelLogs.setLocation(attendanceTimeout.getLocation());
                        usersTravelLogs.setSourceValue(4); // for timeoutstop it will be 5 , for timeout start 4
                        usersTravelLogs.setCreatedOn(attendanceTimeout.getStartTimeout());
                        usersTravelLogs.setVersionCode(attendanceTimeout.getVersionCode());
                        usersTravelLogsDao.insertUsersTravelLog(usersTravelLogs, accountId);
                    }*/
                }
                
                boolean res=attendanceDao.startTimeoutForAttendance(attendanceTimeout,accountId);
                if(res){
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Timeout Started Successfully.", "", "");
                }else{
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Failed To Start Timeout .", "", "");
                }
             
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again. ", "", "");
        }
    }
    
    /**
     *
     * @param attendanceTimeout
     * @param userToken
     * @return
     */
    public Object stopTimeoutForAttendance(AttendanceTimeout attendanceTimeout,String userToken){
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                //int userId = fieldSenseUtils.userIdForToken(userToken);
                if(attendanceTimeout.getStopTimeout().toString().startsWith("1970")){
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Failed To Stop Timeout , Invalid Timeout Stop Time.", "", "");
                }
                
                AttendanceTimeout startTimeout=attendanceDao.selectAttendanceTimeout(attendanceTimeout.getUserId(), attendanceTimeout.getStrTimeoutDate(), attendanceTimeout.getSerialNumber(), accountId);
                
                if(!isTimeoutValid(startTimeout,attendanceTimeout)){
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Stop Timeout can't be less than Start Timeout.", "", "");
                }
                
                if(attendanceTimeout.getLatitude()!=0 || attendanceTimeout.getLongitude()!=0){
                    if (attendanceTimeout.getLocation().trim().equals("") || attendanceTimeout.getLocation().trim().equalsIgnoreCase("Unknown location") ) {
                            attendanceTimeout.setLocation("Unknown location");
                            try {
                                com.qlc.cacheLocation.model.CacheLocationManager cacheManager= new com.qlc.cacheLocation.model.CacheLocationManager();
                                String address=cacheManager.getLocationFromLatLongSats(attendanceTimeout.getLatitude(), attendanceTimeout.getLongitude(),attendanceTimeout.getId(),accountId);
                                if(!address.trim().equals("")){
                                    attendanceTimeout.setLocation(address);
                                }
                            } catch (Exception ex) {
                                log4jLog.info("stopTimeoutForAttendance"+ex);
//                                ex.printStackTrace();
                            }     
                    }
                    
                    /*if(attendanceTimeout.getVersionCode()<22){
                        UsersTravelLogs usersTravelLogs = new UsersTravelLogs();
                        usersTravelLogs.setUserId(attendanceTimeout.getUserId());
                        usersTravelLogs.setLatitude(attendanceTimeout.getLatitude());
                        usersTravelLogs.setLangitude(attendanceTimeout.getLongitude());
                        usersTravelLogs.setLocation(attendanceTimeout.getLocation());
                        usersTravelLogs.setSourceValue(5); // for timeoutstop it will be 5 , for timeout start 4
                        usersTravelLogs.setCreatedOn(attendanceTimeout.getStopTimeout());
                        usersTravelLogs.setVersionCode(attendanceTimeout.getVersionCode());
                        usersTravelLogsDao.insertUsersTravelLog(usersTravelLogs, accountId);
                    }*/
                }
                
                boolean res=attendanceDao.stopTimeoutForAttendance(attendanceTimeout,accountId);

                if(res){
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Timeout Resumed Successfully.", "", "");
                }else{
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Failed To Resume Timeout .", "", "");
                }
             
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again. ", "", "");
        }
    }
    
    private boolean isTimeoutValid(AttendanceTimeout startTimeout,AttendanceTimeout stopTimeout){
        Timestamp stop=stopTimeout.getStopTimeout();
        Timestamp start=startTimeout.getStartTimeout();
        if(stop.compareTo(start)==-1){
            return false;
        }else{
            return true;
        }
    }
    
    /**
     *
     * @param userId
     * @param timeoutDate
     * @param userToken
     * @return
     */
    public Object selectAllTimeoutForAttendance(int userId,String timeoutDate,String userToken){
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List timeOutList=attendanceDao.selectAllAttendanceTimeoutForDate( userId, timeoutDate,  accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Timeout report list .", "", timeOutList);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again. ", "", "");
        }
    }
    
    /**
    * @Created by Awaneesh for Coverting timezoneoffset from int to String for Attendance sent from Android
    * @param (int)offset
    * @param (String)offset
    */
    private String convertTimeZoneOffsetToStringForMobile(int offset){
            String timeZoneOffset="";
            offset=(-1)*offset; // from android offset comes as positive in case of IST wherever we required
            String addSubSign="+";
            
            if(offset<0){
                addSubSign="-";
                // make offset positive for hh and mm calculations
                offset=(-1)*offset;
            }
            
            int hh = offset/60;
            int mm = offset%60;
            String shh = String.valueOf(hh);
            if(hh <10){
                shh="0"+shh;
            }
            String smm = String.valueOf(mm);
            if(mm <10){
                smm="0"+smm;
            }
            timeZoneOffset=addSubSign+shh+":"+smm;
            return timeZoneOffset;
    }
}
