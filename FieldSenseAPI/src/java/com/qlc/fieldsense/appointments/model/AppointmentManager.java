/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.appointments.model;

import com.qlc.fieldsense.appointments.dao.AppointmentDao;
import com.qlc.fieldsense.customer.dao.customerDao;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.userKey.dao.UserKayDaoImpl;
import com.qlc.fieldsense.userKey.model.UserKey;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import com.qlc.fieldsense.utils.PushNotificationManager;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javapns.notification.PushNotificationBigPayload;
import javapns.notification.PushNotificationPayload;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

/**
 *
 * @author Ramesh
 * @date 21-02-2014
 */
public class AppointmentManager {

    AppointmentDao appointmentDao = (AppointmentDao) GetApplicationContext.ac.getBean("appointmentDaoImpl");
    customerDao customerDao = (customerDao) GetApplicationContext.ac.getBean("customerDaoImpl");
    UserKayDaoImpl userKeyDao = (UserKayDaoImpl) GetApplicationContext.ac.getBean("userKayDaoImpl");
    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
    public static final Logger log4jLog = Logger.getLogger("AppointmentManager");

    /**
     *
     * @param appointment
     * @param userToken
     * @return 
     * @purpose used to create appointment in web
     */
    public Object createAppointment(Appointment appointment, String userToken) {
        try{ // added by jyoti
        if (fieldSenseUtils.isTokenValid(userToken)) {
            appointment.setDateTime(fieldSenseUtils.converDateToTimestamp(appointment.getSdateTime()));
            appointment.setEndTime(fieldSenseUtils.converDateToTimestamp(appointment.getSendTime()));
            if (!appointment.getScheckInTime().equals("")) {
                appointment.setCheckInTime(fieldSenseUtils.converDateToTimestamp(appointment.getScheckInTime()));
            }
            if (!appointment.getScheckOutTime().equals("")) {
                appointment.setCheckOutTime(fieldSenseUtils.converDateToTimestamp(appointment.getScheckOutTime()));
            }
            int accountId = fieldSenseUtils.accountIdForToken(userToken);
                int userId = fieldSenseUtils.userIdForToken(userToken);
                int appointmentId = appointmentDao.createAppointment(appointment, accountId);
                if (appointmentId != 0) {
                    
//                    if (appointment.getAssignedTo().getId() != appointment.getCreatedBy().getId()) {  // commented by jyoti, mobile team requirment for push notification send for self created visit
//                    }
                        int appointmentPosstionId = appointmentDao.getUserMaxAppointmentPossition(userId, accountId);
//                        System.out.println("appointmentPosstionId : "+ appointmentPosstionId);
                        appointmentDao.insertAppointmentPossition(appointmentId, userId, appointmentPosstionId + 1, accountId);
                        appointment = appointmentDao.selectAppointment(appointmentId, accountId);

                    // Added by jyoti,  11-01-2018
//                            System.out.println("appointmentid : "+appointmentId+" , assigned id : "+appointment.getAssignedTo().getId()+ " , ownerid : "+appointment.getOwner().getId());
                    java.util.HashMap gcmInfo = fieldSenseUtils.getGcmUserInfo(appointment.getAssignedTo().getId());
                    String gcmId =(String)gcmInfo.get("gcmId");
                    int deviceOS =(Integer)gcmInfo.get("deviceOS");
                    int appVersion = (Integer) gcmInfo.get("appVersion"); // added by jyoti
                    String NotificationMessage=null;// added by sanchita 29-08-2018
//                    System.out.println("createAppointment, appVersion : "+appVersion + " , if > "+Integer.parseInt(Constant.APP_VERSION_CHECK));
                    if(appVersion > Integer.parseInt(Constant.APP_VERSION_CHECK) && deviceOS == 1){
                        if(!(gcmId.equals("0") || gcmId.isEmpty())) {
                            Map<String, Object> m_data = new HashMap<String, Object>();
                            if(appointment.getTitle().isEmpty()){  // to solve blank title issue changed to set purpose.
                                m_data.put("message", appointment.getPurpose().getPurpose() + " is created");
                            } else {
                                m_data.put("message", appointment.getTitle() + " is created");
                            }
                            m_data.put("type", "activity");
                            m_data.put("visitStatus", "0"); // 0=new, 1=update, 2=delete
                            m_data.put("id", appointment.getId()); 
                            m_data.put("nextAppointmentTime", appointment.getNextAppointmentTime()); 
                            m_data.put("nextAppointmentType", appointment.getNextAppointmentType());                            
                            m_data.put("title", appointment.getTitle());                    
                            m_data.put("dateTime", appointment.getDateTime()); 
                            m_data.put("sdateTime", appointment.getSdateTime()); 
                            m_data.put("endTime", appointment.getEndTime());
                            m_data.put("sendTime", appointment.getSendTime()); 
                            m_data.put("assignedTo", appointment.getAssignedTo().getId());
                            m_data.put("description", appointment.getDescription()); 
                            m_data.put("appointmentType", appointment.getType()); 
                            m_data.put("outcome", appointment.getOutcome());
                            m_data.put("status", appointment.getStatus()); 
                            m_data.put("checkInLat",appointment.getCheckInLat()); 
                            m_data.put("checkInLong",appointment.getCheckInLong());
                            m_data.put("checkInLocation",appointment.getCheckInLocation());
                            m_data.put("checkInTime",appointment.getCheckInTime());
                            m_data.put("scheckInTime",appointment.getScheckInTime());
                            m_data.put("checkOutLat",appointment.getCheckOutLat());
                            m_data.put("checkOutLong", appointment.getCheckOutLong());
                            m_data.put("checkOutLocation", appointment.getCheckOutLocation());
                            m_data.put("checkOutTime",appointment.getCheckOutTime());
                            m_data.put("scheckOutTime",appointment.getScheckOutTime());
                            m_data.put("nextAppointment",appointment.getNextAppointment());
                            m_data.put("createdOn",appointment.getCreatedOn());
                            m_data.put("appointmentPosition",appointment.getAppointmentPosition());
                            m_data.put("hasNextAppointment",appointment.isHasNextAppointment());                    
                            m_data.put("ownerId", appointment.getOwner().getId());
                            m_data.put("purposeId", appointment.getPurpose().getId());
                            m_data.put("purposeName", appointment.getPurpose().getPurpose());
                            m_data.put("createdBy",appointment.getCreatedBy().getId());
                            m_data.put("customerId", appointment.getCustomer().getId());
                            m_data.put("customerLat",appointment.getCustomer().getLasknownLatitude());
                            m_data.put("customerLong",appointment.getCustomer().getLasknownLangitude());
                            m_data.put("customerName", appointment.getCustomer().getCustomerName());
                            if (appointment.getCreatedOn().before(appointment.getUpdated_on())) {
                                m_data.put("isModified", "true");
                            } else {
                                m_data.put("isModified", "false");
                            }
//                            System.out.println("m_data : "+ m_data);
                            PushNotificationManager push= new PushNotificationManager();
                            push.addEditNotification(m_data, gcmId, deviceOS,null);
                        }
                    } else if (deviceOS == 2) {
                         if(!(gcmId.equals("0") || gcmId.isEmpty())) {
                            JSONObject dataJsonObject = new JSONObject();
                            try
                            {                            
                            dataJsonObject.put("visitStatus", "0"); // 0=new, 1=update, 2=delete
                            dataJsonObject.put("id", appointment.getId()); 
                            dataJsonObject.put("nextAppointmentTime", appointment.getNextAppointmentTime()); 
                            dataJsonObject.put("nextAppointmentType", appointment.getNextAppointmentType());                            
                            dataJsonObject.put("title", appointment.getTitle());                    
                            dataJsonObject.put("dateTime", appointment.getDateTime()); 
                            dataJsonObject.put("sdateTime", appointment.getSdateTime()); 
                            dataJsonObject.put("endTime", appointment.getEndTime());
                            dataJsonObject.put("sendTime", appointment.getSendTime()); 
                            dataJsonObject.put("assignedTo", appointment.getAssignedTo().getId());
                            dataJsonObject.put("description", appointment.getDescription()); 
                            dataJsonObject.put("appointmentType", appointment.getType()); 
                            dataJsonObject.put("outcome", appointment.getOutcome());
                            dataJsonObject.put("status", appointment.getStatus()); 
                            dataJsonObject.put("checkInLat",appointment.getCheckInLat()); 
                            dataJsonObject.put("checkInLong",appointment.getCheckInLong());
                            dataJsonObject.put("checkInLocation",appointment.getCheckInLocation());
                            dataJsonObject.put("checkInTime",appointment.getCheckInTime());
                            dataJsonObject.put("scheckInTime",appointment.getScheckInTime());
                            dataJsonObject.put("checkOutLat",appointment.getCheckOutLat());
                            dataJsonObject.put("checkOutLong", appointment.getCheckOutLong());
                            dataJsonObject.put("checkOutLocation", appointment.getCheckOutLocation());
                            dataJsonObject.put("checkOutTime",appointment.getCheckOutTime());
                            dataJsonObject.put("scheckOutTime",appointment.getScheckOutTime());
                            dataJsonObject.put("nextAppointment",appointment.getNextAppointment());
                            dataJsonObject.put("createdOn",appointment.getCreatedOn());
                            dataJsonObject.put("appointmentPosition",appointment.getAppointmentPosition());
                            dataJsonObject.put("hasNextAppointment",appointment.isHasNextAppointment());                    
                            dataJsonObject.put("ownerId", appointment.getOwner().getId());
                            dataJsonObject.put("purposeId", appointment.getPurpose().getId());
                            dataJsonObject.put("purposeName", appointment.getPurpose().getPurpose());
                            dataJsonObject.put("createdBy",appointment.getCreatedBy().getId());
                            dataJsonObject.put("customerId", appointment.getCustomer().getId());
                            dataJsonObject.put("customerLat",appointment.getCustomer().getLasknownLatitude());
                            dataJsonObject.put("customerLong",appointment.getCustomer().getLasknownLangitude());
                            dataJsonObject.put("customerName", appointment.getCustomer().getCustomerName());
                            if (appointment.getCreatedOn().before(appointment.getUpdated_on())) {
                                dataJsonObject.put("isModified", "true");
                            } else {
                                dataJsonObject.put("isModified", "false");
                            }                          

                             // payload send in notification, Added by sanchita
                             PushNotificationPayload payload = PushNotificationBigPayload.complex();
                             
                            if(appointment.getTitle().isEmpty()){  // to solve blank title issue changed to set purpose.
                                NotificationMessage=appointment.getPurpose().getPurpose() + " is created";
                                payload.addAlert(NotificationMessage);
                            } else {
                                NotificationMessage= appointment.getTitle() + " is created";
                                payload.addAlert(NotificationMessage);                                
                            }
                             payload.addCustomDictionary("type", "activity");
                             payload.addCustomDictionary("objectData", dataJsonObject.toString());                                                                                     
                                             
//                            System.out.println("payload >> "+payload.toString());
                            PushNotificationManager push= new PushNotificationManager();
                            push.addEditNotification(dataJsonObject, gcmId, deviceOS,payload);
//                            System.out.println("sendAddAppointmentNotificationToUsersOfAccount device == "+deviceOS+", gcmId : "+gcmId);
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                         // ended by sanchita
                    } else {
                        if (appointment.getAssignedTo().getId() != appointment.getCreatedBy().getId()) {
                            //GcmPushNotifications activityNotifications = new GcmPushNotifications();
                            //String gcmId = fieldSenseUtils.getUserGcmSenderId(appointment.getAssignedTo().getId());
//                            java.util.HashMap gcmInfo = fieldSenseUtils.getGcmUserInfo(appointment.getAssignedTo().getId());
//                            String gcmId =(String)gcmInfo.get("gcmId");
//                            int deviceOS =(Integer)gcmInfo.get("deviceOS");
                            String message = "";
                            if(appointment.getTitle().isEmpty()){  // to solve blank title issue changed to set purpose.
                                message = appointment.getPurpose().getPurpose() + " is created";
                            } else {
                                message = appointment.getTitle() + " is created";
                            }
                            PushNotificationManager push= new PushNotificationManager();
                            push.activityNotification(message, gcmId, deviceOS);

                        }
                    }
                    // Ended by jyoti, 09-01-2018
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.APPOINTMENT_CREATE_SUCCESS, " appointment   ", appointment);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Appointment creation failed . Try again .", "", "");
                } 
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
        } catch(Exception e){ // added by jyoti
            e.printStackTrace();
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Appointment creation failed . Try again .", "", "");
        }
    }

    /**
     *
     * @param appointment
     * @param userToken
     * @purpose used to create appointment in mobile
     */
    public Object createAppointmentMobile(Appointment appointment, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                appointment.setDateTime(fieldSenseUtils.converDateToTimestamp(appointment.getSdateTime()));
                appointment.setEndTime(fieldSenseUtils.converDateToTimestamp(appointment.getSendTime()));
                
                if(appointment.getStatus()==1){ //appointment created with Punch-In from Mobile starttime will be checkIn time
                    appointment.setCheckInTime(fieldSenseUtils.converDateToTimestamp(appointment.getSdateTime()));
                    //appointment.setCheckInTime(fieldSenseUtils.converDateToTimestamp(appointment.getScheckInTime()));
                    //appointment.setCheckOutTime(fieldSenseUtils.converDateToTimestamp(appointment.getScheckOutTime()));
                }
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                int userId = fieldSenseUtils.userIdForToken(userToken);
                int appointmentId = appointmentDao.createAppointmentMobile(appointment, accountId);
                if (appointmentId != 0) {
                    int appointmentPosstionId = appointmentDao.getUserMaxAppointmentPossition(userId, accountId);
                    appointmentDao.insertAppointmentPossition(appointmentId, userId, appointmentPosstionId + 1, accountId);
                    appointment = appointmentDao.selectAppointment(appointmentId, accountId);
                    // Added by jyoti,  03-02-2018
//                    System.out.println("createAppointmentMobile, assigned id : "+appointment.getAssignedTo().getId()+" , createdby id : "+appointment.getCreatedBy().getId());
                    if (appointment.getAssignedTo().getId() != appointment.getCreatedBy().getId()) {
                        java.util.HashMap gcmInfo = fieldSenseUtils.getGcmUserInfo(appointment.getAssignedTo().getId());
                        String gcmId =(String)gcmInfo.get("gcmId");
                        int deviceOS =(Integer)gcmInfo.get("deviceOS");
                        int appVersion = (Integer) gcmInfo.get("appVersion"); // added by jyoti
                        String NotificationMessage = null;// added by sanchita 29-08-2018
//                        System.out.println("createAppointmentMobile, appVersion : "+appVersion + " , if > "+Integer.parseInt(Constant.APP_VERSION_CHECK));
                        if(appVersion > Integer.parseInt(Constant.APP_VERSION_CHECK) && deviceOS==1){
                            if(!(gcmId.equals("0") || gcmId.isEmpty())) {                                
                                Map<String, Object> m_data = new HashMap<String, Object>();
                                if(appointment.getTitle().isEmpty()){  // to solve blank title issue changed to set purpose.
                                    m_data.put("message", appointment.getPurpose().getPurpose() + " is created");
                                } else {
                                    m_data.put("message", appointment.getTitle() + " is created");
                                }
                                m_data.put("message", appointment.getTitle()+" is created");
                                m_data.put("type", "activity");
                                m_data.put("visitStatus", "0"); // 0=new, 1=update, 2=delete
                                m_data.put("id", appointment.getId()); 
                                m_data.put("nextAppointmentTime", appointment.getNextAppointmentTime()); 
                                m_data.put("nextAppointmentType", appointment.getNextAppointmentType());                                
                                m_data.put("title", appointment.getTitle());
                                m_data.put("dateTime", appointment.getDateTime()); 
                                m_data.put("sdateTime", appointment.getSdateTime()); 
                                m_data.put("endTime", appointment.getEndTime());
                                m_data.put("sendTime", appointment.getSendTime()); 
                                m_data.put("assignedTo", appointment.getAssignedTo().getId());
                                m_data.put("description", appointment.getDescription()); 
                                m_data.put("appointmentType", appointment.getType()); 
                                m_data.put("outcome", appointment.getOutcome());
                                m_data.put("status", appointment.getStatus()); 
                                m_data.put("checkInLat",appointment.getCheckInLat()); 
                                m_data.put("checkInLong",appointment.getCheckInLong());
                                m_data.put("checkInLocation",appointment.getCheckInLocation());
                                m_data.put("checkInTime",appointment.getCheckInTime());
                                m_data.put("scheckInTime",appointment.getScheckInTime());
                                m_data.put("checkOutLat",appointment.getCheckOutLat());
                                m_data.put("checkOutLong", appointment.getCheckOutLong());
                                m_data.put("checkOutLocation", appointment.getCheckOutLocation());
                                m_data.put("checkOutTime",appointment.getCheckOutTime());
                                m_data.put("scheckOutTime",appointment.getScheckOutTime());
                                m_data.put("nextAppointment",appointment.getNextAppointment());
                                m_data.put("createdOn",appointment.getCreatedOn());
                                m_data.put("appointmentPosition",appointment.getAppointmentPosition());
                                m_data.put("hasNextAppointment",appointment.isHasNextAppointment());                    
                                m_data.put("ownerId", appointment.getOwner().getId());
                                m_data.put("purposeId", appointment.getPurpose().getId());
                                m_data.put("purposeName", appointment.getPurpose().getPurpose());
                                m_data.put("createdBy",appointment.getCreatedBy().getId());
                                m_data.put("customerId", appointment.getCustomer().getId());
                                m_data.put("customerLat",appointment.getCustomer().getLasknownLatitude());
                                m_data.put("customerLong",appointment.getCustomer().getLasknownLangitude());
                                m_data.put("customerName", appointment.getCustomer().getCustomerName());
                                if (appointment.getCreatedOn().before(appointment.getUpdated_on())) {
                                    m_data.put("isModified", "true");
                                } else {
                                    m_data.put("isModified", "false");
                                }
//                                System.out.println("m_data : "+ m_data);
                                PushNotificationManager push= new PushNotificationManager();
                                push.addEditNotification(m_data, gcmId, deviceOS,null);
                            }
                        }
                        else if (deviceOS == 2) { //Added by sanchita   ---//optimization for ios notification
                         if(!(gcmId.equals("0") || gcmId.isEmpty())) {
                            JSONObject dataJsonObject = new JSONObject();
                            try
                            {                            
                            dataJsonObject.put("visitStatus", "0"); // 0=new, 1=update, 2=delete
                            dataJsonObject.put("id", appointment.getId()); 
                            dataJsonObject.put("nextAppointmentTime", appointment.getNextAppointmentTime()); 
                            dataJsonObject.put("nextAppointmentType", appointment.getNextAppointmentType());                            
                            dataJsonObject.put("title", appointment.getTitle());                    
                            dataJsonObject.put("dateTime", appointment.getDateTime()); 
                            dataJsonObject.put("sdateTime", appointment.getSdateTime()); 
                            dataJsonObject.put("endTime", appointment.getEndTime());
                            dataJsonObject.put("sendTime", appointment.getSendTime()); 
                            dataJsonObject.put("assignedTo", appointment.getAssignedTo().getId());
                            dataJsonObject.put("description", appointment.getDescription()); 
                            dataJsonObject.put("appointmentType", appointment.getType()); 
                            dataJsonObject.put("outcome", appointment.getOutcome());
                            dataJsonObject.put("status", appointment.getStatus()); 
                            dataJsonObject.put("checkInLat",appointment.getCheckInLat()); 
                            dataJsonObject.put("checkInLong",appointment.getCheckInLong());
                            dataJsonObject.put("checkInLocation",appointment.getCheckInLocation());
                            dataJsonObject.put("checkInTime",appointment.getCheckInTime());
                            dataJsonObject.put("scheckInTime",appointment.getScheckInTime());
                            dataJsonObject.put("checkOutLat",appointment.getCheckOutLat());
                            dataJsonObject.put("checkOutLong", appointment.getCheckOutLong());
                            dataJsonObject.put("checkOutLocation", appointment.getCheckOutLocation());
                            dataJsonObject.put("checkOutTime",appointment.getCheckOutTime());
                            dataJsonObject.put("scheckOutTime",appointment.getScheckOutTime());
                            dataJsonObject.put("nextAppointment",appointment.getNextAppointment());
                            dataJsonObject.put("createdOn",appointment.getCreatedOn());
                            dataJsonObject.put("appointmentPosition",appointment.getAppointmentPosition());
                            dataJsonObject.put("hasNextAppointment",appointment.isHasNextAppointment());                    
                            dataJsonObject.put("ownerId", appointment.getOwner().getId());
                            dataJsonObject.put("purposeId", appointment.getPurpose().getId());
                            dataJsonObject.put("purposeName", appointment.getPurpose().getPurpose());
                            dataJsonObject.put("createdBy",appointment.getCreatedBy().getId());
                            dataJsonObject.put("customerId", appointment.getCustomer().getId());
                            dataJsonObject.put("customerLat",appointment.getCustomer().getLasknownLatitude());
                            dataJsonObject.put("customerLong",appointment.getCustomer().getLasknownLangitude());
                            dataJsonObject.put("customerName", appointment.getCustomer().getCustomerName());
                            if (appointment.getCreatedOn().before(appointment.getUpdated_on())) {
                                dataJsonObject.put("isModified", "true");
                            } else {
                                dataJsonObject.put("isModified", "false");
                            }                          

                             // payload send in notification, Added by sanchita
                             PushNotificationPayload payload = PushNotificationBigPayload.complex();
                             
                            if(appointment.getTitle().isEmpty()){  // to solve blank title issue changed to set purpose.
                                NotificationMessage=appointment.getPurpose().getPurpose() + " is created";
                                payload.addAlert(NotificationMessage);
                            } else {
                                NotificationMessage= appointment.getTitle() + " is created";
                                payload.addAlert(NotificationMessage);                                
                            }
                             payload.addCustomDictionary("type", "activity");
                             payload.addCustomDictionary("objectData", dataJsonObject.toString());                                                                                     
                                             
//                            System.out.println("payload >> "+payload.toString());
                            PushNotificationManager push= new PushNotificationManager();
                            push.addEditNotification(dataJsonObject, gcmId, deviceOS,payload);
//                            System.out.println("sendAddAppointmentFromMobileNotificationToUsersOfAccount device == "+deviceOS+", gcmId : "+gcmId);
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                         // ended by sanchita
                    }
                    }
                    // Ended by jyoti, 03-02-2018
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.APPOINTMENT_CREATE_SUCCESS, " appointment   ", appointment);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Appointment creation failed . Try again .", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     *
     * @param date
     * @param userToken
     * @purpose Used to get list of all appointments of specific users for
     * particular date.
     */
    public Object selectAppointments(String date, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                int userId = fieldSenseUtils.userIdForToken(userToken);
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateUtil = new Date();
                String timeAfter24Hours = "";
                try {
                    dateUtil = parser.parse(date);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateUtil);
                    calendar.add(Calendar.HOUR_OF_DAY, 24);
                    dateUtil = calendar.getTime();
                    timeAfter24Hours = parser.format(dateUtil);
                } catch (ParseException ex) {
                    log4jLog.info(" selectAppointments " + ex);
                }
                List<Appointment> appointmentsList = appointmentDao.selectAppointments(date, timeAfter24Hours, userId, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " appointments   ", appointmentsList);
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     *
     * @param date
     * @param userToken
     * @purpose Used to get list of all appointments of specific users for
     * particular date in mobile.
     */
//    public Object selectAppointmentsForMobile(String date, String userToken) {
//        if (fieldSenseUtils.isTokenValid(userToken)) {
//            //if(fieldSenseUtils.isSessionExpired(userToken)){
//                int accountId = fieldSenseUtils.accountIdForToken(userToken);
//                int userId = fieldSenseUtils.userIdForToken(userToken);
//                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date dateUtil = new Date();
//                String timeAfter24Hours = "";
//                try {
//                    dateUtil = parser.parse(date);
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.setTime(dateUtil);
//                    calendar.add(Calendar.HOUR_OF_DAY, 24);
//                    dateUtil = calendar.getTime();
//                    timeAfter24Hours = parser.format(dateUtil);
//                } catch (ParseException ex) {
//                    log4jLog.info(" selectAppointmentsForMobile " + ex);
//                }
//                List<Appointment> appointmentsList = appointmentDao.selectAppointmentsForMobile(date, timeAfter24Hours, userId, accountId);
//                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " appointments   ", appointmentsList);
//            // }else {
//                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
//            //}     
//        } else {
//            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
//        }
//    }
    //modified by nikhil
    public Object selectAppointmentsForMobile(String date, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
           int accountId = fieldSenseUtils.accountIdForToken(userToken);
                int userId = fieldSenseUtils.userIdForToken(userToken);
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateUtil = new Date();
                String timeAfter24Hours = "";
                try {
                    dateUtil = parser.parse(date);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateUtil);
                    calendar.add(Calendar.HOUR_OF_DAY, 24);
                    dateUtil = calendar.getTime();
                    timeAfter24Hours = parser.format(dateUtil);
                } catch (ParseException ex) {
                    log4jLog.info(" selectAppointmentsForMobile " + ex);
                }
//                List<Appointment> appointmentsList = appointmentDao.selectAppointmentsForMobile(date, Time,OnlyDate,OnlyDateAfter24,timeAfter24Hours, userId, accountId);
            List<Appointment> appointmentsList = appointmentDao.selectAppointmentsForMobile(date, timeAfter24Hours, userId, accountId);
//            System.out.println("selectAppointmentsForMobile userToken >> "+userToken);
//            System.out.println("selectAppointmentsForMobile userId >> "+userId);
//            System.out.println("selectAppointmentsForMobile appointmentsList >> "+appointmentsList);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " appointments   ", appointmentsList);
            // }else {
            //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
    /**
     *
     * @param date
     * @param userId
     * @purpose Used to get list of all appointments of specific users for
     * particular date in mobile.
     */
    public Object selectAppointmentsForMobile(String date, int userId, String userToken) {
        if (fieldSenseUtils.isUserValid(userId)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateUtil = new Date();
                String timeAfter24Hours = "";
                try {
                    dateUtil = parser.parse(date);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateUtil);
                    calendar.add(Calendar.HOUR_OF_DAY, 24);
                    dateUtil = calendar.getTime();
                    timeAfter24Hours = parser.format(dateUtil);
                } catch (ParseException ex) {
                    log4jLog.info(" selectAppointmentsForMobile " + ex);
                }
                List<Appointment> appointmentsList = appointmentDao.selectAppointmentsForMobileForMyTeam(date, timeAfter24Hours, userId, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " appointments   ", appointmentsList);
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid user . Please try again . ", "", "");
        }
    }

    
    /**
     *
     * @param id
     * @param userToken
     * @purpose Used to get details of particular appointment based on id.
     */
    public Object selectAppointment(int appointmentId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (appointmentDao.isAppointmentValid(appointmentId, accountId)) {
                    Appointment appointment = new Appointment();
                    appointment = appointmentDao.selectAppointment(appointmentId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " appointments   ", appointment);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid appointment or appointment is deleted.", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     *
     * @param id
     * @param userToken
     * @purpose Used to delete specific appointment based on id.
     */
    public Object deleteAppointmentFromMobile(int appointmentId,String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (appointmentDao.isAppointmentValid(appointmentId, accountId)) {
                    if (appointmentDao.isAppointmentStatusNotChanged(appointmentId, accountId)) {
                        if (appointmentDao.deleteAppointment(appointmentId, accountId)) {
                            if (appointmentDao.deleteAppointmentPossition(appointmentId, accountId)) {
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.APPOINTMENT_DELETE_SUCCESS, "", "");
                            } else {
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Appointment deletion failed. Please try again . ", "", "");
                            }
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Appointment deletion failed. Please try again . ", "", "");
                        }
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Appointment status has been changed.", "", "");
                    }    
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid appointment or appointment is deleted.", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
    /**
     *
     * @param id
     * @param userToken
     * @purpose Used to delete specific appointment based on id.
     */
    public Object deleteAppointment(int appointmentId,int loginId,int assignedTo,Appointment appointment,String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            int accountId = fieldSenseUtils.accountIdForToken(userToken);
            if (appointmentDao.isAppointmentValid(appointmentId, accountId)) {
                if (appointmentDao.isAppointmentStatusNotChanged(appointmentId, accountId)) {
                    if (appointmentDao.deleteAppointment(appointmentId, accountId)) {
                        if (appointmentDao.deleteAppointmentPossition(appointmentId, accountId)) {
//                                if (loginId != assignedTo) { // commented by jyoti
                                    try{
                                        java.util.HashMap gcmInfo = fieldSenseUtils.getGcmUserInfo(assignedTo);
                                        String gcmId =(String)gcmInfo.get("gcmId");
                                        int deviceOS =(Integer)gcmInfo.get("deviceOS");// Added by jyoti,  11-01-2018
                                        
                                        String NotificationMessage =null;// Added by sanchita,  29-08-2018
                                        int appVersion = (Integer) gcmInfo.get("appVersion"); // added by jyoti
//                                        System.out.println("deleteAppointment, appVersion : "+appVersion + " , if > "+Integer.parseInt(Constant.APP_VERSION_CHECK));
                                        if(appVersion > Integer.parseInt(Constant.APP_VERSION_CHECK) && deviceOS == 1){
                                            if(!(gcmId.equals("0") || gcmId.isEmpty())) {
                                                Map<String, Object> m_data = new HashMap<String, Object>();
                                                m_data.put("message", appointment.getPurpose().getPurpose()+" is deleted");
                                                m_data.put("type", "activity");
                                                m_data.put("visitStatus", "2"); // 0=new, 1=update, 2=delete
                                                m_data.put("id", appointmentId);
                                                if (appointment.getCreatedOn().before(appointment.getUpdated_on())) {
                                                    m_data.put("isModified", "true");
                                                } else {
                                                    m_data.put("isModified", "false");
                                                }

                                                PushNotificationManager push= new PushNotificationManager();
                                                push.addEditNotification(m_data, gcmId, deviceOS,null);
                                            }
                                        } else if (deviceOS == 2) {
                                            if (!(gcmId.equals("0") || gcmId.isEmpty())) {
                                                JSONObject dataJsonObject = new JSONObject();
                                                try {                                                    
                                                    dataJsonObject.put("visitStatus", "2"); // 0=new, 1=update, 2=delete
                                                    dataJsonObject.put("id", appointmentId);                                                    
                                                    if (appointment.getCreatedOn().before(appointment.getUpdated_on())) {
                                                        dataJsonObject.put("isModified", "true");
                                                    } else {
                                                        dataJsonObject.put("isModified", "false");
                                                    }

                                                    // payload send in notification, Added by sanchita
                                                    PushNotificationPayload payload = PushNotificationBigPayload.complex();
 
                                                    NotificationMessage = appointment.getPurpose().getPurpose() + " is deleted";
                                                    payload.addAlert(NotificationMessage);
                                                    payload.addCustomDictionary("type", "activity");
                                                    payload.addCustomDictionary("objectData", dataJsonObject.toString());

//                                                    System.out.println("payload >> " + payload.toString());
                                                    PushNotificationManager push = new PushNotificationManager();
                                                    push.addEditNotification(dataJsonObject, gcmId, deviceOS, payload);
//                                                    System.out.println("sendDeleteAppointmentNotificationToUsersOfAccount device == "+deviceOS+", gcmId : "+gcmId);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            // ended by sanchita
                                        }else {
                                            if (loginId != assignedTo) {
                                                //GcmPushNotifications activityNotifications = new GcmPushNotifications();
                                                //String gcmId = fieldSenseUtils.getUserGcmSenderId(assignedTo);
                                                try{
//                                                    java.util.HashMap gcmInfo = fieldSenseUtils.getGcmUserInfo(assignedTo);
//                                                    String gcmId =(String)gcmInfo.get("gcmId");
//                                                    int deviceOS =(Integer)gcmInfo.get("deviceOS");                                    
                                                    String message =appointment.getPurpose().getPurpose()+" is deleted";
//                                                    System.out.println("message1 : " + message);
                                                    log4jLog.info("message : " + message);                                        
                                                    PushNotificationManager push= new PushNotificationManager();
                                                    push.activityNotification(message, gcmId, deviceOS);
                                                } catch(Exception e){
//                                                    System.out.println("Exception delete visit");
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                        // Ended by jyoti, 09-01-2018

                                    } catch(Exception e){
//                                        System.out.println("Exception delete visit");
                                        e.printStackTrace();
                                    }
//                                } // commented by jyoti
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.APPOINTMENT_DELETE_SUCCESS, "", appointment.getPurpose().getPurpose());
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Appointment deletion failed . Please try again . ", "", "");
                        }
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Appointment deletion failed . Please try again . ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Appointment status has been changed.", "", "");
                }    
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid appointment or appointment is deleted.", "", "");
            }  
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     *
     * @param appointment
     * @param userToken
     * @purpose used to update appointment in web.
     */
    public Object updateAppointment(Appointment appointment, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                appointment.setDateTime(fieldSenseUtils.converDateToTimestamp(appointment.getSdateTime()));
                appointment.setEndTime(fieldSenseUtils.converDateToTimestamp(appointment.getSendTime()));
                appointment.setCheckInTime(fieldSenseUtils.converDateToTimestamp(appointment.getScheckInTime()));
                appointment.setCheckOutTime(fieldSenseUtils.converDateToTimestamp(appointment.getScheckOutTime()));
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (appointmentDao.isAppointmentValid(appointment.getId(), accountId)) {
                    if (appointmentDao.updateAppointment(appointment, accountId)) {
                        /* Push notification for edit appointment  starts */
                        if (appointment.getAssignedTo().getId() != appointment.getUpdated_by().getId()) {
                            //GcmPushNotifications activityNotifications = new GcmPushNotifications();
                            //String gcmId = fieldSenseUtils.getUserGcmSenderId(appointment.getAssignedTo().getId());
//                            // Commented by jyoti
//                            java.util.HashMap gcmInfo = fieldSenseUtils.getGcmUserInfo(appointment.getAssignedTo().getId());
//                            String gcmId =(String)gcmInfo.get("gcmId");
//                            int deviceOS =(Integer)gcmInfo.get("deviceOS");
//                            
//                            String message=appointment.getTitle()+" is updated";
//                            PushNotificationManager push= new PushNotificationManager();
//                            push.activityNotification(message, gcmId, deviceOS);
                            // comment ended by jyoti
                            
                        }
                        /* Push notification for edit appointment  starts */
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.APPOINTMENT_UPDATE_SUCCESS, "", "");
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Appointment updation failed . Please try again . ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid appointment or appointment is deleted.", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     *
     * @param appointment
     * @param userToken
     * @purpose used to update appointment in mobile
     */
    public Object updateAppointmentMobile(Appointment appointment, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                appointment.setDateTime(fieldSenseUtils.converDateToTimestamp(appointment.getSdateTime()));
                appointment.setEndTime(fieldSenseUtils.converDateToTimestamp(appointment.getSendTime()));
                appointment.setCheckInTime(fieldSenseUtils.converDateToTimestamp(appointment.getScheckInTime()));
                appointment.setCheckOutTime(fieldSenseUtils.converDateToTimestamp(appointment.getScheckOutTime()));
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (appointmentDao.isAppointmentValid(appointment.getId(), accountId)) {
                    if (appointmentDao.updateAppointmentMobile(appointment, accountId)) {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.APPOINTMENT_UPDATE_SUCCESS, "", "");
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Appointment updation failed . Please try again . ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid appointment or appointment is deleted.", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    public Object updateAppointmentPossition(List<Appointment> appointmentList, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (appointmentList != null) {
                    for (Appointment appointment : appointmentList) {
                        appointmentDao.updateAppointmentPossition(appointment.getId(), appointment.getAppointmentPosition(), accountId);
                    }
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.APPOINTMENT_UPDATE_SUCCESS, "", "");
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid appointment or appointment is deleted.", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     *
     * @param appointment
     * @param userToken
     * @purpose used to update appointment outcome and set next appointment
     */
    public Object updateAppointmentOutcomeAndNextAppointment(Appointment appointment, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                appointment.setDateTime(fieldSenseUtils.converDateToTimestamp(appointment.getSdateTime()));
                appointment.setEndTime(fieldSenseUtils.converDateToTimestamp(appointment.getSendTime()));
                // Check In and out should not be updated on appointment creation hence commented
                //appointment.setCheckInTime(fieldSenseUtils.converDateToTimestamp(appointment.getScheckInTime()));
                //appointment.setCheckOutTime(fieldSenseUtils.converDateToTimestamp(appointment.getScheckOutTime()));
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                String title;
                if (appointment.getType() == 0) {
                    title = " Meeting ";
                } else {
                    title = " Call ";
                }
                appointment.setTitle(title + fieldSenseUtils.getCustomerName(appointment.getCustomer().getId(), accountId));
                int appointmentId = appointmentDao.createAppointment(appointment, accountId);
                appointment.setNextAppointment(appointmentId);
                if (appointmentDao.isAppointmentValid(appointment.getId(), accountId)) {
                    if (appointmentDao.updateAppointmentOutcomeAndNextAppointment(appointment, accountId)) {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.APPOINTMENT_UPDATE_SUCCESS, "", "");
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Appointment updation failed . Please try again . ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid appointment or appointment is deleted.", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     *
     * @param appointment
     * @param userToken
     * @purpose used to update appointment outcome and set next appointment
     */
    public Object updateAppointmentOutcomeAndNextAppointmentMobile(Appointment appointment, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (appointment.isHasNextAppointment()) {
                    appointment.setDateTime(fieldSenseUtils.converDateToTimestamp(appointment.getSdateTime()));
                    appointment.setEndTime(fieldSenseUtils.converDateToTimestamp(appointment.getSendTime()));
                    // Check In and out should not be updated on appointment creation hence commented
                    //appointment.setCheckInTime(fieldSenseUtils.converDateToTimestamp(appointment.getScheckInTime()));
                    //appointment.setCheckOutTime(fieldSenseUtils.converDateToTimestamp(appointment.getScheckOutTime()));
                    int appointmentId = appointmentDao.createAppointmentMobile(appointment, accountId);
                    appointment.setNextAppointment(appointmentId);
                    if (appointmentDao.isAppointmentValid(appointment.getId(), accountId)) {
                        if (appointmentDao.updateAppointmentOutcomeAndNextAppointmentMobile(appointment, accountId)) {
                            int userId = fieldSenseUtils.userIdForToken(userToken);
                            int appointmentPosstionId = appointmentDao.getUserMaxAppointmentPossition(userId, accountId);
                        appointmentDao.insertAppointmentPossition(appointmentId, userId, appointmentPosstionId + 1, accountId);
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.APPOINTMENT_UPDATE_SUCCESS, "", "");
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Appointment updation failed . Please try again . ", "", "");
                        }
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid appointment or appointment is deleted.", "", "");
                    }
                } else {
                    if (appointmentDao.isAppointmentValid(appointment.getId(), accountId)) {
                        if (appointmentDao.updateAppointmentOutcomeAndNextAppointmentMobile(appointment, accountId)) {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.APPOINTMENT_UPDATE_SUCCESS, "", "");
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Appointment updation failed . Please try again . ", "", "");
                        }
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid appointment or appointment is deleted.", "", "");
                    }
                }
             // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     *
     * @param teamId
     * @param userId
     * @param date
     * @param userToken
     * @return list of appointments of specific user for specific day
     */
    public Object selectAppointForUser(String date, int teamId, int userId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateUtil = new Date();
                String timeAfter24Hours = "";
                try {
                    dateUtil = parser.parse(date);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateUtil);
                    calendar.add(Calendar.HOUR_OF_DAY, 24);
                    dateUtil = calendar.getTime();
                    timeAfter24Hours = parser.format(dateUtil);
                } catch (ParseException ex) {
                    log4jLog.info(" selectAppointments " + ex);
                }
                List<Appointment> appointmentsList = appointmentDao.selectAppointForUser(date, timeAfter24Hours, teamId, userId, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " appointments   ", appointmentsList);
             // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     *
     * @param customerId
     * @param appointmentId
     * @param userToken
     * @return list of all previous appointments of of particular appointment
     */
    public Object selectAppointmentsPriorToAppointment(int customerId, int appointmentId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (appointmentDao.isAppointmentValid(appointmentId, accountId)) {
                    List<Appointment> appointmentsList = new ArrayList<Appointment>();
                    appointmentsList = appointmentDao.selectAppointmentsPriorToAppointment(customerId, appointmentId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " appointments   ", appointmentsList);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid appointment or appointment is deleted.", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
    /**
     *
     * @param customerId
     * @param appointmentId
     * @param userToken
     * @return list of all previous appointments of of particular appointment
     */
    public Object selectAllAppointmentsPriorToAppointment(int customerId, int appointmentId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (appointmentDao.isAppointmentValid(appointmentId, accountId)) {
                    List<Appointments> appointmentsList = new ArrayList<Appointments>();
                    appointmentsList = appointmentDao.selectAllAppointmentsPriorToAppointment(customerId, appointmentId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " appointments   ", appointmentsList);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid appointment or appointment is deleted.", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
    

    /**
     *
     * @param date
     * @param teamId
     * @param userId
     * @param userToken
     * @return list of appointments of particular user for particular date
     */
    public Object selectAppointmentsDateWiseForUser(String date, int teamId, int userId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<Appointment> appointmentsList = new ArrayList<Appointment>();
                appointmentsList = appointmentDao.selectAppointmentsDateWiseForUser(date, teamId, userId, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " appointments   ", appointmentsList);
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     *
     * @param appointments
     * @param userToken
     * @purpose used to update appointment position in mobile
     */
    public Object sortApointments(List<Appointment> appointmentList, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (appointmentList != null) {
                    for (Appointment appointment : appointmentList) {
                        appointment.setDateTime(fieldSenseUtils.converDateToTimestamp(appointment.getSdateTime()));

                        appointment.setEndTime(fieldSenseUtils.converDateToTimestamp(appointment.getSendTime()));  // added by manohar
                        appointmentDao.updateAppointmentTime(appointment.getId(), appointment.getDateTime(),appointment.getEndTime(), accountId); // modified by manohar
                    }
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.APPOINTMENT_UPDATE_SUCCESS, "", "");
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid appointment or appointment is deleted.", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     *
     * @param customerId
     * @param userToken
     * @return list of appointments of specific customer
     */
    public Object selectAppointmentsForCustomer(int customerId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (customerDao.isCustomerValid(customerId, accountId)) {
                    List<Appointment> appointmentsList = appointmentDao.selectAppointmentsForCustomer(customerId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " appointments   ", appointmentsList);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid customer Id . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     *
     * @param appointment
     * @param userToken
     * @purpose used to update appointment
     */
    public Object updateOneAppointment(Appointment appointment, String userToken) {
            if (fieldSenseUtils.isTokenValid(userToken)) {
                appointment.setDateTime(fieldSenseUtils.converDateToTimestamp(appointment.getSdateTime()));
                appointment.setEndTime(fieldSenseUtils.converDateToTimestamp(appointment.getSendTime()));
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (appointmentDao.isAppointmentValid(appointment.getId(), accountId)) {
                    if(!appointmentDao.isScheduleFreeForUpdateAppointment(appointment, accountId)){
                         return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "You have already completed this appointment", "You have already completed this appointment", "");
                    }
                    appointment = appointmentDao.updateOneAppointment(appointment, accountId);
                    appointment=appointmentDao.selectAppointment(appointment.getId(), accountId);
//                    if (appointment.getAssignedTo().getId() != appointment.getUpdated_by().getId()) { // commented by jyoti
                        java.util.HashMap gcmInfo = fieldSenseUtils.getGcmUserInfo(appointment.getAssignedTo().getId());
                        String gcmId =(String)gcmInfo.get("gcmId");
                        int deviceOS =(Integer)gcmInfo.get("deviceOS");
                        // Added by jyoti,  09-01-2018
                        String NotificationMessage = null; // Added by sanchita,  29-08-2018
                        int appVersion = (Integer) gcmInfo.get("appVersion"); // added by jyoti
//                        System.out.println("updateOneAppointment, appVersion : "+appVersion + " , if > "+Integer.parseInt(Constant.APP_VERSION_CHECK));
                        if(appVersion > Integer.parseInt(Constant.APP_VERSION_CHECK) && deviceOS==1 ){
                            if(!(gcmId.equals("0") || gcmId.isEmpty())) {
                                Map<String, Object> m_data = new HashMap<String, Object>();
                                m_data.put("message", appointment.getPurpose().getPurpose()+" is updated");
                                m_data.put("type", "activity");
                                m_data.put("visitStatus", "1"); // 0=new, 1=update, 2=delete
                                m_data.put("id", appointment.getId()); 
                                m_data.put("nextAppointmentTime", appointment.getNextAppointmentTime()); 
                                m_data.put("nextAppointmentType", appointment.getNextAppointmentType());
                                m_data.put("title", appointment.getTitle()); 
                                m_data.put("dateTime", appointment.getDateTime()); 
                                m_data.put("sdateTime", appointment.getSdateTime()); 
                                m_data.put("endTime", appointment.getEndTime());
                                m_data.put("sendTime", appointment.getSendTime()); 
                                m_data.put("assignedTo", appointment.getAssignedTo().getId());
                                m_data.put("description", appointment.getDescription()); 
                                m_data.put("appointmentType", appointment.getType()); 
                                m_data.put("outcome", appointment.getOutcome());
                                m_data.put("status", appointment.getStatus()); 
                                m_data.put("checkInLat",appointment.getCheckInLat()); 
                                m_data.put("checkInLong",appointment.getCheckInLong());
                                m_data.put("checkInLocation",appointment.getCheckInLocation());
                                m_data.put("checkInTime",appointment.getCheckInTime());
                                m_data.put("scheckInTime",appointment.getScheckInTime());
                                m_data.put("checkOutLat",appointment.getCheckOutLat());
                                m_data.put("checkOutLong", appointment.getCheckOutLong());
                                m_data.put("checkOutLocation", appointment.getCheckOutLocation());
                                m_data.put("checkOutTime",appointment.getCheckOutTime());
                                m_data.put("scheckOutTime",appointment.getScheckOutTime());
                                m_data.put("nextAppointment",appointment.getNextAppointment());
                                m_data.put("createdOn",appointment.getCreatedOn());
                                m_data.put("appointmentPosition",appointment.getAppointmentPosition());
                                m_data.put("hasNextAppointment",appointment.isHasNextAppointment());                        
                                m_data.put("ownerId", appointment.getOwner().getId());
                                m_data.put("purposeId", appointment.getPurpose().getId());
                                m_data.put("purpose", appointment.getPurpose().getPurpose());
                                m_data.put("purposeName", appointment.getPurpose().getPurpose());
                                m_data.put("createdBy",appointment.getCreatedBy().getId());
                                m_data.put("customerLat",appointment.getCustomer().getLasknownLatitude());
                                m_data.put("customerLong",appointment.getCustomer().getLasknownLangitude());
                                m_data.put("customerId", appointment.getCustomer().getId());
                                m_data.put("customerName", appointment.getCustomer().getCustomerName());
                                if (appointment.getCreatedOn().before(appointment.getUpdated_on())) {
                                    m_data.put("isModified", "true");
                                } else {
                                    m_data.put("isModified", "false");
                                }
                                PushNotificationManager push= new PushNotificationManager();
                                push.addEditNotification(m_data, gcmId, deviceOS,null); // Modified by jyoti, 09-01-2018
                            }
                        }else if (deviceOS == 2) {
//                            System.out.println("deviceOS is 2");
                        if (!(gcmId.equals("0") || gcmId.isEmpty())) {
                            JSONObject dataJsonObject = new JSONObject();
                            try {
                                dataJsonObject.put("visitStatus", "1"); // 0=new, 1=update, 2=delete
                                dataJsonObject.put("id", appointment.getId()); 
                                dataJsonObject.put("nextAppointmentTime", appointment.getNextAppointmentTime()); 
                                dataJsonObject.put("nextAppointmentType", appointment.getNextAppointmentType());
                                dataJsonObject.put("title", appointment.getTitle()); 
                                dataJsonObject.put("dateTime", appointment.getDateTime()); 
                                dataJsonObject.put("sdateTime", appointment.getSdateTime()); 
                                dataJsonObject.put("endTime", appointment.getEndTime());
                                dataJsonObject.put("sendTime", appointment.getSendTime()); 
                                dataJsonObject.put("assignedTo", appointment.getAssignedTo().getId());
                                dataJsonObject.put("description", appointment.getDescription()); 
                                dataJsonObject.put("appointmentType", appointment.getType()); 
                                dataJsonObject.put("outcome", appointment.getOutcome());
                                dataJsonObject.put("status", appointment.getStatus()); 
                                dataJsonObject.put("checkInLat",appointment.getCheckInLat()); 
                                dataJsonObject.put("checkInLong",appointment.getCheckInLong());
                                dataJsonObject.put("checkInLocation",appointment.getCheckInLocation());
                                dataJsonObject.put("checkInTime",appointment.getCheckInTime());
                                dataJsonObject.put("scheckInTime",appointment.getScheckInTime());
                                dataJsonObject.put("checkOutLat",appointment.getCheckOutLat());
                                dataJsonObject.put("checkOutLong", appointment.getCheckOutLong());
                                dataJsonObject.put("checkOutLocation", appointment.getCheckOutLocation());
                                dataJsonObject.put("checkOutTime",appointment.getCheckOutTime());
                                dataJsonObject.put("scheckOutTime",appointment.getScheckOutTime());
                                dataJsonObject.put("nextAppointment",appointment.getNextAppointment());
                                dataJsonObject.put("createdOn",appointment.getCreatedOn());
                                dataJsonObject.put("appointmentPosition",appointment.getAppointmentPosition());
                                dataJsonObject.put("hasNextAppointment",appointment.isHasNextAppointment());                        
                                dataJsonObject.put("ownerId", appointment.getOwner().getId());
                                dataJsonObject.put("purposeId", appointment.getPurpose().getId());
                                dataJsonObject.put("purpose", appointment.getPurpose().getPurpose());
                                dataJsonObject.put("purposeName", appointment.getPurpose().getPurpose());
                                dataJsonObject.put("createdBy",appointment.getCreatedBy().getId());
                                dataJsonObject.put("customerLat",appointment.getCustomer().getLasknownLatitude());
                                dataJsonObject.put("customerLong",appointment.getCustomer().getLasknownLangitude());
                                dataJsonObject.put("customerId", appointment.getCustomer().getId());
                                dataJsonObject.put("customerName", appointment.getCustomer().getCustomerName());
                                if (appointment.getCreatedOn().before(appointment.getUpdated_on())) {
                                    dataJsonObject.put("isModified", "true");
                                } else {
                                    dataJsonObject.put("isModified", "false");
                                }

                                // payload send in notification, Added by sanchita
                                PushNotificationPayload payload = PushNotificationBigPayload.complex();

                                NotificationMessage = appointment.getPurpose().getPurpose() + " is updated";
                                payload.addAlert(NotificationMessage);
                                payload.addCustomDictionary("type", "activity");
                                payload.addCustomDictionary("objectData", dataJsonObject.toString());

//                                System.out.println("payload >> " + payload.toString());
                                PushNotificationManager push = new PushNotificationManager();
                                push.addEditNotification(dataJsonObject, gcmId, deviceOS, payload);
//                                System.out.println("sendEditAppointmentNotificationToUsersOfAccount device == "+deviceOS+", gcmId : "+gcmId);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        // ended by sanchita
                    } 
                        else {
                            if (appointment.getAssignedTo().getId() != appointment.getUpdated_by().getId()) {
                                /* Push notification for update appointment  starts */
                                //GcmPushNotifications activityNotifications = new GcmPushNotifications();
                                //String gcmId = fieldSenseUtils.getUserGcmSenderId(appointment.getAssignedTo().getId());

//                                java.util.HashMap gcmInfo = fieldSenseUtils.getGcmUserInfo(appointment.getAssignedTo().getId());
//                                String gcmId =(String)gcmInfo.get("gcmId");
//                                int deviceOS =(Integer)gcmInfo.get("deviceOS");
                                
                                String message="";
                                 if (appointment.getTitle().isEmpty()) {  // to solve blank title issue changed to set purpose.
                                    message = appointment.getPurpose().getPurpose() + " is updated";
                                } else {
                                    message = appointment.getTitle() + " is updated";
                                }
                                PushNotificationManager push= new PushNotificationManager();
                                push.activityNotification(message, gcmId, deviceOS);


                                /* Push notification for update appointment  ends  */
                            }
                        }
                        // Ended by jyoti, 09-01-2018
//                    } // commented by jyoti
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.APPOINTMENT_UPDATE_SUCCESS, "appointment", appointment);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid appointment or appointment is deleted.", "", "");
                }  
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
            }
    }

    /**
     *
     * @param userId
     * @param date
     * @param userToken
     * @return list of appointments of user for particular date.
     */
    public Object selectAppointForUserWithDate(String date, int userId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateUtil = new Date();
                String timeAfter24Hours = "";
                try {
                    dateUtil = parser.parse(date);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateUtil);
                    calendar.add(Calendar.HOUR_OF_DAY, 24);
                    dateUtil = calendar.getTime();
                    timeAfter24Hours = parser.format(dateUtil);
                } catch (ParseException ex) {
                    log4jLog.info(" selectAppointments " + ex);
                }
                List<Appointment> appointmentsList = appointmentDao.selectAppointForUserWithDate(date, timeAfter24Hours, userId, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " appointments   ", appointmentsList);
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}      
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     *
     * @param appointment
     * @param userToken
     * @purpose used to update appointment time
     */
    public Object updateOneAppointmentTime(Appointment appointment, String userToken) {
        try{
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                //appointment.setDateTime(fieldSenseUtils.converDateToTimestamp(appointment.getSdateTime()));
                //appointment.setEndTime(fieldSenseUtils.converDateToTimestamp(appointment.getSendTime()));
//                
//                appointment.setCheckInTime(fieldSenseUtils.converDateToTimestamp(appointment.getSdateTime()));
//                appointment.setCheckOutTime(fieldSenseUtils.converDateToTimestamp(appointment.getSendTime()));
            
            try{
                    appointment.setCheckInTime(fieldSenseUtils.converDateToTimestampForCheckInOut(appointment.getSdateTime()));
                    appointment.setCheckOutTime(fieldSenseUtils.converDateToTimestampForCheckInOut(appointment.getSendTime()));
            }catch(Exception ex) {
                ex.printStackTrace();
            }


                  // Added By Jyoti #11461
                Timestamp getCheckInDateTime = appointment.getCheckInTime();
                Timestamp getCheckOutDateTime = appointment.getCheckOutTime();
                 
                Date checkInDate = getCheckInDateTime;
                Date checkOutDate = getCheckOutDateTime;

                //in milliseconds
                long difference =checkOutDate.getTime() - checkInDate.getTime();
                     difference =difference/1000; // diff in seconds
               
                //comparison:
                
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (appointmentDao.isAppointmentValid(appointment.getId(), accountId)) {                    
                    if(appointment.getStatus()!=2 && !appointmentDao.isScheduleFreeForUpdateAppointment(appointment, accountId)){
                         return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Appointment already exists in that period for user.", "Appointment already exists in that period for user.", "");
                    }  
                    //check if difference > 48 hour in seconds 3600*48
                    if ( difference >172799 ){                       
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Check out updation failed . Please try again . ", "", "");                       
                    }
                // Ended by Jyoti
                    UserKey userKey=new UserKey();
                    if (appointmentDao.updateOneAppointmentTimeAndStus(appointment, accountId)) {
                        try{
                        if(appointment.getStatus()==1){
                            userKey.setKeyValue("1");
                            User user1=new User();
                            user1.setId(appointment.getAssignedTo().getId());
                            userKey.setUserId(user1);
                            userKey.setUserKay("InMeeting");
                        userKeyDao.updateUserKeys(userKey, accountId);
                        }else{
                            userKey.setKeyValue("0");
                            User user1=new User();
                            user1.setId(appointment.getAssignedTo().getId());
                            userKey.setUserId(user1);
                            userKey.setUserKay("InMeeting");
                        userKeyDao.updateUserKeys(userKey, accountId);
                        
                        }
                        }catch(Exception e){
                        e.printStackTrace();
                            log4jLog.info(" updateOneAppointmentTime " + e);
                        }
    //                    /* Push notification for update appointment  starts */
    //                    GcmPushNotifications activityNotifications = new GcmPushNotifications();
    //                    String gcmId = fieldSenseUtils.getUserGcmSenderId(appointment.getAssignedTo().getId());
    //                    log4jLog.info(" updateAppointment Notification gcm Id " + gcmId + " appointment Tittle " + appointment.getTitle());
    //
    //                    if (!gcmId.equals("0")) {
    //                        activityNotifications.activityNotifications(appointment.getTitle(), gcmId);
    //                    }
    //                    /* Push notification for update appointment  ends  */

                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.APPOINTMENT_UPDATE_SUCCESS, "", "");

                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Appointment updation failed . Please try again . ", "", "");
                        }                    
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid appointment or appointment is deleted.", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
        } catch (Exception e) {
            e.printStackTrace();
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Some Exception. Please try again . ", "", "");
        }
    }
    
    /**
     * @Added by jyoti
     * @param userId
     * @param customerId
     * @param userToken
     * @return
     */
    public Object selectAppointForRelevantCustomerOfUser(int userId, int customerId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            int accountId = fieldSenseUtils.accountIdForToken(userToken);
            List<Appointment> appointmentsList = appointmentDao.selectAppointForRelevantCustomerOfUser(userId, customerId, accountId);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " appointments   ", appointmentsList);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
     public int selectAppointmentOfUser(int userId,String from,int accountId) {
        try {
            String to ="";
             Date date1=new SimpleDateFormat("dd MMM yyyy").parse(from);  
             SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
             from = format.format(date1);
             Date date = format.parse(from);
             Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            to=format.format(calendar.getTime());
            from = from +" 00:00:00";
            to = to + " 00:00:00";
            return  appointmentDao.selectAppointment(userId, from, to, accountId);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(AppointmentManager.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return 0;
        }
    }

     // rework start on 10-april-2018
    /**
     * Added by Jyoti
     *
     * @param appointment
     * @param userToken
     * @return
     * @purpose used to create appointment in web for multiple users
     */
    public Object createAppointmentForMultipleUsers(Appointment appointment, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            try {
                appointment.setDateTime(fieldSenseUtils.converDateToTimestamp(appointment.getSdateTime()));
                appointment.setEndTime(fieldSenseUtils.converDateToTimestamp(appointment.getSendTime()));
                if (!appointment.getScheckInTime().equals("")) {
                    appointment.setCheckInTime(fieldSenseUtils.converDateToTimestamp(appointment.getScheckInTime()));
                }
                if (!appointment.getScheckOutTime().equals("")) {
                    appointment.setCheckOutTime(fieldSenseUtils.converDateToTimestamp(appointment.getScheckOutTime()));
                }
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                int userId = fieldSenseUtils.userIdForToken(userToken);
                // Added by jyoti                          
                List<Integer> assignedIdList = appointment.getAssignedToList();
                String visitsNotAddedForUsers = "";
                int i = 0, j = 1;
                int checkVisitCreated = 0;
                for (int assignedId : assignedIdList) {

                    if (!appointmentDao.isScheduleFreeForCreateAppointmentAssignedIdList(appointment, accountId, assignedId)) {
                        i++;
                        if (i != j) {
                            visitsNotAddedForUsers += ",";
                        }
                        visitsNotAddedForUsers += fieldSenseUtils.getUserFullName(assignedId);
                        continue;
                    }
                    int appointmentId = appointmentDao.createAppointmentAssignedIdList(appointment, accountId, assignedId);
                    if (appointmentId != 0) {
                        checkVisitCreated++;
                        if (assignedId != appointment.getCreatedBy().getId()) {
                            java.util.HashMap gcmInfo = fieldSenseUtils.getGcmUserInfo(assignedId); // added by jyoti
                            String gcmId = (String) gcmInfo.get("gcmId");
                            int deviceOS = (Integer) gcmInfo.get("deviceOS");
                            String message = appointment.getTitle() + " is created";
                            PushNotificationManager push = new PushNotificationManager();
                            push.activityNotification(message, gcmId, deviceOS);
                        }
                        /* Push notification for create appointment  starts */
                        int appointmentPosstionId = appointmentDao.getUserMaxAppointmentPossition(userId, accountId);
                        appointmentDao.insertAppointmentPossition(appointmentId, userId, appointmentPosstionId + 1, accountId);
                        appointment = appointmentDao.selectAppointment(appointmentId, accountId);
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Appointment creation failed. Try again.", "", "");
                    }
                }
                // added by jyoti
                if (!visitsNotAddedForUsers.isEmpty() && checkVisitCreated != 0) {
                    appointment.setVisitsNotAddedForUsers(visitsNotAddedForUsers);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.MIXRESULT, " Appointment already exists for the following user : " + appointment.getVisitsNotAddedForUsers(), "appointment created", appointment);
                    // show red alert, close the form
                } else if (!visitsNotAddedForUsers.isEmpty() && checkVisitCreated == 0) {
                    appointment.setVisitsNotAddedForUsers(visitsNotAddedForUsers);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Appointment already exists for the following user : " + appointment.getVisitsNotAddedForUsers(), "appointment created", appointment);
                    // show red alert, dont close form
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.APPOINTMENT_CREATE_SUCCESS, " appointment   ", appointment);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Appointment creation failed. Try again.", "", "");
            }
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again. ", "", "");
        }
    }

    // start, added by jyoti, recurring visits
    /**
     * @Added by Jyoti
     * @param appointment
     * @param userToken
     * @return
     * @purpose used to create recurring Appointment in web
     */
    public Object createAppointmentRecurring(Appointment appointment, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            // Added by jyoti, 01-feb-2017 PURPOSE - create recurring visit
            // In worst case - 46.6s took to create visits, need to implement thread concept
            AppointmentRecurringList appointmentRecurringList = new AppointmentRecurringList();
            int accountId = fieldSenseUtils.accountIdForToken(userToken);
            int userId = fieldSenseUtils.userIdForToken(userToken);
            // Added by jyoti, for adding multiple visits to multiple users
            List<Integer> assignedIdList = appointment.getAssignedToList();
            String visitsNotAddedForUsers = "";
            int p = 0, q = 1;
            int checkVisitCreated = 0;
            try {
                List<String> repeatListOfDates = new ArrayList<String>();                                                           // list of dates to add 
                String startDateStartTime = appointment.getSdateTime();                                                             // start Date with start time
                String startDateEndTime = appointment.getSendTime();                                                                // start Date with end time               
                String recurrTillDateStartTime = appointment.getAppointmentRecurringList().getRecurrTill_Date_startTime();         // end date (with start time)
                String recurrTillDateEndTime = appointment.getAppointmentRecurringList().getRecurrTill_Date_endTime();             // end date (with start time)
                List<Integer> repeatOnDay_weekly = appointment.getAppointmentRecurringList().getRepeatOnDay_weekly();               // days like 0=monday, 1=tues etc
                List<Integer> repeatOnDate_monthly = appointment.getAppointmentRecurringList().getRepeatOnDate_monthly();           // dates like 01,23,31 etc                
                int repeatType = appointment.getAppointmentRecurringList().getRepeatType();                                        // 11=daily,22=weekly,33=monthly
                int repeatAfterEvery = appointment.getAppointmentRecurringList().getRepeatAfterEvery();                            // default 1

                //trim start/end date time
                String startTime = startDateStartTime.split(" ")[1];
                String endTime = startDateEndTime.split(" ")[1];
                String startDate = startDateStartTime.split(" ")[0];
                String endDate = recurrTillDateStartTime.split(" ")[0];

                repeatListOfDates = appointmentDao.createAppointmentListOfRecurringDates(repeatType, startDate, endDate, repeatAfterEvery, repeatOnDay_weekly, repeatOnDate_monthly);
                int size = repeatListOfDates.size();
//                System.out.println("list of dates : " + repeatListOfDates);

                // weekly/monthly data concatenation for CSV in table
                StringBuilder weeklyDay = new StringBuilder();
                StringBuilder monthlyDate = new StringBuilder();

                if (repeatType == 22) {
                    for (int i = 0; i < repeatOnDay_weekly.size(); i++) {

                        if (i == (repeatOnDay_weekly.size() - 1)) {
                            weeklyDay.append(repeatOnDay_weekly.get(i));
                            weeklyDay.append("");
                        } else {
                            weeklyDay.append(repeatOnDay_weekly.get(i));
                            weeklyDay.append(",");
                        }

                    }
                }
                if (repeatType == 33) {
                    for (int i = 0; i < repeatOnDate_monthly.size(); i++) {

                        if (i == (repeatOnDate_monthly.size() - 1)) {
                            monthlyDate.append(repeatOnDate_monthly.get(i));
                            monthlyDate.append("");
                        } else {
                            monthlyDate.append(repeatOnDate_monthly.get(i));
                            monthlyDate.append(",");
                        }

                    }
                }

                // setting appointmentsRecurring table values
                appointmentRecurringList.setRepeatType(repeatType);
                appointmentRecurringList.setRepeatAfterEvery(repeatAfterEvery);
                appointmentRecurringList.setRepeatOnDayWeeklyCSV(weeklyDay.toString());
                appointmentRecurringList.setRepeatOnDateMonthlyCSV(monthlyDate.toString());
                appointmentRecurringList.setEndDateStartTime(fieldSenseUtils.converDateToTimestamp(recurrTillDateStartTime));
                appointmentRecurringList.setEndDateEndTime(fieldSenseUtils.converDateToTimestamp(recurrTillDateEndTime));
                appointment.setAppointmentRecurringList(appointmentRecurringList);
                boolean IsScheduleFreeRecurringCheck = false;

//                for (int assignedId : assignedIdList) { // used when multiple user visit creating
                    try {
                        for (int i = 0; i <= (size - 1); i++) {

                            appointment.setDateTime(fieldSenseUtils.converDateToTimestamp(repeatListOfDates.get(i) + " " + startTime));
                            appointment.setEndTime(fieldSenseUtils.converDateToTimestamp(repeatListOfDates.get(i) + " " + endTime));

                            // need to work on this later
//                            if (!appointmentDao.isScheduleFreeForCreateAppointmentAssignedIdList(appointment, accountId, assignedId)) {
//                                if ((i == (size - 1)) && (IsScheduleFreeRecurringCheck == false)) {
//                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Appointment creation failed . List of Appointments already exists in that period for user.", "List of Appointments already exists in that period for user.", "");
//                                }
//                            } else {
//                                IsScheduleFreeRecurringCheck = true;
//                            }

                        }

                        appointment.setDateTime(fieldSenseUtils.converDateToTimestamp(appointment.getSdateTime()));
                        appointment.setEndTime(fieldSenseUtils.converDateToTimestamp(appointment.getSendTime()));

//                        if (IsScheduleFreeRecurringCheck == true) { // need to work on this later
                            int appointmentRecurringListId = appointmentDao.createAppointmentRecurringList(appointment, accountId);
                            appointmentRecurringList.setId(appointmentRecurringListId);             // setting recurring id for inserting into appointment table
                            appointment.setAppointmentRecurringList(appointmentRecurringList);

//                            System.out.println("appointmentRecurringId : " + appointmentRecurringListId);
                            // have to put if else for handling exception - later
//                        }

                    } catch (Exception e) {
                        log4jLog.info("appointmentRecurringListId creation : " + e);
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "1 Appointment creation failed . Try again .", "", "");
                    }

                    int appointmentId = 0;

                    for (int i = 0; i < size; i++) {

                        appointment.setDateTime(fieldSenseUtils.converDateToTimestamp(repeatListOfDates.get(i) + " " + startTime));          // setting list of date with start time
                        appointment.setEndTime(fieldSenseUtils.converDateToTimestamp(repeatListOfDates.get(i) + " " + endTime));             // setting list of date with end time

                        if (!appointment.getScheckInTime().equals("")) {
                            appointment.setCheckInTime(fieldSenseUtils.converDateToTimestamp(appointment.getScheckInTime()));
                        }
                        if (!appointment.getScheckOutTime().equals("")) {
                            appointment.setCheckOutTime(fieldSenseUtils.converDateToTimestamp(appointment.getScheckOutTime()));
                        }

                        // need to work on this later
//                        if (!appointmentDao.isScheduleFreeForCreateAppointmentAssignedIdList(appointment, accountId, assignedId)) {
//                            p++;
//                            if (p != q) {
//                                visitsNotAddedForUsers += ",";
//                            }
//                            visitsNotAddedForUsers += fieldSenseUtils.getUserFullName(assignedId);
//                            //                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Appointment creation failed . Appointment already exists in that period for user.", "Appointment already exists in that period for user.", "");
//                            continue;   // if visit already exists for the date, continue for next iteration. skip the further steps. 
//                        }

                        appointmentId = appointmentDao.createAppointmentRecurring(appointment, accountId, appointment.getAssignedTo().getId());
//                        System.out.println("appointmentId : " + appointmentId);
                        if (appointmentId != 0) {
                            int appointmentPosstionId = appointmentDao.getUserMaxAppointmentPossition(userId, accountId);
                            appointmentDao.insertAppointmentPossition(appointmentId, userId, appointmentPosstionId + 1, accountId);
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "2 Appointment creation failed . Try again .", "", "");
                        }

                    }
                    /* Push notification for ios/android when appointment created */
                    if (appointmentId != 0) {
                        if (appointment.getAssignedTo().getId() != appointment.getCreatedBy().getId()) {
                            java.util.HashMap gcmInfo = fieldSenseUtils.getGcmUserInfo(appointment.getAssignedTo().getId());
                            String gcmId = (String) gcmInfo.get("gcmId");
                            int deviceOS = (Integer) gcmInfo.get("deviceOS");
                            String message = appointment.getTitle() + " is created";
                            PushNotificationManager push = new PushNotificationManager();
                            push.activityNotification(message, gcmId, deviceOS);
                        }
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Appointment creation failed. Try again.", "", "");
                    }
//                }
                // added by jyoti
                if (!visitsNotAddedForUsers.isEmpty() && checkVisitCreated != 0) {
                    appointment.setVisitsNotAddedForUsers(visitsNotAddedForUsers);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.MIXRESULT, " Appointment already exists for the following user : " + appointment.getVisitsNotAddedForUsers(), "appointment created", appointment);
                    // show red alert, close the form
                } else if (!visitsNotAddedForUsers.isEmpty() && checkVisitCreated == 0) {
                    appointment.setVisitsNotAddedForUsers(visitsNotAddedForUsers);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Appointment already exists for the following user : " + appointment.getVisitsNotAddedForUsers(), "appointment created", appointment);
                    // show red alert, dont close form
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.APPOINTMENT_CREATE_SUCCESS, " appointment   ", appointment);
                }
                /* Push notification for create appointment */
            } catch (Exception e) {
                log4jLog.info("Recurring appointmentId creation : " + e);
                e.printStackTrace();
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "3 Appointment creation failed . Try again .", "", "");
            }
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again. ", "", "");
        }
    }
            // Ended by jyoti

    /**
     * Added by Jyoti, 08-march-2017
     *
     * @param appointment
     * @param userToken
     * @return
     * @purpose used to update selected and following recurring appointment
     */
    public Object updateRecurringAppointment(Appointment appointment, String userToken) {

        if (fieldSenseUtils.isTokenValid(userToken)) {

            int accountId = fieldSenseUtils.accountIdForToken(userToken);
            int userId = fieldSenseUtils.userIdForToken(userToken);
//                  for log check purpose, remove later
            long beforeResponseTime = System.currentTimeMillis();
            long elapsedTime = System.currentTimeMillis() - beforeResponseTime;
//            System.out.println("seconds Before response : " + elapsedTime);

            if (appointmentDao.isAppointmentValid(appointment.getId(), accountId)) {
                elapsedTime = System.currentTimeMillis() - beforeResponseTime;
//                System.out.println("seconds after isAppointmentValid response : " + elapsedTime);
                // delete this and following appointment
                if (appointmentDao.deleteRecurringAppointment(appointment.getAppointmentRecurringList().getId(), appointment.getId(), accountId)) {
                    elapsedTime = System.currentTimeMillis() - beforeResponseTime;
//                    System.out.println("seconds after deleteRecurringAppointment response : " + elapsedTime);
                    //insert new appointments
                    AppointmentRecurringList appointmentRecurringList = new AppointmentRecurringList();
                    if (appointment.getIsRecurring() == 1) {
                        try {
                            List<String> repeatListOfDates = new ArrayList<String>();                                                           // list of dates to add 
                            String startDateStartTime = appointment.getSdateTime();                                                             // start Date with start time
                            String startDateEndTime = appointment.getSendTime();                                                                // start Date with end time               
                            String recurrTillDateStartTime = appointment.getAppointmentRecurringList().getRecurrTill_Date_startTime();         // end date (with start time)
                            String recurrTillDateEndTime = appointment.getAppointmentRecurringList().getRecurrTill_Date_endTime();             // end date (with start time)
                            List<Integer> repeatOnDay_weekly = appointment.getAppointmentRecurringList().getRepeatOnDay_weekly();               // days like 0=monday, 1=tues etc
                            List<Integer> repeatOnDate_monthly = appointment.getAppointmentRecurringList().getRepeatOnDate_monthly();           // dates like 01,23,31 etc                
                            int repeatType = appointment.getAppointmentRecurringList().getRepeatType();                                        // 11=daily,22=weekly,33=monthly
                            int repeatAfterEvery = appointment.getAppointmentRecurringList().getRepeatAfterEvery();                            // default 1

                            //trim start/end date time
                            String startTime = startDateStartTime.split(" ")[1];
                            String endTime = startDateEndTime.split(" ")[1];
                            String startDate = startDateStartTime.split(" ")[0];
                            String endDate = recurrTillDateStartTime.split(" ")[0];

                            repeatListOfDates = appointmentDao.createAppointmentListOfRecurringDates(repeatType, startDate, endDate, repeatAfterEvery, repeatOnDay_weekly, repeatOnDate_monthly);
                            int size = repeatListOfDates.size();
//                            System.out.println("(On Edit) list of dates : " + repeatListOfDates);

                            // weekly/monthly data concatenation for CSV in table
                            StringBuilder weeklyDay = new StringBuilder();
                            StringBuilder monthlyDate = new StringBuilder();

                            if (repeatType == 22) {
                                for (int i = 0; i < repeatOnDay_weekly.size(); i++) {
                                    if (i == (repeatOnDay_weekly.size() - 1)) {
                                        weeklyDay.append(repeatOnDay_weekly.get(i));
                                        weeklyDay.append("");
                                    } else {
                                        weeklyDay.append(repeatOnDay_weekly.get(i));
                                        weeklyDay.append(",");
                                    }
                                }
                            }
                            if (repeatType == 33) {
                                for (int i = 0; i < repeatOnDate_monthly.size(); i++) {
                                    if (i == (repeatOnDate_monthly.size() - 1)) {
                                        monthlyDate.append(repeatOnDate_monthly.get(i));
                                        monthlyDate.append("");
                                    } else {
                                        monthlyDate.append(repeatOnDate_monthly.get(i));
                                        monthlyDate.append(",");
                                    }
                                }
                            }

                            try {
                                // setting appointmentsRecurring table values
                                appointmentRecurringList.setRepeatType(repeatType);
                                appointmentRecurringList.setRepeatAfterEvery(repeatAfterEvery);
                                appointmentRecurringList.setRepeatOnDayWeeklyCSV(weeklyDay.toString());
                                appointmentRecurringList.setRepeatOnDateMonthlyCSV(monthlyDate.toString());
                                appointmentRecurringList.setEndDateStartTime(fieldSenseUtils.converDateToTimestamp(recurrTillDateStartTime));
                                appointmentRecurringList.setEndDateEndTime(fieldSenseUtils.converDateToTimestamp(recurrTillDateEndTime));
                                appointment.setAppointmentRecurringList(appointmentRecurringList);

                                boolean IsScheduleFreeRecurringCheck = false;
                                for (int i = 0; i <= (size - 1); i++) {
                                    appointment.setDateTime(fieldSenseUtils.converDateToTimestamp(repeatListOfDates.get(i) + " " + startTime));
                                    appointment.setEndTime(fieldSenseUtils.converDateToTimestamp(repeatListOfDates.get(i) + " " + endTime));

                                    if (!appointmentDao.isScheduleFreeForCreateAppointment(appointment, accountId)) {
                                        if ((i == (size - 1)) && (IsScheduleFreeRecurringCheck == false)) {
                                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "(On Edit) Appointment updation failed . List of Appointments already exists in that period for user.", "List of Appointments already exists in that period for user.", "");
                                        }
                                    } else {
                                        IsScheduleFreeRecurringCheck = true;
                                    }
                                }

                                appointment.setDateTime(fieldSenseUtils.converDateToTimestamp(appointment.getSdateTime()));
                                appointment.setEndTime(fieldSenseUtils.converDateToTimestamp(appointment.getSendTime()));

                                if (IsScheduleFreeRecurringCheck == true) {
                                    int appointmentRecurringListId = appointmentDao.createAppointmentRecurringList(appointment, accountId);
                                    appointmentRecurringList.setId(appointmentRecurringListId);             // setting recurring id for inserting into appointment table
                                    appointment.setAppointmentRecurringList(appointmentRecurringList);
//                                    System.out.println("(On Edit) appointmentRecurringId : " + appointmentRecurringListId);
                                }

                            } catch (Exception e) {
                                log4jLog.info("(On Edit) new appointmentRecurringListId creation : " + e);
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " (On Edit) Appointment updation failed. Try again .", "", "");
                            }

                            int appointmentId = 0;
                            for (int i = 0; i < size; i++) {
                                appointment.setDateTime(fieldSenseUtils.converDateToTimestamp(repeatListOfDates.get(i) + " " + startTime));          // setting list of date with start time
                                appointment.setEndTime(fieldSenseUtils.converDateToTimestamp(repeatListOfDates.get(i) + " " + endTime));             // setting list of date with end time

                                if (!appointment.getScheckInTime().equals("")) {
                                    appointment.setCheckInTime(fieldSenseUtils.converDateToTimestamp(appointment.getScheckInTime()));
                                }
                                if (!appointment.getScheckOutTime().equals("")) {
                                    appointment.setCheckOutTime(fieldSenseUtils.converDateToTimestamp(appointment.getScheckOutTime()));
                                }

                                if (!appointmentDao.isScheduleFreeForCreateAppointment(appointment, accountId)) {
                                    continue;   // if visit already exists for the date, continue for next iteration. skip the further steps. 
                                }

                                appointmentId = appointmentDao.createAppointment(appointment, accountId);
//                                System.out.println("(On Edit) appointmentId : " + appointmentId);
                                if (appointmentId != 0) {
                                    int appointmentPosstionId = appointmentDao.getUserMaxAppointmentPossition(userId, accountId);
                                    appointmentDao.insertAppointmentPossition(appointmentId, userId, appointmentPosstionId + 1, accountId);
                                } else {
                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "(On Edit) Appointment updation failed . Try again .", "", "");
                                }
                            }
                            /* Push notification for ios/android when appointment updated */
                            if (appointmentId != 0) {
                                if (appointment.getAssignedTo().getId() != appointment.getUpdated_by().getId()) {
                                    java.util.HashMap gcmInfo = fieldSenseUtils.getGcmUserInfo(appointment.getAssignedTo().getId());
                                    String gcmId = (String) gcmInfo.get("gcmId");
                                    int deviceOS = (Integer) gcmInfo.get("deviceOS");
                                    String message = appointment.getTitle() + " is updated";
                                    PushNotificationManager push = new PushNotificationManager();
                                    push.activityNotification(message, gcmId, deviceOS);
                                }
                            }
                            /* Push notification for ios/android when appointment updated */
                            elapsedTime = System.currentTimeMillis() - beforeResponseTime;
//                            System.out.println("seconds after insertRecurringAppointment response : " + elapsedTime);
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.APPOINTMENT_UPDATE_SUCCESS, "(On Edit) appointment", appointment);

                        } catch (Exception e) {
                            log4jLog.info("(On Edit) new appointmentId creation : " + e);
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "(On Edit) Appointment updation failed. Please Try again.", "", "");
                        }
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Appointment updation failed. Appointment is non recurring.", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " (On Edit) Appointment status has changed. Please Try again.", "", "");
                }
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid appointment or appointment is deleted.", "", "");
            }
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again.", "", "");
        }
    }

    // Ended by Jyoti, 08-march-2017
    /**
     * fromDate Added by Jyoti, 06-march-2017
     *
     * @param appointmentRecurringId
     * @param appointmentId
     * @param loginId
     * @param assignedTo
     * @param activity
     * @param userToken
     * @return
     * @purpose to delete (recurring) selected visit and its following visit
     */
    public Object deleteThisAndFollowingAppointment(int appointmentRecurringId, int appointmentId, int loginId, int assignedTo, String activity, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            int accountId = fieldSenseUtils.accountIdForToken(userToken);
            if (appointmentDao.isAppointmentValid(appointmentId, accountId)) {
                if (appointmentDao.deleteRecurringAppointment(appointmentRecurringId, appointmentId, accountId)) {
                    if (appointmentDao.deleteAppointmentPossition(appointmentId, accountId)) {
                        /* Push notification for delete appointment  starts */
                        if (loginId != assignedTo) {
                            java.util.HashMap gcmInfo = fieldSenseUtils.getGcmUserInfo(assignedTo);
                            String gcmId = (String) gcmInfo.get("gcmId");
                            int deviceOS = (Integer) gcmInfo.get("deviceOS");

                            String message = activity + " is deleted";
                            PushNotificationManager push = new PushNotificationManager();
                            push.activityNotification(message, gcmId, deviceOS);
                        }
                        /* Push notification for delete appointment  starts */
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.APPOINTMENT_DELETE_SUCCESS, "", "");
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Appointment deletion failed . Please try again . ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Appointment status has been changed.", "", "");
                }
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid appointment or appointment is deleted.", "", "");
            }
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    // Ended by jyoti march-06-2017
    // end , recurring visits
    // rework end

	 public Object createAppointmentMobileList(Appointment appointment, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                int userId = fieldSenseUtils.userIdForToken(userToken);
                
//                System.out.println("appointment.getSdateTime()="+appointment.getSdateTime());
//                System.out.println("appointment.getSendTime()="+appointment.getSendTime());
                
                User user=new User();

                int assigned_id_fk=appointment.getAssignedTo().getId();
                List<java.util.HashMap> allAppointment = appointmentDao.selectAllAppointForRelevantCustomerOfUserForGivenDateList(appointment.getSdateTime(), appointment.getSendTime(),assigned_id_fk, accountId);
                /* for (HashMap allAppointment1 : allAppointment) {
//                     System.out.println("all="+allAppointment);
            }
                
                
                List<Appointment> allAppointment = appointmentDao.selectAllAppointForRelevantCustomerOfUserForGivenDate(appointment.getSdateTime(), appointment.getSendTime(),assigned_id_fk, accountId);
               
                List<Map<String,Object>> listofhmap=new ArrayList<>();
//            System.out.println("allAppointment.size()="+allAppointment.size());  
        
            Map<String,Object> hmap = null;
        
           for (Appointment allAppointment1 : allAppointment) {
                hmap=new HashMap<>();
                hmap.put("customerName", allAppointment1.getCustomer().getCustomerName().toString());
                hmap.put("dateTime", allAppointment1.getDateTime().toString());
                hmap.put("endTime", allAppointment1.getEndTime().toString());
                hmap.put("purpose", allAppointment1.getPurpose().getPurpose().toString());
                hmap.put("id", allAppointment1.getAssignedTo().getId());
//                
//                   System.out.println("getCustomer="+allAppointment1.getCustomer().getCustomerName());
//                    System.out.println("getDateTime="+allAppointment1.getDateTime());
//                    System.out.println("getEndTime="+allAppointment1.getEndTime());
//                    System.out.println("getPurpose="+allAppointment1.getPurpose().getPurpose());
//                    System.out.println("getAssignedTo="+allAppointment1.getAssignedTo().getId());  
                 listofhmap.add(hmap);
                }
                */
               return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.APPOINTMENT_CREATE_SUCCESS, " appointment   ", allAppointment);
               
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
}
