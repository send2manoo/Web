/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.auth.service;

import com.qlc.fieldsense.auth.model.AuthenticationUser;
import com.qlc.fieldsense.auth.model.AuthenticationUserManager;
import com.qlc.fieldsense.auth.model.UserLogin;
import java.sql.Timestamp;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Ramesh
 * @date 30-01-2014
 * @purpose This Class contains all the services to perform authentication .
 */
@Controller
@RequestMapping("/authenticate")
public class AuthenticationUserService {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("AuthenticationUserService");
    AuthenticationUserManager authenticationUserManager = new AuthenticationUserManager();

    /**
     * @param login
     * @return 
     * @purpose used to login from mobile 
     */
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object login(@RequestBody UserLogin login) {
        log4jLog.info("Login through mobile .");
         Timestamp timeStamp=new Timestamp(System.currentTimeMillis());    
//         System.out.println("Start Login for:-"+login.getUserEmailAddress()+"@time="+timeStamp);
         String emailreg = "^[a-zA-Z0-9_.-]*@[a-zA-Z0-9_.-]+(\\.[A-Za-z]+[A-Za-z])$";
        if(login.getUserEmailAddress().matches(emailreg)){
            return authenticationUserManager.authenticateUser(login);
        }else{
            login.setUserMobileNo(login.getUserEmailAddress());
            return authenticationUserManager.authenticateUserMobileNo(login);
        }
    }
    
    /**
     * @Added by jyoti
     * @param login
     * @return 
     * @purpose used to login from mobile [leftslider code merged, for optimization]
     */
    @RequestMapping(value = "/loginV2", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object loginV2(@RequestBody UserLogin login) {
        log4jLog.info("LoginV2 through mobile.");
        String emailreg = "^[a-zA-Z0-9_.-]*@[a-zA-Z0-9_.-]+(\\.[A-Za-z]+[A-Za-z])$";
        if(login.getUserEmailAddress().matches(emailreg)) {
            return authenticationUserManager.authenticateUserEmailAddress_V2(login);
        } else{
            login.setUserMobileNo(login.getUserEmailAddress());
            return authenticationUserManager.authenticateUserMobileNo_V2(login);
        }
    }

    /**
     * @param login
     * @return 
     * @purpose used to login from web
     */
//    @CrossOrigin( origins = {"*"}, maxAge=300 )
    @RequestMapping(value = "/TL", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object loginTL(@RequestBody UserLogin login) {
        log4jLog.info("Login through web .");
        String emailreg = "^[a-zA-Z0-9_.-]*@[a-zA-Z0-9_.-]+(\\.[A-Za-z]+[A-Za-z])$";
        if(login.getUserEmailAddress().matches(emailreg)){
//            System.out.println("inside if");
            return authenticationUserManager.authenticateUserInWeb(login);
        }else{
//            System.out.println("inside else");
            login.setUserMobileNo(login.getUserEmailAddress());
            return authenticationUserManager.authenticateUserInWebMobileNo(login);
        }
    }
    
    /**
     * @added by nikhil
     * @param authenticationUser
     * @return 
     */
    @RequestMapping(value = "/update_terms_condition_agreed", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object Set_terms_condition_agreed(@RequestBody AuthenticationUser authenticationUser) {
         return authenticationUserManager.updateTermsCondition(authenticationUser);  
    }

    /**
     * @param userToken
     * @return logged in users authentication details
     */
//    @CrossOrigin( origins = {"*"}, maxAge=300 )
    @RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getUserAuthDetails(@RequestHeader(value = "userToken") String userToken) {
        return authenticationUserManager.getUserAuthDetails(userToken);
    }

    /**
     * @param userToken
     * @param hasKey
     * @return 
     * @purpose used to log out. hashkey used to differentiate logout from mobile and web. 1 is for mobile and 0 is for web
     */
    @RequestMapping(value = "/{hasKey}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public
    @ResponseBody
    Object logout(@RequestHeader(value = "userToken") String userToken, @PathVariable("hasKey") int hasKey) {
        return authenticationUserManager.logoutUser(userToken, hasKey);
    }

    /**
     * @param login
     * @return first login value 0- first login 1- not first login ,-1- user not registered
     * @purpose used check login is users first login or not
     */
    @RequestMapping(value = "/firstLogin", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object firstLogin(@RequestBody UserLogin login) {
        return authenticationUserManager.userFirstLogin(login);
    }
}
