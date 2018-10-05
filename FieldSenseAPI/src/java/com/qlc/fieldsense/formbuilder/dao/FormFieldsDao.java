/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.formbuilder.dao;

import com.qlc.fieldsense.formbuilder.model.FormFields;
import java.util.ArrayList;
import java.util.List;

;

/**
 *
 * @author pallavi.s
 */
public interface FormFieldsDao {

    /**
     *
     * @param formfields
     * @param accountId
     * @param formid
     * @return
     */
    public int insertFields (ArrayList<FormFields> formfields, int accountId, int formid);

    /**
     *
     * @param formid
     * @param accountId
     * @return
     */
    public boolean isFieldsValid(int formid, int accountId);

    /**
     *
     * @param formId
     * @param accountId
     * @return
     */
    public boolean deleteFields(int formId, int accountId);

    /**
     *
     * @param formfields
     * @param deletefields
     * @param accountId
     * @param formid
     * @return
     */
    public boolean updateFields(ArrayList<FormFields> formfields,ArrayList<Integer> deletefields, int accountId,int formid);

    /**
     *
     * @param formfields
     * @param accountId
     * @param formid
     * @return
     */
    public int insertField (FormFields formfields, int accountId, int formid);
    //public List<Integer> getFieldId(int formId, int accountId);
}
