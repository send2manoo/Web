/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var intervalIndex = window.setInterval(function () {
    var userToken2 = fieldSenseGetCookie("userToken");
    var teamId = fieldSenseGetCookie("teamId");
    try {
        if (userToken2.toString() == "undefined") {
            window.location.href = "login.html";
        } else if (loginUserId != 0) {
            loggedinUserImageData();
            loggedinUserData();
            userDashBoard(loginUserId, teamId);
            window.clearTimeout(intervalIndex);
        }
    } catch (err) {
        window.location.href = "login.html";
    }
}, 1000);
var appointmentData;
var attendanceUserData;
var attendanceData;
function userDashBoard(dashboardId, teamId)
{
    time = new Date();
    var loggedInUserDataHtml = '<img alt="" src="' + imageURLPath + accountId + '_' + loginUserId + '_29X29.png?' + time + '" onerror="this.src=\'assets/img/usrsml_29.png\';"/>'
    loggedInUserDataHtml += '<span class="membername"> ' + loginUserName + '';
    loggedInUserDataHtml += '</span>';
    $('#id_loggedinUser').html(loggedInUserDataHtml);
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/dashboard/" + teamId,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            var dashBoardData = data.result;

            appointmentData = dashBoardData[0];
            attendanceUserData = dashBoardData[1];
            attendanceData = dashBoardData[2];

            var expenseData = dashBoardData[3];
            var leaderboardData = dashBoardData[4];
            var customerData = dashBoardData[5];
            var formsFilledData = dashBoardData[6];

            var date = new Date();
            var m = date.getMonth() + 1;
            if (m < 10) {
                m = '0' + m;
            }
            var y = date.getFullYear();
            var d = date.getDate();
            if (d < 10) {
                d = '0' + d;
            }
            var hour = date.getHours();
            var AMPM = hour > 12 ? ' PM' : ' AM';
            hour = hour > 12 ? hour - 12 : hour;

            var minute = date.getMinutes();
            hour = hour + parseInt(minute / 60);
            minute = minute % 60;
            minute = minute < 10 ? '0' + minute : minute;

            hour = hour < 10 ? '0' + hour.toString() : hour;
            m = m == 1 ? "January" : m == 2 ? "February" : m == 3 ? "March" : m == 4 ? "April" : m == 5 ? "May" : m == 6 ? "June" : m == 7 ? "July" : m == 8 ? "August" : m == 9 ? "September" : m == 10 ? "October" : m == 11 ? "November" : "December";
//            m=m==1?"Jan":m==2?"Feb":m==3?"Mar":m==4?"Apr":m==5?"May":m==6?"Jun":m==7?"Jul":m==8?"Aug":m==9?"Sep":m==10?"Oct":m==11?"Nov":"Dec";
            var firstDay = m + ' ' + d + ', ' + y + ' : ' + hour + ':' + minute + ' ' + AMPM;
            var userDashBoardDataHtml = "";
            userDashBoardDataHtml += "<div class='page-container'>";
            userDashBoardDataHtml += "<div class='page-content-wrapper'>";
            userDashBoardDataHtml += "<div class='page-content-wrapper'>";
            userDashBoardDataHtml += "<div id='dashboard'>";
            userDashBoardDataHtml += "<div class='row'>";
            userDashBoardDataHtml += "<div class='col-md-12'>";
            userDashBoardDataHtml += "<h3 class='page-title' style='float: left;'><small><i class='fa fa-calendar'></i> " + firstDay + "</small></h3>";

//            userDashBoardDataHtml += "<h3 class='page-title' style='float: left;'>Dashboard</h3>";
//
//            userDashBoardDataHtml+="<ul class='page-breadcrumb breadcrumb' style='margin-top: 0px;'>";
//            userDashBoardDataHtml+="<li class='pull-right'>";
//            userDashBoardDataHtml+="<div id='dashboard-report-range' class='dashboard-date-range tooltips' data-placement='bottom' data-original-title='' style='display: block;'>";
//            userDashBoardDataHtml+="<i class='fa fa-calendar'></i>";
//            userDashBoardDataHtml+="<span>"+firstDay+"</span>";
////            userDashBoardDataHtml+="<i class='fa fa-angle-down'></i>";
//            userDashBoardDataHtml+="</div>";
//            userDashBoardDataHtml+="</li></ul>";


            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "<div class='row'>";
            userDashBoardDataHtml += "<div class='col-md-5'>";
            userDashBoardDataHtml += "<div class='well'>";
            userDashBoardDataHtml += Visits(appointmentData);
            userDashBoardDataHtml += "<!-- END Portlet PORTLET-->";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "<div class='col-md-3'>";
            userDashBoardDataHtml += "<div class='row'>";
            userDashBoardDataHtml += "<div class='col-md-12'>";
            userDashBoardDataHtml += "<div class='well'>";
            userDashBoardDataHtml += Attendance(attendanceData, attendanceUserData);
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "<div class='row' style='margin-top: 10px;'>";
            userDashBoardDataHtml += "<div class='col-md-12'>";
            userDashBoardDataHtml += "<div class='well'>";
            userDashBoardDataHtml += Expenses(expenseData);
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "<div class='col-md-2 row-eq-height'>";
            userDashBoardDataHtml += "<div class='row'>";
            userDashBoardDataHtml += "<div class='col-md-12'>";
            userDashBoardDataHtml += "<div class='well'>";
            userDashBoardDataHtml += newCustomers(customerData);
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "<div class='row' style='margin-top: 10px;'>";
            userDashBoardDataHtml += "<div class='col-md-12'>";
            userDashBoardDataHtml += "<div class='well'>";
            userDashBoardDataHtml += formFilled(formsFilledData);
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "<div class='col-md-2 row-eq-height'>";
            userDashBoardDataHtml += "<div class='well2'>";
//            userDashBoardDataHtml += "<!-- BEGIN Portlet PORTLET-->";
//            userDashBoardDataHtml += "<div class='portlet'>";
//            userDashBoardDataHtml += "<div class='portlet-title'>";
//            userDashBoardDataHtml += "<div class='caption'> Insights";
//            userDashBoardDataHtml += "</div>";
//            userDashBoardDataHtml += "</div>";
//            userDashBoardDataHtml += "<div class='portlet-body'>";
//            userDashBoardDataHtml += "<div class='' style='height:200px' data-rail-visible='1' data-rail-color='fff' data-handle-color='#f9f9f9'>";
//            userDashBoardDataHtml += "</div>";
//            userDashBoardDataHtml += "</div>";
//            userDashBoardDataHtml += "</div>";
//            userDashBoardDataHtml += "<!-- END Portlet PORTLET-->";
            userDashBoardDataHtml += "<!-- BEGIN Portlet PORTLET-->";
            userDashBoardDataHtml += "<div class='portlet'>";
            userDashBoardDataHtml += "<div class='portlet-title'>";
            userDashBoardDataHtml += "<div class='caption'>Leaderboard";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "<div class='portlet-body'>";
            userDashBoardDataHtml += "<div class='scrollerLeaderboard' style='height: 172px;' data-rail-visible='1' data-rail-color='eeeeee' data-handle-color='#a1b2bd'>";
            userDashBoardDataHtml += leaderBoard(leaderboardData);
            //    userDashBoardDataHtml += "</div><div class='slimScrollBar' style='background: rgb(249, 249, 249) none repeat scroll 0% 0%; width: 7px; position: absolute; top: 0px; opacity: 0.4; display: block; border-radius: 4px; z-index: 99; right: 1px; height: 190.491px;'></div><div class='slimScrollRail' style='width: 7px; height: 100%; position: absolute; top: 0px; display: none; border-radius: 4px; opacity: 0.2; z-index: 90; right: 1px;'></div></div>";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "</div></div>";
            userDashBoardDataHtml += "<!-- END Portlet PORTLET-->";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "<div class='row' style='margin-top: 10px;'>";
            userDashBoardDataHtml += "<div class='col-md-6'>";
            userDashBoardDataHtml += "<div class='well'>";
            userDashBoardDataHtml += "<!-- BEGIN Portlet PORTLET-->";
            userDashBoardDataHtml += "<div class='portlet tab'>";
            userDashBoardDataHtml += "<div class='portlet-title'>";
            userDashBoardDataHtml += "<div class='caption'>Current Visits";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "<div class='actions'>";
            userDashBoardDataHtml += "<select class='form-control' id='id_currentVisits' onchange='getCurrentVisits(this);' data-placeholder='Select...'>";
            userDashBoardDataHtml += "<option value='All' selected=''>All</option>";
            userDashBoardDataHtml += "<option value='Completed'>Completed</option>";
            userDashBoardDataHtml += "<option value='In-Progress'>In-Progress</option>";
            userDashBoardDataHtml += "<option value='Pending'>Pending</option>";
            userDashBoardDataHtml += "<option value='Missed'>Missed</option>";
            userDashBoardDataHtml += "</select>";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "<div class='portlet-body'>";
            // userDashBoardDataHtml += "<div  class='slimScrollDiv' style='position: relative; overflow: scroll; width: auto; height: 275px;'>";
            userDashBoardDataHtml += "<div id='dv_appointmentData' class='scroller' style='height: 300px;' data-rail-visible='1' data-rail-color='eeeeee' data-handle-color='#a1b2bd'>";
            userDashBoardDataHtml += currentVisits(appointmentData, 0);
            //userDashBoardDataHtml += "</div><div id='dv_Visitscroll' class='slimScrollBar' style='background: rgb(161, 178, 189) none repeat scroll 0% 0%; width: 7px; position: absolute; top: 0px; opacity: 0.4; display: none; border-radius: 4px; z-index: 99; right: 1px; height: 219.84px;'></div><div class='slimScrollRail' id='dv_slimScrollRail' style='width: 7px; height: 100%; position: absolute; top: 0px; border-radius: 4px; opacity: 0.2; z-index: 90; right: 1px; display: none;'></div></div>";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "</div></div>";
            userDashBoardDataHtml += "<!-- END Portlet PORTLET-->";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "<div class='col-md-6'>";
            userDashBoardDataHtml += "<div class='well'>";
            userDashBoardDataHtml += "<!-- BEGIN Portlet PORTLET-->";
            userDashBoardDataHtml += "<div class='portlet tab'>";
            userDashBoardDataHtml += "<div class='portlet-title'>";
            userDashBoardDataHtml += "<div class='caption'>My Team";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "<div class='actions'>";
            userDashBoardDataHtml += "<select class='form-control' data-placeholder='Select...' onchange='GetValue(this);'>";
            userDashBoardDataHtml += "<option value='All'>All</option>";
            userDashBoardDataHtml += "<option value='Punched-In' selected=''>Punched-In</option>";
            userDashBoardDataHtml += "<option value='On-Field'>On-Field</option>";
            userDashBoardDataHtml += "<option value='In-Office'>In-Office</option>";
            userDashBoardDataHtml += "<option value='Punched-Out'>Punched-Out</option>";
            userDashBoardDataHtml += "<option value='Not Punched-In'>Not Punched-In</option>";
            userDashBoardDataHtml += "</select>";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "<div class='portlet-body'>";
            //userDashBoardDataHtml += "<div id='dv_attendanceData' class='slimScrollDiv' style='position: relative; overflow: hidden; width: auto; height: 275px;'><div class='scroller' style='height: 275px; overflow: hidden; width: auto;' data-rail-visible='1' data-rail-color='eeeeee' data-handle-color='#a1b2bd'>";
            userDashBoardDataHtml += "<div id='dv_attendanceData' class='scrollerMyTeam' style='height:175px' data-rail-visible='1' data-rail-color='eeeeee' data-handle-color='#a1b2bd'>";
            userDashBoardDataHtml += myTeam(attendanceData, 0);
            //userDashBoardDataHtml += "</div><div class='slimScrollBar' style='background: rgb(161, 178, 189) none repeat scroll 0% 0%; width: 7px; position: absolute; top: 0px; opacity: 0.4; display: block; border-radius: 4px; z-index: 99; right: 1px; height: 219.84px;'></div><div class='slimScrollRail' style='width: 7px; height: 100%; position: absolute; top: 0px; display: none; border-radius: 4px; opacity: 0.2; z-index: 90; right: 1px;'></div></div>";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "</div></div>";
            userDashBoardDataHtml += "<!-- END Portlet PORTLET-->";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "</div>";

            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "<!-- END PAGE CONTENT-->";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "</div>";
            userDashBoardDataHtml += "<!-- END CONTENT -->";
            userDashBoardDataHtml += "</div>";
            $('#id_dashboardData').html(userDashBoardDataHtml);
            App.init();
            $(".scroller").slimScroll({scrollTo: '0px'});
            $(".scrollerMyTeam").slimScroll({scrollTo: '0px'});
            $(".scrollerLeaderboard").slimScroll({scrollTo: '0px'});  
            var scroll = $(".test").offset().top;
            var scroll1=0;
//            if($("td").hasClass("pd"))
//            {
//                scroll1 = $(".pd").offset().top;
//                scroll1=scroll1-scroll+100;
//            }
            if($("td").hasClass("cmp"))
            {
                scroll1=$(".cmp").offset().top;
                scroll1=scroll-scroll1;
            }
//            else if($("td").hasClass("pd"))
//            {
//                scroll1 = $(".pd").offset().top;
//                s   else if($("td").hasClass("pd"))croll1=scroll1-scroll+100;
//            }
            
            $(".scroller").slimScroll({scrollTo:scroll1+'px'});
           //  $(".scroller").slimScroll({scrollTo:'0 px'});
           // $(".scroller").slimScroll({scrollTo: $(".test").offset().top + 'px'});
//            $(document).ready(function () {
//
//        
//
//            });
        }
    });
}
function Visits(appointmentData) {
    var visitsDataHtml = "";
    var countMissed = 0;
    var countCompleted = 0;
    var countInprogress = 0;
    var countPending = 0;
    var totalAppointment = 0;
    var totalVisits = 0;
    var percentofCompleted = 0;
    var percentofInprogress = 0;
    var percentofPending = 0;
    var percentofMissed = 0;

    for (var i = 0; i < appointmentData.length; i++) {
        totalAppointment++;
        var status = appointmentData[i].appointment.status;
        var appointmentEndTime = appointmentData[i].appointment.sendTime;
        var recordState = appointmentData[i].appointment.recordState;
        appointmentEndTime = appointmentEndTime.toLocaleString().split(" ");
        var discardappointmentEndTimeAMPM = appointmentEndTime[1].split(":");


//        var activityDateTime = appointmentTime.split(' ');
//        var time = activityDateTime[1];
//        var timeSplit = time.split(':');
//        var timeSplit1 = '05:30'.split(':');

        var hours = parseInt(discardappointmentEndTimeAMPM[0]);
        hours=hours*60+parseInt(discardappointmentEndTimeAMPM[1])+330;
        
        var curHour=new Date().getHours();
        curHour=curHour*60+new Date().getMinutes();
        
//        var minutes = parseInt(discardappointmentEndTimeAMPM[1]) + parseInt(timeSplit1[1]);
//        hours = hours + parseInt(minutes / 60);
//        minutes = minutes % 60;
//        minutes = minutes < 10 ? '0' + minutes : minutes;
//        var AMPM = hours > 12 ? ' PM' : ' AM'
////        hours = hours > 12 ? '0' + (hours - 12) : hours;
//        var t1 = new Date().getHours();
//        var t2 = new Date().getMinutes();
//        t1 = t1 * 60 + t2;
//        debugger;
        if (status == 2 && recordState != 3)
        {
            countCompleted++;
        } else if (status == 1 && recordState != 3)
        {
            countInprogress++;
        } else if ((parseInt(hours) < curHour && status == 0 && recordState != 3))
        {
            countMissed++;
        } else if ((parseInt(hours) >= curHour && status == 0 && recordState != 3))
        {
            countPending++;
        }

    }
    percentofCompleted = (countCompleted / totalAppointment) * 100;
    percentofInprogress = (countInprogress / totalAppointment) * 100;
    percentofPending = (countPending / totalAppointment) * 100;
    percentofMissed = (countMissed / totalAppointment) * 100;
    percentofCompleted = isNaN(percentofCompleted) ? 0 : percentofCompleted;
    percentofInprogress = isNaN(percentofInprogress) ? 0 : percentofInprogress;
    percentofPending = isNaN(percentofPending) ? 0 : percentofPending;
    percentofMissed = isNaN(percentofMissed) ? 0 : percentofMissed;

    totalVisits = countCompleted;
    visitsDataHtml += "<div class='portlet'>";
    visitsDataHtml += "<div class='portlet-title'>";
    visitsDataHtml += "<div class='caption'> Visits";

    visitsDataHtml += "</div>";
//    visitsDataHtml += "<div class='actions'>";
//    visitsDataHtml += "<a href='index.html' class='btn btn-default btn-sm'><i class='fa fa-plus'></i></a>";
////                visitsDataHtml += "<a href='#' class='btn btn-default btn-sm'><i class='fa fa-reorder'></i></a>";
//    visitsDataHtml += "</div>";
    visitsDataHtml += "<div class='chart_data'>";
    visitsDataHtml += "<b class='sm'>" + totalVisits + "</b>/" + totalAppointment + "";
    visitsDataHtml += "</div>";
    visitsDataHtml += "</div>";
    visitsDataHtml += "<div class='portlet-body'>";
    visitsDataHtml += "<div class='slimScrollDiv' style='position: relative; overflow: hidden; width: auto; height: 176px;'><div class='scroller' style='height: 176px; overflow: hidden; width: auto;' data-rail-visible='1' data-rail-color='eeeeee' data-handle-color='#a1b2bd'>Completed";
    if (countCompleted == totalAppointment)
    {
        if (countCompleted == 0)
        {
            visitsDataHtml += "<div class='progress'>&nbsp;";
            visitsDataHtml += "<div class='progress-bar progress-bar-success' role='progressbar' aria-valuenow='40' aria-valuemin='0' aria-valuemax='100' style='color:black;width: " + percentofCompleted + "%'>" + countCompleted + "";
        } else
        {
            visitsDataHtml += "<div class='progress'>";
            visitsDataHtml += "<div class='progress-bar progress-bar-success' role='progressbar' aria-valuenow='40' aria-valuemin='0' aria-valuemax='100' style='width: " + percentofCompleted + "%'>" + countCompleted + "";
        }
    } else
    {
        visitsDataHtml += "<div class='progress'>&nbsp;" + countCompleted + "";
        visitsDataHtml += "<div class='progress-bar progress-bar-success' role='progressbar' aria-valuenow='40' aria-valuemin='0' aria-valuemax='100' style='width: " + percentofCompleted + "%'>";
    }

    visitsDataHtml += "</div>";
    visitsDataHtml += "</div>In - Progress";
    if (countInprogress == totalAppointment)
    {
        if (countInprogress == 0)
        {
            visitsDataHtml += "<div class='progress'>&nbsp;";
            visitsDataHtml += "<div class='progress-bar progress-bar-success' role='progressbar' aria-valuenow='40' aria-valuemin='0' aria-valuemax='100' style='color:black;width: " + percentofInprogress + "%'>" + countInprogress + "";
        } else
        {
            visitsDataHtml += "<div class='progress'>";
            visitsDataHtml += "<div class='progress-bar progress-bar-success' role='progressbar' aria-valuenow='40' aria-valuemin='0' aria-valuemax='100' style='width: " + percentofInprogress + "%'>" + countInprogress + "";
        }
    } else
    {
        visitsDataHtml += "<div class='progress'>&nbsp;" + countInprogress + "";
        visitsDataHtml += "<div class='progress-bar progress-bar-success' role='progressbar' aria-valuenow='40' aria-valuemin='0' aria-valuemax='100' style='width: " + percentofInprogress + "%'>";
    }
    visitsDataHtml += "</div>";
    visitsDataHtml += "</div>Pending";

    if (countPending == totalAppointment)
    {
        if (countPending == 0)
        {
            visitsDataHtml += "<div class='progress'>&nbsp;";
            visitsDataHtml += "<div class='progress-bar progress-bar-success' role='progressbar' aria-valuenow='40' aria-valuemin='0' aria-valuemax='100' style='color:black;width: " + percentofPending + "%'>" + countPending + "";
        } else {
            visitsDataHtml += "<div class='progress'>";
            visitsDataHtml += "<div class='progress-bar progress-bar-success' role='progressbar' aria-valuenow='40' aria-valuemin='0' aria-valuemax='100' style='width: " + percentofPending + "%'>" + countPending + "";
        }
    } else
    {
        visitsDataHtml += "<div class='progress'>&nbsp;" + countPending + "";
        visitsDataHtml += "<div class='progress-bar progress-bar-success' role='progressbar' aria-valuenow='40' aria-valuemin='0' aria-valuemax='100' style='width: " + percentofPending + "%'>";
    }
    visitsDataHtml += "</div>";
    visitsDataHtml += "</div>Missed";

    if (countMissed == totalAppointment)
    {
        if (countMissed == 0)
        {
            visitsDataHtml += "<div class='progress'>&nbsp;";
            visitsDataHtml += "<div class='progress-bar progress-bar-danger' role='progressbar' aria-valuenow='40' aria-valuemin='0' aria-valuemax='100' style='color:black;width: " + percentofMissed + "%'>" + countMissed + "";
        } else {
            visitsDataHtml += "<div class='progress'>";
            visitsDataHtml += "<div class='progress-bar progress-bar-danger' role='progressbar' aria-valuenow='40' aria-valuemin='0' aria-valuemax='100' style='width: " + percentofMissed + "%'>" + countMissed + "";
        }
    } else
    {
        visitsDataHtml += "<div class='progress'>&nbsp;" + countMissed + "";
        visitsDataHtml += "<div class='progress-bar progress-bar-danger' role='progressbar' aria-valuenow='40' aria-valuemin='0' aria-valuemax='100' style='width: " + percentofMissed + "%'>";
    }

    visitsDataHtml += "</div>";
    visitsDataHtml += "</div>";
    visitsDataHtml += "</div><div class='slimScrollBar' style='background: rgb(161, 178, 189) none repeat scroll 0% 0%; width: 7px; position: absolute; top: 0px; opacity: 0.4; display: none; border-radius: 4px; z-index: 99; right: 1px; height: 200px;'></div><div class='slimScrollRail' style='width: 7px; height: 100%; position: absolute; top: 0px; display: none; border-radius: 4px; opacity: 0.2; z-index: 90; right: 1px;'></div></div>";
    visitsDataHtml += "</div>";
    visitsDataHtml += "</div>";
    return visitsDataHtml;
}
function leaderBoard(leaderboardData)
{

    //var dict = {}, dict1 = {};
    var dict = [];
    time = new Date();
    var leaderBoard = "";
    if (leaderboardData.length == 0)
    {
        leaderBoard += "<tr>";
        leaderBoard += "<td style='text-align: center;color: grey;'> No Visit has been completed yet</td>";
        leaderBoard += "</tr>";
    } else {
        for (var i = 0; i < leaderboardData.length; i++) {
//            debugger;
            var visitCount = leaderboardData[i].visitCount;
            var fullname = leaderboardData[i].user.fullname;
            var userId = leaderboardData[i].user.id;
            dict.push({"visitCount": visitCount, "fullname": fullname, "userId": userId});
            //dict.sort();
            // dict[i] = fullname;
            // dict1[i] = userId;
        }
        dict.sort(function (a, b) {
            var a1 = a.visitCount, b1 = b.visitCount;
            if (a1 == b1)
                return 0;
            return a1 > b1 ? 1 : -1;
        });
        // var keys = Object.keys(dict); // or loop over the object to get the array
        //  var keys1 = Object.keys(dict1); // or loop over the object to get the array
        // keys will be in any order
        dict.reverse(); // maybe use custom sort, to change direction use .reverse()


        for (var i = 0; i < dict.length; i++) { // now lets iterate in sort order
            var visitsCount = dict[i].visitCount;
            var name = dict[i].fullname;
            var userId = dict[i].userId;
            leaderBoard += "<div class='pic_block'>";
            leaderBoard += "<div class='avatar'>";
            leaderBoard += '<img alt="" src="' + imageURLPath + accountId + '_' + userId + '_29X29.png?' + time + '" onerror="this.src=\'assets/img/usrsml_29.png\';"/>';
//        leaderBoard += "<img alt='' src='assets_dashboard/img/avatar1_small.jpg'>";
            leaderBoard += "</div>";
            leaderBoard += "<div class='info'>";
            leaderBoard += "<div class='caption'><span class='caption'  title='"+name+"'>" + name + "</span></div>";
            leaderBoard += "<div class='location'>Visits : <span style='font-size:18px;font-weight: 600;'>" + visitsCount + "</span></div>";
            leaderBoard += "</div>";
            leaderBoard += "</div>";
        }
    }
    return leaderBoard;
}

