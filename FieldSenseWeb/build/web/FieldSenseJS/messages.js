/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * @autor Ramesh
 * @date 10-05-2014
 */
var firstmessageId = 0;
var previousOrNextMesg = 1;
var teamIdMessages = 0;
var userIdMessage = 0;
var userMessagedMessageIds = new Array();
var messageCounter = 0;
var messageintervalVariable;
var id_member_active = 0;
var teamIdLoggedInUser = 0;
var user = [];
var isActiveUser = 0;
var recentSenders = [];
var intervalMessages = window.setInterval(function () {
    var userToken2 = fieldSenseGetCookie("userToken");
    //    var specificUserId = fieldSenseGetCookie("specificUserId");
    //    var specificUserName = fieldSenseGetCookie("specificUserName");
    try {
        if (userToken2.toString() == "undefined") {
            window.location.href = "login.html";
        }
        else if (loginUserId != 0) {
            loggedinUserImageData();
            loggedinUserData();
            recentMessageSenders();
            allOrganizationUsers();
            //            try {
            //                if (specificUserId.toString() == "undefined") {
            //                    userTopLevelHierarchy(loginUserName, loginUserId, 0, 0);
            //                }
            //                else {
            //                    userTopLevelHierarchy(loginUserName, loginUserId, specificUserId, specificUserName);
            //                    deleteCookie("specificUserId");
            //                    deleteCookie("specificUserName");
            //                }
            //            } catch (exception) {
            //                userTopLevelHierarchy(loginUserName, loginUserId, 0, 0);
            //            }
            window.clearTimeout(intervalMessages);
        }
    }
    catch (err) {
        window.location.href = "login.html";
    }
}, 1000);
/*function userTopLevelHierarchy(hirarchyName, hirarchyId, specificUserId, specificUserName) {
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
 var hierarchyData = data.result;
 var memberId = 0;
 var tId = 0;
 var userTopLevelHierarchyHtml = '';
 userTopLevelHierarchyHtml += '<ul class="page-sidebar-menu">';
 userTopLevelHierarchyHtml += '<li class="" id="id_member' + memberId + '">';
 userTopLevelHierarchyHtml += '<a class="showSingle" target="2" onClick="recentMessageShowTemplate(\'' + tId + '\');">';
 userTopLevelHierarchyHtml += '<span class="membername">Recent Messages</span></a></li>';
 userTopLevelHierarchyHtml += '<li class="start active">';
 userTopLevelHierarchyHtml += '<span class="membername">MY TEAM</span>';
 userTopLevelHierarchyHtml += '<ul class="sub-menu">';
 for (var i = 0; i < hierarchyData.length; i++) {
 memberId = hierarchyData[i].user.id;
 var teamId = hierarchyData[i].teamId;
 if (memberId == hirarchyId) {
 tId = teamId;
 teamIdLoggedInUser = teamId;
 }
 if (memberId != hirarchyId) {
 var userName = hierarchyData[i].user.firstName + " " + hierarchyData[i].user.lastName
 var isUserOnline = hierarchyData[i].isOnline;
 var canTakeCalls = hierarchyData[i].canTakeCalls;
 var inMeeting = hierarchyData[i].inMeeting;
 var hasSubordinate = hierarchyData[i].hasSubordinate;
 var cls_usr_status_in_meet = 'online';
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
 userTopLevelHierarchyHtml += '<a class="showSingle" target="2" onClick="userLevelHirarchy(\'' + userName + '\',\'' + memberId + '\');messageShowTemplate(\'' + teamId + '\',\'' + memberId + '\');" id="id_usr_alt' + memberId + '">';
 if (hasSubordinate) {
 userTopLevelHierarchyHtml += '<span class="arrow" id="id_memberSelection' + memberId + '"></span>';
 } else {
 userTopLevelHierarchyHtml += '<span class="barrow" id="id_memberSelection' + memberId + '"></span>';
 }
 userTopLevelHierarchyHtml += '<span class="membername">' + userName + '</span>';
 userTopLevelHierarchyHtml += '<span id="id_user_status' + memberId + '">';
 userTopLevelHierarchyHtml += '<span class="' + cls_usr_status_can_take_calls + '"></span>';
 if (inMeeting) {
 userTopLevelHierarchyHtml += '<span class="' + cls_usr_status_in_meet + '"></span>';
 } else {
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
 $('#id_leftaside').html(userTopLevelHierarchyHtml);
 recentMessageShowTemplate(tId);
 var userRecentMessagesHtml = '<a href="javascript:;" alt="Recent Messages" onClick="recentMessageShowTemplate(\'' + tId + '\');">';
 userRecentMessagesHtml += '<span>';
 userRecentMessagesHtml += '<i class="fa fa-comments"></i>';
 userRecentMessagesHtml += '</span>';
 userRecentMessagesHtml += '<span>';
 userRecentMessagesHtml += ' Recent Messages';
 userRecentMessagesHtml += '</span></a>';
 $('#id_member0').html(userRecentMessagesHtml);
 userLevelHirarchy(specificUserName, specificUserId);
 $("#id_memberOpen" + specificUserId).addClass("open");
 $("#id_memberSelection" + specificUserId).addClass("open");
 $("#id_Hierachy_" + specificUserId).css("display", "block");
 }
 });
 }
 function userLevelHirarchy(hierarchyName, hierarchyId) {
 if (!$('#id_memberSelection' + hierarchyId).hasClass('open')) {
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
 var memberId = hierarchyData[i].user.id;
 var userTime = hierarchyData[i].user.lastKnownLocationTime;
 var hasSubordinate = hierarchyData[i].hasSubordinate;
 if (memberId != hierarchyId) {
 var cls_usr_status_in_meet = 'online';
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
 userLevelHirarchyHtml += '<a class="showSingle" target="2" onClick="userLevelHirarchy(\'' + userName + '\',\'' + memberId + '\'); messageShowTemplate(\'' + teamId + '\',\'' + memberId + '\');">';
 //                        userLevelHirarchyHtml += '<span class="arrow" id="id_memberSelection' + memberId + '"></span>';
 if (hasSubordinate) {
 userLevelHirarchyHtml += '<span class="arrow" id="id_memberSelection' + memberId + '"></span>';
 } else {
 userLevelHirarchyHtml += '<span class="barrow" id="id_memberSelection' + memberId + '"></span>';
 }
 userLevelHirarchyHtml += '<span class="membername">' + userName + '</span>';
 userLevelHirarchyHtml += '<span id="id_user_status' + memberId + '">';
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
 }
 }
 });
 }
 }
 */
