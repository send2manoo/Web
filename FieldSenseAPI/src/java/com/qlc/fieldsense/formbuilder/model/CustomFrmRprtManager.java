/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.formbuilder.model;

import com.qlc.fieldsense.formbuilder.dao.CustomFrmRprtDao;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author pallavi.s
 */
public class CustomFrmRprtManager {

    public static final Logger log4jLog = Logger.getLogger("CustomFrmRprtManager");
    FieldSenseUtils util = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
    CustomFrmRprtDao customFrmRprtDao = (CustomFrmRprtDao) GetApplicationContext.ac.getBean("customFrmRprtImpl");

    /**
     *
     * @param formdetails
     * @param token
     * @return
     */
    public Object insertCustomFormDetails(FilledForm formdetails, String token) {
        if (util.isTokenValid(token)) {
            //if(util.isSessionExpired(token)){
            int accountId = util.accountIdForToken(token);
            int userId = util.userIdForToken(token);
            int filledformid = customFrmRprtDao.insertFormData(formdetails, accountId);
            if (filledformid > 0) {
                formdetails.setId(filledformid); //set formid object
                boolean res = customFrmRprtDao.insertFieldsData(formdetails, formdetails.getFormid(), filledformid, accountId);
                if (res == true) {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "submitted successfully ", " formdetails ", formdetails);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Problem in submitting data", "", "");
                }
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Problem in submitting Form", "", "");
            }
            // }else {
            //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }

    }

