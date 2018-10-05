/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.mobile.dao;

import com.qlc.fieldsense.mobile.model.LeftSliderMenu;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Ramesh
 * @date 06-06-2014
 */
public class MobileDapImpl implements MobileDao {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("mobileDapImpl");
    JdbcTemplate jdbcTemplate;

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
     * @param fromDate
     * @param toDate
     * @param userId
     * @param accountId
     * @return
     */
    @Override
    public LeftSliderMenu getLeftSliderMenuData(String fromDate, String toDate, int userId, int accountId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT(");
        query.append("SELECT COUNT(id) FROM appointments WHERE (appointment_time BETWEEN ? AND ?) AND (assigned_id_fk= ?) AND record_state!=3");
        query.append(") AS today_appointments,");
        query.append("(SELECT COUNT(id) FROM messages WHERE is_read=0 AND user_id_fk != ? AND message_connection_id_fk IN (SELECT DISTINCT connection_id_fk FROM message_connections WHERE user_id_fk_1=?)");
        query.append(") AS unread_message,");
        query.append("(IFNULL((SELECT punch_In FROM attendances WHERE punch_date=? AND user_id_fk=? ),'')");
        query.append(") as punch_in_time,");
        query.append("(IFNULL((SELECT punch_out FROM attendances WHERE punch_date=? AND user_id_fk=?),'')");
        query.append(") as punch_out_time");
        String date=toDate.split(" ")[0];
        log4jLog.info("getLeftSliderMenuData " + query);
        Object param[] = new Object[]{fromDate, toDate, userId, userId, userId,date, userId,date, userId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query.toString(), param, new RowMapper<LeftSliderMenu>() {

                @Override
                public LeftSliderMenu mapRow(ResultSet rs, int i) throws SQLException {
                    LeftSliderMenu leftSliderMenu = new LeftSliderMenu();
                    leftSliderMenu.setTodayAppointments(rs.getInt("today_appointments"));
                    leftSliderMenu.setUnreadMessagesCount(rs.getInt("unread_message"));
                    leftSliderMenu.setPunchInTime(rs.getString("punch_in_time"));
                    leftSliderMenu.setPunchOutTime(rs.getString("punch_out_time"));
                    return leftSliderMenu;
                }
            });
        } catch (Exception e) {
            log4jLog.info("getLeftSliderMenuData " + e);
            return new LeftSliderMenu();
        }
    }

