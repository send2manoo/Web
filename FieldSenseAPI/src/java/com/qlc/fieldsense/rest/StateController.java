package com.qlc.fieldsense.rest;

import static com.qlc.fieldsense.report.dao.ReportsDaoImpl.log4jLog;
import com.qlc.fieldsense.utils.APNPushNotifications;
import com.qlc.fieldsense.utils.PushNotifications;
import java.util.HashMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author jyoti
 */
@Controller
@RequestMapping("/state")
public class StateController {

    /**
     *
     * @param code
     * @return
     */
    @RequestMapping(value = "/{code}", method = RequestMethod.GET)
    public
    @ResponseBody
    String getState(@PathVariable String code) {

        String result;
        if (code.equals("KL")) {
            result = "Kerala";

        } else if (code.equals("RJ")) {
            result = "Rajasthan";

        } else if (code.equals("MH")) {
            result = "Maharashtra";

        } else {
            result = "Other State";
        }
        return result;

    }

    /**
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    String getState() {

        StringBuilder result = new StringBuilder();
        result.append("Kerala");
        result.append(", Rajasthan");
        result.append(", Maharashtra");
        return result.toString();

    }

    /**
     *
     * @param name
     * @param id
     * @return
     */
    @RequestMapping(value = "/{name}", method = RequestMethod.PUT)
    public
    @ResponseBody
    String putState(@PathVariable String name, @RequestHeader(value = "Id") String id) {

        String result = name + " " + id;

        return result;

    }

    /**
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    String postState(@RequestHeader(value = "Id") String id) {

        String result = id + " is added";//Insert data

        return result;

    }

    /**
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public
    @ResponseBody
    String deleteState(@RequestParam(value = "Id") String id) {

        String result = id + " is deleted";//Delete data 

        return result;

    }
    
    /**
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/notification", method = RequestMethod.POST,headers = "Accept=application/json")
    public
    @ResponseBody
    String testNotification(@RequestBody HashMap<String,String> map) {
        String deviceToken= map.get("deviceToken");
        String message= map.get("message");

        try{
        PushNotifications apn= null;
        apn= new APNPushNotifications();
        apn.activityNotifications(message, deviceToken);
        return "Notification Sent to '"+deviceToken+"'. Message is:"+message;

        }catch(Exception e){
            log4jLog.info(" testNotification " + e);
            e.printStackTrace();
            return "excpetion :"+e.getMessage();
        }
        //String result = name + " " + id;


    }
}
