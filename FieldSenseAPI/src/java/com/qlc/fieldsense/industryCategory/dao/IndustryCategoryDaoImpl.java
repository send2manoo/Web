/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.industryCategory.dao;

import com.qlc.fieldsense.industryCategory.model.IndustryCategory;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author awaneesh
 */
public class IndustryCategoryDaoImpl implements IndustryCategoryDao {
    
    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("IndustryCategoryDaoImpl");

    /**
     *
     * @param eCategory
     * @param accountId
     * @return
     */
    @Override
    public int createIndustryCategory(IndustryCategory eCategory, int accountId) {
        String query = "INSERT INTO industry_categories(category_name,is_active,created_on,created_by_id_fk,updated_on) VALUES(?,?,now(),?,now())";
        log4jLog.info(" createActivityPurpose " + query);
        Object param[] = new Object[]{eCategory.getCategoryName(), eCategory.isIsActive(), eCategory.getCreatedBy().getId()};
        try {
            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    try {
                        String query1 = "SELECT MAX(id) FROM industry_categories";
                        return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query1, Integer.class);
                    } catch (Exception e) {
                        log4jLog.info("createIndustryCategory" + e);
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            log4jLog.info(" createIndustryCategory " + e);
            return 0;
        }
    }

    /**
     *
     * @param allRequestParams
     * @param accountId
     * @return
     */
    @Override
    public List<IndustryCategory> selectAllIndustryCategoryWithOffset(@RequestParam Map<String,String> allRequestParams, int accountId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT id,category_name,is_active,created_on,created_by_id_fk,(SELECT count(id) FROM industry_categories) as eCategoryCount");
        query.append(" FROM industry_categories");
        String sortcolindex=allRequestParams.get("order[0][column]");
        if(sortcolindex==null){
            query.append(" ORDER BY created_on DESC");
        }else if(Integer.parseInt(sortcolindex)==0){
            if(allRequestParams.get("order[0][dir]").equals("asc")){
                query.append(" ORDER BY category_name");
            }else{
                query.append(" ORDER BY category_name DESC");
            }
        }
        query.append(" LIMIT ? OFFSET ?");
        int start=Integer.parseInt(allRequestParams.get("start"));
        int length=Integer.parseInt(allRequestParams.get("length"));
        log4jLog.info("selectAllIndustryCategoryWithOffset " + query);
        Object param[] = new Object[]{length,start};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<IndustryCategory>() {

                @Override
                public IndustryCategory mapRow(ResultSet rs, int i) throws SQLException {
                    IndustryCategory eCategory = new IndustryCategory();
                    eCategory.setId(rs.getInt("id"));
                    eCategory.setCategoryName(rs.getString("category_name"));
                    eCategory.setIsActive(rs.getBoolean("is_active"));
                    eCategory.setCretedOn(rs.getTimestamp("created_on"));
                    User user = new User();
                    user.setId(rs.getInt("created_by_id_fk"));
                    eCategory.setCreatedBy(user);
                    eCategory.setTotalIndustryCategory(rs.getInt("eCategoryCount"));
                    return eCategory;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectAllIndustryCategoryWithOffset " + e);
            return new ArrayList<IndustryCategory>();
        }
    }

    /**
     *
     * @param eCategoryId
     * @param accountId
     * @return
     */
    @Override
    public IndustryCategory selectIndustryCategory(int eCategoryId, int accountId) {
        String query = "SELECT id,category_name,is_active,created_on,created_by_id_fk FROM industry_categories WHERE id=?";
        log4jLog.info("selectIndustryCategory " + query);
        Object[] param = new Object[]{eCategoryId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query.toString(), param, new RowMapper<IndustryCategory>() {

                @Override
                public IndustryCategory mapRow(ResultSet rs, int i) throws SQLException {
                    IndustryCategory eCategory = new IndustryCategory();
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
            log4jLog.info("selectIndustryCategory " + e);
            return new IndustryCategory();
        }
    }

    /**
     *
     * @param eCategory
     * @param accountId
     * @return
     */
    @Override
    public IndustryCategory updateIndustryCategory(IndustryCategory eCategory, int accountId) {
        String query = "UPDATE industry_categories SET category_name=?,is_active=?,updated_on = now() WHERE id=?";
        log4jLog.info(" updateIndustryCategory " + query);
        Object[] param = new Object[]{eCategory.getCategoryName(), eCategory.isIsActive(), eCategory.getId()};

        try {
            String category=getIndustryCategory(eCategory.getId(), accountId);
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                //Update all customers with Unknown Industry
                param = new Object[]{eCategory.getCategoryName(),category};
                query = "UPDATE customers SET industry=?,record_state = 2 WHERE industry=?";
                FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) ;
                // end of update customers
                return eCategory;
            } else {
                return new IndustryCategory();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info(" updateIndustryCategory " + e);
            return new IndustryCategory();
        }
    }

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    @Override
    public boolean deleteIndustryCategory(int id, int accountId) {
        String query = "DELETE FROM industry_categories WHERE id=?";
        log4jLog.info(" deleteIndustryCategory " + query);
        Object[] param = new Object[]{id};

        try {
            // Added by jyoti, optimization release feb 2018
            IndustryCategory industryCategoryObject = selectIndustryCategory(id, accountId);
            FieldSenseUtils.setDeletedCategoriesRecord("industryCategory", industryCategoryObject.getId(), industryCategoryObject.getCategoryName(), industryCategoryObject.isIsActive(), 0, 0, accountId);
            // Ended by jyoti, optimization release feb 2018
            String category=getIndustryCategory(id, accountId);
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                //Update all customers with Unknown Industry
                param = new Object[]{"Unknown",category};
                query = "UPDATE customers SET industry=?,record_state = 2 WHERE industry=?";
                FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) ;
                // end of update customers
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" deleteIndustryCategory " + e);
            return false;
        }
    }

    /**
     *
     * @param categoryName
     * @param accountId
     * @return
     */
    @Override
    public boolean isIndustryCategoryAlreadyExists(String categoryName, int accountId) {
        String query = "SELECT COUNT(id) FROM industry_categories WHERE category_name=?";
        log4jLog.info(" isIndustryCategoryAlreadyExists " + query);
        Object[] param = new Object[]{categoryName};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isIndustryCategoryAlreadyExists " + e);
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
    public boolean isIndustryCategoryValid(int id, int accountId) {
        String query = "SELECT COUNT(id) FROM industry_categories WHERE id=?";
        log4jLog.info(" isIndustryCategoryValid " + query);
        Object[] param = new Object[]{id};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isIndustryCategoryValid " + e);
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
    public String getIndustryCategory(int eCategoryId, int accountId) {
        String query = "SELECT category_name FROM industry_categories WHERE id=?";
        log4jLog.info(" getIndustryCategory " + query);
        Object[] param = new Object[]{eCategoryId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" getIndustryCategory " + e);
            return null;
        }
    }
    
    /**
     *
     * @param accountId
     * @return
     */
    @Override
    public  List<IndustryCategory>  selectAllActiveIndustryCategory(int accountId) {
       String query = new String();
        query="SELECT id,category_name,is_active,created_on,created_by_id_fk FROM industry_categories WHERE is_active=1 order by category_name";
        log4jLog.info("selectAllActiveIndustryCategory " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, new RowMapper<IndustryCategory>() {

                @Override
                public IndustryCategory mapRow(ResultSet rs, int i) throws SQLException {
                    IndustryCategory eCategory = new IndustryCategory();
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
            log4jLog.info("selectAllActiveIndustryCategory " + e);
            return new ArrayList<IndustryCategory>();
        }
    }
}
