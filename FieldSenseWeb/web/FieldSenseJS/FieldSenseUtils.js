/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * @author Ramesh
 * @date 13-03-2014
 */

var userToken = fieldSenseGetCookie("userToken");
var loginUserId = 0;
var loginUserName;
var loginUserEmailId;
var accountId;
var accountName; // by nikhil
var imageURLPath;
var expenseImageURLPath;
var customFormImageURLPath;
var role;
var globalSplit = '.@#%*.';
var firstTeamId;
var usersArray = new Array();
var time = new Date();
var errorFileURL;
var loginUserLatitude;
var loginUserLangitude;
var loginUserDesignation;
var companyName;
/*
 *userMode Paramer gives 1 if he is admin mode and gives 2 if he is user mode . deafult is user mode .
 */
var userMode = fieldSenseGetCookie("userMode");
function getUserDetails() {
    $.ajax({
        type: "GET",
        url: fieldSenseURL + '/authenticate',
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: false,
        success: function (data) {
            if (data.errorCode == '0000') {
                loginUserId = data.result.userId;
                loginUserName = data.result.userFirstName + " " + data.result.userLastName;
                loginUserEmailId = data.result.userEmailAddress;
                imageURLPath = data.result.imsgrRetivalPath;
                expenseImageURLPath = data.result.expenseImageRetivalPath;
                customFormImageURLPath = data.result.customFormImageRetivalPath;
                accountId = data.result.accountId;
                role = data.result.role;
                importErrorFileURLPath = data.result.importErrorFileRetrivePath;
                loginUserLatitude = data.result.latitude;
                loginUserLangitude = data.result.langitude;
                loginUserDesignation = data.result.userDesignation;
                firstTeamId = data.result.teamId;
                companyName = data.result.companyName;//added  by nikhil

                document.cookie = "loginUserIdcookie=" + loginUserId;
                document.cookie = "loginUserNamecookie=" + loginUserName;
                document.cookie = "accountIdcookie=" + accountId;
                document.cookie = "accountNamecookie=" + companyName;
                document.cookie = "UserRolecookie=" + role;
                console.log("auth "+companyName);
            } else {
                FieldSenseInvalidToken(data.errorMessage);
            }
            usersOfFieldSenseAccount();
        }
    });
}
getUserDetails();


