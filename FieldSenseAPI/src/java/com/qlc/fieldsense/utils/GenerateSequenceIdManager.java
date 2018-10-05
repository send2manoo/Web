/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.utils;

import org.apache.log4j.Logger;

/**
 *
 * @author Ramesh
 * @date 10-02-2014
 * 
 */
public class GenerateSequenceIdManager {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("GenerateSequenceIdManager");

    /**
     *
     * @param tableName
     * @param accountId
     * @return
     */
    public static synchronized int getNewSequenceIdForAccount(String tableName, int accountId) {
        int id = 0;
        String queryUpdate = "UPDATE " + tableName + " SET next_val = (next_val + 1) WHERE id=1";
        String queryForPostId = "SELECT next_val FROM " + tableName;
        log4jLog.info("queryUpdate: " + queryUpdate);

        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(queryUpdate) > 0) {
                log4jLog.info("Sequence is updated");
                try {
                    log4jLog.info("queryForPostId: " + queryForPostId);
                    id = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(queryForPostId, Integer.class);

                } catch (Exception e) {
                    id = 0;
//                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            id = 0;
        }
        return id;
    }

    /**
     *
     * @param tableName
     * @return
     */
    public static synchronized int getNewSequenceId(String tableName) {
        int id = 0;
        String queryUpdate = "UPDATE " + tableName + " SET next_val = (next_val + 1) WHERE id=1";
        String queryForPostId = "SELECT next_val FROM " + tableName;
        log4jLog.info("queryUpdate: " + queryUpdate);
        FieldSenseUtils fieldSenseUtils = ((FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils"));

        try {
            if (fieldSenseUtils.getJdbcTemplate().update(queryUpdate) > 0) {
                log4jLog.info("Sequence is updated");
                try {
                    log4jLog.info("queryForPostId: " + queryForPostId);
                    id = fieldSenseUtils.getJdbcTemplate().queryForObject(queryForPostId, Integer.class);

                } catch (Exception e) {
                    id = 0;
//                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            id = 0;
        }
        return id;
    }
}
