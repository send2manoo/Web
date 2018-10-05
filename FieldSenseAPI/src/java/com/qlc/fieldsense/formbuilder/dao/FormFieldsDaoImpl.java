/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.formbuilder.dao;

import static com.qlc.fieldsense.customer.dao.CustomerDaoImpl.log4jLog;
import com.qlc.fieldsense.formbuilder.model.CustomForm;
import com.qlc.fieldsense.formbuilder.model.FormFields;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author pallavi.s
 */

public class FormFieldsDaoImpl implements FormFieldsDao {

   // private Object formdetails;
    
    /**
     *
     * @param formfields
     * @param accountId
     * @param formid
     * @return
     */
        
     @Override
    public int insertFields(ArrayList<FormFields> formfields, int accountId, int formid) {
        Iterator <FormFields> itr=formfields.iterator();  
        while(itr.hasNext()){  
            FormFields fieldDetails=itr.next();
        //  System.out.println("siddheshId"+fieldDetails.getListIdFK());
            String query = "INSERT INTO custom_forms_fields (label,type,placeholder,mandatory,editable,field_options,field_order,form_id_fk,geo_tag_enable,list_id_fk) VALUES (?,?,?,?,?,?,?,?,?,?)";
            log4jLog.info(" insertFields " + query);
            Object param[] = new Object[]{ fieldDetails.getLabelName(), fieldDetails.getFieldType(), fieldDetails.getPlaceHolder(), fieldDetails.getMandField(), fieldDetails.getEditField(),fieldDetails.getOption(),fieldDetails.getFieldOrder(),formid,fieldDetails.getGeoTagEnable(),fieldDetails.getListIdFK()};
            try {
                synchronized (this) {
                    if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                        log4jLog.info("FormFields inserted successfully" );
                    } 
                }
            } catch (Exception e) {
                log4jLog.info(" insertFormFields " + e);
//                e.printStackTrace();
                return 0;
            }
        }
              return FieldSenseUtils.getMaxIdForTable("formfields", accountId);
    }
    
    /**
     *
     * @param fieldsId
     * @param accountId
     * @return
     */
    @Override
    public boolean deleteFields(int fieldsId, int accountId) {
        String query = "DELETE FROM custom_forms_fields WHERE id=?";
        log4jLog.info(" deleteFields " + query);
        Object param[] = new Object[]{fieldsId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" deleteFields " + e);
//            e.printStackTrace();
            return false;
        }
}
    
    /**
     *
     * @param formid
     * @param accountId
     * @return
     */
    @Override
    public boolean isFieldsValid(int formid, int accountId) {
        String query = "SELECT id FROM custom_forms_fields WHERE id=?";
        log4jLog.info(" isFieldsValid " + query);
        Object[] param = new Object[]{formid};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) == formid) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isFieldsValid " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param formfields
     * @param deletedfields
     * @param accountId
     * @param formid
     * @return
     */
    public boolean updateFields(ArrayList<FormFields> formfields,ArrayList<Integer> deletedfields, int accountId, int formid)
    {
      Iterator <FormFields>itr = formfields.iterator();
        while(itr.hasNext())
        {
         FormFields fieldsdetails=itr.next();
        int fieldId= fieldsdetails.getFieldId();
        if(fieldId==0)
        {
            insertField(fieldsdetails,accountId,formid);
        }
        else if(isFieldsValid(fieldId,accountId)) {
           String query = "Update custom_forms_fields set label=?,type=?,placeholder=?,mandatory=?,editable=?,field_options=?,field_order=?,geo_tag_enable=?,list_id_fk=? where id=?";
        log4jLog.info(" insertFields " + query);
         Object param[] = new Object[]{fieldsdetails.getLabelName(), fieldsdetails.getFieldType(), fieldsdetails.getPlaceHolder(), fieldsdetails.getMandField(), fieldsdetails.getEditField(),fieldsdetails.getOption(),fieldsdetails.getFieldOrder(),fieldsdetails.getGeoTagEnable(),fieldsdetails.getListIdFK(),fieldId};
        try {
        
            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    log4jLog.info("FormFields inserted successfully" );
                } 
            }
        } catch (Exception e) {
            log4jLog.info(" insertFormFields " + e);
//            e.printStackTrace();
            return false;
        }
            
        }
        }
         Iterator <Integer> itr1 = deletedfields.iterator();
        while(itr1.hasNext())
        {
         Integer fieldid=itr1.next();
         deleteFields(fieldid,accountId);
        }
        return true;
 }
  
    /**
     *
     * @param formfields
     * @param accountId
     * @param formid
     * @return
     */
    public int insertField(FormFields formfields, int accountId, int formid) {
        String query = "INSERT INTO custom_forms_fields (label,type,placeholder,mandatory,editable,field_options,field_order,form_id_fk,geo_tag_enable,list_id_fk) VALUES (?,?,?,?,?,?,?,?,?,?)";
        log4jLog.info(" insertFields " + query);
        Object param[] = new Object[]{ formfields.getLabelName(), formfields.getFieldType(), formfields.getPlaceHolder(), formfields.getMandField(), formfields.getEditField(),formfields.getOption(),formfields.getFieldOrder(),formid,formfields.getGeoTagEnable(),formfields.getListIdFK()};
        try {
        
            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    log4jLog.info("FormFields inserted successfully" );
                } 
            }
        } catch (Exception e) {
            log4jLog.info(" insertFormFields " + e);
            e.printStackTrace();
            return 0;
        }
    
            return 1;
 }
}