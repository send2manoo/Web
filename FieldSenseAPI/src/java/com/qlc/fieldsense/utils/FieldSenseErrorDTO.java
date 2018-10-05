/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.utils;

/**
 *
 * @author Ramesh
 * @date 29-01-2014 .
 * @purpose To transfer error messages in fieldsense .
 */
public class FieldSenseErrorDTO {

    private String field;
    private String message;

    /**
     *
     * @param field
     * @param message
     */
    public FieldSenseErrorDTO(String field, String message) {

        this.field = field;

        this.message = message;

    }

    /**
     *
     * @return
     */
    public String getField() {
        return field;
    }

    /**
     *
     * @param field
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
