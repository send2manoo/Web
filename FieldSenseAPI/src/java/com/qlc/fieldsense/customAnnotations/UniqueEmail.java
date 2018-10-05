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
 * @author anuja
 * @date 4/2/2014
 * @purpose custom unique email validation(account)
 */
@Documented  
@Retention(RetentionPolicy.RUNTIME)  
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.CONSTRUCTOR,ElementType.PARAMETER,ElementType.ANNOTATION_TYPE})  
@Constraint(validatedBy = UniqueEmailValidator.class)  
public @interface UniqueEmail {

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
