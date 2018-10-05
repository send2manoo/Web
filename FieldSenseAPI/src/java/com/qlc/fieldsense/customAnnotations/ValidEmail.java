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
 * @date 30/1/2014
 * @purpose custom email validation
 */

@Documented  
@Retention(RetentionPolicy.RUNTIME)  
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.CONSTRUCTOR,ElementType.PARAMETER,ElementType.ANNOTATION_TYPE})  
@Constraint(validatedBy = EmailValidator.class)  
public @interface ValidEmail {  
   
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