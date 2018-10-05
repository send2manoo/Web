/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.industryCategory.dao;

import com.qlc.fieldsense.industryCategory.model.IndustryCategory;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;
/**
 *
 * @author awaneesh
 */
public interface IndustryCategoryDao {
    
    /**
     *
     * @param eCategory
     * @param accountId
     * @return
     */
    public int createIndustryCategory(IndustryCategory eCategory, int accountId);

    /**
     *
     * @param allRequestParams
     * @param accountId
     * @return
     */
    public List<IndustryCategory> selectAllIndustryCategoryWithOffset(@RequestParam Map<String,String> allRequestParams, int accountId);

    /**
     *
     * @param eCategoryId
     * @param accountId
     * @return
     */
    public IndustryCategory selectIndustryCategory(int eCategoryId, int accountId);

    /**
     *
     * @param eCategory
     * @param accountId
     * @return
     */
    public IndustryCategory updateIndustryCategory(IndustryCategory eCategory, int accountId);

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    public boolean deleteIndustryCategory(int id, int accountId);

    /**
     *
     * @param categoryName
     * @param accountId
     * @return
     */
    public boolean isIndustryCategoryAlreadyExists(String categoryName, int accountId);

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    public boolean isIndustryCategoryValid(int id, int accountId);

    /**
     *
     * @param eCategoryId
     * @param accountId
     * @return
     */
    public String getIndustryCategory(int eCategoryId, int accountId);
    
    /**
     *
     * @param accountId
     * @return
     */
    public  List<IndustryCategory>  selectAllActiveIndustryCategory(int accountId);
}
