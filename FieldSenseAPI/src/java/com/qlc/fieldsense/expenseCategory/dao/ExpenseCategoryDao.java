/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.expenseCategory.dao;

import com.qlc.fieldsense.expenseCategory.model.ExpenseCategory;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author anuja
 */
public interface ExpenseCategoryDao {

    /**
     *
     * @param eCategory
     * @param accountId
     * @return
     */
    public int createExpenseCategory(ExpenseCategory eCategory, int accountId);

    /**
     *
     * @param allRequestParams
     * @param accountId
     * @return
     */
    public List<ExpenseCategory> selectAllExpenseCategoryWithOffset(@RequestParam Map<String,String> allRequestParams, int accountId);

    /**
     *
     * @param eCategoryId
     * @param accountId
     * @return
     */
    public ExpenseCategory selectExpenseCategory(int eCategoryId, int accountId);

    /**
     *
     * @param eCategory
     * @param accountId
     * @return
     */
    public ExpenseCategory updateExpenseCategory(ExpenseCategory eCategory, int accountId);

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    public boolean deleteExpenseCategory(int id, int accountId);

    /**
     *
     * @param categoryName
     * @param accountId
     * @return
     */
    public boolean isExpenseCategoryAlreadyExists(String categoryName, int accountId);

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    public boolean isExpenseCategoryValid(int id, int accountId);

    /**
     *
     * @param eCategoryId
     * @param accountId
     * @return
     */
    public String getExpenseCategory(int eCategoryId, int accountId);
    
    /**
     *
     * @param accountId
     * @return
     */
    public  List<ExpenseCategory>  selectAllActiveExpenseCategory(int accountId);
}
