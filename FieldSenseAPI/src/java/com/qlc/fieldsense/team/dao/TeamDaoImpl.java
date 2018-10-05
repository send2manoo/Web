/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.team.dao;

import com.qlc.fieldsense.expense.model.Expense;
import com.qlc.fieldsense.location.model.Location;
import com.qlc.fieldsense.report.model.Attendance;
import com.qlc.fieldsense.team.model.OraganizationChart;
import com.qlc.fieldsense.team.model.Team;
import com.qlc.fieldsense.team.model.TeamMember;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.userKey.dao.UserKayDaoImpl;
import com.qlc.fieldsense.userKey.model.UserKey;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.jivesoftware.smackx.bytestreams.ibb.packet.Data;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author mayank
 */
public class TeamDaoImpl implements TeamDao {

    public static final Logger log4jLog = Logger.getLogger(" TeamDaoImpl ");

    @Override
    public Team selectTeam(int teamId, int accountId) {
//        String query = "SELECT id, team_name, description, user_id_fk, is_active, created_on, created_by_id_fk FROM teams WHERE id =? ";
        StringBuilder query = new StringBuilder();
        query.append("SELECT t.id, t.team_name, t.description, t.user_id_fk, t.is_active, t.created_on, t.created_by_id_fk ,");
        query.append("u.first_name,u.last_name");
        query.append(" FROM teams t INNER JOIN fieldsense.users u ON t.user_id_fk=u.id");
        query.append(" WHERE t.id =?");
        log4jLog.info(" selectTeam " + query);
        Object[] param = new Object[]{teamId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query.toString(), param, new RowMapper<Team>() {

                @Override
                public Team mapRow(ResultSet rs, int i) throws SQLException {
                    Team team = new Team();
                    team.setId(rs.getInt("id"));
                    team.setTeamName(rs.getString("team_name"));
                    team.setDescription(rs.getString("description"));
                    team.setIsActive(rs.getInt("is_active"));

                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setAccountId(0);
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmailAddress("");
                    user.setPassword("");
                    user.setMobileNo("");
                    user.setGender(0);
                    user.setRole(0);
                    user.setActive(false);
                    user.setLastLoggedOn(new Timestamp(0));
                    user.setLastKnownLocationTime(new Timestamp(0));
                    user.setLastKnownLocation("");
                    user.setCreatedOn(new Timestamp(0));
                    user.setCreatedBy(0);
                    team.setOwnerId(user);

                    User createdBy = new User();
                    createdBy.setId(rs.getInt("created_by_id_fk"));

                    createdBy.setAccountId(0);
                    createdBy.setFirstName("");
                    createdBy.setLastName("");
                    createdBy.setEmailAddress("");
                    createdBy.setPassword("");
                    createdBy.setMobileNo("");
                    createdBy.setGender(0);
                    createdBy.setRole(0);
                    createdBy.setActive(false);
                    createdBy.setLastLoggedOn(new Timestamp(0));
                    createdBy.setLastKnownLocationTime(new Timestamp(0));
                    createdBy.setLastKnownLocation("");
                    createdBy.setCreatedOn(new Timestamp(0));
                    createdBy.setCreatedBy(0);
                    team.setCreatedBy(createdBy);

                    team.setCreatedOn(rs.getTimestamp("created_on"));
                    return team;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            log4jLog.info(" selectTeam " + e);
            return new Team();
        } catch (Exception e) {
            log4jLog.info(" selectTeam " + e);
            return new Team();
        }
    }

    @Override
    public List<Team> selectTeam(int accountId) {
//        String query = "SELECT id, team_name, description, user_id_fk, is_active, created_on, created_by_id_fk FROM teams";
        StringBuilder query = new StringBuilder();
        query.append("SELECT t.id, t.team_name, t.description, t.user_id_fk owner_id, t.is_active, t.created_on,");
        query.append("t.created_by_id_fk,u.first_name owner_first_name,u.last_name owner_last_name ,count(tm.id) teamMembersCount FROM teams t");
        query.append(" INNER JOIN fieldsense.users u ON t.user_id_fk=u.id");
        query.append(" LEFT JOIN team_members tm ON  t.id=tm.team_id_fk");
        query.append(" GROUP BY tm.team_id_fk");

        log4jLog.info(" selectTeam " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), new RowMapper<Team>() {

                @Override
                public Team mapRow(ResultSet rs, int i) throws SQLException {
                    Team team = new Team();
                    team.setId(rs.getInt("id"));
                    team.setTeamName(rs.getString("team_name"));
                    team.setDescription(rs.getString("description"));
                    team.setIsActive(rs.getInt("is_active"));

                    User user = new User();
                    user.setId(rs.getInt("owner_id"));

                    user.setAccountId(0);
                    user.setFirstName(rs.getString("owner_first_name"));
                    user.setLastName(rs.getString("owner_last_name"));
                    user.setEmailAddress("");
                    user.setPassword("");
                    user.setMobileNo("");
                    user.setGender(0);
                    user.setRole(0);
                    user.setActive(false);
                    user.setLastLoggedOn(new Timestamp(0));
                    user.setLastKnownLocationTime(new Timestamp(0));
                    user.setLastKnownLocation("");
                    user.setCreatedOn(new Timestamp(0));
                    user.setCreatedBy(0);

                    team.setOwnerId(user);

                    User createdBy = new User();
                    createdBy.setId(rs.getInt("created_by_id_fk"));

                    createdBy.setAccountId(0);
                    createdBy.setFirstName("");
                    createdBy.setLastName("");
                    createdBy.setEmailAddress("");
                    createdBy.setPassword("");
                    createdBy.setMobileNo("");
                    createdBy.setGender(0);
                    createdBy.setRole(0);
                    createdBy.setActive(false);
                    createdBy.setLastLoggedOn(new Timestamp(0));
                    createdBy.setLastKnownLocationTime(new Timestamp(0));
                    createdBy.setLastKnownLocation("");
                    createdBy.setCreatedOn(new Timestamp(0));
                    createdBy.setCreatedBy(0);

                    team.setCreatedBy(createdBy);

                    team.setCreatedOn(rs.getTimestamp("created_on"));
                    team.setTeamMembersCount(rs.getInt("teamMembersCount"));
                    return team;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            log4jLog.info(" selectTeam " + e);
            return new ArrayList<Team>();
        } catch (Exception e) {
            log4jLog.info(" selectTeam " + e);
            return new ArrayList<Team>();
        }
    }

    @Override
    public List<Team> selectTeamByStatus(int status, int accountId) {
        String query = "SELECT id, team_name, description, user_id_fk, is_active, created_on, created_by_id_fk FROM teams WHERE is_active=?";
        log4jLog.info(" selectTeamByStatus " + query);
        Object[] param = new Object[]{status};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<Team>() {

                @Override
                public Team mapRow(ResultSet rs, int i) throws SQLException {
                    Team team = new Team();
                    team.setId(rs.getInt("id"));
                    team.setTeamName(rs.getString("team_name"));
                    team.setDescription(rs.getString("description"));
                    team.setIsActive(rs.getInt("is_active"));

                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setAccountId(0);
                    user.setFirstName("");
                    user.setLastName("");
                    user.setEmailAddress("");
                    user.setPassword("");
                    user.setMobileNo("");
                    user.setGender(0);
                    user.setRole(0);
                    user.setActive(false);
                    user.setLastLoggedOn(new Timestamp(0));
                    user.setLastKnownLocationTime(new Timestamp(0));
                    user.setLastKnownLocation("");
                    user.setCreatedOn(new Timestamp(0));
                    user.setCreatedBy(0);
                    team.setOwnerId(user);

                    User createdBy = new User();
                    createdBy.setId(rs.getInt("created_by_id_fk"));
                    createdBy.setAccountId(0);
                    createdBy.setFirstName("");
                    createdBy.setLastName("");
                    createdBy.setEmailAddress("");
                    createdBy.setPassword("");
                    createdBy.setMobileNo("");
                    createdBy.setGender(0);
                    createdBy.setRole(0);
                    createdBy.setActive(false);
                    createdBy.setLastLoggedOn(new Timestamp(0));
                    createdBy.setLastKnownLocationTime(new Timestamp(0));
                    createdBy.setLastKnownLocation("");
                    createdBy.setCreatedOn(new Timestamp(0));
                    createdBy.setCreatedBy(0);
                    team.setCreatedBy(createdBy);

                    team.setCreatedOn(rs.getTimestamp("created_on"));
                    return team;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            log4jLog.info(" selectTeamByStatus " + e);
            return new ArrayList<Team>();
        } catch (Exception e) {
            log4jLog.info(" selectTeamByStatus " + e);
            return new ArrayList<Team>();
        }
    }

    @Override
    public int insertTeam(Team team, int accountId) {
        String query = "INSERT INTO teams(team_name, description,user_id_fk,is_active,created_on,created_by_id_fk) VALUES(?,?,?,?,now(),?)";
        log4jLog.info(" insertTeam " + query);
        Object[] param = new Object[]{team.getTeamName(), team.getDescription(), team.getOwnerId().getId(), team.getIsActive(), team.getCreatedBy().getId()};
        try {
            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    String query1 = "SELECT MAX(id) FROM teams";
                    log4jLog.info(" insertTeam " + query1);
                    try {
                        return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query1, Integer.class);
                    } catch (Exception e) {
                        log4jLog.info(" insertTeam " + e);
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            log4jLog.info(" insertTeam " + e);
//            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public boolean updateTeam(Team team, int accountId) {
        String query = "UPDATE teams SET team_name =? , description = ? ,is_active =? ,user_id_fk=? WHERE id=?";
        log4jLog.info(" updateTeam " + query);
        Object[] param = new Object[]{team.getTeamName(), team.getDescription(), team.getIsActive(), team.getOwnerId().getId(), team.getId()};

        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateTeam " + e);
            return false;
        }
    }

    @Override
    public boolean deleteTeam(int id, int accountId) {
        String query = "DELETE FROM teams WHERE id=?";
        log4jLog.info(" deleteTeam " + query);
        Object[] param = new Object[]{id};

        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" deleteTeam " + e);
            return false;
        }
    }
  
    public boolean updateTeamCSV(String CSV,int teamId, int accountId) {
        String query = "UPDATE teams SET team_position_csv=? WHERE id=?";                  //UPDATE team_members SET user_id_fk=? WHERE team_id_fk=?
        log4jLog.info(" deleteTeam " + query);
        Object[] param = new Object[]{CSV,teamId};

        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" 1 " + e);
            return false;
        }
    }
    

