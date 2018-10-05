/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.external.service;

import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @date 17-april-2018
 * @author jyoti
 */
@Controller
@RequestMapping("/external")
public class ExternalService {

    ExternalManager externalManager = new ExternalManager();

    /**
     * @param mobileNumber
     * @param fromDate
     * @param toDate
     * @param requestParam
     * @return
     * @added by jyoti, New Feature #29288
     * @purpose Integration of FieldSense Attendance record with inhouse ERP FOR
     * SPECIFIED USER.
     * @date 17-04-2018
     */
    @RequestMapping(value = "/attendance/{mobileNumber}/{fromDate}/{toDate}", method = RequestMethod.GET)
    public @ResponseBody
    Object getAttendanceForUser(@PathVariable("mobileNumber") String mobileNumber, @PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @RequestParam Map<String, String> requestParam) {
        return externalManager.getAttendanceForUser(mobileNumber, fromDate, toDate, requestParam);
    }

    /**
     * @param fromDate
     * @param toDate
     * @param requestParam (authToken)
     * @return
     * @added by jyoti, New Feature #29288
     * @purpose Integration of FieldSense Attendance record with inhouse ERP for
     * ALL USERS. of mentioned authToken (accountId)
     * @date 17-04-2018
     */
    @RequestMapping(value = "/attendance/AllUsers/{fromDate}/{toDate}", method = RequestMethod.GET)
    public @ResponseBody
    Object getAttendanceForAllUser(@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @RequestParam Map<String, String> requestParam) {
        return externalManager.getAttendanceForAllUser(fromDate, toDate, requestParam);
    }

}
