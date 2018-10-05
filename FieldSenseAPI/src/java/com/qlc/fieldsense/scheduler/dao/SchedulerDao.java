/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.scheduler.dao;

import com.qlc.fieldsense.scheduler.model.Scheduler;
import com.qlc.fieldsense.usersTravelLogs.model.UsersTravelLogs;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;

/**
 *
 * @author siddhesh
 */
public interface SchedulerDao {
    
    public List<UsersTravelLogs> getListOfDataFromLocationNotFound();
    
    public boolean deletelocationNotFoundEntires(List<Integer> listOfIds);
    
}
