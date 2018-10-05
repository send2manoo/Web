/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.utils;

import com.qlc.fieldsense.stats.dao.StatsDao;
import com.qlc.fieldsense.stats.model.Stats;
import java.util.List;
import java.util.TimerTask;
import org.apache.log4j.Logger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Ramesh
 * @date 23-04-2015
 *
 */
public class GenarateStats extends TimerTask {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("GenarateStats");
    StatsDao statsDao = (StatsDao) GetApplicationContext.ac.getBean("statsDaoImpl");
    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");

    @Override
    public void run() {
        genarateAllAccountsStatsDaily();

    }

    /**
     *
     */
    public void genarateAllAccountsStatsDaily() {
        log4jLog.info("Inside FieldSenseStatsUtils class genarateAllAccountsStatsDaily method");
        synchronized (this) {
            List<Integer> accountsList = fieldSenseUtils.getAllAccountIds();
            for (int i = 0; i < accountsList.size(); i++) {
                int accountId = accountsList.get(i);
                String DATE_FORMAT_NOW = "yyyy-MM-dd";
                Date date1 = new Date();
                Date date = new Date(date1.getDate() - 1);
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
                String stringDate = sdf.format(date);
                Stats fieldSenseStat = statsDao.getAccountWaiseStatDataDaily(stringDate, accountId);
                fieldSenseStat.setAccountId(accountId);
                fieldSenseStat.setStatYear(date.getYear() + 1900);
                fieldSenseStat.setStatMonth(date.getMonth() + 1);
                fieldSenseStat.setStatDay(date.getDate());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int week = cal.get(Calendar.WEEK_OF_YEAR);
                fieldSenseStat.setStatWeek(week);
                if (statsDao.isStatRecordAvailableForTheDate(accountId)) {
                    statsDao.inserIntoStatsTable(fieldSenseStat);
                }
            }
        }
    }
}