    /**
     *
     * @param formid
     * @param userId
     * @param fromDate
     * @param toDate
     * @param token
     * @return
     */
    public Object getFrmFilledData(int formid, int userId, String fromDate, String toDate, String token) {
        if (util.isTokenValid(token)) {
            //if(util.isSessionExpired(token)){
            int accountId = util.accountIdForToken(token);
            Timestamp start = util.converDateToTimestamp(fromDate);
            Timestamp end = util.converDateToTimestamp(toDate);
            List<FilledForm> formDetails = customFrmRprtDao.getFormData(formid, userId, start, end, accountId);
            List<FilledForm> cmpformDetails = customFrmRprtDao.getFieldsData(formDetails, accountId);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " cmpformDetails ", cmpformDetails);
            // }else {
            //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /**
     *
     * @param formid
     * @param fromDate
     * @param toDate
     * @param token
     * @return
     */
    public Object getAllAdminFrmFilledData(int formid, String fromDate, String toDate, String token) {
        if (util.isTokenValid(token)) {
            //if(util.isSessionExpired(token)){
            int accountId = util.accountIdForToken(token);
            Timestamp start = util.converDateToTimestamp(fromDate);
            Timestamp end = util.converDateToTimestamp(toDate);
            List<FilledForm> formDetails = customFrmRprtDao.getAllAdminFormData(formid, start, end, accountId);
            List<FilledForm> cmpformDetails = customFrmRprtDao.getAllAdminFieldsData(formDetails, accountId);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " cmpformDetails ", cmpformDetails);
            // }else {
            //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /**
     *
     * @param formid
     * @param userId
     * @param fromDate
     * @param toDate
     * @param token
     * @return
     */
    public Object getAllUserFrmFilledData(int formid, int userId, String fromDate, String toDate, String token) {
        if (util.isTokenValid(token)) {
            //if(util.isSessionExpired(token)){
            int accountId = util.accountIdForToken(token);
            Timestamp start = util.converDateToTimestamp(fromDate);
            Timestamp end = util.converDateToTimestamp(toDate);
            List<FilledForm> formDetails = customFrmRprtDao.getAllUserFormData(formid, userId, start, end, accountId);
            List<FilledForm> cmpformDetails = customFrmRprtDao.getAllUserFieldsData(formDetails, accountId);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " cmpformDetails ", cmpformDetails);
            // }else {
            //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /**
     * @Added by vaibhav, modified by jyoti
     * @param formId
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return
     */
    public Object getAllAdminFrmFilledDataAsZip(int formId, String fromDate, String toDate, String userToken) {
        try {
            if (util.isTokenValid(userToken)) {
                int accountId = util.accountIdForToken(userToken);
                Timestamp start = util.converDateToTimestamp(fromDate);
                Timestamp end = util.converDateToTimestamp(toDate);
                List<FilledForm> formDetails = customFrmRprtDao.getAllAdminFormData(formId, start, end, accountId);
                List<FilledForm> customeFormDetails = customFrmRprtDao.getAllAdminFieldsData(formDetails, accountId);
                boolean flagForSumbittedOnCsv = true;
                return getPathOfSavedZipFile(customeFormDetails, accountId, formId, flagForSumbittedOnCsv, null);

            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.log(Level.INFO, " getAllAdminFrmFilledDataAsZip{0}", e);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Some Exception", "", "");
        }
    }

    /**
     * @Added by vaibhav, modified by jyoti
     * @param formId
     * @param userId
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return
     */
    public Object getFormFilledDataAsZip(int formId, int userId, String fromDate, String toDate, String userToken) {
        try {
            if (util.isTokenValid(userToken)) {
                int accountId = util.accountIdForToken(userToken);
                Timestamp start = util.converDateToTimestamp(fromDate);
                Timestamp end = util.converDateToTimestamp(toDate);
                java.util.HashMap userData = util.getUserDetails(userId);
                List<FilledForm> formDetails = customFrmRprtDao.getFormData(formId, userId, start, end, accountId);
                List<FilledForm> customeFormDetails = customFrmRprtDao.getFieldsData(formDetails, accountId);
                boolean flagForSumbittedOnCsv = false;
                return getPathOfSavedZipFile(customeFormDetails, accountId, formId, flagForSumbittedOnCsv, userData);

            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.log(Level.INFO, " getFormFilledDataAsZip{0}", e);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Some Exception", "", "");
        }
    }

    /**
     * @Added by vaibhav, modified by jyoti
     * @param formId
     * @param userId
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return
     */
    public Object getAllUserFrmFilledDataAsZip(int formId, int userId, String fromDate, String toDate, String userToken) {
        try {
            if (util.isTokenValid(userToken)) {
                int accountId = util.accountIdForToken(userToken);
                Timestamp start = util.converDateToTimestamp(fromDate);
                Timestamp end = util.converDateToTimestamp(toDate);
                List<FilledForm> formDetails = customFrmRprtDao.getAllUserFormData(formId, userId, start, end, accountId);
                List<FilledForm> customeFormDetails = customFrmRprtDao.getAllUserFieldsData(formDetails, accountId);
                boolean flagForSumbittedOnCsv = true;
                return getPathOfSavedZipFile(customeFormDetails, accountId, formId, flagForSumbittedOnCsv, null);
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.log(Level.INFO, " getAllUserFrmFilledDataAsZip{0}", e);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Some Exception", "", "");
        }
    }

    /**
     * @param flagForSumbittedOnCsv
     * @param userData
     * @Added by jyoti
     * @purpose - to create zip file
     * @param customeFormDetails
     * @param accountId
     * @param formId
     * @return
     */
    public Object getPathOfSavedZipFile(List<FilledForm> customeFormDetails, int accountId, int formId, boolean flagForSumbittedOnCsv, java.util.HashMap userData) {
        try {
            if (customeFormDetails.size() > 0) {
                StringBuilder csvData = new StringBuilder(); // for creating csv file inside zip file
                
                String formName = customFrmRprtDao.getCustomFormName(formId, accountId); // for saving file using form name
                // For csv header
                List<String> formLabelList = customFrmRprtDao.getAllFormLabelList(formId, accountId);                
                
                if (flagForSumbittedOnCsv) {
                    csvData.append("\"" + "Submitted by" + "\",");
                }

                csvData.append("\"" + "Submitted on" + "\",");

                for (String formLabelList1 : formLabelList) {
                    csvData.append("\"").append(formLabelList1).append("\",");
                }

                csvData.append("\n");
                // csv header end

                // to save file name using details
                StringBuilder nameForFileSaveAs = new StringBuilder(formName);
                if(userData == null){
                    nameForFileSaveAs.append("_ALL" + "_").append(System.currentTimeMillis());
                } else if(userData.size() > 0){
                    nameForFileSaveAs.append("_").append(userData.get("fullName").toString()).append("_").append(userData.get("empCode").toString()).append("_").append(System.currentTimeMillis());
                }
                // do processing here for zip and set url for saved zip file
                
                String CUSTOM_FORM_IMAGE_UPLOAD_PATH = FieldSenseUtils.getPropertyValue("CUSTOM_FORM_IMAGE_UPLOAD_PATH");
                String constantPath = CUSTOM_FORM_IMAGE_UPLOAD_PATH + "account_" + accountId + "_form_" + formId;
                String zipFilePath = CUSTOM_FORM_IMAGE_UPLOAD_PATH + nameForFileSaveAs + ".zip";

                File file = new File(zipFilePath);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(fileOutputStream));

                for (FilledForm fielddetails : customeFormDetails) {

                    List<FilledFields> fieldList = fielddetails.getFilledData();

                    if (flagForSumbittedOnCsv) {
                        csvData.append("\"").append(fielddetails.getSubmittedby()).append("\",");
                    }

                    String submittedOnDateTime_12hour = "";
                    try {
                        java.sql.Timestamp getSubmitTime = fielddetails.getSubmitTime();
                        String submittedOnDateTime = getSubmitTime.toString();
                        // to add 5:30 hour
                        java.text.SimpleDateFormat sdf_for24hour = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        java.util.Calendar calendar = java.util.Calendar.getInstance();
                        calendar.setTime(sdf_for24hour.parse(submittedOnDateTime));
                        calendar.add(java.util.Calendar.MINUTE, +330);  // to get time 5:30 hrs.
                        String submittedOnDateTime_24hour = sdf_for24hour.format(calendar.getTime());
                        // to convert time in 12 hour format
                        java.text.SimpleDateFormat sdf_for12hour = new java.text.SimpleDateFormat("dd MMM yyyy hh:mm a"); // seconds removed bcz reports dont have seconds
                        java.util.Date date = sdf_for24hour.parse(submittedOnDateTime_24hour);
                        submittedOnDateTime_12hour = sdf_for12hour.format(date);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    
                    csvData.append("\"").append(submittedOnDateTime_12hour).append("\",");

                    for (FilledFields filledfields : fieldList) {

                        if (filledfields.getFieldType().equals("Image")) {

                            try {
                                String imagefilePath = constantPath + "_field_" + filledfields.getField_id_fk() + "_filledOnId_" + filledfields.getForm_fill_detail_id_fk() + ".png";
                                File imageFile = new File(imagefilePath);
                                BufferedImage bufferedImage = ImageIO.read(imageFile);
                                ZipEntry zipentry = new ZipEntry(filledfields.getField_value());
                                zipOutputStream.putNextEntry(zipentry);
                                ImageIO.write(bufferedImage, "png", zipOutputStream);
                                zipOutputStream.closeEntry();
                            } catch (Exception ex) {
//                                ex.printStackTrace();
                                log4jLog.log(Level.INFO, " getPathOfSavedZipFile for form field id :  {0}", filledfields.getField_id_fk());
                            }

                        }

                        csvData.append("\"").append(filledfields.getField_value()).append("\",");

                    }
                    csvData.append("\n");
                }

                String csvPath = FieldSenseUtils.getPropertyValue("CUSTOM_FORM_IMAGE_UPLOAD_PATH") + accountId + "_" + formId + ".csv";
//                System.out.println("csvPath : " + csvPath);

                File csvFile = new File(csvPath);
                //write contents of StringBuffer to a file
                try (BufferedWriter bwr = new BufferedWriter(new FileWriter(csvFile))) {
                    //write contents of StringBuffer to a file
                    bwr.write(csvData.toString());

                    //flush the stream
                    bwr.flush();

                    //close the stream
                    ZipEntry zipentry1 = new ZipEntry(nameForFileSaveAs + ".csv");
                    zipOutputStream.putNextEntry(zipentry1);
                    InputStream in = new FileInputStream(csvFile);

                    byte[] buf = new byte[4096];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        zipOutputStream.write(buf, 0, len);
                    }

                    zipOutputStream.closeEntry();
//                    System.out.println("csv done ");

                } catch (Exception e) {
                    e.printStackTrace();
                    log4jLog.log(Level.INFO, " getPathOfSavedZipFile{0}", e);

                } finally {
                    try {
                        zipOutputStream.flush();
                        zipOutputStream.close();
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        log4jLog.log(Level.INFO, " getPathOfSavedZipFile{0}", ex);
                    }

                }
                
                String customeFormImageZipFilePath = FieldSenseUtils.getPropertyValue("CUSTOM_FORM_IMAGE_GET_PATH") + nameForFileSaveAs + ".zip";

                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " getAllAdminFrmFilledDataAsZip ", customeFormImageZipFilePath);
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " No data available.", "", "");
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Some Exception.", "", "");
        }
    }

}
