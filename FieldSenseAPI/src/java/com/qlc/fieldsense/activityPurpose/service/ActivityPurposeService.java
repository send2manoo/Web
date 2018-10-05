/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.activityPurpose.service;

import com.qlc.fieldsense.activityPurpose.model.ActivityPurpose;
import com.qlc.fieldsense.activityPurpose.model.ActivityPurposeManager;
import java.util.Iterator;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 *
 * @author anuja
 */
@Controller
@RequestMapping("/activityPurpose")
public class ActivityPurposeService {

    ActivityPurposeManager aPurposeManager = new ActivityPurposeManager();
    
    /**
    * @param aPurpose
    * @param userToken
    * @return 
    * @purpose used to create activity purpose
    */
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object createActivityPurpose(@RequestBody ActivityPurpose aPurpose, @RequestHeader(value = "userToken") String userToken) {
        return aPurposeManager.createActivityPurpose(aPurpose, userToken);
    }

    /**
     * @param userToken
     * @return list of all activity purpose
     * @purpose to get list of all active activity purpose
     */
    @RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getAllActiveActivityPurpose(@RequestHeader(value = "userToken") String userToken) {
        return aPurposeManager.getAllActiveActivityPurpose(userToken);
    }

    /**
     * @param aPurpose
     * @param userToken
     * @return 
     * @purpose used to modify activity purpose 
     */
    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object updateActivityPurpose(@RequestBody ActivityPurpose aPurpose, @RequestHeader(value = "userToken") String userToken) {
        return aPurposeManager.updateActivityPurpose(aPurpose, userToken);
    }

    /**
     * @param id
     * @param userToken
     * @return details of particular activity purpose 
     * @purpose used to get details of particular activity purpose based on id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getOneActivityPurpose(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return aPurposeManager.getOneActivityPurpose(id, userToken);
    }

    /**
     * 
     * @param id
     * @param userToken
     * @return 
     * @purpose used to delete specific activity purpose based on id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    Object deleteActivityPurpose(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return aPurposeManager.deleteActivityPurpose(id, userToken);
    }
    
    /**
     * 
     * @param userToken
     * @return 
     * @purpose used to get list of  all activity purpose
     */
    @RequestMapping(value = "/purpose", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getAllActivityPurpose(@RequestHeader(value = "userToken") String userToken) {
        return aPurposeManager.getAllActivityPurpose(userToken);
    }

    /**
     * @param allRequestParams
     * @param userToken
     * @param response
     * @return 
     * @purpose used to get list of activity purpose with offset 
     */
    @RequestMapping(value = "/purpose/offset/", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getAllActivityPurposeWithOffset(@RequestParam Map<String,String> allRequestParams, @RequestHeader(value = "userToken") String userToken,HttpServletResponse response) {
        return aPurposeManager.getAllActivityPurposeWithOffset(allRequestParams, userToken,response);
    }
    
    /**
     * 
     * @param request
     * @param userToken
     * @author: Jyoti, 28-02-2017
     * @return 
     * @purpose:import purpose category
     */
    @RequestMapping(value = "/importPurposeCategory", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object importPurposeCategory(MultipartHttpServletRequest request, @RequestHeader(value = "userToken") String userToken){
        Iterator<String> itr = request.getFileNames();
        MultipartFile file;
        while (itr.hasNext()) {
            file = request.getFile(itr.next());
            return aPurposeManager.insertPurposeCategoryByImport(file, userToken);
        }
        return null;
    }
    
}
