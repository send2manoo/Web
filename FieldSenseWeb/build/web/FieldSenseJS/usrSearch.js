/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//var searchText;
var intervalAdminUser = window.setInterval(function () {
   // alert("hello");
    var userToken2 = fieldSenseGetCookie("userToken");
    
  //alert(userToken2+" userToken2"+searchText);
    try {
        if (userToken2.toString() == "undefined") {
          //   alert("role"+role);
            //alert("intervalCustomer stats + undefined condition" + intervalAdminUser);
            window.location.href = "login.html";
        } else if (loginUserId != 0) {
            if (role == 0) {
              //  alert("role"+role);
              loggedinUserImageData();
                showUserTable();
                window.clearTimeout(intervalAdminUser);

            } else {
                window.location.href = "login.html";
            }

        }
    }
    catch (err) {
        alert("exception hello " + err);
        window.location.href = "login.html";
    }
}, 1000);

var accountRegions;


function showUserTable(){
    $('#userListTable').html('');
//    myTable=$("#sample_3").DataTable();
//	if(myTable != null)myTable.destroy();
    var start=0;
    var length=20;
    var userAddTemplate ='';
    var accname=$("#search_text").val();
    var searchText =$.cookie("searchText");
    if(searchText!==undefined){
        accname=searchText;
        $("#search_text").val(searchText);
    }
    
    userAddTemplate='<table class="table table-striped table-bordered table-hover" id="sample_3">';
    userAddTemplate += '<thead>';
    userAddTemplate += '<tr>';
//    userAddTemplate +='<th class="table-checkbox">';
//    userAddTemplate +='<input type="checkbox" class="group-checkable" data-set="#sample_3 .checkboxes"/>';
//    userAddTemplate +='</th>';								
    userAddTemplate += '<th>';
    userAddTemplate += 'User Name';
    userAddTemplate += '</th>';
    userAddTemplate += '<th>';
    userAddTemplate += 'Account Name';
    userAddTemplate += '</th>';
    userAddTemplate += '<th>';
    userAddTemplate += 'Mobile Number';
    userAddTemplate += '</th>';
    userAddTemplate += '<th>';
    userAddTemplate += 'Email ID';
    userAddTemplate += '</th>';
    userAddTemplate += '<th>';
    userAddTemplate += 'Created Date';
    userAddTemplate += '</th>';
    userAddTemplate += '<th>';
    userAddTemplate += 'Status';
    userAddTemplate += '</th>';
    userAddTemplate += '<th>';
    userAddTemplate += 'Region';
    userAddTemplate += '</th>';
    userAddTemplate += '<th>';
    userAddTemplate += '';
    userAddTemplate += '</th>';
//    userAddTemplate += '<th>';
//    userAddTemplate += '</th>';
    userAddTemplate += '</tr>';
    userAddTemplate += '</thead>';
    userAddTemplate += '<tbody>';
    userAddTemplate += '</tbody>';
    userAddTemplate += '</table>';
    $('#userListTable').html(userAddTemplate);
    //$('#sample_3').dataTable();
        var inboxtable=$('#sample_3').dataTable({
        	"bServerSide": true,
                "bDestroy": true,
                "ajax": {
   		 	"url": fieldSenseURL +"/stats/user/searchData/?searchText="+accname,
    			"type": "GET",
			headers: {
            			"userToken": userToken
        		},
			contentType: "application/json; charset=utf-8",
			crossDomain: false,
      			cache: false,
        		dataType: 'json',
        		asyn: true
  		},
		"iDisplayStart":start,
                // set the initial value
                "iDisplayLength": length, // It will be by default page width set by user
                "sPaginationType": "bootstrap_full_number",
		"bProcessing": false,
                "aLengthMenu": [
		 	[ 5, 10, 15, 20,50,100],
                    	[ 5, 10, 15, 20,50,100] // change per page values here
                 ],
                "oLanguage": {
                  //"sProcessing":"Loading",
		 "sEmptyTable":"No data available in table",
                 "sLengthMenu": "_MENU_ <span id ='id_totalCustRecords'></span>",
		//"sInfo": "Showing _START_ to _END_ of _TOTAL_ Mails",
              	"oPaginate": {
   		        "sPrevious": "Prev",
                        "sNext": "Next"
               		}
                },
		"aoColumnDefs": [
                        {   "bSortable": false,
                            "aTargets": [7] 
			                             
                	}
		],
		"aoColumns": [ // ahmed : here we populate the data in datatable that is returned using Ajax
                                { "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						return full.userName;
					}
				},
				{ "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						return full.accountName;
					}
				},
                                { "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						return full.mobileNo;
					}
				},
                                { "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						return full.emailId;
                                                
					}
				},
				{ "mData": null,
				"sClass":"hidden-xs",
				"mRender" : function(data,type,full){
			            		var createdOn= full.createdDate;
                                    		createdOn=convertServerDateToLocalDate((createdOn.year) + 1900, createdOn.month, createdOn.date, createdOn.hours, createdOn.minutes);
                                    		var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
                                    		createdOn=createdOn.getDate()+' '+monthNames[createdOn.getMonth()]+' '+createdOn.getFullYear();
				   		return createdOn;	
                                }
				},
                                { "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						if(full.active==1){
                                                	return '<span class="label label-sm label-success">Active</span>';
                                                }else{
                                                    	return '<span class="label label-sm label-danger">InActive</span>'; 
                                                }
					}
				},
				{ "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
                                     		return full.region;

					}
				},
                                {"mData": null,
                                "sClass": "inbox-small-cells",
                                "mRender": function (data, type, full) {
                                        return '<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Download" data-toggle="modal" href="#adm_cre11" onclick="downloadAccTemplate1(\'' + full.emailId + '\')"><i class="fa fa-download"></i></button>';                                  
                                }
                            }
		],
		//"aaSorting": [3,"desc"],
		"bFilter": false,
		"preDrawCallback": function( settings ) {
    			$('#pleaseWaitDialog').modal('show');
 		},
		"fnDrawCallback": function( oSettings ) { 
					if(oSettings.fnDisplayEnd()>0){
						$("#id_totalCustRecords").html("<b>"+(oSettings._iDisplayStart+1)+"</b> - <b>"+oSettings.fnDisplayEnd()+"</b> of <b>"+oSettings.fnRecordsTotal()+"</b> records");
					}
					inboxtable.$('td').mouseover( function () {
						var sData=$(this).text();
						$(this).attr('title',sData);
 			 		} );
					App.initUniform();
					App.fixContentHeight();
					jQuery('.tooltips').tooltip();
					$('#pleaseWaitDialog').modal('hide');
		}
                });
               jQuery('#sample_3_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
                jQuery('#sample_3_wrapper .dataTables_length select').addClass(""); // modify table per page dropdown
               jQuery('#sample_3_wrapper .dataTables_length select').select2(); // initialize select2 dropdown	
               $.removeCookie("searchText");
    
}

