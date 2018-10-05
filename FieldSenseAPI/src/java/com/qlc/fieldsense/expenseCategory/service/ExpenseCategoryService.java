/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.expenseCategory.service;

import com.qlc.fieldsense.expenseCategory.model.ExpenseCategory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.qlc.fieldsense.expenseCategory.service.ExpenseCategoryManager;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 *
 * @author anuja
 */
@Controller
@RequestMapping("/expenseCategory")
public class ExpenseCategoryService {

    ExpenseCategoryManager expenseCategoryManager = new ExpenseCategoryManager();

    /**
     * 
     * @param eCategory
     * @param userToken
     * @return 
     * @purpose used to create expense category
     */
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody
    Object createExpenseCategory(@RequestBody ExpenseCategory eCategory, @RequestHeader(value = "userToken") String userToken) {
        return expenseCategoryManager.createExpenseCategory(eCategory, userToken);
    }

    /**
     * 
     * @param eCategory
     * @param userToken
     * @return 
     * @purpose used to update expense category 
     */
    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public @ResponseBody
    Object updateExpenseCategory(@RequestBody ExpenseCategory eCategory, @RequestHeader(value = "userToken") String userToken) {
        return expenseCategoryManager.updateExpenseCategory(eCategory, userToken);
    }

    /**
     * 
     * @param id
     * @param userToken
     * @return details of expense category base on expense category id 
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public @ResponseBody
    Object getOneExpenseCategory(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return expenseCategoryManager.getOneExpenseCategory(id, userToken);
    }

    /**
     * 
     * @param id
     * @param userToken
     * @return 
     * @purpose delete expense category with category id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object deleteExpenseCategory(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return expenseCategoryManager.deleteExpenseCategory(id, userToken);
    }
    
    /**
     * 
     * @param allRequestParams
     * @param userToken
     * @param response
     * @return all expense category details with offset 
     */
    @RequestMapping(value = "/offset/", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getAllExpenseCategoryWithOffset(@RequestParam Map<String,String> allRequestParams, @RequestHeader(value = "userToken") String userToken,HttpServletResponse response) {
        return expenseCategoryManager.getAllExpenseCategoryWithOffset(allRequestParams, userToken,response);
    }
    
    /**
     * 
     * @param userToken
     * @return all active expense categories
     */
    @RequestMapping(value = "/active", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getAllActiveExpenseCategory(@RequestHeader(value = "userToken") String userToken) {
        return expenseCategoryManager.getAllActiveExpenseCategory(userToken);
    }

    /**
     * 
     * @param request
     * @param userToken
     * @author: Jyoti, 27-02-2017
     * @return 
     * @purpose:import expense category
     */
    @RequestMapping(value = "/importExpenseCategory", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object importExpenseCategory(MultipartHttpServletRequest request, @RequestHeader(value = "userToken") String userToken){
        Iterator<String> itr = request.getFileNames();
        MultipartFile file = null;
        while (itr.hasNext()) {
            file = request.getFile(itr.next());
            return expenseCategoryManager.insertExpenseCategoryByImport(file, userToken);
        }
        return null;
    }
}
