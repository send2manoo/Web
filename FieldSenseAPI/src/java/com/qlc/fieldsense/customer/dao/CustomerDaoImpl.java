/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customer.dao;

import com.qlc.fieldsense.customer.model.Customer;
import com.qlc.fieldsense.customer.service.CustomerService;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Ramesh
 * @date 28-01-2014 .
 * @purpouse To manipulate customer related database operations .
 */
public class CustomerDaoImpl implements customerDao {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("CustomerDaoImpl");

    /**
     *
     * @param customer
     * @param accountId
     * @return
     */
    @Override
    public int insertCustomer(Customer customer, int accountId) {  // modified by manohar for dashboard
        String query = "INSERT INTO customers (customer_name, customer_printas,customer_location_identifier,is_headoffice,territory,industry,address1,phone1,phone2,phone3,fax1,fax2, email1,email2,email3,website1,website2,created_on,modified_on,created_by_id_fk,record_state) VALUES (?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?,?,?,now(),now(),?,1)";
        log4jLog.info(" insertCustomer " + query);
        int createdbyId=0;
//        System.out.println("customer.getCreated_by().getId()="+customer.getCreated_by().getId());
        if(customer.getCreated_by().getId()==0)
        {
            createdbyId=customer.getCreated_by_id_fk();
        }
        else
        {
            createdbyId=customer.getCreated_by().getId();
        }
        Object param[] = new Object[]{customer.getCustomerName(), customer.getCustomerPrintas(), customer.getCustomerLocation(), customer.isIsHeadOffice(), customer.getTerritory(), customer.getIndustry(), customer.getCustomerAddress1(), customer.getCustomerPhone1(), customer.getCustomerPhone2(), customer.getCustomerPhone3(), customer.getCustomerFax1(), customer.getCustomerFax2(), customer.getCustomerEmail1(), customer.getCustomerEmail2(), customer.getCustomerEmail3(), customer.getCustomerWebsiteUrl1(), customer.getCustomerWebsiteUrl2(),createdbyId};
        try {
            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    return FieldSenseUtils.getMaxIdForTable("customers", accountId);
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            log4jLog.info(" insertCustomer " + e);
//            e.printStackTrace();
            return 0;
        }
    }

