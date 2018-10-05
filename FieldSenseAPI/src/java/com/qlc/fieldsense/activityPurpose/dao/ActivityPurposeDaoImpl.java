/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.activityPurpose.dao;

import com.qlc.fieldsense.activityPurpose.model.ActivityPurpose;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
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
public class ActivityPurposeDaoImpl implements ActivityPurposeDao {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("ActivityPurposeDaoImpl");

    /**
     *
     * @param aPurpose
     * @param accountId
     * @return
     */
    @Override
    public int createActivityPurpose(ActivityPurpose aPurpose, int accountId) {
//        // Added by Jyoti 26-june-2017
//        boolean flagToCheckPurposeExist;
//        String queryToCheckIsDeletedPurpose = "SELECT COUNT(id) FROM activity_purpose WHERE purpose=? AND isDeleted =1";
//        Object[] paramToCheckIsDeletedPurpose = new Object[]{aPurpose.getPurpose()};
//        try {
//            synchronized (this) {
//                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(queryToCheckIsDeletedPurpose, paramToCheckIsDeletedPurpose, Integer.class) == 1) {
//                    flagToCheckPurposeExist = true;
//                } else {
//                    flagToCheckPurposeExist = false;
//                }
//            }
//        } catch (Exception e) {
//            log4jLog.info(" 1 createActivityPurpose " + e);
//            return 0;
//        }
//        if(flagToCheckPurposeExist == true){
//            String query = "UPDATE activity_purpose SET is_active = ?, created_on = now(), created_by_id_fk = ?, isDeleted = 0 WHERE purpose = ?";        
//            log4jLog.info(" createActivityPurpose ,update isdeleted flag " + query);
//            Object param[] = new Object[]{aPurpose.isIsActive(), aPurpose.getCreatedBy().getId(), aPurpose.getPurpose()};
//            try {
//                synchronized (this) {
//                    if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
//                        try {
//                            String query1 = "SELECT id FROM activity_purpose WHERE purpose = ?";
//                            Object param1[] = new Object[]{aPurpose.getPurpose()};
//                            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query1, param1, Integer.class);
//                        } catch (Exception e) {
//                            log4jLog.info("2 createActivityPurpose" + e);
//                            return 0;
//                        }
//                    } else {
//                        return 0;
//                    }
//                }
//            } catch (Exception e) {
//                log4jLog.info(" 3 createActivityPurpose " + e);
//                return 0;
//            }
//        }
//        else if(flagToCheckPurposeExist == false){
//    // Ended by Jyoti 26-june-2017
            String query = "INSERT INTO activity_purpose(purpose,is_active,created_on,created_by_id_fk,updated_on) VALUES(?,?,now(),?,now())";        
            log4jLog.info(" createActivityPurpose " + query);
            Object param[] = new Object[]{aPurpose.getPurpose(), aPurpose.isIsActive(), aPurpose.getCreatedBy().getId()};
            try {
                synchronized (this) {
                    if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                        try {
                            String query1 = "SELECT MAX(id) FROM activity_purpose";
                            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query1, Integer.class);
                        } catch (Exception e) {
                            log4jLog.info("createActivityPurpose" + e);
                            return 0;
                        }
                    } else {
                        return 0;
                    }
                }
            } catch (Exception e) {
                log4jLog.info(" createActivityPurpose " + e);
                return 0;
            }
