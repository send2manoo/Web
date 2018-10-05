/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Ramesh
 */
public class CommunityDataSourceManager {

    /**
     *
     */
    public static final org.apache.log4j.Logger log4jLog = org.apache.log4j.Logger.getLogger("CommunityDataSourceManager");
    private JdbcTemplate jdbcTemplate;
    private static HashMap<Integer, ComboPooledDataSource> mapOfDataSorce = new HashMap<Integer, ComboPooledDataSource>();
    private String databaseUrl;

    /**
     *
     * @return
     */
    public String getDatabaseUrl() {
        return databaseUrl;
    }

    /**
     *
     * @param databaseUrl
     */
    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
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
     * @param dataSource
     * @param databaseUrl
     */
    public CommunityDataSourceManager(DataSource dataSource, String databaseUrl) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.databaseUrl = databaseUrl;
        log4jLog.info("CommunityDataSourceManager dataSource: "+dataSource+" databaseUrl: "+databaseUrl);
        refreshCommunityDataSourceManager();
    }

    /**
     *
     */
    public void refreshCommunityDataSourceManager() {
        log4jLog.info("refreshCommunityDataSourceManager");
        mapOfDataSorce = generateC3P0DataSource();
    }

    private ComboPooledDataSource getC3P0DataSource(int communityId, String userName, String password) {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass("com.mysql.jdbc.Driver");
        } catch (PropertyVetoException ex) {
            Logger.getLogger(CommunityDataSourceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        dataSource.setJdbcUrl(databaseUrl + "account_" + communityId + "?autoReconnect=true");
        dataSource.setMinPoolSize(Constant.DEFAULT_COMMUNITY_DB_MIN_CONNECTION);
        dataSource.setMaxPoolSize(Constant.DEFAULT_COMMUNITY_DB_MAX_CONNECTION);
        dataSource.setMaxConnectionAge(Constant.COMMUNITY_MAX_CONNECTION_AGE);
        dataSource.setMaxIdleTimeExcessConnections(Constant.COMMUNITY_MAX_IDEAL_TIME_EXCESS_CONNECTIONS);
        dataSource.setAcquireIncrement(Constant.COMMUNITY_ACQUIRE_INCREMENT);
        dataSource.setMaxIdleTime(Constant.COMMUNITY_MAX_IDEAL_TIME);
        dataSource.setNumHelperThreads(Constant.COMMUNITY_NUM_HELPER_THREADS);
        dataSource.setUnreturnedConnectionTimeout(Constant.COMMUNITY_UNRETURNED_CONNECTION_TIMEOUT);
        dataSource.setUser(userName);
        dataSource.setPassword(password);
        return dataSource;
    }

    private synchronized HashMap<Integer, ComboPooledDataSource> generateC3P0DataSource() {
        log4jLog.info("generateC3P0DataSource");
        HashMap<Integer, ComboPooledDataSource> dataSourceMap = new HashMap<Integer, ComboPooledDataSource>();
       // log4jLog.info("generateC3P0DataSource dataSourceMap: "+dataSourceMap);
        List<Integer> communityIds = getCommunityIdList();
        for (int id : communityIds) {
            dataSourceMap.put(id, getC3P0DataSource(id, Constant.DEFAULT_COMMUNITY_DB_USERNAME, Constant.DEFAULT_COMMUNITY_DB_PASSWORD));
        }
        return dataSourceMap;
    }

    private List<Integer> getCommunityIdList() {
        List<Integer> listOfCommunities = this.jdbcTemplate.query("select id from accounts",
                new RowMapper<Integer>() {

                    @Override
                    public Integer mapRow(ResultSet rs, int i) throws SQLException {
                        return rs.getInt("id");
                    }
                });
        return listOfCommunities;
    }

    /**
     *
     * @param communityId
     * @return
     */
    public ComboPooledDataSource getDataSource(int communityId) {
        try{
        log4jLog.info("getDataSource  communityId: "+communityId);
       // refreshCommunityDataSourceManager();
       log4jLog.info( "Data source size: " +mapOfDataSorce.size() );
       // log4jLog.info("getDataSource  mapOfDataSorce: "+mapOfDataSorce.get(communityId));
        }
        catch(Exception e)
        {
          log4jLog.info(" getDataSource stack trace : " + e.getStackTrace()+"\nmessage: "+ e.getMessage()+"\ncause: "+e.getCause());

        }
       
        if(mapOfDataSorce.get(communityId)==null)
        {
         ((CommunityDataSourceManager)GetApplicationContext.ac.getBean("communityDataSourceManager")).setDataSource(communityId);
        }
        
        log4jLog.info( "Data source size: " +mapOfDataSorce.size() );
        //log4jLog.info("getDataSource  mapOfDataSorce: "+mapOfDataSorce.get(communityId));
        
        return mapOfDataSorce.get(communityId);
        
    }

    /**
     *
     * @param communityId
     */
    public void setDataSource(int communityId) {
        log4jLog.info("setDataSource  communityId: "+communityId);
       // log4jLog.info("setDataSource  username: "+Constant.DEFAULT_COMMUNITY_DB_USERNAME+" password: "+Constant.DEFAULT_COMMUNITY_DB_PASSWORD);
       // log4jLog.info("setDataSource  getC3P0DataSource: "+getC3P0DataSource(communityId, Constant.DEFAULT_COMMUNITY_DB_USERNAME, Constant.DEFAULT_COMMUNITY_DB_PASSWORD));
        
        mapOfDataSorce.put(communityId, getC3P0DataSource(communityId, Constant.DEFAULT_COMMUNITY_DB_USERNAME, Constant.DEFAULT_COMMUNITY_DB_PASSWORD));
    }
}
