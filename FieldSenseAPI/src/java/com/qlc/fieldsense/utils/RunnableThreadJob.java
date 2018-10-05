/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.utils;

import com.qlc.fieldsense.accounts.model.AccountSettingValues;
import com.qlc.fieldsense.activityPurpose.model.ActivityPurpose;
import com.qlc.fieldsense.customer.model.Customer;
import com.qlc.fieldsense.expenseCategory.model.ExpenseCategory;
import com.qlc.fieldsense.industryCategory.model.IndustryCategory;
import com.qlc.fieldsense.territoryCategory.model.TerritoryCategory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javapns.notification.PushNotificationBigPayload;
import javapns.notification.PushNotificationPayload;
import net.sf.json.JSONObject;
import org.apache.struts2.components.Else;

/**
 *
 * @author Jyoti
 */
public class RunnableThreadJob {

    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
    public static final Logger LOG4JLOG = Logger.getLogger("RunnableThreadJob");

    /**
     * @param customerStatus
     * @return @Added by Jyoti, 14-01-2018
     * @param accountId
     * @param customer
     */
    public Runnable sendAddEditCustomerNotificationToUsersOfAccount(final int customerStatus, final Customer customer, final int accountId) {
        try{
            LOG4JLOG.log(Level.INFO, "RunnableThreadJob inside sendAddEditCustomerNotificationToUsersOfAccount for accountId : ", accountId);
            Runnable runnableJob = new Runnable() {
                @Override
                public void run() {
//                    System.out.println(accountId + " << for accnid. sendAddEditCustomerNotificationToUsersOfAccount, terrirot"+customer.getTerritory().trim());
                    List<java.util.HashMap> listOfuserIDs = fieldSenseUtils.getListOfUserIdsForTerritoryID(customer.getTerritory().trim(), accountId);
                    if (listOfuserIDs.size() > 0) {
                        int count = 0;
                        for (java.util.HashMap userInfo : listOfuserIDs) {
                            String gcmId = (String) userInfo.get("gcmID");
                            int deviceOS = (Integer) userInfo.get("device_os");
                            int appVersion = (Integer) userInfo.get("appVersion");
                            if(appVersion > Integer.parseInt(Constant.APP_VERSION_CHECK)){
                                if(!(gcmId.equals("0") || gcmId.isEmpty())) {

                                    Map<String, Object> m_data = new HashMap<>();
                                    m_data.put("type", "customer");
                                    m_data.put("message", "");
                                    m_data.put("customerStatus", customerStatus+""); // 0=new, 1=update, 2=delete
                                    m_data.put("id", customer.getId());
                                    m_data.put("customerAddress1", customer.getCustomerAddress1());
                                    m_data.put("customerLocation", customer.getCustomerLocation());
                                    m_data.put("customerName", customer.getCustomerName());
                                    m_data.put("customerPrintas", customer.getCustomerPrintas());                                
                                    m_data.put("lasknownLangitude", customer.getLasknownLangitude());
                                    m_data.put("lasknownLatitude", customer.getLasknownLatitude());
                                    m_data.put("customerType", customer.getCustomerType());
                                    m_data.put("industry", customer.getIndustry());
                                    m_data.put("territory", customer.getTerritory());
                                    m_data.put("customerZipcode", "");
                                    m_data.put("dbId", 0);
                                    m_data.put("isOffline", false);                                
                                    m_data.put("isSynced", false);
                                    m_data.put("recordState", customer.getRecordState());
                                    if (customer.getCreatedOn().before(customer.getModifiedOn())) {
                                        m_data.put("isModified", "true");
                                    } else {
                                        m_data.put("isModified", "false");
                                    }
                                    com.qlc.fieldsense.utils.PushNotificationManager push = new com.qlc.fieldsense.utils.PushNotificationManager();
                                    push.addEditNotification(m_data, gcmId, deviceOS,null);
                                    int userid = (Integer) userInfo.get("id");      
//                                    System.out.println("sendAddEditCustomerNotificationToUsersOfAccount userid : "+userid+ ", gcmId : "+gcmId+ ", count : " + count++);
                                }
                            }
                        }
                    }
                }
            };
            Thread sendPushNotification = new Thread(runnableJob);
            sendPushNotification.start();
//            sendPushNotification.join(); // maintain lock like synchronize
//            System.out.println("sendAddEditCustomerNotificationToUsersOfAccount runnableJob finished : " + runnableJob);
//            System.out.println("=====================================================================================================");
            return runnableJob;
        } catch(Exception e){
            e.printStackTrace();
            LOG4JLOG.log(Level.INFO, " Exception RunnableThreadJob inside sendAddEditCustomerNotificationToUsersOfAccount for accountId : {0}{1}", new Object[]{accountId, e});
            return null;
        }
    }

