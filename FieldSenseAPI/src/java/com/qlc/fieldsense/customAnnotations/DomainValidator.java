/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customAnnotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author anuja
 * @date 30/1/2013
 * @purpose custom domain validation
 */
public class DomainValidator implements ConstraintValidator<ValidDomain, String> {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("DomainValidator");

    /**
     *
     * @param domain
     */
    @Override
    public void initialize(ValidDomain domain) {
    }

    /**
     *
     * @param domain
     * @param context
     * @return
     */
    @Override
    public boolean isValid(String domain, ConstraintValidatorContext context) {
        if (domain.trim().length() == 0) {
            return true;
        }
        try {
            //String dom = domain.substring(domain.indexOf("@") + 1, domain.indexOf("."));
            String dom = domain.substring(domain.indexOf("@") + 1);
            if(dom.indexOf(".")!=-1)
                dom=dom.substring(0,dom.indexOf("."));
            List<String> domains = new ArrayList<String>();
            domains.add("gmail");
            domains.add("yahoo");
            domains.add("rediff");
            domains.add("hotmail");
            Iterator<String> itr = domains.iterator();
            while (itr.hasNext()) {
                String domtocheck = itr.next();
                if (dom.equalsIgnoreCase(domtocheck)) {
                    return false;
                }
            }
        } catch (Exception e) {
        }
        return true;
    }
}
