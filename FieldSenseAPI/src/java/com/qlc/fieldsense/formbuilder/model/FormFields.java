package com.qlc.fieldsense.formbuilder.model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pallavi.s
 */
public class FormFields {
    
    private int fieldId;
    private String labelName;
    private String fieldType;
    private String placeHolder;
    private int mandField;
    private int editField;
    private String option;
    private int fieldOrder;
    private int form_id_fk;
    private int geoTagEnable;
    private String listType;
    private String listName;
    private int listIdFK;
    
    /**
     *
     */
    public FormFields() {
        
        this.fieldId=-1;
        this.labelName="";
        this.fieldType="";
        this.placeHolder="";
        this.mandField=0; // by default not manadatory
        this.editField=1; // by default editable
        this. option="";
        this.fieldOrder=-1;
        this.form_id_fk=-1;
        this.geoTagEnable=0;
        this.listType="";
        this.listName="";
        this.listIdFK=0;
    }

  
    
    /**
     * @return the fieldId
     */
    public int getFieldId() {
        return fieldId;
    }
    
    /**
     * @param fieldId the fieldId to set
     */
    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    /**
     * @return the labelName
     */
    public String getLabelName() {
        return labelName;
    }

    /**
     * @param labelName the labelName to set
     */
    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    /**
     * @return the fieldType
     */
    public String getFieldType() {
        return fieldType;
    }

    /**
     * @param fieldType the fieldType to set
     */
    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    /**
     * @return the placeHolder
     */
    public String getPlaceHolder() {
        return placeHolder;
    }

    /**
     * @param placeHolder the placeHolder to set
     */
    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
    }

    /**
     * @return the mandField
     */
    public int getMandField() {
        return mandField;
    }

    /**
     * @param mandField the mandField to set
     */
    public void setMandField(int mandField) {
        this.mandField = mandField;
    }

    /**
     * @return the editField
     */
    public int getEditField() {
        return editField;
    }

    /**
     * @param editField the editField to set
     */
    public void setEditField(int editField) {
        this.editField = editField;
    }

    /**
     * @return the option
     */
    public String getOption() {
        return option;
    }

    /**
     * @param option the option to set
     */
    public void setOption(String option) {
        this.option = option;
    }

    /**
     * @return the fieldOrder
     */
    public int getFieldOrder() {
        return fieldOrder;
    }

    /**
     * @param fieldOrder the fieldOrder to set
     */
    public void setFieldOrder(int fieldOrder) {
        this.fieldOrder = fieldOrder;
    }

    /**
     * @return the form_id_fk
     */
    public int getForm_id_fk() {
        return form_id_fk;
    }

    /**
     * @param form_id_fk the form_id_fk to set
     */
    public void setForm_id_fk(int form_id_fk) {
        this.form_id_fk = form_id_fk;
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

    /**
     *
     * @return
     */
    public String getListType() {
        return listType;
    }

    /**
     *
     * @param listType
     */
    public void setListType(String listType) {
        this.listType = listType;
    }

    /**
     *
     * @return
     */
    public String getListName() {
        return listName;
    }

    /**
     *
     * @param listName
     */
    public void setListName(String listName) {
        this.listName = listName;
    }
  
    /**
     *
     * @return
     */
    public int getListIdFK() {
        return listIdFK;
    }

    /**
     *
     * @param listIdFK
     */
    public void setListIdFK(int listIdFK) {
        this.listIdFK = listIdFK;
    }

    @Override
    public String toString() {
        return "FormFields{" + "fieldId=" + fieldId + ", labelName=" + labelName + ", fieldType=" + fieldType + ", placeHolder=" + placeHolder + ", mandField=" + mandField + ", editField=" + editField + ", option=" + option + ", fieldOrder=" + fieldOrder + ", form_id_fk=" + form_id_fk + ", geoTagEnable=" + geoTagEnable + ", listType=" + listType + ", listName=" + listName + ", listIdFK=" + listIdFK + '}';
    }
    
}
