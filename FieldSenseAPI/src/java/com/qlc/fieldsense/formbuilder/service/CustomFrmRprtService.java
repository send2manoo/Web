/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.formbuilder.service;

import com.qlc.fieldsense.formbuilder.model.CustomFrmRprtManager;
import com.qlc.fieldsense.formbuilder.model.FilledForm;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
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
 * @author pallavi.s
 */
@Controller
@RequestMapping("/CustomFormsReport")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomFrmRprtService {

    private final CustomFrmRprtManager custFrmRprtManager = new CustomFrmRprtManager();
    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");

    /**
     *
     * @param formdetails
     * @param userToken
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody
    Object insertFormFilledData(@RequestBody FilledForm formdetails, @RequestHeader(value = "userToken") String userToken) {
        return custFrmRprtManager.insertCustomFormDetails(formdetails, userToken);
    }

    /**
     *
     * @param id
     * @param userId
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return
     */
    @RequestMapping(value = "{id}/{userId}/{fromDate}/{toDate}", method = RequestMethod.GET)
    public @ResponseBody
    Object getFormFilledData(@PathVariable("id") int id, @PathVariable("userId") int userId, @PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @RequestHeader(value = "userToken") String userToken) {
        return custFrmRprtManager.getFrmFilledData(id, userId, fromDate, toDate, userToken);
    }

    /**
     *
     * @param id
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return
     */
    @RequestMapping(value = "{id}/Admin/All/{fromDate}/{toDate}", method = RequestMethod.GET)
    public @ResponseBody
    Object getAllAdminFrmFilledData(@PathVariable("id") int id, @PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @RequestHeader(value = "userToken") String userToken) {
        return custFrmRprtManager.getAllAdminFrmFilledData(id, fromDate, toDate, userToken);
    }

    /**
     *
     * @param id
     * @param userId
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return
     */
    @RequestMapping(value = "{id}/User/{userId}/All/{fromDate}/{toDate}", method = RequestMethod.GET)
    public @ResponseBody
    Object getAllUserFormFilledData(@PathVariable("id") int id, @PathVariable("userId") int userId, @PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @RequestHeader(value = "userToken") String userToken) {
        return custFrmRprtManager.getAllUserFrmFilledData(id, userId, fromDate, toDate, userToken);
    }

    /**
     * @author Ramesh
     * @date 17-06-1987
     * @param customFormImages
     * @param userToken
     * @return
     * @purpose add image for expense while creating a image
     */
    @RequestMapping(value = "/image/save", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody
    Object customFormImages(MultipartHttpServletRequest customFormImages, @RequestHeader(value = "userToken") String userToken) {
        Iterator<String> itr = customFormImages.getFileNames();
        MultipartFile file = null;
        int userId = fieldSenseUtils.userIdForToken(userToken);
        Date date = new Date();
        String imageName;
        String imageFileName;
        String saveDirectory;
        int i = 0;
        List<Map<String, String>> imageList = new ArrayList<Map<String, String>>();
        while (itr.hasNext()) {
            // imageName = userId + "_" + date.getTime() +i+ ".png";
            ///saveDirectory = Constant.CUSTOM_FORM_IMAGE_UPLOAD_PATH + imageName;
            try {
                Map<String, String> imageMapList = new HashMap<String, String>();
                file = customFormImages.getFile(itr.next());
                imageName = userId + "_" + date.getTime() + i + ".png";
                imageFileName = file.getOriginalFilename();
                saveDirectory = Constant.CUSTOM_FORM_IMAGE_UPLOAD_PATH + imageName;
                file.transferTo(new File(saveDirectory));
                imageMapList.put("imageFileName", imageFileName);
                imageMapList.put("imageName", imageName);
                imageList.add(imageMapList);
                //return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Image uploded successfully .", " image name ", imageName);
            } catch (Exception ex) {
                ex.printStackTrace();
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Image not uploaded. Please try again", "", "");
            }
            i++;
        }
        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Image uploded successfully .", " imagenames list ", imageList);
        //return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Image not uploaded. Please try again", "", "");
    }

    /**
     * @Added by vaibhav, modified by jyoti
     * @purpose used to zip the data for all users selected from admin panel
     * @param formid
     * @param fromDate
     * @param toDate
     * @param userToken
     * @param response
     * @return
     */
    @RequestMapping(value = "/downloadaszip/{id}/Admin/All/{fromDate}/{toDate}", method = RequestMethod.GET)
    public @ResponseBody
    Object getAdminAllFrmFilledDataAsZip(@PathVariable("id") int formid, @PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @RequestHeader(value = "userToken") String userToken, HttpServletResponse response) {
        return custFrmRprtManager.getAllAdminFrmFilledDataAsZip(formid, fromDate, toDate, userToken);
    }

    /**
     * @Added by vaibhav, modified by jyoti
     * @purpose used to zip the data for specified user
     * @param formId
     * @param userId
     * @param fromDate
     * @param toDate
     * @param userToken
     * @param response
     * @return
     */
    @RequestMapping(value = "/downloadaszip/{id}/{userId}/{fromDate}/{toDate}", method = RequestMethod.GET)
    public @ResponseBody
    Object getFormFilledDataAsZip(@PathVariable("id") int formId, @PathVariable("userId") int userId, @PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @RequestHeader(value = "userToken") String userToken, HttpServletResponse response) {
        return custFrmRprtManager.getFormFilledDataAsZip(formId, userId, fromDate, toDate, userToken);
    }

    /**
     * @added by vaibhav
     * @purpose used to zip the data for all users selected from user panel
     * @param id
     * @param userId
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return
     */
    @RequestMapping(value = "/downloadaszip/{id}/User/{userId}/All/{fromDate}/{toDate}", method = RequestMethod.GET)
    public @ResponseBody
    Object getAllUserFormFilledDataAsZip(@PathVariable("id") int id, @PathVariable("userId") int userId, @PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @RequestHeader(value = "userToken") String userToken, HttpServletResponse response) {
        return custFrmRprtManager.getAllUserFrmFilledDataAsZip(id, userId, fromDate, toDate, userToken);
    }

}
