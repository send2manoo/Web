/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.utils;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Ramesh
 * @date 29-01-2014 .
 * @purpose To transfer error messages in fieldsense .
 */
public class FieldSenseValidationErrorDTO {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("FieldSenseValidationErrorDTO");
    private List<FieldSenseErrorDTO> fieldErrors = new ArrayList<FieldSenseErrorDTO>();

    /**
     *
     */
    public FieldSenseValidationErrorDTO() {
    }

    /**
     *
     * @param path
     * @param message
     */
    public void addFieldError(String path, String message) {
        FieldSenseErrorDTO error = new FieldSenseErrorDTO(path, message);
        fieldErrors.add(error);
    }

    /**
     *
     * @return
     */
    public List<FieldSenseErrorDTO> getFieldErrors() {
        return fieldErrors;
    }

    /**
     *
     * @param fieldErrors
     */
    public void setFieldErrors(List<FieldSenseErrorDTO> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
