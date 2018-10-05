/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.external.dao;

import com.qlc.fieldsense.external.model.ExternalAttendance;

/**
 * @date 17-april-2018
 * @author jyoti
 */
public class ExternalDaoImpl implements ExternalDao {

    public static final org.apache.log4j.Logger log4jLog = org.apache.log4j.Logger.getLogger("ExternalDaoImpl");

    @Override
    public java.util.List<com.qlc.fieldsense.external.model.ExternalAttendance> getAttendanceForUser(String fromDate, String toDate, int accountId, int userId, java.util.Map<String, String> requestParam) {

        StringBuilder query = new StringBuilder();
        query.append("SELECT a.user_id_fk, DATE(CONVERT_TZ(a.punch_date,'+00:00','+5:30')) AS punch_date, TIME(CONVERT_TZ(CONCAT(a.punch_date,' ', a.punch_in),'+00:00','+5:30')) AS punch_in, a.punch_in_location, TIME(CONVERT_TZ(CONCAT(a.punch_out_date,' ', a.punch_out),'+00:00','+5:30')) AS punch_out, a.punch_out_location, DATE(CONVERT_TZ(a.punch_out_date,'+00:00','+5:30')) AS punch_out_date, ");
        query.append(" a.punch_in_location_reason, a.punch_out_location_reason, CONCAT_WS(' ',u.first_name,u.last_name) ");
        query.append(" AS userName, u.emp_code AS emp_code ");
        query.append(" , (SELECT SUM( TIME_TO_SEC( TIME(CONVERT_TZ(CONCAT(timeout.timeout_date,' ', timeout.interval_time),'+00:00','+5:30')) ) )  ");
        query.append(" FROM attendances_timeout  timeout WHERE timeout.user_id_fk = ? AND DATE(CONVERT_TZ(timeout.timeout_date,'+00:00','+5:30')) >= DATE(CONVERT_TZ(a.punch_date,'+00:00','+5:30'))) AS time_out_sum  ");
        query.append(" FROM attendances a INNER JOIN fieldsense.users u ON u.id = a.user_id_fk WHERE a.user_id_fk = ? ");
        query.append(" AND DATE(CONVERT_TZ(CONCAT(a.punch_date,' ', a.punch_in),'+00:00','+5:30')) BETWEEN ? AND ? ORDER BY a.punch_date ");

//        System.out.println("query : " + query);

        Object param[] = new Object[]{userId, userId, fromDate, toDate};

        try {
            return com.qlc.fieldsense.utils.FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new org.springframework.jdbc.core.RowMapper<com.qlc.fieldsense.external.model.ExternalAttendance>() {

                @Override
                public com.qlc.fieldsense.external.model.ExternalAttendance mapRow(java.sql.ResultSet rs, int i) throws java.sql.SQLException {

                    com.qlc.fieldsense.external.model.ExternalAttendance attendance = new com.qlc.fieldsense.external.model.ExternalAttendance();

                    attendance.setPunchDate(rs.getString("punch_date"));
                    attendance.setPunchInTime(rs.getString("punch_in"));
                    attendance.setPunchInLocation(rs.getString("punch_in_location"));
                    attendance.setPunchOutDate(rs.getString("punch_out_date"));
                    attendance.setPunchOutTime(rs.getString("punch_out"));
                    attendance.setPunchOutLocation(rs.getString("punch_out_location"));
                    attendance.setPunchInLocationNotFoundReason(rs.getString("punch_in_location_reason"));
                    attendance.setPunchOutLocationNotFoundReason(rs.getString("punch_out_location_reason"));
                    attendance.setUserName(rs.getString("userName"));
                    attendance.setEmp_code(rs.getString("emp_code"));
                    Long intvl = rs.getLong("time_out_sum");
                    attendance.setTotalTimeOut(fromSecToStringTime(intvl));
                    if (!(attendance.getPunchOutTime().equals("00:00:00") && attendance.getPunchOutDate().equals("1111-11-11"))) {
                        attendance.setWorkHr(calculateWorkHr(attendance.getPunchDate(), attendance.getPunchInTime(), attendance.getPunchOutDate(), attendance.getPunchOutTime()));
                    }

                    return attendance;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" getAttendanceForUser for userId :  " + userId + e);
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    @Override
    public java.util.List<ExternalAttendance> getAttendanceForAllUser(String fromDate, String toDate, int accountId, java.util.Map<String, String> requestParams) {

        StringBuilder query = new StringBuilder();
        query.append("SELECT u.emp_code AS emp_code, a.user_id_fk, DATE(CONVERT_TZ(a.punch_date,'+00:00','+5:30')) AS punch_date, TIME(CONVERT_TZ(CONCAT(a.punch_date,' ', a.punch_in),'+00:00','+5:30')) AS punch_in, a.punch_in_location, TIME(CONVERT_TZ(CONCAT(a.punch_out_date,' ', a.punch_out),'+00:00','+5:30')) AS punch_out, a.punch_out_location, DATE(CONVERT_TZ(a.punch_out_date,'+00:00','+5:30')) AS punch_out_date, ");
        query.append(" CONCAT_WS(' ',u.first_name,u.last_name) AS userName, a.punch_in_location_reason, a.punch_out_location_reason,");
        query.append(" (SELECT COUNT(k.id) FROM attendances k INNER JOIN fieldsense.users f ON k.user_id_fk = f.id WHERE DATE(CONVERT_TZ(a.punch_date,'+00:00','+5:30')) ");
        query.append(" BETWEEN ? AND ? ) AS totalrecords, (SELECT SUM( TIME_TO_SEC( TIME(CONVERT_TZ(CONCAT(timeout.timeout_date,' ', timeout.interval_time),'+00:00','+5:30')) )  )  FROM attendances_timeout timeout ");
        query.append(" WHERE  DATE(CONVERT_TZ(timeout.timeout_date,'+00:00','+5:30')) >= DATE(CONVERT_TZ(a.punch_date,'+00:00','+5:30')) AND a.user_id_fk = timeout.user_id_fk) AS time_out_sum ");
        query.append(" FROM attendances a INNER JOIN fieldsense.users u ON a.user_id_fk = u.id WHERE DATE(CONVERT_TZ(CONCAT(a.punch_date,' ', a.punch_in),'+00:00','+5:30'))  BETWEEN ? AND ? ");
        query.append(" ORDER BY punch_out_date DESC");

//        System.out.println("query : " + query);

        Object param[] = new Object[]{fromDate, toDate, fromDate, toDate};

        try {
            return com.qlc.fieldsense.utils.FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new org.springframework.jdbc.core.RowMapper<com.qlc.fieldsense.external.model.ExternalAttendance>() {

                @Override
                public com.qlc.fieldsense.external.model.ExternalAttendance mapRow(java.sql.ResultSet rs, int i) throws java.sql.SQLException {

                    com.qlc.fieldsense.external.model.ExternalAttendance attendance = new com.qlc.fieldsense.external.model.ExternalAttendance();

                    attendance.setPunchDate(rs.getString("punch_date"));
                    attendance.setPunchInTime(rs.getString("punch_in"));
                    attendance.setPunchInLocation(rs.getString("punch_in_location"));
                    attendance.setPunchOutDate(rs.getString("punch_out_date"));
                    attendance.setPunchOutTime(rs.getString("punch_out"));
                    attendance.setPunchOutLocation(rs.getString("punch_out_location"));
                    attendance.setPunchInLocationNotFoundReason(rs.getString("punch_in_location_reason"));
                    attendance.setPunchOutLocationNotFoundReason(rs.getString("punch_out_location_reason"));
                    attendance.setUserName(rs.getString("userName"));
                    attendance.setEmp_code(rs.getString("emp_code"));
                    Long intvl = rs.getLong("time_out_sum");
                    attendance.setTotalTimeOut(fromSecToStringTime(intvl));
                    if (!(attendance.getPunchOutTime().equals("00:00:00") && attendance.getPunchOutDate().equals("1111-11-11"))) {
                        attendance.setWorkHr(calculateWorkHr(attendance.getPunchDate(), attendance.getPunchInTime(), attendance.getPunchOutDate(), attendance.getPunchOutTime()));
                    }
                    attendance.setTotalrecords(Integer.parseInt(rs.getString("totalrecords")));

                    return attendance;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" getAttendanceForAllUser for accountId : " + accountId + e);
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }

    }

    private String fromSecToStringTime(long seconds) {
        boolean isNegative = false;
        if (seconds < 0) {
            isNegative = true;
            seconds = (-1 * seconds);
        }
        long hh1 = (seconds / 3600);
        long mm1 = (seconds % 3600) / 60;
        long ss1 = (seconds % 3600) % 60;
        String hh2;
        String mm2;
        String ss2;

        if (hh1 < 10) {
            hh2 = "0" + hh1;
        } else {
            hh2 = "" + hh1;
        }
        if (mm1 < 10) {
            mm2 = "0" + mm1;
        } else {
            mm2 = "" + mm1;
        }
        if (ss1 < 10) {
            ss2 = "0" + ss1;
        } else {
            ss2 = "" + ss1;
        }

        if (isNegative) {
            return "-" + hh2 + ":" + mm2 + ":" + ss2;
        } else {
            return hh2 + ":" + mm2 + ":" + ss2;
        }
    }

    public String calculateWorkHr(String punchInDate, String punchInTime, String punchOutDate, String punchOutTime) {
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        try {
            java.sql.Timestamp punchInTimestamp = java.sql.Timestamp.valueOf(punchInDate + " " + punchInTime);
            java.sql.Timestamp punchOutTimestamp = java.sql.Timestamp.valueOf(punchOutDate + " " + punchOutTime);
            long punchInMills = punchInTimestamp.getTime();
            long punchOutMills = punchOutTimestamp.getTime();
            long diff = punchOutMills - punchInMills;
            diff = (diff / ((60) * (1000)));
            String hours = String.valueOf(diff / 60);
            String mins = String.valueOf(diff % 60);
            if ((diff / 60) < 10) {
                hours = "0" + hours;
            }
            if (((diff % 60)) < 10) {
                mins = "0" + mins;
            }
            return hours + ":" + mins;
        } catch (Exception e) {
            log4jLog.info("calculateWorkHr" + e);
            return "-";
        }
    }

}
