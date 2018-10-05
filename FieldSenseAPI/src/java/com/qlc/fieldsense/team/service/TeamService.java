/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.team.service;

import com.qlc.fieldsense.team.model.OraganizationChart;
import com.qlc.fieldsense.team.model.Team;
import com.qlc.fieldsense.team.model.TeamManager;
import com.qlc.fieldsense.team.model.TeamMember;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Ramesh
 * @date 24-02-2014
 */
@Controller
@RequestMapping("/team")
public class TeamService {

    TeamManager teamManager = new TeamManager();

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("TeamService");

    /**
     * 
     * @param team
     * @param userToken
     * @return 
     * @purpose Used to create team
     * @deprecated 
     */
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object createTeam(@RequestBody Team team, @RequestHeader(value = "userToken") String userToken) {
        return teamManager.createTeam(team, userToken);
    }

    /**
     * 
     * @param teamId
     * @param userToken
     * @return 
     * @purpose Used to retrieve team details based on teamId 
     * @deprecated 
     */
    @RequestMapping(value = "/{teamId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectTeam(@PathVariable("teamId") int teamId, @RequestHeader(value = "userToken") String userToken) {
        return teamManager.selectTeam(teamId, userToken);
    }

    /**
     * 
     * @param userToken
     * @return list of all teams
     */
    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    Object selectTeams(@RequestHeader(value = "userToken") String userToken) {
        return teamManager.selectTeams(userToken);
    }

    /**
     * 
     * @param status
     * @param userToken
     * @return 
     * @purpose Used to retrieve the team lists depending on status of team i.e. team is active or not. 
     * @deprecated 
     */
    @RequestMapping(value = "/selectTeamsByStatus/{status}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectTeamsByStatus(@PathVariable("status") int status, @RequestHeader(value = "userToken") String userToken) {
        return teamManager.selectTeamsByStatus(status, userToken);
    }

    /**
     * 
     * @param userId
     * @param userToken
     * @return 
     * @purpose Used to retrieve the users team lists based on userId
     * @deprecated 
     */
    @RequestMapping(value = "/selectUserTeams/{userId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectUserTeams(@PathVariable("userId") int userId, @RequestHeader(value = "userToken") String userToken) {
        return teamManager.selectUserTeams(userId, userToken);
    }

    /**
     * 
     * @param team
     * @param userToken
     * @return 
     * @purpose Used to update team and team member details
     * @deprecated 
     */
    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object updateTeam(@RequestBody Team team, @RequestHeader(value = "userToken") String userToken) {
        return teamManager.updateTeam(team, userToken);
    }

