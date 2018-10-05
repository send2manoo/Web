/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.cacheLocation.dao;

import com.qlc.cacheLocation.model.CacheLocation;
import com.qlc.fieldsense.usersTravelLogs.model.UsersTravelLogs;
import java.util.List;

/**
 *
 * @author awaneesh
 */
public interface CacheLocationDao {

    /**
     *
     * @param latitude
     * @param longitude
     * @return
     */
    public CacheLocation getLocationFromLatLong(double latitude,double longitude);
    
    /**
     *
     * @param location
     * @return
     */
    public CacheLocation createLocationInDB(CacheLocation location);
    
    /**
     *
     * @param location
     * @return
     */
    public int createEntryInStatsDB(CacheLocation location);
    
    /**
     *
     * @param location
     * @return
     */
    public int updateInfoAfterGetLocation(CacheLocation location);
    
    /**
     *
     * @param latitude
     * @param longitude
     * @return
     */
    public CacheLocation getShortLocationFromLatLong(double latitude,double longitude);
    
    /**
     *
     * @param location
     * @return
     */
    public CacheLocation createShortLocationInDB(CacheLocation location);
    
    /**
     *
     * @param location
     * @return
     */
    public int createEntryInShortStatsDB(CacheLocation location);
    
    /**
     *
     * @param location
     * @return
     */
    public int updateInfoAfterGetLocationInShortStats(CacheLocation location);
    
    /**
     *
     * @param location
     * @param source
     * @return
     */
    public  int createCacheStats(CacheLocation location,int source);  //    Added By jotiba to Count the requests
    
    
    public String getMaxFromLocationCounter();
    
    
    public boolean updateQueryForLocationCounter(String query,String date);
    
    public boolean insertIntolocationCounter(String date,int status);
    
    public boolean editTotalInCounter(String maxDate);
    
    public boolean addBatchInCounter(String max,int sizeOfArray);
   
}
