/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.attendance.dao;

import com.qlc.fieldsense.attendance.model.Attendance;
import com.qlc.fieldsense.attendance.model.AttendanceTimeout;
import com.qlc.fieldsense.attendance.model.PunchDateTime;
import com.qlc.fieldsense.mobile.model.LeftSliderMenu;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.userKey.dao.UserKayDaoImpl;
import com.qlc.fieldsense.userKey.model.UserKey;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import static com.qlc.fieldsense.utils.FieldSenseUtils.getJdbcTemplateForAccount;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author mayank
 * @modified by :anuja
 */
public class AttendanceDaoImpl implements AttendanceDao {

    private static final Logger log4jLog = Logger.getLogger("FieldSenseUtils");
    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
            return jdbcTemplate;
        }
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    /**
     *
     * @param attendance
     * @param accountId
     * @return
     */
    @Override
    public boolean insertAttendancePunchIn(Attendance attendance, int accountId) {
        String query = "INSERT INTO attendances (user_id_fk,punch_date,punch_in,punch_in_location,punch_in_latitude,punch_in_langitude,status,created_on) VALUES(?,CURDATE(),CURTIME(),?,?,?,1,now())";
        log4jLog.info(" insertAttendancePunchIn " + query);
        Object[] param = new Object[]{attendance.getUser().getId(), attendance.getPunchInLocation(), attendance.getPunchInLatitude(), attendance.getPunchInLangitude()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                String query1 = "UPDATE fieldsense.users SET last_known_latitude=?,last_known_langitude=? WHERE id=?";
                Object[] param1 = new Object[]{attendance.getPunchInLatitude(), attendance.getPunchInLangitude(), attendance.getUser().getId()};
                FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query1, param1);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" insertAttendancePunchIn " + e);
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param attendance
     * @param accountId
     * @return
     */
    @Override
    public boolean updateAttendancePunchOut(Attendance attendance, int accountId) {
        String query = "UPDATE attendances SET punch_out = CURTIME(), punch_out_location = ?,punch_out_latitude = ?,punch_out_langitude = ? WHERE user_id_fk=? AND punch_date=CURDATE()";
        log4jLog.info(" updateAttendancePunchOut " + query);
        Object[] param = new Object[]{attendance.getPunchOutLocation(), attendance.getPunchOutLatitude(), attendance.getPunchOutLagitude(), attendance.getUser().getId()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateAttendancePunchOut " + e);
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    @Override
    public List<Attendance> selectAttendance(int userId, int accountId) {
        String query = "SELECT id, user_id_fk, punch_date,punch_out_date, punch_in, punch_in_location, punch_in_latitude,punch_in_langitude, punch_out, punch_out_location, punch_out_latitude,punch_out_langitude, created_on FROM attendances WHERE user_id_fk=? ";
        log4jLog.info(" selectAttendance " + query);
        Object[] param = new Object[]{userId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<Attendance>() {

                @Override
                public Attendance mapRow(ResultSet rs, int i) throws SQLException {
                    Attendance attendance = new Attendance();
                    attendance.setId(rs.getInt("id"));

                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    attendance.setUser(user);

                    attendance.setDate(new Date(rs.getDate("punch_date").getTime()));
                    attendance.setPunchInLatitude(rs.getString("punch_in_latitude"));
                    attendance.setPunchInLangitude(rs.getString("punch_in_langitude"));
                    attendance.setPunchInLocation(rs.getString("punch_in_location"));
                    attendance.setPunchInTime(rs.getTime("punch_in"));
                    attendance.setPunchOutLatitude(rs.getString("punch_out_latitude"));
                    attendance.setPunchOutLagitude(rs.getString("punch_out_langitude"));
                    attendance.setPunchOutLocation(rs.getString("punch_out_location"));
                    attendance.setPunchOutDate(new Date(rs.getDate("punch_out_date").getTime())); //For punch out date
                    attendance.setPunchOutTime(rs.getTime("punch_out"));
                    attendance.setCreatedOn(rs.getTimestamp("created_on"));
                    return attendance;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAttendance " + e);
            e.printStackTrace();
            return null;
        }
    }
    
//added by manohar
    public List<LeftSliderMenu> getPunchList(int userId, int accountId) {
       String query = "select IFNULL(punch_in,'00:00:00') punch_in,IFNULL(punch_out,'00:00:00') punch_out from attendances where id= (select max(id) from attendances where user_id_fk=?)"; log4jLog.info(" selectAttendance " + query);
        Object[] param = new Object[]{userId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<LeftSliderMenu>() {

                @Override
                public LeftSliderMenu mapRow(ResultSet rs, int i) throws SQLException {
                    LeftSliderMenu leftSliderMenu =new  LeftSliderMenu();
                    leftSliderMenu.setLastpunchInTime(rs.getString("punch_in"));
                    leftSliderMenu.setLastpunchOutTime(rs.getString("punch_out"));
                    return leftSliderMenu;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAttendance " + e);
            e.printStackTrace();
            return null;
        }
    }
    //ended by manohar
    
    //added by manohar
    public List<LeftSliderMenu> getCheckList(int userId, int accountId) {
       //String query = "select  IFNULL(check_in_time,'0') check_in_time, IFNULL(check_out_time,'0') check_out_time  from appointments where id= (select max(id) from appointments where user_id_fk=?)";
//      String query = "select  IFNULL(check_in_time,'0') check_in_time, IFNULL(check_out_time,'0') check_out_time  from appointments  where user_id_fk=? order by appointment_time desc limit 1";
    String query=  "select  IFNULL(check_in_time,'0') check_in_time, IFNULL(check_out_time,'0') check_out_time  from appointments  where assigned_id_fk=? AND status = 1 order by appointment_time desc limit 1";
       Object[] param = new Object[]{userId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<LeftSliderMenu>() {

                @Override
                public LeftSliderMenu mapRow(ResultSet rs, int i) throws SQLException {
                    LeftSliderMenu leftSliderMenu =new  LeftSliderMenu();
                    leftSliderMenu.setLastcheckInTime(rs.getString("check_in_time"));
                    leftSliderMenu.setLastcheckOutTime(rs.getString("check_out_time"));
                    return leftSliderMenu;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAttendance " + e);
            e.printStackTrace();
            return null;
        }
    }
//ended by manohar
    
     //added by manohar
    public List<LeftSliderMenu> getStatus(int userId, int accountId) {
       String query = "select id , customer_id_fk  from appointments where id= (select max(id) from appointments where user_id_fk=? AND status=2)";
       Object[] param = new Object[]{userId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<LeftSliderMenu>() {

                @Override
                public LeftSliderMenu mapRow(ResultSet rs, int i) throws SQLException {
                    LeftSliderMenu leftSliderMenu =new  LeftSliderMenu();
                    leftSliderMenu.setAppointmentId(rs.getInt("id"));
                    leftSliderMenu.setCustomerId(rs.getInt("customer_id_fk"));
                    return leftSliderMenu;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAttendance " + e);
            e.printStackTrace();
            return null;
        }
    }
//ended by manohar
    /**
     * @param userId
     * @param date
     * @param accountId
     * @return
     */
    @Override
    public Attendance selectAttendance(int userId, Date date, int accountId) {
        String query = "SELECT id, user_id_fk, punch_date,punch_out_date, punch_in, punch_in_location, punch_in_latitude,punch_in_langitude, punch_out, punch_out_location, punch_out_latitude,punch_out_langitude,created_on FROM attendances WHERE user_id_fk=? AND punch_date =?";
        log4jLog.info(" selectAttendance " + query);
        Object[] param = new Object[]{userId, date};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, new RowMapper<Attendance>() {

                @Override
                public Attendance mapRow(ResultSet rs, int i) throws SQLException {
                    Attendance attendance = new Attendance();
                    attendance.setId(rs.getInt("id"));

                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    attendance.setUser(user);

                    attendance.setDate(new Date(rs.getDate("punch_date").getTime()));
                    attendance.setPunchInLatitude(rs.getString("punch_in_latitude"));
                    attendance.setPunchInLangitude(rs.getString("punch_in_langitude"));
                    attendance.setPunchInLocation(rs.getString("punch_in_location"));
                    attendance.setPunchInTime(rs.getTime("punch_in"));
                    attendance.setPunchOutLatitude(rs.getString("punch_out_latitude"));
                    attendance.setPunchOutLagitude(rs.getString("punch_out_langitude"));
                    attendance.setPunchOutLocation(rs.getString("punch_out_location"));
                    attendance.setPunchOutTime(rs.getTime("punch_out"));
                    attendance.setPunchOutDate(new Date(rs.getDate("punch_out_date").getTime())); //For punch out date
                    attendance.setCreatedOn(rs.getTimestamp("created_on"));
                    return attendance;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAttendance " + e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    @Override
    public Attendance selectAttendanceUsingId(int id, int accountId) {
        String query = "SELECT id, user_id_fk, punch_date,punch_out_date, punch_in, punch_in_location, punch_in_latitude,punch_in_langitude, punch_out, punch_out_location, punch_out_latitude,punch_out_langitude,created_on, attendance_status FROM attendances WHERE id =?";
        log4jLog.info(" selectAttendance " + query);
        Object[] param = new Object[]{id};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, new RowMapper<Attendance>() {

                @Override
                public Attendance mapRow(ResultSet rs, int i) throws SQLException {
                    Attendance attendance = new Attendance();
                    attendance.setId(rs.getInt("id"));

                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    attendance.setUser(user);

                    attendance.setDate(new Date(rs.getDate("punch_date").getTime()));
                    attendance.setPunchInLatitude(rs.getString("punch_in_latitude"));
                    attendance.setPunchInLangitude(rs.getString("punch_in_langitude"));
                    attendance.setPunchInLocation(rs.getString("punch_in_location"));
                    attendance.setPunchInTime(rs.getTime("punch_in"));
                    attendance.setPunchOutLatitude(rs.getString("punch_out_latitude"));
                    attendance.setPunchOutLagitude(rs.getString("punch_out_langitude"));
                    attendance.setPunchOutLocation(rs.getString("punch_out_location"));
                    attendance.setPunchOutTime(rs.getTime("punch_out"));
                    attendance.setPunchOutDate(new Date(rs.getDate("punch_out_date").getTime())); //For punch out date
                    attendance.setCreatedOn(rs.getTimestamp("created_on"));
                    attendance.setAttendanceStatus(rs.getInt("attendance_status")); // added by jyoti
                    return attendance;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAttendance " + e);
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     *
     * @param date
     * @param accountId
     * @return
     */
    @Override
    public Attendance selectAttendance(Date date, int accountId) {
        String query = "SELECT id, user_id_fk, punch_date,punch_out_date, punch_in, punch_in_location,punch_in_latitude,punch_in_langitude, punch_out, punch_out_location, punch_out_latitude,punch_out_langitude,created_on FROM attendances WHERE punch_date =?";
        log4jLog.info(" selectAttendance " + query);
        Object[] param = new Object[]{date};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, new RowMapper<Attendance>() {

                @Override
                public Attendance mapRow(ResultSet rs, int i) throws SQLException {
                    Attendance attendance = new Attendance();
                    attendance.setId(rs.getInt("id"));

                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    attendance.setUser(user);

                    attendance.setDate(new Date(rs.getDate("punch_date").getTime()));
                    attendance.setPunchInLatitude(rs.getString("punch_in_latitude"));
                    attendance.setPunchInLangitude(rs.getString("punch_in_langitude"));
                    attendance.setPunchInLocation(rs.getString("punch_in_location"));
                    attendance.setPunchInTime(rs.getTime("punch_in"));
                    attendance.setPunchOutLatitude(rs.getString("punch_out_latitude"));
                    attendance.setPunchOutLagitude(rs.getString("punch_out_langitude"));
                    attendance.setPunchOutLocation(rs.getString("punch_out_location"));
                    attendance.setPunchOutTime(rs.getTime("punch_out"));
                    attendance.setCreatedOn(rs.getTimestamp("created_on"));
                    attendance.setPunchOutDate(new Date(rs.getDate("punch_out_date").getTime())); //For punch out date

                    return attendance;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAttendance " + e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    @Override
    public boolean isUsersFirstPunch(int userId, int accountId) {
        String query = "SELECT Count(id) FROM attendances WHERE user_id_fk=? AND punch_date=CURDATE()";
        log4jLog.info(" isUsersFirstPunch " + query);
        Object[] param = new Object[]{userId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info(" isUsersFirstPunch " + e);
            return false;
        }
    }

    /**
     *
     * @param userId
     * @param date
     * @param accountId
     * @return
     */
    @Override
    public boolean isUsersFirstPunch(int userId, String date, int accountId) {
        String query = "SELECT Count(id) FROM attendances WHERE user_id_fk=? AND punch_date=?";
        log4jLog.info(" isUsersFirstPunch " + query);
        Object[] param = new Object[]{userId, date};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info(" isUsersFirstPunch " + e);
            return false;
        }
    }
    
    /**
     *
     * @param userId
     * @param date
     * @param time
     * @param timeZoneOffset
     * @param accountId
     * @return
     */
    @Override
    public boolean isUsersFirstPunch(int userId, String date,String time,String timeZoneOffset, int accountId){
        String query = "SELECT Count(id) FROM attendances WHERE user_id_fk=? AND ";
        query+="DATE(CONVERT_TZ(concat(punch_date,' ',punch_in),?,@@global.time_zone)) =DATE(CONVERT_TZ(concat(?,' ',?),?,@@global.time_zone))";
        log4jLog.info(" isUsersFirstPunch " + query);
        Object[] param = new Object[]{userId, timeZoneOffset,date,time,timeZoneOffset};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) >0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info(" isUsersFirstPunch " + e);
            return false;
        }
    }

    /**
     *
     * @param userId
     * @param date
     * @param accountId
     * @return
     */
    @Override
    public int getAttendanceId(int userId, String date, int accountId){
        String query = "SELECT id FROM attendances WHERE user_id_fk=? AND punch_date=? order by id desc limit 1"; // now it may happend due to timezone two entries of same day will be present
        log4jLog.info(" isUsersFirstPunch " + query);
        Object[] param = new Object[]{userId, date};
        try {
            int attendanceId = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param,Integer.class);
            return attendanceId;
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info(" attendanceId " + e);
            return 0;
        }
    }
    
    /**
     *
     * @param attendId
     * @param accountId
     * @return
     */
    @Override
    public boolean isUsersFirstPunchUsingId(int attendId, int accountId) {
        String query = "SELECT Count(id) FROM attendances WHERE id=?";
        log4jLog.info(" isUsersFirstPunch " + query);
        Object[] param = new Object[]{attendId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info(" isUsersFirstPunch " + e);
            return false;
        }
    }

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    @Override
    public String userPunchInTime(int userId, int accountId) {
        String query = "SELECT IFNULL(punch_in, '00:00:00') FROM attendances WHERE punch_date= CURDATE() AND user_id_fk = ?";
        log4jLog.info("userPunchInTime " + query);
        Object[] param = new Object[]{userId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info("userPunchInTime " + e);
            return "";
        }
    }

    /**
     *
     * @param userId
     * @param date
     * @param accountId
     * @return
     */
    @Override
    public PunchDateTime userPunchInTimeDate(int userId, String date, int accountId) {
//        String query = " SELECT IFNULL(punch_date,' ') date, IFNULL(punch_in,' ') time, IFNULL(punch_out,' ') punchOutTime FROM attendances WHERE user_id_fk= ? AND punch_date=CURDATE() ORDER BY id DESC LIMIT 1";
        StringBuilder query = new StringBuilder();
        query.append("SELECT IFNULL(a.punch_date,' ') date,IFNULL(a.punch_out_date,' ') as punchOutDate, IFNULL(a.punch_in,' ') time, IFNULL(a.punch_out,' ') punchOutTime,");
        query.append("u.key_value userInMeeting,DATE(u.modified_on) inMeetingModifiedOn FROM attendances a");
        query.append(" INNER JOIN user_keys u ON u.user_id_fk=a.user_id_fk");
        query.append(" WHERE a.user_id_fk= ? AND a.punch_date=CURDATE() AND u.user_key='InMeeting' ORDER BY a.id DESC LIMIT 1");
        final String date1 = date;
        final int accountId1 = accountId;
        final int userId1 = userId;
        log4jLog.info("userPunchInTimeDate " + query);
        Object[] param = new Object[]{userId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query.toString(), param, new RowMapper<PunchDateTime>() {

                @Override
                public PunchDateTime mapRow(ResultSet rs, int i) throws SQLException {
                    PunchDateTime punchDateTime = new PunchDateTime();
                    punchDateTime.setDate(rs.getString("date").trim());
                    punchDateTime.setPunchOutDate(rs.getString("punchOutDate"));
                    punchDateTime.setTime(rs.getString("time"));
                    punchDateTime.setPunchOutTime(rs.getString("punchOutTime"));
                    String inMeetingModifiedOn = rs.getString("inMeetingModifiedOn");
                    if (date1.equals(inMeetingModifiedOn)) {
                        int inMeeting = rs.getInt("userInMeeting");
                        if (inMeeting == 1) {
                            punchDateTime.setIsUseInMeeting(true);
                        } else {
                            punchDateTime.setIsUseInMeeting(false);
                        }
                    } else {
                        punchDateTime.setIsUseInMeeting(false);
                        UserKayDaoImpl userKayDaoImpl = new UserKayDaoImpl();
                        UserKey keyInMeeting = new UserKey();
                        keyInMeeting.setKeyValue("0");
                        keyInMeeting.setUserKay("InMeeting");
                        User userInMeeting = new User();
                        userInMeeting.setId(userId1);
                        keyInMeeting.setUserId(userInMeeting);
                        userKayDaoImpl.updateUserKeys(keyInMeeting, accountId1);
                    }
                    return punchDateTime;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info("userPunchInTimeDate " + e);
            return new PunchDateTime();
        }
    }

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    @Override
    public PunchDateTime userLastPunchDateTime(int userId, int accountId) {
//        String query = " SELECT IFNULL(punch_date,' ') date, IFNULL(punch_in,' ') time, IFNULL(punch_out,' ') punchOutTime FROM attendances WHERE user_id_fk= ? AND punch_date=CURDATE() ORDER BY id DESC LIMIT 1";
        StringBuilder query = new StringBuilder();
        query.append("SELECT IFNULL(a.punch_date,' ') date, IFNULL(a.punch_out_date,' ') as punchOutDate,IFNULL(a.punch_in,' ') time, IFNULL(a.punch_out,' ') punchOutTime,");
        query.append("u.key_value userInMeeting,DATE(u.modified_on) inMeetingModifiedOn FROM attendances a");
        query.append(" INNER JOIN user_keys u ON u.user_id_fk=a.user_id_fk");
        query.append(" WHERE a.user_id_fk= ? AND u.user_key='InMeeting' ORDER BY a.id DESC LIMIT 1");
        //final String date1 = date;
        final int accountId1 = accountId;
        final int userId1 = userId;
        log4jLog.info("userPunchInTimeDate " + query);
        Object[] param = new Object[]{userId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query.toString(), param, new RowMapper<PunchDateTime>() {

                @Override
                public PunchDateTime mapRow(ResultSet rs, int i) throws SQLException {
                    PunchDateTime punchDateTime = new PunchDateTime();
                    punchDateTime.setDate(rs.getString("date").trim());
                    punchDateTime.setPunchOutDate(rs.getString("punchOutDate"));
                    punchDateTime.setTime(rs.getString("time"));
                    punchDateTime.setPunchOutTime(rs.getString("punchOutTime"));
                    String inMeetingModifiedOn = rs.getString("inMeetingModifiedOn");
                    java.util.Date date = java.util.Calendar.getInstance().getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String date1=sdf.format(date);
                    if (date1.equals(inMeetingModifiedOn)) {
                        int inMeeting = rs.getInt("userInMeeting");
                        if (inMeeting == 1) {
                            punchDateTime.setIsUseInMeeting(true);
                        } else {
                            punchDateTime.setIsUseInMeeting(false);
                        }
                    } else {
                        punchDateTime.setIsUseInMeeting(false);
                        UserKayDaoImpl userKayDaoImpl = new UserKayDaoImpl();
                        UserKey keyInMeeting = new UserKey();
                        keyInMeeting.setKeyValue("0");
                        keyInMeeting.setUserKay("InMeeting");
                        User userInMeeting = new User();
                        userInMeeting.setId(userId1);
                        keyInMeeting.setUserId(userInMeeting);
                        userKayDaoImpl.updateUserKeys(keyInMeeting, accountId1);
                    }
                    return punchDateTime;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info("userPunchInTimeDate " + e);
            return new PunchDateTime();
        }
    }

    /**
     *
     * @param attendance
     * @param accountId
     * @return
     */
    @Override
    public int insertAttendancePunchInWithLocationNotFoundReason(Attendance attendance, int accountId) {
        String query = "INSERT INTO attendances (user_id_fk,punch_date,punch_in,punch_in_location,punch_in_latitude,punch_in_langitude,status,created_on,punch_in_location_reason) VALUES(?,?,?,?,?,?,1,now(),?)";
        log4jLog.info("insertAttendancePunchInWithLocationNotFoundReason " + query);
        Object[] param = new Object[]{attendance.getUser().getId(), attendance.getSdate(), attendance.getSpunchInTime(), attendance.getPunchInLocation(), attendance.getPunchInLatitude(), attendance.getPunchInLangitude(), attendance.getPunchInLocationNotFoundReason()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                String query1 = "UPDATE fieldsense.users SET last_known_latitude=?,last_known_langitude=? WHERE id=?";
                Object[] param1 = new Object[]{attendance.getPunchInLatitude(), attendance.getPunchInLangitude(), attendance.getUser().getId()};
                FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query1, param1);
                String query2 = "select max(id) from attendances";
                int attendanceId = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query2, Integer.class);
                return attendanceId;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info("insertAttendancePunchInWithLocationNotFoundReason " + e);
            return 0;
        }
    }

    /**
     *
     * @param attendance
     * @param accountId
     * @return
     */
    @Override
    public boolean updateAttendancePunchOutWithLocationNotFoundReason(Attendance attendance, int accountId) {
        String query = "UPDATE attendances SET punch_out = ?, punch_out_date = ?,punch_out_location = ?,punch_out_latitude = ?,punch_out_langitude = ?,attendance_status=?,punch_out_location_reason=? WHERE user_id_fk=? AND punch_date=?";
        log4jLog.info("updateAttendancePunchOutWithLocationNotFoundReason " + query);
        Object[] param = new Object[]{attendance.getSpunchInTime(),attendance.getSdate(), attendance.getPunchOutLocation(), attendance.getPunchOutLatitude(), attendance.getPunchOutLagitude(),1, attendance.getPunchOutLocationNotFoundReason(), attendance.getUser().getId(), attendance.getSdate()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info("updateAttendancePunchOutWithLocationNotFoundReason " + e);
            return false;
        }
    }

    /**
     *
     * @param attendance
     * @param accountId
     * @return
     */
    @Override
    public boolean updateAttendancePunchOutWithLocationNotFoundReasonUsingId(Attendance attendance, int accountId) {
        String query = "UPDATE attendances SET punch_out = ?,punch_out_date = ?, punch_out_location = ?,punch_out_latitude = ?,punch_out_langitude = ?,attendance_status=?,punch_out_location_reason=? WHERE id=?";
        log4jLog.info("updateAttendancePunchOutWithLocationNotFoundReason " + query);
        Object[] param = new Object[]{attendance.getSpunchInTime(),attendance.getSdate(), attendance.getPunchOutLocation(), attendance.getPunchOutLatitude(), attendance.getPunchOutLagitude(),1, attendance.getPunchOutLocationNotFoundReason(), attendance.getId()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info("updateAttendancePunchOutWithLocationNotFoundReason " + e);
            return false;
        }
    }
        
    /**
     *
     * @param userId
     * @param date
     * @param accountId
     * @return
     */
    @Override
    public String getPunchInTime(int userId, String date, int accountId) {
        String query = "SELECT punch_in FROM attendances WHERE user_id_fk=? AND punch_date=?";
        log4jLog.info("getPunchInTime " + query);
        Object[] param = new Object[]{userId, date};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info("getPunchInTime " + e);
            return null;
        }
    }

    /**
     *
     * @param attendanceId
     * @param accountId
     * @return
     */
    @Override
    public String getPunchInTimeUsingId(int attendanceId,int accountId) {
        String query = "SELECT punch_in FROM attendances WHERE id=?";
        log4jLog.info("getPunchInTime " + query);
        Object[] param = new Object[]{attendanceId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info("getPunchInTime " + e);
            return null;
        }
    }
    
    /**
     *
     * @param userId
     * @param accountId
     * @param attendanceId
     * @return
     */
    @Override
    public boolean updatePunchOutStatus(int userId, int accountId,int attendanceId) {
        String query = "UPDATE attendances SET punch_out = ?,punch_out_date=?, punch_out_location = ?,punch_out_latitude = ?,punch_out_langitude = ?,punch_out_location_reason=?,attendance_status=?  WHERE user_id_fk=? AND id=?";
        log4jLog.info("updatePunchOutStatus " + query);
        Object[] param = new Object[]{"00:00:00","1111-11-11", "", "", "", "", 0, userId,attendanceId}; // Updated from 1 to 0 by awaneesh punchout issue
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) == 1) {
                //Added By Awaneesh to reset source value in user_travel_logs
                query="UPDATE user_travel_logs set source_value=? where user_id_fk=? and source_value=? order by id desc limit 1";
                param = new Object[]{ 3, userId,2};
                FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param);
                //End By Awaneesh
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info("updatePunchOutStatus " + e);
            return false;
        }
    }

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    @Override
    public boolean updateOnlyPunchOutStatus(int userId, int accountId) {
        String query = "UPDATE attendances SET attendance_status=?  WHERE user_id_fk=? AND punch_date=CURDATE()";
        log4jLog.info("updateOnlyPunchOutStatus " + query);
        Object[] param = new Object[]{0, userId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info("updateOnlyPunchOutStatus " + e);
            return false;
        }
    }

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public boolean getPunchStatus(int userId, int accountId) {
        String query = "SELECT attendance_status FROM attendances WHERE user_id_fk=? AND punch_date=CURDATE()";
        log4jLog.info(" getPunchStatus " + query);
        Object[] param = new Object[]{userId};
        try {
            int status = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
            if (status == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info(" getPunchStatus " + e);
            return false;
        }
    }
    
    /**
     *
     * @param attendanceId
     * @param accountId
     * @return
     */
    public boolean getPunchStatusUsingId(int attendanceId, int accountId) {
        String query = "SELECT attendance_status FROM attendances WHERE id=?";
        log4jLog.info(" getPunchStatus " + query);
        Object[] param = new Object[]{attendanceId};
        try {
            int status = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
            if (status == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info(" getPunchStatus " + e);
            return false;
        }
    }
    
    /**
     *
     * @param userId
     * @param accountId
     * @param date
     * @return
     */
    public boolean getPunchStatusByDate(int userId, int accountId,String date) {
        String query = "SELECT attendance_status FROM attendances WHERE user_id_fk=? AND punch_date=?";
        log4jLog.info(" getPunchStatus " + query);
        
        Object[] param = new Object[]{userId,date};
        try {
            int status = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);          
            if (status == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info(" getPunchStatus " + e);
            return false;
        }
    }
    
    /**
     *
     * @param timeout
     * @param accountId
     * @return
     */
    @Override
    public boolean startTimeoutForAttendance(AttendanceTimeout timeout ,int accountId){
        String query="";
        Object[] param=null;
        
        query ="insert into attendances_timeout(user_id_fk,timeout_date,serial_number,start_time,created_on,updated_on) ";
        query+="values (?,?,?,?,now(),now());";
        
        param= new Object[]{timeout.getUserId(),timeout.getStrTimeoutDate(),timeout.getSerialNumber(),timeout.getStartTimeout()};
        
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info("startTimeoutForAttendance " + e);
            return false;
        }
        
    }
    
    /**
     *
     * @param timeout
     * @param accountId
     * @return
     */
    @Override
    public boolean stopTimeoutForAttendance(AttendanceTimeout timeout,int accountId){
        String query="";
        Object[] param=null;
        
        query ="update attendances_timeout set stop_time=?,interval_time=timediff(stop_time,start_time),status=0,updated_on=now() where user_id_fk=? and timeout_date=? and serial_number=? "; //interval can not be negative hence timediff(stop_time,start_time) >=0 is added
        
        param= new Object[]{timeout.getStopTimeout(),timeout.getUserId(),timeout.getStrTimeoutDate(),timeout.getSerialNumber()};
        
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info("stopTimeoutForAttendance " + e);
            return false;
        }
        
    }
    
    /**
     *
     * @param userId
     * @param date
     * @param serialNumber
     * @param accountId
     * @return
     */
    @Override
    public AttendanceTimeout selectAttendanceTimeout(int userId,String date,int serialNumber,int accountId){
       String query = "SELECT id, user_id_fk, serial_number, timeout_date, start_time,stop_time,CONCAT('',interval_time) as interval_time,status, created_on FROM attendances_timeout WHERE user_id_fk=? and timeout_date=? and serial_number=? ";
        log4jLog.info(" selectAttendance " + query);
        Object[] param = new Object[]{userId,date,serialNumber};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, new RowMapper<AttendanceTimeout>() {

                @Override
                public AttendanceTimeout mapRow(ResultSet rs, int i) throws SQLException {
                    AttendanceTimeout attendance = new AttendanceTimeout();
                    attendance.setId(rs.getInt("id"));
                    attendance.setUserId(rs.getInt("user_id_fk"));
                    attendance.setSerialNumber(rs.getInt("serial_number"));
                    attendance.setStrTimeoutDate(rs.getDate("timeout_date").toString());
                    attendance.setStartTimeout(rs.getTimestamp("start_time"));
                    attendance.setStopTimeout(rs.getTimestamp("stop_time"));
                    attendance.setStrIntervalTime(rs.getString("interval_time"));
                    attendance.setStatus(rs.getInt("status"));
                    attendance.setCreatedOn(rs.getTimestamp("created_on"));
                    return attendance;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAttendance " + e);
            e.printStackTrace();
            return new AttendanceTimeout();
        }
    } 
    
    /**
     *
     * @param userId
     * @param date
     * @param accountId
     * @return
     */
    @Override
    public AttendanceTimeout selectAttendanceTimeoutForDate(int userId,String date, int accountId) {
        String query = "SELECT id, user_id_fk, serial_number, timeout_date, start_time,stop_time,CONCAT('',interval_time) as interval_time,status, created_on FROM attendances_timeout WHERE user_id_fk=? and timeout_date=? ";
        log4jLog.info(" selectAttendance " + query);
        Object[] param = new Object[]{userId,date};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, new RowMapper<AttendanceTimeout>() {

                @Override
                public AttendanceTimeout mapRow(ResultSet rs, int i) throws SQLException {
                    AttendanceTimeout attendance = new AttendanceTimeout();
                    attendance.setId(rs.getInt("id"));
                    attendance.setUserId(rs.getInt("user_id_fk"));
                    attendance.setSerialNumber(rs.getInt("serial_number"));
                    attendance.setStrTimeoutDate(rs.getDate("timeout_date").toString());
                    attendance.setStartTimeout(rs.getTimestamp("start_time"));
                    attendance.setStopTimeout(rs.getTimestamp("stop_time"));
                    attendance.setStrIntervalTime(rs.getString("interval_time"));
                    attendance.setStatus(rs.getInt("status"));
                    attendance.setCreatedOn(rs.getTimestamp("created_on"));
                    return attendance;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAttendance " + e);
            e.printStackTrace();
            return new AttendanceTimeout();
        }
    }
    
    /**
     *
     * @param userId
     * @param date
     * @param accountId
     * @return
     */
    @Override
    public List<AttendanceTimeout> selectAllAttendanceTimeoutForDate(int userId,String date, int accountId) {
        String query = "SELECT id, user_id_fk, serial_number, timeout_date, start_time,stop_time,CONCAT('',interval_time) as interval_time,status, created_on FROM attendances_timeout WHERE user_id_fk=? and timeout_date=? order by start_time";
        log4jLog.info(" selectAllAttendanceTimeoutForDate " + query);
        Object[] param = new Object[]{userId,date};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<AttendanceTimeout>() {

                @Override
                public AttendanceTimeout mapRow(ResultSet rs, int i) throws SQLException {
                    AttendanceTimeout attendance = new AttendanceTimeout();
                    attendance.setId(rs.getInt("id"));
                    attendance.setUserId(rs.getInt("user_id_fk"));
                    attendance.setSerialNumber(rs.getInt("serial_number"));
                    attendance.setStrTimeoutDate(rs.getDate("timeout_date").toString());
                    attendance.setStartTimeout(rs.getTimestamp("start_time"));
                    attendance.setStopTimeout(rs.getTimestamp("stop_time"));
                    attendance.setStrIntervalTime(rs.getString("interval_time"));
                    attendance.setStatus(rs.getInt("status"));
                    attendance.setCreatedOn(rs.getTimestamp("created_on"));
                    return attendance;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAllAttendanceTimeoutForDate " + e);
            e.printStackTrace();
            return new ArrayList<AttendanceTimeout>();
        }
    }
    
    /**
     *
     * @param attendance
     * @param stopTime
     * @param userId
     * @param accountId
     * @return
     */
    @Override
     public boolean updateAttendanceTimeOutStopTime(Attendance attendance,Timestamp stopTime,int userId,int accountId) {
        String query = "update attendances_timeout set stop_time=?,status=0,interval_time=timediff(?,start_time),updated_on=now()  where stop_time='1111:11:11 11:11:11' and timeout_date=? and user_id_fk=?";
        log4jLog.info(" updateAttendanceTimeOutStopTime " + query);
        Object[] param = new Object[]{stopTime,stopTime,attendance.getSdate(),userId};
        try {
            int update = FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param);
            return true;    
        } catch (Exception e) {
            log4jLog.info(" updateAttendanceTimeOutStopTime " + e);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     *
     * @param userId
     * @param date
     * @param accountId
     * @return
     */
    @Override
    public AttendanceTimeout selectLastTimeoutOfDayForUser(int userId,String date, int accountId) {
        String query = "SELECT id, user_id_fk, serial_number, timeout_date, start_time,stop_time,CONCAT('',interval_time ) as intervalTime,status, created_on,(SELECT  SUM( TIME_TO_SEC( `interval_time` ) ) FROM attendances_timeout  WHERE user_id_fk=? and timeout_date=? ) AS time_out_sum FROM attendances_timeout WHERE user_id_fk=? and timeout_date=? order by start_time desc limit 1";
        log4jLog.info(" selectAttendance " + query);
        Object[] param = new Object[]{userId,date,userId,date};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, new RowMapper<AttendanceTimeout>() {

                @Override
                public AttendanceTimeout mapRow(ResultSet rs, int i) throws SQLException {
                    AttendanceTimeout attendance = new AttendanceTimeout();
                    attendance.setId(rs.getInt("id"));
                    attendance.setUserId(rs.getInt("user_id_fk"));
                    attendance.setSerialNumber(rs.getInt("serial_number"));
                    attendance.setStrTimeoutDate(rs.getDate("timeout_date").toString());
                    attendance.setStartTimeout(rs.getTimestamp("start_time"));
                    attendance.setStopTimeout(rs.getTimestamp("stop_time"));
                    attendance.setStrIntervalTime(rs.getString("intervalTime"));
                    attendance.setStatus(rs.getInt("status"));
                    attendance.setCreatedOn(rs.getTimestamp("created_on"));
                    Long intvl=rs.getLong("time_out_sum");
                    attendance.setTotalIntervalTime(fromSecToStringTime(intvl));
                    return attendance;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAttendance " + e);
           // e.printStackTrace();
            return new AttendanceTimeout();
        }
    }
    
    private String fromSecToStringTime(long seconds){
        boolean isNegative=false;
        if(seconds <0){
            isNegative=true;
            seconds = (-1 * seconds);
        }
        long hh1=(seconds/3600);
        long mm1= (seconds % 3600)/60;
        long ss1= (seconds % 3600)% 60;
        String hh2="00";
        String mm2="00";
        String ss2="00";
        
        if(hh1<10){
            hh2="0"+hh1;
        }else{
            hh2=""+hh1; 
        }
        if(mm1<10){
            mm2="0"+mm1;
        }else{
            mm2=""+mm1;
        }
        if(ss1<10){
            ss2="0"+ss1;
        }else{
            ss2=""+ss1;
        }
        
        if(isNegative){
            return "-"+hh2+":"+mm2+":"+ss2;
        }else{
            return hh2+":"+mm2+":"+ss2;
        }
    }

   
}