    /**
     *
     * @param customerId
     * @param accountId
     * @return
     */
    @Override
    public Customer selectCustomer(int customerId, int accountId) {
//        String query1= "";
        StringBuilder query = new StringBuilder();
        query.append("SELECT c.id, c.record_state, customer_name,customer_printas,customer_location_identifier,is_headoffice,territory,industry,address1,phone1,phone2,phone3,");
        query.append("fax1,fax2,email1,email2,email3,website1,website2,c.created_on,IFNULL(l.latitude,0) latitude,IFNULL(l.longitude,0) longitude FROM customers as c");
        query.append(" LEFT OUTER JOIN location as l ON l.location_type_id_fk=c.id WHERE c.id=? AND c.record_state !=3 ");
        log4jLog.info(" selectCustomer " + query);
        Object param[] = new Object[]{customerId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query.toString(), param, new RowMapper<Customer>() {

                @Override
                public Customer mapRow(ResultSet rs, int i) throws SQLException {
                    Customer customer = new Customer();
                    customer.setId(rs.getInt("id"));
                    customer.setCustomerName(rs.getString("customer_name"));
                    customer.setCustomerPrintas(rs.getString("customer_printas"));
//                    customer.setCustomerShortName(rs.getString("customer_shortName"));
                    customer.setCustomerLocation(rs.getString("customer_location_identifier"));
                    customer.setIsHeadOffice(rs.getBoolean("is_headoffice"));
                    customer.setTerritory(rs.getString("territory"));
                    customer.setIndustry(rs.getString("industry"));
                    customer.setCustomerAddress1(rs.getString("address1"));
                    customer.setCustomerPhone1(rs.getString("phone1"));
                    customer.setCustomerPhone2(rs.getString("phone2"));
                    customer.setCustomerPhone3(rs.getString("phone3"));
                    customer.setCustomerFax1(rs.getString("fax1"));
                    customer.setCustomerFax2(rs.getString("fax2"));
                    customer.setCustomerEmail1(rs.getString("email1"));
                    customer.setCustomerEmail2(rs.getString("email2"));
                    customer.setCustomerEmail3(rs.getString("email3"));
                    customer.setCustomerWebsiteUrl1(rs.getString("website1"));
                    customer.setCustomerWebsiteUrl2(rs.getString("website2"));
                    customer.setCreatedOn(rs.getTimestamp("created_on"));
                    customer.setLasknownLatitude(rs.getDouble("latitude"));
                    customer.setLasknownLangitude(rs.getDouble("longitude"));
                    customer.setRecordState(rs.getInt("c.record_state")); // Added by jyoti, jan 30 2018
                    return customer;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectCustomer " + e);
            e.printStackTrace();
            return new Customer();
        }
    }

    /**
     *
     * @param userId
     * @param customerId
     * @param accountId
     * @return
     */
    @Override
    public Customer selectCustomerWithLocatio(int userId, int customerId, int accountId) {
//        String query = "SELECT id,customer_name,customer_printas,customer_location_identifier,is_headoffice,territory,industry,address1,address2,address3,city,state,country,zipcode,phone1,phone2,phone3,fax1,fax2,email1,email2,email3,website1,website2,created_on FROM customers WHERE id=?";
        StringBuilder query = new StringBuilder();
        query.append("SELECT customers.id, customers.record_state, customers.customer_name,customers.customer_printas,customers.is_headoffice,customers.customer_location_identifier,customers.territory,customers.industry,customers.address1,");
        query.append(" customers.phone1,customers.phone2,customers.phone3,");
        query.append(" customers.fax1,customers.fax2,customers.email1,customers.email2,customers.email3,customers.website1,customers.website2,customers.created_on, location.latitude, location.longitude");
        query.append(" FROM customers INNER JOIN location ON customers.id= location.location_type_id_fk WHERE customers.record_state !=3 AND location.user_id_fk=? AND location_type_id_fk=?");
        log4jLog.info(" selectCustomer " + query);
        Object param[] = new Object[]{userId, customerId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query.toString(), param, new RowMapper<Customer>() {

                @Override
                public Customer mapRow(ResultSet rs, int i) throws SQLException {
                    Customer customer = new Customer();
                    customer.setId(rs.getInt("id"));
                    customer.setCustomerName(rs.getString("customer_name"));
                    customer.setCustomerPrintas(rs.getString("customer_printas"));
//                    customer.setCustomerShortName(rs.getString("customer_shortName"));
                    customer.setCustomerLocation(rs.getString("customer_location_identifier"));
                    customer.setIsHeadOffice(rs.getBoolean("is_headoffice"));
                    customer.setTerritory(rs.getString("territory"));
                    customer.setIndustry(rs.getString("industry"));
                    customer.setCustomerAddress1(rs.getString("address1"));
                    customer.setCustomerPhone1(rs.getString("phone1"));
                    customer.setCustomerPhone2(rs.getString("phone2"));
                    customer.setCustomerPhone3(rs.getString("phone3"));
                    customer.setCustomerFax1(rs.getString("fax1"));
                    customer.setCustomerFax2(rs.getString("fax2"));
                    customer.setCustomerEmail1(rs.getString("email1"));
                    customer.setCustomerEmail2(rs.getString("email2"));
                    customer.setCustomerEmail3(rs.getString("email3"));
                    customer.setCustomerWebsiteUrl1(rs.getString("website1"));
                    customer.setCustomerWebsiteUrl2(rs.getString("website2"));
                    customer.setCreatedOn(rs.getTimestamp("created_on"));
                    customer.setLasknownLatitude(rs.getDouble("latitude"));
                    customer.setLasknownLangitude(rs.getDouble("longitude"));
                    customer.setRecordState(rs.getInt("customers.record_state")); // Added by jyoti, jan 30 2018
                    return customer;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectCustomer " + e);
            e.printStackTrace();
            return new Customer();
        }

    }

    /**
     *
     * @param accountId
     * @return
     */
    @Override
    public List<Customer> selectCustomers(int accountId) {
        String query = "SELECT id,customer_name,customer_printas,customer_location_identifier,is_headoffice,territory,industry,address1,phone1,phone2,phone3,fax1,fax2,email1,email2,email3,website1,website2,created_on FROM customers WHERE record_state !=3 ORDER BY customer_name ASC";
        log4jLog.info(" selectCustomers " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, new RowMapper<Customer>() {

                @Override
                public Customer mapRow(ResultSet rs, int i) throws SQLException {
                    Customer customer = new Customer();
                    customer.setId(rs.getInt("id"));
                    customer.setCustomerName(rs.getString("customer_name"));
                    customer.setCustomerPrintas(rs.getString("customer_printas"));
                    customer.setCustomerLocation(rs.getString("customer_location_identifier"));
                    customer.setIsHeadOffice(rs.getBoolean("is_headoffice"));
                    customer.setTerritory(rs.getString("territory"));
                    customer.setIndustry(rs.getString("industry"));
                    customer.setCustomerAddress1(rs.getString("address1"));
                    customer.setCustomerPhone1(rs.getString("phone1"));
                    customer.setCustomerPhone2(rs.getString("phone2"));
                    customer.setCustomerPhone3(rs.getString("phone3"));
                    customer.setCustomerFax1(rs.getString("fax1"));
                    customer.setCustomerFax2(rs.getString("fax2"));
                    customer.setCustomerEmail1(rs.getString("email1"));
                    customer.setCustomerEmail2(rs.getString("email2"));
                    customer.setCustomerEmail3(rs.getString("email3"));
                    customer.setCustomerWebsiteUrl1(rs.getString("website1"));
                    customer.setCustomerWebsiteUrl2(rs.getString("website2"));
                    customer.setCreatedOn(rs.getTimestamp("created_on"));
                    return customer;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectCustomer " + e);
//            e.printStackTrace();
            return new ArrayList<Customer>();
        }
    }
    
    /**
     * 
     * @param accountId
     * @param userId
     * @return 
     * @purpose  By jyoti- MObile side sending customer list with only names
     */
    @Override
    public List<String> selectCustomerNames(int accountId, int userId){
        String query = "SELECT customer_name FROM customers c INNER JOIN territory_categories tc ON c.territory = tc.category_name INNER JOIN user_territory t1 ON t1.teritory_id=tc.id INNER JOIN fieldsense.users u ON u.id=t1.user_id_fk WHERE u.id= ? ORDER BY customer_name ASC";
        Object param[] = new Object[]{userId};
        log4jLog.info(" selectCustomerNames " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForList(query, param, String.class);
        } 
        catch (Exception e) {
            log4jLog.info(" selectCustomerNames " + e.getMessage());
//            e.printStackTrace();
            return new ArrayList<String>();
        }
    }    
    
    /**
     *
     * @param accountId
     * @param searchText
     * @return
     */
    @Override
    public List<Map> selectCustomersInVisit(int accountId,String searchText) {
        String query = "SELECT id,customer_name,customer_location_identifier FROM customers WHERE record_state !=3 AND customer_name LIKE ? OR  customer_location_identifier LIKE ? ORDER BY customer_name ASC";
        log4jLog.info(" selectCustomers " + query);
        Object param[] = new Object[]{"%"+searchText+"%", "%"+searchText+"%"};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query,param, new RowMapper<Map>() {

                @Override
                public Map mapRow(ResultSet rs, int i) throws SQLException {
                    Map customer = new HashMap();
                    customer.put("customerId",rs.getInt("id"));
                    customer.put("value",rs.getString("customer_name")+"-"+rs.getString("customer_location_identifier"));
                    return customer;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectCustomer " + e);
//            e.printStackTrace();
            return new ArrayList<Map>();
        }
    }

    /**
     * @added by jyoti
     * @param userId
     * @param accountId
     * @param searchText
     * @return
     */
    @Override
    public List<Map> selectCustomersOnlyWhoHaveVisit(int userId, int accountId, String searchText) {
        StringBuilder query = new StringBuilder();
        query.append(" SELECT DISTINCT c.id, c.customer_name, c.customer_location_identifier FROM customers c ");
        query.append(" INNER JOIN appointments a  ON a.customer_id_fk = c.id AND c.record_state !=3 AND ");
        query.append(" a.record_state !=3 AND a.assigned_id_fk = ? WHERE c.customer_name LIKE ? OR ");
        query.append(" c.customer_location_identifier LIKE ? ORDER BY c.customer_name ASC ");

        log4jLog.info(" selectCustomersOnlyWhoHaveVisit for userId : " + userId);
        Object param[] = new Object[]{userId, "%" + searchText + "%", "%" + searchText + "%"};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Map>() {
                @Override
                public Map mapRow(ResultSet rs, int i) throws SQLException {
                    Map customer = new HashMap();
                    customer.put("customerId", rs.getInt("c.id"));
                    customer.put("value", rs.getString("c.customer_name") + "-" + rs.getString("c.customer_location_identifier"));
                    return customer;
                }
            });
        } catch (Exception e) {
            log4jLog.info(e + " selectCustomersOnlyWhoHaveVisit for userId : " + userId);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     *
     * @param customerId
     * @param accountId
     * @return
     */
    @Override
    public Customer deleteCustomer(int customerId, int accountId) {
        try {
            Customer customer = selectCustomer(customerId, accountId);
            String query = "UPDATE customers SET record_state =3, modified_on=now() WHERE id=?"; //Changed by siddhesh From delete to update.
            log4jLog.info(" deleteCustomer " + query);
            Object param[] = new Object[]{customerId};
        
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return customer;
            } else {
                return new Customer();
            }
        } catch (Exception e) {
            log4jLog.info(" deleteCustomer " + e);
//            e.printStackTrace();
            return new Customer();
        }
    }

    /**
     *
     * @param customer
     * @param accountId
     * @return
     */
    @Override
    public Customer updateCustomer(Customer customer, int accountId) {
        String query = "UPDATE customers SET customer_name=?, customer_printas=?,customer_location_identifier=?,is_headoffice=?, territory=?, industry=?,address1=?,phone1=?,phone2=?,phone3=?,fax1=?,fax2=?,email1=?, email2=?,email3=?, website1=?,website2=? ,modified_on=now(),modified_by_id_fk=?,record_state = 2 WHERE id=?";
        log4jLog.info(" updateCustomer " + query);
        Object param[] = new Object[]{customer.getCustomerName(), customer.getCustomerPrintas(), customer.getCustomerLocation(), customer.isIsHeadOffice(), customer.getTerritory(), customer.getIndustry(), customer.getCustomerAddress1(), customer.getCustomerPhone1(), customer.getCustomerPhone2(), customer.getCustomerPhone3(), customer.getCustomerFax1(), customer.getCustomerFax2(), customer.getCustomerEmail1(), customer.getCustomerEmail2(), customer.getCustomerEmail3(), customer.getCustomerWebsiteUrl1(), customer.getCustomerWebsiteUrl2(),customer.getModified_by().getId(), customer.getId()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return customer;
            } else {
                return new Customer();
            }
        } catch (Exception e) {
            log4jLog.info(" updateCustomer " + e);
//            e.printStackTrace();
            return new Customer();
        }
    }

    /**
     *
     * @param customer
     * @param accountId
     * @return
     */
    @Override
    public Customer updateCustomerAddress(Customer customer, int accountId) {
        String query = "UPDATE customers SET customer_name=?,address1=?,modified_on=now(),record_state = 2 WHERE id=?";
        log4jLog.info(" updateCustomer " + query);
        Object param[] = new Object[]{customer.getCustomerName(), customer.getCustomerAddress1(), customer.getId()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) == 1) {
                return customer;
            } else {
                return new Customer();
            }
        } catch (Exception e) {
            log4jLog.info(" updateCustomer " + e);
            return new Customer();
        }
    }

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    @Override
    public boolean isCustomerValid(int id, int accountId) {
        String query = "SELECT COUNT(id) FROM customers WHERE id=? AND record_state !=3";
        log4jLog.info(" isCustomerValid " + query);
        Object[] param = new Object[]{id};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) == 1) {
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
     *
     * @param customerName
     * @param accountId
     * @return
     */
    public int getCustomerId(String customerName, int accountId) {
        String query = "SELECT id FROM customers WHERE customer_name=? AND record_state !=3 ";
        log4jLog.info(" getCustomerId " + query);
        Object[] param = new Object[]{customerName};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     *
     * @param customerName
     * @param locationIdentifier
     * @param accountId
     * @return
     */
    public int getCustomerIdOnNmLocation(String customerName, String locationIdentifier, int accountId) {
        String query = "SELECT id FROM customers WHERE customer_name=? AND customer_location_identifier=? AND record_state !=3 ";
        log4jLog.info(" getCustomerIdOnNmLocation " + query);
        Object[] param = new Object[]{customerName, locationIdentifier};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     *
     * @param recentId
     * @param accountId
     * @return
     */
    @Override
    public List<Customer> selectRecentCustomers(int recentId, int accountId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT id,customer_name,customer_printas,customer_location_identifier,is_headoffice,territory,industry,address1,phone1,phone2,");
        query.append("phone3,fax1,fax2,email1,email2,email3,website1,website2,created_on FROM customers WHERE record_state !=3 ");
        if (recentId == 0) {
            query.append(" ORDER BY created_on DESC LIMIT 50");
        } else {
            query.append(" ORDER BY modified_on DESC LIMIT 50");
        }
//        String query = "SELECT id,customer_name,customer_printas,customer_location_identifier,is_headoffice,territory,industry,address1,phone1,phone2,phone3,fax1,fax2,email1,email2,email3,website1,website2,created_on FROM customers";
        log4jLog.info(" selectCustomers " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), new RowMapper<Customer>() {

                @Override
                public Customer mapRow(ResultSet rs, int i) throws SQLException {
                    Customer customer = new Customer();
                    customer.setId(rs.getInt("id"));
                    customer.setCustomerName(rs.getString("customer_name"));
                    customer.setCustomerPrintas(rs.getString("customer_printas"));
                    customer.setCustomerLocation(rs.getString("customer_location_identifier"));
                    customer.setIsHeadOffice(rs.getBoolean("is_headoffice"));
                    customer.setTerritory(rs.getString("territory"));
                    customer.setIndustry(rs.getString("industry"));
                    customer.setCustomerAddress1(rs.getString("address1"));
                    customer.setCustomerPhone1(rs.getString("phone1"));
                    customer.setCustomerPhone2(rs.getString("phone2"));
                    customer.setCustomerPhone3(rs.getString("phone3"));
                    customer.setCustomerFax1(rs.getString("fax1"));
                    customer.setCustomerFax2(rs.getString("fax2"));
                    customer.setCustomerEmail1(rs.getString("email1"));
                    customer.setCustomerEmail2(rs.getString("email2"));
                    customer.setCustomerEmail3(rs.getString("email3"));
                    customer.setCustomerWebsiteUrl1(rs.getString("website1"));
                    customer.setCustomerWebsiteUrl2(rs.getString("website2"));
                    customer.setCreatedOn(rs.getTimestamp("created_on"));
                    return customer;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectCustomer " + e);
//            e.printStackTrace();
            return new ArrayList<Customer>();
        }
    }

    /**
     *
     * @param customer
     * @param accountId
     * @param userid
     * @return
     */
    @Override
    public int insertCustomerByImport(Customer customer, int accountId,int userid) {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO customers(customer_name, customer_printas, customer_location_identifier, is_headoffice, territory, industry, address1, phone1, phone2, phone3, fax1, fax2, email1, email2, email3, website1, website2, created_on,created_by_id_fk,record_state) VALUES( ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?, now(),?,1)");
        query.append(" ON DUPLICATE KEY UPDATE");

        query.append(" customer_printas     = IFNULL(NULLIF(VALUES(customer_printas),\"\"),customer_printas),");
        query.append(" is_headoffice = IFNULL(NULLIF(VALUES(is_headoffice),\"\"),is_headoffice),");
        query.append(" territory = IFNULL(NULLIF(VALUES(territory),\"\"),territory),");
        query.append(" industry = IFNULL(NULLIF(VALUES(industry),\"\"),industry),");
        query.append(" address1 = IFNULL(NULLIF(VALUES(address1),\"\"),address1),");
        query.append(" phone1 = IFNULL(NULLIF(VALUES(phone1),\"\"),phone1),");
        query.append(" phone2 = IFNULL(NULLIF(VALUES(phone2),\"\"),phone2),");
        query.append(" phone3 = IFNULL(NULLIF(VALUES(phone3),\"\"),phone3),");
        query.append(" fax1 = IFNULL(NULLIF(VALUES(fax1),\"\"),fax1),");
        query.append(" fax2 = IFNULL(NULLIF(VALUES(fax2),\"\"),fax2),");
        query.append(" email1 = IFNULL(NULLIF(VALUES(email1),\"\"),email1),");
        query.append(" email2 = IFNULL(NULLIF(VALUES(email2),\"\"),email2),");
        query.append(" email3 = IFNULL(NULLIF(VALUES(email3),\"\"),email3),");
        query.append(" website1 = IFNULL(NULLIF(VALUES(website1),\"\"),website1),");
        query.append(" website2 = IFNULL(NULLIF(VALUES(website2),\"\"),website2),");
        query.append(" modified_on = VALUES(created_on),");//record_state
        query.append(" record_state = VALUES(record_state)");
        log4jLog.info(" insertCustomerByImport " + query);
        Object param[] = new Object[]{customer.getCustomerName(), customer.getCustomerPrintas(), customer.getCustomerLocation(), customer.isIsHeadOffice(), customer.getTerritory(), customer.getIndustry(), customer.getCustomerAddress1(), customer.getCustomerPhone1(), customer.getCustomerPhone2(), customer.getCustomerPhone3(), customer.getCustomerFax1(), customer.getCustomerFax2(), customer.getCustomerEmail1(), customer.getCustomerEmail2(), customer.getCustomerEmail3(), customer.getCustomerWebsiteUrl1(), customer.getCustomerWebsiteUrl2(),userid};
        try {
            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query.toString(), param) > 0) {
                    return FieldSenseUtils.getMaxIdForTable("customers", accountId);
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info(" insertCustomerByImport " + e);
            return 0;
        }
    }

    /**
     *
     * @param customerId
     * @param accountId
     * @return
     */
    @Override
    public String selectCustomerAddress(int customerId, int accountId) {
        String query = "SELECT address1 FROM customers WHERE id=? AND record_state !=3 ";
        log4jLog.info(" selectCustomerAddress " + query);
        Object param[] = new Object[]{customerId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" selectCustomerAddress " + e);
//            e.printStackTrace();
            return "";
        }
    }

    /**
     *
     * @param accountId
     * @return
     */
    @Override
    public List<String> selectCustomerIndustry(int accountId) {
        String query = "SELECT DISTINCT industry FROM customers WHERE record_state !=3 ORDER BY industry";
        log4jLog.info(" selectCustomerIndustry " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForList(query, String.class);
        } catch (Exception e) {
            log4jLog.info(" selectCustomerIndustry " + e);
//            e.printStackTrace();
            return new ArrayList<String>();
        }
    }

    /**
     *
     * @param accountId
     * @return
     */
    @Override
    public List<String> selectCustomerTerritory(int accountId) {
        String query = "SELECT DISTINCT territory FROM customers WHERE record_state !=3 ORDER BY territory";
        log4jLog.info(" selectCustomerTerritory " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForList(query, String.class);
        } catch (Exception e) {
            log4jLog.info(" selectCustomerTerritory " + e);
//            e.printStackTrace();
            return new ArrayList<String>();
        }
    }

    /**
     *
     * @param allRequestParams
     * @param accountId
     * @return
     */
    @Override
    public List<Customer> selectCustomersWithLimt(Map<String,String> allRequestParams,int accountId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT id,customer_name,customer_printas,customer_location_identifier,is_headoffice,territory,");
        query.append("industry,address1,phone1,phone2,phone3,fax1,fax2,email1,email2,email3,website1,website2,");
        query.append("created_on,(SELECT COUNT(id) FROM customers");
        String searchText=allRequestParams.get("searchText").trim();
        if(searchText.equals("")){
            query.append(" WHERE record_state !=3 ) AS customersCount FROM customers WHERE record_state !=3 ");
        }else{
            query.append(" WHERE (customer_name LIKE ? OR email1 LIKE ? OR phone1 LIKE ?) AND record_state !=3 ) AS customersCount  FROM customers WHERE ( customer_name LIKE ? OR email1 LIKE ? OR phone1 LIKE ? ) AND record_state !=3 ");
        }
        String sortcolindex=allRequestParams.get("order[0][column]");
        if(sortcolindex==null){
            query.append(" ORDER BY customer_name");
        }else{
            switch(Integer.parseInt(sortcolindex)){
                case 0:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append(" ORDER BY customer_name");
                    }else{
                        query.append(" ORDER BY customer_name DESC");
                    }
                    break;
                case 2:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append(" ORDER BY email1");
                    }else{
                        query.append(" ORDER BY email1 DESC");
                    }
                    break;
                case 3:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append(" ORDER BY industry");
                    }else{
                        query.append(" ORDER BY industry DESC");
                    }
                    break;
                case 4:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append(" ORDER BY territory");
                    }else{
                        query.append(" ORDER BY territory DESC");
                    }
                    break;    
                default:
                   query.append(" ORDER BY customer_name");
                   break;
            }        
        }
        log4jLog.info(" selectAttendaceReportForAdmin " + query);
        final int start=Integer.parseInt(allRequestParams.get("start"));
        final int length=Integer.parseInt(allRequestParams.get("length"));
        Object param[]=new Object[10];
        //if(length!=-1){
            query.append(" LIMIT ? OFFSET ?");
            if(searchText.equals("")){
                param = new Object[]{length,start};
            }else{
                 param =  new Object[]{"%"+searchText+"%","%"+searchText+"%","%"+searchText+"%","%"+searchText+"%","%"+searchText+"%","%"+searchText+"%",length,start};
            }
       // }else{
       //     param = new Object[]{};
       // }    
       // query.append(" INNER JOIN ");
        //query.append("(SELECT id FROM customers ORDER BY customer_name LIMIT 10 OFFSET ?) AS my_results USING(id)");
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Customer>() {

                @Override
                public Customer mapRow(ResultSet rs, int i) throws SQLException {
                    Customer customer = new Customer();
                    customer.setId(rs.getInt("id"));
                    customer.setCustomerName(rs.getString("customer_name"));
                    customer.setCustomerPrintas(rs.getString("customer_printas"));
                    customer.setCustomerLocation(rs.getString("customer_location_identifier"));
                    customer.setIsHeadOffice(rs.getBoolean("is_headoffice"));
                    customer.setTerritory(rs.getString("territory"));
                    customer.setIndustry(rs.getString("industry"));
                    customer.setCustomerAddress1(rs.getString("address1"));
                    customer.setCustomerPhone1(rs.getString("phone1"));
                    customer.setCustomerPhone2(rs.getString("phone2"));
                    customer.setCustomerPhone3(rs.getString("phone3"));
                    customer.setCustomerFax1(rs.getString("fax1"));
                    customer.setCustomerFax2(rs.getString("fax2"));
                    customer.setCustomerEmail1(rs.getString("email1"));
                    customer.setCustomerEmail2(rs.getString("email2"));
                    customer.setCustomerEmail3(rs.getString("email3"));
                    customer.setCustomerWebsiteUrl1(rs.getString("website1"));
                    customer.setCustomerWebsiteUrl2(rs.getString("website2"));
                    customer.setCreatedOn(rs.getTimestamp("created_on"));
                    customer.setCustomersCount(rs.getInt("customersCount"));
                    return customer;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectCustomer " + e);
//            e.printStackTrace();
            return new ArrayList<Customer>();
        }
    }

    /**
     *
     * @param allRequestParams
     * @param userId
     * @param accountId
     * @return
     */
    @Override
    public List<Customer> selectUserCustomersWithLimt(Map<String,String> allRequestParams,int userId,int accountId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT c.id, c.customer_name,c.customer_printas,c.customer_location_identifier,c.is_headoffice,c.territory,c.industry,");
        query.append("c.address1,c.phone1,c.phone2,c.phone3,c.fax1,c.fax2,c.email1,c.email2,c.email3,c.website1,c.website2,c.created_on,");
        query.append(" (SELECT COUNT(*) FROM customers c1 LEFT JOIN territory_categories t1 ");
        query.append("ON c1.territory=t1.category_name INNER JOIN user_territory u1 ON  t1.id=u1.teritory_id AND u1.user_id_fk=?  ");
        //query.append(" and t1.is_active=1 ");
        String searchText=allRequestParams.get("searchText").trim();
        if(searchText.equals("")){
            query.append(" WHERE c1.record_state !=3 ) AS customersCount FROM customers c ");
        }else{
            query.append(" WHERE c1.record_state !=3 AND c1.customer_name LIKE ? OR c1.email1 LIKE ? OR c1.phone1 LIKE ? ) AS customersCount  FROM customers c ");
        }
        query.append(" LEFT JOIN territory_categories t ON c.territory=t.category_name INNER JOIN user_territory u ON  t.id=u.teritory_id ");
        query.append(" AND u.user_id_fk=? ");
        query.append(" WHERE c.record_state !=3  "); // added by jyoti, to fix the bug #30740
        if(!searchText.equals("")){
//            query.append(" WHERE c.record_state !=3 AND c.customer_name LIKE ? OR c.email1 LIKE ? OR c.phone1 LIKE ?  "); // commented by jyoti
            query.append(" AND c.customer_name LIKE ? OR c.email1 LIKE ? OR c.phone1 LIKE ?  "); // added by jyoti
        }
        String sortcolindex=allRequestParams.get("order[0][column]");
        if(sortcolindex==null){
            query.append(" ORDER BY customer_name");
        }else{
            switch(Integer.parseInt(sortcolindex)){
                case 0:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append(" ORDER BY customer_name");
                    }else{
                        query.append(" ORDER BY customer_name DESC");
                    }
                    break;
                case 2:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append(" ORDER BY email1");
                    }else{
                        query.append(" ORDER BY email1 DESC");
                    }
                    break;
                case 3:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append(" ORDER BY industry");
                    }else{
                        query.append(" ORDER BY industry DESC");
                    }
                    break;
                case 4:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append(" ORDER BY territory");
                    }else{
                        query.append(" ORDER BY territory DESC");
                    }
                    break;    
                default:
                   query.append(" ORDER BY customer_name");
                   break;
            }        
        }
        log4jLog.info(" selectAttendaceReportForAdmin " + query);
        final int start=Integer.parseInt(allRequestParams.get("start"));
        final int length=Integer.parseInt(allRequestParams.get("length"));
        Object param[]=new Object[10];
        //if(length!=-1){
            query.append(" LIMIT ? OFFSET ?");
            if(searchText.equals("")){
                param = new Object[]{userId,userId,length,start};
            }else{
                 param =  new Object[]{userId,"%"+searchText+"%","%"+searchText+"%","%"+searchText+"%",userId,"%"+searchText+"%","%"+searchText+"%","%"+searchText+"%",length,start};
            }
       // }else{
       //     param = new Object[]{};
       // }    
       // query.append(" INNER JOIN ");
        //query.append("(SELECT id FROM customers ORDER BY customer_name LIMIT 10 OFFSET ?) AS my_results USING(id)");
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Customer>() {

                @Override
                public Customer mapRow(ResultSet rs, int i) throws SQLException {
                    Customer customer = new Customer();
                    customer.setId(rs.getInt("id"));
                    customer.setCustomerName(rs.getString("customer_name"));
                    customer.setCustomerPrintas(rs.getString("customer_printas"));
                    customer.setCustomerLocation(rs.getString("customer_location_identifier"));
                    customer.setIsHeadOffice(rs.getBoolean("is_headoffice"));
                    customer.setTerritory(rs.getString("territory"));
                    customer.setIndustry(rs.getString("industry"));
                    customer.setCustomerAddress1(rs.getString("address1"));
                    customer.setCustomerPhone1(rs.getString("phone1"));
                    customer.setCustomerPhone2(rs.getString("phone2"));
                    customer.setCustomerPhone3(rs.getString("phone3"));
                    customer.setCustomerFax1(rs.getString("fax1"));
                    customer.setCustomerFax2(rs.getString("fax2"));
                    customer.setCustomerEmail1(rs.getString("email1"));
                    customer.setCustomerEmail2(rs.getString("email2"));
                    customer.setCustomerEmail3(rs.getString("email3"));
                    customer.setCustomerWebsiteUrl1(rs.getString("website1"));
                    customer.setCustomerWebsiteUrl2(rs.getString("website2"));
                    customer.setCreatedOn(rs.getTimestamp("created_on"));
                    customer.setCustomersCount(rs.getInt("customersCount"));
                    return customer;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectCustomer " + e);
//            e.printStackTrace();
            return new ArrayList<Customer>();
        }
    }
    
    /**
     *
     * @param customer
     * @param accountId
     * @return
     */
    public Customer updateCustomerAddressLatLong(Customer customer, int accountId) {
        String query = "UPDATE customers SET address1=?,modified_on=now(),record_state = 2 WHERE id=?";
        log4jLog.info(" updateCustomerAddressLatLong " + query);
        Object param[] = new Object[]{customer.getCustomerAddress1(), customer.getId()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) == 1) {
                String query1 = "UPDATE location SET latitude=?,longitude=? WHERE location_type_id_fk=?";
                Object param1[] = new Object[]{customer.getLasknownLatitude(), customer.getLasknownLangitude(), customer.getId()};
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query1, param1) == 1) {
                    return customer;
                } else {
                    return new Customer();
                }
            } else {
                return new Customer();
            }
        } catch (Exception e) {
            log4jLog.info(" updateCustomerAddressLatLong " + e);
            return new Customer();
        }
    }

    /**
     *
     * @param customer
     * @param accountId
     * @return
     */
    @Override
    public boolean updateCustomerAddressOnly(Customer customer, int accountId) {
        String query = "UPDATE customers SET address1=?,modified_on=now(),record_state = 2 WHERE id=?";
        log4jLog.info(" updateCustomerAddressOnly " + query);
        Object param[] = new Object[]{customer.getCustomerAddress1(), customer.getId()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
//            e.printStackTrace();
            log4jLog.info(" updateCustomerAddressOnly " + query);
            return false;
        }
    }

    /**
     *
     * @param customerName
     * @param customerLocation
     * @param accountId
     * @return
     */
    @Override
    public boolean isCustomerExistWithLocation(String customerName, String customerLocation, int accountId) {
        String query = "SELECT COUNT(id) FROM customers WHERE customer_name=? AND customer_location_identifier=? AND record_state !=3 ";
        log4jLog.info(" Inside CustomerDaoImpl class isCustomerExistWithLocation() method " + query);
        Object param[] = new Object[]{customerName, customerLocation};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            log4jLog.info(" Inside CustomerDaoImpl class isCustomerExistWithLocation() method " + e);
            return true;
        }
    }
    
    /**
     *
     * @param territory
     * @param accountId
     * @return
     */
    @Override
    public boolean isTerritoryExists(String territory,int accountId){
        String query = "SELECT COUNT(id) FROM territory_categories WHERE category_name=? AND is_active=1";
        log4jLog.info(" is territory exists " + query);
        Object param[] = new Object[]{territory};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param,Integer.class) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
