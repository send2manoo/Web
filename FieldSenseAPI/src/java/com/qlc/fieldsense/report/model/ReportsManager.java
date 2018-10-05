/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.report.model;

import com.qlc.fieldsense.appointments.model.Appointment;
import com.qlc.fieldsense.appointments.model.AppointmentManager;
import com.qlc.fieldsense.attendance.dao.AttendanceDao;
import com.qlc.fieldsense.attendance.dao.AttendanceDaoImpl;
import com.qlc.fieldsense.expense.model.Expense;
import com.qlc.fieldsense.report.dao.ReportsDao;
import com.qlc.fieldsense.team.dao.TeamDao;
import com.qlc.fieldsense.user.dao.UserDao;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.user.model.UserManager;
import com.qlc.fieldsense.usersTravelLogs.model.UsersTravelLogs;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;

/**
 *
 * @author Ramesh
 * @date 08-08-2014
 */
public class ReportsManager {

    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
    ReportsDao reportsDao = (ReportsDao) GetApplicationContext.ac.getBean("reportsDaoImpl");
    TeamDao teamDao = (TeamDao) GetApplicationContext.ac.getBean("teamDaoImpl");
    AttendanceDao attendanceDao = (AttendanceDaoImpl) GetApplicationContext.ac.getBean("attendanceDaoImpl");
    UserDao userDao = (UserDao) GetApplicationContext.ac.getBean("userDaoImpl");

