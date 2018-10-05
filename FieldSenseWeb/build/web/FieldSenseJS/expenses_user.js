/*
 * @Added by jyoti, 02-2018
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var intervalIndex = window.setInterval(function () {
    var userToken2 = fieldSenseGetCookie("userToken");
    try {
        if (userToken2.toString() == "undefined") {
            window.location.href = "login.html";
        } else if (loginUserId != 0) {
            loggedinUserImageData();
            loggedinUserData();
            teamMembers();
            getAllExpCat();
            getAllExpenses("reload");
            window.clearTimeout(intervalIndex);
        }
    } catch (err) {
        window.location.href = "login.html";
    }
}, 1000);

var expensearray = [];
var dicount = [];
var cust_start = 0;
var cust_length = 5;
var set_def_date = 0;
var expCatArray;

function teamMembers() {
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/team/userPositions/orderbyName/" + loginUserId,
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
                for (var i = 0; i < teamMemberData.length; i++) {
                    var userId = teamMemberData[i].user.id;
                    var firstName = teamMemberData[i].user.firstName;
                    var lastName = teamMemberData[i].user.lastName;
                    var fullName = firstName + ' ' + lastName;
                    teamMemberTemplate = '<option value="' + userId + '">' + fullName + '</option>';
                    $('#all_users_exp').append(teamMemberTemplate);
                }
            }
        }
    });
}

function getAllExpCat() {
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/expenseCategory/active",
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
                var expcatdata = data.result;
                expCatArray = data.result;
                var expcatdatatempl = '';
                for (var i = 0; i < expcatdata.length; i++) {
                    expcatdatatempl = '<option value="' + expcatdata[i].id + '">' + expcatdata[i].categoryName + '</option>';
                    $('#all_exp_cat').append(expcatdatatempl);
                }
            }
        }
    });
}

function getAllExpenses(action) {
    var teamMember = document.getElementById("all_users_exp").value.trim();
    var status = document.getElementById("exp_status").value.trim();
    var category = document.getElementById("all_exp_cat").value.trim();

    if (action == "reload" || action == "clear") {
        $(".daterangepicker > .ranges > ul > li:eq(4)").click();
        $("#s2id_all_users_exp > a").removeClass("select2-default");
        $("#s2id_all_users_exp > a> span:eq(0)").html("All Users");
        $("#all_exp_cat").val("All");
        $("#exp_status").val("0"); // to set default selected value on page load
        teamMember = "All";
        status = "0"; // to get all result where status 0
        category = "All";
    }
    if (teamMember == "") {
        if ($("#s2id_all_users_exp > a > span").text().trim() == "All Users") {
            teamMember = "All";
        } else {
            fieldSenseTosterError("Please select any user", true);
            return false;
        }
    }
    var totaltime = $("#reportrange >span").text().trim();

    totaltime = totaltime.replace(/Jan/g, "01");
    totaltime = totaltime.replace(/Feb/g, "02");
    totaltime = totaltime.replace(/Mar/g, "03");
    totaltime = totaltime.replace(/Apr/g, "04");
    totaltime = totaltime.replace(/May/g, "05");
    totaltime = totaltime.replace(/Jun/g, "06");
    totaltime = totaltime.replace(/Jul/g, "07");
    totaltime = totaltime.replace(/Aug/g, "08");
    totaltime = totaltime.replace(/Sep/g, "09");
    totaltime = totaltime.replace(/Oct/g, "10");
    totaltime = totaltime.replace(/Nov/g, "11");
    totaltime = totaltime.replace(/Dec/g, "12");

    totaltime = totaltime.split("-");
    var startTime = totaltime[0].trim();
    var endTime = totaltime[1].trim();

    var startTimeSplit = startTime.split(' ');
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
    var ensdTimeSplit = endTime.split(' ');
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

    var start = 0;
    var length = 5;
    cust_start = 0;
    cust_length = 5;
    var i = -1;
    var j = 0;
    var total_amts = 0;
    var all_total_amts = 0;
    var myTable = $("#example").DataTable();
    if (myTable != null)
        myTable.destroy();
    var expensetable = $('#example').dataTable({
        "bServerSide": true,
        "bDestroy": true,
        "ajax": {
            "url": fieldSenseURL + "/expense/userExpenses",
            "type": "GET",
            headers: {
                "userToken": userToken
            },
            contentType: "application/json; charset=utf-8",
            crossDomain: false,
            "data": {
                "userid": teamMember,
                "status": status,
                "expensecategory": category,
                "fromdate": fromDate,
                "todate": toDate,
            },
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
        "iDisplayLength": length, // It will be by default page width set by user
        "sPaginationType": "bootstrap_full_number",
        "bProcessing": false,
        "aLengthMenu": [
            [-1, 5, 10, 15, 20, 50, 100],
            ["All", 5, 10, 15, 20, 50, 100] // change per page values here
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
                "aTargets": [0, 7, 8]
            }
        ],
        "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            if (j == 0) {
                all_total_amts += aData.total_amounts;
            }
            j++;
            total_amts += aData.amountSpent;
            var expenseTime = convertToLocalTime(aData.expenseTime);
            expensearray.push({"expenseTime": expenseTime, "expense_data": aData});
        },
        "aoColumns": [// ahmed : here we populate the data in datatable that is returned using Ajax
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    i = i + 1;

                    if (full.status == 0 && full.user.id != loginUserId) {
                        return '<input type="checkbox" onclick="deSelectAll()" class="checkboxes" id="checkbox_' + i + '" value="' + full.id + '"/>';
                    } else {
                        return '<input type="checkbox" disabled="disabled" id="checkbox_' + i + '" class="checkboxes"/>';
                    }
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.user.fullname;
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.expenseName;
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.exp_category_name;
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.amountSpent;
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    var expenseTime = convertToLocalTime(full.expenseTime);
                    return expenseTime;
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    var customerNameWithLocation = full.customerName + " - " + full.appointment.customer.customerLocation;
                    if (full.customerName.length == 0) {
                        customerNameWithLocation = "";
                    }
                    return customerNameWithLocation;
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells status",
                "mRender": function (data, type, full) {
                    if (full.status == 0) {
                        return '<span class="label label-sm label-default" title="Pending Approval by Reporting Head">Pending</span>';
                    } else if (full.status == 1) {
                        return '<span class="label label-sm label-default" title="Pending Approval by Accounts">Pending</span>';
                    } else if (full.status == 2) {
                        return '<span class="label label-sm label-warning" title="Rejected by Reporting Head">Rejected</span>';
                    } else if (full.status == 3) {
                        return '<span class="label label-sm label-info" title="Approved">Approved</span>';
                    } else if (full.status == 4) {
                        return '<span class="label label-sm label-warning" title="Rejected by Accounts">Rejected</span>';
                    } else if (full.status == 5) {
                        return '<span class="label label-sm label-success" title="Disbursed">Disbursed</span>';
                    } else {
                        return '';
                    }
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    if (full.status == 0 && full.user.id == loginUserId) {
                        return '<button type="button" id="exp_tab' + i + '"  onclick="displayExpDetails(' + i + ')" class="btn btn-default btn-xs tooltips modal-toggle" data-toggle="modal" href="#expenses" data-placement="bottom" title="Details"><i class="fa fa-edit"></i></button><button type="button" id="exp_tab' + i + '"  onclick="deleteLoggedInUserPendingExpense(' + i + ')" class="btn btn-default btn-xs tooltips" data-toggle="modal" data-placement="bottom" title="Delete"><i class="fa fa-trash-o"></i></button>';
                    } else {
                        return '<button type="button" id="exp_tab' + i + '"  onclick="displayExpDetails(' + i + ')" class="btn btn-default btn-xs tooltips" data-toggle="modal" href="#expenses" data-placement="bottom" title="Details"><i class="fa fa-edit"></i></button>';
                    }
                }
            }
        ],
        "aaSorting": [],
        //"bFilter": true,
        "preDrawCallback": function (settings) {
            i = -1;
            expensearray = [];
            total_amts = 0;
            all_total_amts = 0;
            j = 0;
            var sdf = settings;
            if (document.getElementById("exp_status").value.trim() != "All") {
                if ($(".group-checkable").is(':checked') || $('.checkboxes').length == 1) {
                    if (cust_start != 0) {
                        settings._iDisplayStart = cust_start - cust_length;
                    }
                }
            }
            $("#example_info").hide();
            $("#example_length").parent().attr("class", "col-md-6 col-sm-12");
            $("#example_paginate").parent().attr("class", "col-md-6 col-sm-12");
            $('#pleaseWaitDialog').modal('show');
        },
        "fnDrawCallback": function (oSettings) {
            if (oSettings.fnDisplayEnd() > 0) {
                $("#id_totalCustRecords").html("<b>" + (oSettings._iDisplayStart + 1) + "</b> - <b>" + oSettings.fnDisplayEnd() + "</b> of <b>" + oSettings.fnRecordsTotal() + "</b> records");
            }
            cust_start = oSettings._iDisplayStart;
            cust_length = oSettings._iDisplayLength;
            expensetable.$('td').mouseover(function () {
                if ($(this).hasClass("status") == false) {
                    var sData = $(this).text();
                    $(this).attr('title', sData);
                }
            });

            $("#example").find('td').css({'max-width': '300px', 'overflow': 'hidden', 'text-overflow': 'ellipsis'});
            $("#example_filter >label >input").attr("class", "form-control input-medium");
            $("#example_filter >label").css({'position': 'absolute', 'margin-left': '65%', 'margin-top': '-35px'}); // styling search box
            $("#total_expenses").html('<i class="fa fa-inr fa-fw" aria-hidden="true"></i>' + total_amts + " ( of" + '<i class="fa fa-inr fa-fw" aria-hidden="true"></i>' + all_total_amts + " Total)");

            $(".group-checkable").attr('checked', false);
            $(".group-checkable").parent().removeClass('checked');
            $(".checkboxes").each(function () {
                $(this).attr('checked', false);
                $(this).parent().removeClass('checked');
            });
            $("#btnApprovepo").css("display", "none");
            App.initUniform();
            App.fixContentHeight();
            jQuery('.tooltips').tooltip();

            $('#pleaseWaitDialog').modal('hide');

        }
    });

    jQuery('#example_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
    jQuery('#example_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
    jQuery('#example_wrapper .dataTables_length select').select2(); // initialize select2 dropdown

    $("#example_filter >label >input").unbind();
    var table = $('#example').DataTable();
    $("#example_filter >label >input").bind('keyup', function (e) {
        if (e.keyCode == 13) {
            table.search($(this).val()).draw(  );
        }
    });

    if (status == 1 || status == 2 || status == 3 || status == 4 || status == 5) {
        table.column(0).visible(false);
    }

}

/**
 * @added by jyoti
 * @returns {undefined}
 */
