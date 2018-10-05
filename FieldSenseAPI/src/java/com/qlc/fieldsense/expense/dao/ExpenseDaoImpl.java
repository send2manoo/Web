/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.expense.dao;

import com.qlc.fieldsense.activityPurpose.model.ActivityPurpose;
import com.qlc.fieldsense.appointments.model.Appointment;
import com.qlc.fieldsense.customer.model.Customer;
import com.qlc.fieldsense.expense.model.Expense;
import com.qlc.fieldsense.expense.model.ExpenseFilter;
import com.qlc.fieldsense.expense.model.ExpenseImage;
import com.qlc.fieldsense.expenseCategory.model.ExpenseCategory;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.utils.APNPushNotifications;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GcmPushNotifications;
import com.qlc.fieldsense.utils.GetApplicationContext;
import com.qlc.fieldsense.utils.PushNotificationManager;
import com.qlc.fieldsense.utils.PushNotifications;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.RequestParam;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author Ramesh
 */
public class ExpenseDaoImpl implements ExpenseDao {
    
    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("ExpenseDaoImpl");

    /**
     *
     * @param expense
     * @param accountId
     * @return
     */
    @Override
    public int insertExpense(Expense expense, int accountId) {
        StringBuilder query = new StringBuilder();
        /*query.append("INSERT INTO expenses(appointment_id_fk,customer_id_fk,user_id_fk,expense_name,desription,amount_spent,status,");
        query.append("expense_time,submitted_on,created_on) VALUES (?,?,?,?,?,?,0,?,now(),now())");*/
        StringBuffer expenseImageIdList=new StringBuffer();
        try{
        if(!expense.getExpenseImageArray().isEmpty()){
            
             expenseImageIdList.append(expense.getExpenseImageArray().get(0).getId());
                for(int i=1;i<expense.getExpenseImageArray().size();i++){
                    expenseImageIdList.append(",");
                     expenseImageIdList.append(expense.getExpenseImageArray().get(i).getId());
            }
        }
//            System.out.println("Data "+expenseImageIdList.toString());
        }catch(Exception e){
        e.printStackTrace();
        }
        String defdatequery="SELECT distinct(default_date) FROM expenses where (status=1 || status=0 || status=2) and default_date!='1111:11:11 11:11:11'";
        Timestamp time=null;
        try{
            time= FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(defdatequery,Timestamp.class);   
        }catch (Exception e) {
            log4jLog.info(" insert Expense :" + e);
            e.printStackTrace();
            time=null;
        }
        Object[] param=new Object[40];
        query.append("INSERT INTO expenses(appointment_id_fk,customer_id_fk,user_id_fk,expense_name,desription,amount_spent,status,expense_image_id_csv,");
         if(time==null){
            query.append("expense_time,submitted_on,created_on,category_id_fk,exp_category_name) VALUES (?,?,?,?,?,?,0,?,?,now(),now(),?,?)");
            param = new Object[]{expense.getAppointmentId(), expense.getCustomerId(), expense.getUser().getId(), expense.getExpenseName(), expense.getDescription(),expense.getAmountSpent(),expenseImageIdList,expense.getExpenseTime() ,expense.geteCategoryName().getId(),expense.getExp_category_name()};
        }else{
            query.append("expense_time,submitted_on,created_on,category_id_fk,default_date,exp_category_name) VALUES (?,?,?,?,?,?,0,?,?,now(),now(),?,?,?)");
            param = new Object[]{expense.getAppointmentId(), expense.getCustomerId(), expense.getUser().getId(), expense.getExpenseName(), expense.getDescription(),expense.getAmountSpent(),expenseImageIdList,expense.getExpenseTime() ,expense.geteCategoryName().getId(),time,expense.getExp_category_name()};
         }
        log4jLog.info(" insert Expense :" + query);
        int expense_id=0;
        try {
            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query.toString(), param) > 0) {
                    String query1 = "SELECT MAX(id) FROM expenses";
                    try {
                        expense_id= FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query1, Integer.class);
                        String query2 ="INSERT INTO expenses_audit(expense_id_fk,appointment_id_fk,customer_id_fk,user_id_fk,expense_name,desription,amount_spent,status,expense_time,submitted_on,created_on,category_id_fk,exp_category_name) ";
                        query2 +=" VALUES (?,?,?,?,?,?,?,0,?,now(),now(),?,?)";
                        Object[] param2 = new Object[]{expense_id,expense.getAppointmentId(), expense.getCustomerId(), expense.getUser().getId(), expense.getExpenseName(), expense.getDescription(), expense.getAmountSpent(),expense.getExpenseTime() ,expense.geteCategoryName().getId(),expense.getExp_category_name()};
                        try{
                            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query2, param2) > 0) {
                                return expense_id;
                            }else{
                                  return 0;
                            }
                        }catch (Exception e) {
                        log4jLog.info(" insert Expense :" + e);
                      e.printStackTrace();
                        return 0;
                    }
                    } catch (Exception e) {
                        log4jLog.info(" insert Expense :" + e);
                        e.printStackTrace();
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            log4jLog.info(" insert Expense" + e);
            e.printStackTrace();
            return 0;
        }
    }

    /**
     *
     * @param expenseId
     * @param accountId
     * @return
     */
    @Override
    public Expense selectExpense(final int expenseId, final int accountId) {
        StringBuilder query = new StringBuilder();
        /*query.append("SELECT id,appointment_id_fk,customer_id_fk,user_id_fk,expense_name,Expense_type_id_fk,desription,");
        query.append("amount_spent,status,expense_time,submitted_on,approved_rejected_on,");
        query.append("approved_rejected_by_id_fk,rejected_reason,created_on FROM expenses WHERE id=?");*/
        query.append("SELECT e.id,e.expense_image_id_csv,e.appointment_id_fk,e.customer_id_fk,e.user_id_fk,e.expense_name,e.Expense_type_id_fk,e.desription,");
        query.append("e.amount_spent,e.status,e.expense_time,e.submitted_on,e.approved_rejected_on,");
        query.append("e.approved_rejected_by_id_fk,e.rejected_reason,e.created_on,e.category_id_fk,IFNULL (e.disburse_amount,0) disburse_amount,IFNULL (e.payment_mode,'') payment_mode,IFNULL (e.default_date,'1111-11-11 11:11:11') default_date,IFNULL (e.exp_category_name,'') exp_category_name,a.appointment_title,c.customer_name,u.id,IFNULL (u.first_name,'') first_name,IFNULL (u.last_name,'') last_name,IFNULL (u.full_name,'') full_name,ue.id,ue.report_to,ue.first_name,ue.last_name,ue.full_name,IFNULL(ue1.full_name,'') reporting_head,ec.id,ec.category_name FROM expenses e");
        query.append(" LEFT JOIN appointments a ON e.appointment_id_fk=a.id");
        query.append(" LEFT JOIN customers c ON e.customer_id_fk=c.id");
	query.append(" LEFT OUTER JOIN fieldsense.users u ON u.id=e.approved_rejected_by_id_fk");
        query.append(" INNER JOIN fieldsense.users ue ON ue.id=e.user_id_fk");
        query.append(" INNER JOIN fieldsense.users ue1 ON ue1.id=ue.report_to");
        query.append(" LEFT OUTER JOIN expense_categories ec ON e.category_id_fk=ec.id");
        query.append(" WHERE e.id=?");
        log4jLog.info(" select Expense " + query);
        Object param[] = new Object[]{expenseId};
         final int account_id = accountId;
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query.toString(), param, new RowMapper<Expense>() {

                @Override
                public Expense mapRow(ResultSet rs, int i) throws SQLException {
                    Expense expense = new Expense();

                    User user = new User();
	            int user_id=rs.getInt("ue.id");
                    user.setId(rs.getInt("user_id_fk"));
                    user.setAccountId(0);
                    user.setFirstName(rs.getString("ue.first_name"));
                    user.setLastName(rs.getString("ue.last_name"));
                    user.setEmailAddress("");
                    user.setPassword("");
                    user.setMobileNo("");
                    user.setGender(0);
                    user.setRole(0);
                    user.setActive(false);
                    user.setLastLoggedOn(new Timestamp(0));
                    user.setLastKnownLocationTime(new Timestamp(0));
                    user.setLastKnownLocation("");
                    user.setCreatedOn(new Timestamp(0));
                    user.setCreatedBy(0);
		    expense.setUser(user);

                    User approvedOrRejectedBy = new User();
                    approvedOrRejectedBy.setId(rs.getInt("approved_rejected_by_id_fk"));
                    approvedOrRejectedBy.setAccountId(0);
                    approvedOrRejectedBy.setFirstName(rs.getString("first_name"));
                    approvedOrRejectedBy.setLastName(rs.getString("last_name"));
                    approvedOrRejectedBy.setEmailAddress("");
                    approvedOrRejectedBy.setPassword("");
                    approvedOrRejectedBy.setMobileNo("");
                    approvedOrRejectedBy.setGender(0);
                    approvedOrRejectedBy.setRole(0);
                    approvedOrRejectedBy.setActive(false);
                    approvedOrRejectedBy.setLastLoggedOn(new Timestamp(0));
                    approvedOrRejectedBy.setLastKnownLocationTime(new Timestamp(0));
                    approvedOrRejectedBy.setLastKnownLocation("");
                    approvedOrRejectedBy.setCreatedOn(new Timestamp(0));
                    approvedOrRejectedBy.setCreatedBy(0);
		    expense.setApprovedOrRjectedBy(approvedOrRejectedBy);

                    ExpenseImage expenseImage = new ExpenseImage();
                    expenseImage.setId(0);
                    expenseImage.setExpenseId(0);
                    expenseImage.setImageURL("");
                    expenseImage.setUser(user);
                    expenseImage.setCreatedOn(new Timestamp(0));
                    String userExpenseCsv=(rs.getString("expense_image_id_csv"));
                    expense.setId(rs.getInt("id"));
                    expense.setAppointmentId(rs.getInt("appointment_id_fk"));
                    expense.setCustomerId(rs.getInt("customer_id_fk"));
                    expense.setExpenseName(rs.getString("expense_name"));
                    expense.setExpenseType(rs.getInt("Expense_type_id_fk"));
                    expense.setDescription(rs.getString("desription"));
                    expense.setAmountSpent(rs.getDouble("amount_spent"));
                    expense.setStatus(rs.getInt("status"));
                    expense.setSubmittedOn(rs.getTimestamp("submitted_on"));
                    expense.setApprovedOrRejectedOn(rs.getTimestamp("approved_rejected_on"));

                    expense.setRejectedReson(rs.getString("rejected_reason"));
                    expense.setCreatedOn(rs.getTimestamp("created_on"));
                    expense.setExpenseImage(expenseImage);

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
                    ArrayList<ExpenseImage> expenseImagesArray = new ArrayList<ExpenseImage>();
                    if(userExpenseCsv==null || userExpenseCsv.equals("") || userExpenseCsv.equals(" ")){
                        }else{
                            String [] imageArray=userExpenseCsv.split(",");
                            for(int j = 0 ; j <imageArray.length ; j++){
                                if(imageArray[j].equals("") || imageArray[j].equals(" ")){
                                }else{
                                    ExpenseImage expenseImageForMultiple = new ExpenseImage();
                                    expenseImageForMultiple.setId(Long.parseLong(imageArray[j]));
                                    expenseImageForMultiple.setImageURL(Constant.IMAGE_GET_PATH+"expense"+"_"+account_id+"_"+user_id+"_"+expenseId+"_"+imageArray[j]+".png");
                                    expenseImagesArray.add(expenseImageForMultiple);
                                }  
                            }
                        }
                    return expense;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" select Expense " + e);
//            e.printStackTrace();
            return new Expense();
        }
    }

    /**
     *
     * @param expenseId
     * @param accountId
     * @return
     */
    @Override
    public List<Expense> selectExpenseAudit(int expenseId, final int accountId) {
        StringBuilder query = new StringBuilder();

        query.append("SELECT e.expense_id_fk,e.expense_name,e.amount_spent,e.expense_time,e.status,e.approved_rejected_on,e.rejected_reason,IFNULL (e.disburse_amount,0) disburse_amount,e.exp_category_name,");
        query.append("c.customer_printas,c.customer_name,a.appointment_title,IFNULL (u.first_name,'') first_name,IFNULL (u.last_name,'') last_name,IFNULL (u.full_name,'') full_name,ue.id,ue.report_to,ue.first_name,ue.last_name,ue.full_name,IFNULL(ue1.full_name,'') reporting_head,ec.category_name FROM expenses_audit e");
        query.append(" LEFT JOIN customers c ON e.customer_id_fk = c.id");
        query.append(" LEFT JOIN appointments a ON a.id=e.appointment_id_fk");
        query.append(" LEFT OUTER JOIN fieldsense.users u ON u.id=e.approved_rejected_by_id_fk");
        query.append(" INNER JOIN fieldsense.users ue ON ue.id=e.user_id_fk and ue.active=1");
        query.append(" INNER JOIN fieldsense.users ue1 ON ue1.id=ue.report_to");
        query.append(" LEFT OUTER JOIN expense_categories ec ON e.category_id_fk=ec.id");
      query.append(" WHERE e.expense_id_fk=? order by e.approved_rejected_on desc");
        log4jLog.info(" selectExpenseReport " + query);
        Object param[] = new Object[]{expenseId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Expense>() {

                @Override
                public Expense mapRow(ResultSet rs, int i) throws SQLException {
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
     * @param userId
     * @param startDate
     * @param endDate
     * @param accountId
     * @return
     */
    @Override
    public List<Expense> selectUserExpense(int userId, String startDate, String endDate, final int accountId) {
        StringBuilder query = new StringBuilder();
        /*query.append("SELECT e.id,appointment_id_fk,e.customer_id_fk,e.user_id_fk,e.expense_name,e.Expense_type_id_fk,e.desription,");
        query.append("e.amount_spent,e.status,e.expense_time,e.submitted_on,e.approved_rejected_on,");
        query.append("e.approved_rejected_by_id_fk,e.rejected_reason,e.created_on,a.appointment_title,c.customer_name FROM expenses e");
        query.append(" INNER JOIN appointments a ON e.appointment_id_fk=a.id");
        query.append(" INNER JOIN customers c ON e.customer_id_fk=c.id");
        query.append(" WHERE e.user_id_fk=? AND DATE(e.submitted_on) BETWEEN ? AND ? ORDER BY e.expense_time DESC");*/
	
        query.append("SELECT e.id,e.appointment_id_fk,e.customer_id_fk,e.expense_image_id_csv,e.user_id_fk,e.expense_name,e.Expense_type_id_fk,e.desription,");
        query.append("e.amount_spent,e.status,e.expense_time,e.submitted_on,e.approved_rejected_on,");
        query.append("e.approved_rejected_by_id_fk,e.rejected_reason,e.created_on,e.category_id_fk,IFNULL (e.disburse_amount,0) disburse_amount,IFNULL (e.payment_mode,'') payment_mode,IFNULL (e.default_date,'1111-11-11 11:11:11') default_date,IFNULL (e.exp_category_name,'') exp_category_name,a.appointment_title,c.customer_name,u.id,IFNULL (u.first_name,'') first_name,IFNULL (u.last_name,'') last_name,IFNULL (u.full_name,'') full_name,ue.id,ue.report_to,ue.first_name,ue.last_name,ue.full_name,IFNULL(ue1.full_name,'') reporting_head,ec.id,ec.category_name FROM expenses e");
        query.append(" LEFT JOIN appointments a ON e.appointment_id_fk=a.id");
        query.append(" LEFT JOIN customers c ON e.customer_id_fk=c.id");
	query.append(" LEFT OUTER JOIN fieldsense.users u ON u.id=e.approved_rejected_by_id_fk");
        query.append(" INNER JOIN fieldsense.users ue ON ue.id=e.user_id_fk");
        query.append(" INNER JOIN fieldsense.users ue1 ON ue1.id=ue.report_to");
        query.append(" LEFT OUTER JOIN expense_categories ec ON e.category_id_fk=ec.id");
        query.append(" WHERE e.user_id_fk=? AND (e.expense_time BETWEEN ? AND ?) ORDER BY e.expense_time DESC");
        log4jLog.info(" selectUserExpense  " + query);
        final int account_id = accountId;
        Object param[] = new Object[]{userId, startDate, endDate};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Expense>() {

                @Override
                public Expense mapRow(ResultSet rs, int i) throws SQLException {
                    Expense expense = new Expense();
                    int expenseId=rs.getInt("id");
                    expense.setId(expenseId);
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
                    String userExpenseCsv=(rs.getString("expense_image_id_csv"));
                    expense.setExpenseImageCsv(userExpenseCsv);
                    expense.setExpenseType(rs.getInt("Expense_type_id_fk"));
                    expense.setDescription(rs.getString("desription"));
                    expense.setAmountSpent(rs.getDouble("amount_spent"));
                    expense.setStatus(rs.getInt("status"));
                    expense.setExpenseTime(rs.getTimestamp("expense_time"));
                    expense.setSubmittedOn(rs.getTimestamp("submitted_on"));
                    expense.setApprovedOrRejectedOn(rs.getTimestamp("approved_rejected_on"));
                    User user = new User();
                    int userId=(rs.getInt("u.id"));
                    user.setId(userId);
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
                    ArrayList<ExpenseImage> expenseImagesArray = new ArrayList<ExpenseImage>();
                    try{
//                        String expensePath = FieldSenseUtils.getPropertyValue("IMAGE_UPLOAD_PATH");
//                        File file = new File(expensePath+"expense"+"_"+account_id+"_"+user_id+"_"+expense.getId()+".png");
//                        if(file.exists()){
//                        System.out.println("File name "+file.getName());
//                        BufferedImage img = ImageIO.read(file);
//                        String imageBase64 = encodeToString(img, "png");
//                        expense.setImageBase64(imageBase64);
//                        }
                    if(userExpenseCsv==null || userExpenseCsv.equals("") || userExpenseCsv.equals(" ")){
                    }else{
                        String [] imageArray=userExpenseCsv.split(",");
                        for(int j = 0 ; j <imageArray.length ; j++){
                            if(imageArray[j].equals("") || imageArray[j].equals(" ")){
                            }else{
                                ExpenseImage expenseImage = new ExpenseImage();
                                expenseImage.setId(Long.parseLong(imageArray[j]));
                                expenseImage.setImageURL(Constant.IMAGE_GET_PATH+"expense"+"_"+account_id+"_"+user_id+"_"+expenseId+"_"+imageArray[j]+".png");
                                expenseImagesArray.add(expenseImage);
                            }  
                        }
                    }
                    }catch(Exception e){
                    e.printStackTrace();
                    }
                    expense.setExpenseImageArray(expenseImagesArray);
                    return expense;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectUserExpense  " + e);
            e.printStackTrace();
            return new ArrayList<Expense>();
        }
    }

    /**
     *
     * @param userId
     * @param customerId
     * @param accountId
     * @return
     */
    @Override
    public List<Expense> selectUserCustomerExpense(int userId, int customerId, int accountId) {
        StringBuilder query = new StringBuilder();
        /*query.append("SELECT id,appointment_id_fk,customer_id_fk,user_id_fk,expense_name,Expense_type_id_fk,desription,");
        query.append("amount_spent,status,expense_time,submitted_on,approved_rejected_on,");
        query.append("approved_rejected_by_id_fk,rejected_reason,created_on FROM expenses WHERE user_id_fk=? AND customer_id_fk=?");*/

        query.append("SELECT e.id,appointment_id_fk,customer_id_fk,user_id_fk,expense_name,Expense_type_id_fk,desription,");
        query.append("amount_spent,status,expense_time,submitted_on,approved_rejected_on,");
        query.append("approved_rejected_by_id_fk,rejected_reason,e.created_on,ec.category_name FROM expenses e");
        query.append(" LEFT OUTER JOIN expense_categories ec ON e.category_id_fk=ec.id");
        query.append(" WHERE user_id_fk=? AND customer_id_fk=?");
        log4jLog.info(" selectUserCustomerExpense " + query);
        Object param[] = new Object[]{userId, customerId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Expense>() {

                @Override
                public Expense mapRow(ResultSet rs, int i) throws SQLException {
                    Expense expense = new Expense();
                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setAccountId(0);
                    user.setFirstName("");
                    user.setLastName("");
                    user.setEmailAddress("");
                    user.setPassword("");
                    user.setMobileNo("");
                    user.setGender(0);
                    user.setRole(0);
                    user.setActive(false);
                    user.setLastLoggedOn(new Timestamp(0));
                    user.setLastKnownLocationTime(new Timestamp(0));
                    user.setLastKnownLocation("");
                    user.setCreatedOn(new Timestamp(0));
                    user.setCreatedBy(0);

                    User approvedOrRejectedBy = new User();
                    approvedOrRejectedBy.setId(rs.getInt("approved_rejected_by_id_fk"));
                    approvedOrRejectedBy.setAccountId(0);
                    approvedOrRejectedBy.setFirstName("");
                    approvedOrRejectedBy.setLastName("");
                    approvedOrRejectedBy.setEmailAddress("");
                    approvedOrRejectedBy.setPassword("");
                    approvedOrRejectedBy.setMobileNo("");
                    approvedOrRejectedBy.setGender(0);
                    approvedOrRejectedBy.setRole(0);
                    approvedOrRejectedBy.setActive(false);
                    approvedOrRejectedBy.setLastLoggedOn(new Timestamp(0));
                    approvedOrRejectedBy.setLastKnownLocationTime(new Timestamp(0));
                    approvedOrRejectedBy.setLastKnownLocation("");
                    approvedOrRejectedBy.setCreatedOn(new Timestamp(0));
                    approvedOrRejectedBy.setCreatedBy(0);

                    ExpenseImage expenseImage = new ExpenseImage();
                    expenseImage.setId(0);
                    expenseImage.setExpenseId(0);
                    expenseImage.setImageURL("");
                    expenseImage.setUser(user);
                    expenseImage.setCreatedOn(new Timestamp(0));

                    expense.setId(rs.getInt("id"));
                    expense.setAppointmentId(rs.getInt("appointment_id_fk"));
                    expense.setCustomerId(rs.getInt("customer_id_fk"));
                    expense.setUser(user);
                    expense.setExpenseName(rs.getString("expense_name"));
                    expense.setExpenseType(rs.getInt("Expense_type_id_fk"));
                    expense.setDescription(rs.getString("desription"));
                    expense.setAmountSpent(rs.getDouble("amount_spent"));
                    expense.setStatus(rs.getInt("status"));
                    expense.setSubmittedOn(rs.getTimestamp("submitted_on"));
                    expense.setApprovedOrRejectedOn(rs.getTimestamp("approved_rejected_on"));
                    expense.setApprovedOrRjectedBy(approvedOrRejectedBy);
                    expense.setRejectedReson(rs.getString("rejected_reason"));
                    expense.setCreatedOn(rs.getTimestamp("created_on"));
                    expense.setExpenseImage(expenseImage);

                    ExpenseCategory eCategory = new ExpenseCategory();
                    eCategory.setCategoryName(rs.getString("ec.category_name"));
                    expense.seteCategoryName(eCategory);
                    return expense;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectUserCustomerExpense " + e);
//            e.printStackTrace();
            return new ArrayList<Expense>();
        }
    }

    /**
     *
     * @param customerId
     * @param accountId
     * @return
     */
    @Override
    public List<Expense> selectCustomerExpense(int customerId, int accountId) {
        StringBuilder query = new StringBuilder();
        /*query.append("SELECT id,appointment_id_fk,customer_id_fk,user_id_fk,expense_name,Expense_type_id_fk,desription,");
        query.append("amount_spent,status,expense_time,submitted_on,approved_rejected_on,");
        query.append("approved_rejected_by_id_fk,rejected_reason,created_on FROM expenses WHERE customer_id_fk=?");*/

        query.append("SELECT e.id,appointment_id_fk,customer_id_fk,user_id_fk,expense_name,Expense_type_id_fk,desription,");
        query.append("amount_spent,status,expense_time,submitted_on,approved_rejected_on,");
        query.append("approved_rejected_by_id_fk,rejected_reason,e.created_on,ec.category_name FROM expenses e LEFT OUTER JOIN expense_categories ec");
        query.append(" ON e.category_id_fk=ec.id WHERE customer_id_fk=?");
        log4jLog.info(" selectCustomerExpense " + query);
        Object param[] = new Object[]{customerId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Expense>() {

                @Override
                public Expense mapRow(ResultSet rs, int i) throws SQLException {
                    Expense expense = new Expense();
                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setAccountId(0);
                    user.setFirstName("");
                    user.setLastName("");
                    user.setEmailAddress("");
                    user.setPassword("");
                    user.setMobileNo("");
                    user.setGender(0);
                    user.setRole(0);
                    user.setActive(false);
                    user.setLastLoggedOn(new Timestamp(0));
                    user.setLastKnownLocationTime(new Timestamp(0));
                    user.setLastKnownLocation("");
                    user.setCreatedOn(new Timestamp(0));
                    user.setCreatedBy(0);

                    User approvedOrRejectedBy = new User();
                    approvedOrRejectedBy.setId(rs.getInt("approved_rejected_by_id_fk"));
                    approvedOrRejectedBy.setAccountId(0);
                    approvedOrRejectedBy.setFirstName("");
                    approvedOrRejectedBy.setLastName("");
                    approvedOrRejectedBy.setEmailAddress("");
                    approvedOrRejectedBy.setPassword("");
                    approvedOrRejectedBy.setMobileNo("");
                    approvedOrRejectedBy.setGender(0);
                    approvedOrRejectedBy.setRole(0);
                    approvedOrRejectedBy.setActive(false);
                    approvedOrRejectedBy.setLastLoggedOn(new Timestamp(0));
                    approvedOrRejectedBy.setLastKnownLocationTime(new Timestamp(0));
                    approvedOrRejectedBy.setLastKnownLocation("");
                    approvedOrRejectedBy.setCreatedOn(new Timestamp(0));
                    approvedOrRejectedBy.setCreatedBy(0);

                    ExpenseImage expenseImage = new ExpenseImage();
                    expenseImage.setId(0);
                    expenseImage.setExpenseId(0);
                    expenseImage.setImageURL("");
                    expenseImage.setUser(user);
                    expenseImage.setCreatedOn(new Timestamp(0));

                    expense.setId(rs.getInt("id"));
                    expense.setAppointmentId(rs.getInt("appointment_id_fk"));
                    expense.setCustomerId(rs.getInt("customer_id_fk"));
                    expense.setUser(user);
                    expense.setExpenseName(rs.getString("expense_name"));
                    expense.setExpenseType(rs.getInt("Expense_type_id_fk"));
                    expense.setDescription(rs.getString("desription"));
                    expense.setAmountSpent(rs.getDouble("amount_spent"));
                    expense.setStatus(rs.getInt("status"));
                    expense.setSubmittedOn(rs.getTimestamp("submitted_on"));
                    expense.setApprovedOrRejectedOn(rs.getTimestamp("approved_rejected_on"));
                    expense.setApprovedOrRjectedBy(approvedOrRejectedBy);
                    expense.setRejectedReson(rs.getString("rejected_reason"));
                    expense.setCreatedOn(rs.getTimestamp("created_on"));
                    expense.setExpenseImage(expenseImage);

                    ExpenseCategory eCategory = new ExpenseCategory();
                    eCategory.setCategoryName(rs.getString("ec.category_name"));
                    expense.seteCategoryName(eCategory);
                    return expense;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectCustomerExpense " + e);
//            e.printStackTrace();
            return new ArrayList<Expense>();
        }
    }

    /**
     *
     * @param expenseType
     * @param accountId
     * @return
     */
    @Override
    public List<Expense> selectSpecificTypeExpense(int expenseType, int accountId) {
        StringBuilder query = new StringBuilder();
        /*query.append("SELECT id,appointment_id_fk,customer_id_fk,user_id_fk,expense_name,Expense_type_id_fk,desription,");
        query.append("amount_spent,status,expense_time,submitted_on,approved_rejected_on,");
        query.append("approved_rejected_by_id_fk,rejected_reason,created_on FROM expenses WHERE Expense_type_id_fk=?");*/

        query.append("SELECT e.id,appointment_id_fk,customer_id_fk,user_id_fk,expense_name,Expense_type_id_fk,desription,");
        query.append("amount_spent,status,expense_time,submitted_on,approved_rejected_on,");
        query.append("approved_rejected_by_id_fk,rejected_reason,e.created_on,ec.category_name FROM expenses e");
        query.append(" LEFT OUTER JOIN expense_categories ec ON e.category_id_fk=ec.id");
        query.append(" WHERE Expense_type_id_fk=?");
        log4jLog.info(" selectSpecificTypeExpense " + query);
        Object param[] = new Object[]{expenseType};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Expense>() {

                @Override
                public Expense mapRow(ResultSet rs, int i) throws SQLException {
                    Expense expense = new Expense();
                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setAccountId(0);
                    user.setFirstName("");
                    user.setLastName("");
                    user.setEmailAddress("");
                    user.setPassword("");
                    user.setMobileNo("");
                    user.setGender(0);
                    user.setRole(0);
                    user.setActive(false);
                    user.setLastLoggedOn(new Timestamp(0));
                    user.setLastKnownLocationTime(new Timestamp(0));
                    user.setLastKnownLocation("");
                    user.setCreatedOn(new Timestamp(0));
                    user.setCreatedBy(0);

                    User approvedOrRejectedBy = new User();
                    approvedOrRejectedBy.setId(rs.getInt("approved_rejected_by_id_fk"));
                    approvedOrRejectedBy.setAccountId(0);
                    approvedOrRejectedBy.setFirstName("");
                    approvedOrRejectedBy.setLastName("");
                    approvedOrRejectedBy.setEmailAddress("");
                    approvedOrRejectedBy.setPassword("");
                    approvedOrRejectedBy.setMobileNo("");
                    approvedOrRejectedBy.setGender(0);
                    approvedOrRejectedBy.setRole(0);
                    approvedOrRejectedBy.setActive(false);
                    approvedOrRejectedBy.setLastLoggedOn(new Timestamp(0));
                    approvedOrRejectedBy.setLastKnownLocationTime(new Timestamp(0));
                    approvedOrRejectedBy.setLastKnownLocation("");
                    approvedOrRejectedBy.setCreatedOn(new Timestamp(0));
                    approvedOrRejectedBy.setCreatedBy(0);

                    ExpenseImage expenseImage = new ExpenseImage();
                    expenseImage.setId(0);
                    expenseImage.setExpenseId(0);
                    expenseImage.setImageURL("");
                    expenseImage.setUser(user);
                    expenseImage.setCreatedOn(new Timestamp(0));

                    expense.setId(rs.getInt("id"));
                    expense.setAppointmentId(rs.getInt("appointment_id_fk"));
                    expense.setCustomerId(rs.getInt("customer_id_fk"));
                    expense.setUser(user);
                    expense.setExpenseName(rs.getString("expense_name"));
                    expense.setExpenseType(rs.getInt("Expense_type_id_fk"));
                    expense.setDescription(rs.getString("desription"));
                    expense.setAmountSpent(rs.getDouble("amount_spent"));
                    expense.setStatus(rs.getInt("status"));
                    expense.setSubmittedOn(rs.getTimestamp("submitted_on"));
                    expense.setApprovedOrRejectedOn(rs.getTimestamp("approved_rejected_on"));
                    expense.setApprovedOrRjectedBy(approvedOrRejectedBy);
                    expense.setRejectedReson(rs.getString("rejected_reason"));
                    expense.setCreatedOn(rs.getTimestamp("created_on"));
                    expense.setExpenseImage(expenseImage);

                    ExpenseCategory eCategory = new ExpenseCategory();
                    eCategory.setCategoryName(rs.getString("ec.category_name"));
                    expense.seteCategoryName(eCategory);
                    return expense;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectSpecificTypeExpense " + e);
//            e.printStackTrace();
            return new ArrayList<Expense>();
        }
    }

    /**
     *
     * @param expense
     * @param accountId
     * @return
     */
    @Override
    public boolean updateExpense(Expense expense, int accountId) {
        StringBuffer expenseImageIdList=new StringBuffer();
        try{
        if(!expense.getExpenseImageArray().isEmpty()){
            
             expenseImageIdList.append(expense.getExpenseImageArray().get(0).getId());
                for(int i=1;i<expense.getExpenseImageArray().size();i++){
                    expenseImageIdList.append(",");
                     expenseImageIdList.append(expense.getExpenseImageArray().get(i).getId());
            }
        }
//            System.out.println("Data "+expenseImageIdList.toString());
        }catch(Exception e){
        e.printStackTrace();
        }
        
        String query = "UPDATE expenses SET expense_name=?,Expense_type_id_fk=?,desription=?,amount_spent=?,status=?,expense_time=?,category_id_fk=?,exp_category_name=?,appointment_id_fk=?,customer_id_fk=?,expense_image_id_csv=? WHERE id=?"; // appId,custId added by jyoti
        log4jLog.info(" updateExpense " + query);
        Object param[] = new Object[]{expense.getExpenseName(), expense.getExpenseType(), expense.getDescription(), expense.getAmountSpent(), expense.getStatus(),expense.getExpenseTime(), expense.geteCategoryName().getId(),expense.getExp_category_name(),expense.getAppointmentId(), expense.getCustomerId(),expenseImageIdList.toString(),expense.getId()}; // appId,custId added by jyoti  // added multiple image by siddhesh
//        Object param[] = new Object[]{expense.getExpenseName(), expense.getExpenseType(), expense.getDescription(), expense.getAmountSpent(), expense.getStatus(),expense.getExpenseTime(), expense.geteCategoryName().getId(),expense.getExp_category_name(),expense.getAppointmentId(), expense.getCustomerId(),expense.getId()}; 
        String query2 ="INSERT INTO expenses_audit(expense_id_fk,appointment_id_fk,customer_id_fk,user_id_fk,expense_name,Expense_type_id_fk,desription,amount_spent,status,expense_time,submitted_on,approved_rejected_on,created_on,category_id_fk,exp_category_name) ";
        query2 +=" VALUES (?,?,?,?,?,?,?,?,?,?,now(),now(),?,?,?)";
        Object[] param2 = new Object[]{expense.getId(),expense.getAppointmentId(), expense.getCustomerId(), expense.getUser().getId(), expense.getExpenseName(),expense.getExpenseType(), expense.getDescription(), expense.getAmountSpent(),expense.getStatus(),expense.getExpenseTime(),expense.getCreatedOn(), expense.geteCategoryName().getId(),expense.getExp_category_name()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                try{
                   if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query2, param2) > 0) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    log4jLog.info(" updateExpense " + e);
        //            System.out.println("updateExpense"+e);
//                  e.printStackTrace();
                    return false;
                }  
            }else{
                 return false;
            }    
        } catch (Exception e) {
            log4jLog.info(" updateExpense " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param expenseId
     * @param accountId
     * @return
     */
    @Override
    public boolean deleteExpense(int expenseId, int accountId) {
        String query = "DELETE FROM expenses WHERE id=?";
        log4jLog.info(" deleteExpense " + query);
        Object param[] = new Object[]{expenseId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" deleteExpense " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param expenseImage
     * @param accountId
     * @return
     */
    @Override
    public int insertImage(ExpenseImage expenseImage, int accountId) {
        String query = "INSERT INTO images(expense_id_fk, image_url, user_id_fk, created_on) VALUES ( ?, ?, ?, now())";
        log4jLog.info(" insertImage " + query);
        Object param[] = new Object[]{expenseImage.getExpenseId(), expenseImage.getImageURL(), expenseImage.getUser().getId()};
        try {
            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    String query1 = "SELECT MAX(id) FROM images";
                    try {
                        return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query1, Integer.class);
                    } catch (Exception e) {
                        log4jLog.info(" insertImage " + e);
//                        e.printStackTrace();
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            log4jLog.info(" insertImage " + e);
//            e.printStackTrace();
            return 0;
        }
    }

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    @Override
    public ExpenseImage selectImage(int id, int accountId) {
        String query = "SELECT id,expense_id_fk,image_url,user_id_fk,created_on FROM images WHERE id=?";
        log4jLog.info(" selectImage " + query);
        Object param[] = new Object[]{id};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, new RowMapper<ExpenseImage>() {

                @Override
                public ExpenseImage mapRow(ResultSet rs, int i) throws SQLException {
                    ExpenseImage expenseImage = new ExpenseImage();
                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setAccountId(0);
                    user.setFirstName("");
                    user.setLastName("");
                    user.setEmailAddress("");
                    user.setPassword("");
                    user.setMobileNo("");
                    user.setGender(0);
                    user.setRole(0);
                    user.setActive(false);
                    user.setLastLoggedOn(new Timestamp(0));
                    user.setLastKnownLocationTime(new Timestamp(0));
                    user.setLastKnownLocation("");
                    user.setCreatedOn(new Timestamp(0));
                    user.setCreatedBy(0);

                    expenseImage.setId(rs.getInt("id"));
                    expenseImage.setExpenseId(rs.getInt("expense_id_fk"));
                    expenseImage.setImageURL(rs.getString("image_url"));
                    expenseImage.setUser(user);
                    expenseImage.setCreatedOn(rs.getTimestamp("created_on"));
                    return expenseImage;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectImage " + e);
//            e.printStackTrace();
            return new ExpenseImage();
        }
    }

    /**
     *
     * @param expenseId
     * @param accountId
     * @return
     */
    @Override
    public ExpenseImage selectExpenseImage(int expenseId, int accountId) {
        String query = "SELECT id,expense_id_fk,image_url,user_id_fk,created_on FROM images WHERE id=?";
        log4jLog.info(" selectExpenseImage " + query);
        Object param[] = new Object[]{expenseId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, new RowMapper<ExpenseImage>() {

                @Override
                public ExpenseImage mapRow(ResultSet rs, int i) throws SQLException {
                    ExpenseImage expenseImage = new ExpenseImage();
                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setAccountId(0);
                    user.setFirstName("");
                    user.setLastName("");
                    user.setEmailAddress("");
                    user.setPassword("");
                    user.setMobileNo("");
                    user.setGender(0);
                    user.setRole(0);
                    user.setActive(false);
                    user.setLastLoggedOn(new Timestamp(0));
                    user.setLastKnownLocationTime(new Timestamp(0));
                    user.setLastKnownLocation("");
                    user.setCreatedOn(new Timestamp(0));
                    user.setCreatedBy(0);

                    expenseImage.setId(rs.getInt("id"));
                    expenseImage.setExpenseId(rs.getInt("expense_id_fk"));
                    expenseImage.setImageURL(rs.getString("image_url"));
                    expenseImage.setUser(user);
                    expenseImage.setCreatedOn(rs.getTimestamp("created_on"));
                    return expenseImage;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectExpenseImage " + e);
//            e.printStackTrace();
            return new ExpenseImage();
        }
    }

    /**
     *
     * @param expenseImage
     * @param accountId
     * @return
     */
    @Override
    public boolean updateImage(ExpenseImage expenseImage, int accountId) {
        String query = "UPDATE images SET expense_id_fk=?,image_url=?,user_id_fk=? WHERE id=?";
        log4jLog.info(" updateImage " + query);
        Object param[] = new Object[]{expenseImage.getExpenseId(), expenseImage.getImageURL(), expenseImage.getUser().getId(), expenseImage.getId()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateImage " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    @Override
    public boolean deleteImage(int id, int accountId) {
        String query = "DELETE FROM images WHERE id=?";
        log4jLog.info(" deleteImage " + query);
        Object param[] = new Object[]{id};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" deleteImage " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param expenseId
     * @param accountId
     * @return
     */
    @Override
    public boolean isExpenseValid(int expenseId, int accountId) {
        String query = "SELECT Count(id) FROM expenses WHERE id=?";
        log4jLog.info(" isExpenseValid " + query);
        Object param[] = new Object[]{expenseId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isExpenseValid " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param imageId
     * @param accountId
     * @return
     */
    @Override
    public boolean isImageValid(int imageId, int accountId) {
        String query = "SELECT Count(id) FROM images WHERE id=?";
        log4jLog.info(" isImageValid " + query);
        Object param[] = new Object[]{imageId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isImageValid " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    @Override
    public List<Expense> selectUserExpense(int userId, int accountId) {
        StringBuilder query = new StringBuilder();
//        query.append("SELECT id,appointment_id_fk,customer_id_fk,user_id_fk,expense_name,Expense_type_id_fk,desription,");
//        query.append("amount_spent,status,expense_time,submitted_on,approved_rejected_on,");
//        query.append("approved_rejected_by_id_fk,rejected_reason,created_on FROM expenses WHERE user_id_fk=?");
        query.append("SELECT e.id,e.appointment_id_fk,e.customer_id_fk,e.user_id_fk,e.expense_name,e.Expense_type_id_fk,e.desription,");
        query.append("e.amount_spent,e.status,e.expense_time,e.submitted_on,e.approved_rejected_on,");
        query.append("e.approved_rejected_by_id_fk,e.rejected_reason,e.created_on,c.customer_printas FROM expenses e ");
        query.append(" INNER JOIN customers c ON c.id=e.customer_id_fk ");
        query.append(" WHERE user_id_fk=? ");

        log4jLog.info(" selectUserExpense  " + query);
        Object param[] = new Object[]{userId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Expense>() {

                @Override
                public Expense mapRow(ResultSet rs, int i) throws SQLException {
                    Expense expense = new Expense();
                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setAccountId(0);
                    user.setFirstName("");
                    user.setLastName("");
                    user.setEmailAddress("");
                    user.setPassword("");
                    user.setMobileNo("");
                    user.setGender(0);
                    user.setRole(0);
                    user.setActive(false);
                    user.setLastLoggedOn(new Timestamp(0));
                    user.setLastKnownLocationTime(new Timestamp(0));
                    user.setLastKnownLocation("");
                    user.setCreatedOn(new Timestamp(0));
                    user.setCreatedBy(0);

                    User approvedOrRejectedBy = new User();
                    approvedOrRejectedBy.setId(rs.getInt("approved_rejected_by_id_fk"));
                    approvedOrRejectedBy.setAccountId(0);
                    approvedOrRejectedBy.setFirstName("");
                    approvedOrRejectedBy.setLastName("");
                    approvedOrRejectedBy.setEmailAddress("");
                    approvedOrRejectedBy.setPassword("");
                    approvedOrRejectedBy.setMobileNo("");
                    approvedOrRejectedBy.setGender(0);
                    approvedOrRejectedBy.setRole(0);
                    approvedOrRejectedBy.setActive(false);
                    approvedOrRejectedBy.setLastLoggedOn(new Timestamp(0));
                    approvedOrRejectedBy.setLastKnownLocationTime(new Timestamp(0));
                    approvedOrRejectedBy.setLastKnownLocation("");
                    approvedOrRejectedBy.setCreatedOn(new Timestamp(0));
                    approvedOrRejectedBy.setCreatedBy(0);

                    ExpenseImage expenseImage = new ExpenseImage();
                    expenseImage.setId(0);
                    expenseImage.setExpenseId(0);
                    expenseImage.setImageURL("");
                    expenseImage.setUser(user);
                    expenseImage.setCreatedOn(new Timestamp(0));

                    expense.setId(rs.getInt("id"));
                    expense.setAppointmentId(rs.getInt("appointment_id_fk"));
                    expense.setCustomerId(rs.getInt("customer_id_fk"));
                    expense.setUser(user);
                    expense.setExpenseName(rs.getString("expense_name"));
                    expense.setExpenseType(rs.getInt("Expense_type_id_fk"));
                    expense.setDescription(rs.getString("desription"));
                    expense.setAmountSpent(rs.getDouble("amount_spent"));
                    expense.setStatus(rs.getInt("status"));
                    expense.setSubmittedOn(rs.getTimestamp("submitted_on"));
                    expense.setApprovedOrRejectedOn(rs.getTimestamp("approved_rejected_on"));
                    expense.setApprovedOrRjectedBy(approvedOrRejectedBy);
                    expense.setExpenseTime(rs.getTimestamp("expense_time"));
                    expense.setRejectedReson(rs.getString("rejected_reason"));
                    expense.setCreatedOn(rs.getTimestamp("created_on"));
                    expense.setExpenseImage(expenseImage);
                    expense.setCustomerName(rs.getString("customer_printas"));
                    return expense;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectUserExpense  " + e);
//            e.printStackTrace();
            return new ArrayList<Expense>();
        }
    }

    /**
     *
     * @param expense
     * @param accountId
     * @return
     */
    @Override
    public boolean approveExpense(Expense expense, int accountId) {
       String query = "UPDATE expenses SET status=? ,approved_rejected_on=now(),approved_rejected_by_id_fk=? WHERE id=?";
        String query1 ="INSERT INTO expenses_audit(expense_id_fk,appointment_id_fk,customer_id_fk,user_id_fk,expense_name,Expense_type_id_fk,desription,amount_spent,status,expense_time,submitted_on,approved_rejected_on,approved_rejected_by_id_fk,rejected_reason,created_on,category_id_fk,exp_category_name) ";
        query1 +=" VALUES (?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,?)";
        log4jLog.info(" approveExpense  " + query+" approveExpense audit"+query1);
        Object param[] = new Object[]{expense.getStatus(),expense.getApprovedOrRjectedBy().getId(),expense.getId()};
        Object param1[] = new Object[]{expense.getId(),expense.getAppointmentId(),expense.getCustomerId(),expense.getUser().getId(),expense.getExpenseName(),expense.getExpenseType(),expense.getDescription(),expense.getAmountSpent(),expense.getStatus(),expense.getExpenseTime(),expense.getSubmittedOn(),expense.getApprovedOrRjectedBy().getId(),expense.getRejectedReson(),expense.getCreatedOn(),expense.geteCategoryName().getId(),expense.getExp_category_name()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                try {
                    if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query1, param1) > 0) {
                        return true;
                    } else {
                        return false;
                    }   
                } catch (Exception e) {
                    log4jLog.info(" approveExpense audit " + e);
//                    e.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" approveExpense  " + e);
//            e.printStackTrace();
            return false;
        }
    }
    
    /**
     *
     * @param expense
     * @param accountId
     * @param fieldSenseUtils
     * @return
     */
    @Override
    public boolean approveExpenseByAccount(Expense expense, int accountId,FieldSenseUtils fieldSenseUtils) {
        String query = "UPDATE expenses SET status=? ,approved_rejected_on=now(),approved_rejected_by_id_fk=? WHERE id=?";
        String query1 ="INSERT INTO expenses_audit(expense_id_fk,appointment_id_fk,customer_id_fk,user_id_fk,expense_name,Expense_type_id_fk,desription,amount_spent,status,expense_time,submitted_on,approved_rejected_on,approved_rejected_by_id_fk,rejected_reason,created_on,category_id_fk,exp_category_name) ";
        query1 +=" VALUES (?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,?)";
        log4jLog.info(" approveExpense  " + query+" approveExpense audit"+query1);
        Object param[] = new Object[]{expense.getStatus(),expense.getApprovedOrRjectedBy().getId(),expense.getId()};
        Object param1[] = new Object[]{expense.getId(),expense.getAppointmentId(),expense.getCustomerId(),expense.getUser().getId(),expense.getExpenseName(),expense.getExpenseType(),expense.getDescription(),expense.getAmountSpent(),expense.getStatus(),expense.getExpenseTime(),expense.getSubmittedOn(),expense.getApprovedOrRjectedBy().getId(),expense.getRejectedReson(),expense.getCreatedOn(),expense.geteCategoryName().getId(),expense.getExp_category_name()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                try {
                    if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query1, param1) > 0) {
                            try{
                            /* Push notification for approve  starts */
                            //GcmPushNotifications activityNotifications = new GcmPushNotifications();
                            //String gcmId = fieldSenseUtils.getUserGcmSenderId(expense.getUser().getId());
                            
                            java.util.HashMap gcmInfo = fieldSenseUtils.getGcmUserInfo(expense.getUser().getId());
                            String gcmId =(String)gcmInfo.get("gcmId");
                            int deviceOS =(Integer)gcmInfo.get("deviceOS");
                            
                           // String message="Your expense of amount "+expense.getAmountSpent()+" has been Approved";
                             String message="Your expense claim(s) have been updated";
                            //log4jLog.info(" expense approve Notification deviceOS="+deviceOS+" gcm Id " + gcmId + " approve message "+message);
                            PushNotificationManager push= new PushNotificationManager();
                            push.expenseNotification(message, gcmId, deviceOS, String.valueOf(expense.getId()));
                            /* Push notification for approve  ends */
                            }catch (Exception e) {
                                log4jLog.info(" approv Expense Notification " + e);
//                                e.printStackTrace();
                            }
                        return true;
                    } else {
                        return false;
                    }   
                } catch (Exception e) {
                    log4jLog.info(" approveExpense audit " + e);
//                    e.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" approveExpense  " + e);
//            e.printStackTrace();
            return false;
        }
        
    }
    
    /**
     *
     * @param expense
     * @param accountId
     * @param fieldSenseUtils
     * @return
     */
    @Override
    public boolean approveMultiExpenseByAccount(Expense[] expense, int accountId,FieldSenseUtils fieldSenseUtils) {
        
        for(int i=0;i<expense.length;i++){
            Expense expense1=expense[i];
            String query = "UPDATE expenses SET status=? ,approved_rejected_on=now(),approved_rejected_by_id_fk=? WHERE id=?";
            String query1 ="INSERT INTO expenses_audit(expense_id_fk,appointment_id_fk,customer_id_fk,user_id_fk,expense_name,Expense_type_id_fk,desription,amount_spent,status,expense_time,submitted_on,approved_rejected_on,approved_rejected_by_id_fk,rejected_reason,created_on,category_id_fk,exp_category_name) ";
            query1 +=" VALUES (?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,?)";
            log4jLog.info(" approveExpense  " + query+" approveExpense audit"+query1);
            Object param[] = new Object[]{expense1.getStatus(),expense1.getApprovedOrRjectedBy().getId(),expense1.getId()};
            Object param1[] = new Object[]{expense1.getId(),expense1.getAppointmentId(),expense1.getCustomerId(),expense1.getUser().getId(),expense1.getExpenseName(),expense1.getExpenseType(),expense1.getDescription(),expense1.getAmountSpent(),expense1.getStatus(),expense1.getExpenseTime(),expense1.getSubmittedOn(),expense1.getApprovedOrRjectedBy().getId(),expense1.getRejectedReson(),expense1.getCreatedOn(),expense1.geteCategoryName().getId(),expense1.getExp_category_name()};
            try {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    try {
                        if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query1, param1) > 0) {                  
                            try{
                            /* Push notification for approve  starts */
                            //GcmPushNotifications activityNotifications = new GcmPushNotifications();
                            //String gcmId = fieldSenseUtils.getUserGcmSenderId(expense1.getUser().getId());
                           // String message="Your expense of amount "+expense1.getAmountSpent()+" has been Approved";
                            java.util.HashMap gcmInfo = fieldSenseUtils.getGcmUserInfo(expense1.getUser().getId());
                            String gcmId =(String)gcmInfo.get("gcmId");
                            int deviceOS =(Integer)gcmInfo.get("deviceOS");
                            
                            String message="Your expense claim(s) have been updated";
                            //log4jLog.info(" expense approve Notification deviceOS="+deviceOS+" gcm Id " + gcmId + " approve message "+message);
                            
                            PushNotificationManager push= new PushNotificationManager();
                            push.expenseNotification(message, gcmId, deviceOS, String.valueOf(expense1.getId()));
                            /* Push notification for approve  ends */
                            }catch (Exception e) {
                                log4jLog.info(" approv Expense Notification " + e);
//                                e.printStackTrace();
                            }
                        } else {
                            return false;
                        }   
                    } catch (Exception e) {
                        log4jLog.info(" approveExpense audit " + e);
//                        e.printStackTrace();
                        return false;
                    }
                } else {
                    return false;
                }
            } catch (Exception e) {
                log4jLog.info(" approveExpense  " + e);
//                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param expense
     * @param accountId
     * @return
     */
    @Override
    public boolean rejectExpense(Expense expense, int accountId) {
        String query = "UPDATE expenses SET status=?,approved_rejected_on=now(),approved_rejected_by_id_fk=? ,rejected_reason=? WHERE id=?";
        log4jLog.info(" rejectExpense  " + query);
        String query1 ="INSERT INTO expenses_audit(expense_id_fk,appointment_id_fk,customer_id_fk,user_id_fk,expense_name,Expense_type_id_fk,desription,amount_spent,status,expense_time,submitted_on,approved_rejected_on,approved_rejected_by_id_fk,rejected_reason,created_on,category_id_fk,exp_category_name) ";
        query1 +=" VALUES (?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,?)";
        Object param[] = new Object[]{expense.getStatus(),expense.getApprovedOrRjectedBy().getId(),expense.getRejectedReson(),expense.getId()};
        Object param1[] = new Object[]{expense.getId(),expense.getAppointmentId(),expense.getCustomerId(),expense.getUser().getId(),expense.getExpenseName(),expense.getExpenseType(),expense.getDescription(),expense.getAmountSpent(),expense.getStatus(),expense.getExpenseTime(),expense.getSubmittedOn(),expense.getApprovedOrRjectedBy().getId(),expense.getRejectedReson(),expense.getCreatedOn(),expense.geteCategoryName().getId(),expense.getExp_category_name()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                try {
                    if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query1, param1) > 0) {
                        return true;
                    } else {
                        return false;
                    }   
                } catch (Exception e) {
                    log4jLog.info(" approveExpense audit " + e);
//                    e.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" rejectExpense  " + e);
//            e.printStackTrace();
            return false;
        }
    }
    
    /**
     *
     * @param expense
     * @param accountId
     * @param fieldSenseUtils
     * @return
     */
    @Override
    public boolean rejectExpenseByAccount(Expense expense,int accountId,FieldSenseUtils fieldSenseUtils) {
        String query = "UPDATE expenses SET status=?,approved_rejected_on=now(),approved_rejected_by_id_fk=? ,rejected_reason=? WHERE id=?";
        log4jLog.info(" rejectExpense  " + query);
        String query1 ="INSERT INTO expenses_audit(expense_id_fk,appointment_id_fk,customer_id_fk,user_id_fk,expense_name,Expense_type_id_fk,desription,amount_spent,status,expense_time,submitted_on,approved_rejected_on,approved_rejected_by_id_fk,rejected_reason,created_on,category_id_fk,exp_category_name) ";
        query1 +=" VALUES (?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,?)";
        Object param[] = new Object[]{expense.getStatus(),expense.getApprovedOrRjectedBy().getId(),expense.getRejectedReson(),expense.getId()};
        Object param1[] = new Object[]{expense.getId(),expense.getAppointmentId(),expense.getCustomerId(),expense.getUser().getId(),expense.getExpenseName(),expense.getExpenseType(),expense.getDescription(),expense.getAmountSpent(),expense.getStatus(),expense.getExpenseTime(),expense.getSubmittedOn(),expense.getApprovedOrRjectedBy().getId(),expense.getRejectedReson(),expense.getCreatedOn(),expense.geteCategoryName().getId(),expense.getExp_category_name()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                try {
                    if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query1, param1) > 0) {
                        try{
                            /* Push notification for approve  starts */
                            //GcmPushNotifications activityNotifications = new GcmPushNotifications();
                            //String gcmId = fieldSenseUtils.getUserGcmSenderId(expense.getUser().getId());
                            //String message="Your expense of amount "+expense.getAmountSpent()+" has been Rejected";
                            java.util.HashMap gcmInfo = fieldSenseUtils.getGcmUserInfo(expense.getUser().getId());
                            String gcmId =(String)gcmInfo.get("gcmId");
                            int deviceOS =(Integer)gcmInfo.get("deviceOS");
                            
                            String message="Your expense claim(s) have been updated";
                            //log4jLog.info(" expense reject Notification deviceOS="+deviceOS+" gcm Id " + gcmId + " reject message "+message);
                               
                            PushNotificationManager push= new PushNotificationManager();
                            push.expenseNotification(message, gcmId, deviceOS, String.valueOf(expense.getId()));
                            /* Push notification for approve  ends */
                        }catch (Exception e) {
                                log4jLog.info(" reject Expense Notification " + e);
//                                e.printStackTrace();
                        }
                        return true;
                    } else {
                        return false;
                    }   
                } catch (Exception e) {
                    log4jLog.info(" approveExpense audit " + e);
//                    e.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" rejectExpense  " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param expense
     * @param accountId
     * @param fieldSenseUtils
     * @return
     */
    @Override
    public boolean rejectMultiExpenseByAccount(Expense[] expense, int accountId,FieldSenseUtils fieldSenseUtils) {
        
        for(int i=0;i<expense.length;i++){
            Expense expense1=expense[i];
            String query = "UPDATE expenses SET status=? ,approved_rejected_on=now(),approved_rejected_by_id_fk=?,rejected_reason=? WHERE id=?";
            String query1 ="INSERT INTO expenses_audit(expense_id_fk,appointment_id_fk,customer_id_fk,user_id_fk,expense_name,Expense_type_id_fk,desription,amount_spent,status,expense_time,submitted_on,approved_rejected_on,approved_rejected_by_id_fk,rejected_reason,created_on,category_id_fk,exp_category_name) ";
            query1 +=" VALUES (?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,?)";
            log4jLog.info("reject Expense  " + query+" reject Expense audit"+query1);
            Object param[] = new Object[]{expense1.getStatus(),expense1.getApprovedOrRjectedBy().getId(),expense1.getRejectedReson(),expense1.getId()};
            Object param1[] = new Object[]{expense1.getId(),expense1.getAppointmentId(),expense1.getCustomerId(),expense1.getUser().getId(),expense1.getExpenseName(),expense1.getExpenseType(),expense1.getDescription(),expense1.getAmountSpent(),expense1.getStatus(),expense1.getExpenseTime(),expense1.getSubmittedOn(),expense1.getApprovedOrRjectedBy().getId(),expense1.getRejectedReson(),expense1.getCreatedOn(),expense1.geteCategoryName().getId(),expense1.getExp_category_name()};
            try {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    try {
                        if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query1, param1) > 0) {                  
                            try{
                            /* Push notification for approve  starts */
                            //GcmPushNotifications activityNotifications = new GcmPushNotifications();
                            //String gcmId = fieldSenseUtils.getUserGcmSenderId(expense1.getUser().getId());
                            java.util.HashMap gcmInfo = fieldSenseUtils.getGcmUserInfo(expense1.getUser().getId());
                            String gcmId =(String)gcmInfo.get("gcmId");
                            int deviceOS =(Integer)gcmInfo.get("deviceOS");
                            
                            //String message="Your expense of amount "+expense1.getAmountSpent()+" has been Rejected";
                             String message="Your expense claim(s) have been updated";
                            //log4jLog.info(" expense reject Notification deviceOS="+deviceOS+" gcm Id " + gcmId + " reject message "+message);
                              
                            PushNotificationManager push= new PushNotificationManager();
                            push.expenseNotification(message, gcmId, deviceOS, String.valueOf(expense1.getId()));
                            /* Push notification for approve  ends */
                            }catch (Exception e) {
                                    log4jLog.info(" reject Expense Notification " + e);
//                                    e.printStackTrace();
                            }
                        } else {
                            return false;
                        }   
                    } catch (Exception e) {
                        log4jLog.info(" reject Expense audit " + e);
//                        e.printStackTrace();
                        return false;
                    }
                } else {
                    return false;
                }
            } catch (Exception e) {
                log4jLog.info(" reject Expense  " + e);
//                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
    
    /**
     *
     * @param expense
     * @param status
     * @param accountId
     * @param fieldSenseUtils
     * @return
     */
    @Override
    public boolean disburseExpenseByAccount(Expense expense,int status,int accountId,FieldSenseUtils fieldSenseUtils) {
        String query = "UPDATE expenses SET status=?,approved_rejected_on=now(),approved_rejected_by_id_fk=? ,disburse_amount=?,payment_mode=?,default_date=? WHERE id=?";
        log4jLog.info(" dusburseExpense  " + query);
        Object param[] = new Object[]{expense.getStatus(),expense.getApprovedOrRjectedBy().getId(),expense.getDisburse_amount(),expense.getPayment_mode(),expense.getDefault_date(),expense.getId()};
        try {
            if(status!=3){
              try{
                String query2 ="INSERT INTO expenses_audit(expense_id_fk,appointment_id_fk,customer_id_fk,user_id_fk,expense_name,Expense_type_id_fk,desription,amount_spent,status,expense_time,submitted_on,approved_rejected_on,approved_rejected_by_id_fk,rejected_reason,created_on,category_id_fk,disburse_amount,payment_mode,default_date,exp_category_name) ";
                query2 +=" VALUES (?,?,?,?,?,?,?,?,3,?,?,now(),?,?,?,?,?,?,?,?)";                        
                log4jLog.info(" approve Expense audit  " + query);
                Object param2[] = new Object[]{expense.getId(),expense.getAppointmentId(),expense.getCustomerId(),expense.getUser().getId(),expense.getExpenseName(),expense.getExpenseType(),expense.getDescription(),expense.getAmountSpent(),expense.getExpenseTime(),expense.getSubmittedOn(),expense.getApprovedOrRjectedBy().getId(),expense.getRejectedReson(),expense.getCreatedOn(),expense.geteCategoryName().getId(),expense.getDisburse_amount(),expense.getPayment_mode(),expense.getDefault_date(),expense.getExp_category_name()};
                FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query2, param2);  
                try{
                    /* Push notification for approve  starts */
                    //GcmPushNotifications activityNotifications = new GcmPushNotifications();
                    //String gcmId = fieldSenseUtils.getUserGcmSenderId(expense.getUser().getId());
                    java.util.HashMap gcmInfo = fieldSenseUtils.getGcmUserInfo(expense.getUser().getId());
                    String gcmId =(String)gcmInfo.get("gcmId");
                    int deviceOS =(Integer)gcmInfo.get("deviceOS");
                    
                    //String message="Your expense of amount "+expense.getAmountSpent()+" has been Approved";
                    String message="Your expense claim(s) have been updated";
                    //log4jLog.info(" expense approve Notification deviceOS="+deviceOS+" gcm Id " + gcmId + " approve message "+message);
                    
                    PushNotificationManager push= new PushNotificationManager();
                    push.expenseNotification(message, gcmId, deviceOS, String.valueOf(expense.getId()));
                    /* Push notification for approve  ends */
                    }catch (Exception e) {
                        log4jLog.info(" approve Expense Notification " + e);
//                        e.printStackTrace();
                    }
                }catch(Exception e) {
                log4jLog.info(" approve Expense audit " + e);
//                e.printStackTrace();
                return false;
                } 
            }
            try{
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    try{
                        String query2 ="INSERT INTO expenses_audit(expense_id_fk,appointment_id_fk,customer_id_fk,user_id_fk,expense_name,Expense_type_id_fk,desription,amount_spent,status,expense_time,submitted_on,approved_rejected_on,approved_rejected_by_id_fk,rejected_reason,created_on,category_id_fk,disburse_amount,payment_mode,default_date,exp_category_name) ";
                        query2 +=" VALUES (?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,?,?,?,?)";                        
                        log4jLog.info(" disburse Expense audit  " + query);
                        Object param2[] = new Object[]{expense.getId(),expense.getAppointmentId(),expense.getCustomerId(),expense.getUser().getId(),expense.getExpenseName(),expense.getExpenseType(),expense.getDescription(),expense.getAmountSpent(),expense.getStatus(),expense.getExpenseTime(),expense.getSubmittedOn(),expense.getApprovedOrRjectedBy().getId(),expense.getRejectedReson(),expense.getCreatedOn(),expense.geteCategoryName().getId(),expense.getDisburse_amount(),expense.getPayment_mode(),expense.getDefault_date(),expense.getExp_category_name()};
                        FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query2, param2); 
                        return true;  
                    }catch(Exception e) {
                        log4jLog.info(" disburse Expense audit " + e);
//                        e.printStackTrace();
                        return false;
                        } 
                } else {
                        return false;
                }     
            }catch(Exception e) {
                log4jLog.info(" disburse Expense  " + e);
//                e.printStackTrace();
                return false;
            }    
                 
        } catch (Exception e) {
            log4jLog.info(" dusburseExpense  " + e);
//            e.printStackTrace();
            return false;
        }
    }
    
    /**
     *
     * @param expense
     * @param status
     * @param accountId
     * @param fieldSenseUtils
     * @return
     */
    @Override
    public boolean disburseExpenseDefaultByAccount(Expense expense,int status,int accountId,FieldSenseUtils fieldSenseUtils) {
        String query = "UPDATE expenses SET status=?,approved_rejected_on=now(),approved_rejected_by_id_fk=? ,disburse_amount=?,payment_mode=?,default_date=? WHERE id=?";
        log4jLog.info(" dusburseExpense  " + query);
        Object param[] = new Object[]{expense.getStatus(),expense.getApprovedOrRjectedBy().getId(),expense.getDisburse_amount(),expense.getPayment_mode(),expense.getDefault_date(),expense.getId()};
        String query3 = "update expenses set default_date=? where status!=5";
        log4jLog.info(" dusburseExpense  " + query);
        Object param3[] = new Object[]{expense.getDefault_date()};
        try {
            if(status!=3){
              try{
                String query2 ="INSERT INTO expenses_audit(expense_id_fk,appointment_id_fk,customer_id_fk,user_id_fk,expense_name,Expense_type_id_fk,desription,amount_spent,status,expense_time,submitted_on,approved_rejected_on,approved_rejected_by_id_fk,rejected_reason,created_on,category_id_fk,disburse_amount,payment_mode,default_date,exp_category_name) ";
                query2 +=" VALUES (?,?,?,?,?,?,?,?,3,?,?,now(),?,?,?,?,?,?,?,?)";                        
                log4jLog.info(" approve Expense audit  " + query2);
                Object param2[] = new Object[]{expense.getId(),expense.getAppointmentId(),expense.getCustomerId(),expense.getUser().getId(),expense.getExpenseName(),expense.getExpenseType(),expense.getDescription(),expense.getAmountSpent(),expense.getExpenseTime(),expense.getSubmittedOn(),expense.getApprovedOrRjectedBy().getId(),expense.getRejectedReson(),expense.getCreatedOn(),expense.geteCategoryName().getId(),expense.getDisburse_amount(),expense.getPayment_mode(),expense.getDefault_date(),expense.getExp_category_name()};
                FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query2, param2);         
                try{
                    /* Push notification for approve  starts */
                    //GcmPushNotifications activityNotifications = new GcmPushNotifications();
                    //String gcmId = fieldSenseUtils.getUserGcmSenderId(expense.getUser().getId());
                    java.util.HashMap gcmInfo = fieldSenseUtils.getGcmUserInfo(expense.getUser().getId());
                    String gcmId =(String)gcmInfo.get("gcmId");
                    int deviceOS =(Integer)gcmInfo.get("deviceOS");
                                        
                    //String message="Your expense of amount "+expense.getAmountSpent()+" has been Approved";
                    String message="Your expense claim(s) have been updated";
                    //log4jLog.info(" expense approve Notification deviceOS"+deviceOS+" gcm Id " + gcmId + " approve message "+message);
                    
                    PushNotificationManager push= new PushNotificationManager();
                    push.expenseNotification(message, gcmId, deviceOS, String.valueOf(expense.getId()));
                    /* Push notification for approve  ends */
                    }catch (Exception e) {
                        log4jLog.info(" approve Expense Notification " + e);
//                        e.printStackTrace();
                    }
                }catch(Exception e) {
                log4jLog.info(" approve Expense audit " + e);
//                e.printStackTrace();
                return false;
                } 
            }
            try{
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    if(FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query3, param3) > 0){
                        try{
                            String query2 ="INSERT INTO expenses_audit(expense_id_fk,appointment_id_fk,customer_id_fk,user_id_fk,expense_name,Expense_type_id_fk,desription,amount_spent,status,expense_time,submitted_on,approved_rejected_on,approved_rejected_by_id_fk,rejected_reason,created_on,category_id_fk,disburse_amount,payment_mode,default_date,exp_category_name) ";
                            query2 +=" VALUES (?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,?,?,?,?)";                        
                            log4jLog.info(" disburse Expense audit  " + query);
                            Object param2[] = new Object[]{expense.getId(),expense.getAppointmentId(),expense.getCustomerId(),expense.getUser().getId(),expense.getExpenseName(),expense.getExpenseType(),expense.getDescription(),expense.getAmountSpent(),expense.getStatus(),expense.getExpenseTime(),expense.getSubmittedOn(),expense.getApprovedOrRjectedBy().getId(),expense.getRejectedReson(),expense.getCreatedOn(),expense.geteCategoryName().getId(),expense.getDisburse_amount(),expense.getPayment_mode(),expense.getDefault_date(),expense.getExp_category_name()};
                            FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query2, param2);             
                            return true;  
                        }catch(Exception e) {
                            log4jLog.info(" disburse Expense audit " + e);
//                            e.printStackTrace();
                            return false;
                        } 
                    }else{
                           return false;               
                    }    
                } else {
                        return false;
                }     
            }catch(Exception e) {
                log4jLog.info(" disburse Expense  " + e);
//                e.printStackTrace();
                return false;
            }    
                 
        } catch (Exception e) {
            log4jLog.info(" dusburseExpense  " + e);
//            e.printStackTrace();
            return false;
        }
    }
    
    /**
     *
     * @param userId
     * @param approverId
     * @param accountId
     * @return
     */
    @Override
    public boolean approveUserAllExpenses(int userId, int approverId, int accountId) {
        String query = "UPDATE expenses SET status=1,approved_rejected_on=now(),approved_rejected_by_id_fk=? WHERE user_id_fk=? AND status=0";
        log4jLog.info(" approveUserAllExpenses  " + query);
        Object param[] = new Object[]{approverId, userId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" approveUserAllExpenses  " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param userId
     * @param startDate
     * @param endDate
     * @param expenseStatus
     * @param accountId
     * @return
     */
    @Override
    public List<Expense> selectUserExpense(int userId, String startDate, String endDate, int expenseStatus, final int accountId) {
          StringBuilder query = new StringBuilder();
        /*query.append("SELECT e.id,appointment_id_fk,e.customer_id_fk,e.user_id_fk,e.expense_name,e.Expense_type_id_fk,e.desription,");
        query.append("e.amount_spent,e.status,e.expense_time,e.submitted_on,e.approved_rejected_on,");
        query.append("e.approved_rejected_by_id_fk,e.rejected_reason,e.created_on,a.appointment_title,c.customer_name FROM expenses e");
        query.append(" INNER JOIN appointments a ON e.appointment_id_fk=a.id");
        query.append(" INNER JOIN customers c ON e.customer_id_fk=c.id");
        query.append(" WHERE e.user_id_fk=? AND DATE(e.submitted_on) BETWEEN ? AND ? ORDER BY e.expense_time DESC");*/
	
        query.append("SELECT e.id,e.appointment_id_fk,e.customer_id_fk,e.user_id_fk,e.expense_name,e.Expense_type_id_fk,e.desription,");
        query.append("e.amount_spent,e.status,e.expense_time,e.submitted_on,e.approved_rejected_on,");
        query.append("e.approved_rejected_by_id_fk,e.rejected_reason,e.created_on,e.category_id_fk,IFNULL (e.disburse_amount,0) disburse_amount,IFNULL (e.payment_mode,'') payment_mode,IFNULL (e.default_date,'1111-11-11 11:11:11') default_date,IFNULL (e.exp_category_name,'') exp_category_name,a.appointment_title,c.customer_name,u.id,IFNULL (u.first_name,'') first_name,IFNULL (u.last_name,'') last_name,IFNULL (u.full_name,'') full_name,ue.id,ue.report_to,ue.first_name,ue.last_name,ue.full_name,ec.id,IFNULL(ue1.full_name,'') reporting_head,ec.category_name FROM expenses e");
        query.append(" LEFT JOIN appointments a ON e.appointment_id_fk=a.id");
        query.append(" LEFT JOIN customers c ON e.customer_id_fk=c.id");
	query.append(" LEFT OUTER JOIN fieldsense.users u ON u.id=e.approved_rejected_by_id_fk");
        query.append(" INNER JOIN fieldsense.users ue ON ue.id=e.user_id_fk");
        query.append(" INNER JOIN fieldsense.users ue1 ON ue1.id=ue.report_to");
        query.append(" LEFT OUTER JOIN expense_categories ec ON e.category_id_fk=ec.id");
        query.append(" WHERE e.user_id_fk=? AND e.status=? AND (e.expense_time BETWEEN ? AND ?) ORDER BY e.expense_time DESC");
        log4jLog.info(" selectUserExpense  " + query);
        Object param[] = new Object[]{userId, expenseStatus, startDate, endDate};
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
            log4jLog.info(" selectUserExpense  " + e);
//            e.printStackTrace();
            return new ArrayList<Expense>();
        }
    }
    
    /**
     *
     * @param allRequestParams
     * @param expfilter
     * @param accountId
     * @return
     */
    @Override
    public List<Expense> selectAccountExpenseDetils(@RequestParam Map<String,String> allRequestParams,ExpenseFilter expfilter,final int accountId) {
        
        Map<String,String> expdetails=new LinkedHashMap<String,String>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT e.id,e.appointment_id_fk,e.customer_id_fk,e.expense_name,e.Expense_type_id_fk,e.desription,e.amount_spent,e.expense_time,e.submitted_on,e.created_on,e.status,e.approved_rejected_on,e.rejected_reason,IFNULL (e.disburse_amount,0) disburse_amount,IFNULL (e.payment_mode,'') payment_mode,IFNULL (e.default_date,'1111-11-11 11:11:11') default_date,IFNULL (e.exp_category_name,'') exp_category_name,");
        query.append("c.customer_printas,c.customer_name,a.appointment_title,e.expense_image_id_csv,u.id,IFNULL (u.first_name,'') first_name,IFNULL (u.last_name,'') last_name,IFNULL (u.full_name,'') full_name,ue.id,ue.report_to,ue.first_name,ue.last_name,ue.full_name,ec.id,IFNULL(ue1.full_name,'') reporting_head,ec.category_name FROM expenses e");
        query.append(" LEFT JOIN customers c ON e.customer_id_fk = c.id");
        query.append(" INNER JOIN expenses ex on e.id=ex.id ");
        if(expfilter.getStatus().trim().equals("All")==false){
            expdetails.put("status",expfilter.getStatus().trim());
            query.append("  and ex.status=?");
        }
        if(expfilter.getExpensecategory().trim().equals("All")==false){
            expdetails.put("expensecategory",expfilter.getExpensecategory().trim());
            query.append("  and ex.category_id_fk=?");
        }
        query.append(" LEFT JOIN appointments a ON a.id=e.appointment_id_fk");
        query.append(" LEFT OUTER JOIN fieldsense.users u ON u.id=ex.approved_rejected_by_id_fk");
        query.append(" INNER JOIN fieldsense.users ue ON ue.id=ex.user_id_fk and ue.active=1");
        query.append(" INNER JOIN fieldsense.users ue1 ON ue1.id=ue.report_to");
        query.append(" LEFT OUTER JOIN expense_categories ec ON ex.category_id_fk=ec.id WHERE");
        if(expfilter.getUserid().trim().equals("All")==false){
            expdetails.put("userid",expfilter.getUserid().trim());
            query.append(" ex.user_id_fk=? and ");
        }
        query.append(" (ex.expense_time BETWEEN ? AND ?) ");
        String searchText=allRequestParams.get("search[value]").trim();
        if(!searchText.equals("")){
            query.append(" AND (ue.full_name like ? OR ex.expense_name like ? OR ex.exp_category_name like ? OR ex.amount_spent like ? OR ex.expense_time like ?  OR c.customer_name like ?) ");
        }
        String sortcolindex=allRequestParams.get("order[0][column]");
        if(sortcolindex==null){
            query.append(" ORDER BY ex.expense_time desc");
        }else{
            switch(Integer.parseInt(sortcolindex)){
                case 1:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append("ORDER BY ue.full_name");
                    }else{
                        query.append("ORDER BY ue.full_name DESC");
                    }
                    break;
                case 2:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append("ORDER BY e.expense_name");
                    }else{
                        query.append("ORDER BY e.expense_name DESC");
                    }
                    break;
                case 3:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append("ORDER BY e.exp_category_name");
                    }else{
                        query.append("ORDER BY e.exp_category_name DESC");
                    }
                    break;
                case 4:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append("ORDER BY e.amount_spent");
                    }else{
                        query.append("ORDER BY e.amount_spent DESC");
                    }
                    break;    
                case 5:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append("ORDER BY e.expense_time");
                    }else{
                        query.append("ORDER BY e.expense_time DESC");
                    }
                    break;
                case 6:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append("ORDER BY c.customer_name");
                    }else{
                        query.append("ORDER BY c.customer_name DESC");
                    }
                    break;    
                default:
                   query.append(" ORDER BY e.expense_time desc");
                   break;
            }        
        }
        log4jLog.info(" selectExpenseReport " + query);
        expdetails.put("fromdate",expfilter.getFromdate().trim());
        expdetails.put("todate",expfilter.getTodate().trim());
        log4jLog.info(" selectExpenseReport " + query);
        final int start=Integer.parseInt(allRequestParams.get("start"));
        final int length=Integer.parseInt(allRequestParams.get("length"));
        Object param[] = new Object[20];
        int index=0;
        if(!searchText.equals("")){
            param = new Object[expdetails.size()+6];
        }else{
            param = new Object[expdetails.size()];
        }
        for(Map.Entry<String,String> expensedetails:expdetails.entrySet()){
          
           if(expensedetails.getKey().equals("status") || expensedetails.getKey().equals("expensecategory") || expensedetails.getKey().equals("userid")){
               param[index]=Integer.valueOf(expensedetails.getValue());
            }else{
               param[index]=expensedetails.getValue();
           }  
             index++;
        }
        if(!searchText.equals("")){
            for(int i=index;i<param.length;i++){
                 param[i] = "%"+searchText+"%";
            }
        }
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Expense>() {

                @Override
                public Expense mapRow(ResultSet rs, int i) throws SQLException {
                    if((i>=start && i<start+length) || length==-1){
                        Expense expense = new Expense();
                        expense.setId(rs.getInt("id"));
                        expense.setAppointmentId(rs.getInt("appointment_id_fk"));
                        expense.setCustomerId(rs.getInt("customer_id_fk"));
                        User user1 = new User();
                        int user_id=rs.getInt("ue.id");
                        user1.setId(user_id);
                        user1.setAccountId(accountId);
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
                        expense.setExpenseImageCsv(rs.getString("e.expense_image_id_csv"));
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
                    }else{
                            Expense expense = new Expense();
                            expense.setAmountSpent(rs.getDouble("amount_spent"));
                            expense.setExpenseImageCsv(rs.getString("e.expense_image_id_csv"));
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
     * @Added by jyoti
     * @param subordinateList
     * @param allRequestParams
     * @param expfilter
     * @param accountId
     * @return 
     */
    @Override
    public List<Expense> selectUserExpenseDetails(List<com.qlc.fieldsense.team.model.TeamMember> subordinateList, @RequestParam Map<String, String> allRequestParams, ExpenseFilter expfilter, final int accountId) {

        Map<String, String> expdetails = new LinkedHashMap<String, String>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT e.id,e.appointment_id_fk,e.customer_id_fk,e.expense_name,e.expense_image_id_csv,e.Expense_type_id_fk,e.desription,e.amount_spent,e.expense_time,e.submitted_on,e.created_on,e.status,e.approved_rejected_on,e.rejected_reason,IFNULL (e.disburse_amount,0) disburse_amount,IFNULL (e.payment_mode,'') payment_mode,IFNULL (e.default_date,'1111-11-11 11:11:11') default_date,IFNULL (e.exp_category_name,'') exp_category_name,");
//        query.append("c.customer_printas,c.customer_name,a.appointment_title,u.id,IFNULL (u.first_name,'') first_name,IFNULL (u.last_name,'') last_name,IFNULL (u.full_name,'') full_name,ue.id,ue.report_to,ue.first_name,ue.last_name,ue.full_name,ec.id,IFNULL(ue1.full_name,'') reporting_head,ec.category_name FROM expenses e"); // commented by jyoti
        query.append("IFNULL(c.customer_printas,'') AS customer_printas, IFNULL(c.customer_name,'') AS customer_name, IFNULL(c.customer_location_identifier,'') AS customer_location_identifier, IFNULL(a.appointment_title,'') AS appointment_title, IFNULL(a.appointment_time, '1111-11-11 11:11:11') AS appointment_time, IFNULL(a.appointment_end_time,'1111-11-11 11:11:11') AS appointment_end_time, IFNULL(a.status,0) AS appt_status, IFNULL(p.purpose,'') AS p_purpose, IFNULL(u.id,0) AS u_id,IFNULL (u.first_name,'') first_name,IFNULL (u.last_name,'') last_name,IFNULL (u.full_name,'') full_name,ue.id,ue.report_to,ue.first_name,ue.last_name,ue.full_name,ec.id,IFNULL(ue1.full_name,'') reporting_head,ec.category_name FROM expenses e"); // extra fields added to display appointment purpose
        query.append(" LEFT JOIN customers c ON e.customer_id_fk = c.id");
        query.append(" INNER JOIN expenses ex on e.id=ex.id ");
        if (expfilter.getStatus().trim().equals("All") == false) {
            expdetails.put("status", expfilter.getStatus().trim());
            query.append("  and ex.status=?");
        }
        if (expfilter.getExpensecategory().trim().equals("All") == false) {
            expdetails.put("expensecategory", expfilter.getExpensecategory().trim());
            query.append("  and ex.category_id_fk=?");
        }
        query.append(" LEFT JOIN appointments a ON a.id=e.appointment_id_fk");
        query.append(" LEFT JOIN activity_purpose p ON a.purpose_id_fk = p.id"); // added to display appointment purpose if title is blank
        query.append(" LEFT OUTER JOIN fieldsense.users u ON u.id=ex.approved_rejected_by_id_fk");
        query.append(" INNER JOIN fieldsense.users ue ON ue.id=ex.user_id_fk and ue.active=1");
        query.append(" INNER JOIN fieldsense.users ue1 ON ue1.id=ue.report_to");
        query.append(" LEFT OUTER JOIN expense_categories ec ON ex.category_id_fk=ec.id WHERE");

        if (expfilter.getUserid().trim().equals("All") == false) {
            expdetails.put("userid", expfilter.getUserid().trim());
            query.append(" ex.user_id_fk=? and ");
        } else if (expfilter.getUserid().trim().equals("All") == true) {
            ArrayList<Integer> subuserIdList = new ArrayList<Integer>();
            for (com.qlc.fieldsense.team.model.TeamMember teamMember : subordinateList) { // user type replaced with TeamMember
                subuserIdList.add(teamMember.getUser().getId()); // getid changed to getuser.getid
            }
            query.append(" ex.user_id_fk IN ( ");
            for (int k = 0; k < subuserIdList.size(); k++) {
                query.append(subuserIdList.get(k));
                if (k != (subuserIdList.size() - 1)) {
                    query.append(",");
                }
            }
            query.append(" ) AND ");
        }

        query.append(" (ex.expense_time BETWEEN ? AND ?) ");
        String searchText = allRequestParams.get("search[value]").trim();
        if (!searchText.equals("")) {
            query.append(" AND (ue.full_name like ? OR ex.expense_name like ? OR ex.exp_category_name like ? OR ex.amount_spent like ? OR ex.expense_time like ?  OR c.customer_name like ?) ");
        }
        String sortcolindex = allRequestParams.get("order[0][column]");
        if (sortcolindex == null) {
            query.append(" ORDER BY ex.expense_time desc");
        } else {
            switch (Integer.parseInt(sortcolindex)) {
                case 1:
                    if (allRequestParams.get("order[0][dir]").equals("asc")) {
                        query.append("ORDER BY ue.full_name");
                    } else {
                        query.append("ORDER BY ue.full_name DESC");
                    }
                    break;
                case 2:
                    if (allRequestParams.get("order[0][dir]").equals("asc")) {
                        query.append("ORDER BY e.expense_name");
                    } else {
                        query.append("ORDER BY e.expense_name DESC");
                    }
                    break;
                case 3:
                    if (allRequestParams.get("order[0][dir]").equals("asc")) {
                        query.append("ORDER BY e.exp_category_name");
                    } else {
                        query.append("ORDER BY e.exp_category_name DESC");
                    }
                    break;
                case 4:
                    if (allRequestParams.get("order[0][dir]").equals("asc")) {
                        query.append("ORDER BY e.amount_spent");
                    } else {
                        query.append("ORDER BY e.amount_spent DESC");
                    }
                    break;
                case 5:
                    if (allRequestParams.get("order[0][dir]").equals("asc")) {
                        query.append("ORDER BY e.expense_time");
                    } else {
                        query.append("ORDER BY e.expense_time DESC");
                    }
                    break;
                case 6:
                    if (allRequestParams.get("order[0][dir]").equals("asc")) {
                        query.append("ORDER BY c.customer_name");
                    } else {
                        query.append("ORDER BY c.customer_name DESC");
                    }
                    break;
                default:
                    query.append(" ORDER BY e.expense_time desc");
                    break;
            }
        }
        
        expdetails.put("fromdate", expfilter.getFromdate().trim());
        expdetails.put("todate", expfilter.getTodate().trim());
//        log4jLog.info(" selectUserExpenseDetails " + query);
        final int start = Integer.parseInt(allRequestParams.get("start"));
        final int length = Integer.parseInt(allRequestParams.get("length"));
        Object param[] = new Object[20];
        int index = 0;
        if (!searchText.equals("")) {
            param = new Object[expdetails.size() + 6];
        } else {
            param = new Object[expdetails.size()];
        }
        for (Map.Entry<String, String> expensedetails : expdetails.entrySet()) {

            if (expensedetails.getKey().equals("status") || expensedetails.getKey().equals("expensecategory") || expensedetails.getKey().equals("userid")) {
                param[index] = Integer.valueOf(expensedetails.getValue());
            } else {
                param[index] = expensedetails.getValue();
            }
            index++;
        }
        if (!searchText.equals("")) {
            for (int i = index; i < param.length; i++) {
                param[i] = "%" + searchText + "%";
            }
        }
//        System.out.println("query for expense user details : "+query.toString());
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Expense>() {

                @Override
                public Expense mapRow(ResultSet rs, int i) throws SQLException {
                    if ((i >= start && i < start + length) || length == -1) {
                        Expense expense = new Expense();
                        expense.setId(rs.getInt("id"));
                        expense.setAppointmentId(rs.getInt("appointment_id_fk"));
                        expense.setCustomerId(rs.getInt("customer_id_fk"));
                        User user1 = new User();
                        int user_id = rs.getInt("ue.id");
                        user1.setId(user_id);
                        user1.setFirstName(rs.getString("ue.first_name"));
                        user1.setLastName(rs.getString("ue.last_name"));
                        user1.setFullname(rs.getString("ue.full_name"));
                        expense.setUser(user1);
                        expense.setExpenseName(rs.getString("expense_name"));
                        expense.setExpenseImageCsv(rs.getString("expense_image_id_csv"));
                        expense.setExpenseType(rs.getInt("Expense_type_id_fk"));
                        expense.setDescription(rs.getString("desription"));
                        expense.setAmountSpent(rs.getDouble("amount_spent"));
                        expense.setStatus(rs.getInt("status"));
                        expense.setExpenseTime(rs.getTimestamp("expense_time"));
                        expense.setSubmittedOn(rs.getTimestamp("submitted_on"));
                        expense.setApprovedOrRejectedOn(rs.getTimestamp("approved_rejected_on"));
                        User user = new User();
                        user.setId(rs.getInt("u_id"));
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
                        // added by jyoti
                        appointment.setDateTime(rs.getTimestamp("appointment_time"));
                        appointment.setSdateTime(appointment.getDateTime().toString());
                        appointment.setEndTime(rs.getTimestamp("appointment_end_time"));
                        appointment.setSendTime(appointment.getEndTime().toString());
                        appointment.setStatus(rs.getInt("appt_status"));
                        ActivityPurpose purpose = new ActivityPurpose();
                        purpose.setPurpose(rs.getString("p_purpose"));
                        appointment.setPurpose(purpose);
                        Customer customer = new Customer();
                        customer.setCustomerLocation(rs.getString("customer_location_identifier"));
                        appointment.setCustomer(customer);
                        // ended by jyoti
                        expense.setAppointment(appointment);
                        User reporting_head = new User();
                        reporting_head.setFullname(rs.getString("reporting_head"));
                        expense.setReport_head(reporting_head);
                        return expense;
                    } else {
                        Expense expense = new Expense();
                        expense.setAmountSpent(rs.getDouble("amount_spent"));
                        expense.setExpenseTime(Timestamp.valueOf("1111-11-11 11:11:11"));
                        return expense;
                    }
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectUserExpenseDetails " + e);
            e.printStackTrace();
            return new ArrayList<Expense>();
        }
    }
    
    /**
     * added by manohar
     * @param expfilter
     * @param accountId
     * @return 
     */
    public List<Expense> selectAccountExpenseDetilsCSV(ExpenseFilter expfilter, int accountId) {
        Map<String,String> expdetails=new LinkedHashMap<String,String>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT e.id,e.appointment_id_fk,e.customer_id_fk,e.expense_name,e.Expense_type_id_fk,e.desription,e.amount_spent,e.expense_time,e.submitted_on,e.created_on,e.status,e.approved_rejected_on,e.rejected_reason,IFNULL (e.disburse_amount,0) disburse_amount,IFNULL (e.payment_mode,'') payment_mode,IFNULL (e.default_date,'1111-11-11 11:11:11') default_date,IFNULL (e.exp_category_name,'') exp_category_name,");
        query.append("c.customer_printas,c.customer_name,a.appointment_title,u.id,IFNULL (u.first_name,'') first_name,IFNULL (u.last_name,'') last_name,IFNULL (u.full_name,'') full_name,ue.id,ue.report_to,ue.first_name,ue.last_name,ue.full_name,ec.id,IFNULL(ue1.full_name,'') reporting_head,ec.category_name FROM expenses e");
        query.append(" LEFT JOIN customers c ON e.customer_id_fk = c.id");
        query.append(" INNER JOIN expenses ex on e.id=ex.id ");
        if(expfilter.getStatus().trim().equals("All")==false){
            expdetails.put("status",expfilter.getStatus().trim());
            query.append("  and ex.status=?");
        }
        if(expfilter.getExpensecategory().trim().equals("All")==false){
            expdetails.put("expensecategory",expfilter.getExpensecategory().trim());
            query.append("  and ex.category_id_fk=?");
        }
        query.append(" LEFT JOIN appointments a ON a.id=e.appointment_id_fk");
        query.append(" LEFT OUTER JOIN fieldsense.users u ON u.id=ex.approved_rejected_by_id_fk");
        query.append(" INNER JOIN fieldsense.users ue ON ue.id=ex.user_id_fk and ue.active=1");
        query.append(" INNER JOIN fieldsense.users ue1 ON ue1.id=ue.report_to");
        query.append(" LEFT OUTER JOIN expense_categories ec ON ex.category_id_fk=ec.id WHERE");
        if(expfilter.getUserid().trim().equals("All")==false){
            expdetails.put("userid",expfilter.getUserid().trim());
            query.append(" ex.user_id_fk=? and ");
        }
        query.append(" (ex.expense_time BETWEEN ? AND ?) order by ex.expense_time DESC");
        log4jLog.info(" selectAccountExpenseDetilsCSV " + query);
        expdetails.put("fromdate",expfilter.getFromdate().trim());
        expdetails.put("todate",expfilter.getTodate().trim());
        log4jLog.info(" selectAccountExpenseDetilsCSV " + query);            
        Object param[] = new Object[20];
        int index=0;
        param = new Object[expdetails.size()];
        for(Map.Entry<String,String> expensedetails:expdetails.entrySet()){       
           if(expensedetails.getKey().equals("status") || expensedetails.getKey().equals("expensecategory") || expensedetails.getKey().equals("userid")){
               param[index]=Integer.valueOf(expensedetails.getValue());
            }else{
               param[index]=expensedetails.getValue();
           }  
             index++;
        }
//        System.out.println("query="+query);
        try{
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Expense>() {
                @Override
                public Expense mapRow(ResultSet rs, int i) throws SQLException {
                  
                        Expense expense = new Expense();
                        User user = new User();
                        int user_id=rs.getInt("ue.id");
                        user.setId(user_id);
                        user.setFirstName(rs.getString("ue.first_name"));
                        user.setLastName(rs.getString("ue.last_name"));
                        user.setFullname(rs.getString("ue.full_name"));
                        expense.setUser(user);
                        expense.setExpenseName(rs.getString("expense_name"));
                        expense.setAmountSpent(rs.getDouble("amount_spent"));
                        expense.setStatus(rs.getInt("status"));
                        expense.setExpenseTime(rs.getTimestamp("expense_time"));
                        expense.setExp_category_name(rs.getString("exp_category_name"));
                        expense.setCustomerName(rs.getString("customer_name"));
                        return expense;   
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAccountExpenseDetilsCSV " + e);
            e.printStackTrace();
            return new ArrayList<Expense>();
        }
    }
    
    public  String encodeToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
 
        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();
            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }
}
