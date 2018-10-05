/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.territoryCategory.service;

import au.com.bytecode.opencsv.CSVWriter;
import com.qlc.fieldsense.territoryCategory.dao.TerritoryCategoryDao;
import static com.qlc.fieldsense.territoryCategory.dao.TerritoryCategoryDaoImpl.log4jLog;
import com.qlc.fieldsense.territoryCategory.model.MapCSVToTerritoryCategory;
import com.qlc.fieldsense.territoryCategory.model.TerritoryCategory;
import com.qlc.fieldsense.territoryCategory.model.TerritoryCategoryCSV;
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
import java.util.HashMap;
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
public class TerritoryCategoryManager {
    
    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
    TerritoryCategoryDao territoryCategoryDao = (TerritoryCategoryDao) GetApplicationContext.ac.getBean("territoryCategoryDaoImpl");

    /**
     * 
     * @param tCategory
     * @param userToken
     * @return 
     * @purpose used to create territory category
     */
    public Object createTerritoryCategory(TerritoryCategory tCategory, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (!territoryCategoryDao.isTerritoryCategoryAlreadyExists(tCategory.getCategoryName(), accountId)) {
                    if(territoryCategoryDao.createTerritoryCategory(tCategory, accountId)){
                        // for push notification, Added by Jyoti
                        // Added by Jyoti, 04-01-2018
//                        int territoryStatus = 0; // 0=new, 1=update, 2=delete
//                        RunnableThreadJob theJob = new RunnableThreadJob();
//                        theJob.sendAddEditTerritoryCategoryNotificationToUsersOfAccount(territoryStatus, tCategory, accountId);
                        // Ended by Jyoti, 04-01-2018
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Territory Category is created successfully.", "", "");
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Territory Category is not created. Please try again . ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Territory Category already exists.", "", "");
                }    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param eCategory
     * @param oldCatPoscsv
     * @param oldCatName
     * @param oldIsActive
     * @param userToken
     * @return 
     * @purpose used to update territory category 
     */
    public Object updateTerritoryCategory(TerritoryCategory tCategory,String oldCatPoscsv, String oldCatName,boolean oldIsActive,String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (territoryCategoryDao.isTerritoryCategoryValid(tCategory.getId(), accountId)) {
                    String oldTerritoryCategory = territoryCategoryDao.getTerritoryCategory(tCategory.getId(), accountId);
                    if(oldTerritoryCategory.trim().equalsIgnoreCase("Unknown")) {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Territory Category Unknown can not be modified", "", "");
                    }
//                    System.out.println("updateTerritoryCategory, terr cat name : "+tCategory.getCategoryName().trim());
                    List<java.util.HashMap> userInfoList = fieldSenseUtils.getListOfUserIdsForTerritoryID(tCategory.getCategoryName().trim(), accountId);
                    if (tCategory.getCategoryName().equals(oldTerritoryCategory)) {
                        if(territoryCategoryDao.updateTerritoryCategory(tCategory, oldCatPoscsv,oldCatName,oldIsActive,accountId)){
                            // for push notification, Added by Jyoti
                            // Added by Jyoti, 04-01-2018
                            int territoryStatus = 1; // 0=new, 1=update, 2=delete
                            RunnableThreadJob theJob = new RunnableThreadJob();
                            theJob.sendAddEditTerritoryCategoryNotificationToUsersOfAccount(userInfoList, territoryStatus, tCategory, accountId);
                            // Ended by Jyoti, 04-01-2018
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.TERRITORY_CATEGORY_UPDATED, " eCategory ", tCategory);
                        }else{
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Territory Category update failed.", "", "");
                        }            
                    } else {
                        if (!territoryCategoryDao.isTerritoryCategoryAlreadyExists(tCategory.getCategoryName(), accountId)) {
                            if(territoryCategoryDao.updateTerritoryCategory(tCategory,oldCatPoscsv,oldCatName, oldIsActive,accountId)){
                                // for push notification, Added by Jyoti
                                // Added by Jyoti, 04-01-2018
                                int territoryStatus = 1; // 0=new, 1=update, 2=delete
                                RunnableThreadJob theJob = new RunnableThreadJob();
                                Runnable job = theJob.sendAddEditTerritoryCategoryNotificationToUsersOfAccount(userInfoList, territoryStatus, tCategory, accountId);
                                Thread sendPushNotification = new Thread(job);
                                sendPushNotification.start();
                                // Ended by Jyoti, 04-01-2018
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.TERRITORY_CATEGORY_UPDATED, " eCategory ", tCategory);
                            }else {
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Territory Category update failed.", "", "");
                            }
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Territory Category already exists.", "", "");
                        }
                    }

                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.TERRITORY_CATEGORY_INVALID, "", "");
                }     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param eCategoryId
     * @param userToken
     * @return details of TERRITORY category base on expense category id 
     */
    public Object getOneTerritoryCategory(int eCategoryId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                TerritoryCategory eCategory = territoryCategoryDao.selectTerritoryCategory(eCategoryId, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Territory Category.", "eCategory", eCategory);  
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param eCategory
     * @param parentPosCsv
     * @param userToken
     * @param categoryName
     * @return 
     * @purpose delete territory category with category id
     */
    public Object deleteTerritoryCategory(int eCategory,String parentPosCsv,String categoryName,String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (territoryCategoryDao.isTerritoryCategoryValid(eCategory, accountId)) {
                    if(territoryCategoryDao.getTerritoryCategory(eCategory, accountId).trim().equalsIgnoreCase("Unknown")) {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Territory Category Unknown can not be deleted", "", "");
                    }
//                    System.out.println("deleteTerritoryCategory, terr cat name : "+categoryName.trim());
                    List<java.util.HashMap> userInfoList = fieldSenseUtils.getListOfUserIdsForTerritoryID(categoryName.trim(), accountId);
                    if (territoryCategoryDao.deleteTerritoryCategory(eCategory,parentPosCsv,categoryName,accountId)) {
                        // for push notification, Added by Jyoti
                        // Added by Jyoti, 04-01-2018
                        TerritoryCategory tCategoryObj = new TerritoryCategory();
                        tCategoryObj.setId(eCategory);
                        int expenseStatus = 2; // 0=new, 1=update, 2=delete
                        RunnableThreadJob theJob = new RunnableThreadJob();
                        theJob.sendAddEditTerritoryCategoryNotificationToUsersOfAccount(userInfoList, expenseStatus, tCategoryObj, accountId);
                        // Ended by jyoti, 08-01-2018
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.TERRITORY_CATEGORY_DELETE, "", "");
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.TERRITORY_CATEGORY_NOT_DELETE, "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.TERRITORY_CATEGORY_INVALID, "", "");
                }      
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param allRequestParams
     * @param userToken
     * @param response
     * @return all territory category details with offset 
     */
    public Object getAllTerritoryCategoryWithOffset(@RequestParam Map<String,String> allRequestParams,String userToken,HttpServletResponse response) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<TerritoryCategory> territoryCategoryList = territoryCategoryDao.selectAllTerritoryCategoryWithOffset(allRequestParams, accountId);
                int Total=0;
                if(!territoryCategoryList.isEmpty()){
                    Total=territoryCategoryList.get(0).getTotalTerritoryCategory();
                }
                return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, "List of Territory Category.", "territoryCategoryList", territoryCategoryList,Total);    
        } else {
            response.setContentType( MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }
    
    /**
     * 
     * @param userToken
     * @return all territory category details with offset 
     */
    public Object getAllTerritoryCategory(String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<TerritoryCategory> territoryCategoryList = territoryCategoryDao.selectAllTerritoryCategory(accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "List of Territory Category.", "territoryCategoryList", territoryCategoryList);     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }
    
    
    /**
     * 
     * @param parentCsvPos
     * @param userToken
     * @return all territory category details with offset 
     */
    public Object getAllParentTerritoryCategory(String parentCsvPos,String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<TerritoryCategory> territoryCategoryList = territoryCategoryDao.selectAllParentTerritoryCategory(parentCsvPos,accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "List of Territory Category.", "territoryCategoryList", territoryCategoryList);    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }        
    
    
    /**
     * 
     * @param userToken
     * @return all active territory categories
     */
    public Object getAllActiveTerritoryCategory(String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List territoryCategoryList = territoryCategoryDao.selectAllActiveTerritoryCategory(accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "List of Territory Category.", "territoryCategoryList", territoryCategoryList);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }
    
    
    /**
     * 
     * @param userToken
     * @return all active territory categories
     */
    public Object getAllActiveTerritoryCategories(String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<TerritoryCategory> territoryCategoryList = territoryCategoryDao.selectAllActiveTerritoryCategories(accountId);
                for(TerritoryCategory tecat:territoryCategoryList){
                    boolean hasParent=false;
                    int parentId=tecat.getParentCategory();
                    for(TerritoryCategory tecategry:territoryCategoryList){
                        if(parentId==tecategry.getId()){
                            hasParent=true;
                            break;
                        }
                    }
                    if(hasParent==false){
                        tecat.setParentCategory(0);
                    }                    
                }
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "List of Territory Category.", "territoryCategoryList", territoryCategoryList);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }
    
     /**
     * 
     * @param userToken
     * @return all active territory categories
     */
    public Object getAllMobileTerritoryCategories(String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<HashMap> territoryCategoryList= territoryCategoryDao.selectAllTerritoryCategoriesForMobile(accountId);                
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "List of Territory Category.", "territoryCategoryList",territoryCategoryList);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }
     /**
     * 
     * @param userId
     * @param userToken
     * @return all active territory categories
     */
    public Object getAllActiveTerritoryCategories(int userId,String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List territoryCategoryList = territoryCategoryDao.selectAllActiveTerritoryCategories(userId,accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "List of Territory Category.", "territoryCategoryList", territoryCategoryList);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }
    
