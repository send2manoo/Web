/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customAnnotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.log4j.Logger;

/**
 *
 * @author anuja
 * @date 3/2/2014
 * @purpose custom trim space validation
 */
public class TrimSpaceValidator implements ConstraintValidator<TrimSpaceValidation, String> {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("TrimSpaceValidator");

    /**
     *
     * @param space
     */
    @Override
    public void initialize(TrimSpaceValidation space) {
    }

    /**
     *
     * @param space
     * @param context
     * @return
     */
    @Override
    public boolean isValid(String space, ConstraintValidatorContext context) {
        int len = 0;
        len = space.trim().length();
        if (len == 0) {
            return false;
        }
        return true;
    }
}
