/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.activity.dao;

import com.qlc.fieldsense.activity.model.Activity;
import com.qlc.fieldsense.activityPurpose.model.ActivityPurpose;
import com.qlc.fieldsense.customer.model.Customer;
import com.qlc.fieldsense.customerContact.model.CustomerContacts;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author anuja
 */
public class ActivityDaoImpl implements ActivityDao {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("ActivityDaoImpl");

    /**
     *
     * @param teamId
     * @param accountId
     * @return
     */
    @Override
    public List<Activity> selectActivity(int teamId, int accountId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT ac.id,activity,customer_id_fk,customer_name,address1,customer_contact_id_fk,owner_id_fk,assigned_id_fk,");
        query.append(" u.first_name,u.last_name,u.email_address,activity_date_time,description,purpose_id_fk,purpose,outcome_id_fk,status,ac.created_by_id_fk,u1.first_name,u1.last_name,u1.email_address,ac.created_on");
        query.append(" FROM activities as ac INNER JOIN customers as c ON ac.customer_id_fk = c.id");
        query.append(" INNER JOIN fieldsense.users u ON ac.assigned_id_fk= u.id INNER JOIN fieldsense.users u1 ON ac.created_by_id_fk= u1.id  INNER JOIN activity_purpose ON ac.purpose_id_fk=activity_purpose.id WHERE owner_id_fk = (SELECT user_id_fk FROM account_1.teams WHERE id=?)");
        query.append(" ORDER BY ac.created_on DESC");
        log4jLog.info(" selectActivity " + query);
        Object param[] = new Object[]{teamId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Activity>() {

                @Override
                public Activity mapRow(ResultSet rs, int i) throws SQLException {
                    Activity activity = new Activity();
                    activity.setId(rs.getInt("id"));
                    activity.setActivity(rs.getString("activity"));
                    Customer customer = new Customer();
                    customer.setId(rs.getInt("customer_id_fk"));
                    customer.setCustomerName(rs.getString("customer_name"));
                    customer.setCustomerAddress1(rs.getString("address1"));
                    activity.setCustomerId(customer);

                    CustomerContacts custcont = new CustomerContacts();
                    custcont.setId(rs.getInt("customer_contact_id_fk"));
                    activity.setCustomerContactId(custcont);

                    User owner = new User();
                    owner.setId(rs.getInt("owner_id_fk"));
                    activity.setOwnerId(owner);

                    User assigned = new User();
                    assigned.setId(rs.getInt("assigned_id_fk"));
                    assigned.setFirstName(rs.getString("first_name"));
                    assigned.setLastName(rs.getString("last_name"));
                    assigned.setEmailAddress(rs.getString("email_address"));
                    activity.setAssignedId(assigned);

                    activity.setActivityDateTime(rs.getTimestamp("activity_date_time"));
                    activity.setDescription(rs.getString("description"));

                    ActivityPurpose aPurpose = new ActivityPurpose();
                    aPurpose.setId(rs.getInt("purpose_id_fk"));
                    aPurpose.setPurpose(rs.getString("purpose"));
                    activity.setPurposeId(aPurpose);
                    activity.setOutComeId(rs.getInt("outcome_id_fk"));
                    activity.setStatus(rs.getInt("status"));

                    User createdBy = new User();
                    createdBy.setId(rs.getInt("created_by_id_fk"));
                    createdBy.setFirstName(rs.getString("u1.first_name"));
                    createdBy.setLastName(rs.getString("u1.last_name"));
                    createdBy.setEmailAddress(rs.getString("u1.email_address"));
                    activity.setCreatedBy(createdBy);

                    activity.setCreatedOn(rs.getTimestamp("created_on"));

                    return activity;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectActivity " + e);
//            e.printStackTrace();
            return new ArrayList<Activity>();
        }
    }

    /**
     *
     * @param teamId
     * @param userId
     * @param accountId
     * @return
     */
    @Override
    public List<Activity> selectActivityForUser(int teamId, int userId, int accountId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT ac.id,activity,customer_id_fk,customer_name,address1,customer_contact_id_fk,owner_id_fk,assigned_id_fk,");
        query.append(" u.first_name,u.last_name,u.email_address,activity_date_time,description,purpose_id_fk,purpose,outcome_id_fk,status,");
        query.append(" ac.created_by_id_fk,ac.created_on FROM activities as ac INNER JOIN customers as c ON");
        query.append(" ac.customer_id_fk = c.id INNER JOIN fieldsense.users u ON ac.assigned_id_fk= u.id INNER JOIN activity_purpose ON ac.purpose_id_fk=activity_purpose.id WHERE owner_id_fk = ");
        query.append(" (SELECT user_id_fk FROM account_1.teams WHERE id=?) AND assigned_id_fk=?");
        query.append(" ORDER BY ac.created_on DESC");
        log4jLog.info(" selectActivityForUser " + query);
        Object param[] = new Object[]{teamId, userId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Activity>() {

                @Override
                public Activity mapRow(ResultSet rs, int i) throws SQLException {
                    Activity activity = new Activity();
                    activity.setId(rs.getInt("id"));
                    activity.setActivity(rs.getString("activity"));
                    Customer customer = new Customer();
                    customer.setId(rs.getInt("customer_id_fk"));
                    customer.setCustomerName(rs.getString("customer_name"));
                    customer.setCustomerAddress1(rs.getString("address1"));
                    activity.setCustomerId(customer);

                    CustomerContacts custcont = new CustomerContacts();
                    custcont.setId(rs.getInt("customer_contact_id_fk"));
                    activity.setCustomerContactId(custcont);

                    User owner = new User();
                    owner.setId(rs.getInt("owner_id_fk"));
                    activity.setOwnerId(owner);

                    User assigned = new User();
                    assigned.setId(rs.getInt("assigned_id_fk"));
                    assigned.setFirstName(rs.getString("first_name"));
                    assigned.setLastName(rs.getString("last_name"));
                    assigned.setEmailAddress(rs.getString("email_address"));
                    activity.setAssignedId(assigned);

                    activity.setActivityDateTime(rs.getTimestamp("activity_date_time"));
                    activity.setDescription(rs.getString("description"));
                    ActivityPurpose aPurpose = new ActivityPurpose();
                    aPurpose.setId(rs.getInt("purpose_id_fk"));
                    aPurpose.setPurpose(rs.getString("purpose"));
                    activity.setPurposeId(aPurpose);
                    activity.setOutComeId(rs.getInt("outcome_id_fk"));
                    activity.setStatus(rs.getInt("status"));

                    User createdBy = new User();
                    createdBy.setId(rs.getInt("created_by_id_fk"));
                    activity.setCreatedBy(createdBy);

                    activity.setCreatedOn(rs.getTimestamp("created_on"));

                    return activity;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectActivity " + e);
//            e.printStackTrace();
            return new ArrayList<Activity>();
        }

    }

    /**
     * @author Ramesh
     * @param cutomerId
     * @param accountId
     * @return 
     * @date 09-05-2014
     * @purpose This method is used to select all activities of customer
     */
    @Override
    public List<Activity> selectActivityForCustomer(int cutomerId, int accountId) {
        StringBuilder query = new StringBuilder();

        query.append("SELECT ac.id,ac.activity,ac.customer_id_fk,c.customer_name,c.address1,ac.customer_contact_id_fk,ac.owner_id_fk,ac.assigned_id_fk,");
        query.append("ac.activity_date_time,ac.description,ac.purpose_id_fk,activity_purpose.purpose,ac.outcome_id_fk,ac.status,");
        query.append("ac.created_by_id_fk,ac.created_on,customer_contacts.first_name customerContactFirstName,");
        query.append("customer_contacts.middle_name customerContactMiddleName,customer_contacts.last_name customerContactLastName,");
        query.append("u.first_name activityCreatorFirstName,u.last_name activityCreatorLastName FROM activities as ac INNER JOIN customers as c ON ");
        query.append("ac.customer_id_fk = c.id INNER JOIN activity_purpose ON ac.purpose_id_fk=activity_purpose.id ");
        query.append("INNER JOIN customer_contacts ON ac.customer_contact_id_fk =customer_contacts.id ");
        query.append("INNER JOIN fieldsense.users u ON ac.created_by_id_fk =u.id ");
        query.append("WHERE ac.customer_id_fk=?");
        query.append(" ORDER BY ac.created_on DESC");
        Object param[] = new Object[]{cutomerId};
        log4jLog.info(" selectActivityForUser " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Activity>() {

                @Override
                public Activity mapRow(ResultSet rs, int i) throws SQLException {
                    Activity activity = new Activity();
                    activity.setId(rs.getInt("id"));
                    activity.setActivity(rs.getString("activity"));
                    Customer customer = new Customer();
                    customer.setId(rs.getInt("customer_id_fk"));
                    customer.setCustomerName(rs.getString("customer_name"));
                    customer.setCustomerAddress1(rs.getString("address1"));
                    activity.setCustomerId(customer);

                    CustomerContacts custcont = new CustomerContacts();
                    custcont.setId(rs.getInt("customer_contact_id_fk"));
                    custcont.setFirstName(rs.getString("customerContactFirstName"));
                    custcont.setMiddleName(rs.getString("customerContactMiddleName"));
                    custcont.setLastName(rs.getString("customerContactLastName"));
                    activity.setCustomerContactId(custcont);

                    User owner = new User();
                    owner.setId(rs.getInt("owner_id_fk"));
                    activity.setOwnerId(owner);

                    User assigned = new User();
                    assigned.setId(rs.getInt("assigned_id_fk"));
//                    assigned.setFirstName(rs.getString("first_name"));
//                    assigned.setLastName(rs.getString("last_name"));
//                    assigned.setEmailAddress(rs.getString("email_address"));
                    activity.setAssignedId(assigned);

                    activity.setActivityDateTime(rs.getTimestamp("activity_date_time"));
                    activity.setDescription(rs.getString("description"));
                    ActivityPurpose aPurpose = new ActivityPurpose();
                    aPurpose.setId(rs.getInt("purpose_id_fk"));
                    aPurpose.setPurpose(rs.getString("purpose"));
                    activity.setPurposeId(aPurpose);
                    activity.setOutComeId(rs.getInt("outcome_id_fk"));
                    activity.setStatus(rs.getInt("status"));

                    User createdBy = new User();
                    createdBy.setId(rs.getInt("created_by_id_fk"));
                    createdBy.setFirstName(rs.getString("activityCreatorFirstName"));
                    createdBy.setLastName(rs.getString("activityCreatorLastName"));
                    activity.setCreatedBy(createdBy);

                    activity.setCreatedOn(rs.getTimestamp("created_on"));

                    return activity;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectActivity " + e);
//            e.printStackTrace();
            return new ArrayList<Activity>();
        }

    }

    /**
     *
     * @param activity
     * @param accountId
     * @return
     */
    @Override
    public int creatActivity(Activity activity, int accountId) {
        String query = "INSERT INTO activities(activity,customer_id_fk,customer_contact_id_fk,owner_id_fk,assigned_id_fk,activity_date_time,description,purpose_id_fk,outcome_id_fk,status,created_by_id_fk,created_on) VALUES(?,?,?,?,?,?,?,?,?,?,?,now())";
        log4jLog.info("creatActivity " + query);
        Object[] param = new Object[]{activity.getActivity(), activity.getCustomerId().getId(), activity.getCustomerContactId().getId(),
            activity.getOwnerId().getId(), activity.getAssignedId().getId(), activity.getActivityDateTime(), activity.getDescription(),
            activity.getPurposeId().getId(), activity.getOutComeId(), activity.getStatus(), activity.getCreatedBy().getId()};
        try {
            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    try {
                        String query1 = "SELECT MAX(id) FROM activities";
                        return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query1, Integer.class);
                    } catch (Exception e) {
                        log4jLog.info("creatActivity " + e);
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            log4jLog.info("creatActivity" + e);
            return 0;
        }
    }

    /**
     *
     * @param activityId
     * @param accountId
     * @return
     */
    @Override
    public Activity selectOneActivity(int activityId, int accountId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT ac.id,activity,customer_id_fk,customer_name,address1,customer_contact_id_fk,owner_id_fk,assigned_id_fk,");
        query.append(" u.first_name,u.last_name,u.email_address,activity_date_time,description,purpose_id_fk,purpose,outcome_id_fk,status,");
        query.append(" ac.created_by_id_fk,ac.created_on FROM account_1.activities as ac INNER JOIN account_1.customers as c ON");
        query.append(" ac.customer_id_fk = c.id INNER JOIN fieldsense.users u ON ac.assigned_id_fk= u.id INNER JOIN account_1.activity_purpose");
        query.append(" ON ac.purpose_id_fk=activity_purpose.id WHERE ac.id =?");
        log4jLog.info(" selectActivity " + query);
        Object param[] = new Object[]{activityId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query.toString(), param, new RowMapper<Activity>() {

                @Override
                public Activity mapRow(ResultSet rs, int i) throws SQLException {
                    Activity activity = new Activity();
                    activity.setId(rs.getInt("id"));
                    activity.setActivity(rs.getString("activity"));
                    Customer customer = new Customer();
                    customer.setId(rs.getInt("customer_id_fk"));
                    customer.setCustomerName(rs.getString("customer_name"));
                    customer.setCustomerAddress1(rs.getString("address1"));
                    activity.setCustomerId(customer);

                    CustomerContacts custcont = new CustomerContacts();
                    custcont.setId(rs.getInt("customer_contact_id_fk"));
                    activity.setCustomerContactId(custcont);

                    User owner = new User();
                    owner.setId(rs.getInt("owner_id_fk"));
                    activity.setOwnerId(owner);

                    User assigned = new User();
                    assigned.setId(rs.getInt("assigned_id_fk"));
                    assigned.setFirstName(rs.getString("first_name"));
                    assigned.setLastName(rs.getString("last_name"));
                    assigned.setEmailAddress(rs.getString("email_address"));
                    activity.setAssignedId(assigned);

                    activity.setActivityDateTime(rs.getTimestamp("activity_date_time"));
                    activity.setDescription(rs.getString("description"));

                    ActivityPurpose aPurpose = new ActivityPurpose();
                    aPurpose.setId(rs.getInt("purpose_id_fk"));
                    aPurpose.setPurpose(rs.getString("purpose"));
                    activity.setPurposeId(aPurpose);
                    activity.setOutComeId(rs.getInt("outcome_id_fk"));
                    activity.setStatus(rs.getInt("status"));

                    User createdBy = new User();
                    createdBy.setId(rs.getInt("created_by_id_fk"));
                    activity.setCreatedBy(createdBy);

                    activity.setCreatedOn(rs.getTimestamp("created_on"));

                    return activity;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectActivity " + e);
//            e.printStackTrace();
            return new Activity();
        }

    }
}
