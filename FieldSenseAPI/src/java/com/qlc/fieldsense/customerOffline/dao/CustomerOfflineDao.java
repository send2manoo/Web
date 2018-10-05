/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customerOffline.dao;
import com.qlc.fieldsense.appointments.model.Appointment;
import com.qlc.fieldsense.customerOffline.model.AppointmentOffline;
import com.qlc.fieldsense.customerOffline.model.CustomerOffline;
import com.qlc.fieldsense.customerOffline.model.OfflineDataSync;
import com.qlc.fieldsense.customerOffline.model.CustomerOfflineWithFewData;
import com.qlc.fieldsense.customerOffline.model.CustomersOfflineRequestedList;
import com.qlc.fieldsense.customerOffline.model.LocalData;
import com.qlc.fieldsense.customerOffline.model.TerritoryList;
import com.qlc.fieldsense.expense.model.Expense;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author jyoti
 */
public interface CustomerOfflineDao {

//    public List<CustomerOfflineWithFewData> selectCustomersOffline(int accountId, int userId);
 
    /**
     *
     * @param accountId
     * @param userId
     * @param customerOfflineWithFewData
     * @return
     */
     
    public List<CustomerOfflineWithFewData> selectRequestedListOfCustomersOffline(int accountId, int userId, List<CustomersOfflineRequestedList> customerOfflineWithFewData);     // Added by Jyoti 11-01-2017

    /**
     *
     * @param accountId
     * @param userId
     * @param lastSync
     * @param currentTime
     * @return
     */
    public List<CustomerOffline> selectCustomersOfflineAfterLastSync(int accountId, int userId, Timestamp lastSync,Timestamp currentTime);
    
    
     public List<CustomerOffline> selectCustomersBasedOnNewAddedTerriorty(int accountId, int userId, Timestamp lastSync,Timestamp currentTime);
    
    /**
     *
     * @param offlineDataSync
     * @param accountId
     * @return
     */
    public boolean insertOfflineDataCustomer(OfflineDataSync offlineDataSync, int accountId);
    
    /**
     *
     * @param offlineDataSync
     * @param accountId
     * @return
     */
    public boolean insertOfflineDataAppointment(OfflineDataSync offlineDataSync, int accountId);
    
    /**
     *
     * @param offlineDataSync
     * @param accountId
     * @return
     */
    public List<CustomerOffline> getAllOfflineCustomers(OfflineDataSync offlineDataSync ,int accountId);
    
    /**
     *
     * @param offlineDataSync
     * @param accountId
     * @return
     */
    public List<Appointment> getAllOfflineAppointments(OfflineDataSync offlineDataSync ,int accountId);
    
    /**
     *
     * @param offlineDataSync
     * @param accountId
     * @param userId
     * @return
     */
    public List<HashMap<String,String>> getAllOfflineData(OfflineDataSync offlineDataSync,int accountId,int userId);
    
    /**
     *
     * @param OffflineData
     * @param accountId
     * @param userId
     * @return
     * @throws Exception
     */
    public int insertCustomers(LocalData OffflineData,int accountId,int userId) throws Exception;
    
    /**
     *
     * @param appointmentOffline
     * @param accountId
     * @param userId
     * @param customerId
     * @return
     */
    public int insertAppointments(AppointmentOffline appointmentOffline,int accountId,int userId,int customerId);
    
    /**
     *
     * @param offlineData
     * @param accoutId
     * @param userId
     * @param customerId
     * @return
     */
    public boolean updateCustomers(LocalData offlineData,int accoutId,int userId,int customerId);
    
    /**
     *
     * @param OfflinneData
     * @param accoutId
     * @param userId
     * @param customerId
     * @return
     */
    public Timestamp checkCustomerLastUpdateTimeStamp(LocalData OfflinneData,int accoutId,int userId,int customerId);
            
    /**
     *
     * @param appointmentOffline
     * @param accoutId
     * @param userId
     * @param customerId
     * @param appointmentId
     * @return
     */
    public boolean updateAppointment(AppointmentOffline appointmentOffline,int accoutId,int userId,int customerId,int appointmentId);
     
    /**
     *
     * @param appointmentOffline
     * @param accoutId
     * @param userId
     * @param customerId
     * @param appointmentId
     * @return
     */
    public boolean updateAppointmentForCheckInCheckOut(AppointmentOffline appointmentOffline,int accoutId,int userId,int customerId,int appointmentId);
    
    /**
     *
     * @param expense
     * @param accountId
     * @param userId
     * @param customerId
     * @param appointmentId
     * @param mobileAppointmentId
     * @return
     */
    public int insertExpense(Expense expense,int accountId,int userId,int customerId,int appointmentId,String mobileAppointmentId);
    
    /**
     *
     * @param accountId
     * @param customerId
     * @return
     */
    public int getRecordStatusForCustomer(int accountId,int customerId);
    
    /**
     *
     * @param tempId
     * @param accountId
     * @return
     */
    public int isTempIdPresentForCustomer(String tempId,int accountId);

    /**
     *
     * @param tempId
     * @param accountId
     * @return
     */
    public int isTempIdPresentForAppointment(String tempId,int accountId);
    
    /**
     *
     * @param tempId
     * @param accountId
     * @return
     */
    public int isTempIdPresentForExpense(String tempId,int accountId);
    
    /**
     *
     * @param customerOfflineWithFewData
     * @param accountId
     * @param userId
     * @return
     */
    public  List<Integer> terriortyList(CustomerOfflineWithFewData customerOfflineWithFewData,int accountId,int userId);
    
    /**
     *
     * @param deletedTerriorty
     * @param accountId
     * @param userId
     * @return
     */
    public  List<Integer> removedCustomerList(List<Integer> deletedTerriorty,int accountId,int userId);
    
    /**
     *
     * @param accountId
     * @param userId
     * @param lastSync
     * @param currentTime
     * @return
     */
    public List<CustomerOffline> selectCustomersOfflineAfterLastSyncForFirstSync(int accountId, int userId, Timestamp lastSync,Timestamp currentTime);
    
    /**
     * @param customerId
     * @added by jyoti, #31015
     * @param accountId
     * @param customerTmpId
     * @return 
     */
    public int updateUserTravelLogCustomerIdWithTmp(int accountId, String customerTmpId, int customerId);
       
}