    @Override
    public List<Team> selectTeams(int userId, int accountId) {
//        String query = "SELECT id, team_id_fk, user_id_fk, created_on, created_by_id_fk FROM teams WHERE user_id_fk=?";
        String query = "SELECT id, team_name, description, user_id_fk, is_active, created_on, created_by_id_fk FROM teams WHERE user_id_fk=?";
        log4jLog.info(" selectTeams " + query);
        Object[] param = new Object[]{userId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<Team>() {

                @Override
                public Team mapRow(ResultSet rs, int i) throws SQLException {
                    Team team = new Team();
                    team.setId(rs.getInt("id"));
                    team.setTeamName(rs.getString("team_name"));
                    team.setDescription(rs.getString("description"));
                    team.setIsActive(rs.getInt("is_active"));

                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setAccountId(0);
                    user.setFirstName("");
                    user.setLastName("");
                    user.setEmailAddress("");
                    user.setPassword("");
                    user.setMobileNo("");
                    user.setGender(0);
                    user.setRole(0);
                    user.setActive(false);
                    user.setLastLoggedOn(new Timestamp(0));
                    user.setLastKnownLocationTime(new Timestamp(0));
                    user.setLastKnownLocation("");
                    user.setCreatedOn(new Timestamp(0));
                    user.setCreatedBy(0);
                    team.setOwnerId(user);

                    User createdBy = new User();
                    createdBy.setId(rs.getInt("created_by_id_fk"));
                    createdBy.setAccountId(0);
                    createdBy.setFirstName("");
                    createdBy.setLastName("");
                    createdBy.setEmailAddress("");
                    createdBy.setPassword("");
                    createdBy.setMobileNo("");
                    createdBy.setGender(0);
                    createdBy.setRole(0);
                    createdBy.setActive(false);
                    createdBy.setLastLoggedOn(new Timestamp(0));
                    createdBy.setLastKnownLocationTime(new Timestamp(0));
                    createdBy.setLastKnownLocation("");
                    createdBy.setCreatedOn(new Timestamp(0));
                    createdBy.setCreatedBy(0);
                    team.setCreatedBy(createdBy);

                    team.setCreatedOn(rs.getTimestamp("created_on"));
                    return team;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            log4jLog.info(" selectTeams " + e);
            return new ArrayList<Team>();
        } catch (Exception e) {
            log4jLog.info(" selectTeams " + e);
//            e.printStackTrace();
            return new ArrayList<Team>();
        }
    }

    @Override
    public TeamMember selectTeamMember(int teamMemberId, int accountId) {
        String query = "SELECT id, team_id_fk, user_id_fk, member_type, created_on, created_by_id_fk, status FROM team_members WHERE id=?";
        log4jLog.info(" selectTeamMember " + query);
        Object[] param = new Object[]{teamMemberId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, new RowMapper<TeamMember>() {

                @Override
                public TeamMember mapRow(ResultSet rs, int i) throws SQLException {
                    TeamMember teamMember = new TeamMember();
                    teamMember.setId(rs.getInt("id"));
                    teamMember.setTeamId(rs.getInt("team_id_fk"));
                    teamMember.setMemberType(rs.getInt("member_type"));
                    teamMember.setStatus(rs.getInt("status"));

                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setAccountId(0);
                    user.setFirstName("");
                    user.setLastName("");
                    user.setEmailAddress("");
                    user.setPassword("");
                    user.setMobileNo("");
                    user.setGender(0);
                    user.setRole(0);
                    user.setActive(false);
                    user.setLastLoggedOn(new Timestamp(0));
                    user.setLastKnownLocationTime(new Timestamp(0));
                    user.setLastKnownLocation("");
                    user.setCreatedOn(new Timestamp(0));
                    user.setCreatedBy(0);
                    teamMember.setUser(user);

                    User createdBy = new User();
                    createdBy.setId(rs.getInt("created_by_id_fk"));
                    createdBy.setAccountId(0);
                    createdBy.setFirstName("");
                    createdBy.setLastName("");
                    createdBy.setEmailAddress("");
                    createdBy.setPassword("");
                    createdBy.setMobileNo("");
                    createdBy.setGender(0);
                    createdBy.setRole(0);
                    createdBy.setActive(false);
                    createdBy.setLastLoggedOn(new Timestamp(0));
                    createdBy.setLastKnownLocationTime(new Timestamp(0));
                    createdBy.setLastKnownLocation("");
                    createdBy.setCreatedOn(new Timestamp(0));
                    createdBy.setCreatedBy(0);
                    teamMember.setCreatedBy(createdBy);

                    teamMember.setCreatedOn(rs.getTimestamp("created_on"));
                    return teamMember;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            log4jLog.info(" selectTeamMember " + e);
            return new TeamMember();
        } catch (Exception e) {
            log4jLog.info(" selectTeamMember " + e);
            return new TeamMember();
        }
    }


    // added by manohar
    
    public   List<TeamMember> selectTeamMemberOnly(int teamMemberId, int accountId) {
        String query = "select distinct(user_id_fk) from team_members where team_id_fk =? and member_type=3";
        log4jLog.info(" selectTeamMember " + query);
        Object[] param = new Object[]{teamMemberId};
        try {
//            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, new RowMapper<TeamMember>() {
                return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<TeamMember>() {
                @Override
                public TeamMember mapRow(ResultSet rs, int i) throws SQLException {
                    TeamMember teamMember = new TeamMember();
                    teamMember.setId(rs.getInt("user_id_fk"));
                    return teamMember;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            log4jLog.info(" selectTeamMember " + e);
            return new ArrayList<TeamMember>();
        } catch (Exception e) {
            log4jLog.info(" selectTeamMember " + e);
            return new ArrayList<TeamMember>();
        }
    }


    @Override
    public List<TeamMember> selectTeamMembers(int teamId, int accountId) {
//        String query = "SELECT team_members.id, team_id_fk, user_id_fk,first_name,last_name,email_address,member_type, team_members.created_on, team_members.created_by_id_fk, status,u.last_known_latitude,u.last_known_langitude,u.last_known_location_time,u.last_known_location FROM team_members INNER JOIN fieldsense.users as u ON user_id_fk=u.id WHERE team_id_fk=?";
        StringBuilder query = new StringBuilder();
        query.append("SELECT tm.id, tm.team_id_fk, tm.user_id_fk,u.first_name,u.last_name,u.email_address,u.last_known_latitude,");
        query.append("u.last_known_langitude,u.last_known_location_time,u.last_known_location,tm.member_type,tm.created_on,");
        query.append(" tm.created_by_id_fk, tm.status,a.status,IFNULL(a.punch_in,0) punchIn,IFNULL(a.punch_out,0) punchOut FROM team_members tm");
        query.append(" INNER JOIN fieldsense.users as u ON tm.user_id_fk=u.id LEFT OUTER JOIN attendances a");
        query.append(" ON a.user_id_fk=tm.user_id_fk AND a.punch_date = CURDATE()");
        query.append(" WHERE tm.team_id_fk=?");
        log4jLog.info(" selectTeamMembers " + query);
        Object[] param = new Object[]{teamId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<TeamMember>() {

                @Override
                public TeamMember mapRow(ResultSet rs, int i) throws SQLException {
                    TeamMember teamMember = new TeamMember();
                    teamMember.setId(rs.getInt("id"));
                    teamMember.setTeamId(rs.getInt("team_id_fk"));
                    teamMember.setMemberType(rs.getInt("member_type"));
                    teamMember.setStatus(rs.getInt("status"));

                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmailAddress(rs.getString("email_address"));
                    user.setLatitude(rs.getDouble("last_known_latitude"));
                    user.setLangitude(rs.getDouble("last_known_langitude"));
                    user.setLastKnownLocationTime(rs.getTimestamp("last_known_location_time"));
                    user.setLastKnownLocation(rs.getString("last_known_location"));
                    teamMember.setUser(user);

                    User createdBy = new User();
                    createdBy.setId(rs.getInt("created_by_id_fk"));
                    createdBy.setAccountId(0);
                    createdBy.setFirstName("");
                    createdBy.setLastName("");
                    createdBy.setEmailAddress("");
                    createdBy.setPassword("");
                    createdBy.setMobileNo("");
                    createdBy.setGender(0);
                    createdBy.setRole(0);
                    createdBy.setActive(false);
                    createdBy.setLastLoggedOn(new Timestamp(0));
                    createdBy.setLastKnownLocationTime(new Timestamp(0));
                    createdBy.setLastKnownLocation("");
                    createdBy.setCreatedOn(new Timestamp(0));
                    createdBy.setCreatedBy(0);
                    teamMember.setCreatedBy(createdBy);

                    String punchIn = rs.getString("punchIn");
                    String punchOut = rs.getString("punchOut");
                    if (punchIn.equals("0")) {
                        teamMember.setIsOnline(false);
                    } else if (punchOut.equals("00:00:00")) {
                        teamMember.setIsOnline(true);
                    } else {
                        teamMember.setIsOnline(false);
                    }
                    teamMember.setCreatedOn(rs.getTimestamp("created_on"));
                    return teamMember;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            log4jLog.info(" selectTeamMembers " + e);
            return new ArrayList<TeamMember>();
        } catch (Exception e) {
            log4jLog.info(" selectTeamMembers " + e);
//            e.printStackTrace();
            return new ArrayList<TeamMember>();
        }
    }

    @Override
    public List<TeamMember> selectTeamMembersWithOutTL(int teamId, int accountId) {
//        String query = "SELECT team_members.id, team_id_fk, user_id_fk,first_name,last_name,email_address,member_type, team_members.created_on, team_members.created_by_id_fk, status,u.last_known_latitude,u.last_known_langitude,u.last_known_location_time,u.last_known_location FROM team_members INNER JOIN fieldsense.users as u ON user_id_fk=u.id WHERE team_id_fk=?";
        StringBuilder query = new StringBuilder();
        /*query.append("SELECT tm.id, tm.team_id_fk, tm.user_id_fk,u.first_name,u.last_name,u.email_address,u.last_known_latitude,");
        query.append("u.last_known_langitude,u.last_known_location_time,u.last_known_location,tm.member_type,tm.created_on,");
        query.append(" tm.created_by_id_fk, tm.status,a.status,IFNULL(a.punch_in,0) punchIn,IFNULL(a.punch_out,0) punchOut,");
        query.append("IFNULL(loff.latitude,0) officelatitude,IFNULL(loff.longitude,0) officelongitude,");
        query.append("IFNULL(lhome.latitude,0) homelatitude,IFNULL(lhome.longitude,0) homelongitude");
        query.append("  FROM team_members tm");
        query.append(" INNER JOIN fieldsense.users as u ON tm.user_id_fk=u.id LEFT OUTER JOIN attendances a");
        query.append(" ON a.user_id_fk=tm.user_id_fk AND a.punch_date = CURDATE()");
        query.append(" LEFT OUTER JOIN location loff ON loff.user_id_fk=tm.user_id_fk AND loff.location_type_id_fk=0");
        query.append(" LEFT OUTER JOIN location lhome ON lhome.user_id_fk=tm.user_id_fk AND lhome.location_type_id_fk=-1");
        query.append(" WHERE tm.team_id_fk=? AND member_type!=2 ");*/

        query.append("SELECT tm.id, tm.team_id_fk, tm.user_id_fk,u.first_name,u.last_name,u.email_address,u.last_known_latitude,");
        query.append("u.last_known_langitude,u.last_known_location_time,u.last_known_location,tm.member_type,tm.created_on,");
        query.append(" tm.created_by_id_fk, tm.status,a.status,IFNULL(a.punch_in,0) punchIn,IFNULL(a.punch_out,0) punchOut,");
        query.append("IFNULL(u.office_latitude,0) officelatitude,IFNULL(u.office_langitude,0) officelongitude,");
        query.append("IFNULL(u.home_latitude,0) homelatitude,IFNULL(u.home_langitude,0) homelongitude,");
        query.append("IFNULL(uk.key_value,0) canTakeCalls,IFNULL(ukm.key_value,0) inMeeting");
        query.append("  FROM team_members tm");
        query.append(" INNER JOIN fieldsense.users as u ON tm.user_id_fk=u.id LEFT OUTER JOIN attendances a");
        query.append(" ON a.user_id_fk=tm.user_id_fk AND a.punch_date = CURDATE()");
        query.append(" LEFT OUTER JOIN user_keys uk ON uk.user_id_fk=u.id AND uk.user_key='CanTakeCalls'");
        query.append(" LEFT OUTER JOIN user_keys ukm ON ukm.user_id_fk=u.id AND ukm.user_key='InMeeting'");
        query.append(" WHERE tm.team_id_fk=? AND member_type!=2 AND u.account_id_fk=?");
        log4jLog.info(" selectTeamMembers " + query);
        Object[] param = new Object[]{teamId, accountId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<TeamMember>() {

                @Override
                public TeamMember mapRow(ResultSet rs, int i) throws SQLException {
                    TeamMember teamMember = new TeamMember();
                    teamMember.setId(rs.getInt("id"));
                    teamMember.setTeamId(rs.getInt("team_id_fk"));
                    teamMember.setMemberType(rs.getInt("member_type"));
                    teamMember.setStatus(rs.getInt("status"));

                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmailAddress(rs.getString("email_address"));
                    user.setLatitude(rs.getDouble("last_known_latitude"));
                    user.setLangitude(rs.getDouble("last_known_langitude"));
                    user.setLastKnownLocationTime(rs.getTimestamp("last_known_location_time"));
                    user.setLastKnownLocation(rs.getString("last_known_location"));

                    Location homeLocation = new Location();
                    homeLocation.setLatitude(rs.getDouble("homelatitude"));
                    homeLocation.setLangitude(rs.getDouble("homelongitude"));
                    user.setHomeLocation(homeLocation);

                    Location officeLocation = new Location();
                    officeLocation.setLatitude(rs.getDouble("officelatitude"));
                    officeLocation.setLangitude(rs.getDouble("officelongitude"));
                    user.setOfficeLocation(officeLocation);

                    teamMember.setUser(user);

                    User createdBy = new User();
                    createdBy.setId(rs.getInt("created_by_id_fk"));
                    createdBy.setAccountId(0);
                    createdBy.setFirstName("");
                    createdBy.setLastName("");
                    createdBy.setEmailAddress("");
                    createdBy.setPassword("");
                    createdBy.setMobileNo("");
                    createdBy.setGender(0);
                    createdBy.setRole(0);
                    createdBy.setActive(false);
                    createdBy.setLastLoggedOn(new Timestamp(0));
                    createdBy.setLastKnownLocationTime(new Timestamp(0));
                    createdBy.setLastKnownLocation("");
                    createdBy.setCreatedOn(new Timestamp(0));
                    createdBy.setCreatedBy(0);
                    teamMember.setCreatedBy(createdBy);

                    String punchIn = rs.getString("punchIn");
                    String punchOut = rs.getString("punchOut");
                    if (punchIn.equals("0")) {
                        teamMember.setIsOnline(false);
                    } else if (punchOut.equals("00:00:00")) {
                        teamMember.setIsOnline(true);
                    } else {
                        teamMember.setIsOnline(false);
                    }
                    teamMember.setCreatedOn(rs.getTimestamp("created_on"));
                    int canTakeCall = rs.getInt("canTakeCalls");
                    if (canTakeCall == 1) {
                        teamMember.setCanTakeCalls(true);
                    } else {
                        teamMember.setCanTakeCalls(false);
                    }
                    int inMeeting = rs.getInt("inMeeting");
                    if (inMeeting == 1) {
                        teamMember.setInMeeting(true);
                    } else {
                        teamMember.setInMeeting(false);
                    }
                    return teamMember;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            log4jLog.info(" selectTeamMembers " + e);
            return new ArrayList<TeamMember>();
        } catch (Exception e) {
            log4jLog.info(" selectTeamMembers " + e);
//            e.printStackTrace();
            return new ArrayList<TeamMember>();
        }
    }

    @Override
    public int insertTeamMember(TeamMember teamMember, int accountId) {
        String query = "INSERT INTO team_members(team_id_fk, user_id_fk, member_type , created_on, created_by_id_fk, status) VALUES(?,?,?,now(),?,?)";
        log4jLog.info(" insertTeamMember " + query);
        Object[] param = new Object[]{teamMember.getTeamId(), teamMember.getUser().getId(), teamMember.getMemberType(), teamMember.getCreatedBy().getId(), teamMember.getStatus()};
        try {
            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    String query1 = "SELECT MAX(id) FROM team_members";
                    log4jLog.info(" insertTeamMember " + query1);
                    try {
                        return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query1, Integer.class);
                    } catch (Exception e) {
                        log4jLog.info(" insertTeamMember " + e);
//                        e.printStackTrace();
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            log4jLog.info(" insertTeamMember " + e);
//            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public boolean updateTeamMember(TeamMember teamMember, int accountId) {
        String query = "UPDATE team_members SET status=?, member_type=? WHERE team_id_fk=? AND user_id_fk=?";
        log4jLog.info(" updateTeamMember " + query);
        Object[] param = new Object[]{teamMember.getStatus(), teamMember.getMemberType(), teamMember.getTeamId(), teamMember.getUser().getId()};

        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateTeamMember " + e);
            return false;
        }
    }
    
    @Override
    public boolean updateTeamMemberId(TeamMember teamMember, int accountId) {
        String query = "UPDATE team_members SET team_id_fk=? WHERE user_id_fk=? AND member_type=3";
        log4jLog.info(" updateTeamMember " + query);
        Object[] param = new Object[]{teamMember.getTeamId(), teamMember.getUser().getId()};

        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateTeamMember " + e);
            return false;
        }
    }

    @Override
    public boolean deleteTeamMember(int id, int accountId) {
        String query = "DELETE FROM team_members WHERE id=?";
        log4jLog.info(" deleteTeamMember " + query);
        Object[] param = new Object[]{id};

        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" deleteTeamMember " + e);
            return false;
        }
    }

    @Override
    public boolean isTeamValid(int teamId, int accountId) {
        String query = "SELECT COUNT(id) FROM teams WHERE id= ?";
        log4jLog.info(" isTeamValid " + query);
        Object[] param = new Object[]{teamId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isTeamValid " + e);
//            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Team> selectTeamsList(int userId, int accountId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT teams.id, teams.team_name, teams.description, teams.user_id_fk as owner, teams.is_active, teams.created_on,teams.created_by_id_fk, owner.first_name,owner.last_name,owner.email_address,");
        query.append(" owner.last_known_latitude,owner.last_known_langitude FROM teams INNER JOIN team_members AS tm ON teams.id=tm.team_id_fk INNER JOIN fieldsense.users AS owner ON teams.user_id_fk = owner.id WHERE teams.user_id_fk=? AND member_type=2 AND teams.is_active=1");
        log4jLog.info(" selectTeamMemberLocation " + query);
        Object[] param = new Object[]{userId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<Team>() {

                @Override
                public Team mapRow(ResultSet rs, int i) throws SQLException {
                    Team team = new Team();
                    User owner = new User();
                    User user1 = new User();
                    team.setId(rs.getInt("id"));
                    team.setTeamName(rs.getString("team_name"));
                    team.setDescription(rs.getString("description"));
                    owner.setId(rs.getInt("owner"));
                    owner.setFirstName(rs.getString("first_name"));
                    owner.setLastName(rs.getString("last_name"));
                    owner.setEmailAddress(rs.getString("email_address"));
                    owner.setLatitude(rs.getDouble("last_known_latitude"));
                    owner.setLangitude(rs.getDouble("last_known_langitude"));
                    team.setOwnerId(owner);
                    team.setIsActive(rs.getInt("is_active"));
                    team.setCreatedOn(rs.getTimestamp("created_on"));
                    user1.setCreatedBy(rs.getInt("created_by_id_fk"));
                    team.setCreatedBy(user1);

                    return team;
                }
            });

        } catch (Exception e) {
            log4jLog.info(" selectTeamMemberLocation" + e);
//            e.printStackTrace();
            return new ArrayList<Team>();
        }
    }

    @Override
    public List<TeamMember> selectTeamMembersLocation(int teamId, int accountId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT tm.id,tm.user_id_fk,tm.member_type,tm.created_on,tm.status,tm.created_by_id_fk, u.first_name, u.last_name, u.email_address,u.mobile_number,u.gender,u.role,u.active,");
        query.append(" u.last_logged_on,u.last_known_location_time,u.last_known_location,u.last_known_latitude,u.last_known_langitude,u.created_on FROM team_members as tm");
        query.append(" INNER JOIN fieldsense.users as u ON tm.user_id_fk=u.id WHERE u.active=1 AND tm.team_id_fk=?");
        log4jLog.info(" selectTeamMembersLocation " + query);
        Object[] param = new Object[]{teamId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<TeamMember>() {

                @Override
                public TeamMember mapRow(ResultSet rs, int i) throws SQLException {
                    TeamMember teamMember = new TeamMember();
                    teamMember.setId(rs.getInt("id"));
                    teamMember.setMemberType(rs.getInt("member_type"));
                    teamMember.setCreatedOn(rs.getTimestamp("created_on"));
                    teamMember.setStatus(rs.getInt("status"));
                    User user = new User();
                    User u = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmailAddress(rs.getString("email_address"));
                    user.setMobileNo(rs.getString("mobile_number"));
                    user.setGender(rs.getInt("gender"));
                    user.setRole(rs.getInt("role"));
                    user.setActive(rs.getBoolean("active"));
                    user.setLastLoggedOn(rs.getTimestamp("last_logged_on"));
                    user.setLastKnownLocationTime(rs.getTimestamp("last_known_location_time"));
                    user.setLastKnownLocation(rs.getString("last_known_location"));
                    user.setLangitude(rs.getDouble("last_known_langitude"));
                    user.setLatitude(rs.getDouble("last_known_latitude"));
                    user.setCreatedOn(rs.getTimestamp("created_on"));
                    teamMember.setUser(user);
                    u.setCreatedBy(rs.getInt("created_by_id_fk"));
                    teamMember.setCreatedBy(u);
                    return teamMember;
                }
            });

        } catch (Exception e) {
            log4jLog.info(" selectTeamMemberLocation " + e);
            return new ArrayList<TeamMember>();
        }

    }

    @Override
    public boolean isTeamMemeber(int teamId, int userId, int acountId) {
        String query = "SELECT COUNT(id) FROM team_members WHERE team_id_fk=? AND user_id_fk=?";
        log4jLog.info(" isTeamMemeber " + query);
        Object[] param = new Object[]{teamId, userId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(acountId).queryForObject(query, param, Integer.class) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isTeamMemeber " + e);
            return false;
        }
    }

    @Override
    public int insertTeamMember(TeamMember teamMember, int teamId, int accountId) {
        String query = "INSERT INTO team_members(team_id_fk, user_id_fk, member_type , created_on, created_by_id_fk, status) VALUES(?,?,?,now(),?,?)";
        log4jLog.info(" insertTeamMember " + query);
        Object[] param = new Object[]{teamId, teamMember.getUser().getId(), teamMember.getMemberType(), teamMember.getCreatedBy().getId(), teamMember.getStatus()};
        try {
            synchronized (this) {
                if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                    String query1 = "SELECT MAX(id) FROM team_members";
                    log4jLog.info(" insertTeamMember " + query1);
                    try {
                        return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query1, Integer.class);
                    } catch (Exception e) {
                        log4jLog.info(" insertTeamMember " + e);
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            log4jLog.info(" insertTeamMember " + e);
            return 0;
        }
    }

    @Override
    public List<TeamMember> selectTeamMembersForTeamLead(int userId, int accountId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT tm.user_id_fk,u.first_name,u.last_name FROM team_members as tm INNER JOIN fieldsense.users as u ");
        query.append("ON tm.user_id_fk=u.id WHERE tm.team_id_fk IN (SELECT t.id FROM teams as t  WHERE t.user_id_fk = ? ) AND u.account_id_fk= ?");
        log4jLog.info("selectTeamMembersForTeamLead " + query);
        Object[] param = new Object[]{userId, accountId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<TeamMember>() {

                @Override
                public TeamMember mapRow(ResultSet rs, int i) throws SQLException {
                    TeamMember tMember = new TeamMember();
                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    tMember.setUser(user);
                    return tMember;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectTeamMembersForTeamLead " + e);
            return new ArrayList<TeamMember>();
        }
    }

    @Override
    public boolean deleteTeamMember(int teamId, int userId, int accountId) {
        String query = "DELETE FROM team_members WHERE team_id_fk=? AND user_id_fk=?";
        log4jLog.info(" deleteTeamMember " + query);
        Object[] param = new Object[]{teamId, userId};

        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" deleteTeamMember " + e);
            return false;
        }
    }

    @Override
    public boolean deleteTeamFromTeamMembers(int teamId, int accountId) {
        String query = "DELETE FROM team_members WHERE team_id_fk=?";
        log4jLog.info(" deleteTeamFromTeamMembers " + query);
        Object[] param = new Object[]{teamId};

        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" deleteTeamFromTeamMembers " + e);
            return false;
        }
    }
    
//     @Override
    public boolean updateTeamFromTeamMembers(int teamId,int parentId, int accountId) {
        String query = "UPDATE team_members SET user_id_fk=? WHERE team_id_fk=?";
        log4jLog.info(" deleteTeamFromTeamMembers " + query);
        Object[] param = new Object[]{parentId,teamId};

        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" deleteTeamFromTeamMembers " + e);
            return false;
        }
    }

    @Override
    public boolean isTeamMemeberIsInAnyTeam(int userId, int acountId) {
        String query = "SELECT COUNT(id) FROM team_members WHERE user_id_fk=?";
        log4jLog.info(" isTeamMemeberIsInAnyTeam " + query);
        Object[] param = new Object[]{userId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(acountId).queryForObject(query, param, Integer.class) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isTeamMemeberIsInAnyTeam " + e);
            return false;
        }
    }

    @Override
    public List<TeamMember> selectTeamLead(int accountId) {
        String query = "SELECT DISTINCT user_id_fk,u.first_name,u.last_name FROM teams INNER JOIN fieldsense.users as u ON user_id_fk=u.id ";
        log4jLog.info("selectTeamLead " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), new RowMapper<TeamMember>() {

                @Override
                public TeamMember mapRow(ResultSet rs, int i) throws SQLException {
                    TeamMember tMember = new TeamMember();
                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    tMember.setUser(user);
                    return tMember;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectTeamLead " + e);
            return new ArrayList<TeamMember>();
        }
    }

    @Override
    public boolean isTeamNameUnique(int userId, String teamName, int accountId) {
        String query = "SELECT COUNT(id) FROM teams WHERE team_name=? AND user_id_fk=?";
        log4jLog.info(" isTeamNameUnique " + query);
        Object[] param = new Object[]{teamName, userId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" isTeamNameUnique " + e);
            return false;
        }
    }

    @Override
    public String teamName(int teamId, int accountId) {
        String query = "SELECT team_name FROM teams WHERE id=?";
        log4jLog.info("teamName" + query);
        Object[] param = new Object[]{teamId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class);
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<TeamMember> selectUserPositions(int userId, int accountId) {
        String query = "SELECT team_position_csv FROM teams WHERE user_id_fk=?";
        log4jLog.info("selectUserPositions" + query);
        Connection connection = null;
        Statement stmt = null;
        Object[] param = new Object[]{userId};
        try {
            connection = FieldSenseUtils.getDataSourceForAccount(accountId).getConnection();
            stmt = connection.createStatement();
            String userPosition = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class);
            StringBuilder query2 = new StringBuilder();
//            String query2 = " SELECT id, team_name, description, user_id_fk, is_active, created_on, created_by_id_fk,team_position_csv FROM teams WHERE MATCH(team_position_csv) AGAINST(" + userPosition + " IN BOOLEAN MODE)";
            query2.append("SELECT DISTINCT(t.id), t.team_name, t.description, t.user_id_fk, t.is_active, t.created_on, ");
            query2.append("t.created_by_id_fk,t.team_position_csv ,u.first_name,u.last_name,u.last_known_latitude,");
            query2.append("u.last_known_langitude,u.last_known_location_time,u.last_known_location,IFNULL(a.punch_in,0) punchIn,");
            query2.append("IFNULL(a.punch_out,0) punchOut,IFNULL(uk.key_value,0) canTakeCalls,IFNULL(ukm.key_value,0) inMeeting,");
            query2.append("IFNULL(u.office_latitude,0) officelatitude,IFNULL(u.office_langitude,0) officelongitude,");
            query2.append("IFNULL(u.home_latitude,0) homelatitude,IFNULL(u.home_langitude,0) homelongitude");
            query2.append(" FROM teams t ");
            query2.append(" INNER JOIN fieldsense.users u ON u.id=t.user_id_fk");
            query2.append(" LEFT OUTER JOIN attendances a ON a.user_id_fk=t.user_id_fk AND a.punch_date = CURDATE()");
            query2.append(" LEFT OUTER JOIN user_keys uk ON uk.user_id_fk=u.id AND uk.user_key='CanTakeCalls'");
            query2.append(" LEFT OUTER JOIN user_keys ukm ON ukm.user_id_fk=u.id AND ukm.user_key='InMeeting'");
            query2.append(" WHERE team_position_csv LIKE '%" + userPosition + "%' AND u.active=1");
            ResultSet rs = stmt.executeQuery(query2.toString());
            List<TeamMember> listOfTeams = new ArrayList<TeamMember>();
            while (rs.next()) {
                int userPositionsArrayLength = userPosition.split(",").length;
                String userPosition2 = rs.getString("team_position_csv");
                int userPositions2ArrayLength = userPosition2.split(",").length;
                if ((userPositionsArrayLength + 1 == userPositions2ArrayLength) || (userPositionsArrayLength == userPositions2ArrayLength)) {
                    TeamMember teamMember = new TeamMember();
                    teamMember.setTeamId(rs.getInt("id"));

                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setLangitude(rs.getDouble("last_known_langitude"));
                    user.setLatitude(rs.getDouble("last_known_latitude"));
                    user.setLastKnownLocationTime(rs.getTimestamp("last_known_location_time"));
                    user.setLastKnownLocation(rs.getString("last_known_location"));

                    Location homeLocation = new Location();
                    homeLocation.setLatitude(rs.getDouble("homelatitude"));
                    homeLocation.setLangitude(rs.getDouble("homelongitude"));
                    user.setHomeLocation(homeLocation);

                    Location officeLocation = new Location();
                    officeLocation.setLatitude(rs.getDouble("officelatitude"));
                    officeLocation.setLangitude(rs.getDouble("officelongitude"));
                    user.setOfficeLocation(officeLocation);

                    teamMember.setUser(user);

                    User createdBy = new User();
                    createdBy.setId(rs.getInt("created_by_id_fk"));
                    teamMember.setCreatedBy(createdBy);

                    String punchIn = rs.getString("punchIn");
                    String punchOut = rs.getString("punchOut");
                    if (punchIn.equals("0")) {
                        teamMember.setIsOnline(false);
                    } else if (punchOut.equals("00:00:00")) {
                        teamMember.setIsOnline(true);
                    } else {
                        teamMember.setIsOnline(false);
                    }
                    teamMember.setCreatedOn(rs.getTimestamp("created_on"));
                    int canTakeCall = rs.getInt("canTakeCalls");
                    if (canTakeCall == 1) {
                        teamMember.setCanTakeCalls(true);
                    } else {
                        teamMember.setCanTakeCalls(false);
                    }
                    int inMeeting = rs.getInt("inMeeting");
                    if (inMeeting == 1) {
                        teamMember.setInMeeting(true);
                    } else {
                        teamMember.setInMeeting(false);
                    }
                    listOfTeams.add(teamMember);
                }
            }
            return listOfTeams;
        } catch (Exception e) {
//            e.printStackTrace();
            return new ArrayList<TeamMember>();
        } finally {
            try {                
                stmt.close();
                connection.close();
            } catch (SQLException ex) {
                log4jLog.info("selectUserPositions"+ex);
//                ex.printStackTrace();
                return new ArrayList<TeamMember>();
            }
        }
    }

    /**
     * @Added by jyoti, 14-july-2017
     * @param userId
     * @param accountId
     * @purpose to get the list of all subordinates with their subordinate for displaying users while adding visits to multiple user
     * @return
     */
    @Override
    public List<User> selectUserPositionsSubordinatesOfSubordinate(int userId, int accountId) {
        String query = "SELECT team_position_csv FROM teams WHERE user_id_fk=?";
        log4jLog.info("selectUserPositions" + query);
        Connection connection = null;
        Statement stmt = null;
        Object[] param = new Object[]{userId};
        try {
            connection = FieldSenseUtils.getDataSourceForAccount(accountId).getConnection();
            stmt = connection.createStatement();
            String userPosition = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class);
            StringBuilder query2 = new StringBuilder();
            query2.append("SELECT t.user_id_fk, u.first_name, u.last_name FROM teams t INNER JOIN fieldsense.users u ON u.id=t.user_id_fk");
            query2.append(" WHERE team_position_csv LIKE '%" + userPosition + "%' AND u.active=1 ORDER BY u.first_name");
            ResultSet rs = stmt.executeQuery(query2.toString());
            List<User> listOfSubordinates = new ArrayList<User>();
            while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));                    
                    listOfSubordinates.add(user);                
            }
            return listOfSubordinates;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<User>();
        } finally {
            try {                
                stmt.close();
                connection.close();
            } catch (SQLException ex) {
                log4jLog.info("selectUserPositions"+ex);
                ex.printStackTrace();
                return new ArrayList<User>();
            }
        }
    }
    
    /**
     *
     * @param userId
     * @param date
     * @param accountId
     * @return
     */

    @Override
    public List<TeamMember> selectUserPositions(int userId, String date, int accountId) {

        String query = "SELECT team_position_csv FROM teams WHERE user_id_fk=?";
        log4jLog.info("selectUserPositions" + query);
        Connection connection = null;
        Statement stmt = null;
        Object[] param = new Object[]{userId};
        try {
            connection = FieldSenseUtils.getDataSourceForAccount(accountId).getConnection();
            stmt = connection.createStatement();
            String userPosition = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class);
            StringBuilder query2 = new StringBuilder();
//            String query2 = " SELECT id, team_name, description, user_id_fk, is_active, created_on, created_by_id_fk,team_position_csv FROM teams WHERE MATCH(team_position_csv) AGAINST(" + userPosition + " IN BOOLEAN MODE)";
            query2.append("SELECT DISTINCT(t.id), t.team_name, t.description, t.user_id_fk, t.is_active, t.created_on, ");
            query2.append("t.created_by_id_fk,t.team_position_csv ,u.first_name,u.last_name,u.last_known_latitude,");
            query2.append("u.last_known_langitude,u.last_known_location_time,u.last_known_location,IFNULL(a.punch_in,0) punchIn,");
            query2.append("IFNULL(a.punch_out,0) punchOut,IFNULL(uk.key_value,0) canTakeCalls,IFNULL(ukm.key_value,0) inMeeting,");
            query2.append("IFNULL(u.office_latitude,0) officelatitude,IFNULL(u.office_langitude,0) officelongitude,");
            query2.append("IFNULL(u.home_latitude,0) homelatitude,IFNULL(u.home_langitude,0) homelongitude,IFNULL(DATE(uk.modified_on),DATE(now())) uk_modifiedDay,now() new_uk_modified_date,t.has_subordinates");
            query2.append(" FROM teams t ");
            query2.append(" INNER JOIN fieldsense.users u ON u.id=t.user_id_fk");
            query2.append(" LEFT OUTER JOIN attendances a ON a.user_id_fk=t.user_id_fk AND a.punch_date = CURDATE()");
            query2.append(" LEFT OUTER JOIN user_keys uk ON uk.user_id_fk=u.id AND uk.user_key='CanTakeCalls'");
            query2.append(" LEFT OUTER JOIN user_keys ukm ON ukm.user_id_fk=u.id AND ukm.user_key='InMeeting'");
            query2.append(" WHERE team_position_csv LIKE '%" + userPosition + "%' AND u.active=1 order by u.first_name asc");
            ResultSet rs = stmt.executeQuery(query2.toString());
            List<TeamMember> listOfTeams = new ArrayList<TeamMember>();
            while (rs.next()) {
                int userPositionsArrayLength = userPosition.split(",").length;
                String userPosition2 = rs.getString("team_position_csv");
                int userPositions2ArrayLength = userPosition2.split(",").length;
                if ((userPositionsArrayLength + 1 == userPositions2ArrayLength) || (userPositionsArrayLength == userPositions2ArrayLength)) {
                    String modifiedDate = rs.getString("uk_modifiedDay");

                    TeamMember teamMember = new TeamMember();
                    teamMember.setTeamId(rs.getInt("id"));
                    
                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setLangitude(rs.getDouble("last_known_langitude"));
                    user.setLatitude(rs.getDouble("last_known_latitude"));
                    user.setLastKnownLocationTime(rs.getTimestamp("last_known_location_time"));
                    user.setLastKnownLocation(rs.getString("last_known_location"));

                    Location homeLocation = new Location();
                    homeLocation.setLatitude(rs.getDouble("homelatitude"));
                    homeLocation.setLangitude(rs.getDouble("homelongitude"));
                    user.setHomeLocation(homeLocation);

                    Location officeLocation = new Location();
                    officeLocation.setLatitude(rs.getDouble("officelatitude"));
                    officeLocation.setLangitude(rs.getDouble("officelongitude"));
                    user.setOfficeLocation(officeLocation);

                    teamMember.setUser(user);

                    User createdBy = new User();
                    createdBy.setId(rs.getInt("created_by_id_fk"));
                    teamMember.setCreatedBy(createdBy);

                    String punchIn = rs.getString("punchIn");
                    String punchOut = rs.getString("punchOut");
                    if (punchIn.equals("0")) {
                        teamMember.setIsOnline(false);
                    } else if (punchOut.equals("00:00:00")) {
                        teamMember.setIsOnline(true);
                    } else {
                        teamMember.setIsOnline(false);
                    }
                    teamMember.setCreatedOn(rs.getTimestamp("created_on"));
                    if (date.equals(modifiedDate)) {
                        int canTakeCall = rs.getInt("canTakeCalls");
                        if (canTakeCall == 1) {
                            teamMember.setCanTakeCalls(true);
                        } else {
                            teamMember.setCanTakeCalls(false);
                        }
                        int inMeeting = rs.getInt("inMeeting");
                        if (inMeeting == 1) {
                            teamMember.setInMeeting(true);
                        } else {
                            teamMember.setInMeeting(false);
                        }
                    } else {
                        teamMember.setCanTakeCalls(false);
                        teamMember.setInMeeting(false);

                        UserKayDaoImpl userKayDaoImpl = new UserKayDaoImpl();

                        UserKey keyCantakeCall = new UserKey();
                        keyCantakeCall.setKeyValue("0");
                        User userCanTakeCall = new User();
                        userCanTakeCall.setId(rs.getInt("user_id_fk"));
                        keyCantakeCall.setUserId(userCanTakeCall);
                        keyCantakeCall.setUserKay("CanTakeCalls");
                        userKayDaoImpl.updateUserKeys(keyCantakeCall, accountId);

                        UserKey keyInMeeting = new UserKey();
                        keyInMeeting.setKeyValue("0");
                        keyInMeeting.setUserId(userCanTakeCall);
                        keyInMeeting.setUserKay("InMeeting");
                        userKayDaoImpl.updateUserKeys(keyInMeeting, accountId);

                    }
                    int hasSubOrdinates = rs.getInt("has_subordinates");
                    if (hasSubOrdinates == 1) {
                        teamMember.setHasSubordinate(true);
                    } else {
                        teamMember.setHasSubordinate(false);
                    }
                    listOfTeams.add(teamMember);
                }
            }
            return listOfTeams;
        } catch (Exception e) {
            log4jLog.info("selectUserPositions"+e);
//            e.printStackTrace();
            return new ArrayList<TeamMember>();
        } finally {
            try {
                stmt.close();
                connection.close();                
            } catch (SQLException ex) {
                log4jLog.info("selectUserPositions"+ex);
//                ex.printStackTrace();
                return new ArrayList<TeamMember>();
            }
        }
    }
    
     // Added by Jyoti, 28-02-2017 purpose : to get all subordinate (later need to find another way .. )
    @Override
    public List<TeamMember> selectUserPositionsAllSubordinate(int userId, String date, int accountId) {

        String query = "SELECT team_position_csv FROM teams WHERE user_id_fk=?";
        log4jLog.info("selectUserPositionsAllSubordinate" + query);
        Connection connection = null;
        Statement stmt = null;
        Object[] param = new Object[]{userId};
        try {
            connection = FieldSenseUtils.getDataSourceForAccount(accountId).getConnection();
            stmt = connection.createStatement();
            String userPosition = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class);
            StringBuilder query2 = new StringBuilder();
            query2.append("SELECT DISTINCT(t.id), t.team_name, t.description, t.user_id_fk, t.is_active, t.created_on, ");
            query2.append("t.created_by_id_fk,t.team_position_csv ,u.first_name,u.last_name,u.last_known_latitude,");
            query2.append("u.last_known_langitude,u.last_known_location_time,u.last_known_location,IFNULL(a.punch_in,0) punchIn,");
            query2.append("IFNULL(a.punch_out,0) punchOut,IFNULL(uk.key_value,0) canTakeCalls,IFNULL(ukm.key_value,0) inMeeting,");
            query2.append("IFNULL(u.office_latitude,0) officelatitude,IFNULL(u.office_langitude,0) officelongitude,");
            query2.append("IFNULL(u.home_latitude,0) homelatitude,IFNULL(u.home_langitude,0) homelongitude,IFNULL(DATE(uk.modified_on),DATE(now())) uk_modifiedDay,now() new_uk_modified_date,t.has_subordinates");
            query2.append(" FROM teams t ");
            query2.append(" INNER JOIN fieldsense.users u ON u.id=t.user_id_fk");
            query2.append(" LEFT OUTER JOIN attendances a ON a.user_id_fk=t.user_id_fk AND a.punch_date = CURDATE()");
            query2.append(" LEFT OUTER JOIN user_keys uk ON uk.user_id_fk=u.id AND uk.user_key='CanTakeCalls'");
            query2.append(" LEFT OUTER JOIN user_keys ukm ON ukm.user_id_fk=u.id AND ukm.user_key='InMeeting'");
            query2.append(" WHERE team_position_csv LIKE '%" + userPosition + "%' AND u.active=1 order by u.first_name asc");
            ResultSet rs = stmt.executeQuery(query2.toString());
            List<TeamMember> listOfTeams = new ArrayList<TeamMember>();
            while (rs.next()) {
                    String modifiedDate = rs.getString("uk_modifiedDay");

                    TeamMember teamMember = new TeamMember();
                    teamMember.setTeamId(rs.getInt("id"));
                    
                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setLangitude(rs.getDouble("last_known_langitude"));
                    user.setLatitude(rs.getDouble("last_known_latitude"));
                    user.setLastKnownLocationTime(rs.getTimestamp("last_known_location_time"));
                    user.setLastKnownLocation(rs.getString("last_known_location"));

                    Location homeLocation = new Location();
                    homeLocation.setLatitude(rs.getDouble("homelatitude"));
                    homeLocation.setLangitude(rs.getDouble("homelongitude"));
                    user.setHomeLocation(homeLocation);

                    Location officeLocation = new Location();
                    officeLocation.setLatitude(rs.getDouble("officelatitude"));
                    officeLocation.setLangitude(rs.getDouble("officelongitude"));
                    user.setOfficeLocation(officeLocation);

                    teamMember.setUser(user);

                    User createdBy = new User();
                    createdBy.setId(rs.getInt("created_by_id_fk"));
                    teamMember.setCreatedBy(createdBy);

                    String punchIn = rs.getString("punchIn");
                    String punchOut = rs.getString("punchOut");
                    if (punchIn.equals("0")) {
                        teamMember.setIsOnline(false);
                    } else if (punchOut.equals("00:00:00")) {
                        teamMember.setIsOnline(true);
                    } else {
                        teamMember.setIsOnline(false);
                    }
                    teamMember.setCreatedOn(rs.getTimestamp("created_on"));
                    if (date.equals(modifiedDate)) {
                        int canTakeCall = rs.getInt("canTakeCalls");
                        if (canTakeCall == 1) {
                            teamMember.setCanTakeCalls(true);
                        } else {
                            teamMember.setCanTakeCalls(false);
                        }
                        int inMeeting = rs.getInt("inMeeting");
                        if (inMeeting == 1) {
                            teamMember.setInMeeting(true);
                        } else {
                            teamMember.setInMeeting(false);
                        }
                    } else {
                        teamMember.setCanTakeCalls(false);
                        teamMember.setInMeeting(false);

                        UserKayDaoImpl userKayDaoImpl = new UserKayDaoImpl();

                        UserKey keyCantakeCall = new UserKey();
                        keyCantakeCall.setKeyValue("0");
                        User userCanTakeCall = new User();
                        userCanTakeCall.setId(rs.getInt("user_id_fk"));
                        keyCantakeCall.setUserId(userCanTakeCall);
                        keyCantakeCall.setUserKay("CanTakeCalls");
                        userKayDaoImpl.updateUserKeys(keyCantakeCall, accountId);

                        UserKey keyInMeeting = new UserKey();
                        keyInMeeting.setKeyValue("0");
                        keyInMeeting.setUserId(userCanTakeCall);
                        keyInMeeting.setUserKay("InMeeting");
                        userKayDaoImpl.updateUserKeys(keyInMeeting, accountId);

                    }
                    int hasSubOrdinates = rs.getInt("has_subordinates");
                    if (hasSubOrdinates == 1) {
                        teamMember.setHasSubordinate(true);
                    } else {
                        teamMember.setHasSubordinate(false);
                    }
                    listOfTeams.add(teamMember);
            }
            return listOfTeams;
        } catch (Exception e) {
            log4jLog.info("selectUserPositionsAllSubordinate" + e);
//            e.printStackTrace();
            return new ArrayList<TeamMember>();
        } finally {
            try {
                stmt.close();
                connection.close();                
            } catch (SQLException ex) {
                log4jLog.info("selectUserPositionsAllSubordinate" + ex);
//                ex.printStackTrace();
                return new ArrayList<TeamMember>();
            }
        }
    }
    
    // Added by Jyoti, 24-02-2017 purpose : to get count of visits & subordinate
    @Override
    public List<TeamMember> selectUserPositions(int userId, String date, String fromDate, String toDate, int accountId) {

        String query = "SELECT team_position_csv FROM teams WHERE user_id_fk=?";
        log4jLog.info("selectUserPositions-withCount" + query);
        Connection connection = null;
        Statement stmt = null;
        Object[] param = new Object[]{userId};
//        System.out.println("fdate"+fdate+"tdate"+tdate);
        
        try {
            connection = FieldSenseUtils.getDataSourceForAccount(accountId).getConnection();
            stmt = connection.createStatement();
            String userPosition = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class);
            StringBuilder query2 = new StringBuilder();
            query2.append("SELECT DISTINCT(t.id), t.team_name, t.description, t.user_id_fk, t.is_active, t.created_on, ");
            query2.append("t.created_by_id_fk,t.team_position_csv ,u.first_name,u.last_name,u.last_known_latitude,");
            query2.append("u.last_known_langitude,u.last_known_location_time,u.last_known_location,IFNULL(a.punch_in,0) punchIn,");
            query2.append("IFNULL(a.punch_out,0) punchOut,a.punch_date,a.punch_out_date,IFNULL(uk.key_value,0) canTakeCalls,IFNULL(ukm.key_value,0) inMeeting,");
            query2.append("IFNULL(u.office_latitude,0) officelatitude,IFNULL(u.office_langitude,0) officelongitude,");
            query2.append("IFNULL(u.home_latitude,0) homelatitude,IFNULL(u.home_langitude,0) homelongitude,IFNULL(DATE(uk.modified_on),DATE(now())) uk_modifiedDay,now() new_uk_modified_date,t.has_subordinates,");
            query2.append(" (SELECT count(ap.id) FROM appointments ap WHERE (appointment_time BETWEEN '"+ fromDate +"' AND '"+ toDate +"' AND assigned_id_fk=t.user_id_fk) AND record_state !=3) as activityCount ");   // Added by Jyoti, 25-02-2017
            query2.append(" FROM teams t ");
            query2.append(" INNER JOIN fieldsense.users u ON u.id=t.user_id_fk");
            query2.append(" LEFT OUTER JOIN attendances a ON a.user_id_fk=t.user_id_fk AND a.id=(SELECT MAX(id) FROM  attendances att WHERE att.user_id_fk=u.id)");   
//            query2.append(" LEFT OUTER JOIN attendances a ON a.user_id_fk=t.user_id_fk AND a.punch_date =CURDATE()"); // commented to solve the issue of on-field status in dashboard
            query2.append(" LEFT OUTER JOIN user_keys uk ON uk.user_id_fk=u.id AND uk.user_key='CanTakeCalls'");
            query2.append(" LEFT OUTER JOIN user_keys ukm ON ukm.user_id_fk=u.id AND ukm.user_key='InMeeting'");
            query2.append(" LEFT OUTER JOIN appointments ap on ap.assigned_id_fk=u.id ");  // Added by Jyoti, 25-02-2017
            query2.append(" WHERE team_position_csv LIKE '%" + userPosition + "%' AND u.active=1 order by u.first_name asc");
            ResultSet rs = stmt.executeQuery(query2.toString());
            List listOfTeams = new ArrayList();
            while (rs.next()) {
                int userPositionsArrayLength = userPosition.split(",").length;
                String userPosition2 = rs.getString("team_position_csv");
                int userPositions2ArrayLength = userPosition2.split(",").length;
                if ((userPositionsArrayLength + 1 == userPositions2ArrayLength) || (userPositionsArrayLength == userPositions2ArrayLength)) {
                    String modifiedDate = rs.getString("uk_modifiedDay");

                    TeamMember teamMember = new TeamMember();
                    teamMember.setTeamId(rs.getInt("id"));
                    teamMember.setActivityCount(rs.getInt("activityCount"));    // Added by jyoti, 25-02-2017

                    Attendance att=new Attendance();
                    att.setPunchInTime(rs.getString("punchIn"));
                    att.setPunchOutTime(rs.getString("punchOut"));
                    
                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setLangitude(rs.getDouble("last_known_langitude"));
                    user.setLatitude(rs.getDouble("last_known_latitude"));
                    user.setLastKnownLocationTime(rs.getTimestamp("last_known_location_time"));
                    user.setLastKnownLocation(rs.getString("last_known_location"));

                    Location homeLocation = new Location();
                    homeLocation.setLatitude(rs.getDouble("homelatitude"));
                    homeLocation.setLangitude(rs.getDouble("homelongitude"));
                    user.setHomeLocation(homeLocation);

                    Location officeLocation = new Location();
                    officeLocation.setLatitude(rs.getDouble("officelatitude"));
                    officeLocation.setLangitude(rs.getDouble("officelongitude"));
                    user.setOfficeLocation(officeLocation);

                    teamMember.setUser(user);
                    
                    // for getting subordinate count, Added by Jyoti
                    int subOrdinateCount = 0;
                    try{
                        StringBuilder queryforSubordinateCount = new StringBuilder();
                        queryforSubordinateCount.append("SELECT COUNT(t.user_id_fk) FROM teams AS t LEFT JOIN fieldsense.users AS u ON t.user_id_fk=u.id WHERE u.active=1  AND SUBSTRING(t.team_position_csv,8,6) = (SELECT id FROM teams WHERE user_id_fk='"+teamMember.getUser().getId()+"') ");
                        subOrdinateCount =  FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(queryforSubordinateCount.toString(), Integer.class);
                    }
                    catch(Exception ex){
                        return new ArrayList();
                    }
                        
                    teamMember.setSubordinateCount(subOrdinateCount);
                      
                    User createdBy = new User();
                    createdBy.setId(rs.getInt("created_by_id_fk"));
                    teamMember.setCreatedBy(createdBy);

                    String punchIn = rs.getString("punchIn");

                    String punchOut = rs.getString("punchOut");          //Added by manohar
                 
                    
                    /*   // commented by manohar
                    
                    String punchin_am_pm = null,punchout_am_pm = null;
                   
                            
                    String output_punch_Date = null,output_punch_out_Date,punch_date,punch_out_date;
                  
                        punch_date=rs.getString("punch_date");
=======
                    String punchOut = rs.getString("punchOut");          //Added by manohar                    
                    teamMember.setPunchin_date(rs.getString("punch_date"));
                    teamMember.setPunchout_date(rs.getString("punch_out_date"));
>>>>>>> .merge-right.r7755
                        
<<<<<<< .working
                         punch_out_date=rs.getString("punch_out_date");
                        
                        
//                        System.out.println("date1="+punch_date+"punchIn="+punchIn+"punch_out_date="+punch_out_date);
                 
                        
                        
                        if(!punchIn.equals("0")&&!punch_date.equals("null")&&!punch_out_date.equals("null"))
                        {
                            teamMember.setPunchin_date(rs.getString("punch_date"));
                            teamMember.setPunchout_date(rs.getString("punch_out_date"));
                        
                            java.sql.Timestamp punchinstartDate = java.sql.Timestamp.valueOf(punch_date+" "+punchIn);
                            date=punchinstartDate.toString();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Calendar c3 = Calendar.getInstance();
                            c3.setTime(sdf.parse(date));
                            c3.add(Calendar.MINUTE, +330);  // to get time 5:30 hrs back.
                            output_punch_Date = sdf.format(c3.getTime());
                           
                      
                            
                            
//                            System.out.println("output_punch_Date="+output_punch_Date);
                            
                            java.sql.Timestamp punchoutstartDate = java.sql.Timestamp.valueOf(punch_out_date+" "+punchOut);
                            date=punchoutstartDate.toString();
                            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Calendar c31 = Calendar.getInstance();
                            c31.setTime(sdf1.parse(date));
                            c31.add(Calendar.MINUTE, +330);  // to get time 5:30 hrs back.
                            output_punch_out_Date = sdf1.format(c31.getTime());
                            
                            
                            
                            
                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                             Date d = sdf2.parse(output_punch_Date);
                            DateFormat f2 = new SimpleDateFormat("h:mma");
                            punchin_am_pm=f2.format(d).toLowerCase();
//                            System.out.println("am and pm strStartDate="+punchin_am_pm);
                            
                            SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                             Date d1 = sdf3.parse(output_punch_out_Date);
                            DateFormat f3 = new SimpleDateFormat("h:mma");
                            punchout_am_pm=f3.format(d1).toLowerCase();
//                            System.out.println("am and pm strStartDate="+punchout_am_pm);
                        
                            
                            SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd");
                            Calendar c = Calendar.getInstance();
                            c.setTime(new Date()); // Now use today date.
                            c.add(Calendar.DATE, -2); // Adding 2 days
                            String output = sdf4.format(c.getTime());
//                            System.out.println("out="+output);
                         
//                            
                            teamMember.setTwo_day_from_current_date(output);
                            
//                              System.out.println("output_punch_out_Date="+output_punch_out_Date);
//                              System.out.println("split="+output_punch_Date.split(" ")[0]);
//                              System.out.println("split="+output_punch_out_Date.split(" ")[0]);
                              
                              
//                              System.out.println("punchin_am_pm="+punchin_am_pm);
//                              System.out.println("punchout_am_pm="+punchout_am_pm);
                              
                              String test=output_punch_Date.split(" ")[0];
                              
                              String test1=output_punch_out_Date.split(" ")[0];
                              
                              if(test.equals(punch_date))
                              {
//                                   System.out.println("hours="+output_punch_Date.split(" ")[1]);
                                   //teamMember.setPunchin(output_punch_Date.split(" ")[1]);
                                   teamMember.setPunchin(punchin_am_pm);
                                   
                                   if(punchOut.equals("00:00:00"))
                                   {
                                       teamMember.setPunchout("0");
                                   }
                                   else
                                   {
//                                       teamMember.setPunchin(output_punch_Date.split(" ")[1]);
//                                       teamMember.setPunchout(output_punch_out_Date.split(" ")[1]);
                                       teamMember.setPunchin(punchin_am_pm);
                                       teamMember.setPunchout(punchout_am_pm);
                                   }
                        
                              }
                             else
                              {
                                   
//                                  System.out.println("date="+output_punch_Date.split(" ")[0]);
//                                  System.out.println("time="+output_punch_Date.split(" ")[1]);
                                
                                  teamMember.setPunchin_date(output_punch_Date.split(" ")[0]);
//                                  teamMember.setPunchin(output_punch_Date.split(" ")[1]);
                                   teamMember.setPunchin(punchin_am_pm);
                                  
                              }
                            
                            
                        }
                       
                        else
                        {
                       
                       teamMember.setPunchin_date(punch_date);
//                       teamMember.setPunchin(punchIn);
//                       teamMember.setPunchout(punchOut);
                        teamMember.setPunchin(punchin_am_pm);
                        teamMember.setPunchout(punchout_am_pm);
                       teamMember.setPunchout_date(punch_out_date);
                        
                        }
                        
                        */
                     
//                        
//                        if(punchDate.equals(tdate)){
//                        todaysPunch=true;
//                        }else{
//                        todaysPunch=false;
//                        }
                        
                        
                    if (punchIn.equals("0")) 
                    {
                        teamMember.setIsOnline(false);
                    }
                    else if (punchOut.equals("00:00:00")) {
                               teamMember.setIsOnline(true);
                       // commented by manohar        
                      /*  if(punch_date.equals(fdate)||punch_date.equals(tdate))
                        {
//                            System.out.println("test"+punch_date);
                            
                             teamMember.setIsOnline(true);
//                           teamMember.setPunchin(output_punch_Date.split(" ")[1]);
                             teamMember.setPunchin(punchin_am_pm);
                        }
                        else if(punch_date.equals(fdate)||punch_date.equals(teamMember.getTwo_day_from_current_date()))
                        {
//                            System.out.println("test"+punch_date);
                            
                             teamMember.setIsOnline(true);
//                             teamMember.setPunchin(output_punch_Date.split(" ")[1]);
                             teamMember.setPunchin(punchin_am_pm);
                        }
                        
                        
                        */
//                        teamMember.setPunchin(punchIn);
                         
                               // commented by manohar
                    } else {
//                        System.out.println("else .. ");
                        teamMember.setIsOnline(false);
                    }
                    teamMember.setCreatedOn(rs.getTimestamp("created_on"));
                    if (date.equals(modifiedDate)) {
                        int canTakeCall = rs.getInt("canTakeCalls");
                        if (canTakeCall == 1) {
                            teamMember.setCanTakeCalls(true);
                        } else {
                            teamMember.setCanTakeCalls(false);
                        }
                        int inMeeting = rs.getInt("inMeeting");
                        if (inMeeting == 1) {
                            teamMember.setInMeeting(true);
                        } else {
                            teamMember.setInMeeting(false);
                        }
                    } else {
                        teamMember.setCanTakeCalls(false);
                        teamMember.setInMeeting(false);

                        UserKayDaoImpl userKayDaoImpl = new UserKayDaoImpl();

                        UserKey keyCantakeCall = new UserKey();
                        keyCantakeCall.setKeyValue("0");
                        User userCanTakeCall = new User();
                        userCanTakeCall.setId(rs.getInt("user_id_fk"));
                        keyCantakeCall.setUserId(userCanTakeCall);
                        keyCantakeCall.setUserKay("CanTakeCalls");
                        userKayDaoImpl.updateUserKeys(keyCantakeCall, accountId);

                        UserKey keyInMeeting = new UserKey();
                        keyInMeeting.setKeyValue("0");
                        keyInMeeting.setUserId(userCanTakeCall);
                        keyInMeeting.setUserKay("InMeeting");
                        userKayDaoImpl.updateUserKeys(keyInMeeting, accountId);

                    }
                    int hasSubOrdinates = rs.getInt("has_subordinates");
                    if (hasSubOrdinates == 1) {
                        teamMember.setHasSubordinate(true);
                    } else {
                        teamMember.setHasSubordinate(false);
                    }
                    listOfTeams.add(teamMember);
                }
            }
            return listOfTeams;
        } catch (Exception e) {
            log4jLog.info(" selectUserPositions " + e);
//            e.printStackTrace();
            return new ArrayList();
        } finally {
            try {
                stmt.close();
                connection.close();                
            } catch (SQLException e) {
                log4jLog.info(" selectUserPositions " + e);
//                ex.printStackTrace();
                return new ArrayList();
            }
        }
    }

    @Override
    public List<TeamMember> selectUserPositionsOrderByName(int userId, int accountId) {
        String query = "SELECT team_position_csv FROM teams WHERE user_id_fk=?";
        log4jLog.info("selectUserPositions" + query);
        Connection connection = null;
        Statement stmt = null;
        Object[] param = new Object[]{userId};
        try {
            connection = FieldSenseUtils.getDataSourceForAccount(accountId).getConnection();
            stmt = connection.createStatement();
            String userPosition = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class);
            StringBuilder query2 = new StringBuilder();
//            String query2 = " SELECT id, team_name, description, user_id_fk, is_active, created_on, created_by_id_fk,team_position_csv FROM teams WHERE MATCH(team_position_csv) AGAINST(" + userPosition + " IN BOOLEAN MODE)";
            query2.append("SELECT DISTINCT(t.id), t.team_name, t.description, t.user_id_fk, t.is_active, t.created_on, ");
            query2.append("t.created_by_id_fk,t.team_position_csv ,u.first_name,u.last_name,u.last_known_latitude,");
            query2.append("u.last_known_langitude,u.last_known_location_time,u.last_known_location,IFNULL(a.punch_in,0) punchIn,");
            query2.append("IFNULL(a.punch_out,0) punchOut,IFNULL(uk.key_value,0) canTakeCalls,IFNULL(ukm.key_value,0) inMeeting,");
            query2.append("IFNULL(u.office_latitude,0) officelatitude,IFNULL(u.office_langitude,0) officelongitude,");
            query2.append("IFNULL(u.home_latitude,0) homelatitude,IFNULL(u.home_langitude,0) homelongitude");
            query2.append(" FROM teams t ");
            query2.append(" INNER JOIN fieldsense.users u ON u.id=t.user_id_fk");
            query2.append(" LEFT OUTER JOIN attendances a ON a.user_id_fk=t.user_id_fk AND a.punch_date = CURDATE()");
            query2.append(" LEFT OUTER JOIN user_keys uk ON uk.user_id_fk=u.id AND uk.user_key='CanTakeCalls'");
            query2.append(" LEFT OUTER JOIN user_keys ukm ON ukm.user_id_fk=u.id AND ukm.user_key='InMeeting'");
            query2.append(" WHERE team_position_csv LIKE '%" + userPosition + "%' AND u.active=1 ORDER BY u.first_name,u.last_name");
            ResultSet rs = stmt.executeQuery(query2.toString());
            List<TeamMember> listOfTeams = new ArrayList<TeamMember>();
            while (rs.next()) {
                int userPositionsArrayLength = userPosition.split(",").length;
                String userPosition2 = rs.getString("team_position_csv");
                int userPositions2ArrayLength = userPosition2.split(",").length;
                if ((userPositionsArrayLength + 1 == userPositions2ArrayLength) || (userPositionsArrayLength == userPositions2ArrayLength)) {
                    TeamMember teamMember = new TeamMember();
                    teamMember.setTeamId(rs.getInt("id"));

                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setLangitude(rs.getDouble("last_known_langitude"));
                    user.setLatitude(rs.getDouble("last_known_latitude"));
                    user.setLastKnownLocationTime(rs.getTimestamp("last_known_location_time"));
                    user.setLastKnownLocation(rs.getString("last_known_location"));

                    Location homeLocation = new Location();
                    homeLocation.setLatitude(rs.getDouble("homelatitude"));
                    homeLocation.setLangitude(rs.getDouble("homelongitude"));
                    user.setHomeLocation(homeLocation);

                    Location officeLocation = new Location();
                    officeLocation.setLatitude(rs.getDouble("officelatitude"));
                    officeLocation.setLangitude(rs.getDouble("officelongitude"));
                    user.setOfficeLocation(officeLocation);

                    teamMember.setUser(user);

                    User createdBy = new User();
                    createdBy.setId(rs.getInt("created_by_id_fk"));
                    teamMember.setCreatedBy(createdBy);

                    String punchIn = rs.getString("punchIn");                     
                    String punchOut = rs.getString("punchOut");
                    if (punchIn.equals("0")) {
                        teamMember.setIsOnline(false);
                    } else if (punchOut.equals("00:00:00")) {
                        teamMember.setIsOnline(true);
                    } else {
                        teamMember.setIsOnline(false);
                    }
                    teamMember.setCreatedOn(rs.getTimestamp("created_on"));
                    int canTakeCall = rs.getInt("canTakeCalls");
                    if (canTakeCall == 1) {
                        teamMember.setCanTakeCalls(true);
                    } else {
                        teamMember.setCanTakeCalls(false);
                    }
                    int inMeeting = rs.getInt("inMeeting");
                    if (inMeeting == 1) {
                        teamMember.setInMeeting(true);
                    } else {
                        teamMember.setInMeeting(false);
                    }
                    listOfTeams.add(teamMember);
                }
            }
            return listOfTeams;
        } catch (Exception e) {
            log4jLog.info(" selectUserPositionsOrderByName " + e);
//            e.printStackTrace();
            return new ArrayList<TeamMember>();
        } finally {
            try {
                stmt.close();
                connection.close();                
            } catch (SQLException ex) {
                log4jLog.info("selectUserPositionsOrderByName" + ex);
//                ex.printStackTrace();
                return new ArrayList<TeamMember>();
            }
        }
    }

    @Override
    public List<OraganizationChart> selectOraganizationChart(int accountId) {
        StringBuilder query = new StringBuilder();
        query.append(" SELECT t.id,t.user_id_fk,t.team_position_csv,u.first_name,u.last_name,u.role,u.designation,u.mobile_number,u.email_address,u.active FROM teams t");
        query.append(" INNER JOIN fieldsense.users u ON u.id=t.user_id_fk");

        log4jLog.info("selectOraganizationChart" + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), new RowMapper<OraganizationChart>() {

                @Override
                public OraganizationChart mapRow(ResultSet rs, int i) throws SQLException {
                    OraganizationChart oraganizationChart = new OraganizationChart();
                    String teamPositionCsv = rs.getString("team_position_csv");    //         100351,100211,100000
                    String teamPosition[] = teamPositionCsv.split(",");           //   100351 100211 100000
                    int parentId = 0;
                    if (teamPosition.length > 1) {           //     3
                        parentId = Integer.parseInt(teamPosition[1]);    //     100211
                    }
                    oraganizationChart.setId(rs.getInt("id"));
                    oraganizationChart.setParentId(parentId);
                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setRole(rs.getInt("role"));
                    user.setDesignation(rs.getString("designation"));
                    user.setEmailAddress(rs.getString("email_address"));
                    user.setMobileNo(rs.getString("mobile_number"));
                    if(rs.getInt("active")==0)
                    {
                        user.setActive(false);
                    }
                    else
                    {
                        user.setActive(true);
                    }
                    oraganizationChart.setUser(user);
                    return oraganizationChart;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectOraganizationChart" + e);
           e.printStackTrace();
            return new ArrayList<OraganizationChart>();
        }
    }

    @Override
    public List<User> selectOrganizationMembersForChart(int accountId) {
        String query = "SELECT id,first_name,last_name,role FROM fieldsense.users u WHERE id NOT IN (SELECT user_id_fk from teams) AND active =1 AND account_id_fk=? ORDER BY first_name ASC";
        log4jLog.info("selectOrganizationMembersForChart" + query);
        Object param[] = new Object[]{accountId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query.toString(), param, new RowMapper<User>() {

                @Override
                public User mapRow(ResultSet rs, int i) throws SQLException {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setRole(rs.getInt("role"));

                    return user;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectOrganizationMembersForChart" + query);
//            e.printStackTrace();
            return new ArrayList<User>();
        }
    }

    @Override
    public String selectUserPositionCsv(int parentId, int accountId) {
        String query = "SELECT team_position_csv FROM teams WHERE id=?";
        Object param[] = new Object[]{parentId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class);
        } catch (Exception e) {
            log4jLog.info(" selectUserPositionCsv " + e);
//            e.printStackTrace();
            return "";
        }
    }

    @Override
    public boolean updateUserPostionCsv(String positionCsv, int teamId, int accountId) {
        String query = "UPDATE teams SET team_position_csv=? WHERE id=?";
        Object param[] = new Object[]{positionCsv, teamId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateUserPostionCsv " + e);
//            e.printStackTrace();
            return false;
        }
    }
     
    
    // added by manohar
    @Override
         public int getParentID(int parentTeamId, int accountId) {
        String query = "SELECT user_id_fk FROM teams WHERE id=?";
        log4jLog.info(" selectTeams " + query);
        Object[] param = new Object[]{parentTeamId};
        try {
              return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
            }
           catch (Exception e) {
            log4jLog.info(" selectTeamMember " + e);
            return 0;
        }
    }     
       
    
    @Override
    public OraganizationChart selectOraganizationChartMember(int memberId, int accountId) {
        StringBuilder query = new StringBuilder();
        query.append(" SELECT t.id,t.user_id_fk,t.team_position_csv,u.first_name,u.last_name,u.designation,u.mobile_number,u.email_address,u.role FROM teams t");
        query.append(" INNER JOIN fieldsense.users u ON u.id=t.user_id_fk WHERE t.id=?");
        log4jLog.info("selectOraganizationChartMember" + query);
        Object param[] = new Object[]{memberId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query.toString(), param, new RowMapper<OraganizationChart>() {

                @Override
                public OraganizationChart mapRow(ResultSet rs, int i) throws SQLException {
                    OraganizationChart oraganizationChart = new OraganizationChart();
                    String teamPositionCsv = rs.getString("team_position_csv");
                    String teamPosition[] = teamPositionCsv.split(",");
                    int parentId = 0;
                    if (teamPosition.length > 1) {
                        parentId = Integer.parseInt(teamPosition[1]);
                    }

                    oraganizationChart.setId(rs.getInt("id"));
                    oraganizationChart.setParentId(parentId);
                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setDesignation(rs.getString("designation"));
                    user.setEmailAddress(rs.getString("email_address"));
                    user.setMobileNo(rs.getString("mobile_number"));
                    user.setRole(rs.getInt("role"));
                    oraganizationChart.setUser(user);
                    return oraganizationChart;
                }
            });
        } catch (Exception e) {
            log4jLog.info("selectOraganizationChartMember" + e);
//            e.printStackTrace();
            return new OraganizationChart();
        }
    }

    @Override
    public boolean deleteTeamWithUserId(int userId, int accountId) {
        String query = "DELETE FROM teams WHERE user_id_fk=?";
        log4jLog.info(" deleteTeam " + query);
        Object[] param = new Object[]{userId};

        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" deleteTeamWithUserId " + e);
//            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteTeamMemberWithUserId(int userId, int accountId) {
        String query = "DELETE FROM team_members WHERE user_id_fk=?";
        log4jLog.info(" deleteTeamMember " + query);
        Object[] param = new Object[]{userId};    

        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" deleteTeamMemberWithUserId " + e);
