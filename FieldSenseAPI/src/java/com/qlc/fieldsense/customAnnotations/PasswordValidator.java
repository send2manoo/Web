/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customAnnotations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author Ramesh
 * @date 19-02-2014
 */
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    /**
     *
     * @param a
     */
    public void initialize(ValidPassword a) {
    }

    /**
     *
     * @param password
     * @param cvc
     * @return
     */
    public boolean isValid(String password, ConstraintValidatorContext cvc) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9!@#$.]+$");
        Matcher m = p.matcher(password);
        boolean b = m.find();
        return b;
    }
}
