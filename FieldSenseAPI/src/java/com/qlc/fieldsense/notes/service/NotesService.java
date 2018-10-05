/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.notes.service;

import com.qlc.fieldsense.notes.model.Notes;
import com.qlc.fieldsense.notes.model.NotesManager;
import javax.validation.Valid;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author anuja
 */
@Controller
@RequestMapping("/notes")
public class NotesService {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("NotesService");
    NotesManager notesManager = new NotesManager();

    /**
     * 
     * @param notes
     * @param userToken
     * @return 
     * @purpose used to add notes 
     */
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody
    Object createNotes(@Valid @RequestBody Notes notes, @RequestHeader(value = "userToken") String userToken) {
        return notesManager.createNotes(notes, userToken);
    }
    
    /**
     * 
     * @param userToken
     * @return all notes details
     */
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Object selectNotes(@RequestHeader(value = "userToken") String userToken) {
        return notesManager.getNotes(userToken);
    }
    
    /**
     * 
     * @param customerId
     * @param userToken
     * @return list of notes of particular customer based on cutomerId
     */
    @RequestMapping(value="/{customerId}",method = RequestMethod.GET)
    public @ResponseBody
    Object selectNotesOnCustomerId(@PathVariable Integer customerId,@RequestHeader(value = "userToken") String userToken) {
        return notesManager.getNotesOnCustomerId(customerId, userToken);
    }
    
    /**
     * 
     * @param notes
     * @param userToken
     * @return 
     * @purpose used to update notes details
     */
     @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public @ResponseBody
    Object updateNotes(@RequestBody Notes notes, @RequestHeader(value = "userToken") String userToken) {
        return notesManager.updateNotes(notes, userToken);
    }
    
    /**
     * 
     * @param id
     * @param userToken
     * @return 
     * @purpose used to delete note based on note id. 
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object deleteNotes(@PathVariable int id, @RequestHeader(value = "userToken") String userToken) {
        return notesManager.deleteNotes(id, userToken);
    }
}
