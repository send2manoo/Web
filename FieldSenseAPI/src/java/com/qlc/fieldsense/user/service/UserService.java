/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.user.service;

import com.qlc.fieldsense.user.model.SuperUser;
import com.qlc.fieldsense.user.model.TravelLogsData;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.user.model.UserOld;
import com.qlc.fieldsense.user.model.UserManager;
import com.qlc.fieldsense.usersTravelLogs.model.UsersTravelLogs;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import com.qlc.fieldsense.utils.MapsIconCreate;
import com.qlc.fieldsense.utils.PhotoToIconCreator;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 *
 * @author Ramesh
 * @date 18-02-2014
 * @purpose To perform user related services .
 */
@Controller
@RequestMapping("/user")
public class UserService {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("UserService");
    UserManager userManager = new UserManager();
    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");

    /**
     * 
     * @param user
     * @param userToken
     * @return 
     * @purpose Used to create user at admin side.
     */
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object createUser(@Valid @RequestBody User user, @RequestHeader(value = "userToken") String userToken) {
//        System.out.println("user%%%^@#$ "+user.toString());
        return userManager.createNewUser(user, userToken);
    }

//    /**  added by manohar
//     * @param user
//     * @param userToken
//     * @return 
//     */
//    @RequestMapping(value = "/superuser",method = RequestMethod.POST, headers = "Accept=application/json")
//    public
//    @ResponseBody
//    Object createSuperUser(@Valid @RequestBody SuperUser user, @RequestHeader(value = "userToken") String userToken) {
//        return userManager.createSuperUser(user, userToken);
//    }

    /**
     * 
     * @param userToken
     * @return list of all users with details
     */
    @RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getOffsetUsers(@RequestHeader(value = "userToken") String userToken) {
        return userManager.getUsers(userToken);
    }

