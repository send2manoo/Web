/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.usersTravelLogs.model;

import com.qlc.fieldsense.appointments.model.Appointment;
import com.qlc.fieldsense.report.model.Attendance;
import com.qlc.fieldsense.user.dao.UserDao;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.usersTravelLogs.dao.UsersTravelLogsDao;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author anuja
 */
public class UsersTravelLogsManager {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("UsersTravelLogsManager");
    UsersTravelLogsDao usersTravelLogsDao = (UsersTravelLogsDao) GetApplicationContext.ac.getBean("usersTravelLogsDaoImpl");
    FieldSenseUtils util = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
     UserDao userDao = (UserDao) GetApplicationContext.ac.getBean("userDaoImpl"); // by nikhil
     /**
     * 
     * @param usersTravelLogs
     * @param token
     * @return 
     * @purpose Used to create user travel logs
     */
     public Object createUsersTravelLogs(UsersTravelLogs usersTravelLogs, String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                int trvelLogId = usersTravelLogsDao.insertUsersTravelLog(usersTravelLogs, accountId);
                if (trvelLogId != 0) {
                    usersTravelLogs = usersTravelLogsDao.selectUsersTravelLog(trvelLogId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.USER_TRAVEL_LOG_CREATED, " usersTravelLogs ", usersTravelLogs);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.USER_TRAVEL_LOG_NOT_CREATED, "", "");
                }   
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /**
     * @param token
     * @return list of travels logs
     */
    public Object selectUsersTravelLogs(String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                Integer userId = util.userIdForToken(token);
                List<UsersTravelLogs> usersTravelLogsList = usersTravelLogsDao.selectUsersTravelLogs(userId, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "usersTravelLogsList", usersTravelLogsList);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

        /**
     * @added by nikhil
     * @param userId
     * @param date
     * @param token
     * @return 
     * @purpose Used to get details of travels logs of specified user for specified date. Date formate is yyyy-MM-dd
     */
    public Object selectUsersTravelLogs1(int userId, String date, String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateUtil = new Date();
                String timeAfter24Hours = "";
                try {
                    dateUtil = parser.parse(date);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateUtil);
                    calendar.add(Calendar.HOUR_OF_DAY, 24);
                    dateUtil = calendar.getTime();
                    timeAfter24Hours = parser.format(dateUtil);
//                    System.out.println("date****"+date+ "***" +timeAfter24Hours);
                } catch (ParseException ex) {
                    log4jLog.info(" selectAppointments " + ex);
                }
               System.out.println("date $$ "+date);
                List<UsersTravelLogs> usersTravelLogsList = usersTravelLogsDao.selectUsersTravelLogsDateWise(userId, date, timeAfter24Hours, accountId);
                List<Attendance> attendanceList = usersTravelLogsDao.attendanceDateWise(userId, date, timeAfter24Hours, accountId);
                List<Appointment> appointmentList = usersTravelLogsDao.appointmentsDateWise(userId, date, timeAfter24Hours, accountId);
                 User userDetails = userDao.selectUser(userId);
                   List<Appointment> appointmentList1 = usersTravelLogsDao.selectAppointForUser(userId, date, timeAfter24Hours, accountId); //added by nikhil :- fresh change
                    List<Attendance> attendanceListPreviousDate = usersTravelLogsDao.attendanceDateWise(userId, date, date, accountId); //added by nikhil :- for previous date punch IN-OUT  Details
//                   System.out.println("appointmentList1.size "+appointmentList1.size());
                List complDayList=new ArrayList();
                List<UsersTravelLogs> userTravelledreports = new ArrayList<UsersTravelLogs>();
                for (int i = 0; i < usersTravelLogsList.size(); i++) {
                    UsersTravelLogs travelLog = usersTravelLogsList.get(i);
                    double distance;
                    if (i == 0) {
                        distance = 0;
                    } else {
                        UsersTravelLogs travelLogPrevios = usersTravelLogsList.get(i-1);
                        distance = FieldSenseUtils.travelDistance(travelLogPrevios.getLatitude(), travelLogPrevios.getLangitude(), travelLog.getLatitude(), travelLog.getLangitude());
                        distance = Math.round (distance * 10000.0) / 10000.0;
                    }
                    travelLog.setTravelDistance(distance);
                    userTravelledreports.add(travelLog);
                }
             
                complDayList.add(userTravelledreports);
                complDayList.add(attendanceList);
                complDayList.add(appointmentList);
                   complDayList.add(userDetails);
                       complDayList.add(appointmentList1);
                        complDayList.add(attendanceListPreviousDate);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "usersTravelLogsList", complDayList);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }


    
    /**
     * 
     * @param userId
     * @param date
     * @param token
     * @return 
     * @purpose Used to get details of travels logs of specified user for specified date. Date formate is yyyy-MM-dd
     */
    public Object selectUsersTravelLogs(int userId, String date, String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateUtil = new Date();
                String timeAfter24Hours = "";
                try {
                    dateUtil = parser.parse(date);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateUtil);
                    calendar.add(Calendar.HOUR_OF_DAY, 24);
                    dateUtil = calendar.getTime();
                    timeAfter24Hours = parser.format(dateUtil);
//                    System.out.println("date****"+date+ "***" +timeAfter24Hours);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                    log4jLog.info(" selectAppointments " + ex);
                }
                List<UsersTravelLogs> usersTravelLogsList = usersTravelLogsDao.selectUsersTravelLogsDateWise(userId, date, timeAfter24Hours, accountId);
                List<Attendance> attendanceList = usersTravelLogsDao.attendanceDateWise(userId, date, timeAfter24Hours, accountId);
                List<Appointment> appointmentList = usersTravelLogsDao.appointmentsDateWise(userId, date, timeAfter24Hours, accountId);
                List complDayList=new ArrayList();
                List<UsersTravelLogs> userTravelledreports = new ArrayList<UsersTravelLogs>();
                for (int i = 0; i < usersTravelLogsList.size(); i++) {
                    UsersTravelLogs travelLog = usersTravelLogsList.get(i);
                    double distance;
                    if (i == 0) {
                        distance = 0;
                    } else {
                        UsersTravelLogs travelLogPrevios = usersTravelLogsList.get(i-1);
                        distance = FieldSenseUtils.travelDistance(travelLogPrevios.getLatitude(), travelLogPrevios.getLangitude(), travelLog.getLatitude(), travelLog.getLangitude());
                        distance = Math.round (distance * 10000.0) / 10000.0;
                    }
                    travelLog.setTravelDistance(distance);
                    userTravelledreports.add(travelLog);
                }
                complDayList.add(userTravelledreports);
                complDayList.add(attendanceList);
                complDayList.add(appointmentList);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "usersTravelLogsList", complDayList);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /**
     * 
     * @param location
     * @param token
     * @return 
     * @purpose Used to get details of travels logs based on location
     */
    public Object selectUsersTravelLog(String location, String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                Integer userId = util.userIdForToken(token);
                List<UsersTravelLogs> usersTravelLogsList = usersTravelLogsDao.selectUsersTravelLogs(userId, location, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "usersTravelLogsList", usersTravelLogsList);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }
}