/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.predefinedList.dao;

import com.opensymphony.xwork2.inject.util.Strings;
import com.qlc.fieldsense.predefinedList.model.PredefinedList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author siddhesh
 */
public interface PredefinedListDao {
    
    /**
     *
     * @param listName
     * @param accountId
     * @return
     */
    public boolean isPredefinedListNameAlreadyExists(String listName, int accountId);
    
    /**
     *
     * @param predefinedList
     * @param accountId
     * @return
     */
    public boolean createPredefinedList(PredefinedList predefinedList,int accountId);
    
    /**
     *
     * @param allRequestParams
     * @param accountId
     * @return
     */
    public List<PredefinedList> selectAllPredefinedListWithOffset(@RequestParam Map<String,String> allRequestParams, int accountId);   
    
    /**
     *
     * @param predefinedList
     * @param accountId
     * @return
     */
    public PredefinedList selectPredefinedList(int predefinedList, int accountId);
     
    /**
     *
     * @param predefinedList
     * @param accountId
     * @return
     */
    public boolean updatePredefinedList(PredefinedList predefinedList,int accountId);
      
    /**
     *
     * @param listName
     * @param accountId
     * @return
     */
    public boolean isPredefinedListAlreadyExistsForEdit(String listName, int accountId);
    
    /**
     *
     * @param listId
     * @param accountId
     * @return
     */
    public boolean deletePredefinedList(int listId,int accountId);
      
    /**
     *
     * @param accountId
     * @return
     */
    public HashMap<String,List<HashMap>> getAllLists(int accountId);
    
    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    public HashMap<String, List<HashMap>> getAllListData(int id,int accountId);
    
 //    public HashMap<String, List<HashMap>> getAllListDataForPreview(int id,int accountId);
    
    /**
     *
     * @param id
     * @param accountId
     * @return
     */
        
    public List<HashMap<String,List<String>>> getAllListData1(int id,int accountId);
    
    /**
     *
     * @param listId
     * @param accountId
     * @return
     */
    public boolean deletePredefinedListStatus(int listId,int accountId);
    
    /**
     * @Added by jyoti
     * @param predefinedListId
     * @param accountId
     * @return 
     */
    public List<Integer> selectListOfFormForPredefinedList(int predefinedListId, int accountId);
    
    /**
     * @Added by jyoti
     * @param formId
     * @param accountId
     * @return 
     */
    public boolean updateCustomFormUpdatedOn(int formId, int accountId);
   
}
