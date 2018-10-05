/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * @author Ramesh
 * @date 12-03-2014
 */
var firstTeamId = 0;
var firstTeamName = '';
var doubleClickFlag = true;
var customer = [];
var teamMembersDatatableIndex = 0;
var teamMembersUpdateArray = new Array();
var usersArray = new Array();
var teamMembersToAdd = new Array();
var map;
var markers = [];
var expensearray=[];
var userActivityMarkersFlag = false;
var intervalMapMarkers;
var isAssigneeIsImmidiateSubOrdinate = false;
var id_member_active = 0;
var isTravelRouteMode=false;
var isRecurring = 1;    // delete popup bug solve -pending

var intervalIndex = window.setInterval(function () {
    var userToken2 = fieldSenseGetCookie("userToken");
    var specificUserId = fieldSenseGetCookie("specificUserId");
    var specificUserName = fieldSenseGetCookie("specificUserName");
    var specificUserTeamId = fieldSenseGetCookie("specificUserTeamId");
    try {
        if (userToken2.toString() == "undefined") {
            window.location.href = "login.html";
        }
        else if (loginUserId != 0) {
            loggedinUserImageData();
            loggedinUserData();
          //  customerList();
            try {
                if (specificUserId.toString() == "undefined") {
                    userTopLevelHierarchy(loginUserName, loginUserId, 0, 0, firstTeamId);
                }
                else {
                    userTopLevelHierarchy(loginUserName, loginUserId, specificUserId, specificUserName, specificUserTeamId);
                    deleteCookie("specificUserId");
                    deleteCookie("specificUserName");
                    deleteCookie("specificUserTeamId");
                }
            } catch (exception) {
                userTopLevelHierarchy(loginUserName, loginUserId, 0, 0, firstTeamId);
            }
            window.clearTimeout(intervalIndex);
        }
    }
    catch (err) {
        window.location.href = "login.html";
    }
}, 1000);
function userTopLevelHierarchy(hirarchyName, hirarchyId, specificUserId, specificUserName, specificUserTeamId) {
    showTeamMapHtmlTemplate(specificUserTeamId, hirarchyId, hirarchyName);
    window.clearTimeout(intervalMapMarkers);
    var dateOnly = new Date();
    dateOnly = convertLocalDateToServerDate(dateOnly.getFullYear(), dateOnly.getMonth(), dateOnly.getDate(), dateOnly.getHours(), dateOnly.getMinutes());
    var month = dateOnly.getMonth() + 1;
    var date = dateOnly.getDate();
    if (month < 10) {
        month = '0' + month;
    }
    if (date < 10) {
        date = '0' + date;
    }
    dateOnly = dateOnly.getFullYear() + "-" + month + "-" + date;
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/team/userPositions/" + hirarchyId + "/" + dateOnly,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            initializeMap(loginUserLatitude, loginUserLangitude);
            var hierarchyData = data.result;
            var userTopLevelHierarchyHtml = '';
            userTopLevelHierarchyHtml += '<ul class="page-sidebar-menu1">';
            userTopLevelHierarchyHtml += '<li class="start active">';
            userTopLevelHierarchyHtml += '<span class="membername">MY TEAM</span>';
            userTopLevelHierarchyHtml += '</li>';
            userTopLevelHierarchyHtml += '</ul>';
            userTopLevelHierarchyHtml += '<ul class="page-sidebar-menu">';
            userTopLevelHierarchyHtml += '<li class="start active">';
            userTopLevelHierarchyHtml += '<ul class="sub-menu">';
            for (var i = 0; i < hierarchyData.length; i++) {
                var memberId = hierarchyData[i].user.id;
                var teamId = hierarchyData[i].teamId;
                var userName = hierarchyData[i].user.firstName + " " + hierarchyData[i].user.lastName;
                var isUserOnline = hierarchyData[i].isOnline;
                var canTakeCalls = hierarchyData[i].canTakeCalls;
                var inMeeting = hierarchyData[i].inMeeting;
                var latitude = hierarchyData[i].user.latitude;
                var langitude = hierarchyData[i].user.langitude;
                var latlng = new google.maps.LatLng(latitude, langitude);
                var userTime = hierarchyData[i].user.lastKnownLocationTime;
                var officeLatitude = hierarchyData[i].user.officeLocation.latitude;
                var officeLongitude = hierarchyData[i].user.officeLocation.langitude;
                var homeeLatitude = hierarchyData[i].user.homeLocation.latitude;
                var homeLongitude = hierarchyData[i].user.homeLocation.langitude;
                var hasSubordinate = hierarchyData[i].hasSubordinate;
                var userLocationText = '';
                if (latitude == officeLatitude && langitude == officeLongitude) {
                    userLocationText = ' at office';
                }
                else if (latitude == homeeLatitude && langitude == homeLongitude) {
                    userLocationText = ' at home';
                }
                var jsDate = convertServerDateToLocalDate((userTime.year) + 1900, userTime.month, userTime.date, userTime.hours, userTime.minutes);
                var today = new Date();
                var dateFromJson = jsDate.getDate() + '/' + jsDate.getMonth() + '/' + jsDate.getFullYear();
                var dateToday = today.getDate() + '/' + today.getMonth() + '/' + today.getFullYear();
                var hours = jsDate.getHours();
                var minutes = jsDate.getMinutes();
                var timeToShow = ' am';
                if (hours < 10) {
                    hours = '0' + hours;
                }
                else {
                    if (hours > 12) {
                        hours = hours - 12;
                        if (hours < 10) {
                            hours = '0' + hours;
                        }
                        timeToShow = ' pm';
                    } else if (hours == 12) {
                        timeToShow = ' pm';
                    }
                }
                if (minutes < 10) {
                    minutes = '0' + minutes;
                }
                timeToShow = hours + ':' + minutes + '' + timeToShow;
                var timeString = '';
                if (dateFromJson == dateToday) {
                    timeString = 'Today at ' + timeToShow;
                }
                else {
                    timeString = fieldSenseseMesageTimeFormateForJSDate(jsDate);
                }
                var infoWindowContent = '<span style="color:#000"><b>' + userName + '</b> ' + userLocationText + '<br>' + timeString + '</span>';
                addMapMarker(latlng, memberId, infoWindowContent, hirarchyId);
                if (memberId == hirarchyId) {
                    map.panTo(latlng);
                }
                if (memberId != loginUserId) {
                    var cls_usr_status_in_meet = 'inmeet';
                    var cls_usr_status_can_take_calls;
                    var cls_usr_status_online_offline;
                    if (canTakeCalls) {
                        cls_usr_status_can_take_calls = 'P_online';
                    } else {
                        cls_usr_status_can_take_calls = 'P_offline';
                    }
                    if (isUserOnline) {
                        cls_usr_status_online_offline = 'online';
                    } else {
                        cls_usr_status_online_offline = 'offline';
                    }
                    userTopLevelHierarchyHtml += '<li id="id_memberOpen' + memberId + '">';
                    userTopLevelHierarchyHtml += '<a class="showSingle" target="2" onClick="userLevelHirarchy(\'' + userName + '\',\'' + memberId + '\',\'' + teamId + '\')">';
                    if (hasSubordinate) {
                        userTopLevelHierarchyHtml += '<span class="arrow" id="id_memberSelection' + memberId + '"></span>';
                    } else {
                        userTopLevelHierarchyHtml += '<span class="barrow" id="id_memberSelection' + memberId + '"></span>';
                    }
                    userTopLevelHierarchyHtml += '<span class="membername">' + userName + '</span>';
                    userTopLevelHierarchyHtml += '<span id="id_user_status' + memberId + '" style="width: 37px;">';
                    userTopLevelHierarchyHtml += '<span class="' + cls_usr_status_can_take_calls + '"></span>';
                    if (inMeeting) {
                        userTopLevelHierarchyHtml += '<span class="' + cls_usr_status_in_meet + '"></span>';
                    }
                    else {
                        userTopLevelHierarchyHtml += '<span class="' + cls_usr_status_online_offline + '"></span>';
                    }
                    userTopLevelHierarchyHtml += '</span>';
                    userTopLevelHierarchyHtml += '</a>';
                    userTopLevelHierarchyHtml += '<ul class="sub-menu" style="display:none" id="id_Hierachy_' + memberId + '">';
                    userTopLevelHierarchyHtml += '</ul>';
                    userTopLevelHierarchyHtml += '</li>';
                }
            }
            userTopLevelHierarchyHtml += '</ul>';
            userTopLevelHierarchyHtml += '</li>';
            userTopLevelHierarchyHtml += '</ul>';
            $('#id_leftaside').html(userTopLevelHierarchyHtml);
	
            if (specificUserId == 0) {

                userActivitiesTemplate(specificUserTeamId, loginUserId, loginUserName);
                intervalMapMarkers = window.setInterval(function () {
                    realTimeMapMarkers(hirarchyId);
                }, 1800000);
            } else {

                userLevelHirarchy(specificUserName, specificUserId, specificUserTeamId);
                $("#id_memberOpen" + specificUserId).addClass("open");
                $("#id_memberSelection" + specificUserId).addClass("open");
                $("#id_Hierachy_" + specificUserId).css("display", "block");
            }
		App.callHandleFixedSidebar(); //Slim scroll issue
        }
    });
}
function userLevelHirarchy(hierarchyName, hierarchyId, teamId) {
    /*if($(".toast-message")!=undefined && $(".toast-message").html()!=undefined){
        if($(".toast-message").html().trim()=="Exit travelled route mode."){
          //  $(".cls_close_route").click();
          $("#toast-container").remove();
        userTopLevelHierarchy(loginUserName, loginUserId,hierarchyId,hierarchyName,teamId);
        return;
        }
    }*/    
    // if user in travel route mode
    if(isTravelRouteMode==true){
        isTravelRouteMode=false;
        $("#toast-container").remove();
        userTopLevelHierarchy(loginUserName, loginUserId,hierarchyId,hierarchyName,teamId);
        return;
    }
    //end of user in travel route mode
    userActivitiesTemplate(teamId, hierarchyId, hierarchyName);
   if (!$('#id_memberSelection' + hierarchyId).hasClass('open')) {
        clearAllMapMarkers();
        window.clearTimeout(intervalMapMarkers);
        var hierarchyidforRefreshHtml = '<a href="#" onClick="refreshRouteMap(\'' + teamId + '\',\'' + hierarchyId + '\',\'' + hierarchyName + '\')"><img src="assets/img/refresh.png" alt="Refresh Map"></a>';
        $('#id_hierarchyidforrefresh').html(hierarchyidforRefreshHtml);
        var dateOnly = new Date();
        dateOnly = convertLocalDateToServerDate(dateOnly.getFullYear(), dateOnly.getMonth(), dateOnly.getDate(), dateOnly.getHours(), dateOnly.getMinutes());
        var month = dateOnly.getMonth() + 1;
        var date = dateOnly.getDate();
        if (month < 10) {
            month = '0' + month;
        }
        if (date < 10) {
            date = '0' + date;
        }
        dateOnly = dateOnly.getFullYear() + "-" + month + "-" + date;
        $.ajax({
            type: "GET",
            url: fieldSenseURL + "/team/userPositions/" + hierarchyId + "/" + dateOnly,
            contentType: "application/json; charset=utf-8",
            headers: {
                "userToken": userToken
            },
            crossDomain: false,
            cache: false,
            dataType: 'json',
            asyn: true,
            success: function (data) {
                var hierarchyData = data.result;
                var userLevelHirarchyHtml = '';
                var id = '#id_memberOpen' + id_member_active + ' #id_memberOpen' + hierarchyId;
                if (!$(id).length) {
                    $('#id_memberOpen' + id_member_active).removeClass("active");
                    id_member_active = hierarchyId;
                    $('#id_memberOpen' + hierarchyId).addClass("active");
                }
                if (hierarchyData.length > 1) {
                    $('#id_memberSelection' + hierarchyId).addClass("arrow open");
                    $('#id_Hierachy_' + hierarchyId).css("display", "block");
                } else {
                    $('#id_Hierachy_' + hierarchyId).css("display", "none");
                }
                for (var i = 0; i < hierarchyData.length; i++) {
                    var teamId = hierarchyData[i].teamId;
                    var userName = hierarchyData[i].user.firstName + " " + hierarchyData[i].user.lastName;
                    var isUserOnline = hierarchyData[i].isOnline;
                    var canTakeCalls = hierarchyData[i].canTakeCalls;
                    var inMeeting = hierarchyData[i].inMeeting;
                    var latitude = hierarchyData[i].user.latitude;
                    var langitude = hierarchyData[i].user.langitude;
                    var latlng = new google.maps.LatLng(latitude, langitude);
                    var memberId = hierarchyData[i].user.id;
                    var userTime = hierarchyData[i].user.lastKnownLocationTime;
                    var officeLatitude = hierarchyData[i].user.officeLocation.latitude;
                    var officeLongitude = hierarchyData[i].user.officeLocation.langitude;
                    var homeeLatitude = hierarchyData[i].user.homeLocation.latitude;
                    var homeLongitude = hierarchyData[i].user.homeLocation.langitude;
                    var hasSubordinate = hierarchyData[i].hasSubordinate;
                    var userLocationText = '';
                    if (latitude == officeLatitude && langitude == officeLongitude) {
                        userLocationText = ' at office';
                    } else if (latitude == homeeLatitude && langitude == homeLongitude) {
                        userLocationText = ' at home';
                    }
                    var jsDate = convertServerDateToLocalDate((userTime.year) + 1900, userTime.month, userTime.date, userTime.hours, userTime.minutes);
                    var today = new Date();
                    var dateFromJson = jsDate.getDate() + '/' + jsDate.getMonth() + '/' + jsDate.getFullYear();
                    var dateToday = today.getDate() + '/' + today.getMonth() + '/' + today.getFullYear();
                    var hours = jsDate.getHours();
                    var minutes = jsDate.getMinutes();
                    var timeToShow = ' am';
                    if (hours < 10) {
                        hours = '0' + hours;
                    }
                    else {
                        if (hours > 12) {
                            hours = hours - 12;
                            if (hours < 10) {
                                hours = '0' + hours;
                            }
                            timeToShow = ' pm';
                        }
                        else if (hours == 12) {
                            timeToShow = ' pm';
                        }
                    }
                    if (minutes < 10) {
                        minutes = '0' + minutes;
                    }
                    timeToShow = hours + ':' + minutes + '' + timeToShow;
                    var timeString = '';
                    if (dateFromJson == dateToday) {
                        timeString = 'Today at ' + timeToShow;
                    }
                    else {
                        timeString = fieldSenseseMesageTimeFormateForJSDate(jsDate);
                    }
                    var infoWindowContent = '<span style="color:#000"><b>' + userName + '</b> ' + userLocationText + '<br>' + timeString + '</span>';
			
		if(latitude!=0 && langitude!=0){
                    addMapMarker(latlng, memberId, infoWindowContent, hierarchyId);
                    if (memberId == hierarchyId) {
			
                        map.panTo(latlng);
                    }
		}
                    if (memberId != hierarchyId) {
                        var cls_usr_status_in_meet = 'inmeet';
                        var cls_usr_status_can_take_calls;
                        var cls_usr_status_online_offline;
                        if (canTakeCalls) {
                            cls_usr_status_can_take_calls = 'P_online';
                        } else {
                            cls_usr_status_can_take_calls = 'P_offline';
                        }
                        if (isUserOnline) {
                            cls_usr_status_online_offline = 'online';
                        } else {
                            cls_usr_status_online_offline = 'offline';
                        }
                        userLevelHirarchyHtml += '<li id="id_memberOpen' + memberId + '">';
                        userLevelHirarchyHtml += '<a class="showSingle" target="2" onClick="userLevelHirarchy(\'' + userName + '\',\'' + memberId + '\',\'' + teamId + '\')">';
                        if (hasSubordinate) {
                            userLevelHirarchyHtml += '<span class="arrow" id="id_memberSelection' + memberId + '"></span>';
                        } else {
                            userLevelHirarchyHtml += '<span class="barrow" id="id_memberSelection' + memberId + '"></span>';
                        }
                        userLevelHirarchyHtml += '<span class="membername">' + userName + '</span>';
                        userLevelHirarchyHtml += '<span id="id_user_status' + memberId + '" style="width: 37px;">';
                        userLevelHirarchyHtml += '<span class="' + cls_usr_status_can_take_calls + '"></span>';
                        if (inMeeting) {
                            userLevelHirarchyHtml += '<span class="' + cls_usr_status_in_meet + '"></span>';
                        } else {
                            userLevelHirarchyHtml += '<span class="' + cls_usr_status_online_offline + '"></span>';
                        }
                        userLevelHirarchyHtml += '</span>';
                        userLevelHirarchyHtml += '</a>';
                        userLevelHirarchyHtml += '<ul class="sub-menu" style="display:none" id="id_Hierachy_' + memberId + '">';
                        userLevelHirarchyHtml += '</ul>';
                        userLevelHirarchyHtml += '</li>';
                    }
                    $('#id_Hierachy_' + hierarchyId).html(userLevelHirarchyHtml);
                    intervalMapMarkers = window.setInterval(function () {
                        realTimeMapMarkers(hierarchyId);
                    }, 1800000);
			App.callHandleFixedSidebar(); //Slim scroll issue
                }
            }
        });
   }
}
function showTeamMapHtmlTemplate(teamId, hierarchyId, hierarchyName) {
    var showTeamHtmlTemplateHtml = '';
    showTeamHtmlTemplateHtml += '<div class="page-content-wrapper">';
    showTeamHtmlTemplateHtml += '<div class="page-content">';
    showTeamHtmlTemplateHtml += '<div id="acti" class="row">';
    showTeamHtmlTemplateHtml += '<div class="col-md-12" style="margin-top:15px !important;">';
    showTeamHtmlTemplateHtml += '<div id="gmap_marker" class="gmaps"></div>';
    showTeamHtmlTemplateHtml += '</div>';
    showTeamHtmlTemplateHtml += '</div>';
    showTeamHtmlTemplateHtml += '<div class="portlet" id="refresh">';
    showTeamHtmlTemplateHtml += '<span id="id_hierarchyidforrefresh"><a href="#" onClick="refreshRouteMap(\'' + teamId + '\',\'' + hierarchyId + '\',\'' + hierarchyName + '\')"><img src="assets/img/refresh.png" alt="Refresh Map"></a></span>';
    showTeamHtmlTemplateHtml += '</div>';
    showTeamHtmlTemplateHtml += '<div class="portlet cls_usractivities" id="MDholder">';
    showTeamHtmlTemplateHtml += '</div>';
    showTeamHtmlTemplateHtml += '<div id="responsive" class="modal fade cls_addActivity" tabindex="-1" role="basic" aria-hidden="true"> ';
    showTeamHtmlTemplateHtml += '</div>';
    showTeamHtmlTemplateHtml += '</div>';
    showTeamHtmlTemplateHtml += '</div>';
    $('#id_mid').html(showTeamHtmlTemplateHtml);
}
function addActivityTemplate(hierarchyId, hierarchyName, isSpecificUserActivity, teamId) {
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/activityPurpose",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            if (data.errorCode == '0000') {
                var purposeData = data.result;
                if (isSpecificUserActivity == 'true') {
                    var addActivityTemplateHtml = '';
                    addActivityTemplateHtml += '<div class="modal-dialog modal-wide">';
                    addActivityTemplateHtml += '<div class="modal-content">';
                    addActivityTemplateHtml += '<div class="modal-header">';
                    addActivityTemplateHtml += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
                    addActivityTemplateHtml += '<h4 class="modal-title">Add Visit</h4>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '<form action="#" class="form-horizontal form-row-seperated">';
                    //                    addActivityTemplateHtml += '<div class="modal-body" id="act">';
                    addActivityTemplateHtml += '<div class="modal-body">';
                    //                    addActivityTemplateHtml += '<div class="scroller2" data-always-visible="1" data-rail-visible1="1">';
                    addActivityTemplateHtml += '<div class="scroller" data-always-visible="1" data-rail-visible1="1" id="id_createactivity">';
                    addActivityTemplateHtml += '<div class="form-body">';
                    addActivityTemplateHtml += '<div class="row">';
                    addActivityTemplateHtml += '<div class="col-md-6 form-group">';
                    addActivityTemplateHtml += '<label class="col-md-5 control-label">Customer Name<span style="color:red"> *</span></label>';
                    addActivityTemplateHtml += '<div class="col-md-7">';
                    addActivityTemplateHtml += '<input type="hidden" id="id_hidden" value="Val"/>';
                    addActivityTemplateHtml += '<input type="Cname" name="currency" class="form-control" id="autocompleteCnm" placeholder="Enter text" autocomplete="off">';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '<div class="col-md-6 form-group">';
                    addActivityTemplateHtml += '<label class="col-md-5 control-label">Contact<span style="color:red"> *</span></label>';
                    addActivityTemplateHtml += '<div class="col-md-7">';
                    addActivityTemplateHtml += '<select class="form-control" id="id_contact">';
                    addActivityTemplateHtml += '</select>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '<div class="row">';
                    addActivityTemplateHtml += '<div class="col-md-6 form-group">';
                    addActivityTemplateHtml += '<label class="col-md-5 control-label">Title</label>';
                    addActivityTemplateHtml += '<div class="col-md-7">';
                    addActivityTemplateHtml += '<input type="activity" class="form-control" placeholder="Enter text" id="id_activity">';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '<div class="col-md-6 form-group">';
                    addActivityTemplateHtml += '<label class="col-md-5 control-label">Start Date</label>';
                    addActivityTemplateHtml += '<div class="col-md-7">';
                    var today = new Date();
                    var dd = today.getDate();
                    var mm = today.getMonth() + 1; //January is 0!

                    var yyyy = today.getFullYear();
                    if (dd < 10) {
                        dd = '0' + dd
                    }
                    if (mm < 10) {
                        mm = '0' + mm
                    }
                    var currentHr = today.getHours();
                    var currentMin = today.getMinutes();
                    var today = dd + '-' + mm + '-' + yyyy;
                    addActivityTemplateHtml += '<div class="input-group date date-picker" data-date-format="dd-mm-yyyy" data-date-start-date="-1m">';
                    addActivityTemplateHtml += '<input type="text" class="form-control" id="id_date" readonly value="' + today + '">';
                    addActivityTemplateHtml += '<span class="input-group-btn">';
                    addActivityTemplateHtml += '<button class="btn btn-default" type="button"><i class="fa fa-calendar"></i></button>';
                    addActivityTemplateHtml += '</span>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '<div class="row">';
                    addActivityTemplateHtml += '<div class="col-md-6 form-group">';
                    addActivityTemplateHtml += '<label class="col-md-5 control-label">Start Time</label>';
                    addActivityTemplateHtml += '<div class="col-md-3">';
                    addActivityTemplateHtml += '<select id="activity_hr"  class="form-control input-xssmall">';
                    addActivityTemplateHtml += '<option value="HH">HH</option>';
                    for (var j = 0; j < 24; j++) {
                        if (j < 10)
                        {
                            if (currentHr == j) {
                                addActivityTemplateHtml += '<option value="' + j + '" selected>0' + j + '</option>';
                            }
                            else {
                                addActivityTemplateHtml += '<option value="' + j + '">0' + j + '</option>';
                            }
                        } else {
                            if (currentHr == j) {
                                addActivityTemplateHtml += '<option value="' + j + '" selected>' + j + '</option>';
                            }
                            else {
                                addActivityTemplateHtml += '<option value="' + j + '">' + j + '</option>';
                            }
                        }
                    }
                    addActivityTemplateHtml += '</select></div>';
                    addActivityTemplateHtml += '<div class="col-md-3">';
                    addActivityTemplateHtml += '<select id="activity_min" class="form-control input-xssmall">';
                    addActivityTemplateHtml += '<option value="MM">MM</option>';
                    for (var k = 0; k < 60; k = k + 1) {
                        if (k < 10)
                        {
                            if (currentMin == k) {
                                addActivityTemplateHtml += '<option value="' + k + '" selected>0' + k + '</option>';
                            } else {
                                addActivityTemplateHtml += '<option value="' + k + '">0' + k + '</option>';
                            }
                        }
                        else {
                            if (currentMin == k) {
                                addActivityTemplateHtml += '<option value="' + k + '"selected>' + k + '</option>';
                            } else {
                                addActivityTemplateHtml += '<option value="' + k + '">' + k + '</option>';
                            }
                        }
                    }
                    addActivityTemplateHtml += '</select>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '<div class="col-md-6 form-group">';
                    addActivityTemplateHtml += '<label class="col-md-5 control-label">End Time</label>';
                    addActivityTemplateHtml += '<div class="col-md-3">';
                    addActivityTemplateHtml += '<select id="activity_endHr" class="form-control input-xssmall">';
                    addActivityTemplateHtml += '<option value="HH">HH</option>';
                    for (var l = 0; l < 24; l++) {
                        if (l < 10)
                        {
                            if (currentHr + 1 == l) {
                                addActivityTemplateHtml += '<option value="' + l + '" selected>0' + l + '</option>';
                            } else {
                                addActivityTemplateHtml += '<option value="' + l + '">0' + l + '</option>';
                            }
                        } else {
                            if (currentHr + 1 == l) {
                                addActivityTemplateHtml += '<option value="' + l + '" selected>' + l + '</option>';
                            } else {
                                addActivityTemplateHtml += '<option value="' + l + '">' + l + '</option>';
                            }
                        }
                    }
                    addActivityTemplateHtml += '</select></div>';
                    addActivityTemplateHtml += '<div class="col-md-3">';
                    addActivityTemplateHtml += '<select id="activity_endMin" class="form-control input-xssmall">';
                    addActivityTemplateHtml += '<option value="MM">MM</option>';
                    for (var m = 0; m < 60; m = m + 1) {
                        if (m < 10)
                        {
                            if (currentMin == m) {
                                addActivityTemplateHtml += '<option value="' + m + '" selected>0' + m + '</option>';
                            } else {
                                addActivityTemplateHtml += '<option value="' + m + '">0' + m + '</option>';
                            }
                        } else {
                            if (currentMin == m) {
                                addActivityTemplateHtml += '<option value="' + m + '" selected>' + m + '</option>';
                            } else {
                                addActivityTemplateHtml += '<option value="' + m + '">' + m + '</option>';
                            }
                        }
                    }
                    addActivityTemplateHtml += '</select>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '<div class="row">';
                    addActivityTemplateHtml += '<div class="col-md-6 form-group">';
                    addActivityTemplateHtml += '<label class="col-md-5 control-label">Purpose</label>';
                    addActivityTemplateHtml += '<div class="col-md-7">';
                    addActivityTemplateHtml += '<select class="form-control" id="id_purpose">';
                    for (var i = 0; i < purposeData.length; i++) {
                        var id = purposeData[i].id;
                        var purpose = purposeData[i].purpose;
                        addActivityTemplateHtml += '<option value="' + id + '">' + purpose + '</option>';
                    }
                    addActivityTemplateHtml += '</select>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    
                    //Added by Jyoti 30-01-2017
                    if(isRecurring == 1){
                    addActivityTemplateHtml += '<div class="row">';
                    addActivityTemplateHtml += '<div class="col-md-6 form-group">';
                    addActivityTemplateHtml += '<label class="col-md-5 control-label">Recurring Till Date</label>';
                    addActivityTemplateHtml += '<div class="col-md-7">';                    
                    addActivityTemplateHtml += '<div class="input-group date date-picker" data-date-format="dd-mm-yyyy" data-date-start-date="-1m">';
                    addActivityTemplateHtml += '<input type="text" class="form-control" id="end_date" readonly value="' + today + '">';
                    addActivityTemplateHtml += '<span class="input-group-btn">';
                    addActivityTemplateHtml += '<button class="btn btn-default" type="button"><i class="fa fa-calendar"></i></button>';
                    addActivityTemplateHtml += '</span>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    
                    addActivityTemplateHtml += '<div class="row">';
                    addActivityTemplateHtml += '<div class="col-md-6 form-group">';
                    addActivityTemplateHtml += '<label class="col-md-5 control-label">Repeat Type</label>';
                    addActivityTemplateHtml += '<div class="col-md-7">';
                    addActivityTemplateHtml += '<select class="form-control" id="repeat_type">';
                    addActivityTemplateHtml += '<option>Select</option>';
                    addActivityTemplateHtml += '<option value="11">Daily</option>';
                    addActivityTemplateHtml += '<option value="22">Weekly</option>';
                    addActivityTemplateHtml += '<option value="33">Monthly</option>';
                    addActivityTemplateHtml += '</select>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '<div class="col-md-6 form-group">';
                    addActivityTemplateHtml += '<label class="col-md-5 control-label">Repeat After Every</label>';
                    addActivityTemplateHtml += '<div class="col-md-7">';
                    addActivityTemplateHtml += '<select class="form-control" id="repeatAfterEvery" >';
                    addActivityTemplateHtml += '<option>Select</option>';
                    for (var i = 1; i <= 31; i++) {
                    var id = i;                        
                    addActivityTemplateHtml += '<option value="' + id + '">' + id + '</option>';
                    }
                    addActivityTemplateHtml += '</select>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    
                    addActivityTemplateHtml += '<div class="row">';
                    addActivityTemplateHtml += '<div class="col-md-6 form-group">';
                    addActivityTemplateHtml += '<label class="col-md-5 control-label">Repeat On Day(Weekly) </label>';
                    addActivityTemplateHtml += '<div class="col-md-7">';
                    addActivityTemplateHtml += '<select class="form-control" id="repeatOnDay_weekly" multiple="multiple" >';
                    addActivityTemplateHtml += '<option selected="selected" >Select</option>';
                    addActivityTemplateHtml += '<option value="0" >MONDAY</option>';
                    addActivityTemplateHtml += '<option value="1" >TUESDAY</option>';
                    addActivityTemplateHtml += '<option value="2" >WEDNESDAY</option>';
                    addActivityTemplateHtml += '<option value="3" >THURSDAY</option>';
                    addActivityTemplateHtml += '<option value="4" >FRIDAY</option>';
                    addActivityTemplateHtml += '<option value="5" >SATURDAY</option>';
                    addActivityTemplateHtml += '<option value="6" >SUNDAY</option>';
                    addActivityTemplateHtml += '</select>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '<div class="col-md-6 form-group">';
                    addActivityTemplateHtml += '<label class="col-md-5 control-label">Repeat On Date (Monthly)</label>';
                    addActivityTemplateHtml += '<div class="col-md-7">';                    
                    addActivityTemplateHtml += '<select class="form-control" id="repeatOnDate_monthly" multiple="multiple" >';
                    addActivityTemplateHtml += '<option selected="selected" >Select</option>';
                    for (var i = 01; i <= 31; i++) {
                    var id = i;                        
                    addActivityTemplateHtml += '<option value="' + id + '">' + id + '</option>';
                    }
                    addActivityTemplateHtml += '</select>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    }
                    //
                    addActivityTemplateHtml += '<div class="row">';
                    addActivityTemplateHtml += '<div class="col-md-6 form-group">';
                    addActivityTemplateHtml += '<label class="col-md-5 control-label">Owner</label>';
                    addActivityTemplateHtml += '<div class="col-md-7">';
                    addActivityTemplateHtml += '<input type="Cname" name="currency" class="form-control" id="id_owner" value="' + loginUserName + '" disabled>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '<div class="col-md-6 form-group">';
                    addActivityTemplateHtml += '<label class="col-md-5 control-label">Assigned To</label>';
                    addActivityTemplateHtml += '<div class="col-md-7">';
                    addActivityTemplateHtml += '<input type="Cname" name="currency" class="form-control id_assigned" id="' + hierarchyId + '" value="' + hierarchyName + '" disabled>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '<div class="row">';
                    addActivityTemplateHtml += '<div class="col-md-6 form-group">';
                    addActivityTemplateHtml += '<label class="col-md-5 control-label">Status</label>';
                    addActivityTemplateHtml += '<div class="col-md-7">';
                    addActivityTemplateHtml += '<select class="form-control" id="id_status" onchange="outcomeForAddActivity()">';
                    addActivityTemplateHtml += '<option>Select</option>';
                    addActivityTemplateHtml += '<option value="2">Completed</option>';
                    addActivityTemplateHtml += '<option value="0" selected>Pending</option>';
                    addActivityTemplateHtml += '<option value="1">In-Progress</option>';
                    addActivityTemplateHtml += '<option value="3">Cancelled</option>';
                    addActivityTemplateHtml += '</select>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '<div class="col-md-6 form-group">';
                    addActivityTemplateHtml += '<label class="col-md-5 control-label">Outcome</label>';
                    addActivityTemplateHtml += '<div class="col-md-7">';
                    addActivityTemplateHtml += '<select class="form-control" id="id_outcome" disabled>';
                    addActivityTemplateHtml += '<option value="0" disabled>None</option>';
                    addActivityTemplateHtml += '</select>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '<div class="row">';
                    addActivityTemplateHtml += '<div class="col-md-12 form-group" id="dscp">';
                    addActivityTemplateHtml += '<label class="col-md-3 control-label">Description</label>';
                    addActivityTemplateHtml += '<div class="col-md-9">';
                    addActivityTemplateHtml += '<textarea class="form-control" rows="3" placeholder="Enter text" id="id_description"></textarea>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '<span id="id_outcomedescriptiontextbox" style="display:none;">';
                    addActivityTemplateHtml += '<div class="row">';
                    addActivityTemplateHtml += '<div class="col-md-12 form-group" id="dscp">';
                    addActivityTemplateHtml += '<label class="col-md-3 control-label">Outcome Description</label>';
                    addActivityTemplateHtml += '<div class="col-md-9">';
                    addActivityTemplateHtml += '<textarea class="form-control" rows="3" placeholder="Enter text" id="id_outcomedescription"></textarea>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</span>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '<div class="modal-footer">';
                    addActivityTemplateHtml += '<button type="submit" class="btn btn-info" onclick="createActivity(\'' + isSpecificUserActivity + '\',\'' + teamId + '\',\'' + hierarchyId + '\');return false;">Save</button>';
                    addActivityTemplateHtml += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</form>';
                    addActivityTemplateHtml += '</div>';
                    addActivityTemplateHtml += '</div>';
                    $('.cls_addActivity').html(addActivityTemplateHtml);
                    $('#pleaseWaitDialog').modal('hide');
                    $('#autocompleteCnm').autocomplete({
                        //lookup: customer,
			minChars:3,
	                paramName: 'searchText',
    			transformResult: function(response) {
        			return {
            				suggestions: $.map(response.result, function(dataItem) {
                				return { value: dataItem.value, id:dataItem.customerId  };
            				})
        			};
    			},
		        dataType:"json",
                        serviceUrl: fieldSenseURL + '/customer/visit?userToken='+userToken,
                        onSelect: function (suggestion) {
                            $('#id_hidden').val(suggestion.id);
                            customerContacts(suggestion.id);
                            var custId = suggestion.id;
                            $.ajax({
                                type: "PUT",
                                url: fieldSenseURL + "/customer/insertLatLang/" + custId,
                                contentType: "application/json; charset=utf-8",
                                headers: {
                                    "userToken": userToken
                                },
                                crossDomain: false,
                                cache: false,
                                dataType: 'json',
                                asyn: true
                            });
                        },
                       /* onSearchStart: function (query) {
				$('#pleaseWaitDialog').modal('show');
	                },
		        onSearchComplete: function (query, suggestions) {
				$('#pleaseWaitDialog').modal('hide');
		        }*/
                    });
                    if (jQuery().datepicker) {
                        $('.date-picker').datepicker({
                            rtl: App.isRTL(),
                            autoclose: true
                        });
                        $('body').removeClass("modal-open"); // fix bug when inline picker is used in modal
                    }
                    $('#id_createactivity').slimScroll({
                        size: '7px',
                        color: '#bbbbbb',
                        opacity: .9,
                        position: false ? 'left' : 'right',
                        height: 420,
                        allowPageScroll: false,
                        disableFadeOut: false
                    });
                } else {
                    $.ajax({
                        type: "GET",
                        url: fieldSenseURL + "/team/userPositions/" + loginUserId,
                        contentType: "application/json; charset=utf-8",
                        headers: {
                            "userToken": userToken
                        },
                        crossDomain: false,
                        cache: false,
                        dataType: 'json',
                        asyn: true,
                        success: function (datateamMember) {
                            if (datateamMember.errorCode == '0000') {
                                var teamMemberData = datateamMember.result;
                                var activityTemplate = '';
                                activityTemplate += '<div class="modal-dialog modal-wide">';
                                activityTemplate += '<div class="modal-content">';
                                activityTemplate += '<div class="modal-header">';
                                activityTemplate += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
                                activityTemplate += '<h4 class="modal-title">Add Activity</h4>';
                                activityTemplate += '</div>';
                                activityTemplate += '<form class="form-horizontal" role="form">';
                                activityTemplate += '<div class="modal-body">';
                                activityTemplate += '<div class="scroller" style="max-height:700px" data-always-visible="1" data-rail-visible1="1" id="id_createactivity">';
                                activityTemplate += '<div class="row">';
                                activityTemplate += '<div class="col-md-6 form-group">';
                                activityTemplate += '<label class="col-md-5 control-label">Customer Name</label>';
                                activityTemplate += '<div class="col-md-7">';
                                activityTemplate += '<input type="hidden" id="id_hidden" value="Val"/>';
                                activityTemplate += '<input type="Cname" name="currency" class="form-control" id="autocompleteCnm" placeholder="Enter text">';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '<div class="col-md-6 form-group">';
                                activityTemplate += '<label class="col-md-5 control-label">Contact</label>';
                                activityTemplate += '<div class="col-md-7">';
                                activityTemplate += '<select class="form-control" id="id_contact">';
                                activityTemplate += '</select>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '<div class="row">';
                                activityTemplate += '<div class="col-md-6 form-group">';
                                activityTemplate += '<label class="col-md-5 control-label">Activity</label>';
                                activityTemplate += '<div class="col-md-7">';
                                activityTemplate += '<input type="activity" class="form-control" placeholder="Enter text" id="id_activity">';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '<div class="col-md-6 form-group">';
                                activityTemplate += '<label class="col-md-5 control-label">Date</label>';
                                activityTemplate += '<div class="col-md-7">';
                                activityTemplate += '<div class="input-group date date-picker" data-date-format="dd-mm-yyyy" data-date-start-date="+0d">';
                                activityTemplate += '<input type="text" class="form-control" id="id_date" readonly>';
                                activityTemplate += '<span class="input-group-btn">';
                                activityTemplate += '<button class="btn btn-default" type="button"><i class="fa fa-calendar"></i></button>';
                                activityTemplate += '</span>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '<div class="row">';
                                activityTemplate += '<div class="col-md-6 form-group">';
                                activityTemplate += '<label class="col-md-5 control-label">Start Time</label>';
                                activityTemplate += '<div class="col-md-3">';
                                activityTemplate += '<select id="activity_hr"  class="form-control input-xssmall">';
                                activityTemplate += '<option value="HH">HH</option>';
                                for (var j = 0; j < 24; j++) {
                                    if (j < 10)
                                    {
                                        activityTemplate += '<option value="' + j + '">0' + j + '</option>';
                                    } else {
                                        activityTemplate += '<option value="' + j + '">' + j + '</option>';
                                    }
                                }
                                activityTemplate += '</select></div>';
                                activityTemplate += '<div class="col-md-3">';
                                activityTemplate += '<select id="activity_min" class="form-control input-xssmall">';
                                activityTemplate += '<option value="MM">MM</option>';
                                for (var k = 0; k < 60; k = k + 1) {
                                    if (k < 10)
                                    {
                                        activityTemplate += '<option value="' + k + '">0' + k + '</option>';
                                    } else {
                                        activityTemplate += '<option value="' + k + '">' + k + '</option>';
                                    }
                                }
                                activityTemplate += '</select>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '<div class="col-md-6 form-group">';
                                activityTemplate += '<label class="col-md-5 control-label">End Time</label>';
                                activityTemplate += '<div class="col-md-3">';
                                activityTemplate += '<select id="activity_endHr" class="form-control input-xssmall">';
                                activityTemplate += '<option value="HH">HH</option>';
                                for (var l = 0; l < 24; l++) {
                                    if (l < 10)
                                    {
                                        activityTemplate += '<option value="' + l + '">0' + l + '</option>';
                                    } else {
                                        activityTemplate += '<option value="' + l + '">' + l + '</option>';
                                    }
                                }
                                activityTemplate += '</select></div>';
                                activityTemplate += '<div class="col-md-3">';
                                activityTemplate += '<select id="activity_endMin" class="form-control input-xssmall">';
                                activityTemplate += '<option value="MM">MM</option>';
                                for (var m = 0; m < 60; m = m + 1) {
                                    if (m < 10)
                                    {
                                        activityTemplate += '<option value="' + m + '">0' + m + '</option>';
                                    } else {
                                        activityTemplate += '<option value="' + m + '">' + m + '</option>';
                                    }
                                }
                                activityTemplate += '</select>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '<div class="row">';
                                activityTemplate += '<div class="col-md-6 form-group">';
                                activityTemplate += '<label class="col-md-5 control-label">Purpose</label>';
                                activityTemplate += '<div class="col-md-7">';
                                activityTemplate += '<select class="form-control" id="id_purpose">';
                                for (var i = 0; i < purposeData.length; i++) {
                                    var id = purposeData[i].id;
                                    var purpose = purposeData[i].purpose;
                                    activityTemplate += '<option value="' + id + '">' + purpose + '</option>';
                                }
                                activityTemplate += '</select>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                
                                //Added by Jyoti 30-01-2017
                                if(isRecurring == 1){
                                activityTemplate += '<div class="row">';
                                activityTemplate += '<div class="col-md-6 form-group">';
                                activityTemplate += '<label class="col-md-5 control-label">Recurring Till Date</label>';
                                activityTemplate += '<div class="col-md-7">';                    
                                activityTemplate += '<div class="input-group date date-picker" data-date-format="dd-mm-yyyy" data-date-start-date="-1m">';
                                activityTemplate += '<input type="text" class="form-control" id="end_date" readonly value="' + today + '">';
                                activityTemplate += '<span class="input-group-btn">';
                                activityTemplate += '<button class="btn btn-default" type="button"><i class="fa fa-calendar"></i></button>';
                                activityTemplate += '</span>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';

                                activityTemplate += '<div class="row">';
                                activityTemplate += '<div class="col-md-6 form-group">';
                                activityTemplate += '<label class="col-md-5 control-label">Repeat Type</label>';
                                activityTemplate += '<div class="col-md-7">';
                                activityTemplate += '<select class="form-control" id="repeat_type">';
                                activityTemplate += '<option>Select</option>';
                                activityTemplate += '<option value="11">Daily</option>';
                                activityTemplate += '<option value="22">Weekly</option>';
                                activityTemplate += '<option value="33">Monthly</option>';
                                activityTemplate += '</select>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '<div class="col-md-6 form-group">';
                                activityTemplate += '<label class="col-md-5 control-label">Repeat After Every</label>';
                                activityTemplate += '<div class="col-md-7">';
                                activityTemplate += '<select class="form-control" id="repeatAfterEvery" >';
                                activityTemplate += '<option>Select</option>';
                                for (var i = 1; i <= 31; i++) {
                                var id = i;                        
                                activityTemplate += '<option value="' + id + '">' + id + '</option>';
                                }
                                activityTemplate += '</select>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';

                                activityTemplate += '<div class="row">';
                                activityTemplate += '<div class="col-md-6 form-group">';
                                activityTemplate += '<label class="col-md-5 control-label">Repeat On Day(Weekly) </label>';
                                activityTemplate += '<div class="col-md-7">';
                                activityTemplate += '<select class="form-control" id="repeatOnDay_weekly" multiple="multiple" >';
                                activityTemplate += '<option selected="selected" >Select</option>';
                                activityTemplate += '<option value="0" >MONDAY</option>';
                                activityTemplate += '<option value="1" >TUESDAY</option>';
                                activityTemplate += '<option value="2" >WEDNESDAY</option>';
                                activityTemplate += '<option value="3" >THURSDAY</option>';
                                activityTemplate += '<option value="4" >FRIDAY</option>';
                                activityTemplate += '<option value="5" >SATURDAY</option>';
                                activityTemplate += '<option value="6" >SUNDAY</option>';
                                activityTemplate += '</select>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '<div class="col-md-6 form-group">';
                                activityTemplate += '<label class="col-md-5 control-label">Repeat On Date (Monthly)</label>';
                                activityTemplate += '<div class="col-md-7">';                    
                                activityTemplate += '<select class="form-control" id="repeatOnDate_monthly" multiple="multiple" >';
                                activityTemplate += '<option selected="selected" >Select</option>';
                                for (var i = 01; i <= 31; i++) {
                                var id = i;                        
                                activityTemplate += '<option value="' + id + '">' + id + '</option>';
                                }
                                activityTemplate += '</select>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                }
                                //
                                
                                activityTemplate += '</div>';
                                activityTemplate += '<div class="row">';
                                activityTemplate += '<div class="col-md-6 form-group">';
                                activityTemplate += '<label class="col-md-5 control-label">Owner</label>';
                                activityTemplate += '<div class="col-md-7">';
                                activityTemplate += '<input type="Cname" name="currency" class="form-control" id="id_owner" value="' + loginUserName + '" disabled>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '<div class="col-md-6 form-group">';
                                activityTemplate += '<label class="col-md-5 control-label">Assigned To</label>';
                                activityTemplate += '<div class="col-md-7">';
                                activityTemplate += '<select class="form-control" id="id_assigned">';
                                activityTemplate += '<option>None</option>';
                                for (i = 0; i < teamMemberData.length; i++) {
                                    var userId = teamMemberData[i].user.id;
                                    var firstName = teamMemberData[i].user.firstName;
                                    var lastName = teamMemberData[i].user.lastName;
                                    var fullName = firstName + ' ' + lastName;
                                    activityTemplate += '<option value="' + userId + '">' + fullName + '</option>';
                                }
                                activityTemplate += '</select>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '<div class="row">';
                                activityTemplate += '<div class="col-md-6 form-group">';
                                activityTemplate += '<label class="col-md-5 control-label">Status</label>';
                                activityTemplate += '<div class="col-md-7">';
                                activityTemplate += '<select class="form-control" id="id_status" onchange="outcomeForAddActivity()">';
                                activityTemplate += '<option>Select</option>';
                                activityTemplate += '<option value="2">Completed</option>';
                                activityTemplate += '<option value="0" selected>Pending</option>';
                                activityTemplate += '<option value="1">In-Progress</option>';
                                activityTemplate += '<option value="3">Cancelled</option>';
                                activityTemplate += '</select>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '<div class="col-md-6 form-group">';
                                activityTemplate += '<label class="col-md-5 control-label">Outcome</label>';
                                activityTemplate += '<div class="col-md-7">';
                                activityTemplate += '<select class="form-control" id="id_outcome">';
                                activityTemplate += '<option value="0" disabled>None</option>';
                                activityTemplate += '</select>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '<div class="form-group" id="dscp">';
                                activityTemplate += '<label class="col-md-3 control-label">Description</label>';
                                activityTemplate += '<div class="col-md-9">';
                                activityTemplate += '<textarea class="form-control" rows="3" placeholder="Enter text" id="id_description"></textarea>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '<span id="id_outcomedescriptiontextbox" style="display:none;">';
                                activityTemplate += '<div class="row">';
                                activityTemplate += '<div class="col-md-12 form-group" id="dscp">';
                                activityTemplate += '<label class="col-md-3 control-label">Outcome Description</label>';
                                activityTemplate += '<div class="col-md-9">';
                                activityTemplate += '<textarea class="form-control" rows="3" placeholder="Enter text" id="id_outcomedescription"></textarea>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '</span>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                activityTemplate += '<div class="modal-footer">';
                                activityTemplate += '<button type="submit" class="btn btn-info" onclick="createActivity(\'' + isSpecificUserActivity + '\',\'' + teamId + '\',\'' + hierarchyId + '\');return false;">Save</button>';
                                activityTemplate += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
                                activityTemplate += '</div>';
                                activityTemplate += '</form>';
                                activityTemplate += '</div>';
                                activityTemplate += '</div>';
                                $('.cls_addActivity').html(activityTemplate);
                                $('#pleaseWaitDialog').modal('hide');
                                $('#autocompleteCnm').autocomplete({
                                    //lookup: customer,
				    minChars:3,
				    serviceUrl: fieldSenseURL + '/customer/visit?userToken='+userToken,
				    paramName: 'searchText',
    				    transformResult: function(response) {
        			    	return {
            					suggestions: $.map(response.result, function(dataItem) {
                					return { value: dataItem.value, id:dataItem.customerId  };
            					})
        			    	};
    				    },
		        	    dataType:"json",
                                    onSelect: function (suggestion) {
                                        $('#id_hidden').val(suggestion.customerId);
                                        customerContacts(suggestion.customerId);
                                        var custId = suggestion.customerId;
                                        $.ajax({
                                            type: "PUT",
                                            url: fieldSenseURL + "/customer/insertLatLang/" + custId,
                                            contentType: "application/json; charset=utf-8",
                                            headers: {
                                                "userToken": userToken
                                            },
                                            crossDomain: false,
                                            cache: false,
                                            dataType: 'json',
                                            asyn: true
                                        });
                                    },
				   /* onSearchStart: function (query) {
						$('#pleaseWaitDialog').modal('show');
				    },
				    onSearchComplete: function (query, suggestions) {
						$('#pleaseWaitDialog').modal('hide');
				    } */
                                });
                                if (jQuery().datepicker) {
                                    $('.date-picker').datepicker({
                                        rtl: App.isRTL(),
                                        autoclose: true
                                    });
                                    $('body').removeClass("modal-open"); // fix bug when inline picker is used in modal
                                }
                                $('#id_createactivity').slimScroll({
                                    size: '7px',
                                    color: '#bbbbbb',
                                    opacity: .9,
                                    position: false ? 'left' : 'right',
                                    height: 420,
                                    allowPageScroll: false,
                                    disableFadeOut: false
                                });
                            }
                        }
                    });
                }
            }
        }
    });
}
function customerList() {
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/customer",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            if (data.errorCode == '0000') {
                var customerData = data.result;
                for (var i = 0; i < customerData.length; i++) {
                    var customerId = customerData[i].id;
                    var customerName = customerData[i].customerName;
                    var customerLocation = customerData[i].customerLocation;
                    var customerObj = {
                        "customerId": customerId,
                        "value": customerName + "-" + customerLocation
                    }
                    customer.push(customerObj);
                }
            } else {
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}

