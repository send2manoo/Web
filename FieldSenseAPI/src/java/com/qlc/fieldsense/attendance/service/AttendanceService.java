/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.attendance.service;

import com.qlc.fieldsense.attendance.model.Attendance;
import com.qlc.fieldsense.attendance.model.AttendanceManager;
import java.sql.Date;
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
 * @author mayank
 * @modified by :anuja
 */
@Controller
@RequestMapping("/attendance")
public class AttendanceService {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("AttendanceService");
    private AttendanceManager attendanceManager = new AttendanceManager();

    /**
     * @param id
     * @param userToken
     * @return 
     * @ structure changed and not in use anymore
     * @deprecated 
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Object attendance(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return attendanceManager.getUserAttendance(id, userToken);
    }

    /**
     * @param date
     * @param userToken
     * @return 
     * @ structure changed and not in use anymore
     * @deprecated 
     */
    @RequestMapping(value = "/date/{date}", method = RequestMethod.GET)
    public @ResponseBody
    Object attendance(@PathVariable Date date, @RequestHeader(value = "userToken") String userToken) {
        return attendanceManager.getAttendance(date, userToken);
    }

    /**
     * @param id
     * @param userToken
     * @param date
     * @return 
     * @ structure changed and not in use anymore
     * @deprecated 
     */
    @RequestMapping(value = "/user/{id}/date/{date}", method = RequestMethod.GET)
    public @ResponseBody
    Object attendance(@PathVariable int id, @PathVariable Date date, @RequestHeader(value = "userToken") String userToken) {
        return attendanceManager.getUserAttendance(id, date, userToken);
    }

    /**
     *
     * @param attendance
     * @param userToken
     * @return 
     * @purpose used to punch in or punch out
     * @deprecated 
     */
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    Object attendance(@RequestBody Attendance attendance, @RequestHeader(value = "userToken") String userToken) {
        return attendanceManager.punch(attendance, userToken);
    }

    /**
     * 
     * @param attendance
     * @param userToken
     * @return 
     * @purpose used to punch in or punch out with LocationNotFoundReason
     */
    @RequestMapping(value = "/locationNotFoundReason", method = RequestMethod.POST)
    public @ResponseBody
    Object attendanceWithLocationNotFoundReason(@RequestBody Attendance attendance, @RequestHeader(value = "userToken") String userToken) {
        return attendanceManager.punchWithLocationNotFoundReason(attendance, userToken);
    }

    /**
     *
     * @param userToken
     * @return punch in time for loggedin user
     * @ author : anuja
     */
    @RequestMapping(value = "/time", method = RequestMethod.GET)
    public @ResponseBody
    Object getPunchInTime(@RequestHeader(value = "userToken") String userToken) {
        return attendanceManager.getPunchInTime(userToken);
    }

    /**
     *
     * @param id
     * @param date
     * @param userToken
     * @return 
     * @purposr : get attendance details time of specific user for specific date
     * @ author : anuja
     */
    @RequestMapping(value = "/time/{id}/{date}", method = RequestMethod.GET)
    public @ResponseBody
    Object getPunchInTimeForUser(@PathVariable(value = "id") int id, @PathVariable(value = "date") String date, @RequestHeader(value = "userToken") String userToken) {
        return attendanceManager.getPunchInTimeForUser(userToken, id, date);
    }
    
    /**
     * @param id
     * @param attendanceId
     * @param userToken
     * @return 
     * @purpose : update punchout details and status, web
     * @author : anuja
     * @date : 24 Jul,2015
     */
    @RequestMapping(value = "/punchoutStatus/{id}/{attendanceId}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public @ResponseBody
    Object updatePunchoutStatus(@PathVariable(value = "id") int id, @PathVariable(value = "attendanceId") int attendanceId,@RequestHeader(value = "userToken") String userToken) {
        return attendanceManager.updatePunchoutStatus(userToken, id,attendanceId);
    }

    /**
     * @param userToken
     * @return 
     * @purpose : update punchout status
     * @author : anuja
     * @date : 24 Jul,2015
     */
    @RequestMapping(value = "/punchoutStatus", method = RequestMethod.PUT, headers = "Accept=application/json")
    public @ResponseBody
    Object updateOnlyPunchOutStatus(@RequestHeader(value = "userToken") String userToken) {
        return attendanceManager.updateOnlyPunchOutStatus(userToken);
    }
    
    /**
     * @param attendanceTimeout
     * @param userToken
     * @return 
     * @purpose : used to set allow timeout for attendance
     */
    @RequestMapping(value = "/timeout/start", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody
    Object startTimeoutForAttendance(@RequestBody com.qlc.fieldsense.attendance.model.AttendanceTimeout attendanceTimeout,@RequestHeader(value = "userToken") String userToken) {
        return attendanceManager.startTimeoutForAttendance(attendanceTimeout,userToken);
    }
    
    /**
     *
     * @param attendanceTimeout
     * @param userToken
     * @return
     */
    @RequestMapping(value = "/timeout/stop", method = RequestMethod.PUT, headers = "Accept=application/json")
    public @ResponseBody
    Object stopTimeoutForAttendance(@RequestBody com.qlc.fieldsense.attendance.model.AttendanceTimeout attendanceTimeout,@RequestHeader(value = "userToken") String userToken) {
        return attendanceManager.stopTimeoutForAttendance(attendanceTimeout,userToken);
    }
    
    /**
     *
     * @param userToken
     * @param userId
     * @param timeoutDate
     * @return
     */
    @RequestMapping(value = "/timeout/{userId}/{timeoutDate}", method = RequestMethod.GET, headers = "Accept=application/json")
    public @ResponseBody
    Object selectAllTimeoutForAttendance(@RequestHeader(value = "userToken") String userToken,@PathVariable(value = "userId") int userId,@PathVariable(value = "timeoutDate") String timeoutDate) {
        return attendanceManager.selectAllTimeoutForAttendance(userId,timeoutDate,userToken);
    }
    
    /**
     * @param id
     * @param userToken
     * @return 
     * @purposr : get last attendance details of specific user 
     * @author : awaneesh
     */
    @RequestMapping(value = "/lastPunchDateTime/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Object getlastPunchDateTimeForUser(@PathVariable(value = "id") int id, @RequestHeader(value = "userToken") String userToken) {
        return attendanceManager.getLastPunchDateTimeForUser(userToken, id);
    }
    
}