//            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateOrganizationChartMemberInTeams(int orgChartId, int userId, int accountId) {
        String query = "UPDATE teams SET user_id_fk=? WHERE id=?";
        Object param[] = new Object[]{userId, orgChartId};
        log4jLog.info(" updateOrganizationChartMemberInTeams " + query);
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateOrganizationChartMemberInTeams " + e);
            return false;
        }
    }

    @Override
    public boolean updateOrganizationChartMemberInTeamMembers(int orgChartId, int memberType, int userId, int accountId, int editUserId) {
        String query = "UPDATE team_members SET user_id_fk=? WHERE team_id_fk=? AND member_type=? AND user_id_fk=?";
        Object param[] = new Object[]{userId, orgChartId, memberType, editUserId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateOrganizationChartMemberInTeamMembers " + e);
            return false;
        }
    }

    // added by manohar
    public int getParentCSV(int parent_id, int accountId) {
        String query = "select team_position_csv from teams where team_position_csv like ? ";
        Object param[] = new Object[]{"%"+parent_id+"%"};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" getting the parent userid " + e);
//            e.printStackTrace();
            return 0;
        }
    }    
    
    
    @Override
    public int getParentUserId(int parent_id, int accountId) {
        String query = "select user_id_fk from teams where team_position_csv like ? ";
        Object param[] = new Object[]{parent_id+"%"};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" getting the parent userid " + e);
