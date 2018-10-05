/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.usersTravelLogs.dao;

import com.qlc.fieldsense.activityPurpose.model.ActivityPurpose;
import com.qlc.fieldsense.appointments.model.Appointment;
import com.qlc.fieldsense.customer.model.Customer;
import com.qlc.fieldsense.customerContact.model.CustomerContacts;
import com.qlc.fieldsense.report.model.Attendance;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.usersTravelLogs.model.UsersTravelLogs;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author anuja
 */
public class UsersTravelLogsDaoImpl implements UsersTravelLogsDao {

    /**
     *
     */
 private JdbcTemplate jdbcTemplateCache;

    /**
     *
     * @return
     */
    public JdbcTemplate getJdbcTemplateCache() {
        return jdbcTemplateCache;
    }

    /**
     *
     * @param jdbcTemplateCache
     */
    public void setJdbcTemplateCache(JdbcTemplate jdbcTemplateCache) {
        this.jdbcTemplateCache = jdbcTemplateCache;
    }

    public static final Logger log4jLog = Logger.getLogger("UsersTravelLogsDaoImpl");

    /**
     *
     * @param usersTravelLogs
     * @param accountId
     * @return
     */
    @Override
    public int insertUsersTravelLog(UsersTravelLogs usersTravelLogs, int accountId) {
        //String query = "INSERT INTO user_travel_logs(user_id_fk,latitude,longitude,location,created_on,is_customer_location,customer_location_identifier,customer_name,is_server_fetch,source_value,distance_travel) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        int isCustomer=0,isServerFetch=0;
        if(usersTravelLogs.isIsCustomerLocation()){
            isCustomer=1;
        }
        if(usersTravelLogs.getIsServerFetch()){
            isServerFetch=1;
        }
        String query="";
        Object param[]=null;
        /*if(usersTravelLogs.getVersionCode()<22){
            query = "INSERT INTO user_travel_logs(user_id_fk,latitude,longitude,location,created_on,is_customer_location,customer_location_identifier,customer_name,is_server_fetch,source_value) VALUES(?,?,?,?,?,?,?,?,?,?)";
            param = new Object[]{usersTravelLogs.getUserId(), usersTravelLogs.getLatitude(), usersTravelLogs.getLangitude(), usersTravelLogs.getLocation(),usersTravelLogs.getCreatedOn(),isCustomer,usersTravelLogs.getLocationIdentifier(),usersTravelLogs.getCustomerName(),isServerFetch,usersTravelLogs.getSourceValue()};
        }else{*/
//            query = "INSERT INTO user_travel_logs(user_id_fk,latitude,longitude,location,created_on,is_customer_location,customer_location_identifier,customer_name,is_server_fetch,source_value,distance_travel) VALUES(?,?,?,?,?,?,?,?,?,?,?)"; // commented by jyoti
            query = "INSERT INTO user_travel_logs(user_id_fk,latitude,longitude,location,created_on,is_customer_location,customer_location_identifier,customer_name,is_server_fetch,source_value,distance_travel, battery_Percentage, isGPS, network_type, app_version, oS_Version, device_Name, isMockLocation, isShowIntoReports, customer_id_fk) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; // added by jyoti
//            param = new Object[]{usersTravelLogs.getUserId(), usersTravelLogs.getLatitude(), usersTravelLogs.getLangitude(), usersTravelLogs.getLocation(),usersTravelLogs.getCreatedOn(),isCustomer,usersTravelLogs.getLocationIdentifier(),usersTravelLogs.getCustomerName(),isServerFetch,usersTravelLogs.getSourceValue(),usersTravelLogs.getTravelDistance()};
            param = new Object[]{usersTravelLogs.getUserId(), usersTravelLogs.getLatitude(), usersTravelLogs.getLangitude(), usersTravelLogs.getLocation(),usersTravelLogs.getCreatedOn(),isCustomer,usersTravelLogs.getLocationIdentifier(),usersTravelLogs.getCustomerName(),isServerFetch,usersTravelLogs.getSourceValue(),usersTravelLogs.getTravelDistance(), usersTravelLogs.getBattery_Percentage(), usersTravelLogs.getIsGPS(), usersTravelLogs.getNetwork_type(), usersTravelLogs.getApp_version(), usersTravelLogs.getoS_Version(), usersTravelLogs.getDevice_Name(), usersTravelLogs.getIsMockLocation(), usersTravelLogs.getIsShowIntoReports(), usersTravelLogs.getCustomerId()};
        //}
        log4jLog.info(" insertUsersTravelLog " + query);
        //Object param[] = new Object[]{usersTravelLogs.getUserId(), usersTravelLogs.getLatitude(), usersTravelLogs.getLangitude(), usersTravelLogs.getLocation(),usersTravelLogs.getCreatedOn(),isCustomer,usersTravelLogs.getLocationIdentifier(),usersTravelLogs.getCustomerName(),isServerFetch,usersTravelLogs.getSourceValue(),usersTravelLogs.getTravelDistance()};
        try {
            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    return FieldSenseUtils.getMaxIdForTable("user_travel_logs", accountId);
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            log4jLog.info(" insertUsersTravelLog " + e);
            e.printStackTrace();
            return 0;
        }
    }

