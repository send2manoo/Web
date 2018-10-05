/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.team.model;

import static com.qlc.fieldsense.appointments.model.AppointmentManager.log4jLog;
import com.qlc.fieldsense.team.dao.TeamDao;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author Ramesh
 * @date 24-02-2014
 */
public class TeamManager {

    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
    TeamDao teamDao = (TeamDao) GetApplicationContext.ac.getBean("teamDaoImpl");

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("TeamManager");

    /**
     * 
     * @param team
     * @param userToken
     * @return 
     * @purpose Used to create team
     * @deprecated 
     */
    public Object createTeam(Team team, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                int teamId = teamDao.insertTeam(team, accountId);
                if (teamId != 0) {
                    team = teamDao.selectTeam(teamId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Team created succeessfully . ", " Team ", team);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Team creation failed  . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param teamId
     * @param userToken
     * @return 
     * @purpose Used to retrieve team details based on teamId 
     * @deprecated 
     */
    public Object selectTeam(int teamId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (teamDao.isTeamValid(teamId, accountId)) {
                    Team team = new Team();
                    team = teamDao.selectTeam(teamId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " Team ", team);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid teamId . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}      
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param userToken
     * @return list of all teams
     */
    public Object selectTeams(String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<Team> teamsList = new ArrayList<Team>();
                teamsList = teamDao.selectTeam(accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " Teams List ", teamsList);
        // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param status
     * @param userToken
     * @return 
     * @purpose Used to retrieve the team lists depending on status of team i.e. team is active or not. 
     * @deprecated 
     */
    public Object selectTeamsByStatus(int status, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<Team> teamsList = new ArrayList<Team>();
                teamsList = teamDao.selectTeamByStatus(status, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " Teams List ", teamsList);
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param userId
     * @param userToken
     * @return 
     * @purpose Used to retrieve the users team lists based on userId
     * @deprecated 
     */
    public Object selectUserTeams(int userId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (fieldSenseUtils.isUserValid(userId)) {
                    List<Team> teamsList = new ArrayList<Team>();
                    teamsList = teamDao.selectTeams(userId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " Teams List ", teamsList);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid user id . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param team
     * @param userToken
     * @return 
     * @purpose Used to update team and team member details
     * @deprecated 
     */
    public Object updateTeam(Team team, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<TeamMember> teamMembersList = new ArrayList<TeamMember>();
                teamMembersList = team.getTeamMembers();
                if (!teamMembersList.isEmpty()) {
                    String oldTeamName = teamDao.teamName(team.getId(), accountId);
                    if (!(oldTeamName.equalsIgnoreCase(team.getTeamName()))) {
                        if (teamDao.isTeamNameUnique(team.getOwnerId().getId(), team.getTeamName(), accountId)) {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Team with this name already exists. Please try again . ", "", "");
                        }
                    }
                    for (TeamMember teamMember : teamMembersList) {
                        int no = teamMember.getType();
                        if (no == 0) {
                            teamDao.deleteTeamMember(teamMember.getTeamId(), teamMember.getUser().getId(), accountId);
                        }
                    }
                    for (TeamMember teamMember : teamMembersList) {
                        int no = teamMember.getType();
                        if (no == 1) {
                            teamDao.insertTeamMember(teamMember, accountId);
                        }
                    }
                    for (TeamMember teamMember : teamMembersList) {
                        int no = teamMember.getType();
                        if (no == 2) {
                            teamDao.updateTeamMember(teamMember, accountId);
                        }
                    }
                    if (teamDao.updateTeam(team, accountId)) {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Team updated successfully . ", " ", "");
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Team updation failed . Please try again . ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Team should contain atleast 1 member. ", "", "");
                }
             // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}   
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param teamId
     * @param userToken
     * @return 
     * @purpose Used to delete team.
     * @deprecated 
     */
    public Object deleteTeam(int teamId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (teamDao.isTeamValid(teamId, accountId)) {
                    if (teamDao.deleteTeam(teamId, accountId)) {
                        teamDao.deleteTeamFromTeamMembers(teamId, accountId);
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Team deleted successfully . ", " ", "");
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Team deletion failed  . Please try again . ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid team id  . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param teamMember
     * @param userToken
     * @return 
     * @purpose Used to create team and team members.
     * @deprecated 
     */
    public Object addTeamMember(TeamMember teamMember, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (!teamDao.isTeamMemeber(teamMember.getTeamId(), teamMember.getUser().getId(), accountId)) {
                    if (!teamDao.isTeamMemeberIsInAnyTeam(teamMember.getUser().getId(), accountId)) {
                        int memberId = teamDao.insertTeamMember(teamMember, accountId);
                        if (memberId != 0) {
                            teamMember = teamDao.selectTeamMember(memberId, accountId);
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Team member created successfully .", "  ", teamMember);
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Team member creation failed. Please try again . ", "", "");
                        }
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " User is already in other team .Please try with diff user or remove from that team .", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " User is already in team .Please try with diff user .", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

     /**
     * 
     * @param teamId
     * @param userToken
     * @return 
     * @purpose Used to get list of team members of team based on teamId 
     * @deprecated 
     */
    public Object selectTeamMembers(int teamId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (teamDao.isTeamValid(teamId, accountId)) {
                    List<TeamMember> teammemberList = new ArrayList();
                    teammemberList = teamDao.selectTeamMembers(teamId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " Team member list ", teammemberList);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid teamId . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

     /**
     * 
     * @param teamId
     * @param userToken
     * @return 
     * @purpose Used to get list of team members except of TL based on teamId 
     * @deprecated 
     */
    public Object selectTeamMembersWithOutTL(int teamId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (teamDao.isTeamValid(teamId, accountId)) {
                    List<TeamMember> teammemberList = new ArrayList();
                    teammemberList = teamDao.selectTeamMembersWithOutTL(teamId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " Team member list ", teammemberList);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid teamId . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /*@structure changed and not in use anymore*/

    /**
     *
     * @param teamMember
     * @param userToken
     * @return
     */
    
    public Object updateTeamMember(TeamMember teamMember, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (teamDao.isTeamValid(teamMember.getTeamId(), accountId)) {
                    if (teamDao.updateTeamMember(teamMember, accountId)) {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Team member updated successfully . ", " ", "");
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Team member updation failed . Please try again . ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid teamId . Please try again . ", "", "");
                }
          // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}      
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /*@structure changed and not in use anymore*/

    /**
     *
     * @param memberId
     * @param userToken
     * @return
     */
    
    public Object deleteTeamMember(int memberId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (teamDao.isTeamValid(memberId, accountId)) {
                    if (teamDao.deleteTeamMember(memberId, accountId)) {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Team member deleted successfully . ", " ", "");
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Team member deletion failed . Please try again . ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid teamId . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param userToken
     * @author :anuja
     * @return 
     * @purpose :get team member location
     * @deprecated 
     */
    public Object selectTeamMemberLocation(String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int userId = fieldSenseUtils.userIdForToken(userToken);
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
    //            if (fieldSenseUtils.isUserTeamLead(userId)) {
                List<Team> teamList = teamDao.selectTeamsList(userId, accountId);
                if (teamList == null || teamList.isEmpty()) {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "No any related data", "", "");
                } else {
                    for (int i = 0; i < teamList.size(); i++) {
                        teamList.get(i).setTeamMembers(teamDao.selectTeamMembersLocation(teamList.get(i).getId(), accountId));
                    }
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", "teamList", teamList);
                }
    //            } else {
    //                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " You are not authorised.", "", "");
    //            }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token. Please try again. ", "", "");
        }
    }

    /**
     * 
     * @param team
     * @param userToken
     * @author : anuja
     * @return 
     * @purpose: to add team and team members
     * @deprecated 
     */
    public Object createTeamTeamMembers(Team team, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (!team.getTeamMembers().isEmpty()) {
                    if (!(teamDao.isTeamNameUnique(team.getOwnerId().getId(), team.getTeamName(), accountId))) {
                        int teamId = teamDao.insertTeam(team, accountId);
                        int count = team.getTeamMembers().size();
                        int len = 0;
                        if (teamId != 0) {
                            for (int i = 0; i < team.getTeamMembers().size(); i++) {
                                if (!fieldSenseUtils.isMemberInTeam(teamId, team.getTeamMembers().get(i).getId(), accountId)) {
                                    int memberId = teamDao.insertTeamMember(team.getTeamMembers().get(i), teamId, accountId);
                                    if (memberId != 0) {
                                        len++;
                                    }
                                } else {
                                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Team created successfully,team member is repeated. Please try again . ", "", "");
                                }
                            }
                            if (count == len) {
                                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Team and team members created succeessfully . ", "", "");
                            }
                        } else {
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Team and team member creation failed  . Please try again . ", "", "");
                        }
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Team with this name already exists. Please try again . ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "  Team should contain atleast 1 member. ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //} 
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Team and team members created succeessfully . ", "", "");
    }

    /**
     * 
     * @param userId
     * @param userToken
     * @author: anuja
     * @return 
     * @purpose : list of team members based on team lead
     * @deprecated 
     */
    public Object selectTeamMembersForTeamLead(int userId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
//              int userId = fieldSenseUtils.userIdForToken(userToken);
                List<TeamMember> teamMember = teamDao.selectTeamMembersForTeamLead(userId, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "teamMember", teamMember);
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

     /**
     * 
     * @param userToken
     *  @author: anuja
     * @return 
     * @purpose : list of team leads
     */
    public Object selectTeamLead(String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<TeamMember> teamMember = teamDao.selectTeamLead(accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "teamMember", teamMember);
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param userToken
     * @param userId
     * @return 
     * @purpose This object is used to retrieve user and his immediate subordinates
     */
    public Object selectUserPositions(int userId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<TeamMember> teamsList = teamDao.selectUserPositions(userId, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "selected user positions", teamsList);
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * Added by jyoti, 14-july-2017
     * @param userToken
     * @param userId
     * @return 
     * @purpose This object is used to retrieve user and his subordinates with their subordinates
     */
    public Object selectUserPositionsSubordinatesOfSubordinate(int userId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<User> teamsList = teamDao.selectUserPositionsSubordinatesOfSubordinate(userId, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "selectUserPositionsSubordinatesOfSubordinate", teamsList);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
     /**
     * 
     * @param userToken
     * @param userId
     * @param date
     * @return 
     * @purpose This object is used to retrieve user and his immediate subordinates
     */
    public Object selectUserPositions(int userId, String date, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);                
                List<TeamMember> teamsList = teamDao.selectUserPositions(userId, date, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "selected user positions", teamsList);
        // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
     /**
     * 
     * @param userToken
     * @param userId
     * @param date
     * @purpose This object is used to retrieve user and his immediate subordinates
     */
    public Object selectUserPositionsWithSubs(int userId, String date, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);                
                List<TeamMember> teamsList = teamDao.selectUserPositionsWithSubs(userId, date, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "selected user positions", teamsList);
        // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    //Siddhesh: My team mobile.
    /**
     * 
     * @param userToken
     * @param userId
     * @param date
     * @purpose This object is used to retrieve user and his immediate subordinates for My team mobile.
     */
    public Object selectUserPositionsMobile(int userId, String date, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<Object> teamsList = teamDao.selectUserPositionsMobile(userId, date, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "selected user positions", teamsList);
        // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
     //Siddhesh: My team mobile.
    /**
     * 
     * @param userToken
     * @param userId
     * @param date
     * @purpose This object is used to retrieve user and his immediate subordinates for My team mobile.
     */
    public Object selectUserPositionsMobileForAllSubordinates(int userId, String date, String userToken,int subordinates) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<Object> teamsList = teamDao.selectUserPositionsMobileForAllSubordinate(userId, date, accountId,subordinates);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "selected user positions", teamsList);
        // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
    //Siddhesh: My team mobile.
    /**
     * 
     * @param userToken
     * @param userId
     * @param date
     * @purpose This object is used to retrieve specific user data
      */
      
    public Object selectUserSpecificDataMobile(int userId,String date,String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<Object> teamsList= teamDao.selectUserDetailedPositionsMobile(userId, date, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "selected user positions", teamsList);
        // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
    
    

    // Added by jyoti, 25-02-2017

    /**
     *
     * @param userId
     * @param date
     * @param fromDate
     * @param userToken
     * @return
     */
        public Object selectUserPositions(int userId, String date, String fromDate, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                // Added By Jyoti, 25-02-2017
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateUtil = new Date();
                String toDate = "";
                try {
                    dateUtil = parser.parse(fromDate);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateUtil);
                    calendar.add(Calendar.HOUR_OF_DAY, 24);
                    dateUtil = calendar.getTime();
                    toDate = parser.format(dateUtil);
                } catch (ParseException ex) {
                    log4jLog.info(" selectUserPositions- PArse-DateToServer " + ex);
                }
                //Ended By Jyoti, 25-02-2017
                List<TeamMember> teamsList = teamDao.selectUserPositions(userId, date, fromDate, toDate, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "selected user positions", teamsList);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
    // Added by jyoti, 28-02-2017

    /**
     *
     * @param userId
     * @param date
     * @param userToken
     * @return
     */
        public Object selectUserPositionsAllSubordinate(int userId, String date, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<TeamMember> teamsList = teamDao.selectUserPositionsAllSubordinate(userId, date, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "selected user positions", teamsList);
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
     /**
     * 
     * @param userToken
     * @param userId
     * @return 
     * @purpose Used to retrieve users position order by name 
     */
    public Object selectUserPositionsOrderByName(int userId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<TeamMember> teamsList = teamDao.selectUserPositionsOrderByName(userId, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "selected user positions", teamsList);
        // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param userToken
     * @return 
     * @purpose Used to retrieve organization chart details 
     */
    public Object selectOrganisationChart(String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<OraganizationChart> OraganizationChartList = teamDao.selectOraganizationChart(accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "selected user positions", OraganizationChartList);
        // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param userToken
     * @return 
     * @purpose Used to retrieve members in organization chart 
     */
    public Object selectOrganizationMembersForChart(String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<User> userList = teamDao.selectOrganizationMembersForChart(accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "selected user positions", userList);
          // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param team
     * @param parentId
     * @param userToken
     * @return 
     * @purpose Used to add users to organization chart
     */
    public Object addMemberToOrganizationChart(Team team, int parentId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                int teamId = teamDao.insertTeam(team, accountId);                    // max id
                if(teamId==0){
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, "User is already present in the oraganization chart  ", "", "");
                }
                OraganizationChart oraganizationChart = new OraganizationChart();   
                int parent_user_id=teamDao.getParentUserId(parentId,accountId);      // parent user id
                if(teamDao.updateReportingHead(parent_user_id,team.getOwnerId().getId(),accountId)){
                    if (teamId != 0) {
                        User user = new User();
                        user.setId(team.getOwnerId().getId());

                        User createdBy = new User();
                        createdBy.setId(team.getCreatedBy().getId());

                        TeamMember teamLeader = new TeamMember();
                        teamLeader.setTeamId(teamId);
                        teamLeader.setUser(user);
                        teamLeader.setMemberType(2);
                        teamLeader.setCreatedBy(createdBy);
                        teamLeader.setStatus(1);
                        teamDao.insertTeamMember(teamLeader, accountId);

                        TeamMember teamMember = new TeamMember();
                        teamMember.setTeamId(parentId);
                        teamMember.setUser(user);
                        teamMember.setMemberType(3);
                        teamMember.setCreatedBy(createdBy);
                        teamMember.setStatus(1);
                        teamDao.insertTeamMember(teamMember, accountId);
                        String parentPositionCsv = teamDao.selectUserPositionCsv(parentId, accountId);
                        parentPositionCsv = teamId + "," + parentPositionCsv;
                        if (teamDao.updateUserPostionCsv(parentPositionCsv, teamId, accountId)) {
                            teamDao.updateHasSubordiantes(parentId, 1, accountId);
                            oraganizationChart = teamDao.selectOraganizationChartMember(teamId, accountId);
                        }
                       if(fieldSenseUtils.isUserTeamLead(user.getId(),accountId))
                       {
                           List<Integer> teamMembers =fieldSenseUtils.getMembersOFTL(parentId,accountId);
                           for (int i = 0; i < teamMembers.size(); i++) {
                               int userId=teamMembers.get(i).intValue();
                               int userTeamId=teamDao.getTeamId(userId, accountId);
                               if(userTeamId>0)
                               {
                              String userparentPositionCsv = userTeamId + "," + parentPositionCsv;
                              teamDao.updateUserPostionCsv(userparentPositionCsv, userTeamId, accountId);
                               }
                           }

                       }
                       return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "user added into chart .", oraganizationChart);
                    }else{
                       return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " user is not added into chart . ", "", "");
                    }
                }else{
                       return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " user is not added into chart . ", "", "");
                }    
                
       // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
    /**
     *
     * @param team
     * @param parentId
     * @param userToken
     * @return
     */
    public Object updateMemberToOrganizationChart(Team team, int parentId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                int userId=team.getOwnerId().getId();
                int teamId = teamDao.getTeamId(userId, accountId);
                OraganizationChart oraganizationChart = new OraganizationChart();
                if (teamId != 0) {
                    User user = new User();
                    user.setId(team.getOwnerId().getId());

                    User createdBy = new User();
                    createdBy.setId(team.getCreatedBy().getId());

                    TeamMember teamMember = new TeamMember();
                    teamMember.setTeamId(parentId);
                    teamMember.setUser(user);
                    teamMember.setMemberType(3);
                    teamMember.setCreatedBy(createdBy);
                    teamMember.setStatus(1);
                    teamDao.updateTeamMemberId(teamMember, accountId);

                    String parentPositionCsv = teamDao.selectUserPositionCsv(parentId, accountId);
     //              System.out.println("parentPositionCsv------"+parentPositionCsv);
                    parentPositionCsv = teamId + "," + parentPositionCsv;
                    if (teamDao.updateUserPostionCsv(parentPositionCsv, teamId, accountId)) {
                        teamDao.updateHasSubordiantes(parentId, 1, accountId);
                        oraganizationChart = teamDao.selectOraganizationChartMember(teamId, accountId);
                    }
                }
                else
                {
                   addMemberToOrganizationChart(team,parentId,userToken);
                }
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "user added into chart .", oraganizationChart);
        // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    /**
     * 
     * @param orgChartId
     * @param userToken
     * @return 
     * @purpose Used to get details of member in organization chart 
     */
    public Object getMemberFromOrganizationChart(int orgChartId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                User user = teamDao.selectOrganizationChartUser(orgChartId, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Organization chart user .", user);
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param oraganizationChart
     * @param userToken
     * @return 
     * @purpose Used to replace member in organization chart.
     */
    public Object editOrganizationChartMember(OraganizationChart oraganizationChart, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                int userId = oraganizationChart.getUser().getId();
                int editUserId = oraganizationChart.getUser().getType();
                int edit_parent_id=0;
                int parent_user_id=teamDao.getParentUserId(oraganizationChart.getParentId(),accountId);
                if(teamDao.updateReportingHead(parent_user_id,userId,accountId)){
                    if(teamDao.updateReportingHead(edit_parent_id,editUserId,accountId)){
                        if (teamDao.updateOrganizationChartMemberInTeams(oraganizationChart.getId(), userId, accountId)) {
                            teamDao.updateOrganizationChartMemberInTeamMembers(oraganizationChart.getId(), 2, userId, accountId, editUserId);
                            teamDao.updateOrganizationChartMemberInTeamMembers(oraganizationChart.getParentId(), 3, userId, accountId, editUserId);
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Organization chart member updated successfully .", "");
                        }else{
                            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Organization chart member not updated .please try again  ", "", "");
                        }
                    }else{
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Organization chart member not updated .please try again  ", "", "");
                    }    
                }else{
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Organization chart member not updated .please try again  ", "", "");
                }   
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param userToken
     * @param teamId
     * @param userId
     * @return 
     * @purpose Used to retrieve immediate superior and immediate subordinate of user.
     */
    public Object selectUserImmidiateSupiriorsAndSubordinates(int teamId, int userId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (teamDao.isTeamValid(teamId, accountId)) {
                    List<TeamMember> teammemberList = teamDao.selectUserImmidiateSupiriorAndSubOrdinatePositions(userId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " Team member list ", teammemberList);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid teamId . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

     /**
     * 
     * @param teamId
     * @param userId
     * @param userToken
     * @author: anuja
     * @return 
     * @purpose: get users subordinates
     */
    public Object userIsSubordinate(int teamId, int userId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                int headId = teamDao.userIsReportingHead(accountId, teamId);
                int count = 0;
                if (headId == userId) {
                    count = 1;
                } else {
                    count = teamDao.userIsSubordinate(accountId, teamId, userId);
                }
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " count ", count);
             // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}   
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

     /**
     * 
     * @param teamId
     * @param parentTeamId
     * @param email
     * @param userToken
     * @return 
     * @purpose Used to delete member from organization chart.
     */
   // modified by manohar
public Object deleteMemberFromOrganizationChart(int teamId, int parentTeamId, String email, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                
                    
                List<Team> teams = teamDao.selectUserSubordiatesPositionIds(teamId, accountId);

                   int parentUserId=teamDao.getParentID(parentTeamId,accountId);  // added by manohar
                    
                    int userId = fieldSenseUtils.getUserId(email);    
               
                      List<TeamMember> teammember = null;
                      
                   for (int i=0;i<teams.size();i++) {

                            teamDao.updateTeamCSV(teams.get(i).getParentCSV(),teams.get(i).getId(), accountId);    //  added by manohar
                            teammember=teamDao.selectTeamMemberOnly(teams.get(i).getId(), accountId);       // added by manohar
                            int nvalue=teams.get(i).getUser_id();
                      if(nvalue==userId)
                      {
                          for (TeamMember teammember1 : teammember) {
//                           System.out.println(" if sizet="+teammember1.getId()+"getUser_id===="+nvalue);   //    report id
                                teamDao.updateReportingHead(parentUserId,teammember1.getId(),accountId);   //   3574    3587
                       }
                      }
                      else
                      {
//                       System.out.println("nvalue="+nvalue);
                       for (TeamMember teammember1 : teammember) {
//                           System.out.println(" else sizet="+teammember1.getId()+"getUser_id===="+nvalue);   //    report id
                                teamDao.updateReportingHead(nvalue,teammember1.getId(),accountId);   //   3574    3587                    
                       }
                      }
                  }
                  teamDao.deleteTeam(teamId, accountId);    //100398    100399   100397     
                  teamDao.updateReportingHead(0,userId,accountId);
              //  int userId = fieldSenseUtils.getUserId(email);
//                teamDao.deleteTeamMemberFromTeam(userId, accountId);
//                int count = teamDao.selectImmidiateSubOrdinatesCount(parentTeamId, accountId);
//                if (count < 1)
//                {
//                    teamDao.updateHasSubordiantes(teamId, 0, accountId);
//                }
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " Member deleted from Organization chart . ", "");
             // }else {
                    //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
               //} 
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    /**
     * @author Ramesh
     * @param userId
     * @param userToken
     * @return 
     * @purpose To get the all uses under him
     * @date 17 Feb 2015
     *
     */
    public Object getAllSubordinatesOfUser(int userId, String userToken) {
        log4jLog.info("insideTeam Manager  getAllSubordinatesOfUser method ");
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (fieldSenseUtils.isUserActive(userId)) {
                    List<User> userSubordinates = teamDao.selectUserAllSubordinates(userId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " User Subordinates ", userSubordinates);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " User is inactive . Please try with diffrent user  . ", "", "");
                }
            // }else {
                    //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
               //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

     /**
     * @author anuja
     * @param date
     * @param userToken
     * @return 
     * @purpose To get all uses in organization
     * @date 13 March, 2015
     *
     */
    public Object selectAllOraganizationMember(String date, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                List<TeamMember> teamsList = teamDao.selectAllOraganizationMember(date, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "All users in organization", teamsList);
             // }else {
                    //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
               //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * @author Ramesh
     * @param ownerId
     * @param memberId
     * @param userToken
     * @return 
     * @purpose To check is member is in hierarchy or not .
     * @date 29 March 2015 .
     */
    public Object isMemberInHierarchy(int ownerId, int memberId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                boolean isMemberInHierarchy = teamDao.isMemberInHierarchy(ownerId, memberId, accountId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Is member in hierarchy ", isMemberInHierarchy);
            // }else {
                    //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
               //}
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
    public Object deleteMemberFromOrganizationChartForSuperAdmin(int teamId, int parentTeamId, String email, int accountId) {
          //  if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
           //     int accountId = fieldSenseUtils.accountIdForToken(userToken);
                
                    
                List<Team> teams = teamDao.selectUserSubordiatesPositionIdsForSuperAdmin(teamId, accountId);

                   int parentUserId=teamDao.getParentID(parentTeamId,accountId);  // added by manohar
                    
                    int userId = fieldSenseUtils.getUserId(email);    
               
                      List<TeamMember> teammember = null;
                      
                   for (int i=0;i<teams.size();i++) {

                            teamDao.updateTeamCSV(teams.get(i).getParentCSV(),teams.get(i).getId(), accountId);    //  added by manohar
                            teammember=teamDao.selectTeamMemberOnly(teams.get(i).getId(), accountId);       // added by manohar
                            int nvalue=teams.get(i).getUser_id();
                      if(nvalue==userId)
                      {
                          for (TeamMember teammember1 : teammember) {
//                           System.out.println(" if sizet="+teammember1.getId()+"getUser_id===="+nvalue);   //    report id
                                teamDao.updateReportingHead(parentUserId,teammember1.getId(),accountId);   //   3574    3587
                       }
                      }
                      else
                      {
//                       System.out.println("nvalue="+nvalue);
                       for (TeamMember teammember1 : teammember) {
//                           System.out.println(" else sizet="+teammember1.getId()+"getUser_id===="+nvalue);   //    report id
                                teamDao.updateReportingHead(nvalue,teammember1.getId(),accountId);   //   3574    3587                    
                       }
                      }
                  }
                  teamDao.deleteTeam(teamId, accountId);    //100398    100399   100397     
                   teamDao.updateReportingHead(0,userId,accountId);
              //  int userId = fieldSenseUtils.getUserId(email);
//                teamDao.deleteTeamMemberFromTeam(userId, accountId);
//                int count = teamDao.selectImmidiateSubOrdinatesCount(parentTeamId, accountId);
//                if (count < 1)
//                {
//                    teamDao.updateHasSubordiantes(teamId, 0, accountId);
//                }
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " Member deleted from Organization chart . ", "");
             // }else {
                    //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
               //} 
//        } else {
//            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
//        }
    
}
}
