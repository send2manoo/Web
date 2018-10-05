/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.expenseCategory.service;

import au.com.bytecode.opencsv.CSVWriter;
import com.qlc.fieldsense.expenseCategory.dao.ExpenseCategoryDao;
import com.qlc.fieldsense.expenseCategory.model.ExpenseCategory;
import com.qlc.fieldsense.expenseCategory.model.ExpenseCategoryCSV;
import com.qlc.fieldsense.expenseCategory.model.MapCSVToExpenseCategory;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import com.qlc.fieldsense.utils.RunnableThreadJob;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author anuja
 */
public class ExpenseCategoryManager {

    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
    ExpenseCategoryDao expenseCategoryDao = (ExpenseCategoryDao) GetApplicationContext.ac.getBean("expenseCategoryDaoImpl");

    /**
     * 
     * @param eCategory
     * @param userToken
     * @return 
     * @purpose used to create expense category
     */
    public Object createExpenseCategory(ExpenseCategory eCategory, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (!expenseCategoryDao.isExpenseCategoryAlreadyExists(eCategory.getCategoryName(), accountId)) {
                    int eCategoryId = expenseCategoryDao.createExpenseCategory(eCategory, accountId);
                    if (eCategoryId != 0) {
                        eCategory = expenseCategoryDao.selectExpenseCategory(eCategoryId, accountId);
                        // for push notification, Added by Jyoti
                        // Added by Jyoti, 04-01-2018
                        int expenseStatus = 0; // 0=new, 1=update, 2=delete
                        RunnableThreadJob theJob = new RunnableThreadJob();
                        theJob.sendAddEditExpenseCategoryNotificationToUsersOfAccount(expenseStatus, eCategory, accountId);
                        // Ended by jyoti, 08-01-2018
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Expense Category is created successfully.", "eCategory", eCategory);
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Expense Category is not created. Please try again . ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Expense Category already exists.", "", "");
                }
             // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param eCategory
     * @param userToken
     * @return 
     * @purpose used to update expense category 
     */
    public Object updateExpenseCategory(ExpenseCategory eCategory, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (expenseCategoryDao.isExpenseCategoryValid(eCategory.getId(), accountId)) {
                    String oldExpenseCategory = expenseCategoryDao.getExpenseCategory(eCategory.getId(), accountId);
                    if (eCategory.getCategoryName().equals(oldExpenseCategory)) {
                        eCategory = expenseCategoryDao.updateExpenseCategory(eCategory, accountId);
                        // for push notification, Added by Jyoti
                        // Added by Jyoti, 04-01-2018
                        int expenseStatus = 1; // 0=new, 1=update, 2=delete
                        RunnableThreadJob theJob = new RunnableThreadJob();
                        theJob.sendAddEditExpenseCategoryNotificationToUsersOfAccount(expenseStatus, eCategory, accountId);
                        // Ended by jyoti, 08-01-2018
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.EXPENSE_CATEGORY_UPDATED, " eCategory ", eCategory);
                    } else {
                        if (!expenseCategoryDao.isExpenseCategoryAlreadyExists(eCategory.getCategoryName(), accountId)) {
                            eCategory = expenseCategoryDao.updateExpenseCategory(eCategory, accountId);
                            // for push notification, Added by Jyoti
                            // Added by Jyoti, 04-01-2018
                            int expenseStatus = 1; // 0=new, 1=update, 2=delete
                            RunnableThreadJob theJob = new RunnableThreadJob();
                            theJob.sendAddEditExpenseCategoryNotificationToUsersOfAccount(expenseStatus, eCategory, accountId);
                            // Ended by jyoti, 08-01-2018
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.EXPENSE_CATEGORY_UPDATED, " eCategory ", eCategory);
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Expense Category already exists.", "", "");
                        }
                    }

                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.EXPENSE_CATEGORY_INVALID, "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param eCategoryId
     * @param id
     * @param userToken
     * @return details of expense category base on expense category id 
     */
    public Object getOneExpenseCategory(int eCategoryId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                ExpenseCategory eCategory = expenseCategoryDao.selectExpenseCategory(eCategoryId, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Expense Category.", "eCategory", eCategory);
           // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param eCategory
     * @param id
     * @param userToken
     * @return 
     * @purpose delete expense category with category id
     */
    public Object deleteExpenseCategory(int eCategory, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (expenseCategoryDao.isExpenseCategoryValid(eCategory, accountId)) {
                    if (expenseCategoryDao.deleteExpenseCategory(eCategory, accountId)) {
                        // for push notification, Added by Jyoti
                        // Added by Jyoti, 04-01-2018
                        ExpenseCategory eCategoryObj = new ExpenseCategory();
                        eCategoryObj.setId(eCategory);
                        int expenseStatus = 2; // 0=new, 1=update, 2=delete
                        RunnableThreadJob theJob = new RunnableThreadJob();
                        theJob.sendAddEditExpenseCategoryNotificationToUsersOfAccount(expenseStatus, eCategoryObj, accountId);
                        // Ended by jyoti, 08-01-2018
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.EXPENSE_CATEGORY_DELETE, "", "");
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.EXPENSE_CATEGORY_NOT_DELETE, "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.EXPENSE_CATEGORY_INVALID, "", "");
                }
           // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param allRequestParams
     * @param offset
     * @param response
     * @param userToken
     * @return all expense category details with offset 
     */
    public Object getAllExpenseCategoryWithOffset(@RequestParam Map<String,String> allRequestParams, String userToken,HttpServletResponse response) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<ExpenseCategory> expenseCategoryList = expenseCategoryDao.selectAllExpenseCategoryWithOffset(allRequestParams, accountId);
                int Total=0;
                if(!expenseCategoryList.isEmpty()){
                    Total=expenseCategoryList.get(0).getTotalexpenseCategory();
                }
                return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, "List of Expense Category.", "expenseCategoryList", expenseCategoryList,Total);
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            response.setContentType( MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }
    
    /**
     * 
     * @param userToken
     * @return all active expense categories
     */
    public Object getAllActiveExpenseCategory(String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List expenseCategoryList = expenseCategoryDao.selectAllActiveExpenseCategory(accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "List of Expense Category.", "expenseCategoryList", expenseCategoryList);
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }

    // Added by Jyoti, 27-02-2017

    /**
     *
     * @param file
     * @param userToken
     * @return
     */
        public Object insertExpenseCategoryByImport(MultipartFile file, String userToken) {
        
        try {
            String validStatus = Constant.VALID;
            List<Integer> expenseCategoryList = new ArrayList<Integer>();
            List<Integer> expenseCategoryTotalList = new ArrayList<Integer>();            
            List<String[]> expenseErrorList = new ArrayList<String[]>();
            if (fieldSenseUtils.isTokenValid(userToken)) {
                //if(util.isSessionExpired(userToken)){
                    int accountId = fieldSenseUtils.accountIdForToken(userToken);
                    int userId = fieldSenseUtils.userIdForToken(userToken);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss_SSS");
                    java.util.Date d = new java.util.Date();
                    String date = dateFormat.format(d);
                    String saveFileTo = Constant.IMPORT_ERROR_FILES;
                    String errorFileNm = accountId + "_" + date + "_ExpenseCategoryImportError.csv";
                    String csv = saveFileTo + errorFileNm;
                    List<ExpenseCategoryCSV> list = MapCSVToExpenseCategory.mapJavaBean(file);
                    for (int i = 0; i < 1; i++) {                                      
                        if (((list.get(i).getCategoryName().equalsIgnoreCase("Category Name")) && (list.get(i).getIsActive().equalsIgnoreCase("Active")))) {           
                        } else {            
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "CSV file not in correct format. Please download the sample file.", "", "");
                        }
                    }
                    expenseErrorList.add(new String[]{"categoryName", "isActive"});
                    for (int i = 1; i < list.size(); i++) {
                        ExpenseCategory expenseCategory = new ExpenseCategory();
                        User user = new User();
                        user.setId(userId);
                        String validStatus2 = Constant.VALID;
                        
                        if (list.get(i).getCategoryName().equals(""))  {
                            validStatus = FieldSenseUtils.notvalid(validStatus);
                            validStatus = validStatus + "Category name should not be blank at line:" + (i + 1) + ".";
                            validStatus2 = validStatus;
                        }
                        
                        if (list.get(i).getCategoryName().trim().length() >  100)  {
                            validStatus = FieldSenseUtils.notvalid(validStatus);
                            validStatus = validStatus + "Category name cannot be more than 100 characters at line:" + (i + 1) + ".";
                            validStatus2 = validStatus;
                        }
                        
                        if (!(expenseCategoryDao.isExpenseCategoryAlreadyExists(list.get(i).getCategoryName(), accountId))) {
                                    
                                        if ((list.get(i).getIsActive().equals("")) || (list.get(i).getIsActive().equals(null))) {
                                        } else if (list.get(i).getIsActive().trim().equalsIgnoreCase("yes")) {
                                            expenseCategory.setIsActive(true);
                                        } else if (list.get(i).getIsActive().equalsIgnoreCase("no")) {
                                            expenseCategory.setIsActive(false);
                                        } else {
                                            validStatus = FieldSenseUtils.notvalid(validStatus);
                                            validStatus = validStatus + " Invalid active field at line:" + (i + 1) + ".";
                                            validStatus2 = validStatus;
                                        }
                        }else {
                            validStatus = validStatus + " Category Name at line " + (i + 1) + " already present.";
                        } 

                        if (!(validStatus2.equals(Constant.VALID))) {
                            expenseErrorList.add(new String[]{list.get(i).getCategoryName(), list.get(i).getIsActive()});
                            continue;
                        }
                        if (validStatus2.equals(Constant.VALID)) {
                            expenseCategory.setCategoryName(list.get(i).getCategoryName());
                            expenseCategory.setCreatedBy(user);                                
                            int expenseCategoryID = expenseCategoryDao.createExpenseCategory(expenseCategory, accountId);                                
                            if (expenseCategoryID != 0) {
                                expenseCategoryList.add(expenseCategoryID);
                                // for push notification, Added by Jyoti
                                // Added by Jyoti, 04-01-2018
                                int expenseStatus = 0; // 0=new, 1=update, 2=delete
                                RunnableThreadJob theJob = new RunnableThreadJob();
                                theJob.sendAddEditExpenseCategoryNotificationToUsersOfAccount(expenseStatus, expenseCategory, accountId);
                                // Ended by jyoti, 08-01-2018
                            }
                            
                        } else {
                            validStatus = FieldSenseUtils.notvalid(validStatus);
                            validStatus = validStatus + "Customer name does not exist at line:" + (i + 1) + ".";
                        }
                        
                    }                    
            
                    if ((expenseErrorList.size() > 1)) {
                        CSVWriter writer = new CSVWriter(new FileWriter(csv));
                        writer.writeAll(expenseErrorList);
                  //      System.out.println("CSV written successfully.");
                        writer.close();
                    }
                    int totalCreated = expenseCategoryList.size();
                    
                    if ((expenseCategoryList.size() > 0) && validStatus.equals(Constant.VALID)) {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.EXPENSE_CATEGORY_CREATED, " ExpenseCategoryList ", expenseCategoryTotalList);
                    } else if ((expenseCategoryList.size() > 0) && !(validStatus.equals(Constant.VALID))) {
                        if ((expenseErrorList.size() > 1)){
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, totalCreated + " Expense category get created." + validStatus, " ErrorFileName ", errorFileNm);
                        }else{
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, totalCreated + " Expense category get created." + validStatus, "No Error File", "Error file not generated");                                
                        }
                    } else {
                        if ((expenseErrorList.size() > 1)){
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, validStatus + " Expense category not created . Please try again .", "ErrorFileName", errorFileNm);
                        }else{
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, validStatus + " Expense category not created . Please try again .", "No Error File", "Error file not generated");
                        }
                    }  

            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
            }
            
        } catch (NullPointerException e) {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Expense Category can not be empty. Please try again . ", "", "");
        } catch (FileNotFoundException ex) {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "File not uploaded. Please try again . ", "", "");
        } catch (IOException ex) {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "File not uploaded. Please try again . ", "", "");
        }
    }
    
}
