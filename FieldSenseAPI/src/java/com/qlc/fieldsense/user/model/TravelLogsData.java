/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.user.model;

import com.qlc.fieldsense.usersTravelLogs.model.UsersTravelLogs;
import java.util.ArrayList;

/**
 *
 * @author siddhesh
 */
public class TravelLogsData {
    
  private ArrayList<UsersTravelLogs> addressResolved;
  private ArrayList<UsersTravelLogs> addressNotResolved;
  
  public TravelLogsData(){
      addressResolved=new ArrayList<UsersTravelLogs>();
      addressNotResolved=new ArrayList<UsersTravelLogs>();
  }

    public ArrayList<UsersTravelLogs> getAddressResolved() {
        return addressResolved;
    }

    public void setAddressResolved(ArrayList<UsersTravelLogs> addressResolved) {
        this.addressResolved = addressResolved;
    }

    @Override
    public String toString() {
        return "TravelLogsData{" + "addressResolved=" + addressResolved + ", addressNotResolved=" + addressNotResolved + '}';
    }

    public ArrayList<UsersTravelLogs> getAddressNotResolved() {
        return addressNotResolved;
    }

    public void setAddressNotResolved(ArrayList<UsersTravelLogs> addressNotResolved) {
        this.addressNotResolved = addressNotResolved;
    }
}
