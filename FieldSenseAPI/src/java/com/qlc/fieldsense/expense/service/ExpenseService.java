/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.expense.service;

import com.qlc.fieldsense.expense.model.Expense;
import com.qlc.fieldsense.expense.model.ExpenseImage;
import com.qlc.fieldsense.expense.model.ExpenseManager;
import com.qlc.fieldsense.expense.model.ExpenseFilter;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import com.qlc.fieldsense.utils.PhotoToIconCreator;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 *
 * @author Ramesh
 * @date 23-02-2014
 */
@Controller
@RequestMapping("/expense")
public class ExpenseService {

    ExpenseManager expenseManager = new ExpenseManager();
    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");

    /**
     * 
     * @param expense
     * @param userToken
     * @return 
     * @purpose used to create expenses
     */
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object createExpense(@RequestBody Expense expense, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.createExpense(expense, userToken);
    }
    
    
    /**
     * 
     * @param expense
     * @param userToken
     * @return 
     * @purpose used to create expenses
     */
    @RequestMapping(value = "/createExpenseArray",method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object createExpenseArray(@RequestBody Expense expense, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.createExpenseArray(expense, userToken);
    }
   
    /**
     * 
     * @param id
     * @param userToken
     * @return details of expense based on expense id 
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectExpense(@PathVariable("id") int id, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.selectExpense(id, userToken);
    }
    
    /**
     * 
     * @param id
     * @param userToken
     * @return details of expense based on expense id 
     */
    @RequestMapping(value = "/expense_audit/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectExpenseAudit(@PathVariable("id") int id, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.selectExpenseAudit(id, userToken);
    }

    /**
     * 
     * @param userId
     * @param startDate
     * @param endDate
     * @param userToken
     * @return details of expenses of particular user in specific period
     */
    @RequestMapping(value = "/userExpense/{userId}/{startDate}/{endDate}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectUserexpense(@PathVariable("userId") int userId, @PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.selectUserExpense(userId, startDate, endDate, userToken);
    }

    /**
     * 
     * @param customerId
     * @param userToken
     * @return details of expenses occurred related to particular customer based on customer id
     */
    @RequestMapping(value = "/customerExpense/{customerId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectCustomerExpense(@PathVariable("customerId") int customerId, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.selectCustomerExpense(customerId, userToken);
    }

    /**
     * 
     * @param userId
     * @param customerId
     * @param userToken
     * @return  expense details of particular user for particular customer
     */
    @RequestMapping(value = "/userCustomerExpense/{userId}/{customerId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectUserCustomerExpense(@PathVariable("userId") int userId, @PathVariable("customerId") int customerId, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.selectUserCustomerExpense(userId, customerId, userToken);
    }

    /**
     * 
     * @param specificTypeId
     * @param userToken
     * @return all expense details of specific type based on expense type
     */
    @RequestMapping(value = "/specificTypeExpense/{specificTypeId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectSpecificTypeExpense(@PathVariable("specificTypeId") int specificTypeId, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.selctTypeOfExpense(specificTypeId, userToken);
    }

    /**
     * 
     * @param expense
     * @param userToken
     * @return 
     * @purpose used to update expense details
     */
    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object updateExpense(@RequestBody Expense expense, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.updateExpense(expense, userToken);
    }
    
     /**
     * 
     * @param expense
     * @param userToken
     * @return 
     * @purpose used to update expense details
     */
    @RequestMapping(value = "/updateExpense",method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object updateExpenseImage(@RequestBody Expense expense, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.updateExpenseForImage(expense, userToken);
    }
    

    /**
     * 
     * @param id
     * @param userToken
     * @return 
     * @purpose used to delete expense details based on expense id. 
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public
    @ResponseBody
    Object deleteExpense(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.deleteExpense(id, userToken);
    }

    /**
     * 
     * @param expenseImage
     * @param userToken
     * @return 
     * @structure changed and not in use anymore
     */
    @RequestMapping(value = "/image", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object createImage(@RequestBody ExpenseImage expenseImage, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.createExpenseImage(expenseImage, userToken);
    }

     /**
     * 
     * @param expenseId
     * @param userToken
     * @return 
     * @structure changed and not in use anymore
     */
    @RequestMapping(value = "/image/{expenseId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectExpenseImage(@PathVariable("expenseId") int expenseId, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.selectExpenseImage(expenseId, userToken);
    }

     /**
     * 
     * @param expenseImage
     * @param userToken
     * @return 
     * @structure changed and not in use anymore
     */
    @RequestMapping(value = "/image", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object updateExpenseImage(@RequestBody ExpenseImage expenseImage, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.updateExpenseImage(expenseImage, userToken);
    }

     /**
     * 
     * @param imageId
     * @param userToken
     * @return 
     * @structure changed and not in use anymore
     */
    @RequestMapping(value = "/image/{imageId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public
    @ResponseBody
    Object deleteExpenseImage(@PathVariable("imageId") int imageId, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.deleteExpenseImage(imageId, userToken);
    }

    /**
     * @author Ramesh
     * @date 17-06-1987
     * @param expenseImage
     * @param userToken
     * @return
     * @purpose add image for expense while creating a image
     */
    @RequestMapping(value = "/image/save", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object expenseImage(MultipartHttpServletRequest expenseImage, @RequestHeader(value = "userToken") String userToken) {
        Iterator<String> itr = expenseImage.getFileNames();
        MultipartFile file = null;
        int userId = fieldSenseUtils.userIdForToken(userToken);
        Date date = new Date();
        String imageName = userId + "_" + date.getTime() + ".png";
        String saveDirectory = Constant.IMAGE_UPLOAD_PATH + imageName;

        while (itr.hasNext()) {
            file = expenseImage.getFile(itr.next());
            try {
                file.transferTo(new File(saveDirectory));
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Image uploded successfully .", " image name ", imageName);
            } catch (IOException ex) {
                ex.printStackTrace();
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Image not uploaded. Please try again", "", "");
            } catch (IllegalStateException ex) {
                ex.printStackTrace();
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Image not uploaded. Please try again", "", "");
            }
        }
        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Image not uploaded. Please try again", "", "");
    }

    /**
     * 
     * @param expenseImage (image)
     * @param expenseId
     * @param userToken
     * @author:anuja
     * @return 
     * @purpose:upload image
     */
    @RequestMapping(value = "/{expenseId}/image/save", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object expenseImage(MultipartHttpServletRequest expenseImage, @PathVariable int expenseId, @RequestHeader(value = "userToken") String userToken) {
        Iterator<String> itr = expenseImage.getFileNames();
        MultipartFile file = null;
        int accountId = fieldSenseUtils.accountIdForToken(userToken);
        int userId = fieldSenseUtils.userIdForToken(userToken);
        String saveDirectory = Constant.IMAGE_UPLOAD_PATH + "expense" + "_" + accountId + "_" + userId + "_" + expenseId + ".png";

        while (itr.hasNext()) {
            file = expenseImage.getFile(itr.next());
            try {
                PhotoToIconCreator photoToIconCreator = new PhotoToIconCreator();
                photoToIconCreator.uploadExpenseImage(file, "expense_" + accountId + "_" + userId + "_" + expenseId);
                file.transferTo(new File(saveDirectory));

                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Image uploded successfully .", "", "");
            } catch (IOException ex) {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Image not uploaded. Please try again", "", "");
            } catch (IllegalStateException ex) {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Image not uploaded. Please try again", "", "");
            }
        }
        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Image not uploaded. Please try again", "", "");
    }

    /**
     * 
     * @param userId
     * @param userToken
     * @return details of user expenses with user id
     */
    @RequestMapping(value = "/userExpense/{userId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectUserexpense(@PathVariable("userId") int userId, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.selectUserExpense(userId, userToken);
    }

    /**
     * 
     * @param expense
     * @param userToken
     * @return 
     * @purpose used to approve user expense 
     */
    @RequestMapping(value = "/approve", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object approveExpense(@RequestBody Expense expense, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.approveExpense(expense, userToken);
    }

    /**
     * 
     * @param expense
     * @param id
     * @param userToken
     * @return 
     * @purpose used to approve user expense 
     */
    @RequestMapping(value = "/Account/approve", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object approveExpenseByAccount(@RequestBody Expense expense, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.approveExpenseByAccount(expense, userToken);
    }
    
    /**
     * 
     * @param expense
     * @param id
     * @param userToken
     * @return 
     * @purpose used to approve user expense 
     */
    @RequestMapping(value = "/Account/approve/multiple", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object approveMultiExpenseByAccount(@RequestBody Expense[] expense, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.approveMultiExpenseByAccount(expense, userToken);
    }
    
    
    /**
     * 
     * @param expense
     * @param userToken
     * @return 
     * @used to reject user expenses 
     */
    @RequestMapping(value = "/reject", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object rejectExpense(@RequestBody Expense expense, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.rejectExpense(expense, userToken);
    }

    /**
     * 
     * @param expense
     * @param id
     * @param userToken
     * @return 
     * @purpose used to approve user expense 
     */
    @RequestMapping(value = "/Account/reject", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object rejectExpenseByAccount(@RequestBody Expense expense, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.rejectExpenseByAccount(expense, userToken);
    }
    
    /**
     * 
     * @param expense
     * @param id
     * @param userToken
     * @return 
     * @purpose used to approve user expense 
     */
    @RequestMapping(value = "/Account/reject/multiple", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object rejectMultiExpenseByAccount(@RequestBody Expense[] expense, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.rejectMultiExpenseByAccount(expense, userToken);
    }
    
    /**
     * 
     * @param expense
     * @param id
     * @param userToken
     * @return 
     * @purpose used to approve user expense 
     */
    @RequestMapping(value = "/Account/disburse", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object disburseExpenseByAccount(@RequestBody Expense expense, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.disburseExpenseByAccount(expense,userToken);
    }
    
    /**
     * 
     * @param expense
     * @param id
     * @param userToken
     * @return 
     * @purpose used to approve user expense 
     */
    @RequestMapping(value = "/Account/disburse/defdate", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object disburseExpenseDefaultByAccount(@RequestBody Expense expense, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.disburseExpenseDefaultByAccount(expense,userToken);
    }
    
    /**
     * 
     * @param expense
     * @param userToken
     * @return 
     * @purpose used to approve users all expenses   
     */
    @RequestMapping(value = "/approveAllUserExpenses", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object approveUserAllExpense(@RequestBody Expense expense, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.approveUserAllExpenses(expense, userToken);
    }

    /**
     * 
     * @param userId
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return  details of expenses of specific user in specific period
     */
    @RequestMapping(value = "/userExpense/dateWaise/{userId}/{fromDate}/{toDate}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectUserexpenseDateWaise(@PathVariable("userId") int userId, @PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.selectUserExpenseDateWaise(userId, fromDate, toDate, userToken);
    }

    /**
     * 
     * @param userId
     * @param fromDate
     * @param toDate
     * @param expenseStatus
     * @param userToken
     * @return details of expenses of specific user in specific period with specific expense status
     */
        @RequestMapping(value = "/userExpense/dateWaise/{userId}/{fromDate}/{toDate}/{expenseStatus}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectUserexpenseDateWaiseFilterWithStatus(@PathVariable("userId") int userId, @PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @PathVariable("expenseStatus") int expenseStatus, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.selectUserExpenseDateWaise(userId, fromDate, toDate, expenseStatus, userToken);
    }
    
    /**
     * 
     * 
     * @param allRequestParams
     * @param userId
     * @param response
     * @param expenseStatus
     * @param expenseCategory
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return details of expenses of specific user in specific period with specific expense status
     */
    @RequestMapping(value="/Account", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectAccountExpenseDetails(@RequestParam Map<String,String> allRequestParams, @RequestHeader(value = "userToken") String userToken,HttpServletResponse response) {
         return expenseManager.selectAccountExpenseDetails(allRequestParams, userToken,response);
    }
    
    /**
     * @Added by jyoti, 02-18-2018
     * @param allRequestParams
     * @param userToken
     * @param response
     * @return 
     */
    @RequestMapping(value="/userExpenses", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectUserExpenseDetails(@RequestParam Map<String,String> allRequestParams, @RequestHeader(value = "userToken") String userToken,HttpServletResponse response) {
         return expenseManager.selectUserExpenseDetails(allRequestParams, userToken,response);
    }
    
    /**
     * @added by manohar
     * @param teamMember
     * @param category
     * @param status
     * @param fromDate
     * @param todate
     * @param userToken
     * @return 
     */
    @RequestMapping(value="/Account/{teamMember}/{category}/{status}/{fromdate}/{todate}/csv", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectAccountExpenseDetailsCSV(@PathVariable("teamMember") String teamMember,@PathVariable("category") String category,@PathVariable("status") String status,@PathVariable("fromdate") String fromDate,@PathVariable("todate") String todate, @RequestHeader(value = "userToken") String userToken) {
        return expenseManager.selectAccountExpenseDetailscsv(teamMember,category,status,fromDate,todate,userToken);
         
     }
    
    
     /**
     * @added by siddhesh 
     * @param expense
     * @param userToken
     * @return 
     * @purpose Test purpose of BASE64 image working --NOT IN USE.
     */
    @RequestMapping(value = "/updateExpenses", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object updateExpenseBase64(@RequestBody Expense expense, @RequestHeader(value = "userToken") String userToken) {
//        System.out.println("I am un createExpenseBase64");
        return expenseManager.updateExpenseWithBase64(expense, userToken);
    }
}
