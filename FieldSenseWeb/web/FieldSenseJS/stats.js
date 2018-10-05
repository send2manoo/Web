/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var intervalAdminUser = window.setInterval(function () {
    var userToken2 = fieldSenseGetCookie("userToken");
    // alert(userToken2+" userToken2");
    try {
        if (userToken2.toString() == "undefined") {
            //  alert("intervalCustomer stats + undefined condition" + intervalAdminUser);
            window.location.href = "login.html";
        } else if (loginUserId != 0) {
            if (role == 0) {
                leftSideMenu();
                getAccountRegions(); // load all region , from same method set region in search filter
                statsData();
                window.clearTimeout(intervalAdminUser);
            } else {
                window.location.href = "dashboard.html";
            }

        }
    }
    catch (err) {
//alert("exception " + err);
        window.location.href = "login.html";
    }
}, 1000);
var accountRegions;
var deleteIds = [];
function getAccountRegions() {
    ///alert("getAccountRegions");
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/account/regions",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        //data:jsonData,
        cache: false,
        dataType: 'json',
        asyn: false,
        success: function (data) {
            if (data.errorCode == '0000') {
                accountRegions = data.result;
            }
            var regionFilterData = "<option value='0' selected>All</option>";
            $.each(accountRegions, function (i, data) {
                regionFilterData += '<option value="' + data.id + '">' + data.region_name + '</option>';
            });
            $("#id_searchRegion").html(regionFilterData);
        },
        error: function (xhr, status, error) {
            var err = eval("(" + xhr.responseText + ")");
            var message = err.fieldErrors[0].message;
            fieldSenseTosterError(message, true);
        }
    });
}

function leftSideMenu() {
//var creAccountAndLogOutHtml='<a href="" class="btn btn-default pull-right" style="margin-top: 5px;" onclick="logout();return false;"><i class="fa fa-key"></i> Log Out</a>';
//creAccountAndLogOutHtml+='<a href="support.html" class="btn btn-default pull-right" style="margin-top: 5px;margin-right: 5px;"><i class="fa fa-plus"></i>  Create Account</a>';
//$('#id_stats_cre_acnt_logout').html(creAccountAndLogOutHtml);
    $("#id_status").val("All");
    $("#id_activityFrequency").val("Any");
    $("#id_accSearchText").val("");
    $("#id_start_date").val("");
    $("#id_expiry_date").val("");
    $("#id_plan_filter").val("All");
    // alert("leftslider status" + $("#id_status").val());

}

function clearFilters() {
//    alert("dfsf354");
    $('#select_region').val("0");
    $("#id_status").val("All");
    $("#id_activityFrequency").val("Any");
    $("#id_accSearchText").val("");
    $("#id_searchRegion").val("0");
    $('#id_expiry_date').val("");
    $('#id_start_date').val("");
    $("#id_plan_filter").val("All");
//    document.getElementById("id_plan_filter").options[document.getElementById("id_plan_filter").selectedIndex].value="All";
    statsData();
}

function statsData() {
//alert("statsData");
    var plan_filter = document.getElementById("id_plan_filter").options[document.getElementById("id_plan_filter").selectedIndex].value;
//    alert(plan_filter);
    var regionId = $('#select_region').val();
    console.log("region $" + regionId);
    var regionId = $('#select_region').val();
    console.log("region $"+regionId);
    var accname = $('#id_accSearchText').val();
    var status = $("#id_status").val();
    var actfreq = $("#id_activityFrequency").val();
    var region = $("#select2_region").val();
    console.log("region # " + region);
    if ($("#id_expiry_date").val() != "")
    {
        var Expdate = dateConverter($("#id_expiry_date").val());
    } else
    {
        var Expdate = "";
    }

    if ($("#id_start_date").val() != "")
    {
        var Startdate = dateConverter($("#id_start_date").val());
    } else
    {
        var Startdate = "";
    }



// alert("Expdate" + Expdate);
//	var regionId=$("#id_searchRegion").val();
//alert("regionId"+regionId);
//        var regionId=1;
    var start = 0;
    var length = 20;
    myTable = $("#sample_5").DataTable();
    if (myTable != null)
        myTable.destroy();
    var statsDataHtml = '';
    statsDataHtml += '<table class="table table-striped table-bordered table-hover" id="sample_5">';
    statsDataHtml += '<thead>';
    statsDataHtml += '<tr>';
    statsDataHtml += '<th>';
    statsDataHtml += 'Account Name';
    statsDataHtml += '</th>';
    statsDataHtml += '<th>';
    statsDataHtml += 'Plan';
    statsDataHtml += '</th>';
    statsDataHtml += '<th>';
    statsDataHtml += 'No. of Users';
    statsDataHtml += '</th>';
    statsDataHtml += '<th>';
    statsDataHtml += 'User Limit';
    statsDataHtml += '</th>';
    statsDataHtml += '<th>';
    statsDataHtml += 'Created Date';
    statsDataHtml += '</th>';
    statsDataHtml += '<th>';
    statsDataHtml += 'Start Date';
    statsDataHtml += '</th>';
    statsDataHtml += '<th>';
    statsDataHtml += 'Last Access Date';
    statsDataHtml += '</th>';
    statsDataHtml += '<th>';
    statsDataHtml += 'Status';
    statsDataHtml += '</th>';
//    statsDataHtml += '<th>';
//    statsDataHtml += 'Last Access Date';
//    statsDataHtml += '</th>';
    statsDataHtml += '<th>';
    statsDataHtml += 'Activity Frequency (%)';
    statsDataHtml += '</th>';
    statsDataHtml += '<th>';
    statsDataHtml += 'Region';
    statsDataHtml += '</th>';
    statsDataHtml += '<th>';
    statsDataHtml += '</th>';
    statsDataHtml += '</tr>';
    statsDataHtml += '</thead>';
    statsDataHtml += '<tbody style="font-size: small;font-family: "Open Sans",sans-serif;">';
    statsDataHtml += '</tbody>';
    statsDataHtml += '</table>';
    $('#id_statsData').html(statsDataHtml);
    var inboxtable = $('#sample_5').dataTable({
        "bServerSide": true,
        "bDestroy": true,
        "bFilter": false,
        //searchDelay: 350,
        "ajax": {
            //"url": fieldSenseURL + "/stats/accountsData/offset/?accname="+accname+"&status="+status+"&actfreq="+actfreq+"&regionId="+regionId,
            "url": fieldSenseURL + "/stats/accountsData/offset/?accname=" + accname + "&status=" + status + "&actfreq=" + actfreq + "&created_on=" + Expdate + "&startdate=" + Startdate + "&regionId="+regionId +"&plan="+plan_filter,

            "type": "GET",
            headers: {
                "userToken": userToken
            },
            contentType: "application/json; charset=utf-8",
            crossDomain: false,
            cache: false,
            dataType: 'json',
            asyn: true,
        },
        showSearchInput: true,
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
                "aTargets": [10]

            }
        ],
        "aoColumns": [// ahmed : here we populate the data in datatable that is returned using Ajax
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.accountName;
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.plan;
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.usersCount - 1; /*removed comapny default user*/
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.usersLimit;
                }
            },
            {"mData": null,
                "sClass": "hidden-xs",
                "mRender": function (data, type, full) {
                    // console.log("full.createdOn "+full.createdOn);
                    var createdOn = full.createdOn;
                    createdOn = convertServerDateToLocalDate((createdOn.year) + 1900, createdOn.month, createdOn.date, createdOn.hours, createdOn.minutes);
                    var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
                    createdOn = createdOn.getDate() + ' ' + monthNames[createdOn.getMonth()] + ' ' + createdOn.getFullYear();
                    return createdOn;
                }
            },
            {"mData": null,
                "sClass": "hidden-xs",
                "mRender": function (data, type, full) {
                    console.log("full.startDate 1" + full.startdate);
                    var startdate = full.startdate;
                    startdate = convertServerDateToLocalDate((startdate.year) + 1900, startdate.month, startdate.date, startdate.hours, startdate.minutes);
                    var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
                    startdate = startdate.getDate() + ' ' + monthNames[startdate.getMonth()] + ' ' + startdate.getFullYear();
                    return startdate;
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    var lastAccessDate = full.lastPunchInDateOfanAccount;
                    lastAccessDate = convertServerDateToLocalDate((lastAccessDate.year) + 1900, lastAccessDate.month, lastAccessDate.date, lastAccessDate.hours, lastAccessDate.minutes);
                    var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
                    lastAccessDate = lastAccessDate.getDate() + ' ' + monthNames[lastAccessDate.getMonth()] + ' ' + lastAccessDate.getFullYear();
                    if (lastAccessDate == "1 Jan 1970") {
                        return '';
                    } else {
                        return lastAccessDate;
                    }

                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {

                    if (full.isActive) {
                        return '<span class="label label-sm label-success">Active</span>';
                    } else {
                        return '<span class="label label-sm label-danger">Inactive</span>';
                    }
                }
            },
//            {"mData": null,
//                "sClass": "inbox-small-cells",
//                "mRender": function (data, type, full) {
//                    var lastAccessDate = full.lastPunchInDateOfanAccount;
//                    lastAccessDate = convertServerDateToLocalDate((lastAccessDate.year) + 1900, lastAccessDate.month, lastAccessDate.date, lastAccessDate.hours, lastAccessDate.minutes);
//                    var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
//                    lastAccessDate = lastAccessDate.getDate() + ' ' + monthNames[lastAccessDate.getMonth()] + ' ' + lastAccessDate.getFullYear();
//                    if (lastAccessDate == "1 Jan 1970") {
//                        return '';
//                    } else {
//                        return lastAccessDate;
//                    }
//
//                }
//            },
            {"mData": null,
                "sClass": "hidden-xs",
                "mRender": function (data, type, full) {
                    return full.activityFrequency;
                }
            },
            {"mData": null,
                "sClass": "hidden-xs",
                "mRender": function (data, type, full) {
                    return full.regionName; //regionName

                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
//                    return '<button type="button" class="btn btn-default btn-xs tooltips" data-toggle="modal" href="#responsive2" data-placement="bottom" title="Edit" onclick="editAccTemplate(\'' + full.id + '\');"><i class="fa fa-edit"></i></button>';
//                         return '<button type="button" class="btn btn-default btn-xs tooltips" data-toggle="modal" href="#responsive2" data-placement="bottom" title="Edit" onclick="createAccTemplate();"><i class="fa fa-edit"></i></button>';

                    return '<button data-target="#acc_cre" type="button" class="btn btn-default btn-xs tooltips"  data-toggle="modal" data-placement="bottom" title="Details" onclick="editAccTemplate(\'' + full.id + '\');" ><i class="fa fa-file-text-o"></i> </button>';
                } // modified by manohar

            }
        ],
        "aaSorting": [4, "desc"],
        "preDrawCallback": function (settings) {
            $('#pleaseWaitDialog').modal('show');
        },
        "fnDrawCallback": function (oSettings) {
            if (oSettings.fnDisplayEnd() > 0) {
                $("#id_totalCustRecords").html(" records");
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
//    $('#sample_5_filter input').unbind();
//$('#sample_5_filter input').bind('keyup', function(e) {
//if(e.keyCode == 13) {
//inboxtable.fnFilter(this.value);
//}
//});
    jQuery('#sample_5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
    jQuery('#sample_5_wrapper .dataTables_length select').addClass(""); // modify table per page dropdown
    jQuery('#sample_5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown		
}

function statsData1() {



    var accname = $('#id_accSearchText').val();
    var status = $("#id_status").val();
    var actfreq = $("#id_activityFrequency").val();
    var regionId = $("#id_searchRegion").val();
    var start = 0;
    var length = 20;
    // alert("statsData1");
    myTable = $("#sample_5").DataTable();
    if (myTable != null)
        myTable.destroy();
    var statsDataHtml = '';
    statsDataHtml += '<table class="table table-striped table-bordered table-hover" id="sample_5">';
    statsDataHtml += '<thead>';
    statsDataHtml += '<tr>';
    statsDataHtml += '<th>';
    statsDataHtml += 'Account Name';
    statsDataHtml += '</th>';
    statsDataHtml += '<th>';
    statsDataHtml += 'Plan';
    statsDataHtml += '</th>';
    statsDataHtml += '<th>';
    statsDataHtml += 'No. of Users';
    statsDataHtml += '</th>';
    statsDataHtml += '<th>';
    statsDataHtml += 'User Limit';
    statsDataHtml += '</th>';
    statsDataHtml += '<th>';
    statsDataHtml += 'Created Date';
    statsDataHtml += '</th>';
    statsDataHtml += '<th>';
    statsDataHtml += 'Start Date';
    statsDataHtml += '</th>';
    statsDataHtml += '<th>';
    statsDataHtml += 'Last Access Date';
    statsDataHtml += '</th>';
    statsDataHtml += '<th>';
    statsDataHtml += 'Status';
    statsDataHtml += '</th>';
    statsDataHtml += '<th>';
    statsDataHtml += 'Activity Frequency (%)';
    statsDataHtml += '</th>';
    statsDataHtml += '<th>';
    statsDataHtml += 'Region';
    statsDataHtml += '</th>';
    statsDataHtml += '<th>';
    statsDataHtml += '</th>';
    statsDataHtml += '</tr>';
    statsDataHtml += '</thead>';
    statsDataHtml += '<tbody>';
//        statsDataHtml += '<tr class="odd gradeX">';
//        statsDataHtml += '<td>';
//        statsDataHtml += '<a data-toggle="modal" href="#acc_dtl" class="tooltips" data-placement="bottom" title="Account Details">RatanFire</a>'
//        statsDataHtml += '</td>';
//        statsDataHtml += '<td>';
//        statsDataHtml += 'Free';
//        statsDataHtml += '</td>';
//         statsDataHtml += '<td>';
//        statsDataHtml += '31';
//        statsDataHtml += '</td>';
//         statsDataHtml += '<td>';
//        statsDataHtml += '40';
//        statsDataHtml += '</td>';
//         statsDataHtml += '<td>';
//        statsDataHtml += '20 Apr 2015';
//        statsDataHtml += '</td>';
//         statsDataHtml += '<td>';
//        statsDataHtml += '20 Apr 2015';
//        statsDataHtml += '</td>';
//         statsDataHtml += '<td>';
//        statsDataHtml += '28 Apr 2015';
//        statsDataHtml += '</td>';
//         statsDataHtml += '<td>';
//        statsDataHtml += '<span class="label label-sm label-danger">Inactive</span>';
//        statsDataHtml += '</td>';
//         statsDataHtml += '<td>';
//        statsDataHtml += '10%';
//        statsDataHtml += '</td>';
//         statsDataHtml += '<td>';
//        statsDataHtml += 'Mumbai';
//        statsDataHtml += '</td>';
//        
//        statsDataHtml += '</tr>';
    statsDataHtml += '</tbody>';
    statsDataHtml += '</table>';
    $('#id_statsData').html(statsDataHtml);
    var inboxtable = $('#sample_5').dataTable({
        "bServerSide": true,
        "bDestroy": true,
        "ajax": {
            "url": fieldSenseURL + "/stats/accountsData/offset/?accname=" + accname + "&status=" + status + "&actfreq=" + actfreq + "&regionId=" + regionId,
            //after this line reloading
            "type": "GET",
            headers: {
                "userToken": userToken
            },
            contentType: "application/json; charset=utf-8",
            crossDomain: false,
            cache: false,
            dataType: 'json',
            asyn: true,
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
                "aTargets": [10]

            }
        ],
        "aoColumns": [// ahmed : here we populate the data in datatable that is returned using Ajax

            //account name
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.accountName;
                }
            },
            //plan
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.plan;
                }
            },
            // no. of users
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.usersCount - 1; /*removed comapny default user*/
                }
            },
            //user limit
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.usersLimit;
                }
            },
            //created date
            {"mData": null,
                "sClass": "hidden-xs",
                "mRender": function (data, type, full) {
                    var createdOn = full.createdOn;
                    createdOn = convertServerDateToLocalDate((createdOn.year) + 1900, createdOn.month, createdOn.date, createdOn.hours, createdOn.minutes);
                    var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
                    createdOn = createdOn.getDate() + ' ' + monthNames[createdOn.getMonth()] + ' ' + createdOn.getFullYear();
                    return createdOn;
                }
            },
            // start date
            {"mData": null,
                "sClass": "hidden-xs",
                "mRender": function (data, type, full) {
                    var createdOn = full.createdOn;
                    createdOn = convertServerDateToLocalDate((createdOn.year) + 1900, createdOn.month, createdOn.date, createdOn.hours, createdOn.minutes);
                    var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
                    createdOn = createdOn.getDate() + ' ' + monthNames[createdOn.getMonth()] + ' ' + createdOn.getFullYear();
                    return createdOn;
                }
            },
            // lastaccessdate
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    var lastAccessDate = full.lastPunchInDateOfanAccount;
                    lastAccessDate = convertServerDateToLocalDate((lastAccessDate.year) + 1900, lastAccessDate.month, lastAccessDate.date, lastAccessDate.hours, lastAccessDate.minutes);
                    var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
                    lastAccessDate = lastAccessDate.getDate() + ' ' + monthNames[lastAccessDate.getMonth()] + ' ' + lastAccessDate.getFullYear();
                    if (lastAccessDate == "1 Jan 1970") {
                        return '';
                    } else {
                        return lastAccessDate;
                    }

                }
            },
            // status
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    if (full.isActive) {
                        return '<span class="label label-sm label-success">Active</span>';
                    } else {
                        return '<span class="label label-sm label-danger">Inactive</span>';
                    }
                }
            },
//				{ "mData": null,
//				"sClass":"inbox-small-cells",
//				"mRender" : function(data,type,full){
//                                     		var lastAccessDate= full.lastPunchInDateOfanAccount;
//                                    		lastAccessDate=convertServerDateToLocalDate((lastAccessDate.year) + 1900, lastAccessDate.month, lastAccessDate.date, lastAccessDate.hours, lastAccessDate.minutes);
//                                    		var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
//                                    		lastAccessDate=lastAccessDate.getDate()+' '+monthNames[lastAccessDate.getMonth()]+' '+lastAccessDate.getFullYear();
//						if(lastAccessDate=="1 Jan 1970"){
//							return '';
//						}else{
//							return lastAccessDate;
//						}
//
//					}
//				},
            //active freq
            {"mData": null,
                "sClass": "hidden-xs",
                "mRender": function (data, type, full) {
                    return full.activityFrequency;
                }
            },
            //region name
            {"mData": null,
                "sClass": "hidden-xs",
                "mRender": function (data, type, full) {
                    return full.regionName; //regionName

                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return '<button type="button" class="btn btn-default btn-xs tooltips" data-toggle="modal" href="#responsive2" data-placement="bottom" title="Edit" onclick="editAccTemplate(\'' + full.id + '\');"><i class="fa fa-edit"></i></button>';
                }
            }
        ],
        "aaSorting": [3, "desc"],
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




function showUser() {



    var mobile = $('#id_accSearchText').val();
//	var status=$("#id_status").val();
//	var actfreq=$("#id_activityFrequency").val();
//	var regionId=$("#id_searchRegion").val();
//        var start=0;
//	var length=20;
    myTable = $("#sample_5").DataTable();
    if (myTable != null)
        myTable.destroy();
    var statsDataHtml = '';
    statsDataHtml += '<table class="table table-striped table-bordered table-hover" id="sample_5">';
    statsDataHtml += '<thead>';
    statsDataHtml += '<tr>';
    statsDataHtml += '<th>';
    statsDataHtml += 'Full Name';
    statsDataHtml += '</th>';
    statsDataHtml += '<th>';
    statsDataHtml += 'Company Name';
    statsDataHtml += '</th>';
    statsDataHtml += '<th>';
    statsDataHtml += 'Email-id';
    statsDataHtml += '</th>';
    statsDataHtml += '</tr>';
    statsDataHtml += '</thead>';
    statsDataHtml += '<tbody>';
    statsDataHtml += '</tbody>';
    statsDataHtml += '</table>';
    $('#id_statsData').html(statsDataHtml);
    var inboxtable = $('#sample_5').dataTable({
        "bServerSide": true,
        "bDestroy": true,
        "ajax": {
            "url": fieldSenseURL + "/stats/account/user/mobileno?searchText=" + mobile,
            "type": "GET",
            headers: {
                "userToken": userToken
            },
            contentType: "application/json; charset=utf-8",
            crossDomain: false,
            cache: false,
            dataType: 'json',
            asyn: true,
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
                "aTargets": [08]

            }
        ],
        "aoColumns": [// ahmed : here we populate the data in datatable that is returned using Ajax
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.full_name;
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.accountName; /*removed comapny default user*/
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.email_id;
                }
            },
//				{ "mData": null,
//				"sClass":"hidden-xs",
//				"mRender" : function(data,type,full){
//			            		var createdOn= full.createdOn;
//                                    		createdOn=convertServerDateToLocalDate((createdOn.year) + 1900, createdOn.month, createdOn.date, createdOn.hours, createdOn.minutes);
//                                    		var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
//                                    		createdOn=createdOn.getDate()+' '+monthNames[createdOn.getMonth()]+' '+createdOn.getFullYear();
//				   		return createdOn;	
//                                }
//				},
//                                { "mData": null,
//				"sClass":"inbox-small-cells",
//				"mRender" : function(data,type,full){
//						if(full.isActive){
//                                                	return '<span class="label label-sm label-success">Active</span>';
//                                                }else{
//                                                    	return '<span class="label label-sm label-danger">InActive</span>'; 
//                                                }
//					}
//				},
//				{ "mData": null,
//				"sClass":"inbox-small-cells",
//				"mRender" : function(data,type,full){
//                                     		var lastAccessDate= full.lastPunchInDateOfanAccount;
//                                    		lastAccessDate=convertServerDateToLocalDate((lastAccessDate.year) + 1900, lastAccessDate.month, lastAccessDate.date, lastAccessDate.hours, lastAccessDate.minutes);
//                                    		var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
//                                    		lastAccessDate=lastAccessDate.getDate()+' '+monthNames[lastAccessDate.getMonth()]+' '+lastAccessDate.getFullYear();
//						if(lastAccessDate=="1 Jan 1970"){
//							return '';
//						}else{
//							return lastAccessDate;
//						}
//
//					}
//				},
//                                { "mData": null,
//				"sClass":"hidden-xs",
//				"mRender" : function(data,type,full){
//						return full.activityFrequency;
//                                                
//					}
//				},
//				{ "mData": null,
//				"sClass":"hidden-xs",
//				"mRender" : function(data,type,full){
//						return full.regionName;//regionName
//                                                
//					}
//				},
//                                { "mData": null,
//				"sClass":"inbox-small-cells",
//				"mRender" : function(data,type,full){
//                                    return '<button type="button" class="btn btn-default btn-xs tooltips" data-toggle="modal" href="#responsive2" data-placement="bottom" title="Edit" onclick="editAccTemplate(\''+full.id+'\');"><i class="fa fa-edit"></i></button>';
//					}
//				}
        ],
        "aaSorting": [3, "desc"],
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


