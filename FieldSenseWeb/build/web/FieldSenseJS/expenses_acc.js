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
            		loggedinUserImageData();
            		loggedinUserData();
            		getAllUsers();
			getAllExpCat();
			getAllExpenses("reload");
            		window.clearTimeout(intervalIndex);
        	}
    	}
    	catch (err) {
        	window.location.href = "login.html";
    	}
}, 1000);

var expensearray=[];
//var element;
var dicount=[];
var cust_start=0;
var cust_length=5;
//var reject=[];
//var approve=[];
//var reject_all=true;
//var approve_all=true;
var set_def_date=0;
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
				$('#all_users_exp').append(teamMemberTemplate);
                	}
               
            	}
		
        }
   	});

}

function getAllExpCat(){
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
                	var expcatdatatempl = '';
			//teamMemberTemplate +='<option value=""></option><option value="All" selected>All Users</option>';
                	for (var i = 0; i < expcatdata.length; i++) {
            
                    		expcatdatatempl = '<option value="' + expcatdata[i].id + '">' + expcatdata[i].categoryName + '</option>';
				$('#all_exp_cat').append(expcatdatatempl);
                	}
               
            	}
		
        }
   	});

}

function getAllExpenses(action){

	var teamMember = document.getElementById("all_users_exp").value.trim();
	var status =document.getElementById("exp_status").value.trim();
	var category = document.getElementById("all_exp_cat").value.trim();

	if(action=="reload" || action=="clear"){
		$(".daterangepicker > .ranges > ul > li:eq(4)").click();
		$("#s2id_all_users_exp > a").removeClass("select2-default");
		$("#s2id_all_users_exp > a> span:eq(0)").html("All Users");
		$("#all_exp_cat").val("All");
		$("#exp_status").val("1");
		teamMember="All";
		status="1";
		category="All";
	}
	if(teamMember==""){
		if($("#s2id_all_users_exp > a > span").text().trim()=="All Users"){
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
	cust_start=0;
	cust_length=5;
	var i=-1;
	var j=0;
	var total_amts=0;
	var all_total_amts=0;
	var myTable=$("#example").DataTable();
	if(myTable != null)myTable.destroy();
	var expensetable = $('#example').dataTable({
                    
                "bServerSide": true,
                "bDestroy": true,
                "ajax": {
   		 	"url": fieldSenseURL + "/expense/Account",
    			"type": "GET",
			headers: {
            			"userToken": userToken
        		},
			contentType: "application/json; charset=utf-8",
			crossDomain: false,
			"data":  {
            			"userid":teamMember,
            			"status":status,
				"expensecategory":category,
				"fromdate":fromDate,
				"todate":toDate,
        		},
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
                            "aTargets": [0,7,8] 			                             
                	}
		],
		"fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
					if(j==0){
						all_total_amts+=aData.total_amounts;
					}
					j++;
					total_amts+=aData.amountSpent;
					var expenseTime=convertToLocalTime(aData.expenseTime);
					expensearray.push({"expenseTime":expenseTime,"expense_data":aData});
		},
		"aoColumns": [ // ahmed : here we populate the data in datatable that is returned using Ajax
			      { "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						i=i+1;
						if(full.status==0 || full.status==4 || full.status==5){
							return '<input type="checkbox" disabled="disabled" id="checkbox_'+i+'" class="checkboxes"/>';
						}else{
							return '<input type="checkbox" onclick="deSelectAll()" class="checkboxes" id="checkbox_'+i+'" value="'+full.id+'"/>';
						}
					}
				},
				{ "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						return full.user.fullname;
					}
				},
				{ "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						return full.expenseName;
					}
				},
				{ "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						return full.exp_category_name;
					}
				},
				{ "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						return full.amountSpent;
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
						return full.customerName;
						
					}
				},
				{ "mData": null,
				"sClass":"inbox-small-cells status",
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
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						return '<button type="button" id="exp_tab'+i+'"  onclick="displayExpDetails('+i+')" class="btn btn-default btn-xs tooltips" data-toggle="modal" href="#expenses" data-placement="bottom" title="Details"><i class="fa fa-edit"></i></button>';
						
					}
				}
		],
		"aaSorting": [],
		//"bFilter": true,
		"preDrawCallback": function( settings ) {
			i=-1;
			expensearray=[];
			total_amts=0;
			all_total_amts=0;
			j=0;
			var sdf=settings;
			if(document.getElementById("exp_status").value.trim()!="All"){			
				if($(".group-checkable").is(':checked') || $('.checkboxes').length==1){
					if(cust_start!=0){
						settings._iDisplayStart=cust_start-cust_length;
					}
				}
			}
			$("#example_info").hide();
			$("#example_length").parent().attr("class","col-md-6 col-sm-12");
			$("#example_paginate").parent().attr("class","col-md-6 col-sm-12");
    			$('#pleaseWaitDialog').modal('show');
  		},
		"fnDrawCallback": function( oSettings ) {  // modified by manohar
                                          $("#id_exportExpense").html('<button type="button" class="btn btn-info fr" style="margin-bottom:10px;"><i class=" fa fa-file-text" id="id_exportExpenseChild"></i> Save as CSV</button>');               
                                         if (oSettings.fnRecordsTotal() != 0) {                                
                                                 $("#id_exportExpense > button").attr("onclick", "getExpenseCsv('"+fromDate+"','"+toDate+"','"+teamMember+"','"+category+"','"+status+"','"+all_total_amts+"')");
                                        }
					if(oSettings.fnDisplayEnd()>0){
						$("#id_totalCustRecords").html("<b>"+(oSettings._iDisplayStart+1)+"</b> - <b>"+oSettings.fnDisplayEnd()+"</b> of <b>"+oSettings.fnRecordsTotal()+"</b> records");
					}
					cust_start=oSettings._iDisplayStart;
					cust_length=oSettings._iDisplayLength;
					expensetable.$('td').mouseover( function () {
						if($(this).hasClass("status")==false){
							var sData=$(this).text();
							$(this).attr('title',sData);
						}
 			 		} );
					
					$("#example").find('td').css({'max-width':'300px','overflow':'hidden','text-overflow':'ellipsis'});
					$("#example_filter >label >input").attr("class","form-control input-medium");

					$("#total_expenses").html('<i class="fa fa-inr fa-fw" aria-hidden="true"></i>' + total_amts + " ( of" + '<i class="fa fa-inr fa-fw" aria-hidden="true"></i>' + all_total_amts + " Total)");
					
					$(".group-checkable").attr('checked', false);
					$(".group-checkable").parent().removeClass('checked');
					$(".checkboxes").each(function(){
						$(this).attr('checked',false);	
						$(this).parent().removeClass('checked');
					});
					$("#btnApprovepo").hide();
					App.initUniform();
					App.fixContentHeight();
					jQuery('.tooltips').tooltip();

					$('#pleaseWaitDialog').modal('hide');
					
		},

		});

    		jQuery('#example_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
                jQuery('#example_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
                jQuery('#example_wrapper .dataTables_length select').select2(); // initialize select2 dropdown

		$("#example_filter >label >input").unbind();
		var table = $('#example').DataTable();
   		$("#example_filter >label >input").bind('keyup', function(e) {
       			if(e.keyCode == 13) {     				
		    		table.search($(this).val()).draw(  );
			}
   		});
		if(status==0 || status==4 || status==5){
			table.column( 0 ).visible( false );
		}

}

/**
 * added by manohar
 * @param {type} fromdate
 * @param {type} todate
 * @param {type} teamMember
 * @param {type} category
 * @param {type} status
 * @param {type} all_total_amts
 * @returns {undefined}
 */
function getExpenseCsv(fromdate,todate,teamMember,category,status,all_total_amts) {

    var exportAccounts = new Array();
    var csvData = new Array();
    csvData.push('"User Name","Expense Head","Expense Category","Amount","Expense Date","Customer","Status"');

    var attendanceReportHtml = '';
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/expense/Account/" +teamMember+"/"+category+"/"+status+"/" +fromdate+ "/"+todate+"/csv",
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
//                var expenseData = data.aaData;
                var expenseData = data.result;
                for (var i = 0; i < expenseData.length; i++) {
                   
                   // added by manohar 
                    var fullname = expenseData[i].user.fullname;
                    var expenseName = expenseData[i].expenseName;
                    var exp_category_name = expenseData[i].exp_category_name;
                    var amountSpent = expenseData[i].amountSpent;
                    var expenseTime=convertToLocalTime(expenseData[i].expenseTime);                    
                    var customerName = expenseData[i].customerName;                    
                    var status = expenseData[i].status;
                    
                     if(status==0){
                            status="Pending Approval by Reporting Head";
                           
                    }else if(status==1){
                            status="Pending Approval by Accounts";
                            
                    }else if(status==2){
                             status="Rejected by Reporting Head";
                            
                    }else if(status==3){
                            status="Approved";
                           
                    }else if(status==4){
                             status="Rejected by Accounts";
                           
                    }else if(status==5){
                             status="Disbursed";
                       
                    }else{
                            status='';
                    }		
                  
                    exportAccounts[i] = [{
                            "fullname":fullname,   // added by manohar 
                            "expenseName": expenseName,
                            "exp_category_name": exp_category_name,
                            "amountSpent": amountSpent,
                            "expenseTime": expenseTime,
                            "customerName": customerName,
                            "status": status,                         
                        }];
                }
                var i=0;
                for (i = 0; i < exportAccounts.length; i++) {
                    
               csvData.push('"' + exportAccounts[i][0].fullname + '","' + exportAccounts[i][0].expenseName + '","' + exportAccounts[i][0].exp_category_name + '","' + exportAccounts[i][0].amountSpent + '","' + exportAccounts[i][0].expenseTime + '","' + exportAccounts[i][0].customerName + '","' + exportAccounts[i][0].status +'"');
                    //modified  by manohar 
                }
                csvData.push('"' +" "+ '","' +" "+ '","'  +"Total:"+ '","'  +all_total_amts+ '","'  +" "+ '","'  +" "+ '","'  +" "+'"');
           
                var fileName = "All_Expenses.csv";
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
                $('#id_exportExpense').html('');
                link.innerHTML = '<button type="button" class="btn btn-info fr" style="margin-bottom:10px;"><i class=" fa fa-file-text" id="id_exportExpenseChild"></i> Save as CSV</button>';
                document.getElementById("id_exportExpense").appendChild(link);
                $('#id_exportExpenseChild').click();
            }
        }
    });
}
function displayExpDetails(index){

	var details=expensearray[index];

	var templatehtml="";
	var footerhtml="";
	var status=details.expense_data.status;
        var expenseCSV=details.expense_data.expenseImageCsv;
        //var userId=details.expense_data.user.id;
        //var accountId=details.expense_data.user.accountId;

	templatehtml+='<div class="form-group">';
	templatehtml+='<label class="col-md-4 control-label">Expense Head</label>';
	templatehtml+='<div class="col-md-8">';
	templatehtml+='<input type="name" class="form-control" Value="'+details.expense_data.expenseName+'" tabindex="1" readonly>';
	templatehtml+='</div>';
	templatehtml+='</div>';
	
	templatehtml+='<div class="form-group">';
	templatehtml+='<label class="col-md-4 control-label">Date/Time</label>';
	templatehtml+='<div class="col-md-8">';
	templatehtml+='<input type="text" class="form-control" value="'+details.expenseTime+'" tabindex="2" readonly>';
	templatehtml+='</div>';
	templatehtml+='</div>';

	templatehtml+='<div class="form-group">';
	templatehtml+='<label class="col-md-4 control-label">Customer Name</label>';
	templatehtml+='<div class="col-md-8">';
	templatehtml+='<input type="name" class="form-control" Value="'+details.expense_data.customerName+'" tabindex="3" readonly>';
	templatehtml+='</div>';
	templatehtml+='</div>';

	templatehtml+='<div class="form-group">';
	templatehtml+='<label class="col-md-4 control-label">Visit</label>';
	templatehtml+='<div class="col-md-8">';
	templatehtml+='<textarea class="form-control" style="resize: none;" tabindex="4" readonly>'+details.expense_data.appointment.title+'</textarea>';
	templatehtml+='</div>';
	templatehtml+='</div>';
	
        if(expenseCSV==null){
            templatehtml+='<div class="form-group">';
            templatehtml+='<label class="col-md-4 control-label">Attachment</label>';
            templatehtml+='<div class="col-md-8">';
            templatehtml+='<p class="form-control-static"><img src="' + expenseImageURLPath + 'expense_' + accountId + '_' + details.expense_data.user.id + '_' + details.expense_data.id + '.png?' + time + '" onerror="this.src=\'assets/img/noimage.jpg\';"/></p>';
            templatehtml+='</div>';
            templatehtml+='</div>';
        }else{
            var splitExpense = expenseCSV.split(",");
            for(var i=0; i<splitExpense.length; i++){
            templatehtml+='<div class="form-group">';
            templatehtml+='<label class="col-md-4 control-label">Attachment</label>';
            templatehtml+='<div class="col-md-8">';
            templatehtml+='<p class="form-control-static"><img src="' + expenseImageURLPath + 'expense_' + accountId + '_' + details.expense_data.user.id + '_' + details.expense_data.id +'_'+splitExpense[i] +'.png?' + time + '" onerror="this.src=\'assets/img/noimage.jpg\';"/></p>';
            templatehtml+='</div>';
            templatehtml+='</div>';  
            }
        
        }

	templatehtml+='<div class="form-group">';
	templatehtml+='<label class="col-md-4 control-label">Amount Claimed <span>&#8377;</span></label>';
	templatehtml+='<div class="col-md-8">';
	templatehtml+='<input type="text" class="form-control" value="'+details.expense_data.amountSpent+'" tabindex="5" readonly>';
	templatehtml+='</div>';
	templatehtml+='</div>';

	
	templatehtml+='<div class="form-group">';
	templatehtml+='<label class="col-md-4 control-label">Status</label>';
	templatehtml+='<div class="col-md-8">';
	templatehtml+='<select class="form-control" id="expense_status" data-placeholder="Select..." disabled tabindex="6"">';
	if(status==0){
		templatehtml+='<option value="0" selected>Pending Approval by Reporting Head</option>';
	}else if(status==1){
		templatehtml+='<option value="1" selected>Pending Approval by Accounts</option>';
	}else if(status==2){
		templatehtml+='<option value="2" selected>Rejected by Reporting Head</option>';
	}else if(status==3){
		templatehtml+='<option value="3" selected>Approved</option>';
	}else if(status==4){
		templatehtml+='<option value="4" selected>Rejected by Accounts</option>';
	}else if(status==5){
		templatehtml+='<option value="5" selected>Disbursed</option>';
	}
	templatehtml+='</select>';
	templatehtml+='</div>';
	templatehtml+='</div>';
	
	templatehtml+='<div class="form-group">';
	templatehtml+='<label class="col-md-4 control-label">Reporting Head</label>';
	templatehtml+='<div class="col-md-8">';
	templatehtml+='<select class="form-control" data-placeholder="Select..." disabled tabindex="7">';
	templatehtml+='<option value="'+details.expense_data.report_head.fullname+'" selected>'+details.expense_data.report_head.fullname+'</option>';
	templatehtml+='</select>';
	templatehtml+='</div>';
	templatehtml+='</div>'

	templatehtml+='<div class="form-group">';
	templatehtml+='<label class="col-md-1 control-label">&nbsp;</label>';
	templatehtml+='<div class="col-md-11">';
	templatehtml+='<div class="radio-list">';
	if(status==1){
		templatehtml+='<label class="radio-inline"><input type="radio" name="optionsRadios" id="optionsRadios1" value="option1" tabindex="8">Approve</label>';
	}else{
		templatehtml+='<label class="radio-inline"><input type="radio" name="optionsRadios" id="optionsRadios1" value="option1" tabindex="8" disabled>Approve</label>';		
	}
	if(status==1 || status==3){
		templatehtml+='<label class="radio-inline"><input type="radio" name="optionsRadios" id="optionsRadios2" value="option2" tabindex="9">Disburse (will be approved automatically)</label>';
	}else{
		templatehtml+='<label class="radio-inline"><input type="radio" name="optionsRadios" id="optionsRadios2" value="option2" tabindex="9" disabled>Disburse (will be approved automatically)</label>';		
	}
	if(status==1 || status==2 || status==3){
		templatehtml+='<label class="radio-inline"><input type="radio" name="optionsRadios" id="optionsRadios3" value="option3" tabindex="10">Reject</label>';
	}else{
		templatehtml+='<label class="radio-inline"><input type="radio" name="optionsRadios" id="optionsRadios3" value="option3" tabindex="10" disabled>Reject</label>';		
	}		
	templatehtml+='</div>';
	templatehtml+='</div>';
	templatehtml+='</div>';

	if(status==5){

		templatehtml+='<div class="form-group" id="exp_amount">';
		templatehtml+='<label class="col-md-4 control-label">Amount to be disbursed <span>&#8377;</span></label>';
		templatehtml+='<div class="col-md-8">';
		templatehtml+='<input type="text" id="dis_amount" class="form-control" value="'+details.expense_data.disburse_amount+'" tabindex="11" readonly>';
		templatehtml+='</div>';
		templatehtml+='</div>';
	
		templatehtml+='<div class="form-group" id="exp_mode">';
		templatehtml+='<label class="col-md-4 control-label">Mode</label>';
		templatehtml+='<div class="col-md-8">';
		templatehtml+='<input type="text" id="dis_mode" class="form-control" value="'+details.expense_data.payment_mode+'" tabindex="11" readonly>';
		templatehtml+='</div>';
		templatehtml+='</div>';
		
		/*templatehtml+='<div class="form-group" id="exp_mode">';
		templatehtml+='<label class="col-md-4 control-label">Mode</label>';
		templatehtml+='<div class="col-md-8">';
		templatehtml+='<select class="form-control" id="dis_mode" data-placeholder="Select..." tabindex="12" disabled>';
		if(details.expense_data.payment_mode==0){
			templatehtml+='<option value="0" selected>Cash</option>';
		}else if(details.expense_data.payment_mode==1){
			templatehtml+='<option value="1" selected>Cheque</option>';
		}else{
			templatehtml+='<option value="2" selected>NEFT</option>';
		}				
		templatehtml+='</select>';
		templatehtml+='</div>';
		templatehtml+='</div>'*/

		templatehtml+='<div class="form-group" id="exp_date">';
		templatehtml+='<label class="col-md-4 control-label">Date</label>';
		templatehtml+='<div class="col-md-8">';
		var defdate=convertToLocalTime(details.expense_data.default_date).split(",")[0].trim();
		for(i=0;i<dicount.length;i++){
			if(dicount[i]==details.expense_data.id){
				defdate=details.expense_data.default_date;
			}		
		}
		templatehtml+='<input class="form-control" id="default_date" size="16" type="text" value="'+defdate+'" tabindex="13" readonly/>';
		templatehtml+='<span class="help-block">';	
		templatehtml+='<label class="checkbox-inline" style="padding-left:0px;"><input type="checkbox" value="option1" disabled="disabled" tabindex="14" > Save as default date </label>';
		templatehtml+='</span>';
		templatehtml+='</div>';
		templatehtml+='</div>';
	}else{
		templatehtml+='<div class="form-group" id="exp_amount" style="display:none">';
		templatehtml+='<label class="col-md-4 control-label">Amount to be disbursed <span>&#8377;</span></label>';
		templatehtml+='<div class="col-md-8">';
		templatehtml+='<input type="text" id="dis_amount" class="form-control"   value="'+details.expense_data.amountSpent+'" tabindex="11">';
		templatehtml+='</div>';
		templatehtml+='</div>';
	
		templatehtml+='<div class="form-group" id="exp_mode"  style="display:none">';
		templatehtml+='<label class="col-md-4 control-label">Mode</label>';
		templatehtml+='<div class="col-md-8">';
		templatehtml+='<input type="text" id="dis_mode" class="form-control" value="" tabindex="11">';
		templatehtml+='</div>';
		templatehtml+='</div>';

		/*templatehtml+='<div class="form-group" id="exp_mode" style="display:none">';
		templatehtml+='<label class="col-md-4 control-label">Mode</label>';
		templatehtml+='<div class="col-md-8">';
		templatehtml+='<select class="form-control" id="dis_mode" data-placeholder="Select..." tabindex="12">';
		templatehtml+='<option value="0">Cash</option>';
		templatehtml+='<option value="1" selected>Cheque</option>';
		templatehtml+='<option value="2">NEFT</option>';
		templatehtml+='</select>';
		templatehtml+='</div>';
		templatehtml+='</div>'*/

		templatehtml+='<div class="form-group" id="exp_date" style="display:none">';
		templatehtml+='<label class="col-md-4 control-label">Date</label>';
		templatehtml+='<div class="col-md-8">';

		var date = new Date();
		//var firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
		var lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);
		var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
		var lastDayWithSpaces = (lastDay.getDate()) + ' ' + (monthNames[lastDay.getMonth()]) + ' ' + lastDay.getFullYear();
		var defadate=convertToLocalTime(details.expense_data.default_date).split(",")[0].trim();
		if(set_def_date!=0){
			var defdate1=set_def_date.split(' ')[0];
			var defdate2=0;
			if(parseInt(defdate1)>parseInt(lastDay.getDate())){
				defdate2=(lastDay.getDate()) + ' ' + (monthNames[lastDay.getMonth()]) + ' ' + lastDay.getFullYear();
				templatehtml+='<input class="form-control date-picker" id="default_date" size="16" type="text" value="'+defdate2+'" tabindex="13" />';

			}else{
				defdate2=(defdate1) + ' ' + (monthNames[lastDay.getMonth()]) + ' ' + lastDay.getFullYear();
			templatehtml+='<input class="form-control date-picker" id="default_date" size="16" type="text" value="'+defdate2+'" tabindex="13" />';
			}
		}else{
			if(defadate!="18 Nov 1111"){
				var defdate1=defadate.split(' ')[0];
				var defdate2=0;
				if(parseInt(defdate1)>parseInt(lastDay.getDate())){
					defdate2=(lastDay.getDate()) + ' ' + (monthNames[lastDay.getMonth()]) + ' ' + lastDay.getFullYear();
					templatehtml+='<input class="form-control date-picker" id="default_date" size="16" type="text" value="'+defdate2+'" tabindex="13" />';
				
				}else{
					defdate2=(defdate1) + ' ' + (monthNames[lastDay.getMonth()]) + ' ' + lastDay.getFullYear();
				templatehtml+='<input class="form-control date-picker" id="default_date" size="16" type="text" value="'+defdate2+'" tabindex="13" />';
				}
			}else{
			
				templatehtml+='<input class="form-control date-picker" id="default_date" size="16" type="text" value="'+lastDayWithSpaces+'" tabindex="13" />';
			}
		}
		templatehtml+='<span class="help-block">';	
		templatehtml+='<label class="checkbox-inline" style="padding-left:0px;"><input type="checkbox" value="option1" id="dis_default_date" tabindex="14" > Save as default date </label>';
		templatehtml+='</span>';
		templatehtml+='</div>';
		templatehtml+='</div>';

	}

	if(status==4){
		templatehtml+='<div class="form-group" id="exp_reason">';
		templatehtml+='<label class="col-md-4 control-label">Rejected Reason</label>';
		templatehtml+='<div class="col-md-8">';
		templatehtml+='<textarea id="reject_reason" class="form-control" style="resize: none;" tabindex="15" readonly>'+details.expense_data.rejectedReson+'</textarea>';
		templatehtml+='</div>';
		templatehtml+='</div>';
	}else{
		templatehtml+='<div class="form-group" id="exp_reason" style="display:none">';
		templatehtml+='<label class="col-md-4 control-label">Rejected Reason</label>';
		templatehtml+='<div class="col-md-8">';
		templatehtml+='<textarea id="reject_reason" class="form-control" style="resize: none;" tabindex="15"></textarea>';
		templatehtml+='</div>';
		templatehtml+='</div>';

	}
	if(status==0 || status==4 || status==5){
		footerhtml+='<button type="button" class="btn btn-info" disabled onclick="saveAppRegDis('+index+');" tabindex="16">Save</button>';
	}else{
		footerhtml+='<button type="button" class="btn btn-info" onclick="saveAppRegDis('+index+');" tabindex="16">Save</button>';
	}
	footerhtml+='<button type="button" data-dismiss="modal" class="btn btn-default" tabindex="17">Close</button>';

	$(".modal-title").html("Expenses - "+details.expense_data.user.fullname);
	$("#exp_footer").html(footerhtml);
	$("#expense_details").html(templatehtml);
	$('.date-picker').datepicker({
        	rtl: App.isRTL(),
        	autoclose: true
    	});
	$("#optionsRadios1").click(function(){
        	if($(this).is(':checked')){            
              		$('#exp_amount').hide();
			$('#exp_mode').hide();
			$('#exp_date').hide();
			$('#exp_reason').hide();
            	}
        });

	$("#optionsRadios2").click(function(){
        	if($(this).is(':checked')){
                	$('#exp_amount').show();
			$('#exp_mode').show();
			$('#exp_date').show();
			$('#exp_reason').hide();
           	 }
            	else{
              		$('#exp_amount').hide();
			$('#exp_mode').hide();
			$('#exp_date').hide();
            	}
        });
	
	$("#optionsRadios3").click(function(){
        	if($(this).is(':checked')){
             		$('#exp_amount').hide();
			$('#exp_mode').hide();
			$('#exp_date').hide();
			$('#exp_reason').show();
           	}else{
			$('#exp_reason').hide();
		}	
        });
	App.initUniform();
	App.fixContentHeight();
	//$(".tooltip fade bottom in").hide();
}

