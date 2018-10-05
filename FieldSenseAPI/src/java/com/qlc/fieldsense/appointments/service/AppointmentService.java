/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.appointments.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qlc.fieldsense.appointments.model.Appointment;
import com.qlc.fieldsense.appointments.model.AppointmentManager;
import java.util.ArrayList;
import java.util.List;
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
 * @author Ramesh
 * @date 21-02-2014
 */
@Controller
@RequestMapping("/appointment")
public class AppointmentService {

    AppointmentManager appointmentManager = new AppointmentManager();
    
    /**
     * 
     * @param appointment
     * @param userToken
     * @purpose used to create appointment in web  
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object createAppointment(@RequestBody Appointment appointment, @RequestHeader(value = "userToken") String userToken) {
        return appointmentManager.createAppointment(appointment, userToken);
    }

    /**
     * 
     * @param appointment
     * @param userToken
     * @purpose used to create appointment in mobile 
     */
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object createAppointmentMobile(@RequestBody Appointment appointment, @RequestHeader(value = "userToken") String userToken) {
        return appointmentManager.createAppointmentMobile(appointment, userToken);
    }

    /**
     * 
     * @param date
     * @param userToken
     * @purpose Used to get list of all appointments of specific users for particular date. 
     */
    @RequestMapping(value = "/appointments/{date}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectAppointments(@PathVariable String date, @RequestHeader(value = "userToken") String userToken) {
        return appointmentManager.selectAppointments(date, userToken);
    }

