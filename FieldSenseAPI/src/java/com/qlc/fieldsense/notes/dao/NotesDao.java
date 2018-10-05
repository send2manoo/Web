/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.notes.dao;

import com.qlc.fieldsense.notes.model.Notes;
import java.util.List;

/**
 *
 * @author anuja
 */
public interface NotesDao {

    /**
     *
     * @param notes
     * @param accountId
     * @return
     */
    public int insertNotes(Notes notes, int accountId);

    /**
     *
     * @param accountId
     * @return
     */
    public Notes selectNote(int accountId);

    /**
     *
     * @param accountId
     * @return
     */
    public List<Notes> selectNotes(int accountId);

    /**
     *
     * @param customerId
     * @param accountId
     * @return
     */
    public List<Notes> selectNotesOnCustomerId(Integer customerId, int accountId);

    /**
     *
     * @param customerId
     * @param accountId
     * @return
     */
    public boolean isCustomerIdValid(Integer customerId, int accountId);

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    public boolean isIdValid(int id, int accountId);

    /**
     *
     * @param notes
     * @param accountId
     * @return
     */
    public Notes updateNotes(Notes notes, int accountId);

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    public Notes deleteUserKey(int id, int accountId);

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    public Notes selectNote(int id, int accountId);
}
