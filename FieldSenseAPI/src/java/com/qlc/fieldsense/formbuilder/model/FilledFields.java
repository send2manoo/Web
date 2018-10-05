/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.formbuilder.model;

import java.util.List;

/**
 *
 * @author pallavi.s
 */
public class FilledFields {

    private int id;
    private String field_id_fk;	
    private String field_value;
    private String fieldType;
    private int geoTagEnable;
    private CustomFormImage customFormImage;
    private int form_fill_detail_id_fk; 

    /**
     *
     */
    public FilledFields()
    {
        this.id=0;
        this.field_id_fk="";
        this.field_value="";
        this.fieldType="";
        this.customFormImage = new CustomFormImage();
        this.form_fill_detail_id_fk=0;
        this.geoTagEnable=0;
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
     * @return the field_id_fk
     */
    public String getField_id_fk() {
        return field_id_fk;
    }

    /**
     * @param field_id_fk the field_id_fk to set
     */
    public void setField_id_fk(String field_id_fk) {
        this.field_id_fk = field_id_fk;
    }

    /**
     * @return the field_value
     */
    public String getField_value() {
        return field_value;
    }

    /**
     * @param field_value the field_value to set
     */
    public void setField_value(String field_value) {
        this.field_value = field_value;
    }

    /**
     * @return the form_fill_detail_id_fk
     */
    public int getForm_fill_detail_id_fk() {
        return form_fill_detail_id_fk;
    }

    /**
     * @param form_fill_detail_id_fk to set
     */
    public void setForm_fill_detail_id_fk(int form_fill_detail_id_fk) {
        this.form_fill_detail_id_fk = form_fill_detail_id_fk;
    }

    /**
     *
     * @return
     */
    public String getFieldType() {
        return fieldType;
    }

    /**
     *
     * @param fieldType
     */
    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    /**
     *
     * @return
     */
    public CustomFormImage getCustomFormImage() {
        return customFormImage;
    }

    /**
     *
     * @param customFormImage
     */
    public void setCustomFormImage(CustomFormImage customFormImage) {
        this.customFormImage = customFormImage;
    }

    /**
     *
     * @return
     */
    public int getGeoTagEnable() {
        return geoTagEnable;
    }

    /**
     *
     * @param geoTagEnable
     */
    public void setGeoTagEnable(int geoTagEnable) {
        this.geoTagEnable = geoTagEnable;
    }

  
    
}
