/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.external.service;

import com.qlc.fieldsense.external.dao.ExternalDao;
import static com.qlc.fieldsense.external.dao.ExternalDaoImpl.log4jLog;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;

/**
 * @date 17-april-2018
 * @author jyoti
 */
public class ExternalManager {

    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
    ExternalDao externalDao = (ExternalDao) GetApplicationContext.ac.getBean("externalDaoImpl");

    /**
     * @param mobileNumber
     * @added by jyoti, New Feature #29288
     * @param fromDate
     * @param toDate
     * @param requestParam
     * @return
     */
    public Object getAttendanceForUser(String mobileNumber, String fromDate, String toDate, java.util.Map<String, String> requestParam) {
        String authToken = requestParam.get("authToken");
        int responseCode = getResponse(mobileNumber, fromDate, toDate, authToken);
        switch (responseCode) {
            case 0:
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Field can not be blank. Please try again. ", "", "");
            case 1:
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Please enter valid mobile number. ", "", "");
            case 2:
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Please enter proper date format for fromDate. example - 2017-12-31 ", "", "");
            case 3:
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Please enter proper date format for toDate. example - 2017-12-31 ", "", "");
            case 4:
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " fromDate can not be greater than toDate. ", "", "");
            case 5:

                if (fieldSenseUtils.isAuthTokenValid(authToken)) {

                    int auth_accountId = fieldSenseUtils.accountIdForAuthToken(authToken);

                    if (fieldSenseUtils.isMobileValid(mobileNumber)) {

                        java.util.HashMap userHashMap = fieldSenseUtils.getUserIdAccountIdBasedOnMobileNo(mobileNumber);

                        int userId = (Integer) userHashMap.get("userId");
                        int user_accountId = (Integer) userHashMap.get("accountId");

                        if (userId > 0) {
                            if (auth_accountId == user_accountId) {
                                String fromDateLocalTime = "", toDateLocalTime = "";
                                try {
                                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
//                                    java.util.Calendar calendar = java.util.Calendar.getInstance();
//                                    calendar.setTime(sdf.parse(fromDate));
//                                    calendar.add(java.util.Calendar.MINUTE, -330);  // to get time 5:30 hrs.
//                                    fromDateLocalTime = sdf.format(calendar.getTime());
//
//                                    calendar.setTime(sdf.parse(toDate));
//                                    calendar.add(java.util.Calendar.MINUTE, -330);  // to get time 5:30 hrs.
//                                    toDateLocalTime = sdf.format(calendar.getTime());
//                                    System.out.println("fromdate : " + fromDateLocalTime);
//                                    System.out.println("toDate : " + toDateLocalTime);
                                    java.util.Date fDate = sdf.parse(fromDate);
                                    java.util.Date tDate = sdf.parse(toDate);
                                    sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
                                    fromDateLocalTime = sdf.format(fDate);
                                    toDateLocalTime = sdf.format(tDate);
//                                    System.out.println("fDate : " + fDate);
//                                    System.out.println("tDate : " + tDate);

                                } catch (Exception e) {
//                                    e.printStackTrace();
                                    log4jLog.info(" getAttendanceForUser for userId :  " + userId + e);
                                }

                                java.util.List<com.qlc.fieldsense.external.model.ExternalAttendance> attendaceList = externalDao.getAttendanceForUser(fromDateLocalTime, toDateLocalTime, user_accountId, userId, requestParam);
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Attendace list.", attendaceList);

                            } else {
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " The given mobile number is registered with different company. Please contact customer care. ", "", "");
                            }
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Please enter valid mobile number. ", "", "");
                        }
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Please enter registered mobile number. ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid authentication token. Please try again. ", "", "");
                }
            default:
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Please enter proper date format. example : 2017-12-31 ", "", "");
        }
    }

    /**
     * @added by jyoti, New Feature #29288
     * @param fromDate
     * @param toDate
     * @param requestParam
     * @return
     */
    public Object getAttendanceForAllUser(String fromDate, String toDate, java.util.Map<String, String> requestParam) {

        String authToken = requestParam.get("authToken");
        int responseCode = getResponse("NOMOBILE_ALLUSER", fromDate, toDate, authToken);
        switch (responseCode) {
            case 0:
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Field can not be blank. Please try again. ", "", "");
            case 2:
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Please enter proper date format for fromDate. example - 2017-12-31 ", "", "");
            case 3:
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Please enter proper date format for toDate. example - 2017-12-31 ", "", "");
            case 4:
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " fromDate can not be greater than toDate. ", "", "");
            case 5:

                if (fieldSenseUtils.isAuthTokenValid(authToken)) {

                    int auth_accountId = fieldSenseUtils.accountIdForAuthToken(authToken);

                    String fromDateLocalTime = "", toDateLocalTime = "";
                    try {
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                        java.util.Date fDate = sdf.parse(fromDate);
                        java.util.Date tDate = sdf.parse(toDate);
                        sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
                        fromDateLocalTime = sdf.format(fDate);
                        toDateLocalTime = sdf.format(tDate);
//                        System.out.println("fDate : " + fDate);
//                        System.out.println("tDate : " + tDate);

                    } catch (Exception e) {
//                        e.printStackTrace();
                        log4jLog.info(" getAttendanceForAllUser for authToken :  " + authToken + e);
                    }

                    java.util.List<com.qlc.fieldsense.external.model.ExternalAttendance> attendaceList = externalDao.getAttendanceForAllUser(fromDateLocalTime, toDateLocalTime, auth_accountId, requestParam);
                    int totalRecord = 0;
                    if (!attendaceList.isEmpty()) {
                        totalRecord = attendaceList.get(0).getTotalrecords();
                    }
                    return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Attendace report list .", attendaceList, totalRecord);

                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid authentication token. Please try again. ", "", "");
                }
            default:
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Please enter proper date format. example : 2017-12-31 ", "", "");
        }
    }

    /**
     * @added by jyoti, 18-apr-2018
     * @param date
     * @return
     */
    public boolean validateDate(String date) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (java.text.ParseException ex) {
//            ex.printStackTrace();
            return false;
        }
    }

    /**
     * @added by jyoti, 18-apr-2018
     * @param mobileNumber
     * @return
     */
    public boolean isValidMobileNumber(String mobileNumber) {
        if (mobileNumber == null) {
            return false;
        }
        //validate phone numbers of format "1234567890"
        if (mobileNumber.matches("\\d{10}")) {
            return true;
        } //validating phone number with -, . or spaces
        else if (mobileNumber.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) {
            return true;
        } //validating phone number with extension length from 3 to 5
        else if (mobileNumber.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) {
            return true;
        } //validating phone number where area code is in braces ()
        else if (mobileNumber.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) {
            return true;
        } //return false if nothing matches the input
        else {
            return false;
        }
    }

    /**
     * @param mobileNumber
     * @param fromDate
     * @param authToken
     * @param toDate
     * @added by jyoti, error message for respective validation
     * @return
     */
    public int getResponse(String mobileNumber, String fromDate, String toDate, String authToken) {
        try {
            if (mobileNumber.equals("NOMOBILE_ALLUSER")) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                java.util.Date fDate = sdf.parse(fromDate);
                java.util.Date tDate = sdf.parse(toDate);

                if (fromDate.isEmpty() || toDate.isEmpty() || authToken.isEmpty()) {
                    return 0;
                } else if (!validateDate(fromDate)) {
                    return 2;
                } else if (!validateDate(toDate)) {
                    return 3;
                } else if (fDate.compareTo(tDate) > 0) {
                    return 4;
                } else {
                    return 5; // all OK
                }
            } else {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                java.util.Date fDate = sdf.parse(fromDate);
                java.util.Date tDate = sdf.parse(toDate);

                if (mobileNumber.isEmpty() || fromDate.isEmpty() || toDate.isEmpty() || authToken.isEmpty()) {
                    return 0;
                } else if (!isValidMobileNumber(mobileNumber)) {
                    return 1;
                } else if (!validateDate(fromDate)) {
                    return 2;
                } else if (!validateDate(toDate)) {
                    return 3;
                } else if (fDate.compareTo(tDate) > 0) {
                    return 4;
                } else {
                    return 5; // all OK
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            log4jLog.info(" getResponse for authToken :  " + authToken + e);
            return -1;
        }
    }

}