//nikhil@123
function writeonly() {
    alert("write");
    $("input[name='first_name'], input[name='last_name'], input[name='pass'], input[name='designation'], input[name='mobile_no']\n\
        , input[name='gender']").removeAttr("readonly");
}
;
function readonly() {
    alert("read");
    $("input[name='first_name'], input[name='last_name'], input[name='pass'], input[name='designation'], input[name='mobile_no']\n\
        , input[name='gender']").attr('readonly', 'readonly');
}
;
// added by nikhil - complete new method
function createAccTemplate() {
    //Getting current date
    //siddhesh
// alert("createAccTemplate++++");
    //$('div#sample_editable_1').remove();
//   var myNode = document.getElementById("responsive2");
//while (myNode.removeChild()) {
//    alert("destroyed");
//    myNode.removeChild(myNode.firstChild);
//}
    var currentDate = new Date();
    var dd = currentDate.getDate();
    var mm = currentDate.getMonth() + 1; //January is 0!
    var yyyy = currentDate.getFullYear();
    if (dd < 10) {
        dd = '0' + dd;
    }

    if (mm < 10) {
        mm = '0' + mm;
    }

    currentDate = dd + '/' + mm + '/' + yyyy;
    mm = mm - 1;
    var dateOfDefaultExpiry = new Date(yyyy, mm, dd);
    dateOfDefaultExpiry = new Date(new Date(dateOfDefaultExpiry).setMonth(dateOfDefaultExpiry.getMonth() + 12));
    dd = dateOfDefaultExpiry.getDate() - 1;
    mm = dateOfDefaultExpiry.getMonth() + 1;
    yyyy = dateOfDefaultExpiry.getFullYear();
    if (dd < 10) {
        dd = '0' + dd;
    }

    if (mm < 10) {
        mm = '0' + mm;
    }
    dateOfDefaultExpiry = dd + '/' + mm + '/' + yyyy;
    //end 

    var accStatusTemplate = '';
    var accStatusTemplate = '<div id="id_div">';
    //   accStatusTemplate += '<div id="acc_cre" class="modal fade" tabindex="-1"aria-hidden="true">';
    accStatusTemplate += '<div class="modal-dialog"  id="id_accStatus" >';
    accStatusTemplate += '<div class="modal-content">';
    accStatusTemplate += '<div class="modal-header">';
    accStatusTemplate += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true" id="btn_close" onclick="resetCounter()"></button>		';
    accStatusTemplate += '<h4 class="modal-title">Create Account</h4>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="modal-body">';
    accStatusTemplate += '<div class="tabbable tabbable-custom">';
    accStatusTemplate += '<ul class="nav nav-tabs">';
    accStatusTemplate += '<li class="active">';
    accStatusTemplate += '<a href="#tab_1_0" data-toggle="tab">Admin</a>';
    accStatusTemplate += '</li>';
    accStatusTemplate += '<li class="">';
    accStatusTemplate += '<a href="#tab_1_1" data-toggle="tab">Plan</a>';
    accStatusTemplate += '</li>';
    accStatusTemplate += '<li class="">';
    accStatusTemplate += '<a href="#tab_1_2" data-toggle="tab">Account</a>';
    accStatusTemplate += '</li>';
    accStatusTemplate += '</ul>';
    accStatusTemplate += '<div class="tab-content">';
    accStatusTemplate += '<div class="tab-pane fade active in" id="tab_1_0">';
    accStatusTemplate += '<div class="form-body" id="act">';
    accStatusTemplate += '<form id="my_form" class="form-horizontal" role="form">';
    accStatusTemplate += '<div class="table-toolbar">';
    accStatusTemplate += '<div class="btn-group">';
    accStatusTemplate += '<button class="btn btn-info" id="sample_editable_1_new">';
    accStatusTemplate += '<i class="fa fa-plus"></i> Add New';
    accStatusTemplate += '</button>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    // accStatusTemplate += '<table class="table table-striped table-hover table-bordered" id="sample_editable_1" >';
    accStatusTemplate += '<table class="table table-striped table-hover table-bordered" id="sample_editable_1" style="font-size: small;font-family: "Open Sans",sans-serif;" >';
    accStatusTemplate += '<thead>';
    accStatusTemplate += '<tr >';
    accStatusTemplate += '<th>';
    // accStatusTemplate += 'Name';
    accStatusTemplate += 'Username';
    accStatusTemplate += '</th>';
    accStatusTemplate += '<th>';
    //  accStatusTemplate += 'Email';
    accStatusTemplate += 'Email';
    accStatusTemplate += '</th>';
    accStatusTemplate += '<th>';
//    accStatusTemplate += 'Password';
    accStatusTemplate += 'Password';
    accStatusTemplate += '</th>';
    accStatusTemplate += '<th>';
//    accStatusTemplate += 'Password';
    accStatusTemplate += 'Re-type Password';
    accStatusTemplate += '</th>';
    accStatusTemplate += '<th>';
    //   accStatusTemplate += 'Designation';
    accStatusTemplate += 'Designation';
    accStatusTemplate += '</th>';
    accStatusTemplate += '<th>';
    //   accStatusTemplate += 'Mobile';
    accStatusTemplate += 'Mobile No.';
    accStatusTemplate += '</th>';
    accStatusTemplate += '<th>';
    //  accStatusTemplate += 'Gender';
    accStatusTemplate += 'Gender';
    accStatusTemplate += '</th>';
    accStatusTemplate += '<th>';
    accStatusTemplate += '</th>';
    accStatusTemplate += '</tr>';
    accStatusTemplate += '</thead>';
    accStatusTemplate += '<tbody>';
    accStatusTemplate += '<tr>';
    accStatusTemplate += '<td >';
    accStatusTemplate += '<input name="" value="" size="10" class="form-control input-small" id="id_uname" >';
    //   accStatusTemplate += ' <input name="" value="" size="8"   readonly >';
    //  accStatusTemplate += 'Alex';
    accStatusTemplate += '</td>';
    accStatusTemplate += '<td>';
    accStatusTemplate += '<input name="" value="" size="10" class="form-control input-small" id="id_adm_email">';
//accStatusTemplate += 'Alex@ratanfire.com';
    accStatusTemplate += '</td>';
    accStatusTemplate += '<td>';
    // accStatusTemplate += '<input class="form-control input-small" type="password"  value="" size="8" id="id_pwd" readOnly> ';
    accStatusTemplate += ' <input type="password" name="" value="" size="8" class="form-control input-small" id="id_pwd0" >';
//    accStatusTemplate += '<input name="pass" value="********" size="8" id="id_pwd" readonly> ';
//accStatusTemplate += '********';
    accStatusTemplate += '</td>';
    accStatusTemplate += '<td>';
    accStatusTemplate += ' <input name="" type="password" value="" size="8" class="form-control input-small"  id="id_pwd10"  >';
//    accStatusTemplate += '<input name="pass" value="********" size="8" id="id_pwd" readonly> ';
//accStatusTemplate += '********';
    accStatusTemplate += '</td>';
    accStatusTemplate += '<td >';
    accStatusTemplate += ' <input name="" value="" size="8" class="form-control input-small" id="id_designation"  >';
//    accStatusTemplate += '<input name="designation" value="Manager - Support" size="10" id="id_designation" readonly>';
//accStatusTemplate += 'Manager - Support';
    accStatusTemplate += '</td>';
    accStatusTemplate += '<td >';
    accStatusTemplate += ' <input name="" value="" size="8"  class="form-control input-small" id="id_mobile_number" >';
//    accStatusTemplate += '<input name="mobile_no" value="9819245678" size="10" id="id_mobile_number" readonly>';
//accStatusTemplate += '9819245678';
    accStatusTemplate += '</td>';
    accStatusTemplate += '<td>';
    accStatusTemplate += ' <select id="id_gender" class="class_gender">';
    accStatusTemplate += '<option value="1">Male</option>';
    accStatusTemplate += '<option value="0">Female</option>';
    accStatusTemplate += '</select>  ';
    accStatusTemplate += '</td>';
//    accStatusTemplate += '<td>';
//    accStatusTemplate += '<button class=" " id="btnEdit" onclick="writeonly()" > edit </button> ' ;
////    accStatusTemplate += '<button class=" " id="btnCancel" onclick="readonly()" > cancel </button> ' ;
//    accStatusTemplate += '</td>';

    accStatusTemplate += '<td>';
//    accStatusTemplate += '<a class="edit" href="javascript:;">Edit</a>';
//    accStatusTemplate += '<a class="delete" href="javascript:;">Delete</a>';
    accStatusTemplate += '<a class="edit" href="">Save</a> <a class="cancel" href="">Cancel</a>'
    accStatusTemplate += ' <input type="hidden" id="id_counter" class="form-control input-small" value="0">';
    accStatusTemplate += '</td>';
    accStatusTemplate += '</tr>';
    accStatusTemplate += '</tbody>';
    accStatusTemplate += '</table>';
    accStatusTemplate += '</form>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="tab-pane fade" id="tab_1_1">';
    accStatusTemplate += '<div class="form-body" id="act">';
//    accStatusTemplate += '<form action="#" class="form-horizontal">';
    accStatusTemplate += '<form  class="form-horizontal" role="form">';
    accStatusTemplate += '<div class="row">';
    accStatusTemplate += '<div class="col-xs-6">	';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label required">Plan</label>';
    accStatusTemplate += '<div class="col-xs-8">';
    //id_plan
    accStatusTemplate += '<select name="region" id="id_plan" class="form-control select2"><option value="0">Select</option><option value="1" selected="selected">Free</option><option value="2">';
    accStatusTemplate += 'Premium';
    accStatusTemplate += '</option>';
    accStatusTemplate += '</select>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="col-xs-6">';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label required">User Limit</label>';
    accStatusTemplate += '<div class="col-xs-8">';
    accStatusTemplate += '<input type="text" class="form-control" placeholder="" value="25" id="id_userLimit">';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="row">';
    accStatusTemplate += '<div class="col-xs-6">';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label required">Start Date</label>';
    accStatusTemplate += '<div class="col-md-8">';
//    accStatusTemplate += '<div class="input-group date date-picker" data-date="20 May 2017" data-date-format="dd MM yyyy" data-date-viewmode="years">';
    accStatusTemplate += '<div class="input-group date date-picker" data-date-format="dd/mm/yyyy" data-date-viewmode="years">';
//    accStatusTemplate += '<input type="text" class="form-control" id="start_date" readonly>';
    accStatusTemplate += '<input type="text" class="form-control" id="start_date" value=' + currentDate + ' readonly>';
    accStatusTemplate += '<span class="input-group-btn">';
    accStatusTemplate += '<button class="btn btn-info" type="button"><i class="fa fa-calendar"></i></button>';
    accStatusTemplate += '</span>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="col-xs-6">';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label required">Expiry Date</label>';
    accStatusTemplate += '<div class="col-md-8">';
    accStatusTemplate += '<div class="input-group date date-picker" data-date-format="dd/mm/yyyy" data-date-viewmode="years">';
    accStatusTemplate += '<input type="text" class="form-control" id="expiry_date" value=' + dateOfDefaultExpiry + ' readonly>'; // modified by manohar
    accStatusTemplate += '<span class="input-group-btn">';
    accStatusTemplate += '<button class="btn btn-info" type="button"><i class="fa fa-calendar"></i></button>';
    accStatusTemplate += '</span>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>	';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</form>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="tab-pane fade  " id="tab_1_2">';
    accStatusTemplate += '<div class="form-body" id="act">';
//    accStatusTemplate += '<form action="#" class="form-horizontal">';
    accStatusTemplate += '<form class="form-horizontal" role="form">';
    accStatusTemplate += '<div class="row">';
    accStatusTemplate += '<div class="col-xs-6">';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label required">Status</label>';
    accStatusTemplate += '<div class="col-xs-8">';
    accStatusTemplate += '<div class="radio-list">';
    accStatusTemplate += '<label class="radio-inline">';
    accStatusTemplate += '<input type="radio" name="optionsRadios" id="optionsRadios4" value="1" checked> Active </label>';
    accStatusTemplate += '<label class="radio-inline">';
    accStatusTemplate += '<input type="radio" name="optionsRadios" id="optionsRadios5" value="0"> Inactive </label>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    //by nikhil
            accStatusTemplate += '<div class="row">';
    accStatusTemplate += '<div class="col-xs-6">';
      accStatusTemplate += '<div class="form-group">';
                            accStatusTemplate += '<label class="col-md-4 control-label">Allow Offline</label>';
                            accStatusTemplate += '<div class="col-md-8">';
                            accStatusTemplate += '<div class="checkbox-list">';
//                            if (offlineEnable == 1) {
                                accStatusTemplate += '<input type="checkbox" id="id_allowoffline_edit" checked>';
//                            } else {
//                                accStatusTemplate += '<input type="checkbox" id="id_allowoffline_edit">';
//                            }
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
        accStatusTemplate += '</div>';
                            
    accStatusTemplate += '</div>';
    //ended by nikhil
    accStatusTemplate += '<div class="row">';
    accStatusTemplate += '<div class="col-xs-6">';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label required">Name</label>';
    accStatusTemplate += '<div class="col-xs-8">';
    accStatusTemplate += '<input type="text" class="form-control" placeholder="" value="" id="id_companyName">';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="col-xs-6">';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label required">Phone</label>';
    accStatusTemplate += '<div class="col-xs-8">';
    accStatusTemplate += '<input type="Phone" class="form-control" placeholder="" value="" id="id_comPnNo1">';
    accStatusTemplate += '</div>	';
    accStatusTemplate += '</div>	';
    accStatusTemplate += '</div>	';
    accStatusTemplate += '</div>	';
    accStatusTemplate += '<div class="row">	';
    accStatusTemplate += '<div class="col-xs-6">	';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label required">Address</label>';
    accStatusTemplate += '<div class="col-xs-8">';
    accStatusTemplate += '<textarea class="form-control" rows="3" placeholder="" id="id_address1" ></textarea>';
    accStatusTemplate += '</div>	';
    accStatusTemplate += '</div>	';
    accStatusTemplate += '</div>	';
    accStatusTemplate += '<div class="col-xs-6">	';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label required">City</label>';
    accStatusTemplate += '<div class="col-xs-8">';
    accStatusTemplate += '<input type="text" class="form-control" value="" id="id_city">';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="row">	';
    accStatusTemplate += '<div class="col-xs-6">';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label required">State</label>';
    accStatusTemplate += '<div class="col-xs-8">';
    accStatusTemplate += '<input type="Phone1" class="form-control" placeholder="" value="" id="id_state">';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="col-xs-6">';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label required">Country</label>';
    accStatusTemplate += '<div class="col-xs-8">';
    accStatusTemplate += '<select name="" id="select2_sample4" class="form-control select2">';
    accStatusTemplate += '<option value="select">--Select Country--</option>';
    accStatusTemplate += '<option value="Afghanistan">Afghanistan</option>';
    accStatusTemplate += '<option value="Albania">Albania</option>';
    accStatusTemplate += '<option value="Algeria">Algeria</option>';
    accStatusTemplate += '<option value="American Samoa">American Samoa</option>';
    accStatusTemplate += '<option value="Andorra">Andorra</option>';
    accStatusTemplate += '<option value="Angola">Angola</option>';
    accStatusTemplate += '<option value="Anguilla">Anguilla</option>';
    accStatusTemplate += '<option value="Antarctica">Antarctica</option>';
    accStatusTemplate += '<option value="Argentina">Argentina</option>';
    accStatusTemplate += '<option value="Armenia">Armenia</option>';
    accStatusTemplate += '<option value="Aruba">Aruba</option>';
    accStatusTemplate += '<option value="Australia">Australia</option>';
    accStatusTemplate += '<option value="Austria">Austria</option>';
    accStatusTemplate += '<option value="Azerbaijan">Azerbaijan</option>';
    accStatusTemplate += '<option value="Bahamas">Bahamas</option>';
    accStatusTemplate += '<option value="Bahrain">Bahrain</option>';
    accStatusTemplate += '<option value="Bangladesh">Bangladesh</option>';
    accStatusTemplate += '<option value="Barbados">Barbados</option>';
    accStatusTemplate += '<option value="Belarus">Belarus</option>';
    accStatusTemplate += '<option value="Belgium">Belgium</option>';
    accStatusTemplate += '<option value="Belize">Belize</option>';
    accStatusTemplate += '<option value="Benin">Benin</option>';
    accStatusTemplate += '<option value="Bermuda">Bermuda</option>';
    accStatusTemplate += '<option value="Bhutan">Bhutan</option>';
    accStatusTemplate += '<option value="Bolivia">Bolivia</option>';
    accStatusTemplate += '<option value="Bosnia and Herzegowina">Bosnia and Herzegowina</option>';
    accStatusTemplate += '<option value="Bouvet Island">Bouvet Island</option>';
    accStatusTemplate += '<option value="Brazil">Brazil</option>';
    accStatusTemplate += '<option value="British Indian Ocean Territory">British Indian Ocean Territory</option>';
    accStatusTemplate += '<option value="Brunei Darussalam">Brunei Darussalam</option>';
    accStatusTemplate += '<option value="Bulgaria">Bulgaria</option>';
    accStatusTemplate += '<option value="Burkina Faso">Burkina Faso</option>';
    accStatusTemplate += '<option value="Burundi">Burundi</option>';
    accStatusTemplate += '<option value="Cambodia">Cambodia</option>';
    accStatusTemplate += '<option value="Cameroon">Cameroon</option>';
    accStatusTemplate += '<option value="Canada">Canada</option>';
    accStatusTemplate += '<option value="Cape Verde">Cape Verde</option>';
    accStatusTemplate += '<option value="Cayman Islands">Cayman Islands</option>';
    accStatusTemplate += '<option value="Central African Republic">Central African Republic</option>';
    accStatusTemplate += '<option value="Chad">Chad</option>';
    accStatusTemplate += '<option value="Chile">Chile</option>';
    accStatusTemplate += '<option value="China">China</option>';
    accStatusTemplate += '<option value="Christmas Island">Christmas Island</option>';
    accStatusTemplate += '<option value="Cocos (Keeling) Islands">Cocos (Keeling) Islands</option>';
    accStatusTemplate += '<option value="Colombia">Colombia</option>';
    accStatusTemplate += '<option value="Comoros">Comoros</option>';
    accStatusTemplate += '<option value="Congo">Congo</option>';
    accStatusTemplate += '<option value="Congo, the Democratic Republic of the">Congo, the Democratic Republic of the</option>';
    accStatusTemplate += '<option value="Cook Islands">Cook Islands</option>';
    accStatusTemplate += '<option value="Costa Rica">Costa Rica</option>';
    accStatusTemplate += '<option value="Cote dIvoire">Cote dIvoire</option>';
    accStatusTemplate += '<option value="Croatia (Hrvatska)">Croatia (Hrvatska)</option>';
    accStatusTemplate += '<option value="Cuba">Cuba</option>';
    accStatusTemplate += '<option value="Cyprus">Cyprus</option>';
    accStatusTemplate += '<option value="Czech Republic">Czech Republic</option>';
    accStatusTemplate += '<option value="Denmark">Denmark</option>';
    accStatusTemplate += '<option value="Djibouti">Djibouti</option>';
    accStatusTemplate += '<option value="Dominica">Dominica</option>';
    accStatusTemplate += '<option value="Dominican Republic">Dominican Republic</option>';
    accStatusTemplate += '<option value="Ecuador">Ecuador</option>';
    accStatusTemplate += '<option value="Egypt">Egypt</option>';
    accStatusTemplate += '<option value="El Salvador">El Salvador</option>';
    accStatusTemplate += '<option value="Equatorial Guinea">Equatorial Guinea</option>';
    accStatusTemplate += '<option value="Eritrea">Eritrea</option>';
    accStatusTemplate += '<option value="Estonia">Estonia</option>';
    accStatusTemplate += '<option value="Ethiopia">Ethiopia</option>';
    accStatusTemplate += '<option value="Falkland Islands (Malvinas)">Falkland Islands (Malvinas)</option>';
    accStatusTemplate += '<option value="Faroe Islands">Faroe Islands</option>';
    accStatusTemplate += '<option value="Fiji">Fiji</option>';
    accStatusTemplate += '<option value="Finland">Finland</option>';
    accStatusTemplate += '<option value="France">France</option>';
    accStatusTemplate += '<option value="French Guiana">French Guiana</option>';
    accStatusTemplate += '<option value="French Polynesia">French Polynesia</option>';
    accStatusTemplate += '<option value="French Southern Territories">French Southern Territories</option>';
    accStatusTemplate += '<option value="Gabon">Gabon</option>';
    accStatusTemplate += '<option value="Gambia">Gambia</option>';
    accStatusTemplate += ' <option value="Georgia">Georgia</option>';
    accStatusTemplate += '<option value="Germany">Germany</option>';
    accStatusTemplate += '<option value="Ghana">Ghana</option>';
    accStatusTemplate += '<option value="Gibraltar">Gibraltar</option>';
    accStatusTemplate += '<option value="Greece">Greece</option>';
    accStatusTemplate += '<option value="Greenland">Greenland</option>';
    accStatusTemplate += '<option value="Grenada">Grenada</option>';
    accStatusTemplate += '<option value="Guadeloupe">Guadeloupe</option>';
    accStatusTemplate += '<option value="Guam">Guam</option>';
    accStatusTemplate += '<option value="Guatemala">Guatemala</option>';
    accStatusTemplate += '<option value="Guinea">Guinea</option>';
    accStatusTemplate += '<option value="Guinea-Bissau">Guinea-Bissau</option>';
    accStatusTemplate += '<option value="Guyana">Guyana</option>';
    accStatusTemplate += '<option value="Haiti">Haiti</option>';
    accStatusTemplate += '<option value="Heard and Mc Donald Islands">Heard and Mc Donald Islands</option>';
    accStatusTemplate += '<option value="Holy See (Vatican City State)">Holy See (Vatican City State)</option>';
    accStatusTemplate += '<option value="Honduras">Honduras</option>';
    accStatusTemplate += '<option value="Hong Kong">Hong Kong</option>';
    accStatusTemplate += '<option value="Hungary">Hungary</option>';
    accStatusTemplate += '<option value="Iceland">Iceland</option>';
    accStatusTemplate += '<option value="India">India</option>';
    accStatusTemplate += '<option value="Indonesia">Indonesia</option>';
    accStatusTemplate += '<option value="Iran (Islamic Republic of)">Iran (Islamic Republic of)</option>';
    accStatusTemplate += '<option value="Iraq">Iraq</option>';
    accStatusTemplate += '<option value="Ireland">Ireland</option>';
    accStatusTemplate += '<option value="Israel">Israel</option>';
    accStatusTemplate += '<option value="Italy">Italy</option>';
    accStatusTemplate += '<option value="Jamaica">Jamaica</option>';
    accStatusTemplate += '<option value="Japan">Japan</option>';
    accStatusTemplate += '<option value="Jordan">Jordan</option>';
    accStatusTemplate += '<option value="Kazakhstan">Kazakhstan</option>';
    accStatusTemplate += '<option value="Kenya">Kenya</option>';
    accStatusTemplate += '<option value="Kiribati">Kiribati</option>';
    accStatusTemplate += '<option value="Korea, Democratic Peoples Republic of">Korea, Democratic Peoples 	Republic of</option>';
    accStatusTemplate += '<option value="Korea, Republic of">Korea, Republic of</option>';
    accStatusTemplate += '<option value="Kuwait">Kuwait</option>';
    accStatusTemplate += '<option value="Kyrgyzstan">Kyrgyzstan</option>';
    accStatusTemplate += '<option value="Lao Peoples Democratic Republic">Lao Peoples Democratic Republic</option>';
    accStatusTemplate += '<option value="Latvia">Latvia</option>';
    accStatusTemplate += '<option value="Lebanon">Lebanon</option>';
    accStatusTemplate += '<option value="Lesotho">Lesotho</option>';
    accStatusTemplate += '<option value="Liberia">Liberia</option>';
    accStatusTemplate += '<option value="Libyan Arab Jamahiriya<">Libyan Arab Jamahiriya</option>';
    accStatusTemplate += '<option value="Liechtenstein">Liechtenstein</option>';
    accStatusTemplate += '<option value="Lithuania">Lithuania</option>';
    accStatusTemplate += '<option value="Luxembourg">Luxembourg</option>';
    accStatusTemplate += '<option value="MO">Macau</option>';
    accStatusTemplate += '<option value="Macedonia, The Former Yugoslav Republic of">Macedonia, The Former Yugoslav Republic of</option>';
    accStatusTemplate += '<option value="Madagascar">Madagascar</option>';
    accStatusTemplate += '<option value="Malawi">Malawi</option>';
    accStatusTemplate += '<option value="Malaysia">Malaysia</option>';
    accStatusTemplate += '<option value="Maldives">Maldives</option>';
    accStatusTemplate += '<option value="Mali">Mali</option>';
    accStatusTemplate += '<option value="Malta">Malta</option>';
    accStatusTemplate += '<option value="Marshall Islands">Marshall Islands</option>';
    accStatusTemplate += '<option value="Martinique">Martinique</option>';
    accStatusTemplate += '<option value="Mauritania">Mauritania</option>';
    accStatusTemplate += '<option value="Mauritius">Mauritius</option>';
    accStatusTemplate += '<option value="Mayotte">Mayotte</option>';
    accStatusTemplate += '<option value="Mexico">Mexico</option>';
    accStatusTemplate += '<option value="Micronesia, Federated States of">Micronesia, Federated States of</option>';
    accStatusTemplate += '<option value=">Moldova, Republic of">Moldova, Republic of</option>';
    accStatusTemplate += '<option value="Monaco">Monaco</option>';
    accStatusTemplate += '<option value="Mongolia">Mongolia</option>';
    accStatusTemplate += '<option value="Montserrat">Montserrat</option>';
    accStatusTemplate += '<option value="Morocco">Morocco</option>';
    accStatusTemplate += '<option value="Mozambique">Mozambique</option>';
    accStatusTemplate += '<option value="Myanmar">Myanmar</option>';
    accStatusTemplate += '<option value="Namibia">Namibia</option>';
    accStatusTemplate += '<option value="Nauru">Nauru</option>';
    accStatusTemplate += '<option value="Nepal">Nepal</option>';
    accStatusTemplate += '<option value="Netherlands">Netherlands</option>';
    accStatusTemplate += '<option value="New Caledonia">New Caledonia</option>';
    accStatusTemplate += '<option value="New Zealand">New Zealand</option>';
    accStatusTemplate += '<option value="Nicaragua">Nicaragua</option>';
    accStatusTemplate += '<option value="Niger">Niger</option>';
    accStatusTemplate += '<option value="Nigeria">Nigeria</option>';
    accStatusTemplate += '<option value="Niue">Niue</option>';
    accStatusTemplate += '<option value="Norfolk Island">Norfolk Island</option>';
    accStatusTemplate += '<option value="Northern Mariana Islands">Northern Mariana Islands</option>';
    accStatusTemplate += '<option value="Norway">Norway</option>';
    accStatusTemplate += '<option value="Oman">Oman</option>';
    accStatusTemplate += '<option value="Pakistan">Pakistan</option>';
    accStatusTemplate += '<option value="Palau">Palau</option>';
    accStatusTemplate += '<option value="Palestinian Territory">Palestinian Territory</option>';//Palestine
    accStatusTemplate += '<option value="Panama">Panama</option>';
    accStatusTemplate += '<option value="Papua New Guinea">Papua New Guinea</option>';
    accStatusTemplate += '<option value="Paraguay">Paraguay</option>';
    accStatusTemplate += '<option value="Peru">Peru</option>';
    accStatusTemplate += '<option value="Philippines">Philippines</option>';
    accStatusTemplate += '<option value="Pitcairn">Pitcairn</option>';
    accStatusTemplate += '<option value="Poland">Poland</option>';
    accStatusTemplate += '<option value="Portugal">Portugal</option>';
    accStatusTemplate += '<option value="Puerto Rico">Puerto Rico</option>';
    accStatusTemplate += '<option value="Qatar">Qatar</option>';
    accStatusTemplate += '<option value="Reunion">Reunion</option>';
    accStatusTemplate += '<option value="Romania">Romania</option>';
    accStatusTemplate += '<option value="Russian Federation">Russian Federation</option>';
    accStatusTemplate += '<option value="Rwanda">Rwanda</option>';
    accStatusTemplate += '<option value="Saint Kitts and Nevis">Saint Kitts and Nevis</option>';
    accStatusTemplate += '<option value="Saint LUCIA">Saint LUCIA</option>';
    accStatusTemplate += '<option value="Saint Vincent and the Grenadines">Saint Vincent and the Grenadines</option>';
    accStatusTemplate += '<option value="Samoa">Samoa</option>';
    accStatusTemplate += '<option value="San Marino">San Marino</option>';
    accStatusTemplate += '<option value="Sao Tome and Principe">Sao Tome and Principe</option>';
    accStatusTemplate += '<option value="Saudi Arabia">Saudi Arabia</option>';
    accStatusTemplate += '<option value="Senegal">Senegal</option>';
    accStatusTemplate += '<option value="Seychelles">Seychelles</option>';
    accStatusTemplate += '<option value="Sierra Leone">Sierra Leone</option>';
    accStatusTemplate += '<option value="Singapore">Singapore</option>';
    accStatusTemplate += '<option value="Slovakia (Slovak Republic)">Slovakia (Slovak Republic)</option>';
    accStatusTemplate += '<option value="Slovenia">Slovenia</option>';
    accStatusTemplate += '<option value="Solomon Islands">Solomon Islands</option>';
    accStatusTemplate += '<option value="Somalia">Somalia</option>';
    accStatusTemplate += '<option value="South Africa">South Africa</option>';
    accStatusTemplate += '<option value="South Georgia and the South Sandwich Islands">South Georgia and the South Sandwich Islands</option>';
    accStatusTemplate += '<option value="Spain">Spain</option>';
    accStatusTemplate += '<option value="Sri Lanka">Sri Lanka</option>';
    accStatusTemplate += '<option value="St. Helena">St. Helena</option>';
    accStatusTemplate += '<option value="St. Pierre and Miquelon">St. Pierre and Miquelon</option>';
    accStatusTemplate += '<option value="Sudan">Sudan</option>';
    accStatusTemplate += '<option value="Suriname">Suriname</option>';
    accStatusTemplate += '<option value="Svalbard and Jan Mayen Islands">Svalbard and Jan Mayen Islands</option>';
    accStatusTemplate += '<option value="Swaziland">Swaziland</option>';
    accStatusTemplate += '<option value="Sweden">Sweden</option>';
    accStatusTemplate += '<option value="Switzerland">Switzerland</option>';
    accStatusTemplate += '<option value="Syrian Arab Republic">Syrian Arab Republic</option>';
    accStatusTemplate += '<option value="Taiwan, Province of China">Taiwan, Province of China</option>';
    accStatusTemplate += '<option value="TJ">Tajikistan</option>';
    accStatusTemplate += '<option value="Tajikistan">Tajikistan</option>';
    accStatusTemplate += '<option value="Tanzania, United Republic of">Tanzania, United Republic of</option>';
    accStatusTemplate += '<option value="Thailand">Thailand</option>';
    accStatusTemplate += '<option value="Togo">Togo</option>';
    accStatusTemplate += '<option value="Tokelau">Tokelau</option>';
    accStatusTemplate += '<option value="Tonga">Tonga</option>';
    accStatusTemplate += '<option value="Trinidad and Tobago">Trinidad and Tobago</option>';
    accStatusTemplate += '<option value="Tunisia">Tunisia</option>';
    accStatusTemplate += '<option value="Turkey">Turkey</option>';
    accStatusTemplate += '<option value="Turkmenistan">Turkmenistan</option>';
    accStatusTemplate += '<option value="Turks and Caicos Islands">Turks and Caicos Islands</option>';
    accStatusTemplate += '<option value="Tuvalu">Tuvalu</option>'; //
    accStatusTemplate += '<option value="Uganda">Uganda</option>';
    accStatusTemplate += '<option value="Ukraine">Ukraine</option>';
    accStatusTemplate += '<option value="United Arab Emirates">United Arab Emirates</option>';
    accStatusTemplate += '<option value="United Kingdom">United Kingdom</option>';
    accStatusTemplate += '<option value="United States">United States</option>';
    accStatusTemplate += '<option value="United States Minor Outlying Islands">United States Minor Outlying Islands</option>';
    accStatusTemplate += '<option value="Uruguay">Uruguay</option>';
    accStatusTemplate += '<option value="Uzbekistan">Uzbekistan</option>';
    accStatusTemplate += '<option value="Vanuatu">Vanuatu</option>';
    accStatusTemplate += '<option value="Venezuela">Venezuela</option>';
    accStatusTemplate += '<option value="Viet Nam">Viet Nam</option>';
    accStatusTemplate += '<option value="Virgin Islands (British)">Virgin Islands (British)</option>';
    accStatusTemplate += '<option value="Virgin Islands (U.S.)">Virgin Islands (U.S.)</option>';
    accStatusTemplate += '<option value="Wallis and Futuna Islands">Wallis and Futuna Islands</option>';
    accStatusTemplate += '<option value="Western Sahara">Western Sahara</option>';
    accStatusTemplate += '<option value="Yemen">Yemen</option>';
    accStatusTemplate += '<option value="Zambia">Zambia</option>';
    accStatusTemplate += '<option value="Zimbabwe">Zimbabwe</option>';

    //by nikhil
   // accStatusTemplate += '<option value="1">Select</option>';
//    accStatusTemplate += '<option value="Afghanistan">Afghanistan</option>';
//    accStatusTemplate += '<option value="Albania">Albania</option>';
//    accStatusTemplate += '<option value="Algeria">Algeria</option>';
//    accStatusTemplate += '<option value="AmericanSamoa">AmericanSamoa</option>';
//    accStatusTemplate += '<option value="Andorra">Andorra</option>';
//    accStatusTemplate += '<option value="Angola">Angola</option>';
//    accStatusTemplate += '<option value="Anguilla">Anguilla</option>';
//    accStatusTemplate += '<option value="Antarctica">Antarctica</option>';
//    accStatusTemplate += '<option value="Argentina">Argentina</option>';
//    accStatusTemplate += '<option value="Armenia">Armenia</option>';
//    accStatusTemplate += '<option value="Aruba">Aruba</option>';
//    accStatusTemplate += '<option value="Australia">Australia</option>';
//    accStatusTemplate += '<option value="Austria">Austria</option>';
//    accStatusTemplate += '<option value="Azerbaijan">Azerbaijan</option>';
//    accStatusTemplate += '<option value="Bahamas">Bahamas</option>';
//    accStatusTemplate += '<option value="Bahrain">Bahrain</option>';
//    accStatusTemplate += '<option value="Bangladesh">Bangladesh</option>';
//    accStatusTemplate += '<option value="Barbados">Barbados</option>';
//    accStatusTemplate += '<option value="Belarus">Belarus</option>';
//    accStatusTemplate += '<option value="Belgium">Belgium</option>';
//    accStatusTemplate += '<option value="Belize">Belize</option>';
//    accStatusTemplate += '<option value="Benin">Benin</option>';
//    accStatusTemplate += '<option value="Bermuda">Bermuda</option>';
//    accStatusTemplate += '<option value="Bhutan">Bhutan</option>';
//    accStatusTemplate += '<option value="Bolivia">Bolivia</option>';
//    accStatusTemplate += '<option value="BosniaandHerzegowina">BosniaandHerzegowina</option>';
//    accStatusTemplate += '<option value="Botswana">Botswana</option>';
//    accStatusTemplate += '<option value="BouvetIsland">BouvetIsland</option>';
//    accStatusTemplate += '<option value="Brazil">Brazil</option>';
//    accStatusTemplate += '<option value="BritishIndianOceanTerritory">BritishIndianOceanTerritory</option>';
//    accStatusTemplate += '<option value="BruneiDarussalam">BruneiDarussalam</option>';
//    accStatusTemplate += '<option value="Bulgaria">Bulgaria</option>';
//    accStatusTemplate += '<option value="BurkinaFaso">BurkinaFaso</option>';
//    accStatusTemplate += '<option value="Burundi">Burundi</option>';
//    accStatusTemplate += '<option value="Cambodia">Cambodia</option>';
//    accStatusTemplate += '<option value="Cameroon">Cameroon</option>';
//    accStatusTemplate += '<option value="Canada">Canada</option>';
//    accStatusTemplate += '<option value="CapeVerde">CapeVerde</option>';
//    accStatusTemplate += '<option value="CaymanIslands">CaymanIslands</option>';
//    accStatusTemplate += '<option value="CentralAfricanRepublic">CentralAfricanRepublic</option>';
//    accStatusTemplate += '<option value="Chad">Chad</option>';
//    accStatusTemplate += '<option value="Chile">Chile</option>';
//    accStatusTemplate += '<option value="China">China</option>';
//    accStatusTemplate += '<option value="ChristmasIsland">ChristmasIsland</option>';
//    accStatusTemplate += '<option value="Cocos(Keeling)Islands">Cocos(Keeling)Islands</option>';
//    accStatusTemplate += '<option value="Colombia">Colombia</option>';
//    accStatusTemplate += '<option value="Comoros">Comoros</option>';
//    accStatusTemplate += '<option value="Congo">Congo</option>';
//    accStatusTemplate += '<option value="Congo,theDemocraticRepublicofthe">Congo,theDemocraticRepublicofthe</option>';
//    accStatusTemplate += '<option value="CookIslands">CookIslands</option>';
//    accStatusTemplate += '<option value="CostaRica">CostaRica</option>';
//    accStatusTemplate += '<option value="CotedIvoire">CotedIvoire</option>';
//    accStatusTemplate += '<option value="Croatia(Hrvatska)">Croatia(Hrvatska)</option>';
//    accStatusTemplate += '<option value="Cuba">Cuba</option>';
//    accStatusTemplate += '<option value="Cyprus">Cyprus</option>';
//    accStatusTemplate += '<option value="CzechRepublic">CzechRepublic</option>';
//    accStatusTemplate += '<option value="Denmark">Denmark</option>';
//    accStatusTemplate += '<option value="Djibouti">Djibouti</option>';
//    accStatusTemplate += '<option value="Dominica">Dominica</option>';
//    accStatusTemplate += '<option value="DominicanRepublic">DominicanRepublic</option>';
//    accStatusTemplate += '<option value="Ecuador">Ecuador</option>';
//    accStatusTemplate += '<option value="Egypt">Egypt</option>';
//    accStatusTemplate += '<option value="ElSalvador">ElSalvador</option>';
//    accStatusTemplate += '<option value="EquatorialGuinea">EquatorialGuinea</option>';
//    accStatusTemplate += '<option value="Eritrea">Eritrea</option>';
//    accStatusTemplate += '<option value="Estonia">Estonia</option>';
//    accStatusTemplate += '<option value="Ethiopia">Ethiopia</option>';
//    accStatusTemplate += '<option value="FalklandIslands(Malvinas)">FalklandIslands(Malvinas)</option>';
//    accStatusTemplate += '<option value="FaroeIslands">FaroeIslands</option>';
//    accStatusTemplate += '<option value="Fiji">Fiji</option>';
//    accStatusTemplate += '<option value="Finland">Finland</option>';
//    accStatusTemplate += '<option value="France">France</option>';
//    accStatusTemplate += '<option value="FrenchGuiana">FrenchGuiana</option>';
//    accStatusTemplate += '<option value="PF">FrenchPolynesia</option>';
//    accStatusTemplate += '<option value="TF">FrenchSouthernTerritories</option>';
//    accStatusTemplate += '<option value="Gabon">Gabon</option>';
//    accStatusTemplate += '<option value="Gambia">Gambia</option>';
//    accStatusTemplate += '<option value="Georgia">Georgia</option>';
//    accStatusTemplate += '<option value="Germany">Germany</option>';
//    accStatusTemplate += '<option value="Ghana">Ghana</option>';
//    accStatusTemplate += '<option value="Gibraltar">Gibraltar</option>';
//    accStatusTemplate += '<option value="Greece">Greece</option>';
//    accStatusTemplate += '<option value="Greenland">Greenland</option>';
//    accStatusTemplate += '<option value="Grenada">Grenada</option>';
//    accStatusTemplate += '<option value="Guadeloupe">Guadeloupe</option>';
//    accStatusTemplate += '<option value="Guam">Guam</option>';
//    accStatusTemplate += '<option value="Guatemala">Guatemala</option>';
//    accStatusTemplate += '<option value="Guinea">Guinea</option>';
//    accStatusTemplate += '<option value="Guinea-Bissau">Guinea-Bissau</option>';
//    accStatusTemplate += '<option value="Guyan">Guyana</option>';
//    accStatusTemplate += '<option value="Haiti">Haiti</option>';
//    accStatusTemplate += '<option value="HeardandMcDonaldIslands">HeardandMcDonaldIslands</option>';
//    accStatusTemplate += '<option value="HolySee(VaticanCityState)">HolySee(VaticanCityState)</option>';
//    accStatusTemplate += '<option value="Honduras">Honduras</option>';
//    accStatusTemplate += '<option value="HongKong">HongKong</option>';
//    accStatusTemplate += '<option value="Hungary">Hungary</option>';
//    accStatusTemplate += '<option value="Iceland">Iceland</option>';
//    accStatusTemplate += '<option value="India">India</option>';
//    accStatusTemplate += '<option value="Indonesia">Indonesia</option>';
//    accStatusTemplate += '<option value="Iran(IslamicRepublicof)">Iran(IslamicRepublicof)</option>';
//    accStatusTemplate += '<option value="Iraq">Iraq</option>';
//    accStatusTemplate += '<option value="Ireland">Ireland</option>';
//    accStatusTemplate += '<option value="Israel">Israel</option>';
//    accStatusTemplate += '<option value="Italy">Italy</option>';
//    accStatusTemplate += '<option value="Jamaica">Jamaica</option>';
//    accStatusTemplate += '<option value="Japan">Japan</option>';
//    accStatusTemplate += '<option value="Jordan">Jordan</option>';
//    accStatusTemplate += '<option value="Kazakhstan">Kazakhstan</option>';
//    accStatusTemplate += '<option value="Kenya">Kenya</option>';
//    accStatusTemplate += '<option value="Kiribati">Kiribati</option>';
//    accStatusTemplate += '<option value="Korea,DemocraticPeoplesRepublicof">Korea,DemocraticPeoplesRepublicof</option>';
//    accStatusTemplate += '<option value="Korea,Republicof">Korea,Republicof</option>';
//    accStatusTemplate += '<option value="Kuwait">Kuwait</option>';
//    accStatusTemplate += '<option value="Kyrgyzstan">Kyrgyzstan</option>';
//    accStatusTemplate += '<option value="LaoPeopleDemocraticRepublic">LaoPeopleDemocraticRepublic</option>';
//    accStatusTemplate += '<option value="Latvia">Latvia</option>';
//    accStatusTemplate += '<option value="Lebanon">Lebanon</option>';
//    accStatusTemplate += '<option value="Lesotho">Lesotho</option>';
//    accStatusTemplate += '<option value="Liberia">Liberia</option>';
//    accStatusTemplate += '<option value="LibyanArabJamahiriya">LibyanArabJamahiriya</option>';
//    accStatusTemplate += '<option value="Liechtenstein">Liechtenstein</option>';
//    accStatusTemplate += '<option value="Lithuania">Lithuania</option>';
//    accStatusTemplate += '<option value="Luxembourg">Luxembourg</option>';
//    accStatusTemplate += '<option value="Macau">Macau</option>';
//    accStatusTemplate += '<option value="Macedonia,TheFormerYugoslavRepublicof">Macedonia,TheFormerYugoslavRepublicof</option>';
//    accStatusTemplate += '<option value="Madagascar">Madagascar</option>';
//    accStatusTemplate += '<option value="Malawi">Malawi</option>';
//    accStatusTemplate += '<option value="Malaysia">Malaysia</option>';
//    accStatusTemplate += '<option value="Maldives">Maldives</option>';
//    accStatusTemplate += '<option value="Mali">Mali</option>';
//    accStatusTemplate += '<option value="Malta">Malta</option>';
//    accStatusTemplate += '<option value="MarshallIslands">MarshallIslands</option>';
//    accStatusTemplate += '<option value="Martinique">Martinique</option>';
//    accStatusTemplate += '<option value="Mauritania">Mauritania</option>';
//    accStatusTemplate += '<option value="Mauritius">Mauritius</option>';
//    accStatusTemplate += '<option value="Mayotte">Mayotte</option>';
//    accStatusTemplate += '<option value="Mexico">Mexico</option>';
//    accStatusTemplate += '<option value="Micronesia,FederatedStatesof">Micronesia,FederatedStatesof</option>';
//    accStatusTemplate += '<option value="Moldova,Republicof">Moldova,Republicof</option>';
//    accStatusTemplate += '<option value="Monaco">Monaco</option>';
//    accStatusTemplate += '<option value="Mongolia">Mongolia</option>';
//    accStatusTemplate += '<option value="Montserrat">Montserrat</option>';
//    accStatusTemplate += '<option value="Morocco">Morocco</option>';
//    accStatusTemplate += '<option value="Mozambique">Mozambique</option>';
//    accStatusTemplate += '<option value="Myanmar">Myanmar</option>';
//    accStatusTemplate += '<option value="Namibia">Namibia</option>';
//    accStatusTemplate += '<option value="Nauru">Nauru</option>';
//    accStatusTemplate += '<option value="Nepal">Nepal</option>';
//    accStatusTemplate += '<option value="Netherlands">Netherlands</option>';
//    accStatusTemplate += '<option value="NetherlandsAntilles">NetherlandsAntilles</option>';
//    accStatusTemplate += '<option value="NewCaledonia">NewCaledonia</option>';
//    accStatusTemplate += '<option value="NewZealand">NewZealand</option>';
//    accStatusTemplate += '<option value="Nicaragua">Nicaragua</option>';
//    accStatusTemplate += '<option value="Niger">Niger</option>';
//    accStatusTemplate += '<option value="Nigeria">Nigeria</option>';
//    accStatusTemplate += '<option value="Niue">Niue</option>';
//    accStatusTemplate += '<option value="NorfolkIsland">NorfolkIsland</option>';
//    accStatusTemplate += '<option value="NorthernMarianaIslands">NorthernMarianaIslands</option>';
//    accStatusTemplate += '<option value="Norway">Norway</option>';
//    accStatusTemplate += '<option value="Oman">Oman</option>';
//    accStatusTemplate += '<option value="Pakistan">Pakistan</option>';
//    accStatusTemplate += '<option value="Palau">Palau</option>';
//    accStatusTemplate += '<option value="Panama">Panama</option>';
//    accStatusTemplate += '<option value="PapuaNewGuinea">PapuaNewGuinea</option>';
//    accStatusTemplate += '<option value="Paraguay">Paraguay</option>';
//    accStatusTemplate += '<option value="Peru">Peru</option>';
//    accStatusTemplate += '<option value="Philippines">Philippines</option>';
//    accStatusTemplate += '<option value="Pitcairn">Pitcairn</option>';
//    accStatusTemplate += '<option value="Poland">Poland</option>';
//    accStatusTemplate += '<option value="Portugal">Portugal</option>';
//    accStatusTemplate += '<option value="PuertoRico">PuertoRico</option>';
//    accStatusTemplate += '<option value="Qatar">Qatar</option>';
//    accStatusTemplate += '<option value="Reunion">Reunion</option>';
//    accStatusTemplate += '<option value="Romania">Romania</option>';
//    accStatusTemplate += '<option value="RussianFederation">RussianFederation</option>';
//    accStatusTemplate += '<option value="Rwanda">Rwanda</option>';
//    accStatusTemplate += '<option value="SaintKittsandNevis">SaintKittsandNevis</option>';
//    accStatusTemplate += '<option value="SaintLUCIA">SaintLUCIA</option>';
//    accStatusTemplate += '<option value="SaintVincentandtheGrenadines">SaintVincentandtheGrenadines</option>';
//    accStatusTemplate += '<option value="Samoa">Samoa</option>';
//    accStatusTemplate += '<option value="SanMarino">SanMarino</option>';
//    accStatusTemplate += '<option value="SaoTomeandPrincipe">SaoTomeandPrincipe</option>';
//    accStatusTemplate += '<option value="SaudiArabia">SaudiArabia</option>';
//    accStatusTemplate += '<option value="Senegal">Senegal</option>';
//    accStatusTemplate += '<option value="Seychelles">Seychelles</option>';
//    accStatusTemplate += '<option value="SierraLeone">SierraLeone</option>';
//    accStatusTemplate += '<option value="SierraLeone">Singapore</option>';
//    accStatusTemplate += '<option value="Slovakia(SlovakRepublic)">Slovakia(SlovakRepublic)</option>';
//    accStatusTemplate += '<option value="Slovenia">Slovenia</option>';
//    accStatusTemplate += '<option value="SolomonIslands">SolomonIslands</option>';
//    accStatusTemplate += '<option value="Somalia">Somalia</option>';
//    accStatusTemplate += '<option value="SouthAfrica">SouthAfrica</option>';
//    accStatusTemplate += '<option value="SouthGeorgiaandtheSouthSandwichIslands">SouthGeorgiaandtheSouthSandwichIslands</option>';
//    accStatusTemplate += '<option value="Spain">Spain</option>';
//    accStatusTemplate += '<option value="SriLanka">SriLanka</option>';
//    accStatusTemplate += '<option value="St.Helena">St.Helena</option>';
//    accStatusTemplate += '<option value="St.PierreandMiquelon">St.PierreandMiquelon</option>';
//    accStatusTemplate += '<option value="Sudan">Sudan</option>';
//    accStatusTemplate += '<option value="Suriname">Suriname</option>';
//    accStatusTemplate += '<option value="SvalbardandJanMayenIslands">SvalbardandJanMayenIslands</option>';
//    accStatusTemplate += '<option value="Swaziland">Swaziland</option>';
//    accStatusTemplate += '<option value="Sweden">Sweden</option>';
//    accStatusTemplate += '<option value="Switzerland">Switzerland</option>';
//    accStatusTemplate += '<option value="SyrianArabRepublic">SyrianArabRepublic</option>';
//    accStatusTemplate += '<option value="Taiwan,ProvinceofChina">Taiwan,ProvinceofChina</option>';
//    accStatusTemplate += '<option value="Tajikistan">Tajikistan</option>';
//    accStatusTemplate += '<option value="Tanzania,UnitedRepublicof">Tanzania,UnitedRepublicof</option>';
//    accStatusTemplate += '<option value="Thailand">Thailand</option>';
//    accStatusTemplate += '<option value="Togo">Togo</option>';
//    accStatusTemplate += '<option value="Tokelau">Tokelau</option>';
//    accStatusTemplate += '<option value="Tonga">Tonga</option>';
//    accStatusTemplate += '<option value="TrinidadandTobago">TrinidadandTobago</option>';
//    accStatusTemplate += '<option value="Tunisia">Tunisia</option>';
//    accStatusTemplate += '<option value="Turkey">Turkey</option>';
//    accStatusTemplate += '<option value="Turkmenistan">Turkmenistan</option>';
//    accStatusTemplate += '<option value="TurksandCaicosIslands">TurksandCaicosIslands</option>';
//    accStatusTemplate += '<option value="Tuvalu">Tuvalu</option>';
//    accStatusTemplate += '<option value="Uganda">Uganda</option>';
//    accStatusTemplate += '<option value="Ukraine">Ukraine</option>';
//    accStatusTemplate += '<option value="UnitedArabEmirates">UnitedArabEmirates</option>';
//    accStatusTemplate += '<option value="UnitedKingdom">UnitedKingdom</option>';
//    accStatusTemplate += '<option value="UnitedStates">UnitedStates</option>';
//    accStatusTemplate += '<option value="UnitedStatesMinorOutlyingIslands">UnitedStatesMinorOutlyingIslands</option>';
//    accStatusTemplate += '<option value="Uruguay">Uruguay</option>';
//    accStatusTemplate += '<option value="Uzbekistan">Uzbekistan</option>';
//    accStatusTemplate += '<option value="Vanuatu">Vanuatu</option>';
//    accStatusTemplate += '<option value="Venezuela">Venezuela</option>';
//    accStatusTemplate += '<option value="VietNam">VietNam</option>';
//    accStatusTemplate += '<option value="VirginIslands(British)">VirginIslands(British)</option>';
//    accStatusTemplate += '<option value="VirginIslands(U.S.)">VirginIslands(U.S.)</option>';
//    accStatusTemplate += '<option value="WallisandFutunaIslands">WallisandFutunaIslands</option>';
//    accStatusTemplate += '<option value="WesternSahara">WesternSahara</option>';
//    accStatusTemplate += '<option value="Yemen">Yemen</option>';
//    accStatusTemplate += '<option value="Zambia">Zambia</option>';
//    accStatusTemplate += '<option value="Zimbabwe">Zimbabwe</option>';
    //ended by nikhil
    accStatusTemplate += '</select>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="row">';
    accStatusTemplate += '<div class="col-xs-6">';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label ">Zip Code</label>';
    accStatusTemplate += '<div class="col-xs-8">';
    accStatusTemplate += '<input type="text" class="form-control" placeholder="" value="" id="id_zipCode">';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="col-xs-6">';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label required">Region</label>';
    accStatusTemplate += '<div class="col-xs-8">';
    accStatusTemplate += '<select name="region" id="select2_region" class="form-control select2">';
    accStatusTemplate += '<option value="0">Select</option>';
    accStatusTemplate += '<option value="1">Mumbai</option>';
    accStatusTemplate += '<option value="2">North</option>';
    accStatusTemplate += '<option value="3">South</option>';
    accStatusTemplate += '<option value="4">West</option>';
    accStatusTemplate += '<option value="5">Gujarat</option>';
    accStatusTemplate += '<option value="6">East</option>';
    accStatusTemplate += '<option value="7">International</option>';
    accStatusTemplate += '</select>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="row">';
    accStatusTemplate += '<div class="col-xs-6">';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label">Website</label>';
    accStatusTemplate += '<div class="col-xs-8">';
    accStatusTemplate += '<input type="text" class="form-control" placeholder="" value="" id="id_website">';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="col-xs-6">	';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label">Industry</label>';
    accStatusTemplate += '<div class="col-xs-8">';
//    accStatusTemplate += '<input type="text" class="form-control" placeholder="" value="" id="id_industry">';
accStatusTemplate += '<select name="industry" id="Industry_list" class="form-control">';
                                                                        accStatusTemplate += '<option value="">Choose industry...</option>' ;
                                                                        accStatusTemplate += '<option value="Accounting">Accounting</option>' ;
                                                                        accStatusTemplate += '<option value="Airlines/Aviatio">Airlines/Aviation</option>' ;
                                                                        accStatusTemplate += '<option value="Alternative Dispute Resolution">Alternative Dispute Resolution</option>' ;
                                                                        accStatusTemplate += '<option value="Alternative Medicine">Alternative Medicine</option>' ;
                                                                        accStatusTemplate += '<option value="Animation">Animation</option>' ;
                                                                        accStatusTemplate += '<option value="Apparel &amp; Fashion">Apparel &amp; Fashion</option>' ;
                                                                        accStatusTemplate += '<option value="Architecture &amp; Planning">Architecture &amp; Planning</option>' ;
                                                                        accStatusTemplate += '<option value="Arts and Crafts">Arts and Crafts</option>' ;
                                                                        accStatusTemplate += '<option value="Automotive">Automotive</option>' ;
                                                                        accStatusTemplate += '<option value="Aviation &amp; Aerospace">Aviation &amp; Aerospace</option>' ;
                                                                        accStatusTemplate += '<option value="Banking">Banking</option>' ;
                                                                        accStatusTemplate += '<option value="Biotechnology">Biotechnology</option>' ;
                                                                        accStatusTemplate += '<option value="Broadcast Media">Broadcast Media</option>' ;
                                                                        accStatusTemplate += '<option value="Building Materials">Building Materials</option>' ;
                                                                        accStatusTemplate += '<option value="Business Supplies and Equipment">Business Supplies and Equipment</option>' ;
                                                                        accStatusTemplate += '<option value="Capital Markets">Capital Markets</option>' ;
                                                                        accStatusTemplate += '<option value="Chemicals">Chemicals</option>' ;
                                                                        accStatusTemplate += '<option value="Civic &amp; Social Organization">Civic &amp; Social Organization</option>' ;
                                                                        accStatusTemplate += '<option value="Civil Engineering">Civil Engineering</option>' ;
                                                                        accStatusTemplate += '<option value="Commercial Real Estate">Commercial Real Estate</option>' ;
                                                                        accStatusTemplate += '<option value="Computer &amp; Network Security">Computer &amp; Network Security</option>' ;
                                                                        accStatusTemplate += '<option value="Computer Games">Computer Games</option>' ;
                                                                        accStatusTemplate += '<option value="Computer Hardware">Computer Hardware</option>' ;
                                                                        accStatusTemplate += '<option value="Computer Networking">Computer Networking</option>' ;
                                                                        accStatusTemplate += '<option value="Computer Software">Computer Software</option>' ;
                                                                        accStatusTemplate += '<option value="Construction">Construction</option>' ;
                                                                        accStatusTemplate += '<option value="Consumer Electronics">Consumer Electronics</option>' ;
                                                                        accStatusTemplate += '<option value="Consumer Goods">Consumer Goods</option>' ;
                                                                        accStatusTemplate += '<option value="Consumer Services">Consumer Services</option>' ;
                                                                        accStatusTemplate += '<option value="Cosmetics">Cosmetics</option>' ;
                                                                        accStatusTemplate += '<option value="Dairy">Dairy</option>' ;
                                                                        accStatusTemplate += '<option value="Defense &amp; Space">Defense &amp; Space</option>' ;
                                                                        accStatusTemplate += '<option value="Design">Design</option>' ;
                                                                        accStatusTemplate += '<option value="Education Management">Education Management</option>' ;
                                                                        accStatusTemplate += '<option value="E-Learning">E-Learning</option>' ;
                                                                        accStatusTemplate += '<option value="Electrical/Electronic Manufacturing">Electrical/Electronic Manufacturing</option>' ;
                                                                        accStatusTemplate += '<option value="Entertainment">Entertainment</option>' ;
                                                                        accStatusTemplate += '<option value="Environmental Services">Environmental Services</option>' ;
                                                                        accStatusTemplate += '<option value="Events Services">Events Services</option>' ;
                                                                        accStatusTemplate += '<option value="Executive Office">Executive Office</option>' ;
                                                                        accStatusTemplate += '<option value="Facilities Services">Facilities Services</option>' ;
                                                                        accStatusTemplate += '<option value="Farming">Farming</option>' ;
                                                                        accStatusTemplate += '<option value="Financial Services">Financial Services</option>' ;
                                                                        accStatusTemplate += '<option value="Fine Art">Fine Art</option>' ;
                                                                        accStatusTemplate += '<option value="Fishery">Fishery</option>' ;
                                                                        accStatusTemplate += '<option value="Food &amp; Beverages">Food &amp; Beverages</option>' ;
                                                                        accStatusTemplate += '<option value="Food Production">Food Production</option>' ;
                                                                        accStatusTemplate += '<option value="Fund-Raising">Fund-Raising</option>' ;
                                                                        accStatusTemplate += '<option value="Furniture">Furniture</option>' ;
                                                                        accStatusTemplate += '<option value="Gambling &amp; Casinos">Gambling &amp; Casinos</option>' ;
                                                                        accStatusTemplate += '<option value="Glass, Ceramics &amp; Concrete">Glass, Ceramics &amp; Concrete</option>' ;
                                                                        accStatusTemplate += '<option value="Government Administration">Government Administration</option>' ;
                                                                        accStatusTemplate += '<option value="Government Relations">Government Relations</option>' ;
                                                                        accStatusTemplate += '<option value="Graphic Design">Graphic Design</option>' ;
                                                                        accStatusTemplate += '<option value="Health, Wellness and Fitness">Health, Wellness and Fitness</option>' ;
                                                                        accStatusTemplate += '<option value="Higher Education">Higher Education</option>' ;
                                                                        accStatusTemplate += '<option value="Hospital &amp; Health Care">Hospital &amp; Health Care</option>' ;
                                                                        accStatusTemplate += '<option value="Hospitality">Hospitality</option>' ;
                                                                        accStatusTemplate += '<option value="Human Resources">Human Resources</option>' ;
                                                                        accStatusTemplate += '<option value="Import and Export">Import and Export</option>' ;
                                                                        accStatusTemplate += '<option value="Individual &amp; Family Services">Individual &amp; Family Services</option>' ;
                                                                        accStatusTemplate += '<option value="Individual &amp; Family Services">Industrial Automation</option>' ;
                                                                        accStatusTemplate += '<option value="Information Services">Information Services</option>' ;
                                                                        accStatusTemplate += '<option value="Information Technology and Services">Information Technology and Services</option>' ;
                                                                        accStatusTemplate += '<option value="Insurance">Insurance</option>' ;
                                                                        accStatusTemplate += '<option value="International Affairs">International Affairs</option>' ;
                                                                        accStatusTemplate += '<option value="International Trade and Development">International Trade and Development</option>' ;
                                                                        accStatusTemplate += '<option value="Internet">Internet</option>' ;
                                                                        accStatusTemplate += '<option value="Investment Banking">Investment Banking</option>' ;
                                                                        accStatusTemplate += '<option value="Investment Management">Investment Management</option>' ;
                                                                        accStatusTemplate += '<option value="Judiciar">Judiciary</option>' ;
                                                                        accStatusTemplate += '<option value="Law Enforcement">Law Enforcement</option>' ;
                                                                        accStatusTemplate += '<option value="Law Practice">Law Practice</option>' ;
                                                                        accStatusTemplate += '<option value="Legal Services">Legal Services</option>' ;
                                                                        accStatusTemplate += '<option value="Legislative Office">Legislative Office</option>' ;
                                                                        accStatusTemplate += '<option value="Leisure, Travel &amp; Tourism">Leisure, Travel &amp; Tourism</option>' ;
                                                                        accStatusTemplate += '<option value="Libraries">Libraries</option>' ;
                                                                        accStatusTemplate += '<option value="Logistics and Supply Chain">Logistics and Supply Chain</option>' ;
                                                                        accStatusTemplate += '<option value="Luxury Goods &amp; Jewelry">Luxury Goods &amp; Jewelry</option>' ;
                                                                        accStatusTemplate += '<option value="Machinery">Machinery</option>' ;
                                                                        accStatusTemplate += '<option value="Management Consulting">Management Consulting</option>' ;
                                                                        accStatusTemplate += '<option value="Maritim">Maritime</option>' ;
                                                                        accStatusTemplate += '<option value="Marketing and Advertising">Marketing and Advertising</option>' ;
                                                                        accStatusTemplate += '<option value="Market Research">Market Research</option>' ;
                                                                        accStatusTemplate += '<option value="Mechanical or Industrial Engineering">Mechanical or Industrial Engineering</option>' ;
                                                                        accStatusTemplate += '<option value="Media Production">Media Production</option>' ;
                                                                        accStatusTemplate += '<option value="Medical Devices">Medical Devices</option>' ;
                                                                        accStatusTemplate += '<option value="Medical Practice">Medical Practice</option>' ;
                                                                        accStatusTemplate += '<option value="Mental Health Care">Mental Health Care</option>' ;
                                                                        accStatusTemplate += '<option value="Military">Military</option>' ;
                                                                        accStatusTemplate += '<option value="Mining &amp; Metals">Mining &amp; Metals</option>' ;
                                                                        accStatusTemplate += '<option value="Motion Pictures and Film">Motion Pictures and Film</option>' ;
                                                                        accStatusTemplate += '<option value="Museums and Institutions">Museums and Institutions</option>' ;
                                                                        accStatusTemplate += '<option value="Music">Music</option>' ;
                                                                        accStatusTemplate += '<option value="Nanotechnology">Nanotechnology</option>' ;
                                                                        accStatusTemplate += '<option value="Newspapers">Newspapers</option>' ;
                                                                        accStatusTemplate += '<option value="Nonprofit Organization Managemen">Nonprofit Organization Management</option>' ;
                                                                        accStatusTemplate += '<option value="Oil &amp; Energy">Oil &amp; Energy</option>' ;
                                                                        accStatusTemplate += '<option value="Online Media">Online Media</option>' ;
                                                                        accStatusTemplate += '<option value="Outsourcing/Offshoring">Outsourcing/Offshoring</option>' ;
                                                                        accStatusTemplate += '<option value="Package/Freight Delivery">Package/Freight Delivery</option>' ;
                                                                        accStatusTemplate += '<option value="Packaging and Containers">Packaging and Containers</option>' ;
                                                                        accStatusTemplate += '<option value="Paper &amp; Forest Products">Paper &amp; Forest Products</option>' ;
                                                                        accStatusTemplate += '<option value="Performing Arts">Performing Arts</option>' ;
                                                                        accStatusTemplate += '<option value="Pharmaceuticals">Pharmaceuticals</option>' ;
                                                                        accStatusTemplate += '<option value="Philanthropy">Philanthropy</option>' ;
                                                                        accStatusTemplate += '<option value="Photography">Photography</option>' ;
                                                                        accStatusTemplate += '<option value="Plastics">Plastics</option>' ;
                                                                        accStatusTemplate += '<option value="Political Organization">Political Organization</option>' ;
                                                                        accStatusTemplate += '<option value="Primary/Secondary Education">Primary/Secondary Education</option>' ;
                                                                        accStatusTemplate += '<option value="Printing">Printing</option>' ;
                                                                        accStatusTemplate += '<option value="Professional Training &amp; Coaching">Professional Training &amp; Coaching</option>' ;
                                                                        accStatusTemplate += '<option value="Program Development">Program Development</option>' ;
                                                                        accStatusTemplate += '<option value="Public Policy">Public Policy</option>' ;
                                                                        accStatusTemplate += '<option value="Public Relations and Communications">Public Relations and Communications</option>' ;
                                                                        accStatusTemplate += '<option value="Public Safety">Public Safety</option>' ;
                                                                        accStatusTemplate += '<option value="Publishing">Publishing</option>' ;
                                                                        accStatusTemplate += '<option value="Railroad Manufacture">Railroad Manufacture</option>' ;
                                                                        accStatusTemplate += '<option value="Ranching">Ranching</option>' ;
                                                                        accStatusTemplate += '<option value="Real Estate">Real Estate</option>' ;
                                                                        accStatusTemplate += '<option value="Recreational Facilities and Services">Recreational Facilities and Services</option>' ;
                                                                        accStatusTemplate += '<option value="Religious Institutions">Religious Institutions</option>' ;
                                                                        accStatusTemplate += '<option value="Renewables &amp; Environment">Renewables &amp; Environment</option>' ;
                                                                        accStatusTemplate += '<option value="Research">Research</option>' ;
                                                                        accStatusTemplate += '<option value="Restaurants">Restaurants</option>' ;
                                                                        accStatusTemplate += '<option value="Retail">Retail</option>' ;
                                                                        accStatusTemplate += '<option value="Security and Investigations">Security and Investigations</option>' ;
                                                                        accStatusTemplate += '<option value="Semiconductors">Semiconductors</option>' ;
                                                                        accStatusTemplate += '<option value="Shipbuilding">Shipbuilding</option>' ;
                                                                        accStatusTemplate += '<option value="Sporting Goods">Sporting Goods</option>' ;
                                                                        accStatusTemplate += '<option value="Sports">Sports</option>' ;
                                                                        accStatusTemplate += '<option value="Staffing and Recruiting">Staffing and Recruiting</option>' ;
                                                                        accStatusTemplate += '<option value="Supermarkets">Supermarkets</option>' ;
                                                                        accStatusTemplate += '<option value="Telecommunications">Telecommunications</option>' ;
                                                                        accStatusTemplate += '<option value="Textiles">Textiles</option>' ;
                                                                        accStatusTemplate += '<option value="Think Tanks">Think Tanks</option>' ;
                                                                        accStatusTemplate += '<option value="Tobacco">Tobacco</option>' ;
                                                                        accStatusTemplate += '<option value="Translation and Localization">Translation and Localization</option>' ;
                                                                        accStatusTemplate += '<option value="Transportation/Trucking/Railroad">Transportation/Trucking/Railroad</option>' ;
                                                                        accStatusTemplate += '<option value="Utilities">Utilities</option>' ;
                                                                        accStatusTemplate += '<option value="Venture Capital &amp; Private Equity">Venture Capital &amp; Private Equity</option>' ;
                                                                        accStatusTemplate += '<option value="Veterinary">Veterinary</option>' ;
                                                                        accStatusTemplate += '<option value="Warehousing">Warehousing</option>' ;
                                                                        accStatusTemplate += '<option value="Wholesale">Wholesale</option>' ;
                                                                        accStatusTemplate += '<option value="Wine and Spirits">Wine and Spirits</option>' ;
                                                                        accStatusTemplate += '<option value="Wireless">Wireless</option>' ;
                                                                        accStatusTemplate += '<option value="Writing and Editing">Writing and Editing</option>' ;
                                                                     accStatusTemplate += '</select>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</form>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="modal-footer">';
    accStatusTemplate += '<button type="button" data-dismiss="modal" class="btn btn-default" onclick="resetCounter()">Close</button>';
    accStatusTemplate += '   ';
    accStatusTemplate += '<button type="button" data-toggle="modal" class="btn btn-primary" onClick="checkCancel()">Save All</button>';
    accStatusTemplate += '</div>';
//    accStatusTemplate += '</div>';
//    accStatusTemplate += '<div class="modal-footer">';
//    accStatusTemplate += '<button type="button" data-toggle="modal" class="btn btn-default">Close</button>';
//    accStatusTemplate += '<button type="button" data-toggle="modal" class="btn btn-primary" onClick="createAcoount()">Save All</button>';
//    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    $('#acc_cre').html(accStatusTemplate);
    App.initUniform();
    App.fixContentHeight();
    $('.date-picker').datepicker({
        rtl: App.isRTL(),
        autoclose: true
    });
    TableEditable.init();
    //App.callHandleScrollers();

}
// ended by nikhil

