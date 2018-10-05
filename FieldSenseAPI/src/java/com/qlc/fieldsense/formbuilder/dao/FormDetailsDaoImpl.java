/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.formbuilder.dao;

import static com.qlc.fieldsense.customer.dao.CustomerDaoImpl.log4jLog;
import com.qlc.fieldsense.formbuilder.model.CustomForm;
import com.qlc.fieldsense.formbuilder.model.FormFields;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author pallavi.s
 */

public class FormDetailsDaoImpl implements CustomFormDao{
    
    /**
     *
     * @param formdetails
     * @param accountId
     * @return
     */
    @Override
    public boolean insertForm(CustomForm formdetails, int accountId) {
        String query = "INSERT INTO custom_forms (id,form_name,form_related_to,form_trigger_event,submit_caption,active,mobile_only,createdby_fk,createdon,updatedby_fk,updatedon) VALUES (?,?,?,?,?,?,?,?,now(),?,now())";
        log4jLog.info(" insertCustomer " + query);
        Object param[] = new Object[]{formdetails.getId(),formdetails.getFormName(),formdetails.getForm_related_to(),formdetails.getForm_trigerred_event(),formdetails.getSubmit_caption(),formdetails.getActive(),formdetails.getMobileOnly(),formdetails.getCreatedBy(),formdetails.getUpdatedBy()};
        try {
            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) 
                {
                   return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            log4jLog.info(" insertFormFields " + e);
//            e.printStackTrace();
            return false;
        
    }
    }
    
