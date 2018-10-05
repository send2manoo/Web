/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.stats.service;

import com.qlc.fieldsense.stats.model.Stats;
import com.qlc.fieldsense.stats.model.StatsManager;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
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
 * @date 25-04-2015
 */
@Controller
@RequestMapping("/stats")
public class StatsService {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("StatsService");
    StatsManager statsManager = new StatsManager();

    /**
     * 
     * @param allRequestParams
     * @param offset
     * @param userToken
     * @return 
     * @purpose Used to get statistics of accounts. 
     */
    @RequestMapping(value = "/accountsData/offset/", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getAccountsData(@RequestParam Map<String,String> allRequestParams, @RequestHeader(value = "userToken") String userToken) {
        log4jLog.info("Inside StatsService class getAccountsData() method ");
//        System.out.println("getacountdata%%%");
        return statsManager.selectAccountsData(allRequestParams, userToken);
    }

    /**
     * 
     * @param offset
     * @param filter
     * @param userToken
     * @purpose Used to get statistics of active accounts or inactive accounts. In URL filter values is required.0=inactive account,1=active accounts In URL offset is required. 
     */
    /*
    @RequestMapping(value = "/accountsData/filter/{filterValue}/offset/{offset}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getAccountsDataWithFilter(@PathVariable("offset") int offset, @PathVariable("filterValue") int filter, @RequestHeader(value = "userToken") String userToken) {
        log4jLog.info("Inside StatsService class getAccountsDataWithFilter() method ");
        return statsManager.selectAccountsDataWithFilter(filter, offset, userToken);
    }
*/
    /**
     * 
     * @param accountId
     * @param fromDate
     * @param toDate
     * @param offset
     * @param userToken
     * @return 
     * @purpose Used to search account on bases of account name. 
     */
    @RequestMapping(value = "/account/{accountId}/{fromDate}/{toDate}/{offset}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getAccountStats(@PathVariable("accountId") int accountId, @PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @PathVariable("offset") int offset, @RequestHeader(value = "userToken") String userToken) {
        log4jLog.info("Inside StatsService class getAccountStats() method ");
        return statsManager.selectAccountStats(fromDate, toDate, accountId, offset, userToken);
    }
    //nikhil : to get details of user
      @RequestMapping(value = "/account/user/mobileno", method = RequestMethod.GET)
    public
    @ResponseBody
    Object getUserStats(@RequestParam Map<String,String> allRequestParams,@RequestHeader(value = "userToken") String userToken) {
        log4jLog.info("Inside StatsService class getUserStats() method ");        
        return statsManager.selectUserStats(allRequestParams,userToken);
    }
    

    /**
     * Added by siddhesh for search user data in new super user panel.29-06-17 #
     * @param offset
     * @param userToken
     * @purpose Used to get searched user data.
     */
    @RequestMapping(value = "/user/searchData/", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getUsersListDataWithSearch(@RequestParam Map<String,String> allRequestParams, @RequestHeader(value = "userToken") String userToken) {
        log4jLog.info("Inside StatsService class getAccountsData() method ");
        return statsManager.selectUsers(allRequestParams, userToken);
    }
    
    /**
     * Added by manohar mar 23 2018
     * @param stats
     * @param userToken
     * @return 
     */
    @RequestMapping(value = "/Download/get_Log_Files", method = RequestMethod.PUT,headers = "Accept=application/json")
    public
    @ResponseBody
    Object getLogs(@RequestBody Stats stats , @RequestHeader(value = "userToken") String userToken) {
        return statsManager.getLogs(stats,userToken);
    }

    /**
     * @param userToken
     * @added by jyoti, 27-july-2018
     * @param fromDate
     * @param toDate
     * @return 
     */
    @RequestMapping(value = "/diy/getDiy4StepsData/{fromDate}/{toDate}", method = RequestMethod.GET, headers = "Accept=application/json")
    public @ResponseBody
    Object getDiy4StepsData(@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @RequestHeader(value = "userToken") String userToken) {
        return statsManager.getDiy4StepsData(fromDate, toDate, userToken);
    }
    
}
