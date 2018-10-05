/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.report.dao;

import com.qlc.fieldsense.appointments.model.Appointment;
import com.qlc.fieldsense.expense.model.Expense;
import com.qlc.fieldsense.report.model.Attendance;
import com.qlc.fieldsense.report.model.MonthlyAttendanceMuster;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.usersTravelLogs.model.UsersTravelLogs;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Ramesh
 * @date 08-08-2014
 */
public interface ReportsDao {

    /**
     *
     * @param userId
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    public List<Attendance> selectAttendaceReport(int userId, String fromDate, String toDate, int accountId);

    /**
     *
     * @param userId
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    public List<Expense> selectExpenseReport(int userId, String fromDate, String toDate, int accountId);
    
    /**
     *
     * @param allRequestParams
     * @param userId
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    public List<Expense> selectAccountExpenseReport(@RequestParam Map<String,String> allRequestParams,int userId, String fromDate, String toDate, int accountId);
    
    /**
     *
     * @param allRequestParams
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    public List<Expense> selectAllAccountExpenseReport(@RequestParam Map<String,String> allRequestParams,String fromDate, String toDate, int accountId);

    /**
     *
     * @param userId
     * @param allRequestParams
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    public List<Appointment> selectVistReport(int userId,List<User> userList, Map<String,String> allRequestParams,String fromDate, String toDate, int accountId);
    
    public List<Appointment> selectVisitReportForAdminCsv(int userId,List<User> userList, Map<String,String> allRequestParams,String fromDate, String toDate, int accountId);

    /**
     *
     * @param userId
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    public List<Appointment> selectVistReportCsv(int userId,List<User> subordinateList, String fromDate, String toDate, int accountId);

    /**
     *
     * @param allRequestParams
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    List<Attendance> selectAttendaceReportForAdmin(Map<String,String> allRequestParams,String fromDate, String toDate, int accountId);

    /**
     *
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    List<Attendance> selectAttendaceReportCsvForAdmin(String fromDate, String toDate, int accountId);

    /**
     *
     * @param userId
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    public List<UsersTravelLogs> selectUsersTravelledReoprt(int userId, String fromDate, String toDate, int accountId);

    /**
     *
     * @param travelUserId
     * @param allRequestParams
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    List<java.util.HashMap> selectMonthlyTravelReoprt(int travelUserId,Map<String,String> allRequestParams,String fromDate, String toDate, int accountId);

    /**
     *
     * @param subUserList
     * @param travelUserId
     * @param allRequestParams
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    List<java.util.HashMap>selectMonthlyTravelReoprtForSubordinate(ArrayList<Integer>subUserList,int travelUserId,Map<String,String> allRequestParams,String fromDate, String toDate, int accountId);
    
    /**
     *
     * @param travelUserId
     * @param timeZoneOffset
     * @param allRequestParams
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    public List<HashMap> dayWiseMonthlyTravelReoprt(int travelUserId,String timeZoneOffset,Map<String,String> allRequestParams,String fromDate, String toDate, int accountId);

    /**
     *
     * @param travelUserId
     * @param idList
     * @param timeZoneOffset
     * @param allRequestParams
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    public HashMap<Integer,List<HashMap>> dayWiseMonthlyTravelReoprtNew(int travelUserId,ArrayList<Integer> idList,String timeZoneOffset,Map<String,String> allRequestParams,String fromDate, String toDate, int accountId);

    /**
     *
     * @param travelEntry
     * @param accountId
     * @return
     */
    boolean isTravelEntryPresentInAttendance(UsersTravelLogs travelEntry,int accountId);
    
    public List<MonthlyAttendanceMuster> generateMonthlyAttendanceMuster(int month,int year,int accountId);
    
//    public HashMap<Integer,List<HashMap>> dayWiseMonthlyTravelReportGroupBy(int travelUserId,ArrayList<Integer> idList,String timeZoneOffset,Map<String,String> allRequestParams,String fromDate, String toDate, int accountId);
    
      public LinkedHashMap<Object,Object> getDayWiseMonthlyReport(int travelUserId,String fromDate, String toDate, int accountId ,String timeZoneOffset);
    
}
