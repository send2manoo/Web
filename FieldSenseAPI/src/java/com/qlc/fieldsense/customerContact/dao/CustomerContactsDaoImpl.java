/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customerContact.dao;

import com.qlc.fieldsense.customer.model.Customer;
import com.qlc.fieldsense.customerContact.model.CustomerContacts;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author anuja
 */
public class CustomerContactsDaoImpl implements CustomerContactsDao {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("CustomerContactsDaoImpl");

    /**
     *
     * @param customerCont
     * @param communityId
     * @return
     */
    @Override
    public int insertCustomerContacts(CustomerContacts customerCont, int communityId) {
        String query = "INSERT INTO customer_contacts(customer_id_fk,first_name,middle_name,last_name,phone_number1,phone_number2,phone_number3,fax1,fax2,mobile1,mobile2,email1,email2,report_to,assistant_name,spouse_name,birth_date,anniversary_date,designation,created_on) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now())";
        log4jLog.info("insertCustomerContacts " + query);
        Object param[] = new Object[]{customerCont.getCustomerId(), customerCont.getFirstName(), customerCont.getMiddleName(), customerCont.getLastName(), customerCont.getPhone1(), customerCont.getPhone2(), customerCont.getPhone3(), customerCont.getFax1(), customerCont.getFax2(), customerCont.getMobile1(), customerCont.getMobile2(), customerCont.getEmail1(), customerCont.getEmail2(), customerCont.getReportTo(), customerCont.getAssistantName(), customerCont.getSpouseName(), customerCont.getBirthDate(), customerCont.getAnniversaryDate(), customerCont.getDesignation()};
        try {
            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(communityId).update(query, param) > 0) {
                    String query1 = "SELECT MAX(id) FROM customer_contacts";
                    return FieldSenseUtils.getJdbcTemplateForAccount(communityId).queryForObject(query1, Integer.class);
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            log4jLog.info("insertCustomerContacts" + e);
//            e.printStackTrace();
            return 0;
        }
    }

    /**
     *
     * @param communityId
     * @return
     */
    @Override
    public CustomerContacts selectCustomerContact(int communityId) {
        synchronized (this) {
            String query = "SELECT id,customer_id_fk,first_name,middle_name,last_name,phone_number1,phone_number2,phone_number3,fax1,fax2,mobile1,mobile2,email1,email2,report_to,assistant_name,spouse_name,birth_date,anniversary_date,designation,created_on FROM customer_contacts WHERE id=(SELECT MAX(id) FROM customer_contacts)";
            log4jLog.info("selectCustomerContact " + query);
            try {
                return FieldSenseUtils.getJdbcTemplateForAccount(communityId).queryForObject(query, new RowMapper<CustomerContacts>() {
                    @Override
                    public CustomerContacts mapRow(ResultSet rs, int i) throws SQLException {
                        CustomerContacts customercontacts = new CustomerContacts();
                        customercontacts.setId(rs.getInt("id"));
                        customercontacts.setCustomerId(rs.getInt("customer_id_fk"));
                        customercontacts.setFirstName(rs.getString("first_name"));
                        customercontacts.setMiddleName(rs.getString("middle_name"));
                        customercontacts.setLastName(rs.getString("last_name"));
                        customercontacts.setPhone1(rs.getString("phone_number1"));
                        customercontacts.setPhone2(rs.getString("phone_number2"));
                        customercontacts.setPhone3(rs.getString("phone_number3"));
                        customercontacts.setFax1(rs.getString("fax1"));
                        customercontacts.setFax2(rs.getString("fax2"));
                        customercontacts.setMobile1(rs.getString("mobile1"));
                        customercontacts.setMobile2(rs.getString("mobile2"));
                        customercontacts.setEmail1(rs.getString("email1"));
                        customercontacts.setEmail2(rs.getString("email2"));
                        customercontacts.setReportTo(rs.getString("report_to"));
                        customercontacts.setAssistantName(rs.getString("assistant_name"));
                        customercontacts.setSpouseName(rs.getString("spouse_name"));
                        customercontacts.setBirthDate(new Date(rs.getDate("birth_date").getTime()));
                        customercontacts.setAnniversaryDate(new Date(rs.getDate("anniversary_date").getTime()));
                        customercontacts.setDesignation(rs.getString("designation"));
                        customercontacts.setCreatedOn(rs.getTimestamp("created_on"));
                        return customercontacts;
                    }
                });
            } catch (Exception e) {
                log4jLog.info("selectCustomerContact" + e);
//                e.printStackTrace();
                return new CustomerContacts();
            }
        }
    }

