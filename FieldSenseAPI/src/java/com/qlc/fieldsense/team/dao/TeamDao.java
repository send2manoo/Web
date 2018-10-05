/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.team.dao;

import com.qlc.fieldsense.team.model.OraganizationChart;
import com.qlc.fieldsense.team.model.Team;
import com.qlc.fieldsense.team.model.TeamMember;
import com.qlc.fieldsense.user.model.User;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mayank
 */
public interface TeamDao {

    /**
     *
     * @param teamId
     * @param accountId
     * @return
     */
    Team selectTeam(int teamId, int accountId);

    /**
     *
     * @param accountId
     * @return
     */
    List<Team> selectTeam(int accountId);

    /**
     *
     * @param status
     * @param accountId
     * @return
     */
    List<Team> selectTeamByStatus(int status, int accountId);

    /**
     *
     * @param team
     * @param acountId
     * @return
     */
    int insertTeam(Team team, int acountId);

    /**
     *
     * @param team
     * @param accountId
     * @return
     */
    boolean updateTeam(Team team, int accountId);

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    boolean deleteTeam(int id, int accountId);


    boolean updateTeamCSV(String parentCSV,int parentId, int accountId);   // added by manohar


    /**
     *
     * @param userId
     * @param accountId
     * @return
     */

    List<Team> selectTeams(int userId, int accountId);
    
    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    int getTeamId(int userId, int accountId);

    /**
     *
     * @param teamId
     * @param accountId
     * @return
     */
    List<TeamMember> selectTeamMembers(int teamId, int accountId);

    /**
     *
     * @param teamId
     * @param accountId
     * @return
     */
    public List<TeamMember> selectTeamMembersWithOutTL(int teamId, int accountId);

    /**
     *
     * @param teamMember
     * @param accountId
     * @return
     */
    int insertTeamMember(TeamMember teamMember, int accountId);

    /**
     *
     * @param teamMember
     * @param accountId
     * @return
     */
    boolean updateTeamMember(TeamMember teamMember, int accountId);
    
    /**
     *
     * @param teamMember
     * @param accountId
     * @return
     */
    boolean updateTeamMemberId(TeamMember teamMember, int accountId);

    /**
     *
     * @param id
     * @param accountId
     * @return
     */
    boolean deleteTeamMember(int id, int accountId);

    /**
     *
     * @param teamId
     * @param userId
     * @param accountId
     * @return
     */
    boolean deleteTeamMember(int teamId, int userId, int accountId);

    /**
     *
     * @param teamMemberId
     * @param accountId
     * @return
     */
    public TeamMember selectTeamMember(int teamMemberId, int accountId);


     public   List<TeamMember> selectTeamMemberOnly(int teamMemberId, int accountId);   // added by manohar
    

    /**
     *
     * @param teamId
     * @param accountId
     * @return
     */

    public boolean isTeamValid(int teamId, int accountId);

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public List<Team> selectTeamsList(int userId, int accountId);

    /**
     *
     * @param teamId
     * @param accountId
     * @return
     */
    public List<TeamMember> selectTeamMembersLocation(int teamId, int accountId);

    /**
     *
     * @param teamId
     * @param userId
     * @param acountId
     * @return
     */
    public boolean isTeamMemeber(int teamId, int userId, int acountId);

    /**
     *
     * @param teamMember
     * @param teamId
     * @param accountId
     * @return
     */
    public int insertTeamMember(TeamMember teamMember, int teamId, int accountId);

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public List<TeamMember> selectTeamMembersForTeamLead(int userId, int accountId);

    /**
     *
     * @param teamId
     * @param accountId
     * @return
     */
    public boolean deleteTeamFromTeamMembers(int teamId, int accountId);


    public boolean updateTeamFromTeamMembers(int teamId,int userId, int accountId);

    /**
     *
     * @param userId
     * @param acountId
     * @return
     */

    public boolean isTeamMemeberIsInAnyTeam(int userId, int acountId);

    /**
     *
     * @param accountId
     * @return
     */
    public List<TeamMember> selectTeamLead(int accountId);

    /**
     *
     * @param userId
     * @param teamName
     * @param accountId
     * @return
     */
    public boolean isTeamNameUnique(int userId, String teamName, int accountId);

    /**
     *
     * @param teamId
     * @param accountId
     * @return
     */
    public String teamName(int teamId, int accountId);

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public List<TeamMember> selectUserPositions(int userId, int accountId);
    
    /**
     * Added by jyoti, 14-july-2017
     * @param userId
     * @param accountId
     * @return
     */
    public List<User> selectUserPositionsSubordinatesOfSubordinate(int userId, int accountId);

    /**
     *
     * @param userId
     * @param date
     * @param accountId
     * @return
     */
    public List<TeamMember> selectUserPositions(int userId, String date, int accountId);
    
    public List<TeamMember> selectUserPositionsWithSubs(int userId, String date, int accountId);
    
    public List<TeamMember> selectUserPositionsWithSubsDetailedData(int userId, String date, int accountId);
    
    public List<Object> selectUserPositionsMobile(int userId, String date, int accountId);
    
    public List<Object> selectUserDetailedPositionsMobile(int userId, String date, int accountId);
    
    /**
     *
     * @param userId
     * @param date
     * @param accountId
     * @return
     */
    public List<TeamMember> selectUserPositionsAllSubordinate(int userId, String date, int accountId);
    
