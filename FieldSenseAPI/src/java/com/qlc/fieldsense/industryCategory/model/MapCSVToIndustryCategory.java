/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.industryCategory.model;

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
 * @author jyoti
 */
public class MapCSVToIndustryCategory {
    
    /**
     *
     * @param uploads
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<IndustryCategoryCSV> mapJavaBean(MultipartFile uploads) throws FileNotFoundException, IOException {
        ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
        strat.setType(IndustryCategoryCSV.class);
        String[] columns = new String[]{"categoryName", "isActive"};
        strat.setColumnMapping(columns);
        CsvToBean csv = new CsvToBean();
        CSVReader csvReader = new CSVReader(new InputStreamReader(uploads.getInputStream()),',','\"');
        List<IndustryCategoryCSV> list = new ArrayList<IndustryCategoryCSV>();
        try {
            list = csv.parse(strat, csvReader);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return list;
    }
    
}
