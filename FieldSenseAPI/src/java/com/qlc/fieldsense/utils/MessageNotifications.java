/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.utils;

import static com.qlc.fieldsense.user.service.UserService.log4jLog;

/**
 *
 * @author Ramesh
 * @date 22-06-2015
 */
public class MessageNotifications {

    FieldSenseUtils fieldSenseUtils = new FieldSenseUtils();

    /**
     *
     * @param userFirstName
     * @param userLastName
     * @param adminFirstName
     * @param adminLastName
     * @param senderMobileNo
     */
    public void createUser(String userFirstName, String userLastName, String adminFirstName, String adminLastName, String senderMobileNo) {
        try{
            // Added by jyoti, purpose - to handle space. replace all space with %20
            if(userFirstName.contains(" ")){
                userFirstName = userFirstName.replaceAll(" ", "%20");
            }
            if(userLastName.contains(" ")){
                userLastName = userLastName.replaceAll(" ", "%20");
            }
            if(adminFirstName.contains(" ")){
                adminFirstName = adminFirstName.replaceAll(" ", "%20");
            }
            if(adminLastName.contains(" ")){
                adminLastName = adminLastName.replaceAll(" ", "%20");
            }
            // Ended by Jyoti
            String downloadUrl = FieldSenseUtils.getPropertyValue("DOWNLOAD_URL");
            String message = "Dear%20" + userFirstName + "%20" + userLastName + ",%20" + adminFirstName + "%20" + adminLastName + "%20has%20created%20an%20Employee%20Account%20for%20you%20on%20FieldSense.%20Download%20the%20app%20from%20" + downloadUrl + ".%20Then%20Launch%20the%20app%20and%20Sign%20In";
            String url = FieldSenseUtils.getPropertyValue("MESSAGE_SEND_URL") + "&destination=" + senderMobileNo + "&source=FIELDS&message=" + message;
            fieldSenseUtils.fieldSenseUrlCall(url);
//            System.out.println("message : "+message);
        } catch(Exception e){
            e.printStackTrace();
            log4jLog.info("under_msg_notification_createUser_>>>"+e);
        }
    }
      
    /**
     * @Added by Jyoti, purpose - to change update message for user
     * @param userFirstName
     * @param userLastName
     * @param adminFirstName
     * @param adminLastName
     * @param senderMobileNo
     */
    public void updateUserMessage(String userFirstName, String userLastName, String adminFirstName, String adminLastName, String senderMobileNo) {
        try{
            // Added by jyoti, purpose - to handle space. replace all space with %20
            if(userFirstName.contains(" ")){
                userFirstName = userFirstName.replaceAll(" ", "%20");
            }
            if(userLastName.contains(" ")){
                userLastName = userLastName.replaceAll(" ", "%20");
            }
            if(adminFirstName.contains(" ")){
                adminFirstName = adminFirstName.replaceAll(" ", "%20");
            }
            if(adminLastName.contains(" ")){
                adminLastName = adminLastName.replaceAll(" ", "%20");
            }
            // Ended by Jyoti
            String downloadUrl = FieldSenseUtils.getPropertyValue("DOWNLOAD_URL");
            String message = "Dear%20" + userFirstName + "%20" + userLastName + ",%20" + adminFirstName + "%20" + adminLastName + "%20has%20updated%20an%20Employee%20Account%20for%20you%20on%20FieldSense.%20Download%20the%20app%20from%20" + downloadUrl + ".%20Then%20Launch%20the%20app%20and%20Sign%20In";
            String url = FieldSenseUtils.getPropertyValue("MESSAGE_SEND_URL") + "&destination=" + senderMobileNo + "&source=FIELDS&message=" + message;
            fieldSenseUtils.fieldSenseUrlCall(url);
//            System.out.println("message : "+message);
        } catch(Exception e){
            e.printStackTrace();
            log4jLog.info("under_msg_notification_createUser_>>>"+e);
        }
    }
    
    
    /**
     *
     * @param senderMobileNo
     */
    public void createUser( String senderMobileNo) {
        String message = "Your%20FieldSense%20account%20has%20been%20created%20successfully.%20Please%20check%20your%20registered%20email%20for%20login%20credentials.%20Thanks%20-%20FieldSense%20Team";
        String url = FieldSenseUtils.getPropertyValue("MESSAGE_SEND_URL") + "&destination=" + senderMobileNo + "&source=FIELDS&message=" + message;
        fieldSenseUtils.fieldSenseUrlCall(url);
    }
    
    /**
     *
     * @param otpkey
     * @param senderMobileNo
     */
    public void sendOTP(String otpkey, String senderMobileNo) {
        String message="Your%20One%20Time%20Password%20for%20FieldSense%20Login%20is%20"+otpkey+".%20Use%20this%20password%20to%20login";
        String url = FieldSenseUtils.getPropertyValue("MESSAGE_SEND_URL") + "&destination=" + senderMobileNo + "&source=FIELDS&message=" + message;
        fieldSenseUtils.fieldSenseUrlCall(url);
    }
    
        public boolean sendOTP1(String otpkey, String senderMobileNo ,String Country_code) {
//         System.out.println("sendOTP1 "+senderMobileNo);
//         System.out.println("sendOTP1 Country_code:-"+Country_code);
        String message="Your%20One%20Time%20Password%20for%20FieldSense%20Login%20is%20"+otpkey+".%20Use%20this%20password%20to%20login";
        String url="";
        if(Country_code.endsWith("91"))
        {
//            System.out.println("National sms");
            url = FieldSenseUtils.getPropertyValue("MESSAGE_SEND_URL") + "&destination=" + senderMobileNo + "&source=FIELDS&message=" + message;
        }else
        {
//            System.out.println("International sms");
            url = FieldSenseUtils.getPropertyValue("WORLD_MESSAGE_SEND_URL") + "&destination=" + senderMobileNo + "&source=FIELDS&message=" + message; 
        }
        fieldSenseUtils.fieldSenseUrlCall(url);
        return true;
    }
}