function  createAccountValidation()
{
    var startDate = $("#start_date").val();
    //coming
    var expiredOn = $("#expiry_date").val();
    // alert("expiredOn "+expiredOn);//coming
    var sStartDate = dateConverter(startDate);
    //alert("startDate "+sStartDate);
    var sExpiredOn = dateConverter(expiredOn);
    //alert("sExpiredOn "+sExpiredOn);



//     var dateSplit = startDate.split("/");
//    var date = dateSplit[0];
//    var month = dateSplit[1];
//    var year = dateSplit[2];
//    //var sExpireDate=year+"-"+month+"-"+date+" "+"18:30:00" ;
//   var toDate = convertLocalDateToServerDate(year,month - 1,date, 23, 59);
//    month = toDate.getMonth() + 1;
//    if (month < 10) {
//        month = '0' + month;
//    }
//    date = toDate.getDate();
//    if (date < 10) {
//        date = '0' + date;
//    }
//    hours = toDate.getHours();
//    if (hours < 10) {
//        hours = '0' + hours;
//    }
//    minutes = toDate.getMinutes();
//    if (minutes < 10) {
//        minutes = '0' + minutes;
//    }
//    var sStartDate = toDate.getFullYear() + "-" + month + "-" + date + " " + hours + ":" + minutes + ':59';

    var plan = document.getElementById("id_plan").options[document.getElementById("id_plan").selectedIndex].text;
    if (plan.length == 0 || plan == "Select") {
        fieldSenseTosterError("Plan should be selected .", true);
        return false;
    }


    var userLimit = document.getElementById("id_userLimit").value.trim();
    //alert("userLimit"+userLimit);//coming
    if (isPostiveInteger(userLimit) == false) {
        fieldSenseTosterError("Please Enter Valid User Limit .", true);
        return false;
    }
    if (userLimit < 25) {
        fieldSenseTosterError("Minimum User Limit should be 25.", true);
        return false;
    }
    if (startDate === "" || startDate === " " || startDate === 0)
    {
        fieldSenseTosterError("Start Date should not be blank", true);
        return false;
    }
    if (expiredOn === "" || expiredOn === " " || expiredOn === 0)
    {
        fieldSenseTosterError(" Expiry Date should not be blank", true);
        return false;
    }
    if (sStartDate > sExpiredOn)
    {
        fieldSenseTosterError("Start date should not be greater than Expiry date", true);
        return false;
    }
    var companyName = document.getElementById("id_companyName").value.trim();
    //alert("companyName"+companyName);//coming
    if (companyName.length == 0) {
        fieldSenseTosterError("Company Name can't be empty .", true);
        return false;
    }
    if (companyName.length > 50) {
        fieldSenseTosterError("Company Name can not be more than 50 characters", true);
        return false;
    }
    var phNo1 = document.getElementById("id_comPnNo1").value.trim();
    // alert("phNo1"+phNo1);//coming
    if (phNo1.length == 0) {
        fieldSenseTosterError("phone number can't be empty .", true);
        return false;
    }
    var isphNo1 = /^\d+$/.test(phNo1);
    if (!isphNo1) {
        fieldSenseTosterError("Invalid phone number.", true);
        return false;
    }
    var address1 = document.getElementById("id_address1").value.trim();
    //alert("address1"+address1);//coming
    if (address1.length == 0) {
        fieldSenseTosterError("Address can't be empty .", true);
        return false;
    }
    var city = document.getElementById("id_city").value.trim();
    //alert("city"+city);//coming
    if (city.length == 0) {
        fieldSenseTosterError("City can't be empty .", true);
        return false;
    }
//     var isCity = /^\d+$/.test(city);
//    if (isCity) {
//        fieldSenseTosterError("Invalid city name.", true);
//        return false;
//    }
    var state = document.getElementById("id_state").value.trim();
    //alert("state"+state);//coming
    if (state.length == 0) {
        fieldSenseTosterError("State can't be empty .", true);
        return false;
    }
//     var isState = /^\d+$/.test(zipCode);
//    if (isState) {
//        fieldSenseTosterError("Invalid state name.", true);
//        return false;
//    }

    var indexOfCountry = document.getElementById("select2_sample4");
    var country = indexOfCountry.options[indexOfCountry.selectedIndex].text;
    //alert("country"+country);//coming
    if (country.length == 0 || country == "--Select Country--") {
        fieldSenseTosterError("Country should be selected .", true);
        return false;
    }
    //   var zipCode = document.getElementById("id_zipCode").value.trim();
    var zipCode = document.getElementById("id_zipCode").value.trim();
    if (zipCode !== "")
    {

        var iszipCode = /^\d+$/.test(zipCode);
        if (!iszipCode) {
            fieldSenseTosterError("Invalid zip code.", true);
            return false;
        }
    }
//    
    var regionId = document.getElementById("select2_region").options[document.getElementById("select2_region").selectedIndex].value;
    //alert("regionId ++"+regionId);
    if (regionId == 0) {
        fieldSenseTosterError("Please Select Region .", true);
        return false;
    }



    //var regionId = document.getElementById("select2_region").value.trim();









    // alert("zipCode"+zipCode);//coming

//    var emailId = document.getElementById("id_adm_email").innerHTML;
//   // alert("emailId"+emailId);
//    if (emailId.length == 0) {
//        fieldSenseTosterError("Email can't be empty .", true);
//        return false;
//    }
//    var emailPattern = /^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;
//    if (!emailPattern.test(emailId)) {
//        fieldSenseTosterError("Invalid email address .", true);
//        return false;
//    }
//  



    //  var status = document.getElementById("optionsRadios4").value.trim();
    //  alert("status "+status);
//    if (mobile.trim().length != 0) {
//        if (!(/^\d+$/.test(mobile))) {
//            fieldSenseTosterError("Invalid Mobile number.", true);
//            return false;
//        } else if (mobile.trim().length > 20) {
//            fieldSenseTosterError("Mobile number should not be more than 20 digits", true);
//            return false;
//        }
//    }
    // var gender = document.getElementById("id_gender").options[document.getElementById("id_gender").selectedIndex].value;
    // alert("gender "+gender);

    var plan = document.getElementById("id_plan").options[document.getElementById("id_plan").selectedIndex].text;
//    alert("plan++++"+plan);
//    if (gender == undefined) {
//        fieldSenseTosterError("Please select the gender.", true);
//        return false;
//    }

    return true;
}
//added y nikhil :- 11th july 2017
function createAcoount(saveCounter, addCounter) {
//    $('#pleaseWaitDialog').modal('show');
//   alert("createAcoount");
//    //Editing for start date feature.
    var startDate = $("#start_date").val();
    console.log("befor str method "+startDate);
    console.log("startDate :"+startDate);
    //coming
    var expiredOn = $("#expiry_date").val();

    var sStartDate = dateConverter(startDate);
    //alert("startDate "+sStartDate);
    var sExpiredOn = dateConverter(expiredOn);
    //alert("sExpiredOn "+sExpiredOn);



//     var dateSplit = startDate.split("/");
//    var date = dateSplit[0];
//    var month = dateSplit[1];
//    var year = dateSplit[2];
//    //var sExpireDate=year+"-"+month+"-"+date+" "+"18:30:00" ;
//   var toDate = convertLocalDateToServerDate(year,month - 1,date, 23, 59);
//    month = toDate.getMonth() + 1;
//    if (month < 10) {
//        month = '0' + month;
//    }
//    date = toDate.getDate();
//    if (date < 10) {
//        date = '0' + date;
//    }
//    hours = toDate.getHours();
//    if (hours < 10) {
//        hours = '0' + hours;
//    }
//    minutes = toDate.getMinutes();
//    if (minutes < 10) {
//        minutes = '0' + minutes;
//    }
//    var sStartDate = toDate.getFullYear() + "-" + month + "-" + date + " " + hours + ":" + minutes + ':59';

    var companyName = document.getElementById("id_companyName").value.trim();
    //alert("companyName"+companyName);//coming
    if (companyName.length == 0) {
        fieldSenseTosterError("Company Name can't be empty .", true);
        return false;
    }
    if (companyName.length > 50) {
        fieldSenseTosterError("Company Name can not be more than 50 characters", true);
        return false;
    }
    var userLimit = document.getElementById("id_userLimit").value.trim();
    //alert("userLimit"+userLimit);//coming
    if (isPostiveInteger(userLimit) == false) {
        fieldSenseTosterError("Please Enter Valid User Limit .", true);
        return false;
    }
    if (userLimit < 25) {
        fieldSenseTosterError("No.of Users must be greater than 25.", true);
        return false;
    }
    if (startDate === "" || startDate === " " || startDate === 0)
    {
        fieldSenseTosterError("Start Date should not be blank", true);
        return false;
    }
    if (expiredOn === "" || expiredOn === " " || expiredOn === 0)
    {
        fieldSenseTosterError(" Expiry Date should not be blank", true);
        return false;
    }
    if (sStartDate > sExpiredOn)
    {
        fieldSenseTosterError("Start date should not be greater than Expiry date", true);
        return false;
    }
    var address1 = document.getElementById("id_address1").value.trim();
    //alert("address1"+address1);//coming
    if (address1.length == 0) {
        fieldSenseTosterError("Address can't be empty .", true);
        return false;
    }

//var regionId = document.getElementById("select2_region").value.trim();

    var regionId = document.getElementById("select2_region").options[document.getElementById("select2_region").selectedIndex].value;
    //alert("regionId ++"+regionId);
    if (regionId == 0) {
        fieldSenseTosterError("Please Select Region .", true);
        return false;
    }

    var city = document.getElementById("id_city").value.trim();
    var icity = /^\d+$/.test(city);
    if (icity) {
        fieldSenseTosterError("Invalid city name .", true);
        return false;
    }
//alert("city"+city);//coming
    if (city.length == 0) {
        fieldSenseTosterError("City can't be empty .", true);
        return false;
    }
    var state = document.getElementById("id_state").value.trim();
    var iState = /^\d+$/.test(state);
    if (iState) {
        fieldSenseTosterError("Invalid state name .", true);
        return false;
    }
//alert("state"+state);//coming
    if (state.length == 0) {
        fieldSenseTosterError("State can't be empty .", true);
        return false;
    }
    var indexOfCountry = document.getElementById("select2_sample4");
    var country = indexOfCountry.options[indexOfCountry.selectedIndex].text;
    //alert("country"+country);//coming
    if (country.length == 0 || country == "--Select Country--") {
        fieldSenseTosterError("Country should be selected .", true);
        return false;
    }
    var zipCode = document.getElementById("id_zipCode").value.trim();
    if (zipCode !== "")
    {
        var iszipCode = /^\d+$/.test(zipCode);
        if (!iszipCode) {
            fieldSenseTosterError("Invalid zip code.", true);
            return false;
        }
    }
// alert("zipCode"+zipCode);//coming
    var phNo1 = document.getElementById("id_comPnNo1").value.trim();
    // alert("phNo1"+phNo1);//coming
    if (phNo1.length == 0) {
        fieldSenseTosterError("phone number can't be empty .", true);
        return false;
    }
    var isphNo1 = /^\d+$/.test(phNo1);
    if (!isphNo1) {
        fieldSenseTosterError("Invalid phone number.", true);
        return false;
    }
//    var emailId = document.getElementById("id_adm_email").innerHTML;
//   // alert("emailId"+emailId);
//    if (emailId.length == 0) {
//        fieldSenseTosterError("Email can't be empty .", true);
//        return false;
//    }
//    var emailPattern = /^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;
//    if (!emailPattern.test(emailId)) {
//        fieldSenseTosterError("Invalid email address .", true);
//        return false;
//    }
//  

    var plan = document.getElementById("id_plan").options[document.getElementById("id_plan").selectedIndex].text;
    if (plan.length == 0 || plan == "Select") {
        fieldSenseTosterError("Plan should be selected .", true);
        return false;
    }
    
    if(document.getElementById("optionsRadios4").checked)
    {   
    var status = document.getElementById("optionsRadios4").value;
    console.log("status +"+status);
}
 if(document.getElementById("optionsRadios5").checked)
    {   
    var status = document.getElementById("optionsRadios5").value;
    console.log("status 5 +"+status);
}
//added by nikhil
var allowOffline_edit = 1;
    var valueAllowOffline_edit = document.getElementById("id_allowoffline_edit").checked;
    //    alert("valueAllowOffline_edit "+valueAllowOffline_edit);
    if (valueAllowOffline_edit == false) {
        allowOffline_edit = 0;
    }
//ended by nikhil
   //alert("status "+status);

//    if (mobile.trim().length != 0) {
//        if (!(/^\d+$/.test(mobile))) {
//            fieldSenseTosterError("Invalid Mobile number.", true);
//            return false;
//        } else if (mobile.trim().length > 20) {
//            fieldSenseTosterError("Mobile number should not be more than 20 digits", true);
//            return false;
//        }
//    }
// var gender = document.getElementById("id_gender").options[document.getElementById("id_gender").selectedIndex].value;
// alert("gender "+gender);

    var plan = document.getElementById("id_plan").options[document.getElementById("id_plan").selectedIndex].text;
    console.log("plan new : "+plan);
//    alert("plan++++"+plan);
//    if (gender == undefined) {
//        fieldSenseTosterError("Please select the gender.", true);
//        return false;
//    }
    var company_website = document.getElementById("id_website").value.trim();
   
    if (company_website.length > 50) {
        fieldSenseTosterError("Company Name can not be more than 50 characters", true);
        return false;
    }
//  alert("id_website "+company_website);

//    var industry = document.getElementById("id_industry").value.trim();
    //  alert("industry "+industry);
    var indexOfIndustry = document.getElementById("Industry_list");
var industry = indexOfIndustry.options[indexOfIndustry.selectedIndex].value;

    // added by nikhil := code convert html table into one object
    var tbl = $('#sample_editable_1 tbody tr').map(function (idxRow, ele) {
//
// start building the retVal object
//
        var retVal = {id: ++idxRow};
        //
        // for each cell
        //
        tbl = $(ele).find('td').map(function (idxCell, ele) {
            var input = $(ele).find(':input');
            //
            // if cell contains an input or select....
            //
            if (input.length == 1) {
                var attr = $('#sample_editable_1 thead tr th').eq(idxCell).text();
                console.log("in if condition " + attr);
                console.log("gender dekh " + retVal[attr]);
                if (attr == 'Gender')
                {
                    attr = 'gender';
                }
                if (attr == 'Password')
                {
                    attr = 'password';
                }
                if (attr == 'Designation ')
                {
                    attr = 'designation';
                }
                retVal[attr] = input.val();
                if (retVal[attr] == "2")
                {
                    retVal[attr] = "0";
                }

            } else {
                var attr = $('#sample_editable_1 thead tr th').eq(idxCell).text();
                if (attr == 'Username')
                {
                    attr = 'firstName';
                }
                if (attr == 'Email')
                {
                    attr = 'emailAddress';
                }
                if (attr == 'Designation')
                {
                    attr = 'designation';
                }
                if (attr == 'Mobile No.')
                {
                    attr = 'mobileNo';
                }

                console.log("in else condition " + attr);
                console.log();
                retVal[attr] = $(ele).text();
                console.log("table heads " + retVal[attr]);
            }
        });
        return retVal;
    }).get();
    console.log(JSON.stringify(tbl));
    $('#pleaseWaitDialog').modal('show');
//alert(JSON.stringify(tbl));
    // $('#pleaseWaitDialog').modal('show');
    var dataSmple = new Array();
    dataSmple.push(tbl);
    var accountObject = {
        //added by nikhil
        "plan": plan,
        "companyWebsite": company_website,
        "industry": industry,
//"industry":"IT",
        "status": status,
// "status":"1",
        "companyName": companyName,
        "userLimit": userLimit,
        "regionId": regionId,
        "address1": address1,
        "city": city,
        "state": state,
        "country": country,
        "zipCode": zipCode,
        "companyPhoneNumber1": parseFloat(phNo1),
        //"emailAddress": emailId,
        "strStartDate": sStartDate,
        "sExpiredOn": sExpiredOn,
        "adminuser": tbl

    }
    console.log(JSON.stringify(accountObject));
    var jsonData = JSON.stringify(accountObject);
    $("#responsive2").modal('show');
    $.ajax({
        type: "POST",
        url: fieldSenseURL + "/account",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        asyn: false,
        success: function (data) {
//            alert(JSON.stringify(data.user.id));
                console.log("data.result :- "+data.result.id);
            if (data.errorCode == '0000') {
                //   document.removeChild(document.getElementById("id_accStatus"));
                saveCounter = 0;
                addCounter = 1;
                fieldSenseTosterSuccess(data.errorMessage, true);
                // /("inside if");
                $("#acc_cre").modal('hide');
                $('div').removeClass('modal-backdrop');
                // $(".modal-backdrop").modal('hide');
                clearFilters();
                // alert("data@@ "+data.errorCode);
                verify(data.errorCode);
                // return "success";
            } else {
                // alert("inside else");
                saveCounter = 0;
                addCounter = 1;
                fieldSenseTosterError(data.errorMessage, true);
                $('#pleaseWaitDialog').modal('hide');
            }
            var accountSettingsObject = {
                    "id": data.result.id,
                    "allowOffline": allowOffline_edit
                }

                var jsonData = JSON.stringify(accountSettingsObject);
                $.ajax({
                    type: "PUT",
                    url: fieldSenseURL + "/account/settings/values/" + data.result.id,
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

                    }

                });

            
        },
        error: function (xhr, status, error) {
            // alert("in error");
            var err = eval("(" + xhr.responseText + ")");
            var message = err.fieldErrors[0].message;
//            alert(message);
            fieldSenseTosterError(message, true);
            $('#pleaseWaitDialog').modal('hide');
        }
    });
}
// ended by nikhil

