/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.user.dao;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import static com.qlc.fieldsense.accounts.dao.AccountRegistrationDaoImpl.log4jLog;
import com.qlc.fieldsense.accounts.model.Account;
import com.qlc.fieldsense.password.model.Password;
import com.qlc.fieldsense.team.model.TeamMember;
import com.qlc.fieldsense.user.model.SuperUser;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.user.model.UserOld;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Ramesh
 * @date 18-02-2014
 * @purpose To perform the user related database operations .
 */
public class UserDaoImpl implements UserDao {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("UserDaoImpl");
    JdbcTemplate jdbcTemplate;
    private JdbcTemplate jdbcTemplateStats;
    
    
     /**
     *
     * @return
     */
    public JdbcTemplate getJdbcTemplateStats() {
        return jdbcTemplateStats;
    }

    /**
     *
     * @param jdbcTemplateStats
     */
    public void setJdbcTemplateStats(JdbcTemplate jdbcTemplateStats) {
        this.jdbcTemplateStats = jdbcTemplateStats;
    }
    

    /**
     *
     * @return
     */
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
     *
     * @param jdbcTemplate
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     *
     * @param accountId
     * @return
     */
    @Override
    public List<User> slectAllUsers(int accountId) {
        String query = "SELECT emp_code,id,account_id_fk,first_name,last_name,email_address,password,mobile_number,gender,user_accuracy,check_in_radius,role,report_to,active,last_logged_on,last_known_location_time,last_known_location,created_on,created_by,allow_timeout FROM users WHERE account_id_fk=? AND role!=0 ORDER BY first_name,last_name";
        log4jLog.info(" slectAllUsers " + query);
        Object param[] = new Object[]{accountId};
        try {
            return this.jdbcTemplate.query(query, param, new RowMapper<User>() {

                @Override
                public User mapRow(ResultSet rs, int i) throws SQLException {
                    User user = new User();
                    user.setEmp_code(rs.getString("emp_code"));
                    user.setId(rs.getInt("id"));
                    user.setAccountId(rs.getInt("account_id_fk"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmailAddress(rs.getString("email_address"));
                    user.setPassword(rs.getString("password"));
                    user.setMobileNo(rs.getString("mobile_number"));
                    user.setGender(rs.getInt("gender"));
                    
                    //Added by Mayank Ramaiya
                    user.setUserAccuracy(rs.getInt("user_accuracy"));
                    user.setCheckInRadius(rs.getInt("check_in_radius"));
                    //End by Mayank Ramaiya
                    
                    user.setAllowTimeout(rs.getInt("allow_timeout"));
                    
                    
                    user.setRole(rs.getInt("role"));
                    user.setReport_to(rs.getInt("report_to"));
                    user.setActive(rs.getBoolean("active"));
                    user.setLastLoggedOn(rs.getTimestamp("last_logged_on"));
                    user.setLastKnownLocationTime(rs.getTimestamp("last_known_location_time"));
                    user.setLastKnownLocation(rs.getString("last_known_location"));
                    user.setCreatedOn(rs.getTimestamp("created_on"));
                    user.setCreatedBy(rs.getInt("created_by"));
                    return user;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" slectAllUsers " + e);
//            e.printStackTrace();
            return new ArrayList<User>();
        }
    }

    /**
     *
     * @param allRequestParams
     * @param accountId
     * @return
     */
    @Override
    public List<User> slectAllUsersWithOffset(@RequestParam Map<String,String> allRequestParams, int accountId) {
        
        log4jLog.info("slectAllUsersWithOffset");
        StringBuilder query = new StringBuilder();
        
        query.append("SELECT DISTINCT u.id,account_id_fk,first_name,last_name,email_address,password,mobile_number,gender,user_accuracy,check_in_radius,report_to,role,allow_timeout,emp_code,");
        query.append(" active,last_logged_on,last_known_location_time,last_known_location,u.created_on,u.created_by,");
        query.append(" (SELECT COUNT(DISTINCT u2.id) FROM fieldsense.users u2 ");
        query.append(" INNER JOIN  user_territory t1 ON u2.id=t1.user_id_fk ");
        query.append(" WHERE (u2.account_id_fk=? AND u2.role!=0)");
        String searchText=allRequestParams.get("searchText").trim();
        if(searchText.equals("")){
            query.append(") AS usersCount, ");
            query.append("IFNULL(a.id,0) attendanceId,IFNULL(a.punch_date,'1111-11-11') punchInDate,IFNULL(a.punch_in,0) punchIntime,IFNULL(a.punch_out_date,'1111-11-11') punchOutDate, IFNULL(a.punch_out,0) punchOutTime FROM fieldsense.users as u");
            query.append(" INNER JOIN (SELECT u1.id FROM fieldsense.users as u1 WHERE account_id_fk=? AND role!=0 ");
        }else{
            query.append(" and ( u2.full_name LIKE ? or u2.email_address LIKE ? or u2.mobile_number LIKE ?)) AS usersCount, ");
            query.append(" IFNULL(a.id,0) attendanceId,IFNULL(a.punch_date,'1111-11-11') punchInDate,IFNULL(a.punch_in,0) punchIntime,IFNULL(a.punch_out_date,'1111-11-11') punchOutDate, IFNULL(a.punch_out,0) punchOutTime FROM fieldsense.users as u");
            query.append(" INNER JOIN (SELECT u1.id FROM fieldsense.users as u1 WHERE (account_id_fk=? AND role!=0)  and (u1.full_name LIKE ? or u1.email_address LIKE ? or u1.mobile_number LIKE ? or u1.emp_code LIKE ?) ");
        }
        String sortcolindex=allRequestParams.get("order[0][column]");
        if(sortcolindex==null){
            query.append(" ORDER BY u1.full_name");
        }else if(Integer.parseInt(sortcolindex)==0){
            if(allRequestParams.get("order[0][dir]").equals("asc")){
                query.append(" ORDER BY u1.full_name");
            }else{
                query.append(" ORDER BY u1.full_name DESC");
            }
        }else if(Integer.parseInt(sortcolindex)==1){
            if(allRequestParams.get("order[0][dir]").equals("asc")){
                query.append(" ORDER BY u1.email_address");
            }else{
                query.append(" ORDER BY  u1.email_address DESC");
            }
//        }else if(Integer.parseInt(sortcolindex)==5){
//            if(allRequestParams.get("order[0][dir]").equals("asc")){
//                query.append(" ORDER BY u1.last_logged_on");
//            }else{
//                query.append(" ORDER BY u1.last_logged_on DESC");
//            }
        }
        else if(Integer.parseInt(sortcolindex)==5){
            if(allRequestParams.get("order[0][dir]").equals("asc")){
                query.append(" ORDER BY u1.role");
            }else{
                query.append(" ORDER BY  u1.full_name");
            }
//        }else if(Integer.parseInt(sortcolindex)==5){
//            if(allRequestParams.get("order[0][dir]").equals("asc")){
//                query.append(" ORDER BY u1.last_logged_on");
//            }else{
//                query.append(" ORDER BY u1.last_logged_on DESC");
//            }
        }
        query.append(" LIMIT ? OFFSET ? ) AS my_results USING(id)");
        query.append(" LEFT OUTER JOIN attendances as a ON u.id=a.user_id_fk AND a.id=(select max(id) from  attendances att where att.user_id_fk=u.id)");
        //query.append(" INNER JOIN  user_territory t1 ON u.id=t1.user_id_fk INNER JOIN territory_categories c on c.id=t1.teritory_id ");
       // query.append(" GROUP BY u.id ");
       
        /*query.append("SELECT DISTINCT u.id,account_id_fk,first_name,last_name,email_address,password,mobile_number,gender,role,");
        query.append(" active,last_logged_on,last_known_location_time,last_known_location,u.created_on,u.created_by,");
        query.append(" (SELECT count(u2.id) FROM fieldsense.users as u2 WHERE account_id_fk=? AND role!=0) AS usersCount,IFNULL(a.punch_in,0) punchIntime, IFNULL(a.punch_out,0) punchOutTime FROM fieldsense.users as u");
        query.append(" LEFT OUTER JOIN attendances as a ON u.id=a.user_id_fk AND a.punch_date=CURDATE() ");
        query.append(" INNER JOIN (SELECT u1.id FROM fieldsense.users as u1 WHERE account_id_fk=? AND role!=0 ORDER BY first_name LIMIT 10 OFFSET ?) AS my_results ON u.id");*/
      
        final int start=Integer.parseInt(allRequestParams.get("start"));
        final int length=Integer.parseInt(allRequestParams.get("length"));
        Object param[]=new Object[10]; 
        //if(length!=-1){
            if(searchText.equals("")){
                param =  new Object[]{accountId, accountId,length,start};
            }else{
                 param =  new Object[]{accountId,"%"+searchText+"%","%"+searchText+"%","%"+searchText+"%",accountId,"%"+searchText+"%","%"+searchText+"%","%"+searchText+"%","%"+searchText+"%",length,start};
            }
        //}   
        log4jLog.info(" slectAllUsers " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int i) throws SQLException {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setAccountId(rs.getInt("account_id_fk"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmailAddress(rs.getString("email_address"));
                    user.setPassword(rs.getString("password"));
                    user.setMobileNo(rs.getString("mobile_number"));
                    user.setGender(rs.getInt("gender"));
                    
                    //Added by Mayank Ramaiya
                    user.setUserAccuracy(rs.getInt("user_accuracy"));
                    user.setCheckInRadius(rs.getInt("check_in_radius"));
                    //End by Mayank Ramaiya
                    user.setAllowTimeout(rs.getInt("allow_timeout"));
                    user.setRole(rs.getInt("role"));
                    user.setReport_to(rs.getInt("report_to"));
                    user.setActive(rs.getBoolean("active"));
                    user.setLastLoggedOn(rs.getTimestamp("last_logged_on"));
                    user.setLastKnownLocationTime(rs.getTimestamp("last_known_location_time"));
                    user.setLastKnownLocation(rs.getString("last_known_location"));
                    user.setCreatedOn(rs.getTimestamp("created_on"));
                    user.setCreatedBy(rs.getInt("created_by"));
                    user.setUsersCount(rs.getInt("usersCount"));
                    user.setEmp_code(rs.getString("emp_code"));   //added by manohar
                    String punchInDate=rs.getString("punchInDate");
                    String punchInTime=rs.getString("punchIntime");
                    String punchOutDate=rs.getString("punchOutDate");
                    String punchOutTime=rs.getString("punchOutTime");
                    if(!punchOutTime.equals("0")){
                        Timestamp punchInTimestamp=Timestamp.valueOf(punchInDate+" "+punchInTime);
                        long currentTime=System.currentTimeMillis();
                        long punchInMills = punchInTimestamp.getTime();
                        long diff=currentTime-punchInMills;
                        diff = (diff/((60)*(1000)));
                        if(diff>((48)*(60))){
                            punchOutTime="0";
                        }
                    }
                   // String tempTime = rs.getString("punchOutTime");
                    String temp = "00:00:00";
                    if (punchOutTime.equals(temp)) {
                        user.setPunchStatusTime(punchInTime);
                        user.setPunchStatus("PunchIn");
                        user.setPunchDate(punchInDate);
                        user.setAttendanceId(rs.getInt("attendanceId"));
                    } else if (punchOutTime.equals("0")) {
                        user.setPunchStatus("NotPunchIn");
                    } else {
                        user.setPunchStatusTime(punchOutTime);
                        user.setPunchStatus("PunchOut");
                        user.setPunchDate(punchOutDate);
                        user.setAttendanceId(rs.getInt("attendanceId"));
                    }
                    return user;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" slectAllUsers " + e);
//            e.printStackTrace();
            return new ArrayList<User>();
        }
    }
    
//  added by manohar
    public List<User> getSuperUsers(@RequestParam Map<String,String> allRequestParams, int accountId) {
        
        log4jLog.info("slectAllSuperUsers");
        StringBuilder query = new StringBuilder();
        
        query.append("SELECT DISTINCT u.id,account_id_fk,first_name,last_name,email_address,password,mobile_number,gender,user_accuracy,check_in_radius,report_to,role,allow_timeout,");
        query.append(" active,last_logged_on,last_known_location_time,last_known_location,u.created_on,u.created_by,");
        query.append(" (SELECT COUNT(DISTINCT u2.id) FROM fieldsense.users u2 ");
        query.append(" INNER JOIN  user_territory t1 ON u2.id=t1.user_id_fk ");
        query.append(" WHERE (u2.account_id_fk=? AND u2.role=0)");
        String searchText=allRequestParams.get("search").trim();
        if(searchText.equals("")){
            query.append(") AS usersCount, ");
            query.append("IFNULL(a.id,0) attendanceId,IFNULL(a.punch_date,'1111-11-11') punchInDate,IFNULL(a.punch_in,0) punchIntime,IFNULL(a.punch_out_date,'1111-11-11') punchOutDate, IFNULL(a.punch_out,0) punchOutTime FROM fieldsense.users as u");
            query.append(" INNER JOIN (SELECT u1.id FROM fieldsense.users as u1 WHERE account_id_fk=? AND role=0 ");
        }else{
            query.append(" and ( u2.full_name LIKE ? or u2.email_address LIKE ? or u2.mobile_number LIKE ?)) AS usersCount, ");
            query.append(" IFNULL(a.id,0) attendanceId,IFNULL(a.punch_date,'1111-11-11') punchInDate,IFNULL(a.punch_in,0) punchIntime,IFNULL(a.punch_out_date,'1111-11-11') punchOutDate, IFNULL(a.punch_out,0) punchOutTime FROM fieldsense.users as u");
            query.append(" INNER JOIN (SELECT u1.id FROM fieldsense.users as u1 WHERE (account_id_fk=? AND role=0)  and (u1.full_name LIKE ? or u1.email_address LIKE ? or u1.mobile_number LIKE ?) ");
        }
        String sortcolindex=allRequestParams.get("order[0][column]");
        if(sortcolindex==null){
            query.append(" ORDER BY u1.full_name");
        }else if(Integer.parseInt(sortcolindex)==0){
            if(allRequestParams.get("order[0][dir]").equals("asc")){
                query.append(" ORDER BY u1.full_name");
            }else{
                query.append(" ORDER BY u1.full_name DESC");
            }
        }else if(Integer.parseInt(sortcolindex)==1){
            if(allRequestParams.get("order[0][dir]").equals("asc")){
                query.append(" ORDER BY u1.email_address");
            }else{
                query.append(" ORDER BY  u1.email_address DESC");
            }
//        }else if(Integer.parseInt(sortcolindex)==5){
//            if(allRequestParams.get("order[0][dir]").equals("asc")){
//                query.append(" ORDER BY u1.last_logged_on");
//            }else{
//                query.append(" ORDER BY u1.last_logged_on DESC");
//            }
        }
        query.append(" LIMIT ? OFFSET ? ) AS my_results USING(id)");
        query.append(" LEFT OUTER JOIN attendances as a ON u.id=a.user_id_fk AND a.id=(select max(id) from  attendances att where att.user_id_fk=u.id)");
        //query.append(" INNER JOIN  user_territory t1 ON u.id=t1.user_id_fk INNER JOIN territory_categories c on c.id=t1.teritory_id ");
       // query.append(" GROUP BY u.id ");
       
        /*query.append("SELECT DISTINCT u.id,account_id_fk,first_name,last_name,email_address,password,mobile_number,gender,role,");
        query.append(" active,last_logged_on,last_known_location_time,last_known_location,u.created_on,u.created_by,");
        query.append(" (SELECT count(u2.id) FROM fieldsense.users as u2 WHERE account_id_fk=? AND role!=0) AS usersCount,IFNULL(a.punch_in,0) punchIntime, IFNULL(a.punch_out,0) punchOutTime FROM fieldsense.users as u");
        query.append(" LEFT OUTER JOIN attendances as a ON u.id=a.user_id_fk AND a.punch_date=CURDATE() ");
        query.append(" INNER JOIN (SELECT u1.id FROM fieldsense.users as u1 WHERE account_id_fk=? AND role!=0 ORDER BY first_name LIMIT 10 OFFSET ?) AS my_results ON u.id");*/
      
        final int start=Integer.parseInt(allRequestParams.get("start"));
        final int length=Integer.parseInt(allRequestParams.get("length"));
        Object param[]=new Object[10]; 
        //if(length!=-1){
            if(searchText.equals("")){
                param =  new Object[]{accountId, accountId,length,start};
            }else{
                 param =  new Object[]{accountId,"%"+searchText+"%","%"+searchText+"%","%"+searchText+"%",accountId,"%"+searchText+"%","%"+searchText+"%","%"+searchText+"%",length,start};
            }
        //}   
        log4jLog.info(" slectAllUsers " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int i) throws SQLException {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setAccountId(rs.getInt("account_id_fk"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmailAddress(rs.getString("email_address"));
                    user.setPassword(rs.getString("password"));
                    user.setMobileNo(rs.getString("mobile_number"));
                    user.setGender(rs.getInt("gender"));
                    
                    //Added by Mayank Ramaiya
                    user.setUserAccuracy(rs.getInt("user_accuracy"));
                    user.setCheckInRadius(rs.getInt("check_in_radius"));
                    //End by Mayank Ramaiya
                    user.setAllowTimeout(rs.getInt("allow_timeout"));
                    user.setRole(rs.getInt("role"));
                    user.setReport_to(rs.getInt("report_to"));
                    user.setActive(rs.getBoolean("active"));
                    user.setLastLoggedOn(rs.getTimestamp("last_logged_on"));
                    user.setLastKnownLocationTime(rs.getTimestamp("last_known_location_time"));
                    user.setLastKnownLocation(rs.getString("last_known_location"));
                    user.setCreatedOn(rs.getTimestamp("created_on"));
                    user.setCreatedBy(rs.getInt("created_by"));
                    user.setUsersCount(rs.getInt("usersCount"));
                    String punchInDate=rs.getString("punchInDate");
                    String punchInTime=rs.getString("punchIntime");
                    String punchOutDate=rs.getString("punchOutDate");
                    String punchOutTime=rs.getString("punchOutTime");
                    if(!punchOutTime.equals("0")){
                        Timestamp punchInTimestamp=Timestamp.valueOf(punchInDate+" "+punchInTime);
                        long currentTime=System.currentTimeMillis();
                        long punchInMills = punchInTimestamp.getTime();
                        long diff=currentTime-punchInMills;
                        diff = (diff/((60)*(1000)));
                        if(diff>((48)*(60))){
                            punchOutTime="0";
                        }
                    }
                   // String tempTime = rs.getString("punchOutTime");
                    String temp = "00:00:00";
                    if (punchOutTime.equals(temp)) {
                        user.setPunchStatusTime(punchInTime);
                        user.setPunchStatus("PunchIn");
                        user.setPunchDate(punchInDate);
                        user.setAttendanceId(rs.getInt("attendanceId"));
                    } else if (punchOutTime.equals("0")) {
                        user.setPunchStatus("NotPunchIn");
                    } else {
                        user.setPunchStatusTime(punchOutTime);
                        user.setPunchStatus("PunchOut");
                        user.setPunchDate(punchOutDate);
                        user.setAttendanceId(rs.getInt("attendanceId"));
                    }
                    return user;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" slectAllUsers " + e);
//            e.printStackTrace();
            return new ArrayList<User>();
        }
    }
    
    
    
    
   
    
    

 /**  added by manohar
  * @param user
  * @return 
  */
     public int insertSuperUser(SuperUser user) {
//        String query = "INSERT INTO users(account_id_fk, first_name, last_name, email_address, password, mobile_number, gender, role, active, last_logged_on, last_known_location_time, last_known_location, created_on, created_by,designation,accountcontact_type) VALUES (?,?,?,?,?,?,?,?,?,now(),now(),?,now(),?,?,?)";
        String query = "INSERT INTO users(account_id_fk, first_name, last_name,full_name, email_address, password, mobile_number, user_accuracy, check_in_radius, gender, role, active, last_logged_on, last_known_location_time, last_known_location, created_on, created_by,designation,allow_timeout,allow_offline,accountcontact_type) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,now(),now(),?,now(),?,?,?,?,?)";
        log4jLog.info(" insertUser " + query);
        Object param[] = new Object[]{user.getAccountId(), user.getFirstName(), user.getLastName(), user.getFirstName() + " " + user.getLastName(), user.getEmailAddress(), user.getPassword(), user.getMobileNo(), user.getUserAccuracy(), user.getCheckInRadius(), user.getGender(), user.getRole(),user.isActive(), user.getLastKnownLocation(), user.getCreatedBy(), user.getDesignation(),user.getAllowTimeout(),user.getAllowOffline(),user.getAccountContactType()};
        //System.out.println("off" +user.getAllowOffline());
        try {
            synchronized (this) {
                if (this.jdbcTemplate.update(query, param) > 0) {
                    try {
                        String query1 = "SELECT MAX(id) FROM users";
                        log4jLog.info(" insertUser " + query1);
                        return this.jdbcTemplate.queryForObject(query1, Integer.class);
                    } catch (Exception e) {
                        log4jLog.info(" insertUser " + e);
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            log4jLog.info(" insertUser " + e);
//            e.printStackTrace();
            return 0;
        }
    }
     




    
    
    
    
    
    
    
//    @Override
//    public List<User> slectAllUsersWithOffset(int offset, int accountId) {
////        String query = "SELECT id,account_id_fk,first_name,last_name,email_address,password,mobile_number,gender,role,active,last_logged_on,last_known_location_time,last_known_location,created_on,created_by FROM users WHERE account_id_fk=? AND role!=0";
//        StringBuilder query = new StringBuilder();
//        query.append("SELECT id,account_id_fk,first_name,last_name,email_address,password,mobile_number,gender,role,");
//        query.append("active,last_logged_on,last_known_location_time,last_known_location,created_on,created_by,");
//        query.append("(SELECT count(id) FROM users WHERE account_id_fk=? AND role!=0) AS usersCount FROM users");
//        query.append(" INNER JOIN (SELECT id FROM users WHERE account_id_fk=? AND role!=0 ORDER BY first_name LIMIT 10 OFFSET ?) AS my_results USING(id)");
//        log4jLog.info(" slectAllUsers " + query);
//        Object param[] = new Object[]{accountId, accountId, offset};
//        try {
//            return this.jdbcTemplate.query(query.toString(), param, new RowMapper<User>() {
//
//                @Override
//                public User mapRow(ResultSet rs, int i) throws SQLException {
//                    User user = new User();
//                    user.setId(rs.getInt("id"));
//                    user.setAccountId(rs.getInt("account_id_fk"));
//                    user.setFirstName(rs.getString("first_name"));
//                    user.setLastName(rs.getString("last_name"));
//                    user.setEmailAddress(rs.getString("email_address"));
//                    user.setPassword(rs.getString("password"));
//                    user.setMobileNo(rs.getString("mobile_number"));
//                    user.setGender(rs.getInt("gender"));
//                    user.setRole(rs.getInt("role"));
//                    user.setActive(rs.getBoolean("active"));
//                    user.setLastLoggedOn(rs.getTimestamp("last_logged_on"));
//                    user.setLastKnownLocationTime(rs.getTimestamp("last_known_location_time"));
//                    user.setLastKnownLocation(rs.getString("last_known_location"));
//                    user.setCreatedOn(rs.getTimestamp("created_on"));
//                    user.setCreatedBy(rs.getInt("created_by"));
//                    user.setUsersCount(rs.getInt("usersCount"));
//                    return user;
//                }
//            });
//        } catch (Exception e) {
//            log4jLog.info(" slectAllUsers " + e);
////            e.printStackTrace();
//            return new ArrayList<User>();
//        }
//    }

    /**
     *
     * @param accountId
     * @return
     */
    @Override
    public User selectTopUser(int accountId) {
        String query = "select u.id,u.account_id_fk,u.first_name,u.last_name,u.email_address,u.password,u.mobile_number,u.gender,u.user_accuracy,u.check_in_radius,u.role,u.active,u.last_logged_on,u.last_known_location_time,u.last_known_location,u.created_on,u.created_by,u.designation,u.allow_timeout from teams t   inner join fieldsense.users u on t.user_id_fk=u.id and t.id=100000;";
        log4jLog.info(" selectUser " + query);
       // Object param[] = new Object[]{accountId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, new RowMapper<User>() {

                @Override
                public User mapRow(ResultSet rs, int i) throws SQLException {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setAccountId(rs.getInt("account_id_fk"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmailAddress(rs.getString("email_address"));
                    user.setPassword(rs.getString("password"));
                    user.setMobileNo(rs.getString("mobile_number"));
                    user.setGender(rs.getInt("gender"));
                    
                    //Added by Mayank Ramaiya
                    user.setUserAccuracy(rs.getInt("user_accuracy"));
                    user.setCheckInRadius(rs.getInt("check_in_radius"));
                    //End by Mayank Ramaiya
                    user.setAllowTimeout(rs.getInt("allow_timeout"));
                    user.setRole(rs.getInt("role"));
                    user.setActive(rs.getBoolean("active"));
                    user.setLastLoggedOn(rs.getTimestamp("last_logged_on"));
                    user.setLastKnownLocationTime(rs.getTimestamp("last_known_location_time"));
                    user.setLastKnownLocation(rs.getString("last_known_location"));
                    user.setCreatedOn(rs.getTimestamp("created_on"));
                    user.setCreatedBy(rs.getInt("created_by"));
                    user.setDesignation(rs.getString("designation"));
                   
                    return user;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectUser " + e);
//            e.printStackTrace();
            return new User();
        }
    }
    // added by nikhil on 11th july 2017
    public List<User> selectAllAdmins(int accountId) {
         String query = "select u.id,u.account_id_fk,u.first_name,u.full_name,u.last_name,u.email_address,u.password,u.mobile_number,u.gender,u.user_accuracy,u.check_in_radius,u.role,u.active,u.last_logged_on,u.last_known_location_time,u.last_known_location,u.created_on,u.created_by,u.designation,u.allow_timeout from teams t   inner join fieldsense.users u on t.user_id_fk=u.id and role=1";
        log4jLog.info(" selectAllAdmins " + query);
   // Object param[] = new Object[]{accountId};
     //   System.out.println("paramselectAllAdmins"+param.toString());
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, new RowMapper<User>() {

                @Override
                public User mapRow(ResultSet rs, int i) throws SQLException {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setAccountId(rs.getInt("account_id_fk"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setFullname(rs.getString("full_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmailAddress(rs.getString("email_address"));
                    user.setPassword(rs.getString("password"));
                    user.setMobileNo(rs.getString("mobile_number"));
                    user.setGender(rs.getInt("gender"));
                    user.setDesignation(rs.getString("designation"));
                    //Added by Mayank Ramaiya
                    user.setUserAccuracy(rs.getInt("user_accuracy")); 
                    user.setCheckInRadius(rs.getInt("check_in_radius"));
                    //End by Mayank Ramaiya
                    user.setAllowTimeout(rs.getInt("allow_timeout"));
                    
                    // added by jyoti 21-12-2016, used for offline feature
                   // user.setAllowOffline(rs.getInt("allow_offline"));
                    
                    user.setRole(rs.getInt("role"));
                 //   user.setReport_to(rs.getInt("report_to"));
                    user.setActive(rs.getBoolean("active"));
                    user.setLastLoggedOn(rs.getTimestamp("last_logged_on"));
                    user.setLastKnownLocationTime(rs.getTimestamp("last_known_location_time"));
                    user.setLastKnownLocation(rs.getString("last_known_location"));
                    user.setCreatedOn(rs.getTimestamp("created_on"));
                    user.setCreatedBy(rs.getInt("created_by"));
//                    System.out.println("###%^&$$^^"+user.toString());
                    return user;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectTeamLeaders " + query);
            e.printStackTrace();
//            System.out.println("new ArrayList<User>()+++++"+new ArrayList<User>());
            return new ArrayList<User>();
        }
    
    }; // ended by nikhil


    /**
     *
     * @param userId
     * @return
     */
    @Override
    public User selectUser(int userId) {
//        String query = "SELECT id,account_id_fk,first_name,last_name,email_address,password,mobile_number,gender,user_accuracy,check_in_radius,role,report_to,active,last_logged_on,last_known_location_time,last_known_location,last_known_latitude,last_known_langitude,created_on,created_by,designation,allow_timeout,allow_offline,location_interval,emp_code  FROM users WHERE id=?";    // Added by jyoti - currency_symbol
  String query = "SELECT id,account_id_fk,first_name,last_name,full_name,email_address,password,mobile_number,gender,user_accuracy,check_in_radius,role,report_to,active,last_logged_on,last_known_location_time,last_known_location,last_known_latitude,last_known_langitude,created_on,created_by,designation,allow_timeout,allow_offline,location_interval,emp_code,office_latitude,office_langitude  FROM users WHERE id=?";    // Added by jyoti - currency_symbol
        log4jLog.info(" selectUser " + query);
        Object param[] = new Object[]{userId};
        try {
            return this.jdbcTemplate.queryForObject(query, param, new RowMapper<User>() {

                @Override
                public User mapRow(ResultSet rs, int i) throws SQLException {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setAccountId(rs.getInt("account_id_fk"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setFullname(rs.getString("full_name"));  //fresh nikhil
                    user.setEmailAddress(rs.getString("email_address"));
                    user.setPassword(rs.getString("password"));
                    user.setMobileNo(rs.getString("mobile_number"));
                    user.setGender(rs.getInt("gender"));                    
                    //Added by Mayank Ramaiya
                    user.setUserAccuracy(rs.getInt("user_accuracy"));
                    user.setCheckInRadius(rs.getInt("check_in_radius"));
                    //End by Mayank Ramaiya
                    user.setAllowTimeout(rs.getInt("allow_timeout"));
                    // added by jyoti 21-12-2016, used for offline feature , currency symbol
                    user.setAllowOffline(rs.getInt("allow_offline"));
                   // user.setCurrencySymbol(rs.getString("currency_symbol"));
                    // ended by jyoti 
                    user.setRole(rs.getInt("role"));
                    user.setReport_to(rs.getInt("report_to"));
                    user.setActive(rs.getBoolean("active"));
                    user.setLastLoggedOn(rs.getTimestamp("last_logged_on"));
                    user.setLastKnownLocationTime(rs.getTimestamp("last_known_location_time"));
                    user.setLastKnownLocation(rs.getString("last_known_location"));
                    user.setLastKnownLat(rs.getDouble("last_known_latitude"));   // by nikhil
                    user.setLastKnownLong(rs.getDouble("last_known_langitude")); //by nikhil
                    user.setCreatedOn(rs.getTimestamp("created_on"));
                    user.setCreatedBy(rs.getInt("created_by"));
                    user.setDesignation(rs.getString("designation"));
                    //added by nikhil 23rd june 2017
                    user.setInterval(rs.getString("location_interval"));
                    user.setEmp_code(rs.getString("emp_code"));
                    user.setOfficeLocationLat(rs.getString("office_latitude"));
                    user.setOfficeLocationLong(rs.getString("office_langitude"));
                    //ended by nikhil
                    return user;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectUser " + e);
            e.printStackTrace();
            return new User();
        }
    }

    /**
     *
     * @param user
     * @return
     */

    // by nikhil 
    public String selectlocationInterval(int userId) {
        String query = "SELECT location_interval FROM users WHERE id=?";
        log4jLog.info(" selectlocationInterval " + query);
        Object param[] = new Object[]{userId};
        try {
            return this.jdbcTemplate.queryForObject(query, param, String.class); 
          //  {
//
//                @Override
//                public User mapRow(ResultSet rs, int i) throws SQLException {
//                    User user = new User();
//                    
//                    user.setLocationInterval(rs.getString("location_interval"));
//                    return user;
//                }
         //   });
        } catch (Exception e) {
            log4jLog.info(" selectlocation interval " + e);
//            e.printStackTrace();
            return "";
        }
    }
    
    @Override
    public int insertUser(User user) {
//        String query = "INSERT INTO users(account_id_fk, first_name, last_name, email_address, password, mobile_number, gender, role, active, last_logged_on, last_known_location_time, last_known_location, created_on, created_by,designation,accountcontact_type) VALUES (?,?,?,?,?,?,?,?,?,now(),now(),?,now(),?,?,?)";
        String query = "INSERT INTO users(account_id_fk, first_name, last_name,full_name, email_address, password, mobile_number, user_accuracy, check_in_radius, gender, role, report_to, active, last_logged_on, last_known_location_time, last_known_location, created_on, created_by,designation,allow_timeout,allow_offline,accountcontact_type,emp_code,is_terms_condition_agreed,is_newsletter_opt_in) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now(),?,now(),?,?,?,?,?,?,2,2)"; //added by nikhil.
        log4jLog.info(" insertUser " + query);
        Object param[] = new Object[]{user.getAccountId(), user.getFirstName(), user.getLastName(), user.getFirstName() + " " + user.getLastName(), user.getEmailAddress(), user.getPassword(), user.getMobileNo(), user.getUserAccuracy(), user.getCheckInRadius(), user.getGender(), user.getRole(), user.getReport_to(),user.isActive(), user.getLastKnownLocation(), user.getCreatedBy(), user.getDesignation(),user.getAllowTimeout(),user.getAllowOffline(),user.getAccountContactType(),user.getEmp_code()};
        //System.out.println("off" +user.getAllowOffline());
        try {
            synchronized (this) {
                if (this.jdbcTemplate.update(query, param) > 0) {
                    try {
                        String query1 = "SELECT MAX(id) FROM users";
                        log4jLog.info(" insertUser " + query1);
                        return this.jdbcTemplate.queryForObject(query1, Integer.class);
                    } catch (Exception e) {
                        log4jLog.info(" insertUser " + e);
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            log4jLog.info(" insertUser " + e);
            e.printStackTrace();
            return 0;
        }
    }
    
    //  added by manohar     

    /**
     *
     * @param userId
     * @return
     */
    @Override
    public User deleteUser(int userId) {
        String query = "DELETE FROM users WHERE id=?";
        log4jLog.info(" deleteUser " + query);
        Object param[] = new Object[]{userId};
        User user = new User();
        user = selectUser(userId);
        try {
            if (this.jdbcTemplate.update(query, param) > 0) {
                return user;
            } else {
                return new User();
            }
        } catch (Exception e) {
            log4jLog.info(" deleteUser " + e);
//            e.printStackTrace();
            return new User();
        }
    }

    /**
     *
     * @param user
     * @return
     */
    @Override
    public boolean updateUserLastKnownDetails(User user) {
//        String query = "UPDATE users SET last_known_location_time=now(),last_known_location=?,last_known_latitude=? ,last_known_langitude=?,app_version=?  WHERE id=?"; // commented by jyoti, removed app_version
        String query = "UPDATE users SET last_known_location_time=now(),last_known_location=?,last_known_latitude=? ,last_known_langitude=?  WHERE id=?";
        log4jLog.info(" updateUserLastKnownLocation " + query);
//        Object param[] = new Object[]{user.getLastKnownLocation(), user.getLatitude(), user.getLangitude(),user.getVersionCode(), user.getId()}; // commented by jyoti
        Object param[] = new Object[]{user.getLastKnownLocation(), user.getLatitude(), user.getLangitude(), user.getId()};
        try {
            synchronized (this) { //doubtfull about using sync block .#siddhesh while code review task.
                if (jdbcTemplate.update(query, param) > 0) {
                    return true;
                } else {
                    return false;
                }
            }    
        } catch (Exception e) {
            log4jLog.info(" updateUserLastKnownLocation " + e);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     *
     * @param user
     * @return
     */
    @Override
    public boolean updateUserLastKnownDetails(UserOld user) {
        String query = "UPDATE users SET last_known_location_time=now(),last_known_location=?,last_known_latitude=? ,last_known_langitude=?,app_version=?   WHERE id=?";
        log4jLog.info(" updateUserLastKnownLocation " + query);
        Object param[] = new Object[]{user.getLastKnownLocation(), user.getLatitude(), user.getLangitude(),user.getVersionCode(), user.getId()};
        try {
            synchronized (this) {
                if (jdbcTemplate.update(query, param) > 0) {
                    return true;
                } else {
                    return false;
                }
            }    
        } catch (Exception e) {
            log4jLog.info(" updateUserLastKnownLocation " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param password
     * @return
     */
    @Override
    public boolean resetPassword(Password password) {
        String query = "UPDATE users SET password=? WHERE email_address=?";
        log4jLog.info(" resetPassword " + query);
        Object param[] = new Object[]{password.getNewPassword(), password.getEmailId()};
        try {
            if (jdbcTemplate.update(query, param) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" resetPassword " + e);
            return false;
        }
    }
    
    /**
     *
     * @param user
     * @return
     */
    @Override
    public boolean updateUserDetails(User user) {                                                                                                                                                                                                                                               
        String query = "UPDATE users SET first_name=?,last_name=?,gender=?,full_name=?,password=?,mobile_number=?,user_accuracy=?,check_in_radius=?,active=?,role=?,designation=?,allow_timeout=?,allow_offline=?,report_to=?,location_interval=?,updated_on=now(),updated_by=?,emp_code=? WHERE id=?";  //modified by manohar
        Object param[] = new Object[]{user.getFirstName(), user.getLastName(),user.getGender(), user.getFirstName() + " " + user.getLastName(), user.getPassword(), user.getMobileNo(), user.getUserAccuracy(), user.getCheckInRadius(), user.isActive(), user.getRole(), user.getDesignation(),user.getAllowTimeout(),user.getAllowOffline(),user.getReport_to(),user.getInterval(),user.getUpdatedBy(),user.getEmp_code(),user.getId()};

//            System.out.println("devaa ashirvad"+user.getInterval());
        try {
            if (jdbcTemplate.update(query, param) == 1) {
                if (!user.isActive()) {
                    String deleteQuery = "DELETE FROM user_auth WHERE user_id_fk=?";
                    Object deleteParam[] = new Object[]{user.getId()};
                    jdbcTemplate.update(deleteQuery, deleteParam);
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateUserDetails " + e);
            return false;
        }
    }

    /**
     *
     * @param user
     * @return
     */
    @Override
    public boolean updateUserDetailsForUser(User user) {
        String query = "UPDATE users SET first_name=?,last_name=?,full_name=?,mobile_number=?,gender=?,designation=? WHERE id=?";
        log4jLog.info(" updateUserDetailsForUser " + query);
        
        
        Object param[] = new Object[]{user.getFirstName(), user.getLastName(), user.getFirstName() + " " + user.getLastName(), user.getMobileNo(), user.getGender(), user.getDesignation(), user.getId()};
        try {
            if (jdbcTemplate.update(query, param) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateUserDetailsForUser " + e);
            return false;
        }
    }

    /**
     *
     * @param accountId
     * @return
     */
    @Override
    public List<User> selectUsersExceptTeamMembers(int accountId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT id,account_id_fk,first_name,last_name,email_address,password,mobile_number,gender,user_accuracy,check_in_radius,report_to,role,active,last_logged_on,allow_timeout,allow_offline");
        query.append(" last_known_location_time,last_known_location,created_on,created_by FROM fieldsense.users WHERE (id NOT IN (SELECT user_id_fk FROM team_members) AND role=3 AND active=1) AND account_id_fk= ?");
        Object param[] = new Object[]{accountId};
        log4jLog.info(" selectUsersExceptTeamMembers " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<User>() {

                @Override
                public User mapRow(ResultSet rs, int i) throws SQLException {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setAccountId(rs.getInt("account_id_fk"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmailAddress(rs.getString("email_address"));
                    user.setPassword(rs.getString("password"));
                    user.setMobileNo(rs.getString("mobile_number"));
                    user.setGender(rs.getInt("gender"));
                    
                    //Added by Mayank Ramaiya
                    user.setUserAccuracy(rs.getInt("user_accuracy")); 
                    user.setCheckInRadius(rs.getInt("check_in_radius"));
                    //End by Mayank Ramaiya
                    user.setAllowTimeout(rs.getInt("allow_timeout"));
                    
                    // added by jyoti 21-12-2016, used for offline feature
                    user.setAllowOffline(rs.getInt("allow_offline"));
                    
                    user.setRole(rs.getInt("role"));
                    user.setReport_to(rs.getInt("report_to"));
                    user.setActive(rs.getBoolean("active"));
                    user.setLastLoggedOn(rs.getTimestamp("last_logged_on"));
                    user.setLastKnownLocationTime(rs.getTimestamp("last_known_location_time"));
                    user.setLastKnownLocation(rs.getString("last_known_location"));
                    user.setCreatedOn(rs.getTimestamp("created_on"));
                    user.setCreatedBy(rs.getInt("created_by"));
                    return user;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectUsersExceptTeamMembers " + e);
//            e.printStackTrace();
            return new ArrayList<User>();
        }
    }

    /**
     *
     * @param emailAddres
     * @return
     */
    @Override
    public String getUserPassword(String emailAddres) {
        String query = "SELECT password FROM users WHERE email_address=?";
        log4jLog.info(" getUserPassword " + query);
        try {
            Object param[] = new Object[]{emailAddres};
            return jdbcTemplate.queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" getUserPassword " + e);
//            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param user
     * @return
     */
    @Override
    public boolean updateOfficeLatlong(User user) {
        String query = "UPDATE users SET office_latitude=?,office_langitude=? WHERE id=?";
        log4jLog.info(" updateOfficeLatlong " + query);
        Object param[] = new Object[]{user.getLatitude(), user.getLangitude(), user.getId()};
        try {
            if (jdbcTemplate.update(query, param) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateOfficeLatlong " + e);
            return false;
        }
    }

    /**
     *
     * @param user
     * @return
     */
    @Override
    public boolean updateHomeLatlong(User user) {
        String query = "UPDATE users SET home_latitude=?,home_langitude=? WHERE id=?";
        log4jLog.info(" updateHomeLatlong " + query);
        Object param[] = new Object[]{user.getLatitude(), user.getLangitude(), user.getId()};
        try {
            if (jdbcTemplate.update(query, param) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateHomeLatlong " + e);
            return false;
        }
    }

    /**
     *
     * @return
     */
    @Override
    public List<String> getCreateUserDeafultQueries() {
        String query = "SELECT sql_query FROM create_user_deafult_data ORDER BY id ";
        log4jLog.info("getCreateUserDeafultQueries " + query);
//        Object[] param = new Object[]{queryType + "%"};
        try {
            return jdbcTemplate.queryForList(query, String.class);
        } catch (Exception e) {
            log4jLog.info("getCreateUserDeafultQueries " + query);
//            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param queryList
     * @param userId
     * @param accountId
     * @return
     */
    @Override
    public boolean executeCreateUserDeafultQueries(List<String> queryList, int userId, int accountId) {
        Connection connection = null;
        PreparedStatement stmt=null;
        try {
            
            String communityDbName = "account_" + accountId;
            connection = (Connection) DriverManager.getConnection(Constant.DATA_BASE_CONNECTION_URL + communityDbName, Constant.DEFAULT_COMMUNITY_DB_USERNAME, Constant.DEFAULT_COMMUNITY_DB_PASSWORD);
            //PreparedStatement stmt;
            for (String insertQuery : queryList) {
                stmt = (PreparedStatement) connection.prepareStatement(insertQuery);
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }
            return true;
        } catch (Exception e) {
            log4jLog.info("executeCreateAccountQueries " + e);
//            e.printStackTrace();
            return false;
        }finally{
            try{
                if(stmt!=null) stmt.close();
                if(connection!=null) connection.close();                               
            }catch(Exception e){
//                e.printStackTrace();
                log4jLog.info("executeCreateAccountQueries " + e);
            }
        }
    }

    /**
     *
     * @param accountId
     * @return
     */
    @Override
    public List<User> selectTeamLeaders(int accountId) {
        String query = "SELECT id,first_name,last_name FROM users WHERE account_id_fk=? AND role=2";
        log4jLog.info(" selectTeamLeaders " + query);
        Object param[] = new Object[]{accountId};
        try {
            return this.jdbcTemplate.query(query, param, new RowMapper<User>() {

                @Override
                public User mapRow(ResultSet rs, int i) throws SQLException {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    return user;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectTeamLeaders " + query);
//            e.printStackTrace();
            return new ArrayList<User>();
        }
    }

    /**
     *
     * @param accountId
     * @return
     */
    @Override
    public List<TeamMember> slectAllUsersForMobile(int accountId) {
        String query = "SELECT id,account_id_fk,first_name,last_name,email_address,password,mobile_number,gender,user_accuracy,check_in_radius,role,active,last_logged_on,last_known_location_time,last_known_location,created_on,created_by FROM users WHERE account_id_fk=? AND role!=0 AND active!=0";
        log4jLog.info(" slectAllUsersForMobile " + query);
        Object param[] = new Object[]{accountId};
        try {
            return this.jdbcTemplate.query(query, param, new RowMapper<TeamMember>() {

                @Override
                public TeamMember mapRow(ResultSet rs, int i) throws SQLException {
                    TeamMember teamMember = new TeamMember();
                    teamMember.setTeamId(rs.getInt("id"));
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    teamMember.setUser(user);
                    return teamMember;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" slectAllUsersForMobile " + e);
//            e.printStackTrace();
            return new ArrayList<TeamMember>();
        }
    }
    
    /**
     *
     * @param mobile
     * @return
     */
    @Override
    public List getUsersFromMobileNo(String mobile){
        Object param[] =null;
        String query="";
        String extMobile="";
        if(mobile.startsWith("+")){           
             if(mobile.startsWith("+91")){
                 extMobile=mobile.substring(3);
                 query = "SELECT  id,first_name,last_name,mobile_number from users where (mobile_number=? OR mobile_number=? )";
                 param=new Object[]{mobile,extMobile};
             }else{
                 query = "SELECT  id,first_name,last_name,mobile_number from users where mobile_number=?";
                 param=new Object[]{mobile};
             }
         }else{
             //if()
             extMobile="+91"+mobile;
             query = "SELECT  id,first_name,last_name,mobile_number from users where (mobile_number=? OR mobile_number=? )";
             param=new Object[]{mobile,extMobile};
         }
        try {
            return this.jdbcTemplate.query(query, param, new RowMapper<User>() {

                @Override
                public User mapRow(ResultSet rs, int i) throws SQLException {
 
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setMobileNo(rs.getString("mobile_number"));
                    return user;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" slectAllUsersFromMobileNo " + e);
//            e.printStackTrace();
            return new ArrayList<User>();
        }
    }
    
    /**
     *
     * @param mobile
     * @param otp
     * @return
     */
    @Override
    public int insertOTPForUser(String mobile, String otp) {
        /*String query="";
         String excludingCountry="";
         String mobileWithINCountry="";
         Object param[] = null;

         if(mobile.startsWith("+")){
            
             if(mobile.startsWith("+91")){
                 excludingCountry=mobile.substring(3);
                 query = "UPDATE users SET otp=? ,otp_expiry=now()  WHERE mobile_number=? OR mobile_number=?";
                 param=new Object[]{otp,mobile,excludingCountry};
             }else{
                 query = "UPDATE users SET otp=? ,otp_expiry=now()  WHERE mobile_number=? ";
                 param=new Object[]{otp,mobile};
             }
         }else{
             //if()
             mobileWithINCountry="+91"+mobile;
             query = "UPDATE users SET otp=? ,otp_expiry=now()  WHERE mobile_number=? OR mobile_number=?";
             param=new Object[]{otp,mobile,mobileWithINCountry};

         }*/
            
        String query = "UPDATE users SET otp=? ,otp_expiry=now()  WHERE mobile_number=? ";
        log4jLog.info(" insertOTPForUser " + query);
        Object param[] = new Object[]{otp,mobile};

        try{
            int count=jdbcTemplate.update(query,param);
            return count;
        } catch (Exception e) {
            log4jLog.info(" insertAuthDetails " + e);
//            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     *
     * @param mobile
     * @return
     */
    @Override
    public java.util.List getOTPAndExpiryForUser(String mobile) {
        String query = "SELECT  otp,otp_expiry from users where mobile_number=?";
        log4jLog.info(" selectOfficeLatLong " + query);
        
        //java.util.List result= new java.util.ArrayList();
        Object param[] = new Object[]{mobile};
        /*Object param[] =null;
        String query="";
        String extMobile="";
        if(mobile.startsWith("+")){
            
             if(mobile.startsWith("+91")){
                 extMobile=mobile.substring(3);
                 query = "SELECT  otp,otp_expiry from users where mobile_number=? OR mobile_number=? ";
                 param=new Object[]{mobile,extMobile};
             }else{
                 query = "SELECT  otp,otp_expiry from users where mobile_number=?";
                 param=new Object[]{mobile};
             }
         }else{
             //if()
             extMobile="+91"+mobile;
             query = "SELECT  otp,otp_expiry from users where mobile_number=? OR mobile_number=? ";
             param=new Object[]{mobile,extMobile};

         }*/
        try {
            return jdbcTemplate.queryForObject(query, param, new RowMapper<java.util.List>() {

                @Override
                public java.util.List mapRow(ResultSet rs, int i) throws SQLException {
                    java.util.List result = new java.util.ArrayList();
                    result.add(rs.getString("otp"));
                    result.add(rs.getTimestamp("otp_expiry"));

                    return result;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" getOTPAndExpiryForUser " + e);
            return null;
        }
    }
    
    /**
     *
     * @param password
     * @param mobile
     * @return
     */
    @Override
    public boolean resetPasswordUsingPhone(String password,String mobile) {
        String query = "UPDATE users SET password=? WHERE mobile_number=?";
        log4jLog.info(" resetPassword " + query);
        Object param[] = new Object[]{password, mobile};
        /*Object param[] =null;
        String query="";
        String extMobile="";
        if(mobile.startsWith("+")){
            
             if(mobile.startsWith("+91")){
                 extMobile=mobile.substring(3);
                 query = "UPDATE users SET password=? WHERE mobile_number=? OR mobile_number=?";
                 param=new Object[]{password,mobile,extMobile};
             }else{
                 query = "UPDATE users SET password=? WHERE mobile_number=?";
                 param=new Object[]{password,mobile};
             }
         }else{
             //if()
             extMobile="+91"+mobile;
             query = "UPDATE users SET password=? WHERE mobile_number=? OR mobile_number=?";
             param=new Object[]{password,mobile,extMobile};

         }*/
        try {
            if (jdbcTemplate.update(query, param) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" resetPassword " + e);
            return false;
        }
    }
    
    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public int getParentTeamId(int userId, int accountId) {
        String query = "SELECT team_id_fk FROM team_members WHERE user_id_fk=? and member_type='3'";
        log4jLog.info(" selectTeams " + query);
        Object[] param = new Object[]{userId};
        try {
              return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
            }
           catch (Exception e) {
            log4jLog.info(" selectTeamMember " + e);
            return 0;
        }
    } 
     
    /* @Override
    public Timestamp getLastLocationUpdateTime(int id) {
        //String query = "UPDATE users SET password=? WHERE mobile_number=?";
        String query="SELECT last_known_location_time  from users where id=?";
        log4jLog.info(" getLastLocationUpdateTime " + query);
        Object param[] = new Object[]{id};
     
        try {
            return this.jdbcTemplate.queryForObject(query, param, Timestamp.class);
            
        } catch (Exception e) {
            log4jLog.info(" getLastLocationUpdateTime " + e);
            return null;
        }
    }*/

    /**
     *
     * @param accountId
     * @param topId
     * @return
     */
    
    
    @Override
    public List<HashMap> slectAllUsersForReportTo(int accountId,int topId){
        
        String query = "SELECT id,full_name,role,active FROM users WHERE (account_id_fk=? and report_to !=0) or id =? ORDER BY full_name";
        log4jLog.info(" slectAllUsersForReportTo " + query);
        Object param[] = new Object[]{accountId,topId};
        try {
            return this.jdbcTemplate.query(query, param, new RowMapper<HashMap>() {

                @Override
                public HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    HashMap user = new HashMap();
                    user.put("id",rs.getInt("id"));
                    user.put("fullName",rs.getString("full_name"));
                    user.put("role",rs.getInt("role"));
                    user.put("active",rs.getBoolean("active"));
                    return user;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" slectAllUsers " + e);
//            e.printStackTrace();
            return new ArrayList<HashMap>();
        }
        //return null;
    }
    
    /**
     *
     * @param accountId
     * @return
     */
    @Override
    public int getTopUserInHierarachy( int accountId) {
        String query = "SELECT user_id_fk FROM teams WHERE id='100000'";
        log4jLog.info(" selectTeams " + query);
        //Object[] param = new Object[]{userId};
        try {
              return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, Integer.class);
            }
           catch (Exception e) {
            log4jLog.info("top user  " + e);
            return 0;
        }
    }
    
    /**
     *
     * @param addTerritoryList
     * @param deleteTerritoryList
     * @param userId
     * @param accountId
     */
    @Override
    public void insertUserTerritories(List<Integer> addTerritoryList,List<Integer> deleteTerritoryList,int userId,int accountId){
        String query = "INSERT into user_territory(teritory_id,user_id_fk,created_on) VALUES(?,?,now())";
        log4jLog.info(" insert user  territory " + query);
        List<Object[]> paramList = new ArrayList<Object[]>();
        try {
            if(!addTerritoryList.isEmpty()){
                for(Integer terrList:addTerritoryList){
                    Object[] tmp = {terrList,userId};
                    paramList.add(tmp);
                }
                FieldSenseUtils.getJdbcTemplateForAccount(accountId).batchUpdate(query, paramList);
            } 
            if(!deleteTerritoryList.isEmpty()){
                String query1 = "DELETE from user_territory where (teritory_id=? ";
                Object[] paramList1 = new Object[deleteTerritoryList.size()+1];
                paramList1[0]=deleteTerritoryList.get(0);
                for(int i=1;i<deleteTerritoryList.size();i++){
                    query1+=" OR teritory_id=? ";
                    paramList1[i] = deleteTerritoryList.get(i);
                }
                query1+=") and user_id_fk=?";
                paramList1[paramList1.length-1]=userId;
                FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query1, paramList1);
            }
        }catch (Exception e) {
//            e.printStackTrace();
            log4jLog.info("insert user  territory" + e);
        }
    }

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    @Override
    public int insertUserTerritoryUnknownForCSVFile(int userId, int accountId) {
        
        String query = "INSERT into user_territory(teritory_id,user_id_fk,created_on) VALUES(1,?,now())";   // 1 -> Unknown
        log4jLog.info(" insertUserTerritoryUnknownForCSVFile " + query);
        Object[] param = new Object[]{userId};
       try {
            synchronized (this) {
                 
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    try {
                        String query1 = "SELECT MAX(id) FROM user_territory";
                        log4jLog.info(" insertUserTerritoryUnknownForCSVFile " + query1);                        
                        return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query1, Integer.class);
                    } catch (Exception e) {
                        log4jLog.info(" insertUserTerritoryUnknownForCSVFile " + e);
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            log4jLog.info(" insertUserTerritoryUnknownForCSVFile " + e);
            //e.printStackTrace();
            return 0;
        }
        
    }

    /**
     * Added by Jyoti
     * @return get id of users whose report_to = userId
     */
    @Override
    public List<Integer> getListOfReporterOfUserID(int userId) {
        String query = "SELECT id FROM users WHERE report_to = ? ";
        Object[] param = new Object[]{userId};
        try {
            return jdbcTemplate.queryForList(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info("getListOfReporterOfUserID " + query);
//            e.printStackTrace();
            return null;
        }
    }

    public User deleteAdmin(int accountId) {
         String query = "DELETE FROM users WHERE account_id_fk=?";
        log4jLog.info(" deleteUser " + query);
        Object param[] = new Object[]{accountId};
        User user = new User();
        user = selectUser(accountId);
        try {
            if (this.jdbcTemplate.update(query, param) > 0) {
                return user;
            } else {
                return new User();
            }
        } catch (Exception e) {
            log4jLog.info(" deleteUser " + e);
//            e.printStackTrace();
            return new User();
        }
    }

    public User deleteAccount(int accountId) {
        String query = "DELETE FROM accounts WHERE id=?";
        log4jLog.info(" deleteUser " + query);
        Object param[] = new Object[]{accountId};
        User user = new User();
        user = selectUser(accountId);
        try {
            if (this.jdbcTemplate.update(query, param) > 0) {
                return user;
            } else {
                return new User();
            }
        } catch (Exception e) {
            log4jLog.info(" deleteAccount " + e);
//            e.printStackTrace();
            return new User();
        }
    }

  
    public boolean  deleteAdminEditTemp(int userId) {
       String query = "DELETE FROM users WHERE id=?";
        Object param[] = new Object[]{userId};
        try {
            if (this.jdbcTemplate.update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" deleteAppointment " + e);
//            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser_Account_punchin_stats(int accountId) {
        String query = "DELETE FROM accounts_punchin_stats WHERE account_id=?";
        Object param[] = new Object[]{accountId};
        try {
            if (this.jdbcTemplateStats.update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" deleteAccountFromaccounts_punchin_stats " + e);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * @Added by siddhesh
     * @param userId
     * @return
     */
    @Override
    public HashMap selectUserForOffline(int userId, int accountId) {
        final int account_id=accountId;
        String query = "SELECT id,allow_timeout,allow_offline,location_interval FROM users WHERE id=?";   
        try{
        Object[] param = new Object[] {userId};
        return this.jdbcTemplate.queryForObject(query, param, new RowMapper<HashMap>() {
                    public HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    HashMap<String,String> data=new HashMap<String,String>();
                    data.put("id",Integer.toString(rs.getInt("id")));
                    data.put("allowTimeout",Integer.toString(rs.getInt("allow_timeout")));
                    data.put("allowOffline", Integer.toString(rs.getInt("allow_offline")));
                    String locationInterval=Integer.toString(rs.getInt("location_interval"));
                    if(locationInterval.equals("0")){
                        Object param[] = new Object[]{"Location_interval"};
                     locationInterval= FieldSenseUtils.getJdbcTemplateForAccount(account_id).queryForObject("select value from account_settings where name=?",param,String.class);
                    }
                    data.put("locationInterval",locationInterval);
                    return data;
                    }
        });
        }catch(Exception e){
        return new HashMap();
        }
    }
    
    /**
     * @param versionCodeFromMobile
     * @Added by jyoti, 13-02-2018 purpose - to update app version code
     * @param userId
     * @return 
     */
    @Override
    public boolean updateUserAppVersion(int userId, int versionCodeFromMobile){
        log4jLog.info(" updateUserAppVersion userid : " + userId+" , appVersion : "+versionCodeFromMobile);
        try{
            String query = "UPDATE users SET app_version = ? WHERE id = ?";
            Object param[] = new Object[]{versionCodeFromMobile, userId};
            return this.jdbcTemplate.update(query, param) > 0;
            
        } catch(Exception e){
            e.printStackTrace();
            log4jLog.info(" updateUserAppVersion " + e);
            return false;
        }        
    }

    @Override
    public List<HashMap> getUsers(int accountId,int userId,String fromDate,String toDate,int start, int end) {
         java.sql.Connection connection = null;
        java.sql.PreparedStatement stmt = null;
        if(userId==0){
//            System.out.println("user id "+userId + "is zero");
              StringBuffer query = new StringBuffer();
         query.append("select id as id ,full_name as full_name from fieldsense.users where account_id_fk=? and role !=0 order by full_name");
         if(end!=-1){
              query.append(" limit ? offset ?");  
            }
        List<HashMap> listOfFinalData = new ArrayList<HashMap>();
        try{
            connection = FieldSenseUtils.getDataSourceForAccount(accountId).getConnection();
            stmt = connection.prepareStatement(query.toString());
             stmt.setInt(1, accountId);
             if(end!=-1){
                stmt.setInt(2, end);
                 stmt.setInt(3, start);
             }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                HashMap<Object,Object> mapData = new HashMap<Object,Object>();
                mapData.put("userId", rs.getInt("id"));
                mapData.put("fullName", rs.getString("full_name"));
                listOfFinalData.add(mapData);
            }
            connection.close();
            stmt.close();
            return listOfFinalData;
        }catch(Exception e){
            try {
                e.printStackTrace();
                connection.close();
                stmt.close();
                return listOfFinalData;
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(UserDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
                 return listOfFinalData;
            }
        }
        }else{
//            System.out.println("user id "+userId + "is not  zero");
      String query = "select id as id ,full_name as full_name from fieldsense.users where id=?";     
        List<HashMap> listOfFinalData = new ArrayList<HashMap>();
        try{
            connection = FieldSenseUtils.getDataSourceForAccount(accountId).getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                HashMap<Object,Object> mapData = new HashMap<Object,Object>();
                mapData.put("userId", rs.getInt("id"));
                mapData.put("fullName", rs.getString("full_name"));
                listOfFinalData.add(mapData);
            }
            connection.close();
            stmt.close();
            return listOfFinalData;
        }catch(Exception e){
            e.printStackTrace();
            try {
                connection.close();
                stmt.close();
                return listOfFinalData;
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(UserDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
                 return listOfFinalData;
            }
        }
        }
    }

    @Override
    public List<HashMap> getUsersForSubordinate(int accountId, ArrayList subordateList, int userId, String fromDate, String toDate, int start , int end) {
               java.sql.Connection connection = null;
        java.sql.PreparedStatement stmt = null;
        if(userId==0){
//            System.out.println("user id "+userId + "is zero "+subordateList.size());
       
        List<HashMap> listOfFinalData = new ArrayList<HashMap>();
        try{
            connection = FieldSenseUtils.getDataSourceForAccount(accountId).getConnection();
             StringBuffer buffer = new StringBuffer();
            for(int k=0;k<subordateList.size();k++){
                    buffer.append(subordateList.get(k));
                    if(k != (subordateList.size()-1)){
                        buffer.append(",");
                    }
            }
            StringBuffer query = new StringBuffer();
            query.append("select id as id ,full_name as full_name from fieldsense.users where id IN ("+buffer.toString()+") and role!=0 order by full_name");
            if(end!=-1){
              query.append(" limit ? offset ?");  
            }
//            System.out.println("buffer  "+buffer.toString());
            stmt = connection.prepareStatement(query.toString());
            if(end!=-1){
                stmt.setInt(1, end);
                 stmt.setInt(2, start);
             }
//            System.out.println("Query "+query.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                HashMap<Object,Object> mapData = new HashMap<Object,Object>();
                mapData.put("userId", rs.getInt("id"));
                mapData.put("fullName", rs.getString("full_name"));
                listOfFinalData.add(mapData);
            }
            connection.close();
            stmt.close();
            return listOfFinalData;
        }catch(Exception e){
            try {
                e.printStackTrace();
                connection.close();
                stmt.close();
                return listOfFinalData;
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(UserDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
                 return listOfFinalData;
            }
        }
        }else{
//            System.out.println("user id "+userId + "is not  zero");
      String query = "select id as id ,full_name as full_name from fieldsense.users where id=?";     
        List<HashMap> listOfFinalData = new ArrayList<HashMap>();
        try{
            connection = FieldSenseUtils.getDataSourceForAccount(accountId).getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                HashMap<Object,Object> mapData = new HashMap<Object,Object>();
                mapData.put("userId", rs.getInt("id"));
                mapData.put("fullName", rs.getString("full_name"));
                listOfFinalData.add(mapData);
            }
            connection.close();
            stmt.close();
            return listOfFinalData;
        }catch(Exception e){
            e.printStackTrace();
            try {
                connection.close();
                stmt.close();
                return listOfFinalData;
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(UserDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
                 return listOfFinalData;
            }
        }
        }
    }
   
}
