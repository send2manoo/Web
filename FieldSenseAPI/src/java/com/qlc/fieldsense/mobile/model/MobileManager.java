 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.mobile.model;

import com.qlc.fieldsense.accounts.dao.AccountSettingDao;
import com.qlc.fieldsense.attendance.dao.AttendanceDao;
import com.qlc.fieldsense.mobile.dao.MobileDao;
import com.qlc.fieldsense.team.dao.TeamDao;
import com.qlc.fieldsense.user.dao.UserDao;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.EmailNotification;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Ramesh
 * @date 06-06-2014
 */
public class MobileManager {

    MobileDao mobileDao = (MobileDao) GetApplicationContext.ac.getBean("mobileDao");
    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
    AttendanceDao attendanceDao = (AttendanceDao) GetApplicationContext.ac.getBean("attendanceDaoImpl");
    UserDao userDao = (UserDao) GetApplicationContext.ac.getBean("userDaoImpl");
    AccountSettingDao accountSettingDaoImpl = (AccountSettingDao) GetApplicationContext.ac.getBean("accountSettingDaoImpl");
    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("MobileManager");

     /**
     * 
     * @param date
     * @param userToken
     * @return 
     * @purpose Used to get leftslidermenu details for mobile.
     */
    public Object selectLeftSliderMenuDetails(String date, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                int userId = fieldSenseUtils.userIdForToken(userToken);
                int versionCode=fieldSenseUtils.selectAppVersionForUser(userId);
                
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateUtil = new Date();
                String timeAfter24Hours = "";
                String dateAfter24Hours="";
                
                try {
                    dateUtil = parser.parse(date);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateUtil);
                    calendar.add(Calendar.HOUR_OF_DAY, 24);
                    dateUtil = calendar.getTime();
                    timeAfter24Hours = parser.format(dateUtil);
                    dateAfter24Hours=timeAfter24Hours.split(" ")[0];
                } catch (ParseException ex) {
                    log4jLog.info(" selectLeftSliderMentDetails " + ex);
                }
                
                LeftSliderMenu leftSliderMenu =null;
                /*if(versionCode<22){
                    leftSliderMenu = mobileDao.getLeftSliderMenuData(date, timeAfter24Hours, userId, accountId);
                    leftSliderMenu.setAttendanceStatus(attendanceDao.getPunchStatus(userId, accountId));
                    leftSliderMenu.setAttendanceTiemout(attendanceDao.selectLastTimeoutOfDayForUser(userId, dateAfter24Hours, accountId));//Added by Awaneesh
                }else{*/
                    leftSliderMenu = mobileDao.getLeftSliderMenuData(date, timeAfter24Hours,versionCode, userId, accountId);
                    // Need to be handled at mobile side
                    //Added by Awaneesh Required by madhuri for overnight punch issue
                    String sDate=timeAfter24Hours.split(" ")[0];
                    if(!leftSliderMenu.getPunchOutTime().equals("00:00:00") && !leftSliderMenu.getPunchOutTime().equals("") && ! sDate.equals(leftSliderMenu.getPunchDate())){
                        leftSliderMenu.setPunchInTime("");
                        leftSliderMenu.setPunchOutTime("");
                        leftSliderMenu.setPunchDate(sDate);
                    }
                    //End
                   
                    leftSliderMenu.setAttendanceStatus(attendanceDao.getPunchStatus(userId, accountId));
                    //leftSliderMenu.setAttendanceTiemout(attendanceDao.selectLastTimeoutOfDayForUser(userId, dateAfter24Hours, accountId));//Added by Awaneesh
                    leftSliderMenu.setAttendanceTiemout(attendanceDao.selectLastTimeoutOfDayForUser(userId, leftSliderMenu.getPunchDate(), accountId));//Added by Awaneesh
                //}
                
                User user = userDao.selectUser(userId);
                int user_accuracy= user.getUserAccuracy();
                int user_check_in_radius=user.getCheckInRadius();
                int allowTimeout= 0;
                int offlineOperation = 0;
                String allowTimeoutValue=accountSettingDaoImpl.getValueFromAccountSetting("allow_timeout",accountId);
                 
