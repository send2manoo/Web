/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.expenseCategory.dao;

import static com.qlc.fieldsense.activityPurpose.dao.ActivityPurposeDaoImpl.log4jLog;
import com.qlc.fieldsense.expenseCategory.model.ExpenseCategory;
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
 * @author anuja
 */
public class ExpenseCategoryDaoImpl implements ExpenseCategoryDao {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("ExpenseCategoryDaoImpl");

    /**
     *
     * @param eCategory
     * @param accountId
     * @return
     */
    @Override
    public int createExpenseCategory(ExpenseCategory eCategory, int accountId) {
        String query = "INSERT INTO expense_categories(category_name,is_active,created_on,created_by_id_fk,updated_on) VALUES(?,?,now(),?,now())";
        log4jLog.info(" createActivityPurpose " + query);
        Object param[] = new Object[]{eCategory.getCategoryName(), eCategory.isIsActive(), eCategory.getCreatedBy().getId()};
        try {
            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    try {
                        String query1 = "SELECT MAX(id) FROM expense_categories";
                        return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query1, Integer.class);
                    } catch (Exception e) {
                        log4jLog.info("createExpenseCategory" + e);
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            log4jLog.info(" createExpenseCategory " + e);
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
    public List<ExpenseCategory> selectAllExpenseCategoryWithOffset(@RequestParam Map<String,String> allRequestParams, int accountId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT id,category_name,is_active,created_on,updated_on,created_by_id_fk,(SELECT count(id) FROM expense_categories) as eCategoryCount");
        query.append(" FROM expense_categories");
        query.append(" INNER JOIN (SELECT id FROM expense_categories ");
        String sortcolindex=allRequestParams.get("order[0][column]");
        if(sortcolindex==null){
            query.append("ORDER BY created_on DESC ");
        }else if(Integer.parseInt(sortcolindex)==0){
            if(allRequestParams.get("order[0][dir]").equals("asc")){
                query.append(" ORDER BY category_name ");
            }else{
                query.append(" ORDER BY category_name DESC ");
            }
        }
        query.append(" LIMIT ? OFFSET ?) AS my_results USING(id)");
        final int start=Integer.parseInt(allRequestParams.get("start"));
        final int length=Integer.parseInt(allRequestParams.get("length"));
        log4jLog.info("selectAllExpenseCategoryWithOffset " + query);
        Object param[] = new Object[]{length,start};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<ExpenseCategory>() {

                @Override
                public ExpenseCategory mapRow(ResultSet rs, int i) throws SQLException {
                    ExpenseCategory eCategory = new ExpenseCategory();
                    eCategory.setId(rs.getInt("id"));
                    eCategory.setCategoryName(rs.getString("category_name"));
                    eCategory.setIsActive(rs.getBoolean("is_active"));
                    eCategory.setCretedOn(rs.getTimestamp("created_on"));
                    User user = new User();
                    user.setId(rs.getInt("created_by_id_fk"));
                    eCategory.setCreatedBy(user);
                    eCategory.setTotalexpenseCategory(rs.getInt("eCategoryCount"));
                    return eCategory;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectAllExpenseCategoryWithOffset " + e);
            return new ArrayList<ExpenseCategory>();
        }
    }

    /**
     *
     * @param eCategoryId
     * @param accountId
     * @return
     */
    @Override
    public ExpenseCategory selectExpenseCategory(int eCategoryId, int accountId) {
        String query = "SELECT id,category_name,is_active,created_on,created_by_id_fk FROM expense_categories WHERE id=?";
        log4jLog.info("selectActivityPurpose " + query);
        Object[] param = new Object[]{eCategoryId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query.toString(), param, new RowMapper<ExpenseCategory>() {

                @Override
                public ExpenseCategory mapRow(ResultSet rs, int i) throws SQLException {
                    ExpenseCategory eCategory = new ExpenseCategory();
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
            log4jLog.info("selectExpenseCategory " + e);
            return new ExpenseCategory();
        }
    }

    /**
     *
     * @param eCategory
     * @param accountId
     * @return
     */
    @Override
    public ExpenseCategory updateExpenseCategory(ExpenseCategory eCategory, int accountId) {
        String query = "UPDATE expense_categories SET category_name=?,is_active=?, updated_on = now() WHERE id=?";
        log4jLog.info(" updateExpenseCategory " + query);
        Object[] param = new Object[]{eCategory.getCategoryName(), eCategory.isIsActive(), eCategory.getId()};

        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return eCategory;
            } else {
                return new ExpenseCategory();
            }
        } catch (Exception e) {
            log4jLog.info(" updateExpenseCategory " + e);
            return new ExpenseCategory();
        }
    }

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    @Override
    public boolean deleteExpenseCategory(int id, int accountId) {
        String query = "DELETE FROM expense_categories WHERE id=?";
        log4jLog.info(" deleteExpenseCategory " + query);
        Object[] param = new Object[]{id};

        try {
            // Added by jyoti, optimization release feb 2018
            ExpenseCategory expenseCategoryObject = selectExpenseCategory(id, accountId);
            FieldSenseUtils.setDeletedCategoriesRecord("expenseCategory", expenseCategoryObject.getId(), expenseCategoryObject.getCategoryName(), expenseCategoryObject.isIsActive(), 0, 0, accountId);
            // Ended by jyoti, optimization release feb 2018
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" deleteExpenseCategory " + e);
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
    public boolean isExpenseCategoryAlreadyExists(String categoryName, int accountId) {
        String query = "SELECT COUNT(id) FROM expense_categories WHERE category_name=?";
        log4jLog.info(" isExpenseCategoryAlreadyExists " + query);
        Object[] param = new Object[]{categoryName};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isExpenseCategoryAlreadyExists " + e);
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
    public boolean isExpenseCategoryValid(int id, int accountId) {
        String query = "SELECT COUNT(id) FROM expense_categories WHERE id=?";
        log4jLog.info(" isExpenseCategoryValid " + query);
        Object[] param = new Object[]{id};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isExpenseCategoryValid " + e);
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
    public String getExpenseCategory(int eCategoryId, int accountId) {
        String query = "SELECT category_name FROM expense_categories WHERE id=?";
        log4jLog.info(" getExpenseCategory " + query);
        Object[] param = new Object[]{eCategoryId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" getExpenseCategory " + e);
            return null;
        }
    }
    
    /**
     *
     * @param accountId
     * @return
     */
    @Override
    public  List<ExpenseCategory>  selectAllActiveExpenseCategory(int accountId) {
       String query = new String();
        query="SELECT id,category_name,is_active,created_on,created_by_id_fk FROM expense_categories WHERE is_active=1";
        log4jLog.info("selectAllActiveExpenseCategory " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, new RowMapper<ExpenseCategory>() {

                @Override
                public ExpenseCategory mapRow(ResultSet rs, int i) throws SQLException {
                    ExpenseCategory eCategory = new ExpenseCategory();
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
            log4jLog.info("selectAllActiveExpenseCategory " + e);
            return new ArrayList<ExpenseCategory>();
        }
    }

}
