/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customer.service;

import com.qlc.fieldsense.customer.model.Customer;
import com.qlc.fieldsense.customer.model.CustomerManager;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
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
 * @author Ramesh
 * @date 28-01-2014 .
 * @purpouse To manipulate customer related services .
 */
@Controller
@RequestMapping("/customer")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerService {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("CustomerService");
    private CustomerManager customerManager = new CustomerManager();

    /**
     * @param id
     * @param userToken
     * @return customer details based on id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectCustomer(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return customerManager.getCustomer(id, userToken);
    }

    /**
     * 
     * @param id
     * @param userToken
     * @return customer details with its location details based on id.
     */
    @RequestMapping(value = "/selectCustomerWithLocation/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectCustomerWithLocation(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return customerManager.getCustomerWithLocation(id, userToken);
    }

    /**
     * 
     * @param userToken
     * @return list of all customers with its details
     */
    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectCustomers(@RequestHeader(value = "userToken") String userToken) {
        return customerManager.getCustomers(userToken);
    }
        
    /**
     * @param userToken
     * @return list of all customers with its details
     * @purpose  By jyoti- MObile side sending customer list with only names
     */
    @RequestMapping(value = "/allNames",method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectAllCustomersNames(@RequestHeader(value = "userToken") String userToken) {
        return customerManager.getAllCustomersNames(userToken);
    }
    
    /**
     * 
     * @param userToken
     * @return list of all customers with its details
     */
    @RequestMapping(value = "/visit",method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectCustomersInVisit(@RequestParam Map<String,String> allRequestParams) {
        return customerManager.getCustomersInVisit(allRequestParams);
    }

    /**
     * @Added by jyoti
     * @param allRequestParams
     * @return
     */
    @RequestMapping(value = "/onlyWhoHaveVisit", method = RequestMethod.GET)
    public @ResponseBody
    Object selectCustomersOnlyWhoHaveVisit(@RequestParam Map<String, String> allRequestParams) {
        return customerManager.selectCustomersOnlyWhoHaveVisit(allRequestParams);
    }
    
    /**
     * 
     * @param customer
     * @param userToken
     * @return 
     * @purpose used to create customer
     */
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object createCustomer(@Valid @RequestBody Customer customer, @RequestHeader(value = "userToken") String userToken) {
        return customerManager.createCustomer(customer, userToken);
    }

    /**
     * 
     * @param customer
     * @param userToken
     * @return used to update customer 
     */
    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object updateCustomer(@Valid @RequestBody Customer customer, @RequestHeader(value = "userToken") String userToken) {
        return customerManager.updateCustomer(customer, userToken);
    }

    /**
     * 
     * @param id
     * @param userToken
     * @return 
     * @purpose used to delete customer based on id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    Object deleteCustomer(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return customerManager.deleteCustomer(id, userToken);
    }

    /*@RequestMapping(value = "/customerCustomerContact", method = RequestMethod.PUT, headers = "Accept=application/json")
    public @ResponseBody
    Object updateCustomerCustomerContacts(@RequestBody Customer customer, @RequestHeader(value = "userToken") String userToken) {
    return customerManager.updateCustomerCustomerContacts(customer, userToken);
    }*/
    
    /**
     * 
     * @param customer
     * @param userToken
     * @return 
     * @purpose Used to update customer name and address
     */
    @RequestMapping(value = "/Customer", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object updateCustomerNameAddress(@RequestBody Customer customer, @RequestHeader(value = "userToken") String userToken) {
        return customerManager.updateCustomerNameAddress(customer, userToken);
    }

    /**
     * @author Ramesh
     * @date 13-05-2014
     * @param recentType='added' or recentType='modified'
     * @param userToken
     * @return details of recently added or modified customers
     */
    @RequestMapping(value = "recent/{recentType}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectRecentCustomer(@PathVariable String recentType, @RequestHeader(value = "userToken") String userToken) {
        return customerManager.getRecentCustomers(recentType, userToken);
    }

    /**
     * @author anuja
     * @return 
     * @purpose import customer records
     * @param request (csv file)
     * @param userToken
     */
    @RequestMapping(value = "/importCustomer", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object importCustomer(MultipartHttpServletRequest request, @RequestHeader(value = "userToken") String userToken) {
        Iterator<String> itr = request.getFileNames();
        MultipartFile file = null;
        while (itr.hasNext()) {
            file = request.getFile(itr.next());
            return customerManager.createCustomerByImport(file, userToken);
        }
        return null;
    }

    /**
     * @author Ramesh
     * @return 
     * @date 20-08-2014
     * @param customerId
     * @param userToken
     * @purpose If location is not available for customer then insert the location for customer if available nothing to do .
     */
    @RequestMapping(value = "/insertLatLang/{customerId}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object insertCustomerLatLang(@PathVariable int customerId, @RequestHeader(value = "userToken") String userToken) {
        return customerManager.insertCustomerLatLang(customerId, userToken);
    }

    /**
     * @author: anuja
     * @return 
     * @date: 19 Sep, 2014
     * @param userToken
     * @purpose: display list of industry
     *
     */
    @RequestMapping(value = "industry", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectIndustry(@RequestHeader(value = "userToken") String userToken) {
        return customerManager.getCustomersIndustry(userToken);
    }

    /**
     * @author: anuja
     * @return 
     * @date: 19 Sep, 2014
     * @param userToken
     * @purpose: display list of territory
     *
     */
    @RequestMapping(value = "territory", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectTerritory(@RequestHeader(value = "userToken") String userToken) {
        return customerManager.getCustomersTerritory(userToken);
    }

    /**
     * 
     * @param offset
     * @param userToken
     * @return list of all customers with offset
     */
  /*  @RequestMapping(value = "/offset/{offset}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectCustomersWithLimit(@PathVariable("offset") int offset, @RequestHeader(value = "userToken") String userToken) {
        return customerManager.getCustomersWithLimit(offset, userToken);
    }*/

    /**
     * @param allRequestParams
     * @param userToken
     * @param response
     * @return list of all customers with offset
     */
    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public
    @ResponseBody
    Object getCustomersWithLimit(@RequestParam Map<String,String> allRequestParams,@RequestHeader(value = "userToken") String userToken,HttpServletResponse response) {
        return customerManager.getCustomersWithLimit(allRequestParams,userToken,response);
    }
    
    /**
     * 
     * @param allRequestParams
     * @param  userId
     * @param userToken
     * @param response
     * @return list of all customers with offset for specific user
     */
    @RequestMapping(value = "/details/{userId}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object getUserCustomersWithLimit(@RequestParam Map<String,String> allRequestParams,@PathVariable int userId,@RequestHeader(value = "userToken") String userToken,HttpServletResponse response) {
        return customerManager.getUserCustomersWithLimit(allRequestParams,userId,userToken,response);
    }
    
    /**
     *@author:anuja
     *@param customer
     *@param userToken
     * @return 
     *@purpose: update customer address and lat-long
     *@date:18 Dec,2014
     */
    @RequestMapping(value = "/address", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object updateCustomerAddressLatLong(@RequestBody Customer customer, @RequestHeader(value = "userToken") String userToken) {
        return customerManager.updateCustomerAddressLatLong(customer, userToken);
    }

    /**
     * 
     * @param customer
     * @param userToken
     * @return 
     * @purpose Used to update customer address and customer lat long
     */
    @RequestMapping(value = "/addressandlatlong", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object updateCustomerLatLong(@RequestBody Customer customer, @RequestHeader(value = "userToken") String userToken) {
        return customerManager.updateCustomerLatLong(customer, userToken);
}
                  
     /**
     * Added by siddhesh 
     * @param customer
     * @param userToken
     * @return 
     * @purpose Used to update customer address and customer lat long
     */
    @RequestMapping(value = "nearBy/{lat}/{lang}/getNearByCustomers/{radius}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object customerNearBy(@PathVariable double lat,@PathVariable double lang,@PathVariable int radius,@RequestHeader(value = "userToken") String userToken) {
        return customerManager.getCustomerNearBy(lat,lang,radius,userToken);
        
        
        
    }
    
}
