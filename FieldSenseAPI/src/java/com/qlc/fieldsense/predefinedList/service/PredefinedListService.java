/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.predefinedList.service;

import com.qlc.fieldsense.predefinedList.model.PredefinedList;
import com.qlc.fieldsense.predefinedList.model.PredefinedListManager;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author siddhesh
 */
@Controller
@RequestMapping("/predefinedList")
public class PredefinedListService {
    
     PredefinedListManager prdefinedListManager = new PredefinedListManager();
     
      /* 
     * @param PredefinedList
     * @param userToken
     * @purpose used to create user defined list
     */

    /**
     *
     * @param predefinedList
     * @param userToken
     * @return
     */
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody
    Object createTerritoryCategory(@RequestBody PredefinedList predefinedList, @RequestHeader(value = "userToken") String userToken) {
        return prdefinedListManager.createPredefinedList(predefinedList, userToken);
    }
    
    /**
     *
     * @param id
     * @param userToken
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public @ResponseBody
    Object getOneIndustryCategory(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return prdefinedListManager.getOnePredefinedList(id, userToken);
    }
    
    /**
     *
     * @param id
     * @param userToken
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object deleteIndustryCategory(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return prdefinedListManager.deletePredefinedList(id, userToken);
    }
    
    /**
     *
     * @param predefined
     * @param userToken
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public @ResponseBody
    Object updateIndustryCategory(@RequestBody PredefinedList predefined, @RequestHeader(value = "userToken") String userToken) {
        return prdefinedListManager.updatePredefinedList(predefined, userToken);
    }
    
    /**
     *
     * @param allRequestParams
     * @param userToken
     * @param response
     * @return
     */
    @RequestMapping(value = "/offset/", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getAllTerritoryCategoryWithOffset(@RequestParam Map<String,String> allRequestParams, @RequestHeader(value = "userToken") String userToken,HttpServletResponse response) {
        return prdefinedListManager.getAllPredefinedListWithOffset(allRequestParams, userToken,response);
    }
    
    /**
     *
     * @param userToken
     * @return
     */
    @RequestMapping(value = "/getAllListNames", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getAllList(@RequestHeader(value = "userToken") String userToken) {
        return prdefinedListManager.getListNames(userToken);
    }
    
    /**
     *
     * @param id
     * @param userToken
     * @return
     */
    @RequestMapping(value = "/getAllListData/{id}", method = RequestMethod.GET,headers = "Accept=application/json")
    public @ResponseBody
    Object getAllListData(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return prdefinedListManager.getAllListData(id, userToken);
    }
    
    /**
     *
     * @param id
     * @param userToken
     * @return
     */
    @RequestMapping(value = "/getAllDataForPreview/{id}", method = RequestMethod.GET,headers = "Accept=application/json")
    public @ResponseBody
    Object getDataForPreview(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return prdefinedListManager.getAllDataForPreview(id, userToken);
    }
    
    /**
     *
     * @param id
     * @param userToken
     * @return
     */
    @RequestMapping(value = "/getDeleteStatus/{id}", method = RequestMethod.GET,headers = "Accept=application/json")
    public @ResponseBody
    Object getDeleteStatus(@PathVariable int id,@RequestHeader(value = "userToken") String userToken) {
        return prdefinedListManager.deletePredefinedListStatus(id, userToken);
    }
    
    
  
    
}