/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.stats.model;

import com.qlc.fieldsense.customer.model.Customer;
import com.qlc.fieldsense.stats.dao.StatsDao;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Ramesh
 * @date 16-04-2015
 *
 */
public class StatsManager {

    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
    StatsDao statsDao = (StatsDao) GetApplicationContext.ac.getBean("statsDaoImpl");

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("StatsManager");

    /**
     * 
     * @param allRequestParams
     * @param offset
     * @param userToken
     * @return 
     * @purpose Used to get statistics of accounts. 
     */
    public Object selectAccountsData(@RequestParam Map<String,String> allRequestParams, String userToken) {
        log4jLog.info("Inside StatsManager class selectAccountsData() method ");
//           System.out.println("selectAccountsData$$$$$");
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                List<AccountData> listOfAccountsData = statsDao.selectAccountsData(allRequestParams);
                List<AccountData> accountslist=new ArrayList<AccountData>();
                for(AccountData accdata:listOfAccountsData){
                    if(!accdata.getCreatedOn().equals(Timestamp.valueOf("1111-11-11 11:11:11"))){
                        accountslist.add(accdata);
                    }
                }
//                System.out.println("StatsManager "+accountslist);
//                System.out.println("returned from manager");
                return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " Accounts list", accountslist,listOfAccountsData.size());
        // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //} 
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    
    /**
     * 
     * @param accountId
     * @param fromDate
     * @param toDate
     * @param offset
     * @param userToken
     * @return 
     * @purpose Used to get statistics of specific accounts for specific period. 
     */
    public Object selectAccountStats(String fromDate, String toDate, int accountId, int offset, String userToken) {
        log4jLog.info("Inside StatsManager class selectAccountStats() method ");
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                List<Stats> statsList = statsDao.selectAccountStats(fromDate, toDate, accountId, offset);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " Stats list", statsList);
        // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //} 
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
//nikhil
    public Object selectUserStats(@RequestParam Map<String,String> allRequestParams, String userToken) {
        log4jLog.info("Inside StatsManager class selectUserStats() method ");
        
        if (fieldSenseUtils.isTokenValid(userToken)) {
//            int accountId = fieldSenseUtils.accountIdForToken(userToken);
            
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                List<Stats> statsList = statsDao.selectUserStats(allRequestParams);
//                System.out.println("$$$$$$$$$$ "+statsList);
                //return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " Stats list", statsList);
                return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Stats list", statsList,statsList.size());
        // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //} 
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
      /**
     * Added by siddhesh for search user data in new super user panel.29-06-17 #
     * @param offset
     * @param userToken
     * @purpose Used to get searched user data.
     */
    public Object selectUsers(@RequestParam Map<String,String> allRequestParams, String userToken) {
        log4jLog.info("Inside StatsManager class selectAccountsData() method ");
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                List<AccountUsers> listOfUserData= statsDao.selectUsersData(allRequestParams);
                List<AccountUsers> listOfUsers=new ArrayList<AccountUsers>();
                for(AccountUsers accdata:listOfUserData){
                    if(accdata.getCheckId()!=0){
                        listOfUsers.add(accdata);
                    }
                }
                return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " Accounts list", listOfUsers,listOfUserData.size());
        // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //} 
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
   
 /**
  * Added  by manohar mar 23 2018
  * @param stats
  * @param userToken
  * @return 
  */
     public Object getLogs(Stats stats, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
             int isEmail = fieldSenseUtils.isEmailExist(stats.getEmail_id());
             if (isEmail>0) {
                        int userId=fieldSenseUtils.getUserId(stats.getEmail_id());
                        List<Stats> getTravelList = null;
                        List<Stats> getDebugList = null;
                        List<Stats> list = new ArrayList<Stats>();                       
                        int debugStatus=stats.getDebugStatus();
                        int travelStatus=stats.getTravelStatus(); 
                       
                        String dir = "";
                        if(debugStatus==1&&travelStatus==1)
                        {
                            try 
                            {
                                dir  = Constant.MOBILE_APP_DEBUG_UPLOAD_PATH;
                                getDebugList = getLogs(userId,stats, dir,"DEBUG");
                                dir  = Constant.MOBILE_TRAVEL_LOG_UPLOAD_PATH;
                                getTravelList = getLogs(userId,stats, dir,"TRAVEL");
                                list.addAll(getDebugList);
                                list.addAll(getTravelList);  
                             } 
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else if(debugStatus==1)
                        { 
                          dir  = Constant.MOBILE_APP_DEBUG_UPLOAD_PATH;
                          list = getLogs(userId,stats, dir,"DEBUG"); 
                        }
                        else
                        {                           
                          dir  = Constant.MOBILE_TRAVEL_LOG_UPLOAD_PATH;
                          list = getLogs(userId,stats, dir,"TRAVEL"); 
                        }
                        if(list.size()!=0)
                           return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "log list .", list);
                        else
                           return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Logs is not on the Server Please try with diffrent date .", "", "");
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " User is not registered Please try with diffrent user .", "", "");
                }
             // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        } 
    } 
     
    /**
     * Addeed by manohar 23 2018
     * @param userId
     * @param stats
     * @param dir
     * @param status
     * @return 
     */   
    public  List<Stats> getLogs(int userId,Stats stats, String dir,String status) 
    {
        List<String> strdates = new ArrayList<String>();
        List<Stats> logList=new ArrayList<Stats>();
        try {
            List<Date> dates = new ArrayList<Date>();          
            String str_date =stats.getStartDate();
//            System.out.println("str_date="+str_date);
            
//            String end_date =stats.getEndDate();
            String splitstr_date[]=str_date.split("/");
//            String splitend_date[]=end_date.split("/");
            String concatstrSplit=splitstr_date[2]+splitstr_date[1]+splitstr_date[0];
//            System.out.println("concatstrSplit 210="+concatstrSplit);
//            String concatendSplit=splitend_date[2]+splitend_date[0]+splitend_date[1];
            DateFormat formatter ;            
            formatter = new SimpleDateFormat("yyyyMMdd");
            Date  startDate = (Date)formatter.parse(concatstrSplit);
//            Date  endDate = (Date)formatter.parse(concatendSplit);
            long interval = 24*1000 * 60 * 60; // 1 hour in millis
//            long endTime =endDate.getTime() ; // create your endtime here, possibly using Calendar or Date
            long curTime = startDate.getTime();
//            System.out.println("curTime="+curTime);
//            while (curTime <= endTime) {
//                dates.add(new Date(curTime));
//                curTime += interval;
//            }
//            for(int i=0;i<dates.size();i++){
//                Date lDate =(Date)dates.get(i);
//                String ds = formatter.format(lDate);
//                
//                strdates.add(ds);
//            }            
            String ext = ".txt";
               try {
                   logList=findFilesForLogs(dir,ext,userId,concatstrSplit,status);
//                   System.out.println("logList"+logList.size());
            } catch (Exception e) {
                   e.printStackTrace();
            }            
        } catch (Exception ex) {
              ex.printStackTrace();
        }
        return logList;
    } 

    /**
     * Addeed by manohar 23 2018
     * @param dir
     * @param ext
     * @param userId
     * @param strdates
     * @param status
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */ 
    private  List<Stats> findFilesForLogs(final String dir, String ext,int userId,String strdates,String status) throws FileNotFoundException, IOException 
    {
//               System.out.println("userId="+userId+"\tstrdates="+strdates);
               File file = new File(dir);               
               if (!file.exists())
                       System.out.println(dir + " Directory doesn't exists");              
               File[] listFiles = file.listFiles(new MyFileNameFilter(ext));
               final List<Stats>  ls=new ArrayList<Stats>();	
               List<String>  allFileList=new ArrayList<String>();
               final List<String> QualifiedList=new ArrayList<String>();
               boolean s = false;
               if (listFiles.length == 0) 
                       System.out.println(dir + "doesn't have any file with extension " + ext);
                else {
                                    for (File f : listFiles)
                                            allFileList.add(f.getName());                                      
//                                          for (String  eachDate : strdates) 
//                                          {
//                                            System.out.println("eachDate="+eachDate);
                                            for (String eachFile : allFileList) 
                                            {
                                                    if(status=="TRAVEL")
                                                        s=eachFile.startsWith("LocationLogs"+userId+"_"+strdates);  
                                                    if(status=="DEBUG")                                     
                                                        s=eachFile.startsWith("Debug_Log_"+userId+"_"+strdates);  
                                                    if(s==true)
                                                      QualifiedList.add(eachFile);
//                                                        System.out.println("eachFile="+eachFile);
                                             }
//                                          }
//                                          System.out.println("QualifiedList="+QualifiedList.size());
//                                          System.out.println("thread runnable="+Thread.currentThread().getName());                                       
                                          Thread r1=new Thread(new Runnable() {
                                                   int i=0;
                                                   public void run() {
                                                        for (String QualifiedList1 : QualifiedList) 
                                                       {    
                                                            Stats s1=new Stats();

                                                            File file1=new File(dir+QualifiedList1);  
                                                            i++;
                                                            try
                                                            {
                                                            s1.setFile(FileUtils.readFileToString(file1, "utf-8"));
                                                            String token[]=QualifiedList1.split("_");
                                                            if(token[0].startsWith("Debug"))
                                                            {
                                                                 s1.setFile_Name(token[0]+"_"+token[1]+"_"+token[3]);
                                                            }
                                                            else
                                                            {
                                                               String sub= token[0].substring(0,12)+"_"+token[1];
                                                               s1.setFile_Name(sub);                                                  
                                                            }
                                                            ls.add(s1);
                                                            s1=null;
                                                            file1=null;  
                                                            System.gc();
                                                            }catch(Exception e)
                                                            {
//                                                                System.out.println("test e.."+e);
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                   }
                                               });
                                               r1.setPriority(10);
                                               r1.start();
                                               try {
                       r1.join();
                   } catch (Exception e) {
                       e.printStackTrace();
                   }                          
                }
                return ls;
    }     
    /**
     * Addeed by manohar mar 23 2018
     */
    public static class MyFileNameFilter implements FilenameFilter 
    {
               private String ext;
               public MyFileNameFilter(String ext) {
                       this.ext = ext.toLowerCase();
               }
               @Override
               public boolean accept(File dir, String name) {
                       return name.toLowerCase().endsWith(ext);
               }
    }
    
    /**
     * @param userToken
     * @added by jyoti, 30-07-2018
     * @param fromDate
     * @param toDate
     * @return 
     */
    public Object getDiy4StepsData(String fromDate, String toDate, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            List<DIYData> listOfDiy4StepsData = statsDao.getDiy4StepsData(fromDate, toDate);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "DIY 4 steps data list", "DIYData", listOfDiy4StepsData);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "Invalid token. Please try again.", "", "");
        }
    }
    
}
