/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var intervalIndex = window.setInterval(function () {
	var userToken2 = fieldSenseGetCookie("userToken");
    	try {
        	if (userToken2.toString() == "undefined") {
            		window.location.href = "login.html";
        	}
        	else if (loginUserId != 0) {
                        if(role==5)
                        {
                            window.location.href = "dashboard.html";
                        }
            		loggedinUserImageData();
            		loggedinUserData();
            		getAllUsers();
			getAllDashExpenses("reload");
            		window.clearTimeout(intervalIndex);
        	}
    	}
    	catch (err) {
        	window.location.href = "login.html";
    	}
}, 1000);

var mainarray1=[];
function getAllUsers(){

	$.ajax({
        type: "GET",
        url: fieldSenseURL + "/user",
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
			//teamMemberTemplate +='<option value=""></option><option value="All" selected>All Users</option>';
                	for (var i = 0; i < teamMemberData.length; i++) {
                    		var userId = teamMemberData[i].id;
                    		var firstName = teamMemberData[i].firstName;
                    		var lastName = teamMemberData[i].lastName;
                    		var fullName = firstName + ' ' + lastName;
                    		teamMemberTemplate = '<option value="' + userId + '">' + fullName + '</option>';
				$('#all_users').append(teamMemberTemplate);
                	}
               
            	}
		
        }
   	});

}

