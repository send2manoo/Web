/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customerOffline.model;


import com.qlc.cacheLocation.dao.CacheLocationDao;
import com.qlc.cacheLocation.model.CacheLocationManager;
import com.qlc.fieldsense.appointments.dao.AppointmentDao;
import com.qlc.fieldsense.appointments.model.Appointment;
import com.qlc.fieldsense.auth.model.AuthenticationUserManager;
import com.qlc.fieldsense.customer.dao.customerDao;
import com.qlc.fieldsense.customer.model.Customer;
import com.qlc.fieldsense.customerContact.dao.CustomerContactsDao;
import com.qlc.fieldsense.customerOffline.dao.CustomerOfflineDao;
import com.qlc.fieldsense.expense.model.Expense;
import com.qlc.fieldsense.formbuilder.model.CustomFormManager;
import com.qlc.fieldsense.location.dao.LocationDao;
import com.qlc.fieldsense.territoryCategory.dao.TerritoryCategoryDao;
import com.qlc.fieldsense.industryCategory.dao.IndustryCategoryDao;
import com.qlc.fieldsense.location.model.Location;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.userKey.dao.UserKayDaoImpl;
import com.qlc.fieldsense.userKey.model.UserKey;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.apache.log4j.Logger;

/**
 *
 * @author jyoti
 */
public class CustomerOfflineManager {
    
    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("CustomerOfflineManager");
    CustomerOfflineDao customerOfflineDao = (CustomerOfflineDao) GetApplicationContext.ac.getBean("customerOfflineDaoImpl");
    FieldSenseUtils util = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
    LocationDao locationDao = (LocationDao) GetApplicationContext.ac.getBean("locationDaoImpl");
    IndustryCategoryDao industryCategoryDao = (IndustryCategoryDao) GetApplicationContext.ac.getBean("industryCategoryDaoImpl");
    TerritoryCategoryDao territoryCategoryDao = (TerritoryCategoryDao) GetApplicationContext.ac.getBean("territoryCategoryDaoImpl");
    CustomerContactsDao customerContactsDao = (CustomerContactsDao) GetApplicationContext.ac.getBean("customerContactsDaoImpl");
    customerDao customerDao = (customerDao) GetApplicationContext.ac.getBean("customerDaoImpl");
      UserKayDaoImpl userKeyDao = (UserKayDaoImpl) GetApplicationContext.ac.getBean("userKayDaoImpl");
   AppointmentDao appointmentDao = (AppointmentDao) GetApplicationContext.ac.getBean("appointmentDaoImpl");
   CacheLocationDao cachelocationDao = (CacheLocationDao) GetApplicationContext.ac.getBean("cacheDaoImpl");
   CacheLocationManager cacheManager = new CacheLocationManager();
    /**
     * @Jyoti 24-12-2016
     * @param syncDateTime (in milliseconds)
     * @param userToken
     * @return list of after Last Sync customers with its details
     */
//    public Object getAfterLastSyncCustomersOffline(String lastSyncDateTime ,String token) {
//        if (util.isTokenValid(token)) {            
//                int accountId = util.accountIdForToken(token);
//                int userId = util.userIdForToken(token);
//                List<CustomerOfflineWithFewData> customerOfflineList = new ArrayList<CustomerOfflineWithFewData>();
//                
//            try{
//                long lastSyncMilliSeconds = Long.parseLong(lastSyncDateTime);   // date time in milliseconds                 
//                Date lastSyncDate = new Date(lastSyncMilliSeconds);            //creating Date from millisecond
//                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");                 
//                Timestamp lastSyncTimestamp = Timestamp.valueOf(df.format(lastSyncDate));  
//                customerOfflineList = customerOfflineDao.selectCustomersOfflineAfterLastSync(accountId, userId, lastSyncTimestamp); 
//            }
//            catch(Exception e){
//                e.printStackTrace();
//                return  FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "INVALID DATE FORMAT", "", "");
//            }
//                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " afterLastSyncCustomerOfflineList ", customerOfflineList);   
//        } else {
//            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
//        }
//    }
    
