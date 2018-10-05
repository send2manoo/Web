/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.territoryCategory.dao;

import com.qlc.fieldsense.territoryCategory.model.TerritoryCategory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author awaneesh
 */
public interface TerritoryCategoryDao {
    
    /**
     *
     * @param eCategory
     * @param accountId
     * @return
     */
    public boolean createTerritoryCategory(TerritoryCategory eCategory, int accountId);

    /**
     *
     * @param allRequestParams
     * @param accountId
     * @return
     */
    public List<TerritoryCategory> selectAllTerritoryCategoryWithOffset(@RequestParam Map<String,String> allRequestParams, int accountId);
    
    /**
     *
     * @param accountId
     * @return
     */
    public List<TerritoryCategory> selectAllTerritoryCategory(int accountId);
    
    /**
     *
     * @param parentPosCsv
     * @param accountId
     * @return
     */
    public List<TerritoryCategory>selectAllParentTerritoryCategory(String parentPosCsv,int accountId);

    /**
     *
     * @param eCategoryId
     * @param accountId
     * @return
     */
    public TerritoryCategory selectTerritoryCategory(int eCategoryId, int accountId);

    /**
     *
     * @param eCategory
     * @param oldPosCsv
     * @param oldCatName
     * @param oldIsActive
     * @param accountId
     * @return
     */
    public boolean updateTerritoryCategory(TerritoryCategory eCategory,String oldPosCsv, String oldCatName,boolean oldIsActive,int accountId);

    /**
     *
     * @param id
     * @param parentPosCsv
     * @param categoryName
     * @param accountId
     * @return
     */
    public boolean deleteTerritoryCategory(int id,String parentPosCsv,String categoryName, int accountId);

    /**
     *
     * @param categoryName
     * @param accountId
     * @return
     */
    public boolean isTerritoryCategoryAlreadyExists(String categoryName, int accountId);

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    public boolean isTerritoryCategoryValid(int id, int accountId);

    /**
     *
     * @param eCategoryId
     * @param accountId
     * @return
     */
    public String getTerritoryCategory(int eCategoryId, int accountId);
    
    /**
     *
     * @param accountId
     * @return
     */
    public  List<TerritoryCategory>  selectAllActiveTerritoryCategory(int accountId);
    
    /**
     *
     * @param accountId
     * @return
     */
    public  List<TerritoryCategory>  selectAllActiveTerritoryCategories(int accountId);
    
    /**
     *
     * @param accountId
     * @return
     */
    public  List<HashMap>  selectAllTerritoryCategoriesForMobile(int accountId);
    
    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public  List<TerritoryCategory>  selectAllActiveTerritoryCategories(int userId,int accountId);
    
    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public  List<TerritoryCategory>  selectAllUserActiveTerritoryCategories(int userId,int accountId);
    
    /**
     *
     * @param eCategory
     * @param accountId
     * @return
     */
    public TerritoryCategory selectParentCSVID(TerritoryCategory eCategory, int accountId);    // Added by Jyoti, 02-March-2017
    
}