//            e.printStackTrace();
            return 0;
        }
    }        
      
    
    @Override
    //Change please.
    public boolean updateReportingHead(int  reportId,int userId,int accountId) {                 // 3574   3587
        String query = "update fieldsense.users set report_to=? where id=?";
        Object param[] = new Object[]{reportId,userId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }        
        } catch (Exception e) {
            log4jLog.info("updating report_to of users " + e);
            e.printStackTrace();
            return false;
        }
    }
    
    
    @Override
    public User selectOrganizationChartUser(int orgChartId, int accountId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT u.id,u.first_name,u.last_name FROM teams t");
        query.append(" INNER JOIN fieldsense.users u ON t.user_id_fk=u.id WHERE t.id= ?");
        log4jLog.info(" selectOrganizationChartUser " + query);
        Object param[] = new Object[]{orgChartId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query.toString(), param, new RowMapper<User>() {

                @Override
                public User mapRow(ResultSet rs, int i) throws SQLException {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    return user;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectOrganizationChartUser " + e);
            return new User();
        }
    }

    @Override
    public List<TeamMember> selectUserImmidiateSupiriorAndSubOrdinatePositions(int userId, int accountId) {
        String query = "SELECT team_position_csv FROM teams WHERE user_id_fk=?";
        log4jLog.info("selectUserPositions" + query);
        Connection connection = null;
        Statement stmt = null;
        Object[] param = new Object[]{userId};
        try {
            String userPosition = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class);
            int position = userPosition.indexOf(",");
            String userPosition3 = userPosition.substring(position + 1);
            StringBuilder query2 = new StringBuilder();
            query2.append("SELECT DISTINCT(t.id), t.team_name, t.description, t.user_id_fk, t.is_active, t.created_on, ");
            query2.append("t.created_by_id_fk,t.team_position_csv ,u.first_name,u.last_name,u.last_known_latitude,");
            query2.append("u.last_known_langitude,u.last_known_location_time,u.last_known_location,IFNULL(a.punch_in,0) punchIn,");
            query2.append("IFNULL(a.punch_out,0) punchOut,IFNULL(uk.key_value,0) canTakeCalls,IFNULL(ukm.key_value,0) inMeeting,");
            query2.append("IFNULL(u.office_latitude,0) officelatitude,IFNULL(u.office_langitude,0) officelongitude,");
            query2.append("IFNULL(u.home_latitude,0) homelatitude,IFNULL(u.home_langitude,0) homelongitude");
            query2.append(" FROM teams t ");
            query2.append(" INNER JOIN fieldsense.users u ON u.id=t.user_id_fk");
            query2.append(" LEFT OUTER JOIN attendances a ON a.user_id_fk=t.user_id_fk AND a.punch_date = CURDATE()");
            query2.append(" LEFT OUTER JOIN user_keys uk ON uk.user_id_fk=u.id AND uk.user_key='CanTakeCalls'");
            query2.append(" LEFT OUTER JOIN user_keys ukm ON ukm.user_id_fk=u.id AND ukm.user_key='InMeeting'");
            query2.append(" WHERE (team_position_csv LIKE '%" + userPosition + "%')||(team_position_csv LIKE '" + userPosition3 + "')");

            connection = FieldSenseUtils.getDataSourceForAccount(accountId).getConnection();
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query2.toString());
            List<TeamMember> listOfTeams = new ArrayList<TeamMember>();
            while (rs.next()) {
                int userPositionsArrayLength = userPosition.split(",").length;
                String userPosition2 = rs.getString("team_position_csv");
                int userPositions2ArrayLength = userPosition2.split(",").length;
                if ((userPositionsArrayLength + 1 == userPositions2ArrayLength) || (userPositionsArrayLength == userPositions2ArrayLength) || (userPositionsArrayLength - 1 == userPositions2ArrayLength)) {
                    TeamMember teamMember = new TeamMember();
                    teamMember.setTeamId(rs.getInt("id"));
                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    teamMember.setUser(user);
                    listOfTeams.add(teamMember);
                }
            }
            return listOfTeams;
        } catch (Exception e) {
            log4jLog.info("selectUserImmidiateSupiriorAndSubOrdinatePositions" + e);
//            e.printStackTrace();
            return new ArrayList<TeamMember>();
        } finally {
            try {
                stmt.close();
                connection.close();                
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(TeamDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public int userIsSubordinate(int accountId, int teamId, int userId) {
        String query = "SELECT COUNT(id) FROM team_members WHERE user_id_fk=? AND team_id_fk=?";
        Object param[] = new Object[]{userId, teamId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
        } catch (Exception e) {
            log4jLog.info(" userIsSubordinate " + e);
            return 0;
        }
    }

    public int userIsReportingHead(int accountId, int teamId) {
        String query = "SELECT team_position_csv FROM teams WHERE id=?";
        Object[] param = new Object[]{teamId};
        try {
            String userPosition = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class);
            int position = userPosition.indexOf(",");
            String userPosition3 = userPosition.substring(position + 1);
            String query1 = "SELECT user_id_fk FROM teams WHERE id=?";
            Object[] param1 = new Object[]{userPosition3};
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query1, param1, Integer.class);
//            return Integer.parseInt(userPosition3);
        } catch (Exception e) {
            log4jLog.info(" userIsReportingHead " + e);
            return 0;
        }
    }

    @Override
    public boolean isUserInOrganizationChart(int userId, int accountId) {
        String query = "SELECT COUNT(id) FROM teams WHERE user_id_fk=?";
        log4jLog.info(" isUserInOrganizationChart " + query);
        Object param[] = new Object[]{userId};
        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class) > 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            log4jLog.info(" isUserInOrganizationChart " + e);
            return false;
        }
    }

    // modified by manohar
    @Override
    public List<Team> selectUserSubordiatesPositionIds(final int teamId, int accountId) {  
        //  100321
        
//        System.out.println("selectUserSubordiatesPositionIds"+teamId); // teamid added by jyoti
            String query = "SELECT id,user_id_fk,team_position_csv  FROM teams where team_position_csv LIKE ?";
        Object param[] = new Object[]{"%" + teamId + "%"};
        log4jLog.info(" selectUserSubordiatesPositionIds " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<Team>() {
                @Override
                public Team mapRow(ResultSet rs, int i) throws SQLException {
                    Team team=new Team();          
                    String teamPositionCsv = rs.getString("team_position_csv");               //  100321,100320,100319,100318,100261,100000    
                    team.setId(rs.getInt("id"));
                    team.setUser_id(rs.getInt("user_id_fk")); 
                    if(teamPositionCsv.contains(","+teamId))
                    {                 
                        String s1=teamPositionCsv.replace(","+teamId,"");      //  100424
//                        System.out.println("replace="+s1);
			team.setParentCSV(s1);         //    100424,100422,100420,100391,100351,100211,100000		 
                    }                 
//                    System.out.println("n="+team.getParentCSV());
                    return team;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info(" selectUserSubordiatesPositionIds " + e);
            return new ArrayList<Team>();
        }
    }

    @Override
    public boolean deleteTeamMemberFromTeam(int userId, int accountId) {
//        String query = "DELETE FROM team_members WHERE user_id_fk=? AND member_type=3";   //  3569
    String query = "DELETE FROM team_members WHERE user_id_fk=? ";   //  3574
    
        log4jLog.info(" deleteTeamMember " + query);
        Object[] param = new Object[]{userId};

        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" deleteTeamMember " + e);
//            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int insertDefaultTeam(Team team, String teamPositionCSV, int accountId,FieldSenseUtils fieldSenseUtils) {

        //String query = "INSERT INTO teams(team_name, description,user_id_fk,is_active,created_on,created_by_id_fk,team_position_csv) VALUES(?,?,?,?,now(),?,?)";
        String query = "INSERT INTO account_"+accountId+".teams(team_name, description,user_id_fk,is_active,created_on,created_by_id_fk,team_position_csv) VALUES(?,?,?,?,now(),?,?)";
        log4jLog.info(" insertTeam " + query);
        Object[] param = new Object[]{team.getTeamName(), team.getDescription(), team.getOwnerId().getId(), team.getIsActive(), team.getCreatedBy().getId(), teamPositionCSV};
        try {
            synchronized (this) {
               // FieldSenseUtils util= new FieldSenseUtils(); //Added by Awaneesh
                //if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                
                if(fieldSenseUtils.getJdbcTemplate().update(query, param)>0){
                    //String query1 = "SELECT MAX(id) FROM teams";
                    String query1 = "SELECT MAX(id) FROM account_"+accountId+".teams";
                    log4jLog.info(" insertTeam " + query1);
                    try {
                        //return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query1, Integer.class);
                        return fieldSenseUtils.getJdbcTemplate().queryForObject(query1, Integer.class);
                    } catch (Exception e) {
                        log4jLog.info(" insertTeam " + e);
//                        e.printStackTrace();
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            log4jLog.info(" insertTeam " + e);
//            e.printStackTrace();
            return 0;
        }
    }
    
    @Override
    public int insertDefaultTeamMember(ArrayList<TeamMember> teamList, String userToken, int accountId,FieldSenseUtils fieldSenseUtils) {

        //String query = "INSERT INTO teams(team_name, description,user_id_fk,is_active,created_on,created_by_id_fk,team_position_csv) VALUES(?,?,?,?,now(),?,?)";
        String query = "INSERT INTO account_"+accountId+".team_members(team_id_fk,user_id_fk,member_type,created_on,created_by_id_fk,status) VALUES(?,?,?,now(),?,?)";
        log4jLog.info(" insertTeam " + query);
        ArrayList<Object[]> paramList= new ArrayList<Object[]>();
        for(int i=0;i<teamList.size();i++){
            TeamMember teamMember=teamList.get(i);
            Object[] param = new Object[]{teamMember.getTeamId(), teamMember.getUser().getId(), teamMember.getMemberType(), teamMember.getCreatedBy().getId(),teamMember.getStatus()};
            paramList.add(param);
        }    
        try {
            synchronized (this) {
                //FieldSenseUtils util= new FieldSenseUtils(); //Added by Awaneesh
                //if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                fieldSenseUtils.getJdbcTemplate().batchUpdate(query, paramList);                    
            }
            return 1;
        } catch (Exception e) {
            log4jLog.info(" insertTeam " + e);
//            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int selectImmidiateSubOrdinatesCount(int teamId, int accountId) {    //  100422
        String query = "SELECT team_position_csv FROM teams WHERE id=?";
        log4jLog.info("selectUserPositions" + query);
        Connection connection = null;
        Statement stmt = null;
        Object[] param = new Object[]{teamId};
        try {
            connection = FieldSenseUtils.getDataSourceForAccount(accountId).getConnection();
            stmt = connection.createStatement();
            String userPosition = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class);
            String query2 = "SELECT team_position_csv FROM teams WHERE team_position_csv LIKE '%" + userPosition + "'";
            ResultSet rs = stmt.executeQuery(query2);
            int userPositionsArrayLength = userPosition.split(",").length;
//            System.err.println(" 1csv length="+userPositionsArrayLength);      //  6
            int count = 0;
            while (rs.next()) {
                String userPosition2 = rs.getString("team_position_csv");    //  
                int userPositions2ArrayLength = userPosition2.split(",").length;    // 8
//                System.err.println("2csv="+userPositions2ArrayLength);     // 8
                if (userPositionsArrayLength + 1 == userPositions2ArrayLength) {
                    count++;
                }
            }
            return count;
        } catch (Exception e) {
            log4jLog.info("selectImmidiateSubOrdinatesCount" + e);
//            e.printStackTrace();
            return 0;
        } finally {
            try {
                stmt.close();
                connection.close();                
            } catch (SQLException e) {
                log4jLog.info("selectImmidiateSubOrdinatesCount" + e);
//                ex.printStackTrace();
                return 0;
            }
        }
    }
    
    @Override
    public boolean updateHasSubordiantes(int teamId, int subordinateStatus, int accountId) {
        String query = "UPDATE teams SET has_subordinates=? WHERE id=?";
        log4jLog.info(" updateHasSubordiantes " + query);
        Object[] param = new Object[]{subordinateStatus, teamId};

        try {
            if (FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query, param) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log4jLog.info(" updateHasSubordiantes " + e);
//            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<User> selectUserAllSubordinates(int userId, int accountId) {
        String query = "SELECT id FROM teams WHERE user_id_fk=?";
        log4jLog.info(" selectUserAllSubordinates " + query);
        Object[] param = new Object[]{userId};
        try {
            int teamId = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
            String query2 = "SELECT u.emp_code,u.id,u.first_name,u.last_name FROM teams t INNER JOIN fieldsense.users u ON t.user_id_fk=u.id WHERE team_position_csv LIKE ? ORDER BY u.first_name,u.last_name";
            log4jLog.info(" selectUserAllSubordinates " + query2);
            Object[] param2 = new Object[]{"%" + teamId + "%"};
            try {
                return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query2, param2, new RowMapper<User>() {

                    @Override
                    public User mapRow(ResultSet rs, int i) throws SQLException {
                        User user = new User();
                        user.setId(rs.getInt("id"));
                        user.setEmp_code(rs.getString("emp_code"));
                        user.setFirstName(rs.getString("first_name"));
                        user.setLastName(rs.getString("last_name"));
                        return user;
                    }
                });
            } catch (Exception e) {
//                e.printStackTrace();
                log4jLog.info("selectUserAllSubordinates " + e);
                return new ArrayList<User>();
            }
        } catch (Exception e) {
//            e.printStackTrace();
            log4jLog.info("selectUserAllSubordinates " + e);
            return new ArrayList<User>();
        }
    }

    public List<TeamMember> selectAllOraganizationMember(String date, int accountId) {
        StringBuilder query = new StringBuilder();
        query.append(" SELECT DISTINCT t.id,t.user_id_fk,t.team_position_csv,u.first_name,u.last_name,u.designation,u.mobile_number,u.email_address,");
        query.append(" IFNULL(a.punch_in,0) punchIn,IFNULL(a.punch_out,0) punchOut,IFNULL(uk.key_value,0) canTakeCalls,IFNULL(ukm.key_value,0) inMeeting,");
        query.append(" IFNULL(DATE(uk.modified_on),DATE(now())) uk_modifiedDay,now() new_uk_modified_date FROM teams t");
        query.append(" INNER JOIN fieldsense.users u ON u.id=t.user_id_fk");
        query.append(" LEFT OUTER JOIN attendances a ON a.user_id_fk=t.user_id_fk AND a.punch_date = CURDATE()");
        query.append(" LEFT OUTER JOIN user_keys uk ON uk.user_id_fk=u.id AND uk.user_key='CanTakeCalls'");
        query.append(" LEFT OUTER JOIN user_keys ukm ON ukm.user_id_fk=u.id AND ukm.user_key='InMeeting' ORDER BY u.first_name ASC");
        log4jLog.info("selectAllOraganizationMember" + query);
        Connection connection = null;
        Statement stmt = null;
        try {
            connection = FieldSenseUtils.getDataSourceForAccount(accountId).getConnection();
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query.toString());
            List<TeamMember> listOfTeams = new ArrayList<TeamMember>();
            while (rs.next()) {

                String modifiedDate = rs.getString("uk_modifiedDay");

                TeamMember teamMember = new TeamMember();
                teamMember.setTeamId(rs.getInt("id"));

                User user = new User();
                user.setId(rs.getInt("user_id_fk"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                teamMember.setUser(user);
                String punchIn = rs.getString("punchIn");
                String punchOut = rs.getString("punchOut");
                if (punchIn.equals("0")) {
                    teamMember.setIsOnline(false);
                } else if (punchOut.equals("00:00:00")) {
                    teamMember.setIsOnline(true);
                } else {
                    teamMember.setIsOnline(false);
                }
//                teamMember.setCreatedOn(rs.getTimestamp("created_on"));
                if (date.equals(modifiedDate)) {
                    int canTakeCall = rs.getInt("canTakeCalls");
                    if (canTakeCall == 1) {
                        teamMember.setCanTakeCalls(true);
                    } else {
                        teamMember.setCanTakeCalls(false);
                    }
                    int inMeeting = rs.getInt("inMeeting");
                    if (inMeeting == 1) {
                        teamMember.setInMeeting(true);
                    } else {
                        teamMember.setInMeeting(false);
                    }
                } else {
                    teamMember.setCanTakeCalls(false);
                    teamMember.setInMeeting(false);

                    UserKayDaoImpl userKayDaoImpl = new UserKayDaoImpl();

                    UserKey keyCantakeCall = new UserKey();
                    keyCantakeCall.setKeyValue("0");
                    User userCanTakeCall = new User();
                    userCanTakeCall.setId(rs.getInt("user_id_fk"));
                    keyCantakeCall.setUserId(userCanTakeCall);
                    keyCantakeCall.setUserKay("CanTakeCalls");
                    userKayDaoImpl.updateUserKeys(keyCantakeCall, accountId);

                    UserKey keyInMeeting = new UserKey();
                    keyInMeeting.setKeyValue("0");
                    keyInMeeting.setUserId(userCanTakeCall);
                    keyInMeeting.setUserKay("InMeeting");
                    userKayDaoImpl.updateUserKeys(keyInMeeting, accountId);

                }
                listOfTeams.add(teamMember);
            }
            return listOfTeams;
        } catch (Exception e) {
            log4jLog.info("selectAllOraganizationMember" + e);
//            e.printStackTrace();
            return new ArrayList<TeamMember>();
        }finally{
            try{
                if(stmt!=null) stmt.close();
                if(connection!=null) connection.close();                               
            }catch(Exception e){
//                e.printStackTrace();
                log4jLog.info("selectAllOraganizationMember >> "+e);      
            }
        }
    }

    @Override
    public boolean isMemberInHierarchy(int ownerId, int memberId, int accountId) {
        String query = "SELECT id FROM teams WHERE user_id_fk=?";
        Object[] param = new Object[]{ownerId};
        log4jLog.info("isMemberInHierarchy " + query);
        try {
            String hierarchyId = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param,String.class);
            String query2 = "SELECT team_position_csv FROM teams t INNER JOIN fieldsense.users u ON t.user_id_fk=u.id WHERE user_id_fk=?";
            Object param2[] = new Object[]{memberId};
            try {
                String memberteamPostionCsv = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query2, param2, String.class);
                if (memberteamPostionCsv.contains(hierarchyId)) {
                    return true;
                } else {
                    return false;
                }

            } catch (Exception e) {
                log4jLog.info("isMemberInHierarchy " + e);
//                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            log4jLog.info("isMemberInHierarchy " + e);
//            e.printStackTrace();
            return false;
        }

    }

    public int getTeamId(int userId, int accountId) {
        String query = "SELECT id FROM teams WHERE user_id_fk=?";
        log4jLog.info(" selectTeams " + query);
        Object[] param = new Object[]{userId};
        try {
              return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
            }
           catch (Exception e) {
            log4jLog.info(" selectTeamMember " + e);
//            e.printStackTrace();
            return 0;
        }
    } 
    
    @Override
    public HashMap selectUserPositionCsvUsingUserId(int userId, int accountId) {
        String query = "SELECT id,user_id_fk,team_position_csv,has_subordinates FROM teams WHERE user_id_fk=?";
        Object param[] = new Object[]{userId};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, new RowMapper<HashMap>() {

                @Override
                public HashMap mapRow(ResultSet rs, int i) throws SQLException {
                    HashMap team = new HashMap();
                    team.put("id",rs.getInt("id"));
                    team.put("user_id_fk",rs.getInt("user_id_fk"));
                    team.put("team_position_csv",rs.getString("team_position_csv"));
                    team.put("has_subordinates",rs.getInt("has_subordinates"));
                    return team;
                }
            });
        } catch (Exception e) {
            log4jLog.info(" selectUser " + e);
//            e.printStackTrace();
            return new HashMap();
        }
    }    
    
    @Override
    public int updateSubOrdinatesPositionReportTo(String nheadsExistingCSV, String nheadsNewCSV,int accountId){
        //String query = "UPDATE teams SET has_subordinates=? WHERE id=?";
        String query="UPDATE teams set team_position_csv= REPLACE(team_position_csv,'"+nheadsExistingCSV+"','"+nheadsNewCSV+"') where team_position_csv like  '%"+nheadsExistingCSV+"'";
        log4jLog.info(" updateHasSubordiantes " + query);
        //Object[] param = new Object[]{subordinateStatus, teamId};
        int count=0;
        try {
             count =FieldSenseUtils.getJdbcTemplateForAccount(accountId).update(query);
            
        } catch (Exception e) {
            log4jLog.info(" updateSubOrdinatesPositionReportTo " + e);
//            e.printStackTrace();
            return 0;
        }
        return count;
    }
    
    @Override
    public int numberOfSubordinate(int parentTeam,int accountId){
        String query="Select count(id) from team_members where team_id_fk="+parentTeam+" and member_type=3";
        log4jLog.info(" numberOfSubordinate " + query);
        //Object[] param = new Object[]{subordinateStatus, teamId};
        int count=0;
        try {
             count =FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, Integer.class);
            
        } catch (Exception e) {
            log4jLog.info(" numberOfSubordinate " + e);
//            e.printStackTrace();
            return 0;
        }
        return count;
    }

    

    public int selectTeamId(int UserId,int accountId){
        String query="select id from teams where user_id_fk=?";
        log4jLog.info(" numberOfSubordinate " + query);
        Object[] param = new Object[]{UserId};
        int count=0;
        try {
             count =FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, Integer.class);
            
        } catch (Exception e) {
            log4jLog.info(" numberOfSubordinate " + e);
//            e.printStackTrace();
            return 0;
        }
        return count;
    }
    
    
    
    public List<Object> selectUserPositionsMobile(int userId, String date, int accountId) {
      String query = "SELECT team_position_csv FROM teams WHERE user_id_fk=?";
        log4jLog.info("selectUserPositions" + query);
        Connection connection = null;
        Statement stmt = null;
        Object[] param = new Object[]{userId};
        List<Object> listOfTeamData=new ArrayList<Object>();
        try {
            connection = FieldSenseUtils.getDataSourceForAccount(accountId).getConnection();
            stmt = connection.createStatement();
            String userPosition = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class);
            StringBuilder query2 = new StringBuilder();
            String query1 = "";
            String dateOfMonday;
            String strStartDate="";
            String todaysDate=date;
            //Siddhesh :-To solve count issue.Time 5:30 hrs back.
            //
            java.sql.Timestamp startDate = java.sql.Timestamp.valueOf(date+" 00:00:00");
            date=startDate.toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c3 = Calendar.getInstance();
            c3.setTime(sdf.parse(date));
            c3.add(Calendar.MINUTE, -330);  // to get time 5:30 hrs back.
            strStartDate = sdf.format(c3.getTime());
            //  
            String endDate = strStartDate;  // Start date
           // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(endDate));
            c.add(Calendar.DATE, 1);  // number of days to add1970-01-01 05:30:00
            endDate = sdf.format(c.getTime());  // dt is now the new date
           // endDate=endDate +" 00:00:00";
            
             //Get the date before 7 days.
            String weekBeforeDate = strStartDate; // Start date
            //SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c1 = Calendar.getInstance();
            c1.setTime(sdf.parse(weekBeforeDate));
            c1.add(Calendar.DATE, -6);  // number of days to add1970-01-01 05:30:00
            weekBeforeDate = sdf.format(c1.getTime());  // dt is now the new date
            //
            
            
            Calendar c2=Calendar.getInstance(Locale.UK);
            c2.setTime(sdf.parse(date));
            c2.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
            dateOfMonday=sdf.format(c2.getTime());
            c3.setTime(sdf.parse(dateOfMonday));
            c3.add(Calendar.MINUTE, -330);  // to get time 5:30 hrs back.
            dateOfMonday = sdf.format(c3.getTime());
            
            
      //      System.out.println("Start date"+startDate+"Str start date"+strStartDate+"One week before"+weekBeforeDate+"monday"+dateOfMonday+"end date"+endDate);
//            String query2 = " SELECT id, team_name, description, user_id_fk, is_active, created_on, created_by_id_fk,team_position_csv FROM teams WHERE MATCH(team_position_csv) AGAINST(" + userPosition + " IN BOOLEAN MODE)";
            query2.append("SELECT DISTINCT(t.id), t.team_name, t.description, t.user_id_fk, t.is_active, t.created_on, ");
            query2.append("t.created_by_id_fk,t.team_position_csv ,u.first_name,u.last_name,u.last_known_latitude,IFNULL(u.role,0) role,IFNULL(u.mobile_number,0) mobileNumber,");
            query2.append("u.last_known_langitude,u.last_known_location_time,u.last_known_location,IFNULL(a.punch_in,0) punchIn,IFNULL(a.punch_date,0) punchDate,IFNULL(a.punch_in_location,'') punchInLocation,IFNULL(a.punch_out_location,'') punchOutLocation,");
            query2.append("IFNULL(a.punch_out,0) punchOut,IFNULL(uk.key_value,0) canTakeCalls,IFNULL(ukm.key_value,0) inMeeting,");
            query2.append("IFNULL(u.office_latitude,0) officelatitude,IFNULL(u.office_langitude,0) officelongitude,");
            query2.append("IFNULL(DATE(uk.modified_on),DATE(now())) uk_modifiedDay,now() new_uk_modified_date");
            query2.append(" FROM teams t ");
            query2.append(" INNER JOIN fieldsense.users u ON u.id=t.user_id_fk");
            query2.append(" LEFT OUTER JOIN attendances a ON a.user_id_fk=t.user_id_fk AND a.id=(SELECT MAX(id) FROM  attendances att WHERE att.user_id_fk=u.id)");
            query2.append(" LEFT OUTER JOIN user_keys uk ON uk.user_id_fk=u.id AND uk.user_key='CanTakeCalls'");
            query2.append(" LEFT OUTER JOIN user_keys ukm ON ukm.user_id_fk=u.id AND ukm.user_key='InMeeting'");
            query2.append(" WHERE team_position_csv LIKE '%" + userPosition + "%' AND u.active=1 order by u.first_name asc");
            ResultSet rs = stmt.executeQuery(query2.toString());
            List<TeamMember> listOfTeams = new ArrayList<TeamMember>();
            int userId1=0;
            int userRole;
            double lastKnowLat;
            double lastKnowLong;
            double officeLat;
            double officelong;
            String strLastKnowLat;
            String strlastKnowLong;
            String strOfficeLat;
            String strOfficelong;
            boolean todaysPunch;
            while (rs.next()) {
                int userPositionsArrayLength = userPosition.split(",").length;
                String userPosition2 = rs.getString("team_position_csv");
                userId1=rs.getInt("user_id_fk");
                userRole=rs.getInt("role");
                int userPositions2ArrayLength = userPosition2.split(",").length;
                if ((userPositionsArrayLength + 1 == userPositions2ArrayLength) || (userPositionsArrayLength == userPositions2ArrayLength)) {
                    String modifiedDate = rs.getString("uk_modifiedDay");
                     Map<String,Object> mapOfUserInfo=new HashMap<String, Object>();
                     Map<String,Object> mapOfAppointmentInfo=new HashMap<String, Object>();
                     Map<String,Object> mapOfExpenseInfo=new HashMap<String, Object>();
                     Map<String,Object> mapOfPunchInInfo=new HashMap<String, Object>();
                     Map<String,Object> mapOfUser=new HashMap<String, Object>();
                     List<Integer> listOfVisitCount=new ArrayList<Integer>();
                     Map<String,Object> mapOfAppointmentCount=new HashMap<String, Object>();
                     Map<String,Object> mapOfAppointment=new HashMap<String, Object>();
                     Map<String,Object> mapOfExpense=new HashMap<String, Object>();
                      Map<String,Object> mapOfPunchDetails=new HashMap<String, Object>();
                      Map<String,Object> mapOfGraphInfo=new HashMap<String, Object>();
                      List<HashMap> ListOfGraphInfo=new ArrayList<HashMap>();
                      Map<String,Object> mapOfGraph=new HashMap<String, Object>();
                      List<String> listOfTerriorty=new ArrayList<String>();
                     //Map<String ,Object> mapWithIdList=new HashMap<String, Object>();
                     List<Object> listOfData=new ArrayList<Object>();
                     //TeamMember teamMember = new TeamMember();
                    //teamMember.setTeamId(rs.getInt("id"));
                  //  mapOfUserInfo.put("id",rs.getInt("id"));
                 //   User user = new User();
                  //user.setFirstName(rs.getString("first_name"));
                    mapOfUserInfo.put("userId",userId1);
                    mapOfUserInfo.put("firstName",rs.getString("first_name"));
                    mapOfUserInfo.put("lastName",rs.getString("last_name"));
                    mapOfUserInfo.put("mobileNumber",rs.getString("mobileNumber"));
                    lastKnowLat=rs.getDouble("last_known_latitude");
                    lastKnowLong=rs.getDouble("last_known_langitude");
                    officeLat=rs.getDouble("officelatitude");
                    officelong=rs.getDouble("officelongitude");
                    mapOfUserInfo.put("lastKnownLat",lastKnowLat);
                    mapOfUserInfo.put("lastKnownLong",lastKnowLong);
                    strOfficeLat=convertDouble(officeLat);
                    strOfficelong=convertDouble(officelong);
                    strLastKnowLat=convertDouble(lastKnowLat);
                    strlastKnowLong=convertDouble(lastKnowLong);
                    String url=FieldSenseUtils.getPropertyValue("PROFILEPIC_GET_PATH");
                    String pattern = "E MMMM dd yyyy HH:mm:ss";
                    SimpleDateFormat format = new SimpleDateFormat(pattern);
                    //System.out.println("url HH "+url+accountId+"_"+userId1+"_140X140.png?"+format.format(new Date()));
                    String profilePicUrl=url+accountId+"_"+userId1+"_140X140.png?"+format.format(new Date());
                    //user.setLangitude(rs.getDouble("last_known_langitude"));
                    //user.setLatitude(rs.getDouble("last_known_latitude"));
                    //user.setLastKnownLocationTime(rs.getTimestamp("last_known_location_time"));
                    //user.setLastKnownLocation(rs.getString("last_known_location"));
                    mapOfUserInfo.put("profilePicURL",profilePicUrl);
                    mapOfUserInfo.put("lastKnownLocationTime",rs.getTimestamp("last_known_location_time"));
                     String lastKnownLocation=rs.getString("last_known_location");
                        lastKnownLocation=lastKnownLocation.replace("\n"," ").trim();
                    mapOfUserInfo.put("last_known_location",lastKnownLocation);
                     String punchIn = rs.getString("punchIn");
                    String punchOut = rs.getString("punchOut");
                    String punchDate=rs.getString("punchDate");
                    mapOfPunchInInfo.put("punchDate",punchDate);
                    mapOfPunchInInfo.put("punchIn",rs.getString("punchIn"));
                    mapOfPunchInInfo.put("punchOut",rs.getString("punchOut"));
                    mapOfPunchInInfo.put("punchOutLocation",rs.getString("punchOutLocation"));
                    mapOfPunchInInfo.put("punchInLocation",rs.getString("punchInLocation"));

                    
                    if(userRole==1){
                    mapOfUserInfo.put("userRole","admin");
                    }else if(userRole==5){
                     mapOfUserInfo.put("userRole","OnField");
                    }else{
                    mapOfUserInfo.put("userRole","invalid");
                    }
                    
                    mapOfUserInfo.put("officeLatitude", officeLat);
                    mapOfUserInfo.put("officeLongitude", officelong);
                   // mapOfUserInfo.put("homeLatitude", rs.getDouble("homelatitude"));
                   // mapOfUserInfo.put("homeLongitude", rs.getDouble("homelongitude"));
                    
                  //   System.out.println("start date "+strStartDate +" End date"+endDate+"userId"+userId1+"beforeDate"+weekBeforeDate+"Monday"+dateOfMonday);
                    //End date query to take app of current date.
                    query1=("select IFNULL(app.appointment_title, '') as appointmentTitle,IFNULL(cu.customer_name, '') as customerName,IFNULL(cu.customer_location_identifier,'') as customerLocationIdentifier  from appointments app join customers cu on app.customer_id_fk=cu.id where app.appointment_time BETWEEN ? AND ? AND (app.status=0 OR app.status=1) AND app.assigned_id_fk=? AND app.record_state!=3 order by app.appointment_time asc limit 1");
                     param=new Object[]{strStartDate,endDate,userId1}; 
                     try{
                    mapOfAppointmentInfo=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForMap(query1,param);
                     }catch(Exception e){
                     //mapOfAppointmentInfo=new HashMap<String,Object>();
                      mapOfAppointmentInfo.put("appointmentTitle", "");
                      mapOfAppointmentInfo.put("customerName", "");
                      mapOfAppointmentInfo.put("customerLocationIdentifier", "");
                      //mapOfAppointmentInfo.put("yetToVisit", 0);
                      //mapOfAppointmentInfo.put("totalVisit", 0);
//                     e.printStackTrace();
                      log4jLog.info("selectUserPositionsMobile >> "+e);      
                     }
                     try{
            query1=("select IFNULL(count(app.id),0) yetToVisit,IFNULL((select count(id) from appointments where appointment_time BETWEEN ? AND ? AND assigned_id_fk=? AND record_state!=3),0) totalVisit from appointments app join customers cu on app.customer_id_fk=cu.id where app.appointment_time BETWEEN ? AND ? AND (app.status=0 OR app.status=3)  AND app.assigned_id_fk=? AND app.record_state!=3");
            param=new Object[]{strStartDate,endDate,userId1,strStartDate,endDate,userId1}; 
           mapOfAppointmentCount=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForMap(query1, param);
            mapOfAppointmentInfo.putAll(mapOfAppointmentCount);
            }catch(Exception e){
                mapOfAppointmentInfo.put("yetToVisit", 0);
                mapOfAppointmentInfo.put("totalVisit", 0);
//            e.printStackTrace();
                log4jLog.info("selectUserPositionsMobile >> "+e);
            }
//                     query1=("select IFNULL(expense_name,'') as expenseName,IFNULL(desription,'') as desription,IFNULL(amount_spent,0) as amountSpent,count(id) as count,IFNULL((select sum(amount_spent) from expenses where expense_time between ? AND ? AND user_id_fk=?),0) as amountTotal from expenses where expense_time < now() AND user_id_fk=? group by id order by expense_time desc limit 1");
//                      param=new Object[]{weekBeforeDate,endDate,userId1,userId1};
//                      try{
//                     mapOfExpenseInfo=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForMap(query1,param);
//                     }catch(Exception e){
//                     mapOfExpenseInfo=new HashMap<String,Object>();
//                     e.printStackTrace();
//                     }
                      
//                     query1=("Select DATE_FORMAT(check_in_time,'%d/%m/%Y') as count_date,count(check_in_time) as counted_leads from appointments where (check_in_time between ? AND ? AND check_out_time between ? AND ?) AND assigned_id_fk=? group by DATE_FORMAT(check_in_time,'%d/%m/%Y')");
//                      param=new Object[]{dateOfMonday,endDate,dateOfMonday,endDate,userId1};
//                      try{
//                     ListOfGraphInfo=FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query1, param, new RowMapper<HashMap>() {
//                        public HashMap mapRow(ResultSet rs, int i) throws SQLException {
//                        HashMap team = new HashMap();
//                        Date dt1=new Date();
//                        team.put("Visit count",rs.getInt("counted_leads"));
//                        team.put("Dates",rs.getString("count_date"));
//                        return team;
//                     }
//                        
//                     });
//                     
//                     
//                     }catch(Exception e){
//                     mapOfGraphInfo=new HashMap<String,Object>();
//                     e.printStackTrace();
//                     }
                      
                      
                     query1=("select tc.category_name from user_territory as ut join territory_categories as tc on ut.teritory_id=tc.id where ut.user_id_fk=? ");
                      param=new Object[]{userId1};
                      try{
                     listOfTerriorty=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForList(query1,String.class,param);
                     }catch(Exception e){
                     listOfTerriorty=new ArrayList<String>();
//                     e.printStackTrace();
                     log4jLog.info("selectUserPositionsMobile >> "+e);      
                     }
                      mapOfUserInfo.put("Terriorty", listOfTerriorty);
//                    Location homeLocation = new Location();
//                    homeLocation.setLatitude(rs.getDouble("homelatitude"));
//                    homeLocation.setLangitude(rs.getDouble("homelongitude"));
//                    user.setHomeLocation(homeLocation);
//              
//                    Location officeLocation = new Location();
//                    officeLocation.setLatitude(rs.getDouble("officelatitude"));
//                    officeLocation.setLangitude(rs.getDouble("officelongitude"));
//                    user.setOfficeLocation(officeLocation);
//
//                    teamMember.setUser(user);
//
//                    User createdBy = new User();
//                    createdBy.setId(rs.getInt("created_by_id_fk"));
//                    teamMember.setCreatedBy(createdBy);
//
//                    String punchIn = rs.getString("punchIn");
//                    String punchOut = rs.getString("punchOut");
//                    if (punchIn.equals("0")) {
//                        teamMember.setIsOnline(false);
//                    } else if (punchOut.equals("00:00:00")) {
//                        teamMember.setIsOnline(true);
//                    } else {
//                        teamMember.setIsOnline(false);
//                    }
//                    teamMember.setCreatedOn(rs.getTimestamp("created_on"));
                       if (punchIn.equals("0")) {
                        mapOfUserInfo.put("isOnline",false);
                    } else if (punchOut.equals("00:00:00")) {
                        mapOfPunchInInfo.put("isOnline",true);
                        //mapOfUserInfo.put("status","PunchIn");
                    } else {
                        mapOfPunchInInfo.put("isOnline",false);
                    }
                       if(punchDate.equals(todaysDate)){
                        todaysPunch=true;
                        }else{
                        todaysPunch=false;
                        }
                        int inMeeting = rs.getInt("inMeeting");
                        if(inMeeting == 1){
                       mapOfUserInfo.put("status","In Meeting");
                       }else if (punchOut.equals("00:00:00") && !punchIn.equals("0")) {
                           if(strLastKnowLat.equals(strOfficeLat) && strlastKnowLong.equals(strOfficelong)){
                       mapOfUserInfo.put("status","In Office");
                       }else{
                        mapOfUserInfo.put("status","On Field");
                           }
                       }else if((punchOut.equals("00:00:00") && punchIn.equals("00:00:00")) ||(!punchOut.equals("00:00:00") && !punchIn.equals("00:00:00") && !todaysPunch)){
                       mapOfUserInfo.put("status","Not Punched-in");
                       }else if(!(punchOut.equals("00:00:00")) && !(punchIn.equals("00:00:00")) && todaysPunch){
                       mapOfUserInfo.put("status","Punched-out");
                       }else{
                       mapOfUserInfo.put("status","unknown");
                       }
                       
                       
                    if (date.equals(modifiedDate)) {
                        int canTakeCall = rs.getInt("canTakeCalls");
                        if (canTakeCall == 1) {
                           // teamMember.setCanTakeCalls(true);
                            mapOfUserInfo.put("canTakeCalls",true);
                        } else {
                           // teamMember.setCanTakeCalls(false);
                            mapOfUserInfo.put("canTakeCalls",false);
                        }
                        if (inMeeting == 1) {
                          //  teamMember.setInMeeting(true);
                            mapOfUserInfo.put("inMeeting",true);
                        } else {
                          //  teamMember.setInMeeting(false);
                            mapOfUserInfo.put("inMeeting",false);
                        }
                    } else {
                       mapOfUserInfo.put("canTakeCalls",false);
                       mapOfUserInfo.put("inMeeting",false);

                  }
//                    int hasSubOrdinates = rs.getInt("has_subordinates");
//                    if (hasSubOrdinates == 1) {
//                       //teamMember.setHasSubordinate(true);
//                        mapOfUserInfo.put("hasSubordinates",true);
//                  } else {  
//                        //teamMember.setHasSubordinate(false);
//                        mapOfUserInfo.put("hasSubordinates",false);
//                    }
                    //listOfTeams.add(teamMember);homelatitude
                     
                     mapOfUser.put("user_info",mapOfUserInfo);
                     mapOfAppointment.put("appointment_info", mapOfAppointmentInfo);
                     mapOfPunchDetails.put("punch_in_info", mapOfPunchInInfo);
                    // mapOfExpense.put("expense_info", mapOfExpenseInfo);
                     //mapOfGraph.put("Graph Info",ListOfGraphInfo);
                     listOfData.add(mapOfUser);
                     listOfData.add(mapOfAppointment);
                     listOfData.add(mapOfPunchDetails);
                    //listOfData.add(mapOfExpense);
                    // listOfData.add(mapOfGraph);
                    ///mapWithIdList.put(Integer.toString(userId1), listOfData);
                     listOfTeamData.add(listOfData);
                }
            }
            
            
//                    ResultSet rs1 = stmt.executeQuery(query3.toString());
//                     while (rs1.next()) {
//                     
//                     }
            //mapOfAppointmentInfo=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForMap(query,param);
            //param=new Object[]{starTime,endTime,userId1};
            
        } catch (Exception e) {
//            e.printStackTrace();
            log4jLog.info("selectUserPositionsMobile >> "+e);
            
        } finally {
            try {
                connection.close();
                stmt.close();
            } catch (SQLException ex) {
//                ex.printStackTrace();
                log4jLog.info("selectUserPositionsMobile >> "+ex);                
            }
        }
        return listOfTeamData;
    }

    public List<Object> selectUserDetailedPositionsMobile(int userId, String date, final int accountId) {
        String query1= " ";
        final String todayDate=date;
       Object[] param ={};
        String dateOfMonday;
        String strStartDate="";
        List<Object> listOfTeamData=new ArrayList<Object>();
        try{
            
        java.sql.Timestamp startDate = java.sql.Timestamp.valueOf(date+" 00:00:00");
             //
         date=startDate.toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c3 = Calendar.getInstance();
            c3.setTime(sdf.parse(date));
            c3.add(Calendar.MINUTE, -330);  // to get time 5:30 hrs back.
            strStartDate = sdf.format(c3.getTime());
        
        //
        
        
            String endDate = strStartDate;  // Start date
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(endDate));
            c.add(Calendar.DATE, 1);  // number of days to add1970-01-01 05:30:00
            endDate = sdf.format(c.getTime());  // dt is now the new date
           // endDate=endDate +" 00:00:00";
            Map<String,Object> mapOfExpenseInfo=new HashMap<String, Object>();
            List<Integer> listOfVisitCount=new ArrayList<Integer>();
            Map<String,Object> mapOfUserInfo=new HashMap<String, Object>();
            Map<String,Object> mapOfExpense=new HashMap<String, Object>();
            Map<String,Object> mapOfAppointmentCount=new HashMap<String, Object>();
            Map<String,Object> mapOfUsers=new HashMap<String, Object>();
            Map<String,Object> mapOfGraphInfo=new HashMap<String, Object>();
            Map<String,Object> mapOfAppointmentInfo=new HashMap<String, Object>();
            Map<String,Object> mapOfAppointment=new HashMap<String, Object>();
            List<HashMap> ListOfGraphInfo=new ArrayList<HashMap>();
            Map<String,Object> mapOfGraph=new HashMap<String, Object>();
            
            //Get the date before 7 days.
            String weekBeforeDate = strStartDate; // Start date
           // SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c1 = Calendar.getInstance();
            c1.setTime(sdf.parse(weekBeforeDate));
            c1.add(Calendar.DATE, -6);  // number of days to add1970-01-01 05:30:00
            weekBeforeDate = sdf.format(c1.getTime());  // dt is now the new date
           // weekBeforeDate=weekBeforeDate +" 00:00:00";
            Calendar c2=Calendar.getInstance(Locale.UK);
            c2.setTime(sdf.parse(date));
            c2.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
            dateOfMonday=sdf.format(c2.getTime());
            c3.setTime(sdf.parse(dateOfMonday));
            c3.add(Calendar.MINUTE, -330);  // to get time 5:30 hrs back.
            dateOfMonday = sdf.format(c3.getTime());
           // dateOfMonday=dateOfMonday +" 00:00:00";
            //
//            System.out.println("Start date"+startDate+"Str start date"+strStartDate+"One week before"+weekBeforeDate+"monday"+dateOfMonday+"end date"+endDate);
            try{
//                      query1=("select IFNULL(u.id,0) id,IFNULL(u.full_name,'') fullName,IFNULL(u.first_name,'') firstName,IFNULL(u.last_name,'') lastName,IFNULL(u.mobile_number,0) mobileNo,u.last_known_latitude lastKnownLat,IFNULL(u.role,0) role,IFNULL(app.appointment_title,'') appointmentTitle,IFNULL(l.latitude,0) customerLat,IFNULL(l.longitude,0) customerLong,IFNULL(cu.customer_name,'') customerName,u.last_known_langitude lastKnownLong,IFNULL(ukm.key_value,0) inMeeting,"
//                              + "u.last_known_location_time as lastKnownLocationTime,u.last_known_location lastKnowLocation,IFNULL(u.office_latitude,0) officelatitude,IFNULL(u.office_langitude,0) officelongitude,IFNULL(a.punch_in,0) punchIn,IFNULL(a.punch_in_location,'') punchInLocation,IFNULL(a.punch_out_location,'') "
//                              + "punchOutLocation,IFNULL(a.punch_out,0) punchOut,IFNULL(a.punch_date,0) punchDate from fieldsense.users u " +
//                              "LEFT JOIN appointments app ON app.assigned_id_fk=u.id AND status=1 AND appointment_time>CURDATE() " +
//                              "LEFT JOIN customers cu ON app.customer_id_fk=cu.id " +
//                              "LEFT JOIN location l ON l.location_type_id_fk=cu.id " +
//                              "LEFT OUTER JOIN attendances a ON a.user_id_fk=u.id AND a.punch_date = CURDATE() " +
//                              "LEFT OUTER JOIN user_keys ukm ON ukm.user_id_fk=u.id AND ukm.user_key='InMeeting' " +
//                              "where u.id=?");
                      
                // added by jyoti for testing of punch in date 13-april-2017
                query1=("SELECT DISTINCT IFNULL(u.id,0) id,IFNULL(u.full_name,'') fullName,IFNULL(u.first_name,'') firstName,IFNULL(u.last_name,'') lastName,"
                        + " IFNULL(u.mobile_number,0) mobileNo,u.last_known_latitude lastKnownLat,IFNULL(u.role,0) role,u.last_known_langitude lastKnownLong,"
                        + " u.last_known_location_time as lastKnownLocationTime,u.last_known_location lastKnowLocation,IFNULL(u.office_latitude,0) officelatitude,"
                        + " IFNULL(u.office_langitude,0) officelongitude,IFNULL(u.designation,'') designation,"
                        + " IFNULL(a.punch_in,0) punchIn,IFNULL(a.punch_in_location,'') punchInLocation,IFNULL(a.punch_out_location,'') punchOutLocation,"
                        + " IFNULL(a.punch_out,0) punchOut,IFNULL(a. punch_out_date,0) punchOutDate,IFNULL(a.punch_date,0) punchDate, IFNULL(app.appointment_title,'') appointmentTitle, IFNULL(cu.customer_name,'') customerName, IFNULL(cu.address1,'') customerAddress," // Added by siddhesh, "  IFNULL(cu.address1,'') customerAddress, "
                        + " IFNULL(a.punch_in_latitude,0) punchInLatitude,IFNULL(a.punch_in_langitude,0) punchInLangitude,IFNULL(a.punch_out_latitude,0) punchOutLatitude,IFNULL(a.punch_out_langitude,0) punchOutLangitude,"
                        + " IFNULL(l.latitude,0) customerLat,IFNULL(l.longitude,0) customerLong, IFNULL(ukm.key_value,0) inMeeting FROM fieldsense.users as u "
                        + " LEFT OUTER JOIN attendances as a ON u.id=a.user_id_fk AND a.id=(SELECT MAX(id) FROM  attendances att WHERE att.user_id_fk=u.id) "
                        + " LEFT JOIN appointments app ON app.assigned_id_fk=u.id AND app.status=1 AND check_in_time>CURDATE() "
                        + " LEFT JOIN customers cu ON app.customer_id_fk=cu.id LEFT JOIN location l ON l.location_type_id_fk=cu.id "
                        + " LEFT OUTER JOIN user_keys ukm ON ukm.user_id_fk=u.id AND ukm.user_key='InMeeting' WHERE u.id=?");
                      
                param=new Object[]{userId};
                mapOfUserInfo= FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query1, param, new RowMapper<HashMap>() {
                    double lastKnowLat;
                    double lastKnowLong;
                    double officeLat;
                    double officelong;
                    String strLastKnowLat;
                    String strlastKnowLong;
                    String strOfficeLat;
                    String strOfficelong;
                    String punchDate;
                    String punchOutDate;
                    boolean todaysPunch;
                    Timestamp lastknownTimeForUser;
                    public HashMap mapRow(ResultSet rs, int i) throws SQLException {
                        HashMap team = new HashMap();
                        int teamId=rs.getInt("id");
                        team.put("id",rs.getInt("id"));
                        team.put("fullName",rs.getString("fullName"));
                        team.put("firstName",rs.getString("firstName"));
                        team.put("lastName",rs.getString("lastName"));                        
                        int role=rs.getInt("role");
                        lastknownTimeForUser=rs.getTimestamp("lastKnownLocationTime");
                        if(role==1){
                            team.put("role","admin");
                        }else if(role==5){
                            team.put("role","onField");
                        }else{
                            team.put("role","inValid");
                        }

                        String punchIn = rs.getString("punchIn");
                        String punchOut = rs.getString("punchOut");
                  //     team.put("punch in testing by jyoti", punchIn); // added by jyoti
                      //  team.put("punch Out testing by jyoti", punchOut); // added by jyoti
                        officeLat=rs.getDouble("officelatitude");
                        officelong=rs.getDouble("officelongitude");
                        lastKnowLat=rs.getDouble("lastKnownLat");
                        lastKnowLong=rs.getDouble("lastKnownLong");
                        team.put("lastKnownLat",lastKnowLat);
                        team.put("lastKnownLong",lastKnowLong);
                      //  team.put("lastKnownLocationTime", lastknownTimeForUser);
                        punchDate=rs.getString("punchDate");
                        punchOutDate=rs.getString("punchOutDate");
                        strOfficeLat=TeamDaoImpl.convertDouble(officeLat);
                        strOfficelong=TeamDaoImpl.convertDouble(officelong);
                        strLastKnowLat=TeamDaoImpl.convertDouble(lastKnowLat);
                        strlastKnowLong=TeamDaoImpl.convertDouble(lastKnowLong);
                        int inMeeting = rs.getInt("inMeeting");
                        if(punchDate.equals(todayDate)){
                        todaysPunch=true;
                        }else{
                        todaysPunch=false;
                        }
                         java.sql.Timestamp punchOutTimestamp=new Timestamp(0);
                        if(!punchOutDate.equals("0")){
                        punchOutDate=punchOutDate+" "+punchOut;
                      //  System.out.println("hdfwihqwiodhiowd"+punchOutDate);
                       punchOutTimestamp = java.sql.Timestamp.valueOf(punchOutDate) ;
                        }else{
                        punchOutTimestamp=lastknownTimeForUser;
                        }
                        
                        if(inMeeting == 1){
                            String customerAddress = rs.getString("customerAddress");
                           team.put("status","In Meeting");
                           team.put("customerAddress", customerAddress); // added by siddhesh
//                            System.out.println("customerAddress : "+ customerAddress);
                           team.put("customerName",rs.getString("customerName"));
                           team.put("customerLat",rs.getDouble("customerLat"));
                           team.put("customerLong",rs.getDouble("customerLong"));
                           team.put("appointmentTitle",rs.getString("appointmentTitle"));
                           team.put("inMeeting",true);
                           team.put("punchLatitude", rs.getString("punchInLatitude"));
                           team.put("punchLangitude", rs.getString("punchInLangitude")); 
                           team.put("lastKnownLocationTime", lastknownTimeForUser);
                        }else if (punchOut.equals("00:00:00") && !punchIn.equals("0") ) {
                            if(strLastKnowLat.equals(strOfficeLat) && strlastKnowLong.equals(strOfficelong)){
                                team.put("status","In Office");
                            }else{
                                team.put("status","On Field");
                            }
                        team.put("punchLatitude", rs.getString("punchInLatitude"));
                        team.put("punchLangitude", rs.getString("punchInLangitude")); 
                       team.put("lastKnownLocationTime", lastknownTimeForUser);
                        }else if((punchOut.equals("00:00:00") && punchIn.equals("00:00:00"))||(!punchOut.equals("00:00:00") && !punchIn.equals("00:00:00") && !todaysPunch)){
                            team.put("status","Not Punched-in");
                            team.put("lastKnownLocationTime", punchOutTimestamp);
                        }else if(!(punchOut.equals("00:00:00")) && !(punchIn.equals("00:00:00")) && todaysPunch){
                            team.put("status","Punched-out");
                            team.put("lastKnownLocationTime", punchOutTimestamp);
                        team.put("punchLatitude", rs.getString("punchOutLatitude"));
                        team.put("punchLangitude", rs.getString("punchOutLangitude"));
                        }else{
                            team.put("staus", "unknown");
                         team.put("lastKnownLocationTime", punchOutTimestamp);
                        }

                       // team.put("lastKnownLocationTime",rs.getTimestamp("lastKnownLocationTime"));
                        String lastKnownLocation=rs.getString("lastKnowLocation");
                        lastKnownLocation=lastKnownLocation.replace("\n"," ").trim();
                        team.put("lastKnowLocation",lastKnownLocation);
                        team.put("punchDate",punchDate);
                        team.put("punchIn",punchIn);
                        team.put("designation", rs.getString("designation"));
                        team.put("punchOut",punchOut);
                        team.put("mobileNo",rs.getString("mobileNo"));
                        team.put("punchOutLocation",rs.getString("punchOutLocation"));
                        team.put("punchInLocation",rs.getString("punchInLocation"));
                        return team;
                    }
                });
            }catch(Exception e){
                mapOfUserInfo=new HashMap<String,Object>();
                e.printStackTrace();
                log4jLog.info("selectUserDetailedPositionsMobile >> "+e);
            }
            
            //
            
            
