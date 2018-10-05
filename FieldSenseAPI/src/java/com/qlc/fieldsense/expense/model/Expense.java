/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.expense.model;

import com.qlc.fieldsense.appointments.model.Appointment;
import com.qlc.fieldsense.expenseCategory.model.ExpenseCategory;
import com.qlc.fieldsense.user.model.User;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author mayank
 */
public class Expense {

    private int id;
    private int appointmentId;
    private int customerId;
    private User user;
    private String expenseName;
    private int expenseType;
    private String description;
    private double amountSpent;
    private int status;
    private String strexpenseTime;
    private Timestamp expenseTime;
    private Timestamp submittedOn;
    private Timestamp approvedOrRejectedOn;
    private User approvedOrRjectedBy;
    private String rejectedReson;
    private ExpenseImage expenseImage;
    private ArrayList<ExpenseImage> expenseImageArray;
    private String expenseImageCsv;
    private Timestamp createdOn;
    private String customerName;
    private Appointment appointment;
    private ExpenseCategory eCategoryName;
    private String expenseTempId;
    private String appointmentTempId;
    private String customerTempId;
    private double disburse_amount;
    private String payment_mode;
    private Timestamp default_date;
    private String exp_category_name;
    private User report_head;
    private String imageBase64;
    private ArrayList status_all_percnt=new  ArrayList();   
    private ArrayList cat_all_percnt=new  ArrayList();
    private double total_amounts;

    /**
     *
     */
    public Expense() {
        this.id = 0;
        this.appointmentId = 0;
        this.customerId = 0;
        this.user = new User();
        this.expenseName = "";
        this.expenseType = 0;
        this.description = "";
        this.amountSpent = 0.0;
        this.status = 0;
        this.expenseTempId="";
        this.appointmentTempId="";
        this.customerTempId="";
        this.strexpenseTime = "";
        this.expenseTime = new Timestamp(0);
        this.submittedOn = new Timestamp(0);
        this.approvedOrRejectedOn = new Timestamp(0);
        this.approvedOrRjectedBy = new User();
        this.rejectedReson = "";
        this.expenseImage = new ExpenseImage();
        this.createdOn = new Timestamp(0);
        this.customerName = "";
        this.appointment = new Appointment();
        this.eCategoryName= new ExpenseCategory();
        this.disburse_amount=0.0;
        this.payment_mode="";
        this.exp_category_name="";
        this.default_date=new Timestamp(0);
        this.report_head=new User();
        this.status_all_percnt=new  ArrayList(); 
        this.cat_all_percnt=new  ArrayList();
        this.total_amounts=0.0;
        this.imageBase64 = "";
        this.expenseImageArray = new ArrayList<>();
        this.expenseImageCsv="";
    }

    /**
     *
     * @return
     */
    public double getAmountSpent() {
        return amountSpent;
    }

    /**
     *
     * @param amountSpent
     */
    public void setAmountSpent(double amountSpent) {
        this.amountSpent = amountSpent;
    }

    /**
     *
     * @return
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     *
     * @param appointmentId
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     *
     * @return
     */
    public Timestamp getApprovedOrRejectedOn() {
        return approvedOrRejectedOn;
    }

    /**
     *
     * @param approvedOrRejectedOn
     */
    public void setApprovedOrRejectedOn(Timestamp approvedOrRejectedOn) {
        this.approvedOrRejectedOn = approvedOrRejectedOn;
    }

    /**
     *
     * @return
     */
    public User getApprovedOrRjectedBy() {
        return approvedOrRjectedBy;
    }

    /**
     *
     * @param approvedOrRjectedBy
     */
    public void setApprovedOrRjectedBy(User approvedOrRjectedBy) {
        this.approvedOrRjectedBy = approvedOrRjectedBy;
    }

    /**
     *
     * @return
     */
    public Timestamp getCreatedOn() {
        return createdOn;
    }

    /**
     *
     * @param createdOn
     */
    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    /**
     *
     * @return
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     *
     * @param customerId
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     */
    public ExpenseImage getExpenseImage() {
        return expenseImage;
    }

    /**
     *
     * @param expenseImage
     */
    public void setExpenseImage(ExpenseImage expenseImage) {
        this.expenseImage = expenseImage;
    }

    /**
     *
     * @return
     */
    public String getExpenseName() {
        return expenseName;
    }

    /**
     *
     * @param expenseName
     */
    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    /**
     *
     * @return
     */
    public Timestamp getExpenseTime() {
        return expenseTime;
    }

    /**
     *
     * @param expenseTime
     */
    public void setExpenseTime(Timestamp expenseTime) {
        this.expenseTime = expenseTime;
    }

    /**
     *
     * @return
     */
    public String getStrexpenseTime() {
        return strexpenseTime;
    }

