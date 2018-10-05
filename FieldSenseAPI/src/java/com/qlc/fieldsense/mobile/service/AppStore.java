/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.mobile.service;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import com.qlc.fieldsense.utils.GetApplicationContext;
import com.qlc.fieldsense.mobile.dao.MobileDao;

/**
 *
 * @author Ramesh
 * @date 19-06-2015
 * @purpose This class uses to download the the fieldsense app from the app store
 */
@Controller
@RequestMapping("/dld")
public class AppStore {

    MobileDao mobileDao = (MobileDao) GetApplicationContext.ac.getBean("mobileDao");

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("AppStore");

    /**
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String processForm(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        userAgent = userAgent.toLowerCase();
        
        int appType;
        if (userAgent.contains("android")) {
            appType = 0;
//        } else if (userAgent.contains("ios")) { // commented by jyoti
        } else if (userAgent.contains("iphone")) { // added by jyoti
            appType = 1;
        } else {
            appType = -1;
        }
        String redirectUrl = mobileDao.selectAppStoreURL(appType);
        log4jLog.info("download appstore processForm , redirectUrl :" + redirectUrl + " , userAgent : "+userAgent + " , appType : "+ appType);
        if (appType != -1) {
            return "redirect:" + redirectUrl;
        } else {
            return "";
        }
    }
}