    /**
     *
     * @param formId
     * @param accountId
     * @return
     */
    @Override
    public boolean deleteCustomForm(int formId, int accountId) {
        String query = "DELETE FROM custom_forms WHERE id=?";
        log4jLog.info(" deleteCustomer " + query);
        Object param[] = new Object[]{formId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                query ="DELETE FROM custom_forms_fields where form_id_fk=?";
                FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) ; // delete from fields
                
                return true;
                
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" deleteCustomer " + e);
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
    public boolean isFormValid(int formid, int accountId) {
        String query = "SELECT id FROM custom_forms WHERE id=?";
        log4jLog.info(" isFormValid " + query);
        Object[] param = new Object[]{formid};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) == formid) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isFormValid " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param formdetails
     * @param accountId
     * @return
     */
    public int updateCustomForm(CustomForm formdetails, int accountId)
    {
        String query = "UPDATE custom_forms SET form_name=?,form_related_to=?,form_trigger_event=?,submit_caption=?,active=?,mobile_only=?,createdby_fk=?,createdon=now(),updatedby_fk=?,updatedon=now() where id=?";
        log4jLog.info(" updateCustomer " + query);
        Object param[] = new Object[]{formdetails.getFormName(),formdetails.getForm_related_to(),formdetails.getForm_trigerred_event(),formdetails.getSubmit_caption(),formdetails.getActive(),formdetails.getMobileOnly(),formdetails.getCreatedBy(),formdetails.getUpdatedBy(),formdetails.getId()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return formdetails.getId();
            } else {
                return 0;
            }
        } catch (Exception e) {
            log4jLog.info(" updateCustomer " + e);
            e.printStackTrace();
            return 0;
        }
    
    }
    
    /**
     *
     * @param accountId
     * @return
     */
    public List<CustomForm> getAllFormDetails(int accountId )
    {
      String query = "SELECT * from custom_forms order by form_name asc";
        log4jLog.info(" getFormNames " + query);       
        try {
                return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query,new RowMapper<CustomForm>(){
                    public CustomForm mapRow(ResultSet rs, int i) throws SQLException{

                       CustomForm custform= new CustomForm();
                       custform.setId((Integer)(rs.getInt("id")));
                       custform.setFormName((String)(rs.getString("form_name")));
                        custform.setForm_related_to((String)(rs.getString("form_related_to")));
                       custform.setForm_trigerred_event((String)(rs.getString("form_trigger_event")));
                       custform.setSubmit_caption((String)(rs.getString("submit_caption")));
                       custform.setActive((Integer)(rs.getInt("active")));
                       custform.setMobileOnly((Integer)(rs.getInt("mobile_only")));
                       custform.setCreatedBy(rs.getString("createdby_fk"));
                       custform.setCreatedOn(rs.getTimestamp("createdon"));
                       custform.setUpdatedBy(rs.getString("updatedby_fk"));
                       custform.setUpdatedOn(rs.getTimestamp("updatedon"));
                       return custform;
                    }
                });
             }
        catch (Exception e) {
            log4jLog.info(" getFormNames " + e);
//            e.printStackTrace();
            return new ArrayList<CustomForm>();
        }
    
    }
    
    /**
     *
     * @param accountId
     * @return
     */
    @Override
    public List<CustomForm> getAllActiveFormsDetails(int accountId )
    {
        String query = "SELECT * from custom_forms where active=1 order by form_name asc";
        log4jLog.info(" getFormNames " + query);       
        try {
               final int  accountid=accountId;
                return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query,new RowMapper<CustomForm>(){
                    public CustomForm mapRow(ResultSet rs, int i) throws SQLException{

                       CustomForm custform= new CustomForm();
                       //custform.setId((Integer)(rs.getInt("id")));
                       //
                       StringBuilder sb = new StringBuilder();
                       java.io.BufferedReader br =null;
                       String xmlFile="";
                       int formid=rs.getInt("id");
                       try{
                        xmlFile=Constant.CUSTOM_FORM_XML_UPLOAD_PATH+"account_"+accountid+"_form_"+formid+".xml";
                        br = new java.io.BufferedReader(new java.io.FileReader(xmlFile));
                        String line;
                        while((line=br.readLine())!= null){
                            sb.append(line.trim());
                        }
                        custform.setFormXml(sb.toString());
                        sb.setLength(0);
                       }catch(Exception e){
//                           System.out.println("Error got at logs");
                           e.printStackTrace();
                           custform.setFormXml("");                      
                       }
                       //
                       custform.setId((Integer)(formid));
                       custform.setFormName((String)(rs.getString("form_name")));
                        custform.setForm_related_to((String)(rs.getString("form_related_to")));
                       custform.setForm_trigerred_event((String)(rs.getString("form_trigger_event")));
                       custform.setSubmit_caption((String)(rs.getString("submit_caption")));
                       custform.setActive((Integer)(rs.getInt("active")));
                       custform.setMobileOnly((Integer)(rs.getInt("mobile_only")));
                       custform.setCreatedBy(rs.getString("createdby_fk"));
                       custform.setCreatedOn(rs.getTimestamp("createdon"));
                       custform.setUpdatedBy(rs.getString("updatedby_fk"));
                       custform.setUpdated_On(rs.getTimestamp("updatedon"));
                       return custform;
                    }
                });
             }
        catch (Exception e) {
            log4jLog.info(" getFormNames " + e);
//            e.printStackTrace();
            return new ArrayList<CustomForm>();
        }
    
    }
    
    /**
     * @param formId
     * @Added by jyoti
     * @param accountId
     * @return 
     */
    @Override
    public List<CustomForm> getAllFormsForMobileCustForms(int formId, int accountId )
    {
        String query = "SELECT * from custom_forms WHERE id = ? order by form_name asc";
        log4jLog.info(" getAllFormsForMobileCustForms for accountId : " + accountId);       
        try {
               final int  accountid=accountId;
               Object param[] = new Object[]{formId};
                return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<CustomForm>(){
                    public CustomForm mapRow(ResultSet rs, int i) throws SQLException{

                       CustomForm custform= new CustomForm();
                       //custform.setId((Integer)(rs.getInt("id")));
                       //
                       StringBuilder sb = new StringBuilder();
                       java.io.BufferedReader br =null;
                       String xmlFile="";
                       int formid=rs.getInt("id");
                       try{
                        xmlFile=Constant.CUSTOM_FORM_XML_UPLOAD_PATH+"account_"+accountid+"_form_"+formid+".xml";
                        br = new java.io.BufferedReader(new java.io.FileReader(xmlFile));
                        String line;
                        while((line=br.readLine())!= null){
                            sb.append(line.trim());
                        }
                        custform.setFormXml(sb.toString());
                        sb.setLength(0);
                       }catch(Exception e){
//                           System.out.println("Error got at logs");
                           e.printStackTrace();
                           custform.setFormXml("");                      
                       }
                       //
                       custform.setId((Integer)(formid));
                       custform.setFormName((String)(rs.getString("form_name")));
                        custform.setForm_related_to((String)(rs.getString("form_related_to")));
                       custform.setForm_trigerred_event((String)(rs.getString("form_trigger_event")));
                       custform.setSubmit_caption((String)(rs.getString("submit_caption")));
                       custform.setActive((Integer)(rs.getInt("active")));
                       custform.setMobileOnly((Integer)(rs.getInt("mobile_only")));
                       custform.setCreatedBy(rs.getString("createdby_fk"));
                       custform.setCreatedOn(rs.getTimestamp("createdon"));
                       custform.setUpdatedBy(rs.getString("updatedby_fk"));
                       custform.setUpdated_On(rs.getTimestamp("updatedon"));
                       return custform;
                    }
                });
             }
        catch (Exception e) {
            log4jLog.info(accountId+" accountid,  getAllFormsForMobileCustForms  " + e);
            e.printStackTrace();
            return new ArrayList<CustomForm>();
        }
    
    }
    /**
     *
     * @param accountId
     * @return
     */
    public List<CustomForm> getAllActiveWebFormDetails(int accountId )
    {
      String query = "SELECT * from custom_forms where active=1 and mobile_only=0 and form_trigger_event=1  order by form_name asc";
        log4jLog.info(" getFormNames " + query);       
        try {
                return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query,new RowMapper<CustomForm>(){
                    public CustomForm mapRow(ResultSet rs, int i) throws SQLException{

                       CustomForm custform= new CustomForm();
                       custform.setId((Integer)(rs.getInt("id")));
                       custform.setFormName((String)(rs.getString("form_name")));
                        custform.setForm_related_to((String)(rs.getString("form_related_to")));
                       custform.setForm_trigerred_event((String)(rs.getString("form_trigger_event")));
                       custform.setSubmit_caption((String)(rs.getString("submit_caption")));
                       custform.setActive((Integer)(rs.getInt("active")));
                       custform.setMobileOnly((Integer)(rs.getInt("mobile_only")));
                       custform.setCreatedBy(rs.getString("createdby_fk"));
                       custform.setCreatedOn(rs.getTimestamp("createdon"));
                       custform.setUpdatedBy(rs.getString("updatedby_fk"));
                       custform.setUpdatedOn(rs.getTimestamp("updatedon"));
                       return custform;
                    }
                });
             }
        catch (Exception e) {
            log4jLog.info(" getFormNames " + e);
//            e.printStackTrace();
            return new ArrayList<CustomForm>();
        }
    
    }
    
    /**
     *
     * @param formid
     * @param accountId
     * @return
     */
    public CustomForm getFormDetails(int formid,int accountId )
    {
//        System.out.println("getFormDetails, for formid : "+formid);
       StringBuilder query1 = new StringBuilder();
       /*query.append("SELECT cf.id,cf.form_name,cf.form_related_to,cf.form_trigger_event,cf.submit_caption,cf.active,cf.mobile_only,cf.createdby,cf.createdon,cf.updatedby,cf.updatedon");
       query.append(" ,ff.fieldId,ff.labelName,ff.fieldType,ff.placeHolder,ff.mandField,ff.editField,ff.fieldOptions,ff.fieldOrder,ff.form_id_fk"); 
        query.append(" from customForm as cf , formFields as ff where cf.id= ff.form_id_fk and cf.id=?");
        */
        query1.append("Select * from custom_forms where id=?");
        log4jLog.info(" updateCustomer " + query1.toString());  
        Object param[] = new Object[]{formid};
        CustomForm custform1= new CustomForm();
        try {
             custform1= FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query1.toString(), param, new RowMapper<CustomForm>(){
              //ArrayList<FormFields> ff=new ArrayList<FormFields>();
             public CustomForm mapRow(ResultSet rs, int i) throws SQLException{
                CustomForm custform= new CustomForm();
                //FormFields formfields =new FormFields();
                custform.setId((Integer)(rs.getInt("id")));
                custform.setFormName((String)(rs.getString("form_name")));
                custform.setForm_related_to((String)(rs.getString("form_related_to")));
                custform.setForm_trigerred_event((String)(rs.getString("form_trigger_event")));
                custform.setSubmit_caption((String)(rs.getString("submit_caption")));
                custform.setActive((Integer)(rs.getInt("active")));
                custform.setMobileOnly((Integer)(rs.getInt("mobile_only")));
               /* formfields.setFieldId((Integer)(rs.getInt("fieldId")));
                formfields.setLabelName((String)(rs.getString("labelName")));
                formfields.setFieldType((String)(rs.getString("fieldType")));
                formfields.setPlaceHolder((String)(rs.getString("placeHolder")));      ;
                formfields.setMandField((String)(rs.getString("mandField")));
                formfields.setEditField((String)(rs.getString("editField")));
                formfields.setOption((String)(rs.getString("fieldOptions")));
                formfields.setFieldOrder((String)(rs.getString("fieldOrder")));
                formfields.setForm_id_fk((String)(rs.getString("form_id_fk")));
                ff.add(formfields);
                custform.setTableData(ff);*/
                return custform;
                }
              });
             
             query1.setLength(0);
             query1.append("Select fields.id,fields.label,fields.type,fields.placeholder,fields.mandatory,fields.editable,IFNULL(fields.field_options,'') field_options,fields.field_order,fields.form_id_fk,fields.geo_tag_enable,fields.list_id_fk,list.list_name,list.list_type,IFNULL(list.options_list,'') options_list from custom_forms_fields as fields left join predifined_list_categories as list on fields.list_id_fk=list.id where fields.form_id_fk=? order by field_order");
             
             List <FormFields> fieldList = new ArrayList<FormFields>();
             fieldList= FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query1.toString(), param, new RowMapper<FormFields>(){
              //ArrayList<FormFields> ff=new ArrayList<FormFields>();
             public FormFields mapRow(ResultSet rs, int i) throws SQLException{
                FormFields formfields =new FormFields();
                
                formfields.setFieldId((Integer)(rs.getInt("id")));
                formfields.setLabelName((String)(rs.getString("label")));
                formfields.setFieldType((String)(rs.getString("type")));
                formfields.setPlaceHolder((String)(rs.getString("placeholder"))); 
                formfields.setMandField((Integer)(rs.getInt("mandatory")));
                formfields.setEditField((Integer)(rs.getInt("editable")));
                String check=rs.getString("field_options");
//                 System.out.println("check "+check);
                if(check.isEmpty() || check==null || check.equals(" ")){
                check=rs.getString("options_list");
                check=check.replace(",","\n");
//                    System.out.println("chedc "+check);
                }
                formfields.setOption((String)(check));
                formfields.setFieldOrder((Integer)(rs.getInt("field_order")));
                formfields.setForm_id_fk((Integer)(rs.getInt("form_id_fk")));
                formfields.setGeoTagEnable(rs.getInt("geo_tag_enable"));
                //editing for form data display
                formfields.setListType((String)rs.getString("list_type"));
                formfields.setListName((String)rs.getString("list_name"));
                formfields.setListIdFK(rs.getInt("list_id_fk"));
                 return formfields;
                }
              });
             
             custform1.setTableData((ArrayList<FormFields>)fieldList);
              //System.err.println("customeform"+custform1);
                return custform1;
}
        catch (Exception e) {
            log4jLog.info(" selectCustomFormDetails " + e);
            e.printStackTrace();
            return new CustomForm();
        }
       
    }
  
    
    /**
     * @Added by jyoti, 23-02-2018
     * @param formId
     * @param accountId
     * @return 
     */
    public CustomForm getFormDetailsForWeb(int formId, int accountId) {
        StringBuilder query1 = new StringBuilder();
        query1.append("SELECT * FROM custom_forms WHERE id = ?");
        log4jLog.info(" getFormDetailsForWeb " + formId);
        Object param[] = new Object[]{formId};
        try {
            CustomForm custform1 = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query1.toString(), param, new RowMapper<CustomForm>() {
                public CustomForm mapRow(ResultSet rs, int i) throws SQLException {
                    CustomForm custform = new CustomForm();
                    custform.setId((Integer) (rs.getInt("id")));
                    custform.setFormName((String) (rs.getString("form_name")));
                    custform.setForm_related_to((String) (rs.getString("form_related_to")));
                    custform.setForm_trigerred_event((String) (rs.getString("form_trigger_event")));
                    custform.setSubmit_caption((String) (rs.getString("submit_caption")));
                    custform.setActive((Integer) (rs.getInt("active")));
                    custform.setMobileOnly((Integer) (rs.getInt("mobile_only")));
                    return custform;
                }
            });

            query1.setLength(0);
            query1.append("SELECT fields.id, fields.label, fields.type, fields.placeholder, fields.mandatory, fields.editable, IFNULL(fields.field_options,'') field_options, fields.field_order, fields.form_id_fk, fields.geo_tag_enable, fields.list_id_fk, list.list_name, list.list_type, IFNULL(list.options_list,'') options_list FROM custom_forms_fields AS fields LEFT JOIN predifined_list_categories AS list ON fields.list_id_fk = list.id WHERE fields.form_id_fk = ? ORDER BY field_order");

            List<FormFields> fieldList = FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query1.toString(), param, new RowMapper<FormFields>() {
                public FormFields mapRow(ResultSet rs, int i) throws SQLException {
                    FormFields formfields = new FormFields();
                    formfields.setFieldId((Integer) (rs.getInt("id")));
                    formfields.setLabelName((String) (rs.getString("label")));
                    formfields.setFieldType((String) (rs.getString("type")));
                    formfields.setPlaceHolder((String) (rs.getString("placeholder")));
                    formfields.setMandField((Integer) (rs.getInt("mandatory")));
                    formfields.setEditField((Integer) (rs.getInt("editable")));
                    formfields.setOption(rs.getString("field_options"));
                    formfields.setFieldOrder((Integer) (rs.getInt("field_order")));
                    formfields.setForm_id_fk((Integer) (rs.getInt("form_id_fk")));
                    formfields.setGeoTagEnable(rs.getInt("geo_tag_enable"));
                    formfields.setListType((String) rs.getString("list_type"));
                    formfields.setListName((String) rs.getString("list_name"));
                    formfields.setListIdFK(rs.getInt("list_id_fk"));
                    return formfields;
                }
            });

            custform1.setTableData((ArrayList<FormFields>) fieldList);
            return custform1;
        } catch (Exception e) {
            log4jLog.info(" getFormDetailsForWeb " + e);
            e.printStackTrace();
            return new CustomForm();
        }

    }

    
    /**
     *
     * @param accountId
     * @return
     */
    public boolean updateFormSettingDateTime( int accountId)
    {
        
        String query = "UPDATE settings SET updated_on=now() where name=?";
        Object param[] = new Object[]{"customform"};

        log4jLog.info(" updateCustomer " + query);
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query,param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateCustomer " + e);
//            e.printStackTrace();
            return false;
        }
    
    } 
      
    /**
     *
     * @param accountId
     * @return
     */
    public Timestamp getFormsLastUpdatedOn( int accountId)
    {
        Timestamp updatedOn;
        String query = "Select updated_on from settings where name=?";
        Object param[] = new Object[]{"customform"};

        log4jLog.info(" updateCustomer " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query,param, Timestamp.class);
        } catch (Exception e) {
            log4jLog.info(" updateCustomer " + e);
//            e.printStackTrace();
            return null;
        }
    
    } 
    
    /**
     *
     * @param formdetails
     * @param accountid
     * @return
     */
    public List<Map> getUpdatedForms(CustomForm[] formdetails,int accountid){
        StringBuilder query1 = new StringBuilder();
        StringBuilder query2 = new StringBuilder();
        ArrayList paramlist=new ArrayList();
        if(formdetails.length==0){
           query1.append("SELECT id,(select max(updatedon) from custom_forms where active=1) updatedon from custom_forms where active=1 order by form_name");
           //query1.append("SELECT * from custom_forms where active=1  and form_trigger_event!=2 and form_trigger_event!=4 order by form_name");
        }else{
           query1.append("SELECT id,(select max(updatedon) from custom_forms where active=1) updatedon from custom_forms where active=1 and (updatedon>? ");
           //query1.append("SELECT * from custom_forms where active=1  and form_trigger_event!=2 and form_trigger_event!=4 ");
           query2.append("id!=? ");
           CustomForm firstform=formdetails[0];
           paramlist.add(firstform.getUpdated_On());
           paramlist.add(firstform.getId());
           for(int i=1;i<formdetails.length;i++){
               CustomForm form=formdetails[i];
               query2.append(" and id!=? ");
               paramlist.add(form.getId());
           }
           query1.append(" or (");
           query1.append(query2);
           query1.append("))  order by form_name");
        }
        log4jLog.info(" getFormNames " + query1);
        Object param[]=new Object[paramlist.size()];
        for(int j=0;j<paramlist.size();j++){
            param[j]=paramlist.get(j);
        }
        try {
            final int  accountId=accountid;
                return FieldSenseUtils.getJdbcTemplateForAccount(accountid).query(query1.toString(),param,new RowMapper<Map>(){
                       public Map mapRow(ResultSet rs, int i) throws SQLException{
                       StringBuilder sb = new StringBuilder();
                       java.io.BufferedReader br =null;
                       String xmlFile="";
                       Map custform= new HashMap();
                       int formid=rs.getInt("id");
                       custform.put("id",(Integer)(formid));
                       try{
                        xmlFile=Constant.CUSTOM_FORM_XML_UPLOAD_PATH+"account_"+accountId+"_form_"+formid+".xml";
                        br = new java.io.BufferedReader(new java.io.FileReader(xmlFile));
                        String line;
                        while((line=br.readLine())!= null){
                            sb.append(line.trim());
                        }
                        custform.put("formData",sb.toString());
                        sb.setLength(0);
                       }catch(Exception e){
//                           System.out.println("Error got at logs");
                           e.printStackTrace();
                       }
                       custform.put("updated_On",rs.getTimestamp("updatedon").getTime());
                       return custform;
                    }
                });
             }
        catch (Exception e) {
            log4jLog.info(" getFormNames " + e);
//            e.printStackTrace();
            return new ArrayList();
        }
    }
    
    /**
     *
     * @param accountid
     * @return
     */
    public List<Integer> getAllFormIds(int accountid){
        String query = "Select id from custom_forms where active=1";
        Object param[] = new Object[]{"customform"};

        log4jLog.info(" updateCustomer " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountid).query(query,new RowMapper<Integer>(){
                    public Integer mapRow(ResultSet rs, int i) throws SQLException{
                       Integer id=(Integer)(rs.getInt("id"));
                       return id;
                    }
                });
        } catch (Exception e) {
            log4jLog.info(" updateCustomer " + e);
//            e.printStackTrace();
            return null;
        }
    }

    public List<Map> getUpdatedAllForms(CustomForm[] formdetails,String timeStamp, int accountid) {
        String query="SELECT form_name,form_trigger_event,submit_caption,id,(select max(updatedon) from custom_forms where active=1) updatedon from custom_forms where active=1 AND ( createdon > ? OR updatedon > ? ) order by form_name";
        Object pram[]=new Object[]{timeStamp,timeStamp};
        final int accountId=accountid;
        List<Map> listOfFormData=null;
        try{
                       listOfFormData= FieldSenseUtils.getJdbcTemplateForAccount(accountid).query(query.toString(),pram,new RowMapper<Map>(){
                       public Map mapRow(ResultSet rs, int i) throws SQLException{
                       StringBuilder sb = new StringBuilder();
                       java.io.BufferedReader br =null;
                       String xmlFile="";
                       Map custform= new HashMap();
                       int formid=rs.getInt("id");
                       custform.put("id",(Integer)(formid));
                       custform.put("formName",rs.getString("form_name"));
                       custform.put("submitcaption",rs.getString("submit_caption"));
                       custform.put("form_trigerred_event",rs.getInt("form_trigger_event"));
                       custform.put("active",1);
                       try{
                        xmlFile=Constant.CUSTOM_FORM_XML_UPLOAD_PATH+"account_"+accountId+"_form_"+formid+".xml";
                        br = new java.io.BufferedReader(new java.io.FileReader(xmlFile));
                        String line;
                        while((line=br.readLine())!= null){
                            sb.append(line.trim());
                        }
                        custform.put("formXml",sb.toString());
                        sb.setLength(0);
                       }catch(Exception e){
//                           System.out.println("Error got at logs");
                           e.printStackTrace();
                       }
                       custform.put("updated_On",rs.getTimestamp("updatedon"));
                       custform.put("isDeleted",0);
                       return custform;
                    }
                });
        }catch(Exception e){
        e.printStackTrace();
//            System.out.println("Erroer im query ");
        }       
                List<Integer> formIds=this.getAllFormIds(accountId);
                //HashMap result=new HashMap();
                List<Map> deletedData=new ArrayList<Map>();
                try{
                for(CustomForm form:formdetails){
                    boolean idExists=false;
                    for(Integer ids:formIds){
                        if(form.getId()==ids){
                            idExists=true;
                        }
                    }
                    if(idExists==false){ 
                       Map<String,Object> deletedIds=new HashMap<String,Object>();
                        deletedIds.put("id",form.getId());
                         deletedIds.put("formXml"," ");
                         deletedIds.put("updated_On",new Timestamp(0));
                         deletedIds.put("isDeleted",1);
                          deletedIds.put("formName"," ");
                       deletedIds.put("submitcaption"," ");
                        deletedIds.put("active", 1);
                       deletedIds.put("form_trigerred_event",0);
                        deletedData.add(deletedIds);
                    }
                }
                }catch(Exception e){
                e.printStackTrace();
//                    System.out.println("Erroe in for loop");
                }
               listOfFormData.addAll(deletedData);
               return  listOfFormData;        
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