    /**
     *
     * @param strexpenseTime
     */
    public void setStrexpenseTime(String strexpenseTime) {
        this.strexpenseTime = strexpenseTime;
    }

    /**
     *
     * @return
     */
    public int getExpenseType() {
        return expenseType;
    }

    /**
     *
     * @param expenseType
     */
    public void setExpenseType(int expenseType) {
        this.expenseType = expenseType;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getRejectedReson() {
        return rejectedReson;
    }

    /**
     *
     * @param rejectedReson
     */
    public void setRejectedReson(String rejectedReson) {
        this.rejectedReson = rejectedReson;
    }

    /**
     *
     * @return
     */
    public int getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     *
     * @return
     */
    public Timestamp getSubmittedOn() {
        return submittedOn;
    }

    /**
     *
     * @param submittedOn
     */
    public void setSubmittedOn(Timestamp submittedOn) {
        this.submittedOn = submittedOn;
    }

    /**
     *
     * @return
     */
    public User getUser() {
        return user;
    }

    /**
     *
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     *
     * @return
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     *
     * @param customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     *
     * @return
     */
    public Appointment getAppointment() {
        return appointment;
    }

    /**
     *
     * @param appointment
     */
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    /**
     *
     * @return
     */
    public ExpenseCategory geteCategoryName() {
        return eCategoryName;
    }

    /**
     *
     * @param eCategoryName
     */
    public void seteCategoryName(ExpenseCategory eCategoryName) {
        this.eCategoryName = eCategoryName;
    }
    
    /**
     *
     * @return
     */
    public double getDisburse_amount() {
        return disburse_amount;
    }

    /**
     *
     * @param disburse_amount
     */
    public void setDisburse_amount(double disburse_amount) {
        this.disburse_amount = disburse_amount;
    }

    /**
     *
     * @return
     */
    public String getPayment_mode() {
        return payment_mode;
    }

    /**
     *
     * @param payment_mode
     */
    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    /**
     *
     * @return
     */
    public Timestamp getDefault_date() {
        return default_date;
    }

    /**
     *
     * @param default_date
     */
    public void setDefault_date(Timestamp default_date) {
        this.default_date = default_date;
    }
    
    /**
     *
     * @return
     */
    public User getReport_head() {
        return report_head;
    }

    /**
     *
     * @param report_head
     */
    public void setReport_head(User report_head) {
        this.report_head = report_head;
    }
    
    /**
     *
     * @return
     */
    public String getExp_category_name() {
        return exp_category_name;
    }

    /**
     *
     * @param exp_category_name
     */
    public void setExp_category_name(String exp_category_name) {
        this.exp_category_name = exp_category_name;
    }
    
    /**
     *
     * @return
     */
    public ArrayList getStatus_all_percnt() {
        return status_all_percnt;
    }

    /**
     *
     * @param status_all_percnt
     */
    public void setStatus_all_percnt(ArrayList status_all_percnt) {
        this.status_all_percnt = status_all_percnt;
    }

    /**
     *
     * @return
     */
    public ArrayList getCat_all_percnt() {
        return cat_all_percnt;
    }

    /**
     *
     * @param cat_all_percnt
     */
    public void setCat_all_percnt(ArrayList cat_all_percnt) {
        this.cat_all_percnt = cat_all_percnt;
    }
    
    /**
     *
     * @return
     */
    public double getTotal_amounts() {
        return total_amounts;
    }

    /**
     *
     * @param total_amounts
     */
    public void setTotal_amounts(double total_amounts) {
        this.total_amounts = total_amounts;
    }

    /**
     *
     * @return
     */
    public String getExpenseTempId() {
        return expenseTempId;
    }

    /**
     *
     * @param expenseTempId
     */
    public void setExpenseTempId(String expenseTempId) {
        this.expenseTempId = expenseTempId;
    }

    /**
     *
     * @return
     */
    public String getAppointmentTempId() {
        return appointmentTempId;
    }

    /**
     *
     * @param appointmentTempId
     */
    public void setAppointmentTempId(String appointmentTempId) {
        this.appointmentTempId = appointmentTempId;
    }

    /**
     *
     * @return
     */
    public String getCustomerTempId() {
        return customerTempId;
    }

    /**
     *
     * @param customerTempId
     */
    public void setCustomerTempId(String customerTempId) {
        this.customerTempId = customerTempId;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public ArrayList<ExpenseImage> getExpenseImageArray() {
        return expenseImageArray;
    }

    public void setExpenseImageArray(ArrayList<ExpenseImage> expenseImageArray) {
        this.expenseImageArray = expenseImageArray;
    }

    public String getExpenseImageCsv() {
        return expenseImageCsv;
    }

    public void setExpenseImageCsv(String expenseImageCsv) {
        this.expenseImageCsv = expenseImageCsv;
    }
    
}