function customerContacts(customerId) {
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/customerContact/customer/" + customerId,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            $('#id_contactEdit').html('');
            $('#id_contact').html('');
            if (data.errorCode == '0000') {
                var contactData = data.result;
                var contactTemplate = '';
                contactTemplate = '<option>None</option>';
                for (var i = 0; i < contactData.length; i++) {
                    var contactId = contactData[i].id;
                    var firstName = contactData[i].firstName;
                    var middleName = contactData[i].middleName;
                    var lastName = contactData[i].lastName;
                    var fullName = firstName + ' ' + middleName + ' ' + lastName;
                    contactTemplate += '<option value="' + contactId + '">' + fullName + '</option>';
                }
                $('#id_contact').append(contactTemplate);
                $('#id_contactEdit').append(contactTemplate);
                $('#pleaseWaitDialog').modal('hide');
            }
            else {
                $('#pleaseWaitDialog').modal('hide');
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}

function createActivity(isSpecificUserActivity, teamId, userId) {
    var customer = document.getElementById("id_hidden").value.trim();
    if (customer === 'Val') {
        fieldSenseTosterError("Customer Name cannot be blank", true);
        return false;
    }
    var contact = document.getElementById("id_contact").value.trim();
    if (contact.trim() == "" || contact.trim() == "None") {
        contact = 0;
    }
   var activity = document.getElementById("id_activity").value.trim();
//    if (activity.trim().length == 0) {
//        fieldSenseTosterError("Title cannot be blank.", true);
//        return false;
//    }
    var dates = document.getElementById("id_date").value.trim();
    if (dates.trim().length == 0) {
        fieldSenseTosterError("Date cannot be empty", true);
        return false;
    }
    var startHr = document.getElementById("activity_hr").value.trim();
    if (startHr == "HH") {
        fieldSenseTosterError("Start Time should be selected", true);
        return false;
    }
    var startMin = document.getElementById("activity_min").value.trim();
    if (startMin == "MM") {
        fieldSenseTosterError("Start Time should be selected", true);
        return false;
    }
    var endtHr = document.getElementById("activity_endHr").value.trim();
    if (endtHr == "HH") {
        fieldSenseTosterError("End Time should be selected", true);
        return false;
    }
    var endMin = document.getElementById("activity_endMin").value.trim();
    if (endMin == "MM") {
        fieldSenseTosterError("End Time should be selected", true);
        return false;
    }
    if(parseInt(startHr)>parseInt(endtHr) || (startHr==endtHr && parseInt(startMin)>parseInt(endMin))){
	fieldSenseTosterError("Start Time should be less than End Time", true);
        return false;
    }
    var purpose = document.getElementById("id_purpose").value.trim();
    if (purpose === 'Select') {
        fieldSenseTosterError("Purpose cannot be empty", true);
        return false;
    }
    var assignedTo;
    if (isSpecificUserActivity == 'true') {
        assignedTo = document.getElementsByClassName("id_assigned")[0].id.trim();
    }
    else {
        assignedTo = document.getElementById("id_assigned").value.trim();
    }
    if (assignedTo === 'None') {
        fieldSenseTosterError("Please select proper assigned to", true);
        return false;
    }
    var status = document.getElementById("id_status").value.trim();
    if (status === 'Select') {
        fieldSenseTosterError("Please select proper status", true);
        return false;
    }
    var outcome = document.getElementById("id_outcome").value;
    var description = document.getElementById("id_description").value.trim();
    if (description.trim().length != 0) {
        if (description.trim().length > 1000) {
            fieldSenseTosterError("Description can not be more than 1000 character", true);
            return false;
        }
    }
    var outcomeDescription = document.getElementById("id_outcomedescription").value.trim();
    if (outcomeDescription.trim().length != 0) {
        if (outcomeDescription.trim().length > 2000) {
            fieldSenseTosterError("Outcome Description can not be more than 2000 character", true);
            return false;
        }
    }
     // for start date with start/end time
    var dateSplit = dates.split("-");
    var date = dateSplit[0];
    var month = dateSplit[1];
    var year = dateSplit[2];
    var startDate = convertLocalDateToServerDate(year, month - 1, date, startHr, startMin);
    var adate = startDate.getFullYear() + "-" + (startDate.getMonth() + 1) + "-" + startDate.getDate() + " " + startDate.getHours() + ":" + startDate.getMinutes() + ':00';
    var endtHours = endtHr + ":" + endMin;
    var endDTime;
        endDTime = convertLocalDateToServerDate(year, month - 1, date, endtHr, endMin);
        endDTime = endDTime.getFullYear() + "-" + (endDTime.getMonth() + 1) + "-" + endDTime.getDate() + " " + endDTime.getHours() + ":" + endDTime.getMinutes() + ':00';
    
    //  Added by Jyoti, 01-02-2017
    if(isRecurring == 1){
        var repeatOnDay_weekly = new Array();
        var repeatOnDate_monthly = new Array();
        var recurrTillDate = document.getElementById("end_date").value.trim();
        // for recurrTill date with start/end time
        var recurrTillDateSplit = recurrTillDate.split("-");
        var recurrTill_date = recurrTillDateSplit[0];
        var recurrTill_month = recurrTillDateSplit[1];
        var recurrTill_year = recurrTillDateSplit[2];
        var recurrTill_Date = convertLocalDateToServerDate(recurrTill_year, recurrTill_month - 1, recurrTill_date, startHr, startMin);
        var recurrTill_Date_startTime = recurrTill_Date.getFullYear() + "-" + (recurrTill_Date.getMonth() + 1) + "-" + recurrTill_Date.getDate() + " " + recurrTill_Date.getHours() + ":" + recurrTill_Date.getMinutes() + ':00';
        var recurrTill_Date_endTime;
            recurrTill_Date_endTime = convertLocalDateToServerDate(recurrTill_year, recurrTill_month - 1, recurrTill_date, endtHr, endMin);
            recurrTill_Date_endTime = recurrTill_Date_endTime.getFullYear() + "-" + (recurrTill_Date_endTime.getMonth() + 1) + "-" + recurrTill_Date_endTime.getDate() + " " + recurrTill_Date_endTime.getHours() + ":" + recurrTill_Date_endTime.getMinutes() + ':00';

        if (recurrTill_Date < startDate || recurrTillDate.trim().length == 0) {
            fieldSenseTosterError("Date cannot be less than start date or empty.", true);
            return false;
        }
        
        var repeatType = document.getElementById("repeat_type").value.trim();
        if (repeatType == 'Select') {
            fieldSenseTosterError("Please select repeat type", true);
            return false;
        }

        var repeatAfterEvery = document.getElementById("repeatAfterEvery").value.trim();
        if (repeatAfterEvery == 'Select') {
            fieldSenseTosterError("Please select repeat after every.", true);
            return false;
        }
        
        if(repeatType == '22'){
            repeatOnDay_weekly = $('#repeatOnDay_weekly').val();    // multi value selection
            if($('#repeatOnDay_weekly').val() == 'Select'){
                fieldSenseTosterError("Please Select Weekly Day", true);
                return false;
            }
        }
        
        if(repeatType == '33'){
            repeatOnDate_monthly = $('#repeatOnDate_monthly').val();
            if($('#repeatOnDate_monthly').val() == 'Select'){
                fieldSenseTosterError("Please Select Monthly Date", true);
                return false;
            }
        }
        
    }
    
    $('#pleaseWaitDialog').modal('show');
    var activityObject = {
        "title": activity,
        "sdateTime": adate,
        "sendTime": endDTime,
        // for recurring event
        "isRecurring": isRecurring,
        "appointmentRecurringList":{
            "repeatAfterEvery": repeatAfterEvery,
            "repeatType": repeatType,        
            "recurrTill_Date_startTime": recurrTill_Date_startTime,
            "recurrTill_Date_endTime": recurrTill_Date_endTime,
            "repeatOnDay_weekly": repeatOnDay_weekly,
            "repeatOnDate_monthly": repeatOnDate_monthly
        },
        // ended by jyoti
        "customer": {
            "id": customer
        },
        "customerContact": {
            "id": contact
        },
        "owner": {
            "id": loginUserId
        },
        "assignedTo": {
            "id": assignedTo
        },
        "description": description,
        "type": 0,
        "purpose": {
            "id": purpose
        },
        "outcomes": {
            "id": outcome
        },
        "status": status,
        "createdBy": {
            "id": loginUserId
        },
        "outcomeDescription": outcomeDescription
    }
    var jsonData = JSON.stringify(activityObject);
    var url = fieldSenseURL + "/appointment/create";
    if(isRecurring){
        url = fieldSenseURL + "/appointment/create/recurring"
    }
    console.log("url : "+url);
    $.ajax({
        type: "POST",
        url: url,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            if (data.errorCode == '0000') {
                $(".cls_addActivity").modal('hide');
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterSuccess(data.errorMessage, true);
                activityShowTemplate(teamId, userId);
            }
            else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}
function dateWiseActivity(date, teamId, userId) {
    var dat = formatJSDate(date);
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/appointment/" + dat + "/team/" + teamId + "/user/" + userId,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            if (data.errorCode == '0000') {
                $('#id_forDateWise').html('');
                var activityData = data.result;
                var activityShowTemplate = "";
                if (activityData.length != 0) {
                    for (var i = 0; i < activityData.length; i++) {
                        var companyName = activityData[i].customer.customerPrintas;
                        var customerFnm = activityData[i].customerContact.firstName;
                        var customerMnm = activityData[i].customerContact.middleName;
                        var customerLnm = activityData[i].customerContact.lastName;
                        var customerName = customerFnm + ' ' + customerMnm + ' ' + customerLnm;
                        var activity = activityData[i].title;
                        var activityPurpose = activityData[i].purpose.purpose;
                        var activityStatus = activityData[i].status;
                        var activityP = '';
                        if (activityStatus == 0) {
                            activityP = "Pending";
                        }
                        else if (activityStatus == 1) {
                            activityP = "In-Progress";
                        }
                        else if (activityStatus == 2) {
                            activityP = "Completed";
                        }

                        else if (activityStatus == 3) {
                            activityP = "Cancelled";
                        }
                        var activityDateTime = fieldSenseseMesageTimeFormateForJsonDate(activityData[i].dateTime);
                        var activityDateTimeSplit = activityDateTime.split(" ");
                        var activityTimeSplit = activityDateTimeSplit[3];
                        var activityTimeAfterSplit = activityTimeSplit.split(":");
                        var activityHourse = activityTimeAfterSplit[0];
                        var activityMinutes = activityTimeAfterSplit[1];
                        var activityTime;
                        if (activityHourse < 12) {
                            activityTime = activityHourse + '.' + activityMinutes + ' AM';
                        }
                        else if (activityHourse == 12) {
                            activityTime = activityHourse + '.' + activityMinutes + ' PM';
                        } else {
                            var activityHourses = activityHourse - 12;
                            activityTime = activityHourses + '.' + activityMinutes + ' PM';
                        }
                        activityShowTemplate += '<div class="form-body">';
                        activityShowTemplate += '<div class="form-group">';
                        activityShowTemplate += '<label class="col-md-2 control-label">Company</label>';
                        activityShowTemplate += '<label class="col-md-10 label2">' + companyName + '</label>';
                        activityShowTemplate += '</div>';
                        activityShowTemplate += '<div class="form-group">';
                        activityShowTemplate += '<label class="col-md-2 control-label">Activity</label>';
                        activityShowTemplate += '<label class="col-md-5 label2">' + activity + '</label>';
                        activityShowTemplate += '<label class="col-md-2 control-label">Time</label>';
                        activityShowTemplate += '<label class="col-md-3 label2">' + activityTime + '</label>';
                        activityShowTemplate += '</div>';
                        activityShowTemplate += '<div class="form-group">';
                        activityShowTemplate += '<label class="col-md-2 control-label">Purpose</label>';
                        activityShowTemplate += '<label class="col-md-5 label2">' + activityPurpose + '</label>';
                        activityShowTemplate += '<label class="col-md-2 control-label">Status</label>';
                        activityShowTemplate += '<label class="col-md-3 label2">' + activityP + '</label>';
                        activityShowTemplate += '</div>';
                        activityShowTemplate += '<div class="form-group">';
                        activityShowTemplate += '<label class="col-md-2 control-label">Contact</label>';
                        activityShowTemplate += '<label class="col-md-10 label2">' + customerName + '</label>';
                        activityShowTemplate += '</div>';
                        activityShowTemplate += '</div>';
                    }
                } else {
                    activityShowTemplate += ' No activites for user ';
                }

                $('#id_forDateWise').html(activityShowTemplate);
                $('#pleaseWaitDialog').modal('hide');
                $('.date-picker').datepicker({
                    rtl: App.isRTL(),
                    autoclose: true
                }).on('changeDate', function (ev) {
                    dateWiseActivity(ev.date);
                });
            } else {
                $('#pleaseWaitDialog').modal('hide');
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });

}
function users() {
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/user/noTeamMembers",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: false,
        success: function (data) {
            var userData = data.result;
            var user;
            for (var i = 0; i < userData.length; i++) {
                var id = userData[i].id;
                var firstName = userData[i].firstName;
                var lastName = userData[i].lastName;
                var name = firstName + ' ' + lastName;
                if (id != loginUserId) {
                    user = {
                        userId: id,
                        userName: name
                    }
                    usersArray.push(user);
                }
            }
            $('#pleaseWaitDialog').modal('hide');
        }
    });
}
function showExpenseTemplate(index) {
   // var expenseDetails = id.split(globalSplit);
    var expenseId = expensearray[index].expense_data.id;
    var expenseName = expensearray[index].expense_data.expenseName;
    var expenseTime = fieldSenseDateTimeForExpense(expensearray[index].expense_data.expenseTime);
    var amountSpent = expensearray[index].expense_data.amountSpent;
    var expenseStatus = expensearray[index].expense_data.status;
    var customerName = expensearray[index].expense_data.customerName;
    var userId = expensearray[index].expense_data.user.id; 
    var appointmentTitle = expensearray[index].expense_data.appointment.title;
    expenseName = expenseName.replace(/my11c/g, "'");
    var status= expensearray[index].expense_data.status;
    console.log();

    var showExpenseTemplateHtml = '';
    showExpenseTemplateHtml += '<div class="modal-dialog modal-wide">';
    showExpenseTemplateHtml += '<div class="modal-content">';
    showExpenseTemplateHtml += '<div class="modal-header">';
    showExpenseTemplateHtml += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
    showExpenseTemplateHtml += '<h4 class="modal-title">Approve/Reject Expenses</h4>';
    showExpenseTemplateHtml += '</div>';
    showExpenseTemplateHtml += '<form action="#" class="form-horizontal form-row-seperated">';
    showExpenseTemplateHtml += '<div class="modal-body">';
    showExpenseTemplateHtml += '<div class="scroller" data-always-visible="1" data-rail-visible1="1" id="id_scrollerShowExpense">';
    showExpenseTemplateHtml += '<div class="form-body">';
    showExpenseTemplateHtml += '<div class="row">';
    showExpenseTemplateHtml += '<div class="col-md-12 form-group" id="trs">';
    showExpenseTemplateHtml += '<label class="col-md-3 control-label">Expense Head</label>';
    showExpenseTemplateHtml += '<div class="col-md-9">';
    showExpenseTemplateHtml += '<input type="name" class="form-control" Value="' + expenseName + '" tabindex="1" readonly>';
    showExpenseTemplateHtml += '</div>';
    showExpenseTemplateHtml += '</div>';
    showExpenseTemplateHtml += '</div>';
    showExpenseTemplateHtml += '<div class="row">';
    showExpenseTemplateHtml += '<div class="col-md-6 form-group">';
    showExpenseTemplateHtml += '<label class="col-md-5 control-label">Customer Name</label>';
    showExpenseTemplateHtml += '<div class="col-md-7">';
    showExpenseTemplateHtml += '<input type="name" class="form-control" Value="' + customerName + '" tabindex="1" readonly>';
    showExpenseTemplateHtml += '</div>';
    showExpenseTemplateHtml += '</div>';
    showExpenseTemplateHtml += '<div class="col-md-6 form-group">';
    showExpenseTemplateHtml += '<label class="col-md-5 control-label">Visit</label>';
    showExpenseTemplateHtml += '<div class="col-md-7">';
    showExpenseTemplateHtml += '<input type="text" class="form-control" value="' + appointmentTitle + '" tabindex="2" readonly>';
    showExpenseTemplateHtml += '</div>';
    showExpenseTemplateHtml += '</div>';
    showExpenseTemplateHtml += '</div>';
    showExpenseTemplateHtml += '<div class="row">';
    showExpenseTemplateHtml += '<div class="col-md-6 form-group">';
    showExpenseTemplateHtml += '<label class="col-md-5 control-label">Date/Time</label>';
    showExpenseTemplateHtml += '<div class="col-md-7">';
    showExpenseTemplateHtml += '<input type="text" class="form-control" value="' + expenseTime + '" tabindex="3" readonly>';
    showExpenseTemplateHtml += '</div>';
    showExpenseTemplateHtml += '</div>';
    showExpenseTemplateHtml += '<div class="col-md-6 form-group">';
    showExpenseTemplateHtml += '<label class="col-md-5 control-label">Amount <span>&#8377;</span></label>';
    showExpenseTemplateHtml += '<div class="col-md-7">';
    showExpenseTemplateHtml += '<input type="text" class="form-control" value="' + amountSpent + '" tabindex="4" readonly>';
    showExpenseTemplateHtml += '</div>';
    showExpenseTemplateHtml += '</div>';
    showExpenseTemplateHtml += '</div>';

     showExpenseTemplateHtml += '<div class="row">';
    showExpenseTemplateHtml += '<div class="col-md-6 form-group">';
    showExpenseTemplateHtml += '<label class="col-md-5 control-label">Status</label>';
    showExpenseTemplateHtml += '<div class="col-md-7">';
    if(status==0){
	 showExpenseTemplateHtml += '<input type="text" class="form-control" value="Pending Approval by Reporting Head" tabindex="3" readonly>';
    }else if(status==1){
	showExpenseTemplateHtml += '<input type="text" class="form-control" value="Pending Approval by Accounts" tabindex="3" readonly>';
    }else if(status==2){
	showExpenseTemplateHtml += '<input type="text" class="form-control" value="Rejected by Reporting Head" tabindex="3" readonly>';
    }else if(status==3){
	showExpenseTemplateHtml += '<input type="text" class="form-control" value="Approved" tabindex="3" readonly>';
    }else if(status==4){
	showExpenseTemplateHtml += '<input type="text" class="form-control" value="Rejected by Accounts" tabindex="3" readonly>';
    }else if(status==5){
	showExpenseTemplateHtml += '<input type="text" class="form-control" value="Disbursed" tabindex="3" readonly>';
    }
   
    showExpenseTemplateHtml += '</div>';
    showExpenseTemplateHtml += '</div>';
    if(status==5){
	    showExpenseTemplateHtml += '<div class="col-md-6 form-group">';
	    showExpenseTemplateHtml += '<label class="col-md-5 control-label">Disbursed Amount <span>&#8377;</span></label>';
	    showExpenseTemplateHtml += '<div class="col-md-7">';
	    showExpenseTemplateHtml += '<input type="text" class="form-control" value="' + expensearray[index].expense_data.disburse_amount + '" tabindex="4" readonly>';
	    showExpenseTemplateHtml += '</div>';
	    showExpenseTemplateHtml += '</div>';
    }else{
	showExpenseTemplateHtml += '<div class="col-md-6 form-group" style="display:none">';
    	showExpenseTemplateHtml += '<label class="col-md-5 control-label">Disbursed Amount <span>&#8377;</span></label>';
    	showExpenseTemplateHtml += '<div class="col-md-7">';
    	showExpenseTemplateHtml += '<input type="text" class="form-control" value="' + expensearray[index].expense_data.disburse_amount + '" tabindex="4" readonly>';
    	showExpenseTemplateHtml += '</div>';
    	showExpenseTemplateHtml += '</div>';
    }
    	showExpenseTemplateHtml += '</div>';


    showExpenseTemplateHtml += '<div class="row">';
    showExpenseTemplateHtml += '<div class="col-md-12 form-group" id="trs">';
    showExpenseTemplateHtml += '<label class="col-md-3 control-label">Attached Receipt</label>';
    showExpenseTemplateHtml += '<div class="col-md-9">';
    time = new Date();
    showExpenseTemplateHtml += '<img src="' + expenseImageURLPath + 'expense_' + accountId + '_' + userId + '_' + expenseId + '_640X480.png?' + time + '" onerror="this.src=\'assets/img/noimage.jpg\';"/>';
    showExpenseTemplateHtml += '</div>';
    showExpenseTemplateHtml += '</div>';
    showExpenseTemplateHtml += '</div>';
    showExpenseTemplateHtml += '</div>';
    showExpenseTemplateHtml += '</div>';
    showExpenseTemplateHtml += '</div>';
    showExpenseTemplateHtml += '<div class="modal-footer">';
    if (expenseStatus == 0 && loginUserId != userId) {
        showExpenseTemplateHtml += '<button type="button" data-dismiss="modal" class="btn btn-info" tabindex="17" onClick="approveExpense(\'' + index + '\');return false;">Approve</button>';
        //        showExpenseTemplateHtml += '<button type="button" data-dismiss="modal" class="btn btn-default" tabindex="18" onClick="rejectExpense(\'' + id + '\');return false;">Reject</button>';
        showExpenseTemplateHtml += '<button type="button" data-dismiss="modal" class="btn btn-default" tabindex="18" onClick="rejectExpenseWithResonTemplate(\'' + index + '\');return false;">Reject</button>';
    } else {
        showExpenseTemplateHtml += '<button type="button" data-dismiss="modal" class="btn btn-default" tabindex="18" >Close</button>';
    }
    showExpenseTemplateHtml += '</div>';
    showExpenseTemplateHtml += '</form>';
    showExpenseTemplateHtml += '</div>';
    showExpenseTemplateHtml += '</div>';
    $('.cls_showexpense').html(showExpenseTemplateHtml);
    $('#id_scrollerShowExpense').slimScroll({
        size: '7px',
        color: '#bbbbbb',
        opacity: .9,
        position: false ? 'left' : 'right',
        height: 420,
        allowPageScroll: false,
        disableFadeOut: false
    });
}
function userActivitiesTemplate(teamId, userId, userName) {

    userActivityMarkersFlag = true;
    var activityTemplate = ''
    $('.cls_usractivities').html("");
    $('#pleaseWaitDialog').modal('show');
    var dateOnly = new Date();
    dateOnly = convertLocalDateToServerDate(dateOnly.getFullYear(), dateOnly.getMonth(), dateOnly.getDate(), dateOnly.getHours(), dateOnly.getMinutes());
    var month = dateOnly.getMonth() + 1;
    var date = dateOnly.getDate();
    if (month < 10) {
        month = '0' + month;
    }
    if (date < 10) {
        date = '0' + date;
    }
    dateOnly = dateOnly.getFullYear() + "-" + month + "-" + date;
    $.ajax({
        type: "GET",
        //url: fieldSenseURL + "/attendance/time/" + userId + "/" + dateOnly,
        url: fieldSenseURL + "/attendance/lastPunchDateTime/" + userId ,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            if (data.errorCode == '0000') {
                var dateString = data.result.date;
                var punchOutDateString = data.result.punchOutDate;
                var isUserInMeeting = data.result.isUseInMeeting;
                var fullDate;
                if (dateString != "") {
                    var timeString = data.result.time;
                    var punchOutString = data.result.punchOutTime;
                    var slpitDateString = dateString.split('-');
                    var year = slpitDateString[0];
                    var month = slpitDateString[1];
                    var date = slpitDateString[2];
                    month = month - 1;

		    var punchoutSplit = punchOutString.split(':');
                    var punchOutHr = punchoutSplit[0];
                    var punchOutMin = punchoutSplit[1];
		    var slpitPODString = punchOutDateString.split('-');
                    var podYear = slpitPODString[0];
                    var podMonth = slpitPODString[1];
                    var podDate = slpitPODString[2];
                    podMonth = podMonth - 1;
                    var punchOutDateTime = convertServerDateToLocalDate(podYear, podMonth, podDate, punchOutHr, punchOutMin);
                    punchOutDateTime = punchInOutTimeFormate(punchOutDateTime);
		
		    var slpitTimeString = timeString.split(':');
                    var hr = slpitTimeString[0];
                    var min = slpitTimeString[1];
                    var punchDateTime = convertServerDateToLocalDate(year, month, date, hr, min);
                    punchDateTime = punchInOutTimeFormate(punchDateTime);


                    if (punchOutDateString != "1111-11-11") {
                        fullDate = punchOutDateTime;
                    } else {
                        fullDate = punchDateTime;
                    }
                }

                var dateForSelection = new Date();
                var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
                var fromDateForExpense = '1 ' + monthNames[dateForSelection.getMonth()] + ' ' + dateForSelection.getFullYear();
                var toDateForExpense = dateForSelection.getDate() + ' ' + monthNames[dateForSelection.getMonth()] + ' ' + dateForSelection.getFullYear();
                dateForSelection = dateForSelection.getDate() + " " + monthNames[dateForSelection.getMonth()] + " " + dateForSelection.getFullYear();
                activityTemplate += '<div class="targetDiv">';
                activityTemplate += '<div class="portlet-title">';
                activityTemplate += '<div class="caption">';
                activityTemplate += '' + userName + '';
                activityTemplate += '</div>';
                activityTemplate += '<div class="caption2">';
                if (isUserInMeeting) {
                    activityTemplate += 'In Meeting|';
                }
                activityTemplate += '<span>';
                if (dateString == "" || (punchOutDateString != "1111-11-11" && dateForSelection != punchDateTime.split(",")[0] )) {
                    activityTemplate += 'Not punched in';
                } else if (punchOutDateString == "1111-11-11") {
                    activityTemplate += 'Punched in : ' + fullDate + '';
                } else {
                    activityTemplate += 'Punched out : ' + fullDate + '';
                }
                activityTemplate += '</span>';
                activityTemplate += '</div>';
                activityTemplate += '</div>';
                activityTemplate += '<div class="portlet-body cls_traveldrouteData">';
                if (userId != loginUserId) {
                    activityTemplate += '<form class="form-horizontal" role="form" onsubmit="return false">';
                    activityTemplate += '<div class="form-group">';
                    activityTemplate += '<div class="col-md-8">';
                    activityTemplate += '<input type="search" class="form-control input-sm" style="width:172px;" placeholder="Send a Quick Message" id="id_message_data">';
                    activityTemplate += '</div>';
                    activityTemplate += '<div class="col-md-4">';
                    activityTemplate += '<button type="button" class="btn btn-sm btn-info" style="float:right;" onclick="messageSendTemplate(\'' + userId + '\');return false;"> SEND</button>';
                    activityTemplate += '</div>';
                    activityTemplate += '</div>';
                    activityTemplate += '</form>';
                }
                activityTemplate += '<ul class="nav nav-tabs">';
                activityTemplate += '<li class="active">';
                activityTemplate += '<a href="#tab_1_1" data-toggle="tab">Visits</a>';
                activityTemplate += '</li>';
                activityTemplate += '<li>';
                activityTemplate += '<a href="#tab_1_2" data-toggle="tab" onClick="expenseShowTemplate(\'' + userId + '\',\'' + fromDateForExpense + '\',\'' + toDateForExpense + '\')">Expenses</a>';
                activityTemplate += '</li>';
                activityTemplate += '</ul>';
                activityTemplate += '<div class="tab-content">';
                activityTemplate += '<div class="tab-pane fade active in" id="tab_1_1">';
                activityTemplate += '<form class="form-inline" role="form">';
                activityTemplate += '<div class="form-body" id="tab_hdr">';
                activityTemplate += '<div class="form-group">';
                activityTemplate += '<div class="col-md-6">';
                activityTemplate += '<div class="input-group date date-picker input-small" data-date-format="dd M yyyy">';
                activityTemplate += '<input type="text" disabled class="form-control" value="' + dateForSelection + '" id="id_activityForDate">';
                activityTemplate += '<span class="input-group-btn">';
                activityTemplate += '<button class="btn btn-default" type="button"><i class="fa fa-calendar"></i></button>';
                activityTemplate += '</span>';
                activityTemplate += '</div>';
                activityTemplate += '</div>';
                activityTemplate += '<div class="col-md-2">';
                activityTemplate += '<button data-toggle="modal" href="#responsive" type="button" class="btn btn-info" onclick="addActivityTemplate(\'' + userId + '\',\'' + userName + '\',\'' + true + '\',\'' + teamId + '\')"><i class="fa fa-plus"></i> Add Visit</button>';
                activityTemplate += '</div>';
                activityTemplate += '</div>';
                activityTemplate += '<div class="form-group">';
                activityTemplate += '<div class="col-md-12">';
                activityTemplate += '<label class="route-label"><a href="#" onclick="caliculateRouteOfUser(\'' + userId + '\',\'' + userName + '\',\'' + teamId + '\')">Show travelled route</a></label>';
                activityTemplate += '</div>';
                activityTemplate += '</div>';
                activityTemplate += '</div>';
                activityTemplate += '<div class="tab-pane2">';
                activityTemplate += '<div class="scroller" data-always-visible="1" data-rail-visible1="1" id="id_scrollerActivites">';
                activityTemplate += '<span id="id_toShowActivity">';
                activityTemplate += '</span>';
                activityTemplate += '</div>';
                activityTemplate += '</div>';
                activityTemplate += '</form>';
                activityTemplate += '</div>';
                activityTemplate += '<div class="tab-pane fade" id="tab_1_2">';
                activityTemplate += '<form class="form-inline" role="form">';
                activityTemplate += '<div class="form-body" id="tab_hdr">';
                activityTemplate += '<div class="form-group">';
                activityTemplate += '<div class="col-md-6">';
                activityTemplate += '<div id="reportrange" class="btn btn-default">';
                activityTemplate += '<span id="id_reportrange">';
                activityTemplate += '</span>';
                activityTemplate += '&nbsp;';
                activityTemplate += '<i class="fa fa-calendar"></i>';
                activityTemplate += '&nbsp;';
                activityTemplate += '<b class="fa fa-angle-down"></b>';
                activityTemplate += '</div>';
                activityTemplate += '</div>';
                activityTemplate += '</div>';
                activityTemplate += '<div class="form-group">';
                activityTemplate += '<div class="col-md-12">';
                //                activityTemplate += '<label class="appr-label" id="id_amountSubmitedAndApproved">Submitted <span>&#8377;</span> 2,300 | Approved <span>&#8377;</span> 700</label>';
                activityTemplate += '<label class="appr-label" id="id_amountSubmitedAndApproved"></label>';
                activityTemplate += '</div>';
                activityTemplate += '</div>';
                activityTemplate += '</div>';
                activityTemplate += '<div class="tab-pane2">';
                activityTemplate += '<div class="scroller" data-always-visible="1" data-rail-visible1="1" id="id_scrollerExpenses">';
                activityTemplate += '<span id="id_showuserExpenses">';
                activityTemplate += '</span>';
                activityTemplate += '</div>';
                activityTemplate += '</div>';
                activityTemplate += '</form>';
                activityTemplate += '</div>';
                activityTemplate += '</div>';
                activityTemplate += '</div>';
                activityTemplate += '</div>';
                $('.cls_usractivities').html(activityTemplate);
                activityShowTemplate(teamId, userId);
            }
            $('#pleaseWaitDialog').modal('hide');
            if (jQuery().datepicker) {
                $('.date-picker').datepicker({
                    rtl: App.isRTL(),
                    autoclose: true
                }).on('changeDate', function (en) {
                    activityShowTemplate(teamId, userId);
                });

                $('body').removeClass("modal-open"); // fix bug when inline picker is used in modal
            }

            $('#reportrange').daterangepicker(
            {
                ranges: {
                    'Today': [moment(), moment()],
                    'Yesterday': [moment().subtract('days', 1), moment().subtract('days', 1)],
                    'Last 7 Days': [moment().subtract('days', 6), moment()],
                    'Last 30 Days': [moment().subtract('days', 29), moment()],
                    'This Month': [moment().startOf('month'), moment().endOf('month')],
                    'Last Month': [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')]
                },
                startDate: moment().startOf('month'),
                endDate: moment()
            },
            function (start, end) {
                $('#reportrange span').html(start.format('D MMM YYYY') + ' - ' + end.format('D MMM YYYY') + ' ');
                expenseShowTemplate(userId, start.format('D MMM YYYY'), end.format('D MMM YYYY'))
            }
            );
            $('#id_reportrange').html(fromDateForExpense + ' - ' + toDateForExpense + ' ');
            $('#id_scrollerActivites').slimScroll({
                size: '7px',
                color: '#bbbbbb',
                opacity: .9,
                position: false ? 'left' : 'right',
                height: 700,
                allowPageScroll: false,
                disableFadeOut: false
            });
            $('#id_scrollerExpenses').slimScroll({
                size: '7px',
                color: '#bbbbbb',
                opacity: .9,
                position: false ? 'left' : 'right',
                height: 700,
                allowPageScroll: false,
                disableFadeOut: false
            });
            if (userId != loginUserId) {
                var sendMessage = document.getElementById("id_message_data");
                sendMessage.addEventListener("keydown", function (e) {
                    if (e.keyCode === 13) {
                        messageSendTemplate(userId);
                    }
                });
            }
        }
    });
}
// to display the assigned visits
function activityShowTemplate(teamId, userId) {

    //    $('#pleaseWaitDialog').modal('show');
    var now = document.getElementById("id_activityForDate").value;
    if (now.length == 0) {
        var now1 = new Date();
        now = new Date(now1.getFullYear(), now1.getMonth(), now1.getDate());
    }
    else {
        var dateToSplit = now.split(' ');
        var year = dateToSplit[2];
        var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
        var month1 = monthNames.indexOf(dateToSplit[1]);
        var day = dateToSplit[0];
        now = new Date(year, month1, day);
    }
    var dateToServer = convertLocalDateToServerDate(now.getFullYear(), now.getMonth(), now.getDate(), now.getHours(), now.getMinutes());
    var month = dateToServer.getMonth() + 1;
    if (month < 10) {
        month = '0' + month;
    }
    var date = dateToServer.getDate();
    if (date < 10) {
        date = '0' + date;
    }
    var hours = dateToServer.getHours();
    if (hours < 10) {
        hours = '0' + hours;
    }
    var minutes = dateToServer.getMinutes();
    if (minutes < 10) {
        minutes = '0' + minutes;
    }
    dateToServer = dateToServer.getFullYear() + "-" + month + "-" + date + " " + hours + ":" + minutes + ':00';
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/appointment/team/" + teamId + "/user/" + userId + "/" + dateToServer,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            var activityShowTemplateHtml = '';
            if (data.errorCode == '0000') {
                var activityData = data.result;
                if (activityData.length != 0) {
                    for (var i = 0; i < activityData.length; i++) {
                        var activityId = activityData[i].id;
                        var activity = activityData[i].title;
                        var activityPurpose=  activityData[i].purpose.purpose;
                        if(activity==null || activity=="")
                        {
                            activity=activityPurpose;
                        }
                        var activityDescription = activityData[i].description;
                        var customerLocation = activityData[i].customer.customerLocation;
                        var activityOutCome = activityData[i].outcomes.id;
			var visitStatus = activityData[i].status;
                        
                        // Added by Jyoti 06-03-2017
                        var activityRecurringID  = activityData[i].appointmentRecurringList.id;
                        var activityIsRecurring = activityData[i].isRecurring;
                        var setIsRecurring;
                        var onClickDeleteAction;
                        if(activityIsRecurring == 1){
                            setIsRecurring = "Recurring Visit";
                            onClickDeleteAction = "actionOnDeleteRecurringVisit("+activityRecurringID+","+activityId+","+loginUserId+","+userId+","+teamId+",\'"+activity+"\');";
                        }else{
                            setIsRecurring = "";
                            onClickDeleteAction = "deleteVisit("+activityId+","+loginUserId+","+userId+","+teamId+",\'"+activity+"\');";
                        }
                        // Ended by Jyoti 06-03-2017
                        var activityDateTime = activityData[i].sdateTime;
                        activityDateTime = activityDateTime.split(' ');
                        var activityDate = activityDateTime[0];
                        activityDate = activityDate.split('-');
                        var activityTime = activityDateTime[1];
                        activityTime = activityTime.split(':');
                        var activityDateJs = convertServerDateToLocalDate(activityDate[0], activityDate[1] - 1, activityDate[2], activityTime[0], activityTime[1]);
                        activityDateTime = activityDateJs.getFullYear() + "-" + (activityDateJs.getMonth() + 1) + "-" + activityDateJs.getDate() + " " + activityDateJs.getHours() + ":" + activityDateJs.getMinutes() + ":00.0";
                        activityDateTime = fieldSenseseForJSDate(activityDateTime);
                        var activtyOfCustomer = activityData[i].customer.customerPrintas;
                        var activityStatusClass = '';
                        if (activityOutCome == 0) {
                            activityStatusClass = 'form-group stat_blnk opt';
                        }
                        else if (activityOutCome == 1) {
                            activityStatusClass = 'form-group success opt';
                        } else if (activityOutCome == 2) {
                            activityStatusClass = 'form-group failed opt';
                        }
                        activityShowTemplateHtml += '<div class="form-body">';
                        activityShowTemplateHtml += '<div class="' + activityStatusClass + '">';
                        activityShowTemplateHtml += '<label class="col-md-12 isRecurring-label" style="color:orange; text-align: left !important; font-size:11px;">' + setIsRecurring + '</label>'; // Added by jyoti
                        activityShowTemplateHtml += '<label class="col-md-12 time-label">' + activityDateTime + '</label>';
			activityShowTemplateHtml += '<div class="action">'; 
                        //changed by jyoti
                        if(activityIsRecurring == 1){
                            activityShowTemplateHtml += '<button type="button" class="btn btn-info tooltips" data-toggle="modal" href="#responsive" data-placement="bottom" title="Edit" onclick="editThisAndFollowingRecurringVisit(\'' + activityRecurringID + '\',\'' + activityId + '\',\'' + teamId + '\',\'' + userId + '\');"><i class="fa fa-pencil"></i></button>';
//                            activityShowTemplateHtml += '<button type="button" class="btn btn-info tooltips" data-toggle="modal" data-placement="bottom" title="Edit" onclick="actionOnEditRecurringVisit(\'' + activityRecurringID + '\',\'' + activityId + '\',\'' + teamId + '\',\'' + userId + '\');"><i class="fa fa-pencil"></i></button>';
                        }else{
                            activityShowTemplateHtml += '<button type="button" class="btn btn-info tooltips" data-toggle="modal" href="#responsive" data-placement="bottom" title="Edit" onClick="editActivityTemplate(\'' + activityId + '\',\'' + teamId + '\',\'' + userId + '\')"><i class="fa fa-pencil"></i></button>';
                        }
                        //ended by jyoti
                        if(visitStatus==0 || visitStatus==3){
                            // Changed by jyoti
				activityShowTemplateHtml += '<button type="button" class="btn btn-info tooltips" data-toggle="modal"  data-placement="bottom" title="Delete" onclick='+onClickDeleteAction+'><i class="fa fa-times"></i></button>';			
                            // ended by jyoti
                        }
			activityShowTemplateHtml += '</div>';
                        activityShowTemplateHtml += '<label class="col-md-12 title-label"><a>' + activity + '</a></label>';
                        activityShowTemplateHtml += '<label class="col-md-12 loc-label">' + customerLocation + '</label>';
                        activityShowTemplateHtml += '<label class="col-md-12 dsc-label">' + activityDescription + '';
                        activityShowTemplateHtml += '</label>';
                        activityShowTemplateHtml += '</div>';
                        activityShowTemplateHtml += '</div>';
                    }
                } else {
                    activityShowTemplateHtml += ' No activites for user ';
                }
            }
            $('#id_toShowActivity').html(activityShowTemplateHtml);
		jQuery('.tooltips').tooltip();
        //            $('#pleaseWaitDialog').modal('hide');
        }
    });
}

