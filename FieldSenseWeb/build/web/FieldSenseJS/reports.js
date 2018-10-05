var users_emp_codes_map = new Object();
// added by vaibhav
var imagezip = new JSZip();
var zipFileName ="";

// end vy vaibhav
var totalAmt = new Array();
var totalAmt1 = [];
var firstdraw = true;
var formHeaders = [];
var perviousTravelLogTime = 0;
var perviousTravelLogDistance = 1;
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
        } else if (loginUserId != 0) {
            loggedinUserImageData();
            loggedinUserData();
            custFormNames();// for custom forms
            teamMembers();
            dateRange();
            $('#s2id_autogen1 > a > span:eq(0)').text("20");
            window.clearTimeout(intervalAdminUser);
        }
    }
    catch (err) {
        window.location.href = "login.html";
    }
}, 1000);

function teamMembers() {
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/team/allSubordinates/" + loginUserId,
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
                var teamMemberData = data.result;
                var teamMemberTemplate = '';
                teamMemberTemplate += '<select class="form-control" id="id_memberName">';
                teamMemberTemplate += '<option value="select">Select Member</option>';
                //by vaibhav
                if ($("#visit_list").hasClass('active')) {
                    teamMemberTemplate += '<option value="0">All</option>';
                }
                for (var i = 0; i < teamMemberData.length; i++) {
                    var userId = teamMemberData[i].id;
                    users_emp_codes_map[userId] = teamMemberData[i].emp_code;
                    var firstName = teamMemberData[i].firstName;
                    var lastName = teamMemberData[i].lastName;
                    var fullName = firstName + ' ' + lastName;
                    teamMemberTemplate += '<option value="' + userId + '">' + fullName + '</option>';
                }
                teamMemberTemplate += '</select>';
            }
            $('#id_teamMembers').html(teamMemberTemplate);
        }
    });
}


