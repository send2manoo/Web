/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.external.dao;

/**
 * @date 17-april-2018
 * @author jyoti
 */
public interface ExternalDao {

    /**
     * @param userId
     * @added by jyoti, New Feature #29288
     * @param fromDate
     * @param toDate
     * @param requestParam
     * @param accountId
     * @return
     */
    java.util.List<com.qlc.fieldsense.external.model.ExternalAttendance> getAttendanceForUser(String fromDate, String toDate, int accountId, int userId, java.util.Map<String, String> requestParam);

    /**
     * @added by jyoti, New Feature #29288
     * @param fromDate
     * @param toDate
     * @param requestParam
     * @param accountId
     * @return
     */
    java.util.List<com.qlc.fieldsense.external.model.ExternalAttendance> getAttendanceForAllUser(String fromDate, String toDate, int accountId, java.util.Map<String, String> requestParam);

}
