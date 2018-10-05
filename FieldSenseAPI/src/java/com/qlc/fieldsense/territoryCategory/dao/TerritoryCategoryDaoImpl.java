/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.territoryCategory.dao;

import com.qlc.fieldsense.territoryCategory.model.TerritoryCategory;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.RequestParam;
/**
 *
 * @author awaneesh
 */
public class TerritoryCategoryDaoImpl implements TerritoryCategoryDao {
    
    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("TerritoryCategoryDaoImpl");

    /**
     *
     * @param eCategory
     * @param accountId
     * @return
     */
    @Override
    public boolean createTerritoryCategory(TerritoryCategory eCategory, int accountId) {
        String query = "INSERT INTO territory_categories (category_name,is_active,created_on,created_by_id_fk,parent_category,category_position_csv,updated_on,updated_by) VALUES(?,?,now(),?,?,?,now(),?)";  
        String query1 = "SELECT max(id) from territory_categories";
        String query2 = "UPDATE territory_categories set category_position_csv=? where id=?";
        log4jLog.info(" createTerritoryCategory " + query);
        Object param[] = new Object[]{eCategory.getCategoryName(), eCategory.isIsActive(), eCategory.getCreatedBy().getId(),eCategory.getParentCategory(),eCategory.getCategoryPositionCsv(),eCategory.getCreatedBy().getId()};
        try{
            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    int id=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query1,Integer.class);
                    Object param1[] = new Object[]{id,id};
                    if(!eCategory.getCategoryPositionCsv().equals("0")){
                        param1 = new Object[]{id+","+eCategory.getCategoryPositionCsv(),id};                        
                    }
                    if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query2, param1) > 0) {
                       /* if(eCategory.getHasChild()==0){
                            query = "UPDATE territory_categories set has_childs=1 where id=?";
                            param =  new Object[]{eCategory.getParentCategory()};
                            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                                return true;
                            }else{
                                return false;
                            }
                        } */
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            log4jLog.info(" createTerritoryCategory " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param allRequestParams
     * @param accountId
     * @return
     */
    @Override
    public List<TerritoryCategory> selectAllTerritoryCategoryWithOffset(@RequestParam Map<String,String> allRequestParams, int accountId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT id,category_name,is_active,created_on,created_by_id_fk,(SELECT count(id) FROM territory_categories) as eCategoryCount");
        query.append(" FROM territory_categories");
        query.append(" INNER JOIN (SELECT id FROM territory_categories ");
        String sortcolindex=allRequestParams.get("order[0][column]");
        if(sortcolindex==null){
            query.append("ORDER BY created_on DESC");
        }else if(Integer.parseInt(sortcolindex)==0){
            if(allRequestParams.get("order[0][dir]").equals("asc")){
                query.append(" ORDER BY category_name");
            }else{
                query.append(" ORDER BY category_name DESC");
            }
        }
        query.append(" LIMIT ? OFFSET ?) AS my_results USING(id)");
        int start=Integer.parseInt(allRequestParams.get("start"));
        int length=Integer.parseInt(allRequestParams.get("length"));
        log4jLog.info("selectAllTerritoryCategoryWithOffset " + query);
        Object param[] = new Object[]{length,start};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<TerritoryCategory>() {

                @Override
                public TerritoryCategory mapRow(ResultSet rs, int i) throws SQLException {
                    TerritoryCategory eCategory = new TerritoryCategory();
                    eCategory.setId(rs.getInt("id"));
                    eCategory.setCategoryName(rs.getString("category_name"));
                    eCategory.setIsActive(rs.getBoolean("is_active"));
                    eCategory.setCretedOn(rs.getTimestamp("created_on"));
                    User user = new User();
                    user.setId(rs.getInt("created_by_id_fk"));
                    eCategory.setCreatedBy(user);
                    eCategory.setTotalTerritoryCategory(rs.getInt("eCategoryCount"));
                    return eCategory;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectAllTerritoryCategoryWithOffset " + e);
            return new ArrayList<TerritoryCategory>();
        }
    }

    /**
     *
     * @param accountId
     * @return
     */
    @Override
    public List<TerritoryCategory> selectAllTerritoryCategory(int accountId) {
        String query="SELECT id,category_name,is_active,created_on,parent_category,created_by_id_fk,category_position_csv csv,";
        query+=" (select count(id) from territory_categories where category_position_csv like concat('%',',',csv))  childcount ";
        query+=" FROM territory_categories order by category_name";
        log4jLog.info("selectAllTerritoryCategory " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query,new RowMapper<TerritoryCategory>() {

                @Override
                public TerritoryCategory mapRow(ResultSet rs, int i) throws SQLException {
                    TerritoryCategory eCategory = new TerritoryCategory();
                    eCategory.setId(rs.getInt("id"));
                    eCategory.setCategoryName(rs.getString("category_name"));
                    eCategory.setIsActive(rs.getBoolean("is_active"));
                    eCategory.setCretedOn(rs.getTimestamp("created_on"));
                    eCategory.setCategoryPositionCsv(rs.getString("csv"));
                    eCategory.setParentCategory(rs.getInt("parent_category"));
                    int hasChilds=0;
                    if(rs.getInt("childcount")>0){
                        hasChilds=1;
                    }
                    eCategory.setHasChild(hasChilds);
                    User user = new User();
                    user.setId(rs.getInt("created_by_id_fk"));
                    eCategory.setCreatedBy(user);
                    return eCategory;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectAllTerritoryCategoryWithOffset " + e);
            return new ArrayList<TerritoryCategory>();
        }
    }
    
    /**
     *
     * @param parentPosCsv
     * @param accountId
     * @return
     */
    public List<TerritoryCategory> selectAllParentTerritoryCategory(String parentPosCsv,int accountId) {
        String query="SELECT * from (SELECT id,category_name,is_active,created_on,parent_category,created_by_id_fk,category_position_csv csv,";
        query+=" (select count(id) from territory_categories where category_position_csv like concat('%',',',csv))  childcount ";
        query+=" FROM territory_categories ) subq where csv not like ? and  csv!=? order by category_name";
        Object[] param = new Object[]{"%,"+parentPosCsv+"%",parentPosCsv};
        log4jLog.info("selectAllTerritoryCategory " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query,param,new RowMapper<TerritoryCategory>() {

                @Override
                public TerritoryCategory mapRow(ResultSet rs, int i) throws SQLException {
                    TerritoryCategory eCategory = new TerritoryCategory();
                    eCategory.setId(rs.getInt("id"));
                    eCategory.setCategoryName(rs.getString("category_name"));
                    eCategory.setIsActive(rs.getBoolean("is_active"));
                    eCategory.setCretedOn(rs.getTimestamp("created_on"));
                    eCategory.setCategoryPositionCsv(rs.getString("csv"));
                    eCategory.setParentCategory(rs.getInt("parent_category"));
                    int hasChilds=0;
                    if(rs.getInt("childcount")>0){
                        hasChilds=1;
                    }
                    eCategory.setHasChild(hasChilds);
                    User user = new User();
                    user.setId(rs.getInt("created_by_id_fk"));
                    eCategory.setCreatedBy(user);
                    return eCategory;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectAllTerritoryCategoryWithOffset " + e);
            return new ArrayList<TerritoryCategory>();
        }
    }        
            
    /**
     *
     * @param eCategoryId
     * @param accountId
     * @return
     */
    @Override
    public TerritoryCategory selectTerritoryCategory(int eCategoryId, int accountId) {
        String query = "SELECT id,category_name,is_active,created_on,created_by_id_fk,parent_category parent,category_position_csv,";
        query+=" (select category_name from territory_categories where id=parent) parent_cat_name FROM territory_categories WHERE id=?";
        log4jLog.info("selectTerritoryCategories " + query);
        Object[] param = new Object[]{eCategoryId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query.toString(), param, new RowMapper<TerritoryCategory>() {

                @Override
                public TerritoryCategory mapRow(ResultSet rs, int i) throws SQLException {
                    TerritoryCategory eCategory = new TerritoryCategory();
                    eCategory.setId(rs.getInt("id"));
                    eCategory.setCategoryName(rs.getString("category_name"));
                    eCategory.setIsActive(rs.getBoolean("is_active"));
                    eCategory.setCretedOn(rs.getTimestamp("created_on"));
                    eCategory.setParentCategory(rs.getInt("parent"));
                    eCategory.setParentCatName(rs.getString("parent_cat_name"));
                    eCategory.setCategoryPositionCsv(rs.getString("category_position_csv"));
                    User user = new User();
                    user.setId(rs.getInt("created_by_id_fk"));
                    eCategory.setCreatedBy(user);
                    return eCategory;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectTerritoryCategory " + e);
            return new TerritoryCategory();
        }
    }

    /**
     *
     * @param eCategory
     * @param oldPosCsv
     * @param oldCatName
     * @param oldIsActive
     * @param accountId
     * @return
     */
    @Override
    public boolean updateTerritoryCategory(TerritoryCategory eCategory,String oldPosCsv,String oldCatName,boolean oldIsActive, int accountId) {
        String query = "UPDATE territory_categories SET category_name=?,is_active=?,parent_category=?,category_position_csv=? ,updated_on=now(),updated_by=? WHERE id=?";
        log4jLog.info(" updateTerritoryCategory " + query);
        Object[] param = new Object[]{eCategory.getCategoryName(), eCategory.isIsActive(),eCategory.getParentCategory(),eCategory.getCategoryPositionCsv(),eCategory.getUpdatedBy().getId(),eCategory.getId()};
        try {
            //String category=getTerritoryCategory(eCategory.getId(), accountId);
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                if(!oldPosCsv.equals(eCategory.getCategoryPositionCsv())){
                    query ="UPDATE territory_categories   category_position_csv = REPLACE(category_position_csv, ?, ?) WHERE category_position_csv LIKE ?";
                    param = new Object[]{","+oldPosCsv,","+eCategory.getCategoryPositionCsv(),"%,"+oldPosCsv};
                    try{
                        FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param);
                    }catch (Exception e) {
//                        e.printStackTrace();
                        log4jLog.info(" updateTerritoryCategory " + e);
                    }
                }
                if(!eCategory.getCategoryName().equals(oldCatName)){
                    //Update all customers with old territory name
                    query = "UPDATE customers SET territory=?,record_state = 2 WHERE territory=?";
                    param = new Object[]{eCategory.getCategoryName(),oldCatName};
                    try{
                        FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param);
                    }catch (Exception e){
//                        e.printStackTrace();
                        log4jLog.info(" update customers " + e);
                    }
                    // end of update customers
                }
                if(oldIsActive!=eCategory.isIsActive()){
                    query = "UPDATE territory_categories set is_active=? WHERE category_position_csv=? OR category_position_csv like ?";
                    param =  new Object[]{eCategory.isIsActive(),eCategory.getCategoryPositionCsv(),"%,"+eCategory.getCategoryPositionCsv()};
                    try{
                        FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param);
                    }catch (Exception e){
//                        e.printStackTrace();
                        log4jLog.info(" updateTerritoryCategory " + e);
                    }
                }
                /*if(eCategory.getHasChild()==0){
                    query = "UPDATE territory_categories set has_childs=1 where id=?";
                    param =  new Object[]{eCategory.getParentCategory()};
                    try{
                        FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param);
                    }catch (Exception e){
//                        e.printStackTrace();
                        log4jLog.info(" updateTerritoryCategory " + e);
                    }
                }*/
                return true;
            } else {
                return  false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateTerritoryCategory " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param id
     * @param parentPosCsv
     * @param categoryName
     * @param accountId
     * @return
     */
    @Override                             //178,   178,82,81,2,    5-ROADS 
    public boolean deleteTerritoryCategory(int id,String parentPosCsv,String categoryName,int accountId) {
        String  query = "DELETE FROM territory_categories WHERE category_position_csv=? OR category_position_csv LIKE? ";
        log4jLog.info(" deleteTerritoryCategory " + query);
        Object[] param = new Object[]{parentPosCsv,"%,"+parentPosCsv};   // 178,82,81,2 OR  178,82,81,2
        String catId = new Integer(id).toString();     //178
        
        List<TerritoryCategory> categoryList=new ArrayList<TerritoryCategory>();
      
        try {
            // Added by jyoti, optimization release feb 2018
            TerritoryCategory territoryCategoryObject = selectTerritoryCategory(id, accountId);
            FieldSenseUtils.setDeletedCategoriesRecord("territoryCategory", territoryCategoryObject.getId(), territoryCategoryObject.getCategoryName(), territoryCategoryObject.isIsActive(), territoryCategoryObject.getHasChild(), territoryCategoryObject.getParentCategory(), accountId);
            // Ended by jyoti, optimization release feb 2018
            if(!parentPosCsv.equals(catId)){   //   Atleast one child                                   ////  if(178,82,81,2!=178) true
                categoryList=getAllTerritoryCatNames(parentPosCsv,accountId); //  list of category name   big cinemas, holi, sun
    
                  String query1 = "UPDATE user_territory SET teritory_id=? WHERE teritory_id=?";
                  log4jLog.info(" updateUser_Territory " + query1);
               
                  if(categoryList.size()==1)  //child has no child 
                  {
                       Object[] category_param = new Object[]{categoryList.get(0).getParentCategory(),catId};    //3+1=4
//                       System.out.println("parent_param[0]"+category_param[0]);
                       FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query1, category_param);
                  }
                  else        // child has list
                  {
                         Object[] category_param = new Object[categoryList.size()+1];    //3+1=4
                         category_param[0] = categoryList.get(0).getParentCategory();  // 82
                         category_param[1] = categoryList.get(0).getId();  // 82
                       for(int i=1;i<categoryList.size();i++)   //2
                       {          //3
                            query1 += " OR teritory_id=? ";            
                            category_param[i+1] = categoryList.get(i).getId();
                       }
                    try{
                        FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query1, category_param);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log4jLog.info(" updateAllChildTerritoryCategory " + e);
                    } 
           
                  }
            } 
            
            //  No Child
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {// delete the record
               
                if(parentPosCsv.equals(catId)){  //  if(46,45,4!=46) false it will goes on else incase  if(46==46) { category name set it as UnKnown}
                    query = "UPDATE customers SET territory=?,record_state = 2 WHERE territory=?";
                    param = new Object[]{"Unknown",categoryName};
                    try{
                        FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) ;
                    } catch (Exception e) {
//                        e.printStackTrace();
                        log4jLog.info(" deleteTerritoryCategory " + e);
                    }    
                }else{
                    query = "UPDATE customers SET territory=?,record_state = 2 WHERE territory=?  ";
                    param = new Object[categoryList.size()+1];    //3+1=4
                    
                    param[0]="UnKnown";
  
                    param[1] = categoryList.get(0).getCategoryName();   // category name is hello2
                    for(int i=1;i<categoryList.size();i++){          //3
                        query += " OR territory=? ";            
                        param[i+1] = categoryList.get(i).getCategoryName();
                    }
                    try{
                        FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param);
                    } catch (Exception e) {
//                        e.printStackTrace();
                        log4jLog.info(" deleteTerritoryCategory " + e);
                    }    
                }
                
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" deleteTerritoryCategory " + e);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     *
     * @param parentPosCsv
     * @param accountId
     * @return
     */
    public  List<TerritoryCategory>  getAllTerritoryCatNames(String parentPosCsv,int accountId) {//178,82,81,2   OR    %,178,82,81,2 
        String query = "SELECT id,category_name,parent_category FROM territory_categories WHERE category_position_csv=? OR category_position_csv like ?";
        log4jLog.info("selectAllActiveTerritoryCategory " + query);
        Object[] param = new Object[]{parentPosCsv,"%,"+parentPosCsv};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query,param,new RowMapper<TerritoryCategory>() {

                @Override
                public TerritoryCategory mapRow(ResultSet rs, int i) throws SQLException {
                    TerritoryCategory eCategory = new TerritoryCategory();
                    eCategory.setId(rs.getInt("id"));
                    eCategory.setCategoryName(rs.getString("category_name"));//5 roads , big cinemas
                    eCategory.setParentCategory(rs.getInt("parent_category"));
                    return eCategory;
                }
            });
        } catch (Exception e) {
//            e.printStackTrace();
            log4jLog.info("selectAllActiveTerritoryCategory " + e);
            return new ArrayList();
        }
    } 
    
    /**
     *
     * @param categoryName
     * @param accountId
     * @return
     */
    @Override
    public boolean isTerritoryCategoryAlreadyExists(String categoryName, int accountId) {
        String query = "SELECT COUNT(id) FROM territory_categories WHERE category_name=?";
        log4jLog.info(" isTerritoryCategoryAlreadyExists " + query);
        Object[] param = new Object[]{categoryName};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isTerritoryCategoryAlreadyExists " + e);
            return false;
        }
    }

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    @Override
    public boolean isTerritoryCategoryValid(int id, int accountId) {
        String query = "SELECT COUNT(id) FROM territory_categories WHERE id=?";
        log4jLog.info(" isTerritoryCategoryValid " + query);
        Object[] param = new Object[]{id};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isTerritoryCategoryValid " + e);
            return false;
        }
    }
    
    /**
     *
     * @param eCategoryId
     * @param accountId
     * @return
     */
    @Override
    public String getTerritoryCategory(int eCategoryId, int accountId) {
        String query = "SELECT category_name FROM territory_categories WHERE id=?";
        log4jLog.info(" getTerritoryCategory " + query);
        Object[] param = new Object[]{eCategoryId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" getTerritoryCategory " + e);
            return null;
        }
    }
    
    /**
     *
     * @param accountId
     * @return
     */
    @Override
    public  List<TerritoryCategory>  selectAllActiveTerritoryCategory(int accountId) {
       String query = new String();
        query="SELECT id,category_name,is_active,created_on,created_by_id_fk FROM territory_categories WHERE is_active=1 order by category_name";
        log4jLog.info("selectAllActiveTerritoryCategory " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, new RowMapper<TerritoryCategory>() {

                @Override
                public TerritoryCategory mapRow(ResultSet rs, int i) throws SQLException {
                    TerritoryCategory eCategory = new TerritoryCategory();
                    eCategory.setId(rs.getInt("id"));
                    eCategory.setCategoryName(rs.getString("category_name"));
                    eCategory.setIsActive(rs.getBoolean("is_active"));
                    eCategory.setCretedOn(rs.getTimestamp("created_on"));
                    User user = new User();
                    user.setId(rs.getInt("created_by_id_fk"));
                    eCategory.setCreatedBy(user);
                    return eCategory;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectAllActiveTerritoryCategory " + e);
            return new ArrayList<TerritoryCategory>();
        }
    }
    
    /**
     *
     * @param accountId
     * @return
     */
    @Override
    public  List<TerritoryCategory>  selectAllActiveTerritoryCategories(int accountId) {
        String query = new String();
        query="SELECT id,category_name,is_active,created_on,parent_category,created_by_id_fk,category_position_csv csv,";
        query+=" (select count(id) from territory_categories where category_position_csv like concat('%',',',csv) and is_active=1)  childcount ";
        query+=" FROM territory_categories where is_active=1 order by category_name";
        log4jLog.info("selectAllActiveTerritoryCategory " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, new RowMapper<TerritoryCategory>() {

                @Override
                public TerritoryCategory mapRow(ResultSet rs, int i) throws SQLException {
                    TerritoryCategory eCategory = new TerritoryCategory();
                    eCategory.setId(rs.getInt("id"));
                    eCategory.setCategoryName(rs.getString("category_name"));
                    int hasChilds=0;
                    if(rs.getInt("childcount")>0){
                        hasChilds=1;
                    }
                    eCategory.setHasChild(hasChilds);
                    eCategory.setCategoryPositionCsv(rs.getString("csv"));
                    eCategory.setParentCategory(rs.getInt("parent_category"));
                    eCategory.setIsActive(rs.getBoolean("is_active"));
                    eCategory.setUpdatedOn(new Timestamp(new java.util.Date().getTime()));
                  //  eCategory.setCretedOn(rs.getTimestamp("created_on"));
                  //  User user = new User();
                   // user.setId(rs.getInt("created_by_id_fk"));
                   // eCategory.setCreatedBy(user);
                    return eCategory;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectAllActiveTerritoryCategory " + e);
            return new ArrayList<TerritoryCategory>();
        }
    }
    
    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    @Override
    public  List<TerritoryCategory>  selectAllActiveTerritoryCategories(int userId,int accountId) {
        String query = new String();
        query="SELECT t.id,t.category_name,t.is_active,t.created_on,t.parent_category,t.created_by_id_fk,t.category_position_csv csv,";
        query+=" IFNULL(t1.id,0) user_territory,(select count(id) from territory_categories where category_position_csv like ";
        query+=" concat('%',',',csv) and is_active=1)  childcount FROM territory_categories t left  join user_territory t1 ";
        query+="  on t.id=t1.teritory_id and t1.user_id_fk=? where is_active=1 order by category_name";
        Object[] param = {userId};
        log4jLog.info("selectAllActiveTerritoryCategory " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query,param, new RowMapper<TerritoryCategory>() {

                @Override
                public TerritoryCategory mapRow(ResultSet rs, int i) throws SQLException {
                    TerritoryCategory eCategory = new TerritoryCategory();
                    eCategory.setId(rs.getInt("id"));
                    eCategory.setCategoryName(rs.getString("category_name"));
                    int hasChilds=0;
                    if(rs.getInt("childcount")>0){
                        hasChilds=1;
                    }
                    eCategory.setHasChild(hasChilds);
                    eCategory.setCategoryPositionCsv(rs.getString("csv"));
                    eCategory.setParentCategory(rs.getInt("parent_category"));
                    eCategory.setIsActive(rs.getBoolean("is_active"));
                    boolean userHasTerritory=false;
                    if(rs.getInt("user_territory")!=0){
                       userHasTerritory=true;
                    }
                    eCategory.setUserHasTerritory(userHasTerritory);
                  //  eCategory.setCretedOn(rs.getTimestamp("created_on"));
                  //  User user = new User();
                   // user.setId(rs.getInt("created_by_id_fk"));
                   // eCategory.setCreatedBy(user);
                    return eCategory;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectAllActiveTerritoryCategory " + e);
            return new ArrayList<TerritoryCategory>();
        }
    }
    
    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    @Override
    public  List<TerritoryCategory>  selectAllUserActiveTerritoryCategories(int userId,int accountId) {
        String query = new String();
        query="SELECT subq.*,(select count(t4.id) from user_territory t4 inner join territory_categories t5  on t5.id=t4.teritory_id ";
        query+="and t4.user_id_fk=? where is_active=1 and category_position_csv like concat('%',',',subq.csv)) childcount from ";
        query+="(SELECT t.id,t.category_name,t.is_active,t.parent_category,t.category_position_csv csv,IFNULL(t1.id,0) user_terr FROM ";
        query+="user_territory t1 inner join territory_categories t  on t.id=t1.teritory_id and t1.user_id_fk=? where is_active=1) ";
        query+="as subq  order by category_name;";
        Object[] param = {userId,userId};
        log4jLog.info("selectAllActiveTerritoryCategory " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query,param, new RowMapper<TerritoryCategory>() {

                @Override
                public TerritoryCategory mapRow(ResultSet rs, int i) throws SQLException {
                    TerritoryCategory eCategory = new TerritoryCategory();
                    eCategory.setId(rs.getInt("id"));
                    eCategory.setCategoryName(rs.getString("category_name"));
                    int hasChilds=0;
                    if(rs.getInt("childcount")>0){
                        hasChilds=1;
                    }
                    eCategory.setHasChild(hasChilds);
                    eCategory.setCategoryPositionCsv(rs.getString("csv"));
                    eCategory.setParentCategory(rs.getInt("parent_category"));
                    eCategory.setIsActive(rs.getBoolean("is_active"));
                    eCategory.setUpdatedOn(new Timestamp(new java.util.Date().getTime()));
                   // boolean userHasTerritory=false;
                   // if(rs.getInt("user_territory")!=0){
                    //   userHasTerritory=true;
                   // }
                   // eCategory.setUserHasTerritory(userHasTerritory);
                  //  eCategory.setCretedOn(rs.getTimestamp("created_on"));
                  //  User user = new User();
                   // user.setId(rs.getInt("created_by_id_fk"));
                   // eCategory.setCreatedBy(user);
                    return eCategory;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectAllActiveTerritoryCategory " + e);
            return new ArrayList<TerritoryCategory>();
        }
    }

    /**
     *
     * @param accountId
     * @return
     */
    public List<HashMap> selectAllTerritoryCategoriesForMobile(int accountId) {
            String query = new String();
        query="SELECT id,category_name,is_active,parent_category";
       // query+=" (select count(id) from territory_categories where category_position_csv like concat('%',',',csv) and is_active=1)  childcount ";
        query+=" FROM territory_categories WHERE is_active=1 order by category_name";  // Edited by jyoti,10-04-2017 is_active =1
        log4jLog.info("selectAllActiveTerritoryCategory " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, new RowMapper<HashMap>() {

                @Override
                public HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    HashMap<String,Object> mapOfterriorties = new HashMap<String,Object>();
//                    eCategory.setId(rs.getInt("id"));
                    mapOfterriorties.put("territoryID", rs.getInt("id"));
                    mapOfterriorties.put("territoryName",rs.getString("category_name"));
                    mapOfterriorties.put("parentTerritoryID",rs.getInt("parent_category"));
                    mapOfterriorties.put("isActive",rs.getBoolean("is_active"));
//                    eCategory.setCategoryName(rs.getString("category_name"));
//                    int hasChilds=0;
//                    if(rs.getInt("childcount")>0){
//                        hasChilds=1;
//                    }
//                    eCategory.setHasChild(hasChilds);
//                    eCategory.setCategoryPositionCsv(rs.getString("csv"));
//                    eCategory.setParentCategory(rs.getInt("parent_category"));
//                    eCategory.setIsActive(rs.getBoolean("is_active"));
//                    eCategory.setUpdatedOn(new Timestamp(new java.util.Date().getTime()));
//                  //  eCategory.setCretedOn(rs.getTimestamp("created_on"));
//                  //  User user = new User();
//                   // user.setId(rs.getInt("created_by_id_fk"));
//                   // eCategory.setCreatedBy(user);
                    return mapOfterriorties;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectAllActiveTerritoryCategory " + e);
            return new ArrayList<HashMap>();
        }
    }
    
    /**
     *
     * @param eCategory
     * @param accountId
     * @return
     */
    @Override
    public TerritoryCategory selectParentCSVID(TerritoryCategory eCategory, int accountId) {
        String query = " SELECT category_position_csv FROM territory_categories WHERE category_name = ? ";       
        log4jLog.info("selectParentCategoryID " + query);
        Object[] param = new Object[]{eCategory.getParentCatName()};
        try {             
            String category_position_csv =  FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class );              
//            System.out.println("category_position_csv id from query : " + category_position_csv);            
            eCategory.setCategoryPositionCsv(category_position_csv);
            return eCategory;
        } catch (Exception e) {
            log4jLog.info("selectTerritoryCategory " + e);
            return new TerritoryCategory();
        }
    }
    
}
