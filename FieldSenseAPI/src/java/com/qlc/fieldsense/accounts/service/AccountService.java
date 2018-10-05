/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.accounts.service;

import com.qlc.fieldsense.accounts.model.Account;
import com.qlc.fieldsense.accounts.model.AccountLinkActivation;
import com.qlc.fieldsense.accounts.model.AccountSettingValues;
import com.qlc.fieldsense.accounts.model.AccountsManager;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.validation.Valid;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Ramesh
 */
@Controller
@RequestMapping("/account")
public class AccountService {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("AccountService");
    AccountsManager accountManager = new AccountsManager();

    /**
     * @param account
     * @param userToken
     * @return
     * @purpose used to create client account
     */
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object createAccount(@Valid @RequestBody Account account, @RequestHeader(value = "userToken") String userToken) {
        return accountManager.createAccount(userToken, account);
    }
    /**
     * 
     * @param userToken
     * @param OTP
     * @return 
     */
     @RequestMapping(value = "/verify/",method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object verify(@RequestParam Map<String,String> allRequestParams,@RequestHeader(value = "userToken") String userToken) {
//         System.out.println("reached ");
         String otp = allRequestParams.get("OTP");
         String mobile = allRequestParams.get("mobile_no");
         String email_add= allRequestParams.get("email_add");
//               return otp+"@ "+mobile+"* "+email_add;
//         System.out.println(" accountManager.verfiyOTP(otp, mobile, email_add):- "+accountManager.verfiyOTP(otp, mobile, email_add));
          return accountManager.verfiyOTP(otp, mobile, email_add);
        
    }
    
    @RequestMapping(value = "/resend/",method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    JSONObject resend(@RequestParam Map<String,String> allRequestParams,@RequestHeader(value = "userToken") String userToken) {
//         System.out.println("reached ");
//         String otp = allRequestParams.get("OTP");
         String mobile = allRequestParams.get("mobile_no");
         String email_add= allRequestParams.get("email_add");
         String Country_code = mobile.substring(0, 2);
//               return otp+"@ "+mobile+"* "+email_add;
//         System.out.println(" accountManager.verfiyOTP(otp, mobile, email_add):- "+accountManager.verfiyOTP(otp, mobile, email_add));
          accountManager.resendOTP(mobile, email_add,Country_code);
         return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " customer ", "got success");
    }
    /**
     *
     * @param account
     * @param id
     * @param userToken
     * @return 
     * @purpose edit client account
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}" ,headers = "Accept=application/json")
    public
    @ResponseBody
    Object editAccount(@RequestBody Account account,@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return accountManager.editAccDetails(userToken,id,account);
    }

    /**
     * @purpose used to get list of all client accounts
     * @return list of all client accounts
     */    
    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectAccount() {
        return accountManager.selctAccounts();
    }

    /**
     * @param id
     * @param userToken
     * @purpose used to get details of particular account based on account id 
     * @return details of particular account
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectAccount(@PathVariable int id,@RequestHeader(value = "userToken") String userToken) {
        return accountManager.selectAccount(userToken,id);
    }

    /**
     * 
     * @param id
     * @param userToken
     * @return 
     * @purpose used to delete particular account based on account id. 
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    Object deleteAccount(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return accountManager.deleteAccount(userToken, id);
    }
    
    /**
     * @param userToken
     * @return 
     * @purpose used to select account settings values (admin user side)
     */
    @RequestMapping(value = "/settings/values", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectAccountSettingsValues(@RequestHeader(value = "userToken") String userToken) {
        return accountManager.selectAccountSettingValues(userToken);
    }
    
    /**
     * @param id
     * @added by jyoti 23-12-2016
     * @param userToken
     * @return 
     * @purpose used to select account settings values by id (super admin user side)
     */
    @RequestMapping(value = "/settings/values/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectAccountSettingsValuesById(@PathVariable int id,@RequestHeader(value = "userToken") String userToken) {
        return accountManager.selectAccountSettingValuesForOffline(userToken, id);
    }
    
    /**
     * @param accountValues
     * @param userToken
     * @return 
     * @purpose used to edit account settings values by id ( admin user side)
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/settings/values" ,headers = "Accept=application/json")
    public
    @ResponseBody
    Object editAccountSettingsValues(@RequestBody AccountSettingValues accountValues,@RequestHeader(value = "userToken") String userToken) {
       
        return accountManager.editAccountSettingValues(accountValues,userToken);
        
    }
    
    /**
     * @param id
     * @added by jyoti 22-12-2016
     * @param accountValues
     * @param userToken
     * @return 
     * @purpose used to edit account settings values by id ( super admin user side)
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/settings/values/{id}" ,headers = "Accept=application/json")
    public
    @ResponseBody
    Object editAccountSettingsValuesById(@RequestBody AccountSettingValues accountValues,@PathVariable int id,@RequestHeader(value = "userToken") String userToken) {
        return accountManager.editAccountSettingValuesForOffline(accountValues,userToken,id);
    }
    
    /**
     * @param userToken
     * @return 
     * @purpose used to get details of all account regions
     */
    @RequestMapping(value = "/regions", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectAllAccountRegions(@RequestHeader(value = "userToken") String userToken) {
        return accountManager.selectAllAccountRegions(userToken);
    }
    
     /**
     * 
     * @param teamId
     * @param parentTeamId
     * @param email
     * @param userToken
     * @return 
     * @purpose Used to delete member from organization chart.
     */
    @RequestMapping(value = "/userAdmin/deleteAdminUser", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object removeFromOrganizationChart(@RequestBody ArrayList<Integer> listOfIds, @RequestHeader(value = "userToken") String userToken) {
        return accountManager.deleteAdminUser(listOfIds,userToken);
    }
    
    /**
     * @Added by Jyoti
     * @param accountLinkActivation
     * @CrossOrigin annotation is used to allow domains (which is mentioned in origins) to send request and get the response.
     * @purpose to send email with encrypted url
     * @return
     */
    @CrossOrigin( origins = {"*"}, maxAge=300 )
    @RequestMapping(value = "/sendMailWithSecureLink", method = RequestMethod.POST )
    public
    @ResponseBody
    Object sendAccountCreationEmail(@RequestBody AccountLinkActivation accountLinkActivation) {
        return accountManager.sendMailWithSecureLink(accountLinkActivation);
    }
    
    /**
     * @Added by Jyoti
     * @param encryptedKey
     * @purpose to verify the link (clicked from email)
     * @return
     */
    @RequestMapping(value = "/verifyLink/{encryptedKey}/link", method = RequestMethod.GET )
    public Object verifyLink(@PathVariable String encryptedKey) {
        return accountManager.verifyLink(encryptedKey);
    }
    
//    /**
//     * @Added by Jyoti
//     * @param model
//     * @param emailAddress
//     * @param redirectPage
//     * @purpose to redirect page for verified link
//     * @return
//     */
//    @RequestMapping(value = "/redirectedUrl/{emailAddress}/{redirectPage}/link", method = RequestMethod.GET)
//    public Object redirectToPage(@PathVariable("emailAddress") String emailAddress, @PathVariable("redirectPage") String redirectPage, Model model) {
//    	model.addAttribute("emailAddress", emailAddress);
//        String url = Constant.WEBSITE_PATH + redirectPage;
//        return "redirect:"+url;
//    }
    
     /**
     * @Added by Jyoti
     * @param model
     * @param encryptedKey
     * @param redirectPage
     * @purpose to redirect page for verified link
     * @return
     */
    @RequestMapping(value = "/redirectedUrl/{encryptedKey}/{redirectPage}/link", method = RequestMethod.GET)
    public Object redirectToPage(@PathVariable("encryptedKey") String encryptedKey, @PathVariable("redirectPage") String redirectPage, Model model) {
//        System.out.println("redirectPage "+redirectPage);
    	model.addAttribute("userEmailKey", encryptedKey);
        String url = Constant.WEBSITE_PATH + redirectPage;
        return "redirect:"+url;
    }
    /**
     * @Added by Jyoti
     * @param accountLinkActivation
     * @return 
     */
    @RequestMapping(value = "/verifyKey", method = RequestMethod.POST )
    public
    @ResponseBody
    Object verifyKeyReturnValidMailAddress(@RequestBody AccountLinkActivation accountLinkActivation) {
        return accountManager.verifyKeyReturnValidMailAddress(accountLinkActivation);
    }

    /**
     * @added by jyoti, remove later     
     * @return 
     */
    @RequestMapping(value = "/generateAuthToken", method = RequestMethod.GET )
    public
    @ResponseBody
    Object removeLaterForCreatingAuthToken() {
        return accountManager.generateAuthToken();
    }
    
    /**
     * @Added by jyoti, to send mail manually using postman
     * @return 
     */
//    @CrossOrigin( origins = {"*"}, maxAge=300 )
//    @RequestMapping(value = "/sendMailWithSecureLink/manually", method = RequestMethod.GET )
//    public
//    @ResponseBody
//    Object sendMailWithSecureLinkManually() {
//        return accountManager.sendMailWithSecureLinkManually();
//    }
    
    /**
     * @Added by Jyoti
     * @param encryptedKey
     * @purpose to verify the link (clicked from email), remove later (after 2 months - from june)
     * @return
     */
    @RequestMapping(value = "/verifyLink/onManual/{encryptedKey}/link", method = RequestMethod.GET )
    public Object verifyLinkOnManual(@PathVariable String encryptedKey) {
        return accountManager.verifyLinkOnManual(encryptedKey);
    }
}