    /**
     *
     * @param travelLogId
     * @param accountId
     * @return
     */
    @Override
    public UsersTravelLogs selectUsersTravelLog(int travelLogId, int accountId) {
        String query = "SELECT id,user_id_fk,latitude,longitude,location,created_on FROM user_travel_logs WHERE id=?";
        log4jLog.info(" selectUsersTravelLog " + query);
        Object param[] = new Object[]{travelLogId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, new RowMapper<UsersTravelLogs>() {

                @Override
                public UsersTravelLogs mapRow(ResultSet rs, int i) throws SQLException {
                    UsersTravelLogs usersTravelLogs = new UsersTravelLogs();
                    usersTravelLogs.setId(rs.getInt("id"));
                    usersTravelLogs.setUserId(rs.getInt("user_id_fk"));
                    usersTravelLogs.setLatitude(rs.getDouble("latitude"));
                    usersTravelLogs.setLangitude(rs.getDouble("longitude"));
                    usersTravelLogs.setLocation(rs.getString("location"));
                    usersTravelLogs.setCreatedOn(rs.getTimestamp("created_on"));
                    return usersTravelLogs;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectUsersTravelLog " + e);
            e.printStackTrace();
            return new UsersTravelLogs();
        }
    }

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    @Override
    public List<UsersTravelLogs> selectUsersTravelLogs(Integer userId, int accountId) {
        String query = "SELECT id,user_id_fk,latitude,longitude,location,created_on FROM user_travel_logs WHERE user_id_fk=?";
        log4jLog.info(" selectUsersTravelLogs " + query);
        try {
            Object param[] = new Object[]{userId};
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<UsersTravelLogs>() {

                public UsersTravelLogs mapRow(ResultSet rs, int i) throws SQLException {
                    UsersTravelLogs usersTravelLogs = new UsersTravelLogs();
                    usersTravelLogs.setId(rs.getInt("id"));
                    usersTravelLogs.setUserId(rs.getInt("user_id_fk"));
                    usersTravelLogs.setLatitude(rs.getDouble("latitude"));
                    usersTravelLogs.setLangitude(rs.getDouble("longitude"));
                    usersTravelLogs.setLocation(rs.getString("location"));
                    usersTravelLogs.setCreatedOn(rs.getTimestamp("created_on"));
                    return usersTravelLogs;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectUsersTravelLogs " + e);
            e.printStackTrace();
            return new ArrayList<UsersTravelLogs>();
        }
    }

//    /**
//     *
//     * @param userId
//     * @param fromDate
//     * @param toDate
//     * @param accountId
//     * @return
//     */
//    @Override
//    public List<UsersTravelLogs> selectUsersTravelLogsDateWise(Integer userId, String fromDate, String toDate, int accountId) {
//        String query = "SELECT * FROM user_travel_logs WHERE (created_on BETWEEN ? AND ?) AND user_id_fk=? ORDER BY created_on"; // replace order by id with order by created_on
//        log4jLog.info(" selectUsersTravelLogs " + query);
//        //final List<UsersTravelLogs> userTodaysAppointmentsLocations = selectUserTodaysApointmentCustomerLocations(fromDate, toDate, userId, accountId);
//        try {
//            
//            log4jLog.info("selectUsersTravelLogsDateWise fromDate: "+fromDate+" toDate: "+toDate);
//            
//            Object param[] = new Object[]{fromDate, toDate, userId};
//            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<UsersTravelLogs>() {
//
//                @Override
//                public UsersTravelLogs mapRow(ResultSet rs, int i) throws SQLException {
//                    UsersTravelLogs usersTravelLogs = new UsersTravelLogs();
//                    //boolean isCustomerLocation = false;
//                    //String customerName = "";
//                    /*for (UsersTravelLogs customerLocation : userTodaysAppointmentsLocations) {
//                        double customerLatitude = customerLocation.getLatitude();
//                        double userLatitude = rs.getDouble("latitude");
//                        if (customerLatitude == userLatitude) {
//                            double customerLangitude = customerLocation.getLangitude();
//                            double userLangitude = rs.getDouble("longitude");
//                            if (customerLangitude == userLangitude) {
//                                isCustomerLocation = true;
//                                customerName = customerLocation.getCustomerName();
//                            }
//                        }
//                    }*/
//                    usersTravelLogs.setId(rs.getInt("id"));
//                    usersTravelLogs.setUserId(rs.getInt("user_id_fk"));
//                    usersTravelLogs.setLatitude(rs.getDouble("latitude"));
//                    usersTravelLogs.setLangitude(rs.getDouble("longitude"));
//                    usersTravelLogs.setLocation(rs.getString("location"));
//                    usersTravelLogs.setCreatedOn(rs.getTimestamp("created_on"));
//                    usersTravelLogs.setCreatedonString(rs.getString("created_on"));
//                    
//                    if(rs.getInt("is_customer_location")==1){
//                        usersTravelLogs.setIsCustomerLocation(true);
//                    }else{
//                        usersTravelLogs.setIsCustomerLocation(false);
//
//                    }
//                    usersTravelLogs.setCustomerName(rs.getString("customer_name"));
//                    usersTravelLogs.setLocationIdentifier(rs.getString("customer_location_identifier"));
//                    usersTravelLogs.setSourceValue(rs.getInt("source_value"));
//                    return usersTravelLogs;
//                }
//            });
//        } catch (Exception e) {
//            log4jLog.info(" selectUsersTravelLogs " + e);
////            e.printStackTrace();
//            return new ArrayList<UsersTravelLogs>();
//        }
//    }
    /**
     *
     * @param userId
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    @Override
    public List<UsersTravelLogs> selectUsersTravelLogsDateWise(Integer userId, String fromDate, String toDate, int accountId) {
        String query = "SELECT * FROM user_travel_logs WHERE (created_on BETWEEN ? AND ?) AND user_id_fk=? ORDER BY created_on"; // replace order by id with order by created_on
        log4jLog.info(" selectUsersTravelLogs " + query);
        //final List<UsersTravelLogs> userTodaysAppointmentsLocations = selectUserTodaysApointmentCustomerLocations(fromDate, toDate, userId, accountId);
        try {
            
            log4jLog.info("selectUsersTravelLogsDateWise fromDate: "+fromDate+" toDate: "+toDate);
            
            Object param[] = new Object[]{fromDate, toDate, userId};
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<UsersTravelLogs>() {

                @Override
                public UsersTravelLogs mapRow(ResultSet rs, int i) throws SQLException {
                    UsersTravelLogs usersTravelLogs = new UsersTravelLogs();
                    //boolean isCustomerLocation = false;
                    //String customerName = "";
                    /*for (UsersTravelLogs customerLocation : userTodaysAppointmentsLocations) {
                        double customerLatitude = customerLocation.getLatitude();
                        double userLatitude = rs.getDouble("latitude");
                        if (customerLatitude == userLatitude) {
                            double customerLangitude = customerLocation.getLangitude();
                            double userLangitude = rs.getDouble("longitude");
                            if (customerLangitude == userLangitude) {
                                isCustomerLocation = true;
                                customerName = customerLocation.getCustomerName();
                            }
                        }
                    }*/
                    usersTravelLogs.setId(rs.getInt("id"));
                    usersTravelLogs.setUserId(rs.getInt("user_id_fk"));
                    usersTravelLogs.setLatitude(rs.getDouble("latitude"));
                    usersTravelLogs.setLangitude(rs.getDouble("longitude"));
                    usersTravelLogs.setLocation(rs.getString("location"));
                    usersTravelLogs.setCreatedOn(rs.getTimestamp("created_on"));
                    usersTravelLogs.setCreatedonString(rs.getString("created_on"));
                    
                    if(rs.getInt("is_customer_location")==1){
                        usersTravelLogs.setIsCustomerLocation(true);
                    }else{
                        usersTravelLogs.setIsCustomerLocation(false);

                    }
                    usersTravelLogs.setCustomerName(rs.getString("customer_name"));
                    usersTravelLogs.setLocationIdentifier(rs.getString("customer_location_identifier"));
                    usersTravelLogs.setSourceValue(rs.getInt("source_value"));
                    usersTravelLogs.setBattery_Percentage(rs.getString("battery_Percentage"));
                    usersTravelLogs.setIsGPS(rs.getString("isGPS"));
                    usersTravelLogs.setApp_version(rs.getString("app_version"));
                    usersTravelLogs.setNetwork_type(rs.getString("network_type"));
                             usersTravelLogs.setoS_Version(rs.getString("oS_Version"));
                                      usersTravelLogs.setDevice_Name(rs.getString("device_Name"));
                                      usersTravelLogs.setCustomerId(rs.getString("customer_id_fk"));
                      return usersTravelLogs;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectUsersTravelLogs " + e);
//            e.printStackTrace();
            return new ArrayList<UsersTravelLogs>();
        }
    }
    /**
     *
     * @param userId
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    @Override
    public List<Attendance> attendanceDateWise(Integer userId, String fromDate, String toDate, int accountId) {
        String query = "SELECT punch_date,punch_in,punch_in_location,punch_out_date,punch_out,punch_out_location FROM attendances WHERE punch_date=? AND user_id_fk=?"; // replace order by id with order by created_on
        log4jLog.info(" selectUsersTravelLogs " + query);
        log4jLog.info("attendanceDateWise fromDate: "+fromDate+" toDate: "+toDate);

        Object param[] = new Object[]{toDate.split(" ")[0], userId};
        //final List<UsersTravelLogs> userTodaysAppointmentsLocations = selectUserTodaysApointmentCustomerLocations(fromDate, toDate, userId, accountId);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<Attendance>() {

                @Override
                public Attendance mapRow(ResultSet rs, int i) throws SQLException {
                    Attendance attendance = new Attendance();
                    attendance.setPunchDate(rs.getString("punch_date"));
                    attendance.setPunchInTime(rs.getString("punch_in"));
                    attendance.setPunchInLocation(rs.getString("punch_in_location"));
                    attendance.setPunchOutDate(rs.getString("punch_out_date"));
                    attendance.setPunchOutTime(rs.getString("punch_out"));
                    attendance.setPunchOutLocation(rs.getString("punch_out_location"));
                    return attendance;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAttendance " + e);
            e.printStackTrace();
            return new ArrayList<Attendance>();
        }
    }
            
    /**
     *
     * @param userId
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    @Override
    public List<Appointment> appointmentsDateWise(Integer userId, String fromDate, String toDate, int accountId) {
        String query="SELECT app.appointment_title,app.appointment_time,app.appointment_end_time,app.check_in_time,app.check_in_location,";
        query+=" app.check_out_time,app.check_out_location,app.status,cust.customer_name FROM appointments app  INNER JOIN customers ";
        query+=" cust on app.customer_id_fk=cust.id AND (app.appointment_time BETWEEN ? AND ?) AND app.assigned_id_fk=? AND app.record_state!=3 ";
        query+=" ORDER BY app.appointment_time";
        log4jLog.info(" selectUsersTravelLogs " + query);
        //final List<UsersTravelLogs> userTodaysAppointmentsLocations = selectUserTodaysApointmentCustomerLocations(fromDate, toDate, userId, accountId);
        try {
            Object param[] = new Object[]{fromDate, toDate, userId};
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<Appointment>() {

                @Override
                public Appointment mapRow(ResultSet rs, int i) throws SQLException {
                    Appointment appointment = new Appointment();
                    appointment.setTitle(rs.getString("appointment_title"));
                    appointment.setDateTime(rs.getTimestamp("appointment_time"));
                    appointment.setEndTime(rs.getTimestamp("appointment_end_time"));
                    appointment.setCheckInTime(rs.getTimestamp("check_in_time"));
                    appointment.setCheckInLocation(rs.getString("check_in_location"));
                    appointment.setCheckOutTime(rs.getTimestamp("check_out_time"));
                    appointment.setCheckOutLocation(rs.getString("check_out_location"));
                    appointment.setStatus(rs.getInt("status"));
                    Customer customer=new Customer();
                    customer.setCustomerName(rs.getString("customer_name"));
                    appointment.setCustomer(customer);
                    return appointment;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectattendance " + e);
//            e.printStackTrace();
            return new ArrayList<Appointment>();
        }
    }
    //by nikhil 
        @Override
    public List<Appointment> selectAppointForUser( int userId,String fromDate, String toDate, int accountId) {
        StringBuilder query = new StringBuilder();

        /*query.append("SELECT a.id,a.appointment_title,a.customer_id_fk,c.customer_printas,a.customer_contact_id_fk,cc.first_name,cc.middle_name,");
        query.append("cc.last_name,a.appointment_time,a.purpose_id_fk,p.purpose,a.status,a.appointment_description,a.outcome FROM appointments as a INNER JOIN ");
        query.append("customers as c ON a.customer_id_fk=c.id LEFT OUTER JOIN customer_contacts as cc ON ");
        query.append("a.customer_contact_id_fk=cc.id INNER JOIN activity_purpose as p ON a.purpose_id_fk=p.id");
        query.append(" WHERE assigned_id_fk=? ORDER BY a.created_on DESC");*/
//        query.append("SELECT a.id,a.appointment_title,a.customer_id_fk,c.customer_printas,c.customer_location_identifier,a.customer_contact_id_fk,cc.first_name,cc.middle_name,");
//        query.append("cc.last_name,a.appointment_time,a.purpose_id_fk,a.check_in_time,a.check_in_location,a.check_out_time,a.check_out_location,p.purpose,a.status,a.appointment_description,a.outcome_description FROM appointments as a INNER JOIN ");
//        query.append("customers as c ON a.customer_id_fk=c.id LEFT OUTER JOIN customer_contacts as cc ON ");
//        query.append("a.customer_contact_id_fk=cc.id LEFT OUTER JOIN activity_purpose as p ON a.purpose_id_fk=p.id");
//        query.append(" WHERE c.record_state !=3 AND a.record_state!=3 AND (appointment_time BETWEEN ? AND ?) AND assigned_id_fk=? ORDER BY a.created_on DESC");
         query.append("SELECT  a.id,a.appointment_title,a.customer_id_fk,c.customer_printas,c.customer_location_identifier,a.customer_contact_id_fk,cc.first_name,cc.middle_name,cc.last_name,a.appointment_time,a.appointment_end_time,a.purpose_id_fk,a.check_in_time,a.check_in_location,a.check_out_time,a.check_out_location,p.purpose,a.status,a.appointment_description,"); 
         query.append("a.outcome_description FROM appointments as a INNER JOIN  customers as c ON a.customer_id_fk=c.id LEFT OUTER JOIN customer_contacts as cc ON a.customer_contact_id_fk=cc.id LEFT OUTER "); 
query.append("JOIN activity_purpose as p ON a.purpose_id_fk=p.id WHERE c.record_state !=3 AND a.record_state!=3 AND (appointment_time BETWEEN ? AND  ?) AND ");
  query.append("assigned_id_fk=? ORDER BY a.created_on DESC ");

        log4jLog.info(" selectAppointForUser " + query);
        Object param[] = new Object[]{fromDate, toDate, userId};
        try {
//            System.out.println("fromDate "+fromDate +" toDate "+toDate+" userId "+userId);
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Appointment>() {

                @Override
                public Appointment mapRow(ResultSet rs, int i) throws SQLException {
                    Appointment appointment = new Appointment();
                    appointment.setId(rs.getInt("a.id"));
                    appointment.setTitle(rs.getString("appointment_title"));

                    Customer c = new Customer();
                    c.setId(rs.getInt("customer_id_fk"));
                    c.setCustomerPrintas(rs.getString("customer_printas"));
                    c.setCustomerLocation(rs.getString("customer_location_identifier"));
                    appointment.setCustomer(c);

                    CustomerContacts cContact = new CustomerContacts();
                    cContact.setId(rs.getInt("customer_contact_id_fk"));
                    cContact.setFirstName(rs.getString("first_name"));
                    cContact.setMiddleName(rs.getString("middle_name"));
                    cContact.setLastName(rs.getString("last_name"));
                    appointment.setCustomerContact(cContact);

                     appointment.setDateTime(rs.getTimestamp("appointment_time"));
                    appointment.setEndTime(rs.getTimestamp("appointment_end_time"));
                    ActivityPurpose aPurpose = new ActivityPurpose();
                    aPurpose.setId(rs.getInt("purpose_id_fk"));
                    
                    appointment.setPurpose(aPurpose);
                    
                    appointment.setCheckInTime(rs.getTimestamp("check_in_time"));
                    appointment.setCheckInLocation(rs.getString("check_in_location"));
                    appointment.setCheckOutTime(rs.getTimestamp("check_out_time"));
                    appointment.setCheckOutLocation(rs.getString("check_out_location"));
                    
                    aPurpose.setPurpose(rs.getString("purpose"));
                    
                    appointment.setSdateTime(appointment.getDateTime().toString());
                    appointment.setStatus(rs.getInt("status"));
                    appointment.setDescription(rs.getString("appointment_description"));
                    appointment.setOutcomeDescription(rs.getString("outcome_description"));
                    
//                    ActivityOutcome out = new ActivityOutcome();
//                    out.setId(rs.getInt("outcome"));
//                    appointment.setOutcomes(out);
//                    appointment.setOutcome(rs.getInt("a.outcome"));
//                    System.out.println("getOutcomeDescription "+appointment.getOutcomeDescription());
                    return appointment;
                }
            });

        } catch (Exception e) {
            log4jLog.info(" selectAppointForUser " + e);
            e.printStackTrace();
            return new ArrayList<Appointment>();
        }
    }
    /**
     *
     * @param userId
     * @param location
     * @param accountId
     * @return
     */
    @Override
    public List<UsersTravelLogs> selectUsersTravelLogs(Integer userId, String location, int accountId) {
        String query = "SELECT id,user_id_fk,latitude,longitude,location,created_on FROM user_travel_logs WHERE user_id_fk=? AND location LIKE '%" + location + "%'";
        log4jLog.info(" selectUsersTravelLogs " + query);
        Object param[] = new Object[]{userId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<UsersTravelLogs>() {

                @Override
                public UsersTravelLogs mapRow(ResultSet rs, int i) throws SQLException {
                    UsersTravelLogs usersTravelLogs = new UsersTravelLogs();
                    usersTravelLogs.setId(rs.getInt("id"));
                    usersTravelLogs.setUserId(rs.getInt("user_id_fk"));
                    usersTravelLogs.setLatitude(rs.getDouble("latitude"));
                    usersTravelLogs.setLangitude(rs.getDouble("longitude"));
                    usersTravelLogs.setLocation(rs.getString("location"));
                    usersTravelLogs.setCreatedOn(rs.getTimestamp("created_on"));
                    return usersTravelLogs;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectUsersTravelLogs " + e);
            e.printStackTrace();
            return new ArrayList<UsersTravelLogs>();
        }
    }

    /**
     *
     * @param fromDate
     * @param toDate
     * @param userId
     * @param accountId
     * @return
     */
    @Override
    public List<UsersTravelLogs> selectUserTodaysApointmentCustomerLocations(String fromDate, String toDate, int userId, int accountId) {
        StringBuilder query = new StringBuilder();
//        query.append("SELECT a.customer_id_fk,IFNULL(l.latitude,0),IFNULL(l.longitude,0),c.customer_name FROM appointments a");
//        query.append(" LEFT OUTER JOIN location l ON a.customer_id_fk=l.location_type_id_fk");
//        query.append(" INNER JOIN customers c ON a.customer_id_fk=c.id");
//        query.append(" WHERE (appointment_time BETWEEN ? AND ?) AND assigned_id_fk=?");
        query.append("SELECT a.customer_id_fk,IFNULL(l.latitude,0) as latitude,IFNULL(l.longitude,0) as longitude ,");
        query.append("c.customer_name FROM appointments a INNER JOIN customers c ON a.customer_id_fk=c.id");
        query.append(" LEFT OUTER JOIN location l ON a.customer_id_fk=l.location_type_id_fk ");
        query.append(" WHERE (a.appointment_time BETWEEN ? AND ?) AND a.assigned_id_fk=? AND a.record_state!=3");
        Object param[] = new Object[]{fromDate, toDate, userId};
        log4jLog.info(" selectUserTodaysApointmentCustomerLocations " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<UsersTravelLogs>() {

                @Override
                public UsersTravelLogs mapRow(ResultSet rs, int i) throws SQLException {
                    UsersTravelLogs usersTravelLogs = new UsersTravelLogs();
                    usersTravelLogs.setLatitude(rs.getDouble("latitude"));
                    usersTravelLogs.setLangitude(rs.getDouble("longitude"));
                    usersTravelLogs.setCustomerName(rs.getString("customer_name"));
                    return usersTravelLogs;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectUserTodaysApointmentCustomerLocations " + e);
            e.printStackTrace();
            return new ArrayList<UsersTravelLogs>();
        }
    }
    
    //Added by Mayank on 29 aug 2016

    /**
     *
     * @param userId
     * @param accountId
     * @param created_date
     * @return
     */
            @Override
    public List<UsersTravelLogs> selectUsersTravelLogsLastLocation(Integer userId, int accountId, String created_date) {
        log4jLog.info("selectUsersTravelLogsLastLocation created_on_date: "+created_date);
        String query = "SELECT created_on FROM user_travel_logs WHERE user_id_fk=? and created_on = ?";
        log4jLog.info(" selectUsersTravelLogsLastLocation " + query);
        Object param[] = new Object[]{userId, created_date};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<UsersTravelLogs>() {

                @Override
                public UsersTravelLogs mapRow(ResultSet rs, int i) throws SQLException {
                    UsersTravelLogs usersTravelLogs = new UsersTravelLogs();
                    usersTravelLogs.setCreatedOn(rs.getTimestamp("created_on"));
                    return usersTravelLogs;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectUsersTravelLogsLastLocation " + e);
            e.printStackTrace();
            return new ArrayList<UsersTravelLogs>();
        }
    }

    public int insertUsersTravelLogBatch(List<UsersTravelLogs> usersTravelLogs, int accountId) {
//        String query = "INSERT INTO user_travel_logs(user_id_fk,latitude,longitude,location,created_on,is_customer_location,customer_location_identifier,customer_name,is_server_fetch,source_value,distance_travel) VALUES(?,?,?,?,?,?,?,?,?,?,?)"; // commented by jyoti
        String query = "INSERT INTO user_travel_logs(user_id_fk,latitude,longitude,location,created_on,is_customer_location,customer_location_identifier,customer_name,is_server_fetch,source_value,distance_travel, battery_Percentage, isGPS, network_type, app_version, oS_Version, device_Name, isMockLocation, isShowIntoReports, customer_id_fk) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; // added by jyoti
        final List<UsersTravelLogs> usersTravelLogsList =(List)usersTravelLogs;
          try{
        FieldSenseUtils.getJdbcTemplateForAccount(accountId).batchUpdate(query,new BatchPreparedStatementSetter() {

          public void setValues(PreparedStatement ps, int i) throws SQLException {
              UsersTravelLogs usersTravelLogs=usersTravelLogsList.get(i);
//              System.out.println(i + " << insertUsersTravelLogBatch >> " + usersTravelLogs.toString());
             // System.out.println("Sidd"+customerObj.getMobileTempId());
              ps.setInt(1,usersTravelLogs.getId());
              ps.setDouble(2,usersTravelLogs.getLatitude());
              ps.setDouble(3,usersTravelLogs.getLangitude());
              ps.setString(4,usersTravelLogs.getLastKnownLocation());
              ps.setTimestamp(5,usersTravelLogs.getCreatedOn());
              ps.setInt(6, usersTravelLogs.getIsCustomerLocationInt());
              ps.setString(7,usersTravelLogs.getLocationIdentifier());
              ps.setString(8,usersTravelLogs.getCustomerName());
              ps.setInt(9,usersTravelLogs.getIsServerFetchInt());
              ps.setInt(10,usersTravelLogs.getSourceValue());
              ps.setDouble(11,usersTravelLogs.getTravelDistance());
              
              // added by jyoti
              ps.setString(12, usersTravelLogs.getBattery_Percentage());
              ps.setString(13, usersTravelLogs.getIsGPS());
              ps.setString(14, usersTravelLogs.getNetwork_type());
              ps.setString(15, usersTravelLogs.getApp_version());
              ps.setString(16, usersTravelLogs.getoS_Version());
              ps.setString(17, usersTravelLogs.getDevice_Name());
              ps.setString(18, usersTravelLogs.getIsMockLocation());
              ps.setInt(19, usersTravelLogs.getIsShowIntoReports());
              ps.setString(20, usersTravelLogs.getCustomerId());
              // ended by jyoti
          }

          public int getBatchSize() {
             return usersTravelLogsList.size();
          }
      });
        }catch(Exception e){
            log4jLog.info("insertOfflineDataCustomer " + e);
//            System.out.println("error in batchj");
           e.printStackTrace();
        return 0;
        }     
      return 1;
    }
  
public int insertIntoLocationNotFound(List<UsersTravelLogs> usersTravelLogs, int accountId) {
        final int account_id=accountId;
//        System.out.println("hello size of travel logs"+usersTravelLogs.size()+"account_id"+account_id);
//       String query = "INSERT INTO location_not_found(lat,lang,account_id_fk,user_id_fk,created_on,is_customer_location,customer_location_identifier,customer_name,source_value,distance_travel) VALUES(?,?,?,?,?,?,?,?,?,?)"; // commented by jyoti
       String query = "INSERT INTO location_not_found(lat,lang,account_id_fk,user_id_fk,created_on,is_customer_location,customer_location_identifier,customer_name,source_value,distance_travel, battery_Percentage, isGPS, network_type, app_version, oS_Version, device_Name, isMockLocation, isShowIntoReports, customer_id_fk) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; // added by jyoti
// Object param = new Object[]{usersTravelLogs.getUserId(), usersTravelLogs.getLatitude(), usersTravelLogs.getLangitude(), usersTravelLogs.getLocation(),usersTravelLogs.getCreatedOn(),usersTravelLogs.getIsCustomerLocationInt(),usersTravelLogs.getLocationIdentifier(),usersTravelLogs.getCustomerName(),usersTravelLogs.getIsServerFetchInt(),usersTravelLogs.getSourceValue(),usersTravelLogs.getTravelDistance()};
        final List<UsersTravelLogs> usersTravelLogsList =(List)usersTravelLogs;
          try{
    jdbcTemplateCache.batchUpdate(query,new BatchPreparedStatementSetter() {

          public void setValues(PreparedStatement ps, int i) throws SQLException {
              UsersTravelLogs usersTravelLogs=usersTravelLogsList.get(i);
//              System.out.println(i + " << insertIntoLocationNotFound >> " + usersTravelLogs.toString());
              ps.setDouble(1,usersTravelLogs.getLatitude());
              ps.setDouble(2,usersTravelLogs.getLangitude());
              ps.setInt(3,account_id);
              ps.setInt(4,usersTravelLogs.getId());
              ps.setTimestamp(5,usersTravelLogs.getCreatedOn());
              ps.setInt(6, usersTravelLogs.getIsCustomerLocationInt());
              ps.setString(7,usersTravelLogs.getLocationIdentifier());
              ps.setString(8,usersTravelLogs.getCustomerName());
              ps.setInt(9,usersTravelLogs.getSourceValue());
              ps.setDouble(10,usersTravelLogs.getTravelDistance());
              // added by jyoti
              ps.setString(11, usersTravelLogs.getBattery_Percentage());
              ps.setString(12, usersTravelLogs.getIsGPS());
              ps.setString(13, usersTravelLogs.getNetwork_type());
              ps.setString(14, usersTravelLogs.getApp_version());
              ps.setString(15, usersTravelLogs.getoS_Version());
              ps.setString(16, usersTravelLogs.getDevice_Name());
              ps.setString(17, usersTravelLogs.getIsMockLocation());
              ps.setInt(18, usersTravelLogs.getIsShowIntoReports());
              ps.setString(19, usersTravelLogs.getCustomerId());
              // ended by jyoti
          }

          public int getBatchSize() {
             return usersTravelLogsList.size();
          }
      });
        }catch(Exception e){
            log4jLog.info("insertOfflineDataCustomer " + e);
//            System.out.println("error in batchj");
           e.printStackTrace();
        return 0;
        }     
      return 1;
    }

    public boolean insertIntoTravelLogForUnresolvedAddress(UsersTravelLogs usersTravelLogs) {
        try{
        int accountId=usersTravelLogs.getAccountId();
//        System.out.println("usersTravelLogs.getAccountId()"+usersTravelLogs.getAccountId()+"obj"+usersTravelLogs);
//        String query="INSERT INTO user_travel_logs ( user_id_fk, latitude, longitude, location, created_on, is_customer_location, customer_location_identifier,customer_name,is_server_fetch,source_value,distance_travel) VALUES (?,?,?,?,?,?,?,?,?,?,?)"; //commented by jyoti
        String query="INSERT INTO user_travel_logs ( user_id_fk, latitude, longitude, location, created_on, is_customer_location, customer_location_identifier,customer_name,is_server_fetch,source_value,distance_travel, battery_Percentage, isGPS, network_type, app_version, oS_Version, device_Name, isMockLocation, isShowIntoReports, customer_id_fk) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; // added by jyoti
//        Object param[]= new Object[]{usersTravelLogs.getUserId(), usersTravelLogs.getLatitude(), usersTravelLogs.getLangitude(), usersTravelLogs.getLocation(), usersTravelLogs.getCreatedOn(), usersTravelLogs.getIsCustomerLocationInt(), usersTravelLogs.getLocationIdentifier(), usersTravelLogs.getCustomerName(), usersTravelLogs.getIsServerFetchInt(),usersTravelLogs.getSourceValue(), usersTravelLogs.getTravelDistance()};
        Object param[]= new Object[]{usersTravelLogs.getUserId(), usersTravelLogs.getLatitude(), usersTravelLogs.getLangitude(), usersTravelLogs.getLocation(), usersTravelLogs.getCreatedOn(), usersTravelLogs.getIsCustomerLocationInt(), usersTravelLogs.getLocationIdentifier(), usersTravelLogs.getCustomerName(), usersTravelLogs.getIsServerFetchInt(),usersTravelLogs.getSourceValue(), usersTravelLogs.getTravelDistance(), usersTravelLogs.getBattery_Percentage(), usersTravelLogs.getIsGPS(), usersTravelLogs.getNetwork_type(), usersTravelLogs.getApp_version(), usersTravelLogs.getoS_Version(), usersTravelLogs.getDevice_Name(), usersTravelLogs.getIsMockLocation(), usersTravelLogs.getIsShowIntoReports(),usersTravelLogs.getCustomerId()};
         if (FieldSenseUtils.getJdbcTemplateForAccount(1).update(query,param) > 0) {
                    return true;
                } else {
                    return false;
                }
        }catch(Exception e ){
        e.printStackTrace();
//            System.out.println("Helllo error"+e);
            return false;
        }
    }

     //@Added by siddhesh, 09-01-2018
    public boolean insertTravelLogsSingle(UsersTravelLogs usersTravelLogs, int account) {
//        String query = "INSERT INTO user_travel_logs (user_id_fk,latitude,longitude,location,created_on,is_customer_location,customer_location_identifier,customer_name,is_server_fetch,source_value,distance_travel) VALUES(?,?,?,?,?,?,?,?,?,?,?)"; // commented by jyoti
        String query=  "INSERT INTO user_travel_logs ( user_id_fk, latitude, longitude, location, created_on, is_customer_location, customer_location_identifier,customer_name,is_server_fetch,source_value,distance_travel, battery_Percentage, isGPS, network_type, app_version, oS_Version, device_Name, isMockLocation, isShowIntoReports, customer_id_fk) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; // added by jyoti

        try{
//             System.out.println("usersTravelLogs.getAccountId()"+account+"obj"+usersTravelLogs);
//            Object param[] = new Object[]{usersTravelLogs.getId(), usersTravelLogs.getLatitude(), usersTravelLogs.getLangitude(), usersTravelLogs.getLocation(), usersTravelLogs.getCreatedOn(), 1, usersTravelLogs.getLocationIdentifier(), usersTravelLogs.getCustomerName(), 1, usersTravelLogs.getSourceValue(), usersTravelLogs.getTravelDistance()};
            Object param[]= new Object[]{usersTravelLogs.getUserId(), usersTravelLogs.getLatitude(), usersTravelLogs.getLangitude(), usersTravelLogs.getLocation(), usersTravelLogs.getCreatedOn(), usersTravelLogs.getIsCustomerLocationInt(), usersTravelLogs.getLocationIdentifier(), usersTravelLogs.getCustomerName(), usersTravelLogs.getIsServerFetchInt(),usersTravelLogs.getSourceValue(), usersTravelLogs.getTravelDistance(), usersTravelLogs.getBattery_Percentage(), usersTravelLogs.getIsGPS(), usersTravelLogs.getNetwork_type(), usersTravelLogs.getApp_version(), usersTravelLogs.getoS_Version(), usersTravelLogs.getDevice_Name(), usersTravelLogs.getIsMockLocation(), usersTravelLogs.getIsShowIntoReports(), usersTravelLogs.getCustomerId()};
            if (FieldSenseUtils.getJdbcTemplateForAccount(account).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public int insertIntoCacheInsertBatch(List<UsersTravelLogs> usersTravelLogs, int accountId) {  
        final int account_id=accountId;
//        System.out.println("hello size of cache logs"+usersTravelLogs+"account_id"+account_id);
       String query = "INSERT INTO cache_location_insert(lat,lang,cache_locations) VALUES(?,?,?)";
       // Object param = new Object[]{usersTravelLogs.getUserId(), usersTravelLogs.getLatitude(), usersTravelLogs.getLangitude(), usersTravelLogs.getLocation(),usersTravelLogs.getCreatedOn(),usersTravelLogs.getIsCustomerLocationInt(),usersTravelLogs.getLocationIdentifier(),usersTravelLogs.getCustomerName(),usersTravelLogs.getIsServerFetchInt(),usersTravelLogs.getSourceValue(),usersTravelLogs.getTravelDistance()};
        final List<UsersTravelLogs> usersTravelLogsList =(List)usersTravelLogs;
          try{
    jdbcTemplateCache.batchUpdate(query,new BatchPreparedStatementSetter() {

          public void setValues(PreparedStatement ps, int i) throws SQLException {
              UsersTravelLogs usersTravelLogs=usersTravelLogsList.get(i);
              ps.setDouble(1,usersTravelLogs.getLatitude());
              ps.setDouble(2,usersTravelLogs.getLangitude());
              ps.setString(3,usersTravelLogs.getLastKnownLocation());
          }

          public int getBatchSize() {
             return usersTravelLogsList.size();
          }
      });
        }catch(Exception e){
            log4jLog.info("insertOfflineDataCustomer " + e);
//            System.out.println("error in batchj");
           e.printStackTrace();
        return 0;
        }     
      return 1;
    
    }
}
