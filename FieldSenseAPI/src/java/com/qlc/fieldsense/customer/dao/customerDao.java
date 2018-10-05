/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customer.dao;

import com.qlc.fieldsense.customer.model.Customer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ramesh
 * @date 28-01-2014 .
 * @purpouse To manipulate customer related database operations .
 */
public interface customerDao {

    /**
     *
     * @param customer
     * @param accountId
     * @return
     */
    public int insertCustomer(Customer customer, int accountId);

    /**
     *
     * @param customerId
     * @param accountId
     * @return
     */
    public Customer selectCustomer(int customerId, int accountId);

    /**
     *
     * @param userId
     * @param customerId
     * @param accountId
     * @return
     */
    public Customer selectCustomerWithLocatio(int userId, int customerId, int accountId);

    /**
     *
     * @param accountId
     * @return
     */
    public List<Customer> selectCustomers(int accountId);
    
    /**
     * 
     * @param accountId
     * @param userId
     * @return 
     */
    public List<String> selectCustomerNames(int accountId, int userId);

    /**
     *
     * @param accountId
     * @param searchText
     * @return
     */
    public List<Map> selectCustomersInVisit(int accountId,String searchText);
    
    /**
     * @Added by jyoti
     * @param userId
     * @param accountId
     * @param searchText
     * @return
     */
    public List<Map> selectCustomersOnlyWhoHaveVisit(int userId, int accountId, String searchText);
   
    /**
     *
     * @param customerId
     * @param accountId
     * @return
     */
    public Customer deleteCustomer(int customerId, int accountId);

    /**
     *
     * @param customer
     * @param accountId
     * @return
     */
    public Customer updateCustomer(Customer customer, int accountId);

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    public boolean isCustomerValid(int id, int accountId);

    /**
     *
     * @param customerName
     * @param accountId
     * @return
     */
    public int getCustomerId(String customerName, int accountId);

    /**
     *
     * @param customer
     * @param accountId
     * @return
     */
    public Customer updateCustomerAddress(Customer customer, int accountId);

    /**
     *
     * @param recentId
     * @param accountId
     * @return
     */
    public List<Customer> selectRecentCustomers(int recentId, int accountId);

    /**
     *
     * @param customerName
     * @param locationIdentifier
     * @param accountId
     * @return
     */
    public int getCustomerIdOnNmLocation(String customerName, String locationIdentifier, int accountId);

    /**
     *
     * @param customer
     * @param accountId
     * @param userid
     * @return
     */
    public int insertCustomerByImport(Customer customer, int accountId,int userid);

    /**
     *
     * @param customerId
     * @param accountId
     * @return
     */
    public String selectCustomerAddress(int customerId, int accountId);

    /**
     *
     * @param accountId
     * @return
     */
    public List<String> selectCustomerIndustry(int accountId);

    /**
     *
     * @param accountId
     * @return
     */
    public List<String> selectCustomerTerritory(int accountId);

    /**
     *
     * @param allRequestParams
     * @param accountId
     * @return
     */
    public List<Customer> selectCustomersWithLimt(Map<String,String> allRequestParams, int accountId);
    
    /**
     *
     * @param allRequestParams
     * @param userId
     * @param accountId
     * @return
     */
    public List<Customer> selectUserCustomersWithLimt(Map<String,String> allRequestParams,int userId, int accountId);

    /**
     *
     * @param customer
     * @param accountId
     * @return
     */
    public Customer updateCustomerAddressLatLong(Customer customer, int accountId);

    /**
     *
     * @param customer
     * @param accountId
     * @return
     */
    public boolean updateCustomerAddressOnly(Customer customer, int accountId);

    /**
     *
     * @param customerName
     * @param customerLocation
     * @param accountId
     * @return
     */
    public boolean isCustomerExistWithLocation(String customerName, String customerLocation, int accountId);
        
    /**
     *
     * @param territory
     * @param accountId
     * @return
     */
    public boolean isTerritoryExists(String territory,int accountId);  

    public List<HashMap <String,Object>> getNearByCustomers(double lat,double lang,int radius,int accountId);
}