                //added by Jyoti
                String allowOfflineValue=accountSettingDaoImpl.getValueFromAccountSetting("allow_offline",accountId);                
                if(allowOfflineValue.trim().equals("1")){
                    offlineOperation= user.getAllowOffline();
                }
                //End by Jyoti                    
                if(allowTimeoutValue.trim().equals("1")){
                    allowTimeout=user.getAllowTimeout();
                }                
                leftSliderMenu.setAllowTimeout(allowTimeout);
                leftSliderMenu.setAllowOfflineOperation(offlineOperation);  // added by jyoti
                leftSliderMenu.setUserAccuracy(user_accuracy);
                leftSliderMenu.setCheckInRadius(user_check_in_radius);
                
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " leftSliderMenu Details", leftSliderMenu);
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
     * @param versionCode
     * @param userToken
     * @return 
     * @purpose Used to get leftslidermenu details for mobile.
     */
    public Object selectLeftSliderMenu(String date,int versionCode, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
           // System.out.println("reaching manager");
            
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                int userId = fieldSenseUtils.userIdForToken(userToken);
                //int versionCode=fieldSenseUtils.selectAppVersionForUser(userId);
                
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateUtil = new Date();
                String timeAfter24Hours = "";
                String dateAfter24Hours="";
                
                try {
                    dateUtil = parser.parse(date);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateUtil);
                    calendar.add(Calendar.HOUR_OF_DAY, 24);
                    dateUtil = calendar.getTime();
                    timeAfter24Hours = parser.format(dateUtil);
                    dateAfter24Hours=timeAfter24Hours.split(" ")[0];
                } catch (ParseException ex) {
                    log4jLog.info(" selectLeftSliderMentDetails " + ex);
                }
                
                LeftSliderMenu leftSliderMenu =null;
                leftSliderMenu = mobileDao.getLeftSliderMenuData(date, timeAfter24Hours,versionCode, userId, accountId);
                //Siddhesh:-Commented because requirment changed no need to send subordinate data.
                
                //leftSliderMenu.setHasSubordinates(mobileDao.getSubordinates(userId, accountId));
                //leftSliderMenu.setHasSubordinates(0);
                //End siddhesh 
                
//                /*Need to be handled at mobile side*/
//                //Added by Awaneesh Required by madhuri for overnight punch issu
//                String sDate=timeAfter24Hours.split(" ")[0];
//                if(!leftSliderMenu.getPunchOutTime().equals("00:00:00") && !leftSliderMenu.getPunchOutTime().equals("") && ! sDate.equals(leftSliderMenu.getPunchDate())){
//                    leftSliderMenu.setPunchInTime("");
//                    leftSliderMenu.setPunchOutTime("");
//                    leftSliderMenu.setPunchDate(sDate);
//                }
//                //End 
//                
                leftSliderMenu.setAttendanceStatus(attendanceDao.getPunchStatus(userId, accountId));
                //leftSliderMenu.setAttendanceTiemout(attendanceDao.selectLastTimeoutOfDayForUser(userId, dateAfter24Hours, accountId));//Added by Awaneesh
                leftSliderMenu.setAttendanceTiemout(attendanceDao.selectLastTimeoutOfDayForUser(userId, leftSliderMenu.getPunchDate(), accountId));//Added by Awaneesh
                User user = userDao.selectUser(userId);
                int user_accuracy= user.getUserAccuracy();
                int user_check_in_radius=user.getCheckInRadius();
                int allowTimeout= 0;
                int offlineOperation = 0;
                
                // added by manohar
                List<LeftSliderMenu> punchList=new ArrayList<LeftSliderMenu>();
                List<LeftSliderMenu> checkList=new ArrayList<LeftSliderMenu>();
                List<LeftSliderMenu> getstatus=new ArrayList<LeftSliderMenu>();
                
                punchList=attendanceDao.getPunchList(userId,accountId);
                checkList=attendanceDao.getCheckList(userId,accountId);
                getstatus=attendanceDao.getStatus(userId, accountId);
                