    /**
     * 
     * @param date
     * @param userToken
     * @purpose Used to get list of all appointments of specific users for particular date in mobile.
     */
    @RequestMapping(value = "/mobileAppointments/{date}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectAppointmentsForMobile(@PathVariable String date, @RequestHeader(value = "userToken") String userToken) {
//        System.out.println("nikhil");
        return appointmentManager.selectAppointmentsForMobile(date, userToken);
    }

    
/**
     * Added by Jyoti, 11-april-2017
     * @param date
     * @param userId
     * @purpose Used to get list of all appointments of specific users for particular date in mobile (used for myTeam Subordinate's visits).
     */
    @RequestMapping(value = "/mobileAppointments/{date}/{userId}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectAppointmentsForMobile(@PathVariable String date, @PathVariable int userId, @RequestHeader(value = "userToken") String userToken) {
        return appointmentManager.selectAppointmentsForMobile(date, userId, userToken);
    }
    
    
    /**
     * 
     * @param id
     * @param userToken
     * @purpose Used to get details of particular appointment based on id. 
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectAppointment(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return appointmentManager.selectAppointment(id, userToken);
    }

    /**
     * 
     * @param id
     * @param userToken
     * @purpose Used to delete specific appointment based on id.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    Object deleteAppointment(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return appointmentManager.deleteAppointmentFromMobile(id, userToken);
    }
    
//    /**
//     * 
//     * @param id
//     * @param userToken
//     * @purpose Used to delete specific appointment based on id.
//     */
//    @RequestMapping(value = "/{id}/{loginId}/{assignedTo}/{activity}", method = RequestMethod.DELETE)
//    public
//    @ResponseBody
//    Object deleteAppointment(@PathVariable int id,@PathVariable int loginId,@PathVariable int assignedTo, @PathVariable String activity,@RequestHeader(value = "userToken") String userToken) {
//        return appointmentManager.deleteAppointment(id,loginId,assignedTo,activity,userToken);
//    }
    /**
     * 
     * @param appointment
     * @param id
     * @param loginId
     * @param assignedTo
     * @param userToken
     * @return 
     * @purpose Used to delete specific appointment based on id.
     */
    @RequestMapping(value = "/{id}/{loginId}/{assignedTo}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public
    @ResponseBody
    Object deleteAppointment(@RequestBody Appointment appointment, @PathVariable int id,@PathVariable int loginId,@PathVariable int assignedTo,@RequestHeader(value = "userToken") String userToken) {
        
        return appointmentManager.deleteAppointment(id,loginId,assignedTo,appointment,userToken);
    }
    /**
     * 
     * @param appointment
     * @param userToken
     * @purpose used to update appointment in web. 
     */
    @RequestMapping(value = "/web", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object updateAppointment(@RequestBody Appointment appointment, @RequestHeader(value = "userToken") String userToken) {
        return appointmentManager.updateAppointment(appointment, userToken);
    }

    /**
     * 
     * @param appointment
     * @param userToken
     * @purpose used to update appointment in mobile 
     */
    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object updateAppointmentMobile(@RequestBody Appointment appointment, @RequestHeader(value = "userToken") String userToken) {
        return appointmentManager.updateAppointmentMobile(appointment, userToken);
    }

    /**
     * 
     * @param appointments
     * @param userToken
     * @purpose used to update appointment position in mobile 
     */
    @RequestMapping(value = "/possition", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object updateAppointmentPossition(@RequestBody String appointments, @RequestHeader(value = "userToken") String userToken) {
        Gson gsonObj = new Gson();
        java.lang.reflect.Type collectionType = new TypeToken<List<Appointment>>() {
        }.getType();
        ArrayList<Appointment> appointmentList = gsonObj.fromJson(appointments, collectionType);
        return appointmentManager.sortApointments(appointmentList, userToken);
    }

    /**
     * 
     * @param appointment
     * @param userToken
     * @purpose  used to update appointment outcome and set next appointment
     */
    @RequestMapping(value = "/outcomeNextAppointment/update", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object updateAppointmentOutComeAndNextAppointment(@RequestBody Appointment appointment, @RequestHeader(value = "userToken") String userToken) {
        return appointmentManager.updateAppointmentOutcomeAndNextAppointment(appointment, userToken);
    }

    /**
     * 
     * @param appointment
     * @param userToken
     * @purpose  used to update appointment outcome and set next appointment
     */
    @RequestMapping(value = "/outcomeNextAppointment", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object updateAppointmentOutComeAndNextAppointmentMobile(@RequestBody Appointment appointment, @RequestHeader(value = "userToken") String userToken) {
        return appointmentManager.updateAppointmentOutcomeAndNextAppointmentMobile(appointment, userToken);
    }

    /**
     * 
     * @param teamId
     * @param userId
     * @param date
     * @param userToken
     * @return list of appointments of specific user for specific day
     */
    @RequestMapping(value = "/team/{teamId}/user/{userId}/{date}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectAppointForUser(@PathVariable int teamId, @PathVariable int userId, @PathVariable("date") String date, @RequestHeader(value = "userToken") String userToken) {
        return appointmentManager.selectAppointForUser(date, teamId, userId, userToken);
    }

    /**
     * 
     * @param customerId
     * @param appointmentId
     * @param userToken
     * @return  list of all previous appointments of of particular appointment
     */
    @RequestMapping(value = "/customer/{customerId}/prior/{appointmentId}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectAppointmentsPriorToAppointment(@PathVariable("customerId") int customerId, @PathVariable("appointmentId") int appointmentId, @RequestHeader(value = "userToken") String userToken) {
        return appointmentManager.selectAppointmentsPriorToAppointment(customerId, appointmentId, userToken);
    }
    
    /**
     * 
     * @param customerId
     * @param appointmentId
     * @param userToken
     * @return  list of all previous appointments of of particular appointment
     */
    @RequestMapping(value = "/getAllcustomer/{customerId}/prior/{appointmentId}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectAllAppointmentsPriorToAppointment(@PathVariable("customerId") int customerId, @PathVariable("appointmentId") int appointmentId, @RequestHeader(value = "userToken") String userToken) {
        return appointmentManager.selectAllAppointmentsPriorToAppointment(customerId, appointmentId, userToken);
    }

    /**
     * 
     * @param date
     * @param teamId
     * @param userId
     * @param userToken
     * @return list of appointments of particular user for particular date
     */
    @RequestMapping(value = "/{date}/team/{teamId}/user/{userId}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectAppointmentsDateWiseForUser(@PathVariable String date, @PathVariable int teamId, @PathVariable int userId, @RequestHeader(value = "userToken") String userToken) {
        return appointmentManager.selectAppointmentsDateWiseForUser(date, teamId, userId, userToken);
    }

    /**
     * 
     * @param customerId
     * @param userToken
     * @return list of appointments of specific customer
     */
    @RequestMapping(value = "/customer/{customerId}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectAppointmentsForCustomer(@PathVariable("customerId") int customerId, @RequestHeader(value = "userToken") String userToken) {
        return appointmentManager.selectAppointmentsForCustomer(customerId, userToken);
    }

    /**
     * 
     * @param appointment
     * @param userToken
     * @purpose used to update appointment
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object updateOneAppointment(@RequestBody Appointment appointment, @RequestHeader(value = "userToken") String userToken) {
        return appointmentManager.updateOneAppointment(appointment, userToken);
    }

    /**
     * 
     * @param userId
     * @param date
     * @param userToken
     * @return list of appointments of user for particular date.
     */
    @RequestMapping(value = "/user/{userId}/{date}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectAppointForUserWithDate(@PathVariable int userId, @PathVariable("date") String date, @RequestHeader(value = "userToken") String userToken) {
        return appointmentManager.selectAppointForUserWithDate(date, userId, userToken);
    }
    
    /**
     * 
     * @param appointment
     * @param userToken
     * @purpose used to update appointment time , used in check in and checkout
     */
    @RequestMapping(value = "/updateTime", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object updateOneAppointmentTime(@RequestBody Appointment appointment, @RequestHeader(value = "userToken") String userToken) {
        return appointmentManager.updateOneAppointmentTime(appointment, userToken);
    }
    
    /**
     * @param customerId
     * @Added by jyoti
     * @param userId
     * @param userToken
     * @return
     */
    @RequestMapping(value = "relevantCustomer/user/{userId}/{customerId}", method = RequestMethod.GET)
    public @ResponseBody
    Object selectAppointForRelevantCustomerOfUser(@PathVariable int userId, @PathVariable int customerId, @RequestHeader(value = "userToken") String userToken) {
        return appointmentManager.selectAppointForRelevantCustomerOfUser(userId, customerId, userToken);
    }
    
    // Rework start on 10-april-2018
    /**
     * Added by Jyoti
     *
     * @param appointment
     * @param userToken
     * @return 
     * @purpose used to create appointment in web for multiple users
     */
    @RequestMapping(value = "/create/multipleUsers", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody
    Object createAppointmentForMultipleUsers(@RequestBody Appointment appointment, @RequestHeader(value = "userToken") String userToken) {
        return appointmentManager.createAppointmentForMultipleUsers(appointment, userToken);
    }

    /* start , Added by jyoti, service related to recurring visits */
    /**
     * Added by Jyoti
     *
     * @param appointment
     * @param userToken
     * @return
     * @purpose used to create recurring appointment in web
     */
    @RequestMapping(value = "/create/recurring", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody
    Object createAppointmentRecurring(@RequestBody Appointment appointment, @RequestHeader(value = "userToken") String userToken) {
        return appointmentManager.createAppointmentRecurring(appointment, userToken);
    }

    /**
     * Added by Jyoti, 06-march-2017
     *
     * @param appointmentRecurringId
     * @param id
     * @param loginId
     * @param assignedTo
     * @param activity
     * @param userToken
     * @return
     * @purpose to delete (recurring) selected visit and its following visit
     */
    @RequestMapping(value = "followingRecurringVisits/{appointmentRecurringId}/{id}/{loginId}/{assignedTo}/{activity}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object deleteThisAndFollowingAppointment(@PathVariable int appointmentRecurringId, @PathVariable int id, @PathVariable int loginId, @PathVariable int assignedTo, @PathVariable String activity, @RequestHeader(value = "userToken") String userToken) {
        return appointmentManager.deleteThisAndFollowingAppointment(appointmentRecurringId, id, loginId, assignedTo, activity, userToken);
    }

    /**
     * Added by Jyoti, 08-march-2017
     *
     * @param appointment
     * @param userToken
     * @return
     * @purpose used to update recurring selected and following appointment
     */
    @RequestMapping(value = "/update/recurringVisits", method = RequestMethod.PUT, headers = "Accept=application/json")
    public @ResponseBody
    Object updateRecurringAppointment(@RequestBody Appointment appointment, @RequestHeader(value = "userToken") String userToken) {
        return appointmentManager.updateRecurringAppointment(appointment, userToken);
    }

    /* end , service related to recurring visits */
    // rework end
    
      @RequestMapping(value = "/appointmentCreate",method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object createAppointmentMobileList(@RequestBody Appointment appointment, @RequestHeader(value = "userToken") String userToken) {
        return appointmentManager.createAppointmentMobileList(appointment, userToken);
    }
    
}
