/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customerOffline.model;
import com.qlc.fieldsense.customAnnotations.*;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
/**
 *
 * @author jyoti
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerOfflineWithFewData {
    
    private int id;
    @NotEmpty
    @Length(min = 1, max = 200)
    @TrimSpaceValidation(message = "Please enter valid data")
    private String customerName;
    @ValidSize(size = 200, message = "Size must be less than 200")
    private String customerPrintas;
    @Length(min = 1, max = 200)
    @NotEmpty
    @TrimSpaceValidation(message = "Please enter valid data")
    private String customerAddress1;
    @ValidSize(size = 100, message = "Size must be less than 100")
    private String customerLocation;
    private double lasknownLatitude;
    private double lasknownLangitude;
    @ValidSize(size = 50, message = "Size must be less than 50")
    private String customerPhone1;
    @ValidSize(size = 50, message = "Size must be less than 50")
    private String customerPhone2;
    @ValidSize(size = 50, message = "Size must be less than 50")
    private String customerPhone3;
    @ValidSize(size = 50, message = "Size must be less than 50")
    private String customerFax1;
    @ValidSize(size = 50, message = "Size must be less than 50")
    private String customerFax2;
    @ValidSize(size = 100, message = "Size must be less than 100")
    private String customerEmail1;
    @ValidSize(size = 100, message = "Size must be less than 100")
    private String customerEmail2;
    @ValidSize(size = 100, message = "Size must be less than 100")
    private String customerEmail3;
    @ValidSize(size = 200, message = "Size must be less than 200")
    private String customerWebsiteUrl1;
    @ValidSize(size = 200, message = "Size must be less than 200")
    private String customerWebsiteUrl2;
    private List<CustomersOfflineRequestedList> customersOfflineRequestedList;
    private List<TerritoryList> territoryLists;

    /**
     *
     */
    public CustomerOfflineWithFewData() {
        this.id = 0;
        this.customerName = "";
        this.customerPrintas = "";
        this.customerAddress1 = "";
        this.customerLocation = "";
        this.lasknownLatitude = 0.0;
        this.lasknownLangitude = 0.0;
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
        this.customersOfflineRequestedList = new ArrayList<CustomersOfflineRequestedList>();
        this.territoryLists=new ArrayList<TerritoryList>();
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
     * @return
     */
    public String getCustomerName() {
        return customerName;
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
     * @return
     */
    public String getCustomerLocation() {
        return customerLocation;
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
     * @return
     */
    public double getLasknownLangitude() {
        return lasknownLangitude;
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
     * @return
     */
    public String getCustomerPhone2() {
        return customerPhone2;
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
     * @return
     */
    public String getCustomerFax1() {
        return customerFax1;
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
     * @return
     */
    public String getCustomerEmail1() {
        return customerEmail1;
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
     * @return
     */
    public String getCustomerEmail3() {
        return customerEmail3;
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
     * @return
     */
    public String getCustomerWebsiteUrl2() {
        return customerWebsiteUrl2;
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
     * @param customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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
     * @param customerLocation
     */
    public void setCustomerLocation(String customerLocation) {
        this.customerLocation = customerLocation;
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
     * @param lasknownLangitude
     */
    public void setLasknownLangitude(double lasknownLangitude) {
        this.lasknownLangitude = lasknownLangitude;
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
     * @param customerPhone2
     */
    public void setCustomerPhone2(String customerPhone2) {
        this.customerPhone2 = customerPhone2;
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
     * @param customerFax1
     */
    public void setCustomerFax1(String customerFax1) {
        this.customerFax1 = customerFax1;
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
     * @param customerEmail1
     */
    public void setCustomerEmail1(String customerEmail1) {
        this.customerEmail1 = customerEmail1;
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
     * @param customerEmail3
     */
    public void setCustomerEmail3(String customerEmail3) {
        this.customerEmail3 = customerEmail3;
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
     * @param customerWebsiteUrl2
     */
    public void setCustomerWebsiteUrl2(String customerWebsiteUrl2) {
        this.customerWebsiteUrl2 = customerWebsiteUrl2;
    }

    /**
     *
     * @return
     */
    public List<CustomersOfflineRequestedList> getCustomersOfflineRequestedList() {
        return customersOfflineRequestedList;
    }

    /**
     *
     * @param customersOfflineRequestedList
     */
    public void setCustomersOfflineRequestedList(List<CustomersOfflineRequestedList> customersOfflineRequestedList) {
        this.customersOfflineRequestedList = customersOfflineRequestedList;
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
    public List<TerritoryList> getTerritoryLists() {
        return territoryLists;
    }

    /**
     *
     * @param territoryLists
     */
    public void setTerritoryLists(List<TerritoryList> territoryLists) {
        this.territoryLists = territoryLists;
    }

    
}
