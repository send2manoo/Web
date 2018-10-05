/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.formbuilder.model;

import com.qlc.fieldsense.customAnnotations.ValidSize;
import com.qlc.fieldsense.customerContact.model.CustomerContacts;
import java.sql.Timestamp;
import java.util.ArrayList;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
/**
 *
 * @author pallavi.s
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomForm {
    
    private int id;
  //  @ValidSize(size = 200, message = "Size must be less than 200")
    private String formName;
    private String form_related_to;
    private String form_trigerred_event;
    private String submit_caption;
    private int mobileOnly; 
    private Timestamp createdOn;
    private String createdBy;
    private Timestamp UpdatedOn;
    private String UpdatedBy;
    private int active;
    private String formXml;

    private ArrayList<FormFields> tableData;
    private ArrayList<Integer> deletedFields;
    
    //added by shivakrishna to get timestamp from app to fetch updated forms list
    private Timestamp updated_On;
    private CustomForm[] updatedForms;

    /**
     *
     */
    public CustomForm()
    {
    this.id = 0;
    this.formName="";
    this.form_related_to="";
    this.form_trigerred_event="";
    this.submit_caption="";
    this.mobileOnly=1; 
    this.createdOn= new Timestamp(0);
    this.createdBy="";
    this.UpdatedOn= new Timestamp(0);
    this.updated_On= new Timestamp(0);
    this.UpdatedBy="";
    this.active=1;
    this.tableData= new ArrayList<FormFields>();
    this.deletedFields= new ArrayList<Integer>();
    this.formXml="";
    this.updatedForms=new CustomForm[0];
   
    }
    
    /**
     *
     * @return
     */
    public ArrayList<Integer> getDeletedFields() {
        return deletedFields;
    }

    /**
     *
     * @param deletedFields
     */
    public void setDeletedFields(ArrayList<Integer> deletedFields) {
        this.deletedFields = deletedFields;
    }

    /**
     *
     * @return
     */
    public ArrayList<FormFields> getTableData() {
        return tableData;
    }

    /**
     *
     * @param tableData
     */
    public void setTableData(ArrayList<FormFields> tableData) {
        this.tableData = tableData;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the createdOn
     */
    public Timestamp getCreatedOn() {
        return createdOn;
    }

    /**
     * @param createdOn the createdOn to set
     */
    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * @return the createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the UpdatedOn
     */
    public Timestamp getUpdatedOn() {
        return UpdatedOn;
    }

    /**
     * @param UpdatedOn the UpdatedOn to set
     */
    public void setUpdatedOn(Timestamp UpdatedOn) {
        this.UpdatedOn = UpdatedOn;
    }

    /**
     * @return the UpdatedBy
     */
    public String getUpdatedBy() {
        return UpdatedBy;
    }

    /**
     * @param UpdatedBy the UpdatedBy to set
     */
    public void setUpdatedBy(String UpdatedBy) {
        this.UpdatedBy = UpdatedBy;
    }

    /**
     *
     * @return
     */
    public int getActive() {
        return active;
    }

    /**
     *
     * @param active
     */
    public void setActive(int active) {
        this.active = active;
    }
    /**
     * @return the formName
     */
    public String getFormName() {
        return formName;
    }

    /**
     * @param formName the formName to set
     */
    public void setFormName(String formName) {
        this.formName = formName;
    }

    /**
     * @return the form_related_to
     */
    public String getForm_related_to() {
        return form_related_to;
    }

    /**
     * @param form_related_to the form_related_to to set
     */
    public void setForm_related_to(String form_related_to) {
        this.form_related_to = form_related_to;
    }

    /**
     * @return the form_trigerred_event
     */
    public String getForm_trigerred_event() {
        return form_trigerred_event;
    }

    /**
     * @param form_trigerred_event the form_trigerred_event to set
     */
    public void setForm_trigerred_event(String form_trigerred_event) {
        this.form_trigerred_event = form_trigerred_event;
    }

    /**
     * @return the submit_caption
     */
    public String getSubmit_caption() {
        return submit_caption;
    }

    /**
     * @param submit_caption the submit_caption to set
     */
    public void setSubmit_caption(String submit_caption) {
        this.submit_caption = submit_caption;
    }

    /**
     * @return the mobile_enable
     */
    public int getMobileOnly() {
        return mobileOnly;
    }

    /**
     * @param mobileOnly
     * @param mobile_enable the mobile_enable to set
     */
    public void setMobileOnly(int mobileOnly) {
        this.mobileOnly = mobileOnly;
    }

    /**
     *
     * @return
     */
    public Timestamp getUpdated_On() {
        return updated_On;
    }

    /**
     *
     * @param updated_On
     */
    public void setUpdated_On(Timestamp updated_On) {
        this.updated_On = updated_On;
    }

    public String getFormXml() {
        return formXml;
    }

    public void setFormXml(String formXml) {
        this.formXml = formXml;
    }

    public CustomForm[] getUpdatedForms() {
        return updatedForms;
    }

    public void setUpdatedForms(CustomForm[] updatedForms) {
        this.updatedForms = updatedForms;
    }

    @Override
    public String toString() {
        return "CustomForm{" + "id=" + id + ", formName=" + formName + ", form_related_to=" + form_related_to + ", form_trigerred_event=" + form_trigerred_event + ", submit_caption=" + submit_caption + ", mobileOnly=" + mobileOnly + ", createdOn=" + createdOn + ", createdBy=" + createdBy + ", UpdatedOn=" + UpdatedOn + ", UpdatedBy=" + UpdatedBy + ", active=" + active + ", formXml=" + formXml + ", tableData=" + tableData + ", deletedFields=" + deletedFields + ", updated_On=" + updated_On + ", updatedForms=" + updatedForms + '}';
    }

      
}
