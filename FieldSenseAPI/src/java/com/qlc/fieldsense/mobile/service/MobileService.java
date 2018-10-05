/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.mobile.service;

import com.qlc.fieldsense.mobile.model.MobileManager;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.io.IOException;
import java.util.Iterator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 *
 * @author Ramesh
 * @date 06-06-2014
 */
@Controller
@RequestMapping("/mobile")
public class MobileService {

    MobileManager mobileManager = new MobileManager();

    /**
     * 
     * @param date
     * @param userToken
     * @return 
     * @purpose Used to get leftslidermenu details for mobile.
     * @Used in Lower version
     */
    @RequestMapping(value = "/leftSliderMenu/{date}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object leftSliderMenuDetails(@PathVariable("date") String date, @RequestHeader(value = "userToken") String userToken) {
             return mobileManager.selectLeftSliderMenuDetails(date, userToken);
    }
    
    /**
     * 
     * @param date
     *  @param versionCode
     * @param userToken
     * @return 
     * @purpose Used to get leftslidermenu details for mobile.
     * @Used in current version
     */
    @RequestMapping(value = "/leftSliderMenu/{date}/{versionCode}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object leftSliderMenuDetails(@PathVariable("date") String date, @PathVariable("versionCode") int versionCode,@RequestHeader(value = "userToken") String userToken) {
       // System.out.println("reaching left of service &&&&&&&&");
        return mobileManager.selectLeftSliderMenu(date, versionCode,userToken);
    }

    /**
     * @Added by JYoti
     * @param date
     * @param versionCode
     * @param userToken
     * @return 
     */
    @RequestMapping(value = "/leftSliderMenu_V2/{date}/{versionCode}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object leftSliderMenu_V2(@PathVariable("date") String date, @PathVariable("versionCode") int versionCode,@RequestHeader(value = "userToken") String userToken) {
        return mobileManager.selectLeftSliderMenu_V2(date, versionCode,userToken);
    }
    
    /**
     * 
     * @param logsFile
     * @return
     * @throws IOException 
     * @purpose Used to send log file from mobile. You have to upload file for this API.
     */
    @RequestMapping(value = "/logssend", method = RequestMethod.POST)
    public
    @ResponseBody
    Object userProfilePic(MultipartHttpServletRequest logsFile) throws IOException {
        Iterator<String> itr = logsFile.getFileNames();
        MultipartFile file = null;
        while (itr.hasNext()) {
            file = logsFile.getFile(itr.next());
            if (mobileManager.sendLogsFileAsEmail(file)) {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Logs Email sent successfully .", "", "");
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Logs Email sent failed .", "", "");
            }
        }
        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Logs Email sent successfully .", "", "");
    }

    /**
     * @author Ramesh
     * @param appType  0- ondroid 1- ios
     * @return Json Object
     * @purpose This method gives the current version of the app in appstore .
     */
    /*
      This API is depreacted and was supported till 2.4 
      now we will directly send response as 9.9 so that user can update to latest version.
    */
    @RequestMapping(value = "/version/{apptype}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object getAppVersion(@PathVariable("apptype") int appType) {
        return mobileManager.selectAppVersion(appType);
    }

    /**
     * @author Ramesh
     * @param appType  0- ondroid 1- ios
     * @return Json Object
     * @purpose This method gives the current version of the app in appstore .
     */
    @RequestMapping(value = "/versionDetails/{apptype}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object getAppVersionDetails(@PathVariable("apptype") int appType) {
        return mobileManager.selectAppVersionDetails(appType);
    }

    
    /**
     * 
     * Upload Debug file from Mobile
     * @param name
     * @param file
     * @param userToken
     * @return 
     * @deprecated  replaced by method uploadFileHandler from version 24
     */
    @Deprecated
    @RequestMapping(value = "/uploadAppDebug", method = RequestMethod.POST)
    public @ResponseBody
    Object uploadMobileDebugFileHandler(@RequestParam("fileName") String name,@RequestParam("file") MultipartFile file,@RequestHeader(value = "userToken") String userToken) {
            return mobileManager.uploadMobileDebugFileHandler( name,file,userToken);
    }
    
    /**
     * Added by Awaneesh to upload Files from Mobile
     * Upload Debug file from Mobile
     * @param name
     * @param fileType
     * @param fileName : 
     * @param file 
     * @param userToken 
     * @return  
     */
    @RequestMapping(value = "/fileUpload/{filetype}", method = RequestMethod.POST)
    public @ResponseBody
    Object uploadFileHandler(@RequestParam("fileName") String name,@PathVariable("filetype") int fileType,@RequestParam("file") MultipartFile file,@RequestHeader(value = "userToken") String userToken) {
            return mobileManager.uploadFileHandler( name,fileType,file,userToken);
    }
        
        
}
