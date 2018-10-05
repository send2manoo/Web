/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * @author Ramesh
 * @date 12-03-2014
 */
var teamMembersUpdateArray=new Array();
usersArray = new Array();
var doubleClickFlag=true;
var teamMembersDatatableIndex=0;
var intervalIndex= window.setInterval(function(){
    var userToken2=fieldSenseGetCookie("userToken");
    try{
        if(userToken2.toString()=="undefined"){
            window.location.href = "login.html";
        }else if(loginUserId!=0){
            loggedinUserImageData();
            loggedinUserData();
            adminTeams();
            window.clearTimeout(intervalIndex);
        }
    }
    catch(err){
        window.location.href = "login.html";
    }
},1000);
function adminTeams(){
    $.ajax({
        type: "GET",
        url: fieldSenseURL+"/team",
        contentType: "application/json; charset=utf-8",
        headers:{
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn:true,
        success: function(data) {
            if(data.errorCode=='0000'){
                var teamsData=data.result;
                var adminTeamsHtml='';
                adminTeamsHtml+='<div class="page-content-wrapper">';
                adminTeamsHtml+='<div class="page-content">';
                adminTeamsHtml+='<div class="row">';
                adminTeamsHtml+='<div class="col-md-12">';
                adminTeamsHtml+='<ul class="page-breadcrumb breadcrumb">';
                adminTeamsHtml+='<li>';
                adminTeamsHtml+='<i class="fa fa-group"></i>';
                adminTeamsHtml+='<a href="#">Teams</a>';
                adminTeamsHtml+='</li>';
                adminTeamsHtml+='</ul>';
                adminTeamsHtml+='</div>';
                adminTeamsHtml+='</div>';
                adminTeamsHtml+='<div class="row">';
                adminTeamsHtml+='<div class="col-md-12">';
                adminTeamsHtml+='<div class="portlet-body">';
                adminTeamsHtml+='<div class="search">';
                adminTeamsHtml+='<input type="text" aria-controls="sample_3" class="form-control input-large" placeholder="Search" id="id_teamSearchText">';
                adminTeamsHtml+='<div class="actions">';
                adminTeamsHtml+='<a data-toggle="modal" href="#" class="btn btn-info" onClick="searchTeams();"><i class="fa fa-search"></i></a>';
                adminTeamsHtml+='</div>';
                adminTeamsHtml+='</div>';
                adminTeamsHtml+='<button type="button" class="btn btn-info" style="float:right;" onclick="addTeamTemplate();"><i class="fa fa-plus"></i> Add Team</button>';
                adminTeamsHtml+='<span id="id_tbl_teams">';
                adminTeamsHtml+='</span>';
                adminTeamsHtml+='</div>';
                adminTeamsHtml+='</div>';
                adminTeamsHtml+='</div>';
                adminTeamsHtml+='</div>';
                adminTeamsHtml+='</div>';
                $('#id_mid').html(adminTeamsHtml);
                teamsDataTableShow(teamsData);
            }
        }
    });
}
function searchTeams(){
    var searchText=$('#id_teamSearchText').val().trim();
    if(searchText.length==0){
        return false;
    }
    $.ajax({
        type: "GET",
        url: fieldSenseURL+"/search/team/"+searchText,
        contentType: "application/json; charset=utf-8",
        headers:{
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn:true,
        success: function(data) {
            if(data.errorCode=='0000'){
                var teamsData=data.result;
                teamsDataTableShow(teamsData);
            }
        }
    });
}
function teamsDataTableShow(teamsData){
    var teamsDataTableShowHtml='';
    teamsDataTableShowHtml+='<table class="table table-striped table-bordered table-hover" id="sample_31">';
    teamsDataTableShowHtml+='<thead>';
    teamsDataTableShowHtml+='<tr>';
    teamsDataTableShowHtml+='<th>';
    teamsDataTableShowHtml+='Name';
    teamsDataTableShowHtml+='</th>';
    teamsDataTableShowHtml+='<th>';
    teamsDataTableShowHtml+='Team Leader';
    teamsDataTableShowHtml+='</th>';
    teamsDataTableShowHtml+='<th>';
    teamsDataTableShowHtml+='Member Count';
    teamsDataTableShowHtml+='</th>';
    teamsDataTableShowHtml+='<th>';
    teamsDataTableShowHtml+='Active';
    teamsDataTableShowHtml+='</th>';
    teamsDataTableShowHtml+='<th>';
    teamsDataTableShowHtml+='</th>';
    teamsDataTableShowHtml+='</tr>';
    teamsDataTableShowHtml+='</thead>';
    teamsDataTableShowHtml+='<tbody>';
    for(var i=0;i<teamsData.length;i++){
        var teamId=teamsData[i].id;
        var teamName=teamsData[i].teamName;
        var ownerName=teamsData[i].ownerId.firstName+' '+teamsData[i].ownerId.lastName;
        var teamMembersCount=teamsData[i].teamMembersCount;
        var teamActive=teamsData[i].isActive;
        teamsDataTableShowHtml+='<tr>';
        teamsDataTableShowHtml+='<td>';
        teamsDataTableShowHtml+='<a data-toggle="modal" href="#">'+teamName+'</a>';
        teamsDataTableShowHtml+='</td>';
        teamsDataTableShowHtml+='<td>'+ownerName+'';
        teamsDataTableShowHtml+='</td>';
        teamsDataTableShowHtml+='<td>';
        teamsDataTableShowHtml+=''+teamMembersCount+'';
        teamsDataTableShowHtml+='</td>';
        teamsDataTableShowHtml+='<td>';
        if(teamActive==0){
            teamsDataTableShowHtml+='<input type="checkbox" disabled >';
        }else{
            teamsDataTableShowHtml+='<input type="checkbox" checked disabled >';
        }
        teamsDataTableShowHtml+='</td>';
        teamsDataTableShowHtml+='<td>';
        teamsDataTableShowHtml+='<button type="button" class="btn btn-default btn-xs tooltips" data-toggle="modal" href="#" data-placement="bottom" title="Edit" onClick="updateTeamHtmlTemplate(\''+teamId+'\')" ><i class="fa fa-edit"></i></button>';
        teamsDataTableShowHtml+='<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Delete"><i class="fa fa-trash-o"></i></button>';
        teamsDataTableShowHtml+='</td>';
        teamsDataTableShowHtml+='</tr>';
    }
    teamsDataTableShowHtml+='</tbody>';
    teamsDataTableShowHtml+='</table>';
    $('#id_tbl_teams').html(teamsDataTableShowHtml);
    App.init();
    TableAdvanced.init();
}
function teamMembersForUpdateTeam(teamId){
    $.ajax({
        type: "GET",
        url: fieldSenseURL+"/team/"+teamId+"/teamMemberWithOutTL",
        contentType: "application/json; charset=utf-8",
        headers:{
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn:false,
        success: function(membersData) {
            var teamMembers=membersData.result;
            teamMembersDatatableIndex=0;
            var teamMembersForUpdateTeamHtml='';
            teamMembersForUpdateTeamHtml+='<div class="portlet">';
            teamMembersForUpdateTeamHtml+='<div class="portlet-title">';
            teamMembersForUpdateTeamHtml+='<div class="caption">';
            teamMembersForUpdateTeamHtml+='<i class="fa fa-list"></i>Team Members';
            teamMembersForUpdateTeamHtml+='</div>';
            teamMembersForUpdateTeamHtml+='<div class="actions">';
            teamMembersForUpdateTeamHtml+='<a data-toggle="modal" href="#responsive" class="btn btn-info" onClick="addTeamMembersTemplate();"><i class="fa fa-pencil"></i> Add</a>';
            teamMembersForUpdateTeamHtml+='</div>';
            teamMembersForUpdateTeamHtml+='</div>';
            teamMembersForUpdateTeamHtml+='<div class="portlet-body">';
            teamMembersForUpdateTeamHtml+='<table class="table table-striped table-bordered table-hover" id="sample_2">';
            teamMembersForUpdateTeamHtml+='<thead>';
            teamMembersForUpdateTeamHtml+='<tr>';
            teamMembersForUpdateTeamHtml+='<th>';
            teamMembersForUpdateTeamHtml+='Member Name';
            teamMembersForUpdateTeamHtml+='</th>';
            teamMembersForUpdateTeamHtml+='<th>';
            teamMembersForUpdateTeamHtml+='Member Type';
            teamMembersForUpdateTeamHtml+='</th>';
            teamMembersForUpdateTeamHtml+='<th>';
            teamMembersForUpdateTeamHtml+='Active';
            teamMembersForUpdateTeamHtml+='</th>';
            teamMembersForUpdateTeamHtml+='<th>';
            teamMembersForUpdateTeamHtml+='</th>';
            teamMembersForUpdateTeamHtml+='</tr>';
            teamMembersForUpdateTeamHtml+='</thead>';
            teamMembersForUpdateTeamHtml+='<tbody>';
            var j=0;
            for(var i=0;i<teamMembers.length;i++){
                j++
                teamMembersDatatableIndex++;
                var teamMemberName=teamMembers[i].user.firstName+" "+teamMembers[i].user.lastName;
                var teamMemberId=teamMembers[i].user.id;
                var memberType=teamMembers[i].memberType;
                var memberTypeFirst=teamMembers[i].memberType;
                var memberStatus=teamMembers[i].status;
                if(memberType!==2){
                    if(memberType==1){
                        memberType='Admin';
                    }else if(memberType==2){
                        memberType='Team Leader';
                    }
                    else if(memberType==3){
                        memberType='Team Member';
                    }
                    var member={
                        "memberId": teamMemberId,
                        "memberName": teamMemberName,
                        "memberType": memberTypeFirst,
                        "memberIsActive": memberStatus,
                        "type":-1
                    }
                    teamMembersUpdateArray.push(member);
                    var status=teamMembers[i].status;
                    teamMembersForUpdateTeamHtml+='<tr id="id_teamMemberAdd'+teamMembersDatatableIndex+'">';
                    teamMembersForUpdateTeamHtml+='<td>';
                    teamMembersForUpdateTeamHtml+='<a data-toggle="modal" href="#responsive1" onClick="viewTeamMemberTemplate(\''+teamMemberId+'\')">'+teamMemberName+'</a>';
                    teamMembersForUpdateTeamHtml+='</td>';
                    teamMembersForUpdateTeamHtml+='<td id="teamMemberType'+teamMemberId+'">';
                    teamMembersForUpdateTeamHtml+=''+memberType+'';
                    teamMembersForUpdateTeamHtml+='</td>';
                    teamMembersForUpdateTeamHtml+='<td id="teamMemberstatus'+teamMemberId+'">';
                    if(status==0){
                        teamMembersForUpdateTeamHtml+='<input type="checkbox" disabled/>';
                    }else{
                        teamMembersForUpdateTeamHtml+='<input type="checkbox" checked disabled/>';
                    }
                    teamMembersForUpdateTeamHtml+='</td>';
                    teamMembersForUpdateTeamHtml+='<td>';
                    teamMembersForUpdateTeamHtml+='<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Edit" data-toggle="modal" href="#responsive2" onClick="editMemberTemplate(\''+teamMemberId+'\')"><i class="fa fa-edit"></i></button>';
                    teamMembersForUpdateTeamHtml+='<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Delete" onClick="deleteMember(\''+teamMemberId+'\',\''+teamMembersDatatableIndex+'\');return false;"><i class="fa fa-trash-o"></i></button>';
                    teamMembersForUpdateTeamHtml+='</td>';
                    teamMembersForUpdateTeamHtml+='</tr>';
                }
            }
            var n=j+100;
            for(j;j<n;j++){
                teamMembersForUpdateTeamHtml+='<tr id="id_teamMemberAdd'+j+'"></tr>';
            }
            teamMembersForUpdateTeamHtml+='</tbody>';
            teamMembersForUpdateTeamHtml+='</table>';
            teamMembersForUpdateTeamHtml+='</div>';
            teamMembersForUpdateTeamHtml+='</div>';
            $('#id_teamMembersdatatable').html(teamMembersForUpdateTeamHtml);
            jQuery('.tooltips').tooltip();
        }
    });
}
function addTeamMembersTemplate(){
    $('.cls_addteamMember').html('');
    var addTeamMembersTemplateHtml='';
    addTeamMembersTemplateHtml+='<div class="modal-dialog modal-wide">';
    addTeamMembersTemplateHtml+='<div class="modal-content">';
    addTeamMembersTemplateHtml+='<div class="modal-header">';
    addTeamMembersTemplateHtml+='<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
    addTeamMembersTemplateHtml+='<h4 class="modal-title">Add Member</h4>';
    addTeamMembersTemplateHtml+='</div>';
    addTeamMembersTemplateHtml+='<div class="modal-body">';
    addTeamMembersTemplateHtml+='<form class="form-horizontal" role="form">';
    addTeamMembersTemplateHtml+='<div class="form-body">';
    addTeamMembersTemplateHtml+='<div class="form-group">';
    addTeamMembersTemplateHtml+='<label class="col-md-6 control-label">Member Name</label>';
    addTeamMembersTemplateHtml+='<div class="col-md-6 input-large">';
    addTeamMembersTemplateHtml+='<select class="form-control" id="id_teamMemberSelect">';
    addTeamMembersTemplateHtml+='<option value="0">Select Member</option>';
    for(var i=0;i< usersArray.length;i++){
        var el= usersArray[i];
        addTeamMembersTemplateHtml+='<option value="'+el.userId+'">'+el.userName+'</option>';
    }
    addTeamMembersTemplateHtml+='</select>';
    addTeamMembersTemplateHtml+='</div>';
    addTeamMembersTemplateHtml+='</div>';
    addTeamMembersTemplateHtml+='<div class="form-group">';
    addTeamMembersTemplateHtml+='<label class="col-md-6 control-label">Member Type</label>';
    addTeamMembersTemplateHtml+='<div class="col-md-6 input-large">';
    addTeamMembersTemplateHtml+='<select class="form-control" id="id_memberType">';
    addTeamMembersTemplateHtml+='<option value="0">Select Type</option>';
    addTeamMembersTemplateHtml+='<option value="3">Team Member</option>';
    addTeamMembersTemplateHtml+='</select>';
    addTeamMembersTemplateHtml+='</div>';
    addTeamMembersTemplateHtml+='</div>';
    addTeamMembersTemplateHtml+='</div>';
    addTeamMembersTemplateHtml+='</form>';
    addTeamMembersTemplateHtml+='</div>';
    addTeamMembersTemplateHtml+='<div class="modal-footer">';
    addTeamMembersTemplateHtml+='<button type="submit" class="btn btn-info" onClick="addTeamMember();return;">Save</button>';
    addTeamMembersTemplateHtml+='<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
    addTeamMembersTemplateHtml+='</div>';
    addTeamMembersTemplateHtml+='</div>';
    addTeamMembersTemplateHtml+='</div>';
    $('.cls_addteamMember').html(addTeamMembersTemplateHtml);
}
function addTeamMember(){
    teamMembersDatatableIndex++;
    var indexOfMemberName = document.getElementById("id_teamMemberSelect");
    var teamMemberName =indexOfMemberName.options[indexOfMemberName.selectedIndex].text;
    var teamMemberId =indexOfMemberName.options[indexOfMemberName.selectedIndex].value;
    if(teamMemberId==0){
        fieldSenseTosterError("please select team member name ",true);
        return false;
    }
    var indexOfMemberType = document.getElementById("id_memberType");
    var teamMemberType =indexOfMemberType.options[indexOfMemberType.selectedIndex].value;
    if(teamMemberType==0){
        fieldSenseTosterError("please select team member type",true);
        return false;
    }
    for(var j=0;j<teamMembersUpdateArray.length;j++){
        var el= teamMembersUpdateArray[j];
        if((teamMemberId==el.memberId)&&(el.type!=0)){
            fieldSenseTosterError("This members is already in team",true);
            return false;
        }
    }
    var member={
        "memberId": teamMemberId,
        "memberName": teamMemberName,
        "memberType": teamMemberType,
        "memberIsActive": 1,
        "type":1
    }
    teamMembersUpdateArray.push(member);
    $(".cls_addteamMember").modal('hide');
    var memeberTypeText='';
    if(teamMemberType==3){
        memeberTypeText='Team Member';
    }
    var addTeamMemberHtml='';
    addTeamMemberHtml+='<td>';
    addTeamMemberHtml+='<a data-toggle="modal" href="#responsive1" onClick="viewTeamMemberTemplate(\''+teamMemberId+'\')">'+teamMemberName+'</a>';
    addTeamMemberHtml+='</td>';
    addTeamMemberHtml+='<td id="teamMemberType'+teamMemberId+'">';
    addTeamMemberHtml+=''+memeberTypeText+'';
    addTeamMemberHtml+='</td>';
    addTeamMemberHtml+='<td id="teamMemberstatus'+teamMemberId+'">';
    addTeamMemberHtml+='<input type="checkbox" checked disabled/>';
    addTeamMemberHtml+='</td>';
    addTeamMemberHtml+='<td>';
    addTeamMemberHtml+='<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Edit" data-toggle="modal" href="#responsive2" onClick="editMemberTemplate(\''+teamMemberId+'\')"><i class="fa fa-edit"></i></button>';
    addTeamMemberHtml+='<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Delete" onClick="deleteMember(\''+teamMemberId+'\',\''+teamMembersDatatableIndex+'\');return false;"><i class="fa fa-trash-o"></i></button>';
    addTeamMemberHtml+='</td>';
    $('#id_teamMemberAdd'+teamMembersDatatableIndex).append(addTeamMemberHtml);
    jQuery('.tooltips').tooltip();
}
function deleteMember(teamMemberId,index){
    var len=0;
    var tempArray= new Array();
    for(var i=0;i<teamMembersUpdateArray.length;i++){
        var e= teamMembersUpdateArray[i];
        var type= e.type;
        if(type!=0){
            len=len+1;
            var member={
                "memberId": e.teamMemberId,
                "memberName": e.teamMemberName,
                "memberType": e.memberTypeFirst,
                "memberIsActive": e.memberStatus,
                "type":e.type
            }
            tempArray.push(member);
        }
    }
    if(len!=1){
        for(var j=0,k=0;j<tempArray.length;j++,k++){
            var el= teamMembersUpdateArray[k];
            if(teamMemberId==el.memberId){
                el['type']=0;
                break;
            }
        }
        $('#id_teamMemberAdd'+index).hide();
    }else{
        fieldSenseTosterError("You need to add at least one member in team",true);
    }

}
function editMemberTemplate(teamMemberId){
    for(var i=0;i<teamMembersUpdateArray.length;i++){
        var e= teamMembersUpdateArray[i];
        if(teamMemberId==e.memberId){
            var editMemberTemplateHtml='';
            editMemberTemplateHtml+='<div class="modal-dialog modal-wide">';
            editMemberTemplateHtml+='<div class="modal-content">';
            editMemberTemplateHtml+='<div class="modal-header">';
            editMemberTemplateHtml+='<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
            editMemberTemplateHtml+='<h4 class="modal-title">Edit Member Details</h4>';
            editMemberTemplateHtml+='</div>';
            editMemberTemplateHtml+='<div class="modal-body">';
            editMemberTemplateHtml+='<form class="form-horizontal" role="form">';
            editMemberTemplateHtml+='<div class="form-body">';
            editMemberTemplateHtml+='<div class="form-group">';
            editMemberTemplateHtml+='<label class="col-md-6 control-label">Member Name</label>';
            editMemberTemplateHtml+='<div class="col-md-6 input-large">';
            editMemberTemplateHtml+='<input type="name" class="form-control" placeholder="'+e.memberName+'" disabled>';
            editMemberTemplateHtml+='</div>';
            editMemberTemplateHtml+='</div>';
            editMemberTemplateHtml+='<div class="form-group">';
            editMemberTemplateHtml+='<label class="col-md-6 control-label">Member Type</label>';
            editMemberTemplateHtml+='<div class="col-md-6 input-large">';
            editMemberTemplateHtml+='<select class="form-control" id="id_mTypeedit">';
            editMemberTemplateHtml+='<option value="0">Select Type</option>';
            if(e.memberType==3){
                editMemberTemplateHtml+='<option selected value="3">Team Member</option>';
            }
            editMemberTemplateHtml+='</select>';
            editMemberTemplateHtml+='</div>';
            editMemberTemplateHtml+='</div>';
            editMemberTemplateHtml+='</div>';
            editMemberTemplateHtml+='</form>';
            editMemberTemplateHtml+='</div>';
            editMemberTemplateHtml+='<div class="modal-footer">';
            editMemberTemplateHtml+='<button type="submit" class="btn btn-info" onclick="editMemberU(\''+teamMemberId+'\')">Save</button>';
            editMemberTemplateHtml+='<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
            editMemberTemplateHtml+='</div>';
            editMemberTemplateHtml+='</div>';
            editMemberTemplateHtml+='</div>';
        }
    }
    $('.cls_memberEdit').html(editMemberTemplateHtml);
}
function editMemberU(teamMemberId){
    var memberTypesIndex=document.getElementById("id_mTypeedit");
    var memberType =memberTypesIndex.options[memberTypesIndex.selectedIndex].value;
    if(memberType==0){
        fieldSenseTosterError("please select team member type",true);
        return false;
    }
    for(var i=0;i<teamMembersUpdateArray.length;i++){
        var e= teamMembersUpdateArray[i];
        if(teamMemberId==e.memberId){
            e['memberType']=memberType;
            e['type']=2;
            break;
        }
    }
    $(".cls_memberEdit").modal('hide');
    if(memberType==3){
        memberType='Team Member';
    }
    $('#teamMemberType'+teamMemberId+'').html(memberType);
}
function viewTeamMemberTemplate(teamMemberId){
    for(var i=0;i<teamMembersUpdateArray.length;i++){
        var e= teamMembersUpdateArray[i];
        if(teamMemberId==e.memberId){
            var viewTeamMemberTemplateHtml='';
            viewTeamMemberTemplateHtml+='<div id="responsive1" class="modal fade" tabindex="-1" data-width="760">';
            viewTeamMemberTemplateHtml+='<div class="modal-header">';
            viewTeamMemberTemplateHtml+='<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
            viewTeamMemberTemplateHtml+='<h4 class="modal-title">View Member Details</h4>';
            viewTeamMemberTemplateHtml+='</div>';
            viewTeamMemberTemplateHtml+='<div class="modal-body">';
            viewTeamMemberTemplateHtml+='<form class="form-horizontal" role="form">';
            viewTeamMemberTemplateHtml+='<div class="form-body">';
            viewTeamMemberTemplateHtml+='<div class="form-group">';
            viewTeamMemberTemplateHtml+='<label class="col-md-6 control-label">Member Name</label>';
            viewTeamMemberTemplateHtml+='<label class="col-md-6 label2">'+e.memberName+'</label>';
            viewTeamMemberTemplateHtml+='</div>';
            viewTeamMemberTemplateHtml+='<div class="form-group">';
            viewTeamMemberTemplateHtml+='<label class="col-md-6 control-label">Member Type</label>';
            if(e.memberType==2){
                viewTeamMemberTemplateHtml+='<label class="col-md-6 label2">Team Leader</label>';
            }
            if(e.memberType==3){
                viewTeamMemberTemplateHtml+='<label class="col-md-6 label2">Team Member</label>';
            }
            viewTeamMemberTemplateHtml+='</div>';
            viewTeamMemberTemplateHtml+='</div>';
            viewTeamMemberTemplateHtml+='</form>';
            viewTeamMemberTemplateHtml+='</div>';
            viewTeamMemberTemplateHtml+='<div class="modal-footer">';
            viewTeamMemberTemplateHtml+='<button type="button" data-dismiss="modal" class="btn btn-default">Close</button>';
            viewTeamMemberTemplateHtml+='</div>';
            viewTeamMemberTemplateHtml+='</div>';
        }
    }
    $('#id_viewTeamMember').html(viewTeamMemberTemplateHtml);
}
function updateTeam(teamId){
    if(doubleClickFlag){
        var teamName=$('#id_teamNameedit').val().trim();
        if(teamName.trim().length==0){
            fieldSenseTosterError("Team Name cannot be empty",true);
            return false;
        }

        var teamDescription=$('#id_teamdescriptionedit').val().trim();
        if(teamName.trim().length>100){
            fieldSenseTosterError("Team name should not be more than 100 characters",true);
            return false;
        }
        if(teamDescription.trim().length!==0){
            if(teamDescription.trim().length>500){
                fieldSenseTosterError("Description should not be more than 500 characters",true);
                return false;
            }
        }

        var teamLeaderSelect = document.getElementById("id_teamLeader");
        var teamLeaderId =teamLeaderSelect.options[teamLeaderSelect.selectedIndex].value;
        if(teamLeaderId==0){
            fieldSenseTosterError("Please select team leader .",true);
            return false;
        }

        var teamMembers=new Array();
        var deleteCounter=0;
        for(var i=0;i<teamMembersUpdateArray.length;i++){
            var e=teamMembersUpdateArray[i];
            if(e.type==0){
                deleteCounter++;
            }
            var teamMem={
                "teamId":parseInt(teamId),
                "user":{
                    "id":parseInt(e.memberId)
                },
                "memberType":e.memberType,
                "createdBy":{
                    "id":loginUserId
                },
                "status":1,
                "type":parseInt(e.type)
            }
            teamMembers.push(teamMem);
        }
        if(deleteCounter==teamMembersUpdateArray.length){
            fieldSenseTosterError(" Team should contain atleast 1 member", true);
            return false;
        }
        doubleClickFlag=false;
        var teamObject={
            "id":teamId,
            "teamName":teamName,
            "description":teamDescription,
            "ownerId":{
                "id":teamLeaderId
            },
            "isActive":1,
            "createdBy":{
                "id":loginUserId
            },
            "teamMembers":teamMembers
        }
        var jsonData = JSON.stringify( teamObject );
        $.ajax({
            type: "PUT",
            url: fieldSenseURL+"/team",
            contentType: "application/json; charset=utf-8",
            headers:{
                "userToken": userToken
            },
            crossDomain: false,
            data : jsonData,
            cache: false,
            dataType: 'json',
            success: function(data) {
                if(data.errorCode=='0000'){
                    fieldSenseTosterSuccess(data.errorMessage,true);
                    adminTeams();
                    doubleClickFlag=true;
                }else{
                    fieldSenseTosterError(data.errorMessage,true);
                    FieldSenseInvalidToken(data.errorMessage);
                    doubleClickFlag=true;
                }
            }
        });
    }
}
function addTeam(){
    if(doubleClickFlag){
        var teamName= document.getElementById("id_teamName").value.trim();
        var activeIs=1;
        if(teamName.trim().length==0){
            fieldSenseTosterError("Team Name cannot be empty",true);
            return false;
        }
        if(teamName.trim().length>100){
            fieldSenseTosterError("Team name should not be more than 100 characters",true);
            return false;
        }
        var description= document.getElementById("id_description").value.trim();
        if(description.trim().length!==0){
            if(description.trim().length>500){
                fieldSenseTosterError("Description should not be more than 500 characters",true);
                return false;
            }
        }
        var teamLeaderSelect = document.getElementById("id_teamLeader");
        var teamLeaderId =teamLeaderSelect.options[teamLeaderSelect.selectedIndex].value;
        if(teamLeaderId==0){
            fieldSenseTosterError("Please select team leader .",true);
            return false;
        }
        var teamMArray=[];
        if(teamMembersToAdd.length==0){
            fieldSenseTosterError(" Team should contain atleast 1 member",true);
            return false;
        }
        doubleClickFlag=false;
        for(var i=0;i<teamMembersToAdd.length;i++){
            var e= teamMembersToAdd[i];
            var temp =1;
            var mType;
            if(e.memberType=="Team Member"){
                mType=3;
            }
            var id=parseInt(e.memberId);
            var teamMem={
                "teamId":0,
                "user":{
                    "id":id
                },
                "memberType":mType,
                "createdBy":{
                    "id":loginUserId
                },
                "status":temp
            }

            teamMArray.push(teamMem);
        }
        var teamOwnerAsTeamMember ={
            "teamId":0,
            "user":{
                "id":teamLeaderId
            },
            "memberType":2,
            "createdBy":{
                "id":loginUserId
            },
            "status":1
        }
        teamMArray.push(teamOwnerAsTeamMember);
        var teamObject={
            "teamName":teamName,
            "description":description,
            "ownerId":{
                "id":teamLeaderId
            },
            "isActive":activeIs,
            "createdBy":{
                "id":loginUserId
            },
            "teamMembers":teamMArray
        }

        var jsonData = JSON.stringify( teamObject );

        $.ajax({
            type: "POST",
            url: fieldSenseURL+"/team/teamMembers",
            contentType: "application/json; charset=utf-8",
            headers:{
                "userToken": userToken
            },
            crossDomain: false,
            data : jsonData,
            cache: false,
            dataType: 'json',
            success: function(data) {
                if(data.errorCode=='0000'){
                    $('#id_teamName').val('');
                    $('#id_description').val('');
                    $('#id_leftaside').html('');
                    //                    userTeams();
                    adminTeams();
                    usersArray = new Array();
                    teamMembersToAdd = new Array();
                    fieldSenseTosterSuccess(data.errorMessage,true);
                    doubleClickFlag=true;
                }else{
                    fieldSenseTosterError(data.errorMessage,true);
                    doubleClickFlag=true;
                    FieldSenseInvalidToken(data.errorMessage);
                }
            }
        });
    }
}

function users(){
    $.ajax({
        type: "GET",
        url: fieldSenseURL+"/user/noTeamMembers",
        contentType: "application/json; charset=utf-8",
        headers:{
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn:false,
        success: function(data) {
            var userData=data.result;
            var user;
            for(var i=0;i<userData.length;i++){
                var id=userData[i].id;
                var firstName = userData[i].firstName;
                var lastName = userData[i].lastName;
                var name = firstName +' '+ lastName;
                if(id!=loginUserId){
                    user={
                        userId: id,
                        userName: name
                    }
                    usersArray.push(user);
                }
            }
        }
    });
}

function addTeamMembersTemplateIn(){
    $('#id_addTeamMemberIntemplate').html('');
    var addMemberTemplate='';
    addMemberTemplate+='<div id="responsive" class="modal fade cls_addTeamMemClose" tabindex="-1" aria-hidden="true">';
    addMemberTemplate+='<div class="modal-dialog modal-wide">';
    addMemberTemplate+='<div class="modal-content">';
    addMemberTemplate+='<div class="modal-header">';
    addMemberTemplate+='<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
    addMemberTemplate+='<h4 class="modal-title">Add Member</h4>';
    addMemberTemplate+='</div>';
    addMemberTemplate+='<div class="modal-body">';
    addMemberTemplate+='<form class="form-horizontal" role="form">';
    addMemberTemplate+='<div class="form-body">';
    addMemberTemplate+='<div class="form-group">';
    addMemberTemplate+='<label class="col-md-6 control-label">Member Name</label>';
    addMemberTemplate+='<div class="col-md-6 input-large">';
    addMemberTemplate+='<select class="form-control" id="id_teamMemberSelects">';
    addMemberTemplate+='<option>Select Member</option>';
    for(var i=0;i< usersArray.length;i++){
        var el= usersArray[i];
        addMemberTemplate+='<option value="'+el.userId+'">'+el.userName+'</option>';
    }
    addMemberTemplate+='</select>';
    addMemberTemplate+='</div>';
    addMemberTemplate+='</div>';
    addMemberTemplate+='<div class="form-group">';
    addMemberTemplate+='<label class="col-md-6 control-label">Member Type</label>';
    addMemberTemplate+='<div class="col-md-6 input-large">';
    addMemberTemplate+='<select class="form-control" id="id_memberType">';
    addMemberTemplate+='<option>Select Type</option>';
    addMemberTemplate+='<option>Team Member</option>';
    addMemberTemplate+='</select>';
    addMemberTemplate+='</div>';
    addMemberTemplate+='</div>';
    addMemberTemplate+='</div>';
    addMemberTemplate+='</form>';
    addMemberTemplate+='</div>';
    addMemberTemplate+='<div class="modal-footer">';
    addMemberTemplate+='<button type="submit" class="btn btn-info" onclick="addMember()">Save</button>';
    addMemberTemplate+='<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
    addMemberTemplate+='</div>';
    addMemberTemplate+='</div>';
    addMemberTemplate+='</div>';
    addMemberTemplate+='</div>';
    $('#id_addTeamMemberIntemplate').html(addMemberTemplate);
}

function listView(){
    var addTemplate='';
    addTemplate+='<div class="portlet">';
    addTemplate+='<div class="portlet-title">';
    addTemplate+='<div class="caption">';
    addTemplate+='<i class="fa fa-list"></i>Team Members';
    addTemplate+='</div>';
    addTemplate+='<div class="actions">';
    addTemplate+='<a data-toggle="modal" href="#responsive" class="btn btn-info" onclick="addTeamMembersTemplateIn()"><i class="fa fa-pencil"></i> Add</a>';
    addTemplate+='</div>';
    addTemplate+='</div>';
    addTemplate+='<div class="portlet-body">';
    addTemplate+='<table class="table table-striped table-bordered table-hover" id="sample_2">';
    addTemplate+='<thead>';
    addTemplate+='<tr>';
    addTemplate+='<th>';
    addTemplate+='Member Name';
    addTemplate+='</th>';
    addTemplate+='<th>';
    addTemplate+='Member Type';
    addTemplate+='</th>';
    addTemplate+='<th>';
    addTemplate+='</th>';
    addTemplate+='</tr>';
    addTemplate+='</thead>';
    addTemplate+='<tbody id="id_teamMemberAdd">';
    addTemplate+='</tbody>';
    addTemplate+='</table>';
    addTemplate+='</div>';
    addTemplate+='</div>';
    $('#id_teamMAdd').html(addTemplate);
}

function addMember(){
    var addTemplate='';
    var indexOfMemberName = document.getElementById("id_teamMemberSelects");
    var teamMemberName =indexOfMemberName.options[indexOfMemberName.selectedIndex].text;
    var teamMemberId =indexOfMemberName.options[indexOfMemberName.selectedIndex].value;
    var indexOfMemberType = document.getElementById("id_memberType");
    var teamMemberType =indexOfMemberType.options[indexOfMemberType.selectedIndex].text;
    if(teamMemberName==='Select Member'){
        fieldSenseTosterError("Please select proper member",true);
        return false;
    }
    if(teamMemberType==='Select Type'){
        fieldSenseTosterError("Please select proper member type",true);
        return false;
    }
    for(var i=0;i<teamMembersToAdd.length;i++){
        var e= teamMembersToAdd[i].memberId;
        if(teamMemberId==e){
            return false;
        }
    }
    var member={
        memberId: teamMemberId,
        memberName: teamMemberName,
        memberType: teamMemberType
    }
    teamMembersToAdd.push(member);
    for(var j=0;j<usersArray.length;j++){
        var el= usersArray[j];
        if(teamMemberId==el.userId){
            var b=usersArray.splice(j,1);
            break;
        }
    }
    addTemplate+='<tr id="delete'+teamMemberId+'">';
    addTemplate+='<td>';
    addTemplate+='<a data-toggle="modal" href="#responsive1" id="'+teamMemberId+'" onclick="viewMember('+teamMemberId+')">'+teamMemberName+'</a>'
    addTemplate+='</td>';
    addTemplate+='<td id="teamMemberType'+teamMemberId+'">'+teamMemberType+'</td>';
    addTemplate+='<td>';
    addTemplate+='<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Edit" data-toggle="modal" href="#responsive2" onclick="editMemberTemplateA(\''+teamMemberId+'\')"><i class="fa fa-edit"></i></button>';
    addTemplate+='<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Delete" onclick="deleteMembers('+teamMemberId+')"><i class="fa fa-trash-o"></i></button>';
    addTemplate+='</td>';
    addTemplate+='</tr>';
    $('#id_teamMemberAdd').append(addTemplate);
    $(".cls_addTeamMemClose").modal('hide');
    jQuery('.tooltips').tooltip();

}

function viewMember(teamMemberId){
    for(var i=0;i<teamMembersToAdd.length;i++){
        var e= teamMembersToAdd[i];
        if(teamMemberId==e.memberId){
            $('#id_teamMemberView').html('');
            var viewMemberTemplate='';
            viewMemberTemplate+='<div id="responsive1" class="modal fade" tabindex="-1" data-width="760">';
            viewMemberTemplate+='<div class="modal-header">';
            viewMemberTemplate+='<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
            viewMemberTemplate+='<h4 class="modal-title">View Member Details</h4>';
            viewMemberTemplate+='</div>';
            viewMemberTemplate+='<div class="modal-body">';
            viewMemberTemplate+='<form class="form-horizontal" role="form">';
            viewMemberTemplate+='<div class="form-body">';
            viewMemberTemplate+='<div class="form-group">';
            viewMemberTemplate+='<label class="col-md-6 control-label">Member Name</label>';
            viewMemberTemplate+='<label class="col-md-6 label2">'+e.memberName +'</label>';
            viewMemberTemplate+='</div>';
            viewMemberTemplate+='<div class="form-group">';
            viewMemberTemplate+='<label class="col-md-6 control-label">Member Type</label>';
            viewMemberTemplate+='<label class="col-md-6 label2">'+e.memberType +'</label>';
            viewMemberTemplate+='</div>';
            viewMemberTemplate+='</div>';
            viewMemberTemplate+='</form>';
            viewMemberTemplate+='</div>';
            viewMemberTemplate+='<div class="modal-footer">';
            viewMemberTemplate+='<button type="button" data-dismiss="modal" class="btn btn-default">Close</button>';
            viewMemberTemplate+='</div>';
            viewMemberTemplate+='</div>';
        }
    }
    $('#id_teamMemberView').append(viewMemberTemplate);
}

function editMemberTemplateA(teamMemberId){
    $('#id_memberEdit').html('');
    for(var i=0;i<teamMembersToAdd.length;i++){
        var e= teamMembersToAdd[i];
        if(teamMemberId==e.memberId){
            var editMember='';

            editMember+='<div id="responsive2" class="modal fade cls_editTeamMemClose" tabindex="-1" aria-hidden="true">';
            editMember+='<div class="modal-dialog modal-wide">';
            editMember+='<div class="modal-content">';
            editMember+='<div class="modal-header">';
            editMember+='<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
            editMember+='<h4 class="modal-title">Edit Member Details</h4>';
            editMember+='</div>';
            editMember+='<div class="modal-body">';
            editMember+='<form class="form-horizontal" role="form">';
            editMember+='<div class="form-body">';
            editMember+='<div class="form-group">';
            editMember+='<label class="col-md-6 control-label">Member Name</label>';
            editMember+='<div class="col-md-6 input-large">';
            editMember+='<input type="name" class="form-control" placeholder="'+e.memberName+'" disabled>';
            editMember+='</div>';
            editMember+='</div>';
            editMember+='<div class="form-group">';
            editMember+='<label class="col-md-6 control-label">Member Type</label>';
            editMember+='<div class="col-md-6 input-large">';
            editMember+='<select class="form-control" id="id_mType">';
            editMember+='<option>Select Type</option>';
            if(e.memberType=="Team Member"){
                editMember+='<option selected>Team Member</option>';
            }
            editMember+='</select>';
            editMember+='</div>';
            editMember+='</div>';
            editMember+='</div>';
            editMember+='</form>';
            editMember+='</div>';
            editMember+='<div class="modal-footer">';
            editMember+='<button type="submit" class="btn btn-info" onclick="editMember(\''+teamMemberId+'\',\''+e.memberName+'\')">Save</button>';
            editMember+='<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
            editMember+='</div></div>';
            editMember+='</div></div>';
        }
    }
    $('#id_memberEdit').append(editMember);
}
function editMember(teamMemberId,memberName){
    var memberTypesIndex=document.getElementById("id_mType");
    var memberTypes =memberTypesIndex.options[memberTypesIndex.selectedIndex].text;
    for(var i=0;i<teamMembersToAdd.length;i++){
        var e= teamMembersToAdd[i];
        if(teamMemberId==e.memberId){
            var b= teamMembersToAdd.splice(i,1);
            var member={
                memberId: teamMemberId,
                memberName: memberName,
                memberType: memberTypes
            }
            teamMembersToAdd.push(member);
            break;
        }
    }
    $('#teamMemberType'+teamMemberId+'').html('');
    var type= memberTypes;
    $('#teamMemberType'+teamMemberId+'').append(type);
    $('.cls_editTeamMemClose').modal('hide');
}

function deleteMembers(teamMemberId){
    for(var i=0;i<teamMembersToAdd.length;i++){
        var e= teamMembersToAdd[i];
        if(teamMemberId==e.memberId){
            var user={
                userId: e.memberId,
                userName: e.memberName
            }
            usersArray.push(user);
            var b= teamMembersToAdd.splice(i,1);
        }
    }
    $('#delete'+teamMemberId+'').hide();
}

function addTeamTemplate(){
    $.ajax({
        type: "GET",
        url: fieldSenseURL+"/user/teamLeaders",
        contentType: "application/json; charset=utf-8",
        headers:{
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn:true,
        success: function(data) {
            var teamLeaders=data.result;
            var addTemplate='';
            addTemplate+='<div class="page-content-wrapper">';
            addTemplate+='<div class="page-content">';
            addTemplate+='<div class="row">';
            addTemplate+='<div class="col-md-12">';
            addTemplate+='<ul class="page-breadcrumb breadcrumb">';
            addTemplate+='<li>';
            addTemplate+='<i class="fa fa-group"></i>';
            addTemplate+='<a href="#" onclick="addTeamTemplate()">Add Team</a>';
            addTemplate+='</ul>';
            addTemplate+='</div>';
            addTemplate+='</div>';
            addTemplate+='<div class="row">';
            addTemplate+='<div class="col-md-12">';
            addTemplate+='<form action="#" class="form-horizontal">';
            addTemplate+='<div class="form-body">';
            addTemplate+='<div class="form-group">';
            addTemplate+='<label class="col-md-3 control-label">Team Name</label>';
            addTemplate+='<div class="col-md-4">';
            addTemplate+='<input type="text" class="form-control" placeholder="Enter text" id="id_teamName">';
            addTemplate+='</div>';
            addTemplate+='</div>';
            addTemplate+='<div class="form-group">';
            addTemplate+='<label class="col-md-3 control-label">Description</label>';
            addTemplate+='<div class="col-md-4">';
            addTemplate+='<textarea class="form-control" rows="3" placeholder="Enter text" id="id_description"></textarea>';
            addTemplate+='</div>';
            addTemplate+='</div>';
            addTemplate+='<div class="form-group">';
            addTemplate+='<label class="col-md-3 control-label">Team Leader</label>';
            addTemplate+='<div class="col-md-4">';
            addTemplate+='<select class="form-control" tabindex="4" id="id_teamLeader">';
            addTemplate+='<option value="0">Select</option>';
            for(var i=0 ;i<teamLeaders.length;i++){
                var teamLeaderId=teamLeaders[i].id;
                var teamLeaderName=teamLeaders[i].firstName+' '+teamLeaders[i].lastName;
                addTemplate+='<option value="'+teamLeaderId+'">'+teamLeaderName+'</option>';
            }
            addTemplate+='</select>';
            addTemplate+='</div>';
            addTemplate+='</div>';
            addTemplate+='</div>';
            addTemplate+='</form>';
            addTemplate+='<span id="id_teamMAdd"></span>';
            addTemplate+='<button type="submit" class="btn btn-info" onclick="addTeam()">Save</button>&nbsp;';
            addTemplate+='<button type="button" class="btn btn-default" onclick="adminTeams();">Cancel</button>';
            addTemplate+='</div>';
            addTemplate+='</div>';
            addTemplate+='</div>';
            addTemplate+='</div>';
            addTemplate+='<span id="id_addTeamMemberIntemplate"></span>';
            addTemplate+='<span id="id_teamMemberView"></span>';
            addTemplate+='<span id="id_memberEdit"></span>';
            $('#id_mid').html(addTemplate);
            listView();
            users();
            usersArray = new Array();
            teamMembersToAdd = new Array();
        }
    });
}
function updateTeamHtmlTemplate(teamId){
    $.ajax({
        type: "GET",
        url: fieldSenseURL+"/team/"+teamId,
        contentType: "application/json; charset=utf-8",
        headers:{
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn:true,
        success: function(data) {
            if(data.errorCode=='0000'){
                $.ajax({
                    type: "GET",
                    url: fieldSenseURL+"/user/teamLeaders",
                    contentType: "application/json; charset=utf-8",
                    headers:{
                        "userToken": userToken
                    },
                    crossDomain: false,
                    cache: false,
                    dataType: 'json',
                    asyn:true,
                    success: function(teamLeadData) {
                        var teamLeaders=teamLeadData.result;
                        
                        var teamDeatils=data.result;
                        var teamName=teamDeatils.teamName;
                        var teamDescription=teamDeatils.description;
                        var teamLeadId=teamDeatils.ownerId.id;
                        var updateTeamHtmlTemplate='';
                        updateTeamHtmlTemplate+='<div class="page-content-wrapper">';
                        updateTeamHtmlTemplate+='<div class="page-content">';
                        updateTeamHtmlTemplate+='<div class="row">';
                        updateTeamHtmlTemplate+='<div class="col-md-12">';
                        updateTeamHtmlTemplate+='<ul class="page-breadcrumb breadcrumb">';
                        updateTeamHtmlTemplate+='<li>';
                        updateTeamHtmlTemplate+='<i class="fa fa-group"></i>';
                        updateTeamHtmlTemplate+='<a href="#">Update Team</a>';
                        updateTeamHtmlTemplate+='<!--i class="fa fa-angle-right"></i>';
                        updateTeamHtmlTemplate+='</li>';
                        updateTeamHtmlTemplate+='<li>';
                        updateTeamHtmlTemplate+='<a href="#">Member 1</a>';
                        updateTeamHtmlTemplate+='</li-->';
                        updateTeamHtmlTemplate+='</ul>';
                        updateTeamHtmlTemplate+='</div>';
                        updateTeamHtmlTemplate+='</div>';
                        updateTeamHtmlTemplate+='<div class="row">';
                        updateTeamHtmlTemplate+='<div class="col-md-12">';
                        updateTeamHtmlTemplate+='<form action="#" class="form-horizontal">';
                        updateTeamHtmlTemplate+='<div class="form-body">';
                        updateTeamHtmlTemplate+='<div class="form-group">';
                        updateTeamHtmlTemplate+='<label class="col-md-3 control-label">Team Name</label>';
                        updateTeamHtmlTemplate+='<div class="col-md-4">';
                        updateTeamHtmlTemplate+='<input type="text" class="form-control" placeholder="Enter text" value="'+teamName+'" id="id_teamNameedit">';
                        updateTeamHtmlTemplate+='</div>';
                        updateTeamHtmlTemplate+='</div>';
                        updateTeamHtmlTemplate+='<div class="form-group">';
                        updateTeamHtmlTemplate+='<label class="col-md-3 control-label">Description</label>';
                        updateTeamHtmlTemplate+='<div class="col-md-4">';
                        updateTeamHtmlTemplate+='<textarea class="form-control" rows="3" placeholder="Enter text" id="id_teamdescriptionedit">'+teamDescription+'</textarea>';
                        updateTeamHtmlTemplate+='</div>';
                        updateTeamHtmlTemplate+='</div>';
                        updateTeamHtmlTemplate+='<div class="form-group">';
                        updateTeamHtmlTemplate+='<label class="col-md-3 control-label">Team Leader</label>';
                        updateTeamHtmlTemplate+='<div class="col-md-4">';
                        updateTeamHtmlTemplate+='<select class="form-control" tabindex="4" id="id_teamLeader">';
                        updateTeamHtmlTemplate+='<option value="0">Select</option>';
                        for(var i=0 ;i<teamLeaders.length;i++){
                            var selectTeamLeaderId=teamLeaders[i].id;
                            var teamLeaderName=teamLeaders[i].firstName+' '+teamLeaders[i].lastName;
                            if(selectTeamLeaderId==teamLeadId){
                                updateTeamHtmlTemplate+='<option value="'+selectTeamLeaderId+'" selected>'+teamLeaderName+'</option>';
                            }else{
                                updateTeamHtmlTemplate+='<option value="'+selectTeamLeaderId+'">'+teamLeaderName+'</option>';
                            }
                            
                        }
                        updateTeamHtmlTemplate+='</select>';
                        updateTeamHtmlTemplate+='</div>';
                        updateTeamHtmlTemplate+='</div>';
                        updateTeamHtmlTemplate+='</div>';
                        updateTeamHtmlTemplate+='</form>';
                        updateTeamHtmlTemplate+='<span id="id_teamMembersdatatable">';
                        updateTeamHtmlTemplate+='</span>';
                        updateTeamHtmlTemplate+='<button type="submit" class="btn btn-info" onClick="updateTeam(\''+teamId+'\');return false;">Save</button>';
                        updateTeamHtmlTemplate+='<button type="button" class="btn btn-default" style="margin-left: 3px;" onclick="adminTeams();">Cancel</button>';
                        updateTeamHtmlTemplate+='<button type="button" class="col-md-0 btn btn-danger" style="margin-left: 800px;" onClick="deleteTeam(\''+teamId+'\');return false;">Delete</button>';
                        updateTeamHtmlTemplate+='</div>';
                        updateTeamHtmlTemplate+='</div>';
                        updateTeamHtmlTemplate+='<div id="responsive" class="modal fade cls_addteamMember" tabindex="-1" aria-hidden="true"></div>';
                        updateTeamHtmlTemplate+='<span id="id_viewTeamMember"></span>';
                        updateTeamHtmlTemplate+='<div id="responsive2" class="modal fade cls_memberEdit" tabindex="-1" aria-hidden="true">';
                        updateTeamHtmlTemplate+='</div>';
                        updateTeamHtmlTemplate+='</div>';
                        $('#id_mid').html(updateTeamHtmlTemplate);
                        teamMembersForUpdateTeam(teamId);
                        teamMembersUpdateArray=new Array();
                        usersArray = new Array();
                        users();
                    }
                });
            }
        }
    });
}
function deleteTeam(teamId){
    $.ajax({
        type: "DELETE",
        url: fieldSenseURL+"/team/"+teamId,
        contentType: "application/json; charset=utf-8",
        headers:{
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn:true,
        success: function(data) {
            if(data.errorCode=='0000'){
                //                userTeams();
                adminTeams();
            }else{
                fieldSenseTosterError(data.errorMessage,true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}
function activate(userId){
    for(var i=0;i<teamMembersUpdateArray.length;i++){
        var e= teamMembersUpdateArray[i];
        if(userId==e.memberId){
            e['memberIsActive']=1;
            if(e.type!==1){
                e['type']=2;
            }
            break;
        }
    }
    var active='';
    if(e.memberIsActive==true){
        active+='<input type="checkbox" checked disabled/>';
    }
    else{
        active+='<input type="checkbox" disabled/>';
    }
    $('#teamMemberstatus'+userId+'').html(active);
    $(".cls_memberEdit").modal('hide');
    $('#id_deact_act'+userId+'').html('<button type="button" class="btn btn-danger" style="margin-right:483px;" onclick="deactivate('+userId+')">Deactive</button>');
}
function deactivate(userId){
    for(var i=0;i<teamMembersUpdateArray.length;i++){
        var e= teamMembersUpdateArray[i];
        if(userId==e.memberId){
            e['memberIsActive']=0;
            if(e.type!==1){
                e['type']=2;
            }
            break;
        }
    }
    var active='';
    if(e.memberIsActive==true){
        active+='<input type="checkbox" checked disabled/>';
    }
    else{
        active+='<input type="checkbox" disabled/>';
    }
    $('#teamMemberstatus'+userId+'').html(active);
    $(".cls_memberEdit").modal('hide');
    $('#id_deact_act'+userId+'').html('<button type="button" class="btn btn-success" style="margin-right:502px;" onclick="activate('+userId+')">Active</button>');
}
