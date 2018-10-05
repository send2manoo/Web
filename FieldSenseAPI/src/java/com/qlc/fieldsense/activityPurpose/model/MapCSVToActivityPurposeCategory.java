/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.activityPurpose.model;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import com.qlc.fieldsense.expenseCategory.model.ExpenseCategoryCSV;
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
public class MapCSVToActivityPurposeCategory {
    
    /**
     *
     * @param uploads
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<ActivityPurposeCategoryCSV> mapJavaBean(MultipartFile uploads) throws FileNotFoundException, IOException {
        ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
        strat.setType(ActivityPurposeCategoryCSV.class);
        String[] columns = new String[]{"purpose", "isActive"};
        strat.setColumnMapping(columns);
        CsvToBean csv = new CsvToBean();
        CSVReader csvReader = new CSVReader(new InputStreamReader(uploads.getInputStream()),',','\"');
        List<ActivityPurposeCategoryCSV> list = new ArrayList<ActivityPurposeCategoryCSV>();
        try {
            list = csv.parse(strat, csvReader);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return list;
    }
    
}
