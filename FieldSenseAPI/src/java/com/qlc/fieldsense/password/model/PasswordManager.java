 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.password.model;

import com.qlc.fieldsense.user.dao.UserDao;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.EmailNotification;
import com.qlc.fieldsense.utils.FieldSensePasswordEncryptionDecryption;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import com.qlc.fieldsense.utils.MessageNotifications;
import java.sql.Timestamp;
import org.apache.log4j.Logger;

/**
 *
 * @author anuja
 */
public class PasswordManager {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("ForgotPasswordManager");
    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
    UserDao user = (UserDao) GetApplicationContext.ac.getBean("userDaoImpl");
    private EmailNotification sendEmail = new EmailNotification();

/**
 * 
 * @param email
     * @return 
 * @purpose Used to send forgot password request.
 */
    public Object forgotPassword(Password email) {
        String emailId = email.getEmailId();
      //  if (FieldSenseUtils.isDomainValid(emailId)) {
            if (FieldSenseUtils.isValidEmailAddress(emailId)) {
                if (fieldSenseUtils.isUserRegisterd(emailId)) {
                    String userName = fieldSenseUtils.getUserFirstName(emailId) + " " + fieldSenseUtils.getUserLastName(emailId);
                    EmailNotification emailNotification = new EmailNotification();
                    emailNotification.forgotPassword(userName, emailId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "An email has been sent to you with instructions to reset your password.", "", "");
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.EMAIL_NOT_REGISTERD, "", "");
                }
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_EMAIL, "", "");
            }
       // } else {
       //     return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_DOMAIN, "", "");
        //}
    }

    /**
     * 
     * @param password
     * @return 
     * @purpose Used to reset password.
     */
    public Object resetPassword(Password password) {
        if (password.getNewPassword().equals(password.getConfirmPassword())) {
            password.setNewPassword(FieldSensePasswordEncryptionDecryption.hashPassword(password.getNewPassword()));
            if (user.resetPassword(password)) {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.SUCCESS_PASSWORD, "", "");
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.FAIL_PASSWORD, "", "");
            }
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_CONFIRM_PASSWORD, "", "");
        }
    }

    /**
     * 
     * @param email
     * @return 
     * @purpose Used to set password.
     * @deprecated 
     */
    public Object changePasswordEmail(Password email) {
        String emailId = email.getEmailId();
       // if (FieldSenseUtils.isDomainValid(emailId)) {
            if (FieldSenseUtils.isValidEmailAddress(emailId)) {
                if (fieldSenseUtils.isEmailUnique(emailId)) {
                    String fname = fieldSenseUtils.getUserFirstName(emailId);
                    String lname = fieldSenseUtils.getUserLastName(emailId);
                    String userName = fname + " " + lname;
                    String subject = "Invitation to FieldSense registration";
                    String url = Constant.WEBSITE_PATH + "setPassword.html?email=" + emailId;
                    String Msg = "Dear " + userName + ",<br><br> Welcome to FieldSense.<br>You are added successfully in the FiledSense.<br> Please click <a href=" + url + "> here </a>to complete the registration process.<br> Please log into FieldSense with your Email ID : " + emailId + " and new password.<br><br> Thanks, <br> FieldSense Team";
                    sendEmail.sendMail(emailId, Msg, subject);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "An email has been sent to you with instructions to reset your password.", "", "");
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_EMAIL, "", "");
                }
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_EMAIL, "", "");
            }
       // } else {
       //     return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_DOMAIN, "", "");
       // }
    }

    /**
     * 
     * @param password
     * @param userToken
     * @return 
     * @purpose Used to change password. 
     */
    public Object changePassword(Password password, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                String emailAddress = fieldSenseUtils.userEmailIdForToken(userToken);
                password.setEmailId(emailAddress);
                String userPassword = user.getUserPassword(password.getEmailId());
                if (FieldSensePasswordEncryptionDecryption.hashPassword(password.getOldPassword()).equals(userPassword)) {
                    if (password.getNewPassword().equals(password.getConfirmPassword())) {
                        password.setNewPassword(FieldSensePasswordEncryptionDecryption.hashPassword(password.getNewPassword()));
                        if (user.resetPassword(password)) {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "Password Changed Successfully.", "", "");
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Password updation failed please try again ", "", "");
                        }
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " New password and confirm password is not matched .", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Old password is wrong .", "", "");
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
     * @param mobile
     * @return
     */
    public Object getOTP(String mobile) {
        //int firstLoginValue = authenticationUserDao.selectUserFirstLogin(login.getUserEmailAddress()); // retrive user first login flag
        //String otp="";
        java.util.Random rnd = new java.util.Random();
        String otp = (100000 + rnd.nextInt(900000))+"";
        
        /*java.util.List<User> userList=user.getUsersFromMobileNo(mobile);
        if(userList.size()>1){
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "", "There are multiple users accociated with same number. Please Contact Administrator ", "");
        }
        if(userList.size()<1){
             return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "", "Please Contact Administrator ", "");            
        }
        mobile=userList.get(0).getMobileNo();*/
        int result = user.insertOTPForUser(mobile,otp);
        
        if (result == 1) {
            MessageNotifications notify= new MessageNotifications();
            notify.sendOTP(otp, mobile);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "OTP Sent .", "", mobile);
        } else if(result >1) {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "There are multiple accounts with this mobile number. Please contact your account administrator.", "User is not registered ", mobile);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Sorry, we couldn’t find an account with that mobile number. Please contact your account administrator.", "User is not registered ", mobile);
        }
    }
    
    /**
     *
     * @param authObject
     * @return
     */
    public Object verifyOTP(java.util.HashMap authObject) {
        String mobile= (String)authObject.get("mobile");
        String otp= (String)authObject.get("otpkey");
        String newPassword= (String)authObject.get("newPassword");

        
        java.util.List authDetails= user.getOTPAndExpiryForUser(mobile); // retrive user first login flag
        Timestamp expiry= (Timestamp)authDetails.get(1);
        String otpfromdb= (String)authDetails.get(0);
        
        if(expiry ==null || otpfromdb ==null){
        return FieldSenseUtils.generateFieldSenseResponse(Constant.FAILED, Constant.FAILED, "OTP does not match.", "", "OTP does not match.");

        }
        long diff=System.currentTimeMillis()-expiry.getTime();
 
         // Now compare time with from autdetail with current time .. it should not exceed more than 30 min
        if (diff <(30*60*1000)) {
            if(otpfromdb.equals(otp)){
                String hashPass=FieldSensePasswordEncryptionDecryption.hashPassword(newPassword);
                if(user.resetPasswordUsingPhone(hashPass, mobile))
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "OTP verified .", "", "OTP verified .");
                else{
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.FAILED, Constant.FAILED, "Please Try again .", "", "Please try again .");

                }
            }else{
                return FieldSenseUtils.generateFieldSenseResponse(Constant.FAILED, Constant.FAILED, "OTP does not matched .", "", "OTP does not matched .");
            }      
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.FAILED, Constant.FAILED, "", "OTP expired", "OTP expired");
        }
    }
}