/**
 * Added by manohar
 * @returns {undefined}
 */ 
function downloadAccTemplate1(emailId) {
    
        var date = new Date();
        var m = date.getMonth() + 1;
        if (m < 10) {
            m = '0' + m;
        }
        var y = date.getFullYear();
        date.setDate(date.getDate() - 1);
        var d = date.getDate();
        if (d < 10) {
            d = '0' + d;
        }
        var yesterDay = d + '/' + m + '/' + y;
        var addDownloadLogTemplate = '';
        addDownloadLogTemplate += '<div class="modal-dialog">';
        addDownloadLogTemplate += '<div class="modal-content">';
        addDownloadLogTemplate += '<div class="modal-header">';
        addDownloadLogTemplate += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
        addDownloadLogTemplate += '<h4 class="modal-title">Download Logs</h4>';
        addDownloadLogTemplate += '</div>';
        addDownloadLogTemplate += '<div class="modal-body">';
        addDownloadLogTemplate += '<div class="form-body">';
        addDownloadLogTemplate += '<form action="#" class="form-horizontal">';
        addDownloadLogTemplate += '<div class="form-group">';
        addDownloadLogTemplate += '<label class="col-md-3 control-label">User Email</label>';
        addDownloadLogTemplate += '<div class="col-md-6">';
        addDownloadLogTemplate += '<input type="text" class="form-control" id="id_uemail" disabled placeholder="Enter Email" value='+emailId+'>';
        addDownloadLogTemplate += '</div>';
        addDownloadLogTemplate += '</div>';
        addDownloadLogTemplate += '<div class="form-group">';
        addDownloadLogTemplate += '<label class="col-md-3 control-label">Date </label>';
        addDownloadLogTemplate += '<div class="col-md-6">';
        addDownloadLogTemplate += '<div class="input-group date date-picker"   data-date-format="dd/mm/yyyy">';
//        addDownloadLogTemplate += '<input type="text" class="form-control  " id="id_startDate"   name="from" value='+yesterDay+'> <span class="input-group-addon " id="datetimepicker1" ><span class="glyphicon glyphicon-calendar date-picker"></span></span>';
//        addDownloadLogTemplate += '<input type="text" class="form-control" id="id_endDate" name="to"  value='+yesterDay+'>';
        
        addDownloadLogTemplate +='<input type="text" class="form-control" id="id_startDate" value='+yesterDay+'><span class="input-group-btn"><button class="btn btn-info" type="button"><i class="fa fa-calendar"></i></button></span>'
        addDownloadLogTemplate += '</div>';
        addDownloadLogTemplate += '</div>';
        addDownloadLogTemplate += '</div>';
        addDownloadLogTemplate += '<div class="form-group">';
        addDownloadLogTemplate += '<label class="col-md-3 control-label">Log Type</label>';
        addDownloadLogTemplate += '<div class="col-md-4">';
        addDownloadLogTemplate += '<div class="checkbox-list">';
        addDownloadLogTemplate += '<label class="checkbox-inline">';
        addDownloadLogTemplate += '<input type="checkbox" id="chkTravellogs" value="option1"> Travel </label>';
        addDownloadLogTemplate += '<label class="checkbox-inline">';
        addDownloadLogTemplate += '<input type="checkbox" id="chkDebuglogs" value="option2"> Debug </label>';
        addDownloadLogTemplate += '</div>';
        addDownloadLogTemplate += '</div>';
        addDownloadLogTemplate += '</div>';
        addDownloadLogTemplate += '</form>';
        addDownloadLogTemplate += '</div>';
        addDownloadLogTemplate += '</div>';
        addDownloadLogTemplate += '<div class="modal-footer">';
        addDownloadLogTemplate += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
        addDownloadLogTemplate += '<button type="button" data-toggle="modal" class="btn btn-primary" onclick="downloadLogs1(\'' + emailId + '\')">Download</button>';
        addDownloadLogTemplate += '</div>';
        addDownloadLogTemplate += '</div>';
        addDownloadLogTemplate += '</div>';
  
    $('#adm_cre11').html(addDownloadLogTemplate);  
    App.initUniform();
    App.fixContentHeight();
    
    $('.date-picker').datepicker({
        autoclose:true
    });

}
//  Ended by manohar 

