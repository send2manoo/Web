/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.cacheLocation.service;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.log4j.Logger;
import com.qlc.cacheLocation.model.*;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;

/**
 *
 * @author awaneesh
 */
@Controller
@RequestMapping("/cacheLocation")
public class CacheLocationService {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("AccountService");
    CacheLocationManager cacheManager = new CacheLocationManager();

    /*@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
     public
     @ResponseBody
     Object createCacheLocation(@Valid @RequestBody CacheLocation account, @RequestHeader(value = "userToken") String userToken) {
     log4jLog.info("CreateAccount:" + account);
     return null;
     }*/
    /**
     * @param latlng
     * @purpose used to get lat-long from cacheLocation [Hit From Mobile]
     * @return lat-long from cacheLocation
     */
    @RequestMapping(value = "/latlng/{latlng}/json", method = RequestMethod.GET, headers = "Accept=application/json")
    public @ResponseBody
    Object getCacheLocationFromLatLng(@PathVariable("latlng") String latlng) {
        log4jLog.info("getCacheLocationFromLatLng:");
        try {
            double latitude = 0;
            double longitude = 0;
            latitude = Double.parseDouble(latlng.split(",")[0]);
            longitude = Double.parseDouble(latlng.split(",")[1]);
            String address = cacheManager.getLocationFromLatLongSats(latitude, longitude,0,0);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " Address ", address);
        } catch (NumberFormatException e) {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "  Enter Valid Latitude and Longitude ", "", "");
        } catch (Exception e) {
            log4jLog.info("getCacheLocationFromLatLng " + e);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "  Please try again . ", "", "");
        }
    }

}
