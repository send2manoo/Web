/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.dashboard.dao;

import com.qlc.fieldsense.appointments.model.Appointment;
import com.qlc.fieldsense.customer.model.Customer;
import com.qlc.fieldsense.dashboard.model.AppointmentStats;
import com.qlc.fieldsense.dashboard.model.AttendanceStats;
import com.qlc.fieldsense.dashboard.model.AttendanceStatsUsers;
import com.qlc.fieldsense.dashboard.model.CustomFormStats;
import com.qlc.fieldsense.dashboard.model.CustomerStats;
import com.qlc.fieldsense.dashboard.model.ExpenseStats;
import com.qlc.fieldsense.dashboard.model.LeaderboardStats;
import com.qlc.fieldsense.expense.model.Expense;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author root
 */
public class DashboardStatsDaoImpl implements DashboardStatsDao{
    public static final Logger log4jLog = Logger.getLogger("DashboardStatsDaoImpl");


    
        
    @Override
    public List<CustomerStats> selectCustomerStats(int accountId,int teamId){
        
    StringBuilder query = new StringBuilder();
    query.append("select count(cus.id) AS countofCustomer from customers cus INNER JOIN fieldsense.users u ON cus.created_by_id_fk=u.id ");
    query.append("where cus.created_on between CONCAT(curdate(),' 00:00:00') and CONCAT(curdate(),' 23:59:59') ");
    query.append("AND u.id IN(select DISTINCT user_id_fk from teams where team_position_csv like ?)");
//    System.out.println("query="+query);
    log4jLog.info(" selectCustomerStats " + query);
    Object param[] = new Object[]{"%" + teamId + "%"};
    try {
      return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<CustomerStats>() {

            @Override
            public CustomerStats mapRow(ResultSet rs, int i) throws SQLException {

                CustomerStats customerstats = new CustomerStats();
                customerstats.setCountofCustomer(rs.getInt("countofCustomer"));  
                return customerstats;
            }
       }); 
    }catch (Exception e) {
        log4jLog.info(" selectCustomerStats " + e);
        e.printStackTrace();
       return new ArrayList<CustomerStats>();
    } 
}
    
    
    
    @Override
    public List<LeaderboardStats> selectLeaderboardStats(int accountId,int teamId){
        
    StringBuilder query = new StringBuilder();
    query.append("select u.id,u.full_name ,count(app.id) As countofVisits from appointments app ");
    query.append("INNER JOIN fieldsense.users u on u.id=app.assigned_id_fk where ");
    query.append("app.created_on between CONCAT(curdate(),' 00:00:00') and CONCAT(curdate(),' 23:59:59') ");
    query.append(" and app.user_id_fk IN (select user_id_fk from ");
    query.append(" teams where team_position_csv like ?) and app.status=2  group by u.full_name");
    
//    System.out.println("query="+query);
    log4jLog.info(" selectLeaderboardStats " + query);
    Object param[] = new Object[]{"%" + teamId + "%"};
    try {
      return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<LeaderboardStats>() {

            @Override
            public LeaderboardStats mapRow(ResultSet rs, int i) throws SQLException {

                LeaderboardStats leaderboardstats = new LeaderboardStats();
                leaderboardstats.setVisitCount(rs.getInt("countofVisits"));
               
                User user=new User();
                user.setId(rs.getInt("u.id"));
                user.setFullname(rs.getString("u.full_name"));   
                leaderboardstats.setUser(user);
                
                return leaderboardstats;
            }
       }); 
    }catch (Exception e) {
        log4jLog.info(" selectLeaderboardStats " + e);
        e.printStackTrace();
       return new ArrayList<LeaderboardStats>();
    } 
}
    
    
    
    @Override
    public List<ExpenseStats> selectExpenseStats(int accountId,int teamId){
    StringBuilder query = new StringBuilder();
    query.append("select ex.id,ex.user_id_fk,ex.amount_spent,ex.disburse_amount,ex.status ");
    query.append("from expenses ex  where ex.created_on between ");
    query.append("CONCAT(curdate(),' 00:00:00') and CONCAT(curdate(),' 23:59:59') and ");
    query.append("ex.user_id_fk IN (select user_id_fk from teams where team_position_csv LIKE ?) ");

//    System.out.println("query="+query);
    log4jLog.info(" selectExpenseStats " + query);
    Object param[] = new Object[]{"%" + teamId + "%"};
    try {
      return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<ExpenseStats>() {

            @Override
            public ExpenseStats mapRow(ResultSet rs, int i) throws SQLException {

                ExpenseStats expensestats = new ExpenseStats();
                
                Expense expense=new Expense();
                expense.setId(rs.getInt("ex.id"));
                expense.setAmountSpent(rs.getDouble("ex.amount_spent"));
                expense.setDisburse_amount(rs.getDouble("ex.disburse_amount"));
                expense.setStatus(rs.getInt("ex.status"));
                expensestats.setExpense(expense);
                
                User user=new User();
                user.setId(rs.getInt("ex.user_id_fk"));
                expensestats.setUser(user);
                
                return expensestats;
            }
       }); 
    }catch (Exception e) {
        log4jLog.info(" selectExpenseStats " + e);
        e.printStackTrace();
       return new ArrayList<ExpenseStats>();
    } 
}
    
    
    @Override
    public List<AttendanceStats> selectAttendanceStats(int accountId,int teamId){
    StringBuilder query = new StringBuilder();
//    query.append("select u.full_name,IFNULL(att.punch_in,0) punchIn,IFNULL(att.punch_out,0) punchOut,att.punch_in_location,att.user_id_fk,k.user_key,IFNULL(k.key_value,0) inMeeting  ");
//    query.append(",u.last_known_latitude,u.last_known_langitude,u.office_latitude,u.office_langitude,u.last_known_location,u.last_known_location_time from fieldsense.users u INNER JOIN attendances att ON att.user_id_fk=u.id ");
//    query.append("INNER JOIN user_keys k ON k.user_id_fk=u.id where att.created_on between ");
//    query.append("CONCAT(curdate(),' 00:00:00') and CONCAT(curdate(),' 23:59:59') and ");
//    query.append("u.id IN (select distinct user_id_fk from teams where ");
//    query.append("team_position_csv LIKE ?) AND k.user_key='InMeeting'");
    
//    query.append("select u.full_name,IFNULL(att.punch_in,0) punchIn,IFNULL(att.punch_out,0) punchOut,att.punch_in_location,att.user_id_fk,k.user_key,IFNULL(k.key_value,0) inMeeting  ");
//    query.append(",u.last_known_latitude,u.last_known_langitude,u.office_latitude,u.office_langitude,u.last_known_location,u.last_known_location_time from fieldsense.users u INNER JOIN attendances att ON att.user_id_fk=u.id ");
//    query.append("INNER JOIN user_keys k ON k.user_id_fk=u.id where att.punch_date = curdate() and "); // modified by jyoti
//    query.append("u.id IN (select distinct user_id_fk from teams where ");
//    query.append("team_position_csv LIKE ?) AND k.user_key='InMeeting' order by att.punch_in desc ");


query.append("SELECT result.full_name,result.punchIn,result.punchOut,result.punch_in_location,result.user_id_fk, result.user_key,result.inMeeting , ");
query.append("result.last_known_latitude,result.last_known_langitude,result.office_latitude, result.office_langitude,result.last_known_location,result.last_known_location_time ");
query.append("FROM((select u.full_name,IFNULL(att.punch_in,0) ");
query.append("punchIn,IFNULL(att.punch_out,0) punchOut,att.punch_in_location,att.user_id_fk,k.user_key, ");
query.append("IFNULL(k.key_value,0)inMeeting  ,u.last_known_latitude,u.last_known_langitude,u.office_latitude, ");
query.append("u.office_langitude,u.last_known_location,u.last_known_location_time from  ");
query.append("fieldsense.users u INNER JOIN attendances att ON att.user_id_fk=u.id INNER JOIN user_keys k  ");
query.append("ON k.user_id_fk=u.id where att.punch_date = curdate() and u.id IN ");
query.append("(select distinct user_id_fk from teams where team_position_csv LIKE ?) AND k.user_key='InMeeting') ");
query.append("union all    (select u.full_name,'00:00:00' AS punchIn,'00:00:00' AS punchOut,'' AS punch_in_location, ");
query.append("u.id,'' AS user_key,0 AS inMeeting,u.last_known_latitude , ");
query.append("u.last_known_langitude ,0 office_latitude ,0 office_langitude ,u.last_known_location , ");
query.append("u.last_known_location_time  from fieldsense.users u    where u.id IN (select distinct user_id_fk ");
query.append("from teams  where team_position_csv LIKE ?) AND u.id not in ");
query.append("(select user_id_fk from attendances where punch_date = curdate()) and u.active=1)) as result order by result.punchIn desc");



//    System.out.println("query="+query);
    log4jLog.info(" selectAppointmentStats " + query);
    Object param[] = new Object[]{"%" + teamId + "%", "%" + teamId + "%"};
    try {
      return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<AttendanceStats>() {

            @Override
            public AttendanceStats mapRow(ResultSet rs, int i) throws SQLException {

                AttendanceStats attendancestats = new AttendanceStats();
                attendancestats.setUserkey(rs.getString("result.user_key"));
                attendancestats.setKeyValue(rs.getInt("result.inMeeting"));
                attendancestats.setPunchInTime(rs.getTime("result.punchIn").toString());
                attendancestats.setPunchOutTime(rs.getTime("result.punchOut").toString());
                attendancestats.setPunchInLocation(rs.getString("result.punch_in_location"));
                
//                Attendance attendance=new Attendance();
//                attendance.setPunchInLocation(rs.getString("att.punch_in_location"));
//                attendancestats.setAttendance(attendance);

                User user=new User();
                user.setFullname(rs.getString("result.full_name"));
                user.setId(rs.getInt("result.user_id_fk"));
                user.setLatitude(rs.getDouble("result.last_known_latitude"));
                user.setLangitude(rs.getDouble("result.last_known_langitude"));
                user.setOfficeLatitude(rs.getDouble("result.office_latitude"));
                user.setOfficeLangitude(rs.getDouble("result.office_langitude"));
                user.setLastKnownLocation(rs.getString("result.last_known_location"));
                user.setLastKnownLocationTime(rs.getTimestamp("result.last_known_location_time"));
                
                
                attendancestats.setUser(user);

                return attendancestats;
            }
       }); 
    }catch (Exception e) {
        log4jLog.info(" selectAttendanceStats " + e);
        e.printStackTrace();
       return new ArrayList<AttendanceStats>();
    } 
}
    
    @Override
    public List<AttendanceStatsUsers> selectAttendanceStatsUsers(int accountId,int teamId,int userId){
    StringBuilder query = new StringBuilder();
    query.append(" SELECT count(*) as countOfUsers FROM teams t INNER JOIN fieldsense.users u ");
    query.append(" ON u.id=t.user_id_fk  WHERE team_position_csv ");
    query.append(" LIKE ? AND u.active=1");

//    System.out.println("query="+query);
    log4jLog.info(" selectAttendanceStatsUsers " + query);
    Object param[] = new Object[]{"%" + teamId + "%"};
    try {
      return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<AttendanceStatsUsers>() {

            @Override
            public AttendanceStatsUsers mapRow(ResultSet rs, int i) throws SQLException {
                AttendanceStatsUsers attendancestatsUsers = new AttendanceStatsUsers();
                attendancestatsUsers.setUserCount(rs.getInt("countOfUsers"));
                return attendancestatsUsers;
            }
       }); 
    }catch (Exception e) {
        log4jLog.info(" selectAttendanceStatsUsers " + e);
        e.printStackTrace();
       return new ArrayList<AttendanceStatsUsers>();
    } 
}
    
        
    @Override
     public List<AppointmentStats> selectAppointmentStats(int accountId,int teamId){
        StringBuilder query = new StringBuilder();
        query.append("SELECT cust.customer_name AS CompanyName,user.full_name AS UserName,cust.address1 ");
        query.append("location,appo.status,appo.record_state,appo.check_in_time,appo.check_out_time ,appo.appointment_time,appo.appointment_end_time,user1.full_name AS ");
        query.append("SubordinateName, CASE WHEN  appo.status=0 THEN appo.appointment_time  WHEN  appo.status=1 THEN appo.check_in_time  WHEN  appo.status=2 THEN ");
        query.append("appo.check_out_time END time FROM appointments appo  INNER JOIN customers cust ON cust.id=appo.customer_id_fk  INNER JOIN fieldsense.users user ON ");
        query.append("user.Id=appo.user_id_fk  INNER JOIN fieldsense.users user1 ON user1.Id=appo.assigned_id_fk  where appointment_time between concat(curdate(),' 00:00:00') ");
        query.append("and concat(curdate(),' 23:59:59')  and appo.record_state!=3 and user.id in(select user_id_fk from teams where team_position_csv like ?)");
//        System.out.println("query="+query);
        log4jLog.info(" selectAppointmentStats " + query);
        Object param[] = new Object[]{"%" + teamId + "%"};
        try {
          return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<AppointmentStats>() {
    
                @Override
                public AppointmentStats mapRow(ResultSet rs, int i) throws SQLException {

                    AppointmentStats appointmentstats = new AppointmentStats();
                    appointmentstats.setSubordinateName(rs.getString("SubordinateName"));
                    
                    Customer customer=new Customer();
                    customer.setCustomerName(rs.getString("CompanyName"));
                    customer.setCustomerAddress1(rs.getString("location"));
                    appointmentstats.setCustomer(customer);
                    
                    Appointment appointment=new Appointment();
                    appointment.setStatus(rs.getInt("appo.status"));
                    appointment.setDateTime(rs.getTimestamp("appo.appointment_time"));
                    appointment.setSdateTime(appointment.getDateTime().toString());
                    appointment.setEndTime(rs.getTimestamp("appo.appointment_end_time"));
                    appointment.setSendTime(appointment.getEndTime().toString());
                    appointment.setRecordState(rs.getInt("appo.record_state"));
                    
                    appointmentstats.setTime(rs.getString("time"));
                    appointmentstats.setAppointment(appointment);
                    
                    User user=new User();
                    user.setFullname(rs.getString("UserName"));
                    appointmentstats.setUser(user);
                    
                    return appointmentstats;
                }
           }); 
        }catch (Exception e) {
            log4jLog.info(" selectAppointmentStats " + e);
            e.printStackTrace();
           return new ArrayList<AppointmentStats>();
        } 
}

    @Override
    public List<CustomFormStats> selectFormFilledStats(int accountId, int teamId) {
            StringBuilder query = new StringBuilder();
            query.append("select count(*) AS countOfFormsFilled from fieldsense.users u ");
            query.append("inner join account_"+accountId+".form_fill_details_table form "); // modified by jyoti, accountid
            query.append("on form.user_id=u.id  where u.id in (select distinct user_id_fk from account_"+accountId+".teams where team_position_csv like ?) "); // modified by jyoti, accountid
            query.append("and form.submitted_on between concat(curdate(),' 00:00:00') and concat(curdate(),' 23:59:59') group by u.email_address ");

//            System.out.println("query="+query);
            log4jLog.info(" selectFormFilledStats " + query);
            Object param[] = new Object[]{"%" + teamId + "%"};
            try {
              return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<CustomFormStats>() {

                    @Override
                    public CustomFormStats mapRow(ResultSet rs, int i) throws SQLException {

                        CustomFormStats customformstats = new CustomFormStats();
                        customformstats.setCountOfFormFilled(rs.getInt("countOfFormsFilled"));  
                        return customformstats;
                    }
               }); 
            }catch (Exception e) {
                log4jLog.info(" selectFormFilledStats " + e);
                e.printStackTrace();
               return new ArrayList<CustomFormStats>();
            } 
    }
     
}