function getAttendanceDiv() {
    $('.id_emp').html('');  // added by manohar

    var myDiv = document.getElementById("downloadimagezipdiv");
    myDiv.style = "display: none;";
    $(".leftnav_form").each(function () {
        $(this).removeClass("active");
    });
    $("#attendance_list").attr("class", "active");
    $("#visit_list").removeClass("active");
    $("#expense_list").removeClass("active");
    $("#travel_list").removeClass("active");
    $("#monthly_travel_list").removeClass("active");
    $("#id_memberName").val("select");
    jQuery("#id_memberName option").filter(function () {
        return $.trim($(this).text()) == 'All'
    }).remove();
    //$("#id_memberName option[value='All']").remove();
    $("#reports_show").attr("onclick", "attendance()");
    $("#reports_show").attr("style", "margin-left: 47px;");
    $('#dateRange').attr("class", "col-md-5");
    dateRange();
    $("#id_exportAttendance").html('<button type="button" class="btn btn-info fr" style="margin-right:-15px;"><i class=" fa fa-file-text" id="id_exportAttendanceChild"></i> Save as CSV</button>');
    var tabletemplate = '<table class="table table-striped table-bordered table-hover" id="sample_5">';
    tabletemplate += '<thead>';
    tabletemplate += '<tr>';
    tabletemplate += '<th>Punch_In Date</th>';
    tabletemplate += '<th>Punch-In</th>';
    tabletemplate += '<th>Punch-In Location</th>';
    tabletemplate += '<th>Punch_Out Date</th>';
    tabletemplate += '<th>Punch-Out</th>';
    tabletemplate += '<th>Punch-Out Location</th>';
    tabletemplate += '<th class="hidden-xs">Time Out</th>';
    tabletemplate += '<th class="hidden-xs">Worked For</th>';
    tabletemplate += '</tr></thead>';
    tabletemplate += '<tbody></tbody></table>';
    $(".cls_attendance").html(tabletemplate);
    $('#sample_5').dataTable({
        "aaSorting": [],
        "aLengthMenu": [
            [-1, 5, 10, 15, 20, 50, 100],
            ["All", 5, 10, 15, 20, 50, 100] // change per page values here
        ],
        // set the initial value
        "iDisplayLength": 20,
        "bFilter": false
    });

    jQuery('#sample_5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
    jQuery('#sample_5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
    jQuery('#sample_5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown
    App.initUniform();
    App.fixContentHeight();
}

function getVisitDiv() {
    $('.id_emp').html('');  // added by manohar
    var myDiv = document.getElementById("downloadimagezipdiv");
    myDiv.style = "display: none;";
    $(".leftnav_form").each(function () {
        $(this).removeClass("active");
    });
    $("#attendance_list").removeClass("active");
    $("#visit_list").attr("class", "active");
    $("#expense_list").removeClass("active");
    $("#travel_list").removeClass("active");
    $("#monthly_travel_list").removeClass("active");
    //$("#id_memberName").val("select");
    jQuery("#id_memberName option").filter(function () {
        return $.trim($(this).text()) == 'All'
    }).remove();
    $("#id_memberName option[value='select']").remove();
    $("#id_memberName option[value='0']").remove();
    $('#id_memberName').prepend("<option value='select'>" + "Select Member" + "</option>" + "<option value='" + 0 + "'>" + "All" + "</option>");// Add All
    //$("#id_memberName option[value='All']").remove();
    $("#id_memberName").val("select");
    $("#reports_show").attr("onclick", "visit()");
    $("#reports_show").attr("style", "margin-left: 47px;");
    $('#dateRange').attr("class", "col-md-5");
    dateRange();
    $("#id_exportAttendance").html('<button type="button" class="btn btn-info fr" style="margin-right:-15px;"><i class=" fa fa-file-text" id="id_exportAttendanceChild"></i> Save as CSV</button> ');
    var tabletemplate = '<table class="table table-striped table-bordered table-hover" id="sample_5">';
    tabletemplate += '<thead>';
    tabletemplate += '<tr>';
    tabletemplate += '<th>Customer</th>';
    tabletemplate += '<th>Visit</th>';
    tabletemplate += '<th>Date</th>';
    tabletemplate += '<th>Start Time</th>';
    tabletemplate += '<th>End Time</th>';
    tabletemplate += '<th>Purpose</th>';
    tabletemplate += '<th>Owner</th>';
    tabletemplate += '<th>Check-In Date</th>';
    tabletemplate += '<th>Check-In Time</th>';
    tabletemplate += '<th>Check-Out Date</th>';
    tabletemplate += '<th>Check-Out Time</th>';
    tabletemplate += '<th class="hidden-xs">Status</th>';
    tabletemplate += '<th class="hidden-xs">Outcome</th>';
    tabletemplate += '<th class="hidden-xs">Outcome Description</th>';
    tabletemplate += '</tr></thead>';
    tabletemplate += '<tbody></tbody></table>';
    $(".cls_attendance").html(tabletemplate);
    $('#sample_5').dataTable({
        "aaSorting": [],
        "aLengthMenu": [
            [-1, 5, 10, 15, 20, 50, 100],
            ["All", 5, 10, 15, 20, 50, 100] // change per page values here
        ],
        // set the initial value
        "iDisplayLength": 20,
        "bFilter": false
    });

    jQuery('#sample_5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
    jQuery('#sample_5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
    jQuery('#sample_5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown
    App.initUniform();
    App.fixContentHeight();
}

function getExpenseDiv() {
    $('.id_emp').html('');  // added by manohar
    var myDiv = document.getElementById("downloadimagezipdiv");
    myDiv.style = "display: none;";
    $(".leftnav_form").each(function () {
        $(this).removeClass("active");
    });
    $("#attendance_list").removeClass("active");
    $("#visit_list").removeClass("active");
    $("#expense_list").attr("class", "active");
    $("#travel_list").removeClass("active");
    $("#monthly_travel_list").removeClass("active");
    $("#id_memberName").val("select");
    jQuery("#id_memberName option").filter(function () {
        return $.trim($(this).text()) == 'All'
    }).remove();
    //$("#id_memberName option[value='All']").remove();
    $("#reports_show").attr("onclick", "expense()");
    $("#reports_show").attr("style", "margin-left: 47px;");
    $('#dateRange').attr("class", "col-md-5");
    dateRange();
    $("#id_exportAttendance").html('<button type="button" class="btn btn-info fr" style="margin-right:-15px;"><i class=" fa fa-file-text" id="id_exportAttendanceChild"></i> Save as CSV</button>');
    var tabletemplate = '<table class="table table-striped table-bordered table-hover" id="sample_5">';
    tabletemplate += '<thead>';
    tabletemplate += '<tr>';
    tabletemplate += '<th>Expenses</th>';
    tabletemplate += '<th>Expense Category</th>';
    tabletemplate += '<th>Date / Time</th>';
    tabletemplate += '<th>Customer</th>';
    tabletemplate += '<th>Visit</th>';
    tabletemplate += '<th>Status</th>';
    tabletemplate += '<th class="hidden-xs">Approved / Rejected By</th>';
    tabletemplate += '<th class="hidden-xs">Approved / Rejected On</th>';
    tabletemplate += '<th class="hidden-xs">Rejected Reason</th>';
    tabletemplate += '<th>Amount Claimed</th>';
    tabletemplate += '<th class="hidden-xs">Disbursed Amount</th>';
    tabletemplate += '</tr></thead>';
    tabletemplate += '<tbody></tbody></table>';
    $(".cls_attendance").html(tabletemplate);
    $('#sample_5').dataTable({
        "aaSorting": [],
        "aLengthMenu": [
            [-1, 5, 10, 15, 20, 50, 100],
            ["All", 5, 10, 15, 20, 50, 100] // change per page values here
        ],
        // set the initial value
        "iDisplayLength": 20,
        "bFilter": false
    });

    jQuery('#sample_5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
    jQuery('#sample_5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
    jQuery('#sample_5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown
    App.initUniform();
    App.fixContentHeight();
}

function getTravleDiv() {
    $('.id_emp').html('');  // added by manohar
    var myDiv = document.getElementById("downloadimagezipdiv");
    myDiv.style = "display: none;";
    $(".leftnav_form").each(function () {
        $(this).removeClass("active");
    });
    $("#attendance_list").removeClass("active");
    $("#visit_list").removeClass("active");
    $("#expense_list").removeClass("active");
    $("#monthly_travel_list").removeClass("active");
    $("#travel_list").attr("class", "active");
    $("#id_memberName").val("select");
    jQuery("#id_memberName option").filter(function () {
        return $.trim($(this).text()) == 'All'
    }).remove();
    //$("#id_memberName option[value='All']").remove();
    $("#reports_show").attr("onclick", "travelReports()");
    $("#reports_show").attr("style", "margin-left: -39px;");
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
    var firstDay = d + '/' + m + '/' + y;
    var dateRangeTemplate = '';
    dateRangeTemplate += '<div class = "form-group">';
    dateRangeTemplate += '<div>     </div>';
    dateRangeTemplate += '<label class = "control-label col-md-4"> Date </label>';
    dateRangeTemplate += '<div class = "col-md-8">';
//    dateRangeTemplate += '<div class = "input-group input-large date-picker input-daterange" data - date = "20/11/2012" data-date-format="dd/mm/yyyy">';
    dateRangeTemplate += '<div class = "input-group date-picker input-daterange" data - date = "20/11/2012" data-date-format="dd/mm/yyyy">';
    dateRangeTemplate += '<input type = "text" class = "form-control" name = "from" id = "id_startDate" value="' + firstDay + '">';
    dateRangeTemplate += '</div></div></div>';
    $('#dateRange').attr("class", "col-md-4");
    $('#dateRange').html(dateRangeTemplate);
    $('.date-picker').datepicker({
        rtl: App.isRTL(),
        autoclose: true
    });
    $("#id_exportAttendance").html('<button type="button" class="btn btn-info fr" style="margin-right:-15px;"><i class=" fa fa-file-text" id="id_exportAttendanceChild"></i> Save as CSV</button>');
    var tabletemplate = '<table class="table table-striped table-bordered table-hover" id="sample_5">';
    tabletemplate += '<thead>';
    tabletemplate += '<tr>';
    tabletemplate += '<th>Time</th>';
    tabletemplate += '<th>Location</th>';
    tabletemplate += '<th>Latitude</th>';
    tabletemplate += '<th>Longitude</th>';
    tabletemplate += '<th>Distance (in kms)</th>';
    // added by jyoti
    tabletemplate += '<th>';
    tabletemplate += 'Battery percentage';
    tabletemplate += '</th>';
    tabletemplate += '<th>';
    tabletemplate += 'GPS (Y/N)';
    tabletemplate += '</th>';
    tabletemplate += '<th>';
    tabletemplate += 'Connectivity';
    tabletemplate += '</th>';
    tabletemplate += '<th>';
    tabletemplate += 'App version';
    tabletemplate += '</th>';
    tabletemplate += '<th>';
    tabletemplate += 'Mobile OS version';
    tabletemplate += '</th>';
    tabletemplate += '<th>';
    tabletemplate += 'Device name';
    tabletemplate += '</th>';
    tabletemplate += '<th>';
    tabletemplate += 'Mock Location (Y/N)';
    tabletemplate += '</th>';
    // ended by jyoti
    tabletemplate += '</tr></thead>';
    tabletemplate += '<tbody></tbody></table>';
    $(".cls_attendance").html(tabletemplate);
    $('#sample_5').dataTable({
        "aaSorting": [],
        "aLengthMenu": [
            [-1, 5, 10, 15, 20, 50, 100],
            ["All", 5, 10, 15, 20, 50, 100] // change per page values here
        ],
        // set the initial value
        "iDisplayLength": 20,
        "bFilter": false
    });

    jQuery('#sample_5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
    jQuery('#sample_5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
    jQuery('#sample_5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown
    $("#sample_5_wrapper > .row:eq(1)").hide();
    App.initUniform();
    App.fixContentHeight();
}

function loadCustomFormReportPage(id) {
    $('.id_emp').html('');  // added by manohar
    formHeaders = [];
    var myDiv = document.getElementById("downloadimagezipdiv");
    myDiv.style = "display: inline;";
    $(".leftnav_form").each(function () {
        $(this).removeClass("active");
    });
    $("#attendance_list").removeClass("active");
    $("#visit_list").removeClass("active");
    $("#expense_list").removeClass("active");
    $("#travel_list").removeClass("active");
    $("#monthly_travel_list").removeClass("active");
    $("#leftnav_form_" + id).addClass("active");
    $("#id_memberName").val("select");

    jQuery("#id_memberName option").filter(function () {
        return $.trim($(this).text()) == 'All'
    }).remove();
    //$("#id_memberName option[value='All']").remove();
    if ($('select#id_memberName option').length > 1) {
        $("#id_memberName option").eq(1).before($("<option></option>").val("All").text("All"));
    }
    //$("#id_memberName").prepend('<option value="All">All</option>');
    $("#reports_show").attr("onclick", "formReports(" + id + ")");
    dateRange();
    $("#reports_show").attr("style", "margin-left: 47px;");
    $('#dateRange').attr("class", "col-md-5");
    $("#id_exportAttendance").html('<button type="button" class="btn btn-info fr" style="margin-right:-15px;"><i class=" fa fa-file-text" id="id_exportAttendanceChild"></i> Save as CSV</button>');
    //$("#id_exportAttendance").append('<button id="downloadimagesaszip_btn" onclick="zipImagesAndCsv()" type="button" class="btn btn-info fr" style="margin-right:-18px;margin-left:20px;"><i class=" fa fa-file-text" ></i> Download Images as zip</button>');
    var custForm = '';
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/adminCustomForms/" + id, //change url
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: false,
        success: function (data) {
            var myData = data;
            if (data.errorCode == '0000') {

                $('.cls_attendance').html('');
                custForm += '<table class="table table-striped table-bordered table-hover" id="sample_i5">';
                custForm += '<thead>';
                custForm += '<tr>';
                custForm += '<th>Submitted by</th>';
                custForm += '<th>Submitted on</th>';

                for (var i = 0; i < data.result.tableData.length; i++) {
                    custForm += '<th>';
                    custForm += data.result.tableData[i].labelName;
                    custForm += '</th>';
                    formHeaders.push({"labelName": data.result.tableData[i].labelName, "fieldId": data.result.tableData[i].fieldId, "fieldType": data.result.tableData[i].fieldType});
                    //Headersids.push(data.result.tableData[i].fieldId);
                    //console.log(data.result.tableData[i].fieldId);
                }
                custForm += '</tr>';
                custForm += '</thead>';
                custForm += '<tbody> </tbody></table>';
            }

            $('.cls_attendance').html(custForm);

            //load DataTable

            $('#sample_i5').dataTable({
                //                    "aaSorting": [[1, 'asc']],
                "bDestory": true,
                "sPaginationType": "bootstrap_full_number",
                //"scrollY":"500px",
                // "scrollCollapse": true,
                // "paging": true,
                "aLengthMenu": [
                    [-1, 5, 10, 15, 20, 50, 100],
                    ["All", 5, 10, 15, 20, 50, 100] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": 20,
                "bFilter": false,
                "aaSorting": []
            });

            var reportTable = $('#sample_i5').dataTable();

            reportTable.fnSetColumnVis(0, false);

            jQuery('#sample_i5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
            jQuery('#sample_i5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
            jQuery('#sample_i5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown
            //DataTable Done
            $('#pleaseWaitDialog').modal('hide');
        },
        error: function (xhr, ajaxOptions, erroThrown) {
            alert("erroThrown:" + erroThrown);
        }
        //}
    });
}

function attendance() {
    var exportAttendance = new Array();
    $("#id_exportAttendance").html('<button type="button" class="btn btn-info fr" style="margin-right:-15px;"><i class=" fa fa-file-text" id="id_exportAttendanceChild"></i> Save as CSV</button>');
    var teamMember = document.getElementById("id_memberName").value;
    if (teamMember == 'select') {
        fieldSenseTosterError("Please select any member", true);
        return false;
    }
    /*	
     var startTime = document.getElementById("id_startDate").value;
     var endTime = document.getElementById("id_endDate").value;
     var startTimeSplit = startTime.split('/');
     var date = startTimeSplit[0];
     var month = startTimeSplit[1];
     var year = startTimeSplit[2];
     startTime = year + '-' + month + '-' + date;
     var ensdTimeSplit = endTime.split('/');
     var date1 = ensdTimeSplit[0];
     var month1 = ensdTimeSplit[1];
     var year1 = ensdTimeSplit[2];
     endTime = year1 + '-' + month1 + '-' + date1;
     
     */
    var startTime = document.getElementById("id_startDate").value;
    var endTime = document.getElementById("id_endDate").value;
    var startTimeSplit = startTime.split('/');
    var fromDate = convertLocalDateToServerDate(startTimeSplit[2], startTimeSplit[1] - 1, startTimeSplit[0], 0, 0);
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

    var ensdTimeSplit = endTime.split('/');
    var toDate = convertLocalDateToServerDate(ensdTimeSplit[2], ensdTimeSplit[1] - 1, ensdTimeSplit[0], 23, 59);
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


    var count='';	
    var attendanceTemplate = '';
    var userTemplate = '';    
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/reports/attendance/" + teamMember + "/" + fromDate + "/" + toDate,
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
                var attendanceData = data.result;
                clevertap.event.push("Attendance Report", {
//                    "UserId": loginUserIdcookie,
//                    "User Name": loginUserNamecookie,
//                    "AccountId": accountIdcookie,
                    "Source": "Web",
                    "Account name": accountNamecookie,
                    "UserRolecookie": UserRolecookie,
                });
                myTable = $("#sample_5").DataTable();
                if (myTable != null)
                    myTable.destroy();
                $('.cls_attendance').html('');
                attendanceTemplate += '<table class="table table-striped table-bordered table-hover" id="sample_5">';
                attendanceTemplate += '<thead>';
                attendanceTemplate += '<tr>';
                attendanceTemplate += '<th>';
                attendanceTemplate += 'Punch_In Date';
                attendanceTemplate += '</th>';
                attendanceTemplate += '<th>';
                attendanceTemplate += 'Punch-In';
                attendanceTemplate += '</th>';
                attendanceTemplate += '<th>';
                attendanceTemplate += 'Punch-In Location';
                attendanceTemplate += '</th>';
                //attendanceTemplate += '<th>';
                //attendanceTemplate += 'Punch-in Location Not Found Reason ';
                //attendanceTemplate += '</th>';
                attendanceTemplate += '<th>';
                attendanceTemplate += 'Punch_Out Date';
                attendanceTemplate += '</th>';
                attendanceTemplate += '<th>';
                attendanceTemplate += 'Punch-Out';
                attendanceTemplate += '</th>';
                attendanceTemplate += '<th>';
                attendanceTemplate += 'Punch-Out Location';
                attendanceTemplate += '</th>';
                //attendanceTemplate += '<th>';
                //attendanceTemplate += 'Punch-out Location Not Found Reason ';
                //attendanceTemplate += '</th>';
                attendanceTemplate += '<th class="hidden-xs">';
                attendanceTemplate += 'Time Out';
                attendanceTemplate += '</th>';
                attendanceTemplate += '<th class="hidden-xs">';
                attendanceTemplate += 'Worked For';
                attendanceTemplate += '</th>';
                attendanceTemplate += '</tr>';
                attendanceTemplate += '</thead>';
                attendanceTemplate += '<tbody>';
                for (var i = 0; i < attendanceData.length; i++) {
                    var date = attendanceData[i].punchDate;
                    var dateSplit = date.split('-');
                    var punchOutdate = attendanceData[i].punchOutDate;
                    var punchOutdateSplit = punchOutdate.split('-');
                    var userName = attendanceData[i].userName;
                    var punchIn = attendanceData[i].punchInTime;
                    var punchOut = attendanceData[i].punchOutTime;
                    var totalTimeOut = attendanceData[i].totalTimeOut;
                    var punchInLocation = attendanceData[i].punchInLocation;
                    var workHr = attendanceData[i].workHr;
                    if (punchInLocation.trim() == "Unknown location") {
                        punchInLocation = "Address could not be resolved";
                    }
                    var punchInLocationNotFoundReason = attendanceData[i].punchInLocationNotFoundReason;
                    var punchOutLocation = attendanceData[i].punchOutLocation;
                    if (punchOutLocation.trim() == "Unknown location") {
                        punchOutLocation = "Address could not be resolved";
                    }
                    var punchOutLocationNotFoundReason = attendanceData[i].punchOutLocationNotFoundReason;
                    var punchInSplit = punchIn.split(':');
                    var punchOutSplit = punchOut.split(':');
                    var punchInDateJs = convertServerDateToLocalDate(dateSplit[0], dateSplit[1] - 1, dateSplit[2], punchInSplit[0], punchInSplit[1]);
                    var punchInHr = punchInDateJs.getHours();
                    var punchInHr1 = punchInDateJs.getHours();
                    var punchInMin = punchInDateJs.getMinutes();
                    var punchInMin1 = punchInDateJs.getMinutes();
                    var punchOutDateJs = convertServerDateToLocalDate(punchOutdateSplit[0], punchOutdateSplit[1] - 1, punchOutdateSplit[2], punchOutSplit[0], punchOutSplit[1]);
                    var punchOuttHr = punchOutDateJs.getHours();
                    var punchOuttHr1 = punchOutDateJs.getHours();
                    var punchOutMin = punchOutDateJs.getMinutes();
                    var punchOutMin1 = punchOutDateJs.getMinutes();
                    if (punchInMin < 10) {
                        punchInMin = '0' + punchInMin;
                    }
                    if (punchOutMin < 10) {
                        punchOutMin = '0' + punchOutMin;
                    }
                    if (punchInHr < 12) {
                        if (punchInHr < 10) {
                            punchInHr = '0' + punchInHr;
                        }
                        punchIn = punchInHr + ':' + punchInMin + ' AM';
                    } else if (punchInHr == 12) {
                        punchIn = punchInHr + ':' + punchInMin + ' PM';
                    } else {
                        punchInHr = punchInHr - 12;
                        if (punchInHr < 10) {
                            punchInHr = '0' + punchInHr;
                        }
                        punchIn = punchInHr + ':' + punchInMin + ' PM';
                    }

                    if (punchOut != '00:00:00') {
                        var d = punchOutDateJs.getDate();
                        if (d < 10) {
                            d = '0' + d;
                        }
                        var m = punchOutDateJs.getMonth();
                        var y = punchOutDateJs.getFullYear();
                        var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
                        m = monthNames[m];
                        punchOutdate = d + ' ' + m + ' ' + y;
                        if (punchOuttHr > 12) {
                            punchOuttHr = punchOuttHr - 12;
                            if (punchOuttHr < 10) {
                                punchOuttHr = '0' + punchOuttHr;
                            }
                            punchOut = punchOuttHr + ':' + punchOutMin + ' PM';
                        } else if (punchOuttHr == 12) {
                            punchOut = punchOuttHr + ':' + punchOutMin + ' PM';
                        } else {
                            if (punchOuttHr < 10) {
                                punchOuttHr = '0' + punchOuttHr;
                            }
                            punchOut = punchOuttHr + ':' + punchOutMin + ' AM';
                        }
                        /* var punchOutMinuts = (punchOuttHr1 * 60) + punchOutMin1;
                         var punchInMinuts = (punchInHr1 * 60) + punchInMin1;
                         var workedMinutes = punchOutMinuts - punchInMinuts;
                         if(totalTimeOut !='00:00:00' && totalTimeOut !='000000' ){
                         workedMinutes=workedMinutes-((parseInt(totalTimeOut.split(":")[0])*60)+(parseInt(totalTimeOut.split(":")[1])));
                         }
                         var workhr = Math.floor(workedMinutes / 60);
                         var workmin = workedMinutes % 60;*/
                        var workedMinutes = (parseInt(workHr.split(":")[0]) * 60) + (parseInt(workHr.split(":")[1]));
                        if (totalTimeOut != '00:00:00' && totalTimeOut != '000000') {
                            workedMinutes = workedMinutes - ((parseInt(totalTimeOut.split(":")[0]) * 60) + (parseInt(totalTimeOut.split(":")[1])));
                        }
                        var workhr = Math.floor(workedMinutes / 60);
                        var workmin = workedMinutes % 60;
                        if (parseInt(workhr) < 10) {
                            workhr = "0" + workhr;
                        }
                        if (parseInt(workmin) < 10) {
                            workmin = "0" + workmin;
                        }
                        workHr = workhr + ":" + workmin + ' Hrs';

                    } else {
                        punchOutdate = '';
                        punchOut = '';
                        workHr = '-';
                    }

                    var d = punchInDateJs.getDate();
                    if (d < 10) {
                        d = '0' + d;
                    }
                    var m = punchInDateJs.getMonth();
                    var y = punchInDateJs.getFullYear();
                    var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
                    m = monthNames[m];
                    date = d + ' ' + m + ' ' + y;
                    attendanceTemplate += '<tr>';
                    attendanceTemplate += '<td>';
                    attendanceTemplate += date;
                    attendanceTemplate += '</td>';
                    attendanceTemplate += '<td>';
                    attendanceTemplate += punchIn;
                    attendanceTemplate += '</td>'
                    attendanceTemplate += '<td class="puchinloc">';
                    if (punchInLocation.length > 90) {
                        punchInLocation = punchInLocation.substring(0, 45) + "<br>" + punchInLocation.substring(45, 90) + "<br>" + punchInLocation.substring(90) + "<br>";
                    } else if (punchInLocation.length > 45) {
                        punchInLocation = punchInLocation.substring(0, 45) + "<br>" + punchInLocation.substring(45) + "<br>";
                    }
                    attendanceTemplate += punchInLocation;
                    attendanceTemplate += '</td>';
                    //attendanceTemplate += '<td>';
                    //attendanceTemplate += punchInLocationNotFoundReason;
                    //attendanceTemplate += '</td>';
                    attendanceTemplate += '<td>';
                    attendanceTemplate += punchOutdate;
                    attendanceTemplate += '</td>';
                    attendanceTemplate += '<td>';
                    attendanceTemplate += punchOut;
                    attendanceTemplate += '</td>';
                    attendanceTemplate += '<td class="puchoutloc">';
                    if (punchOutLocation.length > 90) {
                        punchOutLocation = punchOutLocation.substring(0, 45) + "<br>" + punchOutLocation.substring(45, 90) + "<br>" + punchOutLocation.substring(90) + "<br>";
                    } else if (punchOutLocation.length > 45) {
                        punchOutLocation = punchOutLocation.substring(0, 45) + "<br>" + punchOutLocation.substring(45) + "<br>";
                    }
                    attendanceTemplate += punchOutLocation;
                    attendanceTemplate += '</td>';
                    //attendanceTemplate += '<td>';
                    //attendanceTemplate += punchOutLocationNotFoundReason;
                    //attendanceTemplate += '</td>';
                    attendanceTemplate += '<td class="hidden-xs">';
                    if (totalTimeOut != '000000') {
                        attendanceTemplate += '<a data-toggle="modal" href="#timeout" onclick="getTimeOutDetails(\'' + attendanceData[i].userId + '\',\'' + attendanceData[i].punchDate + '\');"</a>' + totalTimeOut;
                    } else {
                        totalTimeOut = "00:00:00";
                        attendanceTemplate += totalTimeOut;
                    }
                    attendanceTemplate += '</td>';
                    attendanceTemplate += '<td class="hidden-xs">';
                    attendanceTemplate += workHr;
                    attendanceTemplate += '</td>';
                    attendanceTemplate += '</tr>';
                    exportAttendance[i] = [{
                            "date": date,
                            "punchin": punchIn,
                            "punchinLocation": punchInLocation,
                            //"punchinLocationNot": punchInLocationNotFoundReason,
                            "punchoutdate": punchOutdate,
                            "punchout": punchOut,
                            "punchoutLocation": punchOutLocation,
                            //"punchoutLocationNot": punchOutLocationNotFoundReason,
                            "TimeOut": totalTimeOut,
                            "worked": workHr
                        }];
                }
                attendanceTemplate += '</tbody>';
                attendanceTemplate += '</table>';
                //added by manohar
                for (var i = 0; i < attendanceData.length; i++) {
                   var emp_code = attendanceData[i].emp_code;
                   var userName = attendanceData[i].userName;
                   break;
                }
                count=attendanceData.length;//added by manohar
                if(attendanceData.length!=0){
                    // modified by jyoti
                    if(emp_code == ''){
                        userTemplate += '<h4 class="form-section">' + userName + '</h4>';
                    } else {
                        userTemplate += '<h4 class="form-section">' + userName + '<small> - Emp. Code: ' + emp_code + '</small></h4>';
                    }
                    // ended by jyoti
                    $('.id_emp').html(userTemplate);
                }
                $('.cls_attendance').html(attendanceTemplate);
                // prepare CSV data
                var csvData = new Array();
                //                csvData.push('"Date","Punch-in","Punch-in Location","Punch-in Location Not Found Reason","Punch-out","Punch-out Location","Punch-out Location Not Found Reason","Worked For"');
                csvData.push('"Punch_In Date","Punch-In","Punch-In Location","Punch_Out Date","Punch-Out","Punch-Out Location","Time Out","Worked For"');
                for (var i = 0; i < exportAttendance.length; i++) {
                    //for (var j = 0; j < 1; j++) {
                    //                        csvData.push('"' + exportAttendance[i][j].date + '","' + exportAttendance[i][j].punchin + '","' + exportAttendance[i][j].punchinLocation + '","' + exportAttendance[i][j].punchinLocationNot + '","' + exportAttendance[i][j].punchout + '","' + exportAttendance[i][j].punchoutLocation + '","' + exportAttendance[i][j].punchoutLocationNot + '","' + exportAttendance[i][j].worked + '"');
                    csvData.push('"' + exportAttendance[i][0].date + '","' + exportAttendance[i][0].punchin + '","' + exportAttendance[i][0].punchinLocation + '","' + exportAttendance[i][0].punchoutdate + '","' + exportAttendance[i][0].punchout + '","' + exportAttendance[i][0].punchoutLocation + '","' + exportAttendance[i][0].TimeOut + '","' + exportAttendance[i][0].worked + '"');
                    // }
                }
                // download stuff
                if (csvData.length > 1) {
                    // modified by jyoti
                    var fileName = '';
                    if(emp_code == ''){
                        fileName = userName + "_Attendance.csv";
                    } else {
                        fileName = userName + "_Attendance_" + emp_code +".csv";
                    }
                    // ended by jyoti
                    var buffer = csvData.join("\n");
                    var blob;
                    try {
                        blob = new Blob([buffer], {
                            "type": "text/csv;charset=utf8;"
                        });
                    } catch (e) {
                        window.BlobBuilder = window.BlobBuilder ||
                                window.WebKitBlobBuilder ||
                                window.MozBlobBuilder ||
                                window.MSBlobBuilder;

                        if (e.name == 'TypeError' && window.BlobBuilder) {
                            var bb = new BlobBuilder();
                            bb.append(buffer);
                            blob = bb.getBlob("text/csv;charset=utf8;");
                        }
                        else if (e.name == "InvalidStateError") {
                            // InvalidStateError (tested on FF13 WinXP)
                            blob = new Blob([buffer], {
                                type: "text/csv;charset=utf8;"
                            });
                        }
                    }
                    var link = document.createElement("a");
                    var myURL = window.URL || window.webkitURL;
                    if (link.download !== undefined) { // feature detection
                        // Browsers that support HTML5 download attribute
                        //                        var myURL = window.URL || window.webkitURL;
                        link.setAttribute("href", myURL.createObjectURL(blob));
                        link.setAttribute("download", fileName);
                    }
                    else if (navigator.msSaveBlob) { // IE 10+
                        navigator.msSaveBlob(blob, fileName);
                    }
                    else {
                        link.setAttribute("href", myURL.createObjectURL(blob));
                        link.setAttribute("name", fileName);
                    }
                    $('#id_exportAttendance').html('');
                    link.innerHTML = '<button type="button" class="btn btn-info fr" style="margin-right:-15px;"><i class=" fa fa-file-text" id="id_exportAttendanceChild"></i> Save as CSV</button>';
                    document.getElementById("id_exportAttendance").appendChild(link);
                }
                $('#pleaseWaitDialog').modal('hide');
                $('#sample_5').dataTable({
                    "aaSorting": [],
                    "sPaginationType": "bootstrap_full_number",
                    // "scrollY":"500px",
                    //"scrollCollapse": true,
                    // "paging": true,
                    "aLengthMenu": [
                        [-1, 5, 10, 15, 20, 50, 100],
                        ["All", 5, 10, 15, 20, 50, 100] // change per page values here
                    ],
                    // set the initial value
                    "iDisplayLength": 20,
                    "bFilter": false,
                    "aoColumnDefs": [
                        {"bSortable": false,
                            "aTargets": [1, 2, 4, 5, 6, 7]
                        }
                    ],
                    "fnDrawCallback": function (oSettings) {
                        inboxtable = $('#sample_5').dataTable();
                        inboxtable.$('td').mouseover(function () {
                            var sData = $(this).text();
                            $(this).attr('title', sData);
                        });
                        //$("#sample_5").find('td').css({'max-width':'300px','overflow':'hidden','text-overflow':'ellipsis'});	
                        /*$('#sample_5').find('tr').find('td:eq(3)').each(function(){
                         $(this).css({'max-width':'300px','overflow':'hidden','text-overflow':'ellipsis'});
                         });
                         $('#sample_5').find('tr').find('td:eq(5)').each(function(){
                         $(this).css({'max-width':'300px','overflow':'hidden','text-overflow':'ellipsis'});
                         });*/
                    },
                });

                jQuery('#sample_5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
                jQuery('#sample_5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
                jQuery('#sample_5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown
            } else {
                fieldSenseTosterError(data.errorMessage, true);
                $('#pleaseWaitDialog').modal('hide');
            }
            if(count==0)   // added by manohar
            {
                  $('.id_emp').html('');
            }
        }
    });
}
function visit() {
    clevertap.event.push("Visit Report", {
//                    "UserId": loginUserIdcookie,
//                    "User Name": loginUserNamecookie,
//                    "AccountId": accountIdcookie,
        "Source": "Web",
        "Account name": accountNamecookie,
        "UserRolecookie": UserRolecookie,
    });
    var exportVisit = new Array();
    $("#id_exportAttendance").html('<button type="button" class="btn btn-info fr" style="margin-right:-15px;"><i class=" fa fa-file-text" id="id_exportAttendanceChild"></i> Save as CSV</button>');
    var teamMember = document.getElementById("id_memberName").value;
    if(teamMember!=0)
    {
        var emp_code=users_emp_codes_map[teamMember]; // added by manohar
    }
//    var emp_code=users_emp_codes_map[teamMember];   // added by manohar
    if (teamMember == 'select') {
        fieldSenseTosterError("Please select any member", true);
        return false;
    }
    var startTime = document.getElementById("id_startDate").value;
    var endTime = document.getElementById("id_endDate").value;
    var startTimeSplit = startTime.split('/');
    var fromDate = convertLocalDateToServerDate(startTimeSplit[2], startTimeSplit[1] - 1, startTimeSplit[0], 0, 0);
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
    var ensdTimeSplit = endTime.split('/');
    var toDate = convertLocalDateToServerDate(ensdTimeSplit[2], ensdTimeSplit[1] - 1, ensdTimeSplit[0], 23, 59);
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
    var visitTemplate = '';
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/reports/visit/" + teamMember + "/" + fromDate + "/" + toDate + "/csv",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: false,
        success: function (data) {

            console.log(data.resultType);
            var ResultType = data.resultType;
            if (ResultType == 'Visit report list All .') {
                if (data.errorCode == '0000') {
                    console.log("checkAllVisits1 " + data.result.checkAllVisits);
                    myTable = $("#sample_5").DataTable();
                    //   myTable.fixedHeader.enable();
                    if (myTable != null)
                        myTable.destroy();
                    $('.cls_attendance').html('');
                    visitTemplate += '<table class="table table-striped table-bordered table-hover" id="sample_5">';
                    visitTemplate += '<thead>';
                    visitTemplate += '<tr>';
                    visitTemplate += '<th>';
                    visitTemplate += 'Visited By';
                    visitTemplate += '</th>';
                    visitTemplate += '<th>';
                    visitTemplate += 'Customer';
                    visitTemplate += '</th>';
                    visitTemplate += '<th>';
                    visitTemplate += 'Visit';
                    visitTemplate += '</th>';
                    visitTemplate += '<th>';
                    visitTemplate += 'Date';
                    visitTemplate += '</th>';
                    visitTemplate += '<th>';
                    visitTemplate += 'Start Time';
                    visitTemplate += '</th>';
                    visitTemplate += '<th>';
                    visitTemplate += 'End Time';
                    visitTemplate += '</th>';
                    visitTemplate += '<th>';
                    visitTemplate += 'Purpose';
                    visitTemplate += '<th>';
                    visitTemplate += 'Owner';
                    visitTemplate += '</th>	';
                    visitTemplate += '<th>';
                    visitTemplate += 'Check-In Date';
                    visitTemplate += '</th>	';
                    visitTemplate += '<th>';
                    visitTemplate += 'Check-In Time';
                    visitTemplate += '</th>	';
                    visitTemplate += '<th>';
                    visitTemplate += 'Check-Out Date';
                    visitTemplate += '</th>	';
                    visitTemplate += '<th>';
                    visitTemplate += 'Check-Out Time';
                    visitTemplate += '</th>	';
                    visitTemplate += '<th class="hidden-xs">';
                    visitTemplate += 'Status';
                    visitTemplate += '</th>';
                    visitTemplate += '<th class="hidden-xs">';
                    visitTemplate += 'Outcome';
                    visitTemplate += '</th>';
                    visitTemplate += '<th>';
                    visitTemplate += 'Outcome Description';
                    visitTemplate += '</th>	';
                    visitTemplate += '</tr>';
                    visitTemplate += '</thead>';
                    visitTemplate += '<tbody>';
                    var visitData = data.result;
                    for (var i = 0; i < visitData.length; i++) {
                        var checkAllVisits = visitData[i].checkAllVisits;
                        console.log("checkAllVisits$$ " + checkAllVisits);
                        var customerNm = visitData[i].customer.customerName + '-' + visitData[i].customer.customerLocation;     // modified by manohar
                        var customerAddress = visitData[i].customer.customerAddress1; // added by jyoti                        
                        if(!customerAddress){
                            customerAddress = "Address could not be resolved.";
                        }
                        var activity = visitData[i].title;
                        var sdateTime = visitData[i].sdateTime;
                        var sEndTime = visitData[i].sendTime;
                        var sCheckInTime = visitData[i].scheckInTime;
                        var sCheckOutTime = visitData[i].scheckOutTime;
                        var sCheckInDateTime = visitData[i].scheckInTime;
                        var sCheckOutDateTime = visitData[i].scheckOutTime;
                        var outDesc = visitData[i].outcomeDescription;
                        var purpose = visitData[i].purpose.purpose;
                        var fnm = visitData[i].owner.firstName;
                        var lnm = visitData[i].owner.lastName;
                        var owner = fnm + ' ' + lnm;
                        var status = visitData[i].status;
                        var outcome = visitData[i].outcomes.outcome;
                        var firstnm = visitData[i].assignedTo.firstName;
                        var lastnm = visitData[i].assignedTo.lastName;
                        var userName = firstnm + ' ' + lastnm;
                        var sta
                        if (status == 0) {
                            sta = "Pending";
                        } else if (status == 1) {
                            sta = "In-Progress";
                        } else if (status == 2) {
                            sta = "Completed";
                        } else if (status == 3) {
                            sta = "Cancelled";
                        }
                        sdateTime = sdateTime.split(' ');
                        var activityDate = sdateTime[0];
                        activityDate = activityDate.split('-');
                        var activityTime = sdateTime[1];
                        activityTime = activityTime.split(':');
                        var activityDateJs = convertServerDateToLocalDate(activityDate[0], activityDate[1] - 1, activityDate[2], activityTime[0], activityTime[1]);
                        sdateTime = activityDateJs.getFullYear() + "-" + (activityDateJs.getMonth() + 1) + "-" + activityDateJs.getDate() + " " + activityDateJs.getHours() + ":" + activityDateJs.getMinutes() + ":00.0";
                        sdateTime = fieldSenseseForJSDate(sdateTime);
                        sdateTime = sdateTime.split(',');
                        activityDate = sdateTime[0];
                        activityTime = sdateTime[1];
                        sEndTime = sEndTime.split(' ');
                        var activityEndDate = sEndTime[0];
                        activityEndDate = activityEndDate.split('-');
                        var activityEndTime = sEndTime[1];
                        activityEndTime = activityEndTime.split(':');
                        var activityEndDateJs = convertServerDateToLocalDate(activityEndDate[0], activityEndDate[1] - 1, activityEndDate[2], activityEndTime[0], activityEndTime[1]);
                        sEndTime = activityEndDateJs.getFullYear() + "-" + (activityEndDateJs.getMonth() + 1) + "-" + activityEndDateJs.getDate() + " " + activityEndDateJs.getHours() + ":" + activityEndDateJs.getMinutes() + ":00.0";
                        sEndTime = fieldSenseseForJSDate(sEndTime);
                        sEndTime = sEndTime.split(',');
                        activityEndDate = sEndTime[0];
                        activityEndTime = sEndTime[1];

                        //For chkin date-time 

                        sCheckInDateTime = sCheckInDateTime.split(' ');
                        var activityCheckInDate = sCheckInDateTime[0];
                        activityCheckInDate = activityCheckInDate.split('-');
                        var activityCheckInTime = sCheckInDateTime[1];
                        activityCheckInTime = activityCheckInTime.split(':');
                        var activityDateCheckInJs = convertServerDateToLocalDate(activityCheckInDate[0], activityCheckInDate[1] - 1, activityCheckInDate[2], activityCheckInTime[0], activityCheckInTime[1]);
                        sCheckInDateTime = activityDateCheckInJs.getFullYear() + "-" + (activityDateCheckInJs.getMonth() + 1) + "-" + activityDateCheckInJs.getDate() + " " + activityDateCheckInJs.getHours() + ":" + activityDateCheckInJs.getMinutes() + ":00.0";
                        sCheckInDateTime = fieldSenseseForJSDate(sCheckInDateTime);
                        sCheckInDateTime = sCheckInDateTime.split(',');
                        activityCheckInDate = sCheckInDateTime[0];
                        activityCheckInTime = sCheckInDateTime[1];

                        var chkIn = "--:--";
                        var chkInDate = "--:--";
                        var sCheckInDate = sCheckInTime.split(' ')[0];

                        if (sCheckInDate != "1970-01-01") {
                            chkInDate = activityCheckInDate;
                            chkIn = activityCheckInTime;
                        }

                        //For chkout date-time 

                        sCheckOutDateTime = sCheckOutDateTime.split(' ');
                        var activityCheckOutDate = sCheckOutDateTime[0];
                        activityCheckOutDate = activityCheckOutDate.split('-');
                        var activityCheckOutTime = sCheckOutDateTime[1];
                        activityCheckOutTime = activityCheckOutTime.split(':');
                        var activityDateCheckOutJs = convertServerDateToLocalDate(activityCheckOutDate[0], activityCheckOutDate[1] - 1, activityCheckOutDate[2], activityCheckOutTime[0], activityCheckOutTime[1]);
                        sCheckOutDateTime = activityDateCheckOutJs.getFullYear() + "-" + (activityDateCheckOutJs.getMonth() + 1) + "-" + activityDateCheckOutJs.getDate() + " " + activityDateCheckOutJs.getHours() + ":" + activityDateCheckOutJs.getMinutes() + ":00.0";
                        sCheckOutDateTime = fieldSenseseForJSDate(sCheckOutDateTime);
                        sCheckOutDateTime = sCheckOutDateTime.split(',');
                        activityCheckOutDate = sCheckOutDateTime[0];
                        activityCheckOutTime = sCheckOutDateTime[1];

                        var sCheckOutDate = sCheckOutTime.split(' ')[0];
                        var chkOut = "--:--";
                        var chkOutDate = "--:--";
                        if (sCheckOutDate != "1970-01-01" && status != 1) {   // status 1 means appointment in progress in that case chkout should be blank
                            chkOutDate = activityCheckOutDate;
                            chkOut = activityCheckOutTime;
                        }

                        //end 
                        visitTemplate += '<tr>';
                        visitTemplate += '<td>';
                        visitTemplate += userName;
                        visitTemplate += '</td>';
                        visitTemplate += '<td>';
//                        visitTemplate += customerNm;
                        visitTemplate += '<span data-toggle="tooltip" title="' + customerAddress + '">' + customerNm + '</span>'; // Feature #29845, added by jyoti
                        visitTemplate += '</td>';
                        visitTemplate += '<td>';
                        visitTemplate += activity;
                        visitTemplate += '</td>'
                        visitTemplate += '<td>';
                        visitTemplate += activityDate;
                        visitTemplate += '</td>';
                        visitTemplate += '<td>';
                        visitTemplate += activityTime;
                        visitTemplate += '</td>';
                        visitTemplate += '<td>';
                        visitTemplate += activityEndTime;
                        visitTemplate += '</td>';
                        visitTemplate += '<td>';
                        visitTemplate += purpose;
                        visitTemplate += '</td>';
                        visitTemplate += '<td>';
                        visitTemplate += owner;
                        visitTemplate += '</td>';
                        visitTemplate += '<td>';
                        visitTemplate += chkInDate;
                        visitTemplate += '</td>';
                        visitTemplate += '<td>';
                        visitTemplate += chkIn;
                        visitTemplate += '</td>';
                        visitTemplate += '<td>';
                        visitTemplate += chkOutDate;
                        visitTemplate += '</td>';
                        visitTemplate += '<td>';
                        visitTemplate += chkOut;
                        visitTemplate += '</td>';
                        visitTemplate += '<td class="hidden-xs">';
                        visitTemplate += sta;
                        visitTemplate += '</td>';
                        visitTemplate += '<td class="hidden-xs">';
                        visitTemplate += outcome;
                        visitTemplate += '</td>';
                        visitTemplate += '<td>';
                        visitTemplate += outDesc;
                        visitTemplate += '</td>';
                        visitTemplate += '</tr>';
                        exportVisit[i] = [{
                                "visitedBy": userName,
                                "cuctomerNm": customerNm,
                                "activity": activity,
                                "activityDate": activityDate,
                                "activityTime": activityTime,
                                "activityEndTime": activityEndTime,
                                "purpose": purpose,
                                "owner": owner,
                                "chkInDate": chkInDate,
                                "chkIn": chkIn,
                                "chkOutDate": chkOutDate,
                                "chkOut": chkOut,
                                "sta": sta,
                                "outcome": outcome,
                                "outDesc": outDesc,
                                "customerAddress": customerAddress
                            }];
                    }
                    visitTemplate += '</tbody>';
                    visitTemplate += '</table>';
                    $('.cls_attendance').html(visitTemplate);
                    // prepare CSV data
                    var csvData = new Array();
                    csvData.push('"Visited By","Customer","Activity","Date","Start Time","End Time","Purpose","Owner","Check-In Date","Check-In Time","Check-Out Date","Check-Out Time","Status","Outcome","Outcome Description","Customer Address"');
                    for (var i = 0; i < exportVisit.length; i++) {
                        // for (var j = 0; j < 1; j++) {
                        csvData.push('"' + exportVisit[i][0].visitedBy + '","' + exportVisit[i][0].cuctomerNm + '","' + exportVisit[i][0].activity + '","' + exportVisit[i][0].activityDate + '","' + exportVisit[i][0].activityTime + '","' + exportVisit[i][0].activityEndTime + '","' + exportVisit[i][0].purpose + '","' + exportVisit[i][0].owner + '","' + exportVisit[i][0].chkInDate + '","' + exportVisit[i][0].chkIn + '","' + exportVisit[i][0].chkOutDate + '","' + exportVisit[i][0].chkOut + '","' + exportVisit[i][0].sta + '","' + exportVisit[i][0].outcome + '","' + exportVisit[i][0].outDesc + '","' + exportVisit[i][0].customerAddress + '"');
                        //  }
                    }
                    // download stuff
                    if (csvData.length > 1) {
                        var fileName = "All_Visit.csv";
                        var buffer = csvData.join("\n");
                        var blob;
                        try {
                            blob = new Blob([buffer], {
                                "type": "text/csv;charset=utf8;"
                            });
                        } catch (e) {
                            window.BlobBuilder = window.BlobBuilder ||
                                    window.WebKitBlobBuilder ||
                                    window.MozBlobBuilder ||
                                    window.MSBlobBuilder;

                            if (e.name == 'TypeError' && window.BlobBuilder) {
                                var bb = new BlobBuilder();
                                bb.append(buffer);
                                blob = bb.getBlob("text/csv;charset=utf8;");
                            }
                            else if (e.name == "InvalidStateError") {
                                // InvalidStateError (tested on FF13 WinXP)
                                blob = new Blob([buffer], {
                                    type: "text/csv;charset=utf8;"
                                });
                            }
                        }
                        var link = document.createElement("a");
                        var myURL = window.URL || window.webkitURL;
                        if (link.download !== undefined) { // feature detection
                            // Browsers that support HTML5 download attribute
                            link.setAttribute("href", myURL.createObjectURL(blob));
                            link.setAttribute("download", fileName);
                        }
                        else if (navigator.msSaveBlob) { // IE 10+
                            navigator.msSaveBlob(blob, fileName);
                        }
                        else {
                            link.setAttribute("href", myURL.createObjectURL(blob));
                            link.setAttribute("name", fileName);
                        }
                        $('#id_exportAttendance').html('');
                        link.innerHTML = '<button type="button" class="btn btn-info fr" style="margin-right:-15px;"><i class=" fa fa-file-text"></i> Save as CSV</button>';
                        document.getElementById("id_exportAttendance").appendChild(link);
                    }
                    $('#pleaseWaitDialog').modal('hide');
                    $('#sample_5').dataTable({
                        "aaSorting": [],
                        "sPaginationType": "bootstrap_full_number",
                        //  "scrollY":"500px",
                        //"scrollCollapse": true,
                        //"paging": true,   
                        "aLengthMenu": [
                            [-1, 5, 10, 15, 20, 50, 100],
                            ["All", 5, 10, 15, 20, 50, 100] // change per page values here
                        ],
                        // set the initial value
                        "iDisplayLength": 20,
                        "bFilter": false
                    });

                    jQuery('#sample_5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
                    jQuery('#sample_5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
                    jQuery('#sample_5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown
                    //  TableAdvanced.init();
                } else {
                    fieldSenseTosterError(data.errorMessage, true);
                    $('#pleaseWaitDialog').modal('hide');
                }
            } else {
                if (data.errorCode == '0000') {
                    console.log("checkAllVisits1 " + data.result.checkAllVisits);
                    myTable = $("#sample_5").DataTable();
                    //   myTable.fixedHeader.enable();
                    if (myTable != null)
                        myTable.destroy();
                    $('.cls_attendance').html('');
                    visitTemplate += '<table class="table table-striped table-bordered table-hover" id="sample_5">';
                    visitTemplate += '<thead>';
                    visitTemplate += '<tr>';
                    visitTemplate += '<th>';
                    visitTemplate += 'Customer';
                    visitTemplate += '</th>';
                    visitTemplate += '<th>';
                    visitTemplate += 'Visit';
                    visitTemplate += '</th>';
                    visitTemplate += '<th>';
                    visitTemplate += 'Date';
                    visitTemplate += '</th>';
                    visitTemplate += '<th>';
                    visitTemplate += 'Start Time';
                    visitTemplate += '</th>';
                    visitTemplate += '<th>';
                    visitTemplate += 'End Time';
                    visitTemplate += '</th>';
                    visitTemplate += '<th>';
                    visitTemplate += 'Purpose';
                    visitTemplate += '<th>';
                    visitTemplate += 'Owner';
                    visitTemplate += '</th>	';
                    visitTemplate += '<th>';
                    visitTemplate += 'Check-In Date';
                    visitTemplate += '</th>	';
                    visitTemplate += '<th>';
                    visitTemplate += 'Check-In Time';
                    visitTemplate += '</th>	';
                    visitTemplate += '<th>';
                    visitTemplate += 'Check-Out Date';
                    visitTemplate += '</th>	';
                    visitTemplate += '<th>';
                    visitTemplate += 'Check-Out Time';
                    visitTemplate += '</th>	';
                    visitTemplate += '<th class="hidden-xs">';
                    visitTemplate += 'Status';
                    visitTemplate += '</th>';
                    visitTemplate += '<th class="hidden-xs">';
                    visitTemplate += 'Outcome';
                    visitTemplate += '</th>';
                    visitTemplate += '<th>';
                    visitTemplate += 'Outcome Description';
                    visitTemplate += '</th>	';
                    visitTemplate += '</tr>';
                    visitTemplate += '</thead>';
                    visitTemplate += '<tbody>';
                    var visitData = data.result;
                    for (var i = 0; i < visitData.length; i++) {
                        var checkAllVisits = visitData[i].checkAllVisits;
                        var customerNm = visitData[i].customer.customerName + '-' + visitData[i].customer.customerLocation;     // modified by manohar
                        var customerAddress = visitData[i].customer.customerAddress1; // added by jyoti
                        if(!customerAddress){
                            customerAddress = "Address could not be resolved.";
                        }
                        var activity = visitData[i].title;
                        var sdateTime = visitData[i].sdateTime;
                        var sEndTime = visitData[i].sendTime;
                        var sCheckInTime = visitData[i].scheckInTime;
                        var sCheckOutTime = visitData[i].scheckOutTime;
                        var sCheckInDateTime = visitData[i].scheckInTime;
                        var sCheckOutDateTime = visitData[i].scheckOutTime;
                        var outDesc = visitData[i].outcomeDescription;
                        var purpose = visitData[i].purpose.purpose;
                        var fnm = visitData[i].owner.firstName;
                        var lnm = visitData[i].owner.lastName;
                        var owner = fnm + ' ' + lnm;
                        var status = visitData[i].status;
                        var outcome = visitData[i].outcomes.outcome;
                        var firstnm = visitData[i].assignedTo.firstName;
                        var lastnm = visitData[i].assignedTo.lastName;
                        var userName = firstnm + ' ' + lastnm;
                        var sta
                        if (status == 0) {
                            sta = "Pending";
                        } else if (status == 1) {
                            sta = "In-Progress";
                        } else if (status == 2) {
                            sta = "Completed";
                        } else if (status == 3) {
                            sta = "Cancelled";
                        }
                        sdateTime = sdateTime.split(' ');
                        var activityDate = sdateTime[0];
                        activityDate = activityDate.split('-');
                        var activityTime = sdateTime[1];
                        activityTime = activityTime.split(':');
                        var activityDateJs = convertServerDateToLocalDate(activityDate[0], activityDate[1] - 1, activityDate[2], activityTime[0], activityTime[1]);
                        sdateTime = activityDateJs.getFullYear() + "-" + (activityDateJs.getMonth() + 1) + "-" + activityDateJs.getDate() + " " + activityDateJs.getHours() + ":" + activityDateJs.getMinutes() + ":00.0";
                        sdateTime = fieldSenseseForJSDate(sdateTime);
                        sdateTime = sdateTime.split(',');
                        activityDate = sdateTime[0];
                        activityTime = sdateTime[1];
                        sEndTime = sEndTime.split(' ');
                        var activityEndDate = sEndTime[0];
                        activityEndDate = activityEndDate.split('-');
                        var activityEndTime = sEndTime[1];
                        activityEndTime = activityEndTime.split(':');
                        var activityEndDateJs = convertServerDateToLocalDate(activityEndDate[0], activityEndDate[1] - 1, activityEndDate[2], activityEndTime[0], activityEndTime[1]);
                        sEndTime = activityEndDateJs.getFullYear() + "-" + (activityEndDateJs.getMonth() + 1) + "-" + activityEndDateJs.getDate() + " " + activityEndDateJs.getHours() + ":" + activityEndDateJs.getMinutes() + ":00.0";
                        sEndTime = fieldSenseseForJSDate(sEndTime);
                        sEndTime = sEndTime.split(',');
                        activityEndDate = sEndTime[0];
                        activityEndTime = sEndTime[1];

                        //For chkin date-time 

                        sCheckInDateTime = sCheckInDateTime.split(' ');
                        var activityCheckInDate = sCheckInDateTime[0];
                        activityCheckInDate = activityCheckInDate.split('-');
                        var activityCheckInTime = sCheckInDateTime[1];
                        activityCheckInTime = activityCheckInTime.split(':');
                        var activityDateCheckInJs = convertServerDateToLocalDate(activityCheckInDate[0], activityCheckInDate[1] - 1, activityCheckInDate[2], activityCheckInTime[0], activityCheckInTime[1]);
                        sCheckInDateTime = activityDateCheckInJs.getFullYear() + "-" + (activityDateCheckInJs.getMonth() + 1) + "-" + activityDateCheckInJs.getDate() + " " + activityDateCheckInJs.getHours() + ":" + activityDateCheckInJs.getMinutes() + ":00.0";
                        sCheckInDateTime = fieldSenseseForJSDate(sCheckInDateTime);
                        sCheckInDateTime = sCheckInDateTime.split(',');
                        activityCheckInDate = sCheckInDateTime[0];
                        activityCheckInTime = sCheckInDateTime[1];

                        var chkIn = "--:--";
                        var chkInDate = "--:--";
                        var sCheckInDate = sCheckInTime.split(' ')[0];

                        if (sCheckInDate != "1970-01-01") {
                            chkInDate = activityCheckInDate;
                            chkIn = activityCheckInTime;
                        }

                        //For chkout date-time 

                        sCheckOutDateTime = sCheckOutDateTime.split(' ');
                        var activityCheckOutDate = sCheckOutDateTime[0];
                        activityCheckOutDate = activityCheckOutDate.split('-');
                        var activityCheckOutTime = sCheckOutDateTime[1];
                        activityCheckOutTime = activityCheckOutTime.split(':');
                        var activityDateCheckOutJs = convertServerDateToLocalDate(activityCheckOutDate[0], activityCheckOutDate[1] - 1, activityCheckOutDate[2], activityCheckOutTime[0], activityCheckOutTime[1]);
                        sCheckOutDateTime = activityDateCheckOutJs.getFullYear() + "-" + (activityDateCheckOutJs.getMonth() + 1) + "-" + activityDateCheckOutJs.getDate() + " " + activityDateCheckOutJs.getHours() + ":" + activityDateCheckOutJs.getMinutes() + ":00.0";
                        sCheckOutDateTime = fieldSenseseForJSDate(sCheckOutDateTime);
                        sCheckOutDateTime = sCheckOutDateTime.split(',');
                        activityCheckOutDate = sCheckOutDateTime[0];
                        activityCheckOutTime = sCheckOutDateTime[1];

                        var sCheckOutDate = sCheckOutTime.split(' ')[0];
                        var chkOut = "--:--";
                        var chkOutDate = "--:--";
                        if (sCheckOutDate != "1970-01-01" && status != 1) {   // status 1 means appointment in progress in that case chkout should be blank
                            chkOutDate = activityCheckOutDate;
                            chkOut = activityCheckOutTime;
                        }

                        //end
                        visitTemplate += '<tr>';
                        visitTemplate += '<td>';
//                        visitTemplate += customerNm;
                        visitTemplate += '<span data-toggle="tooltip" title="' + customerAddress + '">' + customerNm + '</span>'; // Feature #29845, added by jyoti
                        visitTemplate += '</td>';
                        visitTemplate += '<td>';
                        visitTemplate += activity;
                        visitTemplate += '</td>'
                        visitTemplate += '<td>';
                        visitTemplate += activityDate;
                        visitTemplate += '</td>';
                        visitTemplate += '<td>';
                        visitTemplate += activityTime;
                        visitTemplate += '</td>';
                        visitTemplate += '<td>';
                        visitTemplate += activityEndTime;
                        visitTemplate += '</td>';
                        visitTemplate += '<td>';
                        visitTemplate += purpose;
                        visitTemplate += '</td>';
                        visitTemplate += '<td>';
                        visitTemplate += owner;
                        visitTemplate += '</td>';
                        visitTemplate += '<td>';
                        visitTemplate += chkInDate;
                        visitTemplate += '</td>';
                        visitTemplate += '<td>';
                        visitTemplate += chkIn;
                        visitTemplate += '</td>';
                        visitTemplate += '<td>';
                        visitTemplate += chkOutDate;
                        visitTemplate += '</td>';
                        visitTemplate += '<td>';
                        visitTemplate += chkOut;
                        visitTemplate += '</td>';
                        visitTemplate += '<td class="hidden-xs">';
                        visitTemplate += sta;
                        visitTemplate += '</td>';
                        visitTemplate += '<td class="hidden-xs">';
                        visitTemplate += outcome;
                        visitTemplate += '</td>';
                        visitTemplate += '<td>';
                        visitTemplate += outDesc;
                        visitTemplate += '</td>';
                        visitTemplate += '</tr>';
                        exportVisit[i] = [{
                                "cuctomerNm": customerNm,
                                "activity": activity,
                                "activityDate": activityDate,
                                "activityTime": activityTime,
                                "activityEndTime": activityEndTime,
                                "purpose": purpose,
                                "owner": owner,
                                "chkInDate": chkInDate,
                                "chkIn": chkIn,
                                "chkOutDate": chkOutDate,
                                "chkOut": chkOut,
                                "sta": sta,
                                "outcome": outcome,
                                "outDesc": outDesc,
                                "customerAddress": customerAddress
                            }];
                    }
                    visitTemplate += '</tbody>';
                    visitTemplate += '</table>';
                    $('.cls_attendance').html(visitTemplate);
                    // prepare CSV data
                    var csvData = new Array();
                    csvData.push('"Customer","Activity","Date","Start Time","End Time","Purpose","Owner","Check-In Date","Check-In Time","Check-Out Date","Check-Out Time","Status","Outcome","Outcome Description","Customer Address"');
                    for (var i = 0; i < exportVisit.length; i++) {
                        // for (var j = 0; j < 1; j++) {
                        csvData.push('"' + exportVisit[i][0].cuctomerNm + '","' + exportVisit[i][0].activity + '","' + exportVisit[i][0].activityDate + '","' + exportVisit[i][0].activityTime + '","' + exportVisit[i][0].activityEndTime + '","' + exportVisit[i][0].purpose + '","' + exportVisit[i][0].owner + '","' + exportVisit[i][0].chkInDate + '","' + exportVisit[i][0].chkIn + '","' + exportVisit[i][0].chkOutDate + '","' + exportVisit[i][0].chkOut + '","' + exportVisit[i][0].sta + '","' + exportVisit[i][0].outcome + '","' + exportVisit[i][0].outDesc + '","' + exportVisit[i][0].customerAddress + '"');
                        //  }
                    }
                    // download stuff
                    if (csvData.length > 1) {                       
                        if(emp_code==0)  // added by manohar
                        {   
                               var fileName = userName + "_Visit.csv";  
                        }
                        else{
                                var fileName = userName + "_Visit_" +emp_code+ ".csv";
                        }
                        var buffer = csvData.join("\n");
                        var blob;
                        try {
                            blob = new Blob([buffer], {
                                "type": "text/csv;charset=utf8;"
                            });
                        } catch (e) {
                            window.BlobBuilder = window.BlobBuilder ||
                                    window.WebKitBlobBuilder ||
                                    window.MozBlobBuilder ||
                                    window.MSBlobBuilder;

                            if (e.name == 'TypeError' && window.BlobBuilder) {
                                var bb = new BlobBuilder();
                                bb.append(buffer);
                                blob = bb.getBlob("text/csv;charset=utf8;");
                            }
                            else if (e.name == "InvalidStateError") {
                                // InvalidStateError (tested on FF13 WinXP)
                                blob = new Blob([buffer], {
                                    type: "text/csv;charset=utf8;"
                                });
                            }
                        }
                        var link = document.createElement("a");
                        var myURL = window.URL || window.webkitURL;
                        if (link.download !== undefined) { // feature detection
                            // Browsers that support HTML5 download attribute
                            link.setAttribute("href", myURL.createObjectURL(blob));
                            link.setAttribute("download", fileName);
                        }
                        else if (navigator.msSaveBlob) { // IE 10+
                            navigator.msSaveBlob(blob, fileName);
                        }
                        else {
                            link.setAttribute("href", myURL.createObjectURL(blob));
                            link.setAttribute("name", fileName);
                        }
                        $('#id_exportAttendance').html('');
                        link.innerHTML = '<button type="button" class="btn btn-info fr" style="margin-right:-15px;"><i class=" fa fa-file-text"></i> Save as CSV</button>';
                        document.getElementById("id_exportAttendance").appendChild(link);
                    }
                    $('#pleaseWaitDialog').modal('hide');
                    $('#sample_5').dataTable({
                        "aaSorting": [],
                        "sPaginationType": "bootstrap_full_number",
                        //  "scrollY":"500px",
                        //"scrollCollapse": true,
                        //"paging": true,   
                        "aLengthMenu": [
                            [-1, 5, 10, 15, 20, 50, 100],
                            ["All", 5, 10, 15, 20, 50, 100] // change per page values here
                        ],
                        // set the initial value
                        "iDisplayLength": 20,
                        "bFilter": false
                    });

                    jQuery('#sample_5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
                    jQuery('#sample_5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
                    jQuery('#sample_5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown
                    //  TableAdvanced.init();
                } else {
                    fieldSenseTosterError(data.errorMessage, true);
                    $('#pleaseWaitDialog').modal('hide');
                }
            }
        }
    });
}

function expense() {
    var exportExpense = new Array();
    $("#id_exportAttendance").html('<button type="button" class="btn btn-info fr" style="margin-right:-15px;"><i class=" fa fa-file-text" id="id_exportAttendanceChild"></i> Save as CSV</button>');
    var teamMember = document.getElementById("id_memberName").value;
    var emp_code=users_emp_codes_map[teamMember];   // added by manohar
    if (teamMember == 'select') {
        fieldSenseTosterError("Please select any member", true);
        return false;
    }
    var startTime = document.getElementById("id_startDate").value;
    var endTime = document.getElementById("id_endDate").value;
    var startTimeSplit = startTime.split('/');
    var date = startTimeSplit[0];
    var month = startTimeSplit[1];
    var year = startTimeSplit[2];
    startTime = year + '-' + month + '-' + date;
    var ensdTimeSplit = endTime.split('/');
    var date1 = ensdTimeSplit[0];
    var month1 = ensdTimeSplit[1];
    var year1 = ensdTimeSplit[2];
    endTime = year1 + '-' + month1 + '-' + date1;
    var expenseTemplate = '';
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/reports/expense/" + teamMember + "/" + startTime + "/" + endTime,
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
                clevertap.event.push("Expense Report", {
//                    "UserId": loginUserIdcookie,
//                    "User Name": loginUserNamecookie,
//                    "AccountId": accountIdcookie,
                    "Source": "Web",
                    "Account name": accountNamecookie,
                    "UserRolecookie": UserRolecookie,
                });
                myTable = $("#sample_5").DataTable();
                if (myTable != null)
                    myTable.destroy();
                $('.cls_attendance').html('');
                expenseTemplate += '<table class="table table-striped table-bordered table-hover" id="sample_5">';
                expenseTemplate += '<thead>';
                expenseTemplate += '<tr>';
                expenseTemplate += '<th>';
                expenseTemplate += 'Expenses';
                expenseTemplate += '</th>';
                expenseTemplate += '<th>';
                expenseTemplate += 'Expense Category';
                expenseTemplate += '</th>';
                expenseTemplate += '<th>';
                expenseTemplate += 'Date / Time';
                expenseTemplate += '</th>';
                expenseTemplate += '<th>';
                expenseTemplate += 'Date';
                expenseTemplate += '</th>';
                expenseTemplate += '<th>';
                expenseTemplate += 'Customer';
                expenseTemplate += '<th>';
                expenseTemplate += 'Visit';
                expenseTemplate += '</th>';
                expenseTemplate += '<th>';
                expenseTemplate += 'Status';
                expenseTemplate += '</th>';
                expenseTemplate += '<th class="hidden-xs">';
                expenseTemplate += 'Approved / Rejected By';
                expenseTemplate += '</th>';
                expenseTemplate += '<th class="hidden-xs">';
                expenseTemplate += 'Approved / Rejected On';
                expenseTemplate += '</th>';
                expenseTemplate += '<th class="hidden-xs">';
                expenseTemplate += 'Rejected Reason';
                expenseTemplate += '</th>';
                expenseTemplate += '<th>';
                expenseTemplate += 'Amount Claimed';
                expenseTemplate += '</th>';
                expenseTemplate += '<th class="hidden-xs">';
                expenseTemplate += 'Disbursed Amount';
                expenseTemplate += '</th>';
                expenseTemplate += '</tr>';
                expenseTemplate += '</thead>';
                expenseTemplate += '<tbody>';
                var expenseData = data.result;
                var expenseDataLength = expenseData.length;
                var dateForCheck = 0;
                var amt = 0;
                var approvedAmt = 0;
                var rejectedAmt = 0;
                var disbursedamount = 0;
                firstdraw = true;
                totalAmt1 = [];
                totalAmt = new Array();
                var totalExpensesClaimed = 0;
                var totalDisbExpensesClaimed = 0;
                for (var i = 0; i < expenseData.length; i++) {
                    var expenseName = expenseData[i].expenseName;
                    var expenseId = expenseData[i].id;
                    var ammount = expenseData[i].amountSpent;
                    totalExpensesClaimed += ammount;
                    //                    var dateTime = fieldSenseDateTimeForExpense(expenseData[i].expenseTime);
                    var serverDate = expenseData[i].expenseTime;
                    var localDate = convertServerDateToLocalDate(serverDate.year + 1900, serverDate.month, serverDate.date, serverDate.hours, serverDate.minutes)
                    var dateTime = fieldSenseDateTimeForExpenseJsDate(localDate);
                    var customer = expenseData[i].customerName;
                    var activity = expenseData[i].appointment.title;
                    var status = expenseData[i].status;
                    var approveByFnm = expenseData[i].approvedOrRjectedBy.firstName;
                    var approveByLnm = expenseData[i].approvedOrRjectedBy.lastName;
                    var approveBy = approveByFnm + ' ' + approveByLnm;
                    var fnm = expenseData[i].user.firstName;
                    var lnm = expenseData[i].user.lastName;
                    var userName = fnm + ' ' + lnm;
                    var st;
                    var reason;
                    var dis_amt;
                    serverDate = expenseData[i].approvedOrRejectedOn;
                    localDate = convertServerDateToLocalDate(serverDate.year + 1900, serverDate.month, serverDate.date, serverDate.hours, serverDate.minutes)
                    var approveOn = fieldSenseDateTimeForExpenseJsDate(localDate);
                    if (approveOn == "11 Nov 1111 04:41 pm" || status == 0) {
                        approveOn = '';
                    }
                    if (status == 0) {
                        approveBy = '';
                    }
                    var rejectedReason = expenseData[i].rejectedReson;
                    var eCategory = expenseData[i].exp_category_name;
                    var disburse_amount = expenseData[i].disburse_amount;
                    totalDisbExpensesClaimed += disburse_amount;
                    var date = dateTime.split(' ');
                    var d = date[0];
                    var m = date[1];
                    var y = date[2];
                    date = d + ' ' + m + ' ' + y;
                    expenseTemplate += '<tr>';
                    expenseTemplate += '<td><a data-toggle="modal" href="#expenses" onclick="getExpenseStatus(' + expenseId + ');"</a>' + expenseName + '</td>';
                    expenseTemplate += '<td>' + eCategory + '</td>';
                    expenseTemplate += '<td>' + dateTime + '</td>';
                    expenseTemplate += '<td>' + date + '</td>';
                    expenseTemplate += '<td>' + customer + '</td>';
                    expenseTemplate += '<td>' + activity + '</td>';
                    expenseTemplate += '<td>';
                    if (status == 0) {
                        st = "Pending Approval by Reporting Head";
                        expenseTemplate += '<span class="label label-sm label-default" title="Pending Approval by Reporting Head">Pending</span>';
                    } else if (status == 1) {
                        st = "Pending Approval by Accounts";
                        expenseTemplate += '<span class="label label-sm label-default" title="Pending Approval by Accounts">Pending</span>';
                    } else if (status == 2) {
                        st = "Rejected by Reporting Head";
                        expenseTemplate += '<span class="label label-sm label-warning" title="Rejected by Reporting Head">Rejected</span>';
                    } else if (status == 3) {
                        st = "Approved";
                        expenseTemplate += '<span class="label label-sm label-info" title="Approved">Approved</span>';
                    } else if (status == 4) {
                        st = "Rejected by Accounts";
                        expenseTemplate += '<span class="label label-sm label-warning" title="Rejected by Accounts">Rejected</span>';
                    } else if (status == 5) {
                        st = "Disbursed";
                        expenseTemplate += '<span class="label label-sm label-success" title="Disbursed">Disbursed</span>';
                    }
                    expenseTemplate += '</td>';
                    expenseTemplate += '<td class="hidden-xs">' + approveBy + '</td>';
                    expenseTemplate += '<td class="hidden-xs">' + approveOn + '</td>';
                    if (status == 0 || status == 1 || status == 3 || status == 5) {
                        reason = "-";
                        expenseTemplate += '<td class="hidden-xs">-</td>';
                    } else {
                        reason = rejectedReason;
                        expenseTemplate += '<td class="hidden-xs">' + rejectedReason + '</td>';
                    }

                    expenseTemplate += '<td align="right">' + ammount + '</td>';

                    if (status == 5) {
                        dis_amt = disburse_amount;
                        expenseTemplate += '<td class="hidden-xs"  align="right" >' + dis_amt + '</td>';
                    } else {
                        dis_amt = 0;
                        expenseTemplate += '<td class="hidden-xs"  align="right" >' + dis_amt + '</td>';
                    }
                    expenseTemplate += '</tr>'
                    if (i == 0) {
                        dateForCheck = date;
                        //                        if (status == 1) {
                        //                            approvedAmt = ammount;
                        //                        }
                        //                        if (status == 2) {
                        //                            rejectedAmt = ammount;
                        //                        }
                        totalAmt[0] = {
                            "date": date,
                            "amt": ammount,
                            "approvedAmt": approvedAmt,
                            "rejectedAmt": rejectedAmt,
                            "disbursedamount": disbursedamount
                        };
                    }
                    if (dateForCheck == date) {
                        amt += ammount;
                        if (status == 3) {
                            approvedAmt += ammount;
                        }
                        if (status == 2 || status == 4) {
                            rejectedAmt += ammount;
                        }
                        if (status == 5) {
                            disbursedamount += disburse_amount;
                        }
                        for (var j = 0; j < totalAmt.length; j++) {
                            if (totalAmt[j].date == date) {
                                totalAmt[j] = {
                                    "date": date,
                                    "amt": amt,
                                    "approvedAmt": approvedAmt,
                                    "rejectedAmt": rejectedAmt,
                                    "disbursedamount": disbursedamount
                                };
                            }
                        }
                    } else {
                        dateForCheck = date;
                        amt = ammount;
                        approvedAmt = 0;
                        rejectedAmt = 0;
                        disbursedamount = 0;
                        if (status == 3) {
                            approvedAmt = ammount;
                        }
                        if (status == 2 || status == 4) {
                            rejectedAmt = ammount;
                        }
                        if (status == 5) {
                            disbursedamount = disburse_amount;
                        }
                        totalAmt[totalAmt.length] = {
                            "date": date,
                            "amt": amt,
                            "approvedAmt": approvedAmt,
                            "rejectedAmt": rejectedAmt,
                            "disbursedamount": disbursedamount
                        };
                    }
                    exportExpense[i] = [{
                            "expenseName": expenseName,
                            "eCategory": eCategory,
                            "dateTime": dateTime,
                            "customer": customer,
                            "activity": activity,
                            "st": st,
                            "approveBy": approveBy,
                            "approveOn": approveOn,
                            "reason": reason,
                            "ammount": ammount,
                            "dis_amt": dis_amt
                        }];
                }

                if (expenseDataLength != 0) {
                    expenseTemplate += '<tfoot> <tr>';
                    expenseTemplate += '<th colspan="10" style="text-align:right">Total: </th>';
                    expenseTemplate += '<th></th>';
                    expenseTemplate += '<th style="text-align:right" ></th>';
                    expenseTemplate += '</tr> </tfoot>';
                    expenseTemplate += '</tbody>';
                    expenseTemplate += '</table>';
                    $('.cls_attendance').html(expenseTemplate);

                    $('#sample_5').on('draw.dt', function () {
                        if (firstdraw != true) {
                            var datedraw = []
                            $('#sample_5').find('tr').find('td:eq(3)').each(function () {
                                var date = $(this).text().split(' ');
                                var d = date[0];
                                var m = date[1];
                                var y = date[2];
                                datedraw.push(d + ' ' + m + ' ' + y);
                            });

                            var unique = datedraw.filter(function (itm, i, datedraw) {
                                return i == datedraw.indexOf(itm);
                            });
                            for (var j = 0; j < totalAmt.length; j++) {
                                for (var k = 0; k < unique.length; k++) {
                                    if (totalAmt[j].date == unique[k]) {
                                        totalAmt1.push({"date": totalAmt[j].date, "amt": totalAmt[j].amt, "approvedAmt": totalAmt[j].approvedAmt, "rejectedAmt": totalAmt[j].rejectedAmt, "disbursedamount": totalAmt[j].disbursedamount});
                                    }
                                }
                            }
                            getdifferentamount();
                        }
                    });

                    // prepare CSV data
                    var csvData = new Array();
                    csvData.push('"Expenses","Expense Category","Date / Time","Customer","Activity","Status","Approved / Rejected By","Approved / Rejected On","Rejected Reason","Amount Claimed","Disbursed Amount"');
                    for (var i = 0; i < exportExpense.length; i++) {
                        // for (var j = 0; j < 1; j++) {
                        csvData.push('"' + exportExpense[i][0].expenseName + '","' + exportExpense[i][0].eCategory + '","' + exportExpense[i][0].dateTime + '","' + exportExpense[i][0].customer + '","' + exportExpense[i][0].activity + '","' + exportExpense[i][0].st + '","' + exportExpense[i][0].approveBy + '","' + exportExpense[i][0].approveOn + '","' + exportExpense[i][0].reason + '","' + exportExpense[i][0].ammount + '","' + exportExpense[i][0].dis_amt + '"');
                        // }
                    }

                    csvData.push('" "," "," "," "," ",""," "," ","Total Expenses: ","' + totalExpensesClaimed + '","' + totalDisbExpensesClaimed + '"');

                    // download stuff
                    if (csvData.length > 1) {
                        if(emp_code.length==0){ // added by manohar
                            var fileName = userName + "_Expense.csv";
                        }
                        else
                        {
                            var fileName = userName + "_Expense_"+emp_code+".csv";
                        }
                        var buffer = csvData.join("\n");
                        var blob;
                        try {
                            blob = new Blob([buffer], {
                                "type": "text/csv;charset=utf8;"
                            });
                        } catch (e) {
                            window.BlobBuilder = window.BlobBuilder ||
                                    window.WebKitBlobBuilder ||
                                    window.MozBlobBuilder ||
                                    window.MSBlobBuilder;

                            if (e.name == 'TypeError' && window.BlobBuilder) {
                                var bb = new BlobBuilder();
                                bb.append(buffer);
                                blob = bb.getBlob("text/csv;charset=utf8;");
                            }
                            else if (e.name == "InvalidStateError") {
                                // InvalidStateError (tested on FF13 WinXP)
                                blob = new Blob([buffer], {
                                    type: "text/csv;charset=utf8;"
                                });
                            }
                        }
                        var link = document.createElement("a");
                        var myURL = window.URL || window.webkitURL;
                        if (link.download !== undefined) { // feature detection
                            // Browsers that support HTML5 download attribute
                            link.setAttribute("href", myURL.createObjectURL(blob));
                            link.setAttribute("download", fileName);
                        }
                        else if (navigator.msSaveBlob) { // IE 10+
                            navigator.msSaveBlob(blob, fileName);
                        }
                        else {
                            link.setAttribute("href", myURL.createObjectURL(blob));
                            link.setAttribute("name", fileName);
                        }
                        $('#id_exportAttendance').html('');
                        link.innerHTML = '<button type="button" class="btn btn-info fr" style="margin-right:-15px;"><i class=" fa fa-file-text"></i> Save as CSV</button>';
                        document.getElementById("id_exportAttendance").appendChild(link);
                    }
                    $('#pleaseWaitDialog').modal('hide');
                    dataTableForExpenseReport();
                    //                TableAdvanced.init();

                }
                else {
                    expenseTemplate += '</tbody>';
                    expenseTemplate += '</table>';
                    $('.cls_attendance').html(expenseTemplate);

                    $('#sample_5').on('draw.dt', function () {
                        if (firstdraw != true) {
                            var datedraw = []
                            $('#sample_5').find('tr').find('td:eq(3)').each(function () {
                                var date = $(this).text().split(' ');
                                var d = date[0];
                                var m = date[1];
                                var y = date[2];
                                datedraw.push(d + ' ' + m + ' ' + y);
                            });

                            var unique = datedraw.filter(function (itm, i, datedraw) {
                                return i == datedraw.indexOf(itm);
                            });
                            for (var j = 0; j < totalAmt.length; j++) {
                                for (var k = 0; k < unique.length; k++) {
                                    if (totalAmt[j].date == unique[k]) {
                                        totalAmt1.push({"date": totalAmt[j].date, "amt": totalAmt[j].amt, "approvedAmt": totalAmt[j].approvedAmt, "rejectedAmt": totalAmt[j].rejectedAmt, "disbursedamount": totalAmt[j].disbursedamount});
                                    }
                                }
                            }
                            getdifferentamount();
                        }
                    });

                    // prepare CSV data
                    var csvData = new Array();
                    csvData.push('"Expenses","Expense Category","Date / Time","Customer","Activity","Status","Approved / Rejected By","Approved / Rejected On","Rejected Reason","Amount","Disbursed Amount"');
                    for (var i = 0; i < exportExpense.length; i++) {
                        // for (var j = 0; j < 1; j++) {
                        csvData.push('"' + exportExpense[i][0].expenseName + '","' + exportExpense[i][0].eCategory + '","' + exportExpense[i][0].dateTime + '","' + exportExpense[i][0].customer + '","' + exportExpense[i][0].activity + '","' + exportExpense[i][0].st + '","' + exportExpense[i][0].approveBy + '","' + exportExpense[i][0].approveOn + '","' + exportExpense[i][0].reason + '","' + exportExpense[i][0].ammount + '","' + exportExpense[i][0].dis_amt + '"');
                        // }
                    }
                    csvData.push('" " ," "," "," "," ",""," "," ","Total: ","' + totalExpensesClaimed + '","' + totalDisbExpensesClaimed + '"');

                    // download stuff
                    if (csvData.length > 1) {
                        if(emp_code.length==0){ // added by manohar
                            var fileName = userName + "_Expense.csv";
                        }
                        else
                        {
                            var fileName = userName + "_Expense_"+emp_code+".csv";
                        }
                        var buffer = csvData.join("\n");
                        var blob;
                        try {
                            blob = new Blob([buffer], {
                                "type": "text/csv;charset=utf8;"
                            });
                        } catch (e) {
                            window.BlobBuilder = window.BlobBuilder ||
                                    window.WebKitBlobBuilder ||
                                    window.MozBlobBuilder ||
                                    window.MSBlobBuilder;

                            if (e.name == 'TypeError' && window.BlobBuilder) {
                                var bb = new BlobBuilder();
                                bb.append(buffer);
                                blob = bb.getBlob("text/csv;charset=utf8;");
                            }
                            else if (e.name == "InvalidStateError") {
                                // InvalidStateError (tested on FF13 WinXP)
                                blob = new Blob([buffer], {
                                    type: "text/csv;charset=utf8;"
                                });
                            }
                        }
                        var link = document.createElement("a");
                        var myURL = window.URL || window.webkitURL;
                        if (link.download !== undefined) { // feature detection
                            // Browsers that support HTML5 download attribute
                            link.setAttribute("href", myURL.createObjectURL(blob));
                            link.setAttribute("download", fileName);
                        }
                        else if (navigator.msSaveBlob) { // IE 10+
                            navigator.msSaveBlob(blob, fileName);
                        }
                        else {
                            link.setAttribute("href", myURL.createObjectURL(blob));
                            link.setAttribute("name", fileName);
                        }
                        $('#id_exportAttendance').html('');
                        link.innerHTML = '<button type="button" class="btn btn-info fr" style="margin-right:-15px;"><i class=" fa fa-file-text"></i> Save as CSV</button>';
                        document.getElementById("id_exportAttendance").appendChild(link);
                    }
                    $('#pleaseWaitDialog').modal('hide');
                    dataTableForExpenseReport1();
                    //                TableAdvanced.init();

                }
            }
            else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
            }
        }
    });
}

function travelReports() {
    var exportTravelReport = new Array();
    $("#id_exportAttendance").html('<button type="button" class="btn btn-info fr" style="margin-right:-15px;"><i class=" fa fa-file-text" id="id_exportAttendanceChild"></i> Save as CSV</button>');
    var teamMember = document.getElementById("id_memberName").value;
    var emp_code=users_emp_codes_map[teamMember];   // added by manohar
    if (teamMember == 'select') {
        fieldSenseTosterError("Please select any member", true);
        return false;
    }
    var selectedUserName = $("#id_memberName option:selected").html();//Added By Awaneesh

    var startTime = document.getElementById("id_startDate").value;
    var startTimeSplit = startTime.split('/');
    var date = startTimeSplit[0];
    var month = Number(startTimeSplit[1]) - 1;
    var year = startTimeSplit[2];
    var today = new Date(year, month, date);
    var dateToServerToday = convertLocalDateToServerDate(today.getFullYear(), today.getMonth(), today.getDate(), today.getHours(), today.getMinutes());
    var monthToday = dateToServerToday.getMonth() + 1;
    if (monthToday < 10) {
        monthToday = '0' + monthToday;
    }
    var dateToday = dateToServerToday.getDate();
    if (dateToday < 10) {
        dateToday = '0' + dateToday;
    }
    var hoursToday = dateToServerToday.getHours();
    if (hoursToday < 10) {
        hoursToday = '0' + hoursToday;
    }
    var minutesToday = dateToServerToday.getMinutes();
    if (minutesToday < 10) {
        minutesToday = '0' + minutesToday;
    }
    dateToServerToday = dateToServerToday.getFullYear() + "-" + monthToday + "-" + dateToday + " " + hoursToday + ":" + minutesToday + ':00';
    var tomorrow = new Date(year, month, date); //Changed By Awaneesh
    tomorrow.setDate(today.getDate() + 1);
    tomorrow.setHours(0, 0, 0, 0);

    var dateToServerTomarrow = convertLocalDateToServerDate(tomorrow.getFullYear(), tomorrow.getMonth(), tomorrow.getDate(), tomorrow.getHours(), tomorrow.getMinutes());
    var monthTomarrow = dateToServerTomarrow.getMonth() + 1;

    if (monthTomarrow < 10) {
        monthTomarrow = '0' + monthTomarrow;
    }

    var dateTomarrow = dateToServerTomarrow.getDate();
    if (dateTomarrow < 10) {
        dateTomarrow = '0' + dateTomarrow;
    }
    var hoursTomarrow = dateToServerTomarrow.getHours();
    if (hoursTomarrow < 10) {
        hoursTomarrow = '0' + hoursTomarrow;
    }
    var minutesTomarrow = dateToServerTomarrow.getMinutes();
    if (minutesTomarrow < 10) {
        minutesTomarrow = '0' + minutesTomarrow;
    }
    dateToServerTomarrow = dateToServerTomarrow.getFullYear() + "-" + monthTomarrow + "-" + dateTomarrow + " " + hoursTomarrow + ":" + minutesTomarrow + ':00';

    var travelReportTemplateHtml = '';
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/reports/travel/" + teamMember + "/" + dateToServerToday + "/" + dateToServerTomarrow,
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
                clevertap.event.push("Travel Report", {
//                    "UserId": loginUserIdcookie,
//                    "User Name": loginUserNamecookie,
//                    "AccountId": accountIdcookie,
                    "Source": "Web",
                    "Account name": accountNamecookie,
                    "UserRolecookie": UserRolecookie,
                });
                myTable = $("#sample_5").DataTable();
                if (myTable != null)
                    myTable.destroy();
                $('.cls_attendance').html('');
                travelReportTemplateHtml += '<table class="table table-striped table-bordered table-hover" id="sample_5">';
                travelReportTemplateHtml += '<thead>';
                travelReportTemplateHtml += '<tr>';
                travelReportTemplateHtml += '<th>';
                travelReportTemplateHtml += 'Time';
                travelReportTemplateHtml += '</th>';
                travelReportTemplateHtml += '<th>';
                travelReportTemplateHtml += 'Location';
                travelReportTemplateHtml += '</th>';
                travelReportTemplateHtml += '<th>';
                travelReportTemplateHtml += 'Latitude';
                travelReportTemplateHtml += '</th>';
                travelReportTemplateHtml += '<th>';
                travelReportTemplateHtml += 'Longitude';
                travelReportTemplateHtml += '</th>';
                travelReportTemplateHtml += '<th>';
                travelReportTemplateHtml += 'Distance (in kms)';
                travelReportTemplateHtml += '</th>';
                // added by jyoti
                travelReportTemplateHtml += '<th>';
                travelReportTemplateHtml += 'Battery percentage';
                travelReportTemplateHtml += '</th>';
                travelReportTemplateHtml += '<th>';
                travelReportTemplateHtml += 'GPS (Y/N)';
                travelReportTemplateHtml += '</th>';
                travelReportTemplateHtml += '<th>';
                travelReportTemplateHtml += 'Connectivity';
                travelReportTemplateHtml += '</th>';
                travelReportTemplateHtml += '<th>';
                travelReportTemplateHtml += 'App version';
                travelReportTemplateHtml += '</th>';
                travelReportTemplateHtml += '<th>';
                travelReportTemplateHtml += 'Mobile OS version';
                travelReportTemplateHtml += '</th>';
                travelReportTemplateHtml += '<th>';
                travelReportTemplateHtml += 'Device name';
                travelReportTemplateHtml += '</th>';
                travelReportTemplateHtml += '<th>';
                travelReportTemplateHtml += 'Mock Location (Y/N)';
                travelReportTemplateHtml += '</th>';
                // ended by jyoti
                travelReportTemplateHtml += '</tr>';
                travelReportTemplateHtml += '</thead>';
                travelReportTemplateHtml += '<tbody>';
                var travelData = data.result;
                var sourceValue = 0;
                var totalDistance = 0;
                var time = "";
                var j = 0;
                for (var i = 0; i < travelData.length; i++) {
                    // added by jyoti, purpose - display only those reports which has isShowIntoReports = true/1
                    var latitudeForAllIsShowReports = travelData[i].latitude;
                    var longitudeForAllIsShowReports = travelData[i].langitude;
                    if (latitudeForAllIsShowReports !== 0 && longitudeForAllIsShowReports !== 0) {
                        var distanceForAllIsShowReports = travelData[i].travelDistance;
                        totalDistance = totalDistance + distanceForAllIsShowReports;
                    }
                    var isShowIntoReports = travelData[i].isShowIntoReports;
                    
                    if (isShowIntoReports == 1) {
                        console.log("isShowIntoReports >> for ith : " + i + " >> " + isShowIntoReports);
                        // ended by jyoti
                        var checkForDateChange = false;
                        var location = travelData[i].location;
                        sourceValue = travelData[i].sourceValue;
                        if (location.trim() == "" || location.trim() == "Unknown location") {
                            location = "Address could not be resolved";
                        }
                        var latitude = travelData[i].latitude;
                        var longitude = travelData[i].langitude;
                        if (latitude !== 0 && longitude !== 0) {
                            var distance = travelData[i].travelDistance;
//                    totalDistance = totalDistance + distance; // should calculate all distance of isShowIntoReports 0 and 1, commented by jyoti
                            var createdOn = travelData[i].createdOn;
                            var timeZoneOffSet = createdOn.timezoneOffset * 60 * 1000;
                            var jsDate = new Date(createdOn.time - timeZoneOffSet);

                            // added by jyoti
                            var battery_Percentage = travelData[i].battery_Percentage;
                            var isGPS = travelData[i].isGPS;
                            var network_type = travelData[i].network_type;
                            if(network_type == '-'){
                                network_type = 'Offline';
                            }
                            var app_version = travelData[i].app_version;
                            var oS_Version = travelData[i].oS_Version;
                            var device_Name = travelData[i].device_Name;
                            var isMockLocation = travelData[i].isMockLocation;
                            // ended by jyoti

                            //Added By jotiba for to show the seconds in reports on 25-10-2016
                            time = jsDate.toTimeString().split(' ')[0];
                            //time=tConv24(time);
//                    //Added by siddhesh for enhanchment of travel report. 
//                    if(perviousTravelLogDistance==0){
//                        if(distance==0){
//                          checkForDateChange=true;
//                        }else{
//                           
//                            time=perviousTravelLogTime+" - "+time;
//                        }
//                    }
//                    //End of siddhesh.
                            //if (distance !== 0) {
                            if (sourceValue == 1 || sourceValue == 2 || sourceValue == 6) {
                                travelReportTemplateHtml += '<tr class="punchInOut">';
                            } else if (sourceValue == 4 || sourceValue == 5) {
                                travelReportTemplateHtml += '<tr class="attTimeOut">';
                            } else {
                                travelReportTemplateHtml += '<tr>';
                            }
                            travelReportTemplateHtml += '<td>';
                            travelReportTemplateHtml += time;
                            travelReportTemplateHtml += '</td>';
                            travelReportTemplateHtml += '<td>';
                            travelReportTemplateHtml += location;
                            travelReportTemplateHtml += '</td>';
                            travelReportTemplateHtml += '<td>';
                            travelReportTemplateHtml += latitude;
                            travelReportTemplateHtml += '</td>';
                            travelReportTemplateHtml += '<td>';
                            travelReportTemplateHtml += longitude;
                            travelReportTemplateHtml += '</td>';
                            travelReportTemplateHtml += '<td>';
                            travelReportTemplateHtml += distance;
                            travelReportTemplateHtml += '</td>';
                            // added by jyoti
                            travelReportTemplateHtml += '<td>';
                            travelReportTemplateHtml += battery_Percentage;
                            travelReportTemplateHtml += '</td>';
                            travelReportTemplateHtml += '<td>';
                            travelReportTemplateHtml += isGPS;
                            travelReportTemplateHtml += '</td>';
                            travelReportTemplateHtml += '<td>';
                            travelReportTemplateHtml += network_type;
                            travelReportTemplateHtml += '</td>';
                            travelReportTemplateHtml += '<td>';
                            travelReportTemplateHtml += app_version;
                            travelReportTemplateHtml += '</td>';
                            travelReportTemplateHtml += '<td>';
                            travelReportTemplateHtml += oS_Version;
                            travelReportTemplateHtml += '</td>';
                            travelReportTemplateHtml += '<td>';
                            travelReportTemplateHtml += device_Name;
                            travelReportTemplateHtml += '</td>';
                            travelReportTemplateHtml += '<td>';
                            travelReportTemplateHtml += isMockLocation;
                            travelReportTemplateHtml += '</td>';
                            //ended by jyoti
                            travelReportTemplateHtml += '</tr>';
                            exportTravelReport[j] = [{
                                    "time": time,
                                    "location": location,
                                    "latitude": latitude,
                                    "langitude": longitude,
                                    "distance": distance,
                                    "battery_Percentage":battery_Percentage,
                                    "isGPS":isGPS,
                                    "network_type":network_type,
                                    "app_version":app_version,
                                    "oS_Version":oS_Version,
                                    "device_Name":device_Name,
                                    "isMockLocation":isMockLocation

                                }];         //For CSV exception .
                            //}
//                perviousTravelLogDistance=distance;
//                if(!checkForDateChange){
//                perviousTravelLogTime=time;
                            j++
                        }
                        // added by jyoti
                    }
                    // ended by jyoti
                }
                /* if (totalDistance !== 0) {
                 travelReportTemplateHtml += '<tr>';
                 travelReportTemplateHtml += '<th colspan="5" style="text-align:right";>Total Distance:  <span id="id_totalCustRecords"> ' +  Math.round(totalDistance*10000)/10000  + ' kms</span></th>';
                 travelReportTemplateHtml += '</tr>';
                 }*/
                travelReportTemplateHtml += '</tbody>';
                travelReportTemplateHtml += '</table>';
                $('.cls_attendance').html(travelReportTemplateHtml);
                $('#sample_5').dataTable({
                    "aaSorting": [],
                    "sPaginationType": "bootstrap_full_number",
                    //  "scrollY":"500px",
                    //"scrollCollapse": true,
                    // "paging": true,
                    "aLengthMenu": [
                        [-1, 5, 10, 15, 20, 50, 100],
                        ["All", 5, 10, 15, 20, 50, 100] // change per page values here
                    ],
                    // set the initial value
                    "iDisplayLength": -1,
                    "bFilter": false,
                    "aoColumnDefs": [
                        {"bSortable": false,
                            "aTargets": [0, 1, 2, 3, 4]
                        }
                    ],
                    "drawCallback": function (settings) {
                        $('#sample_5').find('tr.punchInOut >td').css({"background-color": "#B4E9FF"});
                        $('#sample_5').find('tr.attTimeOut >td').css({"background-color": "#adadad"});
                        var api = this.api();
                        var rows = api.rows({page: 'current'}).nodes();
                        var records_length = exportTravelReport.length - 1;
                        //var last=null;
                        //api.column(2, {page:'current'} ).data().each( function ( group, i ) {
                        //if ( last !== group ) {
                        $(rows).eq(records_length).after(
                                '<tr class="group"><th colspan="5" style="text-align:right">Total Distance:  <span id="id_totalCustRecords"> ' + Math.round(totalDistance * 10000) / 10000 + ' kms</span></th></tr>'
                                );

                        //last = group;
                        //}
                        // } );
                    }
                });

                jQuery('#sample_5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
                jQuery('#sample_5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
                jQuery('#sample_5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown

                $("#sample_5_wrapper > .row:eq(1)").hide();

                //Added by Awaneesh
                // prepare CSV data
                var csvData = new Array();
                //                csvData.push('"Date","Punch-in","Punch-in Location","Punch-in Location Not Found Reason","Punch-out","Punch-out Location","Punch-out Location Not Found Reason","Worked For"');
//                csvData.push('"Time","Location","Latitude","Longitude","Distance (in kms)"'); // commented by jyoti
                csvData.push('"Time","Location","Latitude","Longitude","Distance (in kms)", "Battery percentage", "GPS", "Connectivity", "App version", "Mobile OS version", "Device name", "Mock Location" '); // added by jyoti
                var location = "";
                for (var i = 0; i < exportTravelReport.length; i++) {
                    //for (var j = 0; j < 1; j++) {
                    //location = exportTravelReport[i][j].location.toString().trim();
                    //location = location.replace("\r\n", " ");
                    //location = location.replace("\n", " ");
//                    csvData.push('"' + exportTravelReport[i][0].time + '","' + exportTravelReport[i][0].location + '","' + exportTravelReport[i][0].latitude + '","' + exportTravelReport[i][0].langitude + '","' + exportTravelReport[i][0].distance + '"'); // commented by jyoti
                   csvData.push('"' + exportTravelReport[i][0].time + '","' + exportTravelReport[i][0].location + '","' + exportTravelReport[i][0].latitude + '","' + exportTravelReport[i][0].langitude + '","' + exportTravelReport[i][0].distance + '","' + exportTravelReport[i][0].battery_Percentage + '","' + exportTravelReport[i][0].isGPS + '","' + exportTravelReport[i][0].network_type + '","' + exportTravelReport[i][0].app_version + '","' + exportTravelReport[i][0].oS_Version + '","' + exportTravelReport[i][0].device_Name + '"' + '","' + exportTravelReport[i][0].isMockLocation + '"'); // added by jyoti
                    // }
                }
                csvData.push('"","","","Total Distance","' + Math.round(totalDistance * 10000) / 10000 + ' kms"');

                // download stuff
                if (csvData.length > 2) {
                        if(emp_code.length==0){  // added by manohar
                            var fileName = selectedUserName + "_Travel.csv"; //teamMember + "_Travel.csv";
                        }
                        else{
                                var fileName = selectedUserName + "_Travel_"+emp_code+".csv"; //teamMember + "_Travel.csv";
                        }
                    var buffer = csvData.join("\n");
                    var blob;
                    try {
                        blob = new Blob([buffer], {
                            "type": "text/csv;charset=utf8;"
                        });
                    } catch (e) {
                        window.BlobBuilder = window.BlobBuilder ||
                                window.WebKitBlobBuilder ||
                                window.MozBlobBuilder ||
                                window.MSBlobBuilder;

                        if (e.name == 'TypeError' && window.BlobBuilder) {
                            var bb = new BlobBuilder();
                            bb.append(buffer);
                            blob = bb.getBlob("text/csv;charset=utf8;");
                        }
                        else if (e.name == "InvalidStateError") {
                            // InvalidStateError (tested on FF13 WinXP)
                            blob = new Blob([buffer], {
                                type: "text/csv;charset=utf8;"
                            });
                        }
                    }
                    var link = document.createElement("a");
                    var myURL = window.URL || window.webkitURL;
                    if (link.download !== undefined) { // feature detection
                        // Browsers that support HTML5 download attribute
                        //                        var myURL = window.URL || window.webkitURL;
                        link.setAttribute("href", myURL.createObjectURL(blob));
                        link.setAttribute("download", fileName);
                    }
                    else if (navigator.msSaveBlob) { // IE 10+
                        navigator.msSaveBlob(blob, fileName);
                    }
                    else {
                        link.setAttribute("href", myURL.createObjectURL(blob));
                        link.setAttribute("name", fileName);
                    }
                    $('#id_exportAttendance').html('');
                    link.innerHTML = '<button type="button" class="btn btn-info fr" style="margin-right:-15px;"><i class=" fa fa-file-text"></i> Save as CSV</button>';
                    document.getElementById("id_exportAttendance").appendChild(link);
                }
                //End By Awaneesh
                $('#pleaseWaitDialog').modal('hide');

            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
            }
        }
    });
}

function formReports(id)
{
    // added by vaibhav
     var imagemap = new Object(); 
    var imageCellValue ="";
    var allimagemap = new Object();
    var allimageCellValue ="";
    // ended by vaibhav
       $("#downloadimagesaszip_btn").attr("onclick", "downloadzipImagesUsers(" + id + ")");
    $("#id_exportAttendance").html('<button type="button" class="btn btn-info fr" style="margin-right:-15px;"><i class=" fa fa-file-text" id="id_exportAttendanceChild"></i> Save as CSV</button> ');
    //$("#id_exportAttendance").append('<button id="downloadimagesaszip_btn" onclick="zipImagesAndCsv()" type="button" class="btn btn-info fr" style="margin-right:-18px;margin-left:20px;"><i class=" fa fa-file-text" ></i> Download Images as zip</button>');
    var teamMember = document.getElementById("id_memberName").value;
    var emp_code; // added by manohar
    if(teamMember!='All'){
        emp_code=users_emp_codes_map[teamMember];
    }
    if (teamMember == 'select') {
        fieldSenseTosterError("Please select any member", true);
        return false;
    }
    var startTime = document.getElementById("id_startDate").value;
    var endTime = document.getElementById("id_endDate").value;
    var startTimeSplit = startTime.split('/');
    var fromDate = convertLocalDateToServerDate(startTimeSplit[2], startTimeSplit[1] - 1, startTimeSplit[0], 0, 0);
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
    var ensdTimeSplit = endTime.split('/');
    var toDate = convertLocalDateToServerDate(ensdTimeSplit[2], ensdTimeSplit[1] - 1, ensdTimeSplit[0], 23, 59);
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
    var url = fieldSenseURL + "/CustomFormsReport/" + id + "/" + teamMember + "/" + fromDate + "/" + toDate;
    if (teamMember == "All")
        url = fieldSenseURL + "/CustomFormsReport/" + id + "/User/" + loginUserId + "/All/" + fromDate + "/" + toDate;
    //var expenseTemplate = '';
    $('#pleaseWaitDialog').modal('show');
     var imageKey ="";
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
        asyn: false,
        success: function (data) {
            if (data.errorCode == '0000') {
                var mainArray = [];
                var add1 = [];
                var tableData = data.result;
                if (teamMember == "All") {
                    imagezip = new JSZip();
                    for (i = 0; i < tableData.length; i++) {
                        var fields = tableData[i].filledData;
                        add1 = [];
                        var val;
                        var k = 0;
                        add1.push(tableData[i].submitTime.time);//added to sort based on date
                        add1.push(tableData[i].submittedby);
                        // added by vaibhav
                        //add1.push(convertToLocalTime(tableData[i].submitTime));
                        var displayTime = convertToLocalTime(tableData[i].submitTime);
                        var newDisplaytime = displayTime.replace(/[ ,]+/g, " ");
                        add1.push(newDisplaytime);
                        imageKey =newDisplaytime; 
                        // ended by vaibhav
                        for (j = 0; j < formHeaders.length; j++) {
                            if (k < fields.length) {
                                if (formHeaders[j].fieldId == fields[k].field_id_fk) {
                                    if (formHeaders[j].fieldType == "Checkboxes")
                                        val = fields[k].field_value.replace(/\n/g, ', ');
                                    else if (fields[k].fieldType == "Image"){
                                        val= '<a  data-toggle="modal" href="#customformimage" onclick="getCustomFormImage(\''+id+'\',\''+fields[k].field_id_fk+'\',\''+fields[k].form_fill_detail_id_fk+'\',\''+fields[k].field_value+'\')">'+fields[k].field_value+'</a>';
                                            // added by vaibhav to store link in csv
                                        if(fields[k].field_value != ''){
                                            allimageCellValue = formCustomFormImageLink(id,fields[k].field_id_fk,fields[k].form_fill_detail_id_fk);//,fields[k].field_value) //+ "\"" +";"+  "\""  +fields[k].field_value  +"\"" + ")"; 
                                            imageKey = imageKey + " " + fields[k].field_value;
                                            allimagemap[imageKey] = encodeURI(allimageCellValue);
                                            //vaibhav
                                          }
                                        }
                                    else
                                        val = fields[k].field_value;
                                    add1.push(val);
                                    k = k + 1;
                                } else {
                                    add1.push(""); 
                                }
                            } else {
                                add1.push("");
                            }

                        }

                        mainArray.push(add1);
                    }

                    //sorting based on date
                    mainArray.sort(function (a, b) {
                        if (a[0] > b[0])
                            return -1;
                        else if (a[0] < b[0])
                            return 1;
                        else
                            return 0;

                    });

                    //remove after sorting
                    for (var i = 0; i < mainArray.length; i++) {
                        mainArray[i] = mainArray[i].slice(1, mainArray[i].length);
                    }

                    //console.log(mainArray);
                    var myTable = $('#sample_i5').DataTable();
                    myTable.clear();
                    myTable.order([1, "desc"]);
                    myTable.column(0).visible(true);
                    myTable.rows.add(mainArray).draw();

                    //tooltip
                    myTable.$('td').mouseover(function () {
                        var sData = $(this).text();
                        $(this).attr('title', sData);
                    });

                    $("#sample_i5").find('td').css({'max-width': '300px', 'overflow': 'hidden', 'text-overflow': 'ellipsis'});

                    // prepare CSV data
                    var excelHeader = "";
                    var csvData = new Array();
                    var k = 0;
                    excelHeader += '"Submitted by",';
                    excelHeader += '"Submitted on",';
                    while (k < formHeaders.length - 1) {

                        excelHeader += '"' + formHeaders[k].labelName + '",';
                        k = k + 1;

                    }
                    excelHeader += '"' + formHeaders[k].labelName + '"';
                    csvData.push(excelHeader);
                    excelHeader = "";
                    var i = 0;
                    var fieldValue;
                    while (i < mainArray.length) {
                        //var j=0;
                        excelHeader += '"' + mainArray[i][0] + '",';
                        excelHeader += '"' + mainArray[i][1] + '",';
                        imageKey = mainArray[i][1]; // added by vaibhav
                        j = 2;
                        while (j <= formHeaders.length) {
                            fieldValue = mainArray[i][j];
                            if(fieldValue.indexOf("data-toggle=\"modal\"")!==-1){
                                fieldValue=fieldValue.substring(fieldValue.indexOf(">")+1,fieldValue.lastIndexOf("<"));	
                                    // added by vaibhav to store image link in csv
                                    if(fieldValue != ''){            
                                        imageKey = imageKey + " " + fieldValue;
                                        fieldValue = allimagemap[imageKey];
                                    }else{
                                        fieldValue ='';
                                    }
                                }
                                // ended by vaibhav
                                excelHeader += '"' + fieldValue + '",';
                                j = j + 1;
                            }
                            fieldValue = mainArray[i][j];
                            if(fieldValue.indexOf("data-toggle=\"modal\"")!==-1){
                            // added by vaibhav to store image link in csv
                                fieldValue=fieldValue.substring(fieldValue.indexOf(">")+1,fieldValue.lastIndexOf("<"));
                                if(fieldValue != ''){
                                    imageKey = imageKey + " " + fieldValue;
                                    fieldValue = allimagemap[imageKey];
                               }else{
                                    fieldValue = '';
                                }

                            } // ended by  vaibhav
                        excelHeader += '"' + fieldValue + '"';
                        csvData.push(excelHeader);
                        excelHeader = "";
                        i = i + 1;
                    }

                    /* for (var i = 0; i < mainArray.length; i++) {
                     for (var j = 0; j < formHeaders.length; j++) {
                     csvData.push('"' + exportExpense[i][j].expenseName + '","' + exportExpense[i][j].eCategory + '","' + exportExpense[i][j].ammount + '","' + exportExpense[i][j].dateTime + '","' + exportExpense[i][j].customer + '","' + exportExpense[i][j].activity + '","' + exportExpense[i][j].st + '","' + exportExpense[i][j].approveBy + '","' + exportExpense[i][j].approveOn + '","' + exportExpense[i][j].reason + '"');
                     }
                     }*/
                    // download stuff

                    if (csvData.length > 1) {
                        var fileName;
                        $(".leftnav_form").each(function () {
                            if ($(this).hasClass("active")) {
                                fileName = $(this).find(".membername").text();
                            }
                        });
                        var fileName = fileName + "_" + $("#id_memberName option:selected").text() + ".csv";
                        var buffer = csvData.join("\n");
                        var blob;
                        try {
                            blob = new Blob([buffer], {
                                "type": "text/csv;charset=utf8;"
                            });
                        } catch (e) {
                            window.BlobBuilder = window.BlobBuilder ||
                                    window.WebKitBlobBuilder ||
                                    window.MozBlobBuilder ||
                                    window.MSBlobBuilder;

                            if (e.name == 'TypeError' && window.BlobBuilder) {
                                var bb = new BlobBuilder();
                                bb.append(buffer);
                                blob = bb.getBlob("text/csv;charset=utf8;");
                            }
                            else if (e.name == "InvalidStateError") {
                                // InvalidStateError (tested on FF13 WinXP)
                                blob = new Blob([buffer], {
                                    type: "text/csv;charset=utf8;"
                                });
                            }
                        }
                        var link = document.createElement("a");
                        var myURL = window.URL || window.webkitURL;
                        if (link.download !== undefined) { // feature detection
                            // Browsers that support HTML5 download attribute
                            link.setAttribute("href", myURL.createObjectURL(blob));
                            link.setAttribute("download", fileName);
                        }
                        else if (navigator.msSaveBlob) { // IE 10+
                            navigator.msSaveBlob(blob, fileName);
                        }
                        else {
                            link.setAttribute("href", myURL.createObjectURL(blob));
                            link.setAttribute("name", fileName);
                        }
                        $('#id_exportAttendance').html('');
                        link.innerHTML = '<button type="button" class="btn btn-info fr" style="margin-right:-15px;"><i class=" fa fa-file-text"></i> Save as CSV</button> ';
                        document.getElementById("id_exportAttendance").appendChild(link);
                        //var dnloddiv = document.getElementById("downloadimagezipdiv");
                       // var newHTML ='"downloadimagesaszip_btn" onclick="zipImagesAndCsv()" type="button" class="btn btn-info fr" style="margin-right:-18px;margin-left:20px;"><i class=" fa fa-file-text" ></i> Download Images as zip</button>';
                        //dnloddiv.innerHtml = newHTML;
                    } else {
                        // Added by jyoti
                        var link = document.createElement("a");
                        $('#id_exportFormReport').html('');
                        link.innerHTML = '<button type="button" id="save_as_csv_btn" class="btn btn-info fr"  style="margin-right:-15px;"><i class=" fa fa-file-text"></i> Save as CSV</button>';
                        $('#id_exportAttendance').html(link);
                        document.getElementById("save_as_csv_btn").addEventListener("click", function () {
                            noDataAvailableOnZipAndCsv();
                        }, false);
                        
                        $("#downloadimagesaszip_btn").attr("onclick", "noDataAvailableOnZipAndCsv()");
                        // ended by jyoti
                    }
                    //prepareImageCSVFileZipAll(mainArray,allimagemap);
                } else {
                    imagezip = new JSZip();
                    for (i = 0; i < tableData.length; i++) {
                        var fields = tableData[i].filledData;
                        add1 = [];
                        var val;
                        var k = 0;
                        add1.push("");
                        // added by vaibhav
                        //add1.push(convertToLocalTime(tableData[i].submitTime));
                        var displayTime = convertToLocalTime(tableData[i].submitTime);
                        var newDisplaytime = displayTime.replace(/[ ,]+/g, " ");
                        add1.push(newDisplaytime);
                        // ended by vaibhav
                        imageKey =newDisplaytime; 
                        for (j = 0; j < formHeaders.length; j++) {
                            if (k < fields.length) {
                                if (formHeaders[j].fieldId == fields[k].field_id_fk) {
                                    if (formHeaders[j].fieldType == "Checkboxes")
                                        val = fields[k].field_value.replace(/\n/g, ', ');
                                    else if (fields[k].fieldType == "Image"){
                                        val= '<a  data-toggle="modal" href="#customformimage" onclick="getCustomFormImage(\''+id+'\',\''+fields[k].field_id_fk+'\',\''+fields[k].form_fill_detail_id_fk+'\',\''+fields[k].field_value+'\')">'+fields[k].field_value+'</a>';
                                        // added by vaibhav to store image link in csv
                                        if(fields[k].field_value != ''){
                                           imageCellValue = formCustomFormImageLink(id,fields[k].field_id_fk,fields[k].form_fill_detail_id_fk); //,fields[k].field_value) + "\"" +";"+  "\""  +fields[k].field_value  +"\"" + ")"; 
                                           imageKey = imageKey + " " + fields[k].field_value;
                                           imagemap[imageKey] = encodeURI(imageCellValue);
                                            //   ended by vaibhav
                                         }
                                        }
                                    else
//                                        if(fields[k].fieldType == "Location" && fields[k].field_value.includes('Lat:')){ // added by jyoti, Feature #30909
//                                            var latitude = fields[k].field_value.split(',')[0].split(':')[1].trim();
//                                            var longitude = fields[k].field_value.split(',')[1].split(':')[1].trim();
//                                            console.log(latitude);
//                                            console.log(longitude);
//                                            reverseGeoCoding(latitude, longitude, function (object) {
//                                                console.log('address : '+object);
//                                                val = object;
//                                                console.log("val after setting: " + val);
////                                                val = fields[k].field_value;
//                                                add1.push(val);
//                                                k = k + 1;                                                
//                                            });
//                                        } else {
                                            val = fields[k].field_value;
                                            add1.push(val);
                                            k = k + 1;
//                                        }
                                } else {
                                    add1.push("");
                                }
                            } else {
                                add1.push("");
                            }

                        }

                        mainArray.push(add1);
                    }

                    var myTable = $('#sample_i5').DataTable();
                    myTable.clear();
                    myTable.order([1, "desc"]);
                    myTable.rows.add(mainArray).draw();

                    myTable.column(0).visible(false);

                    //tooltip
                    myTable.$('td').mouseover(function () {
                        var sData = $(this).text();
                        $(this).attr('title', sData);
                    });

                    $("#sample_i5").find('td').css({'max-width': '300px', 'overflow': 'hidden', 'text-overflow': 'ellipsis'});

                    // prepare CSV data
                    var excelHeader = "";
                    var csvData = new Array();
                    var k = 0;
                    excelHeader += '"Submitted on",';
                    while (k < formHeaders.length - 1) {

                        excelHeader += '"' + formHeaders[k].labelName + '",';
                        k = k + 1;

                    }
                    excelHeader += '"' + formHeaders[k].labelName + '"';
                    csvData.push(excelHeader);
                    excelHeader = "";
                    var i = 0;
                    var fieldValue;
                    while (i < mainArray.length) {
                        imageKey = mainArray[i][1]; // added by vaibhav
                        excelHeader += '"' + mainArray[i][1] + '",';
                        var j = 2;
                        while (j <= formHeaders.length) {
                            fieldValue = mainArray[i][j];
                            if(fieldValue.indexOf("data-toggle=\"modal\"")!=-1){
                                fieldValue=fieldValue.substring(fieldValue.indexOf(">")+1,fieldValue.lastIndexOf("<"));
                                   // added by vaibhav to store image link in csv
                                if(fieldValue != ''){
                                     imageKey = imageKey + " " + fieldValue;
                                    fieldValue = imagemap[imageKey];
                                }else{
                                   fieldValue =''; 
                                }

                            } // ended by vai
                            excelHeader += '"' + fieldValue + '",';
                            j = j + 1;
                        }
                        fieldValue = mainArray[i][j];
                        if(fieldValue.indexOf("data-toggle=\"modal\"")!=-1){
                            // added by vaibhav
                            fieldValue=fieldValue.substring(fieldValue.indexOf(">")+1,fieldValue.lastIndexOf("<"));
                            if(fieldValue != ''){
                                 imageKey = imageKey + " " + fieldValue;
                                 fieldValue = imagemap[imageKey];
                            }else{
                               fieldValue =''; 
                            }
                       }
                       // ended by vaibhav
                        excelHeader += '"' + fieldValue + '"';

                        csvData.push(excelHeader);
                        excelHeader = "";
                        i = i + 1;
                    }

                    /* for (var i = 0; i < mainArray.length; i++) {
                     for (var j = 0; j < formHeaders.length; j++) {
                     csvData.push('"' + exportExpense[i][j].expenseName + '","' + exportExpense[i][j].eCategory + '","' + exportExpense[i][j].ammount + '","' + exportExpense[i][j].dateTime + '","' + exportExpense[i][j].customer + '","' + exportExpense[i][j].activity + '","' + exportExpense[i][j].st + '","' + exportExpense[i][j].approveBy + '","' + exportExpense[i][j].approveOn + '","' + exportExpense[i][j].reason + '"');
                     }
                     }*/
                    // download stuff

                    if (csvData.length > 1) {
                        var fileName;
                        $(".leftnav_form").each(function () {
                            if ($(this).hasClass("active")) {
                                fileName = $(this).find(".membername").text();
                            }
                        });
                            if(emp_code.length==0){ // added by manohar
                                    var fileName = fileName + "_" + $("#id_memberName option:selected").text() +".csv";
                                }
                            else
                            {
                                 var fileName = fileName + "_" + $("#id_memberName option:selected").text() +'_'+emp_code+ ".csv";
                            }
                        var buffer = csvData.join("\n");
                        var blob;
                        try {
                            blob = new Blob([buffer], {
                                "type": "text/csv;charset=utf8;"
                            });
                        } catch (e) {
                            window.BlobBuilder = window.BlobBuilder ||
                                    window.WebKitBlobBuilder ||
                                    window.MozBlobBuilder ||
                                    window.MSBlobBuilder;

                            if (e.name == 'TypeError' && window.BlobBuilder) {
                                var bb = new BlobBuilder();
                                bb.append(buffer);
                                blob = bb.getBlob("text/csv;charset=utf8;");
                            }
                            else if (e.name == "InvalidStateError") {
                                // InvalidStateError (tested on FF13 WinXP)
                                blob = new Blob([buffer], {
                                    type: "text/csv;charset=utf8;"
                                });
                            }
                        }
                        var link = document.createElement("a");
                        var myURL = window.URL || window.webkitURL;
                        if (link.download !== undefined) { // feature detection
                            // Browsers that support HTML5 download attribute
                            link.setAttribute("href", myURL.createObjectURL(blob));
                            link.setAttribute("download", fileName);
                        }
                        else if (navigator.msSaveBlob) { // IE 10+
                            navigator.msSaveBlob(blob, fileName);
                        }
                        else {
                            link.setAttribute("href", myURL.createObjectURL(blob));
                            link.setAttribute("name", fileName);
                        }
                        $('#id_exportAttendance').html('');
                        link.innerHTML = '<button type="button" class="btn btn-info fr" style="margin-right:-15px;"><i class=" fa fa-file-text"></i> Save as CSV</button>';
                        document.getElementById("id_exportAttendance").appendChild(link);
                        //var dnloddiv = document.getElementById("downloadimagezipdiv");
                        //var  newHTML ='<button id="downloadimagesaszip_btn" onclick="zipImagesAndCsv()" type="button" class="btn btn-info fr" style="margin-right:-18px;margin-left:20px;"><i class=" fa fa-file-text" ></i> Download Images as zip</button>';
                        //dnloddiv.innerHtml = newHTML;
                    } else {
                        // Added by jyoti
                        var link = document.createElement("a");
                        $('#id_exportFormReport').html('');
                        link.innerHTML = '<button type="button" id="save_as_csv_btn" class="btn btn-info fr"  style="margin-right:-15px;"><i class=" fa fa-file-text"></i> Save as CSV</button>';
                        $('#id_exportAttendance').html(link);
                        document.getElementById("save_as_csv_btn").addEventListener("click", function () {
                            noDataAvailableOnZipAndCsv();
                        }, false);
                        
                        $("#downloadimagesaszip_btn").attr("onclick", "noDataAvailableOnZipAndCsv()");
                        // ended by jyoti
                    }
                 //prepareImageandCsvforSingleUser(mainArray,imagemap);
                }
                App.fixContentHeight();
                App.initUniform();
                $('#pleaseWaitDialog').modal('hide');
            } else {
                fieldSenseTosterError(data.errorMessage, true);
                $('#pleaseWaitDialog').modal('hide');
            }
        }
    });

}
// added by vaibhav
function formCustomFormImageLink(formId,fieldId,filledOnId){

	
	templatehtml= customFormImageURLPath + 'account_' + accountId + '_form_' + formId + '_field_' + fieldId + '_filledOnId_' + filledOnId + '.png';
	
	return templatehtml;
}
// ended by vaibhav
function convertToLocalTime(submittedon) {
    var submittedon = submittedon;
    var timeZoneOffSet = submittedon.timezoneOffset * 60 * 1000;
    submittedon = new Date(submittedon.time - timeZoneOffSet);
    submittedon = convertServerDateToLocalTimezone(submittedon.getFullYear(), submittedon.getMonth(), submittedon.getDate(), submittedon.getHours(), submittedon.getMinutes(), timeZoneOffSet);
    submittedon = submittedon.getFullYear() + "-" + (submittedon.getMonth() + 1) + "-" + submittedon.getDate() + " " + submittedon.getHours() + ":" + submittedon.getMinutes() + ":00.0";
    submittedon = fieldSenseseForJSDate(submittedon);
    return submittedon;
}

function dateRange() {
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
    var firstDay = '01/' + m + '/' + y;
    var today = d + '/' + m + '/' + y;
    var dateRangeTemplate = '';
    dateRangeTemplate += '<div class = "form-group">';
    dateRangeTemplate += '<label class = "control-label col-md-4"> Date Range </label>';
    dateRangeTemplate += '<div class = "col-md-8">';
    dateRangeTemplate += '<div class = "input-group input-large date-picker input-daterange" data - date = "20/11/2012" data-date-format="dd/mm/yyyy">';
    dateRangeTemplate += '<input type = "text" class = "form-control" name = "from" id = "id_startDate" value="' + firstDay + '">';
    dateRangeTemplate += '<span class = "input-group-addon" >to</span>';
    dateRangeTemplate += '<input type = "text" class = "form-control" name = "to" id = "id_endDate" value="' + today + '">';
    dateRangeTemplate += '</div></div></div>';
    $('#dateRange').html(dateRangeTemplate);
    $('.date-picker').datepicker({
        rtl: App.isRTL(),
        autoclose: true
    });
}
dataTableForExpenseReport.count = 0;
function dataTableForExpenseReport() {
    var oTable = $('#sample_5').dataTable({
        "aaSorting": [],
        "aoColumnDefs": [
            {"bSortable": false,
                "aTargets": [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]

            }],
        "sPaginationType": "bootstrap_full_number",
        //  "scrollY":"500px",
        // "scrollCollapse": true,
        //  "paging": true,
        "aLengthMenu": [
            [-1, 5, 10, 15, 20, 50, 100],
            ["All", 5, 10, 15, 20, 50, 100] // change per page values here
        ],
        // set the initial value
        "iDisplayLength": 20,
        "bFilter": false,
        "footerCallback": function (row, data, start, end, display) {
            var api = this.api(), data;

//  Total for Amount Claimed
            // Remove the formatting to get integer data for summation
            var intVal = function (i) {
                return typeof i === 'string' ?
                        i.replace(/[\$,]/g, '') * 1 :
                        typeof i === 'number' ?
                        i : 0;
            };

            total = api
                    .column(10)
                    .data()
                    .reduce(function (a, b) {
                        return intVal(a) + intVal(b);
                    }, 0);


            pageTotal = api
                    .column(10, {page: 'current'})
                    .data()
                    .reduce(function (a, b) {
                        return intVal(a) + intVal(b);
                    }, 0);

// Update footer
            $(api.column(10).footer()).html(
                    '<i class="fa fa-inr fa-fw" aria-hidden="true"></i>' + pageTotal + ' (<i class="fa fa-inr fa-fw" aria-hidden="true"></i>' + total + ' Total)'
                    );

// Total for Disbursed Amount total

            var intVal1 = function (i) {
                return typeof i === 'string' ?
                        i.replace(/[\$,]/g, '') * 1 :
                        typeof i === 'number' ?
                        i : 0;
            };

// Total over all pages, for Disbursed Amount
            total1 = api
                    .column(11)
                    .data()
                    .reduce(function (a, b) {
                        return intVal1(a) + intVal1(b);
                    }, 0);

// Total over this page, for Disbursed Amount
            pageTotal1 = api
                    .column(11, {page: 'current'})
                    .data()
                    .reduce(function (a, b) {
                        return intVal1(a) + intVal1(b);
                    }, 0);

// Update footer
            $(api.column(11).footer()).html(
                    '<i class="fa fa-inr fa-fw" aria-hidden="true"></i>' + pageTotal1 + ' (<i class="fa fa-inr fa-fw" aria-hidden="true"></i>' + total1 + ' Total)'
                    );

        }
    });

    jQuery('#sample_5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
    jQuery('#sample_5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
    jQuery('#sample_5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown
    $(document).ready(function () {
        $('#sample_5').dataTable().rowGrouping({
            iGroupingColumnIndex: 3,
            sGroupBy: "date",
            sGroupingColumnSortDirection: "desc",
            bHideGroupingColumn: true
        });
    });

    $('span.rowCount-grid').remove();
    $('.dataTables_wrapper').find('[id|=group-id]').each(function () {
        //        var rowCount = $(this).nextUntil('[id|=group-id]').length;
        var ammount = totalAmt[dataTableForExpenseReport.count].amt;
        var approvedAmt = totalAmt[dataTableForExpenseReport.count].approvedAmt;
        var rejectedAmt = totalAmt[dataTableForExpenseReport.count].rejectedAmt;
        var disbursedAmount = totalAmt[dataTableForExpenseReport.count].disbursedamount;
        //        $(this).find('td').append($('<span style="float:right; padding-right: 915px; " />', {'class': 'rowCount-grid'}).append($('<span />', {'text': "Expenses Claimed:" + ammount})));
        $(this).find('td').append($('<span style="padding-left: 156px;"/>', {
            'class': 'rowCount-grid'
        }).append($('<span />', {
            'text': " Expenses Claimed: " + ammount + " |Approved: " + approvedAmt + " |Rejected: " + rejectedAmt + " |Disbursed Amount: " + disbursedAmount
        })));
        dataTableForExpenseReport.count++;
    });
    dataTableForExpenseReport.count = 0;
    // totalAmt=[];
    firstdraw = false;
}
//Fri May  8 06:42:46 UTC 2015
// at 12:12 

function dataTableForExpenseReport1() {
    var oTable1 = $('#sample_5').dataTable({
        "aaSorting": [],
        "aoColumnDefs": [
            {"bSortable": false,
                "aTargets": [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]

            }],
        "sPaginationType": "bootstrap_full_number",
        //  "scrollY":"500px",
        // "scrollCollapse": true,
        //  "paging": true,
        "aLengthMenu": [
            [-1, 5, 10, 15, 20, 50, 100],
            ["All", 5, 10, 15, 20, 50, 100] // change per page values here
        ],
        // set the initial value
        "iDisplayLength": 20,
        "bFilter": false

    });

    jQuery('#sample_5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
    jQuery('#sample_5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
    jQuery('#sample_5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown
    $(document).ready(function () {
        $('#sample_5').dataTable().rowGrouping({
            iGroupingColumnIndex: 3,
            sGroupBy: "date",
            sGroupingColumnSortDirection: "desc",
            bHideGroupingColumn: true
        });
    });

    $('span.rowCount-grid').remove();
    $('.dataTables_wrapper').find('[id|=group-id]').each(function () {
        //        var rowCount = $(this).nextUntil('[id|=group-id]').length;
        var ammount = totalAmt[dataTableForExpenseReport.count].amt;
        var approvedAmt = totalAmt[dataTableForExpenseReport.count].approvedAmt;
        var rejectedAmt = totalAmt[dataTableForExpenseReport.count].rejectedAmt;
        var disbursedAmount = totalAmt[dataTableForExpenseReport.count].disbursedamount;
        //        $(this).find('td').append($('<span style="float:right; padding-right: 915px; " />', {'class': 'rowCount-grid'}).append($('<span />', {'text': "Expenses Claimed:" + ammount})));
        $(this).find('td').append($('<span style="padding-left: 156px;"/>', {
            'class': 'rowCount-grid'
        }).append($('<span />', {
            'text': " Expenses Claimed: " + ammount + " |Approved: " + approvedAmt + " |Rejected: " + rejectedAmt + " |Disbursed Amount: " + disbursedAmount
        })));
        dataTableForExpenseReport.count++;
    });
    dataTableForExpenseReport.count = 0;
    // totalAmt=[];
    firstdraw = false;
}


function getExpenseStatus(id) {

    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/expense/expense_audit/" + id,
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
                var templatehtml = "";
                var expenseData1 = data.result;
                var expenseData = [];
                for (i = 0; i < expenseData1.length; i++) {
                    expenseData.push({"status": expenseData1[i].status, "expense_data": expenseData1[i]});
                }
                expenseData.sort(function (a, b) {
                    return parseInt(b.status) - parseInt(a.status);
                });
                if (expenseData.length != 0) {
                    templatehtml += '<div class="form-group">';
                    templatehtml += '<label class="col-md-4 control-label">Expense Name</label>';
                    templatehtml += '<div class="col-md-8">';
                    templatehtml += '<input type="name" class="form-control" Value="' + expenseData[0].expense_data.expenseName + '" tabindex="1" readonly>';
                    templatehtml += '</div>';
                    templatehtml += '</div>';
                    templatehtml += '<div class="form-group">';
                    templatehtml += '<label class="col-md-4 control-label">Amount Claimed <span>&#8377;</span></label>';
                    templatehtml += '<div class="col-md-8">';
                    templatehtml += '<input type="text" class="form-control" value="' + expenseData[0].expense_data.amountSpent + '" tabindex="5" readonly>';
                    templatehtml += '</div>';
                    templatehtml += '</div>';
                    templatehtml += '<div class="form-group">';
                    templatehtml += '<label class="col-md-4 control-label">Current Status</label>';
                    templatehtml += '<div class="col-md-8">';
                    if (expenseData[0].status == 0) {
                        templatehtml += '<input type="text" class="form-control" value="Pending Approval by Reporting Head" tabindex="5" readonly>';
                    }
                    else if (expenseData[0].status == 1) {
                        templatehtml += '<input type="text" class="form-control" value="Pending Approval by Accounts" tabindex="5" readonly>';
                    }
                    else if (expenseData[0].status == 2) {
                        templatehtml += '<input type="text" class="form-control" value="Rejected by Reporting Head" tabindex="5" readonly>';
                    }
                    else if (expenseData[0].status == 3) {
                        templatehtml += '<input type="text" class="form-control" value="Approved" tabindex="5" readonly>';
                    }
                    else if (expenseData[0].status == 4) {
                        templatehtml += '<input type="text" class="form-control" value="Rejected by Accounts" tabindex="5" readonly>';
                    }
                    else if (expenseData[0].status == 5) {
                        templatehtml += '<input type="text" class="form-control" value="Disbursed" tabindex="5" readonly>';
                    }
                    templatehtml += '</div>';
                    templatehtml += '</div>';
                    if (expenseData[0].status == 5) {
                        templatehtml += '<div class="form-group">';
                        templatehtml += '<label class="col-md-4 control-label">Disbursed Amount</label>';
                        templatehtml += '<div class="col-md-8">';
                        templatehtml += '<input type="name" class="form-control" Value="' + expenseData[0].expense_data.disburse_amount + '" tabindex="1" readonly>';
                        templatehtml += '</div>';
                        templatehtml += '</div>';
                    }

                    templatehtml += '<div class="form-group">';
                    templatehtml += '<label class="col-md-4 control-label">Event History:</label>';
                    templatehtml += '<div class="col-md-8">';
                    templatehtml += '</div>';
                    templatehtml += '</div>';
                    templatehtml += '<div>';
                    templatehtml += '<table class="table table-striped table-bordered table-hover"  id="sample_5">';
                    templatehtml += '<thead>';
                    templatehtml += '<tr>';
                    templatehtml += '<th>';
                    templatehtml += 'Status';
                    templatehtml += '</th>';
                    templatehtml += '<th>';
                    templatehtml += 'Approved / Rejected By';
                    templatehtml += '</th>';
                    templatehtml += '<th>';
                    templatehtml += 'Approved / Rejected On';
                    templatehtml += '</th>';
                    templatehtml += '<th>';
                    templatehtml += 'Rejected Reason';
                    templatehtml += '</th>';
                    templatehtml += '</tr>';
                    templatehtml += '</thead>';
                    templatehtml += '<tbody>';
                    for (i = 0; i < expenseData.length; i++) {
                        var status1 = expenseData[i].expense_data.status;
                        var app_reg_by = expenseData[i].expense_data.approvedOrRjectedBy.fullname;
                        var rej_reason = expenseData[i].expense_data.rejectedReson;
                        var serverDate1 = expenseData[i].expense_data.approvedOrRejectedOn;
                        var localDate1 = convertServerDateToLocalDate(serverDate1.year + 1900, serverDate1.month, serverDate1.date, serverDate1.hours, serverDate1.minutes)
                        var approveOn1 = fieldSenseDateTimeForExpenseJsDate(localDate1);
                        if (approveOn1 == "11 Nov 1111 04:41 pm" || status1 == 0) {
                            approveOn1 = '';
                        }
                        if (status1 == 0) {
                            app_reg_by = '';
                        }
                        templatehtml += '<tr>';
                        if (status1 == 0) {
                            templatehtml += '<td>Pending Approval by Reporting Head</td>';
                        } else if (status1 == 1) {
                            templatehtml += '<td>Approved by Reporting Head</td>';
                        } else if (status1 == 2) {
                            templatehtml += '<td>Rejected by Reporting Head</td>';
                        } else if (status1 == 3) {
                            templatehtml += '<td>Approved</td>';
                        } else if (status1 == 4) {
                            templatehtml += '<td>Rejected by Accounts</td>';
                        } else if (status1 == 5) {
                            templatehtml += '<td>Disbursed</td>';
                        }
                        templatehtml += '<td>' + app_reg_by + '</td>';
                        templatehtml += '<td>' + approveOn1 + '</td>';
                        templatehtml += '<td>' + rej_reason + '</td>';
                        templatehtml += '</tr>';

                    }
                    templatehtml += '</tbody>';
                    templatehtml += '</table>';
                    templatehtml += '</div>';
                }
                $("#expense_details").html(templatehtml);
            } else {
                fieldSenseTosterError(data.errorMessage, true);
            }
        },
        error: function (xhr, ajaxOptions, thrownError)
        {
            alert("in error:" + thrownError);
        }
    });


}

function getTimeOutDetails(userId, TimeOutDate) {

    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/attendance/timeout/" + userId + "/" + TimeOutDate,
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
                var templatehtml = "";
                var timeOutData = data.result;
                if (timeOutData.length != 0) {
                    templatehtml += '<div>';
                    templatehtml += '<table class="table table-striped table-bordered table-hover"  id="sample_5">';
                    templatehtml += '<thead>';
                    templatehtml += '<tr>';
                    /*templatehtml += '<th>';
                     templatehtml += 'Date';
                     templatehtml += '</th>';*/
                    templatehtml += '<th>';
                    templatehtml += 'Start Time';
                    templatehtml += '</th>';
                    templatehtml += '<th>';
                    templatehtml += 'Stop Time';
                    templatehtml += '</th>';
                    templatehtml += '<th>';
                    templatehtml += 'Interval Time';
                    templatehtml += '</th>';
                    templatehtml += '</tr>';
                    templatehtml += '</thead>';
                    templatehtml += '<tbody>';
                    for (i = 0; i < timeOutData.length; i++) {
                        var timeOutDate = timeOutData[i].strTimeoutDate;
                        var serverDate1 = timeOutData[i].startTimeout;
                        var localDate1 = convertServerDateToLocalDate(serverDate1.year + 1900, serverDate1.month, serverDate1.date, serverDate1.hours, serverDate1.minutes)
                        var startTime = fieldSenseDateTimeForExpenseJsDate(localDate1);
                        var serverDate2 = timeOutData[i].stopTimeout;
                        var localDate2 = convertServerDateToLocalDate(serverDate2.year + 1900, serverDate2.month, serverDate2.date, serverDate2.hours, serverDate2.minutes)
                        var stopTime = fieldSenseDateTimeForExpenseJsDate(localDate2);
                        if (stopTime.indexOf("11 Nov 1111") != -1 || stopTime.indexOf("18 Nov 1111") != -1) {
                            stopTime = "";
                        }
                        var intervalTime = timeOutData[i].strIntervalTime;
                        templatehtml += '<tr>';
                        //templatehtml+='<td>'+timeOutDate+'</td>';
                        templatehtml += '<td>' + startTime + '</td>';
                        templatehtml += '<td>' + stopTime + '</td>';
                        templatehtml += '<td>' + intervalTime + '</td>';
                        templatehtml += '</tr>';

                    }
                    templatehtml += '</tbody>';
                    templatehtml += '</table>';
                    templatehtml += '</div>';
                }
                $("#timeout_details").html(templatehtml);
            } else {
                fieldSenseTosterError(data.errorMessage, true);
            }
        },
        error: function (xhr, ajaxOptions, thrownError)
        {
            alert("in error:" + thrownError);
        }
    });
}


function getdifferentamount() {
    var count = 0;
    $('span.rowCount-grid').remove();
    $('.dataTables_wrapper').find('[id|=group-id]').each(function () {
        //        var rowCount = $(this).nextUntil('[id|=group-id]').length;
        var ammount = totalAmt1[count].amt;
        var approvedAmt = totalAmt1[count].approvedAmt;
        var rejectedAmt = totalAmt1[count].rejectedAmt;
        var disbursedAmount = totalAmt1[count].disbursedamount;
        //        $(this).find('td').append($('<span style="float:right; padding-right: 915px; " />', {'class': 'rowCount-grid'}).append($('<span />', {'text': "Expenses Claimed:" + ammount})));
        $(this).find('td').append($('<span style="padding-left: 156px;"/>', {
            'class': 'rowCount-grid'
        }).append($('<span />', {
            'text': " Expenses Claimed: " + ammount + " |Approved: " + approvedAmt + " |Rejected: " + rejectedAmt + " |Disbursed Amount: " + disbursedAmount
        })));
        count++;
    });
    count = 0;
    totalAmt1 = [];
}


function getCustomFormImage(formId, fieldId, filledOnId, fieldValue) {

    var templatehtml = "";
    fieldValue1 = fieldValue;
    if (fieldValue1.length > 20) {
        fieldValue1 = fieldValue1.substring(0, 20) + "...";
    }
    templatehtml += '<div class="form-group">';
    templatehtml += '<label class="col-md-4 control-label" title="' + fieldValue + '">' + fieldValue1 + '</label>';
    templatehtml += '<div class="col-md-8">';
    templatehtml += '<p class="form-control-static"><img style="width:330px;height:330px" src="' + customFormImageURLPath + 'account_' + accountId + '_form_' + formId + '_field_' + fieldId + '_filledOnId_' + filledOnId + '.png?' + time + '" onerror="this.src=\'assets/img/noimage.jpg\';"/></p>';
    templatehtml += '</div>';
    templatehtml += '</div>';

    $("#custom_form_image").html(templatehtml);
}

//Awaneesh started working on monthly travel report

function getMonthlyTravleDiv() {

    $('.id_emp').html('');  // added by manohar
    var myDiv = document.getElementById("downloadimagezipdiv");
    myDiv.style = "display: none;";

    $(".leftnav_form").each(function () {
        $(this).removeClass("active");
    });
    $("#attendance_list").removeClass("active");
    $("#visit_list").removeClass("active");
    $("#expense_list").removeClass("active");
    $("#travel_list").removeClass("active");
    $("#monthly_travel_list").attr("class", "active");
    $("#memebrs_div").show();
    //$("#id_memberName option[value='All']").remove(); // for value all

    jQuery("#id_memberName option").filter(function () {
        return $.trim($(this).text()) == 'All'
    }).remove();

    $("#id_memberName option[value='select']").remove();
    $("#id_memberName option[value='0']").remove();
    $('#id_memberName').prepend("<option value='select'>" + "Select Member" + "</option>" + "<option value='" + 0 + "'>" + "All" + "</option>");// Add All

    $("#id_memberName").val("select");
    $("#reports_show").attr("onclick", "showMonthlyReport()");
    $("#reports_show").attr("style", "margin-left: 47px;");
    $('#dateRange').attr("class", "col-md-5");
    dateRange();
    $("#id_exportAttendance").html('<button type="button" class="btn btn-info fr" style="margin-right:-15px;" onclick="exportMonthlyReport()"><i class=" fa fa-file-text" id="id_exportAttendanceChild"></i> Save as CSV</button> ');
    var tabletemplate = '<table class="table table-striped table-bordered table-hover" id="sample_5">';
    tabletemplate += '<thead>';
    tabletemplate += '<tr>';
    tabletemplate += '<th style="width:45px;"></th>';
    tabletemplate += '<th>User</th>';
    tabletemplate += '<th>Total Distance Travelled (in kms)</th>';
    tabletemplate += '<th>Total Visits</th>';
    tabletemplate += '</tr></thead>';
    tabletemplate += '<tbody></tbody></table>';
    $(".cls_attendance").html(tabletemplate);
    $('#sample_5').dataTable({
        "aaSorting": [],
        "aLengthMenu": [
            [-1, 5, 10, 15, 20, 50, 100],
            ["All", 5, 10, 15, 20, 50, 100] // change per page values here
        ],
        // set the initial value
        "iDisplayLength": 20,
        //   "scrollY":"500px",
        //"scrollCollapse": true,
        // "paging": true,
        "bFilter": false,
        "bSort": false
    });

    jQuery('#sample_5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
    jQuery('#sample_5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
    jQuery('#sample_5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown
    App.initUniform();
    App.fixContentHeight();

}

function showMonthlyReport() {
    clevertap.event.push("Monthly Travel", {
//                    "UserId": loginUserIdcookie,
//                    "User Name": loginUserNamecookie,
//                    "AccountId": accountIdcookie,
        "Source": "Web",
        "Account name": accountNamecookie,
        "UserRolecookie": UserRolecookie,
    });
    var userId = document.getElementById("id_memberName").value;
    if (userId == 'select') {
        fieldSenseTosterError("Please select any member", true);
        return false;
    }
    var startTime = document.getElementById("id_startDate").value;
    var endTime = document.getElementById("id_endDate").value;
    var startTimeSplit = startTime.split('/');
    var fromDate = convertLocalDateToServerDate(startTimeSplit[2], startTimeSplit[1] - 1, startTimeSplit[0], 0, 0);
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
    var ensdTimeSplit = endTime.split('/');
    var toDate = convertLocalDateToServerDate(ensdTimeSplit[2], ensdTimeSplit[1] - 1, ensdTimeSplit[0], 23, 59);
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
    var tzoffset = getTimeZoneOffset();

    var start = 0;
    var slength = 20;
    myTable = $("#sample_5").DataTable();
    if (myTable != null)
        myTable.destroy();
    //$('#pleaseWaitDialog').modal('show');
    var inboxtable = $('#sample_5').dataTable({
        "bServerSide": true,
        "bDestroy": true,
        "ajax": {
            "url": fieldSenseURL + "/reports/monthlyTravel/subordinate/" + userId + "/" + tzoffset + "/" + fromDate + "/" + toDate,
            "type": "GET",
            headers: {
                "userToken": userToken
            },
            contentType: "application/json; charset=utf-8",
            crossDomain: false,
            cache: false,
            dataType: 'json',
            asyn: true,
            error: function (data) {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.responseJSON.errorMessage, true);
                //logout();
            }
        },
        "iDisplayStart": start,
        // set the initial value
        "iDisplayLength": slength, // It will be by default page width set by user
        "sPaginationType": "bootstrap_full_number",
        //     "scrollY":"500px",
        // "scrollCollapse": true,
        //  "paging": true,
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
                "aTargets": [0, 1, 2, 3]

            }
        ],
        "aoColumns": [
            {"mData": null,
                "sClass": "details-control",
                //"sWidth":"45px",
                "mRender": function (data, type, full) {
                    return '';
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.fullName;
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.distance;
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.visits;
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

    $('#sample_5 tbody').off('click', 'td.details-control'); //Remove previous event listner 
    // Add New event listener for opening and closing details
    $('#sample_5 tbody').on('click', 'td.details-control', function () {
        var table = $("#sample_5").DataTable();
        var tr = $(this).closest('tr');
        var row = table.row(tr);

        if (row.child.isShown()) {
            // This row is already open - close it
            row.child.hide();
            tr.removeClass('shown');
        }
        else {
            // Open this row
            row.child(format(row.data())).show();
            tr.addClass('shown');
        }
    });
}

function format(d) {

    if (d.distance == 0 && d.visits == 0) {
        return 'No Data.'
    }
    var dayWiseData = d.dayWiseReport;
    var innerDetails = "<table class='table table-striped table-bordered table-hover' style='width:500px; margin-left:35px;'> <thead><tr><th>Date</th><th>Distance Travelled (in Kms)</th><th>No. of Visits </th></tr></thead> <tbody>";
    $.each(dayWiseData, function (i, id) {
        innerDetails += '<tr><td>' + id.date + '</td><td>' + id.distance + '</td><td>' + id.visits + '</td>' + '</tr>';
        //$('#'+id.date+' td.details-control').trigger( 'click' );
    });
    innerDetails += '</tbody></table>';
    return innerDetails;
}

function exportMonthlyReport() {
    var csvData = new Array();

    var userId = document.getElementById("id_memberName").value;
    if(userId!=0)
    {
        var emp_code=users_emp_codes_map[userId]; // added by manohar
    }
    if (userId == 'select') {
        fieldSenseTosterError("Please select any member", true);
        return false;
    }
    var startTime = document.getElementById("id_startDate").value;
    var endTime = document.getElementById("id_endDate").value;
    var startTimeSplit = startTime.split('/');
    var fromDate = convertLocalDateToServerDate(startTimeSplit[2], startTimeSplit[1] - 1, startTimeSplit[0], 0, 0);
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
    var ensdTimeSplit = endTime.split('/');
    var toDate = convertLocalDateToServerDate(ensdTimeSplit[2], ensdTimeSplit[1] - 1, ensdTimeSplit[0], 23, 59);
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
    var tzoffset = getTimeZoneOffset();

    //$('#pleaseWaitDialog').modal('show');

    //Add authentication headers in URL
    //Date also added in Param to show in Excel
    var userName = $('#id_memberName').find(":selected").text();
    //var url = fieldSenseURL + "/reports/monthlyTravel/subordinate/export/"+userId+"/"+tzoffset+"/"+fromDate+"/"+toDate+"/"+userToken+"?start=0&length=-1&userName="+userName+"&startTime="+startTime+"&endTime="+endTime;
    //window.location.assign(url);
    //document.getElementById("atchmtsviewdwnld").src=url;

    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        "url": fieldSenseURL + "/reports/monthlyTravel/subordinate/" + userId + "/" + tzoffset + "/" + fromDate + "/" + toDate + "?start=0&length=-1",
        //"url":url,
        "type": "GET",
        headers: {
            "userToken": userToken
        },
        contentType: "application/json; charset=utf-8",
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            csvData.push('"User","Date","Distance Travelled (in Kms)","No. of Visits"');

            $.each(data.aaData, function (i, id) {
                var dayWiseData = id.dayWiseReport;
                if (id.distance == 0 && id.visits == 0) {
                    csvData.push('"' + id.fullName + '","-","' + id.distance + '","' + id.visits + '"');
                } else {
                    $.each(dayWiseData, function (j, jd) {
                        if (j == 0) {
                            csvData.push('"' + id.fullName + '","' + jd.date + '","' + jd.distance + '","' + jd.visits + '"');
                        } else {
                            csvData.push('"","' + jd.date + '","' + jd.distance + '","' + jd.visits + '"');
                        }
                    });
                    csvData.push('"","Total :","' + id.distance + '","' + id.visits + '"');
                    csvData.push('"","","",""');
                }
            });

            $('#pleaseWaitDialog').modal('hide');
            if(userId==0||emp_code.length==0){
                var fileName = userName + "_MonthlyTravelReport.csv";
            }
            else{
                var fileName = userName + "_MonthlyTravelReport_"+emp_code+".csv";
            }
            var buffer = csvData.join("\n");
            var blob;
            try {
                blob = new Blob([buffer], {
                    "type": "text/csv;charset=utf8;"
                });
            } catch (e) {
                window.BlobBuilder = window.BlobBuilder ||
                        window.WebKitBlobBuilder ||
                        window.MozBlobBuilder ||
                        window.MSBlobBuilder;

                if (e.name == 'TypeError' && window.BlobBuilder) {
                    var bb = new BlobBuilder();
                    bb.append(buffer);
                    blob = bb.getBlob("text/csv;charset=utf8;");
                }
                else if (e.name == "InvalidStateError") {
                    // InvalidStateError (tested on FF13 WinXP)
                    blob = new Blob([buffer], {
                        type: "text/csv;charset=utf8;"
                    });
                }
            }
            var link = document.createElement("a");
            var myURL = window.URL || window.webkitURL;
            if (link.download !== undefined) { // feature detection
                // Browsers that support HTML5 download attribute
                link.setAttribute("href", myURL.createObjectURL(blob));
                link.setAttribute("download", fileName);
            }
            else if (navigator.msSaveBlob) { // IE 10+
                navigator.msSaveBlob(blob, fileName);
            }
            else {
                link.setAttribute("href", myURL.createObjectURL(blob));
                link.setAttribute("name", fileName);
            }
            $('#pleaseWaitDialog').modal('hide');
            $('#id_exportAttendance').html('');
            link.innerHTML = '<button type="button" class="btn btn-info fr" style="margin-right:-15px;"><i class=" fa fa-file-text" id="id_exportAttendanceChild"></i> Save as CSV</button>';
            document.getElementById("id_exportAttendance").appendChild(link);

            $('#id_exportAttendanceChild').click();
            $("#id_exportAttendance").html('<button type="button" class="btn btn-info fr" style="margin-right:-15px;" onclick="exportMonthlyReport()"><i class=" fa fa-file-text" id="id_exportAttendanceChild"></i> Save as CSV</button>');

        },
        error: function (xhr, ajaxOptions, thrownError) {
            $('#pleaseWaitDialog').modal('hide');
            alert("in error:" + thrownError);
        }
    });

}

function getTimeZoneOffset() {
    var curDate = new Date();
    var timeZone = curDate.getTimezoneOffset();
    return timeZone;
}
function tConv24(time24) {
    var ts = time24;
    var sec = ts.split(":")[2];
    var H = +ts.substr(0, 2);
    var h = (H % 12) || 12;
    h = (h < 10) ? ("0" + h) : h;  // leading 0 at the left for 1 digit hours
    var ampm = H < 12 ? " AM" : " PM";
    ts = h + ts.substr(2, 3) + ":" + sec + ampm;
    return ts;
}

function prepareImageCSVFileZipAll(mainArray,linkmap){
    
    // prepare CSV data
                    zipFileName="";
                    var zipimagekey = "";
                    var excelHeader = "";
                    var csvData = new Array();
                    var k = 0;
                    excelHeader += '"Submitted by",';
                    excelHeader += '"Submitted on",';
                    while (k < formHeaders.length - 1) {

                        excelHeader += '"' + formHeaders[k].labelName + '",';
                        k = k + 1;

                    }
                    excelHeader += '"' + formHeaders[k].labelName + '"';
                    csvData.push(excelHeader);
                    excelHeader = "";
                    var i = 0;
                    var fieldValue;
                    while (i < mainArray.length) {
                        //var j=0;
                        excelHeader += '"' + mainArray[i][0] + '",';
                        excelHeader += '"' + mainArray[i][1] + '",';
                        zipimagekey = mainArray[i][1]; // added by vaibhav
                        j = 2;
                        
                        while (j <= formHeaders.length) {
                            fieldValue = mainArray[i][j];
                           
                            if(fieldValue.indexOf("data-toggle=\"modal\"")!==-1){
                                fieldValue=fieldValue.substring(fieldValue.indexOf(">")+1,fieldValue.lastIndexOf("<"));	
                           
                                    if(fieldValue != ''){            
                                        zipimagekey = zipimagekey + " " + fieldValue;
                                        var tempimagePath = linkmap[zipimagekey];
                                      
                                      // put in csv as excel value
                                        fieldValue = i + "_" + tempimagePath.substring(tempimagePath.lastIndexOf('/') + 1);
                                          // put in image zip
                                           var xhr = new XMLHttpRequest();
                                            xhr.open('GET', tempimagePath, false);
                                            //xhr.responseType = "arraybuffer";
                                            // workaround for responseType cannot be set for sync ajax
                                            xhr.overrideMimeType('text\/plain; charset=x-user-defined');
                                             // end workaround for responseType cannot be set for sync ajax
                                            xhr.onreadystatechange = function(evt) {
                                                if (xhr.readyState === 4) {

                                                if (xhr.status === 200) {
                                                     // alert(xhr.response);
                                                     // xhr.response in binary format
                                                     console.log(i + "_" + fieldValue);
                                                     imagezip.file(fieldValue, xhr.response,{binary:true});
                                                   }
                                               }
                                            };
                                            xhr.send();
                                                                               
                                    }else{
                                        fieldValue ='';
                                    }
                                }
                                
                                excelHeader += '"' + fieldValue + '",';
                                j = j + 1;
                            }
                            fieldValue = mainArray[i][j];
                            // last header
                            if(fieldValue.indexOf("data-toggle=\"modal\"")!=-1){
                            // added by vaibhav to store image link in csv
                                fieldValue=fieldValue.substring(fieldValue.indexOf(">")+1,fieldValue.lastIndexOf("<"));
                                if(fieldValue != ''){
                                    zipimagekey = zipimagekey + " " + fieldValue;
                                    var tempimagePath = linkmap[zipimagekey];
                                    fieldValue = i +"_" + tempimagePath.substring(tempimagePath.lastIndexOf('/')+1);
                                  // loading a image file and add it in a zip file
                                 console.log(tempimagePath);
                                  var xhr = new XMLHttpRequest();
                                  xhr.open('GET', tempimagePath, false);
                                  //xhr.responseType = "arraybuffer";
                                  // workaround for responseType cannot be set for sync ajax
                                   xhr.overrideMimeType('text\/plain; charset=x-user-defined');
                                   // end workaround for responseType cannot be set for sync ajax
                                  xhr.onreadystatechange = function(evt) {
                                      if (xhr.readyState === 4) {
                                          
                                      if (xhr.status === 200) {
                                           // alert(xhr.response);
                                           // xhr.response in binary format
                                           imagezip.file(fieldValue , xhr.response,{binary:true});
                                         }
                                     }
                                  };
                                  xhr.send();
                              }else{
                                    fieldValue = '';
                                }

                            }
                        excelHeader += '"' + fieldValue + '"';
                        csvData.push(excelHeader);
                        excelHeader = "";
                        i = i + 1;
                    }
                    
                    // create csv file from csv data
                    
                    if (csvData.length > 1) {
                        var fileName;
                        $(".leftnav_form").each(function () {
                            if ($(this).hasClass("active")) {
                                fileName = $(this).find(".membername").text();
                            }
                        });
                         zipFileName = fileName + "_" + $("#id_memberName option:selected").text() +".zip";
                        var fileName = fileName + "_" + $("#id_memberName option:selected").text() + ".csv";
                       
                        var buffer = csvData.join("\n");
                        var blob;
                        try {
                            blob = new Blob([buffer], {
                                "type": "text/csv;charset=utf8;"
                            });
                        } catch (e) {
                            window.BlobBuilder = window.BlobBuilder ||
                                    window.WebKitBlobBuilder ||
                                    window.MozBlobBuilder ||
                                    window.MSBlobBuilder;

                            if (e.name == 'TypeError' && window.BlobBuilder) {
                                var bb = new BlobBuilder();
                                bb.append(buffer);
                                blob = bb.getBlob("text/csv;charset=utf8;");
                            }
                            else if (e.name == "InvalidStateError") {
                                // InvalidStateError (tested on FF13 WinXP)
                                blob = new Blob([buffer], {
                                    type: "text/csv;charset=utf8;"
                                });
                            }
                        }
                        // blob is csv data in excel file
                        // save csv in zip
                        imagezip.file(fileName,blob);
                    }
}


function prepareImageandCsvforSingleUser(mainArray,imagelinkmap){
      // prepare CSV data
                    zipFileName="";
                    var excelHeader = "";
                    var csvData = new Array();
                    var k = 0;
                    excelHeader += '"Submitted on",';
                    while (k < formHeaders.length - 1) {

                        excelHeader += '"' + formHeaders[k].labelName + '",';
                        k = k + 1;

                    }
                    excelHeader += '"' + formHeaders[k].labelName + '"';
                    csvData.push(excelHeader);
                    excelHeader = "";
                    var i = 0;
                    var fieldValue;
                    while (i < mainArray.length) {
                        zipsuimageKey = mainArray[i][1]; // added by vaibhav
                        excelHeader += '"' + mainArray[i][1] + '",';
                        var j = 2;
                        while (j <= formHeaders.length) {
                            fieldValue = mainArray[i][j];
                            if(fieldValue.indexOf("data-toggle=\"modal\"")!=-1){
                                fieldValue=fieldValue.substring(fieldValue.indexOf(">")+1,fieldValue.lastIndexOf("<"));
                                   // added by vaibhav to store image link in csv
                                if(fieldValue != ''){
                                     zipsuimageKey = zipsuimageKey + " " + fieldValue;
                                     var tempimagePath = imagelinkmap[zipsuimageKey];
                                    
                                    // put in csv as excel value
                                        fieldValue = i +"_" + tempimagePath.substring(tempimagePath.lastIndexOf('/') + 1);
                                          // put in image zip
                                           var xhr = new XMLHttpRequest();
                                            xhr.open('GET', tempimagePath, false);
                                            //xhr.responseType = "arraybuffer";
                                            // workaround for responseType cannot be set for sync ajax
                                            xhr.overrideMimeType('text\/plain; charset=x-user-defined');
                                             // end workaround for responseType cannot be set for sync ajax
                                            xhr.onreadystatechange = function(evt) {
                                                if (xhr.readyState === 4) {

                                                if (xhr.status === 200) {
                                                     // alert(xhr.response);
                                                     // xhr.response in binary format
                                                     imagezip.file(fieldValue, xhr.response,{binary:true});
                                                   }
                                               }
                                            };
                                            xhr.send();
                                }else{
                                   fieldValue =''; 
                                }

                            } // ended by vai
                            excelHeader += '"' + fieldValue + '",';
                            j = j + 1;
                        }
                        fieldValue = mainArray[i][j];
                        if(fieldValue.indexOf("data-toggle=\"modal\"")!=-1){
                            // added by vaibhav
                            fieldValue=fieldValue.substring(fieldValue.indexOf(">")+1,fieldValue.lastIndexOf("<"));
                            if(fieldValue != ''){
                                 zipsuimageKey = zipsuimageKey + " " + fieldValue;
                                 var tempimagePath = imagelinkmap[zipsuimageKey];
                               // put in csv as excel value only filename
                                         fieldValue = i + "_" + tempimagePath.substring(tempimagePath.lastIndexOf('/') + 1);
                                  // put in image zip
                                           var xhr = new XMLHttpRequest();
                                            xhr.open('GET', tempimagePath, false);
                                            //xhr.responseType = "arraybuffer";
                                            // workaround for responseType cannot be set for sync ajax
                                            xhr.overrideMimeType('text\/plain; charset=x-user-defined');
                                             // end workaround for responseType cannot be set for sync ajax
                                            xhr.onreadystatechange = function(evt) {
                                                if (xhr.readyState === 4) {

                                                if (xhr.status === 200) {
                                                     // alert(xhr.response);
                                                     // xhr.response in binary format
                                                     imagezip.file(fieldValue, xhr.response,{binary:true});
                                                   }
                                               }
                                            };
                                            xhr.send();
                            }else{
                               fieldValue =''; 
                            }
                       }
                        excelHeader += '"' + fieldValue + '"';

                        csvData.push(excelHeader);
                        excelHeader = "";
                        i = i + 1;
                    }

                     // download stuff

                    if (csvData.length > 1) {
                        var fileName;
                        $(".leftnav_form").each(function () {
                            if ($(this).hasClass("active")) {
                                fileName = $(this).find(".membername").text();
                            }
                        });
                        // zipFileName = fileName + "_" + $("#id_memberName option:selected").text() +".zip";
                         var emp_code = "";
                       
                        var selected_team_member = document.getElementById("id_memberName").value;
                        if (selected_team_member !== 'select' && selected_team_member !== '0') {
                           emp_code = users_emp_codes_map[selected_team_member];
                        }
                       
                        if(emp_code !== ''){
                            zipFileName = fileName + "_" + $("#id_memberName option:selected").text() + "_" + emp_code + ".zip";
                        }else{
                           zipFileName = fileName + "_" + $("#id_memberName option:selected").text() +".zip";  
                        }
                        console.log(zipFileName);
                        var fileName = fileName + "_" + $("#id_memberName option:selected").text() + ".csv";
                        var buffer = csvData.join("\n");
                        var blob;
                        try {
                            blob = new Blob([buffer], {
                                "type": "text/csv;charset=utf8;"
                            });
                        } catch (e) {
                            window.BlobBuilder = window.BlobBuilder ||
                                    window.WebKitBlobBuilder ||
                                    window.MozBlobBuilder ||
                                    window.MSBlobBuilder;

                            if (e.name == 'TypeError' && window.BlobBuilder) {
                                var bb = new BlobBuilder();
                                bb.append(buffer);
                                blob = bb.getBlob("text/csv;charset=utf8;");
                            }
                            else if (e.name == "InvalidStateError") {
                                // InvalidStateError (tested on FF13 WinXP)
                                blob = new Blob([buffer], {
                                    type: "text/csv;charset=utf8;"
                                });
                            }
                        }
                      // blob is csv data in excel file
                        // save csv in zip
                        imagezip.file(fileName,blob); 
                    }
}

function zipImagesAndCsv(){
    if(zipFileName !== ''){
        imagezip.generateAsync({type:"blob"})
            .then(function(content) {
        // Force down of the Zip file
        saveAs(content, zipFileName); // need fileSaver.js
       });
   }else{
      // fieldSenseTosterError("No data available", true);
       
   }
}

function downloadzipImagesUsers(id){
  var fileName;
  var teamMember = document.getElementById("id_memberName").value;
    if (teamMember == 'select') {
        fieldSenseTosterError("Please select any member", true);
        return false;
    }
    var startTime = document.getElementById("id_startDate").value;
    var endTime = document.getElementById("id_endDate").value;
    var startTimeSplit = startTime.split('/');
    var fromDate = convertLocalDateToServerDate(startTimeSplit[2], startTimeSplit[1] - 1, startTimeSplit[0], 0, 0);
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
    var ensdTimeSplit = endTime.split('/');
    var toDate = convertLocalDateToServerDate(ensdTimeSplit[2], ensdTimeSplit[1] - 1, ensdTimeSplit[0], 23, 59);
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
   $(".leftnav_form").each(function () {
                            if ($(this).hasClass("active")) {
                                fileName = $(this).find(".membername").text();
                            }
    });
     
    var url = "";
    if (teamMember == "All"){
        url = fieldSenseURL + "/CustomFormsReport/downloadaszip/" + id + "/User/" + loginUserId + "/All/" + fromDate + "/" + toDate; 
        zipFileName = fileName + "_" + $("#id_memberName option:selected").text() +".zip";  
                      
        console.log(zipFileName);
    } else{
        url = fieldSenseURL + "/CustomFormsReport/downloadaszip/" + id + "/" + teamMember + "/" + fromDate + "/" + toDate;
        var emp_code = "";
                       
                        var selected_team_member = document.getElementById("id_memberName").value;
                        if (selected_team_member !== 'select' && selected_team_member !== '0') {
                           emp_code = users_emp_codes_map[selected_team_member];
                        }
                       
                        if(emp_code !== ''){
                            zipFileName = fileName + "_" + $("#id_memberName option:selected").text() + "_" + emp_code + ".zip";
                        }else{
                           zipFileName = fileName + "_" + $("#id_memberName option:selected").text() +".zip";  
                        }
                        console.log(zipFileName);
    }
//   	var xhr = new XMLHttpRequest();
//       
//			xhr.open("GET", url, true);
//			xhr.setRequestHeader("userToken", userToken);
//			xhr.setRequestHeader("Access-Control-Allow-Origin", "*");
//			xhr.onreadystatechange = function () {
//					if (xhr.readyState == 4 && xhr.status == 200) {
//							// alert("Failed to download:" + xhr.status + "---" + xhr.statusText);
//                                                      console.log(xhr.response);
//                                // If you require the blob to have correct mime type
//                                                        //var mime = this.getResponseHeader('content-type');
//                      
//							var blob = new Blob([xhr.response], { type: "application/zip" });
//							console.log(blob);
//                                                        saveAs(blob, zipFileName); 
//                                                        $('#pleaseWaitDialog').modal('hide');
//                                                              							
//					}
//			}
//			xhr.responseType = "arraybuffer";
//                        xhr.send();
//                         $('#pleaseWaitDialog').modal('show');

$('#pleaseWaitDialog').modal('show');
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
        asyn: false,
        success: function (data) {
            if (data.errorCode == '0000') {
                var customeFormImageZipFilePath = data.result;
                console.log(customeFormImageZipFilePath);
                window.location.href = customeFormImageZipFilePath;
                $('#pleaseWaitDialog').modal('hide');
            }
        }
    });
                  
}


/**
 * @Added by jyoti
 * @returns {undefined}
 */
function noDataAvailableOnZipAndCsv() {
    fieldSenseTosterError("No data available", true);
}
