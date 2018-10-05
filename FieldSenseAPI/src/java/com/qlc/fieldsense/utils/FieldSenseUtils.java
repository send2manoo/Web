/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.qlc.fieldsense.accounts.model.GoogleReCaptcha;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sql.DataSource;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Ramesh
 */
public class FieldSenseUtils {

    private JdbcTemplate jdbcTemplate;
    private static final Logger log4jLog = Logger.getLogger("FieldSenseUtils");

    /**
     *
     * @return
     */
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
     *
     * @param jdbcTemplate
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     *
     * @param gosocioDataSource
     * @param companyDataSource
     */
    public FieldSenseUtils(DataSource gosocioDataSource, DataSource companyDataSource) {
        try {
            jdbcTemplate = new JdbcTemplate(gosocioDataSource);
            log4jLog.info("FieldSenseUtils");
        } catch (Exception e) {
            log4jLog.info(" FieldSenseUtils " + e);
        }
    }

    /**
     *
     */
    public FieldSenseUtils() {
    }

    /**
     *
     * @param accountId
     * @return
     */
    public static JdbcTemplate getJdbcTemplateForAccount(int accountId) {
        try {
            ComboPooledDataSource dataSource = ((CommunityDataSourceManager) GetApplicationContext.ac.getBean("communityDataSourceManager")).getDataSource(accountId);
            JdbcTemplate jdbcTemplateCommunity = new JdbcTemplate(dataSource);            
            return jdbcTemplateCommunity;
        } catch (Exception e) {
            log4jLog.info(" getJdbcTemplateForAccount " + e);
            return null;
        }
    }

    /**
     *
     * @param accountId
     * @return
     */
    public static DataSource getDataSourceForAccount(int accountId) {
        try {
            DataSource dataSource = ((CommunityDataSourceManager) GetApplicationContext.ac.getBean("communityDataSourceManager")).getDataSource(accountId);
            return dataSource;
        } catch (Exception e) {
            log4jLog.info(" getDataSourceForAccount" + e);
            return null;
        }
    }

    /**
     * To get the property value for property file
     *
     * @author Ramesh
     * @param property
     * @return
     */
    public static String getPropertyValue(String property) {
        try {
     //       System.out.println("getPropertyValue method start .");
            Properties prop = new Properties();
            //get the path of tomcat's bin directory
            String path = (new File(".")).getCanonicalPath();
            //load the property file
//            System.out.println(" config path ." + path);
            prop.load(new FileInputStream(path + "/fieldSenseConfig.properties"));
            //get the property value of mail image path
            String propertyValue = prop.getProperty(property);
//            System.out.println("Poperty File Canonical Path: " + path + ", Property: " + property + " = " + propertyValue);
            log4jLog.info("Poperty File Canonical Path: " + path + ", Property: " + property + " = " + propertyValue);
            return propertyValue;
        } catch (IOException ex) {
            log4jLog.info(" getPropertyValue 104" + ex);
//             ex.printStackTrace();
            return "";
        } catch (Exception ex) {
            log4jLog.info(" getPropertyValue 108" + ex);
//           ex.printStackTrace();
            return "";
        }
    }
    /*
     * @Ramesh
     * @param emailAddress
     * @date 31-01-2014
     * @purpouse This method id used to get the userFirstName by passing email addres .
     */

    /**
     *
     * @param emailAddress
     * @return
     */
    public String getUserFirstName(String emailAddress) {
        String query = "SELECT first_name FROM users WHERE email_address=?";
        log4jLog.info(" getUserFirstName" + query);
        Object param[] = new Object[]{emailAddress};
        try {
            return jdbcTemplate.queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" getUserFirstName" + e);
//            e.printStackTrace();
            return null;
        }
    }
    //added by nikhil
     /**
     *
     * @param emailAddress
     * @return
     */
    public int getGDPRFlag(String emailAddress) {
        String query = "SELECT is_terms_condition_agreed FROM users WHERE email_address=?";
        log4jLog.info(" getUserFirstName" + query);
        Object param[] = new Object[]{emailAddress};
        try {
            return jdbcTemplate.queryForObject(query, param , Integer.class);
        } catch (Exception e) {
            log4jLog.info(" getUserFirstName" + e);
//            e.printStackTrace();
            return 0;
        }
    }
      //added by nikhil
     /**
     *
     * @param emailAddress
     * @return
     */
    public int getGDPRFlagMobileNo(String mobileNo) {
        String query = "SELECT is_terms_condition_agreed FROM users WHERE mobile_number like ?";
        log4jLog.info(" getUserFirstName" + query);
        Object param[] = new Object[]{"%"+mobileNo+"%"};
        try {
            return jdbcTemplate.queryForObject(query, param , Integer.class);
        } catch (Exception e) {
            log4jLog.info(" getUserFirstName" + e);
//            e.printStackTrace();
            return 0;
        }
    }
    //ended by nikhil
    
    /*
     * @Ramesh
     * @param emailAddress
     * @date 31-01-2014
     * @purpouse This method id used to get the userFirstName by passing email addres .
     */

    /**
     *
     * @param mobileno
     * @return
     */
    

    public String getUserFirstNameMobileNo(String mobileno) {
        String query = "SELECT first_name FROM users WHERE mobile_number=?";
        log4jLog.info(" getUserFirstName" + query);
        Object param[] = new Object[]{mobileno};
        try {
            return jdbcTemplate.queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" getUserFirstName" + e);
//            e.printStackTrace();
            return null;
        }
    }
    
    /*
     * @Ramesh
     * @param emailAddress
     * @date 31-01-2014
     * @purpouse This method id used to get the userLastName by passing email addres .
     */

    /**
     *
     * @param emailAddress
     * @return
     */
    

    public String getUserLastName(String emailAddress) {
        String query = "SELECT last_name FROM users WHERE email_address=?";
        log4jLog.info(" getUserLastName" + query);
        Object param[] = new Object[]{emailAddress};
        try {
            return jdbcTemplate.queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" getUserLastName" + e);
//            e.printStackTrace();
            return null;
        }
    }
    
    /*
     * @Ramesh
     * @param emailAddress
     * @date 31-01-2014
     * @purpouse This method id used to get the userLastName by passing email addres .
     */

    /**
     *
     * @param mobileno
     * @return
     */
    

    public String getUserLastNameMobileNo(String mobileno) {
        String query = "SELECT last_name FROM users WHERE mobile_number=?";
        log4jLog.info(" getUserLastName" + query);
        Object param[] = new Object[]{mobileno};
        try {
            return jdbcTemplate.queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" getUserLastName" + e);
//            e.printStackTrace();
            return null;
        }
    }
    
    /*
     * @Ramesh
     * @param emailAddress
     * @date 31-01-2014
     * @purpouse This method id used to get the userId by passing email addres .
     */

    /**
     *
     * @param emailAddress
     * @return
     */
    

    public int getUserId(String emailAddress) {
        String query = "SELECT id FROM users WHERE email_address=?";
        log4jLog.info(" getUserId " + query);
        Object param[] = new Object[]{emailAddress};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" getUserId " + e);
//            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     *
     * @param fullname
     * @return
     */
    public int getUserIdFrmFullName(String fullname) {
        String query = "SELECT id FROM users WHERE full_name=?";
        log4jLog.info(" getUserId " + query);
        Object param[] = new Object[]{fullname};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" getUserIdFrmFullName " + e);
//            e.printStackTrace();
            return 0;
        }
    }
    /*
     * @Ramesh
     * @param mobileno
     * @date 31-01-2014
     * @purpouse This method id used to get the userId by passing email addres .
     */

    /**
     *
     * @param mobileno
     * @return
     */
    public int getUserIdMobileNo(String mobileno) {
        String query = "SELECT id FROM users WHERE mobile_number=?";
        log4jLog.info(" getUserId " + query);
        Object param[] = new Object[]{mobileno};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" getUserId " + e);
//            e.printStackTrace();
            return 0;
        }
    }
    
     /*
     * @Ramesh
     * @param mobileno
     * @date 31-01-2014
     * @purpouse This method id used to get the userId by passing email addres .
     */

    /**
     *
     * @param userId
     * @return
     */
    

    public String getUserEmailAddress(int userId) {
        String query = "SELECT email_address FROM users WHERE id=?";
        log4jLog.info(" getUseemailid " + query);
        Object[] param = new Object[]{userId};
        try {
            return jdbcTemplate.queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" getUseremailid " + e);
            return "";
        }
    }
    
    /*
     * @Ramesh
     * @param emailAddress
     * @date 31-01-2014
     * @purpouse This method id used to get the userAccountId by passing email addres .
     */

    /**
     *
     * @param formName
     * @param accountid
     * @return
     */
    
      public int getFormId(String formName,int accountid) 
    {
        String query = "SELECT id FROM account_"+ accountid+".custom_forms WHERE form_name=?";
        Object param[] = new Object[]{formName};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" getUserId " + e);
//            e.printStackTrace();
            return 0;
        }
     }
      
    /**
     *
     * @param tableName
     * @param accountid
     * @return
     */
    public int getFilledFormId(String tableName,int accountid) 
        {
            String query = "select last_insert_id() as last_id from account_"+ accountid+"."+tableName;
            try {
                 return jdbcTemplate.queryForObject(query, Integer.class);
            } catch (Exception e) {
             log4jLog.info(" getUserId " + e);
//            e.printStackTrace();
            return 0;
        }
     }
      
    /**
     *
     * @param emailAddress
     * @return
     */
    public int getUserAccountId(String emailAddress) {
        String query = "SELECT account_id_fk FROM users WHERE email_address=?";
        log4jLog.info(" getUserAccountId " + query);
        Object param[] = new Object[]{emailAddress};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" getUserAccountId " + e);
