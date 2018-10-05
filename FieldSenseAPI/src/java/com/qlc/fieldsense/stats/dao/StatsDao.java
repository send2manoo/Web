/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.stats.dao;

import com.qlc.fieldsense.stats.model.AccountData;
import com.qlc.fieldsense.stats.model.AccountUsers;
import com.qlc.fieldsense.stats.model.DIYData;
import com.qlc.fieldsense.stats.model.Stats;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Ramesh
 * @date 21-04-2015
 */
public interface StatsDao {

    /**
     *
     * @param date
     * @param accountId
     * @return
     */

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
//    public Stats defaultPassword(int id, int accountId); commented bu siddhesh as code is not required.

    public Stats getAccountWaiseStatDataDaily(String date, int accountId);

    /**
     *
     * @param stat
     * @return
     */
    public int inserIntoStatsTable(Stats stat);

    /**
     *
     * @param fromDate
     * @param toDate
     * @param accountId
     * @param offset
     * @return
     */
    public List<Stats> selectAccountStats(String fromDate, String toDate, int accountId, int offset);

    /**
     *
     * @param AllRequestparam
     * @return
     */
    public List<AccountData> selectAccountsData(@RequestParam Map<String,String> AllRequestparam);
    
    public List<AccountUsers> selectUsersData(@RequestParam Map<String,String> AllRequestparam);

    /**
     *
     * @param accountId
     * @return
     */
    public boolean isStatRecordAvailableForTheDate(int accountId);
       
    /**
     *
     * @return
     */
    public int getAccountCount();

    public List<Stats> selectUserStats(@RequestParam Map<String,String> allRequestParams);
    
    public List<DIYData> getDiy4StepsData(String fromDate, String toDate); // added by jyoti, 30-07-2018
    
}