//            e.printStackTrace();
            log4jLog.info(" is territory exists " + e);
            return false;
        }
    }
    
    public List<HashMap<String,Object>> getNearByCustomers(double lat,double lang,int radius,int accountId) {
        try{
       String query="SELECT latitude, longitude, 3956 * 2 * ASIN(SQRT( POWER(SIN((? - abs(latitude)) * pi()/180/2),2)+COS(? * pi()/180 )*COS(abs(latitude)*pi()/180)*POWER(SIN((?-longitude)*pi()/180/2),2))) as distance, location_type_id_fk, customer_name ,territory, address1 , customer_location_identifier FROM location INNER JOIN customers on location.location_type_id_fk = customers.id " +
       "WHERE location_type_id_fk is not null and longitude between (?-?/abs(cos(radians(?))*69)) and (?+?/abs(cos(radians(?))*69)) and latitude between (?-(?/69)) and (?+(?/69)) having distance < ? ORDER BY distance";
//       Object param[] = new Object[]{customer.getLasknownLatitude(),customer.getLasknownLatitude(),customer.getLasknownLangitude(),customer.getLasknownLangitude(),customer.getCustomerType(),customer.getLasknownLangitude(),customer.getCustomerType(),customer.getLasknownLatitude(),customer.getCustomerType(),customer.getLasknownLatitude(),customer.getCustomerType(),customer.getCustomerType()};
       Object param[] = new Object[]{lat,lat,lang,lang,radius,lang,lang,radius,lang,lat,radius,lat,radius,radius};
    return  FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<HashMap<String,Object>>(){

                @Override
                public HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    HashMap<String,Object> mapOfData=new HashMap<String, Object>();
                    mapOfData.put("latitude", rs.getDouble("latitude"));
                    mapOfData.put("longitude", rs.getDouble("longitude"));
                    mapOfData.put("distance", rs.getDouble("distance"));
                    mapOfData.put("customerId", rs.getInt("location_type_id_fk"));
                    mapOfData.put("customer_name", rs.getString("customer_name"));
                     mapOfData.put("territory", rs.getString("territory"));         
                     mapOfData.put("customerLocationIdentifier", rs.getString("customer_location_identifier"));
                    return mapOfData;
                }
            });
      
        }catch(Exception e){
        e.printStackTrace();
       return new ArrayList<HashMap<String,Object>>();
        }
    }
  
}
