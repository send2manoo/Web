/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.travels.service;

import com.qlc.fieldsense.travels.model.Travels;
import com.qlc.fieldsense.travels.model.TravelsManager;
import javax.validation.Valid;
import org.apache.log4j.Logger;
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
 * @ structure changed and not in use anymore
 */
@Controller
@RequestMapping("/travel")
public class TravelsService {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("TravelsService");
    TravelsManager travelsManager = new TravelsManager();

    /**
     * 
     * @param travels
     * @param userToken
     * @return 
     */
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody
    Object createTravels(@Valid @RequestBody Travels travels, @RequestHeader(value = "userToken") String userToken) {
        return travelsManager.createTravels(travels, userToken);
    }

    /**
     * 
     * @param userToken
     * @return 
     */
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Object selectTravels(@RequestHeader(value = "userToken") String userToken) {
        return travelsManager.selectTravels(userToken);
    }
    
    /**
     * 
     * @param userId
     * @param userToken
     * @return 
     */
    @RequestMapping(value ="/{userId}",method = RequestMethod.GET)
    public @ResponseBody
    Object selectTravel(@PathVariable int userId,@RequestHeader(value = "userToken") String userToken) {
        return travelsManager.selectTravels(userId,userToken);
    }
}
