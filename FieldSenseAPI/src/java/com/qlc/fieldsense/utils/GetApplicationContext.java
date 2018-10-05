/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Ramesh
 */
public class GetApplicationContext {

    /**
     *
     */
    public static final ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
}
