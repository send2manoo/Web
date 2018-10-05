/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customerOffline.model;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import com.qlc.fieldsense.customer.model.CustomerCSV;
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
public class MapCSVToCustomerOffline {
    
    /**
     *
     * @param uploads
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<CustomerOfflineCSV> mapJavaBean(MultipartFile uploads) throws FileNotFoundException, IOException {
        ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
        strat.setType(CustomerCSV.class);
        String[] columns = new String[]{"customerName", "customerPrintas", "customerLocation", "isHeadOffice", "territory","industry", "customerPhone1", "customerPhone2", "customerPhone3", "customerFax1", "customerFax2", "customerEmail1", "customerEmail2", "customerEmail3", "customerWebsiteUrl1", "customerWebsiteUrl2","customerAddress1"};
        strat.setColumnMapping(columns);
        CsvToBean csv = new CsvToBean();
//        CSVReader csvReader = new CSVReader(new InputStreamReader(uploads.getInputStream()),',','\"',1);
        CSVReader csvReader = new CSVReader(new InputStreamReader(uploads.getInputStream()),',','\"');
        List<CustomerOfflineCSV> list = new ArrayList<CustomerOfflineCSV>();
        try {
            list = csv.parse(strat, csvReader);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return list;
    }
    
}