function deSelectAll(){

	var count=0;
	$(".checkboxes").each(function(){
		if($(this).attr("disabled")=="disabled"){	
			count+=1;
		}
	});
	//alert(ele[0].checked+"id"+ele[0].attributes.id.value);
	/*var index=ele[0].attributes.id.value.substring(9,ele[0].attributes.id.value.length);
	if(ele[0].checked==true){	
		if(expensearray[index].expense_data.status==1){	
			approve.push({"element":ele,"id":ele[0].attributes.id.value});	
		}
		reject.push({"element":ele,"id":ele[0].attributes.id.value});
	}else{	
		reject=reject.filter(function (el) {
                      return el.id !== ele[0].attributes.id.value;
                });
		if(expensearray[index].expense_data.status==1){	
			approve=approve.filter(function (el) {
                      	return el.id !== ele[0].attributes.id.value;
                });
		}
	}	*/	
	if($('.checkboxes:checked').length == $('.checkboxes').length || $('.checkboxes:checked').length == $('.checkboxes').length-count) {
		$(".group-checkable").attr('checked', true);
		$(".group-checkable").parent().attr('class','checked');
    	}else{
		$(".group-checkable").attr('checked', false);
		$(".group-checkable").parent().removeClass('checked');
	}
	if($('.checkboxes:checked').length>0){
		$("#btnApprovepo").show();
	}else{
		$("#btnApprovepo").hide();
	}
	App.initUniform();
	App.fixContentHeight();
	jQuery('.tooltips').tooltip();
}


