/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.cacheLocation.model;

import com.qlc.cacheLocation.dao.*;
import com.qlc.fieldsense.addressConverter.AddressConverter;
import com.qlc.fieldsense.addressConverter.GoogleResponse;
import com.qlc.fieldsense.addressConverter.Result;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.utils.GetApplicationContext;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author awaneesh
 */
public class CacheLocationManager {

    //FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");

    CacheLocationDao cachelocationDao = (CacheLocationDao) GetApplicationContext.ac.getBean("cacheDaoImpl");

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("CacheLocationManager");

    /**
     *
     * @param latitude
     * @param longitude
     * @param userId
     * @param accountId
     * @return
     */
    public ArrayList<String> getLocationFromLatLong(double latitude,double longitude,int userId,int accountId) {
        //latitude = Double.parseDouble(new DecimalFormat("##.######").format(latitude));
        //longitude =Double.parseDouble(new DecimalFormat("##.######").format(longitude));
        
        //We will follow six decimal place after . in latlnog hence convert address 
        if(latitude==0 && longitude==0){
            return new ArrayList<String>();
        }
        int precision=Integer.parseInt(com.qlc.fieldsense.utils.Constant.CACHE_LOCATION_PRECISION);
        Double newLat=truncateDecimal(latitude,precision).doubleValue();
        Double newLong=truncateDecimal(longitude,precision).doubleValue();
        String location ="";
        //CacheLocation cache =cachelocationDao.getLocationFromLatLong(latitude,longitude);
        CacheLocation cache =cachelocationDao.getShortLocationFromLatLong(newLat,newLong);
        ArrayList<String> resultList=new ArrayList<String>();
        String status="2";
        int locationStatsId=0;
        //CacheLocation cache =cachelocationDao.getLocationFromLatLong(newLat,newLong);
        //location =cache.getAddress();     
        
        if(cache==null){
            cache= new CacheLocation();
            cache.setAccountId(accountId);
            cache.setUserId(userId);
            try {
                 GoogleResponse res = new AddressConverter().convertToAddressFromLatLong(String.valueOf(latitude),String.valueOf(longitude));
                // System.out.println("Erroer code"+res.getError_message()+"status code"+res.getStatus());
                 cache.setStatus(res.getStatus());
                 cache.setErrorCode(res.getError_message());
                 if (res.getStatus().equals("OK")) {
                        Result result = res.getResults()[0];
                        location=result.getFormatted_address();
//                         System.out.println("Hello If null "+location);
                        if(location!=null && !location.equals("")){ 
//                            System.out.println("Hello im google. "+location);
                            cache.setAddress(location);
                            cache.setIsFetchFromGoogle(1);
                            //cache.setSourceValue(1);
                            //cache.setUsedCount(1);
                            //cache.setCreatedOn(null);
                            cache.setLatitude(newLat);
                            cache.setLongitude(newLong);
                            cache=cachelocationDao.createShortLocationInDB(cache);
                            if(cache.getId()!=0) // id=0 means object is not created
                            cachelocationDao.createEntryInShortStatsDB(cache); // create entry in stats DB
                            cachelocationDao.createCacheStats(cache,1);// Added by jotiba the value 1 is for the value fetched from Google Api
                            cache.setLatitude(latitude);
                            cache.setLongitude(longitude);
                            cache=cachelocationDao.createLocationInDB(cache);
                            status="1";
                        }
                    }else{
                      //   System.out.println("Google Error Message is:"+ (new Date()).toString()+" : "+res.getError_message());
                      // Added by jotiba the value 0 is for the  data if not getting anything from google or from the Cache Database.
                      cachelocationDao.createCacheStats(cache,0);
                      status="0";
                      //End By Jotiba
                 }
                } catch (Exception ex) {
                    ex.printStackTrace();
                   //cache.setStatus("Invalid lat long");
                    status="0";
                   cachelocationDao.createCacheStats(cache,0);
                } 
        }else{
            //cache.setUsedCount(cache.getUsedCount()+1);
             cache.setAccountId(accountId);
             cache.setUserId(userId);
              cachelocationDao.updateInfoAfterGetLocationInShortStats(cache);
               // Added by jotiba the value 2 is for the fetching data from Cache Database
              cachelocationDao.createCacheStats(cache,2);
              status="2";
              //End By Jotiba           
        }
        
        location=cache.getAddress();
//        System.out.println("Hello dqjqjdo "+location);
        resultList.add(location);
        resultList.add(status);
        return resultList;
        //return location;
        //return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " account list ", accountList);
    }
    private static BigDecimal truncateDecimal(double x, int numberofDecimals) {
        if (x > 0) {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_FLOOR);
        } else {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_CEILING);
        }
    }   
    
    public boolean insertDataInStats(Timestamp timeStamp,int status,int sizeOfArray){
        try {
            String query="";
            String timeStampString=timeStamp.toString();
//            System.out.println("Hiiii"+timeStampString);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
            Date d1 = sdf1.parse(timeStampString);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateWithoutTime = sdf.format(d1);
//            System.out.println("sdf.format(d) " + dateWithoutTime);
            String maxDate=cachelocationDao.getMaxFromLocationCounter();
//            System.out.println("maxDate"+maxDate);
            if(dateWithoutTime.equals(maxDate)){
                if(status==0){
                    query="update location_counter set count_of_unsuccessfull_google=count_of_unsuccessfull_google+1 where created_date=?";
                }else if(status==1){
                    query="update location_counter set count_of_successfull_google=count_of_successfull_google+1 where created_date=?";
                }else if(status==2){
                    query="update location_counter set count_of_cache=count_of_cache+1 where created_date=?";
                }else if(status==4){
                    cachelocationDao.addBatchInCounter(maxDate, status);
                }else{
                    query="update location_counter set count_of_mobile=count_of_mobile+1 where created_date=?";
                }
            cachelocationDao.updateQueryForLocationCounter(query, maxDate);
            }else{
//               System.out.println("Siddhesh hello");
               cachelocationDao.editTotalInCounter(maxDate);
//                 System.out.println("Siddhesh hello for insert");
               cachelocationDao.insertIntolocationCounter(dateWithoutTime,status);
            }
            return true;
            }catch (Exception ex) {
                java.util.logging.Logger.getLogger(CacheLocationManager.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
                return false;
            }
    }
    
    public String getLocationFromLatLongSats(double latitude,double longitude,int userId,int accountId) {
        ArrayList<String> listOfResultSet=new ArrayList<String>();
        listOfResultSet=this.getLocationFromLatLong(latitude, longitude, userId, accountId);
        Timestamp timeStamp=new Timestamp(System.currentTimeMillis());
        String address=listOfResultSet.get(0);
        int status=Integer.parseInt(listOfResultSet.get(1));
        this.insertDataInStats(timeStamp, status,0);
        return address;    
    }
}
