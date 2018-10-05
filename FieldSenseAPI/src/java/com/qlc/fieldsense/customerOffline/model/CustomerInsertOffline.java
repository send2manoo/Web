/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customerOffline.model;

import com.qlc.fieldsense.customAnnotations.TrimSpaceValidation;
import com.qlc.fieldsense.customAnnotations.ValidSize;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author siddhesh
 */
public class CustomerInsertOffline {
     private String id;
    @NotEmpty
    @Length(min = 1, max = 200)
    @TrimSpaceValidation(message = "Please enter valid data")
    private String customerName;
    @ValidSize(size = 200, message = "Size must be less than 200")
    private String customerPrintas;
    @ValidSize(size = 100, message = "Size must be less than 100")
    private String customerLocation;
      @ValidSize(size = 100, message = "Size must be less than 100")
    private String territory;
    @ValidSize(size = 100, message = "Size must be less than 100")
    private String industry;
     @Length(min = 1, max = 200)
    @NotEmpty
    @TrimSpaceValidation(message = "Please enter valid data")
    private String customerAddress1;
     
    /**
     *
     */
    public  CustomerInsertOffline()
     {
     
     id="";
     //mobileTempId=0;
     customerName="";
     customerPrintas="";
     customerAddress1="";
     territory="";
     industry="";
     customerLocation="";
     
     
     }

    @Override
    public String toString() {
        return "CustomerInsertOffline{" + "id=" + id + ", customerName=" + customerName + ", customerPrintas=" + customerPrintas + ", customerLocation=" + customerLocation + ", territory=" + territory + ", industry=" + industry + ", customerAddress1=" + customerAddress1 + '}';
    }

    /**
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     *
     * @param customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     *
     * @return
     */
    public String getCustomerPrintas() {
        return customerPrintas;
    }

    /**
     *
     * @param customerPrintas
     */
    public void setCustomerPrintas(String customerPrintas) {
        this.customerPrintas = customerPrintas;
    }

    /**
     *
     * @return
     */
    public String getCustomerLocation() {
        return customerLocation;
    }

    /**
     *
     * @param customerLocation
     */
    public void setCustomerLocation(String customerLocation) {
        this.customerLocation = customerLocation;
    }

    /**
     *
     * @return
     */
    public String getTerritory() {
        return territory;
    }

    /**
     *
     * @param territory
     */
    public void setTerritory(String territory) {
        this.territory = territory;
    }

    /**
     *
     * @return
     */
    public String getIndustry() {
        return industry;
    }

    /**
     *
     * @param industry
     */
    public void setIndustry(String industry) {
        this.industry = industry;
    }

    /**
     *
     * @return
     */
    public String getCustomerAddress1() {
        return customerAddress1;
    }

    /**
     *
     * @param customerAddress1
     */
    public void setCustomerAddress1(String customerAddress1) {
        this.customerAddress1 = customerAddress1;
    }
}