//            query1=("select IFNULL(u.id,0) id,IFNULL(u.full_name,'') fullName,IFNULL(u.first_name,'') firstName,IFNULL(u.last_name,'') lastName,u.last_known_latitude lastKnownLat,IFNULL(u.role,0) role,u.last_known_langitude lastKnownLong,u.last_known_location_time as lastKnownLocationTime,u.last_known_location lastKnowLocation,IFNULL(a.punch_in,0) punchIn,IFNULL(a.punch_in_location,'') punchInLocation,IFNULL(a.punch_out_location,'') punchOutLocation,IFNULL(a.punch_out,0) punchOut from fieldsense.users u LEFT OUTER JOIN attendances a ON a.user_id_fk=u.id AND a.punch_date = CURDATE() where u.id=?");
//             param=new Object[]{userId};
//             try{
//                     mapOfUserInfo=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForMap(query1,param);
//                     }catch(Exception e){
//                     mapOfUserInfo=new HashMap<String,Object>();
//                     e.printStackTrace();
//                     }
            String url=FieldSenseUtils.getPropertyValue("PROFILEPIC_GET_PATH");
            String pattern = "E MMMM dd yyyy HH:mm:ss";
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            //System.out.println("url HH "+url+accountId+"_"+userId+"_140X140.png?"+format.format(new Date()));
            String profilePicUrl=url+accountId+"_"+userId+"_140X140.png?"+format.format(new Date());
            mapOfUserInfo.put("profilePic",profilePicUrl);            
