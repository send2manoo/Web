/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var intervalIndex = window.setInterval(function () {
    var userToken2 = fieldSenseGetCookie("userToken");
    try {
        if (userToken2.toString() == "undefined") {
            window.location.href = "login.html";
        }
        else if (loginUserId != 0) {
            loggedinUserImageData();
            loggedinUserData();
            userDashBoardData(loginUserId);
            window.clearTimeout(intervalIndex);
        }
    }
    catch (err) {
        window.location.href = "login.html";
    }
}, 1000);
function userDashBoardData(dashboardId){
    time=new Date();
    var loggedInUserDataHtml='<img alt="" src="' + imageURLPath + accountId + '_' + loginUserId + '_29X29.png?' + time + '" onerror="this.src=\'assets/img/usrsml_29.png\';"/>'
    loggedInUserDataHtml+='<span class="membername"> '+loginUserName+'';
    loggedInUserDataHtml+='</span>';
    $('#id_loggedinUser').html(loggedInUserDataHtml);    
    var dateOnly= currentDateOnly(); // Added by Jyoti, (yesterday's Date)      today
    var fromDate = ServerDate();    // Added by Jyoti, (yesterday's Date)       yesterday
//   var beforeyesterday=
    var userDashBoardDataHtml='';
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/team/userPositions/" + dashboardId + "/" + dateOnly + "/" + fromDate ,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            var dashBoardData=data.result;
            for(var i=0;i<dashBoardData.length;i++){
                var userId= dashBoardData[i].user.id;
                if(dashboardId!=userId){
                /*                                                    // Added By manohar
                    var punchin=dashBoardData[i].punchin;
                    var punchout=dashBoardData[i].punchout;
                    var punchin_date=dashBoardData[i].punchin_date;
                    var punchout_date=dashBoardData[i].punchout_date;
                    var two_days_from_current=dashBoardData[i].two_day_from_current_date;
                   
//                    alert("two="+two_days_from_current);
//                    alert("today="+dateOnly+",yesterday="+fromDate);
//                    alert("punchin="+punchin+",punchout="+punchout+",punchin_date"+punchin_date+",punchout_date"+punchout_date+",two_days_from_current="+two_days_from_current+",userId="+userId);
                   */
                    var isUserOnline = dashBoardData[i].isOnline;
                    var canTakeCalls = dashBoardData[i].canTakeCalls;
                    var inMeeting = dashBoardData[i].inMeeting;
                    var teamId = dashBoardData[i].teamId;
                    var userName = dashBoardData[i].user.firstName + " " + dashBoardData[i].user.lastName;
                    var lastKnownLocation = dashBoardData[i].user.lastKnownLocation;
                    var userTime = dashBoardData[i].user.lastKnownLocationTime;
                    var activityCount = dashBoardData[i].activityCount; // Added By Jyoti, 24-02-2017
                    var subordinateCount = dashBoardData[i].subordinateCount;    // Added By Jyoti, 25-02-2017                
                    userTime = convertServerDateToLocalDate((userTime.year) + 1900, userTime.month, userTime.date, userTime.hours, userTime.minutes);
                    var timeToShow;
                    var isUserLocationKnown=false;
                             
                    if(isDateIsTodaysDate(userTime)){
                        isUserLocationKnown=true;
                        var hours = userTime.getHours();
                        var minutes = userTime.getMinutes();
                        timeToShow = ' am';
                        if (hours > 12) {
                            hours = hours - 12;
                            timeToShow = ' pm';
                        } else if (hours == 12) {
                            timeToShow = ' pm';
                        }else if(hours==0){
                            hours=12;
                        }
                        if (hours < 10) {
                            hours = '0' + hours;
                        }
                        if (minutes < 10) {
                            minutes = '0' + minutes;
                        }
                        timeToShow=hours+':'+minutes+timeToShow;
                        lastKnownLocation=lastKnownLocation+' @ '+timeToShow+', Today';
                    }else{
                        lastKnownLocation='Unknown';
                    }
                    var cls_filter;
                    var cls_status;
                    if(inMeeting){
                        cls_filter='col-md-3 col-sm-4 mix category_3';
                        cls_status='inmeet';
                    }else if(canTakeCalls){
                        cls_filter='col-md-3 col-sm-4 mix category_4';
                        cls_status='P_online';
                    }else if(isUserOnline){
                        cls_filter='col-md-3 col-sm-4 mix category_1';
                        cls_status='online';
                    }else{
                        cls_filter='col-md-3 col-sm-4 mix category_2';
                        cls_status='offline';
                    }
                  
                  /*   commented by manohar
                    if(punchin==0)                   // not in, out
                    {
                        punchin="";
                    }
                    if(punchout=='00:00:00')                 // in done 
                    {
                        punchout="";
                    }
                     if(punchout_date=='1111-11-11')                 // in done 
                    {
                        punchout="";
                    }
  
//                      alert("p="+punchout_date+" two_days_from_current="+two_days_from_current);

                  if(punchin_date==two_days_from_current)
                    {
//                   
                        if(punchout==0)
                        {
                          punchout="";

                          userDashBoardDataHtml+='<div class="'+cls_filter+'">';
                          userDashBoardDataHtml+='<div class="mix-inner">';
                          userDashBoardDataHtml+='<div class="meet-our-team">';
                          time = new Date();                                                                     
                          userDashBoardDataHtml+='<h3><img alt="" src="' + imageURLPath + accountId + '_' + userId + '_45X45.png?' + time + '" onerror="this.src=\'assets/img/usrsml_45.png\';"/> <span class="membername"> '+userName+'<h6 style="font-size: 8pt">Punch In :  '+punchin_date+' '+punchin+'</h6><h6 style="font-size: 8pt">Punch Out : '+punchout+'</h6> </span>   <i class="'+cls_status+'"></i></h3>';
                          userDashBoardDataHtml+='<div class="team-info">';
                          userDashBoardDataHtml+='<div class="form-body clearfix">';
                          userDashBoardDataHtml+='<div class="form-group">';
                        }  
                        else
                        {
                            
                         userDashBoardDataHtml+='<div class="'+cls_filter+'">';
                         userDashBoardDataHtml+='<div class="mix-inner">';
                         userDashBoardDataHtml+='<div class="meet-our-team">';
                         time = new Date();                                                                           
                         userDashBoardDataHtml+='<h3><img alt="" src="' + imageURLPath + accountId + '_' + userId + '_45X45.png?' + time + '" onerror="this.src=\'assets/img/usrsml_45.png\';"/> <span class="membername"> '+userName+'<h6 style="font-size: 8pt">Punch In :  '+punchin_date+' '+punchin+'</h6><h6 style="font-size: 8pt">Punch Out : '+punchout_date+' '+punchout+'</h6> </span>   <i class="'+cls_status+'"></i></h3>';
                         userDashBoardDataHtml+='<div class="team-info">';
                         userDashBoardDataHtml+='<div class="form-body clearfix">';
                         userDashBoardDataHtml+='<div class="form-group">';
                        }
                    }
                   
         
             else{ 
                  
                    if(punchin_date==fromDate.split(" ")[0])
                    {
//                   
                        if(punchout==0)
                        {
                          punchout="";

                          userDashBoardDataHtml+='<div class="'+cls_filter+'">';
                          userDashBoardDataHtml+='<div class="mix-inner">';
                          userDashBoardDataHtml+='<div class="meet-our-team">';
                          time = new Date();                                                                     
                          userDashBoardDataHtml+='<h3><img alt="" src="' + imageURLPath + accountId + '_' + userId + '_45X45.png?' + time + '" onerror="this.src=\'assets/img/usrsml_45.png\';"/> <span class="membername"> '+userName+'<h6 style="font-size: 8pt">Punch In :  '+punchin_date+' '+punchin+'</h6><h6 style="font-size: 8pt">Punch Out : '+punchout+'</h6> </span>   <i class="'+cls_status+'"></i></h3>';
                          userDashBoardDataHtml+='<div class="team-info">';
                          userDashBoardDataHtml+='<div class="form-body clearfix">';
                          userDashBoardDataHtml+='<div class="form-group">';
                        }  
                        else
                        {
                            
                          userDashBoardDataHtml+='<div class="'+cls_filter+'">';
                         userDashBoardDataHtml+='<div class="mix-inner">';
                         userDashBoardDataHtml+='<div class="meet-our-team">';
                         time = new Date();                                                                           
                         userDashBoardDataHtml+='<h3><img alt="" src="' + imageURLPath + accountId + '_' + userId + '_45X45.png?' + time + '" onerror="this.src=\'assets/img/usrsml_45.png\';"/> <span class="membername"> '+userName+'<h6 style="font-size: 8pt">Punch In :  '+punchin_date+' '+punchin+'</h6><h6 style="font-size: 8pt">Punch Out : '+punchout_date+' '+punchout+'</h6> </span>   <i class="'+cls_status+'"></i></h3>';
                         userDashBoardDataHtml+='<div class="team-info">';
                         userDashBoardDataHtml+='<div class="form-body clearfix">';
                         userDashBoardDataHtml+='<div class="form-group">';
                        }
                    }
                   
                   
                   else if(punchin_date==dateOnly.split(" ")[0])
                   {
                      
                      if(punchout==0)
                      {
                          
                            punchout="";
                            userDashBoardDataHtml+='<div class="'+cls_filter+'">';
                            userDashBoardDataHtml+='<div class="mix-inner">';
                            userDashBoardDataHtml+='<div class="meet-our-team">';
                            time = new Date();                                                                     
                            userDashBoardDataHtml+='<h3><img alt="" src="' + imageURLPath + accountId + '_' + userId + '_45X45.png?' + time + '" onerror="this.src=\'assets/img/usrsml_45.png\';"/> <span class="membername"> '+userName+'<h6 style="font-size: 8pt">Punch In :  '+punchin+'</h6><h6 style="font-size: 8pt">Punch Out : '+punchout+'</h6> </span>   <i class="'+cls_status+'"></i></h3>';


                            userDashBoardDataHtml+='<div class="team-info">';
                            userDashBoardDataHtml+='<div class="form-body clearfix">';
                            userDashBoardDataHtml+='<div class="form-group">';
                      }
                      else 
                      {
                            userDashBoardDataHtml+='<div class="'+cls_filter+'">';
                            userDashBoardDataHtml+='<div class="mix-inner">';
                            userDashBoardDataHtml+='<div class="meet-our-team">';
                            time = new Date();                                                                     
                            userDashBoardDataHtml+='<h3><img alt="" src="' + imageURLPath + accountId + '_' + userId + '_45X45.png?' + time + '" onerror="this.src=\'assets/img/usrsml_45.png\';"/> <span class="membername"> '+userName+'<h6 style="font-size: 8pt">Punch In :  '+punchin+'</h6><h6 style="font-size: 8pt">Punch Out : '+punchout+'</h6> </span>   <i class="'+cls_status+'"></i></h3>';


                            userDashBoardDataHtml+='<div class="team-info">';
                            userDashBoardDataHtml+='<div class="form-body clearfix">';
                            userDashBoardDataHtml+='<div class="form-group">';
                      }
                   }
                 
                   else if(punchout==0)
                    {
                      punchout="";   
                       userDashBoardDataHtml+='<div class="'+cls_filter+'">';
                      userDashBoardDataHtml+='<div class="mix-inner">';
                      userDashBoardDataHtml+='<div class="meet-our-team">';
                      time = new Date();                                                                     
                      userDashBoardDataHtml+='<h3><img alt="" src="' + imageURLPath + accountId + '_' + userId + '_45X45.png?' + time + '" onerror="this.src=\'assets/img/usrsml_45.png\';"/> <span class="membername"> '+userName+'<h6 style="font-size: 8pt">Punch In :'+punchin_date+' '+punchin+'</h6><h6 style="font-size: 8pt">Punch Out :'+punchout+'</h6> </span>   <i class="'+cls_status+'"></i></h3>';
                      userDashBoardDataHtml+='<div class="team-info">';
                      userDashBoardDataHtml+='<div class="form-body clearfix">';
                      userDashBoardDataHtml+='<div class="form-group">';

                    }
                    
                    else 
                    {
                      punchin="";  
                      punchout="";   
                      userDashBoardDataHtml+='<div class="'+cls_filter+'">';
                      userDashBoardDataHtml+='<div class="mix-inner">';
                      userDashBoardDataHtml+='<div class="meet-our-team">';
                      time = new Date();                                                                     
                      userDashBoardDataHtml+='<h3><img alt="" src="' + imageURLPath + accountId + '_' + userId + '_45X45.png?' + time + '" onerror="this.src=\'assets/img/usrsml_45.png\';"/> <span class="membername"> '+userName+'<h6 style="font-size: 8pt">Punch In :'+punchin+'</h6><h6 style="font-size: 8pt">Punch Out :'+punchout+'</h6> </span>   <i class="'+cls_status+'"></i></h3>';
                      userDashBoardDataHtml+='<div class="team-info">';
                      userDashBoardDataHtml+='<div class="form-body clearfix">';
                      userDashBoardDataHtml+='<div class="form-group">';

                    }
                    
                 }
                 */  // commented by manohar
                
                
                  userDashBoardDataHtml+='<div class="'+cls_filter+'">';
                      userDashBoardDataHtml+='<div class="mix-inner">';
                      userDashBoardDataHtml+='<div class="meet-our-team">';
                      time = new Date();                                                                     
                      userDashBoardDataHtml+='<h3><img alt="" src="' + imageURLPath + accountId + '_' + userId + '_45X45.png?' + time + '" onerror="this.src=\'assets/img/usrsml_45.png\';"/> <span class="membername"> '+userName+' <i class="'+cls_status+'"></i></h3>';
                      userDashBoardDataHtml+='<div class="team-info">';
                      userDashBoardDataHtml+='<div class="form-body clearfix">';
                      userDashBoardDataHtml+='<div class="form-group">';
                      
                      
                    if(isUserLocationKnown){
                        userDashBoardDataHtml+='<a href="#" title="Location" onClick="moveToUsersLocationOnMap(\''+userId+'\',\''+userName+'\',\''+teamId+'\');"><label class="control-label col-md-12"><li class="fa fa-map-marker"></li> '+lastKnownLocation+'</label></a>';
                    }else{
                        userDashBoardDataHtml+='<label class="control-label col-md-12"><li class="fa fa-map-marker"></li> '+lastKnownLocation+'</label>';
                    }
                    userDashBoardDataHtml+='</div>';
                    userDashBoardDataHtml+='</div>';
                    userDashBoardDataHtml+='<div class="wrapper">';
                    userDashBoardDataHtml+='<div class="panel-group" id="accordion">';
                    userDashBoardDataHtml+='<div class="panel-heading">';
                    var id_userActivities='collapse_on'+userId;
                    userDashBoardDataHtml+='<a id="visit_'+userId+'" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#'+id_userActivities+'"  onClick="userActivitiesToday('+userId+')">';
                    userDashBoardDataHtml+='<h5 class="panel-title">';
                    userDashBoardDataHtml+='Visits [ '+activityCount+' ]';  // Added by Jyoti, for displaying visits count
                    userDashBoardDataHtml+='<i class="indicator fa fa-angle-left  pull-right"></i>';
                    userDashBoardDataHtml+='</h5>';
                    userDashBoardDataHtml+='</a>';
                    userDashBoardDataHtml+='</div>';
                    userDashBoardDataHtml+='<div id="'+id_userActivities+'" class="panel-collapse collapse">';
                    userDashBoardDataHtml+='<span id="id_activities'+userId+'"><span>';
                    userDashBoardDataHtml+='</div>';
                    userDashBoardDataHtml+='<div class="panel-heading">';
                    var id_userSubOrdinates='collapse_sub'+userId;
                    userDashBoardDataHtml+='<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#'+id_userSubOrdinates+'" onClick="userImmidiateSubOrdinatesInDashboard('+userId+');">';
                    userDashBoardDataHtml+='<h5 class="panel-title">';
                    userDashBoardDataHtml+='Subordinates [ '+subordinateCount+' ]';   // Added by Jyoti, for displaying subordinate count
                    userDashBoardDataHtml+='<i class="indicator fa fa-angle-left  pull-right"></i>';
                    userDashBoardDataHtml+='</h5>';
                    userDashBoardDataHtml+='</a>';
                    userDashBoardDataHtml+='</div>';
                    userDashBoardDataHtml+='<div id="'+id_userSubOrdinates+'" class="panel-collapse collapse">';
                    userDashBoardDataHtml+='<span id="id_userSubOrdinates'+userId+'"><span>';
                    userDashBoardDataHtml+='</div>';
                    userDashBoardDataHtml+='</div>';
                    userDashBoardDataHtml+='</div>';
                    userDashBoardDataHtml+='</div>';
                    userDashBoardDataHtml+='</div>';
                    userDashBoardDataHtml+='</div>';
                    userDashBoardDataHtml+='</div>';
                }
            }
            $('#id_dashboardData').html(userDashBoardDataHtml);
            App.init();
            Portfolio.init();

            /*About Scroller*/
            $('.wrapper').find('a[href="#"]').on('click', function (e) {
                e.preventDefault();
                this.expand = !this.expand;
                $(this).text(this.expand?"Show Less Activities":"Show More Activities");
                $(this).closest('.wrapper').find('.small1, .big').toggleClass('small1 big');

            });
            $('.small1, .big').slimScroll({
                opacity: '0.1'
            });
            function toggleChevron(e) {
                $(e.target)
                .prev('.panel-heading')
                .find("i.indicator")
                .toggleClass('fa-angle-down fa-angle-left');
            }
            $('#accordion,#accordion1,#accordion2,#accordion3,#accordion4,#accordion5,#accordion6,#accordion7,#accordion8,#accordion9,#accordion10').on('hidden.bs.collapse', toggleChevron);
            $('#accordion,#accordion1,#accordion2,#accordion3,#accordion4,#accordion5,#accordion6,#accordion7,#accordion8,#accordion9,#accordion10').on('shown.bs.collapse', toggleChevron);

        }
    });
}
function userActivitiesToday(userId){
    if(($("#collapse_on"+userId).hasClass("collapsed in")==true) || ($("#collapse_on"+userId).hasClass("panel-collapse in")==true)){
	return false;
    }	    
    var dateToServer = ServerDate();   // Added by Jyoti

    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/appointment/user/" + userId + "/" + dateToServer,
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
                var activityData = data.result;
                var dashboardUserActivitiesHtml='';
                dashboardUserActivitiesHtml+='<div class="panel-body">';
                dashboardUserActivitiesHtml+='<div class="small1">';
                dashboardUserActivitiesHtml+='<div class="portlet-body form">';
                dashboardUserActivitiesHtml+='<form class="form-horizontal" role="form">';
                dashboardUserActivitiesHtml+='<div class="form-body">';
                for(var i=0;i<activityData.length;i++){
                    var activityDateTime = activityData[i].sdateTime;
                    activityDateTime = activityDateTime.split(' ');
                    var activityDate = activityDateTime[0];
                    activityDate = activityDate.split('-');
                    var activityTime = activityDateTime[1];
                    activityTime = activityTime.split(':');
                    var activityDateJs = convertServerDateToLocalDate(activityDate[0], activityDate[1] - 1, activityDate[2], activityTime[0], activityTime[1]);
                    activityDateTime = activityDateJs.getFullYear() + "-" + (activityDateJs.getMonth() + 1) + "-" + activityDateJs.getDate() + " " + activityDateJs.getHours() + ":" + activityDateJs.getMinutes() + ":00.0";
                    var splitDateTime = activityDateTime.split(' ');
                    var time = splitDateTime[1];
                    var timeSplit = time.split(':');
                    var hours = timeSplit[0];
                    var minutes = timeSplit[1];
                    var timeToShow = ' am';
                    if (hours > 12) {
                        hours = hours - 12;
                        timeToShow = ' pm';
                    } else if (hours == 12) {
                        timeToShow = ' pm';
                    }
                    if (hours < 10) {
                        hours = '0' + hours;
                    }
                    if (minutes < 10) {
                        minutes = '0' + minutes;
                    }
                    timeToShow=hours+':'+minutes+timeToShow;
                    var activtyOfCustomer = activityData[i].customer.customerPrintas;
                    var activityStatus = activityData[i].status;
                    if (activityStatus == 0) {
                        activityStatus = 'Pending';
                    }
                    else if (activityStatus == 1) {
                        activityStatus = 'In-Progress';
                    } else if (activityStatus == 2) {
                        activityStatus = 'Completed';
                    }else if(activityStatus == 2){
                        activityStatus = 'Cancelled';
                    }
                    dashboardUserActivitiesHtml+='<h5>Today at '+timeToShow+'</h5>';
                    dashboardUserActivitiesHtml+='<div class="row">';
                    dashboardUserActivitiesHtml+='<div class="col-md-12">';
                    dashboardUserActivitiesHtml+='<div class="form-group">';
                    dashboardUserActivitiesHtml+='<label class="col-md-8">'+activtyOfCustomer+'.</label>';
                    dashboardUserActivitiesHtml+='<label class="col-md-4">'+activityStatus+'</label>';
                    dashboardUserActivitiesHtml+='</div>';
                    dashboardUserActivitiesHtml+='</div>';
                    dashboardUserActivitiesHtml+='</div>';
                    if((i+1)!=activityData.length){
                        dashboardUserActivitiesHtml+='<li class="divider">';
                        dashboardUserActivitiesHtml+='</li>';
                    }
                }
                if(activityData.length==0){
                    dashboardUserActivitiesHtml='';
                    dashboardUserActivitiesHtml+='<div class="panel-body">';
                    dashboardUserActivitiesHtml+='<div class="small1" style="height:40px;">';
                    dashboardUserActivitiesHtml+='<div class="portlet-body form">';
                    dashboardUserActivitiesHtml+='<form class="form-horizontal" role="form">';
                    dashboardUserActivitiesHtml+='<div class="form-body">';
                    dashboardUserActivitiesHtml+='<lable class="col-md-8" style="margin-left:63px; margin-top:16px; "> None </lable';
                }
                dashboardUserActivitiesHtml+='</div>';
                dashboardUserActivitiesHtml+='</form>';
                dashboardUserActivitiesHtml+='</div>';
                dashboardUserActivitiesHtml+='</div>';
                dashboardUserActivitiesHtml+='</div>';
                $('#id_activities'+userId).html(dashboardUserActivitiesHtml);
                $('.small1, .big').slimScroll({
                    opacity: '0.1'
                });
            }
        }
    });
}
function userImmidiateSubOrdinatesInDashboard(userId){
    if(($("#collapse_sub"+userId).hasClass("collapsed in")==true) || ($("#collapse_sub"+userId).hasClass("panel-collapse in")==true)){
	return false;
    }
    var dateOnly= currentDateOnly();    // Added by Jyoti    
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/team/userPositions/" + userId+"/"+dateOnly,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            var userSubordinates = data.result;
            var userImmidiateSubOrdinatesInDashboardHtml='';
            userImmidiateSubOrdinatesInDashboardHtml+='<div class="panel-body">';
            userImmidiateSubOrdinatesInDashboardHtml+='<div class="small1">';
            userImmidiateSubOrdinatesInDashboardHtml+='<div class="portlet-body form">';
            userImmidiateSubOrdinatesInDashboardHtml+='<form class="form-horizontal" role="form">';
            userImmidiateSubOrdinatesInDashboardHtml+='<div class="form-body">';
            for(var i=0;i<userSubordinates.length;i++){
                var subordinateId = userSubordinates[i].user.id;
                if(userId!=subordinateId){
                    var userName = userSubordinates[i].user.firstName + " " + userSubordinates[i].user.lastName;
                    var isUserOnline = userSubordinates[i].isOnline;
                    var canTakeCalls = userSubordinates[i].canTakeCalls;
                    var inMeeting = userSubordinates[i].inMeeting;
                    var cls_status;
                    if(inMeeting){
                        cls_status='inmeet';
                    }else if(canTakeCalls){
                        cls_status='P_online';
                    }else if(isUserOnline){
                        cls_status='online';
                    }else{
                        cls_status='offline';
                    }
                    userImmidiateSubOrdinatesInDashboardHtml+='<div class="row">';
                    userImmidiateSubOrdinatesInDashboardHtml+='<label class="col-md-12">'+userName+' <i class="'+cls_status+'"></i></label>';
                    userImmidiateSubOrdinatesInDashboardHtml+='</div>';
                    if((i+1)!=userSubordinates.length){
                        userImmidiateSubOrdinatesInDashboardHtml+='<li class="divider"></li>';
                    }
                }
            }
            if(userSubordinates.length==1){
                userImmidiateSubOrdinatesInDashboardHtml='';
                userImmidiateSubOrdinatesInDashboardHtml+='<div class="panel-body">';
                userImmidiateSubOrdinatesInDashboardHtml+='<div class="small1" style="height:40px;" >';
                userImmidiateSubOrdinatesInDashboardHtml+='<div class="portlet-body form">';
                userImmidiateSubOrdinatesInDashboardHtml+='<form class="form-horizontal" role="form">';
                userImmidiateSubOrdinatesInDashboardHtml+='<div class="form-body">';
                userImmidiateSubOrdinatesInDashboardHtml+='<lable class="col-md-8" style="margin-left:63px; margin-top:16px;"> None </lable>';
            }
            userImmidiateSubOrdinatesInDashboardHtml+='</div>';
            userImmidiateSubOrdinatesInDashboardHtml+='</form>';
            userImmidiateSubOrdinatesInDashboardHtml+='</div>';
            userImmidiateSubOrdinatesInDashboardHtml+='</div>';
            userImmidiateSubOrdinatesInDashboardHtml+='</div>';
            $('#id_userSubOrdinates'+userId).html(userImmidiateSubOrdinatesInDashboardHtml);
            $('.small1, .big').slimScroll({
                opacity: '0.1'
            });
        }
    });
}

