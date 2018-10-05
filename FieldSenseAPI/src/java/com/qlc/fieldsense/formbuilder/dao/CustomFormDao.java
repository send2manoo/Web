/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.formbuilder.dao;

import com.qlc.fieldsense.customer.model.Customer;
import com.qlc.fieldsense.formbuilder.model.CustomForm;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

/**
 *
 * @author pallavi.s
 */
public interface CustomFormDao {
    
    /**
     *
     * @param formdetails
     * @param accountId
     * @return
     */
    public boolean insertForm(CustomForm formdetails, int accountId);

    /**
     *
     * @param formid
     * @param accountId
     * @return
     */
    public boolean isFormValid(int formid, int accountId);

    /**
     *
     * @param formId
     * @param accountId
     * @return
     */
    public boolean deleteCustomForm(int formId, int accountId);

    /**
     *
     * @param formdetails
     * @param accountId
     * @return
     */
    public int updateCustomForm(CustomForm formdetails, int accountId);

    /**
     *
     * @param accountid
     * @return
     */
    public List<CustomForm>  getAllActiveFormsDetails(int accountid);
    
    /**
     * @Added by jyoti
     * @param accountid
     * @return 
     */
    public List<CustomForm>  getAllFormsForMobileCustForms(int formId, int accountid);

    /**
     *
     * @param accountid
     * @return
     */
    public List<CustomForm>  getAllFormDetails(int accountid);

    /**
     *
     * @param accountid
     * @return
     */
    public List<CustomForm>  getAllActiveWebFormDetails(int accountid);

    /**
     *
     * @param formid
     * @param accountid
     * @return
     */
    public CustomForm getFormDetails(int formid,int accountid);

    /**
     *
     * @param accountid
     * @return
     */
    public boolean updateFormSettingDateTime(int accountid);

    /**
     *
     * @param accountid
     * @return
     */
    public Timestamp getFormsLastUpdatedOn(int accountid); 

    /**
     *
     * @param formdetails
     * @param accountid
     * @return
     */
    public List<Map> getUpdatedForms(CustomForm[] formdetails,int accountid);
    
    public List<Map> getUpdatedAllForms(CustomForm[] formdetails,String timeStamp,int accountid);

    /**
     *
     * @param accountid
     * @return
     */
    public List<Integer> getAllFormIds(int accountid);
    
    /**
     * @Added by jyoti, 23-02-2018
     * @param formId
     * @param accountId
     * @return 
     */
    public CustomForm getFormDetailsForWeb(int formId, int accountId);
}