//            String accountSettingCurrency = setAccountSettingsCurrency("currency_symbol", accountId);   // Added By Jyoti, 31-08-2017            
//            query1=("SELECT IFNULL(expense_name,'') AS expenseName,IFNULL(desription,'') AS desription,IFNULL(amount_spent,0) AS amountSpent, IFNULL(currency_symbol,'"+accountSettingCurrency+"') AS expenseCurrency, COUNT(id) AS count,IFNULL((SELECT SUM(amount_spent) FROM expenses WHERE expense_time BETWEEN ? AND ? AND user_id_fk=?),0) AS amountTotal FROM expenses WHERE expense_time BETWEEN ? AND ? AND user_id_fk=? GROUP BY id ORDER BY expense_time DESC LIMIT 1");
            query1=("SELECT IFNULL(expense_name,'') AS expenseName,IFNULL(desription,'') AS desription,IFNULL(amount_spent,0) AS amountSpent, COUNT(id) AS count,IFNULL((SELECT SUM(amount_spent) FROM expenses WHERE expense_time BETWEEN ? AND ? AND user_id_fk=?),0) AS amountTotal FROM expenses WHERE expense_time BETWEEN ? AND ? AND user_id_fk=? GROUP BY id ORDER BY expense_time DESC LIMIT 1");
            
            param=new Object[]{strStartDate,endDate,userId,strStartDate,endDate,userId};
            try{
                mapOfExpenseInfo=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForMap(query1,param);
            }catch(Exception e){
                mapOfExpenseInfo=new HashMap<String,Object>();
                e.printStackTrace();
                log4jLog.info("selectUserDetailedPositionsMobile >> "+e);
            }

                     //query1=("select IFNULL(app.appointment_title, '') as appointmentTitle,IFNULL(cu.customer_name, '') as customerName,IFNULL(cu.customer_location_identifier,'') as customerLocationIdentifier,(select IFNULL(count(app.id),0) from appointments app join customers cu on app.customer_id_fk=cu.id where app.appointment_time BETWEEN ? AND ? AND app.status=0 AND app.assigned_id_fk=? AND app.record_state!=3) as yetToVisit,IFNULL((select count(id) from appointments where appointment_time BETWEEN ? AND ? AND assigned_id_fk=? AND record_state!=3),0) totalVisit from appointments app join customers cu on app.customer_id_fk=cu.id where app.appointment_time BETWEEN ? AND ? AND app.assigned_id_fk=? AND app.record_state!=3 order by app.appointment_time asc limit 1");
  //sidd    //query1=("select IFNULL(app.appointment_title, '') as appointmentTitle,IFNULL(cu.customer_name, '') as customerName,IFNULL(cu.customer_location_identifier,'') as customerLocationIdentifier  from appointments app join customers cu on app.customer_id_fk=cu.id where app.appointment_time BETWEEN ? AND ? AND app.status=0 AND app.assigned_id_fk=? AND app.record_state!=3 order by app.appointment_time asc limit 1");
            query1=("select IFNULL(app.appointment_title, '') as appointmentTitle,IFNULL(ac.purpose, '') as purpose,IFNULL(cu.customer_name, '') as customerName,IFNULL(cu.customer_location_identifier,'') as customerLocationIdentifier  from appointments app join customers cu on app.customer_id_fk=cu.id join activity_purpose as ac on app.purpose_id_fk=ac.id  where app.appointment_time BETWEEN ? AND ? AND (app.status=0 OR app.status=1) AND app.assigned_id_fk=? AND app.record_state!=3 order by app.appointment_time asc limit 1");
            param=new Object[]{strStartDate,endDate,userId}; 
            try{
                mapOfAppointmentInfo=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForMap(query1,param);
            }catch(Exception e){
                mapOfAppointmentInfo.put("appointmentTitle", "");
                      mapOfAppointmentInfo.put("customerName", "");
                      mapOfAppointmentInfo.put("customerLocationIdentifier", "");
                     // mapOfAppointmentInfo.put("yetToVisit", 0);
                      //mapOfAppointmentInfo.put("totalVisit", 0);
//                e.printStackTrace();
                      log4jLog.info("selectUserDetailedPositionsMobile >> "+e);
            }  
            
            try{
            query1=("select IFNULL(count(app.id),0) yetToVisit,IFNULL((select count(id) from appointments where appointment_time BETWEEN ? AND ? AND assigned_id_fk=? AND record_state!=3),0) totalVisit from appointments app join customers cu on app.customer_id_fk=cu.id where app.appointment_time BETWEEN ? AND ? AND (app.status=0 OR app.status=3) AND app.assigned_id_fk=? AND app.record_state!=3");
            param=new Object[]{strStartDate,endDate,userId,strStartDate,endDate,userId}; 
            mapOfAppointmentCount=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForMap(query1, param);
            mapOfAppointmentInfo.putAll(mapOfAppointmentCount);
            }catch(Exception e){
                mapOfAppointmentInfo.put("yetToVisit", 0);
                mapOfAppointmentInfo.put("totalVisit", 0);
//            e.printStackTrace();
                log4jLog.info("selectUserPositionsWithSubs >> "+e);
            }
            try{
                query1=("Select DATE_FORMAT(check_in_time,'%m/%d/%Y') as count_date,count(check_in_time) as counted_leads from appointments where (check_in_time between ? AND ? AND check_out_time between ? AND ?) AND assigned_id_fk=? AND status=2 group by DATE_FORMAT(check_in_time,'%m/%d/%Y')");
                param=new Object[]{dateOfMonday,endDate,dateOfMonday,endDate,userId};
                ListOfGraphInfo= FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query1, param, new RowMapper<HashMap>() {
                    public HashMap mapRow(ResultSet rs, int i) throws SQLException {
                        HashMap team = new HashMap();
                        team.put("Visit count",rs.getInt("counted_leads"));
                        team.put("Dates",rs.getString("count_date"));
                        return team;
                    }
                });
            }catch(Exception e){
                mapOfGraphInfo=new HashMap<String,Object>();
//                e.printStackTrace();
                log4jLog.info("selectUserDetailedPositionsMobile >> "+e);
            }
            mapOfUsers.put("user_info",mapOfUserInfo);
            mapOfExpense.put("expense_info", mapOfExpenseInfo);
            mapOfAppointment.put("appointment_info", mapOfAppointmentInfo);
            mapOfGraph.put("Graph Info",ListOfGraphInfo); 
            listOfTeamData.add(mapOfUsers);
            listOfTeamData.add(mapOfAppointment);
            listOfTeamData.add(mapOfExpense);
            listOfTeamData.add(mapOfGraph);            
        }catch(Exception e){
//            e.printStackTrace();
            log4jLog.info("selectUserDetailedPositionsMobile >> "+e);
        }
        return listOfTeamData;
    }
    //Siddhesh: To get lat long in short format,Getting less accurate location.
    //For my team used in this class only no declaration in dao.
    public static String convertDouble(double d){
        return String.format("%.02f", d);
    }

    /**
     * @Added by jyoti
     * @param key
     * @param accountId
     * @return 
     */
    public String setAccountSettingsCurrency(String key, int accountId){
        String accSettingQuery = "SELECT value FROM account_settings WHERE name = ? ";        
        Object param[] = new Object[]{key};
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(accSettingQuery, param,String.class);
        } catch (Exception e) {
            log4jLog.info("setAccountSettingsCurrency" + e);
            return null;
        }
    }
    
    public List<TeamMember> selectUserPositionsWithSubs(int userId, String date, int accountId) {
        
        String query = "SELECT team_position_csv FROM teams WHERE user_id_fk=?";
        log4jLog.info("selectUserPositions" + query);
        Connection connection = null;
        Statement stmt = null;
        Object[] param = new Object[]{userId};
        try {
            connection = FieldSenseUtils.getDataSourceForAccount(accountId).getConnection();
            stmt = connection.createStatement();
            String userPosition = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class);
            StringBuilder query2 = new StringBuilder();
//            String query2 = " SELECT id, team_name, description, user_id_fk, is_active, created_on, created_by_id_fk,team_position_csv FROM teams WHERE MATCH(team_position_csv) AGAINST(" + userPosition + " IN BOOLEAN MODE)";
            query2.append("SELECT DISTINCT(t.id), t.team_name, t.description, t.user_id_fk, t.is_active, t.created_on, ");
            query2.append("t.created_by_id_fk,t.team_position_csv ,u.first_name,u.last_name,u.last_known_latitude,");
            query2.append("u.last_known_langitude,u.last_known_location_time,u.last_known_location,IFNULL(a.punch_in,0) punchIn,");
            query2.append("IFNULL(a.punch_out,0) punchOut,IFNULL(uk.key_value,0) canTakeCalls,IFNULL(ukm.key_value,0) inMeeting,");
            query2.append("IFNULL(u.office_latitude,0) officelatitude,IFNULL(u.office_langitude,0) officelongitude,");
            query2.append("IFNULL(u.home_latitude,0) homelatitude,IFNULL(u.home_langitude,0) homelongitude,IFNULL(DATE(uk.modified_on),DATE(now())) uk_modifiedDay,now() new_uk_modified_date,t.has_subordinates");
            query2.append(" FROM teams t ");
            query2.append(" INNER JOIN fieldsense.users u ON u.id=t.user_id_fk");
            query2.append(" LEFT OUTER JOIN attendances a ON a.user_id_fk=t.user_id_fk AND a.id=(SELECT MAX(id) FROM  attendances att WHERE att.user_id_fk=u.id)");   
//            query2.append(" LEFT OUTER JOIN attendances a ON a.user_id_fk=t.user_id_fk AND a.punch_date = CURDATE()"); // commented to solve the issue of punch in on previous date - by siddhesh
            query2.append(" LEFT OUTER JOIN user_keys uk ON uk.user_id_fk=u.id AND uk.user_key='CanTakeCalls'");
            query2.append(" LEFT OUTER JOIN user_keys ukm ON ukm.user_id_fk=u.id AND ukm.user_key='InMeeting'");
            query2.append(" WHERE team_position_csv LIKE '%" + userPosition + "%' AND u.active=1 order by u.first_name asc");
            ResultSet rs = stmt.executeQuery(query2.toString());
            List<TeamMember> listOfTeams = new ArrayList<TeamMember>();
            int userIdNew=0;      
            while (rs.next()) {
                int userPositionsArrayLength = userPosition.split(",").length;
                String userPosition2 = rs.getString("team_position_csv");
                int userPositions2ArrayLength = userPosition2.split(",").length;
                if ((userPositionsArrayLength + 1 == userPositions2ArrayLength) || (userPositionsArrayLength == userPositions2ArrayLength)) {
                    String modifiedDate = rs.getString("uk_modifiedDay");

                    TeamMember teamMember = new TeamMember();
                    teamMember.setTeamId(rs.getInt("id"));
                    
                    User user = new User();
                    userIdNew=rs.getInt("user_id_fk");
                    user.setId(userIdNew);
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setLangitude(rs.getDouble("last_known_langitude"));
                    user.setLatitude(rs.getDouble("last_known_latitude"));
                    user.setLastKnownLocationTime(rs.getTimestamp("last_known_location_time"));
                    user.setLastKnownLocation(rs.getString("last_known_location"));

                    Location homeLocation = new Location();
                    homeLocation.setLatitude(rs.getDouble("homelatitude"));
                    homeLocation.setLangitude(rs.getDouble("homelongitude"));
                    user.setHomeLocation(homeLocation);

                    Location officeLocation = new Location();
                    officeLocation.setLatitude(rs.getDouble("officelatitude"));
                    officeLocation.setLangitude(rs.getDouble("officelongitude"));
                    user.setOfficeLocation(officeLocation);

                    teamMember.setUser(user);

                    User createdBy = new User();
                    createdBy.setId(rs.getInt("created_by_id_fk"));
                    teamMember.setCreatedBy(createdBy);

                    String punchIn = rs.getString("punchIn");
                    String punchOut = rs.getString("punchOut");
                    if (punchIn.equals("0")) {
                        teamMember.setIsOnline(false);
                    } else if (punchOut.equals("00:00:00")) {
                        teamMember.setIsOnline(true);
                    } else {
                        teamMember.setIsOnline(false);
                    }
                    teamMember.setCreatedOn(rs.getTimestamp("created_on"));
                    if (date.equals(modifiedDate)) {
                        int canTakeCall = rs.getInt("canTakeCalls");
                        if (canTakeCall == 1) {
                            teamMember.setCanTakeCalls(true);
                        } else {
                            teamMember.setCanTakeCalls(false);
                        }
                        int inMeeting = rs.getInt("inMeeting");
                        if (inMeeting == 1) {
                            teamMember.setInMeeting(true);
                        } else {
                            teamMember.setInMeeting(false);
                        }
                    } else {
                        teamMember.setCanTakeCalls(false);
                        teamMember.setInMeeting(false);

                        UserKayDaoImpl userKayDaoImpl = new UserKayDaoImpl();

                        UserKey keyCantakeCall = new UserKey();
                        keyCantakeCall.setKeyValue("0");
                        User userCanTakeCall = new User();
                        userCanTakeCall.setId(rs.getInt("user_id_fk"));
                        keyCantakeCall.setUserId(userCanTakeCall);
                        keyCantakeCall.setUserKay("CanTakeCalls");
                        userKayDaoImpl.updateUserKeys(keyCantakeCall, accountId);

                        UserKey keyInMeeting = new UserKey();
                        keyInMeeting.setKeyValue("0");
                        keyInMeeting.setUserId(userCanTakeCall);
                        keyInMeeting.setUserKay("InMeeting");
                        userKayDaoImpl.updateUserKeys(keyInMeeting, accountId);

                    }
                    int hasSubOrdinates = rs.getInt("has_subordinates");
                    if (hasSubOrdinates == 1) {
                        teamMember.setHasSubordinate(true);
                        teamMember.setListOfSubs(this.selectUserPositionsWithSubsDetailedData(userIdNew, date, accountId));
                    } else {
                        teamMember.setHasSubordinate(false);
                    }
                    listOfTeams.add(teamMember);
                }
            }
//            System.out.println("listOfTeams:- "+listOfTeams);
            return listOfTeams;
        } catch (Exception e) {
            log4jLog.info("selectUserPositionsWithSubs >> "+e);
            return new ArrayList<TeamMember>();
        } finally {
            try {
                stmt.close();
                connection.close();                
            } catch (SQLException ex) {
                log4jLog.info("selectUserPositionsWithSubs >> "+ex);
                return new ArrayList<TeamMember>();
            }
        }
    }
