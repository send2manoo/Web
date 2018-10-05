/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.attendance.dao;

import com.qlc.fieldsense.attendance.model.Attendance;
import com.qlc.fieldsense.attendance.model.AttendanceTimeout;
import com.qlc.fieldsense.attendance.model.PunchDateTime;
import com.qlc.fieldsense.mobile.model.LeftSliderMenu;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author mayank
 * @modified by :anuja
 */
public interface AttendanceDao {

    /**
     *
     * @param attendance
     * @param accountId
     * @return
     */
    boolean insertAttendancePunchIn(Attendance attendance, int accountId);

    /**
     *
     * @param attendance
     * @param accountId
     * @return
     */
    boolean updateAttendancePunchOut(Attendance attendance, int accountId);

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    List<Attendance> selectAttendance(int userId, int accountId);
    
    public List<LeftSliderMenu> getPunchList(int userId, int accountId); //added by manohar

    public List<LeftSliderMenu> getCheckList(int userId, int accountId); // added by manohar
    
    public List<LeftSliderMenu> getStatus(int userId, int accountId); // added by manohar
    /**
     *
     * @param userId
     * @param date
     * @param accountId
     * @return
     */
    Attendance selectAttendance(int userId, Date date, int accountId);

    /**
     *
     * @param date
     * @param accountId
     * @return
     */
    Attendance selectAttendance(Date date, int accountId);

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public boolean isUsersFirstPunch(int userId, int accountId);
    
    /**
     *
     * @param attendanceId
     * @param accountId
     * @return
     */
    public boolean isUsersFirstPunchUsingId(int attendanceId, int accountId);

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public String userPunchInTime(int userId, int accountId);

    /**
     *
     * @param userId
     * @param date
     * @param accountId
     * @return
     */
    public PunchDateTime userPunchInTimeDate(int userId, String date, int accountId);

    /**
     *
     * @param userId
     * @param date
     * @param accountId
     * @return
     */
    public boolean isUsersFirstPunch(int userId, String date, int accountId);
    
    /**
     *
     * @param userId
     * @param date
     * @param time
     * @param timeZoneOffset
     * @param accountId
     * @return
     */
    public boolean isUsersFirstPunch(int userId, String date,String time,String timeZoneOffset, int accountId);
  
    /**
     *
     * @param userId
     * @param date
     * @param accountId
     * @return
     */
    public int getAttendanceId(int userId, String date, int accountId);

    /**
     *
     * @param attendance
     * @param accountId
     * @return
     */
    public int insertAttendancePunchInWithLocationNotFoundReason(Attendance attendance, int accountId);

    /**
     *
     * @param attendance
     * @param accountId
     * @return
     */
    public boolean updateAttendancePunchOutWithLocationNotFoundReason(Attendance attendance, int accountId);

    /**
     *
     * @param attendance
     * @param accountId
     * @return
     */
    public boolean updateAttendancePunchOutWithLocationNotFoundReasonUsingId(Attendance attendance, int accountId);
    
    /**
     *
     * @param userId
     * @param date
     * @param accountId
     * @return
     */
    public String getPunchInTime(int userId, String date, int accountId);
    
    /**
     *
     * @param attendanceId
     * @param accountId
     * @return
     */
    public String getPunchInTimeUsingId(int attendanceId, int accountId);
    
    /**
     *
     * @param userId
     * @param accountId
     * @param attendanceId
     * @return
     */
    boolean updatePunchOutStatus(int userId, int accountId,int attendanceId);
    
    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public boolean updateOnlyPunchOutStatus(int userId, int accountId);
    
    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public boolean getPunchStatus(int userId, int accountId);
    
    /**
     *
     * @param attendanceId
     * @param accountId
     * @return
     */
    public boolean getPunchStatusUsingId(int attendanceId, int accountId);
    
    /**
     *
     * @param userId
     * @param accountId
     * @param date
     * @return
     */
    public boolean getPunchStatusByDate(int userId, int accountId,String date);
    
    /**
     *
     * @param attendanceTimeout
     * @param accountId
     * @return
     */
    public boolean startTimeoutForAttendance(AttendanceTimeout attendanceTimeout,int accountId);
    
    /**
     *
     * @param attendanceTimeout
     * @param accountId
     * @return
     */
    public boolean stopTimeoutForAttendance(AttendanceTimeout attendanceTimeout,int accountId);
    
    /**
     *
     * @param userId
     * @param date
     * @param serialNumber
     * @param accountId
     * @return
     */
    public AttendanceTimeout selectAttendanceTimeout(int userId,String date,int serialNumber,int accountId);
    
    /**
     *
     * @param userId
     * @param date
     * @param accountId
     * @return
     */
    public AttendanceTimeout selectAttendanceTimeoutForDate(int userId,String date, int accountId);
    
    /**
     *
     * @param userId
     * @param date
     * @param accountId
     * @return
     */
    public List<AttendanceTimeout> selectAllAttendanceTimeoutForDate(int userId,String date, int accountId);
    
    /**
     *
     * @param userId
     * @param date
     * @param accountId
     * @return
     */
    public AttendanceTimeout selectLastTimeoutOfDayForUser(int userId,String date, int accountId);
    
    /**
     *
     * @param attendance
     * @param stopTime
     * @param userId
     * @param accountId
     * @return
     */
    public boolean updateAttendanceTimeOutStopTime(Attendance attendance,Timestamp stopTime,int userId,int accountId);

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    public Attendance selectAttendanceUsingId(int id, int accountId);
    
    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public PunchDateTime userLastPunchDateTime(int userId,int accountId);
    
}