function addNewExpenseTemplate() {

    var todayDateTime = moment().format('DD MMM YYYY , hh:mm a');

    var addNewExpenseTemplate = '';
    addNewExpenseTemplate += '<div class="modal-dialog modal-wide">';
    addNewExpenseTemplate += '<div class="modal-content">';

    addNewExpenseTemplate += '<div class="modal-header">';
    addNewExpenseTemplate += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
    addNewExpenseTemplate += '<h4 class="modal-title">Add Expense</h4>';
    addNewExpenseTemplate += '</div>';

    addNewExpenseTemplate += '<form action="#" class="form-horizontal form-row-seperated">';
    addNewExpenseTemplate += '<div class="modal-body">';
    addNewExpenseTemplate += '<div class="scroller" data-always-visible="1" data-rail-visible1="1">';
    addNewExpenseTemplate += '<div class="form-body">';

    addNewExpenseTemplate += '<div class="form-group" >';
    addNewExpenseTemplate += '<label class="col-md-4 control-label">Select visit </label>';
    addNewExpenseTemplate += '<div class="col-md-8">';
    addNewExpenseTemplate += '<div class="checkbox-list">';
    addNewExpenseTemplate += '<input type="checkbox" id="addid_showCustVisit" onclick="enableDisableCustVisitFileds(this, \'add\')" tabindex="1" >';
    addNewExpenseTemplate += '</div>';
    addNewExpenseTemplate += '</div>';
    addNewExpenseTemplate += '</div>';

    addNewExpenseTemplate += '<div class="form-group">';
    addNewExpenseTemplate += '<label class="col-md-4 control-label">Customer Name</label>';
    addNewExpenseTemplate += '<div class="col-md-8">';
    addNewExpenseTemplate += '<input type="hidden" id="add_id_hiddenCustValue" />';
    addNewExpenseTemplate += '<input type="name" placeholder="Search customer" class="form-control" id="addexp_customerName" tabindex="2" disabled />';
    addNewExpenseTemplate += '</div>';
    addNewExpenseTemplate += '</div>';

    addNewExpenseTemplate += '<div class="form-group">';
    addNewExpenseTemplate += '<label class="col-md-4 control-label">Visit</label>';
    addNewExpenseTemplate += '<div class="col-md-8">';
    addNewExpenseTemplate += '<select class="form-control" id="addexp_appointmentId" tabindex="3" disabled >';
    addNewExpenseTemplate += '<option value="none">None</option>';
    addNewExpenseTemplate += '</select>';
    addNewExpenseTemplate += '</div>';
    addNewExpenseTemplate += '</div>';

    addNewExpenseTemplate += '<div class="form-group">';
    addNewExpenseTemplate += '<label class="col-md-4 control-label">Expense Category</label>';
    addNewExpenseTemplate += '<div class="col-md-8">';
    addNewExpenseTemplate += '<select class="form-control" id="addid_expCategory" tabindex="4" >';
    addNewExpenseTemplate += '<option>None</option>';
    for (var i = 0; i < expCatArray.length; i++) {
        addNewExpenseTemplate += '<option value="' + expCatArray[i].id + '">' + expCatArray[i].categoryName + '</option>';
    }
    addNewExpenseTemplate += '</select>';
    addNewExpenseTemplate += '</div>';
    addNewExpenseTemplate += '</div>';

    addNewExpenseTemplate += '<div class="form-group">';
    addNewExpenseTemplate += '<label class="col-md-4 control-label">Expense Head</label>';
    addNewExpenseTemplate += '<div class="col-md-8">';
    addNewExpenseTemplate += '<input type="name" class="form-control" tabindex="5" id="addexp_headName" maxlength="50" >';
    addNewExpenseTemplate += '</div>';
    addNewExpenseTemplate += '</div>';

    addNewExpenseTemplate += '<div class="form-group">';
    addNewExpenseTemplate += '<label class="col-md-4 control-label">Expense Amount <i class="fa fa-inr fa-fw" aria-hidden="true"></i></label>';
    addNewExpenseTemplate += '<div class="col-md-8">';
    addNewExpenseTemplate += '<div class="input-group">';
    addNewExpenseTemplate += '<input type="text" value="" class="form-control amt" name="text" placeholder="00.00" tabindex="6" id="addexp_amtSpent" maxlength="50"  >';
    addNewExpenseTemplate += '</div>';
    addNewExpenseTemplate += '</div>';
    addNewExpenseTemplate += '</div>';

    addNewExpenseTemplate += '<div class="form-group">';
    addNewExpenseTemplate += '<label class="col-md-4 control-label">Date & Time</label>';
    addNewExpenseTemplate += '<div class="col-md-8">';
    addNewExpenseTemplate += '<div class="input-group date form_datetime" id="div_addexp_dateTime">';
    addNewExpenseTemplate += '<input type="text" size="16" readonly class="form-control" value="' + todayDateTime + '" id="addexp_dateTime" tabindex="7" >';
    addNewExpenseTemplate += '<span class="input-group-btn">';
    addNewExpenseTemplate += '<button class="btn btn-primary date-set" type="button"><i class="fa fa-calendar"></i></button>';
    addNewExpenseTemplate += '</span>';
    addNewExpenseTemplate += '</div>';
    addNewExpenseTemplate += '</div>';
    addNewExpenseTemplate += '</div>';
     addNewExpenseTemplate += '<div id="Images_Save">';
    addNewExpenseTemplate += '<div class="form-group" style="border-bottom:0px;">';
    addNewExpenseTemplate += '<label class="col-md-4 control-label">Attach Receipt</label>';
    addNewExpenseTemplate += '<div class="col-md-4">';
    addNewExpenseTemplate += '<div class="imageupload">';
    addNewExpenseTemplate += '<div class="file-tab ">';
    addNewExpenseTemplate += '<label class="btn btn-default btn-file">';
    addNewExpenseTemplate += '<span>Browse</span>';
    addNewExpenseTemplate += '<!-- The file is stored here. -->';
//    addNewExpenseTemplate += '<input type="file" name="image-file" id="image_file" tabindex="8" onClick="setShowDisplay()">';
    addNewExpenseTemplate += '<input type="file" name="image-file" id="image_file" tabindex="8">';
    addNewExpenseTemplate += '</label>';
    addNewExpenseTemplate += '&nbsp<button type="button" class="btn btn-default">Remove</button>';
    addNewExpenseTemplate += '</div>';
    addNewExpenseTemplate += '</div>';
    addNewExpenseTemplate += '</div>';'['
    addNewExpenseTemplate += '</div>';
     addNewExpenseTemplate += '</div>';
    addNewExpenseTemplate += '</div>';
            addNewExpenseTemplate += '<div class="col-md-offset-4 col-md-2">';
//    addNewExpenseTemplate += '<button type="button" id="getNewimage" onClick="getNewExpenseDiv()" class="btn btn-default" style="display:none">Add New Image</button>';
     addNewExpenseTemplate += '<button type="button" id="getNewimage" onClick="getNewExpenseDiv()" class="btn btn-default">Add New Image</button>';
    addNewExpenseTemplate += '</div>';
    addNewExpenseTemplate += '</div>';
    addNewExpenseTemplate += '</div>';

    addNewExpenseTemplate += '<div class="modal-footer">';
    addNewExpenseTemplate += '<button type="button" class="btn btn-info" tabindex="9" id="id_btn_add_new_expense" onclick="addNewExpenseMultiple()" >Submit</button>';
    addNewExpenseTemplate += '<button type="button" data-dismiss="modal" class="btn btn-default" tabindex="10" >Cancel</button>';
    addNewExpenseTemplate += '</div>';

    addNewExpenseTemplate += '</form>';
    addNewExpenseTemplate += '</div>';
    addNewExpenseTemplate += '</div>';

    $("#id_addNewExpense").html(addNewExpenseTemplate);

    // for autocomplete customer search
    var sourceArray = {id_customerName: $('#addexp_customerName'), id_hiddenCustValue: $('#add_id_hiddenCustValue'), id_appointmentId: $('#addexp_appointmentId')};
    searchCustomerAutoComplete(sourceArray);

    // use to initialize the datetime picker and set the format
    $("#div_addexp_dateTime").datetimepicker({
        format: "dd M yyyy , HH:ii p",
        autoclose: true,
        todayBtn: true,
        endDate: new Date(),
        pickerPosition: "bottom-left"
    });

    var $imageupload = $('.imageupload'); // initialize imageupload 
    $imageupload.imageupload();

    App.initUniform();
    App.fixContentHeight();
    App.callHandleScrollers();
//    jQuery('.tooltips').tooltip();

}
function setShowDisplay(){
        $("#getNewimage").show(); 
}

function removeImage(removeId,mainId){
  //  alert("remove"+mainId);
    $("#"+removeId).hide(); 
    //$("#"+mainId).id="-1";
    //var e = $("#"+mainId);
    //e.id="-1";
    $("#"+mainId).attr('id',"-1");
}

function hideRemoveButton(id){
   $("#removeMe"+id).hide();  
    
}

function showImage(id){
    //alert("#removeMe"+id);
    $("#removeMe"+id).show();  
    
}

function getNewExpenseDiv(){
   var now= $.now();
    var addNewExpenseTemplateTemp="";
        addNewExpenseTemplateTemp += '<div class="form-group" style="border-bottom:0px;">';
    addNewExpenseTemplateTemp += '<label class="col-md-4 control-label"></label>';
    addNewExpenseTemplateTemp += '<div class="col-md-6">';
    addNewExpenseTemplateTemp += '<div class="imageupload" id=Image_'+now+'>';
    addNewExpenseTemplateTemp += '<div class="file-tab ">';
    addNewExpenseTemplateTemp += '<label class="btn btn-default btn-file">';
    addNewExpenseTemplateTemp += '<span>Browse</span>';
    addNewExpenseTemplateTemp += '<!-- The file is stored here. -->';
    addNewExpenseTemplateTemp += '<input type="file" name="image-file" id="image_file" tabindex="8" >';
    addNewExpenseTemplateTemp += '</label>';
    addNewExpenseTemplateTemp += '&nbsp<button type="button" class="btn btn-default">Remove</button>';
    addNewExpenseTemplateTemp += '</div>';
    addNewExpenseTemplateTemp += '</div>';
    addNewExpenseTemplateTemp += '</div>';
    addNewExpenseTemplateTemp += '</div>';
    console.log("Hello sdfsd");
     $(".slimScrollBar").animate({
        scrollTop: $("#getNewimage").offset().top},
        'slow');
    $("#Images_Save").append(addNewExpenseTemplateTemp);
    console.log("Hello11112121221212 ");
    //alert('Image_'+now);
     var $imageupload = $('#Image_'+now); // initialize imageupload 
    $imageupload.imageupload();
}

function getNewExpenseDivAdd(){
      var templatehtml="";
       var now= $.now();
 templatehtml += '<div class="form-group" id="'+now+'">';
        templatehtml += '<label class="col-md-4 control-label"></label>';
        templatehtml += '<div class="col-md-8">';
        templatehtml += '<div class="fileupload fileupload-new" data-provides="fileupload" >';
        templatehtml += '<div class="fileupload-new thumbnail" >';
        templatehtml += '<img id="old_expenseImage"  src="" onerror="this.src=\'assets/img/noimage.jpg\';"/>';
        templatehtml += '</div>';
        templatehtml += '<div class="fileupload-preview fileupload-exists thumbnail" style="max-width: 150px; max-height: 150px; line-height: 20px;">';
        templatehtml += '</div>';
        templatehtml += '<div>';
        templatehtml += '<span class="btn btn-default btn-file" style="width: 37%;">';
        templatehtml += '<span class="fileupload-new">';
        templatehtml += '<i class="fa fa-paperclip"></i> Select image';
        templatehtml += '</span>';
        templatehtml += '<span class="fileupload-exists">';
        templatehtml += '<i class="fa fa-undo"></i> Change';
        templatehtml += '</span>';
//        templatehtml += '<input type="file" id="'+"-1"+'" class="default" name="fileUpload" onClick="hideRemoveButton('+now+')" accept="image/*" tabindex="7" />';
        templatehtml += '<input type="file" id="'+"-1"+'" class="default" name="fileUpload" accept="image/*" tabindex="7" />';
        templatehtml += '</span>';
//        templatehtml += '&nbsp<a href="#" class="btn btn-default fileupload-exists" onClick="showImage('+now+')" data-dismiss="fileupload"><i class="fa fa-trash-o"></i> Remove</a>';
        templatehtml += '&nbsp<a href="#" id="removeMe'+now+'" class="btn btn-default" onClick="removeImage('+now+')"><i class="fa fa-trash-o"></i> Remove</a>';
        templatehtml += '</div>';
        templatehtml += '<div class="col-md-4 col-md-offset-8" style="margin-left: 28%;">';
        templatehtml += '<div class="row" style="margin-top: -34px;">';
        templatehtml += '</div>';
        templatehtml += '</div>';
        templatehtml += '</div>';
        templatehtml += '</div>';
        templatehtml += '</div>'; 
        console.log("FINE DKWPDKWPD");
     $("#Image_Save_Edit").append(templatehtml);
       $(".slimScrollBar").animate({
        scrollTop: $("#getNewimage").offset().top},
        'slow');
        console.log("QMKDQDQD");
}
/**
 * @added by jyoti
 * @returns {Boolean}
 */