public List<TeamMember> selectUserPositionsWithSubsDetailedData(int userId, String date, int accountId) {
        
        String query = "SELECT team_position_csv FROM teams WHERE user_id_fk=?";
        log4jLog.info("selectUserPositions" + query);
        Connection connection = null;
        Statement stmt = null;
        Object[] param = new Object[]{userId};
        try {
            connection = FieldSenseUtils.getDataSourceForAccount(accountId).getConnection();
            stmt = connection.createStatement();
            String userPosition = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class);
            StringBuilder query2 = new StringBuilder();
//            String query2 = " SELECT id, team_name, description, user_id_fk, is_active, created_on, created_by_id_fk,team_position_csv FROM teams WHERE MATCH(team_position_csv) AGAINST(" + userPosition + " IN BOOLEAN MODE)";
            query2.append("SELECT DISTINCT(t.id), t.team_name, t.description, t.user_id_fk, t.is_active, t.created_on, ");
            query2.append("t.created_by_id_fk,t.team_position_csv ,u.first_name,u.last_name,u.last_known_latitude,");
            query2.append("u.last_known_langitude,u.last_known_location_time,u.last_known_location,IFNULL(a.punch_in,0) punchIn,");
            query2.append("IFNULL(a.punch_out,0) punchOut,IFNULL(uk.key_value,0) canTakeCalls,IFNULL(ukm.key_value,0) inMeeting,");
            query2.append("IFNULL(u.office_latitude,0) officelatitude,IFNULL(u.office_langitude,0) officelongitude,");
            query2.append("IFNULL(u.home_latitude,0) homelatitude,IFNULL(u.home_langitude,0) homelongitude,IFNULL(DATE(uk.modified_on),DATE(now())) uk_modifiedDay,now() new_uk_modified_date,t.has_subordinates");
            query2.append(" FROM teams t ");
            query2.append(" INNER JOIN fieldsense.users u ON u.id=t.user_id_fk");
            query2.append(" LEFT OUTER JOIN attendances a ON a.user_id_fk=t.user_id_fk AND a.id=(SELECT MAX(id) FROM  attendances att WHERE att.user_id_fk=u.id)");   
//            query2.append(" LEFT OUTER JOIN attendances a ON a.user_id_fk=t.user_id_fk AND a.punch_date = CURDATE()"); // commented to solve the issue of punch in on previous date - by siddhesh
            query2.append(" LEFT OUTER JOIN user_keys uk ON uk.user_id_fk=u.id AND uk.user_key='CanTakeCalls'");
            query2.append(" LEFT OUTER JOIN user_keys ukm ON ukm.user_id_fk=u.id AND ukm.user_key='InMeeting'");
            query2.append(" WHERE team_position_csv LIKE '%" + userPosition + "%' AND u.active=1 order by u.first_name asc");
            ResultSet rs = stmt.executeQuery(query2.toString());
            List<TeamMember> listOfTeams = new ArrayList<TeamMember>();
            int userIdNew=0;
            while (rs.next()) {
                int userPositionsArrayLength = userPosition.split(",").length;
                String userPosition2 = rs.getString("team_position_csv");
                userIdNew=rs.getInt("user_id_fk");
                int userPositions2ArrayLength = userPosition2.split(",").length;
                if (((userPositionsArrayLength + 1 == userPositions2ArrayLength) || (userPositionsArrayLength == userPositions2ArrayLength)) && userId!=userIdNew) {
                    String modifiedDate = rs.getString("uk_modifiedDay");

                    TeamMember teamMember = new TeamMember();
                    teamMember.setTeamId(rs.getInt("id"));
                    
                    User user = new User();
                    user.setId(rs.getInt("user_id_fk"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setLangitude(rs.getDouble("last_known_langitude"));
                    user.setLatitude(rs.getDouble("last_known_latitude"));
                    user.setLastKnownLocationTime(rs.getTimestamp("last_known_location_time"));
                    user.setLastKnownLocation(rs.getString("last_known_location"));

                    Location homeLocation = new Location();
                    homeLocation.setLatitude(rs.getDouble("homelatitude"));
                    homeLocation.setLangitude(rs.getDouble("homelongitude"));
                    user.setHomeLocation(homeLocation);

                    Location officeLocation = new Location();
                    officeLocation.setLatitude(rs.getDouble("officelatitude"));
                    officeLocation.setLangitude(rs.getDouble("officelongitude"));
                    user.setOfficeLocation(officeLocation);

                    teamMember.setUser(user);

                    User createdBy = new User();
                    createdBy.setId(rs.getInt("created_by_id_fk"));
                    teamMember.setCreatedBy(createdBy);

                    String punchIn = rs.getString("punchIn");
                    String punchOut = rs.getString("punchOut");
                    if (punchIn.equals("0")) {
                        teamMember.setIsOnline(false);
                    } else if (punchOut.equals("00:00:00")) {
                        teamMember.setIsOnline(true);
                    } else {
                        teamMember.setIsOnline(false);
                    }
                    teamMember.setCreatedOn(rs.getTimestamp("created_on"));
                    if (date.equals(modifiedDate)) {
                        int canTakeCall = rs.getInt("canTakeCalls");
                        if (canTakeCall == 1) {
                            teamMember.setCanTakeCalls(true);
                        } else {
                            teamMember.setCanTakeCalls(false);
                        }
                        int inMeeting = rs.getInt("inMeeting");
                        if (inMeeting == 1) {
                            teamMember.setInMeeting(true);
                        } else {
                            teamMember.setInMeeting(false);
                        }
                    } else {
                        teamMember.setCanTakeCalls(false);
                        teamMember.setInMeeting(false);

                        UserKayDaoImpl userKayDaoImpl = new UserKayDaoImpl();

                        UserKey keyCantakeCall = new UserKey();
                        keyCantakeCall.setKeyValue("0");
                        User userCanTakeCall = new User();
                        userCanTakeCall.setId(rs.getInt("user_id_fk"));
                        keyCantakeCall.setUserId(userCanTakeCall);
                        keyCantakeCall.setUserKay("CanTakeCalls");
                        userKayDaoImpl.updateUserKeys(keyCantakeCall, accountId);

                        UserKey keyInMeeting = new UserKey();
                        keyInMeeting.setKeyValue("0");
                        keyInMeeting.setUserId(userCanTakeCall);
                        keyInMeeting.setUserKay("InMeeting");
                        userKayDaoImpl.updateUserKeys(keyInMeeting, accountId);

                    }
                    int hasSubOrdinates = rs.getInt("has_subordinates");
                    if (hasSubOrdinates == 1) {
                       teamMember.setListOfSubs(selectUserPositionsWithSubsDetailedData(userIdNew, date, accountId));
                        teamMember.setHasSubordinate(true);
                    } else {
                        teamMember.setHasSubordinate(false);
                    }
                    listOfTeams.add(teamMember);
                }
            }
            return listOfTeams;
        } catch (Exception e) {
//            e.printStackTrace();
            log4jLog.info("selectUserPositionsWithSubsDetailedData >> "+e);
            return new ArrayList<TeamMember>();
        } finally {
            try {
                stmt.close();
                connection.close();                
            } catch (SQLException ex) {
                log4jLog.info("selectUserPositionsWithSubsDetailedData >> "+ex);
                return new ArrayList<TeamMember>();
            }
        }
    }
    //added by siddhesh
     // modified by manohar
    @Override
    public List<Team> selectUserSubordiatesPositionIdsForSuperAdmin(final int teamId, int accountId) {  
        //  100321
        
//        System.out.println("selectUserSubordiatesPositionIds");
            String query = "SELECT id,user_id_fk,team_position_csv  FROM teams where team_position_csv LIKE ?";
        Object param[] = new Object[]{"%" + teamId + "%"};
        log4jLog.info(" selectUserSubordiatesPositionIds " + query);
        try {
            return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<Team>() {
                @Override
                public Team mapRow(ResultSet rs, int i) throws SQLException {
                    Team team=new Team();          
                    String teamPositionCsv = rs.getString("team_position_csv");               //  100321,100320,100319,100318,100261,100000    
                    team.setId(rs.getInt("id"));
                    team.setUser_id(rs.getInt("user_id_fk")); 
                    if(teamPositionCsv.contains(","+teamId))
                    {                 
                        String s1=teamPositionCsv.replace(","+teamId,"");      //  100424
//                        System.out.println("replace="+s1);
			team.setParentCSV(s1);         //    100424,100422,100420,100391,100351,100211,100000		 
                    }                 
//                    System.out.println("n="+team.getParentCSV());
                    return team;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info(" selectUserSubordiatesPositionIds " + e);
            return new ArrayList<Team>();
        }
    }
    
    //Added by siddhesh
    public int checkSubordinatePersent(int teamId, int accountId) {
        try{
            
         
        String query="select count(id) idCount from teams where locate(?,team_position_csv,8)";
        Object[] param = new Object[]{Integer.toString(teamId)};
        int count= FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, Integer.class);
//            System.out.println("hdhqwid"+teamId+"djqjqjdoq"+count);
        return count;
        }catch(Exception e){
        e.printStackTrace();
        return 0;
        }
    }
    
        public List<Object> selectUserPositionsMobileForAllSubordinate(int userId, String date, int accountId,int subordinate) {
            String todaysDate=date;
            String dateOfMonday;
            String strStartDate="";
            String endDate="";
            try{
            //Siddhesh :-To solve count issue.Time 5:30 hrs back.
            //
//                System.out.println("Reacghe here"+subordinate);
            java.sql.Timestamp startDate = java.sql.Timestamp.valueOf(date+" 00:00:00");
            date=startDate.toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c3 = Calendar.getInstance();
            c3.setTime(sdf.parse(date));
            c3.add(Calendar.MINUTE, -330);  // to get time 5:30 hrs back.
            strStartDate = sdf.format(c3.getTime());
            //  
            endDate = strStartDate;  // Start date
           // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(endDate));
            c.add(Calendar.DATE, 1);  // number of days to add1970-01-01 05:30:00
            endDate = sdf.format(c.getTime());  // dt is now the new date
           // endDate=endDate +" 00:00:00";
            
             //Get the date before 7 days.
            String weekBeforeDate = strStartDate; // Start date
            //SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c1 = Calendar.getInstance();
            c1.setTime(sdf.parse(weekBeforeDate));
            c1.add(Calendar.DATE, -6);  // number of days to add1970-01-01 05:30:00
            weekBeforeDate = sdf.format(c1.getTime());  // dt is now the new date
            //
            
            
            Calendar c2=Calendar.getInstance(Locale.UK);
            c2.setTime(sdf.parse(date));
            c2.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
            dateOfMonday=sdf.format(c2.getTime());
            c3.setTime(sdf.parse(dateOfMonday));
            c3.add(Calendar.MINUTE, -330);  // to get time 5:30 hrs back.
            dateOfMonday = sdf.format(c3.getTime());
            }catch(Exception e){
            e.printStackTrace();
            return null;
            }
            if(subordinate==0){
      String query = "SELECT team_position_csv FROM teams WHERE user_id_fk=?";
        log4jLog.info("selectUserPositions" + query);
        Connection connection = null;
        Statement stmt = null;
        Object[] param = new Object[]{userId};
        List<Object> listOfTeamData=new ArrayList<Object>();
        try {
            connection = FieldSenseUtils.getDataSourceForAccount(accountId).getConnection();
            stmt = connection.createStatement();
            String userPosition = FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForObject(query, param, String.class);
            StringBuilder query2 = new StringBuilder();
            String query1 = "";
            query2.append("SELECT DISTINCT(t.id), t.team_name, t.description, t.user_id_fk, t.is_active, t.created_on, ");
            query2.append("t.created_by_id_fk,t.team_position_csv ,u.first_name,u.last_name,u.last_known_latitude,u.designation,IFNULL(u.role,0) role,IFNULL(u.mobile_number,0) mobileNumber,");
            query2.append("u.last_known_langitude,u.last_known_location_time,u.last_known_location,IFNULL(a.punch_in,0) punchIn,IFNULL(a.punch_date,0) punchDate,IFNULL(a.punch_in_location,'') punchInLocation,IFNULL(a.punch_out_location,'') punchOutLocation,");
            query2.append("IFNULL(a.punch_out,0) punchOut,IFNULL(uk.key_value,0) canTakeCalls,IFNULL(ukm.key_value,0) inMeeting,");
            query2.append("IFNULL(u.office_latitude,0) officelatitude,IFNULL(u.office_langitude,0) officelongitude,");
            query2.append("IFNULL(DATE(uk.modified_on),DATE(now())) uk_modifiedDay,now() new_uk_modified_date");
            query2.append(" FROM teams t ");
            query2.append(" INNER JOIN fieldsense.users u ON u.id=t.user_id_fk");
            query2.append(" LEFT OUTER JOIN attendances a ON a.user_id_fk=t.user_id_fk AND a.id=(SELECT MAX(id) FROM  attendances att WHERE att.user_id_fk=u.id)");
            query2.append(" LEFT OUTER JOIN user_keys uk ON uk.user_id_fk=u.id AND uk.user_key='CanTakeCalls'");
            query2.append(" LEFT OUTER JOIN user_keys ukm ON ukm.user_id_fk=u.id AND ukm.user_key='InMeeting'");
            query2.append(" WHERE team_position_csv LIKE '%" + userPosition + "%' AND u.active=1 order by u.first_name asc");
            ResultSet rs = stmt.executeQuery(query2.toString());
            List<TeamMember> listOfTeams = new ArrayList<TeamMember>();
            int userId1=0;
            int userRole;
            int teamId=0;
            double lastKnowLat;
            double lastKnowLong;
            double officeLat;
            double officelong;
            String strLastKnowLat;
            String strlastKnowLong;
            String strOfficeLat;
            String strOfficelong;
            boolean todaysPunch;
            while (rs.next()) {
                int userPositionsArrayLength = userPosition.split(",").length;
                String userPosition2 = rs.getString("team_position_csv");
                userId1=rs.getInt("user_id_fk");
                userRole=rs.getInt("role");
                int userPositions2ArrayLength = userPosition2.split(",").length;
                if ((userPositionsArrayLength + 1 == userPositions2ArrayLength) || (userPositionsArrayLength == userPositions2ArrayLength)) {
                    String modifiedDate = rs.getString("uk_modifiedDay");
                    teamId=rs.getInt("id");
                     Map<String,Object> mapOfUserInfo=new HashMap<String, Object>();
                     Map<String,Object> mapOfAppointmentInfo=new HashMap<String, Object>();
                     Map<String,Object> mapOfExpenseInfo=new HashMap<String, Object>();
                     Map<String,Object> mapOfPunchInInfo=new HashMap<String, Object>();
                     Map<String,Object> mapOfUser=new HashMap<String, Object>();
                     List<Integer> listOfVisitCount=new ArrayList<Integer>();
                     Map<String,Object> mapOfAppointmentCount=new HashMap<String, Object>();
                     Map<String,Object> mapOfAppointment=new HashMap<String, Object>();
                     Map<String,Object> mapOfExpense=new HashMap<String, Object>();
                      Map<String,Object> mapOfPunchDetails=new HashMap<String, Object>();
                      Map<String,Object> mapOfGraphInfo=new HashMap<String, Object>();
                      List<HashMap> ListOfGraphInfo=new ArrayList<HashMap>();
                      Map<String,Object> mapOfGraph=new HashMap<String, Object>();
                      List<String> listOfTerriorty=new ArrayList<String>();
                     List<Object> listOfData=new ArrayList<Object>();
                    mapOfUserInfo.put("userId",userId1);
                    mapOfUserInfo.put("firstName",rs.getString("first_name"));
                    mapOfUserInfo.put("subordinateCount",this.checkSubordinatePersent(teamId, accountId));
                     mapOfUserInfo.put("reportingHead", this.getParentName(teamId,accountId));
                    mapOfUserInfo.put("SubordianteInfo",this.getSubordinateName(teamId,accountId));
                    mapOfUserInfo.put("lastName",rs.getString("last_name"));
                    mapOfUserInfo.put("designation", rs.getString("designation"));
                    mapOfUserInfo.put("mobileNumber",rs.getString("mobileNumber"));
                    lastKnowLat=rs.getDouble("last_known_latitude");
                    lastKnowLong=rs.getDouble("last_known_langitude");
                    officeLat=rs.getDouble("officelatitude");
                    officelong=rs.getDouble("officelongitude");
                    mapOfUserInfo.put("lastKnownLat",lastKnowLat);
                    mapOfUserInfo.put("lastKnownLong",lastKnowLong);
                    strOfficeLat=convertDouble(officeLat);
                    strOfficelong=convertDouble(officelong);
                    strLastKnowLat=convertDouble(lastKnowLat);
                    strlastKnowLong=convertDouble(lastKnowLong);
                    String url=FieldSenseUtils.getPropertyValue("PROFILEPIC_GET_PATH");
                    String pattern = "E MMMM dd yyyy HH:mm:ss";
                    SimpleDateFormat format = new SimpleDateFormat(pattern);
                    String profilePicUrl=url+accountId+"_"+userId1+"_140X140.png?"+format.format(new Date());
                    mapOfUserInfo.put("profilePicURL",profilePicUrl);
                    mapOfUserInfo.put("lastKnownLocationTime",rs.getTimestamp("last_known_location_time"));
                     String lastKnownLocation=rs.getString("last_known_location");
                        lastKnownLocation=lastKnownLocation.replace("\n"," ").trim();
                    mapOfUserInfo.put("last_known_location",lastKnownLocation);
                     String punchIn = rs.getString("punchIn");
                    String punchOut = rs.getString("punchOut");
                    String punchDate=rs.getString("punchDate");
                    mapOfPunchInInfo.put("punchDate",punchDate);
                    mapOfPunchInInfo.put("punchIn",rs.getString("punchIn"));
                    mapOfPunchInInfo.put("punchOut",rs.getString("punchOut"));
                    mapOfPunchInInfo.put("punchOutLocation",rs.getString("punchOutLocation"));
                    mapOfPunchInInfo.put("punchInLocation",rs.getString("punchInLocation"));

                    
                    if(userRole==1){
                    mapOfUserInfo.put("userRole","admin");
                    }else if(userRole==5){
                     mapOfUserInfo.put("userRole","OnField");
                    }else{
                    mapOfUserInfo.put("userRole","invalid");
                    }
                    
                    mapOfUserInfo.put("officeLatitude", officeLat);
                    mapOfUserInfo.put("officeLongitude", officelong);
                   
                   // mapOfUserInfo.put("homeLatitude", rs.getDouble("homelatitude"));
                   // mapOfUserInfo.put("homeLongitude", rs.getDouble("homelongitude"));
                    
                  //   System.out.println("start date "+strStartDate +" End date"+endDate+"userId"+userId1+"beforeDate"+weekBeforeDate+"Monday"+dateOfMonday);
                    //End date query to take app of current date.
                    query1=("select IFNULL(app.appointment_title, '') as appointmentTitle,IFNULL(cu.customer_name, '') as customerName,IFNULL(cu.customer_location_identifier,'') as customerLocationIdentifier  from appointments app join customers cu on app.customer_id_fk=cu.id where app.appointment_time BETWEEN ? AND ? AND (app.status=0 OR app.status=1) AND app.assigned_id_fk=? AND app.record_state!=3 order by app.appointment_time asc limit 1");
                     param=new Object[]{strStartDate,endDate,userId1}; 
                     try{
                    mapOfAppointmentInfo=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForMap(query1,param);
                     }catch(Exception e){
                     //mapOfAppointmentInfo=new HashMap<String,Object>();
                      mapOfAppointmentInfo.put("appointmentTitle", "");
                      mapOfAppointmentInfo.put("customerName", "");
                      mapOfAppointmentInfo.put("customerLocationIdentifier", "");
//                     e.printStackTrace();
                      log4jLog.info("selectUserPositionsMobile >> "+e);      
                     }
                     try{
            query1=("select IFNULL(count(app.id),0) yetToVisit,IFNULL((select count(id) from appointments where appointment_time BETWEEN ? AND ? AND assigned_id_fk=? AND record_state!=3),0) totalVisit from appointments app join customers cu on app.customer_id_fk=cu.id where app.appointment_time BETWEEN ? AND ? AND (app.status=0 OR app.status=3)  AND app.assigned_id_fk=? AND app.record_state!=3");
            param=new Object[]{strStartDate,endDate,userId1,strStartDate,endDate,userId1}; 
           mapOfAppointmentCount=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForMap(query1, param);
            mapOfAppointmentInfo.putAll(mapOfAppointmentCount);
            }catch(Exception e){
                mapOfAppointmentInfo.put("yetToVisit", 0);
                mapOfAppointmentInfo.put("totalVisit", 0);
//            e.printStackTrace();
                log4jLog.info("selectUserPositionsMobile >> "+e);
            }
                      
                     query1=("select tc.category_name from user_territory as ut join territory_categories as tc on ut.teritory_id=tc.id where ut.user_id_fk=? ");
                      param=new Object[]{userId1};
                      try{
                     listOfTerriorty=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForList(query1,String.class,param);
                     }catch(Exception e){
                     listOfTerriorty=new ArrayList<String>();
//                     e.printStackTrace();
                     log4jLog.info("selectUserPositionsMobile >> "+e);      
                     }
                      mapOfUserInfo.put("Terriorty", listOfTerriorty);
//                    Location homeLocation = new Location();
//                    homeLocation.setLatitude(rs.getDouble("homelatitude"));
//                    homeLocation.setLangitude(rs.getDouble("homelongitude"));
//                    user.setHomeLocation(homeLocation);
//              
//                    Location officeLocation = new Location();
//                    officeLocation.setLatitude(rs.getDouble("officelatitude"));
//                    officeLocation.setLangitude(rs.getDouble("officelongitude"));
//                    user.setOfficeLocation(officeLocation);
//
//                    teamMember.setUser(user);
//
//                    User createdBy = new User();
//                    createdBy.setId(rs.getInt("created_by_id_fk"));
//                    teamMember.setCreatedBy(createdBy);
//
//                    String punchIn = rs.getString("punchIn");
//                    String punchOut = rs.getString("punchOut");
//                    if (punchIn.equals("0")) {
//                        teamMember.setIsOnline(false);
//                    } else if (punchOut.equals("00:00:00")) {
//                        teamMember.setIsOnline(true);
//                    } else {
//                        teamMember.setIsOnline(false);
//                    }
//                    teamMember.setCreatedOn(rs.getTimestamp("created_on"));
                       if (punchIn.equals("0")) {
                        mapOfUserInfo.put("isOnline",false);
                    } else if (punchOut.equals("00:00:00")) {
                        mapOfPunchInInfo.put("isOnline",true);
                        //mapOfUserInfo.put("status","PunchIn");
                    } else {
                        mapOfPunchInInfo.put("isOnline",false);
                    }
                       if(punchDate.equals(todaysDate)){
                        todaysPunch=true;
                        }else{
                        todaysPunch=false;
                        }
                        int inMeeting = rs.getInt("inMeeting");
                        if(inMeeting == 1){
                       mapOfUserInfo.put("status","In Meeting");
                       }else if (punchOut.equals("00:00:00") && !punchIn.equals("0")) {
                           if(strLastKnowLat.equals(strOfficeLat) && strlastKnowLong.equals(strOfficelong)){
                       mapOfUserInfo.put("status","In Office");
                       }else{
                        mapOfUserInfo.put("status","On Field");
                           }
                       }else if((punchOut.equals("00:00:00") && punchIn.equals("00:00:00")) ||(!punchOut.equals("00:00:00") && !punchIn.equals("00:00:00") && !todaysPunch)){
                       mapOfUserInfo.put("status","Not Punched-in");
                       }else if(!(punchOut.equals("00:00:00")) && !(punchIn.equals("00:00:00")) && todaysPunch){
                       mapOfUserInfo.put("status","Punched-out");
                       }else{
                       mapOfUserInfo.put("status","unknown");
                       }
                       
                       
                    if (date.equals(modifiedDate)) {
                        int canTakeCall = rs.getInt("canTakeCalls");
                        if (canTakeCall == 1) {
                           // teamMember.setCanTakeCalls(true);
                            mapOfUserInfo.put("canTakeCalls",true);
                        } else {
                           // teamMember.setCanTakeCalls(false);
                            mapOfUserInfo.put("canTakeCalls",false);
                        }
                        if (inMeeting == 1) {
                          //  teamMember.setInMeeting(true);
                            mapOfUserInfo.put("inMeeting",true);
                        } else {
                          //  teamMember.setInMeeting(false);
                            mapOfUserInfo.put("inMeeting",false);
                        }
                    } else {
                       mapOfUserInfo.put("canTakeCalls",false);
                       mapOfUserInfo.put("inMeeting",false);

                  }
//                    int hasSubOrdinates = rs.getInt("has_subordinates");
//                    if (hasSubOrdinates == 1) {
//                       //teamMember.setHasSubordinate(true);
//                        mapOfUserInfo.put("hasSubordinates",true);
//                  } else {  
//                        //teamMember.setHasSubordinate(false);
//                        mapOfUserInfo.put("hasSubordinates",false);
//                    }
                    //listOfTeams.add(teamMember);homelatitude
                     
                     mapOfUser.put("user_info",mapOfUserInfo);
                     mapOfAppointment.put("appointment_info", mapOfAppointmentInfo);
                     mapOfPunchDetails.put("punch_in_info", mapOfPunchInInfo);
                    // mapOfExpense.put("expense_info", mapOfExpenseInfo);
                     //mapOfGraph.put("Graph Info",ListOfGraphInfo);
                     listOfData.add(mapOfUser);
                     listOfData.add(mapOfAppointment);
                     listOfData.add(mapOfPunchDetails);
                    //listOfData.add(mapOfExpense);
                    // listOfData.add(mapOfGraph);
                    ///mapWithIdList.put(Integer.toString(userId1), listOfData);
                     listOfTeamData.add(listOfData);
                }
            }
            
            
//                    ResultSet rs1 = stmt.executeQuery(query3.toString());
//                     while (rs1.next()) {
//                     
//                     }
            //mapOfAppointmentInfo=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForMap(query,param);
            //param=new Object[]{starTime,endTime,userId1};
            
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info("selectUserPositionsMobile >> "+e);
            
        } finally {
            try {
                connection.close();
                stmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                log4jLog.info("selectUserPositionsMobile >> "+ex);                
            }
        }
        return listOfTeamData;
        }else{
                
              Connection connection = null;
        Statement stmt = null;  
        Object[] param = new Object[]{userId};
        List<Object> listOfTeamData=new ArrayList<Object>();
          try {
              connection = FieldSenseUtils.getDataSourceForAccount(accountId).getConnection();
            stmt = connection.createStatement();
            StringBuilder query2 = new StringBuilder();
            String query1 = "";
            query2.append("SELECT DISTINCT(t.id), t.team_name, t.description, t.user_id_fk, t.is_active, t.created_on, ");
            query2.append("t.created_by_id_fk,t.team_position_csv ,u.first_name,u.last_name,u.designation,u.last_known_latitude,IFNULL(u.role,0) role,IFNULL(u.mobile_number,0) mobileNumber,");
            query2.append("u.last_known_langitude,u.last_known_location_time,u.last_known_location,IFNULL(a.punch_in,0) punchIn,IFNULL(a.punch_date,0) punchDate,IFNULL(a.punch_in_location,'') punchInLocation,IFNULL(a.punch_out_location,'') punchOutLocation,");
            query2.append("IFNULL(a.punch_out,0) punchOut,IFNULL(uk.key_value,0) canTakeCalls,IFNULL(ukm.key_value,0) inMeeting,");
            query2.append("IFNULL(u.office_latitude,0) officelatitude,IFNULL(u.office_langitude,0) officelongitude,");
            query2.append("IFNULL(DATE(uk.modified_on),DATE(now())) uk_modifiedDay,now() new_uk_modified_date");
            query2.append(" FROM teams t ");
            query2.append(" INNER JOIN fieldsense.users u ON u.id=t.user_id_fk");
            query2.append(" LEFT OUTER JOIN attendances a ON a.user_id_fk=t.user_id_fk AND a.id=(SELECT MAX(id) FROM  attendances att WHERE att.user_id_fk=u.id)");
            query2.append(" LEFT OUTER JOIN user_keys uk ON uk.user_id_fk=u.id AND uk.user_key='CanTakeCalls'");
            query2.append(" LEFT OUTER JOIN user_keys ukm ON ukm.user_id_fk=u.id AND ukm.user_key='InMeeting'");
            query2.append(" WHERE team_position_csv like CONCAT('%',(select team_position_csv from teams where user_id_fk="+userId+"),'%') AND u.active=1 order by u.first_name asc");
            ResultSet rs = stmt.executeQuery(query2.toString());
            List<TeamMember> listOfTeams = new ArrayList<TeamMember>();
            int userId1=0;
            int userRole;
            int teamId;
            double lastKnowLat;
            double lastKnowLong;
            double officeLat;
            double officelong;
            String strLastKnowLat;
            String strlastKnowLong;
            String strOfficeLat;
            String strOfficelong;
            boolean todaysPunch;
            while (rs.next()) {
                teamId=rs.getInt("id");
                String userPosition2 = rs.getString("team_position_csv");
                userId1=rs.getInt("user_id_fk");
                userRole=rs.getInt("role");
                    String modifiedDate = rs.getString("uk_modifiedDay");
                     Map<String,Object> mapOfUserInfo=new HashMap<String, Object>();
                     Map<String,Object> mapOfAppointmentInfo=new HashMap<String, Object>();
                     Map<String,Object> mapOfExpenseInfo=new HashMap<String, Object>();
                     Map<String,Object> mapOfPunchInInfo=new HashMap<String, Object>();
                     Map<String,Object> mapOfUser=new HashMap<String, Object>();
                     List<Integer> listOfVisitCount=new ArrayList<Integer>();
                     Map<String,Object> mapOfAppointmentCount=new HashMap<String, Object>();
                     Map<String,Object> mapOfAppointment=new HashMap<String, Object>();
                     Map<String,Object> mapOfExpense=new HashMap<String, Object>();
                      Map<String,Object> mapOfPunchDetails=new HashMap<String, Object>();
                      Map<String,Object> mapOfGraphInfo=new HashMap<String, Object>();
                      List<HashMap> ListOfGraphInfo=new ArrayList<HashMap>();
                      Map<String,Object> mapOfGraph=new HashMap<String, Object>();
                      List<String> listOfTerriorty=new ArrayList<String>();
                     List<Object> listOfData=new ArrayList<Object>();
                    mapOfUserInfo.put("userId",userId1);
                    mapOfUserInfo.put("firstName",rs.getString("first_name"));
                    mapOfUserInfo.put("subordinateCount",this.checkSubordinatePersent(teamId, accountId));
                    mapOfUserInfo.put("reportingHead", this.getParentName(teamId,accountId));
                    mapOfUserInfo.put("SubordianteInfo",this.getSubordinateName(teamId,accountId));
                    mapOfUserInfo.put("lastName",rs.getString("last_name"));
                    mapOfUserInfo.put("mobileNumber",rs.getString("mobileNumber"));
                    mapOfUserInfo.put("designation", rs.getString("designation"));
                    lastKnowLat=rs.getDouble("last_known_latitude");
                    lastKnowLong=rs.getDouble("last_known_langitude");
                    officeLat=rs.getDouble("officelatitude");
                    officelong=rs.getDouble("officelongitude");
                    mapOfUserInfo.put("lastKnownLat",lastKnowLat);
                    mapOfUserInfo.put("lastKnownLong",lastKnowLong);
                    strOfficeLat=convertDouble(officeLat);
                    strOfficelong=convertDouble(officelong);
                    strLastKnowLat=convertDouble(lastKnowLat);
                    strlastKnowLong=convertDouble(lastKnowLong);
                    String url=FieldSenseUtils.getPropertyValue("PROFILEPIC_GET_PATH");
                    String pattern = "E MMMM dd yyyy HH:mm:ss";
                    SimpleDateFormat format = new SimpleDateFormat(pattern);
                    String profilePicUrl=url+accountId+"_"+userId1+"_140X140.png?"+format.format(new Date());
                    mapOfUserInfo.put("profilePicURL",profilePicUrl);
                    mapOfUserInfo.put("lastKnownLocationTime",rs.getTimestamp("last_known_location_time"));
                     String lastKnownLocation=rs.getString("last_known_location");
                        lastKnownLocation=lastKnownLocation.replace("\n"," ").trim();
                    mapOfUserInfo.put("last_known_location",lastKnownLocation);
                     String punchIn = rs.getString("punchIn");
                    String punchOut = rs.getString("punchOut");
                    String punchDate=rs.getString("punchDate");
                    mapOfPunchInInfo.put("punchDate",punchDate);
                    mapOfPunchInInfo.put("punchIn",rs.getString("punchIn"));
                    mapOfPunchInInfo.put("punchOut",rs.getString("punchOut"));
                    mapOfPunchInInfo.put("punchOutLocation",rs.getString("punchOutLocation"));
                    mapOfPunchInInfo.put("punchInLocation",rs.getString("punchInLocation"));

                    
                    if(userRole==1){
                    mapOfUserInfo.put("userRole","admin");
                    }else if(userRole==5){
                     mapOfUserInfo.put("userRole","OnField");
                    }else{
                    mapOfUserInfo.put("userRole","invalid");
                    }
                    
                    mapOfUserInfo.put("officeLatitude", officeLat);
                    mapOfUserInfo.put("officeLongitude", officelong);
                   // mapOfUserInfo.put("homeLatitude", rs.getDouble("homelatitude"));
                   // mapOfUserInfo.put("homeLongitude", rs.getDouble("homelongitude"));
                    
                  //   System.out.println("start date "+strStartDate +" End date"+endDate+"userId"+userId1+"beforeDate"+weekBeforeDate+"Monday"+dateOfMonday);
                    //End date query to take app of current date.
                    query1=("select IFNULL(app.appointment_title, '') as appointmentTitle,IFNULL(cu.customer_name, '') as customerName,IFNULL(cu.customer_location_identifier,'') as customerLocationIdentifier  from appointments app join customers cu on app.customer_id_fk=cu.id where app.appointment_time BETWEEN ? AND ? AND (app.status=0 OR app.status=1) AND app.assigned_id_fk=? AND app.record_state!=3 order by app.appointment_time asc limit 1");
                     param=new Object[]{strStartDate,endDate,userId1}; 
                     try{
                    mapOfAppointmentInfo=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForMap(query1,param);
                     }catch(Exception e){
                     //mapOfAppointmentInfo=new HashMap<String,Object>();
                      mapOfAppointmentInfo.put("appointmentTitle", "");
                      mapOfAppointmentInfo.put("customerName", "");
                      mapOfAppointmentInfo.put("customerLocationIdentifier", "");
//                     e.printStackTrace();
                      log4jLog.info("selectUserPositionsMobile >> "+e);      
                     }
                     try{
            query1=("select IFNULL(count(app.id),0) yetToVisit,IFNULL((select count(id) from appointments where appointment_time BETWEEN ? AND ? AND assigned_id_fk=? AND record_state!=3),0) totalVisit from appointments app join customers cu on app.customer_id_fk=cu.id where app.appointment_time BETWEEN ? AND ? AND (app.status=0 OR app.status=3)  AND app.assigned_id_fk=? AND app.record_state!=3");
            param=new Object[]{strStartDate,endDate,userId1,strStartDate,endDate,userId1}; 
           mapOfAppointmentCount=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForMap(query1, param);
            mapOfAppointmentInfo.putAll(mapOfAppointmentCount);
            }catch(Exception e){
                mapOfAppointmentInfo.put("yetToVisit", 0);
                mapOfAppointmentInfo.put("totalVisit", 0);
            e.printStackTrace();
                log4jLog.info("selectUserPositionsMobile >> "+e);
            }
                      
                     query1=("select tc.category_name from user_territory as ut join territory_categories as tc on ut.teritory_id=tc.id where ut.user_id_fk=? ");
                      param=new Object[]{userId1};
                      try{
                     listOfTerriorty=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForList(query1,String.class,param);
                     }catch(Exception e){
                     listOfTerriorty=new ArrayList<String>();
                     e.printStackTrace();
                     log4jLog.info("selectUserPositionsMobile >> "+e);      
                     }
                      mapOfUserInfo.put("Terriorty", listOfTerriorty);
//                    Location homeLocation = new Location();
//                    homeLocation.setLatitude(rs.getDouble("homelatitude"));
//                    homeLocation.setLangitude(rs.getDouble("homelongitude"));
//                    user.setHomeLocation(homeLocation);
//              
//                    Location officeLocation = new Location();
//                    officeLocation.setLatitude(rs.getDouble("officelatitude"));
//                    officeLocation.setLangitude(rs.getDouble("officelongitude"));
//                    user.setOfficeLocation(officeLocation);
//
//                    teamMember.setUser(user);
//
//                    User createdBy = new User();
//                    createdBy.setId(rs.getInt("created_by_id_fk"));
//                    teamMember.setCreatedBy(createdBy);
//
//                    String punchIn = rs.getString("punchIn");
//                    String punchOut = rs.getString("punchOut");
//                    if (punchIn.equals("0")) {
//                        teamMember.setIsOnline(false);
//                    } else if (punchOut.equals("00:00:00")) {
//                        teamMember.setIsOnline(true);
//                    } else {
//                        teamMember.setIsOnline(false);
//                    }
//                    teamMember.setCreatedOn(rs.getTimestamp("created_on"));
                       if (punchIn.equals("0")) {
                        mapOfUserInfo.put("isOnline",false);
                    } else if (punchOut.equals("00:00:00")) {
                        mapOfPunchInInfo.put("isOnline",true);
                        //mapOfUserInfo.put("status","PunchIn");
                    } else {
                        mapOfPunchInInfo.put("isOnline",false);
                    }
                       if(punchDate.equals(todaysDate)){
                        todaysPunch=true;
                        }else{
                        todaysPunch=false;
                        }
                        int inMeeting = rs.getInt("inMeeting");
                        if(inMeeting == 1){
                       mapOfUserInfo.put("status","In Meeting");
                       }else if (punchOut.equals("00:00:00") && !punchIn.equals("0")) {
                           if(strLastKnowLat.equals(strOfficeLat) && strlastKnowLong.equals(strOfficelong)){
                       mapOfUserInfo.put("status","In Office");
                       }else{
                        mapOfUserInfo.put("status","On Field");
                           }
                       }else if((punchOut.equals("00:00:00") && punchIn.equals("00:00:00")) ||(!punchOut.equals("00:00:00") && !punchIn.equals("00:00:00") && !todaysPunch)){
                       mapOfUserInfo.put("status","Not Punched-in");
                       }else if(!(punchOut.equals("00:00:00")) && !(punchIn.equals("00:00:00")) && todaysPunch){
                       mapOfUserInfo.put("status","Punched-out");
                       }else{
                       mapOfUserInfo.put("status","unknown");
                       }
                       
                       
                    if (date.equals(modifiedDate)) {
                        int canTakeCall = rs.getInt("canTakeCalls");
                        if (canTakeCall == 1) {
                           // teamMember.setCanTakeCalls(true);
                            mapOfUserInfo.put("canTakeCalls",true);
                        } else {
                           // teamMember.setCanTakeCalls(false);
                            mapOfUserInfo.put("canTakeCalls",false);
                        }
                        if (inMeeting == 1) {
                          //  teamMember.setInMeeting(true);
                            mapOfUserInfo.put("inMeeting",true);
                        } else {
                          //  teamMember.setInMeeting(false);
                            mapOfUserInfo.put("inMeeting",false);
                        }
                    } else {
                       mapOfUserInfo.put("canTakeCalls",false);
                       mapOfUserInfo.put("inMeeting",false);

                  }
//                    int hasSubOrdinates = rs.getInt("has_subordinates");
//                    if (hasSubOrdinates == 1) {
//                       //teamMember.setHasSubordinate(true);
//                        mapOfUserInfo.put("hasSubordinates",true);
//                  } else {  
//                        //teamMember.setHasSubordinate(false);
//                        mapOfUserInfo.put("hasSubordinates",false);
//                    }
                    //listOfTeams.add(teamMember);homelatitude
                     
                     mapOfUser.put("user_info",mapOfUserInfo);
                     mapOfAppointment.put("appointment_info", mapOfAppointmentInfo);
                     mapOfPunchDetails.put("punch_in_info", mapOfPunchInInfo);
                    // mapOfExpense.put("expense_info", mapOfExpenseInfo);
                     //mapOfGraph.put("Graph Info",ListOfGraphInfo);
                     listOfData.add(mapOfUser);
                     listOfData.add(mapOfAppointment);
                     listOfData.add(mapOfPunchDetails);
                    //listOfData.add(mapOfExpense);
                    // listOfData.add(mapOfGraph);
                    ///mapWithIdList.put(Integer.toString(userId1), listOfData);
                     listOfTeamData.add(listOfData);
            }
            
            
//                    ResultSet rs1 = stmt.executeQuery(query3.toString());
//                     while (rs1.next()) {
//                     
//                     }
            //mapOfAppointmentInfo=FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForMap(query,param);
            //param=new Object[]{starTime,endTime,userId1};
            
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info("selectUserPositionsMobile >> "+e);
            
        } finally {
            try {
                connection.close();
                stmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                log4jLog.info("selectUserPositionsMobile >> "+ex);                
            }
        }
        return listOfTeamData;
        }
        
    }

     public Map getParentName(int teamId, int accountId) {
        try{
        String query="select u.id,u.full_name from teams t join fieldsense.users u on t.user_id_fk=u.id where t.id=(select SUBSTRING(team_position_csv, 8,6) AS ExtractString from teams where id=?)";
        Object[] param = new Object[]{teamId};
       return FieldSenseUtils.getJdbcTemplateForAccount(accountId).queryForMap(query, param);
        }catch(Exception e){
        e.printStackTrace();
        return new HashMap<String, Object>();
        }
    } 
     
     
     public List getSubordinateName(int teamId ,int accountId){
try{
        String query="select u.id as id,u.full_name as fullName from teams t join fieldsense.users u on t.user_id_fk=u.id where SUBSTRING(team_position_csv, 8,6) like ? and u.active!=0 order by u.full_name";
        Object[] param = new Object[]{teamId};
        return FieldSenseUtils.getJdbcTemplateForAccount(accountId).query(query, param, new RowMapper<Map>() {
                @Override
                public Map mapRow(ResultSet rs, int i) throws SQLException {
                    Map<String,Object> mapOfUserData =new HashMap<String,Object>();
                    mapOfUserData.put("id", rs.getInt("id"));
                    mapOfUserData.put("fullName", rs.getString("fullName"));
                   return mapOfUserData;
                }
            });
        }catch(Exception e){
        e.printStackTrace();
        return new ArrayList();
        }
}
        

}