function expenseShowTemplate(userId, fromDate, toDate) {
    $('#pleaseWaitDialog').modal('show');
    var splitFromDate = fromDate.split(' ');
    var fromDateYear = splitFromDate[2];
    var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    var fromDateMonth = monthNames.indexOf(splitFromDate[1]);
    var fromDateDate = splitFromDate[0];
    fromDate = convertLocalDateToServerDate(fromDateYear, fromDateMonth, fromDateDate, 0, 0);
    var month = fromDate.getMonth() + 1;
    if (month < 10) {
        month = '0' + month;
    }
    var date = fromDate.getDate();
    if (date < 10) {
        date = '0' + date;
    }
    var hours = fromDate.getHours();
    if (hours < 10) {
        hours = '0' + hours;
    }
    var minutes = fromDate.getMinutes();
    if (minutes < 10) {
        minutes = '0' + minutes;
    }
    fromDate = fromDate.getFullYear() + "-" + month + "-" + date + " " + hours + ":" + minutes + ':00';

    var splitToDate = toDate.split(' ');
    var toDateYear = splitToDate[2];
    var toDateMonth = monthNames.indexOf(splitToDate[1]);
    var toDateDate = splitToDate[0];
    toDate = convertLocalDateToServerDate(toDateYear, toDateMonth, toDateDate, 23, 59);
    month = toDate.getMonth() + 1;
    if (month < 10) {
        month = '0' + month;
    }
    date = toDate.getDate();
    if (date < 10) {
        date = '0' + date;
    }
    hours = toDate.getHours();
    if (hours < 10) {
        hours = '0' + hours;
    }
    minutes = toDate.getMinutes();
    if (minutes < 10) {
        minutes = '0' + minutes;
    }
    toDate = toDate.getFullYear() + "-" + month + "-" + date + " " + hours + ":" + minutes + ':59';
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/expense/userExpense/dateWaise/" + userId + "/" + fromDate + "/" + toDate,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            var expenseShowTemplate = '';
            if (data.errorCode == '0000') {
                var userExpensese = data.result;
                var amount = 0;
                var amountApproved = 0;
		expensearray=[];
                for (var i = 0; i < userExpensese.length; i++) {
		    expensearray.push({"expense_data":userExpensese[i]});
                    var expense = userExpensese[i];
                    var userId = expense.user.id;
                    var expenseTime = fieldSenseDateTimeForExpense(expense.expenseTime)
                    var expenseName = expense.expenseName;
                    var amountSpent = expense.amountSpent;
                    var expenseId = expense.id;
                    var expenseStatus = expense.status;
                    var customerName = expense.customerName;
                    var appointmentTitle = expense.appointment.title;
                    expenseName = expenseName.replace(/'/gi, "my11c");
                    var id = expenseId + globalSplit + expenseName + globalSplit + expenseTime + globalSplit + amountSpent + globalSplit + expenseStatus + globalSplit + customerName + globalSplit + userId + globalSplit + appointmentTitle;
                    expenseName = expenseName.replace(/my11c/g, "'");
                    amount = amount + amountSpent
                    if (expenseStatus == 1 || expenseStatus == 3) {
                        amountApproved = amountApproved + amountSpent;
                    }
                    var expenseStatusClass = '';
                   // if (expenseStatus == 0) {
                        expenseStatusClass = 'form-group stat_blnk';
                   // }
                   /*else if (expenseStatus == 1) {
                        expenseStatusClass = 'form-group success';
                    } else if (expenseStatus == 2) {
                        expenseStatusClass = 'form-group failed';
                    }else if (expenseStatus == 3) {
                        expenseStatusClass = 'form-group success';
                    }else if (expenseStatus == 4) {
                        expenseStatusClass = 'form-group failed';
                    }else if (expenseStatus == 5) {
                        expenseStatusClass = 'form-group success';
                    }*/

                    expenseShowTemplate += '<div class="form-body">';
                    expenseShowTemplate += '<div id="id_expenseStatus_' + expenseId + '" class="' + expenseStatusClass + '">';
                    expenseShowTemplate += '<label class="col-md-12 time-label">' + expenseTime + '</label>';
                    expenseShowTemplate += '<div class="col-md-12">';
                    expenseShowTemplate += '<label class="col-md-8 title-label">' + expenseName + '</label>';
                    expenseShowTemplate += '<label class="col-md-4 amt-label"><span>&#8377;</span>' + amountSpent + '</label>';
                    expenseShowTemplate += '</div>';
		   if(expenseStatus==0){
			expenseShowTemplate+='<label class="col-md-12 time-label" id="status_'+i+'">Status : Pending Approval by Reporting Head</label>';
		    }else if(expenseStatus==1){
			expenseShowTemplate+='<label class="col-md-12 time-label" id="status_'+i+'">Status : Pending Approval by Accounts</label>';
		    }else if(expenseStatus==2){
			expenseShowTemplate+='<label class="col-md-12 time-label" id="status_'+i+'">Status : Rejected by Reporting Head</label>';
		    }else if(expenseStatus==3){
			expenseShowTemplate+='<label class="col-md-12 time-label" id="status_'+i+'">Status : Approved</label>';
		    }else if(expenseStatus==4){
			expenseShowTemplate+='<label class="col-md-12 time-label" id="status_'+i+'">Status : Rejected by Accounts</label>';
		    }else if(expenseStatus==5){
			expenseShowTemplate+='<label class="col-md-12 time-label" id="status_'+i+'">Status : Disbursed</label>';
		    }
		   
                    expenseShowTemplate += '<div class="col-md-12" id="id_expensenmae' + expenseId + '">';
                    if (expenseStatus == 0 && loginUserId != userId) { //loginUserId == userId added by Awaneesh
                        expenseShowTemplate += '<button type="button" data-toggle="modal" href="#expenses" class="btn btn-sm btn-info" onClick="showExpenseTemplate(' + i + ');return false;">Approve / Reject</button>';
                    } else {
                        expenseShowTemplate += '<button type="button" data-toggle="modal" href="#expenses" class="btn btn-sm btn-info" onClick="showExpenseTemplate(' + i + ');return false;">View</button>';
                    }
                    expenseShowTemplate += '</div>';
                    expenseShowTemplate += '</div>';
                    expenseShowTemplate += '</div>';
                }
                var id_amountSubmitedAndApprovedHtml = 'Submitted <span>&#8377;</span> ' + amount + ' | Approved <span>&#8377;</span> <span id="id_amountapproved">' + amountApproved + '</span>';
                $('#id_amountSubmitedAndApproved').html(id_amountSubmitedAndApprovedHtml);
            }
            $('#id_showuserExpenses').html(expenseShowTemplate)
            $('#pleaseWaitDialog').modal('hide');
        }
    });
}
function messageSendTemplate(userId) {
    var message = document.getElementById("id_message_data").value;
    if (message.trim().length == 0) {
        fieldSenseTosterError("Please enter message.", true);
        return false;
    }
    if (message.trim().length > 1000) {
        fieldSenseTosterError("Message should not be more than 1000 characters .", true);
        return false;
    }
    var reciverId = userId;
    if (reciverId == loginUserId) {
        fieldSenseTosterError("You can't send message to yourself.", true);
        return false;
    }
    $('#pleaseWaitDialog').modal('show');
    var messageObject = {
        "sender": {
            "id": loginUserId
        },
        "receiverId": reciverId,
        "messageType": 1,
        "message": message
    }
    var jsonData = JSON.stringify(messageObject);
    $.ajax({
        type: "POST",
        url: fieldSenseURL + '/message',
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            if (data.errorCode == '0000') {
                $('#id_message_data').val('');
                $('#pleaseWaitDialog').modal('hide');
            }
            else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}
function approveExpense(index) {
  //  var expenseDetails = id.split(globalSplit);
    var expenseId = expensearray[index].expense_data.id;
    var expenseName =expensearray[index].expense_data.expenseName;
    var expenseTime = expensearray[index].expense_data.expenseTime;
    var amountSpent = expensearray[index].expense_data.amountSpent;
    var expenseStatus = expensearray[index].expense_data.status;
   // var customerName = expenseDetails[5];
    var userId = expensearray[index].expense_data.user.id;
    
    //var appointmentTitle = expenseDetails[7];
    expenseName = expenseName.replace(/my11c/g, "'");
    $('#pleaseWaitDialog').modal('show');
    var expenseObject = {
        	"id":expenseId,
		"appointmentId":expensearray[index].expense_data.appointmentId,
		"customerId":expensearray[index].expense_data.customerId,
		"user":{
			"id":userId	
		},
		"expenseName":expenseName,
		"expenseType":expensearray[index].expense_data.expenseType,
		"description":expensearray[index].expense_data.description,
		"amountSpent":amountSpent,
		"status":1,
		"expenseTime":new Date(expensearray[index].expense_data.expenseTime.time),
		"submittedOn":new Date(expensearray[index].expense_data.submittedOn.time),
		//"approvedOrRejectedOn":new Date(expensearray[index].expense_data.approvedOrRejectedOn.time),
		"approvedOrRjectedBy":{
			"id":loginUserId,
		},
		//"rejectedReson":rej_reason,
		"createdOn":new Date(expensearray[index].expense_data.createdOn.time),
		"eCategoryName":{
			"id":expensearray[index].expense_data.eCategoryName.id
		},
		"exp_category_name":expensearray[index].expense_data.exp_category_name
		//"disburse_amount":dis_amt,
		//"payment_mode":pay_mode,
		//"default_date":default_date
    }
    var jsonData = JSON.stringify(expenseObject);
    $.ajax({
        type: "PUT",
        url: fieldSenseURL + '/expense/approve',
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            if (data.errorCode == '0000') {
                $(".cls_showexpense").modal('hide');
                expensearray[index].expense_data.status = 1;
		$("#status_"+index).text("Status : Pending Approval by Accounts");
               // id = expenseId + globalSplit + expenseName + globalSplit + expenseTime + globalSplit + amountSpent + globalSplit + expenseStatus + globalSplit + customerName + globalSplit + userId + globalSplit + appointmentTitle;
                $('#id_expensenmae' + expenseId).html('<button type="button" data-toggle="modal" href="#expenses" class="btn btn-sm btn-info" onClick="showExpenseTemplate(\'' + index + '\');return false;">View</button>');
               // $('#id_expenseStatus_' + expenseId).removeClass('stat_blnk');
               // $('#id_expenseStatus_' + expenseId).addClass('success');
                $('#pleaseWaitDialog').modal('hide');
                var totalAmountSpent = $('#id_amountapproved').text();
                totalAmountSpent = Number(totalAmountSpent) + Number(amountSpent);
                $('#id_amountapproved').html(totalAmountSpent);
		fieldSenseTosterSuccess(data.errorMessage, true);
            }
            else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}
function approveUserAllExpenses(userId) {
    $('#pleaseWaitDialog').modal('show');
    var expenseObject = {
        "user": {
            "id": userId
        },
        "approvedOrRjectedBy": {
            "id": loginUserId
        }
    }
    var jsonData = JSON.stringify(expenseObject);
    $.ajax({
        type: "PUT",
        url: fieldSenseURL + '/expense/approveAllUserExpenses',
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            if (data.errorCode == '0000') {
                $('#id_app_rej' + expenseId).css("display", "none");
                $('#pleaseWaitDialog').modal('hide');
            }
            else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}
function rejectExpense(index, hasRejectedReson) {
   // var expenseDetails = id.split(globalSplit);
    var expenseId = expensearray[index].expense_data.id;
    var expenseName =expensearray[index].expense_data.expenseName;
    var expenseTime = expensearray[index].expense_data.expenseTime;
    var amountSpent = expensearray[index].expense_data.amountSpent;
    var expenseStatus = expensearray[index].expense_data.status;
    //var customerName = expenseDetails[5];
    var userId = expensearray[index].expense_data.user.id;
   // var appointmentTitle = expenseDetails[7];
    expenseName = expenseName.replace(/my11c/g, "'");
    var rejectedReson = "";
    if (hasRejectedReson === 'true') {
        rejectedReson = $('#id_rejectedreason').val().trim();
        if (rejectedReson.length == 0) {
            fieldSenseTosterError("Rejected reason cannot be empty", true);
            return false;
        }
    }
    $('#pleaseWaitDialog').modal('show');
    var expenseObject = {
                "id":expenseId,
		"appointmentId":expensearray[index].expense_data.appointmentId,
		"customerId":expensearray[index].expense_data.customerId,
		"user":{
			"id":userId	
		},
		"expenseName":expenseName,
		"expenseType":expensearray[index].expense_data.expenseType,
		"description":expensearray[index].expense_data.description,
		"amountSpent":amountSpent,
		"status":2,
		"expenseTime":new Date(expensearray[index].expense_data.expenseTime.time),
		"submittedOn":new Date(expensearray[index].expense_data.submittedOn.time),
		//"approvedOrRejectedOn":new Date(expensearray[index].expense_data.approvedOrRejectedOn.time),
		"approvedOrRjectedBy":{
			"id":loginUserId,
		},
		"rejectedReson":rejectedReson,
		"createdOn":new Date(expensearray[index].expense_data.createdOn.time),
		"eCategoryName":{
			"id":expensearray[index].expense_data.eCategoryName.id
		},
		"exp_category_name":expensearray[index].expense_data.exp_category_name
		//"disburse_amount":dis_amt,
		//"payment_mode":pay_mode,
		//"default_date":default_date
    }
    var jsonData = JSON.stringify(expenseObject);
    $.ajax({
        type: "PUT",
        url: fieldSenseURL + '/expense/reject',
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            if (data.errorCode == '0000') {
                //                $(".cls_showexpense").modal('hide');
                $(".cls_userfeedback").modal('hide');
                expensearray[index].expense_data.status = 2;
		$("#status_"+index).text("Status : Rejected by Reporting Head");
                //id = expenseId + globalSplit + expenseName + globalSplit + expenseTime + globalSplit + amountSpent + globalSplit + expenseStatus + globalSplit + customerName + globalSplit + userId + globalSplit + appointmentTitle;
                $('#id_expensenmae' + expenseId).html('<button type="button" data-toggle="modal" href="#expenses" class="btn btn-sm btn-info" onClick="showExpenseTemplate(\'' + index + '\');return false;">View</button>');
               // $('#id_expenseStatus_' + expenseId).removeClass('stat_blnk');
              //  $('#id_expenseStatus_' + expenseId).addClass('failed');
                $('#pleaseWaitDialog').modal('hide');
		fieldSenseTosterSuccess(data.errorMessage, true);
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}
function rejectExpenseWithResonTemplate(index) {
    $(".cls_userfeedback").modal('show');
    var rejectExpenseWithResonTemplateHtml = '';
    rejectExpenseWithResonTemplateHtml += '<div class="modal-dialog">';
    rejectExpenseWithResonTemplateHtml += '<div class="modal-content">';
    rejectExpenseWithResonTemplateHtml += '<div class="modal-header">';
    rejectExpenseWithResonTemplateHtml += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
    rejectExpenseWithResonTemplateHtml += '<h4 class="modal-title">Expense Reject</h4>';
    rejectExpenseWithResonTemplateHtml += '</div>';
    //    userFeedbackTemplateHtml+='<form action="#" class="form-horizontal form-row-seperated">';
    rejectExpenseWithResonTemplateHtml += '<form action="#" class="form-horizontal">';
    rejectExpenseWithResonTemplateHtml += '<div class="modal-body">';
    rejectExpenseWithResonTemplateHtml += '<div class="scroller2" data-always-visible="1" data-rail-visible1="1">';
    rejectExpenseWithResonTemplateHtml += '<div class="form-body">';
    rejectExpenseWithResonTemplateHtml += '<div class="row">';
    rejectExpenseWithResonTemplateHtml += '<div class="col-md-12 form-group">';
    rejectExpenseWithResonTemplateHtml += '<label class="col-md-3 control-label">Reason</label>';
    rejectExpenseWithResonTemplateHtml += '<div class="col-md-9">';
    rejectExpenseWithResonTemplateHtml += '<textarea class="form-control" rows="5" placeholder="Enter here" id="id_rejectedreason"></textarea>';
    rejectExpenseWithResonTemplateHtml += '</div>';
    rejectExpenseWithResonTemplateHtml += '</div>';
    rejectExpenseWithResonTemplateHtml += '</div>';
    rejectExpenseWithResonTemplateHtml += '</div>';
    rejectExpenseWithResonTemplateHtml += '</div>';
    rejectExpenseWithResonTemplateHtml += '</div>';
    rejectExpenseWithResonTemplateHtml += '<div class="modal-footer">';
    rejectExpenseWithResonTemplateHtml += '<button type="button" class="btn btn-info" tabindex="17" onClick="rejectExpense(\'' + index + '\',\'' + true + '\')">OK</button>';
    rejectExpenseWithResonTemplateHtml += '<button type="button" data-dismiss="modal" class="btn btn-default" tabindex="18" onClick="rejectExpense(\'' + index + '\',\'' + false + '\')">Cancel</button>';
    rejectExpenseWithResonTemplateHtml += '</div>';
    rejectExpenseWithResonTemplateHtml += '</form>';
    rejectExpenseWithResonTemplateHtml += '</div>';
    rejectExpenseWithResonTemplateHtml += '</div>';
    $('.cls_userfeedback').html(rejectExpenseWithResonTemplateHtml);
}

// Added by jyoti 06-03-2017
function actionOnDeleteRecurringVisit(appointmentRecurringId, id, loginUserId, assignedTo, teamId, activity){
	
	bootbox.dialog({
  		message: "You want to delete only this visit / delete this and following visits ? ",
  		title: "Action on Delete Recurring Visit",
  		buttons: {
    			Onlythis: {
      				label: "Delete Only This visit",
      				className: "btn-default",
      				callback: function() {
        				deleteVisit(id,loginUserId, assignedTo,teamId,activity);
      				}
    			},
    			thisAndFollowing: {
    				label: "Delete This and Following visits",
      				className: "btn-default",
      				callback: function() {
					deleteThisAndFollowingRecurringVisit(appointmentRecurringId, id, loginUserId, assignedTo, teamId, activity);
      				}
    			}
  		}
	});
}

function deleteThisAndFollowingRecurringVisit(appointmentRecurringId, id, loginUserId, assignedTo, teamId, activity){
	
	bootbox.dialog({
  		message: "Are you sure you want to delete this and following visit ?",
  		title: "Delete Visit",
  		buttons: {
    			yes: {
      				label: "Yes",
      				className: "btn-default",
      				callback: function() {
        				$('#pleaseWaitDialog').modal('show');
					$.ajax({
        					type: "DELETE",
        					url: fieldSenseURL + '/appointment/' + 'followingRecurringVisits/' +appointmentRecurringId+ "/" +id+ "/" +loginUserId+ "/" +assignedTo+"/"+activity,
        					contentType: "application/json; charset=utf-8",
        					headers: {
            						"userToken": userToken
        					},
        					crossDomain: false,
        					cache: false,
        					dataType: 'json',
        					success: function (data) {
            						if (data.errorCode == '0000') {
								activityShowTemplate(teamId, assignedTo);
                						$('#pleaseWaitDialog').modal('hide');
								fieldSenseTosterSuccess(data.errorMessage, true);
            						}else {
                						$('#pleaseWaitDialog').modal('hide');
                						fieldSenseTosterError(data.errorMessage, true);
                						FieldSenseInvalidToken(data.errorMessage);
								if(data.errorMessage.indexOf("deleted")!=-1 || data.errorMessage.indexOf("status")!=-1){
									activityShowTemplate(teamId, assignedTo);
								}
            						}
        					},
						error: function(xhr, ajaxOptions, thrownError){	
							$('#pleaseWaitDialog').modal('hide');
							alert("in error:"+thrownError);
        					}
    					});
      				}
    			},
    			no: {
    				label: "No",
      				className: "btn-default",
      				callback: function() {
                                    
      				}
    			}
  		}
	});
}

// ended by jyoti 06-03-2017

function deleteVisit(id,loginUserId, assignedTo,teamId,activity){
	//var isDel= confirm("Are you sure you want to delete  the visit.");
       // if(isDel===false) 
	bootbox.dialog({
  		message: "Are you sure you want to delete  the visit ?",
  		title: "Delete Visit",
  		buttons: {
    			yes: {
      				label: "Yes",
      				className: "btn-default",
      				callback: function() {
        				$('#pleaseWaitDialog').modal('show');
					$.ajax({
        					type: "DELETE",
        					url: fieldSenseURL + '/appointment/'+id+"/"+loginUserId+"/"+assignedTo+"/"+activity,
        					contentType: "application/json; charset=utf-8",
        					headers: {
            						"userToken": userToken
        					},
        					crossDomain: false,
        					cache: false,
        					dataType: 'json',
        					success: function (data) {
            						if (data.errorCode == '0000') {
								activityShowTemplate(teamId, assignedTo);
                						$('#pleaseWaitDialog').modal('hide');
								fieldSenseTosterSuccess(data.errorMessage, true);
            						}else {
                						$('#pleaseWaitDialog').modal('hide');
                						fieldSenseTosterError(data.errorMessage, true);
                						FieldSenseInvalidToken(data.errorMessage);
								if(data.errorMessage.indexOf("deleted")!=-1 || data.errorMessage.indexOf("status")!=-1){
									activityShowTemplate(teamId, assignedTo);
								}
            						}
        					},
						error: function(xhr, ajaxOptions, thrownError){	
							$('#pleaseWaitDialog').modal('hide');
							alert("in error:"+thrownError);
        					}
    					});
      				}
    			},
    			no: {
    				label: "No",
      				className: "btn-default",
      				callback: function() {
					//$('#pleaseWaitDialog').modal('hide');
	       				//return false;
      				}
    			}
  		}
	});
}

// Added by Jyoti 06-03-2017
function actionOnEditRecurringVisit(appointmentRecurringId, appointmentId, teamId, user){
	
	bootbox.dialog({
  		message: "You want to edit only this visit / edit this and following visits ? ",
  		title: "Action on Edit Recurring Visit",
                href : "#responsive",
  		buttons: {
    			Onlythis: {
      				label: "Edit Only This visit",
      				className: "btn-default",
      				callback: function() {
//                                        $(this).attr("href","#responsive");
        				editActivityTemplate(appointmentId, teamId, user);
      				}
    			},
    			thisAndFollowing: {
    				label: "Edit This and Following visits",
      				className: "btn-default",
      				callback: function() {
					editThisAndFollowingRecurringVisit(appointmentRecurringId, appointmentId, teamId, user);
      				}
    			}
  		}
	});
}

// Added by Jyoti 07-03-2017
function editThisAndFollowingRecurringVisit(appointmentRecurringId, appointmentId, teamId, user){
    
    $('.cls_addActivity').html("");
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/appointment/" + appointmentId,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (dataActivity) {
            if (dataActivity.errorCode == '0000') {
                $.ajax({
                    type: "GET",
                    
                    url: fieldSenseURL + "/activityPurpose",
                    contentType: "application/json; charset=utf-8",
                    headers: {
                        "userToken": userToken
                    },
                    crossDomain: false,
                    cache: false,
                    dataType: 'json',
                    asyn: true,
                    success: function (data) {
                        if (data.errorCode == '0000') {
                            var purposeData = data.result;
                            $.ajax({
                                type: "GET",
                                url: fieldSenseURL + "/team/userPositions/" + loginUserId,
                                contentType: "application/json; charset=utf-8",
                                headers: {
                                    "userToken": userToken
                                },
                                crossDomain: false,
                                cache: false,
                                dataType: 'json',
                                asyn: true,
                                success: function (datateamMember) {
                                    if (datateamMember.errorCode == '0000') {
                                        var teamMemberData = datateamMember.result;
                                        $.ajax({
                                            type: "GET",
                                            url: fieldSenseURL + "/activityOutcome",
                                            contentType: "application/json; charset=utf-8",
                                            headers: {
                                                "userToken": userToken
                                            },
                                            crossDomain: false,
                                            cache: false,
                                            dataType: 'json',
                                            asyn: true,
                                            success: function (dataOutCome) {
                                                if (dataOutCome.errorCode == '0000') {
                                                    var activityData = dataActivity.result;
                                                    var customerId = activityData.customer.id;
                                                    $.ajax({
                                                        type: "GET",
                                                        url: fieldSenseURL + "/customerContact/customer/" + customerId,
                                                        contentType: "application/json; charset=utf-8",
                                                        headers: {
                                                            "userToken": userToken
                                                        },
                                                        crossDomain: false,
                                                        cache: false,
                                                        dataType: 'json',
                                                        asyn: true,
                                                        success: function (dataCustomerContact) {
                                                            if (dataCustomerContact.errorCode == '0000') {
                                                                var activityTitle = activityData.title;
                                                                var activityStatus = activityData.status;
                                                                var activityDescription = activityData.description;
                                                                var contactId = activityData.customerContact.id;
                                                                var customerPrint = activityData.customer.customerPrintas;
                                                                var customerName = activityData.customer.customerName;
                                                                var dateTime = activityData.sdateTime;
                                                                var endDateTime = activityData.sendTime;
                                                                var purposeId = activityData.purpose.id;
                                                                var purpose = activityData.purpose.purpose;
                                                                var ownerId = activityData.owner.id;
                                                                var ownerName = activityData.owner.firstName + ' ' + activityData.owner.lastName;
                                                                var outcomeId = activityData.outcomes.id;
                                                                var assignId = activityData.assignedTo.id;
                                                                var assigneeName = activityData.assignedTo.firstName + ' ' + activityData.assignedTo.lastName;
                                                                dateTime = dateTime.split(' ');
                                                                var starttDate = dateTime[0];
                                                                var splitStartDate = starttDate.split('-');
                                                                var startTime = dateTime[1];
                                                                var splitStartTime = startTime.split(':');
                                                                var startDateJs = convertServerDateToLocalDate(splitStartDate[0], splitStartDate[1] - 1, splitStartDate[2], splitStartTime[0], splitStartTime[1]);
                                                                var startDate = (startDateJs.getDate() < 10) ? '0' + startDateJs.getDate() : startDateJs.getDate();
                                                                var startMonth = (startDateJs.getMonth() < 9) ? '0' + (startDateJs.getMonth() + 1) : (startDateJs.getMonth() + 1);
                                                                starttDate = startDate + '-' + startMonth + '-' + startDateJs.getFullYear();
                                                                var startHr = startDateJs.getHours();
                                                                var startMin = startDateJs.getMinutes();
                                                                endDateTime = endDateTime.split(' ');
                                                                var endDate = endDateTime[0];
                                                                var splitEndDate = endDate.split('-');
                                                                var endTime = endDateTime[1];
                                                                var splitEndTime = endTime.split(':');
                                                                var endDateJs = convertServerDateToLocalDate(splitEndDate[0], splitEndDate[1] - 1, splitEndDate[2], splitEndTime[0], splitEndTime[1]);
                                                                var endHr = endDateJs.getHours();
                                                                var endMin = endDateJs.getMinutes();
                                                                var outData = dataOutCome.result;
                                                                var outcomeDescription = activityData.outcomeDescription;
                                                                
                                                                // Added by jyoti
                                                                var checkRecurring = activityData.isRecurring;                                                                
                                                                var getRecurrTillDate = activityData.appointmentRecurringList.recurrTill_Date_startTime;
                                                                getRecurrTillDate = getRecurrTillDate.split(' ');
                                                                var endtDate1 = getRecurrTillDate[0];
                                                                var splitendDate1 = endtDate1.split('-');
                                                                var endTime1 = getRecurrTillDate[1];
                                                                var splitendTime1 = endTime1.split(':');
                                                                var endDateJs1 = convertServerDateToLocalDate(splitendDate1[0], splitendDate1[1] - 1, splitendDate1[2], splitendTime1[0], splitendTime1[1]);
                                                                var endDate1 = (endDateJs1.getDate() < 10) ? '0' + endDateJs1.getDate() : endDateJs1.getDate();
                                                                var endMonth1 = (endDateJs1.getMonth() < 9) ? '0' + (endDateJs1.getMonth() + 1) : (endDateJs1.getMonth() + 1);
                                                                endtDate1 = endDate1 + '-' + endMonth1 + '-' + endDateJs1.getFullYear();
                                                                
                                                                var getRepeatType = activityData.appointmentRecurringList.repeatType;
                                                                var getRepeatAfterEvery = activityData.appointmentRecurringList.repeatAfterEvery;
                                                                var getRepeatOnDayWeekly = activityData.appointmentRecurringList.repeatOnDayWeeklyCSV;
                                                                var getRepeatOnDateMonthly = activityData.appointmentRecurringList.repeatOnDateMonthlyCSV;
                                                                //
                                                                var editActivityTemplateHtml = '';
                                                                editActivityTemplateHtml += '<div class="modal-dialog modal-wide">';
                                                                editActivityTemplateHtml += '<div class="modal-content">';
                                                                editActivityTemplateHtml += '<div class="modal-header">';
                                                                editActivityTemplateHtml += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
                                                                editActivityTemplateHtml += '<h4 class="modal-title">Edit Visit</h4>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '<form action="#" class="form-horizontal form-row-seperated">';
                                                                editActivityTemplateHtml += '<div class="modal-body">';
                                                                editActivityTemplateHtml += '<div class="scroller" data-always-visible="1" data-rail-visible1="1" id="id_editactivity">';
                                                                editActivityTemplateHtml += '<div class="form-body">';
                                                                editActivityTemplateHtml += '<div class="row">';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Customer Name<span style="color:red"> *</span></label>';
                                                                editActivityTemplateHtml += '<div class="col-md-7">';
                                                                editActivityTemplateHtml += '<input type="hidden" id="id_hiddenEdit" value="' + customerId + '"/>';
                                                                editActivityTemplateHtml += '<input type="Cname" name="currency" class="form-control" id="autocompleteCnm" placeholder="Enter text" value="' + customerName + '">';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Contact<span style="color:red"> *</span></label>';
                                                                editActivityTemplateHtml += '<div class="col-md-7">';
                                                                editActivityTemplateHtml += '<select class="form-control" id="id_contactEdit">';
                                                                var dataCustomerContact = dataCustomerContact.result;
                                                                editActivityTemplateHtml += '<option>None</option>';
                                                                for (var i = 0; i < dataCustomerContact.length; i++) {
                                                                    var contId = dataCustomerContact[i].id;
                                                                    var firstName = dataCustomerContact[i].firstName;
                                                                    var middleName = dataCustomerContact[i].middleName;
                                                                    var lastName = dataCustomerContact[i].lastName;
                                                                    var fullName = firstName + ' ' + middleName + ' ' + lastName;
                                                                    if (contactId == contId) {
                                                                        editActivityTemplateHtml += '<option value="' + contId + '" selected>' + fullName + '</option>';
                                                                    } else {
                                                                        editActivityTemplateHtml += '<option value="' + contId + '">' + fullName + '</option>';
                                                                    }
                                                                }
                                                                editActivityTemplateHtml += '</select>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '<div class="row">';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Title</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-7">';
                                                                editActivityTemplateHtml += '<input type="activity" class="form-control" placeholder="Enter text" id="id_activityEdit" value="' + activityTitle + '">';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Date</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-7">';
                                                                editActivityTemplateHtml += '<div class="input-group date date-picker" data-date-format="dd-mm-yyyy" data-date-start-date="-1m">';
                                                                editActivityTemplateHtml += '<input type="text" class="form-control" id="id_dateEdit" readonly value="' + starttDate + '">';
                                                                editActivityTemplateHtml += '<span class="input-group-btn">';
                                                                editActivityTemplateHtml += '<button class="btn btn-default" type="button"><i class="fa fa-calendar"></i></button>';
                                                                editActivityTemplateHtml += '</span>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '<div class="row">';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Start Time</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-3">';
                                                                editActivityTemplateHtml += '<select id="edit_Shr" class="form-control input-xssmall">';
                                                                editActivityTemplateHtml += '<option value="HH">HH</option>';
                                                                for (var j = 0; j < 24; j++) {
                                                                    if (j < 10)
                                                                    {
                                                                        if (startHr == j) {
                                                                            editActivityTemplateHtml += '<option value="' + j + '" selected>0' + j + '</option>';
                                                                        }
                                                                        else {
                                                                            editActivityTemplateHtml += '<option value="' + j + '">0' + j + '</option>';
                                                                        }
                                                                    } else {
                                                                        if (startHr == j) {
                                                                            editActivityTemplateHtml += '<option value="' + j + '" selected>' + j + '</option>';
                                                                        }
                                                                        else {
                                                                            editActivityTemplateHtml += '<option value="' + j + '">' + j + '</option>';
                                                                        }
                                                                    }
                                                                }
                                                                editActivityTemplateHtml += '</select></div>';
                                                                editActivityTemplateHtml += '<div class="col-md-3">';
                                                                editActivityTemplateHtml += '<select id="edit_Smin" class="form-control input-xssmall">';
                                                                editActivityTemplateHtml += '<option value="MM">MM</option>';
                                                                for (var k = 0; k < 60; k = k + 1) {
                                                                    if (k < 10)
                                                                    {
                                                                        if (startMin == k) {
                                                                            editActivityTemplateHtml += '<option value="' + k + '" selected>0' + k + '</option>';
                                                                        }
                                                                        else {
                                                                            editActivityTemplateHtml += '<option value="' + k + '">0' + k + '</option>';
                                                                        }
                                                                    } else {
                                                                        if (startMin == k) {
                                                                            editActivityTemplateHtml += '<option value="' + k + '" selected>' + k + '</option>';
                                                                        } else {
                                                                            editActivityTemplateHtml += '<option value="' + k + '">' + k + '</option>';
                                                                        }
                                                                    }
                                                                }
                                                                editActivityTemplateHtml += '</select>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">End Time</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-3">';
                                                                editActivityTemplateHtml += '<select id="edit_ehr" class="form-control input-xssmall">';
                                                                editActivityTemplateHtml += '<option value="HH">HH</option>';
                                                                for (var l = 0; l < 24; l++) {
                                                                    if (l < 10)
                                                                    {
                                                                        if (endHr == l) {
                                                                            editActivityTemplateHtml += '<option value="' + l + '" selected>0' + l + '</option>';
                                                                        }
                                                                        else {
                                                                            editActivityTemplateHtml += '<option value="' + l + '">0' + l + '</option>';
                                                                        }
                                                                    }
                                                                    else {
                                                                        if (endHr == l) {
                                                                            editActivityTemplateHtml += '<option value="' + l + '" selected>' + l + '</option>';
                                                                        } else {
                                                                            editActivityTemplateHtml += '<option value="' + l + '">' + l + '</option>';
                                                                        }
                                                                    }
                                                                }
                                                                editActivityTemplateHtml += '</select></div>';
                                                                editActivityTemplateHtml += '<div class="col-md-3">';
                                                                editActivityTemplateHtml += '<select id="edit_emin" class="form-control input-xssmall">';
                                                                editActivityTemplateHtml += '<option value="MM">MM</option>';
                                                                for (var m = 0; m < 60; m = m + 1) {
                                                                    if (m < 10)
                                                                    {
                                                                        if (endMin == m) {
                                                                            editActivityTemplateHtml += '<option value="' + m + '" selected>0' + m + '</option>';
                                                                        } else {
                                                                            editActivityTemplateHtml += '<option value="' + m + '">0' + m + '</option>';
                                                                        }
                                                                    } else {
                                                                        if (endMin == m) {
                                                                            editActivityTemplateHtml += '<option value="' + m + '" selected>' + m + '</option>';
                                                                        } else {
                                                                            editActivityTemplateHtml += '<option value="' + m + '">' + m + '</option>';
                                                                        }
                                                                    }
                                                                }
                                                                editActivityTemplateHtml += '</select>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '<div class="row">';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Purpose</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-7">';
                                                                editActivityTemplateHtml += '<select class="form-control" id="id_purposeEdit">';
                                                                for (var i = 0; i < purposeData.length; i++) {
                                                                    var id = purposeData[i].id;
                                                                    var purpose = purposeData[i].purpose;
                                                                    if (id == purposeId) {
                                                                        editActivityTemplateHtml += '<option value="' + id + '" selected>' + purpose + '</option>';
                                                                    } else {
                                                                        editActivityTemplateHtml += '<option value="' + id + '">' + purpose + '</option>';
                                                                    }
                                                                }
                                                                editActivityTemplateHtml += '</select>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '<div class="row">';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Owner</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-7">';
                                                                editActivityTemplateHtml += '<input type="Cname" name="currency" class="form-control cls_ownerEdit" id="' + ownerId + '" value="' + ownerName + '" disabled>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Assigned To</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-7">';
                                                                editActivityTemplateHtml += '<select class="form-control" id="id_assignedEdit">';
                                                                editActivityTemplateHtml += '<option>None</option>';
                                                                for (var i = 0; i < teamMemberData.length; i++) {
                                                                    var userId = teamMemberData[i].user.id;
                                                                    var firstName = teamMemberData[i].user.firstName;
                                                                    var lastName = teamMemberData[i].user.lastName;
                                                                    var fullName = firstName + ' ' + lastName;
                                                                    if (assignId == userId) {
                                                                        isAssigneeIsImmidiateSubOrdinate = true;
                                                                        editActivityTemplateHtml += '<option value="' + userId + '" selected>' + fullName + '</option>';
                                                                    }
                                                                    else {
                                                                        editActivityTemplateHtml += '<option value="' + userId + '">' + fullName + '</option>';
                                                                    }
                                                                }
                                                                if (!isAssigneeIsImmidiateSubOrdinate) {
                                                                    editActivityTemplateHtml += '<option value="' + assignId + '" selected>' + assigneeName + '</option>';
                                                                }
                                                                editActivityTemplateHtml += '</select>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '<div class="row">';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Status</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-7">';
                                                                editActivityTemplateHtml += '<select class="form-control" id="id_statusEdit" onchange="outcomeForAddActivity()">';
                                                                editActivityTemplateHtml += '<option>Select</option>';
                                                                if (activityStatus == 0) {
                                                                    editActivityTemplateHtml += '<option value="2">Completed</option>';
                                                                    editActivityTemplateHtml += '<option value="0" selected>Pending</option>';
                                                                    editActivityTemplateHtml += '<option value="1">In-Progress</option>';
                                                                    editActivityTemplateHtml += '<option value="3">Cancelled</option>';
                                                                }
                                                                if (activityStatus == 1) {
                                                                    editActivityTemplateHtml += '<option value="2">Completed</option>';
                                                                    editActivityTemplateHtml += '<option value="0">Pending</option>';
                                                                    editActivityTemplateHtml += '<option value="1" selected>In-Progress</option>';
                                                                    editActivityTemplateHtml += '<option value="3">Cancelled</option>';
                                                                }
                                                                if (activityStatus == 2) {
                                                                    editActivityTemplateHtml += '<option value="2" selected>Completed</option>';
                                                                    editActivityTemplateHtml += '<option value="0">Pending</option>';
                                                                    editActivityTemplateHtml += '<option value="1">In-Progress</option>';
                                                                    editActivityTemplateHtml += '<option value="3">Cancelled</option>';
                                                                }
                                                                if (activityStatus == 3) {
                                                                    editActivityTemplateHtml += '<option value="2">Completed</option>';
                                                                    editActivityTemplateHtml += '<option value="0">Pending</option>';
                                                                    editActivityTemplateHtml += '<option value="1">In-Progress</option>';
                                                                    editActivityTemplateHtml += '<option value="3" selected>Cancelled</option>';
                                                                }
                                                                editActivityTemplateHtml += '</select>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Outcome</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-7">';
                                                                editActivityTemplateHtml += '<select class="form-control" id="id_outcomeEdit">';
                                                                for (var i = 0; i < outData.length; i++) {
                                                                    var outId = outData[i].id;
                                                                    var out = outData[i].outcome;
                                                                    if (outId == outcomeId) {
                                                                        editActivityTemplateHtml += '<option value="' + outId + '" selected>' + out + '</option>';
                                                                    }
                                                                    else {
                                                                        editActivityTemplateHtml += '<option value="' + outId + '">' + out + '</option>';
                                                                    }
                                                                }
                                                                editActivityTemplateHtml += '</select>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                
                                                                //Added by Jyoti 30-01-2017
                                                                if(checkRecurring == 1){
                                                                editActivityTemplateHtml += '<div class="row">';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Recurring Till Date</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-7">';                    
                                                                editActivityTemplateHtml += '<div class="input-group date date-picker" data-date-format="dd-mm-yyyy" data-date-start-date="-1m">';
                                                                editActivityTemplateHtml += '<input type="text" class="form-control" id="end_dateEdit" readonly value="' + endtDate1 +'">';
                                                                editActivityTemplateHtml += '<span class="input-group-btn">';
                                                                editActivityTemplateHtml += '<button class="btn btn-default" type="button"><i class="fa fa-calendar"></i></button>';
                                                                editActivityTemplateHtml += '</span>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';

                                                                editActivityTemplateHtml += '<div class="row">';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Repeat Type</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-7">';
                                                                editActivityTemplateHtml += '<select class="form-control" id="editRepeat_type">';
                                                                if(getRepeatType == 11){
                                                                editActivityTemplateHtml += '<option value="11" selected >Daily</option>';    // if 11 auto select daily else if ..
                                                                editActivityTemplateHtml += '<option value="22" >Weekly</option>';
                                                                editActivityTemplateHtml += '<option value="33" >Monthly</option>';
                                                                }
                                                                else if(getRepeatType == 22){
                                                                editActivityTemplateHtml += '<option value="22" selected >Weekly</option>';
                                                                editActivityTemplateHtml += '<option value="11" >Daily</option>';
                                                                editActivityTemplateHtml += '<option value="33" >Monthly</option>';
                                                                }
                                                                else if(getRepeatType == 33){
                                                                editActivityTemplateHtml += '<option value="33" selected >Monthly</option>';
                                                                editActivityTemplateHtml += '<option value="22" >Weekly</option>';
                                                                editActivityTemplateHtml += '<option value="11" >Daily</option>';
                                                                }
                                                                editActivityTemplateHtml += '</select>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';                                                                
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Repeat After Every</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-7">';
                                                                editActivityTemplateHtml += '<select class="form-control" id="editRepeatAfterEvery" >';                                                                                                                     
                                                                    for (var i = 1; i <= 31; i++) {
                                                                        if(i == getRepeatAfterEvery){
                                                                                editActivityTemplateHtml += '<option selected="selected" value="' + i + '">' + i + '</option>';
                                                                                continue;
                                                                        }                                                                      
                                                                        editActivityTemplateHtml += '<option value="' + i + '">' + i + '</option>';
                                                                    }
                                                                editActivityTemplateHtml += '</select>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';

                                                                editActivityTemplateHtml += '<div class="row">';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Repeat On Day(Weekly) </label>';
                                                                editActivityTemplateHtml += '<div class="col-md-7">';
                                                                editActivityTemplateHtml += '<select class="form-control" id="editRepeatOnDay_weekly" multiple="multiple" >';
//                                                                editActivityTemplateHtml += '<option>Select</option>';
                                                                
                                                                    var array_getRepeatOnDayWeekly = getRepeatOnDayWeekly.split(',');
                                                                    var weekdaysName = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"];                                                                    
                                                                    for(var i=0; i<=6; i++){
                                                                        var selected = "";
                                                                        for(var j=0; j< array_getRepeatOnDayWeekly.length; j++){                                                                        
                                                                            if(i == array_getRepeatOnDayWeekly[j]){
                                                                                selected = "selected";
                                                                                break;
                                                                            }
                                                                        }
                                                                        editActivityTemplateHtml += '<option '+selected+' value="'+i+'" >'+weekdaysName[i]+'</option>';
                                                                    }
                                                                    
                                                                editActivityTemplateHtml += '</select>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Repeat On Date (Monthly)</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-7">';                    
                                                                editActivityTemplateHtml += '<select class="form-control" id="editRepeatOnDate_monthly" multiple="multiple" >';
//                                                                editActivityTemplateHtml += '<option>Select</option>';
                                                                
                                                                    var array_getRepeatOnDateMonthly = getRepeatOnDateMonthly.split(',');                                                                
                                                                    for (var i = 1; i <= 31; i++) {
                                                                        var selected = "";
                                                                        for(var j=0; j< array_getRepeatOnDateMonthly.length;j++){
                                                                            var a = array_getRepeatOnDateMonthly[j];
                                                                            if(i == a){
                                                                                selected = "selected";
                                                                                break;
                                                                            }
                                                                        }  
                                                                        editActivityTemplateHtml += '<option '+selected+' value="' + i + '">' + i + '</option>';
                                                                    }
                                                                    
                                                                editActivityTemplateHtml += '</select>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                }
                                                                //Ended by jyoti
                                                                
                                                                editActivityTemplateHtml += '<div class="row">';
                                                                editActivityTemplateHtml += '<div class="form-group" id="dscp">';
                                                                editActivityTemplateHtml += '<label class="col-md-3 control-label">Description</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-9">';
                                                                editActivityTemplateHtml += '<textarea class="form-control" rows="3" placeholder="Enter text" id="id_descriptionEdit">' + activityDescription + '</textarea>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';

                                                                editActivityTemplateHtml += '<span id="id_outcomedescriptiontextbox" style="display:block;">';
                                                                editActivityTemplateHtml += '<div class="row">';
                                                                editActivityTemplateHtml += '<div class="col-md-12 form-group" id="dscp">';
                                                                editActivityTemplateHtml += '<label class="col-md-3 control-label">Outcome Description</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-9">';
                                                                editActivityTemplateHtml += '<textarea class="form-control" rows="3" placeholder="Enter text" id="id_outcomedescription">' + outcomeDescription + '</textarea>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</span>';

                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '<div class="modal-footer">';
                                                                if (isAssigneeIsImmidiateSubOrdinate) {
                                                                    editActivityTemplateHtml += '<button type="submit" class="btn btn-info" onclick="editThisAndFollowingActivity(\'' + endtDate1 + '\',\'' + getRepeatType + '\',\'' + getRepeatAfterEvery + '\',\'' + getRepeatOnDayWeekly + '\',\'' + getRepeatOnDateMonthly + '\',\'' + appointmentRecurringId + '\',\'' + appointmentId + '\',\'' + teamId + '\',\'' + user + '\');return false;">Save</button>';
                                                                }
                                                                editActivityTemplateHtml += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</form>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                $('.cls_addActivity').html(editActivityTemplateHtml);
                                                                if (!isAssigneeIsImmidiateSubOrdinate) {
                                                                    $('#id_assignedEdit').prop("disabled", true);
                                                                }
                                                                
                                                                $('#autocompleteCnm').autocomplete({
								    minChars:3,
                                                                    //lookup: customer,
								    paramName: 'searchText',
    								    transformResult: function(response) {
        								return {
            									suggestions: $.map(response.result, function(dataItem) {
                								return { value: dataItem.value, id:dataItem.customerId  };
            									})
        								};
    								    },
		        					    dataType:"json",
								    serviceUrl: fieldSenseURL + '/customer/visit?userToken='+userToken,
                                                                    onSelect: function (suggestion) {
                                                                        $('#id_hiddenEdit').val(suggestion.id);
                                                                        customerContacts(suggestion.id)
                                                                    },
                        					   /* onSearchStart: function (query) {
								 		$('#pleaseWaitDialog').modal('show');
	                					    },
		        					    onSearchComplete: function (query, suggestions) {
										$('#pleaseWaitDialog').modal('hide');
		        					    }*/
                                                                });
                                                                if (jQuery().datepicker) {
                                                                    $('.date-picker').datepicker({
                                                                        rtl: App.isRTL(),
                                                                        autoclose: true
                                                                    });
                                                                    $('body').removeClass("modal-open"); // fix bug when inline picker is used in modal
                                                                }
                                                                $('#id_editactivity').slimScroll({
                                                                    size: '7px',
                                                                    color: '#bbbbbb',
                                                                    opacity: .9,
                                                                    position: false ? 'left' : 'right',
                                                                    height: 420,
                                                                    allowPageScroll: false,
                                                                    disableFadeOut: false
                                                                });
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                },
            			error: function(xhr, ajaxOptions, thrownError){	
					$('#pleaseWaitDialog').modal('hide');
					alert("error in ajax call : " + thrownError);
                			//toggleButton(el);
            			},
                            });
                        }
                    },
		    error: function(xhr, ajaxOptions, thrownError){	
			$('#pleaseWaitDialog').modal('hide');
			alert("error in ajax call : " + thrownError);
                	//toggleButton(el);
            	    },
                });
            }else {
		fieldSenseTosterError(dataActivity.errorMessage, true);
                FieldSenseInvalidToken(dataActivity.errorMessage);
		$(".cls_addActivity").modal('hide');
		if(dataActivity.errorMessage.indexOf("deleted")!=-1){
			activityShowTemplate(teamId, user);
		}
            }
        },
	error: function(xhr, ajaxOptions, thrownError){	
		$('#pleaseWaitDialog').modal('hide');
		alert("error in ajax call : " + thrownError);
                //toggleButton(el);
        },
    });
    $('#pleaseWaitDialog').modal('hide');
}

// Ended by Jyoti

function editActivityTemplate(appointmentId, teamId, user) {
    $('.cls_addActivity').html("");
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/appointment/" + appointmentId,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (dataActivity) {
            if (dataActivity.errorCode == '0000') {
                $.ajax({
                    type: "GET",
                    
                    url: fieldSenseURL + "/activityPurpose",
                    contentType: "application/json; charset=utf-8",
                    headers: {
                        "userToken": userToken
                    },
                    crossDomain: false,
                    cache: false,
                    dataType: 'json',
                    asyn: true,
                    success: function (data) {
                        if (data.errorCode == '0000') {
                            var purposeData = data.result;
                            $.ajax({
                                type: "GET",
                                url: fieldSenseURL + "/team/userPositions/" + loginUserId,
                                contentType: "application/json; charset=utf-8",
                                headers: {
                                    "userToken": userToken
                                },
                                crossDomain: false,
                                cache: false,
                                dataType: 'json',
                                asyn: true,
                                success: function (datateamMember) {
                                    if (datateamMember.errorCode == '0000') {
                                        var teamMemberData = datateamMember.result;
                                        $.ajax({
                                            type: "GET",
                                            url: fieldSenseURL + "/activityOutcome",
                                            contentType: "application/json; charset=utf-8",
                                            headers: {
                                                "userToken": userToken
                                            },
                                            crossDomain: false,
                                            cache: false,
                                            dataType: 'json',
                                            asyn: true,
                                            success: function (dataOutCome) {
                                                if (dataOutCome.errorCode == '0000') {
                                                    var activityData = dataActivity.result;
                                                    var customerId = activityData.customer.id;
                                                    $.ajax({
                                                        type: "GET",
                                                        url: fieldSenseURL + "/customerContact/customer/" + customerId,
                                                        contentType: "application/json; charset=utf-8",
                                                        headers: {
                                                            "userToken": userToken
                                                        },
                                                        crossDomain: false,
                                                        cache: false,
                                                        dataType: 'json',
                                                        asyn: true,
                                                        success: function (dataCustomerContact) {
                                                            if (dataCustomerContact.errorCode == '0000') {
                                                                var activityTitle = activityData.title;
                                                                var activityStatus = activityData.status;
                                                                var activityDescription = activityData.description;
                                                                var contactId = activityData.customerContact.id;
                                                                var customerPrint = activityData.customer.customerPrintas;
                                                                var customerName = activityData.customer.customerName;
                                                                var dateTime = activityData.sdateTime;
                                                                var endDateTime = activityData.sendTime;
                                                                var purposeId = activityData.purpose.id;
                                                                var purpose = activityData.purpose.purpose;
                                                                var ownerId = activityData.owner.id;
                                                                var ownerName = activityData.owner.firstName + ' ' + activityData.owner.lastName;
                                                                var outcomeId = activityData.outcomes.id;
                                                                var assignId = activityData.assignedTo.id;
                                                                var assigneeName = activityData.assignedTo.firstName + ' ' + activityData.assignedTo.lastName;
                                                                dateTime = dateTime.split(' ');
                                                                var starttDate = dateTime[0];
                                                                var splitStartDate = starttDate.split('-');
                                                                var startTime = dateTime[1];
                                                                var splitStartTime = startTime.split(':');
                                                                var startDateJs = convertServerDateToLocalDate(splitStartDate[0], splitStartDate[1] - 1, splitStartDate[2], splitStartTime[0], splitStartTime[1]);
                                                                var startDate = (startDateJs.getDate() < 10) ? '0' + startDateJs.getDate() : startDateJs.getDate();
                                                                var startMonth = (startDateJs.getMonth() < 9) ? '0' + (startDateJs.getMonth() + 1) : (startDateJs.getMonth() + 1);
                                                                starttDate = startDate + '-' + startMonth + '-' + startDateJs.getFullYear();
                                                                var startHr = startDateJs.getHours();
                                                                var startMin = startDateJs.getMinutes();
                                                                endDateTime = endDateTime.split(' ');
                                                                var endDate = endDateTime[0];
                                                                var splitEndDate = endDate.split('-');
                                                                var endTime = endDateTime[1];
                                                                var splitEndTime = endTime.split(':');
                                                                var endDateJs = convertServerDateToLocalDate(splitEndDate[0], splitEndDate[1] - 1, splitEndDate[2], splitEndTime[0], splitEndTime[1]);
                                                                var endHr = endDateJs.getHours();
                                                                var endMin = endDateJs.getMinutes();
                                                                var outData = dataOutCome.result;
                                                                var outcomeDescription = activityData.outcomeDescription;
//                                                                // Added by jyoti
//                                                                var checkRecurring = activityData.isRecurring;
//                                                                var getRecurrTillDate = activityData.appointmentRecurringList.endDateStartTime;
////                                                                getRecurrTillDate = getRecurrTillDate.split(' ');
////                                                                var recurrTillEDate = getRecurrTillDate[0];
////                                                                var splitEDate = recurrTillEDate.split('-');
////                                                                var startTime = getRecurrTillDate[1];
////                                                                var splitStartTime = startTime.split(':');
////                                                                var startDateJs = convertServerDateToLocalDate(splitEDate[0], splitEDate[1] - 1, splitEDate[2], splitEDate[0], splitEDate[1]);
////                                                                var startDate = (startDateJs.getDate() < 10) ? '0' + startDateJs.getDate() : startDateJs.getDate();
////                                                                var startMonth = (startDateJs.getMonth() < 9) ? '0' + (startDateJs.getMonth() + 1) : (startDateJs.getMonth() + 1);
////                                                                recurrTillEDate = startDate + '-' + startMonth + '-' + startDateJs.getFullYear();
////                                                                
//                                                                var getRepeatType = activityData.appointmentRecurringList.repeatType;
//                                                                var getRepeatAfterEvery = activityData.appointmentRecurringList.repeatAfterEvery;
//                                                                var getRepeatOnDayWeekly = activityData.appointmentRecurringList.repeatOnDayWeeklyCSV;
//                                                                var getRepeatOnDateMonthly = activityData.appointmentRecurringList.repeatOnDateMonthlyCSV;                                                                
//                                                                var getAppointmentsRecurringListIdFk = activityData.appointmentRecurringList.id;
////                                                                alert(getRecurrTillDate);
//                                                                //
                                                                var editActivityTemplateHtml = '';
                                                                editActivityTemplateHtml += '<div class="modal-dialog modal-wide">';
                                                                editActivityTemplateHtml += '<div class="modal-content">';
                                                                editActivityTemplateHtml += '<div class="modal-header">';
                                                                editActivityTemplateHtml += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
                                                                editActivityTemplateHtml += '<h4 class="modal-title">Edit Visit</h4>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '<form action="#" class="form-horizontal form-row-seperated">';
                                                                editActivityTemplateHtml += '<div class="modal-body">';
                                                                editActivityTemplateHtml += '<div class="scroller" data-always-visible="1" data-rail-visible1="1" id="id_editactivity">';
                                                                editActivityTemplateHtml += '<div class="form-body">';
                                                                editActivityTemplateHtml += '<div class="row">';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Customer Name<span style="color:red"> *</span></label>';
                                                                editActivityTemplateHtml += '<div class="col-md-7">';
                                                                editActivityTemplateHtml += '<input type="hidden" id="id_hiddenEdit" value="' + customerId + '"/>';
                                                                editActivityTemplateHtml += '<input type="Cname" name="currency" class="form-control" id="autocompleteCnm" placeholder="Enter text" value="' + customerName + '">';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Contact<span style="color:red"> *</span></label>';
                                                                editActivityTemplateHtml += '<div class="col-md-7">';
                                                                editActivityTemplateHtml += '<select class="form-control" id="id_contactEdit">';
                                                                var dataCustomerContact = dataCustomerContact.result;
                                                                editActivityTemplateHtml += '<option>None</option>';
                                                                for (var i = 0; i < dataCustomerContact.length; i++) {
                                                                    var contId = dataCustomerContact[i].id;
                                                                    var firstName = dataCustomerContact[i].firstName;
                                                                    var middleName = dataCustomerContact[i].middleName;
                                                                    var lastName = dataCustomerContact[i].lastName;
                                                                    var fullName = firstName + ' ' + middleName + ' ' + lastName;
                                                                    if (contactId == contId) {
                                                                        editActivityTemplateHtml += '<option value="' + contId + '" selected>' + fullName + '</option>';
                                                                    } else {
                                                                        editActivityTemplateHtml += '<option value="' + contId + '">' + fullName + '</option>';
                                                                    }
                                                                }
                                                                editActivityTemplateHtml += '</select>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '<div class="row">';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Title</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-7">';
                                                                editActivityTemplateHtml += '<input type="activity" class="form-control" placeholder="Enter text" id="id_activityEdit" value="' + activityTitle + '">';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Date</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-7">';
                                                                editActivityTemplateHtml += '<div class="input-group date date-picker" data-date-format="dd-mm-yyyy" data-date-start-date="-1m">';
                                                                editActivityTemplateHtml += '<input type="text" class="form-control" id="id_dateEdit" readonly value="' + starttDate + '">';
                                                                editActivityTemplateHtml += '<span class="input-group-btn">';
                                                                editActivityTemplateHtml += '<button class="btn btn-default" type="button"><i class="fa fa-calendar"></i></button>';
                                                                editActivityTemplateHtml += '</span>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '<div class="row">';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Start Time</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-3">';
                                                                editActivityTemplateHtml += '<select id="edit_Shr" class="form-control input-xssmall">';
                                                                editActivityTemplateHtml += '<option value="HH">HH</option>';
                                                                for (var j = 0; j < 24; j++) {
                                                                    if (j < 10)
                                                                    {
                                                                        if (startHr == j) {
                                                                            editActivityTemplateHtml += '<option value="' + j + '" selected>0' + j + '</option>';
                                                                        }
                                                                        else {
                                                                            editActivityTemplateHtml += '<option value="' + j + '">0' + j + '</option>';
                                                                        }
                                                                    } else {
                                                                        if (startHr == j) {
                                                                            editActivityTemplateHtml += '<option value="' + j + '" selected>' + j + '</option>';
                                                                        }
                                                                        else {
                                                                            editActivityTemplateHtml += '<option value="' + j + '">' + j + '</option>';
                                                                        }
                                                                    }
                                                                }
                                                                editActivityTemplateHtml += '</select></div>';
                                                                editActivityTemplateHtml += '<div class="col-md-3">';
                                                                editActivityTemplateHtml += '<select id="edit_Smin" class="form-control input-xssmall">';
                                                                editActivityTemplateHtml += '<option value="MM">MM</option>';
                                                                for (var k = 0; k < 60; k = k + 1) {
                                                                    if (k < 10)
                                                                    {
                                                                        if (startMin == k) {
                                                                            editActivityTemplateHtml += '<option value="' + k + '" selected>0' + k + '</option>';
                                                                        }
                                                                        else {
                                                                            editActivityTemplateHtml += '<option value="' + k + '">0' + k + '</option>';
                                                                        }
                                                                    } else {
                                                                        if (startMin == k) {
                                                                            editActivityTemplateHtml += '<option value="' + k + '" selected>' + k + '</option>';
                                                                        } else {
                                                                            editActivityTemplateHtml += '<option value="' + k + '">' + k + '</option>';
                                                                        }
                                                                    }
                                                                }
                                                                editActivityTemplateHtml += '</select>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">End Time</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-3">';
                                                                editActivityTemplateHtml += '<select id="edit_ehr" class="form-control input-xssmall">';
                                                                editActivityTemplateHtml += '<option value="HH">HH</option>';
                                                                for (var l = 0; l < 24; l++) {
                                                                    if (l < 10)
                                                                    {
                                                                        if (endHr == l) {
                                                                            editActivityTemplateHtml += '<option value="' + l + '" selected>0' + l + '</option>';
                                                                        }
                                                                        else {
                                                                            editActivityTemplateHtml += '<option value="' + l + '">0' + l + '</option>';
                                                                        }
                                                                    }
                                                                    else {
                                                                        if (endHr == l) {
                                                                            editActivityTemplateHtml += '<option value="' + l + '" selected>' + l + '</option>';
                                                                        } else {
                                                                            editActivityTemplateHtml += '<option value="' + l + '">' + l + '</option>';
                                                                        }
                                                                    }
                                                                }
                                                                editActivityTemplateHtml += '</select></div>';
                                                                editActivityTemplateHtml += '<div class="col-md-3">';
                                                                editActivityTemplateHtml += '<select id="edit_emin" class="form-control input-xssmall">';
                                                                editActivityTemplateHtml += '<option value="MM">MM</option>';
                                                                for (var m = 0; m < 60; m = m + 1) {
                                                                    if (m < 10)
                                                                    {
                                                                        if (endMin == m) {
                                                                            editActivityTemplateHtml += '<option value="' + m + '" selected>0' + m + '</option>';
                                                                        } else {
                                                                            editActivityTemplateHtml += '<option value="' + m + '">0' + m + '</option>';
                                                                        }
                                                                    } else {
                                                                        if (endMin == m) {
                                                                            editActivityTemplateHtml += '<option value="' + m + '" selected>' + m + '</option>';
                                                                        } else {
                                                                            editActivityTemplateHtml += '<option value="' + m + '">' + m + '</option>';
                                                                        }
                                                                    }
                                                                }
                                                                editActivityTemplateHtml += '</select>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '<div class="row">';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Purpose</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-7">';
                                                                editActivityTemplateHtml += '<select class="form-control" id="id_purposeEdit">';
                                                                for (var i = 0; i < purposeData.length; i++) {
                                                                    var id = purposeData[i].id;
                                                                    var purpose = purposeData[i].purpose;
                                                                    if (id == purposeId) {
                                                                        editActivityTemplateHtml += '<option value="' + id + '" selected>' + purpose + '</option>';
                                                                    } else {
                                                                        editActivityTemplateHtml += '<option value="' + id + '">' + purpose + '</option>';
                                                                    }
                                                                }
                                                                editActivityTemplateHtml += '</select>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '<div class="row">';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Owner</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-7">';
                                                                editActivityTemplateHtml += '<input type="Cname" name="currency" class="form-control cls_ownerEdit" id="' + ownerId + '" value="' + ownerName + '" disabled>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Assigned To</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-7">';
                                                                editActivityTemplateHtml += '<select class="form-control" id="id_assignedEdit">';
                                                                editActivityTemplateHtml += '<option>None</option>';
                                                                for (var i = 0; i < teamMemberData.length; i++) {
                                                                    var userId = teamMemberData[i].user.id;
                                                                    var firstName = teamMemberData[i].user.firstName;
                                                                    var lastName = teamMemberData[i].user.lastName;
                                                                    var fullName = firstName + ' ' + lastName;
                                                                    if (assignId == userId) {
                                                                        isAssigneeIsImmidiateSubOrdinate = true;
                                                                        editActivityTemplateHtml += '<option value="' + userId + '" selected>' + fullName + '</option>';
                                                                    }
                                                                    else {
                                                                        editActivityTemplateHtml += '<option value="' + userId + '">' + fullName + '</option>';
                                                                    }
                                                                }
                                                                if (!isAssigneeIsImmidiateSubOrdinate) {
                                                                    editActivityTemplateHtml += '<option value="' + assignId + '" selected>' + assigneeName + '</option>';
                                                                }
                                                                editActivityTemplateHtml += '</select>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '<div class="row">';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Status</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-7">';
                                                                editActivityTemplateHtml += '<select class="form-control" id="id_statusEdit" onchange="outcomeForAddActivity()">';
                                                                editActivityTemplateHtml += '<option>Select</option>';
                                                                if (activityStatus == 0) {
                                                                    editActivityTemplateHtml += '<option value="2">Completed</option>';
                                                                    editActivityTemplateHtml += '<option value="0" selected>Pending</option>';
                                                                    editActivityTemplateHtml += '<option value="1">In-Progress</option>';
                                                                    editActivityTemplateHtml += '<option value="3">Cancelled</option>';
                                                                }
                                                                if (activityStatus == 1) {
                                                                    editActivityTemplateHtml += '<option value="2">Completed</option>';
                                                                    editActivityTemplateHtml += '<option value="0">Pending</option>';
                                                                    editActivityTemplateHtml += '<option value="1" selected>In-Progress</option>';
                                                                    editActivityTemplateHtml += '<option value="3">Cancelled</option>';
                                                                }
                                                                if (activityStatus == 2) {
                                                                    editActivityTemplateHtml += '<option value="2" selected>Completed</option>';
                                                                    editActivityTemplateHtml += '<option value="0">Pending</option>';
                                                                    editActivityTemplateHtml += '<option value="1">In-Progress</option>';
                                                                    editActivityTemplateHtml += '<option value="3">Cancelled</option>';
                                                                }
                                                                if (activityStatus == 3) {
                                                                    editActivityTemplateHtml += '<option value="2">Completed</option>';
                                                                    editActivityTemplateHtml += '<option value="0">Pending</option>';
                                                                    editActivityTemplateHtml += '<option value="1">In-Progress</option>';
                                                                    editActivityTemplateHtml += '<option value="3" selected>Cancelled</option>';
                                                                }
                                                                editActivityTemplateHtml += '</select>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Outcome</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-7">';
                                                                editActivityTemplateHtml += '<select class="form-control" id="id_outcomeEdit">';
                                                                for (var i = 0; i < outData.length; i++) {
                                                                    var outId = outData[i].id;
                                                                    var out = outData[i].outcome;
                                                                    if (outId == outcomeId) {
                                                                        editActivityTemplateHtml += '<option value="' + outId + '" selected>' + out + '</option>';
                                                                    }
                                                                    else {
                                                                        editActivityTemplateHtml += '<option value="' + outId + '">' + out + '</option>';
                                                                    }
                                                                }
                                                                editActivityTemplateHtml += '</select>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                
//                                                                //Added by Jyoti 30-01-2017
//                                                                if(checkRecurring == 1){
//                                                                editActivityTemplateHtml += '<div class="row">';
//                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
//                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Recurring Till Date</label>';
//                                                                editActivityTemplateHtml += '<div class="col-md-7">';                    
//                                                                editActivityTemplateHtml += '<div class="input-group date date-picker" data-date-format="dd-mm-yyyy" data-date-start-date="-1m">';
//                                                                editActivityTemplateHtml += '<input type="text" class="form-control" id="end_date" readonly value="' + getRecurrTillDate +'">';
//                                                                editActivityTemplateHtml += '<span class="input-group-btn">';
//                                                                editActivityTemplateHtml += '<button class="btn btn-default" type="button"><i class="fa fa-calendar"></i></button>';
//                                                                editActivityTemplateHtml += '</span>';
//                                                                editActivityTemplateHtml += '</div>';
//                                                                editActivityTemplateHtml += '</div>';
//                                                                editActivityTemplateHtml += '</div>';
//                                                                editActivityTemplateHtml += '</div>';
//
//                                                                editActivityTemplateHtml += '<div class="row">';
//                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
//                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Repeat Type</label>';
//                                                                editActivityTemplateHtml += '<div class="col-md-7">';
//                                                                editActivityTemplateHtml += '<select class="form-control" id="repeat_type">';
//                                                                if(getRepeatType == 11){
//                                                                editActivityTemplateHtml += '<option value="11" selected >Daily</option>';    // if 11 auto select daily else if ..
//                                                                editActivityTemplateHtml += '<option value="22" >Weekly</option>';
//                                                                editActivityTemplateHtml += '<option value="33" >Monthly</option>';
//                                                                }
//                                                                else if(getRepeatType == 22){
//                                                                editActivityTemplateHtml += '<option value="22" selected >Weekly</option>';
//                                                                editActivityTemplateHtml += '<option value="11" >Daily</option>';
//                                                                editActivityTemplateHtml += '<option value="33" >Monthly</option>';
//                                                                }
//                                                                else if(getRepeatType == 33){
//                                                                editActivityTemplateHtml += '<option value="33" selected >Monthly</option>';
//                                                                editActivityTemplateHtml += '<option value="22" >Weekly</option>';
//                                                                editActivityTemplateHtml += '<option value="11" >Daily</option>';
//                                                                }
//                                                                editActivityTemplateHtml += '</select>';
//                                                                editActivityTemplateHtml += '</div>';
//                                                                editActivityTemplateHtml += '</div>';
//                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
//                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Repeat After Every</label>';
//                                                                editActivityTemplateHtml += '<div class="col-md-7">';
//                                                                editActivityTemplateHtml += '<select class="form-control" id="repeatAfterEvery" >';
//                                                                for (var i = 1; i <= 31; i++) {
//                                                                var id = i;                        
//                                                                editActivityTemplateHtml += '<option value="' + id + '">' + id + '</option>';
//                                                                }
//                                                                editActivityTemplateHtml += '</select>';
//                                                                editActivityTemplateHtml += '</div>';
//                                                                editActivityTemplateHtml += '</div>';
//                                                                editActivityTemplateHtml += '</div>';
//
//                                                                editActivityTemplateHtml += '<div class="row">';
//                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
//                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Repeat On Day(Weekly) </label>';
//                                                                editActivityTemplateHtml += '<div class="col-md-7">';
//                                                                editActivityTemplateHtml += '<select class="form-control" id="repeatOnDay_weekly" multiple="multiple" >';
//                                                                editActivityTemplateHtml += '<option selected="selected" >Select</option>';
//                                                                editActivityTemplateHtml += '<option value="0" >MONDAY</option>';   // if 11 auto select daily else if ..
//                                                                editActivityTemplateHtml += '<option value="1" >TUESDAY</option>';
//                                                                editActivityTemplateHtml += '<option value="2" >WEDNESDAY</option>';
//                                                                editActivityTemplateHtml += '<option value="3" >THURSDAY</option>';
//                                                                editActivityTemplateHtml += '<option value="4" >FRIDAY</option>';
//                                                                editActivityTemplateHtml += '<option value="5" >SATURDAY</option>';
//                                                                editActivityTemplateHtml += '<option value="6" >SUNDAY</option>';
//                                                                editActivityTemplateHtml += '</select>';
//                                                                editActivityTemplateHtml += '</div>';
//                                                                editActivityTemplateHtml += '</div>';
//                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
//                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Repeat On Date (Monthly)</label>';
//                                                                editActivityTemplateHtml += '<div class="col-md-7">';                    
//                                                                editActivityTemplateHtml += '<select class="form-control" id="repeatOnDate_monthly" multiple="multiple" >';
//                                                                editActivityTemplateHtml += '<option selected="selected" >Select</option>';
//                                                                for (var i = 01; i <= 31; i++) {
//                                                                var id = i;                        
//                                                                editActivityTemplateHtml += '<option value="' + id + '">' + id + '</option>';
//                                                                }
//                                                                editActivityTemplateHtml += '</select>';
//                                                                editActivityTemplateHtml += '</div>';
//                                                                editActivityTemplateHtml += '</div>';
//                                                                editActivityTemplateHtml += '</div>';
//                                                                }
//                                                                //Ended by jyoti
                                                                
                                                                editActivityTemplateHtml += '<div class="row">';
                                                                editActivityTemplateHtml += '<div class="form-group" id="dscp">';
                                                                editActivityTemplateHtml += '<label class="col-md-3 control-label">Description</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-9">';
                                                                editActivityTemplateHtml += '<textarea class="form-control" rows="3" placeholder="Enter text" id="id_descriptionEdit">' + activityDescription + '</textarea>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';

                                                                editActivityTemplateHtml += '<span id="id_outcomedescriptiontextbox" style="display:block;">';
                                                                editActivityTemplateHtml += '<div class="row">';
                                                                editActivityTemplateHtml += '<div class="col-md-12 form-group" id="dscp">';
                                                                editActivityTemplateHtml += '<label class="col-md-3 control-label">Outcome Description</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-9">';
                                                                editActivityTemplateHtml += '<textarea class="form-control" rows="3" placeholder="Enter text" id="id_outcomedescription">' + outcomeDescription + '</textarea>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</span>';

                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '<div class="modal-footer">';
                                                                if (isAssigneeIsImmidiateSubOrdinate) {
                                                                    editActivityTemplateHtml += '<button type="submit" class="btn btn-info" onclick="editActivity(\'' + appointmentId + '\',\'' + teamId + '\',\'' + user + '\');return false;">Save</button>';
                                                                }
                                                                editActivityTemplateHtml += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</form>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                $('.cls_addActivity').html(editActivityTemplateHtml);
                                                                if (!isAssigneeIsImmidiateSubOrdinate) {
                                                                    $('#id_assignedEdit').prop("disabled", true);
                                                                }
                                                                
                                                                $('#autocompleteCnm').autocomplete({
								    minChars:3,
                                                                    //lookup: customer,
								    paramName: 'searchText',
    								    transformResult: function(response) {
        								return {
            									suggestions: $.map(response.result, function(dataItem) {
                								return { value: dataItem.value, id:dataItem.customerId  };
            									})
        								};
    								    },
		        					    dataType:"json",
								    serviceUrl: fieldSenseURL + '/customer/visit?userToken='+userToken,
                                                                    onSelect: function (suggestion) {
                                                                        $('#id_hiddenEdit').val(suggestion.id);
                                                                        customerContacts(suggestion.id)
                                                                    },
                        					   /* onSearchStart: function (query) {
								 		$('#pleaseWaitDialog').modal('show');
	                					    },
		        					    onSearchComplete: function (query, suggestions) {
										$('#pleaseWaitDialog').modal('hide');
		        					    }*/
                                                                });
                                                                if (jQuery().datepicker) {
                                                                    $('.date-picker').datepicker({
                                                                        rtl: App.isRTL(),
                                                                        autoclose: true
                                                                    });
                                                                    $('body').removeClass("modal-open"); // fix bug when inline picker is used in modal
                                                                }
                                                                $('#id_editactivity').slimScroll({
                                                                    size: '7px',
                                                                    color: '#bbbbbb',
                                                                    opacity: .9,
                                                                    position: false ? 'left' : 'right',
                                                                    height: 420,
                                                                    allowPageScroll: false,
                                                                    disableFadeOut: false
                                                                });
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                },
            			error: function(xhr, ajaxOptions, thrownError){	
					$('#pleaseWaitDialog').modal('hide');
					alert("error in ajax call : " + thrownError);
                			//toggleButton(el);
            			},
                            });
                        }
                    },
		    error: function(xhr, ajaxOptions, thrownError){	
			$('#pleaseWaitDialog').modal('hide');
			alert("error in ajax call : " + thrownError);
                	//toggleButton(el);
            	    },
                });
            }else {
		fieldSenseTosterError(dataActivity.errorMessage, true);
                FieldSenseInvalidToken(dataActivity.errorMessage);
		$(".cls_addActivity").modal('hide');
		if(dataActivity.errorMessage.indexOf("deleted")!=-1){
			activityShowTemplate(teamId, user);
		}
            }
        },
	error: function(xhr, ajaxOptions, thrownError){	
		$('#pleaseWaitDialog').modal('hide');
		alert("error in ajax call : " + thrownError);
                //toggleButton(el);
        },
    });
    $('#pleaseWaitDialog').modal('hide');
}
function editActivity(appointmentId, teamId, userId) {
    var customer = document.getElementById("id_hiddenEdit").value.trim();
    if (customer.trim().length == 0) {
        fieldSenseTosterError("Customer Name cannot be empty", true);
        return false;
    }
    var contact = document.getElementById("id_contactEdit").value.trim();
    if (contact.trim() == "" || contact.trim() == "None") {
        contact = 0;
    }
   var activity = document.getElementById("id_activityEdit").value.trim();
//    if (activity.trim().length == 0) {
//        fieldSenseTosterError("Activity cannot be empty", true);
//        return false;
//    }
    var dates = document.getElementById("id_dateEdit").value;
    if (dates.trim().length == 0) {
        fieldSenseTosterError("Date cannot be empty", true);
        return false;
    }
    var startHr = document.getElementById("edit_Shr").value.trim();
    if (startHr == 'HH') {
        fieldSenseTosterError("Start Time sould be selected", true);
        return false;
    }
    var startMin = document.getElementById("edit_Smin").value.trim();
    if (startMin == 'MM') {
        fieldSenseTosterError("Start Time sould be selected", true);
        return false;
    }
    var endHr = document.getElementById("edit_ehr").value.trim();
    if (endHr == "HH") {
        fieldSenseTosterError("End Time should be selected", true);
        return false;
    }
    var endMin = document.getElementById("edit_emin").value.trim();
    if (endMin == "MM") {
        fieldSenseTosterError("End Time should be selected", true);
        return false;
    }
    if(parseInt(startHr)>parseInt(endHr) || (startHr==endHr && parseInt(startMin)>parseInt(endMin))){
	fieldSenseTosterError("Start Time should be less than End Time", true);
        return false;
    }	
    var purpose = document.getElementById("id_purposeEdit").value.trim();
    if (purpose === 'Select') {
        fieldSenseTosterError("Purpose cannot be empty", true);
        return false;
    }
    var owner = document.getElementsByClassName("cls_ownerEdit")[0].id.trim();
    var assignedTo = document.getElementById("id_assignedEdit").value;
    if (assignedTo === 'None') {
        fieldSenseTosterError("Please select proper assigned to", true);
        return false;
    }
    var status = document.getElementById("id_statusEdit").value;
    if (status === 'Select') {
        fieldSenseTosterError("Please select proper status", true);
        return false;
    }
    var outcome = document.getElementById("id_outcomeEdit").value;
    var description = document.getElementById("id_descriptionEdit").value.trim();
    if (description.length > 1) {
        if (description.length > 1000) {
            fieldSenseTosterError("Description can not be more than 1000 characters", true);
            return false;
        }
    }
    var outcomeDescription = document.getElementById("id_outcomedescription").value.trim();
    if (outcomeDescription.trim().length != 0) {
        if (outcomeDescription.trim().length > 2000) {
            fieldSenseTosterError("Outcome Description can not be more than 2000 character", true);
            return false;
        }
    }
    var dateSplit = dates.split("-");
    var startDate = convertLocalDateToServerDate(dateSplit[2], dateSplit[1] - 1, dateSplit[0], startHr, startMin);
    var adate = startDate.getFullYear() + "-" + (startDate.getMonth() + 1) + "-" + startDate.getDate() + " " + startDate.getHours() + ":" + startDate.getMinutes() + ':00';
    var endtHours = endHr + ":" + endMin;
    var endDTime;
   // if (endtHours.trim() == "HH:MM") {
       // endDTime = adate;
   // } else {
        endDTime = convertLocalDateToServerDate(dateSplit[2], dateSplit[1] - 1, dateSplit[0], endHr, endMin);
        endDTime = endDTime.getFullYear() + "-" + (endDTime.getMonth() + 1) + "-" + endDTime.getDate() + " " + endDTime.getHours() + ":" + endDTime.getMinutes() + ':00';
    //}
    var activityObject = {
        "id": appointmentId,
        "title": activity,
        "sdateTime": adate,
        "sendTime": endDTime,
        "customer": {
            "id": customer
        },
        "customerContact": {
            "id": contact
        },
        "owner": {
            "id": owner
        },
        "assignedTo": {
            "id": assignedTo
        },
        "description": description,
        "purpose": {
            "id": purpose
        },
        "outcomes": {
            "id": outcome
        },
        "status": status,
        "outcomeDescription": outcomeDescription,
        "updated_by": {
            "id": loginUserId
        }
    }
    $('#pleaseWaitDialog').modal('show');
    var jsonData = JSON.stringify(activityObject);
    $.ajax({
        type: "PUT",
        url: fieldSenseURL + '/appointment/update',
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            if (data.errorCode == '0000') {
                activityShowTemplate(teamId, userId);
                $(".cls_addActivity").modal('hide');
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterSuccess(data.errorMessage, true);
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}

//  Added by Jyoti, 07-03-2017
function editThisAndFollowingActivity(endtDate1, getRepeatType, getRepeatAfterEvery, getRepeatOnDayWeekly, getRepeatOnDateMonthly, appointmentRecurringId, appointmentId, teamId, userId){
    var decideChangeURL = false;
    var customer = document.getElementById("id_hiddenEdit").value.trim();
    if (customer.trim().length == 0) {
        fieldSenseTosterError("Customer Name cannot be empty", true);
        return false;
    }
    var contact = document.getElementById("id_contactEdit").value.trim();
    if (contact.trim() == "" || contact.trim() == "None") {
        contact = 0;
    }
    var activity = document.getElementById("id_activityEdit").value.trim();
    var dates = document.getElementById("id_dateEdit").value;
    if (dates.trim().length == 0) {
        fieldSenseTosterError("Date cannot be empty", true);
        return false;
    }
    var startHr = document.getElementById("edit_Shr").value.trim();
    if (startHr == 'HH') {
        fieldSenseTosterError("Start Time sould be selected", true);
        return false;
    }
    var startMin = document.getElementById("edit_Smin").value.trim();
    if (startMin == 'MM') {
        fieldSenseTosterError("Start Time sould be selected", true);
        return false;
    }
    var endHr = document.getElementById("edit_ehr").value.trim();
    if (endHr == "HH") {
        fieldSenseTosterError("End Time should be selected", true);
        return false;
    }
    var endMin = document.getElementById("edit_emin").value.trim();
    if (endMin == "MM") {
        fieldSenseTosterError("End Time should be selected", true);
        return false;
    }
    if(parseInt(startHr)>parseInt(endHr) || (startHr==endHr && parseInt(startMin)>parseInt(endMin))){
	fieldSenseTosterError("Start Time should be less than End Time", true);
        return false;
    }	
    var purpose = document.getElementById("id_purposeEdit").value.trim();
    if (purpose === 'Select') {
        fieldSenseTosterError("Purpose cannot be empty", true);
        return false;
    }
    var owner = document.getElementsByClassName("cls_ownerEdit")[0].id.trim();
    var assignedTo = document.getElementById("id_assignedEdit").value;
    if (assignedTo === 'None') {
        fieldSenseTosterError("Please select proper assigned to", true);
        return false;
    }
    var status = document.getElementById("id_statusEdit").value;
    if (status === 'Select') {
        fieldSenseTosterError("Please select proper status", true);
        return false;
    }
    var outcome = document.getElementById("id_outcomeEdit").value;
    var description = document.getElementById("id_descriptionEdit").value.trim();
    if (description.length > 1) {
        if (description.length > 1000) {
            fieldSenseTosterError("Description can not be more than 1000 characters", true);
            return false;
        }
    }
    var outcomeDescription = document.getElementById("id_outcomedescription").value.trim();
    if (outcomeDescription.trim().length != 0) {
        if (outcomeDescription.trim().length > 2000) {
            fieldSenseTosterError("Outcome Description can not be more than 2000 character", true);
            return false;
        }
    }
    var dateSplit = dates.split("-");
    var startDate = convertLocalDateToServerDate(dateSplit[2], dateSplit[1] - 1, dateSplit[0], startHr, startMin);
    var adate = startDate.getFullYear() + "-" + (startDate.getMonth() + 1) + "-" + startDate.getDate() + " " + startDate.getHours() + ":" + startDate.getMinutes() + ':00';
    var endtHours = endHr + ":" + endMin;
    var endDTime;   
        endDTime = convertLocalDateToServerDate(dateSplit[2], dateSplit[1] - 1, dateSplit[0], endHr, endMin);
        endDTime = endDTime.getFullYear() + "-" + (endDTime.getMonth() + 1) + "-" + endDTime.getDate() + " " + endDTime.getHours() + ":" + endDTime.getMinutes() + ':00';
        
    if(isRecurring == 1){
        var recurrTillDate = document.getElementById("end_dateEdit").value.trim();              
        var recurrTillDateSplit = recurrTillDate.split("-");
        var recurrTill_date = recurrTillDateSplit[0];
        var recurrTill_month = recurrTillDateSplit[1];
        var recurrTill_year = recurrTillDateSplit[2];
        var recurrTill_Date = convertLocalDateToServerDate(recurrTill_year, recurrTill_month - 1, recurrTill_date, startHr, startMin);
        var recurrTill_Date_startTime = recurrTill_Date.getFullYear() + "-" + (recurrTill_Date.getMonth() + 1) + "-" + recurrTill_Date.getDate() + " " + recurrTill_Date.getHours() + ":" + recurrTill_Date.getMinutes() + ':00';
            if(endtDate1 != recurrTillDate){
                decideChangeURL = true;
            }
        var recurrTill_Date_endTime;
            recurrTill_Date_endTime = convertLocalDateToServerDate(recurrTill_year, recurrTill_month - 1, recurrTill_date, endHr, endMin);
            recurrTill_Date_endTime = recurrTill_Date_endTime.getFullYear() + "-" + (recurrTill_Date_endTime.getMonth() + 1) + "-" + recurrTill_Date_endTime.getDate() + " " + recurrTill_Date_endTime.getHours() + ":" + recurrTill_Date_endTime.getMinutes() + ':00';

        if (recurrTill_Date < startDate || recurrTillDate.trim().length == 0) {
            fieldSenseTosterError("Date cannot be less than start date or empty.", true);
            return false;
        }
        
        var repeatType = document.getElementById("editRepeat_type").value.trim();
        if (repeatType == 'Select') {
            fieldSenseTosterError("Please select repeat type", true);
            return false;
        }

        var repeatAfterEvery = document.getElementById("editRepeatAfterEvery").value.trim();
        if (repeatAfterEvery == 'Select') {
            fieldSenseTosterError("Please select repeat after every day.", true);
            return false;
        }
        
        var editRepeatOnDay_weekly = new Array();
        var editRepeatOnDate_monthly = new Array();
        
        if(repeatType == '22'){
            editRepeatOnDay_weekly = $('#editRepeatOnDay_weekly').val();    // multi value selection
            if($('#editRepeatOnDay_weekly').val() == 'Select'){
                fieldSenseTosterError("Please Select Weekly Day", true);
                return false;
            }
        }
        
        if(repeatType == '33'){
            editRepeatOnDate_monthly = $('#editRepeatOnDate_monthly').val();
            if($('#editRepeatOnDate_monthly').val() == 'Select'){
                fieldSenseTosterError("Please Select Monthly Date", true);
                return false;
            }
        }
        alert("recid " + appointmentRecurringId + " eds " +recurrTill_Date_startTime +  " t "+repeatType +" rae "+repeatAfterEvery + " m  "+editRepeatOnDate_monthly + " w "+editRepeatOnDay_weekly);
        if(getRepeatType != repeatType || getRepeatAfterEvery != repeatAfterEvery || getRepeatOnDateMonthly != editRepeatOnDate_monthly || getRepeatOnDayWeekly != editRepeatOnDay_weekly){
                decideChangeURL = true;
        }
    }
    
    var activityObject = {
        "id": appointmentId,
        "title": activity,
        "sdateTime": adate,
        "sendTime": endDTime,
        "isRecurring": isRecurring,        
        "appointmentRecurringList":{
            "id": appointmentRecurringId,
            "repeatAfterEvery": repeatAfterEvery,
            "repeatType": repeatType,        
            "recurrTill_Date_startTime": recurrTill_Date_startTime,
            "recurrTill_Date_endTime": recurrTill_Date_endTime,
            "repeatOnDay_weekly": editRepeatOnDay_weekly,
            "repeatOnDate_monthly": editRepeatOnDate_monthly
        },        
        "customer": {
            "id": customer
        },
        "customerContact": {
            "id": contact
        },
        "owner": {
            "id": owner
        },
        "assignedTo": {
            "id": assignedTo
        },
        "description": description,
        "purpose": {
            "id": purpose
        },
        "outcomes": {
            "id": outcome
        },
        "status": status,
        "outcomeDescription": outcomeDescription,
        "updated_by": {
            "id": loginUserId
        }
    }
    
    $('#pleaseWaitDialog').modal('show');
    var jsonData = JSON.stringify(activityObject);
    if(decideChangeURL == false){
        $.ajax({
            type: "PUT",
            url: fieldSenseURL + '/appointment/update',
            contentType: "application/json; charset=utf-8",
            headers: {
                "userToken": userToken
            },
            crossDomain: false,
            data: jsonData,
            cache: false,
            dataType: 'json',
            success: function (data) {
                if (data.errorCode == '0000') {
                    activityShowTemplate(teamId, userId);
                    $(".cls_addActivity").modal('hide');
                    $('#pleaseWaitDialog').modal('hide');
                    fieldSenseTosterSuccess(data.errorMessage, true);
                } else {
                    $('#pleaseWaitDialog').modal('hide');
                    fieldSenseTosterError(data.errorMessage, true);
                    FieldSenseInvalidToken(data.errorMessage);
                }
            }
        });
    } else{
        $.ajax({
            type: "PUT",
            url: fieldSenseURL + '/appointment/update/recurringVisits',
            contentType: "application/json; charset=utf-8",
            headers: {
                "userToken": userToken
            },
            crossDomain: false,
            data: jsonData,
            cache: false,
            dataType: 'json',
            success: function (data) {
                if (data.errorCode == '0000') {
                    activityShowTemplate(teamId, userId);
                    $(".cls_addActivity").modal('hide');
                    $('#pleaseWaitDialog').modal('hide');
                    fieldSenseTosterSuccess(data.errorMessage, true);
                } else {
                    $('#pleaseWaitDialog').modal('hide');
                    fieldSenseTosterError(data.errorMessage, true);
                    FieldSenseInvalidToken(data.errorMessage);
                }
            }
        });
    }
}

// Ended by Jyoti, 08-march-2017

function outcomeForAddActivity() {
    var status = document.getElementById("id_status").value;
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/activityOutcome",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (dataOutCome) {
            if (dataOutCome.errorCode == '0000') {
                var outData = dataOutCome.result;
                var activityTemplate = '';
                if (status == 2) {
                    for (var i = 0; i < outData.length; i++) {
                        var outId = outData[i].id;
                        var out = outData[i].outcome;

                        activityTemplate += '<option value="' + outId + '">' + out + '</option>';
                    }
                } else {
                    activityTemplate += '<option value="0">None</option>';
                }
                if (status == 2 || status == 3) {
                    $('#id_outcomedescriptiontextbox').css("display", "block");
                    $('#id_createactivity').slimscroll({
                        scrollBy: '420px'
                    });
                } else {
                    $('#id_outcomedescriptiontextbox').css("display", "none");
                }
                $('#id_outcome').html(activityTemplate);
                $('#pleaseWaitDialog').modal('hide');
            }
        }
    });
}

/* clear map and add new markers */
function initializeMap(lat, lng) {
    var haightAshbury = new google.maps.LatLng(lat, lng);
    var mapOptions = {
        zoom: 14,
        center: haightAshbury,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    map = new google.maps.Map(document.getElementById('gmap_marker'),
        mapOptions);
}
// Add a marker to the map and push to the array.
function addMapMarker(location, memberId, infoWindowContent, clickedUserId) {
    time = new Date();
    var image = imageURLPath + accountId + '_' + memberId + '_mm.png?' + time;
    var marker = new google.maps.Marker({
        position: location,
        map: map,
        icon: image,
        infoWindow: {
            content: infoWindowContent
        }
    });
    markers.push(marker);
    var infowindow = new google.maps.InfoWindow();
    infowindow.setContent(infoWindowContent);
    google.maps.event.addListener(marker, 'click', function () {
        infowindow.open(map, marker);
    });
    if (clickedUserId == memberId) {
        infowindow.open(map, marker);
    }
}
// Sets the map on all markers in the array.
function setAllMapMarkers(map) {
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(map);
    }
}
// Removes the markers from the map, but keeps them in the array.
function clearAllMapMarkers() {
    setAllMapMarkers(null);
}
function realTimeMapMarkers(hirarchyId) {
    var dateOnly = new Date();
    dateOnly = convertLocalDateToServerDate(dateOnly.getFullYear(), dateOnly.getMonth(), dateOnly.getDate(), dateOnly.getHours(), dateOnly.getMinutes());
    var month = dateOnly.getMonth() + 1;
    var date = dateOnly.getDate();
    if (month < 10) {
        month = '0' + month;
    }
    if (date < 10) {
        date = '0' + date;
    }
    dateOnly = dateOnly.getFullYear() + "-" + month + "-" + date;
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/team/userPositions/" + hirarchyId + "/" + dateOnly,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: false,
        success: function (data) {
            var teamMembers = data.result;
            if (teamMembers.length != 0) {
                if (!userActivityMarkersFlag) {
                    clearAllMapMarkers();
                }
                for (var i = 0; i < teamMembers.length; i++) {
                    var memberId = teamMembers[i].user.id;
                    if (loginUserId != memberId) {
                        var teamId = teamMembers[i].teamId;
                        var memberName = teamMembers[i].user.firstName + ' ' + teamMembers[i].user.lastName;
                        var latitude = teamMembers[i].user.latitude;
                        var langitude = teamMembers[i].user.langitude;
                        var userTime = teamMembers[i].user.lastKnownLocationTime;
                        var jsDate = convertServerDateToLocalDate((userTime.year) + 1900, userTime.month, userTime.date, userTime.hours, userTime.minutes);
                        var today = new Date();
                        var dateFromJson = jsDate.getDate() + '/' + jsDate.getMonth() + '/' + jsDate.getFullYear();
                        var dateToday = today.getDate() + '/' + today.getMonth() + '/' + today.getFullYear();
                        var hours = jsDate.getHours();
                        var minutes = jsDate.getMinutes();
                        var isOnline = teamMembers[i].isOnline;
                        var officeLatitude = teamMembers[i].user.officeLocation.latitude;
                        var officeLongitude = teamMembers[i].user.officeLocation.langitude;
                        var homeeLatitude = teamMembers[i].user.homeLocation.latitude;
                        var homeLongitude = teamMembers[i].user.homeLocation.langitude;
                        var canTakeCalls = teamMembers[i].canTakeCalls;
                        var inMeeting = teamMembers[i].inMeeting;
                        var userLocationText = '';
                        if (latitude == officeLatitude && langitude == officeLongitude) {
                            userLocationText = ' at office';
                        } else if (latitude == homeeLatitude && langitude == homeLongitude) {
                            userLocationText = ' at home';
                        }
                        var timeToShow = ' am';
                        if (hours < 10) {
                            hours = '0' + hours;
                        }
                        else {
                            if (hours > 12) {
                                hours = hours - 12;
                                if (hours < 10) {
                                    hours = '0' + hours;
                                }
                                timeToShow = ' pm';
                            }
                        }
                        if (minutes < 10) {
                            minutes = '0' + minutes;
                        }
                        timeToShow = hours + ':' + minutes + '' + timeToShow;
                        var timeString = '';
                        if (dateFromJson == dateToday) {
                            timeString = 'Today at ' + timeToShow;
                        }
                        else {
                            timeString = fieldSenseseMesageTimeFormateForJSDate(jsDate);
                        }
                        if (!userActivityMarkersFlag) {
                            var latlng = new google.maps.LatLng(latitude, langitude);
                            var infoWindowContent = '<span style="color:#000"><b>' + memberName + '</b> ' + userLocationText + '<br>' + timeString + '</span>';
                            addMapMarker(latlng, memberId, infoWindowContent, hirarchyId);
                        }
                        var cls_usr_status_in_meet = 'inmeet';
                        var cls_usr_status_can_take_calls;
                        var cls_usr_status_online_offline;
                        if (canTakeCalls) {
                            cls_usr_status_can_take_calls = 'P_online';
                        } else {
                            cls_usr_status_can_take_calls = 'P_offline';
                        }
                        if (isOnline) {
                            cls_usr_status_online_offline = 'online';
                        } else {
                            cls_usr_status_online_offline = 'offline';
                        }
                        var realTimeUserStatus = '';
                        realTimeUserStatus += '<span class="' + cls_usr_status_can_take_calls + '"></span>';
                        if (inMeeting) {
                            realTimeUserStatus += '<span class="' + cls_usr_status_in_meet + '"></span>';
                        } else {
                            realTimeUserStatus += '<span class="' + cls_usr_status_online_offline + '"></span>';
                        }
                        $('#id_user_status' + memberId).html(realTimeUserStatus);
                        intervalMapMarkers = window.setInterval(function () {
                            realTimeMapMarkers(hirarchyId);
                        }, 1800000);
                    }
                }
            }
        }
    });
}

function refreshRouteMap(teamId, hirarchyId, hirarchyName) {

	for (i = 0; i < markerArray.length; i++) {
     	 	markerArray[i].setMap(null);
    	}
	for (i = 0; i < ploylineArray.length; i++) {
      		ploylineArray[i].setMap(null);
    	}
	if(isTravelRouteMode==true){
	userLevelHirarchy(hirarchyName, hirarchyId, teamId);
        return;
    }
    userActivityMarkersFlag = false;
    window.clearTimeout(intervalMapMarkers);
    realTimeMapMarkers(hirarchyId);
    userActivitiesTemplate(teamId, hirarchyId, hirarchyName);

}

var directionsService;
var directionsDisplay;
var markerArray = [];
var ploylineArray = [];
var countroute1=0;
var countroute2=0;
function caliculateRouteOfUser(userId, userName, teamId) {
	if(countroute1!=countroute2) 
		return;

	countroute1++;
 	$("#toast-container").remove();

	// First, remove any existing markers from the map.
   	for (i = 0; i < markerArray.length; i++) {
      		markerArray[i].setMap(null);
    	}
	for (i = 0; i < ploylineArray.length; i++) {
      		ploylineArray[i].setMap(null);
    	}

    userActivityMarkersFlag = false;
    var dateSelected = document.getElementById("id_activityForDate").value;
    var now;
    if (dateSelected.length == 0) {
        var now1 = new Date();
        now = new Date(now1.getFullYear(), now1.getMonth(), now1.getDate());
    } else {
        var dateToSplit = dateSelected.split(' ');
        var y = dateToSplit[2];
        var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
        var m = monthNames.indexOf(dateToSplit[1]);
        var d = dateToSplit[0];
        now = new Date(y, m, d);
    }
    var dateToServer = convertLocalDateToServerDate(now.getFullYear(), now.getMonth(), now.getDate(), now.getHours(), now.getMinutes());
    var month = dateToServer.getMonth() + 1;
    if (month < 10) {
        month = '0' + month;
    }
    var date = dateToServer.getDate();
    if (date < 10) {
        date = '0' + date;
    }
    var hours = dateToServer.getHours();
    if (hours < 10) {
        hours = '0' + hours;
    }
    var minutes = dateToServer.getMinutes();
    if (minutes < 10) {
        minutes = '0' + minutes;
    }
    dateToServer = dateToServer.getFullYear() + "-" + month + "-" + date + " " + hours + ":" + minutes + ':00';
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/userTravelLog/user/" + userId + "/" + dateToServer,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            if (data.errorCode == '0000') {
                	var userTraveldata = data.result[0];
			var attendanceData=data.result[1];
			var appointmentsData=data.result[2];
			var displayHtml="";
			var punchInTemplate="";
			var punchOutTemplate="";
			var appointmentTemplate="";
			var traveledRouteDataHtml = "";
			var lastknownLocation="";
			var lastKnownTime="";
			if(userTraveldata.length == 0 && attendanceData.length == 0 && appointmentsData.length == 0){
				$('.cls_traveldroutesOnly').html("No activites for user");
				fieldSenseTosterError("No data to display the travelled route.", true);
				countroute2++;
				$('#pleaseWaitDialog').modal('hide');
				return false;
                	}else{				
                   	 	//document.getElementById('MDholder').style.display = 'none';
                    		var dateForSelection = $('#id_activityForDate').val();
                    		traveledRouteDataHtml = '';
                    		traveledRouteDataHtml += '<div class="tab-content">';
                    		traveledRouteDataHtml += '<div class="tab-pane fade active in" id="tab_1_1">';
                    		traveledRouteDataHtml += '<form class="form-inline" role="form">';
                    		traveledRouteDataHtml += '<div class="form-body" id="tab_hdr">';
                    		traveledRouteDataHtml += '<div class="form-group">';
                    		traveledRouteDataHtml += '<div class="col-md-6">';
                    		traveledRouteDataHtml += '<div class="input-group date date-picker input-small" data-date-format="dd M yyyy">';
                    		traveledRouteDataHtml += '<input type="text" disabled class="form-control" value="' + dateForSelection + '" id="id_activityForDate">';
                    		traveledRouteDataHtml += '<span class="input-group-btn">';
                    		traveledRouteDataHtml += '<button class="btn btn-default" type="button"><i class="fa fa-calendar"></i></button>';
                    		traveledRouteDataHtml += '</span>';
                    		traveledRouteDataHtml += '</div>';
                    		traveledRouteDataHtml += '</div>';
                    		traveledRouteDataHtml += '<div class="col-md-2">';
                    		traveledRouteDataHtml += '<button type="button" class="btn btn-info" data-toggle="modal" href="#responsive" onclick="addActivityTemplate(\'' + userId + '\',\'' + userName + '\',\'' + true + '\',\'' + teamId + '\')"><i class="fa fa-plus"></i> Add Visit</button>';
                    		traveledRouteDataHtml += '</div>';
                    		traveledRouteDataHtml += '</div>';
                    		traveledRouteDataHtml += '<div class="form-group">';
                    		traveledRouteDataHtml += '<div class="col-md-12">';
                   	 	//                    traveledRouteDataHtml+='<label class="route-label"><a href="" onclick="caliculateRouteOfUser(\'' + userId + '\',\'' + userName + '\',\'' + teamId + '\')">Show travelled route</a></label>';
                    		traveledRouteDataHtml += '<label class="route-label"><a href="" onclick="mapRouteFromRoute(\'' + userId + '\');return false;">Show travelled route</a></label>';
                    		traveledRouteDataHtml += '</div>';
                   		traveledRouteDataHtml += '</div>';
                    		traveledRouteDataHtml += '</div>';
                    		traveledRouteDataHtml += '<div class="tab-pane2" id="shw_path">';
                    		traveledRouteDataHtml += '<div class="scroller id_scrollerTraveledRoutes" id="path" data-always-visible="1" data-rail-visible1="1" >';
                    		var rendererOptions = {
                        		map: map
                   		}
                    		directionsService = new google.maps.DirectionsService();
                    		directionsDisplay = new google.maps.DirectionsRenderer(rendererOptions);
                    		var waypts1 = [];
		   	 	//var waypts2 = [];
		   		var timeperiod=[];
                    		var markerPoint = 0;
				var prevlong;
				var prevlat;
				var custlocatstarttime;
				var custlocatendtime;
				var custname;
				var count=0;
				var timecount;
				var timespan;
				var previscust;
				var prevcustname;
				var prevcustloc;
				var prevloc;
				var precustlatlong;
				var titlecount=0;
				var marker;
				var punch_in_time;
				var punch_in_location;
				var punch_out_time;
				var punch_out_location;
				var bounds = new google.maps.LatLngBounds();
                    		traveledRouteDataHtml += '<span class="cls_traveldroutesOnly">';
				displayHtml+=traveledRouteDataHtml;
				if (userTraveldata.length != 0) {
		           		for (var i = 0; i < userTraveldata.length; i++) {			
		                		var latitude = userTraveldata[i].latitude;
		                		var langitude = userTraveldata[i].langitude;
						if(latitude==0.0 && langitude==0.0){
							continue;
						}
		               			var isCustomerLocation = userTraveldata[i].isCustomerLocation;
						var custname= userTraveldata[i].customerName;
						var custloc= userTraveldata[i].locationIdentifier;
						var location= userTraveldata[i].location;
		                		var locationTime = userTraveldata[i].createdOn;
						var source_value = userTraveldata[i].sourceValue;
		                		var timeZoneOffSet = locationTime.timezoneOffset * 60 * 1000;
		                		locationTime = new Date(locationTime.time - timeZoneOffSet);
		                		//locationTime = convertServerDateToLocalDate(locationTime.getFullYear(), locationTime.getMonth(), locationTime.getDate(), locationTime.getHours(), locationTime.getMinutes());
		                		locationTime = convertServerDateToLocalTimezone(locationTime.getFullYear(), locationTime.getMonth(), 		locationTime.getDate(), locationTime.getHours(), locationTime.getMinutes(),timeZoneOffSet);
		                		locationTime = locationTime.getFullYear() + "-" + (locationTime.getMonth() + 1) + "-" + locationTime.getDate() + " " + locationTime.getHours() + ":" + locationTime.getMinutes() + ":00.0";
		                		locationTime = fieldSenseseForJSDate(locationTime);
						//console.log("latitude="+latitude+"langitude="+langitude+"locationTime="+locationTime);
						var location2 = new google.maps.LatLng(latitude, langitude);
						if((prevlat==latitude && prevlong==langitude) || (previscust==isCustomerLocation && prevcustname==custname.trim() && prevcustloc==custloc.trim())){
							if(isCustomerLocation){
								//timecount++;
								//var timeperiod=timecount*5;
								if(previscust==isCustomerLocation && prevcustname==custname.trim() && prevcustloc==custloc.trim()){
									custlocatendtime=locationTime.split(",")[1];
									timespan=calculatemins(custlocatendtime)-calculatemins(custlocatstarttime);
									timeperiod[count]=timespan;
									var title=custname+" @"+custlocatstarttime;
									//if(parseInt(timespan)>5){
									//	title=custname+" @"+custlocatstarttime+" for "+timespan+" mins";
									//}
									marker = new google.maps.Marker({
						     				position: precustlatlong,
						 				//label: "B",
										icon: "https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld="+count+"|00B200|FFFFFF",
						   	 			map: map,
						   	 			//title: title
										tooltip: '<B>'+title+'</B>'
									});
									markerArray.push(marker);
									var tooltip ;
				 					google.maps.event.addListener(marker, 'mouseover', (function(marker, titlecount) {
				   		 				return function() {
											tooltip = new Tooltip({map: map}, marker);
											tooltip.bindTo("text", marker, "tooltip");
					       						tooltip.addTip();
					    						tooltip.getPos2(marker.getPosition());
				  		 		 		}
	       			 					})(marker, titlecount));

			 						google.maps.event.addListener(marker, 'mouseout',  (function(marker, titlecount) {
		 						 		return function() {
		   									tooltip.removeTip();
										}
	      			 			 		})(marker, titlecount));
							 			continue;
								}else{
									custlocatstarttime=locationTime.split(",")[1]
									//console.log("custlocationtime"+custlocationtime);
									custname=userTraveldata[i].customerName;
									prevcustname=custname.trim();
									previscust=isCustomerLocation;
									prevcustloc=custloc.trim();
									precustlatlong=location2;
									timespan=0;
									count++;
									timeperiod[count]=0;
				  					marker = new google.maps.Marker({
				     						position: location2,
				 						//label: "B",
										icon: "https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld="+count+"|00B200|FFFFFF",
				   	 					map: map,
				   						// title: custname+" @"+custlocatstarttime
										tooltip: '<B>'+custname+' @'+custlocatstarttime+'</B>'
				  					});
								}
							}else{
								if(i==userTraveldata.length-1){
									var label="assets/img/end.png";
									if(latitude==userTraveldata[0].latitude && langitude==userTraveldata[0].langitude){
										label="assets/img/startend.png";
										titlecount--;
									}
									if(prevlat==latitude && prevlong==langitude){
										titlecount--;
									}
									bounds.extend(location2);
									marker = new google.maps.Marker({
				    						position: location2,
				  						//icon: "https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld="+label+"|57b5e3|FFFFFF",
										icon:label,
				    						map: map,
				   						//title: locationTime.split(",")[1]
										tooltip: '<B>@'+locationTime.split(",")[1]+'</B>'
				 			 		});
							 		markerArray.push(marker);
								}else if(source_value==4){
								
									bounds.extend(location2);
				    					marker = new google.maps.Marker({
				    						position: location2,
				   						map: map,
				    						icon: "https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld=T|adadad|FFFFFF",
				    						title: locationTime.split(",")[1],
							 			tooltip: '<B>@'+locationTime.split(",")[1]+'</B>'
				  					});
								}else if(source_value==5){
								
									bounds.extend(location2);
				    					marker = new google.maps.Marker({
				    						position: location2,
				   						map: map,
				    						icon: "https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld=R|adadad|FFFFFF",
				    						title: locationTime.split(",")[1],
							 			tooltip: '<B>@'+locationTime.split(",")[1]+'</B>'
				  					});
								}else{
									continue;
								}
							}
						}else{
							if(isCustomerLocation){
								custlocatstarttime=locationTime.split(",")[1]
								//console.log("custlocationtime"+custlocationtime);
								custname=userTraveldata[i].customerName;
								prevcustname=custname.trim();
								previscust=isCustomerLocation;
								prevcustloc=custloc.trim();
								precustlatlong=location2;
								timespan=0;
								count++;
								timeperiod[count]=0;
				  				marker = new google.maps.Marker({
				     					position: location2,
				 					//label: "B",
									icon: "https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld="+count+"|00B200|FFFFFF",
				   	 				map: map,
				   					// title: custname+" @"+custlocatstarttime
									tooltip: '<B>'+custname+' @'+custlocatstarttime+'</B>'
				  				});
							}else{

								if(i==0){
									var label="assets/img/start.png";
									bounds.extend(location2);
									marker = new google.maps.Marker({
				    						position: location2,
				   						map: map,
										//icon: "https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld="+label+"|57b5e3|FFFFFF",
										icon:label,
				   						//title:locationTime.split(",")[1]
										tooltip: '<B>@'+locationTime.split(",")[1]+'</B>'
				  					});
								}
								else if(i==userTraveldata.length-1){
									var label="assets/img/end.png";
									if(latitude==userTraveldata[0].latitude && langitude==userTraveldata[0].langitude){
										label="assets/img/startend.png";
									}
									bounds.extend(location2);
									marker = new google.maps.Marker({
				    						position: location2,
				  						//icon: "https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld="+label+"|57b5e3|FFFFFF",
										icon:label,
				    						map: map,
				   						//title:locationTime.split(",")[1]
										tooltip: '<B>@'+locationTime.split(",")[1]+'</B>'
				 				 	});
								}else if(source_value==4){
								
									bounds.extend(location2);
				    					marker = new google.maps.Marker({
				    						position: location2,
				   						map: map,
				    						icon: "https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld=T|adadad|FFFFFF",
				    						title: locationTime.split(",")[1],
							 			tooltip: '<B>@'+locationTime.split(",")[1]+'</B>'
				  					});
								}else if(source_value==5){
								
									bounds.extend(location2);
				    					marker = new google.maps.Marker({
				    						position: location2,
				   						map: map,
				    						icon: "https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld=R|adadad|FFFFFF",
				    						title: locationTime.split(",")[1],
							 			tooltip: '<B>@'+locationTime.split(",")[1]+'</B>'
				  					});
								}else{
									var lineSymbol = {
				   	 					path: google.maps.SymbolPath.CIRCLE,
				   						scale: 3,
				   	 					strokeColor: '#57b5e3'
				 		 			};
									bounds.extend(location2);
				    					marker = new google.maps.Marker({
				    						position: location2,
				   						map: map,
				    						//icon: "https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld="+k+"|57b5e3|000000",
				    						icon:lineSymbol,
				    						//title: locationTime.split(",")[1]
							 			tooltip: '<B>@'+locationTime.split(",")[1]+'</B>'
				  					});

					
								}
							prevloc=userTraveldata[i].location.trim();
				   			}

						}
		
			 			markerArray.push(marker);
			 			waypts1.push({"Geometry":{"Latitude":latitude,"Longitude":langitude}});
			
						prevlat=latitude;
						prevlong=langitude;
 						var tooltip ;
						google.maps.event.addListener(marker, 'mouseover', (function(marker, titlecount) {
		   		 			return function() {
								tooltip = new Tooltip({map: map}, marker);
								tooltip.bindTo("text", marker, "tooltip");
		       						tooltip.addTip();
		    						tooltip.getPos2(marker.getPosition());
		  		  			}
       						})(marker, titlecount));

			 			google.maps.event.addListener(marker, 'mouseout',  (function(marker, titlecount) {
	 		 				return function() {
           							tooltip.removeTip();
							}
      			  			})(marker, titlecount));

  						map.fitBounds(bounds);
						//console.log("prevloc:"+prevloc);
			       			/*if (isCustomerLocation) {
		                   			markerPoint++;
		                    			var customerName = userTraveldata[i].customerName;
		                    			var location = userTraveldata[i].location;
		                    			traveledRouteDataHtml += '<div class="form-body">';
		                    			traveledRouteDataHtml += '<div class="form-group">';
		                    			traveledRouteDataHtml += '<span class="badge badge-info">' + markerPoint + '</span>';
		                    			traveledRouteDataHtml += '<label class="col-md-12 title-label"><a href="#">' + customerName + '</a></label>';
		                    			traveledRouteDataHtml += '<label class="col-md-12 time-label">' + locationTime + '</label>';
		                    			traveledRouteDataHtml += '<label class="col-md-12 loc-label">' + location + '</label>';
		                    			traveledRouteDataHtml += '</div>';
		                    			traveledRouteDataHtml += '</div>';
				        		}*/
						titlecount++;
			

				        		/*traveledRouteDataHtml += '<div class="form-body">';
				        		traveledRouteDataHtml += '<div class="form-group">';
							if(isCustomerLocation) {
								traveledRouteDataHtml += '<span class="badge badge-success">' + count+ '</span>';
							}
							else if(i==0){
				       	 			traveledRouteDataHtml += '<span class="badge badge-info">S</span>';
							}else if(i==userTraveldata.length-1){
								traveledRouteDataHtml += '<span class="badge badge-info">E</span>';
							}
							else{
					 			traveledRouteDataHtml += '<span class="inmeet" style="float:left">&nbsp;</span>';
							}
				       	 		traveledRouteDataHtml += '<label class="col-md-12 title-label"><a href="#">' + locationTime.split(",")[1]+'</a>'+'</label>' ;
				        		if(isCustomerLocation){
				            			raveledRouteDataHtml += '<label class="col-md-12 loc-label"><a href="#">'+custname +'</a>'+'</label>';
				        		}
				        		traveledRouteDataHtml += '<label class="col-md-12 loc-label">' + location + '</label>';
				        		traveledRouteDataHtml += '<label class="col-md-12 loc-label">'+'Distance: ' + userTraveldata[i].travelDistance + ' km'+'</label>';
							if(isCustomerLocation){
					 			traveledRouteDataHtml += '<label class="col-md-12 loc-label custmrloc" style="display:none">Time Spent: 0mins</label>';
							}
				        		traveledRouteDataHtml += '</div>';
				        		traveledRouteDataHtml += '</div>';*/
		
			
		            		}
			
				  	//$('.cls_traveldrouteData').html(traveledRouteDataHtml);
				    	
					  var symbolTwo = {
				  		path: 'M-4,-4 L0,-8 L4,-4',
				   		strokeColor: '#000000',
				 		strokeWeight: 1
					};
					for(var i=0;i<waypts1.length-1;i++){
						var line = new google.maps.Polyline({
							path: [ {lat: waypts1[i].Geometry.Latitude, lng: waypts1[i].Geometry.Longitude},{lat:waypts1[i+1].Geometry.Latitude, lng:  waypts1[i+1].Geometry.Longitude}],
					  		icons: [
					    			{
					       				icon: symbolTwo,
					       				 offset: '50%'
					     		 	}
					   			],
								strokeColor:"#000000",
								strokeWeight: 1,
				  		  		map: map
				  			});

				 		//animateCircle(line);

					ploylineArray.push(line);
					}
			
					// var waypts2 = waypts;
					/*alert("waypts2.length="+waypts2.length);
					if (waypts1.length <= 10) {
						//alert("waypts2.length<= 10");
						caliculateUserRoute(waypts1, directionsService, directionsDisplay);
					 } else {
						/alert("else waypts2.length<= 10");
					      //  for (var j = 0; j < waypts2.length; j=j + 10) {
						 //   var k = j + 10;
			
						 //   if (k > waypts.length) {
						   //     k = waypts.length;
						  //  }
						  //  var waypoints = waypts.slice(j, k);
						    caliculateUserRouteMoreThanTen(waypts2, directionsService, directionsDisplay);

					       // }

					}*/ 
					var boundsListener = google.maps.event.addListener((map), 'bounds_changed', function(event) {
						this.setZoom(13);
						google.maps.event.removeListener(boundsListener);
					});
					for (i = 0; i < markers.length; i++) {
				      		markers[i].setMap(null);
				   	}

		                	
					var lastknownLocation=userTraveldata[userTraveldata.length-1].location;
					var lastKnownTime=userTraveldata[userTraveldata.length-1].createdOn;
			
					isTravelRouteMode=true;
					fieldSenseTosterSuccessForUserRoute("Exit travelled route mode.", true, userId, userName, teamId);
			
					/*var index=1;
					$('.custmrloc').each(function(){
						if(parseInt(timeperiod[index])>5){
							$(this).show();
							$(this).html('Time Spent:'+timeperiod[index]+'mins');
						}
						index++;
					});*/
		        		
				}else{
					//$('.cls_traveldroutesOnly').html("No activites for user");
				    	fieldSenseTosterError("No data to display the travelled route.", true);
			       	}
				if(attendanceData.length !=0){
					for (var i = 0; i < attendanceData.length; i++) {
						var date = attendanceData[i].punchDate;
                    				var dateSplit = date.split('-');
						punch_in_time=attendanceData[i].punchInTime;
						punch_in_time=convertTimeToLocal(punch_in_time,dateSplit).split(",")[1];
						punch_in_location=attendanceData[i].punchInLocation;				
						punchInTemplate +='<div class="form-body">';
						punchInTemplate +='<div class="form-group">';
						punchInTemplate += '<span class="badge badge-info">S</span>';
						punchInTemplate += '<label class="col-md-12 title-label"><a href="#">Punch In : ' + punch_in_time+'</a>'+'</label>' ;
						punchInTemplate += '<label class="col-md-12 loc-label">' +  punch_in_location + '</label>';
				            	//punchInTemplate += '<label class="col-md-12 loc-label"></label>';
				            	punchInTemplate += '</div>';
						punchInTemplate += '</div>';
						displayHtml+=punchInTemplate;
						punchOutTemplate="";
						punch_out_time=attendanceData[i].punchOutTime;
						var label="";
						if(punch_out_time != '00:00:00'){
							var punchOutDate = attendanceData[i].punchOutDate;
                    					var punchOutDateSplit = punchOutDate.split('-');
							punch_out_time=attendanceData[i].punchOutTime;
							punch_out_time=convertTimeToLocal(punch_out_time,punchOutDateSplit);
							if(date==punchOutDate){
								punch_out_time=punch_out_time.split(",")[1];
							}
							punch_out_location=attendanceData[i].punchOutLocation;
							label="Punch Out";
						}else{
							punch_out_time=lastKnownTime;
							punch_out_location=lastknownLocation;
							if(punch_out_time !=""){
								punch_out_time=convertDateToLocal(punch_out_time);
								punch_out_time=punch_out_time.split(",")[1];
							}
							label="Last Known Location";
						}
						punchOutTemplate +='<div class="form-body">';
						punchOutTemplate +='<div class="form-group">';
				            	punchOutTemplate += '<span class="badge badge-info">E</span>';
						if(label=="Last Known Location"){
							punchOutTemplate += '<label class="col-md-12 title-label"><a href="#">' +  label +'</a>'+'</label>';
							punchOutTemplate += '<label class="col-md-12 loc-label"><a href="#">'+punch_out_time +'</a>'+'</label>';
							punchOutTemplate += '<label class="col-md-12 loc-label">' + punch_out_location + '</label>';
				            	//punchOutTemplate += '<label class="col-md-12 loc-label"></label>';
						}else {
							punchOutTemplate += '<label class="col-md-12 title-label"><a href="#">Punch Out : ' +  punch_out_time+'</a>'+'</label>' ;
							punchOutTemplate += '<label class="col-md-12 loc-label">' + punch_out_location + '</label>';
						}
				            	punchOutTemplate += '</div>';
						punchOutTemplate += '</div>';
						
					}
				}
				var app_count=0;
				if(appointmentsData.length !=0){
					for (var i = 0; i < appointmentsData.length; i++) {
						app_count++;
						var customername=appointmentsData[i].customer.customerName;
						var title = appointmentsData[i].title;
						var status = appointmentsData[i].status;
                    				var checkInTime = appointmentsData[i].checkInTime;
						checkInTime=convertDateToLocal(checkInTime);
						if(checkInTime.indexOf("01 Jan 1970")!=-1){
							if(status!=0 && status!=3){
								checkInTime = appointmentsData[i].dateTime;
								checkInTime=convertDateToLocal(checkInTime);
								checkInTime=checkInTime.split(",")[1];
						
							}else{
								checkInTime = "--:--";
							}
						}else{
							if(status!=0 && status!=3){
								checkInTime=checkInTime.split(",")[1];
							}else{
								checkInTime = "--:--";
							}
						}
						var checkInLocation=appointmentsData[i].checkInLocation;			
						var checkOutTime = appointmentsData[i].checkOutTime;
						checkOutTime=convertDateToLocal(checkOutTime);
						if(checkOutTime.indexOf("01 Jan 1970")!=-1){
							if(status==2){
								checkOutTime = appointmentsData[i].endTime;
								checkOutTime=convertDateToLocal(checkOutTime);
								checkOutTime=checkOutTime.split(",")[1];
						
							}else{
								checkOutTime = "--:--";
							}
						}else{
							if(status==2){
								checkOutTime=checkOutTime.split(",")[1];
							}else{
								checkOutTime = "--:--";
							}
						}
						var checkOutLocation=appointmentsData[i].checkOutLocation;
						apptTemplate ='<div class="form-body">';
						apptTemplate +='<div class="form-group">';
						apptTemplate += '<span class="badge badge-success">' + app_count+ '</span>';
						apptTemplate += '<label class="col-md-12 title-label"><a href="#">' + title+'</a>'+'</label>' ;
						apptTemplate += '<label class="col-md-12 loc-label"><a href="#">'+customername +'</a>'+'</label>';
				            	apptTemplate += '<label class="col-md-12 loc-label">CheckIn Time : ' + checkInTime + '</label>';
						
						apptTemplate += '<label class="col-md-12 loc-label">CheckOut Time : ' + checkOutTime + '</label>';
						
				            	apptTemplate += '</div>';
						apptTemplate += '</div>';
						appointmentTemplate +=apptTemplate;
						
					}
					displayHtml+=appointmentTemplate;
				}
				
			}
			displayHtml+=punchOutTemplate;
			displayHtml += '</span>';
			displayHtml += '</div>';
			displayHtml += '</div>';
			displayHtml += '</form>';
			displayHtml += '</div>';
			displayHtml += '</div>';
			$('.cls_traveldrouteData').html(displayHtml);
			$('.route-label').hide();
			if (jQuery().datepicker) {
                        	$('.date-picker').datepicker({
                            		rtl: App.isRTL(),
                            		autoclose: true
                        	}).on('changeDate', function (en) {
					mapRouteFromRoute(userId);
                            	});
                        	$('body').removeClass("modal-open"); // fix bug when inline picker is used in modal
                    	}
                    	$('.id_scrollerTraveledRoutes').slimScroll({
                        	size: '9px',
                        	color: '#bbbbbb',
                        	opacity: .6,
                        	position: false ? 'left' : 'right',
                        	height: 700,
				alwaysVisible: true,
                        	allowPageScroll: false,
                        	//disableFadeOut: true,
				wheelstep:10
                    	});		    					
		}else{
			fieldSenseTosterError(data.errorMessage, true);
		}
		$('#pleaseWaitDialog').modal('hide');		
		countroute2++;
       	  }
    });
}

function convertDateToLocal(dateTime){
	var timeZoneOffSet = dateTime.timezoneOffset * 60 * 1000;
	var dateTime = new Date(dateTime.time - timeZoneOffSet);
	dateTime = convertServerDateToLocalTimezone(dateTime.getFullYear(), dateTime.getMonth(),dateTime.getDate(), dateTime.getHours(), dateTime.getMinutes(),timeZoneOffSet);
	dateTime = dateTime.getFullYear() + "-" + (dateTime.getMonth() + 1) + "-" + dateTime.getDate() + " " + dateTime.getHours() + ":" + dateTime.getMinutes() + ":00.0";
	dateTime = fieldSenseseForJSDate(dateTime);
	return dateTime;
}


function convertTimeToLocal(punchIn,dateSplit){
	var punchIn=punchIn;
	var punchInSplit = punchIn.split(':');
	var punchInDateJs = convertServerDateToLocalDate(dateSplit[0], dateSplit[1] - 1, dateSplit[2], punchInSplit[0], punchInSplit[1]);
	var punchInHr = punchInDateJs.getHours();
	var punchInHr1 = punchInDateJs.getHours();
	var punchInMin = punchInDateJs.getMinutes();
	var punchInMin1 = punchInDateJs.getMinutes();
	var d = punchInDateJs.getDate();
        if (d < 10) {
        	d = '0' + d;
        }
        var m = punchInDateJs.getMonth();
        var y = punchInDateJs.getFullYear();
        var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
        m = monthNames[m];
        var punchDate = d + ' ' + m + ' ' + y;
	if (punchInMin < 10) {
        	punchInMin = '0' + punchInMin;
        }
	if (punchInHr < 12) {
        	if (punchInHr < 10) {
                	punchInHr = '0' + punchInHr;
                }
                punchIn = punchInHr + ':' + punchInMin + ' am';
                } else if (punchInHr == 12) {
                	punchIn = punchInHr + ':' + punchInMin + ' pm';
                } else {
                        punchInHr = punchInHr - 12;
                        if (punchInHr < 10) {
                            punchInHr = '0' + punchInHr;
                        }
                	punchIn = punchInHr + ':' + punchInMin + ' pm';
             	}
	return punchDate+","+punchIn;

}

function animateCircle(line) {
    var count = 0;
    window.setInterval(function() {
      count = (count + 1) % 200;

      var icons = line.get('icons');
      icons[0].offset = (count / 2) + '%';
      line.set('icons', icons);
  }, 100);
}

function calculatemins(time){
	var timesplit = time.split(":");
	var hours = parseInt(timesplit[0]);
	var minutes = parseInt(timesplit[1]);
	if(time.indexOf("am")!=-1 && hours==12){
		hours=0;
	}
	if(time.indexOf("pm")!=-1 && hours>=01 && hours!=12){
		hours=hours+12;
	}

	var mins=60*hours+minutes;
	return mins;
			
}

var counttravel1=0;
var counttravel2=0;

function mapRouteFromRoute(userId) {

	if(counttravel1!=counttravel2) 
	return;

	counttravel1++;

    	directionsDisplay.setMap(null);

	for (i = 0; i < markerArray.length; i++) {
    		markerArray[i].setMap(null);
   	}

	for (i = 0; i < ploylineArray.length; i++) {
     	 	ploylineArray[i].setMap(null);
    	}

    var dateSelected = document.getElementById("id_activityForDate").value;
    var now;
    if (dateSelected.length == 0) {
        var now1 = new Date();
        now = new Date(now1.getFullYear(), now1.getMonth(), now1.getDate());
    } else {
        var dateToSplit = dateSelected.split(' ');
        var y = dateToSplit[2];
        var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
        var m = monthNames.indexOf(dateToSplit[1]);
        var d = dateToSplit[0];
        now = new Date(y, m, d);
    }
    var dateToServer = convertLocalDateToServerDate(now.getFullYear(), now.getMonth(), now.getDate(), now.getHours(), now.getMinutes());
    var month = dateToServer.getMonth() + 1;
    if (month < 10) {
        month = '0' + month;
    }
    var date = dateToServer.getDate();
    if (date < 10) {
        date = '0' + date;
    }
    var hours = dateToServer.getHours();
    if (hours < 10) {
        hours = '0' + hours;
    }
    var minutes = dateToServer.getMinutes();
    if (minutes < 10) {
        minutes = '0' + minutes;
    }
    dateToServer = dateToServer.getFullYear() + "-" + month + "-" + date + " " + hours + ":" + minutes + ':00';
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/userTravelLog/user/" + userId + "/" + dateToServer,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            	if (data.errorCode == '0000') {
           		var userTraveldata = data.result[0];
			var attendanceData=data.result[1];
			var appointmentsData=data.result[2];
			var displayHtml="";
			var punchInTemplate="";
			var punchOutTemplate="";
			var appointmentTemplate="";
			//var traveledRouteDataHtml = "";
			var lastknownLocation="";
			var lastKnownTime="";
			if(userTraveldata.length == 0 && attendanceData.length == 0 && appointmentsData.length == 0){
				$('.cls_traveldroutesOnly').html("No activites for user");
				fieldSenseTosterError("No data to display the travelled route.", true);
				counttravel2++;
				$('#pleaseWaitDialog').modal('hide');
				return false;
                	}
                	else {
                    		var rendererOptions = {
                        		map: map,
					suppressMarkers : true
                   		}
                    		directionsService = new google.maps.DirectionsService();
                    		directionsDisplay = new google.maps.DirectionsRenderer(rendererOptions);
				//var geocoder =  new google.maps.Geocoder();
                   		var waypts1 = [];
		    		//var waypts2 = [];
				var timeperiod=[];
                   		var markerPoint = 0;
				var prevlong;
				var prevlat;
				var custlocatstarttime;
				var custlocatendtime;
				var custname;
				var count=0;
				var timecount;
				var timespan;
				var previscust;
				var prevcustname;
				var prevcustloc;
				var prevloc;
				var precustlatlong;
				var titlecount=0;
				var marker;
				var punch_in_time;
				var punch_in_location;
				var punch_out_time;
				var punch_out_location;
				var bounds = new google.maps.LatLngBounds();
				if (userTraveldata.length != 0) {
                    			for (var i = 0; i < userTraveldata.length; i++) {			
                        			var latitude = userTraveldata[i].latitude;
                        			var langitude = userTraveldata[i].langitude;
						if(latitude==0.0 && langitude==0.0){
							continue;
						}
                        			var isCustomerLocation = userTraveldata[i].isCustomerLocation;
						var custname= userTraveldata[i].customerName;
						var custloc= userTraveldata[i].locationIdentifier;
						var location= userTraveldata[i].location;
                        			var locationTime = userTraveldata[i].createdOn;
						var source_value = userTraveldata[i].sourceValue;
                        			var timeZoneOffSet = locationTime.timezoneOffset * 60 * 1000;
                        			locationTime = new Date(locationTime.time - timeZoneOffSet);
                        			//locationTime = convertServerDateToLocalDate(locationTime.getFullYear(), locationTime.getMonth(), locationTime.getDate(), locationTime.getHours(), locationTime.getMinutes());
                        			locationTime = convertServerDateToLocalTimezone(locationTime.getFullYear(), locationTime.getMonth(), 	locationTime.getDate(), locationTime.getHours(), locationTime.getMinutes(),timeZoneOffSet);
                        			locationTime = locationTime.getFullYear() + "-" + (locationTime.getMonth() + 1) + "-" + locationTime.getDate() + " " + locationTime.getHours() + ":" + locationTime.getMinutes() + ":00.0";
                        			locationTime = fieldSenseseForJSDate(locationTime);
						//console.log("latitude="+latitude+"langitude="+langitude+"locationTime="+locationTime);
			 			var location2 = new google.maps.LatLng(latitude, langitude);
						if((prevlat==latitude && prevlong==langitude) || (previscust==isCustomerLocation && prevcustname==custname.trim() && prevcustloc==custloc.trim())){
							if(isCustomerLocation){
							//timecount++;
							//var timeperiod=timecount*5;
								if(previscust==isCustomerLocation && prevcustname==custname.trim() && prevcustloc==custloc.trim()){

									custlocatendtime=locationTime.split(",")[1];
									timespan=calculatemins(custlocatendtime)-calculatemins(custlocatstarttime);
									timeperiod[count]=timespan;
									var title=custname+" @"+custlocatstarttime;
									//if(parseInt(timespan)>5){
									//	title=custname+" @"+custlocatstarttime+" for "+timespan+" mins";
									//}
									marker = new google.maps.Marker({
				     						position: precustlatlong,
				 						//label: "B",
										icon: "https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld="+count+"|00B200|FFFFFF",
				   	 					map: map,
				   	 					//title: title
										tooltip: '<B>'+title+'</B>'
									});
									markerArray.push(marker);
									var tooltip ;
			 						google.maps.event.addListener(marker, 'mouseover', (function(marker, titlecount) {
			   		 					return function() {
											tooltip = new Tooltip({map: map}, marker);
										tooltip.bindTo("text", marker, "tooltip");
			       							tooltip.addTip();
			    							tooltip.getPos2(marker.getPosition());
		  		 		 			}
       			 						})(marker, titlecount));

		 							google.maps.event.addListener(marker, 'mouseout',  (function(marker, titlecount) {
	 						 			return function() {
           										tooltip.removeTip();
										}
      			 			 			})(marker, titlecount));
									continue;
								}else{
									custlocatstarttime=locationTime.split(",")[1]
									//console.log("custlocationtime"+custlocationtime);
									custname=userTraveldata[i].customerName;
									prevcustname=custname.trim();
									previscust=isCustomerLocation;
									prevcustloc=custloc.trim();
									precustlatlong=location2;
									bounds.extend(location2);
									timespan=0;
									count++;
									timeperiod[count]=0;
					  				marker = new google.maps.Marker({
					     					position: location2,
					 					//label: "B",
										icon: "https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld="+count+"|00B200|FFFFFF",
					   	 				map: map,
					   					// title: custname+" @"+custlocatstarttime
										tooltip: '<B>'+custname+' @'+custlocatstarttime+'</B>'
					  				});
								}
							}else{
								if(i==userTraveldata.length-1){
									var label="assets/img/end.png";
									if(latitude==userTraveldata[0].latitude && langitude==userTraveldata[0].langitude){
										label="assets/img/startend.png";
										titlecount--;
									}
									if(prevlat==latitude && prevlong==langitude){
										titlecount--;
									}
									bounds.extend(location2);
									marker = new google.maps.Marker({
			    							position: location2,
			  							//icon: "https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld="+label+"|57b5e3|FFFFFF",
										icon:label,
			    							map: map,
			   							//title: locationTime.split(",")[1]
										tooltip: '<B>@'+locationTime.split(",")[1]+'</B>'
			 			 			});
						 			markerArray.push(marker);
								}else if(source_value==4){
								
									bounds.extend(location2);
				    					marker = new google.maps.Marker({
				    						position: location2,
				   						map: map,
				    						icon: "https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld=T|adadad|FFFFFF",
				    						title: locationTime.split(",")[1],
							 			tooltip: '<B>@'+locationTime.split(",")[1]+'</B>'
				  					});
								}else if(source_value==5){
								
									bounds.extend(location2);
				    					marker = new google.maps.Marker({
				    						position: location2,
				   						map: map,
				    						icon: "https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld=R|adadad|FFFFFF",
				    						title: locationTime.split(",")[1],
							 			tooltip: '<B>@'+locationTime.split(",")[1]+'</B>'
				  					});
								}else{
									continue;
								}
							}
						}else{
							if(isCustomerLocation){
								custlocatstarttime=locationTime.split(",")[1]
								//console.log("custlocationtime"+custlocationtime);
								custname=userTraveldata[i].customerName;
								prevcustname=custname.trim();
								previscust=isCustomerLocation;
								prevcustloc=custloc.trim();
								precustlatlong=location2;
								bounds.extend(location2);
								timespan=0;
								count++;
								timeperiod[count]=0;
				  				marker = new google.maps.Marker({
				     					position: location2,
				 					//label: "B",
									icon: "https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld="+count+"|00B200|FFFFFF",
				   	 				map: map,
				   					// title: custname+" @"+custlocatstarttime
									tooltip: '<B>'+custname+' @'+custlocatstarttime+'</B>'
				  				});
							}else{
								if(i==0){
									var label="assets/img/start.png";
									bounds.extend(location2);
									marker = new google.maps.Marker({
				    						position: location2,
				   						map: map,
										//icon: "https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld="+label+"|57b5e3|FFFFFF",
										icon:label,
				   						//title:locationTime.split(",")[1]
										tooltip: '<B>@'+locationTime.split(",")[1]+'</B>'
			  						});
								}else if(i==userTraveldata.length-1){
									var label="assets/img/end.png";
									if(latitude==userTraveldata[0].latitude && langitude==userTraveldata[0].langitude){
										label="assets/img/startend.png";
									}
									bounds.extend(location2);
									marker = new google.maps.Marker({
			    							position: location2,
			  							//icon: "https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld="+label+"|57b5e3|FFFFFF",
										icon:label,
			    							map: map,
			   							//title:locationTime.split(",")[1]
										tooltip: '<B>@'+locationTime.split(",")[1]+'</B>'
			 			 			});
								}else if(source_value==4){
								
									bounds.extend(location2);
				    					marker = new google.maps.Marker({
				    						position: location2,
				   						map: map,
				    						icon: "https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld=T|adadad|FFFFFF",
				    						title: locationTime.split(",")[1],
							 			tooltip: '<B>@'+locationTime.split(",")[1]+'</B>'
				  					});
								}else if(source_value==5){
								
									bounds.extend(location2);
				    					marker = new google.maps.Marker({
				    						position: location2,
				   						map: map,
				    						icon: "https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld=R|adadad|FFFFFF",
				    						title: locationTime.split(",")[1],
							 			tooltip: '<B>@'+locationTime.split(",")[1]+'</B>'
				  					});
								}else{
									var lineSymbol = {
			   	 						path: google.maps.SymbolPath.CIRCLE,
			   							scale: 3,
			   	 						strokeColor: '#57b5e3'
			 		 				};
									bounds.extend(location2);
			    						marker = new google.maps.Marker({
			    							position: location2,
			   							map: map,
			    							//icon: "https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld="+k+"|57b5e3|000000",
			    							icon:lineSymbol,
			    							//title: locationTime.split(",")[1]
						 				tooltip: '<B>@'+locationTime.split(",")[1]+'</B>'
			  						});

					
								}
							prevloc=userTraveldata[i].location.trim();
			   				}

						}
		
			 			markerArray.push(marker);
			 			waypts1.push({"Geometry":{"Latitude":latitude,"Longitude":langitude}});
			

						prevlat=latitude;
						prevlong=langitude;
 						var tooltip ;
			 			google.maps.event.addListener(marker, 'mouseover', (function(marker, titlecount) {
		   		 			return function() {
								tooltip = new Tooltip({map: map}, marker);
								tooltip.bindTo("text", marker, "tooltip");
		       						tooltip.addTip();
		    						tooltip.getPos2(marker.getPosition());
		  		  			}
       			 			})(marker, titlecount));

		 				google.maps.event.addListener(marker, 'mouseout',  (function(marker, titlecount) {
	 		 				return function() {
           							tooltip.removeTip();
							}
      			  			})(marker, titlecount));

  						map.fitBounds(bounds);
						//console.log("prevloc:"+prevloc);
			      	 		/*if (isCustomerLocation) {
		                   			markerPoint++;
		                    			var customerName = userTraveldata[i].customerName;
		                   			var location = userTraveldata[i].location;
		                    			traveledRouteDataHtml += '<div class="form-body">';
		                    			traveledRouteDataHtml += '<div class="form-group">';
		                    			traveledRouteDataHtml += '<span class="badge badge-info">' + markerPoint + '</span>';
		                    			traveledRouteDataHtml += '<label class="col-md-12 title-label"><a href="#">' + customerName + '</a></label>';
		                    			traveledRouteDataHtml += '<label class="col-md-12 time-label">' + locationTime + '</label>';
		                    			traveledRouteDataHtml += '<label class="col-md-12 loc-label">' + location + '</label>';
		                    			traveledRouteDataHtml += '</div>';
		                    			traveledRouteDataHtml += '</div>';
		                		}*/
						titlecount++;
		                		/*traveledRouteDataHtml += '<div class="form-body">';
		                		traveledRouteDataHtml += '<div class="form-group">';
                       				if(isCustomerLocation) {
							traveledRouteDataHtml += '<span class="badge badge-success">' + count+ '</span>';
						}
						else if(i==0){
                       					traveledRouteDataHtml += '<span class="badge badge-info">S</span>';
						}else if(i==userTraveldata.length-1){
							traveledRouteDataHtml += '<span class="badge badge-info">E</span>';
						}
						else{
			 				traveledRouteDataHtml += '<span class="inmeet" style="float:left">&nbsp;</span>';
						}
                        			traveledRouteDataHtml += '<label class="col-md-12 title-label"><a href="#">' + locationTime.split(",")[1]+'</a>'+'</label>' ;
                        			if(isCustomerLocation){
                            				traveledRouteDataHtml += '<label class="col-md-12 loc-label"><a href="#">'+custname +'</a>'+'</label>';
                        			}
                       	 			traveledRouteDataHtml += '<label class="col-md-12 loc-label">' + location + '</label>';
                        			traveledRouteDataHtml += '<label class="col-md-12 loc-label">'+'Distance: ' + userTraveldata[i].travelDistance + ' km'+'</label>';
						if(isCustomerLocation){
			 				traveledRouteDataHtml += '<label class="col-md-12 loc-label custmrloc" style="display:none">Time Spent: 0mins</label>';
						}
                        			traveledRouteDataHtml += '</div>';
                        			traveledRouteDataHtml += '</div>';*/
			
                    			}

                    			//$('.cls_traveldroutesOnly').html(traveledRouteDataHtml);
					var symbolTwo = {
     						path: 'M-4,-4 L0,-8 L4,-4',
    						strokeColor: '#000000',
 						strokeWeight: 1
		  			};
					for(var i=0;i<waypts1.length-1;i++){
						var line = new google.maps.Polyline({
			 				path: [ {lat: waypts1[i].Geometry.Latitude, lng: waypts1[i].Geometry.Longitude},{lat:waypts1[i+1].Geometry.Latitude, lng:  waypts1[i+1].Geometry.Longitude}],
	  			  			icons: [
	    				  		{
	       				 			icon: symbolTwo,
	       				 			offset: '50%'
	     		 				}
	   				 		],
							strokeColor:"#000000",
							strokeWeight: 1,
  		  					map: map
  						});

 				 		//animateCircle(line);

					ploylineArray.push(line);
					}
                   	 		//var waypts2 = waypts;
                 			/*if (waypts2.length <= 10) {
						for(var i=0;i<waypts2.length;i++){
		               				geocoder.geocode({ 'latLng':new window.google.maps.LatLng(waypts2[i].Geometry.Latitude,waypts2[i].Geometry.Longitude)}, function (results, status) {
                						if (status == google.maps.GeocoderStatus.OK) {
                    							if (results[0]) {
                        							console.log("Location: " + results[0].formatted_address);
			 							waypts1.push({
            									location:results[0].formatted_address,
            									stopover:true});
                    							} caliculateUserRoute(waypts1, directionsService, directionsDisplay);
               							}
           						});
						}

                   			} else {
                       				//for (var j = 0; j < waypts2.length; j= j + 10) {
                        				//var k = j + 10;
                       					//if (k > waypts.length) {
                        					//k = waypts.length;
                         				//}
                         				//var waypoints = waypts.slice(j, k);
                            				caliculateUserRouteMoreThanTen(waypts2, directionsService, directionsDisplay);
               
                     	 			//}

                   			}*/
		 			// Override our map zoom level once our fitBounds function runs (Make sure it only runs once)
		    			var boundsListener = google.maps.event.addListener((map), 'bounds_changed', function(event) {
						this.setZoom(13);
						google.maps.event.removeListener(boundsListener);
		    			});
					for (i = 0; i < markers.length; i++) {
	      					markers[i].setMap(null);
	   		 		}
			
					var lastknownLocation=userTraveldata[userTraveldata.length-1].location;
					var lastKnownTime=userTraveldata[userTraveldata.length-1].createdOn;
				
					/*var index=1;
					$('.custmrloc').each(function(){
						if(parseInt(timeperiod[index])>5){
							$(this).show();
							$(this).html('Time Spent:'+timeperiod[index]+'mins');
						}
						index++;
					});*/
				}else{
					//$('.cls_traveldroutesOnly').html("No activites for user");
					fieldSenseTosterError("No data to display the travelled route.", true);
				}
				if(attendanceData.length !=0){
					for (var i = 0; i < attendanceData.length; i++) {
						var date = attendanceData[i].punchDate;
                    				var dateSplit = date.split('-');
						punch_in_time=attendanceData[i].punchInTime;
						punch_in_time=convertTimeToLocal(punch_in_time,dateSplit).split(",")[1];
						punch_in_location=attendanceData[i].punchInLocation;				
						punchInTemplate +='<div class="form-body">';
						punchInTemplate +='<div class="form-group">';
						punchInTemplate += '<span class="badge badge-info">S</span>';
						punchInTemplate += '<label class="col-md-12 title-label"><a href="#">Punch In : ' + punch_in_time+'</a>'+'</label>' ;
						punchInTemplate += '<label class="col-md-12 loc-label">' +  punch_in_location + '</label>';
				            	//punchInTemplate += '<label class="col-md-12 loc-label"></label>';
				            	punchInTemplate += '</div>';
						punchInTemplate += '</div>';
						displayHtml+=punchInTemplate;
						punchOutTemplate="";
						punch_out_time=attendanceData[i].punchOutTime;
						var label="";
						if(punch_out_time != '00:00:00'){
							var punchOutDate = attendanceData[i].punchOutDate;
                    					var punchOutDateSplit = punchOutDate.split('-');
							punch_out_time=attendanceData[i].punchOutTime;
							punch_out_time=convertTimeToLocal(punch_out_time,punchOutDateSplit);
							if(date==punchOutDate){
								punch_out_time=punch_out_time.split(",")[1];
							}
							punch_out_location=attendanceData[i].punchOutLocation;
							label="Punch Out";
						}else{
							punch_out_time=lastKnownTime;
							punch_out_location=lastknownLocation;
							if(punch_out_time !=""){
								punch_out_time=convertDateToLocal(punch_out_time);
								punch_out_time=punch_out_time.split(",")[1];
							}
							label="Last Known Location";
						}
						punchOutTemplate +='<div class="form-body">';
						punchOutTemplate +='<div class="form-group">';
				            	punchOutTemplate += '<span class="badge badge-info">E</span>';
						if(label=="Last Known Location"){
							punchOutTemplate += '<label class="col-md-12 title-label"><a href="#">' +  label +'</a>'+'</label>';
							punchOutTemplate += '<label class="col-md-12 loc-label"><a href="#">'+punch_out_time +'</a>'+'</label>';
							punchOutTemplate += '<label class="col-md-12 loc-label">' + punch_out_location + '</label>';
				            	//punchOutTemplate += '<label class="col-md-12 loc-label"></label>';
						}else {
							punchOutTemplate += '<label class="col-md-12 title-label"><a href="#">Punch Out : ' +  punch_out_time+'</a>'+'</label>' ;
							punchOutTemplate += '<label class="col-md-12 loc-label">' + punch_out_location + '</label>';
						}
				            	punchOutTemplate += '</div>';
						punchOutTemplate += '</div>';
						
					}
				}
				var app_count=0;
				if(appointmentsData.length !=0){
					for (var i = 0; i < appointmentsData.length; i++) {
						app_count++;
						var customername=appointmentsData[i].customer.customerName;
						var title = appointmentsData[i].title;
						var status = appointmentsData[i].status;
                    				var checkInTime = appointmentsData[i].checkInTime;
						checkInTime=convertDateToLocal(checkInTime);
						if(checkInTime.indexOf("01 Jan 1970")!=-1){
							if(status!=0 && status!=3){
								checkInTime = appointmentsData[i].dateTime;
								checkInTime=convertDateToLocal(checkInTime);
								checkInTime=checkInTime.split(",")[1];
						
							}else{
								checkInTime = "--:--";
							}
						}else{
							if(status!=0 && status!=3){
								checkInTime=checkInTime.split(",")[1];
							}else{
								checkInTime = "--:--";
							}
						}
						var checkInLocation=appointmentsData[i].checkInLocation;			
						var checkOutTime = appointmentsData[i].checkOutTime;
						checkOutTime=convertDateToLocal(checkOutTime);
						if(checkOutTime.indexOf("01 Jan 1970")!=-1){
							if(status==2){
								checkOutTime = appointmentsData[i].endTime;
								checkOutTime=convertDateToLocal(checkOutTime);
								checkOutTime=checkOutTime.split(",")[1];
						
							}else{
								checkOutTime = "--:--";
							}
						}else{
							if(status==2){
								checkOutTime=checkOutTime.split(",")[1];
							}else{
								checkOutTime = "--:--";
							}
						}
						var checkOutLocation=appointmentsData[i].checkOutLocation;
						apptTemplate ='<div class="form-body">';
						apptTemplate +='<div class="form-group">';
						apptTemplate += '<span class="badge badge-success">' + app_count+ '</span>';
						apptTemplate += '<label class="col-md-12 title-label"><a href="#">' + title+'</a>'+'</label>' ;
						apptTemplate += '<label class="col-md-12 loc-label"><a href="#">'+customername +'</a>'+'</label>';
				            	apptTemplate += '<label class="col-md-12 loc-label">CheckIn Time : ' + checkInTime + '</label>';
						
						apptTemplate += '<label class="col-md-12 loc-label">CheckOut Time : ' + checkOutTime + '</label>';
						
				            	apptTemplate += '</div>';
						apptTemplate += '</div>';
						appointmentTemplate +=apptTemplate;
						
					}
					displayHtml+=appointmentTemplate;
				}
                	}
			displayHtml+=punchOutTemplate;
			$('.cls_traveldroutesOnly').html(displayHtml);
           	}else{
			fieldSenseTosterError(data.errorMessage, true);
		}
		$('#pleaseWaitDialog').modal('hide');		
		counttravel2++;
       	  }
    });
}

/*function caliculateUserRouteMoreThanTen(stops, directionsService, directionsDisplay){
var batches = [];
var itemsPerBatch = 10; // google API max - 1 start, 1 stop, and 8 waypoints
var itemsCounter = 0;
var wayptsExist = stops.length > 0;
 
while (wayptsExist) {
    var subBatch = [];
    var subitemsCounter = 0;
 
    for (var j = itemsCounter; j < stops.length; j++) {
        subitemsCounter++;
        subBatch.push({
            location:new window.google.maps.LatLng(stops[j].Geometry.Latitude, stops[j].Geometry.Longitude),
            stopover:true
        });
        if (subitemsCounter == itemsPerBatch)
            break;
    }
 
    itemsCounter += subitemsCounter;
    batches.push(subBatch);
    wayptsExist = itemsCounter < stops.length;
    // If it runs again there are still points. Minus 1 before continuing to 
    // start up with end of previous tour leg
    itemsCounter--;
}



    var combinedResults;
    var unsortedResults = [{}]; // to hold the counter and the results themselves as they come back, to later sort
    var directionsResultsReturned = 0;
     
    for (var k = 0; k < batches.length; k++) {
        var lastIndex = batches[k].length - 1;
        var start = batches[k][0].location;
        var end = batches[k][lastIndex].location;
         
        // trim first and last entry from array
        var waypts = [];
        waypts = batches[k];
        waypts.splice(0, 1);
        waypts.splice(waypts.length - 1, 1);
         
        var request = {
            origin : start,
            destination : end,
            waypoints : waypts,
            travelMode : window.google.maps.TravelMode.DRIVING
        };
        (function (kk) {
            directionsService.route(request, function (result, status) {
                if (status == window.google.maps.DirectionsStatus.OK) {
                     
                    var unsortedResult = {
                        order : kk,
                        result : result
                    };
                    unsortedResults.push(unsortedResult);
                     
                    directionsResultsReturned++;
                     
                    if (directionsResultsReturned == batches.length) // we've received all the results. put to map
                    {
                        // sort the returned values into their correct order
                        unsortedResults.sort(function (a, b) {
                            return parseFloat(a.order) - parseFloat(b.order);
                        });
                        var count = 0;
                        for (var key in unsortedResults) {
                            if (unsortedResults[key].result != null) {
                                if (unsortedResults.hasOwnProperty(key)) {
                                    if (count == 0) // first results. new up the combinedResults object
                                        combinedResults = unsortedResults[key].result;
                                    else {
                                        // only building up legs, overview_path, and bounds in my consolidated object. This is not a complete
                                        // directionResults object, but enough to draw a path on the map, which is all I need
                                        combinedResults.routes[0].legs = combinedResults.routes[0].legs.concat(unsortedResults[key].result.routes[0].legs);
                                        combinedResults.routes[0].overview_path = combinedResults.routes[0].overview_path.concat(unsortedResults[key].result.routes[0].overview_path);
                                         
                                        combinedResults.routes[0].bounds = combinedResults.routes[0].bounds.extend(unsortedResults[key].result.routes[0].bounds.getNorthEast());
                                        combinedResults.routes[0].bounds = combinedResults.routes[0].bounds.extend(unsortedResults[key].result.routes[0].bounds.getSouthWest());
                                    }
                                    count++;
                                }
                            }
                        }
                        directionsDisplay.setDirections(combinedResults);
                    }
                }
            });
        })(k);
    }


}
/*function wayPointsDivide(stops) {
 var batches = [];
 var itemsPerBatch = 10; // google API max - 1 start, 1 stop, and 8 waypoints
 var itemsCounter = 0;
 var wayptsExist = stops.length > 0;
 
 while (wayptsExist) {
 var subBatch = [];
 var subitemsCounter = 0;
 
 for (var j = itemsCounter; j < stops.length; j++) {
 subitemsCounter++;
 var location2 = new google.maps.LatLng(stops[j].latitude, stops[j].langitude);
 subBatch.push({
 location: location2,
 stopover: stops[j].isCustomerLocation
 });
 if (subitemsCounter == itemsPerBatch)
 break;
 }
 
 itemsCounter += subitemsCounter;
 batches.push(subBatch);
 wayptsExist = itemsCounter < stops.length;
 // If it runs again there are still points. Minus 1 before continuing to
 // start up with end of previous tour leg
 itemsCounter--;
 }
 return batches;
 }
 function caliculateUserRoute(batches, directionsService, directionsDisplay) {
 var combinedResults;
 var directionsResultsReturned = 0;
 
 for (var k = 0; k < batches.length; k++) {
 var lastIndex = batches[k].length - 1;
 var start = batches[k][0].location;
 var end = batches[k][lastIndex].location;
 
 // trim first and last entry from array
 var waypts = [];
 waypts = batches[k];
 waypts.splice(0, 1);
 waypts.splice(waypts.length - 1, 1);
 
 var request = {
 origin: start,
 destination: end,
 waypoints: waypts,
 travelMode: window.google.maps.TravelMode.DRIVING
 };
 directionsService.route(request, function (result, status) {
 if (status == window.google.maps.DirectionsStatus.OK) {
 if (directionsResultsReturned == 0) { // first bunch of results in. new up the combinedResults object
 combinedResults = result;
 directionsResultsReturned++;
 }
 else {
 // only building up legs, overview_path, and bounds in my consolidated object. This is not a complete
 // directionResults object, but enough to draw a path on the map, which is all I need
 combinedResults.routes[0].legs = combinedResults.routes[0].legs.concat(result.routes[0].legs);
 combinedResults.routes[0].overview_path = combinedResults.routes[0].overview_path.concat(result.routes[0].overview_path);
 
 combinedResults.routes[0].bounds = combinedResults.routes[0].bounds.extend(result.routes[0].bounds.getNorthEast());
 combinedResults.routes[0].bounds = combinedResults.routes[0].bounds.extend(result.routes[0].bounds.getSouthWest());
 directionsResultsReturned++;
 }
 if (directionsResultsReturned == batches.length) // we've received all the results. put to map
 directionsDisplay.setDirections(combinedResults);
 showSteps(combinedResults);
 }
 });
 }
 }
function caliculateUserRoute(waypoints, directionsService, directionsDisplay) {

    var start = waypoints[0].location;

    var end = waypoints[waypoints.length - 1].location;
	
    var waypts = waypoints.splice(1, waypoints.length - 2);
	 for (i = 0; i < markerArray.length; i++) {
      markerArray[i].setMap(null);
    }

    // Now, clear the array itself.
    markerArray = [];

    var request = {
        origin: start,
        destination: end,
        waypoints: waypts,
        travelMode: window.google.maps.TravelMode.DRIVING
    };
    directionsService.route(request, function (result, status) {
        if (status == window.google.maps.DirectionsStatus.OK) {
            var distance =result.routes[0].legs[0].distance.value;
           directionsDisplay.setDirections(result);
        }
    });
}
*/