    /**
     *
     * @param userId
     * @param date
     * @param fromDate
     * @param toDate
     * @param accountId
     * @return
     */
    public List<TeamMember> selectUserPositions(int userId, String date, String fromDate, String toDate, int accountId);

    /**
     *
     * @param accountId
     * @return
     */
    public List<OraganizationChart> selectOraganizationChart(int accountId);

    /**
     *
     * @param accountId
     * @return
     */
    public List<User> selectOrganizationMembersForChart(int accountId);

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public String selectUserPositionCsv(int userId, int accountId);

    /**
     *
     * @param positionCsv
     * @param teamId
     * @param accountId
     * @return
     */
    public boolean updateUserPostionCsv(String positionCsv, int teamId, int accountId);

    /**
     *
     * @param memberId
     * @param accountId
     * @return
     */
    public OraganizationChart selectOraganizationChartMember(int memberId, int accountId);


    public int getParentID(int parentTeamId, int accountId);        // added by manohar
    
    public int getParentCSV(int parent_id, int accountId);    // added by manohar
    

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */

    public boolean deleteTeamWithUserId(int userId, int accountId);

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public boolean deleteTeamMemberWithUserId(int userId, int accountId);

    /**
     *
     * @param orgChartId
     * @param userId
     * @param accountId
     * @return
     */
    public boolean updateOrganizationChartMemberInTeams(int orgChartId, int userId, int accountId);

    /**
     *
     * @param parent_id
     * @param accountId
     * @return
     */
    public int getParentUserId(int parent_id, int accountId);



    /**
     *
     * @param parent_user_id
     * @param userId
     * @param accountId
     * @return
     */
    public boolean updateReportingHead(int parent_user_id,int userId,int accountId);

            
    /**
     *
     * @param orgChartId
     * @param memberType
     * @param userId
     * @param accountId
     * @param editUserId
     * @return
     */
    public boolean updateOrganizationChartMemberInTeamMembers(int orgChartId, int memberType, int userId, int accountId, int editUserId);

    /**
     *
     * @param orgChart
     * @param accountId
     * @return
     */
    public User selectOrganizationChartUser(int orgChart, int accountId);

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public List<TeamMember> selectUserImmidiateSupiriorAndSubOrdinatePositions(int userId, int accountId);

    /**
     *
     * @param accountId
     * @param teamId
     * @param userId
     * @return
     */
    public int userIsSubordinate(int accountId, int teamId, int userId);

    /**
     *
     * @param accountId
     * @param teamId
     * @return
     */
    public int userIsReportingHead(int accountId, int teamId);

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public boolean isUserInOrganizationChart(int userId, int accountId);

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public List<Team> selectUserSubordiatesPositionIds(int userId, int accountId);

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public boolean deleteTeamMemberFromTeam(int userId, int accountId);

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public List<TeamMember> selectUserPositionsOrderByName(int userId, int accountId);

    /**
     *
     * @param team
     * @param teamPositionCSV
     * @param acountId
     * @param fieldSenseUtils
     * @return
     */
    public int insertDefaultTeam(Team team, String teamPositionCSV, int acountId,FieldSenseUtils fieldSenseUtils);

    /**
     *
     * @param teamId
     * @param accountId
     * @return
     */
    public int selectImmidiateSubOrdinatesCount(int teamId, int accountId);

    /**
     *
     * @param teamId
     * @param subordinateStatus
     * @param accountId
     * @return
     */
    public boolean updateHasSubordiantes(int teamId, int subordinateStatus, int accountId);

    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public List<User> selectUserAllSubordinates(int userId, int accountId);

    /**
     *
     * @param date
     * @param accountId
     * @return
     */
    public List<TeamMember> selectAllOraganizationMember(String date, int accountId);

    /**
     *
     * @param ownerId
     * @param memberId
     * @param accountId
     * @return
     */
    public boolean isMemberInHierarchy(int ownerId, int memberId, int accountId);
    
    /**
     *
     * @param userId
     * @param accountId
     * @return
     */
    public java.util.HashMap selectUserPositionCsvUsingUserId(int userId, int accountId);
    
    /**
     *
     * @param nheadsExistingCSV
     * @param nheadsNewCSV
     * @param accountId
     * @return
     */
    public int updateSubOrdinatesPositionReportTo(String nheadsExistingCSV, String nheadsNewCSV ,int accountId);

    /**
     *
     * @param parentTeam
     * @param accountId
     * @return
     */
    public int numberOfSubordinate(int parentTeam,int accountId);
    
    
      public int selectTeamId(int parentTeam,int accountId);
    
    
    /**
     *
     * @param teamMemberList
     * @param userToken
     * @param accountId
     * @param fieldSenseUtils
     * @return
     */
    public int insertDefaultTeamMember(java.util.ArrayList<TeamMember> teamMemberList, String userToken,int accountId,FieldSenseUtils fieldSenseUtils);
    
    public List<Team> selectUserSubordiatesPositionIdsForSuperAdmin(final int teamId, int accountId);
    
     public int checkSubordinatePersent(int teamId, int accountId);
     
     public List<Object> selectUserPositionsMobileForAllSubordinate(int userId, String date, int accountId,int subordinate);
    
}