function messageShowTemplate(teamId, userId, inMessage, memberName) {
    $('#messagetmt').html("");
    $('#id_textSelect').html("");
    if ($('#isMemberActive_' + isActiveUser).hasClass("active")) {
        $('#isMemberActive_' + isActiveUser).removeClass("active");
    }
    if (inMessage == "all") {
        $('#isMemberActive_' + userId).addClass("active");
    }
    if ($('#isMemberActiveRecent_' + isActiveUser).hasClass("active")) {
        $('#isMemberActiveRecent_' + isActiveUser).removeClass("active");
    }
    if (inMessage == "recent") {
        $('#isMemberActiveRecent_' + userId).addClass("active");
    }
    isActiveUser = userId;
    window.clearInterval(messageintervalVariable);
    firstmessageId = 0;
    teamIdMessages = teamId;
    userIdMessage = userId;
    //    $('#id_memberOpen' + id_member_active).removeClass("active");
    //    id_member_active = userId;
    //    $('#id_memberOpen' + userId).addClass("active");
    //    $('#id_rcntmsg').html("Messages")
    var url = fieldSenseURL + "/message/team/" + teamId + "/user/" + userId + "/" + firstmessageId + "/" + previousOrNextMesg;
    $.ajax({
        type: "GET",
        url: url,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            var messageShowTemplateHtml = '';
            if (data.errorCode == '0000') {
                var messageData = data.result;
                var cont = $('#chats');
                var list = $('.chats', cont);
                var form = $('.chat-form', cont);
                var input = $('input', form);
                var btn = $('.btn', form);
                for (var i = 0; i < messageData.length; i++) {
                    var message = messageData[i].message;
                    var messageId = messageData[i].id;
                    var messageSenderName = messageData[i].sender.firstName + " " + messageData[i].sender.lastName;
                    var messageSenderId = messageData[i].sender.id;
                    var messageSentTime = messageData[i].createdOn;
                    var isRead = messageData[i].isRead;
                    if (firstmessageId < messageId) {
                        firstmessageId = messageId
                    }
                    messageSentTime = fieldSenseseMesageTimeFormateForJsonDate(messageSentTime);
                    messageShowTemplateHtml += '<li class="out" id="id_messageVisible_' + messageId + '">';
                    time = new Date();
                    messageShowTemplateHtml += '<img class="avatar img-responsive" alt="" src="' + imageURLPath + accountId + '_' + messageSenderId + '_48X48.png?' + time + '" onerror="this.src=\'assets/img/usrsml_29.png\';"/>';
                    messageShowTemplateHtml += '<div class="message">';
                    messageShowTemplateHtml += '<span class="arrow">';
                    messageShowTemplateHtml += '</span>';
                    messageShowTemplateHtml += '<a href="#" class="name">' + messageSenderName + '</a>';
                    messageShowTemplateHtml += '<span class="datetime"> ' + messageSentTime;
                    messageShowTemplateHtml += '</span>';
                    messageShowTemplateHtml += '<span class="body">' + message;
                    messageShowTemplateHtml += '</span>';
                    messageShowTemplateHtml += '</div>';
                    messageShowTemplateHtml += '</li>';
                }
                $('#messagetmt').html(messageShowTemplateHtml);
                isMessageVisible(userId, 0);
                $('.scroller', cont).slimScroll({
                    scrollTo: list.height()
                });
            }
        }
    });
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/team/isMemberInHierarchy/" + loginUserId + "/" + userId + "",
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
                var isMemberInHierarchy = data.result;
                if (isMemberInHierarchy) {
                    userActivitiesTemplate(teamId, userId, memberName);
                } else {
                    $('.cls_usractivities').html('');
                }
            }
        }
    });
    sendMessageTemplate(userId, teamId);
    messageintervalVariable = window.setInterval(function () {
        messagesRealTime();
    }, 5000);
}

