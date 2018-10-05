/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.search.dao;

import com.qlc.fieldsense.customer.model.Customer;
import com.qlc.fieldsense.search.model.SearchCustomerMobile;
import com.qlc.fieldsense.search.model.SearchKeysCustomer;
import com.qlc.fieldsense.search.model.SearchKeysUser;
import com.qlc.fieldsense.stats.model.AccountData;
import com.qlc.fieldsense.team.model.Team;
import com.qlc.fieldsense.user.model.User;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Ramesh
 * @date 12-05-2014
 */
public interface SearchDao {

    /**
     *
     * @param offset
     * @param searchText
     * @param accountId
     * @return
     */
    public List<Customer> searchCustomer(int offset, String searchText, int accountId);

    /**
     *
     * @param searchText
     * @param accountId
     * @return
     */
    public List<SearchCustomerMobile> searchCustomersForMobile(String searchText, int accountId, int userId); // modified by jyoti

    /**
     *
     * @param searchText
     * @param offset
     * @param accountId
     * @return
     */
    public List<SearchCustomerMobile> searchCustomerForMobileWithOffset(String searchText,int offset, int accountId);
            
    /**
     *
     * @param offset
     * @param searchKeys
     * @param accountId
     * @return
     */
    public List<Customer> searchCustomersByLocationAndType(int offset, SearchKeysCustomer searchKeys, int accountId);
    
    /**
     *
     * @param allRequestParams
     * @param searchKeys
     * @param accountId
     * @return
     */
    public List<Customer>  searchCustomersByLocationAndType2( @RequestParam Map<String,String> allRequestParams,SearchKeysCustomer searchKeys, int accountId);
    
    /**
     *
     * @param allRequestParams
     * @param searchKeys
     * @param userId
     * @param accountId
     * @return
     */
    public List<Customer>  searchCustomersByLocationAndType2( @RequestParam Map<String,String> allRequestParams,SearchKeysCustomer searchKeys,int userId, int accountId);
    
    /**
     *
     * @param searchText
     * @param offset
     * @param accountId
     * @return
     */
    public List<User> searchUser(String searchText, int offset, int accountId);

    /**
     *
     * @param allRequestParams
     * @param searchKeys
     * @param accountId
     * @return
     */
    public List<User> searchUserWithFilter(@RequestParam Map<String,String> allRequestParams,SearchKeysUser searchKeys, int accountId);

    /**
     *
     * @param searchText
     * @param accountId
     * @return
     */
    public List<Team> searchTeam(String searchText, int accountId);
    
    /**
     *
     * @param activityfreq
     * @param accountList
     * @return
     */
    public List<AccountData> searchAccActivityFreq(int activityfreq,List accountList);  
}