function myTeam(attendanceData, val)
{
    var teamVal = val == 0 ? "Punched-In" : val;
    var myTeam = "";
    myTeam += "<table class='table' id='sample_2'>";
    myTeam += "<tbody>";
    var punchedInCount = 0;

    if (attendanceData.length == 0)
    {
        myTeam += "<tr>";
        myTeam += "<td style='text-align: center;color: grey;'> No Data available</td>";
        myTeam += "</tr>";
    } else {

        var totalUserCount = 0;
        var inOffice = 0;
        var onField = 0;
        var punchOut = 0;

        var total = 0;
        var All = 0;
        var punchedIN = 0;
        var filterMyTeam = [];
        for (var i = 0; i < attendanceData.length; i++) {
            totalUserCount++;

            var id = attendanceData[i].user.id;
            var punchInLocation = attendanceData[i].punchInLocation;
            var punchInTime = attendanceData[i].punchInTime;
            var punchOutTime = attendanceData[i].punchOutTime;
            var userkey = attendanceData[i].userkey;
            var keyValue = attendanceData[i].keyValue;

            var fullname = attendanceData[i].user.fullname;
            var latitude = attendanceData[i].user.latitude;
            var langitude = attendanceData[i].user.langitude;
            var officeLatitude = attendanceData[i].user.officeLatitude;
            var officeLangitude = attendanceData[i].user.officeLangitude;
            var last_known_location = attendanceData[i].user.lastKnownLocation;
            var last_known_location_time = attendanceData[i].user.lastKnownLocationTime;

            var truncLatitude = latitude.toString().substr(0, 5);
            var truncLangitude = langitude.toString().substr(0, 5);
            var truncOfficeLatitude = officeLatitude.toString().substr(0, 5);
            var truncOfficeLangitude = officeLangitude.toString().substr(0, 5);

            last_known_location_time = convertServerDateToLocalDate((last_known_location_time.year) + 1900, last_known_location_time.month, last_known_location_time.date, last_known_location_time.hours, last_known_location_time.minutes);
            last_known_location_time = last_known_location_time.toLocaleString().split(" ");
            var last_known_location_time_date = last_known_location_time[0].toString().split("/");
            var last_known_location_time_hours = last_known_location_time[1];
            last_known_location_time_hours = last_known_location_time_hours.toString().split(":");
            var onlyDate = last_known_location_time_date[1];
            last_known_location_time_date = last_known_location_time_date[1] + '-' + last_known_location_time_date[0] + '-' + last_known_location_time_date[2];
            var hours = last_known_location_time_hours[0];
            var min = last_known_location_time_hours[1];
            var AMPM = last_known_location_time[2];
            
            hours = hours < 10 ? '0' + hours.toString() : hours;
//            min = min < 10 ? '0' + min : min;
            last_known_location_time_hours = hours + ":" + min + " " + AMPM;

            var time2 = "05:30:00";

            var hour = 0;
            var minute = 0;
            var second = 0;
            var splitTime1 = punchInTime.split(':');
            var splitTime2 = time2.split(':');


            hour = parseInt(splitTime1[0]) + parseInt(splitTime2[0]);
            minute = parseInt(splitTime1[1]) + parseInt(splitTime2[1]);
            hour = hour + parseInt(minute / 60);
            minute = minute % 60;
            minute = minute < 10 ? '0' + minute : minute;
            var AMPM = hour >= 12 ? ' PM' : ' AM';
            hour = hour > 12 ? hour - 12 : hour;
            hour = hour < 10 ? '0' + hour.toString() : hour;
             
            var splitpunchOutTime = punchOutTime.split(':');
            var splitTime3 = time2.split(':');


            var hour_1 = parseInt(splitpunchOutTime[0]) + parseInt(splitTime3[0]);
            var minute_1 = parseInt(splitpunchOutTime[1]) + parseInt(splitTime3[1]);
            hour_1 = hour_1 + parseInt(minute_1 / 60);
            minute_1 = minute_1 % 60;
            minute_1 = minute_1 < 10 ? '0' + minute_1 : minute_1;
            var AMPM_1 = hour_1 >= 12 ? ' PM' : ' AM';
            hour_1 = hour_1 > 12 ? hour_1 - 12 : hour_1;
            hour_1 = hour_1 < 10 ? '0' + hour_1.toString() : hour_1;

//            if ((punchInTime != "00:00:00") && (punchOutTime != "00:00:00")) {
//                if ((truncLatitude == truncOfficeLatitude) && (truncLangitude == truncOfficeLangitude)) {
//                    inOffice++;
//                } else if((punchInTime != "00:00:00") && (punchOutTime == "00:00:00")){
//                    onField++;
//                }
//                else{
//                     punchOut++;
//                }
//            }


            if ((punchInTime != "00:00:00") && (punchOutTime == "00:00:00"))
            {
                if ((truncLatitude == truncOfficeLatitude) && (truncLangitude == truncOfficeLangitude)) {
                    if (keyValue == 1)
                    {
                        if (new Date().getDate() == parseInt(onlyDate, 10))
                        {
                            last_known_location_time_date = "";
                            filterMyTeam.push({"val": 2, "status": "punchIn", "status1": "inOffice", "keyValue": 1, "onlyDate": onlyDate, "fullname": fullname, "hour": hour, "minute": minute, "AMPM": AMPM, "last_known_location": last_known_location, "last_known_location_time_hours": last_known_location_time_hours, "last_known_location_time_date": last_known_location_time_date});
                        } else
                        {
                            filterMyTeam.push({"val": 2, "status": "punchIn", "status1": "inOffice", "keyValue": 1, "onlyDate": "", "fullname": fullname, "hour": hour, "minute": minute, "AMPM": AMPM, "last_known_location": last_known_location, "last_known_location_time_hours": last_known_location_time_hours, "last_known_location_time_date": last_known_location_time_date});
                        }
                    } else
                    {
                        if (new Date().getDate() == parseInt(onlyDate, 10))
                        {
                            last_known_location_time_date = "";
                            filterMyTeam.push({"val": 2, "status": "punchIn", "status1": "inOffice", "keyValue": 0, "onlyDate": onlyDate, "fullname": fullname, "hour": hour, "minute": minute, "AMPM": AMPM, "last_known_location": last_known_location, "last_known_location_time_hours": last_known_location_time_hours, "last_known_location_time_date": last_known_location_time_date});
                        } else
                        {
                            filterMyTeam.push({"val": 2, "status": "punchIn", "status1": "inOffice", "keyValue": 0, "onlyDate": "", "fullname": fullname, "hour": hour, "minute": minute, "AMPM": AMPM, "last_known_location": last_known_location, "last_known_location_time_hours": last_known_location_time_hours, "last_known_location_time_date": last_known_location_time_date});
                        }
                    }

                    inOffice++;
                } else if ((punchInTime != "00:00:00") && (punchOutTime == "00:00:00")) {
                    if (keyValue == 1)
                    {
                        if (new Date().getDate() == parseInt(onlyDate, 10))
                        {
                            last_known_location_time_date = "";
                            filterMyTeam.push({"val": 1, "status": "punchIn", "status1": "onField", "keyValue": 1, "onlyDate": onlyDate, "fullname": fullname, "hour": hour, "minute": minute, "AMPM": AMPM, "last_known_location": last_known_location, "last_known_location_time_hours": last_known_location_time_hours, "last_known_location_time_date": last_known_location_time_date});

                        } else
                        {
                            filterMyTeam.push({"val": 1, "status": "punchIn", "status1": "onField", "keyValue": 1, "onlyDate": "", "fullname": fullname, "hour": hour, "minute": minute, "AMPM": AMPM, "last_known_location": last_known_location, "last_known_location_time_hours": last_known_location_time_hours, "last_known_location_time_date": last_known_location_time_date});
                        }
                    } else {
                        if (new Date().getDate() == parseInt(onlyDate, 10))
                        {
                            last_known_location_time_date = "";
                            filterMyTeam.push({"val": 1, "status": "punchIn", "status1": "onField", "keyValue": 0, "onlyDate": onlyDate, "fullname": fullname, "hour": hour, "minute": minute, "AMPM": AMPM, "last_known_location": last_known_location, "last_known_location_time_hours": last_known_location_time_hours, "last_known_location_time_date": last_known_location_time_date});
                        } else {

                            filterMyTeam.push({"val": 1, "status": "punchIn", "status1": "onField", "keyValue": 0, "onlyDate": "", "fullname": fullname, "hour": hour, "minute": minute, "AMPM": AMPM, "last_known_location": last_known_location, "last_known_location_time_hours": last_known_location_time_hours, "last_known_location_time_date": last_known_location_time_date});
                        }
                    }

                    onField++;
                }
            } else if ((punchInTime != "00:00:00") && (punchOutTime != "00:00:00"))
            {
                if ((truncLatitude == truncOfficeLatitude) && (truncLangitude == truncOfficeLangitude)) {

                    last_known_location_time_date = "";
                    last_known_location = "";
                    filterMyTeam.push({"val": 3, "status": "punchOut", "status1": "inOffice", "fullname": fullname, "hour_1": hour_1, "minute_1": minute_1, "AMPM_1": AMPM_1, "last_known_location": last_known_location, "last_known_location_time_hours": last_known_location_time_hours, "last_known_location_time_date": last_known_location_time_date});

                } else
                {

                    last_known_location_time_date = "";
                    filterMyTeam.push({"val": 3, "status": "punchOut", "status1": "", "fullname": fullname, "hour_1": hour_1, "minute_1": minute_1, "AMPM_1": AMPM_1, "last_known_location": last_known_location, "last_known_location_time_hours": last_known_location_time_hours, "last_known_location_time_date": last_known_location_time_date});
                }
                punchOut++;
            } else if ((punchInTime == "00:00:00") && (punchOutTime == "00:00:00"))
            {

                filterMyTeam.push({"val": 4, "status": "not-punchedIn", "fullname": fullname, "hour": "", "minute": "", "AMPM": "", "last_known_location": "", "last_known_location_time_hours": "", "last_known_location_time_date": ""});
            }

        }

        filterMyTeam.sort(function (a, b) {
            return parseInt(a.val) - parseInt(b.val);
        });

        total = onField + inOffice;

        var flagInOffice = 1;
        var flagOnField = 1;
        var flagPunchOut = 1;
        var flagPunchIn = 1;
        var flagAll = 1;

//        console.log("attendanceData.length=" + attendanceData.length);
        for (var i = 0; i < attendanceData.length; i++) {
            punchedInCount++;

            var id = attendanceData[i].user.id;
            var punchInLocation = attendanceData[i].punchInLocation;
            var punchInTime = attendanceData[i].punchInTime;
            var punchOutTime = attendanceData[i].punchOutTime;
            var userkey = attendanceData[i].userkey;
            var keyValue = attendanceData[i].keyValue;

            var fullname = attendanceData[i].user.fullname;
            var latitude = attendanceData[i].user.latitude;
            var langitude = attendanceData[i].user.langitude;
            var officeLatitude = attendanceData[i].user.officeLatitude;
            var officeLangitude = attendanceData[i].user.officeLangitude;
            var last_known_location = attendanceData[i].user.lastKnownLocation;
            var last_known_location_time = attendanceData[i].user.lastKnownLocationTime;

            var truncLatitude = latitude.toString().substr(0, 5);
            var truncLangitude = langitude.toString().substr(0, 5);
            var truncOfficeLatitude = officeLatitude.toString().substr(0, 5);
            var truncOfficeLangitude = officeLangitude.toString().substr(0, 5);

            last_known_location_time = convertServerDateToLocalDate((last_known_location_time.year) + 1900, last_known_location_time.month, last_known_location_time.date, last_known_location_time.hours, last_known_location_time.minutes);
            last_known_location_time = last_known_location_time.toLocaleString().split(" ");
            var last_known_location_time_date = last_known_location_time[0].toString().split("/");
            var last_known_location_time_hours = last_known_location_time[1];
            last_known_location_time_hours = last_known_location_time_hours.toString().split(":");
            var onlyDate = last_known_location_time_date[1];
            last_known_location_time_date = last_known_location_time_date[1] + '-' + last_known_location_time_date[0] + '-' + last_known_location_time_date[2];
            var hours = last_known_location_time_hours[0];
            hours = hours < 10 ? '0' + hours : hours;
            var min = last_known_location_time_hours[1];
           
            var AMPM = last_known_location_time[2];
            last_known_location_time_hours = hours + ":" + min + " " + AMPM;

            var time2 = "05:30:00";

            var hour = 0;
            var minute = 0;
            var second = 0;
            var splitTime1 = punchInTime.split(':');
            var splitTime2 = time2.split(':');


            hour = parseInt(splitTime1[0]) + parseInt(splitTime2[0]);
            minute = parseInt(splitTime1[1]) + parseInt(splitTime2[1]);
            hour = hour + parseInt(minute / 60);
            minute = minute % 60;
            minute = minute < 10 ? '0' + minute : minute;
            var AMPM = hour >= 12 ? ' PM' : ' AM';
            hour = hour > 12 ? hour - 12 : hour;
            hour = hour < 10 ? '0' + hour.toString() : hour;
            
            var splitpunchOutTime = punchOutTime.split(':');
            var splitTime3 = time2.split(':');


            var hour_1 = parseInt(splitpunchOutTime[0]) + parseInt(splitTime3[0]);
            var minute_1 = parseInt(splitpunchOutTime[1]) + parseInt(splitTime3[1]);
            hour_1 = hour_1 + parseInt(minute_1 / 60);
            minute_1 = minute_1 % 60;
            minute_1 = minute_1 < 10 ? '0' + minute_1 : minute_1;
            var AMPM_1 = hour_1 >= 12 ? ' PM' : ' AM';
            hour_1 = hour_1 > 12 ? hour_1 - 12 : hour_1;
            hour_1 = hour_1 < 10 ? '0' + hour_1.toString() : hour_1;

            if (last_known_location.length == 0)
            {
                last_known_location = ' Address not available';
            }
//            debugger;
            if (teamVal == "Not Punched-In")
            {
                if ((punchInTime == "00:00:00") && (punchOutTime == "00:00:00"))
                {

                    myTeam += "<tr>";
                    myTeam += "<td>";
                    myTeam += "<a nohref><span class='teamname'><strong>" + fullname + "</strong></span> | " + "Not Punched-In" + " <br>";
                    myTeam += "<span class='address'><i class='fa fa-map-marker'></i> " + "Not Available" + " </span></a>";
                    myTeam += "</td>";
                    myTeam += "<td style='width:75px'><span class='time'>" + "N/A" + "</span></td>";
                    myTeam += "</tr>";
                }
//                else
//                {
//                    myTeam += "<tr>";
//                    myTeam += "<td style='text-align: center;color: grey;'> No Data available</td>";
//                    myTeam += "</tr>";
//                }
            } else if (teamVal === "All")
            {

                for (var k = 0; k < filterMyTeam.length; k++)
                {

                    if (filterMyTeam[k].status == "punchIn") {


                        if (filterMyTeam[k].status1 == "onField") {
                            //                                                        On-Field
                            if (filterMyTeam[k].last_known_location.length == 0)
                            {
                                filterMyTeam[k].last_known_location = "Address not available";
                            }
                            if (filterMyTeam[k].keyValue == 1)
                            {
                                
                                    myTeam += "<tr>";
                                    myTeam += "<td>";
                                    myTeam += "<a nohref><span class='teamname'><strong>" + filterMyTeam[k].fullname + "</strong></span> | Punched-in: " + filterMyTeam[k].hour + ':' + filterMyTeam[k].minute + filterMyTeam[k].AMPM + " <br>";
                                    myTeam += "<span class='address'  title='"+filterMyTeam[k].last_known_location+"'> In-Meeting&nbsp;&nbsp;&nbsp;<i class='fa fa-map-marker'> </i>  " + filterMyTeam[k].last_known_location + "  </span></a>";
                                    myTeam += "</td>";
                                    myTeam += "<td style='width:75px'><span class='time'>" + filterMyTeam[k].last_known_location_time_hours + "</span></td>";
                                    myTeam += "</tr>";
                                
                            } else
                            {
                               
                                    myTeam += "<tr>";
                                    myTeam += "<td>";
                                    myTeam += "<a nohref><span class='teamname'><strong>" + filterMyTeam[k].fullname + "</strong></span> | Punched-in: " + filterMyTeam[k].hour + ':' + filterMyTeam[k].minute + filterMyTeam[k].AMPM + " <br>";
                                    myTeam += "<span class='address'  title='"+filterMyTeam[k].last_known_location+"'><i class='fa fa-map-marker'></i> " + filterMyTeam[k].last_known_location + "  </span></a>";
                                    myTeam += "</td>";
                                    myTeam += "<td style='width:75px'><span class='time'>" + filterMyTeam[k].last_known_location_time_hours + "</span></td>";
                                    myTeam += "</tr>";
                               
                                  
                            }

                        } else
                        {       // In-Office
                            if (filterMyTeam[k].keyValue == 1)
                            {
                               
                                    myTeam += "<tr>";
                                    myTeam += "<td>";
                                    myTeam += "<a nohref><span class='teamname'><strong>" + filterMyTeam[k].fullname + "</strong></span> | Punched-in: " + filterMyTeam[k].hour + ':' + filterMyTeam[k].minute + filterMyTeam[k].AMPM + " <br>";
                                    myTeam += "<span class='address'><i class='fa fa-map-marker'></i> In-Meeting | In-Office </span></a> ";
                                    myTeam += "</td>";
                                    myTeam += "<td style='width:75px'><span class='time'>" + filterMyTeam[k].last_known_location_time_hours + "</span></td>";
                                    myTeam += "</tr>";
                                
                                  
                            } else {
                               
                                    myTeam += "<tr>";
                                    myTeam += "<td>";
                                    myTeam += "<a nohref><span class='teamname'><strong>" + filterMyTeam[k].fullname + "</strong></span> | Punched-in: " + filterMyTeam[k].hour + ':' + filterMyTeam[k].minute + filterMyTeam[k].AMPM + " <br>";
                                    myTeam += "<span class='address'><i class='fa fa-map-marker'></i> In-Office  </span></a> ";
                                    myTeam += "</td>";
                                    myTeam += "<td style='width:75px'><span class='time'>" + filterMyTeam[k].last_known_location_time_hours + "</span></td>";
                                    myTeam += "</tr>";
                              
                            }
                        }
                    } else if (filterMyTeam[k].status == "punchOut") {
                        if (filterMyTeam[k].last_known_location.length == 0)
                        {
                            filterMyTeam[k].last_known_location = "Address not available";
                        }
                        if (filterMyTeam[k].status1 == "inOffice") {
                            myTeam += "<tr>";
                            myTeam += "<td>";
                            myTeam += "<a nohref><span class='teamname'><strong>" + filterMyTeam[k].fullname + "</strong></span> | Punched-out: " + filterMyTeam[k].hour_1 + ':' + filterMyTeam[k].minute_1 + filterMyTeam[k].AMPM_1 + " <br>";
                            myTeam += "<span class='address'><i class='fa fa-map-marker'></i> In-Office </span></a>";
                            myTeam += "</td>";
                            myTeam += "<td style='width:75px'><span class='time'> " + filterMyTeam[k].last_known_location_time_hours + "</span></td>";
                            myTeam += "</tr>";
                        } else
                        {
                            myTeam += "<tr>";
                            myTeam += "<td>";
                            myTeam += "<a nohref><span class='teamname'><strong>" + filterMyTeam[k].fullname + "</strong></span> | Punched-out: " + filterMyTeam[k].hour_1 + ':' + filterMyTeam[k].minute_1 + filterMyTeam[k].AMPM_1 + " <br>";
                            myTeam += "<span class='address'  title='"+filterMyTeam[k].last_known_location+"'><i class='fa fa-map-marker'></i> " + filterMyTeam[k].last_known_location + "</span></a>";
                            myTeam += "</td>";
                            myTeam += "<td style='width:75px'><span class='time'> " + filterMyTeam[k].last_known_location_time_hours + "</span></td>";
                            myTeam += "</tr>";
                        }
                    } else if (filterMyTeam[k].status == "not-punchedIn")
                    {
                        myTeam += "<tr>";
                        myTeam += "<td>";
                        myTeam += "<a nohref><span class='teamname'><strong>" + filterMyTeam[k].fullname + "</strong></span> | " + "Not Punched-In" + " <br>";
                        myTeam += "<span class='address'><i class='fa fa-map-marker'></i> " + "Not Available" + " </span></a>";
                        myTeam += "</td>";
                        myTeam += "<td style='width:75px'><span class='time'>" + "N/A" + "</span></td>";
                        myTeam += "</tr>";
                    } else {
                        if (flagAll === 1 && All === 0)
                        {
                            flagAll++;
                            myTeam += "<tr>";
                            myTeam += "<td style='text-align: center;color: grey;'> No Data available</td>";
                            myTeam += "</tr>";
                        }
                    }
                }
                break;
            }
            if (teamVal == "Punched-In")    // PunchIn && In-Meeting
            {

                if ((punchOutTime == "00:00:00") && (punchInTime != "00:00:00")) {
                    if ((truncLatitude == truncOfficeLatitude) && (truncLangitude == truncOfficeLangitude)) {
                        //               
                        //inoffice
                        if (keyValue == 1)
                        {
                            
                                myTeam += "<tr>";
                                myTeam += "<td>";
                                myTeam += "<a nohref><span class='teamname'><strong>" + fullname + "</strong></span> | Punched-in: " + hour + ':' + minute + AMPM + " <br>";
                                myTeam += "<span class='address'><i class='fa fa-map-marker'></i> In-Meeting | In-Office  </span></a> ";
                                myTeam += "</td>";
                                myTeam += "<td style='width:75px'><span class='time'>" + last_known_location_time_hours + "</span></td>";
                                myTeam += "</tr>";
                           
                              
                        } else {
                           
                                myTeam += "<tr>";
                                myTeam += "<td>";
                                myTeam += "<a nohref><span class='teamname'><strong>" + fullname + "</strong></span> | Punched-in: " + hour + ':' + minute + AMPM + " <br>";
                                myTeam += "<span class='address'><i class='fa fa-map-marker'></i> In-Office  </span></a> ";
                                myTeam += "</td>";
                                myTeam += "<td style='width:75px'><span class='time'>" + last_known_location_time_hours + "</span></td>";
                                myTeam += "</tr>";
                           
                        }

                    } else
                    {
                        if (keyValue == 1)
                        {
                          
                                myTeam += "<tr>";
                                myTeam += "<td>";
                                myTeam += "<a nohref><span class='teamname'><strong>" + fullname + "</strong></span> | Punched-in: " + hour + ':' + minute + AMPM + " <br>";
                                myTeam += "<span class='address'  title='"+last_known_location+"'> In-Meeting&nbsp;&nbsp;&nbsp;<i class='fa fa-map-marker'>  </i>  " + last_known_location + "  </span></a>";
                                myTeam += "</td>";
                                myTeam += "<td style='width:75px'><span class='time'>" + last_known_location_time_hours + "</span></td>";
                                myTeam += "</tr>";
                           
                        } else
                        {
                           
                                myTeam += "<tr>";
                                myTeam += "<td>";
                                myTeam += "<a nohref><span class='teamname'><strong>" + fullname + "</strong></span> | Punched-in: " + hour + ':' + minute + AMPM + " <br>";
                                myTeam += "<span class='address'  title='"+last_known_location+"'><i class='fa fa-map-marker'></i> " + last_known_location + "  </span></a>";
                                myTeam += "</td>";
                                myTeam += "<td style='width:75px'><span class='time'>" + last_known_location_time_hours + "</span></td>";
                                myTeam += "</tr>";
               
                        }
                    }
                } else {
                    if (flagPunchIn === 1 && total === 0)
                    {
                        flagPunchIn++;
                        myTeam += "<tr>";
                        myTeam += "<td style='text-align: center;color: grey;'> No Data available</td>";
                        myTeam += "</tr>";
                    }
                }

            } else if (teamVal == "In-Office") {
                if ((punchOutTime == "00:00:00") && (punchInTime != "00:00:00")) {


                    if ((truncLatitude == truncOfficeLatitude) && (truncLangitude == truncOfficeLangitude)) {
                        //               
                        //                                                         inoffice
                        if (keyValue == 1)
                        {
                           
                                myTeam += "<tr>";
                                myTeam += "<td>";
                                myTeam += "<a nohref><span class='teamname'><strong>" + fullname + "</strong></span> | Punched-in: " + hour + ':' + minute + AMPM + " <br>";
                                myTeam += "<span class='address'><i class='fa fa-map-marker'></i> In-Meeting | In-Office  </span></a> ";
                                myTeam += "</td>";
                                myTeam += "<td style='width:75px'><span class='time'>" + last_known_location_time_hours + "</span></td>";
                                myTeam += "</tr>";
                            
                        } else {
                           
                                myTeam += "<tr>";
                                myTeam += "<td>";
                                myTeam += "<a nohref><span class='teamname'><strong>" + fullname + "</strong></span> | Punched-in: " + hour + ':' + minute + AMPM + " <br>";
                                myTeam += "<span class='address'><i class='fa fa-map-marker'></i> In-Office  </span></a> ";
                                myTeam += "</td>";
                                myTeam += "<td style='width:75px'><span class='time'>" + last_known_location_time_hours + "</span></td>";
                                myTeam += "</tr>";
                          
                        }
                    }

                } else {
                    if (flagInOffice === 1 && inOffice === 0)
                    {
                        flagInOffice++;
                        myTeam += "<tr>";
                        myTeam += "<td style='text-align: center;color: grey;'> No Data available</td>";
                        myTeam += "</tr>";
                    }
                }

            } else if (teamVal == "On-Field") {
                //                                  onField++;
                if ((punchOutTime == "00:00:00") && (punchInTime != "00:00:00") && (truncLatitude != truncOfficeLatitude) && (truncLangitude != truncOfficeLangitude))
                {
                    if (keyValue == 1) {


                       
                            myTeam += "<tr>";
                            myTeam += "<td>";
                            myTeam += "<a nohref><span class='teamname'><strong>" + fullname + "</strong></span> | Punched-in: " + hour + ':' + minute + AMPM + " <br>";
                            myTeam += "<span class='address'  title='"+last_known_location+"'> In-Meeting&nbsp;&nbsp;&nbsp;<i class='fa fa-map-marker'></i> " + last_known_location + "  </span></a>";
                            myTeam += "</td>";
                            myTeam += "<td style='width:75px'><span class='time'>" + last_known_location_time_hours + "</span></td>";
                            myTeam += "</tr>";
                       
                    } else
                    {
                      
                            myTeam += "<tr>";
                            myTeam += "<td>";
                            myTeam += "<a nohref><span class='teamname'><strong>" + fullname + "</strong></span> | Punched-in: " + hour + ':' + minute + AMPM + " <br>";
                            myTeam += "<span class='address'  title='"+last_known_location+"'><i class='fa fa-map-marker'></i>" + last_known_location + "  </span></a>";
                            myTeam += "</td>";
                            myTeam += "<td style='width:75px'><span class='time'>" + last_known_location_time_hours + "</span></td>";
                            myTeam += "</tr>";
                      
                    }
                } else {
                    if (flagOnField === 1 && onField === 0)
                    {
                        flagOnField++;
                        myTeam += "<tr>";
                        myTeam += "<td style='text-align: center;color: grey;'> No Data available</td>";
                        myTeam += "</tr>";
                    }
                }

            } else if ((teamVal == "Punched-Out")) // Punchout
            {
                if ((punchOutTime != "00:00:00") && (punchInTime != "00:00:00")) {

                    if ((truncLatitude == truncOfficeLatitude) && (truncLangitude == truncOfficeLangitude)) {

                        myTeam += "<tr>";
                        myTeam += "<td>";
                        myTeam += "<a nohref><span class='teamname'><strong>" + fullname + "</strong></span> | Punched-out: " + hour_1 + ':' + minute_1 + AMPM_1 + " <br>";
                        myTeam += "<span class='address'><i class='fa fa-map-marker'></i> In-Office </span></a>";
                        myTeam += "</td>";
                        myTeam += "<td style='width:75px'><span class='time'>" + last_known_location_time_hours + "</span></td>";
                        myTeam += "</tr>";
                    } else
                    {
                        myTeam += "<tr>";
                        myTeam += "<td>";
                        myTeam += "<a nohref><span class='teamname'><strong>" + fullname + "</strong></span> | Punched-out: " + hour_1 + ':' + minute_1 + AMPM_1 + " <br>";
                        myTeam += "<span class='address'  title='"+last_known_location+"'><i class='fa fa-map-marker'></i> " + last_known_location + "</span></a>";
                        myTeam += "</td>";
                        myTeam += "<td style='width:75px'><span class='time'>" + last_known_location_time_hours + "</span></td>";
                        myTeam += "</tr>";
                    }
                } else {
                    if (flagPunchOut === 1 && punchOut === 0)
                    {
                        flagPunchOut++;
                        myTeam += "<tr>";
                        myTeam += "<td style='text-align: center;color: grey;'> No Data available</td>";
                        myTeam += "</tr>";
                    }
                }
            }
        }
    }
    myTeam += "</tbody>";
    myTeam += "</table>";
    return myTeam;
}
function currentVisits(appointmentData, val)
{
    var outer;
    var currentVisits = "";
    var currentVisitVal = val == 0 ? "All" : val;
    currentVisits += "<table class='table' id='sample_2'>";
    currentVisits += "<tbody>";
    if (appointmentData.length == 0)
    {
        currentVisits += "<tr>";
        currentVisits += "<td style='text-align: center;color: grey;'> No Data available</td>";
        currentVisits += "</tr>";
    } else {

        var countCompleted = 0;
        var countInprogress = 0;
        var countPending = 0;
        var countMissed = 0;
        var flagComp = 1;
        var flagInProg = 1;
        var flagPend = 1;
        var flagMiss = 1;

        var filterVisits = [];
        var filterInprogress=[];
//
        for (var j = 0; j < appointmentData.length; j++)
        {
            var status = appointmentData[j].appointment.status;
            var appointmentEndTime = appointmentData[j].appointment.sendTime;
            var recordState = appointmentData[j].appointment.recordState;
            appointmentEndTime = appointmentEndTime.toLocaleString().split(" ");
            var discardappointmentEndTimeAMPM = appointmentEndTime[1].split(":");

            var ehours = parseInt(discardappointmentEndTimeAMPM[0]);
            ehours = ehours * 60 + parseInt(discardappointmentEndTimeAMPM[1]) + 330;

            var curHour = new Date().getHours();
            curHour = curHour * 60 + new Date().getMinutes();
            if (status == 2 && recordState != 3)
            {
                countCompleted++;
            } else if (status == 1 && recordState != 3)
            {
                countInprogress++;
            } else if ((parseInt(ehours) < curHour && status == 0 && recordState != 3))
            {
                countMissed++;
            } else if ((parseInt(ehours) >= curHour && status == 0 && recordState != 3))
            {
                countPending++;
            }
            var tclass = status == 2 ? "completed" : tclass;
            var tclass = status == 1 ? "inprogess" : tclass;
            var tclass = status == 0 ? "pending" : tclass;
            var appointmentTime = appointmentData[j].time;

            var activityDateTime = appointmentTime.split(' ');
            var time = activityDateTime[1];
            var timeSplit = time.split(':');
            var timeSplit1 = '05:30'.split(':');
            
            var hours = parseInt(timeSplit[0]) + parseInt(timeSplit1[0]);
            var minutes = parseInt(timeSplit[1]) + parseInt(timeSplit1[1]);
            hours = hours + parseInt(minutes / 60);
            minutes = minutes % 60;
            minutes = minutes < 10 ? '0' + minutes : minutes;

            var timeToShow;
            var AMPM = hours >= 12 ? ' PM' : ' AM'
            var t1=hours*60+parseInt(minutes);
            hours = hours > 12 ? (hours - 12) : hours;
            hours = hours < 10 ? '0' + hours.toString() : hours;
            timeToShow = hours + ':' + minutes + AMPM;
            var customerAddress1 = appointmentData[j].customer.customerAddress1;
            if (customerAddress1.length == 0)
            {
                customerAddress1 = " Address not available ";
            }
            if (status == 2)
            {
                filterVisits.push({"val": 1, "cls": tclass, "custName": appointmentData[j].customer.customerName, "subName": appointmentData[j].subordinateName, "addr": customerAddress1, "time": timeToShow,"time1":t1});
            }
            if (status == 1)
            {
                filterVisits.push({"val": 2, "cls": tclass, "custName": appointmentData[j].customer.customerName, "subName": appointmentData[j].subordinateName, "addr": customerAddress1, "time": timeToShow,"time1":t1});
            }
            if (status == 0)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
            {
                filterVisits.push({"val": 3, "cls": tclass, "custName": appointmentData[j].customer.customerName, "subName": appointmentData[j].subordinateName, "addr": customerAddress1, "time": timeToShow,"time1":t1});
            }
        }

        filterVisits.sort(function (a, b) {
            return parseInt(a.val) - parseInt(b.val);
        });
        var removeIndex=0;
         for (var k = 0; k < filterVisits.length; k++)
        {   
            
            if(filterVisits[k].cls == "inprogess")
            {
                var time=filterVisits[k].time;
                var tclass=filterVisits[k].cls;
                var custName=filterVisits[k].custName;
                var subName=filterVisits[k].subName;
                var addr=filterVisits[k].addr;
                var t1=filterVisits[k].time1;
 
                filterInprogress.push({"val": 2, "cls": tclass, "custName":custName, "subName": subName, "addr": addr, "time": time,"time1":t1});
                removeIndex=filterVisits.map(function(item) { return item.cls; }).indexOf("inprogess");
                filterVisits.splice(removeIndex, 1);
                k--;
            }
            
        }
         filterInprogress.sort(function (a, b) {
            return parseInt(b.time1) - parseInt(a.time1);
        });
        Array.prototype.push.apply(filterVisits,filterInprogress); 
        filterVisits.sort(function (a, b) {
            return parseInt(a.val) - parseInt(b.val);
        });

//        console.log("countCompleted=" + countCompleted);
//        console.log("countInprogress=" + countInprogress);
//        console.log("countPending=" + countPending);
//        console.log("countMissed=" + countMissed);

        for (var i = 0; i < appointmentData.length; i++)
        {
            var subordinateName = appointmentData[i].subordinateName;
            var customerName = appointmentData[i].customer.customerName;
            var customerAddress1 = appointmentData[i].customer.customerAddress1;
            var status = appointmentData[i].appointment.status;
            var appointmentTime = appointmentData[i].time;
            var appointmentEndTime = appointmentData[i].appointment.sendTime;
            var recordState = appointmentData[i].appointment.recordState;

            appointmentEndTime = appointmentEndTime.toLocaleString().split(" ");
            var discardappointmentEndTimeAMPM = appointmentEndTime[1].split(":");
            
            var ehours = parseInt(discardappointmentEndTimeAMPM[0]);
            ehours = ehours * 60 + parseInt(discardappointmentEndTimeAMPM[1]) + 330;

            var timeSplit1 = '05:30'.split(':');
            
            appointmentTime = appointmentTime.toLocaleString().split(" ");
            var discardappointmentTimeAMPM = appointmentTime[1].split(":");
            
            var hours = parseInt(discardappointmentTimeAMPM[0]) + parseInt(timeSplit1[0]);
            var minutes = parseInt(discardappointmentTimeAMPM[1]) + parseInt(timeSplit1[1]);
            
            hours = hours + parseInt(minutes / 60);
            minutes = minutes % 60;
            minutes = minutes < 10 ? '0' + minutes : minutes;
            var AMPM = hours >= 12 ? ' PM' : ' AM'

            var timeToShow;
   
            if (customerAddress1.length == 0)
            {
                customerAddress1 = " Address not available ";
            }
            if (currentVisitVal == "All" && recordState != 3)
            {
                   var find="test"; 
                   var cnt=0;
                for (var k = 0; k < filterVisits.length; k++)
                {
                    var c = "clshighLight";
                   
                    if (filterVisits[k].cls == "inprogess")
                    {
                        if(cnt==0)
                        {
                            cnt++;
                          
                            currentVisits += "<tr class='" + filterVisits[k].cls +"'>";
                            currentVisits += "<td>";
                            currentVisits += "<a nohref><span class='companyname'><strong>" + filterVisits[k].custName + "</strong></span> | " + filterVisits[k].subName + "<br>";
                            currentVisits += "<span class='address'  title='" + filterVisits[k].addr + "'><i class='fa fa-map-marker'></i> " + filterVisits[k].addr + "</span></a>";
                            currentVisits += "</td>";
                            currentVisits += "<td style='width:75px' class='"+ find +"'><span class='time'> " + filterVisits[k].time + "</span></td>"

                            currentVisits += "</tr>";
                        }else {
                            currentVisits += "<tr class='" + filterVisits[k].cls + "'>";
                            currentVisits += "<td>";
                            currentVisits += "<a nohref><span class='companyname'><strong>" + filterVisits[k].custName + "</strong></span> | " + filterVisits[k].subName + "<br>";
                            currentVisits += "<span class='address'  title='" + filterVisits[k].addr + "'><i class='fa fa-map-marker'></i> " + filterVisits[k].addr + "</span></a>";
                            currentVisits += "</td>";
                            currentVisits += "<td style='width:75px'><span class='time'> " + filterVisits[k].time + "</span></td>"

                            currentVisits += "</tr>";
                        }
                    } else if(filterVisits[k].cls == "pending") {
                        var t=0;
                        var t1='pd';
                        if(t==0)
                        {
                            t++;
                          
                            currentVisits += "<tr class='" + filterVisits[k].cls +"'>";
                            currentVisits += "<td>";
                            currentVisits += "<a nohref><span class='companyname'><strong>" + filterVisits[k].custName + "</strong></span> | " + filterVisits[k].subName + "<br>";
                            currentVisits += "<span class='address'  title='" + filterVisits[k].addr + "'><i class='fa fa-map-marker'></i> " + filterVisits[k].addr + "</span></a>";
                            currentVisits += "</td>";
                            currentVisits += "<td style='width:75px'  class='"+ t1 +"'><span class='time'> " + filterVisits[k].time + "</span></td>"

                            currentVisits += "</tr>";
                        }else {
                            currentVisits += "<tr class='" + filterVisits[k].cls + "'>";
                            currentVisits += "<td>";
                            currentVisits += "<a nohref><span class='companyname'><strong>" + filterVisits[k].custName + "</strong></span> | " + filterVisits[k].subName + "<br>";
                            currentVisits += "<span class='address'  title='" + filterVisits[k].addr + "'><i class='fa fa-map-marker'></i> " + filterVisits[k].addr + "</span></a>";
                            currentVisits += "</td>";
                            currentVisits += "<td style='width:75px'><span class='time'> " + filterVisits[k].time + "</span></td>"

                            currentVisits += "</tr>";
                        }

                    }
                    else {
                        var t=0;
                        var t1='cmp';
                        if(t==0)
                        {
                            t++;
                          
                            currentVisits += "<tr class='" + filterVisits[k].cls +"'>";
                            currentVisits += "<td>";
                            currentVisits += "<a nohref><span class='companyname'><strong>" + filterVisits[k].custName + "</strong></span> | " + filterVisits[k].subName + "<br>";
                            currentVisits += "<span class='address'  title='" + filterVisits[k].addr + "'><i class='fa fa-map-marker'></i> " + filterVisits[k].addr + "</span></a>";
                            currentVisits += "</td>";
                            currentVisits += "<td style='width:75px' class='"+ t1 +"'><span class='time'> " + filterVisits[k].time + "</span></td>"

                            currentVisits += "</tr>";
                        }else {
                            currentVisits += "<tr class='" + filterVisits[k].cls + "'>";
                            currentVisits += "<td>";
                            currentVisits += "<a nohref><span class='companyname'><strong>" + filterVisits[k].custName + "</strong></span> | " + filterVisits[k].subName + "<br>";
                            currentVisits += "<span class='address'  title='" + filterVisits[k].addr + "'><i class='fa fa-map-marker'></i> " + filterVisits[k].addr + "</span></a>";
                            currentVisits += "</td>";
                            currentVisits += "<td style='width:75px'><span class='time'> " + filterVisits[k].time + "</span></td>"

                            currentVisits += "</tr>";
                        }

                    }
                }
                break;

            } else if ((currentVisitVal == "Completed"))
            {
                hours = hours > 12 ? (hours - 12) : hours;
                hours = hours < 10 ? '0' + hours.toString() : hours;
                timeToShow = hours + ':' + minutes + AMPM;
                if (status === 2 && countCompleted > 0 && recordState != 3)
                {
                    currentVisits += "<tr class='completed'>";
                    currentVisits += "<td>";
                    currentVisits += "<a nohref><span class='companyname'><strong>" + customerName + "</strong></span> | " + subordinateName + "<br>";
                    currentVisits += "<span class='address'  title='"+customerAddress1+"'><i class='fa fa-map-marker'></i> " + customerAddress1 + "</span></a>";
                    currentVisits += "</td>";
                    currentVisits += "<td style='width:75px'><span class='time'> " + timeToShow + "</span></td>"
                    currentVisits += "</tr>";
                } else if (flagComp === 1 && countCompleted == 0 && recordState != 3) {
                    flagComp++;
                    currentVisits += "<tr>";
                    currentVisits += "<td style='text-align: center;color: grey;'> No Data available</td>";
                    currentVisits += "</tr>";
                }

            } else if ((currentVisitVal == "In-Progress"))
            {
                hours = hours > 12 ? (hours - 12) : hours;
                hours = hours < 10 ? '0' + hours.toString() : hours;
                timeToShow = hours + ':' + minutes + AMPM;
                if (status === 1 && countInprogress > 0 && recordState != 3) {
                    currentVisits += "<tr class='inprogess'>";
                    currentVisits += "<td>";
                    currentVisits += "<a nohref><span class='companyname'><strong>" + customerName + "</strong></span> | " + subordinateName + "<br>";
                    currentVisits += "<span class='address'  title='"+customerAddress1+"'><i class='fa fa-map-marker'></i> " + customerAddress1 + "</span></a>";
                    currentVisits += "</td>";
                    currentVisits += "<td style='width:75px'><span class='time'> " + timeToShow + "</span></td>"
                    currentVisits += "</tr>";
                } else if (flagInProg == 1 && countInprogress == 0 && recordState != 3)
                {
                    flagInProg++;
                    currentVisits += "<tr>";
                    currentVisits += "<td style='text-align: center;color: grey;'> No Data available</td>";
                    currentVisits += "</tr>";
                }
            } //else if (((status == 0) && (currentVisitVal == "Pending") && (new Date().getHours() <= discardappointmentEndTimeAMPM[0])) || ((status == 0) && (currentVisitVal == "Pending") && (new Date().getHours() >= discardappointmentEndTimeAMPM[0])))

            else if ((currentVisitVal == "Pending"))
            {
                var curHour = new Date().getHours();
                curHour = curHour * 60 + new Date().getMinutes();
            
                if (status === 0 && recordState != 3 && parseInt(ehours) >= curHour) {
                    hours = hours > 12 ? (hours - 12) : hours;
                    hours = hours < 10 ? '0' + hours.toString() : hours;
                    timeToShow = hours + ':' + minutes + AMPM;

                    currentVisits += "<tr class='pending'>";
                    currentVisits += "<td>";
                    currentVisits += "<a nohref><span class='companyname'><strong>" + customerName + "</strong></span> | " + subordinateName + "<br>";
                    currentVisits += "<span class='address'  title='"+customerAddress1+"'><i class='fa fa-map-marker'></i> " + customerAddress1 + "</span></a>";
                    currentVisits += "</td>";
                    currentVisits += "<td style='width:75px'><span class='time'> " + timeToShow + "</span></td>"
                    currentVisits += "</tr>";
                } else if (flagPend === 1 && countPending == 0 && recordState != 3) {
                    flagPend++;
                    currentVisits += "<tr>";
                    currentVisits += "<td style='text-align: center;color: grey;'> No Data available</td>";
                    currentVisits += "</tr>";
                }
            } else if ((currentVisitVal == "Missed"))
            {
                var endHours = parseInt(timeSplit[0]);
                endHours = endHours * 60 + parseInt(timeSplit[1])+330;

                var curHour = new Date().getHours();
                curHour = curHour * 60 + new Date().getMinutes();
                
                if (status === 0 && recordState != 3 && parseInt(ehours) < curHour) {
                    hours = hours > 12 ? (hours - 12) : hours;
                    hours = hours < 10 ? '0' + hours.toString() : hours;
                    timeToShow = hours + ':' + minutes + AMPM;
                    currentVisits += "<tr class='Missed'>";
                    currentVisits += "<td>";
                    currentVisits += "<a nohref><span class='companyname'><strong>" + customerName + "</strong></span> | " + subordinateName + "<br>";
                    currentVisits += "<span class='address'  title='"+customerAddress1+"'><i class='fa fa-map-marker'></i> " + customerAddress1 + "</span></a>";
                    currentVisits += "</td>";
                    currentVisits += "<td style='width:75px'><span class='time'> " + timeToShow + "</span></td>"
                    currentVisits += "</tr>";
                } else if (flagMiss === 1 && countMissed == 0 && recordState != 3) {
                    flagMiss++;
                    currentVisits += "<tr>";
                    currentVisits += "<td style='text-align: center;color: grey;'> No Data available</td>";
                    currentVisits += "</tr>";
                }
            }
            //outer:continue;

        }
        //continue;
    }
    currentVisits += "</tbody>";
    currentVisits += "</table>";
    return currentVisits;
}