function messageSendTemplate(userId) {
    var messageSendTemplateBtn = '';
    messageSendTemplateBtn += '<a class="btn btn-primary icn-only" href="" onClick="messageSendTemplate(' + userId + ');return false;" disabled>';
    messageSendTemplateBtn += '<i class="fa fa-check"/>';
    messageSendTemplateBtn += '</a>';
    $('#id_sendMessagebtn').html(messageSendTemplateBtn);
    var message = document.getElementById("id_message_data").value;
    if (message.trim().length == 0) {
        fieldSenseTosterError("Message cannot be empty .", true);
        messageSendTemplateBtn = '<a class="btn btn-primary icn-only" href="" onClick="messageSendTemplate(' + userId + ');return false;">';
        messageSendTemplateBtn += '<i class="fa fa-check"/>';
        messageSendTemplateBtn += '</a>';
        $('#id_sendMessagebtn').html(messageSendTemplateBtn);
        return false;
    }
    if (message.trim().length > 1000) {
        fieldSenseTosterError("Message should not be more than 1000 characters .", true);
        messageSendTemplateBtn = '<a class="btn btn-primary icn-only" href="" onClick="messageSendTemplate(' + userId + ');return false;">';
        messageSendTemplateBtn += '<i class="fa fa-check"/>';
        messageSendTemplateBtn += '</a>';
        $('#id_sendMessagebtn').html(messageSendTemplateBtn);
        return false;
    }
    var messageSentTime = fieldSenseseMesageTimeFormateForJSDate(new Date());
    var reciverId = userId;
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
                $("li.cls_recent_" + userId).parent().prepend($("li.cls_recent_" + userId));
                var msgdata = data.result;
                var messageId = msgdata.id;
                if (firstmessageId < messageId) {
                    firstmessageId = messageId
                }
                var receiverId = msgdata.receiver.id;
                var receiverName = msgdata.receiver.firstName + ' ' + msgdata.receiver.lastName;
                var cont = $('#chats');
                var list = $('.chats', cont);
                var form = $('.chat-form', cont);
                var input = $('input', form);
                var btn = $('.btn', form);
                var messageShowTemplateHtml = '';
                messageShowTemplateHtml += '<li class="out" id="id_messageVisible_' + messageId + '">';
                time = new Date();
                messageShowTemplateHtml += '<img class="avatar img-responsive" alt="" src="' + imageURLPath + accountId + '_' + loginUserId + '_48X48.png?' + time + '" onerror="this.src=\'assets/img/usrsml_29.png\';"/>';
                messageShowTemplateHtml += '<div class="message">';
                messageShowTemplateHtml += '<span class="arrow">';
                messageShowTemplateHtml += '</span>';
                messageShowTemplateHtml += '<a href="#" class="name">' + loginUserName + '</a>';
                messageShowTemplateHtml += '<span class="datetime"> ' + messageSentTime;
                messageShowTemplateHtml += '</span>';
                messageShowTemplateHtml += '<span class="body">' + message;
                messageShowTemplateHtml += '</span>';
                messageShowTemplateHtml += '</div>';
                messageShowTemplateHtml += '</li>';
                var msg = list.append(messageShowTemplateHtml);
                input.val("");
                var count = 0;
                for (var i = 0; i < recentSenders.length; i++) {
                    if (recentSenders[i] == receiverId) {
                        count++;
                        break;
                    }
                }
                if (count == 0) {
                    recentSenders.push(receiverId);
                    recentMessageSenders();
                }
                $('.scroller', cont).slimScroll({
                    scrollTo: list.height()
                });
            } else {
                FieldSenseInvalidToken(data.errorMessage);
            }
            messageSendTemplateBtn = '<a class="btn btn-primary icn-only" href="" onClick="messageSendTemplate(' + userId + ');return false;">';
            messageSendTemplateBtn += '<i class="fa fa-check"/>';
            messageSendTemplateBtn += '</a>';
            $('#id_sendMessagebtn').html(messageSendTemplateBtn);
        }
    });
}
function messagesRealTime() {
    var url;
    var firstId = firstmessageId;
    url = fieldSenseURL + "/message/team/" + teamIdMessages + "/user/" + userIdMessage + "/" + firstmessageId + "/" + previousOrNextMesg;
    $.ajax({
        type: "GET",
        url: url,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            var messageShowTemplateHtml = '';
            if (data.errorCode == '0000') {
                var messageData = data.result;
                for (var i = 0; i < messageData.length; i++) {
                    var messageId = messageData[i].id;
                    var message = messageData[i].message;
                    var messageSenderName = messageData[i].sender.firstName + " " + messageData[i].sender.lastName;
                    var messageSenderId = messageData[i].sender.id;
                    var messageSentTime = messageData[i].createdOn;
                    var isRead = messageData[i].isRead;
                    if (firstmessageId < messageId) {
                        firstmessageId = messageId
                    }
                    messageSentTime = fieldSenseseMesageTimeFormateForJsonDate(messageSentTime);
                    messageShowTemplateHtml += '<li class="out" id="id_messageVisible_' + messageId + '">';
                    messageShowTemplateHtml += '<img class="avatar img-responsive" alt="" src="' + imageURLPath + accountId + '_' + messageSenderId + '_48X48.png" onerror="this.src=\'assets/img/usrsml_29.png\';"/>';
                    messageShowTemplateHtml += '<div class="message">';
                    messageShowTemplateHtml += '<span class="arrow">';
                    messageShowTemplateHtml += '</span>';
                    messageShowTemplateHtml += '<a href="#" class="name">' + messageSenderName + '</a>';
                    messageShowTemplateHtml += '<span class="datetime"> ' + messageSentTime;
                    messageShowTemplateHtml += '</span>';
                    messageShowTemplateHtml += '<span class="body">' + message;
                    messageShowTemplateHtml += '</span>';
                    messageShowTemplateHtml += '</div>';
                    messageShowTemplateHtml += '</li>';
                }
                $('#messagetmt').append(messageShowTemplateHtml);
                isMessageVisible(userIdMessage, firstId - 10);
            }
        }
    });
}
/*function recentMessageShowTemplate(teamId) {
 $('#messagetmt').html("");
 $('#id_ShowSendBox').css("display", "none");
 window.clearInterval(messageintervalVariable);
 firstmessageId = 0;
 $('#id_memberOpen' + id_member_active).removeClass("active");
 id_member_active = 0;
 $('#id_memberOpen0').addClass("active");
 $('#id_rcntmsg').html("Recent Messages")
 $.ajax({
 type: "GET",
 url: fieldSenseURL + '/message/team/' + teamId,
 contentType: "application/json; charset=utf-8",
 headers: {
 "userToken": userToken
 },
 crossDomain: false,
 cache: false,
 dataType: 'json',
 asyn: true,
 success: function (data) {
 var messageShowTemplateHtml = '';
 if (data.errorCode == '0000') {
 var messageData = data.result;
 var cont = $('#chats');
 var list = $('.chats', cont);
 var form = $('.chat-form', cont);
 var input = $('input', form);
 var btn = $('.btn', form);
 for (var i = 0; i < messageData.length; i++) {
 var message = messageData[i].message;
 var messageId = messageData[i].id;
 var messageSenderName = messageData[i].sender.firstName + " " + messageData[i].sender.lastName;
 var messageSenderId = messageData[i].sender.id;
 var messageSentTime = messageData[i].createdOn;
 if (firstmessageId < messageId) {
 firstmessageId = messageId
 }
 messageSentTime = fieldSenseseMesageTimeFormateForJsonDate(messageSentTime);
 messageShowTemplateHtml += '<li class="out">';
 time = new Date();
 messageShowTemplateHtml += '<img class="avatar img-responsive" alt="" src="' + imageURLPath + accountId + '_' + messageSenderId + '_48X48.png?' + time + '" onerror="this.src=\'assets/img/usrsml_29.png\';"/>';
 messageShowTemplateHtml += '<div class="message">';
 messageShowTemplateHtml += '<span class="arrow">';
 messageShowTemplateHtml += '</span>';
 messageShowTemplateHtml += '<a href="#" class="name" onclick="messageShowTemplate(\'' + teamId + '\',\'' + messageSenderId + '\')" >' + messageSenderName + '</a>';
 messageShowTemplateHtml += '<span class="datetime"> ' + messageSentTime;
 messageShowTemplateHtml += '</span>';
 messageShowTemplateHtml += '<span class="body">' + message;
 messageShowTemplateHtml += '</span>';
 messageShowTemplateHtml += '</div>';
 messageShowTemplateHtml += '</li>';
 }
 $('#messagetmt').html(messageShowTemplateHtml);
 $('.scroller', cont).slimScroll({
 scrollTo: list.height()
 });
 }
 }
 });
 }*/
