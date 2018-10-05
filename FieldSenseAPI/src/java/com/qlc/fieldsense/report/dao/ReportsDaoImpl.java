/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.report.dao;

import com.qlc.fieldsense.activityOutcome.model.ActivityOutcome;
import com.qlc.fieldsense.activityPurpose.model.ActivityPurpose;
import com.qlc.fieldsense.appointments.model.Appointment;
import com.qlc.fieldsense.customer.model.Customer;
import com.qlc.fieldsense.expense.model.Expense;
import com.qlc.fieldsense.expenseCategory.model.ExpenseCategory;
import com.qlc.fieldsense.report.model.Attendance;
import com.qlc.fieldsense.report.model.MonthlyAttendanceMuster;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.usersTravelLogs.model.UsersTravelLogs;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Ramesh
 * @date 08-08-2014
 */
public class ReportsDaoImpl implements ReportsDao {

    /**
     *
     */
    private JdbcTemplate jdbcTemplate;
    public static final Logger log4jLog = Logger.getLogger("ReportsDaoImpl");

    /**
     *
     * @param userId
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    @Override
    public List<Attendance> selectAttendaceReport(int userId, String fromDate, String toDate, int accountId) {
//        String query = "SELECT user_id_fk,punch_date,punch_in,punch_in_location,punch_out,punch_out_location,punch_in_location_reason,punch_out_location_reason FROM attendances WHERE user_id_fk=? AND punch_date BETWEEN ? AND ? ORDER BY punch_date DESC";
       // String query = "SELECT user_id_fk,punch_date,punch_in,punch_in_location,punch_out,punch_out_location,punch_in_location_reason,punch_out_location_reason,CONCAT_WS(' ',u.first_name,u.last_name) AS userName,,(SELECT  SEC_TO_TIME( SUM( TIME_TO_SEC( `interval_time` ) ) )   FROM attendances_timeout) AS time_out_sum FROM attendances INNER JOIN fieldsense.users u ON u.id=user_id_fk WHERE user_id_fk=? AND punch_date BETWEEN ? AND ? ORDER BY punch_date DESC";
            String query="SELECT att.user_id_fk,att.punch_date,att.punch_in,att.punch_in_location,att.punch_out,att.punch_out_location,att.punch_out_date, ";
        query += " att.punch_in_location_reason,att.punch_out_location_reason,CONCAT_WS(' ',u.first_name,u.last_name) ";
        query += " AS userName,u.emp_code AS emp_code,(SELECT SUM( TIME_TO_SEC( `interval_time` ) )  ";
        query += " FROM attendances_timeout  timeout WHERE timeout.user_id_fk=? AND timeout.timeout_date >= att.punch_date) AS time_out_sum "; // edited by siddhesh changed "=" to ">="
        query += " FROM attendances att INNER JOIN fieldsense.users u ON u.id=att.user_id_fk WHERE att.user_id_fk=? ";
        //query += " AND att.punch_date between ? and ? ORDER BY att.punch_date DESC";
        query += " AND CONCAT(att.punch_date,' ',att.punch_in) between ? and ? ORDER BY att.punch_date DESC";

            log4jLog.info(" selectAttendaceReport " + query);
        Object param[] = new Object[]{userId,userId, fromDate, toDate};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<Attendance>() {

                @Override
                public Attendance mapRow(ResultSet rs, int i) throws SQLException {
                    Attendance attendance = new Attendance();
                    attendance.setUserId(rs.getInt("user_id_fk"));
                    attendance.setPunchDate(rs.getString("punch_date"));
                    attendance.setPunchInTime(rs.getString("punch_in"));
                    attendance.setPunchInLocation(rs.getString("punch_in_location"));
                    attendance.setPunchOutDate(rs.getString("punch_out_date"));
                    attendance.setPunchOutTime(rs.getString("punch_out"));
                    attendance.setPunchOutLocation(rs.getString("punch_out_location"));
                    attendance.setPunchInLocationNotFoundReason(rs.getString("punch_in_location_reason"));
                    attendance.setPunchOutLocationNotFoundReason(rs.getString("punch_out_location_reason"));
                    attendance.setUserName(rs.getString("userName"));
                    attendance.setEmp_code(rs.getString("emp_code"));
                    Long intvl=rs.getLong("time_out_sum");
                    attendance.setTotalTimeOut(fromSecToStringTime(intvl));
                    /*
                    if(rs.getTime("time_out_sum")==null){
                        attendance.setTotalTimeOut("000000");
                    }else{    
                        attendance.setTotalTimeOut(rs.getTime("time_out_sum").toString());
                    }*/
                    if(!(attendance.getPunchOutTime().equals("00:00:00") && attendance.getPunchOutDate().equals("1111-11-11"))){
                        attendance.setWorkHr(calculateWorkHr(attendance.getPunchDate(),attendance.getPunchInTime(),attendance.getPunchOutDate(),attendance.getPunchOutTime()));
                    }
                    return attendance;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAttendaceReport " + e);
            e.printStackTrace();
            return new ArrayList<Attendance>();
        }
    }

    /**
     *
     * @param punchInDate
     * @param punchInTime
     * @param punchOutDate
     * @param punchOutTime
     * @return
     */
    public  String calculateWorkHr(String punchInDate,String punchInTime,String punchOutDate,String punchOutTime){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        try{
            //Date punchIn= format.parse(punchInDate+" "+punchInTime);
           // Date punchOut= format.parse(punchOutDate+" "+punchOutTime);
            Timestamp punchInTimestamp = Timestamp.valueOf(punchInDate+" "+punchInTime);
            Timestamp punchOutTimestamp = Timestamp.valueOf(punchOutDate+" "+punchOutTime);
            long punchInMills = punchInTimestamp.getTime();
            long punchOutMills = punchOutTimestamp.getTime();
            long diff=punchOutMills-punchInMills;
            diff = (diff/((60)*(1000)));
            String hours = String.valueOf(diff/ 60);
            String mins = String.valueOf(diff % 60);
            if((diff/ 60)<10){
                hours="0"+hours;
            }
            if(((diff % 60))<10){
                mins="0"+mins;
            }
            return hours+":"+mins;
        }catch(Exception e){
            log4jLog.info("calculateWorkHr" + e);
            return "-";
        }
    }
    
    /**
     *
     * @param userId
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    @Override
    public List<Expense> selectExpenseReport(int userId, String fromDate, String toDate, final int accountId) {
        StringBuilder query = new StringBuilder();

        /*query.append("SELECT e.id,e.expense_name,e.amount_spent,e.expense_time,e.status,e.approved_rejected_on,e.rejected_reason,");
        query.append("c.customer_printas,a.appointment_title,IFNULL (u.first_name,'') first_name,IFNULL (u.last_name,'') last_name,ue.first_name,ue.last_name FROM expenses e");
        query.append(" INNER JOIN customers c ON e.customer_id_fk = c.id");
        query.append(" INNER JOIN appointments a ON a.id=e.appointment_id_fk");
        query.append(" LEFT OUTER JOIN fieldsense.users u ON u.id=e.approved_rejected_by_id_fk");
        query.append(" INNER JOIN fieldsense.users ue ON ue.id=e.user_id_fk");
        query.append(" WHERE e.user_id_fk=? AND (Date(e.expense_time) BETWEEN DATE(?) AND DATE(?)) ORDER BY e.expense_time ASC");*/

        query.append("SELECT e.id,e.appointment_id_fk,e.customer_id_fk,e.user_id_fk,e.expense_name,e.Expense_type_id_fk,e.desription,");
        query.append("e.amount_spent,e.status,e.expense_time,e.submitted_on,e.approved_rejected_on,");
        query.append("e.approved_rejected_by_id_fk,e.rejected_reason,e.created_on,e.category_id_fk,IFNULL (e.disburse_amount,0) disburse_amount,IFNULL (e.payment_mode,'') payment_mode,IFNULL (e.default_date,'1111-11-11 11:11:11') default_date,IFNULL (e.exp_category_name,'') exp_category_name,a.appointment_title,c.customer_name,u.id,IFNULL (u.first_name,'') first_name,IFNULL (u.last_name,'') last_name,IFNULL (u.full_name,'') full_name,ue.id,ue.report_to,ue.first_name,ue.last_name,ue.full_name,ec.id,IFNULL(ue1.full_name,'') reporting_head,ec.category_name FROM expenses e");
        query.append(" LEFT JOIN appointments a ON e.appointment_id_fk=a.id");
        query.append(" LEFT JOIN customers c ON e.customer_id_fk=c.id");
	query.append(" LEFT OUTER JOIN fieldsense.users u ON u.id=e.approved_rejected_by_id_fk");
        query.append(" INNER JOIN fieldsense.users ue ON ue.id=e.user_id_fk");
        query.append(" INNER JOIN fieldsense.users ue1 ON ue1.id=ue.report_to");
        query.append(" LEFT OUTER JOIN expense_categories ec ON e.category_id_fk=ec.id");
        query.append(" WHERE e.user_id_fk=? AND DATE(e.expense_time) BETWEEN ? AND ? ORDER BY e.expense_time DESC");

	/*query.append("SELECT e.id,e.expense_name,e.amount_spent,e.expense_time,e.status,e.approved_rejected_on,e.rejected_reason,");
        query.append("c.customer_printas,a.appointment_title,IFNULL (u.first_name,'') first_name,IFNULL (u.last_name,'') last_name,ue.first_name,ue.last_name,ec.category_name FROM expenses e");
        query.append(" INNER JOIN customers c ON e.customer_id_fk = c.id");
        query.append(" INNER JOIN appointments a ON a.id=e.appointment_id_fk");
        query.append(" LEFT OUTER JOIN fieldsense.users u ON u.id=e.approved_rejected_by_id_fk");
        query.append(" INNER JOIN fieldsense.users ue ON ue.id=e.user_id_fk");
        query.append(" LEFT OUTER JOIN expense_categories ec ON e.category_id_fk=ec.id");
        query.append(" WHERE e.user_id_fk=? AND (Date(e.expense_time) BETWEEN DATE(?) AND DATE(?)) ORDER BY e.expense_time ASC,ec.category_name ASC");*/

        log4jLog.info(" selectExpenseReport " + query);
        Object param[] = new Object[]{userId, fromDate, toDate};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Expense>() {

                @Override
                public Expense mapRow(ResultSet rs, int i) throws SQLException {
                     Expense expense = new Expense();
                    expense.setId(rs.getInt("id"));
                    expense.setAppointmentId(rs.getInt("appointment_id_fk"));
                    expense.setCustomerId(rs.getInt("customer_id_fk"));
                    User user1 = new User();
                    int user_id=rs.getInt("ue.id");
                    user1.setId(user_id);
                    user1.setFirstName(rs.getString("ue.first_name"));
                    user1.setLastName(rs.getString("ue.last_name"));
                    user1.setFullname(rs.getString("ue.full_name"));
                    expense.setUser(user1);
                    expense.setExpenseName(rs.getString("expense_name"));
                    expense.setExpenseType(rs.getInt("Expense_type_id_fk"));
                    expense.setDescription(rs.getString("desription"));
                    expense.setAmountSpent(rs.getDouble("amount_spent"));
                    expense.setStatus(rs.getInt("status"));
                    expense.setExpenseTime(rs.getTimestamp("expense_time"));
                    expense.setSubmittedOn(rs.getTimestamp("submitted_on"));
                    expense.setApprovedOrRejectedOn(rs.getTimestamp("approved_rejected_on"));
                    User user = new User();
                    user.setId(rs.getInt("u.id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setFullname(rs.getString("full_name"));
                    expense.setApprovedOrRjectedBy(user);
                    expense.setRejectedReson(rs.getString("rejected_reason"));
                    expense.setCreatedOn(rs.getTimestamp("created_on"));
                    ExpenseCategory eCategory = new ExpenseCategory();
                    eCategory.setCategoryName(rs.getString("ec.category_name"));
                    eCategory.setId(rs.getInt("ec.id"));
                    expense.seteCategoryName(eCategory);
                    expense.setDisburse_amount(rs.getDouble("disburse_amount"));
                    expense.setPayment_mode(rs.getString("payment_mode"));
                    expense.setDefault_date(rs.getTimestamp("default_date"));
                    expense.setExp_category_name(rs.getString("exp_category_name"));
                    expense.setCustomerName(rs.getString("customer_name"));
                    Appointment appointment = new Appointment();
                    appointment.setTitle(rs.getString("appointment_title"));
                    expense.setAppointment(appointment);
                    User reporting_head=new User();
                    reporting_head.setFullname(rs.getString("reporting_head"));
                    expense.setReport_head(reporting_head);
                    return expense;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectExpenseReport " + e);
//            e.printStackTrace();
            return new ArrayList<Expense>();
        }
    }

    /**
     *
     * @param allRequestParams
     * @param userId
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    @Override
    public List<Expense> selectAccountExpenseReport(@RequestParam Map<String,String> allRequestParams,int userId, String fromDate, String toDate, final int accountId) {
        StringBuilder query = new StringBuilder();

        /*query.append("SELECT e.id,e.expense_name,e.amount_spent,e.expense_time,e.status,e.approved_rejected_on,e.rejected_reason,");
        query.append("c.customer_printas,a.appointment_title,IFNULL (u.first_name,'') first_name,IFNULL (u.last_name,'') last_name,ue.first_name,ue.last_name FROM expenses e");
        query.append(" INNER JOIN customers c ON e.customer_id_fk = c.id");
        query.append(" INNER JOIN appointments a ON a.id=e.appointment_id_fk");
        query.append(" LEFT OUTER JOIN fieldsense.users u ON u.id=e.approved_rejected_by_id_fk");
        query.append(" INNER JOIN fieldsense.users ue ON ue.id=e.user_id_fk");
        query.append(" WHERE e.user_id_fk=? AND (Date(e.expense_time) BETWEEN DATE(?) AND DATE(?)) ORDER BY e.expense_time ASC");*/

        query.append("SELECT e.id,e.expense_name,e.amount_spent,e.expense_time,e.status,e.expense_image_id_csv,e.approved_rejected_on,e.rejected_reason,IFNULL (e.disburse_amount,0) disburse_amount,e.exp_category_name,");
        query.append("c.customer_printas,c.customer_name,a.appointment_title,IFNULL (u.first_name,'') first_name,IFNULL (u.last_name,'') last_name,IFNULL (u.full_name,'') full_name,ue.id,ue.report_to,ue.first_name,ue.last_name,ue.full_name,IFNULL(ue1.full_name,'') reporting_head,ec.category_name FROM expenses e");
        query.append(" LEFT JOIN customers c ON e.customer_id_fk = c.id");
        query.append(" LEFT JOIN appointments a ON a.id=e.appointment_id_fk");
        query.append(" LEFT OUTER JOIN fieldsense.users u ON u.id=e.approved_rejected_by_id_fk");
        query.append(" INNER JOIN fieldsense.users ue ON ue.id=e.user_id_fk");
        query.append(" INNER JOIN fieldsense.users ue1 ON ue1.id=ue.report_to");
        query.append(" LEFT OUTER JOIN expense_categories ec ON e.category_id_fk=ec.id");
        query.append(" WHERE e.user_id_fk=? AND (e.expense_time BETWEEN ? AND ?) ");
        String searchText=allRequestParams.get("search[value]").trim();
        if(!searchText.equals("")){
            query.append(" AND (ue.full_name like ? OR e.expense_time like ? OR e.amount_spent like ? OR e.exp_category_name like ? OR ue1.full_name like ? ) ");
        }
        String sortcolindex=allRequestParams.get("order[0][column]");
        if(sortcolindex==null){
            query.append(" ORDER BY e.expense_time desc");
        }else{
            switch(Integer.parseInt(sortcolindex)){
                case 0:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append("ORDER BY ue.full_name");
                    }else{
                        query.append("ORDER BY ue.full_name DESC");
                    }
                    break;
                case 1:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append("ORDER BY e.expense_time");
                    }else{
                        query.append("ORDER BY e.expense_time DESC");
                    }
                    break;
                case 2:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append("ORDER BY e.amount_spent");
                    }else{
                        query.append("ORDER BY e.amount_spent DESC");
                    }
                    break;
                case 3:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append("ORDER BY e.exp_category_name");
                    }else{
                        query.append("ORDER BY e.exp_category_name DESC");
                    }
                    break;
               /* case 4:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append("ORDER BY e.status");
                    }else{
                        query.append("ORDER BY e.status DESC");
                    }
                    break;*/
                case 5:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append("ORDER BY reporting_head");
                    }else{
                        query.append("ORDER BY reporting_head DESC");
                    }
                    break;    
                default:
                   query.append(" ORDER BY e.expense_time desc");
                   break;
            }        
        }
        log4jLog.info(" selectExpenseReport " + query);
        final int start=Integer.parseInt(allRequestParams.get("start"));
        final int length=Integer.parseInt(allRequestParams.get("length"));
        Object param[]=new Object[10];
        if(searchText.equals("")){
            param = new Object[]{userId, fromDate, toDate};
        }else{
            param =  new Object[]{userId, fromDate, toDate,"%"+searchText+"%","%"+searchText+"%","%"+searchText+"%","%"+searchText+"%","%"+searchText+"%"};
        }
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Expense>() {

                @Override
                public Expense mapRow(ResultSet rs, int i) throws SQLException {
                    if((i>=start && i<start+length) || length==-1){
                        Expense expense = new Expense();
                        expense.setId(rs.getInt("id"));
                        expense.setExpenseName(rs.getString("expense_name"));
                        expense.setAmountSpent(rs.getDouble("amount_spent"));
                        expense.setExpenseTime(rs.getTimestamp("expense_time"));
                        expense.setExpenseImageCsv(rs.getString("expense_image_id_csv"));
                        expense.setStatus(rs.getInt("status"));
                        expense.setApprovedOrRejectedOn(rs.getTimestamp("approved_rejected_on"));
                        expense.setRejectedReson(rs.getString("rejected_reason"));
                        expense.setCustomerName(rs.getString("customer_name"));
                        expense.setExp_category_name(rs.getString("exp_category_name"));
                        expense.setDisburse_amount(rs.getDouble("disburse_amount"));
                        Appointment appointment = new Appointment();
                        appointment.setTitle(rs.getString("appointment_title"));
                        expense.setAppointment(appointment);
                        User user = new User();
                        user.setFirstName(rs.getString("first_name"));
                        user.setLastName(rs.getString("last_name"));
                        user.setFullname(rs.getString("full_name"));
                        expense.setApprovedOrRjectedBy(user);
                        User user1 = new User();
                        int user_id=rs.getInt("ue.id");
                        user1.setId(user_id);
                        user1.setFirstName(rs.getString("ue.first_name"));
                        user1.setLastName(rs.getString("ue.last_name"));
                        user1.setFullname(rs.getString("ue.full_name"));
                        expense.setUser(user1);
                        ExpenseCategory eCategory = new ExpenseCategory();
                        eCategory.setCategoryName(rs.getString("ec.category_name"));
                        expense.seteCategoryName(eCategory);
                        User reporting_head=new User();
                        reporting_head.setFullname(rs.getString("reporting_head"));
                        expense.setReport_head(reporting_head);
                        return expense;
                    }else{
                            Expense expense = new Expense();
                            expense.setStatus(rs.getInt("status"));
                            expense.setExpenseImageCsv(rs.getString("expense_image_id_csv"));
                            expense.setExp_category_name(rs.getString("exp_category_name"));
                            expense.setExpenseTime(Timestamp.valueOf("1111-11-11 11:11:11"));
                            return expense;
                    }     
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectExpenseReport " + e);
            e.printStackTrace();
            return new ArrayList<Expense>();
        }
    }
    
    /**
     *
     * @param allRequestParams
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    @Override
    public List<Expense> selectAllAccountExpenseReport(@RequestParam Map<String,String> allRequestParams,String fromDate, String toDate, final int accountId) {
        StringBuilder query = new StringBuilder();

        query.append("SELECT e.id,e.expense_name,e.amount_spent,e.expense_image_id_csv,e.expense_time,e.status,e.approved_rejected_on,e.rejected_reason,IFNULL (e.disburse_amount,0) disburse_amount,e.exp_category_name,");
        query.append("c.customer_printas,c.customer_name,a.appointment_title,IFNULL (u.first_name,'') first_name,IFNULL (u.last_name,'') last_name,IFNULL (u.full_name,'') full_name,ue.id,ue.report_to,ue.first_name,ue.last_name,ue.full_name,IFNULL(ue1.full_name,'') reporting_head,ec.category_name FROM expenses e");
        query.append(" LEFT JOIN customers c ON e.customer_id_fk = c.id");
        query.append(" LEFT JOIN appointments a ON a.id=e.appointment_id_fk");
        query.append(" LEFT OUTER JOIN fieldsense.users u ON u.id=e.approved_rejected_by_id_fk");
        query.append(" INNER JOIN fieldsense.users ue ON ue.id=e.user_id_fk and ue.active=1");
        query.append(" INNER JOIN fieldsense.users ue1 ON ue1.id=ue.report_to");
        query.append(" LEFT OUTER JOIN expense_categories ec ON e.category_id_fk=ec.id");
        query.append(" WHERE (e.expense_time BETWEEN ? AND ?) ");
        String searchText=allRequestParams.get("search[value]").trim();
        if(!searchText.equals("")){
            if(searchText.contains("pen")){
                query.append(" AND (ue.full_name like ? OR e.expense_time like ? OR e.amount_spent like ? OR exp_category_name like ? OR ue1.full_name like ? OR e.status=0 OR e.status=1 ) ");
            }else if(searchText.contains("rej")){
                query.append(" AND (ue.full_name like ? OR e.expense_time like ? OR e.amount_spent like ? OR exp_category_name like ? OR ue1.full_name like ? OR e.status=2 OR e.status=4 ) ");
            }else if(searchText.contains("app")){
                query.append(" AND (ue.full_name like ? OR e.expense_time like ? OR e.amount_spent like ? OR exp_category_name like ? OR ue1.full_name like ? OR e.status=3 ) ");
            }else if(searchText.contains("dis")){
                query.append(" AND (ue.full_name like ? OR e.expense_time like ? OR e.amount_spent like ? OR exp_category_name like ? OR ue1.full_name like ? OR e.status=5 ) ");
            }else{
                query.append(" AND (ue.full_name like ? OR e.expense_time like ? OR e.amount_spent like ? OR exp_category_name like ? OR ue1.full_name like ?  ) ");
            }
        }
        String sortcolindex=allRequestParams.get("order[0][column]");
        if(sortcolindex==null){
            query.append(" ORDER BY e.expense_time desc");
        }else{
            switch(Integer.parseInt(sortcolindex)){
                case 0:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append("ORDER BY ue.full_name");
                    }else{
                        query.append("ORDER BY ue.full_name DESC");
                    }
                    break;
                case 1:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append("ORDER BY e.expense_time");
                    }else{
                        query.append("ORDER BY e.expense_time DESC");
                    }
                    break;
                case 2:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append("ORDER BY e.amount_spent");
                    }else{
                        query.append("ORDER BY e.amount_spent DESC");
                    }
                    break;
                case 3:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append("ORDER BY e.exp_category_name");
                    }else{
                        query.append("ORDER BY e.exp_category_name DESC");
                    }
                    break;
               /* case 4:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append("ORDER BY e.status");
                    }else{
                        query.append("ORDER BY e.status DESC");
                    }
                    break;*/
                case 5:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append("ORDER BY reporting_head");
                    }else{
                        query.append("ORDER BY reporting_head DESC");
                    }
                    break;    
                default:
                   query.append(" ORDER BY e.expense_time desc");
                   break;
            }        
        }
        log4jLog.info(" selectExpenseReport " + query);
	final int start=Integer.parseInt(allRequestParams.get("start"));
        final int length=Integer.parseInt(allRequestParams.get("length"));
        Object param[]=new Object[10];
        if(searchText.equals("")){
            param = new Object[]{fromDate, toDate};
        }else{
            param =  new Object[]{fromDate, toDate,"%"+searchText+"%","%"+searchText+"%","%"+searchText+"%","%"+searchText+"%","%"+searchText+"%"};
        }
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Expense>() {

                @Override
                public Expense mapRow(ResultSet rs, int i) throws SQLException {
                    if((i>=start && i<start+length) || length==-1){
                        Expense expense = new Expense();
                        expense.setId(rs.getInt("id"));
                        expense.setExpenseName(rs.getString("expense_name"));
                        expense.setAmountSpent(rs.getDouble("amount_spent"));
                        expense.setExpenseTime(rs.getTimestamp("expense_time"));
                        expense.setStatus(rs.getInt("status"));
                        expense.setApprovedOrRejectedOn(rs.getTimestamp("approved_rejected_on"));
                        expense.setRejectedReson(rs.getString("rejected_reason"));
                        expense.setCustomerName(rs.getString("customer_name"));
                        expense.setExp_category_name(rs.getString("exp_category_name"));
                        expense.setDisburse_amount(rs.getDouble("disburse_amount"));
                        expense.setExpenseImageCsv(rs.getString("e.expense_image_id_csv"));
                        Appointment appointment = new Appointment();
                        appointment.setTitle(rs.getString("appointment_title"));
                        expense.setAppointment(appointment);
                        User user = new User();
                        user.setFirstName(rs.getString("first_name"));
                        user.setLastName(rs.getString("last_name"));
                        user.setFullname(rs.getString("full_name"));
                        expense.setApprovedOrRjectedBy(user);
                        User user1 = new User();
                        int user_id=rs.getInt("ue.id");
                        user1.setId(user_id);
                        user1.setAccountId(accountId);
                        user1.setFirstName(rs.getString("ue.first_name"));
                        user1.setLastName(rs.getString("ue.last_name"));
                        user1.setFullname(rs.getString("ue.full_name"));
                        expense.setUser(user1);
                        ExpenseCategory eCategory = new ExpenseCategory();
                        eCategory.setCategoryName(rs.getString("ec.category_name"));
                        expense.seteCategoryName(eCategory);
                        User reporting_head=new User();
                        reporting_head.setFullname(rs.getString("reporting_head"));
                        expense.setReport_head(reporting_head);
                        return expense;
                    }else{
                            Expense expense = new Expense();
                            expense.setStatus(rs.getInt("status"));
                            expense.setExpenseImageCsv(rs.getString("e.expense_image_id_csv"));
                            expense.setExp_category_name(rs.getString("exp_category_name"));
                            expense.setExpenseTime(Timestamp.valueOf("1111-11-11 11:11:11"));
                            return expense;
                    }    	
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectExpenseReport " + e);
            e.printStackTrace();
            return new ArrayList<Expense>();
        }
    }
    
    /**
     *
     * @param userId
     * @param allRequestParams
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    public List<Appointment> selectVistReport(int userId,List<User> userList,Map<String,String> allRequestParams,String fromDate, String toDate, int accountId) {
        StringBuilder query = new StringBuilder();
        /*query.append("SELECT a.appointment_title,a.appointment_time,a.status,a.outcome,ao.outcome,ap.purpose,c.customer_printas,");
        query.append("u.first_name,u.last_name FROM appointments a");
        query.append(" INNER JOIN activity_outcomes ao ON a.outcome=ao.id");
        query.append(" INNER JOIN activity_purpose ap ON a.purpose_id_fk=ap.id");
        query.append(" INNER JOIN customers c ON a.customer_id_fk=c.id");
        query.append(" INNER JOIN fieldsense.users u ON u.id=a.user_id_fk");
        query.append(" WHERE a.assigned_id_fk=? AND (DATE(a.appointment_time) BETWEEN DATE(?) AND DATE(?)) ORDER BY a.appointment_time ASC");*/

        query.append("SELECT a.appointment_title,a.appointment_time,a.status,a.outcome,a.appointment_end_time,a.outcome_description,a.check_in_time,a.check_out_time,ao.outcome,ap.purpose,c.customer_printas,c.customer_name,c.customer_location_identifier, c.address1, "); //modified by manohar
        query.append("u.first_name,u.last_name,ua.first_name,ua.last_name FROM appointments a");
        query.append(" INNER JOIN activity_outcomes ao ON a.outcome=ao.id");
        query.append(" INNER JOIN activity_purpose ap ON a.purpose_id_fk=ap.id");
        query.append(" INNER JOIN customers c ON a.customer_id_fk=c.id");
        query.append(" INNER JOIN fieldsense.users u ON u.id=a.user_id_fk");
        query.append(" INNER JOIN fieldsense.users ua ON ua.id=a.assigned_id_fk");
        
        if(userId == 0){
            ArrayList<Integer> userIdList= new ArrayList<Integer>();
            for(User user: userList){
                userIdList.add(user.getId());
            }
             query.append(" WHERE a.record_state!=3 AND a.assigned_id_fk in(");
            for(int k=0;k<userIdList.size();k++){
                    query.append(userIdList.get(k));
                    if(k != (userIdList.size()-1)){
                        query.append(",");
                    }
            }
              query.append(")");
                    
              query.append("  AND ( a.appointment_time BETWEEN ? AND ? )");
        }else{
         query.append(" WHERE a.record_state!=3 AND a.assigned_id_fk=? AND (a.appointment_time BETWEEN ? AND ?) ");   
            
        }
        
        log4jLog.info(" selectVistReport in ADMIN USERId " + userId);
        String sortcolindex=allRequestParams.get("order[0][column]");
        if(sortcolindex==null){
            query.append("ORDER BY a.appointment_time DESC");
        }else if(Integer.parseInt(sortcolindex)==0){
            if(allRequestParams.get("order[0][dir]").equals("asc")){
                query.append("ORDER BY c.customer_name");
            }else{
                query.append("ORDER BY c.customer_name DESC");
            }
        }else if(Integer.parseInt(sortcolindex)==2){
            if(allRequestParams.get("order[0][dir]").equals("asc")){
                query.append("ORDER BY a.appointment_time");
            }else{
                query.append("ORDER BY a.appointment_time DESC");
            }
        }
        log4jLog.info(" selectVistReport ADMIN " + query);
        final int start=Integer.parseInt(allRequestParams.get("start"));
        final int length=Integer.parseInt(allRequestParams.get("length"));
        Object param[] = null;
        if(userId == 0){
            param = new Object[]{fromDate, toDate};
            
        }else{
                param = new Object[]{userId, fromDate, toDate};
        }
                
            
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Appointment>() {

                @Override
                public Appointment mapRow(ResultSet rs, int i) throws SQLException {
                    if((i>=start && i<start+length) || length==-1){
                        Appointment appointment = new Appointment();
                        appointment.setTitle(rs.getString("appointment_title"));
                        appointment.setDateTime(rs.getTimestamp("appointment_time"));
                        appointment.setSdateTime(appointment.getDateTime().toString());
                        appointment.setStatus(rs.getInt("status"));
                        
                        appointment.setEndTime(rs.getTimestamp("appointment_end_time"));
                        appointment.setSendTime(appointment.getEndTime().toString());
                        appointment.setOutcomeDescription(rs.getString("outcome_description"));
                        appointment.setCheckInTime(rs.getTimestamp("check_in_time"));
                        appointment.setCheckOutTime(rs.getTimestamp("check_out_time"));
                        appointment.setScheckInTime(appointment.getCheckInTime().toString());
                        appointment.setScheckOutTime(appointment.getCheckOutTime().toString());



                        ActivityOutcome outcome = new ActivityOutcome();
                        outcome.setOutcome(rs.getString("ao.outcome"));
                        appointment.setOutcomes(outcome);

                        ActivityPurpose activityPurpose = new ActivityPurpose();
                        activityPurpose.setPurpose(rs.getString("purpose"));
                        appointment.setPurpose(activityPurpose);

                        Customer customer = new Customer();
                        customer.setCustomerPrintas(rs.getString("customer_printas"));
                        customer.setCustomerName(rs.getString("customer_name"));
                        customer.setCustomerLocation(rs.getString("customer_location_identifier")); // modified by manohar
                        customer.setCustomerAddress1(rs.getString("address1").trim()); // added by jyoti
                        appointment.setCustomer(customer);

                        User owner = new User();
                        owner.setFirstName(rs.getString("u.first_name"));
                        owner.setLastName(rs.getString("u.last_name"));
                        appointment.setOwner(owner);

                        User assigned = new User();
                        assigned.setFirstName(rs.getString("ua.first_name"));
                        assigned.setLastName(rs.getString("ua.last_name"));
//                        System.out.println("inside selct visit report");
//                        assigned.setFullname(rs.getString("ua.full_name"));// added by nikhil
                        appointment.setAssignedTo(assigned);
//                       System.out.println("appointment %% "+appointment.toString());
                        return appointment;
                    }else{
                        Appointment appointment = new Appointment();
                        appointment.setSdateTime("1111-11-11");
                        return appointment;
                    }    
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectVistReport " + e);
//            e.printStackTrace();
            return new ArrayList<Appointment>();
        }
    }

    /**
     *
     * @param userId
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    public List<Appointment> selectVistReportCsv(int userId,List<User> subordinateList, String fromDate, String toDate, int accountId) {
        StringBuilder query = new StringBuilder();
        /*query.append("SELECT a.appointment_title,a.appointment_time,a.status,a.outcome,ao.outcome,ap.purpose,c.customer_printas,");
        query.append("u.first_name,u.last_name FROM appointments a");
        query.append(" INNER JOIN activity_outcomes ao ON a.outcome=ao.id");
        query.append(" INNER JOIN activity_purpose ap ON a.purpose_id_fk=ap.id");
        query.append(" INNER JOIN customers c ON a.customer_id_fk=c.id");
        query.append(" INNER JOIN fieldsense.users u ON u.id=a.user_id_fk");
        query.append(" WHERE a.assigned_id_fk=? AND (DATE(a.appointment_time) BETWEEN DATE(?) AND DATE(?)) ORDER BY a.appointment_time ASC");*/
        log4jLog.info(" selectVistReport CSV USERID" + userId);
        query.append("SELECT a.appointment_title,a.appointment_time,a.status,a.outcome,a.appointment_end_time,a.outcome_description,a.check_in_time,a.check_out_time,ao.outcome,ap.purpose,c.customer_printas,c.customer_name,c.customer_location_identifier, c.address1, "); //modified by manohar
        query.append("u.first_name,u.last_name,ua.first_name,ua.last_name FROM appointments a");
        query.append(" INNER JOIN activity_outcomes ao ON a.outcome=ao.id");
        query.append(" INNER JOIN activity_purpose ap ON a.purpose_id_fk=ap.id");
        query.append(" INNER JOIN customers c ON a.customer_id_fk=c.id");
        query.append(" INNER JOIN fieldsense.users u ON u.id=a.user_id_fk");
        query.append(" INNER JOIN fieldsense.users ua ON ua.id=a.assigned_id_fk");
        if(userId == 0){
            ArrayList<Integer> subuserIdList= new ArrayList<Integer>();
            for(User user: subordinateList){
                subuserIdList.add(user.getId());
            }
            query.append(" WHERE a.record_state!=3 AND a.assigned_id_fk in(");
            for(int k=0;k<subuserIdList.size();k++){
                    query.append(subuserIdList.get(k));
                    if(k != (subuserIdList.size()-1)){
                        query.append(",");
                    }
            }
              query.append(")");
                    
                 query.append("  AND ( a.appointment_time BETWEEN ? AND ? ) ORDER BY a.appointment_time DESC");
        }else{
         query.append(" WHERE a.record_state!=3 AND a.assigned_id_fk=? AND (a.appointment_time BETWEEN ? AND ?) ORDER BY a.appointment_time DESC");   
        }
        
        log4jLog.info(" selectVistReport " + query);
         Object param[] = null;
        if(userId ==0){
            param= new Object[]{fromDate, toDate};
        }else{
            param = new Object[]{userId, fromDate, toDate};
        }
        
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Appointment>() {

                @Override
                public Appointment mapRow(ResultSet rs, int i) throws SQLException {
                    Appointment appointment = new Appointment();
                    appointment.setTitle(rs.getString("appointment_title"));
                    appointment.setDateTime(rs.getTimestamp("appointment_time"));
                    appointment.setSdateTime(appointment.getDateTime().toString());
                    appointment.setStatus(rs.getInt("status"));
                    
                    appointment.setEndTime(rs.getTimestamp("appointment_end_time"));
                    appointment.setSendTime(appointment.getEndTime().toString());
                    appointment.setOutcomeDescription(rs.getString("outcome_description"));
                    appointment.setCheckInTime(rs.getTimestamp("check_in_time"));
                    appointment.setCheckOutTime(rs.getTimestamp("check_out_time"));
                    appointment.setScheckInTime(appointment.getCheckInTime().toString());
                    appointment.setScheckOutTime(appointment.getCheckOutTime().toString());


                    ActivityOutcome outcome = new ActivityOutcome();
                    outcome.setOutcome(rs.getString("ao.outcome"));
                    appointment.setOutcomes(outcome);

                    ActivityPurpose activityPurpose = new ActivityPurpose();
                    activityPurpose.setPurpose(rs.getString("purpose"));
                    appointment.setPurpose(activityPurpose);

                    Customer customer = new Customer();
                    customer.setCustomerPrintas(rs.getString("customer_printas"));
                    customer.setCustomerName(rs.getString("customer_name"));
                    customer.setCustomerLocation(rs.getString("customer_location_identifier")); // modified by manohar
                    customer.setCustomerAddress1(rs.getString("address1").trim()); // added by jyoti                    
                    appointment.setCustomer(customer);

                    User owner = new User();
                    owner.setFirstName(rs.getString("u.first_name"));
                    owner.setLastName(rs.getString("u.last_name"));
                    appointment.setOwner(owner);

                    User assigned = new User();
                    assigned.setFirstName(rs.getString("ua.first_name"));
                    assigned.setLastName(rs.getString("ua.last_name"));
                    appointment.setAssignedTo(assigned);
                    
                    //appointment.setCheckAllVisits("All");
//                    System.out.println("appointment #"+appointment.toString());
//                    System.out.println("appointment #"+appointment.toString());
                    
                    return appointment;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectVistReport " + e);
            e.printStackTrace();
            return new ArrayList<Appointment>();
        }
    }

    /**
     *
     * @param allRequestParams
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    @Override
    public List<Attendance> selectAttendaceReportForAdmin(Map<String,String> allRequestParams,String fromDate, String toDate, int accountId) {
//        String query = "SELECT user_id_fk,punch_date,punch_in,punch_in_location,punch_out,punch_out_location FROM attendances WHERE user_id_fk=? AND punch_date BETWEEN ? AND ? ORDER BY punch_date DESC";
        StringBuilder query = new StringBuilder();
        query.append("SELECT u.emp_code AS emp_code, a.user_id_fk,a.punch_date,a.punch_in,a.punch_in_location,a.punch_out,punch_out_location,a.punch_out_date, ");
        query.append(" CONCAT_WS(' ',u.first_name,u.last_name) AS userName,a.punch_in_location_reason,a.punch_out_location_reason,");
        query.append(" (select count(k.id) from attendances k INNER JOIN fieldsense.users f ON k.user_id_fk=f.id WHERE punch_date ");
        query.append(" between ? and ? ) as totalrecords,(SELECT   SUM( TIME_TO_SEC( `interval_time` )  )  FROM attendances_timeout  timeout ");
        query.append(" WHERE  timeout.timeout_date >= a.punch_date and a.user_id_fk=timeout.user_id_fk) AS time_out_sum "); // edited by siddhesh changed "=" to ">="
        //query.append(" FROM attendances a INNER JOIN fieldsense.users u ON a.user_id_fk=u.id WHERE punch_date between ? and ? ");
        query.append(" FROM attendances a INNER JOIN fieldsense.users u ON a.user_id_fk=u.id WHERE CONCAT(a.punch_date,' ',a.punch_in) between ? and ? ");      
        String sortcolindex=allRequestParams.get("order[0][column]");
        if(sortcolindex==null){
            query.append(" ORDER BY punch_date DESC");
        }else if(Integer.parseInt(sortcolindex)==2){
            if(allRequestParams.get("order[0][dir]").equals("asc")){
                query.append(" ORDER BY punch_date");
            }else{
                query.append(" ORDER BY punch_date DESC");
            }
        }else if(Integer.parseInt(sortcolindex)==1){
            if(allRequestParams.get("order[0][dir]").equals("asc")){
                query.append(" ORDER BY CONCAT_WS(' ',u.first_name,u.last_name)");
            }else{
                query.append(" ORDER BY CONCAT_WS(' ',u.first_name,u.last_name) DESC");
            }
        }else if(Integer.parseInt(sortcolindex)==5){
            if(allRequestParams.get("order[0][dir]").equals("asc")){
                query.append(" ORDER BY punch_out_date");
            }else{
                query.append(" ORDER BY punch_out_date DESC");
            }
        }
        log4jLog.info(" selectAttendaceReportForAdmin " + query);
        final int start=Integer.parseInt(allRequestParams.get("start"));
        final int length=Integer.parseInt(allRequestParams.get("length"));
        Object param[]=new Object[10];
        if(length!=-1){
            query.append(" limit ? offset ?");
            param = new Object[]{fromDate, toDate,fromDate, toDate,length,start};
        }else{
            param = new Object[]{fromDate, toDate,fromDate, toDate};
        }    
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Attendance>() {

                @Override
                public Attendance mapRow(ResultSet rs, int i) throws SQLException {
                    //if(i>=start && i<start+length || length==-1){
                        Attendance attendance = new Attendance();
                        attendance.setUserId(rs.getInt("user_id_fk"));
                        attendance.setPunchDate(rs.getString("punch_date"));
                        attendance.setPunchInTime(rs.getString("punch_in"));
                        attendance.setPunchInLocation(rs.getString("punch_in_location"));
                        attendance.setPunchOutDate(rs.getString("punch_out_date"));
                        attendance.setPunchOutTime(rs.getString("punch_out"));
                        attendance.setPunchOutLocation(rs.getString("punch_out_location"));
                        attendance.setUserName(rs.getString("userName"));
                        attendance.setEmp_code(rs.getString("emp_code"));
                        attendance.setPunchInLocationNotFoundReason(rs.getString("punch_in_location_reason"));
                        attendance.setPunchOutLocationNotFoundReason(rs.getString("punch_out_location_reason"));
                        attendance.setTotalrecords(Integer.parseInt(rs.getString("totalrecords")));
                        /*if(rs.getTime("time_out_sum")==null){
                            attendance.setTotalTimeOut("000000");
                        }else{    
                            attendance.setTotalTimeOut(rs.get("time_out_sum").toString());
                        }*/
                        Long intvl=rs.getLong("time_out_sum");
                        attendance.setTotalTimeOut(fromSecToStringTime(intvl));
                        if(!(attendance.getPunchOutTime().equals("00:00:00") && attendance.getPunchOutDate().equals("1111-11-11"))){
                            attendance.setWorkHr(calculateWorkHr(attendance.getPunchDate(),attendance.getPunchInTime(),attendance.getPunchOutDate(),attendance.getPunchOutTime()));
                        }
                        return attendance;
                   // }else{
                      //  Attendance attendance = new Attendance();
                       // attendance.setPunchDate("1111-11-11");
                       ////return attendance;
                   // }
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAttendaceReportForAdmin " + e);
            e.printStackTrace();
            return new ArrayList<Attendance>();
        }
    }
    
    /**
     *
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    @Override
    public List<Attendance> selectAttendaceReportCsvForAdmin(String fromDate, String toDate, int accountId) {
//        String query = "SELECT user_id_fk,punch_date,punch_in,punch_in_location,punch_out,punch_out_location FROM attendances WHERE user_id_fk=? AND punch_date BETWEEN ? AND ? ORDER BY punch_date DESC";
        StringBuilder query = new StringBuilder();
        query.append("SELECT a.user_id_fk,a.punch_date,a.punch_in,a.punch_in_location,a.punch_out,a.punch_out_date,punch_out_location,CONCAT_WS(' ',u.first_name,u.last_name) AS userName,a.punch_in_location_reason,a.punch_out_location_reason,u.emp_code AS emp_code, ");
        query.append(" IFNULL ((SELECT  SEC_TO_TIME( SUM( TIME_TO_SEC( `interval_time` ) ) )  FROM attendances_timeout  timeout ");
        query.append(" WHERE  timeout.timeout_date >= a.punch_date and a.user_id_fk=timeout.user_id_fk),'00:00:00') AS time_out_sum "); // edited by siddhesh changed "=" to ">="
        query.append(" FROM attendances a");
        query.append(" INNER JOIN fieldsense.users u ON a.user_id_fk=u.id");
        query.append(" WHERE CONCAT(a.punch_date,' ',a.punch_in) BETWEEN ? AND ? ORDER BY punch_date DESC");
        //query.append(" WHERE punch_date BETWEEN ? AND ? ORDER BY punch_date DESC");
        log4jLog.info(" selectAttendaceReportForAdmin " + query);
        Object param[] = new Object[]{fromDate, toDate};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Attendance>() {

                @Override
                public Attendance mapRow(ResultSet rs, int i) throws SQLException {
                    Attendance attendance = new Attendance();
                    attendance.setUserId(rs.getInt("user_id_fk"));
                    attendance.setPunchDate(rs.getString("punch_date"));
                    attendance.setPunchInTime(rs.getString("punch_in"));
                    attendance.setPunchInLocation(rs.getString("punch_in_location"));
                    attendance.setPunchOutDate(rs.getString("punch_out_date"));
                    attendance.setPunchOutTime(rs.getString("punch_out"));
                    attendance.setPunchOutLocation(rs.getString("punch_out_location"));
                    attendance.setUserName(rs.getString("userName"));
                    attendance.setEmp_code(rs.getString("emp_code"));
                    attendance.setPunchInLocationNotFoundReason(rs.getString("punch_in_location_reason"));
                    attendance.setPunchOutLocationNotFoundReason(rs.getString("punch_out_location_reason"));                    
//                    attendance.setTotalTimeOut(rs.getTime("time_out_sum").toString()); // commented by Jyoti
                    // Added by jyoti
                    Long intvl=rs.getLong("time_out_sum");
                    attendance.setTotalTimeOut(fromSecToStringTime(intvl));
                    // ended by Jyoti, 01-aug-2017
                    if(!(attendance.getPunchOutTime().equals("00:00:00") && attendance.getPunchOutDate().equals("1111-11-11"))){
                            attendance.setWorkHr(calculateWorkHr(attendance.getPunchDate(),attendance.getPunchInTime(),attendance.getPunchOutDate(),attendance.getPunchOutTime()));
                    }
                    return attendance;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAttendaceReportForAdmin " + e);
//            e.printStackTrace();
            return new ArrayList<Attendance>();
        }
    }

    /**
     *
     * @param userId
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    @Override
    public List<UsersTravelLogs> selectUsersTravelledReoprt(int userId, String fromDate, String toDate, int accountId) {
//        String query = "SELECT created_on,user_id_fk,latitude,longitude,location,id,source_value,distance_travel,(Select count(id) FROM user_travel_logs WHERE (created_on BETWEEN ? AND ?) AND user_id_fk=? AND distance_travel IS NULL) as nullCount FROM user_travel_logs WHERE (created_on BETWEEN ? AND ?) AND user_id_fk=? group by created_on"; //order by id replaced with created_on //#siddhesh 30-06-17 updated for travel log issue multiple entries in reports 'distinct' added for created_on // commented by jyoti
        String query = "SELECT created_on,user_id_fk,latitude,longitude,location,id,source_value,distance_travel, battery_Percentage, isGPS, network_type, app_version, oS_Version, device_Name, isMockLocation, isShowIntoReports, customer_id_fk, (Select count(id) FROM user_travel_logs WHERE (created_on BETWEEN ? AND ?) AND user_id_fk=? AND distance_travel IS NULL) as nullCount FROM user_travel_logs WHERE (created_on BETWEEN ? AND ?) AND user_id_fk=? group by created_on"; // added by jyoti, 2018-07-30, enhancement - to fetch only those reports which has isShowIntoReports = 1
        log4jLog.info(" selectUsersTravelLogs " + query);
        try {
            Object param[] = new Object[]{fromDate, toDate, userId,fromDate, toDate, userId};
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<UsersTravelLogs>() {

                @Override
                public UsersTravelLogs mapRow(ResultSet rs, int i) throws SQLException {
                    UsersTravelLogs usersTravelLogs = new UsersTravelLogs();
                    usersTravelLogs.setId(rs.getInt("id"));
                    usersTravelLogs.setUserId(rs.getInt("user_id_fk"));
                    usersTravelLogs.setLatitude(rs.getDouble("latitude"));
                    usersTravelLogs.setLangitude(rs.getDouble("longitude"));
                    usersTravelLogs.setLocation(rs.getString("location"));
                    usersTravelLogs.setCreatedOn(rs.getTimestamp("created_on"));
                    usersTravelLogs.setSourceValue(rs.getInt("source_value"));
                    usersTravelLogs.setNullDistanceCount(rs.getInt("nullCount"));
                    double distance = rs.getDouble("distance_travel")/1000;
                    //Added by siddhesh for precision data in distance
                    DecimalFormat df = new DecimalFormat("#.0000");
                    distance =  Double.valueOf(df.format(distance));
                    // end siddhesh
                    usersTravelLogs.setTravelDistance(distance); //added for distance in KM
                    // added by jyoti
                    usersTravelLogs.setBattery_Percentage(rs.getString("battery_Percentage"));
                    usersTravelLogs.setIsGPS(rs.getString("isGPS"));
                    usersTravelLogs.setNetwork_type(rs.getString("network_type"));
                    usersTravelLogs.setApp_version(rs.getString("app_version"));
                    usersTravelLogs.setoS_Version(rs.getString("oS_Version"));
                    usersTravelLogs.setDevice_Name(rs.getString("device_Name"));
                    usersTravelLogs.setIsMockLocation(rs.getString("isMockLocation"));
                    usersTravelLogs.setIsShowIntoReports(rs.getInt("isShowIntoReports"));
                    usersTravelLogs.setCustomerId(rs.getString("customer_id_fk"));
                    // ended by jyoti
                            
                    return usersTravelLogs;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectUsersTravelLogs " + e);
            e.printStackTrace();
            return new ArrayList<UsersTravelLogs>();
        }
    }
    
    /**
     *
     * @param travelUserId
     * @param allRequestParams
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    @Override
    public List<HashMap> selectMonthlyTravelReoprt(int travelUserId,Map<String,String> allRequestParams,String fromDate, String toDate, int accountId) {
        //String query = "SELECT id,user_id_fk,latitude,longitude,location,created_on,source_value FROM user_travel_logs WHERE (created_on BETWEEN ? AND ?) AND user_id_fk=? ORDER BY created_on"; //order by id replaced with created_on
        StringBuilder query = new StringBuilder();
        Object param[]=new Object[10];
        final int start=Integer.parseInt(allRequestParams.get("start"));
        final int length=Integer.parseInt(allRequestParams.get("length"));
        
        
        
        if(travelUserId==0){
            
            query.append("select uTable.id as uid,uTable.full_name as userName,IFNULL(tTable.distance,0) as totalDistance,(");
            query.append("select count(id) from appointments where check_in_time between ? and ? and assigned_id_fk=uTable.id) as totalVisits from (");
            query.append("select u.id,u.full_name from fieldsense.users u where u.account_id_fk=? and role !=0 order by u.full_name");
            if(length!=-1){
              query.append(" limit ? offset ?");  
            }
            query.append(") uTable left join (");
            query.append("select user_id_fk, IFNULL(sum(distance_travel),0) as distance from user_travel_logs where created_on between ? and ? group by user_id_fk) tTable on uTable.id = tTable.user_id_fk order by userName");
            
            if(length!=-1){
                //query.append(" limit ? offset ?");
                param = new Object[]{fromDate, toDate,accountId,length,start,fromDate, toDate};
            }else{
                param = new Object[]{fromDate, toDate,accountId,fromDate, toDate};
            }
        }else{
            // it's not taking too much time , later on we can make change and compare join+subquery vs union in this case
            query.append("select u.id as uid ,u.full_name as userName ,IFNULL(sum(t.distance_travel),0) as totalDistance ,(select count(id) from appointments where ");
            query.append("check_in_time between ? and ? and assigned_id_fk=?) as totalVisits from fieldsense.users u inner join user_travel_logs t ");
            query.append("on u.id=t.user_id_fk where t.user_id_fk=? and u.role !=0 and t.created_on between ? and ? order by u.full_name");

            if(length!=-1){
                query.append(" limit ? offset ?");
                param = new Object[]{fromDate, toDate,travelUserId,travelUserId,fromDate, toDate,length,start};
            }else{
                param = new Object[]{fromDate, toDate,travelUserId,travelUserId,fromDate, toDate};
            }
        }
        //log4jLog.info(" selectUsersTravelLogs " + query);
        
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<HashMap>() {

                @Override
                public HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    if (rs.getString("userName")==null){
                        return null;
                    }
                    HashMap map= new HashMap();
                    map.put("userId",rs.getInt("uid"));
                    map.put("fullName",rs.getString("userName"));
                    double ddist= rs.getDouble("totalDistance")/1000;// from meter convert to KM
                    DecimalFormat df = new DecimalFormat("#.0000");
                    ddist =  Double.valueOf(df.format(ddist));
                    map.put("distance",ddist); 
                    map.put("visits",rs.getInt("totalVisits"));
                   
                    return map;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectUsersTravelLogs " + e);
//            e.printStackTrace();
            return new ArrayList<HashMap>();
        }
    }
    
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
    @Override
    public List<HashMap> selectMonthlyTravelReoprtForSubordinate(ArrayList<Integer>subUserList,int travelUserId,Map<String,String> allRequestParams,String fromDate, String toDate, int accountId) {
        //String query = "SELECT id,user_id_fk,latitude,longitude,location,created_on,source_value FROM user_travel_logs WHERE (created_on BETWEEN ? AND ?) AND user_id_fk=? ORDER BY created_on"; //order by id replaced with created_on
        if(subUserList.size()==0){
            return new ArrayList<HashMap>();
        }
        
        StringBuilder query = new StringBuilder();
        Object param[]=new Object[10];
        final int start=Integer.parseInt(allRequestParams.get("start"));
        final int length=Integer.parseInt(allRequestParams.get("length"));
      
        if(travelUserId==0){
            query.append("select uTable.id as uid,uTable.full_name as userName,IFNULL(tTable.distance,0) as totalDistance,(");
            query.append("select count(id) from appointments where check_in_time between ? and ? and assigned_id_fk=uTable.id) as totalVisits from (");
            query.append("select u.id,u.full_name from fieldsense.users u where u.account_id_fk=? and u.id IN(");
            for(int k=0;k<subUserList.size();k++){
                    query.append(subUserList.get(k));
                    if(k != (subUserList.size()-1)){
                        query.append(",");
                    }
            }
            query.append(")and role !=0");
            if(length!=-1){
              query.append(" limit ? offset ?");  
            }
            query.append(") uTable left join (");
            query.append("select user_id_fk, IFNULL(sum(distance_travel),0) as distance from user_travel_logs where created_on between ? and ? group by user_id_fk) tTable on uTable.id = tTable.user_id_fk order by userName");
            
            if(length!=-1){
                //query.append(" limit ? offset ?");
                param = new Object[]{fromDate, toDate,accountId,length,start,fromDate, toDate};
            }else{
                param = new Object[]{fromDate, toDate,accountId,fromDate, toDate};
            }
        }else{
            // it's not taking too much time , later on we can make change and compare join+subquery vs union in this case
            query.append("select u.id as uid ,u.full_name as userName ,IFNULL(sum(t.distance_travel),0) as totalDistance ,(select count(id) from appointments where ");
            query.append("check_in_time between ? and ? and assigned_id_fk=?) as totalVisits from fieldsense.users u inner join user_travel_logs t ");
            query.append("on u.id=t.user_id_fk where t.user_id_fk=? and u.role !=0 and t.created_on between ? and ? order by u.full_name");

            if(length!=-1){
                query.append(" limit ? offset ?");
                param = new Object[]{fromDate, toDate,travelUserId,travelUserId,fromDate, toDate,length,start};
            }else{
                param = new Object[]{fromDate, toDate,travelUserId,travelUserId,fromDate, toDate};
            }
        }
        //log4jLog.info(" selectUsersTravelLogs " + query);
        
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<HashMap>() {

                @Override
                public HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    if (rs.getString("userName")==null){
                        return null;
                    }
                    HashMap map= new HashMap();
                    map.put("userId",rs.getInt("uid"));
                    map.put("fullName",rs.getString("userName"));
                    double ddist= rs.getDouble("totalDistance")/1000;// from meter convert to KM
                    DecimalFormat df = new DecimalFormat("#.0000");
                    ddist =  Double.valueOf(df.format(ddist));
                    map.put("distance",ddist); 
                    map.put("visits",rs.getInt("totalVisits"));
                   
                    return map;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectUsersTravelLogs " + e);
//            e.printStackTrace();
            return new ArrayList<HashMap>();
        }
    }
    
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
    @Override
    public List<HashMap> dayWiseMonthlyTravelReoprt(int travelUserId,String timeZoneOffset,Map<String,String> allRequestParams,String fromDate, String toDate, int accountId){
        StringBuilder query = new StringBuilder();
        Object param[]=new Object[10];
        
        query.append("select u.id as uid,u.full_name as fullName,IFNULL(sum(t.distance_travel),0) as distance,date(CONVERT_TZ(t.created_on,?,@@global.time_zone)) as ndate,");
        query.append("(select count(id) from appointments where check_in_time between ? and ? and assigned_id_fk=? and date(CONVERT_TZ(check_in_time,?,@@global.time_zone))=ndate) as visits ");
        query.append("from fieldsense.users u inner join user_travel_logs t on u.id=t.user_id_fk where t.created_on between ? and ? and t.user_id_fk=? ");
        query.append("group by date(CONVERT_TZ(t.created_on,?,@@global.time_zone)) order by t.created_on");    
        param = new Object[]{timeZoneOffset,fromDate, toDate,travelUserId,timeZoneOffset,fromDate, toDate,travelUserId,timeZoneOffset};
        //log4jLog.info(" selectUsersTravelLogs " + query);
        
        final SimpleDateFormat format1 = new SimpleDateFormat("yyyy-mm-dd");
        final SimpleDateFormat format2 = new SimpleDateFormat("dd MMM yyyy");
        try {
             return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<HashMap>() {

                @Override
                public HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    HashMap map= new HashMap();
                    map.put("userId",rs.getInt("uid"));
                    map.put("fullName",rs.getString("fullName"));
                    
                    double ddist= rs.getDouble("distance")/1000;
                    DecimalFormat df = new DecimalFormat("#.0000");
                    ddist =  Double.valueOf(df.format(ddist));
                    map.put("distance",ddist);
                    
                    map.put("visits",rs.getInt("visits"));
                    
                    Date date=rs.getDate("ndate");
                    String nDate=rs.getDate("ndate").toString();
                    /*if(nDate.contains("-")){
                        String[] dArray=nDate.split("-");
                        nDate=dArray[2]+"-"+dArray[1]+"-"+dArray[0];
                    }*/
                    try{
                        nDate=format2.format(date);
                    }catch(Exception e){
                        log4jLog.info(" dayWiseMonthlyTravelReoprt " + e);
//                        e.printStackTrace();
                    }    
                    map.put("date",nDate);
                   
                    return map;
                }
            });
             
             
             
        } catch (Exception e) {
            log4jLog.info(" selectUsersTravelLogs " + e);
//            e.printStackTrace();
            return new ArrayList<HashMap>();
        }
    }
    
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
    @Override
    public HashMap<Integer,List<HashMap>> dayWiseMonthlyTravelReoprtNew(int travelUserId,ArrayList<Integer> idList,String timeZoneOffset,Map<String,String> allRequestParams,String fromDate, String toDate, int accountId) {
        //String query = "SELECT id,user_id_fk,latitude,longitude,location,created_on,source_value FROM user_travel_logs WHERE (created_on BETWEEN ? AND ?) AND user_id_fk=? ORDER BY created_on"; //order by id replaced with created_on
        StringBuilder query = new StringBuilder();
        Object param[]=new Object[10];
        final int start=Integer.parseInt(allRequestParams.get("start"));
        final int length=Integer.parseInt(allRequestParams.get("length"));
               
        //log4jLog.info(" selectUsersTravelLogs " + query);
        
        if(travelUserId == 0){
            if(length!=-1){
                
                String idContainer="";
                for(int k=0;k<idList.size();k++){
                    idContainer += idList.get(k);
                    if(k != (idList.size()-1)){
                       idContainer +=",";                         
                    }
                }
                
                query.append("select uid,sum(distance) as totalDistance,ndate,sum(visit) as totalVisits  from ( "); 
                query.append("Select user_id_fk as uid ,IFNULL(sum(distance_travel),0) as distance,date(CONVERT_TZ(created_on,?,@@global.time_zone)) as ndate ,0 as visit from user_travel_logs ");
                query.append("where user_id_fk IN (");
                for(int k=0;k<idList.size();k++){
                    /*idContainer += idList.get(k);*/
                    query.append(idList.get(k));
                    if(k != (idList.size()-1)){
                       //idContainer +=","; 
                        query.append(",");
                    }
                }
                query.append(") and created_on between ? and ? group by user_id_fk,date(CONVERT_TZ(created_on,?,@@global.time_zone)) UNION  ");
                query.append("select assigned_id_fk as uid, 0 as distance,date(CONVERT_TZ(check_in_time,?,@@global.time_zone)) as ndate,count(id) as visit from appointments ");
                query.append("where assigned_id_fk IN (");
                for(int k=0;k<idList.size();k++){
                    /*idContainer += idList.get(k);*/
                    query.append(idList.get(k));
                    if(k != (idList.size()-1)){
                       //idContainer +=","; 
                        query.append(",");
                    }
                }
                query.append(") and check_in_time between ? and ? group by assigned_id_fk,date(CONVERT_TZ(check_in_time,?,@@global.time_zone)) ) derivedtable group by uid,ndate order by uid,ndate");
                
                param = new Object[]{timeZoneOffset,fromDate, toDate,timeZoneOffset,timeZoneOffset,fromDate, toDate,timeZoneOffset};

            }else{
                query.append("select uid,sum(distance) as totalDistance,ndate,sum(visit) as totalVisits  from ( "); 
                query.append("Select user_id_fk as uid ,IFNULL(sum(distance_travel),0) as distance,date(CONVERT_TZ(created_on,?,@@global.time_zone)) as ndate ,0 as visit from user_travel_logs ");
                query.append("where created_on between ? and ? group by user_id_fk,date(CONVERT_TZ(created_on,?,@@global.time_zone)) UNION  ");
                query.append("select assigned_id_fk as uid, 0 as distance,date(CONVERT_TZ(check_in_time,?,@@global.time_zone)) as ndate,count(id) as visit from appointments ");
                query.append("where check_in_time between ? and ? group by assigned_id_fk,date(CONVERT_TZ(check_in_time,?,@@global.time_zone)) ) derivedtable group by uid,ndate order by uid,ndate");
                
                param = new Object[]{timeZoneOffset,fromDate, toDate,timeZoneOffset,timeZoneOffset,fromDate, toDate,timeZoneOffset};
            }
        }else{
                query.append("select u.id as uid,IFNULL(sum(t.distance_travel),0) as totalDistance,date(CONVERT_TZ(t.created_on,?,@@global.time_zone)) as ndate,");
                query.append("(select count(id) from appointments where check_in_time between ? and ? and assigned_id_fk=? and date(CONVERT_TZ(check_in_time,?,@@global.time_zone))=ndate) as totalVisits ");
                query.append("from fieldsense.users u inner join user_travel_logs t on u.id=t.user_id_fk where t.created_on between ? and ? and t.user_id_fk=? ");
                query.append("group by date(CONVERT_TZ(t.created_on,?,@@global.time_zone)) order by t.created_on");    
                param = new Object[]{timeZoneOffset,fromDate, toDate,travelUserId,timeZoneOffset,fromDate, toDate,travelUserId,timeZoneOffset};

        }    
        final SimpleDateFormat format1 = new SimpleDateFormat("yyyy-mm-dd");
        final SimpleDateFormat format2 = new SimpleDateFormat("dd MMM yyyy");
        try {
            
            ///test
             
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new ResultSetExtractor<HashMap<Integer,List<HashMap>>>() {
                
             @Override
                public HashMap<Integer,List<HashMap>> extractData(ResultSet rs) throws SQLException,DataAccessException {
                    //HashMap<String,String> mapRet= new HashMap<String,String>();
                    HashMap<Integer,List<HashMap>> finalResult = new HashMap<Integer,List<HashMap>>();

                    while(rs.next()){
                        int uid=rs.getInt("uid");
                        HashMap map= new HashMap();
                        map.put("userId",rs.getInt("uid"));
                        //map.put("fullName",rs.getString("fullName"));

                        double ddist= rs.getDouble("totalDistance")/1000;
                        DecimalFormat df = new DecimalFormat("#.0000");
                        ddist =  Double.valueOf(df.format(ddist));
                        map.put("distance",ddist);

                        map.put("visits",rs.getInt("totalVisits"));

                        Date date=rs.getDate("ndate");
                        String nDate=rs.getDate("ndate").toString();
                        /*if(nDate.contains("-")){
                            String[] dArray=nDate.split("-");
                            nDate=dArray[2]+"-"+dArray[1]+"-"+dArray[0];
                        }*/
                        try{
                            nDate=format2.format(date);
                        }catch(Exception e){
                            log4jLog.info(" extractData " + e);
//                            e.printStackTrace();
                        }    
                        map.put("date",nDate);

                        List<HashMap> value = finalResult.get(uid);
                        if (value != null) {
                            value.add(map);
                            finalResult.put(uid, value);                    
                        } else {
                            List<HashMap> newlist = new ArrayList<HashMap>();
                            newlist.add(map);
                            finalResult.put(uid, newlist);
                        }

                    }
                        return finalResult;
                    }
            });    
             //test end
             //return (HashMap<Integer,List<HashMap>>)result[0];
        } catch (Exception e) {
            log4jLog.info(" selectUsersTravelLogs " + e);
//            e.printStackTrace();
            return new HashMap<Integer,List<HashMap>>();
        }
    }

    /*
    *This method is just to provide patch for mobile issue, added on sept14 2016 by Awaneesh after discussing with Bappa Sir and Madhuri
    *This should be removed in next release 
    */

    /**
     *
     * @param travelEntry
     * @param accountId
     * @return
     */
    
    @Override
    public boolean isTravelEntryPresentInAttendance(UsersTravelLogs travelEntry,int accountId){
        String query="";
        Object[] param=null;
        String[] sCreatedOn=travelEntry.getCreatedOn().toString().split(" ");
        String sDate=sCreatedOn[0];
        String sTime=sCreatedOn[1];//HH:mm:ss
        String nTime=sTime.split(":")[0]+":"+sTime.split(":")[1]; //HH:mm
        // There is difference in seconds between punchin and trave enrty so we will just check hh:mm and use like
        if(travelEntry.getSourceValue()==1){
            query="Select count(id) as punch_count from attendances where user_id_fk=? and punch_date=? and punch_in like ?";
        }else if(travelEntry.getSourceValue()==2){
            query="Select count(id) as punch_count from attendances where user_id_fk=? and punch_out_date=? and punch_out like ?";
        }else{
            return false;
        }
        
        param= new Object []{travelEntry.getUserId(),sDate,nTime+"%"};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) >0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