var uemail='';
//Added by manohar
function downloadLogs1(uemail) {
   
//    var uemail = document.getElementById("id_uemail").value.trim();
    /**
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
    }*/

    var id_startDate = document.getElementById("id_startDate").value.trim();
//    var id_endDate = document.getElementById("id_endDate").value.trim();
    if (id_startDate.length == 0) {
        fieldSenseTosterError("Start date cannot be empty", true);
        return false;
    }  
//    if (id_endDate.length == 0) {
//        fieldSenseTosterError("End date cannot be empty", true);
//        return false;
//    }
    var Travel_logs = 0;
    var chkTravellogs = document.getElementById("chkTravellogs").checked;
    
    var Debug_logs = 0;
    var chkDebuglogs = document.getElementById("chkDebuglogs").checked;
    if(chkTravellogs || chkDebuglogs)
    {
            if (chkTravellogs == true)
            {
                     Travel_logs = 1;
            }
            if (chkTravellogs == false)
            {
                     Travel_logs = 0;               
            }
            if (chkDebuglogs == true)
            {
                     Debug_logs = 1;
            }
            if (chkDebuglogs == false)
            {
                     Debug_logs = 0;
            }     
    }
    else
    {
       if(chkDebuglogs==false)
        {
              fieldSenseTosterError("Please Select Debug logs or Travel logs..", true);
              return false;
        }
        else
        {
             fieldSenseTosterError("Please Select Debug logs or Travel logs..", true);
              return false;
        }
    }
// $('#adm_cre11').modal("hide");

    $('#pleaseWaitDialog').modal('show');
//debugger;
var statsObject={
    "email_id":uemail,
    "startDate":id_startDate,
//    "endDate":id_endDate,
    "debugStatus":Debug_logs,
    "travelStatus":Travel_logs
};
// debugger;
var jsonData=JSON.stringify(statsObject);
// debugger;
    $.ajax({
        type: "PUT",
        url: fieldSenseURL + "/stats/Download/get_Log_Files",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        data:jsonData,
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: false,
        success: function (data) {	
		if (data.errorCode == '0000')
		{
                    $('#pleaseWaitDialog').modal('hide');
		        var travelLogData = data.result;
                        var file;
                        var fileName;
                        var listFiles = [];
                        var fileData=[];
                        var zip = new JSZip();
                        if(travelLogData.length!=0)
                        {
                            for (var i = 0; i < travelLogData.length; i++) 
                            {
                                file = travelLogData[i].file;
                                fileName = travelLogData[i].file_Name; 
                                listFiles[i]=fileName;
                                fileData[i]=file;       
                                
                            }   
//                             debugger;
                            for (var i = 0; i < listFiles.length; i++) 
                                 zip.file(listFiles[i], fileData[i]);                      
                            zip.generateAsync({type:"blob"})
                             .then(function(content) {
                                 // see FileSaver.js
                                 saveAs(content, "Logs.zip");
                             });
//                              debugger;
                             $('#adm_cre11').modal("hide");
                             fieldSenseTosterSuccess("success", true);
                        }else
                        {
//                             debugger;
                            fieldSenseTosterError(data.errorMessage,true);
			    $('#pleaseWaitDialog').modal('hide');
                        }
		        
		}
		else
		{
//                     debugger;
		        fieldSenseTosterError(data.errorMessage,true);
			$('#pleaseWaitDialog').modal('hide');
                        $('#adm_cre11').modal("show");
                        
		}
        }
 }); 
} 
//<!--Ended by manohar--> 
function userSearch(){
    
    
    var searchText=$("#search_text").val();
    showUserTable(searchText);
    
}
function removeEnter(e) {
    if (e.keyCode === 13 ) {
        return false;
    }
}


function checkURL(){
    var pathname = window.location.search;
    pathname=pathname.split("=")[1];
    $("#search_text").val(pathname);
    console.log(pathname);
}