function addNewExpense() {

    var selectVisitIsChecked = $('#addid_showCustVisit').prop("checked");
    var exp_customerId = $('#add_id_hiddenCustValue').val();
    var exp_customerName = $('#addexp_customerName').val().trim();
    var exp_appointmentId = $('#addexp_appointmentId').val();
    var expCategoryId = $('addid_expCategory').val();
    var expCategoryName = $("#addid_expCategory option:selected").text().trim();
    var exp_headName = $('#addexp_headName').val().trim();
    var exp_amtSpent = $('#addexp_amtSpent').val().trim();
    var exp_dateTime = $('#addexp_dateTime').val().trim();
//    document.getElementsByClassName('myimg').src = document.getElementById('myimd').src + '?' + (new Date()).getTime();
//    $(".thumbnail").attr('src', $(this).src + '?' + (new Date()).getTime());
// validation
    if (selectVisitIsChecked) {
        if (exp_customerName.length == 0) {
            fieldSenseTosterError("Customer name can not be blank.", true);
            $("#addexp_customerName").focus();
            return false;
        }

        if (exp_customerId == 0) {
            fieldSenseTosterError("Please enter valid customer name.", true);
            $("#addexp_customerName").focus();
            return false;
        }

        if (exp_appointmentId == 'none') {
            fieldSenseTosterError("Visit can not be none.", true);
            $("#addexp_appointmentId").focus();
            return false;
        }

    } else {
        if (exp_appointmentId == 'none') {
            exp_appointmentId = 0;
        }
    }

    if (expCategoryName == 'None') {
        fieldSenseTosterError("Expense category can not be none.", true);
        $("#addid_expCategory").focus();
        return false;
    }

    if (exp_headName.length == 0 || exp_headName == ' ') {
        fieldSenseTosterError("Expense head can not be blank.", true);
        $("#addexp_headName").focus();
        return false;
    }

    if (exp_headName.length > 50) {
        fieldSenseTosterError("Expense head can not be more than 50 characters.", true);
        $("#addexp_headName").focus();
        return false;
    }

    if (exp_amtSpent.length == 0 || exp_amtSpent == ' ') {
        fieldSenseTosterError("Expense amount can not be blank.", true);
        $("#addexp_amtSpent").focus();
        return false;
    }

    if (exp_amtSpent <= 0) {
        fieldSenseTosterError("Please enter valid expense amount.", true);
        $("#addexp_amtSpent").focus();
        return false;
    }

    var expAmountRegex = /^\d+(\.\d+)?$/.test(exp_amtSpent);
    if (!expAmountRegex) {
        fieldSenseTosterError("Please enter valid expense amount.", true);
        $("#addexp_amtSpent").focus();
        return false;
    }

    if (exp_dateTime.trim().length == 0) {
        fieldSenseTosterError("Date & Time can not be blank.", true);
        $("#addexp_dateTime").focus();
        return false;
    }


// for updating image with other data if imagefile is not undefined
//    var inputFile = $("input[type=file]")[0].files[0];
    var file = document.getElementById('image_file').files[0];
    console.log(typeof file);

    if (typeof file != 'undefined') {

        var expenseImageData = new FormData();

        if (file.type.indexOf("image") != 0) {
            fieldSenseTosterError("Please upload proper image file.");
            return false;
        }

        expenseImageData.append('fileupload[0]', file);

        enableDisableOnSumbitButton("disable", "add"); // to disable the save button to avoid multiple selection of save
        $('#pleaseWaitDialog').modal('show');

        $.ajax({
            url: fieldSenseURL + "/expense/image/save",
            type: 'POST',
            headers: {
                "userToken": userToken
            },
            data: expenseImageData,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            success: function (data) {

                if (data.errorCode == '0000') {

                    var imageName = data.result;
                    reloadImageOnAdd(); // set image blank for next add

                    var jsondata = {
                        "appointmentId": exp_appointmentId,
                        "customerId": exp_customerId,
                        "user": {
                            "id": loginUserId
                        },
                        "expenseName": exp_headName,
                        "amountSpent": exp_amtSpent,
                        "status": 0, // new expense status is 0
                        "expenseTime": new Date(exp_dateTime),
                        "eCategoryName": {
                            "id": expCategoryId
                        },
                        "exp_category_name": expCategoryName,
                        "expenseImage": {
                            "id": 1, // 1 to indicate image is added true
                            "imageURL": imageName
                        }
                    };

                    var url = fieldSenseURL + "/expense";
                    var jsonData = JSON.stringify(jsondata);

                    $.ajax({
                        type: "POST",
                        url: url,
                        contentType: "application/json; charset=utf-8",
                        headers: {
                            "userToken": userToken
                        },
                        crossDomain: false,
                        data: jsonData,
                        cache: false,
                        dataType: 'json',
                        asyn: true,
                        success: function (data) {

                            if (data.errorCode == '0000') {
                                $(".cls_expense").modal('hide');
                                fieldSenseTosterSuccess(data.errorMessage, true);
                                var table = $('#example').DataTable();
                                table.draw(false);
                            } else {
                                enableDisableOnSumbitButton("enable", "add");
                                $('#pleaseWaitDialog').modal('hide');
                                fieldSenseTosterError(data.errorMessage, true);
                            }

                        },
                        error: function (xhr, ajaxOptions, thrownError)
                        {
                            enableDisableOnSumbitButton("enable", "add");
                            $('#pleaseWaitDialog').modal('hide');
                            alert("in error:" + thrownError);
                        }
                    });
                } else {
                    enableDisableOnSumbitButton("enable", "add");
                    $('#pleaseWaitDialog').modal('hide');
                    fieldSenseTosterError(data.errorMessage, true);
                }
            },
            error: function (xhr, ajaxOptions, thrownError)
            {
                enableDisableOnSumbitButton("enable", "add");
                $('#pleaseWaitDialog').modal('hide');
                alert("in error:" + thrownError);
            }
        });

    } else if (typeof file == 'undefined') {

        var jsondata = {
            "appointmentId": exp_appointmentId,
            "customerId": exp_customerId,
            "user": {
                "id": loginUserId
            },
            "expenseName": exp_headName,
            "amountSpent": exp_amtSpent,
            "status": 0, // new expense status is 0
            "expenseTime": new Date(exp_dateTime),
            "eCategoryName": {
                "id": expCategoryId
            },
            "exp_category_name": expCategoryName
        };

        var url = fieldSenseURL + "/expense";
        var jsonData = JSON.stringify(jsondata);

        enableDisableOnSumbitButton("disable", "add"); // to disable the save button to avoid multiple selection of save
        $('#pleaseWaitDialog').modal('show');

        $.ajax({
            type: "POST",
            url: url,
            contentType: "application/json; charset=utf-8",
            headers: {
                "userToken": userToken
            },
            crossDomain: false,
            data: jsonData,
            cache: false,
            dataType: 'json',
            asyn: true,
            success: function (data) {

                if (data.errorCode == '0000') {
                    $(".cls_expense").modal('hide');
                    fieldSenseTosterSuccess(data.errorMessage, true);
                    var table = $('#example').DataTable();
                    table.draw(false);
                } else {
                    enableDisableOnSumbitButton("enable", "add");
                    $('#pleaseWaitDialog').modal('hide');
                    fieldSenseTosterError(data.errorMessage, true);
                }

            },
            error: function (xhr, ajaxOptions, thrownError)
            {
                enableDisableOnSumbitButton("enable", "add");
                $('#pleaseWaitDialog').modal('hide');
                alert("in error:" + thrownError);
            }
        });
    }

}

/**
 * @added by jyoti
 * @returns {Boolean}
 */
function addNewExpenseMultiple() {

    var selectVisitIsChecked = $('#addid_showCustVisit').prop("checked");
    var exp_customerId = $('#add_id_hiddenCustValue').val();
    var exp_customerName = $('#addexp_customerName').val().trim();
    var exp_appointmentId = $('#addexp_appointmentId').val();
    var expCategoryId = $('addid_expCategory').val();
    var expCategoryName = $("#addid_expCategory option:selected").text().trim();
    var exp_headName = $('#addexp_headName').val().trim();
    var exp_amtSpent = $('#addexp_amtSpent').val().trim();
    var exp_dateTime = $('#addexp_dateTime').val().trim();
//    document.getElementsByClassName('myimg').src = document.getElementById('myimd').src + '?' + (new Date()).getTime();
//    $(".thumbnail").attr('src', $(this).src + '?' + (new Date()).getTime());
// validation
    if (selectVisitIsChecked) {
        if (exp_customerName.length == 0) {
            fieldSenseTosterError("Customer name can not be blank.", true);
            $("#addexp_customerName").focus();
            return false;
        }

        if (exp_customerId == 0) {
            fieldSenseTosterError("Please enter valid customer name.", true);
            $("#addexp_customerName").focus();
            return false;
        }

        if (exp_appointmentId == 'none') {
            fieldSenseTosterError("Visit can not be none.", true);
            $("#addexp_appointmentId").focus();
            return false;
        }

    } else {
        if (exp_appointmentId == 'none') {
            exp_appointmentId = 0;
        }
    }

    if (expCategoryName == 'None') {
        fieldSenseTosterError("Expense category can not be none.", true);
        $("#addid_expCategory").focus();
        return false;
    }

    if (exp_headName.length == 0 || exp_headName == ' ') {
        fieldSenseTosterError("Expense head can not be blank.", true);
        $("#addexp_headName").focus();
        return false;
    }

    if (exp_headName.length > 50) {
        fieldSenseTosterError("Expense head can not be more than 50 characters.", true);
        $("#addexp_headName").focus();
        return false;
    }

    if (exp_amtSpent.length == 0 || exp_amtSpent == ' ') {
        fieldSenseTosterError("Expense amount can not be blank.", true);
        $("#addexp_amtSpent").focus();
        return false;
    }

    if (exp_amtSpent <= 0) {
        fieldSenseTosterError("Please enter valid expense amount.", true);
        $("#addexp_amtSpent").focus();
        return false;
    }

    var expAmountRegex = /^\d+(\.\d+)?$/.test(exp_amtSpent);
    if (!expAmountRegex) {
        fieldSenseTosterError("Please enter valid expense amount.", true);
        $("#addexp_amtSpent").focus();
        return false;
    }

    if (exp_dateTime.trim().length == 0) {
        fieldSenseTosterError("Date & Time can not be blank.", true);
        $("#addexp_dateTime").focus();
        return false;
    }
var inputFileSz = $("input[type=file]").length;
var imageNameList = [];
var idList = [];
 //console.log("hello3");
for(var i=0;i<inputFileSz;i++){
   // var inputFile = $("input[type=file]")[i].files[0];
    var idServer =  $("input[type=file]")[i].id;
// for updating image with other data if imagefile is not undefined
    var file = $("input[type=file]")[i].files[0];
//    var file = document.getElementById('image_file').files[0];
//    console.log(typeof file);

    if (typeof file != 'undefined') {

        var expenseImageData = new FormData();

        if (file.type.indexOf("image") != 0) {
            fieldSenseTosterError("Please upload proper image file.");
            return false;
        }

        expenseImageData.append('fileupload[0]', file);

        enableDisableOnSumbitButton("disable", "add"); // to disable the save button to avoid multiple selection of save
//        $('#pleaseWaitDialog').modal('show');

        $.ajax({
            url: fieldSenseURL + "/expense/image/save",
            type: 'POST',
            headers: {
                "userToken": userToken
            },
            data: expenseImageData,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            success: function (data) {

                if (data.errorCode == '0000') {

                    var imageName = data.result;
                    //reloadImageOnAdd(); // set image blank for next add
                    imageNameList.push(imageName);
                    idList.push($.now());
                    }
                }
                });
            }
            }
                // console.log("hello12212");
            $.when.apply(null, imageNameList,idList).done(function() {
                 var imageList= [];
    for(var j =0 ; j<imageNameList.length ; j++){
         var ExpenseImage ={
             "id" :idList[j],
             "imageURL" : imageNameList[j]
         }
     //     console.log("hello2");
        imageList.push(ExpenseImage);
    }
   //  console.log("hello21");
                    var jsondata = {
                        "appointmentId": exp_appointmentId,
                        "customerId": exp_customerId,
                        "user": {
                            "id": loginUserId
                        },
                        "expenseName": exp_headName,
                        "amountSpent": exp_amtSpent,
                        "status": 0, // new expense status is 0
                        "expenseTime": new Date(exp_dateTime),
                        "eCategoryName": {
                            "id": expCategoryId
                        },
                        "exp_category_name": expCategoryName,
                         "expenseImageArray" :imageList
//                        "expenseImage": {
//                            "id": 1, // 1 to indicate image is added true
//                            "imageURL": imageName
//                        }
                    };

                    var url = fieldSenseURL + "/expense/createExpenseArray";
                    var jsonData = JSON.stringify(jsondata);
 console.log("hello1");
                    $.ajax({
                        type: "POST",
                        url: url,
                        contentType: "application/json; charset=utf-8",
                        headers: {
                            "userToken": userToken
                        },
                        crossDomain: false,
                        data: jsonData,
                        cache: false,
                        dataType: 'json',
                        asyn: true,
                        success: function (data) {

                            if (data.errorCode == '0000') {
                                $(".cls_expense").modal('hide');
                                fieldSenseTosterSuccess(data.errorMessage, true);
                                var table = $('#example').DataTable();
                                table.draw(false);
                            } else {
                                enableDisableOnSumbitButton("enable", "add");
                                $('#pleaseWaitDialog').modal('hide');
                                fieldSenseTosterError(data.errorMessage, true);
                            }

                        },
                        error: function (xhr, ajaxOptions, thrownError)
                        {
                            enableDisableOnSumbitButton("enable", "add");
                            $('#pleaseWaitDialog').modal('hide');
                            alert("in error:" + thrownError);
                        }
                    });
                });
//                } else {
//                    enableDisableOnSumbitButton("enable", "add");
//                    $('#pleaseWaitDialog').modal('hide');
//                    fieldSenseTosterError(data.errorMessage, true);
//                }
//            },
//            error: function (xhr, ajaxOptions, thrownError)
//            {
//                enableDisableOnSumbitButton("enable", "add");
//                $('#pleaseWaitDialog').modal('hide');
//                alert("in error:" + thrownError);
//            }
//        });

//    } else if (typeof file == 'undefined') {
//
//        var jsondata = {
//            "appointmentId": exp_appointmentId,
//            "customerId": exp_customerId,
//            "user": {
//                "id": loginUserId
//            },
//            "expenseName": exp_headName,
//            "amountSpent": exp_amtSpent,
//            "status": 0, // new expense status is 0
//            "expenseTime": new Date(exp_dateTime),
//            "eCategoryName": {
//                "id": expCategoryId
//            },
//            "exp_category_name": expCategoryName
//        };
//
//        var url = fieldSenseURL + "/expense";
//        var jsonData = JSON.stringify(jsondata);
//
//        enableDisableOnSumbitButton("disable", "add"); // to disable the save button to avoid multiple selection of save
//        $('#pleaseWaitDialog').modal('show');
//
//        $.ajax({
//            type: "POST",
//            url: url,
//            contentType: "application/json; charset=utf-8",
//            headers: {
//                "userToken": userToken
//            },
//            crossDomain: false,
//            data: jsonData,
//            cache: false,
//            dataType: 'json',
//            asyn: true,
//            success: function (data) {
//
//                if (data.errorCode == '0000') {
//                    $(".cls_expense").modal('hide');
//                    fieldSenseTosterSuccess(data.errorMessage, true);
//                    var table = $('#example').DataTable();
//                    table.draw(false);
//                } else {
//                    enableDisableOnSumbitButton("enable", "add");
//                    $('#pleaseWaitDialog').modal('hide');
//                    fieldSenseTosterError(data.errorMessage, true);
//                }
//
//            },
//            error: function (xhr, ajaxOptions, thrownError)
//            {
//                enableDisableOnSumbitButton("enable", "add");
//                $('#pleaseWaitDialog').modal('hide');
//                alert("in error:" + thrownError);
//            }
//        });
//    }

}