function currentDateOnly(){
    var dateOnly=new Date();
    dateOnly = convertLocalDateToServerDate(dateOnly.getFullYear(), dateOnly.getMonth(), dateOnly.getDate(), dateOnly.getHours(), dateOnly.getMinutes());
    var month=dateOnly.getMonth()+1;
    var date=dateOnly.getDate();
    if(month<10){
        month='0'+month;
    }
    if(date<10){
        date='0'+date;
    }
    dateOnly=dateOnly.getFullYear()+"-"+month+"-"+date;
    
    return dateOnly;
}

function ServerDate(){
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

function moveToUsersLocationOnMap(userId,userName,teamId){
    document.cookie="specificUserId="+userId;
    document.cookie="specificUserName="+userName;
    document.cookie="specificUserTeamId="+teamId;
    window.location.href = "index.html";
}
function clickOnFieldUser(){
    $('#id_onfieldUser').addClass("active");
    $('#id_offfieldUser').removeClass("active");
    $('#id_inMeetingUser').removeClass("active");
    $('#id_cantakecallUser').addClass("active");
    $('#id_directReports').removeClass("active");
}
function clickOffFieldUser(){
    $('#id_onfieldUser').removeClass("active");
    $('#id_offfieldUser').addClass("active");
    $('#id_inMeetingUser').removeClass("active");
    $('#id_cantakecallUser').removeClass("active");
    $('#id_directReports').removeClass("active");
}
function clickOnInMeetingUser(){
    $('#id_onfieldUser').removeClass("active");
    $('#id_offfieldUser').removeClass("active");
    $('#id_inMeetingUser').addClass("active");
    $('#id_cantakecallUser').removeClass("active");
    $('#id_directReports').removeClass("active");
}
function clickOnCanTakeCallUser(){
    $('#id_onfieldUser').removeClass("active");
    $('#id_offfieldUser').removeClass("active");
    $('#id_inMeetingUser').removeClass("active");
    $('#id_cantakecallUser').addClass("active");
    $('#id_directReports').removeClass("active");
}
function clickOnDirectReportsUser(){
    $('#id_onfieldUser').removeClass("active");
    $('#id_offfieldUser').removeClass("active");
    $('#id_inMeetingUser').removeClass("active");
    $('#id_cantakecallUser').removeClass("active");
    $('#id_directReports').addClass("active");
}
