/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.addressConverter;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Ramesh
 * @date 20-08-2014
 */
public class Geometry {

    private Location location;
    private String location_type;
    @JsonIgnore
    private Object bounds;
    @JsonIgnore
    private Object viewport;

    /**
     *
     * @return
     */
    public Location getLocation() {
        return location;
    }

    /**
     *
     * @param location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     *
     * @return
     */
    public String getLocation_type() {
        return location_type;
    }

    /**
     *
     * @param location_type
     */
    public void setLocation_type(String location_type) {
        this.location_type = location_type;
    }

    /**
     *
     * @return
     */
    public Object getBounds() {
        return bounds;
    }

    /**
     *
     * @param bounds
     */
    public void setBounds(Object bounds) {
        this.bounds = bounds;
    }

    /**
     *
     * @return
     */
    public Object getViewport() {
        return viewport;
    }

    /**
     *
     * @param viewport
     */
    public void setViewport(Object viewport) {
        this.viewport = viewport;
    }
}