//            e.printStackTrace();
            log4jLog.info(" isTravelEntryPresentInAttendance " + e.getMessage());
            return false;
        }
        //return false;
    }
    
    
    /*Created By Awaneesh
      @purpose to convert seconds to hh:mm:ss format
      @param time in seconds
      @return String in HH:mm:ss format
    */
    private String fromSecToStringTime(long seconds){
        boolean isNegative=false;
        if(seconds <0){
            isNegative=true;
            seconds = (-1 * seconds);
        }
        long hh1=(seconds/3600);
        long mm1= (seconds % 3600)/60;
        long ss1= (seconds % 3600)% 60;
        String hh2="00";
        String mm2="00";
        String ss2="00";
        
        if(hh1<10){
            hh2="0"+hh1;
        }else{
            hh2=""+hh1; 
        }
        if(mm1<10){
            mm2="0"+mm1;
        }else{
            mm2=""+mm1;
        }
        if(ss1<10){
            ss2="0"+ss1;
        }else{
            ss2=""+ss1;
        }
        
        if(isNegative){
            return "-"+hh2+":"+mm2+":"+ss2;
        }else{
            return hh2+":"+mm2+":"+ss2;
        }
    }

    public List<Appointment> selectVisitReportForAdminCsv(int userId, List<User> userList, Map<String, String> allRequestParams, String fromDate, String toDate, int accountId) {
         StringBuilder query = new StringBuilder();
        /*query.append("SELECT a.appointment_title,a.appointment_time,a.status,a.outcome,ao.outcome,ap.purpose,c.customer_printas,");
        query.append("u.first_name,u.last_name FROM appointments a");
        query.append(" INNER JOIN activity_outcomes ao ON a.outcome=ao.id");
        query.append(" INNER JOIN activity_purpose ap ON a.purpose_id_fk=ap.id");
        query.append(" INNER JOIN customers c ON a.customer_id_fk=c.id");
        query.append(" INNER JOIN fieldsense.users u ON u.id=a.user_id_fk");
        query.append(" WHERE a.assigned_id_fk=? AND (DATE(a.appointment_time) BETWEEN DATE(?) AND DATE(?)) ORDER BY a.appointment_time ASC");*/
//        System.out.println("allRequestParams "+allRequestParams.toString());
        query.append("SELECT a.appointment_title,a.appointment_time,a.status,a.outcome,a.appointment_end_time,a.outcome_description,a.check_in_time,a.check_out_time,ao.outcome,ap.purpose,c.customer_printas,c.customer_name,c.customer_location_identifier, c.address1, ");   // modified by manohar
        query.append("u.first_name,u.last_name,ua.first_name,ua.last_name FROM appointments a");
        query.append(" INNER JOIN activity_outcomes ao ON a.outcome=ao.id");
        query.append(" INNER JOIN activity_purpose ap ON a.purpose_id_fk=ap.id");
        query.append(" INNER JOIN customers c ON a.customer_id_fk=c.id");
        query.append(" INNER JOIN fieldsense.users u ON u.id=a.user_id_fk");
        query.append(" INNER JOIN fieldsense.users ua ON ua.id=a.assigned_id_fk");
        
        if(userId == 0){
            ArrayList<Integer> userIdList= new ArrayList<Integer>();
            for(User user: userList){
                userIdList.add(user.getId());
            }
             query.append(" WHERE a.record_state!=3 AND a.assigned_id_fk in(");
            for(int k=0;k<userIdList.size();k++){
                    query.append(userIdList.get(k));
                    if(k != (userIdList.size()-1)){
                        query.append(",");
                    }
            }
              query.append(")");
                    
              query.append("  AND ( a.appointment_time BETWEEN ? AND ? ) ORDER BY a.appointment_time DESC");
        }else{
         query.append(" WHERE a.record_state!=3 AND a.assigned_id_fk=? AND (a.appointment_time BETWEEN ? AND ?) ORDER BY a.appointment_time DESC");   
            
        }
        
        log4jLog.info(" selectVistReport in ADMIN USERId " + userId);
//        System.out.println("");
//        log4jLog.info(" selectVistReport in ADMIN USERId " + userId);
//        String sortcolindex=allRequestParams.get("order[0][column]");
//        if(sortcolindex==null){
//            query.append("ORDER BY a.appointment_time DESC");
//        }else if(Integer.parseInt(sortcolindex)==0){
//            if(allRequestParams.get("order[0][dir]").equals("asc")){
//                query.append("ORDER BY c.customer_name");
//            }else{
//                query.append("ORDER BY c.customer_name DESC");
//            }
//        }else if(Integer.parseInt(sortcolindex)==2){
//            if(allRequestParams.get("order[0][dir]").equals("asc")){
//                query.append("ORDER BY a.appointment_time");
//            }else{
//                query.append("ORDER BY a.appointment_time DESC");
//            }
//        }
//        log4jLog.info(" selectVistReport ADMIN " + query);
//        final int start=Integer.parseInt(allRequestParams.get("start"));
//        final int length=Integer.parseInt(allRequestParams.get("length"));
        Object param[] = null;
        if(userId == 0){
            param = new Object[]{fromDate, toDate};
            
        }else{
                param = new Object[]{userId, fromDate, toDate};
        }
                
            
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Appointment>() {

                @Override
                public Appointment mapRow(ResultSet rs, int i) throws SQLException {
                    Appointment appointment = new Appointment();
                    appointment.setTitle(rs.getString("appointment_title"));
                    appointment.setDateTime(rs.getTimestamp("appointment_time"));
                    appointment.setSdateTime(appointment.getDateTime().toString());
                    appointment.setStatus(rs.getInt("status"));
                    
                    appointment.setEndTime(rs.getTimestamp("appointment_end_time"));
                    appointment.setSendTime(appointment.getEndTime().toString());
                    appointment.setOutcomeDescription(rs.getString("outcome_description"));
                    appointment.setCheckInTime(rs.getTimestamp("check_in_time"));
                    appointment.setCheckOutTime(rs.getTimestamp("check_out_time"));
                    appointment.setScheckInTime(appointment.getCheckInTime().toString());
                    appointment.setScheckOutTime(appointment.getCheckOutTime().toString());


                    ActivityOutcome outcome = new ActivityOutcome();
                    outcome.setOutcome(rs.getString("ao.outcome"));
                    appointment.setOutcomes(outcome);

                    ActivityPurpose activityPurpose = new ActivityPurpose();
                    activityPurpose.setPurpose(rs.getString("purpose"));
                    appointment.setPurpose(activityPurpose);

                    Customer customer = new Customer();
                    customer.setCustomerPrintas(rs.getString("customer_printas"));
                    customer.setCustomerLocation(rs.getString("customer_location_identifier")); // modified by manohar                   
                    customer.setCustomerName(rs.getString("customer_name"));
                    customer.setCustomerAddress1(rs.getString("address1").trim()); // added by jyoti
                    appointment.setCustomer(customer);

                    User owner = new User();
                    owner.setFirstName(rs.getString("u.first_name"));
                    owner.setLastName(rs.getString("u.last_name"));
                    appointment.setOwner(owner);

                    User assigned = new User();
                    assigned.setFirstName(rs.getString("ua.first_name"));
                    assigned.setLastName(rs.getString("ua.last_name"));
                    appointment.setAssignedTo(assigned);
                    
//                    System.out.println("appointment #1"+appointment.toString());
                    return appointment;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectVistReport " + e);
            e.printStackTrace();
            return new ArrayList<Appointment>();
        }
    }
    
    /**
     * @Added by vaibhav, renamed to old by jyoti [NOT IN USE ANYMORE] for ticket #30782
     * @param month
     * @param year
     * @param accountId
     * @return 
     * @deprecated
     */
     public List<MonthlyAttendanceMuster> generateMonthlyAttendanceMuster_old(int month,int year,int accountId){
       String query="SELECT u.id, u.report_to, att.punch_in , att.punch_date ,CONCAT_WS(' ',u.first_name,u.last_name) AS userName,u.emp_code AS emp_code";
        query += " from attendances att INNER JOIN fieldsense.users u ON u.id=att.user_id_fk";
        query += " WHERE MONTH(att.punch_date) = ? and YEAR(att.punch_date) = ?";
        query += " group by u.id,att.punch_date order by userName, u.id"; // edited by jyoti, Bug #29632 - u.id with username combo in order by require to avoid duplicate repcord if same username exist multiple times 
       
            log4jLog.info(" selectMonthlyMusterReport " + query);
        Object param[] = new Object[]{month, year};
        MonthlyAttendanceMuster attendanceMuster = null;
        String monArray[] = {};
        boolean firstTime = true;
        Integer  oldUserId = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        int ctr = -1;
         List<Map<String,Object>> list = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForList(query, param);
         List<MonthlyAttendanceMuster> monthlyAttMusterList = new ArrayList<>();
         // Added by jyoti, purpose to display reporting head, New Feature #29345
         String query1 = "SELECT IFNULL(full_name, '') AS full_name FROM users WHERE id = ?";
         // ended by jyoti
        try{
            for (Map row : list) {
                ctr ++;
                if(Objects.equals(oldUserId, (Integer)row.get("id")) ){
                          Date date = (Date)row.get("punch_date");
                          cal.setTime(date);
                          int day = cal.get(Calendar.DAY_OF_MONTH);
                           monArray[day - 1] = "P"; // day starts from 1 and array from 0
                           if(ctr + 1 >= list.size()){ // for last group of userid
                                    attendanceMuster.setMonthlyMusterArray(monArray);
                                    monthlyAttMusterList.add(attendanceMuster);
                            }       
                         }else{
                               if(firstTime){
                                  firstTime = false;
                               }else{
                                     attendanceMuster.setMonthlyMusterArray(monArray);
//                                     System.out.println("olduserid" + oldUserId);
//                                     System.out.println("curr row id "+ (Integer)row.get("id"));
//                                     System.out.println((String)row.get("userName"));
                                     monthlyAttMusterList.add(attendanceMuster);
                               }
                               attendanceMuster = new MonthlyAttendanceMuster();
                               attendanceMuster.setUserId((Integer)row.get("id"));  
                               attendanceMuster.setEmp_code((String)row.get("emp_code"));
                               attendanceMuster.setPunchDate(sdf.format(row.get("punch_Date")));
                               attendanceMuster.setUserName((String)row.get("userName"));
                               
                               // Added by jyoti, New Feature #29345
                                int reportToId = (Integer) row.get("report_to");
                                if (reportToId != 0) {
                                    Object param1[] = new Object[]{reportToId};
                                    String reportingHead = jdbcTemplate.queryForObject(query1, param1, String.class);
                                    attendanceMuster.setReportsTo(reportingHead);
                                } else {
                                    attendanceMuster.setReportsTo("-");
                                }
                               // ended by jyoti
                               
                               Date date = (Date)row.get("punch_date");
                               cal.setTime(date);
                               int day = cal.get(Calendar.DAY_OF_MONTH);
                               // Get the number of days in that month
                               int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                               //System.out.println("daysInMonth " + daysInMonth);
                               monArray  = new String[daysInMonth + 1];
                               monArray[day - 1 ] = "P";
                        }    
                           oldUserId = (Integer)row.get("id");
                  } 
            }catch (Exception e) {
                log4jLog.info(" selectMonthlyMusterReport " + e);
                e.printStackTrace();

            }
         return monthlyAttMusterList;    
     }

    /**
     * @added by jyoti, for ticket #30782, In Monthly Attendance report all the active users should display.
     * @param month
     * @param year
     * @param accountId
     * @return 
     */
    @Override
    public List<MonthlyAttendanceMuster> generateMonthlyAttendanceMuster(int month, int year, int accountId) {

        String query = "SELECT u.id, u.report_to, att.punch_in , att.punch_date ,CONCAT_WS(' ',u.first_name,u.last_name) AS userName,u.emp_code AS emp_code";
        query += " from fieldsense.users u LEFT JOIN attendances att ON u.id=att.user_id_fk";
        query += " AND MONTH(att.punch_date) = ? and YEAR(att.punch_date) = ? WHERE u.account_id_fk = ? AND u.active = 1 AND u.role != 0 AND u.email_address NOT LIKE '%@fieldsense.in%' ";
        query += " group by u.id,att.punch_date order by userName, u.id"; // edited by jyoti, Bug #29632 - u.id with username combo in order by require to avoid duplicate repcord if same username exist multiple times
//        System.out.println(query);
        Object param[] = new Object[]{month, year, accountId};
        MonthlyAttendanceMuster attendanceMuster = null;
        String monArray[] = {};
        Integer oldUserId = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        List<Map<String, Object>> list = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForList(query, param);
        List<MonthlyAttendanceMuster> monthlyAttMusterList = new ArrayList<>();
        // Added by jyoti, purpose to display reporting head, New Feature #29345
        String query1 = "SELECT IFNULL(full_name, '') AS full_name FROM users WHERE id = ?";
        // ended by jyoti
        try {
            for (Map row : list) {
                if (Objects.equals(oldUserId, (Integer) row.get("id"))) {
                    if (row.get("punch_Date") == null) {
//                        System.out.println("old aur new id same hai, pd null hai");
                    } else {
                        Date date = (Date) row.get("punch_date");
                        cal.setTime(date);
                        int day = cal.get(Calendar.DAY_OF_MONTH);
//                        System.out.println("day > " + day);
                        monArray[day - 1] = "P"; // day starts from 1 and array from 0
                    }
                } else {

                    attendanceMuster = new MonthlyAttendanceMuster();
                    attendanceMuster.setUserId((Integer) row.get("id"));
                    attendanceMuster.setEmp_code((String) row.get("emp_code"));
                    if (row.get("punch_Date") == null) {
                        attendanceMuster.setPunchDate("00-00-0000");
                    } else {
                        attendanceMuster.setPunchDate(sdf.format(row.get("punch_Date")));
                    }
                    attendanceMuster.setUserName((String) row.get("userName"));

                    // Added by jyoti, New Feature #29345
                    int reportToId = (Integer) row.get("report_to");
                    if (reportToId != 0) {
                        Object param1[] = new Object[]{reportToId};
                        String reportingHead = jdbcTemplate.queryForObject(query1, param1, String.class);
                        attendanceMuster.setReportsTo(reportingHead);
                    } else {
                        attendanceMuster.setReportsTo("-");
                    }
                    // ended by jyoti
                    if (row.get("punch_Date") == null) {
                        // Get the number of days in that month
                        YearMonth yearMonthObject = YearMonth.of(year, month);
                        int daysInMonth = yearMonthObject.lengthOfMonth(); //28 
                        // Get the number of days in that month
                        monArray = new String[daysInMonth + 1];
                    } else {
                        Date date = (Date) row.get("punch_date");
                        cal.setTime(date);
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        // Get the number of days in that month
                        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                        monArray = new String[daysInMonth + 1];
                        monArray[day - 1] = "P";
                    }

                    attendanceMuster.setMonthlyMusterArray(monArray);
                    monthlyAttMusterList.add(attendanceMuster);

                }
                oldUserId = (Integer) row.get("id");
            }
        } catch (Exception e) {
            log4jLog.info(" selectMonthlyMusterReport " + e);
            e.printStackTrace();

        }
        return monthlyAttMusterList;
    }


//    @Override
//    public HashMap<Integer, List<HashMap>> dayWiseMonthlyTravelReportGroupBy(int travelUserId, ArrayList<Integer> idList, String timeZoneOffset, Map<String, String> allRequestParams, String fromDate, String toDate, int accountId) {
//       String query="";
//        if(travelUserId==0){
//       
//       }else{
//        query = "select created_on,sum(distance_travel) total_distance,user_id_fk from user_travel_logs where user_id_fk=? and created_on between ? and ? group by created_on order by created_on";
//        Object param[] = new Object[]{travelUserId, fromDate,toDate};
//        try{
//           FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new ResultSetExtractor<HashMap<Integer,List<HashMap>>>() {
//                
//             @Override
//                public HashMap<Integer,List<HashMap>> extractData(ResultSet rs) throws SQLException,DataAccessException {
//                    //HashMap<String,String> mapRet= new HashMap<String,String>();
//                    HashMap<Integer,List<HashMap>> finalResult = new HashMap<Integer,List<HashMap>>();
//
//                    while(rs.next()){
//                        int uid=rs.getInt("user_id_fk");
//                        HashMap map= new HashMap();
//                        map.put("userId",rs.getInt("uid"));
//                        //map.put("fullName",rs.getString("fullName"));
//
//                        double ddist= rs.getDouble("total_distance")/1000;
//                        DecimalFormat df = new DecimalFormat("#.0000");
//                        ddist =  Double.valueOf(df.format(ddist));
//                        
//                        String date=rs.getString("created_on");
//
//                        
//                        String nDate=rs.getDate("ndate").toString();
//                        /*if(nDate.contains("-")){
//                            String[] dArray=nDate.split("-");
//                            nDate=dArray[2]+"-"+dArray[1]+"-"+dArray[0];
//                        }*/
////                        try{
////                            nDate=format2.format(date);
////                        }catch(Exception e){
////                            log4jLog.info(" extractData " + e);
////                            e.printStackTrace();
////                        }    
//                        map.put("date",nDate);
//
//                        List<HashMap> value = finalResult.get(uid);
//                        if (value != null) {
//                            value.add(map);
//                            finalResult.put(uid, value);                    
//                        } else {
//                            List<HashMap> newlist = new ArrayList<HashMap>();
//                            newlist.add(map);
//                            finalResult.put(uid, newlist);
//                        }
//
//                    }
//                        return finalResult;
//                    }
//            });    
//        }catch(Exception e){
//        
//        }
//        
//        
//     
//       }
//        
//        
//        
//   return null;
//    }

    @Override
    public LinkedHashMap<Object, Object> getDayWiseMonthlyReport(int travelUserId, String fromDate, String toDate, int accountId,String timeZoneOffset) {
//         System.out.println("I am here " + fromDate + "to date "+toDate);
      String query = "select date(CONVERT_TZ(created_on,?,@@global.time_zone)) as created_on,distance_travel total_distance from user_travel_logs where user_id_fk=? and created_on between ? and ? group by created_on order by created_on";
      Object param[] = new Object[]{travelUserId, fromDate,toDate};
      Connection connection = null;
        PreparedStatement stmt = null;
        LinkedHashMap<Object,Object> mapOfUserTraveldata = new LinkedHashMap<Object,Object>();
      try{
      connection = FieldSenseUtils.getDataSourceForAccount(accountId).getConnection();
       stmt = connection.prepareStatement(query);
       stmt.setString(1, timeZoneOffset);
       stmt.setInt(2, travelUserId);
       stmt.setString(3, fromDate);
       stmt.setString(4, toDate);
       ResultSet rs = stmt.executeQuery();
       while (rs.next()){
            double ddist= rs.getDouble("total_distance")/1000;
            DecimalFormat df = new DecimalFormat("#.0000");
            ddist =  Double.valueOf(df.format(ddist));
            String createdOn = rs.getString("created_on");
            //createdOn = createdOn.split(" ")[0].trim();
            Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(createdOn);  
             SimpleDateFormat format2 = new SimpleDateFormat("dd MMM yyyy");
               createdOn = format2.format(date1);
           // System.out.println("Hello "+mapOfIterate);
               //System.out.println("Hello ");
           Object data = mapOfUserTraveldata.get(createdOn);
            if(data == null) {
        mapOfUserTraveldata.put(createdOn, ddist);
            }
            else{
                double sum = (double) data;
                sum=sum + ddist;
                sum =  Double.valueOf(df.format(sum));
          mapOfUserTraveldata.put(createdOn, sum);
    }  
       }
       connection.close();
       stmt.close();
       return mapOfUserTraveldata;
      }catch(Exception e){
          e.printStackTrace();
          try {
              connection.close();
               stmt.close();
          } catch (SQLException ex) {
              java.util.logging.Logger.getLogger(ReportsDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
          }
      return mapOfUserTraveldata;
      }
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

     
     
     
     
}
