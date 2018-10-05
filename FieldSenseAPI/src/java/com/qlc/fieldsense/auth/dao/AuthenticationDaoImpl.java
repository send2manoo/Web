/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.auth.dao;

import com.qlc.fieldsense.auth.model.AuthenticationUser;
import static com.qlc.fieldsense.auth.model.AuthenticationUserManager.log4jLog;
import com.qlc.fieldsense.mobile.model.LeftSliderMenu;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Ramesh
 * @date 30-01-2014
 * @purpose This Class contains all the methods to perform database activities
 * for authentication .
 */
public class AuthenticationDaoImpl implements AuthenticationUserDao {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("AuthenticationDaoImpl");
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
     * @param user
     * @return
     */
    @Override
    public String selectUserToken(AuthenticationUser user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @param token
     * @return
     */
    @Override
    public boolean updateTokenTime(String token) {
        String query = "UPDATE user_auth set modifiedOn=now() WHERE user_token=?";
        log4jLog.info(" updateTokenTime " + query);
        Object param[] = new Object[]{token};
        try {
            if (jdbcTemplate.update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateTokenTime " + query);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param token
     * @return
     */
    @Override
    public boolean deleteToken(String token) {
        String query = "DELETE FROM user_auth WHERE user_token= ?";
        log4jLog.info(" deleteToken " + query);
        Object param[] = new Object[]{token};
        try {
            if (jdbcTemplate.update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" deleteToken " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param user
     * @return
     */
    @Override
    public boolean insertAuthDetails(AuthenticationUser user) {
        String query = "INSERT INTO user_auth (user_id_fk,account_id_fk,user_token,user_first_name,user_last_name,user_email_address,createdOn,modifiedOn) VALUES (?,?,?, ?,?, ?, now(),now())";
        log4jLog.info(" insertAuthDetails " + query);
        Object param[] = new Object[]{user.getUserId(), user.getAccountId(), user.getUserToken(), user.getUserFirstName(), user.getUserLastName(), user.getUserEmailAddress()};
        try {
            if (jdbcTemplate.update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" insertAuthDetails " + e);
//            e.printStackTrace();
            return false;
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
     * @param mobileno
     * @return
     */
    @Override
    public String getUserPasswordMobileNo(String mobileno) {
        String query = "SELECT password FROM users WHERE mobile_number=?";
        log4jLog.info(" getUserPassword " + query);
        try {
            Object param[] = new Object[]{mobileno};
            return jdbcTemplate.queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" getUserPassword " + e);
//            e.printStackTrace();
            return null;
        }
    }
    
    /**
     *
     * @param token
     * @return
     */
//    @Override
//    public AuthenticationUser selectUserAuthDetails(String token) {
//        StringBuilder query = new StringBuilder();
//        query.append("SELECT ua.id,ua.user_id_fk,ua.account_id_fk,ua.user_token,u.first_name,u.last_name,");
//        query.append(" u.email_address,ua.createdOn,ua.modifiedOn,u.role,u.last_known_latitude,u.last_known_langitude,u.designation,a.company_name FROM user_auth ua ");
//        query.append(" INNER JOIN users u ON ua.user_id_fk=u.id");
//        query.append(" INNER JOIN accounts a ON u.account_id_fk=a.id");
//        query.append(" WHERE user_token=?");
////        String query = "SELECT id,user_id_fk,account_id_fk,user_token,user_first_name,user_last_name,user_email_address,createdOn,modifiedOn FROM user_auth WHERE user_token=?";
//        log4jLog.info(" selectUserAuthDetails " + query);
//        Object param[] = new Object[]{token};
//        try {
//            return jdbcTemplate.queryForObject(query.toString(), param, new RowMapper<AuthenticationUser>() {
//
//                @Override
//                public AuthenticationUser mapRow(ResultSet rs, int i) throws SQLException {
//                    AuthenticationUser authenticationUser = new AuthenticationUser();
//                    authenticationUser.setId(rs.getInt("ua.id"));
//                    authenticationUser.setUserId(rs.getInt("ua.user_id_fk"));
//                    authenticationUser.setAccountId(rs.getInt("ua.account_id_fk"));
//                    authenticationUser.setUserToken(rs.getString("user_token"));
//                    authenticationUser.setUserFirstName(rs.getString("u.first_name"));
//                    authenticationUser.setUserLastName(rs.getString("u.last_name"));
//                    authenticationUser.setUserEmailAddress(rs.getString("u.email_address"));
//                    authenticationUser.setCreatedOn(rs.getTimestamp("ua.createdOn"));
//                    authenticationUser.setModifiedOn(rs.getTimestamp("ua.modifiedOn"));
//                    authenticationUser.setRole(rs.getInt("u.role"));
//                    authenticationUser.setLatitude(rs.getDouble("last_known_latitude"));
//                    authenticationUser.setLangitude(rs.getDouble("last_known_langitude"));
//                    authenticationUser.setUserDesignation(rs.getString("designation"));
//                    authenticationUser.setCompanyName(rs.getString("a.company_name"));
//                    return authenticationUser;
//                }
//            });
//        } catch (Exception e) {
//            log4jLog.info(" selectUserAuthDetails " + e);
////            e.printStackTrace();
//            return new AuthenticationUser();
//        }
//    }
    
     @Override
    public AuthenticationUser selectUserAuthDetails(String token) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT ua.id,ua.user_id_fk,ua.account_id_fk,ua.user_token,u.first_name,u.last_name,");
        query.append(" u.email_address,ua.createdOn,ua.modifiedOn,u.role,u.last_known_latitude,u.last_known_langitude,u.designation,a.company_name FROM user_auth ua ");
        query.append(" INNER JOIN users u ON ua.user_id_fk=u.id");
        query.append(" INNER JOIN accounts a ON u.account_id_fk=a.id");
        query.append(" WHERE user_token=?");
//        String query = "SELECT id,user_id_fk,account_id_fk,user_token,user_first_name,user_last_name,user_email_address,createdOn,modifiedOn FROM user_auth WHERE user_token=?";
        log4jLog.info(" selectUserAuthDetails " + query);
        Object param[] = new Object[]{token};
        try {
            return jdbcTemplate.queryForObject(query.toString(), param, new RowMapper<AuthenticationUser>() {

                @Override
                public AuthenticationUser mapRow(ResultSet rs, int i) throws SQLException {
                    AuthenticationUser authenticationUser = new AuthenticationUser();
                    authenticationUser.setId(rs.getInt("ua.id"));
                    authenticationUser.setUserId(rs.getInt("ua.user_id_fk"));
                    authenticationUser.setAccountId(rs.getInt("ua.account_id_fk"));
                    authenticationUser.setUserToken(rs.getString("user_token"));
                    authenticationUser.setUserFirstName(rs.getString("u.first_name"));
                    authenticationUser.setUserLastName(rs.getString("u.last_name"));
                    authenticationUser.setUserEmailAddress(rs.getString("u.email_address"));
                    authenticationUser.setCreatedOn(rs.getTimestamp("ua.createdOn"));
                    authenticationUser.setModifiedOn(rs.getTimestamp("ua.modifiedOn"));
                    authenticationUser.setRole(rs.getInt("u.role"));
                    authenticationUser.setLatitude(rs.getDouble("last_known_latitude"));
                    authenticationUser.setLangitude(rs.getDouble("last_known_langitude"));
                    authenticationUser.setUserDesignation(rs.getString("designation"));
                    authenticationUser.setCompanyName(rs.getString("a.company_name"));
                    return authenticationUser;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectUserAuthDetails " + e);
//            e.printStackTrace();
            return new AuthenticationUser();
        }
    }


    /**
     *
     * @param gcmId
     * @param deviceOS
     * @param userId
     * @return
     */
    @Override
    public boolean updateGcmIdOfUser(String gcmId,int deviceOS, int userId) {
        String query = "UPDATE users SET gcm_id=? , device_os=? WHERE id=?";
        log4jLog.info(" updateGcmIdOfUser " + query);
        Object param[] = new Object[]{gcmId,deviceOS, userId};
        try {
            if (jdbcTemplate.update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
//            e.printStackTrace();
            log4jLog.info(" updateGcmIdOfUser " + e);
            return false;
        }
    }

    /**
     *
     * @param emailAddress
     * @return
     */
    @Override
    public AuthenticationUser selectOfficeLatLong(String emailAddress) {
        String query = "SELECT  IFNULL(office_latitude,0) office_latitude,IFNULL(office_langitude,0) office_langitude FROM users WHERE email_address = ?";
        log4jLog.info(" selectOfficeLatLong " + query);
        Object param[] = new Object[]{emailAddress};
        try {
            return jdbcTemplate.queryForObject(query, param, new RowMapper<AuthenticationUser>() {

                @Override
                public AuthenticationUser mapRow(ResultSet rs, int i) throws SQLException {
                    AuthenticationUser authenticationUser = new AuthenticationUser();
                    authenticationUser.setOfficelatitude(rs.getDouble("office_latitude"));
                    authenticationUser.setOfficelangitude(rs.getDouble("office_langitude"));
                    return authenticationUser;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectOfficeLatLong " + e);
            return new AuthenticationUser();
        }
    }
    
    /**
     *
     * @param mobileno
     * @return
     */
    @Override
    public AuthenticationUser selectOfficeLatLongMobileNo(String mobileno) {
        String query = "SELECT  IFNULL(office_latitude,0) office_latitude,IFNULL(office_langitude,0) office_langitude FROM users WHERE mobile_number = ?";
        log4jLog.info(" selectOfficeLatLong " + query);
        Object param[] = new Object[]{mobileno};
        try {
            return jdbcTemplate.queryForObject(query, param, new RowMapper<AuthenticationUser>() {

                @Override
                public AuthenticationUser mapRow(ResultSet rs, int i) throws SQLException {
                    AuthenticationUser authenticationUser = new AuthenticationUser();
                    authenticationUser.setOfficelatitude(rs.getDouble("office_latitude"));
                    authenticationUser.setOfficelangitude(rs.getDouble("office_langitude"));
                    return authenticationUser;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectOfficeLatLong " + e);
            return new AuthenticationUser();
        }
    }
    
    /**
     *
     * @param firstlogin
     * @param userId
     * @return
     */
    @Override
    public boolean updateUserFirstloginAndLastLoggedOn(int firstlogin, int userId) {
        String query = "UPDATE users SET isfirstlogin=?,last_logged_on=now() WHERE id=?";
        Object[] param = new Object[]{firstlogin, userId};
        try {
            if (jdbcTemplate.update(query, param) == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param emailAddress
     * @return
     */
    @Override
    public int selectUserFirstLogin(String emailAddress) {
        String query = "SELECT isfirstlogin FROM users WHERE email_address=?";
        Object[] param = new Object[]{emailAddress};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     *
     * @param mobileno
     * @return
     */
    @Override
    public int selectUserFirstLoginMobileNo(String mobileno) {
        String query = "SELECT isfirstlogin FROM users WHERE mobile_number=?";
        Object[] param = new Object[]{mobileno};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            return -1;
        }
    }
    
    /**
     *
     * @param userId
     * @return
     */
    public boolean updateLastLoggedOn(int userId) {
        String query = "UPDATE users SET last_logged_on=now() WHERE id=?";
        Object[] param = new Object[]{userId};
        try {
            if (jdbcTemplate.update(query, param) == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param userId
     * @return
     */
    public String getUsrPassword(int userId) {
        String query = "SELECT password FROM users WHERE id=?";
        log4jLog.info(" getUserPassword " + query);
        try {
            Object param[] = new Object[]{userId};
            return jdbcTemplate.queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" getUserPassword " + e);
//            e.printStackTrace();
            return null;
        }
        
    }
    
    /**
     * @Added by jyoti, 16-dec-2017
     * @param userId
     * @return
     * @purpose select all user data
     */
    @Override
    public AuthenticationUser selectAuthUserAllData(int userId) {
        String query = " SELECT id, account_id_fk, first_name, last_name, email_address, password, mobile_number, gender, user_accuracy, check_in_radius, allow_timeout, allow_offline, role, designation, isfirstlogin, office_latitude, office_langitude, location_interval, app_version, home_latitude, home_langitude, is_terms_condition_agreed, is_newsletter_opt_in, IFNULL(newsletter_agreed_on, '1111:11:11 00:00:00') AS newsletter_agreed_on, IFNULL(terms_condition_agreed_on,'1111:11:11 00:00:00') AS terms_condition_agreed_on FROM users WHERE id = ? ";
        log4jLog.info(" selectAuthUserAllData for userId : " + userId);
        Object param[] = new Object[]{userId};
        try {
            return this.jdbcTemplate.queryForObject(query, param, new RowMapper<AuthenticationUser>() {
                @Override
                public AuthenticationUser mapRow(ResultSet rs, int i) throws SQLException {
                    AuthenticationUser authUser = new AuthenticationUser();
                    authUser.setUserId(rs.getInt("id"));
                    authUser.setAccountId(rs.getInt("account_id_fk"));
                    authUser.setUserFirstName(rs.getString("first_name"));
                    authUser.setUserLastName(rs.getString("last_name"));
                    authUser.setUserEmailAddress(rs.getString("email_address"));
                    authUser.setUserPassword(rs.getString("password"));
                    authUser.setMobileNo(rs.getString("mobile_number"));
                    authUser.setGender(rs.getInt("gender"));
                    authUser.setUserAccuracy(rs.getInt("user_accuracy"));
                    authUser.setCheckInRadius(rs.getInt("check_in_radius"));
                    authUser.setAllowTimoutUser(rs.getInt("allow_timeout"));
                    authUser.setAllowOfflineUser(rs.getInt("allow_offline"));
//                    authUser.setCurrencySymbol(rs.getString("currency_symbol"));                    
                    authUser.setRole(rs.getInt("role"));                    
                    authUser.setUserDesignation(rs.getString("designation"));
                    if(rs.getInt("isfirstlogin") == 0){
                        authUser.setIsFirstLogin(true);
                    }
                    authUser.setOfficelatitude(rs.getDouble("office_latitude"));
                    authUser.setOfficelangitude(rs.getDouble("office_langitude"));
                    authUser.setLocationIntervalUser(rs.getInt("location_interval"));
                    authUser.setAppVersion(rs.getInt("app_version"));
                    authUser.setHomLatitude(rs.getDouble("home_latitude")); // added by jyoti
                    authUser.setHomeLongitude(rs.getDouble("home_langitude")); // added by jyoti
                    authUser.setIs_terms_condition_agreed(rs.getInt("is_terms_condition_agreed")); // added by jyoti
                    authUser.setIs_newsletter_opt_in(rs.getInt("is_newsletter_opt_in")); // added by jyoti
                    authUser.setTerms_condition_agreed_on(rs.getTimestamp("terms_condition_agreed_on")); // added by jyoti
                    authUser.setNewsletter_agreed_on(rs.getTimestamp("newsletter_agreed_on")); // added by jyoti
                    return authUser;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAuthUserAllData for userId : " + userId + ", Exception : "+e);
            e.printStackTrace();
            return new AuthenticationUser();
        }
    }
    
    /**
     * @Added by jyoti, 2017-dec-18
     * @param userRole
     * @param userId
     * @param accountId
     * @return 
     */
    public java.util.Map lastUpdatedOfTerritoriesWithCount(int userRole, int userId, int accountId) {
        String query = "SELECT MAX(u.created_on) AS created_on, MAX(t.updated_on) AS updated_on, COUNT(u.id) AS id_count FROM user_territory u INNER JOIN territory_categories t ON t.id = u.teritory_id AND u.user_id_fk = ? ";
        Object[] param = new Object[]{userId};
        try {
            log4jLog.info("lastUpdatedOfTerritoriesWithCount for userId : "+userId);
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForMap(query,param);
        }catch(Exception e){
            e.printStackTrace();
            log4jLog.info("lastUpdatedOfTerritoriesWithCount userId : "+userId +" Exception : "+ e);
            return new java.util.HashMap();
        }
    }  
    
    /**
     * @Added by Jyoti, 08-01-2018
     * @param userId
     * @param accountId
     * @return 
     */
    public java.util.List<java.util.HashMap> assignedTerritoryList(int userId, int accountId) {
//        String query=" SELECT t.id, t.category_name, t.is_active  FROM territory_categories t, user_territory u WHERE u.teritory_id = t.id AND user_id_fk = ? ";
        String query = "SELECT t.id, t.category_name, t.is_active, t.created_on, t.parent_category, t.category_position_csv AS csv, t.updated_on, (SELECT COUNT(id) FROM territory_categories WHERE category_position_csv LIKE CONCAT('%',',',csv) AND is_active = 1) AS childcount FROM territory_categories t, user_territory t1 WHERE t1.teritory_id = t.id AND user_id_fk = ? AND t.is_active = 1 GROUP BY t.id ORDER BY t.category_name";
        Object[] param = new Object[]{userId};
        log4jLog.info("assignedTerritoryList for userId :  " + userId);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<java.util.HashMap>() {

                @Override
                public java.util.HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    java.util.HashMap hMap = new java.util.HashMap();
                    hMap.put("id", rs.getInt("t.id"));
                    hMap.put("categoryName",rs.getString("t.category_name"));
//                    hMap.put("isActive",rs.getBoolean("t.is_active"));
                    hMap.put("hasChild", rs.getInt("childcount"));
//                    hMap.put("categoryPositionCsv",rs.getString("csv"));
                    hMap.put("parentCategory", rs.getInt("t.parent_category"));
                    hMap.put("updatedOn", rs.getTimestamp("t.updated_on"));
                    return hMap;
                }
            });
        } catch (Exception e) {
            log4jLog.info("assignedTerritoryList " + e);
            e.printStackTrace();
            return new java.util.ArrayList<java.util.HashMap>();
        }
    }
    
    /**
     * @Added by Jyoti, 08-01-2018
     * @param accountId
     * @return 
     */
    public java.util.List<java.util.HashMap> expenseCategoryList(int accountId) {
        String query=" SELECT id, category_name, is_active FROM expense_categories WHERE is_active = 1 order by category_name ";
        log4jLog.info("expenseCategoryList for accountId :  " + accountId);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, new RowMapper<java.util.HashMap>() {

                @Override
                public java.util.HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    java.util.HashMap hMap = new java.util.HashMap();
                    hMap.put("id", rs.getInt("id"));
                    hMap.put("categoryName",rs.getString("category_name"));
                    hMap.put("isActive",rs.getBoolean("is_active"));
                    return hMap;
                }
            });
        } catch (Exception e) {
            log4jLog.info("expenseCategoryList " + e);
            e.printStackTrace();
            return new java.util.ArrayList<java.util.HashMap>();
        }
    }
    
    /**
     * @Added by Jyoti, 08-01-2018
     * @param accountId
     * @return 
     */
    public java.util.List<java.util.HashMap> purposeCategoryList(int accountId) {
        String query=" SELECT id, purpose, is_active FROM activity_purpose WHERE is_active = 1 order by purpose ";
        log4jLog.info("purposeCategoryList for accountId :  " + accountId);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, new RowMapper<java.util.HashMap>() {

                @Override
                public java.util.HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    java.util.HashMap hMap = new java.util.HashMap();
                    hMap.put("id", rs.getInt("id"));
                    hMap.put("purpose",rs.getString("purpose"));
                    hMap.put("isActive",rs.getBoolean("is_active"));
                    return hMap;
                }
            });
        } catch (Exception e) {
            log4jLog.info("purposeCategoryList " + e);
            e.printStackTrace();
            return new java.util.ArrayList<java.util.HashMap>();
        }
    }
    
    /**
     * @Added by Jyoti, 08-01-2018
     * @param accountId
     * @return 
     */
    public java.util.List<java.util.HashMap> industryCategoryList(int accountId) {
        String query=" SELECT id,category_name,is_active FROM industry_categories WHERE is_active=1 order by category_name ";
        log4jLog.info("purposeCategoryList for accountId :  " + accountId);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, new RowMapper<java.util.HashMap>() {

                @Override
                public java.util.HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    java.util.HashMap hMap = new java.util.HashMap();
                    hMap.put("id", rs.getInt("id"));
                    hMap.put("categoryName",rs.getString("category_name"));
                    hMap.put("isActive",rs.getBoolean("is_active"));
                    return hMap;
                }
            });
        } catch (Exception e) {
            log4jLog.info("purposeCategoryList " + e);
            e.printStackTrace();
            return new java.util.ArrayList<java.util.HashMap>();
        }
    }
    

    public HashMap getLeftSliderMenuForOffline(String fromDate, String toDate, int userId, int accountId) {
        StringBuilder query=new StringBuilder();
//        Map mapOfData=new HashMap();
        // Commented by jyoti
//        query.append(" SELECT IFNULL(id,0) as attendanceId, IFNULL(punch_in,'') as punchInTime,IFNULL(punch_out,'') as punchOutTime,IFNULL(punch_date,'') as punchDate ");       
//        query.append("FROM attendances WHERE user_id_fk = ? ORDER BY id DESC LIMIT 1");
        
        // Added by jyoti
        query.append(" SELECT IFNULL(id,0) as attendanceId, IFNULL(punch_in,'') as punch_in_time,IFNULL(punch_out,'') as punch_out_time,IFNULL(punch_date,'') as punchDate,");
        query.append("attendance_status, ");  // added by jyoti, 24-jul-2018
        query.append("(SELECT COUNT(id) FROM appointments WHERE (appointment_time BETWEEN ? AND ?) AND (assigned_id_fk= ?) AND record_state!=3");
        query.append(") AS today_appointments,");
        query.append("(SELECT COUNT(id) FROM messages WHERE is_read=0 AND user_id_fk != ? AND message_connection_id_fk IN (SELECT DISTINCT connection_id_fk FROM message_connections WHERE user_id_fk_1=?)");
        query.append(") AS unread_message ");
        query.append("FROM attendances WHERE user_id_fk = ? ORDER BY id DESC LIMIT 1");
        // Ended by jyoti
//        Object[] param = new Object[] {userId};
//        mapOfData= FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryFor
//        return mapOfData;
        try{
//        Object[] param = new Object[] {userId}; // commented by jyoti
            Object param[] = new Object[]{fromDate, toDate, userId, userId, userId, userId}; // added by jyoti
                return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query.toString(), param, new RowMapper<HashMap>() {
                    public HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    HashMap<String,String> data=new HashMap<String,String>();
                    data.put("attendanceId",Integer.toString(rs.getInt("attendanceId")));
                    // added by jyoti, 24-jul-2018
                    if(rs.getString("punch_out_time").equals("00:00:00")){
                        data.put("punchOutTime","");
                    } else{
                        data.put("punchOutTime",rs.getString("punch_out_time"));
                    }
                    data.put("attendanceStatus",rs.getString("attendance_status"));
                    // ended by jyoti, 24-jul-2018
                    data.put("punchInTime",rs.getString("punch_in_time"));                    
                    data.put("punchDate",rs.getString("punchDate"));
                    data.put("visitCount",rs.getString("today_appointments"));
                    data.put("messageCount",rs.getString("unread_message"));
                    return data;
                }
            });
        } catch(Exception e){
            e.printStackTrace();
            return new HashMap();
        }
    } 
    
    /**
     * @param lastSyncDate
     * @Added by Jyoti, 02-02-2018
     * @param userId
     * @param accountId
     * @return 
     */
    public java.util.List<java.util.HashMap<String,Object>> assignedTerritoryListAfterLastSync(int userId, int accountId, String lastSyncDate) {
        
        String query = "SELECT t.id, t.category_name, t.is_active, t.created_on, t.parent_category, t.category_position_csv AS csv, t.updated_on, IF( t.updated_on > t.created_on, '1','0') AS status, (SELECT COUNT(id) FROM territory_categories WHERE category_position_csv LIKE CONCAT('%',',',csv) AND is_active = 1) AS childcount FROM territory_categories t, user_territory t1 WHERE t1.teritory_id = t.id AND user_id_fk = ? AND t.updated_on > ? GROUP BY t.id ORDER BY t.category_name";
        Object[] param = new Object[]{userId, lastSyncDate};
        log4jLog.info("assignedTerritoryListAfterLastSync for userId :  " + userId);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<java.util.HashMap<String,Object>>() {

                @Override
                public java.util.HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    java.util.HashMap hMap = new java.util.HashMap();
                    hMap.put("id", rs.getInt("t.id"));
                    hMap.put("categoryName",rs.getString("t.category_name"));
                    hMap.put("isActive",rs.getBoolean("t.is_active"));
                    hMap.put("hasChild", rs.getInt("childcount"));
//                    hMap.put("categoryPositionCsv",rs.getString("csv"));
                    hMap.put("parentCategory", rs.getInt("t.parent_category"));
                    hMap.put("updatedOn", rs.getTimestamp("t.updated_on"));
                    hMap.put("status", rs.getString("status"));
                    return hMap;
                }
            });
        } catch (Exception e) {
            log4jLog.info("assignedTerritoryListAfterLastSync " + e);
            e.printStackTrace();
            return new java.util.ArrayList<java.util.HashMap<String,Object>>();
        }
    }
    
    /**
     * @param lastSyncDate
     * @Added by Jyoti, 02-02-2018
     * @param accountId
     * @return 
     */
    public java.util.List<java.util.HashMap<String,Object>> expenseCategoryListAfterLastSync( int accountId, String lastSyncDate) {
        String query=" SELECT id, category_name, is_active, IF( updated_on > created_on, '1','0') AS status FROM expense_categories WHERE updated_on > ? order by category_name ";
        log4jLog.info("expenseCategoryListAfterLastSync for accountId :  " + accountId);
        try {
            Object[] param = new Object[]{lastSyncDate};
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<java.util.HashMap<String,Object>>() {

                @Override
                public java.util.HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    java.util.HashMap hMap = new java.util.HashMap();
                    hMap.put("id", rs.getInt("id"));
                    hMap.put("categoryName",rs.getString("category_name"));
                    hMap.put("isActive",rs.getBoolean("is_active"));
                    hMap.put("status", rs.getString("status"));
                    return hMap;
                }
            });
        } catch (Exception e) {
            log4jLog.info("expenseCategoryListAfterLastSync " + e);
            e.printStackTrace();
            return new java.util.ArrayList<java.util.HashMap<String,Object>>();
        }
    }
    
    /**
     * @param lastSyncDate
     * @Added by Jyoti, 02-02-2018
     * @param accountId
     * @return 
     */
    public java.util.List<java.util.HashMap<String,Object>> purposeCategoryListAfterLastSync( int accountId, String lastSyncDate) {
        String query=" SELECT id, purpose, is_active, IF( updated_on > created_on, '1','0') AS status FROM activity_purpose WHERE updated_on > ? order by purpose ";
        log4jLog.info("purposeCategoryListAfterLastSync for accountId :  " + accountId);
        try {
            Object[] param = new Object[]{lastSyncDate};
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<java.util.HashMap<String,Object>>() {

                @Override
                public java.util.HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    java.util.HashMap hMap = new java.util.HashMap();
                    hMap.put("id", rs.getInt("id"));
                    hMap.put("purpose",rs.getString("purpose"));
                    hMap.put("isActive",rs.getBoolean("is_active"));
                    hMap.put("status", rs.getString("status"));
                    return hMap;
                }
            });
        } catch (Exception e) {
            log4jLog.info("purposeCategoryListAfterLastSync " + e);
            e.printStackTrace();
            return new java.util.ArrayList<java.util.HashMap<String,Object>>();
        }
    }
    
    /**
     * @param lastSyncDate
     * @Added by Jyoti, 02-02-2018
     * @param accountId
     * @return 
     */
    public java.util.List<java.util.HashMap<String,Object>> industryCategoryListAfterLastSync( int accountId, String lastSyncDate) {
        String query=" SELECT id,category_name,is_active, IF( updated_on > created_on, '1','0') AS status FROM industry_categories WHERE updated_on > ? order by category_name ";
        log4jLog.info("industryCategoryListAfterLastSync for accountId :  " + accountId);
        try {
            Object[] param = new Object[]{lastSyncDate};
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<java.util.HashMap<String,Object>>() {

                @Override
                public java.util.HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    java.util.HashMap hMap = new java.util.HashMap();
                    hMap.put("id", rs.getInt("id"));
                    hMap.put("categoryName",rs.getString("category_name"));
                    hMap.put("isActive",rs.getBoolean("is_active"));
                    hMap.put("status", rs.getString("status"));
                    return hMap;
                }
            });
        } catch (Exception e) {
            log4jLog.info("industryCategoryListAfterLastSync " + e);
            e.printStackTrace();
            return new java.util.ArrayList<java.util.HashMap<String,Object>>();
        }
    }
    
    @Override
    public boolean update_terms_condition_agreed(AuthenticationUser authenticationUser) {
        String query = "update users SET is_terms_condition_agreed = ? ,is_newsletter_opt_in = ?,newsletter_agreed_on=now(),terms_condition_agreed_on=now() where email_address = ?  ";
        Object param[] = new Object[]{authenticationUser.getIs_terms_condition_agreed(), authenticationUser.getIs_newsletter_opt_in(), authenticationUser.getUserEmailAddress()};
        try {
            if (this.jdbcTemplate.update(query, param) > 0) {
//                System.out.println("updated");
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info("updateAccountStats " + e);
            e.printStackTrace();
            return false;
        }
    }
}
