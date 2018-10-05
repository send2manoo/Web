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
 * @purpose custom size validation
 */
public class SizeValidator implements ConstraintValidator<ValidSize, String> {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("SizeValidator");
    int maxlen = 0;

    /**
     *
     * @param siz
     */
    @Override
    public void initialize(ValidSize siz) {
        maxlen = siz.size();
    }

    /**
     *
     * @param field
     * @param context
     * @return
     */
    @Override
    public boolean isValid(String field, ConstraintValidatorContext context) {
        int len = field.length();
        if (len > maxlen) {
            return false;
        }
        return true;
    }
}