    /**
     *
     * @param communityId
     * @return
     */
    @Override
    public List<CustomerContacts> selectCustomerContacts(int communityId) {
        String query = "SELECT id,customer_id_fk,first_name,middle_name,last_name,phone_number1,phone_number2,phone_number3,fax1,fax2,mobile1,mobile2,email1,email2,report_to,assistant_name,spouse_name,birth_date,anniversary_date,designation,created_on FROM customer_contacts";
        log4jLog.info("selectCustomerContacts " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(communityId).query(query, new RowMapper<CustomerContacts>() {
                public CustomerContacts mapRow(ResultSet rs, int i) throws SQLException {
                    CustomerContacts customercontacts = new CustomerContacts();
                    customercontacts.setId(rs.getInt("id"));
                    customercontacts.setCustomerId(rs.getInt("customer_id_fk"));
                    customercontacts.setFirstName(rs.getString("first_name"));
                    customercontacts.setMiddleName(rs.getString("middle_name"));
                    customercontacts.setLastName(rs.getString("last_name"));
                    customercontacts.setPhone1(rs.getString("phone_number1"));
                    customercontacts.setPhone2(rs.getString("phone_number2"));
                    customercontacts.setPhone3(rs.getString("phone_number3"));
                    customercontacts.setFax1(rs.getString("fax1"));
                    customercontacts.setFax2(rs.getString("fax2"));
                    customercontacts.setMobile1(rs.getString("mobile1"));
                    customercontacts.setMobile2(rs.getString("mobile2"));
                    customercontacts.setEmail1(rs.getString("email1"));
                    customercontacts.setEmail2(rs.getString("email2"));
                    customercontacts.setReportTo(rs.getString("report_to"));
                    customercontacts.setAssistantName(rs.getString("assistant_name"));
                    customercontacts.setSpouseName(rs.getString("spouse_name"));
                    customercontacts.setBirthDate(new Date(rs.getDate("birth_date").getTime()));
                    customercontacts.setAnniversaryDate(new Date(rs.getDate("anniversary_date").getTime()));
                    customercontacts.setDesignation(rs.getString("designation"));
                    customercontacts.setCreatedOn(rs.getTimestamp("created_on"));
                    return customercontacts;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectCustomerContacts" + e);
//            e.printStackTrace();
            return new ArrayList<CustomerContacts>();
        }
    }

    /**
     *
     * @param id
     * @param communityId
     * @return
     */
    @Override
    public CustomerContacts selectCustomerContact(int id, int communityId) {
        String query = "SELECT id,customer_id_fk,first_name,middle_name,last_name,phone_number1,phone_number2,phone_number3,fax1,fax2,mobile1,mobile2,email1,email2,report_to,assistant_name,spouse_name,birth_date,anniversary_date,designation,created_on FROM customer_contacts WHERE id=?";
        log4jLog.info("selectCustomerContact " + query);
        Object param[] = new Object[]{id};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(communityId).queryForObject(query, param, new RowMapper<CustomerContacts>() {
                @Override
                public CustomerContacts mapRow(ResultSet rs, int i) throws SQLException {
                    CustomerContacts customercontacts = new CustomerContacts();
                    customercontacts.setId(rs.getInt("id"));
                    customercontacts.setCustomerId(rs.getInt("customer_id_fk"));
                    customercontacts.setFirstName(rs.getString("first_name"));
                    customercontacts.setMiddleName(rs.getString("middle_name"));
                    customercontacts.setLastName(rs.getString("last_name"));
                    customercontacts.setPhone1(rs.getString("phone_number1"));
                    customercontacts.setPhone2(rs.getString("phone_number2"));
                    customercontacts.setPhone3(rs.getString("phone_number3"));
                    customercontacts.setFax1(rs.getString("fax1"));
                    customercontacts.setFax2(rs.getString("fax2"));
                    customercontacts.setMobile1(rs.getString("mobile1"));
                    customercontacts.setMobile2(rs.getString("mobile2"));
                    customercontacts.setEmail1(rs.getString("email1"));
                    customercontacts.setEmail2(rs.getString("email2"));
                    customercontacts.setReportTo(rs.getString("report_to"));
                    customercontacts.setAssistantName(rs.getString("assistant_name"));
                    customercontacts.setSpouseName(rs.getString("spouse_name"));
                    customercontacts.setBirthDate(new Date(rs.getDate("birth_date").getTime()));
                    customercontacts.setAnniversaryDate(new Date(rs.getDate("anniversary_date").getTime()));
                    customercontacts.setDesignation(rs.getString("designation"));
                    customercontacts.setCreatedOn(rs.getTimestamp("created_on"));
                    return customercontacts;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectCustomerContact" + e);
            return new CustomerContacts();
        }
    }

    /**
     *
     * @param customerId
     * @param communityId
     * @return
     */
    @Override
    public List<CustomerContacts> selectCustomerContactOnCustomerId(int customerId, int communityId) {
        String query = "SELECT id,customer_id_fk,first_name,middle_name,last_name,phone_number1,phone_number2,phone_number3,fax1,fax2,mobile1,mobile2,email1,email2,report_to,assistant_name,spouse_name,birth_date,anniversary_date,designation,created_on FROM customer_contacts WHERE customer_id_fk=?";
        Object param[] = new Object[]{customerId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(communityId).query(query, param, new RowMapper<CustomerContacts>() {
                @Override
                public CustomerContacts mapRow(ResultSet rs, int i) throws SQLException {
                    CustomerContacts customercontacts = new CustomerContacts();
                    customercontacts.setId(rs.getInt("id"));
                    customercontacts.setCustomerId(rs.getInt("customer_id_fk"));
                    customercontacts.setFirstName(rs.getString("first_name"));
                    customercontacts.setMiddleName(rs.getString("middle_name"));
                    customercontacts.setLastName(rs.getString("last_name"));
                    customercontacts.setPhone1(rs.getString("phone_number1"));
                    customercontacts.setPhone2(rs.getString("phone_number2"));
                    customercontacts.setPhone3(rs.getString("phone_number3"));
                    customercontacts.setFax1(rs.getString("fax1"));
                    customercontacts.setFax2(rs.getString("fax2"));
                    customercontacts.setMobile1(rs.getString("mobile1"));
                    customercontacts.setMobile2(rs.getString("mobile2"));
                    customercontacts.setEmail1(rs.getString("email1"));
                    customercontacts.setEmail2(rs.getString("email2"));
                    customercontacts.setReportTo(rs.getString("report_to"));
                    customercontacts.setAssistantName(rs.getString("assistant_name"));
                    customercontacts.setSpouseName(rs.getString("spouse_name"));
                    customercontacts.setBirthDate(new Date(rs.getDate("birth_date").getTime()));
                    customercontacts.setAnniversaryDate(new Date(rs.getDate("anniversary_date").getTime()));
                    customercontacts.setDesignation(rs.getString("designation"));
                    customercontacts.setCreatedOn(rs.getTimestamp("created_on"));
                    return customercontacts;
                }
            });
        } catch (Exception e) {
//            e.printStackTrace();
            return new ArrayList<CustomerContacts>();
        }
    }

    /**
     *
     * @param id
     * @param communityId
     * @return
     */
    @Override
    public CustomerContacts deleteCustomerContacts(int id, int communityId) {
        CustomerContacts customerCont = new CustomerContacts();
        customerCont = selectCustomerContact(id, communityId);
        String query = "DELETE FROM customer_contacts WHERE id=?";
        Object param[] = new Object[]{id};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(communityId).update(query, param) > 0) {
                return customerCont;
            } else {
                return new CustomerContacts();
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return new CustomerContacts();
        }
    }

    /**
     *
     * @param customerId
     * @param communityId
     * @return
     */
    @Override
    public List<CustomerContacts> deleteCustomerContactOnCustomerId(int customerId, int communityId) {
        List<CustomerContacts> customerCont = selectCustomerContactOnCustomerId(customerId, communityId);
        String query = "DELETE FROM customer_contacts WHERE customer_id_fk=?";
        Object param[] = new Object[]{customerId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(communityId).update(query, param) > 0) {
                return customerCont;
            } else {
                return new ArrayList<CustomerContacts>();
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return new ArrayList<CustomerContacts>();
        }
    }

    /**
     *
     * @param customerCont
     * @param communityId
     * @return
     */
    @Override
    public CustomerContacts updateCustomerContacts(CustomerContacts customerCont, int communityId) {
        String query = "UPDATE customer_contacts SET first_name=?,middle_name=?,last_name=?,phone_number1=?,phone_number2=?,phone_number3=?,fax1=?,fax2=?,mobile1=?,mobile2=?,email1=?,email2=?,report_to=?,assistant_name=?,spouse_name=?,birth_date=?,anniversary_date=?,designation=? WHERE id=?";
        Object param[] = new Object[]{customerCont.getFirstName(), customerCont.getMiddleName(), customerCont.getLastName(), customerCont.getPhone1(), customerCont.getPhone2(), customerCont.getPhone3(), customerCont.getFax1(), customerCont.getFax2(), customerCont.getMobile1(), customerCont.getMobile2(), customerCont.getEmail1(), customerCont.getEmail2(), customerCont.getReportTo(), customerCont.getAssistantName(), customerCont.getSpouseName(), customerCont.getBirthDate(), customerCont.getAnniversaryDate(), customerCont.getDesignation(), customerCont.getId()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(communityId).update(query, param) > 0) {
                return customerCont;
            } else {
                return new CustomerContacts();
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return new CustomerContacts();
        }
    }

    /**
     *
     * @param customerCont
     * @param communityId
     * @return
     */
    @Override
    public CustomerContacts updateCustomerContactDetails(CustomerContacts customerCont, int communityId) {
        String query = "UPDATE customer_contacts SET first_name=?,middle_name=?,last_name=?,phone_number1=?,phone_number2=?,phone_number3=?,mobile1=?,mobile2=? WHERE id=?";
        Object param[] = new Object[]{customerCont.getFirstName(), customerCont.getMiddleName(), customerCont.getLastName(), customerCont.getPhone1(), customerCont.getPhone2(), customerCont.getPhone3(), customerCont.getMobile1(), customerCont.getMobile2(), customerCont.getId()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(communityId).update(query, param) == 1) {
                return customerCont;
            } else {
                return new CustomerContacts();
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return new CustomerContacts();
        }
    }

    /*  
     @Override
     public CustomerContacts updateCustomerContactsOnCustomerId(CustomerContacts customerCont, int communityId) {
     Connection con = null;
     try {
     con = FieldSenseUtils.getJdbcTemplateForAccount(communityId).getDataSource().getConnection();
     } catch (SQLException ex) {
     log4jLog.info("SQLException", ex);
     }
     String query = "UPDATE customer_contacts SET first_name=?,middle_name=?,last_name=?,phone_number1=?,phone_number2=?,phone_number3=?,fax1=?,fax2=?,mobile1=?,mobile2=?,email1=?,email2=?,report_to=?,assistant_name=?,spouse_name=?,birth_date=?,anniversary_date=?,designation=? WHERE customer_id_fk=?";
     String query1 = "UPDATE customers SET address1=?, address2=?,address3=?, city=?,state=?, country=?,zipcode=? WHERE id=?";
     try {
     con.setAutoCommit(false);
     PreparedStatement stmt = con.prepareStatement(query1);
     stmt.setString(1, customerCont.getCustomerId().getCustomerAddress1());
     stmt.setString(2, customerCont.getCustomerId().getCustomerAddress2());
     stmt.setString(3, customerCont.getCustomerId().getCustomerAddress3());
     stmt.setString(4, customerCont.getCustomerId().getCustomerCity());
     stmt.setString(5, customerCont.getCustomerId().getCustomerState());
     stmt.setString(6, customerCont.getCustomerId().getCustomerCountry());
     stmt.setString(7, customerCont.getCustomerId().getCustomerZipcode());
     stmt.setInt(8, customerCont.getCustomerId().getId());
     stmt.execute();

     PreparedStatement stmt1 = con.prepareStatement(query);
     stmt1.setString(1, customerCont.getFirstName());
     stmt1.setString(2, customerCont.getMiddleName());
     stmt1.setString(3, customerCont.getLastName());
     stmt1.setString(4, customerCont.getPhone1());
     stmt1.setString(5, customerCont.getPhone2());
     stmt1.setString(6, customerCont.getPhone3());
     stmt1.setString(7, customerCont.getFax1());
     stmt1.setString(8, customerCont.getFax2());
     stmt1.setString(9, customerCont.getMobile1());
     stmt1.setString(10, customerCont.getMobile2());
     stmt1.setString(11, customerCont.getEmail1());
     stmt1.setString(12, customerCont.getEmail2());
     stmt1.setString(13, customerCont.getReportTo());
     stmt1.setString(14, customerCont.getAssistantName());
     stmt1.setString(15, customerCont.getSpouseName());
     stmt1.setDate(16, new java.sql.Date(customerCont.getBirthDate().getTime()));
     stmt1.setDate(17, new java.sql.Date(customerCont.getAnniversaryDate().getTime()));
     stmt1.setString(18, customerCont.getDesignation());
     stmt1.setInt(19, customerCont.getCustomerId().getId());
     stmt1.execute();
     con.commit();
     return customerCont;
     } catch (Exception e) {
     try {
     con.rollback();
     } catch (SQLException ex) {
     log4jLog.info("SQLException", ex);
     }
     log4jLog.info("Exception", e);
     return new CustomerContacts();
     } finally {
     try {
     con.close();
     } catch (SQLException ex) {
     log4jLog.info("SQLException", ex);
     }
     }
     }*/

    /**
     *
     * @param id
     * @param communityId
     * @return
     */
    
    @Override
    public boolean isCustomerContactValid(int id, int communityId) {
        String query = "SELECT COUNT(id) FROM customer_contacts WHERE id=?";
        Object[] param = new Object[]{id};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(communityId).queryForObject(query, param, Integer.class) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param customerId
     * @param communityId
     * @return
     */
    @Override
    public boolean isContactCustomerIdValid(int customerId, int communityId) {
//        String query = "SELECT COUNT(id) FROM customer_contacts WHERE customer_id_fk=?";
        String query = "SELECT COUNT(id) FROM customers WHERE record_state !=3 AND id=?";
        Object[] param = new Object[]{customerId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(communityId).queryForObject(query, param, Integer.class) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param id
     * @param communityId
     * @return
     */
    @Override
    public CustomerContacts selectCustomerContactForImport(int id, int communityId) {
        String query = "SELECT id,customer_id_fk,first_name,middle_name,last_name,phone_number1,phone_number2,phone_number3,fax1,fax2,mobile1,mobile2,email1,email2,report_to,assistant_name,spouse_name,birth_date,anniversary_date,designation,created_on FROM customer_contacts WHERE id=?";
        log4jLog.info("selectCustomerContact " + query);
        Object param[] = new Object[]{id};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(communityId).queryForObject(query, param, new RowMapper<CustomerContacts>() {
                public CustomerContacts mapRow(ResultSet rs, int i) throws SQLException {
                    CustomerContacts customercontacts = new CustomerContacts();
                    customercontacts.setId(rs.getInt("id"));
                    customercontacts.setCustomerId(rs.getInt("customer_id_fk"));
                    customercontacts.setFirstName(rs.getString("first_name"));
                    customercontacts.setMiddleName(rs.getString("middle_name"));
                    customercontacts.setLastName(rs.getString("last_name"));
                    customercontacts.setPhone1(rs.getString("phone_number1"));
                    customercontacts.setPhone2(rs.getString("phone_number2"));
                    customercontacts.setPhone3(rs.getString("phone_number3"));
                    customercontacts.setFax1(rs.getString("fax1"));
                    customercontacts.setFax2(rs.getString("fax2"));
                    customercontacts.setMobile1(rs.getString("mobile1"));
                    customercontacts.setMobile2(rs.getString("mobile2"));
                    customercontacts.setEmail1(rs.getString("email1"));
                    customercontacts.setEmail2(rs.getString("email2"));
                    customercontacts.setReportTo(rs.getString("report_to"));
                    customercontacts.setAssistantName(rs.getString("assistant_name"));
                    customercontacts.setSpouseName(rs.getString("spouse_name"));
//                    customercontacts.setBirthDate(new Date(rs.getDate("birth_date").getTime()));
//                    customercontacts.setAnniversaryDate(new Date(rs.getDate("anniversary_date").getTime()));
                    customercontacts.setsBirthDate(rs.getDate("birth_date").toString());
                    customercontacts.setsAnniversaryDate(rs.getDate("anniversary_date").toString());
                    customercontacts.setDesignation(rs.getString("designation"));
                    customercontacts.setCreatedOn(rs.getTimestamp("created_on"));
                    return customercontacts;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectCustomerContact" + e);
//            e.printStackTrace();
            return new CustomerContacts();
        }
    }

    /**
     *
     * @param firstNm
     * @param middleNm
     * @param lastNm
     * @param customerId
     * @param accountId
     * @return
     */
    @Override
    public boolean isCustomerContactExist(String firstNm, String middleNm, String lastNm, int customerId, int accountId) {
        String query = "SELECT COUNT(id) FROM customer_contacts WHERE first_name=? AND middle_name=? AND last_name=? AND customer_id_fk=?";
        log4jLog.info(" isCustomerContactExist " + query);
        Object[] param = new Object[]{firstNm, middleNm, lastNm, customerId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isCustomerContactExist " + e);
            return false;
        }
    }
}