    /**
     * @return @Added by Jyoti, 14-01-2018
     * @param accountId
     * @param accountSettings
     */
    public Runnable sendEditAccountSettingsNotificationToUsersOfAccount(final AccountSettingValues accountSettings, final int accountId) {
        try{
            LOG4JLOG.log(Level.INFO, "RunnableThreadJob inside sendEditAccountSettingsNotificationToUsersOfAccount for accountId : ", accountId);
            Runnable runnableJob = new Runnable() {
                @Override
                public void run() {
                    List<java.util.HashMap> userInfoList = fieldSenseUtils.getListOfUserIdForAccountId(accountId);
//                    System.out.println("userList size : " + userInfoList.size());
                    if (userInfoList.size() > 0) {
                        int count = 0;
                        for (java.util.HashMap userInfo : userInfoList) {
                            String gcmId = (String) userInfo.get("gcmID");
                            int deviceOS = (Integer) userInfo.get("device_os");
                            int appVersion = (Integer) userInfo.get("appVersion");
//                            System.out.println("sendEditAccountSettingsNotificationToUsersOfAccount >> appVersion : "+appVersion);
                            if(appVersion > Integer.parseInt(Constant.APP_VERSION_CHECK) && deviceOS == 1){                            
                                if(!(gcmId.equals("0") || gcmId.isEmpty())) {

                                    Map<String, Object> m_data = new HashMap<>();
                                    // added by jyoti, discussed after sagar
                                    String allowOffline = "0", allowTimeout = "0";
                                    if (accountSettings.isAllowOffline()) {
                                        allowOffline = "1";
                                    }
                                    if (accountSettings.isAllowTimeout()) {
                                        allowTimeout = "1";
                                    }
                                    // ended by jyoti, discussed after sagar
                                    m_data.put("type", "accountSettings");
                                    m_data.put("message", "Account Settings updated");
//                                    m_data.put("allowTimeout", accountSettings.isAllowTimeout());
                                    m_data.put("allowTimeout", allowTimeout);
//                                    m_data.put("allowOffline", accountSettings.isAllowOffline());
                                    m_data.put("allowOffline", allowOffline);
                                    m_data.put("interval", accountSettings.getInterval());
                                    m_data.put("auto_punch_out_time", accountSettings.getAuto_punch_out_time());
                                    m_data.put("auto_punch_out_type", accountSettings.getAuto_punch_out_type());
                                    m_data.put("working_hours", accountSettings.getWorking_hours());                                    
                                    m_data.put("isModified", "true"); // need to compare with created_on - updated_on, in db/modal field is missing
                                    com.qlc.fieldsense.utils.PushNotificationManager push = new com.qlc.fieldsense.utils.PushNotificationManager();                            
                                    push.addEditNotification(m_data, gcmId, deviceOS,null);
                                    int userid = (Integer) userInfo.get("id");   
//                                    System.out.println("wh >> " + accountSettings.getWorking_hours() + "atype >> " +accountSettings.getAuto_punch_out_type() + "atime >> " +accountSettings.getAuto_punch_out_time());
//                                    System.out.println("sendEditAccountSettingsNotificationToUsersOfAccount userid : "+userid+ ", gcmId : "+gcmId+ ", count : " + count++);
                                }
                            } 
                            //Added by sanchita   ---//optimization for ios notification
                            else if(deviceOS == 2){                            
                                if(!(gcmId.equals("0") || gcmId.isEmpty())) {
                                    String allowOffline = "0", allowTimeout = "0";
                                    if (accountSettings.isAllowOffline()) {
                                        allowOffline = "1";
                                    }
                                    if (accountSettings.isAllowTimeout()) {
                                        allowTimeout = "1";
                                    }
                                    // ended by jyoti, discussed after sagar
                                    try{
                                    JSONObject messageJsonObject = new JSONObject();
                            
                                    messageJsonObject.put("allowTimeout", allowTimeout);
                                    messageJsonObject.put("allowOffline", allowOffline);
                                    messageJsonObject.put("interval", accountSettings.getInterval());
                                    messageJsonObject.put("auto_punch_out_time", accountSettings.getAuto_punch_out_time());
                                    messageJsonObject.put("auto_punch_out_type", accountSettings.getAuto_punch_out_type());
                                    messageJsonObject.put("working_hours", accountSettings.getWorking_hours());                                    
                                    messageJsonObject.put("isModified", "true"); // need to compare with created_on - updated_on, in db/modal field is missing
                                    
                                    // payload send in notification, Added by sanchita
                                    PushNotificationPayload payload = PushNotificationBigPayload.complex(); 
                                    payload.addAlert("Account Settings updated");
                                    payload.addCustomDictionary("type", "accountSettings");                                   
                                    payload.addCustomDictionary("objectData", messageJsonObject.toString());                                                                                    
                                             
//                                    System.out.println("payload >> "+payload.toString());
                                    com.qlc.fieldsense.utils.PushNotificationManager push = new com.qlc.fieldsense.utils.PushNotificationManager();                            
                                    push.addEditNotification(messageJsonObject, gcmId, deviceOS,payload);
                                    int userid = (Integer) userInfo.get("id");   
                                    count++;
//                                    System.out.println("sendEditAccountSettingsNotificationToUsersOfAccount device == 2,  userid : "+userid+ ", gcmId : "+gcmId+ ", count : " + count);
                                    }
                                    catch(Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            }// ended by sanchita
                            else{
                                int userid = (Integer) userInfo.get("id");                             
                            }
                        }
                    }
                }
            };
            Thread sendPushNotification = new Thread(runnableJob);
            sendPushNotification.start();
//            sendPushNotification.join(); // maintain lock like synchronize
//            System.out.println("sendEditAccountSettingsNotificationToUsersOfAccount runnableJob finished : " + runnableJob);
//            System.out.println("=====================================================================================================");
            return runnableJob;
        } catch(Exception e){
            e.printStackTrace();
            LOG4JLOG.log(Level.INFO, " Exception RunnableThreadJob inside sendEditAccountSettingsNotificationToUsersOfAccount for accountId : {0}{1}", new Object[]{accountId, e});
            return null;
        }
    }

    /**
     * @added by Jyoti, 14-01-2018
     * @param purposeStatus
     * @param aPurpose
     * @param accountId
     * @return
     */
    public Runnable sendAddEditActivityPurposeNotificationToUsersOfAccount(final int purposeStatus, final ActivityPurpose aPurpose, final int accountId) {
        try{
            LOG4JLOG.log(Level.INFO, "RunnableThreadJob inside sendAddEditActivityPurposeNotificationToUsersOfAccount for accountId : ", accountId);
            Runnable runnableJob = new Runnable() {
                @Override
                public void run() {
                    List<java.util.HashMap> userInfoList = fieldSenseUtils.getListOfUserIdForAccountId(accountId);
//                    System.out.println("userList size : " + userInfoList.size());
                    if (userInfoList.size() > 0) {
                        int count = 0;
                        for (java.util.HashMap userInfo : userInfoList) {
                            String gcmId = (String) userInfo.get("gcmID");
                            int deviceOS = (Integer) userInfo.get("device_os");
                            int appVersion = (Integer) userInfo.get("appVersion");
                            if(appVersion > Integer.parseInt(Constant.APP_VERSION_CHECK)){
                                if(!(gcmId.equals("0") || gcmId.isEmpty())) {

                                    Map<String, Object> m_data = new HashMap<>();
                                    m_data.put("type", "activityPurpose");
                                    m_data.put("message", "");
                                    m_data.put("purposeStatus", purposeStatus+""); // 0=new, 1=update, 2=delete
                                    m_data.put("id", aPurpose.getId());
                                    m_data.put("purpose", aPurpose.getPurpose());
                                    m_data.put("isActive", aPurpose.isIsActive());
                                    m_data.put("isModified", "true"); // need to compare with created_on - updated_on, in db/modal field is missing
                                    com.qlc.fieldsense.utils.PushNotificationManager push = new com.qlc.fieldsense.utils.PushNotificationManager();
                                    push.addEditNotification(m_data, gcmId, deviceOS,null);
                                    int userid = (Integer) userInfo.get("id");      
//                                    System.out.println("sendAddEditActivityPurposeNotificationToUsersOfAccount userid : "+userid+ ", gcmId : "+gcmId+ ", count : " + count++);
                                }
                            }                            
                        }
                    }
                }
            };
            Thread sendPushNotification = new Thread(runnableJob);
            sendPushNotification.start();
//            sendPushNotification.join(); // maintain lock like synchronize
//            System.out.println("sendAddEditActivityPurposeNotificationToUsersOfAccount runnableJob finished : " + runnableJob);
//            System.out.println("=====================================================================================================");
            return runnableJob;
        } catch(Exception e){
            e.printStackTrace();
            LOG4JLOG.log(Level.INFO, " Exception RunnableThreadJob inside sendAddEditActivityPurposeNotificationToUsersOfAccount for accountId : {0}{1}", new Object[]{accountId, e});
            return null;
        }
    }
    
    /**
     * @param userInfoList
     * @param territoryStatus
     * @added by Jyoti, 14-01-2018
     * @param tCategory
     * @param accountId
     * @return
     */
    public Runnable sendAddEditTerritoryCategoryNotificationToUsersOfAccount(final List<java.util.HashMap> userInfoList,final int territoryStatus, final TerritoryCategory tCategory, final int accountId) {
        try{
            LOG4JLOG.log(Level.INFO, "RunnableThreadJob inside sendAddEditTerritoryCategoryNotificationToUsersOfAccount for accountId : ", accountId);
            Runnable runnableJob = new Runnable() {
                @Override
                public void run() {
//                    List<java.util.HashMap> userInfoList = fieldSenseUtils.getListOfUserIdsForTerritoryID(tCategory.getCategoryName().trim(), accountId);
//                    System.out.println("userInfoList : "+userInfoList.size());
                    if(userInfoList.size() > 0) {
                        int count = 0;
                        for (java.util.HashMap userInfo : userInfoList) {
                            String gcmId = (String) userInfo.get("gcmID");
                            int deviceOS = (Integer) userInfo.get("device_os");
                            int appVersion = (Integer) userInfo.get("appVersion");
                            if(appVersion > Integer.parseInt(Constant.APP_VERSION_CHECK)){
                                if(!(gcmId.equals("0") || gcmId.isEmpty())) {
                                    Map<String, Object> m_data = new HashMap<>();
                                    m_data.put("type", "territory");
                                    m_data.put("message", "");
                                    m_data.put("territoryStatus", territoryStatus+""); // 0=new, 1=update, 2=delete
                                    m_data.put("id", tCategory.getId()); 
                                    m_data.put("categoryName", tCategory.getCategoryName());
                                    m_data.put("isActive", tCategory.isIsActive());
                                    m_data.put("updatedOn", tCategory.getUpdatedOn());
                                    if (tCategory.getCretedOn().before(tCategory.getUpdatedOn())) {
                                        m_data.put("isModified", "true");
                                    } else {
                                        m_data.put("isModified", "false");
                                    }
                                    com.qlc.fieldsense.utils.PushNotificationManager push = new com.qlc.fieldsense.utils.PushNotificationManager();
                                    push.addEditNotification(m_data, gcmId, deviceOS,null);
                                    int userid = (Integer) userInfo.get("id");      
//                                    System.out.println("sendAddEditTerritoryCategoryNotificationToUsersOfAccount userid : "+userid+ ", gcmId : "+gcmId+ ", count : " + count++);
                                }
                            }                            
                        }
                    }
                }
            };
            Thread sendPushNotification = new Thread(runnableJob);
            sendPushNotification.start();
//            sendPushNotification.join(); // maintain lock like synchronize
//            System.out.println("sendAddEditTerritoryCategoryNotificationToUsersOfAccount runnableJob finished : " + runnableJob);
//            System.out.println("=====================================================================================================");
            return runnableJob;
        } catch(Exception e){
            e.printStackTrace();
            LOG4JLOG.log(Level.INFO, " Exception RunnableThreadJob inside sendAddEditTerritoryCategoryNotificationToUsersOfAccount for accountId : {0}{1}", new Object[]{accountId, e});
            return null;
        }
    }
    
    /**
     * @param expenseStatus
     * @added by Jyoti, 14-01-2018
     * @param expenseCategory
     * @param accountId
     * @return
     */
    public Runnable sendAddEditExpenseCategoryNotificationToUsersOfAccount(final int expenseStatus, final ExpenseCategory expenseCategory, final int accountId) {
        try{
            LOG4JLOG.log(Level.INFO, "RunnableThreadJob inside sendAddEditExpenseCategoryNotificationToUsersOfAccount for accountId : ", accountId);
            Runnable runnableJob = new Runnable() {
                @Override
                public void run() {
                    List<java.util.HashMap> userInfoList = fieldSenseUtils.getListOfUserIdForAccountId(accountId);
                    if(userInfoList.size() > 0) {
                        int count = 0;
                        for (java.util.HashMap userInfo : userInfoList) {
                            String gcmId = (String) userInfo.get("gcmID");
                            int deviceOS = (Integer) userInfo.get("device_os");
                            int appVersion = (Integer) userInfo.get("appVersion");
                            if(appVersion > Integer.parseInt(Constant.APP_VERSION_CHECK)){
                                if(!(gcmId.equals("0") || gcmId.isEmpty())) {

                                    Map<String, Object> m_data = new HashMap<>();
                                    m_data.put("type", "expense");
                                    m_data.put("message", "");
                                    m_data.put("expenseStatus", expenseStatus+""); // 0=new, 1=update, 2=delete
                                    m_data.put("id", expenseCategory.getId()); 
                                    m_data.put("categoryName", expenseCategory.getCategoryName()); 
                                    m_data.put("isActive", expenseCategory.isIsActive());
                                    m_data.put("isModified", "true"); // need to compare with created_on - updated_on, in db/modal field is missing
                                    com.qlc.fieldsense.utils.PushNotificationManager push = new com.qlc.fieldsense.utils.PushNotificationManager();
                                    push.addEditNotification(m_data, gcmId, deviceOS,null);
                                    int userid = (Integer) userInfo.get("id");      
//                                    System.out.println("sendAddEditExpenseCategoryNotificationToUsersOfAccount userid : "+userid+ ", gcmId : "+gcmId+ ", count : " + count++);
                                }
                            }                            
                        }
                    }
                }
            };
            
            Thread sendPushNotification = new Thread(runnableJob);
            sendPushNotification.start();
//            sendPushNotification.join(); // maintain lock like synchronize
//            System.out.println("sendAddEditExpenseCategoryNotificationToUsersOfAccount runnableJob finished : " + runnableJob);
//            System.out.println("=====================================================================================================");
            return runnableJob;
        } catch(Exception e){
            e.printStackTrace();
            LOG4JLOG.log(Level.INFO, " Exception RunnableThreadJob inside sendAddEditExpenseCategoryNotificationToUsersOfAccount for accountId : {0}{1}", new Object[]{accountId, e});
            return null;
        }
    }
    
    /**
     * @param industryStatus
     * @added by Jyoti, 14-01-2018
     * @param industryCategory
     * @param accountId
     * @return
     */
    public Runnable sendAddEditIndustryCategoryNotificationToUsersOfAccount(final int industryStatus, final IndustryCategory industryCategory, final int accountId) {
        try{
            LOG4JLOG.log(Level.INFO, "RunnableThreadJob inside sendAddEditIndustryCategoryNotificationToUsersOfAccount for accountId : ", accountId);
            Runnable runnableJob = new Runnable() {
                @Override
                public void run() {
                    List<java.util.HashMap> userInfoList = fieldSenseUtils.getListOfUserIdForAccountId(accountId);
                    if(userInfoList.size() > 0) {
                        int count = 0;
                        for (java.util.HashMap userInfo : userInfoList) {
                            String gcmId = (String) userInfo.get("gcmID");
                            int deviceOS = (Integer) userInfo.get("device_os");
                            int appVersion = (Integer) userInfo.get("appVersion");
                            if(appVersion > Integer.parseInt(Constant.APP_VERSION_CHECK)){
                                if(!(gcmId.equals("0") || gcmId.isEmpty())) {

                                    Map<String, Object> m_data = new HashMap<String, Object>();
                                    m_data.put("type", "industry");
                                    m_data.put("message", "");
                                    m_data.put("industryStatus", industryStatus+""); // 0=new, 1=update, 2=delete
                                    m_data.put("id", industryCategory.getId()); 
                                    m_data.put("categoryName", industryCategory.getCategoryName()); 
                                    m_data.put("isActive", industryCategory.isIsActive());
                                    m_data.put("isModified", "true"); // need to compare with created_on - updated_on, in db/modal field is missing
                                    com.qlc.fieldsense.utils.PushNotificationManager push = new com.qlc.fieldsense.utils.PushNotificationManager();
                                    push.addEditNotification(m_data, gcmId, deviceOS,null);
                                    int userid = (Integer) userInfo.get("id");      
//                                    System.out.println("sendAddEditIndustryCategoryNotificationToUsersOfAccount userid : "+userid+ ", gcmId : "+gcmId+ ", count : " + count++);
                                }
                            }                            
                        }
                    }
                }
            };
            Thread sendPushNotification = new Thread(runnableJob);
            sendPushNotification.start();
//            sendPushNotification.join(); // maintain lock like synchronize
//            System.out.println("sendAddEditIndustryCategoryNotificationToUsersOfAccount runnableJob finished : " + runnableJob);
//            System.out.println("=====================================================================================================");
            return runnableJob;
        } catch(Exception e){
            e.printStackTrace();
            LOG4JLOG.log(Level.INFO, " Exception RunnableThreadJob inside sendAddEditIndustryCategoryNotificationToUsersOfAccount for accountId : {0}{1}", new Object[]{accountId, e});
            return null;
        }
    }
    
    /**
     * @param formStatus
     * @added by Jyoti, 14-01-2018
     * @param formId
     * @param accountId
     * @return
     */
    public Runnable sendAddEditFormsNotificationToUsersOfAccount(final int formId, final int formStatus, final int accountId) {
        try{
            LOG4JLOG.log(Level.INFO, "RunnableThreadJob inside sendAddEditFormsNotificationToUsersOfAccount for accountId  : ", accountId + " , & formId : "+formId);
            Runnable runnableJob = new Runnable() {
                @Override
                public void run() {
                    List<java.util.HashMap> userInfoList = fieldSenseUtils.getListOfUserIdForAccountId(accountId);
//                    System.out.println("userSize : "+userInfoList.size());
                    if(userInfoList.size() > 0) {
                        for (java.util.HashMap userInfo : userInfoList) {
                            String gcmId = (String) userInfo.get("gcmID");
                            int deviceOS = (Integer) userInfo.get("device_os");
                            int appVersion = (Integer) userInfo.get("appVersion");
                            if(appVersion > Integer.parseInt(Constant.APP_VERSION_CHECK)){
                                if(!(gcmId.equals("0") || gcmId.isEmpty())) {

                                    Map<String, Object> m_data = new HashMap<>();
                                    m_data.put("type", "form");
                                    m_data.put("message", "");
                                    m_data.put("formId", formId);
                                    m_data.put("formStatus", formStatus+"");
                                    m_data.put("isModified", "true");

                                    com.qlc.fieldsense.utils.PushNotificationManager push = new com.qlc.fieldsense.utils.PushNotificationManager();
                                    push.addEditNotification(m_data, gcmId, deviceOS,null);
                                    int userid = (Integer) userInfo.get("id");
//                                    System.out.println("sendAddEditFormsNotificationToUsersOfAccount formId : "+formId +" ,userid : "+userid+ ", gcmId : "+gcmId+ " accountId : "+accountId);
                                }
                            }                            
                        }
                    }
                }
            };
            Thread sendPushNotification = new Thread(runnableJob);
            sendPushNotification.start();
//            sendPushNotification.join(); // maintain lock like synchronize
//            System.out.println("sendAddEditFormsNotificationToUsersOfAccount runnableJob finished : " + runnableJob);
//            System.out.println("=====================================================================================================");
            return runnableJob;
        } catch(Exception e){
            e.printStackTrace();
            LOG4JLOG.log(Level.INFO, " Exception RunnableThreadJob inside sendAddEditFormsNotificationToUsersOfAccount for accountId : {0}{1}", new Object[]{accountId, e});
            return null;
        }
    }
}
