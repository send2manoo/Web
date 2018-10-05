/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.cacheLocation.dao;

import com.qlc.cacheLocation.model.CacheLocation;
//import static com.qlc.fieldsense.user.dao.UserDaoImpl.log4jLog;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
        

/**
 *
 * @author awaneesh
 */
public class CacheLocationDaoImpl implements CacheLocationDao{
    JdbcTemplate jdbcTemplate;
      
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("cacheDaoImpl");
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
    
    /**
     *
     * @param latitude
     * @param longitude
     * @return
     */
    @Override
    public CacheLocation getLocationFromLatLong(double latitude,double longitude){
        String query = "SELECT * FROM locations WHERE latitude=? and longitude=?";
        log4jLog.info("getLocationFromLatLong " + query);
        Object[] param = new Object[]{latitude,longitude};
        try {
            return jdbcTemplateCache.queryForObject(query.toString(), param, new RowMapper<CacheLocation>() {

                @Override
                public CacheLocation mapRow(ResultSet rs, int i) throws SQLException {
                    CacheLocation cache = new CacheLocation();
                    cache.setId(rs.getInt("id"));
                    cache.setLatitude(rs.getDouble("latitude"));
                    cache.setLongitude(rs.getDouble("longitude"));
                    cache.setAddress(rs.getString("address"));
                    // nikhil bhosale :- added short_address
                   // cache.setShort_address(rs.getString("short_addr"));
                    cache.setCreatedOn(rs.getTimestamp("created_on"));
                    cache.setIsFetchFromGoogle(rs.getInt("is_fetch_google"));
                    //cache.setLastUsedOn(rs.getTimestamp("last_used_on"));
                    //cache.setUsedCount(rs.getInt("used_count"));                    
                    return cache;
                }
            });
        } catch (Exception e) {
            log4jLog.info("getLocationFromLatLong " + e);
            return null;
            //return new CacheLocation();
        }
        
    }
    
    /**
     *
     * @param location
     * @return
     */
    @Override
    public CacheLocation createLocationInDB(CacheLocation location){ //Need to be optimized
        String query = "INSERT INTO locations (latitude,longitude,address,created_on,is_fetch_google) VALUES(?,?,?,now(),?)";
        log4jLog.info(" createLocationInDB " + query);
        // Nikhil bhosale - short address code :- below one line is commented which is related to short address
//         String query = "INSERT INTO locations (latitude,longitude,address,created_on,is_fetch_google,short_addr) VALUES(?,?,?,now(),?,?)";
       // Object param[] = new Object[]{location.getLatitude(),location.getLongitude(),location.getAddress(),location.getIsFetchFromGoogle(),location.getShort_address()};
        boolean flag=false;
        Object param[] = new Object[]{location.getLatitude(),location.getLongitude(),location.getAddress(),location.getIsFetchFromGoogle()};
        try {
            synchronized (this) {
                flag=jdbcTemplateCache.update(query, param) > 0;
//                if (jdbcTemplateCache.update(query, param) > 0) {
//                    try {
//                        String query1 = "SELECT id FROM locations where latitude=? and longitude=?";
//                        param = new Object[]{location.getLatitude(),location.getLongitude()};
//                        int id=jdbcTemplateCache.queryForObject(query1,param, Integer.class);
//                        location.setId(id); //set id in location object
//                        return location;
//                    } catch (Exception e) {
//                        log4jLog.info("createLocationInDB" + e);
//                        return new CacheLocation();
//                    }
//                } else {
//                    return new CacheLocation();
//                }
            }
            if(flag){
            try {
                        String query1 = "SELECT id FROM locations where latitude=? and longitude=?";
                        param = new Object[]{location.getLatitude(),location.getLongitude()};
                        int id=jdbcTemplateCache.queryForObject(query1,param, Integer.class);
                        location.setId(id); //set id in location object
                        return location;
                    } catch (Exception e) {
                        log4jLog.info("createLocationInDB" + e);
//                        e.printStackTrace();
                        return new CacheLocation();
                    }
            }else{
            return new CacheLocation();
            }
        } catch (Exception e) {
            log4jLog.info(" createLocationInDB " + e);
//            e.printStackTrace();
            return new CacheLocation();
        }
    }
    
