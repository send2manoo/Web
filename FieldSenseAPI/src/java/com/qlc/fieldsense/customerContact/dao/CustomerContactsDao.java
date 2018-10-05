/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customerContact.dao;

import com.qlc.fieldsense.customerContact.model.CustomerContacts;
import java.util.List;

/**
 *
 * @author anuja
 */
public interface CustomerContactsDao {

    /**
     *
     * @param customerCont
     * @param accountId
     * @return
     */
    public int insertCustomerContacts(CustomerContacts customerCont, int accountId);

    /**
     *
     * @param accountId
     * @return
     */
    public CustomerContacts selectCustomerContact(int accountId);

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    public CustomerContacts selectCustomerContact(int id, int accountId);

    /**
     *
     * @param accountId
     * @return
     */
    public List<CustomerContacts> selectCustomerContacts(int accountId);

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    public CustomerContacts deleteCustomerContacts(int id, int accountId);

    /**
     *
     * @param customerId
     * @param accountId
     * @return
     */
    public List<CustomerContacts> deleteCustomerContactOnCustomerId(int customerId, int accountId);

    /**
     *
     * @param customerCont
     * @param accountId
     * @return
     */
    public CustomerContacts updateCustomerContacts(CustomerContacts customerCont, int accountId);

    /**
     *
     * @param customerCont
     * @param communityId
     * @return
     */
    public CustomerContacts updateCustomerContactDetails(CustomerContacts customerCont, int communityId);

    /**
     *
     * @param customerId
     * @param accountId
     * @return
     */
    public List<CustomerContacts> selectCustomerContactOnCustomerId(int customerId, int accountId);

//    public CustomerContacts updateCustomerContactsOnCustomerId(CustomerContacts customerCont, int accountId);

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
        public boolean isCustomerContactValid(int id, int accountId);

    /**
     *
     * @param customerId
     * @param accountId
     * @return
     */
    public boolean isContactCustomerIdValid(int customerId, int accountId);

    /**
     *
     * @param id
     * @param communityId
     * @return
     */
    public CustomerContacts selectCustomerContactForImport(int id, int communityId);

    /**
     *
     * @param firstNm
     * @param middleNm
     * @param lastNm
     * @param customerId
     * @param accountId
     * @return
     */
    public boolean isCustomerContactExist(String firstNm, String middleNm, String lastNm,int customerId,int accountId);
}
