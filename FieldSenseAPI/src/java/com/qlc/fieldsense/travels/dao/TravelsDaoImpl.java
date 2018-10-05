/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.travels.dao;

import com.qlc.fieldsense.travels.model.Travels;
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
public class TravelsDaoImpl implements TravelsDao {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("TravelsDaoImpl");

    /**
     *
     * @param travels
     * @param accountId
     * @return
     */
    public int insertTravels(Travels travels, int accountId) {
        String query = "INSERT INTO travels(user_id_fk,travel_started_on,start_location,start_location_latlong,travel_ended_on,ended_location,ended_location_latlong,mobile_number,imei_number,created_on) VALUES(?,?,?,?,?,?,?,?,?,now())";
        Object param[] = new Object[]{travels.getUserId(), travels.getStartedOn(), travels.getStartLocation(), travels.getStartLocationLatlong(), travels.getEndedOn(), travels.getEndLocation(), travels.getEndLocationLatlong(), travels.getMobileNo(), travels.getImeiNo()};
        try {
            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    return FieldSenseUtils.getMaxIdForTable("travels", accountId);
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return 0;
        }
    }

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    public Travels selectTravels(int id,int accountId) {
        try {
            synchronized (this) {
                String query = "SELECT id,user_id_fk,travel_started_on,start_location,start_location_latlong,travel_ended_on,ended_location,ended_location_latlong,mobile_number,imei_number,created_on FROM travels WHERE id=?";
                Object[] param=new Object[]{id};
                return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query,param,new RowMapper<Travels>() {
                    public Travels mapRow(ResultSet rs, int i) throws SQLException {
                        Travels travels = new Travels();
                        travels.setId(rs.getInt("id"));
                        travels.setUserId(rs.getInt("user_id_fk"));
                        travels.setStartedOn(rs.getTimestamp("travel_started_on"));
                        travels.setStartLocation(rs.getString("start_location"));
                        travels.setStartLocationLatlong(rs.getString("start_location_latlong"));
                        travels.setEndedOn(rs.getTimestamp("travel_ended_on"));
                        travels.setEndLocation(rs.getString("ended_location"));
                        travels.setEndLocationLatlong(rs.getString("ended_location_latlong"));
                        travels.setMobileNo(rs.getInt("mobile_number"));
                        travels.setImeiNo(rs.getInt("imei_number"));
                        travels.setCreatedOn(rs.getTimestamp("created_on"));
                        return travels;
                    }
                });
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return new Travels();
        }
    }

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public List<Travels> selectTravelsList(Integer userId, int accountId) {
        try {
            String query = "SELECT id,user_id_fk,travel_started_on,start_location,start_location_latlong,travel_ended_on,ended_location,ended_location_latlong,mobile_number,imei_number,created_on FROM travels WHERE user_id_fk=?";
            Object param[] = new Object[]{userId};
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<Travels>() {
                public Travels mapRow(ResultSet rs, int i) throws SQLException {
                    Travels travels = new Travels();
                    travels.setId(rs.getInt("id"));
                    travels.setUserId(rs.getInt("user_id_fk"));
                    travels.setStartedOn(rs.getTimestamp("travel_started_on"));
                    travels.setStartLocation(rs.getString("start_location"));
                    travels.setStartLocationLatlong(rs.getString("start_location_latlong"));
                    travels.setEndedOn(rs.getTimestamp("travel_ended_on"));
                    travels.setEndLocation(rs.getString("ended_location"));
                    travels.setEndLocationLatlong(rs.getString("ended_location_latlong"));
                    travels.setMobileNo(rs.getInt("mobile_number"));
                    travels.setImeiNo(rs.getInt("imei_number"));
                    travels.setCreatedOn(rs.getTimestamp("created_on"));
                    return travels;
                }
            });
        } catch (Exception e) {
//            e.printStackTrace();
            return new ArrayList<Travels>();
        }
    }
}
