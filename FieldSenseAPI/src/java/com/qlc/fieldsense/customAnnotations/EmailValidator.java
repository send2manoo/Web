/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customAnnotations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author anuja
 * @date 30/1/2013
 * @purpose custom email validation
 */
public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("EmailValidator");
    private static String EMAIL_PATTERN = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
    Pattern pattern;
    Matcher matcher;

    /**
     *
     * @param email
     */
    @Override
    public void initialize(ValidEmail email) {
//        pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
    }

    /**
     *
     * @param emailId
     * @param context
     * @return
     */
    @Override
    public boolean isValid(String emailId, ConstraintValidatorContext context) {
        int len = 0;
        len = emailId.trim().length();
        if (len == 0) {
            return true;
        }


        //To check email id contains "@" and "." or not
        if (emailId.contains("@") && emailId.contains(".")) {
            int count = StringUtils.countMatches(emailId, "@");
            if (count > 1) {
                return false;
            }
        } else {
            return false;
        }
        //Check email id ends with @ or .
        if (emailId.endsWith("@") || emailId.endsWith(".")) {
            return false;
        }

        String input = emailId;//"@sun.com";
        //Checks for email addresses starting with
        //inappropriate symbols like dots or @ signs.
        Pattern p = Pattern.compile("^\\.|^\\@");
        Matcher m = p.matcher(input);
        if (m.find()) {
            System.err.println("Email addresses don't start"
                    + " with dots or @ signs.");
            return false;
        }
        //Checks for email addresses that start with
        //www. and prints a message if it does.
        p = Pattern.compile("^www\\.");
        m = p.matcher(input);
        if (m.find()) {
   //         System.out.println("Email addresses don't start" + " with \"www.\", only web pages do.");
            return false;
        }
        p = Pattern.compile("[^A-Za-z0-9\\.\\@_\\-~#]+");
        m = p.matcher(input);
        StringBuffer sb = new StringBuffer();
        boolean result = m.find();
        boolean deletedIllegalChars = false;

        while (result) {
            deletedIllegalChars = true;
            m.appendReplacement(sb, "");
            result = m.find();
        }

        // Add the last segment of input to the new String
        m.appendTail(sb);

        input = sb.toString();
   //     System.out.println(input);
        if (deletedIllegalChars) {
    //        System.out.println("It contained incorrect characters"
      //              + " , such as spaces or commas." + input);
            // return "It contained incorrect characters, such as spaces or commas. Please try again.";
            return false;
        }
        //return "SUCCESS";
        return true;

    }
}