    /**
     * 
     * @param teamId
     * @param userToken
     * @return 
     * @purpose Used to delete team.
     * @deprecated 
     */
    @RequestMapping(value = "/{teamId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public
    @ResponseBody
    Object deleteTeam(@PathVariable("teamId") int teamId, @RequestHeader(value = "userToken") String userToken) {
        return teamManager.deleteTeam(teamId, userToken);
    }

    /**
     * 
     * @param teamMember
     * @param userToken
     * @return 
     * @purpose Used to create team and team members.
     * @deprecated 
     */
    @RequestMapping(value = "/teamMember", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object addTeamMember(@RequestBody TeamMember teamMember, @RequestHeader(value = "userToken") String userToken) {
        return teamManager.addTeamMember(teamMember, userToken);
    }

    /**
     * 
     * @param teamId
     * @param userToken
     * @return 
     * @purpose Used to get list of team members of team based on teamId 
     * @deprecated 
     */
    @RequestMapping(value = "/{teamId}/teamMember", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectTeamMembers(@PathVariable("teamId") int teamId, @RequestHeader(value = "userToken") String userToken) {
        return teamManager.selectTeamMembers(teamId, userToken);
    }

    /**
     * 
     * @param teamId
     * @param userToken
     * @return 
     * @purpose Used to get list of team members except of TL based on teamId 
     * @deprecated 
     */
    @RequestMapping(value = "/{teamId}/teamMemberWithOutTL", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectTeamMembersWithOutTL(@PathVariable("teamId") int teamId, @RequestHeader(value = "userToken") String userToken) {
        return teamManager.selectTeamMembersWithOutTL(teamId, userToken);
    }

   
    /**
     * @structure changed and not in use anymore
     * @param teamMember
     * @param userToken
     * @return
     * @deprecated 
     */
    
    @RequestMapping(value = "/teamMember", method = RequestMethod.PUT, headers = "Accept=application/json")
    public
    @ResponseBody
    Object updateTeamMember(TeamMember teamMember, @RequestHeader(value = "userToken") String userToken) {
        return teamManager.updateTeamMember(teamMember, userToken);
    }

    /**
     * @structure changed and not in use anymore
     * @param memberId
     * @param userToken
     * @return
     * @deprecated 
     */
    
    @RequestMapping(value = "/teamMember/{memberId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public
    @ResponseBody
    Object deleteTeamMember(@PathVariable("memberId") int memberId, @RequestHeader(value = "userToken") String userToken) {
        return teamManager.deleteTeamMember(memberId, userToken);
    }

    /**
     * 
     * @param userToken
     * @author :anuja
     * @return 
     * @purpose :get team member location
     * @deprecated 
     */
    @RequestMapping(value = "/teamMemberLocation", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectTeamMemberLocation(@RequestHeader(value = "userToken") String userToken) {
        return teamManager.selectTeamMemberLocation(userToken);
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
    @RequestMapping(value = "/teamMembers", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object createTeamTeamMembers(@RequestBody Team team, @RequestHeader(value = "userToken") String userToken) {
        return teamManager.createTeamTeamMembers(team, userToken);
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
    @RequestMapping(value = "/teamMember/TL/{userId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectTeamMembersForTeamLead(@PathVariable("userId") int userId, @RequestHeader(value = "userToken") String userToken) {
        return teamManager.selectTeamMembersForTeamLead(userId, userToken);
    }
   
    /**
     * 
     * @param userToken
     *  @author: anuja
     * @return 
     * @purpose : list of team leads
     */
    @RequestMapping(value = "/TL", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectTeamLead(@RequestHeader(value = "userToken") String userToken) {
        return teamManager.selectTeamLead(userToken);
    }

    /**
     * 
     * @param userToken
     * @param userId
     * @return 
     * @purpose This object is used to retrieve user and his immediate subordinates
     */
    @RequestMapping(value = "/userPositions/{userId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectUserPositions(@RequestHeader(value = "userToken") String userToken, @PathVariable("userId") int userId) {
        return teamManager.selectUserPositions(userId, userToken);
    }

    /**
     * 
     * @param userToken
     * @param userId
     * @param date
     * @return 
     * @purpose This object is used to retrieve user and his immediate subordinates
     */
    @RequestMapping(value = "/userPositions/{userId}/{date}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectUserPositions(@RequestHeader(value = "userToken") String userToken, @PathVariable("userId") int userId, @PathVariable("date") String date) {
        return teamManager.selectUserPositions(userId, date, userToken);
    }
    //Siddhesh :demo purpose Search tab in my team.
    /**
     * 
     * @param userToken
     * @param userId
     * @param date
     * @purpose This object is used to retrieve users subordinates and their subordinates.
     */
    @RequestMapping(value = "/userPositionsWithSubs/{userId}/{date}",method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectUserPositionsWithSubs(@RequestHeader(value = "userToken") String userToken, @PathVariable("userId") int userId, @PathVariable("date") String date) {
        return teamManager.selectUserPositionsWithSubs(userId, date, userToken);
    }
    
    //Siddhesh :My Team mobile ticketNo:-
     /**
     * @param subordinates
     * @param userToken
     * @param userId
     * @param date
     * @purpose This object is used to retrieve user and his immediate subordinates for mobile My team.
     */
    @RequestMapping(value = "/mobile/userPositions/{userId}/{date}/{subordinates}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectUserPositionsForMobile(@RequestHeader(value = "userToken") String userToken, @PathVariable("userId") int userId, @PathVariable("date") String date,@PathVariable("subordinates") int subordinates) {
        return teamManager.selectUserPositionsMobileForAllSubordinates(userId, date, userToken,subordinates);
    }
    
    //Siddhesh :My Team mobile ticketNo:-
     /**
     * 
     * @param userToken
     * @param userId
     * @param date
     * @purpose This object is used to retrieve user and his immediate subordinates for mobile My team.
     */
    @RequestMapping(value = "/mobile/userPositions/{userId}/{date}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectUserPositionsForMobile(@RequestHeader(value = "userToken") String userToken, @PathVariable("userId") int userId, @PathVariable("date") String date) {
        return teamManager.selectUserPositionsMobile(userId, date, userToken);
    }
    
     //Siddhesh :My Team mobile ticketNo:-
    /**
     * 
     * @param userToken
     * @param userId
     * @purpose Used to retrieve specific user data for mobile my team function 
     */
    @RequestMapping(value = "/mobile/userPositions/detailedData/{userId}/{date}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectUserPositionsDetailedDataMobile(@RequestHeader(value = "userToken") String userToken, @PathVariable("userId") int userId, @PathVariable("date") String date) {
        return teamManager.selectUserSpecificDataMobile(userId,date,userToken);
    }
    /**
     * Added by jyoti, 25-02-2017
     * @param userToken
     * @param userId
     * @param date
     * @param fromDate
     * @return 
     * @purpose This object is used to retrieve user and his activityCount, subordinateCount
     */
    @RequestMapping(value = "/userPositions/{userId}/{date}/{fromDate}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectUserPositions(@RequestHeader(value = "userToken") String userToken, @PathVariable("userId") int userId, @PathVariable("date") String date, @PathVariable("fromDate") String fromDate) {
        return teamManager.selectUserPositions(userId, date, fromDate, userToken);
    }
    
    /**
     * Added by jyoti, 28-02-2017
     * @param userToken
     * @param userId
     * @param date
     * @return 
     * @purpose This object is used to retrieve user and his all subordinate
     */
    @RequestMapping(value = "/all/subordinatePositions/{userId}/{date}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectUserPositionsAllSubordinate(@RequestHeader(value = "userToken") String userToken, @PathVariable("userId") int userId, @PathVariable("date") String date) {
        return teamManager.selectUserPositionsAllSubordinate(userId, date, userToken);
    }
    
    /**
     * Added by jyoti, 14-july-2017
     * @param userToken
     * @param userId
     * @param date
     * @param fromDate
     * @return 
     * @purpose This object is used to retrieve user subordinates Of Subordinate
     */
    @RequestMapping(value = "/userPositions/subordinatesOfSubordinate/{userId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectUserPositionsSubordinatesOfSubordinate(@RequestHeader(value = "userToken") String userToken, @PathVariable("userId") int userId) {
        return teamManager.selectUserPositionsSubordinatesOfSubordinate(userId, userToken);
    }
    
    /**
     * 
     * @param userToken
     * @param userId
     * @return 
     * @purpose Used to retrieve users position order by name 
     */
    @RequestMapping(value = "/userPositions/orderbyName/{userId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectUserPositionsOrderByName(@RequestHeader(value = "userToken") String userToken, @PathVariable("userId") int userId) {
        return teamManager.selectUserPositionsOrderByName(userId, userToken);
    }

    /**
     * 
     * @param userToken
     * @return 
     * @purpose Used to retrieve organization chart details 
     */
    @RequestMapping(value = "/organizationChart", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectOrganizationChart(@RequestHeader(value = "userToken") String userToken) {
        return teamManager.selectOrganisationChart(userToken);
    }

    /**
     * 
     * @param userToken
     * @return 
     * @purpose Used to retrieve members in organization chart 
     */
    @RequestMapping(value = "/organizationChart/membersToChart", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectOrganizationMembersForChart(@RequestHeader(value = "userToken") String userToken) {
        return teamManager.selectOrganizationMembersForChart(userToken);
    }

    /**
     * 
     * @param team
     * @param parentId
     * @param userToken
     * @return 
     * @purpose Used to add users to organization chart
     */
    @RequestMapping(value = "/organizationChart/member/add/{parentId}", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object addMemberToOrganizationChart(@RequestBody Team team, @PathVariable("parentId") int parentId, @RequestHeader(value = "userToken") String userToken) {
        return teamManager.addMemberToOrganizationChart(team, parentId, userToken);
    }

    /**
     *
     * @param team
     * @param parentId
     * @param userToken
     * @return
     */
    @RequestMapping(value = "/organizationChart/member/edit/{parentId}", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object updateMemberToOrganizationChart(@RequestBody Team team, @PathVariable("parentId") int parentId, @RequestHeader(value = "userToken") String userToken) {
        return teamManager.updateMemberToOrganizationChart(team, parentId, userToken);
    }

    /**
     * 
     * @param orgChartId
     * @param userToken
     * @return 
     * @purpose Used to get details of member in organization chart 
     */
    @RequestMapping(value = "/organizationChart/member/get/{orgChartId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getMemberFromOrganizationChart(@PathVariable("orgChartId") int orgChartId, @RequestHeader(value = "userToken") String userToken) {
        return teamManager.getMemberFromOrganizationChart(orgChartId, userToken);
    }

    /**
     * 
     * @param oraganizationChart
     * @param userToken
     * @return 
     * @purpose Used to edit member in organization chart.
     */
    @RequestMapping(value = "/organizationChart/member/update", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Object editMemberInOrganizationChart(@RequestBody OraganizationChart oraganizationChart, @RequestHeader(value = "userToken") String userToken) {
        return teamManager.editOrganizationChartMember(oraganizationChart, userToken);
    }

    /**
     * 
     * @param userToken
     * @param teamId
     * @param userId
     * @return 
     * @purpose Used to retrieve immediate superior and immediate subordinate of user.
     */
    @RequestMapping(value = "/userImmidiateSuperiorsAndSubOrdiantes/{teamId}/{userId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectImmidiateSupiriorAndSubOrdiantes(@RequestHeader(value = "userToken") String userToken, @PathVariable("teamId") int teamId, @PathVariable("userId") int userId) {
        return teamManager.selectUserImmidiateSupiriorsAndSubordinates(teamId, userId, userToken);
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
    @RequestMapping(value = "/userSubordinate/{teamId}/{userId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object userIsSubordinate(@PathVariable("teamId") int teamId, @PathVariable("userId") int userId, @RequestHeader(value = "userToken") String userToken) {
        return teamManager.userIsSubordinate(teamId, userId, userToken);
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
    @RequestMapping(value = "/orgChart/{email}/{teamId}/{parentTeamId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public
    @ResponseBody
    Object removeFromOrganizationChart(@PathVariable("teamId") int teamId, @PathVariable("parentTeamId") int parentTeamId, @PathVariable("email") String email, @RequestHeader(value = "userToken") String userToken) {
        return teamManager.deleteMemberFromOrganizationChart(teamId, parentTeamId, email, userToken);
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
    @RequestMapping(value = "/allSubordinates/{userId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object getAllSubordinatesOfUser(@PathVariable("userId") int userId, @RequestHeader(value = "userToken") String userToken) {
        log4jLog.info("TeamService    getAllSubordinatesOfUser method");
        return teamManager.getAllSubordinatesOfUser(userId, userToken);
    }

    /**
     * @author anuja
     * @param userToken
     * @param date
     * @return 
     * @purpose To get all uses in organization
     * @date 13 March, 2015
     *
     */
    @RequestMapping(value = "/organizationChart/{date}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object selectAllOraganizationMember(@RequestHeader(value = "userToken") String userToken, @PathVariable("date") String date) {
        return teamManager.selectAllOraganizationMember(date, userToken);
    }

    /**
     * @author Ramesh
     * @param ownerId
     * @param userToken
     * @param memberId
     * @return 
     * @purpose To check is member is in hierarchy or not .
     * @date 29 March 2015 .
     */
    @RequestMapping(value = "/isMemberInHierarchy/{ownerId}/{memberId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Object isMemberInHierarchy(@PathVariable("ownerId") int ownerId, @PathVariable("memberId") int memberId, @RequestHeader(value = "userToken") String userToken) {
        return teamManager.isMemberInHierarchy(ownerId, memberId, userToken);
    }
}
