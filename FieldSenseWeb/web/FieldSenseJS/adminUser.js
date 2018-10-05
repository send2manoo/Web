var leftside_userTerritoryList = new Array();
var selectedTerritories = new Array();
var offlineEnable;
//added by nikhil
var loginUserIdcookie = fieldSenseGetCookie("loginUserIdcookie");
var loginUserNamecookie = fieldSenseGetCookie("loginUserNamecookie");
var accountIdcookie = fieldSenseGetCookie("accountIdcookie");
var accountNamecookie = fieldSenseGetCookie("accountNamecookie");
var UserRolecookie = fieldSenseGetCookie("UserRolecookie");
//ended by nikhil
var intervalAdminUser = window.setInterval(function () {
    var userToken2 = fieldSenseGetCookie("userToken");
    try {
        if (userToken2.toString() == "undefined") {
            window.location.href = "login.html";
        } else if (loginUserId != 0) {       // modified by manohar
              if (role == 1 || role==6 || role==3 || role==8) {       // role : 0 -super user, 1 -admin, 2 -account person, 5 -on-field user 
                loggedinUserImageData();
                loggedinUserData();
                var loc = window.location;
                var pathName = loc.pathname.substring(loc.pathname.lastIndexOf('/') + 1).trim();
                if (pathName == "adminImportUser.html") {
                    downloadSampleUsersFile();
                } else {
                    //alert(" and "+pathName);
                    leftSideMenu();
                    showUser();
                    //allFieldSenseUsersDetails();
                    userDataForReportTo();
                }
            } else if (role == 0) {
                window.location.href = "stats.html";
            }
            else {
                window.location.href = "dashboard.html";
            }
            window.clearTimeout(intervalAdminUser);
        }
    }
    catch (err) {
        window.location.href = "login.html";
    }
}, 1000);
function showUser() {
    leftside_userTerritoryList = new Array();
    selectedTerritories = new Array();

    $("#id_activeSearch").attr('checked', false);
    $("#id_activeSearch").parent().removeClass('checked');
    $("#id_deactiveSearch").attr('checked', false);
    $("#id_deactiveSearch").parent().removeClass('checked');

    $("[id^=id_Territory_]").each(function () {
        $(this).attr('checked', false);
        $(this).parent().removeClass('checked');
    });
    $(".cls_clearTerritory").hide();
    $(".cls_clearStatus").hide();

    var searchText = $('#id_searchUser').val().trim();

    var start = 0;
    var length = 20;
    myTable = $("#sample_5").DataTable();
    if (myTable != null)
        myTable.destroy();
    //$('#pleaseWaitDialog').modal('show');
    $('#id_userShow').html('');
    var userTemplate = '';
    userTemplate += '<table class="table table-striped table-bordered table-hover" id="sample_5">';
    userTemplate += '<thead>';
    userTemplate += '<tr>';
    userTemplate += '<th>';
    userTemplate += 'Emp. Code';
    userTemplate += '</th>';
    userTemplate += '<th>';
    userTemplate += 'Name';
    userTemplate += '</th>';
    userTemplate += '<th>';
    userTemplate += 'Email';
    userTemplate += '</th>';
    userTemplate += '<th>';
    userTemplate += 'Mobile';
    userTemplate += '</th>';
    userTemplate += '<th class="hidden-xs">';
    userTemplate += 'Gender';
    userTemplate += '</th>';
    userTemplate += '<th>';
    userTemplate += 'Active';
    userTemplate += '</th>';
    userTemplate += '<th>';
    userTemplate += 'Attendance Status';
    userTemplate += '</th>';
    userTemplate += '<th>';
    userTemplate += 'DateTime';
    userTemplate += '</th>';
    userTemplate += '<th>';
    userTemplate += '</th>';
    userTemplate += '</tr>';
    userTemplate += '</thead>';
    userTemplate += '<tbody>';
    userTemplate += '</tbody>';
    userTemplate += '</table>';
    $('#id_userShow').html(userTemplate);
    var inboxtable = $('#sample_5').dataTable({
        "bServerSide": true,
        "bDestroy": true,
        "ajax": {
            "url": fieldSenseURL + "/user/details?searchText=" + searchText,
            "type": "GET",
            headers: {
                "userToken": userToken
            },
            contentType: "application/json; charset=utf-8",
            crossDomain: false,
            cache: false,
            dataType: 'json',
            async: true,
            error: function (data) {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.responseJSON.errorMessage, true);
                //logout();
            }
        },
        "iDisplayStart": start,
        // set the initial value
        "iDisplayLength": length, // It will be by default page width set by user
        "sPaginationType": "bootstrap_full_number",
        "bProcessing": false,
        "aLengthMenu": [
            [5, 10, 15, 20, 50, 100],
            [5, 10, 15, 20, 50, 100] // change per page values here
        ],
        "oLanguage": {
            // "sProcessing":"Loading",
            "sEmptyTable": "No data available in table",
            "sLengthMenu": "_MENU_ <span id ='id_totalCustRecords'></span>",
            //"sInfo": "Showing _START_ to _END_ of _TOTAL_ Mails",
            "oPaginate": {
                "sPrevious": "Prev",
                "sNext": "Next"
            }
        },
        "aoColumnDefs": [
            {"bSortable": false,
                "aTargets": [0, 3, 4, 5, 6, 7, 8]

            }
        ],
        "aoColumns": [// ahmed : here we populate the data in datatable that is returned using Ajax
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {                   
                    return full.emp_code;
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.firstName + ' ' + full.lastName;
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.emailAddress;
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.mobileNo;
                }
            },
            {"mData": null,
                "sClass": "hidden-xs",
                "mRender": function (data, type, full) {
                    var userGender = full.gender;
                    var gender;
                    if (userGender == 0) {
                        gender = "Female";
                    } else if (userGender == 1) {
                        gender = "Male";
                    }
                    return gender;
                }
            },
            {"mData": null,
                "sClass": "hidden-xs",
                "mRender": function (data, type, full) {
                    var userActive = full.active;
                    if (userActive == 0) {
                        return'<input type="checkbox" disabled >';
                    } else if (userActive == 1) {
                        return '<input type="checkbox" checked disabled >';
                    }
                }
            },
            {"mData": null,
                "sClass": "hidden-xs",
                "mRender": function (data, type, full) {
                    var ateendanceStatus = full.punchStatus;
                    if (ateendanceStatus == "NotPunchIn") {
                        return '';
                    } else if (ateendanceStatus == "PunchIn") {
                        return "Punched In";
                    } else if (ateendanceStatus == "PunchOut") {
                        return 'Punched Out   <button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Reset" onclick="resetAttendanceStatus(\'' + full.id + '\',\'' + full.attendanceId + '\')"><i class="fa fa-toggle-on"></i></button>';

                    }
                }
            },
            {"mData": null,
                "sClass": "hidden-xs",
                "mRender": function (data, type, full) {
                    var ateendanceStatusTime = full.punchStatusTime;
                    if (ateendanceStatusTime != "" && full.punchDate != "") {
                        var punchDate = full.punchDate.split('-');
                        var punchInSplit = ateendanceStatusTime.split(':');
                        var punchInDateJs = convertServerDateToLocalDate(punchDate[0], punchDate[1] - 1, punchDate[2], punchInSplit[0], punchInSplit[1]);
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
                        punchDate = d + ' ' + m + ' ' + y;
                        if (punchInMin < 10) {
                            punchInMin = '0' + punchInMin;
                        }
                        if (punchInHr < 12) {
                            if (punchInHr < 10) {
                                punchInHr = '0' + punchInHr;
                            }
                            ateendanceStatusTime = punchInHr + ':' + punchInMin + ' AM';
                        } else if (punchInHr == 12) {
                            ateendanceStatusTime = punchInHr + ':' + punchInMin + ' PM';
                        } else {
                            punchInHr = punchInHr - 12;
                            if (punchInHr < 10) {
                                punchInHr = '0' + punchInHr;
                            }
                            ateendanceStatusTime = punchInHr + ':' + punchInMin + ' PM';
                        }
                        var punchInMinuts = (punchInHr1 * 60) + punchInMin1;
                        ateendanceStatusTime = punchDate + " , " + ateendanceStatusTime;
                    }
                    return ateendanceStatusTime;
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    var userName = full.firstName + ' ' + full.lastName;
                    userName = userName.replace("'", globalSplit);
                    if (loginUserId != full.id) {                        
                        return '<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Edit" data-toggle="modal" href="#responsive2" onclick="editUserTemplate(' + full.id + ')"><i class="fa fa-edit"></i></button><button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Delete" onclick="deleteUser(\'' + full.id + '\',\'' + userName + '\')"><i class="fa fa-trash-o"></i></button>';
                    } else {
                        return '<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Edit" data-toggle="modal" href="#responsive2" onclick="editUserTemplate(' + full.id + ')"><i class="fa fa-edit"></i></button>';
                    }
                }
            }
        ],
        "aaSorting": [],
        "bFilter": false,
        "preDrawCallback": function (settings) {
            $('#pleaseWaitDialog').modal('show');
        },
        "fnDrawCallback": function (oSettings) {
            if (oSettings.fnDisplayEnd() > 0) {
                $("#id_totalCustRecords").html("<b>" + (oSettings._iDisplayStart + 1) + "</b> - <b>" + oSettings.fnDisplayEnd() + "</b> of <b>" + oSettings.fnRecordsTotal() + "</b> records");
            }
            inboxtable.$('td').mouseover(function () {
                var sData = $(this).text();
                $(this).attr('title', sData);
            });
            App.initUniform();
            App.fixContentHeight();
            jQuery('.tooltips').tooltip();
            $('#pleaseWaitDialog').modal('hide');
        },
    });

    jQuery('#sample_5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
    jQuery('#sample_5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
    jQuery('#sample_5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown	

    $('#id_searchUser').val("");
}


function addUser() {

    var ufnm = document.getElementById("id_ufnm").value.trim();
    if (ufnm.length == 0) {
        fieldSenseTosterError("First Name cannot be empty", true);
        return false;
    }
    if (ufnm.length > 50) {
        fieldSenseTosterError("First name can not be more than 50 characters", true);
        return false;
    }
    var ulnm = document.getElementById("id_ulnm").value.trim();
    if (ulnm.length != 0) {
        if (ulnm.trim().length > 50) {
            fieldSenseTosterError("Last name can not be more than 50 characters", true);
            return false;
        }
    }
    var designation = document.getElementById("id_designation").value.trim();
    if (designation.length == 0) {
        fieldSenseTosterError("Please enter designation ", true);
        return false;
    }
    if (designation.length > 50) {
        fieldSenseTosterError("Designation can not be more than 50 characters", true);
        return false;
    }
    var role = document.getElementById("id_role").options;
    var all = '';
    $('#id_role  > option:selected').each(function () {
        all += $(this).val();
    });
    if (all.length == 3)
    {
        var admin = all.substring(0, 1);
        var account = all.substring(1, 2);
        var onfield = all.substring(2, 3);
        role = 8;
    } else if (all.length == 2)
    {
        var role1 = all.substring(0, 1);
        var role2 = all.substring(1, 2);
        if (role1 == 1)
        {
            if (role1 == 1 && role2 == 2)
            {
                role = 3;
            } else if (role1 == 1 && role2 == 5)
            {
                role = 6;
            }
        } else
        {
            role = 7;
        }
    } else
    {
        role = all;
    }
    if (role == 0) {
        fieldSenseTosterError("Please select user role .", true);
        return false;
    }
    var reportTo = document.getElementById("id_ofAllSubordinate").value.trim();
    if (reportTo == 0) {
        fieldSenseTosterError("Please select Reporting head .", true);
        return false;
    }
    var uemail = document.getElementById("id_uemail").value.trim();
    if (uemail.length == 0) {
        fieldSenseTosterError("Email cannot be empty", true);
        return false;
    }
    if (uemail.length > 100) {
        fieldSenseTosterError("Email address can not be more than 100 characters", true);
        return false;
    }
    var emailPattern = /^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;
    if (!emailPattern.test(uemail.trim())) {
        fieldSenseTosterError("Invalid email address .", true);
        return false;
    }
    var unewPass = document.getElementById("id_upass").value.trim();
    if (unewPass.length == 0) {
        fieldSenseTosterError("Password cannot be empty", true);
        return false;
    }
    if (unewPass.length > 20) {
        fieldSenseTosterError("Password can not be more than 20 characters", true);
        return false;
    }
    if (unewPass.length < 8) {
        fieldSenseTosterError("Password can not be less than 8 characters", true);
        return false;
    }
    var uCPass = document.getElementById("id_ucpass").value.trim();
    if (unewPass !== uCPass) {
        fieldSenseTosterError("Confirm password does not match with password", true);
        return false;
    }
    var uMob = document.getElementsByName("id_cls_mobile_name")[0].value.trim();
    if (uMob.length == 0) {
        fieldSenseTosterError("Mobile No cannot be blank", true);
        return false;
    }
    var isphNo1 = /^\d+$/.test(uMob);
    if (!isphNo1) {
        fieldSenseTosterError("Invalid Mobile number.", true);
        return false;
    }
    if (uMob.length > 20) {
        fieldSenseTosterError("Mobile number should not be more than 20 digits", true);
        return false;
    }


    //var uAccuracy = document.getElementsByName("id_cls_accuray")[0].value.trim();
    var uAccuracy = document.getElementById("id_cls_accuray").value;
    //alert(uAccuracy);
    if (uAccuracy.length == 0) {
        fieldSenseTosterError("Accuracy cannot be blank", true);
        return false;
    }

    if (uAccuracy <= 0) {
        fieldSenseTosterError("Accuracy should be greater than zero", true);
        return false;
    }

    var isvalidaccuray = /^\d+$/.test(uAccuracy);
    if (!isvalidaccuray) {
        fieldSenseTosterError("Invalid Accuracy.", true);
        return false;
    }

    //var uCheckInRadius = document.getElementsByName("id_cls_check_in_radius")[0].value.trim();
    var uCheckInRadius = document.getElementById("id_cls_check_in_radius").value;
    //alert(uCheckInRadius);
    if (uCheckInRadius.length == 0) {
        fieldSenseTosterError("Check-In Radius cannot be blank", true);
        return false;
    }

    if (uCheckInRadius <= 0) {
        fieldSenseTosterError("Check-In Radius should be greater than zero", true);
        return false;
    }

    var isvalidcheck_in_radius = /^\d+$/.test(uCheckInRadius);
    if (!isvalidcheck_in_radius) {
        fieldSenseTosterError("Invalid Check-In Radius.", true);
        return false;
    }


    var ugmale = document.getElementsByClassName("cls_ugmale")[0].checked;
    var ugfe = document.getElementsByClassName("cls_ugfemale")[0].checked;
    var gender;
    if (ugmale) {
        gender = 1;
    } else if (ugfe) {
        gender = 0;
    }
    if ((ugmale === false) && (ugfe === false)) {
        fieldSenseTosterError("Please select gender", true);
        return false;
    }
    var uact = document.getElementById("id_uact").checked;
    ufnm = htmlEntities(ufnm);
    ulnm = htmlEntities(ulnm);
    designation = htmlEntities(designation);
    var notificationEmail = document.getElementById("id_notification_email").checked;
    var notificationMobile = document.getElementById("id_notification_mobile").checked;

    var allowTimeout = 0;
    var valueAllow = document.getElementById("id_allowtimeout").checked;
    if (valueAllow == true) {
        allowTimeout = 1;
    }
    //added by jyoti
    var allowOffline = 1;
    var valueAllowOffline = document.getElementById("id_allowoffline").checked;
    if (valueAllowOffline == false) {
        allowOffline = 0;
    }
     //added by manohar   
    var emp_code=document.getElementById("add_id_empcode").value.trim();
    if (emp_code.length > 100) {
        fieldSenseTosterError("Employee code should not be more than 100 letters", true);
        return false;
    }
    // ended by manohar
    $('#pleaseWaitDialog').modal('show');

    var userObject = {
        "firstName": ufnm,
        "lastName": ulnm,
        "designation": designation,
        "emailAddress": uemail,
        "password": unewPass,
        "mobileNo": uMob,
        "gender": gender,
        "userAccuracy": uAccuracy,
        "checkInRadius": uCheckInRadius,
        "allowTimeout": allowTimeout,
        "allowOffline": allowOffline,
        "role": role,
        // "parentId":parentId,
        "active": uact,
        "createdBy": loginUserId,
        "accountContactType": 0,
        "emailNotification": notificationEmail,
        "mobileNotification": notificationMobile,
        "report_to": reportTo,
        "emp_code":emp_code,           //added by manohar   
        "addTerritoryList": userTerritoryList
    }
    var jsonData = JSON.stringify(userObject);

    $.ajax({
        type: "POST",
        url: fieldSenseURL + "/user",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken,
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            if (data.errorCode == '0000') {
                clevertap.event.push("Add User", {
//                    "UserId": loginUserIdcookie,
//                    "User Name": loginUserNamecookie,
//                    "AccountId": accountIdcookie,
                    "Source": "Web",
                    "Account name": accountNamecookie,
                    "UserRolecookie": UserRolecookie,
                });
                $(".cls_user").modal('hide');
                userDataForReportTo(); //update for reportTo
                var table = $('#sample_5').DataTable();
                table.draw(false);
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

function makeTreeGridViewForTerritories() {

    var cateArray = [];
    userTerritoryList = new Array();
    fullTerriCategories = new Array();
    userHasTerritoryList = new Array();
    for (var i = 0; i < territoryCatData.length; i++) {
        var id = territoryCatData[i].id;
        var parentId = territoryCatData[i].parentCategory;
        var categoryName = territoryCatData[i].categoryName.trim();
        var hasChild = territoryCatData[i].hasChild;
        var catCsv = territoryCatData[i].categoryPositionCsv.trim();
        var userHasTerritory = territoryCatData[i].userHasTerritory;
        fullTerriCategories.push({"parentCat": parentId, "id": id, "categoryName": categoryName, "hasChild": hasChild, "categoryPositionCsv": catCsv});
        if (categoryName == "Unknown") {
            cateArray.push({"id": id, "parentid": parentId, "name": categoryName, "actions": "<input type=\"checkbox\" class=\"userhas_" + userHasTerritory + "\" name=\"option[]\" value=\"" + id + "\" id=\"add_id_Territory_" + id + "\" checked disabled>"});
            userHasTerritory = true;
        } else {
            cateArray.push({"id": id, "parentid": parentId, "name": categoryName, "actions": "<input type=\"checkbox\" class=\"userhas_" + userHasTerritory + "\" name=\"option[]\" value=\"" + id + "\" id=\"add_id_Territory_" + id + "\" onchange=\"EnableCheckboxesOnAdd('" + id + "','" + hasChild + "','" + catCsv + "');return false;\">"});
        }
        if (userHasTerritory == true) {
            userTerritoryList.push(id);
            userHasTerritoryList.push(id);
        }
//function makeTreeGridViewForTerritories() {
//
//    var cateArray = [];
//    userTerritoryList = new Array();
//    fullTerriCategories = new Array();
//    userHasTerritoryList = new Array();
//    for (var i = 0; i < territoryCatData.length; i++) {
//        var id = territoryCatData[i].id;
//        var parentId = territoryCatData[i].parentCategory;
//        var categoryName = territoryCatData[i].categoryName.trim();
//        var hasChild = territoryCatData[i].hasChild;
//        var catCsv = territoryCatData[i].categoryPositionCsv.trim();
//        var userHasTerritory = territoryCatData[i].userHasTerritory;
//        fullTerriCategories.push({"parentCat": parentId, "id": id, "categoryName": categoryName, "hasChild": hasChild, "categoryPositionCsv": catCsv});
//        if (categoryName == "Unknown") {
//            cateArray.push({"id": id, "parentid": parentId, "name": categoryName, "actions": "<input type=\"checkbox\" class=\"userhas_" + userHasTerritory + "\" name=\"option[]\" value=\"" + id + "\" id=\"add_id_Territory_" + id + "\" checked disabled>"});
//            userHasTerritory = true;
//        } else {
//            cateArray.push({"id": id, "parentid": parentId, "name": categoryName, "actions": "<input type=\"checkbox\" class=\"userhas_" + userHasTerritory + "\" name=\"option[]\" value=\"" + id + "\" id=\"add_id_Territory_" + id + "\" onchange=\"EnableCheckboxesOnAdd('" + id + "','" + hasChild + "','" + catCsv + "');return false;\">"});
//        }
//        if (userHasTerritory == true) {
//            userTerritoryList.push(id);
//            userHasTerritoryList.push(id);
//        }

    }
    if (territoryCatData.length != 0) {
        var source =
                {
                    dataType: "json",
                    dataFields: [
                        {name: "name", type: "string"},
                        //{ name: "quantity", type: "number" },
                        {name: "id", type: "number"},
                        {name: "parentid", type: "number"},
                        //{ name: "active", type: "string" },
                        // { name: "date", type: "string" },
                        {name: "actions", type: "string"}
                    ],
                    hierarchy:
                            {
                                keyDataField: {name: 'id'},
                                parentDataField: {name: 'parentid'}
                            },
                    id: 'id',
                    localData: cateArray
                };
        var dataAdapter = new $.jqx.dataAdapter(source);
        // create Tree Grid
        $("#add_treeGrid").jqxTreeGrid(
                {
                    width: 745,
                    height: 420,
                    source: dataAdapter,
                    sortable: true,
                    //pageable: true,
                    pagerMode: 'advanced',
                    /*ready: function () {
                     //$("#treeGrid").jqxTreeGrid('expandRow', '2');
                     App.initUniform();
                     App.fixContentHeight();
                     jQuery('.tooltips').tooltip();
                     },*/
                    rendered: function () {
                        App.initUniform();
                        App.fixContentHeight();
                        jQuery('.tooltips').tooltip();
                        $('[id^=add_id_Territory_]').each(function () {
                            $(this).attr('checked', false);
                            $(this).parent().removeClass('checked');
                        });
                        for (var i = 0; i < userTerritoryList.length; i++) {
                            //if(userTerritoryList[i].checked==true){
                            $("#add_id_Territory_" + userTerritoryList[i]).parent().attr("class", "checked");
                            $("#add_id_Territory_" + userTerritoryList[i]).attr('checked', true);
                            //}
                        }
                    },
                    columns: [
                        {text: 'Category Name', dataField: "name", width: 400},
                        //{ text: '', dataField: "checkbox",  width: 160},
                        {text: '   ', dataField: "actions", cellsFormat: "c2"},
                    ]
                });
        /*$('#treeGrid').on('rowCollapse',function (event){
         App.initUniform();
         App.fixContentHeight();
         jQuery('.tooltips').tooltip();
         });
         $('#treeGrid').on('rowExpand',function (event){
         App.initUniform();
         App.fixContentHeight();
         jQuery('.tooltips').tooltip();
         });*/
    }
//    $("#verticalScrollBaradd_treeGrid").css("visibility", "visible");
//    $("#jqxScrollThumbverticalScrollBaradd_treeGrid").css("visibility", "visible");
    App.initUniform();
    App.fixContentHeight();
    jQuery('.tooltips').tooltip();
    //$('#pleaseWaitDialog').modal('hide');
}


function getAllUserActiveTerritoriesOnEdit(userId) {

    var cateArray = [];

    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/territoryCategory/active/territory/" + userId,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        success: function (data) {
            var territoryData = data.result;
            if (data.errorCode == '0000') {
                userTerritoryList = new Array();
                fullTerriCategories = new Array();
                userHasTerritoryList = new Array();
                for (var i = 0; i < territoryData.length; i++) {
                    var id = territoryData[i].id;
                    var parentId = territoryData[i].parentCategory;
                    var categoryName = territoryData[i].categoryName.trim();
                    var hasChild = territoryData[i].hasChild;
                    var catCsv = territoryData[i].categoryPositionCsv.trim();
                    var userHasTerritory = territoryData[i].userHasTerritory;
                    fullTerriCategories.push({"parentCat": parentId, "id": id, "categoryName": categoryName, "hasChild": hasChild, "categoryPositionCsv": catCsv});
                    if (categoryName == "Unknown") {
                        cateArray.push({"id": id, "parentid": parentId, "name": categoryName, "actions": "<input type=\"checkbox\" class=\"userhas_" + userHasTerritory + "\" name=\"option[]\" value=\"" + id + "\" id=\"edit_id_Territory_" + id + "\" checked disabled>"});
                    } else {
                        cateArray.push({"id": id, "parentid": parentId, "name": categoryName, "actions": "<input type=\"checkbox\" class=\"userhas_" + userHasTerritory + "\" name=\"option[]\" value=\"" + id + "\" id=\"edit_id_Territory_" + id + "\" onchange=\"EnableCheckboxes('" + id + "','" + hasChild + "','" + catCsv + "');return false;\">"});
                    }
                    if (userHasTerritory == true) {
                        userTerritoryList.push(id);
                        userHasTerritoryList.push(id);
                    }

                }
                if (territoryData.length != 0) {
                    var source =
                            {
                                dataType: "json",
                                dataFields: [
                                    {name: "name", type: "string"},
                                    //{ name: "quantity", type: "number" },
                                    {name: "id", type: "number"},
                                    {name: "parentid", type: "number"},
                                    //{ name: "active", type: "string" },
                                    // { name: "date", type: "string" },
                                    {name: "actions", type: "string"}
                                ],
                                hierarchy:
                                        {
                                            keyDataField: {name: 'id'},
                                            parentDataField: {name: 'parentid'}
                                        },
                                id: 'id',
                                localData: cateArray
                            };
                    var dataAdapter = new $.jqx.dataAdapter(source);
                    // create Tree Grid
                    $("#edit_treeGrid").jqxTreeGrid(
                            {
                                width: 745,
                                height: 420,
                                source: dataAdapter,
                                sortable: true,
                                //pageable: true,
                                pagerMode: 'advanced',
                                /*ready: function () {
                                 //$("#treeGrid").jqxTreeGrid('expandRow', '2');
                                 App.initUniform();
                                 App.fixContentHeight();
                                 jQuery('.tooltips').tooltip();
                                 },*/
                                rendered: function () {
                                    App.initUniform();
                                    App.fixContentHeight();
                                    jQuery('.tooltips').tooltip();
                                    $('[id^=edit_id_Territory_]').each(function () {
                                        $(this).attr('checked', false);
                                        $(this).parent().removeClass('checked');
                                    });
                                    for (var i = 0; i < userTerritoryList.length; i++) {
                                        //if(userTerritoryList[i].checked==true){
                                        $("#edit_id_Territory_" + userTerritoryList[i]).parent().attr("class", "checked");
                                        $("#edit_id_Territory_" + userTerritoryList[i]).attr('checked', true);
                                        //}
                                    }
                                },
                                columns: [
                                    {text: 'Category Name', dataField: "name", width: 400},
                                    //{ text: '', dataField: "checkbox",  width: 160},
                                    {text: '   ', dataField: "actions", cellsFormat: "c2"},
                                ]
                            });
                    /*$('#treeGrid').on('rowCollapse',function (event){
                     App.initUniform();
                     App.fixContentHeight();
                     jQuery('.tooltips').tooltip();
                     });
                     $('#treeGrid').on('rowExpand',function (event){
                     App.initUniform();
                     App.fixContentHeight();
                     jQuery('.tooltips').tooltip();
                     });*/
                }
                $("#verticalScrollBaredit_treeGrid").css("visibility", "visible");
                App.initUniform();
                App.fixContentHeight();
                jQuery('.tooltips').tooltip();
                $('#pleaseWaitDialog').modal('hide');
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                //$(".cls_editUser").modal('hide');
                FieldSenseInvalidToken(data.errorMessage);
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            $('#pleaseWaitDialog').modal('hide');
            alert("in error:" + thrownError);
        }
    });


}

var userTerritoryList = new Array();
var fullTerriCategories = new Array();
var userHasTerritoryList = new Array();

function EnableCheckboxesOnAdd(id, hasChild, catCsv) {
    var id = parseInt(id);
    if ($("#add_id_Territory_" + id).is(":checked")) {
        ///index = a.findIndex(x => x.prop2=="yutu")
        /*var index= $.map(userTerritoryList, function(obj, index) {
         if(obj.catid == id) {
         return index;
         }
         });*/
        var index = userTerritoryList.indexOf(id);
        //if(index.length==0){
        if (index < 0) {
            userTerritoryList.push(id);
        }
        if (hasChild == "1") {
            var childCat = [];
            for (var i = 0; i < fullTerriCategories.length; i++) {
                var parentCsvindex = fullTerriCategories[i].categoryPositionCsv.lastIndexOf("," + catCsv);
                if (parentCsvindex != -1) {
                    var parentPos = fullTerriCategories[i].categoryPositionCsv.substring(parentCsvindex + 1);
                    if (catCsv == parentPos) {
                        childCat.push(fullTerriCategories[i].id);
                    }
                }
            }
            for (var i = 0; i < childCat.length; i++) {
                /*var index= $.map(userTerritoryList, function(obj, index) {
                 if(obj.catid == childCat[i].id) {
                 return index;
                 }
                 });*/
                var index = userTerritoryList.indexOf(childCat[i]);
                //if(index.length==0){
                if (index < 0) {
                    userTerritoryList.push(childCat[i]);
                    $("#add_id_Territory_" + childCat[i]).parent().attr("class", "checked");
                    $("#add_id_Territory_" + childCat[i]).attr('checked', true);
                }
            }
        }
    } else {
        /*var index= $.map(userTerritoryList, function(obj, index) {
         if(obj.catid == id) {
         return index;
         }
         });*/
        var index = userTerritoryList.indexOf(id);
        //if (index.length!=0){
        if (index > -1) {
            userTerritoryList.splice(index, 1);
        }
        if (hasChild == "1") {
            var childCat = [];
            for (var i = 0; i < fullTerriCategories.length; i++) {
                var parentCsvindex = fullTerriCategories[i].categoryPositionCsv.lastIndexOf("," + catCsv);
                if (parentCsvindex != -1) {
                    var parentPos = fullTerriCategories[i].categoryPositionCsv.substring(parentCsvindex + 1);
                    if (catCsv == parentPos) {
                        childCat.push(fullTerriCategories[i].id);
                    }
                }
            }
            for (var i = 0; i < childCat.length; i++) {
                /*var index= $.map(userTerritoryList, function(obj, index) {
                 if(obj.catid == childCat[i].id) {
                 return index;
                 }
                 });*/
                var index = userTerritoryList.indexOf(childCat[i]);
                //if(index.length!=0){
                if (index > -1) {
                    userTerritoryList.splice(index, 1);
                    $("#add_id_Territory_" + childCat[i]).parent().removeClass("checked");
                    $("#add_id_Territory_" + childCat[i]).attr('checked', false);
                }
            }
        }
    }
}


function EnableCheckboxesTerritory(id, catName, hasChild, catCsv) {
    var id = parseInt(id);
    if ($("#id_Territory_" + id).is(":checked")) {
        ///index = a.findIndex(x => x.prop2=="yutu")
        var index = $.map(leftside_userTerritoryList, function (obj, index) {
            if (obj.catid == id) {
                return index;
            }
        });
        if (index.length == 0) {
            leftside_userTerritoryList.push({"catid": id, "checked": true, "categoryName": catName});
            selectedTerritories.push(catName);
        }
        if (hasChild == "1") {
            var childCat = [];
            for (var i = 0; i < leftside_fullTerriCategories.length; i++) {
                var parentCsvindex = leftside_fullTerriCategories[i].categoryPositionCsv.lastIndexOf("," + catCsv);
                if (parentCsvindex != -1) {
                    var parentPos = leftside_fullTerriCategories[i].categoryPositionCsv.substring(parentCsvindex + 1);
                    if (catCsv == parentPos) {
                        childCat.push({"id": leftside_fullTerriCategories[i].id, "checked": true, "categoryName": leftside_fullTerriCategories[i].categoryName});
                    }
                }
            }
            for (var i = 0; i < childCat.length; i++) {
                var index = $.map(leftside_userTerritoryList, function (obj, index) {
                    if (obj.catid == childCat[i].id) {
                        return index;
                    }
                });
                if (index.length == 0) {
                    leftside_userTerritoryList.push({"catid": childCat[i].id, "checked": true, "categoryName": childCat[i].categoryName});
                    selectedTerritories.push(childCat[i].categoryName);
                    $("#id_Territory_" + childCat[i].id).parent().attr("class", "checked");
                    $("#id_Territory_" + childCat[i].id).attr('checked', true);
                }
            }
        }
    } else {
        var index = $.map(leftside_userTerritoryList, function (obj, index) {
            if (obj.catid == id) {
                return index;
            }
        });
        if (index.length != 0) {
            leftside_userTerritoryList.splice(index[0], 1);
            selectedTerritories.splice(index[0], 1);
        }
        if (hasChild == "1") {
            var childCat = [];
            for (var i = 0; i < leftside_fullTerriCategories.length; i++) {
                var parentCsvindex = leftside_fullTerriCategories[i].categoryPositionCsv.lastIndexOf("," + catCsv);
                if (parentCsvindex != -1) {
                    var parentPos = leftside_fullTerriCategories[i].categoryPositionCsv.substring(parentCsvindex + 1);
                    if (catCsv == parentPos) {
                        childCat.push({"id": leftside_fullTerriCategories[i].id, "checked": true, "categoryName": leftside_fullTerriCategories[i].categoryName});
                    }
                }
            }
            for (var i = 0; i < childCat.length; i++) {
                var index = $.map(leftside_userTerritoryList, function (obj, index) {
                    if (obj.catid == childCat[i].id) {
                        return index;
                    }
                });
                if (index.length != 0) {
                    leftside_userTerritoryList.splice(index[0], 1);
                    selectedTerritories.splice(index[0], 1);
                    $("#id_Territory_" + childCat[i].id).parent().removeClass("checked");
                    $("#id_Territory_" + childCat[i].id).attr('checked', false);
                }
            }
        }
    }
}



function EnableCheckboxes(id, hasChild, catCsv) {
    var id = parseInt(id);
    if ($("#edit_id_Territory_" + id).is(":checked")) {
        ///index = a.findIndex(x => x.prop2=="yutu")
        /*var index= $.map(userTerritoryList, function(obj, index) {
         if(obj.catid == id) {
         return index;
         }
         });*/
        var index = userTerritoryList.indexOf(id);
        //if(index.length==0){
        if (index < 0) {
            userTerritoryList.push(id);
        }
        if (hasChild == "1") {
            var childCat = [];
            for (var i = 0; i < fullTerriCategories.length; i++) {
                var parentCsvindex = fullTerriCategories[i].categoryPositionCsv.lastIndexOf("," + catCsv);
                if (parentCsvindex != -1) {
                    var parentPos = fullTerriCategories[i].categoryPositionCsv.substring(parentCsvindex + 1);
                    if (catCsv == parentPos) {
                        childCat.push(fullTerriCategories[i].id);
                    }
                }
            }
            for (var i = 0; i < childCat.length; i++) {
                /*var index= $.map(userTerritoryList, function(obj, index) {
                 if(obj.catid == childCat[i].id) {
                 return index;
                 }
                 });*/
                var index = userTerritoryList.indexOf(childCat[i]);
                //if(index.length==0){
                if (index < 0) {
                    userTerritoryList.push(childCat[i]);
                    $("#edit_id_Territory_" + childCat[i]).parent().attr("class", "checked");
                    $("#edit_id_Territory_" + childCat[i]).attr('checked', true);
                }
            }
        }
    } else {
        /*var index= $.map(userTerritoryList, function(obj, index) {
         if(obj.catid == id) {
         return index;
         }
         });*/
        var index = userTerritoryList.indexOf(id);
        //if (index.length!=0){
        if (index > -1) {
            userTerritoryList.splice(index, 1);
        }
        if (hasChild == "1") {
            var childCat = [];
            for (var i = 0; i < fullTerriCategories.length; i++) {
                var parentCsvindex = fullTerriCategories[i].categoryPositionCsv.lastIndexOf("," + catCsv);
                if (parentCsvindex != -1) {
                    var parentPos = fullTerriCategories[i].categoryPositionCsv.substring(parentCsvindex + 1);
                    if (catCsv == parentPos) {
                        childCat.push(fullTerriCategories[i].id);
                    }
                }
            }
            for (var i = 0; i < childCat.length; i++) {
                /*var index= $.map(userTerritoryList, function(obj, index) {
                 if(obj.catid == childCat[i].id) {
                 return index;
                 }
                 });*/
                var index = userTerritoryList.indexOf(childCat[i]);
                //if(index.length!=0){
                if (index > -1) {
                    userTerritoryList.splice(index, 1);
                    $("#edit_id_Territory_" + childCat[i]).parent().removeClass("checked");
                    $("#edit_id_Territory_" + childCat[i]).attr('checked', false);
                }
            }
        }
    }
}
$("select").click(function () {
    $("option[selected='selected']", this).hide();
});

var listOfReporterOfIDs;
function getListOfReporterOfUserId(reporter_of_userID){
    // Added by jyoti 22-june-2017, purpose - to check if report_to is already someone else reporter then dont display that user in "Reports to" list
    $.ajax({
           type: "GET",
           url: fieldSenseURL + "/user/reporter_of/" + reporter_of_userID,
           contentType: "application/json; charset=utf-8",
           headers: {
               "userToken": userToken
           },
           crossDomain: false,
           cache: false,
           dataType: 'json',
           async: false,
           success: function (data) {
               if (data.errorCode == '0000') {                                
                    listOfReporterOfIDs = data.result;
               }
           }
       });
   // Ended by jyoti 22-june-2017
}

function editUserTemplate(userId) {
    $('.cls_editUser').html('');
    $('#pleaseWaitDialog').modal('show');
    offlineStatusCheck();       // Added by jyoti, purpose - to check offline feature : if not enable, disable checkbox of allow offline
    getListOfReporterOfUserId(userId); // Added by jyoti 22-june-2017
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/user/" + userId,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        async: true,
        success: function (data) {
            if (data.errorCode == '0000') {

                var userData = data.result;
                var userTemplate = '';
                var userId = userData.id;
                var userFnm = userData.firstName;
                var userLnm = userData.lastName;
                var userDesignation = userData.designation;
                var userEmail = userData.emailAddress;
                var userMobile = userData.mobileNo;
                var userActive = userData.active;
                var userGender = userData.gender;
                var userAccuracy = userData.userAccuracy;
                var userCheckInRadius = userData.checkInRadius;
                var allowTimeout = userData.allowTimeout;
                var allowOffline_edit = userData.allowOffline;
                var password = userData.password;
                var parentId = userData.parentId;
                var reportTo = userData.report_to;
                var role = userData.role;
                var interval1 = userData.interval;             
                var emp_code = userData.emp_code;  //added by manohar
//                alert("emp_code "+emp_code);
              
                if(interval1 == '0')
                {
                    interval1 ="Default";
                }
                
                
                userTemplate += '<div class="modal-dialog modal-wide">';
                userTemplate += '<div class="modal-content">	';
                userTemplate += '<div class="modal-header">';
                userTemplate += '<button aria-hidden="true" class="close" data-dismiss="modal" type="button"/>';
                userTemplate += '<h4 class="modal-title">Edit User Details</h4>';
                userTemplate += '</div>';
                userTemplate += '<form class="form-horizontal form-row-seperated" role="form" id="edit-userDetails">';
                userTemplate += '<div class="modal-body" id="act1">';
                userTemplate += '<ul class="nav nav-tabs">';
                userTemplate += '<li class="active">';
//                userTemplate += '<a href="#tab_1_1_1" data-toggle="tab">General Info</a>'; // commented by jyoti
                userTemplate += '<a href="#tab_1_1_1" data-toggle="tab" id="edit_general_tab_1_1" onclick="editUserActionBtn(this.id,\'' + userId + '\',\'' + userEmail + '\',\'' + password + '\')" >General Info</a>'; // added by jyoti
                userTemplate += '</li>';
                userTemplate += '<li class="">';
//                userTemplate += '<a href="#tab_1_2_1" data-toggle="tab">Territories</a>'; // commented by jyoti
                userTemplate += '<a href="#tab_1_2_1" data-toggle="tab" id="edit_territory_tab_1_2" onclick="editUserActionBtn(this.id,\'' + userId + '\',\'' + userEmail + '\',\'' + password + '\')" >Territories</a>'; // added by jyoti
                userTemplate += '</li>';
                userTemplate += '</ul>';
                userTemplate += '<div class="tab-content">';
                userTemplate += '<div class="tab-pane fade active in" id="tab_1_1_1">';
                //userTemplate += '<div class="scroller" data-always-visible="1" data-rail-visible1="1">';
                userTemplate += '<div class="form-body" id="act">';
                userTemplate += '<div class="scroller" data-always-visible="1" data-rail-visible1="1">';
                userTemplate += '<div class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">First Name</label>';
                userTemplate += '<div class="col-md-6">';
                userTemplate += '<input type="fname" class="form-control" value="' + userFnm + '" id="id_userFnm">';
                userTemplate += '</div>';
                userTemplate += '</div>';               
                userTemplate += '<div class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">Last Name</label>';
                userTemplate += '<div class="col-md-6">';
                userTemplate += '<input type="lname" class="form-control" value="' + userLnm + '" id="id_userLnm">';
                userTemplate += '</div>';
                userTemplate += '</div>';  //added by manohar                
                userTemplate += '<div class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">Emp. Code</label>';
                userTemplate += '<div class="col-md-6">';
                userTemplate += '<input type="empcode" class="form-control" value="' + emp_code + '" id="edit_id_empcode">';
                userTemplate += '</div>';
                userTemplate += '</div>';                
                userTemplate += '<div class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">Gender</label>';
                if (userGender == 1)
                {
                    userTemplate += '<div class="col-md-6">';
                    userTemplate += '<div class="radio-list" id="id_gender_radiobtns">';
                    userTemplate += '<label class="radio-inline">';
                    userTemplate += '<input type="radio" name="optionsRadios3" id="optionsRadios1" value="1" checked > Male </label>';
                    userTemplate += '<label class="radio-inline">';
                    userTemplate += '<input type="radio" name="optionsRadios3" id="optionsRadios2" value="0"> Female </label>';
                    userTemplate += '</div></div>';
                } else {
                    userTemplate += '<div class="col-md-6">';
                    userTemplate += '<div class="radio-list" id="id_gender_radiobtns">';
                    userTemplate += '<label class="radio-inline">';
                    userTemplate += '<input type="radio" name="optionsRadios3" id="optionsRadios1" value="1" > Male </label>';
                    userTemplate += '<label class="radio-inline">';
                    userTemplate += '<input type="radio" name="optionsRadios3" id="optionsRadios2" value="0" checked> Female </label>';
                    userTemplate += '</div></div>';
                }
                userTemplate += '</div>';
                userTemplate += '<div class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">Designation</label>';
                userTemplate += '<div class="col-md-6">';
                userTemplate += '<input type="lname" class="form-control" value="' + userDesignation + '" id="id_userDesignation">';
                userTemplate += '</div>';
                userTemplate += '</div>';
                userTemplate += '<div class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">Role</label>';
                userTemplate += '<div class="col-md-6">';
//                userTemplate += '<select class="form-control" onchange ="showReportingHead(this.value,true,' + userId + ')" id="id_editRole" >';
//                userTemplate += '<option value="0">Select</option>';
//                userTemplate += '<option value="1">Admin</option>';
//                userTemplate += '<option value="2">Accounts Personnel</option>';
//                userTemplate += '<option value="5">On-field Personnel</option>';
                /* if (role == 1) {
                 userTemplate += '<option value="1"  selected >Admin</option>';
                 userTemplate += '<option value="2" >Accounts Personnel</option>';
                 userTemplate += '<option value="5" >On-field Personnel</option>';
                 }  else if (role == 5) {
                 userTemplate += '<option value="1"  >Admin</option>';
                 userTemplate += '<option value="2"  >Accounts Personnel</option>';
                 userTemplate += '<option value="5" selected>On-field Personnel</option>';
                 } else if (role == 2) {
                 userTemplate += '<option value="1" >Admin</option>';
                 userTemplate += '<option value="2"  selected>Accounts Personnel</option>';
                 userTemplate += '<option value="5"  >On-field Personnel</option>';
                 } 
                 else {
                 userTemplate += '<option value="1" >Admin</option>';
                 userTemplate += '<option value="2" >Accounts Personnel</option>';
                 userTemplate += '<option value="5" >On-field Personnel</option>';
                 }*/
//                userTemplate += '</select>';
                userTemplate += '<select class="form-control selectpicker" multiple onchange ="showReportingHead(this.value,true,' + userId + ')" id="id_editRole" >';
                userTemplate += '<option value="1" style="font-size:10pt">Admin</option>';
                userTemplate += '<option value="2" style="font-size:10pt">Accounts Personnel</option>';
                userTemplate += '<option value="5" style="font-size:10pt">On-field Personnel</option>';
                userTemplate += '</select>';
                userTemplate += '</div>';
                userTemplate += '</div>';
                userTemplate += '<div class="form-group" id="editReportingHeadDiv">';
                userTemplate += '<label class="col-md-6 control-label">Reports To</label>';
                userTemplate += '<div class="col-md-6">';
                userTemplate += '<select class="form-control" id="id_editReportingHead">';
                userTemplate += '<option value="0">Select</option>';
                userTemplate += '</select>';
                userTemplate += ' </div>';
                userTemplate += '</div>';
                userTemplate += '<div class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">Email</label>';
                userTemplate += '<div class="col-md-6">';
                userTemplate += '<input type="fname" class="form-control" value="' + userEmail + '" disabled>';
                userTemplate += '</div>';
                userTemplate += '</div>';
                userTemplate += '<div class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">Show Password</label>';
                userTemplate += '<div class="col-md-6">';
                userTemplate += '<div class="checkbox-list">';
                userTemplate += '<input type="checkbox" id="id_showPass" onclick="showPassFileds()">';
                userTemplate += '</div>';
                userTemplate += '</div>';
                userTemplate += '</div>';
                userTemplate += '<div id="id_hideShowPass" style="display:none;">';
                userTemplate += '<div class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">Password</label>';
                userTemplate += '<div class="col-md-6">';
                userTemplate += '<input type="password" class="form-control" value="' + password + '" id="id_password">';
                userTemplate += '</div>';
                userTemplate += '</div>';
                userTemplate += '<div class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">Confirm Password</label>';
                userTemplate += '<div class="col-md-6">';
                userTemplate += '<input type="password" class="form-control" value="' + password + '" id="id_cpassword">';
                userTemplate += '</div>';
                userTemplate += '</div>';
                userTemplate += '</div>';
                userTemplate += '<div class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">Mobile No.</label>';
                userTemplate += '<div class="col-md-6">';
                userTemplate += '<input class="form-control" id="mask_phone" type="text" value="' + userMobile + '" name="name_mobile_number"/>';
                userTemplate += '</div>';
                userTemplate += '</div>';

                userTemplate += '<div id="user_accuracy_div" class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">Accuracy (In Meters)</label>';
                userTemplate += '<div class="col-md-6">';
                userTemplate += '<select class="form-control" id="mask_accuracy" name="name_user_accuracy">';
                userTemplate += '<option value=50>50</option>';
                userTemplate += '<option value=100>100</option>';
                userTemplate += '<option value=250>250</option>';
                userTemplate += '<option value=500>500</option>';
                userTemplate += '<option value=1000>1000</option>';
                userTemplate += '<option value=2500>2500</option>';
                userTemplate += '<option value=5000>5000</option>';
                /*  if(userAccuracy === 50){
                 userTemplate += '<option value=50 selected>50</option>';
                 }else{
                 userTemplate += '<option value=50>50</option>';
                 }               
                 
                 if(userAccuracy === 100){
                 userTemplate += '<option value=100 selected>100</option>';
                 }else{
                 userTemplate += '<option value=100>100</option>';
                 }
                 
                 if(userAccuracy === 250){
                 userTemplate += '<option value=250 selected>250</option>';
                 }else{
                 userTemplate += '<option value=250>250</option>';
                 }
                 
                 if(userAccuracy === 500){
                 userTemplate += '<option value=500 selected>500</option>';
                 }else{
                 userTemplate += '<option value=500>500</option>';
                 }
                 
                 if(userAccuracy === 1000){
                 userTemplate += '<option value=1000 selected>1000</option>';
                 }else{
                 userTemplate += '<option value=1000>1000</option>';
                 }
                 
                 if(userAccuracy === 2500){
                 userTemplate += '<option value=2500 selected>2500</option>';
                 }else{
                 userTemplate += '<option value=2500>2500</option>';
                 }
                 
                 if(userAccuracy === 5000){
                 userTemplate += '<option value=5000 selected>5000</option>';
                 }else{
                 userTemplate += '<option value=5000>5000</option>';
                 }*/

                userTemplate += '</select>';
                userTemplate += '</div>';
                userTemplate += '</div>';

                userTemplate += '<div class="form-group" id="check_in_radius_div">';
                userTemplate += '<label class="col-md-6 control-label">Check-In Radius (In Meters)</label>';
                userTemplate += '<div class="col-md-6">';
                userTemplate += '<select class="form-control" id="mask_check_in_radius" name="name_check_in_radius">';
                userTemplate += '<option value=50>50</option>';
                userTemplate += '<option value=100>100</option>';
                userTemplate += '<option value=250>250</option>';
                userTemplate += '<option value=500>500</option>';
                userTemplate += '<option value=1000>1000</option>';
                userTemplate += '<option value=2500>2500</option>';
                userTemplate += '<option value=5000>5000</option>';
                /* if(userCheckInRadius === 50){
                 userTemplate += '<option value=50 selected>50</option>';
                 }else{
                 userTemplate += '<option value=50>50</option>';
                 }
                 
                 if(userCheckInRadius === 100){
                 userTemplate += '<option value=100 selected>100</option>';
                 }else{
                 userTemplate += '<option value=100>100</option>';
                 }
                 
                 if(userCheckInRadius === 250){
                 userTemplate += '<option value=250 selected>250</option>';
                 }else{
                 userTemplate += '<option value=250>250</option>';
                 }
                 
                 if(userCheckInRadius === 500){
                 userTemplate += '<option value=500 selected>500</option>';
                 }else{
                 userTemplate += '<option value=500>500</option>';
                 }
                 
                 if(userCheckInRadius === 1000){
                 userTemplate += '<option value=1000 selected>1000</option>';
                 }else{
                 userTemplate += '<option value=1000>1000</option>';
                 }
                 
                 if(userCheckInRadius === 2500){
                 userTemplate += '<option value=2500 selected>2500</option>';
                 }else{
                 userTemplate += '<option value=2500>2500</option>';
                 }
                 
                 if(userCheckInRadius === 5000){
                 userTemplate += '<option value=5000 selected>5000</option>';
                 }else{
                 userTemplate += '<option value=5000>5000</option>';
                 }*/

                userTemplate += '</select>';
                userTemplate += '</div>';
                userTemplate += '</div>';

                userTemplate += '<div class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">Active</label>';
                userTemplate += '<div class="col-md-6">';
                userTemplate += '<div class="checkbox-list">';
                if (userActive == true) {
                    if (userId == loginUserId) {
                        userTemplate += '<input type="checkbox" checked id="id_userAct" disabled>';
                    } else {
                        userTemplate += '<input type="checkbox" checked id="id_userAct">';
                    }
                } else {
                    userTemplate += '<input type="checkbox" id="id_userAct">';
                }
                userTemplate += '</div>';
                userTemplate += '</div>';
                userTemplate += '</div>';

                userTemplate += '<div class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">Allow Timeout</label>';
                userTemplate += '<div class="col-md-6">';
                userTemplate += '<div class="checkbox-list">';
                if (allowTimeout == 1) {
                    userTemplate += '<input type="checkbox" id="id_allowtimeout_edit" checked>';
                } else {
                    userTemplate += '<input type="checkbox" id="id_allowtimeout_edit">';
                }
                userTemplate += '</div>';
                userTemplate += '</div>';
                userTemplate += '</div>';
                //added by jyoti
                userTemplate += '<div class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">Allow Offline</label>';
                userTemplate += '<div class="col-md-6">';
                userTemplate += '<div class="checkbox-list">';
                if (offlineEnable == true) {
                    if (allowOffline_edit == 1) {
                        userTemplate += '<input type="checkbox" id="id_allowoffline_edit" checked>';
                    } else {
                        userTemplate += '<input type="checkbox" id="id_allowoffline_edit">';
                    }
                }
                else {
                    if (allowOffline_edit == 1) {
//                                        userTemplate += '<label style="display: block; color: #D3D3D3;"> <input type="checkbox" id="id_allowoffline_edit" checked disabled > Offline feature disabled </label>';
                        userTemplate += ' <input type="checkbox" id="id_allowoffline_edit" checked disabled > ';
                    } else {
//                                        userTemplate += '<label style="display: block; color: #D3D3D3;"> <input type="checkbox" id="id_allowoffline_edit" disabled > Offline feature disabled </label>';
                        userTemplate += ' <input type="checkbox" id="id_allowoffline_edit" disabled > ';

                    }
                }
                userTemplate += '</div>';
                userTemplate += '</div>';
                userTemplate += '</div>';
                //ended by jyoti
                userTemplate += '<div class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">Notification</label>';
                userTemplate += '<div class="col-md-6">';
                userTemplate += '<div class="checkbox-list">';
                userTemplate += '<input type="checkbox" id="id_notification_email_edit"/> <label class="control-label" style="margin-left: 30px;margin-top: -25px!important">Email</label>';
                userTemplate += '<input type="checkbox" id="id_notification_mobile_edit"/> <label class="control-label" style="margin-left: 30px;margin-top: -25px!important">Mobile</label>';
                userTemplate += '</div>';
                userTemplate += '</div>';
                userTemplate += '</div>';
        // added by nikhil 23 jun 2017
                userTemplate+='<div class="form-group">';
                userTemplate+='<label class="col-md-6 control-label">Location Interval (In Minute)</label>';
                userTemplate+='<div class="col-md-6">';
                userTemplate+='<select class="form-control" id="Interval">';
//                userTemplate += '<option value=' + interval1 + ' >' + interval1 + '</option>';
//                userTemplate += '<option value="1">1</option>';
//                userTemplate += '<option value="2">2</option>';
//                userTemplate += '<option value="3">3</option>';
//                userTemplate += '<option value="4">4</option>';
//                userTemplate += '<option value="5">5</option>';
//                userTemplate += '<option value="6">6</option>';
//                userTemplate += '<option value="7">7</option>';
//                userTemplate += '<option value="8">8</option>';
//                userTemplate += '<option value="9">9</option>';
//                userTemplate += '<option value="10">10</option>';

                if(interval1 === '0'){
                  
                  userTemplate += '<option value="Default" selected>Default</option>';
                 }else{
                 userTemplate += '<option value="Default">Default</option>';
                 }
                if(interval1 === '1'){
                 userTemplate += '<option value=1 selected>1</option>';
                 }else{
                 userTemplate += '<option value=1>1</option>';
                 }
                 
                 if(interval1 === '2'){
                 userTemplate += '<option value=2 selected>2</option>';
                 }else{
                 userTemplate += '<option value=2>2</option>';
                 }
                 
                 if(interval1 === '3'){
                 userTemplate += '<option value=3 selected>3</option>';
                 }else{
                 userTemplate += '<option value=3>3</option>';
                 }
                 
                 if(interval1 === '4'){
                 userTemplate += '<option value=4 selected>4</option>';
                 }else{
                 userTemplate += '<option value=4>4</option>';
                 }
                 
                 if(interval1 === '5'){
                 userTemplate += '<option value=5 selected>5</option>';
                 }else{
                 userTemplate += '<option value=5>5</option>';
                 }
                 
                
                userTemplate+='</select>';
                userTemplate+='</div>';
                userTemplate+='</div>';
                //ended by nikhil
                userTemplate += '</div>';
                userTemplate += '</div>';
                userTemplate += '</div>';
                userTemplate += '<div class="tab-pane fade" id="tab_1_2_1">';
                //userTemplate+='<div class="scroller" data-always-visible="1" data-rail-visible1="1">';
                userTemplate += '<div id="edit_treeGrid"></div>';
                //userTemplate+='</div>';
                userTemplate += '</div>';
                userTemplate += '</div>';
                userTemplate += '</div>';
                userTemplate += '<div class="modal-footer">';
//                userTemplate += '<button type="button" class="btn btn-info" onclick="editUser(\'' + userId + '\',\'' + userEmail + '\',\'' + password + '\')">Save</button>'; // commented by jyoti
                userTemplate += '<button type="button" class="btn btn-info" id="editUserContinue"  onclick="editUserActionBtn(this.id,\'' + userId + '\',\'' + userEmail + '\',\'' + password + '\')" >Continue</button>'; // added by jyoti
                userTemplate += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
                userTemplate += '</div>';
                userTemplate += '</form>';
                userTemplate += '</div>';
                userTemplate += '</div>';
                /* userTemplate += '<div class="modal-footer">';
                 userTemplate += '<button type="button" class="btn btn-info" onclick="editUser(\'' + userId + '\',\'' + userEmail + '\',\'' + password + '\')">Save</button>';
                 userTemplate += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
                 userTemplate += '</div>';
                 userTemplate += '</div>';
                 userTemplate += '</div>';*/
                //  $('#id_ofAllSubordinates').html($('#id_ofAllSubordinate').html());
                $('#responsive2').html(userTemplate);
                if (role == 8)   // added by manohar
                {
                    $("#id_editRole").val(['1', '2', '5']);
                } else if (role == 1)
                {
                    $("#id_editRole").val(['1']);
                }else if (role == 2)
                {
                    $("#id_editRole").val(['2']);
                }else if (role == 5)
                {
                    $("#id_editRole").val(['5']);
                } else if (role == 3)
                {
                    $("#id_editRole").val(['1', '2']);
                } else if (role == 6)
                {
                    $("#id_editRole").val(['1', '5']);
                } else if (role == 7)
                {
                    $("#id_editRole").val(['2', '5']);
                }
                $("#editReportingHeadDiv").show();
                $("#mask_accuracy").val(userAccuracy);
                $("#mask_check_in_radius").val(userCheckInRadius);
                getAllUserActiveTerritoriesOnEdit(userId);
               if (topIdInHirarachy != userId) {   // modified by manohar
                    if (role == 1 || role == 3 || role == 8 || role == 6) {
                        showReportingHead(1, true, userId);
                    } else if (role == 2 || role == 7) {
                        showReportingHead(2, true, userId);
                    } else if (role == 5) {
                        showReportingHead(5, true, userId);
                    }
                    $('#id_editReportingHead').val(reportTo);
                } else {
                    $("#editReportingHeadDiv").hide();
                }
                $('#pleaseWaitDialog').modal('hide');
                /// App.init();
                App.initUniform();
                App.callHandleScrollers();
                App.fixContentHeight();
                jQuery('.tooltips').tooltip();
            }   // added by manohar
            $('.selectpicker').selectpicker({
                style: 'btn-default',
                size: 4
            });
        } });
}


/**
 * @added by jyoti
 * @param {type} source_id
 * @param {type} userId
 * @param {type} userEmail
 * @param {type} password
 * @returns {undefined}
 */
function editUserActionBtn(source_id, userId, userEmail, password) {
    
    if (source_id == 'editUserContinue') {
        $('#edit_territory_tab_1_2').trigger('click');

    } else if (source_id == 'edit_territory_tab_1_2') {
        $('#editUserContinue').html('Save');
        $('#editUserContinue').attr('onclick', 'editUser(\'' + userId + '\',\'' + userEmail + '\',\'' + password + '\')');

    } else if (source_id == 'edit_general_tab_1_1') {
        $('#editUserContinue').html('Continue');
        $('#editUserContinue').attr('onclick', 'editUserActionBtn(this.id,\'' + userId + '\',\'' + userEmail + '\',\'' + password + '\')');
        
    }
}

function editUser(userId, userEmail, originalPassword) {

 //added by manohar   
    var emp_code=document.getElementById("edit_id_empcode").value.trim();
//    if (emp_code.length > 100) {
//        fieldSenseTosterError("Employee code should not be more than 100 letters", true);
//        return false;
//    }
    // ended by manohar
    var firstName = document.getElementById("id_userFnm").value.trim();
    var lastName = document.getElementById("id_userLnm").value.trim();
    var designation = document.getElementById("id_userDesignation").value.trim();
    var password = document.getElementById("id_password").value.trim();
    var cPassword = document.getElementById("id_cpassword").value.trim();
    var userGender = $('input[name=optionsRadios3]:checked', '#edit-userDetails').val();
    var mobile = document.getElementsByName("name_mobile_number")[0].value.trim();
    var userAccuracy = document.getElementById("mask_accuracy").value;
    var userCheckInRadius = document.getElementById("mask_check_in_radius").value;
    var allowTimeout = 0;
    var valueAllow = document.getElementById("id_allowtimeout_edit").checked;
    if (valueAllow == true) {
        allowTimeout = 1;
    }
    // added by jyoti
    var allowOffline_edit1 = 1;
    var valueAllowOffline_edit = document.getElementById("id_allowoffline_edit").checked;
    if (valueAllowOffline_edit == false) {
        allowOffline_edit1 = 0;
    }
    var active = document.getElementById("id_userAct").checked;
    var all = '';
    var role;  // modified by manohar
    $('#id_editRole  > option:selected').each(function () {
        all += $(this).val();
    });
    if (all.length == 3)
    {
        var admin = all.substring(0, 1);
        var account = all.substring(1, 2);
        var onfield = all.substring(2, 3);
        role = 8;
    } else if (all.length == 2)
    {
        var role1 = all.substring(0, 1);
        var role2 = all.substring(1, 2);
        if (role1 == 1)
        {
            if (role1 == 1 && role2 == 2)
            {
                role = 3;
            } else if (role1 == 1 && role2 == 5)
            {
                role = 6;
            }
        } else
        {
            role = 7;
        }
    } else
    {
        role = all;
    }
    var reportTo = document.getElementById("id_editReportingHead").value.trim();
    var passActive = document.getElementById("id_showPass").checked;
    var notificationEmail = $("#id_notification_email_edit").is(':checked');
    var notificationMobile = $("#id_notification_mobile_edit").is(':checked');
     if (role == 0) {
        fieldSenseTosterError("Please select user role .", true);
        return false;
    }
    if (topIdInHirarachy != userId && reportTo == 0) {
        fieldSenseTosterError("Please select Reporting head .", true);
        return false;
    }
    /*if (topIdInHirarachy != userId && !validateReportingHead(role,reportTo))
     {
     fieldSenseTosterError("Change Reporting head to Admin or Specific Role User", true);
     return false;
     }*/
    /*if (chkReportingHead(userId,reportTo))
     {
     fieldSenseTosterError("Select Valid Reporting Head", true);
     return false;
     }*/
    if (firstName.trim().length == 0) {
        fieldSenseTosterError("First Name cannot be empty", true);
        return false;
    }
    if (firstName.trim().length > 50) {
        fieldSenseTosterError("First name can not be more than 50 characters", true);
        return false;
    }
    if (lastName.trim().length != 0) {
        if (lastName.trim().length > 50) {
            fieldSenseTosterError("Last name can not be more than 50 characters", true);
            return false;
        }
    }
    if (designation.trim().length == 0) {
        fieldSenseTosterError("Please enter designation ", true);
        return false;
    }
    if (designation.trim().length > 50) {
        fieldSenseTosterError("Designation can not be more than 50 characters", true);
        return false;
    }
    if (password.trim().length == 0) {
        fieldSenseTosterError("Password cannot be empty", true);
        return false;
    }
    if (password.trim().length > 20) {
        fieldSenseTosterError("Password can not be more than 20 characters", true);
        return false;
    }
    if (password.trim().length < 8) {
        fieldSenseTosterError("Password can not be less than 8 characters", true);
        return false;
    }
    if (mobile.trim().length == 0) {
        fieldSenseTosterError("Mobile No cannot be blank", true);
        return false;
    }
    var isphNo1 = /^\d+$/.test(mobile);
    if (!isphNo1) {
        fieldSenseTosterError("Invalid Mobile number.", true);
        return false;
    }
    if (mobile.trim().length > 20) {
        fieldSenseTosterError("Mobile number should not be more than 20 digits", true);
        return false;
    }

    if (userAccuracy.trim().length == 0) {
        fieldSenseTosterError("Accuracy cannot be blank", true);
        return false;
    }

    if (userAccuracy.trim() <= 0) {
        fieldSenseTosterError("Accuracy should be greater than zero", true);
        return false;
    }

    var isvalidaccuray = /^\d+$/.test(userAccuracy);
    if (!isvalidaccuray) {
        fieldSenseTosterError("Invalid Accuracy.", true);
        return false;
    }

    if (userCheckInRadius.trim().length == 0) {
        fieldSenseTosterError("Check-In Radius cannot be blank", true);
        return false;
    }

    if (userCheckInRadius.trim() <= 0) {
        fieldSenseTosterError("Check-In Radius should be greater than zero", true);
        return false;
    }

    var isvalidcheck_in_radius = /^\d+$/.test(userCheckInRadius);
    if (!isvalidcheck_in_radius) {
        fieldSenseTosterError("Invalid Check-In Radius.", true);
        return false;
    }

    if (password !== cPassword) {
        fieldSenseTosterError("Confirm password must match with password", true);
        return false;
    }
   
    firstName = htmlEntities(firstName);
    lastName = htmlEntities(lastName);
    designation = htmlEntities(designation);
    var addTerritoryList = [];
    var deleteTerritoryList = [];
    for (var i = 0; i < userHasTerritoryList.length; i++) {
        var index = userTerritoryList.indexOf(userHasTerritoryList[i]);
        if (index < 0) {
            deleteTerritoryList.push(userHasTerritoryList[i]);
        }
    }
    for (var i = 0; i < userTerritoryList.length; i++) {
        var index = userHasTerritoryList.indexOf(userTerritoryList[i]);
        if (index < 0) {
            addTerritoryList.push(userTerritoryList[i]);
        }
    }

    var e = document.getElementById("Interval");
    var Interval = e.options[e.selectedIndex].value;
    
    

    var userObject;
    $('#pleaseWaitDialog').modal('show');
    if (passActive == true) {
        userObject = {
            "emp_code": emp_code,    //added by manohar   
            "id": userId,
            "firstName": firstName,
            "lastName": lastName,
            "designation": designation,
            "emailAddress": userEmail,
            "password": password,
            "mobileNo": mobile,
            "gender": userGender,
            "userAccuracy": userAccuracy,
            "checkInRadius": userCheckInRadius,
            "allowTimeout": allowTimeout,
            "allowOffline": allowOffline_edit1,
            "active": active,
            "role": role,
            "emailNotification": notificationEmail,
            "mobileNotification": notificationMobile,
            "updatedBy": loginUserId,
            "report_to": reportTo,
            "addTerritoryList": addTerritoryList,
            "deleteTerritoryList": deleteTerritoryList,
            // interval added nikhil
            "interval": e.options[e.selectedIndex].value
        }
    } else if (passActive == false) {
        userObject = {
            "emp_code": emp_code,     //added by manohar   
            "id": userId,
            "firstName": firstName,
            "lastName": lastName,
            "designation": designation,
            "emailAddress": userEmail,
            "password": originalPassword,
            "mobileNo": mobile,
            "gender": userGender,
            "userAccuracy": userAccuracy,
            "checkInRadius": userCheckInRadius,
            "allowTimeout": allowTimeout,
            "allowOffline": allowOffline_edit1,
            "active": active,
            "role": role,
            "emailNotification": notificationEmail,
            "mobileNotification": notificationMobile,
            "updatedBy": loginUserId,
            "report_to": reportTo,
            "addTerritoryList": addTerritoryList,
            "deleteTerritoryList": deleteTerritoryList,
            // by nik
            "interval": e.options[e.selectedIndex].value

        }
    }

    var jsonData = JSON.stringify(userObject);

    //alert(jsonData);

    $.ajax({
        type: "PUT",
        url: fieldSenseURL + "/user/user/" + userId,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            var userData = data.result;
            if (data.errorCode == '0000') {
                clevertap.event.push("Edit User", {
//                    "UserId": loginUserIdcookie,
//                    "User Name": loginUserNamecookie,
//                    "AccountId": accountIdcookie,
                    "Source": "Web",
                    "Account name": accountNamecookie,
                    "UserRolecookie": UserRolecookie,
                });
                $('#id_userFnm').val('');
                $('#id_userLnm').val('');
                $('#id_userEmail').val('');
                $('#id_password').val('');
                $('#id_cpassword').val('');
                document.getElementsByName("name_mobile_number")[0].value = '';
                $(".cls_editUser").modal('hide');
                var table = $('#sample_5').DataTable();
                table.draw(false);
                fieldSenseTosterSuccess(data.errorMessage, true);
                userDataForReportTo(); //update for reportTo
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                //  $(".cls_editUser").modal('hide');
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}

function deleteUser(userId, userNm) {
    userNm = userNm.replace(globalSplit, "'");
    //var deleteU = confirm("Are you sure you want to delete " + userNm);
    ///if (deleteU == true) {
    bootbox.dialog({
        message: "Are you sure you want to delete  the User ?",
        title: "Delete User",
        buttons: {
            yes: {
                label: "Yes",
                className: "btn-default",
                callback: function () {
                    $('#pleaseWaitDialog').modal('show');
                    $.ajax({
                        type: "DELETE",
                        url: fieldSenseURL + "/user/" + userId,
                        contentType: "application/json; charset=utf-8",
                        headers: {
                            "userToken": userToken
                        },
                        crossDomain: false,
                        cache: false,
                        dataType: 'json',
                        success: function (data) {
                            
                            if (data.errorCode == '0000') {
                                clevertap.event.push("Delete User", {
//                    "UserId": loginUserIdcookie,
//                    "User Name": loginUserNamecookie,
//                    "AccountId": accountIdcookie,
                    "Source": "Web",
                    "Account name": accountNamecookie,
                    "UserRolecookie": UserRolecookie,
                });
                                //$('#pleaseWaitDialog').modal('hide');
                                fieldSenseTosterSuccess(data.errorMessage, true);
                                var table = $('#sample_5').DataTable();
                                table.draw(false);
                            } else {
                                $('#pleaseWaitDialog').modal('hide');
                                fieldSenseTosterError(data.errorMessage, true);
                                FieldSenseInvalidToken(data.errorMessage);
                            }
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            $('#pleaseWaitDialog').modal('hide');
                            alert("in error:" + thrownError);
                        }
                    });
                }
            },
            no: {
                label: "No",
                className: "btn-default",
                callback: function () {
                    //$('#pleaseWaitDialog').modal('hide');
                    //return false;
                }
            }
        }
    });
    // }
}

function viewUser(userId) {
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/user/" + userId,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        async: true,
        success: function (data) {
            if (data.errorCode == '0000') {
                $('#id_viewUserDisplay').html('');
                var userData = data.result;
                var userId = userData.id;
                var userFnm = userData.firstName;
                var userLnm = userData.lastName;
                var userEmail = userData.emailAddress;
                var userMobile = userData.mobileNo;
                var userActive = userData.active;
                var userGender = userData.gender;
                var userDesignation = userData.designation;

                var userAccuracy = userData.userAccuracy;
                var userCheckInRadius = userData.userCheckInRadius;
                var allowTimeout = userData.allowTimeout;

                var r = userData.role;
                var role;
                if (r == 1) {
                    role = "Admin";
                } else if (r == 2) {
                    role = "Team Lead";
                } else if (r == 3) {
                    role = "Team Member";
                }
                else if (r == 5) {
                    role = "On-field Personnel";
                }
                var gender = '';
                if (userGender == 0) {
                    gender = "Female";
                } else if (userGender == 1) {
                    gender = "Male";
                }
                var userTemplate = '';
                userTemplate += '<form class="form-horizontal" role="form">';
                userTemplate += '<div class="form-body">';
                userTemplate += '<div class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">First Name</label>';
                userTemplate += '<label class="col-md-6 control-label">' + userFnm + '</label>';
                userTemplate += '</div>';
                userTemplate += '<div class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">Last Name</label>';
                userTemplate += '<label class="col-md-6 control-label">' + userLnm + '</label>';
                userTemplate += '</div>';
                userTemplate += '<div class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">Designation</label>';
                userTemplate += '<label class="col-md-6 control-label">' + userDesignation + '</label>';
                userTemplate += '</div>';
                userTemplate += '<div class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">Role</label>';
                userTemplate += '<label class="col-md-6 control-label">' + role + '</label>';
                userTemplate += '</div>';
                userTemplate += '<div class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">Email</label>';
                userTemplate += '<label class="col-md-6 control-label">' + userEmail + '</label>';
                userTemplate += '</div>';
                userTemplate += '<div class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">Mobile No.</label>';
                userTemplate += '<label class="col-md-6 control-label">' + userMobile + '</label>';
                userTemplate += '</div>';
                userTemplate += '<div class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">Gender</label>';
                userTemplate += '<label class="col-md-6 control-label">' + gender + '</label>';
                userTemplate += '</div>';

                userTemplate += '<div class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">Accuracy</label>';
                userTemplate += '<label class="col-md-6 control-label">' + userAccuracy + '</label>';
                userTemplate += '</div>';

                userTemplate += '<div class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">Check-In Radius</label>';
                userTemplate += '<label class="col-md-6 control-label">' + userCheckInRadius + '</label>';
                userTemplate += '</div>';

                userTemplate += '<div class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">Allow Timeout</label>';
                userTemplate += '<div class="col-md-6">';
                userTemplate += '<div class="checkbox-list">';
                if (allowTimeout == 1) {
                    userTemplate += '<input type="checkbox" checked disabled >';
                } else {
                    userTemplate += '<input type="checkbox" disabled>';
                }
                userTemplate += '</div>';
                userTemplate += '</div>';
                userTemplate += '</div>';

                userTemplate += '<div class="form-group">';
                userTemplate += '<label class="col-md-6 control-label">Active</label>';
                userTemplate += '<div class="col-md-6">';
                userTemplate += '<div class="checkbox-list">';
                if (userActive == true) {
                    userTemplate += '<input type="checkbox" checked disabled>';
                } else {
                    userTemplate += '<input type="checkbox" disabled>';
                }
                userTemplate += '</div>';
                userTemplate += '</div>';
                userTemplate += '</div>';
                userTemplate += '</div>';
                userTemplate += '</form>';

                $('#id_viewUserDisplay').append(userTemplate);
                $('#pleaseWaitDialog').modal('hide');
            }

        }
    });
}

/*
 function leftSideMenu() {
 var leftSideMenuTemplate = '';
 leftSideMenuTemplate += '<ul class="page-sidebar-menu">';
 leftSideMenuTemplate += '<li class="start active">';
 leftSideMenuTemplate += '<span class="title">FILTERS</span>';
 leftSideMenuTemplate += '</li>';
 leftSideMenuTemplate += '<li class="opt">';
 leftSideMenuTemplate += '<span class="title">Status';
 leftSideMenuTemplate += '</span>';
 leftSideMenuTemplate += '<div multiple class="form-control multiselect">';
 leftSideMenuTemplate += '<span class="cls_addRecentCustomerFilter">';
 leftSideMenuTemplate += '<label><input type="checkbox" name="option[]" value="1" id="id_activeSearch" onChange="searchUserWithFilter();return false;" />Active</label>';
 leftSideMenuTemplate += '<label><input type="checkbox" name="option[]" value="2" id="id_deactiveSearch" onChange="searchUserWithFilter();return false;"/>Inactive</label>';
 leftSideMenuTemplate += '</span>';
 leftSideMenuTemplate += '</div>';
 leftSideMenuTemplate += '</li>';
 leftSideMenuTemplate += '<a href="#" class="btn btn-warning" onclick="showUser()">Clear Filters</a>';
 leftSideMenuTemplate += '<li class="last">';
 leftSideMenuTemplate += '</li>';
 leftSideMenuTemplate += '</ul>';
 $('#id_leftMenuUser').html(leftSideMenuTemplate);
 //App.init();
 App.initUniform();
 //App.callHandleScrollers();
 App.fixContentHeight();  
 //jQuery('.tooltips').tooltip();
 getAllActiveTerritoriesToAdd();
 }
 */

var activeTerritoryData;
var parentTerritoryCatagories = [];
var leftside_parentTerritoryCatagories = [];
var leftside_fullTerriCategories = new Array();
var fullTerriCategories = [];
//var leftside_userTerritoryList=[];

function leftSideMenu() {
    var territory_url = fieldSenseURL + "/territoryCategory/active/territory";
//if(role!=1){
//	territory_url=fieldSenseURL + "/territoryCategory/active/territory/user/"+loginUserId;
//}

    $.ajax({
        type: "GET",
        url: territory_url,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        async: false,
        success: function (dataTerritory) {
            var cateArray = [];
            var territoryData = dataTerritory.result;
            // activeTerritoryData=territoryData; // to be use in later like on edit customer page
            var territoryLength = territoryData.length;
            var leftSideMenuTemplate = '';
            leftSideMenuTemplate += '<ul class="page-sidebar-menu">';
            leftSideMenuTemplate += '<li class="start active">';
            leftSideMenuTemplate += '<span class="title">FILTERS</span>';
            leftSideMenuTemplate += '</li>';

            leftSideMenuTemplate += '<li class="opt">';
            leftSideMenuTemplate += '<span class="title">Status';
            leftSideMenuTemplate += '<a href="javascript:;" class="cl cls_clearStatus" style="display:none;" onClick="clearStatusUsers();">Clear</a>';
            leftSideMenuTemplate += '</span>';
            leftSideMenuTemplate += '<div multiple class="form-control multiselect">';
            leftSideMenuTemplate += '<span class="cls_addRecentCustomerFilter">';
            leftSideMenuTemplate += '<label><input type="checkbox" name="option[]" value="1" id="id_activeSearch" onChange="searchUserWithFilter();return false;" />Active</label>';
            leftSideMenuTemplate += '<label><input type="checkbox" name="option[]" value="2" id="id_deactiveSearch" onChange="searchUserWithFilter();return false;"/>Inactive</label>';
            leftSideMenuTemplate += '</span>';
            leftSideMenuTemplate += '</div>';
            leftSideMenuTemplate += '</li>';

            leftSideMenuTemplate += '<li class="opt">';
            leftSideMenuTemplate += '<span class="title">Territory';
            leftSideMenuTemplate += '<a href="javascript:;" class="cl cls_clearTerritory" style="display:none;" onClick="clearTerritoryUsers();">Clear</a>';
            leftSideMenuTemplate += '</span>';
            leftSideMenuTemplate += '<div multiple class="form-control multiselect" id="territorytree">';

            leftSideMenuTemplate += '</div>';
            leftSideMenuTemplate += '</li>';

            leftSideMenuTemplate += '<a href="#" class="btn btn-warning" onclick="showUser()">Clear Filters</a>';
            leftSideMenuTemplate += '<li class="last">';
            leftSideMenuTemplate += '</li>';
            leftSideMenuTemplate += '</ul>';
            $('#id_leftMenuUser').html(leftSideMenuTemplate);
            for (var i = 0; i < territoryData.length; i++) {
                var id = territoryData[i].id;
                var parentId = territoryData[i].parentCategory;
                var categoryName = territoryData[i].categoryName;
                var hasChild = territoryData[i].hasChild;
                var catCsv = territoryData[i].categoryPositionCsv;
                if (territoryData[i].parentCategory == 0) {
                    if (territoryData[i].hasChild == 0) {
                        leftside_parentTerritoryCatagories.push({"id": id, "categoryName": categoryName, "categoryPositionCsv": catCsv});
                    } else {
                        leftside_parentTerritoryCatagories.push({"id": id, "categoryName": categoryName, "categoryPositionCsv": catCsv, "list": [{"id": id, "categoryName": "unknown"}]});
                    }
                } else {
                    leftside_fullTerriCategories.push({"parentCat": parentId, "id": id, "categoryName": categoryName, "hasChild": hasChild, "categoryPositionCsv": catCsv});
                }

                cateArray.push({"id": id, "parentid": parentId, "name": "<input type=\"checkbox\"  name=\"option[]\" value=\"" + categoryName + "\" id=\"id_Territory_" + id + "\" onchange=\"EnableCheckboxesTerritory('" + id + "','" + categoryName + "','" + hasChild + "','" + catCsv + "');searchUserWithFilter();return false;\">" + categoryName});

            }
            if (territoryData.length != 0) {
                var source =
                        {
                            dataType: "json",
                            dataFields: [
                                {name: "name", type: "string"},
                                {name: "id", type: "number"},
                                {name: "parentid", type: "number"},
                            ],
                            hierarchy:
                                    {
                                        keyDataField: {name: 'id'},
                                        parentDataField: {name: 'parentid'}
                                    },
                            id: 'id',
                            localData: cateArray
                        };
                var dataAdapter = new $.jqx.dataAdapter(source);
                // create Tree Grid
                $("#territorytree").jqxTreeGrid(
                        {
                            width: 180,
                            source: dataAdapter,
                            sortable: true,
                            //pageable: true,
                            pagerMode: 'advanced',
                            ready: function () {
                                $("#territorytree").jqxTreeGrid('expandRow', '2');
                                $("#territorytree").removeClass("jqx-grid ");
                                $("#contentterritorytree > .jqx-grid-header").hide();
                                $("#tableterritorytree > tbody > tr > td").each(function () {
                                    $(this).removeClass("jqx-grid-cell");
                                });
                                App.initUniform();
                                App.fixContentHeight();
                                App.callHandleScrollers();
                                jQuery('.tooltips').tooltip();
                            },
                            columns: [
                                {text: ' ', dataField: "name"},
                            ]
                        });
                $('#territorytree').on('rowCollapse', function (event) {
                    $("#territorytree").removeClass("jqx-grid ");
                    $("#contentterritorytree > .jqx-grid-header").hide();
                    $("#tableterritorytree > tbody > tr > td").each(function () {
                        $(this).removeClass("jqx-grid-cell");
                    });
                    App.initUniform();
                    App.fixContentHeight();
                    App.callHandleScrollers();
                    jQuery('.tooltips').tooltip();
                    for (var i = 0; i < leftside_userTerritoryList.length; i++) {
                        if (leftside_userTerritoryList[i].checked == true) {
                            $("#id_Territory_" + leftside_userTerritoryList[i].catid).parent().attr("class", "checked");
                            $("#id_Territory_" + leftside_userTerritoryList[i].catid).attr('checked', true);
                        }
                    }

                });
                $('#territorytree').on('rowExpand', function (event) {
                    $("#territorytree").removeClass("jqx-grid ");
                    $("#contentterritorytree > .jqx-grid-header").hide();
                    $("#tableterritorytree > tbody > tr > td").each(function () {
                        $(this).removeClass("jqx-grid-cell");
                    });
                    App.initUniform();
                    App.fixContentHeight();
                    App.callHandleScrollers();
                    jQuery('.tooltips').tooltip();
                    for (var i = 0; i < leftside_userTerritoryList.length; i++) {
                        if (leftside_userTerritoryList[i].checked == true) {
                            $("#id_Territory_" + leftside_userTerritoryList[i].catid).parent().attr("class", "checked");
                            $("#id_Territory_" + leftside_userTerritoryList[i].catid).attr('checked', true);
                        }
                    }
                });
                $("#territorytree").removeClass("jqx-grid ");
                $("#contentterritorytree > .jqx-grid-header").hide();
                $("#tableterritorytree > tbody > tr > td").each(function () {
                    $(this).removeClass("jqx-grid-cell");
                });
            }
            App.initUniform();
            App.fixContentHeight();
            App.callHandleScrollers();
            jQuery('.tooltips').tooltip();
            getAllActiveTerritoriesToAdd();
        }
    });
}
//    });
//}

var territoryCatData;
function getAllActiveTerritoriesToAdd() {
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/territoryCategory/active/territory",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        success: function (data) {
            territoryCatData = data.result;
        },
        error: function (xhr, ajaxOptions, thrownError) {
            $('#pleaseWaitDialog').modal('hide');
            alert("in error:" + thrownError);
        }
    });
}


