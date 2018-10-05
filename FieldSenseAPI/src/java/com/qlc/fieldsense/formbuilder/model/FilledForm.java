/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.formbuilder.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pallavi.s
 */
public class FilledForm {
    
    private int id;
    private int userid;
    private Timestamp submitTime;
    private int formid;
    private ArrayList<FilledFields> filledData;
    private String submittedby;

    /**
     *
     * @return
     */
    public String getSubmittedby() {
        return submittedby;
    }

    /**
     *
     * @param submittedby
     */
    public void setSubmittedby(String submittedby) {
        this.submittedby = submittedby;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public int getUserid() {
        return userid;
    }

    /**
     *
     * @param userid
     */
    public void setUserid(int userid) {
        this.userid = userid;
    }

    /**
     *
     * @return
     */
    public Timestamp getSubmitTime() {
        return submitTime;
    }

    /**
     *
     * @param submitTime
     */
    public void setSubmitTime(Timestamp submitTime) {
        this.submitTime = submitTime;
    }

    /**
     *
     * @return
     */
    public int getFormid() {
        return formid;
    }

    /**
     *
     * @param formid
     */
    public void setFormid(int formid) {
        this.formid = formid;
    }

    /**
     *
     * @return
     */
    public ArrayList<FilledFields> getFilledData() {
        return filledData;
    }

    /**
     *
     * @param filledData
     */
    public void setFilledData(ArrayList<FilledFields> filledData) {
        this.filledData = filledData;
    }
    
    /**
     *
     */
    public FilledForm()
    {
        this.id=0;
        this.userid=0;
        this.submitTime=new Timestamp(0);
        this.formid=0;
        this.filledData=new ArrayList<FilledFields>();
        this.submittedby="";
    }
}
