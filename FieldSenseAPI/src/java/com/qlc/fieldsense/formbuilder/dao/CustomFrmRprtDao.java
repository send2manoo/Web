/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.formbuilder.dao;

import com.qlc.fieldsense.formbuilder.model.FilledForm;
import java.sql.Timestamp;
import java.util.*;

/**
 *
 * @author pallavi.s
 */
public interface CustomFrmRprtDao {

    /**
     *
     * @param formdetails
     * @param accountId
     * @return
     */
    public int insertFormData(FilledForm formdetails, int accountId);
    // public int updateCustomForm(FilledForm formdetails, int accountId);

    /**
     *
     * @param fieldData
     * @param formId
     * @param filledformid
     * @param accountId
     * @return
     */
    public boolean insertFieldsData(FilledForm fieldData, int formId, int filledformid, int accountId);

    /**
     *
     * @param formid
     * @param userId
     * @param fromDate
     * @param toDate
     * @param accountid
     * @return
     */
    public List<FilledForm> getFormData(int formid, int userId, Timestamp fromDate, Timestamp toDate, int accountid);

    /**
     *
     * @param formid
     * @param fromDate
     * @param toDate
     * @param accountid
     * @return
     */
    public List<FilledForm> getAllAdminFormData(int formid, Timestamp fromDate, Timestamp toDate, int accountid);

    /**
     *
     * @param formid
     * @param userId
     * @param fromDate
     * @param toDate
     * @param accountid
     * @return
     */
    public List<FilledForm> getAllUserFormData(int formid, int userId, Timestamp fromDate, Timestamp toDate, int accountid);

    /**
     *
     * @param formdetails
     * @param accountid
     * @return
     */
    public List<FilledForm> getFieldsData(List<FilledForm> formdetails, int accountid);

    /**
     *
     * @param formdetails
     * @param accountid
     * @return
     */
    public List<FilledForm> getAllAdminFieldsData(List<FilledForm> formdetails, int accountid);

    /**
     * @Added by jyoti
     * @param formId
     * @param accountid
     * @return
     */
    public List<String> getAllFormLabelList(int formId, int accountid);
    
    /**
     * @Added by jyoti
     * @param formId
     * @param accountid
     * @return
     */
    public String getCustomFormName(int formId, int accountid);

    /**
     *
     * @param formdetails
     * @param accountId
     * @return
     */
    public List<FilledForm> getAllUserFieldsData(List<FilledForm> formdetails, int accountId);

}
