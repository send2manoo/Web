/*
 * @Added by sanchita, 22-09-2018
 */
var users_emp_codes_map = new Object();
var loginUserIdcookie = fieldSenseGetCookie("loginUserIdcookie");
var loginUserNamecookie = fieldSenseGetCookie("loginUserNamecookie");
var accountIdcookie = fieldSenseGetCookie("accountIdcookie");
var accountNamecookie = fieldSenseGetCookie("accountNamecookie");
var UserRolecookie = fieldSenseGetCookie("UserRolecookie");

var intervalAdminUser = window.setInterval(function () {
    var userToken2 = fieldSenseGetCookie("userToken");
    try {
        if (userToken2.toString() == "undefined") {
            window.location.href = "login.html";
        } else if (loginUserId != 0) {
            loggedinUserImageData();
            loggedinUserData();
            teamMembers();
            CustomFormType();
            $('#s2id_autogen1 > a > span:eq(0)').text("20");
            window.clearTimeout(intervalAdminUser);
        }
    } catch (err) {
        window.location.href = "login.html";
    }
}, 1000);

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
                    users_emp_codes_map[userId] = teamMemberData[i].emp_code;
                    var firstName = teamMemberData[i].user.firstName;
                    var lastName = teamMemberData[i].user.lastName;
                    var fullName = firstName + ' ' + lastName;
                    teamMemberTemplate = '<option value="' + userId + '">' + fullName + '</option>';
                    $('#all_users_forms').append(teamMemberTemplate);
                }
            }
        }
    });
}

function CustomFormType() {
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/adminCustomForms/all",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        asyn: true,
        success: function (data) {
            if (data.errorCode == '0000') {
                var formid = 1; // get this from return data
                var addformName = 'SampleName';
                var formList = data.result;
                var templ = "";

                templ += '<option value="0" selected>Select Form</option>';
                for (var i = 0; i < formList.length; i++)
                {
                    templ += '<option value="' + formList[i].id + '">' + formList[i].formName + '</option>';//                   
                }
                $("#formListMenu").html(templ);
            }
        }
    });

}

var formHeaders = [];
function loadCustomFormReportPage() {
    formHeaders = [];
    var id = document.getElementById("formListMenu").value;
    var custForm = '';
    if (id == 0) {
        fieldSenseTosterError("Please select form type", true);
        return false;
    }
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/adminCustomForms/" + id,
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
                 $('.cls_form').html('');
                custForm += '<table class="table table-striped table-bordered table-hover" id="sample_i5">';
                custForm += '<thead>';
                custForm += '<tr>';              
                custForm += '<th>Submitted By</th>';
                custForm += '<th>Submitted On</th>';
                for (var i = 0; i < data.result.tableData.length; i++) {
                    custForm += '<th>';
                    custForm += data.result.tableData[i].labelName;
                    custForm += '</th>';
                    formHeaders.push({"labelName": data.result.tableData[i].labelName, "fieldId": data.result.tableData[i].fieldId, "fieldType": data.result.tableData[i].fieldType});
                }
                custForm += '<th></th>';
                custForm += '</tr>';
                custForm += '</thead>';
                custForm += '<tbody> </tbody></table>';
            }

            $('.cls_form').html(custForm);
            //load DataTable

            $('#sample_i5').dataTable({
                //"aaSorting": [[1, 'asc']],
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
                "aaSorting": [],
                "aoColumnDefs": [{"bSortable": false, "aTargets": [formHeaders.length + 2]}]
            });

            var reportTable = $('#sample_i5').dataTable();

            reportTable.fnSetColumnVis(0, false);

            jQuery('#sample_i5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
            jQuery('#sample_i5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
            jQuery('#sample_i5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown
            //DataTable Done
            formReports();
//            ('#pleaseWaitDialog').modal('hide');

        },
        error: function (xhr, ajaxOptions, erroThrown) {
            ('#pleaseWaitDialog').modal('hide');
            alert("erroThrown:" + erroThrown);
        }
    });
}