function sendMessageTemplate(userId, teamId) {
    var sendMessageTemplate = '';
    sendMessageTemplate += '<div class="chat-form" id="id_ShowSendBox">';
    sendMessageTemplate += '<div class="input-cont">';
    sendMessageTemplate += '<input class="form-control" id="id_message_data" placeholder="Type a message here..." type="text"/>';
    sendMessageTemplate += '</div>';
    sendMessageTemplate += '<div class="btn-cont">';
    sendMessageTemplate += '<span class="arrow"/>';
    sendMessageTemplate += '<span class="cls_messagesend" id="id_sendMessagebtn">';
    sendMessageTemplate += '<a class="btn btn-primary icn-only" href="" onClick="messageSendTemplate(' + userId + ');return false;">';
    sendMessageTemplate += '<i class="fa fa-check"/>';
    sendMessageTemplate += '</a>';
    sendMessageTemplate += '</span>';
    sendMessageTemplate += '</div>';
    sendMessageTemplate += '</div>';
    $('#id_sendMessageTemplate').html(sendMessageTemplate);
    var sendMessage = document.getElementById("id_message_data");
    sendMessage.addEventListener("keydown", function (e) {
        if (e.keyCode === 13) {
            messageSendTemplate(userId);
        }
    });
}
function isMessageVisible(senderId, firstId) {
    var messageIds = new Array();
    var url = fieldSenseURL + "/message/sender/" + senderId;
    $.ajax({
        type: "GET",
        url: url,
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
                var messageData = data.result;
                var j = 0;
                for (var i = 0; i < messageData.length; i++) {
                    var messageId = messageData[i].id;
                    var isRead = messageData[i].isRead;
                    if (isRead == false) {
                        var temp = $('#id_messageVisible_' + messageId + '').isOnScreen();
                        if (temp == true) {
                            messageIds[j++] = messageId;
                        }
                    }
                }
                if (messageIds.length > 0) {
                    var messageIdObject = {
                        "messageIds": messageIds
                    }
                    var jsonData = JSON.stringify(messageIdObject);
                    $.ajax({
                        type: "PUT",
                        url: fieldSenseURL + "/message",
                        contentType: "application/json; charset=utf-8",
                        headers: {
                            "userToken": userToken
                        },
                        crossDomain: false,
                        data: jsonData,
                        cache: false,
                        dataType: 'json',
                        success: function (data) {
                            var messageData = data.result;
                            if (data.errorCode == '0000') {
                            }
                        }
                    });
                }
            }
        }
    });
}
function allOrganizationUsers() {
    $('.cls_organizationUser').html('');
    var inMessage = "all";
    var teamId = 0;
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
        url: fieldSenseURL + "/team/organizationChart/" + dateOnly,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            var organizationChartMembers = data.result;
            var allFromOrganizationTemplate = '';
            allFromOrganizationTemplate += '<li class = "active">';
            allFromOrganizationTemplate += '<ul class = "sub-menu list">';
            for (var i = 0; i < organizationChartMembers.length; i++) {
                var userId = organizationChartMembers[i].user.id;
                if (userId != loginUserId) {
                    var userName = organizationChartMembers[i].user.firstName + " " + organizationChartMembers[i].user.lastName
                    var isUserOnline = organizationChartMembers[i].isOnline;
                    var canTakeCalls = organizationChartMembers[i].canTakeCalls;
                    var inMeeting = organizationChartMembers[i].inMeeting;
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
                    allFromOrganizationTemplate += '<li id="isMemberActive_' + userId + '">';
                    //                    allFromOrganizationTemplate += '<a class = "showSingle" target = "2" onclick="messageShowTemplate(\'' + teamId + '\',\'' + userId + '\',\'' + inMessage + '\');userActivitiesTemplate(\'' + teamId + '\',\'' + userId + '\',\'' + userName + '\')">';
                    allFromOrganizationTemplate += '<a class = "showSingle" target = "2" onclick="messageShowTemplate(\'' + teamId + '\',\'' + userId + '\',\'' + inMessage + '\',\'' + userName + '\');">';
                    allFromOrganizationTemplate += '<span class = "barrow"> </span>';
                    allFromOrganizationTemplate += '<span class = "membername name" >' + userName + '</span>';
                    allFromOrganizationTemplate += '<span class = "' + cls_usr_status_can_take_calls + '"> </span>';
                    if (inMeeting) {
                        allFromOrganizationTemplate += '<span class="' + cls_usr_status_in_meet + '"></span>';
                    } else {
                        allFromOrganizationTemplate += '<span class="' + cls_usr_status_online_offline + '"></span>';
                    }
                    allFromOrganizationTemplate += '</a>';
                    allFromOrganizationTemplate += '</li>';
                }
            }
            allFromOrganizationTemplate += '</ul>';
            allFromOrganizationTemplate += '</li>';
            $('.cls_organizationUser').append(allFromOrganizationTemplate);
            var options = {
                valueNames: ['name']
            };
            var userList = new List('test-list', options);
        }
    });
}

