/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.predefinedList.model;

import com.qlc.fieldsense.formbuilder.dao.CustomFormDao;
import com.qlc.fieldsense.formbuilder.model.CustomForm;
import com.qlc.fieldsense.predefinedList.dao.PredefinedListDao;
import com.qlc.fieldsense.territoryCategory.model.TerritoryCategory;
import com.qlc.fieldsense.utils.Constant;

import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import com.qlc.fieldsense.utils.RunnableThreadJob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author siddhesh
 */
public class PredefinedListManager {
    
    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
    PredefinedListDao predefinedListDao = (PredefinedListDao) GetApplicationContext.ac.getBean("predefinedListDaoImpl");
    CustomFormDao formDetailDao = (CustomFormDao) GetApplicationContext.ac.getBean("formDetailsDaoImpl");
    
    /**
     *
     * @param predefinedList
     * @param userToken
     * @return
     */
    public Object createPredefinedList(PredefinedList predefinedList, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                  if (!predefinedListDao.isPredefinedListNameAlreadyExists(predefinedList.getListName(), accountId)) {
                     if(predefinedListDao.createPredefinedList(predefinedList, accountId))
                     {
                      return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Predefined List is created successfully.", " ", "");
                     }
                     else
                     {
                      return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " fail to add list. ", "", "");
                     }                      
                  }else
                  {
                      return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "List name already exists.", "", "");
                  }
                 
        }else
        {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "",""); 
        }
    }
    
    /**
     *
     * @param allRequestParams
     * @param userToken
     * @param response
     * @return
     */
    public Object  getAllPredefinedListWithOffset (@RequestParam Map<String,String> allRequestParams,String userToken,HttpServletResponse response){
   
    if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<PredefinedList> predefinedList = predefinedListDao.selectAllPredefinedListWithOffset(allRequestParams, accountId);
               int Total=0;
                if(!predefinedList.isEmpty()){
                    Total=predefinedList.get(0).getTotalPredefinedList();
                }
                return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, "List of predefined List.", "predefinedList", predefinedList,Total);
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
     * @param predefinedList
     * @param userToken
     * @return
     */
    public Object getOnePredefinedList(int predefinedList, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                PredefinedList eCategory = predefinedListDao.selectPredefinedList(predefinedList, accountId);
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
     * @param predefinedList
     * @param userToken
     * @return
     */
    public Object updatePredefinedList(PredefinedList predefinedList, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (predefinedListDao.isPredefinedListAlreadyExistsForEdit(predefinedList.getListName(),accountId)){
                  
                   
                    if(predefinedListDao.updatePredefinedList(predefinedList, accountId)){
                    {                        
                        // Added by Jyoti, 04-01-2018, for push notification
                        List<Integer> listOfFormId = predefinedListDao.selectListOfFormForPredefinedList(predefinedList.getListId(), accountId);
//                        System.out.println("updatePredefinedList listOfFormId : " + listOfFormId.size() + " , data " + listOfFormId);
                        if (listOfFormId.size() > 0) {
                            int formStatus = 1; // if form is updated set status 1 for mobile side

                            for (Integer formId : listOfFormId) {
                                // update predefined list data for forms in xml
                                try {
                                    CustomForm formDetails = formDetailDao.getFormDetails(formId, accountId);
//                                    System.out.println("formDetails : "+formDetails.getFormName() + ", "+formDetails.getId());
                                    com.qlc.fieldsense.formbuilder.model.XMLConverter xml = new com.qlc.fieldsense.formbuilder.model.XMLConverter();
                                    boolean xmlCreated = xml.generateFormXml(formDetails, accountId);
                                    boolean formUpdatedOnupdated = predefinedListDao.updateCustomFormUpdatedOn(formId, accountId);                                        
                                    boolean settingUpdated = formDetailDao.updateFormSettingDateTime(accountId);
//                                    System.out.println("updatePredefinedList xmlCreated ? : " + xmlCreated + " , settingUpdated : "+settingUpdated +" , formUpdatedOnupdated : "+ formUpdatedOnupdated);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                RunnableThreadJob theJob = new RunnableThreadJob();
                                theJob.sendAddEditFormsNotificationToUsersOfAccount(formId, formStatus, accountId);
                            }
                        }
                        // Ended by Jyoti, 04-01-2018
                        
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Updated Successfully", "", "");
                    }
                    } else{
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "List not updated.", "", "");
                    }
                } else{     
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "List name already exists.", "", "");
                }

              
        }else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
        }
    
}
    
    /**
     *
     * @param predefinedList
     * @param userToken
     * @return
     */
    public Object deletePredefinedList(int predefinedList, String userToken) {
         if (fieldSenseUtils.isTokenValid(userToken)) {
         int accountId = fieldSenseUtils.accountIdForToken(userToken);
           if (predefinedListDao.deletePredefinedList(predefinedList, accountId)) {               
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "List Deleted", "", "");
            }else{
             return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED,"List Not deleted", "", "");
           
           }
           }else{
         return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
         }
        
        
    }
    
    /**
     *
     * @param userToken
     * @return
     */
    public Object getListNames(String userToken){
     if (fieldSenseUtils.isTokenValid(userToken)) {
     int accountId = fieldSenseUtils.accountIdForToken(userToken);
     HashMap<String,List<HashMap>> listOfPredefinedObjects=predefinedListDao.getAllLists(accountId);
       return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " List of names in predefined list . ", "listOfNames",listOfPredefinedObjects );  
     }else{
         return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
         }
    
    }

    /**
     *
     * @param id
     * @param userToken
     * @return
     */
    public Object getAllListData(int id,String userToken)
    {
     if (fieldSenseUtils.isTokenValid(userToken)) {
     int accountId = fieldSenseUtils.accountIdForToken(userToken);   
    HashMap<String, List<HashMap>> dataList=predefinedListDao.getAllListData(id,accountId);
     return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.STATUS_SUCCESS, "Successful ", "dataList",dataList);
     }else{
         return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
         }
     
    }
    
    /**
     *
     * @param id
     * @param userToken
     * @return
     */
    public Object getAllDataForPreview(int id,String userToken)
    {
     if (fieldSenseUtils.isTokenValid(userToken)) {
     int accountId = fieldSenseUtils.accountIdForToken(userToken);   
    HashMap<String, List<HashMap>> dataList=predefinedListDao.getAllListData(id,accountId);
     return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.STATUS_SUCCESS, "Successful ", "dataList",dataList);
     }else{
         return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
         }
     
    }
    
    /**
     *
     * @param listId
     * @param userToken
     * @return
     */
    public Object deletePredefinedListStatus(int listId, String userToken) {
         if (fieldSenseUtils.isTokenValid(userToken)) {
         int accountId = fieldSenseUtils.accountIdForToken(userToken);
           if (predefinedListDao.deletePredefinedListStatus(listId, accountId)) {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "List not present is form", "", "");
            }else{
             return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED,"List present In form", "", "");
           
           }
           }else{
         return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again . ", "", "");
         }
        
        
    }
    
}