//added by nikhil - 11th july 2017
function editAccTemplate(accId) {
//alert("editAccTemplate");
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/account/" + accId,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken,
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            if (data.errorCode == '0000') {
                var accDetails = data.result;
                var userId = accDetails.user.id;
                //nikhil bhosale
                var email = accDetails.user.email_address;
//                        alert(emailId);
//                        alert(emailId);
                var email = accDetails.emailAddress;
                var currentDate = new Date();
                var dd = currentDate.getDate();
                var mm = currentDate.getMonth() + 1; //January is 0!
                var yyyy = currentDate.getFullYear();
                if (dd < 10) {
                    dd = '0' + dd;
                }

                if (mm < 10) {
                    mm = '0' + mm;
                }

                currentDate = dd + '/' + mm + '/' + yyyy;
                var sExpiredOn = accDetails.sExpiredOn;
                //  alert("sExpiredOn "+sExpiredOn);
                sExpiredOn = sExpiredOn.split(" ")[0];
                sExpiredOn = sExpiredOn.split("-");
                sExpiredOn = sExpiredOn[2] + '/' + sExpiredOn[1] + '/' + sExpiredOn[0];
                var strStartDate = accDetails.strStartDate;
                strStartDate = strStartDate.split(" ")[0];
                //  alert("strStartDate "+strStartDate);
                strStartDate = strStartDate.split("-");
                strStartDate = strStartDate[2] + '/' + strStartDate[1] + '/' + strStartDate[0];
                // alert("strStartDate ****"+strStartDate);
                var status = accDetails.status;
                // alert("status "+status);
                var userLimit = accDetails.userLimit;
                var companyName = accDetails.companyName;
                // alert("companyName "+companyName);
                var address1 = accDetails.address1;
                var city = accDetails.city;
                var state = accDetails.state;
                var country = accDetails.country;
                var zipCode = accDetails.zipCode;
                var companyPhoneNumber1 = accDetails.companyPhoneNumber1;
                var emailId = accDetails.emailAddress;
                var userFname = accDetails.user.firstName;
                var usrdesignation = accDetails.user.designation;
                var usrEmailId = accDetails.user.emailAddress;
                var userMobile = accDetails.mobileNo;
                var userGender = accDetails.user.gender;
                var usrpassword = accDetails.user.password;
                var role = accDetails.role;
                var regionId = accDetails.regionId;
                //added by nikhil 15-06-2017
                var admin_mobile = accDetails.user.mobileNo;
                //   alert("admin_mobile"+admin_mobile);
                var first_name = accDetails.user.firstName;
                var admin_email = accDetails.user.emailAddress
                var adm_designation = accDetails.user.designation
                var adm_password = accDetails.user.password
                //  alert("first_name "+first_name);
                var plan = accDetails.plan;
//                alert(plan);
                var industry = accDetails.industry;
                var website = accDetails.companyWebsite;
                var companyPhoneNumber = accDetails.companyPhoneNumber1;
                var regionId = accDetails.regionId;
               // var regionName =
//              alert("plan "+plan);
             //   alert("industry"+industry);

                //ended by nikhil
                // added by jyoti 22-12-2016


                $.ajax({
                    type: "GET",
                    url: fieldSenseURL + "/account/settings/values/" + accId,
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
                            var offlineEnable = accountSettings.allowOffline;
                            // alert("offlineEnable "+offlineEnable);
                            var allowOfflineAccount_State = accountSettings.allowOffline;
                            //ended by jyoti
                            var accStatusTemplate = '';
                            //  accStatusTemplate += '<div id="acc_cre" class="modal fade" tabindex="-1"aria-hidden="true">';
                            accStatusTemplate += '<div class="modal-dialog"  id="id_accStatus">';
                            accStatusTemplate += '<div class="modal-content">';
                            accStatusTemplate += '<div class="modal-header">';
                            accStatusTemplate += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true" ></button>';
                            accStatusTemplate += '<h4 class="modal-title">Account Details - ' + companyName + '</h4>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '<div class="modal-body">';
                            accStatusTemplate += '<div class="tabbable tabbable-custom">';
                            accStatusTemplate += '<ul class="nav nav-tabs">';
                            accStatusTemplate += '<li class="active">';
                            accStatusTemplate += '<a href="#tab_1_0" data-toggle="tab">Admin</a>';
                            accStatusTemplate += '</li>';
                            accStatusTemplate += '<li class="">';
                            accStatusTemplate += '<a href="#tab_1_1" data-toggle="tab">Plan</a>';
                            accStatusTemplate += '</li>';
                            accStatusTemplate += '<li class="">';
                            accStatusTemplate += '<a href="#tab_1_2" data-toggle="tab">Account</a>';
                            accStatusTemplate += '</li>';
                            accStatusTemplate += '</ul>';
                            accStatusTemplate += '<div class="tab-content">';
                            accStatusTemplate += '<div class="tab-pane fade active in" id="tab_1_0">';
                            accStatusTemplate += '<div class="form-body" id="act">';
                            accStatusTemplate += '<form id="my_form" class="form-horizontal" >';
                            accStatusTemplate += '<div class="table-toolbar">';
                            accStatusTemplate += '<div class="btn-group">';
                            accStatusTemplate += '<button class="btn btn-info" id="sample_editable_1_new1">';
                            accStatusTemplate += '<i class="fa fa-plus"></i> Add New';
                            accStatusTemplate += '</button>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            //accStatusTemplate += '<table class="table table-striped table-hover table-bordered" id="sample_editable_1">';
                            accStatusTemplate += '<table class="table table-striped table-hover table-bordered" id="sample_editable_1" style="font-size: small;font-family: "Open Sans",sans-serif;" >';
                            accStatusTemplate += '<thead>';
                            accStatusTemplate += '<tr>';
                            accStatusTemplate += '<th class="sorting_disabled">';
                            // accStatusTemplate += 'Name';
                            accStatusTemplate += 'Username';
                            accStatusTemplate += '</th>';
                            accStatusTemplate += '<th>';
                            //  accStatusTemplate += 'Email';
                            accStatusTemplate += 'Email';
                            accStatusTemplate += '</th>';
                            accStatusTemplate += '<th>';
//    accStatusTemplate += 'Password';
                            accStatusTemplate += 'Password';
                            accStatusTemplate += '</th>';
                            accStatusTemplate += '<th>';
//    accStatusTemplate += 'Password';
                            accStatusTemplate += 'Re-type Password';
                            accStatusTemplate += '</th>';
                            accStatusTemplate += '<th>';
                            //   accStatusTemplate += 'Designation';
                            accStatusTemplate += 'Designation';
                            accStatusTemplate += '</th>';
                            accStatusTemplate += '<th>';
                            //   accStatusTemplate += 'Mobile';
                            accStatusTemplate += 'Mobile No.';
                            accStatusTemplate += '</th>';
                            accStatusTemplate += '<th>';
                            //  accStatusTemplate += 'Gender';
                            accStatusTemplate += 'Gender';
                            accStatusTemplate += '</th>';
                            accStatusTemplate += '<th>';
                            accStatusTemplate += '</th>';
                            accStatusTemplate += '<th style="display:none;">adminUserId';
                            accStatusTemplate += '</th>';
                            accStatusTemplate += '</tr>';
                            accStatusTemplate += '</thead>';
                            accStatusTemplate += '<tbody>';
                            var counter = accDetails.allAdminUser.length;
                            console.log("accDetails.allAdminUser.length counter " + counter);
                            for (var i = 0; i < accDetails.allAdminUser.length; i++)
                            {
                                var fullname = accDetails.allAdminUser[i].fullname;
                                var userEmail = accDetails.allAdminUser[i].emailAddress;
                                var password = accDetails.allAdminUser[i].password;
                                var retypepassword = accDetails.allAdminUser[i].password;
                                var designation = accDetails.allAdminUser[i].designation;
                                var mobileno = accDetails.allAdminUser[i].mobileNo;
                                var gender = accDetails.allAdminUser[i].gender;
                                // Added by jyoti singh
//        var userFName = accDetails.allAdminUser[i].firstName;
                                var userFName = accDetails.allAdminUser[i].fullname;
                                console.log("userFName@@ " + userFName);
                                var userFNameSubString;
                                if (userFName.length > 19) {
                                    userFNameSubString = userFName.substring(0, 18) + "..";
                                } else {
                                    userFNameSubString = accDetails.allAdminUser[i].fullname;
                                }
                                accStatusTemplate += '<tr>';
                                accStatusTemplate += '<td title="' + userFName + '" >';
                                // accStatusTemplate += '<input type="hidden"  value="'+userFName+'"  id="id_uname" readOnly> ';
                                accStatusTemplate += userFNameSubString;
                                accStatusTemplate += '</td>';
                                // ended by jyoti
//                    console.log("id of admin"+accDetails.allAdminUser[i].id);
//    accStatusTemplate += '<tr>';
//    accStatusTemplate += '<td id="id_uname">';
////    accStatusTemplate += ' <input name="first_name" value="Alex" size="6" id="id_uname"  readonly >';
//    accStatusTemplate += accDetails.allAdminUser[i].firstName;
//    accStatusTemplate += '</td>';
                                accStatusTemplate += '<td id="id_adm_email">';
//    accStatusTemplate += '<input name="last_name" value="Alex@ratanfire.com" size="10" id="id_adm_email" readonly>';
//accStatusTemplate += 'Alex@ratanfire.com';
                                accStatusTemplate += accDetails.allAdminUser[i].emailAddress;
                                accStatusTemplate += '</td>';
                                accStatusTemplate += '<td >';
                                accStatusTemplate += '<input class="form-control input-small" type="password"  value="' + accDetails.allAdminUser[i].password + '" size="8" id="id_pwd' + i + '" readOnly> ';
                                accStatusTemplate += '<input class="form-control input-small" type="hidden"  value="' + accDetails.allAdminUser[i].gender + '" size="8" id="id_gender_1" readOnly> ';
//accStatusTemplate += accDetails.allAdminUser[i].password;
                                accStatusTemplate += '</td>';
                                accStatusTemplate += '<td >';
                                accStatusTemplate += '<input class="form-control input-small" type="password"  value="' + accDetails.allAdminUser[i].password + '" size="8" id="id_pwd1' + i + '" readOnly> ';
                                accStatusTemplate += '</td>';
                                accStatusTemplate += '<td id="id_designation">';
//    accStatusTemplate += '<input name="designation" value="Manager - Support" size="10" id="id_designation" readonly>';
//accStatusTemplate += 'Manager - Support';
                                accStatusTemplate += accDetails.allAdminUser[i].designation;
                                accStatusTemplate += '</td>';
                                accStatusTemplate += '<td id="id_mobile_number">';
//    accStatusTemplate += '<input name="mobile_no" value="9819245678" size="10" id="id_mobile_number" readonly>';
                                accStatusTemplate += accDetails.allAdminUser[i].mobileNo;
                                accStatusTemplate += '</td>';
                                accStatusTemplate += '<td>';
                                if (accDetails.allAdminUser[i].gender == 1)

                                {
                                    accStatusTemplate += ' <select id="id_gender" class="class_editGender1' + i + '" disabled>';
                                    accStatusTemplate += '<option value="1" selected >Male</option>';
                                    accStatusTemplate += '<option value="0">Female</option>';
                                    accStatusTemplate += '</select>  ';
                                } else {
                                    accStatusTemplate += ' <select id="id_gender"  class="class_editGender1' + i + '" disabled>';
                                    accStatusTemplate += '<option value="1"  >Male</option>';
                                    accStatusTemplate += '<option value="0" selected>Female</option>';
                                    accStatusTemplate += '</select>  '

                                }
                                accStatusTemplate += '</td>';
//    accStatusTemplate += '<td>';
//    accStatusTemplate += '<button class=" " id="btnEdit" onclick="writeonly()" > edit </button> ' ;
////    accStatusTemplate += '<button class=" " id="btnCancel" onclick="readonly()" > cancel </button> ' ;
//    accStatusTemplate += '</td>';

                                accStatusTemplate += '<td>';
                                accStatusTemplate += '<a class="edit1" href="javascript:;">Edit</a>  ';
                                accStatusTemplate += '<a class="delete1" href="javascript:;">Delete</a>';
                                accStatusTemplate += '<input type="hidden" id="id_counter" class="form-control input-small" value="' + counter + '">';
                                accStatusTemplate += '<input type="hidden" id="id_counter_i" class="form-control input-small" value="' + i + '">';
                                accStatusTemplate += '</td>';
                                accStatusTemplate += '<td id="id_admin_id" style="display:none;" >';
//    accStatusTemplate += '<input name="mobile_no" value="9819245678" size="10" id="id_mobile_number" readonly>';
                                accStatusTemplate += accDetails.allAdminUser[i].id;
                                accStatusTemplate += '</td>';
                                accStatusTemplate += '</tr>';
                            }
                            accStatusTemplate += '</tbody>';
                            accStatusTemplate += '</table>';
                            accStatusTemplate += '</form>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '<div class="tab-pane fade" id="tab_1_1">';
                            accStatusTemplate += '<div class="form-body" id="act">';
                            accStatusTemplate += '<form action="#" class="form-horizontal">';
                            accStatusTemplate += '<div class="row">';
                            accStatusTemplate += '<div class="col-xs-6">	';
                            accStatusTemplate += '<div class="form-group">';
                            accStatusTemplate += '<label class="col-xs-4 control-label required">Plan</label>';
                            accStatusTemplate += '<div class="col-xs-8">';
//                            //id_plan
    if(plan == "Free")
    {    
    accStatusTemplate += '<select name="region" id="id_plan" class="form-control select2" >\n\
                            <option value="0" >Select</option>\n\
                            <option value="1" selected >Free</option>\n\
                            <option value="2">';
    accStatusTemplate += 'Premium';
    accStatusTemplate += '</option>';
    accStatusTemplate += '</select>';
}     if(plan == "Premium")
    {    
    accStatusTemplate += '<select name="region" id="id_plan" class="form-control select2" >\n\
                            <option value="0" >Select</option>\n\
                            <option value="1" selected >Free</option>\n\
                            <option value="2" selected>';
    accStatusTemplate += 'Premium';
    accStatusTemplate += '</option>';
    accStatusTemplate += '</select>';
}  
//    accStatusTemplate += '<select name="region" id="id_plan" class="form-control select2" disabled>\n\
//                            <option value="0" >Select</option>\n\
//                            <option value="1" selected >Free</option>\n\
//                            <option value="2">';
//    accStatusTemplate += 'Premium';
//    accStatusTemplate += '</option>';
//    accStatusTemplate += '</select>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="col-xs-6">';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label required">User Limit</label>';
    accStatusTemplate += '<div class="col-xs-8">';
    accStatusTemplate += '<input type="text" class="form-control" placeholder="" value="'+userLimit+'" id="id_userLimit">';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';

    accStatusTemplate += '<div class="row">';
    accStatusTemplate += '<div class="col-xs-6">';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label required">Start Date</label>';
    accStatusTemplate += '<div class="col-md-8">';

//    accStatusTemplate += '<div class="input-group date date-picker" data-date="20 May 2017" data-date-format="dd MM yyyy" data-date-viewmode="years">';
                            accStatusTemplate += '<div class="input-group date date-picker" data-date-format="dd/mm/yyyy" data-date-viewmode="years">';
                            accStatusTemplate += '<input type="text" class="form-control" id="start_date" readonly value="' + strStartDate + '">';
                            accStatusTemplate += '<span class="input-group-btn">';
                            accStatusTemplate += '<button class="btn btn-info" type="button"><i class="fa fa-calendar"></i></button>';
                            accStatusTemplate += '</span>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '<div class="col-xs-6">';
                            accStatusTemplate += '<div class="form-group">';
                            accStatusTemplate += '<label class="col-xs-4 control-label required">Expiry Date</label>';
                            accStatusTemplate += '<div class="col-md-8">';
                            accStatusTemplate += '<div class="input-group date date-picker" data-date-format="dd/mm/yyyy" data-date-viewmode="years">';
                            accStatusTemplate += '<input type="text" class="form-control" id="expiry_date" value="' + sExpiredOn + '" readonly>';
                            accStatusTemplate += '<span class="input-group-btn">';
                            accStatusTemplate += '<button class="btn btn-info" type="button"><i class="fa fa-calendar"></i></button>';
                            accStatusTemplate += '</span>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>	';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</form>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '<div class="tab-pane fade  " id="tab_1_2">';
                            accStatusTemplate += '<div class="form-body" id="act">';
                            accStatusTemplate += '<form action="#" class="form-horizontal" name="edit_account">';
                            accStatusTemplate += '<div class="row">';
                            accStatusTemplate += '<div class="col-xs-6">';
                            accStatusTemplate += '<div class="form-group">';
                            accStatusTemplate += '<label class="col-xs-4 control-label required">Status</label>';
                            accStatusTemplate += '<div class="col-xs-8">';
                            accStatusTemplate += '<div class="radio-list">';
                            if (status == 1)
                            {
                                accStatusTemplate += '<label class="radio-inline">';
                                accStatusTemplate += '<input type="radio" name="optionsRadios" id="optionsRadios4" value="1" checked> Active </label>';
                                accStatusTemplate += '<label class="radio-inline">';
                                accStatusTemplate += '<input type="radio" name="optionsRadios" id="optionsRadios5" value="0"> Inactive </label>';
                            } else if (status == 0)
                            {
                                accStatusTemplate += '<label class="radio-inline">';
                                accStatusTemplate += '<input type="radio" name="optionsRadios" id="optionsRadios4" value="1" > Active </label>';
                                accStatusTemplate += '<label class="radio-inline">';
                                accStatusTemplate += '<input type="radio" name="optionsRadios" id="optionsRadios5" value="0" checked > Inactive </label>';
                            }
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '<div class="row">';
                            accStatusTemplate += '<div class="col-xs-6">';
                            accStatusTemplate += '<div class="form-group">';
                            accStatusTemplate += '<label class="col-md-4 control-label">Allow Offline</label>';
                            accStatusTemplate += '<div class="col-md-8">';
                            accStatusTemplate += '<div class="checkbox-list">';
                            if (offlineEnable == 1) {
                                accStatusTemplate += '<input type="checkbox" id="id_allowoffline_edit" checked>';
                            } else {
                                accStatusTemplate += '<input type="checkbox" id="id_allowoffline_edit">';
                            }
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '<div class="row">';
                            accStatusTemplate += '<div class="col-xs-6">';
                            accStatusTemplate += '<div class="form-group">';
                            accStatusTemplate += '<label class="col-xs-4 control-label required">Name</label>';
                            accStatusTemplate += '<div class="col-xs-8">';
                            accStatusTemplate += '<input type="text" class="form-control" placeholder="" value="' + companyName + '" id="id_companyName">';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '<div class="col-xs-6">';
                            accStatusTemplate += '<div class="form-group">';
                            accStatusTemplate += '<label class="col-xs-4 control-label required">Phone</label>';
                            accStatusTemplate += '<div class="col-xs-8">';
                            accStatusTemplate += '<input type="Phone" class="form-control" placeholder="" value="' + companyPhoneNumber + '" id="id_comPnNo1">';
                            accStatusTemplate += '</div>	';
                            accStatusTemplate += '</div>	';
                            accStatusTemplate += '</div>	';
                            accStatusTemplate += '</div>	';
                            accStatusTemplate += '<div class="row">	';
                            accStatusTemplate += '<div class="col-xs-6">	';
                            accStatusTemplate += '<div class="form-group">';
                            accStatusTemplate += '<label class="col-xs-4 control-label required">Address</label>';
                            accStatusTemplate += '<div class="col-xs-8">';
                            accStatusTemplate += '<textarea class="form-control" rows="3" placeholder="" id="id_address1"  >' + address1 + '</textarea>';
                            accStatusTemplate += '</div>	';
                            accStatusTemplate += '</div>	';
                            accStatusTemplate += '</div>	';
                            accStatusTemplate += '<div class="col-xs-6">	';
                            accStatusTemplate += '<div class="form-group">';
                            accStatusTemplate += '<label class="col-xs-4 control-label required">City</label>';
                            accStatusTemplate += '<div class="col-xs-8">';
                            accStatusTemplate += '<input type="text" class="form-control" value="' + city + '" id="id_city">';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '<div class="row">	';
                            accStatusTemplate += '<div class="col-xs-6">';
                            accStatusTemplate += '<div class="form-group">';
                            accStatusTemplate += '<label class="col-xs-4 control-label required">State</label>';
                            accStatusTemplate += '<div class="col-xs-8">';
                            accStatusTemplate += '<input type="Phone1" class="form-control" placeholder="" value="' + state + '" id="id_state">';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '<div class="col-xs-6">';
                            accStatusTemplate += '<div class="form-group">';
                            accStatusTemplate += '<label class="col-xs-4 control-label required">Country</label>';
                            accStatusTemplate += '<div class="col-xs-8">';
                            accStatusTemplate += '<select id="select2_sample_modal_4" name="country"  class="form-control select2">';
                                accStatusTemplate += '<option value="select">--Select Country--</option>';
                                accStatusTemplate += '<option value="Afghanistan">Afghanistan</option>';
                                accStatusTemplate += '<option value="Albania">Albania</option>';
                                accStatusTemplate += '<option value="Algeria">Algeria</option>';
                                accStatusTemplate += '<option value="American Samoa">American Samoa</option>';
                                accStatusTemplate += '<option value="Andorra">Andorra</option>';
                                accStatusTemplate += '<option value="Angola">Angola</option>';
                                accStatusTemplate += '<option value="Anguilla">Anguilla</option>';
                                accStatusTemplate += '<option value="Antarctica">Antarctica</option>';
                                accStatusTemplate += '<option value="Argentina">Argentina</option>';
                                accStatusTemplate += '<option value="Armenia">Armenia</option>';
                                accStatusTemplate += '<option value="Aruba">Aruba</option>';
                                accStatusTemplate += '<option value="Australia">Australia</option>';
                                accStatusTemplate += '<option value="Austria">Austria</option>';
                                accStatusTemplate += '<option value="Azerbaijan">Azerbaijan</option>';
                                accStatusTemplate += '<option value="Bahamas">Bahamas</option>';
                                accStatusTemplate += '<option value="Bahrain">Bahrain</option>';
                                accStatusTemplate += '<option value="Bangladesh">Bangladesh</option>';
                                accStatusTemplate += '<option value="Barbados">Barbados</option>';
                                accStatusTemplate += '<option value="Belarus">Belarus</option>';
                                accStatusTemplate += '<option value="Belgium">Belgium</option>';
                                accStatusTemplate += '<option value="Belize">Belize</option>';
                                accStatusTemplate += '<option value="Benin">Benin</option>';
                                accStatusTemplate += '<option value="Bermuda">Bermuda</option>';
                                accStatusTemplate += '<option value="Bhutan">Bhutan</option>';
                                accStatusTemplate += '<option value="Bolivia">Bolivia</option>';
                                accStatusTemplate += '<option value="Bosnia and Herzegowina">Bosnia and Herzegowina</option>';
                                accStatusTemplate += '<option value="Bouvet Island">Bouvet Island</option>';
                                accStatusTemplate += '<option value="Brazil">Brazil</option>';
                                accStatusTemplate += '<option value="British Indian Ocean Territory">British Indian Ocean Territory</option>';
                                accStatusTemplate += '<option value="Brunei Darussalam">Brunei Darussalam</option>';
                                accStatusTemplate += '<option value="Bulgaria">Bulgaria</option>';
                                accStatusTemplate += '<option value="Burkina Faso">Burkina Faso</option>';
                                accStatusTemplate += '<option value="Burundi">Burundi</option>';
                                accStatusTemplate += '<option value="Cambodia">Cambodia</option>';
                                accStatusTemplate += '<option value="Cameroon">Cameroon</option>';
                                accStatusTemplate += '<option value="Canada">Canada</option>';
                                accStatusTemplate += '<option value="Cape Verde">Cape Verde</option>';
                                accStatusTemplate += '<option value="Cayman Islands">Cayman Islands</option>';
                                accStatusTemplate += '<option value="Central African Republic">Central African Republic</option>';
                                accStatusTemplate += '<option value="Chad">Chad</option>';
                                accStatusTemplate += '<option value="Chile">Chile</option>';
                                accStatusTemplate += '<option value="China">China</option>';
                                accStatusTemplate += '<option value="Christmas Island">Christmas Island</option>';
                                accStatusTemplate += '<option value="Cocos (Keeling) Islands">Cocos (Keeling) Islands</option>';
                                accStatusTemplate += '<option value="Colombia">Colombia</option>';
                                accStatusTemplate += '<option value="Comoros">Comoros</option>';
                                accStatusTemplate += '<option value="Congo">Congo</option>';
                                accStatusTemplate += '<option value="Congo, the Democratic Republic of the">Congo, the Democratic Republic of the</option>';
                                accStatusTemplate += '<option value="Cook Islands">Cook Islands</option>';
                                accStatusTemplate += '<option value="Costa Rica">Costa Rica</option>';
                                accStatusTemplate += '<option value="Cote dIvoire">Cote dIvoire</option>';
                                accStatusTemplate += '<option value="Croatia (Hrvatska)">Croatia (Hrvatska)</option>';
                                accStatusTemplate += '<option value="Cuba">Cuba</option>';
                                accStatusTemplate += '<option value="Cyprus">Cyprus</option>';
                                accStatusTemplate += '<option value="Czech Republic">Czech Republic</option>';
                                accStatusTemplate += '<option value="Denmark">Denmark</option>';
                                accStatusTemplate += '<option value="Djibouti">Djibouti</option>';
                                accStatusTemplate += '<option value="Dominica">Dominica</option>';
                                accStatusTemplate += '<option value="Dominican Republic">Dominican Republic</option>';
                                accStatusTemplate += '<option value="Ecuador">Ecuador</option>';
                                accStatusTemplate += '<option value="Egypt">Egypt</option>';
                                accStatusTemplate += '<option value="El Salvador">El Salvador</option>';
                                accStatusTemplate += '<option value="Equatorial Guinea">Equatorial Guinea</option>';
                                accStatusTemplate += '<option value="Eritrea">Eritrea</option>';
                                accStatusTemplate += '<option value="Estonia">Estonia</option>';
                                accStatusTemplate += '<option value="Ethiopia">Ethiopia</option>';
                                accStatusTemplate += '<option value="Falkland Islands (Malvinas)">Falkland Islands (Malvinas)</option>';
                                accStatusTemplate += '<option value="Faroe Islands">Faroe Islands</option>';
                                accStatusTemplate += '<option value="Fiji">Fiji</option>';
                                accStatusTemplate += '<option value="Finland">Finland</option>';
                                accStatusTemplate += '<option value="France">France</option>';
                                accStatusTemplate += '<option value="French Guiana">French Guiana</option>';
                                accStatusTemplate += '<option value="French Polynesia">French Polynesia</option>';
                                accStatusTemplate += '<option value="French Southern Territories">French Southern Territories</option>';
                                accStatusTemplate += '<option value="Gabon">Gabon</option>';
                                accStatusTemplate += '<option value="Gambia">Gambia</option>';
                                accStatusTemplate += ' <option value="Georgia">Georgia</option>';
                                accStatusTemplate += '<option value="Germany">Germany</option>';
                                accStatusTemplate += '<option value="Ghana">Ghana</option>';
                                accStatusTemplate += '<option value="Gibraltar">Gibraltar</option>';
                                accStatusTemplate += '<option value="Greece">Greece</option>';
                                accStatusTemplate += '<option value="Greenland">Greenland</option>';
                                accStatusTemplate += '<option value="Grenada">Grenada</option>';
                                accStatusTemplate += '<option value="Guadeloupe">Guadeloupe</option>';
                                accStatusTemplate += '<option value="Guam">Guam</option>';
                                accStatusTemplate += '<option value="Guatemala">Guatemala</option>';
                                accStatusTemplate += '<option value="Guinea">Guinea</option>';
                                accStatusTemplate += '<option value="Guinea-Bissau">Guinea-Bissau</option>';
                                accStatusTemplate += '<option value="Guyana">Guyana</option>';
                                accStatusTemplate += '<option value="Haiti">Haiti</option>';
                                accStatusTemplate += '<option value="Heard and Mc Donald Islands">Heard and Mc Donald Islands</option>';
                                accStatusTemplate += '<option value="Holy See (Vatican City State)">Holy See (Vatican City State)</option>';
                                accStatusTemplate += '<option value="Honduras">Honduras</option>';
                                accStatusTemplate += '<option value="Hong Kong">Hong Kong</option>';
                                accStatusTemplate += '<option value="Hungary">Hungary</option>';
                                accStatusTemplate += '<option value="Iceland">Iceland</option>';
                                accStatusTemplate += '<option value="India">India</option>';
                                accStatusTemplate += '<option value="Indonesia">Indonesia</option>';
                                accStatusTemplate += '<option value="Iran (Islamic Republic of)">Iran (Islamic Republic of)</option>';
                                accStatusTemplate += '<option value="Iraq">Iraq</option>';
                                accStatusTemplate += '<option value="Ireland">Ireland</option>';
                                accStatusTemplate += '<option value="Israel">Israel</option>';
                                accStatusTemplate += '<option value="Italy">Italy</option>';
                                accStatusTemplate += '<option value="Jamaica">Jamaica</option>';
                                accStatusTemplate += '<option value="Japan">Japan</option>';
                                accStatusTemplate += '<option value="Jordan">Jordan</option>';
                                accStatusTemplate += '<option value="Kazakhstan">Kazakhstan</option>';
                                accStatusTemplate += '<option value="Kenya">Kenya</option>';
                                accStatusTemplate += '<option value="Kiribati">Kiribati</option>';
                                accStatusTemplate += '<option value="Korea, Democratic Peoples Republic of">Korea, Democratic Peoples 	Republic of</option>';
                                accStatusTemplate += '<option value="Korea, Republic of">Korea, Republic of</option>';
                                accStatusTemplate += '<option value="Kuwait">Kuwait</option>';
                                accStatusTemplate += '<option value="Kyrgyzstan">Kyrgyzstan</option>';
                                accStatusTemplate += '<option value="Lao Peoples Democratic Republic">Lao Peoples Democratic Republic</option>';
                                accStatusTemplate += '<option value="Latvia">Latvia</option>';
                                accStatusTemplate += '<option value="Lebanon">Lebanon</option>';
                                accStatusTemplate += '<option value="Lesotho">Lesotho</option>';
                                accStatusTemplate += '<option value="Liberia">Liberia</option>';
                                accStatusTemplate += '<option value="Libyan Arab Jamahiriya<">Libyan Arab Jamahiriya</option>';
                                accStatusTemplate += '<option value="Liechtenstein">Liechtenstein</option>';
                                accStatusTemplate += '<option value="Lithuania">Lithuania</option>';
                                accStatusTemplate += '<option value="Luxembourg">Luxembourg</option>';
                                accStatusTemplate += '<option value="MO">Macau</option>';
                                accStatusTemplate += '<option value="Macedonia, The Former Yugoslav Republic of">Macedonia, The Former Yugoslav Republic of</option>';
                                accStatusTemplate += '<option value="Madagascar">Madagascar</option>';
                                accStatusTemplate += '<option value="Malawi">Malawi</option>';
                                accStatusTemplate += '<option value="Malaysia">Malaysia</option>';
                                accStatusTemplate += '<option value="Maldives">Maldives</option>';
                                accStatusTemplate += '<option value="Mali">Mali</option>';
                                accStatusTemplate += '<option value="Malta">Malta</option>';
                                accStatusTemplate += '<option value="Marshall Islands">Marshall Islands</option>';
                                accStatusTemplate += '<option value="Martinique">Martinique</option>';
                                accStatusTemplate += '<option value="Mauritania">Mauritania</option>';
                                accStatusTemplate += '<option value="Mauritius">Mauritius</option>';
                                accStatusTemplate += '<option value="Mayotte">Mayotte</option>';
                                accStatusTemplate += '<option value="Mexico">Mexico</option>';
                                accStatusTemplate += '<option value="Micronesia, Federated States of">Micronesia, Federated States of</option>';
                                accStatusTemplate += '<option value=">Moldova, Republic of">Moldova, Republic of</option>';
                                accStatusTemplate += '<option value="Monaco">Monaco</option>';
                                accStatusTemplate += '<option value="Mongolia">Mongolia</option>';
                                accStatusTemplate += '<option value="Montserrat">Montserrat</option>';
                                accStatusTemplate += '<option value="Morocco">Morocco</option>';
                                accStatusTemplate += '<option value="Mozambique">Mozambique</option>';
                                accStatusTemplate += '<option value="Myanmar">Myanmar</option>';
                                accStatusTemplate += '<option value="Namibia">Namibia</option>';
                                accStatusTemplate += '<option value="Nauru">Nauru</option>';
                                accStatusTemplate += '<option value="Nepal">Nepal</option>';
                                accStatusTemplate += '<option value="Netherlands">Netherlands</option>';
                                accStatusTemplate += '<option value="New Caledonia">New Caledonia</option>';
                                accStatusTemplate += '<option value="New Zealand">New Zealand</option>';
                                accStatusTemplate += '<option value="Nicaragua">Nicaragua</option>';
                                accStatusTemplate += '<option value="Niger">Niger</option>';
                                accStatusTemplate += '<option value="Nigeria">Nigeria</option>';
                                accStatusTemplate += '<option value="Niue">Niue</option>';
                                accStatusTemplate += '<option value="Norfolk Island">Norfolk Island</option>';
                                accStatusTemplate += '<option value="Northern Mariana Islands">Northern Mariana Islands</option>';
                                accStatusTemplate += '<option value="Norway">Norway</option>';
                                accStatusTemplate += '<option value="Oman">Oman</option>';
                                accStatusTemplate += '<option value="Pakistan">Pakistan</option>';
                                accStatusTemplate += '<option value="Palau">Palau</option>';
                                accStatusTemplate += '<option value="Palestinian Territory">Palestinian Territory</option>'; //Palestine
                                accStatusTemplate += '<option value="Panama">Panama</option>';
                                accStatusTemplate += '<option value="Papua New Guinea">Papua New Guinea</option>';
                                accStatusTemplate += '<option value="Paraguay">Paraguay</option>';
                                accStatusTemplate += '<option value="Peru">Peru</option>';
                                accStatusTemplate += '<option value="Philippines">Philippines</option>';
                                accStatusTemplate += '<option value="Pitcairn">Pitcairn</option>';
                                accStatusTemplate += '<option value="Poland">Poland</option>';
                                accStatusTemplate += '<option value="Portugal">Portugal</option>';
                                accStatusTemplate += '<option value="Puerto Rico">Puerto Rico</option>';
                                accStatusTemplate += '<option value="Qatar">Qatar</option>';
                                accStatusTemplate += '<option value="Reunion">Reunion</option>';
                                accStatusTemplate += '<option value="Romania">Romania</option>';
                                accStatusTemplate += '<option value="Russian Federation">Russian Federation</option>';
                                accStatusTemplate += '<option value="Rwanda">Rwanda</option>';
                                accStatusTemplate += '<option value="Saint Kitts and Nevis">Saint Kitts and Nevis</option>';
                                accStatusTemplate += '<option value="Saint LUCIA">Saint LUCIA</option>';
                                accStatusTemplate += '<option value="Saint Vincent and the Grenadines">Saint Vincent and the Grenadines</option>';
                                accStatusTemplate += '<option value="Samoa">Samoa</option>';
                                accStatusTemplate += '<option value="San Marino">San Marino</option>';
                                accStatusTemplate += '<option value="Sao Tome and Principe">Sao Tome and Principe</option>';
                                accStatusTemplate += '<option value="Saudi Arabia">Saudi Arabia</option>';
                                accStatusTemplate += '<option value="Senegal">Senegal</option>';
                                accStatusTemplate += '<option value="Seychelles">Seychelles</option>';
                                accStatusTemplate += '<option value="Sierra Leone">Sierra Leone</option>';
                                accStatusTemplate += '<option value="Singapore">Singapore</option>';
                                accStatusTemplate += '<option value="Slovakia (Slovak Republic)">Slovakia (Slovak Republic)</option>';
                                accStatusTemplate += '<option value="Slovenia">Slovenia</option>';
                                accStatusTemplate += '<option value="Solomon Islands">Solomon Islands</option>';
                                accStatusTemplate += '<option value="Somalia">Somalia</option>';
                                accStatusTemplate += '<option value="South Africa">South Africa</option>';
                                accStatusTemplate += '<option value="South Georgia and the South Sandwich Islands">South Georgia and the South Sandwich Islands</option>';
                                accStatusTemplate += '<option value="Spain">Spain</option>';
                                accStatusTemplate += '<option value="Sri Lanka">Sri Lanka</option>';
                                accStatusTemplate += '<option value="St. Helena">St. Helena</option>';
                                accStatusTemplate += '<option value="St. Pierre and Miquelon">St. Pierre and Miquelon</option>';
                                accStatusTemplate += '<option value="Sudan">Sudan</option>';
                                accStatusTemplate += '<option value="Suriname">Suriname</option>';
                                accStatusTemplate += '<option value="Svalbard and Jan Mayen Islands">Svalbard and Jan Mayen Islands</option>';
                                accStatusTemplate += '<option value="Swaziland">Swaziland</option>';
                                accStatusTemplate += '<option value="Sweden">Sweden</option>';
                                accStatusTemplate += '<option value="Switzerland">Switzerland</option>';
                                accStatusTemplate += '<option value="Syrian Arab Republic">Syrian Arab Republic</option>';
                                accStatusTemplate += '<option value="Taiwan, Province of China">Taiwan, Province of China</option>';
                                accStatusTemplate += '<option value="TJ">Tajikistan</option>';
                                accStatusTemplate += '<option value="Tajikistan">Tajikistan</option>';
                                accStatusTemplate += '<option value="Tanzania, United Republic of">Tanzania, United Republic of</option>';
                                accStatusTemplate += '<option value="Thailand">Thailand</option>';
                                accStatusTemplate += '<option value="Togo">Togo</option>';
                                accStatusTemplate += '<option value="Tokelau">Tokelau</option>';
                                accStatusTemplate += '<option value="Tonga">Tonga</option>';
                                accStatusTemplate += '<option value="Trinidad and Tobago">Trinidad and Tobago</option>';
                                accStatusTemplate += '<option value="Tunisia">Tunisia</option>';
                                accStatusTemplate += '<option value="Turkey">Turkey</option>';
                                accStatusTemplate += '<option value="Turkmenistan">Turkmenistan</option>';
                                accStatusTemplate += '<option value="Turks and Caicos Islands">Turks and Caicos Islands</option>';
                                accStatusTemplate += '<option value="Tuvalu">Tuvalu</option>';
                                accStatusTemplate += '<option value="Uganda">Uganda</option>';
                                accStatusTemplate += '<option value="Ukraine">Ukraine</option>';
                                accStatusTemplate += '<option value="United Arab Emirates">United Arab Emirates</option>';
                                accStatusTemplate += '<option value="United Kingdom">United Kingdom</option>';
                                accStatusTemplate += '<option value="United States">United States</option>';
                                accStatusTemplate += '<option value="United States Minor Outlying Islands">United States Minor Outlying Islands</option>';
                                accStatusTemplate += '<option value="Uruguay">Uruguay</option>';
                                accStatusTemplate += '<option value="Uzbekistan">Uzbekistan</option>';
                                accStatusTemplate += '<option value="Vanuatu">Vanuatu</option>';
                                accStatusTemplate += '<option value="Venezuela">Venezuela</option>';
                                accStatusTemplate += '<option value="Viet Nam">Viet Nam</option>';
                                accStatusTemplate += '<option value="Virgin Islands (British)">Virgin Islands (British)</option>';
                                accStatusTemplate += '<option value="Virgin Islands (U.S.)">Virgin Islands (U.S.)</option>';
                                accStatusTemplate += '<option value="Wallis and Futuna Islands">Wallis and Futuna Islands</option>';
                                accStatusTemplate += '<option value="Western Sahara">Western Sahara</option>';
                                accStatusTemplate += '<option value="Yemen">Yemen</option>';
                                accStatusTemplate += '<option value="Zambia">Zambia</option>';
                                accStatusTemplate += '<option value="Zimbabwe">Zimbabwe</option>';
                            // by nikhil
                            //accStatusTemplate += '<option value="1">Select</option>';
//                            accStatusTemplate += '<option value="Afghanistan">Afghanistan</option>';
//                            accStatusTemplate += '<option value="Albania">Albania</option>';
//                            accStatusTemplate += '<option value="Algeria">Algeria</option>';
//                            accStatusTemplate += '<option value="AmericanSamoa">AmericanSamoa</option>';
//                            accStatusTemplate += '<option value="Andorra">Andorra</option>';
//                            accStatusTemplate += '<option value="Angola">Angola</option>';
//                            accStatusTemplate += '<option value="Anguilla">Anguilla</option>';
//                            accStatusTemplate += '<option value="Antarctica">Antarctica</option>';
//                            accStatusTemplate += '<option value="Argentina">Argentina</option>';
//                            accStatusTemplate += '<option value="Armenia">Armenia</option>';
//                            accStatusTemplate += '<option value="Aruba">Aruba</option>';
//                            accStatusTemplate += '<option value="Australia">Australia</option>';
//                            accStatusTemplate += '<option value="Austria">Austria</option>';
//                            accStatusTemplate += '<option value="Azerbaijan">Azerbaijan</option>';
//                            accStatusTemplate += '<option value="Bahamas">Bahamas</option>';
//                            accStatusTemplate += '<option value="Bahrain">Bahrain</option>';
//                            accStatusTemplate += '<option value="Bangladesh">Bangladesh</option>';
//                            accStatusTemplate += '<option value="Barbados">Barbados</option>';
//                            accStatusTemplate += '<option value="Belarus">Belarus</option>';
//                            accStatusTemplate += '<option value="Belgium">Belgium</option>';
//                            accStatusTemplate += '<option value="Belize">Belize</option>';
//                            accStatusTemplate += '<option value="Benin">Benin</option>';
//                            accStatusTemplate += '<option value="Bermuda">Bermuda</option>';
//                            accStatusTemplate += '<option value="Bhutan">Bhutan</option>';
//                            accStatusTemplate += '<option value="Bolivia">Bolivia</option>';
//                            accStatusTemplate += '<option value="BosniaandHerzegowina">BosniaandHerzegowina</option>';
//                            accStatusTemplate += '<option value="Botswana">Botswana</option>';
//                            accStatusTemplate += '<option value="BouvetIsland">BouvetIsland</option>';
//                            accStatusTemplate += '<option value="Brazil">Brazil</option>';
//                            accStatusTemplate += '<option value="BritishIndianOceanTerritory">BritishIndianOceanTerritory</option>';
//                            accStatusTemplate += '<option value="BruneiDarussalam">BruneiDarussalam</option>';
//                            accStatusTemplate += '<option value="Bulgaria">Bulgaria</option>';
//                            accStatusTemplate += '<option value="BurkinaFaso">BurkinaFaso</option>';
//                            accStatusTemplate += '<option value="Burundi">Burundi</option>';
//                            accStatusTemplate += '<option value="Cambodia">Cambodia</option>';
//                            accStatusTemplate += '<option value="Cameroon">Cameroon</option>';
//                            accStatusTemplate += '<option value="Canada">Canada</option>';
//                            accStatusTemplate += '<option value="CapeVerde">CapeVerde</option>';
//                            accStatusTemplate += '<option value="CaymanIslands">CaymanIslands</option>';
//                            accStatusTemplate += '<option value="CentralAfricanRepublic">CentralAfricanRepublic</option>';
//                            accStatusTemplate += '<option value="Chad">Chad</option>';
//                            accStatusTemplate += '<option value="Chile">Chile</option>';
//                            accStatusTemplate += '<option value="China">China</option>';
//                            accStatusTemplate += '<option value="ChristmasIsland">ChristmasIsland</option>';
//                            accStatusTemplate += '<option value="Cocos(Keeling)Islands">Cocos(Keeling)Islands</option>';
//                            accStatusTemplate += '<option value="Colombia">Colombia</option>';
//                            accStatusTemplate += '<option value="Comoros">Comoros</option>';
//                            accStatusTemplate += '<option value="Congo">Congo</option>';
//                            accStatusTemplate += '<option value="Congo,theDemocraticRepublicofthe">Congo,theDemocraticRepublicofthe</option>';
//                            accStatusTemplate += '<option value="CookIslands">CookIslands</option>';
//                            accStatusTemplate += '<option value="CostaRica">CostaRica</option>';
//                            accStatusTemplate += '<option value="CotedIvoire">CotedIvoire</option>';
//                            accStatusTemplate += '<option value="Croatia(Hrvatska)">Croatia(Hrvatska)</option>';
//                            accStatusTemplate += '<option value="Cuba">Cuba</option>';
//                            accStatusTemplate += '<option value="Cyprus">Cyprus</option>';
//                            accStatusTemplate += '<option value="CzechRepublic">CzechRepublic</option>';
//                            accStatusTemplate += '<option value="Denmark">Denmark</option>';
//                            accStatusTemplate += '<option value="Djibouti">Djibouti</option>';
//                            accStatusTemplate += '<option value="Dominica">Dominica</option>';
//                            accStatusTemplate += '<option value="DominicanRepublic">DominicanRepublic</option>';
//                            accStatusTemplate += '<option value="Ecuador">Ecuador</option>';
//                            accStatusTemplate += '<option value="Egypt">Egypt</option>';
//                            accStatusTemplate += '<option value="ElSalvador">ElSalvador</option>';
//                            accStatusTemplate += '<option value="EquatorialGuinea">EquatorialGuinea</option>';
//                            accStatusTemplate += '<option value="Eritrea">Eritrea</option>';
//                            accStatusTemplate += '<option value="Estonia">Estonia</option>';
//                            accStatusTemplate += '<option value="Ethiopia">Ethiopia</option>';
//                            accStatusTemplate += '<option value="FalklandIslands(Malvinas)">FalklandIslands(Malvinas)</option>';
//                            accStatusTemplate += '<option value="FaroeIslands">FaroeIslands</option>';
//                            accStatusTemplate += '<option value="Fiji">Fiji</option>';
//                            accStatusTemplate += '<option value="Finland">Finland</option>';
//                            accStatusTemplate += '<option value="France">France</option>';
//                            accStatusTemplate += '<option value="FrenchGuiana">FrenchGuiana</option>';
//                            accStatusTemplate += '<option value="PF">FrenchPolynesia</option>';
//                            accStatusTemplate += '<option value="TF">FrenchSouthernTerritories</option>';
//                            accStatusTemplate += '<option value="Gabon">Gabon</option>';
//                            accStatusTemplate += '<option value="Gambia">Gambia</option>';
//                            accStatusTemplate += '<option value="Georgia">Georgia</option>';
//                            accStatusTemplate += '<option value="Germany">Germany</option>';
//                            accStatusTemplate += '<option value="Ghana">Ghana</option>';
//                            accStatusTemplate += '<option value="Gibraltar">Gibraltar</option>';
//                            accStatusTemplate += '<option value="Greece">Greece</option>';
//                            accStatusTemplate += '<option value="Greenland">Greenland</option>';
//                            accStatusTemplate += '<option value="Grenada">Grenada</option>';
//                            accStatusTemplate += '<option value="Guadeloupe">Guadeloupe</option>';
//                            accStatusTemplate += '<option value="Guam">Guam</option>';
//                            accStatusTemplate += '<option value="Guatemala">Guatemala</option>';
//                            accStatusTemplate += '<option value="Guinea">Guinea</option>';
//                            accStatusTemplate += '<option value="Guinea-Bissau">Guinea-Bissau</option>';
//                            accStatusTemplate += '<option value="Guyan">Guyana</option>';
//                            accStatusTemplate += '<option value="Haiti">Haiti</option>';
//                            accStatusTemplate += '<option value="HeardandMcDonaldIslands">HeardandMcDonaldIslands</option>';
//                            accStatusTemplate += '<option value="HolySee(VaticanCityState)">HolySee(VaticanCityState)</option>';
//                            accStatusTemplate += '<option value="Honduras">Honduras</option>';
//                            accStatusTemplate += '<option value="HongKong">HongKong</option>';
//                            accStatusTemplate += '<option value="Hungary">Hungary</option>';
//                            accStatusTemplate += '<option value="Iceland">Iceland</option>';
//                            accStatusTemplate += '<option value="India">India</option>';
//                            accStatusTemplate += '<option value="Indonesia">Indonesia</option>';
//                            accStatusTemplate += '<option value="Iran(IslamicRepublicof)">Iran(IslamicRepublicof)</option>';
//                            accStatusTemplate += '<option value="Iraq">Iraq</option>';
//                            accStatusTemplate += '<option value="Ireland">Ireland</option>';
//                            accStatusTemplate += '<option value="Israel">Israel</option>';
//                            accStatusTemplate += '<option value="Italy">Italy</option>';
//                            accStatusTemplate += '<option value="Jamaica">Jamaica</option>';
//                            accStatusTemplate += '<option value="Japan">Japan</option>';
//                            accStatusTemplate += '<option value="Jordan">Jordan</option>';
//                            accStatusTemplate += '<option value="Kazakhstan">Kazakhstan</option>';
//                            accStatusTemplate += '<option value="Kenya">Kenya</option>';
//                            accStatusTemplate += '<option value="Kiribati">Kiribati</option>';
//                            accStatusTemplate += '<option value="Korea,DemocraticPeoplesRepublicof">Korea,DemocraticPeoplesRepublicof</option>';
//                            accStatusTemplate += '<option value="Korea,Republicof">Korea,Republicof</option>';
//                            accStatusTemplate += '<option value="Kuwait">Kuwait</option>';
//                            accStatusTemplate += '<option value="Kyrgyzstan">Kyrgyzstan</option>';
//                            accStatusTemplate += '<option value="LaoPeopleDemocraticRepublic">LaoPeopleDemocraticRepublic</option>';
//                            accStatusTemplate += '<option value="Latvia">Latvia</option>';
//                            accStatusTemplate += '<option value="Lebanon">Lebanon</option>';
//                            accStatusTemplate += '<option value="Lesotho">Lesotho</option>';
//                            accStatusTemplate += '<option value="Liberia">Liberia</option>';
//                            accStatusTemplate += '<option value="LibyanArabJamahiriya">LibyanArabJamahiriya</option>';
//                            accStatusTemplate += '<option value="Liechtenstein">Liechtenstein</option>';
//                            accStatusTemplate += '<option value="Lithuania">Lithuania</option>';
//                            accStatusTemplate += '<option value="Luxembourg">Luxembourg</option>';
//                            accStatusTemplate += '<option value="Macau">Macau</option>';
//                            accStatusTemplate += '<option value="Macedonia,TheFormerYugoslavRepublicof">Macedonia,TheFormerYugoslavRepublicof</option>';
//                            accStatusTemplate += '<option value="Madagascar">Madagascar</option>';
//                            accStatusTemplate += '<option value="Malawi">Malawi</option>';
//                            accStatusTemplate += '<option value="Malaysia">Malaysia</option>';
//                            accStatusTemplate += '<option value="Maldives">Maldives</option>';
//                            accStatusTemplate += '<option value="Mali">Mali</option>';
//                            accStatusTemplate += '<option value="Malta">Malta</option>';
//                            accStatusTemplate += '<option value="MarshallIslands">MarshallIslands</option>';
//                            accStatusTemplate += '<option value="Martinique">Martinique</option>';
//                            accStatusTemplate += '<option value="Mauritania">Mauritania</option>';
//                            accStatusTemplate += '<option value="Mauritius">Mauritius</option>';
//                            accStatusTemplate += '<option value="Mayotte">Mayotte</option>';
//                            accStatusTemplate += '<option value="Mexico">Mexico</option>';
//                            accStatusTemplate += '<option value="Micronesia,FederatedStatesof">Micronesia,FederatedStatesof</option>';
//                            accStatusTemplate += '<option value="Moldova,Republicof">Moldova,Republicof</option>';
//                            accStatusTemplate += '<option value="Monaco">Monaco</option>';
//                            accStatusTemplate += '<option value="Mongolia">Mongolia</option>';
//                            accStatusTemplate += '<option value="Montserrat">Montserrat</option>';
//                            accStatusTemplate += '<option value="Morocco">Morocco</option>';
//                            accStatusTemplate += '<option value="Mozambique">Mozambique</option>';
//                            accStatusTemplate += '<option value="Myanmar">Myanmar</option>';
//                            accStatusTemplate += '<option value="Namibia">Namibia</option>';
//                            accStatusTemplate += '<option value="Nauru">Nauru</option>';
//                            accStatusTemplate += '<option value="Nepal">Nepal</option>';
//                            accStatusTemplate += '<option value="Netherlands">Netherlands</option>';
//                            accStatusTemplate += '<option value="NetherlandsAntilles">NetherlandsAntilles</option>';
//                            accStatusTemplate += '<option value="NewCaledonia">NewCaledonia</option>';
//                            accStatusTemplate += '<option value="NewZealand">NewZealand</option>';
//                            accStatusTemplate += '<option value="Nicaragua">Nicaragua</option>';
//                            accStatusTemplate += '<option value="Niger">Niger</option>';
//                            accStatusTemplate += '<option value="Nigeria">Nigeria</option>';
//                            accStatusTemplate += '<option value="Niue">Niue</option>';
//                            accStatusTemplate += '<option value="NorfolkIsland">NorfolkIsland</option>';
//                            accStatusTemplate += '<option value="NorthernMarianaIslands">NorthernMarianaIslands</option>';
//                            accStatusTemplate += '<option value="Norway">Norway</option>';
//                            accStatusTemplate += '<option value="Oman">Oman</option>';
//                            accStatusTemplate += '<option value="Pakistan">Pakistan</option>';
//                            accStatusTemplate += '<option value="Palau">Palau</option>';
//                            accStatusTemplate += '<option value="Panama">Panama</option>';
//                            accStatusTemplate += '<option value="PapuaNewGuinea">PapuaNewGuinea</option>';
//                            accStatusTemplate += '<option value="Paraguay">Paraguay</option>';
//                            accStatusTemplate += '<option value="Peru">Peru</option>';
//                            accStatusTemplate += '<option value="Philippines">Philippines</option>';
//                            accStatusTemplate += '<option value="Pitcairn">Pitcairn</option>';
//                            accStatusTemplate += '<option value="Poland">Poland</option>';
//                            accStatusTemplate += '<option value="Portugal">Portugal</option>';
//                            accStatusTemplate += '<option value="PuertoRico">PuertoRico</option>';
//                            accStatusTemplate += '<option value="Qatar">Qatar</option>';
//                            accStatusTemplate += '<option value="Reunion">Reunion</option>';
//                            accStatusTemplate += '<option value="Romania">Romania</option>';
//                            accStatusTemplate += '<option value="RussianFederation">RussianFederation</option>';
//                            accStatusTemplate += '<option value="Rwanda">Rwanda</option>';
//                            accStatusTemplate += '<option value="SaintKittsandNevis">SaintKittsandNevis</option>';
//                            accStatusTemplate += '<option value="SaintLUCIA">SaintLUCIA</option>';
//                            accStatusTemplate += '<option value="SaintVincentandtheGrenadines">SaintVincentandtheGrenadines</option>';
//                            accStatusTemplate += '<option value="Samoa">Samoa</option>';
//                            accStatusTemplate += '<option value="SanMarino">SanMarino</option>';
//                            accStatusTemplate += '<option value="SaoTomeandPrincipe">SaoTomeandPrincipe</option>';
//                            accStatusTemplate += '<option value="SaudiArabia">SaudiArabia</option>';
//                            accStatusTemplate += '<option value="Senegal">Senegal</option>';
//                            accStatusTemplate += '<option value="Seychelles">Seychelles</option>';
//                            accStatusTemplate += '<option value="SierraLeone">SierraLeone</option>';
//                            accStatusTemplate += '<option value="SierraLeone">Singapore</option>';
//                            accStatusTemplate += '<option value="Slovakia(SlovakRepublic)">Slovakia(SlovakRepublic)</option>';
//                            accStatusTemplate += '<option value="Slovenia">Slovenia</option>';
//                            accStatusTemplate += '<option value="SolomonIslands">SolomonIslands</option>';
//                            accStatusTemplate += '<option value="Somalia">Somalia</option>';
//                            accStatusTemplate += '<option value="SouthAfrica">SouthAfrica</option>';
//                            accStatusTemplate += '<option value="SouthGeorgiaandtheSouthSandwichIslands">SouthGeorgiaandtheSouthSandwichIslands</option>';
//                            accStatusTemplate += '<option value="Spain">Spain</option>';
//                            accStatusTemplate += '<option value="SriLanka">SriLanka</option>';
//                            accStatusTemplate += '<option value="St.Helena">St.Helena</option>';
//                            accStatusTemplate += '<option value="St.PierreandMiquelon">St.PierreandMiquelon</option>';
//                            accStatusTemplate += '<option value="Sudan">Sudan</option>';
//                            accStatusTemplate += '<option value="Suriname">Suriname</option>';
//                            accStatusTemplate += '<option value="SvalbardandJanMayenIslands">SvalbardandJanMayenIslands</option>';
//                            accStatusTemplate += '<option value="Swaziland">Swaziland</option>';
//                            accStatusTemplate += '<option value="Sweden">Sweden</option>';
//                            accStatusTemplate += '<option value="Switzerland">Switzerland</option>';
//                            accStatusTemplate += '<option value="SyrianArabRepublic">SyrianArabRepublic</option>';
//                            accStatusTemplate += '<option value="Taiwan,ProvinceofChina">Taiwan,ProvinceofChina</option>';
//                            accStatusTemplate += '<option value="Tajikistan">Tajikistan</option>';
//                            accStatusTemplate += '<option value="Tanzania,UnitedRepublicof">Tanzania,UnitedRepublicof</option>';
//                            accStatusTemplate += '<option value="Thailand">Thailand</option>';
//                            accStatusTemplate += '<option value="Togo">Togo</option>';
//                            accStatusTemplate += '<option value="Tokelau">Tokelau</option>';
//                            accStatusTemplate += '<option value="Tonga">Tonga</option>';
//                            accStatusTemplate += '<option value="TrinidadandTobago">TrinidadandTobago</option>';
//                            accStatusTemplate += '<option value="Tunisia">Tunisia</option>';
//                            accStatusTemplate += '<option value="Turkey">Turkey</option>';
//                            accStatusTemplate += '<option value="Turkmenistan">Turkmenistan</option>';
//                            accStatusTemplate += '<option value="TurksandCaicosIslands">TurksandCaicosIslands</option>';
//                            accStatusTemplate += '<option value="Tuvalu">Tuvalu</option>';
//                            accStatusTemplate += '<option value="Uganda">Uganda</option>';
//                            accStatusTemplate += '<option value="Ukraine">Ukraine</option>';
//                            accStatusTemplate += '<option value="UnitedArabEmirates">UnitedArabEmirates</option>';
//                            accStatusTemplate += '<option value="UnitedKingdom">UnitedKingdom</option>';
//                            accStatusTemplate += '<option value="UnitedStates">UnitedStates</option>';
//                            accStatusTemplate += '<option value="UnitedStatesMinorOutlyingIslands">UnitedStatesMinorOutlyingIslands</option>';
//                            accStatusTemplate += '<option value="Uruguay">Uruguay</option>';
//                            accStatusTemplate += '<option value="Uzbekistan">Uzbekistan</option>';
//                            accStatusTemplate += '<option value="Vanuatu">Vanuatu</option>';
//                            accStatusTemplate += '<option value="Venezuela">Venezuela</option>';
//                            accStatusTemplate += '<option value="VietNam">VietNam</option>';
//                            accStatusTemplate += '<option value="VirginIslands(British)">VirginIslands(British)</option>';
//                            accStatusTemplate += '<option value="VirginIslands(U.S.)">VirginIslands(U.S.)</option>';
//                            accStatusTemplate += '<option value="WallisandFutunaIslands">WallisandFutunaIslands</option>';
//                            accStatusTemplate += '<option value="WesternSahara">WesternSahara</option>';
//                            accStatusTemplate += '<option value="Yemen">Yemen</option>';
//                            accStatusTemplate += '<option value="Zambia">Zambia</option>';
//                            accStatusTemplate += '<option value="Zimbabwe">Zimbabwe</option>';
                            //ended by nikhil
                            accStatusTemplate += '</select>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '<div class="row">';
                            accStatusTemplate += '<div class="col-xs-6">';
                            accStatusTemplate += '<div class="form-group">';
                            accStatusTemplate += '<label class="col-xs-4 control-label">Zip Code</label>';
                            accStatusTemplate += '<div class="col-xs-8">';
                            accStatusTemplate += '<input type="text" class="form-control" placeholder="" value="' + zipCode + '" id="id_zipCode">';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '<div class="col-xs-6">';
                            accStatusTemplate += '<div class="form-group">';
                            accStatusTemplate += '<label class="col-xs-4 control-label required">Region</label>';
                            accStatusTemplate += '<div class="col-xs-8">';
//    accStatusTemplate += '<select name="region" id="select2_region" class="form-control select2">';
//    accStatusTemplate += '<option value="0">Select</option>';
//    accStatusTemplate += '<option value="1">Mumbai</option>';
//    accStatusTemplate += '<option value="2">North</option>';
//    accStatusTemplate += '<option value="3">South</option>';
//    accStatusTemplate += '<option value="4">West</option>';
//    accStatusTemplate += '<option value="5">Gujarat</option>';
//    accStatusTemplate += '<option value="6">East</option>';
//    accStatusTemplate += '<option value="7">International</option>';
//    accStatusTemplate += '</select>';
// accStatusTemplate += '<div class="col-md-6">';
                            accStatusTemplate += '<select name="region" id="select2_region" class="form-control select2">';
//                            accStatusTemplate += '<option value="0">--Select Region--</option>';
                            $.each(accountRegions, function (i, data) {

                                console.log("data.id@ "+data.id);
                                console.log("data.region_name"+data.region_name);

                                accStatusTemplate += '<option value="' + data.id + '">' + data.region_name + '</option>';
                                //$('#'+id.date+' td.details-control').trigger( 'click' );
                            });
                            accStatusTemplate += '</select>';
//    accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '<div class="row">';
                            accStatusTemplate += '<div class="col-xs-6">';
                            accStatusTemplate += '<div class="form-group">';
                            accStatusTemplate += '<label class="col-xs-4 control-label">Website</label>';
                            accStatusTemplate += '<div class="col-xs-8">';
                            accStatusTemplate += '<input type="text" class="form-control" placeholder="" value="' + website + '" id="id_website">';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '<div class="col-xs-6">	';
                            accStatusTemplate += '<div class="form-group">';
                            accStatusTemplate += '<label class="col-xs-4 control-label">Industry</label>';
                            accStatusTemplate += '<div class="col-xs-8">';
//                            accStatusTemplate += '<input type="text" class="form-control" placeholder="" value="' + industry + '" id="id_industry">';
accStatusTemplate += '<select name="industry" id="Industry_list" class="form-control">';
                                                                        accStatusTemplate += '<option value="">Choose industry...</option>' ;
                                                                        accStatusTemplate += '<option value="Accounting">Accounting</option>' ;
                                                                        accStatusTemplate += '<option value="Airlines/Aviatio">Airlines/Aviation</option>' ;
                                                                        accStatusTemplate += '<option value="Alternative Dispute Resolution">Alternative Dispute Resolution</option>' ;
                                                                        accStatusTemplate += '<option value="Alternative Medicine">Alternative Medicine</option>' ;
                                                                        accStatusTemplate += '<option value="Animation">Animation</option>' ;
                                                                        accStatusTemplate += '<option value="Apparel &amp; Fashion">Apparel &amp; Fashion</option>' ;
                                                                        accStatusTemplate += '<option value="Architecture &amp; Planning">Architecture &amp; Planning</option>' ;
                                                                        accStatusTemplate += '<option value="Arts and Crafts">Arts and Crafts</option>' ;
                                                                        accStatusTemplate += '<option value="Automotive">Automotive</option>' ;
                                                                        accStatusTemplate += '<option value="Aviation &amp; Aerospace">Aviation &amp; Aerospace</option>' ;
                                                                        accStatusTemplate += '<option value="Banking">Banking</option>' ;
                                                                        accStatusTemplate += '<option value="Biotechnology">Biotechnology</option>' ;
                                                                        accStatusTemplate += '<option value="Broadcast Media">Broadcast Media</option>' ;
                                                                        accStatusTemplate += '<option value="Building Materials">Building Materials</option>' ;
                                                                        accStatusTemplate += '<option value="Business Supplies and Equipment">Business Supplies and Equipment</option>' ;
                                                                        accStatusTemplate += '<option value="Capital Markets">Capital Markets</option>' ;
                                                                        accStatusTemplate += '<option value="Chemicals">Chemicals</option>' ;
                                                                        accStatusTemplate += '<option value="Civic &amp; Social Organization">Civic &amp; Social Organization</option>' ;
                                                                        accStatusTemplate += '<option value="Civil Engineering">Civil Engineering</option>' ;
                                                                        accStatusTemplate += '<option value="Commercial Real Estate">Commercial Real Estate</option>' ;
                                                                        accStatusTemplate += '<option value="Computer &amp; Network Security">Computer &amp; Network Security</option>' ;
                                                                        accStatusTemplate += '<option value="Computer Games">Computer Games</option>' ;
                                                                        accStatusTemplate += '<option value="Computer Hardware">Computer Hardware</option>' ;
                                                                        accStatusTemplate += '<option value="Computer Networking">Computer Networking</option>' ;
                                                                        accStatusTemplate += '<option value="Computer Software">Computer Software</option>' ;
                                                                        accStatusTemplate += '<option value="Construction">Construction</option>' ;
                                                                        accStatusTemplate += '<option value="Consumer Electronics">Consumer Electronics</option>' ;
                                                                        accStatusTemplate += '<option value="Consumer Goods">Consumer Goods</option>' ;
                                                                        accStatusTemplate += '<option value="Consumer Services">Consumer Services</option>' ;
                                                                        accStatusTemplate += '<option value="Cosmetics">Cosmetics</option>' ;
                                                                        accStatusTemplate += '<option value="Dairy">Dairy</option>' ;
                                                                        accStatusTemplate += '<option value="Defense &amp; Space">Defense &amp; Space</option>' ;
                                                                        accStatusTemplate += '<option value="Design">Design</option>' ;
                                                                        accStatusTemplate += '<option value="Education Management">Education Management</option>' ;
                                                                        accStatusTemplate += '<option value="E-Learning">E-Learning</option>' ;
                                                                        accStatusTemplate += '<option value="Electrical/Electronic Manufacturing">Electrical/Electronic Manufacturing</option>' ;
                                                                        accStatusTemplate += '<option value="Entertainment">Entertainment</option>' ;
                                                                        accStatusTemplate += '<option value="Environmental Services">Environmental Services</option>' ;
                                                                        accStatusTemplate += '<option value="Events Services">Events Services</option>' ;
                                                                        accStatusTemplate += '<option value="Executive Office">Executive Office</option>' ;
                                                                        accStatusTemplate += '<option value="Facilities Services">Facilities Services</option>' ;
                                                                        accStatusTemplate += '<option value="Farming">Farming</option>' ;
                                                                        accStatusTemplate += '<option value="Financial Services">Financial Services</option>' ;
                                                                        accStatusTemplate += '<option value="Fine Art">Fine Art</option>' ;
                                                                        accStatusTemplate += '<option value="Fishery">Fishery</option>' ;
                                                                        accStatusTemplate += '<option value="Food &amp; Beverages">Food &amp; Beverages</option>' ;
                                                                        accStatusTemplate += '<option value="Food Production">Food Production</option>' ;
                                                                        accStatusTemplate += '<option value="Fund-Raising">Fund-Raising</option>' ;
                                                                        accStatusTemplate += '<option value="Furniture">Furniture</option>' ;
                                                                        accStatusTemplate += '<option value="Gambling &amp; Casinos">Gambling &amp; Casinos</option>' ;
                                                                        accStatusTemplate += '<option value="Glass, Ceramics &amp; Concrete">Glass, Ceramics &amp; Concrete</option>' ;
                                                                        accStatusTemplate += '<option value="Government Administration">Government Administration</option>' ;
                                                                        accStatusTemplate += '<option value="Government Relations">Government Relations</option>' ;
                                                                        accStatusTemplate += '<option value="Graphic Design">Graphic Design</option>' ;
                                                                        accStatusTemplate += '<option value="Health, Wellness and Fitness">Health, Wellness and Fitness</option>' ;
                                                                        accStatusTemplate += '<option value="Higher Education">Higher Education</option>' ;
                                                                        accStatusTemplate += '<option value="Hospital &amp; Health Care">Hospital &amp; Health Care</option>' ;
                                                                        accStatusTemplate += '<option value="Hospitality">Hospitality</option>' ;
                                                                        accStatusTemplate += '<option value="Human Resources">Human Resources</option>' ;
                                                                        accStatusTemplate += '<option value="Import and Export">Import and Export</option>' ;
                                                                        accStatusTemplate += '<option value="Individual &amp; Family Services">Individual &amp; Family Services</option>' ;
                                                                        accStatusTemplate += '<option value="Individual &amp; Family Services">Industrial Automation</option>' ;
                                                                        accStatusTemplate += '<option value="Information Services">Information Services</option>' ;
                                                                        accStatusTemplate += '<option value="Information Technology and Services">Information Technology and Services</option>' ;
                                                                        accStatusTemplate += '<option value="Insurance">Insurance</option>' ;
                                                                        accStatusTemplate += '<option value="International Affairs">International Affairs</option>' ;
                                                                        accStatusTemplate += '<option value="International Trade and Development">International Trade and Development</option>' ;
                                                                        accStatusTemplate += '<option value="Internet">Internet</option>' ;
                                                                        accStatusTemplate += '<option value="Investment Banking">Investment Banking</option>' ;
                                                                        accStatusTemplate += '<option value="Investment Management">Investment Management</option>' ;
                                                                        accStatusTemplate += '<option value="Judiciar">Judiciary</option>' ;
                                                                        accStatusTemplate += '<option value="Law Enforcement">Law Enforcement</option>' ;
                                                                        accStatusTemplate += '<option value="Law Practice">Law Practice</option>' ;
                                                                        accStatusTemplate += '<option value="Legal Services">Legal Services</option>' ;
                                                                        accStatusTemplate += '<option value="Legislative Office">Legislative Office</option>' ;
                                                                        accStatusTemplate += '<option value="Leisure, Travel &amp; Tourism">Leisure, Travel &amp; Tourism</option>' ;
                                                                        accStatusTemplate += '<option value="Libraries">Libraries</option>' ;
                                                                        accStatusTemplate += '<option value="Logistics and Supply Chain">Logistics and Supply Chain</option>' ;
                                                                        accStatusTemplate += '<option value="Luxury Goods &amp; Jewelry">Luxury Goods &amp; Jewelry</option>' ;
                                                                        accStatusTemplate += '<option value="Machinery">Machinery</option>' ;
                                                                        accStatusTemplate += '<option value="Management Consulting">Management Consulting</option>' ;
                                                                        accStatusTemplate += '<option value="Maritim">Maritime</option>' ;
                                                                        accStatusTemplate += '<option value="Marketing and Advertising">Marketing and Advertising</option>' ;
                                                                        accStatusTemplate += '<option value="Market Research">Market Research</option>' ;
                                                                        accStatusTemplate += '<option value="Mechanical or Industrial Engineering">Mechanical or Industrial Engineering</option>' ;
                                                                        accStatusTemplate += '<option value="Media Production">Media Production</option>' ;
                                                                        accStatusTemplate += '<option value="Medical Devices">Medical Devices</option>' ;
                                                                        accStatusTemplate += '<option value="Medical Practice">Medical Practice</option>' ;
                                                                        accStatusTemplate += '<option value="Mental Health Care">Mental Health Care</option>' ;
                                                                        accStatusTemplate += '<option value="Military">Military</option>' ;
                                                                        accStatusTemplate += '<option value="Mining &amp; Metals">Mining &amp; Metals</option>' ;
                                                                        accStatusTemplate += '<option value="Motion Pictures and Film">Motion Pictures and Film</option>' ;
                                                                        accStatusTemplate += '<option value="Museums and Institutions">Museums and Institutions</option>' ;
                                                                        accStatusTemplate += '<option value="Music">Music</option>' ;
                                                                        accStatusTemplate += '<option value="Nanotechnology">Nanotechnology</option>' ;
                                                                        accStatusTemplate += '<option value="Newspapers">Newspapers</option>' ;
                                                                        accStatusTemplate += '<option value="Nonprofit Organization Managemen">Nonprofit Organization Management</option>' ;
                                                                        accStatusTemplate += '<option value="Oil &amp; Energy">Oil &amp; Energy</option>' ;
                                                                        accStatusTemplate += '<option value="Online Media">Online Media</option>' ;
                                                                        accStatusTemplate += '<option value="Outsourcing/Offshoring">Outsourcing/Offshoring</option>' ;
                                                                        accStatusTemplate += '<option value="Package/Freight Delivery">Package/Freight Delivery</option>' ;
                                                                        accStatusTemplate += '<option value="Packaging and Containers">Packaging and Containers</option>' ;
                                                                        accStatusTemplate += '<option value="Paper &amp; Forest Products">Paper &amp; Forest Products</option>' ;
                                                                        accStatusTemplate += '<option value="Performing Arts">Performing Arts</option>' ;
                                                                        accStatusTemplate += '<option value="Pharmaceuticals">Pharmaceuticals</option>' ;
                                                                        accStatusTemplate += '<option value="Philanthropy">Philanthropy</option>' ;
                                                                        accStatusTemplate += '<option value="Photography">Photography</option>' ;
                                                                        accStatusTemplate += '<option value="Plastics">Plastics</option>' ;
                                                                        accStatusTemplate += '<option value="Political Organization">Political Organization</option>' ;
                                                                        accStatusTemplate += '<option value="Primary/Secondary Education">Primary/Secondary Education</option>' ;
                                                                        accStatusTemplate += '<option value="Printing">Printing</option>' ;
                                                                        accStatusTemplate += '<option value="Professional Training &amp; Coaching">Professional Training &amp; Coaching</option>' ;
                                                                        accStatusTemplate += '<option value="Program Development">Program Development</option>' ;
                                                                        accStatusTemplate += '<option value="Public Policy">Public Policy</option>' ;
                                                                        accStatusTemplate += '<option value="Public Relations and Communications">Public Relations and Communications</option>' ;
                                                                        accStatusTemplate += '<option value="Public Safety">Public Safety</option>' ;
                                                                        accStatusTemplate += '<option value="Publishing">Publishing</option>' ;
                                                                        accStatusTemplate += '<option value="Railroad Manufacture">Railroad Manufacture</option>' ;
                                                                        accStatusTemplate += '<option value="Ranching">Ranching</option>' ;
                                                                        accStatusTemplate += '<option value="Real Estate">Real Estate</option>' ;
                                                                        accStatusTemplate += '<option value="Recreational Facilities and Services">Recreational Facilities and Services</option>' ;
                                                                        accStatusTemplate += '<option value="Religious Institutions">Religious Institutions</option>' ;
                                                                        accStatusTemplate += '<option value="Renewables &amp; Environment">Renewables &amp; Environment</option>' ;
                                                                        accStatusTemplate += '<option value="Research">Research</option>' ;
                                                                        accStatusTemplate += '<option value="Restaurants">Restaurants</option>' ;
                                                                        accStatusTemplate += '<option value="Retail">Retail</option>' ;
                                                                        accStatusTemplate += '<option value="Security and Investigations">Security and Investigations</option>' ;
                                                                        accStatusTemplate += '<option value="Semiconductors">Semiconductors</option>' ;
                                                                        accStatusTemplate += '<option value="Shipbuilding">Shipbuilding</option>' ;
                                                                        accStatusTemplate += '<option value="Sporting Goods">Sporting Goods</option>' ;
                                                                        accStatusTemplate += '<option value="Sports">Sports</option>' ;
                                                                        accStatusTemplate += '<option value="Staffing and Recruiting">Staffing and Recruiting</option>' ;
                                                                        accStatusTemplate += '<option value="Supermarkets">Supermarkets</option>' ;
                                                                        accStatusTemplate += '<option value="Telecommunications">Telecommunications</option>' ;
                                                                        accStatusTemplate += '<option value="Textiles">Textiles</option>' ;
                                                                        accStatusTemplate += '<option value="Think Tanks">Think Tanks</option>' ;
                                                                        accStatusTemplate += '<option value="Tobacco">Tobacco</option>' ;
                                                                        accStatusTemplate += '<option value="Translation and Localization">Translation and Localization</option>' ;
                                                                        accStatusTemplate += '<option value="Transportation/Trucking/Railroad">Transportation/Trucking/Railroad</option>' ;
                                                                        accStatusTemplate += '<option value="Utilities">Utilities</option>' ;
                                                                        accStatusTemplate += '<option value="Venture Capital &amp; Private Equity">Venture Capital &amp; Private Equity</option>' ;
                                                                        accStatusTemplate += '<option value="Veterinary">Veterinary</option>' ;
                                                                        accStatusTemplate += '<option value="Warehousing">Warehousing</option>' ;
                                                                        accStatusTemplate += '<option value="Wholesale">Wholesale</option>' ;
                                                                        accStatusTemplate += '<option value="Wine and Spirits">Wine and Spirits</option>' ;
                                                                        accStatusTemplate += '<option value="Wireless">Wireless</option>' ;
                                                                        accStatusTemplate += '<option value="Writing and Editing">Writing and Editing</option>' ;
                                                                     accStatusTemplate += '</select>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</form>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
//    accStatusTemplate += '<div class="modal-footer">';
//    accStatusTemplate += '<button type="button" data-dismiss="modal" class="btn btn-default">Close</button>';
//    accStatusTemplate += '<button type="button" data-dismiss="modal" class="btn btn-primary" onClick="ooount()">Save All</button>';
//    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="modal-footer">';
    accStatusTemplate += '<button type="button" data-dismiss="modal" class="btn btn-default" >Close</button>';
   // accStatusTemplate += '<button type="button" class="btn btn-default" id="reset" onClick="document.edit_account.reset();$(\'#select2_region\').val(\'' + regionId + '\');$(\'#select2_sample_modal_4\').val(\'' + country + '\');App.updateUniform();" >Reset</button>';
  //  accStatusTemplate += '<button type="button" data-toggle="modal" class="btn btn-primary" onClick="editAccDetails(\'' + accId + '\',\'' + userId + '\',\'' + allowOfflineAccount_State + '\')" >Save All</button>';
     accStatusTemplate += '<button type="button" data-toggle="modal" class="btn btn-primary" onClick="checkEditTempAdminSave(\'' + accId + '\',\'' + userId + '\',\'' + allowOfflineAccount_State + '\')" >Save All</button>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    $('#acc_cre').html(accStatusTemplate);
     $("#select2_sample_modal_4").val(country);
     $("#Industry_list").val(industry); // to fix the issue of industry, bug #30390
     console.log("regionId$$ "+regionId);
     var reg = document.getElementById("select2_region").value; 
     console.log("reg "+reg);
     $("#select2_region").val(regionId);
    App.initUniform();
    App.fixContentHeight();
    $('.date-picker').datepicker({
        rtl: App.isRTL(),
        autoclose: true
    });
    
    TableEditable.init();

//                            var value = $('#start_date').val();
//                            var value1 = $('#date_value').val();
////                            $('#responsive2').html(accStatusTemplate);
//                            $('#responsive2').html(accStatusTemplate);
//                            $("#select2_sample_modal_4").val(country);
//                            $("#select2_region").val(regionId);
//                            App.initUniform();
//                            App.callHandleScrollers();
//                            App.fixContentHeight();
//                            $('.date-picker').datepicker({
//                                rtl: App.isRTL(),
//                                autoclose: true
//                            });
//                            jQuery('.tooltips').tooltip();
                        }
                    }
                }); // end of second ajax call                
                $('#acc_cre').html();
                App.initUniform();
                App.fixContentHeight();
                jQuery('.tooltips').tooltip();
                $('#pleaseWaitDialog').modal('hide');
            } else {
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}
// ended by nikhil
function testing() {
//    alert("testing");
    $('#responsive2').html("testing");
}
;
function editAccValidation()
{
    // alert("createAcoount");
//    //Editing for start date feature.
    var startDate = $("#start_date").val();
    //coming
    var expiredOn = $("#expiry_date").val();
    // alert("expiredOn "+expiredOn);//coming
    var sStartDate = dateConverter(startDate);
    //alert("startDate "+sStartDate);
    var sExpiredOn = dateConverter(expiredOn);
    //alert("sExpiredOn "+sExpiredOn);
    var companyName = document.getElementById("id_companyName").value.trim();
    //alert("companyName"+companyName);//coming
    var plan = document.getElementById("id_plan").options[document.getElementById("id_plan").selectedIndex].text;
    if (plan.length == 0 || plan == "Select") {
        fieldSenseTosterError("Plan should be selected .", true);
        return false;
    }
    var userLimit = document.getElementById("id_userLimit").value.trim();
    //alert("userLimit"+userLimit);//coming
    if (isPostiveInteger(userLimit) == false) {
        fieldSenseTosterError("Please Enter Valid User Limit .", true);
        return false;
    }
    if (userLimit < 25) {
        fieldSenseTosterError("Minimum User Limit should be 25.", true);
        return false;
    }

    if (startDate === "" || startDate === " " || startDate === 0)
    {
        fieldSenseTosterError("Start Date should not be blank", true);
        return false;
    }
    if (expiredOn === "" || expiredOn === " " || expiredOn === 0)
    {
        fieldSenseTosterError(" Expiry Date should not be blank", true);
        return false;
    }
    if (sStartDate > sExpiredOn)
    {
        fieldSenseTosterError("Start date should not be greater than Expiry date", true);
        return false;
    }

    if (companyName.length == 0) {
        fieldSenseTosterError("Company Name can't be empty .", true);
        return false;
    }
    if (companyName.length > 50) {
        fieldSenseTosterError("Company Name can not be more than 50 characters", true);
        return false;
    }
    var phNo1 = document.getElementById("id_comPnNo1").value.trim();
    // alert("phNo1"+phNo1);//coming
    if (phNo1.length == 0) {
        fieldSenseTosterError("phone number can't be empty .", true);
        return false;
    }
    var isphNo1 = /^\d+$/.test(phNo1);
    if (!isphNo1) {
        fieldSenseTosterError("Invalid phone number.", true);
        return false;
    }
    var address1 = document.getElementById("id_address1").value.trim();
    //alert("address1"+address1);//coming
    if (address1.length == 0) {
        fieldSenseTosterError("Address can't be empty .", true);
        return false;
    }
    var city = document.getElementById("id_city").value.trim();
    //alert("city"+city);//coming
    if (city.length == 0) {
        fieldSenseTosterError("City can't be empty .", true);
        return false;
    }
//      var isCity = /^\d+$/.test(city);
//    if (isCity) {
//        fieldSenseTosterError("Invalid city name.", true);
//        return false;
//    }

    var state = document.getElementById("id_state").value.trim();
    //alert("state"+state);//coming
    if (state.length == 0) {

        fieldSenseTosterError("State can't be empty .", true);
        return false;
    }
//     var isState = /^\d+$/.test(state);
//    if (isState) {
//        fieldSenseTosterError("Invalid state name.", true);
//        return false;
//    }
    var indexOfCountry = document.getElementById("select2_sample_modal_4");
    var country = indexOfCountry.options[indexOfCountry.selectedIndex].text;
    //alert("country"+country);//coming
    if (country.length == 0 || country == "--Select Country--") {
        fieldSenseTosterError("Country should be selected .", true);
        return false;
    }
    var zipCode = document.getElementById("id_zipCode").value.trim();
    if (zipCode !== "")
    {
        var iszipCode = /^\d+$/.test(zipCode);
        if (!iszipCode) {
            fieldSenseTosterError("Invalid zip code.", true);
            return false;
        }
    }
    var regionId = document.getElementById("select2_region").options[document.getElementById("select2_region").selectedIndex].value;
    //alert("regionId ++"+regionId);
    if (regionId == 0) {
        fieldSenseTosterError("Please Select Region .", true);
        return false;
    }
    var company_website = document.getElementById("id_website").value.trim();
    if (company_website.length > 50) {
        fieldSenseTosterError("Website name can not be more than 50 characters", true);
        return false;
    }


    //var regionId = document.getElementById("select2_region").value.trim();




    //  var indexOfCountry = document.getElementById("select2_sample4");







    // alert("zipCode"+zipCode);//coming

    var address1 = document.getElem
    var isphNo1 = /^\d+$/.test(phNo1);
    if (!isphNo1) {
        fieldSenseTosterError("Invalid phone number.", true);
        return false;
    }
//    
    //  var status = document.getElementById("optionsRadios4").value.trim();
    // alert("status "+status);
//    if (mobile.trim().length != 0) {
//        if (!(/^\d+$/.test(mobile))) {
//            fieldSenseTosterError("Invalid Mobile number.", true);
//            return false;
//        } else if (mobile.trim().length > 20) {
//            fieldSenseTosterError("Mobile number should not be more than 20 digits", true);
//            return false;
//        }
//    }
    // var gender = document.getElementById("id_gender").options[document.getElementById("id_gender").selectedIndex].value;
    // alert("gender "+gender);


//  







//    var address1 = document.getElementById("id_address1").value.trim();
//    //alert("address1"+address1);//coming
//    if (address1.length == 0) {
//        fieldSenseTosterError("Address can't be empty .", true);
//        return false;
//    }
//
//    //var regionId = document.getElementById("select2_region").value.trim();
//    
//    var regionId = document.getElementById("select2_region").options[document.getElementById("select2_region").selectedIndex].value;
//    //alert("regionId ++"+regionId);
//    if (regionId == 0) {
//        fieldSenseTosterError("Please Select Region .", true);
//        return false;
//    }
//
//    var city = document.getElementById("id_city").value.trim();
//    //alert("city"+city);//coming
//    if (city.length == 0) {
//        fieldSenseTosterError("City can't be empty .", true);
//        return false;
//    }
//    var state = document.getElementById("id_state").value.trim();
//    //alert("state"+state);//coming
//    if (state.length == 0) {
//        fieldSenseTosterError("State can't be empty .", true);
//        return false;
//    }
//  //  var indexOfCountry = document.getElementById("select2_sample4");
//  
//    
//     var indexOfCountry = document.getElementById("select2_sample_modal_4");
//    
//    var country = indexOfCountry.options[indexOfCountry.selectedIndex].text;
//    //alert("country"+country);//coming
//    if (country.length == 0 || country == "--Select Country--") {
//        fieldSenseTosterError("Country should be selected .", true);
//        return false;
//    }
//   
//    
//    var zipCode = document.getElementById("id_zipCode").value.trim();
//     var iszipCode = /^\d+$/.test(zipCode);
//    if (!iszipCode) {
//        fieldSenseTosterError("Invalid zip code.", true);
//        return false;
//    }
//   // alert("zipCode"+zipCode);//coming
//   
//
//    var status = document.getElementById("optionsRadios4").value.trim();
//  // alert("status "+status);
////    if (mobile.trim().length != 0) {
////        if (!(/^\d+$/.test(mobile))) {
////            fieldSenseTosterError("Invalid Mobile number.", true);
////            return false;
////        } else if (mobile.trim().length > 20) {
////            fieldSenseTosterError("Mobile number should not be more than 20 digits", true);
////            return false;
////        }
////    }
//   // var gender = document.getElementById("id_gender").options[document.getElementById("id_gender").selectedIndex].value;
//   // alert("gender "+gender);
//   
//    
////    alert("plan++++"+plan);
////    if (gender == undefined) {
////        fieldSenseTosterError("Please select the gender.", true);
////        return false;
////    }
//    var company_website = document.getElementById("id_website").value.trim();
//     if (company_website.length > 50) {
//        fieldSenseTosterError("Company Name can not be more than 50 characters", true);
//        return false;
//    }
    return true;
}
//added by nikhil - 11th july 2017  
function editAccDetails(id, userId, offlineAccountState)
{
    console.log("deleteIds " + deleteIds.toString());
    if (deleteIds != null && deleteIds != '') {

        var jsonData = JSON.stringify(deleteIds);
        console.log("heklo " + jsonData);
        $.ajax({
            type: "PUT",
            url: fieldSenseURL + "/account/userAdmin/deleteAdminUser/",
            contentType: "application/json; charset=utf-8",
            headers: {
                "userToken": userToken,
            },
            crossDomain: false,
            cache: false,
            data: jsonData,
            dataType: 'json',
            success: function (data) {
                if (data.errorCode == '0000') {
                    //oTable.fnDeleteRow(nRow);
                    // fieldSenseTosterSuccess(data.errorMessage, true);
                    // $('#pleaseWaitDialog').modal('hide');
                    deleteIds.length = 0;
                } else {
                    // $('#pleaseWaitDialog').modal('hide');
                    fieldSenseTosterError(data.errorMessage, true);
                    //$('#responsive2').modal('hide');
                    FieldSenseInvalidToken(data.errorMessage);
                }
            }

        });
    }

    var startDate = $("#start_date").val();
    //coming
    var expiredOn = $("#expiry_date").val();
    // alert("expiredOn "+expiredOn);//coming
    var sStartDate = dateConverter(startDate);
    //alert("startDate "+sStartDate);
    var sExpiredOn = dateConverter(expiredOn);
    //alert("sExpiredOn "+sExpiredOn);
    if (startDate === "" || startDate === " " || startDate === 0)
    {
        fieldSenseTosterError("Start Date should not be blank", true);
        return false;
    }
    if (expiredOn === "" || expiredOn === " " || expiredOn === 0)
    {
        fieldSenseTosterError(" Expiry Date should not be blank", true);
        return false;
    }
    if (sStartDate > sExpiredOn)
    {
        fieldSenseTosterError("Start date should not be greater than Expiry date", true);
        return false;
    }
    var address1 = document.getElementById("id_address1").value.trim();
    //alert("address1"+address1);//coming
    if (address1.length == 0) {
        fieldSenseTosterError("Address can't be empty .", true);
        return false;
    }

    //var regionId = document.getElementById("select2_region").value.trim();

    var regionId = document.getElementById("select2_region").options[document.getElementById("select2_region").selectedIndex].value;
    //alert("regionId ++"+regionId);
    if (regionId == 0) {
        fieldSenseTosterError("Please Select Region .", true);
        return false;
    }

    var city = document.getElementById("id_city").value.trim();
    //alert("city"+city);//coming
    if (city.length == 0) {
        fieldSenseTosterError("City can't be empty .", true);
        return false;
    }
    var state = document.getElementById("id_state").value.trim();
    //alert("state"+state);//coming
    if (state.length == 0) {
        fieldSenseTosterError("State can't be empty .", true);
        return false;
    }
    //  var indexOfCountry = document.getElementById("select2_sample4");


    var indexOfCountry = document.getElementById("select2_sample_modal_4");
    var country = indexOfCountry.options[indexOfCountry.selectedIndex].text;
    //alert("country"+country);//coming
    if (country.length == 0 || country == "--Select Country--") {
        fieldSenseTosterError("Country should be selected .", true);
        return false;
    }
    var plan = document.getElementById("id_plan").options[document.getElementById("id_plan").selectedIndex].text;
    if (plan.length == 0 || plan == "Select") {
        fieldSenseTosterError("Plan should be selected .", true);
        return false;
    }

    var zipCode = document.getElementById("id_zipCode").value.trim();
    if (zipCode != '')
    {
        var iszipCode = /^\d+$/.test(zipCode);
        if (!iszipCode) {
            fieldSenseTosterError("Invalid zip code.", true);
            return false;
        }
    }
    // alert("zipCode"+zipCode);//coming
    var phNo1 = document.getElementById("id_comPnNo1").value.trim();
    // alert("phNo1"+phNo1);//coming
    if (phNo1.length == 0) {
        fieldSenseTosterError("phone number can't be empty .", true);
        return false;
    }
    var isphNo1 = /^\d+$/.test(phNo1);
    if (!isphNo1) {
        fieldSenseTosterError("Invalid phone number.", true);
        return false;
    }
//    
//    var status = document.getElementById("optionsRadios4").value.trim();
//    var accStatus = $('input[name=optionsRadios4]:checked', '#edit_account').val();
    //alert("status "+accStatus);
//    if (mobile.trim().length != 0) {
//        if (!(/^\d+$/.test(mobile))) {
//            fieldSenseTosterError("Invalid Mobile number.", true);
//            return false;
//        } else if (mobile.trim().length > 20) {
//            fieldSenseTosterError("Mobile number should not be more than 20 digits", true);
//            return false;
//        }
//    }
    // var gender = document.getElementById("id_gender").options[document.getElementById("id_gender").selectedIndex].value;
    // alert("gender "+gender);


//  
    var company_website = document.getElementById("id_website").value.trim();
    if (company_website.length > 50) {
        fieldSenseTosterError("Company Name can not be more than 50 characters", true);
        return false;
    }

    var companyName = document.getElementById("id_companyName").value.trim();
    //alert("companyName"+companyName);//coming
    if (companyName.length == 0) {
        fieldSenseTosterError("Company Name can't be empty .", true);
        return false;
    }
    if (companyName.length > 50) {
        fieldSenseTosterError("Company Name can not be more than 50 characters", true);
        return false;
    }
    var userLimit = document.getElementById("id_userLimit").value.trim();
    //alert("userLimit"+userLimit);//coming
    if (isPostiveInteger(userLimit) == false) {
        fieldSenseTosterError("Please Enter Valid User Limit .", true);
        return false;
    }
    if (userLimit < 25) {
        fieldSenseTosterError("Minimum User Limit should be 25.", true);
        return false;
    }
    if (startDate === "" || startDate === " " || startDate === 0)
    {
        fieldSenseTosterError("Start Date should not be blank", true);
        return false;
    }
    if (expiredOn === "" || expiredOn === " " || expiredOn === 0)
    {
        fieldSenseTosterError(" Expiry Date should not be blank", true);
        return false;
    }
    if (sStartDate > sExpiredOn)
    {
        fieldSenseTosterError("Start date should not be greater than Expiry date", true);
        return false;
    }
    var address1 = document.getElementById("id_address1").value.trim();
    //alert("address1"+address1);//coming
    if (address1.length == 0) {
        fieldSenseTosterError("Address can't be empty .", true);
        return false;
    }

    //var regionId = document.getElementById("select2_region").value.trim();

    var regionId = document.getElementById("select2_region").options[document.getElementById("select2_region").selectedIndex].value;
    //alert("regionId ++"+regionId);
    if (regionId == 0) {
        fieldSenseTosterError("Please Select Region .", true);
        return false;
    }

    var city = document.getElementById("id_city").value.trim();
    //alert("city"+city);//coming
    if (city.length == 0) {
        fieldSenseTosterError("City can't be empty .", true);
        return false;
    }
    var state = document.getElementById("id_state").value.trim();
    //alert("state"+state);//coming
    if (state.length == 0) {
        fieldSenseTosterError("State can't be empty .", true);
        return false;
    }
    //  var indexOfCountry = document.getElementById("select2_sample4");


    var indexOfCountry = document.getElementById("select2_sample_modal_4");
    var country = indexOfCountry.options[indexOfCountry.selectedIndex].text;
    //alert("country"+country);//coming
    if (country.length == 0 || country == "--Select Country--") {
        fieldSenseTosterError("Country should be selected .", true);
        return false;
    }
    var plan = document.getElementById("id_plan").options[document.getElementById("id_plan").selectedIndex].text;
    if (plan.length == 0 || plan == "Select") {
        fieldSenseTosterError("Plan should be selected .", true);
        return false;
    }

    var zipCode = document.getElementById("id_zipCode").value.trim();
    if (zipCode !== "")
    {
        var iszipCode = /^\d+$/.test(zipCode);
        if (!iszipCode) {
            fieldSenseTosterError("Invalid zip code.", true);
            return false;
        }
    }
    // alert("zipCode"+zipCode);//coming
    var phNo1 = document.getElementById("id_comPnNo1").value.trim();
    // alert("phNo1"+phNo1);//coming
    if (phNo1.length == 0) {
        fieldSenseTosterError("phone number can't be empty .", true);
        return false;
    }
    var isphNo1 = /^\d+$/.test(phNo1);
    if (!isphNo1) {
        fieldSenseTosterError("Invalid phone number.", true);
        return false;
    }

    if (document.getElementById("optionsRadios4").checked)
    {
        var status = document.getElementById("optionsRadios4").value;
        console.log("status +" + status);
    }
    if (document.getElementById("optionsRadios5").checked)
    {
        var status = document.getElementById("optionsRadios5").value;
        console.log("status 5 +" + status);
    }
    // alert("status "+status);
    // alert("status "+status);
//    if (mobile.trim().length != 0) {
//        if (!(/^\d+$/.test(mobile))) {
//            fieldSenseTosterError("Invalid Mobile number.", true);
//            return false;
//        } else if (mobile.trim().length > 20) {
//            fieldSenseTosterError("Mobile number should not be more than 20 digits", true);
//            return false;
//        }
//    }
    // var gender = document.getElementById("id_gender").options[document.getElementById("id_gender").selectedIndex].value;
    // alert("gender "+gender);


//    alert("plan++++"+plan);
//    if (gender == undefined) {
//        fieldSenseTosterError("Please select the gender.", true);
//        return false;
//    }
    var company_website = document.getElementById("id_website").value.trim();
    if (company_website.length > 50) {
        fieldSenseTosterError("Company Name can not be more than 50 characters", true);
        return false;
    }
    //  alert("id_website "+company_website);

//    var industry = document.getElementById("id_industry").value.trim();
    // alert("industry "+industry);
var indexOfIndustry = document.getElementById("Industry_list");
var industry = indexOfIndustry.options[indexOfIndustry.selectedIndex].value;
    ///////////////////////////
    // added by jyoti 21-12-2016
    var allowOffline_edit = 1;
    var valueAllowOffline_edit = document.getElementById("id_allowoffline_edit").checked;
    //    alert("valueAllowOffline_edit "+valueAllowOffline_edit);
    if (valueAllowOffline_edit == false) {
        allowOffline_edit = 0;
    }
    // ended by jyoti

    var tbl = $('#sample_editable_1 tbody tr').map(function (idxRow, ele) {
        //
        // start building the retVal object
        //
        var retVal = {id: ++idxRow};
        //
        // for each cell
        //
        tbl = $(ele).find('td').map(function (idxCell, ele) {
            var input = $(ele).find(':input');
            //
            // if cell contains an input or select....
            //
            if (input.length == 1) {
                var attr = $('#sample_editable_1 thead tr th').eq(idxCell).text();
                console.log("attribute if " + attr);
                if (attr == 'Username')
                {
                    attr = 'firstName';
                }
                if (attr == 'Email')
                {
                    attr = 'emailAddress';
                }
                if (attr == 'Gender')
                {
                    attr = 'gender';
                }
                if (attr == 'Re-type Password')
                {
                    attr = 'password';
                }
                if (attr == 'Designation ')
                {
                    attr = 'designation';
                }
                if (attr == 'Mobile No.')
                {
                    attr = 'mobileNo';
                }
                retVal[attr] = input.val();
                if (retVal[attr] == "2")
                {
                    retVal[attr] = "0";
                }
                console.log("gender dekh " + retVal[attr]);
            } else {
                var attr = $('#sample_editable_1 thead tr th').eq(idxCell).text();
                console.log("attribute else " + attr);
                if (attr == 'Username')
                {
                    attr = 'firstName';
                }
                if (attr == 'Email')
                {
                    attr = 'emailAddress';
                }
                if (attr == 'Designation')
                {
                    attr = 'designation';
                }
                if (attr == 'Mobile No.')
                {
                    attr = 'mobileNo';
                }
                retVal[attr] = $(ele).text();
                console.log("sab admin " + retVal[attr]);
            }
        });
        return retVal;
    }).get();
    console.log("only tbl " + JSON.stringify(tbl));
    $('#pleaseWaitDialog').modal('show');
//console.log("tbl 0 "+JSON.stringify(tbl).);
    var accountObject = {
        //added by nikhil
        "id": id,
        "plan": plan,
        "companyWebsite": company_website,
        "industry": industry,
        "status": status,
        "companyName": companyName,
        "userLimit": userLimit,
        "regionId": regionId,
        "address1": address1,
        "city": city,
        "state": state,
        "country": country,
        "zipCode": zipCode,
        "companyPhoneNumber1": parseFloat(phNo1),
        // "emailAddress": emailId,
        "strStartDate": sStartDate,
        "sExpiredOn": sExpiredOn,
        "allAdminUser": tbl

    }
    var jsonData = JSON.stringify(accountObject);
    console.log(jsonData);
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "PUT",
        url: fieldSenseURL + "/account/" + id,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken,
        },
        destroy: true,
        crossDomain: false,
        cache: false,
        data: jsonData,
        dataType: 'json',
        success: function (data) {
            if (data.errorCode == '0000') {
                fieldSenseTosterSuccess(data.errorMessage, true);
                var table = $('#sample_5').DataTable();
                table.draw(false);
//                $('#responsive2').modal('hide');
                $('#acc_cre').modal('hide');
                //alert("edit success");
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                //$('#responsive2').modal('hide');
                FieldSenseInvalidToken(data.errorMessage);
            }
            // added by jyoti 22-12-2016
            if (offlineAccountState !== allowOffline_edit) {

                var accountSettingsObject = {
                    "id": id,
                    "allowOffline": allowOffline_edit
                }

                var jsonData = JSON.stringify(accountSettingsObject);
                $.ajax({
                    type: "PUT",
                    url: fieldSenseURL + "/account/settings/values/" + id,
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

                    }

                });
            }  //ended by jyoti
        }

    });