function recentMessageSenders() {
    $('.recentSender').html('');
    var inMessage = "recent";
    var teamId = 0;
    recentSenders = [];
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
        url: fieldSenseURL + '/message/recentSender/' + dateOnly,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            var recentMessagedTemplate = '';
            if (data.errorCode == '0000') {
                var senderData = data.result;
                recentMessagedTemplate += '<li class="active">';
                recentMessagedTemplate += '<ul class="sub-menu">';
                for (var i = 0; i < senderData.length; i++) {
                    var userId = senderData[i].user.id;
                    recentSenders.push(userId);
                    var userName = senderData[i].user.firstName + " " + senderData[i].user.lastName
                    var isUserOnline = senderData[i].isOnline;
                    var canTakeCalls = senderData[i].canTakeCalls;
                    var inMeeting = senderData[i].inMeeting;
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
                    recentMessagedTemplate += '<li class = "cls_recent_' + userId + '" id="isMemberActiveRecent_' + userId + '">';
                    //                    recentMessagedTemplate += '<a class = "showSingle" target = "2" onclick="messageShowTemplate(\'' + teamId + '\',\'' + userId + '\',\'' + inMessage + '\');userActivitiesTemplate(\'' + teamId + '\',\'' + userId + '\',\'' + userName + '\')"">';
                    recentMessagedTemplate += '<a class = "showSingle" target = "2" onclick="messageShowTemplate(\'' + teamId + '\',\'' + userId + '\',\'' + inMessage + '\',\'' + userName + '\');">';
                    recentMessagedTemplate += '<span class = "barrow" > </span>';
                    recentMessagedTemplate += '<span class = "membername">' + userName + '</span>';
                    recentMessagedTemplate += '<span class = "' + cls_usr_status_can_take_calls + '" > </span>';
                    if (inMeeting) {
                        recentMessagedTemplate += '<span class="' + cls_usr_status_in_meet + '"></span>';
                    } else {
                        recentMessagedTemplate += '<span class="' + cls_usr_status_online_offline + '"></span>';
                    }
                    recentMessagedTemplate += '</a>';
                    recentMessagedTemplate += '</li>';
                }
                recentMessagedTemplate += '</ul>';
                recentMessagedTemplate += '</li>';
                $('.recentSender').append(recentMessagedTemplate);
            }
        }
    });
}
function showExpenseTemplate(id) {
    var expenseDetails = id.split(globalSplit);
    var expenseId = expenseDetails[0];
    var expenseName = expenseDetails[1];
    var expenseTime = expenseDetails[2];
    var amountSpent = expenseDetails[3];
    var expenseStatus = expenseDetails[4];
    var customerName = expenseDetails[5];
    var userId = expenseDetails[6];
    var appointmentTitle = expenseDetails[7];
    expenseName = expenseName.replace(/my11c/g, "'");
    var showExpenseTemplateHtml = '';
    showExpenseTemplateHtml += '<div class="modal-dialog modal-wide">';
    showExpenseTemplateHtml += '<div class="modal-content">';
    showExpenseTemplateHtml += '<div class="modal-header">';
    showExpenseTemplateHtml += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
    showExpenseTemplateHtml += '<h4 class="modal-title">View Expenses</h4>';
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
    showExpenseTemplateHtml += '<button type="button" data-dismiss="modal" class="btn btn-default">Close</button>';
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
        url: fieldSenseURL + "/attendance/time/" + userId + "/" + dateOnly,
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
                    if (punchOutString != "00:00:00") {
                        var punchoutSplit = punchOutString.split(':');
                        var punchOutHr = punchoutSplit[0];
                        var punchOutMin = punchoutSplit[1];
                        var punchOutDateTime = convertServerDateToLocalDate(year, month, date, punchOutHr, punchOutMin);
                        fullDate = punchInOutTimeFormate(punchOutDateTime);
                    } else {
                        var slpitTimeString = timeString.split(':');
                        var hr = slpitTimeString[0];
                        var min = slpitTimeString[1];
                        var punchDateTime = convertServerDateToLocalDate(year, month, date, hr, min);
                        fullDate = punchInOutTimeFormate(punchDateTime);
                    }
                }
                var dateForSelection = new Date();
                var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
                var fromDateForExpense = '1 ' + monthNames[dateForSelection.getMonth()] + ' ' + dateForSelection.getFullYear();
                var toDateForExpense = dateForSelection.getDate() + ' ' + monthNames[dateForSelection.getMonth()] + ' ' + dateForSelection.getFullYear();
                dateForSelection = dateForSelection.getDate() + " " + monthNames[dateForSelection.getMonth()] + " " + dateForSelection.getFullYear();
                activityTemplate += '<div class="portlet" id="MSholder">';
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
                if (dateString == "") {
                    activityTemplate += 'Not punched in';
                } else if (punchOutString == "00:00:00") {
                    activityTemplate += 'Punched in : ' + fullDate + '';
                } else {
                    activityTemplate += 'Punched out : ' + fullDate + '';
                }
                activityTemplate += '</span>';
                activityTemplate += '</div>';
                activityTemplate += '</div>';
                activityTemplate += '<div class="portlet-body">';
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
                activityTemplate += '<label class="appr-label" id="id_amountSubmitedAndApproved">Submitted <span>&#8377;</span> 2,300 | Approved <span>&#8377;</span> 700</label>';
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
        }
    });
}
function activityShowTemplate(teamId, userId) {
    //    $('#pleaseWaitDialog').modal('show');
    var now = document.getElementById("id_activityForDate").value;
    if (now.length == 0) {
        var now1 = new Date();
        now = new Date(now1.getFullYear(), now1.getMonth(), now1.getDate());
    } else {
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
                        var activityDescription = activityData[i].description;
                        var customerLocation = activityData[i].customer.customerLocation
                        var activityOutCome = activityData[i].outcomes.id;
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
                            activityStatusClass = 'form-group stat_blnk';
                        }
                        else if (activityOutCome == 1) {
                            activityStatusClass = 'form-group success';
                        } else if (activityOutCome == 2) {
                            activityStatusClass = 'form-group failed';
                        }
                        activityShowTemplateHtml += '<div class="form-body">';
                        activityShowTemplateHtml += '<div class="' + activityStatusClass + '">';
                        activityShowTemplateHtml += '<label class="col-md-12 time-label">' + activityDateTime + '</label>';
                        activityShowTemplateHtml += '<label class="col-md-12 title-label"><a data-toggle="modal" href="#responsive" onClick="viewActivityTemplate(\'' + activityId + '\',\'' + teamId + '\',\'' + userId + '\')">' + activity + '</a></label>';
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
                for (var i = 0; i < userExpensese.length; i++) {
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
                    if (expenseStatus == 1) {
                        amountApproved = amountApproved + amountSpent;
                    }
                    var expenseStatusClass = '';
                    if (expenseStatus == 0) {
                        expenseStatusClass = 'form-group stat_blnk';
                    }
                    else if (expenseStatus == 1) {
                        expenseStatusClass = 'form-group success';
                    } else if (expenseStatus == 2) {
                        expenseStatusClass = 'form-group failed';
                    }

                    expenseShowTemplate += '<div class="form-body">';
                    expenseShowTemplate += '<div id="id_expenseStatus_' + expenseId + '" class="' + expenseStatusClass + '">';
                    expenseShowTemplate += '<label class="col-md-12 time-label">' + expenseTime + '</label>';
                    expenseShowTemplate += '<div class="col-md-12">';
                    expenseShowTemplate += '<label class="col-md-8 title-label">' + expenseName + '</label>';
                    expenseShowTemplate += '<label class="co<lal-md-4 amt-label"><span>&#8377;</span>' + amountSpent + '</label>';
                    expenseShowTemplate += '</div>';
                    expenseShowTemplate += '<div class="col-md-12" id="id_expensenmae' + expenseId + '">';
                    expenseShowTemplate += '<button type="button" data-toggle="modal" href="#expenses" class="btn btn-sm btn-info" onClick="showExpenseTemplate(\'' + id + '\');return false;">View</button>';
                    expenseShowTemplate += '</div>';
                    expenseShowTemplate += '</div>';
                    expenseShowTemplate += '</div>';
                }
                var id_amountSubmitedAndApprovedHtml = 'Submitted <span>&#8377;</span> ' + amount + ' | Approved <span>&#8377;</span> ' + amountApproved + '';
                $('#id_amountSubmitedAndApproved').html(id_amountSubmitedAndApprovedHtml);
            }
            $('#id_showuserExpenses').html(expenseShowTemplate)
            $('#pleaseWaitDialog').modal('hide');
        }
    });
}