/**
 * @added by jyoti
 * @param {type} action
 * @returns {undefined}
 */
function enableDisableOnSumbitButton(action, eventName) {
    if (eventName == 'add') {
        if (action == 'disable') {
            $('#id_btn_add_new_expense').attr('disabled', 'disabled');
        } else if (action == 'enable') {
            $('#id_btn_add_new_expense').removeAttr('disabled');
        }
    } else if (eventName == 'edit') {
        if (action == 'disable') {
            $('#id_btn_edit').attr('disabled', 'disabled');
        } else if (action == 'enable') {
            $('#id_btn_edit').removeAttr('disabled');
        }
    }
}

/**
 * @Added by jyoti
 * @param {type} index
 * @returns {undefined}
 */
function deleteLoggedInUserPendingExpense(index) {

    var details = expensearray[index];
    var expenseId = details.expense_data.id;

    bootbox.dialog({
        message: "Are you sure you want to delete the Expense ?",
        title: "Delete Expense",
        buttons: {
            yes: {
                label: "Yes",
                className: "btn-default",
                callback: function () {
                    $('#pleaseWaitDialog').modal('show');
                    $.ajax({
                        type: "DELETE",
                        url: fieldSenseURL + "/expense/" + expenseId,
                        contentType: "application/json; charset=utf-8",
                        headers: {
                            "userToken": userToken
                        },
                        crossDomain: false,
                        cache: false,
                        dataType: 'json',
                        success: function (data) {

                            if (data.errorCode == '0000') {
                                fieldSenseTosterSuccess(data.errorMessage, true);
                                var table = $('#example').DataTable();
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
                }
            }
        }
    });
}

function displayExpDetails(index) {

    var details = expensearray[index];
    var templatehtml = "";
    var footerhtml = "";
    var expenseCsv = details.expense_data.expenseImageCsv;
   // alert(expenseCsv);
    var imageName = expenseImageURLPath + 'expense_' + accountId + '_' + details.expense_data.user.id + '_' + details.expense_data.id + '_640X480.png?';

    var oldExpenseCategory = details.expense_data.exp_category_name;

    var appointmentTitle = details.expense_data.appointment.title;
    if (appointmentTitle.length == 0) {
        appointmentTitle = details.expense_data.appointment.purpose.purpose;
    }

    var customerNameWithLocation = details.expense_data.customerName + " - " + details.expense_data.appointment.customer.customerLocation;
    if (details.expense_data.customerName.length == 0) {
        customerNameWithLocation = "";
    }

    var status = details.expense_data.status;
    var userId = details.expense_data.user.id;
    var isEnable = 'disabled';
    var displayCheckbox = 'none;';
    if (status == 0 && userId == loginUserId) {
        displayCheckbox = 'block;';
        isEnable = '';
    }

    templatehtml += '<div class="form-group">';
    templatehtml += '<label class="col-md-4 control-label">Expense Head</label>';
    templatehtml += '<div class="col-md-8">';
    templatehtml += '<input type="name" class="form-control" Value="' + details.expense_data.expenseName + '" tabindex="1" id="exp_headName" ' + isEnable + ' >';
    templatehtml += '</div>';
    templatehtml += '</div>';

    templatehtml += '<div class="form-group">';
    templatehtml += '<label class="col-md-4 control-label">Expense Category</label>';
    templatehtml += '<div class="col-md-8">';
    templatehtml += '<select class="form-control" id="exp_Category_id" tabindex="2" '+isEnable+' >';
//        templatehtml += '<option>None</option>';
    var selectedCategory = '';
    for (var i = 0; i < expCatArray.length; i++) {
        if (expCatArray[i].categoryName == oldExpenseCategory) {
            selectedCategory = 'selected';
        } else {
            selectedCategory = '';
        }
        templatehtml += '<option value="' + expCatArray[i].id + '"  ' + selectedCategory + ' >' + expCatArray[i].categoryName + '</option>';
    }
    templatehtml += '</select>';
    templatehtml += '</div>';
    templatehtml += '</div>';

    if (status == 0 && userId == loginUserId) {
        templatehtml += '<div class="form-group">';
        templatehtml += '<label class="col-md-4 control-label">Date & Time</label>';
        templatehtml += '<div class="col-md-8">';
        templatehtml += '<div class="input-group date form_datetime" id="div_exp_dateTime">';
        templatehtml += '<input type="text" class="form-control" value="' + details.expenseTime + '" tabindex="3" id="exp_dateTime" readonly >';
        templatehtml += '<span class="input-group-btn">';
        templatehtml += '<button class="btn btn-primary date-set" type="button"><i class="fa fa-calendar"></i></button>';
        templatehtml += '</span>';
        templatehtml += '</div>';
        templatehtml += '</div>';
        templatehtml += '</div>';
    } else {
        templatehtml += '<div class="form-group">';
        templatehtml += '<label class="col-md-4 control-label">Date & Time</label>';
        templatehtml += '<div class="col-md-8">';
        templatehtml += '<input type="text" class="form-control" value="' + details.expenseTime + '" tabindex="3" readonly>';
        templatehtml += '</div>';
        templatehtml += '</div>';
    }
    templatehtml += '<div class="form-group" style="display: ' + displayCheckbox + ' " >';
    templatehtml += '<label class="col-md-4 control-label">Select visit </label>';
    templatehtml += '<div class="col-md-8">';
    templatehtml += '<div class="checkbox-list">';
    templatehtml += '<input type="checkbox" id="id_showCustVisit" onclick="enableDisableCustVisitFileds(this, \'edit\')" tabindex="4" >';
    templatehtml += '</div>';
    templatehtml += '</div>';
    templatehtml += '</div>';

    templatehtml += '<div class="form-group">';
    templatehtml += '<label class="col-md-4 control-label">Customer Name</label>';
    templatehtml += '<div class="col-md-8">';

    templatehtml += '<input type="hidden" id="id_hiddenCustValue" value="' + details.expense_data.customerId + '"/>';
    templatehtml += '<input type="name" placeholder="Search customer" class="form-control" Value="' + customerNameWithLocation + '" tabindex="5" id="exp_customerName" disabled >';
    templatehtml += '</div>';
    templatehtml += '</div>';

    templatehtml += '<div class="form-group">';
    templatehtml += '<label class="col-md-4 control-label">Visit</label>';
    templatehtml += '<div class="col-md-8">';
    templatehtml += '<select class="form-control" id="exp_appointmentId" tabindex="6"  disabled >';
//    templatehtml += '<option value="none" >None</option>';
    templatehtml += '<option value="' + details.expense_data.appointmentId + '" selected>' + appointmentTitle + '</option>';
    templatehtml += '</select>';

    templatehtml += '</div>';
    templatehtml += '</div>';
console.log(expenseCsv);
if(expenseCsv === null || expenseCsv === "" || expenseCsv === " "){
    if (status == 0 && userId == loginUserId) {
// for image start
   var now = window.performance.now()+"";
    now = now.replace(".","");
    console.log(now);
        templatehtml += '<form class="form-horizontal"  role="form"  method="POST" id="submit_form" name="submit_form">';
        templatehtml += '<div id="Image_Save_Edit">';
        templatehtml += '<div class="form-group" id="'+now+'">';
        templatehtml += '<label class="col-md-4 control-label">Attachment</label>';
        templatehtml += '<div class="col-md-8">';
        templatehtml += '<div class="fileupload fileupload-new" data-provides="fileupload">';
        templatehtml += '<div class="fileupload-new thumbnail" >';
        templatehtml += '<img id="old_expenseImage" src="' + expenseImageURLPath + 'expense_' + accountId + '_' + details.expense_data.user.id + '_' + details.expense_data.id + '.png?' + time + '" onerror="this.src=\'assets/img/noimage.jpg\';"/>';
        templatehtml += '</div>';
        templatehtml += '<div class="fileupload-preview fileupload-exists thumbnail" style="max-width: 150px; max-height: 150px; line-height: 20px;">';
        templatehtml += '</div>';
        templatehtml += '<div>';
        templatehtml += '<span class="btn btn-default btn-file" style="width: 37%;">';
        templatehtml += '<span class="fileupload-new">';
        templatehtml += '<i class="fa fa-paperclip"></i> Select image';
        templatehtml += '</span>';
        templatehtml += '<span class="fileupload-exists">';
        templatehtml += '<i class="fa fa-undo"></i> Change';
        templatehtml += '</span>';
        templatehtml += '<input type="file" id="upload_expenseImage" class="default" name="fileUpload"  accept="image/*" tabindex="7" />';
        templatehtml += '</span>';
        templatehtml += '</div>';
        templatehtml += '<div class="col-md-4 col-md-offset-8" style="margin-left: 28%;">';
        templatehtml += '<div class="row" style="margin-top: -53px;">';
//        templatehtml += '<a href="#" class="btn btn-default fileupload-exists" data-dismiss="fileupload"><i class="fa fa-trash-o"></i> Remove</a>';
        templatehtml += '&nbsp<a href="#" id="removeMe'+now+'" class="btn btn-default" onClick="removeImage('+now+','+""+')" style="margin-left: 30%;"><i class="fa fa-trash-o"></i> Remove</a>';
        templatehtml += '</div>';
        templatehtml += '</div>';
        templatehtml += '</div>';
        templatehtml += '</div>';
        templatehtml += '</div>';
        templatehtml += '</div>';
         templatehtml += '<button type="button" id="getNewimage" onClick="getNewExpenseDivAdd()" style="margin-left: 35%;margin-bottom: 5%;" class="btn btn-default">Add New Image</button>';
        templatehtml += '</form>';
// for image end
    } else {

        templatehtml += '<div class="form-group">';
        templatehtml += '<label class="col-md-4 control-label">Attachment</label>';
        templatehtml += '<div class="col-md-8">';
        templatehtml += '<p class="form-control-static"><img src="' + expenseImageURLPath + 'expense_' + accountId + '_' + details.expense_data.user.id + '_' + details.expense_data.id + '_640X480.png?' + time + '" onerror="this.src=\'assets/img/noimage.jpg\';"/></p>';
        templatehtml += '</div>';
        templatehtml += '</div>';

    }
}else{
        if (status == 0 && userId == loginUserId) {
// for image start
        templatehtml += '<form class="form-horizontal"  role="form"  method="POST" id="submit_form" name="submit_form">';
        var expenseCsvArray = expenseCsv.split(",");
        console.log(expenseCsvArray.length)
        templatehtml += '<div id="Image_Save_Edit">';
        // templatehtml += '<button type="button" id="getNewimage" onClick="getNewExpenseDiv()" class="btn btn-default">Add New Image</button>';
        for(var i = 0 ; i<expenseCsvArray.length ; i++){
          //   var now= $.now();
          var str="";
        //  var now = window.performance.now()+"";
        //  if(now.includes(".")){
        //alert(now)
//              now = now.replace(".","");
           //now=now.split(".").join("");
       //   }
//          var now = window.performance.now()+"";
//    now = now.replace(".","");
    var now = Math.round(new Date().getTime());
    console.log(now);
       
        templatehtml += '<div class="form-group" id="'+now+'">';
        if(i==0){
        templatehtml += '<label class="col-md-4 control-label">Attachment</label>';
        }else{
            templatehtml += '<label class="col-md-4 control-label"></label>';
        }
        templatehtml += '<div class="col-md-8">';
        templatehtml += '<div class="fileupload fileupload-new" data-provides="fileupload" >';
        templatehtml += '<div class="fileupload-new thumbnail" >';
        templatehtml += '<img id="old_expenseImage" src="' + expenseImageURLPath + 'expense_' + accountId + '_' + details.expense_data.user.id + '_' + details.expense_data.id + '_'+expenseCsvArray[i]+'.png?' + time + '" onerror="this.src=\'assets/img/noimage.jpg\';"/>';
        templatehtml += '</div>';
        templatehtml += '<div class="fileupload-preview fileupload-exists thumbnail" style="max-width: 150px; max-height: 150px; line-height: 20px;">';
        templatehtml += '</div>';
        templatehtml += '<div>';
        templatehtml += '<span class="btn btn-default btn-file" style="width: 37%;">';
        templatehtml += '<span class="fileupload-new">';
        templatehtml += '<i class="fa fa-paperclip"></i> Select image';
        templatehtml += '</span>';
        templatehtml += '<span class="fileupload-exists">';
        templatehtml += '<i class="fa fa-undo"></i> Change';
        templatehtml += '</span>';
        templatehtml += '<input type="file" id="'+expenseCsvArray[i]+'" class="default" name="fileUpload" accept="image/*" tabindex="7" />';
        templatehtml += '</span>';
        templatehtml += '</div>';
        templatehtml += '<div class="col-md-4 col-md-offset-8" style="margin-left: 28%;">';
        templatehtml += '<div class="row" style="margin-top: -53px;">';
//        templatehtml += '&nbsp<a href="#" class="btn btn-default fileupload-exists" data-dismiss="fileupload" onClick="showImage('+now+')" ><i class="fa fa-trash-o"></i> Remove</a>';
        templatehtml += '&nbsp<a href="#" id="removeMe'+now+'" class="btn btn-default" onClick="removeImage('+now+','+expenseCsvArray[i]+')" style="margin-left: 30%;"><i class="fa fa-trash-o"></i> Remove</a>';	
        templatehtml += '</div>';
        templatehtml += '</div>';
        templatehtml += '</div>';
        templatehtml += '</div>';
        templatehtml += '</div>';
    }
     
    templatehtml += '</div>';
     templatehtml += '<button type="button" id="getNewimage" onClick="getNewExpenseDivAdd()" style="margin-left: 35%;margin-bottom: 5%;" class="btn btn-default">Add New Image</button>';
        templatehtml += '</form>';
// for image end
    } else {
          var expenseCsvArray = expenseCsv.split(",");
        console.log(expenseCsvArray.length)
        for(var i = 0 ; i<expenseCsvArray.length ; i++){
        templatehtml += '<div class="form-group">';
        templatehtml += '<label class="col-md-4 control-label">Attachment</label>';
        templatehtml += '<div class="col-md-8">';
        templatehtml += '<p class="form-control-static"><img src="' + expenseImageURLPath + 'expense_' + accountId + '_' + details.expense_data.user.id + '_' + details.expense_data.id +  '_'+expenseCsvArray[i]+'.png?' + time + '" onerror="this.src=\'assets/img/noimage.jpg\';"/></p>';
        templatehtml += '</div>';
        templatehtml += '</div>';
    }

    }    
}
    templatehtml += '<div class="form-group">';
    templatehtml += '<label class="col-md-4 control-label">Expense Amount <i class="fa fa-inr fa-fw" aria-hidden="true"></i></label>';
    templatehtml += '<div class="col-md-8">';
    templatehtml += '<input type="text" class="form-control" value="' + details.expense_data.amountSpent + '" tabindex="8" id="exp_amtSpent" ' + isEnable + ' >';
    templatehtml += '</div>';
    templatehtml += '</div>';


    templatehtml += '<div class="form-group">';
    templatehtml += '<label class="col-md-4 control-label">Status</label>';
    templatehtml += '<div class="col-md-8">';
    templatehtml += '<select class="form-control" id="expense_status" data-placeholder="Select..." disabled tabindex="9" >';
    if (status == 0) {
        templatehtml += '<option value="0" selected>Pending Approval by Reporting Head</option>';
    } else if (status == 1) {
        templatehtml += '<option value="1" selected>Pending Approval by Accounts</option>';
    } else if (status == 2) {
        templatehtml += '<option value="2" selected>Rejected by Reporting Head</option>';
    } else if (status == 3) {
        templatehtml += '<option value="3" selected>Approved</option>';
    } else if (status == 4) {
        templatehtml += '<option value="4" selected>Rejected by Accounts</option>';
    } else if (status == 5) {
        templatehtml += '<option value="5" selected>Disbursed</option>';
    }
    templatehtml += '</select>';
    templatehtml += '</div>';
    templatehtml += '</div>';

    templatehtml += '<div class="form-group">';
    templatehtml += '<label class="col-md-4 control-label">Reporting Head</label>';
    templatehtml += '<div class="col-md-8">';
    templatehtml += '<select class="form-control" data-placeholder="Select..." disabled tabindex="10" >';
    templatehtml += '<option value="' + details.expense_data.report_head.fullname + '" selected>' + details.expense_data.report_head.fullname + '</option>';
    templatehtml += '</select>';
    templatehtml += '</div>';
    templatehtml += '</div>'
    if (status == 5) {
        templatehtml += '<div class="form-group" id="exp_amount">';
        templatehtml += '<label class="col-md-4 control-label">Disbursed amount <i class="fa fa-inr fa-fw" aria-hidden="true"></i></label>';
        templatehtml += '<div class="col-md-8">';
        templatehtml += '<input type="text" id="dis_amount" class="form-control" value="' + details.expense_data.disburse_amount + '" tabindex="11" readonly>';
        templatehtml += '</div>';
        templatehtml += '</div>';
    }
    templatehtml += '<div class="form-group">';
    templatehtml += '<label class="col-md-1 control-label">&nbsp;</label>';
    templatehtml += '<div class="col-md-11">';
    templatehtml += '<div class="radio-list">';

    if (status == 0 && userId != loginUserId) {
        templatehtml += '<label class="radio-inline"><input type="radio" name="optionsRadios" id="optionsRadios1" value="option1" tabindex="12">Approve</label>';
    } else {
//        templatehtml += '<label class="radio-inline"><input type="radio" name="optionsRadios" id="optionsRadios1" value="option1" tabindex="8" disabled>Approve</label>';
    }

    if (status == 0 && userId != loginUserId) {
        templatehtml += '<label class="radio-inline"><input type="radio" name="optionsRadios" id="optionsRadios3" value="option3" tabindex="13">Reject</label>';
    } else {
//        templatehtml += '<label class="radio-inline"><input type="radio" name="optionsRadios" id="optionsRadios3" value="option3" tabindex="10" disabled>Reject</label>';
    }
    templatehtml += '</div>';
    templatehtml += '</div>';
    templatehtml += '</div>';

    if (status == 4) {
        templatehtml += '<div class="form-group" id="exp_reason">';
        templatehtml += '<label class="col-md-4 control-label">Rejected Reason</label>';
        templatehtml += '<div class="col-md-8">';
        templatehtml += '<textarea id="reject_reason" class="form-control" style="resize: none;" tabindex="14" readonly>' + details.expense_data.rejectedReson + '</textarea>';
        templatehtml += '</div>';
        templatehtml += '</div>';
    } else {
        templatehtml += '<div class="form-group" id="exp_reason" style="display:none">';
        templatehtml += '<label class="col-md-4 control-label">Rejected Reason</label>';
        templatehtml += '<div class="col-md-8">';
        templatehtml += '<textarea id="reject_reason" class="form-control" style="resize: none;" tabindex="14"></textarea>';
        templatehtml += '</div>';
        templatehtml += '</div>';
    }

    if (status == 0 && userId != loginUserId) {
        footerhtml += '<button type="button" class="btn btn-info" onclick="saveAppRegDis(' + index + ');" tabindex="15">Save</button>';
    } else if (status == 0 && userId == loginUserId) {
        footerhtml += '<button type="button" class="btn btn-info" id="id_btn_edit" onclick="editLoggedInUserPendingExpensesMultiImages(' + index + ');" tabindex="15">Save</button>';
    } else {
//        footerhtml += '<button type="button" class="btn btn-info" disabled onclick="saveAppRegDis(' + index + ');" tabindex="16" >Save</button>';        
    }
    footerhtml += '<button type="button" data-dismiss="modal" class="btn btn-default" tabindex="16">Close</button>';

    $(".modal-title").html("Expenses - " + details.expense_data.user.fullname);
    $("#exp_footer").html(footerhtml);
    $("#expense_details").html(templatehtml);

//    $('.date-picker').datepicker({
//        rtl: App.isRTL(),
//        autoclose: true
//    });

    $("#optionsRadios1").click(function () {
        if ($(this).is(':checked')) {
            $('#exp_amount').hide();
            $('#exp_mode').hide();
            $('#exp_date').hide();
            $('#exp_reason').hide();
        }
    });

    $("#optionsRadios2").click(function () {
        if ($(this).is(':checked')) {
            $('#exp_amount').show();
            $('#exp_mode').show();
            $('#exp_date').show();
            $('#exp_reason').hide();
        } else {
            $('#exp_amount').hide();
            $('#exp_mode').hide();
            $('#exp_date').hide();
        }
    });

    $("#optionsRadios3").click(function () {
        if ($(this).is(':checked')) {
            $('#exp_amount').hide();
            $('#exp_mode').hide();
            $('#exp_date').hide();
            $('#exp_reason').show();
        } else {
            $('#exp_reason').hide();
        }
    });

    if (status == 0 && userId == loginUserId) {
        visitRelatedToSelectedCustomer(details.expense_data.customerId, details.expense_data.appointmentId, $('#exp_appointmentId'));
    }
    // for autocomplete customer search
    var sourceArray = {id_customerName: $('#exp_customerName'), id_hiddenCustValue: $('#id_hiddenCustValue'), id_appointmentId: $('#exp_appointmentId')};
    searchCustomerAutoComplete(sourceArray);

    onKeyUpEvent($("#exp_customerName")); // when customer name cleared by user visit will get selected to none

// use to initialize the datetime picker and set the format
    $("#div_exp_dateTime").datetimepicker({
        format: "dd M yyyy , HH:ii p",
        autoclose: true,
        todayBtn: true,
        endDate: new Date(),
        pickerPosition: "bottom-left"
    });

    App.initUniform();
    App.fixContentHeight();
    $(".tooltip fade bottom in").hide();
//    reloadImageOnEdit(imageName); // to reflect the changed image after image edit

}

/**
 * @added by jyoti
 * @param {type} source
 * @param {type} event
 * @returns {undefined}
 */
function enableDisableCustVisitFileds(source, event) {
    if (event == 'edit') {
        var isChecked = $(source).prop("checked");
        if (isChecked == true) {
            $('#exp_customerName').prop("disabled", false);
            $('#exp_appointmentId').prop("disabled", false);
        } else {
            $('#exp_customerName').prop("disabled", true);
            $('#exp_appointmentId').prop("disabled", true);
        }
    } else if (event == 'add') {
        var isChecked = $(source).prop("checked");
        if (isChecked == true) {
            $('#addexp_customerName').prop("disabled", false);
            $('#addexp_appointmentId').prop("disabled", false);
        } else {
            $('#addexp_customerName').prop("disabled", true);
            $('#addexp_appointmentId').prop("disabled", true);
        }
    }
}

/**
 * @added by jyoti
 * @param {type} customerId
 * @param {type} old_appointmentId
 * @param {type} sourceId
 * @returns {undefined}
 */
function visitRelatedToSelectedCustomer(customerId, old_appointmentId, sourceId) {
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/appointment/relevantCustomer/user/" + loginUserId + "/" + customerId,
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
                sourceId.html('');
                var appoinmentData = data.result;
                var activityTemplate = '';
                activityTemplate = '<option value="none">None</option>';
                for (var i = 0; i < appoinmentData.length; i++) {

                    var appoinmentId = appoinmentData[i].id;
                    var appoinmentTitle = appoinmentData[i].title;
                    var appoinmentStatus = appoinmentData[i].status;
                    var appoinmentSdateTime = appoinmentData[i].sdateTime;
//                    var appoinmentSendTime = appoinmentData[i].sendTime;                    
                    var appoinmentSdateTimeSplit = appoinmentSdateTime.split(' ');
                    var appointmentDate = appoinmentSdateTimeSplit[0];
                    var status = "";

                    if (appoinmentTitle.length == 0) {
                        appoinmentTitle = appoinmentData[i].purpose.purpose;
                    }

                    if (appoinmentStatus == 0) {
                        status = "Pending";
                    } else if (appoinmentStatus == 1) {
                        status = "In-Progress";
                    } else if (appoinmentStatus == 2) {
                        status = "Completed";
                    } else if (appoinmentStatus == 3) {
                        status = "Cancelled";
                    }

                    if (old_appointmentId == appoinmentId) {
                        activityTemplate += '<option value="' + appoinmentId + '" selected>' + appoinmentTitle + " on " + appointmentDate + " - " + status + '</option>';
                    } else if (i == 0) {
                        activityTemplate += '<option value="' + appoinmentId + '" selected>' + appoinmentTitle + " on " + appointmentDate + " - " + status + '</option>';
                    } else {
                        activityTemplate += '<option value="' + appoinmentId + '">' + appoinmentTitle + " on " + appointmentDate + " - " + status + '</option>';
                    }
                }

                sourceId.append(activityTemplate);
                $('#pleaseWaitDialog').modal('hide');

            } else {
                $('#pleaseWaitDialog').modal('hide');
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}

function deSelectAll() {

    var count = 0;
    $(".checkboxes").each(function () {
        if ($(this).attr("disabled") == "disabled") {
            count += 1;
        }
    });
    if ($('.checkboxes:checked').length == $('.checkboxes').length || $('.checkboxes:checked').length == $('.checkboxes').length - count) {
        $(".group-checkable").attr('checked', true);
        $(".group-checkable").parent().attr('class', 'checked');
    } else {
        $(".group-checkable").attr('checked', false);
        $(".group-checkable").parent().removeClass('checked');
    }
    if ($('.checkboxes:checked').length > 0) {
        $("#btnApprovepo").css("display", "inline-block");
        
    } else {
        $("#btnApprovepo").css("display", "none");
    }
    App.initUniform();
    App.fixContentHeight();
    jQuery('.tooltips').tooltip();
}

function approveAllExpense() {
    var checkboxlist = [];
    var jsondata = [];
    $(".checkboxes").each(function () {
        if ($(this).is(":checked")) {
            var index = $(this).attr("id").substring(9, $(this).attr("id").length);
            if (expensearray[index].expense_data.status == 0) { // 1 changed to 0
                checkboxlist.push($(this).attr("id").substring(9, $(this).attr("id").length));
            }
        }
    });
    if (checkboxlist.length == 0) {
        fieldSenseTosterError("Expenses cannot be approved", true);
        return false;
    }
    for (i = 0; i < checkboxlist.length; i++) {
        var jsondatalocal = {
            "id": expensearray[checkboxlist[i]].expense_data.id,
            "appointmentId": expensearray[checkboxlist[i]].expense_data.appointmentId,
            "customerId": expensearray[checkboxlist[i]].expense_data.customerId,
            "user": {
                "id": expensearray[checkboxlist[i]].expense_data.user.id
            },
            "expenseName": expensearray[checkboxlist[i]].expense_data.expenseName,
            "expenseType": expensearray[checkboxlist[i]].expense_data.expenseType,
            "description": expensearray[checkboxlist[i]].expense_data.description,
            "amountSpent": expensearray[checkboxlist[i]].expense_data.amountSpent,
            "status": 1, // 3 status changed to 1
            "expenseTime": new Date(expensearray[checkboxlist[i]].expense_data.expenseTime.time),
            "submittedOn": new Date(expensearray[checkboxlist[i]].expense_data.submittedOn.time),
            "approvedOrRjectedBy": {
                "id": loginUserId,
            },
            //"rejectedReson":rej_reason,
            "createdOn": new Date(expensearray[checkboxlist[i]].expense_data.createdOn.time),
            "eCategoryName": {
                "id": expensearray[checkboxlist[i]].expense_data.eCategoryName.id
            },
            "exp_category_name": expensearray[checkboxlist[i]].expense_data.exp_category_name
        }
        jsondata.push(jsondatalocal);
    }

    var jsonData = JSON.stringify(jsondata);
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "PUT",
        url: fieldSenseURL + "/expense/Account/approve/multiple",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            if (data.errorCode == '0000') {
                $('#basic').modal('toggle');
                fieldSenseTosterSuccess(data.errorMessage, true);
                var table = $('#example').DataTable();
                table.draw(false);
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
            }
        },
        error: function (xhr, ajaxOptions, thrownError)
        {
            $('#pleaseWaitDialog').modal('hide');
            alert("in error:" + thrownError);
        }
    });

}

function rejectAllExpense() {

    var reason = $("#reason_text").val().trim();
    var checkboxlist1 = [];
    var jsondata = [];
    $(".checkboxes").each(function () {
        if ($(this).is(":checked")) {
            checkboxlist1.push($(this).attr("id").substring(9, $(this).attr("id").length));
        }
    });
    if (checkboxlist1.length == 0) {
        fieldSenseTosterError("Expenses cannot be rejected", true);
    }
    for (i = 0; i < checkboxlist1.length; i++) {
        var jsondatalocal = {
            "id": expensearray[checkboxlist1[i]].expense_data.id,
            "appointmentId": expensearray[checkboxlist1[i]].expense_data.appointmentId,
            "customerId": expensearray[checkboxlist1[i]].expense_data.customerId,
            "user": {
                "id": expensearray[checkboxlist1[i]].expense_data.user.id
            },
            "expenseName": expensearray[checkboxlist1[i]].expense_data.expenseName,
            "expenseType": expensearray[checkboxlist1[i]].expense_data.expenseType,
            "description": expensearray[checkboxlist1[i]].expense_data.description,
            "amountSpent": expensearray[checkboxlist1[i]].expense_data.amountSpent,
            "status": 2, // 4 changed to 2
            "expenseTime": new Date(expensearray[checkboxlist1[i]].expense_data.expenseTime.time),
            "submittedOn": new Date(expensearray[checkboxlist1[i]].expense_data.submittedOn.time),
            "approvedOrRjectedBy": {
                "id": loginUserId,
            },
            "rejectedReson": reason,
            "createdOn": new Date(expensearray[checkboxlist1[i]].expense_data.createdOn.time),
            "eCategoryName": {
                "id": expensearray[checkboxlist1[i]].expense_data.eCategoryName.id
            },
            "exp_category_name": expensearray[checkboxlist1[i]].expense_data.exp_category_name
        }
        jsondata.push(jsondatalocal);
    }

    var jsonData = JSON.stringify(jsondata);
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "PUT",
        url: fieldSenseURL + "/expense/Account/reject/multiple",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            if (data.errorCode == '0000') {
                $('#reject').modal('toggle');
                fieldSenseTosterSuccess(data.errorMessage, true);
                var table = $('#example').DataTable();
                table.draw(false);
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
            }
        },
        error: function (xhr, ajaxOptions, thrownError)
        {
            $('#pleaseWaitDialog').modal('hide');
            alert("in error:" + thrownError);
        }
    });
}

