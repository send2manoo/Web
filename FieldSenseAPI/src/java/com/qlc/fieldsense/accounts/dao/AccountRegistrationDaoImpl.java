/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.accounts.dao;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.qlc.fieldsense.accounts.model.Account;
import com.qlc.fieldsense.accounts.model.AccountLinkActivation;
import static com.qlc.fieldsense.stats.dao.StatsDaoImpl.log4jLog;
import com.qlc.fieldsense.team.model.TeamMember;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSensePasswordEncryptionDecryption;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Ramesh
 */
public class AccountRegistrationDaoImpl implements AccountRegistrationDao {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("AccountRegistrationDaoImpl");
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
     * @param account
     * @return
     */
    @Override
    public int insertAccount(Account account) {
//        String query = "INSERT INTO accounts(company_name,address1,address2,address3,city,state,country,zip_code,company_phone_number1,company_phone_number2,company_website,status,created_on,expired_on,user_limit,region_id_fk,start_date,industry,plan) VALUES (?,?,?,?,?,?,?,?,?,?,?,1,now(),?,?,?,?,?)";
//        String query = "INSERT INTO accounts(company_name,address1,address2,address3,city,state,country,zip_code,company_phone_number1,company_phone_number2,company_website,status,created_on,expired_on,user_limit,region_id_fk,start_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?)";
        String query = "INSERT INTO accounts(company_name,address1,address2,address3,city,state,country,zip_code,company_phone_number1,company_phone_number2,company_website,status,created_on,expired_on,user_limit,region_id_fk,start_date,industry,plan) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,?,?)";
        log4jLog.info(" insertAccount " + query);
//        System.out.println("account.getIndustry()"+account.getIndustry());
//        System.out.println("account.getCompanyWebsite()$%^^^ " + account.getCompanyWebsite());
        try {
//            Object param[] = new Object[]{account.getCompanyName(), account.getAddress1(), account.getAddress2(), account.getAddress3(), account.getCity(), account.getState(), account.getCountry(), account.getZipCode(), account.getCompanyPhoneNumber1(), account.getCompanyPhoneNumber2(), account.getCompanyWebsite(),account.getExpiredOn(),account.getUserLimit(),account.getRegionId(),account.getStartDate(),account.getIndustry(),account.getPlan()};
            Object param[] = new Object[]{account.getCompanyName(), account.getAddress1(), account.getAddress2(), account.getAddress3(), account.getCity(), account.getState(), account.getCountry(), account.getZipCode(), account.getCompanyPhoneNumber1(), account.getCompanyPhoneNumber2(), account.getCompanyWebsite(), account.getStatus(), account.getExpiredOn(), account.getUserLimit(), account.getRegionId(), account.getStartDate(), account.getIndustry(), account.getPlan()};
            synchronized (this) {
                if (jdbcTemplate.update(query, param) > 0) {
                    try {
                        String query1 = "SELECT MAX(id) FROM accounts";
                        log4jLog.info(" insertAccount " + query1);
                        return jdbcTemplate.queryForObject(query1, Integer.class);
                    } catch (Exception e) {
                        log4jLog.info(" insertAccount " + e);
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            log4jLog.info(" insertAccount " + e);
            e.printStackTrace();
            return 0;
        }
    }

    /**
     *
     * @return
     */
    @Override
    public Account selectAccount() {
        synchronized (this) {
            String query = "SELECT id,status,user_limit,region_id_fk,created_on,company_name,address1,address2,address3,city,state,country,zip_code,company_phone_number1,company_phone_number2,company_website FROM accounts WHERE id = (SELECT MAX(id) FROM accounts)";
            log4jLog.info(" selectAccount " + query);
            try {
                return this.jdbcTemplate.queryForObject(query, new RowMapper<Account>() {

                    @Override
                    public Account mapRow(ResultSet rs, int i) throws SQLException {
                        Account account = new Account();
                        account.setId(rs.getInt("id"));
                        account.setStatus(rs.getInt("status"));
                        account.setRegionId(rs.getInt("region_id_fk"));
                        account.setUserLimit(rs.getInt("user_limit"));
                        account.setCreatedOn(rs.getTimestamp("created_on"));
                        account.setCompanyName(rs.getString("company_name"));
                        account.setAddress1(rs.getString("address1"));
                        account.setAddress2(rs.getString("address2"));
                        account.setAddress3(rs.getString("address3"));
                        account.setCity(rs.getString("city"));
                        account.setState(rs.getString("state"));
                        account.setCountry(rs.getString("country"));
                        account.setZipCode(rs.getString("zip_code"));
                        account.setCompanyPhoneNumber1(rs.getLong("company_phone_number1"));
                        account.setCompanyPhoneNumber2(rs.getLong("company_phone_number2"));
                        account.setCompanyWebsite(rs.getString("company_website"));
                        return account;
                    }
                });
            } catch (Exception e) {
                log4jLog.info(" selectAccount " + e);
//                e.printStackTrace();
                return new Account();
            }
        }
    }

    /**
     *
     * @param accountId
     * @return
     */
    @Override
    public Account selectAccount(int accountId) {
        // String query = "SELECT id,status,user_limit,(select email_address from users where account_id_fk='"+accountId+"' and role=1) as adminAddress,region_id_fk,created_on,company_name,address1,address2,address3,city,state,country,zip_code,company_phone_number1,company_phone_number2,company_website,expired_on,start_date,industry,plan FROM accounts WHERE id = ?";
        String query = "SELECT id,status,user_limit,region_id_fk,created_on,company_name,address1,address2,address3,city,state,country,zip_code,company_phone_number1,company_phone_number2,company_website,expired_on,start_date,industry,plan FROM accounts WHERE id = ?";
        log4jLog.info(" selectAccount " + query);
        try {
            Object param[] = new Object[]{accountId};
            return this.jdbcTemplate.queryForObject(query, param, new RowMapper<Account>() {

                @Override
                public Account mapRow(ResultSet rs, int i) throws SQLException {
                    Account account = new Account();
                    account.setId(rs.getInt("id"));
                    account.setStatus(rs.getInt("status"));
                    account.setUserLimit(rs.getInt("user_limit"));
                    account.setRegionId(rs.getInt("region_id_fk"));
                    account.setCreatedOn(rs.getTimestamp("created_on"));
                    account.setCompanyName(rs.getString("company_name"));
                    account.setAddress1(rs.getString("address1"));
                    account.setAddress2(rs.getString("address2"));
                    account.setAddress3(rs.getString("address3"));
                    account.setCity(rs.getString("city"));
                    account.setState(rs.getString("state"));
                    account.setCountry(rs.getString("country"));
                    account.setZipCode(rs.getString("zip_code"));
                    account.setCompanyPhoneNumber1(rs.getLong("company_phone_number1"));
                    account.setCompanyPhoneNumber2(rs.getLong("company_phone_number2"));
                    account.setCompanyWebsite(rs.getString("company_website"));
                    account.setExpiredOn(rs.getTimestamp("expired_on"));
                    account.setsExpiredOn((rs.getTimestamp("expired_on")).toString());
                    account.setStartDate(rs.getTimestamp("start_date"));
                    account.setStrStartDate((rs.getString("start_date")).toString());
                    //added by nikhil:- 11th july 2017  
                    account.setIndustry(rs.getString("industry"));
                    account.setPlan(rs.getString("plan"));
                    //ended by nikhil 
//                    System.out.println("@@@@@@@@@@@@@" + rs.getString("industry") + rs.getString("plan"));
                    return account;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAccount " + e);
//            e.printStackTrace();
            return new Account();
        }
    }

    /**
     *
     * @return
     */
    @Override
    public List<Account> selectAllAccounts() {
        String query = "SELECT id,status,user_limit,region_id_fk,created_on,expired_on,company_name,address1,address2,address3,city,state,country,zip_code,company_phone_number1,company_phone_number2,company_website FROM accounts";
        log4jLog.info(" selectAllAccounts " + query);
        try {
            return this.jdbcTemplate.query(query, new RowMapper<Account>() {

                @Override
                public Account mapRow(ResultSet rs, int i) throws SQLException {
                    Account account = new Account();
                    account.setId(rs.getInt("id"));
                    account.setStatus(rs.getInt("status"));
                    account.setUserLimit(rs.getInt("user_limit"));
                    account.setRegionId(rs.getInt("region_id_fk"));
                    account.setCreatedOn(rs.getTimestamp("created_on"));
                    account.setExpiredOn(rs.getTimestamp("expired_on"));
                    account.setCompanyName(rs.getString("company_name"));
                    account.setAddress1(rs.getString("address1"));
                    account.setAddress2(rs.getString("address2"));
                    account.setAddress3(rs.getString("address3"));
                    account.setCity(rs.getString("city"));
                    account.setState(rs.getString("state"));
                    account.setCountry(rs.getString("country"));
                    account.setZipCode(rs.getString("zip_code"));
                    account.setCompanyPhoneNumber1(rs.getLong("company_phone_number1"));
                    account.setCompanyPhoneNumber2(rs.getLong("company_phone_number2"));
                    account.setCompanyWebsite(rs.getString("company_website"));
                    return account;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAllAccounts " + e);
//            e.printStackTrace();
            return new ArrayList<Account>();
        }
    }

    /**
     *
     * @param accountId
     * @return
     */
    @Override
    public boolean deleteAccount(int accountId) {
        String query = "DELETE FROM accounts WHERE id=?";
        Object param[] = new Object[]{accountId};
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

    /**
     *
     * @return
     */
    @Override
    public List<String> getCreateAccountDeafultQueries() {
        String query = "SELECT sql_query FROM create_account_deafult_data ORDER BY id ";
        log4jLog.info("getCreateUserDeafultQueries " + query);
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
    public boolean executeAccountUserDeafultQueries(List<String> queryList, int userId, int accountId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            String communityDbName = "account_" + accountId;
            connection = (Connection) DriverManager.getConnection(Constant.DATA_BASE_CONNECTION_URL + communityDbName, Constant.DEFAULT_COMMUNITY_DB_USERNAME, Constant.DEFAULT_COMMUNITY_DB_PASSWORD);

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
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
//                e.printStackTrace();
                log4jLog.info("executeCreateAccountQueries " + e);
            }
        }
    }

    /**
     *
     * @param account
     * @return
     */
    @Override
    public boolean editAccountDetails(Account account) {
        String query = "update accounts set region_id_fk=?,user_limit=?,status=?,company_name=?,address1=? ,city=?,state=?,country=?,zip_code=?,company_phone_number1=?,expired_on=?,start_date=?,plan=?,industry=? WHERE id=?";
        Object param[] = new Object[]{account.getRegionId(), account.getUserLimit(), account.getStatus(), account.getCompanyName(), account.getAddress1(), account.getCity(), account.getState(), account.getCountry(), account.getZipCode(), account.getCompanyPhoneNumber1(), account.getsExpiredOn(), account.getStrStartDate(), account.getPlan(), account.getIndustry(), account.getId()};
        try {
            if (this.jdbcTemplate.update(query, param) > 0) {
                String query1 = "update users set first_name = ?,full_name = ?,email_address = ? WHERE account_id_fk=? AND role=0";
                Object param1[] = new Object[]{account.getCompanyName(), account.getCompanyName(), account.getCompanyName() + "@fieldsense.in", account.getId()};
                jdbcTemplate.update(query1, param1);
//                System.out.println("company name updated");
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" editAccountStatus " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param account
     * @return
     */
    public boolean updateAccountStats(Account account) {
        String query = "update fieldsensestats.accounts_punchin_stats set user_limit=?,region_id_fk=?,status=?,company_name=? WHERE account_id=?";
        //  System.out.println("@@@@@@########$$$$$$%%%%%%" + " " + account.getUserLimit() + " " + account.getRegionId() + " " + account.getStatus() + " " + account.getCompanyName() + " " + account.getId());
        Object param[] = new Object[]{account.getUserLimit(), account.getRegionId(), account.getStatus(), account.getCompanyName(), account.getId()};
        try {
            if (this.jdbcTemplate.update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info("updateAccountStats " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param user
     * @return
     */
    public boolean updateSuperAdminUserDetails(User user) {
        String query = "UPDATE users SET first_name=?,last_name=?,full_name=?,password=?,mobile_number=?,active=?,role=?,designation=?,gender=? WHERE id=?";
        log4jLog.info(" updateUserDetails " + query);
        Object param[] = new Object[]{user.getFirstName(), user.getLastName(), user.getFirstName() + " " + user.getLastName(), user.getPassword(), user.getMobileNo(), user.isActive(), user.getRole(), user.getDesignation(), user.getGender(), user.getId()};
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
    public boolean updateUserDetails(User user) {
        String query = "UPDATE users SET first_name=?,last_name=?,full_name=?,password=?,mobile_number=?,active=?,role=?,designation=?,gender=? WHERE id=?";
        log4jLog.info(" updateUserDetails " + query);
        Object param[] = new Object[]{user.getFirstName(), user.getLastName(), user.getFirstName() + " " + user.getLastName(), user.getPassword(), user.getMobileNo(), user.isActive(), user.getRole(), user.getDesignation(), user.getGender(), user.getId()};
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
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param account
     * @return
     */
    public boolean insertInAccountsPunchinStats(Account account) {
        String query = "INSERT INTO  fieldsensestats.accounts_punchin_stats(account_id,company_name,user_limit,created_on,user_count,region_id_fk) VALUES (?,?,?,now(),2,?) ";
        Object param[] = new Object[]{account.getId(), account.getCompanyName(), account.getUserLimit(), account.getRegionId()};
        try {
            if (this.jdbcTemplate.update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info("updateAccountStats " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @return
     */
    public List<java.util.HashMap> getAllAccountRegions() {
        String query = "SELECT * FROM account_regions ORDER BY id ";
        log4jLog.info("getCreateUserDeafultQueries " + query);
        try {
            return this.jdbcTemplate.query(query, new RowMapper<HashMap>() {

                @Override
                public HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    HashMap map = new HashMap();
                    map.put("id", rs.getInt("id"));
                    map.put("region_name", rs.getString("region_name"));
                    map.put("created_on", rs.getTimestamp("created_on"));
                    return map;
                }
            });

        } catch (Exception e) {
            log4jLog.info("getAllAccountRegions " + query);
//            e.printStackTrace();
            return new ArrayList<HashMap>();
        }
    }

//        @Override
//    public boolean updateAllAdmin(Account account) { 
//       String query = "UPDATE users SET first_name=?,gender=?,mobile_number=?,designation=?,updated_on=now(),updated_by=? WHERE id=?";
//        Object param[] = new Object[]{account.getAllAdminUser().get(index)};
//
//            System.out.println("devaa ashirvad"+user.getInterval());
//        try {
//            if (jdbcTemplate.update(query, param) == 1) {
//                if (!user.isActive()) {
//                    String deleteQuery = "DELETE FROM user_auth WHERE user_id_fk=?";
//                    Object deleteParam[] = new Object[]{user.getId()};
//                    jdbcTemplate.update(deleteQuery, deleteParam);
//                }
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            log4jLog.info(" updateUserDetails " + e);
//            return false;
//        }
//    }
    public boolean updateAllAdmin(Account account) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean updateAllAdmin(String full_name, String first_name, String Last_name, int gender, String mobile_no, String designation, String userid, String email_add, int id, String adminPassword, int reportTo, User user) {
//        String query = "UPDATE users SET first_name=?,email_address=?,gender=?,mobile_number=?,designation=?,updated_on=now(),updated_by=? WHERE id=?";
        String query = "";
        Object param[] = {};
//        System.out.println("inside updateAllAdmin user id " + userid);
//        if (userid == "") {
////            query = "INSERT INTO users (first_name,account_id_fk,password,email_address,gender,mobile_number,designation,updated_on,updated_by) VALUES (?,?,?,?,?,?,?,now(),1)";
////            param = new Object[]{first_name, id, adminPassword, email_add, gender, mobile_no, designation};
//            System.out.println("into insert");
//            
//            query = "INSERT INTO users(account_id_fk, first_name, last_name,full_name, email_address, password, mobile_number, user_accuracy, check_in_radius, gender, role, report_to, active, last_logged_on, last_known_location_time, last_known_location, created_on, created_by,designation,allow_timeout,allow_offline,accountcontact_type) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now(),?,now(),?,?,?,?,?)";
//        log4jLog.info(" insertUser " + query);
//        String adm_password = FieldSensePasswordEncryptionDecryption.hashPassword(adminPassword);
//        param = new Object[]{id, first_name, user.getLastName(), user.getFirstName() + " " + user.getLastName(), email_add, adm_password, mobile_no, user.getUserAccuracy(), user.getCheckInRadius(), gender, 1,reportTo ,1, user.getLastKnownLocation(), 1, designation,user.getAllowTimeout(),user.getAllowOffline(),user.getAccountContactType()};
//
//            try {
//                if (this.jdbcTemplate.update(query, param) > 0) {
//                    return true;
//                } else {
//                    return false;
//                }
//            } catch (Exception e) {
//                log4jLog.info("updateAccountStats " + e);
////            e.printStackTrace();
//                return false;
//            }
//        } else {
        query = "UPDATE users SET first_name=?,last_name=?,full_name=?,email_address=?,password=?,gender=?,mobile_number=?,designation=?,updated_on=now(),updated_by=? WHERE id=?";
//            System.out.println("into update");
        param = new Object[]{first_name, Last_name, full_name, email_add, adminPassword, gender, mobile_no, designation, 1, userid};

        //  Object param[] = new Object[]{first_name,email_add,gender,mobile_no,designation,1,userid};
        try {
            if (jdbcTemplate.update(query, param) > 0) {
//                if (!user.isActive()) {
//                    String deleteQuery = "DELETE FROM user_auth WHERE user_id_fk=?";
//                    Object deleteParam[] = new Object[]{user.getId()};
//                    jdbcTemplate.update(deleteQuery, deleteParam);
//                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateUserDetails " + e);
            return false;
        }
        // }
    }

    public int getReportToID(int accountId) {

        try {
            String query = "select user_id_fk from teams where id=100000";
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int insertNewAdmin(String full_name, String first_name, String Last_name, int gender, String mobile_no, String designation, String userid, String email_add, int id, String adminPassword, int reportTo, User user) {
        String query = "INSERT INTO users(account_id_fk, first_name, last_name,full_name, email_address, password, mobile_number, user_accuracy, check_in_radius, gender, role, report_to, active, last_logged_on, last_known_location_time, last_known_location, created_on, created_by,designation,allow_timeout,allow_offline,accountcontact_type) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now(),?,now(),?,?,?,?,?)";
        log4jLog.info(" insertAdmin " + query);
        String adm_password = FieldSensePasswordEncryptionDecryption.hashPassword(adminPassword);

        try {
            Object param[] = new Object[]{id, first_name, Last_name, full_name, email_add, adm_password, mobile_no, user.getUserAccuracy(), user.getCheckInRadius(), gender, 1, reportTo, 1, user.getLastKnownLocation(), 1, designation, user.getAllowTimeout(), user.getAllowOffline(), user.getAccountContactType()};
            if (this.jdbcTemplate.update(query, param) > 0) {
                try {
                    String query1 = "SELECT MAX(id) FROM users";
                    log4jLog.info(" insertAccount " + query1);
                    return jdbcTemplate.queryForObject(query1, Integer.class);
                } catch (Exception e) {
                    log4jLog.info(" insertAccount " + e);
                    return 0;
                }
            } else {
                return 0;
            }
        } catch (Exception e) {
            log4jLog.info("insertNewAdmin " + e);
            e.printStackTrace();
            return 0;
        }
    }
//     public int insertAccount(Account account) {
//        //String query = "INSERT INTO accounts(company_name,address1,address2,address3,city,state,country,zip_code,company_phone_number1,company_phone_number2,company_website,status,created_on,expired_on,user_limit,region_id_fk,start_date,industry,plan) VALUES (?,?,?,?,?,?,?,?,?,?,?,1,now(),?,?,?,?)";
//        String query = "INSERT INTO accounts(company_name,address1,address2,address3,city,state,country,zip_code,company_phone_number1,company_phone_number2,company_website,status,created_on,expired_on,user_limit,region_id_fk,start_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,1,now(),?,?,?,?)";
//        log4jLog.info(" insertAccount " + query);
//        System.out.println("account.getCompanyWebsite()$%^^^ " + account.getCompanyWebsite());
//        try {
////            Object param[] = new Object[]{account.getCompanyName(), account.getAddress1(), account.getAddress2(), account.getAddress3(), account.getCity(), account.getState(), account.getCountry(), account.getZipCode(), account.getCompanyPhoneNumber1(), account.getCompanyPhoneNumber2(), account.getCompanyWebsite(),account.getExpiredOn(),account.getUserLimit(),account.getRegionId(),account.getStartDate(),account.getIndustry(),account.getPlan()};
//            Object param[] = new Object[]{account.getCompanyName(), account.getAddress1(), account.getAddress2(), account.getAddress3(), account.getCity(), account.getState(), account.getCountry(), account.getZipCode(), account.getCompanyPhoneNumber1(), account.getCompanyPhoneNumber2(), account.getCompanyWebsite(), account.getExpiredOn(), account.getUserLimit(), account.getRegionId(), account.getStartDate()};
//            synchronized (this) {
//                if (jdbcTemplate.update(query, param) > 0) {
//                    try {
//                        String query1 = "SELECT MAX(id) FROM accounts";
//                        log4jLog.info(" insertAccount " + query1);
//                        return jdbcTemplate.queryForObject(query1, Integer.class);
//                    } catch (Exception e) {
//                        log4jLog.info(" insertAccount " + e);
//                        return 0;
//                    }
//                } else {
//                    return 0;
//                }
//            }
//        } catch (Exception e) {
//            log4jLog.info(" insertAccount " + e);
//            e.printStackTrace();
//            return 0;
//        }
//    }

    public int getMaxIdFromTeams(int accountId) {
        try {
            String query = "select max(id) from teams";
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String getAdminPassword(int adminID) {
        try {
            String query = "select password from users where id='" + adminID + "'";
            return jdbcTemplate.queryForObject(query, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public Map<String, Object> getTeamDataForUser(int id, int accountId) {
        try {

//         System.out.println("accountId"+accountId);
            String query = "select IFNULL(t.id,0) id,substring(t.team_position_csv,8,6) teamPosition,IFNULL(u.email_address,'') emailAddress from teams as t left join fieldsense.users as u on t.user_id_fk=u.id where t.user_id_fk=?";
            Object param[] = new Object[]{id};
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForMap(query, param);
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<String, Object>();
        }

    }

    public String getUserTokenFromId(int id) {
        String query = "select user_token from users where id=?";
        return null;
    }

    public int getAccountId(int userId) {
        try {
            String query = "select account_id_fk from users where id=?";
            Object param[] = new Object[]{userId};
//            System.out.println("id"+userId);
            return jdbcTemplate.queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean getCountOfAdmin(int accountId) {
        try {
            String query = "select count(id) from users where account_id_fk=? AND role=1";
            Object param[] = new Object[]{accountId};
//            System.out.println("id"+accountId);
            int userCount = jdbcTemplate.queryForObject(query, param, Integer.class);
            if (userCount > 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(int userId) {
        try {
            String query = "DELETE FROM users WHERE id=?";
            Object param[] = new Object[]{userId};
//            System.out.println("id"+userId);
            int userCount = jdbcTemplate.update(query, param);
            if (userCount > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * @param otp
     * @param mobile_no
     * @Added by jyoti
     * @param encryptedKey
     * @param emailAddress
     * @return
     */
    @Override
    public boolean insertLinkActivationData(String encryptedKey, String emailAddress, String otp, String mobile_no) {
        try {
//            String query="INSERT INTO linkactivation (emailAddress, encryptedkey, created_on) values(?,?,now())";
            String query = "INSERT INTO linkactivation (emailAddress, encryptedkey, created_on,mobile_number,otp_validation ,created_on_otp,otp_expired) values(?,?,now(),?,?,now(),DATE_ADD(NOW(), INTERVAL 15 MINUTE))";
            Object param[] = new Object[]{emailAddress, encryptedKey, mobile_no, otp};
            int rowCount = jdbcTemplate.update(query, param);
            return rowCount > 0;
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info("insertLinkActivationData " + e);
            return false;
        }
    }

    /**
     * @Added by jyoti
     * @param encryptedKey
     * @return mobile_number
     */
    @Override
    public AccountLinkActivation selectLinkActivationData(String encryptedKey) {
        String query = "SELECT IFNULL(emailAddress,'') AS emailAddress,IFNULL(mobile_number,'') AS mobileNumber,IFNULL(encryptedkey,'') AS encryptedkey, CONVERT_TZ(created_on,'+00:00','+5:30') AS created_on FROM linkactivation WHERE encryptedkey = ?";
        try {
            Object param[] = new Object[]{encryptedKey};
            return this.jdbcTemplate.queryForObject(query, param, new RowMapper<AccountLinkActivation>() {
                @Override
                public AccountLinkActivation mapRow(ResultSet rs, int i) throws SQLException {
                    AccountLinkActivation linkActivation = new AccountLinkActivation();
                    linkActivation.setEmailAddress(rs.getString("emailAddress"));
                    linkActivation.setMobileNo(rs.getString("mobileNumber"));
                    linkActivation.setEncryptedkey(rs.getString("encryptedkey"));
                    linkActivation.setCreatedOn(rs.getTimestamp("created_on"));
                    return linkActivation;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectLinkActivationData " + e);
            e.printStackTrace();
            return new AccountLinkActivation();
        }
    }

    public String validateOTP(String OTP, String mobile_no, String email_address) {
        try {
            String query = "select otp_validation from linkactivation where emailAddress='" + email_address + "' AND mobile_number=" + mobile_no;
            return jdbcTemplate.queryForObject(query, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public AccountLinkActivation validateOTP1(String OTP, String mobile_no, String email_address) {
        String query = "select otp_validation,otp_expired from linkactivation where emailAddress=? AND mobile_number=?";
        try {
            Object param[] = new Object[]{email_address, mobile_no};
            return this.jdbcTemplate.queryForObject(query, param, new RowMapper<AccountLinkActivation>() {
                @Override
                public AccountLinkActivation mapRow(ResultSet rs, int i) throws SQLException {
                    AccountLinkActivation linkActivation = new AccountLinkActivation();
                    linkActivation.setOTP(rs.getString("otp_validation"));
                    linkActivation.setOTP_Expiration(rs.getTimestamp("otp_expired"));

                    return linkActivation;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectLinkActivationData " + e);
            e.printStackTrace();
            return new AccountLinkActivation();
        }
    }

    public boolean updateOTP(String mobile_no, String email_address, String OTP) {
        String query = "update linkactivation set otp_validation=?,created_on_otp=now(),otp_expired=DATE_ADD(NOW(), INTERVAL 0.5 HOUR) where emailAddress=? AND mobile_number=?";
//            System.out.println("into update");
        Object[] param = new Object[]{OTP, email_address, mobile_no};
//          System.out.println("mobile_no"+mobile_no);
//          System.out.println("email_address"+email_address);
//          System.out.println("mobile_no"+mobile_no);

        //  Object param[] = new Object[]{first_name,email_add,gender,mobile_no,designation,1,userid};
        try {
            if (jdbcTemplate.update(query, param) > 0) {
//                if (!user.isActive()) {
//                    String deleteQuery = "DELETE FROM user_auth WHERE user_id_fk=?";
//                    Object deleteParam[] = new Object[]{user.getId()};
//                    jdbcTemplate.update(deleteQuery, deleteParam);
//                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateUserDetails " + e);
            e.printStackTrace();
            return false;
        }
    }

    public boolean resendOTP_re_Registration(String encryptedKey, String mobile_no, String email_address, String OTP) {
        String query = "UPDATE linkactivation SET encryptedkey = ?, created_on = NOW(), otp_validation = ?, created_on_otp = NOW(), otp_expired = DATE_ADD(NOW(), INTERVAL 0.5 HOUR) WHERE emailAddress = ? AND mobile_number = ?";
//        System.out.println("into resendOTP_re_Registration");
        Object[] param = new Object[]{encryptedKey, OTP, email_address, mobile_no};
//          System.out.println("mobile_no"+mobile_no);
//          System.out.println("email_address"+email_address);
//          System.out.println("mobile_no"+mobile_no);

        //  Object param[] = new Object[]{first_name,email_add,gender,mobile_no,designation,1,userid};
        try {

            if (jdbcTemplate.update(query, param) > 0) {
//                if (!user.isActive()) {
//                    String deleteQuery = "DELETE FROM user_auth WHERE user_id_fk=?";
//                    Object deleteParam[] = new Object[]{user.getId()};
//                    jdbcTemplate.update(deleteQuery, deleteParam);
//                }
//                System.out.println("if update > " + jdbcTemplate.update(query, param));
                return true;
            } else {
//                System.out.println("else update > " + jdbcTemplate.update(query, param));
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateUserDetails " + e);
            e.printStackTrace();
            return false;
        }
    }

    public boolean isMobileNumberEmailExist(String mobile_no, String emailAddress) {
        String query = "select count(*) from linkactivation where emailAddress=? AND mobile_number=?;";
//        log4jLog.info("Inside FieldSenseStatsUtils class  isStatRecordAvailableForTheDate() method" + query);
        Object param[] = new Object[]{emailAddress, mobile_no};
        try {
            if (jdbcTemplate.queryForObject(query, param, Integer.class) == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info("Inside FieldSenseStatsUtils class  isStatRecordAvailableForTheDate() method" + e);
            return false;
        }
    }

    /**
     * @param accountId
     * @added by jyoti, remove later
     * @param encryptedKey
     * @return
     */
    @Override
    public boolean insertAuthToken(String encryptedKey, int accountId) {
        try {
            String query = "INSERT INTO accounts_erp_auth_detail (account_id_fk, auth_token, created_on, modified_on) values(?,?,now(),now())";
            Object param[] = new Object[]{accountId, encryptedKey};
            int rowCount = jdbcTemplate.update(query, param);
//            System.out.println("for accountId : " + accountId + " , rowCount : " + rowCount);
            return rowCount > 0;
        } catch (Exception e) {
//            e.printStackTrace();
//            log4jLog.info("insertLinkActivationData " + e);
            return false;
        }
    }

    /**
     * @added by jyoti, remove later
     * @return
     */
    @Override
    public List<AccountLinkActivation> selectListOfEmailIdsDetails() {
//       String query = "SELECT l.emailAddress, l.encryptedkey, l.mobile_number FROM linkactivation l  LEFT OUTER JOIN users a ON l.emailAddress = a.email_address WHERE a.created_on IS NULL ";
//        String query = " SELECT l.emailAddress, l.encryptedkey, l.mobile_number FROM linkactivation l WHERE l.id IN (8,22,63,64) "; // for testing
        String query = "";
//        System.out.println("query : " + query);
        try {
            return this.jdbcTemplate.query(query, new RowMapper<AccountLinkActivation>() {
                @Override
                public AccountLinkActivation mapRow(ResultSet rs, int i) throws SQLException {
                    AccountLinkActivation r = new AccountLinkActivation();
                    r.setEmailAddress(rs.getString("l.emailAddress"));
                    r.setMobileNo(rs.getString("l.mobile_number"));
                    r.setEncryptedkey(rs.getString("l.encryptedkey"));
                    return r;
                }
            });
        } catch (Exception e) {
            log4jLog.info("getAllAccountRegions " + query);
            e.printStackTrace();
            return new ArrayList();
        }
    }

}
