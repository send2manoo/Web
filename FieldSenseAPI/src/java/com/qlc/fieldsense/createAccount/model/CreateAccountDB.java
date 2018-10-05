/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.createAccount.model;

import com.qlc.fieldsense.createAccount.dao.CreateAccountDao;
import com.qlc.fieldsense.utils.CommunityDataSourceManager;
import com.qlc.fieldsense.utils.GetApplicationContext;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * @purpose to create account database
 * @author Ramesh
 * @date 09-06-2014
 */
public class CreateAccountDB {

    CreateAccountDao createAccountDao = (CreateAccountDao) GetApplicationContext.ac.getBean("createAccountDaoImpl");
    private static final Logger log4jLog = Logger.getLogger("CreateAccountDB");

    /**
     *
     * @param accountId
     * @return
     */
    public boolean createAccountDatabase(int accountId) {
        if (createAccountDao.createAccountDB(accountId)) {
            boolean isCreated = false;
            log4jLog.info("Database is created");
            for (int i = 0; i < 4; i++) {
                String statementType = "";
                switch (i) {
                    case 0:
                        statementType = "CREATE";
                        break;
                    case 1:
                        statementType = "ALTER";
                        break;
                    case 2:
                        statementType = "INSERT";
                        break;
                    case 3:
                        statementType = "UPDATE";
                        break;
                    default:
                        break;
                }
                List<String> sqlQueriesList = createAccountDao.getCreateAccountQueries(statementType);
                if (!(sqlQueriesList == null || sqlQueriesList.isEmpty())) {
                    if (createAccountDao.executeCreateAccountQueries(sqlQueriesList, accountId)) {
                        isCreated = true;
//                        System.out.println("isCreated = true");
                    } else {
                        isCreated = false;
//                        System.out.println("isCreated = false");
                        break;
                    }
                }
            }
            if (isCreated) {
                log4jLog.info("New Account DB is created ");
                return true;
            } else {
                dropAccountDatabase(accountId);
                return false;
            }
        } else {
            createAccountDao.deleteAccountDBDetailsFromAccounts(accountId);
        }
        return false;
    }

    /**
     *
     * @param accountId
     * @return
     */
    public boolean doAccountActivation(int accountId) {
        log4jLog.info("Doing account activation!");
        if (createAccountDatabase(accountId)) {
            ((CommunityDataSourceManager) GetApplicationContext.ac.getBean("communityDataSourceManager")).setDataSource(accountId);
            return true;
        }
        return false;
    }

    /**
     *
     * @param accountId
     */
    public void dropAccountDatabase(int accountId) {
        createAccountDao.dropAccountDB(accountId);
        createAccountDao.deleteAccountDBDetailsFromAccounts(accountId);
    }
}
