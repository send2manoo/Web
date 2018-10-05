/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customerOffline.service;

import com.qlc.fieldsense.customerOffline.model.CustomerOfflineManager;
import com.qlc.fieldsense.customerOffline.model.CustomerOfflineWithFewData;
import com.qlc.fieldsense.customerOffline.model.OfflineDataSync;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author jyoti
 * @date 21-12-2016
 * @purpose To perform offline related services .
 */

@Controller
@RequestMapping("/offline")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerOfflineService {
    
    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("CustomerOfflineService");
    private CustomerOfflineManager customerOfflineManager = new CustomerOfflineManager();
   
    /**
     * 
     * @param userToken
     * @return customer list after last sync offline
     */
//    @RequestMapping(value = "/customersListAfterLastSync/{lastSyncDateTime}", method = RequestMethod.GET, headers = "Accept=application/json")
//    public
//    @ResponseBody
//    Object selectAfterLastSyncCustomersOffline(@PathVariable String lastSyncDateTime,@RequestHeader(value = "userToken") String userToken) {
//        return customerOfflineManager.getAfterLastSyncCustomersOffline(lastSyncDateTime, userToken);
//    }

    /**
     * 
     * @param customerOfflineList
     * @param userToken
     * @return send requested list of customers offline to mobile
     */
    @RequestMapping(value = "/requestedListOfCustomers", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectRequestedListOfCustomersOffline(@RequestBody CustomerOfflineWithFewData customerOfflineList, @RequestHeader(value = "userToken") String userToken) {
        return customerOfflineManager.getRequestedListOfCustomersOffline(customerOfflineList, userToken);
    }
    
     /**
     * 
     * @param offlineDataSync
     * @param userToken
     * @return 
     * @purpose Used to get list of customers and appointment data and insert into database
     */
    @RequestMapping(value = "/insertOfflineMobileDataBatch", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object insertOfflineMobileData(@RequestBody OfflineDataSync offlineDataSync, @RequestHeader(value = "userToken") String userToken) {
        return customerOfflineManager.insertCustomerOfflineData(offlineDataSync,userToken);
    }
    
     
     /**
     * 
     * @param offlineDataSync
     * @param userToken
     * @return 
     * @purpose Used to get list of customers and appointment data and insert into database
     */
    @RequestMapping(value = "/initSync", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object insertOfflineMobileDataWithoutBatchV1(@RequestBody OfflineDataSync offlineDataSync, @RequestHeader(value = "userToken") String userToken) {
        return customerOfflineManager.insertCustomerData1(offlineDataSync,userToken);
    }
    
    
    
     /**
     * 
     * @param offlineDataSync
     * @param userToken
     * @return 
     * @purpose Used to get list of customers and appointment data and insert into database
     */
    @RequestMapping(value = "/initSyncV2", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object insertOfflineMobileDataWithoutBatchV2(@RequestBody OfflineDataSync offlineDataSync, @RequestHeader(value = "userToken") String userToken) {
        return customerOfflineManager.insertCustomerData1Map(offlineDataSync,userToken);
    }
    
    
     /**
     * 
     * @param customerOfflineWithFewData
     * @param customer and appointments list
     * @param userToken
     * @return 
     * @purpose Used to get list of customers and appointment data and insert into database
     */
    @RequestMapping(value = "/removedTerritoryCustomerList", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object removedTerritoryCustomerList(@RequestBody CustomerOfflineWithFewData customerOfflineWithFewData, @RequestHeader(value = "userToken") String userToken) {
        return customerOfflineManager.removedTerritoryCustomerList(customerOfflineWithFewData,userToken);
    }
    
    
}
