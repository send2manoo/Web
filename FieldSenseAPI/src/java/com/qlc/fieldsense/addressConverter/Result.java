/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.addressConverter;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Ramesh
 * @date 21-02-2014
 */
public class Result {

    private String formatted_address;
    private boolean partial_match;
    private Geometry geometry;
    @JsonIgnore
    private Object address_components;
    @JsonIgnore
    private Object types;
    private Object place_id;

    /**
     *
     * @return
     */
    public Object getPlace_id() {
        return place_id;
    }

    /**
     *
     * @param place_id
     */
    public void setPlace_id(Object place_id) {
        this.place_id = place_id;
    }

    /**
     *
     * @return
     */
    public String getFormatted_address() {
        return formatted_address;
    }

    /**
     *
     * @param formatted_address
     */
    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    /**
     *
     * @return
     */
    public boolean isPartial_match() {
        return partial_match;
    }

    /**
     *
     * @param partial_match
     */
    public void setPartial_match(boolean partial_match) {
        this.partial_match = partial_match;
    }

    /**
     *
     * @return
     */
    public Geometry getGeometry() {
        return geometry;
    }

    /**
     *
     * @param geometry
     */
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    /**
     *
     * @return
     */
    public Object getAddress_components() {
        return address_components;
    }

    /**
     *
     * @param address_components
     */
    public void setAddress_components(Object address_components) {
        this.address_components = address_components;
    }

    /**
     *
     * @return
     */
    public Object getTypes() {
        return types;
    }

    /**
     *
     * @param types
     */
    public void setTypes(Object types) {
        this.types = types;
    }
}