/*function viewActivityTemplate(appointmentId, teamId, user) {
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
 var activityData = dataActivity.result;
 var customerId = activityData.customer.id;
 var activityTitle = activityData.title;
 var activityStatus = activityData.status;
 var activityDescription = activityData.description;
 var contactId = activityData.customerContact.id;
 var contactFnm = activityData.customerContact.firstName;
 var contactMnm = activityData.customerContact.middleName;
 var contactLnm = activityData.customerContact.lastName;
 var contactFullNm = contactFnm + ' ' + contactMnm + ' ' + contactLnm;
 var customerPrint = activityData.customer.customerPrintas;
 var dateTime = activityData.sdateTime;
 var endDateTime = activityData.sendTime;
 var purposeId = activityData.purpose.id;
 var purpose = activityData.purpose.purpose;
 var ownerId = activityData.owner.id;
 var ownerName = activityData.owner.firstName + ' ' + activityData.owner.lastName;
 var outcomeId = activityData.outcomes.id;
 var outcome = activityData.outcomes.outcome;
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
 var viewActivityTemplateHtml = '';
 viewActivityTemplateHtml += '<div class="modal-dialog modal-wide">';
 viewActivityTemplateHtml += '<div class="modal-content">';
 viewActivityTemplateHtml += '<div class="modal-header">';
 viewActivityTemplateHtml += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
 viewActivityTemplateHtml += '<h4 class="modal-title">View Visit</h4>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '<form action="#" class="form-horizontal form-row-seperated">';
 viewActivityTemplateHtml += '<div class="modal-body" id="act">';
 viewActivityTemplateHtml += '<div class="scroller2" data-always-visible="1" data-rail-visible1="1">';
 viewActivityTemplateHtml += '<div class="form-body">';
 viewActivityTemplateHtml += '<div class="row">';
 viewActivityTemplateHtml += '<div class="col-md-6 form-group">';
 viewActivityTemplateHtml += '<label class="col-md-5 control-label">Customer Name</label>';
 viewActivityTemplateHtml += '<div class="col-md-7">';
 viewActivityTemplateHtml += '<input type="hidden" id="id_hiddenEdit" value="' + customerId + '"/>';
 viewActivityTemplateHtml += '<input type="Cname" name="currency" class="form-control" id="autocompleteCnm" placeholder="Enter text" value="' + customerPrint + '">';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '<div class="col-md-6 form-group">';
 viewActivityTemplateHtml += '<label class="col-md-5 control-label">Contact</label>';
 viewActivityTemplateHtml += '<div class="col-md-7">';
 viewActivityTemplateHtml += '<select class="form-control" id="id_contactEdit">';
 viewActivityTemplateHtml += '<option value="' + contactId + '" selected>' + contactFullNm + '</option>';
 viewActivityTemplateHtml += '</select>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '<div class="row">';
 viewActivityTemplateHtml += '<div class="col-md-6 form-group">';
 viewActivityTemplateHtml += '<label class="col-md-5 control-label">Title</label>';
 viewActivityTemplateHtml += '<div class="col-md-7">';
 viewActivityTemplateHtml += '<input type="activity" class="form-control" placeholder="Enter text" id="id_activityEdit" value="' + activityTitle + '">';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '<div class="col-md-6 form-group">';
 viewActivityTemplateHtml += '<label class="col-md-5 control-label">Date</label>';
 viewActivityTemplateHtml += '<div class="col-md-7">';
 viewActivityTemplateHtml += '<div class="input-group date date-picker" data-date-format="dd-mm-yyyy" data-date-start-date="+0d">';
 viewActivityTemplateHtml += '<input type="text" class="form-control" id="id_dateEdit" readonly value="' + starttDate + '">';
 viewActivityTemplateHtml += '<span class="input-group-btn">';
 viewActivityTemplateHtml += '<button class="btn btn-default" type="button"><i class="fa fa-calendar"></i></button>';
 viewActivityTemplateHtml += '</span>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '<div class="row">';
 viewActivityTemplateHtml += '<div class="col-md-6 form-group">';
 viewActivityTemplateHtml += '<label class="col-md-5 control-label">Start Time</label>';
 viewActivityTemplateHtml += '<div class="col-md-3">';
 viewActivityTemplateHtml += '<select id="edit_Shr" class="form-control input-xssmall">';
 viewActivityTemplateHtml += '<option value="' + startHr + '" selected>' + startHr + '</option>';
 viewActivityTemplateHtml += '</select></div>';
 viewActivityTemplateHtml += '<div class="col-md-3">';
 viewActivityTemplateHtml += '<select id="edit_Smin" class="form-control input-xssmall">';
 viewActivityTemplateHtml += '<option value="' + startMin + '" selected>' + startMin + '</option>';
 viewActivityTemplateHtml += '</select>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '<div class="col-md-6 form-group">';
 viewActivityTemplateHtml += '<label class="col-md-5 control-label">End Time</label>';
 viewActivityTemplateHtml += '<div class="col-md-3">';
 viewActivityTemplateHtml += '<select id="edit_ehr" class="form-control input-xssmall">';
 viewActivityTemplateHtml += '<option value="' + endHr + '" selected>' + endHr + '</option>';
 viewActivityTemplateHtml += '</select></div>';
 viewActivityTemplateHtml += '<div class="col-md-3">';
 viewActivityTemplateHtml += '<select id="edit_emin" class="form-control input-xssmall">';
 viewActivityTemplateHtml += '<option value="' + endMin + '" selected>' + endMin + '</option>';
 viewActivityTemplateHtml += '</select>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '<div class="row">';
 viewActivityTemplateHtml += '<div class="col-md-6 form-group">';
 viewActivityTemplateHtml += '<label class="col-md-5 control-label">Purpose</label>';
 viewActivityTemplateHtml += '<div class="col-md-7">';
 viewActivityTemplateHtml += '<select class="form-control" id="id_purposeEdit">';
 viewActivityTemplateHtml += '<option value="' + purposeId + '" selected>' + purpose + '</option>';
 viewActivityTemplateHtml += '</select>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '<div class="row">';
 viewActivityTemplateHtml += '<div class="col-md-6 form-group">';
 viewActivityTemplateHtml += '<label class="col-md-5 control-label">Owner</label>';
 viewActivityTemplateHtml += '<div class="col-md-7">';
 viewActivityTemplateHtml += '<input type="Cname" name="currency" class="form-control cls_ownerEdit" id="' + ownerId + '" value="' + ownerName + '" disabled>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '<div class="col-md-6 form-group">';
 viewActivityTemplateHtml += '<label class="col-md-5 control-label">Assigned To</label>';
 viewActivityTemplateHtml += '<div class="col-md-7">';
 viewActivityTemplateHtml += '<select class="form-control" id="id_assignedEdit">';
 viewActivityTemplateHtml += '<option value="' + assignId + '" selected>' + assigneeName + '</option>';
 viewActivityTemplateHtml += '</select>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '<div class="row">';
 viewActivityTemplateHtml += '<div class="col-md-6 form-group">';
 viewActivityTemplateHtml += '<label class="col-md-5 control-label">Status</label>';
 viewActivityTemplateHtml += '<div class="col-md-7">';
 viewActivityTemplateHtml += '<select class="form-control" id="id_statusEdit" onchange="outcomeForAddActivity()">';
 if (activityStatus == 0) {
 viewActivityTemplateHtml += '<option value="0" selected>Pending</option>';
 }
 if (activityStatus == 1) {
 viewActivityTemplateHtml += '<option value="1" selected>In-Progress</option>';
 }
 if (activityStatus == 2) {
 viewActivityTemplateHtml += '<option value="2" selected>Completed</option>';
 }
 if (activityStatus == 3) {
 viewActivityTemplateHtml += '<option value="3" selected>Cancelled</option>';
 }
 viewActivityTemplateHtml += '</select>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '<div class="col-md-6 form-group">';
 viewActivityTemplateHtml += '<label class="col-md-5 control-label">Outcome</label>';
 viewActivityTemplateHtml += '<div class="col-md-7">';
 viewActivityTemplateHtml += '<select class="form-control" id="id_outcomeEdit">';
 viewActivityTemplateHtml += '<option value="' + outcomeId + '" selected>' + outcome + '</option>';
 }
 viewActivityTemplateHtml += '</select>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '<div class="row">';
 viewActivityTemplateHtml += '<div class="form-group" id="dscp">';
 viewActivityTemplateHtml += '<label class="col-md-3 control-label">Description</label>';
 viewActivityTemplateHtml += '<div class="col-md-9">';
 viewActivityTemplateHtml += '<textarea class="form-control" rows="3" placeholder="Enter text" id="id_descriptionEdit">' + activityDescription + '</textarea>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '<div class="modal-footer">';
 viewActivityTemplateHtml += '<button type="button" data-dismiss="modal" class="btn btn-default">Close</button>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '</form>';
 viewActivityTemplateHtml += '</div>';
 viewActivityTemplateHtml += '</div>';
 $('.cls_addActivity').html(viewActivityTemplateHtml);
 if (!isAssigneeIsImmidiateSubOrdinate) {
 $('#id_assignedEdit').prop("disabled", true);
 }
 $('#pleaseWaitDialog').modal('hide');
 $('#autocompleteCnm').autocomplete({
 lookup: customer,
 onSelect: function (suggestion) {
 $('#id_hiddenEdit').val(suggestion.customerId);
 customerContacts(suggestion.customerId)
 }
 });
 if (jQuery().datepicker) {
 $('.date-picker').datepicker({
 rtl: App.isRTL(),
 autoclose: true
 });
 $('body').removeClass("modal-open"); // fix bug when inline picker is used in modal
 }
 //                                                                App.init();
 }
 });
 $('#pleaseWaitDialog').modal('hide');
 }*/