    /**
     *
     * @param fromDate
     * @param toDate
     * @param versionCode
     * @param userId
     * @param accountId
     * @return
     */
    @Override
    public LeftSliderMenu getLeftSliderMenuData(String fromDate, String toDate,int versionCode, int userId, int accountId) {
        StringBuilder query = new StringBuilder();
        
        // now we will simply fetch last punch record of that user for ovenight punch functionality
       // query.append("SELECT IFNULL(id,0) as attendanceId, IFNULL(punch_in,'') as punch_in_time,IFNULL(punch_out,'') as punch_out_time,IFNULL(punch_date,'') as punchDate ,");
        query.append(" SELECT IFNULL(id,0) as attendanceId, IFNULL(punch_in,'') as punch_in_time,IFNULL(punch_out,'') as punch_out_time,IFNULL(punch_date,'') as punchDate,");
        query.append("(SELECT COUNT(id) FROM appointments WHERE (appointment_time BETWEEN ? AND ?) AND (assigned_id_fk= ?) AND record_state!=3");
        query.append(") AS today_appointments,");
        query.append("(SELECT COUNT(id) FROM messages WHERE is_read=0 AND user_id_fk != ? AND message_connection_id_fk IN (SELECT DISTINCT connection_id_fk FROM message_connections WHERE user_id_fk_1=?)");
        query.append(") AS unread_message,IFNULL((select a.company_name from fieldsense.accounts a left join fieldsense.users u on a.id=u.account_id_fk where u.id=?),'') as company_name ");
        query.append("from attendances where user_id_fk= ? order by id desc limit 1");
                     
        //final String date=toDate.split(" ")[0];
        log4jLog.info("getLeftSliderMenuData " + query);
        //Object param[] = new Object[]{fromDate, toDate, userId, userId, userId,date, userId,date, userId};
        Object param[] = new Object[]{fromDate, toDate, userId, userId, userId,userId,userId};

        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query.toString(), param, new RowMapper<LeftSliderMenu>() {

                @Override
                public LeftSliderMenu mapRow(ResultSet rs, int i) throws SQLException {
                    LeftSliderMenu leftSliderMenu = new LeftSliderMenu();
                    leftSliderMenu.setAttendanceId(rs.getInt("attendanceId"));
                    leftSliderMenu.setTodayAppointments(rs.getInt("today_appointments"));
                    leftSliderMenu.setUnreadMessagesCount(rs.getInt("unread_message"));
                    leftSliderMenu.setPunchInTime(rs.getString("punch_in_time"));
                    leftSliderMenu.setPunchOutTime(rs.getString("punch_out_time"));
                    leftSliderMenu.setPunchDate(rs.getString("punchDate"));
                    leftSliderMenu.setAccountName(rs.getString("company_name"));
                    return leftSliderMenu;
                }
            });
        } catch (Exception e) {
            log4jLog.info("getLeftSliderMenuData " + e);
            return new LeftSliderMenu();
        }
    }
    
    /**
     *
     * @param appType
     * @return
     */
    @Override
    public HashMap selectAppVersionDetails(int appType) {
        String query = "SELECT app_version,version_code FROM app_settings WHERE app_type=?";
        Object[] param = new Object[]{appType};
        try {
            return jdbcTemplate.queryForObject(query,param,new RowMapper<HashMap>(){
              @Override
                public HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    HashMap map=new HashMap();
                    map.put("versionName",rs.getString("app_version"));
                    map.put("versionCode",rs.getInt("version_code"));
                    return map;
                }
            });
        } catch (Exception e) {
//            e.printStackTrace();
            return new HashMap();
        }
    }

    /**
     *
     * @param appType
     * @return
     */
    @Override
    public String selectAppStoreURL(int appType) {
        String query = "SELECT app_store_url FROM app_settings WHERE app_type=?";
        Object[] param = new Object[]{appType};
        try {
            return jdbcTemplate.queryForObject(query, param, String.class);
        } catch (Exception e) {
//            e.printStackTrace();
            return "";
        }
    }
    
    /**
     *
     * @param userRole
     * @param userId
     * @param accountId
     * @return
     */
    public Timestamp convertStringToTimeStamp(String timeStamp){
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //sdf.setTimeZone(TimeZone.getTimeZone("GMT-05:30"));
               Date date = new Date();
               java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        try {
            date = sdf.parse(timeStamp);
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(MobileDapImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
               sqlDate = new java.sql.Date(date.getTime());
               java.sql.Timestamp timestamp = new java.sql.Timestamp(sqlDate.getTime());
           return timestamp;
    }
    
    @Override
    public Timestamp lastUpdatedOfTerritories(int userRole,int userId,int accountId){
        
        //String query = "select max(updated_on) updated_on from  territory_categories";
        String query;
        Object[] param;
        Map<String,Object> maxUpdatedOn=new HashMap<String,Object>();
        
        try {
//            if(userRole==1){
//                return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, Timestamp.class);
//            }else{
                query = "select max(u.created_on) as created_on,max(t.updated_on) as updated_on,count(u.id) as id_count from user_territory u inner join territory_categories t  on t.id=u.teritory_id and u.user_id_fk=?";
                param=new Object[]{userId};
             //   FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param,Timestamp.class);
                 //System.err.println("wbnuhcwkhuownfuowuiofhwifniowfhniofjwi"+maxUpdatedOn);
                maxUpdatedOn=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForMap(query,param);
               
                Timestamp createdOn=this.convertStringToTimeStamp(maxUpdatedOn.get("created_on").toString());
                Timestamp updateOn=this.convertStringToTimeStamp(maxUpdatedOn.get("updated_on").toString());
                int terriortyCount=Integer.parseInt(maxUpdatedOn.get("id_count").toString());
                if(createdOn.before(updateOn)){
                  return updateOn;
                }else{
                  return createdOn;
                }
        } catch (Exception e) {
        log4jLog.info("lastUpdatedOfTerritories userId="+userId +"Exception"+ e);
            return new Timestamp(0);
        }
    }

    public Map lastUpdatedOfTerritoriesWithCount(int userRole, int userId, int accountId) {
       String query;
        Object[] param;
        Map<String,Object> maxUpdatedOn=new HashMap<String,Object>();        
        try {
            query = "select max(u.created_on) as created_on,max(t.updated_on) as updated_on, count(u.id) as id_count from user_territory u inner join territory_categories t  on t.id=u.teritory_id and u.user_id_fk=?";
            param=new Object[]{userId};
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForMap(query,param);
        }catch(Exception e){
           log4jLog.info("lastUpdatedOfTerritoriesWithCount userId="+userId +"Exception"+ e);
            return maxUpdatedOn;
        }
        
        
    }

    /**
     * @param currencySymbol
     * @param userID
     * @return 
     * @Added by Jyoti 23-08-2017
     */
    @Override
    public boolean updateUserPreferences(String currencySymbol, int userID){
        String query = "UPDATE users SET currency_symbol = ? WHERE id = ? ";
        Object param[] = new Object[]{currencySymbol, userID};
        try{
            if(jdbcTemplate.update(query, param) == 1){                
                return true;
            } else{
                return false;
            }
        } catch(Exception e){
            e.printStackTrace();
            log4jLog.info("updateUserPreferences"+ e);
            return false;
        }
        
    }
    
//Siddhesh :- requirment changed.   
//    //Added by siddhesh for my team mobile subordinate.
//    public int getSubordinates(int userId,int accountId) {
//        String query;
//        Object[] param;
//        String userPosition="0";
//        int countSubordnates=0;
//        try{
//           
////            query = "select team_position_csv from teams where user_id_fk=?";
////            param=new Object[]{userId};
////           userPosition= FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class); 
////           
////           query="select count(id) from teams where team_position_csv like '%"+userPosition +"%'";
////           countSubordnates= FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, Integer.class);
//            
//            query="select count(id) from teams where team_position_csv like CONCAT('%',(select id from teams where user_id_fk=?),'%')";
//            param=new Object[]{userId};
//            countSubordnates= FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query,param,Integer.class);
//            
//           if(countSubordnates>1){
//           
//           return 1;
//           }else{
//           
//           return 0;
//           }
//           }catch(Exception e){
//        e.printStackTrace();
//                }
//        
//        
//        
//        return 0;
//    }
            
    /**
     * @Added by jyoti, 2017-dec-17
     * @param fromDate
     * @param toDate
     * @param userId
     * @param accountId
     * @return 
     */
    @Override
    public LeftSliderMenu getLeftSliderMenuV2(String fromDate, String toDate, int userId, int accountId) {
        StringBuilder query = new StringBuilder();
        query.append(" SELECT IFNULL(id,0) as attendanceId, IFNULL(punch_in,'') as punch_in_time,IFNULL(punch_out,'') as punch_out_time,IFNULL(punch_date,'') as punchDate,");
        query.append("(SELECT COUNT(id) FROM appointments WHERE (appointment_time BETWEEN ? AND ?) AND (assigned_id_fk= ?) AND record_state!=3");
        query.append(") AS today_appointments,");
        query.append("(SELECT COUNT(id) FROM messages WHERE is_read=0 AND user_id_fk != ? AND message_connection_id_fk IN (SELECT DISTINCT connection_id_fk FROM message_connections WHERE user_id_fk_1=?)");
        query.append(") AS unread_message ");
        query.append("FROM attendances WHERE user_id_fk = ? ORDER BY id DESC LIMIT 1");
        
        log4jLog.info("getLeftSliderMenuV2 for userId : " + userId);
        Object param[] = new Object[]{fromDate, toDate, userId, userId, userId, userId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query.toString(), param, new RowMapper<LeftSliderMenu>() {

                @Override
                public LeftSliderMenu mapRow(ResultSet rs, int i) throws SQLException {
                    LeftSliderMenu leftSliderMenu = new LeftSliderMenu();
                    leftSliderMenu.setAttendanceId(rs.getInt("attendanceId"));
                    leftSliderMenu.setTodayAppointments(rs.getInt("today_appointments"));
                    leftSliderMenu.setUnreadMessagesCount(rs.getInt("unread_message"));
                    leftSliderMenu.setPunchInTime(rs.getString("punch_in_time"));
                    leftSliderMenu.setPunchOutTime(rs.getString("punch_out_time"));
                    leftSliderMenu.setPunchDate(rs.getString("punchDate"));
                    return leftSliderMenu;
                }
            });
        } catch (Exception e) {
            log4jLog.info("getLeftSliderMenuV2 for userId : " + userId+ ", Excepton : "+e);
            e.printStackTrace();
            return new LeftSliderMenu();
        }
    }
    
}