//            e.printStackTrace();
            return 0;
        }
    }
    
    //Added by Mayank

    /**
     *
     * @param emailAddress
     * @return
     */
         public int getUserAccuracy(String emailAddress) {
        String query = "SELECT user_accuracy FROM users WHERE email_address=?";
        log4jLog.info(" getUserAccuracy " + query);
        Object param[] = new Object[]{emailAddress};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" getUserAccuracy " + e);
//            e.printStackTrace();
            return 0;
        }
    }
     
     //Added by Mayank

    /**
     *
     * @param emailAddress
     * @return
     */
         public int getCheckInRadius(String emailAddress) {
        String query = "SELECT check_in_radius FROM users WHERE email_address=?";
        log4jLog.info(" getCheckInRadius " + query);
        Object param[] = new Object[]{emailAddress};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" getCheckInRadius " + e);
//            e.printStackTrace();
            return 0;
        }
    }
     
     //Added by Mayank

    /**
     *
     * @param userId
     * @return
     */
         public int getUserAccuracybyId(int userId) {
        String query = "SELECT user_accuracy FROM users WHERE id=?";
        log4jLog.info(" getUserAccuracy " + query);
        Object param[] = new Object[]{userId};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" getUserAccuracy " + e);
//            e.printStackTrace();
            return 0;
        }
    }
     
     //Added by Mayank

    /**
     *
     * @param userId
     * @return
     */
         public int getCheckInRadiusbyId(int userId) {
        String query = "SELECT check_in_radius FROM users WHERE id=?";
        log4jLog.info(" getCheckInRadiusbyId " + query);
        Object param[] = new Object[]{userId};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" getCheckInRadiusbyId " + e);
//            e.printStackTrace();
            return 0;
        }
    }
     
    /**
     *
     * @param mobileno
     * @return
     */
    public int getUserAccountIdMobileNo(String mobileno) {
        String query = "SELECT account_id_fk FROM users WHERE mobile_number=?";
        log4jLog.info(" getUserAccountId " + query);
        Object param[] = new Object[]{mobileno};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" getUserAccountId " + e);
