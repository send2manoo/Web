/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.activityOutcome.dao;

import com.qlc.fieldsense.activityOutcome.model.ActivityOutcome;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author anuja
 */
public class ActivityOutcomeDaoImpl implements ActivityOutcomeDao {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger(" ActivityOutcomeDaoImpl ");

    /**
     *
     * @param accountId
     * @return
     */
    @Override
    public List<ActivityOutcome> getAllActivityOutcomes(int accountId) {

        String query = "SELECT id,outcome,is_positive,is_active,created_on,created_by_id_fk FROM activity_outcomes";
        log4jLog.info("getAllActivityOutcomes " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), new RowMapper<ActivityOutcome>() {
                @Override
                public ActivityOutcome mapRow(ResultSet rs, int i) throws SQLException {
                    ActivityOutcome aOutcome = new ActivityOutcome();
                    aOutcome.setId(rs.getInt("id"));
                    aOutcome.setOutcome(rs.getString("outcome"));
                    aOutcome.setIsPositive(rs.getInt("is_positive"));
                    aOutcome.setIsActive(rs.getInt("is_active"));
                    aOutcome.setCreatedOn(rs.getTimestamp("created_on"));
                    User user = new User();
                    user.setId(rs.getInt("created_by_id_fk"));
                    aOutcome.setCreatedBy(user);
                    return aOutcome;
                }
            });
        } catch (Exception e) {
            log4jLog.info("getAllActivityOutcomes " + e);
            return new ArrayList<ActivityOutcome>();
        }
    }
    
    /*Auto-increament is not set for id*/
}
