




/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.dashboard.dao;

import com.qlc.fieldsense.dashboard.model.AppointmentStats;
import com.qlc.fieldsense.dashboard.model.AttendanceStats;
import com.qlc.fieldsense.dashboard.model.AttendanceStatsUsers;
import com.qlc.fieldsense.dashboard.model.CustomFormStats;
import com.qlc.fieldsense.dashboard.model.CustomerStats;
import com.qlc.fieldsense.dashboard.model.ExpenseStats;
import com.qlc.fieldsense.dashboard.model.LeaderboardStats;
import java.util.List;

/**
 *
 * @author root
 */
public interface DashboardStatsDao {
    public List<AppointmentStats> selectAppointmentStats(int accountId,int teamId);
    public List<AttendanceStatsUsers> selectAttendanceStatsUsers(int accountId,int teamId,int userId); 
    public List<AttendanceStats> selectAttendanceStats(int accountId,int teamId); 
    public List<ExpenseStats> selectExpenseStats(int accountId,int teamId); 
    public List<LeaderboardStats> selectLeaderboardStats(int accountId,int teamId); 
    public List<CustomerStats> selectCustomerStats(int accountId,int teamId); 
    public List<CustomFormStats> selectFormFilledStats(int accountId,int teamId); 
}
