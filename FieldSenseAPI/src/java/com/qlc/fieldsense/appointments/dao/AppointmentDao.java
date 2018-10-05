/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.appointments.dao;

import com.qlc.fieldsense.appointments.model.Appointment;
import com.qlc.fieldsense.appointments.model.Appointments;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author Ramesh
 * @date 21-02-2014
 */
public interface AppointmentDao {

    public int createAppointment(Appointment appointment, int accountId);
    
    public int createAppointmentAssignedIdList(Appointment appointment, int accountId, int assignedId); // added by jyoti

    public List<Appointment> selectAppointments(String fromDate, String toDate, int userId, int accountId);

    public List<Appointment> selectAppointmentsForMobile(String fromDate, String toDate, int userId, int accountId);
    
    //Added by siddhesh for MY team in mobile
    public List<Appointment> selectAppointmentsForMobileForMyTeam(String fromDate, String toDate, int userId, int accountId);

    public Appointment selectAppointment(int appointmentId, int accountId);

    public boolean deleteAppointment(int appointmentId, int accountId);

    public boolean isAppointmentValid(int appointmentId, int accountId);
    
    public boolean isAppointmentStatusNotChanged(int appointmentId, int accountId);
    
    public boolean updateAppointment(Appointment appointment, int accountId);

    public int getUserMaxAppointmentPossition(int userId, int accountId);

    public boolean insertAppointmentPossition(int appointmentId, int userId, int appointmentPossition, int accountId);

    public boolean updateAppointmentPossition(int appointmentId, int appointmentPossition, int accountId);

    public boolean deleteAppointmentPossition(int appointmentId, int accountId);

    public boolean updateAppointmentOutcomeAndNextAppointment(Appointment appointment, int accountId);

    public List<Appointment> selectAppointForUser(String fromDate, String toDate, int teamId, int userId, int accountId);

    public List<Appointment> selectAppointmentsPriorToAppointment(int customerId, int appointmentId, int accountId);
    
    public List<Appointments> selectAllAppointmentsPriorToAppointment(int customerId, int appointmentId, int accountId);

    public Timestamp selectAppointmentTime(int appointmentId, int accountId);

    public List<Appointment> selectAppointmentsDateWiseForUser(String date, int teamId, int userId, int accountId);

    public boolean updateAppointmentTime(int appointmentId, Timestamp appointmentTime,Timestamp appointmentendTime, int accountId);  // modified by manohar

    public List<Appointment> selectAppointmentsForCustomer(int customerId, int accountId);

    public Appointment updateOneAppointment(Appointment appointment, int accountId);

    public int createAppointmentMobile(Appointment appointment, int accountId);

    public boolean updateAppointmentMobile(Appointment appointment, int accountId);

    public boolean updateAppointmentOutcomeAndNextAppointmentMobile(Appointment appointment, int accountId);

    public List<Appointment> selectAppointForUserWithDate(String fromDate, String toDate, int userId, int accountId);

    public boolean updateOneAppointmentTimeAndStus(Appointment appointment, int accountId);
    
    public boolean isScheduleFreeForCreateAppointment(Appointment appointment, int accountId);
    
    public boolean isScheduleFreeForCreateAppointmentAssignedIdList(Appointment appointment, int accountId, int assignedId); // added by jyoti
    
    public boolean isScheduleFreeForUpdateAppointment(Appointment appointment, int accountId);
    
    public List<java.util.HashMap> selectAllAppointForRelevantCustomerOfUserForGivenDateList(String starttime,String endtime, int assigned_id_fk, int accountId);   
    /**
     * @Added by jyoti
     * @param userId
     * @param customerId
     * @param accountId
     * @return
     */
    public List<Appointment> selectAppointForRelevantCustomerOfUser(int userId, int customerId, int accountId);
    
    public int selectAppointment(int userId ,String from , String to ,int accountId);

    // start , Added by Jyoti - Dao added for recurring visits
    public int createAppointmentRecurring(Appointment appointment, int accountId, int assignedId);

    public List<String> createAppointmentListOfRecurringDates(int repeatType, String startDate, String recurrTillDate, int repeatAfterEvery, List<Integer> repeatOnDay_weekly, List<Integer> repeatOnDate_monthly);       // Added by Jyoti 02-02-2017

    public int createAppointmentRecurringList(Appointment appointment, int accountId);  // rename table of recurring

    public boolean deleteRecurringAppointment(int appointmentRecurringId, int appointmentId, int accountId);
    // ended , recurring visits related dao
    
}
