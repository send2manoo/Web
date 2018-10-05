/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.activityPurpose.dao;

import com.qlc.fieldsense.activityPurpose.model.ActivityPurpose;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author anuja
 */
public interface ActivityPurposeDao {

    /**
     *
     * @param aPurpose
     * @param accountId
     * @return
     */
    public int createActivityPurpose(ActivityPurpose aPurpose, int accountId);

    /**
     *
     * @param aPurposeId
     * @param accountId
     * @return
     */
    public ActivityPurpose selectActivityPurpose(int aPurposeId, int accountId);

    /**
     *
     * @param accountId
     * @return
     */
    public List<ActivityPurpose> selectAllActiveActivityPurpose(int accountId);

    /**
     *
     * @param aPurpose
     * @param accountId
     * @return
     */
    public ActivityPurpose updateActivityPurpose(ActivityPurpose aPurpose, int accountId);

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    public boolean deleteActivityPurpose(int id, int accountId);

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    public boolean isActivityPurposeValid(int id, int accountId);

    /**
     *
     * @param accountId
     * @return
     */
    public List<ActivityPurpose> selectAllActivityPurpose(int accountId);

    /**
     *
     * @param purpose
     * @param accountId
     * @return
     */
    public boolean isActivityPurposeAlreadyExists(String purpose, int accountId);
    
    /**
     *
     * @param purpose
     * @param accountId
     * @return
     */
    public boolean isActivityPurposeAlreadyExistsForUpdate(ActivityPurpose purpose, int accountId);

    /**
     *
     * @param purposeId
     * @param accountId
     * @return
     */
    public String getPurpose(int purposeId, int accountId);

    /**
     *
     * @param allRequestParams
     * @param accountId
     * @return
     */
    public List<ActivityPurpose> selectAllActivityPurposeWithOffset(@RequestParam Map<String,String> allRequestParams,  int accountId);
    
    //Added by jyoti 27-june-2017
    public int getUnknownPurposeId(int purposeId, int accountId);
    
    //Added by jyoti 27-june-2017
    public String getPurposeCategory(int purposeId, int accountId);
}