var imagemap = new Object();
var imageCellValue = "";
var allimagemap = new Object();
var allimageCellValue = "";
function formReports()
{
    //imagemap = new Object();
    imageCellValue = "";
    // allimagemap = new Object();
    allimageCellValue = "";

    $("#downloadimagesaszip_btn").attr("onclick", "downloadzipImagesUsers(" + id + ")");
    $("#id_exportAttendance").html('<button type="button" class="btn btn-info fr" style="margin-right:-15px;"><i class=" fa fa-file-text" id="id_exportAttendanceChild"></i> Save as CSV</button> ');
    //$("#id_exportAttendance").append('<button id="downloadimagesaszip_btn" onclick="zipImagesAndCsv()" type="button" class="btn btn-info fr" style="margin-right:-18px;margin-left:20px;"><i class=" fa fa-file-text" ></i> Download Images as zip</button>');
    var teamMember = document.getElementById("all_users_forms").value;
    var emp_code; // added by manohar  
    if (teamMember != 'All') {
        emp_code = users_emp_codes_map[teamMember];
    }
    if (teamMember == 'select') {
        fieldSenseTosterError("Please select any member", true);
        return false;
    }
    var id = document.getElementById("formListMenu").value;


    var frmDateArray = new Array();
    var toDateArray = new Array();
    frmDateArray = $("input[name=daterangepicker_start]").val().split('/');
    toDateArray = $("input[name=daterangepicker_end]").val().split('/');
    var fromDate = frmDateArray[2] + '-' + frmDateArray[0] + '-' + frmDateArray[1] + " " + "00" + ":" + "00" + ":" + "00";//"2018-07-31 18:30:00";
    var toDate = toDateArray[2] + '-' + toDateArray[0] + '-' + toDateArray[1] + " " + "23" + ":" + "59" + ":" + "59";

    var url = fieldSenseURL + "/CustomFormsReport/" + id + "/" + teamMember + "/" + fromDate + "/" + toDate;
    if (teamMember == "All")
        url = fieldSenseURL + "/CustomFormsReport/" + id + "/User/" + loginUserId + "/All/" + fromDate + "/" + toDate;

    $('#pleaseWaitDialog').modal('show');
    var imageKey = "";
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
                var concateImagestring="";
                if (tableData == 0)
                {
                    $('#pleaseWaitDialog').modal('hide');
                } else
                {
                    if (teamMember == "All")
                    {
                        //imagezip = new JSZip();

                        for (i = 0; i < tableData.length; i++) {
                            var fields = tableData[i].filledData;
                            add1 = [];
                            var val;
                            var k = 0;
                            add1.push(tableData[i].submitTime.time);//added to sort based on date
                            //add1.push(tableData[i].id)
                            var index=tableData[i].id;
                            add1.push(tableData[i].submittedby);
                            var displayTime = convertToLocalTime(tableData[i].submitTime);
                            var newDisplaytime = displayTime.replace(/[ ,]+/g, " ");
                            add1.push(newDisplaytime);
                            imageKey = newDisplaytime;
                            // var uKey = "key";
                            var cnt = 0;
                            for (j = 0; j < formHeaders.length; j++) {
                                if (k < fields.length) {
                                    if (formHeaders[j].fieldId == fields[k].field_id_fk) {
                                        
                                        if (formHeaders[j].fieldType == "Checkboxes")
                                            val = fields[k].field_value.replace(/\n/g, ', ');
                                        else if (fields[k].fieldType == "Image") {
                                            val = '<a  data-toggle="modal" href="#customformimage" onclick="getCustomFormImage(\'' + id + '\',\'' + fields[k].field_id_fk + '\',\'' + fields[k].form_fill_detail_id_fk + '\',\'' + fields[k].field_value + '\')">' + fields[k].field_value + '</a>';
                                            // added by vaibhav to store link in csv
                                            allimageCellValue = "";
                                            if (fields[k].field_value != '') {                                               
                                                allimageCellValue = formCustomFormImageLink(id, fields[k].field_id_fk, fields[k].form_fill_detail_id_fk);//,fields[k].field_value) //+ "\"" +";"+  "\""  +fields[k].field_value  +"\"" + ")"; 
                                                imageKey = imageKey + " " + fields[k].field_value;
                                                allimagemap[cnt] = encodeURI(allimageCellValue);                                                
                                                //vaibhav
                                                cnt++;
                                            }
                                            else{
                                                allimageCellValue = formCustomFormImageLink(id, fields[k].field_id_fk, fields[k].form_fill_detail_id_fk);//,fields[k].field_value) //+ "\"" +";"+  "\""  +fields[k].field_value  +"\"" + ")"; 
                                                imageKey = imageKey + " " + fields[k].field_value;
                                                allimagemap[cnt] = encodeURI(allimageCellValue);
                                                console.log(allimagemap);
                                                //vaibhav
                                                cnt++;
                                                
                                            }                                            
                                        } else
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
                                                       
                            for (var z = 0; z < Object.keys(allimagemap).length; z++)
                            {
                                //
                                if (Object.keys(allimagemap).length === 1)
                                {
                                    concateImagestring = allimagemap[z]+',';
                                } else
                                {
                                    concateImagestring += allimagemap[z] + ',';
                                }
                            }                             
                            add1.push('<button type="button" class="btn btn-default btn-xs tooltips" data-toggle="modal" href="#formdtl" onclick="showcustform(\'' + id + '\',\'' + concateImagestring + '\',this)" data-placement="bottom" title="View"><i class="fa fa-file-o"></i></button>&nbsp;&nbsp;<button title="Edit" data-placement="bottom" data-toggle="modal" href="#formedt" onclick="Updatecustform(\'' + index + '\',\'' + id + '\',\'' + concateImagestring + '\',this)" data-toggle="modal" class="btn btn-default btn-xs tooltips" type="button"><i class="fa fa-edit"></i></button>');
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
                        //myTable.column(1).visible(true);
                        myTable.rows.add(mainArray).draw();

//                        $("#sample_i5").find('thead th').eq(0).addClass('hideTD');
//                        $("#sample_i5").find('tbody td').eq(0).addClass('hideTD');
                        //tooltip
                        myTable.$('td').mouseover(function () {
                            var sData = $(this).text();
                            $(this).attr('title', sData);
                        });

                        $("#sample_i5").find('td').css({'max-width': '300px', 'overflow': 'hidden', 'text-overflow': 'ellipsis'});
//                    // prepare CSV data
//                    var excelHeader = "";
//                    var csvData = new Array();
//                    var k = 0;
//                    excelHeader += '"Submitted by",';
//                    excelHeader += '"Submitted on",';
//                    while (k < formHeaders.length - 1) {
//
//                        excelHeader += '"' + formHeaders[k].labelName + '",';
//                        k = k + 1;
//
//                    }
//                    excelHeader += '"' + formHeaders[k].labelName + '"';
//                    csvData.push(excelHeader);
//                    excelHeader = "";
//                    var i = 0;
//                    var fieldValue;
//                    while (i < mainArray.length) {
//                        //var j=0;
//                        excelHeader += '"' + mainArray[i][0] + '",';
//                        excelHeader += '"' + mainArray[i][1] + '",';
//                        imageKey = mainArray[i][1]; // added by vaibhav
//                        j = 2;
//                        while (j <= formHeaders.length) {
//                            fieldValue = mainArray[i][j];
//                            if(fieldValue.indexOf("data-toggle=\"modal\"")!==-1){
//                                fieldValue=fieldValue.substring(fieldValue.indexOf(">")+1,fieldValue.lastIndexOf("<"));	
//                                    // added by vaibhav to store image link in csv
//                                    if(fieldValue != ''){            
//                                        imageKey = imageKey + " " + fieldValue;
//                                        fieldValue = allimagemap[imageKey];
//                                    }else{
//                                        fieldValue ='';
//                                    }
//                                }
//                                // ended by vaibhav
//                                excelHeader += '"' + fieldValue + '",';
//                                j = j + 1;
//                            }
//                            fieldValue = mainArray[i][j];
//                            if(fieldValue.indexOf("data-toggle=\"modal\"")!==-1){
//                            // added by vaibhav to store image link in csv
//                                fieldValue=fieldValue.substring(fieldValue.indexOf(">")+1,fieldValue.lastIndexOf("<"));
//                                if(fieldValue != ''){
//                                    imageKey = imageKey + " " + fieldValue;
//                                    fieldValue = allimagemap[imageKey];
//                               }else{
//                                    fieldValue = '';
//                                }
//
//                            } // ended by  vaibhav
//                        excelHeader += '"' + fieldValue + '"';
//                        csvData.push(excelHeader);
//                        excelHeader = "";
//                        i = i + 1;
//                    }
                    } else {
                        // imagezip = new JSZip();
                        for (i = 0; i < tableData.length; i++) {
                            var fields = tableData[i].filledData;
                            add1 = [];
                            var val;
                            var k = 0;
                            add1.push("");
                           // add1.push(tableData[i].submittedby);
                            var index=tableData[i].id;
                            var displayTime = convertToLocalTime(tableData[i].submitTime);
                            var newDisplaytime = displayTime.replace(/[ ,]+/g, " ");
                            add1.push(newDisplaytime);
                            imageKey = newDisplaytime;
                            var cnt=0;
                            for (j = 0; j < formHeaders.length; j++) {
                                if (k < fields.length) {
                                    if (formHeaders[j].fieldId == fields[k].field_id_fk) {
                                        if (formHeaders[j].fieldType == "Checkboxes")
                                            val = fields[k].field_value.replace(/\n/g, ', ');
                                        else if (fields[k].fieldType == "Image") {                                            
                                            val = '<a  data-toggle="modal" href="#customformimage" onclick="getCustomFormImage(\'' + id + '\',\'' + fields[k].field_id_fk + '\',\'' + fields[k].form_fill_detail_id_fk + '\',\'' + fields[k].field_value + '\')">' + fields[k].field_value + '</a>';                                      
                                            // added by vaibhav to store image link in csv
                                            if (fields[k].field_value != '') {
                                                imageCellValue = formCustomFormImageLink(id, fields[k].field_id_fk, fields[k].form_fill_detail_id_fk); //,fields[k].field_value) + "\"" +";"+  "\""  +fields[k].field_value  +"\"" + ")"; 
                                                imageKey = imageKey + " " + fields[k].field_value;
                                                imagemap[cnt] = encodeURI(imageCellValue);
                                                cnt++;
                                                //   ended by vaibhav
                                            }
                                            else{
                                                allimageCellValue = formCustomFormImageLink(id, fields[k].field_id_fk, fields[k].form_fill_detail_id_fk);//,fields[k].field_value) //+ "\"" +";"+  "\""  +fields[k].field_value  +"\"" + ")"; 
                                                imageKey = imageKey + " " + fields[k].field_value;
                                                allimagemap[cnt] = encodeURI(allimageCellValue);
                                                console.log(allimagemap);
                                                //vaibhav
                                                cnt++;
                                                
                                            }
                                        } else
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
//                            var concateImagestring="";
                            for (var z = 0; z < Object.keys(allimagemap).length; z++)
                            {
                                //
                                if (Object.keys(allimagemap).length === 1)
                                {
                                    concateImagestring = allimagemap[z]+',';
                                } else
                                {
                                    concateImagestring += allimagemap[z] + ',';
                                }
                            }        
                            add1.push('<button type="button" class="btn btn-default btn-xs tooltips" data-toggle="modal" href="#formdtl" onclick="showcustform(\'' + id + '\',\'' + concateImagestring + '\',this)" data-placement="bottom" title="View"><i class="fa fa-file-o"></i></button>&nbsp;&nbsp;<button title="Edit" data-placement="bottom" data-toggle="modal" href="#formedt" onclick="Updatecustform(\'' + index + '\',\'' + id + '\',\'' + concateImagestring + '\',this)" data-toggle="modal" class="btn btn-default btn-xs tooltips" type="button"><i class="fa fa-edit"></i></button>');
                            mainArray.push(add1);
                        }

                        var myTable = $('#sample_i5').DataTable();
                        myTable.clear();
                        myTable.order([1, "desc"]);
                        myTable.column(0).visible(false);
                        //myTable.column(1).visible(true);
                        myTable.rows.add(mainArray).draw();

//                        $("#sample_i5").find('thead th').eq(0).addClass('hideTD');
//                        $("#sample_i5").find('tbody td').eq(0).addClass('hideTD');
//                        $("#sample_i5").find('thead th').eq(1).addClass('hideTD');
//                        $("#sample_i5").find('tbody td').eq(1).addClass('hideTD');

                        //tooltip
                        myTable.$('td').mouseover(function () {
                            var sData = $(this).text();
                            $(this).attr('title', sData);
                        });

                        $("#sample_i5").find('td').css({'max-width': '300px', 'overflow': 'hidden', 'text-overflow': 'ellipsis'});

                        // prepare CSV data
//                    var excelHeader = "";
                        var csvData = new Array();
//                    var k = 0;
//                    excelHeader += '"Submitted on",';
//                    while (k < formHeaders.length - 1) {
//
//                        excelHeader += '"' + formHeaders[k].labelName + '",';
//                        k = k + 1;
//
//                    }
//                    excelHeader += '"' + formHeaders[k].labelName + '"';
//                    csvData.push(excelHeader);
//                    excelHeader = "";
//                    var i = 0;
//                    var fieldValue;
//                    while (i < mainArray.length) {
//                        imageKey = mainArray[i][1]; // added by vaibhav
//                        excelHeader += '"' + mainArray[i][1] + '",';
//                        var j = 2;
//                        while (j <= formHeaders.length) {
//                            fieldValue = mainArray[i][j];
//                            if(fieldValue.indexOf("data-toggle=\"modal\"")!=-1){
//                                fieldValue=fieldValue.substring(fieldValue.indexOf(">")+1,fieldValue.lastIndexOf("<"));
//                                   // added by vaibhav to store image link in csv
//                                if(fieldValue != ''){
//                                     imageKey = imageKey + " " + fieldValue;
//                                    fieldValue = imagemap[imageKey];
//                                }else{
//                                   fieldValue =''; 
//                                }
//
//                            } // ended by vai
//                            excelHeader += '"' + fieldValue + '",';
//                            j = j + 1;
//                        }
//                        fieldValue = mainArray[i][j];
//                        if(fieldValue.indexOf("data-toggle=\"modal\"")!=-1){
//                            // added by vaibhav
//                            fieldValue=fieldValue.substring(fieldValue.indexOf(">")+1,fieldValue.lastIndexOf("<"));
//                            if(fieldValue != ''){
//                                 imageKey = imageKey + " " + fieldValue;
//                                 fieldValue = imagemap[imageKey];
//                            }else{
//                               fieldValue =''; 
//                            }
//                       }
//                       // ended by vaibhav
//                        excelHeader += '"' + fieldValue + '"';
//
//                        csvData.push(excelHeader);
//                        excelHeader = "";
//                        i = i + 1;
//                    }

                        /* for (var i = 0; i < mainArray.length; i++) {
                         for (var j = 0; j < formHeaders.length; j++) {
                         csvData.push('"' + exportExpense[i][j].expenseName + '","' + exportExpense[i][j].eCategory + '","' + exportExpense[i][j].ammount + '","' + exportExpense[i][j].dateTime + '","' + exportExpense[i][j].customer + '","' + exportExpense[i][j].activity + '","' + exportExpense[i][j].st + '","' + exportExpense[i][j].approveBy + '","' + exportExpense[i][j].approveOn + '","' + exportExpense[i][j].reason + '"');
                         }
                         }*/
                        // download stuff

//                    if (csvData.length > 1) {
//                        var fileName;
//                        $(".leftnav_form").each(function () {
//                            if ($(this).hasClass("active")) {
//                                fileName = $(this).find(".membername").text();
//                            }
//                        });
//                            if(emp_code.length==0){ // added by manohar
//                                    var fileName = fileName + "_" + $("#id_memberName option:selected").text() +".csv";
//                                }
//                            else
//                            {
//                                 var fileName = fileName + "_" + $("#id_memberName option:selected").text() +'_'+emp_code+ ".csv";
//                            }
//                        var buffer = csvData.join("\n");
//                        var blob;
//                        try {
//                            blob = new Blob([buffer], {
//                                "type": "text/csv;charset=utf8;"
//                            });
//                        } catch (e) {
//                            window.BlobBuilder = window.BlobBuilder ||
//                                    window.WebKitBlobBuilder ||
//                                    window.MozBlobBuilder ||
//                                    window.MSBlobBuilder;
//
//                            if (e.name == 'TypeError' && window.BlobBuilder) {
//                                var bb = new BlobBuilder();
//                                bb.append(buffer);
//                                blob = bb.getBlob("text/csv;charset=utf8;");
//                            }
//                            else if (e.name == "InvalidStateError") {
//                                // InvalidStateError (tested on FF13 WinXP)
//                                blob = new Blob([buffer], {
//                                    type: "text/csv;charset=utf8;"
//                                });
//                            }
//                        }
//                        var link = document.createElement("a");
//                        var myURL = window.URL || window.webkitURL;
//                        if (link.download !== undefined) { // feature detection
//                            // Browsers that support HTML5 download attribute
//                            link.setAttribute("href", myURL.createObjectURL(blob));
//                            link.setAttribute("download", fileName);
//                        }
//                        else if (navigator.msSaveBlob) { // IE 10+
//                            navigator.msSaveBlob(blob, fileName);
//                        }
//                        else {
//                            link.setAttribute("href", myURL.createObjectURL(blob));
//                            link.setAttribute("name", fileName);
//                        }
//                        $('#id_exportAttendance').html('');
//                        link.innerHTML = '<button type="button" class="btn btn-info fr" style="margin-right:-15px;"><i class=" fa fa-file-text"></i> Save as CSV</button>';
//                        document.getElementById("id_exportAttendance").appendChild(link);
//                        //var dnloddiv = document.getElementById("downloadimagezipdiv");
//                        //var  newHTML ='<button id="downloadimagesaszip_btn" onclick="zipImagesAndCsv()" type="button" class="btn btn-info fr" style="margin-right:-18px;margin-left:20px;"><i class=" fa fa-file-text" ></i> Download Images as zip</button>';
//                        //dnloddiv.innerHtml = newHTML;
//                    } else {
//                        // Added by jyoti
//                        var link = document.createElement("a");
//                        $('#id_exportFormReport').html('');
//                        link.innerHTML = '<button type="button" id="save_as_csv_btn" class="btn btn-info fr"  style="margin-right:-15px;"><i class=" fa fa-file-text"></i> Save as CSV</button>';
//                        $('#id_exportAttendance').html(link);
//                        document.getElementById("save_as_csv_btn").addEventListener("click", function () {
//                            noDataAvailableOnZipAndCsv();
//                        }, false);
//                        
//                        $("#downloadimagesaszip_btn").attr("onclick", "noDataAvailableOnZipAndCsv()");
//                        // ended by jyoti
//                    }
                        //prepareImageandCsvforSingleUser(mainArray,imagemap);
                    }
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

function convertToLocalTime(submittedon) {
    var submittedon = submittedon;
    var timeZoneOffSet = submittedon.timezoneOffset * 60 * 1000;
    submittedon = new Date(submittedon.time - timeZoneOffSet);
    submittedon = convertServerDateToLocalTimezone(submittedon.getFullYear(), submittedon.getMonth(), submittedon.getDate(), submittedon.getHours(), submittedon.getMinutes(), timeZoneOffSet);
    submittedon = submittedon.getFullYear() + "-" + (submittedon.getMonth() + 1) + "-" + submittedon.getDate() + " " + submittedon.getHours() + ":" + submittedon.getMinutes() + ":00.0";
    submittedon = fieldSenseseForJSDate(submittedon);
    return submittedon;
}



function Updatecustform(index ,id, strImgsPath, object) {    
    var imgsStr=strImgsPath;
    imgsStr=imgsStr.substr(0,imgsStr.length-1);
    var imgArray=[];
    imgArray=imgsStr.split(',');

    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/predefinedList/getAllListData/" + id,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        asyn: false,
        success: function (data) {
            masterDataList = data.result;
            userDefinedList = masterDataList.UserDefinedList;
            systemDefinedList = masterDataList.SystemDefinedList;

            $(".leftnav_form").removeClass("active");
            $("#leftnav_form_" + id).addClass("active");

            var submitcaption = "Save";
            var preview = '<div class="portlet"><div class="portlet-title"><div class="caption"></div></div><div class="portlet-body form"><form class="form-horizontal"  role="form"  method="POST" id="submit_form" name="submit_form"><div class="form-body">';
            var type = "";
            var options = [];
            var caption;
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
            var tableData = [];
            var editable = "";
            var fieldid;
            var editField;
            var mandField;
            var listId;
            var listName;
            var listType;
            var HHArray = [];
            var MMArray = [];
            var FormatArray = [];

            $.ajax({
                type: "GET",
                url: fieldSenseURL + "/adminCustomForms/" + id, //need to change this url
                contentType: "application/json; charset=utf-8",
                headers: {
                    "userToken": userToken
                },
                crossDomain: false,
                cache: false,
                dataType: 'json',
                asyn: false,
                success: function (data) {
                    tableData = data.result.tableData;
                    caption = data.result.formName;
                    var teamMember = document.getElementById("all_users_forms").value;
                    if (teamMember != 'All') {
                        var p = 1;
                    } else
                    {
                        var p = 2;
                    }
                    var imgcnt=0;
                    $("#formedt1").html('');       
                    for (var i = 0; i < tableData.length; i++)
                    {
                        type = tableData[i].fieldType;
                        editable = tableData[i].editField;
                        fieldid = tableData[i].fieldId;
                        submitcaption = data.result.submit_caption;
                        editField = tableData[i].editField;
                        mandField = tableData[i].mandField;
                        listId = tableData[i].listIdFK;
                        listName = tableData[i].listName;
                        listType = tableData[i].listType;                        
                        if (mandField == 1) {
                            preview += '<div class="form-group"><label class="col-md-4 control-label" >' + tableData[i].labelName + '<span style="color:red"> * </label>';
                        } else {
                            preview += '<div class="form-group"><label class="col-md-4 control-label">' + tableData[i].labelName + '</label>';
                        }

                        if (type == 'Textbox') {
                            if (editField == 1) {

                                preview += '<div class="col-md-8"> <input type="text" name="Textbox" id="' + fieldid + '" class="form-control mand' + mandField + '" placeholder="' + tableData[i].placeHolder + '" ' + editable + ' value="' + $(object).closest('tr').find('td').eq(p).text() + '"></div> ';
                            } else {
                                preview += '<div class="col-md-8"> <input type="text" name="Textbox" id="' + fieldid + '" class="form-control mand' + mandField + '" placeholder="' + tableData[i].placeHolder + '" ' + editable + '  value="' + $(object).closest('tr').find('td').eq(p).text() + '" readonly></div> ';
                            }
                        } else if (type == 'Number') {
                            if (editField == 1) {
                                preview += '<div class="col-md-8"> <input type="text" name="Number" id="' + fieldid + '" class="form-control mand' + mandField + '" placeholder="' + tableData[i].placeHolder + '" ' + editable + ' value="' + $(object).closest('tr').find('td').eq(p).text() + '"></div> ';
                            } else {
                                preview += '<div class="col-md-8"> <input type="text" name="Number" id="' + fieldid + '" class="form-control mand' + mandField + '" placeholder="' + tableData[i].placeHolder + '" ' + editable + ' value="' + $(object).closest('tr').find('td').eq(p).text() + '"  readonly></div> ';
                            }
                        } else if (type == 'Dropdown') {
                            if (editField == 1) {
                                preview += ' <div class="col-md-8"><select id="' + fieldid + '" name="Dropdown" class="form-control mand' + mandField + '" ' + editable + '>';//value=' + abc[p] + '                                
                            } else {
                                preview += ' <div class="col-md-8"><select name="Dropdown" id="' + fieldid + '" class="form-control mand' + mandField + '" ' + editable + ' disabled>';
                            }
                            preview += '<option value="---select---">---select---</option>';
                            options = tableData[i].option.split('\n');

                            for (var j = 0; j < options.length; j++) {
                                if (options[j] == $(object).closest('tr').find('td').eq(p).text())
                                {
                                    preview += "<option value='" + options[j] + "' selected>" + options[j] + "</option>";
                                } else {
                                    preview += "<option value='" + options[j] + "'>" + options[j] + "</option>";
                                }

                                // preview += "<option value='" + options[j] + "'>" + options[j] + "</option>";
                            }
                            preview += "</select></div>";

                        } else if (type == 'Date') {

                            preview += '<div class="col-md-8">';
                            if (editField == 1) {
                                preview += '<div class = "input-group date-picker input-daterange" data - date = "20/11/2012" data-date-format="dd/mm/yyyy">';
                                preview += '<input type = "text" class = "form-control mand' + mandField + '" name = "Date" id="' + fieldid + '" ' + editable + ' value="' + $(object).closest('tr').find('td').eq(p).text() + '">';//value="' + firstDay + '"
                            } else {
                                preview += '<div class = "input-group  input-daterange" data - date = "20/11/2012" data-date-format="dd/mm/yyyy">';
                                preview += '<input type = "text" class = "form-control mand' + mandField + '" name = "Date" id="' + fieldid + '"  ' + editable + ' value="' + $(object).closest('tr').find('td').eq(p).text() + '" readonly>';//value="' + firstDay + '"
                            }
                            preview += '</div>';
                            preview += '<span class="help-block">' + tableData[i].placeHolder + '</span></div>';
                        }

                        // nikhil


                        else if (type === "Time") {
                            var hrsarray = $(object).closest('tr').find('td').eq(p).text().split(':');
                            var hrs = hrsarray[0];
                            var MinArray = hrsarray[1].split(' ');
                            var Min = MinArray[0];
                            var format = MinArray[1];
                            var tmp1 = "HH" + fieldid;
                            var tmp2 = "MM" + fieldid;
                            var tmp3 = "am" + fieldid;

                            // added by nikhil for Time field :- 28 aug 2017
                            if (editField == 1) {
                                HHArray.push({id: tmp1, value: hrs});
                                MMArray.push({id: tmp2, value: Min});
                                FormatArray.push({id: tmp3, value: format});
                                preview += '<div class="col-md-8">';
                                // preview += '<input type="hidden"  id="fieldid" value="'+fieldid+'">';

                                preview += '<div style="display:none"><input type="text" name="time" class="time_field_class' + fieldid + '" id="' + fieldid + '" value="' + $(object).closest('tr').find('td').eq(p).text() + '"></div>';

                                preview += '<select name="time1" onchange="setHour(' + fieldid + ');" id="' + fieldid + '" class="HH' + fieldid + '" type="time1" valu"' + hrs + '">';

                                preview += '<option value="HH">HH</option>';
                                preview += '<option value="01">01</option>';
                                preview += '<option value="02">02</option>';
                                preview += '<option value="03">03</option>';
                                preview += '<option value="04">04</option>';
                                preview += '<option value="05">05</option>';
                                preview += '<option value="06">06</option>';
                                preview += '<option value="07">07</option>';
                                preview += '<option value="08">08</option>';
                                preview += '<option value="09">09</option>';
                                preview += '<option value="10">10</option>';
                                preview += '<option value="11">11</option>';
                                preview += '<option value="12">12</option>';
                                preview += '</select>';
                                preview += ' <select name="time1" onchange="setMin(' + fieldid + ');" id="' + fieldid + '" class="MM' + fieldid + '" type="time1">';
                                preview += '<option value="MM">MM</option>';
                                preview += '<option value="00">00</option>';
                                preview += '<option value="01">01</option>';
                                preview += '<option value="02">02</option>';
                                preview += '<option value="03">03</option>';
                                preview += '<option value="04">04</option>';
                                preview += '<option value="05">05</option>';
                                preview += '<option value="06">06</option>';
                                preview += '<option value="07">07</option>';
                                preview += '<option value="08">08</option>';
                                preview += '<option value="09">09</option>';
                                preview += '<option value="10">10</option>';
                                preview += '<option value="11">11</option>';
                                preview += '<option value="12">12</option>';
                                preview += '<option value="13">13</option>';
                                preview += '<option value="14">14</option>';
                                preview += '<option value="15">15</option>';
                                preview += '<option value="16">16</option>';
                                preview += '<option value="17">17</option>';
                                preview += '<option value="18">18</option>';
                                preview += '<option value="19">19</option>';
                                preview += '<option value="20">20</option>';
                                preview += '<option value="21">21</option>';
                                preview += '<option value="22">22</option>';
                                preview += '<option value="23">23</option>';
                                preview += '<option value="24">24</option>';
                                preview += '<option value="25">25</option>';
                                preview += '<option value="26">26</option>';
                                preview += '<option value="27">27</option>';
                                preview += '<option value="28">28</option>';
                                preview += '<option value="29">29</option>';
                                preview += '<option value="30">30</option>';
                                preview += '<option value="31">31</option>';
                                preview += '<option value="32">32</option>';
                                preview += '<option value="33">33</option>';
                                preview += '<option value="34">34</option>';
                                preview += '<option value="35">35</option>';
                                preview += '<option value="36">36</option>';
                                preview += '<option value="37">37</option>';
                                preview += '<option value="38">38</option>';
                                preview += '<option value="39">39</option>';
                                preview += '<option value="40">40</option>';
                                preview += '<option value="41">41</option>';
                                preview += '<option value="42">42</option>';
                                preview += '<option value="43">43</option>';
                                preview += '<option value="44">44</option>';
                                preview += '<option value="45">45</option>';
                                preview += '<option value="46">46</option>';
                                preview += '<option value="47">47</option>';
                                preview += '<option value="48">48</option>';
                                preview += '<option value="49">49</option>';
                                preview += '<option value="50">50</option>';
                                preview += '<option value="51">51</option>';
                                preview += '<option value="52">52</option>';
                                preview += '<option value="53">53</option>';
                                preview += '<option value="54">54</option>';
                                preview += '<option value="55">55</option>';
                                preview += '<option value="56">56</option>';
                                preview += '<option value="57">57</option>';
                                preview += '<option value="58">58</option>';
                                preview += '<option value="59">59</option>';
                                preview += '</select>';
                                preview += '<select name="time1" onchange="setAmPm(' + fieldid + ');" id="' + fieldid + '" class="am' + fieldid + '" type="time1">';
                                preview += '<option value="AM">AM</option>';
                                preview += '<option value="PM">PM</option>';
                                preview += '</select>';
                                preview += '</div>';

                            }
                        }
                        // ended by nikhil 
                        else if (type == 'Checkboxes') {
                            preview += ' <div class="col-md-8"><div class="checkbox-list check-mand' + mandField + '"> ';
                            options = tableData[i].option.split('\n');
                            if (editField == 1) {
                                for (var j = 0; j < options.length; j++) {
                                    if (options[j] == $(object).closest('tr').find('td').eq(p).text())
                                    {
                                        preview += '<label class="checkbox-inline"><input type="checkbox" name = "Checkboxes" class="mand' + mandField + '" id="' + fieldid + '" checked>' + options[j] + ' </label>';
                                    } else
                                    {
                                        preview += '<label class="checkbox-inline"><input type="checkbox" name = "Checkboxes" class="mand' + mandField + '" id="' + fieldid + '">' + options[j] + ' </label>';
                                    }
                                }
                            } else {
                                for (var j = 0; j < options.length; j++) {
                                    if (options[j] == $(object).closest('tr').find('td').eq(p).text())
                                    {
                                        preview += '<label class="checkbox-inline"><input type="checkbox" disabled  name = "Checkboxes" class="mand' + mandField + '" id="' + fieldid + '" checked>' + options[j] + ' </label>';
                                    } else
                                    {
                                        preview += '<label class="checkbox-inline"><input type="checkbox" disabled  name = "Checkboxes" class="mand' + mandField + '" id="' + fieldid + '">' + options[j] + ' </label>';
                                    }
                                }
                            }
                            preview += "</div></div>";
                        } else if (type == 'Radio') {
                            preview += ' <div class="col-md-8"><div class="radio-list radio-mand' + mandField + '"> ';
                            options = tableData[i].option.split('\n');
                            if (editField == 1) {
                                for (var j = 0; j < options.length; j++) {
                                    if (options[j] == $(object).closest('tr').find('td').eq(p).text())
                                    {
                                        preview += '<label class="radio-inline"><input type="radio"  id="' + fieldid + '"  name = "Radio" class="mand' + mandField + '" name="radio_' + i + '" checked>' + options[j] + ' </label>';
                                    } else {
                                        preview += '<label class="radio-inline"><input type="radio"  id="' + fieldid + '"  name = "Radio" class="mand' + mandField + '" name="radio_' + i + '" >' + options[j] + ' </label>';
                                    }
//                                    preview += '<label class="radio-inline"><input type="radio"  id="' + fieldid + '"  name = "Radio" class="mand' + mandField + '" name="radio_' + i + '" >' + options[j] + ' </label>';
                                }
                            } else {
                                for (var j = 0; j < options.length; j++) {
                                    if (options[j] == $(object).closest('tr').find('td').eq(p).text())
                                    {
                                        preview += '<label class="radio-inline"><input type="radio"  id="' + fieldid + '" name = "Radio" class="mand' + mandField + '"  disabled name="radio_' + i + '" checked>' + options[j] + ' </label>';
                                    } else
                                    {
                                        preview += '<label class="radio-inline"><input type="radio"  id="' + fieldid + '" name = "Radio" class="mand' + mandField + '"  disabled name="radio_' + i + '" >' + options[j] + ' </label>';
                                    }
                                }
                            }
                            preview += "</div></div>";
                        } else if (type == 'Image') {
                            var ImageName=$(object).closest('tr').find('td').eq(p).text();                           
                            if (editField == 1) {
                                preview += '<div class="col-md-8">';
                                preview += '<div class="fileupload fileupload-new" data-provides="fileupload">';
                                preview += '<div class="fileupload-new thumbnail" >';//style="width: 150px; height: 150px;"
                                preview += '<img src="' + imgArray[imgcnt] + '" onerror="this.src=\'assets/img/noimage.jpg\';"/>';//style="width:330px;height:330px"
                                preview += '</div>';
                                preview += '<div class="fileupload-preview fileupload-exists thumbnail" style="max-width: 150px; max-height: 150px; line-height: 20px;">';
                                preview += '</div>';
                                preview += '<div>';
                                preview += '<span class="btn btn-default btn-file">';
//                                preview += '<span class="fileupload-new">';
//                                preview += '<i class="fa fa-paperclip"></i> Select image';
//                                preview += '</span>';//class="fileupload-exists"
                                preview += '<span>';
                                preview += '<i class="fa fa-undo"></i> Change';
                                preview += '</span>';
                                preview += '<input type="file" name = "Image" class="default  mand' + mandField + '_'+ImageName+'" id="' + fieldid + '" name="fileUpload"/>';
                               // preview += '<div style="display:none"><input type="text" id="updateImage" value="' + $(object).closest('tr').find('td').eq(p).text() + '"></div>';
                                preview += '</span>';
                               // preview += '<a href="#" class="btn btn-danger" data-dismiss="fileupload"><i class="fa fa-trash-o"></i> Remove</a>';
                                preview += '</div>';
                                preview += '</div>';
                                preview += '</div>';  
                            } else {
                                preview += '<div class="col-md-8">';
                                preview += '<div class="fileupload fileupload-new" data-provides="fileupload[]">';
                                preview += '<div class="fileupload-new thumbnail" style="width: 150px; height: 150px;">';
                                preview += '<img style="width:330px;height:330px" src="' + imgArray[imgcnt] + '" onerror="this.src=\'assets/img/noimage.jpg\';"/>';
                                preview += '</div>';
                                preview += '<div class="fileupload-preview fileupload-exists thumbnail" style="max-width: 150px; max-height: 150px; line-height: 20px;">';
                                preview += '</div>';
                                preview += '<div style="display:none">';
                                preview += '<span class="btn btn-default btn-file">';
//                                preview += '<span class="fileupload-new">';
//                                preview += '<i class="fa fa-paperclip"></i> Select image';
//                                preview += '</span>';
                                preview += '<span class="fileupload-exists">';
                                preview += '<i class="fa fa-undo"></i> Change';
                                preview += '</span>';
                                preview += '<input type="file" name = "Image" class="default  mand' + mandField + '" id="' + fieldid + '" name="fileUpload[]"/>';
                              //  preview += '<div style="display:none"><input type="text" id="mand'+mandField+'" value="' + $(object).closest('tr').find('td').eq(p).text() + '"></div>';
                                preview += '</span>';
                                preview += '<a href="#" class="btn btn-danger fileupload-exists" data-dismiss="fileupload"><i class="fa fa-trash-o"></i> Remove</a>';
                                preview += '</div>';
                                preview += '</div>';
                                preview += '</div>'                                
                            }
                            imgcnt+=1;
                            
                           
                        } else if (type == 'Textarea') {
                            if (editField == 1) {
                                preview += '<div class="col-md-8"> <textarea class="form-control mand' + mandField + '" id="' + fieldid + '" name = "Textarea" rows="3" placeholder="' + tableData[i].placeHolder + '" ' + editable + ' >' + $(object).closest('tr').find('td').eq(p).text() + '</textarea></div> ';
                            } else {
                                preview += '<div class="col-md-8 "> <textarea class="form-control mand' + mandField + '" id="' + fieldid + '" name = "Textarea" rows="3" placeholder="' + tableData[i].placeHolder + '" ' + editable + '  readonly>' + $(object).closest('tr').find('td').eq(p).text() + '</textarea></div> ';
                            }
                        } else if (type === 'Predefined Lists')
                        {
                            //#11446
                            //Submitting form with predefined data.
                            var uOptionList;
                            var count = 0;
                            if (listType == "UserDefinedList")
                            {
                                for (var m = 0; m < userDefinedList.length; m++)
                                {
                                    if (userDefinedList[m].listName == listName && count == 0)
                                    {
                                        count++;
                                        uOptionList = userDefinedList[m].optionList;
                                        if (editField == 1) {
                                            preview += ' <div class="col-md-8"><select id="' + fieldid + '" name="Dropdown" class="form-control mand' + mandField + '" ' + editable + ' >';
                                        } else {
                                            preview += ' <div class="col-md-8"><select name="Dropdown" id="' + fieldid + '" class="form-control mand' + mandField + '" ' + editable + ' disabled>';
                                        }
                                        preview += '<option value="---select---">---select---</option>';
                                        for (var k = 0; k < uOptionList.length; k++) {
                                            if (uOptionList[k] == $(object).closest('tr').find('td').eq(p).text())
                                            {
                                                preview += "<option value='" + uOptionList[k] + "' selected>" + uOptionList[k] + "</option>";
                                            } else
                                            {
                                                preview += "<option value='" + uOptionList[k] + "'>" + uOptionList[k] + "</option>";
                                            }

//                                            preview += "<option value='" + uOptionList[k] + "'>" + uOptionList[k] + "</option>";
                                        }
                                        preview += "</select></div>";
                                    }

                                }
                            } else//(listType=="SystemDefinedList")
                            {
                                count = 0;
                                for (var m = 0; m < systemDefinedList.length; m++)
                                {
                                    if (systemDefinedList[m].listName == listName && count == 0)
                                    {
                                        count++;
                                        uOptionList = systemDefinedList[m].optionList;
                                        if (editField == 1) {
                                            preview += ' <div class="col-md-8"><select id="' + fieldid + '" name="Dropdown" class="form-control mand' + mandField + '" ' + editable + '>';
                                        } else {
                                            preview += ' <div class="col-md-8"><select name="Dropdown" id="' + fieldid + '" class="form-control mand' + mandField + '" ' + editable + '  disabled>';
                                        }
                                        preview += '<option value="---select---">---select---</option>';
                                        for (var k = 0; k < uOptionList.length; k++) {
                                            if (uOptionList[k] == $(object).closest('tr').find('td').eq(p).text())
                                            {
                                                preview += "<option value='" + uOptionList[k] + "' selected>" + uOptionList[k] + "</option>";
                                            } else
                                            {
                                                preview += "<option value='" + uOptionList[k] + "'>" + uOptionList[k] + "</option>";
                                            }
//                                            preview += "<option value='" + uOptionList[k] + "'>" + uOptionList[k] + "</option>";
                                        }
                                        preview += "</select></div>";
                                    }
                                }
                            }
                        }
                        p += 1;                        
                        preview += '</div> ';
                    }
                    preview += '</div></form><div class="modal-footer" style="padding: 20px 0px !important;" id="add_form_footer"><button type="button" class="btn btn-info" style="margin-right:3px;margin-left:10px;" onclick="saveformdetails(' + id + ',' + fieldid + ',' + index + ');">Update</button><button type="button" data-dismiss="modal" class="btn btn-default" tabindex="17">Cancel</button>&nbsp;&nbsp;<div>';
                    $("#formedt1").html(preview);
                    $(".caption").html(caption);
                    for (var i = 0; i < HHArray.length; i++) {
                        $("." + HHArray[i].id).val(HHArray[i].value);
                    }
                    for (var i = 0; i < MMArray.length; i++) {
                        $("." + MMArray[i].id).val(MMArray[i].value);
                    }
                    for (var i = 0; i < FormatArray.length; i++) {
                        $("." + FormatArray[i].id).val(FormatArray[i].value);
                    }
                    $(".caption").attr('title', caption);
                    $("#caption").css({'max-0width': '300px', 'overflow': 'hidden', 'text-overflow': 'ellipsis'});
                    App.initUniform();
                    App.fixContentHeight();
                    $('.date-picker').datepicker({
                        rtl: App.isRTL(),
                        autoclose: true
                    });
                    imgArray=[];
                },
                error: function (xhr, ajaxOptions, thrownError)
                {
                    alert("in error:" + thrownError);
                }

            });
        }
    });
}

function showcustform(id, strImgsPath,object) {    
    var imgsStr=strImgsPath;
    imgsStr=imgsStr.substr(0,imgsStr.length-1);
    var imgArray=new Array();
    imgArray=imgsStr.split(',');
    
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/predefinedList/getAllListData/" + id,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        asyn: false,
        success: function (data) {
            masterDataList = data.result;
            userDefinedList = masterDataList.UserDefinedList;
            systemDefinedList = masterDataList.SystemDefinedList;

            $(".leftnav_form").removeClass("active");
            $("#leftnav_form_" + id).addClass("active");

            var submitcaption = "Save";
            var preview = '<div class="portlet"><div class="portlet-body form"><form class="form-horizontal"  role="form"  method="POST" id="submit_form" name="submit_form"><div class="form-body">';
            var type = "";
            var options = [];
            var caption;
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
            var tableData = [];
            var editable = "";
            var fieldid;
            var editField;
            var mandField;
            var listId;
            var listName;
            var listType;

            $.ajax({
                type: "GET",
                url: fieldSenseURL + "/adminCustomForms/" + id, //need to change this url
                contentType: "application/json; charset=utf-8",
                headers: {
                    "userToken": userToken
                },
                crossDomain: false,
                cache: false,
                dataType: 'json',
                asyn: false,
                success: function (data) {
                    tableData = data.result.tableData;
                    caption = data.result.formName;
                    var teamMember = document.getElementById("all_users_forms").value;
                    if (teamMember != 'All') {
                        var p = 1;
                    } else
                    {
                        var p = 2;
                    }
                    var imgcnt=0;
                    $("#viewForm").html('');
                    for (var i = 0; i < tableData.length; i++)
                    {                        
                        type = tableData[i].fieldType;
                        editable = tableData[i].editField;
                        fieldid = tableData[i].fieldId;
                        submitcaption = data.result.submit_caption;
                        editField = tableData[i].editField;
                        mandField = tableData[i].mandField;
                        listId = tableData[i].listIdFK;
                        listName = tableData[i].listName;
                        listType = tableData[i].listType;

                        if (mandField == 1) {
                            preview += '<div class="form-group"><label class="col-md-4 control-label" >' + tableData[i].labelName + '<span style="color:red"> * </label>';
                        } else {
                            preview += '<div class="form-group"><label class="col-md-4 control-label">' + tableData[i].labelName + '</label>';
                        }
                        if (type == 'Textbox')
                        {
                            preview += '<div class="col-md-8"> <input type="text" name="Textbox" id="' + fieldid + '" class="form-control mand' + mandField + '" placeholder="' + tableData[i].placeHolder + '" ' + editable + ' value="' + $(object).closest('tr').find('td').eq(p).text() + '" disabled></div> ';
                        } else if (type == 'Number')
                        {
                            preview += '<div class="col-md-8"><input type="text" name="Number" id="' + fieldid + '" class="form-control mand' + mandField + '" placeholder="' + tableData[i].placeHolder + '" ' + editable + ' value="' + $(object).closest('tr').find('td').eq(p).text() + '" disabled></div> ';
                        } else if (type == 'Dropdown')
                        {
                            preview += ' <div class="col-md-8"><input type="text" name="Number" id="' + fieldid + '" class="form-control mand' + mandField + '" placeholder="' + tableData[i].placeHolder + '" ' + editable + ' value="' + $(object).closest('tr').find('td').eq(p).text() + '" disabled></div>';

                        } else if (type == 'Date') {

                            preview += '<div class="col-md-8">';
                            preview += '<div class = "input-group" data - date = "20/11/2012" data-date-format="dd/mm/yyyy">';
                            preview += '<input type = "text" class = "form-control mand' + mandField + '" name = "Date" id="' + fieldid + '" ' + editable + ' value="' + $(object).closest('tr').find('td').eq(p).text() + '" disabled>';//value="' + firstDay + '"                            
                            preview += '</div>';
                            preview += '<span class="help-block">' + tableData[i].placeHolder + '</span></div>';
                        } else if (type === "Time") {
                            if (editField == 1) {
                                preview += '<div class="col-md-8"><input type="text" name="Textbox" id="' + fieldid + '" class="form-control mand' + mandField + '" placeholder="' + tableData[i].placeHolder + '" ' + editable + ' value="' + $(object).closest('tr').find('td').eq(p).text() + '" disabled>';//                                
                                preview += '</div>';
                            }
                        } else if (type == 'Checkboxes') {
                            preview += ' <div class="col-md-8"><div class="checkbox-list check-mand' + mandField + '"> ';//                           
                            preview += '<label class="checkbox-inline"><input type="checkbox" name = "Checkboxes" class="mand' + mandField + '" id="' + fieldid + '" checked disabled>' + $(object).closest('tr').find('td').eq(p).text() + ' </label>';
                            preview += "</div></div>";
                        } else if (type == 'Radio') {
                            preview += ' <div class="col-md-8"><div class="radio-list radio-mand' + mandField + '"> ';
                            preview += '<label class="radio-inline"><input type="radio"  id="' + fieldid + '"  name = "Radio" class="mand' + mandField + '" name="radio_' + i + '"  checked disabled>' + $(object).closest('tr').find('td').eq(p).text() + '</label>';
                            preview += "</div></div>";
                        } else if (type == 'Image') {                            
                                preview += '<div class="col-md-8">';
                                preview += '<div class="fileupload fileupload-new" data-provides="fileupload">';
                                preview += '<div class="fileupload-new thumbnail" >';
                                preview += '<img  src="' + imgArray[imgcnt] + '" onerror="this.src=\'assets/img/noimage.jpg\';"/>';
                                preview += '</div>';
                                preview += '<div class="fileupload-preview fileupload-exists thumbnail" style="max-width: 150px; max-height: 150px; line-height: 20px;">';
                                preview += '</div>';
                                preview += '<div>';
                                preview += '<span class="btn btn-default btn-file">';
                                preview += $(object).closest('tr').find('td').eq(p).text();
                                preview += '</span>';
                                preview += '</div>';
                                preview += '</div>';
                                preview += '</div>';
                                imgcnt+=1;
                        } else if (type == 'Textarea') {
                            preview += '<div class="col-md-8"> <textarea class="form-control mand' + mandField + '" id="' + fieldid + '" name = "Textarea" rows="3" placeholder="' + tableData[i].placeHolder + '" ' + editable + ' disabled>' + $(object).closest('tr').find('td').eq(p).text() + '</textarea></div> ';
                        } else if (type === 'Predefined Lists')
                        {
                            var uOptionList;
                            var count = 0;
                            if (listType == "UserDefinedList")
                            {
                                for (var m = 0; m < userDefinedList.length; m++)
                                {
                                    if (userDefinedList[m].listName == listName && count == 0)
                                    {
                                        count++;
                                        preview += ' <div class="col-md-8"><input type="text" name="Textbox" id="' + fieldid + '" class="form-control mand' + mandField + '" placeholder="' + tableData[i].placeHolder + '" ' + editable + ' value="' + $(object).closest('tr').find('td').eq(p).text() + '" disabled></div>';//                                        
                                    }

                                }
                            } else//(listType=="SystemDefinedList")
                            {
                                count = 0;
                                for (var m = 0; m < systemDefinedList.length; m++)
                                {
                                    if (systemDefinedList[m].listName == listName && count == 0)
                                    {
                                        count++;
                                        preview += ' <div class="col-md-8"><input type="text" name="Textbox" id="' + fieldid + '" class="form-control mand' + mandField + '" placeholder="' + tableData[i].placeHolder + '" ' + editable + ' value="' + $(object).closest('tr').find('td').eq(p).text() + '" disabled></div>';
                                    }
                                }
                            }
                        }
                        p += 1;
                        preview += '</div> ';
                    }
                    $("#viewForm").html(preview);
                    $("#formTitle").html(caption);
                    $(".caption").attr('title', caption);
                    $("#caption").css({'max-0width': '300px', 'overflow': 'hidden', 'text-overflow': 'ellipsis'});
                    App.initUniform();
                    App.fixContentHeight();
                    $('.date-picker').datepicker({
                        rtl: App.isRTL(),
                        autoclose: true
                    });
                    imgArray=[];
                },
                error: function (xhr, ajaxOptions, thrownError)
                {
                    alert("in error:" + thrownError);
                }

            });
        }
    });
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

//var templatehtml='';
function formCustomFormImageLink(formId, fieldId, filledOnId) {


    templatehtml = customFormImageURLPath + 'account_' + accountId + '_form_' + formId + '_field_' + fieldId + '_filledOnId_' + filledOnId + '.png';

    return templatehtml;
}

function saveformdetails(formid, fieldid, FormFilledid) {
    var filleddata = [];
    var arrayId = [];
    var timedata = [];
    var customFormImage;
    var previd;
    var field_value;
    var funexit;
    var radiodefault;
    var fieldType;
    var count = 0;
    var indexListForImages = [];
    $($('#submit_form').prop('elements')).each(function () {
        if (this.type == 'checkbox') {
            if ($(this).parent().attr('class') == 'checked') {
                count = count + 1;
                if (previd == this.id) {
                    field_value = field_value + "\n" + $(this).parent().parent().parent().text();
                    filleddata = filleddata.slice(0, filleddata.length - 1);
                    filleddata.push({"field_id_fk": this.id, "field_value": field_value.trim(), "fieldType": this.name});
                } else {
                    field_value = $(this).parent().parent().parent().text();
                    filleddata.push({"field_id_fk": this.id, "field_value": field_value.trim(), "fieldType": this.name});
                    previd = this.id;
                }
            }
        } else if (this.type == 'radio') {
            if ($(this).parent().attr('class') == 'checked') {
                count = count + 1;
                field_value = $(this).parent().parent().parent().text();
                filleddata.push({"field_id_fk": this.id, "field_value": field_value.trim(), "fieldType": this.name});
            }
        } else if (this.type == 'select-one') {
            console.log("Inside not hidden select");
            if ($(this).hasClass('mand1') && (this.value.trim() == "---select---")) {
                var lablename = $(this).parent().parent().find('label').text();
                fieldSenseTosterError(lablename.substring(0, lablename.length - 1) + " should be selected");
                funexit = false;
                return false;
            } else {
                field_value = this.value;
                console.log("this.value " + this.type);
                if (field_value == "---select---") {
                    filleddata.push({"field_id_fk": this.id, "field_value": "", "fieldType": this.name});
                } else if (!$(this).hasClass('HH' + this.id) && !$(this).hasClass('MM' + this.id) && !$(this).hasClass('am' + this.id)) {
                    filleddata.push({"field_id_fk": this.id, "field_value": field_value.trim(), "fieldType": this.name});
                    count = count + 1;
                }
            }

        } else if (this.type == 'file') { 
            
            console.log("Inside not file hidden");
           // if ($(this).hasClass('mand1') && (this.value.trim() == "")) {
           if ($(this).hasClass('mand1') && ($(".mand1").val() == "")) {
                var lablename = $(this).parent().parent().parent().parent().parent().find('label').text();
                fieldSenseTosterError(lablename.substring(0, lablename.length - 1) + " should be uploaded");
                funexit = false;
                return false;
            } else {
                if (this.value.trim() =="") {
                    filleddata.push({"field_id_fk": this.id, "field_value": "", "fieldType": "Image"});
                } else {
                    customFormImage = {"fieldId": this.id, "imageURL": ""};
                    indexListForImages.push(filleddata.length);
                    filleddata.push({"field_id_fk": this.id, "field_value": "", "fieldType": "Image", "customFormImage": customFormImage});
                }
                count = count + 1;
            }

        } else
        if (this.type != "hidden") {
            console.log("Inside not hidden");
            if ($(this).hasClass('mand1') && (this.value.trim() == "")) {
                var lablename = $(this).parent().parent().find('label').text();
                fieldSenseTosterError(lablename.substring(0, lablename.length - 1) + " should not be empty");
                funexit = false;
                return false;
            } else {
                field_value = this.value;

                filleddata.push({"field_id_fk": this.id, "field_value": field_value.trim(), "fieldType": this.name});
                if (field_value.trim() != "")
                    count = count + 1;
            }
        } else
        // added by nikhil for time field :- 28 aug 2017
        //             
        //if (this.type == "hidden" && $(this).hasClass('time_field_class' + this.id))--commented by vinay
        if (this.type == "text" && $(this).hasClass('time_field_class' + this.id))
        {
            console.log("this.id " + this.id);
            if ($(this).hasClass('time_field_class' + this.id) && (this.value.trim() == "")) {
                var lablename = $(this).parent().parent().find('label').text();
                console.log("lablename " + lablename);
                fieldSenseTosterError(lablename.substring(0, lablename.length - 1) + " should be selected");
                funexit = false;
                return false;
            } else {
                field_value = this.value;
                console.log("this.value in hidden " + this.type);
                if (this.name !== 'expM' || this.name !== 'expY' || this.name !== 'expAmPm')
                {
                    filleddata.push({"field_id_fk": this.id, "field_value": field_value.trim(), "fieldType": this.name});
                    if (field_value.trim() != "")
                        count = count + 1;
                }
                arrayId.push(this.id);
            }
        }
        // ended by nikhil           
    });

    //for checkbox validaton if it is mandatory
    if (funexit != false) {
        var checkmand;
        $(".check-mand1").each(function () {
            $(".check-mand1").find('.checkbox-inline').each(function () {
                if ($(this).find('span').hasClass('checked')) {
                    checkmand = true;
                    return false;
                } else {
                    checkmand = false;
                }
            });
            if (checkmand == false) {
                var lablename = $(this).parent().parent().find('.control-label').text();
                fieldSenseTosterError(lablename.substring(0, lablename.length - 1) + " should be selected");
                funexit = false;
                return false;
            }
        });
    }
    //for checkbox validaton  if it is mandatory

    //for radio validaton if it is mandatory
    if (funexit != false) {
        var radiomand;
        $(".radio-mand1").each(function () {
            $(".radio-mand1").find('.radio-inline').each(function () {
                if ($(this).find('span').hasClass('checked')) {
                    radiomand = true;
                    return false;
                } else {
                    radiomand = false;
                }
            });
            if (radiomand == false) {
                var lablename = $(this).parent().parent().find('.control-label').text();
                fieldSenseTosterError(lablename.substring(0, lablename.length - 1) + " should be selected");
                funexit = false;
                return false;
            }
        });
    }
    //for radio validaton  if it is mandatory

    if (funexit != false) {
        //checking empty form
        if (count == 0) {
            funexit = false;
            fieldSenseTosterError("Please fill up atleast one field");
            return false;
        }
        //checking empty form          
                
        //var idServer =  $("input[type=file]")[0].id;
        var formData = new FormData();
        var isFileFormat = true;       
        formData.append('action', 'uploadImages');
        $.each($("input[type=file]"), function (i, obj) {
            
            $.each(obj.files, function (j, file) {                
                //if(file.type != 'image/png' && file.type != 'image/jpg' && file.type != 'image/gif' && file.type != 'image/jpeg' ) {
                if (file.type.indexOf("image") != 0) {
                    isFileFormat = false;
                    return false;
                }
                formData.append('fileupload[' + i + ']', file);
            })
            if (isFileFormat == false) {
                return false;
            }
        });
        if (isFileFormat == false) {
            fieldSenseTosterError("Please upload proper image file.");
            return false;
        }
        $('#pleaseWaitDialog').modal('show');

        //On Demand Custom Forms
        clevertap.event.push("On Demand Custom Forms", {
            "Source": "Web",
            "Account name": accountNamecookie,
            "UserRolecookie": UserRolecookie,
        });
        $.ajax({
            url: fieldSenseURL + "/CustomFormsReport/image/update",
            type: "POST",
            headers: {
                "userToken": userToken
            },
            data: formData,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            success: function (data) {                
                if (data.errorCode == '0000') {
                    var imageUrlList = data.result;
                    for (i = 0; i < imageUrlList.length; i++) {
                        filleddata[indexListForImages[i]].customFormImage.imageURL = imageUrlList[i];
                    }
                    var formdetails = {
                        formid: formid,
                        formfilledid: FormFilledid,
                        userid: loginUserId,
                        filledData: filleddata
                    }
                    var jsonData = JSON.stringify(formdetails);
                    console.log("formdetails " + formdetails);
                    
                    $.ajax({
                        type: "PUT",
                        url: fieldSenseURL + "/CustomFormsReport/update", //need to change this url
                        contentType: "application/json; charset=utf-8",
                        headers: {
                            "userToken": userToken
                        },
                        crossDomain: false,
                        cache: false,
                        dataType: 'json',
                        data: jsonData,
                        asyn: true,
                        success: function (data) {                            
                            if (data.errorCode == '0000') {
                                for (var i = 0; i < arrayId.length; i++)
                                {
                                    $('.time_field_class' + arrayId[i]).val("");
                                }
                                fieldSenseTosterSuccess(data.errorMessage, true);
                                document.submit_form.reset();
                                App.updateUniform();                                                  
                                $(".cls_Editform").modal('hide');                               
                              //  $('#pleaseWaitDialog').modal('hide');
                                loadCustomFormReportPage();

                            } else {
                                fieldSenseTosterError(data.errorMessage, true);
                                $('#pleaseWaitDialog').modal('hide');
                            }
                            //$('#pleaseWaitDialog').modal('hide');
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            $('#pleaseWaitDialog').modal('hide');
                            alert("in error 1:" + thrownError);
                        }
                    });
                } else {
                    $('#pleaseWaitDialog').modal('hide');
                    fieldSenseTosterError(data.errorMessage, true);
                }

            },
            error: function (xhr, ajaxOptions, thrownError) {
                
                $('#pleaseWaitDialog').modal('hide');
                alert("in error 2:" + thrownError);
            }
        });
    }
}


var hour = null;
var min = null;
var AmPm;

function setHour(fieldid) {

    hour = $('.HH' + fieldid).val();

    if (min !== null)
    {
        setAmPm(fieldid);
    }
    if (hour == 'HH')
    {
        $('.time_field_class' + fieldid).val("");
    }

}
function setMin(fieldid) {

    min = $('.MM' + fieldid).val();
    if (hour !== null)
    {
        setAmPm(fieldid);
    }
    if (min == 'MM')
    {
        $('.time_field_class' + fieldid).val("");
    }

}
function setAmPm(fieldid) {
    hour = $('.HH' + fieldid).val();
    min = $('.MM' + fieldid).val();
    AmPm = $('.am' + fieldid).val();
    if (hour == 'HH')
    {
        return false;
    }
    if (min == 'MM')
    {
        return false;
    }
    hour = $('.HH' + fieldid).val();
    min = $('.MM' + fieldid).val();
    AmPm = $('.am' + fieldid).val();
    $('.time_field_class' + fieldid).val(hour + ":" + min + " " + AmPm);

}
// Ended by nikhil


