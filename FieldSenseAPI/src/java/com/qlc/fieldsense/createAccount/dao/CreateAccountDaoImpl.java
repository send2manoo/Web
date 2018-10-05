/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.createAccount.dao;

import com.mysql.jdbc.Statement;
import com.qlc.fieldsense.utils.Constant;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Ramesh
 * @date 09-06-2014
 */
public class CreateAccountDaoImpl implements CreateAccountDao {

    JdbcTemplate jdbcTemplate;
    private static final Logger log4jLog = Logger.getLogger("CreateAccountDaoImpl");

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
    public boolean createAccountDB(int accountId) {
        String accountDbName = "account_" + accountId;
        String query = "CREATE DATABASE " + accountDbName;
        log4jLog.info("createAccountDB " + query);
        try {
            jdbcTemplate.execute(query);
            return true;
        } catch (Exception e) {
            log4jLog.info("createAccountDB " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param accountId
     * @return
     */
    @Override
    public boolean deleteAccountDBDetailsFromAccounts(int accountId) {

        String query = "DELETE FROM accounts WHERE id=?";
        log4jLog.info("deleteAccountDB " + query);
        Object[] param = new Object[]{accountId};
        try {
            if (jdbcTemplate.update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info("deleteAccountDB " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param accountId
     * @return
     */
    @Override
    public boolean dropAccountDB(int accountId) {

        String accountDbName = "account_" + accountId;
        String query = "DROP DATABASE " + accountDbName;
        log4jLog.info("dropAccountDB " + query);
        try {
            jdbcTemplate.execute(query);
//            System.out.println("accountId** "+accountId);
//            System.out.println("dropAccountDB");
            return true;
        } catch (Exception e) {
//            System.out.println("AccountDB present ");
            log4jLog.info("dropAccountDB " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param queryType
     * @return
     */
    @Override
    public List<String> getCreateAccountQueries(String queryType) {
        String query = "SELECT sql_query FROM register_account WHERE sql_query like ? ORDER BY id ";
        log4jLog.info("getCreateAccountQueries " + query);
        Object[] param = new Object[]{queryType + "%"};
        try {
            return jdbcTemplate.queryForList(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info("getCreateAccountQueries " + query);
//            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param queryList
     * @param accountId
     * @return
     */
    @Override
    public boolean executeCreateAccountQueries(List<String> queryList, int accountId) {
        Connection connection = null;
        Statement stmt = null;
        try {
            String communityDbName = "account_" + accountId;
            connection = (Connection) DriverManager.getConnection(Constant.DATA_BASE_CONNECTION_URL + communityDbName, Constant.DEFAULT_COMMUNITY_DB_USERNAME, Constant.DEFAULT_COMMUNITY_DB_PASSWORD);
            stmt = (Statement) connection.createStatement();

            for (String createTableQuery : queryList) {
                stmt.addBatch(createTableQuery);
            }
            int[] updateCounts = stmt.executeBatch();

            return true;
        } catch (Exception e) {
            log4jLog.info("executeCreateAccountQueries " + e);
//            System.out.println("exception 1" +e);
            e.printStackTrace();
            return false;
        }finally{
            try{
                if(stmt!=null) stmt.close();
                if(connection!=null) connection.close();                               
            }catch(Exception e){
                log4jLog.info("executeCreateAccountQueries " + e);
//                 System.out.println("exception 2" +e);
                e.printStackTrace();
            }
        }
    }
}
