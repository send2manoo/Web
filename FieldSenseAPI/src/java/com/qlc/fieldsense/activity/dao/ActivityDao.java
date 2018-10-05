/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.activity.dao;

import com.qlc.fieldsense.activity.model.Activity;
import java.util.List;

/**
 *
 * @author anuja
 */
public interface ActivityDao {

    /**
     *
     * @param activity
     * @param accountId
     * @return
     */
    public int creatActivity(Activity activity, int accountId);

    /**
     *
     * @param teamId
     * @param accountId
     * @return
     */
    public List<Activity> selectActivity(int teamId, int accountId);

    /**
     *
     * @param teamId
     * @param userId
     * @param accountId
     * @return
     */
    public List<Activity> selectActivityForUser(int teamId, int userId, int accountId);

    /**
     *
     * @param activityId
     * @param accountId
     * @return
     */
    public Activity selectOneActivity(int activityId, int accountId);

    /**
     *
     * @param cutomerId
     * @param accountId
     * @return
     */
    public List<Activity> selectActivityForCustomer(int cutomerId, int accountId);
}