//                System.out.println("CheckList"+checkList.size()+"Punchin list"+punchList.size());
                if(getstatus.size()!=0)
                {
                   leftSliderMenu.setAppointmentId(getstatus.get(0).getAppointmentId());
                   leftSliderMenu.setCustomerId(getstatus.get(0).getCustomerId());
                }
                if(checkList.size()!=0){
                   leftSliderMenu.setLastcheckInTime(checkList.get(0).getLastcheckInTime());
                   leftSliderMenu.setLastcheckOutTime(checkList.get(0).getLastcheckOutTime());
                 
                }else{
                   leftSliderMenu.setLastcheckInTime("1970-01-01 00:00:00");
                   leftSliderMenu.setLastcheckOutTime("1970-01-01 00:00:00");                 
                }
                if(punchList.size()!=0){
               leftSliderMenu.setLastpunchInTime(punchList.get(0).getLastpunchInTime());
               leftSliderMenu.setLastpunchOutTime(punchList.get(0).getLastpunchOutTime());
                if (punchList.get(0).getLastpunchOutTime().equals("00:00:00")) {
                       leftSliderMenu.setAttendanceStatus(true);
                    } else {
                        leftSliderMenu.setAttendanceStatus(false);
                        //mapOfUserInfo.put("status","PunchIn");
                    }
                }else{
               leftSliderMenu.setLastpunchInTime("00:00:00");
                leftSliderMenu.setLastpunchOutTime("00:00:00");
                leftSliderMenu.setAttendanceStatus(false);
                }
               // ended by manohar
                String Interval ="";
                //nikhil
                
                
                String allowTimeoutValue=accountSettingDaoImpl.getValueFromAccountSetting("allow_timeout",accountId);
                
                //added by Jyoti
                String allowOfflineValue=accountSettingDaoImpl.getValueFromAccountSetting("allow_offline",accountId);
                
                //added by nikhil 29-5 - 2017
                String locationInterval = userDao.selectlocationInterval(userId);
                
                if(locationInterval.trim().equals("0"))
                {
                    Interval = accountSettingDaoImpl.getValueFromAccountSetting("location_interval",accountId);
                    
                }else 
                {
                   Interval= locationInterval;
                }