//        } // by jyoti
//        return 0; // by jyoti
    }

    /**
     *
     * @param aPurposeId
     * @param accountId
     * @return
     */
    @Override
    public ActivityPurpose selectActivityPurpose(int aPurposeId, int accountId) {
        String query = "SELECT id,purpose,is_active,created_on,created_by_id_fk FROM activity_purpose WHERE id=?";
        log4jLog.info("selectActivityPurpose " + query);
        Object[] param = new Object[]{aPurposeId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query.toString(), param, new RowMapper<ActivityPurpose>() {

                @Override
                public ActivityPurpose mapRow(ResultSet rs, int i) throws SQLException {
                    ActivityPurpose aPurpose = new ActivityPurpose();
                    aPurpose.setId(rs.getInt("id"));
                    aPurpose.setPurpose(rs.getString("purpose"));
                    aPurpose.setIsActive(rs.getBoolean("is_active"));
                    aPurpose.setCretedOn(rs.getTimestamp("created_on"));
                    User user = new User();
                    user.setId(rs.getInt("created_by_id_fk"));
                    aPurpose.setCreatedBy(user);
                    return aPurpose;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectActivityPurpose " + e);
            return new ActivityPurpose();
        }
    }

    /**
     *
     * @param accountId
     * @return
     */
    @Override
    public List<ActivityPurpose> selectAllActiveActivityPurpose(int accountId) {
        String query = "SELECT id,purpose,is_active,created_on,created_by_id_fk FROM activity_purpose WHERE is_active=1 ORDER BY purpose";
        log4jLog.info("selectAllActiveActivityPurpose " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), new RowMapper<ActivityPurpose>() {

                @Override
                public ActivityPurpose mapRow(ResultSet rs, int i) throws SQLException {
                    ActivityPurpose aPurpose = new ActivityPurpose();
                    aPurpose.setId(rs.getInt("id"));
                    aPurpose.setPurpose(rs.getString("purpose"));
                    aPurpose.setIsActive(rs.getBoolean("is_active"));
                    aPurpose.setCretedOn(rs.getTimestamp("created_on"));
                    User user = new User();
                    user.setId(rs.getInt("created_by_id_fk"));
                    aPurpose.setCreatedBy(user);
                    return aPurpose;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectAllActiveActivityPurpose " + e);
            return new ArrayList<ActivityPurpose>();
        }
    }

    /**
     *
     * @param aPurpose
     * @param accountId
     * @return
     */
    @Override
    public ActivityPurpose updateActivityPurpose(ActivityPurpose aPurpose, int accountId) {
        String query = "UPDATE activity_purpose SET purpose=?,is_active=?, updated_on = now() WHERE id=?";
        log4jLog.info(" updateActivityPurpose " + query);
        Object[] param = new Object[]{aPurpose.getPurpose(), aPurpose.isIsActive(), aPurpose.getId()};

        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return aPurpose;
            } else {
                return new ActivityPurpose();
            }
        } catch (Exception e) {
            log4jLog.info(" updateActivityPurpose " + e);
            return new ActivityPurpose();
        }
    }

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    @Override
    public boolean deleteActivityPurpose(int id, int accountId) {
        String query = "DELETE FROM activity_purpose WHERE id=?";
        log4jLog.info(" deleteActivityPurpose " + query);
        Object[] param = new Object[]{id};
        
        // Added by Jyoti 27-june-2017, purpose - to fix the issue of visits whose purpose has been deleted
        try {
            int purposeId = id;
            // Added by jyoti, optimization release feb 2018
            ActivityPurpose activityPurposeObject = selectActivityPurpose(id, accountId);
            FieldSenseUtils.setDeletedCategoriesRecord("purposeCategory", activityPurposeObject.getId(), activityPurposeObject.getPurpose(), activityPurposeObject.isIsActive(), 0, 0, accountId);
            // Ended by jyoti, optimization release feb 2018
            int unknownPurposeId=getUnknownPurposeId(id, accountId);
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                //Update all related appointments with Unknown purpose
                param = new Object[]{unknownPurposeId,purposeId};
                query = "UPDATE appointments SET purpose_id_fk=? WHERE purpose_id_fk=?";
                FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) ;
                // end of, update related appointments purpose
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" deleteActivityPurpose " + e);
            e.printStackTrace();
            return false;
        }
        // Ended by JYoti
        //Commented by jyoti start