//            e.printStackTrace();
            return 0;
        }
    }

    /**
     * This method replaces characters to code
     *
     * @author Ramesh
     * @date 31-01-2014
     * @param data
     * @return
     */
    public static String replaceMe(String data) {
        data = data.replaceAll("=", "eq44l");
        data = data.replaceAll("\\+", "pl43s");
        data = data.replaceAll("/", "fsl42e");
        data = data.replaceAll("\\\\", "bsl41e");
        return data; 
    }

    /**
     * This methods returns characters in string for character code in provided
     * string
     *
     * @author Ramesh
     * @date 31-01-2014
     * @param data
     * @return
     */
    public static String replaceMeBack(String data) {
        data = data.replaceAll("eq44l", "=");
        data = data.replaceAll("pl43s", "+");
        data = data.replaceAll("fsl42e", "/");
        data = data.replaceAll("bsl41e", "\\\\");
        return data;
    }

    /**
     *
     * @author anuja
     * @param emailAddress
     * @return 
     * @date 4/2/2014
     * @purpose check email is unique
     */
    public int isEmailExist(String emailAddress) {
        String query = "SELECT count(id) FROM users WHERE email_address=?";
        log4jLog.info(" isEmailExist " + query);
        Object[] param = new Object[]{emailAddress};
        try {
            int count = jdbcTemplate.queryForObject(query, param, Integer.class);
            return count;
        } catch (Exception e) {
            log4jLog.info(" isEmailExist " + e);
            e.printStackTrace();
            return 0;
        }
    }
    //added by manohar
    public int isEmpCodeExist(String emp_code,int accountId) {
        String query = "SELECT count(id) FROM users WHERE emp_code=? AND account_id_fk=?";
        log4jLog.info(" isEmpCodeExist " + query);
        Object[] param = new Object[]{emp_code,accountId};
        try {
            int count = jdbcTemplate.queryForObject(query, param, Integer.class);
            return count;
        } catch (Exception e) {
            log4jLog.info(" isEmpCodeExist " + e);
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     *
     * @author shivakrishna
     * @param mobileno
     * @return 
     * @date 28/01/2016
     * @purpose check MobileNo is unique
     */
    //change in query done by nikhil :- Because of country code in mobile number
    public int isMobileExist(String mobileno) {
        if(mobileno.trim().equals("")){
            return 0;
        }
        String query = "SELECT count(id) FROM users WHERE mobile_number like ?";
        log4jLog.info(" isMobileExist " + query);
        Object[] param = new Object[]{"%"+mobileno};
        try {
            int count = jdbcTemplate.queryForObject(query, param, Integer.class);
            return count;
        } catch (Exception e) {
            log4jLog.info(" isEmailExist " + e);
            e.printStackTrace();
            return 0;
        }
    }

    /**
     *
     * @author anuja
     * @param companyName
     * @return 
     * @date 4/2/2014
     * @purpose check company name is unique
     */
    public int isCompanyNameExist(String companyName) {
        String query = "SELECT count(id) FROM accounts WHERE company_name=?";
        log4jLog.info(" isCompanyNameExist " + query);
        Object[] param = new Object[]{companyName};
        try {
            int count = jdbcTemplate.queryForObject(query, param, Integer.class);
            return count;
        } catch (Exception e) {
            log4jLog.info(" isCompanyNameExist " + e);
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     *
     * @author anuja
     * @param companyName
     * @param id
     * @return 
     * @date 4/2/2014
     * @purpose check company name is unique
     */
    public int isCompanyNameExistForEdit(String companyName,int id) {
        String query = "SELECT count(id) FROM accounts WHERE company_name=? and id!=?";
        log4jLog.info(" isCompanyNameExist " + query);
        Object[] param = new Object[]{companyName,id};
        try {
            int count = jdbcTemplate.queryForObject(query, param, Integer.class);
            return count;
        } catch (Exception e) {
            log4jLog.info(" isCompanyNameExist " + e);
//            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @author Ramesh
     * @param token
     * @return 
     * @date 19-02-2014
     * @purpose This method is used to check token is valid or not .
     */
    public boolean isTokenValid(String token) {
        String query = "SELECT count(id) FROM user_auth WHERE user_token=?";
        log4jLog.info(" isTokenValid " + query);
        Object[] param = new Object[]{token};
        try {
            if (jdbcTemplate.queryForObject(query, param, Integer.class) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isTokenValid " + e);
//            e.printStackTrace();
            return false;
        }
    }

     /**
     * @author Ramesh
     * @param token
     * @return 
     * @date 19-02-2014
     * @purpose This method is used to check token is valid or not .
     */
    public boolean isSessionExpired(String token) {
        String query = "SELECT createdOn FROM user_auth WHERE user_token=?";
        log4jLog.info(" isSessionExpired " + query);
        Object[] param = new Object[]{token};
        try {
     //       System.out.println("token==="+token);
            Timestamp createdOn=jdbcTemplate.queryForObject(query, param, Timestamp.class);
            //Date today = new java.util.Date();
           // Timestamp ts1 = new java.sql.Timestamp(today.getTime());
            //long tsTime1 = ts1.getTime();
            long tsTime1 = System.currentTimeMillis();
            long tsTime2 = createdOn.getTime();
            long diff=tsTime1-tsTime2;
     //       System.out.println("difference==="+diff);
            if(diff<=60000){
                return true;
            }else{
                return false;
            }
               
        } catch (Exception e) {
            log4jLog.info(" isSessionExpired " + e);
//            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * @author Ramesh
     * @param token
     * @return 
     * @date 19-02-2014
     * @purpose This method is used to userId by using token .
     */
    public int userIdForToken(String token) {
        String query = "SELECT user_id_fk FROM user_auth WHERE user_token=?";
        log4jLog.info(" userIdForToken " + query);
        Object[] param = new Object[]{token};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" userIdForToken " + e);
//            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @author Ramesh
     * @param userId
     * @return 
     * @date 19-02-2014
     * @purpose This method is used to check is user is admin or not .
     */
    public boolean isUserAdmin(int userId) {
        String query = "SELECT role FROM users WHERE id=?";
        log4jLog.info(" isUserAdmin " + query);
        Object[] param = new Object[]{userId};
        try { // modified by manohar
            if ((jdbcTemplate.queryForObject(query, param, Integer.class) == 1) || (jdbcTemplate.queryForObject(query, param, Integer.class) == 3) || (jdbcTemplate.queryForObject(query, param, Integer.class) == 6) || (jdbcTemplate.queryForObject(query, param, Integer.class) == 8)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isUserAdmin " + e);
//            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * @author Awaneesh
     * @param accountId
     * @return 
     * @date 20-04-2016
     * @purpose This method is used to count number of Admin user in an account .
     */
    public int totalAdminUserInAccount(int accountId) {
        int count=0;
        String query = "SELECT count(id) FROM users WHERE account_id_fk=? and role=1 and active=1";
        log4jLog.info(" isUserAdmin " + query);
        Object[] param = new Object[]{accountId};
        try {
            count=jdbcTemplate.queryForObject(query, param, Integer.class) ;
        } catch (Exception e) {
            log4jLog.info(" isUserAdmin " + e);
//            e.printStackTrace();
            return 0;
        }
        return count;
    }
    

    /**
     * @author Awaneesh
     * @param emailId
     * @return 
     * @date 21-04-2016
     * @purpose This method is used to check is user is admin or not .
     */
    public boolean isUserAdminUsingEmail(String emailId) {
        String query = "SELECT role FROM users WHERE email_address=?";
        log4jLog.info(" isUserAdmin " + query);
        Object[] param = new Object[]{emailId.trim()};
        try {
            if (jdbcTemplate.queryForObject(query, param, Integer.class) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isUserAdmin " + e);
//            e.printStackTrace();
            return false;
        }
    }
    /**
     * @author Ramesh
     * @param status
     * @param errorCode
     * @param errorMessage
     * @param resultType
     * @param result
     * @return 
     * @date 19-02-2014
     * @purpose This method is used to generate the response in fieldsense .
     */
    public static JSONObject generateFieldSenseResponse(String status, String errorCode, String errorMessage, String resultType, Object result) {
        JSONObject response = new JSONObject();
        response.put("status", status);
        response.put("errorCode", errorCode);
        response.put("errorMessage", errorMessage);
        response.put("resultType", resultType);
        response.put("result", result);
        return response;
    }
    
    
    /**
     * @author Ramesh
     * @param result
     * @return 
     * @date 19-02-2014
     * @purpose This method is used to generate the response in fieldsense .
     */
    public static JSONObject generateFieldSenseResponseForCustomersListInVisit(Object result) {
        JSONObject response = new JSONObject();
        response.put("result", result);
        return response;
    }
    
    /**
     * @author Ramesh
     * @param status
     * @param errorCode
     * @param resultlength
     * @param errorMessage
     * @param resultType
     * @param result
     * @return 
     * @date 19-02-2014
     * @purpose This method is used to generate the response in fieldsense .
     */
    public static JSONObject generateFieldSenseResponseReport(String status, String errorCode, String errorMessage, String resultType, Object result,int resultlength) {
        JSONObject response = new JSONObject();
        response.put("status", status);
        response.put("errorCode", errorCode);
        response.put("errorMessage", errorMessage);
        response.put("resultType", resultType);
        response.put("aaData", result);
        response.put("iTotalDisplayRecords", resultlength);
        response.put("iTotalRecords", resultlength);
        return response;
    }

    /**
     * @author Ramesh
     * @param userId
     * @return 
     * @date 20-02-2014
     * @purpose This method is used to check is user is valid or not .
     */
    public boolean isUserValid(int userId) {
        String query = "SELECT Count(id) FROM users WHERE id=?";
        log4jLog.info(" isUserValid " + query);
        Object[] param = new Object[]{userId};
        try {
            if (jdbcTemplate.queryForObject(query, param, Integer.class) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isUserValid " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * @author Anuja
     * @param token
     * @return 
     * @date 21-02-2014
     * @purpose This method is used to retrieve accountId by using token .
     */
    public int accountIdForToken(String token) {
        String query = "SELECT account_id_fk FROM fieldsense.user_auth WHERE user_token=?";
        log4jLog.info(" accountIdForToken " + query);
        Object[] param = new Object[]{token};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" accountIdForToken " + query);
//            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @author Ramesh
     * @param accountId
     * @return 
     * @date 20-02-2014
     * @purpose This method is used to check is account is valid or not .
     */
    public boolean isAccountValid(int accountId) {
        String query = "SELECT Count(id) FROM accounts WHERE id=?";
        log4jLog.info(" isAccountValid " + query);
        Object[] param = new Object[]{accountId};
        try {
            if (jdbcTemplate.queryForObject(query, param, Integer.class) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isAccountValid " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * @author Ramesh
     * @param customerId
     * @param accountId
     * @return 
     * @date 23-02-2014
     * @purpose This method is used to check is customer is valid or not .
     */
    public boolean isCustomerValid(int customerId, int accountId) {
        String query = "SELECT Count(id) FROM customers WHERE record_state !=3 AND id=?";
        log4jLog.info(" isCustomerValid " + query);
        Object[] param = new Object[]{customerId};
        try {
            if (getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isCustomerValid " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * @author anuja
     * @param accountId
     * @return 
     * @date 5-03-2014
     * @purpose This method is used to retrieve city based on accountId .
     */
    public String getCity(int accountId) {
        String query = "SELECT city FROM accounts WHERE id=?";
        Object[] param = new Object[]{accountId};
        try {
            String location = jdbcTemplate.queryForObject(query, param, String.class);
            return location;
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }
    /*
     *@author: anuja
     *@purpose: this method checks it contains only characters. 
     */

    /**
     *
     * @param data
     * @return
     */
    public static boolean isContainOnlyCharacters(String data) {
        Pattern p = Pattern.compile("[^a-z-]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(data);
        boolean b = m.find();
        b = (b == true) ? false : true;
        return b;
    }
    
        
    public static boolean isname(String data) {
        Pattern p = Pattern.compile("^[a-z.-]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(data);
        boolean b = m.find();
        b = (b == true) ? false : true;
        return b;
    }

    /*
     *@author:anuja
     *@purpose : this method checks it contain only numbers
     */

    /**
     *
     * @param data
     * @return
     */
    
    public static boolean isContainOnlyNumbers(String data) {
        Pattern p = Pattern.compile("[^0-9]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(data);
        boolean b = m.find();
        b = (b == true) ? false : true;
        return b;
    }

    /*
     *@author:anuja
     *@purpose:check emailis valid or not
     */

    /**
     *
     * @param emailId
     * @return
     */
    
    public static boolean isValidEmailAddress(String emailId) {
        //To check email id contains "@" and "." or not
        if (emailId.contains("@") && emailId.contains(".")) {
            int count = StringUtils.countMatches(emailId, "@");
            if (count > 1) {
                return false;
            }
        } else {
            //return "Invalid Email ID.";
            return false;
        }
        //Check email id ends with @ or .
        if (emailId.endsWith("@") || emailId.endsWith(".")) {
            //return "Invalid Email ID.";
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
            //return "Email addresses don't start with dots or @ signs. Please try again.";
            return false;
        }
        //Checks for email addresses that start with
        //www. and prints a message if it does.
        p = Pattern.compile("^www\\.");
        m = p.matcher(input);
        if (m.find()) {
    //        System.out.println("Email addresses don't start"+ " with \"www.\", only web pages do.");
            //return "Email addresses don't start with \"www.\". Please try again.";
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
    //    System.out.println(input);
        if (deletedIllegalChars) {
     //       System.out.println("It contained incorrect characters" + " , such as spaces or commas." + input);
            return false;
        }
        return true;
    }

    /*
     *@author: anuja
     * @purpose:public domain validation
     */

    /**
     *
     * @param emailId
     * @return
     */
    
    public static boolean isDomainValid(String emailId) {
        try {
//            String dom = emailId.substring(emailId.indexOf("@") + 1, emailId.indexOf("."));
            List<String> domains = new ArrayList<String>();
            domains.add("gmail");
            domains.add("yahoo");
            domains.add("rediff");
            domains.add("hotmail");
            domains.add("facebook");
            domains.add("live");
            domains.add("ymail");
            // String dom = emailId.substring(emailId.indexOf("@") + 1, emailId.lastIndexOf("."));

           String dom = emailId.substring(emailId.indexOf("@") + 1);
            if(dom.indexOf(".")!=-1)
                dom=dom.substring(0,dom.indexOf("."));
            
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

    /**
     *
     * @param password
     * @return
     */
    public static boolean isPasswordValid(String password) {
        if (!((password.length() > 5) && (password.length() < 21))) {
            return false;
        }
        Pattern p = Pattern.compile("^[a-zA-Z0-9!@#$.]+$");
        Matcher m = p.matcher(password);
        boolean b = m.find();
        return b;
    }

    /*
     *@ author :anuja
     * @purpose : to clear valid status for import
     */

    /**
     *
     * @param validStatus
     * @return
     */
    
    public static String notvalid(String validStatus) {
        if (validStatus.equals(Constant.VALID)) {
            return validStatus = "";
        }
        return validStatus;
    }

    /**
     * @author Ramesh
     * @param tableName
     * @param accountId
     * @return 
     * @date 06-03-2014
     * @purpose This method is used get max id in table .
     */
    public static int getMaxIdForTable(String tableName, int accountId) {
        String query = "SELECT MAX(id) FROM " + tableName;
        try {
            synchronized (FieldSenseUtils.class) {
                return getJdbcTemplateForAccount(accountId).queryForObject(query, Integer.class);
            }
        } catch (Exception e) {
            log4jLog.info(" getMaxIdForTable " + e);
            return 0;
        }
    }

    /*
     *@author : anuja
     *@purpose: check user is TL
     */

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    
    public boolean isUserTeamLead(int userId,int accountId) {
        String query = "SELECT has_subordinates FROM account_"+accountId+".teams WHERE user_id_fk=?";
        log4jLog.info(" isUserTL " + query);
        Object[] param = new Object[]{userId};
        try {
            int has_subordinates = jdbcTemplate.queryForObject(query, param, Integer.class);
            if ((has_subordinates == 1)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isUserTL " + e);
            return false;
        }
    }
    /**
     * @param userId
     * @param accountId
     * @return 
     * @purpose to is get TeamID of a user
    */
    public int getUserTeamID(int userId,int accountId) {
        String query = "SELECT id FROM accounts_"+accountId+".teams WHERE user_id_fk=?";
        log4jLog.info(" isUserTL " + query);
        Object[] param = new Object[]{userId};
        try {
            int userTeamId = jdbcTemplate.queryForObject(query, param, Integer.class);
            return userTeamId;
        } catch (Exception e) {
            log4jLog.info(" isUserTL " + e);
            return 0;
        }
    }

    /**
     * @author Ramesh
     * @param teamId
     * @param accountId
     * @return 
     * @date 14-03-2014
     * @purpose to is team is exist or not .
     */
    public boolean isTeamValid(int teamId, int accountId) {
        String query = "SELECT COUNT(id) FROM teams WHERE id=?";
        log4jLog.info(" isTeamValid " + query);
        Object[] param = new Object[]{teamId};
        try {
            if (getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isTeamValid " + e);
            return false;
        }
    }

    /*
     * @author: anuja
     * @purpose:to check team member is in team
     */

    /**
     *
     * @param teamId
     * @param userId
     * @param accountId
     * @return
     */
    
    public boolean isMemberInTeam(int teamId, int userId, int accountId) {
        String query = "SELECT  COUNT(id) FROM team_members WHERE team_id_fk=? AND user_id_fk=?";
        log4jLog.info(" isMemberInTeam " + query);
        Object[] param = new Object[]{teamId, userId};
        try {
            if (getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isMemberInTeam " + e);
            return false;
        }
    }
    
    /**
     * @param accountId
     * @return 
     * @purpose to get the Top Qrganisation Head Id.
     *
     */
     public int getOrganisationHeadId(int accountId) {
        String query = "select user_id_fk from teams WHERE id=?";
        log4jLog.info(" getUsersTeamId " + query);
        Object[] param = new Object[]{100000};
        try {
            return getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" getUsersTeamId " + e);
            return 0;
        }
    }

    /*
     * @author : anuja
     * @purpose: conver String to Timestamp
     */

    /**
     *
     * @param dateTime
     * @return
     */
    
     public Timestamp converDateToTimestamp(String dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        try {
            date = sdf.parse(dateTime);
            sqlDate = new java.sql.Date(date.getTime());
        } catch (ParseException ex) {            
            java.util.logging.Logger.getLogger(FieldSenseUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
//        java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
        java.sql.Timestamp timestamp = new java.sql.Timestamp(sqlDate.getTime());
        return timestamp;
    }
     
    /**
     *
     * @param dateTime
     * @return
     * @throws Exception
     */
    public Timestamp converDateToTimestampForCheckInOut(String dateTime) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());        
            date = sdf.parse(dateTime);
            sqlDate = new java.sql.Date(date.getTime());        
        java.sql.Timestamp timestamp = new java.sql.Timestamp(sqlDate.getTime());
//        System.out.println("converDateToTimestampForCheckInOut timestamp >> "+timestamp);
        return timestamp;
    }
    /*
     * @author : anuja
     * @purpose: conver String to Date
     */

    /**
     *
     * @param dateTime
     * @return
     */
    public Date converStringToDate(String dateTime) {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        try {
            date = sdf.parse(dateTime);
            sqlDate = new java.sql.Date(date.getTime());
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(FieldSenseUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
//        java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
//        java.sql.Timestamp timestamp = new java.sql.Timestamp(sqlDate.getTime());
        return sqlDate;
    }

    /*
     * @author : anuja
     * @purpose: conver String to Time
     */

    /**
     *
     * @param Time
     * @return
     */
    
    public Time converStringToTime(String Time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        try {
            Time time = new Time(sdf.parse(Time).getTime());
            return time;
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(FieldSenseUtils.class.getName()).log(Level.SEVERE, null, ex);
            return new Time(0);
        }
    }

    /**
     * @author Ramesh
     * @date 31-05-2014
     * @param customerId
     * @param accountId
     * @return customerrName
     */
    public String getCustomerName(int customerId, int accountId) {
        String query = "SELECT customer_name FROM customers WHERE record_state !=3 AND  id=?";
        Object[] param = new Object[]{customerId};
        try {
            return getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" getUsersTeamId " + e);
            return "";
        }
    }

    /**
     *
     * @author anuja
     * @param emailAddress
     * @return 
     * @date 5/6/2014
     * @purpose check email is unique
     */
    public boolean isEmailUnique(String emailAddress) {
        String query = "SELECT count(id) FROM users WHERE email_address=?";
        log4jLog.info(" isEmailUnique " + query);
        Object[] param = new Object[]{emailAddress};
        try {
            int c = jdbcTemplate.queryForObject(query, param, Integer.class);
            if (c == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isEmailUnique " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @author anuja
     * @param acoountId
     * @return 
     * @date 5/6/2014
     * @purpose retrieve account name
     */
    public String getAccountName(int acoountId) {
        String query = "SELECT company_name FROM fieldsense.accounts WHERE id=?";
        log4jLog.info(" getAccountName" + query);
        Object param[] = new Object[]{acoountId};
        try {
            return jdbcTemplate.queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" getAccountName" + e);
//            e.printStackTrace();
            return null;
        }
    }

    /**
     * @author Anuja
     * @param userId
     * @return 
     * @date 21-02-2014
     * @purpose This method is used to retrieve role of user .
     */
    public int roleOfUser(int userId) {
        String query = "SELECT role FROM users WHERE id=?";
        log4jLog.info(" accountIdForToken " + query);
        Object[] param = new Object[]{userId};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" accountIdForToken " + query);
//            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @author Ramesh
     * @param token
     * @return 
     * @date 26-06-2014
     * @purpose This method is used to get Email Id by using token .
     */
    public String userEmailIdForToken(String token) {
        String query = "SELECT user_email_address FROM user_auth WHERE user_token=?";
        log4jLog.info(" userEmailIdForToken " + query);
        Object[] param = new Object[]{token};
        try {
            return jdbcTemplate.queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" userEmailIdForToken " + e);
//            e.printStackTrace();
            return null;
        }
    }
        
    /*
     * @author : anuja
     * @purpose : to retrive team menberes for teamid
     */

    /**
     *
     * @param teamId
     * @param accountId
     * @return
     */
    
    public List<Integer> geTeamMembers(int teamId, int accountId) {
        String query = "SELECT user_id_fk FROM team_members WHERE team_id_fk = ?";
        log4jLog.info(" geTeamMembers " + query);
        Object[] param = new Object[]{teamId};
        try {
            return getJdbcTemplateForAccount(accountId).queryForList(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" geTeamMembers " + e);
            return new ArrayList();
        }
    }

    /**
     * @author Ramesh
     * @param teamId
     * @param accountId
     * @return 
     * @date 14-03-2014
     * @purpose to is to get Member reporting to the TL
     *
     * */
    public List<Integer> getMembersOFTL(int teamId, int accountId) {
        String query = "SELECT user_id_fk FROM team_members WHERE team_id_fk = ? and member_type='3'";
        log4jLog.info(" geTeamMembers " + query);
        Object[] param = new Object[]{teamId};
        try {
            return getJdbcTemplateForAccount(accountId).queryForList(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" getMembersOFTL " + e);
            return new ArrayList();
        }
    }

    
      /*
     * @author Ramesh
     * @date 08-07-2014
     * @purpouse This method is used to check user id active or not
     * @param emailId
     * @return
     */

    /**
     *
     * @param emailId
     * @return
     */
    
    public boolean isUserActive(String emailId) {
        String query = "SELECT active from users WHERE email_address=?";
        log4jLog.info(" isUserActive " + query);
        Object[] param = new Object[]{emailId};
        try {
            if (jdbcTemplate.queryForObject(query, param, Integer.class) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isUserActive " + e);
            return false;
        }
    }
    
    public int isAccountActive(String emailId){
        String query ="select account_id_fk from users where email_address = ?";
        Object[] param = new Object[]{emailId};
        
        try{
            return jdbcTemplate.queryForObject(query, param, Integer.class);
        }
        catch(Exception e)
        {
            return 0;
        }
    }
    
     /*
     * @author Ramesh
     * @date 08-07-2014
     * @purpouse This method is used get the email address
     * @param mobileno
     * @return
     */

    /**
     *
     * @param mobileno
     * @return
     */
    
    public String getUserEmailId(String mobileno) {
        String query = "SELECT email_address FROM fieldsense.users where mobile_number=?";
        log4jLog.info(" email address " + query);
        Object[] param = new Object[]{mobileno};
        try {
            return jdbcTemplate.queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" get email address " + e);
            return "";
        }
    }
    
    // Modified by manohar
     public String getUserEmpCode(String emp_code,int accountId) { 
        String query = "SELECT email_address FROM fieldsense.users where emp_code=? AND account_id_fk=?";
        log4jLog.info(" getUserEmpCode " + query);
        Object[] param = new Object[]{emp_code,accountId};
        try {
            return jdbcTemplate.queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" getUserEmpCode " + e);
            return "";
        }
    }
    /**
     * @author shivakrishna
     * @date 28-01-2016
     * @purpouse This method is used to check user id active or not
     * @param mobileno
     * @return
     */
    public boolean isUserActiveMobileNo(String mobileno) {
        String query = "SELECT distinct(active) from users WHERE mobile_number=? limit 1";
        log4jLog.info(" isUserActiveMobile " + query);
        Object[] param = new Object[]{mobileno};
        try {
            if (jdbcTemplate.queryForObject(query, param, Integer.class) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isUserActive " + e);
            return false;
        }
    }

    /**
     * @author Ramesh
     * @param userId
     * @date 08-08-2014
     * @purpouse This method is used to check user id active or not
     * @param emailId
     * @return
     */
    public boolean isUserActive(int userId) {
        String query = "SELECT active from users WHERE id=?";
        log4jLog.info(" isUserActive " + query);
        Object[] param = new Object[]{userId};
        try {
            if (jdbcTemplate.queryForObject(query, param, Integer.class) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isUserActive " + e);
            return false;
        }
    }

    
            /**
     * @author shivakrishna
     * @date 10-03-2016
     * @purpouse This method is used to check user role
     * @param emailId
     * @return
     */
    public int getUserRole(String emailId) {
        String query = "SELECT role from users WHERE email_address=?";
        log4jLog.info(" user role " + query);
        Object[] param = new Object[]{emailId};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class);                  
        } catch (Exception e) {
            log4jLog.info(" user role " + e);
            return 0;
        }
    }
    
          /**
 * @author shivakrishna
     * @param mobileno
     * @date 10-03-2016
     * @purpouse This method is used to check user role
     * @param emailId
     * @return
     */
    public int getUserRoleMobileNo(String mobileno) {
        String query = "SELECT role from users WHERE mobile_number=?";
        log4jLog.info(" user role " + query);
        Object[] param = new Object[]{mobileno};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class);                  
        } catch (Exception e) {
            log4jLog.info(" user role " + e);
            return 0;
        }
    }
    
    
    /*
     * @author anuja
     * @date 8/7/2014
     * @purpose retrive user limit for account
     */

    /**
     *
     * @param accountId
     * @return
     */
    
    public int userLimitForAccount(int accountId) {
        String query = "SELECT user_limit FROM accounts WHERE id=?";
        log4jLog.info(" userLimitForAccount " + query);
        Object[] param = new Object[]{accountId};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" userLimitForAccount " + e);
            return 0;
        }
    }
    /*
     * @author anuja
     * @date 8/7/2014
     * @purpose retrive total users for acoount
     */

    /**
     *
     * @param accountId
     * @return
     */
    public int totalUsersInAccount(int accountId) {
        String query = "SELECT COUNT(id) FROM users WHERE account_id_fk=? and role!=0";
        log4jLog.info(" totalUsersInAccount " + query);
        Object[] param = new Object[]{accountId};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" totalUsersInAccount " + e);
            return 0;
        }
    }
    
    /**
     *
     * @param accountId
     * @return
     */
    public int totalUsersInAccountRoleNotZero(int accountId) {
        String query = "SELECT COUNT(id) FROM users WHERE account_id_fk=? and role != 0";
        log4jLog.info(" totalUsersInAccount " + query);
        Object[] param = new Object[]{accountId,};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" totalUsersInAccount " + e);
            return 0;
        }
    }

    /**
     * @author Ramesh
     * @param userId
     * @return 
     * @date 16-07-2014
     * @purpose This method is used to check is user is admin or super admin .
     */
    public boolean isUserAdminOrSupport(int userId) {
        String query = "SELECT role FROM users WHERE id=?";
        log4jLog.info(" isUserAdmin " + query);
        Object[] param = new Object[]{userId};
        try {
            int role = jdbcTemplate.queryForObject(query, param, Integer.class);
           if ((role == 1) || (role == 3) || (role == 6) || (role == 8) || (role == 0)) {  // modified by manohar
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isUserAdmin " + e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * @author Ramesh
     * @date 18-07-2014
     * @param userId
     * @purpose to check is user is having authority to login from web .
     * @return
     */
    public boolean isUserAuthorisedToLogin(int userId) {
        String query = "SELECT role FROM users WHERE id=?";
        log4jLog.info(" isUserTL " + query);
        Object[] param = new Object[]{userId};
        try {
            int role = jdbcTemplate.queryForObject(query, param, Integer.class);
            if ((role == 2) || (role == 1) || (role == 0)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isUserTL " + e);
            return false;
        }
    }

    /**
     * @author Ramesh
     * @date 18-07-2014
     * @param userId
     * @purpose This method is used to check is user is super admin .
     * @return
     */
    public boolean isUserSuperAdmin(int userId) {
        String query = "SELECT role FROM users WHERE id=?";
        log4jLog.info(" isUserTL " + query);
        Object[] param = new Object[]{userId};
        try {
            int role = jdbcTemplate.queryForObject(query, param, Integer.class);
            if (role == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isUserTL " + e);
            return false;
        }
    }

    /**
     * @author Ramesh
     * @date 05-08-2014
     * @purpose To get the user designation
     * @param userId
     * @return Designation
     */
    public String getUserDesignation(int userId) {
        String query = "SELECT designation FROM users WHERE id=?";
        log4jLog.info(" getUserDesignation " + query);
        Object[] param = new Object[]{userId};
        try {
            return jdbcTemplate.queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" getUserDesignation " + e);
            return "";
        }
    }

    /**
     * @author Ramesh
     * @date 05-08-2014
     * @purpose To get the user gender
     * @param userId
     * @return Gender
     */
    public int getUserGender(int userId) {
        String query = "SELECT gender FROM users WHERE id=?";
        log4jLog.info(" getUserDesignation " + query);
        Object[] param = new Object[]{userId};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" getUserDesignation " + e);
            return 0;
        }
    }

    /**
     * @author Ramesh
     * @date 10-09-2014
     * @purpose To get the user mobile no .
     * @param userId
     * @return mobile no 
     */
    public String getUserMobileNo(int userId) {
        String query = "SELECT mobile_number FROM users WHERE id=?";
        log4jLog.info(" getUserMobileNo " + query);
        Object[] param = new Object[]{userId};
        try {
            return jdbcTemplate.queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" getUserMobileNo " + e);
            return "";
        }
    }

    /**
     * @author Ramesh
     * @date 22-09-2014
     * @purpose To get the teamId as team lead for user  .
     * @param userId
     * @param accountId
     * @return teamId 
     */
    public int selectUsersTeamId(int userId, int accountId) {
        String query = "select id from teams WHERE user_id_fk=?";
        log4jLog.info(" selectUsersTeamId " + query);
        Object[] param = new Object[]{userId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" selectUsersTeamId " + e);
            return 0;
        }
    }
    
    /**
     * @author Ramesh
     * @date 29-10-2014
     * @purpose To check is user is registered or not .
     * @param emailId
     * @return boolean
     */
    public boolean isUserRegisterd(String emailId) {
        String query = "SELECT count(id) FROM users WHERE email_address=?";
        log4jLog.info(" isUserRegisterd " + query);
        Object[] param = new Object[]{emailId};
        try {
            if (jdbcTemplate.queryForObject(query, param, Integer.class) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isUserRegisterd " + e);
            return false;
        }
    }

    /**
     *
     * @param fullName
     * @param accountId
     * @return
     */
    public boolean isReportingHeadValid(String fullName,int accountId) {
        String query = "SELECT full_name FROM users WHERE full_name=? and account_id_fk=?";
        log4jLog.info(" isUserRegisterd " + query);
        Object[] param = new Object[]{fullName,accountId};
        try {
            String full_name= jdbcTemplate.queryForObject(query, param,String.class);
            if(full_name.equals(full_name))
            {
             return true;
            } else {
                return false;
            }
              
        } catch (Exception e) {
            log4jLog.info(" validateFullName " + e);
            return false;
        }
    }
    /**
     * @author Ramesh
     * @date 08-01-2015
     * @purpose To get push notification sender id  .
     * @param userId
     * @return mobile no
     */
    public String getUserGcmSenderId(int userId) {
        String query = "SELECT IFNULL(gcm_id,0) FROM users WHERE id=?";
        log4jLog.info(" getUserGcmSenderId " + query);
        Object[] param = new Object[]{userId};
        try {
            return jdbcTemplate.queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" getUserGcmSenderId " + e);
            return "";
        }
    }
    
    /**
     * @author Awaneesh
     * @date 25-10-2016
     * @purpose To get push notification sender id  .
     * @param userId
     * @return Map having gcmID and os
     */
    public HashMap getGcmUserInfo(int userId) {
//        System.out.println("userId : "+userId);
        String query = "SELECT IFNULL(gcm_id,0) as gcmID,device_os,first_name,last_name, app_version FROM users WHERE id=?"; // app_version, Added by jyoti
        log4jLog.info(" getGcmUserInfo " + query);
        Object[] param = new Object[]{userId};
        try {
            return jdbcTemplate.queryForObject(query,param,new RowMapper<HashMap>(){
              @Override
                public HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    HashMap map=new HashMap();
                    map.put("gcmId",rs.getString("gcmID"));
                    map.put("deviceOS",rs.getInt("device_os"));
                    map.put("firstName",rs.getString("first_name"));
                    map.put("lastName",rs.getString("last_name"));
                    map.put("appVersion",rs.getInt("app_version")); // app_version, Added by jyoti
                    return map;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info(" getGcmUserInfo " + e.getMessage());
            return new HashMap();
        }
    }
    
    

    /**
     * @author Ramesh
     * @date 08-01-2015
     * @purpose To get user first name by using user id .
     * @param userId
     * @return mobile no
     */
    public String getUserFirstName(int userId) {
        String query = "SELECT first_name FROM users WHERE id=?";
        log4jLog.info(" getUserFirstName" + query);
        Object param[] = new Object[]{userId};
        try {
            return jdbcTemplate.queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" getUserFirstName" + e);
//            e.printStackTrace();
            return null;
        }
    }

    /**
     * @author Ramesh
     * @date 08-01-2015
     * @purpose To get user last name by using user id.
     * @param userId
     * @return mobile no
     */
    public String getUserlastName(int userId) {
        String query = "SELECT last_name FROM users WHERE id=?";
        log4jLog.info(" getUserFirstName" + query);
        Object param[] = new Object[]{userId};
        try {
            return jdbcTemplate.queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" getUserlastName" + e);
//            e.printStackTrace();
            return null;
        }
    }

    /**
     * @author Ramesh
     * @date 16-04-2015
     * @purpose To get the all accounts which are presented in the applications .
     * @return list of account ids .
     */
    public List<Integer> getAllAccountIds() {
        String query = "SELECT id FROM accounts";
        log4jLog.info("getAllAccountIds" + query);
        try {
            return jdbcTemplate.queryForList(query, Integer.class);
        } catch (Exception e) {
//            e.printStackTrace();
            log4jLog.info("getAllAccountIds" + query);
            return new ArrayList<Integer>();
        }
    }

    /**
     * @author Awaneesh
     * @date 06-09-2016
     * @purpose To get the App version a user is using  .
     * @param userId
     * @param accountId
     * @return teamId 
     */
    public int selectAppVersionForUser(int userId) {
        String query = "SELECT app_version FROM users WHERE id=?";
        log4jLog.info("selectAppVersionForUser getUserId " + query);
        Object param[] = new Object[]{userId};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" getUserId " + e);
//            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     *
     * @param accountId
     * @return
     */
    public int calculateActivityFrequency(int accountId)
    { 
       int noOfPunchIn=0;
       int totalNoOfUsers=0;
       int activeFrequency=0;
        String query = "SELECT count(punch_date) FROM attendances where punch_date BETWEEN DATE_SUB(curdate(),INTERVAL 1 MONTH) AND curdate();";
        String query1= "SELECT count(id) FROM fieldsense.users where account_id_fk=?";
        log4jLog.info("Inside FieldSenseStatsUtils class  isStatRecordAvailableForTheDate() method" + query);
        Object param[] = new Object[]{accountId};
        Object param1[] = new Object[]{accountId};
        try {
            noOfPunchIn=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, Integer.class);
            totalNoOfUsers=jdbcTemplate.queryForObject(query1, param1, Integer.class);
            activeFrequency=(noOfPunchIn*100)/(totalNoOfUsers*30);
        } catch (Exception e) {
//            e.printStackTrace();
            log4jLog.info("Calculate Activity Frequency" + e);
            return 0;
        }
        return activeFrequency;
    }
    
    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    public int getExpenseStatus(int id,int accountId){
        String query = "SELECT status FROM expenses where id=?";
        log4jLog.info("get status" + query);
        Object param[] = new Object[]{id};
        try {
            return getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
        } catch (Exception e) {
//            e.printStackTrace();
            log4jLog.info("get status" + query);
            return 0;
        }
    }
    
    /**
     *
     * @param accountId
     * @return
     */
    public boolean isTimeoutAllowedForAllUsersInAccount(int accountId){
        String query = "SELECT count(id) FROM users where account_id_fk=? and allow_timeout !=1";
        log4jLog.info("get isTimeoutAllowedForAllUsersInAccount" + query);
        Object param[] = new Object[]{accountId};
        try {
            int count= jdbcTemplate.queryForObject(query, param, Integer.class);
            if(count==0){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
//            e.printStackTrace();
            log4jLog.info("get isTimeoutAllowedForAllUsersInAccount" + query);
            return false;
        }
    }
   
//   public boolean isOfflineAllowedForAllUsersInAccount(int accountId){
//        String query = "SELECT count(id) FROM users where account_id_fk=? and allow_offline =1";
//        log4jLog.info("get isOfflineAllowedForAllUsersInAccount" + query);
//        Object param[] = new Object[]{accountId};
//        try {
//            int count= jdbcTemplate.queryForObject(query, param, Integer.class);
//            if(count==0){
//                return true;
//            }else{
//                return false;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            log4jLog.info("get isOfflineAllowedForAllUsersInAccount" + query);
//            return false;
//        }
//    }
//   
   
    /**
     *
     * @param accountId
     * @return
     */
       
   public boolean allowTimeoutForAllUsersInAccount(int accountId){
        String query = "update users set allow_timeout=1 where account_id_fk=? and allow_timeout !=1";
        log4jLog.info("get isTimeoutAllowedForAllUsersInAccount" + query);
        Object param[] = new Object[]{accountId};
        try {
            jdbcTemplate.update(query, param);
            return true;
        } catch (Exception e) {
//            e.printStackTrace();
            log4jLog.info("get allowTimeoutForAllUsersInAccount" + query);
            return false;
        }
    }
   
   // added by jyoti 21-12-2016

    /**
     *
     * @param accountId
     * @return
     */
        public boolean allowOfflineForAllUsersInAccount(int accountId){
        String query = "update users set allow_offline=1 where account_id_fk=? AND allow_offline =0";
        log4jLog.info("get allowOfflineForAllUsersInAccount" + query);
        Object param[] = new Object[]{accountId};
        try {
            jdbcTemplate.update(query, param);
            return true;
        } catch (Exception e) {
//            e.printStackTrace();
            log4jLog.info("get allowOfflineForAllUsersInAccount" + query);
            return false;
        }
    }
    
    // ended by jyoti
    
    /**
     * @author Ramesh
     * @param url
     * @date 28-02-2015
     * @purpose To make a call to an external system URL .
     */
    public int fieldSenseUrlCall(String url) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            //add reuqest header
            con.setRequestMethod("GET");
//            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
//                System.out.println("fieldSenseUrlCall >> " + response.toString());
                if(response.toString().contains(":")){
//                    System.out.println("array 1 >> "+response.toString());
                } else {
//                    System.out.println("array 0 >> "+response.toString());
                    responseCode = -1;
                }
                
            } else {
//                System.out.println("POST request not worked"+responseCode);
            }
            return responseCode;
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info("fieldSenseUrlCall >>> " + e);
            return -1;

        }
    }

    /**
     * @return 
     * @Purpose for google reCaptcha response
     * @Added by jyoti, 18-02-2018 
     * @param googleReCaptcha
     * @METHOD: POST
        POST Parameter          Description
        secret      Required.   The shared key between your site and reCAPTCHA.
        response    Required.   The user response token provided by reCAPTCHA, verifying the user on your site.
        remoteip    Optional.   The user's IP address.
     */
    public boolean googleReCaptchaURLCall(GoogleReCaptcha googleReCaptcha) {
        try {
            URL obj = new URL(googleReCaptcha.getUrl());
            
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();            
            con.setRequestMethod("POST"); //add reuqest header
//            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            
            con.setDoOutput(true);
            String postParameters = Constant.GOOGLE_RECAPTCHA_DOMAIN_SECRET_KEYNAME + "=" + googleReCaptcha.getDomainSecretKey() + "&" + Constant.GOOGLE_RECAPTCHA_USER_RESPONSE_KEYNAME + "=" + googleReCaptcha.getUserResponse();
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParameters); // send post parameters
            wr.flush();
            wr.close();
            
            int responseCode = con.getResponseCode();
            
//            System.out.println("\nSending 'POST' request to URL : " + googleReCaptcha.getUrl());
//            System.out.println("Post parameters : " + postParameters);
//            System.out.println("Response Code : " + responseCode);

//            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(con.getInputStream()));
//            String inputLine;
//            StringBuilder response = new StringBuilder();
//
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//            // print result
//            System.out.println("response : " + response.toString());

            if(responseCode == 200){ // 200 http success OK code
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info("googleReCaptchaURLCall >>> " + e);
            return false;
        }
    }

    /**
     *
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */
    public static double travelDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        //distance in kilometers
        dist = dist * 1.609344;
        return (dist);
    }
    
    /**
     * <p>This function converts decimal degrees to radians.</p>
     *
     * @param deg - the decimal to convert to radians
     * @return the decimal converted to radians
     */
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /**
     * <p>This function converts radians to decimal degrees.</p>
     *
     * @param rad - the radian to convert
     * @return the radian converted to decimal degrees
     */
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }   
    
    //Siddhesh pande
    //checking for user account active or not
    //Ticket no:-#11452

    /**
     *
     * @param emailId
     * @return
     */
        public boolean isUserAccountActive(String emailId){
        String query = "SELECT acc.status from fieldsense.users u left join fieldsense.accounts acc on u.account_id_fk=acc.id where u.email_address=?";
        log4jLog.info(" isUserAccountActive " + query);
        Object[] param = new Object[]{emailId};
        try {
     int status=jdbcTemplate.queryForObject(query, param, Integer.class);    
     if(status==0)
     {
         return false;
     }
     else
     {
         return true;
         
     }         
        } catch (Exception e) {
            log4jLog.info(" isUserActive " + e);
            
        }
   
  
    return false;
    
}
    
     //Siddhesh pande
    //checking for user account active or not
    //Ticket no:-#11452

    /**
     *
     * @param mobileNO
     * @return
     */
         public boolean isUserAccountActiveMobileNo(String mobileNO){
        String query = "SELECT acc.status from fieldsense.users u left join fieldsense.accounts acc on u.account_id_fk=acc.id where u.mobile_number=?";
        log4jLog.info(" isUserAccountActiveMobileNo " + query);
        Object[] param = new Object[]{mobileNO};
        try {
     int status=jdbcTemplate.queryForObject(query, param, Integer.class);    
     if(status==0)
     {
         return false;
     }
     else
     {
         return true;
         
      }  
            
        } catch (Exception e) {
            log4jLog.info(" isUserActive " + e);
            
        }
  return false;
    
}
     
     
      /*
     * @author : Siddhesh
     * @purpose: conver String to Timestamp
     */

    /**
     *
     * @param dateTime
     * @return
     */
    
   static  public Timestamp converDateToTimestampforBatchUpdate(String dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        try {
            date = sdf.parse(dateTime);
            sqlDate = new java.sql.Date(date.getTime());
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(FieldSenseUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
//        java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
        java.sql.Timestamp timestamp = new java.sql.Timestamp(sqlDate.getTime());
        return timestamp;
    }

    public int isPhoneNumber(String com_Phone_number_string) {
       String query = "SELECT count(id) FROM accounts WHERE company_phone_number1=?";
        log4jLog.info(" isPhoneNumber " + query);
        Object[] param = new Object[]{com_Phone_number_string};
        try {
            int count = jdbcTemplate.queryForObject(query, param, Integer.class);
            return count;
        } catch (Exception e) {
            log4jLog.info(" isPhoneNumber " + e);
            e.printStackTrace();
            return 0;
        }
    }

    public Long getAccountPhoneNo(int id) {
        String query = "SELECT company_phone_number1 FROM accounts WHERE id=?";
        log4jLog.info(" isPhoneNumber " + query);
        Object[] param = new Object[]{id};
        try {
            return jdbcTemplate.queryForObject(query, param, Long.class);
            
        } catch (Exception e) {
            log4jLog.info(" isPhoneNumber " + e);
//            e.printStackTrace();
            return 0L;
        }
    }
    
    /**
     * @Added by Jyoti, 17-dec-2017
     * @param userId
     * @param accountId
     * @return 
     */
    public java.util.HashMap getLastPunchInOutData(int userId, int accountId) {
//        String query = "SELECT IFNULL(punch_in,'00:00:00') punch_in,IFNULL(punch_out,'00:00:00') punch_out FROM attendances WHERE user_id_fk = ? ORDER BY id DESC LIMIT 1,1"; 
        String query = "SELECT IFNULL(punch_in,'00:00:00') punch_in,IFNULL(punch_out,'00:00:00') punch_out, IFNULL(punch_date,'1111:11:11') punch_date, IFNULL(punch_out_date,'1111:11:11') punch_out_date FROM attendances WHERE user_id_fk = ? ORDER BY id DESC LIMIT 1 ";
        log4jLog.info(" getSecondLastPunchInOutData for userId : "+userId);
        Object[] param = new Object[]{userId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, new RowMapper<java.util.HashMap>() {
                @Override
                public java.util.HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    java.util.HashMap hMap = new java.util.HashMap();
                    hMap.put("secondLastPunchIn", rs.getString("punch_in"));
                    hMap.put("secondLastPunchOut", rs.getString("punch_out"));
                    hMap.put("secondLastPunchInDate", rs.getString("punch_date"));
                    hMap.put("secondLastPunchOutDate", rs.getString("punch_out_date"));
                    return hMap;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" getSecondLastPunchInOutData for userId : "+userId+", Exception : "+e);
            e.printStackTrace();
            return new java.util.HashMap();
        }
    }
    
    /**
     * @Added by Jyoti, 17-dec-2017
     * @param userId
     * @param accountId
     * @return 
     */
    public java.util.HashMap getLastCheckInOutData(int userId, int accountId) {
        String query = "SELECT id, IFNULL(check_in_time,'0') check_in_time, IFNULL(check_out_time,'0') check_out_time FROM appointments WHERE assigned_id_fk = ? ORDER BY id DESC LIMIT 1 ";
        log4jLog.info(" getSecondLastCheckInOutData for userId : "+userId);
        Object[] param = new Object[]{userId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, new RowMapper<java.util.HashMap>() {
                @Override
                public java.util.HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    java.util.HashMap hMap =new  java.util.HashMap();
                    hMap.put("secondLastCheckIn", rs.getString("check_in_time"));
                    hMap.put("secondLastCheckOut", rs.getString("check_out_time"));
                    return hMap;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" getSecondLastCheckInOutData for userId : "+userId+", Exception : "+e);
            e.printStackTrace();
            return new java.util.HashMap();
        }
    }
    
    /**
     * @Added by Jyoti, 17-dec-2017
     * @param userId
     * @param accountId
     * @return 
     */
    public java.util.HashMap getLastVisitStatus(int userId, int accountId) {
        String query = "SELECT id, customer_id_fk FROM appointments WHERE assigned_id_fk = ? AND status = 2 ORDER BY id DESC LIMIT 1 ";
        log4jLog.info(" getSecondLastVisitStatus for userId : "+userId);
        Object[] param = new Object[]{userId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, new RowMapper<java.util.HashMap>() {
                @Override
                public java.util.HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    java.util.HashMap hMap =new  java.util.HashMap();
                    hMap.put("secondLastId", rs.getInt("id"));
                    hMap.put("secondLastCustId", rs.getInt("customer_id_fk"));
                    return hMap;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" getSecondLastVisitStatus for userId : "+userId+", Exception : "+e);
            e.printStackTrace();
            return new java.util.HashMap();
        }
    }
   
    /**
     * @Added by Jyoti, 16-dec-2017
     * @param mobileNumber
     * @return 
     * @purpose to get userId , accountId using mobile number
     */
    public java.util.HashMap getUserIdAccountIdBasedOnMobileNo(String mobileNumber){        
        String query = "SELECT id, account_id_fk FROM users WHERE mobile_number = ?";
        log4jLog.info(" getUserIdAccountIdBasedOnMobileNo for : " + mobileNumber);
        Object param[] = new Object[]{mobileNumber};
        try {
            return this.jdbcTemplate.queryForObject(query, param, new RowMapper<java.util.HashMap>() {
                @Override
                public java.util.HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    java.util.HashMap userHashMap = new java.util.HashMap();
                    userHashMap.put("userId", rs.getInt("id"));
                    userHashMap.put("accountId", rs.getInt("account_id_fk"));
                    return userHashMap;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" getUserIdAccountIdBasedOnMobileNo for : " + mobileNumber + ", exception : "+e);
            e.printStackTrace();
            return new java.util.HashMap();
        }
    }
   
    /**
     * @Added by Jyoti, 16-dec-2017
     * @param emailAddress
     * @return 
     * @purpose to get userId , accountId using email address
     */
    public java.util.HashMap getUserIdAccountIdBasedOnEmailAddress(String emailAddress){
        String query = "SELECT id, account_id_fk FROM users WHERE email_address = ?";
        log4jLog.info(" getUserIdAccountIdBasedOnEmailAddress for : " + emailAddress);
        Object param[] = new Object[]{emailAddress};
        try{
            return this.jdbcTemplate.queryForObject(query, param, new RowMapper<java.util.HashMap>() {
                public java.util.HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    java.util.HashMap userHashMap = new java.util.HashMap();
                    userHashMap.put("userId", rs.getInt("id"));
                    userHashMap.put("accountId", rs.getInt("account_id_fk"));
                    return userHashMap;
                }
            });
        } catch(Exception e){
            log4jLog.info(" getUserIdAccountIdBasedOnEmailAddress for : " + emailAddress + ", Exception :"+e);
            e.printStackTrace();
            return new java.util.HashMap();
        }
    }
    
    public java.util.HashMap getDateTime(){        
        try {
            java.util.HashMap hMap = new java.util.HashMap();
            
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(); Date date1 = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR_OF_DAY, 24);
            date = calendar.getTime();
            String dateTimeAfter24Hours = parser.format(date);
            String currentDateTime = parser.format(date1);
            
            hMap.put("currentDateTime", currentDateTime);
            hMap.put("dateTimeAfter24Hours", dateTimeAfter24Hours);
            
            return hMap;
        } catch (Exception ex) {
            log4jLog.info("getDateTime" + ex);
            ex.printStackTrace();
            return new java.util.HashMap();
        }
    }
    
    /**
     * @Added by Jyoti, 14-01-2018
     * @param custTerritory
     * @param accountId
     * @return 
     */
    public List<java.util.HashMap> getListOfUserIdsForTerritoryID(String custTerritory, int accountId){        
        try {
            String query = "SELECT id FROM territory_categories WHERE category_name = ? AND is_active = 1";
            Object param[] = new Object[]{custTerritory};
//            System.out.println("query > "+query);
//            System.out.println(FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class));
            int territoryId = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
//            System.out.println("terri id "+territoryId);
            if(territoryId > 0){
                String query1 = " SELECT u.id, IFNULL(u.gcm_id,0) AS gcmID, u.device_os, u.app_version FROM fieldsense.users u, user_territory t WHERE u.id = t.user_id_fk AND teritory_id = ? AND active = 1 AND email_address NOT LIKE '%fieldsense%' ";
                log4jLog.info(" getListOfUserIdsForTerritoryID for accountID : " + accountId);
                Object param1[] = new Object[]{territoryId};

                return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query1, param1, new RowMapper<java.util.HashMap>(){
                    @Override
                    public java.util.HashMap mapRow(ResultSet rs, int i) throws SQLException {
                        java.util.HashMap hMap = new java.util.HashMap();
                        hMap.put("id", rs.getInt("u.id"));
                        hMap.put("gcmID", rs.getString("gcmID"));
                        hMap.put("device_os", rs.getInt("u.device_os"));
                        hMap.put("appVersion",rs.getInt("u.app_version")); // app_version, Added by jyoti
                        return hMap;
                    }
                });
            } else {
//                System.out.println("terri id null"+territoryId);
                return new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info(e + " getListOfUserIdsForTerritoryID for accountID :  " + accountId);
            return new ArrayList<>();
        }
    }
    
    /**
     * @Added by JYoti
     * @param accountId
     * @return 
     */
    public List<java.util.HashMap> getListOfUserIdForAccountId(int accountId){
        try{
            String query = "SELECT id, IFNULL(gcm_id,0) AS gcmID, device_os, app_version FROM users WHERE account_id_fk = ? AND active = 1 AND email_address NOT LIKE '%fieldsense%'";
            log4jLog.info("getListOfUserIdForAccountId for accountId : " + accountId);
            Object[] param = new Object[]{accountId};            
            return this.jdbcTemplate.query(query, param, new RowMapper<java.util.HashMap>() {
                @Override
                public java.util.HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    java.util.HashMap hashMap = new java.util.HashMap();
                    hashMap.put("id", rs.getInt("id"));
                    hashMap.put("gcmID", rs.getString("gcmID"));
                    hashMap.put("device_os", rs.getInt("device_os"));
                    hashMap.put("appVersion",rs.getInt("app_version")); // app_version, Added by jyoti
                    return hashMap;
                }
            });
        } catch(Exception e){
            e.printStackTrace();
            return new ArrayList<java.util.HashMap>();
        }
    }
    
    /**
     * @Added by jyoti
     * @param userId
     * @param accountId
     * @return 
     */
    public java.util.HashMap getUserDataLeftSlider(int userId, int accountId) {
        
        String query = "SELECT u.id, u.role, u.user_accuracy, u.check_in_radius, u.location_interval, u.allow_timeout, u.allow_offline, u.app_version, a.company_name, "
                        + "(SELECT COUNT(id) FROM teams WHERE locate( ?, team_position_csv, 8)) AS hasSubordinateCount"
                        + "  FROM fieldsense.users u, fieldsense.accounts a WHERE u.account_id_fk = a.id AND u.id = ?";
        log4jLog.info(" getUserDataLeftSlider for userId : "+userId);
        Object[] param = new Object[]{userId, userId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, new RowMapper<java.util.HashMap>() {
                @Override
                public java.util.HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    java.util.HashMap hMap =new  java.util.HashMap();
                    hMap.put("checkInRadius", rs.getInt("u.check_in_radius"));
                    hMap.put("userAccuracy", rs.getInt("u.user_accuracy"));
                    hMap.put("role", rs.getInt("u.role"));                    
                    hMap.put("interval", rs.getInt("u.location_interval"));
                    hMap.put("allowTimeout", rs.getInt("u.allow_timeout"));
                    hMap.put("allowOffline", rs.getInt("u.allow_offline"));
                    hMap.put("accountName", rs.getString("a.company_name"));
                    hMap.put("hasSubordinates", rs.getString("hasSubordinateCount"));
                    hMap.put("appVersion", rs.getInt("u.app_version"));
                    return hMap;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" getUserDataLeftSlider for userId : "+userId+", Exception : "+e);
            e.printStackTrace();
            return new java.util.HashMap();
        }
    }
    
    /**
     * // Added by jyoti, to solve issue of record state , later re work for the same
     * @param lastSyncDate
     * @purpose record_state, type of entry, id stored in this table - later work on record_state
     * @param type
     * @param accountId
     * @return 
     */
    public java.util.List<java.util.HashMap<String,Object>> getRecordStateForExpense_Industry(int accountId, String type, String lastSyncDate){
        String query = "SELECT typeId, typeName, typeIsActive, updated_on FROM deleted_categories_record WHERE type = ? AND updated_on > ?";
         try {
             Object[] param = new Object[]{type, lastSyncDate};
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<java.util.HashMap<String,Object>>() {

                @Override
                public java.util.HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    java.util.HashMap hMap = new java.util.HashMap();
                    hMap.put("id", rs.getInt("typeId"));
                    hMap.put("categoryName",rs.getString("typeName"));
                    hMap.put("isActive",rs.getBoolean("typeIsActive"));
                    hMap.put("status", "2");
                    return hMap;
                }
            });
        } catch (Exception e) {
            log4jLog.info("getRecordStateForExpense_Industry " + e);
            e.printStackTrace();
            return new java.util.ArrayList<java.util.HashMap<String,Object>>();
        }
    }

    /**
     * // Added by jyoti, to solve issue of record state , later re work for the same
     * @param lastSyncDate
     * @purpose record_state, type of entry, id stored in this table - later work on record_state
     * @param type
     * @param accountId
     * @return 
     */
    public java.util.List<java.util.HashMap<String,Object>> getRecordStateForPurpose(int accountId, String type, String lastSyncDate){
        String query = "SELECT typeId, typeName, typeIsActive, updated_on FROM deleted_categories_record WHERE type = ? AND updated_on > ?";
         try {
             Object[] param = new Object[]{type, lastSyncDate};
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<java.util.HashMap<String,Object>>() {

                @Override
                public java.util.HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    java.util.HashMap hMap = new java.util.HashMap();
                    hMap.put("id", rs.getInt("typeId"));
                    hMap.put("purpose",rs.getString("typeName"));
                    hMap.put("isActive",rs.getBoolean("typeIsActive"));
                    hMap.put("status", "2");
                    return hMap;
                }
            });
        } catch (Exception e) {
            log4jLog.info("getRecordStateForPurpose " + e);
            e.printStackTrace();
            return new java.util.ArrayList<java.util.HashMap<String,Object>>();
        }
    }
    
    /**
     * // Added by jyoti, to solve issue of record state , later re work for the same
     * @param lastSyncDate
     * @param userId
     * @purpose record_state, type of entry, id stored in this table - later work on record_state
     * @param type
     * @param accountId
     * @return 
     */
    public java.util.List<java.util.HashMap<String, Object>> getRecordStateForTerritory(int accountId, String type, String lastSyncDate, int userId) {
        String query = "SELECT d.typeId, d.typeName, d.typeIsActive, d.typeHasChild, d.typeParentCategory, d.updated_on FROM deleted_categories_record d ,"
                    + " user_territory t1 WHERE t1.teritory_id = d.typeId AND user_id_fk = ? AND d.typeIsActive = 1 AND d.type = ? AND d.updated_on > ? ORDER BY d.typeName ";
        
        try {            
            Object[] param = new Object[]{userId, type, lastSyncDate};
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<java.util.HashMap<String, Object>>() {

                @Override
                public java.util.HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    java.util.HashMap hMap = new java.util.HashMap();
                    hMap.put("id", rs.getInt("typeId"));
                    hMap.put("categoryName", rs.getString("typeName"));
                    hMap.put("isActive", rs.getBoolean("typeIsActive"));
                    hMap.put("hasChild", rs.getInt("typeHasChild"));
                    hMap.put("parentCategory", rs.getInt("typeParentCategory"));
                    hMap.put("updatedOn", rs.getTimestamp("updated_on"));
                    hMap.put("status", "2");
                    return hMap;
                }
            });
        } catch (Exception e) {
            log4jLog.info("getRecordStateForTerritory " + e);
            e.printStackTrace();
            return new java.util.ArrayList<java.util.HashMap<String, Object>>();
        }
    }
    
    /**
     * // Added by jyoti, to solve issue of record state , later re work for the same
     * @param type
     * @param typeId
     * @param typeIsActive
     * @param typeHasChild
     * @param typeParentCategory
     * @param typeName
     * @param accountId
     * @return
     */
    public static int setDeletedCategoriesRecord(String type, int typeId, String typeName, boolean typeIsActive, int typeHasChild, int typeParentCategory, int accountId ){
        try{
//            System.out.println("type : "+ type+" , typeId :"+typeId);
            String query = "INSERT INTO deleted_categories_record (typeId, type, typeName, typeIsActive, typeHasChild, typeParentCategory, updated_on) VALUES(?,?,?,?,?,?,now())";
            log4jLog.info("setDeletedCategoriesRecord for accountId : " + accountId);
            Object[] param = new Object[]{typeId, type, typeName, typeIsActive, typeHasChild, typeParentCategory};
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param);
        } catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }
    /**
     * @added by vaibhav
     * @param email
     * @return 
     */
    public int getUserIdFrmEmailAddress(String email) {
        String query = "SELECT id FROM users WHERE email_address=?";
        log4jLog.info(" getUserId " + query);
        Object param[] = new Object[]{email};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" getUserIdFrmEmailAddress " + e);
//            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * @Added by jyoti
     * @param userId
     * @return 
     */
    public java.util.HashMap getUserDetails(int userId) {
        
        String query = "SELECT full_name, emp_code FROM users WHERE id = ?  ";
        log4jLog.info(" getUserDetails for userId : "+userId);
        Object[] param = new Object[]{userId};
        try {
            return jdbcTemplate.queryForObject(query, param, new RowMapper<java.util.HashMap>() {
                @Override
                public java.util.HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    java.util.HashMap hMap =new  java.util.HashMap();
                    hMap.put("fullName", rs.getString("full_name"));
                    hMap.put("empCode", rs.getString("emp_code"));
                    return hMap;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" getUserDetails for userId : "+userId+", Exception : "+e);
            e.printStackTrace();
            return new java.util.HashMap();
        }
    }
    
    /**
     * @Added by jyoti
     * @param assignedId
     * @return 
     */
    public String getUserFullName(int assignedId){
        String query = "SELECT full_name FROM users WHERE id = ?";
        log4jLog.info(" getUserFullName " + query);
        try {
            Object param[] = new Object[]{assignedId};
            return jdbcTemplate.queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" getUserFullName " + e);
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * @added by jyoti, New Feature #29288
     * @param authToken
     * @return 
     */
    public boolean isAuthTokenValid(String authToken) {
        String query = " SELECT COUNT(id) FROM accounts_erp_auth_detail WHERE auth_token = ? ";
        log4jLog.info(" isAuthTokenValid for authToken : " + authToken);
        Object[] param = new Object[]{authToken};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class) > 0;
        } catch (Exception e) {
            log4jLog.info("isAuthTokenValid for  authToken : "+ authToken + e);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * @added by jyoti, 18-apr-2018
     * @param mobileNumber
     * @return 
     */
    public boolean isMobileValid(String mobileNumber) {
        String query = "SELECT COUNT(id) FROM users WHERE mobile_number = ? ";
        log4jLog.info(" isMobileValid " + query);
        Object[] param = new Object[]{mobileNumber};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class) > 0;
        } catch (Exception e) {
            log4jLog.info(" isMobileValid " + e);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * @added by jyoti, New Feature #29288
     * @param authToken
     * @return 
     */
    public int accountIdForAuthToken(String authToken) {
        String query = " SELECT account_id_fk FROM accounts_erp_auth_detail WHERE auth_token = ? ";
        log4jLog.info(" accountIdForAuthToken for authToken : " + authToken);
        Object[] param = new Object[]{authToken};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" accountIdForAuthToken for authToken : " + authToken + e);
            e.printStackTrace();
            return 0;
        }
    }
    
          public boolean CheckIfUserAccountActive(String emailId){
        String query = "SELECT acc.status from fieldsense.users u left join fieldsense.accounts acc on u.account_id_fk=acc.id where u.email_address=?";
        log4jLog.info(" isUserAccountActive " + query);
        Object[] param = new Object[]{emailId};
        try {
     int status=jdbcTemplate.queryForObject(query, param, Integer.class);    
     if(status==0)
     {
         return false;
     }
     else
     {
         return true;        
     }         
        } catch (Exception e) {
            log4jLog.info(" isUserActive " + e);          
        }
    return false;   
}
        //added by manohar
        public int getReporttoId(int userId) {
        String query = "SELECT report_to FROM fieldsensestats.visitstats WHERE userId=?";
        log4jLog.info(" getUserId " + query);
        Object param[] = new Object[]{userId};
        try {
            return jdbcTemplate.queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" getUserIdFrmEmailAddress " + e);
//            e.printStackTrace();
            return 0;
        }
    }
}