function saveAppRegDis(index) {

    var radiomand;
    $('.radio-inline').each(function () {
        if ($(this).find('span').hasClass('checked')) {
            radiomand = true;
            return false;
        } else {
            radiomand = false;
        }
    });
    if (radiomand == false) {
        fieldSenseTosterError("Any one option should be selected");
        return false;
    }
    var rej_reason = expensearray[index].expense_data.rejectedReson;
    var pay_mode = expensearray[index].expense_data.payment_mode;
    var default_date = new Date("");
    var selctedvalue = "approved";
    var status = 1; // 3 changed to 1
    if ($("#optionsRadios3").parent().attr("class") == "checked") {
        rej_reason = $("#reject_reason").val().trim();
        selctedvalue = "rejected";
        status = 2; // 4 changed to 2
        //}
    }

    var jsondata = {
        "id": expensearray[index].expense_data.id,
        "appointmentId": expensearray[index].expense_data.appointmentId,
        "customerId": expensearray[index].expense_data.customerId,
        "user": {
            "id": expensearray[index].expense_data.user.id
        },
        "expenseName": expensearray[index].expense_data.expenseName,
        "expenseType": expensearray[index].expense_data.expenseType,
        "description": expensearray[index].expense_data.description,
        "amountSpent": expensearray[index].expense_data.amountSpent,
        "status": status,
        "expenseTime": new Date(expensearray[index].expense_data.expenseTime.time),
        "submittedOn": new Date(expensearray[index].expense_data.submittedOn.time),
        "approvedOrRjectedBy": {
            "id": loginUserId,
        },
        "rejectedReson": rej_reason,
        "createdOn": new Date(expensearray[index].expense_data.createdOn.time),
        "eCategoryName": {
            "id": expensearray[index].expense_data.eCategoryName.id
        },
        "payment_mode": pay_mode,
        "default_date": default_date,
        "exp_category_name": expensearray[index].expense_data.exp_category_name
    }
    var url = fieldSenseURL + "/expense/Account/approve";
    if (selctedvalue == "rejected") {
        url = fieldSenseURL + "/expense/Account/reject";
    }

    var jsonData = JSON.stringify(jsondata);
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "PUT",
        url: url,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            if (data.errorCode == '0000') {
                $('#expenses').modal('toggle');
                fieldSenseTosterSuccess(data.errorMessage, true);
                $(".group-checkable").attr('checked', false);
                $(".group-checkable").parent().removeClass('checked');
                var table = $('#example').DataTable();
                table.draw(false);
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
            }

        },
        error: function (xhr, ajaxOptions, thrownError)
        {
            $('#pleaseWaitDialog').modal('hide');
            alert("in error:" + thrownError);
        }
    });

}

