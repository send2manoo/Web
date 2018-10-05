/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.accounts.dao;

import com.qlc.fieldsense.accounts.model.AccountSetting;
import com.qlc.fieldsense.accounts.model.AccountSettingValues;
import java.util.List;

/**
 *
 * @author awaneesh
 */
public interface AccountSettingDao {
    
    /**
     *
     * @param accountId
     * @param name
     * @return
     */
    public AccountSetting selectAccountSetting(int accountId,String name);
    
    /**
     *
     * @param accountValues
     * @param accountId
     * @return
     */
    public int editAccountSettingValues(AccountSettingValues accountValues,int accountId);

    /**
     *
     * @param accountValues
     * @param accountId
     * @return
     */
    public int editAccountSettingValuesForOffline(AccountSettingValues accountValues,int accountId); // added by jyoti
     
    /**
     *
     * @param accountId
     * @return
     */
    public List<AccountSetting> selectAllAccountSettings(int accountId);
    
    /**
     *
     * @param key
     * @param accountId
     * @return
     */
    public String getValueFromAccountSetting(String key,int accountId);    

    public int editAccountSettingValuesForInterval(AccountSettingValues accountValues, int accountId);
    
    public int editAccountSettingValuesForCurrencySymbol(AccountSettingValues accountValues, int accountId); // added by jyoti
    
    public int editAccountSettings(int accountId, String value, String name); // Added by Jyoti, 22-12-2017
    
}