function fieldSenseseMesageTimeFormateForJsonDate(jsonDate) {
    var timeZoneOffSet = jsonDate.timezoneOffset * 60 * 1000;
    var jsDate = new Date(jsonDate.time - timeZoneOffSet);
    return fieldSenseseMesageTimeFormateForJSDate(jsDate);
}
function fieldSenseseMesageTimeFormateForJSDate(jsDate) {
    var getDate;
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

    if (jsDate.getDate() < 10) {
        getDate = '0' + jsDate.getDate();
    }
    else {
        getDate = jsDate.getDate();
    }
    var date = (jsDate.getFullYear()) + '-' + ((jsDate.getMonth())) + '-' + getDate;
    date = date.split(" ");
    var dateToShow = date[0];
    dateToShow = dateToShow.split('-');
    var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    var fieldsenseDate = dateToShow[2] + ' ' + monthNames[dateToShow[1]] + ' ' + dateToShow[0] + ',' + hours + ':' + minutes + timeToShow;
    return fieldsenseDate;
}
function loggedinUserImageData() {
    var loggedinUserImageDataHtml = '';
    time = new Date();
    loggedinUserImageDataHtml += '<img alt="" src="' + imageURLPath + accountId + '_' + loginUserId + '_29X29.png?' + time + '" onerror="this.src=\'assets/img/usrsml_29.png\';"/>';
    loggedinUserImageDataHtml += '<span class="username" id="id_userNmaeRightCorner">';
    loggedinUserImageDataHtml += ' ' + loginUserName + ' ';
    loggedinUserImageDataHtml += '</span>';
    loggedinUserImageDataHtml += '<i class="fa fa-angle-down"></i>';
    $('#id_usaerImage').html(loggedinUserImageDataHtml);
}
function loggedinUserData() {
    var loggedinUserDataHtml = '';
    loggedinUserDataHtml += '<li>';
    if (userMode == 1) {
        loggedinUserDataHtml += '<a href="adminProfile.html"><i class="fa fa-user"></i> My Profile</a>';
    } else if (userMode == 2 || userMode == 3) {
        loggedinUserDataHtml += '<a href="profile.html"><i class="fa fa-user"></i> My Profile</a>';
    }
    loggedinUserDataHtml += '</li>';
    loggedinUserDataHtml += '<li>';
    loggedinUserDataHtml += '<a data-toggle="modal" href="#responsivechangepwd" onClick="changePassWordTemplate();return false;"><i class="fa fa-lock"></i> Change Password</a>';
    loggedinUserDataHtml += '</li>';
    if ((role == 1 || role==6)) {       //modified by manohar
        loggedinUserDataHtml += '<li>';
         if (userMode == 1 && role==6) {
            loggedinUserDataHtml += '<a data-toggle="modal" href="#" onClick="userPanelTemplate();return false;"><i class="fa fa-user"></i> User Panel</a>';
            loggedinUserDataHtml += '<a data-toggle="modal" href="#responsivechangepwd" onClick="accountSettingsTemplate();return false;"><i class="fa fa-user"></i> Account Settings</a>';
        } else if (userMode == 1) {
            loggedinUserDataHtml += '<a data-toggle="modal" href="#" onClick="userPanelTemplate();return false;"><i class="fa fa-user"></i> User Panel</a>';
            loggedinUserDataHtml += '<a data-toggle="modal" href="#responsivechangepwd" onClick="accountSettingsTemplate();return false;"><i class="fa fa-user"></i> Account Settings</a>';
        }  else if ((userMode == 2) || (userMode == 2 || role==6)) {
            loggedinUserDataHtml += '<a data-toggle="modal" href="#" onClick="adminPanelTemplate();return false;"><i class="fa fa-sitemap"></i> Admin Panel</a>';
        }
        loggedinUserDataHtml += '</li>';
    }/*else if(userMode==3){
     loggedinUserDataHtml +='<li><a href="page_calendar.html"><i class="fa fa-calendar"></i> My Calendar</a></li>';
     loggedinUserDataHtml +='<li><a href="inbox.html"><i class="fa fa-envelope"></i>My Inbox<span class="badge badge-danger">3</span></a></li>';
     loggedinUserDataHtml +='<li><a href="#"><i class="fa fa-tasks"></i> My Tasks<span class="badge badge-success">7</span></a></li>';				
     }*/
    //modified by manohar
     if ((role == 2 || role==7 || role==3 || role==8)) {
        loggedinUserDataHtml += '<li>';
        if (userMode == 3 && role==2) {
            loggedinUserDataHtml += '<a data-toggle="modal" href="#" onClick="userPanelTemplate();return false;"><i class="fa fa-user"></i> User Panel</a>';
        }
        else if (userMode == 3 && role==7) {
            loggedinUserDataHtml += '<a data-toggle="modal" href="#" onClick="userPanelTemplate();return false;"><i class="fa fa-user"></i> User Panel</a>';
        }else if (userMode == 3) {
            loggedinUserDataHtml += '<a data-toggle="modal" href="#" onClick="userPanelTemplate();return false;"><i class="fa fa-user"></i> User Panel</a>';
            loggedinUserDataHtml += '<a data-toggle="modal" href="#" onClick="adminPanelTemplate();return false;"><i class="fa fa-sitemap"></i> Admin Panel</a>';
        }
        else if ((userMode == 2 && role==2) || (userMode == 2 && role==7)) {
            loggedinUserDataHtml += '<a data-toggle="modal" href="#" onClick="accountPanelTemplate();return false;"><i class="fa fa-sitemap"></i> Account Panel</a>';
        }
        else if ((userMode == 2 && role==3) || (userMode == 2 && role==8)) {
               loggedinUserDataHtml += '<a data-toggle="modal" href="#" onClick="adminPanelTemplate();return false;"><i class="fa fa-sitemap"></i> Admin Panel</a>';
            loggedinUserDataHtml += '<a data-toggle="modal" href="#" onClick="accountPanelTemplate();return false;"><i class="fa fa-user"></i> Account Panel</a>';
        }
        else if ((userMode == 1 && role==3) || (userMode==1 && role==8)) {
            loggedinUserDataHtml += '<a data-toggle="modal" href="#" onClick="accountPanelTemplate();return false;"><i class="fa fa-user"></i> Account Panel</a>';
            loggedinUserDataHtml += '<a data-toggle="modal" href="#" onClick="userPanelTemplate();return false;"><i class="fa fa-user"></i> User Panel</a>';
            loggedinUserDataHtml += '<a data-toggle="modal" href="#responsivechangepwd" onClick="accountSettingsTemplate();return false;"><i class="fa fa-user"></i> Account Settings</a>';
        }
        else
        {
             loggedinUserDataHtml += '<a data-toggle="modal" href="#" onClick="accountPanelTemplate();return false;"><i class="fa fa-user"></i> Account Panel</a>';           
        }
        loggedinUserDataHtml += '</li>';
    } // ended by manohar
    loggedinUserDataHtml += '<li>';
    loggedinUserDataHtml += '<a data-toggle="modal" href="#feedback" onclick="userFeedbackTemplate();return false;" ><i class="fa fa-comment"></i> Give Feedback </a>';
    loggedinUserDataHtml += '</li>';
    loggedinUserDataHtml += '<li>';
    loggedinUserDataHtml += '<li>';
    loggedinUserDataHtml += '<a href="" onClick="logout();return false;"><i class="fa fa-key"></i> Log Out</a>';
    loggedinUserDataHtml += '</li>';
    loggedinUserDataHtml += '</ul>';
    loggedinUserDataHtml += '</li>';
    $('#id_loginuser').html(loggedinUserDataHtml);
}
function logout() {
    $.ajax({
        type: "DELETE",
        url: fieldSenseURL + "/authenticate/0",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: false,
        success: function (data) {
            if (data.errorCode == '0000') {
                deleteCookie("userToken");
                deleteCookie("role");
                deleteCookie("userMode");
                window.location.href = "login.html";
            } else {
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}

function getMonthFromString(month) {
    return new Date(Date.parse(month + " 1, 2012")).getMonth() + 1
}
function usersOfFieldSenseAccount() {
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/user",
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
        }
    });
}
function FieldSenseClosePopUp(id) {
    $(id).css("display", "none");
}

function formatJSDate(value)
{
    var month = value.getMonth() + 1;
    var year = value.getFullYear();
    var date = value.getDate()
    return  year + "-" + month + "-" + date;
}
function trimNumber(s) {
    while (s.substr(0, 1) == '0' && s.length > 1) {
        s = s.substr(1, 9999);
    }
    return s;
}

/**
 * @added by jyoti
 */
function changeAutoPunchOutSetting(source) {

    if ($('#po_hourly').prop("checked")) {
        
        $("#id_specific_select").css("display", "none");
        $("#id_hourly_select").css("display", "block");
        $('.working_Hours').hide();
        
    } else if ($('#po_specific').prop("checked")) {
        
        $("#id_hourly_select").css("display", "none");
        $("#id_specific_select").css("display", "block");
        $('.working_Hours').show();
        
    }

}

function accountSettingsTemplate() {
    $('.ls_changePassword').html('');
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/account/settings/values",
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
                var accountSettings = data.result;
                var allowTimeoutAll = accountSettings.allowTimeoutForAll;
                var timeoutEnable = accountSettings.allowTimeout;
                var timeZone = accountSettings.timeZone;
                // added by jyoti 21-12-2016, for offline feature
                var offlineEnable = accountSettings.allowOffline;
                // Added by jyoti, #28115 Wants Auto Punch-Out, at specific configurable time.
                var autoPunchOutTime = accountSettings.auto_punch_out_time;
                var autoPunchOutType = accountSettings.auto_punch_out_type;
                var workingHours = accountSettings.working_hours;
                var po_specific_time = "";
                var interval1 = accountSettings.interval;
                //alert(interval1+" interval1");
                //added by nikhil 23rd jun
                if (interval1 == "1") {
                    interval1 = "1";
                }
                //ended by nikhil
                var accountSettingsTemplateHtml = '';
                accountSettingsTemplateHtml += '<div class="modal-dialog modal-wide">';
                accountSettingsTemplateHtml += '<div class="modal-content">';
                accountSettingsTemplateHtml += '<div class="modal-header">';
                accountSettingsTemplateHtml += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
                accountSettingsTemplateHtml += '<h4 class="modal-title">Account Settings</h4>';
                accountSettingsTemplateHtml += '</div>';
                accountSettingsTemplateHtml += '<div class="modal-body">';
                accountSettingsTemplateHtml += '<form class="form-horizontal" role="form">';
                accountSettingsTemplateHtml += '<div class="form-body">';
                
                // Added by jyoti, #28115 Wants Auto Punch-Out, at specific configurable time.

                accountSettingsTemplateHtml += '<div class="form-group">';
                accountSettingsTemplateHtml += '<label class="col-md-4 control-label radio-list">Auto Punch out</label>';

                if (autoPunchOutType == 0) {                     
                    po_specific_time = "00:00 AM";
                    accountSettingsTemplateHtml += '<label class="radio-inline col-md-2" >';
                    accountSettingsTemplateHtml += '<input type="radio" name="radio_auto_punchOut" id="po_hourly" value="0" onclick="changeAutoPunchOutSetting(this.id)" checked > Hourly </label>';
                    
                    // for hourly time selection
                    accountSettingsTemplateHtml += '<label class="checkbox-inline col-md-2" style="margin-left:-50px;" >';                    
                    accountSettingsTemplateHtml += '<select id="id_hourly_select" class="form-control btn-default" data-live-search="true"  data-width="fit" tabindex="1"  style="display:none;"  >';
                    var selectedValue = "";
                    var i = 12;
                    while (i <= 48) {
                        if (i == autoPunchOutTime) {
                            selectedValue = "selected";
                        } else {
                            selectedValue = '';
                        }
                        accountSettingsTemplateHtml += '<option value=' + i + ' ' + selectedValue + ' >' + i + '</option>';
                        i = i + i;
                    }
                    accountSettingsTemplateHtml += '<select> </label>';
                    
                    accountSettingsTemplateHtml += '<label class="radio-inline col-md-2">';
                    accountSettingsTemplateHtml += '<input type="radio" name="radio_auto_punchOut" id="po_specific" value="1" onclick="changeAutoPunchOutSetting(this.id)" > Specific </label>';
                    
                    // for specific time selection
                    accountSettingsTemplateHtml += '<input type="text" id="id_specific_select" class="form-control col-md-2" value="' + po_specific_time + '" style="display:none; width: 90px; margin-top: 5px;" tabindex="1"  readonly >';
                    
                } else if (autoPunchOutType == 1) {
                    po_specific_time = autoPunchOutTime;
                    accountSettingsTemplateHtml += '<label class="radio-inline col-md-2">';
                    accountSettingsTemplateHtml += '<input type="radio" name="radio_auto_punchOut" id="po_hourly" value="0" onclick="changeAutoPunchOutSetting(this.id)" > Hourly </label>';
                    // for hourly time selection
                    accountSettingsTemplateHtml += '<label class="checkbox-inline col-md-2" style="margin-left:-50px;" >';                    
                    accountSettingsTemplateHtml += '<select id="id_hourly_select" class="form-control btn-default" data-live-search="true"  data-width="fit" tabindex="1"  style="display: none"  >';
                    var selectedValue = "";
                    var i = 12;
                    while (i <= 48) {
                        if (i == autoPunchOutTime) {
                            selectedValue = "selected";
                        } else {
                            selectedValue = '';
                        }
                        accountSettingsTemplateHtml += '<option value=' + i + ' ' + selectedValue + ' >' + i + '</option>';
                        i = i + i;
                    }
                    accountSettingsTemplateHtml += '<select> </label>';
                    
                    accountSettingsTemplateHtml += '<label class="radio-inline col-md-2">';
                    accountSettingsTemplateHtml += '<input type="radio" name="radio_auto_punchOut" id="po_specific" value="1" onclick="changeAutoPunchOutSetting(this.id)" checked > Specific </label>';

                    // for specific time selection
                    accountSettingsTemplateHtml += '<input type="text" id="id_specific_select" class="form-control col-md-2" value="' + po_specific_time + '" style="display: none; width: 90px; margin-top: 5px;" tabindex="1"  readonly >';
                    
                }

                accountSettingsTemplateHtml += '</div>';

                // for working hours selection
                accountSettingsTemplateHtml += '<div class="form-group working_Hours" style="display:none;" >';
                accountSettingsTemplateHtml += '<label class="col-md-4 control-label">Working hours</label>';
                accountSettingsTemplateHtml += '<div class="col-md-8">';
                accountSettingsTemplateHtml += '<div class="checkbox-list">';
                accountSettingsTemplateHtml += '<label class="checkbox-inline">';
                accountSettingsTemplateHtml += '<select id="working_hours_id" class="form-control" style="width: 55px;">';
                var selectedValue = "";
                for(var i=1; i<=15; i++){
                    
                    if(i == workingHours){
                        selectedValue = "selected";
                    } else {
                        selectedValue = "";
                    }
                    accountSettingsTemplateHtml += '<option value='+i+' '+selectedValue+' >'+i+'</option>';
                }
                accountSettingsTemplateHtml += '<select>';
                
                accountSettingsTemplateHtml += '</div>';
                accountSettingsTemplateHtml += '</div>';
                accountSettingsTemplateHtml += '</div>';
                // ended
                
                // Ended by jyoti, #28115 Wants Auto Punch-Out, at specific configurable time.
                
                accountSettingsTemplateHtml += '<div class="form-group">';
                accountSettingsTemplateHtml += '<label class="col-md-4 control-label">Allow Timeout</label>';
                accountSettingsTemplateHtml += '<div class="col-md-8">';
                accountSettingsTemplateHtml += '<div class="checkbox-list">';
                accountSettingsTemplateHtml += '<label class="checkbox-inline">';
                if (timeoutEnable == true) {
                    accountSettingsTemplateHtml += '<input type="checkbox" id="account_allowtimeout" checked></label>';
                } else {
                    accountSettingsTemplateHtml += '<input type="checkbox" id="account_allowtimeout"></label>';
                }
                accountSettingsTemplateHtml += '</div>';
                accountSettingsTemplateHtml += '</div>';
                accountSettingsTemplateHtml += '</div>';
                //        
                accountSettingsTemplateHtml += '<div class="form-group">';
                accountSettingsTemplateHtml += '<label class="col-md-4 control-label">Allow Timeout For All Users</label>';
                accountSettingsTemplateHtml += '<div class="col-md-8">';
                accountSettingsTemplateHtml += '<div class="checkbox-list">';
                accountSettingsTemplateHtml += '<label class="checkbox-inline">';
                if (allowTimeoutAll == true) {
                    accountSettingsTemplateHtml += '<input type="checkbox" id="account_allowtimeoutforall" checked></label>';
                } else {
                    accountSettingsTemplateHtml += '<input type="checkbox" id="account_allowtimeoutforall"></label>';
                }
                accountSettingsTemplateHtml += '</div>';
                accountSettingsTemplateHtml += '</div>';
                accountSettingsTemplateHtml += '</div>';
                //Added by Jyoti
                accountSettingsTemplateHtml += '<div class="form-group">';
                accountSettingsTemplateHtml += '<label class="col-md-4 control-label">Allow Offline</label>';
                accountSettingsTemplateHtml += '<div class="col-md-8">';
                accountSettingsTemplateHtml += '<div class="checkbox-list">';
                accountSettingsTemplateHtml += '<label class="checkbox-inline">';
                if (offlineEnable == true) {
                    accountSettingsTemplateHtml += '<input type="checkbox" id="account_allowoffline" checked></label>';
                } else {
                    accountSettingsTemplateHtml += '<input type="checkbox" id="account_allowoffline"></label>';
                }
                accountSettingsTemplateHtml += '</div>';
                accountSettingsTemplateHtml += '</div>';
                accountSettingsTemplateHtml += '</div>';
                //ended by jyoti
                // added by nikhil 23rd ju 2017
                accountSettingsTemplateHtml += '<div class="form-group">';
                accountSettingsTemplateHtml += '<label class="col-md-4 control-label">Location Interval (In Minute) </label>';
                accountSettingsTemplateHtml += '<div class="col-md-8">';
                accountSettingsTemplateHtml += '<div class="checkbox-list">';
                accountSettingsTemplateHtml += '<label class="checkbox-inline">';
                accountSettingsTemplateHtml += '<select id="id_Interval" class="form-control" style="width: 50px;">';
                var selectedValue = "";
                for(var i=1; i<=5; i++){
                    
                    if(i == interval1){
                        selectedValue = "selected";
                    } else {
                        selectedValue = "";
                    }
                    accountSettingsTemplateHtml += '<option value='+i+' '+selectedValue+' >'+i+'</option>';
                }
                accountSettingsTemplateHtml += '<select>';
                
                accountSettingsTemplateHtml += '</div>';
                accountSettingsTemplateHtml += '</div>';
                accountSettingsTemplateHtml += '</div>';
                //ended by nikhil

//Start of siddhesh Pande>>> Time Zone
                accountSettingsTemplateHtml += '<div class="form-group">';
                accountSettingsTemplateHtml += '<label class="col-md-4 control-label">Local Time Zone Setting </label>';
                accountSettingsTemplateHtml += '<div class="col-md-8">';
                accountSettingsTemplateHtml += '<div class="checkbox-list">';
                accountSettingsTemplateHtml += '<label class="checkbox-inline">';
//		    if(offlineEnable==true){
                accountSettingsTemplateHtml += '<select id="id_TimeZone" class="form-control" style="width: 400px;">';
accountSettingsTemplateHtml += '<option value="W. Europe Standard Time (GMT+01:00)"> Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna (GMT+01:00)</option>'
accountSettingsTemplateHtml += '<option value="Central Europe Standard Time (GMT+01:00)"> Belgrade, Bratislava, Budapest, Ljubljana, Prague (GMT+01:00)</option>'
accountSettingsTemplateHtml += '<option value="Romance Standard Time (GMT+01:00)"> Brussels, Copenhagen, Madrid, Paris (GMT+01:00)</option>'
accountSettingsTemplateHtml += '<option value="Central European Standard Time (GMT+01:00)"> Sarajevo, Skopje, Warsaw, Zagreb (GMT+01:00)</option>'
accountSettingsTemplateHtml += '<option value="W. Central Africa Standard Time (GMT+01:00)"> West Central Africa (GMT+01:00)</option>'
accountSettingsTemplateHtml += '<option value="Jordan Standard Time (GMT+02:00)"> Amman (GMT+02:00)</option>'
accountSettingsTemplateHtml += '<option value="GTB Standard Time (GMT+02:00)"> Athens, Bucharest, Istanbul (GMT+02:00)</option>'
accountSettingsTemplateHtml += '<option value="Middle East Standard Time (GMT+02:00)"> Beirut (GMT+02:00)</option>'
accountSettingsTemplateHtml += '<option value="Egypt Standard Time (GMT+02:00)"> Cairo (GMT+02:00)</option>'
accountSettingsTemplateHtml += '<option value="South Africa Standard Time (GMT+02:00)"> Harare, Pretoria (GMT+02:00)</option>'
accountSettingsTemplateHtml += '<option value="FLE Standard Time (GMT+02:00)"> Helsinki, Kyiv, Riga, Sofia, Tallinn, Vilnius (GMT+02:00)</option>'
accountSettingsTemplateHtml += '<option value="Israel Standard Time (GMT+02:00)"> Jerusalem (GMT+02:00)</option>'
accountSettingsTemplateHtml += '<option value="E. Europe Standard Time (GMT+02:00)"> Minsk (GMT+02:00)</option>'
accountSettingsTemplateHtml += '<option value="Namibia Standard Time (GMT+02:00)"> Windhoek (GMT+02:00)</option>'
accountSettingsTemplateHtml += '<option value="Arabic Standard Time (GMT+03:00)"> Baghdad (GMT+03:00)</option>'
accountSettingsTemplateHtml += '<option value="Arab Standard Time (GMT+03:00)"> Kuwait, Riyadh (GMT+03:00)</option>'
accountSettingsTemplateHtml += '<option value="Russian Standard Time (GMT+03:00)"> Moscow, St. Petersburg, Volgograd (GMT+03:00)</option>'
accountSettingsTemplateHtml += '<option value="E. Africa Standard Time (GMT+03:00)"> Nairobi (GMT+03:00)</option>'
accountSettingsTemplateHtml += '<option value="Georgian Standard Time (GMT+03:00)"> Tbilisi (GMT+03:00)</option>'
accountSettingsTemplateHtml += '<option value="Iran Standard Time (GMT+03:30)"> Tehran (GMT+03:30)</option>'
accountSettingsTemplateHtml += '<option value="Arabian Standard Time (GMT+04:00)"> Abu Dhabi, Muscat (GMT+04:00)</option>'
accountSettingsTemplateHtml += '<option value="Azerbaijan Standard Time (GMT+04:00)"> Baku (GMT+04:00)</option>'
accountSettingsTemplateHtml += '<option value="Mauritius Standard Time (GMT+04:00)"> Port Louis (GMT+04:00)</option>'
accountSettingsTemplateHtml += '<option value="Caucasus Standard Time (GMT+04:00)"> Yerevan (GMT+04:00)</option>'
accountSettingsTemplateHtml += '<option value="Afghanistan Standard Time (GMT+04:30)"> Kabul (GMT+04:30)</option>'
accountSettingsTemplateHtml += '<option value="Ekaterinburg Standard Time (GMT+05:00)"> Ekaterinburg (GMT+05:00)</option>'
accountSettingsTemplateHtml += '<option value="Pakistan Standard Time (GMT+05:00)"> Islamabad, Karachi (GMT+05:00)</option>'
accountSettingsTemplateHtml += '<option value="West Asia Standard Time (GMT+05:00)"> Tashkent (GMT+05:00)</option>'
accountSettingsTemplateHtml += '<option value="India Standard Time (GMT+05:30)"> Chennai, Kolkata, Mumbai, New Delhi (GMT+05:30)</option>'
accountSettingsTemplateHtml += '<option value="Sri Lanka Standard Time (GMT+05:30)"> Sri Jayawardenepura (GMT+05:30)</option>'
accountSettingsTemplateHtml += '<option value="Nepal Standard Time (GMT+05:45)"> Kathmandu (GMT+05:45)</option>'
accountSettingsTemplateHtml += '<option value="N. Central Asia Standard Time (GMT+06:00)">Almaty, Novosibirsk (GMT+06:00)</option>'
accountSettingsTemplateHtml += '<option value="Central Asia Standard Time (GMT+06:00)"> Astana, Dhaka (GMT+06:00)</option>'
accountSettingsTemplateHtml += '<option value="Myanmar Standard Time (GMT+06:30)"> Yangon (Rangoon) (GMT+06:30)</option>'
accountSettingsTemplateHtml += '<option value="SE Asia Standard Time (GMT+07:00)"> Bangkok, Hanoi, Jakarta (GMT+07:00)</option>'
accountSettingsTemplateHtml += '<option value="North Asia Standard Time (GMT+07:00)"> Krasnoyarsk (GMT+07:00)</option>'
accountSettingsTemplateHtml += '<option value="China Standard Time (GMT+08:00)">Beijing, Chongqing, Hong Kong, Urumqi (GMT+08:00)</option>'
accountSettingsTemplateHtml += '<option value="North Asia East Standard Time (GMT+08:00)"> Irkutsk, Ulaan Bataar (GMT+08:00)</option>'
accountSettingsTemplateHtml += '<option value="Singapore Standard Time (GMT+08:00)"> Kuala Lumpur, Singapore (GMT+08:00)</option>'
accountSettingsTemplateHtml += '<option value="W. Australia Standard Time (GMT+08:00)"> Perth (GMT+08:00)</option>'
accountSettingsTemplateHtml += '<option value="Taipei Standard Time (GMT+08:00)"> Taipei (GMT+08:00)</option>'
accountSettingsTemplateHtml += '<option value="Tokyo Standard Time (GMT+09:00)"> Osaka, Sapporo, Tokyo (GMT+09:00)</option>'
accountSettingsTemplateHtml += '<option value="Korea Standard Time (GMT+09:00)"> Seoul (GMT+09:00)</option>'
accountSettingsTemplateHtml += '<option value="Yakutsk Standard Time (GMT+09:00)"> Yakutsk (GMT+09:00)</option>'
accountSettingsTemplateHtml += '<option value="Cen. Australia Standard Time (GMT+09:30)"> Adelaide (GMT+09:30)</option>'
accountSettingsTemplateHtml += '<option value="AUS Central Standard Time (GMT+09:30)"> Darwin (GMT+09:30)</option>'
accountSettingsTemplateHtml += '<option value="E. Australia Standard Time (GMT+10:00)"> Brisbane (GMT+10:00)</option>'
accountSettingsTemplateHtml += '<option value="AUS Eastern Standard Time (GMT+10:00)"> Canberra, Melbourne, Sydney (GMT+10:00)</option>'
accountSettingsTemplateHtml += '<option value="West Pacific Standard Time (GMT+10:00)"> Guam, Port Moresby (GMT+10:00)</option>'
accountSettingsTemplateHtml += '<option value="Tasmania Standard Time (GMT+10:00)"> Hobart (GMT+10:00)</option>'
accountSettingsTemplateHtml += '<option value="Vladivostok Standard Time (GMT+10:00)"> Vladivostok (GMT+10:00)</option>'
accountSettingsTemplateHtml += '<option value="Central Pacific Standard Time (GMT+11:00)"> Magadan, Solomon Is., New Caledonia (GMT+11:00)</option>'
accountSettingsTemplateHtml += '<option value="New Zealand Standard Time (GMT+12:00) ">Auckland, Wellington (GMT+12:00)</option>'
accountSettingsTemplateHtml += '<option value="Fiji Standard Time (GMT+12:00) ">Fiji, Kamchatka, Marshall Is. (GMT+12:00)</option>'
accountSettingsTemplateHtml += '<option value="Tonga Standard Time (GMT+13:00) ">Nukualofa (GMT+13:00)</option>'
accountSettingsTemplateHtml += '<option value="Azores Standard Time (GMT-01:00)"> Azores (GMT-01:00)</option>'
accountSettingsTemplateHtml += '<option value="Cape Verde Standard Time (GMT-01:00)"> Cape Verde Is. (GMT-01:00)</option>'
accountSettingsTemplateHtml += '<option value="Mid-Atlantic Standard Time (GMT-02:00)"> Mid-Atlantic (GMT-02:00)</option>'
accountSettingsTemplateHtml += '<option value="E. South America Standard Time (GMT-03:00)"> Brasilia (GMT-03:00)</option>'
accountSettingsTemplateHtml += '<option value="Argentina Standard Time (GMT-03:00)"> Buenos Aires (GMT-03:00)</option>'
accountSettingsTemplateHtml += '<option value="SA Eastern Standard Time (GMT-03:00)"> Georgetown (GMT-03:00)</option>'
accountSettingsTemplateHtml += '<option value="Greenland Standard Time (GMT-03:00)"> Greenland (GMT-03:00)</option>'
accountSettingsTemplateHtml += '<option value="Montevideo Standard Time (GMT-03:00)"> Montevideo (GMT-03:00)</option>'
accountSettingsTemplateHtml += '<option value="Newfoundland Standard Time (GMT-03:30)"> Newfoundland (GMT-03:30)</option>'
accountSettingsTemplateHtml += '<option value="Atlantic Standard Time (GMT-04:00)"> Atlantic Time (Canada) (GMT-04:00)</option>'
accountSettingsTemplateHtml += '<option value="SA Western Standard Time (GMT-04:00)"> La Paz (GMT-04:00)</option>'
accountSettingsTemplateHtml += '<option value="Central Brazilian Standard Time (GMT-04:00)"> Manaus (GMT-04:00)</option>'
accountSettingsTemplateHtml += '<option value="Pacific SA Standard Time (GMT-04:00)"> Santiago (GMT-04:00)</option>'
accountSettingsTemplateHtml += '<option value="Venezuela Standard Time (GMT-04:30)"> Caracas (GMT-04:30)</option>'
accountSettingsTemplateHtml += '<option value="SA Pacific Standard Time (GMT-05:00)"> Bogota, Lima, Quito, Rio Branco (GMT-05:00)</option>'
accountSettingsTemplateHtml += '<option value="Eastern Standard Time (GMT-05:00)"> Eastern Time (US & Canada) (GMT-05:00)</option>'
accountSettingsTemplateHtml += '<option value="US Eastern Standard Time (GMT-05:00)"> Indiana (East) (GMT-05:00)</option>'
accountSettingsTemplateHtml += '<option value="Central America Standard Time (GMT-06:00)"> Central America (GMT-06:00)</option>'
accountSettingsTemplateHtml += '<option value="Central Standard Time (GMT-06:00)"> Central Time (US & Canada) (GMT-06:00)</option>'
accountSettingsTemplateHtml += '<option value="Central Standard Time (Mexico) (GMT-06:00)"> Guadalajara, Mexico City, Monterrey (GMT-06:00)</option>'
accountSettingsTemplateHtml += '<option value="Canada Central Standard Time(GMT-06:00)" >Saskatchewan (GMT-06:00)</option>'
accountSettingsTemplateHtml += '<option value="US Mountain Standard Time (GMT-07:00)"> Arizona (GMT-07:00)</option>'
accountSettingsTemplateHtml += '<option value="Mountain Standard Time (Mexico) (GMT-07:00)"> Chihuahua, La Paz, Mazatlan (GMT-07:00)</option>'
accountSettingsTemplateHtml += '<option value="Mountain Standard Time (GMT-07:00)"> Mountain Time (US & Canada)  (GMT-07:00)</option>'
accountSettingsTemplateHtml += '<option value="Pacific Standard Time (GMT-08:00)"> Pacific Time (US & Canada) (GMT-08:00)</option>'
accountSettingsTemplateHtml += '<option value="Pacific Standard Time (Mexico)(GMT-08:00)"> Tijuana, Baja California (GMT-08:00)</option>'
accountSettingsTemplateHtml += '<option value="Alaskan Standard Time (GMT-09:00)"> Alaska (GMT-09:00)</option>'
accountSettingsTemplateHtml += '<option value="Hawaiian Standard Time (GMT-10:00)"> Hawaii (GMT-10:00)</option>'
accountSettingsTemplateHtml += '<option value="Samoa Standard Time (GMT-11:00)"> Midway Island, Samoa (GMT-11:00)</option>'
accountSettingsTemplateHtml += '<option value="Dateline Standard Time (GMT-12:00)"> International Date Line West (GMT-12:00)</option>'
                accountSettingsTemplateHtml += '<select>';
//		    }else{
//		    	accountSettingsTemplateHtml += '<input type="checkbox" id="account_allowoffline"></label>';
//		    }
                accountSettingsTemplateHtml += '</div>';
                accountSettingsTemplateHtml += '</div>';
                accountSettingsTemplateHtml += '</div>';
//End of siddhesh
                accountSettingsTemplateHtml += '</div>';
                accountSettingsTemplateHtml += '</form>';
                accountSettingsTemplateHtml += '</div>';
                accountSettingsTemplateHtml += '<div class="modal-footer">';
                accountSettingsTemplateHtml += '<button type="submit" class="btn btn-info" onClick="changeAccountSettings();return;">Save</button>';
                accountSettingsTemplateHtml += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
                accountSettingsTemplateHtml += '</div>';
                accountSettingsTemplateHtml += '</div>';
                accountSettingsTemplateHtml += '</div>';
                $('.cls_changePassword').html(accountSettingsTemplateHtml);
                $("#id_TimeZone").val(timeZone);
                // Added by jyoti, #28115 Wants Auto Punch-Out, at specific configurable time.
                if (autoPunchOutType == 0) { // hourly
                    
                    $("#id_specific_select").css("display", "none");
                    $("#id_hourly_select").css("display", "block");
                    $('.working_Hours').hide();
                    
                } else if (autoPunchOutType == 1) { // specific time
                    
                    $("#id_hourly_select").css("display", "none");
                    $("#id_specific_select").css("display", "block");
                    $('.working_Hours').show();
                    
                }

                $('#id_specific_select').clockface({
                    trigger: 'focus',
                    format: 'hh:mm A'
                });

//                $('.selectpicker').selectpicker({
//                    style: 'btn-info',
//                    size: 4
//                });
                // Ended by jyoti
                
                $('#pleaseWaitDialog').modal('hide');                
                // App.init();
                App.initUniform();
                App.callHandleScrollers();
                App.fixContentHeight();
            }
        }
    });
}