    /**
     * 
     * @param userId
     * @param userToken
     * @return all active territory categories
     */
    public Object getAllUserActiveTerritoryCategories(int userId,String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<TerritoryCategory> territoryCategoryList = territoryCategoryDao.selectAllUserActiveTerritoryCategories(userId,accountId);
                for(TerritoryCategory tecat:territoryCategoryList){
                    boolean hasParent=false;
                    int parentId=tecat.getParentCategory();
                    for(TerritoryCategory tecategry:territoryCategoryList){
                        if(parentId==tecategry.getId()){
                            hasParent=true;
                            break;
                        }
                    }
                    if(hasParent==false){
                        tecat.setParentCategory(0);
                    }                    
                }
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "List of Territory Category.", "territoryCategoryList", territoryCategoryList);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }     
    
    // Added by Jyoti, 28-02-2017

    /**
     *
     * @param file
     * @param userToken
     * @return
     */
        public Object insertTerritoryCategoryByImport(MultipartFile file, String userToken) {
        
        try {
            String validStatus = Constant.VALID;
            List<Boolean> territoryCategoryList = new ArrayList<Boolean>();
            List<Integer> territoryCategoryTotalList = new ArrayList<Integer>();            
            List<String[]> territoryErrorList = new ArrayList<String[]>();
            
            if (fieldSenseUtils.isTokenValid(userToken)) {
                    int accountId = fieldSenseUtils.accountIdForToken(userToken);
                    int userId = fieldSenseUtils.userIdForToken(userToken);
                    
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss_SSS");
                    java.util.Date d = new java.util.Date();
                    String date = dateFormat.format(d);
                    String saveFileTo = Constant.IMPORT_ERROR_FILES;
                    String errorFileNm = accountId + "_" + date + "_TerritoryCategoryImportError.csv";
                    String csv = saveFileTo + errorFileNm;
                    
                    List<TerritoryCategoryCSV> list = MapCSVToTerritoryCategory.mapJavaBean(file);
                    for (int i = 0; i < 1; i++) {                                      
                        if (((list.get(i).getCategoryName().equalsIgnoreCase("Category Name")) && (list.get(i).getParentCatName().equalsIgnoreCase("Parent Category") ) && (list.get(i).getIsActive().equalsIgnoreCase("Active")))) {           
                        
                        } else {            
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "CSV file not in correct format. Please download the sample file.", "", "");
                        }
                    }
                    territoryErrorList.add(new String[]{"categoryName", "parentCatName", "isActive"});
                    
                    for (int i = 1; i < list.size(); i++) {
                        
                        TerritoryCategory territoryCategory = new TerritoryCategory();
                        User user = new User();
                        user.setId(userId);
                        String validStatus2 = Constant.VALID;   // valid
                        
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
                        
                        if (!(territoryCategoryDao.isTerritoryCategoryAlreadyExists(list.get(i).getCategoryName(), accountId))) {
                                
                                if ((territoryCategoryDao.isTerritoryCategoryAlreadyExists(list.get(i).getParentCatName(), accountId)) || list.get(i).getParentCatName().equalsIgnoreCase("0")) {                                        
                                        if ((list.get(i).getIsActive().equals("")) || (list.get(i).getIsActive() == null)) {
                                        } else if (list.get(i).getIsActive().trim().equalsIgnoreCase("yes")) {
                                            territoryCategory.setIsActive(true);
                                        } else if (list.get(i).getIsActive().trim().equalsIgnoreCase("no")) {
                                            territoryCategory.setIsActive(false);
                                        } else {
                                            validStatus = FieldSenseUtils.notvalid(validStatus);
                                            validStatus = validStatus + " Invalid active field at line:" + (i + 1) + ".";
                                            validStatus2 = validStatus;
                                        }
                                }
                        }else {
                            validStatus = validStatus + " Category Name at line " + (i + 1) + " already present.";
                        } 
                        
                        if (!(validStatus2.equals(Constant.VALID))) {
                            territoryErrorList.add(new String[]{list.get(i).getCategoryName(),list.get(i).getParentCatName(), list.get(i).getIsActive()});
                            continue;   // go to next iteration of loop, skips the rest of code
                        }
                        if (validStatus2.equals(Constant.VALID)) {
                            
                            territoryCategory.setCategoryName(list.get(i).getCategoryName());
                            territoryCategory.setCreatedBy(user);
                            
                            // Added by jyoti, bug-fix :12308
                                if(list.get(i).getCategoryName().equalsIgnoreCase("Unknown")){
                                    validStatus = FieldSenseUtils.notvalid(validStatus);
                                    validStatus = validStatus + "Category name cannot be Unknown at line:" + (i + 1) + ".";
                                }
                                else if(list.get(i).getParentCatName().equalsIgnoreCase("Unknown")){
                                    validStatus = FieldSenseUtils.notvalid(validStatus);
                                    validStatus = validStatus + "Parent Category name cannot be Unknown at line:" + (i + 1) + ".";
                                }
                            // Ended
                                else if(list.get(i).getParentCatName().equalsIgnoreCase("-")){
                                    // for adding category with no parent category
                                    territoryCategory.setParentCategory(0);
                                    territoryCategory.setCategoryPositionCsv("0");
                                    boolean territoryCategoryID = territoryCategoryDao.createTerritoryCategory(territoryCategory, accountId);                                
                                    if (territoryCategoryID != false) {                                    
                                        territoryCategoryList.add(territoryCategoryID);
                                        territoryCategoryTotalList.add(i);
                                    }
                                    
                                }                                
                                else{
                                    if ((territoryCategoryDao.isTerritoryCategoryAlreadyExists(list.get(i).getParentCatName(), accountId))) {
                                        
                                        territoryCategory.setParentCatName(list.get(i).getParentCatName());
                                        territoryCategory = territoryCategoryDao.selectParentCSVID(territoryCategory, accountId);

                                        String parentCSVID = territoryCategory.getCategoryPositionCsv();

                                        int  parentCatID = 0;
                                        if(parentCSVID.contains(",")){                                            
                                            String splitparentID = parentCSVID.trim().split(",")[0];
                                            parentCatID = Integer.parseInt(splitparentID.trim());
                                        }
                                        else{
                                            parentCatID = Integer.parseInt(parentCSVID.trim());
                                        }

                                        if(parentCatID != 0){
                                            territoryCategory.setParentCategory(parentCatID);
                                            territoryCategory.setCategoryPositionCsv(parentCSVID);
                                        }

                                        boolean territoryCategoryID = territoryCategoryDao.createTerritoryCategory(territoryCategory, accountId);                                
                                        if (territoryCategoryID != false) {                                    
                                            territoryCategoryList.add(territoryCategoryID);
                                            territoryCategoryTotalList.add(i);
                                        }
                                    }
                                }
                            
                        } else {
                                validStatus = FieldSenseUtils.notvalid(validStatus);
                                validStatus = validStatus + "Category name does not exist at line:" + (i + 1) + ".";
                        }
                        
                    }                    
            
                    if ((territoryErrorList.size() > 1)) {
                        CSVWriter writer = new CSVWriter(new FileWriter(csv));
                        writer.writeAll(territoryErrorList);
                        writer.close();
                    }
                    
                    int totalCreated = territoryCategoryTotalList.size();
                    
                    if ((territoryCategoryTotalList.size() > 0) && validStatus.equals(Constant.VALID)) {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.TERRITORY_CATEGORY_CREATED, " TerritoryCategoryList ", territoryCategoryTotalList);
                    } else if ((territoryCategoryTotalList.size() > 0) && !(validStatus.equals(Constant.VALID))) {
                        if ((territoryErrorList.size() > 1)){
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, totalCreated + " Territory category get created." + validStatus, " ErrorFileName ", errorFileNm);
                        }else{
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, totalCreated + " Territory category get created." + validStatus, "No Error File", "Error file not generated");                                
                        }
                    } else {
                        if ((territoryErrorList.size() > 1)){
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, validStatus + " Territory category not created . Please try again .", "ErrorFileName", errorFileNm);
                        }else{
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, validStatus + " Territory category not created . Please try again .", "No Error File", "Error file not generated");
                        }
                    }  

            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
            }
            
        } catch (NullPointerException e) {
            log4jLog.info(" insertTerritoryCategoryByImport " + e);
//            e.printStackTrace();
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Territory Category can not be empty. Please try again . ", "", "");
        } catch (FileNotFoundException e) {
            log4jLog.info(" insertTerritoryCategoryByImport " + e);
//            ex.printStackTrace();
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "File not uploaded. Please try again . ", "", "");
        } catch (IOException e) {
            log4jLog.info(" insertTerritoryCategoryByImport " + e);
//            ex.printStackTrace();
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "File not uploaded. Please try again . ", "", "");
        }
    }
    
}
