/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.formbuilder.service;

import com.qlc.fieldsense.formbuilder.model.CustomForm;
import com.qlc.fieldsense.formbuilder.model.CustomFormManager;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author pallavi.s
 */
@Controller
@RequestMapping("/adminCustomForms")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomFormService {
    
    private CustomFormManager formDetailsManager = new CustomFormManager();

    /**
     *
     * @param formdetails
     * @param userToken
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object createCustomForm(@RequestBody CustomForm formdetails, @RequestHeader(value = "userToken") String userToken) {
        return formDetailsManager.createCustomForm(formdetails, userToken);
    }
         
    /**
     *
     * @param formdetails
     * @param userToken
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object updateCustomForm(@RequestBody CustomForm formdetails, @RequestHeader(value = "userToken") String userToken) {
        return formDetailsManager.updateCustomFormDetails(formdetails, userToken);
    
    }
    
    /**
     *
     * @param id
     * @param userToken
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    Object deleteCustomForm(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return formDetailsManager.deleteCustomFormDetails(id, userToken);
    }

    /**
     * @param id
     * @Added by jyoti, 2018-02-08, for mobile
     * @param userToken
     * @return 
     */
    @RequestMapping(value = "/allFormsForMobile/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object getAllFormsForMobileCustForms(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return formDetailsManager.getAllFormsForMobileCustForms(id, userToken);
    }
    
    /**
     *
     * @param userToken
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    Object getAllActiveCustForms(@RequestHeader(value = "userToken") String userToken) {
        return formDetailsManager.getAllActiveCustForms(userToken);
    }
    
    /**
     *
     * @param userToken
     * @return
     */
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public
    @ResponseBody
    Object getAllCustomForms(@RequestHeader(value = "userToken") String userToken) {
        return formDetailsManager.getAllCustomForms(userToken);
    }
        
    /**
     *
     * @param userToken
     * @return
     */
    @RequestMapping(value = "/web/active",method = RequestMethod.GET)
    public
    @ResponseBody
    Object getActiveWebCustFormNames(@RequestHeader(value = "userToken") String userToken) {
        return formDetailsManager.getActiveWebCustomFormNames(userToken);
    }
    
    /**
     *
     * @param id
     * @param userToken
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object getCustFormDetails(@PathVariable int id,@RequestHeader(value = "userToken") String userToken) {
        return formDetailsManager.getCustomForm(id,userToken);
    }
    
    /**
     *
     * @param userToken
     * @return
     */
    @RequestMapping(value = "/formsLastUpdatedOn", method = RequestMethod.GET)
    public
    @ResponseBody
    Object formsLastUpdatedOn(@RequestHeader(value = "userToken") String userToken) {
        return formDetailsManager.formsLastUpdatedOn(userToken);
    }
    
    /**
     *
     * @param id
     * @param userToken
     * @return
     */
    @RequestMapping(value = "/xmlFile/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object getCustFormXMLDetails(@PathVariable int id,@RequestHeader(value = "userToken") String userToken) {
        return formDetailsManager.getCustFormXMLDetails(id,userToken);
    }
    
    /**
     *
     * @param formdetails
     * @param userToken
     * @return
     */
    @RequestMapping(value = "/updatedforms", method = RequestMethod.PUT)
    public
    @ResponseBody
    Object getUpdatedForms(@RequestBody CustomForm[] formdetails,@RequestHeader(value = "userToken") String userToken) {
        return formDetailsManager.getUpdatedForms(formdetails,userToken);
    }
    
    
    /**
     *Added by siddhesh
     * @param formdetails
     * @param userToken
     * @return
     */
    @RequestMapping(value = "/updatedforms/allForm/{timeStamp}", method = RequestMethod.PUT)
    public
    @ResponseBody
    Object getUpdatedFormsAllForm(@RequestBody CustomForm[] formdetails,@RequestHeader(value = "userToken") String userToken,@PathVariable String timeStamp) {
        return formDetailsManager.getUpdatedAllForms(formdetails,timeStamp,userToken);
    }
    
    
     
    /**
     *Added by siddhesh
     * @param formdetails
     * @param userToken
     * @return
     */
    @RequestMapping(value = "/updatedformsV2/allForm/{timeStamp}", method = RequestMethod.PUT)
    public
    @ResponseBody
    Object getUpdatedFormsAllFormV2(@RequestBody CustomForm formdetails,@RequestHeader(value = "userToken") String userToken,@PathVariable String timeStamp) {
        return formDetailsManager.getUpdatedAllFormsV2(formdetails,timeStamp,userToken);
    }
}
