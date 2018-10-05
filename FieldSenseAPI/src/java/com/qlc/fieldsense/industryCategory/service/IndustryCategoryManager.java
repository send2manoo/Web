/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.industryCategory.service;

import au.com.bytecode.opencsv.CSVWriter;
import com.qlc.fieldsense.industryCategory.dao.IndustryCategoryDao;
import static com.qlc.fieldsense.industryCategory.dao.IndustryCategoryDaoImpl.log4jLog;
import com.qlc.fieldsense.industryCategory.model.IndustryCategory;
import com.qlc.fieldsense.industryCategory.model.IndustryCategoryCSV;
import com.qlc.fieldsense.industryCategory.model.MapCSVToIndustryCategory;
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
 * @author awaneesh
 */
public class IndustryCategoryManager {
    
    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
    IndustryCategoryDao industryCategoryDao = (IndustryCategoryDao) GetApplicationContext.ac.getBean("industryCategoryDaoImpl");

    /**
     * 
     * @param eCategory
     * @param userToken
     * @return 
     * @purpose used to create industry category
     */
    public Object createIndustryCategory(IndustryCategory eCategory, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (!industryCategoryDao.isIndustryCategoryAlreadyExists(eCategory.getCategoryName(), accountId)) {
                    int eCategoryId = industryCategoryDao.createIndustryCategory(eCategory, accountId);
                    if (eCategoryId != 0) {
                        eCategory = industryCategoryDao.selectIndustryCategory(eCategoryId, accountId);
                        // for push notification, Added by Jyoti
                        // Added by Jyoti, 04-01-2018
                        int industryStatus = 0; // 0=new, 1=update, 2=delete
                        RunnableThreadJob theJob = new RunnableThreadJob();
                        theJob.sendAddEditIndustryCategoryNotificationToUsersOfAccount(industryStatus, eCategory, accountId);
                        // Ended by Jyoti, 04-01-2018
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Industry Category is created successfully.", "eCategory", eCategory);
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Industry Category is not created. Please try again . ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Industry Category already exists.", "", "");
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
     * @purpose used to update industry category 
     */
    public Object updateIndustryCategory(IndustryCategory eCategory, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (industryCategoryDao.isIndustryCategoryValid(eCategory.getId(), accountId)) {
                    String oldIndustryCategory = industryCategoryDao.getIndustryCategory(eCategory.getId(), accountId);
                    if(oldIndustryCategory.trim().equalsIgnoreCase("Unknown")) {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Industry Category Unknown can not be modified", "", "");
                    }
                    if (eCategory.getCategoryName().equals(oldIndustryCategory)) {
                        eCategory = industryCategoryDao.updateIndustryCategory(eCategory, accountId);
                        // for push notification, Added by Jyoti
                        // Added by Jyoti, 04-01-2018
                        int industryStatus = 1; // 0=new, 1=update, 2=delete
                        RunnableThreadJob theJob = new RunnableThreadJob();
                        theJob.sendAddEditIndustryCategoryNotificationToUsersOfAccount(industryStatus, eCategory, accountId);
                        // Ended by Jyoti, 04-01-2018
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.INDUSTRY_CATEGORY_UPDATED, " eCategory ", eCategory);
                    } else {
                        if (!industryCategoryDao.isIndustryCategoryAlreadyExists(eCategory.getCategoryName(), accountId)) {
                            eCategory = industryCategoryDao.updateIndustryCategory(eCategory, accountId);
                            // for push notification, Added by Jyoti
                            // Added by Jyoti, 04-01-2018
                            int industryStatus = 1; // 0=new, 1=update, 2=delete
                            RunnableThreadJob theJob = new RunnableThreadJob();
                            Runnable job = theJob.sendAddEditIndustryCategoryNotificationToUsersOfAccount(industryStatus, eCategory, accountId);
                            Thread sendPushNotification = new Thread(job);
                            sendPushNotification.start();
                            // Ended by Jyoti, 04-01-2018
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.INDUSTRY_CATEGORY_UPDATED, " eCategory ", eCategory);
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Industry Category already exists.", "", "");
                        }
                    }

                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INDUSTRY_CATEGORY_INVALID, "", "");
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
     * @return details of industry category base on industry category id 
     */
    public Object getOneIndustryCategory(int eCategoryId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                IndustryCategory eCategory = industryCategoryDao.selectIndustryCategory(eCategoryId, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Industry Category.", "eCategory", eCategory);
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
     * @purpose delete industry category with category id
     */
    public Object deleteIndustryCategory(int eCategory, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (industryCategoryDao.isIndustryCategoryValid(eCategory, accountId)) {
                    if(industryCategoryDao.getIndustryCategory(eCategory, accountId).trim().equalsIgnoreCase("Unknown")) {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INDUSTRY_CATEGORY_INVALID, "", "");
                    }
                    if (industryCategoryDao.deleteIndustryCategory(eCategory, accountId)) {
                        // for push notification, Added by Jyoti
                        // Added by Jyoti, 04-01-2018
                        IndustryCategory iCategoryObj = new IndustryCategory();
                        iCategoryObj.setId(eCategory);
                        int industryStatus = 2; // 0=new, 1=update, 2=delete
                        RunnableThreadJob theJob = new RunnableThreadJob();
                        theJob.sendAddEditIndustryCategoryNotificationToUsersOfAccount(industryStatus, iCategoryObj, accountId);
                        // Ended by Jyoti, 04-01-2018
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.INDUSTRY_CATEGORY_DELETE, "", "");
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.INDUSTRY_CATEGORY_NOT_DELETE, "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INDUSTRY_CATEGORY_INVALID, "", "");
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
     * @return all industry category details with offset 
     */
    public Object getAllIndustryCategoryWithOffset(@RequestParam Map<String,String> allRequestParams, String userToken,HttpServletResponse response) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<IndustryCategory> industryCategoryList = industryCategoryDao.selectAllIndustryCategoryWithOffset(allRequestParams, accountId);
                int Total=0;
                if(!industryCategoryList.isEmpty()){
                    Total=industryCategoryList.get(0).getTotalIndustryCategory();
                }
                return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, "List of Industry Category.", "industryCategoryList", industryCategoryList,Total);
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
     * @return all active industry categories
     */
    public Object getAllActiveIndustryCategory(String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List industryCategoryList = industryCategoryDao.selectAllActiveIndustryCategory(accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "List of Industry Category.", "industryCategoryList", industryCategoryList);
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
        public Object insertIndustryCategoryByImport(MultipartFile file, String userToken) {
        
        try {
            String validStatus = Constant.VALID;
            List<Integer> industryCategoryList = new ArrayList<Integer>();
            List<Integer> industryCategoryTotalList = new ArrayList<Integer>();            
            List<String[]> industryErrorList = new ArrayList<String[]>();
            
            if (fieldSenseUtils.isTokenValid(userToken)) {
                    int accountId = fieldSenseUtils.accountIdForToken(userToken);
                    int userId = fieldSenseUtils.userIdForToken(userToken);
                    
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss_SSS");
                    java.util.Date d = new java.util.Date();
                    String date = dateFormat.format(d);
                    String saveFileTo = Constant.IMPORT_ERROR_FILES;
                    String errorFileNm = accountId + "_" + date + "_IndustryCategoryImportError.csv";
                    String csv = saveFileTo + errorFileNm;
                    
                    List<IndustryCategoryCSV> list = MapCSVToIndustryCategory.mapJavaBean(file);
                    for (int i = 0; i < 1; i++) {                                      
                        if (((list.get(i).getCategoryName().equalsIgnoreCase("Category Name")) && (list.get(i).getIsActive().equalsIgnoreCase("Active")))) {           
                        } else {            
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "CSV file not in correct format. Please download the sample file.", "", "");
                        }
                    }
                    industryErrorList.add(new String[]{"categoryName", "isActive"});
                    
                    for (int i = 1; i < list.size(); i++) {
                        IndustryCategory industryCategory = new IndustryCategory();
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
                        
                        if (!(industryCategoryDao.isIndustryCategoryAlreadyExists(list.get(i).getCategoryName(), accountId))) {
                                    
                                        if ((list.get(i).getIsActive().equals("")) || (list.get(i).getIsActive().equals(null))) {
                                        } else if (list.get(i).getIsActive().trim().equalsIgnoreCase("yes")) {
                                            industryCategory.setIsActive(true);
                                        } else if (list.get(i).getIsActive().equalsIgnoreCase("no")) {
                                            industryCategory.setIsActive(false);
                                        } else {
                                            validStatus = FieldSenseUtils.notvalid(validStatus);
                                            validStatus = validStatus + " Invalid active field at line:" + (i + 1) + ".";
                                            validStatus2 = validStatus;
                                        }
                        }else {
                            validStatus = validStatus + " Category Name at line " + (i + 1) + " is already present.";
                        } 

                        if (!(validStatus2.equals(Constant.VALID))) {
                            industryErrorList.add(new String[]{list.get(i).getCategoryName(), list.get(i).getIsActive()});
                            continue;
                        }
                        if (validStatus2.equals(Constant.VALID)) {
                            industryCategory.setCategoryName(list.get(i).getCategoryName());
                            industryCategory.setCreatedBy(user);                                
                            int industryCategoryID = industryCategoryDao.createIndustryCategory(industryCategory, accountId);                                
                            if (industryCategoryID != 0) {                                    
                                industryCategoryList.add(industryCategoryID);
                                // for push notification, Added by Jyoti
                                // Added by Jyoti, 04-01-2018
                                int industryStatus = 0; // 0=new, 1=update, 2=delete
                                RunnableThreadJob theJob = new RunnableThreadJob();
                                theJob.sendAddEditIndustryCategoryNotificationToUsersOfAccount(industryStatus, industryCategory, accountId);
                                // Ended by Jyoti, 04-01-2018
                            }
                            
                        } else {
                            validStatus = FieldSenseUtils.notvalid(validStatus);
                            validStatus = validStatus + "Category name does not exist at line:" + (i + 1) + ".";
                        }
                        
                    }                    
            
                    if ((industryErrorList.size() > 1)) {
                        CSVWriter writer = new CSVWriter(new FileWriter(csv));
                        writer.writeAll(industryErrorList);
                        writer.close();
                    }
                    int totalCreated = industryCategoryList.size();
                    
                    if ((industryCategoryList.size() > 0) && validStatus.equals(Constant.VALID)) {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.INDUSTRY_CATEGORY_CREATED, " IndustryCategoryList ", industryCategoryTotalList);
                    } else if ((industryCategoryList.size() > 0) && !(validStatus.equals(Constant.VALID))) {
                        if ((industryErrorList.size() > 1)){
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, totalCreated + " Industry category get created." + validStatus, " ErrorFileName ", errorFileNm);
                        }else{
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, totalCreated + " Industry category get created." + validStatus, "No Error File", "Error file not generated");                                
                        }
                    } else {
                        if ((industryErrorList.size() > 1)){
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, validStatus + " Industry category not created . Please try again .", "ErrorFileName", errorFileNm);
                        }else{
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, validStatus + " Industry category not created . Please try again .", "No Error File", "Error file not generated");
                        }
                    }  

            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
            }
            
        } catch (NullPointerException e) {
            log4jLog.info(" insertIndustryCategoryByImport " + e);
//            e.printStackTrace();
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Industry Category can not be empty. Please try again . ", "", "");
        } catch (FileNotFoundException e) {
            log4jLog.info(" insertIndustryCategoryByImport " + e);
//            ex.printStackTrace();
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "File not uploaded. Please try again . ", "", "");
        } catch (IOException e) {
            log4jLog.info(" insertIndustryCategoryByImport " + e);
//            ex.printStackTrace();
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "File not uploaded. Please try again . ", "", "");
        }
    }
    
}
