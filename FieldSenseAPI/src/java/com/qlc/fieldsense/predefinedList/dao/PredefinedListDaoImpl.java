/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.predefinedList.dao;

import com.qlc.fieldsense.predefinedList.model.PredefinedList;
import static com.qlc.fieldsense.territoryCategory.dao.TerritoryCategoryDaoImpl.log4jLog;
import com.qlc.fieldsense.territoryCategory.model.TerritoryCategory;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author siddhesh
 */
public class PredefinedListDaoImpl implements PredefinedListDao {

    /**
     *
     * @param listName
     * @param accountId
     * @return
     */
    public boolean isPredefinedListNameAlreadyExists(String listName, int accountId) {
        String query = "SELECT COUNT(id) FROM predifined_list_categories WHERE list_name=?";
        log4jLog.info(" isPredefinedListNameAlreadyExists " + query);
        Object[] param = new Object[]{listName};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isPredefinedListNameAlreadyExists " + e);
            return false;
        }
    }

    /**
     *
     * @param predefinedList
     * @param accountId
     * @return
     */
    public boolean createPredefinedList(PredefinedList predefinedList, int accountId) {
        String query = "INSERT INTO predifined_list_categories (list_name,is_active,created_on,created_by_id_fk,options_list) VALUES(?,?,now(),?,?)";
        log4jLog.info(" createTerritoryCategory " + query);
        Object param[] = new Object[]{predefinedList.getListName(), predefinedList.getIsActive(), predefinedList.getCreatedBy().getId(), predefinedList.getOptionValues()};
        try {

            int numberOfRows = FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param);
            if (numberOfRows > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            log4jLog.info(" createTerritoryCategory " + e);
            return false;
        }

    }

    /**
     *
     * @param allRequestParams
     * @param accountId
     * @return
     */
    public List<PredefinedList> selectAllPredefinedListWithOffset(Map<String, String> allRequestParams, int accountId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT id,list_name,is_active,created_on,created_by_id_fk,options_list,(SELECT count(id) FROM predifined_list_categories) as list_count");
        query.append(" FROM predifined_list_categories");
        query.append(" INNER JOIN (SELECT id FROM predifined_list_categories ");
        String sortcolindex = allRequestParams.get("order[0][column]");
        if (sortcolindex == null) {
            query.append("ORDER BY created_on DESC");
        } else if (Integer.parseInt(sortcolindex) == 0) {
            if (allRequestParams.get("order[0][dir]").equals("asc")) {
                query.append(" ORDER BY list_name");
            } else {
                query.append(" ORDER BY list_name DESC");
            }
        }
        query.append(" LIMIT ? OFFSET ?) AS my_results USING(id) where list_type not like '%SystemDefinedList%'");
        int start = Integer.parseInt(allRequestParams.get("start"));
        int length = Integer.parseInt(allRequestParams.get("length"));
        log4jLog.info("selectAllPredefinedListWithOffset " + query);
        Object param[] = new Object[]{length, start};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<PredefinedList>() {

                @Override
                public PredefinedList mapRow(ResultSet rs, int i) throws SQLException {
                    PredefinedList predefinedList = new PredefinedList();
                    predefinedList.setListId(rs.getInt("id"));
                    predefinedList.setListName(rs.getString("list_name"));
                    predefinedList.setIsActive(rs.getInt("is_active"));
                    predefinedList.setCreatedOn(rs.getTimestamp("created_on"));
                    predefinedList.setOptionValues(rs.getString("options_list"));
                    User user = new User();
                    user.setId(rs.getInt("created_by_id_fk"));
                    predefinedList.setCreatedBy(user);
                    predefinedList.setTotalPredefinedList(rs.getInt("list_count")-1);
                    return predefinedList;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectAllPredefinedListWithOffset " + e);
            return new ArrayList<PredefinedList>();
        }
    }

    /**
     *
     * @param predefinedList
     * @param accountId
     * @return
     */
    public PredefinedList selectPredefinedList(int predefinedList, int accountId) {
        String query = "SELECT id,list_name,is_active,created_on,created_by_id_fk,options_list FROM predifined_list_categories WHERE id=?";
        log4jLog.info("selectPredefinedList " + query);
        Object[] param = new Object[]{predefinedList};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, new RowMapper<PredefinedList>() {

                @Override
                public PredefinedList mapRow(ResultSet rs, int i) throws SQLException {
                    PredefinedList ePredefinedList = new PredefinedList();
                    ePredefinedList.setListId(rs.getInt("id"));
                    ePredefinedList.setListName(rs.getString("list_name"));
                    ePredefinedList.setIsActive(rs.getInt("is_active"));
                    ePredefinedList.setCreatedOn(rs.getTimestamp("created_on"));
                    ePredefinedList.setOptionValues(rs.getString("options_list"));
                    User user = new User();
                    user.setId(rs.getInt("created_by_id_fk"));
                    ePredefinedList.setCreatedBy(user);
                    return ePredefinedList;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectPredefinedList" + e);
            return new PredefinedList();
        }
    }

    /**
     *
     * @param predefinedList
     * @param accountId
     * @return
     */
    public boolean updatePredefinedList(PredefinedList predefinedList, int accountId) {
        String query = "UPDATE predifined_list_categories SET list_name=?,is_active=?,options_list=? WHERE id=?";
        log4jLog.info(" updateIndustryCategory " + query);
        Object[] param = new Object[]{predefinedList.getListName(), predefinedList.getIsActive(), predefinedList.getOptionValues(), predefinedList.getListId()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info(" isPredefinedListAlreadyExists " + e);
            return false;
        }
    }

    /**
     *
     * @param listName
     * @param accountId
     * @return
     */
    public boolean isPredefinedListAlreadyExistsForEdit(String listName, int accountId) {
        String query = "SELECT COUNT(id) FROM predifined_list_categories WHERE list_name=?";
        log4jLog.info(" isPredefinedListAlreadyExists " + query);
        Object[] param = new Object[]{listName};
        try {
            int numberOfRows = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
            if (numberOfRows <= 1) {
                return true;

            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isPredefinedListAlreadyExists " + e);
            return false;
        }
    }

    /**
     *
     * @param listId
     * @param accountId
     * @return
     */
    public boolean deletePredefinedList(int listId, int accountId) {

        String query = "DELETE FROM predifined_list_categories WHERE id=?";
        log4jLog.info(" deletePredefinedList " + query);
        Object[] param = new Object[]{listId};
        try {
            int numberOfRows = FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param);
            if (numberOfRows > 0) {  
               return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" deletePredefinedList " + e);
            return false;
        }

    }

    /**
     *
     * @param accountId
     * @return
     */
    public HashMap<String, List<HashMap>> getAllLists(int accountId) {
        String query = "SELECT list_name,id,list_type FROM predifined_list_categories where is_active=1 order by list_name asc";
        // Object param[] = new Object[]{"UserDefinedList"};
        log4jLog.info("selectPredefinedList " + query);

        try {
          

            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, new ResultSetExtractor<HashMap<String, List<HashMap>>>() {

                @Override
                public HashMap<String, List<HashMap>> extractData(ResultSet rs) throws SQLException, DataAccessException {
                    HashMap<String, List<HashMap>> mapData = new HashMap<String, List<HashMap>>();
                    List<HashMap> user = new ArrayList<HashMap>();
                    List<HashMap> system = new ArrayList<HashMap>();
                    while (rs.next()) {
                        HashMap map = new HashMap();
                        if (rs.getString("list_type").equals("UserDefinedList")) {
                            map.put("id", rs.getInt("id"));
                            map.put("listName", rs.getString("list_name"));
                            //  map.put("listType",rs.getString("list_type"));
                            user.add(map);
                        } else if (rs.getString("list_type").equals("SystemDefinedList")) {
                            map.put("id", rs.getInt("id"));
                            map.put("listName", rs.getString("list_name"));
                            system.add(map);
                        }
                        //map.put(rs.getString("list_name"),Arrays.asList(rs.getString("options_list").split(",")));
                    }
                    mapData.put("userdefined", user);
                    mapData.put("systemDefined", system);
                    return mapData;
                }

            });

        } catch (Exception e) {
            log4jLog.info("selectPredefinedList" + e);
            return new HashMap<String, List<HashMap>>();
        }

    }

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    public HashMap<String, List<HashMap>> getAllListData(int id, int accountId) {
        final int accountId1=accountId;
        String query = "select predifined_list_categories.options_list,predifined_list_categories.id,predifined_list_categories.list_name from predifined_list_categories inner join custom_forms_fields on custom_forms_fields.list_id_fk=predifined_list_categories.id where custom_forms_fields.form_id_fk=? and predifined_list_categories.list_type=?";
        log4jLog.info(" getAllListData " + query);
        int count = 0;
        Object[] param = new Object[]{id, "UserDefinedList"};
        //String options="";
        // List<String> listOfCustomers=new ArrayList<String>();
        try {
  //List<HashMap> listOfMapData1=new ArrayList<HashMap>();
  
          //  List<HashMap> listOfMapData=new ArrayList<HashMap>();
            List<HashMap> listOfMapData = FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param,new RowMapper<HashMap>() {
                @Override
                public HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    HashMap map = new HashMap();
                    //List listOfMapdata = new ArrayList();
                    //map.put(rs.getInt("id"),Arrays.asList(rs.getString("options_list").split(",")));
                    map.put("id", rs.getString("id"));
                    map.put("listName",rs.getString("list_name"));
                    map.put("optionList", Arrays.asList(rs.getString("options_list").split(",")));
                    //listOfMapdata.add(map);
                    return map;
                }
            });
            //System.out.println("siddheshMap"+listData);
            query = "select c.list_id_fk,p.list_name from custom_forms_fields as c left join predifined_list_categories as p on c.list_id_fk=p.id where c.form_id_fk=? and p.list_type='SystemDefinedList'";
            log4jLog.info(" getAllListData " + query);
            param = new Object[]{id};
            List<HashMap> databaseValues = FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<HashMap>() {
                List<String> listOfCustomerNames=new ArrayList<String>();
                @Override
                public HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    HashMap map = new HashMap();
                    //List listOfMapdata = new ArrayList();
                    //map.put(rs.getInt("id"),Arrays.asList(rs.getString("options_list").split(",")));
                    if(rs.getString("list_name").equals("Customer Name")){
                        String  query1 = "SELECT customer_name FROM customers WHERE record_state !=3 ORDER BY customer_name ASC";
                        listOfCustomerNames=FieldSenseUtils.getJdbcTemplateForAccount(accountId1).queryForList(query1,String.class);                        
                    }
                    map.put("id", rs.getString("list_id_fk"));
                    map.put("listName",rs.getString("list_name"));
                    map.put("optionList", listOfCustomerNames);
                    //listOfMapdata.add(map);
                    return map;
                }

            });
            // System.out.println("SiddheshCount"+databaseValues);
           // HashMap<String, List<String>> listData1 = new HashMap<String, List<String>>();
           // if (databaseValues.isEmpty()) {
             //   query = "SELECT customer_name FROM customers ORDER BY customer_name ASC";
            //   listOfCustomerNames=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query,ArrayList.class);
                
            /*listOfMapData1= FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query,new RowMapper<HashMap>() {

                 @Override
                public HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    
                    
                    HashMap map = new HashMap();
                    //List listOfMapdata = new ArrayList();
                    //map.put(rs.getInt("id"),Arrays.asList(rs.getString("options_list").split(",")));
                    map.put("customerName", rs.getString("customer_name"));
                  //  map.put("optionList", Arrays.asList(rs.getString("options_list").split(",")));
                    //listOfMapdata.add(map);
                    return map;
                }
                
            });*/
           

            HashMap<String,List<HashMap>> listOfAllData = new HashMap<String,List<HashMap>>();

         //   databaseValues
            
            listOfAllData.put("SystemDefinedList",databaseValues);
            listOfAllData.put("UserDefinedList", listOfMapData);
            return listOfAllData;
        } catch (Exception e) {
            log4jLog.info("selectPredefinedList" + e);
            return new HashMap<String, List<HashMap>>();
        }

    }

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    public List<HashMap<String, List<String>>> getAllListData1(int id, int accountId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param listId
     * @param accountId
     * @return
     */
    public boolean deletePredefinedListStatus(int listId, int accountId) {
       
           String query = "SELECT count(list_id_fk) as list_id from custom_forms_fields where list_id_fk=?";
        log4jLog.info(" isPredefinedListAlreadyExists " + query);
        Object[] param = new Object[]{listId};
        try {
          List<Integer> numberOfRows =FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForList(query,param,Integer.class); 
      //      System.out.println("SiddheshNumberOfRows"+numberOfRows);
            if(numberOfRows.get(0)==0) {
                return true;
                
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isPredefinedListAlreadyExists " + e);
            return false;
        }
        
       
    }

    /**
     * @Added by jyoti
     * @param predefinedListId
     * @param accountId
     * @return 
     */
    public List<Integer> selectListOfFormForPredefinedList(int predefinedListId, int accountId) {
        String query = "SELECT DISTINCT form_id_fk FROM custom_forms_fields WHERE list_id_fk = ?";
        log4jLog.info("selectListOfFormForPredefinedList " + query);
        Object[] param = new Object[]{predefinedListId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForList(query, param,Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info("selectPredefinedList" + e);
            return new ArrayList<Integer>();
        }
    }

    /**
     * @Added by jyoti
     * @param formId
     * @param accountId
     * @return 
     */
    public boolean updateCustomFormUpdatedOn(int formId, int accountId) {
//        System.out.println("updateCustomFormUpdatedOn, formid : "+formId);
        String query = "UPDATE custom_forms SET updatedon = now() WHERE id = ?";
        log4jLog.info(" updateCustomFormUpdatedOn " + query);
        Object[] param = new Object[]{formId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info(" updateCustomFormUpdatedOn " + e);
            return false;
        }
    }
}
