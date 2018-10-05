/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customer.model;

import au.com.bytecode.opencsv.CSVWriter;
import com.qlc.fieldsense.addressConverter.AddressConverter;
import com.qlc.fieldsense.addressConverter.GoogleResponse;
import com.qlc.fieldsense.addressConverter.Result;
import com.qlc.fieldsense.customer.dao.customerDao;
import com.qlc.fieldsense.customerContact.dao.CustomerContactsDao;
import com.qlc.fieldsense.location.dao.LocationDao;
import com.qlc.fieldsense.location.model.Location;
import com.qlc.fieldsense.territoryCategory.dao.TerritoryCategoryDao;
import com.qlc.fieldsense.territoryCategory.model.TerritoryCategory;
import com.qlc.fieldsense.industryCategory.dao.IndustryCategoryDao;
import com.qlc.fieldsense.industryCategory.model.IndustryCategory;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import com.qlc.fieldsense.utils.RunnableThreadJob;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author anuja
 */
public class CustomerManager{

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("CustomerManager");
    customerDao customerDao = (customerDao) GetApplicationContext.ac.getBean("customerDaoImpl");
    FieldSenseUtils util = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
    LocationDao locationDao = (LocationDao) GetApplicationContext.ac.getBean("locationDaoImpl");
    IndustryCategoryDao industryCategoryDao = (IndustryCategoryDao) GetApplicationContext.ac.getBean("industryCategoryDaoImpl");
    TerritoryCategoryDao territoryCategoryDao = (TerritoryCategoryDao) GetApplicationContext.ac.getBean("territoryCategoryDaoImpl");
    CustomerContactsDao customerContactsDao = (CustomerContactsDao) GetApplicationContext.ac.getBean("customerContactsDaoImpl");

