/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customAnnotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;

/**
 *
 * @author Ramesh
 * @date 19-02-2014
 * @purpose To Check is password is valid .
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = PasswordValidator.class)
public @interface ValidPassword {

    /**
     *
     * @return
     */
    String message();

    /**
     *
     * @return
     */
    Class[] groups() default {};

    /**
     *
     * @return
     */
    Class[] payload() default {};
}
