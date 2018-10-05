/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.territoryCategory.service;

import static com.qlc.fieldsense.team.dao.TeamDaoImpl.log4jLog;
import com.qlc.fieldsense.territoryCategory.model.TerritoryCategory;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
//import com.qlc.fieldsense.territoryCategory.service.TerritoryCategoryManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 *
 * @author awaneesh
 */

@Controller
@RequestMapping("/territoryCategory")
public class TerritoryCategoryService {
    
    TerritoryCategoryManager territoryCategoryManager = new TerritoryCategoryManager();

    /**
     * 
     * @param eCategory
     * @param userToken
     * @return 
     * @purpose used to create territory category
     */
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody
    Object createTerritoryCategory(@RequestBody TerritoryCategory eCategory, @RequestHeader(value = "userToken") String userToken) {
        return territoryCategoryManager.createTerritoryCategory(eCategory, userToken);
    }

    /**
     * 
     * @param eCategory
     * @param oldCatPosCsv
     * @param oldCatName
     * @param oldIsActive
     * @param userToken
     * @return 
     * @purpose used to update territory category 
     */
    @RequestMapping(value = "/{oldCatPosCsv}/{oldCatName}/{oldIsActive}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public @ResponseBody
    Object updateTerritoryCategory(@RequestBody TerritoryCategory eCategory, @PathVariable String oldCatPosCsv,@PathVariable String oldCatName,@PathVariable boolean oldIsActive,@RequestHeader(value = "userToken") String userToken) {
        return territoryCategoryManager.updateTerritoryCategory(eCategory,oldCatPosCsv,oldCatName,oldIsActive,userToken);
    }

    /**
     * 
     * @param id
     * @param userToken
     * @return details of territory category base on expense category id 
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public @ResponseBody
    Object getOneTerritoryCategory(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return territoryCategoryManager.getOneTerritoryCategory(id, userToken);
    }

    /**
     * 
     * @param id
     * @param parentPosCsv
     * @param categoryName
     * @param userToken
     * @return 
     * @purpose delete territory category with category id
     */
    @RequestMapping(value = "/{id}/{parentPosCsv}/{categoryName}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object deleteTerritoryCategory(@PathVariable int id,@PathVariable String parentPosCsv, @PathVariable String categoryName,@RequestHeader(value = "userToken") String userToken) {
        return territoryCategoryManager.deleteTerritoryCategory(id,parentPosCsv,categoryName, userToken);
    }
    
    /**
     * 
     * @param allRequestParams
     * @param userToken
     * @param response
     * @return all territory category details with offset 
     */
    @RequestMapping(value = "/offset/", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getAllTerritoryCategoryWithOffset(@RequestParam Map<String,String> allRequestParams, @RequestHeader(value = "userToken") String userToken,HttpServletResponse response) {
        return territoryCategoryManager.getAllTerritoryCategoryWithOffset(allRequestParams, userToken,response);
    }
    
    /**
     * 
     * @param userToken
     * @return all territory category details
     */
    @RequestMapping( method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getAllTerritoryCategory(@RequestHeader(value = "userToken") String userToken) {
        return territoryCategoryManager.getAllTerritoryCategory(userToken);
    }
    
    /**
     * 
     * @param parentPosCsv
     * @param userToken
     * @return all territory category details
     */
    @RequestMapping(value = "/parent/{parentPosCsv}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getAllParentTerritoryCategory(@PathVariable String parentPosCsv,@RequestHeader(value = "userToken") String userToken) {
        return territoryCategoryManager.getAllParentTerritoryCategory(parentPosCsv,userToken);
    }
    
   /**upto app_version 22
     * 
     * @param userToken
     * @return all active territory categories
     */
    @RequestMapping(value = "/active", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getAllActiveTerritoryCategory(@RequestHeader(value = "userToken") String userToken) {
        return territoryCategoryManager.getAllActiveTerritoryCategory(userToken);
    }
    
    
    /**
     * 
     * @param userToken
     * @return all active territory categories
     */
    @RequestMapping(value = "/active/territory", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getAllActiveWebTerritoryCategory(@RequestHeader(value = "userToken") String userToken) {
        return territoryCategoryManager.getAllActiveTerritoryCategories(userToken);
    }
    
     /**
     * 
     * @param userToken
     * @return all active territory categories
     */
    @RequestMapping(value = "/mobile/allTerritory", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getAllTerritoryForMobile(@RequestHeader(value = "userToken") String userToken) {
        return territoryCategoryManager.getAllMobileTerritoryCategories(userToken);
    }
    
    
    /**
     * editing user
     * @param userid
     * @param userToken
     * @return all active territory categories
     */
    @RequestMapping(value = "/active/territory/{userid}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getAllActiveTerritoryCategoryOnUserEdit(@PathVariable int userid,@RequestHeader(value = "userToken") String userToken) {
        return territoryCategoryManager.getAllActiveTerritoryCategories(userid,userToken);
    }
    
    /**
     * 
     * @param userid
     * @param userToken
     * @return all active territory categories
     */
    @RequestMapping(value = "/active/territory/user/{userid}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getAllActiveUserTerritoryCategory(@PathVariable int userid,@RequestHeader(value = "userToken") String userToken) {
        return territoryCategoryManager.getAllUserActiveTerritoryCategories(userid,userToken);
    }
    
    /**
     * 
     * @param request
     * @param userToken
     * @author: Jyoti, 28-02-2017
     * @return 
     * @purpose:import territory category
     */
    @RequestMapping(value = "/importTerritoryCategory", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object importTerritoryCategory(MultipartHttpServletRequest request, @RequestHeader(value = "userToken") String userToken){
        Iterator<String> itr = request.getFileNames();
        MultipartFile file = null;
        while (itr.hasNext()) {
            file = request.getFile(itr.next());
            return territoryCategoryManager.insertTerritoryCategoryByImport(file, userToken);
        }
        return null;
    }
    
     /**22Oct2016
     * This api is used to insert Unkown territory for every user in user_territory for existing users, and it  should be reomoved in next release  
     * @return 
     */
    @RequestMapping(value = "/unkonown", method = RequestMethod.GET, headers = "Accept=application/json")
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
   //                 System.out.println("shiva  id : " +id + " krishna  countid : " + countid);
                    Connection connection = null;
                    PreparedStatement stmt1=null;
                    PreparedStatement stmt2=null;
                    PreparedStatement stmt3=null;
                    PreparedStatement stmt4=null;
                    String communityDbName = "account_" + countid;
                    try{
                        if(!prevaccid.equals("countid")){
                            connection = (Connection) DriverManager.getConnection(Constant.DATA_BASE_CONNECTION_URL + communityDbName, Constant.DEFAULT_COMMUNITY_DB_USERNAME, Constant.DEFAULT_COMMUNITY_DB_PASSWORD);
                        }
                        //stmt1=connection.prepareStatement("select id from territory_categories where category_name=?");
                        stmt1=connection.prepareStatement("select id from territory_categories where category_name!=?");
                        stmt1.setString(1, "Unknown");
                        ResultSet rs=stmt1.executeQuery();
                        while(rs.next()){
         //                       System.out.println("unknown id="+rs.getInt("id"));
                                stmt4=connection.prepareStatement("select id from user_territory where teritory_id=? and user_id_fk=?");
                                stmt4.setInt(1, rs.getInt("id"));
                                stmt4.setInt(2, id);

                           // stmt1.setString(1, "Unknown");
                            ResultSet rs1=stmt4.executeQuery();
                            if(!rs1.next()) {
                                stmt2=connection.prepareStatement(" insert into user_territory (teritory_id,user_id_fk,created_on) values (?,?,now())");
                                stmt2.setInt(1, rs.getInt("id"));
                                stmt2.setInt(2, id);
                                int  count=stmt2.executeUpdate();
                                totalcount++;
                            }    
                        }
                    }catch(Exception e){
                        log4jLog.info("reportingHead >> "+e);
//                          e.printStackTrace();       
                    }finally{
                        if(stmt1!=null)
                            stmt1.close();
                        if(stmt2!=null)
                            stmt2.close();
                        if(stmt3!=null)
                            stmt3.close();
                        if(stmt4!=null)
                            stmt4.close();
                        if(connection!=null){
                          if(!prevaccid.equals("countid")){
                                connection.close();
                            }
                        }
                    }
         //           System.out.println("totalcount ="+totalcount);
                  prevaccid=countid;
                 
          } 
        } catch (DataAccessException e) {
            log4jLog.info("reportingHead >> "+e);
//            e.printStackTrace();
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " failed . ", "", "");
        } catch (NumberFormatException e) {
            log4jLog.info("reportingHead >> "+e);
//            e.printStackTrace();
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " failed . ", "", "");
        } catch (SQLException e) {
            log4jLog.info("reportingHead >> "+e);
//            e.printStackTrace();
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " failed . ", "", "");
        }
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "successfully inserted ", "", "");
        
    }

    
}
