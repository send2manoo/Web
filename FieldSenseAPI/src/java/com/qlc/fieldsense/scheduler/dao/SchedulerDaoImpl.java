/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.scheduler.dao;

import com.qlc.fieldsense.scheduler.model.Scheduler;
import com.qlc.fieldsense.usersTravelLogs.model.UsersTravelLogs;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;

/**
 *
 * @author siddhesh
 */
public class SchedulerDaoImpl implements SchedulerDao{
    
    public static final Logger log4jLog = Logger.getLogger(" Scheduler ");
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


    public List<UsersTravelLogs> getListOfDataFromLocationNotFound() {
          String query = "SELECT * from location_not_found";
        try {
             return jdbcTemplateCache.query(query,new RowMapper<UsersTravelLogs>(){

                @Override
                public UsersTravelLogs mapRow(ResultSet rs, int i) throws SQLException {
                    UsersTravelLogs userTravelLogs = new UsersTravelLogs();
                    userTravelLogs.setId(rs.getInt("id"));
                    userTravelLogs.setLatitude(rs.getDouble("lang"));
                    userTravelLogs.setLangitude(rs.getDouble("lat"));
                    userTravelLogs.setAccountId(rs.getInt("account_id_fk"));
                    userTravelLogs.setUserId(rs.getInt("user_id_fk"));
                    userTravelLogs.setCustomerName(rs.getString("customer_name"));
                     userTravelLogs.setLocationIdentifier(rs.getString("customer_location_identifier"));
                     userTravelLogs.setIsCustomerLocationInt(rs.getInt("is_customer_location"));
                     userTravelLogs.setSourceValue(rs.getInt("source_value"));
                     userTravelLogs.setTravelDistance(rs.getDouble("distance_travel"));
                    return userTravelLogs;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectUsersTravelLogsLastLocation " + e);
            e.printStackTrace();
            return new ArrayList<UsersTravelLogs>();
        }
    }

    public boolean deletelocationNotFoundEntires(List<Integer> listOfIds) {
        try{
        StringBuilder Ids=new StringBuilder();
        Ids.append(listOfIds.get(0).toString());
        for(int i=1;i<listOfIds.size();i++){
        Ids.append(","+listOfIds.get(i).toString());
        }
//         System.err.println("this is delete"+listOfIds.size());
        String query="delete from location_not_found where id in ("+Ids+")";
//        System.out.println("query"+query);
        if(jdbcTemplateCache.update(query) > 0){
                return true;
        }else{            
             return false;
        }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
}
