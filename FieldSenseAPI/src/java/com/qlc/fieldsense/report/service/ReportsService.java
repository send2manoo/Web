/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.report.service;

import com.qlc.fieldsense.report.model.ReportsManager;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Ramesh
 * @date 08-08-2014
 */
@Controller
@RequestMapping("/reports")
public class ReportsService {

    ReportsManager reportsManager = new ReportsManager();
    
    /**
     * 
     * @param userId
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return attendance report of specific user for specific time
     */
    @RequestMapping(value = "/attendance/{userId}/{fromDate}/{toDate}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object getAttendanceReport(@PathVariable("userId") int userId, @PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @RequestHeader(value = "userToken") String userToken) {
        return reportsManager.attendanceReport(userId, fromDate, toDate, userToken);
    }

    /**
     * 
     * @param userId
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return expense report of specific user for specific time
     */
    @RequestMapping(value = "/expense/{userId}/{fromDate}/{toDate}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object getExpenseReport(@PathVariable("userId") int userId, @PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @RequestHeader(value = "userToken") String userToken) {
        return reportsManager.expenseReport(userId, fromDate, toDate, userToken);
    }
    
    /**
     * 
     * @param allRequestParams
     * @param userId
     * @param fromDate
     * @param toDate
     * @param userToken
     * @param response
     * @return expense report of specific user for specific time
     */
    @RequestMapping(value = "/expense/Account/{userId}/{fromDate}/{toDate}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object getAccountExpenseReport(@RequestParam Map<String,String> allRequestParams,@PathVariable("userId") int userId, @PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @RequestHeader(value = "userToken") String userToken,HttpServletResponse response) {
        return reportsManager.accountExpenseReport(allRequestParams,userId, fromDate, toDate, userToken,response);
    }
    /**
     * 
     * @param allRequestParams
     * @param userId
     * @param fromDate
     * @param toDate
     * @param response
     * @param userToken
     * @return expense report of specific user for specific time
     */
    @RequestMapping(value = "/expense/Account/All/{fromDate}/{toDate}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object getAllAccountExpenseReport(@RequestParam Map<String,String> allRequestParams,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @RequestHeader(value = "userToken") String userToken,HttpServletResponse response) {
        return reportsManager.allAccountExpenseReport(allRequestParams,fromDate, toDate, userToken,response);
    }
    

    /**
     * 
     * @param userId
     * @param allRequestParams
     * @param fromDate
     * @param toDate
     * @param userToken
     * @param response
     * @return visit report of specific user for specific time
     */
    @RequestMapping(value = "/visit/{userId}/{fromDate}/{toDate}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object getVisitReport(@PathVariable("userId") int userId,@RequestParam Map<String,String> allRequestParams, @PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @RequestHeader(value = "userToken") String userToken,HttpServletResponse response) {
//        System.out.println("inside visit service");
        return reportsManager.visitReport(userId,allRequestParams, fromDate, toDate, userToken,response);
    }
    //added by nikhil
    /**
     * 
     * @param userId
     * @param allRequestParams
     * @param fromDate
     * @param toDate
     * @param userToken
     * @param response
     * @return visit report of specific user for specific time
     */
    @RequestMapping(value = "/visit/{userId}/{fromDate}/{toDate}/admincsv", method = RequestMethod.GET)
    public
    @ResponseBody
    Object getVisitReportAdminCsv(@PathVariable("userId") int userId,@RequestParam Map<String,String> allRequestParams, @PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @RequestHeader(value = "userToken") String userToken,HttpServletResponse response) {
//        System.out.println("inside visit service");
        return reportsManager.visitReportAdminCsv(userId,allRequestParams, fromDate, toDate, userToken,response);
    }

