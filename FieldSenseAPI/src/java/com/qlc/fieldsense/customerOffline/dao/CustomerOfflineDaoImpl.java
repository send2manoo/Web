/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customerOffline.dao;

import static com.qlc.fieldsense.appointments.dao.AppointmentDaoImpl.log4jLog;
import com.qlc.fieldsense.appointments.model.Appointment;

import com.qlc.fieldsense.customer.model.Customer;
import com.qlc.fieldsense.customerContact.model.CustomerContacts;
import com.qlc.fieldsense.customerOffline.model.AppointmentOffline;
import com.qlc.fieldsense.customerOffline.model.CustomerOffline;
import com.qlc.fieldsense.customerOffline.model.CustomerOfflineWithFewData;
import com.qlc.fieldsense.customerOffline.model.LocalData;
import com.qlc.fieldsense.customerOffline.model.OfflineDataSync;
import com.qlc.fieldsense.customerOffline.model.CustomersOfflineRequestedList;
import com.qlc.fieldsense.customerOffline.model.TerritoryList;
import com.qlc.fieldsense.expense.dao.ExpenseDao;
import com.qlc.fieldsense.expense.model.Expense;
import com.qlc.fieldsense.expense.model.ExpenseManager;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.PhotoToIconCreator;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author jyoti
 */


public class CustomerOfflineDaoImpl implements CustomerOfflineDao {
        
    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("CustomerOfflineDaoImpl");

    /**
     *
     * @param accountId
     * @param userId
     * @param lastSync
     * @param currentTime
     * @return
     */
    @Override
    public List<CustomerOffline> selectCustomersOfflineAfterLastSync(int accountId, int userId, Timestamp lastSync,Timestamp currentTime) {
           StringBuilder query = new StringBuilder();
            
        Object param[] = null;
         
            String query1=" SELECT c.id,c.record_state FROM customers c INNER JOIN territory_categories tc ON c.territory = tc.category_name INNER JOIN user_territory t1 ON t1.teritory_id=tc.id INNER JOIN fieldsense.users u ON u.id=t1.user_id_fk WHERE u.id= ? AND c.modified_on BETWEEN ? AND ? ";
            param = new Object[]{userId,lastSync,currentTime};
                  
        log4jLog.info(" selectCustomersOfflineAfterLastSync " + query1);
        try {
           
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query1,param,new RowMapper<CustomerOffline>() {
                @Override
                public CustomerOffline mapRow(ResultSet rs, int i) throws SQLException {
                    CustomerOffline customer = new CustomerOffline();                    
                    customer.setId(rs.getInt("c.id"));
                    customer.setRecord_state(rs.getInt("c.record_state"));
                    return customer;
                }
            });

             
        } catch (Exception e) {
            log4jLog.info(" selectCustomersOfflineAfterLastSync " + e);
//            e.printStackTrace();
            return new ArrayList<CustomerOffline>();
        }
           
    }
    
    /**
     *
     * @param accountId
     * @param userId
     * @param customersRequestedList
     * @return
     */
    @Override
    public List<CustomerOfflineWithFewData> selectRequestedListOfCustomersOffline(int accountId, int userId, List<CustomersOfflineRequestedList> customersRequestedList) {
        int id;
        StringBuilder query=new StringBuilder();
        StringBuilder query1=new StringBuilder();
        CustomersOfflineRequestedList customersOfflineRequestedList;
//        long startTime = System.currentTimeMillis();
            try{                
                if(!(customersRequestedList.isEmpty())){  
                    query1.append(customersRequestedList.get(0).getCustId());                    
                    for(int i = 1 ; i < customersRequestedList.size() ; i++){
                        customersOfflineRequestedList=customersRequestedList.get(i);
                        id=customersOfflineRequestedList.getCustId();
                        query1.append(",").append(id);
                    }                        
                    query.append("SELECT c.id,c.customer_name,c.customer_printas,c.address1,c.customer_location_identifier,c.phone1,c.phone2,c.phone3,c.fax1,c.fax2,c.email1,c.email2,c.email3,c.website1,c.website2,IFNULL(l.latitude,0) latitude,IFNULL(l.longitude,0) longitude ");
                    query.append(" FROM customers c LEFT OUTER JOIN location as l ON l.location_type_id_fk=c.id WHERE c.id IN( ");
                    query.append(query1);
                    query.append(" ) ");
                }
            } catch (Exception e){
//                e.printStackTrace();
                log4jLog.info(" selectRequestedListOfCustomersOffline " + e);
                return null;
            }
        
            log4jLog.info(" selectRequestedListOfCustomersOffline " + query);
            try {
                return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), new RowMapper<CustomerOfflineWithFewData>() {
                @Override
                public CustomerOfflineWithFewData mapRow(ResultSet rs, int i) throws SQLException {
                    CustomerOfflineWithFewData customer = new CustomerOfflineWithFewData();                    
                    customer.setId(rs.getInt("c.id"));
                    customer.setCustomerName(rs.getString("c.customer_name"));                    
                    customer.setCustomerPrintas(rs.getString("c.customer_printas"));
                    customer.setCustomerAddress1(rs.getString("c.address1"));
                    customer.setCustomerLocation(rs.getString("c.customer_location_identifier"));
                    customer.setLasknownLatitude(rs.getDouble("latitude"));
                    customer.setLasknownLangitude(rs.getDouble("longitude"));
                    customer.setCustomerPhone1(rs.getString("c.phone1"));
                    customer.setCustomerPhone2(rs.getString("c.phone2"));
                    customer.setCustomerPhone3(rs.getString("c.phone3"));
                    customer.setCustomerFax1(rs.getString("c.fax1"));
                    customer.setCustomerFax2(rs.getString("c.fax2"));
                    customer.setCustomerEmail1(rs.getString("c.email1"));
                    customer.setCustomerEmail2(rs.getString("c.email2"));
                    customer.setCustomerEmail3(rs.getString("c.email3"));
                    customer.setCustomerWebsiteUrl1(rs.getString("c.website1"));
                    customer.setCustomerWebsiteUrl2(rs.getString("c.website2"));
                    return customer;
                }
                });            
                
            } catch (Exception e) {
                log4jLog.info(" selectRequestedListOfCustomersOffline " + e);
//                e.printStackTrace();
                return new ArrayList<CustomerOfflineWithFewData>();
            }