    /**
     * 
     * @param id
     * @param token
     * @return customer details based on id
     */
    public Object getCustomer(int id, String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                if (customerDao.isCustomerValid(id, accountId)) {
                    Customer customer = new Customer();
                    customer = customerDao.selectCustomer(id, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " customer ", customer);
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
     * @return customer details with its location details based on id.
     */
    public Object getCustomerWithLocation(int id, String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                if (customerDao.isCustomerValid(id, accountId)) {
                    int userId = util.userIdForToken(token);
                    Customer customer = new Customer();
                    customer = customerDao.selectCustomerWithLocatio(userId, id, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " customer ", customer);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_CUSTOMER, "", "");
                }   
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /**
     * @param token
     * @return list of all customers with its details
     */
    public Object getCustomers(String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                List<Customer> customerList = new ArrayList<Customer>();
                customerList = customerDao.selectCustomers(accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " customerList ", customerList);   
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }
    
    /**
     * 
     * @param token
     * @return list of all customers with its name only
     * @purpose  By jyoti- MObile side sending customer list with only names
     */
    public Object getAllCustomersNames(String token) {
        if (util.isTokenValid(token)) {
            int accountId = util.accountIdForToken(token);
            int userId = util.userIdForToken(token);
            List<String> customerList = customerDao.selectCustomerNames(accountId, userId);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " customerList ", customerList);    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }        
            
    /**
     * 
     * @param allRequestParams
     * @return list of all customers with its details
     */
    public Object getCustomersInVisit(@RequestParam Map<String,String> allRequestParams) {
        String token=allRequestParams.get("userToken");
        String serchText=allRequestParams.get("searchText");
        if (util.isTokenValid(token)) {
            //if(util.isSessionExpired(token)){
                int accountId = util.accountIdForToken(token);
                List<Map> customerList = new ArrayList<Map>();
                customerList = customerDao.selectCustomersInVisit(accountId,serchText);
                return FieldSenseUtils.generateFieldSenseResponseForCustomersListInVisit(customerList);  
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }
    
    /**
     * @Added by jyoti
     * @param allRequestParams
     * @return
     */
    public Object selectCustomersOnlyWhoHaveVisit(@RequestParam Map<String, String> allRequestParams) {
        String token = allRequestParams.get("userToken");
        String serchText = allRequestParams.get("searchText");
        if (util.isTokenValid(token)) {
            int accountId = util.accountIdForToken(token);
            int userId = util.userIdForToken(token);
            List<Map> customerList = customerDao.selectCustomersOnlyWhoHaveVisit(userId, accountId, serchText);
            if (customerList.isEmpty()) { // if no customer exist, No match found will display
                Map customer = new HashMap();
                customer.put("customerId", 0);
                customer.put("value", "No match found");
                List<Map> customerList1 = new ArrayList<>();
                customerList1.add(customer);
                customerList = customerList1;
            }
            return FieldSenseUtils.generateFieldSenseResponseForCustomersListInVisit(customerList);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }
    
    /**
     * 
     * @param customer
     * @param token
     * @return 
     * @purpose used to create customer
     */
    public Object createCustomer(Customer customer, String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                int userId = util.userIdForToken(token);
                if (!customerDao.isCustomerExistWithLocation(customer.getCustomerName(), customer.getCustomerLocation(), accountId)) {
                    if(customerDao.isTerritoryExists(customer.getTerritory().trim(),accountId)){
                        int customerId = customerDao.insertCustomer(customer, accountId);
                        if (customerId != 0) {
                            if (customer.getLasknownLatitude() != 0 || customer.getLasknownLangitude() != 0) {
                                Location location = new Location();
                                location.setLatitude(customer.getLasknownLatitude());
                                location.setLangitude(customer.getLasknownLangitude());
                                boolean isLocationAvailable = false;
                                if (customer.getCustomerType() == 1) {
                                    location.setLocationType(customerId);
                                } else {
                                    location.setLocationType(customer.getCustomerType());
                                }
                                location.setUserId(userId);
                                isLocationAvailable = locationDao.isLocationAvailable(location, accountId);
                                if (isLocationAvailable) {
                                    locationDao.updateUserLocation(location, accountId);
                                } else {
                                    locationDao.insertlocation(location, accountId);
                                }
                                customer = customerDao.selectCustomerWithLocatio(userId, customerId, accountId);
                            } else {
                                customer = customerDao.selectCustomer(customerId, accountId);
                            }
                            
                            // Added by Jyoti, 04-01-2018
//                            System.out.println("territory name "+customer.getTerritory().trim());
                            int customerStatus = 0; // 0=new, 1=update, 2=delete
                            RunnableThreadJob theJob = new RunnableThreadJob();
                            theJob.sendAddEditCustomerNotificationToUsersOfAccount(customerStatus, customer, accountId);                            
                            // Ended by Jyoti, 04-01-2018
                            
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.CUSTOMER_CREATED, " customer ", customer);
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Customer not created . Please try again . ", "", "");
                        }
                    }else{
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Territory is not exists or not active . Please try again . ", "", "");
                    }    
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Customer data already exists", "", "");
                }
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /**
     * 
     * @param customer
     * @param token
     * @return used to update customer 
     */
    public Object updateCustomer(Customer customer, String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                if (customerDao.isCustomerValid(customer.getId(), accountId)) {
                    customer = customerDao.updateCustomer(customer, accountId);
                    Location location = new Location();
                    location.setLatitude(customer.getLasknownLatitude());
                    location.setLangitude(customer.getLasknownLangitude());
                    location.setLocationType(customer.getId());
                    location.setUserId(util.userIdForToken(token));
                    boolean isLocationAvailable = locationDao.isLocationAvailableOnlyWithLocationTypeId(customer.getId(), accountId);
                    if (isLocationAvailable) {
                        locationDao.updateUserLocationOnlyWithLocationTypeId(location, accountId);
                    } else {
                        locationDao.insertlocation(location, accountId);
                    }
                    
                    // Added by Jyoti, 04-01-2018
//                    System.out.println("territory name "+customer.getTerritory().trim());
                    int customerStatus = 1; // 0=new, 1=update, 2=delete
                    RunnableThreadJob theJob = new RunnableThreadJob();
                    theJob.sendAddEditCustomerNotificationToUsersOfAccount(customerStatus, customer, accountId);
                    // Ended by Jyoti, 04-01-2018
                    
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.CUSTOMER_UPDATED, " customer ", customer);
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
     * @purpose used to delete customer based on id
     */
    public Object deleteCustomer(int id, String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                if (customerDao.isCustomerValid(id, accountId)) {
                    Customer customer = new Customer();
                    customer = customerDao.deleteCustomer(id, accountId);
                    // Added by Jyoti, 04-01-2018
//                    System.out.println("territory name "+customer.getTerritory().trim());
                    int customerStatus = 2; // 0=new, 1=update, 2=delete
                    RunnableThreadJob theJob = new RunnableThreadJob();
                    theJob.sendAddEditCustomerNotificationToUsersOfAccount(customerStatus, customer, accountId);                    
                    // Ended by Jyoti, 04-01-2018
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.CUSTOMER_DELETED, " customer ", customer);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_CUSTOMER, "", "");
                }    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /**
     * 
     * @param customer
     * @param usertoken
     * @return 
     * @purpose Used to update customer name and address
     */
    public Object updateCustomerNameAddress(Customer customer, String usertoken) {
        if (util.isTokenValid(usertoken)) {
                int accountId = util.accountIdForToken(usertoken);
                if (customerDao.isCustomerValid(customer.getId(), accountId)) {
                    customer = customerDao.updateCustomerAddress(customer, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.CUSTOMER_UPDATED, " customer ", customer);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_CUSTOMER, "", "");
                }   
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /*  public Object updateCustomerCustomerContacts(Customer customer, String usertoken) {
    if (util.isTokenValid(usertoken)) {
    int accountId = util.accountIdForToken(usertoken);
    if (customerDao.isCustomerValid(customer.getId(), accountId)) {
    customer = customerDao.updateCustomerAddress(customer, accountId);
    int length = customer.getCustomerContacts().size();
    if (length > 0) {
    for (int i = 0; i < customer.getCustomerContacts().size(); i++) {
    customerContactsDao.updateCustomerContactDetails(customer.getCustomerContacts().get(i), accountId);
    }
    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.CUSTOMER_CUSTOMER_CONTACT_UPDATED, " customer ", customer);
    } else {
    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.CUSTOMER_UPDATED, " customer ", customer);
    }
    } else {
    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_CUSTOMER, "", "");
    }
    } else {
    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
    }
    }*/
    
    /**
     * @author Ramesh
     * @date 13-05-2014
     * @param recentType='added' or recentType='modified'
     * @param userToken
     * @return details of recently added or modified customers
     */
    public Object getRecentCustomers(String recentType, String userToken) {
        if (util.isTokenValid(userToken)) {
                int accountId = util.accountIdForToken(userToken);
                List<Customer> customerList = new ArrayList<Customer>();
                int recentId;
                if (recentType.equals("added")) {
                    recentId = 0;
                } else {
                    recentId = 1;
                }
                customerList = customerDao.selectRecentCustomers(recentId, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " customerList ", customerList);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

   /**
     * @author anuja
     * @purpose import customer records
     * @param file
     * @param userToken
     * @return 
     */
    //Edited by nikhil :-2018-01-29 :- Download Error import file
    public Object createCustomerByImport(MultipartFile file, String userToken) {
        try {
            String validStatus = Constant.VALID;
            List<Integer> customerIdList = new ArrayList<Integer>();
            List<String> customerNmList = new ArrayList<String>();
            List<String[]> customerErrorList = new ArrayList<String[]>();
            String errorMessage = "";
//            String desktopPath = null;
            if (util.isTokenValid(userToken)) {
                //if(util.isSessionExpired(token)){
                    int accountId = util.accountIdForToken(userToken);
                    int userid=util.userIdForToken(userToken);
                    /*try {
                    desktopPath = System.getProperty("user.home") + "/Desktop";
                    } catch (Exception e) {
                    log4jLog.info(" createCustomerByImport " + e);
                    System.out.println("Exception caught =" + e.getMessage())0;
                    }*/
    //                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss.SSS");
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss_SSS");
                    Date d = new Date();
                    String date = dateFormat.format(d);
                    String saveFileTo = Constant.IMPORT_ERROR_FILES;
                    String errorFileNm = accountId + "_" + date + "_CustomerImportError.csv";
                    String csv = saveFileTo + errorFileNm;
    //            String csv = desktopPath + "/CustomerImportError_"+d+".csv";

                    List<CustomerCSV> list = MapCSVToCustomer.mapJavaBean(file);
                    for (int i = 0; i < 1; i++) {
    //                    List<CustomerCSV> arrayL = new ArrayList<CustomerCSV>();
    //                    arrayL.add(list.get(0));
                        if (((list.get(i).getCustomerName().equalsIgnoreCase("Customer Name")) && (list.get(i).getCustomerPrintas().equalsIgnoreCase("Customer PrintAs"))) && ((list.get(i).getCustomerLocation().equalsIgnoreCase("Customer Location")) && (list.get(i).getIsHeadOffice().equalsIgnoreCase("Isheadoffice")))) {
                            if (((list.get(i).getTerritory().equalsIgnoreCase("Territory")) && (list.get(i).getIndustry().equalsIgnoreCase("Industry"))) && ((list.get(i).getCustomerPhone1().equalsIgnoreCase("CustomerPhone1")) && (list.get(i).getCustomerPhone2().equalsIgnoreCase("CustomerPhone2")))) {
                                if (((list.get(i).getCustomerPhone3().equalsIgnoreCase("CustomerPhone3")) && (list.get(i).getCustomerFax1().equalsIgnoreCase("CustomerFax1"))) && ((list.get(i).getCustomerFax2().equalsIgnoreCase("CustomerFax2")) && (list.get(i).getCustomerEmail1().equalsIgnoreCase("CustomerEmail1")))) {
                                    if (((list.get(i).getCustomerEmail2().equalsIgnoreCase("CustomerEmail2")) && (list.get(i).getCustomerEmail3().equalsIgnoreCase("CustomerEmail3"))) && ((list.get(i).getCustomerWebsiteUrl1().equalsIgnoreCase("CustomerWebsiteUrl1")) && (list.get(i).getCustomerWebsiteUrl2().equalsIgnoreCase("CustomerWebsiteUrl2")))) {
                                        if (list.get(i).getCustomerAddress1().equalsIgnoreCase("CustomerAddress")) {
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
                    customerErrorList.add(new String[]{"Customer Name", "Customer PrintAs", "Customer Location", "Isheadoffice", "Territory", "Industry", "CustomerPhone1", "CustomerPhone2", "CustomerPhone3", "CustomerFax1", "CustomerFax2", "CustomerEmail1", "CustomerEmail2", "CustomerEmail3", "CustomerWebsiteUrl1", "CustomerWebsiteUrl2", "CustomerAddress","Error Message"});
//                    customerErrorList.add(new String[]{"Company Name","Field Name","Error Meassage"});
                    for (int i = 1; i < list.size(); i++) {
                        Customer customer = new Customer();
                        String validStatus2 = Constant.VALID;

                        /*String add = list.get(i).getCustomerAddress1();
                        int l = add.length();
                        String add1 = add.substring(1, l - 1);
                        list.get(i).setCustomerAddress1(add1);

                        String nm = list.get(i).getCustomerName();
                        l = nm.length();
                        nm = nm.substring(1, l - 1);
                        list.get(i).setCustomerName(nm);

                        String lo = list.get(i).getCustomerLocation();
                        l = lo.length();
                        lo = lo.substring(1, l - 1);
                        list.get(i).setCustomerLocation(lo);*/
                        if (list.get(i).getCustomerName().equals("")) {
                            validStatus = FieldSenseUtils.notvalid(validStatus);
                            validStatus = validStatus + "Customer name is blank at line:" + (i + 1) + ".";
                            validStatus2 = validStatus;
//                            customerErrorList.add(new String[]{"----","Customer Name","Customer name is blank at line:" + (i + 1) + "."});
                            errorMessage=" Customer name is blank. ";
                        }
                        /* if (list.get(i).getCustomerPrintas().equals("")) {
                        validStatus = FieldSenseUtils.notvalid(validStatus);
                        validStatus = validStatus + "Invalid customer print as at line:" + (i + 1) + ".";
                        validStatus2 = validStatus;
                        }*/
                        if (list.get(i).getCustomerLocation().equals("")) {
                            validStatus = FieldSenseUtils.notvalid(validStatus);
                            validStatus = validStatus + "Customer Location is blank at line:" + (i + 1) + ".";
                            validStatus2 = validStatus;
//                            customerErrorList.add(new String[]{list.get(i).getCustomerName(),"Customer Location","Location identifier is blank at line:" + (i + 1) + "."});
                            errorMessage=errorMessage+" Customer Location is blank. ";
                        }
                        if (!(list.get(i).getCustomerPrintas().isEmpty())) {
                            /*String ps = list.get(i).getCustomerPrintas();
                            l = ps.length();
                            ps = ps.substring(1, l - 1);
                            list.get(i).setCustomerPrintas(ps);*/
                            customer.setCustomerPrintas(list.get(i).getCustomerPrintas());
                        } else {
                            customer.setCustomerPrintas(list.get(i).getCustomerName());
                        }

                        if (!(list.get(i).getIsHeadOffice().isEmpty())) {
                            /* String head = list.get(i).getIsHeadOffice();
                            l = head.length();
                            head = head.substring(1, l - 1);
                            list.get(i).setIsHeadOffice(head);*/
    //                        if (!(FieldSenseUtils.isContainOnlyCharacters(list.get(i).getIsHeadOffice()))) {
    //                            validStatus = FieldSenseUtils.notvalid(validStatus);
    //                            validStatus = validStatus + "Invalid isheadOffice number at line:" + (i + 1) + ".";
    //                            validStatus2 = validStatus;
    //                        } else {
                            if (list.get(i).getIsHeadOffice().equalsIgnoreCase("Yes")) {
                                customer.setIsHeadOffice(true);
                            } else if (list.get(i).getIsHeadOffice().equalsIgnoreCase("no")) {
                                customer.setIsHeadOffice(false);
                            }
    //                        }
                        } else {
                            customer.setIsHeadOffice(Boolean.parseBoolean(""));
                        }

                        if (!(list.get(i).getTerritory().isEmpty())) {
                            /*String t = list.get(i).getTerritory();
                            l = t.length();
                            t = t.substring(1, l - 1);
                            list.get(i).setTerritory(t);*/
    //                        if (!(FieldSenseUtils.isContainOnlyCharacters(list.get(i).getTerritory()))) {
    //
    //                            validStatus = FieldSenseUtils.notvalid(validStatus);
    //                            validStatus = validStatus + "Invalid territory number at line:" + (i + 1) + ".";
    //                            validStatus2 = validStatus;
    //                        } else {

                            boolean chk=false;
                            List<TerritoryCategory> territoryCategoryList = territoryCategoryDao.selectAllActiveTerritoryCategory(accountId);
                            for(TerritoryCategory tcat:territoryCategoryList){
                                if(tcat.getCategoryName().trim().equalsIgnoreCase(list.get(i).getTerritory().trim())){
                                    chk=true;
                                    customer.setTerritory(tcat.getCategoryName());
                                    break;
                                }
                            }
                            if(chk==false){
                                customer.setTerritory("Unknown");
                            }
    //                        }
                        }else{
                            customer.setTerritory("Unknown");

                        }
                        if (!(list.get(i).getIndustry().isEmpty())) {
                            /*String in = list.get(i).getIndustry();
                            l = in.length();
                            in = in.substring(1, l - 1);
                            list.get(i).setIndustry(in);*/
    //                        if (!(FieldSenseUtils.isContainOnlyCharacters(list.get(i).getIndustry()))) {
    //                            validStatus = FieldSenseUtils.notvalid(validStatus);
    //                            validStatus = validStatus + "Invalid industry number at line:" + (i + 1) + ".";
    //                            validStatus2 = validStatus;
    //                        } else {
                            boolean chk=false;
                            List<IndustryCategory> categoryList = industryCategoryDao.selectAllActiveIndustryCategory(accountId);
                            for(IndustryCategory tcat:categoryList){
                                if(tcat.getCategoryName().trim().equalsIgnoreCase(list.get(i).getIndustry().trim())){
                                    chk=true;
                                    customer.setIndustry(tcat.getCategoryName());
                                    break;
                                }
                            }
                            if(chk==false){
                                customer.setIndustry("Unknown");
                            }
    //                        }
                        }else{
                            customer.setIndustry("Unknown");
                        }
                        if (list.get(i).getCustomerAddress1().equals("")) {
                            validStatus = FieldSenseUtils.notvalid(validStatus);
                            validStatus = validStatus + "Customer address is blank at line:" + (i + 1) + ".";
                            validStatus2 = validStatus;
                            errorMessage=errorMessage+" Customer address is blank. ";
                        }

                        if (!(list.get(i).getCustomerPhone1().isEmpty())) {
                            /* String ph = list.get(i).getCustomerPhone1();
                            l = ph.length();
                            ph = ph.substring(1, l - 1);
                            list.get(i).setCustomerPhone1(ph);*/
    //                        if (!(FieldSenseUtils.isContainOnlyNumbers(list.get(i).getCustomerPhone1()))) {
    //                            validStatus = FieldSenseUtils.notvalid(validStatus);
    //                            validStatus = validStatus + "Invalid cutomer phone1 at line:" + (i + 1) + ".";
    //                            validStatus2 = validStatus;
    //                        } else {
    //                            customer.setCustomerPhone1(list.get(i).getCustomerPhone1());
    //                        }
//                            System.out.println("list.get(i).getCustomerPhone1()+++ "+list.get(i).getCustomerPhone1());
//                            System.out.println("ContainOnlyNumbers :- "+FieldSenseUtils.isContainOnlyNumbers(list.get(i).getCustomerPhone1()));
                            if (!(FieldSenseUtils.isContainOnlyNumbers(list.get(i).getCustomerPhone1()))) {
                                validStatus = FieldSenseUtils.notvalid(validStatus);
//                                System.out.println("validStatus getCustomerPhone1:- "+validStatus);
                                validStatus = validStatus + "Invalid cutomer phone1 at line:" + (i + 1) + ".";
                                validStatus2 = validStatus;
                                errorMessage=errorMessage+" Invalid cutomer phone1. ";
//                                customerErrorList.add(new String[]{list.get(i).getCustomerName(),"CustomerPhone1","Invalid cutomer phone1 at line:" + (i + 1) + "."});
                                
                            } else {
                            customer.setCustomerPhone1(list.get(i).getCustomerPhone1());
                            }

                        }
                        if (!(list.get(i).getCustomerPhone2().isEmpty())) {

                            /* String ph2 = list.get(i).getCustomerPhone2();
                            l = ph2.length();
                            ph2 = ph2.substring(1, l - 1);
                            list.get(i).setCustomerPhone2(ph2);*/
    //                        if (!(FieldSenseUtils.isContainOnlyNumbers(list.get(i).getCustomerPhone2()))) {
    //                            validStatus = FieldSenseUtils.notvalid(validStatus);
    //                            validStatus = validStatus + "Invalid customer phone2 at line:" + (i + 1) + ".";
    //                            validStatus2 = validStatus;
    //                        } else {
    //                            customer.setCustomerPhone2(list.get(i).getCustomerPhone2());
    //                        }
                            if (!(FieldSenseUtils.isContainOnlyNumbers(list.get(i).getCustomerPhone2()))) {
                                validStatus = FieldSenseUtils.notvalid(validStatus);
                                validStatus = validStatus + "Invalid customer phone2 at line:" + (i + 1) + ".";
                                validStatus2 = validStatus;
                                 errorMessage=errorMessage+" Invalid customer phone2. ";
//                                customerErrorList.add(new String[]{list.get(i).getCustomerName(),"CustomerPhone2","Invalid customer phone2 at line:" + (i + 1) + "."});
                            } else {
                            customer.setCustomerPhone2(list.get(i).getCustomerPhone2());
                            }

                        }
                        if (!(list.get(i).getCustomerPhone3().isEmpty())) {

                            /*  String ph3 = list.get(i).getCustomerPhone3();
                            l = ph3.length();
                            ph3 = ph3.substring(1, l - 1);
                            list.get(i).setCustomerPhone3(ph3);*/
    //                        if (!(FieldSenseUtils.isContainOnlyNumbers(list.get(i).getCustomerPhone3()))) {
    //                            validStatus = FieldSenseUtils.notvalid(validStatus);
    //                            validStatus = validStatus + "Invalid customer phone3 at line:" + (i + 1) + ".";
    //                            validStatus2 = validStatus;
    //                        } else {
    //                            customer.setCustomerPhone3(list.get(i).getCustomerPhone3());
    //                        }
                            if (!(FieldSenseUtils.isContainOnlyNumbers(list.get(i).getCustomerPhone3()))) {
                                validStatus = FieldSenseUtils.notvalid(validStatus);
                                validStatus = validStatus + "Invalid customer phone3 at line:" + (i + 1) + ".";
                                validStatus2 = validStatus;
                                 errorMessage=errorMessage+" Invalid customer phone3. ";
//                                customerErrorList.add(new String[]{list.get(i).getCustomerName(),"CustomerPhone3","Invalid customer phone3 at line:" + (i + 1) + "."});
                            } else {
                            customer.setCustomerPhone3(list.get(i).getCustomerPhone3());
                            }
                        }
                        if (!(list.get(i).getCustomerFax1().isEmpty())) {

                            /* String fax1 = list.get(i).getCustomerFax1();
                            l = fax1.length();
                            fax1 = fax1.substring(1, l - 1);
                            list.get(i).setCustomerFax1(fax1);*/
    //                        if (!(FieldSenseUtils.isContainOnlyNumbers(list.get(i).getCustomerFax1()))) {
    //                            validStatus = FieldSenseUtils.notvalid(validStatus);
    //                            validStatus = validStatus + "Invalid fax number1 at line:" + (i + 1) + ".";
    //                            validStatus2 = validStatus;
    //                        } else {
    //                            customer.setCustomerFax1(list.get(i).getCustomerFax1());
    //                        }
    //
    ////                        if (!(FieldSenseUtils.isContainOnlyNumbers(list.get(i).getCustomerFax1()))) {
    ////                            validStatus = FieldSenseUtils.notvalid(validStatus);
    ////                            validStatus = validStatus + "Invalid fax number1 at line:" + (i + 1) + ".";
    ////                            validStatus2 = validStatus;
    ////                        } else {
                            customer.setCustomerFax1(list.get(i).getCustomerFax1());
    ////                        }

                        }
                        if (!(list.get(i).getCustomerFax2().isEmpty())) {

                            /* String fax2 = list.get(i).getCustomerFax2();
                            l = fax2.length();
                            fax2 = fax2.substring(1, l - 1);
                            list.get(i).setCustomerFax2(fax2);*/
    //                        if (!(FieldSenseUtils.isContainOnlyNumbers(list.get(i).getCustomerFax2()))) {
    //                            validStatus = FieldSenseUtils.notvalid(validStatus);
    //                            validStatus = validStatus + "Invalid fax number2 at line:" + (i + 1) + ".";
    //                            validStatus2 = validStatus;
    //                        } else {
    //                            customer.setCustomerFax2(list.get(i).getCustomerFax2());
    //                        }
    //
    ////                        if (!(FieldSenseUtils.isContainOnlyNumbers(list.get(i).getCustomerFax2()))) {
    ////                            validStatus = FieldSenseUtils.notvalid(validStatus);
    ////                            validStatus = validStatus + "Invalid fax number2 at line:" + (i + 1) + ".";
    ////                            validStatus2 = validStatus;
    ////                        } else {
                            customer.setCustomerFax2(list.get(i).getCustomerFax2());
    //                        }

                        }

                        if (!(list.get(i).getCustomerEmail1().isEmpty())) {

                            /* String em1 = list.get(i).getCustomerEmail1();
                            l = em1.length();
                            em1 = em1.substring(1, l - 1);
                            list.get(i).setCustomerEmail1(em1);*/
                            if (!(FieldSenseUtils.isValidEmailAddress(list.get(i).getCustomerEmail1()))) {
                                validStatus = FieldSenseUtils.notvalid(validStatus);
                                validStatus = validStatus + "Invalid email address1 at line:" + (i + 1) + ".";
                                validStatus2 = validStatus;
                                errorMessage=errorMessage+" Invalid email address1.";
//                                customerErrorList.add(new String[]{list.get(i).getCustomerName(),"CustomerEmail1","Invalid email address1 at line:" + (i + 1) + "."});
                            }
//                            } else if (!(FieldSenseUtils.isDomainValid(list.get(i).getCustomerEmail1()))) {
//                                validStatus = FieldSenseUtils.notvalid(validStatus);
//                                validStatus = validStatus + "Public domain is not allowed at line:" + (i + 1) + ".";
//                                validStatus2 = validStatus;
//                            } 
                                else {
                                customer.setCustomerEmail1(list.get(i).getCustomerEmail1().trim());
                            }
                        }
                        if (!(list.get(i).getCustomerEmail2().isEmpty())) {
                            /*String em2 = list.get(i).getCustomerEmail2();
                            l = em2.length();
                            em2 = em2.substring(1, l - 1);
                            list.get(i).setCustomerEmail2(em2);*/
                            if (!(FieldSenseUtils.isValidEmailAddress(list.get(i).getCustomerEmail2()))) {
                                validStatus = FieldSenseUtils.notvalid(validStatus);
                                validStatus = validStatus + "Invalid email address2 at line:" + (i + 1) + ".";
                                validStatus2 = validStatus;
                                errorMessage=errorMessage+" Invalid email address2. ";
//                                customerErrorList.add(new String[]{list.get(i).getCustomerName(),"CustomerEmail2","Invalid email address2 at line:" + (i + 1) + "."});
                            }
//                            else if (!(FieldSenseUtils.isDomainValid(list.get(i).getCustomerEmail2()))) {
//    //                            validStatus = FieldSenseUtils.notvalid(validStatus);
//    //                            validStatus = validStatus + "Public domain is not allowed at line:" + (i + 1) + ".";
//    //                            validStatus2 = validStatus;
//                            } 
                            else {
                                customer.setCustomerEmail2(list.get(i).getCustomerEmail2().trim());
                            }
                        }
                        if (!(list.get(i).getCustomerEmail3().isEmpty())) {
                            /* String em3 = list.get(i).getCustomerEmail3();
                            l = em3.length();
                            em3 = em3.substring(1, l - 1);
                            list.get(i).setCustomerEmail3(em3);*/
                            if (!(FieldSenseUtils.isValidEmailAddress(list.get(i).getCustomerEmail3()))) {
                                validStatus = FieldSenseUtils.notvalid(validStatus);
                                validStatus = validStatus + "Invalid email address3 at line:" + (i + 1) + ".";
                                validStatus2 = validStatus;
                                errorMessage=errorMessage+" Invalid email address3. ";
//                                 customerErrorList.add(new String[]{list.get(i).getCustomerName(),"CustomerEmail3","Invalid email address3 at line:" + (i + 1) + "."});
                            } 
//                                else if (!(FieldSenseUtils.isDomainValid(list.get(i).getCustomerEmail3()))) {
    //                            validStatus = FieldSenseUtils.notvalid(validStatus);
    //                            validStatus = validStatus + "Public domain is not allowed at line:" + (i + 1) + ".";
    //                            validStatus2 = validStatus;
                            
                                        else {
                                customer.setCustomerEmail3(list.get(i).getCustomerEmail3().trim());
                            }
                        }

                        if (!(list.get(i).getCustomerWebsiteUrl1().isEmpty())) {
                            /*String url1 = list.get(i).getCustomerWebsiteUrl1();
                            l = url1.length();
                            url1 = url1.substring(1, l - 1);
                            list.get(i).setCustomerWebsiteUrl1(url1);*/
                            customer.setCustomerWebsiteUrl1(list.get(i).getCustomerWebsiteUrl1());
                        }
                        if (!(list.get(i).getCustomerWebsiteUrl2().isEmpty())) {
                            /* String url2 = list.get(i).getCustomerWebsiteUrl2();
                            l = url2.length();
                            url2 = url2.substring(1, l - 1);
                            list.get(i).setCustomerWebsiteUrl2(url2);*/
                            customer.setCustomerWebsiteUrl2(list.get(i).getCustomerWebsiteUrl2());
                        }
                        /* if (list.get(i).getCustomerWebsiteUrl1().equals("")) {
                        validStatus = FieldSenseUtils.notvalid(validStatus);
                        validStatus = validStatus + "Invalid website at line:" + (i + 1) + ".";
                        validStatus2 = validStatus;
                        }
                        if (list.get(i).getCustomerWebsiteUrl2().equals("")) {
                        validStatus = FieldSenseUtils.notvalid(validStatus);
                        validStatus = validStatus + "Invalid website at line:" + (i + 1) + ".";
                        validStatus2 = validStatus;
                        }*/

                        if (!(validStatus2.equals(Constant.VALID))) {
//                            customerErrorList.add(new String[]{"for tesing purpose"});
//                            System.out.println("for tesing purpose");
                            customerErrorList.add(new String[]{list.get(i).getCustomerName(), list.get(i).getCustomerPrintas(), list.get(i).getCustomerLocation(), list.get(i).getIsHeadOffice(), list.get(i).getTerritory(), list.get(i).getIndustry(), list.get(i).getCustomerPhone1(), list.get(i).getCustomerPhone2(), list.get(i).getCustomerPhone3(), list.get(i).getCustomerFax1(), list.get(i).getCustomerFax2(), list.get(i).getCustomerEmail1(), list.get(i).getCustomerEmail2(), list.get(i).getCustomerEmail3(), list.get(i).getCustomerWebsiteUrl1(), list.get(i).getCustomerWebsiteUrl2(), list.get(i).getCustomerAddress1(),errorMessage});
                            errorMessage="";
                            continue;
                        }
                        if (validStatus2.equals(Constant.VALID)) {
                            customer.setCustomerName(list.get(i).getCustomerName());
                            customer.setCustomerLocation(list.get(i).getCustomerLocation());
                            customer.setCustomerAddress1(list.get(i).getCustomerAddress1());
                            int customerId = customerDao.insertCustomerByImport(customer, accountId,userid);
                            if (customerId != 0) {
                                customerIdList.add(customerId);
                                customerNmList.add(customer.getCustomerName());
                            }
                        }
                    }
                    if ((customerErrorList.size() > 1)) {
                        CSVWriter writer = new CSVWriter(new FileWriter(csv));
                        writer.writeAll(customerErrorList);
                    //    System.out.println("CSV written successfully.");
                        writer.close();
                    }
                    int totalCreated = customerNmList.size();
                    int totalNotCreated = customerErrorList.size() - 1;
//                    System.out.println("totalCreated "+totalCreated);
//                    System.out.println("totalNotCreated "+totalNotCreated);
                    if ((customerIdList.size() > 0) && validStatus.equals(Constant.VALID)) {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.CUSTOMER_CREATED, " customerNmList ", customerNmList);
                    } else if ((customerIdList.size() > 0) && !(validStatus.equals(Constant.VALID))) {
    //                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, validStatus + " Remaining customers get created and or updated", " customerNmList ", customerNmList);
//                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, totalCreated + " Customers get created and or updated. " + validStatus, " ErrorFileName ", errorFileNm);
                                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED,totalCreated +" out of "+(totalCreated+totalNotCreated) +" customers are added & there is error in "+totalNotCreated+ " customers", " ErrorFileName ", errorFileNm);

                    } else {
//                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, validStatus + " Customer  not created . Please try again .", "ErrorFileName", errorFileNm);
                                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, totalCreated +" out of "+(totalCreated+totalNotCreated) +" customers are added & there is error in "+totalNotCreated+ " customers", "ErrorFileName", errorFileNm);

                    }
            } else {
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
            }
        } catch (NullPointerException e) {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Cutomer address may not be empty. Please try again . ", "", "");
        } catch (FileNotFoundException ex) {
         //   System.out.println(ex);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "File not uploaded. Please try again . ", "", "");
        } catch (IOException ex) {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "File not uploaded. Please try again . ", "", "");
        }

    }


    /**
     * @author Ramesh
     * @return 
     * @date 20-08-2014
     * @param customerId
     * @param userToken
     * @purpose If location is not available for customer then insert the location for customer if available nothing to do .
     */
    public Object insertCustomerLatLang(int customerId, String userToken) {
        log4jLog.info("inside insertCustomerLocation in customerManger");
        if (util.isTokenValid(userToken)) {
                int accountId = util.accountIdForToken(userToken);
                int userId = util.userIdForToken(userToken);
                if (customerDao.isCustomerValid(customerId, accountId)) {
                    if (!locationDao.isCustomerHasLocation(customerId, accountId)) {
                        String address = customerDao.selectCustomerAddress(customerId, accountId);
                        Location location = new Location();
                        location.setLatitude(-1);
                        location.setLangitude(-1);
                        location.setLocationType(customerId);
                        location.setUserId(userId);
                        try {
                            GoogleResponse res = new AddressConverter().convertToLatLong(address);
                            if (res.getStatus().equals("OK")) {
                                for (Result result : res.getResults()) {
                                    location.setLatitude(Double.parseDouble(result.getGeometry().getLocation().getLat()));
                                    location.setLangitude(Double.parseDouble(result.getGeometry().getLocation().getLng()));
                                }
                            }
                            locationDao.insertlocation(location, accountId);
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "", "");
                        } catch (IOException ex) {
                            locationDao.insertlocation(location, accountId);
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "", "");
                        }
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_CUSTOMER, "", "");
                }   
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * @author: anuja
     * @param token
     * @return 
     * @date: 19 Sep, 2014
     * @purpose: display list of industry
     */
    public Object getCustomersIndustry(String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                List<String> industryList = customerDao.selectCustomerIndustry(accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " industryList ", industryList);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /**
     * @author: anuja
     * @param token
     * @return 
     * @date: 19 Sep, 2014
     * @purpose: display list of territory
     */
    public Object getCustomersTerritory(String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                List<String> territoryList = customerDao.selectCustomerTerritory(accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " territoryList ", territoryList);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /**
     * 
     * @param allRequestParams
     * @param response
     * @param token
     * @return list of all customers with offset
     */
    public Object getCustomersWithLimit(Map<String,String> allRequestParams, String token,HttpServletResponse response) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                List<Customer> customerList = new ArrayList<Customer>();
                customerList = customerDao.selectCustomersWithLimt(allRequestParams,accountId);
                int Total=0;
                if(!customerList.isEmpty()){
                    Total=customerList.get(0).getCustomersCount();
                }
                return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " customerList ", customerList,Total);    
        } else {
            response.setContentType( MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }
    
    /**
     * 
     * @param allRequestParams
     * @param userId
     * @param token
     * @param response
     * @return list of all customers with Limit for specific user
     */
    public Object getUserCustomersWithLimit(Map<String,String> allRequestParams,int userId, String token,HttpServletResponse response) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                List<Customer> customerList = new ArrayList<Customer>();
                customerList = customerDao.selectUserCustomersWithLimt(allRequestParams,userId,accountId);
                int Total=0;
                if(!customerList.isEmpty()){
                    Total=customerList.get(0).getCustomersCount();
                }
                return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " customerList ", customerList,Total);    
        } else {
            response.setContentType( MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }
    

    /**
     *@author:anuja
     *@param customer
     * @param token
     * @return 
     *@purpose: update customer address and lat-long
     *@date:18 Dec,2014
     */
    public Object updateCustomerAddressLatLong(Customer customer, String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                if (customerDao.isCustomerValid(customer.getId(), accountId)) {
                    customer = customerDao.updateCustomerAddressLatLong(customer, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.CUSTOMER_UPDATED, " customer ", customer);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_CUSTOMER, "", "");
                }  
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /**
     * 
     * @param customer
     * @param token
     * @return 
     * @purpose Used to update customer address and customer lat long
     */
    public Object updateCustomerLatLong(Customer customer, String token) {
        if (util.isTokenValid(token)) {
                int accountId = util.accountIdForToken(token);
                if (customerDao.isCustomerValid(customer.getId(), accountId)) {
                    if (customerDao.updateCustomerAddressOnly(customer, accountId)) {
                        Location location = new Location();
                        location.setLocationType(customer.getId());
                        location.setLatitude(customer.getLasknownLatitude());
                        location.setLangitude(customer.getLasknownLangitude());
                        location.setUserId(util.userIdForToken(token));
                        if (locationDao.isCustomerHasLocation(customer.getId(), accountId)) {
                            locationDao.updateUserLocationOnlyWithLocationTypeId(location, accountId);
                        } else {
                            locationDao.insertlocation(location, accountId);
                        }
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.CUSTOMER_UPDATED, "", "");
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Customer updation failed.please try again .", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_CUSTOMER, "", "");
                }
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }
    
    /**
     * Siddhesh
     * @param customer
     * @param token
     * @return 
     * @purpose Used to update customer address and customer lat long
     */
    public Object getCustomerNearBy(double lat,double lang,int radius,String userToken) {
        if (util.isTokenValid(userToken)) {
                int accountId = util.accountIdForToken(userToken);
                     List<HashMap<String,Object>> mapOfData=customerDao.getNearByCustomers(lat,lang,radius,accountId);
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.CUSTOMER_UPDATED, "CustomerNearBy", mapOfData);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

}