function getAllDashExpenses(action){

	var teamMember = document.getElementById("all_users").value.trim();
	if(action=="reload" || action=="clear"){
		$(".daterangepicker > .ranges > ul > li:eq(4)").click();
		$("#s2id_all_users > a").removeClass("select2-default");
		$("#s2id_all_users > a > span:eq(0)").html("All Users");
		teamMember="All";
	}
	if(teamMember==""){
		if($("#s2id_all_users > a > span").text().trim()=="All Users"){
			teamMember="All";
		}else{
			fieldSenseTosterError("Please select any user", true);
			return false;
		}
	}
	var totaltime=$("#reportrange >span").text().trim();
	totaltime=totaltime.replace(/Jan/g,"01");
	totaltime=totaltime.replace(/Feb/g,"02");
	totaltime=totaltime.replace(/Mar/g,"03");
	totaltime=totaltime.replace(/Apr/g,"04");
	totaltime=totaltime.replace(/May/g,"05");
	totaltime=totaltime.replace(/Jun/g,"06");
	totaltime=totaltime.replace(/Jul/g,"07");
	totaltime=totaltime.replace(/Aug/g,"08");
	totaltime=totaltime.replace(/Sep/g,"09");
	totaltime=totaltime.replace(/Oct/g,"10");
	totaltime=totaltime.replace(/Nov/g,"11");
	totaltime=totaltime.replace(/Dec/g,"12");

	totaltime=totaltime.split("-");
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
	var start=0;
	var length=5;
	var myTable=$("#sample_21").DataTable();
	if(myTable != null)myTable.destroy();	
	var i=-1;
	var chart=0;
	var statusperc;	
	var catperc;
	var indperc=[];
	var catperc1=[];	

	var dashboardtable = $('#sample_21').dataTable({

                "bServerSide": true,
                "bDestroy": true,
                "ajax": {
   		 	"url": fieldSenseURL + "/reports/expense/Account/" + teamMember + "/"+fromDate+"/"+toDate,
    			"type": "GET",
			headers: {
            			"userToken": userToken
        		},
			contentType: "application/json; charset=utf-8",
			crossDomain: false,
      			cache: false,
        		dataType: 'json',
        		asyn: true,
			//success: function (data) {
			//	var datata=data;
			//	console.log("haiia"+data);
			//},
			error: function (data) {
				$('#pleaseWaitDialog').modal('hide');
				fieldSenseTosterError(data.responseJSON.errorMessage, true);
				//logout();
    			}
  		},
		"iDisplayStart":start,
                // set the initial value
                "iDisplayLength": length, // It will be by default page width set by user
                "sPaginationType": "bootstrap_full_number",
		"bProcessing": false,
                "aLengthMenu": [
		 	[ -1, 5, 10, 15, 20,50,100],
                    	[ "All", 5, 10, 15, 20,50,100] // change per page values here
                 ],
                "oLanguage": {
                 // "sProcessing":"Loading",
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
                            "aTargets": [4,6] 			                             
                	}
		],
		"fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
					if(chart==0){
						statusperc=aData.status_all_percnt[0];	
						catperc=aData.cat_all_percnt[0];
						for (var m in statusperc){	
    							//console.log("asdjhsdasd"+m+"==="+statusperc[m]);
							indperc.push({"action":m,"perce":statusperc[m]});
    						}
						for (var m in catperc){	
    							//console.log("asdjhsdasd"+m+"==="+catperc[m]);
							catperc1.push({"action":m,"perce":catperc[m]});
    						}
						Charts.initPieCharts(indperc,catperc1,"status");							
					}
					chart++;					
					var expenseTime=convertToLocalTime(aData.expenseTime);
					mainarray1.push({"expenseName":aData.expenseName,
							"customerName":aData.customerName,
							"activity":aData.appointment.title,
							"expenseTime":expenseTime,
							"amountSpent":aData.amountSpent,
							"status":aData.status,
                                                        "expenseImageCsv":aData.expenseImageCsv,
							"disburse_amount":aData.disburse_amount,
							"userName":aData.user.fullname,
							"userid":aData.user.id,
							"id":aData.id});	
		},
		"aoColumns": [ // ahmed : here we populate the data in datatable that is returned using Ajax
			      { "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						return full.user.fullname;
					}
				},
				{ "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						var expenseTime=convertToLocalTime(full.expenseTime);
						return expenseTime;
					}
				},
				{ "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						return full.amountSpent;
					}
				},
				{ "mData": null,
				"sClass":"hidden-xs",
				"mRender" : function(data,type,full){
						return full.exp_category_name;
					}
				},
				{ "mData": null,
				"sClass":"hidden-xs status",
				"mRender" : function(data,type,full){
						if(full.status==0){
							return '<span class="label label-sm label-default" title="Pending Approval by Reporting Head">Pending</span>';
						}else if(full.status==1){
							return '<span class="label label-sm label-default" title="Pending Approval by Accounts">Pending</span>';
						}else if(full.status==2){
							return '<span class="label label-sm label-warning" title="Rejected by Reporting Head">Rejected</span>';
						}else if(full.status==3){
							return '<span class="label label-sm label-info" title="Approved">Approved</span>';
						}else if(full.status==4){
							return '<span class="label label-sm label-warning" title="Rejected by Accounts">Rejected</span>';
						}else if(full.status==5){
							return '<span class="label label-sm label-success" title="Disbursed">Disbursed</span>';
						}else{
							return '';
						}
					}
				},
				{ "mData": null,
				"sClass":"hidden-xs",
				"mRender" : function(data,type,full){
						return full.report_head.fullname;
					}
				},
				{ "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						i=i+1;
						return '<button type="button" onclick="displayDetails('+i+')" id="dash_exp'+i+'" class="btn btn-default btn-xs tooltips" data-toggle="modal" href="#expenses" data-placement="bottom" title="Details"><i class="fa fa-edit"></i></button>';
						
					}
				}
		],
		"aaSorting": [],
		//"bFilter": true,
		"preDrawCallback": function( settings ) {
			i=-1;
			mainarray1=[];
    			$('#pleaseWaitDialog').modal('show');
  		},
		"fnDrawCallback": function( oSettings ) { 
					if(oSettings.fnDisplayEnd()>0){
						$("#id_totalCustRecords").html("<b>"+(oSettings._iDisplayStart+1)+"</b> - <b>"+oSettings.fnDisplayEnd()+"</b> of <b>"+oSettings.fnRecordsTotal()+"</b> records");
					}else{
						Charts.initPieCharts(indperc,catperc1,"status");	
					}
					dashboardtable.$('td').mouseover( function () {
						if($(this).hasClass("status")==false){
							var sData=$(this).text();
							$(this).attr('title',sData);
						}
 			 		} );
					App.initUniform();
					App.fixContentHeight();
					jQuery('.tooltips').tooltip();
					$("#sample_21").find('td').css({'max-width':'300px','overflow':'hidden','text-overflow':'ellipsis'});
					$("#sample_21_filter >label >input").attr("class","form-control input-medium");
					
					$('#pleaseWaitDialog').modal('hide');
					
		},

		});

    		jQuery('#sample_21_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
                jQuery('#sample_21_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
                jQuery('#sample_21_wrapper .dataTables_length select').select2(); // initialize select2 dropdown

		$("#sample_21_filter >label >input").unbind();

   		$("#sample_21_filter >label >input").bind('keyup', function(e) {
       			if(e.keyCode == 13) {
       				var table = $('#sample_21').DataTable();
		    		table.search($(this).val()).draw(  );
			}
   		});
		//if(teamMember!="All"){
		//	var table = $('#sample_21').DataTable();
		//	table.column( 0 ).visible( false );
		//}
}

function displayDetails(index){

	var details=mainarray1[index];
        var expenseImageCsv = details.expenseImageCsv ;
	var templatehtml="";

	templatehtml+='<div class="form-group">';
	templatehtml+='<label class="col-md-4 control-label">Expense Head</label>';
	templatehtml+='<div class="col-md-8">';
	templatehtml+='<input type="name" class="form-control" Value="'+details.expenseName+'" tabindex="1" readonly>';
	templatehtml+='</div>';
	templatehtml+='</div>';

	templatehtml+='<div class="form-group">';
	templatehtml+='<label class="col-md-4 control-label">Customer Name</label>';
	templatehtml+='<div class="col-md-8">';
	templatehtml+='<input type="name" class="form-control" Value="'+details.customerName+'" tabindex="3" readonly>';
	templatehtml+='</div>';
	templatehtml+='</div>';

	templatehtml+='<div class="form-group">';
	templatehtml+='<label class="col-md-4 control-label">Visit</label>';
	templatehtml+='<div class="col-md-8">';
	templatehtml+='<input type="text" class="form-control" value="'+details.activity+'" tabindex="2" readonly>';
	templatehtml+='</div>';
	templatehtml+='</div>';

	templatehtml+='<div class="form-group">';
	templatehtml+='<label class="col-md-4 control-label">Date/Time</label>';
	templatehtml+='<div class="col-md-8">';
	templatehtml+='<input type="text" class="form-control" value="'+details.expenseTime+'" tabindex="3" readonly>';
	templatehtml+='</div>';
	templatehtml+='</div>';

	templatehtml+='<div class="form-group">';
	templatehtml+='<label class="col-md-4 control-label">Amount <span>&#8377;</span></label>';
	templatehtml+='<div class="col-md-8">';
	templatehtml+='<input type="text" class="form-control" value="'+details.amountSpent+'" tabindex="5" readonly>';
	templatehtml+='</div>';
	templatehtml+='</div>';
        if(expenseImageCsv==null){
            templatehtml+='<div class="form-group">';
            templatehtml+='<label class="col-md-4 control-label">Attached Receipt</label>';
            templatehtml+='<div class="col-md-8">';
            templatehtml+='<img src="' + expenseImageURLPath + 'expense_' + accountId + '_' + details.userid + '_' + details.id + '.png?' + time + '" onerror="this.src=\'assets/img/noimage.jpg\';"/>';
            templatehtml+='</div>';
            templatehtml+='</div>';
        }else{
            var imageArray = expenseImageCsv.split(",");
            for(var i = 0 ; i <imageArray.length ; i++){
                templatehtml+='<div class="form-group">';
                templatehtml+='<label class="col-md-4 control-label">Attached Receipt</label>';
                templatehtml+='<div class="col-md-8">';
                templatehtml+='<img src="' + expenseImageURLPath + 'expense_' + accountId + '_' + details.userid + '_' + details.id +'_'+ imageArray[i]+'.png?' + time + '" onerror="this.src=\'assets/img/noimage.jpg\';"/>';
                templatehtml+='</div>';
                templatehtml+='</div>';  
            }
        }

	templatehtml+='<div class="form-group">';
	templatehtml+='<label class="col-md-4 control-label">Status</label>';
	templatehtml+='<div class="col-md-8">';
	templatehtml+='<select class="form-control" data-placeholder="Select..." disabled tabindex="6"">';
	if(details.status==0){
		templatehtml+='<option value="0" selected>Pending Approval by Reporting Head</option>';
	}else if(details.status==1){
		templatehtml+='<option value="1" selected>Pending Approval by Accounts</option>';
	}else if(details.status==2){
		templatehtml+='<option value="2" selected>Rejected by Reporting Head</option>';
	}else if(details.status==3){
		templatehtml+='<option value="3" selected>Approved</option>';
	}else if(details.status==4){
		templatehtml+='<option value="4" selected>Rejected by Accounts</option>';
	}else if(details.status==5){
		templatehtml+='<option value="5" selected>Disbursed</option>';
	}
	templatehtml+='</select>';
	templatehtml+='</div>';
	templatehtml+='</div>';

	if(details.status==5){
		templatehtml+='<div class="form-group">';
		templatehtml+='<label class="col-md-4 control-label">Disbursed Amount <span>&#8377;</span></label>';
		templatehtml+='<div class="col-md-8">';
		templatehtml+='<input type="text" class="form-control" value="'+details.disburse_amount+'" tabindex="5" readonly>';
		templatehtml+='</div>';
		templatehtml+='</div>';
	}

	$(".modal-title").html("Expenses - "+details.userName);
	$("#dashboard_details").html(templatehtml);
}

function convertToLocalTime(submittedon){
	var submittedon =submittedon;
	var timeZoneOffSet = submittedon.timezoneOffset * 60 * 1000;
        submittedon = new Date(submittedon.time - timeZoneOffSet);
        submittedon = convertServerDateToLocalTimezone(submittedon.getFullYear(), submittedon.getMonth(), submittedon.getDate(), submittedon.getHours(), submittedon.getMinutes(),timeZoneOffSet);
        submittedon = submittedon.getFullYear() + "-" + (submittedon.getMonth() + 1) + "-" + submittedon.getDate() + " " + submittedon.getHours() + ":" + submittedon.getMinutes() + ":00.0";
        submittedon = fieldSenseseForJSDate(submittedon);               
	return submittedon;
}

