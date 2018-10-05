/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.accounts.dao;

import com.qlc.fieldsense.accounts.model.AccountSetting;
import com.qlc.fieldsense.accounts.model.AccountSettingValues;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author awaneesh
 */
public class AccountSettingDaoImpl implements AccountSettingDao{

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("AccountSettingsDaoImpl");
    // Added by Jyoti
    private JdbcTemplate jdbcTemplate;
    private JdbcTemplate jdbcTemplateStats;
    
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcTemplate getJdbcTemplateStats() {
        return jdbcTemplateStats;
    }

    public void setJdbcTemplateStats(JdbcTemplate jdbcTemplateStats) {
        this.jdbcTemplateStats = jdbcTemplateStats;
    }
    // ended by Jyoti
    /**
     *
     * @param accountId
     * @param name
     * @return
     */
    public AccountSetting selectAccountSetting(int accountId,String name) {
        String query = "SELECT * FROM account_settings where name=?";
        log4jLog.info("getLocationFromLatLong " + query);
        Object[] param = new Object[]{name};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query.toString(), param, new RowMapper<AccountSetting>() {

                public AccountSetting mapRow(ResultSet rs, int i) throws SQLException {
                    AccountSetting setting = new AccountSetting();
                    return setting;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectAccountSetting " + e);
            return null;
            //return new CacheLocation();
        }    
    }

    /**
     *
     * @param accountId
     * @return
     */
    public List<AccountSetting> selectAllAccountSettings(int accountId) {
        String query = "SELECT * FROM account_settings";
        log4jLog.info("account_settings " + query);
        Object[] param = new Object[]{};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<AccountSetting>() {

                public AccountSetting mapRow(ResultSet rs, int i) throws SQLException {
                    AccountSetting setting = new AccountSetting();
                    setting.setId(rs.getInt("id"));
                    setting.setSettingName(rs.getString("name"));
                    setting.setSettingValue(rs.getString("value"));
                    setting.setUpdatedBy(rs.getInt("updated_by"));
                    setting.setUpdatedOn(rs.getTimestamp("updated_on"));
                    return setting;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectAccountSetting " + e);
//            e.printStackTrace();
            return new ArrayList<AccountSetting>();
            //return new CacheLocation();
        }       
    }

    /**
     *
     * @param accountValues
     * @param accountId
     * @return
     */
    public int editAccountSettingValues(AccountSettingValues accountValues, int accountId) {
        String query = "update account_settings set value=?  where name=? ";
                log4jLog.info("editAccountSettingValues " + query);
                String timeoutValue="0";
                if(accountValues.isAllowTimeout()){
                    timeoutValue="1";
                }
                Object[] param = new Object[]{timeoutValue,"allow_timeout"};
                try {
                 int count=FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param);

                   return count;
                } catch (Exception e) {
                    log4jLog.info("editAccountSettingValues " + e);
                    return 0;
                    //return new CacheLocation();
                } 
    }
    
    /**
     * @added by jyoti
     * @param accountValues
     * @param accountId
     * @return
     */
    public int editAccountSettingValuesForOffline(AccountSettingValues accountValues, int accountId) {
        String query = "update account_settings set value=? where name=? ";
                log4jLog.info("editAccountSettingValuesForOffline " + query);
                String offlineValue="0";
                if(accountValues.isAllowOffline()){
                    offlineValue="1";
                }
                Object[] param = new Object[]{offlineValue,"allow_offline"};
                try {
                 int count=FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param);

                   return count;
                } catch (Exception e) {
                    log4jLog.info("editAccountSettingValuesForOffline " + e);
                    return 0;
                    //return new CacheLocation();
                } 
    }
    

    /**
     *
     * @param key
     * @param accountId
     * @return
     */

//    public int editAccountSettingValuesForInterval(AccountSettingValues accountValues, int accountId)
//    {
//       String query = "update account_settings set value=? where name=? ";
//       log4jLog.info("editAccountSettingValuesForInterval " + query);
//       String Interval = accountValues.getInterval();
//        
//       Object[] param = new Object[]{Interval,"Location_interval"};
//       
//       try {
//                 int count=FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param);
//
//                   return count;
//                } catch (Exception e) {
//                    log4jLog.info("editAccountSettingValuesForOffline " + e);
//                    return 0;
//                    //return new CacheLocation();
//                } 
//    }
    
    public String getValueFromAccountSetting(String key,int accountId){
        String query = "SELECT value FROM account_settings where name=?";
        log4jLog.info("getValueFromAccountSetting" + query);
        Object param[] = new Object[]{key};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param,String.class);
        } catch (Exception e) {
//            e.printStackTrace();
            log4jLog.info("getValueFromAccountSetting" + query);
            return "";
        }
    }

    public int editAccountSettingValuesForInterval(AccountSettingValues accountValues, int accountId) {
        String query = "update account_settings set value=? where name=? ";
       log4jLog.info("editAccountSettingValuesForInterval " + query);
       String Interval = accountValues.getInterval();
        
       Object[] param = new Object[]{Interval,"Location_interval"};
       
       try {
                 int count=FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param);

                   return count;
                } catch (Exception e) {
                    log4jLog.info("editAccountSettingValuesForOffline " + e);
                    return 0;
                    //return new CacheLocation();
                } 
    }
    
    // Added by jyoti
    public int editAccountSettingValuesForCurrencySymbol(AccountSettingValues accountValues, int accountId) {
        String query = "UPDATE account_settings SET value=? WHERE name=? ";
        log4jLog.info("editAccountSettingValuesForCurrencySymbol " + query);
        String currencySymbol = accountValues.getCurrencySymbol();        
        Object[] param = new Object[]{currencySymbol,"currency_symbol"};       
        try {
            int count=FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param);
            return count;
        } catch (Exception e) {
            log4jLog.info("editAccountSettingValuesForCurrencySymbol " + e);
            return 0;
        } 
    }
    
    /**
     * @param value
     * @param name
     * @Added by Jyoti, 22-12-2017
     * @param accountId
     * @return 
     */
    @Override
    public int editAccountSettings(int accountId, String value, String name) {
        String query = "UPDATE account_settings SET value = ?  WHERE name = ? ";
        log4jLog.info("editAccountSettings for accountId : " + accountId);        
        Object[] param = new Object[]{value, name};
        try{
            int count=FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param);
            return count;
        } catch (Exception e) {
            log4jLog.info("editAccountSettings for accountId : " + accountId + ", Exception : "+e);
            return 0;
        } 
    }
    
}
