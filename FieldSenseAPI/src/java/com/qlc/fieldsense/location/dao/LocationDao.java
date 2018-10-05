/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.location.dao;

import com.qlc.fieldsense.location.model.Location;
import java.util.List;

/**
 *
 * @author Ramesh
 * @date 06-03-2014
 * @purpose This interface contains all location related abstract method .
 */
public interface LocationDao {

    /**
     *
     * @param location
     * @param accountId
     * @return
     */
    public int insertlocation(Location location, int accountId);

    /**
     *
     * @param userId
     * @param locationId
     * @param accountId
     * @return
     */
    public int insertLocationMap(int userId, int locationId, int accountId);

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public List<Location> selectUserLocation(int userId, int accountId);

    /**
     *
     * @param teamId
     * @param accountId
     * @return
     */
    public List<Location> teamUserLocations(int teamId, int accountId);

    /**
     *
     * @param location
     * @param accountId
     * @return
     */
    public boolean isLocationAvailable(Location location, int accountId);

    /**
     *
     * @param location
     * @param accountId
     * @return
     */
    public boolean updateUserLocation(Location location, int accountId);

    /**
     *
     * @param customerId
     * @param accountId
     * @return
     */
    public boolean isCustomerHasLocation(int customerId, int accountId);

    /**
     *
     * @param locationTypeId
     * @param accountId
     * @return
     */
    public boolean isLocationAvailableOnlyWithLocationTypeId(int locationTypeId, int accountId);

    /**
     *
     * @param location
     * @param accountId
     * @return
     */
    public boolean updateUserLocationOnlyWithLocationTypeId(Location location, int accountId);
}
