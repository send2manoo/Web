/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.formbuilder.model;

import com.qlc.fieldsense.formbuilder.dao.CustomFormDao;
import com.qlc.fieldsense.formbuilder.dao.FormFieldsDao;
import static com.qlc.fieldsense.team.dao.TeamDaoImpl.log4jLog;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import com.qlc.fieldsense.utils.RunnableThreadJob;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pallavi.s
 */
public class CustomFormManager {
     FieldSenseUtils util = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
     CustomFormDao formDetailDao = (CustomFormDao) GetApplicationContext.ac.getBean("formDetailsDaoImpl");
     FormFieldsDao formFieldsDao = (FormFieldsDao) GetApplicationContext.ac.getBean("formFieldsDaoImpl");
     
    /**
     *
     * @param formdetails
     * @param token
     * @return
     */
    public Object createCustomForm(CustomForm formdetails, String token) {
        if (util.isTokenValid(token)) {
            //if(util.isSessionExpired(token)){
                int accountId = util.accountIdForToken(token);
                int userId = Integer.parseInt(formdetails.getUpdatedBy());
                int chkform=util.getFormId(formdetails.getFormName().trim(),accountId);
                if(chkform==0){
                    boolean flag= formDetailDao.insertForm(formdetails, accountId);
                    if(flag){
                        int formid=util.getFormId(formdetails.getFormName(),accountId);
                        formdetails.setId(formid); //set formid object
                        formFieldsDao.insertFields(formdetails.getTableData(), accountId,formid);
                        CustomForm formDetails = formDetailDao.getFormDetails(formid,accountId);
                        com.qlc.fieldsense.formbuilder.model.XMLConverter xml= new com.qlc.fieldsense.formbuilder.model.XMLConverter();
                        boolean xmlCreated=xml.generateFormXml(formDetails,accountId);
           //             System.out.println("Done creating xml :"+xmlCreated);
                        formDetailDao.updateFormSettingDateTime(accountId);
                        // Added by Jyoti, 04-01-2018, for push notification
                        int formStatus = 0; // if form is new set status 0 for mobile side
                        RunnableThreadJob theJob = new RunnableThreadJob();
                        theJob.sendAddEditFormsNotificationToUsersOfAccount(formid, formStatus, accountId);
                        // Ended by Jyoti, 04-01-2018
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Form Created Successfully", " formdetails ", formdetails);
                    }else{
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Problem in Creating Form", "", "");
                    }
                }else{
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Form Name Already Exist", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}       
        }else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
}
     
    /**
     *
     * @param formdetails
     * @param token
     * @return
     */
    public Object updateCustomFormDetails(CustomForm formdetails, String token) {
        if (util.isTokenValid(token)) {
            //if(util.isSessionExpired(token)){
                int accountId = util.accountIdForToken(token);
                int userId = Integer.parseInt(formdetails.getUpdatedBy());
                int formid = formDetailDao.updateCustomForm(formdetails, accountId);
                if(formid > 0){
                     //   formDetailDao.getFormDetails(formid, accountId);
                        formFieldsDao.updateFields(formdetails.getTableData(), formdetails.getDeletedFields(),accountId,formid);
                        CustomForm formDetails = formDetailDao.getFormDetails(formid,accountId);
                        com.qlc.fieldsense.formbuilder.model.XMLConverter xml= new com.qlc.fieldsense.formbuilder.model.XMLConverter();
                        boolean xmlCreated=xml.generateFormXml(formDetails,accountId);
         //               System.out.println("Done creating xml :"+xmlCreated);
                        formDetailDao.updateFormSettingDateTime(accountId);
                        
                        // Added by Jyoti, 04-01-2018, for push notification
                        int formStatus = 1; // if form is updated set status 1 for mobile side
                        RunnableThreadJob theJob = new RunnableThreadJob();
                        theJob.sendAddEditFormsNotificationToUsersOfAccount(formid, formStatus, accountId);
                        // Ended by Jyoti, 04-01-2018
                        
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Form Updated Successfully ", " formdetails ", formdetails);
                }else{
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid Form", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        }else{
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
}
     
    /**
     *
     * @param id
     * @param token
     * @return
     */
    public Object deleteCustomFormDetails(int id, String token) {
        if (util.isTokenValid(token)) {
            //if(util.isSessionExpired(token)){
                int accountId = util.accountIdForToken(token);
                if (formDetailDao.isFormValid(id, accountId)) {
                    CustomForm custform = new CustomForm();
                    boolean i = formDetailDao.deleteCustomForm(id, accountId);
                    com.qlc.fieldsense.formbuilder.model.XMLConverter xml= new com.qlc.fieldsense.formbuilder.model.XMLConverter();
                    xml.deleteXMLFile(id, accountId);
                    formDetailDao.updateFormSettingDateTime(accountId);
                    // Added by Jyoti, 15-01-2018, for push notification
                    int formStatus = 2; // if form is deleted set status 2 for mobile side
                    RunnableThreadJob theJob = new RunnableThreadJob();
                    theJob.sendAddEditFormsNotificationToUsersOfAccount(id, formStatus, accountId);
                    // Ended by Jyoti, 15-01-2018
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.FORM_DELETED, " formdetails ", custform);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_FORM, "", "");
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
     * @param token
     * @return
     */
    public Object getAllCustomForms(String token) {
        //String name=Constant.CUSTOM_FORM_XML_UPLOAD_PATH;
            if (util.isTokenValid(token)) {
                //if(util.isSessionExpired(token)){
                    int accountId = util.accountIdForToken(token);
                    List<CustomForm> formNameList = formDetailDao.getAllFormDetails(accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " formnames ", formNameList);
                // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
                //}     
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
            }
        }
      
    /**
     * @param formId
     * @Added by jyoti
     * @param token
     * @return 
     */
    public Object getAllFormsForMobileCustForms(int formId, String token) {
       if (util.isTokenValid(token)) {
           int accountId = util.accountIdForToken(token);
           List<CustomForm> formNameList = formDetailDao.getAllFormsForMobileCustForms(formId, accountId);
           return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " formnames ", formNameList);
       } else {
           return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
       }
    }
    
    /**
     *
     * @param token
     * @return
     */
    public Object getAllActiveCustForms(String token) {
        //String name=Constant.CUSTOM_FORM_XML_UPLOAD_PATH;
            if (util.isTokenValid(token)) {
                //if(util.isSessionExpired(token)){
                    int accountId = util.accountIdForToken(token);
                    List<CustomForm> formNameList = formDetailDao.getAllActiveFormsDetails(accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " formnames ", formNameList);
             // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
                //}
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
            }
        }
        
    /**
     *
     * @param token
     * @return
     */
    public Object getActiveWebCustomFormNames(String token) {
        //String name=Constant.CUSTOM_FORM_XML_UPLOAD_PATH;
            if (util.isTokenValid(token)) {
                //if(util.isSessionExpired(token)){
                    int accountId = util.accountIdForToken(token);
                    List<CustomForm> formNameList = formDetailDao.getAllActiveWebFormDetails(accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " formnames ", formNameList);
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
     * @param token
     * @return
     */
    public Object getCustomForm(int formid,String token) {
            if (util.isTokenValid(token)) {
                 //if(util.isSessionExpired(token)){
                    int accountId = util.accountIdForToken(token);
                    CustomForm formDetails = formDetailDao.getFormDetailsForWeb(formid,accountId); // changed by jyoti, 23-02-2018
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " customFormDetails ", formDetails);
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
     * @param token
     * @return
     */
    public Object getCustFormXMLDetails(int formid,String token) {
            StringBuilder sb = new StringBuilder();
            java.io.BufferedReader br =null;
            String xmlFile="";
            if (util.isTokenValid(token)) {
                 //if(util.isSessionExpired(token)){
                    int accountId = util.accountIdForToken(token);
                    try{
                        xmlFile=Constant.CUSTOM_FORM_XML_UPLOAD_PATH+"account_"+accountId+"_form_"+formid+".xml";
                        br = new java.io.BufferedReader(new java.io.FileReader(xmlFile));
                        String line;

                        while((line=br.readLine())!= null){
                            sb.append(line.trim());
                        }
                    }catch(Exception e){
                        log4jLog.info(" getCustFormXMLDetails " + e);
         //               System.out.println("Issue in acccessing:"+xmlFile);
//                        e.printStackTrace();
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Problem in accessing xml file", "", "");
                    }
                    finally{
                        try{
                          br.close();
                        }catch(Exception e1){
                            log4jLog.info(" getCustFormXMLDetails " + e1);
                        }
                    }

                    //return sb.toString();
                    //CustomForm formDetails = formDetailDao.getFormDetails(formid,accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " customFormXMLDetails ", sb.toString());
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
                //}
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
            }
    }
    
    /**
     *
     * @param token
     * @return
     */
    public Object formsLastUpdatedOn(String token) {
        if (util.isTokenValid(token)) {
             //if(util.isSessionExpired(token)){
                int accountId = util.accountIdForToken(token);
                //CustomForm formDetails = formDetailDao.getFormDetails(formid,accountId);
                java.sql.Timestamp updatedOn=formDetailDao.getFormsLastUpdatedOn(accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " customFormDetails ", updatedOn);
       // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
             //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }
    
    /**
     *
     * @param formdetails
     * @param token
     * @return
     */
    public Object getUpdatedForms(CustomForm[] formdetails,String token) {
        if (util.isTokenValid(token)) {
             //if(util.isSessionExpired(token)){
                int accountId = util.accountIdForToken(token);
                List<Map> customforms=formDetailDao.getUpdatedForms(formdetails,accountId);
                List<Integer> formIds=formDetailDao.getAllFormIds(accountId);
                HashMap result=new HashMap();
                List<Map<String,Integer>> deleted_Ids=new  ArrayList<Map<String,Integer>>();
               // for(CustomForm form:formdetails){
                //    requestIds.add(form.getId());
                //}
                for(CustomForm form:formdetails){
                    boolean idExists=false;
                    for(Integer ids:formIds){
                        if(form.getId()==ids){
                            idExists=true;
                        }
                    }
                    if(idExists==false){
                        Map<String,Integer> deletedIds=new HashMap<String,Integer>();
                        deletedIds.put("id",form.getId());
                        deleted_Ids.add(deletedIds);
                    }
                }
                result.put("customforms",customforms);
                result.put("deletedIds",deleted_Ids);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "formnames", result);
       // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
             //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }        
    
        public Object getUpdatedAllForms(CustomForm[] formdetails,String timeStamp,String token) {
        if (util.isTokenValid(token)) {
            try {
                //if(util.isSessionExpired(token)){
                int accountId = util.accountIdForToken(token);
                Timestamp timeStampValue=new Timestamp(Long.parseLong(timeStamp));
                String date=timeStampValue.toString();
//                System.out.println("date" +date);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar c3 = Calendar.getInstance();
                c3.setTime(sdf.parse(date));
                //c3.add(Calendar.MINUTE, -330);  // to get time 5:30 hrs back.
                String strStartDate = sdf.format(c3.getTime());
//                System.out.println("date after change"+strStartDate);
                List<Map> customforms=formDetailDao.getUpdatedAllForms(formdetails,strStartDate,accountId);
                //HashMap result=new HashMap();
                //result.put("customforms",customforms);
                //customforms.putAll(deletedIds);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "formnames", customforms);
                // }else {
                //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
                //}
            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(CustomFormManager.class.getName()).log(Level.SEVERE, null, ex);
                 return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
            }
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }
    
             public Object getUpdatedAllFormsV2(CustomForm formdetailsData,String timeStamp,String token) {
        if (util.isTokenValid(token)) {
            try {
                //if(util.isSessionExpired(token)){
                int accountId = util.accountIdForToken(token);
                Timestamp timeStampValue=new Timestamp(Long.parseLong(timeStamp));
                String date=timeStampValue.toString();
//                System.out.println("date" +date);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar c3 = Calendar.getInstance();
                c3.setTime(sdf.parse(date));
                //c3.add(Calendar.MINUTE, -330);  // to get time 5:30 hrs back.
                String strStartDate = sdf.format(c3.getTime());
//                System.out.println("date after change"+strStartDate);
                CustomForm[] formdetails =formdetailsData.getUpdatedForms();
                List<Map> customforms=formDetailDao.getUpdatedAllForms(formdetails,strStartDate,accountId);
                //HashMap result=new HashMap();
                //result.put("customforms",customforms);
                //customforms.putAll(deletedIds);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "formnames", customforms);
                // }else {
                //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
                //}
            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(CustomFormManager.class.getName()).log(Level.SEVERE, null, ex);
                 return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
            }
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }
             
             
        public List<Map> getAllFormsForOffline (CustomForm[] formdetails,String timeStamp,String token){
        try {
                //if(util.isSessionExpired(token)){
                int accountId = util.accountIdForToken(token);
                Timestamp timeStampValue=new Timestamp(Long.parseLong(timeStamp));
                String date=timeStampValue.toString();
//                System.out.println("date" +date);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar c3 = Calendar.getInstance();
                c3.setTime(sdf.parse(date));
                //c3.add(Calendar.MINUTE, -330);  // to get time 5:30 hrs back.
                String strStartDate = sdf.format(c3.getTime());
//                System.out.println("date after change"+strStartDate);
                List<Map> customforms=formDetailDao.getUpdatedAllForms(formdetails,strStartDate,accountId);
                //HashMap result=new HashMap();
                //result.put("customforms",customforms);
                //customforms.putAll(deletedIds);
                //return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "formnames", customforms);
                return customforms;
                // }else {
                //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
                //}
            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(CustomFormManager.class.getName()).log(Level.SEVERE, null, ex);
               //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
                return new ArrayList<Map>();
            }
        
        }
        
}