    /**
     * @Jyoti 11-01-2017
     * @param customerOfflineList (List of id's)
     * @param token
     * @return list of after Last Sync customers with its details
     */
    public Object getRequestedListOfCustomersOffline(CustomerOfflineWithFewData customerOfflineList, String token) {
        if (util.isTokenValid(token)) {
                List<CustomerOfflineWithFewData> customerOfflineListWithFewData = new ArrayList<CustomerOfflineWithFewData>();                
                try{       
                    int accountId = util.accountIdForToken(token);
                    int userId = util.userIdForToken(token); 
                    
                    //  for log check purpose, remove later
//                    long startTime = System.currentTimeMillis();
//                    long elapsedTime = System.currentTimeMillis() - startTime;
//                    System.out.println("sending request ..");
//                    System.out.println("seconds Before response : " + elapsedTime);
                    //
                    
                    List<CustomersOfflineRequestedList> listOfCustomerID = customerOfflineList.getCustomersOfflineRequestedList();
                    customerOfflineListWithFewData =customerOfflineDao.selectRequestedListOfCustomersOffline(accountId, userId, listOfCustomerID);
                } 
                catch(Exception e){
                    log4jLog.info("getRequestedListOfCustomersOffline : " + e);
                    return  FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "INVALID DATA", "", "");
                }
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " requestedListOfCustomerOffline ", customerOfflineListWithFewData);   
        }  else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }
    
    
     /**
     * @author Siddhesh
     * @return 
     * @date 28-12-2016
     * @param offlineDataSync
     * @param userToken
     * @purpose If location is not available for customer then insert the
     * location for customer if available nothing to do .
     */
    public Object insertCustomerOfflineData(OfflineDataSync offlineDataSync, String userToken) {
       // log4jLog.info("inside insertCustomerLocation in customerManger");\
        OfflineDataSync syncData=new OfflineDataSync();
        if (util.isTokenValid(userToken)) {
           
                int accountId = util.accountIdForToken(userToken);
               // int userId = util.userIdForToken(userToken);
                if(customerOfflineDao.insertOfflineDataCustomer(offlineDataSync, accountId))
                {
                  if(customerOfflineDao.insertOfflineDataAppointment(offlineDataSync, accountId))
                  {
                      syncData.setCustomer(customerOfflineDao.getAllOfflineCustomers(offlineDataSync, accountId));
                      syncData.setAppointments(customerOfflineDao.getAllOfflineAppointments(offlineDataSync, accountId));
                      
                      return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " data ", "",syncData);
                  }else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Appointment not inserted. ", "", "");
        }
                }else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Customer not inserted . ", "", "");
        }
                
                
           
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
  
    
}
    
    /**
     *
     * @param offlineDataSync
     * @param userToken
     * @return
     */
    public Object insertCustomerData(OfflineDataSync offlineDataSync, String userToken) {
       // log4jLog.info("inside insertCustomerLocation in customerManger");\
        OfflineDataSync syncData=new OfflineDataSync();
        if (util.isTokenValid(userToken)) {
                 List<HashMap<String,String>> customerInfo;  
                int accountId = util.accountIdForToken(userToken);
                int userId = util.userIdForToken(userToken);
            customerInfo=customerOfflineDao.getAllOfflineData(offlineDataSync, accountId, userId);
                
                 return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "  ", "Customer List",customerInfo);
           
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
  
   
}
        /**
     * @author Siddhesh
     * @return 
     * @date 28-12-2016
     * @param offlineDataSync
     * @param userToken
     * @purpose If location is not available for customer then insert the
     * location for customer if available nothing to do .
     */
     public Object insertCustomerData1(OfflineDataSync offlineDataSync, String userToken) {
    
         if (util.isTokenValid(userToken)) {
        
            List<HashMap<String,String>> customerInfo=new ArrayList<HashMap<String, String>>();  
            int accountId = util.accountIdForToken(userToken);                
            int userId = util.userIdForToken(userToken);         
            List<LocalData> offlineDataList=offlineDataSync.getLocalData();
            int customerId=0;
            boolean flag=false;
            boolean isCheckInCheckOutNull=false;
            String mobileCustomerId="";
            boolean status=false;
            String customerName;
            int appointmentId=0;
            String mobileAppointmentId="0";
            int expenseId=0;
            String mobileExpenseId="0";
            boolean lastSyncZeroFlag=false;
            int recordState=0;
            Timestamp lastmodifiedDate;
            String currentDateTime=offlineDataSync.getCurrentTime();      //Current time of the mobile when the sync button is pressed in string
            long currentMilliSeconds = Long.parseLong(currentDateTime);   // date time in milliseconds                 
            Date currentDate = new Date(currentMilliSeconds);            //creating Date from millisecond
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     
            df1.setTimeZone(TimeZone.getTimeZone("GMT"));
            Timestamp currentTimestamp = Timestamp.valueOf(df1.format(currentDate));// timestamp of current time
            for(int i=0;i<offlineDataList.size();i++){      //Iterating the main for loop which contains customer ,ArrayList of appointments,expenses.
            try{
                LocalData offlineDataObj=offlineDataList.get(i); //Selecting one object from the list
                mobileCustomerId=offlineDataObj.getId();          //Retriving the mobileTempId
                customerName=offlineDataObj.getCustomerName();         
                recordState=offlineDataObj.getRecordState();
                if(offlineDataObj.getRecordState()==1 || offlineDataObj.getId().contains("TMP")){   //Checking the status of the data is for insert or update like wise
                    customerId=customerOfflineDao.isTempIdPresentForCustomer(mobileCustomerId, accountId); //Checking for the temp id present or not.If present then it wont be inserted And if it is present then will give the coresponding database id.
                    if(customerId==0){
                    if (!customerDao.isCustomerExistWithLocation(offlineDataObj.getCustomerName(), offlineDataObj.getCustomerLocation(), accountId)){
                        if(customerDao.isTerritoryExists(offlineDataObj.getTerritory().trim(),accountId)){
                            if(offlineDataObj.getCustomerAddress1()==null || offlineDataObj.getCustomerAddress1().equals(" ") ||offlineDataObj.getCustomerAddress1().equals("")){
                                if(offlineDataObj.getLasknownLangitude()!=0 || offlineDataObj.getLasknownLangitude()!=0){
                                    //System.out.println("wjmfowjowjdo"+offlineDataObj.getLasknownLangitude()+offlineDataObj.getLasknownLatitude());
                                 offlineDataObj.setCustomerAddress1(cacheManager.getLocationFromLatLongSats(offlineDataObj.getLasknownLatitude(),offlineDataObj.getLasknownLangitude(),0,0));
                                }else{
                            HashMap<String,String> customerListMap=new HashMap<String,String>();
                            customerListMap.put("mobileId",mobileCustomerId);
                            customerListMap.put("id", Integer.toString(customerId));
                            customerListMap.put("Status","99");
                            customerInfo.add(customerListMap);
                                }
                            }
                        customerId=customerOfflineDao.insertCustomers(offlineDataObj, accountId, userId);
                        //
                        if (offlineDataObj.getLasknownLatitude() != 0 || offlineDataObj.getLasknownLangitude() != 0) {
                                Location location = new Location();
                                location.setLatitude(offlineDataObj.getLasknownLatitude());
                                location.setLangitude(offlineDataObj.getLasknownLangitude());
                                boolean isLocationAvailable = false;
                                if (offlineDataObj.getCustomerType() == 1) {
                                    location.setLocationType(customerId);
                                } else {
                                    location.setLocationType(offlineDataObj.getCustomerType());
                                }
                                location.setUserId(userId);
                                isLocationAvailable = locationDao.isLocationAvailable(location, accountId);
                                if (isLocationAvailable) {
                                    locationDao.updateUserLocation(location, accountId);
                                } else {
                                    locationDao.insertlocation(location, accountId);
                                }
                        }
                        //
                        log4jLog.info("Customer Inserted:-"+customerId+"userId "+userId);
                          //  System.out.println("Customer Inserted:-"+customerId);
                        HashMap<String,String> customerListMap=new HashMap<String, String>();
                        customerListMap.put("mobileId",mobileCustomerId);
                        customerListMap.put("id", Integer.toString(customerId));
                        customerListMap.put("Status","1");
                        customerInfo.add(customerListMap);
                        }else{
                            HashMap<String,String> customerListMap=new HashMap<String, String>();
                            customerListMap.put("mobileId",mobileCustomerId);
                            customerListMap.put("id", Integer.toString(customerId));
                            customerListMap.put("Status","99");
                            customerInfo.add(customerListMap);
                        }
                    }else{
                            HashMap<String,String> customerListMap=new HashMap<String, String>();
                            customerListMap.put("mobileId",mobileCustomerId);
                            customerListMap.put("id", Integer.toString(customerId));
                            customerListMap.put("Status","99");
                            customerInfo.add(customerListMap);
                    }
                }
                }else if(offlineDataObj.getRecordState()==2 && !offlineDataObj.getId().contains("TMP")){
                    customerId=Integer.parseInt(offlineDataObj.getId());
                    lastmodifiedDate=customerOfflineDao.checkCustomerLastUpdateTimeStamp(offlineDataObj, accountId, userId, customerId);
                    String modifiedOn=offlineDataObj.getModifiedOnMobile();
                    long modifiedOnMilliSeconds = Long.parseLong(modifiedOn);   // date time in milliseconds                 
                    Date modifiedOnDate = new Date(modifiedOnMilliSeconds);  
                  //  df1.setTimeZone(TimeZone.getTimeZone("GMT"));//creating Date from millisecond
                    Timestamp modifiedOnTimestamp = Timestamp.valueOf(df1.format(modifiedOnDate));
                    modifiedOnTimestamp=util.converDateToTimestamp(modifiedOnTimestamp.toString());
                        if(lastmodifiedDate.before(modifiedOnTimestamp)){
                            recordState=customerOfflineDao.getRecordStatusForCustomer(accountId, customerId);
                                if(recordState!=3){
                                    if(offlineDataObj.getCustomerAddress1()==null || offlineDataObj.getCustomerAddress1().equals(" ") ||offlineDataObj.getCustomerAddress1().equals("")){
                                        if(offlineDataObj.getLasknownLangitude()!=0 || offlineDataObj.getLasknownLangitude()!=0){                                    
                                            offlineDataObj.setCustomerAddress1(cacheManager.getLocationFromLatLongSats(offlineDataObj.getLasknownLatitude(),offlineDataObj.getLasknownLangitude(),0,0));
                                        }else{
                                                HashMap<String,String> customerListMap=new HashMap<String,String>();
                                                customerListMap.put("mobileId",mobileCustomerId);
                                                customerListMap.put("id", Integer.toString(customerId));
                                                customerListMap.put("Status","99");
                                                customerInfo.add(customerListMap);
                                        }
                                    }
                                    if(customerOfflineDao.updateCustomers(offlineDataObj, accountId, userId, customerId)){
                                        Location location = new Location();
                                        location.setLatitude(offlineDataObj.getLasknownLatitude());
                                        location.setLangitude(offlineDataObj.getLasknownLangitude());
                                        location.setLocationType(customerId);
                                        location.setUserId(userId);
                                        boolean isLocationAvailable = locationDao.isLocationAvailableOnlyWithLocationTypeId(customerId, accountId);
                                        if (isLocationAvailable) {
                                            locationDao.updateUserLocationOnlyWithLocationTypeId(location, accountId);
                                        } else {
                                            locationDao.insertlocation(location, accountId);
                                        }
                                       // System.out.println("Customer Upadetd"+customerId);
                                        log4jLog.info("Customer Upadeted:-"+customerId+"userId "+userId);
                                        HashMap<String,String> customerListMap=new HashMap<String, String>();
                                        customerListMap.put("mobileId","0");
                                        customerListMap.put("id", Integer.toString(customerId));
                                        customerListMap.put("Status","2"); 
                                        customerInfo.add(customerListMap);

                                    }else{
                                        HashMap<String,String> customerListMap=new HashMap<String,String>();
                                                customerListMap.put("mobileId",mobileCustomerId);
                                                customerListMap.put("id", Integer.toString(customerId));
                                                customerListMap.put("Status","99");
                                                customerInfo.add(customerListMap);
                                    }
                                }
                        }else{
                                HashMap<String,String> customerListMap=new HashMap<String, String>();
                                customerListMap.put("mobileId","0");
                                customerListMap.put("id", Integer.toString(customerId));
                                customerListMap.put("Status","2");
                                customerInfo.add(customerListMap);
                }
                }else if(offlineDataObj.getRecordState()==0 && !offlineDataObj.getId().contains("TMP")){
                        flag=true;
                        customerId=1;
                }
                else{
                        customerId=Integer.parseInt(offlineDataObj.getId());
//                        System.err.println("wdfnwilwndiwndioji"+customerId);
                }
                if(customerId!=0){
                    List<AppointmentOffline> appointmentOfflinesList=offlineDataObj.getAppointments();
                        if(!(appointmentOfflinesList.isEmpty())){   
                            for(int j=0;j<appointmentOfflinesList.size();j++){
                                AppointmentOffline appointmentOfflineObj=appointmentOfflinesList.get(j);
                                mobileAppointmentId=appointmentOfflineObj.getId();
                                if (appointmentOfflineObj.getScheckInTime()!=null && !appointmentOfflineObj.getScheckInTime().equals("")) {
                                    appointmentOfflineObj.setCheckInTime(util.converDateToTimestamp(appointmentOfflineObj.getScheckInTime()));
                                    isCheckInCheckOutNull=true;
                                }
                                if (appointmentOfflineObj.getScheckOutTime()!=null && !appointmentOfflineObj.getScheckOutTime().equals("")) {
                                    appointmentOfflineObj.setCheckOutTime(util.converDateToTimestamp(appointmentOfflineObj.getScheckOutTime()));
                                    isCheckInCheckOutNull=true;
                                }
                                if(appointmentOfflineObj.getRecordState()==1 ||appointmentOfflineObj.getId().contains("TMP")){
                                    if(appointmentOfflineObj.getRecordState()==1){
                                    appointmentId=customerOfflineDao.isTempIdPresentForAppointment(mobileAppointmentId, accountId);
                                    if(appointmentId==0){
                                        appointmentOfflineObj.setDateTime(util.converDateToTimestamp(appointmentOfflineObj.getSdateTime()));
                                        appointmentOfflineObj.setEndTime(util.converDateToTimestamp(appointmentOfflineObj.getSendTime()));
                                        if(flag==true){
                                        customerId=Integer.parseInt(appointmentOfflineObj.getCustomer().getId());
                                        }
                                        appointmentId=customerOfflineDao.insertAppointments(appointmentOfflineObj, accountId, userId, customerId);
                                       // System.out.println("Appointment Inserted"+appointmentId);
                                        log4jLog.info("Appointment Inserted:-"+appointmentId+"userId "+userId);
                                        
                                    }
                                }else if(appointmentOfflineObj.getRecordState()==2){
                                    appointmentId=customerOfflineDao.isTempIdPresentForAppointment(mobileAppointmentId, accountId);
                                    appointmentOfflineObj.setDateTime(util.converDateToTimestamp(appointmentOfflineObj.getSdateTime()));
                                        appointmentOfflineObj.setEndTime(util.converDateToTimestamp(appointmentOfflineObj.getSendTime()));
                                        if(flag==true){
                                        customerId=Integer.parseInt(appointmentOfflineObj.getCustomer().getId());
                                        }
                                        if(appointmentOfflineObj.getNextAppointmentStr().contains("TMP")){
                                            appointmentOfflineObj.setNextAppointment(customerOfflineDao.isTempIdPresentForAppointment(appointmentOfflineObj.getNextAppointmentStr(), accountId));
                                        }
                                            if(isCheckInCheckOutNull==false){
                                                status=customerOfflineDao.updateAppointment(appointmentOfflineObj, accountId, userId, customerId,appointmentId);
                                                //System.out.println("CheckIn CheckOut NULL"+appointmentId);
                                                try{
                                                    
                                                    if(appointmentOfflineObj.getStatus()==1){
                                                            UserKey userKey=new UserKey();
                                                            userKey.setKeyValue("1");
                                                            User user1=new User();
                                                            user1.setId(appointmentOfflineObj.getAssignedTo().getId());
                                                            userKey.setUserId(user1);
                                                            userKey.setUserKay("InMeeting");
                                                            userKeyDao.updateUserKeys(userKey, accountId);
                                                    }else if(appointmentOfflineObj.getStatus()==2){
                                                            UserKey userKey=new UserKey();
                                                            userKey.setKeyValue("0");
                                                            User user1=new User();
                                                            user1.setId(appointmentOfflineObj.getAssignedTo().getId());
                                                            userKey.setUserId(user1);
                                                            userKey.setUserKay("InMeeting");
                                                            userKeyDao.updateUserKeys(userKey, accountId);
                                                    }
                                                }catch(Exception e){
//                                                e.printStackTrace();
                                                    log4jLog.info(" insertCustomerData1 " + e);
                                                }
                                                
                                                log4jLog.info("CheckIn CheckOut NULL:-"+appointmentId+"userId "+userId);
                                            }else{
                                               status=customerOfflineDao.updateAppointmentForCheckInCheckOut(appointmentOfflineObj, accountId, userId, customerId,appointmentId);
                                               //System.out.println("CheckIn CheckOut NOT NULL"+appointmentId);
                                                if(status){
                                                try{
                                                    
                                                    if(appointmentOfflineObj.getStatus()==1){
                                                        UserKey userKey=new UserKey();
                                                            userKey.setKeyValue("1");
                                                            User user1=new User();
                                                            user1.setId(appointmentOfflineObj.getAssignedTo().getId());
                                                            userKey.setUserId(user1);
                                                            userKey.setUserKay("InMeeting");
                                                            userKeyDao.updateUserKeys(userKey, accountId);
                                                        }else if(appointmentOfflineObj.getStatus()==2){
                                                            UserKey userKey=new UserKey();
                                                            userKey.setKeyValue("0");
                                                            User user1=new User();
                                                            user1.setId(appointmentOfflineObj.getAssignedTo().getId());
                                                            userKey.setUserId(user1);
                                                            userKey.setUserKay("InMeeting");
                                                            userKeyDao.updateUserKeys(userKey, accountId);
                                                        }
                                                    }catch(Exception e){
//                                                         e.printStackTrace();
                                                        log4jLog.info(" insertCustomerData1 " + e);
                                                    }
                                                }
                                               log4jLog.info("CheckIn CheckOut NOT NULL:-"+appointmentId+"userId "+userId);
                                            }
                                            if(status){
                                                log4jLog.info("Appointment Updated through Insert:-"+appointmentId+"userId "+userId);
                                                //System.out.println("Appointment Updated through Insert"+appointmentId);
                                            }else{
                                                  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Appointment Update failed", "", ""); // change to be made in case of error
                                            }
                                    }
                                }else if(appointmentOfflineObj.getRecordState()==2 && !appointmentOfflineObj.getId().contains("TMP")){
                                        appointmentId=Integer.parseInt(mobileAppointmentId);
                                        appointmentOfflineObj.setDateTime(util.converDateToTimestamp(appointmentOfflineObj.getSdateTime()));
                                        appointmentOfflineObj.setEndTime(util.converDateToTimestamp(appointmentOfflineObj.getSendTime()));
                                        if(flag==true){
                                        customerId=Integer.parseInt(appointmentOfflineObj.getCustomer().getId());
                                        }
                                        if(appointmentOfflineObj.getNextAppointmentStr().contains("TMP")){
                                            appointmentOfflineObj.setNextAppointment(customerOfflineDao.isTempIdPresentForAppointment(appointmentOfflineObj.getNextAppointmentStr(), accountId));
                                        }
                                           if(isCheckInCheckOutNull==false){
                                                status=customerOfflineDao.updateAppointment(appointmentOfflineObj, accountId, userId, customerId,appointmentId);
                                             //   System.out.println("CheckIn CheckOut NULL"+appointmentId);
                                                try{
                                                    
                                                    if(appointmentOfflineObj.getStatus()==1){
                                                            UserKey userKey=new UserKey();
                                                            userKey.setKeyValue("1");
                                                            User user1=new User();
                                                            user1.setId(appointmentOfflineObj.getAssignedTo().getId());
                                                            userKey.setUserId(user1);
                                                            userKey.setUserKay("InMeeting");
                                                            userKeyDao.updateUserKeys(userKey, accountId);
                                                    }else if(appointmentOfflineObj.getStatus()==2){
                                                            UserKey userKey=new UserKey();
                                                            userKey.setKeyValue("0");
                                                            User user1=new User();
                                                            user1.setId(appointmentOfflineObj.getAssignedTo().getId());
                                                            userKey.setUserId(user1);
                                                            userKey.setUserKay("InMeeting");
                                                            userKeyDao.updateUserKeys(userKey, accountId);
                                                    }
                                                }catch(Exception e){
//                                                e.printStackTrace();
                                                    log4jLog.info(" insertCustomerData1 " + e);
                                                }
                                                log4jLog.info("CheckIn CheckOut NULL:-"+appointmentId+"userId "+userId);
                                            }else{
                                               status=customerOfflineDao.updateAppointmentForCheckInCheckOut(appointmentOfflineObj, accountId, userId, customerId,appointmentId);
//                                            //   System.out.println("CheckIn CheckOut NOT NULL"+appointmentId);
                                               try{
                                                    
                                                    if(appointmentOfflineObj.getStatus()==1){
                                                            UserKey userKey=new UserKey();
                                                            userKey.setKeyValue("1");
                                                            User user1=new User();
                                                            user1.setId(appointmentOfflineObj.getAssignedTo().getId());
                                                            userKey.setUserId(user1);
                                                            userKey.setUserKay("InMeeting");
                                                            userKeyDao.updateUserKeys(userKey, accountId);
                                                    }else if(appointmentOfflineObj.getStatus()==2){
                                                            UserKey userKey=new UserKey();
                                                            userKey.setKeyValue("0");
                                                            User user1=new User();
                                                            user1.setId(appointmentOfflineObj.getAssignedTo().getId());
                                                            userKey.setUserId(user1);
                                                            userKey.setUserKay("InMeeting");
                                                            userKeyDao.updateUserKeys(userKey, accountId);
                                                    }
                                                }catch(Exception e){
//                                                e.printStackTrace();
                                                    log4jLog.info(" insertCustomerData1 " + e);
                                                }
                                               log4jLog.info("CheckIn CheckOut NOT NULL:-"+appointmentId+"userId "+userId);
                                            }
                                            if(status){
                                               // System.out.println("Appointment Updated"+appointmentId);
                                                log4jLog.info("Appointment Updated:-"+appointmentId+"userId "+userId);
                                            }else{
//                                                  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Appointment Update failed", "", ""); // change to be made in case of error
                                                HashMap<String,String> customerListMap=new HashMap<String, String>();
                                                customerListMap.put("mobileId","0");
                                                customerListMap.put("id", Integer.toString(customerId));
                                                customerListMap.put("Status","99");
                                                customerInfo.add(customerListMap);
                                                
                                            }
                                }else if(appointmentOfflineObj.getRecordState()==3 && !appointmentOfflineObj.getId().contains("TMP")){
                                           appointmentId=Integer.parseInt(mobileAppointmentId);
                                           appointmentDao.deleteAppointment(appointmentId, accountId);
                                          // System.out.println("Appointment Deleted"+appointmentId);
                                           log4jLog.info("Appointment Deleted:-"+appointmentId+"userId "+userId);
                                }
                                if(appointmentId!=0){
                                    List<Expense> expensesList=offlineDataObj.getExpense();
                                        if(!(expensesList.isEmpty())){
                                            for(int k=0;k<expensesList.size();k++){
                                                  Expense expenseObj=expensesList.get(k);
                                                  mobileExpenseId=expenseObj.getExpenseTempId();
                                                  expenseId=customerOfflineDao.isTempIdPresentForExpense(mobileExpenseId, accountId);
                                                  if(expenseId==0){
                                                    if(expenseObj.getAppointmentTempId().equalsIgnoreCase(mobileAppointmentId)){
                                                        if(flag==true){
                                                        customerId=Integer.parseInt(expenseObj.getCustomerTempId());
                                                        }
                                                        expenseId=customerOfflineDao.insertExpense(expenseObj, accountId, userId, customerId, appointmentId, mobileAppointmentId);
                                                        //System.out.println("Expense Inserted"+expenseId+"For Customer"+customerId+"And appointment"+appointmentId);
                                                        log4jLog.info("Expense Inserted"+expenseId+"For Customer"+customerId+"And appointment"+appointmentId+"UserID "+userId);
                                                    }
                                                  }
                                            }
                                        }
                                }else{
                                     // return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Appointment not inserted", "", ""); //change 
                                    HashMap<String,String> customerListMap=new HashMap<String, String>();
                                                customerListMap.put("mobileId","0");
                                                customerListMap.put("id", Integer.toString(customerId));
                                                customerListMap.put("Status","99");
                                                customerInfo.add(customerListMap);
                                }
                            }
                        }
                             List<Expense> expensesList=offlineDataObj.getExpense();
                            if(!(expensesList.isEmpty())){
                                for(int k=0;k<expensesList.size();k++){
                                    Expense expenseObj=expensesList.get(k);
                                   mobileExpenseId=expenseObj.getExpenseTempId();
                                    expenseId=customerOfflineDao.isTempIdPresentForExpense(mobileExpenseId, accountId);
                                    if(expenseId==0){
                                       if(expenseObj.getAppointmentTempId().equalsIgnoreCase("0")){
                                            customerId=expenseObj.getCustomerId();
                                            appointmentId=expenseObj.getAppointmentId();
                                            mobileAppointmentId="0";
                                            expenseId=customerOfflineDao.insertExpense(expenseObj, accountId, userId, customerId, appointmentId, mobileAppointmentId);
                                           // System.out.println("Expense Inserted"+expenseId);
                                            log4jLog.info("Expense Inserted"+expenseId+"UserID "+userId);
                                        }
                                    }
                                }
                            }
                        }else{
                     HashMap<String,String> customerListMap=new HashMap<String, String>();
                     customerListMap.put("mobileId",mobileCustomerId);
                     customerListMap.put("id", Integer.toString(customerId));
                     customerListMap.put("Status","99");
                     customerInfo.add(customerListMap);
                  //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Customer not inserted", "", "");
                }
           
            }catch(Exception e ){
                HashMap<String,String> customerListMap=new HashMap<String, String>();
                customerListMap.put("mobileId",mobileCustomerId);
                customerListMap.put("id", Integer.toString(customerId));
                customerListMap.put("Status","99");
                customerInfo.add(customerListMap);
               // e.printStackTrace();
                log4jLog.info(" insertCustomerData1 " + e);
               
            }
           //  }
        }
       // }             
        String lastSyncDateTime=offlineDataSync.getLastSyncTime();
        if(lastSyncDateTime.equals("0")){
        lastSyncZeroFlag=true;
        }
        long lastSyncMilliSeconds = Long.parseLong(lastSyncDateTime);   // date time in milliseconds                 
        Date lastSyncDate = new Date(lastSyncMilliSeconds);            //creating Date from millisecond
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        df.setTimeZone(TimeZone.getTimeZone("GMT")); 
        Timestamp lastSyncTimestamp = Timestamp.valueOf(df.format(lastSyncDate));
        //System.err.println("fihwscinwiofwiof "+lastSyncTimestamp);
        lastSyncTimestamp=util.converDateToTimestamp(lastSyncTimestamp.toString());
        currentTimestamp=util.converDateToTimestamp(currentTimestamp.toString());
        List<CustomerOffline> listOfCustomers;
        if(lastSyncZeroFlag==true){
        listOfCustomers=customerOfflineDao.selectCustomersOfflineAfterLastSyncForFirstSync(accountId, userId, lastSyncTimestamp,currentTimestamp);
        }else{
        listOfCustomers=customerOfflineDao.selectCustomersOfflineAfterLastSync(accountId, userId, lastSyncTimestamp,currentTimestamp);
        }
             //System.out.println("Customer List retrived from db"+" Current time "+currentTimestamp +" Last sync"+lastSyncTimestamp);
             log4jLog.info("Customer List retrived from db"+" Current time "+currentTimestamp +" Last sync"+lastSyncTimestamp+"UserID"+userId);
        for(int i=0;i<listOfCustomers.size();i++){
            CustomerOffline customerOffline=listOfCustomers.get(i);
            HashMap<String,String> mapOfData=new HashMap<String,String>();
            mapOfData.put("mobileId","0");  
            mapOfData.put("id",Integer.toString(customerOffline.getId()));
            mapOfData.put("Status",Integer.toString(customerOffline.getRecord_state()));
            customerInfo.add(mapOfData);                   
        }
            // System.out.println("Send to client");
             log4jLog.info("Send to client");
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "  ", "Customer List",customerInfo);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    /**
     *
     * @param customerOfflineWithFewData
     * @param userToken
     * @return
     */
    public Object removedTerritoryCustomerList(CustomerOfflineWithFewData customerOfflineWithFewData, String userToken) {
       // log4jLog.info("inside insertCustomerLocation in customerManger");\
        List<HashMap<String,String>> deletedCustomersList=new ArrayList<HashMap<String, String>>();
        List<Integer> removedTerriorty=new ArrayList<Integer>();
        List<Integer> deletedCustomersIds=new ArrayList<Integer>();
        if (util.isTokenValid(userToken)) {
                int accountId = util.accountIdForToken(userToken);
                int userId = util.userIdForToken(userToken);
                removedTerriorty=customerOfflineDao.terriortyList(customerOfflineWithFewData, accountId,userId);
                if(removedTerriorty!=null){
                    log4jLog.info("Terriorty Removed"+removedTerriorty.size()+"For user"+userId);
//                    System.out.println("Terriorty Removed"+removedTerriorty.size()+"For user"+userId);
                  deletedCustomersIds=customerOfflineDao.removedCustomerList(removedTerriorty, accountId, userId);
                  log4jLog.info("Deleted Customer="+deletedCustomersIds.size()+"For user"+userId);
//                    System.out.println("Deleted Customer="+deletedCustomersIds.size()+"For user"+userId);
                  for(int i=0;i<deletedCustomersIds.size();i++){
                  HashMap<String,String> mapOfData=new HashMap<String,String>();
                  mapOfData.put("id",deletedCustomersIds.get(i).toString());
                  mapOfData.put("Status","3");
                  deletedCustomersList.add(mapOfData);                   
                  }
                 return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "  ", "Customer List",deletedCustomersList);
                }else{
                
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "No terriorty is removed", "", "");
                }   
                } else {
              return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
  
   
}
      /**
     * @author Siddhesh
     * @return 
     * @date 01-12-2017, new api for sync
     * @param offlineDataSync
     * @param userToken
     * @purpose If location is not available for customer then insert the
     * location for customer if available nothing to do .
     */
     public Object insertCustomerData1Map(OfflineDataSync offlineDataSync, String userToken) {
    
         if (util.isTokenValid(userToken)) {
            HashMap<String,Object> masterData = new HashMap(); // added by Jyoti, 02-02-2018
            List<HashMap<String,Object>> customerInfo=new ArrayList<HashMap<String, Object>>();  
            HashMap <String,String> mapOfSilderData=new HashMap<String, String>();
            HashMap <String,Object> mapOfUserDetails=new HashMap<String, Object>();
            List<Map> listOfFormData = new ArrayList<Map>();
            int accountId = util.accountIdForToken(userToken);                
            int userId = util.userIdForToken(userToken);         
            List<LocalData> offlineDataList=offlineDataSync.getLocalData();
            int customerId=0;
            boolean flag=false;
            boolean isCheckInCheckOutNull=false;
            String mobileCustomerId="";
            boolean status=false;
            String customerName;
            int appointmentId=0;
            String mobileAppointmentId="0";
            int expenseId=0;
            String mobileExpenseId="0";
            boolean lastSyncZeroFlag=false;
            int recordState=0;
            Timestamp lastmodifiedDate;
            String currentDateTime=offlineDataSync.getCurrentTime();      //Current time of the mobile when the sync button is pressed in string
            long currentMilliSeconds = Long.parseLong(currentDateTime);   // date time in milliseconds 
            int syncStatus=offlineDataSync.getSyncState();
            Date currentDate = new Date(currentMilliSeconds);            //creating Date from millisecond
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     
            df1.setTimeZone(TimeZone.getTimeZone("GMT"));
            Timestamp currentTimestamp = Timestamp.valueOf(df1.format(currentDate));// timestamp of current time
            for(int i=0;i<offlineDataList.size();i++){      //Iterating the main for loop which contains customer ,ArrayList of appointments,expenses.
            try{
                LocalData offlineDataObj=offlineDataList.get(i); //Selecting one object from the list
                mobileCustomerId=offlineDataObj.getId();          //Retriving the mobileTempId
                customerName=offlineDataObj.getCustomerName();         
                recordState=offlineDataObj.getRecordState();
                if(offlineDataObj.getRecordState()==1 || offlineDataObj.getId().contains("TMP")){   //Checking the status of the data is for insert or update like wise
                    customerId=customerOfflineDao.isTempIdPresentForCustomer(mobileCustomerId, accountId); //Checking for the temp id present or not.If present then it wont be inserted And if it is present then will give the coresponding database id.
                    if(customerId==0){
                    if (!customerDao.isCustomerExistWithLocation(offlineDataObj.getCustomerName(), offlineDataObj.getCustomerLocation(), accountId)){
                        if(customerDao.isTerritoryExists(offlineDataObj.getTerritory().trim(),accountId)){
                            if(offlineDataObj.getCustomerAddress1()==null || offlineDataObj.getCustomerAddress1().equals(" ") ||offlineDataObj.getCustomerAddress1().equals("")){
                                if(offlineDataObj.getLasknownLangitude()!=0 || offlineDataObj.getLasknownLangitude()!=0){
                                    //System.out.println("wjmfowjowjdo"+offlineDataObj.getLasknownLangitude()+offlineDataObj.getLasknownLatitude());
                                 offlineDataObj.setCustomerAddress1(cacheManager.getLocationFromLatLongSats(offlineDataObj.getLasknownLatitude(),offlineDataObj.getLasknownLangitude(),0,0));
                                }else{
                            HashMap<String,Object> customerListMap=new HashMap<String,Object>();
                            customerListMap.put("mobileId",mobileCustomerId);
                            customerListMap.put("id", Integer.toString(customerId));
                            customerListMap.put("Status","99");
                            customerInfo.add(customerListMap);
                                }
                            }
                        String tmpIdOfCustomer = offlineDataObj.getId();
                        customerId=customerOfflineDao.insertCustomers(offlineDataObj, accountId, userId);
                        // added by jyoti, #31015
//                        System.out.println("check tmp id of customer :"+tmpIdOfCustomer+ " and customerId : "+customerId);
                        int isCustTmpIdUpdated = customerOfflineDao.updateUserTravelLogCustomerIdWithTmp(accountId, tmpIdOfCustomer, customerId);
//                        System.out.println("isCustTmpIdUpdated : "+isCustTmpIdUpdated);
                        // ended by jyoti, #31015
                        //
                        if (offlineDataObj.getLasknownLatitude() != 0 || offlineDataObj.getLasknownLangitude() != 0) {
                                Location location = new Location();
                                location.setLatitude(offlineDataObj.getLasknownLatitude());
                                location.setLangitude(offlineDataObj.getLasknownLangitude());
                                boolean isLocationAvailable = false;
                                if (offlineDataObj.getCustomerType() == 1) {
                                    location.setLocationType(customerId);
                                } else {
                                    location.setLocationType(offlineDataObj.getCustomerType());
                                }
                                location.setUserId(userId);
                                isLocationAvailable = locationDao.isLocationAvailable(location, accountId);
                                if (isLocationAvailable) {
                                    locationDao.updateUserLocation(location, accountId);
                                } else {
                                    locationDao.insertlocation(location, accountId);
                                }
                        }
                        //
                        log4jLog.info("Customer Inserted:-"+customerId+"userId "+userId);
                          //  System.out.println("Customer Inserted:-"+customerId);
                        HashMap<String,Object> customerListMap=new HashMap<String, Object>();
                        customerListMap.put("mobileId",mobileCustomerId);
                        customerListMap.put("id", Integer.toString(customerId));
                        customerListMap.put("Status","1");
                        customerInfo.add(customerListMap);
                        }else{
                            HashMap<String,Object> customerListMap=new HashMap<String, Object>();
                            customerListMap.put("mobileId",mobileCustomerId);
                            customerListMap.put("id", Integer.toString(customerId));
                            customerListMap.put("Status","99");
                            customerInfo.add(customerListMap);
                        }
                    }else{
                            HashMap<String,Object> customerListMap=new HashMap<String, Object>();
                            customerListMap.put("mobileId",mobileCustomerId);
                            customerListMap.put("id", Integer.toString(customerId));
                            customerListMap.put("Status","99");
                            customerInfo.add(customerListMap);
                    }
                }
                }else if(offlineDataObj.getRecordState()==2 && !offlineDataObj.getId().contains("TMP")){
                    customerId=Integer.parseInt(offlineDataObj.getId());
                    lastmodifiedDate=customerOfflineDao.checkCustomerLastUpdateTimeStamp(offlineDataObj, accountId, userId, customerId);
                    String modifiedOn=offlineDataObj.getModifiedOnMobile();
                    long modifiedOnMilliSeconds = Long.parseLong(modifiedOn);   // date time in milliseconds                 
                    Date modifiedOnDate = new Date(modifiedOnMilliSeconds);  
                  //  df1.setTimeZone(TimeZone.getTimeZone("GMT"));//creating Date from millisecond
                    Timestamp modifiedOnTimestamp = Timestamp.valueOf(df1.format(modifiedOnDate));
                    modifiedOnTimestamp=util.converDateToTimestamp(modifiedOnTimestamp.toString());
                        if(lastmodifiedDate.before(modifiedOnTimestamp)){
                            recordState=customerOfflineDao.getRecordStatusForCustomer(accountId, customerId);
                                if(recordState!=3){
                                    if(offlineDataObj.getCustomerAddress1()==null || offlineDataObj.getCustomerAddress1().equals(" ") ||offlineDataObj.getCustomerAddress1().equals("")){
                                        if(offlineDataObj.getLasknownLangitude()!=0 || offlineDataObj.getLasknownLangitude()!=0){                                    
                                            offlineDataObj.setCustomerAddress1(cacheManager.getLocationFromLatLongSats(offlineDataObj.getLasknownLatitude(),offlineDataObj.getLasknownLangitude(),0,0));
                                        }else{
                                                HashMap<String,Object> customerListMap=new HashMap<String,Object>();
                                                customerListMap.put("mobileId",mobileCustomerId);
                                                customerListMap.put("id", Integer.toString(customerId));
                                                customerListMap.put("Status","99");
                                                customerInfo.add(customerListMap);
                                        }
                                    }
                                    if(customerOfflineDao.updateCustomers(offlineDataObj, accountId, userId, customerId)){
                                        Location location = new Location();
                                        location.setLatitude(offlineDataObj.getLasknownLatitude());
                                        location.setLangitude(offlineDataObj.getLasknownLangitude());
                                        location.setLocationType(customerId);
                                        location.setUserId(userId);
                                        boolean isLocationAvailable = locationDao.isLocationAvailableOnlyWithLocationTypeId(customerId, accountId);
                                        if (isLocationAvailable) {
                                            locationDao.updateUserLocationOnlyWithLocationTypeId(location, accountId);
                                        } else {
                                            locationDao.insertlocation(location, accountId);
                                        }
                                       // System.out.println("Customer Upadetd"+customerId);
                                        log4jLog.info("Customer Upadeted:-"+customerId+"userId "+userId);
                                        HashMap<String,Object> customerListMap=new HashMap<String, Object>();
                                        customerListMap.put("mobileId","0");
                                        customerListMap.put("id", Integer.toString(customerId));
                                        customerListMap.put("Status","2"); 
                                        customerInfo.add(customerListMap);

                                    }else{
                                        HashMap<String,Object> customerListMap=new HashMap<String,Object>();
                                                customerListMap.put("mobileId",mobileCustomerId);
                                                customerListMap.put("id", Integer.toString(customerId));
                                                customerListMap.put("Status","99");
                                                customerInfo.add(customerListMap);
                                    }
                                }
                        }else{
                                HashMap<String,Object> customerListMap=new HashMap<String, Object>();
                                customerListMap.put("mobileId","0");
                                customerListMap.put("id", Integer.toString(customerId));
                                customerListMap.put("Status","2");
                                customerInfo.add(customerListMap);
                }
                }else if(offlineDataObj.getRecordState()==0 && !offlineDataObj.getId().contains("TMP")){
                        flag=true;
                        customerId=1;
                }
                else{
                        customerId=Integer.parseInt(offlineDataObj.getId());
//                        System.err.println("wdfnwilwndiwndioji"+customerId);
                }
                if(customerId!=0){
                    List<AppointmentOffline> appointmentOfflinesList=offlineDataObj.getAppointments();
                        if(!(appointmentOfflinesList.isEmpty())){   
                            for(int j=0;j<appointmentOfflinesList.size();j++){
                                AppointmentOffline appointmentOfflineObj=appointmentOfflinesList.get(j);
                                mobileAppointmentId=appointmentOfflineObj.getId();
                                if (appointmentOfflineObj.getScheckInTime()!=null && !appointmentOfflineObj.getScheckInTime().equals("")) {
                                    appointmentOfflineObj.setCheckInTime(util.converDateToTimestamp(appointmentOfflineObj.getScheckInTime()));
                                    isCheckInCheckOutNull=true;
                                }
                                if (appointmentOfflineObj.getScheckOutTime()!=null && !appointmentOfflineObj.getScheckOutTime().equals("")) {
                                    appointmentOfflineObj.setCheckOutTime(util.converDateToTimestamp(appointmentOfflineObj.getScheckOutTime()));
                                    isCheckInCheckOutNull=true;
                                }
                                if(appointmentOfflineObj.getRecordState()==1 ||appointmentOfflineObj.getId().contains("TMP")){
                                    if(appointmentOfflineObj.getRecordState()==1){
                                    appointmentId=customerOfflineDao.isTempIdPresentForAppointment(mobileAppointmentId, accountId);
                                    if(appointmentId==0){
                                        appointmentOfflineObj.setDateTime(util.converDateToTimestamp(appointmentOfflineObj.getSdateTime()));
                                        appointmentOfflineObj.setEndTime(util.converDateToTimestamp(appointmentOfflineObj.getSendTime()));
                                        if(flag==true){
                                        customerId=Integer.parseInt(appointmentOfflineObj.getCustomer().getId());
                                        }
                                        appointmentId=customerOfflineDao.insertAppointments(appointmentOfflineObj, accountId, userId, customerId);
                                       // System.out.println("Appointment Inserted"+appointmentId);
                                        log4jLog.info("Appointment Inserted:-"+appointmentId+"userId "+userId);
                                        
                                    }
                                }else if(appointmentOfflineObj.getRecordState()==2){
                                    appointmentId=customerOfflineDao.isTempIdPresentForAppointment(mobileAppointmentId, accountId);
                                    appointmentOfflineObj.setDateTime(util.converDateToTimestamp(appointmentOfflineObj.getSdateTime()));
                                        appointmentOfflineObj.setEndTime(util.converDateToTimestamp(appointmentOfflineObj.getSendTime()));
                                        if(flag==true){
                                        customerId=Integer.parseInt(appointmentOfflineObj.getCustomer().getId());
                                        }
                                        if(appointmentOfflineObj.getNextAppointmentStr().contains("TMP")){
                                            appointmentOfflineObj.setNextAppointment(customerOfflineDao.isTempIdPresentForAppointment(appointmentOfflineObj.getNextAppointmentStr(), accountId));
                                        }
                                            if(isCheckInCheckOutNull==false){
                                                status=customerOfflineDao.updateAppointment(appointmentOfflineObj, accountId, userId, customerId,appointmentId);
                                                //System.out.println("CheckIn CheckOut NULL"+appointmentId);
                                                try{
                                                    
                                                    if(appointmentOfflineObj.getStatus()==1){
                                                            UserKey userKey=new UserKey();
                                                            userKey.setKeyValue("1");
                                                            User user1=new User();
                                                            user1.setId(appointmentOfflineObj.getAssignedTo().getId());
                                                            userKey.setUserId(user1);
                                                            userKey.setUserKay("InMeeting");
                                                            userKeyDao.updateUserKeys(userKey, accountId);
                                                    }else if(appointmentOfflineObj.getStatus()==2){
                                                            UserKey userKey=new UserKey();
                                                            userKey.setKeyValue("0");
                                                            User user1=new User();
                                                            user1.setId(appointmentOfflineObj.getAssignedTo().getId());
                                                            userKey.setUserId(user1);
                                                            userKey.setUserKay("InMeeting");
                                                            userKeyDao.updateUserKeys(userKey, accountId);
                                                    }
                                                }catch(Exception e){
//                                                e.printStackTrace();
                                                    log4jLog.info(" insertCustomerData1 " + e);
                                                }
                                                
                                                log4jLog.info("CheckIn CheckOut NULL:-"+appointmentId+"userId "+userId);
                                            }else{
                                               status=customerOfflineDao.updateAppointmentForCheckInCheckOut(appointmentOfflineObj, accountId, userId, customerId,appointmentId);
                                               //System.out.println("CheckIn CheckOut NOT NULL"+appointmentId);
                                                if(status){
                                                try{
                                                    
                                                    if(appointmentOfflineObj.getStatus()==1){
                                                        UserKey userKey=new UserKey();
                                                            userKey.setKeyValue("1");
                                                            User user1=new User();
                                                            user1.setId(appointmentOfflineObj.getAssignedTo().getId());
                                                            userKey.setUserId(user1);
                                                            userKey.setUserKay("InMeeting");
                                                            userKeyDao.updateUserKeys(userKey, accountId);
                                                        }else if(appointmentOfflineObj.getStatus()==2){
                                                            UserKey userKey=new UserKey();
                                                            userKey.setKeyValue("0");
                                                            User user1=new User();
                                                            user1.setId(appointmentOfflineObj.getAssignedTo().getId());
                                                            userKey.setUserId(user1);
                                                            userKey.setUserKay("InMeeting");
                                                            userKeyDao.updateUserKeys(userKey, accountId);
                                                        }
                                                    }catch(Exception e){
//                                                         e.printStackTrace();
                                                        log4jLog.info(" insertCustomerData1 " + e);
                                                    }
                                                }
                                               log4jLog.info("CheckIn CheckOut NOT NULL:-"+appointmentId+"userId "+userId);
                                            }
                                            if(status){
                                                log4jLog.info("Appointment Updated through Insert:-"+appointmentId+"userId "+userId);
                                                //System.out.println("Appointment Updated through Insert"+appointmentId);
                                            }else{
                                                  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Appointment Update failed", "", ""); // change to be made in case of error
                                            }
                                    }
                                }else if(appointmentOfflineObj.getRecordState()==2 && !appointmentOfflineObj.getId().contains("TMP")){
                                        appointmentId=Integer.parseInt(mobileAppointmentId);
                                        appointmentOfflineObj.setDateTime(util.converDateToTimestamp(appointmentOfflineObj.getSdateTime()));
                                        appointmentOfflineObj.setEndTime(util.converDateToTimestamp(appointmentOfflineObj.getSendTime()));
                                        if(flag==true){
                                        customerId=Integer.parseInt(appointmentOfflineObj.getCustomer().getId());
                                        }
                                        if(appointmentOfflineObj.getNextAppointmentStr().contains("TMP")){
                                            appointmentOfflineObj.setNextAppointment(customerOfflineDao.isTempIdPresentForAppointment(appointmentOfflineObj.getNextAppointmentStr(), accountId));
                                        }
                                           if(isCheckInCheckOutNull==false){
                                                status=customerOfflineDao.updateAppointment(appointmentOfflineObj, accountId, userId, customerId,appointmentId);
                                             //   System.out.println("CheckIn CheckOut NULL"+appointmentId);
                                                try{
                                                    
                                                    if(appointmentOfflineObj.getStatus()==1){
                                                            UserKey userKey=new UserKey();
                                                            userKey.setKeyValue("1");
                                                            User user1=new User();
                                                            user1.setId(appointmentOfflineObj.getAssignedTo().getId());
                                                            userKey.setUserId(user1);
                                                            userKey.setUserKay("InMeeting");
                                                            userKeyDao.updateUserKeys(userKey, accountId);
                                                    }else if(appointmentOfflineObj.getStatus()==2){
                                                            UserKey userKey=new UserKey();
                                                            userKey.setKeyValue("0");
                                                            User user1=new User();
                                                            user1.setId(appointmentOfflineObj.getAssignedTo().getId());
                                                            userKey.setUserId(user1);
                                                            userKey.setUserKay("InMeeting");
                                                            userKeyDao.updateUserKeys(userKey, accountId);
                                                    }
                                                }catch(Exception e){
//                                                e.printStackTrace();
                                                    log4jLog.info(" insertCustomerData1 " + e);
                                                }
                                                log4jLog.info("CheckIn CheckOut NULL:-"+appointmentId+"userId "+userId);
                                            }else{
                                               status=customerOfflineDao.updateAppointmentForCheckInCheckOut(appointmentOfflineObj, accountId, userId, customerId,appointmentId);
//                                            //   System.out.println("CheckIn CheckOut NOT NULL"+appointmentId);
                                               try{
                                                    
                                                    if(appointmentOfflineObj.getStatus()==1){
                                                            UserKey userKey=new UserKey();
                                                            userKey.setKeyValue("1");
                                                            User user1=new User();
                                                            user1.setId(appointmentOfflineObj.getAssignedTo().getId());
                                                            userKey.setUserId(user1);
                                                            userKey.setUserKay("InMeeting");
                                                            userKeyDao.updateUserKeys(userKey, accountId);
                                                    }else if(appointmentOfflineObj.getStatus()==2){
                                                            UserKey userKey=new UserKey();
                                                            userKey.setKeyValue("0");
                                                            User user1=new User();
                                                            user1.setId(appointmentOfflineObj.getAssignedTo().getId());
                                                            userKey.setUserId(user1);
                                                            userKey.setUserKay("InMeeting");
                                                            userKeyDao.updateUserKeys(userKey, accountId);
                                                    }
                                                }catch(Exception e){
//                                                e.printStackTrace();
                                                    log4jLog.info(" insertCustomerData1 " + e);
                                                }
                                               log4jLog.info("CheckIn CheckOut NOT NULL:-"+appointmentId+"userId "+userId);
                                            }
                                            if(status){
                                               // System.out.println("Appointment Updated"+appointmentId);
                                                log4jLog.info("Appointment Updated:-"+appointmentId+"userId "+userId);
                                            }else{
//                                                  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Appointment Update failed", "", ""); // change to be made in case of error
                                                HashMap<String,Object> customerListMap=new HashMap<String, Object>();
                                                customerListMap.put("mobileId","0");
                                                customerListMap.put("id", Integer.toString(customerId));
                                                customerListMap.put("Status","99");
                                                customerInfo.add(customerListMap);
                                                
                                            }
                                }else if(appointmentOfflineObj.getRecordState()==3 && !appointmentOfflineObj.getId().contains("TMP")){
                                           appointmentId=Integer.parseInt(mobileAppointmentId);
                                           appointmentDao.deleteAppointment(appointmentId, accountId);
                                          // System.out.println("Appointment Deleted"+appointmentId);
                                           log4jLog.info("Appointment Deleted:-"+appointmentId+"userId "+userId);
                                }
                                if(appointmentId!=0){
                                    List<Expense> expensesList=offlineDataObj.getExpense();
                                        if(!(expensesList.isEmpty())){
                                            for(int k=0;k<expensesList.size();k++){
                                                  Expense expenseObj=expensesList.get(k);
                                                  mobileExpenseId=expenseObj.getExpenseTempId();
                                                  expenseId=customerOfflineDao.isTempIdPresentForExpense(mobileExpenseId, accountId);
                                                  if(expenseId==0){
                                                    if(expenseObj.getAppointmentTempId().equalsIgnoreCase(mobileAppointmentId)){
                                                        if(flag==true){
                                                        customerId=Integer.parseInt(expenseObj.getCustomerTempId());
                                                        }
                                                        expenseId=customerOfflineDao.insertExpense(expenseObj, accountId, userId, customerId, appointmentId, mobileAppointmentId);
                                                        //System.out.println("Expense Inserted"+expenseId+"For Customer"+customerId+"And appointment"+appointmentId);
                                                        log4jLog.info("Expense Inserted"+expenseId+"For Customer"+customerId+"And appointment"+appointmentId+"UserID "+userId);
                                                    }
                                                  }
                                            }
                                        }
                                }else{
                                     // return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Appointment not inserted", "", ""); //change 
                                    HashMap<String,Object> customerListMap=new HashMap<String, Object>();
                                                customerListMap.put("mobileId","0");
                                                customerListMap.put("id", Integer.toString(customerId));
                                                customerListMap.put("Status","99");
                                                customerInfo.add(customerListMap);
                                }
                            }
                        }
                             List<Expense> expensesList=offlineDataObj.getExpense();
                            if(!(expensesList.isEmpty())){
                                for(int k=0;k<expensesList.size();k++){
                                    Expense expenseObj=expensesList.get(k);
                                   mobileExpenseId=expenseObj.getExpenseTempId();
                                    expenseId=customerOfflineDao.isTempIdPresentForExpense(mobileExpenseId, accountId);
                                    if(expenseId==0){
                                       if(expenseObj.getAppointmentTempId().equalsIgnoreCase("0")){
                                            customerId=expenseObj.getCustomerId();
                                            appointmentId=expenseObj.getAppointmentId();
                                            mobileAppointmentId="0";
                                            expenseId=customerOfflineDao.insertExpense(expenseObj, accountId, userId, customerId, appointmentId, mobileAppointmentId);
                                           // System.out.println("Expense Inserted"+expenseId);
                                            log4jLog.info("Expense Inserted"+expenseId+"UserID "+userId);
                                        }
                                    }
                                }
                            }
                        }else{
                     HashMap<String,Object> customerListMap=new HashMap<String, Object>();
                     customerListMap.put("mobileId",mobileCustomerId);
                     customerListMap.put("id", Integer.toString(customerId));
                     customerListMap.put("Status","99");
                     customerInfo.add(customerListMap);
                  //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Customer not inserted", "", "");
                }
           
            }catch(Exception e ){
                HashMap<String,Object> customerListMap=new HashMap<String, Object>();
                customerListMap.put("mobileId",mobileCustomerId);
                customerListMap.put("id", Integer.toString(customerId));
                customerListMap.put("Status","99");
                customerInfo.add(customerListMap);
               // e.printStackTrace();
                log4jLog.info(" insertCustomerData1 " + e);
               
            }
           //  }
        }
       // }             
        String lastSyncDateTime=offlineDataSync.getLastSyncTime();
        if(lastSyncDateTime.equals("0")){
        lastSyncZeroFlag=true;
        }
        long lastSyncMilliSeconds = Long.parseLong(lastSyncDateTime);   // date time in milliseconds                 
        Date lastSyncDate = new Date(lastSyncMilliSeconds);            //creating Date from millisecond
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        df.setTimeZone(TimeZone.getTimeZone("GMT")); 
        Timestamp lastSyncTimestamp = Timestamp.valueOf(df.format(lastSyncDate));
        //System.err.println("fihwscinwiofwiof "+lastSyncTimestamp);
        lastSyncTimestamp=util.converDateToTimestamp(lastSyncTimestamp.toString());
        currentTimestamp=util.converDateToTimestamp(currentTimestamp.toString());
        List<CustomerOffline> listOfCustomers=new ArrayList<CustomerOffline>();    
//             System.out.println("syncStatus"+syncStatus); 
        if(lastSyncZeroFlag==true){
        listOfCustomers=customerOfflineDao.selectCustomersOfflineAfterLastSyncForFirstSync(accountId, userId, lastSyncTimestamp,currentTimestamp);
        AuthenticationUserManager authenticationUserManager=new AuthenticationUserManager();
            CustomFormManager customFormManager = new CustomFormManager();
             mapOfSilderData=authenticationUserManager.silderDataForOfflineAPI(currentTimestamp.toString(), lastSyncTimestamp.toString(), userId, accountId, userToken);
                // Added by jyoti, to send all masterDate after last sync
                    masterData = authenticationUserManager.getMasterDataAfterLastSync(currentTimestamp.toString(), lastSyncTimestamp.toString(), userId, accountId);
                // Ended by jyoti
                    listOfFormData = customFormManager.getAllFormsForOffline(offlineDataSync.getFormdetails(), currentDateTime, userToken);
             mapOfUserDetails=new HashMap<String, Object>();
             mapOfUserDetails.put("AdminForms", listOfFormData);
              mapOfUserDetails.put("userSliderData", mapOfSilderData);
              if(!masterData.isEmpty()){
                mapOfUserDetails.putAll(masterData); // Added by Jyoti
              }
             //customerInfo.add(mapOfUserDetails);
            //listOfCustomers=customerOfflineDao.selectCustomersOfflineAfterLastSync(accountId, userId, lastSyncTimestamp,currentTimestamp);
        }else{
            if(syncStatus==1){
                AuthenticationUserManager authenticationUserManager=new AuthenticationUserManager();
           mapOfSilderData=authenticationUserManager.silderDataForOfflineAPI(currentTimestamp.toString(), lastSyncTimestamp.toString(), userId, accountId, userToken);
              CustomFormManager customFormManager = new CustomFormManager();
               listOfFormData = customFormManager.getAllFormsForOffline(offlineDataSync.getFormdetails(), lastSyncDateTime, userToken);
            // Added by jyoti, to send all masterDate after last sync
               masterData = authenticationUserManager.getMasterDataAfterLastSync(currentTimestamp.toString(), lastSyncTimestamp.toString(), userId, accountId);
            // Ended by jyoti
            
            mapOfUserDetails.put("userSliderData", mapOfSilderData);
             mapOfUserDetails.put("AdminForms", listOfFormData);
            if(!masterData.isEmpty()){
                mapOfUserDetails.putAll(masterData); // Added by Jyoti
            }
             //customerInfo.add(mapOfUserDetails);
            listOfCustomers=customerOfflineDao.selectCustomersOfflineAfterLastSync(accountId, userId, lastSyncTimestamp,currentTimestamp);
            List<CustomerOffline> listOfCustomerWithTerriorty=new ArrayList<CustomerOffline>();
            listOfCustomerWithTerriorty=customerOfflineDao.selectCustomersBasedOnNewAddedTerriorty(accountId, userId, lastSyncTimestamp, currentTimestamp);
            if(!listOfCustomerWithTerriorty.isEmpty()){
            listOfCustomers.addAll(listOfCustomerWithTerriorty);
            }
            }else{
              mapOfSilderData.put("attendanceId","");
              mapOfSilderData.put("punchInTime", "");
              mapOfSilderData.put("punchOutTime", "");
              mapOfSilderData.put("punchDate", "");
              mapOfSilderData.put("id", "");
              mapOfSilderData.put("allowTimeout", "");
              mapOfSilderData.put("allowOffline", "");
              mapOfSilderData.put("locationInterval", "");
            mapOfUserDetails.put("userSliderData", mapOfSilderData);
            }
        }
             //System.out.println("Customer List retrived from db"+" Current time "+currentTimestamp +" Last sync"+lastSyncTimestamp);
             log4jLog.info("Customer List retrived from db"+" Current time "+currentTimestamp +" Last sync"+lastSyncTimestamp+"UserID"+userId);
        for(int i=0;i<listOfCustomers.size();i++){
            CustomerOffline customerOffline=listOfCustomers.get(i);
            HashMap<String,Object> mapOfData=new HashMap<String,Object>();
            mapOfData.put("mobileId","0");  
            mapOfData.put("id",Integer.toString(customerOffline.getId()));
            if(lastSyncZeroFlag==true){
            mapOfData.put("Status","1");
            }
            else{
            mapOfData.put("Status",Integer.toString(customerOffline.getRecord_state()));
            }
            customerInfo.add(mapOfData);                   
        }
        HashMap<String,Object> finalDataMap=new HashMap<String, Object>();
        finalDataMap.put("OfflineCustomerState", customerInfo);
        List<HashMap> listOfMapFinalData=new ArrayList<HashMap>();
        Map<String,Object> mapOfFinalData=new HashMap<String, Object>();
//        listOfMapFinalData.add(mapOfUserDetails);
//        listOfMapFinalData.add(finalDataMap);
        mapOfFinalData.putAll(mapOfUserDetails);
        mapOfFinalData.putAll(finalDataMap);
            // System.out.println("Send to client");
             log4jLog.info("Send to client");
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "  ", "Customer List",mapOfFinalData);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
}
