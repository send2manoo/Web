/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * @author Ramesh
 * @date 10-07-2014
 */ 
var fieldSenseURL = '../FieldSense';
//var fieldSenseURL="http://api.old.fieldsense.in";
var fieldSenseWEBURL = '../FieldSenseWeb';
//var fieldSenseWEBURL='http://web.old.fieldsense.in';
function fieldSenseTosterError(message, isClosable) {
    toastr.options = {
        "closeButton": isClosable,
        "debug": false,
        "positionClass": "toast-top-center",
        "onclick": null,
        "showDuration": "1000",
        "hideDuration": "1000",
        "timeOut": "5000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    }
    toastr.error(message);
}
function fieldSenseTosterSuccess(message, isClosable) {
    toastr.options = {
        "closeButton": isClosable,
        "debug": false,
        "positionClass": "toast-top-center",
        "onclick": null,
        "showDuration": "1000",
        "hideDuration": "1000",
        "timeOut": "5000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    }
    toastr.success(message);
}
function fieldSenseTosterInfo(message, isClosable) {
    toastr.options = {
        "closeButton": isClosable,
        "debug": false,
        "positionClass": "toast-top-center",
        "onclick": null,
        "showDuration": "1000",
        "hideDuration": "1000",
        "timeOut": "5000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    }
    toastr.info(message);
}
function fieldSenseTosterWarning(message, isClosable) {
    toastr.options = {
        "closeButton": isClosable,
        "debug": false,
        "positionClass": "toast-top-center",
        "onclick": null,
        "showDuration": "1000",
        "hideDuration": "1000",
        "timeOut": "5000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    }
    toastr.warning(message);
}
function fieldSenseGetCookie(c_name)
{
    try {
        var i, x, y, ARRcookies = document.cookie.split(";");
        for (i = 0; i < ARRcookies.length; i++)
        {
            x = ARRcookies[i].substr(0, ARRcookies[i].indexOf("="));
            y = ARRcookies[i].substr(ARRcookies[i].indexOf("=") + 1);
            x = x.replace(/^\s+|\s+$/g, "");
           // alert("XXX"+x);
            if (x == c_name)
            {
               // alert("XXXunescape(y)"+unescape(y));
                return unescape(y);
            }
        }
    } catch (err) {
    //        return null;
    }
}
function deleteCookie(name) {
    document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}
function FieldSenseInvalidToken(message) {
    if (message.trim() === 'Invalid token . Please try again .') {
        deleteCookie("userToken");
        deleteCookie("role");
        window.location.href = "login.html";
    }
}
function fieldSenseTosterErrorNoTimeOut(message, isClosable) {
    toastr.options = {
        "closeButton": isClosable,
        "debug": false,
        "positionClass": "toast-top-right",
        "onclick": null,
        "showDuration": "1000",
        "hideDuration": "1000",
        "timeOut": "none",
        "extendedTimeOut": "0",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    }
    toastr.error(message);
}
function fieldSenseTosterSuccessNoTimeOut(message, isClosable) {
    toastr.options = {
        "closeButton": isClosable,
        "debug": false,
        "positionClass": "toast-top-right",
        "onclick": null,
        "showDuration": "1000",
        "hideDuration": "1000",
        "timeOut": "none",
        "extendedTimeOut": "0",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    }
    toastr.success(message);
}
function punchInOutTimeFormate(punchDateTime) {
    var year = punchDateTime.getFullYear();
    var month = punchDateTime.getMonth();
    var date = punchDateTime.getDate();
    var hr = punchDateTime.getHours();
    var min = punchDateTime.getMinutes();
    var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    //    var m = monthNames[month - 1];
    var m = monthNames[month];
    var time;
    var hour;
    var fullDate;
    if (min < 10) {
        min = '0' + min;
    }
    if (hr == 0) {
        hour = 12;
        time = hour + ':' + min + ' am';
    }
    else if (hr < 12) {
        hour = hr;
        time = hour + ':' + min + ' am';
    } else if (hr == 12) {
        hour = hr;
        time = hour + ':' + min + ' pm';
    } else {
        hour = hr - 12;
        time = hour + ':' + min + ' pm';
    }
    if (date == undefined) {
        fullDate = '';
    } else {
        fullDate = date + ' ' + m + ' ' + year + ', ' + time;
    }
    return fullDate;
}
function isDateIsTodaysDate(date){
    var today=new Date();
    if(date.getFullYear()==today.getFullYear()){
        if(date.getMonth()==today.getMonth()){
            if(date.getDate()==today.getDate()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }else{
        return false;
    }
}
function fieldSenseTosterSuccessForUserRoute(message, isClosable,specificUserId,specificUserName,specificUserTeamId) {
    toastr.options = {
        "closeButton": isClosable,
        "debug": false,
        "positionClass": "toast-top-center",
        "showDuration": "1000",
        "hideDuration": "1000",
        "timeOut": "none",
        "extendedTimeOut": "0",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    }
    toastr.options.onclick = function () {
        userTopLevelHierarchy(loginUserName, loginUserId,specificUserId,specificUserName,specificUserTeamId);
    };
    toastr.info(message);
    $('.toast-close-button').addClass("cls_close_route");
    var classname = document.getElementsByClassName("cls_close_route");
    var myFunction = function() {
        userTopLevelHierarchy(loginUserName, loginUserId,specificUserId,specificUserName,specificUserTeamId);
    };
    for(var i=0;i<classname.length;i++){
        classname[i].addEventListener('click', myFunction, false);
    }
}