function approveAllExpense(){

var checkboxlist=[];
var jsondata=[];
	$(".checkboxes").each(function(){
		if($(this).is(":checked")){
			var index=$(this).attr("id").substring(9,$(this).attr("id").length);
			if(expensearray[index].expense_data.status==1){	
				checkboxlist.push($(this).attr("id").substring(9,$(this).attr("id").length));
			}
		}
	});
	if(checkboxlist.length==0){
		fieldSenseTosterError("Expenses cannot be approved", true);
		return false;
	}
	for(i=0;i<checkboxlist.length;i++){
		var jsondatalocal={
			"id":expensearray[checkboxlist[i]].expense_data.id,
			"appointmentId":expensearray[checkboxlist[i]].expense_data.appointmentId,
			"customerId":expensearray[checkboxlist[i]].expense_data.customerId,
			"user":{
				"id":expensearray[checkboxlist[i]].expense_data.user.id	
			},
			"expenseName":expensearray[checkboxlist[i]].expense_data.expenseName,
			"expenseType":expensearray[checkboxlist[i]].expense_data.expenseType,
			"description":expensearray[checkboxlist[i]].expense_data.description,
			"amountSpent":expensearray[checkboxlist[i]].expense_data.amountSpent,
			"status":3,
			"expenseTime":new Date(expensearray[checkboxlist[i]].expense_data.expenseTime.time),
			"submittedOn":new Date(expensearray[checkboxlist[i]].expense_data.submittedOn.time),
			//"approvedOrRejectedOn":new Date(expensearray[index].expense_data.approvedOrRejectedOn.time),
			"approvedOrRjectedBy":{
				"id":loginUserId,
			},
			//"rejectedReson":rej_reason,
			"createdOn":new Date(expensearray[checkboxlist[i]].expense_data.createdOn.time),
			"eCategoryName":{
				"id":expensearray[checkboxlist[i]].expense_data.eCategoryName.id
			},
			//"disburse_amount":dis_amt,
			//"payment_mode":pay_mode,
			//"default_date":default_date
			"exp_category_name":expensearray[checkboxlist[i]].expense_data.exp_category_name
		}
		jsondata.push(jsondatalocal);

	}


	var jsonData = JSON.stringify(jsondata);
	$('#pleaseWaitDialog').modal('show');    
	$.ajax({
		type: "PUT",
		url:fieldSenseURL + "/expense/Account/approve/multiple",
		contentType: "application/json; charset=utf-8",
		headers:{
			"userToken": userToken
		},
		crossDomain: false,
		data:jsonData,
		cache: false,
		dataType: 'json',
		asyn:true,
		success: function(data) {
				if(data.errorCode=='0000'){
					$('#basic').modal('toggle');
					fieldSenseTosterSuccess(data.errorMessage, true);
					var table = $('#example').DataTable();
					table.draw( false );

					/*if(document.getElementById("exp_status").value.trim()!="All"){
						for(i=0;i<approve.length;i++){
							approve_all=false;
							all_total_amts=all_total_amts-expensearray[checkboxlist[i]].expense_data.amountSpent;
							var target_row = approve[i].element.closest("tr").get(0); // this line did the trick
    							var aPos = $('#example').dataTable().fnGetPosition(target_row); 
    							$('#example').dataTable().fnDeleteRow(aPos);
						}

					}else{
						for(i=0;i<approve.length;i++)						{	
							var rowIndex = $('#example').dataTable().fnGetPosition(approve[i].element.closest("tr").get(0));					
							
							$('#example').dataTable().fnUpdate('<span class="label label-sm label-info" title="Approved">Approved</span>', rowIndex , 7,false);
							expensearray[checkboxlist[i]].expense_data.status=3;
						}
					}
					approve=[];
					reject=[];
					approve_all=true;
					reject_all=true;*/
				}else{
					$('#pleaseWaitDialog').modal('hide');
        	    			fieldSenseTosterError(data.errorMessage, true);
              			}
				
			},
			error: function(xhr, ajaxOptions, thrownError)
            		{	$('#pleaseWaitDialog').modal('hide');
				alert("in error:"+thrownError);
            		}
		});

}