/**
 * @added by jyoti
 * @param {type} sourceId
 * @returns {undefined}
 */
function onKeyUpEvent(sourceId) {
    sourceId.keyup(function () {
        if (!this.value) {
            var selectVisitIsChecked = $('#id_showCustVisit').prop("checked");
            if (selectVisitIsChecked) {
                var exp_customerName = $('#exp_customerName').val().trim();
                if (exp_customerName.length == 0 || exp_customerName == ' ') {
                    $('#exp_appointmentId').empty()
                    $('#exp_appointmentId').append($('<option>', {value: 'none', text: 'None'}, '</option>'));
                }
            }
        }
    });
}

/**
 * @Added by jyoti
 * @Purpose : to edit own expenses only if pending approval by head [ status = 0] and userid == loginuserid
 * @param {type} index
 * @returns {undefined}
 */
function editLoggedInUserPendingExpenses(index) {

    var exp_headName = $('#exp_headName').val().trim();
    var exp_Category_id = $('#exp_Category_id').val().trim();
    var expCategoryName = $("#exp_Category_id option:selected").text().trim();
    var exp_dateTime = $('#exp_dateTime').val().trim();
    var exp_customerId = $('#id_hiddenCustValue').val();
    var exp_customerName = $('#exp_customerName').val().trim();
    var exp_appointmentId = $('#exp_appointmentId').val();
    var exp_amtSpent = $('#exp_amtSpent').val().trim();
    var selectVisitIsChecked = $('#id_showCustVisit').prop("checked");

// validation
    if (exp_headName.length == 0 || exp_headName == ' ') {
        fieldSenseTosterError("Expense head can not be blank.", true);
        $("#exp_headName").focus();
        return false;
    }

    if (exp_headName.length > 50) {
        fieldSenseTosterError("Expense head can not be more than 50 characters.", true);
        $("#exp_headName").focus();
        return false;
    }

    if (exp_dateTime.trim().length == 0) {
        fieldSenseTosterError("Date & Time can not be blank.", true);
        $("#exp_dateTime").focus();
        return false;
    }

    if (selectVisitIsChecked) {

        if (exp_customerName.length == 0 || exp_customerName == ' ') {
//            $('#exp_appointmentId option[value=none]').attr('selected', 'selected');
            exp_appointmentId = 0;
            exp_customerId = 0;
        }

        if (exp_customerId == 0 && exp_appointmentId != 0) {
            fieldSenseTosterError("Please enter valid customer name.", true);
            $("#exp_customerName").focus();
            return false;
        }

        if (exp_customerName.length != 0 || exp_customerName != ' ') {
            if (exp_appointmentId == 'none') {
                fieldSenseTosterError("Visit can not be none.", true);
                $("#exp_appointmentId").focus();
                return false;
            }
        }

    }

    if (exp_appointmentId == 'none') {
        exp_appointmentId = 0;
    }

    if (exp_amtSpent.length == 0 || exp_amtSpent == ' ') {
        fieldSenseTosterError("Expense amount can not be blank.", true);
        $("#exp_amtSpent").focus();
        return false;
    }

    if (exp_amtSpent <= 0) {
        fieldSenseTosterError("Please enter valid expense amount.", true);
        $("#exp_amtSpent").focus();
        return false;
    }

    var expAmountRegex = /^\d+(\.\d+)?$/.test(exp_amtSpent);
    if (!expAmountRegex) {
        fieldSenseTosterError("Please enter valid expense amount.", true);
        $("#exp_amtSpent").focus();
        return false;
    }


// for updating image with other data if inputFile is not undefined
var inputFile = $("input[type=file]")[0].files[0];
    var inputFileSz = $("input[type=file]").length;
   // alert(inputFileSz);
    if (typeof inputFile != 'undefined') {
        var expenseImageData = new FormData();
        var isFileFormat = true;
        var inputFile = $("input[type=file]")[0].files[0];

        if (inputFile.type.indexOf("image") != 0) {
            isFileFormat = false;
            return false;
        }
        if (isFileFormat == false) {
            fieldSenseTosterError("Please upload proper image file.");
            return false;
        }
        expenseImageData.append('fileupload[0]', inputFile);

        enableDisableOnSumbitButton("disable", "edit"); // to disable the save button to avoid multiple selection of save
        $('#pleaseWaitDialog').modal('show');

        $.ajax({
            url: fieldSenseURL + "/expense/image/save",
            type: 'POST',
            headers: {
                "userToken": userToken
            },
            data: expenseImageData,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            success: function (data) {
                if (data.errorCode == '0000') {
                    var imageName = data.result;

                    var jsondata = {
                        "id": expensearray[index].expense_data.id,
                        "appointmentId": exp_appointmentId,
                        "customerId": exp_customerId,
                        "user": {
                            "id": expensearray[index].expense_data.user.id
                        },
                        "expenseName": exp_headName,
                        "expenseType": expensearray[index].expense_data.expenseType,
                        "description": expensearray[index].expense_data.description,
                        "amountSpent": exp_amtSpent,
                        "status": 0, // self editing in pending approval by reporting head mode
                        "expenseTime": new Date(exp_dateTime),
                        "submittedOn": new Date(expensearray[index].expense_data.submittedOn.time),
                        "createdOn": new Date(expensearray[index].expense_data.createdOn.time),
                        // need to add updated on parameter
                        "eCategoryName": {
                            "id": exp_Category_id
                        },
                        "exp_category_name": expCategoryName,
                        "expenseImage": {
                            "id": 1, // 1 to indicate image is added true
                            "imageURL": imageName
                        }
                    }

                    var url = fieldSenseURL + "/expense";
                    var jsonData = JSON.stringify(jsondata);

                    $.ajax({
                        type: "PUT",
                        url: url,
                        contentType: "application/json; charset=utf-8",
                        headers: {
                            "userToken": userToken
                        },
                        crossDomain: false,
                        data: jsonData,
                        cache: false,
                        dataType: 'json',
                        asyn: true,
                        success: function (data) {
                            if (data.errorCode == '0000') {
                                $('#expenses').modal('toggle');
                                fieldSenseTosterSuccess(data.errorMessage, true);
                                $(".group-checkable").attr('checked', false);
                                $(".group-checkable").parent().removeClass('checked');
                                var table = $('#example').DataTable();
                                table.draw(false);
                            } else {
                                enableDisableOnSumbitButton("enable", "edit");
                                $('#pleaseWaitDialog').modal('hide');
                                fieldSenseTosterError(data.errorMessage, true);
                            }

                        },
                        error: function (xhr, ajaxOptions, thrownError)
                        {
                            enableDisableOnSumbitButton("enable", "edit");
                            $('#pleaseWaitDialog').modal('hide');
                            alert("in error:" + thrownError);
                        }
                    });
                } else {
                    enableDisableOnSumbitButton("enable", "edit");
                    $('#pleaseWaitDialog').modal('hide');
                    fieldSenseTosterError(data.errorMessage, true);
                }
            },
            error: function (xhr, ajaxOptions, thrownError)
            {
                enableDisableOnSumbitButton("enable", "edit");
                $('#pleaseWaitDialog').modal('hide');
                alert("in error:" + thrownError);
            }
        });

    } else if (typeof inputFile == 'undefined') {
        var jsondata = {
            "id": expensearray[index].expense_data.id,
            "appointmentId": exp_appointmentId,
            "customerId": exp_customerId,
            "user": {
                "id": expensearray[index].expense_data.user.id
            },
            "expenseName": exp_headName,
            "expenseType": expensearray[index].expense_data.expenseType,
            "description": expensearray[index].expense_data.description,
            "amountSpent": exp_amtSpent,
            "status": 0, // self editing in pending approval by reporting head mode
            "expenseTime": new Date(exp_dateTime),
            "submittedOn": new Date(expensearray[index].expense_data.submittedOn.time),
            "createdOn": new Date(expensearray[index].expense_data.createdOn.time),
            // add updated on parameter
            "eCategoryName": {
                "id": exp_Category_id
            },
            "exp_category_name": expCategoryName
        }

        var url = fieldSenseURL + "/expense";
        var jsonData = JSON.stringify(jsondata);

        enableDisableOnSumbitButton("disable", "edit");
        $('#pleaseWaitDialog').modal('show');

        $.ajax({
            type: "PUT",
            url: url,
            contentType: "application/json; charset=utf-8",
            headers: {
                "userToken": userToken
            },
            crossDomain: false,
            data: jsonData,
            cache: false,
            dataType: 'json',
            asyn: true,
            success: function (data) {
                if (data.errorCode == '0000') {
                    $('#expenses').modal('toggle');
                    fieldSenseTosterSuccess(data.errorMessage, true);
                    $(".group-checkable").attr('checked', false);
                    $(".group-checkable").parent().removeClass('checked');
                    var table = $('#example').DataTable();
                    table.draw(false);
                } else {
                    enableDisableOnSumbitButton("enable", "edit");
                    $('#pleaseWaitDialog').modal('hide');
                    fieldSenseTosterError(data.errorMessage, true);
                }

            },
            error: function (xhr, ajaxOptions, thrownError)
            {
                enableDisableOnSumbitButton("enable", "edit");
                $('#pleaseWaitDialog').modal('hide');
                alert("in error:" + thrownError);
            }
        });
    }

}
/**
 * @Added by Siddhesh
 * @Purpose : to edit own expenses only if pending approval by head [ status = 0] and userid == loginuserid
 * @param {type} index
 * @returns {undefined}
 */
