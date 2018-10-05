/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.expense.dao;

import com.qlc.fieldsense.expense.model.Expense;
import com.qlc.fieldsense.expense.model.ExpenseImage;
import com.qlc.fieldsense.expense.model.ExpenseFilter;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author mayank
 */
public interface ExpenseDao {

    /**
     *
     * @param expense
     * @param accountId
     * @return
     */
    int insertExpense(Expense expense, int accountId);

    /**
     *
     * @param userId
     * @param startDate
     * @param endDate
     * @param accountId
     * @return
     */
    List<Expense> selectUserExpense(int userId, String startDate, String endDate, int accountId);

    /**
     *
     * @param userId
     * @param customerId
     * @param accountId
     * @return
     */
    List<Expense> selectUserCustomerExpense(int userId, int customerId, int accountId);

    /**
     *
     * @param customerId
     * @param accountId
     * @return
     */
    List<Expense> selectCustomerExpense(int customerId, int accountId);

    /**
     *
     * @param expenseType
     * @param accountId
     * @return
     */
    List<Expense> selectSpecificTypeExpense(int expenseType, int accountId);
    
    /**
     *
     * @param allRequestParams
     * @param expfilter
     * @param accountId
     * @return
     */
    List<Expense> selectAccountExpenseDetils(@RequestParam Map<String,String> allRequestParams,ExpenseFilter expfilter,int accountId);
    /**
     * added by manohar
     * @param expfilter
     * @param accountId
     * @return 
     */
    List<Expense> selectAccountExpenseDetilsCSV(ExpenseFilter expfilter,int accountId);  //added by manohar
    /**
     * @Added by jyoti
     * @param subordinateList
     * @param allRequestParams
     * @param expfilter
     * @param accountId
     * @return 
     */
    List<Expense> selectUserExpenseDetails(List<com.qlc.fieldsense.team.model.TeamMember> subordinateList, @RequestParam Map<String,String> allRequestParams,ExpenseFilter expfilter,int accountId);
    
    /**
     *
     * @param expense
     * @param accountId
     * @return
     */
    boolean updateExpense(Expense expense, int accountId);

    /**
     *
     * @param expenseId
     * @param accountId
     * @return
     */
    boolean deleteExpense(int expenseId, int accountId);

    /**
     *
     * @param expenseImage
     * @param accountId
     * @return
     */
    int insertImage(ExpenseImage expenseImage, int accountId);

    /**
     *
     * @param Id
     * @param accountId
     * @return
     */
    ExpenseImage selectImage(int Id, int accountId);

    /**
     *
     * @param expenseId
     * @param accountId
     * @return
     */
    ExpenseImage selectExpenseImage(int expenseId, int accountId);

    /**
     *
     * @param expenseImage
     * @param accountId
     * @return
     */
    boolean updateImage(ExpenseImage expenseImage, int accountId);

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    boolean deleteImage(int id, int accountId);

    /**
     *
     * @param expenseId
     * @param accountId
     * @return
     */
    public Expense selectExpense(int expenseId, int accountId);
    
    /**
     *
     * @param expenseId
     * @param accountId
     * @return
     */
    public List<Expense> selectExpenseAudit(int expenseId, int accountId);

    /**
     *
     * @param expenseId
     * @param accountId
     * @return
     */
    public boolean isExpenseValid(int expenseId, int accountId);

    /**
     *
     * @param imageId
     * @param accountId
     * @return
     */
    public boolean isImageValid(int imageId, int accountId);

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public List<Expense> selectUserExpense(int userId, int accountId);

    /**
     *
     * @param expense
     * @param accountId
     * @return
     */
    public boolean approveExpense(Expense expense, int accountId);

    /**
     *
     * @param expense
     * @param accountId
     * @param fieldsenseutils
     * @return
     */
    public boolean approveExpenseByAccount(Expense expense, int accountId,FieldSenseUtils fieldsenseutils);
     
    /**
     *
     * @param expense
     * @param accountId
     * @param fieldsenseutils
     * @return
     */
    public boolean approveMultiExpenseByAccount(Expense[] expense, int accountId,FieldSenseUtils fieldsenseutils);
   
    /**
     *
     * @param expense
     * @param accountId
     * @return
     */
    public boolean rejectExpense(Expense expense, int accountId);
    
    /**
     *
     * @param expense
     * @param accountId
     * @param fieldsenseutils
     * @return
     */
    public boolean rejectExpenseByAccount(Expense expense,int accountId,FieldSenseUtils fieldsenseutils);

    /**
     *
     * @param expense
     * @param accountId
     * @param fieldsenseutils
     * @return
     */
    public boolean rejectMultiExpenseByAccount(Expense[] expense, int accountId,FieldSenseUtils fieldsenseutils);

    /**
     *
     * @param expense
     * @param status
     * @param accountId
     * @param fieldsenseutils
     * @return
     */
    public boolean disburseExpenseByAccount(Expense expense,int status, int accountId,FieldSenseUtils fieldsenseutils);
    
    /**
     *
     * @param expense
     * @param status
     * @param accountId
     * @param fieldsenseutils
     * @return
     */
    public boolean disburseExpenseDefaultByAccount(Expense expense,int status, int accountId,FieldSenseUtils fieldsenseutils);
          
    /**
     *
     * @param userId
     * @param approverId
     * @param accountId
     * @return
     */
    public boolean approveUserAllExpenses(int userId, int approverId, int accountId);

    /**
     *
     * @param userId
     * @param startDate
     * @param endDate
     * @param expenseStatus
     * @param accountId
     * @return
     */
    public List<Expense> selectUserExpense(int userId, String startDate, String endDate, int expenseStatus, int accountId);
}