function rejectAllExpense(){

var reason=$("#reason_text").val().trim();
var checkboxlist1=[];
var jsondata=[];
	//if(reason==""){
		//fieldSenseTosterError("Reason for Reject shouldn't be empty");
        	//return false;	
	//}
	$(".checkboxes").each(function(){
		if($(this).is(":checked")){
			checkboxlist1.push($(this).attr("id").substring(9,$(this).attr("id").length));
		}
	});
	if(checkboxlist1.length==0){
		fieldSenseTosterError("Expenses cannot be rejected", true);
	}
	for(i=0;i<checkboxlist1.length;i++){
		var jsondatalocal={
			"id":expensearray[checkboxlist1[i]].expense_data.id,
			"appointmentId":expensearray[checkboxlist1[i]].expense_data.appointmentId,
			"customerId":expensearray[checkboxlist1[i]].expense_data.customerId,
			"user":{
				"id":expensearray[checkboxlist1[i]].expense_data.user.id	
			},
			"expenseName":expensearray[checkboxlist1[i]].expense_data.expenseName,
			"expenseType":expensearray[checkboxlist1[i]].expense_data.expenseType,
			"description":expensearray[checkboxlist1[i]].expense_data.description,
			"amountSpent":expensearray[checkboxlist1[i]].expense_data.amountSpent,
			"status":4,
			"expenseTime":new Date(expensearray[checkboxlist1[i]].expense_data.expenseTime.time),
			"submittedOn":new Date(expensearray[checkboxlist1[i]].expense_data.submittedOn.time),
			//"approvedOrRejectedOn":new Date(expensearray[index].expense_data.approvedOrRejectedOn.time),
			"approvedOrRjectedBy":{
				"id":loginUserId,
			},
			"rejectedReson":reason,
			"createdOn":new Date(expensearray[checkboxlist1[i]].expense_data.createdOn.time),
			"eCategoryName":{
				"id":expensearray[checkboxlist1[i]].expense_data.eCategoryName.id
			},
			//"disburse_amount":dis_amt,
			//"payment_mode":pay_mode,
			//"default_date":default_date
			"exp_category_name":expensearray[checkboxlist1[i]].expense_data.exp_category_name
		}
		jsondata.push(jsondatalocal);

	}


	var jsonData = JSON.stringify(jsondata);
	$('#pleaseWaitDialog').modal('show');
	$.ajax({
		type: "PUT",
		url:fieldSenseURL + "/expense/Account/reject/multiple",
		contentType: "application/json; charset=utf-8",
		headers:{
			"userToken": userToken
		},
		crossDomain: false,
		data:jsonData,
		cache: false,
		dataType: 'json',
		asyn:true,
		success: function(data) {
				if(data.errorCode=='0000'){
					$('#reject').modal('toggle');
					fieldSenseTosterSuccess(data.errorMessage, true);
					var table = $('#example').DataTable();
					table.draw( false );
		
					/*if(document.getElementById("exp_status").value.trim()!="All"){
						for(i=0;i<reject.length;i++){
							reject_all=false;
							all_total_amts=all_total_amts-expensearray[checkboxlist1[i]].expense_data.amountSpent;
							var target_row = reject[i].element.closest("tr").get(0); // this line did the trick
    							var aPos = $('#example').dataTable().fnGetPosition(target_row); 
    							$('#example').dataTable().fnDeleteRow(aPos);
						}

					}else{
						for(i=0;i<reject.length;i++)						{						
							var rowIndex = $('#example').dataTable().fnGetPosition(reject[i].element.closest("tr").get(0));
							$('#example').dataTable().fnUpdate( '<input type="checkbox" disabled="disabled" class="checkboxes"/>', rowIndex , 0,false);
							$('#example').dataTable().fnUpdate( '<span class="label label-sm label-warning" title="Rejected by Accounts">Rejected</span>', rowIndex , 7,false);
							expensearray[checkboxlist1[i]].expense_data.status=4;
							expensearray[checkboxlist1[i]].expense_data.rejectedReson=reason;
						}
						App.initUniform();
						App.fixContentHeight();
						
					}
					approve=[];
					reject=[];
					reject_all=true;*/
				}else{
					$('#pleaseWaitDialog').modal('hide');
        	    			fieldSenseTosterError(data.errorMessage, true);
              			}
				
			},
			error: function(xhr, ajaxOptions, thrownError)
            		{	$('#pleaseWaitDialog').modal('hide');
				alert("in error:"+thrownError);
            		}
		});;
	
}

