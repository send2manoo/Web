/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.stats.dao;

//import static com.qlc.fieldsense.customer.dao.CustomerDaoImpl.log4jLog;
import com.qlc.fieldsense.customer.model.Customer;
import static com.qlc.fieldsense.search.dao.SearchDaoImpl.log4jLog;
import com.qlc.fieldsense.stats.model.AccountData;
import com.qlc.fieldsense.stats.model.AccountUsers;
import com.qlc.fieldsense.stats.model.DIYData;
import com.qlc.fieldsense.stats.model.Stats;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Ramesh
 * @date 21-04-2015
 */
public class StatsDaoImpl implements StatsDao {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("StatsDaoImpl");
    private JdbcTemplate jdbcTemplate;
    private JdbcTemplate jdbcTemplateStats;

    /**
     *
     * @return
     */
    public JdbcTemplate getJdbcTemplateStats() {
        return jdbcTemplateStats;
    }

    /**
     *
     * @param jdbcTemplateStats
     */
    public void setJdbcTemplateStats(JdbcTemplate jdbcTemplateStats) {
        this.jdbcTemplateStats = jdbcTemplateStats;
    }

    /**
     *
     * @return
     */
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
     *
     * @param jdbcTemplate
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     *
     * @param date
     * @param accountId
     * @return
     */
    @Override
    public synchronized Stats getAccountWaiseStatDataDaily(String date, int accountId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT (SELECT COUNT(id) FROM customers WHERE record_state !=3 AND DATE(created_on)=?) AS customersCount,");
        query.append("(SELECT COUNT(id) FROM customer_contacts WHERE DATE(created_on)=?) AS customersContactCount,");
        query.append("(SELECT COUNT(id) FROM appointments WHERE record_state!=3 AND DATE(created_on)=?) AS appointmentsCount,");
        query.append("(SELECT COUNT(id) FROM messages WHERE DATE(created_on)=?) AS messagesCount,");
        query.append("(SELECT COUNT(id) FROM expenses WHERE DATE(created_on)=?) AS expenseCount,");
        query.append("(SELECT COUNT(id) FROM attendances WHERE DATE(created_on)=?) AS attendanceUsersCount,");
        query.append("(SELECT COUNT(id) FROM fieldsense.users WHERE DATE(created_on)=? AND account_id_fk=?) AS usersCount,");
        query.append("(SELECT company_name FROM fieldsense.accounts WHERE id=?) AS accountName");
        log4jLog.info("Inside FieldSenseStatsUtils class  getAccountWaiseStatDataDaily method" + query);
        Object param[] = new Object[]{date, date, date, date, date, date, date, accountId, accountId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query.toString(), param, new RowMapper<Stats>() {

                @Override
                public Stats mapRow(ResultSet rs, int i) throws SQLException {
                    Stats fieldSenseStat = new Stats();
                    fieldSenseStat.setUserCount(rs.getInt("usersCount"));
                    fieldSenseStat.setCustomerCount(rs.getInt("customersCount"));
                    fieldSenseStat.setContactCount(rs.getInt("customersContactCount"));
                    fieldSenseStat.setActivityCount(rs.getInt("appointmentsCount"));
                    fieldSenseStat.setMessageCount(rs.getInt("messagesCount"));
                    fieldSenseStat.setExpenseCount(rs.getInt("expenseCount"));
                    fieldSenseStat.setAttendanceUsersCount(rs.getInt("attendanceUsersCount"));
                    fieldSenseStat.setAccountName(rs.getString("accountName"));
                    return fieldSenseStat;
                }
            });
        } catch (Exception e) {
//            e.printStackTrace();
            log4jLog.info("Inside FieldSenseStatsUtils class  getAccountWaiseStatDataDaily method" + e);
            return new Stats();
        }
    }

    /**
     *
     * @param fss
     * @return
     */
    @Override
    public int inserIntoStatsTable(Stats fss) {
        String query = "INSERT INTO fieldsensestats.fieldsensestats(statday,statweek,statmonth,statyear,account_id_fk,account_name,user_count,customer_count,contact_count,activity_count,message_count,expense_count,attendanceuser_count,created_on) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?, now())";
        Object param[] = new Object[]{fss.getStatDay(), fss.getStatWeek(), fss.getStatMonth(), fss.getStatYear(), fss.getAccountId(), fss.getAccountName(), fss.getUserCount(), fss.getCustomerCount(), fss.getContactCount(), fss.getActivityCount(), fss.getMessageCount(), fss.getExpenseCount(), fss.getAttendanceUsersCount()};
        log4jLog.info("Inside FieldSenseStatsUtils class  inserIntoStatsTable method" + query);
        try {
            return this.jdbcTemplate.update(query, param);
        } catch (Exception e) {
            log4jLog.info("Inside FieldSenseStatsUtils class  inserIntoStatsTable method" + e);
//            e.printStackTrace();
            return 0;
        }
    }

    /**
     *
     * @param fromDate
     * @param toDate
     * @param accountId
     * @param offset
     * @return
     */
    @Override
    public List<Stats> selectAccountStats(String fromDate, String toDate, int accountId, int offset) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT id,statday,statweek,statmonth,statyear,account_id_fk,account_name,");
        query.append("user_count,customer_count,contact_count,activity_count,message_count,");
        query.append("expense_count,attendanceuser_count,created_on,");
        query.append("(SELECT COUNT(id) FROM fieldsensestats.fieldsensestats WHERE (created_on BETWEEN ? AND ? ) AND account_id_fk =?) AS statsCount ");
        query.append(" FROM fieldsensestats.fieldsensestats ");
        query.append(" WHERE (created_on BETWEEN ? AND ? ) AND account_id_fk =?");
        query.append(" LIMIT 10 OFFSET ?");
        Object param[] = new Object[]{fromDate, toDate, accountId, fromDate, toDate, accountId, offset};
        log4jLog.info("Inside FieldSenseStatsUtils class  selectAllStats method" + query);
        try {
            return this.jdbcTemplate.query(query.toString(), param, new RowMapper<Stats>() {

                @Override
                public Stats mapRow(ResultSet rs, int i) throws SQLException {
                    Stats stats = new Stats();
                    stats.setId(rs.getInt("id"));
                    stats.setStatDay(rs.getInt("statday"));
                    stats.setStatWeek(rs.getInt("statweek"));
                    stats.setStatMonth(rs.getInt("statmonth"));
                    stats.setStatYear(rs.getInt("statyear"));
                    stats.setAccountId(rs.getInt("account_id_fk"));
                    stats.setAccountName(rs.getString("account_name"));
                    stats.setUserCount(rs.getInt("user_count"));
                    stats.setCustomerCount(rs.getInt("customer_count"));
                    stats.setContactCount(rs.getInt("contact_count"));
                    stats.setActivityCount(rs.getInt("activity_count"));
                    stats.setMessageCount(rs.getInt("message_count"));
                    stats.setExpenseCount(rs.getInt("expense_count"));
                    stats.setAttendanceUsersCount(rs.getInt("attendanceuser_count"));
                    stats.setCreatedOn(rs.getTimestamp("created_on"));
                    stats.setStatsCount(rs.getInt("statsCount"));
                    return stats;
                }
            });
        } catch (Exception e) {
            log4jLog.info("Inside FieldSenseStatsUtils class  selectAllStats method" + e);
//            e.printStackTrace();
            return new ArrayList<Stats>();
        }
    }
    //\"%1234567890%\"
    public List<Stats> selectUserStats(@RequestParam Map<String,String> allRequestParams){
        StringBuilder query = new StringBuilder();
        String searchText=allRequestParams.get("searchText").trim();
        query.append("select u.id,u.full_name,u.email_address,a.company_name from users u join accounts a ");
                query.append("on u.account_id_fk=a.id where u.mobile_number like ?");
        
        Object param[] = new Object[]{"%" +searchText+ "%"};
//          System.out.println("dao called ..");
        log4jLog.info(" getUserDetailsSuperAdmin " + query);
        
        try {
            return jdbcTemplate.query(query.toString(), param, new RowMapper<Stats>() {

                @Override
                public Stats mapRow(ResultSet rs, int i) throws SQLException {
                    Stats stat = new Stats();
                    stat.setId(rs.getInt("id"));
                    stat.setFull_name(rs.getString("full_name"));
                    stat.setAccountName(rs.getString("company_name"));
                    stat.setEmail_id(rs.getString("email_address"));
                    
                    
                    return stat;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectCustomer " + e);
//            e.printStackTrace();
            return new ArrayList<Stats>();
        }
    }
    
    /**
     *
     * @param allRequestParams
     * @return
     */
//    @Override
//    public List<AccountData> selectAccountsData(@RequestParam Map<String,String> allRequestParams) {
//            try {
//              // Added by nikhil on 11th june 2017 :- plan & startDate columns in query   
//            StringBuilder query = new StringBuilder();
////            query.append("SELECT account_id,IFNULL(regionTable.region_name,'') as regionName,company_name,status,user_count, user_limit,statsTable.created_on,user_punch_last_date,plan,");
////            query.append("user_punch_percentage FROM fieldsensestats.accounts_punchin_stats statsTable left outer join fieldsense.account_regions regionTable on statsTable.region_id_fk=regionTable.id ");
//            query.append("SELECT account_id,IFNULL(regionTable.region_name,'') as regionName,statsTable.company_name,accountTable.status,statsTable.user_count, statsTable.user_limit,statsTable.created_on,statsTable.user_punch_last_date,accountTable.plan,accountTable.start_date,");
//            query.append("statsTable.user_punch_percentage FROM fieldsensestats.accounts_punchin_stats statsTable left outer join fieldsense.accounts accountTable on statsTable.account_id=accountTable.id   left outer join fieldsense.account_regions regionTable on statsTable.region_id_fk=regionTable.id ");
//            String acname=allRequestParams.get("accname").trim();
//            String status=allRequestParams.get("status").trim();
//            String actfreq=allRequestParams.get("actfreq").trim();
//            String createdDateTime = allRequestParams.get("created_on").trim();
//            String startDateTime = allRequestParams.get("startdate").trim();
////                System.out.println("start date "+startDateTime);
//            String[]  createdDate = createdDateTime.split(" ");
//            String createddate = createdDate[0];
//            String[] startD = startDateTime.split(" ");
//            String startedDate = startD[0];
//            String strStartDate = null;
//            String endDate = null;
//            String SubscriptionDate =null;
//            String SubscriptionDate24hrs = null;
//            if(!createddate.equals(""))
//            {                 
//            strStartDate="";
//            //taking two days code
//            java.sql.Timestamp startDate = java.sql.Timestamp.valueOf(createddate+" 00:00:00");
//            createddate=startDate.toString();
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Calendar c3 = Calendar.getInstance();
//            c3.setTime(sdf.parse(createddate));
//            c3.add(Calendar.MINUTE, -330);  // to get time 5:30 hrs back.
//            strStartDate = sdf.format(c3.getTime());
//            //  
//            endDate = strStartDate;  // Start date
//            // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Calendar c = Calendar.getInstance();
//            c.setTime(sdf.parse(endDate));
//            c.add(Calendar.DATE, 1);  // number of days to add1970-01-01 05:30:00
//            endDate = sdf.format(c.getTime());  // dt is now the new date
//            
////            System.out.println("strStartDate "+strStartDate +"endDate "+endDate);
//            }           
//              if(!startedDate.equals(""))
//            {    
////                System.out.println("inside started date");
//            SubscriptionDate="";
//            //taking two days code
//            java.sql.Timestamp startDate1 = java.sql.Timestamp.valueOf(startedDate+" 00:00:00");
//            startedDate=startDate1.toString();
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Calendar c3 = Calendar.getInstance();
//            c3.setTime(sdf.parse(startedDate));
//            c3.add(Calendar.MINUTE, -330);  // to get time 5:30 hrs back.
//            SubscriptionDate = sdf.format(c3.getTime());
//            //  
//            SubscriptionDate24hrs = SubscriptionDate;  // Start date
//            // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Calendar c = Calendar.getInstance();
//            c.setTime(sdf.parse(SubscriptionDate24hrs));
//            c.add(Calendar.DATE, 1);  // number of days to add1970-01-01 05:30:00
//            SubscriptionDate24hrs = sdf.format(c.getTime());  // dt is now the new date
//            
////            System.out.println("SubscriptionDate "+SubscriptionDate +"SubscriptionDate24hrs "+SubscriptionDate24hrs);
//            }
//             
//            // int regionId=Integer.parseInt(allRequestParams.get("regionId").trim());
//            boolean accnameexist=false;
//            boolean actfreqexist=false;
//            boolean dateexist = false;
//            // added by nikhil :- 11th july 2017
//            boolean startdateexist = false;
//            boolean actregionexist=false;
//            // ended by nikhil
//            ArrayList list=new ArrayList();
//            if(!acname.equals("")){
//                accnameexist=true;
//                String acclike="%"+acname+"%";
//                list.add(acclike);
//                query.append(" where ");
//                query.append(" statsTable.company_name like ? ");
//            }
//            if(!status.equals("All")){
//                actfreqexist=true;
//                list.add(Integer.parseInt(status));
//                if(accnameexist==true){
//                    query.append("and ");
//                }else{
//                    query.append(" where ");
//                }
//                query.append(" statsTable.status = ? ");
//            }
//            if(!createddate.equals("")){
//              
//                dateexist=true;
//             
//                list.add(strStartDate);
//                list.add(endDate);
//                
//                if(accnameexist || actfreqexist){
//                    query.append("and ");
//                }else{
//                    query.append(" where ");
//                }
//                query.append(" statsTable.created_on between ? AND ? ");
//            }
//            //added by nikhil :- 11th july 2017
//             if(!startedDate.equals("")){
//              
//                startdateexist = true;
//             
//                list.add(SubscriptionDate);
//                list.add(SubscriptionDate24hrs);
//                
//                if(accnameexist || actfreqexist ||dateexist){
//                    query.append("and ");
//                }else{
//                    query.append(" where ");
//                }
//                query.append(" accountTable.start_date between ? AND ? ");
//            } 
//             // ended by nikhil
////        if(regionId !=0){
////            actregionexist=true;
////            //list.add(Integer.parseInt(status));
////            if(accnameexist || actfreqexist){
////                query.append("and ");
////            }else{
////                query.append(" where ");
////            } 
////            query.append(" region_id_fk="+regionId+" ");
////        }
//            if(!actfreq.equals("Any")){
//                list.add(Integer.parseInt(actfreq.split("AND")[0]));
//                list.add(Integer.parseInt(actfreq.split("AND")[1]));
//                if(accnameexist || actfreqexist ||dateexist){
//                    query.append("and ");
//                }else{
//                    query.append(" where ");
//                }
//                query.append(" statsTable.user_punch_percentage BETWEEN ? And ? ");
//            }
//            String sortcolindex=allRequestParams.get("order[0][column]");
////                System.out.println("sortcolindex ##"+sortcolindex);
//            if(sortcolindex==null){
//                query.append(" ORDER BY created_on DESC");
//            }else{
//                switch(Integer.parseInt(sortcolindex)){
//                    case 0:
//                        if(allRequestParams.get("order[0][dir]").equals("asc")){
//                            System.out.println("Inside company_name");
//                            query.append(" ORDER BY company_name");
//                        }else{
//                            System.out.println("Inside company_name");
//                            query.append(" ORDER BY company_name DESC");
//                        }
//                        break;
//                        
//                    case 2:
//                        if(allRequestParams.get("order[0][dir]").equals("asc")){
//                            System.out.println("Inside user_count");
//                            query.append(" ORDER BY user_count");
//                        }else{
//                            System.out.println("Inside user_count");
//                            query.append(" ORDER BY user_count DESC");
//                        }
//                        break;
//                    case 3:
//                        if(allRequestParams.get("order[0][dir]").equals("asc")){
//                            System.out.println("Inside user_limit");
//                            query.append(" ORDER BY user_limit");
//                        }else{
//                            System.out.println("Inside user_limit");
//                            query.append(" ORDER BY user_limit DESC");
//                        }
//                        break;
//                    case 4:
//                        if(allRequestParams.get("order[0][dir]").equals("asc")){
//                            System.out.println("Inside created_on");
//                            query.append(" ORDER BY created_on");
//                        }else{
//                            System.out.println("Inside created_on");
//                            query.append(" ORDER BY created_on DESC");
//                        }
//                        break;
//                    case 5:
//                        if(allRequestParams.get("order[0][dir]").equals("asc")){
//                            System.out.println("Inside start_date:");
//                            query.append(" ORDER BY start_date");
//                        }else{
//                            System.out.println("Inside start_date:");
//                            query.append(" ORDER BY start_date DESC");
//                        }
//                        break;    
////                    case 6:
////                        if(allRequestParams.get("order[0][dir]").equals("asc")){
////                            System.out.println("Inside status");
////                            query.append(" ORDER BY status DESC");
////                        }else{
////                            System.out.println("Inside status");
////                            query.append(" ORDER BY status ");
////                        }
////                        break;
//                    case 6:
//                        if(allRequestParams.get("order[0][dir]").equals("asc")){
//                            System.out.println("Inside user_punch_last_date");
//                            query.append(" ORDER BY user_punch_last_date");
//                        }else{
//                            System.out.println("Inside user_punch_last_date" );
//                            query.append(" ORDER BY user_punch_last_date DESC");
//                        }
//                        break;
//                    case 7:
//                        if(allRequestParams.get("order[0][dir]").equals("asc")){
//                            System.out.println("Inside status");
//                            query.append(" ORDER BY status DESC");
//                        }else{
//                            System.out.println("Inside status");
//                            query.append(" ORDER BY status ");
//                        }
//                        break;    
//                    case 8:
//                        if(allRequestParams.get("order[0][dir]").equals("asc")){
//                            System.out.println("Inside user_punch_percentage");
//                            query.append(" ORDER BY user_punch_percentage");
//                        }else{
//                            System.out.println("Inside user_punch_percentage");
//                            query.append(" ORDER BY user_punch_percentage DESC");
//                        }
//                        break;
//                        //commented by nikhil
//                    case 9:
//                        if(allRequestParams.get("order[0][dir]").equals("asc")){
//                            System.out.println("Inside region_name");
//                            query.append(" ORDER BY regionTable.region_name");
//                        }else{
//                            System.out.println("Inside region_name");
//                            query.append(" ORDER BY regionTable.region_name DESC");
//                        }
//                        break;
//                    default:
//                        System.out.println("Inside default");
//                        query.append(" ORDER BY created_on DESC");
//                        break;
//                }
//                   
//            }
//            //query.append(" LIMIT ? OFFSET ?");
//            final int start=Integer.parseInt(allRequestParams.get("start"));
//            final int length=Integer.parseInt(allRequestParams.get("length"));
//            //list.add(length);
//            //list.add(start);
//            Object param[] = new Object[list.size() ];
//            for(int i=0;i<list.size();i++){
//                param[i]=list.get(i);
//            }
//        
//                return jdbcTemplate.query(query.toString(), param, new RowMapper<AccountData>() {
//                    
//                    @Override
//                    public AccountData mapRow(ResultSet rs, int i) throws SQLException {
//                        if(i>=start && i<start+length){
//                             // Added by nikhil on 11th june 2017 :- plan   
//                            AccountData accountData = new AccountData();
//                            accountData.setId(rs.getInt("account_id"));
//                            accountData.setRegionName(rs.getString("regionName"));
//                            accountData.setAccountName(rs.getString("company_name"));
//                            accountData.setPlan(rs.getString("plan"));
//                            int accountStatus = rs.getInt("status");
////                            System.out.println("accountStatus%% "+accountStatus);
//                            if(accountStatus ==1){
//                                accountData.setIsActive(true);
//                            }else{
//                                accountData.setIsActive(false);
//                            }
//                            accountData.setUsersCount(rs.getInt("user_count"));
//                            accountData.setUsersLimit(rs.getInt("user_limit"));
//                            accountData.setCreatedOn(rs.getTimestamp("created_on"));
//                            accountData.setStartdate(rs.getTimestamp("start_date"));
//                            accountData.setlastPunchInDateOfanAccount(rs.getTimestamp("user_punch_last_date"));
//                            accountData.setActivityFrequency(rs.getInt("user_punch_percentage"));
//                            if(accountData.getId()==0 ||accountData.getRegionName()== null||accountData.getAccountName()==null||accountData.getPlan()==null ||accountData.getCreatedOn()==null||accountData.getStartdate() == null||accountData.getlastPunchInDateOfanAccount()== null )
//                            {
//                                System.out.println("accountData.getId()"+accountData.getId()+"accountData.getRegionName()"+accountData.getRegionName()+"accountData.getAccountName()"+accountData.getAccountName()+"accountData.getPlan()"+accountData.getPlan()+"accountData.getCreatedOn()"+accountData.getCreatedOn()+"accountData.getStartdate()"+accountData.getStartdate()+"accountData.getlastPunchInDateOfanAccount()"+accountData.getlastPunchInDateOfanAccount());
//                            }
//                            return accountData;
//                        }else{
//                            AccountData accountData = new AccountData();
//                            accountData.setCreatedOn(Timestamp.valueOf("1111-11-11 11:11:11"));
//                            return accountData;
//                        }
//                    }
//                });
//            } catch (Exception e) {
//                log4jLog.info("Inside FieldSenseStatsUtils class  selectAccountsData() method" + e);
//                e.printStackTrace();
//                return new ArrayList<AccountData>();
//            }
//    }
//    
//    
// 
     @Override
    public List<AccountData> selectAccountsData(@RequestParam Map<String,String> allRequestParams) {
            try {
              // Added by nikhil on 11th june 2017 :- plan & startDate columns in query   
            StringBuilder query = new StringBuilder();
//            query.append("SELECT account_id,IFNULL(regionTable.region_name,'') as regionName,company_name,status,user_count, user_limit,statsTable.created_on,user_punch_last_date,plan,");
//            query.append("user_punch_percentage FROM fieldsensestats.accounts_punchin_stats statsTable left outer join fieldsense.account_regions regionTable on statsTable.region_id_fk=regionTable.id ");
            query.append("SELECT account_id,IFNULL(regionTable.region_name,'') as regionName,statsTable.company_name,accountTable.status,statsTable.user_count, statsTable.user_limit,statsTable.created_on,statsTable.user_punch_last_date,accountTable.plan,accountTable.start_date,");
//            query.append("statsTable.user_punch_percentage FROM fieldsensestats.accounts_punchin_stats statsTable left outer join fieldsense.accounts accountTable on statsTable.account_id=accountTable.id   left outer join fieldsense.account_regions regionTable on statsTable.region_id_fk=regionTable.id ");
            query.append("statsTable.user_punch_percentage FROM fieldsensestats.accounts_punchin_stats statsTable INNER join fieldsense.accounts accountTable on statsTable.account_id=accountTable.id   left outer join fieldsense.account_regions regionTable on statsTable.region_id_fk=regionTable.id "); // left join changed to inner join
            String acname=allRequestParams.get("accname").trim();
            String status=allRequestParams.get("status").trim();
            String actfreq=allRequestParams.get("actfreq").trim();
            String plan= allRequestParams.get("plan").trim();
//                System.out.println("plan++"+plan);
            String createdDateTime = allRequestParams.get("created_on").trim();
            String startDateTime = allRequestParams.get("startdate").trim();
            int regionId = Integer.parseInt(allRequestParams.get("regionId").trim());                                       
//                System.out.println("regionId $$"+regionId);
//                System.out.println("start date "+startDateTime);
            String[]  createdDate = createdDateTime.split(" ");
            String createddate = createdDate[0];
            String[] startD = startDateTime.split(" ");
            String startedDate = startD[0];
            String strStartDate = null;
            String endDate = null;
            String SubscriptionDate =null;
            String SubscriptionDate24hrs = null;
            if(!createddate.equals(""))
            {                 
            strStartDate="";
            //taking two days code
            java.sql.Timestamp startDate = java.sql.Timestamp.valueOf(createddate+" 00:00:00");
            createddate=startDate.toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c3 = Calendar.getInstance();
            c3.setTime(sdf.parse(createddate));
            c3.add(Calendar.MINUTE, -330);  // to get time 5:30 hrs back.
            strStartDate = sdf.format(c3.getTime());
            //  
            endDate = strStartDate;  // Start date
            // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(endDate));
            c.add(Calendar.DATE, 1);  // number of days to add1970-01-01 05:30:00
            endDate = sdf.format(c.getTime());  // dt is now the new date
            
//            System.out.println("strStartDate "+strStartDate +"endDate "+endDate);
            }           
              if(!startedDate.equals(""))
            {    
//                System.out.println("inside started date");
            SubscriptionDate="";
            //taking two days code
            java.sql.Timestamp startDate1 = java.sql.Timestamp.valueOf(startedDate+" 00:00:00");
            startedDate=startDate1.toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c3 = Calendar.getInstance();
            c3.setTime(sdf.parse(startedDate));
            c3.add(Calendar.MINUTE, -330);  // to get time 5:30 hrs back.
            SubscriptionDate = sdf.format(c3.getTime());
            //  
            SubscriptionDate24hrs = SubscriptionDate;  // Start date
            // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(SubscriptionDate24hrs));
            c.add(Calendar.DATE, 1);  // number of days to add1970-01-01 05:30:00
            SubscriptionDate24hrs = sdf.format(c.getTime());  // dt is now the new date
            
//            System.out.println("SubscriptionDate "+SubscriptionDate +"SubscriptionDate24hrs "+SubscriptionDate24hrs);
            }
             
            // int regionId=Integer.parseInt(allRequestParams.get("regionId").trim());
            boolean accnameexist=false;
            boolean actfreqexist=false;
            boolean dateexist = false;
            // added by nikhil :- 11th july 2017
            boolean startdateexist = false;
            boolean actregionexist=false;
            boolean planexist = false;
            
            // ended by nikhil
            ArrayList list=new ArrayList();
            if(!acname.equals("")){
                accnameexist=true;
                String acclike="%"+acname+"%";
                list.add(acclike);
                query.append(" where ");
                query.append(" statsTable.company_name like ? ");
            }
            //added by nikhil      
            if(!plan.equals("All")){
                planexist = true;
                list.add(plan);
                if(accnameexist ){
//                    System.out.println("inside plan where");
                    query.append("and ");
                }else{
//                    System.out.println("inside where");
                    query.append(" where ");
                }
                query.append(" accountTable.plan = ?");
            }
            //ended by nikhil
            if(!status.equals("All")){
                actfreqexist=true;
                list.add(Integer.parseInt(status));
                if(accnameexist==true){
                    query.append("and ");
                }else{
                    query.append(" where ");
                }
                query.append(" statsTable.status = ? ");
            }
            if(!createddate.equals("")){
              
                dateexist=true;
             
                list.add(strStartDate);
                list.add(endDate);
                
                if(accnameexist || actfreqexist){
                    query.append("and ");
                }else{
                    query.append(" where ");
                }
                query.append(" statsTable.created_on between ? AND ? ");
            }
            //added by nikhil :- 11th july 2017
             if(!startedDate.equals("")){
              
                startdateexist = true;
             
                list.add(SubscriptionDate);
                list.add(SubscriptionDate24hrs);
                
                if(accnameexist || actfreqexist ||dateexist){
                    query.append("and ");
                }else{
                    query.append(" where ");
                }
                query.append(" accountTable.start_date between ? AND ? ");
            } 
             // ended by nikhil
        if(regionId !=0){
            actregionexist=true;
            //list.add(Integer.parseInt(status));
            if(accnameexist || actfreqexist ||dateexist || startdateexist ){
                query.append("and ");
//                System.out.println("$$and%%");
            }else{
                query.append(" where ");
//                 System.out.println("$$where%%");
            } 
           query.append(" accountTable.region_id_fk="+regionId+" ");
            //query.append(" region_id_fk = ?");
        }   
            if(!actfreq.equals("Any")){
                list.add(Integer.parseInt(actfreq.split("AND")[0]));
                list.add(Integer.parseInt(actfreq.split("AND")[1]));
                if(accnameexist || actfreqexist ||dateexist ||planexist){
                    query.append("and ");
                }else{
                    query.append(" where ");
                }
                query.append(" statsTable.user_punch_percentage BETWEEN ? And ? ");
            }     
            String sortcolindex=allRequestParams.get("order[0][column]");
//                System.out.println("sortcolindex ##"+sortcolindex);
            if(sortcolindex==null){
                query.append(" ORDER BY created_on DESC");
            }else{
                switch(Integer.parseInt(sortcolindex)){
                    case 0:
                        if(allRequestParams.get("order[0][dir]").equals("asc")){
//                            System.out.println("Inside company_name");
                            query.append(" ORDER BY company_name");
                        }else{
//                            System.out.println("Inside company_name");
                            query.append(" ORDER BY company_name DESC");
                        }
                        break;
                    case 1:
                        if(allRequestParams.get("order[0][dir]").equals("asc")){
//                            System.out.println("Inside plan");
                            query.append(" ORDER BY plan");
                        }else{
//                            System.out.println("Inside company_name");
                            query.append(" ORDER BY plan DESC");
                        }
                        break;
                        
                    case 2:
                        if(allRequestParams.get("order[0][dir]").equals("asc")){
//                            System.out.println("Inside user_count");
                            query.append(" ORDER BY user_count");
                        }else{
//                            System.out.println("Inside user_count");
                            query.append(" ORDER BY user_count DESC");
                        }
                        break;
                    case 3:
                        if(allRequestParams.get("order[0][dir]").equals("asc")){
//                            System.out.println("Inside user_limit");
                            query.append(" ORDER BY user_limit");
                        }else{
//                            System.out.println("Inside user_limit");
                            query.append(" ORDER BY user_limit DESC");
                        }
                        break;
                    case 4:
                        if(allRequestParams.get("order[0][dir]").equals("asc")){
//                            System.out.println("Inside created_on");
                            query.append(" ORDER BY created_on");
                        }else{
//                            System.out.println("Inside created_on");
                            query.append(" ORDER BY created_on DESC");
                        }
                        break;
                    case 5:
                        if(allRequestParams.get("order[0][dir]").equals("asc")){
//                            System.out.println("Inside start_date:");
                            query.append(" ORDER BY start_date");
                        }else{
//                            System.out.println("Inside start_date:");
                            query.append(" ORDER BY start_date DESC");
                        }
                        break;    
//                    case 6:
//                        if(allRequestParams.get("order[0][dir]").equals("asc")){
//                            System.out.println("Inside status");
//                            query.append(" ORDER BY status DESC");
//                        }else{
//                            System.out.println("Inside status");
//                            query.append(" ORDER BY status ");
//                        }
//                        break;
                    case 6:
                        if(allRequestParams.get("order[0][dir]").equals("asc")){
//                            System.out.println("Inside user_punch_last_date");
                            query.append(" ORDER BY user_punch_last_date");
                        }else{
//                            System.out.println("Inside user_punch_last_date" );
                            query.append(" ORDER BY user_punch_last_date DESC");
                        }
                        break;
                    case 7:
                        if(allRequestParams.get("order[0][dir]").equals("asc")){
//                            System.out.println("Inside status");
                            query.append(" ORDER BY status DESC");
                        }else{
//                            System.out.println("Inside status");
                            query.append(" ORDER BY status ");
                        }
                        break;    
                    case 8:
                        if(allRequestParams.get("order[0][dir]").equals("asc")){
//                            System.out.println("Inside user_punch_percentage");
                            query.append(" ORDER BY user_punch_percentage");
                        }else{
//                            System.out.println("Inside user_punch_percentage");
                            query.append(" ORDER BY user_punch_percentage DESC");
                        }
                        break;
                        //commented by nikhil
                    case 9:
                        if(allRequestParams.get("order[0][dir]").equals("asc")){
//                            System.out.println("Inside region_name");
                            query.append(" ORDER BY regionTable.region_name");
                        }else{
//                            System.out.println("Inside region_name");
                            query.append(" ORDER BY regionTable.region_name DESC");
                        }
                        break;
                    default:
//                        System.out.println("Inside default");
                        query.append(" ORDER BY created_on DESC");
                        break;
                }
                   
            }
            //query.append(" LIMIT ? OFFSET ?");
            final int start=Integer.parseInt(allRequestParams.get("start"));
            final int length=Integer.parseInt(allRequestParams.get("length"));
            //list.add(length);
            //list.add(start);
            Object param[] = new Object[list.size() ];
            for(int i=0;i<list.size();i++){
                param[i]=list.get(i);
            }
                System.out.println("selectAccountsData, query : "+query.toString());
                return jdbcTemplate.query(query.toString(), param, new RowMapper<AccountData>() {
                    
                    @Override
                    public AccountData mapRow(ResultSet rs, int i) throws SQLException {
                        if(i>=start && i<start+length){
                             // Added by nikhil on 11th june 2017 :- plan   
                            AccountData accountData = new AccountData();
                            accountData.setId(rs.getInt("account_id"));
                            accountData.setRegionName(rs.getString("regionName"));
                            accountData.setAccountName(rs.getString("company_name"));
                            accountData.setPlan(rs.getString("plan"));
                            int accountStatus = rs.getInt("status");
//                            System.out.println("accountStatus%% "+accountStatus);
                            if(accountStatus ==1){
                                accountData.setIsActive(true);
                            }else{
                                accountData.setIsActive(false);
                            }
                            accountData.setUsersCount(rs.getInt("user_count"));
                            accountData.setUsersLimit(rs.getInt("user_limit"));
                            accountData.setCreatedOn(rs.getTimestamp("created_on"));
                            accountData.setStartdate(rs.getTimestamp("start_date"));
                            accountData.setlastPunchInDateOfanAccount(rs.getTimestamp("user_punch_last_date"));
                            accountData.setActivityFrequency(rs.getInt("user_punch_percentage"));
                            if(accountData.getId()==0 ||accountData.getRegionName()== null||accountData.getAccountName()==null||accountData.getPlan()==null ||accountData.getCreatedOn()==null||accountData.getStartdate() == null||accountData.getlastPunchInDateOfanAccount()== null )
                            {
//                                System.out.println("accountData.getId()"+accountData.getId()+"accountData.getRegionName()"+accountData.getRegionName()+"accountData.getAccountName()"+accountData.getAccountName()+"accountData.getPlan()"+accountData.getPlan()+"accountData.getCreatedOn()"+accountData.getCreatedOn()+"accountData.getStartdate()"+accountData.getStartdate()+"accountData.getlastPunchInDateOfanAccount()"+accountData.getlastPunchInDateOfanAccount());
                            }
                            return accountData;
                        }else{
                            AccountData accountData = new AccountData();
                            accountData.setCreatedOn(Timestamp.valueOf("1111-11-11 11:11:11"));
                            return accountData;
                        }
                    }
                });
            } catch (Exception e) {
                log4jLog.info("Inside FieldSenseStatsUtils class  selectAccountsData() method" + e);
                e.printStackTrace();
                return new ArrayList<AccountData>();
            }
    }
    /**
     *
     * @param accountId
     * @return
     */
    @Override
    public boolean isStatRecordAvailableForTheDate(int accountId) {
        String query = "SELECT COUNT(id) FROM fieldsensestats.fieldsensestats WHERE DATE(created_on)=CURDATE() AND account_id_fk=? ";
        log4jLog.info("Inside FieldSenseStatsUtils class  isStatRecordAvailableForTheDate() method" + query);
        Object param[] = new Object[]{accountId};
        try {
            if (jdbcTemplate.queryForObject(query, param, Integer.class) == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info("Inside FieldSenseStatsUtils class  isStatRecordAvailableForTheDate() method" + e);
            return false;
        }
    }

    /**
     *
     * @return
     */
    public int getAccountCount() {
        String query = "SELECT COUNT(account_id) FROM fieldsensestats.accounts_punchin_stats ;";
        log4jLog.info("Inside FieldSenseStatsUtils class  getAccountCount() method" + query);
        Object param[] = new Object[]{};
        try {
            int totalCount=jdbcTemplate.queryForObject(query, param, Integer.class) ;
             return totalCount;
        } catch (Exception e) {
            log4jLog.info("Inside FieldSenseStatsUtils class  isStatRecordAvailableForTheDate() method" + e);
            return 0;
        }
    }
    public List<AccountUsers> selectUsersData(Map<String, String> allRequestParams) {
            StringBuilder query = new StringBuilder();  
//<<<<<<< .working
//            query.append("select acc.status,u.full_name,u.email_address,u.id,acc.company_name,ar.region_name,u.created_on from users as u left join accounts as acc on u.account_id_fk=acc.id");
//            query.append(" left join account_regions as ar on acc.region_id_fk=ar.id where u.role!=0 and u.full_name like ? OR acc.company_name like ? OR u.email_address like ? OR ar.region_name like ? ");//where u.role!=0 added bu siddhesh.
            query.append("select acc.status,u.mobile_number,u.full_name,u.email_address,u.id,acc.company_name,ar.region_name,u.created_on from users as u left join accounts as acc on u.account_id_fk=acc.id");
            query.append(" left join account_regions as ar on acc.region_id_fk=ar.id where u.role != 0 and (u.mobile_number like ? OR u.full_name like ? OR acc.company_name like ? OR u.email_address like ? OR ar.region_name like ?) ");

            String acname=allRequestParams.get("searchText").trim();
            acname="%"+acname+"%";
            final int start=Integer.parseInt(allRequestParams.get("start"));
            final int length=Integer.parseInt(allRequestParams.get("length"));
            String sortcolindex=allRequestParams.get("order[0][column]");
        if(sortcolindex==null){
            query.append(" ORDER BY created_on DESC");
        }else{
            switch(Integer.parseInt(sortcolindex)){
                case 0:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append(" ORDER BY u.full_name");
                    }else{
                        query.append(" ORDER BY u.full_name DESC");
                    }
                    break;
                case 1:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append(" ORDER BY acc.company_name");
                    }else{
                        query.append(" ORDER BY acc.company_name DESC");
                    }
                    break;
                    case 2:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append(" ORDER BY u.mobile_number");
                    }else{
                        query.append(" ORDER BY u.mobile_number DESC");
                    }
                    break;
                case 3:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append(" ORDER BY u.email_address");
                    }else{
                        query.append(" ORDER BY u.email_address DESC");
                    }
                    break;
                case 4:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append(" ORDER BY created_on");
                    }else{
                        query.append(" ORDER BY created_on DESC");
                    }
                    break;
                case 5:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append(" ORDER BY status DESC");
                    }else{
                        query.append(" ORDER BY status ");
                    }
                    break;
                case 6:
                    if(allRequestParams.get("order[0][dir]").equals("asc")){
                        query.append(" ORDER BY ar.region_name");
                    }else{
                        query.append(" ORDER BY ar.region_name DESC");
                    }
                    break;
                default:
                    query.append(" ORDER BY created_on DESC ");
                    break;   
            }
            
        }    
    //s      query.append(" limit ? offset ?");
            //
            Object param[] = new Object[]{acname,acname,acname,acname,acname};
                try{
                   
                     return jdbcTemplate.query(query.toString(), param, new RowMapper<AccountUsers>() {
                     @Override
                     public AccountUsers mapRow(ResultSet rs, int i) throws SQLException {
                         if(i>=start && i<start+length){
                        AccountUsers accountUsers = new AccountUsers();
                        accountUsers.setMobileNo(rs.getString("u.mobile_number"));
                      accountUsers.setAccountName(rs.getString("acc.company_name"));
                      accountUsers.setUserName(rs.getString("u.full_name"));
                      accountUsers.setActive(rs.getInt("acc.status"));
                      accountUsers.setEmailId(rs.getString("email_address"));
                      accountUsers.setRegion(rs.getString("ar.region_name"));
                      accountUsers.setCreatedDate(rs.getTimestamp("u.created_on"));
                        return accountUsers;
                         }else{
                             AccountUsers accountUsers = new AccountUsers();
                             accountUsers.setCheckId(0);
                         return accountUsers;
                         }
                }
            });
        }catch(Exception e){
        e.printStackTrace();
        return new ArrayList<AccountUsers>();
        }
        
    }
    
    /**
     * @added by jyoti, 30-07-2018
     * @param fromDate
     * @param toDate
     * @return
     */
    @Override
    public List<DIYData> getDiy4StepsData(String fromDate, String toDate) {
        try {
            String query = "SELECT id, first_name, last_name, full_name, email_address, country_code, mobile_number, gender, designation, isvalid_email_address, company_name, address1,"
                    + " city, country, zip_code, company_phone_number1, company_website, state, industry, plan, user_limit, is_mobile_valid, is_terms_condition_agreed, is_newsletter_opt_in,"
                    + " g_source, g_feature, device, step_number, otp, IFNULL(otp_expiry,'1111-11-11 00:00:00') AS otp_expiry, created_on, is_account_created, IFNULL(mail_expired_on, '1111-11-11 00:00:00') AS mail_expired_on FROM diy_account_creation WHERE created_on BETWEEN ? AND ? ";
            Object[] param = new Object[]{fromDate, toDate};
            return jdbcTemplate.query(query, param, new RowMapper<DIYData>() {
                @Override
                public DIYData mapRow(ResultSet rs, int i) throws SQLException {
                    DIYData user = new DIYData();
                    user.setFirst_name(rs.getString("first_name"));
                    user.setLast_name(rs.getString("last_name"));
                    user.setFull_name(rs.getString("full_name"));
                    user.setEmail_address(rs.getString("email_address"));
                    user.setCountry_code(rs.getString("country_code"));
                    user.setMobile_number(rs.getString("mobile_number"));
                    user.setGender(rs.getInt("gender"));
                    user.setDesignation(rs.getString("designation"));
                    user.setCompany_name(rs.getString("company_name"));
                    user.setAddress1(rs.getString("address1"));
                    user.setCity(rs.getString("city"));
                    user.setCountry(rs.getString("country"));
                    user.setZip_code(rs.getString("zip_code"));
                    user.setCompany_phone_number1(rs.getString("company_phone_number1"));
                    user.setCompany_website(rs.getString("company_website"));
                    user.setState(rs.getString("state"));
                    user.setIndustry(rs.getString("industry"));
                    user.setPlan(rs.getString("plan"));
                    user.setUser_limit(rs.getInt("user_limit"));
                    user.setIsMailValid(rs.getInt("isvalid_email_address"));
                    user.setIs_mobile_valid(rs.getInt("is_mobile_valid"));
                    user.setIs_terms_condition_agreed(rs.getInt("is_terms_condition_agreed"));
                    user.setIs_newsletter_opt_in(rs.getInt("is_newsletter_opt_in"));
                    user.setIs_account_created(rs.getInt("is_account_created"));
                    user.setgSource(rs.getString("g_source"));
                    user.setgFeature(rs.getString("g_feature"));
                    user.setDevice(rs.getString("device"));
                    user.setMail_expired_on(rs.getTimestamp("mail_expired_on"));
                    user.setCreated_on(rs.getTimestamp("created_on"));
                    user.setOtp(rs.getString("otp"));
                    user.setOtp_expiry(rs.getTimestamp("otp_expiry"));
                    user.setStep_number(rs.getInt("step_number"));
                    return user;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    
//Commented by siddhesh not required code.
//   public Stats defaultPassword(int id, int accountId){
//       Stats stats = new Stats();
//       String password ="68b13df961e262e3";
//       
//       String query = "UPDATE users SET password = '"+password+"' WHERE id=?";
//       log4jLog.info(" updateUser " + query);
//       Object param[] = new Object[]{id};
//       
//       try {
//            if (jdbcTemplate.update(query, param) > 0) {
////                System.out.println("reached database");
//                return stats;
//            } else {
//                return new Stats();
//            }
//        } catch (Exception e) {
//            log4jLog.info(" deleteCustomer " + e);
////            e.printStackTrace();
//            return new Stats();
//        }
//   
//   }
    
}
