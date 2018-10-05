/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.appointments.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author manohar
 */
public class Testing {
    public static void main(String[] args) {
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateUtil = new Date();
                String timeAfter24Hours = "";
                try {
                    String timeStamp =parser.format(Calendar.getInstance().getTime());
                    
                    dateUtil = parser.parse("2018-05-31 00:00:00");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateUtil);
                    calendar.add(Calendar.HOUR_OF_DAY, 24);
                    dateUtil = calendar.getTime();
                    timeAfter24Hours = parser.format(dateUtil);
                    
//                    System.out.println("timeAfter24Hours="+timeAfter24Hours);
//                    System.out.println("timeStamp="+timeStamp);
                    
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
    }
}
