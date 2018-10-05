/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.expense.model;

/**
 *
 * @author shivakrishna
 */
public class ExpenseFilter {
    
    private String userid;
    private String status;
    private String expensecategory;
    private String fromdate;
    private String todate;

    /**
     *
     */
    public ExpenseFilter() {
        this.userid = "";
        this.status="";
        this.expensecategory="";
        this.fromdate="";
        this.todate="";
       
    }
    
    /**
     *
     * @return
     */
    public String getUserid() {
        return userid;
    }

    /**
     *
     * @param userid
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /**
     *
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     */
    public String getExpensecategory() {
        return expensecategory;
    }

    /**
     *
     * @param expensecategory
     */
    public void setExpensecategory(String expensecategory) {
        this.expensecategory = expensecategory;
    }

    /**
     *
     * @return
     */
    public String getFromdate() {
        return fromdate;
    }

    /**
     *
     * @param fromdate
     */
    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    /**
     *
     * @return
     */
    public String getTodate() {
        return todate;
    }

    /**
     *
     * @param todate
     */
    public void setTodate(String todate) {
        this.todate = todate;
    }
  
}
