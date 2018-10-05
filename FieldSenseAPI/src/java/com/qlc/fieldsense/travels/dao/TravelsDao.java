/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.travels.dao;

import com.qlc.fieldsense.travels.model.Travels;
import java.util.List;

/**
 *
 * @author anuja
 */
public interface TravelsDao {

    /**
     *
     * @param travels
     * @param accountId
     * @return
     */
    public int insertTravels(Travels travels, int accountId);

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    public Travels selectTravels(int id,int accountId);

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public List<Travels> selectTravelsList(Integer userId, int accountId);
}
