/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.memcached.model;

import com.qlc.fieldsense.auth.model.AuthenticationUserManager;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
//import net.spy.memcached.MemcachedClient;

/**
 *
 * @author jyoti
 */
public class MemcachedManager {

//    public boolean isTokenValid(String token) {
//        //            MemcachedClient memCachedClient = new MemcachedClient(new InetSocketAddress("localhost", 11211));
//        MemcachedClient memCachedClient = getMemCachedConnection();
//        System.out.println("token : "+memCachedClient.get(token));
//        if(memCachedClient.get(token) != null){
//            memCachedClient.shutdown();
//            return true;
//        } else{
//            memCachedClient.shutdown();
//            return false;
//        }
//    }
//    
//    public static MemcachedClient getMemCachedConnection(){
//        try {
//            MemcachedClient memCachedClient = new MemcachedClient(new InetSocketAddress("localhost", 11211));
//            return memCachedClient;
//        } catch (IOException ex) {
//            Logger.getLogger(MemcachedManager.class.getName()).log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }

}
