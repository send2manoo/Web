/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.notes.model;

import com.qlc.fieldsense.notes.dao.NotesDao;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author anuja
 */
public class NotesManager {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("NotesManager");
    NotesDao notesDao = (NotesDao) GetApplicationContext.ac.getBean("notesDaoImpl");
    FieldSenseUtils util = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");

    /**
     * 
     * @param notes
     * @param token
     * @param userToken
     * @return 
     * @purpose used to add notes 
     */
    public Object createNotes(Notes notes, String token) {
        if (util.isTokenValid(token)) {
            //if(util.isSessionExpired(token)){
                int accountId = util.accountIdForToken(token);
                int noteId = notesDao.insertNotes(notes, accountId);
                if (noteId != 0) {
                    notes = notesDao.selectNote(noteId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.NOTES_CREATED, " notes ", notes);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.NOTES_NOT_CREATED, "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /**
     * 
     * @param token
     * @param userToken
     * @return all notes details
     */
    public Object getNotes(String token) {
        if (util.isTokenValid(token)) {
             //if(util.isSessionExpired(token)){
                int accountId = util.accountIdForToken(token);
                Integer userId = util.userIdForToken(token);
                List<Notes> notesList = notesDao.selectNotes(accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "notesList", notesList);
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //} 
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /**
     * 
     * @param customerId
     * @param token
     * @param userToken
     * @return list of notes of particular customer based on cutomerId
     */
    public Object getNotesOnCustomerId(Integer customerId, String token) {
        if (util.isTokenValid(token)) {
            //if(util.isSessionExpired(token)){
                int accountId = util.accountIdForToken(token);
                if (notesDao.isCustomerIdValid(customerId, accountId)) {
                    List<Notes> notesList = notesDao.selectNotesOnCustomerId(customerId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "notesList", notesList);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_CUSTOMERID, "", "");
                }
             // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

    /**
     * 
     * @param notes
     * @param token
     * @param userToken
     * @return 
     * @purpose used to update notes details
     */
    public Object updateNotes(Notes notes, String token) {
        if (util.isTokenValid(token)) {
            //if(util.isSessionExpired(token)){
                int accountId = util.accountIdForToken(token);
                if (notesDao.isIdValid(notes.getId(), accountId)) {
                    notes = notesDao.updateNotes(notes, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.NOTES_UPDATED, "notes", notes);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_ID, "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }

     /**
     * 
     * @param id
     * @param token
     * @param userToken
     * @return 
     * @purpose used to delete note based on note id. 
     */
    public Object deleteNotes(int id, String token) {
        if (util.isTokenValid(token)) {
           /// /if(util.isSessionExpired(token)){
                int accountId = util.accountIdForToken(token);
                if (notesDao.isIdValid(id, accountId)) {
                    Notes notes = notesDao.deleteUserKey(id, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, Constant.NOTES_DELETED, "notes", notes);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_ID, "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, Constant.INVALID_TOKEN, "", "");
        }
    }
}
