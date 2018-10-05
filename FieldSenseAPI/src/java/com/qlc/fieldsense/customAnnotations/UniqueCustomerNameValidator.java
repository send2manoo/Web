/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customAnnotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.qlc.fieldsense.utils.FieldSenseUtils;

/**
 *
 * @author anuja
 * @date 4/2/2014
 * @purpose custom unique customer name validation
 */
public class UniqueCustomerNameValidator implements ConstraintValidator<UniqueCustomerName, String> {

    FieldSenseUtils util = new FieldSenseUtils();

    /**
     *
     * @param name
     */
    @Override
    public void initialize(UniqueCustomerName name) {
    }

    /**
     *
     * @param name
     * @param context
     * @return
     */
    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        int len = 0;
//        len = util.isCustomerNameExist(name);
        if (len > 0) {
            return false;
        }
        return true;
    }
}
