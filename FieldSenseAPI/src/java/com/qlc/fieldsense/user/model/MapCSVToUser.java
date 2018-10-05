/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.user.model;

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
public class MapCSVToUser {

    /**
     *
     * @param uploads
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<UserCSV> mapJavaBean(MultipartFile uploads) throws FileNotFoundException, IOException {
        ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
        strat.setType(UserCSV.class);     //modified emp_code by manohar
        String[] columns = new String[]{"emp_code","firstName", "lastName", "designation", "password", "mobileNo", "gender","active","allowTimeout","role","reportingHead","emailAddress"};
        strat.setColumnMapping(columns);
        CsvToBean csv = new CsvToBean();
        CSVReader csvReader = new CSVReader(new InputStreamReader(uploads.getInputStream()),',','\"');
        List<UserCSV> list = new ArrayList<UserCSV>();
        try {
            list = csv.parse(strat, csvReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
