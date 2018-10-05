/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.password.service;

import com.qlc.fieldsense.password.model.Password;
import com.qlc.fieldsense.password.model.PasswordManager;
import org.apache.log4j.Logger;
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
 * @author anuja
 */
@Controller
@RequestMapping("/password")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PasswordService {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("ForgotPasswordService");
    PasswordManager passwordManager = new PasswordManager();
    
/**
 * 
 * @param email
     * @return 
 * @purpose Used to send forgot password request.
 */
    @RequestMapping(value = "/forgot", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object forgotPassword(@RequestBody Password email) {
        return passwordManager.forgotPassword(email);
    }

    /**
     * 
     * @param pass
     * @return 
     * @purpose Used to reset password.
     */
    @RequestMapping(value = "/reset", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object resetPassword(@RequestBody Password pass) {
        return passwordManager.resetPassword(pass);
    }

    /**
     * 
     * @param email
     * @return 
     * @purpose Used to set password.
     * @deprecated 
     */
    @RequestMapping(value = "/set", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object setPassword(@RequestBody Password email) {
        return passwordManager.changePasswordEmail(email);
    }

    /**
     * 
     * @param password
     * @param userToken
     * @return 
     * @purpose Used to change password. 
     */
    @RequestMapping(value = "/change", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object changePassword(@RequestBody Password password, @RequestHeader(value = "userToken") String userToken) {
        return passwordManager.changePassword(password, userToken);
    }
    
    /**
     *
     * @param mobileNo
     * @return
     */
    @RequestMapping(value = "/getOTP/{mobile}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getOTP(@PathVariable("mobile") String mobileNo) {
        return passwordManager.getOTP(mobileNo);
    }
    
    /**
     *
     * @param authObject
     * @return
     */
    @RequestMapping(value = "/verifyOTP", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object verifyOTP(@RequestBody java.util.HashMap authObject) {
        return passwordManager.verifyOTP(authObject);
    }
}
