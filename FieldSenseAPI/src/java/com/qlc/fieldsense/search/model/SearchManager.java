/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.search.model;

import com.qlc.fieldsense.customer.model.Customer;
import com.qlc.fieldsense.search.dao.SearchDao;
import com.qlc.fieldsense.stats.dao.StatsDao;
import com.qlc.fieldsense.stats.model.AccountData;
import com.qlc.fieldsense.team.model.Team;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Ramesh
 * @date 13-05-2014
 */
public class SearchManager {

    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
    SearchDao searchDao = (SearchDao) GetApplicationContext.ac.getBean("searchDaoImpl");
    StatsDao statsDao = (StatsDao) GetApplicationContext.ac.getBean("statsDaoImpl");
    /**
     * 
     * @param searchText
     * @param offset
     * @param userToken
     * @return details of customer based on text
     * @ purpose Used to search customer by text.
     */
    public Object searchCustomer(int offset, String searchText, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                if (searchText != null || searchText.length() != 0) {
                    int accountId = fieldSenseUtils.accountIdForToken(userToken);
                    List<Customer> customersList = searchDao.searchCustomer(offset, searchText, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Search is succeessfully . ", " Searched Data  ", customersList);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Search text should not be empty . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param searchText
     * @param userToken
     * @return details of customers based on text
     * @purpose Used to search customer from mobile with search text
     */
    public Object searchCustomerForMobile(String searchText, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                if (searchText != null || searchText.length() != 0) {
                    int accountId = fieldSenseUtils.accountIdForToken(userToken);
                    int userId = fieldSenseUtils.userIdForToken(userToken); // added by jyoti
                    List<SearchCustomerMobile> customersList = searchDao.searchCustomersForMobile(searchText, accountId, userId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Search is succeessfully . ", " Searched Data  ", customersList);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Search text should not be empty . ", "", "");
                }
             // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

        
    /**
     * 
     * @param searchText
     * @param offset
     * @param userToken
     * @return details of customers based on text
     * @purpose Used to search customer from mobile with search text with limit
     */
    public Object searchCustomerForMobileWithOffset(String searchText, int offset,String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                if (searchText != null || searchText.length() != 0) {
                    int accountId = fieldSenseUtils.accountIdForToken(userToken);
                    List<SearchCustomerMobile> customersList = searchDao.searchCustomerForMobileWithOffset(searchText,offset, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Search is succeessfully . ", " Searched Data  ", customersList);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Search text should not be empty . ", "", "");
                }
             // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //} 
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
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
    public Object searchCustomersByLocationAndType(@RequestParam Map<String,String> allRequestParams, String userToken,HttpServletResponse response) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                SearchKeysCustomer searckey=new SearchKeysCustomer();
                String recentItem=allRequestParams.get("recentItems").substring(19);
                if(recentItem.equals("")){
                    searckey.setRecentItems(new ArrayList<String>());
                }else{
                    String[] recentItems=recentItem.substring(18).split(",recentItems_value=");
                    List<String> recentItemsList=new ArrayList<String>();
                    for(int i=0;i<recentItems.length;i++){
                        recentItemsList.add(recentItems[i]);
                    }
                    searckey.setRecentItems(recentItemsList);
                }
                String industry=allRequestParams.get("industry").substring(16);
                if(industry.equals("")){
                    searckey.setIndustry(new ArrayList<String>());
                }else{
                    String[] industries=industry.substring(15).split(",industry_value=");
                    List<String> industriesList=new ArrayList<String>();
                    for(int i=0;i<industries.length;i++){
                        industriesList.add(industries[i]);
                    }
                    searckey.setIndustry(industriesList);
                }
                String territory=allRequestParams.get("territory").substring(17);
                if(territory.equals("")){
                    searckey.setTrritory(new ArrayList<String>());
                }else{
                    String[] territories=territory.substring(16).split(",territory_value=");
                    List<String> territoriesList=new ArrayList<String>();
                    for(int i=0;i<territories.length;i++){
                        territoriesList.add(territories[i]);
                    }
                    searckey.setTrritory(territoriesList);
                }
                List<Customer> customersList = searchDao.searchCustomersByLocationAndType2( allRequestParams,searckey, accountId);
                //List<Customer> customersList = searchDao.searchCustomersByLocationAndType(offset, searchKeys, accountId);
                int Total=0;
                if(!customersList.isEmpty()){
                    Total=customersList.get(0).getCustomersCount();
                }
                return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Search is succeessfully . ", " Searched Data  ", customersList,Total);
        // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //} 
        } else {
            response.setContentType( MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
    /**
     * 
     * @param allRequestParams
     * @param userId
     * @param offset
     * @param response
     * @param userToken
     * @return details of customers
     * @purpose Used to search customer by selection.
     */
    public Object searchCustomersByLocationAndType(@RequestParam Map<String,String> allRequestParams, int userId,String userToken,HttpServletResponse response) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                SearchKeysCustomer searckey=new SearchKeysCustomer();
                String recentItem=allRequestParams.get("recentItems").substring(19);
                if(recentItem.equals("")){
                    searckey.setRecentItems(new ArrayList<String>());
                }else{
                    String[] recentItems=recentItem.substring(18).split(",recentItems_value=");
                    List<String> recentItemsList=new ArrayList<String>();
                    for(int i=0;i<recentItems.length;i++){
                        recentItemsList.add(recentItems[i]);
                    }
                    searckey.setRecentItems(recentItemsList);
                }
                String industry=allRequestParams.get("industry").substring(16);
                if(industry.equals("")){
                    searckey.setIndustry(new ArrayList<String>());
                }else{
                    String[] industries=industry.substring(15).split(",industry_value=");
                    List<String> industriesList=new ArrayList<String>();
                    for(int i=0;i<industries.length;i++){
                        industriesList.add(industries[i]);
                    }
                    searckey.setIndustry(industriesList);
                }
                String territory=allRequestParams.get("territory").substring(17);
                if(territory.equals("")){
                    searckey.setTrritory(new ArrayList<String>());
                }else{
                    String[] territories=territory.substring(16).split(",territory_value=");
                    List<String> territoriesList=new ArrayList<String>();
                    for(int i=0;i<territories.length;i++){
                        territoriesList.add(territories[i]);
                    }
                    searckey.setTrritory(territoriesList);
                }
                List<Customer> customersList = searchDao.searchCustomersByLocationAndType2( allRequestParams,searckey, userId,accountId);
                //List<Customer> customersList = searchDao.searchCustomersByLocationAndType(offset, searchKeys, accountId);
                int Total=0;
                if(!customersList.isEmpty()){
                    Total=customersList.get(0).getCustomersCount();
                }
                return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Search is succeessfully . ", " Searched Data  ", customersList,Total);
        // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //} 
        } else {
            response.setContentType( MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    

    /**
     * 
     * @param searchText
     * @param offset
     * @param userToken
     * @return details of user based on text
     * @purpose Used to search user by text. 
     */
    public Object searchUser(String searchText, int offset, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                if (searchText != null || searchText.length() != 0) {
                    int accountId = fieldSenseUtils.accountIdForToken(userToken);
                    List<User> usersList = searchDao.searchUser(searchText, offset, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Search is succeessfully . ", " Searched Data  ", usersList);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Search text should not be empty . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param allRequestParams
     * @param searchKeys
     * @param offset
     * @param response
     * @param userToken
     * @return details of users
     * @purpose Used to search user by selection. 
     */
    public Object searchUserWithFilter(@RequestParam Map<String,String> allRequestParams, String userToken,HttpServletResponse response) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                SearchKeysUser searchkey=new SearchKeysUser();
                String userStatus=allRequestParams.get("userStatus").substring(18);
                if(userStatus.equals("")){
                    searchkey.setStatus(new ArrayList<String>());
                }else{
                    String[] userStatuses=userStatus.split(",");
                    List<String> userStatusList=new ArrayList<String>();
                    for(int i=0;i<userStatuses.length;i++){
                        userStatusList.add(userStatuses[i]);
                    }
                    searchkey.setStatus(userStatusList);
                }
                // jyoti
                String territory=allRequestParams.get("territory").substring(17);
                if(territory.equals("")){
                    searchkey.setTrritory(new ArrayList<String>());
                }else{
                    String[] territories=territory.substring(16).split(",territory_value=");
                    List<String> territoriesList=new ArrayList<String>();
                    for(int i=0;i<territories.length;i++){
                        territoriesList.add(territories[i]);
                    }
                    searchkey.setTrritory(territoriesList);
                }
                
                // ended by jyoti
                List<User> usersList = searchDao.searchUserWithFilter(allRequestParams,searchkey, accountId);
                int Total=0;
                if(!usersList.isEmpty()){
                    Total=usersList.get(0).getUsersCount();
                }
                return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Search is succeessfully . ", " Searched Data  ", usersList,Total);
           // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            response.setContentType( MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param searchText
     * @param userToken
     * @return 
     * @purpose Used to search team with search text which team name  
     */
    public Object searchTeam(String searchText, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                if (searchText != null || searchText.length() != 0) {
                    int accountId = fieldSenseUtils.accountIdForToken(userToken);
                    List<Team> teamssList = searchDao.searchTeam(searchText, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Search is succeessfully . ", " Searched Data  ", teamssList);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Search text should not be empty . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    /*
    public Object searchAccByActivityFrequency(int activityfreq, int offset, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            List<AccountData> accountList = statsDao.selectAccountsData();
            List<AccountData> accDataList = searchDao.searchAccActivityFreq(activityfreq,accountList);
            int recordsize=accDataList.size();
            int end=offset+10;
            if(end >(recordsize)){
                end=recordsize;
            } 
            //System.out.println("offset:"+offset+" end:"+end+" recordsize:"+recordsize);
            accDataList =accDataList.subList(offset, end);
             return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Search is succeessfully . ",""+recordsize, accDataList);
            }
        else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }        
    }*/
}
