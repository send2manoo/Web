/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.search.service;

import com.qlc.fieldsense.search.model.SearchKeysCustomer;
import com.qlc.fieldsense.search.model.SearchKeysUser;
import com.qlc.fieldsense.search.model.SearchManager;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Ramesh
 * @date 13-05-2014
 */
@Controller
@RequestMapping("/search")
public class SearchService {

    SearchManager searchManager = new SearchManager();

    /**
     * 
     * @param searchText
     * @param offset
     * @param userToken
     * @return details of customer based on text
     * @ purpose Used to search customer by text.
     */
    @RequestMapping(value = "/customers/{searchText}/{offset}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object seachCustomersBySearckText(@PathVariable("searchText") String searchText, @PathVariable("offset") int offset, @RequestHeader(value = "userToken") String userToken) {
        return searchManager.searchCustomer(offset, searchText, userToken);
    }

    /**
     * //for old app versions
     * @param searchText
     * @param userToken
     * @return details of customers based on text
     * @purpose Used to search customer from mobile with search text
     */
    @RequestMapping(value = "/customers/mobile/{searchText}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object seachCustomersBySearchTextForMobile(@PathVariable("searchText") String searchText, @RequestHeader(value = "userToken") String userToken) {
        return searchManager.searchCustomerForMobile(searchText, userToken);
    }
    
    /**
     * 
     * //Added by siddhesh 
     * @param searchText
     * @param userToken
     * @return details of customers based on text
     * @purpose Used to search customer from mobile with search text
     */
    @RequestMapping(value = "/customers/mobile/{searchText}/list", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object seachCustomersBySearchTextForMobileNew(@PathVariable("searchText") String searchText, @RequestHeader(value = "userToken") String userToken) {
        return searchManager.searchCustomerForMobile(searchText, userToken);
    }
    
    /**
     * 
     * @param searchText
     * @param offset
     * @param userToken
     * @return details of customers based on text
     * @purpose Used to search customer from mobile with search text with offset
     */
    @RequestMapping(value = "/customers/mobile/{searchText}/{offset}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object seachCustomersBySearchTextForMobile(@PathVariable("searchText") String searchText,@PathVariable("offset") int offset, @RequestHeader(value = "userToken") String userToken) {
        return searchManager.searchCustomerForMobileWithOffset(searchText, offset,userToken);
    }

    /**
     * 
     * @param allRequestParams
     * @param searchKeys
     * @param response
     * @param userToken
     * @return details of customers
     * @purpose Used to search customer by selection.
     */
    @RequestMapping(value = "/customers/filtering", method = RequestMethod.GET)
    public
    @ResponseBody
    Object searchCustomersByLocationAndType(@RequestParam Map<String,String> allRequestParams,@RequestHeader(value = "userToken") String userToken,HttpServletResponse response) {
        return searchManager.searchCustomersByLocationAndType(allRequestParams,userToken,response);
    }
    
    /**
     * 
     * @param allRequestParams
     * @param userId
     * @param searchKeys
     * @param userToken
     * @param response
     * @return details of customers
     * @purpose Used to search customer by selection.
     */
    @RequestMapping(value = "/customers/filtering/{userId}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object searchCustomersByLocationAndType(@RequestParam Map<String,String> allRequestParams,@PathVariable int userId,@RequestHeader(value = "userToken") String userToken,HttpServletResponse response) {
        return searchManager.searchCustomersByLocationAndType(allRequestParams,userId,userToken,response);
    }

    /**
     * 
     * @param searchText
     * @param offset
     * @param userToken
     * @return details of user based on text
     * @purpose Used to search user by text. 
     */
    @RequestMapping(value = "/user/{searchText}/{offset}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object searchUser(@PathVariable("searchText") String searchText, @PathVariable("offset") int offset, @RequestHeader(value = "userToken") String userToken) {
        return searchManager.searchUser(searchText, offset, userToken);
    }

    /**
     * 
     * @param allRequestParams
     * @param offset
     * @param response
     * @param userToken
     * @return details of users
     * @purpose Used to search user by selection. 
     */
    @RequestMapping(value = "/user/filtering",  method = RequestMethod.GET)
    public
    @ResponseBody
    Object searchUsersWithFilter(@RequestParam Map<String,String> allRequestParams,  @RequestHeader(value = "userToken") String userToken,HttpServletResponse response) {
        return searchManager.searchUserWithFilter(allRequestParams, userToken,response);
    }

    /**
     * 
     * @param searchText
     * @param userToken
     * @return 
     * @purpose Used to search team with search text which team name  
     */
    @RequestMapping(value = "/team/{searchText}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object searchTeam(@PathVariable("searchText") String searchText, @RequestHeader(value = "userToken") String userToken) {
        return searchManager.searchTeam(searchText, userToken);
    }
    
    /*
    @RequestMapping(value = "/accounts/{activityFrequency}/{offset}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object searchAccountsByActivityFreq(@PathVariable("activityFrequency") int activityFrequency,@PathVariable("offset") int offset, @RequestHeader(value = "userToken") String userToken) {
        return searchManager.searchAccByActivityFrequency(activityFrequency, offset, userToken);
    }*/
}
