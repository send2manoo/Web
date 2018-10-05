/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.formbuilder.dao;

import com.qlc.fieldsense.addressConverter.AddressConverter;
import com.qlc.fieldsense.addressConverter.GoogleResponse;
import com.qlc.fieldsense.formbuilder.model.FilledFields;
import com.qlc.fieldsense.formbuilder.model.FilledForm;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.qlc.fieldsense.utils.PhotoToIconCreator;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author pallavi.s
 */
public class CustomFrmRprtImpl implements CustomFrmRprtDao {

    public static final Logger log4jLog = Logger.getLogger(" CustomFrmRprtImpl ");

    /**
     *
     * @param formdetails
     * @param accountId
     * @return
     */
    @Override
    public int insertFormData(FilledForm formdetails, int accountId) {
//        String query = "INSERT INTO form_fill_details_table (user_id,submitted_on,form_id_fk) VALUES (?,now(),?)"; // commented by jyoti
        String query = "INSERT INTO form_fill_details_table (user_id,submitted_on,form_id_fk) VALUES (?,?,?)"; // added by jyoti, #30046 need to set submitted_on value given by mobile instead of now()
        //log4jLog.info(" insertFilledData " + query);
        Timestamp defaultTimestamp = new Timestamp(0);
//        System.out.println("insertFormData 1 submittedOn >> " + formdetails.getSubmitTime());
        Object param[] = new Object[]{formdetails.getUserid(), formdetails.getSubmitTime(), formdetails.getFormid()};
        if (formdetails.getSubmitTime().equals(defaultTimestamp)) {
            query = "INSERT INTO form_fill_details_table (user_id,submitted_on,form_id_fk) VALUES (?,NOW(),?)";
            param = new Object[]{formdetails.getUserid(), formdetails.getFormid()};
//            System.out.println("insertFormData inside if submitted on not set by mobile");
        }

        try {
            synchronized (this) {
                int value = FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param);
                if (value > 0) {
                    return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject("select max(id) from form_fill_details_table", Integer.class);
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            log4jLog.info(" insertFormData " + e);
            e.printStackTrace();
            return 0;

        }
    }

    /**
     *
     * @param formid
     * @param userId
     * @param fromDate
     * @param ToDate
     * @param accountId
     * @return
     */
    public List<FilledForm> getFormData(int formid, int userId, Timestamp fromDate, Timestamp ToDate, int accountId) {
        StringBuilder query1 = new StringBuilder();
        query1.append("SELECT id,user_id,submitted_on from form_fill_details_table where form_id_fk=? and user_id=? and submitted_on between ? and ? order by submitted_on desc");
        //log4jLog.info(" updateCustomer " + query1.toString());  
        Object param[] = new Object[]{formid, userId, fromDate, ToDate};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query1.toString(), param, new RowMapper<FilledForm>() {
                public FilledForm mapRow(ResultSet rs, int i) throws SQLException {
                    FilledForm filledform = new FilledForm();
                    filledform.setId((Integer) (rs.getInt("id")));
                    filledform.setUserid((Integer) (rs.getInt("user_id")));
                    filledform.setSubmitTime(rs.getTimestamp("submitted_on"));
                    return filledform;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" getFormData " + e);
//            e.printStackTrace();
            return new ArrayList<FilledForm>();
        }

    }

    /**
     *
     * @param formid
     * @param fromDate
     * @param ToDate
     * @param accountId
     * @return
     */
    public List<FilledForm> getAllAdminFormData(int formid, Timestamp fromDate, Timestamp ToDate, final int accountId) {
        List<FilledForm> allformdetails = new ArrayList<FilledForm>();
        List<FilledForm> formdetails = new ArrayList<FilledForm>();
        try {
            String query = "SELECT id FROM fieldsense.users WHERE account_id_fk=? and role!=0";
            List<Integer> useridlist = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForList(query, new Object[]{accountId}, Integer.class);
            for (Integer userlist : useridlist) {
                StringBuilder query1 = new StringBuilder();
                query1.append("SELECT id,user_id,submitted_on from form_fill_details_table where form_id_fk=? and user_id=? and submitted_on between ? and ? order by submitted_on desc");
                final String query2 = "SELECT full_name from fieldsense.users where id=?";
                    //String username="";
                //log4jLog.info(" updateCustomer " + query1.toString());  
                Object param[] = new Object[]{formid, userlist, fromDate, ToDate};
                try {
                    formdetails = FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query1.toString(), param, new RowMapper<FilledForm>() {

                        public FilledForm mapRow(ResultSet rs, int i) throws SQLException {
                            FilledForm filledform = new FilledForm();
                            filledform.setId((Integer) (rs.getInt("id")));
                            filledform.setUserid((Integer) (rs.getInt("user_id")));
                            filledform.setSubmitTime(rs.getTimestamp("submitted_on"));
                            int user_id = (Integer) (rs.getInt("user_id"));
                            String username = "";
                            try {
                                username = (String) FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query2, new Object[]{user_id}, String.class);
                            } catch (Exception e) {
                                log4jLog.info("getAllAdminFormData " + e);
                                username = "";
                            }
                            filledform.setSubmittedby(username);
                            return filledform;
                        }
                    });
                    allformdetails.addAll(formdetails);
                } catch (Exception e) {
                    log4jLog.info(" getAllAdminFormData " + e);
                    e.printStackTrace();
                    return new ArrayList<FilledForm>();
                }
            }
        } catch (Exception e) {
            log4jLog.info(" getAllAdminFormData " + e);
            e.printStackTrace();
            return new ArrayList<FilledForm>();
        }
        return allformdetails;
    }

    /**
     *
     * @param formid
     * @param userId
     * @param fromDate
     * @param ToDate
     * @param accountId
     * @return
     */
    public List<FilledForm> getAllUserFormData(int formid, int userId, Timestamp fromDate, Timestamp ToDate, final int accountId) {
        List<FilledForm> allformdetails = new ArrayList<FilledForm>();
        List<FilledForm> formdetails = new ArrayList<FilledForm>();
        String query = "SELECT id FROM teams WHERE user_id_fk=?";
        Object[] param = new Object[]{userId};
        try {
            int teamId = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
            String query1 = "SELECT u.id,u.full_name FROM teams t INNER JOIN fieldsense.users u ON t.user_id_fk=u.id WHERE team_position_csv LIKE ? ORDER BY u.full_name";
            Object[] param1 = new Object[]{"%" + teamId + "%"};
            try {
                List<Map<String, Object>> useridlist = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForList(query1, param1);

                for (final Map<String, Object> userlist : useridlist) {
                    String query2 = "SELECT id,user_id,submitted_on from form_fill_details_table where form_id_fk=? and user_id=? and submitted_on between ? and ? order by submitted_on desc";

                    Object[] param2 = new Object[]{formid, userlist.get("id"), fromDate, ToDate};
                    try {
                        formdetails = FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query2, param2, new RowMapper<FilledForm>() {

                            public FilledForm mapRow(ResultSet rs, int i) throws SQLException {
                                FilledForm filledform = new FilledForm();
                                filledform.setId((Integer) (rs.getInt("id")));
                                filledform.setUserid((Integer) (rs.getInt("user_id")));
                                filledform.setSubmitTime(rs.getTimestamp("submitted_on"));
                                String username = (String) userlist.get("full_name");
                                filledform.setSubmittedby(username);
                                return filledform;
                            }
                        });
                        allformdetails.addAll(formdetails);
                    } catch (Exception e) {
                        // log4jLog.info(" selectCustomFormDetails " + e);
//                    e.printStackTrace();
                        return new ArrayList<FilledForm>();
                    }
                }
            } catch (Exception e) {
                log4jLog.info(" getAllUserFormData " + e);
//                e.printStackTrace();
                return new ArrayList<FilledForm>();
            }
        } catch (Exception e) {
            log4jLog.info(" getAllUserFormData " + e);
//            e.printStackTrace();
            return new ArrayList<FilledForm>();
        }
        return allformdetails;
    }

    /**
     *
     * @param formdetails
     * @param accountId
     * @return
     */
    public List<FilledForm> getFieldsData(List<FilledForm> formdetails, int accountId) {
        List<FilledForm> formList = new ArrayList<FilledForm>();
        List<FilledFields> fieldList = new ArrayList<FilledFields>();
        for (FilledForm fielddetails : formdetails) {
            // FilledForm ff=(FilledForm)i.next();
            int fieldId = fielddetails.getId();
            StringBuilder query1 = new StringBuilder();
//            query1.append("SELECT field_id_fk, field_value, field_type,form_fill_detail_id_fk from form_fill_values_table where form_fill_detail_id_fk=?;"); // commented by jyoti
            query1.append("SELECT field_id_fk, field_value, field_type,form_fill_detail_id_fk FROM form_fill_values_table f INNER JOIN custom_forms_fields c ON f.field_id_fk = c.id WHERE form_fill_detail_id_fk = ? ORDER BY c.field_order"); // added by jyoti, Bug #29696 -  to solve the issue of blank field after changing order of form fields
            //log4jLog.info(" updateCustomer " + query1.toString());  
            Object param[] = new Object[]{fieldId};
            try {
                fieldList = FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query1.toString(), param, new RowMapper<FilledFields>() {
                    public FilledFields mapRow(ResultSet rs, int i) throws SQLException {
                        FilledFields filledfields = new FilledFields();
                        filledfields.setField_id_fk(rs.getString("field_id_fk"));
                        filledfields.setField_value(rs.getString("field_value"));
                        filledfields.setFieldType(rs.getString("field_type"));
                        filledfields.setForm_fill_detail_id_fk(rs.getInt("form_fill_detail_id_fk"));
                        return filledfields;
                    }
                });
                fielddetails.setFilledData((ArrayList<FilledFields>) fieldList);
                formList.add(fielddetails);

            } catch (Exception e) {
                log4jLog.info(" getFieldsData " + e);
//                e.printStackTrace();
                return new ArrayList<FilledForm>();
            }

        }
        return formList;
    }

    /**
     *
     * @param formdetails
     * @param accountId
     * @return
     */
    public List<FilledForm> getAllAdminFieldsData(List<FilledForm> formdetails, int accountId) {
        List<FilledForm> formList = new ArrayList<FilledForm>();
        List<FilledFields> fieldList = new ArrayList<FilledFields>();
        for (FilledForm fielddetails : formdetails) {
            // FilledForm ff=(FilledForm)i.next();
            int fieldId = fielddetails.getId();
            //int userid=fielddetails.getUserid();
            StringBuilder query1 = new StringBuilder();
            //String username="";
//            query1.append("SELECT field_id_fk, field_value,field_type,form_fill_detail_id_fk from form_fill_values_table where form_fill_detail_id_fk=?;"); // commented by jyoti
            query1.append("SELECT field_id_fk, field_value, field_type, form_fill_detail_id_fk FROM form_fill_values_table f INNER JOIN custom_forms_fields c ON f.field_id_fk = c.id WHERE form_fill_detail_id_fk = ? ORDER BY c.field_order"); // added by jyoti, Bug #29696 -  to solve the issue of blank fields displayed after changing order of form fields
            // String query2 = "SELECT full_name from fieldsense.users where id=?";
            //log4jLog.info(" updateCustomer " + query1.toString());  
            Object param1[] = new Object[]{fieldId};
            //Object param2[] = new Object[]{userid};
            try {
                fieldList = FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query1.toString(), param1, new RowMapper<FilledFields>() {
                    public FilledFields mapRow(ResultSet rs, int i) throws SQLException {
                        FilledFields filledfields = new FilledFields();
                        filledfields.setField_id_fk(rs.getString("field_id_fk"));
                        filledfields.setField_value(rs.getString("field_value"));
                        filledfields.setFieldType(rs.getString("field_type"));
                        filledfields.setForm_fill_detail_id_fk(rs.getInt("form_fill_detail_id_fk"));
                        return filledfields;
                    }
                });

                fielddetails.setFilledData((ArrayList) fieldList);
                formList.add(fielddetails);

            } catch (Exception e) {
                log4jLog.info(" getAllAdminFieldsData " + e);
                e.printStackTrace();
                return new ArrayList<FilledForm>();
            }

        }
        return formList;
    }

    /**
     * @Added by jyoti
     * @param formId
     * @param accountId
     * @return
     */
    @Override
    public List<String> getAllFormLabelList(int formId, int accountId) {

        String query = "SELECT  label FROM custom_forms_fields WHERE form_id_fk = ? ORDER BY field_order ";
        Object param[] = new Object[]{formId};
        log4jLog.info(" getAllAdminFormLabels for formId :  " + formId);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForList(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" getAllAdminFormLabels  form formId : " + formId + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * @Added by jyoti
     * @param formId
     * @param accountId
     * @return
     */
    @Override
    public String getCustomFormName(int formId, int accountId) {

        String query = "SELECT form_name FROM custom_forms WHERE id = ? ";
        Object param[] = new Object[]{formId};
        log4jLog.info(" getCustomFormName for formId :  " + formId);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" getCustomFormName  form formId : " + formId + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }
    
    /**
     *
     * @param formdetails
     * @param accountId
     * @return
     */
    public List<FilledForm> getAllUserFieldsData(List<FilledForm> formdetails, int accountId) {
        List<FilledForm> formList = new ArrayList<FilledForm>();
        List<FilledFields> fieldList = new ArrayList<FilledFields>();
        for (FilledForm fielddetails : formdetails) {
            // FilledForm ff=(FilledForm)i.next();
            int fieldId = fielddetails.getId();
            //int userid=fielddetails.getUserid();
            StringBuilder query1 = new StringBuilder();
            //String username="";
//            query1.append("SELECT field_id_fk,field_value,field_type,form_fill_detail_id_fk from form_fill_values_table where form_fill_detail_id_fk=?;"); // commented by jyoti
            query1.append("SELECT field_id_fk, field_value, field_type, form_fill_detail_id_fk FROM form_fill_values_table f INNER JOIN custom_forms_fields c ON f.field_id_fk = c.id WHERE form_fill_detail_id_fk = ? ORDER BY c.field_order"); // added by jyoti, Bug #29696 -  to solve the issue of blank fields displayed after changing order of form fields
            // String query2 = "SELECT full_name from fieldsense.users where id=?";
            //log4jLog.info(" updateCustomer " + query1.toString());  
            Object param1[] = new Object[]{fieldId};
            //Object param2[] = new Object[]{userid};
            try {
                fieldList = FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query1.toString(), param1, new RowMapper<FilledFields>() {
                    public FilledFields mapRow(ResultSet rs, int i) throws SQLException {
                        FilledFields filledfields = new FilledFields();
                        filledfields.setField_id_fk(rs.getString("field_id_fk"));
                        filledfields.setField_value(rs.getString("field_value"));
                        filledfields.setFieldType(rs.getString("field_type"));
                        filledfields.setForm_fill_detail_id_fk(rs.getInt("form_fill_detail_id_fk"));
                        return filledfields;
                    }
                });

                fielddetails.setFilledData((ArrayList) fieldList);
                formList.add(fielddetails);

            } catch (Exception e) {
                log4jLog.info(" selectCustomFormDetails " + e);
//                e.printStackTrace();
                return new ArrayList<FilledForm>();
            }

        }
        return formList;
    }

    /**
     *
     * @param formdetails
     * @param formId
     * @param filledformid
     * @param accountId
     * @return
     */
    public boolean insertFieldsData(FilledForm formdetails, int formId, int filledformid, int accountId) {
        ArrayList<FilledFields> fieldlist = formdetails.getFilledData();
        for (FilledFields fieldDetails : fieldlist) {
            // Added by jyoti, Feature #30909 - when offline forms filled address are unresolved
            if (fieldDetails.getFieldType().trim().equals("Location")) {
                if (fieldDetails.getField_value().contains("Lat:")) {
                    String unResolvedLatLong = fieldDetails.getField_value();
//                    System.out.println("unResolvedLatLong : "+unResolvedLatLong);
                    String latitude = unResolvedLatLong.split(",")[0].split(":")[1].trim();
                    String longitude = unResolvedLatLong.split(",")[1].split(":")[1].trim();
//                    System.out.println("lat long : "+latitude + " , " +longitude);
                    try {
                        GoogleResponse res = new AddressConverter().convertToAddressFromLatLong(latitude, longitude);
//                        System.out.println("res.getStatus() : "+res.getStatus());
                        if (res.getStatus().equals("OK")) {
                            String resolvedAddress = res.getResults()[0].getFormatted_address();
//                            System.out.println("address is : " + resolvedAddress);
                            fieldDetails.setField_value(resolvedAddress);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            // ended by jyoti
            if (fieldDetails.getFieldType().trim().equals("Image")) {
                Map<String, String> imageNameList = fieldDetails.getCustomFormImage().getImageURL();
                if (!imageNameList.isEmpty()) {
                    String imageName = imageNameList.get("imageName");
                    String imageFileName = imageNameList.get("imageFileName");
                    File oldfile = new File(Constant.CUSTOM_FORM_IMAGE_UPLOAD_PATH + imageName);
                    PhotoToIconCreator photoToIconCreator = new PhotoToIconCreator();
                    String imageUrlName = "account_" + accountId + "_form_" + formId + "_field_" + fieldDetails.getField_id_fk() + "_filledOnId_" + filledformid;
                    try {
                        photoToIconCreator.uploadCutomFormImage(oldfile, imageUrlName);
                    } catch (IOException ex) {
//                        ex.printStacskTrace();
                        java.util.logging.Logger.getLogger(CustomFrmRprtImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    String imageNameUrl = Constant.CUSTOM_FORM_IMAGE_UPLOAD_PATH + imageUrlName + ".png";
                    File newfile = new File(imageNameUrl);
                    oldfile.renameTo(newfile);
                    fieldDetails.setField_value(imageFileName);
                }
            }

            String query = "INSERT INTO form_fill_values_table (field_id_fk,field_value,form_fill_detail_id_fk,field_type) VALUES (?,?,?,?)";
            Object param[] = new Object[]{fieldDetails.getField_id_fk(), fieldDetails.getField_value(), formdetails.getId(), fieldDetails.getFieldType()};
            if (fieldDetails.getGeoTagEnable() == 1) {
                query = "INSERT INTO form_fill_values_table (field_id_fk,field_value,form_fill_detail_id_fk,field_type,user_id_fk,created_on,latitude,longitude) VALUES (?,?,?,?,?,?,?,?)";
                param = new Object[]{fieldDetails.getField_id_fk(), fieldDetails.getField_value(), formdetails.getId(), fieldDetails.getFieldType(), fieldDetails.getCustomFormImage().getUser().getId(), fieldDetails.getCustomFormImage().getCreatedOn(), fieldDetails.getCustomFormImage().getUser().getLatitude(), fieldDetails.getCustomFormImage().getUser().getLangitude()};
            }
       // log4jLog.info(" insertFields " + query);

            try {

                synchronized (this) {
                    if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                        // log4jLog.info("FormFields inserted successfully" );
                    }
                }
            } catch (Exception e) {
                log4jLog.info(" insertFormFields " + e);
//            e.printStackTrace();

                //Remove already inserted data
                query = "delete from form_fill_details_table where id=" + formdetails.getId();
                try {
                    synchronized (this) {
                        FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query);
                    }
                } catch (Exception ex) {
                    log4jLog.info(" insertFormFields " + e);
//                ex.printStackTrace();
                }
                query = "delete from form_fill_values_table where form_fill_detail_id_fk=" + formdetails.getId();
                try {
                    synchronized (this) {
                        FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query);
                    }
                } catch (Exception ex) {
                    log4jLog.info(" insertFormFields " + e);
//                ex.printStackTrace();
                }
                //
                return false;
            }
        }
        return true;
        //return FieldSenseUtils.getMaxIdForTable("formfields", accountId);
    }

}