//var oReturnedDatatable = TableEditable.getDataTable();
////alert("Here is the fucker!! : " +oReturnedDatatable)
//        
//	oReturnedDatatable.fnDestroy();

}
// ended by nikhil



function isPostiveInteger(str) {
    return /^\d+$/.test(str);
    ;
}

function dateConverter(dateBasicformat) {
    var dateSplit = dateBasicformat.split("/");
    var date = dateSplit[0];
    var month = dateSplit[1];
    var year = dateSplit[2];
    var toDate = convertLocalDateToServerDate(year, month - 1, date, 23, 59);
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
    var sExpireDate = toDate.getFullYear() + "-" + month + "-" + date + " " + hours + ":" + minutes + ':59';
    return sExpireDate;
}

function demoPage() {
    window.location = 'Try1.html';
}

function removeDiv() {
    alert("remove div2");
    $('#id_div').html("");
}
function resetCounter()
{
    var errorcode1;
    errorcode1 = '0000';
    verify(errorcode1);
    //alert("resetCounter");
}
function deleteCount(id) {
    console.log(id);
    deleteIds.push(id);
}
//function clearFilter()
//{
//    $('#id_accSearchText').html("");
//    $('#id_expiry_date').html("");
//    $('#id_start_date').html("");
//    
//}
