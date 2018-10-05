/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.usersTravelLogs.dao;

import com.qlc.fieldsense.appointments.model.Appointment;
import com.qlc.fieldsense.report.model.Attendance;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.usersTravelLogs.model.UsersTravelLogs;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author anuja
 */
public interface UsersTravelLogsDao {

    /**
     *
     * @param usersTravelLogs
     * @param accountId
     * @return
     */
    public int insertUsersTravelLog(UsersTravelLogs usersTravelLogs, int accountId);

    /**
     *
     * @param travelLogId
     * @param accountId
     * @return
     */
    public UsersTravelLogs selectUsersTravelLog(int travelLogId, int accountId);

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public List<UsersTravelLogs> selectUsersTravelLogs(Integer userId, int accountId);

    /**
     *
     * @param userId
     * @param accountId
     * @param created_date
     * @return
     */
    public List<UsersTravelLogs> selectUsersTravelLogsLastLocation(Integer userId, int accountId, String created_date);

    /**
     *
     * @param userId
     * @param location
     * @param accountId
     * @return
     */
    public List<UsersTravelLogs> selectUsersTravelLogs(Integer userId, String location, int accountId);

    /**
     *
     * @param userId
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    public List<UsersTravelLogs> selectUsersTravelLogsDateWise(Integer userId, String fromDate, String toDate, int accountId);

    /**
     *
     * @param userId
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    public List<Attendance> attendanceDateWise(Integer userId, String fromDate, String toDate, int accountId);

    /**
     *
     * @param userId
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    public List<Appointment> appointmentsDateWise(Integer userId, String fromDate, String toDate, int accountId);
     /**
     *
     * @param userId
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    public List<Appointment> selectAppointForUser(int userId, String fromDate, String toDate, int accountId); 
    /**
     *
     * @param fromDate
     * @param toDate
     * @param userId
     * @param accountId
     * @return
     */
    public List<UsersTravelLogs> selectUserTodaysApointmentCustomerLocations(String fromDate, String toDate, int userId, int accountId);

    public int insertUsersTravelLogBatch(List<UsersTravelLogs> usersTravelLogs, int accountId);
    
    public int insertIntoCacheInsertBatch(List<UsersTravelLogs> UsersTravelLogs, int accountId);
    
    public int insertIntoLocationNotFound(List<UsersTravelLogs> UsersTravelLogs, int accountId);

    public boolean insertIntoTravelLogForUnresolvedAddress(UsersTravelLogs UsersTravelLogs);
    
    public boolean insertTravelLogsSingle(UsersTravelLogs user,int account); //  @Added by siddhesh, 09-01-2018
}
