package com.qlc.fieldsense.industryCategory.service;

import com.qlc.fieldsense.industryCategory.model.IndustryCategory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.qlc.fieldsense.expenseCategory.service.ExpenseCategoryManager;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author awaneesh
 */

@Controller
@RequestMapping("/industryCategory")
public class IndustryCategoryService {
    
    IndustryCategoryManager industryCategoryManager = new IndustryCategoryManager();

    /**
     * 
     * @param eCategory
     * @param userToken
     * @return 
     * @purpose used to create industry category
     */
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody
    Object createIndustryCategory(@RequestBody IndustryCategory eCategory, @RequestHeader(value = "userToken") String userToken) {
        return industryCategoryManager.createIndustryCategory(eCategory, userToken);
    }

    /**
     * 
     * @param eCategory
     * @param userToken
     * @return 
     * @purpose used to update industry category 
     */
    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public @ResponseBody
    Object updateIndustryCategory(@RequestBody IndustryCategory eCategory, @RequestHeader(value = "userToken") String userToken) {
        return industryCategoryManager.updateIndustryCategory(eCategory, userToken);
    }

    /**
     * 
     * @param id
     * @param userToken
     * @return details of industry category base on industry category id 
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public @ResponseBody
    Object getOneIndustryCategory(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return industryCategoryManager.getOneIndustryCategory(id, userToken);
    }

    /**
     * 
     * @param id
     * @param userToken
     * @return 
     * @purpose delete industry category with category id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object deleteIndustryCategory(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return industryCategoryManager.deleteIndustryCategory(id, userToken);
    }
    
    /**
     * 
     * @param allRequestParams
     * @param userToken
     * @param response
     * @return all industry category details with offset 
     */
    @RequestMapping(value = "/offset/", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getAllIndustryCategoryWithOffset(@RequestParam Map<String,String> allRequestParams, @RequestHeader(value = "userToken") String userToken,HttpServletResponse response) {
        return industryCategoryManager.getAllIndustryCategoryWithOffset(allRequestParams, userToken,response);
    }
    
    /**
     * 
     * @param userToken
     * @return all active industry categories
     */
    @RequestMapping(value = "/active", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getAllActiveIndustryCategory(@RequestHeader(value = "userToken") String userToken) {
        return industryCategoryManager.getAllActiveIndustryCategory(userToken);
    }
    
    /**
     * 
     * @param request
     * @param userToken
     * @author: Jyoti, 27-02-2017
     * @return 
     * @purpose:import industry category
     */
    @RequestMapping(value = "/importIndustryCategory", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object importIndustryCategory(MultipartHttpServletRequest request, @RequestHeader(value = "userToken") String userToken){
        Iterator<String> itr = request.getFileNames();
        MultipartFile file = null;
        while (itr.hasNext()) {
            file = request.getFile(itr.next());
            return industryCategoryManager.insertIndustryCategoryByImport(file, userToken);
        }
        return null;
    }
    
}
