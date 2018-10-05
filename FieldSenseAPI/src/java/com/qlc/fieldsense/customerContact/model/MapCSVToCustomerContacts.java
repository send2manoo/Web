/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customerContact.model;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author anuja
 */
public class MapCSVToCustomerContacts {

    /**
     *
     * @param uploads
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<CustomerContactsCSV> mapJavaBean(MultipartFile uploads) throws FileNotFoundException, IOException {
        ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
        strat.setType(CustomerContactsCSV.class);
        String[] columns = new String[]{"firstName", "middleName", "lastName", "phone1", "phone2", "phone3", "fax1", "fax2", "mobile1", "mobile2", "email1", "email2", "reportTo", "assistantName", "spouseName", "birthDate", "anniversaryDate", "designation", "customerName","customerLocationIdentifier"};
        strat.setColumnMapping(columns);
        CsvToBean csv = new CsvToBean();
        CSVReader csvReader = new CSVReader(new InputStreamReader(uploads.getInputStream()),',','\"');
        List<CustomerContactsCSV> list = new ArrayList<CustomerContactsCSV>();
        try {
            list = csv.parse(strat, csvReader);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return list;
    }
}