function saveAppRegDis(index){

	var radiomand;
	$('.radio-inline').each(function(){
		if($(this).find('span').hasClass('checked')){
			radiomand=true;
			return false;
		}else{
			radiomand=false;
		}		
	});	
	if(radiomand==false){
		fieldSenseTosterError("Any one option should be selected");
		return false;
	}
	var rej_reason=expensearray[index].expense_data.rejectedReson;
	var dis_amt=expensearray[index].expense_data.disburse_amount;
	var pay_mode=expensearray[index].expense_data.payment_mode;
	var default_date=new Date("");	
	var selctedvalue="approved";
	var status=3;
	if($("#optionsRadios3").parent().attr("class")=="checked"){
		rej_reason=$("#reject_reason").val().trim();
		//if(rej_reason==""){
			//fieldSenseTosterError("Rejected Reason should't be empty");
			//return false;
		//}else{
			selctedvalue="rejected";
			status=4;
		//}
	}else if($("#optionsRadios2").parent().attr("class")=="checked"){
		dis_amt=$("#dis_amount").val().trim();
		pay_mode=$("#dis_mode").val().trim();
		default_date=new Date($("#default_date").val().trim());
		var date = new Date();
		var deffdate=$("#default_date").val().trim();
		var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
		if(deffdate.split(' ')[2]<date.getFullYear()){
			fieldSenseTosterError("disburse date shouldn't be less than current date");
			return false;
		}else if(monthNames.indexOf(deffdate.split(' ')[1])<date.getMonth()){
			fieldSenseTosterError("disburse date shouldn't be less than current date");
			return false;
		}else if(deffdate.split(' ')[0]<date.getDate()){
			fieldSenseTosterError("disburse date shouldn't be less than current date");
			return false;
		}
		//var firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
		//var lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);
		////var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
		//var lastDayWithSpaces = (lastDay.getDate()) + ' ' + (monthNames[lastDay.getMonth()]) + ' ' + lastDay.getFullYear();
		/*default_date=default_date.replace(/Jan/g,"01");
		default_date=default_date.replace(/Feb/g,"02");
		default_date=default_date.replace(/Mar/g,"03");
		default_date=default_date.replace(/Apr/g,"04");
		default_date=default_date.replace(/May/g,"05");
		default_date=default_date.replace(/Jun/g,"06");
		default_date=default_date.replace(/Jul/g,"07");
		default_date=default_date.replace(/Aug/g,"08");
		default_date=default_date.replace(/Sep/g,"09");
		default_date=default_date.replace(/Oct/g,"10");
		default_date=default_date.replace(/Nov/g,"11");
		default_date=default_date.replace(/Dec/g,"12");

		default_date = default_date.split(' ');
		default_date = convertLocalDateToServerDate(default_date[2], default_date[1] - 1, default_date[0], 0, 0);		*/
		
		if(dis_amt==""){
			fieldSenseTosterError("Amount to be disbursed  should't be empty");
			return false;
		}else if(!/^[0-9]/.test(dis_amt)){
			fieldSenseTosterError("Amount to be disbursed  should be in digits");
			return false;
		}else{
			selctedvalue="disbursed";
			status=5;
		}
	}

	var jsondata={
		"id":expensearray[index].expense_data.id,
		"appointmentId":expensearray[index].expense_data.appointmentId,
		"customerId":expensearray[index].expense_data.customerId,
		"user":{
			"id":expensearray[index].expense_data.user.id	
		},
		"expenseName":expensearray[index].expense_data.expenseName,
		"expenseType":expensearray[index].expense_data.expenseType,
		"description":expensearray[index].expense_data.description,
		"amountSpent":expensearray[index].expense_data.amountSpent,
		"status":status,
		"expenseTime":new Date(expensearray[index].expense_data.expenseTime.time),
		"submittedOn":new Date(expensearray[index].expense_data.submittedOn.time),
		//"approvedOrRejectedOn":new Date(expensearray[index].expense_data.approvedOrRejectedOn.time),
		"approvedOrRjectedBy":{
			"id":loginUserId,
		},
		"rejectedReson":rej_reason,
		"createdOn":new Date(expensearray[index].expense_data.createdOn.time),
		"eCategoryName":{
			"id":expensearray[index].expense_data.eCategoryName.id
		},
		"disburse_amount":dis_amt,
		"payment_mode":pay_mode,
		"default_date":default_date,
		"exp_category_name":expensearray[index].expense_data.exp_category_name
	}
	var url=fieldSenseURL + "/expense/Account/approve";
	if(selctedvalue=="rejected"){
		url=fieldSenseURL + "/expense/Account/reject";
	}else if(selctedvalue=="disbursed"){
		if($("#dis_default_date").is(":checked")){
			set_def_date=$("#default_date").val().trim();
			url=fieldSenseURL + "/expense/Account/disburse/defdate";
		}else{
			url=fieldSenseURL + "/expense/Account/disburse";
		}
	}
	var jsonData = JSON.stringify(jsondata);
	$('#pleaseWaitDialog').modal('show');
	$.ajax({
		type: "PUT",
		url:url,
		contentType: "application/json; charset=utf-8",
		headers:{
			"userToken": userToken
		},
		crossDomain: false,
		data:jsonData,
		cache: false,
		dataType: 'json',
		asyn:true,
		success: function(data) {
				if(data.errorCode=='0000'){
					$('#expenses').modal('toggle');
					fieldSenseTosterSuccess(data.errorMessage, true);
					$(".group-checkable").attr('checked', false);
					$(".group-checkable").parent().removeClass('checked');
					var table = $('#example').DataTable();
					table.draw( false );
					

					/*if(document.getElementById("exp_status").value.trim()!="All"){
						all_total_amts=all_total_amts-expensearray[index].expense_data.amountSpent;
						var target_row = element.closest("tr").get(0); // this line did the trick
    						var aPos = $('#example').dataTable().fnGetPosition(target_row); 
    						$('#example').dataTable().fnDeleteRow(aPos);

					}else{
						if(status==3){
							var rowIndex = $('#example').dataTable().fnGetPosition( element.closest("tr").get(0));
							$('#example').dataTable().fnUpdate( '<span class="label label-sm label-info"  title="Approved">Approved</span>', rowIndex , 7,false);
							expensearray[index].expense_data.status=3;
						}else if(status==4){						
							var rowIndex = $('#example').dataTable().fnGetPosition( element.closest("tr").get(0));
							$('#example').dataTable().fnUpdate( '<input type="checkbox" disabled="disabled" class="checkboxes"/>', rowIndex , 0,false);
							$('#example').dataTable().fnUpdate( '<span class="label label-sm label-warning" title="Rejected by Accounts">Rejected</span>', rowIndex , 7,false);
							expensearray[index].expense_data.status=4;
							expensearray[index].expense_data.rejectedReson=rej_reason;
							App.initUniform();
							App.fixContentHeight();
						}else if(status==5){
							var rowIndex = $('#example').dataTable().fnGetPosition( element.closest("tr").get(0));
							$('#example').dataTable().fnUpdate( '<input type="checkbox" disabled="disabled" class="checkboxes"/>', rowIndex , 0,false);
							$('#example').dataTable().fnUpdate( '<span class="label label-sm label-success"  title="Disbursed">Disbursed</span>', rowIndex , 7,false);
							expensearray[index].expense_data.status=5;
							expensearray[index].expense_data.disburse_amount=dis_amt;
							expensearray[index].expense_data.payment_mode=pay_mode;
							//var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
							//var newdate = default_date.getDate() + ' ' + monthNames[default_date.getMonth()] + ' ' + default_date.getFullYear();
							dicount.push(expensearray[index].expense_data.id);
							expensearray[index].expense_data.default_date=$("#default_date").val().trim();
							App.initUniform();
							App.fixContentHeight();
						}
					}*/

				}else{
					$('#pleaseWaitDialog').modal('hide');
        	    			fieldSenseTosterError(data.errorMessage, true);
              			}
				
			},
			error: function(xhr, ajaxOptions, thrownError)
            		{	$('#pleaseWaitDialog').modal('hide');
				alert("in error:"+thrownError);
            		}
		});

}

function selectAllCheck(){

	if($(".group-checkable").is(':checked')){
		//reject=[];
		//approve=[];
		$(".checkboxes").each(function(){
			if($(this).attr("disabled")!="disabled"){	
				$(this).attr('checked', true);	
				$(this).parent().attr('class','checked');
				/*var index=$(this).attr("id").substring(9,$(this).attr("id").length);
				if(expensearray[index].expense_data.status==1){	
					approve.push({"element":$(this),"id":$(this).attr("id")});	
				}
				reject.push({"element":$(this),"id":$(this).attr("id")});*/
				$('#btnApprovepo').show();
			}	
			
		});
	}else{
		$(".checkboxes").each(function(){
			$(this).attr('checked',false);	
			$(this).parent().removeClass('checked');
		});
		//reject=[];
		//approve=[];
        	$('#btnApprovepo').hide();
        }
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

function clearReason(){
	$("#reason_text").val('');
}
