/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.dashboard.model;

import com.qlc.fieldsense.dashboard.dao.DashboardStatsDao;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author root
 */
public class DashboardManager {
    
    
        DashboardStatsDao dashboardStatsDao = (DashboardStatsDao) GetApplicationContext.ac.getBean("dashboardStatsDaoImpl");
        FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
        public Object selectDashboardData(String userToken,int teamId) {
            if (fieldSenseUtils.isTokenValid(userToken)) {
//                System.out.println("teamId="+teamId);
                //if(fieldSenseUtils.isSessionExpired(userToken)){
                    int accountId = fieldSenseUtils.accountIdForToken(userToken);
                    int userId=fieldSenseUtils.userIdForToken(userToken);    
                    List<AppointmentStats> appointmentstats=null;
                    List<AttendanceStatsUsers> attendancestatsUsers=null;
                    List<AttendanceStats> attendancestats=null;
                    List<ExpenseStats> expensestats=null;
                    List<LeaderboardStats> leaderboardstats=null;
                    List<CustomerStats> customerstats=null;
                    List<CustomFormStats> formfilledstats=null;
                    
                    List<Object> allList=new ArrayList<Object>();
                    
                    
                    try {
                            appointmentstats=dashboardStatsDao.selectAppointmentStats(accountId, teamId);
//                            System.out.println("appointmentstats.sixe()="+appointmentstats.size());
                            
                            attendancestatsUsers=dashboardStatsDao.selectAttendanceStatsUsers(accountId, teamId,userId);
//                            System.out.println("attendancestatsUsers.sixe()="+attendancestatsUsers.size());
                            
                            attendancestats=dashboardStatsDao.selectAttendanceStats(accountId, teamId);
//                            System.out.println("attendancestats.sixe()="+attendancestats.size());
                            
                            expensestats=dashboardStatsDao.selectExpenseStats(accountId, teamId);
//                            System.out.println("expensestats.sixe()="+expensestats.size());
                            leaderboardstats=dashboardStatsDao.selectLeaderboardStats(accountId, teamId);
//                             System.out.println("leaderboardstats.sixe()="+leaderboardstats.size());
                             customerstats=dashboardStatsDao.selectCustomerStats(accountId, teamId);
//                             System.out.println("customerstats.sixe()="+customerstats.size());
                             formfilledstats=dashboardStatsDao.selectFormFilledStats(accountId, teamId);
//                             System.out.println("formfilledstats.sixe()="+formfilledstats.size());
//                             
//                             for(CustomerStats cs:customerstats)
//                             {
//                                 System.out.println("cs="+cs.getCountofCustomer());
//                             }
                             
                            allList.add(appointmentstats);
                            allList.add(attendancestatsUsers);
                            allList.add(attendancestats);
                            allList.add(expensestats);
                            allList.add(leaderboardstats);
                            allList.add(customerstats);
                            allList.add(formfilledstats);
//                            System.out.println("allList="+allList.size());
                    } 
                    catch (Exception e) {
                        e.printStackTrace();
                    }                   
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " appointmentstats   ", allList);

            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
            }
        }   
      
}
