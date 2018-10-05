/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.search.model;

/**
 *
 * @author Ramesh
 * @date 11-09-2014
 */
public class SearchCustomerMobile {

    private int id;
    private String customerPrintAs;
    private String customerName;
    private String customerLocation;
    private double latitude;
    private double longitude;

    /**
     *
     */
    public SearchCustomerMobile() {
        this.id = 0;
        this.customerPrintAs = "";
        this.customerName="";
        this.customerLocation = "";
        this.latitude = 0.0;
        this.longitude = 0.0;
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
    public String getCustomerPrintAs() {
        return customerPrintAs;
    }

    /**
     *
     * @param customerPrintAs
     */
    public void setCustomerPrintAs(String customerPrintAs) {
        this.customerPrintAs = customerPrintAs;
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
    public double getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
}