function editLoggedInUserPendingExpensesMultiImages(index) {

    var exp_headName = $('#exp_headName').val().trim();
    var exp_Category_id = $('#exp_Category_id').val().trim();
    var expCategoryName = $("#exp_Category_id option:selected").text().trim();
    var exp_dateTime = $('#exp_dateTime').val().trim();
    var exp_customerId = $('#id_hiddenCustValue').val();
    var exp_customerName = $('#exp_customerName').val().trim();
    var exp_appointmentId = $('#exp_appointmentId').val();
    var exp_amtSpent = $('#exp_amtSpent').val().trim();
    var selectVisitIsChecked = $('#id_showCustVisit').prop("checked");

// validation
    if (exp_headName.length == 0 || exp_headName == ' ') {
        fieldSenseTosterError("Expense head can not be blank.", true);
        $("#exp_headName").focus();
        return false;
    }

    if (exp_headName.length > 50) {
        fieldSenseTosterError("Expense head can not be more than 50 characters.", true);
        $("#exp_headName").focus();
        return false;
    }

    if (exp_dateTime.trim().length == 0) {
        fieldSenseTosterError("Date & Time can not be blank.", true);
        $("#exp_dateTime").focus();
        return false;
    }

    if (selectVisitIsChecked) {

        if (exp_customerName.length == 0 || exp_customerName == ' ') {
//            $('#exp_appointmentId option[value=none]').attr('selected', 'selected');
            exp_appointmentId = 0;
            exp_customerId = 0;
        }

        if (exp_customerId == 0 && exp_appointmentId != 0) {
            fieldSenseTosterError("Please enter valid customer name.", true);
            $("#exp_customerName").focus();
            return false;
        }

        if (exp_customerName.length != 0 || exp_customerName != ' ') {
            if (exp_appointmentId == 'none') {
                fieldSenseTosterError("Visit can not be none.", true);
                $("#exp_appointmentId").focus();
                return false;
            }
        }

    }

    if (exp_appointmentId == 'none') {
        exp_appointmentId = 0;
    }

    if (exp_amtSpent.length == 0 || exp_amtSpent == ' ') {
        fieldSenseTosterError("Expense amount can not be blank.", true);
        $("#exp_amtSpent").focus();
        return false;
    }

    if (exp_amtSpent <= 0) {
        fieldSenseTosterError("Please enter valid expense amount.", true);
        $("#exp_amtSpent").focus();
        return false;
    }

    var expAmountRegex = /^\d+(\.\d+)?$/.test(exp_amtSpent);
    if (!expAmountRegex) {
        fieldSenseTosterError("Please enter valid expense amount.", true);
        $("#exp_amtSpent").focus();
        return false;
    }


// for updating image with other data if inputFile is not undefined
    var inputFileSz = $("input[type=file]").length;
    $('#pleaseWaitDialog').modal('show');    
    //alert(inputFileSz);
//var inputFile = $("input[type=file]")[0].files[0];
//    if (typeof inputFile != 'undefined') {
//        var expenseImageData = new FormData();
//        var isFileFormat = true;
//        var inputFile = $("input[type=file]")[0].files[0];
//
//        if (inputFile.type.indexOf("image") != 0) {
//            isFileFormat = false;
//            return false;
//        }
//        if (isFileFormat == false) {
//            fieldSenseTosterError("Please upload proper image file.");
//            return false;
//        }
//        expenseImageData.append('fileupload[0]', inputFile);
//
//        enableDisableOnSumbitButton("disable", "edit"); // to disable the save button to avoid multiple selection of save
//        $('#pleaseWaitDialog').modal('show');

//        $.ajax({
//            url: fieldSenseURL + "/expense/image/save",
//            type: 'POST',
//            headers: {
//                "userToken": userToken
//            },
//            data: expenseImageData,
//            async: false,
//            cache: false,
//            contentType: false,
//            processData: false,
//            success: function (data) {
//                if (data.errorCode == '0000') {
//                    var imageName = data.result;
var imageNameList = [];
var idList = [];
for(var i=0;i<inputFileSz;i++){
    var inputFile = $("input[type=file]")[i].files[0];
    var idServer =  $("input[type=file]")[i].id+"";
   // alert(idServer);
    //alert(inputFile.name);
 if (typeof inputFile != 'undefined' && idServer!=="image_file" && idServer!=="upload_expenseImage") {
        var expenseImageData = new FormData();
        var isFileFormat = true;
        //var inputFile = $("input[type=file]")[0].files[0];

        if (inputFile.type.indexOf("image") != 0) {
            isFileFormat = false;
            return false;
        }
        if (isFileFormat == false) {
            fieldSenseTosterError("Please upload proper image file.");
            return false;
        }
        expenseImageData.append('fileupload[0]', inputFile);

        enableDisableOnSumbitButton("disable", "edit"); // to disable the save button to avoid multiple selection of save
        
 
            $.ajax({
            url: fieldSenseURL + "/expense/image/save",
            type: 'POST',
            headers: {
                "userToken": userToken
            },
            data: expenseImageData,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            success: function (data) {
                if (data.errorCode == '0000') {
                    var imageName = data.result;
                    imageNameList.push(imageName);
                    idList.push($.now());
    }
    }
    }); 
           }else{
               //alert(imageNameList.length + idList.length);
               if(idServer === '-1' || !idServer === null || !idServer === '' || !idServer === ' ' || !idServer === -1 || idServer==="image_file" ||  idServer==="upload_expenseImage"){
                   
               }else{
                   imageNameList.push("https://");
                   idList.push(idServer); 
               }
           }
}
$.when.apply(null, imageNameList,idList).done(function() {
    var imageList= [];
 //   alert(imageNameList.length);
    
    for(var j =0 ; j<imageNameList.length ; j++){
       // alert(imageNameList[j]);
         var ExpenseImage ={
             "id" :idList[j],
             "imageURL" : imageNameList[j]
         }
        imageList.push(ExpenseImage);
    }

    //alert("DoneBoss"+imageNameList.length+idList.length)
                        var jsondata = {
                        "id": expensearray[index].expense_data.id,
                        "appointmentId": exp_appointmentId,
                        "customerId": exp_customerId,
                        "user": {
                            "id": expensearray[index].expense_data.user.id
                        },
                        "expenseName": exp_headName,
                        "expenseType": expensearray[index].expense_data.expenseType,
                        "description": expensearray[index].expense_data.description,
                        "amountSpent": exp_amtSpent,
                        "status": 0, // self editing in pending approval by reporting head mode
                        "expenseTime": new Date(exp_dateTime),
                        "submittedOn": new Date(expensearray[index].expense_data.submittedOn.time),
                        "createdOn": new Date(expensearray[index].expense_data.createdOn.time),
                        // need to add updated on parameter
                        "eCategoryName": {
                            "id": exp_Category_id
                        },
                        "exp_category_name": expCategoryName,
                        "expenseImageArray" :imageList
//                        "expenseImage": {
//                            "id": 1, // 1 to indicate image is added true
//                            "imageURL": imageName
//                        }
                    }

                    var url = fieldSenseURL + "/expense/updateExpense";
                    var jsonData = JSON.stringify(jsondata);

                    $.ajax({
                        type: "PUT",
                        url: url,
                        contentType: "application/json; charset=utf-8",
                        headers: {
                            "userToken": userToken
                        },
                        crossDomain: false,
                        data: jsonData,
                        cache: false,
                        dataType: 'json',
                        asyn: true,
                        success: function (data) {
                            if (data.errorCode == '0000') {
                                $('#expenses').modal('toggle');
                                fieldSenseTosterSuccess(data.errorMessage, true);
                                $(".group-checkable").attr('checked', false);
                                $(".group-checkable").parent().removeClass('checked');
                                 $('#pleaseWaitDialog').modal('hide');
                                var table = $('#example').DataTable();
                                table.draw(false);
                            } else {
                                enableDisableOnSumbitButton("enable", "edit");
                                $('#pleaseWaitDialog').modal('hide');
                                fieldSenseTosterError(data.errorMessage, true);
                            }

                        },
                        error: function (xhr, ajaxOptions, thrownError)
                        {
                            enableDisableOnSumbitButton("enable", "edit");
                            $('#pleaseWaitDialog').modal('hide');
                            alert("in error:" + thrownError);
                        }
                    });
});
//                } else {
//                    enableDisableOnSumbitButton("enable", "edit");
//                    $('#pleaseWaitDialog').modal('hide');
//                    fieldSenseTosterError(data.errorMessage, true);
//                }
//            },
//            error: function (xhr, ajaxOptions, thrownError)
//            {
//                enableDisableOnSumbitButton("enable", "edit");
//                $('#pleaseWaitDialog').modal('hide');
//                alert("in error:" + thrownError);
//            }
//        });

//    } else if (typeof inputFile == 'undefined') {
//        var jsondata = {
//            "id": expensearray[index].expense_data.id,
//            "appointmentId": exp_appointmentId,
//            "customerId": exp_customerId,
//            "user": {
//                "id": expensearray[index].expense_data.user.id
//            },
//            "expenseName": exp_headName,
//            "expenseType": expensearray[index].expense_data.expenseType,
//            "description": expensearray[index].expense_data.description,
//            "amountSpent": exp_amtSpent,
//            "status": 0, // self editing in pending approval by reporting head mode
//            "expenseTime": new Date(exp_dateTime),
//            "submittedOn": new Date(expensearray[index].expense_data.submittedOn.time),
//            "createdOn": new Date(expensearray[index].expense_data.createdOn.time),
//            // add updated on parameter
//            "eCategoryName": {
//                "id": exp_Category_id
//            },
//            "exp_category_name": expCategoryName
//        }
//
//        var url = fieldSenseURL + "/expense";
//        var jsonData = JSON.stringify(jsondata);
//
//        enableDisableOnSumbitButton("disable", "edit");
//        $('#pleaseWaitDialog').modal('show');
//
//        $.ajax({
//            type: "PUT",
//            url: url,
//            contentType: "application/json; charset=utf-8",
//            headers: {
//                "userToken": userToken
//            },
//            crossDomain: false,
//            data: jsonData,
//            cache: false,
//            dataType: 'json',
//            asyn: true,
//            success: function (data) {
//                if (data.errorCode == '0000') {
//                    $('#expenses').modal('toggle');
//                    fieldSenseTosterSuccess(data.errorMessage, true);
//                    $(".group-checkable").attr('checked', false);
//                    $(".group-checkable").parent().removeClass('checked');
//                    var table = $('#example').DataTable();
//                    table.draw(false);
//                } else {
//                    enableDisableOnSumbitButton("enable", "edit");
//                    $('#pleaseWaitDialog').modal('hide');
//                    fieldSenseTosterError(data.errorMessage, true);
//                }
//
//            },
//            error: function (xhr, ajaxOptions, thrownError)
//            {
//                enableDisableOnSumbitButton("enable", "edit");
//                $('#pleaseWaitDialog').modal('hide');
//                alert("in error:" + thrownError);
//            }
//        });
//    }

}