    /**
     *
     * @param location
     * @return
     */
    @Override
    public int createEntryInStatsDB(CacheLocation location){
        String query = "INSERT INTO locations_stats (location_id_fk,last_used_on,used_count,stats_created_on) VALUES(?,now(),?,now())";
        log4jLog.info(" createEntryInStatsDB " + query);
        Object param[] = new Object[]{location.getId(),1};
        try {
            synchronized (this) {
                if (jdbcTemplateCache.update(query, param) > 0) {
                    
                    return 1;
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            log4jLog.info(" createEntryInStatsDB " + e);
            return 0;
        }
    }    
    
    /**
     *
     * @param location
     * @return
     */
    @Override
    public int updateInfoAfterGetLocation(CacheLocation location){
        String query = "UPDATE locations_stats SET last_used_on=now(),used_count=(used_count+1) WHERE location_id_fk=?";
        log4jLog.info(" updateInfoAfterGetLocation " + query);
        Object[] param = new Object[]{location.getId()};

        try {
            if (jdbcTemplateCache.update(query, param) > 0) {
                return 1;
            } else {
                // means there are no entry with location_id_fk .. so create one
                int res=createEntryInStatsDB(location);
                return res;
            }
        } catch (Exception e) {
            log4jLog.info(" updateTerritoryCategory " + e);
            return 0;
        }
    }
    
    /**
     *
     * @param latitude
     * @param longitude
     * @return
     */
    @Override
    public CacheLocation getShortLocationFromLatLong(double latitude,double longitude){
        String query = "SELECT * FROM short_locations WHERE latitude=? and longitude=?";
        log4jLog.info("getLocationFromLatLong " + query);
        Object[] param = new Object[]{latitude,longitude};
        try {
            return jdbcTemplateCache.queryForObject(query.toString(), param, new RowMapper<CacheLocation>() {

                @Override
                public CacheLocation mapRow(ResultSet rs, int i) throws SQLException {
                    CacheLocation cache = new CacheLocation();
                    cache.setId(rs.getInt("id"));
                    cache.setLatitude(rs.getDouble("latitude"));
                    cache.setLongitude(rs.getDouble("longitude"));
                    cache.setAddress(rs.getString("address"));
                    cache.setCreatedOn(rs.getTimestamp("created_on"));
                    cache.setIsFetchFromGoogle(rs.getInt("is_fetch_google"));
                    //cache.setLastUsedOn(rs.getTimestamp("last_used_on"));
                    //cache.setUsedCount(rs.getInt("used_count"));                    
                    return cache;
                }
            });
        } catch (Exception e) {
            log4jLog.info("getLocationFromLatLong " + e);
//            e.printStackTrace();
            return null;
            //return new CacheLocation();
        }
        
    }
    
    /**
     *
     * @param location
     * @return
     */
    @Override
    public CacheLocation createShortLocationInDB(CacheLocation location){ //Can be optimized ..#siddhesh
        boolean flag=false;
 // Nikhil bhosale : below three lines are foe short address functionality       
//        String query = "INSERT INTO short_locations (latitude,longitude,address,created_on,is_fetch_google,short_add) VALUES(?,?,?,now(),?,?)";
//        log4jLog.info(" createLocationInDB " + query);
//        Object param[] = new Object[]{location.getLatitude(),location.getLongitude(),location.getAddress(),location.getIsFetchFromGoogle(),location.getShort_address()};
         String query = "INSERT INTO short_locations (latitude,longitude,address,created_on,is_fetch_google) VALUES(?,?,?,now(),?)";
        log4jLog.info(" createLocationInDB " + query);
        Object param[] = new Object[]{location.getLatitude(),location.getLongitude(),location.getAddress(),location.getIsFetchFromGoogle()};
        try {
            synchronized (this) {
                flag=jdbcTemplateCache.update(query, param) > 0;
//                if (jdbcTemplateCache.update(query, param) > 0) {
//                    try {
//                        String query1 = "SELECT id FROM short_locations where latitude=? and longitude=?";
//                        param = new Object[]{location.getLatitude(),location.getLongitude()};
//                        int id=jdbcTemplateCache.queryForObject(query1,param, Integer.class);
//                        location.setId(id); //set id in location object
//                        return location;
//                    } catch (Exception e) {
//                        log4jLog.info("createLocationInDB" + e);
//                        return new CacheLocation();
//                    }
//                } else {
//                    return new CacheLocation();
//                }
            }
            
            if(flag){
            try {
                        String query1 = "SELECT id FROM short_locations where latitude=? and longitude=?";
                        param = new Object[]{location.getLatitude(),location.getLongitude()};
                        int id=jdbcTemplateCache.queryForObject(query1,param, Integer.class);
                        location.setId(id); //set id in location object
                        return location;
                    } catch (Exception e) {
                        log4jLog.info("createLocationInDB" + e);
                        e.printStackTrace();
                        return new CacheLocation();
                    }
                }else {
                    return new CacheLocation();
                }
        } catch (Exception e) {
            log4jLog.info(" createLocationInDB " + e);
            e.printStackTrace();
            return new CacheLocation();
        }
    }
    
    /**
     *
     * @param location
     * @return
     */
    @Override
    public int createEntryInShortStatsDB(CacheLocation location){
        String query = "INSERT INTO short_locations_stats (location_id_fk,last_used_on,used_count,stats_created_on) VALUES(?,now(),?,now())";
        log4jLog.info(" createEntryInShortStatsDB " + query);
        Object param[] = new Object[]{location.getId(),1};
        try {
            synchronized (this) {
                if (jdbcTemplateCache.update(query, param) > 0) {
                    
                    return 1;
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            log4jLog.info(" createEntryInShortStatsDB " + e);
            return 0;
        }
    }    
    
    /**
     *
     * @param location
     * @return
     */
    @Override
    public int updateInfoAfterGetLocationInShortStats(CacheLocation location){
        String query = "UPDATE short_locations_stats SET last_used_on=now(),used_count=(used_count+1) WHERE location_id_fk=?";
        log4jLog.info(" updateInfoAfterGetLocationInShortStats " + query);
        Object[] param = new Object[]{location.getId()};

        try {
            if (jdbcTemplateCache.update(query, param) > 0) {
                return 1;
            } else {
                // means there are no entry with location_id_fk .. so create one
                int res=createEntryInStatsDB(location);
                return res;
            }
        } catch (Exception e) {
            log4jLog.info("updateInfoAfterGetLocationInShortStats " + e);
            return 0;
        }
    }
    
    /*Added By jotiba to count request*/

    /**
     *
     * @param location
     * @param source
     * @return
     */
    
   public int createCacheStats(CacheLocation location, int source) {

        try {
            String query = "insert into location_cache_statistics(location_id,created_on,resolved_by,status,error_code,user_id,account_id) values(?,now(),?,?,?,?,?)";
            Object param[] = new Object[]{location.getId(),source,location.getStatus(),location.getErrorCode(),location.getUserId(),location.getAccountId()};
            log4jLog.info("createCacheStats " + query +" : id="+location.getId()+" :source="+source);
            if (jdbcTemplateCache.update(query, param) > 0) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            log4jLog.info("createCacheStats " + e.getMessage());
//             e.printStackTrace();
            return 0;
        }

    }
     /*End Of Added By jotiba to count request*/

    public String getMaxFromLocationCounter() {
        try{
        String query="select created_date from location_counter where id=(select max(id) from location_counter)";
        return jdbcTemplateCache.queryForObject(query, String.class);
        }catch(Exception e){
//        e.printStackTrace();
        return "";
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean updateQueryForLocationCounter(String query, String date) {
        try{ 
          Object param[] = new Object[]{date};  
          if(jdbcTemplateCache.update(query, param)>0){
            return true;
          }else{
            return false;
          }
        }catch(Exception e){
//            e.printStackTrace();
            return false;
        }
    }

    public boolean insertIntolocationCounter(String date,int status ) {
        try{
        String query = "insert into location_counter(count_of_cache,count_of_successfull_google,count_of_unsuccessfull_google,created_date,count_of_mobile) values(?,?,?,?,?)";
        int countCache=0;
        int countGoogleSuccess=0;
        int countGoogleUnSuccess=0;
        int countMobile=0;
        if(status==1){
                 countGoogleSuccess=1;
            }else if(status==0){
                countGoogleUnSuccess=1;
            }else if(status==2){
                countCache=1;
            }else{
                countMobile=1;
            }
        Object param[] = new Object[]{countCache,countGoogleSuccess,countGoogleUnSuccess,date,countMobile};  
        if (jdbcTemplateCache.update(query, param) > 0) {
            return true;
        }else{
            return false;
        }
    }catch(Exception e){
//    e.printStackTrace();
    return false;
    }
    }

    public boolean editTotalInCounter(String maxDate) {
        try{
        String query="select * from location_counter where created_date=?";
        Object param[]=new Object[]{maxDate};
        CacheLocation cacheLocation=new CacheLocation();
        cacheLocation=jdbcTemplateCache.queryForObject(query.toString(), param, new RowMapper<CacheLocation>() {
                @Override
                public CacheLocation mapRow(ResultSet rs, int i) throws SQLException {
                    CacheLocation cache = new CacheLocation();
                    cache.setCountOfCache(rs.getInt("count_of_cache"));
                    cache.setCountOfGoogleSuccess(rs.getInt("count_of_successfull_google"));
                    cache.setCountOfGoogleUnSuccess(rs.getInt("count_of_unsuccessfull_google"));
                    return cache;
                }
            });
        int totalCountServerAddressHits=cacheLocation.getCountOfCache()+cacheLocation.getCountOfGoogleSuccess()+cacheLocation.getCountOfGoogleUnSuccess();
        query="update location_counter set total_count_server_address_hits=? where created_date=?";
        param=new Object[]{totalCountServerAddressHits,maxDate};
        if(jdbcTemplateCache.update(query, param)>0){
         return true;
        }else{
        return false;
        }
        }catch(Exception e){
//        e.printStackTrace();
        return false;
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean addBatchInCounter(String max, int sizeOfArray) {
        try{
     String query="update location_counter set count_of_mobile=count_of_mobile+? where created_date=?";
      Object param[] =new Object[]{sizeOfArray,max};
       if (jdbcTemplateCache.update(query, param) > 0) {
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
