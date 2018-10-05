/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.notes.dao;

import com.qlc.fieldsense.notes.model.Notes;
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
public class NotesDaoImpl implements NotesDao {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("NotesDaoImpl");

    /**
     *
     * @param notes
     * @param accountId
     * @return
     */
    @Override
    public int insertNotes(Notes notes, int accountId) {
        String query = "INSERT INTO notes(user_id_fk,customer_id_fk,description,created_on) VALUES(?,?,?,now())";
        Object param[] = new Object[]{notes.getUserId().getId(), notes.getCustomerId(), notes.getDescription()};
        try {
            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    return FieldSenseUtils.getMaxIdForTable("notes", accountId);
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return 0;
        }
    }

    /**
     *
     * @param accountId
     * @return
     */
    @Override
    public Notes selectNote(int accountId) {
        try {
            synchronized (this) {
                String query = "SELECT id,user_id_fk,customer_id_fk,description,created_on FROM notes WHERE id=(SELECT MAX(id) FROM notes)";
                return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, new RowMapper<Notes>() {

                    @Override
                    public Notes mapRow(ResultSet rs, int i) throws SQLException {
                        Notes notes = new Notes();
                        notes.setId(rs.getInt("id"));
                        User user = new User();
                        user.setId(rs.getInt("user_id_fk"));
                        notes.setUserId(user);
                        notes.setCustomerId(rs.getInt("customer_id_fk"));
                        notes.setDescription(rs.getString("description"));
                        notes.setCreatedOn(rs.getTimestamp("created_on"));
                        return notes;
                    }
                });
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return new Notes();
        }
    }

    /**
     *
     * @param accountId
     * @return
     */
    @Override
    public List<Notes> selectNotes(int accountId) {
        try {
            String query = "SELECT n.id,user_id_fk,first_name,last_name,customer_id_fk,description,n.created_on FROM notes as n INNER JOIN fieldsense.users as u ON user_id_fk=u.id ORDER BY n.created_on DESC";
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, new RowMapper<Notes>() {

                @Override
                public Notes mapRow(ResultSet rs, int i) throws SQLException {
                    Notes notes = new Notes();
                    notes.setId(rs.getInt("id"));
                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    notes.setUserId(user);
                    notes.setCustomerId(rs.getInt("customer_id_fk"));
                    notes.setDescription(rs.getString("description"));
                    notes.setCreatedOn(rs.getTimestamp("created_on"));
                    return notes;
                }
            });
        } catch (Exception e) {
//            e.printStackTrace();
            return new ArrayList<Notes>();
        }
    }

    /**
     *
     * @param customerId
     * @param accountId
     * @return
     */
    @Override
    public List<Notes> selectNotesOnCustomerId(Integer customerId, int accountId) {
        try {
//            String query = "SELECT id,user_id_fk,first_name,last_name,customer_id_fk,description,created_on FROM notes WHERE customer_id_fk=? ORDER BY created_on DESC";
             String query = "SELECT n.id,user_id_fk,first_name,last_name,customer_id_fk,description,n.created_on FROM notes as n INNER JOIN fieldsense.users as u ON user_id_fk=u.id WHERE customer_id_fk=? ORDER BY n.created_on DESC";
            Object param[] = new Object[]{customerId};
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<Notes>() {

                @Override
                public Notes mapRow(ResultSet rs, int i) throws SQLException {
                    Notes notes = new Notes();
                    notes.setId(rs.getInt("id"));
                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    notes.setUserId(user);
                    notes.setCustomerId(rs.getInt("customer_id_fk"));
                    notes.setDescription(rs.getString("description"));
                    notes.setCreatedOn(rs.getTimestamp("created_on"));
                    return notes;
                }
            });
        } catch (Exception e) {
//            e.printStackTrace();
            return new ArrayList<Notes>();
        }
    }

    /**
     *
     * @param customerId
     * @param accountId
     * @return
     */
    @Override
    public boolean isCustomerIdValid(Integer customerId, int accountId) {
        String query = "SELECT COUNT(id) FROM notes WHERE customer_id_fk=?";
        Object[] param = new Object[]{customerId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    @Override
    public boolean isIdValid(int id, int accountId) {
        String query = "SELECT COUNT(id) FROM notes WHERE id=?";
        Object[] param = new Object[]{id};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param notes
     * @param accountId
     * @return
     */
    @Override
    public Notes updateNotes(Notes notes, int accountId) {
        String query = "UPDATE notes SET description=? WHERE id=?";
        Object param[] = new Object[]{notes.getDescription(), notes.getId()};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                notes = selectNote(notes.getId(), accountId);
                return notes;
            } else {
                return new Notes();
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return new Notes();
        }
    }

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    @Override
    public Notes deleteUserKey(int id, int accountId) {
        Notes notes = new Notes();
        notes = selectNote(id, accountId);
        String query = "DELETE FROM notes WHERE id=?";
        Object param[] = new Object[]{id};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return notes;
            } else {
                return new Notes();
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return new Notes();
        }
    }

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    @Override
    public Notes selectNote(int id, int accountId) {
        try {
//            String query = "SELECT id,user_id_fk,customer_id_fk,description,created_on FROM notes WHERE id=?";
            String query = "SELECT n.id,user_id_fk,first_name,last_name,customer_id_fk,description,n.created_on FROM notes as n INNER JOIN fieldsense.users as u ON u.id=user_id_fk WHERE n.id=?";
            Object param[] = new Object[]{id};
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, new RowMapper<Notes>() {

                @Override
                public Notes mapRow(ResultSet rs, int i) throws SQLException {
                    Notes notes = new Notes();
                    notes.setId(rs.getInt("id"));
                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    notes.setUserId(user);
                    notes.setCustomerId(rs.getInt("customer_id_fk"));
                    notes.setDescription(rs.getString("description"));
                    notes.setCreatedOn(rs.getTimestamp("created_on"));
                    return notes;
                }
            });
        } catch (Exception e) {
//            e.printStackTrace();
            return new Notes();
        }
    }
}