function selectAllCheck() {

    if ($(".group-checkable").is(':checked')) {
        //reject=[];
        //approve=[];
        $(".checkboxes").each(function () {
            if ($(this).attr("disabled") != "disabled") {
                $(this).attr('checked', true);
                $(this).parent().attr('class', 'checked');
                /*var index=$(this).attr("id").substring(9,$(this).attr("id").length);
                 if(expensearray[index].expense_data.status==1){    
                 approve.push({"element":$(this),"id":$(this).attr("id")});    
                 }
                 reject.push({"element":$(this),"id":$(this).attr("id")});*/
                $('#btnApprovepo').css("display", "inline-block");
            }

        });
    } else {
        $(".checkboxes").each(function () {
            $(this).attr('checked', false);
            $(this).parent().removeClass('checked');
        });
        //reject=[];
        //approve=[];
        $('#btnApprovepo').css("display", "none");
    }
}

/**
 * @added by jyoti
 * @param {type} sourceArray
 * @returns {undefined}
 */
function searchCustomerAutoComplete(sourceArray) {
    sourceArray.id_customerName.autocomplete({
        minChars: 3,
        //lookup: customer,
        paramName: 'searchText',
        transformResult: function (response) {
            return {
                suggestions: $.map(response.result, function (dataItem) {
                    return {value: dataItem.value, id: dataItem.customerId};
                })
            };
        },
        dataType: "json",
        serviceUrl: fieldSenseURL + '/customer/onlyWhoHaveVisit?userToken=' + userToken,
        onSelect: function (suggestion) {
            sourceArray.id_hiddenCustValue.val(suggestion.id);
            visitRelatedToSelectedCustomer(suggestion.id, -1, sourceArray.id_appointmentId);
        }
    });
}

function convertToLocalTime(submittedon) {
    var submittedon = submittedon;
    var timeZoneOffSet = submittedon.timezoneOffset * 60 * 1000;
    submittedon = new Date(submittedon.time - timeZoneOffSet);
    submittedon = convertServerDateToLocalTimezone(submittedon.getFullYear(), submittedon.getMonth(), submittedon.getDate(), submittedon.getHours(), submittedon.getMinutes(), timeZoneOffSet);
    submittedon = submittedon.getFullYear() + "-" + (submittedon.getMonth() + 1) + "-" + submittedon.getDate() + " " + submittedon.getHours() + ":" + submittedon.getMinutes() + ":00.0";
    submittedon = fieldSenseseForJSDate(submittedon);
    return submittedon;
}

function clearReason() {
    $("#reason_text").val('');
}

function reloadImageOnEdit(imageName) {
//    console.log("imageName : "+imageName);
    var img = document.getElementById('old_expenseImage');
    $('#old_expenseImage').val('');
    img.src = '#';
    var imagenameWithTimestamp = imageName + 'random=' + new Date().getTime();
    img.src = imagenameWithTimestamp;
}

function reloadImageOnAdd() {
    var img = document.getElementsByClassName('thumbnail');
    $('.thumbnail').val('');
    img.src = '#';
}