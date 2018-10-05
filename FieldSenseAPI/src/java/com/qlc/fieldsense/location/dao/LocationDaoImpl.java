/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.location.dao;

import com.qlc.fieldsense.location.model.Location;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Ramesh
 * @date 06-03-2014
 */
public class LocationDaoImpl implements LocationDao {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("LocationDaoImpl");

    /**
     *
     * @param location
     * @param accountId
     * @return
     */
    @Override
    public int insertlocation(Location location, int accountId) {
        String query = "INSERT INTO location(latitude, longitude, location_type_id_fk, created_on,user_id_fk) VALUES ( ?, ?, ?, now(),?)";
        Object param[] = new Object[]{location.getLatitude(), location.getLangitude(), location.getLocationType(), location.getUserId()};
        log4jLog.info(" insertlocation " + query);
        try {
            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    return FieldSenseUtils.getMaxIdForTable("location", accountId);
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            log4jLog.info(" insertlocation " + e);
//            e.printStackTrace();
            return 0;
        }
    }

    /**
     *
     * @param userId
     * @param locationId
     * @param accountId
     * @return
     */
    @Override
    public int insertLocationMap(int userId, int locationId, int accountId) {
        String query = "INSERT INTO location_map(user_id_fk, location_id_fk, created_on) VALUES (?, ?, now())";
        log4jLog.info(" insertLocationMap " + query);
        Object param[] = new Object[]{userId, locationId};
        try {
            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    return FieldSenseUtils.getMaxIdForTable("location_map", accountId);
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            log4jLog.info(" insertLocationMap " + e);
//            e.printStackTrace();
            return 0;
        }
    }

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    @Override
    public List<Location> selectUserLocation(int userId, int accountId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @param teamId
     * @param accountId
     * @return
     */
    @Override
    public List<Location> teamUserLocations(int teamId, int accountId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @param location
     * @param accountId
     * @return
     */
    @Override
    public boolean isLocationAvailable(Location location, int accountId) {
        String query = "SELECT COUNT(id) FROM location WHERE user_id_fk=? AND location_type_id_fk=?";
        log4jLog.info(" isLocationAvailable " + query);
        Object param[] = new Object[]{location.getUserId(), location.getLocationType()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isLocationAvailable " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param locationTypeId
     * @param accountId
     * @return
     */
    @Override
    public boolean isLocationAvailableOnlyWithLocationTypeId(int locationTypeId, int accountId) {
        String query = "SELECT COUNT(id) FROM location WHERE location_type_id_fk=?";
        log4jLog.info(" isLocationAvailableOnlyWithLocationTypeId " + query);
        Object param[] = new Object[]{locationTypeId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isLocationAvailableOnlyWithLocationTypeId " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param location
     * @param accountId
     * @return
     */
    @Override
    public boolean updateUserLocation(Location location, int accountId) {
        String query = "Update location set latitude=?,longitude=? WHERE user_id_fk=? AND location_type_id_fk=?";
        log4jLog.info(" updateUserLocation " + query);
        Object param[] = new Object[]{location.getLatitude(), location.getLangitude(), location.getUserId(), location.getLocationType()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateUserLocation " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param location
     * @param accountId
     * @return
     */
    @Override
    public boolean updateUserLocationOnlyWithLocationTypeId(Location location, int accountId) {
        String query = "Update location set latitude=?,longitude=?,user_id_fk=? WHERE location_type_id_fk=?";
        log4jLog.info(" updateUserLocationOnlyWithLocationTypeId " + query);
        Object param[] = new Object[]{location.getLatitude(), location.getLangitude(), location.getUserId(), location.getLocationType()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateUserLocationOnlyWithLocationTypeId " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param customerId
     * @param accountId
     * @return
     */
    @Override
    public boolean isCustomerHasLocation(int customerId, int accountId) {
        String query = "SELECT COUNT(id) from location WHERE location_type_id_fk=?";
        log4jLog.info(" isCustomerHasLocation " + query);
        Object param[] = new Object[]{customerId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isCustomerHasLocation " + e);
//            e.printStackTrace();
            return false;
        }
    }
}