//                //ended by nikhil
                
                    if(allowOfflineValue.trim().equals("1")){
                        offlineOperation= user.getAllowOffline();
                    }
                //End by Jyoti
                    
                    if(allowTimeoutValue.trim().equals("1")){
                        allowTimeout=user.getAllowTimeout();
                    }
                leftSliderMenu.setAllowTimeout(allowTimeout);
                leftSliderMenu.setAllowOfflineOperation(offlineOperation);  // added by jyoti
                leftSliderMenu.setUserAccuracy(user_accuracy);
                leftSliderMenu.setCheckInRadius(user_check_in_radius);
                //nikhil
                leftSliderMenu.setLocationInterval(Interval);
                int userRole=user.getRole();
                leftSliderMenu.setUserRole(userRole);
                try{
                    Map<String,Object> maxUpdatedOn=new HashMap<String,Object>();
                    maxUpdatedOn=mobileDao.lastUpdatedOfTerritoriesWithCount(userRole,userId,accountId);
//                    leftSliderMenu.setTerritoryLastUpdated(lastTerritoryUpdated);
                
                    Timestamp createdOn=fieldSenseUtils.converDateToTimestamp(maxUpdatedOn.get("created_on").toString());
                    Timestamp updateOn=fieldSenseUtils.converDateToTimestamp(maxUpdatedOn.get("updated_on").toString());
                    leftSliderMenu.setTerritoryCount(Integer.parseInt(maxUpdatedOn.get("id_count").toString()));
                    if(createdOn.before(updateOn)){
                        leftSliderMenu.setTerritoryLastUpdated(updateOn);
                    }else{
                        leftSliderMenu.setTerritoryLastUpdated (createdOn);
                    }
                }catch(Exception e){
                    log4jLog.info("selectLeftSliderMenu" + e);
                    e.printStackTrace();
                }    
                
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " leftSliderMenu Details", leftSliderMenu);
        // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    
    /**
     * 
     * @param file
     * @param logsFile
     * @return
     * @throws IOException 
     * @purpose Used to send log file from mobile. You have to upload file for this API.
     */
    public boolean sendLogsFileAsEmail(MultipartFile file) {
        try {
            File convFile = new File(file.getOriginalFilename());
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
            EmailNotification emailNotification = new EmailNotification();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            Date date = new Date();
            String sDate = sdf.format(date);
            StringBuilder message = new StringBuilder();
            message.append("Dear FieldSense Dev Team,\r\n\r\n");
            message.append("Find the attached log file generated through FieldSense Android App.\r\n");
            message.append("The log file was generated on ");
            message.append(sDate);
            message.append(" GMT.\r\n\r\n");
            message.append("Thanks\r\n");
            message.append("App Feedback\r\n");
            String mailTo = "feedback@fieldsense.in";
            if (emailNotification.sendEmailWithAttachments(mailTo, message.toString(), "User log", convFile)) {
                return true;
            } else {
                return true;
            }
        } catch (IOException e) {
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * @author Ramesh
     * @param appType  0- ondroid 1- ios
     * @return Json Object
     * @purpose This method gives the current version of the app in appstore .
     */
    public Object selectAppVersion(int appType) {
        if (appType == 0 || appType == 1) {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "app version", 9.9);//this api is supported till 2.3 , we will return 9.9 so all users having version till 2.3 will be prompt to update
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid appType please try again .", "", "");
        }
    }

    /**
     * @author Ramesh
     * @param appType  0- ondroid 1- ios
     * @return Json Object
     * @purpose This method gives the current version of the app in appstore .
     */
    public Object selectAppVersionDetails(int appType) {
        if (appType == 0 || appType == 1) {
            HashMap appVersion = mobileDao.selectAppVersionDetails(appType);
            if (appVersion.isEmpty()) {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Problem to get the app version please try again", "", "");
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "app version", appVersion);
            }
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid appType please try again .", "", "");
        }
    }
    
    
    /**
     * @author Awaneesh
     * @param name filename 
     * @param file debug file from mobile
     * @param userToken
     * @return Json Object
     * @purpose This method gives the current version of the app in appstore .
     */
    @Deprecated
    public Object uploadMobileDebugFileHandler( String name, MultipartFile file,String userToken) {
            if (fieldSenseUtils.isTokenValid(userToken)) {

		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				// Creating the directory to store file
				//String rootPath = System.getProperty("catalina.home");
                                String debugPath=Constant.MOBILE_APP_DEBUG_UPLOAD_PATH;
				File dir = new File(debugPath);
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath()
						+ File.separator + name);
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				log4jLog.info("Server File Location="+ serverFile.getAbsolutePath());

                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Mobile Debug Upload", "You successfully uploaded file=" + name);

			} catch (Exception e) {
//                            e.printStackTrace();
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Problem in uploading file.", "", "You failed to upload " + name + " => " + e.getMessage());
			}
		} else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Problem in uploading file.", "", "You failed to upload " + name	+ " because the file was empty.");
		}
            }else{
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");

            }
	}
    
    /**
     *
     * @param name
     * @param fileType
     * @param file
     * @param userToken
     * @return
     */
    public Object uploadFileHandler( String name,int fileType, MultipartFile file,String userToken) {
            String debugPath="";
            
            if (fieldSenseUtils.isTokenValid(userToken)) {
		if (!file.isEmpty()) {
                    
                        switch(fileType){
                            case 1:debugPath=Constant.MOBILE_APP_DEBUG_UPLOAD_PATH;
                                   break;
                            case 2:debugPath=Constant.MOBILE_TRAVEL_LOG_UPLOAD_PATH;
                                   break;
                            default:debugPath="";    
                        }
                        if(debugPath.equals("")){
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Problem in uploading file.", "", "You failed to upload " + name	+ " because of incorrect filetype");
                        }
                    
			try {
				byte[] bytes = file.getBytes();
				// Creating the directory to store file
				//String rootPath = System.getProperty("catalina.home");
                                
				File dir = new File(debugPath);
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath()
						+ File.separator + name);
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				//log4jLog.info("Server File Location="+ serverFile.getAbsolutePath());

                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "File Upload", "You successfully uploaded file=" + name);

			} catch (Exception e) {
//                            e.printStackTrace();
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Problem in uploading file.", "", "You failed to upload " + name + " => " + e.getMessage());
			}
		} else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Problem in uploading file.", "", "You failed to upload " + name	+ " because the file was empty.");
		}
            }else{
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");

            }
	}
    
    /**
     * @Added by jyoti 23-08-2017
     * @purpose to update user Preferences for currency_symbol
     * @param currencySymbol
     * @param userToken
     * @return 
     */
    public Object updateUserPreferences(String currencySymbol, String userToken){
        if (fieldSenseUtils.isTokenValid(userToken)) {
            int userID = fieldSenseUtils.userIdForToken(userToken);
            if (fieldSenseUtils.isUserValid(userID)) {
                if (mobileDao.updateUserPreferences(currencySymbol, userID)) {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " User Preferences Updated Successfully.", "user", "");
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " User Preferences Updation Failed. Please try again. ", "", "");
                }
            }else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid userId. Please try again. ", "", "");
            } 
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again. ", "", "");
        }
    }
    
    public Object selectLeftSliderMenu_V2(String date,int versionCode, String userToken) {
        int accountId = fieldSenseUtils.accountIdForToken(userToken);
        int userId = fieldSenseUtils.userIdForToken(userToken);
        try{
            if (fieldSenseUtils.isTokenValid(userToken)) {
                // Adding lefsliderData                
                java.util.HashMap hMap = fieldSenseUtils.getDateTime();
                String currentDateTime = (String) hMap.get("currentDateTime");
                String dateTimeAfter24Hours = (String) hMap.get("dateTimeAfter24Hours");
//                System.out.println("selectLeftSliderMenu_V2 versioncode : "+versionCode+"leftslider called for userid : "+userId+", dates : "+ currentDateTime + ", "+dateTimeAfter24Hours);
                LeftSliderMenu leftSliderMenu = mobileDao.getLeftSliderMenuV2(currentDateTime, dateTimeAfter24Hours, userId, accountId);
                
                // get Account level settings
                int allowTimeoutValue = 0, allowOfflineValue = 0, auto_punch_out_type = 0, working_hours = 0;
                String autoPunchOutTime = "";
                String location_intervalAccount = "";
                List<com.qlc.fieldsense.accounts.model.AccountSetting> accountSettings = accountSettingDaoImpl.selectAllAccountSettings(accountId);
                for(com.qlc.fieldsense.accounts.model.AccountSetting settings : accountSettings){
                    switch (settings.getSettingName()) { // modified by jyoti, changed if to switch
                        case "allow_timeout":
                            allowTimeoutValue = Integer.parseInt(settings.getSettingValue().trim());
                            break;
                        case "allow_offline":
                            allowOfflineValue = Integer.parseInt(settings.getSettingValue().trim());
                            break;
                        case "Location_interval":
                            location_intervalAccount = settings.getSettingValue().trim();
                            break;
                        case "auto_punch_out_time":     // added by jyoti, Feature #29532
                            autoPunchOutTime = settings.getSettingValue().trim(); 
                            break;
                        case "auto_punch_out_type":     // added by jyoti, Feature #29532
                            auto_punch_out_type = Integer.parseInt(settings.getSettingValue().trim());
                            break;
                        case "working_hours":     // added by jyoti, Feature #29532
                            working_hours = Integer.parseInt(settings.getSettingValue().trim());
                            break;
                    }
                }
                
                // user data
                java.util.HashMap hMap_userData = fieldSenseUtils.getUserDataLeftSlider(userId, accountId);
                    //update versionCode
                int appVersionOfUser = Integer.parseInt(hMap_userData.get("appVersion").toString());
//                System.out.println("selectLeftSliderMenu_V2 appVersionOfUser : "+appVersionOfUser);
                if(versionCode != appVersionOfUser){
                    if(userDao.updateUserAppVersion(userId, versionCode))
                    leftSliderMenu.setAppVersion(versionCode);
                }
                
                leftSliderMenu.setAuto_punch_out_type(auto_punch_out_type); // added by jyoti, Feature #29532
                leftSliderMenu.setAuto_punch_out_time(autoPunchOutTime);  // added by jyoti, Feature #29532
                leftSliderMenu.setWorking_hours(working_hours);  // added by jyoti, Feature #29532
//                System.out.println("selectLeftSliderMenu_V2 autoPunchoutType > "+ auto_punch_out_type + " , autoPunchoutTime > "+ autoPunchOutTime + " working_hours >> "+working_hours); // comment later
                leftSliderMenu.setAppVersion(appVersionOfUser);
                leftSliderMenu.setUserRole(Integer.parseInt( hMap_userData.get("role").toString()));
                leftSliderMenu.setUserAccuracy(Integer.parseInt( hMap_userData.get("userAccuracy").toString()));
                leftSliderMenu.setCheckInRadius(Integer.parseInt( hMap_userData.get("checkInRadius").toString()));
                leftSliderMenu.setAccountName(hMap_userData.get("accountName").toString());
                String locationIntervalUser = hMap_userData.get("interval").toString();
                leftSliderMenu.setHasSubordinates(Integer.parseInt(hMap_userData.get("hasSubordinates").toString()));
                if(allowTimeoutValue == 1){ // if account level allowTimeoutValue value is true then set user level value else take default '0'
                    leftSliderMenu.setAllowTimeout(Integer.parseInt(hMap_userData.get("allowTimeout").toString())); // set allowtimeout status
                    if(leftSliderMenu.getAllowTimeout() == 1){
                        leftSliderMenu.setAttendanceTiemout(attendanceDao.selectLastTimeoutOfDayForUser(userId, leftSliderMenu.getPunchDate(), accountId));
                    }
                }                
                if(allowOfflineValue == 1){ // if account level allowOfflineValue value is true then set user level value else take default '0'
                    leftSliderMenu.setAllowOfflineOperation(Integer.parseInt(hMap_userData.get("allowOffline").toString())); // set allowoffline status
                }
                if(locationIntervalUser.trim().equals("0")){ // if user locationinterval value is 0 then set account level locationinterval value, 24-jul-2018
                    leftSliderMenu.setLocationInterval(location_intervalAccount);
                } else{
                    leftSliderMenu.setLocationInterval(locationIntervalUser);
                }
                if(leftSliderMenu.getPunchOutTime().equals("00:00:00")){
                    leftSliderMenu.setPunchOutTime(""); // requested by pune team, as 00:00:00 was considered as midnight time
                    leftSliderMenu.setAttendanceStatus(true);
                } else {
                    leftSliderMenu.setAttendanceStatus(false);
                }
//                System.out.println("selectLeftSliderMenu_V2 isAttendanceStatus > "+ leftSliderMenu.isAttendanceStatus()); // comment later
                java.util.Map<String,Object> maxUpdatedOn = mobileDao.lastUpdatedOfTerritoriesWithCount(0,userId,accountId);
                Timestamp createdOn=fieldSenseUtils.converDateToTimestamp(maxUpdatedOn.get("created_on").toString());
                Timestamp updateOn=fieldSenseUtils.converDateToTimestamp(maxUpdatedOn.get("updated_on").toString());
                leftSliderMenu.setTerritoryCount(Integer.parseInt(maxUpdatedOn.get("id_count").toString()));
                if(createdOn.before(updateOn)){
                    leftSliderMenu.setTerritoryLastUpdated(updateOn);
                }else{
                    leftSliderMenu.setTerritoryLastUpdated (createdOn);
                }

//                java.util.HashMap hMap_punchinout = fieldSenseUtils.getLastPunchInOutData(userId,accountId);
//                java.util.HashMap hMap_checkinout = fieldSenseUtils.getLastCheckInOutData(userId,accountId);
//                java.util.HashMap hMap_visitstatus = fieldSenseUtils.getLastVisitStatus(userId, accountId);

//                String punchInDateTime = (String) hMap_punchinout.get("secondLastPunchInDate")+" "+(String) hMap_punchinout.get("secondLastPunchIn");
//                String punchOutDateTime = (String) hMap_punchinout.get("secondLastPunchOutDate")+" "+(String) hMap_punchinout.get("secondLastPunchOut");

//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                java.util.Date punchInDateTimeMillis = format.parse(punchInDateTime);
//                java.util.Date punchOutDateTimeMillis = format.parse(punchOutDateTime);
//                java.util.Date checkInDateTimeMillis = format.parse((String) hMap_checkinout.get("secondLastCheckIn"));
//                java.util.Date checkOutDateTimeMillis = format.parse((String) hMap_checkinout.get("secondLastCheckOut"));

//                leftSliderMenu.setLastPunchInDateTime(punchInDateTimeMillis.getTime());
//                leftSliderMenu.setLastPunchOutDateTime(punchOutDateTimeMillis.getTime());
//                leftSliderMenu.setLastCheckInDateTime(checkInDateTimeMillis.getTime());
//                leftSliderMenu.setLastCheckOutDateTime(checkOutDateTimeMillis.getTime());
//                leftSliderMenu.setAppointmentId((Integer) hMap_visitstatus.get("secondLastId"));
//                leftSliderMenu.setCustomerId((Integer) hMap_visitstatus.get("secondLastCustId")); 
                
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " leftSliderMenu Details", leftSliderMenu);
            // }else {
                     //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
                //}
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
            }
        } catch(NumberFormatException e){
            log4jLog.info("selectLeftSliderMenu_V2 for userId  "+userId + ", NumberFormatException : "+e);
            e.printStackTrace();
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " NumberFormatException Occured.", "  ", "");
        } catch (BeansException e) {
            log4jLog.info("selectLeftSliderMenu_V2 for userId  "+userId + ", BeansException : "+e);
            e.printStackTrace();
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " BeansException Occured.", "  ", "");
        } catch (Exception e) {
            log4jLog.info("selectLeftSliderMenu_V2 for userId  "+userId + ", BeansException : "+e);
            e.printStackTrace();
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " BeansException Occured.", "  ", "");
        }
    }

    
}
