/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var intervalAdminUser = window.setInterval(function () {
    var userToken2 = fieldSenseGetCookie("userToken");
    try {
        if (userToken2.toString() == "undefined") {
            window.location.href = "login.html";
        } else if (loginUserId != 0) {
            loggedinUserImageData();
            loggedinUserData();
            teamMembers();
            dateRangeTravelReports();
            custFormNames();
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
                teamMemberTemplate += '<option>Select Member</option>';
                for (var i = 0; i < teamMemberData.length; i++) {
                    var userId = teamMemberData[i].id;
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
function dateRangeTravelReports() {
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
    $('#dateRange').append(dateRangeTemplate);
    $('.date-picker').datepicker({
        rtl: App.isRTL(),
        autoclose: true
    });
}
function travelReports() {
    var exportTravelReport = new Array();
    var teamMember = document.getElementById("id_memberName").value;

    if (teamMember == 'Select Member') {
        return false;
    }
    var selectedUserName=$("#id_memberName option:selected").html();//Added By Awaneesh

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

                travelReportTemplateHtml += '</tr>';
                travelReportTemplateHtml += '</thead>';
                travelReportTemplateHtml += '<tbody>';
                var travelData = data.result;
                var totalDistance = 0;
                for (var i = 0; i < travelData.length; i++) {
                    var location = travelData[i].location;
                    if(location.trim()=="" || location.trim()=="Unknown location"){
                        location="Address could not be resolved";
                    }
                    var latitude=travelData[i].latitude;
                    var longitude=travelData[i].langitude;
                    var distance = travelData[i].travelDistance;
                    totalDistance = totalDistance + distance;
                    var createdOn = travelData[i].createdOn;
                    var timeZoneOffSet = createdOn.timezoneOffset * 60 * 1000;
                    var jsDate = new Date(createdOn.time - timeZoneOffSet);
                    var time = "";
                    if (jsDate.getMinutes() < 10) {
                        time = jsDate.getHours() + ":0" + jsDate.getMinutes();
                    } else {
                        time = jsDate.getHours() + ":" + jsDate.getMinutes();
                    }
                    
                    travelReportTemplateHtml += '<tr>';
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
                    travelReportTemplateHtml += '</tr>';
                    
                    exportTravelReport[i] = [{
                        "time": time,
                        "location": location,
                        "latitude": latitude,
                        "langitude": longitude,
                        "distance": distance

                    }];
                }
                if (totalDistance !== 0) {
                    travelReportTemplateHtml += '<tr>';
                    travelReportTemplateHtml += '<th colspan="5" style="text-align:right";>Total Distance:  <span id="id_totalCustRecords"> ' +  Math.round(totalDistance*10000)/10000  + ' kms</span></th>';
                    travelReportTemplateHtml += '</tr>';
                }
                travelReportTemplateHtml += '</tbody>';
                travelReportTemplateHtml += '</table>';
                $('.cls_travel').html(travelReportTemplateHtml);
                //Added by Awaneesh
                // prepare CSV data
                var csvData = new Array();
                //                csvData.push('"Date","Punch-in","Punch-in Location","Punch-in Location Not Found Reason","Punch-out","Punch-out Location","Punch-out Location Not Found Reason","Worked For"');
                csvData.push('"Time","Location","Latitude","Longitude","Distance (in kms)"');
                var location = "";
                for (var i = 0; i < exportTravelReport.length; i++) {
                    for (var j = 0; j < 1; j++) {
                        //location = exportTravelReport[i][j].location.toString().trim();
                        //location = location.replace("\r\n", " ");
                        //location = location.replace("\n", " ");
                        csvData.push('"' + exportTravelReport[i][j].time + '","' + exportTravelReport[i][j].location + '","' +exportTravelReport[i][j].latitude + '","' +exportTravelReport[i][j].langitude + '","' + exportTravelReport[i][j].distance + '"');
                    }
                }
                csvData.push('"","","","Total Distance","'+Math.round(totalDistance*10000)/10000+' kms"');

                // download stuff
                if (csvData.length > 1) {
                    var fileName = selectedUserName+"_Travel.csv"; //teamMember + "_Travel.csv";
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
                    $('#id_exportTravel').html('');
                    link.innerHTML = '<button type="button" class="btn btn-info fr" style="margin-right:-15px;"><i class=" fa fa-file-text" id="id_exportTravelChild"></i> Save as CSV</button>';
                    document.getElementById("id_exportTravel").appendChild(link);
                }
                //End By Awaneesh
                $('#pleaseWaitDialog').modal('hide');
                $('#sample_5').dataTable({
                    //                    "aaSorting": [[1, 'asc']],
                    "aLengthMenu": [
                        [-1, 5, 10, 15, 20],
                        ["All", 5, 10, 15, 20] // change per page values here
                    ],
                    // set the initial value
                    "iDisplayLength": -1,
                    "bFilter": false
                });

                jQuery('#sample_5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
                jQuery('#sample_5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
                jQuery('#sample_5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown
            }
        }
    });
}