    /**
     * 
     * @param userId
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return attendance report of specific user for specific time
     */
    public Object attendanceReport(int userId, String fromDate, String toDate, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                if (fieldSenseUtils.isUserValid(userId)) {
                    if (fieldSenseUtils.isUserActive(userId)) {
                        int accountId = fieldSenseUtils.accountIdForToken(userToken);
                        List<Attendance> attendaceReportList = reportsDao.selectAttendaceReport(userId, fromDate, toDate, accountId);
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Attendace report list .", attendaceReportList);
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " User is not active plese contact admin .", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " User is not registered plese try with diffrent user .", "", "");
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
     * @param userId
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return expense report of specific user for specific time
     */
    public Object expenseReport(int userId, String fromDate, String toDate, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                if (fieldSenseUtils.isUserValid(userId)) {
                    if (fieldSenseUtils.isUserActive(userId)) {
                        int accountId = fieldSenseUtils.accountIdForToken(userToken);
                        List<Expense> expenseReportList = reportsDao.selectExpenseReport(userId, fromDate, toDate, accountId);
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Expense report list .", expenseReportList);
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " User is not active plese contact admin .", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " User is not registered plese try with diffrent user .", "", "");
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
     * @param userId
     * @param fromDate
     * @param toDate
     * @param userToken
     * @param response
     * @return expense report of specific user for specific time
     */
    public Object accountExpenseReport(@RequestParam Map<String,String> allRequestParams,int userId, String fromDate, String toDate, String userToken,HttpServletResponse response) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
            if (fieldSenseUtils.isUserValid(userId)) {
                if (fieldSenseUtils.isUserActive(userId)) {
                    int accountId = fieldSenseUtils.accountIdForToken(userToken);
                    List<Expense> expenseReportList = reportsDao.selectAccountExpenseReport(allRequestParams,userId, fromDate, toDate, accountId);
                    List<Expense> exp_list=new ArrayList<Expense>();
                    ArrayList allcatnames=new  ArrayList();
                    LinkedHashMap status_percnt=new LinkedHashMap();
                    ArrayList status_all_percnt=new  ArrayList();
                    LinkedHashMap cat_percnt=new LinkedHashMap();
                    ArrayList cat_all_percnt=new  ArrayList();
                    int pend_rpt_count=0;
                    int reg_rpt_count=0;
                    int pendcount=0;
                    int appcount=0;
                    int regcount=0;
                    int discount=0;
                    HashSet<String> categories=new HashSet<String>();
                    if(!expenseReportList.isEmpty()){
                        for(Expense exp:expenseReportList){
                            int status=exp.getStatus();
                            if(status==0){
                                pend_rpt_count++;
                            }else if(status==1){
                                pendcount++;
                            }else if(status==2){
                                reg_rpt_count++;
                            }else if(status==3){
                                appcount++;
                            }else if(status==4){
                                regcount++;
                            }else if(status==5){
                                discount++;
                            }
                            allcatnames.add(exp.getExp_category_name());
                            categories.add(exp.getExp_category_name());
                            if(!exp.getExpenseTime().equals(Timestamp.valueOf("1111-11-11 11:11:11"))){
                                exp_list.add(exp);
                            }
                        }
                        double singlperc=(double)100/expenseReportList.size();
                        if(pend_rpt_count!=0){
                            status_percnt.put("Pending by Reporting Head",(double)pend_rpt_count*singlperc);
                        }
                        if(pendcount!=0){
                            status_percnt.put("Pending by Accounts",(double)pendcount*singlperc);
			}
			if(appcount!=0){
				status_percnt.put("Approved",(double)appcount*singlperc);
			}
                        if(reg_rpt_count!=0){
                            status_percnt.put("Rejected by Reporting Head",(double)reg_rpt_count*singlperc);
			}
			if(regcount!=0){
                           status_percnt.put("Rejected by Accounts",(double)regcount*singlperc);
			}
			if(discount!=0){
                           status_percnt.put("Disbursed",(double)discount*singlperc);
			}
                        status_all_percnt.add(status_percnt);
                        expenseReportList.get(0).setStatus_all_percnt(status_all_percnt);
                        for (String exp_cat_name : categories) {
				int count=0;
				for(int j=0;j<allcatnames.size();j++){
					if(exp_cat_name.equals(allcatnames.get(j))){
						count++;
					}
				}
				cat_percnt.put(exp_cat_name,(double)count*singlperc);
			}
                        cat_all_percnt.add(cat_percnt);
                        expenseReportList.get(0).setCat_all_percnt(cat_all_percnt);
                    }    
                    return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Expense report list .", exp_list,expenseReportList.size());
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " User is not active plese contact admin .", "", "");
                }
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " User is not registered plese try with diffrent user .", "", "");
            }
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
     * @param fromDate
     * @param toDate
     * @param userToken
     * @param response
     * @return expense report of specific user for specific time
     */
    public Object allAccountExpenseReport(@RequestParam Map<String,String> allRequestParams,String fromDate, String toDate, String userToken,HttpServletResponse response) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
           // if (fieldSenseUtils.isUserValid(userId)) {
             //   if (fieldSenseUtils.isUserActive(userId)) {
                    int accountId = fieldSenseUtils.accountIdForToken(userToken);
                    List<Expense> expenseReportList = reportsDao.selectAllAccountExpenseReport(allRequestParams,fromDate, toDate, accountId);
                    List<Expense> exp_list=new ArrayList<Expense>();
                    ArrayList allcatnames=new  ArrayList();
                    LinkedHashMap status_percnt=new LinkedHashMap();
                    ArrayList status_all_percnt=new  ArrayList();
                    LinkedHashMap cat_percnt=new LinkedHashMap();
                    ArrayList cat_all_percnt=new  ArrayList();
                    int pend_rpt_count=0;
                    int reg_rpt_count=0;
                    int pendcount=0;
                    int appcount=0;
                    int regcount=0;
                    int discount=0;
                    HashSet<String> categories=new HashSet<String>();
                    if(!expenseReportList.isEmpty()){
                        for(Expense exp:expenseReportList){
                            int status=exp.getStatus();
                            if(status==0){
                                pend_rpt_count++;
                            }else if(status==1){
                                pendcount++;
                            }else if(status==2){
                                reg_rpt_count++;
                            }else if(status==3){
                                appcount++;
                            }else if(status==4){
                                regcount++;
                            }else if(status==5){
                                discount++;
                            }
                            allcatnames.add(exp.getExp_category_name());
                            categories.add(exp.getExp_category_name());
                            if(!exp.getExpenseTime().equals(Timestamp.valueOf("1111-11-11 11:11:11"))){
                                exp_list.add(exp);
                            }
                        }
                        double singlperc=(double)100/expenseReportList.size();
                        if(pend_rpt_count!=0){
                            status_percnt.put("Pending by Reporting Head",(double)pend_rpt_count*singlperc);
                        }
                        if(pendcount!=0){
                            status_percnt.put("Pending by Accounts",(double)pendcount*singlperc);
			}
			if(appcount!=0){
				status_percnt.put("Approved",(double)appcount*singlperc);
			}
                        if(reg_rpt_count!=0){
                            status_percnt.put("Rejected by Reporting Head",(double)reg_rpt_count*singlperc);
			}
			if(regcount!=0){
                           status_percnt.put("Rejected by Accounts",(double)regcount*singlperc);
			}
			if(discount!=0){
                           status_percnt.put("Disbursed",(double)discount*singlperc);
			}
                        status_all_percnt.add(status_percnt);
                        expenseReportList.get(0).setStatus_all_percnt(status_all_percnt);
                        for (String exp_cat_name : categories) {
				int count=0;
				for(int j=0;j<allcatnames.size();j++){
					if(exp_cat_name.equals(allcatnames.get(j))){
						count++;
					}
				}
				cat_percnt.put(exp_cat_name,(double)count*singlperc);
			}
                        cat_all_percnt.add(cat_percnt);
                        expenseReportList.get(0).setCat_all_percnt(cat_all_percnt);
                    }    
                    return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Expense report list .", exp_list,expenseReportList.size());
               // } else {
                  //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " User is not active plese contact admin .", "", "");
               // }
           // } else {
           //     return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " User is not registered plese try with diffrent user .", "", "");
            //}
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
     * @param userId
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return visit report of specific user for specific time
     */

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
    public Object visitReport(int userId,Map<String,String> allRequestParams, String fromDate, String toDate, String userToken,HttpServletResponse response) {
       List<User> userList = null;
       
        if (fieldSenseUtils.isTokenValid(userToken)) {
            if(userId == 0){
//                System.out.println("inside All");
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                userList = userDao.slectAllUsers(accountId);
                List<Appointment> visitReportList = reportsDao.selectVistReport(userId,userList,allRequestParams, fromDate, toDate, accountId);
                        List<Appointment> app_list=new ArrayList<Appointment>();
                        for(Appointment app:visitReportList){
                            if(app.getSdateTime().equals("1111-11-11")){
                                continue;
                            }else{
                                app_list.add(app);
                            }
                        } 
//                        System.out.println("app_list##@ "+app_list.toString());
              return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Visit report list All .",app_list, visitReportList.size());
            }else{
               //if(fieldSenseUtils.isSessionExpired(userToken)){
                if (fieldSenseUtils.isUserValid(userId)) {
                    if (fieldSenseUtils.isUserActive(userId)) {
                        int accountId = fieldSenseUtils.accountIdForToken(userToken);
                        List<Appointment> visitReportList = reportsDao.selectVistReport(userId,userList,allRequestParams, fromDate, toDate, accountId);
                        List<Appointment> app_list=new ArrayList<Appointment>();
                        for(Appointment app:visitReportList){
                            if(app.getSdateTime().equals("1111-11-11")){
                                continue;
                            }else{
                                app_list.add(app);
                            }
                        } 
                        return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Visit report list .",app_list, visitReportList.size());
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " User is not active plese contact admin .", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " User is not registered plese try with diffrent user .", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //} }
            }
        } else {
            response.setContentType( MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    public Object visitReportAdminCsv(int userId,Map<String,String> allRequestParams, String fromDate, String toDate, String userToken,HttpServletResponse response) {
       List<User> userList = null;
       
        if (fieldSenseUtils.isTokenValid(userToken)) {
            if(userId == 0){
//                System.out.println("inside All");
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                userList = userDao.slectAllUsers(accountId);
                List<Appointment> visitReportList = reportsDao.selectVisitReportForAdminCsv(userId,userList,allRequestParams, fromDate, toDate, accountId);
                        List<Appointment> app_list=new ArrayList<Appointment>();
                        for(Appointment app:visitReportList){
                            if(app.getSdateTime().equals("1111-11-11")){
                                continue;
                            }else{
                                app_list.add(app);
                            }
                        } 
//                        System.out.println("app_list##@ "+app_list.toString());
              return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Visit report list All .",app_list, visitReportList.size());
            }
         else {
            response.setContentType( MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    /**
     * 
     * @param userId
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return visit report of specific user for specific time
     */
    public Object visitReportCsv(int userId, String fromDate, String toDate, String userToken) {
        List<User> subordinateList = null;
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
              if(userId == 0){
                
                int loginUser= fieldSenseUtils.userIdForToken(userToken);
                int accountId = fieldSenseUtils.accountIdForToken(userToken);

                subordinateList=teamDao.selectUserAllSubordinates(loginUser, accountId);  
                int accountIdU = fieldSenseUtils.accountIdForToken(userToken);
                List<Appointment> visitReportList = reportsDao.selectVistReportCsv(userId,subordinateList, fromDate, toDate, accountIdU);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Visit report list All ." , visitReportList);
            }else {
                  if (fieldSenseUtils.isUserValid(userId)) {
                    if (fieldSenseUtils.isUserActive(userId)) {
                        int accountId = fieldSenseUtils.accountIdForToken(userToken);
                        List<Appointment> visitReportList = reportsDao.selectVistReportCsv(userId,subordinateList, fromDate, toDate, accountId);
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Visit report list .", visitReportList);
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " User is not active plese contact admin .", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " User is not registered plese try with diffrent user .", "", "");
                }
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
     * @param fromDate
     * @param toDate
     * @param userToken
     * @param response
     * @return attendance report of all user for specific time
     */
    public Object attendanceReportForAdmin(Map<String,String> allRequestParams,String fromDate, String toDate, String userToken,HttpServletResponse response) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<Attendance> attendaceReportList = reportsDao.selectAttendaceReportForAdmin(allRequestParams,fromDate, toDate, accountId);
               /// List<Attendance> att_list=new ArrayList<Attendance>();
                ///for(Attendance att:attendaceReportList){
               //     if(att.getPunchDate().equals("1111-11-11")){
               //         continue;
               //     }else{
                //        att_list.add(att);
                 //   }
                //}  
                int Total=0;
                if(!attendaceReportList.isEmpty()){
                    Total=attendaceReportList.get(0).getTotalrecords();
                }
                return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Attendace report list .", attendaceReportList,Total);
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
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return attendance report of all user for specific time
     */
    public Object attendanceReportCsvForAdmin(String fromDate, String toDate, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<Attendance> attendaceReportList = reportsDao.selectAttendaceReportCsvForAdmin(fromDate, toDate, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Attendace report list .", attendaceReportList);
           // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
     /**
     * 
     * @param userId
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return travel report of specific user for specific time
     */
    public Object travelReport(int userId, String fromDate, String toDate, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<UsersTravelLogs> userTravelledroutes = reportsDao.selectUsersTravelledReoprt(userId, fromDate, toDate, accountId);
                List<UsersTravelLogs> userTravelledreports = new ArrayList<UsersTravelLogs>();
                
                if(userTravelledroutes.size()>0 && userTravelledroutes.get(0).getNullDistanceCount()>0){
                    // in this case old technique of calculating distance will be use
                    for (int i = 0; i < userTravelledroutes.size(); i++) {
                        UsersTravelLogs travelLog = userTravelledroutes.get(i);
                        double distance;
                        if (i == 0 || travelLog.getSourceValue()==1 || travelLog.getSourceValue()==5 ) { // if sourcevalue=5 then distance between timeout start and stop should not be calculated
                            distance = 0;
                        } else {
                            UsersTravelLogs travelLogPrevios = userTravelledroutes.get(i-1);
                            distance = FieldSenseUtils.travelDistance(travelLogPrevios.getLatitude(), travelLogPrevios.getLangitude(), travelLog.getLatitude(), travelLog.getLangitude());
                            distance = Math.round (distance * 10000.0) / 10000.0;
                        }  
                        travelLog.setTravelDistance(distance);
                        userTravelledreports.add(travelLog);
                    }
                }else{
                    // in this case distance will be fetch from DB which is set by mobile
                    userTravelledreports=userTravelledroutes;
                }
                
                //Added by Awaneesh after discussion with Bappa Sir and Madhuri on 14th Sept 2014
                // This patch will bring down performance hence should be removed in next release
                //Patch-up added for mobile issue // later on it should be removed and should be fix at mobile side
                UsersTravelLogs travelEntry=null;
                boolean isPresent=false;
                List<UsersTravelLogs> userTravelledreportsTmp = new ArrayList<UsersTravelLogs>();
                for (int i = 0; i < userTravelledreports.size(); i++) {
                    travelEntry = userTravelledroutes.get(i);
                    if(travelEntry.getSourceValue()==2 || travelEntry.getSourceValue()==1){
                        isPresent=reportsDao.isTravelEntryPresentInAttendance(travelEntry,accountId);
                        if(isPresent==false){
//                            System.out.println("travelReport, setting sourcevalue 0 bcz entry not present in attendance");
                            travelEntry.setSourceValue(0);
                        }
                    }
                    userTravelledreportsTmp.add(travelEntry);
                }    
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Travelled rote report .", userTravelledreportsTmp);

                //End of patch
                
                //return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Travelled rote report .", userTravelledreports);
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
    /**
     *
     * @param travelUserId
     * @param allRequestParams
     * @param timeZoneOffset
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return
     */
    public Object monthlyTravelReport(int travelUserId,Map<String,String>allRequestParams, String timeZoneOffset, String fromDate, String toDate, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                
            int accountId = fieldSenseUtils.accountIdForToken(userToken);
            List<HashMap> monthlyTravelReport=getMonthlyTravelReportData( travelUserId,allRequestParams,  timeZoneOffset,  fromDate,  toDate,  userToken);

            int userCount=1;
            if(travelUserId==0){
                userCount=fieldSenseUtils.totalUsersInAccountRoleNotZero(accountId);
            } 
            
            return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Travelled rote report .", monthlyTravelReport,userCount);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
    /**
     *
     * @param travelUserId
     * @param allRequestParams
     * @param timeZoneOffset
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return
     */
    public Object subordinateMonthlyTravelReport(int travelUserId,Map<String,String>allRequestParams, String timeZoneOffset, String fromDate, String toDate, String userToken){ 
        if (fieldSenseUtils.isTokenValid(userToken)) {
            int loginUser= fieldSenseUtils.userIdForToken(userToken);
            int accountId = fieldSenseUtils.accountIdForToken(userToken);

            List<User> subordinateList=teamDao.selectUserAllSubordinates(loginUser, accountId);            
            List<HashMap> monthlyTravelReport=subordinateMonthlyTravelReportData(subordinateList, travelUserId,allRequestParams,  timeZoneOffset,  fromDate,  toDate,  userToken);

            int userCount=1;
            if(travelUserId==0){
                userCount=subordinateList.size();
            } 
            
            return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Travelled rote report .", monthlyTravelReport,userCount);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
    private  List<HashMap> subordinateMonthlyTravelReportData(List<User> subordinateList,int userId,Map<String,String>allRequestParams, String timeZoneOffset, String fromDate, String toDate, String userToken){
        String addSubSign="+";
            if(timeZoneOffset.startsWith("-")){
                addSubSign="-";
            }
            timeZoneOffset=timeZoneOffset.substring(1).trim();
            int tz = Integer.parseInt(timeZoneOffset);
            int hh = tz/60;
            int mm = tz%60;
            String shh = String.valueOf(hh);
            if(hh <10){
                shh="0"+shh;
            }
            String smm = String.valueOf(mm);
            if(mm <10){
                smm="0"+smm;
            }
            timeZoneOffset=addSubSign+shh+":"+smm;
            
            final int start=Integer.parseInt(allRequestParams.get("start"));
            final int length=Integer.parseInt(allRequestParams.get("length"));
            
            int accountId = fieldSenseUtils.accountIdForToken(userToken);
            ArrayList<Integer> subuserIdList= new ArrayList<Integer>();
            for(User user: subordinateList){
                subuserIdList.add(user.getId());
            }
            UserManager userManager = new UserManager();
            List<HashMap> monthlyTravelReport =userManager.getUserlistForSubordinates(accountId, userId,fromDate, toDate,subuserIdList,start ,length);
           // List<HashMap> monthlyTravelReport = reportsDao.selectMonthlyTravelReoprtForSubordinate(subuserIdList,userId,allRequestParams, fromDate, toDate, accountId);
            //monthlyTravelReport.removeAll(Collections.singleton(null));// remove null
            ArrayList<Integer> idList= new ArrayList<Integer>();
             HashMap<Integer,String> mapOfUserKeyValue = new HashMap<Integer,String>(); 
            for(HashMap map : monthlyTravelReport){
                int uId=(Integer)map.get("userId");
                String fullName = (String)map.get("fullName");
                idList.add(uId);
                 mapOfUserKeyValue.put(uId, fullName);
            }
             monthlyTravelReport = null;
             monthlyTravelReport = new ArrayList<HashMap>();
                HashMap<Integer,List<HashMap>> report = new HashMap<Integer,List<HashMap>>();
      //     HashMap<Integer,List<HashMap>> report=reportsDao.dayWiseMonthlyTravelReoprtNew(userId,idList, timeZoneOffset, allRequestParams, fromDate, toDate, accountId);
//                System.out.println("User list "+idList.size());
                  for(int i =0 ;i<idList.size();i++){
//                System.out.println("Helllo ujn mamanges ");
            LinkedHashMap<Object , Object > mapOfDateWiseData= reportsDao.getDayWiseMonthlyReport(idList.get(i), fromDate, toDate, accountId,timeZoneOffset);
//                System.out.println("Got here the out pou");
            Iterator it=mapOfDateWiseData.entrySet().iterator();
                AppointmentManager appointmentManager = new AppointmentManager();
                List<HashMap> listOfMapData = new ArrayList<HashMap>();
//                System.out.println("Got list ");
                HashMap<Object,Object> mapOfDates = null;
                int totalVisits = 0;
                double totalDistance = 0;
            while (it.hasNext()) {
                 Map.Entry pair = (Map.Entry)it.next();
                 mapOfDates = new HashMap<Object,Object>();
                 mapOfDates.put("date", pair.getKey());
                 int visits=appointmentManager.selectAppointmentOfUser(idList.get(i), pair.getKey().toString(), accountId);
                 totalVisits = totalVisits + visits;
                 mapOfDates.put("visits", visits);
                 double distance = (Double) pair.getValue();
                 totalDistance = totalDistance + distance;
                 DecimalFormat df = new DecimalFormat("#.0000");
                 totalDistance =  Double.valueOf(df.format(totalDistance));
                 mapOfDates.put("distance", distance);
                 mapOfDates.put("userId", idList.get(i)); 
                 listOfMapData.add(mapOfDates);
                 mapOfDates=null;
             }
            HashMap<Object,Object> mapOfMainData = new HashMap<Object,Object>();
            mapOfMainData.put("visits", totalVisits);
            mapOfMainData.put("distance", totalDistance);
            mapOfMainData.put("userId", idList.get(i));
            mapOfMainData.put("fullName", mapOfUserKeyValue.get(idList.get(i)));
            monthlyTravelReport.add(mapOfMainData);
            report.put(idList.get(i), listOfMapData);
             }
                
            for(HashMap map : monthlyTravelReport){
                int uId=(Integer)map.get("userId");
                List<HashMap> dayWisemonthlyTravelReport = new ArrayList<HashMap>();
                
                if((Double)map.get("distance")==0 && (Integer)map.get("visits")==0){
                    map.put("dayWiseReport", dayWisemonthlyTravelReport);
                }else{
                    dayWisemonthlyTravelReport = report.get(uId) ;
                    if(dayWisemonthlyTravelReport==null){
                        map.put("dayWiseReport", new ArrayList<HashMap>());
                    }else{
                        map.put("dayWiseReport", dayWisemonthlyTravelReport);
                    }
                }
            }    
            return monthlyTravelReport;
    }
    
    private  List<HashMap> getMonthlyTravelReportData(int userId,Map<String,String>allRequestParams, String timeZoneOffset, String fromDate, String toDate, String userToken){
        String addSubSign="+";
            if(timeZoneOffset.startsWith("-")){
                addSubSign="-";
            }
            timeZoneOffset=timeZoneOffset.substring(1).trim();
            int tz = Integer.parseInt(timeZoneOffset);
            int hh = tz/60;
            int mm = tz%60;
            String shh = String.valueOf(hh);
            if(hh <10){
                shh="0"+shh;
            }
            String smm = String.valueOf(mm);
            if(mm <10){
                smm="0"+smm;
            }
            timeZoneOffset=addSubSign+shh+":"+smm;
            
            final int start=Integer.parseInt(allRequestParams.get("start"));
            final int length=Integer.parseInt(allRequestParams.get("length"));
            
            int accountId = fieldSenseUtils.accountIdForToken(userToken);
            UserManager userManager = new UserManager();
//            System.out.println("User "+userId);
       //     List<HashMap> monthlyTravelReport = reportsDao.selectMonthlyTravelReoprt(userId,allRequestParams, fromDate, toDate, accountId);
            List<HashMap> monthlyTravelReport =userManager.getUserlist(accountId, userId,fromDate, toDate,start,length);
            //monthlyTravelReport.removeAll(Collections.singleton(null));// remove null
            ArrayList<Integer> idList= new ArrayList<Integer>();
            HashMap<Integer,String> mapOfUserKeyValue = new HashMap<Integer,String>(); 
            for(HashMap map : monthlyTravelReport){
                int uId=(Integer)map.get("userId");
                String fullName = (String)map.get("fullName");
                idList.add(uId);
                mapOfUserKeyValue.put(uId, fullName);
            }
             HashMap<Integer,List<HashMap>> report = new HashMap<Integer,List<HashMap>>();
            //if(userId!=0){
             monthlyTravelReport = null;
             monthlyTravelReport = new ArrayList<HashMap>();
             for(int i =0 ;i<idList.size();i++){
//                System.out.println("Helllo ujn mamanges ");
            LinkedHashMap<Object , Object > mapOfDateWiseData= reportsDao.getDayWiseMonthlyReport(idList.get(i), fromDate, toDate, accountId,timeZoneOffset);
//                System.out.println("Got here the out pou");
            Iterator it=mapOfDateWiseData.entrySet().iterator();
                AppointmentManager appointmentManager = new AppointmentManager();
                List<HashMap> listOfMapData = new ArrayList<HashMap>();
//                System.out.println("Got list ");
                HashMap<Object,Object> mapOfDates = null;
                int totalVisits = 0;
                double totalDistance = 0;
            while (it.hasNext()) {
                 Map.Entry pair = (Map.Entry)it.next();
                 mapOfDates = new HashMap<Object,Object>();
                 mapOfDates.put("date", pair.getKey());
                 int visits=appointmentManager.selectAppointmentOfUser(idList.get(i), pair.getKey().toString(), accountId);
                 totalVisits = totalVisits + visits;
                 mapOfDates.put("visits", visits);
                 double distance = (Double) pair.getValue();
                 totalDistance = totalDistance + distance;
                 DecimalFormat df = new DecimalFormat("#.0000");
                 totalDistance =  Double.valueOf(df.format(totalDistance));
                 mapOfDates.put("distance", distance);
                 mapOfDates.put("userId", idList.get(i)); 
                 listOfMapData.add(mapOfDates);
                 mapOfDates=null;
             }
            HashMap<Object,Object> mapOfMainData = new HashMap<Object,Object>();
            mapOfMainData.put("visits", totalVisits);
            mapOfMainData.put("distance", totalDistance);
            mapOfMainData.put("userId", idList.get(i));
            mapOfMainData.put("fullName", mapOfUserKeyValue.get(idList.get(i)));
            monthlyTravelReport.add(mapOfMainData);
            report.put(idList.get(i), listOfMapData);
             }
          ///  }else{
            
            
          //  }
           // HashMap<Integer,List<HashMap>> report=reportsDao.dayWiseMonthlyTravelReoprtNew(userId,idList, timeZoneOffset, allRequestParams, fromDate, toDate, accountId);
            for(HashMap map : monthlyTravelReport){
                int uId=(Integer)map.get("userId");
                List<HashMap> dayWisemonthlyTravelReport = new ArrayList<HashMap>();
                
                if((Double)map.get("distance")==0 && (Integer)map.get("visits")==0){
                    map.put("dayWiseReport", dayWisemonthlyTravelReport);
                }else{
                    dayWisemonthlyTravelReport = report.get(uId) ;
                    if(dayWisemonthlyTravelReport==null){
                        map.put("dayWiseReport", new ArrayList<HashMap>());
                    }else{
                        map.put("dayWiseReport", dayWisemonthlyTravelReport);
                    }
                }
            }    
            return monthlyTravelReport;
    }
    
    /**
     *
     * @param userId
     * @param allRequestParams
     * @param timeZoneOffset
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return
     */
    public byte[] exportMonthlyTravelReportAsExcel(int userId,Map<String,String>allRequestParams, String timeZoneOffset, String fromDate, String toDate, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                List<HashMap> monthlyTravelReport=getMonthlyTravelReportData( userId,allRequestParams,  timeZoneOffset,  fromDate,  toDate,  userToken);
               /* StringBuffer sb = new StringBuffer();
                sb.append("<table><thead><tr><th colspan=\"4\">Monthly Travel Report</th></tr><tr></tr></thead></table>");
                sb.append("<table>");
                sb.append("<thead>");
                sb.append("<tr>");
                sb.append("<th>User</th>");
                sb.append("<th>Date</th>");
                sb.append("<th style='width:100px;'>Distance Travelled (in kms)</th>");
                sb.append("<th>No. of Visits</th>");
                sb.append("</thead>");
                sb.append("<tbody>");

                for(HashMap map:monthlyTravelReport){
                        List<HashMap> dayWisemonthlyTravelReport=(List<HashMap>)map.get("dayWiseReport");
                        String fullName=(String)map.get("fullName");
                        double tdist=(Double)map.get("distance");
                        int tvisit=(Integer)map.get("visits");
                        if(tdist==0 && tvisit==0){
                                sb.append("<tr><td>"+fullName+"</td><td align='center'>-</td><td>"+tdist+"</td><td>"+tvisit+"</td></tr>");
                        }else{
                                int j=0;
                                for(HashMap dailyMap:dayWisemonthlyTravelReport){
                                    String date=dailyMap.get("date").toString();
                                    double dist=(Double)dailyMap.get("distance");
                                    int visit=(Integer)dailyMap.get("visits");
                                    if(j==0){
                                                sb.append("<tr><td>"+fullName+"</td><td align='center'>"+date+"</td><td>"+dist+"</td><td>"+visit+"</td></tr>");
                                        }else{
                                                sb.append("<tr><td></td><td align='center'>"+date+"</td><td>"+dist+"</td><td>"+visit+"</td></tr>");
                                        }
                                        j++;
                                }
                                sb.append("<tr><td></td><td><b>Total :</b></td><td><b>"+tdist+"</b></td><td><b>"+tvisit+"</b></td></tr>");					
                        }
                        sb.append("<tr></tr>"); 
                }
                        sb.append("</tbody></table>"); 
                        
                      */  
                        
                        
        int rownum = 0;
        HSSFWorkbook workbook = new HSSFWorkbook();
	HSSFSheet sheet = workbook.createSheet("Monthly Report");
        
        String startTime=allRequestParams.get("startTime").toString();
        String endTime=allRequestParams.get("endTime").toString();
        
        Row fromRow = sheet.createRow(rownum++);
        Cell fromCell1 = fromRow.createCell(0);
        fromCell1.setCellValue("From:");
        Cell fromCell2 = fromRow.createCell(1);
        fromCell2.setCellValue(startTime);
        makeRowBold(workbook, sheet.getRow(rownum-1));
        
        Row toRow = sheet.createRow(rownum++);
        Cell toCell1 = toRow.createCell(0);
        toCell1.setCellValue("To:");
        Cell toCell2 = toRow.createCell(1);
        toCell2.setCellValue(endTime);
        makeRowBold(workbook, sheet.getRow(rownum-1));   
        
        Row grow1 = sheet.createRow(rownum++);
        
        Row hrow = sheet.createRow(rownum++);
        Cell hcell1 = hrow.createCell(0);
        hcell1.setCellValue("User");
        Cell hcell2 = hrow.createCell(1);
        hcell2.setCellValue("Date");
        Cell hcell3 = hrow.createCell(2);
        hcell3.setCellValue("Distance Travelled (in kms)");
        Cell hcell4 = hrow.createCell(3);
        hcell4.setCellValue("No. of Visits");
        
        makeRowBold(workbook, sheet.getRow(rownum-1));
        
        for (HashMap map:monthlyTravelReport) {
            int cellnum = 0;
            List<HashMap> dayWisemonthlyTravelReport=(List<HashMap>)map.get("dayWiseReport");
            String fullName=(String)map.get("fullName");
            double tdist=(Double)map.get("distance");
            int tvisit=(Integer)map.get("visits");
            if(tdist==0 && tvisit==0){
                Row row = sheet.createRow(rownum++);
                Cell cell1 = row.createCell(0);
                cell1.setCellValue(fullName);
                Cell cell2 = row.createCell(1);
                cell2.setCellValue("-");
                Cell cell3 = row.createCell(2);
                cell3.setCellValue(tdist);
                Cell cell4 = row.createCell(3);
                cell4.setCellValue(tvisit); 
                //sb.append("<tr><td>"+fullName+"</td><td align='center'>-</td><td>"+tdist+"</td><td>"+tvisit+"</td></tr>");
            }else{
                int j=0;
                for (HashMap dailyMap:dayWisemonthlyTravelReport) {
                    String date=dailyMap.get("date").toString();
                    double dist=(Double)dailyMap.get("distance");
                    int visit=(Integer)dailyMap.get("visits");
                    if(j==0){
                        Row row = sheet.createRow(rownum++);
                        Cell cell1 = row.createCell(0);
                        cell1.setCellValue(fullName);
                        Cell cell2 = row.createCell(1);
                        cell2.setCellValue(date);
                        Cell cell3 = row.createCell(2);
                        cell3.setCellValue(dist);
                        Cell cell4 = row.createCell(3);
                        cell4.setCellValue(visit);
                        //sb.append("<tr><td>"+fullName+"</td><td align='center'>"+date+"</td><td>"+dist+"</td><td>"+visit+"</td></tr>");
                    }else{
                        Row row = sheet.createRow(rownum++);
                        Cell cell1 = row.createCell(0);
                        cell1.setCellValue("");
                        Cell cell2 = row.createCell(1);
                        cell2.setCellValue(date);
                        Cell cell3 = row.createCell(2);
                        cell3.setCellValue(dist);
                        Cell cell4 = row.createCell(3);
                        cell4.setCellValue(visit);
                        //sb.append("<tr><td></td><td align='center'>"+date+"</td><td>"+dist+"</td><td>"+visit+"</td></tr>");
                    }
                    j++;
                }
                Row row = sheet.createRow(rownum++);
                Cell cell1 = row.createCell(0);
                cell1.setCellValue("");
                Cell cell2 = row.createCell(1);
                cell2.setCellValue("Total:");
                Cell cell3 = row.createCell(2);
                cell3.setCellValue(tdist);
                Cell cell4 = row.createCell(3);
                cell4.setCellValue(tvisit);        
                makeRowBold(workbook, sheet.getRow(rownum-1));
                Row grow = sheet.createRow(rownum++); // for gap in each entry
                //sb.append("<tr><td></td><td><b>Total :</b></td><td><b>"+tdist+"</b></td><td><b>"+tvisit+"</b></td></tr>");
            }
             
        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        return workbook.getBytes();
            // return sb.toString().getBytes();
        }else{
            return null;
        }     
    }
    
    /**
     *
     * @param wb
     * @param row
     */
    public void makeRowBold(Workbook wb, Row row){
        CellStyle style = wb.createCellStyle();//Create style
        Font font = wb.createFont();//Create font
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);//Make font bold
        style.setFont(font);//set it to bold

        for(int i = 0; i < row.getLastCellNum(); i++){//For each cell in the row 
            row.getCell(i).setCellStyle(style);//Set the sty;e
        }
    }
    /*
     * @Added by vaibhav
     * @param travelUserId
     * @param allRequestParams
     * @param timeZoneOffset
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return
     */
    public Object monthlyAttendanceMuster(int month,int year, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                
            int accountId = fieldSenseUtils.accountIdForToken(userToken);
           List<MonthlyAttendanceMuster> monthlyMusterList =reportsDao.generateMonthlyAttendanceMuster( month,year,  accountId);
            
            for(MonthlyAttendanceMuster mm:monthlyMusterList)   {
                
                String [] monArray = mm.getMonthlyMusterArray();
                
                for(int i=0; i<monArray.length ;i++){
                    
                    if(monArray[i] == null){
                       monArray[i] = "A";
                    }
                }
                
            }
            return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Monthly Attendance muster.", monthlyMusterList,monthlyMusterList.size());
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
}
