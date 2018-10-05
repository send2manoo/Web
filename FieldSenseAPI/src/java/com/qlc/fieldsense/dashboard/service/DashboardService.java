/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.dashboard.service;


import com.qlc.fieldsense.dashboard.model.DashboardManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author manohar
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardService {
    

    DashboardManager dashboardManager = new DashboardManager();

//    @RequestMapping(method = RequestMethod.GET)
//    public
//    @ResponseBody
//    Object selectDashboardData(@RequestHeader(value = "userToken") String userToken,@PathVariable("teamId") int teamId) {
//         return dashboardManager.selectDashboardData(userToken,teamId);
//    }
    
    @RequestMapping(value = "/{teamId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getUser(@PathVariable("teamId") int teamId, @RequestHeader(value = "userToken") String userToken) {
        return dashboardManager.selectDashboardData(userToken,teamId);
    }
 
}