//        try {
//            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            log4jLog.info(" deleteActivityPurpose " + e);
//            return false;
//        }
        // comment ended by jyoti
    }

    // Added by jyoti 27-june-2017 
    @Override
    public int getUnknownPurposeId(int purposeId, int accountId) {
        String query = "SELECT id FROM activity_purpose WHERE purpose = ?";
        log4jLog.info(" getPurposeCategory " + query);
        Object[] param = new Object[]{"Unknown"};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" getUnknownPurposeId " + e);
            return 0;
        }
    }
    
    // Added by jyoti 27-june-2017 
    @Override
    public String getPurposeCategory(int purposeId, int accountId) {
        String query = "SELECT purpose FROM activity_purpose WHERE id=?";
        log4jLog.info(" getPurposeCategory " + query);
        Object[] param = new Object[]{purposeId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" getPurposeCategory " + e);
            return null;
        }
    }
    
    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    @Override
    public boolean isActivityPurposeValid(int id, int accountId) {
        String query = "SELECT COUNT(id) FROM activity_purpose WHERE id=?";
        log4jLog.info(" isActivityPurposeValid " + query);
        Object[] param = new Object[]{id};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isActivityPurposeValid " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param accountId
     * @return
     */
    @Override
    public List<ActivityPurpose> selectAllActivityPurpose(int accountId) {
        String query = "SELECT id,purpose,is_active,created_on,created_by_id_fk FROM activity_purpose ORDER BY purpose "; 
        log4jLog.info("selectAllActivityPurpose " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), new RowMapper<ActivityPurpose>() {

                @Override
                public ActivityPurpose mapRow(ResultSet rs, int i) throws SQLException {
                    ActivityPurpose aPurpose = new ActivityPurpose();
                    aPurpose.setId(rs.getInt("id"));
                    aPurpose.setPurpose(rs.getString("purpose"));
                    aPurpose.setIsActive(rs.getBoolean("is_active"));
                    aPurpose.setCretedOn(rs.getTimestamp("created_on"));
                    User user = new User();
                    user.setId(rs.getInt("created_by_id_fk"));
                    aPurpose.setCreatedBy(user);
                    return aPurpose;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectAllActivityPurpose " + e);
            return new ArrayList<ActivityPurpose>();
        }
    }

    /**
     *
     * @param allRequestParams
     * @param accountId
     * @return
     */
    @Override
    public List<ActivityPurpose> selectAllActivityPurposeWithOffset(@RequestParam Map<String,String> allRequestParams, int accountId) {
//        String query = "SELECT id,purpose,is_active,created_on,created_by_id_fk FROM activity_purpose ORDER BY created_on DESC ";
        StringBuilder query = new StringBuilder();
        query.append("SELECT id,purpose,is_active,created_on,created_by_id_fk,(SELECT count(id) FROM activity_purpose) as activityCount");
        query.append(" FROM activity_purpose");
        query.append(" INNER JOIN (SELECT id FROM activity_purpose ");
        String sortcolindex=allRequestParams.get("order[0][column]");
        if(sortcolindex==null){
            query.append("ORDER BY created_on DESC");
        }else if(Integer.parseInt(sortcolindex)==0){
            if(allRequestParams.get("order[0][dir]").equals("asc")){
                query.append(" ORDER BY purpose");
            }else{
                query.append(" ORDER BY purpose DESC");
            }
        }
        query.append(" LIMIT ? OFFSET ?) AS my_results USING(id)");
        log4jLog.info("selectAllActivityPurpose " + query);
        final int start=Integer.parseInt(allRequestParams.get("start"));
        final int length=Integer.parseInt(allRequestParams.get("length"));
        Object param[] = new Object[]{length,start};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<ActivityPurpose>() {

                @Override
                public ActivityPurpose mapRow(ResultSet rs, int i) throws SQLException {
                    ActivityPurpose aPurpose = new ActivityPurpose();
                    aPurpose.setId(rs.getInt("id"));
                    aPurpose.setPurpose(rs.getString("purpose"));
                    aPurpose.setIsActive(rs.getBoolean("is_active"));
                    aPurpose.setCretedOn(rs.getTimestamp("created_on"));
                    User user = new User();
                    user.setId(rs.getInt("created_by_id_fk"));
                    aPurpose.setCreatedBy(user);
                    aPurpose.setTotalActivityPurpose(rs.getInt("activityCount"));
                    return aPurpose;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectAllActivityPurpose " + e);
            return new ArrayList<ActivityPurpose>();
        }
    }

    /**
     *
     * @param purpose
     * @param accountId
     * @return
     */
    @Override
    public boolean isActivityPurposeAlreadyExists(String purpose, int accountId) {
        String query = "SELECT COUNT(id) FROM activity_purpose WHERE purpose=?";
        log4jLog.info(" isActivityPurposeAlreadyExists " + query);
        Object[] param = new Object[]{purpose};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isActivityPurposeAlreadyExists " + e);
            return false;
        }
    }

    /**
     *
     * @param purpose
     * @param accountId
     * @return
     */
    @Override
    public boolean isActivityPurposeAlreadyExistsForUpdate(ActivityPurpose purpose, int accountId) {
        String query = "SELECT COUNT(id) FROM activity_purpose WHERE purpose=? and id !=?";
        log4jLog.info(" isActivityPurposeAlreadyExists " + query);
        Object[] param = new Object[]{purpose.getPurpose(),purpose.getId()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isActivityPurposeAlreadyExists " + e);
            return false;
        }
    }
    
    /**
     *
     * @param purposeId
     * @param accountId
     * @return
     */
    @Override
    public String getPurpose(int purposeId, int accountId) {
        String query = "SELECT purpose FROM activity_purpose WHERE id=?";
        log4jLog.info(" getPurpose " + query);
        Object[] param = new Object[]{purposeId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" getPurpose " + e);
            return null;
        }
    }
}
