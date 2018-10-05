/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customerContact.service;

import com.qlc.fieldsense.customerContact.model.CustomerContacts;
import com.qlc.fieldsense.customerContact.model.CustomerContactsManager;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import com.qlc.fieldsense.utils.PhotoToIconCreator;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import javax.validation.Valid;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 *
 * @author anuja
 */
@Controller
@RequestMapping("/customerContact")
public class CustomerContactsService {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("CustomerContactsService");
    CustomerContactsManager customerContactsManager = new CustomerContactsManager();
    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");

    /**
     * @param customerContacts
     * @param userToken
     * @return 
     * @purpose used to create customers contact details
     */
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object createCustomerContact(@Valid @RequestBody CustomerContacts customerContacts, @RequestHeader(value = "userToken") String userToken) {
        return customerContactsManager.createCustomerContact(customerContacts, userToken);
    }

    /**
     * 
     * @param userToken
     * @return list of all contacts
     */
    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectCustomersContacts(@RequestHeader(value = "userToken") String userToken) {
        return customerContactsManager.getCustomersContacts(userToken);
    }

    /**
     * 
     * @param id
     * @param userToken
     * @return details of contact based on id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectCustomerContact(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return customerContactsManager.getCustomerContact(id, userToken);
    }

    /**
     * 
     * @param customerId
     * @param userToken
     * @return details of customers all contacts based on customer id
     */
    @RequestMapping(value = "customer/{customerId}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectCustomerContactOnCustomerId(@PathVariable int customerId, @RequestHeader(value = "userToken") String userToken) {
        return customerContactsManager.getCustomerContactOnCustomerId(customerId, userToken);
    }

    /**
     * 
     * @param id
     * @param userToken
     * @return 
     * @purpose delete specific contact details based on id 
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    Object deleteCustomerContacts(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return customerContactsManager.deleteCustomerContacts(id, userToken);
    }

    /**
     * 
     * @param customerId
     * @param userToken
     * @return 
     * @purpose delete specific customers all contact details based on customer id 
     */
    @RequestMapping(value = "customer/{customerId}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    Object deleteCustomerContactOnCustomerId(@PathVariable int customerId, @RequestHeader(value = "userToken") String userToken) {
        return customerContactsManager.deleteCustomerContactOnCustomerId(customerId, userToken);
    }

    /**
     * 
     * @param customerContacts
     * @param userToken
     * @return 
     * @purpose used to edit contact details 
     */
    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object updateCustomerContacts(@Valid @RequestBody CustomerContacts customerContacts, @RequestHeader(value = "userToken") String userToken) {
        return customerContactsManager.updateCustomerContacts(customerContacts, userToken);
    }

    /**
     * 
     * @param customerContacts
     * @param userToken
     * @return 
     * @purpose used to edit contact details based on contact id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object updateCustomerContactDetails(@RequestBody CustomerContacts customerContacts, @RequestHeader(value = "userToken") String userToken) {
        return customerContactsManager.updateCustomerContactDetails(customerContacts, userToken);
    }

    /*
     * It updates CustomerContact and Customer address

    @RequestMapping(value = "customer", method = RequestMethod.PUT, headers = "Accept=application/json")
    public @ResponseBody
    Object updateCustomerContactsOnCustomerId(@Valid @RequestBody CustomerContacts customerContacts, @RequestHeader(value = "userToken") String userToken) {
    return customerContactsManager.updateCustomerContactsOnCustomerId(customerContacts, userToken);
    }
     */
    
    /**
     * 
     * @param request (csv file)
     * @param userToken
     * @return 
     * @purpose used to import contact details
     */
    @RequestMapping(value = "/importCustomerContacts", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object importCustomerContacts(MultipartHttpServletRequest request, @RequestHeader(value = "userToken") String userToken) {
        Iterator<String> itr = request.getFileNames();
        MultipartFile file = null;
        while (itr.hasNext()) {
            file = request.getFile(itr.next());
            return customerContactsManager.insertCustomerContacts(file, userToken);
        }
        return null;
    }

   
    /**
     * 
     * @param expenseImage
     * @param ccId
     * @param cId
     * @param userToken
     * @author:anuja
     * @return 
     * @purpose:upload ProfilePic
     */
    @RequestMapping(value = "/customer/{cId}/customerContact/{ccId}", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object userProfilePic(MultipartHttpServletRequest expenseImage, @PathVariable int ccId, @PathVariable int cId, @RequestHeader(value = "userToken") String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                Iterator<String> itr = expenseImage.getFileNames();
                MultipartFile file = null;
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                String saveDirectory = Constant.PROFILEPIC_UPLOAD_PATH + accountId + "_" + cId + "_" + ccId + ".png";
                while (itr.hasNext()) {
                    file = expenseImage.getFile(itr.next());
                    try {
                        PhotoToIconCreator photoToIconCreator = new PhotoToIconCreator();
                        photoToIconCreator.uploadCustomerContactImage(file, accountId + "_" + cId + "_" + ccId);
                        file.transferTo(new File(saveDirectory));
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ProfilePic uploded successfully .", "", "");
                    } catch (IOException ex) {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " ProfilePic not uploaded. Please try again", "", "");
                    } catch (IllegalStateException ex) {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " ProfilePic not uploaded. Please try again", "", "");
                    }
                }
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ProfilePic uploaded successfully .", "", "");        
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }
   

    /**
     * 
     * @param expenseImage
     * @param cId
     * @param userToken
     * @author:anuja
     * @return 
     * @purpose:upload ProfilePic
     */
    @RequestMapping(value = "/customer/{cId}/customerContact", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object userProfilePic(MultipartHttpServletRequest expenseImage, @PathVariable int cId, @RequestHeader(value = "userToken") String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                Iterator<String> itr = expenseImage.getFileNames();
                MultipartFile file = null;
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                Date date = new Date();
                String imageName = accountId + "_" + cId + "_" + date.getTime() + ".png";
                String saveDirectory = Constant.PROFILEPIC_UPLOAD_PATH + imageName;
                while (itr.hasNext()) {
                    file = expenseImage.getFile(itr.next());
                    try {
                        file.transferTo(new File(saveDirectory));
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ProfilePic uploded successfully .", " image name ", imageName);
                    } catch (IOException ex) {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " ProfilePic not uploaded. Please try again", "", "");
                    } catch (IllegalStateException ex) {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " ProfilePic not uploaded. Please try again", "", "");
                    }
                }
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " ProfilePic not uploaded. Please try again", "", "");
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }
}
