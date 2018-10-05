/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.scheduler.model;

import com.qlc.cacheLocation.model.CacheLocationManager;
import com.qlc.fieldsense.scheduler.dao.SchedulerDao;
import com.qlc.fieldsense.usersTravelLogs.dao.UsersTravelLogsDao;
import com.qlc.fieldsense.usersTravelLogs.model.UsersTravelLogs;
import com.qlc.fieldsense.utils.GetApplicationContext;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author siddhesh
 */
public class SchedulerManager {
    
    SchedulerDao schedulerDao = (SchedulerDao) GetApplicationContext.ac.getBean("SchedulerDaoImpl");
     UsersTravelLogsDao usersTravelLogsDao = (UsersTravelLogsDao) GetApplicationContext.ac.getBean("usersTravelLogsDaoImpl");
    
    public void InsertTravelLog(){
//        try{
//             ArrayList<String> listOfResultSetData=new ArrayList<String>();
//             System.err.println("hello please baby");
//             List<UsersTravelLogs> listOfUnresolvedAddress=new ArrayList<UsersTravelLogs>();
//             List<Integer> listOfTravelLogsId=new ArrayList<Integer>();
//            synchronized(this){
//                listOfUnresolvedAddress=schedulerDao.getListOfDataFromLocationNotFound();
//                 System.err.println("Hi this is new"+listOfUnresolvedAddress.size());
//                 }
//                if(listOfUnresolvedAddress.isEmpty()){
////                    schedulerDao=null;
////                    usersTravelLogsDao=null;
//                    return;
//                }
//                for(int i=0;i<listOfUnresolvedAddress.size();i++){
//                    try{
//                        System.err.println("Hi this is new");
//                    UsersTravelLogs usersTravelLogs=listOfUnresolvedAddress.get(i);
//                    CacheLocationManager cacheLocation=new CacheLocationManager();
//                    listOfResultSetData=cacheLocation.getLocationFromLatLong(usersTravelLogs.getLatitude(), usersTravelLogs.getLangitude(), usersTravelLogs.getUserId(), usersTravelLogs.getAccountId());
//                    String address=listOfResultSetData.get(0);
//                    usersTravelLogs.setLocation(address);
//                    if(usersTravelLogsDao.insertIntoTravelLogForUnresolvedAddress(usersTravelLogs)){
//                    listOfTravelLogsId.add(usersTravelLogs.getId());
//                    }
//                    }catch(Exception e){
//                    e.printStackTrace();
//                     System.err.println("Hi this is error in first try ");
//                    }
//                }
//                schedulerDao.deletelocationNotFoundEntires(listOfTravelLogsId);
//                return;
//        }catch(Exception e){
//            e.printStackTrace();
//             System.err.println("Hi this is error in second try ");
//        }
    }
    
    
    
}