function newCustomers(customerData)
{
    var customersDataHtml = "";
    customersDataHtml += "<!-- BEGIN Portlet PORTLET-->";
    customersDataHtml += "<div class='portlet'>";
    customersDataHtml += "<div class='portlet-title'>";
    customersDataHtml += "<div class='caption'>New Customers";
    customersDataHtml += "</div>";
    customersDataHtml += "</div>";
    customersDataHtml += "<div class='portlet-body'>";
    customersDataHtml += "<div class='' style='height:45px' data-rail-visible='1' data-rail-color='eeeeee' data-handle-color='#a1b2bd'>";
    customersDataHtml += "<div class='col-container'>";
    customersDataHtml += "<div class='col cell-middle'>";
    for (var i = 0; i < customerData.length; i++)
    {
        var countofCustomer = customerData[i].countofCustomer;
    }
    customersDataHtml += "<h4>" + countofCustomer + "</h4>";
    customersDataHtml += "</div>";
    customersDataHtml += "</div>";
    customersDataHtml += "</div>";
    customersDataHtml += "</div>";
    customersDataHtml += "</div>";
    customersDataHtml += "<!-- END Portlet PORTLET-->";
    return customersDataHtml;
}
function Expenses(expenseData)
{
    var expensePortletData = "";
    var total = 0;
    var disburseTotal = 0;
    var pendingApproval = 0;
    var pendingApprovalbyNone = 0;
    var approvedByaccount = 0;
    var approvedByReport = 0;

    for (var i = 0; i < expenseData.length; i++) {
        // debugger;
        var id = expenseData[i].expense.id;
        var amountSpent = expenseData[i].expense.amountSpent;
        var disburse_amount = expenseData[i].expense.disburse_amount;
        var payment_mode = expenseData[i].expense.payment_mode;

        var status = expenseData[i].expense.status;
        var user_id = expenseData[i].user.id;
        total += amountSpent;


        if (status == 0)
        {
            pendingApproval = pendingApprovalbyNone + amountSpent;
        } else if (status == 1)
        {
            pendingApproval = pendingApprovalbyNone + amountSpent;
        } else if (status == 3)
        {
            approvedByaccount = approvedByaccount + amountSpent;
        } else if (status == 5)
        {
            approvedByaccount = approvedByaccount + disburse_amount;
        }
        pendingApprovalbyNone = pendingApproval;
    }

    expensePortletData += "<!-- BEGIN Portlet PORTLET-->";
    expensePortletData += "<div class='portlet'>";
    expensePortletData += "<div class='portlet-title'>";
    expensePortletData += "<div class='caption'>Expenses";
    expensePortletData += "</div>";
//    expensePortletData += "<div class='actions'>";
//    expensePortletData += "<a href='expenses_user.html' class='btn btn-default btn-sm'><i class='fa fa-plus'></i></a>";
////                expensePortletData += "<a href='#' class='btn btn-default btn-sm'><i class='fa fa-reorder'></i></a>";
//    expensePortletData += "</div>";
    expensePortletData += "<div class='chart_data' >";
    expensePortletData += "<b class='sm'>" + approvedByaccount + "</b>/" + total + ""
    expensePortletData += "</div>";
    expensePortletData += "</div>";
    expensePortletData += "<div class='portlet-body'>";
    expensePortletData += "<div class='scroller1' style='height:45px' data-rail-visible='1' data-rail-color='eeeeee' data-handle-color='#a1b2bd'>";
    expensePortletData += "<div class='col-container2'>";
    expensePortletData += "<div class='col'>";
    expensePortletData += "<p>Pending Approval</p>";
    expensePortletData += "<h4><i class='fa fa-inr fa-fw' aria-hidden='true'></i>" + pendingApproval + "</h4>";
    expensePortletData += "</div>";
    expensePortletData += "<div class='col inactive'>";
    expensePortletData += "<p>Claimed</p>";
    expensePortletData += "<h4><i class='fa fa-inr fa-fw' aria-hidden='true'></i>" + total + "</h4>";
    expensePortletData += "</div>";
    expensePortletData += "</div>";
    expensePortletData += "</div>";
    expensePortletData += "</div>";
    expensePortletData += "</div>";
    expensePortletData += "<!-- END Portlet PORTLET-->";
    return expensePortletData;
}
function formFilled(formsFilledData)
{

    var total = 0;
    for (var i = 0; i < formsFilledData.length; i++) {
        var countOfFormFilled = formsFilledData[i].countOfFormFilled;
        total = total + countOfFormFilled;
    }
    var formFilledHtml = "";
    formFilledHtml += "<!-- BEGIN Portlet PORTLET-->";
    formFilledHtml += "<div class='portlet'>";
    formFilledHtml += "<div class='portlet-title'>";
    formFilledHtml += "<div class='caption'>Forms Filled";
    formFilledHtml += "</div>";
    formFilledHtml += "</div>";
    formFilledHtml += "<div class='portlet-body'>";
    formFilledHtml += "<div class='' style='height:45px' data-rail-visible='1' data-rail-color='eeeeee' data-handle-color='#a1b2bd'>";
    formFilledHtml += "<div class='col-container'>";
    formFilledHtml += "<div class='col cell-middle'>";
    formFilledHtml += "<h4>" + total + "</h4>";
    formFilledHtml += "</div>";
    formFilledHtml += "</div>";
    formFilledHtml += "</div>";
    formFilledHtml += "</div>";
    formFilledHtml += "</div>";
    formFilledHtml += "<!-- END Portlet PORTLET-->";
    return formFilledHtml;
}
function Attendance(attendanceData, attendanceUserData)
{
    var totalUserCount = 0;
    var inOffice = 0;
    var onField = 0;
    var total = 0;
    for (var i = 0; i < attendanceUserData.length; i++) {
        totalUserCount = attendanceUserData[i].userCount;
    }
    for (var i = 0; i < attendanceData.length; i++) {
        var punchInLocation = attendanceData[i].punchInLocation;
        var punchInTime = attendanceData[i].punchInTime;
        var punchOutTime = attendanceData[i].punchOutTime;
        var userkey = attendanceData[i].userkey;
        var keyValue = attendanceData[i].keyValue;

        var fullname = attendanceData[i].user.fullname;
        var latitude = attendanceData[i].user.latitude;
        var langitude = attendanceData[i].user.langitude;
        var officeLatitude = attendanceData[i].user.officeLatitude;
        var officeLangitude = attendanceData[i].user.officeLangitude;

        var id = attendanceData[i].user.id;

        var truncLatitude = latitude.toString().substr(0, 5);
        var truncLangitude = langitude.toString().substr(0, 5);
        var truncOfficeLatitude = officeLatitude.toString().substr(0, 5);
        var truncOfficeLangitude = officeLangitude.toString().substr(0, 5);

//        debugger;
        if (punchInTime != "00:00:00")
        {
            total++;
        }
        if ((punchInTime != "00:00:00") && (punchOutTime == "00:00:00")) {
            if ((truncLatitude == truncOfficeLatitude) && (truncLangitude == truncOfficeLangitude)) {
                inOffice++;
            } else {
                onField++;
            }
        }
    }
//    total = onField + inOffice;
    var attendancePortletData = "";
    attendancePortletData += "<!-- BEGIN Portlet PORTLET-->";
    attendancePortletData += "<div class='portlet'>";
    attendancePortletData += "<div class='portlet-title'>";
    attendancePortletData += "<div class='caption'>Attendance";
    attendancePortletData += "</div>";
    attendancePortletData += "<div class='chart_data'>";
    attendancePortletData += "<b class='sm'>" + total + "</b>/" + totalUserCount + ""
    attendancePortletData += "</div>";
    attendancePortletData += "</div>";
    attendancePortletData += "<div class='portlet-body'>";
    attendancePortletData += "<div class='' style='height:45px' data-rail-visible='1' data-rail-color='eeeeee' data-handle-color='#a1b2bd'>";
    attendancePortletData += "<div class='col-container'>";
//    attendancePortletData += "<div class='col cell-middle'>";
//    attendancePortletData += "<p>Punched-In: <strong style='font-size:22px;font-weight: 600;'>" + total + "</strong></p>";
//    attendancePortletData += "</div>";
    attendancePortletData += "<div class='col'>";
    attendancePortletData += "<p>Attendees</p>";
    attendancePortletData += "<h4>" + total + "</h4>";
    attendancePortletData += "</div>";
    attendancePortletData += "<div class='col'>";
    attendancePortletData += "<p>On-Field</p>";
    attendancePortletData += "<h4>" + onField + "</h4>";
    attendancePortletData += "</div>";
    attendancePortletData += "<div class='col'>";
    attendancePortletData += "<p>In-Office</p>";
    attendancePortletData += "<h4>" + inOffice + "</h4>";
    attendancePortletData += "</div>";


    attendancePortletData += "</div>";
    attendancePortletData += "</div>";
    attendancePortletData += "</div>";
    attendancePortletData += "</div>";
    attendancePortletData += "<!-- END Portlet PORTLET-->";



    return attendancePortletData;
}

function currentDateOnly() {
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

    return dateOnly;
}

function ServerDate() {
    var now1 = new Date();
    var now = new Date(now1.getFullYear(), now1.getMonth(), now1.getDate());

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

    return dateToServer;
}


function GetValue(obj)
{
    //debugger;
    var val = $(obj).val();
    //myTeam(attendanceData,val);
    //sample_2
    $("#dv_attendanceData").html(myTeam(attendanceData, val));
}


function getCurrentVisits(obj)
{
    var val = $(obj).val();
    $("#dv_appointmentData").html(currentVisits(appointmentData, val));
}
