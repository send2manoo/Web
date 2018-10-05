/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.activityOutcome.dao;

import com.qlc.fieldsense.activityOutcome.model.ActivityOutcome;
import java.util.List;

/**
 *
 * @author anuja
 */
public interface ActivityOutcomeDao {
    
    /**
     *
     * @param accountId
     * @return
     */
    public List<ActivityOutcome> getAllActivityOutcomes(int accountId);
    
}
