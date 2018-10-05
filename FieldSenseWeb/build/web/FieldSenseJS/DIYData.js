/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var intervalAdminUser = window.setInterval(function () {
    var userToken2 = fieldSenseGetCookie("userToken");
    try {
        if (userToken2.toString() == "undefined") {
            window.location.href = "login.html";
        } else if (loginUserId != 0) {
            if (role == 0) {
                var date = new Date();
                $('#year').html(date.getFullYear() + " © QuantumLink Communications. All Rights Reserved.");
                loggedinUserImageData();
                dateRange();
                getDIYData();
//                getDIYDataDiv();
                window.clearTimeout(intervalAdminUser);
            } else {
                window.location.href = "login.html";
            }

        }
    } catch (err) {
        alert("exception " + err);
        window.location.href = "login.html";
    }
}, 1000);

function getDIYDataDiv() {
    $("#reports_show").attr("onclick", "getDIYData()");
    $("#reports_show").attr("style", "margin-left: 47px;");
    $('#dateRange').attr("class", "col-md-5");
    dateRange();
    var diyDataTemplate = '<table class="table table-striped table-bordered table-hover" id="sample_5">';
    diyDataTemplate += '<thead>';
    diyDataTemplate += ' <tr>';
    diyDataTemplate += '<th colspan="7">User Details</th>';
    diyDataTemplate += '<th colspan="9">Company Details</th>';
    diyDataTemplate += '<th colspan="2">Plan Details</th>';
    diyDataTemplate += '<th colspan="10">Other Details</th>';
    diyDataTemplate += '</tr>';
    diyDataTemplate += '<tr>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'First name';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'Last name';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'Full name';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'Email address';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'Mobile number';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'Gender';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'Designation';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'Company name';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'Country';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'State';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'City';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'Address';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'Zip code';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'Phone number';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'Industry';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'Website';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'Plan';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'User limit';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'Step Reached';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'Is account created';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'Mail link expired on';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'Terms & Conditions agreed';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'Newsletter opt in';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'Source value';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'Feature value';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'Device';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'Mobile verified';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'Email address verified';
    diyDataTemplate += '</th>';
    diyDataTemplate += '<th>';
    diyDataTemplate += 'Created on';
    diyDataTemplate += '</th>';
    diyDataTemplate += '</tr>';
    diyDataTemplate += '</thead>';
    diyDataTemplate += '<tbody></tbody></table>';
    $("#id_displayDiyData").html(diyDataTemplate);
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

function getDIYData() {

    $("#id_saveAsCsvDiyData").html('<button type="button" class="btn btn-info fr" ><i class=" fa fa-file-text"></i> Save as CSV</button>');

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
    var diyDataTemplate = '';
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/stats/diy/getDiy4StepsData/" + fromDate + "/" + toDate,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: false,
        success: function (data) {
            console.log(data.result);
            if (data.errorCode == '0000') {
                var diyData = data.result;
                console.log(diyData);
                myTable = $("#sample_5").DataTable();
                if (myTable != null)
                    myTable.destroy();
                $('#id_displayDiyData').html('');
                diyDataTemplate += '<table class="table table-striped table-bordered table-hover" id="sample_5">';
                diyDataTemplate += '<thead>';
//                diyDataTemplate += ' <tr>';
//                diyDataTemplate += '<th colspan="7">USER DETAILS</th>';
//                diyDataTemplate += '<th colspan="9">COMPANY DETAILS</th>';
//                diyDataTemplate += '<th colspan="2">PLAN DETAILS</th>';
//                diyDataTemplate += '<th colspan="10">OTHER DETAILS</th>';
//                diyDataTemplate += '</tr>';
                diyDataTemplate += '<tr>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'Form filled on';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'Is account created';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'Step Reached';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'First name';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'Last name';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'Full name';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'Email address';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'Mobile number';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'Gender';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'Designation';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'Company name';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'Country';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'State';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'City';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'Address';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'Zip code';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'Phone number';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'Industry';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'Website';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'Plan';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'User limit';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'Mail link expired on';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'Terms & Conditions agreed';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'Newsletter opt in';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'Source value';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'Feature value';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'Device';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'Mobile verified';
                diyDataTemplate += '</th>';
                diyDataTemplate += '<th>';
                diyDataTemplate += 'Email address verified';
                diyDataTemplate += '</th>';
                diyDataTemplate += '</tr>';
                diyDataTemplate += '</thead>';
                diyDataTemplate += '<tbody>';

                var exportDiyData = new Array(); // for csv data array created

                for (var i = 0; i < diyData.length; i++) {
                    var firstName = diyData[i].first_name;
                    var lastName = diyData[i].last_name;
                    var fullName = diyData[i].full_name;
                    var emailAddress = diyData[i].email_address;
                    var countryCode = diyData[i].country_code;
                    var mobileNumber = diyData[i].mobile_number;
                    var gender = diyData[i].gender;
                    if (gender == 1) {
                        gender = 'Male';
                    } else if (gender == 0) {
                        gender = 'Female';
                    }
                    var designation = diyData[i].designation;

                    var companyName = diyData[i].company_name;
                    var country = diyData[i].country;
                    var state = diyData[i].state;
                    var city = diyData[i].city;
                    var address = diyData[i].address1;
                    var zipCode = diyData[i].zip_code;
                    var phoneNumber = diyData[i].company_phone_number1;
                    var industry = diyData[i].industry;
                    var website = diyData[i].company_website;

                    var plan = diyData[i].plan;
                    var userLimit = diyData[i].user_limit;

                    var stepNumber = diyData[i].step_number;
                    var accountCreated = diyData[i].is_account_created;
                    if (accountCreated == 1) {
                        accountCreated = 'YES';
                    } else if (accountCreated == 0) {
                        accountCreated = 'NO';
                    }
                    var mailExpiredOnServerDateTime = diyData[i].mail_expired_on;
                    var mailExpiredOnLocalDateTime = convertServerDateToLocalDate(mailExpiredOnServerDateTime.year + 1900, mailExpiredOnServerDateTime.month, mailExpiredOnServerDateTime.date, mailExpiredOnServerDateTime.hours, mailExpiredOnServerDateTime.minutes)
                    var mailExpiredOn = fieldSenseDateTimeForExpenseJsDate(mailExpiredOnLocalDateTime);
                    if (mailExpiredOn == '11 Nov 1111 05:30 am') {
                        mailExpiredOn = '-';
                    }
                    var termsConditionAgreed = diyData[i].is_terms_condition_agreed;
                    if (termsConditionAgreed == 1) {
                        termsConditionAgreed = 'YES';
                    } else if (termsConditionAgreed == 0) {
                        termsConditionAgreed = 'NO';
                    } else {
                        termsConditionAgreed = 'UNKNOWN';
                    }
                    var newsLetter = diyData[i].is_newsletter_opt_in;
                    if (newsLetter == 1) {
                        newsLetter = 'YES';
                    } else if (newsLetter == 0) {
                        newsLetter = 'NO';
                    } else {
                        newsLetter = 'UNKNOWN';
                    }
                    var gSource = diyData[i].gSource;
                    var gFeature = diyData[i].gFeature;
                    var device = diyData[i].device;
                    var mobileValid = diyData[i].is_mobile_valid;
                    if (mobileValid == 1) {
                        mobileValid = 'YES';
                    } else if (mobileValid == 0) {
                        mobileValid = 'NO';
                    } else {
                        mobileValid = 'UNKNOWN';
                    }
                    var isMailValid = diyData[i].isMailValid;
                    if (isMailValid == 1) {
                        isMailValid = 'YES';
                    } else if (isMailValid == 0) {
                        isMailValid = 'NO';
                    } else {
                        isMailValid = 'UNKNOWN';
                    }
                    var createdOnServerDateTime = diyData[i].created_on;
                    var createdOnLocalDateTime = convertServerDateToLocalDate(createdOnServerDateTime.year + 1900, createdOnServerDateTime.month, createdOnServerDateTime.date, createdOnServerDateTime.hours, createdOnServerDateTime.minutes)
                    var createdOn = fieldSenseDateTimeForExpenseJsDate(createdOnLocalDateTime);

                    // store data for csv in an array for export
                    exportDiyData[i] = [{
                            "createdOn": createdOn,
                            "accountCreated": accountCreated,
                            "stepNumber": stepNumber,
                            "firstName": firstName,
                            "lastName": lastName,
                            "fullName": fullName,
                            "emailAddress": emailAddress,
                            "mobileNumber": (countryCode) + mobileNumber,
                            "gender": gender,
                            "designation": designation,
                            "companyName": companyName,
                            "country": country,
                            "state": state,
                            "city": city,
                            "address": address,
                            "zipCode": zipCode,
                            "phoneNumber": phoneNumber,
                            "industry": industry,
                            "website": website,
                            "plan": plan,
                            "userLimit": userLimit,
                            "mailExpiredOn": mailExpiredOn,
                            "termsConditionAgreed": termsConditionAgreed,
                            "newsLetter": newsLetter,
                            "gSource": gSource,
                            "device": device,
                            "mobileValid": mobileValid,
                            "isMailValid": isMailValid
                        }];
                    // end storing data for export

                    // display data in table start ..
                    diyDataTemplate += '<tr>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += createdOn;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += accountCreated;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += stepNumber;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += firstName;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += lastName;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += fullName;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += emailAddress;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += (countryCode) + mobileNumber;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += gender;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += designation;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += companyName;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += country;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += state;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += city;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += address;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += zipCode;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += phoneNumber;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += industry;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += website;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += plan;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += userLimit;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += mailExpiredOn;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += termsConditionAgreed;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += newsLetter;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += gSource;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += gFeature;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += device;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += mobileValid;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '<td>';
                    diyDataTemplate += isMailValid;
                    diyDataTemplate += '</td>';
                    diyDataTemplate += '</tr>';
                }
                diyDataTemplate += '</tbody>';
                diyDataTemplate += '</table>';
                $('#id_displayDiyData').html(diyDataTemplate);

                // prepare CSV data
                var csvData = new Array();
                csvData.push('"Form filled on","Is account created","Step Reached","First name","Last name","Full name","Email address","Mobile number","Gender","Designation","Company name","Country","State","City","Address","Zip code","Phone number","Industry","Website","Plan","User limit","Mail link expired on","Terms & Conditions agreed","Newsletter opt in","Source value","Feature value","Device","Mobile verified","Email address verified"');
                for (var i = 0; i < exportDiyData.length; i++) {
                    csvData.push('"' + exportDiyData[i][0].createdOn + '","' + exportDiyData[i][0].accountCreated + '","' + exportDiyData[i][0].stepNumber + '","' + exportDiyData[i][0].firstName + '","' + exportDiyData[i][0].lastName + '","' + exportDiyData[i][0].fullName + '","' + exportDiyData[i][0].emailAddress + '","' + exportDiyData[i][0].mobileNumber + '","' + exportDiyData[i][0].gender + '","' + exportDiyData[i][0].designation + '","' + exportDiyData[i][0].companyName + '","' + exportDiyData[i][0].country + '","' + exportDiyData[i][0].state + '","' + exportDiyData[i][0].city + '","' + exportDiyData[i][0].address + '","' + exportDiyData[i][0].zipCode + '","' + exportDiyData[i][0].phoneNumber + '","' + exportDiyData[i][0].industry + '","' + exportDiyData[i][0].website + '","' + exportDiyData[i][0].plan + '","' + exportDiyData[i][0].userLimit + '","' + exportDiyData[i][0].mailExpiredOn + '","' + exportDiyData[i][0].termsConditionAgreed + '","' + exportDiyData[i][0].newsLetter + '","' + exportDiyData[i][0].gSource + '","' + exportDiyData[i][0].gFeature + '","' + exportDiyData[i][0].device + '","' + exportDiyData[i][0].mobileValid + '","' + exportDiyData[i][0].isMailValid + '"');
                }
                // download stuff
                if (csvData.length > 1) {
                    var fileName = "DIY_data.csv";
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
                        } else if (e.name == "InvalidStateError") {
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
                    } else if (navigator.msSaveBlob) { // IE 10+
                        navigator.msSaveBlob(blob, fileName);
                    } else {
                        link.setAttribute("href", myURL.createObjectURL(blob));
                        link.setAttribute("name", fileName);
                    }
                    $('#id_saveAsCsvDiyData').html('');
                    link.innerHTML = '<button type="button" class="btn btn-info fr" ><i class=" fa fa-file-text"></i> Save as CSV</button>';
                    document.getElementById("id_saveAsCsvDiyData").appendChild(link);
                }
                // end preparing csv

                $('#pleaseWaitDialog').modal('hide');
                $('#sample_5').dataTable({
                    "aaSorting": [],
                    "sPaginationType": "bootstrap_full_number",
                    //"scrollCollapse": true,
                    //"paging": true,   
                    "aLengthMenu": [
                        [-1, 5, 10, 15, 20, 50, 100],
                        ["All", 5, 10, 15, 20, 50, 100] // change per page values here
                    ],
                    // set the initial value
                    "iDisplayLength": 20,
                    "bFilter": true
                });

                jQuery('#sample_5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
                jQuery('#sample_5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
                jQuery('#sample_5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown
                $('.dataTables_filter input').attr("placeholder", "Search here...");
            } else {
                fieldSenseTosterError(data.errorMessage, true);
                $('#pleaseWaitDialog').modal('hide');
            }

        }
    });
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
