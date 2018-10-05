/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.appointments.dao;

import com.qlc.fieldsense.activityOutcome.model.ActivityOutcome;
import com.qlc.fieldsense.activityPurpose.model.ActivityPurpose;
import static com.qlc.fieldsense.appointments.dao.AppointmentDaoImpl.log4jLog;
import com.qlc.fieldsense.appointments.model.Appointment;
import com.qlc.fieldsense.appointments.model.Appointments;
import com.qlc.fieldsense.customer.model.Customer;
import com.qlc.fieldsense.customerContact.model.CustomerContacts;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Ramesh
 * @date 21-02-2014
 */
public class AppointmentDaoImpl implements AppointmentDao {

    public static final Logger log4jLog = Logger.getLogger("AppointmentDaoImpl");
    JdbcTemplate jdbcTemplate; // Added by Jyoti
    
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public int createAppointment(Appointment appointment, int accountId) {
//        String query = "INSERT INTO appointments (appointment_time, customer_id_fk, purpose_id_fk, user_id_fk, outcome_id_fk, created_on, created_by_id_fk) VALUES (?, ?, ?, ?, ?,now(), ?)";
        try {
            String query = "INSERT INTO appointments(appointment_title,customer_id_fk,customer_contact_id_fk,user_id_fk,assigned_id_fk,appointment_time,appointment_end_time,appointment_description,appointment_type,purpose_id_fk,outcome,status,check_in_lat,check_in_long,check_in_location,check_in_time,check_out_lat,check_out_long,check_out_location,check_out_time,next_appointment_id,created_on,created_by_id_fk,outcome_description,record_state,updated_on) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,1,now())";
            log4jLog.info(" createAppointment " + query);
            Object param[] = new Object[]{appointment.getTitle(), appointment.getCustomer().getId(), appointment.getCustomerContact().getId(), appointment.getOwner().getId(), appointment.getAssignedTo().getId(), appointment.getDateTime(), appointment.getEndTime(), appointment.getDescription(),
                appointment.getType(), appointment.getPurpose().getId(), appointment.getOutcomes().getId(), appointment.getStatus(), appointment.getCheckInLat(), appointment.getCheckInLong(), appointment.getCheckInLocation(),
                appointment.getCheckInTime(), appointment.getCheckOutLat(), appointment.getCheckOutLong(), appointment.getCheckOutLocation(), appointment.getCheckOutTime(), appointment.getNextAppointment(), appointment.getCreatedBy().getId(), appointment.getOutcomeDescription()};

            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    try {
                        String query1 = "SELECT MAX(id) FROM appointments";
                        return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query1, Integer.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log4jLog.info(" createAppointment " + e);
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info(" createAppointment " + e);
            return 0;
        }
    }

    /*
    Added by jyoti
    */
    @Override
    public int createAppointmentAssignedIdList(Appointment appointment, int accountId, int assignedId) {
        try {
            String query = "INSERT INTO appointments(appointment_title,customer_id_fk,customer_contact_id_fk,user_id_fk,assigned_id_fk,appointment_time,appointment_end_time,appointment_description,appointment_type,purpose_id_fk,outcome,status,check_in_lat,check_in_long,check_in_location,check_in_time,check_out_lat,check_out_long,check_out_location,check_out_time,next_appointment_id,created_on,created_by_id_fk,outcome_description,record_state,updated_on) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,1,now())";
            log4jLog.info(" createAppointmentAssignedIdList " + query);
            Object param[] = new Object[]{appointment.getTitle(), appointment.getCustomer().getId(), appointment.getCustomerContact().getId(), appointment.getOwner().getId(), assignedId, appointment.getDateTime(), appointment.getEndTime(), appointment.getDescription(),
                appointment.getType(), appointment.getPurpose().getId(), appointment.getOutcomes().getId(), appointment.getStatus(), appointment.getCheckInLat(), appointment.getCheckInLong(), appointment.getCheckInLocation(),
                appointment.getCheckInTime(), appointment.getCheckOutLat(), appointment.getCheckOutLong(), appointment.getCheckOutLocation(), appointment.getCheckOutTime(), appointment.getNextAppointment(), appointment.getCreatedBy().getId(), appointment.getOutcomeDescription()};

            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    try {
                        String query1 = "SELECT MAX(id) FROM appointments";
                        return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query1, Integer.class);
                    } catch (Exception e) {
                        log4jLog.info(" createAppointmentAssignedIdList " + e);
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            log4jLog.info(" createAppointmentAssignedIdList " + e);
//            e.printStackTrace();
            return 0;
        }
    }
    
    @Override
    public int createAppointmentMobile(Appointment appointment, int accountId) {
//        String query = "INSERT INTO appointments (appointment_time, customer_id_fk, purpose_id_fk, user_id_fk, outcome_id_fk, created_on, created_by_id_fk) VALUES (?, ?, ?, ?, ?,now(), ?)";
        try {
            String query = "INSERT INTO appointments(appointment_title,customer_id_fk,customer_contact_id_fk,user_id_fk,assigned_id_fk,appointment_time,appointment_end_time,appointment_description,appointment_type,purpose_id_fk,outcome,status,check_in_lat,check_in_long,check_in_location,check_in_time,check_out_lat,check_out_long,check_out_location,check_out_time,next_appointment_id,created_on,created_by_id_fk,record_state) VALUES(?,?,?,?,?,?,?,?,?,?,0,?,?,?,?,?,?,?,?,?,?,now(),?,1)";
            log4jLog.info(" createAppointment " + query);
            Object param[] = new Object[]{appointment.getTitle(), appointment.getCustomer().getId(), appointment.getCustomerContact().getId(), appointment.getOwner().getId(), appointment.getAssignedTo().getId(), appointment.getDateTime(), appointment.getEndTime(), appointment.getDescription(),
                appointment.getType(), appointment.getPurpose().getId(), appointment.getStatus(), appointment.getCheckInLat(), appointment.getCheckInLong(), appointment.getCheckInLocation(),
                appointment.getCheckInTime(), appointment.getCheckOutLat(), appointment.getCheckOutLong(), appointment.getCheckOutLocation(), appointment.getCheckOutTime(), appointment.getNextAppointment(), appointment.getCreatedBy().getId()};

            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    try {
                        String query1 = "SELECT MAX(id) FROM appointments";
                        return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query1, Integer.class);
                    } catch (Exception e) {
                        log4jLog.info(" createAppointment " + e);
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            log4jLog.info(" createAppointment " + e);
            return 0;
        }
    }

    @Override
    public List<Appointment> selectAppointments(String fromDate, String toDate, int userId, int accountId) {
        StringBuilder query = new StringBuilder();

        /* query.append("SELECT customers.id customerId,customers.customer_name,customers.customer_printas,customers.customer_location_identifier,customers.is_headoffice,customers.territory,customers.industry,customers.address1,");
        query.append(" customers.phone1,customers.phone2,customers.phone3,");
        query.append(" customers.fax1,customers.fax2,customers.email1,customers.email2,customers.email3,customers.website1,customers.website2,customers.created_on customerCreatedOn, location.latitude, ");
        query.append(" location.longitude, location.location_type_id_fk, appointments.id appointmentId, appointments.appointment_time,appointments.purpose_id_fk,ap.purpose,appointments.user_id_fk,appointments.outcome_id_fk,");
        query.append(" appointments.created_on appointmentCreatedOn,appointments.created_by_id_fk,appointments_possition.appointment_possition");
        query.append(" FROM customers INNER JOIN location ON customers.id= location.location_type_id_fk ");
        query.append("INNER JOIN appointments ON appointments.customer_id_fk=customers.id INNER JOIN activity_purpose AS ap ON appointments.purpose_id_fk=ap.id");
        query.append(" INNER JOIN appointments_possition ON appointments.id=appointments_possition.appointment_id_fk");
        query.append(" WHERE Date(appointment_time)=?");
        query.append(" ORDER BY appointment_possition ASC");*/

        /*query.append("SELECT a.id,a.appointment_title,a.customer_id_fk,c.customer_name,c.customer_printas,c.address1,l.latitude,l.longitude,a.customer_contact_id_fk,cc.first_name,cc.middle_name,cc.last_name,cc.phone_number1,cc.phone_number2,");
        query.append(" cc.phone_number3,cc.mobile1,cc.mobile2,a.user_id_fk,u.first_name,u.last_name,u.email_address,a.assigned_id_fk,ua.first_name,ua.last_name,ua.email_address,a.appointment_time,a.appointment_end_time,a.appointment_description,");
        query.append(" a.appointment_type,a.purpose_id_fk,p.purpose,a.outcome,a.status,a.check_in_lat,a.check_in_long,a.check_in_location,a.check_in_time,a.check_out_lat,a.check_out_long,a.check_out_location,a.check_out_time,a.next_appointment_id,");
        query.append(" a.created_on,a.created_by_id_fk,appointments_possition.appointment_possition FROM customers as c INNER JOIN location as l ON c.id=l.location_type_id_fk INNER JOIN appointments as a ON a.customer_id_fk=c.id LEFT OUTER JOIN customer_contacts as cc ");
        query.append(" ON a.customer_contact_id_fk=cc.id INNER JOIN fieldsense.users as u ON a.user_id_fk= u.id  INNER JOIN fieldsense.users as ua ON a.assigned_id_fk= ua.id INNER JOIN activity_purpose as p ON a.purpose_id_fk= p.id");
        query.append(" INNER JOIN appointments_possition ON a.id=appointments_possition.appointment_id_fk");
        query.append(" WHERE Date(appointment_time)=?");
        query.append(" ORDER BY appointment_possition ASC");*/

        query.append("SELECT a.id,a.appointment_title,a.customer_id_fk,c.customer_name,c.customer_printas,c.address1,IFNULL(l.latitude,0) latitude,");
        query.append("IFNULL(l.longitude,0) longitude,a.customer_contact_id_fk,cc.first_name,cc.middle_name,cc.last_name,cc.phone_number1,cc.phone_number2,");
        query.append("cc.phone_number3,cc.mobile1,cc.mobile2,a.user_id_fk,u.first_name,u.last_name,u.email_address,a.assigned_id_fk,");
        query.append("ua.first_name,ua.last_name,ua.email_address,a.appointment_time,a.appointment_end_time,a.appointment_description,");
        query.append("a.appointment_type,a.purpose_id_fk,p.purpose,a.outcome,a.status,a.check_in_lat,a.check_in_long,a.check_in_location,");
        query.append("a.check_in_time,a.check_out_lat,a.check_out_long,a.check_out_location,a.check_out_time,a.next_appointment_id,a.created_on,a.created_by_id_fk");
//        query.append(" FROM customers as c INNER JOIN location as l ON c.id=l.location_type_id_fk");
        query.append(" FROM customers as c ");
        query.append(" LEFT OUTER JOIN location as l ON c.id=l.location_type_id_fk");
        query.append(" INNER JOIN appointments as a ON a.customer_id_fk=c.id");
        query.append(" LEFT OUTER JOIN customer_contacts as cc ON a.customer_contact_id_fk=cc.id");
        query.append(" INNER JOIN fieldsense.users as u ON a.user_id_fk= u.id");
        query.append(" INNER JOIN fieldsense.users as ua ON a.assigned_id_fk= ua.id");
        query.append(" INNER JOIN activity_purpose as p ON a.purpose_id_fk= p.id");
        query.append(" WHERE c.record_state !=3  AND (appointment_time BETWEEN ? AND ?) AND (a.user_id_fk = ? || a.assigned_id_fk = ?) AND a.record_state!=3");
        query.append(" ORDER BY appointment_time DESC");
        Object param[] = new Object[]{fromDate, toDate, userId, userId};
        log4jLog.info(" selectAppointments " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Appointment>() {

                @Override
                public Appointment mapRow(ResultSet rs, int i) throws SQLException {
                    Appointment appointment = new Appointment();
                    appointment.setId(rs.getInt("a.id"));
                    appointment.setTitle(rs.getString("appointment_title"));

                    Customer c = new Customer();
                    c.setId(rs.getInt("customer_id_fk"));
                    c.setCustomerName(rs.getString("customer_name"));
                    c.setCustomerPrintas(rs.getString("customer_printas"));
                    c.setCustomerAddress1(rs.getString("address1"));
                    c.setLasknownLatitude(rs.getDouble("latitude"));
                    c.setLasknownLangitude(rs.getDouble("longitude"));
                    appointment.setCustomer(c);

                    CustomerContacts cContact = new CustomerContacts();
                    cContact.setId(rs.getInt("customer_contact_id_fk"));
                    cContact.setFirstName(rs.getString("first_name"));
                    cContact.setMiddleName(rs.getString("middle_name"));
                    cContact.setLastName(rs.getString("last_name"));
                    cContact.setPhone1(rs.getString("phone_number1"));
                    cContact.setPhone2(rs.getString("phone_number2"));
                    cContact.setPhone3(rs.getString("phone_number3"));
                    cContact.setMobile1(rs.getString("mobile1"));
                    cContact.setMobile2(rs.getString("mobile2"));
                    appointment.setCustomerContact(cContact);

                    User owner = new User();
                    owner.setId(rs.getInt("user_id_fk"));
                    owner.setFirstName(rs.getString("u.first_name"));
                    owner.setLastName(rs.getString("u.last_name"));
                    owner.setEmailAddress(rs.getString("u.email_address"));
                    appointment.setOwner(owner);

                    User assignedTo = new User();
                    assignedTo.setId(rs.getInt("assigned_id_fk"));
                    assignedTo.setFirstName(rs.getString("ua.first_name"));
                    assignedTo.setLastName(rs.getString("ua.last_name"));
                    assignedTo.setEmailAddress(rs.getString("ua.email_address"));
                    appointment.setAssignedTo(assignedTo);

                    appointment.setDateTime(rs.getTimestamp("appointment_time"));
                    appointment.setEndTime(rs.getTimestamp("appointment_end_time"));
                    appointment.setDescription(rs.getString("appointment_description"));
                    appointment.setType(rs.getInt("appointment_type"));

                    ActivityPurpose aPurpose = new ActivityPurpose();
                    aPurpose.setId(rs.getInt("purpose_id_fk"));
                    aPurpose.setPurpose(rs.getString("purpose"));
                    appointment.setPurpose(aPurpose);
                    ActivityOutcome out = new ActivityOutcome();
                    out.setId(rs.getInt("outcome"));
                    appointment.setOutcomes(out);
                    appointment.setOutcome(rs.getInt("a.outcome"));
                    appointment.setStatus(rs.getInt("status"));
                    appointment.setCheckInLat(rs.getDouble("check_in_lat"));
                    appointment.setCheckInLong(rs.getDouble("check_in_long"));
                    appointment.setCheckInLocation(rs.getString("check_in_location"));
                    appointment.setCheckInTime(rs.getTimestamp("check_in_time"));
                    appointment.setCheckOutLat(rs.getDouble("check_out_lat"));
                    appointment.setCheckOutLong(rs.getDouble("check_out_long"));
                    appointment.setCheckOutLocation(rs.getString("check_out_location"));
                    appointment.setCheckOutTime(rs.getTimestamp("check_out_time"));
                    appointment.setNextAppointment(rs.getInt("next_appointment_id"));
                    appointment.setCreatedOn(rs.getTimestamp("created_on"));

                    User createdBy = new User();
                    createdBy.setId(rs.getInt("created_by_id_fk"));
                    appointment.setCreatedBy(createdBy);

                    appointment.setAppointmentPosition(0);

                    return appointment;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAppointments " + e);
//            e.printStackTrace();
            return new ArrayList<Appointment>();
        }
    }

    @Override
    public List<Appointment> selectAppointmentsForMobile(String fromDate, String toDate, int userId, int accountId) {
        StringBuilder query = new StringBuilder();

        /* query.append("SELECT customers.id customerId,customers.customer_name,customers.customer_printas,customers.customer_location_identifier,customers.is_headoffice,customers.territory,customers.industry,customers.address1,");
        query.append(" customers.phone1,customers.phone2,customers.phone3,");
        query.append(" customers.fax1,customers.fax2,customers.email1,customers.email2,customers.email3,customers.website1,customers.website2,customers.created_on customerCreatedOn, location.latitude, ");
        query.append(" location.longitude, location.location_type_id_fk, appointments.id appointmentId, appointments.appointment_time,appointments.purpose_id_fk,ap.purpose,appointments.user_id_fk,appointments.outcome_id_fk,");
        query.append(" appointments.created_on appointmentCreatedOn,appointments.created_by_id_fk,appointments_possition.appointment_possition");
        query.append(" FROM customers INNER JOIN location ON customers.id= location.location_type_id_fk ");
        query.append("INNER JOIN appointments ON appointments.customer_id_fk=customers.id INNER JOIN activity_purpose AS ap ON appointments.purpose_id_fk=ap.id");
        query.append(" INNER JOIN appointments_possition ON appointments.id=appointments_possition.appointment_id_fk");
        query.append(" WHERE Date(appointment_time)=?");
        query.append(" ORDER BY appointment_possition ASC");*/

        /*query.append("SELECT a.id,a.appointment_title,a.customer_id_fk,c.customer_name,c.customer_printas,c.address1,l.latitude,l.longitude,a.customer_contact_id_fk,cc.first_name,cc.middle_name,cc.last_name,cc.phone_number1,cc.phone_number2,");
        query.append(" cc.phone_number3,cc.mobile1,cc.mobile2,a.user_id_fk,u.first_name,u.last_name,u.email_address,a.assigned_id_fk,ua.first_name,ua.last_name,ua.email_address,a.appointment_time,a.appointment_end_time,a.appointment_description,");
        query.append(" a.appointment_type,a.purpose_id_fk,p.purpose,a.outcome,a.status,a.check_in_lat,a.check_in_long,a.check_in_location,a.check_in_time,a.check_out_lat,a.check_out_long,a.check_out_location,a.check_out_time,a.next_appointment_id,");
        query.append(" a.created_on,a.created_by_id_fk,appointments_possition.appointment_possition FROM customers as c INNER JOIN location as l ON c.id=l.location_type_id_fk INNER JOIN appointments as a ON a.customer_id_fk=c.id LEFT OUTER JOIN customer_contacts as cc ");
        query.append(" ON a.customer_contact_id_fk=cc.id INNER JOIN fieldsense.users as u ON a.user_id_fk= u.id  INNER JOIN fieldsense.users as ua ON a.assigned_id_fk= ua.id INNER JOIN activity_purpose as p ON a.purpose_id_fk= p.id");
        query.append(" INNER JOIN appointments_possition ON a.id=appointments_possition.appointment_id_fk");
        query.append(" WHERE Date(appointment_time)=?");
        query.append(" ORDER BY appointment_possition ASC");*/

        query.append("SELECT a.id,a.appointment_title,a.customer_id_fk,c.customer_name,c.customer_printas,c.address1,IFNULL(l.latitude,0) latitude,");
        query.append("IFNULL(l.longitude,0) longitude,a.customer_contact_id_fk,cc.first_name,cc.middle_name,cc.last_name,cc.phone_number1,cc.phone_number2,");
        query.append("cc.phone_number3,cc.mobile1,cc.mobile2,a.user_id_fk,u.first_name,u.last_name,u.email_address,a.assigned_id_fk,");
        query.append("ua.first_name,ua.last_name,ua.email_address,a.appointment_time,a.appointment_end_time,a.appointment_description,");
        query.append("a.appointment_type,a.purpose_id_fk,p.purpose,a.outcome,a.status,a.check_in_lat,a.check_in_long,a.check_in_location,");
        query.append("a.check_in_time,a.check_out_lat,a.check_out_long,a.check_out_location,a.check_out_time,a.next_appointment_id,a.created_on,a.created_by_id_fk");
//        query.append(" FROM customers as c INNER JOIN location as l ON c.id=l.location_type_id_fk");
        query.append(" FROM customers as c ");
        query.append(" LEFT OUTER JOIN location as l ON c.id=l.location_type_id_fk");
        query.append(" INNER JOIN appointments as a ON a.customer_id_fk=c.id");
        query.append(" LEFT OUTER JOIN customer_contacts as cc ON a.customer_contact_id_fk=cc.id");
        query.append(" INNER JOIN fieldsense.users as u ON a.user_id_fk= u.id");
        query.append(" INNER JOIN fieldsense.users as ua ON a.assigned_id_fk= ua.id");
        query.append(" INNER JOIN activity_purpose as p ON a.purpose_id_fk= p.id");
        query.append(" WHERE c.record_state !=3 AND (appointment_time BETWEEN ? AND ?) AND (a.assigned_id_fk = ?) AND a.record_state!=3");
        query.append(" ORDER BY appointment_time DESC");
        Object param[] = new Object[]{fromDate, toDate, userId};
        log4jLog.info(" selectAppointments " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Appointment>() {

                @Override
                public Appointment mapRow(ResultSet rs, int i) throws SQLException {
                    Appointment appointment = new Appointment();
                    appointment.setId(rs.getInt("a.id"));
                    appointment.setTitle(rs.getString("appointment_title"));

                    Customer c = new Customer();
                    c.setId(rs.getInt("customer_id_fk"));
                    c.setCustomerName(rs.getString("customer_name"));
                    c.setCustomerPrintas(rs.getString("customer_printas"));
                    c.setCustomerAddress1(rs.getString("address1"));
                    c.setLasknownLatitude(rs.getDouble("latitude"));
                    c.setLasknownLangitude(rs.getDouble("longitude"));
                    appointment.setCustomer(c);

                    CustomerContacts cContact = new CustomerContacts();
                    cContact.setId(rs.getInt("customer_contact_id_fk"));
                    cContact.setFirstName(rs.getString("first_name"));
                    cContact.setMiddleName(rs.getString("middle_name"));
                    cContact.setLastName(rs.getString("last_name"));
                    cContact.setPhone1(rs.getString("phone_number1"));
                    cContact.setPhone2(rs.getString("phone_number2"));
                    cContact.setPhone3(rs.getString("phone_number3"));
                    cContact.setMobile1(rs.getString("mobile1"));
                    cContact.setMobile2(rs.getString("mobile2"));
                    appointment.setCustomerContact(cContact);

                    User owner = new User();
                    owner.setId(rs.getInt("user_id_fk"));
                    owner.setFirstName(rs.getString("u.first_name"));
                    owner.setLastName(rs.getString("u.last_name"));
                    owner.setEmailAddress(rs.getString("u.email_address"));
                    appointment.setOwner(owner);

                    User assignedTo = new User();
                    assignedTo.setId(rs.getInt("assigned_id_fk"));
                    assignedTo.setFirstName(rs.getString("ua.first_name"));
                    assignedTo.setLastName(rs.getString("ua.last_name"));
                    assignedTo.setEmailAddress(rs.getString("ua.email_address"));
                    appointment.setAssignedTo(assignedTo);

                    appointment.setDateTime(rs.getTimestamp("appointment_time"));
                    appointment.setEndTime(rs.getTimestamp("appointment_end_time"));
                    appointment.setDescription(rs.getString("appointment_description"));
                    appointment.setType(rs.getInt("appointment_type"));

                    ActivityPurpose aPurpose = new ActivityPurpose();
                    aPurpose.setId(rs.getInt("purpose_id_fk"));
                    aPurpose.setPurpose(rs.getString("purpose"));
                    appointment.setPurpose(aPurpose);
                    ActivityOutcome out = new ActivityOutcome();
                    out.setId(rs.getInt("outcome"));
                    appointment.setOutcomes(out);
                    appointment.setOutcome(rs.getInt("a.outcome"));
                    appointment.setStatus(rs.getInt("status"));
                    appointment.setCheckInLat(rs.getDouble("check_in_lat"));
                    appointment.setCheckInLong(rs.getDouble("check_in_long"));
                    appointment.setCheckInLocation(rs.getString("check_in_location"));
                    appointment.setCheckInTime(rs.getTimestamp("check_in_time"));
                    appointment.setCheckOutLat(rs.getDouble("check_out_lat"));
                    appointment.setCheckOutLong(rs.getDouble("check_out_long"));
                    appointment.setCheckOutLocation(rs.getString("check_out_location"));
                    appointment.setCheckOutTime(rs.getTimestamp("check_out_time"));
                    appointment.setNextAppointment(rs.getInt("next_appointment_id"));
                    appointment.setCreatedOn(rs.getTimestamp("created_on"));

                    User createdBy = new User();
                    createdBy.setId(rs.getInt("created_by_id_fk"));
                    appointment.setCreatedBy(createdBy);

                    appointment.setAppointmentPosition(0);

                    return appointment;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAppointments " + e);
//            e.printStackTrace();
            return new ArrayList<Appointment>();
        }
    }

    @Override
    public Appointment selectAppointment(int appointmentId, int accountId) {
        StringBuilder query = new StringBuilder();


        /* query.append("SELECT customers.id customerId,customers.customer_name,customers.customer_printas,customers.customer_location_identifier,customers.is_headoffice,customers.territory,customers.industry,customers.address1,");
        query.append("customers.phone1,customers.phone2,customers.phone3,");
        query.append("customers.fax1,customers.fax2,customers.email1,customers.email2,customers.email3,customers.website1,customers.website2,customers.created_on customerCreatedOn, location.latitude, ");
        query.append("location.longitude, location.location_type_id_fk, appointments.id appointmentId, appointments.appointment_time,appointments.purpose_id_fk,ap.purpose,appointments.user_id_fk,appointments.outcome_id_fk,");
        query.append("appointments.created_on appointmentCreatedOn,appointments.created_by_id_fk");
        query.append(" FROM customers INNER JOIN location ON customers.id= location.location_type_id_fk ");
        query.append("INNER JOIN appointments ON appointments.customer_id_fk=customers.id INNER JOIN activity_purpose AS ap ON appointments.purpose_id_fk=ap.id WHERE appointments.id=?");*/
        /*
        query.append("SELECT a.id,a.appointment_title,a.customer_id_fk,c.customer_name,c.customer_printas,c.address1,l.latitude,l.longitude,a.customer_contact_id_fk,cc.first_name,cc.middle_name,cc.last_name,cc.phone_number1,cc.phone_number2,");
        query.append(" cc.phone_number3,cc.mobile1,cc.mobile2,a.user_id_fk,u.first_name,u.last_name,u.email_address,a.assigned_id_fk,ua.first_name,ua.last_name,ua.email_address,a.appointment_time,a.appointment_end_time,a.appointment_description,");
        query.append(" a.appointment_type,a.purpose_id_fk,p.purpose,a.outcome,a.status,a.check_in_lat,a.check_in_long,a.check_in_location,a.check_in_time,a.check_out_lat,a.check_out_long,a.check_out_location,a.check_out_time,a.next_appointment_id,");
        query.append(" a.created_on,a.created_by_id_fk FROM customers as c INNER JOIN location as l ON c.id=l.location_type_id_fk INNER JOIN appointments as a ON a.customer_id_fk=c.id INNER JOIN customer_contacts as cc ");
        query.append(" ON a.customer_contact_id_fk=cc.id INNER JOIN fieldsense.users as u ON a.user_id_fk= u.id  INNER JOIN fieldsense.users as ua ON a.assigned_id_fk= ua.id INNER JOIN activity_purpose as p ON a.purpose_id_fk= p.id WHERE a.id=?");
         */
        /*query.append("SELECT a.id,a.appointment_title,a.customer_id_fk,c.customer_name,c.customer_printas,c.address1,");
        query.append("l.latitude,l.longitude,a.customer_contact_id_fk,cc.first_name,cc.middle_name,cc.last_name,cc.phone_number1,");
        query.append("cc.phone_number2,cc.phone_number3,cc.mobile1,cc.mobile2,a.user_id_fk,u.first_name,u.last_name,");
        query.append("u.email_address,a.assigned_id_fk,ua.first_name,ua.last_name,ua.email_address,a.appointment_time,");
        query.append("a.appointment_end_time,a.appointment_description,a.appointment_type,a.purpose_id_fk,p.purpose,");
        query.append("a.outcome,ac.outcome,a.status,a.check_in_lat,a.check_in_long,a.check_in_location,a.check_in_time,a.check_out_lat,");
        query.append("a.check_out_long,a.check_out_location,a.check_out_time,a.next_appointment_id,a.created_on,a.created_by_id_fk");
        query.append(" FROM customers as c LEFT OUTER JOIN location as l ON c.id=l.location_type_id_fk INNER JOIN appointments as a");
        query.append(" ON a.customer_id_fk=c.id LEFT OUTER JOIN customer_contacts as cc ON a.customer_contact_id_fk=cc.id");
        query.append(" INNER JOIN fieldsense.users as u ON a.user_id_fk= u.id  INNER JOIN fieldsense.users as ua");
        query.append(" ON a.assigned_id_fk= ua.id INNER JOIN activity_purpose as p ON a.purpose_id_fk= p.id INNER JOIN activity_outcomes as ac ON a.outcome=ac.id WHERE a.id=? ");*/

        /* changed on: 26 Nov,2014
        query.append("SELECT a.id,a.appointment_title,a.customer_id_fk,c.customer_name,c.customer_printas,c.address1,");
        query.append("l.latitude,l.longitude,a.customer_contact_id_fk,cc.first_name,cc.middle_name,cc.last_name,cc.phone_number1,");
        query.append("cc.phone_number2,cc.phone_number3,cc.mobile1,cc.mobile2,a.user_id_fk,u.first_name,u.last_name,");
        query.append("u.email_address,a.assigned_id_fk,ua.first_name,ua.last_name,ua.email_address,a.appointment_time,");
        query.append("a.appointment_end_time,a.appointment_description,a.appointment_type,a.purpose_id_fk,p.purpose,");
        query.append("a.outcome,ac.outcome,a.status,a.check_in_lat,a.check_in_long,a.check_in_location,a.check_in_time,a.check_out_lat,");
        query.append("a.check_out_long,a.check_out_location,a.check_out_time,a.next_appointment_id,a.created_on,a.created_by_id_fk");
        query.append(" FROM customers as c LEFT OUTER JOIN location as l ON c.id=l.location_type_id_fk INNER JOIN appointments as a");
        query.append(" ON a.customer_id_fk=c.id LEFT OUTER JOIN customer_contacts as cc ON a.customer_contact_id_fk=cc.id");
        query.append(" INNER JOIN fieldsense.users as u ON a.user_id_fk= u.id  INNER JOIN fieldsense.users as ua");
        query.append(" ON a.assigned_id_fk= ua.id LEFT OUTER JOIN activity_purpose as p ON a.purpose_id_fk= p.id INNER JOIN activity_outcomes as ac ON a.outcome=ac.id WHERE a.id=? ");
        log4jLog.info(" selectAppointment " + query);*/

        query.append("SELECT a.id,a.appointment_title,a.customer_id_fk,c.customer_name,c.customer_printas,c.address1,");
        query.append("l.latitude,l.longitude,a.customer_contact_id_fk,cc.first_name,cc.middle_name,cc.last_name,cc.phone_number1,");
        query.append("cc.phone_number2,cc.phone_number3,cc.mobile1,cc.mobile2,a.user_id_fk,u.first_name,u.last_name,");
        query.append("u.email_address,a.assigned_id_fk,ua.first_name,ua.last_name,ua.email_address,a.appointment_time,");
        query.append("a.appointment_end_time,a.appointment_description,a.appointment_type,a.purpose_id_fk,p.purpose,");
        query.append("a.outcome,ac.outcome,a.status,a.check_in_lat,a.check_in_long,a.check_in_location,a.check_in_time,a.check_out_lat,");
        query.append("a.check_out_long,a.check_out_location,a.check_out_time,a.next_appointment_id,a.created_on,a.created_by_id_fk,IFNULL(a.outcome_description,'') outcome_description");
        query.append(" FROM customers as c LEFT OUTER JOIN location as l ON c.id=l.location_type_id_fk INNER JOIN appointments as a");
        query.append(" ON a.customer_id_fk=c.id LEFT OUTER JOIN customer_contacts as cc ON a.customer_contact_id_fk=cc.id");
        query.append(" INNER JOIN fieldsense.users as u ON a.user_id_fk= u.id   LEFT OUTER JOIN  fieldsense.users as ua");
        query.append(" ON a.assigned_id_fk= ua.id LEFT OUTER JOIN activity_purpose as p ON a.purpose_id_fk= p.id INNER JOIN activity_outcomes as ac ON a.outcome=ac.id WHERE a.id=? AND c.record_state !=3 AND a.record_state!=3 ");
        log4jLog.info(" selectAppointment " + query);
        Object param[] = new Object[]{appointmentId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query.toString(), param, new RowMapper<Appointment>() {

                @Override
                public Appointment mapRow(ResultSet rs, int i) throws SQLException {
                    Appointment appointment = new Appointment();
                    appointment.setId(rs.getInt("a.id"));
                    appointment.setTitle(rs.getString("appointment_title"));

                    Customer c = new Customer();
                    c.setId(rs.getInt("customer_id_fk"));
                    c.setCustomerName(rs.getString("customer_name"));
                    c.setCustomerPrintas(rs.getString("customer_printas"));
                    c.setCustomerAddress1(rs.getString("address1"));
                    c.setLasknownLatitude(rs.getDouble("latitude"));
                    c.setLasknownLangitude(rs.getDouble("longitude"));
                    appointment.setCustomer(c);

                    CustomerContacts cContact = new CustomerContacts();
                    cContact.setId(rs.getInt("customer_contact_id_fk"));
                    cContact.setFirstName(rs.getString("first_name"));
                    cContact.setMiddleName(rs.getString("middle_name"));
                    cContact.setLastName(rs.getString("last_name"));
                    cContact.setPhone1(rs.getString("phone_number1"));
                    cContact.setPhone2(rs.getString("phone_number2"));
                    cContact.setPhone3(rs.getString("phone_number3"));
                    cContact.setMobile1(rs.getString("mobile1"));
                    cContact.setMobile2(rs.getString("mobile2"));
                    appointment.setCustomerContact(cContact);

                    User owner = new User();
                    owner.setId(rs.getInt("user_id_fk"));
                    owner.setFirstName(rs.getString("u.first_name"));
                    owner.setLastName(rs.getString("u.last_name"));
                    owner.setEmailAddress(rs.getString("u.email_address"));
                    appointment.setOwner(owner);

                    User assignedTo = new User();
                    assignedTo.setId(rs.getInt("assigned_id_fk"));
                    assignedTo.setFirstName(rs.getString("ua.first_name"));
                    assignedTo.setLastName(rs.getString("ua.last_name"));
                    assignedTo.setEmailAddress(rs.getString("ua.email_address"));
                    appointment.setAssignedTo(assignedTo);

                    appointment.setDateTime(rs.getTimestamp("appointment_time"));
                    appointment.setSdateTime(appointment.getDateTime().toString());
                    appointment.setEndTime(rs.getTimestamp("appointment_end_time"));
                    appointment.setSendTime(appointment.getEndTime().toString());
                    appointment.setDescription(rs.getString("appointment_description"));
                    appointment.setType(rs.getInt("appointment_type"));

                    ActivityPurpose aPurpose = new ActivityPurpose();
                    aPurpose.setId(rs.getInt("purpose_id_fk"));
                    aPurpose.setPurpose(rs.getString("purpose"));
                    appointment.setPurpose(aPurpose);

                    ActivityOutcome out = new ActivityOutcome();
                    out.setId(rs.getInt("a.outcome"));
                    out.setOutcome(rs.getString("ac.outcome"));
                    appointment.setOutcomes(out);
                    appointment.setOutcome(rs.getInt("a.outcome"));
                    appointment.setStatus(rs.getInt("status"));
                    appointment.setCheckInLat(rs.getDouble("check_in_lat"));
                    appointment.setCheckInLong(rs.getDouble("check_in_long"));
                    appointment.setCheckInLocation(rs.getString("check_in_location"));
                    appointment.setCheckInTime(rs.getTimestamp("check_in_time"));
                    appointment.setCheckOutLat(rs.getDouble("check_out_lat"));
                    appointment.setCheckOutLong(rs.getDouble("check_out_long"));
                    appointment.setCheckOutLocation(rs.getString("check_out_location"));
                    appointment.setCheckOutTime(rs.getTimestamp("check_out_time"));
                    appointment.setNextAppointment(rs.getInt("next_appointment_id"));
                    appointment.setCreatedOn(rs.getTimestamp("created_on"));
                    appointment.setCreatedOnString(rs.getString("created_on"));
                    appointment.setOutcomeDescription(rs.getString("outcome_description"));

                    User createdBy = new User();
                    createdBy.setId(rs.getInt("created_by_id_fk"));
                    appointment.setCreatedBy(createdBy);

                    return appointment;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAppointment " + e);
            e.printStackTrace();
            return new Appointment();
        }
    }

    @Override
    public boolean deleteAppointment(int appointmentId, int accountId) {
        String query = "update appointments set record_state=3 where id=?";
        log4jLog.info(" deleteAppointment " + query);
        Object param[] = new Object[]{appointmentId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" deleteAppointment " + e);
//            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isAppointmentValid(int appointmentId, int accountId) {
        String query = "SELECT COUNT(id) FROM appointments WHERE id=? AND record_state!=3";
        log4jLog.info(" isAppointmentValid " + query);
        Object param[] = new Object[]{appointmentId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isAppointmentValid " + e);
            e.printStackTrace();
            return false;
        }
    }

    
    @Override
    public boolean isAppointmentStatusNotChanged(int appointmentId, int accountId) {
        String query = "SELECT status FROM appointments WHERE id=?";
        log4jLog.info(" isAppointmentValid " + query);
        Object param[] = new Object[]{appointmentId};
        try {
            int status=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
            if(status==0 || status==3){
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isAppointmentValid " + e);
//            e.printStackTrace();
            return false;
        }
    }
    
    
    @Override
    public boolean updateAppointment(Appointment appointment, int accountId) {
//        String query = "UPDATE appointments SET appointment_time=?,customer_id_fk=?,purpose_id_fk=?,user_id_fk=?,outcome_id_fk=? WHERE id=?";
        String query = "UPDATE appointments SET appointment_title=?,customer_id_fk=?,customer_contact_id_fk=?,user_id_fk=?,assigned_id_fk=?,appointment_time=?,appointment_end_time=?,appointment_description=?,appointment_type=?,purpose_id_fk=?,outcome=?,status=?,check_in_lat=?,check_in_long=?,check_in_location=?,check_in_time=?,check_out_lat=?,check_out_long=?,check_out_location=?,check_out_time=?,next_appointment_id=?,record_state=2,updated_on=now() WHERE id=?";

        log4jLog.info(" updateAppointment " + query);
        Object param[] = new Object[]{appointment.getTitle(), appointment.getCustomer().getId(), appointment.getCustomerContact().getId(), appointment.getOwner().getId(), appointment.getAssignedTo().getId(), appointment.getDateTime(), appointment.getEndTime(), appointment.getDescription(),
            appointment.getType(), appointment.getPurpose().getId(), appointment.getOutcomes().getId(), appointment.getStatus(), appointment.getCheckInLat(), appointment.getCheckInLong(), appointment.getCheckInLocation(),
            appointment.getCheckInTime(), appointment.getCheckOutLat(), appointment.getCheckOutLong(), appointment.getCheckOutLocation(), appointment.getCheckOutTime(), appointment.getNextAppointment(), appointment.getId()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateAppointment " + e);
//            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateAppointmentMobile(Appointment appointment, int accountId) {
//        String query = "UPDATE appointments SET appointment_time=?,customer_id_fk=?,purpose_id_fk=?,user_id_fk=?,outcome_id_fk=? WHERE id=?";
        String query = "UPDATE appointments SET appointment_title=?,customer_id_fk=?,customer_contact_id_fk=?,user_id_fk=?,assigned_id_fk=?,appointment_time=?,appointment_end_time=?,appointment_description=?,appointment_type=?,purpose_id_fk=?,outcome=?,status=?,check_in_lat=?,check_in_long=?,check_in_location=?,check_in_time=?,check_out_lat=?,check_out_long=?,check_out_location=?,check_out_time=?,next_appointment_id=?,record_state=2,updated_on=now() WHERE id=?";

        log4jLog.info(" updateAppointment " + query);
        Object param[] = new Object[]{appointment.getTitle(), appointment.getCustomer().getId(), appointment.getCustomerContact().getId(), appointment.getOwner().getId(), appointment.getAssignedTo().getId(), appointment.getDateTime(), appointment.getEndTime(), appointment.getDescription(),
            appointment.getType(), appointment.getPurpose().getId(), appointment.getOutcome(), appointment.getStatus(), appointment.getCheckInLat(), appointment.getCheckInLong(), appointment.getCheckInLocation(),
            appointment.getCheckInTime(), appointment.getCheckOutLat(), appointment.getCheckOutLong(), appointment.getCheckOutLocation(), appointment.getCheckOutTime(), appointment.getNextAppointment(), appointment.getId()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateAppointment " + e);
//            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int getUserMaxAppointmentPossition(int userId, int accountId) {
        String query = "SELECt MAX(appointment_possition) FROM appointments_possition WHERE user_id_fk=?";
        log4jLog.info("getUserMaxAppointmentPossition " + query);
        Object param[] = new Object[]{userId};
        try {
            synchronized (this) {
                return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
            }
        } catch (Exception e) {
            log4jLog.info("getUserMaxAppointmentPossition " + e);
            return 0;
        }
    }

    @Override
    public boolean insertAppointmentPossition(int appointmentId, int userId, int appointmentPossition, int accountId) {
        String query = "INSERT INTO appointments_possition(appointment_id_fk,user_id_fk,appointment_possition,cretaed_on,modified_on) VALUES (?,?,?,now(),now())";
        log4jLog.info(" insertAppointmentPossition " + query);
        Object param[] = new Object[]{appointmentId, userId, appointmentPossition};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info(" insertAppointmentPossition " + e);
            return false;
        }
    }

    @Override
    public boolean updateAppointmentPossition(int appointmentId, int appointmentPossition, int accountId) {
        String query = "UPDATE appointments_possition SET appointment_possition=?,modified_on=now() WHERE appointment_id_fk=?";
        log4jLog.info(" updateAppointmentPossition " + query);
        Object param[] = new Object[]{appointmentPossition, appointmentId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateAppointmentPossition " + e);
            return false;
        }

    }

    @Override
    public boolean deleteAppointmentPossition(int appointmentId, int accountId) {
        String query = "DELETE FROM appointments_possition WHERE appointment_id_fk=?";
        log4jLog.info(" deleteAppointment " + query);
        Object param[] = new Object[]{appointmentId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" deleteAppointment " + e);
//            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateAppointmentOutcomeAndNextAppointment(Appointment appointment, int accountId) {
        String query = "UPDATE appointments SET outcome=?,next_appointment_id=? WHERE id=?";
        log4jLog.info(" updateAppointmentOutcome " + query);
        Object param[] = new Object[]{appointment.getOutcomes().getId(), appointment.getNextAppointment(), appointment.getId()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateAppointmentOutcome " + e);
            return false;
        }
    }

    @Override
    public boolean updateAppointmentOutcomeAndNextAppointmentMobile(Appointment appointment, int accountId) {
        String query = "UPDATE appointments SET outcome=?,next_appointment_id=?,outcome_description=? WHERE id=?";
        log4jLog.info(" updateAppointmentOutcome " + query);
        Object param[] = new Object[]{appointment.getOutcome(), appointment.getNextAppointment(), appointment.getOutcomeDescription(), appointment.getId()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateAppointmentOutcome " + e);
            return false;
        }
    }

    @Override
    public List<Appointment> selectAppointForUser(String fromDate, String toDate, int teamId, int userId, int accountId) {
        StringBuilder query = new StringBuilder();

        /*query.append("SELECT a.id,a.appointment_title,a.customer_id_fk,c.customer_printas,a.customer_contact_id_fk,cc.first_name,cc.middle_name,");
        query.append("cc.last_name,a.appointment_time,a.purpose_id_fk,p.purpose,a.status,a.appointment_description,a.outcome FROM appointments as a INNER JOIN ");
        query.append("customers as c ON a.customer_id_fk=c.id LEFT OUTER JOIN customer_contacts as cc ON ");
        query.append("a.customer_contact_id_fk=cc.id INNER JOIN activity_purpose as p ON a.purpose_id_fk=p.id");
        query.append(" WHERE assigned_id_fk=? ORDER BY a.created_on DESC");*/
        query.append("SELECT a.id,a.appointment_title,a.customer_id_fk,c.customer_printas,c.customer_location_identifier,a.customer_contact_id_fk,cc.first_name,cc.middle_name,");
        query.append("cc.last_name,a.appointment_time,a.purpose_id_fk,p.purpose,a.status,a.appointment_description,a.outcome FROM appointments as a INNER JOIN ");
        query.append("customers as c ON a.customer_id_fk=c.id LEFT OUTER JOIN customer_contacts as cc ON ");
        query.append("a.customer_contact_id_fk=cc.id LEFT OUTER JOIN activity_purpose as p ON a.purpose_id_fk=p.id");
        query.append(" WHERE c.record_state !=3 AND a.record_state!=3 AND (appointment_time BETWEEN ? AND ?) AND assigned_id_fk=? ORDER BY a.created_on DESC");
        log4jLog.info(" selectAppointForUser " + query);
        Object param[] = new Object[]{fromDate, toDate, userId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Appointment>() {

                @Override
                public Appointment mapRow(ResultSet rs, int i) throws SQLException {
                    Appointment appointment = new Appointment();
                    appointment.setId(rs.getInt("a.id"));
                    appointment.setTitle(rs.getString("appointment_title"));

                    Customer c = new Customer();
                    c.setId(rs.getInt("customer_id_fk"));
                    c.setCustomerPrintas(rs.getString("customer_printas"));
                    c.setCustomerLocation(rs.getString("customer_location_identifier"));
                    appointment.setCustomer(c);

                    CustomerContacts cContact = new CustomerContacts();
                    cContact.setId(rs.getInt("customer_contact_id_fk"));
                    cContact.setFirstName(rs.getString("first_name"));
                    cContact.setMiddleName(rs.getString("middle_name"));
                    cContact.setLastName(rs.getString("last_name"));
                    appointment.setCustomerContact(cContact);

                    ActivityPurpose aPurpose = new ActivityPurpose();
                    aPurpose.setId(rs.getInt("purpose_id_fk"));
                    aPurpose.setPurpose(rs.getString("purpose"));
                    appointment.setPurpose(aPurpose);

                    appointment.setDateTime(rs.getTimestamp("appointment_time"));
                    appointment.setSdateTime(appointment.getDateTime().toString());
                    appointment.setStatus(rs.getInt("status"));
                    appointment.setDescription(rs.getString("appointment_description"));

                    ActivityOutcome out = new ActivityOutcome();
                    out.setId(rs.getInt("outcome"));
                    appointment.setOutcomes(out);
                    appointment.setOutcome(rs.getInt("a.outcome"));
                    return appointment;
                }
            });

        } catch (Exception e) {
            log4jLog.info(" selectAppointForUser " + e);
            return new ArrayList<Appointment>();
        }
    }

    @Override
    public List<Appointment> selectAppointmentsPriorToAppointment(int customerId, int appointmentId, int accountId) {
        StringBuilder query = new StringBuilder();
        /*query.append("SELECT a.id,a.appointment_title,a.customer_id_fk,c.customer_name,c.customer_printas,c.address1,l.latitude,");
        query.append("l.longitude,a.customer_contact_id_fk,cc.first_name,cc.middle_name,cc.last_name,cc.phone_number1,cc.phone_number2,");
        query.append("cc.phone_number3,cc.mobile1,cc.mobile2,a.user_id_fk,u.first_name,u.last_name,u.email_address,a.assigned_id_fk,");
        query.append("ua.first_name,ua.last_name,ua.email_address,a.appointment_time,a.appointment_end_time,a.appointment_description,");
        query.append("a.appointment_type,a.purpose_id_fk,p.purpose,a.outcome,a.status,a.check_in_lat,a.check_in_long,a.check_in_location,");
        query.append("a.check_in_time,a.check_out_lat,a.check_out_long,a.check_out_location,a.check_out_time,a.next_appointment_id,a.created_on,a.created_by_id_fk,a1.appointment_time next_appointment_time");
        query.append(" FROM customers as c INNER JOIN location as l ON c.id=l.location_type_id_fk");
        query.append(" INNER JOIN appointments as a ON a.customer_id_fk=c.id");
        query.append(" LEFT OUTER JOIN customer_contacts as cc ON a.customer_contact_id_fk=cc.id");
        query.append(" INNER JOIN fieldsense.users as u ON a.user_id_fk= u.id");
        query.append(" INNER JOIN fieldsense.users as ua ON a.assigned_id_fk= ua.id");
        query.append(" INNER JOIN activity_purpose as p ON a.purpose_id_fk= p.id");
        query.append(" LEFT OUTER JOIN appointments as a1 ON a.next_appointment_id = a1.id");
        query.append(" WHERE a.appointment_time < ? AND a.customer_id_fk = ?");
        query.append(" ORDER BY appointment_time DESC");*/
        query.append("SELECT a.id,a.appointment_title,a.customer_id_fk,c.customer_name,c.customer_printas,c.address1,l.latitude,");
        query.append("l.longitude,a.customer_contact_id_fk,cc.first_name,cc.middle_name,cc.last_name,cc.phone_number1,cc.phone_number2,");
        query.append("cc.phone_number3,cc.mobile1,cc.mobile2,a.user_id_fk,u.first_name,u.last_name,u.email_address,a.assigned_id_fk,");
        query.append("ua.first_name,ua.last_name,ua.email_address,a.appointment_time,a.appointment_end_time,a.appointment_description,");
        query.append("a.appointment_type,a.purpose_id_fk,p.purpose,a.outcome,a.outcome_description,a.status,a.check_in_lat,a.check_in_long,a.check_in_location,");
        query.append("a.check_in_time,a.check_out_lat,a.check_out_long,a.check_out_location,a.check_out_time,a.next_appointment_id,a.created_on,a.created_by_id_fk,");
        query.append("IFNULL(a1.appointment_time,'1111-11-11 11:11:11') next_appointment_time ,a1.appointment_type next_appointment_type");
        query.append(" FROM customers as c INNER JOIN location as l ON c.id=l.location_type_id_fk");
        query.append(" INNER JOIN appointments as a ON a.customer_id_fk=c.id");
        query.append(" LEFT OUTER JOIN customer_contacts as cc ON a.customer_contact_id_fk=cc.id");
        query.append(" INNER JOIN fieldsense.users as u ON a.user_id_fk= u.id");
        query.append(" INNER JOIN fieldsense.users as ua ON a.assigned_id_fk= ua.id");
        query.append(" INNER JOIN activity_purpose as p ON a.purpose_id_fk= p.id");
        query.append(" LEFT OUTER JOIN appointments as a1 ON a.next_appointment_id = a1.id");
        query.append(" WHERE c.record_state !=3 AND a.record_state!=3 AND a.appointment_time < ? AND a.customer_id_fk = ?");
        query.append(" ORDER BY appointment_time DESC");

        Timestamp time = selectAppointmentTime(appointmentId, accountId);
        Object param[] = new Object[]{time, customerId};
        log4jLog.info(" selectAppointmentsPriorToAppointment " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Appointment>() {

                @Override
                public Appointment mapRow(ResultSet rs, int i) throws SQLException {
                    Appointment appointment = new Appointment();
                    appointment.setId(rs.getInt("a.id"));
                    appointment.setTitle(rs.getString("appointment_title"));

                    Customer c = new Customer();
                    c.setId(rs.getInt("customer_id_fk"));
                    c.setCustomerName(rs.getString("customer_name"));
                    c.setCustomerPrintas(rs.getString("customer_printas"));
                    c.setCustomerAddress1(rs.getString("address1"));
                    c.setLasknownLatitude(rs.getDouble("latitude"));
                    c.setLasknownLangitude(rs.getDouble("longitude"));
                    appointment.setCustomer(c);

                    CustomerContacts cContact = new CustomerContacts();
                    cContact.setId(rs.getInt("customer_contact_id_fk"));
                    cContact.setFirstName(rs.getString("first_name"));
                    cContact.setMiddleName(rs.getString("middle_name"));
                    cContact.setLastName(rs.getString("last_name"));
                    cContact.setPhone1(rs.getString("phone_number1"));
                    cContact.setPhone2(rs.getString("phone_number2"));
                    cContact.setPhone3(rs.getString("phone_number3"));
                    cContact.setMobile1(rs.getString("mobile1"));
                    cContact.setMobile2(rs.getString("mobile2"));
                    appointment.setCustomerContact(cContact);

                    User owner = new User();
                    owner.setId(rs.getInt("user_id_fk"));
                    owner.setFirstName(rs.getString("u.first_name"));
                    owner.setLastName(rs.getString("u.last_name"));
                    owner.setEmailAddress(rs.getString("u.email_address"));
                    appointment.setOwner(owner);

                    User assignedTo = new User();
                    assignedTo.setId(rs.getInt("assigned_id_fk"));
                    assignedTo.setFirstName(rs.getString("ua.first_name"));
                    assignedTo.setLastName(rs.getString("ua.last_name"));
                    assignedTo.setEmailAddress(rs.getString("ua.email_address"));
                    appointment.setAssignedTo(assignedTo);

                    appointment.setDateTime(rs.getTimestamp("appointment_time"));
                    appointment.setEndTime(rs.getTimestamp("appointment_end_time"));
                    appointment.setDescription(rs.getString("appointment_description"));
                    appointment.setType(rs.getInt("appointment_type"));

                    ActivityPurpose aPurpose = new ActivityPurpose();
                    aPurpose.setId(rs.getInt("purpose_id_fk"));
                    aPurpose.setPurpose(rs.getString("purpose"));
                    appointment.setPurpose(aPurpose);

                    ActivityOutcome out = new ActivityOutcome();
                    out.setId(rs.getInt("outcome"));
                    appointment.setOutcomes(out);
                    appointment.setOutcome(rs.getInt("a.outcome"));
                    appointment.setOutcomeDescription(rs.getString("a.outcome_description"));
                    appointment.setStatus(rs.getInt("status"));
                    appointment.setCheckInLat(rs.getDouble("check_in_lat"));
                    appointment.setCheckInLong(rs.getDouble("check_in_long"));
                    appointment.setCheckInLocation(rs.getString("check_in_location"));
                    appointment.setCheckInTime(rs.getTimestamp("check_in_time"));
                    appointment.setCheckOutLat(rs.getDouble("check_out_lat"));
                    appointment.setCheckOutLong(rs.getDouble("check_out_long"));
                    appointment.setCheckOutLocation(rs.getString("check_out_location"));
                    appointment.setCheckOutTime(rs.getTimestamp("check_out_time"));
                    appointment.setNextAppointment(rs.getInt("next_appointment_id"));
                    appointment.setCreatedOn(rs.getTimestamp("created_on"));

                    User createdBy = new User();
                    createdBy.setId(rs.getInt("created_by_id_fk"));
                    appointment.setCreatedBy(createdBy);

                    appointment.setAppointmentPosition(0);
                    appointment.setNextAppointmentTime(rs.getTimestamp("next_appointment_time"));
                    appointment.setNextAppointmentType(rs.getInt("next_appointment_type"));
                    return appointment;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAppointmentsPriorToAppointment " + e);
//            e.printStackTrace();
            return new ArrayList<Appointment>();
        }
    }

     @Override
    public List<Appointments> selectAllAppointmentsPriorToAppointment(int customerId, int appointmentId, int accountId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT a.id,a.appointment_title,");
        query.append("a.assigned_id_fk,");
        query.append("ua.first_name,ua.last_name,ua.email_address,a.appointment_time,a.appointment_end_time,a.appointment_description,");
        query.append("a.appointment_type,a.purpose_id_fk,p.purpose,a.outcome,a.status,");
        query.append("a.next_appointment_id,a.created_on,a.check_in_time,a.check_out_time,");
        query.append("IFNULL(a1.appointment_time,'1111-11-11 11:11:11') next_appointment_time ,a1.appointment_type next_appointment_type");
        query.append(" FROM customers as c INNER JOIN location as l ON c.id=l.location_type_id_fk");
        query.append(" INNER JOIN appointments as a ON a.customer_id_fk=c.id");
        query.append(" LEFT OUTER JOIN customer_contacts as cc ON a.customer_contact_id_fk=cc.id");
        query.append(" INNER JOIN fieldsense.users as u ON a.user_id_fk= u.id");
        query.append(" INNER JOIN fieldsense.users as ua ON a.assigned_id_fk= ua.id");
        query.append(" INNER JOIN activity_purpose as p ON a.purpose_id_fk= p.id");
        query.append(" LEFT OUTER JOIN appointments as a1 ON a.next_appointment_id = a1.id");
        query.append(" WHERE c.record_state !=3 AND a.record_state!=3 AND a.appointment_time < ? AND a.customer_id_fk = ?");
        query.append(" ORDER BY appointment_time DESC");

        Timestamp time = selectAppointmentTime(appointmentId, accountId);
        Object param[] = new Object[]{time, customerId};
        log4jLog.info(" selectAppointmentsPriorToAppointment " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Appointments>() {

                @Override
                public Appointments mapRow(ResultSet rs, int i) throws SQLException {
                    Appointments appointment = new Appointments();
                    appointment.setId(rs.getInt("a.id"));
                    appointment.setTitle(rs.getString("appointment_title"));

//                    Customer c = new Customer();
//                    c.setId(rs.getInt("customer_id_fk"));
//                    c.setCustomerName(rs.getString("customer_name"));
//                    c.setCustomerPrintas(rs.getString("customer_printas"));
//                    c.setCustomerAddress1(rs.getString("address1"));
//                    c.setLasknownLatitude(rs.getDouble("latitude"));
//                    c.setLasknownLangitude(rs.getDouble("longitude"));
//                    appointment.setCustomer(c);

//                    CustomerContacts cContact = new CustomerContacts();
//                    cContact.setId(rs.getInt("customer_contact_id_fk"));
//                    cContact.setFirstName(rs.getString("first_name"));
//                    cContact.setMiddleName(rs.getString("middle_name"));
//                    cContact.setLastName(rs.getString("last_name"));
//                    cContact.setPhone1(rs.getString("phone_number1"));
//                    cContact.setPhone2(rs.getString("phone_number2"));
//                    cContact.setPhone3(rs.getString("phone_number3"));
//                    cContact.setMobile1(rs.getString("mobile1"));
//                    cContact.setMobile2(rs.getString("mobile2"));
//                    appointment.setCustomerContact(cContact);

//                    User owner = new User();
//                    owner.setId(rs.getInt("user_id_fk"));
//                    owner.setFirstName(rs.getString("u.first_name"));
//                    owner.setLastName(rs.getString("u.last_name"));
//                    owner.setEmailAddress(rs.getString("u.email_address"));
//                    appointment.setOwner(owner);
//
//                    User assignedTo = new User();
//                    assignedTo.setId(rs.getInt("assigned_id_fk"));
//                    assignedTo.setFirstName(rs.getString("ua.first_name"));
//                    assignedTo.setLastName(rs.getString("ua.last_name"));
//                    assignedTo.setEmailAddress(rs.getString("ua.email_address"));
//                    appointment.setAssignedTo(assignedTo);
                    appointment.setAssignedId(rs.getInt("assigned_id_fk"));
                    appointment.setAssignedUserFirstName(rs.getString("ua.first_name"));
                     appointment.setAssignedUserLastName(rs.getString("ua.last_name"));
                     appointment.setAssignedUserEmailId(rs.getString("ua.email_address"));
                    appointment.setDateTime(rs.getTimestamp("appointment_time"));
                    appointment.setEndTime(rs.getTimestamp("appointment_end_time"));
                    appointment.setDescription(rs.getString("appointment_description"));
                    appointment.setType(rs.getInt("appointment_type"));

//                    ActivityPurpose aPurpose = new ActivityPurpose();
//                    aPurpose.setId(rs.getInt("purpose_id_fk"));
//                    aPurpose.setPurpose(rs.getString("purpose"));
//                    appointment.setPurpose(aPurpose);
                    appointment.setPurposeId(rs.getInt("purpose_id_fk"));
                    appointment.setPurpose(rs.getString("purpose"));

//                    ActivityOutcome out = new ActivityOutcome();
//                    out.setId(rs.getInt("outcome"));
//                    appointment.setOutcomes(out);
//                    appointment.setOutcome(rs.getInt("a.outcome"));
//                    appointment.setStatus(rs.getInt("status"));
//                    appointment.setCheckInLat(rs.getDouble("check_in_lat"));
//                    appointment.setCheckInLong(rs.getDouble("check_in_long"));
//                    appointment.setCheckInLocation(rs.getString("check_in_location"));
                      appointment.setCheckInTime(rs.getTimestamp("check_in_time"));
//                    appointment.setCheckOutLat(rs.getDouble("check_out_lat"));
//                    appointment.setCheckOutLong(rs.getDouble("check_out_long"));
//                    appointment.setCheckOutLocation(rs.getString("check_out_location"));
                      appointment.setCheckOutTime(rs.getTimestamp("check_out_time"));
                    appointment.setNextAppointment(rs.getInt("next_appointment_id"));
                    appointment.setCreatedOn(rs.getTimestamp("created_on"));

//                    User createdBy = new User();
//                    createdBy.setId(rs.getInt("created_by_id_fk"));
//                    appointment.setCreatedBy(createdBy);

                    appointment.setAppointmentPosition(0);
                    appointment.setNextAppointmentTime(rs.getTimestamp("next_appointment_time"));
                    appointment.setNextAppointmentType(rs.getInt("next_appointment_type"));
                    return appointment;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAppointmentsPriorToAppointment " + e);
//           e.printStackTrace();
            return new ArrayList<Appointments>();
        }
    }
    
    
    @Override
    public Timestamp selectAppointmentTime(int appointmentId, int accountId) {
        String query = "SELECT appointment_time FROM appointments WHERE id=?";
        log4jLog.info(" selectAppointmentTime " + query);
        Object param[] = new Object[]{appointmentId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Timestamp.class);
        } catch (Exception e) {
            log4jLog.info(" selectAppointmentTime " + e);
//            e.printStackTrace();
            return new Timestamp(0);
        }
    }

    @Override
    public List<Appointment> selectAppointmentsDateWiseForUser(String date, int teamId, int userId, int accountId) {
        StringBuilder query = new StringBuilder();

        query.append("SELECT a.id,a.appointment_title,a.customer_id_fk,c.customer_name,c.customer_printas,c.address1,l.latitude,");
        query.append("l.longitude,a.customer_contact_id_fk,cc.first_name,cc.middle_name,cc.last_name,cc.phone_number1,cc.phone_number2,");
        query.append("cc.phone_number3,cc.mobile1,cc.mobile2,a.user_id_fk,u.first_name,u.last_name,u.email_address,a.assigned_id_fk,");
        query.append("ua.first_name,ua.last_name,ua.email_address,a.appointment_time,a.appointment_end_time,a.appointment_description,");
        query.append("a.appointment_type,a.purpose_id_fk,p.purpose,a.outcome,a.status,a.check_in_lat,a.check_in_long,a.check_in_location,");
        query.append("a.check_in_time,a.check_out_lat,a.check_out_long,a.check_out_location,a.check_out_time,a.next_appointment_id,a.created_on,a.created_by_id_fk");
        query.append(" FROM customers as c INNER JOIN location as l ON c.id=l.location_type_id_fk");
        query.append(" INNER JOIN appointments as a ON a.customer_id_fk=c.id");
        query.append(" LEFT OUTER JOIN customer_contacts as cc ON a.customer_contact_id_fk=cc.id");
        query.append(" INNER JOIN fieldsense.users as u ON a.user_id_fk= u.id");
        query.append(" INNER JOIN fieldsense.users as ua ON a.assigned_id_fk= ua.id");
        query.append(" INNER JOIN activity_purpose as p ON a.purpose_id_fk= p.id");
        query.append(" WHERE c.record_state !=3 AND Date(appointment_time)=? AND a.user_id_fk IN (SELECT user_id_fk FROM team_members WHERE team_id_fk=?) AND a.assigned_id_fk=? AND a.record_state!=3");
        query.append(" ORDER BY appointment_time DESC");
        Object param[] = new Object[]{date, teamId, userId};
        log4jLog.info(" selectAppointments " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Appointment>() {

                @Override
                public Appointment mapRow(ResultSet rs, int i) throws SQLException {
                    Appointment appointment = new Appointment();
                    appointment.setId(rs.getInt("a.id"));
                    appointment.setTitle(rs.getString("appointment_title"));

                    Customer c = new Customer();
                    c.setId(rs.getInt("customer_id_fk"));
                    c.setCustomerName(rs.getString("customer_name"));
                    c.setCustomerPrintas(rs.getString("customer_printas"));
                    c.setCustomerAddress1(rs.getString("address1"));
                    c.setLasknownLatitude(rs.getDouble("latitude"));
                    c.setLasknownLangitude(rs.getDouble("longitude"));
                    appointment.setCustomer(c);

                    CustomerContacts cContact = new CustomerContacts();
                    cContact.setId(rs.getInt("customer_contact_id_fk"));
                    cContact.setFirstName(rs.getString("first_name"));
                    cContact.setMiddleName(rs.getString("middle_name"));
                    cContact.setLastName(rs.getString("last_name"));
                    cContact.setPhone1(rs.getString("phone_number1"));
                    cContact.setPhone2(rs.getString("phone_number2"));
                    cContact.setPhone3(rs.getString("phone_number3"));
                    cContact.setMobile1(rs.getString("mobile1"));
                    cContact.setMobile2(rs.getString("mobile2"));
                    appointment.setCustomerContact(cContact);

                    User owner = new User();
                    owner.setId(rs.getInt("user_id_fk"));
                    owner.setFirstName(rs.getString("u.first_name"));
                    owner.setLastName(rs.getString("u.last_name"));
                    owner.setEmailAddress(rs.getString("u.email_address"));
                    appointment.setOwner(owner);

                    User assignedTo = new User();
                    assignedTo.setId(rs.getInt("assigned_id_fk"));
                    assignedTo.setFirstName(rs.getString("ua.first_name"));
                    assignedTo.setLastName(rs.getString("ua.last_name"));
                    assignedTo.setEmailAddress(rs.getString("ua.email_address"));
                    appointment.setAssignedTo(assignedTo);

                    appointment.setDateTime(rs.getTimestamp("appointment_time"));
                    appointment.setEndTime(rs.getTimestamp("appointment_end_time"));
                    appointment.setDescription(rs.getString("appointment_description"));
                    appointment.setType(rs.getInt("appointment_type"));

                    ActivityPurpose aPurpose = new ActivityPurpose();
                    aPurpose.setId(rs.getInt("purpose_id_fk"));
                    aPurpose.setPurpose(rs.getString("purpose"));
                    appointment.setPurpose(aPurpose);

                    ActivityOutcome out = new ActivityOutcome();
                    out.setId(rs.getInt("outcome"));
                    appointment.setOutcomes(out);
                    appointment.setOutcome(rs.getInt("a.outcome"));
                    appointment.setStatus(rs.getInt("status"));
                    appointment.setCheckInLat(rs.getDouble("check_in_lat"));
                    appointment.setCheckInLong(rs.getDouble("check_in_long"));
                    appointment.setCheckInLocation(rs.getString("check_in_location"));
                    appointment.setCheckInTime(rs.getTimestamp("check_in_time"));
                    appointment.setCheckOutLat(rs.getDouble("check_out_lat"));
                    appointment.setCheckOutLong(rs.getDouble("check_out_long"));
                    appointment.setCheckOutLocation(rs.getString("check_out_location"));
                    appointment.setCheckOutTime(rs.getTimestamp("check_out_time"));
                    appointment.setNextAppointment(rs.getInt("next_appointment_id"));
                    appointment.setCreatedOn(rs.getTimestamp("created_on"));

                    User createdBy = new User();
                    createdBy.setId(rs.getInt("created_by_id_fk"));
                    appointment.setCreatedBy(createdBy);

                    appointment.setAppointmentPosition(0);

                    return appointment;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAppointments " + e);
//            e.printStackTrace();
            return new ArrayList<Appointment>();
        }
    }

    @Override
    public boolean updateAppointmentTime(int appointmentId, Timestamp appointmentTime,Timestamp appointmentendTime, int accountId) {
        String query = "UPDATE appointments SET appointment_time=?,appointment_end_time=? WHERE id=?";  // modified by manohar
        log4jLog.info(" updateAppointmentTime " + query);
        Object param[] = new Object[]{appointmentTime,appointmentendTime,appointmentId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info(" updateAppointmentTime " + e);
            return false;
        }

    }

    @Override
    public List<Appointment> selectAppointmentsForCustomer(int customerId, int accountId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT a.id,a.appointment_title,a.customer_id_fk,a.customer_contact_id_fk,cc.first_name,cc.middle_name,cc.last_name,");
        query.append(" u.full_name, a.outcome, "); // Added by Jyoti, purpose - to display visitors name on customer edit - > visit tab.
        query.append(" a.user_id_fk,a.assigned_id_fk,a.appointment_time,a.purpose_id_fk,p.purpose,a.status,a.appointment_description FROM appointments as a ");
        query.append(" LEFT OUTER JOIN customer_contacts as cc ON a.customer_contact_id_fk=cc.id INNER JOIN activity_purpose as p");
        query.append(" ON a.purpose_id_fk=p.id ");
        query.append("INNER JOIN fieldsense.users u on u.id=a.assigned_id_fk"); // Added by Jyoti, purpose - to display visitors name on customer edit - > visit tab.
        query.append(" WHERE a.customer_id_fk=? AND a.record_state!=3");
        Object param[] = new Object[]{customerId};
        log4jLog.info(" selectAppointmentsForCustomer " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Appointment>() {

                @Override
                public Appointment mapRow(ResultSet rs, int i) throws SQLException {
                    Appointment appointment = new Appointment();
                    appointment.setId(rs.getInt("a.id"));
                    appointment.setTitle(rs.getString("appointment_title"));
                    CustomerContacts cContact = new CustomerContacts();
                    cContact.setId(rs.getInt("customer_contact_id_fk"));
                    cContact.setFirstName(rs.getString("first_name"));
                    cContact.setMiddleName(rs.getString("middle_name"));
                    cContact.setLastName(rs.getString("last_name"));
                    appointment.setCustomerContact(cContact);
                    appointment.setDateTime(rs.getTimestamp("appointment_time"));
                    appointment.setSdateTime(appointment.getDateTime().toString());
                    ActivityPurpose aPurpose = new ActivityPurpose();
                    aPurpose.setId(rs.getInt("purpose_id_fk"));
                    aPurpose.setPurpose(rs.getString("purpose"));
                    appointment.setPurpose(aPurpose);
                    appointment.setOutcome(rs.getInt("outcome")); // uncommented by jyoti
                    appointment.setStatus(rs.getInt("status"));
                    Customer c = new Customer();
                    c.setId(rs.getInt("customer_id_fk"));
                    appointment.setCustomer(c);
                    appointment.setDescription(rs.getString("appointment_description"));
                    User user = new User(); // Added by Jyoti
                    user.setFullname(rs.getString("full_name")); // Added by Jyoti
                    appointment.setAssignedTo(user); // Added by Jyoti
                    return appointment;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAppointmentsForCustomer " + e);
            return new ArrayList<Appointment>();
        }
    }

    @Override
    public Appointment updateOneAppointment(Appointment appointment, int accountId) {
        String query = "UPDATE appointments SET appointment_title=?,customer_id_fk=?,customer_contact_id_fk=?,user_id_fk=?,assigned_id_fk=?,appointment_time=?,appointment_end_time=?,appointment_description=?,purpose_id_fk=?,outcome=?,status=?,outcome_description=?,updated_on=now(),updated_by_id_fk=?,record_state=2 WHERE id=?";
        log4jLog.info(" updateOneAppointment " + query);
        Object param[] = new Object[]{appointment.getTitle(), appointment.getCustomer().getId(), appointment.getCustomerContact().getId(), appointment.getOwner().getId(), appointment.getAssignedTo().getId(), appointment.getDateTime(), appointment.getEndTime(), appointment.getDescription(),
            appointment.getPurpose().getId(), appointment.getOutcomes().getId(), appointment.getStatus(), appointment.getOutcomeDescription(),appointment.getUpdated_by().getId(), appointment.getId()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return appointment;
            } else {
                return new Appointment();
            }
        } catch (Exception e) {
            log4jLog.info(" updateOneAppointment " + e);
            return new Appointment();
        }
    }

    @Override
    public List<Appointment> selectAppointForUserWithDate(String fromDate, String toDate, int userId, int accountId) {
        StringBuilder query = new StringBuilder();

        /*query.append("SELECT a.id,a.appointment_title,a.customer_id_fk,c.customer_printas,a.customer_contact_id_fk,cc.first_name,cc.middle_name,");
        query.append("cc.last_name,a.appointment_time,a.purpose_id_fk,p.purpose,a.status,a.appointment_description,a.outcome FROM appointments as a INNER JOIN ");
        query.append("customers as c ON a.customer_id_fk=c.id LEFT OUTER JOIN customer_contacts as cc ON ");
        query.append("a.customer_contact_id_fk=cc.id INNER JOIN activity_purpose as p ON a.purpose_id_fk=p.id");
        query.append(" WHERE assigned_id_fk=? ORDER BY a.created_on DESC");*/
        query.append("SELECT a.id,a.appointment_title,a.customer_id_fk,c.customer_printas,a.customer_contact_id_fk,cc.first_name,cc.middle_name,");
        query.append("cc.last_name,a.appointment_time,a.purpose_id_fk,p.purpose,a.status,a.appointment_description,a.outcome FROM appointments as a INNER JOIN ");
        query.append("customers as c ON a.customer_id_fk=c.id LEFT OUTER JOIN customer_contacts as cc ON ");
        query.append("a.customer_contact_id_fk=cc.id LEFT OUTER JOIN activity_purpose as p ON a.purpose_id_fk=p.id");
        query.append(" WHERE (appointment_time BETWEEN ? AND ?) AND assigned_id_fk=? AND a.record_state!=3 ORDER BY a.created_on DESC");
        log4jLog.info(" selectAppointForUser " + query);
        Object param[] = new Object[]{fromDate, toDate, userId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Appointment>() {

                @Override
                public Appointment mapRow(ResultSet rs, int i) throws SQLException {
                    Appointment appointment = new Appointment();
                    appointment.setId(rs.getInt("a.id"));
                    appointment.setTitle(rs.getString("appointment_title"));

                    Customer c = new Customer();
                    c.setId(rs.getInt("customer_id_fk"));
                    c.setCustomerPrintas(rs.getString("customer_printas"));
                    appointment.setCustomer(c);

                    CustomerContacts cContact = new CustomerContacts();
                    cContact.setId(rs.getInt("customer_contact_id_fk"));
                    cContact.setFirstName(rs.getString("first_name"));
                    cContact.setMiddleName(rs.getString("middle_name"));
                    cContact.setLastName(rs.getString("last_name"));
                    appointment.setCustomerContact(cContact);

                    ActivityPurpose aPurpose = new ActivityPurpose();
                    aPurpose.setId(rs.getInt("purpose_id_fk"));
                    aPurpose.setPurpose(rs.getString("purpose"));
                    appointment.setPurpose(aPurpose);

                    appointment.setDateTime(rs.getTimestamp("appointment_time"));
                    appointment.setSdateTime(appointment.getDateTime().toString());
                    appointment.setStatus(rs.getInt("status"));
                    appointment.setDescription(rs.getString("appointment_description"));

                    ActivityOutcome out = new ActivityOutcome();
                    out.setId(rs.getInt("outcome"));
                    appointment.setOutcomes(out);
                    appointment.setOutcome(rs.getInt("a.outcome"));
                    return appointment;
                }
            });

        } catch (Exception e) {
            log4jLog.info(" selectAppointForUser " + e);
            return new ArrayList<Appointment>();
        }
    }

    @Override
    public boolean updateOneAppointmentTimeAndStus(Appointment appointment, int accountId) {
        //String query = "UPDATE appointments SET status=?,appointment_time=?,appointment_end_time=? WHERE id=?";
        
        String query = "UPDATE appointments SET status=?,check_in_time=?,check_out_time=? WHERE id=?";
        log4jLog.info(" updateOneAppointmentTime " + query);
        //Object param[] = new Object[]{appointment.getStatus(), appointment.getDateTime(), appointment.getEndTime(), appointment.getId()};
        Object param[] = new Object[]{appointment.getStatus(), appointment.getCheckInTime(), appointment.getCheckOutTime(), appointment.getId()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info(" updateOneAppointmentTime " + e);
            return false;
        }
    }
    
    @Override
    public boolean isScheduleFreeForCreateAppointment(Appointment appointment, int accountId){
        String query = "SELECT count(id) FROM appointments WHERE assigned_id_fk=? and (status=2 OR status=3 OR record_state=3)";
        log4jLog.info(" isScheduleFreeForAppointment " + query);
        Object param[] = new Object[]{appointment.getAssignedTo().getId()};
        try {
            int count= FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param,Integer.class );
            if(count==0){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" selectAppointmentTime " + e);
//            e.printStackTrace();
            return false;
        }
    }
    
    /*
    added by jyoti
    */
    @Override
    public boolean isScheduleFreeForCreateAppointmentAssignedIdList(Appointment appointment, int accountId, int assignedId){
        String query = "SELECT count(id) FROM appointments WHERE (appointment_end_time >=? AND appointment_time <= ? )  AND assigned_id_fk=? and status !=2 and status !=3 AND record_state !=3";
        log4jLog.info(" isScheduleFreeForCreateAppointmentAssignedIdList " + query);
        Object param[] = new Object[]{appointment.getSdateTime(),appointment.getSendTime(),assignedId};
        try {
            int count= FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param,Integer.class );
            if(count==0){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isScheduleFreeForCreateAppointmentAssignedIdList " + e);
//            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean isScheduleFreeForUpdateAppointment(Appointment appointment, int accountId){
        String query = "SELECT count(id) FROM appointments WHERE id=? and (status=2 OR status=3 OR record_state=3)";
        log4jLog.info(" isScheduleFreeForUpdateAppointment " + query);
        Object param[] = new Object[]{appointment.getId()};
        try {
            int count= FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param,Integer.class );
            if(count==0){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" selectAppointmentTime " + e);
            e.printStackTrace();
            return false;
        }
    }

    public List<Appointment> selectAppointmentsForMobileForMyTeam(String fromDate, String toDate, int userId, int accountId) {
         StringBuilder query = new StringBuilder();
       query.append("SELECT a.id,a.appointment_title,a.customer_id_fk,c.customer_name,c.customer_printas,c.address1,IFNULL(l.latitude,0) latitude,");
        query.append("IFNULL(l.longitude,0) longitude,a.customer_contact_id_fk,cc.first_name,cc.middle_name,cc.last_name,cc.phone_number1,cc.phone_number2,");
        query.append("cc.phone_number3,cc.mobile1,cc.mobile2,a.user_id_fk,u.first_name,u.last_name,u.email_address,a.assigned_id_fk,");
        query.append("ua.first_name,ua.last_name,ua.email_address,a.appointment_time,a.appointment_end_time,a.appointment_description,");
        query.append("a.appointment_type,a.purpose_id_fk,p.purpose,a.outcome,a.status,a.check_in_lat,a.check_in_long,a.check_in_location,");
        query.append("a.check_in_time,a.check_out_lat,a.check_out_long,a.check_out_location,a.check_out_time,a.next_appointment_id,a.created_on,a.created_by_id_fk");
//        query.append(" FROM customers as c INNER JOIN location as l ON c.id=l.location_type_id_fk");
        query.append(" FROM customers as c ");
        query.append(" LEFT OUTER JOIN location as l ON c.id=l.location_type_id_fk");
        query.append(" INNER JOIN appointments as a ON a.customer_id_fk=c.id");
        query.append(" LEFT OUTER JOIN customer_contacts as cc ON a.customer_contact_id_fk=cc.id");
        query.append(" INNER JOIN fieldsense.users as u ON a.user_id_fk= u.id");
        query.append(" INNER JOIN fieldsense.users as ua ON a.assigned_id_fk= ua.id");
        query.append(" INNER JOIN activity_purpose as p ON a.purpose_id_fk= p.id");
        query.append(" WHERE c.record_state !=3 AND (appointment_time BETWEEN ? AND ?) AND (a.assigned_id_fk = ?) AND a.record_state!=3");
        query.append(" ORDER BY appointment_time ASC");
        Object param[] = new Object[]{fromDate, toDate, userId};
        log4jLog.info(" selectAppointments " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Appointment>() {

                @Override
                public Appointment mapRow(ResultSet rs, int i) throws SQLException {
                    Appointment appointment = new Appointment();
                    appointment.setId(rs.getInt("a.id"));
                    appointment.setTitle(rs.getString("appointment_title"));

                    Customer c = new Customer();
                    c.setId(rs.getInt("customer_id_fk"));
                    c.setCustomerName(rs.getString("customer_name"));
                    c.setCustomerPrintas(rs.getString("customer_printas"));
                    c.setCustomerAddress1(rs.getString("address1"));
                    c.setLasknownLatitude(rs.getDouble("latitude"));
                    c.setLasknownLangitude(rs.getDouble("longitude"));
                    appointment.setCustomer(c);

                    CustomerContacts cContact = new CustomerContacts();
                    cContact.setId(rs.getInt("customer_contact_id_fk"));
                    cContact.setFirstName(rs.getString("first_name"));
                    cContact.setMiddleName(rs.getString("middle_name"));
                    cContact.setLastName(rs.getString("last_name"));
                    cContact.setPhone1(rs.getString("phone_number1"));
                    cContact.setPhone2(rs.getString("phone_number2"));
                    cContact.setPhone3(rs.getString("phone_number3"));
                    cContact.setMobile1(rs.getString("mobile1"));
                    cContact.setMobile2(rs.getString("mobile2"));
                    appointment.setCustomerContact(cContact);

                    User owner = new User();
                    owner.setId(rs.getInt("user_id_fk"));
                    owner.setFirstName(rs.getString("u.first_name"));
                    owner.setLastName(rs.getString("u.last_name"));
                    owner.setEmailAddress(rs.getString("u.email_address"));
                    appointment.setOwner(owner);

                    User assignedTo = new User();
                    assignedTo.setId(rs.getInt("assigned_id_fk"));
                    assignedTo.setFirstName(rs.getString("ua.first_name"));
                    assignedTo.setLastName(rs.getString("ua.last_name"));
                    assignedTo.setEmailAddress(rs.getString("ua.email_address"));
                    appointment.setAssignedTo(assignedTo);

                    appointment.setDateTime(rs.getTimestamp("appointment_time"));
                    appointment.setEndTime(rs.getTimestamp("appointment_end_time"));
                    appointment.setDescription(rs.getString("appointment_description"));
                    appointment.setType(rs.getInt("appointment_type"));

                    ActivityPurpose aPurpose = new ActivityPurpose();
                    aPurpose.setId(rs.getInt("purpose_id_fk"));
                    aPurpose.setPurpose(rs.getString("purpose"));
                    appointment.setPurpose(aPurpose);
                    ActivityOutcome out = new ActivityOutcome();
                    out.setId(rs.getInt("outcome"));
                    appointment.setOutcomes(out);
                    appointment.setOutcome(rs.getInt("a.outcome"));
                    appointment.setStatus(rs.getInt("status"));
                    appointment.setCheckInLat(rs.getDouble("check_in_lat"));
                    appointment.setCheckInLong(rs.getDouble("check_in_long"));
                    appointment.setCheckInLocation(rs.getString("check_in_location"));
                    appointment.setCheckInTime(rs.getTimestamp("check_in_time"));
                    appointment.setCheckOutLat(rs.getDouble("check_out_lat"));
                    appointment.setCheckOutLong(rs.getDouble("check_out_long"));
                    appointment.setCheckOutLocation(rs.getString("check_out_location"));
                    appointment.setCheckOutTime(rs.getTimestamp("check_out_time"));
                    appointment.setNextAppointment(rs.getInt("next_appointment_id"));
                    appointment.setCreatedOn(rs.getTimestamp("created_on"));

                    User createdBy = new User();
                    createdBy.setId(rs.getInt("created_by_id_fk"));
                    appointment.setCreatedBy(createdBy);

                    appointment.setAppointmentPosition(0);

                    return appointment;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectAppointments " + e);
//            e.printStackTrace();
            return new ArrayList<Appointment>();
        }
    }

    /**
     * @Added by jyoti
     * @param userId
     * @param customerId
     * @param accountId
     * @return
     */
    @Override
    public List<Appointment> selectAppointForRelevantCustomerOfUser(int userId, int customerId, int accountId) {
        StringBuilder query = new StringBuilder();
        // SELECT a.id, a.appointment_title, p.purpose FROM appointments a INNER JOIN activity_purpose p on a.purpose_id_fk = p.id WHERE a.assigned_id_fk = 16078 AND a.customer_id_fk = 66 AND a.record_state != 3 ORDER BY a.created_on DESC
        query.append(" SELECT a.id, a.appointment_title, a.appointment_time, a.appointment_end_time, a.status,p.purpose FROM appointments a INNER JOIN activity_purpose p ON a.purpose_id_fk = p.id ");
        query.append(" WHERE a.assigned_id_fk = ? AND a.customer_id_fk = ? AND a.record_state != 3 ORDER BY a.appointment_time DESC ");
        log4jLog.info(" selectAppointForRelevantCustomerOfUser for userId :  " + userId);
        Object param[] = new Object[]{userId, customerId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Appointment>() {
                @Override
                public Appointment mapRow(ResultSet rs, int i) throws SQLException {
                    Appointment appointment = new Appointment();
                    appointment.setId(rs.getInt("a.id"));
                    appointment.setTitle(rs.getString("a.appointment_title"));
                    appointment.setDateTime(rs.getTimestamp("a.appointment_time"));
                    appointment.setSdateTime(appointment.getDateTime().toString());
                    appointment.setEndTime(rs.getTimestamp("a.appointment_end_time"));
                    appointment.setSendTime(appointment.getEndTime().toString());
                    appointment.setStatus(rs.getInt("a.status"));
                    ActivityPurpose purpose = new ActivityPurpose();
                    purpose.setPurpose(rs.getString("p.purpose"));
                    appointment.setPurpose(purpose);
                    return appointment;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info(e + " selectAppointForRelevantCustomerOfUser for userId :  " + userId);
            return new ArrayList<>();
        }
    }
    
     public List<java.util.HashMap> selectAllAppointForRelevantCustomerOfUserForGivenDateList(String starttime,String endtime, int assigned_id_fk, int accountId){        
        try {
            String query =null;
         
            query="SELECT c.customer_name, a.appointment_time, a.appointment_end_time,p.purpose, a.assigned_id_fk FROM appointments AS  a INNER JOIN customers AS c ON a.customer_id_fk=c.id INNER JOIN fieldsense.users AS u ON a.user_id_fk= u.id  INNER JOIN activity_purpose AS p ON a.purpose_id_fk= p.id WHERE (a.appointment_end_time >=? AND a.appointment_time <= ?) AND a.assigned_id_fk=? AND  a.status !=3 AND a.record_state !=3";
//            query="SELECT c.customer_name, a.appointment_time, a.appointment_end_time,p.purpose, a.assigned_id_fk FROM appointments a INNER JOIN customers c ON a.customer_id_fk=c.id INNER JOIN fieldsense.users as u ON a.user_id_fk= u.id  INNER JOIN activity_purpose as p ON a.purpose_id_fk= p.id   WHERE (appointment_end_time >=? AND appointment_time <= ? ) AND assigned_id_fk=61 and status !=2 and a.status !=3 AND a.record_state !=3";
//          System.out.println("start="+starttime+" end="+endtime+" assigned="+assigned_id_fk);
//         System.out.println("query="+query);
       
           
               
        log4jLog.info(" selectAppointForRelevantCustomerOfUserForGivenDate1 for userId :  ");
        Object param[] = new Object[]{starttime, endtime,assigned_id_fk};
                return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<java.util.HashMap>(){
                    @Override
                    public java.util.HashMap mapRow(ResultSet rs, int i) throws SQLException {
                        java.util.HashMap hMap = new java.util.HashMap();
                        hMap.put("id", rs.getInt("a.assigned_id_fk"));
                        hMap.put("customerName", rs.getString("c.customer_name"));
                        hMap.put("dateTime", rs.getTimestamp("a.appointment_time"));
                        hMap.put("endTime", rs.getTimestamp("a.appointment_end_time"));
                        hMap.put("purpose", rs.getString("p.purpose"));
                        
                        return hMap;
                    }
                });
            
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info(" getListOfUserIdsForTerritoryID for accountID :  " + accountId);
            return new ArrayList<HashMap>();
        }
    }

    // rework start on 10-april-2018
     
    // start , Recurring visits related dao
    /**
     * @Added by Jyoti 02-02-2017
     * @param repeatType
     * @param startDate
     * @param recurrTillDate
     * @param repeatAfterEvery
     * @param repeatOnDay_weekly
     * @param repeatOnDate_monthly
     * @return
     */
    @Override
    public List<String> createAppointmentListOfRecurringDates(int repeatType, String startDate, String recurrTillDate, int repeatAfterEvery, List<Integer> repeatOnDay_weekly, List<Integer> repeatOnDate_monthly) {

        List<String> repeatListOfDate = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance(Locale.UK);                                                   // Locale.UK - to get weekday start from monday

        switch (repeatType) {
            case 11:	 // Daily
                try {
                    long sDateEpoch = sdf.parse(startDate).getTime() / 1000;
                    long eDateEpoch = sdf.parse(recurrTillDate).getTime() / 1000;

                    while (sDateEpoch <= eDateEpoch) {
                        try {
                            repeatListOfDate.add(startDate);                                            // add date in the list
                            c.setTime(sdf.parse(startDate));                                            // setting calendar date
                            c.add(Calendar.DATE, repeatAfterEvery);                                     // number of days to add
                            startDate = sdf.format(c.getTime());                                        // startDate is now the new date
                            sDateEpoch = sdf.parse(startDate).getTime() / 1000;                         // epoch time of new date                             
                        } catch (Exception e) {
                            log4jLog.info("recurring - daily type appointment id creation : " + e);
                            return new ArrayList<String>();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log4jLog.info("recurring - daily type appointment id creation : " + e);
                    return new ArrayList<String>();
                }
                return repeatListOfDate;

            case 22:   // Weekly
                try {
                    repeatAfterEvery *= 7;    // eg : 2*7 = 14, days will repeat after 2 weeks
                    c.setTime(sdf.parse(startDate));    // setting calendar date to given start date
                    repeatListOfDate.add(startDate);
                    long sDateEpoch = c.getTimeInMillis() / 1000;
                    long eDateEpoch = sdf.parse(recurrTillDate).getTime() / 1000;
                    long sNewEpoch;
                    while (sDateEpoch < eDateEpoch) {
                        try {
                            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);   // setting week from monday
                            int size = repeatOnDay_weekly.size();
                            for (int i = 0; i < size; i++) {
                                c.add(Calendar.DATE, repeatOnDay_weekly.get(i));    // adding repeatOnDay eg. monday=0, tue=1 etc, Date(0)+1=tuesday
                                startDate = sdf.format(c.getTime());  // get new date
                                sNewEpoch = c.getTimeInMillis() / 1000; // epoch time for new date  
                                if (sDateEpoch < sNewEpoch) {
                                    sDateEpoch = sNewEpoch;
                                    if (sDateEpoch <= eDateEpoch) {
                                        repeatListOfDate.add(startDate);
                                    }
                                }
                                c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);   // resetting week from monday.
                            }
                            c.add(Calendar.DATE, repeatAfterEvery); // adding week
                        } catch (Exception e) {
                            log4jLog.info("recurring - weekly type appointment id creation : " + e);
                            return new ArrayList<String>();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log4jLog.info("recurring - weekly type appointment id creation : " + e);
                    return new ArrayList<String>();
                }
                return repeatListOfDate;

            case 33:   // Monthly
                try {
                    long sDateEpoch = sdf.parse(startDate).getTime() / 1000;
                    long eDateEpoch = sdf.parse(recurrTillDate).getTime() / 1000;
                    int monthOfStartDate;
                    int size = repeatOnDate_monthly.size();
                    int yearOfStartDate;
                    int daysInMonth;
                    String mDate = startDate;
                    Date start_Date = sdf.parse(startDate);
                    Date newDate;
                    repeatListOfDate.add(mDate);
                    while (sDateEpoch < eDateEpoch) {
                        try {
                            daysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);                                // gets last date of month 
                            for (int i = 0; i < size; i++) {
                                monthOfStartDate = c.get(Calendar.MONTH);                                           // gets month of given date
                                yearOfStartDate = c.get(Calendar.YEAR);
                                if (repeatOnDate_monthly.get(i) <= daysInMonth) {
                                    c.set(yearOfStartDate, monthOfStartDate, repeatOnDate_monthly.get(i));	// sets list of date monthly
                                    newDate = sdf.parse(sdf.format(c.getTime()));                               // for not letting dates go back from start date
                                    if (newDate.after(start_Date)) {
                                        mDate = sdf.format(c.getTime());                                        // storing newDate into mDate                                                
                                        sDateEpoch = sdf.parse(mDate).getTime() / 1000;                         // epoch time for new date 
                                        if (sDateEpoch <= eDateEpoch) {
                                            repeatListOfDate.add(mDate);                                        // add new date in the list 
                                        }
                                    }
                                } else {
                                    break;
                                }
                            }
                            c.add(Calendar.MONTH, repeatAfterEvery);                                                // to add number of month  
                        } catch (Exception e) {
                            e.printStackTrace();
                            log4jLog.info("recurring - monthly type appointment id creation : " + e);
                            return new ArrayList<String>();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log4jLog.info("recurring - monthly type appointment id creation : " + e);
                    return new ArrayList<String>();
                }
                return repeatListOfDate;
            default:
                break;
        }
        return new ArrayList<String>();
    }
    // Ended by Jyoti 07-02-2017

    /**
     *
     * @param appointment
     * @param accountId
     * @param assignedId
     * @return
     */
    @Override
    public int createAppointmentRecurring(Appointment appointment, int accountId, int assignedId) {
//        System.out.println("createAppointmentRecurring , assignedId : "+assignedId);
        try {
            String query = "INSERT INTO appointments(appointment_title,customer_id_fk,customer_contact_id_fk,user_id_fk,assigned_id_fk,appointment_time,appointment_end_time,appointment_description,appointment_type,purpose_id_fk,outcome,status,check_in_lat,check_in_long,check_in_location,check_in_time,check_out_lat,check_out_long,check_out_location,check_out_time,next_appointment_id,created_on,created_by_id_fk,outcome_description,record_state,updated_on,isRecurring,appointments_recurring_id_fk) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,1,now(),?,?)";
            Object param[] = new Object[]{appointment.getTitle(), appointment.getCustomer().getId(), appointment.getCustomerContact().getId(), appointment.getOwner().getId(), assignedId, appointment.getDateTime(), appointment.getEndTime(), appointment.getDescription(),
                appointment.getType(), appointment.getPurpose().getId(), appointment.getOutcomes().getId(), appointment.getStatus(), appointment.getCheckInLat(), appointment.getCheckInLong(), appointment.getCheckInLocation(),
                appointment.getCheckInTime(), appointment.getCheckOutLat(), appointment.getCheckOutLong(), appointment.getCheckOutLocation(), appointment.getCheckOutTime(), appointment.getNextAppointment(), appointment.getCreatedBy().getId(), appointment.getOutcomeDescription(), appointment.getIsRecurring(), appointment.getAppointmentRecurringList().getId()};
            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    try {
                        String query1 = "SELECT MAX(id) FROM appointments";
                        return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query1, Integer.class);
                    } catch (Exception e) {
                        log4jLog.info(" createAppointmentRecurring " + e);
                        e.printStackTrace();
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            log4jLog.info(" createAppointmentRecurring " + e);
            e.printStackTrace();
            return 0;
        }
    }

        // Added By Jyoti
    /**
     *
     * @param appointment
     * @param accountId
     * @return
     */
    @Override
    public int createAppointmentRecurringList(Appointment appointment, int accountId) {
        try {
            String query = "INSERT INTO appointments_recurring(assigned_id_fk,recurringType,recurringAfterEvery,recurringOnDay_weekly,recurringOnDate_monthly,appointment_start_date_start_time,appointment_start_date_end_time,appointment_end_date_start_time,appointment_end_date_end_time,created_on) VALUES(?,?,?,?,?,?,?,?,?,now())";
            log4jLog.info(" createAppointmentRecurringList " + query);
            Object param[] = new Object[]{appointment.getAssignedTo().getId(), appointment.getAppointmentRecurringList().getRepeatType(),
                appointment.getAppointmentRecurringList().getRepeatAfterEvery(), appointment.getAppointmentRecurringList().getRepeatOnDayWeeklyCSV(),
                appointment.getAppointmentRecurringList().getRepeatOnDateMonthlyCSV(), appointment.getDateTime(), appointment.getEndTime(),
                appointment.getAppointmentRecurringList().getEndDateStartTime(), appointment.getAppointmentRecurringList().getEndDateEndTime()};
            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    try {
                        String query1 = "SELECT MAX(id) FROM appointments_recurring";
                        return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query1, Integer.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log4jLog.info(" createAppointmentRecurringList " + e);
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            log4jLog.info(" createAppointmentRecurringList " + e);
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @Added by Jyoti
     * @param appointmentRecurringId
     * @param appointmentId
     * @param accountId
     * @return
     */
    @Override
    public boolean deleteRecurringAppointment(int appointmentRecurringId, int appointmentId, int accountId) {
        String query = "DELETE FROM appointments WHERE appointments_recurring_id_fk=? AND status=0 OR status=3";
        log4jLog.info(" deleteRecurringAppointment " + query);
        Object param[] = new Object[]{appointmentRecurringId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" deleteRecurringAppointment " + e);
            e.printStackTrace();
            return false;
        }
    }

   // End , recurring visits related dao

   // rework end
    
    @Override
    public int selectAppointment(int userId, String from, String to,int accountId) {
        try{
       String query = "select count(*) from appointments where (status=2 OR status=1) and record_state!=3 and user_id_fk= ? and appointment_time between ? and ? ";
       Object param[] = new Object[]{userId,from,to};
//            System.out.println("UserId "+ userId + from + to);
       return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query,param,Integer.class);
        }catch(Exception e){
        e.printStackTrace();
        return 0;
        }
    }
}
