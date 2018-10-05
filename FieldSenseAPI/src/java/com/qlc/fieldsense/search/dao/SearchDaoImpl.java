/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.search.dao;

import com.qlc.fieldsense.accounts.model.Account;
import com.qlc.fieldsense.customer.model.Customer;
import com.qlc.fieldsense.search.model.SearchCustomerMobile;
import com.qlc.fieldsense.search.model.SearchKeysCustomer;
import com.qlc.fieldsense.search.model.SearchKeysUser;
import com.qlc.fieldsense.stats.dao.StatsDao;
import com.qlc.fieldsense.stats.model.AccountData;
import com.qlc.fieldsense.stats.model.Stats;
import com.qlc.fieldsense.team.model.Team;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Ramesh
 * @date 12-05-2014
 */
public class SearchDaoImpl implements SearchDao {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("CustomerDaoImpl");
    JdbcTemplate jdbcTemplate;

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
     * @param offset
     * @param searchText
     * @param accountId
     * @return
     */
    @Override
    public List<Customer> searchCustomer(int offset, String searchText, int accountId) {
        StringBuilder query = new StringBuilder();
        /*query.append("SELECT id,customer_name,customer_printas,customer_location_identifier,is_headoffice,territory,industry,address1,phone1,phone2,phone3,");
         query.append("fax1,fax2,email1,email2,email3,website1,website2,created_on FROM customers");
         query.append(" WHERE customer_name LIKE ? || address1 LIKE ? ");*/
        /*query.append("SELECT id,customer_name,customer_printas,customer_location_identifier,is_headoffice,territory,");
         query.append("industry,address1,phone1,phone2,phone3,fax1,fax2,email1,email2,email3,website1,website2,");
         query.append("created_on,(SELECT count(id) FROM customers WHERE (customer_name LIKE ? || customer_printas LIKE ?)) as customersCount FROM customers ");
         query.append(" INNER JOIN (SELECT id FROM customers WHERE (customer_name LIKE ? || customer_printas LIKE ?) ORDER BY customer_name LIMIT 10 OFFSET ?) AS my_results USING(id)");
         query.append(" WHERE (customer_name LIKE ? || customer_printas LIKE ?)");
         */
        query.append("SELECT id,customer_name,customer_printas,customer_location_identifier,is_headoffice,territory,");
        query.append("industry,address1,phone1,phone2,phone3,fax1,fax2,email1,email2,email3,website1,website2,");
        query.append("created_on,(SELECT count(id) FROM customers WHERE record_state !=3 AND (customer_name LIKE ? )) as customersCount FROM customers ");
        query.append(" INNER JOIN (SELECT id FROM customers WHERE record_state !=3 AND (customer_name LIKE ? ) ORDER BY customer_name LIMIT 10 OFFSET ?) AS my_results USING(id)");
        query.append(" WHERE record_state !=3 AND (customer_name LIKE ? )");

        Object param[] = new Object[]{"" + searchText + "%", "" + searchText + "%", offset, "" + searchText + "%"};
        log4jLog.info(" selectCustomers " + query);
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
     * @param offset
     * @param searchKeys
     * @param accountId
     * @return
     */
    @Override
    public List<Customer> searchCustomersByLocationAndType(int offset, SearchKeysCustomer searchKeys, int accountId) {
        StringBuilder query = new StringBuilder();
        StringBuilder query2 = new StringBuilder();
        int counter = 0;
        int cretedOnCounter = 0;
        int modifiedOnCounter = 0;
        if (searchKeys.getRecentItems() != null) {
            List<String> recentKeys = new ArrayList<String>();
            recentKeys = searchKeys.getRecentItems();
            for (int i = 0; i < recentKeys.size(); i++) {
                if (i == 0) {
                    String type = recentKeys.get(i);
                    if (type.equals("cretedOn")) {
                        cretedOnCounter++;
                    }
                    if (type.equals("modifiedOn")) {
                        modifiedOnCounter++;
                    }
                } else {
                    String type = recentKeys.get(i);
                    if (type.equals("cretedOn")) {
                        cretedOnCounter++;
                    }
                    if (type.equals("modifiedOn")) {
                        modifiedOnCounter++;
                    }
                }
            }
        }
        ArrayList<String> keys = new ArrayList<String>();
        if (searchKeys.getTrritory() != null && !searchKeys.getTrritory().isEmpty()) {
            if (counter == 0) {
                query2.append(" AND ");
                counter++;
            }
            List<String> territoryKeys = new ArrayList<String>();
            territoryKeys = searchKeys.getTrritory();
            for (int i = 0; i < territoryKeys.size(); i++) {
                if (i == 0) {
                    query2.append(" territory LIKE ?");
                    keys.add(territoryKeys.get(i));
                } else {
//                    query2.append(" OR territory LIKE ?");
                    query2.append(" AND territory LIKE ?");
                    keys.add(territoryKeys.get(i));
                }
            }
        }
        if (searchKeys.getIndustry() != null && !searchKeys.getIndustry().isEmpty()) {
//            if (counter == 0) {
//                query2.append(" WHERE ");
//                counter++;
//            } else {
//                query2.append(" OR ");
                query2.append(" AND ");
//            }
            List<String> industryKeys = new ArrayList<String>();
            industryKeys = searchKeys.getIndustry();
            for (int i = 0; i < industryKeys.size(); i++) {
                if (i == 0) {
                    query2.append(" industry LIKE ?");
                    keys.add(industryKeys.get(i));
                } else {
//                    query2.append(" OR industry LIKE ?");
                    query2.append(" AND industry LIKE ?");
                    keys.add(industryKeys.get(i));
                }
            }
        }
        if (cretedOnCounter != 0) {
            if (modifiedOnCounter == 0) {
//                if (counter == 0) {
//                    query2.append(" WHERE ");
//                    counter++;
//                } else {
//                    query2.append(" OR ");
                    query2.append(" AND ");
//                }
                query2.append(" (created_on BETWEEN DATE_SUB(NOW(), INTERVAL 10 day) AND now())");
            } else {
//                if (counter == 0) {
//                    query2.append(" WHERE ");
//                    counter++;
//                } else {
//                    query2.append(" OR ");
                    query2.append(" AND ");
//                }
//                query2.append(" (created_on BETWEEN DATE_SUB(NOW(), INTERVAL 10 day) AND now()) OR (modified_on BETWEEN DATE_SUB(NOW(), INTERVAL 10 day) AND now()) ");
                query2.append(" (created_on BETWEEN DATE_SUB(NOW(), INTERVAL 10 day) AND now()) AND (modified_on BETWEEN DATE_SUB(NOW(), INTERVAL 10 day) AND now()) ");
            }
        }
        if (modifiedOnCounter != 0) {
            if (cretedOnCounter == 0) {
//                if (counter == 0) {
//                    query2.append(" WHERE ");
//                    counter++;
//                } else {
//                    query2.append(" OR ");
                    query2.append(" AND ");
//                }
                query2.append(" (modified_on BETWEEN DATE_SUB(NOW(), INTERVAL 10 day) AND now())");
            }
        }
        query.append("SELECT id,customer_name,customer_printas,customer_location_identifier,is_headoffice,territory,industry,address1,phone1,phone2,phone3,");
        query.append("fax1,fax2,email1,email2,email3,website1,website2,created_on,");
        query.append("(SELECT count(id) FROM customers WHERE record_state !=3 ");
        query.append(query2);
        query.append(") as customersCount  FROM customers");
        query.append(" INNER JOIN ");
        query.append("(SELECT id FROM customers WHERE record_state !=3 ");
        query.append(query2);
        query.append(" ORDER BY customer_name LIMIT 10 OFFSET ?) AS my_results USING(id) WHERE record_state !=3 ");
        query.append(query2);

        Object param[] = new Object[(3 * (keys.size())) + 1];
        int j = 0;
        for (int i = 0; i < keys.size(); i++) {
            param[j] = keys.get(i);
            j++;
        }
        for (int i = 0; i < keys.size(); i++) {
            param[j] = keys.get(i);
            j++;
        }
        param[j] = offset;
        j++;
        for (int i = 0; i < keys.size(); i++) {
            param[j] = keys.get(i);
            j++;
        }
        log4jLog.info(" selectCustomers " + query);
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
     * @param searchKeys
     * @param accountId
     * @return
     */
    @Override
    public List<Customer> searchCustomersByLocationAndType2(@RequestParam Map<String,String> allRequestParams, SearchKeysCustomer searchKeys, int accountId) {
        StringBuilder query = new StringBuilder();
        StringBuilder query2 = new StringBuilder();
        int counter = 0;
        int cretedOnCounter = 0;
        int modifiedOnCounter = 0;
        int offset=Integer.parseInt(allRequestParams.get("start"));
        int length=Integer.parseInt(allRequestParams.get("length"));
        if (searchKeys.getRecentItems() != null && !searchKeys.getRecentItems().isEmpty()) {
            List<String> recentKeys = new ArrayList<String>();
            recentKeys = searchKeys.getRecentItems();
            for (int i = 0; i < recentKeys.size(); i++) {
                if (i == 0) {
                    String type = recentKeys.get(i);
                    if (type.equals("cretedOn")) {
                        cretedOnCounter++;
                    }
                    if (type.equals("modifiedOn")) {
                        modifiedOnCounter++;
                    }
                } else {
                    String type = recentKeys.get(i);
                    if (type.equals("cretedOn")) {
                        cretedOnCounter++;
                    }
                    if (type.equals("modifiedOn")) {
                        modifiedOnCounter++;
                    }
                }
            }
        }
        ArrayList<String> keys = new ArrayList<String>();
        if (searchKeys.getTrritory() != null && !searchKeys.getTrritory().isEmpty()) {
            if (counter == 0) {
                query2.append(" AND ");
                counter++;
            }
            List<String> territoryKeys = new ArrayList<String>();
            territoryKeys = searchKeys.getTrritory();
            for (int i = 0; i < territoryKeys.size(); i++) {
                if (i == 0) {
                    query2.append("( territory LIKE ?");
                    keys.add(territoryKeys.get(i));
                } else {
//                    query2.append(" OR territory LIKE ?");
                    query2.append(" OR territory LIKE ?");
                    keys.add(territoryKeys.get(i));
                }
                if(i==(territoryKeys.size()-1)){
                    query2.append(")");
                }
            }
        }
        if (searchKeys.getIndustry() != null && !searchKeys.getIndustry().isEmpty()) {
//            if (counter == 0) {
//                query2.append(" WHERE ");
//                counter++;
//            } else {
//                query2.append(" OR ");
                query2.append(" AND ");
//            }
            List<String> industryKeys = new ArrayList<String>();
            industryKeys = searchKeys.getIndustry();
            for (int i = 0; i < industryKeys.size(); i++) {
                if (i == 0) {
                    query2.append("( industry LIKE ?");
                    keys.add(industryKeys.get(i));
                } else {
//                    query2.append(" OR industry LIKE ?");
                    query2.append(" OR industry LIKE ?");
                    keys.add(industryKeys.get(i));
                }
                if(i==(industryKeys.size()-1)){
                    query2.append(")");
                }
            }
        }
        if (cretedOnCounter != 0) {
            if (modifiedOnCounter == 0) {
//                if (counter == 0) {
//                    query2.append(" WHERE ");
//                    counter++;
//                } else {
//                    query2.append(" OR ");
                    query2.append(" AND ");
//                }
                query2.append(" (created_on BETWEEN DATE_SUB(NOW(), INTERVAL 10 day) AND now())");
            } else {
//                if (counter == 0) {
//                    query2.append(" WHERE ");
//                    counter++;
//                } else {
//                    query2.append(" OR ");
                    query2.append(" AND ");
//                }
//                query2.append(" (created_on BETWEEN DATE_SUB(NOW(), INTERVAL 10 day) AND now()) OR (modified_on BETWEEN DATE_SUB(NOW(), INTERVAL 10 day) AND now()) ");
                query2.append(" ((created_on BETWEEN DATE_SUB(NOW(), INTERVAL 10 day) AND now()) OR (modified_on BETWEEN DATE_SUB(NOW(), INTERVAL 10 day) AND now())) ");
            }
        }
        if (modifiedOnCounter != 0) {
            if (cretedOnCounter == 0) {
//                if (counter == 0) {
//                    query2.append(" WHERE ");
//                    counter++;
//                } else {
//                    query2.append(" OR ");
                    query2.append(" AND ");
//                }
                query2.append(" (modified_on BETWEEN DATE_SUB(NOW(), INTERVAL 10 day) AND now())");
            }
        }
        query.append("SELECT id,customer_name,customer_printas,customer_location_identifier,is_headoffice,territory,industry,address1,phone1,phone2,phone3,");
        query.append("fax1,fax2,email1,email2,email3,website1,website2,created_on,");
        query.append("(SELECT count(id) FROM customers WHERE record_state != 3 ");
        query.append(query2);
        query.append(") as customersCount  FROM customers ");
        query.append(" INNER JOIN ");
        query.append("(SELECT id FROM customers WHERE record_state != 3 ");
        query.append(query2);
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
        query.append(" LIMIT ? OFFSET ?) AS my_results USING(id) WHERE record_state != 3 ");
        query.append(query2);

        Object param[] = new Object[(3 * (keys.size())) + 2];
        int j = 0;
        for (int i = 0; i < keys.size(); i++) {
            param[j] = keys.get(i);
            j++;
        }
        for (int i = 0; i < keys.size(); i++) {
            param[j] = keys.get(i);
            j++;
        }
        param[j] = length;
        j++;
        param[j] = offset;
        j++;
        for (int i = 0; i < keys.size(); i++) {
            param[j] = keys.get(i);
            j++;
        }
        log4jLog.info(" selectCustomers " + query);
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
     * @param searchKeys
     * @param userId
     * @param accountId
     * @return
     */
    @Override
    public List<Customer> searchCustomersByLocationAndType2(@RequestParam Map<String,String> allRequestParams, SearchKeysCustomer searchKeys,int userId, int accountId) {
        StringBuilder query = new StringBuilder();
        StringBuilder query2 = new StringBuilder();
        int counter = 0;
        int cretedOnCounter = 0;
        int modifiedOnCounter = 0;
        int offset=Integer.parseInt(allRequestParams.get("start"));
        int length=Integer.parseInt(allRequestParams.get("length"));
        if (searchKeys.getRecentItems() != null && !searchKeys.getRecentItems().isEmpty()) {
            List<String> recentKeys = new ArrayList<String>();
            recentKeys = searchKeys.getRecentItems();
            for (int i = 0; i < recentKeys.size(); i++) {
                if (i == 0) {
                    String type = recentKeys.get(i);
                    if (type.equals("cretedOn")) {
                        cretedOnCounter++;
                    }
                    if (type.equals("modifiedOn")) {
                        modifiedOnCounter++;
                    }
                } else {
                    String type = recentKeys.get(i);
                    if (type.equals("cretedOn")) {
                        cretedOnCounter++;
                    }
                    if (type.equals("modifiedOn")) {
                        modifiedOnCounter++;
                    }
                }
            }
        }
        ArrayList<String> keys = new ArrayList<String>();
        if (searchKeys.getTrritory() != null && !searchKeys.getTrritory().isEmpty()) {
            if (counter == 0) {
                query2.append(" AND ");
                counter++;
            }
            List<String> territoryKeys = new ArrayList<String>();
            territoryKeys = searchKeys.getTrritory();
            for (int i = 0; i < territoryKeys.size(); i++) {
                if (i == 0) {
                    query2.append("( territory LIKE ?");
                    keys.add(territoryKeys.get(i));
                } else {
//                    query2.append(" OR territory LIKE ?");
                    query2.append(" OR territory LIKE ?");
                    keys.add(territoryKeys.get(i));
                }
                if(i==(territoryKeys.size()-1)){
                    query2.append(")");
                }
            }
        }
        if (searchKeys.getIndustry() != null && !searchKeys.getIndustry().isEmpty()) {
//            if (counter == 0) {
//                query2.append(" WHERE ");
//                counter++;
//            } else {
//                query2.append(" OR ");
                query2.append(" AND ");
//            }
            List<String> industryKeys = new ArrayList<String>();
            industryKeys = searchKeys.getIndustry();
            for (int i = 0; i < industryKeys.size(); i++) {
                if (i == 0) {
                    query2.append("( industry LIKE ?");
                    keys.add(industryKeys.get(i));
                } else {
//                    query2.append(" OR industry LIKE ?");
                    query2.append(" OR industry LIKE ?");
                    keys.add(industryKeys.get(i));
                }
                if(i==(industryKeys.size()-1)){
                    query2.append(")");
                }
            }
        }
        if (cretedOnCounter != 0) {
            if (modifiedOnCounter == 0) {
//                if (counter == 0) {
//                    query2.append(" WHERE ");
//                    counter++;
//                } else {
//                    query2.append(" OR ");
                    query2.append(" AND ");
//                }
                query2.append(" (created_on BETWEEN DATE_SUB(NOW(), INTERVAL 10 day) AND now())");
            } else {
//                if (counter == 0) {
//                    query2.append(" WHERE ");
//                    counter++;
//                } else {
//                    query2.append(" OR ");
                    query2.append(" AND ");
//                }
//                query2.append(" (created_on BETWEEN DATE_SUB(NOW(), INTERVAL 10 day) AND now()) OR (modified_on BETWEEN DATE_SUB(NOW(), INTERVAL 10 day) AND now()) ");
                query2.append(" ((created_on BETWEEN DATE_SUB(NOW(), INTERVAL 10 day) AND now()) OR (modified_on BETWEEN DATE_SUB(NOW(), INTERVAL 10 day) AND now())) ");
            }
        }
        if (modifiedOnCounter != 0) {
            if (cretedOnCounter == 0) {
//                if (counter == 0) {
//                    query2.append(" WHERE ");
//                    counter++;
//                } else {
//                    query2.append(" OR ");
                    query2.append(" AND ");
//                }
                query2.append(" (modified_on BETWEEN DATE_SUB(NOW(), INTERVAL 10 day) AND now())");
            }
        }
        query.append("SELECT c.id, c.customer_name,c.customer_printas,c.customer_location_identifier,c.is_headoffice,c.territory,");
        query.append("c.industry,c.address1,c.phone1,c.phone2,c.phone3,c.fax1,c.fax2,c.email1,c.email2,c.email3,c.website1,c.website2,c.created_on,");
        query.append("(SELECT count(*) FROM customers c1 left join territory_categories t1 on c1.territory=t1.category_name inner join ");
        query.append("user_territory u1 on  t1.id=u1.teritory_id and u1.user_id_fk=? WHERE record_state !=3  ");
        //query.append(" and t1.is_active=1 ");
        query.append(query2);
        query.append(") as customersCount  FROM customers c ");
        query.append(" left join territory_categories t on c.territory=t.category_name inner join user_territory ");
        query.append(" u on  t.id=u.teritory_id and u.user_id_fk=? WHERE record_state !=3 ");
        //query.append(" and t.is_active=1 ");
        query.append(query2);
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
        query.append(" LIMIT ? OFFSET ?");
        //query.append(query2);

        Object param[] = new Object[(2 * (keys.size())) + 4];
        int j = 0;
        param[j] = userId;
        j++;
        for (int i = 0; i < keys.size(); i++) {
            param[j] = keys.get(i);
            j++;
        }
        param[j] = userId;
        j++;
        for (int i = 0; i < keys.size(); i++) {
            param[j] = keys.get(i);
            j++;
        }
        param[j] = length;
        j++;
        param[j] = offset;
        j++;
       // for (int i = 0; i < keys.size(); i++) {
       //     param[j] = keys.get(i);
        //    j++;
      //  }
        log4jLog.info(" selectCustomers " + query);
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
     * @param searchText
     * @param offset
     * @param accountId
     * @return
     */
    @Override
    public List<User> searchUser(String searchText, int offset, int accountId) {
        StringBuilder query = new StringBuilder();
        /* query.append("SELECT id,account_id_fk,first_name,last_name,email_address,password,mobile_number,gender,role,");
         query.append("active,last_logged_on,last_known_location_time,last_known_location,created_on,created_by,");
         query.append("(SELECT count(id) FROM users WHERE account_id_fk=? AND role!=0 AND (full_name LIKE ? ");
         query.append(")) AS usersCount FROM users INNER JOIN (SELECT id FROM users WHERE account_id_fk=?");
         query.append(" AND role!=0 AND (full_name LIKE ?) ORDER BY first_name LIMIT 10 OFFSET ?) AS my_results USING(id)");*/

        query.append("SELECT u.id,account_id_fk,first_name,last_name,email_address,password,mobile_number,gender,role,");
        query.append("active,u.last_logged_on,u.last_known_location_time,u.last_known_location,u.created_on,u.created_by,");
        query.append("(SELECT count(id) FROM fieldsense.users WHERE account_id_fk=? AND role!=0 AND (full_name LIKE ? ");
        query.append(")) AS usersCount,IFNULL(a.punch_in,0) punchIntime, IFNULL(a.punch_out,0) punchOutTime");
        query.append(" FROM fieldsense.users AS u INNER JOIN (SELECT id FROM fieldsense.users WHERE account_id_fk=?");
        query.append(" AND role!=0 AND (full_name LIKE ?) ORDER BY first_name LIMIT 10 OFFSET ?) AS my_results USING(id)");
        query.append(" LEFT OUTER JOIN attendances as a ON u.id=a.user_id_fk AND a.punch_date=CURDATE() ");

        Object param[] = new Object[]{accountId, searchText + "%", accountId, searchText + "%", offset};
        log4jLog.info(" searchUser " + query);
        try {
//            return jdbcTemplate.query(query.toString(), param, new RowMapper<User>() {

            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<User>() {

                @Override
                public User mapRow(ResultSet rs, int i) throws SQLException {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmailAddress(rs.getString("email_address"));
                    user.setMobileNo(rs.getString("mobile_number"));
                    user.setGender(rs.getInt("gender"));
                    user.setActive(rs.getBoolean("active"));
                    user.setUsersCount(rs.getInt("usersCount"));
                    String tempTime = rs.getString("punchOutTime");
                    String temp = "00:00:00";
                    if (tempTime.equals(temp)) {
                        user.setPunchStatusTime(rs.getString("punchIntime"));
                        user.setPunchStatus("PunchIn");
                    } else if (tempTime.equals("0")) {
                        user.setPunchStatus("NotPunchIn");
                    } else {
                        user.setPunchStatusTime(rs.getString("punchOutTime"));
                        user.setPunchStatus("PunchOut");
                    }
                    return user;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" searchUser " + e);
//            e.printStackTrace();
            return new ArrayList<User>();
        }
    }

    /**
     *
     * @param allRequestParams
     * @param searchKeys
     * @param accountId
     * @return
     */
    @Override
    public List<User> searchUserWithFilter(@RequestParam Map<String,String> allRequestParams,SearchKeysUser searchKeys, int accountId) {
        StringBuilder query = new StringBuilder();
        StringBuilder query2 = new StringBuilder();
        // Added by Jyoti
        StringBuilder query3 = new StringBuilder();
        StringBuilder query4 = new StringBuilder();
        StringBuilder query5 = new StringBuilder();
        // Ended By Jyoti
        int counterforTerritory = 0;
        int start=Integer.parseInt(allRequestParams.get("start"));
        int length=Integer.parseInt(allRequestParams.get("length"));
        int counter = 0;
        int activeCounter = 0;
        int inactiveCounter = 0;
            
        
        // Added by Jyoti
        ArrayList<String> keys = new ArrayList<String>();
        if (searchKeys.getTrritory() != null && !searchKeys.getTrritory().isEmpty()) {
            
            if (counterforTerritory == 0) {
                query3.append(" INNER JOIN  user_territory t1 ON u1.id=t1.user_id_fk INNER JOIN territory_categories c on c.id=t1.teritory_id  ");
                query4.append(" INNER JOIN  user_territory t1 ON u2.id=t1.user_id_fk INNER JOIN territory_categories c on c.id=t1.teritory_id  ");
                query5.append(" INNER JOIN  user_territory t1 ON u.id=t1.user_id_fk INNER JOIN territory_categories c on c.id=t1.teritory_id  ");
                
                query2.append(" AND ");
                counterforTerritory++;
            }
            List<String> territoryKeys = new ArrayList<String>();
            territoryKeys = searchKeys.getTrritory();
            for (int i = 0; i < territoryKeys.size(); i++) {
                if (i == 0) {
                    query2.append("( c.category_name LIKE ?");
                    keys.add(territoryKeys.get(i));
                } else {
                    query2.append(" OR c.category_name LIKE ?");
                    keys.add(territoryKeys.get(i));
                }
                if(i==(territoryKeys.size()-1)){
                    query2.append(" ) ");
                }
            }
        }
        
        // Ended by Jyoti
        
        
        
        if (searchKeys.getStatus() != null && !searchKeys.getStatus().isEmpty()) {
           
            List<String> status = new ArrayList<String>();
            status = searchKeys.getStatus();
            for (int i = 0; i < status.size(); i++) {
                String type = status.get(i);
                if (type.equals("Active")) {
                    counter++;
                    activeCounter++;
                }
                if (type.equals("Inactive")) {
                    counter++;
                    inactiveCounter++;
                }
            }
        }
            switch (counter) {
                case 0:
                    query.append("SELECT  u.id,account_id_fk,first_name,last_name,email_address,mobile_number,gender,active,emp_code,(SELECT COUNT(DISTINCT u2.id) FROM fieldsense.users u2  ");
                    //query.append(" INNER JOIN  user_territory t1 ON u2.id=t1.user_id_fk INNER JOIN territory_categories c on c.id=t1.teritory_id  ");
                    query.append(query4);
                    query.append(" WHERE (u2.account_id_fk=? AND u2.role!=0 ) ");
                    query.append(query2);
                    query.append(" ) AS usersCount, IFNULL(a.id,0) attendanceId,IFNULL(a.punch_date,'1111-11-11') punchInDate,IFNULL(a.punch_in,0) punchIntime,IFNULL(a.punch_out_date,'1111-11-11') punchOutDate, IFNULL(a.punch_out,0) punchOutTime FROM fieldsense.users as u");
                    query.append(" INNER JOIN (SELECT DISTINCT u1.id FROM fieldsense.users as u1 ");
                    query.append(query3);
                    query.append(" WHERE ( account_id_fk=? AND role!=0 ) ");
                    query.append(query2);
                    break;
                case 1:
                    if (activeCounter == 1) {
                        query.append("SELECT  u.id,account_id_fk,first_name,last_name,email_address,mobile_number,gender,active,emp_code,(SELECT COUNT(DISTINCT u2.id) FROM fieldsense.users u2  ");
                       // query.append(" INNER JOIN  user_territory t1 ON u2.id=t1.user_id_fk INNER JOIN territory_categories c on c.id=t1.teritory_id  ");
                        query.append(query4);
                        query.append(" WHERE u2.active = 1 AND (u2.account_id_fk=? AND u2.role!=0) ");
                        query.append(query2);
                        query.append(" ) AS usersCount, IFNULL(a.id,0) attendanceId,IFNULL(a.punch_date,'1111-11-11') punchInDate,IFNULL(a.punch_in,0) punchIntime,IFNULL(a.punch_out_date,'1111-11-11') punchOutDate, IFNULL(a.punch_out,0) punchOutTime FROM fieldsense.users as u");
                        query.append(" INNER JOIN (SELECT DISTINCT u1.id FROM fieldsense.users as u1 ");
                        query.append(query3);
                        query.append(" WHERE ( account_id_fk=? AND role!=0 AND active = 1 ) ");
                        query.append(query2);
                    }
                    if (inactiveCounter == 1) {
                        query.append("SELECT  u.id,account_id_fk,first_name,last_name,email_address,mobile_number,gender,active,emp_code,(SELECT COUNT(DISTINCT u2.id) FROM fieldsense.users u2  ");
                     //   query.append(" INNER JOIN  user_territory t1 ON u2.id=t1.user_id_fk INNER JOIN territory_categories c on c.id=t1.teritory_id  ");
                        query.append(query4);
                        query.append(" WHERE u2.active = 0 AND (u2.account_id_fk=? AND u2.role!=0) ");
                        query.append(query2);
                        query.append(" ) AS usersCount, IFNULL(a.id,0) attendanceId,IFNULL(a.punch_date,'1111-11-11') punchInDate,IFNULL(a.punch_in,0) punchIntime,IFNULL(a.punch_out_date,'1111-11-11') punchOutDate, IFNULL(a.punch_out,0) punchOutTime FROM fieldsense.users as u");
                        query.append(" INNER JOIN (SELECT DISTINCT u1.id FROM fieldsense.users as u1 ");
                        query.append(query3);
                        query.append(" WHERE ( account_id_fk=? AND role!=0 AND active = 0 )");
                        query.append(query2);
                    }
                    break;
                case 2:
                    query.append("SELECT  u.id,account_id_fk,first_name,last_name,email_address,mobile_number,gender,active,emp_code,(SELECT COUNT(DISTINCT u2.id) FROM fieldsense.users u2  ");
                    //query.append(" INNER JOIN  user_territory t1 ON u2.id=t1.user_id_fk INNER JOIN territory_categories c on c.id=t1.teritory_id  ");
                    query.append(query4);
                    query.append(" WHERE (u2.account_id_fk=? AND u2.role!=0 ) ");
                    query.append(query2);
                    query.append(" ) AS usersCount, IFNULL(a.id,0) attendanceId,IFNULL(a.punch_date,'1111-11-11') punchInDate,IFNULL(a.punch_in,0) punchIntime,IFNULL(a.punch_out_date,'1111-11-11') punchOutDate, IFNULL(a.punch_out,0) punchOutTime FROM fieldsense.users as u");
                    query.append(" INNER JOIN (SELECT DISTINCT u1.id FROM fieldsense.users as u1 ");
                    query.append(query3);
                    query.append(" WHERE ( account_id_fk=? AND role!=0 ) ");
                    query.append(query2);
                    break;
           
       
         }
               
        String sortcolindex=allRequestParams.get("order[0][column]");
        if(sortcolindex==null){
            query.append(" ORDER BY u1.full_name ");
        }
        else if(Integer.parseInt(sortcolindex)==0){
            if(allRequestParams.get("order[0][dir]").equals("asc")){
                query.append(" ORDER BY u1.full_name ");
            }else{
                query.append(" ORDER BY u1.full_name DESC ");
            }
        }else if(Integer.parseInt(sortcolindex)==1){
            if(allRequestParams.get("order[0][dir]").equals("asc")){
                query.append(" ORDER BY u1.email_address ");
            }else{
                query.append(" ORDER BY  u1.email_address DESC ");
            }
        }
        query.append(" LIMIT ? OFFSET ? ) AS my_results USING(id) ");
      //  query.append(" INNER JOIN  user_territory t1 ON u.id=t1.user_id_fk INNER JOIN territory_categories c on c.id=t1.teritory_id  ");
        query.append(query5);
        query.append(query2);
       // query.append(" AND (account_id_fk=? AND role!=0) ");
        query.append(" LEFT OUTER JOIN attendances as a ON u.id=a.user_id_fk AND a.id=(select max(id) from  attendances att where att.user_id_fk=u.id) ");
        query.append(" GROUP BY u.full_name");
        
        //Object param[] = new Object[]{accountId, accountId, length, start};
        
        // Added by Jyoti
        Object param[] = new Object[(3 * (keys.size())) + 4];
        
            int j = 0;

            param[j] = accountId;   // for acc_id ? 1st
            j++;

            for(int i = 0; i < keys.size(); i++) {     // for where clause in query2 -1st 
                param[j] = keys.get(i);
                j++;
            }

            param[j] = accountId;   // for accountid ? 2nd
            j++;

            for (int i = 0; i < keys.size(); i++) {     // for where clause in query2 -2nd
                param[j] = keys.get(i);
                j++;
            }

            param[j] = length;  //for limit ?
            j++;
            param[j] = start;   // for offset ?
            j++;

            for (int i = 0; i < keys.size(); i++) {     // for where clause in query2 -2nd
                param[j] = keys.get(i);
                j++;
            }
           
        //ended by jyoti
        
        log4jLog.info(" searchUserWithFilter " + query);
        try {
//            return jdbcTemplate.query(query.toString(), param, new RowMapper<User>() {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int i) throws SQLException {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setEmp_code(rs.getString("emp_code"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmailAddress(rs.getString("email_address"));
                    user.setMobileNo(rs.getString("mobile_number"));
                    user.setGender(rs.getInt("gender"));
                    user.setActive(rs.getBoolean("active"));
                    user.setUsersCount(rs.getInt("usersCount"));
                    String punchInDate=rs.getString("punchInDate");
                    String punchInTime=rs.getString("punchIntime");
                    String punchOutDate=rs.getString("punchOutDate");
                    String punchOutTime=rs.getString("punchOutTime");
                    
                    if(!punchOutTime.equals("0")){
                        Timestamp punchInTimestamp=Timestamp.valueOf(punchInDate+" "+punchInTime);
                        long currentTime=System.currentTimeMillis();
                        long punchInMills = punchInTimestamp.getTime();
                        long diff=currentTime-punchInMills;
                        diff = (diff/((60)*(1000)));
                        if(diff>((48)*(60))){
                            punchOutTime="0";
                        }
                    }
                   // String tempTime = rs.getString("punchOutTime");
                    String temp = "00:00:00";
                    if (punchOutTime.equals(temp)) {
                        user.setPunchStatusTime(punchInTime);
                        user.setPunchStatus("PunchIn");
                        user.setPunchDate(punchInDate);
                        user.setAttendanceId(rs.getInt("attendanceId"));
                    } else if (punchOutTime.equals("0")) {
                        user.setPunchStatus("NotPunchIn");
                    } else {
                        user.setPunchStatusTime(punchOutTime);
                        user.setPunchStatus("PunchOut");
                        user.setPunchDate(punchOutDate);
                        user.setAttendanceId(rs.getInt("attendanceId"));
                    }
                    return user;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" searchUserWithFilter " + e);
//            e.printStackTrace();
            return new ArrayList<User>();
        }
    }

    /**
     *
     * @param searchText
     * @param accountId
     * @return
     */
    @Override
    public List<Team> searchTeam(String searchText, int accountId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT t.id, t.team_name, t.description, t.user_id_fk owner_id, t.is_active, t.created_on,");
        query.append("t.created_by_id_fk,u.first_name owner_first_name,u.last_name owner_last_name ,count(tm.id) teamMembersCount FROM teams t");
        query.append(" INNER JOIN fieldsense.users u ON t.user_id_fk=u.id");
        query.append(" LEFT JOIN team_members tm ON  t.id=tm.team_id_fk");
        query.append(" WHERE t.team_name LIKE ? ");
        query.append(" GROUP BY tm.team_id_fk");

        Object param[] = new Object[]{"%" + searchText + "%"};
        log4jLog.info(" selectTeam " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Team>() {

                @Override
                public Team mapRow(ResultSet rs, int i) throws SQLException {
                    Team team = new Team();
                    team.setId(rs.getInt("id"));
                    team.setTeamName(rs.getString("team_name"));
                    team.setDescription(rs.getString("description"));
                    team.setIsActive(rs.getInt("is_active"));

                    User user = new User();
                    user.setId(rs.getInt("owner_id"));
                    user.setAccountId(0);
                    user.setFirstName(rs.getString("owner_first_name"));
                    user.setLastName(rs.getString("owner_last_name"));
                    user.setEmailAddress("");
                    user.setPassword("");
                    user.setMobileNo("");
                    user.setGender(0);
                    user.setRole(0);
                    user.setActive(false);
                    user.setLastLoggedOn(new Timestamp(0));
                    user.setLastKnownLocationTime(new Timestamp(0));
                    user.setLastKnownLocation("");
                    user.setCreatedOn(new Timestamp(0));
                    user.setCreatedBy(0);
                    team.setOwnerId(user);

                    User createdBy = new User();
                    createdBy.setId(rs.getInt("created_by_id_fk"));
                    createdBy.setAccountId(0);
                    createdBy.setFirstName("");
                    createdBy.setLastName("");
                    createdBy.setEmailAddress("");
                    createdBy.setPassword("");
                    createdBy.setMobileNo("");
                    createdBy.setGender(0);
                    createdBy.setRole(0);
                    createdBy.setActive(false);
                    createdBy.setLastLoggedOn(new Timestamp(0));
                    createdBy.setLastKnownLocationTime(new Timestamp(0));
                    createdBy.setLastKnownLocation("");
                    createdBy.setCreatedOn(new Timestamp(0));
                    createdBy.setCreatedBy(0);
                    team.setCreatedBy(createdBy);

                    team.setCreatedOn(rs.getTimestamp("created_on"));
                    team.setTeamMembersCount(rs.getInt("teamMembersCount"));
                    return team;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            log4jLog.info(" selectTeam " + e);
            return new ArrayList<Team>();
        } catch (Exception e) {
            log4jLog.info(" selectTeam " + e);
            return new ArrayList<Team>();
        }
    }

    /**
     * 
     * @param searchText
     * @param accountId
     * @param userId
     * @return
     */
    @Override    
    public List<SearchCustomerMobile> searchCustomersForMobile(String searchText, int accountId, int userId) {
//        String query = "SELECT id,customer_name,customer_location_identifier FROM customers WHERE customer_name LIKE ?";
        StringBuilder query = new StringBuilder();
//        query.append("SELECT c.id,c.customer_name,c.customer_printas,c.customer_location_identifier,IFNULL(l.latitude,0) latitude,IFNULL(l.longitude,0) longitude FROM customers c");
//        query.append(" LEFT OUTER JOIN location l ON c.id=l.location_type_id_fk");
//        query.append(" WHERE record_state !=3 AND customer_name LIKE ? OR customer_printas LIKE ?");
        
        // added by jyoti, 17-08-2018
        query.append("SELECT c.id, c.customer_name, c.customer_printas, c.customer_location_identifier, c.territory, IFNULL(l.latitude,0) latitude, IFNULL(l.longitude,0) longitude FROM customers c");
        query.append(" LEFT OUTER JOIN location l ON c.id=l.location_type_id_fk");
        query.append(" INNER JOIN territory_categories tc ON c.territory = tc.category_name INNER JOIN user_territory t1 ON t1.teritory_id=tc.id ");
        query.append(" INNER JOIN fieldsense.users u ON u.id=t1.user_id_fk AND t1.user_id_fk = ? ");
        query.append(" WHERE c.record_state !=3 AND c.customer_name LIKE ? OR c.customer_printas LIKE ? AND c.record_state !=3 ");
        
//        System.out.println("searchCustomersForMobile query > "+query.toString());
//        Object param[] = new Object[]{"%" + searchText + "%","%" + searchText + "%"};
        Object param[] = new Object[]{userId, "%" + searchText + "%","%" + searchText + "%"}; // added by jyoti, 17-08-2018
        log4jLog.info(" searchCustomerForMobile " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<SearchCustomerMobile>() {

                @Override
                public SearchCustomerMobile mapRow(ResultSet rs, int i) throws SQLException {
                    SearchCustomerMobile customerMobile = new SearchCustomerMobile();
                    customerMobile.setId(rs.getInt("id"));
                    customerMobile.setCustomerPrintAs(rs.getString("customer_printas"));  // modified by manohar
                    customerMobile.setCustomerName(rs.getString("customer_name"));
                    customerMobile.setCustomerLocation(rs.getString("customer_location_identifier"));
                    customerMobile.setLatitude(rs.getDouble("latitude"));
                    customerMobile.setLongitude(rs.getDouble("longitude"));

                    return customerMobile;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" searchCustomerForMobile " + e);
            e.printStackTrace();
            return new ArrayList<SearchCustomerMobile>();
        }
    }
    
    /**
     *
     * @param searchText
     * @param offset
     * @param accountId
     * @return
     */
    @Override
    public List<SearchCustomerMobile> searchCustomerForMobileWithOffset(String searchText, int offset,int accountId) {
//        String query = "SELECT id,customer_name,customer_location_identifier FROM customers WHERE customer_name LIKE ?";
        StringBuilder query = new StringBuilder();
        query.append("SELECT c.id,c.customer_name,c.customer_printas,c.customer_location_identifier,IFNULL(l.latitude,0) latitude,IFNULL(l.longitude,0) longitude FROM customers c");
        query.append(" LEFT OUTER JOIN location l ON c.id=l.location_type_id_fk");
        query.append(" WHERE record_state !=3 AND customer_name LIKE ? ORDER BY customer_name  LIMIT 10 OFFSET ?");

        Object param[] = new Object[]{"%" + searchText + "%",offset};
        log4jLog.info(" searchCustomerForMobile " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<SearchCustomerMobile>() {

                @Override
                public SearchCustomerMobile mapRow(ResultSet rs, int i) throws SQLException {
                    SearchCustomerMobile customerMobile = new SearchCustomerMobile();
                    customerMobile.setId(rs.getInt("id"));
                    customerMobile.setCustomerName(rs.getString("customer_name"));
                    customerMobile.setCustomerLocation(rs.getString("customer_location_identifier"));
                    customerMobile.setLatitude(rs.getDouble("latitude"));
                    customerMobile.setLongitude(rs.getDouble("longitude"));
                    customerMobile.setCustomerPrintAs(rs.getString("customer_printas"));
                    return customerMobile;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" searchCustomerForMobile " + e);
            e.printStackTrace();
            return new ArrayList<SearchCustomerMobile>();
        }
    }        

    /**
     *
     * @param activityFreq
     * @param accountList
     * @return
     */
    public List<AccountData> searchAccActivityFreq(int activityFreq,List accountList) {
        try
        {
        ArrayList lowestActivityFreq=new ArrayList();
        ArrayList lowerActivityFreq=new ArrayList();
        ArrayList moderateActivityFreq=new ArrayList();
        ArrayList highActivityFreq=new ArrayList();        
            Iterator i=accountList.iterator();
              while(i.hasNext())
              {
                AccountData accData =(AccountData)i.next();
                int activityFrequency =accData.getActivityFrequency();
                if(activityFrequency <= 10)
                {
                lowestActivityFreq.add(accData);
                }
                else if( activityFrequency > 10 && activityFrequency <= 25)
                {
                   lowerActivityFreq.add(accData);         
                }
                else if( activityFrequency > 25 && activityFrequency <= 50)
                {
                   moderateActivityFreq.add(accData);     
                }
                else if( activityFrequency > 50)
                {
                   highActivityFreq.add(accData);
                }
                }
               if (activityFreq == 1)
               {
                return lowestActivityFreq;
               }
               else if (activityFreq == 2)
               {
                return lowerActivityFreq;
               }
               else if (activityFreq == 3)
               {
                return moderateActivityFreq;
               }
               else if (activityFreq == 4)
               {
                return highActivityFreq;
               }
        }catch (Exception e) {
            log4jLog.info(" searchAccountby Activityfrequency " + e);
            //e.printStackTrace();
            return new ArrayList<AccountData>();
        }
     return new ArrayList<AccountData>();
}
    }