function searchUserWithFilter() {

    var index;
    var territoryusersSub = [];
    var userStatus = new Array();
    var Active = document.getElementById("id_activeSearch").checked;
    if (Active) {
        index = userStatus.indexOf("Active");
        if (index < 0) {
            userStatus.push("Active");
        }
    } else {
        index = userStatus.indexOf("Active");
        if (index > -1) {
            userStatus.splice(index, 1);
        }
    }
    var Inactive = document.getElementById("id_deactiveSearch").checked;
    if (Inactive) {
        index = userStatus.indexOf("Inactive");
        if (index < 0) {
            userStatus.push("Inactive");
        }
    } else {
        index = userStatus.indexOf("Inactive");
        if (index > -1) {
            userStatus.splice(index, 1);
        }
    }

    if (userStatus.length == 0) {
        $('.cls_clearStatus').css("display", "none");
    } else {
        $('.cls_clearStatus').css("display", "block");
    }

    if (leftside_userTerritoryList.length == 0) {
        $('.cls_clearTerritory').css("display", "none");
    } else {
        $('.cls_clearTerritory').css("display", "block");
    }

    for (var i = 0; i < selectedTerritories.length; i++) {
        territoryusersSub.push("territory_value=" + selectedTerritories[i]);
    }


    var start = 0;
    var length = 20;
    myTable = $("#sample_5").DataTable();
    if (myTable != null)
        myTable.destroy();
    //$('#pleaseWaitDialog').modal('show');
    $('#id_userShow').html('');
    var userTemplate = '';
    userTemplate += '<table class="table table-striped table-bordered table-hover" id="sample_5">';
    userTemplate += '<thead>';
    userTemplate += '<tr>';
    userTemplate += '<th>';
    userTemplate += 'Emp. Code';    //added by manohar
    userTemplate += '</th>';
    userTemplate += '<th>';
    userTemplate += 'Name';
    userTemplate += '</th>';
    userTemplate += '<th>';
    userTemplate += 'Email';
    userTemplate += '</th>';
    userTemplate += '<th>';
    userTemplate += 'Mobile';
    userTemplate += '</th>';
    userTemplate += '<th class="hidden-xs">';
    userTemplate += 'Gender';
    userTemplate += '</th>';
    userTemplate += '<th>';
    userTemplate += 'Active';
    userTemplate += '</th>';
    userTemplate += '<th>';
    userTemplate += 'Attendance Status';
    userTemplate += '</th>';
    userTemplate += '<th>';
    userTemplate += 'DateTime';
    userTemplate += '</th>';
    userTemplate += '<th>';
    userTemplate += '</th>';
    userTemplate += '</tr>';
    userTemplate += '</thead>';
    userTemplate += '<tbody>';
    userTemplate += '</tbody>';
    userTemplate += '</table>';
    $('#id_userShow').html(userTemplate);
    var inboxtable = $('#sample_5').dataTable({
        "bServerSide": true,
        "bDestroy": true,
        "ajax": {
            "url": fieldSenseURL + "/search/user/filtering",
            "type": "GET",
            headers: {
                "userToken": userToken
            },
            contentType: "application/json; charset=utf-8",
            crossDomain: false,
            "data": {
                "userStatus": "userStatus_values:" + userStatus,
                "territory": "territory_values:" + territoryusersSub,
            },
            cache: false,
            dataType: 'json',
            async: true,
            error: function (data) {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.responseJSON.errorMessage, true);
                //logout();
            }
        },
        "iDisplayStart": start,
        // set the initial value
        "iDisplayLength": length, // It will be by default page width set by user
        "sPaginationType": "bootstrap_full_number",
        "bProcessing": false,
        "aLengthMenu": [
            [5, 10, 15, 20, 50, 100],
            [5, 10, 15, 20, 50, 100] // change per page values here
        ],
        "oLanguage": {
            // "sProcessing":"Loading",
            "sEmptyTable": "No data available in table",
            "sLengthMenu": "_MENU_ <span id ='id_totalCustRecords'></span>",
            //"sInfo": "Showing _START_ to _END_ of _TOTAL_ Mails",
            "oPaginate": {
                "sPrevious": "Prev",
                "sNext": "Next"
            }
        },
        "aoColumnDefs": [
            {"bSortable": false,
                "aTargets": [0, 3, 4, 5, 6, 7, 8]   // modified by manohar

            }
        ],
        "aoColumns": [// ahmed : here we populate the data in datatable that is returned using Ajax
             {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {   //added by manohar
                    return full.emp_code;
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.firstName + ' ' + full.lastName;
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.emailAddress;
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.mobileNo;
                }
            },
            {"mData": null,
                "sClass": "hidden-xs",
                "mRender": function (data, type, full) {
                    var userGender = full.gender;
                    var gender;
                    if (userGender == 0) {
                        gender = "Female";
                    } else if (userGender == 1) {
                        gender = "Male";
                    }
                    return gender;
                }
            },
            {"mData": null,
                "sClass": "hidden-xs",
                "mRender": function (data, type, full) {
                    var userActive = full.active;
                    if (userActive == 0) {
                        return'<input type="checkbox" disabled >';
                    } else if (userActive == 1) {
                        return '<input type="checkbox" checked disabled >';
                    }
                }
            },
            {"mData": null,
                "sClass": "hidden-xs",
                "mRender": function (data, type, full) {
                    var ateendanceStatus = full.punchStatus;
                    if (ateendanceStatus == "NotPunchIn") {
                        return '';
                    } else if (ateendanceStatus == "PunchIn") {
                        return ateendanceStatus;
                    } else if (ateendanceStatus == "PunchOut") {
                        return '' + ateendanceStatus + '   <button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Reset" onclick="resetAttendanceStatus(\'' + full.id + '\',\'' + full.attendanceId + '\')"><i class="fa fa-toggle-on"></i></button>';

                    }
                }
            },
            {"mData": null,
                "sClass": "hidden-xs",
                "mRender": function (data, type, full) {
                    var ateendanceStatusTime = full.punchStatusTime;
                    if (ateendanceStatusTime != "" && full.punchDate != "") {
                        var punchDate = full.punchDate.split('-');
                        var punchInSplit = ateendanceStatusTime.split(':');
                        var punchInDateJs = convertServerDateToLocalDate(punchDate[0], punchDate[1] - 1, punchDate[2], punchInSplit[0], punchInSplit[1]);
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
                        punchDate = d + ' ' + m + ' ' + y;
                        if (punchInMin < 10) {
                            punchInMin = '0' + punchInMin;
                        }
                        if (punchInHr < 12) {
                            if (punchInHr < 10) {
                                punchInHr = '0' + punchInHr;
                            }
                            ateendanceStatusTime = punchInHr + ':' + punchInMin + ' AM';
                        } else if (punchInHr == 12) {
                            ateendanceStatusTime = punchInHr + ':' + punchInMin + ' PM';
                        } else {
                            punchInHr = punchInHr - 12;
                            if (punchInHr < 10) {
                                punchInHr = '0' + punchInHr;
                            }
                            ateendanceStatusTime = punchInHr + ':' + punchInMin + ' PM';
                        }
                        var punchInMinuts = (punchInHr1 * 60) + punchInMin1;
                        ateendanceStatusTime = punchDate + " , " + ateendanceStatusTime;
                    }
                    return ateendanceStatusTime;
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    var userName = full.firstName + ' ' + full.lastName;
                    userName = userName.replace("'", globalSplit);
                    return '<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Edit" data-toggle="modal" href="#responsive2" onclick="editUserTemplate(' + full.id + ')"><i class="fa fa-edit"></i></button><button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Delete" onclick="deleteUser(\'' + full.id + '\',\'' + userName + '\')"><i class="fa fa-trash-o"></i></button>';
                }
            }
        ],
        "aaSorting": [],
        "bFilter": false,
        "preDrawCallback": function (settings) {
            $('#pleaseWaitDialog').modal('show');
        },
        "fnDrawCallback": function (oSettings) {
            if (oSettings.fnDisplayEnd() > 0) {
                $("#id_totalCustRecords").html("<b>" + (oSettings._iDisplayStart + 1) + "</b> - <b>" + oSettings.fnDisplayEnd() + "</b> of <b>" + oSettings.fnRecordsTotal() + "</b> records");
            }
            inboxtable.$('td').mouseover(function () {
                var sData = $(this).text();
                $(this).attr('title', sData);
            });
            App.initUniform();
            App.fixContentHeight();
            jQuery('.tooltips').tooltip();
            $('#pleaseWaitDialog').modal('hide');
        },
    });

    jQuery('#sample_5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
    jQuery('#sample_5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
    jQuery('#sample_5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown	

}


function showPassFileds() {
    var temp = document.getElementById("id_showPass").checked;
    if (temp == true) {
        $('#id_hideShowPass').css("display", "block");
    } else {
        $('#id_hideShowPass').css("display", "none");
    }
}

function addUserTemplate() {
    offlineStatusCheck();       // Added by jyoti, purpose - to check offline feature : if not enable, disable checkbox of allow offline
//    $(document).ajaxSuccess(function () {    // Added by jyoti - this ensures ajax call has finished, so proceed further	
        var addUserTemplate = '';
        addUserTemplate += '<div class="modal-dialog modal-wide">';
        addUserTemplate += '<div class="modal-content">';
        addUserTemplate += '<div class="modal-header">';
        addUserTemplate += '<button type="button" class="close" data-dismiss="modal"  aria-hidden="true"></button>';
        addUserTemplate += '<h4 class="modal-title">Add User Details</h4>';
        addUserTemplate += '</div>';
        addUserTemplate += '<form class="form-horizontal form-row-seperated" role="form">';
        addUserTemplate += '<div class="modal-body" id="act1">';
        addUserTemplate += '<ul class="nav nav-tabs">';
        addUserTemplate += '<li class="active">';
        addUserTemplate += '<a href="#tab_1_1" id="general_tab_1_1" onclick="addUserActionBtn(this.id)" data-toggle="tab">General Info</a>'; // edited by jyoti
        addUserTemplate += '</li>';
        addUserTemplate += '<li class="">';
        addUserTemplate += '<a href="#tab_1_2" id="territory_tab_1_2" onclick="addUserActionBtn(this.id)" data-toggle="tab">Territories</a>'; // edited by jyoti
        addUserTemplate += '</li>';
        addUserTemplate += '</ul>';
        addUserTemplate += '<div class="tab-content">';
        addUserTemplate += '<div class="tab-pane fade active in" id="tab_1_1">';
        addUserTemplate += '<div class="scroller" data-always-visible="1" data-rail-visible1="1">';
        addUserTemplate += '<div class="form-body">';
        addUserTemplate += '<div class="form-group">';
        addUserTemplate += '<label class="col-md-6 control-label">First Name</label>';
        addUserTemplate += '<div class="col-md-6">';
       addUserTemplate += '<input type="fname" class="form-control" value="" id="id_ufnm">';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';       
        addUserTemplate += '<div class="form-group">';
        addUserTemplate += '<label class="col-md-6 control-label">Last Name</label>';
        addUserTemplate += '<div class="col-md-6">';
        addUserTemplate += '<input type="lname" class="form-control" value="" id="id_ulnm">';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';           //added by manohar       
        addUserTemplate += '<div class="form-group">';
        addUserTemplate += '<label class="col-md-6 control-label">Emp. Code</label>';
        addUserTemplate += '<div class="col-md-6">';
        addUserTemplate += '<input type="empcode" class="form-control" value="" id="add_id_empcode">';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';       
        addUserTemplate += '<div class="form-group">';
        addUserTemplate += '<label class="col-md-6 control-label">Gender</label>';
        addUserTemplate += '<div class="col-md-6">';
        addUserTemplate += '<div class="radio-list">';
        addUserTemplate += '<label class="radio-inline">';
        addUserTemplate += '<input type="radio" name="optionsRadios" id="optionsRadios4" value="option1" class="cls_ugmale" > Male </label>';
        addUserTemplate += '<label class="radio-inline">';
        addUserTemplate += '<input type="radio" name="optionsRadios" id="optionsRadios5" value="option2" class="cls_ugfemale"> Female </label>';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';
        addUserTemplate += '<div class="form-group">';
        addUserTemplate += '<label class="col-md-6 control-label">Designation</label>';
        addUserTemplate += '<div class="col-md-6">';
        addUserTemplate += '<input type="lname" class="form-control" value="" id="id_designation">';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';
        addUserTemplate += '<div class="form-group">';
        addUserTemplate += '<label class="col-md-6 control-label">Role</label>';
        addUserTemplate += '<div class="col-md-6">';
//        addUserTemplate += '<select class="form-control" onchange ="showReportingHead(this.value)" id="id_role">';
//        addUserTemplate += '<option value="0">Select</option>';
//        addUserTemplate += '<option value="1">Admin</option>';
//        addUserTemplate += '<option value="2">Accounts</option>';
//        addUserTemplate += '<option value="5">On-field Personnel</option>';
//        addUserTemplate += '</select>';
        addUserTemplate += '<select class="form-control selectpicker" multiple onchange ="showReportingHead(this.value)" id="id_role">';
        addUserTemplate += '<option value="1" style="font-size:10pt">Admin</option>';
        addUserTemplate += '<option value="2" style="font-size:10pt">Accounts</option>';
        addUserTemplate += '<option value="5" style="font-size:10pt">On-field Personnel</option>';
        addUserTemplate += '</select>';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';
        addUserTemplate += '<div class="form-group">';
        addUserTemplate += '<label class="col-md-6 control-label">Reports To</label>';
        addUserTemplate += '<div class="col-md-6">';
        addUserTemplate += '<select class="form-control" id="id_ofAllSubordinate">';
        addUserTemplate += '<option value="0">Select</option>';
        addUserTemplate += '</select>';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';
        addUserTemplate += '<div class="form-group">';
        addUserTemplate += '<label class="col-md-6 control-label">Email</label>';
        addUserTemplate += '<div class="col-md-6">';
        addUserTemplate += '<input type="email" class="form-control" value="" id="id_uemail">';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';
        addUserTemplate += '<div class="form-group">';
        addUserTemplate += '<label class="col-md-6 control-label">Password</label>';
        addUserTemplate += '<div class="col-md-6">';
        addUserTemplate += '<input type="password" class="form-control" value="" id="id_upass">';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';
        addUserTemplate += '<div class="form-group">';
        addUserTemplate += '<label class="col-md-6 control-label">Confirm Password</label>';
        addUserTemplate += '<div class="col-md-6">';
        addUserTemplate += '<input type="password" class="form-control" value="" id="id_ucpass">';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';
        addUserTemplate += '<div class="form-group">';
        addUserTemplate += '<label class="col-md-6 control-label">Mobile No.</label>';
        addUserTemplate += '<div class="col-md-6" id="mob">';
        addUserTemplate += '<input name="id_cls_mobile_name" class="form-control" id="mask_phone" type="text" value=""/>';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';
        addUserTemplate += '<div class="form-group" id="add_user_accuracy_div">';
        addUserTemplate += '<label class="col-md-6 control-label">Accuracy (In Meters)</label>';
        addUserTemplate += '<div class="col-md-6">';
        addUserTemplate += '<select class="form-control" id="id_cls_accuray">';
        addUserTemplate += '<option value=50>50</option>';
        addUserTemplate += '<option value=100>100</option>';
        addUserTemplate += '<option value=250>250</option>';
        addUserTemplate += '<option value=500 selected>500</option>';
        addUserTemplate += '<option value=1000>1000</option>';
        addUserTemplate += '<option value=2500>2500</option>';
        addUserTemplate += '<option value=5000>5000</option>';
        addUserTemplate += '</select>';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';
        addUserTemplate += '<div class="form-group" id="add_check_in_radius_div">';
        addUserTemplate += '<label class="col-md-6 control-label">Check-In Radius (In Meters)</label>';
        addUserTemplate += '<div class="col-md-6">';
        addUserTemplate += '<select class="form-control" id="id_cls_check_in_radius">';
        addUserTemplate += '<option value=50>50</option>';
        addUserTemplate += '<option value=100>100</option>';
        addUserTemplate += '<option value=250>250</option>';
        addUserTemplate += '<option value=500 selected>500</option>';
        addUserTemplate += '<option value=1000>1000</option>';
        addUserTemplate += '<option value=2500>2500</option>';
        addUserTemplate += '<option value=5000>5000</option>';
        addUserTemplate += '</select>';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';
        addUserTemplate += '<div class="form-group">';
        addUserTemplate += '<label class="col-md-6 control-label">Active</label>';
        addUserTemplate += '<div class="col-md-6">';
        addUserTemplate += '<div class="checkbox-list">';
        addUserTemplate += '<input type="checkbox" id="id_uact">';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';
        addUserTemplate += '<div class="form-group">';
        addUserTemplate += '<label class="col-md-6 control-label">Allow Timeout</label>';
        addUserTemplate += '<div class="col-md-6">';
        addUserTemplate += '<div class="checkbox-list">';
        addUserTemplate += '<input type="checkbox" id="id_allowtimeout">';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';
        addUserTemplate += '<div class="form-group">';
        addUserTemplate += '<label class="col-md-6 control-label">Allow Offline</label>';
        addUserTemplate += '<div class="col-md-6">';
        addUserTemplate += '<div class="checkbox-list">';
        if (offlineEnable == true) {
            addUserTemplate += '<input type="checkbox" id="id_allowoffline">';
        } else {
//            addUserTemplate+='<label style="display: block; color:#D3D3D3;"> <input type="checkbox" id="id_allowoffline" disabled > Offline feature disabled </label>';
            addUserTemplate += ' <input type="checkbox" id="id_allowoffline" disabled > ';

        }
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';
        addUserTemplate += '<div class="form-group">';
        addUserTemplate += '<label class="col-md-6 control-label">Notification</label>';
        addUserTemplate += '<div class="col-md-6">';
        addUserTemplate += '<div class="checkbox-list">';
        addUserTemplate += '<input type="checkbox" id="id_notification_email"   disabled/> <label class="control-label" style="margin-left: 30px;margin-top: -25px!important">Email</label>';
        addUserTemplate += '<input type="checkbox" id="id_notification_mobile"  /> <label class="control-label" style="margin-left: 30px;margin-top: -25px!important">Mobile</label>';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';
        addUserTemplate += '<div class="tab-pane fade" id="tab_1_2">';
        //addUserTemplate+='<div class="scroller" data-always-visible="1" data-rail-visible1="1">';
        addUserTemplate += '<div id="add_treeGrid"></div>';
        //addUserTemplate+='</div>';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';
        addUserTemplate += '<div class="modal-footer">';
//        addUserTemplate += '<button type="button" class="btn btn-info" onclick="addUser()">Save</button>'; // commented by jyoti
        addUserTemplate += '<button type="button" id="addUserContinue" class="btn btn-info" onclick="addUserActionBtn(this.id)" >Continue</button>'; // added by jyoti
        addUserTemplate += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
        addUserTemplate += '</div>';
        addUserTemplate += '</form>';
        addUserTemplate += '</div>';
        addUserTemplate += '</div>';
        //addUserTemplate+='</div>';	
        $("#responsive").html(addUserTemplate);
        App.initUniform();
        App.fixContentHeight();
        App.callHandleScrollers();
        jQuery('.tooltips').tooltip();
        $('#id_ufnm').val('');
        $('#id_ulnm').val('');
        $('#id_designation').val('');
        $('#id_uemail').val('');
        $('#id_upass').val('');
        $('#id_ucpass').val('');
        $('#id_role').val(0);
        $('#id_ofAllSubordinate').val(0);
        $("#id_cls_mobile_name").val(500);
        $("#id_cls_accuray").val(500);
        $('input[name=optionsRadios]').attr('checked', false);
        $("#id_uact").attr("checked", true);
        $("#id_uact").parent().attr("class", "checked");

        $("#id_allowtimeout").attr("checked", false);
        $("#id_allowtimeout").parent().removeClass("checked");
        $("#id_allowoffline").attr("checked", true);
        $("#id_allowoffline").parent().attr("class", "checked");
        $("#id_notification_email").attr("checked", true);
        $("#id_notification_email").parent().attr("class", "checked");
        $("#id_notification_mobile").attr("checked", true);
        $("#id_notification_mobile").parent().attr("class", "checked");
        $('#add_id_empcode').val('');  // added by manohar
        makeTreeGridViewForTerritories();
         $('.selectpicker').selectpicker({  //added by manohar
            style: 'btn-default',
            size: 4
        });
//    });
}

/**
 * @added by jyoti
 * @param {type} source_id
 * @returns {undefined}
 */
function addUserActionBtn(source_id) {

    if (source_id == 'addUserContinue') {
        $('#territory_tab_1_2').trigger('click');

    } else if (source_id == 'territory_tab_1_2') {
        $('#addUserContinue').html('Save');
        $('#addUserContinue').attr('onclick', 'addUser()');

    } else if (source_id == 'general_tab_1_1') {
        $('#addUserContinue').html('Continue');
        $('#addUserContinue').attr('onclick', 'addUserActionBtn(this.id)');
        
    }
}


function clearAddUser() {
    $('#add_id_empcode').val('');  // added by manohar
    $('#id_ufnm').val('');
    $('#id_ulnm').val('');
    $('#id_designation').val('');
    $('#id_uemail').val('');
    $('#id_upass').val('');
    $('#id_ucpass').val('');
    $('#id_role').val(0);
    $('#id_ofAllSubordinate').val(0);
    $("#id_cls_mobile_name").val(500);
    $("#id_cls_accuray").val(500);
    $('input[name=optionsRadios]').attr('checked', false);
    $("#id_uact").attr("checked", true);
    $("#id_uact").parent().attr("class", "checked");

    $("#id_allowtimeout").attr("checked", false);
    $("#id_allowtimeout").parent().removeClass("checked");

    $("#id_notification_email").attr("checked", true);
    $("#id_notification_email").parent().attr("class", "checked");
    $("#id_notification_mobile").attr("checked", true);
    $("#id_notification_mobile").parent().attr("class", "checked");

    var cateArray = [];
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/territoryCategory/active/territory",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        success: function (data) {
            var territoryData = data.result;
            if (data.errorCode == '0000') {
                for (var i = 0; i < territoryData.length; i++) {
                    var id = territoryData[i].id;
                    var parentId = territoryData[i].categoryPositionCsv;
                    var parentPos = parentId;
                    if (id == parentId) {
                        parentId = 0;
                    } else {
                        parentId = parentId.split(",")[1];
                    }
                    var categoryName = territoryData[i].categoryName;

                    cateArray.push({"id": id, "parentid": parentId, "name": categoryName, "actions": "<input type='checkbox' class='checkboxes'>"});

                }
                if (territoryData.length != 0) {
                    var source =
                            {
                                dataType: "json",
                                dataFields: [
                                    {name: "name", type: "string"},
                                    //{ name: "quantity", type: "number" },
                                    {name: "id", type: "number"},
                                    {name: "parentid", type: "number"},
                                    //{ name: "active", type: "string" },
                                    // { name: "date", type: "string" },
                                    {name: "actions", type: "string"}
                                ],
                                hierarchy:
                                        {
                                            keyDataField: {name: 'id'},
                                            parentDataField: {name: 'parentid'}
                                        },
                                id: 'id',
                                localData: cateArray
                            };
                    var dataAdapter = new $.jqx.dataAdapter(source);
                    // create Tree Grid
                    $("#treeGrid").jqxTreeGrid(
                            {
                                //width: 520,
                                source: dataAdapter,
                                sortable: true,
                                //pageable: true,
                                pagerMode: 'advanced',
                                ready: function () {
                                    //$("#territorytree").jqxTreeGrid('expandRow', '2');
                                    App.initUniform();
                                    App.fixContentHeight();
                                    jQuery('.tooltips').tooltip();
                                },
                                columns: [
                                    {text: 'Category Name', dataField: "name", width: 400},
                                    //{ text: '', dataField: "checkbox",  width: 160},
                                    {text: '   ', dataField: "actions", cellsFormat: "c2"},
                                ]
                            });
                    $('#treeGrid').on('rowCollapse', function (event) {
                        App.initUniform();
                        App.fixContentHeight();
                        jQuery('.tooltips').tooltip();
                    });
                    $('#treeGrid').on('rowExpand', function (event) {
                        App.initUniform();
                        App.fixContentHeight();
                        jQuery('.tooltips').tooltip();
                    });
                }
                App.initUniform();
                App.fixContentHeight();
                jQuery('.tooltips').tooltip();
                $('#pleaseWaitDialog').modal('hide');
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                //$(".cls_editUser").modal('hide');
                FieldSenseInvalidToken(data.errorMessage);
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            $('#pleaseWaitDialog').modal('hide');
            alert("in error:" + thrownError);
        }
    });
    $.uniform.update();
}
function downloadSampleUsersFile() {
//    clevertap.event.push("Import user", {
////                    "UserId": loginUserIdcookie,
////                    "User Name": loginUserNamecookie,
////                    "AccountId": accountIdcookie,
//                    "Source": "Web",
//                    "Account name": accountNamecookie,
//                    "UserRolecookie": UserRolecookie,
//                });
    var url = imageURLPath + "users.csv";
    var temp = '<a href="' + url + '" style="margin-right: 775px;" download>Click here to download sample file</a>';
    $('#id_userTemplate').html(temp);
}
function dataTableForUserDetails() {
    var oTable = $('#sample_3').dataTable({
        "aoColumnDefs": [
            {
                "bSortable": false,
                "aTargets": [5]
            }
        ],
        "aaSorting": [[0, 'asc']],
        "aLengthMenu": [
            [5, 10, 15, 20],
            [5, 10, 15, 20] // change per page values here
        ],
        // set the initial value
        "iDisplayLength": 10,
        "bFilter": false,
        "bPaginate": false
    });

    jQuery('#sample_3_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
    jQuery('#sample_3_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
    jQuery('#sample_3_wrapper .dataTables_length select').select2(); // initialize select2 dropdown

    $('#sample_3_column_toggler input[type="checkbox"]').change(function () {
        /* Get the DataTables object again - this is not a recreation, just a get of the object */
        var iCol = parseInt($(this).attr("data-column"));
        var bVis = oTable.fnSettings().aoColumns[iCol].bVisible;
        oTable.fnSetColumnVis(iCol, (bVis ? false : true));
    });
}
function resetAttendanceStatus(userId, attendanceId) {
    //var reset = confirm("Are you sure you want to reset the attendance status back to Punched In?");
    //if (reset == true) {
    bootbox.dialog({
        message: "Are you sure you want to reset the attendance status back to Punched In ?",
        title: "Reset Attendance Status",
        buttons: {
            yes: {
                label: "Yes",
                className: "btn-default",
                callback: function () {
                    $('#pleaseWaitDialog').modal('show');
                    $.ajax({
                        type: "PUT",
                        url: fieldSenseURL + "/attendance/punchoutStatus/" + userId + "/" + attendanceId,
                        contentType: "application/json; charset=utf-8",
                        headers: {
                            "userToken": userToken
                        },
                        crossDomain: false,
                        //data: jsonData,
                        cache: false,
                        dataType: 'json',
                        success: function (data) {
                            var userData = data.result;
                            if (data.errorCode == '0000') {
                                var table = $('#sample_5').DataTable();
                                table.draw(false);
                                fieldSenseTosterSuccess(data.errorMessage, true);
                            } else {
                                $('#pleaseWaitDialog').modal('hide');
                                fieldSenseTosterError(data.errorMessage, true);
                                FieldSenseInvalidToken(data.errorMessage);
                            }
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            $('#pleaseWaitDialog').modal('hide');
                            alert("in error:" + thrownError);
                        }
                    });
                }
            },
            no: {
                label: "No",
                className: "btn-default",
                callback: function () {
                    //$('#pleaseWaitDialog').modal('hide');
                    //return false;
                }
            }
        }
    });
}
//var allUserData='';

function showReportingHead(role, edit, userId)
{

    if (role == 2) { // accounts - 2
        //alert("hi");
        if (edit) {
            document.getElementById("check_in_radius_div").style.display = "none";
            document.getElementById("user_accuracy_div").style.display = "none";
        } else {
            document.getElementById("add_check_in_radius_div").style.display = "none";
            document.getElementById("add_user_accuracy_div").style.display = "none";
        }
    }
    else {
        if (edit) {
            document.getElementById("check_in_radius_div").style.display = "block";
            document.getElementById("user_accuracy_div").style.display = "block";
        } else {
            document.getElementById("add_check_in_radius_div").style.display = "block";
            document.getElementById("add_user_accuracy_div").style.display = "block";
        }
    }

    var allUserDetailsTemplateHtml = '';
    allUserDetailsTemplateHtml += '<option value="' + 0 + '">Select</option>';
    for (var i = 0; i < usersForReportTo.length; i++) {
        var id = usersForReportTo[i].id;
        var fullName = usersForReportTo[i].fullName;
        var role1 = usersForReportTo[i].role;
        var active = usersForReportTo[i].active;
        if (active == false || userId == id) {
            continue;
        }       
       
        // Commented By Jyoti 21-June-2017 (down code)
//        if (role1 == 1 || role1 == role || id == topIdInHirarachy) {
//            allUserDetailsTemplateHtml += '<option value="' + id + '">' + fullName + '</option>';
//        }
        
        // Added By Jyoti 23-06-2017
        if($.inArray(usersForReportTo[i].id, listOfReporterOfIDs) !== -1) {  // to avoid setting report_to to each other 
            continue;            
        }
        //        role : 0 -super user, 1 -admin, 2 -account person, 5 -on-field user 
//        if(role == 5 || role == 1){
//            if(role1 == 5 || role1 == 1 || id == topIdInHirarachy){
                allUserDetailsTemplateHtml += '<option value="' + id + '">' + fullName + '</option>';
//            }
//        }
//        if(role == 2){
//            if(role1 == role || role1 == 1 || id == topIdInHirarachy){
//                allUserDetailsTemplateHtml += '<option value="' + id + '">' + fullName + '</option>';
//            }
//        }
        // ended By Jyoti 23-06-2017
    }

    if (edit == true) {
        $('#id_editReportingHead').html(allUserDetailsTemplateHtml);
    } else {
        $('#id_ofAllSubordinate').html(allUserDetailsTemplateHtml);
    }
}
/*
 function allFieldSenseUsersDetails() {
 $.ajax({
 type: "GET",
 url: fieldSenseURL + "/team/organizationChart",
 contentType: "application/json; charset=utf-8",
 headers: {
 "userToken": userToken
 },
 crossDomain: false,
 cache: false,
 dataType: 'json',
 async: false,
 success: function (data) {
 allUserData = data.result;
 
 }
 
 });
 }
 */
/*
 function validateReportingHead(role,parentId) {
 for (var i = 0; i < usersForReportTo.length; i++) {;
 var id=usersForReportTo[i].id;
 if(parentId == id)
 {
 var userRole=usersForReportTo[i].role;
 if(userRole==1 || userRole == role ){
 return true;    
 }else{                   
 return false;
 }
 }   
 }
 }
 */
/*
 function chkReportingHead(userid,parentId) {
 for (var i = 0; i < allUserData.length; i++) {;
 var id=allUserData[i].id;
 if(parentId == id)
 {
 var userid1=allUserData[i].user.id;
 if(userid1 == userid)
 {               
 return true;
 }
 }   
 }
 }
 
 */

var topIdInHirarachy = 0;
var usersForReportTo;

function userDataForReportTo() {
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/user/usersForReportTo",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        async: false,
        success: function (data) {
            topIdInHirarachy = data.result[0];
            usersForReportTo = data.result[1];
        }

    });
}

function offlineStatusCheck() {
    $.ajax({
        type: 'GET',
        url: fieldSenseURL + "/account/settings/values",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        async: false, // edited by jyoti
        success: function (data) {
            if (data.errorCode == '0000') {
                var accountSettings = data.result;
                offlineEnable = accountSettings.allowOffline;
            }
        }
    });
}

function clearTerritoryUsers() {
    leftside_userTerritoryList = new Array();
    selectedTerritories = new Array();
    $('[id^=id_Territory_]').each(function () {
        $(this).attr('checked', false);
        $(this).parent().removeClass('checked');
    });
    $('.cls_clearTerritory').hide();
    $('#id_searchText').val("");
    searchUserWithFilter();
}

function clearStatusUsers() {
    userStatus = new Array();
    $("#id_activeSearch").attr('checked', false);
    $("#id_activeSearch").parent().removeClass('checked');
    $("#id_deactiveSearch").attr('checked', false);
    $("#id_deactiveSearch").parent().removeClass('checked');
    $('.cls_clearStatus').hide();
    $('#id_searchText').val("");
    searchUserWithFilter();
}