//            finally{
//                 elapsedTime = System.currentTimeMillis() - startTime;
//                System.out.println("sec After object set : " + elapsedTime);
//            }
    }
    
    /**
     *
     * @param offlineDataSync
     * @param accountId
     * @return
     */
    @Override
    public boolean insertOfflineDataCustomer(OfflineDataSync offlineDataSync, int accountId) {
        
        //System.out.println("You have entered the world of offline");
     final List<CustomerOffline> customer =(List)offlineDataSync.getCustomer();
     if(!customer.isEmpty())
     {
 //    List<Appointment> appointment =(List)offlineDataSync.getAppointments();
     //   System.out.println("appontments"+appointment.get(0).getCustomer().getId());
        String sql = "INSERT INTO customers " +
	"(customer_name, email1, phone1,address1,mobile_temp_id) VALUES (?, ?, ?,?,?)";
        try{
    FieldSenseUtils.getJdbcTemplateForAccount(accountId).batchUpdate(sql,new BatchPreparedStatementSetter() {

          public void setValues(PreparedStatement ps, int i) throws SQLException {
              CustomerOffline customerObj=customer.get(i);
             // System.out.println("Sidd"+customerObj.getMobileTempId());
              ps.setString(1,customerObj.getCustomerName());
              ps.setString(2,customerObj.getCustomerEmail1());
              ps.setString(3,customerObj.getCustomerPhone1());
              ps.setString(4,customerObj.getCustomerAddress1());
              ps.setInt(5,customerObj.getMobileTempId());
          }

          public int getBatchSize() {
             return customer.size();
          }
      });
        }catch(Exception e){
            log4jLog.info("insertOfflineDataCustomer " + e);
//           e.printStackTrace();
        return false;
        }
           return true;
     }else{
     return true;
     }
    }
 
    /**
     *
     * @param offlineDataSync
     * @param accountId
     * @return
     */
    public boolean insertOfflineDataAppointment(OfflineDataSync offlineDataSync, int accountId) {
        
     //   System.out.println("You have entered the world of offline");
        
      List<Appointment> appointment =(List)offlineDataSync.getAppointments();
            if(!appointment.isEmpty())
      {
     String sql = "SELECT id FROM customers WHERE record_state !=3 AND mobile_temp_id=?";
    Customer customer;
    Appointment appointmentObj;
   try{
     for(int i=0;i<appointment.size();i++)
     {
          appointmentObj=appointment.get(i);
         Object  param[] = new Object[]{appointmentObj.getCustomer().getId()};
       //  System.err.println(appointmentObj.getCustomer().getId());
         int custId=appointmentObj.getCustomer().getId();
       custId=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(sql,param,Integer.class);
//       if(custId==0)
//       {
//           System.out.println("Hello error"+custId);
//       }
     //  System.out.println("mobile id exchanged"+custId);  
          customer=new Customer();
          customer.setId(custId);
          appointmentObj.setCustomer(customer);
    // System.out.println("mobile id exchanged"+appointmentObj.getCustomer().getId());   
    
      }
   }catch(Exception e)
   {
       log4jLog.info("insertOfflineDataAppointment " + e);
//        e.printStackTrace();
   }
  
     final List<Appointment> appointmentInsert=appointment;
     sql = "INSERT INTO appointments " +
	"(customer_id_fk, appointment_title, user_id_fk,assigned_id_fk,appointment_time,appointment_end_time,"
             + "appointment_description,purpose_id_fk,created_by_id_fk,updated_by_id_fk,mobile_temp_id) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try{
    FieldSenseUtils.getJdbcTemplateForAccount(accountId).batchUpdate(sql,new BatchPreparedStatementSetter() {

          public void setValues(PreparedStatement ps, int i) throws SQLException {
              Appointment appointmentObj=appointmentInsert.get(i);
          appointmentObj.setDateTime(FieldSenseUtils.converDateToTimestampforBatchUpdate(appointmentObj.getSdateTime()));
          appointmentObj.setEndTime(FieldSenseUtils.converDateToTimestampforBatchUpdate(appointmentObj.getSendTime()));
              ps.setInt(1,appointmentObj.getCustomer().getId());
              ps.setString(2,appointmentObj.getTitle());
              ps.setInt(3,appointmentObj.getCreatedBy().getId());
              ps.setInt(4,appointmentObj.getAssignedTo().getId());
              ps.setTimestamp(5,appointmentObj.getDateTime());
              ps.setTimestamp(6,appointmentObj.getEndTime());
              ps.setString(7,appointmentObj.getDescription());
              ps.setInt(8,appointmentObj.getPurpose().getId());
               ps.setInt(9,appointmentObj.getCreatedBy().getId());
              ps.setInt(10,appointmentObj.getUpdated_by().getId());
              ps.setInt(11,appointmentObj.getMobileTempId());
 
          }
          

          public int getBatchSize() {
             return appointmentInsert.size();
          }
      });
        }catch(Exception e){
            log4jLog.info("insertOfflineDataAppointment " + e);
//            e.printStackTrace();
            return false;
        }
        
        
    
           return true;
      
      
    }else{
        return true;   
      }
    }

    /**
     *
     * @param offlineDataSync
     * @param accountId
     * @return
     */
    public List<CustomerOffline> getAllOfflineCustomers(OfflineDataSync offlineDataSync, int accountId) {
        String sql="select id,customer_name,address1,phone1,mobile_temp_id from customers where record_state !=3 AND mobile_temp_id=?";
        final List<CustomerOffline> customer =(List)offlineDataSync.getCustomer();
        List<CustomerOffline> customerInsert=new ArrayList<CustomerOffline>();
        CustomerOffline customerOffline=new CustomerOffline();
        for(int j=0;j<customer.size();j++)
        {
            Object param[] = new Object[]{customer.get(j).getMobileTempId()};
            
        customerOffline=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(sql.toString(), param, new RowMapper<CustomerOffline>() {

                @Override
                public CustomerOffline mapRow(ResultSet rs, int i) throws SQLException {
                  CustomerOffline customerOffline=customer.get(i);
                  customerOffline.setId(rs.getInt(("id")));
                  customerOffline.setCustomerName(rs.getString("customer_name"));
                  customerOffline.setCustomerAddress1(rs.getString("address1"));
                  customerOffline.setCustomerPhone1(rs.getString("phone1"));
                  customerOffline.setMobileTempId(rs.getInt("mobile_temp_id"));
                  return customerOffline;
                }
                
            });
        customerInsert.add(customerOffline);
    }
       return customerInsert;
    }

    /**
     *
     * @param offlineDataSync
     * @param accountId
     * @return
     */
    public List<Appointment> getAllOfflineAppointments(OfflineDataSync offlineDataSync, int accountId) {
        String sql="select id,appointment_title,customer_id_fk,customer_contact_id_fk,assigned_id_fk,appointment_time,appointment_end_time,mobile_temp_id from appointments where mobile_temp_id=?";
        final List<Appointment> appointment =(List)offlineDataSync.getAppointments();
        List<Appointment> appointmentInsert=new ArrayList<Appointment>();
        Appointment appointmentObj=new Appointment();
        for(int j=0;j<appointment.size();j++)
        {
            Object param[] = new Object[]{appointment.get(j).getMobileTempId()};
            
        appointmentObj=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(sql.toString(), param, new RowMapper<Appointment>() {

                @Override
                public Appointment mapRow(ResultSet rs, int i) throws SQLException {
                  Appointment appointmentOffline=appointment.get(i);
                  Customer customer=new Customer();
                  CustomerContacts customerContacts=new CustomerContacts();
                    User user=new User();
                  appointmentOffline.setId(rs.getInt(("id")));
                  appointmentOffline.setTitle(rs.getString("appointment_title"));
                  customer.setId(rs.getInt("customer_id_fk"));
                  appointmentOffline.setCustomer(customer);
                  customerContacts.setId(rs.getInt("customer_contact_id_fk"));
                  appointmentOffline.setCustomerContact(customerContacts);
                  user.setId(rs.getInt("assigned_id_fk"));
                  appointmentOffline.setAssignedTo(user);
                  appointmentOffline.setDateTime(rs.getTimestamp("appointment_time"));
                  appointmentOffline.setEndTime(rs.getTimestamp("appointment_end_time"));
                  appointmentOffline.setMobileTempId(rs.getInt("mobile_temp_id"));
                  return appointmentOffline;
                }
              
            });
        appointmentInsert.add(appointmentObj);
    }
       return appointmentInsert;
    }

    public List<HashMap<String,String>> getAllOfflineData(OfflineDataSync offlineDataSync, int accountId, int userId) {
       List<HashMap<String,String>> customerInfo=new ArrayList<HashMap<String, String>>();
       HashMap<String,String> customerMapInfo=new HashMap<String, String>();
       List<LocalData> offlineDataList=offlineDataSync.getLocalData();
       String query;
       Object param[]={};
       Object param1[]={};
       LocalData offlineData;
       int id=0;
       int appointmentPositionId;
       int count=0;
       String mobileCustomerId="";
       String mobileAppointmentId="";
       List<Expense> expenseList;
       List<AppointmentOffline> appointmentsList;
       
       int appointmentId=0;
     //   System.out.println("Data"+offlineDataList.size());
       boolean flag=true;
       for(int i=0;i<offlineDataList.size();i++)
       {
       offlineData=offlineDataList.get(i);
         //System.out.println("sidd data"+offlineDataList.size());
       expenseList=offlineData.getExpense();
       appointmentsList=offlineData.getAppointments();
       if(offlineData.getId().contains("temp"))
       {
          mobileCustomerId=offlineData.getId();
        //  System.out.println(mobileCustomerId);
       query = "INSERT INTO customers(customer_name,address1,customer_printas,customer_location_identifier,territory,industry,created_by_id_fk,modified_by_id_fk) "
               + "VALUES(?,?,?,?,?,?,?,?)";
            param = new Object[]{offlineData.getCustomerName(),offlineData.getCustomerAddress1(),offlineData.getCustomerPrintas(),offlineData.getCustomerLocation(),offlineData.getTerritory(),offlineData.getIndustry(),userId,userId};
     
        try {
            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                 id= FieldSenseUtils.getMaxIdForTable("customers", accountId);
                 offlineData.setId(Integer.toString(id));
                 //System.out.println("whndiwhndi"+id);
                 
                 
                } else {
                    customerMapInfo.put("error", "customer not inserted");
                    customerInfo.add(customerMapInfo);
                    return customerInfo;
                }
            }
            HashMap<String,String> customerNameInfo=new HashMap<String, String>();
            customerNameInfo.put("id",Integer.toString(id));
                 customerNameInfo.put("Name",offlineData.getCustomerName());
                 customerNameInfo.put("mobileId",mobileCustomerId);
                 //System.out.println("Siddhiwdi"+offlineData.getCustomerName()+mobileId+Integer.toString(id));
                // System.out.println("Count"+count++);
                 customerInfo.add(i, customerNameInfo);
                 //   System.out.println("");
        } catch (Exception e) {
            //log4jLog.info(" insertUsersTravelLog " + e);
//            e.printStackTrace();
             customerMapInfo.put("error", "customer not inserted");
                    customerInfo.add(customerMapInfo);
                    return customerInfo;
            
           
        }
       }
       else{
       id=Integer.parseInt(offlineData.getId());
       }
        //System.out.println("adqdqd "+mobileCustomerId);
           if(!(offlineData.getAppointments().isEmpty())){
           for (AppointmentOffline appointmentsList1 : appointmentsList) {
           //    System.out.println("dddddd");
               AppointmentOffline appointmentObj = appointmentsList1;
            ///   System.out.println("efijweifjeiofj "+appointmentObj.getCustomer().getId()+"="+mobileCustomerId+" "+appointmentsList.size()+" "+i);
               //if(appointmentObj.getCustomer().getId().equals(mobileCustomerId)){
                   mobileAppointmentId=appointmentObj.getId();
                   appointmentObj.setDateTime(FieldSenseUtils.converDateToTimestampforBatchUpdate(appointmentObj.getSdateTime()));
                   appointmentObj.setEndTime(FieldSenseUtils.converDateToTimestampforBatchUpdate(appointmentObj.getSendTime()));
                   query ="INSERT INTO appointments " +
                           "(customer_id_fk, appointment_title, user_id_fk,assigned_id_fk,appointment_time,appointment_end_time,"
                           + "appointment_description,purpose_id_fk,created_by_id_fk,updated_by_id_fk) VALUES (?,?,?,?,?,?,?,?,?,?)";
                     String query1 = "SELECt MAX(appointment_possition) FROM appointments_possition WHERE user_id_fk=?";
                   param = new Object[]{id,appointmentObj.getTitle(),userId,appointmentObj.getAssignedTo().getId(),appointmentObj.getDateTime(),appointmentObj.getEndTime(),appointmentObj.getDescription(),appointmentObj.getPurpose().getId(),appointmentObj.getCreatedBy().getId(),appointmentObj.getUpdated_by().getId()};
                   param1=new Object[]{userId};
                   try {
                       synchronized (this) {
                           if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                               appointmentId= FieldSenseUtils.getMaxIdForTable("appointments", accountId);
                               appointmentPositionId=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query1, param1, Integer.class);
                           } else {
                               customerMapInfo.put("error", "appointment not inserted");
                               customerInfo.add(customerMapInfo);
                               return customerInfo;
                           }
                           query = "INSERT INTO appointments_possition(appointment_id_fk,user_id_fk,appointment_possition,cretaed_on,modified_on) VALUES (?,?,?,now(),now())";
                           param= new Object[]{appointmentId, userId, appointmentPositionId+1};
                         
                           if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) <= 0) {
                                customerMapInfo.put("error", "appointment position not inserted");
                                customerInfo.add(customerMapInfo);
                                return customerInfo;
                                 } 
                       }
                       // appointmentMap.put(mobileAppointmentId, Integer.toString(appointmentId));
                   } catch (Exception e) {
                       log4jLog.info(" insertUsersTravelLog " + e);
//                       e.printStackTrace();
                       customerMapInfo.put("error", "appointment not inserted");
                       customerInfo.add(customerMapInfo);
                       return customerInfo;
                       
                   }
              // }    
               if(!expenseList.isEmpty())
               {
                   for(int k=0;k<expenseList.size();k++)
                   {
                       Expense expenseObj=expenseList.get(k);
                       if(expenseObj.getAppointmentTempId().equalsIgnoreCase(mobileAppointmentId)){
                           expenseObj.setAppointmentId(appointmentId);
                           expenseObj.setCustomerId(id);
                           int expenseId=this.insertExpense(expenseObj, accountId);
                           File oldfile = new File(Constant.IMAGE_UPLOAD_PATH + expenseObj.getExpenseImage().getImageURL());
                           PhotoToIconCreator photoToIconCreator = new PhotoToIconCreator();
                           try {
                               photoToIconCreator.uploadExpenseImage(oldfile, "expense_" + accountId + "_" + userId + "_" + expenseId);
                           } catch (IOException ex) {
                               java.util.logging.Logger.getLogger(ExpenseManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    String imageName = Constant.IMAGE_UPLOAD_PATH + "expense" + "_" + accountId + "_" + userId + "_" + expenseId + ".png";
                    File newfile = new File(imageName);
                    oldfile.renameTo(newfile);
        } 
        }
        }  }
             }
       
        if(!expenseList.isEmpty())
        {
        for(int k=0;k<expenseList.size();k++){
         Expense expenseObj=expenseList.get(k);
        if(expenseObj.getAppointmentTempId().equalsIgnoreCase("0")){
        expenseObj.setAppointmentId(0);
            expenseObj.setCustomerId(0);
            int expenseId=this.insertExpense(expenseObj, accountId);
                    File oldfile = new File(Constant.IMAGE_UPLOAD_PATH + expenseObj.getExpenseImage().getImageURL());
                    PhotoToIconCreator photoToIconCreator = new PhotoToIconCreator();
                    try {
                        photoToIconCreator.uploadExpenseImage(oldfile, "expense_" + accountId + "_" + userId + "_" + expenseId);
                    } catch (IOException ex) {
                        java.util.logging.Logger.getLogger(ExpenseManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    String imageName = Constant.IMAGE_UPLOAD_PATH + "expense" + "_" + accountId + "_" + userId + "_" + expenseId + ".png";
                    File newfile = new File(imageName);
                    oldfile.renameTo(newfile);
        
        }
        }
        }
       
       
       }
      // System.out.println("Flag sid"+flag);
       return customerInfo;
    }
    
    /**
     *
     * @param expense
     * @param accountId
     * @return
     */
    public int insertExpense(Expense expense,int accountId){
    StringBuilder query = new StringBuilder();
        /*query.append("INSERT INTO expenses(appointment_id_fk,customer_id_fk,user_id_fk,expense_name,desription,amount_spent,status,");
        query.append("expense_time,submitted_on,created_on) VALUES (?,?,?,?,?,?,0,?,now(),now())");*/
        String defdatequery="SELECT distinct(default_date) FROM expenses where (status=1 || status=0 || status=2) and default_date!='1111:11:11 11:11:11'";
        Timestamp time=null;
        try{
            time= FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(defdatequery,Timestamp.class);   
        }catch (Exception e) {
            log4jLog.info(" insert Expense :" + e);
//            e.printStackTrace();
            time=null;
        }
        Object[] param=new Object[20];
        query.append("INSERT INTO expenses(appointment_id_fk,customer_id_fk,user_id_fk,expense_name,desription,amount_spent,status,mobile_temp_id,");
         if(time==null){
            query.append("expense_time,submitted_on,created_on,category_id_fk,exp_category_name) VALUES (?,?,?,?,?,?,0,?,?,now(),now(),?,?)");
            param = new Object[]{expense.getAppointmentId(), expense.getCustomerId(), expense.getUser().getId(), expense.getExpenseName(), expense.getDescription(), expense.getAmountSpent(),expense.getExpenseTempId(),expense.getExpenseTime() ,expense.geteCategoryName().getId(),expense.getExp_category_name()};
        }else{
            query.append("expense_time,submitted_on,created_on,category_id_fk,default_date,exp_category_name) VALUES (?,?,?,?,?,?,0,?,?,now(),now(),?,?,?)");
            param = new Object[]{expense.getAppointmentId(), expense.getCustomerId(), expense.getUser().getId(), expense.getExpenseName(), expense.getDescription(), expense.getAmountSpent(),expense.getExpenseTempId(),expense.getExpenseTime() ,expense.geteCategoryName().getId(),time,expense.getExp_category_name()};
         }
        log4jLog.info(" insert Expense :" + query);
        int expense_id=0;
        try {
            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query.toString(), param) > 0) {
                    String query1 = "SELECT MAX(id) FROM expenses";
                    try {
                        expense_id= FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query1, Integer.class);
                        String query2 ="INSERT INTO expenses_audit(expense_id_fk,appointment_id_fk,customer_id_fk,user_id_fk,expense_name,desription,amount_spent,status,expense_time,submitted_on,created_on,category_id_fk,exp_category_name) ";
                        query2 +=" VALUES (?,?,?,?,?,?,?,0,?,now(),now(),?,?)";
                        Object[] param2 = new Object[]{expense_id,expense.getAppointmentId(), expense.getCustomerId(), expense.getUser().getId(), expense.getExpenseName(), expense.getDescription(), expense.getAmountSpent(),expense.getExpenseTime() ,expense.geteCategoryName().getId(),expense.getExp_category_name()};
                        try{
                            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query2, param2) > 0) {
                                return expense_id;
                            }else{
                                  return 0;
                            }
                        }catch (Exception e) {
                        log4jLog.info(" insert Expense :" + e);
                   //   e.printStackTrace();
                        return 0;
                    }
                    } catch (Exception e) {
                        log4jLog.info(" insert Expense :" + e);
                      //  e.printStackTrace();
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            log4jLog.info(" insert Expense" + e);
//            e.printStackTrace();
            return 0;
        }
    }

    /**
     *
     * @param offlineData
     * @param accountId
     * @param userId
     * @return
     * @throws Exception
     */
    public int insertCustomers(LocalData offlineData, int accountId,int userId) throws Exception{
        int id=0;
        
      
         // mobileCustomerId=offlineData.getId();
        //  System.out.println(mobileCustomerId);
       String query = "INSERT INTO customers (customer_name, customer_printas,customer_location_identifier,is_headoffice,territory,industry,address1,phone1,phone2,phone3,fax1,fax2, email1,email2,email3,website1,website2,created_on,modified_on,created_by_id_fk,modified_by_id_fk,record_state,mobile_temp_id)"
               + "VALUES (?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?,?,?,now(),now(),?,?,1,?)";
      Object param[] = new Object[]{offlineData.getCustomerName(), offlineData.getCustomerPrintas(), offlineData.getCustomerLocation(), offlineData.isIsHeadOffice(), offlineData.getTerritory(), offlineData.getIndustry(), offlineData.getCustomerAddress1(), offlineData.getCustomerPhone1(), offlineData.getCustomerPhone2(), offlineData.getCustomerPhone3(), offlineData.getCustomerFax1(), offlineData.getCustomerFax2(), offlineData.getCustomerEmail1(), offlineData.getCustomerEmail2(), offlineData.getCustomerEmail3(), offlineData.getCustomerWebsiteUrl1(), offlineData.getCustomerWebsiteUrl2(),userId,userId,offlineData.getId()};
     
        
            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                 id= FieldSenseUtils.getMaxIdForTable("customers", accountId);
                 offlineData.setId(Integer.toString(id));
                 //System.out.println("whndiwhndi"+id);
                 } else {
                    return id;
                }
            }
            
       
        return id;
    }

    /**
     *
     * @param appointmentObj
     * @param accountId
     * @param userId
     * @param customerId
     * @return
     */
    public int insertAppointments(AppointmentOffline appointmentObj, int accountId, int userId,int customerId)  {
                   int appointmentId; 
                   int appointmentPositionId;
//                   System.out.println("wjdjwodjoj");
                  String query ="INSERT INTO appointments(appointment_title,customer_id_fk,customer_contact_id_fk,user_id_fk,assigned_id_fk,appointment_time,appointment_end_time,appointment_description,appointment_type,purpose_id_fk,outcome,status,check_in_lat,check_in_long,check_in_location,check_in_time,check_out_lat,check_out_long,check_out_location,check_out_time,next_appointment_id,created_on,created_by_id_fk,record_state,mobile_temp_id) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,1,?)";
                     String query1 = "SELECt MAX(appointment_possition) FROM appointments_possition WHERE user_id_fk=?";
                 Object param[] = new Object[]{appointmentObj.getTitle(),customerId, appointmentObj.getCustomerContact().getId(), appointmentObj.getOwner().getId(), appointmentObj.getAssignedTo().getId(), appointmentObj.getDateTime(), appointmentObj.getEndTime(), appointmentObj.getDescription(),
                appointmentObj.getType(), appointmentObj.getPurpose().getId(), appointmentObj.getOutcomes().getId(), appointmentObj.getStatus(), appointmentObj.getCheckInLat(), appointmentObj.getCheckInLong(), appointmentObj.getCheckInLocation(),
                appointmentObj.getCheckInTime(), appointmentObj.getCheckOutLat(), appointmentObj.getCheckOutLong(), appointmentObj.getCheckOutLocation(), appointmentObj.getCheckOutTime(), appointmentObj.getNextAppointment(), appointmentObj.getCreatedBy().getId(),appointmentObj.getId()};
                  Object param1[]=new Object[]{userId};
                   try {
                       synchronized (this) {
                           if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                               appointmentId= FieldSenseUtils.getMaxIdForTable("appointments", accountId);
                               appointmentPositionId=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query1, param1, Integer.class);
                           } else {
                               
                               return 0;
                           }
                           query = "INSERT INTO appointments_possition(appointment_id_fk,user_id_fk,appointment_possition,cretaed_on,modified_on) VALUES (?,?,?,now(),now())";
                           param= new Object[]{appointmentId, userId, appointmentPositionId+1};
                         
                           if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) <= 0) {
                              
                                return 0;
                                 } 
                       }
                       // appointmentMap.put(mobileAppointmentId, Integer.toString(appointmentId));
                   } catch (Exception e) {
                       //log4jLog.info(" insertUsersTravelLog " + e);
//                       e.printStackTrace();
                       return 0;
                       
                   }
                   
                   return appointmentId;
    }

    /**
     *
     * @param expenseObj
     * @param accountId
     * @param userId
     * @param customerId
     * @param appointmentId
     * @param mobileAppointmentId
     * @return
     */
    public int insertExpense(Expense expenseObj, int accountId, int userId, int customerId, int appointmentId,String mobileAppointmentId) {
        int expenseId=0;
       
                           expenseObj.setAppointmentId(appointmentId);
                           expenseObj.setCustomerId(customerId);
                           expenseId=this.insertExpense(expenseObj, accountId);
                           //Commented by siddhesh for removal of expense image from offline sync.
//                           if (expenseId != 0) {
//                           File oldfile = new File(Constant.IMAGE_UPLOAD_PATH + expenseObj.getExpenseImage().getImageURL());
//                           PhotoToIconCreator photoToIconCreator = new PhotoToIconCreator();
//                           try {
//                               photoToIconCreator.uploadExpenseImage(oldfile, "expense_" + accountId + "_" + userId + "_" + expenseId);
//                           } catch (IOException ex) {
//                               java.util.logging.Logger.getLogger(ExpenseManager.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                    String imageName = Constant.IMAGE_UPLOAD_PATH + "expense" + "_" + accountId + "_" + userId + "_" + expenseId + ".png";
//                    File newfile = new File(imageName);
//                    oldfile.renameTo(newfile);
//                           }
       //}
       return expenseId;
    }

    /**
     *
     * @param offlinedata
     * @param accoutId
     * @param userId
     * @param customerId
     * @return
     */
    public boolean updateCustomers(LocalData offlinedata, int accoutId, int userId, int customerId) {
        String query = "UPDATE customers SET address1=?,modified_on=now(),modified_by_id_fk=?,record_state=2 WHERE id=?";
        log4jLog.info(" updateCustomer " + query);
        Object param[] = new Object[]{offlinedata.getCustomerAddress1(),userId, customerId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accoutId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateCustomer " + e);
//            e.printStackTrace();
           return false;
        }
    }

    /**
     *
     * @param appointmentOffline
     * @param accountId
     * @param userId
     * @param customerId
     * @param appointmentId
     * @return
     */
    public boolean updateAppointment(AppointmentOffline appointmentOffline, int accountId, int userId, int customerId,int appointmentId) {
       String query = "UPDATE appointments SET appointment_title=?,customer_id_fk=?,customer_contact_id_fk=?,user_id_fk=?,assigned_id_fk=?,appointment_time=?,appointment_end_time=?,appointment_description=?,appointment_type=?,purpose_id_fk=?,outcome=?,status=?,check_in_lat=?,check_in_long=?,check_in_location=?,check_out_lat=?,check_out_long=?,check_out_location=?,next_appointment_id=?,updated_on=now(),updated_by_id_fk=?,record_state=2,outcome_description=? WHERE id=?";

        log4jLog.info(" updateAppointment " + query);
        Object param[] = new Object[]{appointmentOffline.getTitle(), customerId, appointmentOffline.getCustomerContact().getId(), appointmentOffline.getOwner().getId(), appointmentOffline.getAssignedTo().getId(), appointmentOffline.getDateTime(), appointmentOffline.getEndTime(), appointmentOffline.getDescription(),
            appointmentOffline.getType(), appointmentOffline.getPurpose().getId(), appointmentOffline.getOutcome(), appointmentOffline.getStatus(), appointmentOffline.getCheckInLat(), appointmentOffline.getCheckInLong(), appointmentOffline.getCheckInLocation(),
            appointmentOffline.getCheckOutLat(), appointmentOffline.getCheckOutLong(), appointmentOffline.getCheckOutLocation(), appointmentOffline.getNextAppointment(),userId,appointmentOffline.getOutcomeDescription(),appointmentId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateAppointment " + e);
            //e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param offlineData
     * @param accountId
     * @param userId
     * @param customerId
     * @return
     */
    public Timestamp checkCustomerLastUpdateTimeStamp(LocalData offlineData, int accountId, int userId, int customerId) {
        String query="select modified_on from customers where record_state !=3 AND id=?";
         Timestamp lastModifiedTime;
        Object param[]={customerId};
        try{
        lastModifiedTime=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Timestamp.class);
        }catch(Exception e){
            log4jLog.info(" checkCustomerLastUpdateTimeStamp " + e);
                long lastSyncMilliSeconds = Long.parseLong("0");   // date time in milliseconds                 
                Date lastSyncDate = new Date(lastSyncMilliSeconds);            //creating Date from millisecond
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");                 
                return  Timestamp.valueOf(df.format(lastSyncDate));                 
      
        }
        return lastModifiedTime;
    }

    /**
     *
     * @param accountId
     * @param customerId
     * @return
     */
    public int getRecordStatusForCustomer(int accountId, int customerId) {
        int status=3;
        String query="select record_state from customers where id=?";
        Object param[]={customerId};
         try{
        status=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
        }catch(Exception e){
            log4jLog.info(" getRecordStatusForCustomer " + e);
            return status;
        }
        return status;
    }

    /**
     *
     * @param tempId
     * @param accountId
     * @return
     */
    public int isTempIdPresent(String tempId, int accountId) {
       int customerId=0;
       String query="select id from customers where mobile_temp_id=?";
        Object param[]={tempId};
         try{
        customerId=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
        }catch(Exception e){
            log4jLog.info(" isTempIdPresent " + e);
//            e.printStackTrace();
            
         //   return customerId;
        }
        return customerId;
    }
    
    /**
     *
     * @param tempId
     * @param accountId
     * @return
     */
    public int isTempIdPresentForCustomer(String tempId, int accountId) {
       int customerId=0;
       String query="select id from customers where mobile_temp_id=?";
        Object param[]={tempId};
         try{
        customerId=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
        }catch(Exception e){
            log4jLog.info(" isTempIdPresentForCustomer " + e);
         //   e.printStackTrace();
            return customerId;
        }
        return customerId;
    }
     
    /**
     *
     * @param tempId
     * @param accountId
     * @return
     */
    public int isTempIdPresentForAppointment(String tempId, int accountId) {
       int customerId=0;
       String query="select id from appointments where mobile_temp_id=?";
        Object param[]={tempId};
         try{
        customerId=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
        }catch(Exception e){
            log4jLog.info(" isTempIdPresentForAppointment " + e);
           // e.printStackTrace();
            return customerId;
        }
        return customerId;
    }
      
    /**
     *
     * @param tempId
     * @param accountId
     * @return
     */
    public int isTempIdPresentForExpense(String tempId, int accountId) {
       int customerId=0;
       String query="select id from expenses where mobile_temp_id=?";
        Object param[]={tempId};
         try{
        customerId=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
        }catch(Exception e){
            log4jLog.info(" isTempIdPresentForExpense " + e);
         //  e.printStackTrace();
            return customerId;
        }
        return customerId;
    }

    /**
     *
     * @param appointmentOffline
     * @param accoutId
     * @param userId
     * @param customerId
     * @param appointmentId
     * @return
     */
    public boolean updateAppointmentForCheckInCheckOut(AppointmentOffline appointmentOffline, int accoutId, int userId, int customerId, int appointmentId) {
        String query = "UPDATE appointments SET appointment_title=?,customer_id_fk=?,customer_contact_id_fk=?,user_id_fk=?,assigned_id_fk=?,appointment_description=?,appointment_type=?,purpose_id_fk=?,outcome=?,status=?,check_in_lat=?,check_in_long=?,check_in_location=?,check_in_time=?,check_out_lat=?,check_out_long=?,check_out_location=?,check_out_time=?,next_appointment_id=?,updated_on=now(),updated_by_id_fk=?,record_state=2,outcome_description=? WHERE id=?";

        log4jLog.info(" updateAppointment " + query);
        Object param[] = new Object[]{appointmentOffline.getTitle(), customerId, appointmentOffline.getCustomerContact().getId(), appointmentOffline.getOwner().getId(), appointmentOffline.getAssignedTo().getId(), appointmentOffline.getDescription(),
            appointmentOffline.getType(), appointmentOffline.getPurpose().getId(), appointmentOffline.getOutcome(), appointmentOffline.getStatus(), appointmentOffline.getCheckInLat(), appointmentOffline.getCheckInLong(), appointmentOffline.getCheckInLocation(),
            appointmentOffline.getCheckInTime(), appointmentOffline.getCheckOutLat(), appointmentOffline.getCheckOutLong(), appointmentOffline.getCheckOutLocation(), appointmentOffline.getCheckOutTime(), appointmentOffline.getNextAppointment(),userId,appointmentOffline.getOutcomeDescription(),appointmentId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accoutId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateAppointmentForCheckInCheckOut " + e);
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param customerOfflineWithFewData
     * @param accountId
     * @param userId
     * @return
     */
    public List<Integer> terriortyList(CustomerOfflineWithFewData customerOfflineWithFewData, int accountId, int userId) {
       List<TerritoryList> listOfTerriorty=customerOfflineWithFewData.getTerritoryLists();
       List<Integer> deletedTerriorty=new ArrayList<Integer>();
       int deletedTerritoryId=0;
       String query="";
       Object param[]={} ;
        for(int i=0;i<listOfTerriorty.size();i++){
        try{
        TerritoryList territoryList=listOfTerriorty.get(i);
        deletedTerritoryId=territoryList.getTerritoryId();
         query="select teritory_id from user_territory where teritory_id=? and user_id_fk=?";
         param=new Object[]{deletedTerritoryId,userId};
         FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
         
        }catch(Exception e){
            log4jLog.info(" terriortyList " + e);
//        e.printStackTrace();
        deletedTerriorty.add(deletedTerritoryId);
        }
        } 
        return deletedTerriorty;
    }

    /**
     *
     * @param deletedTerriorty
     * @param accountId
     * @param userId
     * @return
     */
    public List<Integer> removedCustomerList(List<Integer> deletedTerriorty, int accountId, int userId) {
       try{
        StringBuilder query=new StringBuilder();
       query.append("select c.id from customers as c inner join territory_categories as tc on tc.category_name=c.territory where ");
       query.append("tc.id in(?");
       
       for(int i=1;i<deletedTerriorty.size();i++){
       query.append(",?");
       }
       
       query.append(")");
       for(int j=0;j<deletedTerriorty.size();j++){
       
       }
       Object param[]=new Object[deletedTerriorty.size()];
      for(int j=0;j<deletedTerriorty.size();j++){
            param[j]=deletedTerriorty.get(j);
        }
       return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForList(query.toString(), param,Integer.class);
    
       
       }catch(Exception e){
           log4jLog.info(" removedCustomerList " + e);
//       e.printStackTrace();
       return new ArrayList<Integer>();
       }
       }

    /**
     *
     * @param accountId
     * @param userId
     * @param lastSync
     * @param currentTime
     * @return
     */
    public List<CustomerOffline> selectCustomersOfflineAfterLastSyncForFirstSync(int accountId, int userId, Timestamp lastSync, Timestamp currentTime) {
          StringBuilder query = new StringBuilder();
            
        Object param[] = null;
         
            String query1=" SELECT c.id,c.record_state FROM customers c INNER JOIN territory_categories tc ON c.territory = tc.category_name INNER JOIN user_territory t1 ON t1.teritory_id=tc.id INNER JOIN fieldsense.users u ON u.id=t1.user_id_fk WHERE u.id= ? AND c.record_state!=3 "; // edited by siddhesh
            param = new Object[]{userId}; // edited by siddhesh
                  
        log4jLog.info(" selectCustomersOfflineAfterLastSync " + query1);
        try {
           
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query1,param,new RowMapper<CustomerOffline>() {
                @Override
                public CustomerOffline mapRow(ResultSet rs, int i) throws SQLException {
                    CustomerOffline customer = new CustomerOffline();                    
                    customer.setId(rs.getInt("c.id"));
                    customer.setRecord_state(rs.getInt("c.record_state"));
                    return customer;
                }
            });

             
        } catch (Exception e) {
            log4jLog.info(" selectCustomersOfflineAfterLastSyncForFirstSync " + e);
//            e.printStackTrace();
            return new ArrayList<CustomerOffline>();
        }
    }

    public List<CustomerOffline> selectCustomersBasedOnNewAddedTerriorty(int accountId, int userId, Timestamp lastSync, Timestamp currentTime) {
        Object param[] = null;
        String query="SELECT c.id,c.record_state FROM customers c INNER JOIN territory_categories tc ON c.territory = tc.category_name INNER JOIN user_territory t1 ON t1.teritory_id=tc.id INNER JOIN fieldsense.users u ON u.id=t1.user_id_fk WHERE u.id= ? and t1.created_on > ?";
        param = new Object[]{userId,lastSync};
        try {
           
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query,param,new RowMapper<CustomerOffline>() {
                @Override
                public CustomerOffline mapRow(ResultSet rs, int i) throws SQLException {
                    CustomerOffline customer = new CustomerOffline();                    
                    customer.setId(rs.getInt("c.id"));
                    customer.setRecord_state(1);
                    return customer;
                }
            });

             
        } catch (Exception e) {
            log4jLog.info(" selectCustomersOfflineAfterLastSyncForFirstSync " + e);
            e.printStackTrace();
            return new ArrayList<CustomerOffline>();
        }
    }
     
    /**
     * @added by jyoti, #31015
     * @param accountId
     * @param customerTmpId
     * @param customerId
     * @return 
     */
    @Override
    public int updateUserTravelLogCustomerIdWithTmp(int accountId, String customerTmpId, int customerId) {
        String query = "UPDATE user_travel_logs set customer_id_fk = ? WHERE customer_id_fk = ?";
        Object param[] = new Object[]{customerId, customerTmpId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param);
        } catch (DataAccessException e) {
            log4jLog.info(" updateUserTravelLogCustomerIdWithTmp " + e);
            e.printStackTrace();
            return 0;
        }
    }
    
}
       