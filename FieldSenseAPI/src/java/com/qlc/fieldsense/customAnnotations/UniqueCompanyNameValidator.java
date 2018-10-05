/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customAnnotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import org.apache.log4j.Logger;

/**
 *
 * @author anuja
 * @date 4/2/2014
 * @purpose custom unique email validation
 */
public class UniqueCompanyNameValidator implements ConstraintValidator<UniqueCompanyName, String> {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("UniqueCompanyNameValidator");
    FieldSenseUtils util = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");

    /**
     *
     * @param companyName
     */
    @Override
    public void initialize(UniqueCompanyName companyName) {
    }

    /**
     *
     * @param companyName
     * @param context
     * @return
     */
    @Override
    public boolean isValid(String companyName, ConstraintValidatorContext context) {
        int len = 0;
        len = util.isCompanyNameExist(companyName);
        if (len > 0) {
            return false;
        }
        return true;
    }
}