    /**
     * 
     * @param userId
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return visit report of specific user for specific time
     */
    @RequestMapping(value = "/visit/{userId}/{fromDate}/{toDate}/csv", method = RequestMethod.GET)
    public
    @ResponseBody
    Object getVisitReportCsv(@PathVariable("userId") int userId, @PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @RequestHeader(value = "userToken") String userToken) {
        return reportsManager.visitReportCsv(userId, fromDate, toDate, userToken);
    }
    /**
     * 
     * @param allRequestParams
     * @param fromDate
     * @param toDate
     * @param userToken
     * @param response
     * @return attendance report of all user for specific time
     */
    @RequestMapping(value = "/admin/attendance/{fromDate}/{toDate}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object getAttendanceReportForAdmin(@RequestParam Map<String,String> allRequestParams,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @RequestHeader(value = "userToken") String userToken,HttpServletResponse response) {
        return reportsManager.attendanceReportForAdmin(allRequestParams,fromDate, toDate, userToken,response);
    }

    /**
     * 
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return attendance report of all user for specific time
     */
    @RequestMapping(value = "/admin/attendance/{fromDate}/{toDate}/csv", method = RequestMethod.GET)
    public
    @ResponseBody
    Object getAttendanceReportCsvForAdmin(@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @RequestHeader(value = "userToken") String userToken) {
        return reportsManager.attendanceReportCsvForAdmin(fromDate, toDate, userToken);
    }

    /**
     * 
     * @param userId
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return travel report of specific user for specific time
     */
    @RequestMapping(value = "/travel/{userId}/{fromDate}/{toDate}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object getTravelReport(@PathVariable("userId") int userId, @PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @RequestHeader(value = "userToken") String userToken) {
        return reportsManager.travelReport(userId, fromDate, toDate, userToken);
    }
    
    /**
     *
     * @param userId
     * @param allRequestParams
     * @param tzOffset
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return
     */
    @RequestMapping(value = "/monthlyTravel/{userId}/{tzOffset}/{fromDate}/{toDate}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object getMonthlyTravelReport(@PathVariable("userId") int userId,@RequestParam Map<String,String> allRequestParams, @PathVariable("tzOffset") String tzOffset ,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @RequestHeader(value = "userToken") String userToken) {
        return reportsManager.monthlyTravelReport(userId, allRequestParams,tzOffset, fromDate, toDate, userToken);
    }
    
    /**
     *
     * @param userId
     * @param allRequestParams
     * @param tzOffset
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return
     */
    @RequestMapping(value = "/monthlyTravel/subordinate/{userId}/{tzOffset}/{fromDate}/{toDate}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object getSubordinateMonthlyTravelReport(@PathVariable("userId") int userId,@RequestParam Map<String,String> allRequestParams, @PathVariable("tzOffset") String tzOffset ,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @RequestHeader(value = "userToken") String userToken) {
        return reportsManager.subordinateMonthlyTravelReport(userId, allRequestParams,tzOffset, fromDate, toDate, userToken);
    }
    
    /**
     *
     * @param response
     * @param userId
     * @param allRequestParams
     * @param tzOffset
     * @param fromDate
     * @param toDate
     * @param userToken
     */
    @RequestMapping(value = "/monthlyTravel/export/{userId}/{tzOffset}/{fromDate}/{toDate}/{userToken}", method = RequestMethod.GET)
    public void getMonthlyTravelReportAsExcel(HttpServletResponse response,@PathVariable("userId") int userId,@RequestParam Map<String,String> allRequestParams, @PathVariable("tzOffset") String tzOffset ,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @PathVariable("userToken") String userToken) {

        try {
            String fileName=allRequestParams.get("userName").toString();
            fileName=fileName.replaceAll(" ","_")+"_MonthlyReport";
            response.setHeader("Content-disposition", "attachment; filename="+fileName+".xls");
            response.setContentType("application/vnd.ms-excel");
            response.getOutputStream().write(reportsManager.exportMonthlyTravelReportAsExcel(userId, allRequestParams,tzOffset, fromDate, toDate, userToken));
        } catch (Exception e) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @Added by vaibhav
     * @param month
     * @param year
     * @param userToken
     * @return attendance report of specific user for specific time
     */
    @RequestMapping(value = "/monthlyMuster/{month}/{year}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object getAttendanceMuster(@PathVariable("month") int month, @PathVariable("year") int year, @RequestHeader(value = "userToken") String userToken) {
        return reportsManager.monthlyAttendanceMuster(month, year, userToken);
    }

  
    
}