function changeAccountSettings() {
    var timeoutEnable = document.getElementById("account_allowtimeout").checked;
    var timeoutEnableForAll = document.getElementById("account_allowtimeoutforall").checked;
    
    // Added by jyoti, #28115 Wants Auto Punch-Out, at specific configurable time.
    var autoPunchOutTime;
    var autoPunchOutType;

    if ($('#po_hourly').prop("checked")) {
        autoPunchOutType = 0;
        autoPunchOutTime = $('#id_hourly_select').val();
    } else if ($('#po_specific').prop("checked")) {
        autoPunchOutType = 1;
        autoPunchOutTime = $('#id_specific_select').val();
        if (autoPunchOutTime.includes(":")) {            
            if (autoPunchOutTime.split(":")[0].length == 0) {
                autoPunchOutTime = "00" + autoPunchOutTime;
            }
        } else if (!autoPunchOutTime.includes(":") && autoPunchOutTime.length != 0) {            
            var timeArray = autoPunchOutTime.split(" ");            
            autoPunchOutTime = timeArray[0] + ":00 " + timeArray[1];
        } else if (autoPunchOutTime.length == 0) {
            fieldSenseTosterError("Time can not be blank, Please select time", true);
            return false;
        }
    }
    var working_hours = $('#working_hours_id').val();
    // ended by jyoti

    // Added by jyoti
    var offlineEnable = document.getElementById("account_allowoffline").checked;

    //added by nikhil
    var e = document.getElementById("id_Interval");
    var Interval = e.options[e.selectedIndex].value;
    //added by siddhesh 
    var indexOfselected = document.getElementById("id_TimeZone");
    var TimeZone = indexOfselected.options[indexOfselected.selectedIndex].value;
    //  alert("changeAccountSettings()"+e.options[e.selectedIndex].value);
    
    var passwordObject = {
        "allowTimeout": timeoutEnable,
        "allowTimeoutForAll": timeoutEnableForAll,
        "allowOffline": offlineEnable,
        "interval": e.options[e.selectedIndex].value,
//        "auto_punch_out_time": autoPunchOutTime, // Added by jyoti
//        "auto_punch_out_type": autoPunchOutType, // Added by jyoti
        "timeZone":TimeZone,
        "auto_punch_out_time": autoPunchOutTime, // Added by jyoti
        "auto_punch_out_type": autoPunchOutType, // Added by jyoti
        "working_hours": working_hours // Added by jyoti, 6-june
        
    };
    var jsonData = JSON.stringify(passwordObject);
    $.ajax({
        type: "PUT",
        url: fieldSenseURL + "/account/settings/values",
        headers: {
            "userToken": userToken
        },
        contentType: "application/json; charset=utf-8",
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        asyn: false,
        success: function (data) {
            if (data.errorCode == '0000') {
                $(".cls_changePassword").modal('hide');
                fieldSenseTosterSuccess(data.errorMessage, true);
            } else {
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}

function changePassWordTemplate() {
    var changePassWordTemplateHtml = '';
    changePassWordTemplateHtml += '<div class="modal-dialog modal-wide">';
    changePassWordTemplateHtml += '<div class="modal-content">';
    changePassWordTemplateHtml += '<div class="modal-header">';
    changePassWordTemplateHtml += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
    changePassWordTemplateHtml += '<h4 class="modal-title">Change Password</h4>';
    changePassWordTemplateHtml += '</div>';
    changePassWordTemplateHtml += '<div class="modal-body">';
    changePassWordTemplateHtml += '<form class="form-horizontal" role="form">';
    changePassWordTemplateHtml += '<div class="form-body">';
    changePassWordTemplateHtml += '<div class="form-group">';
    changePassWordTemplateHtml += '<label class="col-md-4 control-label">Old Password</label>';
    changePassWordTemplateHtml += '<div class="col-md-8">';
    changePassWordTemplateHtml += '<input type="hidden" id="id_hidden" value="Val"/>';
    changePassWordTemplateHtml += '<input type="password" autocomplete="off" name="password" class="form-control" placeholder="Old Password" id="old_password">';
    changePassWordTemplateHtml += '</div>';
    changePassWordTemplateHtml += '</div>';
    changePassWordTemplateHtml += '<div class="form-group">';
    changePassWordTemplateHtml += '<label class="col-md-4 control-label">New Password</label>';
    changePassWordTemplateHtml += '<div class="col-md-8">';
    changePassWordTemplateHtml += '<input type="hidden" id="id_hidden" value="Val"/>';
    changePassWordTemplateHtml += '<input type="password" autocomplete="off" name="password" class="form-control" placeholder="New Password" id="new_password">';
    changePassWordTemplateHtml += '</div>';
    changePassWordTemplateHtml += '</div>';
    changePassWordTemplateHtml += '<div class="form-group">';
    changePassWordTemplateHtml += '<label class="col-md-4 control-label">Confirm Password</label>';
    changePassWordTemplateHtml += '<div class="col-md-8">';
    changePassWordTemplateHtml += '<input type="hidden" id="id_hidden" value="Val"/>';
    changePassWordTemplateHtml += '<input type="password" autocomplete="off" name="password" class="form-control" placeholder="Confirm Password" id="confirm_password">';
    changePassWordTemplateHtml += '</div>';
    changePassWordTemplateHtml += '</div>';
    changePassWordTemplateHtml += '</div>';
    changePassWordTemplateHtml += '</form>';
    changePassWordTemplateHtml += '</div>';
    changePassWordTemplateHtml += '<div class="modal-footer">';
    changePassWordTemplateHtml += '<button type="submit" class="btn btn-info" onClick="changePassword();return;">Save</button>';
    changePassWordTemplateHtml += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
    changePassWordTemplateHtml += '</div>';
    changePassWordTemplateHtml += '</div>';
    changePassWordTemplateHtml += '</div>';
    $('.cls_changePassword').html(changePassWordTemplateHtml);
}
function changePassword() {
    var oldPassword = document.getElementById("old_password").value;
    oldPassword = oldPassword.trim();

    if (oldPassword.length < 8 || oldPassword.length > 20) {
        fieldSenseTosterError("Old password length should be 8 to 20 characters .", true);
        return false;
    }
    var newPassword = document.getElementById("new_password").value;
    newPassword = newPassword.trim();
    if (newPassword.length < 8 || newPassword.length > 20) {
        fieldSenseTosterError("New password length should be 8 to 20 characters .", true);
        return false;
    }
    var confirmPassword = document.getElementById("confirm_password").value;
    confirmPassword = confirmPassword.trim();
    if (newPassword != confirmPassword) {
        fieldSenseTosterError("Confirm password does not match with password .", true);
        return false;
    }
    var passwordObject = {
        "oldPassword": oldPassword,
        "newPassword": newPassword,
        "confirmPassword": confirmPassword
    };
    var jsonData = JSON.stringify(passwordObject);
    $.ajax({
        type: "POST",
        url: fieldSenseURL + "/password/change",
        headers: {
            "userToken": userToken
        },
        contentType: "application/json; charset=utf-8",
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        asyn: false,
        success: function (data) {
            if (data.errorCode == '0000') {
                $(".cls_changePassword").modal('hide');
                fieldSenseTosterSuccess(data.errorMessage, true);
            } else {
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}
function fieldSenseseDateFormateForJsonDate(jsonDate) {
    var timeZoneOffSet = jsonDate.timezoneOffset * 60 * 1000;
    var jsDate = new Date(jsonDate.time - timeZoneOffSet);
    return fieldSenseseDateFormateForJSDate(jsDate);
}
function fieldSenseseDateFormateForJSDate(jsDate) {
    var getDate;
    var hours = jsDate.getHours();
    var minutes = jsDate.getMinutes();
    var timeToShow = ' am';
    if (hours < 10) {
        hours = '0' + hours;
    } else {
        if (hours > 12) {
            hours = hours - 11;
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

    if (jsDate.getDate() < 10) {
        getDate = '0' + jsDate.getDate();
    }
    else {
        getDate = jsDate.getDate();
    }
    var date = (jsDate.getFullYear()) + '-' + ((jsDate.getMonth() + 1)) + '-' + getDate;
    date = date.split(" ");
    var dateToShow = date[0];
    dateToShow = dateToShow.split('-');
    var month = dateToShow[1];
    //    month = month+1;
    var fieldsenseDate = month + ' ' + dateToShow[2] + ' ' + dateToShow[0];
    return fieldsenseDate;
}
function fieldSenseseForJSDate(dateTime) {
    var splitDateTime = dateTime.split(' ');
    var date = splitDateTime[0];
    var time = splitDateTime[1];
    var splitdate = date.split('-');
    var year = splitdate[0];
    var month = splitdate[1];
    month = month - 1;
    var da = splitdate[2];
    if (da < 10) {
        da = '0' + da;
    }
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
    var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    var fieldsenseDate = da + ' ' + monthNames[month] + ' ' + year + ' , ' + hours + ':' + minutes + timeToShow;
    return fieldsenseDate;
}
function fieldSenseDateTimeForNotes(jsonDate) {
    var jsDate = convertServerDateToLocalDate(jsonDate.year + 1900, jsonDate.month, jsonDate.date, jsonDate.hours, jsonDate.minutes);
    var timeToShow = ' am';
    var hours = jsDate.getHours();
    var minutes = jsDate.getMinutes();
    var getDate;
    if (hours < 10) {
        hours = '0' + hours;
    } else {
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

    if (jsDate.getDate() < 10) {
        getDate = '0' + jsDate.getDate();
    }
    else {
        getDate = jsDate.getDate();
    }
    var date = (jsDate.getFullYear()) + '-' + ((jsDate.getMonth())) + '-' + getDate;
    date = date.split(" ");
    var dateToShow = date[0];
    dateToShow = dateToShow.split('-');
    var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    var fieldsenseDate = dateToShow[2] + ' ' + monthNames[dateToShow[1]] + ' ' + dateToShow[0] + ',' + hours + ':' + minutes + timeToShow;
    return fieldsenseDate;
}
function fieldSenseDateTimeForExpense(jsonDate) {
    var jsDate = convertServerDateToLocalDate(jsonDate.year + 1900, jsonDate.month, jsonDate.date, jsonDate.hours, jsonDate.minutes);
    var timeToShow = ' am';
    var hours = jsDate.getHours();
    var minutes = jsDate.getMinutes();
    var getDate;
    if (hours < 10) {
        hours = '0' + hours;
    } else {
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

    if (jsDate.getDate() < 10) {
        getDate = '0' + jsDate.getDate();
    }
    else {
        getDate = jsDate.getDate();
    }
    var date = (jsDate.getFullYear()) + '-' + ((jsDate.getMonth())) + '-' + getDate;
    date = date.split(" ");
    var dateToShow = date[0];
    dateToShow = dateToShow.split('-');
    var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    var fieldsenseDate = dateToShow[2] + ' ' + monthNames[dateToShow[1]] + ' ' + dateToShow[0] + ' ' + hours + ':' + minutes + timeToShow;
    return fieldsenseDate;
}
function fieldSenseDateTimeForExpenseJsDate(jsDate) {
    //    jsDate = convertServerDateToLocalDate(jsDate.year + 1900, jsDate.month, jsDate.date, jsDate.hours, jsDate.minutes);
    var timeToShow = ' am';
    var hours = jsDate.getHours();
    var minutes = jsDate.getMinutes();
    var getDate;
    if (hours < 10) {
        hours = '0' + hours;
    } else {
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

    if (jsDate.getDate() < 10) {
        getDate = '0' + jsDate.getDate();
    }
    else {
        getDate = jsDate.getDate();
    }
    var date = (jsDate.getFullYear()) + '-' + ((jsDate.getMonth())) + '-' + getDate;
    date = date.split(" ");
    var dateToShow = date[0];
    dateToShow = dateToShow.split('-');
    var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    var fieldsenseDate = dateToShow[2] + ' ' + monthNames[dateToShow[1]] + ' ' + dateToShow[0] + ' ' + hours + ':' + minutes + timeToShow;
    return fieldsenseDate;
}
function convertLocalDateToServerDate(year, month, date, hours, minutes) {
    var d = new Date(year, month, date, hours, minutes);
    var curDate = new Date();
    var timeZone = curDate.getTimezoneOffset();
    d.setMinutes(d.getMinutes() + timeZone);
    return d;
}
function convertLocalDateToServerDate1(year, month, date, hours, minutes) {
    var d = new Date(year, month, date, hours, minutes);
    var curDate = new Date();
    var timeZone = curDate.getTimezoneOffset();
    d.setMinutes(d.getMinutes());
    
        var month = d.getMonth() + 1;
    if (month < 10) {
        month = '0' + month;
    }
    var date = d.getDate();
    if (date < 10) {
        date = '0' + date;
    }
    var hours = d.getHours();
    if (hours < 10) {
        hours = '0' + hours;
    }
    var minutes = d.getMinutes();
    if (minutes < 10) {
        minutes = '0' + minutes;
    }
    d = d.getFullYear() + "-" + month + "-" + date + " " + hours + ":" + minutes + ':00';
    var dateToSelected = d.split(' ')[0]; //added by nikhil
    return dateToSelected;
}
function convertServerDateToLocalDate(year, month, date, hours, minutes) {
    var d = new Date(year, month, date, hours, minutes);
    var curDate = new Date();
    var timeZone = curDate.getTimezoneOffset();
    d.setMinutes(d.getMinutes() - timeZone);
    return d;
}

function convertServerDateToLocalTimezone(year, month, date, hours, minutes, timezoneOffset) {
    var d = new Date(year, month, date, hours, minutes);
    var curDate = new Date();
    //d.setMinutes(d.getMinutes() - timezoneOffset);
    return d;
}
function htmlEntities(string) {
    string = string.replace(/"/g, "&quot;");
    string = string.replace(/'/g, "&apos;");
    string = string.replace(/</g, "&lt;");
    string = string.replace(/>/g, "&gt;");
    string = string.replace(/\?/g, "&quest;");
    string = string.replace(/\+/g, "&plus;");
    string = string.replace(/\#/g, "&num;");
    string = string.replace(/\$/g, "&dollar;");
    string = string.replace(/%/g, "&percnt;");
    string = string.replace(/\*/g, "&ast;");
    return string;
}

function adminPanelTemplate() {
    document.cookie = "userMode=" + 1;
    window.location.href = "adminUser.html";
}
function userPanelTemplate() {
    document.cookie = "userMode=" + 2;
    window.location.href = "dashboard.html";
}
//added by manohar
function accountPanelTemplate() {       
    document.cookie = "userMode=" + 3;
    window.location.href = "Dashboard_accounts.html";  
}
function userFeedbackTemplate() {

    var userFeedbackTemplateHtml = '';
    userFeedbackTemplateHtml += '<div class="modal-dialog">';
    userFeedbackTemplateHtml += '<div class="modal-content">';
    userFeedbackTemplateHtml += '<div class="modal-header">';
    userFeedbackTemplateHtml += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
    userFeedbackTemplateHtml += '<h4 class="modal-title">Feedback</h4>';
    userFeedbackTemplateHtml += '</div>';
    userFeedbackTemplateHtml += '<form action="#" class="form-horizontal form-row-seperated">';
    userFeedbackTemplateHtml += '<div class="modal-body">';
    userFeedbackTemplateHtml += '<div class="scroller2" data-always-visible="1" data-rail-visible1="1">';
    userFeedbackTemplateHtml += '<div class="form-body">';
    userFeedbackTemplateHtml += '<div class="row">';
    userFeedbackTemplateHtml += '<div class="col-md-12 form-group">';
    userFeedbackTemplateHtml += '<label class="col-md-3 control-label">Section</label>';
    userFeedbackTemplateHtml += '<div class="col-md-9">';
    userFeedbackTemplateHtml += '<select class="form-control" id="id_section">';
    if (userMode == 2) {
        userFeedbackTemplateHtml += '<option>Dashboard</option>';
        userFeedbackTemplateHtml += '<option>My Team</option>';
        userFeedbackTemplateHtml += '<option>Customers</option>';
        userFeedbackTemplateHtml += '<option>Messages</option>';
        userFeedbackTemplateHtml += '<option>Forms</option>';
        userFeedbackTemplateHtml += '<option>Reports</option>';

    } else if (userMode == 1) {
        userFeedbackTemplateHtml += '<option>Users</option>';
        userFeedbackTemplateHtml += '<option>Reporting Structure</option>';
        userFeedbackTemplateHtml += '<option>Customers</option>';
        userFeedbackTemplateHtml += '<option>Master Data</option>';
        userFeedbackTemplateHtml += '<option>Forms</option>';
        userFeedbackTemplateHtml += '<option>Reports</option>';

    } else if (userMode == 3) {
        userFeedbackTemplateHtml += '<option>Dashboard</option>';
        userFeedbackTemplateHtml += '<option>Expenses</option>';
    }

    userFeedbackTemplateHtml += '</select>';
    userFeedbackTemplateHtml += '</div>';
    userFeedbackTemplateHtml += '</div>';
    userFeedbackTemplateHtml += '</div>';
    userFeedbackTemplateHtml += '<div class="row">';
    userFeedbackTemplateHtml += '<div class="col-md-12 form-group">';
    userFeedbackTemplateHtml += '<label class="col-md-3 control-label">Feedback</label>';
    userFeedbackTemplateHtml += '<div class="col-md-9">';
    userFeedbackTemplateHtml += '<textarea class="form-control" rows="5" placeholder="Enter here" id="id_description"></textarea>';
    userFeedbackTemplateHtml += '</div>';
    userFeedbackTemplateHtml += '</div>';
    userFeedbackTemplateHtml += '</div>';
    userFeedbackTemplateHtml += '</div>';
    userFeedbackTemplateHtml += '</div>';
    userFeedbackTemplateHtml += '</div>';
    userFeedbackTemplateHtml += '<div class="modal-footer">';
    userFeedbackTemplateHtml += '<button type="button" class="btn btn-info" tabindex="17" onClick="sendUserFeedback();">Send</button>';
    userFeedbackTemplateHtml += '<button type="button" data-dismiss="modal" class="btn btn-default" tabindex="18">Cancel</button>';
    userFeedbackTemplateHtml += '</div>';
    userFeedbackTemplateHtml += '</form>';
    userFeedbackTemplateHtml += '</div>';
    userFeedbackTemplateHtml += '</div>';
    $('.cls_userfeedback').html(userFeedbackTemplateHtml);
}
function sendUserFeedback() {
    var section = $("#id_section option:selected").text().trim();
    if (userMode == 2) {
        section = "User Panel " + section;
    } else if (userMode == 1) {
        section = "Admin Panel " + section;
    } else if (userMode == 3) {
        section = "Account Panel " + section;
    }
    var feedback = $('#id_description').val().trim();
    if (feedback.length == 0) {
        fieldSenseTosterError("Feedback shouldn't be empty .", true);
        return false;
    }
    var feedbackTime = new Date().toString();
    var feedbackObject = {
        "section": section,
        "feedback": feedback,
        "feedbackTime": feedbackTime
    }
    var jsonData = JSON.stringify(feedbackObject);

    $.ajax({
        type: "POST",
        url: fieldSenseURL + "/feedback",
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
                fieldSenseTosterSuccess("Feedback sent successfully .", true);
                $(".cls_userfeedback").modal('hide');
            } else {
                fieldSenseTosterError(data.errorMessage, true);
            }
        }
    });

}

function custFormNames() {
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/adminCustomForms/all", // change url
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: false,
        success: function (data) {
            if (data.errorCode == '0000') {
                var formList = data.result;
                var templ = "";
                for (i = 0; i < formList.length; i++)
                {
                    templ += '<li id="leftnav_form_' + formList[i].id + '" class="leftnav_form"><a class="showSingle" onclick="loadCustomFormReportPage(' + formList[i].id + ')"> <span class="barrow"></span>';
                    templ += '<span class="membername"  title="' + formList[i].formName + '">' + formList[i].formName + '</span></a></li>';
                }
                $("#custFrmNameReports").html(templ);
            }
        }
    });
}
//Addded by siddhesh for search in super user.11-07-17.# 
function saveSearchText() {
    var searchTextSuperUser = $("#search_text").val();
    $.cookie("searchText", searchTextSuperUser);     //Using cookie for sending data from one page to another.
}