    /**
     * @param allRequestParams
     * @param userToken
     * @param response
     * @return details of all users with offset
     */
    @RequestMapping(value = "/details", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getOffsetUsersWithOffset( @RequestParam Map<String,String> allRequestParams,@RequestHeader(value = "userToken") String userToken,HttpServletResponse response) {
        return userManager.getUsersWithOffset( allRequestParams,userToken,response);
    }

//    //  added by manohar
//    @RequestMapping(value = "/getSuperUser", method = RequestMethod.GET, headers = "Accept=application/json")
//    public
//    @ResponseBody
//    Object getSuperUsers( @RequestParam Map<String,String> allRequestParams,@RequestHeader(value = "userToken") String userToken,HttpServletResponse response) {
//        return userManager.getSuperUsers( allRequestParams,userToken,response);
//    }

    
    /**
     * 
     * @param id
     * @param userToken
     * @return details of user based on id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getUser(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return userManager.getUser(id, userToken);
    }
    
    /**
     * @Added by - jyoti 22-june-2017
     * @param id
     * @param userToken
     * @return 
     * @purpose get id of users whose report_to = userId
     */
  //  this should be uncommented in future release
    @RequestMapping(value = "/reporter_of/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getReporterOfUser(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return userManager.getReporterOfUser(id, userToken);
    }

    /**
     * 
     * @param id
     * @param userToken
     * @return 
     * @purpose used to delete user based on id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public
    @ResponseBody
    Object deletrUser(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return userManager.deleteUser(id, userToken);
    }

    /**
     * 
     * @param user
     * @param userToken
     * @return 
     * @purpose used to update user details 
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object userLastKnownDetails(@RequestBody User user, @RequestHeader(value = "userToken") String userToken) {
        Object response= userManager.updateLastKnownDetails(user, userToken);
        user=null; //Edited by Awaneesh for Memory Leakage issue
        return response;
    }
    
//    /**
//     * @param users
//     * @param userToken
//     * @return 
//     * @purpose used to update multiple user details 
//     */
//    @RequestMapping(value = "/multiple/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
//    public
//    @ResponseBody
//    Object usersLastKnownDetailsOld(@RequestBody UserOld[] users, @RequestHeader(value = "userToken") String userToken) {
//        Object response=  userManager.updateMultiUsersLastKnownDetails(users, userToken);
//        users=null; //Edited by Awaneesh for Memory Leakage issue
//        return response;
//    }

    /**
     * @param users
     * @param userToken
     * @return 
     * @purpose used to update multiple user details 
     */
    @RequestMapping(value = "/updateTravelLogs/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object usersLastKnownDetails(@RequestBody User[] users,@RequestHeader(value = "userToken") String userToken) throws IOException {
        
//        System.out.println("reached update travel log");
        Object response=  userManager.updateTravelLogsDetails(users, userToken);
        users=null; //Edited by Awaneesh for Memory Leakage issue
        return response;

    }

  /**
     * @param travelLogsData
     * @param userToken
     * @return 
     * @purpose used to update multiple user details 
     */
    @RequestMapping(value = "/newUpdateTravelLogs/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
        Object UpdateTravelLogs(@RequestBody TravelLogsData travelLogsData,@RequestHeader(value = "userToken") String userToken) throws IOException { 
        
//        System.out.println("reached update travel log");
        //Object response=  userManager.updateTravelLogsDetails(users, userToken);
            //System.out.println("hello hi"+travelLogsData);
        Object response= userManager.UpdateTravelLogsBatch(travelLogsData, userToken);
        travelLogsData=null; //Edited by Awaneesh for Memory Leakage issue
        return response;
    }
    
    /**
     * 
     * @param expenseImage
     * @param userId
     * @param accountId
     * @author:anuja
     * @return 
     * @purpose:upload ProfilePic
     */
    @RequestMapping(value = "/profilePic/save/{userId}/{accountId}", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object userProfilePic(MultipartHttpServletRequest expenseImage, @PathVariable("userId") String userId, @PathVariable("accountId") String accountId) {
//        if (fieldSenseUtils.isTokenValid(userToken)) {
        Iterator<String> itr = expenseImage.getFileNames();
        MultipartFile file = null;
//            int userId = fieldSenseUtils.userIdForToken(userToken);
        String saveDirectory = Constant.PROFILEPIC_UPLOAD_PATH + accountId + "_" + userId + ".png";
        while (itr.hasNext()) {
            file = expenseImage.getFile(itr.next());
            try {
                PhotoToIconCreator photoToIconCreator = new PhotoToIconCreator();
                photoToIconCreator.uploadImage(file, accountId + "_" + userId);
                file.transferTo(new File(saveDirectory));
                String bgImagePath = Constant.PROFILEPIC_UPLOAD_PATH + "FM.png";
                String fgImagePath = Constant.PROFILEPIC_UPLOAD_PATH + accountId + "_" + userId + "_m.png";
                MapsIconCreate mapsIconCreate = new MapsIconCreate();
                BufferedImage mapMarker = mapsIconCreate.overlayImages(bgImagePath, fgImagePath);
                if (mapMarker != null) {
                    mapsIconCreate.writeImage(mapMarker, Constant.PROFILEPIC_UPLOAD_PATH + accountId + "_" + userId + "_mm.png", "PNG");
                }
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ProfilePic uploded successfully .", "", "");
            } catch (IOException ex) {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " ProfilePic not uploaded. Please try again", "", "");
            } catch (IllegalStateException ex) {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " ProfilePic not uploaded. Please try again", "", "");
            }
        }
        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ProfilePic uploaded successfully .", "", "");
    }
    /*
     * @author:anuja
     * @purpose:retrive ProfilePic
     * @ structure changed and not in use anymore
     */

    /**
     *
     * @param userToken
     * @return
     */
    @RequestMapping(value = "/profilePic", method = RequestMethod.GET)
    public
    @ResponseBody
    BufferedImage getProfilePic(@RequestHeader(value = "userToken") String userToken) {
        int accountId = fieldSenseUtils.accountIdForToken(userToken);
        int userId = fieldSenseUtils.userIdForToken(userToken);
        String fileName = Constant.PROFILEPIC_UPLOAD_PATH + accountId + "_" + userId + ".png";
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        try {
            BufferedImage image = ImageIO.read(new File(fileName));
            return image;
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * 
     * @param request
     * @param userToken
     * @author:anuja
     * @return 
     * @purpose:import user
     */
    @RequestMapping(value = "/importUser", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object importUser(MultipartHttpServletRequest request, @RequestHeader(value = "userToken") String userToken) {
        Iterator<String> itr = request.getFileNames();
        MultipartFile file = null;
        while (itr.hasNext()) {
            file = request.getFile(itr.next());
            return userManager.insertUserByImport(file, userToken);
//            return userManager.importUserFromCSV(file, userToken);   // modified by  vaibhav new user import
        
        }
        return null;
    }
    
//    /**
//     * @Added by vaibhav
//     * @param request
//     * @param userToken
//     * @return 
//     */
//    @RequestMapping(value = "/importUser", method = RequestMethod.POST, headers = "Accept=application/json")
//    public
//    @ResponseBody
//    Object importUser(MultipartHttpServletRequest request, @RequestHeader(value = "userToken") String userToken) {
//        Iterator<String> itr = request.getFileNames();
//        MultipartFile file = null;
//        while (itr.hasNext()) {
//            file = request.getFile(itr.next());
//            return userManager.importUserFromCSV(file, userToken); // new method by vaibhav
//        }
//        return null;
//    }

    /**
     * 
     * @param user
     * @param userToken
     *@author:anuja
     * @return 
     * @purpose:update user
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object updateUserDetails(@RequestBody User user, @RequestHeader(value = "userToken") String userToken) {
        return userManager.updateUserDetails(user, userToken);
    }

    /**
     * 
     * @param userToken
     * @author: anuja
     * @return 
     * @purpose: users which are not in any team
     */
    @RequestMapping(value = "/noTeamMembers", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectUsersExceptTeamMembers(@RequestHeader(value = "userToken") String userToken) {
        return userManager.selectUsersExceptTeamMembers(userToken);
    }

    /**
     * @author Ramesh
     * @return 
     * @date 28-06-2014
     * @param user
     * @param userToken
     * @purpose used to update user
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object updateUserDetailsForUser(@RequestBody User user, @RequestHeader(value = "userToken") String userToken) {
        return userManager.updateUserDetailsForUser(user, userToken);
    }

    /**
     * 
     * @param user
     * @param userToken
     * @author: anuja
     * @return 
     * @purpose: update home and office latlong
     */
    @RequestMapping(value = "/officeHome", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object updateHomeOfficeLocation(@RequestBody User user, @RequestHeader(value = "userToken") String userToken) {
        return userManager.updateHomeOfficeLocation(user, userToken);
    }

    /**
     * 
     * @param userToken
     * @return 
     * @purpose Used to get list of all TL (i.e. role=2).
     */
    @RequestMapping(value = "/teamLeaders", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getTeamLeaders(@RequestHeader(value = "userToken") String userToken) {
        return userManager.getTeamLeaders(userToken);
    }
    
    /**
     * 
     * @param userToken
     * @return 
     * @purpose Used to get list of all active non super admin users
     */
     @RequestMapping(value = "/mobile", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getAllUserForMobile(@RequestHeader(value = "userToken") String userToken) {
        return userManager.getUsersForMobile(userToken);
    }
    
    /**9May2016
     * This api is used to insert reporting head of every user in report_to column which is newly added, it  should be reomoved in next release  
     * @return 
     */
    @RequestMapping(value = "/report_to", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object reportingHead() {
        String query = "SELECT id,account_id_fk FROM fieldsense.users where role!=0 order by account_id_fk;";
       // int accountid=0;
        String prevaccid="0";
        int totalcount=0;
        try {
          List<Map<String,Object>>  users =(List<Map<String,Object>>)  FieldSenseUtils.getJdbcTemplateForAccount(1).queryForList(query);
          for (Map<String,Object> entry : users) {
                    int id = Integer.parseInt(entry.get("id").toString());
                    String countid = entry.get("account_id_fk").toString();
        //            System.out.println("shiva  id : " +id + " krishna  countid : " + countid);
                    Connection connection = null;
                    PreparedStatement stmt1=null;
                    PreparedStatement stmt2=null;
                    PreparedStatement stmt3=null;
                    String communityDbName = "account_" + countid;
                    try{
                        if(!prevaccid.equals("countid")){
                            connection = (Connection) DriverManager.getConnection(Constant.DATA_BASE_CONNECTION_URL + communityDbName, Constant.DEFAULT_COMMUNITY_DB_USERNAME, Constant.DEFAULT_COMMUNITY_DB_PASSWORD);
                        }
                        stmt1=connection.prepareStatement("SELECT team_id_fk FROM team_members where user_id_fk=?");
                        stmt1.setInt(1, id);
                        ResultSet rs=stmt1.executeQuery();
                        while(rs.next()){
        //                    System.out.println("team id="+rs.getInt("team_id_fk"));
                            stmt2=connection.prepareStatement("SELECT u.id, u.full_name FROM teams t INNER JOIN fieldsense.users u where t.id=? and t.user_id_fk!=? and t.user_id_fk=u.id");
                            stmt2.setInt(1, rs.getInt("team_id_fk"));
                            stmt2.setInt(2, id);
                            ResultSet rs1=stmt2.executeQuery();
                             while(rs1.next()){
        //                       System.out.println("report id ="+rs1.getInt("id"));
        //                       System.out.println("report name ="+rs1.getString("full_name"));
                               stmt3=connection.prepareStatement("UPDATE fieldsense.users set report_to=? where id=?");
                               stmt3.setInt(1, rs1.getInt("id"));
                               stmt3.setInt(2, id);
                               int record=stmt3.executeUpdate();
                               totalcount++;
                             }
                        }
                    }catch(Exception e){
                        log4jLog.info("reportingHead "+e);
//                          e.printStackTrace();       
                    }finally{
                        if(stmt1!=null)
                            stmt1.close();
                        if(stmt2!=null)
                            stmt2.close();
                        if(stmt3!=null)
                            stmt3.close();
                        if(connection!=null){
                          if(!prevaccid.equals("countid")){
                                connection.close();
                            }
                        }
                    }
        //            System.out.println("totalcount ="+totalcount);
                  prevaccid=countid;
                 
          } 
        } catch (Exception e) {
//            e.printStackTrace();
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " failed . ", "", "");
        }
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "successfully inserted ", "", "");
        
    }
    
    /**
     * @purpose get all users for report to
     * @param userToken 
     */
    @RequestMapping(value = "/usersForReportTo", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getAllUsersForReportTo(@RequestHeader(value = "userToken") String userToken) {
        return userManager.getUsersForReportTo(userToken);
    }
    
    /**
     * @param usersTravelLogs
     * @Added by siddhesh, 09-01-2018
     * @param userToken
     * @return
     * @purpose used to update multiple user details
     */
    @RequestMapping(value = "/updateTravelLogs/singleEntry/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public @ResponseBody
    Object usersLastKnownDetailsSingle(@RequestBody UsersTravelLogs usersTravelLogs, @RequestHeader(value = "userToken") String userToken) throws IOException {
        Object response = userManager.UpdateTravelLogsSingle(usersTravelLogs, userToken);
        usersTravelLogs = null; //Edited by Awaneesh for Memory Leakage issue
        return response;
    }
    
}