function viewActivityTemplate(appointmentId, teamId, user) {
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
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Customer Name</label>';
                                                                editActivityTemplateHtml += '<div class="col-md-7">';
                                                                editActivityTemplateHtml += '<input type="hidden" id="id_hiddenEdit" value="' + customerId + '"/>';
                                                                editActivityTemplateHtml += '<input type="Cname" name="currency" class="form-control" id="autocompleteCnm" placeholder="Enter text" value="' + customerName + '">';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '</div>';
                                                                editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                editActivityTemplateHtml += '<label class="col-md-5 control-label">Contact</label>';
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
                                                                editActivityTemplateHtml += '<div class="input-group date date-picker" data-date-format="dd-mm-yyyy" data-date-start-date="+0d">';
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
                                                                $('#pleaseWaitDialog').modal('hide');
                                                                $('#autocompleteCnm').autocomplete({
                                                                    lookup: customer,
                                                                    onSelect: function (suggestion) {
                                                                        $('#id_hiddenEdit').val(suggestion.customerId);
                                                                        customerContacts(suggestion.customerId)
                                                                    }
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
                                }
                            });
                        }
                    }
                });
            }
        }
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
    if (activity.trim().length == 0) {
        fieldSenseTosterError("Activity cannot be empty", true);
        return false;
    }
    var dates = document.getElementById("id_dateEdit").value;
    if (dates.trim().length == 0) {
        fieldSenseTosterError("Date cannot be empty", true);
        return false;
    }
    var startHr = document.getElementById("edit_Shr").value;
    if (startHr.trim() == 'HH') {
        fieldSenseTosterError("Start Time cannot be empty", true);
        return false;
    }
    var startMin = document.getElementById("edit_Smin").value;
    if (startMin.trim() == 'MM') {
        fieldSenseTosterError("Start Time cannot be empty", true);
        return false;
    }
    var endHr = document.getElementById("edit_ehr").value;
    var endMin = document.getElementById("edit_emin").value;
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
    if (endtHours.trim() == "HH:MM") {
        endDTime = adate;
    } else {
        endDTime = convertLocalDateToServerDate(dateSplit[2], dateSplit[1] - 1, dateSplit[0], endHr, endMin);
        endDTime = endDTime.getFullYear() + "-" + (endDTime.getMonth() + 1) + "-" + endDTime.getDate() + " " + endDTime.getHours() + ":" + endDTime.getMinutes() + ':00';
    }
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
        "outcomeDescription":outcomeDescription
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
                activityShowTemplate(teamId, userId)
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
function enterKeyRestrict(e) {
    if (e.keyCode === 13) {
        return false;
    }
}