/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.activityPurpose.model;

import au.com.bytecode.opencsv.CSVWriter;
import com.qlc.fieldsense.activityPurpose.dao.ActivityPurposeDao;
import static com.qlc.fieldsense.activityPurpose.dao.ActivityPurposeDaoImpl.log4jLog;
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
 * @author anuja
 */
public class ActivityPurposeManager {

    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
    ActivityPurposeDao activityPurposeDao = (ActivityPurposeDao) GetApplicationContext.ac.getBean("activityPurposeDaoImpl");

    
    /**
     * @param aPurpose
     * @param userToken
     * @return 
     * @purpose used to create activity purpose
    */
    public Object createActivityPurpose(ActivityPurpose aPurpose, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (!activityPurposeDao.isActivityPurposeAlreadyExists(aPurpose.getPurpose(), accountId)) {
                    int aPurposeId = activityPurposeDao.createActivityPurpose(aPurpose, accountId);
                    if (aPurposeId != 0) {
                        aPurpose = activityPurposeDao.selectActivityPurpose(aPurposeId, accountId);
                        // for push notification, Added by Jyoti
                        // Added by Jyoti, 04-01-2018
                        int purposeStatus = 0; // 0=new, 1=update, 2=delete
                        RunnableThreadJob theJob = new RunnableThreadJob();
                        theJob.sendAddEditActivityPurposeNotificationToUsersOfAccount(purposeStatus, aPurpose, accountId);
                        // Ended by Jyoti, 04-01-2018
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Activity Purpose is created successfully.", "aPurpose", aPurpose);
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Activity Purpose is not created. Please try again . ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Purpose already exists.", "", "");
                }   
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }

    /**
     * @param userToken
     * @return list of all activity purpose
     * @purpose to get list of all active activity purpose
     */
    public Object getAllActiveActivityPurpose(String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List activityPurposeList = activityPurposeDao.selectAllActiveActivityPurpose(accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "List of Activity Purpose.", "activityPurposeList", activityPurposeList);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }

     /**
     * @param aPurpose
     * @param userToken
     * @return 
     * @purpose used to modify activity purpose 
     */
    public Object updateActivityPurpose(ActivityPurpose aPurpose, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (activityPurposeDao.isActivityPurposeValid(aPurpose.getId(), accountId)) {
                    String oldPurpose = activityPurposeDao.getPurpose(aPurpose.getId(), accountId);
                    if (aPurpose.getPurpose().equals(oldPurpose)) {
                        aPurpose = activityPurposeDao.updateActivityPurpose(aPurpose, accountId);
                        // for push notification, Added by Jyoti
                        // Added by Jyoti, 04-01-2018
                        int purposeStatus = 1; // 0=new, 1=update, 2=delete
                        RunnableThreadJob theJob = new RunnableThreadJob();
                        theJob.sendAddEditActivityPurposeNotificationToUsersOfAccount(purposeStatus, aPurpose, accountId);
                        // Ended by Jyoti, 04-01-2018
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.ACTIVITY_PURPOSE_UPDATED, " aPurpose ", aPurpose);
                    } else {
                        if (!activityPurposeDao.isActivityPurposeAlreadyExistsForUpdate(aPurpose, accountId)) {
                            aPurpose = activityPurposeDao.updateActivityPurpose(aPurpose, accountId);
                            // for push notification, Added by Jyoti
                            // Added by Jyoti, 04-01-2018
                            int purposeStatus = 1; // 0=new, 1=update, 2=delete
                            RunnableThreadJob theJob = new RunnableThreadJob();
                            Runnable job = theJob.sendAddEditActivityPurposeNotificationToUsersOfAccount(purposeStatus, aPurpose, accountId);
                            Thread sendPushNotification = new Thread(job);
                            sendPushNotification.start();
                        // Ended by Jyoti, 04-01-2018
                            // Ended by jyoti, 08-01-2018
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.ACTIVITY_PURPOSE_UPDATED, " aPurpose ", aPurpose);
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Purpose already exists.", "", "");
                        }
                    }

                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.ACTIVITY_PURPOSE_INVALID, "", "");
                }
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }

    /**
     * @param purposeId
     * @param userToken
     * @return details of particular activity purpose 
     * @purpose used to get details of particular activity purpose based on id
     */
    public Object getOneActivityPurpose(int purposeId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                ActivityPurpose activityPurpose = activityPurposeDao.selectActivityPurpose(purposeId, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Activity Purpose.", "activityPurpose", activityPurpose);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }

    /**
     * @param aPurpose
     * @param userToken
     * @return 
     * @purpose used to delete specific activity purpose based on id
     */
    public Object deleteActivityPurpose(int aPurpose, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (activityPurposeDao.isActivityPurposeValid(aPurpose, accountId)) {
                    // Added by jyoti 27-june-2017
                    if(activityPurposeDao.getPurposeCategory(aPurpose, accountId).trim().equalsIgnoreCase("Unknown")) {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.ACTIVITY_PURPOSE_INVALID, "", "");
                    }
                    //Ended by Jyoti
                    if (activityPurposeDao.deleteActivityPurpose(aPurpose, accountId)) {
                        // for push notification, Added by Jyoti
                        // Added by Jyoti, 04-01-2018
                        ActivityPurpose aPurposeObj = new ActivityPurpose();
                        aPurposeObj.setId(aPurpose);
                        int purposeStatus = 2; // 0=new, 1=update, 2=delete
                        RunnableThreadJob theJob = new RunnableThreadJob();
                        theJob.sendAddEditActivityPurposeNotificationToUsersOfAccount(purposeStatus, aPurposeObj, accountId);
                        // Ended by Jyoti, 04-01-2018
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.ACTIVITY_PURPOSE_DELETE, "", "");
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.ACTIVITY_PURPOSE_NOT_DELETE, "", "");
                    }                    
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.ACTIVITY_PURPOSE_INVALID, "", "");
                }  
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }

    /**
     * @param userToken
     * @return 
     * @purpose used to get list of  all activity purpose
     */
    public Object getAllActivityPurpose(String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List activityPurposeList = activityPurposeDao.selectAllActivityPurpose(accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "List of Activity Purpose.", "activityPurposeList", activityPurposeList);  
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }

    /**
     * @param allRequestParams
     * @param userToken
     * @param response
     * @return 
     * @purpose used to get list of activity purpose with offset 
     */
    public Object getAllActivityPurposeWithOffset(@RequestParam Map<String,String> allRequestParams,  String userToken,HttpServletResponse response) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<ActivityPurpose> activityPurposeList = activityPurposeDao.selectAllActivityPurposeWithOffset(allRequestParams, accountId);
                int Total=0;
                if(!activityPurposeList.isEmpty()){
                    Total=activityPurposeList.get(0).getTotalActivityPurpose();
                }
                return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, "List of Activity Purpose.", "activityPurposeList", activityPurposeList,Total);   
        } else {
            response.setContentType( MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    }
    
    /**
     * @Added by Jyoti, 27-02-2017
     * @param file
     * @param userToken
     * @return 
     * @purpose used to insert purpose category by importing csv file
     */
    public Object insertPurposeCategoryByImport(MultipartFile file, String userToken) {        
        try {
            String validStatus = Constant.VALID;
            List<Integer> purposeCategoryList = new ArrayList<Integer>();
            List<Integer> purposeCategoryTotalList = new ArrayList<Integer>();            
            List<String[]> purposeErrorList = new ArrayList<String[]>();
            
            if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                int userId = fieldSenseUtils.userIdForToken(userToken);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss_SSS");
                java.util.Date d = new java.util.Date();
                String date = dateFormat.format(d);
                String saveFileTo = Constant.IMPORT_ERROR_FILES;
                String errorFileNm = accountId + "_" + date + "_PurposeCategoryImportError.csv";
                String csv = saveFileTo + errorFileNm;
                List<ActivityPurposeCategoryCSV> list = MapCSVToActivityPurposeCategory.mapJavaBean(file);
                for (int i = 0; i < 1; i++) {                                      
                    if (((list.get(i).getPurpose().equalsIgnoreCase("Purpose")) && (list.get(i).getIsActive().equalsIgnoreCase("Active")))) {           
                    } else {    
                          return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "CSV file not in correct format. Please download the sample file.", "", "");
                    }
                }
                purposeErrorList.add(new String[]{"purpose", "isActive"});
                for (int i = 1; i < list.size(); i++) {
                    ActivityPurpose activityPurposeCategory = new ActivityPurpose();
                    User user = new User();
                    user.setId(userId);
                    String validStatus2 = Constant.VALID;

                    if (list.get(i).getPurpose().equals(""))  {
                        validStatus = FieldSenseUtils.notvalid(validStatus);
                        validStatus = validStatus + "Purpose should not be blank at line : " + (i + 1) + ".";
                        validStatus2 = validStatus;
                    }

                    if (list.get(i).getPurpose().trim().length() >  100)  {
                        validStatus = FieldSenseUtils.notvalid(validStatus);
                        validStatus = validStatus + "Purpose cannot be more than 100 characters at line : " + (i + 1) + ".";
                        validStatus2 = validStatus;
                    }

                    if (!(activityPurposeDao.isActivityPurposeAlreadyExists(list.get(i).getPurpose(), accountId))) {
                        if ((list.get(i).getIsActive().equals("")) || (list.get(i).getIsActive() == null)) {
                        } else if (list.get(i).getIsActive().trim().equalsIgnoreCase("yes")) {
                            activityPurposeCategory.setIsActive(true);
                        } else if (list.get(i).getIsActive().equalsIgnoreCase("no")) {
                            activityPurposeCategory.setIsActive(false);
                        } else {
                            validStatus = FieldSenseUtils.notvalid(validStatus);
                            validStatus = validStatus + " Invalid active field at line : " + (i + 1) + ".";
                            validStatus2 = validStatus;
                        }
                    }else {
                        validStatus = validStatus + " Category Name at line " + (i + 1) + " is already present.";
                    } 

                    if (!(validStatus2.equals(Constant.VALID))) {
                        purposeErrorList.add(new String[]{list.get(i).getPurpose(), list.get(i).getIsActive()});
                        continue;
                    }
                    if (validStatus2.equals(Constant.VALID)) {
                        activityPurposeCategory.setPurpose(list.get(i).getPurpose());
                        activityPurposeCategory.setCreatedBy(user);                                
                        int activityPurposeCategoryID = activityPurposeDao.createActivityPurpose(activityPurposeCategory, accountId);                                
                        if (activityPurposeCategoryID != 0) {                                    
                            purposeCategoryList.add(activityPurposeCategoryID);
                            // for push notification, Added by Jyoti
                            // Added by Jyoti, 04-01-2018
                            int purposeStatus = 0; // 0=new, 1=update, 2=delete
                            RunnableThreadJob theJob = new RunnableThreadJob();
                            theJob.sendAddEditActivityPurposeNotificationToUsersOfAccount(purposeStatus, activityPurposeCategory, accountId);
                            // Ended by Jyoti, 04-01-2018
                        }
                    } else {
                        validStatus = FieldSenseUtils.notvalid(validStatus);
                        validStatus = validStatus + "Purpose does not exist at line : " + (i + 1) + ".";
                    }
                }                    

                if ((purposeErrorList.size() > 1)) {
                    CSVWriter writer = new CSVWriter(new FileWriter(csv));
                    writer.writeAll(purposeErrorList);
                    writer.close();
                }
                int totalCreated = purposeCategoryList.size();

                if ((purposeCategoryList.size() > 0) && validStatus.equals(Constant.VALID)) {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.ACTIVITY_PURPOSE_CREATED, " PurposeCategoryList ", purposeCategoryTotalList);
                } else if ((purposeCategoryList.size() > 0) && !(validStatus.equals(Constant.VALID))) {
                    if ((purposeErrorList.size() > 1)){
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, totalCreated + " Purpose category get created." + validStatus, " ErrorFileName ", errorFileNm);
                    }else{
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, totalCreated + " Purpose category get created." + validStatus, "No Error File", "Error file not generated");                                
                    }
                } else {
                    if ((purposeErrorList.size() > 1)){
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, validStatus + " Purpose category not created . Please try again .", "ErrorFileName", errorFileNm);
                    }else{
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, validStatus + " Purpose category not created . Please try again .", "No Error File", "Error file not generated");
                    }
                }  
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
            }            
        } catch (NullPointerException e) {
//            e.printStackTrace();
            log4jLog.info("insertPurposeCategoryByImport >> "+e);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Purpose Category can not be empty. Please try again . ", "", "");
        } catch (FileNotFoundException e) {
//            ex.printStackTrace();
            log4jLog.info("insertPurposeCategoryByImport >> "+e);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "File not uploaded. Please try again . ", "", "");
        } catch (IOException e) {
//            ex.printStackTrace();
            log4jLog.info("insertPurposeCategoryByImport >> "+e);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "File not uploaded. Please try again . ", "", "");
        }
    }    
    
}
