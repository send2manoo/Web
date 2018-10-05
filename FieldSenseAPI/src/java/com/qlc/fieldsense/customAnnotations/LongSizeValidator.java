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
 * @date 4/2/2014
 * @purpose custom size of long validation
 */
public class LongSizeValidator implements ConstraintValidator<ValidLongSize, Long> {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("LongSizeValidator");
    int maxlen = 0;

    /**
     *
     * @param siz
     */
    @Override
    public void initialize(ValidLongSize siz) {
        maxlen = siz.size();
    }

    /**
     *
     * @param field
     * @param context
     * @return
     */
    @Override
    public boolean isValid(Long field, ConstraintValidatorContext context) {
        int len = String.valueOf(field).length();
        if (len > maxlen) {
            return false;
        }
        return true;
    }
}
