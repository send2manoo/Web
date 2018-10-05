/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.createAccount.dao;

import java.util.List;

/**
 *
 * @author Ramesh
 * @date 09-06-2014
 */
public interface CreateAccountDao {

    /**
     *
     * @param accountId
     * @return
     */
    public boolean createAccountDB(int accountId);

    /**
     *
     * @param accountId
     * @return
     */
    public boolean deleteAccountDBDetailsFromAccounts(int accountId);

    /**
     *
     * @param accountId
     * @return
     */
    public boolean dropAccountDB(int accountId);

    /**
     *
     * @param queryType
     * @return
     */
    public List<String> getCreateAccountQueries(String queryType);

    /**
     *
     * @param queryList
     * @param accountId
     * @return
     */
    public boolean executeCreateAccountQueries(List<String> queryList, int accountId);
}
