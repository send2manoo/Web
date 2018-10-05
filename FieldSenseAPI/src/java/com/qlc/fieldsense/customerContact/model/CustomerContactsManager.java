/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customerContact.model;

import au.com.bytecode.opencsv.CSVWriter;
import com.qlc.fieldsense.customer.dao.customerDao;
import com.qlc.fieldsense.customerContact.dao.CustomerContactsDao;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import com.qlc.fieldsense.utils.PhotoToIconCreator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author anuja
 */
public class CustomerContactsManager {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("CustomerContactsManager");
    customerDao customerDao = (customerDao) GetApplicationContext.ac.getBean("customerDaoImpl");
    CustomerContactsDao customerContactsDao = (CustomerContactsDao) GetApplicationContext.ac.getBean("customerContactsDaoImpl");
    FieldSenseUtils util = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");

    /**
     * 
     * @param customerContacts
     * @param token
     * @return 
     * @purpose used to create customers contact details
     */
    public Object createCustomerContact(CustomerContacts customerContacts, String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                String validStatus = Constant.VALID;
                if ( !(FieldSenseUtils.isContainOnlyCharacters(customerContacts.getFirstName()))) {
                             return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid first name . ", "", "");
                        }
                        if (!(FieldSenseUtils.isContainOnlyCharacters(customerContacts.getMiddleName()))) {
                             return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid middle name . ", "", "");   
                        }
                        if ( !(FieldSenseUtils.isContainOnlyCharacters(customerContacts.getLastName()))) {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid last name . ", "", "");                        
                        }
                        if (customerContactsDao.isCustomerContactExist(customerContacts.getFirstName(), customerContacts.getMiddleName(), customerContacts.getLastName(), customerContacts.getCustomerId(), accountId)) {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Customer contact already exist.", "", "");
                        }
                int custContId = customerContactsDao.insertCustomerContacts(customerContacts, accountId);
                if (custContId != 0) {
                    File file = new File(Constant.PROFILEPIC_UPLOAD_PATH + customerContacts.getContactImageUrl());

                    PhotoToIconCreator photoToIconCreator = new PhotoToIconCreator();
                    try {
                        photoToIconCreator.uploadCustomerContactImage(file, accountId + "_" + customerContacts.getCustomerId() + "_" + custContId);
                    } catch (IOException ex) {
                        java.util.logging.Logger.getLogger(CustomerContactsManager.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    String imageName = Constant.PROFILEPIC_UPLOAD_PATH + accountId + "_" + customerContacts.getCustomerId() + "_" + custContId + ".png";
                    File newfile = new File(imageName);
                    file.renameTo(newfile);
                    customerContacts = customerContactsDao.selectCustomerContact(custContId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.CUSTOMER_CONTACT_CREATED, " customerContacts ", customerContacts);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Customer contact not created . Please try again . ", "", "");
                }    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /**
     * @param token
     * @return list of all contacts
     */
    public Object getCustomersContacts(String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                List<CustomerContacts> customerContactList = customerContactsDao.selectCustomerContacts(accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "customerContactList", customerContactList);   
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /**
     * 
     * @param id
     * @param token
     * @return details of contact based on id
     */
    public Object getCustomerContact(int id, String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                if (customerContactsDao.isCustomerContactValid(id, accountId)) {
                    CustomerContacts customerContacts = new CustomerContacts();
                    customerContacts = customerContactsDao.selectCustomerContact(id, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " customerContacts ", customerContacts);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_CUSTOMER_CONTACT, "", "");
                }  
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /**
     * 
     * @param customerId
     * @param token
     * @return details of customers all contacts based on customer id
     */
    public Object getCustomerContactOnCustomerId(int customerId, String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                if (customerContactsDao.isContactCustomerIdValid(customerId, accountId)) {
                    List<CustomerContacts> customerContactsList = customerContactsDao.selectCustomerContactOnCustomerId(customerId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " customerContactsList ", customerContactsList);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_CUSTOMER, "", "");
                }   
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

     /**
     * 
     * @param id
     * @param token
     * @return 
     * @purpose delete specific contact details based on id 
     */
    public Object deleteCustomerContacts(int id, String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                if (customerContactsDao.isCustomerContactValid(id, accountId)) {
                    CustomerContacts customerContacts = new CustomerContacts();
                    customerContacts = customerContactsDao.deleteCustomerContacts(id, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.CUSTOMER_CONTACT_DELETED, " customerContacts ", customerContacts);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_CUSTOMER_CONTACT, "", "");
                }  
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /**
     * 
     * @param customerId
     * @param token
     * @return 
     * @purpose delete specific customers all contact details based on customer id 
     */
    public Object deleteCustomerContactOnCustomerId(int customerId, String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                if (customerContactsDao.isContactCustomerIdValid(customerId, accountId)) {
                    List<CustomerContacts> customerContacts = customerContactsDao.deleteCustomerContactOnCustomerId(customerId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.CUSTOMER_CONTACT_DELETED, " customerContacts ", customerContacts);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_CUSTOMER_CONTACT, "", "");
                }   
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /**
     * 
     * @param customerContacts
     * @param token
     * @return 
     * @purpose used to edit contact details 
     */
    public Object updateCustomerContacts(CustomerContacts customerContacts, String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                if (customerContactsDao.isCustomerContactValid(customerContacts.getId(), accountId)) {
                    customerContacts = customerContactsDao.updateCustomerContacts(customerContacts, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.CUSTOMER_CONTACT_UPDATED, " customerContacts ", customerContacts);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_CUSTOMER_CONTACT, "", "");
                }   
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /**
     * @param customerContacts
     * @param token
     * @return 
     * @purpose used to edit contact details based on contact id
     */
    public Object updateCustomerContactDetails(CustomerContacts customerContacts, String token) {
        if (util.isTokenValid(token)) {
            //if(util.isSessionExpired(token)){
                int accountId = util.accountIdForToken(token);
                if (customerContactsDao.isCustomerContactValid(customerContacts.getId(), accountId)) {
                    if (customerDao.isCustomerValid(customerContacts.getCustomerId(), accountId)) {
                        customerContacts = customerContactsDao.updateCustomerContactDetails(customerContacts, accountId);
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.CUSTOMER_CONTACT_UPDATED, " customerContacts ", customerContacts);
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_CUSTOMER, "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_CUSTOMER_CONTACT, "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /*  public Object updateCustomerContactsOnCustomerId(CustomerContacts customerContacts, String token) {
     if (util.isTokenValid(token)) {
     int accountId = util.accountIdForToken(token);
     if (customerContactsDao.isContactCustomerIdValid(customerContacts.getCustomerId().getId(), accountId)) {
     customerContacts = customerContactsDao.updateCustomerContactsOnCustomerId(customerContacts, accountId);
     return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.CUSTOMER_CONTACT_UPDATED, " customerContacts ", customerContacts);
     } else {
     return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_CUSTOMER_CONTACT, "", "");
     }
     } else {
     return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
     }
     }*/
    
    /**
     * @param file
     * @param userToken
     * @return 
     * @purpose used to import contact details
     */
    public Object insertCustomerContacts(MultipartFile file, String userToken) {
        try {
            String validStatus = Constant.VALID;
            List<Integer> customerContactList = new ArrayList<Integer>();
            List<String> customerContactNmList = new ArrayList<String>();
            List<String[]> contactErrorList = new ArrayList<String[]>();
            if (util.isTokenValid(userToken)) {
                //if(util.isSessionExpired(userToken)){
                    int accountId = util.accountIdForToken(userToken);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss_SSS");
                    java.util.Date d = new java.util.Date();
                    String date = dateFormat.format(d);
                    String saveFileTo = Constant.IMPORT_ERROR_FILES;
                    String errorFileNm = accountId + "_" + date + "_ContactImportError.csv";
                    String csv = saveFileTo + errorFileNm;
                    List<CustomerContactsCSV> list = MapCSVToCustomerContacts.mapJavaBean(file);
                    for (int i = 0; i < 1; i++) {
                        if (((list.get(i).getFirstName().equalsIgnoreCase("firstName")) && (list.get(i).getMiddleName().equalsIgnoreCase("middleName"))) && ((list.get(i).getLastName().equalsIgnoreCase("lastName")) && (list.get(i).getPhone1().equalsIgnoreCase("phone1")))) {
                            if (((list.get(i).getPhone2().equalsIgnoreCase("phone2")) && (list.get(i).getPhone3().equalsIgnoreCase("phone3"))) && ((list.get(i).getFax1().equalsIgnoreCase("fax1")) && (list.get(i).getFax2().equalsIgnoreCase("fax2")))) {
                                if (((list.get(i).getMobile1().equalsIgnoreCase("mobile1")) && (list.get(i).getMobile2().equalsIgnoreCase("mobile2"))) && ((list.get(i).getEmail1().equalsIgnoreCase("email1")) && (list.get(i).getEmail2().equalsIgnoreCase("email2")))) {
                                    if (((list.get(i).getReportTo().equalsIgnoreCase("reportTo")) && (list.get(i).getAssistantName().equalsIgnoreCase("assistantName"))) && ((list.get(i).getSpouseName().equalsIgnoreCase("spouseName")) && (list.get(i).getBirthDate().equalsIgnoreCase("birthDate")))) {
                                        if (((list.get(i).getAnniversaryDate().equalsIgnoreCase("anniversaryDate")) && (list.get(i).getDesignation().equalsIgnoreCase("designation"))) && ((list.get(i).getCustomerName().equalsIgnoreCase("customerName")) && (list.get(i).getCustomerLocationIdentifier().equalsIgnoreCase("customerLocationIdentifier")))) {

                                        } else {
                                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "CSV file not in correct format. Please download the sample file.", "", "");
                                        }
                                    } else {
                                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "CSV file not in correct format. Please download the sample file.", "", "");
                                    }
                                } else {
                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "CSV file not in correct format. Please download the sample file.", "", "");
                                }
                            } else {
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "CSV file not in correct format. Please download the sample file.", "", "");
                            }
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "CSV file not in correct format. Please download the sample file.", "", "");
                        }
                    }
                    contactErrorList.add(new String[]{"firstName", "middleName", "lastName", "phone1", "phone2", "phone3", "fax1", "fax2", "mobile1", "mobile2", "email1", "email2", "reportTo", "assistantName", "spouseName", "birthDate", "anniversaryDate", "designation", "customerName", "customerLocationIdentifier"});
                    for (int i = 1; i < list.size(); i++) {
                        CustomerContacts customerContacts = new CustomerContacts();
                        String validStatus2 = Constant.VALID;
                        int customerId = customerDao.getCustomerIdOnNmLocation(list.get(i).getCustomerName(), list.get(i).getCustomerLocationIdentifier(), accountId);

                        if (list.get(i).getFirstName().equals(""))  {
                            validStatus = FieldSenseUtils.notvalid(validStatus);
                            validStatus = validStatus + "First name should not be blank at line:" + (i + 1) + ".";
                            validStatus2 = validStatus;
                        }
                        else if( !(FieldSenseUtils.isContainOnlyCharacters(list.get(i).getFirstName())))
                            {
                            validStatus = FieldSenseUtils.notvalid(validStatus);
                            validStatus = validStatus + "Invalid first name at line:" + (i + 1) + ".";
                            validStatus2 = validStatus;
                            }

                        /*if (list.get(i).getMiddleName().equals(""))  {
                            validStatus = FieldSenseUtils.notvalid(validStatus);
                            validStatus = validStatus + "Middle name should not be blank at line:" + (i + 1) + ".";
                            validStatus2 = validStatus;
                        }
                        else */
                        if(!(FieldSenseUtils.isContainOnlyCharacters(list.get(i).getMiddleName())))
                           {
                             validStatus = FieldSenseUtils.notvalid(validStatus);
                             validStatus = validStatus + "Invalid middle name at line:" + (i + 1) + ".";
                             validStatus2 = validStatus;
                           }

                        if (list.get(i).getLastName().equals("")) {
                            validStatus = FieldSenseUtils.notvalid(validStatus);
                            validStatus = validStatus + " last name should not be blank at line:" + (i + 1) + ".";
                            validStatus2 = validStatus;
                        }
                        else if(!(FieldSenseUtils.isContainOnlyCharacters(list.get(i).getLastName())))
                           {
                             validStatus = FieldSenseUtils.notvalid(validStatus);
                             validStatus = validStatus + "Invalid last name at line:" + (i + 1) + ".";
                             validStatus2 = validStatus;
                           }
                        if (customerContactsDao.isCustomerContactExist(list.get(i).getFirstName(), list.get(i).getMiddleName(), list.get(i).getLastName(), customerId, accountId)) {
                            validStatus = FieldSenseUtils.notvalid(validStatus);
                            validStatus = validStatus + "Customer contact already exist at line:" + (i + 1) + ".";
                            validStatus2 = validStatus;
                        }

                        if (!(list.get(i).getPhone1().isEmpty())) {
    //                        if (!(FieldSenseUtils.isContainOnlyNumbers(list.get(i).getPhone1()))) {
    //                            validStatus = FieldSenseUtils.notvalid(validStatus);
    //                            validStatus = validStatus + "Invalid phone number at line:" + (i + 1) + ".";
    //                            validStatus2 = validStatus;
    //                        } else {
                            customerContacts.setPhone1(list.get(i).getPhone1());
    //                        }
                        }

                        if (!(list.get(i).getPhone2().isEmpty())) {
    //                        if (!(FieldSenseUtils.isContainOnlyNumbers(list.get(i).getPhone2()))) {
    //                            validStatus = FieldSenseUtils.notvalid(validStatus);
    //                            validStatus = validStatus + "Invalid phone number at line:" + (i + 1) + ".";
    //                            validStatus2 = validStatus;
    //                        }else{
                            customerContacts.setPhone2(list.get(i).getPhone2());
    //                        }
                        }

                        if (!(list.get(i).getPhone3().isEmpty())) {
    //                        if (!(FieldSenseUtils.isContainOnlyNumbers(list.get(i).getPhone3()))) {
    //                            validStatus = FieldSenseUtils.notvalid(validStatus);
    //                            validStatus = validStatus + "Invalid phone number at line:" + (i + 1) + ".";
    //                            validStatus2 = validStatus;
    //                        }else{
                            customerContacts.setPhone3(list.get(i).getPhone3());
    //                        }
                        }
                        if (!(list.get(i).getFax1().isEmpty())) {
    //                        if (!(FieldSenseUtils.isContainOnlyNumbers(list.get(i).getFax1()))) {
    //                            validStatus = FieldSenseUtils.notvalid(validStatus);
    //                            validStatus = validStatus + "Invalid fax number at line:" + (i + 1) + ".";
    //                            validStatus2 = validStatus;
    //                        }else{
                            customerContacts.setFax1(list.get(i).getFax1());
    //                        }
                        }
                        if (!(list.get(i).getFax2().isEmpty())) {
    //                        if (!(FieldSenseUtils.isContainOnlyNumbers(list.get(i).getFax2()))) {
    //                            validStatus = FieldSenseUtils.notvalid(validStatus);
    //                            validStatus = validStatus + "Invalid fax number at line:" + (i + 1) + ".";
    //                            validStatus2 = validStatus;
    //                        }else{
                            customerContacts.setFax2(list.get(i).getFax2());
    //                        }
                        }
                        if (!(list.get(i).getMobile1().isEmpty())) {
    //                        if (!(FieldSenseUtils.isContainOnlyNumbers(list.get(i).getMobile1()))) {
    //                            validStatus = FieldSenseUtils.notvalid(validStatus);
    //                            validStatus = validStatus + "Invalid mobile number at line:" + (i + 1) + ".";
    //                            validStatus2 = validStatus;
    //                        }else{
                            customerContacts.setMobile1(list.get(i).getMobile1());
    //                        }
                        }
                        if (!(list.get(i).getMobile2().isEmpty())) {
    //                        if (!(FieldSenseUtils.isContainOnlyNumbers(list.get(i).getMobile2()))) {
    //                            validStatus = FieldSenseUtils.notvalid(validStatus);
    //                            validStatus = validStatus + "Invalid mobile number at line:" + (i + 1) + ".";
    //                            validStatus2 = validStatus;
    //                        }else{
                            customerContacts.setMobile2(list.get(i).getMobile2());
    //                        }
                        }

                        if (!(list.get(i).getEmail1().isEmpty())) {
    //                        if (!(FieldSenseUtils.isValidEmailAddress(list.get(i).getEmail1()))) {
    //                            validStatus = FieldSenseUtils.notvalid(validStatus);
    //                            validStatus = validStatus + "Invalid email address at line:" + (i + 1) + ".";
    //                            validStatus2 = validStatus;
    //                        } else if (!(FieldSenseUtils.isDomainValid(list.get(i).getEmail1()))) {
    //                            validStatus = FieldSenseUtils.notvalid(validStatus);
    //                            validStatus = validStatus + "Public domain is not allowed at line:" + (i + 1) + ".";
    //                            validStatus2 = validStatus;
    //                        }else{
                            customerContacts.setEmail1(list.get(i).getEmail1());
    //                        }
                        }
                        if (!(list.get(i).getEmail2().isEmpty())) {
    //                        if (!(FieldSenseUtils.isValidEmailAddress(list.get(i).getEmail2()))) {
    //                            validStatus = FieldSenseUtils.notvalid(validStatus);
    //                            validStatus = validStatus + "Invalid email address at line:" + (i + 1) + ".";
    //                            validStatus2 = validStatus;
    //                        } else if (!(FieldSenseUtils.isDomainValid(list.get(i).getEmail2()))) {
    //                            validStatus = FieldSenseUtils.notvalid(validStatus);
    //                            validStatus = validStatus + "Public domain is not allowed at line:" + (i + 1) + ".";
    //                            validStatus2 = validStatus;
    //                        }else{
                            customerContacts.setEmail2(list.get(i).getEmail2());
    //                        }
                        }

                        if (!(list.get(i).getReportTo().isEmpty())) {
    //                        if (!(FieldSenseUtils.isContainOnlyCharacters(list.get(i).getReportTo()))) {
    //                            validStatus = FieldSenseUtils.notvalid(validStatus);
    //                            validStatus = validStatus + "Invalid report to at line:" + (i + 1) + ".";
    //                            validStatus2 = validStatus;
    //                        }else{
                            customerContacts.setReportTo(list.get(i).getReportTo());
    //                        }
                        }
                        if (!(list.get(i).getAssistantName().isEmpty())) {
    //                        if (!(FieldSenseUtils.isContainOnlyCharacters(list.get(i).getAssistantName()))) {
    //                            validStatus = FieldSenseUtils.notvalid(validStatus);
    //                            validStatus = validStatus + "Invalid assistant name at line:" + (i + 1) + ".";
    //                            validStatus2 = validStatus;
    //                        }else{
                            customerContacts.setAssistantName(list.get(i).getAssistantName());
    //                        }
                        }
                        if (!(list.get(i).getSpouseName().isEmpty())) {
    //                        if (!(FieldSenseUtils.isContainOnlyCharacters(list.get(i).getSpouseName()))) {
    //                            validStatus = FieldSenseUtils.notvalid(validStatus);
    //                            validStatus = validStatus + "Invalid spouse name at line:" + (i + 1) + ".";
    //                            validStatus2 = validStatus;
    //                        }else{
                            customerContacts.setSpouseName(list.get(i).getSpouseName());
    //                        }
                        }
                        if (!(list.get(i).getBirthDate().isEmpty())) {
                            customerContacts.setsBirthDate(list.get(i).getBirthDate());
                            customerContacts.setBirthDate(util.converStringToDate(list.get(i).getBirthDate()));
                        }
                        if (!(list.get(i).getAnniversaryDate().isEmpty())) {
                            customerContacts.setsAnniversaryDate(list.get(i).getAnniversaryDate());
                            customerContacts.setAnniversaryDate(util.converStringToDate(list.get(i).getAnniversaryDate()));
                        }
                        /* if (list.get(i).getBirthDate().equals("")) {
                         validStatus = FieldSenseUtils.notvalid(validStatus);
                         validStatus = validStatus + "Invalid birth date at line:" + (i + 1) + ".";
                         validStatus2 = validStatus;
                         }
                         if (list.get(i).getAnniversaryDate().equals("")) {
                         validStatus = FieldSenseUtils.notvalid(validStatus);
                         validStatus = validStatus + "Invalid anniversary date at line:" + (i + 1) + ".";
                         validStatus2 = validStatus;
                         }*/
                        if (!(list.get(i).getDesignation().isEmpty())) {
    //                        if (!(FieldSenseUtils.isContainOnlyCharacters(list.get(i).getDesignation()))) {
    //                            validStatus = FieldSenseUtils.notvalid(validStatus);
    //                            validStatus = validStatus + "Invalid designation at line:" + (i + 1) + ".";
    //                            validStatus2 = validStatus;
    //                        }else{
                            customerContacts.setDesignation(list.get(i).getDesignation());
    //                        }
                        }
    //                    if ((list.get(i).getCustomerName().equals("")) || !(FieldSenseUtils.isContainOnlyCharacters(list.get(i).getCustomerName()))) {
                        if ((list.get(i).getCustomerName().equals(""))) {
                            validStatus = FieldSenseUtils.notvalid(validStatus);
                            validStatus = validStatus + "Invalid customer name at line:" + (i + 1) + ".";
                            validStatus2 = validStatus;
                        }
    //                    if ((list.get(i).getCustomerLocationIdentifier().equals("")) || !(FieldSenseUtils.isContainOnlyCharacters(list.get(i).getCustomerLocationIdentifier()))) {
                        if ((list.get(i).getCustomerLocationIdentifier().equals(""))) {
                            validStatus = FieldSenseUtils.notvalid(validStatus);
                            validStatus = validStatus + "location identifier should not be blank at line:" + (i + 1) + ".";
                            validStatus2 = validStatus;
                        }
                        if (!(validStatus2.equals(Constant.VALID))) {
                            contactErrorList.add(new String[]{list.get(i).getFirstName(), list.get(i).getMiddleName(), list.get(i).getLastName(), list.get(i).getPhone1(), list.get(i).getPhone2(), list.get(i).getPhone3(), list.get(i).getFax1(), list.get(i).getFax2(), list.get(i).getMobile1(), list.get(i).getMobile2(), list.get(i).getEmail1(), list.get(i).getEmail2(), list.get(i).getReportTo(), list.get(i).getAssistantName(), list.get(i).getSpouseName(), list.get(i).getBirthDate(), list.get(i).getAnniversaryDate(), list.get(i).getDesignation(), list.get(i).getCustomerName(), list.get(i).getCustomerLocationIdentifier()});
                            continue;
                        }
                        if (validStatus2.equals(Constant.VALID)) {
                            customerContacts.setFirstName(list.get(i).getFirstName());
                            customerContacts.setMiddleName(list.get(i).getMiddleName());
                            customerContacts.setLastName(list.get(i).getLastName());
                            customerContacts.setCustomerId(customerId);
                            if (customerDao.isCustomerValid(customerId, accountId)) {
                                int custContId = customerContactsDao.insertCustomerContacts(customerContacts, accountId);
                                if (custContId != 0) {
    //                                customerContacts = customerContactsDao.selectCustomerContactForImport(custContId, accountId);
                                    customerContactList.add(custContId);
                                    customerContactNmList.add(customerContacts.getFirstName() + ' ' + customerContacts.getLastName());
                                }
                            } else {
                                validStatus = FieldSenseUtils.notvalid(validStatus);
                                validStatus = validStatus + "Customer name does not exist at line:" + (i + 1) + ".";
                            }
                        }
                    }
                    if ((contactErrorList.size() > 1)) {
                        CSVWriter writer = new CSVWriter(new FileWriter(csv));
                        writer.writeAll(contactErrorList);
                  //      System.out.println("CSV written successfully.");
                        writer.close();
                    }
                    int totalCreated = customerContactNmList.size();
                    if ((customerContactList.size() > 0) && validStatus.equals(Constant.VALID)) {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.CUSTOMER_CONTACT_CREATED, " customerContactNmList ", customerContactNmList);
                    } else if ((customerContactList.size() > 0) && !(validStatus.equals(Constant.VALID))) {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, totalCreated + " Customer contacts get created." + validStatus, " ErrorFileName ", errorFileNm);
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, validStatus + " Customer Contact not created . Please try again .", "ErrorFileName", errorFileNm);
                    }
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
            }
        } catch (NullPointerException e) {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Customer location identifier may not be empty. Please try again . ", "", "");
        } catch (FileNotFoundException ex) {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "File not uploaded. Please try again . ", "", "");
        } catch (IOException ex) {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "File not uploaded. Please try again . ", "", "");
        }
    }
}
