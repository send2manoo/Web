/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customerOffline.model;

/**
 *
 * @author siddhesh
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.qlc.fieldsense.appointments.model.Appointment;
import com.qlc.fieldsense.customAnnotations.TrimSpaceValidation;
import com.qlc.fieldsense.customAnnotations.ValidSize;
import com.qlc.fieldsense.expense.model.Expense;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author siddhesh
 */
public class LocalData {
    
    
   // private int mobileTempId;
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
    private int recordState;
    private List<AppointmentOffline> appointments;
    private List<Expense> expense;
   private double lasknownLatitude;
    private double lasknownLangitude;
    private String modifiedOnMobile;
     private boolean isHeadOffice;
     private String customerPhone1;
     private String customerPhone2;
     private String customerPhone3;
     private String customerFax1;
     private String customerFax2;
     private String customerEmail1;
     private String customerEmail2;
     private String customerEmail3;
     private String customerWebsiteUrl1;
     private String customerWebsiteUrl2;
     private int customerType;
    
    /**
     *
     */
    public LocalData()
    {
     this.id="0";
     //mobileTempId=0;
     this.customerName="";
     this.customerPrintas="";
     this.isHeadOffice = false;
     this.customerAddress1="";
     this.territory="";
     this.recordState=0;
     this.industry="";
     this.customerLocation="";
     this.customerPhone1 = "";
        this.customerPhone2 = "";
        this.customerPhone3 = "";
        this.customerFax1 = "";
        this.customerFax2 = "";
        this.customerEmail1 = "";
        this.customerEmail2 = "";
        this.customerEmail3 = "";
        this.customerWebsiteUrl1 = "";
        this.customerWebsiteUrl2 = "";     
        this.appointments=new ArrayList<AppointmentOffline>();
     this.expense=new ArrayList<Expense>();
     this.modifiedOnMobile="";
     this.customerType=0;
    }

    /**
     *
     * @return
     */
    public List<AppointmentOffline> getAppointments() {
        return appointments;
    }

    /**
     *
     * @param appointments
     */
    public void setAppointments(List<AppointmentOffline> appointments) {
        this.appointments = appointments;
    }

    /**
     *
     * @return
     */
    public List<Expense> getExpense() {
        return expense;
    }

    /**
     *
     * @param expense
     */
    public void setExpense(List<Expense> expense) {
        this.expense = expense;
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

    /**
     *
     * @return
     */
    public int getRecordState() {
        return recordState;
    }

    /**
     *
     * @param recordState
     */
    public void setRecordState(int recordState) {
        this.recordState = recordState;
    }

    /**
     *
     * @return
     */
    public double getLasknownLatitude() {
        return lasknownLatitude;
    }

    /**
     *
     * @param lasknownLatitude
     */
    public void setLasknownLatitude(double lasknownLatitude) {
        this.lasknownLatitude = lasknownLatitude;
    }

    /**
     *
     * @return
     */
    public double getLasknownLangitude() {
        return lasknownLangitude;
    }

    /**
     *
     * @param lasknownLangitude
     */
    public void setLasknownLangitude(double lasknownLangitude) {
        this.lasknownLangitude = lasknownLangitude;
    }

    /**
     *
     * @return
     */
    public boolean isIsHeadOffice() {
        return isHeadOffice;
    }

    /**
     *
     * @param isHeadOffice
     */
    public void setIsHeadOffice(boolean isHeadOffice) {
        this.isHeadOffice = isHeadOffice;
    }

    /**
     *
     * @return
     */
    public String getCustomerPhone1() {
        return customerPhone1;
    }

    /**
     *
     * @param customerPhone1
     */
    public void setCustomerPhone1(String customerPhone1) {
        this.customerPhone1 = customerPhone1;
    }

    /**
     *
     * @return
     */
    public String getCustomerPhone2() {
        return customerPhone2;
    }

    /**
     *
     * @param customerPhone2
     */
    public void setCustomerPhone2(String customerPhone2) {
        this.customerPhone2 = customerPhone2;
    }

    /**
     *
     * @return
     */
    public String getCustomerPhone3() {
        return customerPhone3;
    }

    /**
     *
     * @param customerPhone3
     */
    public void setCustomerPhone3(String customerPhone3) {
        this.customerPhone3 = customerPhone3;
    }

    /**
     *
     * @return
     */
    public String getCustomerFax1() {
        return customerFax1;
    }

    /**
     *
     * @param customerFax1
     */
    public void setCustomerFax1(String customerFax1) {
        this.customerFax1 = customerFax1;
    }

    /**
     *
     * @return
     */
    public String getCustomerFax2() {
        return customerFax2;
    }

    /**
     *
     * @param customerFax2
     */
    public void setCustomerFax2(String customerFax2) {
        this.customerFax2 = customerFax2;
    }

    /**
     *
     * @return
     */
    public String getCustomerEmail1() {
        return customerEmail1;
    }

    /**
     *
     * @param customerEmail1
     */
    public void setCustomerEmail1(String customerEmail1) {
        this.customerEmail1 = customerEmail1;
    }

    /**
     *
     * @return
     */
    public String getCustomerEmail2() {
        return customerEmail2;
    }

    /**
     *
     * @param customerEmail2
     */
    public void setCustomerEmail2(String customerEmail2) {
        this.customerEmail2 = customerEmail2;
    }

    /**
     *
     * @return
     */
    public String getCustomerEmail3() {
        return customerEmail3;
    }

    /**
     *
     * @param customerEmail3
     */
    public void setCustomerEmail3(String customerEmail3) {
        this.customerEmail3 = customerEmail3;
    }

    /**
     *
     * @return
     */
    public String getCustomerWebsiteUrl1() {
        return customerWebsiteUrl1;
    }

    /**
     *
     * @param customerWebsiteUrl1
     */
    public void setCustomerWebsiteUrl1(String customerWebsiteUrl1) {
        this.customerWebsiteUrl1 = customerWebsiteUrl1;
    }

    /**
     *
     * @return
     */
    public String getCustomerWebsiteUrl2() {
        return customerWebsiteUrl2;
    }

    /**
     *
     * @param customerWebsiteUrl2
     */
    public void setCustomerWebsiteUrl2(String customerWebsiteUrl2) {
        this.customerWebsiteUrl2 = customerWebsiteUrl2;
    }

    /**
     *
     * @return
     */
    public String getModifiedOnMobile() {
        return modifiedOnMobile;
    }

    /**
     *
     * @param modifiedOnMobile
     */
    public void setModifiedOnMobile(String modifiedOnMobile) {
        this.modifiedOnMobile = modifiedOnMobile;
    }

    /**
     *
     * @return
     */
    public int getCustomerType() {
        return customerType;
    }

    /**
     *
     * @param customerType
     */
    public void setCustomerType(int customerType) {
        this.customerType = customerType;
    }

  
}
