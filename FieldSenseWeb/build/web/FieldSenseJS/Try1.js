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
            if(role==0){
              //	leftSideMenu();
                //getAccountRegions(); // load all region , from same method set region in search filter
		//statsData();
                window.clearTimeout(intervalAdminUser);
            }else{
                window.location.href ="dashboard.html";
            }

        }
    }
    catch (err) {
     window.location.href = "login.html";
    }
}, 1000);

var accountRegions;

function getAccountRegions (){
	$.ajax({
		type: "GET",
		url: fieldSenseURL+"/account/regions",
		contentType: "application/json; charset=utf-8",
		headers:{
		    "userToken": userToken
		},
		crossDomain: false,
		//data:jsonData,
		cache: false,
		dataType: 'json',
		asyn:false,
		success: function(data) {
		    if (data.errorCode=='0000') {
		       accountRegions=data.result;
		    }
		    var regionFilterData="<option value='0' selected>All</option>";

        	    $.each( accountRegions, function ( i, data ) {
				regionFilterData+='<option value="'+data.id+'">'+data.region_name+'</option>';
		    } );
		    $("#id_searchRegion").html(regionFilterData);
		},
		error:function(xhr, status, error){
		    var err = eval("(" + xhr.responseText + ")");
		    var message=err.fieldErrors[0].message;
		    fieldSenseTosterError(message,true);
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
       
}

function clearFilters() {
  	$("#id_status").val("All");
	$("#id_activityFrequency").val("Any");
	$("#id_accSearchText").val("");
	$("#id_searchRegion").val("0");
	statsData();
}

function statsData(){

	var accname=$('#id_accSearchText').val();
	var status=$("#id_status").val();
	var actfreq=$("#id_activityFrequency").val();
	var regionId=$("#id_searchRegion").val();
        var start=0;
	var length=20;
	myTable=$("#sample_5").DataTable();
	if(myTable != null)myTable.destroy();
        var statsDataHtml = '';
        statsDataHtml += '<table class="table table-striped table-bordered table-hover" id="sample_5">';
        statsDataHtml += '<thead>';
        statsDataHtml += '<tr>';
        statsDataHtml += '<th>';
        statsDataHtml += 'Account Name';
        statsDataHtml += '</th>';
        statsDataHtml += '<th>';
        statsDataHtml += 'User Count';
        statsDataHtml += '</th>';
        statsDataHtml += '<th>';
        statsDataHtml += 'User Limit';
        statsDataHtml += '</th>';
        statsDataHtml += '<th>';
        statsDataHtml += 'Created On';
        statsDataHtml += '</th>';
        statsDataHtml += '<th>';
        statsDataHtml += 'Status';
        statsDataHtml += '</th>';
        statsDataHtml += '<th>';
        statsDataHtml += 'Last Access Date';
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
        statsDataHtml += '</tbody>';
        statsDataHtml += '</table>';
        $('#id_statsData').html(statsDataHtml);
        var inboxtable = $('#sample_5').dataTable({
        	"bServerSide": true,
                "bDestroy": true,
                "ajax": {
   		 	"url": fieldSenseURL + "/stats/accountsData/offset/?accname="+accname+"&status="+status+"&actfreq="+actfreq+"&regionId="+regionId,
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
                            "aTargets": [08] 
			                             
                	}
		],
		"aoColumns": [ // ahmed : here we populate the data in datatable that is returned using Ajax
			      { "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						return full.accountName;
					}
				},
				{ "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						return full.usersCount-1;/*removed comapny default user*/
					}
				},
				{ "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						return full.usersLimit;
					}
				},
				{ "mData": null,
				"sClass":"hidden-xs",
				"mRender" : function(data,type,full){
			            		var createdOn= full.createdOn;
                                    		createdOn=convertServerDateToLocalDate((createdOn.year) + 1900, createdOn.month, createdOn.date, createdOn.hours, createdOn.minutes);
                                    		var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
                                    		createdOn=createdOn.getDate()+' '+monthNames[createdOn.getMonth()]+' '+createdOn.getFullYear();
				   		return createdOn;	
                                }
				},
                                { "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						if(full.isActive){
                                                	return '<span class="label label-sm label-success">Active</span>';
                                                }else{
                                                    	return '<span class="label label-sm label-danger">InActive</span>'; 
                                                }
					}
				},
				{ "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
                                     		var lastAccessDate= full.lastPunchInDateOfanAccount;
                                    		lastAccessDate=convertServerDateToLocalDate((lastAccessDate.year) + 1900, lastAccessDate.month, lastAccessDate.date, lastAccessDate.hours, lastAccessDate.minutes);
                                    		var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
                                    		lastAccessDate=lastAccessDate.getDate()+' '+monthNames[lastAccessDate.getMonth()]+' '+lastAccessDate.getFullYear();
						if(lastAccessDate=="1 Jan 1970"){
							return '';
						}else{
							return lastAccessDate;
						}

					}
				},
                                { "mData": null,
				"sClass":"hidden-xs",
				"mRender" : function(data,type,full){
						return full.activityFrequency;
                                                
					}
				},
				{ "mData": null,
				"sClass":"hidden-xs",
				"mRender" : function(data,type,full){
						return full.regionName;//regionName
                                                
					}
				},
                                { "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
                                    return '<button type="button" class="btn btn-default btn-xs tooltips" data-toggle="modal" href="#responsive2" data-placement="bottom" data-original-title="Edit" onclick="editAccTemplate(\''+full.id+'\');"><i class="fa fa-edit"></i></button>';
					}
				}
		],
		"aaSorting": [3,"desc"],
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
		},
                });
                jQuery('#sample_5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
                jQuery('#sample_5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
                jQuery('#sample_5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown		
}

function showUser(){
   
   

	var mobile=$('#id_accSearchText').val();
//	var status=$("#id_status").val();
//	var actfreq=$("#id_activityFrequency").val();
//	var regionId=$("#id_searchRegion").val();
       var start=0;
	var length=20;
	myTable=$("#sample_5").DataTable();
	if(myTable != null)myTable.destroy();
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
        statsDataHtml += '<th>';
       
        statsDataHtml += '</th>';
        statsDataHtml += '</tr>';
        statsDataHtml += '</thead>';
        statsDataHtml += '<tbody>';
        statsDataHtml += '<tr>';
        statsDataHtml += '<td>';
        statsDataHtml += '</td>';
        statsDataHtml += '<td>';
        statsDataHtml += '</td>';
        statsDataHtml += '<td>';
        statsDataHtml += '</td>';
        statsDataHtml += '</tr>';
        statsDataHtml += '</tbody>';
        statsDataHtml += '</table>';
        $('#id_statsData').html(statsDataHtml);
        var inboxtable = $('#sample_5').dataTable({
        	"bServerSide": true,
                "bDestroy": true,
                "ajax": {
   		 	"url": fieldSenseURL +"/stats/account/user/mobileno?searchText=" +mobile,
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
                 // "sProcessing":"Loading",
		 "sEmptyTable":"No data available in table",
                 "sLengthMenu": "_MENU_ <span id ='id_totalCustRecords'></span>",
		//"sInfo": "Showing _START_ to _END_ of _TOTAL_ Mails",
              	"oPaginate": {
   		        "sPrevious": "Prev",
                        "sNext": "Next"
               		}
                },
//		"aoColumnDefs": [
//                        {   "bSortable": false,
//                            "aTargets": [08] 
//			                             
//                	}
//		],
		"aoColumns": [ // ahmed : here we populate the data in datatable that is returned using Ajax
			      { "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						return data.full_name;
                                                //return "siddhesh";
					}
				},
				{ "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
					return data.accountName;/*removed comapny default user*/
                                              //  return "siddhesh"
					}
				},
				{ "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
                                    
						return data.email_id;
                                               // return "siddhesh"
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
                                { "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
                                    //return data.id;
                                    return '<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" data-original-title="Delete" onClick="ResetPass('+data.id+');"><i class="fa fa-trash-o"></i></button>';
                                  //  customerShowTemplateHtml+='<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" data-original-title="Delete" onclick="deleteCustomer(\''+customerId+'\',\''+customerName+'\')"><i class="fa fa-trash-o"></i></button>';
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
		},
                });
                jQuery('#sample_5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
                jQuery('#sample_5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
                jQuery('#sample_5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown		
}

function ResetPass(id){
    //var deletes = confirm("Are you sure you want to delete " + customernm + "");
    //if (deletes == true)
    //{
  
	bootbox.dialog({
            
  		message: "Are you sure you want to Reset Password?",
  		title: "Reset Password",
  		buttons: {
    			yes: {
                            
      				label: "Yes",
      				className: "btn-default",
      				callback: function() {
        				$('#pleaseWaitDialog').modal('show');
        				$.ajax({
                                            
            					type: "POST",
            					url: fieldSenseURL + "/stats/" + id,
            					contentType: "application/json; charset=utf-8",
            					headers: {
                					"userToken": userToken
            					},
            					crossDomain: false,
            					cache: false,
            					dataType: 'json',
            					success: function (data) {
                					if (data.errorCode == '0000') {
								$('#pleaseWaitDialog').modal('hide');
                                                                alert("password reset");
                    						fieldSenseTosterSuccess(data.errorMessage, true);
								//var table = $('#sample_5').DataTable();
								//table.draw( false ); 
                                                                showUser();
                					} else {
                    						$('#pleaseWaitDialog').modal('hide');
                    						FieldSenseInvalidToken(data.errorMessage);
                					}
        					},
						error: function(xhr, ajaxOptions, thrownError){	
							$('#pleaseWaitDialog').modal('hide');
							alert("in error:"+thrownError);
        					}
    					});
      				}
    			},
    			no: {
    				label: "No",
      				className: "btn-default",
      				callback: function() {
					//$('#pleaseWaitDialog').modal('hide');
	       				//return false;
      				}
    			}     
            	}
        });
    //}
}


function createAccTemplate(){   
    //Getting current date
    //siddhesh
  var currentDate = new Date();
var dd = currentDate.getDate();
var mm = currentDate.getMonth()+1; //January is 0!
var yyyy = currentDate.getFullYear();

if(dd<10) {
    dd='0'+dd;
} 

if(mm<10) {
    mm='0'+mm;
} 

 currentDate = dd+'/'+mm+'/'+yyyy;
 mm=mm-1;
 var dateOfDefaultExpiry = new Date(yyyy,mm,dd);
     dateOfDefaultExpiry = new Date(new Date(dateOfDefaultExpiry).setMonth(dateOfDefaultExpiry.getMonth()+12));
     dd=dateOfDefaultExpiry.getDate()-1;
     mm = dateOfDefaultExpiry.getMonth()+1;
     yyyy=dateOfDefaultExpiry.getFullYear();
     if(dd<10) {
    dd='0'+dd;
} 

if(mm<10) {
    mm='0'+mm;
} 
     dateOfDefaultExpiry = dd+'/'+mm+'/'+yyyy;
 //end 
	var accStatusTemplate='';
	accStatusTemplate+='<div class="modal-dialog" id="id_accStatus">';
	accStatusTemplate+='<div class="modal-content"><div class="modal-header">';
	accStatusTemplate+='<button type="button" class="close" data-dismiss="modal" onClick="closeModal();" aria-hidden="true"></button>';					
	accStatusTemplate+='<h4 class="modal-title">Add Account Details</h4></div>';	
	accStatusTemplate+='<form class="form-horizontal" role="form" name="create_account"  id="addAccDetails">';								
	accStatusTemplate+='<div class="modal-body">';
	accStatusTemplate+='<div class="scroller">';
	accStatusTemplate+='<div class="form-body">';
		
	accStatusTemplate+='<div class="form-group">';
	accStatusTemplate+='<label class="col-md-6 control-label">Company Name<span style="color:red"> *</span></label>';
	accStatusTemplate+='<div class="col-md-6">';
	accStatusTemplate+='<input type="text" class="form-control" placeholder="Company Name" name="companyName" id="id_companyName"> ';
	accStatusTemplate+='</div>';
	accStatusTemplate+='</div>';
        
        accStatusTemplate+='<div class="form-group">';
	accStatusTemplate+='<label class="col-md-6 control-label">User Limit<span style="color:red"> *</span></label>';
	accStatusTemplate+='<div class="col-md-6">';
	accStatusTemplate+='<input type="text" value="25" class="form-control" placeholder="Minimum User Limit is 25" name="userLimit" id="id_userLimit"> ';
	accStatusTemplate+='</div>';
	accStatusTemplate+='</div>';
        
        //Siddhesh Pande
//        //Date picker for start date
//        accStatusTemplate+='<div class="form-group">';
//        accStatusTemplate+='<label class="col-md-6 control-label">Start Date<span style="color:red"> *</span></label>';
//        accStatusTemplate+= '<div class = "input-group date-picker input-daterange" data - date = "20/11/2012" data-date-format="dd/mm/yyyy">';
//	accStatusTemplate+= '<input type = "text" class="form-control" name = "Date" id="start_date" value="'+currentDate+'"  1="">';
//        accStatusTemplate += '</div>';
//        accStatusTemplate += '</div>';
//        //end of date picker
//
          accStatusTemplate += '<div class="form-group">';
          accStatusTemplate += '<label class="col-md-6 control-label">Start Date<span style="color:red"> *</span></label>';
         accStatusTemplate+='<div class="col-md-6">';
          accStatusTemplate += '<div class="input-group date date-picker" data - date = "20/11/2012" data-date-format="dd/mm/yyyy">';
                    accStatusTemplate += '<input type="text" class="form-control" id="start_date" readonly value="' + currentDate + '">';
                    accStatusTemplate += '<span class="input-group-btn">';
                    accStatusTemplate += '<button class="btn btn-default" type="button"><i class="fa fa-calendar"></i></button>';
                    accStatusTemplate += '</span>';
                    accStatusTemplate += '</div>';
                    accStatusTemplate += '</div>';
                    accStatusTemplate += '</div>';
                    
                  accStatusTemplate += '<div class="form-group">';
          accStatusTemplate += '<label class="col-md-6 control-label">Expiry date<span style="color:red"> *</span></label>';
         accStatusTemplate+='<div class="col-md-6">';
          accStatusTemplate += '<div class="input-group date date-picker" data - date = "20/11/2012" data-date-format="dd/mm/yyyy">';
                    accStatusTemplate += '<input type="text" class="form-control" id="expiry_date" readonly value="' + dateOfDefaultExpiry + '">';
                    accStatusTemplate += '<span class="input-group-btn">';
                    accStatusTemplate += '<button class="btn btn-default" type="button"><i class="fa fa-calendar"></i></button>';
                    accStatusTemplate += '</span>';
                    accStatusTemplate += '</div>';
                    accStatusTemplate += '</div>';
                    accStatusTemplate += '</div>'; 
        
//        //Date picker for expiry
//        accStatusTemplate+='<div class="form-group">';
//        accStatusTemplate+='<label class="col-md-6 control-label">Expiry date<span style="color:red"> *</span></label>';
//        accStatusTemplate+= '<div class = "input-group date-picker input-daterange" data - date = "20/11/2012" data-date-format="dd/mm/yyyy">';
//	accStatusTemplate+= '<input type = "text" class="form-control" name = "Date" id="expiry_date" value="'+dateOfDefaultExpiry+'"  1="">';
//        accStatusTemplate += '</div>';
//        accStatusTemplate += '</div>';
//        //end of date picker
        
        
		

	accStatusTemplate+='<div class="form-group">';
	accStatusTemplate+='<label class="col-md-6 control-label">Address<span style="color:red"> *</span></label>';
	accStatusTemplate+='<div class="col-md-6">';
	accStatusTemplate+='<input type="text" class="form-control" placeholder="Address" name="address1" id="id_address1"> ';
	accStatusTemplate+='</div>';
	accStatusTemplate+='</div>';

	accStatusTemplate+='<div class="form-group">';
	accStatusTemplate+='<label class="col-md-6 control-label">Region<span style="color:red"> *</span></label>';									
	accStatusTemplate+='<div class="col-md-6">';										
	accStatusTemplate+='<select name="region" id="select2_region" class="form-control select2">';
	accStatusTemplate+='<option value="0">--Select Region--</option>';										
	$.each( accountRegions, function ( i, data ) {
		accStatusTemplate+='<option value="'+data.id+'">'+data.region_name+'</option>';
		//$('#'+id.date+' td.details-control').trigger( 'click' );
	} );
	accStatusTemplate+='</select></div></div>';

	accStatusTemplate+='<div class="form-group">';
	accStatusTemplate+='<label class="col-md-6 control-label">City/Town<span style="color:red"> *</span></label>';
	accStatusTemplate+='<div class="col-md-6">';
	accStatusTemplate+='<input type="text" class="form-control" placeholder="City/Town" name="city" id="id_city"> ';
	accStatusTemplate+='</div>';
	accStatusTemplate+='</div>';

	accStatusTemplate+='<div class="form-group">';
	accStatusTemplate+='<label class="col-md-6 control-label">State<span style="color:red"> *</span></label>';
	accStatusTemplate+='<div class="col-md-6">';
	accStatusTemplate+='<input type="text" class="form-control" placeholder="State" name="state" id="id_state"> ';
	accStatusTemplate+='</div>';
	accStatusTemplate+='</div>';

	accStatusTemplate+='<div class="form-group">';
	accStatusTemplate+='<label class="col-md-6 control-label">Select Country<span style="color:red"> *</span></label>';									
	accStatusTemplate+='<div class="col-md-6">';										
	accStatusTemplate+='<select name="country" id="select2_sample4" class="form-control select2">';										
	accStatusTemplate+='<option value="select">--Select Country--</option>';										
	accStatusTemplate+='<option value="Afghanistan">Afghanistan</option>';										
	accStatusTemplate+='<option value="Albania">Albania</option>';
	accStatusTemplate+='<option value="Algeria">Algeria</option>';
	accStatusTemplate+='<option value="American Samoa">American Samoa</option>';
	accStatusTemplate+='<option value="Andorra">Andorra</option>';
	accStatusTemplate+='<option value="Angola">Angola</option>';
	accStatusTemplate+='<option value="Anguilla">Anguilla</option>';
	accStatusTemplate+='<option value="Antarctica">Antarctica</option>';
	accStatusTemplate+='<option value="Argentina">Argentina</option>';
	accStatusTemplate+='<option value="Armenia">Armenia</option>';
	accStatusTemplate+='<option value="Aruba">Aruba</option>';
	accStatusTemplate+='<option value="Australia">Australia</option>';
	accStatusTemplate+='<option value="Austria">Austria</option>';
	accStatusTemplate+='<option value="Azerbaijan">Azerbaijan</option>';
	accStatusTemplate+='<option value="Bahamas">Bahamas</option>';
	accStatusTemplate+='<option value="Bahrain">Bahrain</option>';
	accStatusTemplate+='<option value="Bangladesh">Bangladesh</option>';
	accStatusTemplate+='<option value="Barbados">Barbados</option>';
	accStatusTemplate+='<option value="Belarus">Belarus</option>';
	accStatusTemplate+='<option value="Belgium">Belgium</option>';
	accStatusTemplate+='<option value="Belize">Belize</option>';
	accStatusTemplate+='<option value="Benin">Benin</option>';
	accStatusTemplate+='<option value="Bermuda">Bermuda</option>';
	accStatusTemplate+='<option value="Bhutan">Bhutan</option>';                 
	accStatusTemplate+='<option value="Bolivia">Bolivia</option>';								
	accStatusTemplate+='<option value="Bosnia and Herzegowina">Bosnia and Herzegowina</option>';										
	accStatusTemplate+='<option value="Bouvet Island">Bouvet Island</option>';										
	accStatusTemplate+='<option value="Brazil">Brazil</option>';													
	accStatusTemplate+='<option value="British Indian Ocean Territory">British Indian Ocean Territory</option>';
	accStatusTemplate+='<option value="Brunei Darussalam">Brunei Darussalam</option>';
	accStatusTemplate+='<option value="Bulgaria">Bulgaria</option>';
	accStatusTemplate+='<option value="Burkina Faso">Burkina Faso</option>';												
	accStatusTemplate+='<option value="Burundi">Burundi</option>';
	accStatusTemplate+='<option value="Cambodia">Cambodia</option>';
	accStatusTemplate+='<option value="Cameroon">Cameroon</option>';
	accStatusTemplate+='<option value="Canada">Canada</option>';
	accStatusTemplate+='<option value="Cape Verde">Cape Verde</option>';
	accStatusTemplate+='<option value="Cayman Islands">Cayman Islands</option>';
	accStatusTemplate+='<option value="Central African Republic">Central African Republic</option>';
	accStatusTemplate+='<option value="Chad">Chad</option>';
	accStatusTemplate+='<option value="Chile">Chile</option>';
	accStatusTemplate+='<option value="China">China</option>';
	accStatusTemplate+='<option value="Christmas Island">Christmas Island</option>';
	accStatusTemplate+='<option value="Cocos (Keeling) Islands">Cocos (Keeling) Islands</option>';										
	accStatusTemplate+='<option value="Colombia">Colombia</option>';
	accStatusTemplate+='<option value="Comoros">Comoros</option>';
	accStatusTemplate+='<option value="Congo">Congo</option>';
	accStatusTemplate+='<option value="Congo, the Democratic Republic of the">Congo, the Democratic Republic of the</option>';
	accStatusTemplate+='<option value="Cook Islands">Cook Islands</option>';
	accStatusTemplate+='<option value="Costa Rica">Costa Rica</option>';
	accStatusTemplate+='<option value="Cote dIvoire">Cote dIvoire</option>';
	accStatusTemplate+='<option value="Croatia (Hrvatska)">Croatia (Hrvatska)</option>';
	accStatusTemplate+='<option value="Cuba">Cuba</option>';
	accStatusTemplate+='<option value="Cyprus">Cyprus</option>';
	accStatusTemplate+='<option value="Czech Republic">Czech Republic</option>';
	accStatusTemplate+='<option value="Denmark">Denmark</option>';
	accStatusTemplate+='<option value="Djibouti">Djibouti</option>';
	accStatusTemplate+='<option value="Dominica">Dominica</option>';
	accStatusTemplate+='<option value="Dominican Republic">Dominican Republic</option>';
	accStatusTemplate+='<option value="Ecuador">Ecuador</option>';
	accStatusTemplate+='<option value="Egypt">Egypt</option>';
	accStatusTemplate+='<option value="El Salvador">El Salvador</option>';												
	accStatusTemplate+='<option value="Equatorial Guinea">Equatorial Guinea</option>';												
	accStatusTemplate+='<option value="Eritrea">Eritrea</option>';												
	accStatusTemplate+='<option value="Estonia">Estonia</option>';												
	accStatusTemplate+='<option value="Ethiopia">Ethiopia</option>';												
	accStatusTemplate+='<option value="Falkland Islands (Malvinas)">Falkland Islands (Malvinas)</option>';												
	accStatusTemplate+='<option value="Faroe Islands">Faroe Islands</option>';												
	accStatusTemplate+='<option value="Fiji">Fiji</option>';											
	accStatusTemplate+='<option value="Finland">Finland</option>';
	accStatusTemplate+='<option value="France">France</option>';												
	accStatusTemplate+='<option value="French Guiana">French Guiana</option>';
	accStatusTemplate+='<option value="French Polynesia">French Polynesia</option>';
	accStatusTemplate+='<option value="French Southern Territories">French Southern Territories</option>';
	accStatusTemplate+='<option value="Gabon">Gabon</option>';
	accStatusTemplate+='<option value="Gambia">Gambia</option>';
	accStatusTemplate+=' <option value="Georgia">Georgia</option>';
	accStatusTemplate+='<option value="Germany">Germany</option>';
	accStatusTemplate+='<option value="Ghana">Ghana</option>';
	accStatusTemplate+='<option value="Gibraltar">Gibraltar</option>';
	accStatusTemplate+='<option value="Greece">Greece</option>';
	accStatusTemplate+='<option value="Greenland">Greenland</option>';
	accStatusTemplate+='<option value="Grenada">Grenada</option>';
	accStatusTemplate+='<option value="Guadeloupe">Guadeloupe</option>';
	accStatusTemplate+='<option value="Guam">Guam</option>';
	accStatusTemplate+='<option value="Guatemala">Guatemala</option>';
	accStatusTemplate+='<option value="Guinea">Guinea</option>';
	accStatusTemplate+='<option value="Guinea-Bissau">Guinea-Bissau</option>';
	accStatusTemplate+='<option value="Guyana">Guyana</option>';
	accStatusTemplate+='<option value="Haiti">Haiti</option>';
	accStatusTemplate+='<option value="Heard and Mc Donald Islands">Heard and Mc Donald Islands</option>';
	accStatusTemplate+='<option value="Holy See (Vatican City State)">Holy See (Vatican City State)</option>';
	accStatusTemplate+='<option value="Honduras">Honduras</option>';
	accStatusTemplate+='<option value="Hong Kong">Hong Kong</option>';
	accStatusTemplate+='<option value="Hungary">Hungary</option>';
	accStatusTemplate+='<option value="Iceland">Iceland</option>';
	accStatusTemplate+='<option value="India">India</option>';
	accStatusTemplate+='<option value="Indonesia">Indonesia</option>';
	accStatusTemplate+='<option value="Iran (Islamic Republic of)">Iran (Islamic Republic of)</option>';
	accStatusTemplate+='<option value="Iraq">Iraq</option>';
	accStatusTemplate+='<option value="Ireland">Ireland</option>';
	accStatusTemplate+='<option value="Israel">Israel</option>';
	accStatusTemplate+='<option value="Italy">Italy</option>';
	accStatusTemplate+='<option value="Jamaica">Jamaica</option>';
	accStatusTemplate+='<option value="Japan">Japan</option>';
	accStatusTemplate+='<option value="Jordan">Jordan</option>';
	accStatusTemplate+='<option value="Kazakhstan">Kazakhstan</option>';
	accStatusTemplate+='<option value="Kenya">Kenya</option>';
	accStatusTemplate+='<option value="Kiribati">Kiribati</option>';
	accStatusTemplate+='<option value="Korea, Democratic Peoples Republic of">Korea, Democratic Peoples 	Republic of</option>';
	accStatusTemplate+='<option value="Korea, Republic of">Korea, Republic of</option>';
	accStatusTemplate+='<option value="Kuwait">Kuwait</option>';
	accStatusTemplate+='<option value="Kyrgyzstan">Kyrgyzstan</option>';
	accStatusTemplate+='<option value="Lao Peoples Democratic Republic">Lao Peoples Democratic Republic</option>';
	accStatusTemplate+='<option value="Latvia">Latvia</option>';
	accStatusTemplate+='<option value="Lebanon">Lebanon</option>';
	accStatusTemplate+='<option value="Lesotho">Lesotho</option>';
	accStatusTemplate+='<option value="Liberia">Liberia</option>';
	accStatusTemplate+='<option value="Libyan Arab Jamahiriya<">Libyan Arab Jamahiriya</option>';
	accStatusTemplate+='<option value="Liechtenstein">Liechtenstein</option>';
	accStatusTemplate+='<option value="Lithuania">Lithuania</option>';
	accStatusTemplate+='<option value="Luxembourg">Luxembourg</option>';
	accStatusTemplate+='<option value="MO">Macau</option>';
	accStatusTemplate+='<option value="Macedonia, The Former Yugoslav Republic of">Macedonia, The Former Yugoslav Republic of</option>';
	accStatusTemplate+='<option value="Madagascar">Madagascar</option>';	   
	accStatusTemplate+='<option value="Malawi">Malawi</option>';	   
	accStatusTemplate+='<option value="Malaysia">Malaysia</option>';		   
	accStatusTemplate+='<option value="Maldives">Maldives</option>';		  
	accStatusTemplate+='<option value="Mali">Mali</option>';
	accStatusTemplate+='<option value="Malta">Malta</option>';
	accStatusTemplate+='<option value="Marshall Islands">Marshall Islands</option>';
	accStatusTemplate+='<option value="Martinique">Martinique</option>';
	accStatusTemplate+='<option value="Mauritania">Mauritania</option>';
	accStatusTemplate+='<option value="Mauritius">Mauritius</option>';
	accStatusTemplate+='<option value="Mayotte">Mayotte</option>';
	accStatusTemplate+='<option value="Mexico">Mexico</option>';
	accStatusTemplate+='<option value="Micronesia, Federated States of">Micronesia, Federated States of</option>';
	accStatusTemplate+='<option value=">Moldova, Republic of">Moldova, Republic of</option>';
	accStatusTemplate+='<option value="Monaco">Monaco</option>';
	accStatusTemplate+='<option value="Mongolia">Mongolia</option>';
	accStatusTemplate+='<option value="Montserrat">Montserrat</option>';
	accStatusTemplate+='<option value="Morocco">Morocco</option>';
	accStatusTemplate+='<option value="Mozambique">Mozambique</option>';
	accStatusTemplate+='<option value="Myanmar">Myanmar</option>';
	accStatusTemplate+='<option value="Namibia">Namibia</option>';
	accStatusTemplate+='<option value="Nauru">Nauru</option>';
	accStatusTemplate+='<option value="Nepal">Nepal</option>';
	accStatusTemplate+='<option value="Netherlands">Netherlands</option>';
	accStatusTemplate+='<option value="New Caledonia">New Caledonia</option>';
	accStatusTemplate+='<option value="New Zealand">New Zealand</option>';
	accStatusTemplate+='<option value="Nicaragua">Nicaragua</option>';
	accStatusTemplate+='<option value="Niger">Niger</option>';									
	accStatusTemplate+='<option value="Nigeria">Nigeria</option>';
	accStatusTemplate+='<option value="Niue">Niue</option>';
	accStatusTemplate+='<option value="Norfolk Island">Norfolk Island</option>';
	accStatusTemplate+='<option value="Northern Mariana Islands">Northern Mariana Islands</option>';
	accStatusTemplate+='<option value="Norway">Norway</option>';
	accStatusTemplate+='<option value="Oman">Oman</option>';
	accStatusTemplate+='<option value="Pakistan">Pakistan</option>';
	accStatusTemplate+='<option value="Palau">Palau</option>';
	accStatusTemplate+='<option value="Panama">Panama</option>';
	accStatusTemplate+='<option value="Papua New Guinea">Papua New Guinea</option>';
	accStatusTemplate+='<option value="Paraguay">Paraguay</option>';										
	accStatusTemplate+='<option value="Peru">Peru</option>';
	accStatusTemplate+='<option value="Philippines">Philippines</option>';
	accStatusTemplate+='<option value="Pitcairn">Pitcairn</option>';
	accStatusTemplate+='<option value="Poland">Poland</option>';
	accStatusTemplate+='<option value="Portugal">Portugal</option>';
	accStatusTemplate+='<option value="Puerto Rico">Puerto Rico</option>';
	accStatusTemplate+='<option value="Qatar">Qatar</option>';
	accStatusTemplate+='<option value="Reunion">Reunion</option>';
	accStatusTemplate+='<option value="Romania">Romania</option>';
	accStatusTemplate+='<option value="Russian Federation">Russian Federation</option>';
	accStatusTemplate+='<option value="Rwanda">Rwanda</option>';
	accStatusTemplate+='<option value="Saint Kitts and Nevis">Saint Kitts and Nevis</option>';
	accStatusTemplate+='<option value="Saint LUCIA">Saint LUCIA</option>';
	accStatusTemplate+='<option value="Saint Vincent and the Grenadines">Saint Vincent and the Grenadines</option>';
	accStatusTemplate+='<option value="Samoa">Samoa</option>';
	accStatusTemplate+='<option value="San Marino">San Marino</option>';
	accStatusTemplate+='<option value="Sao Tome and Principe">Sao Tome and Principe</option>';
	accStatusTemplate+='<option value="Saudi Arabia">Saudi Arabia</option>';
	accStatusTemplate+='<option value="Senegal">Senegal</option>';
	accStatusTemplate+='<option value="Seychelles">Seychelles</option>';
	accStatusTemplate+='<option value="Sierra Leone">Sierra Leone</option>';
	accStatusTemplate+='<option value="Singapore">Singapore</option>';
	accStatusTemplate+='<option value="Slovakia (Slovak Republic)">Slovakia (Slovak Republic)</option>';
	accStatusTemplate+='<option value="Slovenia">Slovenia</option>';
	accStatusTemplate+='<option value="Solomon Islands">Solomon Islands</option>';
	accStatusTemplate+='<option value="Somalia">Somalia</option>';
	accStatusTemplate+='<option value="South Africa">South Africa</option>';
	accStatusTemplate+='<option value="South Georgia and the South Sandwich Islands">South Georgia and the South Sandwich Islands</option>';
	accStatusTemplate+='<option value="Spain">Spain</option>';
	accStatusTemplate+='<option value="Sri Lanka">Sri Lanka</option>';
	accStatusTemplate+='<option value="St. Helena">St. Helena</option>';
	accStatusTemplate+='<option value="St. Pierre and Miquelon">St. Pierre and Miquelon</option>';
	accStatusTemplate+='<option value="Sudan">Sudan</option>';
	accStatusTemplate+='<option value="Suriname">Suriname</option>';
	accStatusTemplate+='<option value="Svalbard and Jan Mayen Islands">Svalbard and Jan Mayen Islands</option>';
	accStatusTemplate+='<option value="Swaziland">Swaziland</option>';
	accStatusTemplate+='<option value="Sweden">Sweden</option>';
	accStatusTemplate+='<option value="Switzerland">Switzerland</option>';
	accStatusTemplate+='<option value="Syrian Arab Republic">Syrian Arab Republic</option>';
	accStatusTemplate+='<option value="Taiwan, Province of China">Taiwan, Province of China</option>';
	accStatusTemplate+='<option value="TJ">Tajikistan</option>';
	accStatusTemplate+='<option value="Tajikistan">Tajikistan</option>';
	accStatusTemplate+='<option value="Tanzania, United Republic of">Tanzania, United Republic of</option>';
	accStatusTemplate+='<option value="Thailand">Thailand</option>';
	accStatusTemplate+='<option value="Togo">Togo</option>';
	accStatusTemplate+='<option value="Tokelau">Tokelau</option>';
	accStatusTemplate+='<option value="Tonga">Tonga</option>';
	accStatusTemplate+='<option value="Trinidad and Tobago">Trinidad and Tobago</option>';
	accStatusTemplate+='<option value="Tunisia">Tunisia</option>';
	accStatusTemplate+='<option value="Turkey">Turkey</option>';
	accStatusTemplate+='<option value="Turkmenistan">Turkmenistan</option>';
	accStatusTemplate+='<option value="Turks and Caicos Islands">Turks and Caicos Islands</option>';
	accStatusTemplate+='<option value="Tuvalu">Tuvalu</option>';
	accStatusTemplate+='<option value="Ukraine">Ukraine</option>';												
	accStatusTemplate+='<option value="United Arab Emirates">United Arab Emirates</option>';
	accStatusTemplate+='<option value="United Kingdom">United Kingdom</option>';
	accStatusTemplate+='<option value="United States">United States</option>';
	accStatusTemplate+='<option value="United States Minor Outlying Islands">United States Minor Outlying Islands</option>';
	accStatusTemplate+='<option value="Uruguay">Uruguay</option>';
	accStatusTemplate+='<option value="Uzbekistan">Uzbekistan</option>';
	accStatusTemplate+='<option value="Vanuatu">Vanuatu</option>';
	accStatusTemplate+='<option value="Venezuela">Venezuela</option>';
	accStatusTemplate+='<option value="Viet Nam">Viet Nam</option>';
	accStatusTemplate+='<option value="Virgin Islands (British)">Virgin Islands (British)</option>';
	accStatusTemplate+='<option value="Virgin Islands (U.S.)">Virgin Islands (U.S.)</option>';
	accStatusTemplate+='<option value="Wallis and Futuna Islands">Wallis and Futuna Islands</option>';
	accStatusTemplate+='<option value="Western Sahara">Western Sahara</option>';
	accStatusTemplate+='<option value="Yemen">Yemen</option>';
	accStatusTemplate+='<option value="Zambia">Zambia</option>';
	accStatusTemplate+='<option value="Zimbabwe">Zimbabwe</option>';
	accStatusTemplate+='</select></div></div>';	

	accStatusTemplate+='<div class="form-group">';
	accStatusTemplate+='<label class="col-md-6 control-label">Zip Code</label>';
	accStatusTemplate+='<div class="col-md-6">';
	accStatusTemplate+='<input type="text" class="form-control" placeholder="Zip Code" name="zipcode" id="id_zipCode"> ';
	accStatusTemplate+='</div>';
	accStatusTemplate+='</div>';

	accStatusTemplate+='<div class="form-group">';
	accStatusTemplate+='<label class="col-md-6 control-label">Company Phone Number<span style="color:red"> *</span></label>';
	accStatusTemplate+='<div class="col-md-6">';
	accStatusTemplate+='<input type="text" class="form-control" placeholder="company Phone Number" name="companyPhoneNumber1" id="id_comPnNo1"> ';
	accStatusTemplate+='</div>';
	accStatusTemplate+='</div>';

	accStatusTemplate+='<h4 class="form-section">Personal Info</h4>';

	accStatusTemplate+='<div class="form-group">';
	accStatusTemplate+='<label class="col-md-6 control-label">Email<span style="color:red"> *</span></label>';
	accStatusTemplate+='<div class="col-md-6">';
	accStatusTemplate+='<input type="text" class="form-control"  placeholder="Email" name="username" id="id_email"> ';
	accStatusTemplate+='</div>';
	accStatusTemplate+='</div>';

	accStatusTemplate+='<div class="form-group">';
	accStatusTemplate+='<label class="col-md-6 control-label">Password<span style="color:red"> *</span></label>';
	accStatusTemplate+='<div class="col-md-6">';
	accStatusTemplate+='<input class="form-control"  type="password" placeholder="Password" name="password" id="id_pwd"> ';
	accStatusTemplate+='</div>';
	accStatusTemplate+='</div>';

	accStatusTemplate+='<div class="form-group">';
	accStatusTemplate+='<label class="col-md-6 control-label">Re-type Your Password<span style="color:red"> *</span></label>';
	accStatusTemplate+='<div class="col-md-6">';
	accStatusTemplate+='<input class="form-control"  type="password"  placeholder="Re-type Your Password" name="" id="id_conpwd"> ';
	accStatusTemplate+='</div>';
	accStatusTemplate+='</div>';

	accStatusTemplate+='<div class="form-group">';
	accStatusTemplate+='<label class="col-md-6 control-label">Username<span style="color:red"> *</span></label>';
	accStatusTemplate+='<div class="col-md-6">';
	accStatusTemplate+='<input type="text" class="form-control"   placeholder="Username" name="username" id="id_uname"> ';
	accStatusTemplate+='</div>';
	accStatusTemplate+='</div>';

	accStatusTemplate+='<div class="form-group">';
	accStatusTemplate+='<label class="col-md-6 control-label">Designation<span style="color:red"> *</span></label>';
	accStatusTemplate+='<div class="col-md-6">';
	accStatusTemplate+='<input type="text" class="form-control" placeholder="Designation" name="username" id="id_designation"> ';
	accStatusTemplate+='</div>';
	accStatusTemplate+='</div>';

	accStatusTemplate+='<div class="form-group">';
	accStatusTemplate+='<label class="col-md-6 control-label">Mobile No.</label>';
	accStatusTemplate+='<div class="col-md-6">';
	accStatusTemplate+='<input type="text" class="form-control" placeholder="Mobile No." name="mobile_number" id="id_mobile_number"> ';
	accStatusTemplate+='</div>';
	accStatusTemplate+='</div>';

	accStatusTemplate+='<div class="form-group">';
	accStatusTemplate+='<label class="col-md-6 control-label">Gender<span style="color:red"> *</span></label>';
	accStatusTemplate+='<div class="col-md-6">';
	accStatusTemplate+='<div class="radio-list" id="id_gender_radiobtns">';
	accStatusTemplate+='<label class="radio-inline">';
	accStatusTemplate+='<input type="radio" name="id_gender" id="optionsRadios4" value="1" > Male </label>';
	accStatusTemplate+='<label class="radio-inline">';											
	accStatusTemplate+='<input type="radio" name="id_gender" id="optionsRadios5" value="0"> Female </label>';
	accStatusTemplate+='</div>';	
	accStatusTemplate+='</div>';
	accStatusTemplate+='</div>';

	accStatusTemplate+='</div></div></div></form>';

	
	accStatusTemplate+='<div class="modal-footer">';																																																
	accStatusTemplate+='<button type="button" class="btn btn-info" onClick="createAcoount();return false;">Save</button>';								
	accStatusTemplate+='<button type="button" class="btn btn-info" id="reset" onClick="document.create_account.reset();App.updateUniform();" class="btn btn-default">Reset</button>';
	accStatusTemplate+='<button type="button" data-dismiss="modal"  onClick="closeModal();" class="btn btn-default">Cancel</button>';																							
	accStatusTemplate+='</div>';

	accStatusTemplate+='</div>/div>';

	$('#responsive2').html(accStatusTemplate); 
	App.initUniform();
	App.fixContentHeight();
        $('.date-picker').datepicker({
				rtl: App.isRTL(),
				autoclose: true
		    	});
	App.callHandleScrollers();		 

}

function editAccTemplate(accId){
        $.ajax({
        type: "GET",
        url: fieldSenseURL + "/account/"+accId,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken,
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
		if(data.errorCode=='0000'){
		        var accDetails=data.result;
		        var userId = accDetails.user.id;
                        var currentDate = new Date();
                        var dd = currentDate.getDate();
                        var mm = currentDate.getMonth()+1; //January is 0!
                        var yyyy = currentDate.getFullYear();

                        if(dd<10) {
                            dd='0'+dd;
                        } 

                        if(mm<10) {
                            mm='0'+mm;
                        }  

                        currentDate = dd+'/'+mm+'/'+yyyy;
                        var sExpiredOn=accDetails.sExpiredOn;
                        sExpiredOn=sExpiredOn.split(" ")[0];
                        sExpiredOn=sExpiredOn.split("-");
                        sExpiredOn= sExpiredOn[2] + '/' + sExpiredOn[1]  + '/' +sExpiredOn[0] ;
                        var strStartDate=accDetails.strStartDate;
                        strStartDate=strStartDate.split(" ")[0];
                        strStartDate=strStartDate.split("-");
                        strStartDate= strStartDate[2] + '/' + strStartDate[1]  + '/' +strStartDate[0] ;
		        var status = accDetails.status;
		        var userLimit = accDetails.userLimit;
		        var companyName= accDetails.companyName;
		        var address1=accDetails.address1;
		        var city=accDetails.city;
		        var state=accDetails.state;
		        var country=accDetails.country;
		        var zipCode=accDetails.zipCode;
		        var companyPhoneNumber1=accDetails.companyPhoneNumber1;
		        var emailId=accDetails.emailAddress;
		        var userFname = accDetails.user.firstName;
		        var usrdesignation = accDetails.user.designation ;
		        var usrEmailId = accDetails.user.emailAddress;
		        var userMobile = accDetails.mobileNo;
		        var userGender = accDetails.user.gender;
		        var usrpassword = accDetails.user.password;
		        var role = accDetails.role;
			var regionId=accDetails.regionId;
                  
                        // added by jyoti 22-12-2016

                        
                $.ajax({                            
                    type: "GET",
                    url: fieldSenseURL + "/account/settings/values/"+accId,
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
                            var accountSettings=data.result;
                            var offlineEnable=accountSettings.allowOffline;
                            var allowOfflineAccount_State = accountSettings.allowOffline;
                            //ended by jyoti
                            var accStatusTemplate='';
                            accStatusTemplate+='<div class="modal-dialog" id="id_accStatus">';
                            accStatusTemplate+='<div class="modal-content"><div class="modal-header">';
                            accStatusTemplate+='<button type="button" class="close" onClick="closeModal();" data-dismiss="modal" aria-hidden="true"></button>';					
                            accStatusTemplate+='<h4 class="modal-title">Edit Account Details</h4></div>';										
                            accStatusTemplate+='<form class="form-horizontal" role="form" name="edit_account" id="edit-AccDetails">';								
                            accStatusTemplate+='<div class="modal-body" id="act">';
                            accStatusTemplate+='<div class="scroller">';	
                            accStatusTemplate+='<div class="form-body"><div class="form-group">';							
                            accStatusTemplate+='<label class="col-md-6 control-label">Status<span style="color:red"> *</span></label>';									       																		
                            if (status){
                                    accStatusTemplate+='<div class="col-md-6">';
                                    accStatusTemplate+='<div class="radio-list">';
                                    accStatusTemplate+='<label class="radio-inline"><input type="radio" name="optionsRadios3" id="optionsRadios1" value="1" checked> Active </label>';
                                    accStatusTemplate+='<label class="radio-inline"><input type="radio" name="optionsRadios3" id="optionsRadios2" value="0"> Inactive </label>';
                                    accStatusTemplate+='</div></div>';
                            }else{
                                    accStatusTemplate+='<div class="col-md-6">';
                                    accStatusTemplate+='<div class="radio-list">';
                                    accStatusTemplate+='<label class="radio-inline"><input type="radio" name="optionsRadios3" id="optionsRadios1" value="1"> Active </label>';
                                    accStatusTemplate+='<label class="radio-inline"><input type="radio" name="optionsRadios3" id="optionsRadios2" value="0" checked> Inactive </label>';       
                                    accStatusTemplate+='</div></div>';
                            }      
                            accStatusTemplate+='</div>';
                            // added by jyoti 22-12-2016
                            accStatusTemplate += '<div class="form-group">';
                            accStatusTemplate += '<label class="col-md-6 control-label">Allow Offline</label>';
                            accStatusTemplate += '<div class="col-md-6">';
                            accStatusTemplate += '<div class="checkbox-list">';
                            if (offlineEnable == 1) {
                                    accStatusTemplate += '<input type="checkbox" id="id_allowoffline_edit" checked>';
                            }else{
                                    accStatusTemplate += '<input type="checkbox" id="id_allowoffline_edit">';
                            }
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                             //ended by jyoti

                            accStatusTemplate+='<div class="form-group">';
                            accStatusTemplate+='<label class="col-md-6 control-label">Company Name<span style="color:red"> *</span></label>';
                            accStatusTemplate+='<div class="col-md-6">';
                            accStatusTemplate+='<input type="text" class="form-control" placeholder="Company Name" name="companyName" id="id_companyName" value="' + companyName + '"> ';
                            accStatusTemplate+='</div></div>';	

                            accStatusTemplate+='<div class="form-group">';
                            accStatusTemplate+='<label class="col-md-6 control-label">User Limit<span style="color:red"> *</span></label>';
                            accStatusTemplate+='<div class="col-md-6">';
                            accStatusTemplate+='<input type="text" class="form-control" placeholder="Minimum User Limit is 25" name="userLimit" id="id_userLimit" value="'+userLimit+'"> ';
                            accStatusTemplate+='</div>';
                            accStatusTemplate+='</div>';
                            //Siddhesh Pande



    //                         //Date picker for start date
    //                         accStatusTemplate+='<div class="form-group">';
    //                         accStatusTemplate+='<label class="col-md-6 control-label">Start Date<span style="color:red"> *</span></label>';
    //                         accStatusTemplate+= '<div class = "input-group date-picker input-daterange" data - date = "20/11/2012" data-date-format="dd/mm/yyyy">';
    //	                 accStatusTemplate+= '<input type = "text" class="form-control" name = "Date" id="start_date" value="'+strStartDate+'"  1="">';
    //                         accStatusTemplate += '</div>';
    //                         accStatusTemplate += '</div>';
    //                          //end of date picker
    //                            //Date picker expiry
    //                         accStatusTemplate+='<div class="form-group">';
    //                        accStatusTemplate+='<label class="col-md-6 control-label">Expire date<span style="color:red"> *</span></label>';
    //                       accStatusTemplate+= '<div class = "input-group date-picker input-daterange" data - date = "20/11/2012" data-date-format="dd/mm/yyyy">';
    //		     accStatusTemplate+= '<input type = "text" class="form-control" name = "Date" id="date_value" value="'+sExpiredOn+'" 1="">';
    //                        accStatusTemplate += '</div>';
    //                         accStatusTemplate += '</div>';
    //                        //end of date picker

                           //
                            accStatusTemplate += '<div class="form-group">';
                            accStatusTemplate += '<label class="col-md-6 control-label">Start Date<span style="color:red"> *</span></label>';
                            accStatusTemplate+='<div class="col-md-6">';
                            accStatusTemplate += '<div class="input-group date date-picker" data - date = "20/11/2012" data-date-format="dd/mm/yyyy">';
                            accStatusTemplate += '<input type="text" class="form-control" id="start_date" readonly value="' + strStartDate + '">';
                            accStatusTemplate += '<span class="input-group-btn">';
                            accStatusTemplate += '<button class="btn btn-default" type="button"><i class="fa fa-calendar"></i></button>';
                            accStatusTemplate += '</span>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';

                            accStatusTemplate += '<div class="form-group">';
                            accStatusTemplate += '<label class="col-md-6 control-label">Expiry date<span style="color:red"> *</span></label>';
                            accStatusTemplate+='<div class="col-md-6">';
                            accStatusTemplate += '<div class="input-group date date-picker" data - date = "20/11/2012" data-date-format="dd/mm/yyyy">';
                            accStatusTemplate += '<input type="text" class="form-control" id="date_value" readonly value="' + sExpiredOn + '">';
                            accStatusTemplate += '<span class="input-group-btn">';
                            accStatusTemplate += '<button class="btn btn-default" type="button"><i class="fa fa-calendar"></i></button>';
                            accStatusTemplate += '</span>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                            accStatusTemplate += '</div>';
                           //

                            accStatusTemplate+='<div class="form-group"><label class="col-md-6 control-label">Address<span style="color:red"> *</span></label>';								
                            accStatusTemplate+='<div class="col-md-6">';									
                            accStatusTemplate+='<textarea class="form-control" rows="3" placeholder="Address" name="address1" id="id_address1">' +address1 + '</textarea>';								
                            accStatusTemplate+='</div></div>';

                            accStatusTemplate+='<div class="form-group">';
                            accStatusTemplate+='<label class="col-md-6 control-label">Region<span style="color:red"> *</span></label>';									
                            accStatusTemplate+='<div class="col-md-6">';										
                            accStatusTemplate+='<select name="region" id="select2_region" class="form-control select2">';
                            accStatusTemplate+='<option value="0">--Select Region--</option>';										
                            $.each( accountRegions, function ( i, data ) {
                                    accStatusTemplate+='<option value="'+data.id+'">'+data.region_name+'</option>';
                                    //$('#'+id.date+' td.details-control').trigger( 'click' );
                            } );
                            accStatusTemplate+='</select></div></div>';

                            accStatusTemplate+='<div class="form-group"><label class="col-md-6 control-label">City/Town<span style="color:red"> *</span></label>';											
                            accStatusTemplate+='<div class="col-md-6">';																								
                            accStatusTemplate+='<input type="text" class="form-control" placeholder="City/Town" name="city" id="id_city" value="' + city + '">';																						
                            accStatusTemplate+='</div></div>';								
                            accStatusTemplate+='<div class="form-group">';										
                            accStatusTemplate+='<label class="col-md-6 control-label">State<span style="color:red"> *</span></label>';									
                            accStatusTemplate+='<div class="col-md-6">';											
                            accStatusTemplate+='<input type="text" class="form-control" placeholder="State" name="state" id="id_state" value="' + state + '">';										
                            accStatusTemplate+='</div></div>';
                            accStatusTemplate+='<div class="form-group">';
                            accStatusTemplate+='<label class="col-md-6 control-label">Select Country<span style="color:red"> *</span></label>';									
                            accStatusTemplate+='<div class="col-md-6">';										
                            accStatusTemplate+='<select name="country" id="select2_sample_modal_4" class="form-control select2">';										
                            accStatusTemplate+='<option value=""></option>';										
                            accStatusTemplate+='<option value="Afghanistan">Afghanistan</option>';										
                            accStatusTemplate+='<option value="Albania">Albania</option>';
                            accStatusTemplate+='<option value="Algeria">Algeria</option>';
                            accStatusTemplate+='<option value="American Samoa"allowOfflineAccount_State>American Samoa</option>';
                            accStatusTemplate+='<option value="Andorra">Andorra</option>';
                            accStatusTemplate+='<option value="Angola">Angola</option>';
                            accStatusTemplate+='<option value="Anguilla">Anguilla</option>';
                            accStatusTemplate+='<option value="Antarctica">Antarctica</option>';
                            accStatusTemplate+='<option value="Argentina">Argentina</option>';
                            accStatusTemplate+='<option value="Armenia">Armenia</option>';
                            accStatusTemplate+='<option value="Aruba">Aruba</option>';
                            accStatusTemplate+='<option value="Australia">Australia</option>';
                            accStatusTemplate+='<option value="Austria">Austria</option>';
                            accStatusTemplate+='<option value="Azerbaijan">Azerbaijan</option>';
                            accStatusTemplate+='<option value="Bahamas">Bahamas</option>';
                            accStatusTemplate+='<option value="Bahrain">Bahrain</option>';
                            accStatusTemplate+='<option value="Bangladesh">Bangladesh</option>';
                            accStatusTemplate+='<option value="Barbados">Barbados</option>';
                            accStatusTemplate+='<option value="Belarus">Belarus</option>';
                            accStatusTemplate+='<option value="Belgium">Belgium</option>';
                            accStatusTemplate+='<option value="Belize">Belize</option>';
                            accStatusTemplate+='<option value="Benin">Benin</option>';
                            accStatusTemplate+='<option value="Bermuda">Bermuda</option>';
                            accStatusTemplate+='<option value="Bhutan">Bhutan</option>';                   														
                            accStatusTemplate+='<option value="Bolivia">Bolivia</option>';								
                            accStatusTemplate+='<option value="Bosnia and Herzegowina">Bosnia and Herzegowina</option>';										
                            accStatusTemplate+='<option value="Bouvet Island">Bouvet Island</option>';										
                            accStatusTemplate+='<option value="Brazil">Brazil</option>';													
                            accStatusTemplate+='<option value="British Indian Ocean Territory">British Indian Ocean Territory</option>';
                            accStatusTemplate+='<option value="Brunei Darussalam">Brunei Darussalam</option>';
                            accStatusTemplate+='<option value="Bulgaria">Bulgaria</option>';
                            accStatusTemplate+='<option value="Burkina Faso">Burkina Faso</option>';												
                            accStatusTemplate+='<option value="Burundi">Burundi</option>';
                            accStatusTemplate+='<option value="Cambodia">Cambodia</option>';
                            accStatusTemplate+='<option value="Cameroon">Cameroon</option>';
                            accStatusTemplate+='<option value="Canada">Canada</option>';
                            accStatusTemplate+='<option value="Cape Verde">Cape Verde</option>';
                            accStatusTemplate+='<option value="Cayman Islands">Cayman Islands</option>';
                            accStatusTemplate+='<option value="Central African Republic">Central African Republic</option>';
                            accStatusTemplate+='<option value="Chad">Chad</option>';
                            accStatusTemplate+='<option value="Chile">Chile</option>';
                            accStatusTemplate+='<option value="China">China</option>';
                            accStatusTemplate+='<option value="Christmas Island">Christmas Island</option>';
                            accStatusTemplate+='<option value="Cocos (Keeling) Islands">Cocos (Keeling) Islands</option>';										
                            accStatusTemplate+='<option value="Colombia">Colombia</option>';
                            accStatusTemplate+='<option value="Comoros">Comoros</option>';
                            accStatusTemplate+='<option value="Congo">Congo</option>';
                            accStatusTemplate+='<option value="Congo, the Democratic Republic of the">Congo, the Democratic Republic of the</option>';
                            accStatusTemplate+='<option value="Cook Islands">Cook Islands</option>';
                            accStatusTemplate+='<option value="Costa Rica">Costa Rica</option>';
                            accStatusTemplate+='<option value="Cote dIvoire">Cote dIvoire</option>';
                            accStatusTemplate+='<option value="Croatia (Hrvatska)">Croatia (Hrvatska)</option>';
                            accStatusTemplate+='<option value="Cuba">Cuba</option>';
                            accStatusTemplate+='<option value="Cyprus">Cyprus</option>';
                            accStatusTemplate+='<option value="Czech Republic">Czech Republic</option>';
                            accStatusTemplate+='<option value="Denmark">Denmark</option>';
                            accStatusTemplate+='<option value="Djibouti">Djibouti</option>';
                            accStatusTemplate+='<option value="Dominica">Dominica</option>';
                            accStatusTemplate+='<option value="Dominican Republic">Dominican Republic</option>';
                            accStatusTemplate+='<option value="Ecuador">Ecuador</option>';
                            accStatusTemplate+='<option value="Egypt">Egypt</option>';
                            accStatusTemplate+='<option value="El Salvador">El Salvador</option>';												
                            accStatusTemplate+='<option value="Equatorial Guinea">Equatorial Guinea</option>';												
                            accStatusTemplate+='<option value="Eritrea">Eritrea</option>';												
                            accStatusTemplate+='<option value="Estonia">Estonia</option>';												
                            accStatusTemplate+='<option value="Ethiopia">Ethiopia</option>';												
                            accStatusTemplate+='<option value="Falkland Islands (Malvinas)">Falkland Islands (Malvinas)</option>';												
                            accStatusTemplate+='<option value="Faroe Islands">Faroe Islands</option>';												
                            accStatusTemplate+='<option value="Fiji">Fiji</option>';											
                            accStatusTemplate+='<option value="Finland">Finland</option>';
                            accStatusTemplate+='<option value="France">France</option>';												
                            accStatusTemplate+='<option value="French Guiana">French Guiana</option>';
                            accStatusTemplate+='<option value="French Polynesia">French Polynesia</option>';
                            accStatusTemplate+='<option value="French Southern Territories">French Southern Territories</option>';
                            accStatusTemplate+='<option value="Gabon">Gabon</option>';
                            accStatusTemplate+='<option value="Gambia">Gambia</option>';
                            accStatusTemplate+=' <option value="Georgia">Georgia</option>';
                            accStatusTemplate+='<option value="Germany">Germany</option>';
                            accStatusTemplate+='<option value="Ghana">Ghana</option>';
                            accStatusTemplate+='<option value="Gibraltar">Gibraltar</option>';
                            accStatusTemplate+='<option value="Greece">Greece</option>';
                            accStatusTemplate+='<option value="Greenland">Greenland</option>';
                            accStatusTemplate+='<option value="Grenada">Grenada</option>';
                            accStatusTemplate+='<option value="Guadeloupe">Guadeloupe</option>';
                            accStatusTemplate+='<option value="Guam">Guam</option>';
                            accStatusTemplate+='<option value="Guatemala">Guatemala</option>';
                            accStatusTemplate+='<option value="Guinea">Guinea</option>';
                            accStatusTemplate+='<option value="Guinea-Bissau">Guinea-Bissau</option>';
                            accStatusTemplate+='<option value="Guyana">Guyana</option>';
                            accStatusTemplate+='<option value="Haiti">Haiti</option>';
                            accStatusTemplate+='<option value="Heard and Mc Donald Islands">Heard and Mc Donald Islands</option>';
                            accStatusTemplate+='<option value="Holy See (Vatican City State)">Holy See (Vatican City State)</option>';
                            accStatusTemplate+='<option value="Honduras">Honduras</option>';
                            accStatusTemplate+='<option value="Hong Kong">Hong Kong</option>';
                            accStatusTemplate+='<option value="Hungary">Hungary</option>';
                            accStatusTemplate+='<option value="Iceland">Iceland</option>';
                            accStatusTemplate+='<option value="India">India</option>';
                            accStatusTemplate+='<option value="Indonesia">Indonesia</option>';
                            accStatusTemplate+='<option value="Iran (Islamic Republic of)">Iran (Islamic Republic of)</option>';
                            accStatusTemplate+='<option value="Iraq">Iraq</option>';
                            accStatusTemplate+='<option value="Ireland">Ireland</option>';
                            accStatusTemplate+='<option value="Israel">Israel</option>';
                            accStatusTemplate+='<option value="Italy">Italy</option>';
                            accStatusTemplate+='<option value="Jamaica">Jamaica</option>';
                            accStatusTemplate+='<option value="Japan">Japan</option>';
                            accStatusTemplate+='<option value="Jordan">Jordan</option>';
                            accStatusTemplate+='<option value="Kazakhstan">Kazakhstan</option>';
                            accStatusTemplate+='<option value="Kenya">Kenya</option>';
                            accStatusTemplate+='<option value="Kiribati">Kiribati</option>';
                            accStatusTemplate+='<option value="Korea, Democratic Peoples Republic of">Korea, Democratic Peoples 	Republic of</option>';
                            accStatusTemplate+='<option value="Korea, Republic of">Korea, Republic of</option>';
                            accStatusTemplate+='<option value="Kuwait">Kuwait</option>';
                            accStatusTemplate+='<option value="Kyrgyzstan">Kyrgyzstan</option>';
                            accStatusTemplate+='<option value="Lao Peoples Democratic Republic">Lao Peoples Democratic Republic</option>';
                            accStatusTemplate+='<option value="Latvia">Latvia</option>';
                            accStatusTemplate+='<option value="Lebanon">Lebanon</option>';
                            accStatusTemplate+='<option value="Lesotho">Lesotho</option>';
                            accStatusTemplate+='<option value="Liberia">Liberia</option>';
                            accStatusTemplate+='<option value="Libyan Arab Jamahiriya<">Libyan Arab Jamahiriya</option>';
                            accStatusTemplate+='<option value="Liechtenstein">Liechtenstein</option>';
                            accStatusTemplate+='<option value="Lithuania">Lithuania</option>';
                            accStatusTemplate+='<option value="Luxembourg">Luxembourg</option>';
                            accStatusTemplate+='<option value="MO">Macau</option>';
                            accStatusTemplate+='<option value="Macedonia, The Former Yugoslav Republic of">Macedonia, The Former Yugoslav Republic of</option>';
                            accStatusTemplate+='<option value="Madagascar">Madagascar</option>';	   
                            accStatusTemplate+='<option value="Malawi">Malawi</option>';	   
                            accStatusTemplate+='<option value="Malaysia">Malaysia</option>';		   
                            accStatusTemplate+='<option value="Maldives">Maldives</option>';		   													
                            accStatusTemplate+='<option value="Mali">Mali</option>';
                            accStatusTemplate+='<option value="Malta">Malta</option>';
                            accStatusTemplate+='<option value="Marshall Islands">Marshall Islands</option>';
                            accStatusTemplate+='<option value="Martinique">Martinique</option>';
                            accStatusTemplate+='<option value="Mauritania">Mauritania</option>';
                            accStatusTemplate+='<option value="Mauritius">Mauritius</option>';
                            accStatusTemplate+='<option value="Mayotte">Mayotte</option>';
                            accStatusTemplate+='<option value="Mexico">Mexico</option>';
                            accStatusTemplate+='<option value="Micronesia, Federated States of">Micronesia, Federated States of</option>';
                            accStatusTemplate+='<option value=">Moldova, Republic of">Moldova, Republic of</option>';
                            accStatusTemplate+='<option value="Monaco">Monaco</option>';
                            accStatusTemplate+='<option value="Mongolia">Mongolia</option>';
                            accStatusTemplate+='<option value="Montserrat">Montserrat</option>';
                            accStatusTemplate+='<option value="Morocco">Morocco</option>';
                            accStatusTemplate+='<option value="Mozambique">Mozambique</option>';
                            accStatusTemplate+='<option value="Myanmar">Myanmar</option>';
                            accStatusTemplate+='<option value="Namibia">Namibia</option>';
                            accStatusTemplate+='<option value="Nauru">Nauru</option>';
                            accStatusTemplate+='<option value="Nepal">Nepal</option>';
                            accStatusTemplate+='<option value="Netherlands">Netherlands</option>';
                            accStatusTemplate+='<option value="New Caledonia">New Caledonia</option>';
                            accStatusTemplate+='<option value="New Zealand">New Zealand</option>';
                            accStatusTemplate+='<option value="Nicaragua">Nicaragua</option>';
                            accStatusTemplate+='<option value="Niger">Niger</option>';									
                            accStatusTemplate+='<option value="Nigeria">Nigeria</option>';
                            accStatusTemplate+='<option value="Niue">Niue</option>';
                            accStatusTemplate+='<option value="Norfolk Island">Norfolk Island</option>';
                            accStatusTemplate+='<option value="Northern Mariana Islands">Northern Mariana Islands</option>';
                            accStatusTemplate+='<option value="Norway">Norway</option>';
                            accStatusTemplate+='<option value="Oman">Oman</option>';
                            accStatusTemplate+='<option value="Pakistan">Pakistan</option>';
                            accStatusTemplate+='<option value="Palau">Palau</option>';
                            accStatusTemplate+='<option value="Panama">Panama</option>';
                            accStatusTemplate+='<option value="Papua New Guinea">Papua New Guinea</option>';
                            accStatusTemplate+='<option value="Paraguay">Paraguay</option>';										
                            accStatusTemplate+='<option value="Peru">Peru</option>';
                            accStatusTemplate+='<option value="Philippines">Philippines</option>';
                            accStatusTemplate+='<option value="Pitcairn">Pitcairn</option>';
                            accStatusTemplate+='<option value="Poland">Poland</option>';
                            accStatusTemplate+='<option value="Portugal">Portugal</option>';
                            accStatusTemplate+='<option value="Puerto Rico">Puerto Rico</option>';
                            accStatusTemplate+='<option value="Qatar">Qatar</option>';
                            accStatusTemplate+='<option value="Reunion">Reunion</option>';
                            accStatusTemplate+='<option value="Romania">Romania</option>';
                            accStatusTemplate+='<option value="Russian Federation">Russian Federation</option>';
                            accStatusTemplate+='<option value="Rwanda">Rwanda</option>';
                            accStatusTemplate+='<option value="Saint Kitts and Nevis">Saint Kitts and Nevis</option>';
                            accStatusTemplate+='<option value="Saint LUCIA">Saint LUCIA</option>';
                            accStatusTemplate+='<option value="Saint Vincent and the Grenadines">Saint Vincent and the Grenadines</option>';
                            accStatusTemplate+='<option value="Samoa">Samoa</option>';
                            accStatusTemplate+='<option value="San Marino">San Marino</option>';
                            accStatusTemplate+='<option value="Sao Tome and Principe">Sao Tome and Principe</option>';
                            accStatusTemplate+='<option value="Saudi Arabia">Saudi Arabia</option>';
                            accStatusTemplate+='<option value="Senegal">Senegal</option>';
                            accStatusTemplate+='<option value="Seychelles">Seychelles</option>';
                            accStatusTemplate+='<option value="Sierra Leone">Sierra Leone</option>';
                            accStatusTemplate+='<option value="Singapore">Singapore</option>';
                            accStatusTemplate+='<option value="Slovakia (Slovak Republic)">Slovakia (Slovak Republic)</option>';
                            accStatusTemplate+='<option value="Slovenia">Slovenia</option>';
                            accStatusTemplate+='<option value="Solomon Islands">Solomon Islands</option>';
                            accStatusTemplate+='<option value="Somalia">Somalia</option>';
                            accStatusTemplate+='<option value="South Africa">South Africa</option>';
                            accStatusTemplate+='<option value="South Georgia and the South Sandwich Islands">South Georgia and the South Sandwich Islands</option>';
                            accStatusTemplate+='<option value="Spain">Spain</option>';
                            accStatusTemplate+='<option value="Sri Lanka">Sri Lanka</option>';
                            accStatusTemplate+='<option value="St. Helena">St. Helena</option>';
                            accStatusTemplate+='<option value="St. Pierre and Miquelon">St. Pierre and Miquelon</option>';
                            accStatusTemplate+='<option value="Sudan">Sudan</option>';
                            accStatusTemplate+='<option value="Suriname">Suriname</option>';
                            accStatusTemplate+='<option value="Svalbard and Jan Mayen Islands">Svalbard and Jan Mayen Islands</option>';
                            accStatusTemplate+='<option value="Swaziland">Swaziland</option>';
                            accStatusTemplate+='<option value="Sweden">Sweden</option>';
                            accStatusTemplate+='<option value="Switzerland">Switzerland</option>';
                            accStatusTemplate+='<option value="Syrian Arab Republic">Syrian Arab Republic</option>';
                            accStatusTemplate+='<option value="Taiwan, Province of China">Taiwan, Province of China</option>';
                            accStatusTemplate+='<option value="TJ">Tajikistan</option>';
                            accStatusTemplate+='<option value="Tajikistan">Tajikistan</option>';
                            accStatusTemplate+='<option value="Tanzania, United Republic of">Tanzania, United Republic of</option>';
                            accStatusTemplate+='<option value="Thailand">Thailand</option>';
                            accStatusTemplate+='<option value="Togo">Togo</option>';
                            accStatusTemplate+='<option value="Tokelau">Tokelau</option>';
                            accStatusTemplate+='<option value="Tonga">Tonga</option>';
                            accStatusTemplate+='<option value="Trinidad and Tobago">Trinidad and Tobago</option>';
                            accStatusTemplate+='<option value="Tunisia">Tunisia</option>';
                            accStatusTemplate+='<option value="Turkey">Turkey</option>';
                            accStatusTemplate+='<option value="Turkmenistan">Turkmenistan</option>';
                            accStatusTemplate+='<option value="Turks and Caicos Islands">Turks and Caicos Islands</option>';
                            accStatusTemplate+='<option value="Tuvalu">Tuvalu</option>';
                            accStatusTemplate+='<option value="Ukraine">Ukraine</option>';												
                            accStatusTemplate+='<option value="United Arab Emirates">United Arab Emirates</option>';
                            accStatusTemplate+='<option value="United Kingdom">United Kingdom</option>';
                            accStatusTemplate+='<option value="United States">United States</option>';
                            accStatusTemplate+='<option value="United States Minor Outlying Islands">United States Minor Outlying Islands</option>';
                            accStatusTemplate+='<option value="Uruguay">Uruguay</option>';
                            accStatusTemplate+='<option value="Uzbekistan">Uzbekistan</option>';
                            accStatusTemplate+='<option value="Vanuatu">Vanuatu</option>';
                            accStatusTemplate+='<option value="Venezuela">Venezuela</option>';
                            accStatusTemplate+='<option value="Viet Nam">Viet Nam</option>';
                            accStatusTemplate+='<option value="Virgin Islands (British)">Virgin Islands (British)</option>';
                            accStatusTemplate+='<option value="Virgin Islands (U.S.)">Virgin Islands (U.S.)</option>';
                            accStatusTemplate+='<option value="Wallis and Futuna Islands">Wallis and Futuna Islands</option>';
                            accStatusTemplate+='<option value="Western Sahara">Western Sahara</option>';
                            accStatusTemplate+='<option value="Yemen">Yemen</option>';
                            accStatusTemplate+='<option value="Zambia">Zambia</option>';
                            accStatusTemplate+='<option value="Zimbabwe">Zimbabwe</option>';
                            accStatusTemplate+='</select></div></div>';																
                            accStatusTemplate+='<div class="form-group">';
                            accStatusTemplate+='<label class="col-md-6 control-label">Zip Code</label>';
                            accStatusTemplate+='<div class="col-md-6">';										
                            accStatusTemplate+='<input type="text" class="form-control" placeholder="Zip Code" name="zipcode" id="id_zipCode"  value="' + zipCode + '"></div></div>';
                            accStatusTemplate+='<div class="form-group">';
                            accStatusTemplate+='<label class="col-md-6 control-label">Company Phone Number</label>';
                            accStatusTemplate+='<div class="col-md-6">';
                            accStatusTemplate+='<input type="text" class="form-control" placeholder="Company Phone Number" name="companyPhoneNumber1" id="id_comPnNo1"  value="' + companyPhoneNumber1 + '">';											
                            accStatusTemplate+='</div></div>';															
                            /*accStatusTemplate+='<h4 class="form-section">Personal Info</h4>';								
                            accStatusTemplate+='<div class="form-group">';
                            accStatusTemplate+='<label class="col-md-6 control-label">Email<span style="color:red"> *</span></label>';									
                            accStatusTemplate+='<div class="col-md-6">';										
                            accStatusTemplate+='<input type="text" class="form-control" placeholder="Email" name="username" id="id_email"  value="' + usrEmailId + '"disabled>';										
                            accStatusTemplate+='</div></div>';												
                            accStatusTemplate+='<div class="form-group">';																							
                            accStatusTemplate+='<label class="col-md-6 control-label">Password<span style="color:red"> *</span></label>';										
                            accStatusTemplate+='<div class="col-md-6">';
                            accStatusTemplate+='<div class="checkbox-list">	<input type="password" class="form-control" placeholder="Password" name="password" id="id_pwd"  value="' + usrpassword + '">';									
                            accStatusTemplate+='</div></div></div>';
                            accStatusTemplate+='<div class="form-group">';
                            accStatusTemplate+='<label class="col-md-6 control-label">Re-type Your Password<span style="color:red"> *</span></label>';
                            accStatusTemplate+='<div class="col-md-6">';
                            accStatusTemplate+='<input type="password" class="form-control" placeholder="Re-type Your Password" name="" id="id_conpwd"  value="' + usrpassword + '"></div></div>';											
                            accStatusTemplate+='<div class="form-group">';																								
                            accStatusTemplate+='<label class="col-md-6 control-label">Username<span style="color:red"> *</span></label>';										
                            accStatusTemplate+='<div class="col-md-6"><div class="checkbox-list">';									
                            accStatusTemplate+='<input type="text" class="form-control" placeholder="Username" name="username" id="id_uname"  value="' + userFname + '">';										
                            accStatusTemplate+='</div></div></div>';									
                            accStatusTemplate+='<div class="form-group">';											
                            accStatusTemplate+='<label class="col-md-6 control-label">Designation<span style="color:red"> *</span></label>';												
                            accStatusTemplate+='<div class="col-md-6">';											
                            accStatusTemplate+='<input type="text" class="form-control" placeholder="Designation" name="username" id="id_designation"  value="' + usrdesignation + '">';																								
                            accStatusTemplate+='</div></div>';										
                            accStatusTemplate+='<div class="form-group">';									
                            accStatusTemplate+='<label class="col-md-6 control-label">Gender<span style="color:red"> *</span></label>';
                            if (userGender ==1) {
                                    accStatusTemplate+='<div class="col-md-6"><div class="radio-list" name="gender_radiobtns" id="id_gender_radiobtns" >';
                                    accStatusTemplate+='<label class="radio-inline">';
                                    accStatusTemplate+='<input type="radio" name="optionsRadios4" id="optionsRadios3" value="1" checked> Male </label>';											
                                    accStatusTemplate+='<label class="radio-inline">';
                                    accStatusTemplate+='<input type="radio" name="optionsRadios4" id="optionsRadios4" value="0"> Female </label>';
                                    accStatusTemplate+='</div>';	
                            }else{
                                    accStatusTemplate+='<div class="col-md-6"><div class="radio-list" name="gender_radiobtns" id="id_gender_radiobtns" >';	
                                    accStatusTemplate+='<label class="radio-inline">';
                                    accStatusTemplate+='<input type="radio" name="optionsRadios4" id="optionsRadios3" value="1" > Male </label>';												
                                    accStatusTemplate+='<label class="radio-inline">';
                                    accStatusTemplate+='<input type="radio" name="optionsRadios4" id="optionsRadios4" value="0" checked> Female </label>';
                                    accStatusTemplate+='</div>';
                            }
                            accStatusTemplate+='</div></div>';*/
                            accStatusTemplate+='</div></div></div></form>';	
                            accStatusTemplate+='<div class="modal-footer">';																																																
                            accStatusTemplate+='<button type="button" class="btn btn-info" onclick="editAccDetails(\''+accId+ '\',\'' + userId + '\',\'' + allowOfflineAccount_State + '\')">Save</button>';
                            accStatusTemplate+='<button type="button" class="btn btn-info" id="reset" onClick="document.edit_account.reset();$(\'#select2_region\').val(\''+regionId+'\');$(\'#select2_sample_modal_4\').val(\''+country+'\');App.updateUniform();" >Reset</button>';								
                            accStatusTemplate+='<button type="button" data-dismiss="modal" onClick="closeModal();" class="btn btn-default">Cancel</button>';																							
                            accStatusTemplate+='</div></div></div>';
                            var value=$('#start_date').val();
                            var value1=$('#date_value').val();                                        
                            $('#responsive2').html(accStatusTemplate);   
                            $("#select2_sample_modal_4").val(country);
                            $("#select2_region").val(regionId);
                            App.initUniform();
                            App.callHandleScrollers();
                            App.fixContentHeight();  
                                $('.date-picker').datepicker({
                                    rtl: App.isRTL(),
                                    autoclose: true
                                });
                            jQuery('.tooltips').tooltip();
                        }                            
                    }                
                });     // end of second ajax call                
            }else{
                FieldSenseInvalidToken(data.errorMessage);
            }    
        }    
    });
}

        
function createAcoount(){
    //Editing for start date feature.
    var startDate=$("#start_date").val();
     var expiredOn=$("#expiry_date").val();
  var sStartDate=dateConverter(startDate);
  var sExpiredOn=dateConverter(expiredOn);
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
    
    var companyName=document.getElementById("id_companyName").value.trim();
    if(companyName.length==0){
        fieldSenseTosterError("Company Name can't be empty .",true);
        return false;
    }
    var userLimit=document.getElementById("id_userLimit").value.trim();
    if(isPostiveInteger(userLimit)==false ){
	fieldSenseTosterError("Please Enter Valid User Limit .",true);
        return false;
    }
    if(userLimit<25 ){
	fieldSenseTosterError("Minimum User Limit should be 25.",true);
        return false;
    }
 if(startDate==="" || startDate===" " || startDate===0)
    {
        fieldSenseTosterError("Start Date should not be blank",true);
        return false;
    }
    if(expiredOn==="" || expiredOn===" " || expiredOn===0)
    {
        fieldSenseTosterError(" Expiry Date should not be blank",true);
        return false;
    }
    if(sStartDate>sExpiredOn)
    {
       fieldSenseTosterError("Start date should not be greater than Expiry date",true);
        return false; 
    }
    var address1=document.getElementById("id_address1").value.trim();
    if(address1.length==0){
        fieldSenseTosterError("Address can't be empty .",true);
        return false;
    }

    var regionId=document.getElementById("select2_region").value.trim();
    if(regionId==0){
        fieldSenseTosterError("Please Select Region .",true);
        return false;
    }

    var city=document.getElementById("id_city").value.trim();
    if(city.length==0){
        fieldSenseTosterError("City can't be empty .",true);
        return false;
    }
    var state=document.getElementById("id_state").value.trim();
    if(state.length==0){
        fieldSenseTosterError("State can't be empty .",true);
        return false;
    }
    var indexOfCountry = document.getElementById("select2_sample4");
    var country =indexOfCountry.options[indexOfCountry.selectedIndex].text;
    if(country.length==0 || country=="--Select Country--"){
        fieldSenseTosterError("Country should be selected .",true);
        return false;
    }
    var zipCode=document.getElementById("id_zipCode").value.trim();
    var phNo1=document.getElementById("id_comPnNo1").value.trim();
    if(phNo1.length==0){
        fieldSenseTosterError("phone number can't be empty .",true);
        return false;
    }
    var isphNo1 = /^\d+$/.test(phNo1);
    if(!isphNo1){
        fieldSenseTosterError("Invalid phone number.",true);
        return false;
    }
    var emailId=document.getElementById("id_email").value.trim();
    if(emailId.length==0){
        fieldSenseTosterError("Email can't be empty .",true);
        return false;
    }
    var emailPattern = /^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;
    if(!emailPattern.test(emailId)){
        fieldSenseTosterError("Invalid email address .",true);
        return false;
    }
    var password=document.getElementById("id_pwd").value;
    if(password.length==0){
        fieldSenseTosterError("Password can't be empty .",true);
        return false;
    }
    if(password.length < 8 || password.length > 20){
        fieldSenseTosterError("Password length should be 8 to 20 characters .",true);
        return false;
    }
    var confirmPassword=document.getElementById("id_conpwd").value.trim();
    if(confirmPassword.length==0){
        fieldSenseTosterError("Re-type Your Password can't be empty .",true);
        return false;
    }
    if(password!=confirmPassword){
        fieldSenseTosterError("Confirm password does not match with password .",true);
        return false;
    }
    var userName=document.getElementById("id_uname").value.trim();
    if(userName.length==0){
        fieldSenseTosterError("User name can't be empty .",true);
        return false;
    }
    var designation=document.getElementById("id_designation").value.trim();
    if(designation.length==0){
        fieldSenseTosterError("User designation can't be empty .",true);
        return false;
    }
    var mobile=document.getElementById("id_mobile_number").value.trim();
    if(mobile.trim().length != 0) {
    	if(!(/^\d+$/.test(mobile))) {
        	fieldSenseTosterError("Invalid Mobile number.", true);
        	return false;
   	}else if (mobile.trim().length > 20) {
        	fieldSenseTosterError("Mobile number should not be more than 20 digits", true);
        	return false;
    	}
    }
    var gender=$( "input:radio[name=id_gender]:checked" ).val();
    if(gender==undefined){
        fieldSenseTosterError("Please select the gender.",true);
        return false;
    }
    var userObject ={
        "firstName":userName,
        "designation":designation,
        "emailAddress":emailId,
        "password":password,
        "gender":gender,
	"mobileNo":mobile,
        "role":1,
        "active":1,
        "accountContactType":1
    }
    var accountObject={
        "companyName":companyName,
	"userLimit":userLimit,
	"regionId":regionId,
        "address1":address1,
        "city":city,
        "state":state,
        "country":country,
        "zipCode":zipCode,
        "companyPhoneNumber1":parseFloat(phNo1),
        "emailAddress":emailId,
        "strStartDate":sStartDate,
        "sExpiredOn":sExpiredOn,
        "user":userObject
    }
    var jsonData = JSON.stringify( accountObject );
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "POST",
        url: fieldSenseURL+"/account",
        contentType: "application/json; charset=utf-8",
        headers:{
            "userToken": userToken
        },
        crossDomain: false,
        data:jsonData,
        cache: false,
        dataType: 'json',
        asyn:false,
        success: function(data) {
            if (data.errorCode=='0000') {
                fieldSenseTosterSuccess(data.errorMessage, true);
		$("#responsive2").modal('hide');
 		clearFilters();
            }else{
                fieldSenseTosterError(data.errorMessage,true);
		$('#pleaseWaitDialog').modal('hide');
            }
        },
        error:function(xhr, status, error){
            var err = eval("(" + xhr.responseText + ")");
            var message=err.fieldErrors[0].message;
            fieldSenseTosterError(message,true);
	    $('#pleaseWaitDialog').modal('hide');
        }
    });
}

function editAccDetails(id,userId,offlineAccountState)
{
     var expiredOn=$("#date_value").val();
     var startDate=$("#start_date").val();
     var sExpireDate=dateConverter(expiredOn)
     var strStartDate=dateConverter(startDate);
//     var dateSplit = expiredOn.split("/");
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
//    var sExpireDate = toDate.getFullYear() + "-" + month + "-" + date + " " + hours + ":" + minutes + ':59';
    
    var accStatus=$('input[name=optionsRadios3]:checked', '#edit-AccDetails').val();
    var companyName=document.getElementById("id_companyName").value.trim();
    if(companyName.length==0){
        fieldSenseTosterError("Company Name can't be empty .",true);
        return false;
    }

    var userLimit=document.getElementById("id_userLimit").value.trim();
    if(isPostiveInteger(userLimit)==false ){
	fieldSenseTosterError("Please Enter Valid User Limit .",true);
        return false;
    }
    if(userLimit<25 ){
	fieldSenseTosterError("Minimum User Limit should be 25.",true);
        return false;
    }
    if(startDate==="" || startDate===" " || startDate===0)
    {
        fieldSenseTosterError("Start Date should not be blank",true);
        return false;
    }
    if(expiredOn==="" || expiredOn===" " || expiredOn===0)
    {
        fieldSenseTosterError(" Expiry Date should not be blank",true);
        return false;
    }
     
    if(strStartDate>sExpireDate)
    {
       fieldSenseTosterError("Start date should not be greater than Expiry date",true);
        return false; 
    }
    var address1=document.getElementById("id_address1").value.trim();
    if(address1.length==0){
        fieldSenseTosterError("Address can't be empty .",true);
        return false;
    }
    var regionId=document.getElementById("select2_region").value.trim();
    if(regionId==0){
        fieldSenseTosterError("Please Select Region .",true);
        return false;
    }
    var city=document.getElementById("id_city").value.trim();
    if(city.length==0){
        fieldSenseTosterError("City can't be empty .",true);
        return false;
    }
    var state=document.getElementById("id_state").value.trim();
    if(state.length==0){
        fieldSenseTosterError("State can't be empty .",true);
        return false;
    }
    var indexOfCountry = document.getElementById("select2_sample_modal_4");
    var country =indexOfCountry.options[indexOfCountry.selectedIndex].text;
    if(country.length==0){
        fieldSenseTosterError("Country can't be empty .",true);
        return false;
    }
    var zipCode=document.getElementById("id_zipCode").value.trim();
    var phNo1=document.getElementById("id_comPnNo1").value.trim();
    if(phNo1.length==0){
        fieldSenseTosterError("phone number can't be empty .",true);
        return false;
    }
    var isphNo1 = /^\d+$/.test(phNo1);
    if(!isphNo1){
        fieldSenseTosterError("Invalid phone number.",true);
        return false;
    }
    
    // added by jyoti 21-12-2016
    var allowOffline_edit = 1;
    var valueAllowOffline_edit = document.getElementById("id_allowoffline_edit").checked;
    if(valueAllowOffline_edit==false){        
	allowOffline_edit=0;        
    }
    // ended by jyoti
    var accountObject={
        "id":id,
	"userLimit":userLimit,
	"regionId":regionId,
        "status":accStatus,
        "companyName":companyName,
        "address1":address1,
        "city":city,
        "state":state,
        "country":country,
        "zipCode":zipCode,
        "companyPhoneNumber1":parseFloat(phNo1),
        "sExpiredOn":sExpireDate,
        "strStartDate":strStartDate
       // "user":userObject
    }
    var jsonData = JSON.stringify( accountObject );
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "PUT",
        url: fieldSenseURL + "/account/"+id,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken,
        },
        crossDomain: false,
        cache: false,
        data:jsonData,
        dataType: 'json',
        success: function (data) {
                if (data.errorCode == '0000') {             
                    fieldSenseTosterSuccess(data.errorMessage, true);
		    var table = $('#sample_5').DataTable();
		    table.draw( false );
                    $('#responsive2').modal('hide');
                } else {
                    $('#pleaseWaitDialog').modal('hide');
                    fieldSenseTosterError(data.errorMessage, true);
                    //$('#responsive2').modal('hide');
                    FieldSenseInvalidToken(data.errorMessage);
                }
    // added by jyoti 22-12-2016
            if(offlineAccountState !== allowOffline_edit){
            
                var accountSettingsObject={  
                    "id":id,
                    "allowOffline":allowOffline_edit
                }

                var jsonData = JSON.stringify( accountSettingsObject );
                $.ajax({
                        type: "PUT",
                        url: fieldSenseURL+"/account/settings/values/"+id,
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
    
    
}


function closeModal(){
	$(".modal-backdrop").remove();
}

function isPostiveInteger(str) {
    return /^\d+$/.test(str);;
}

function dateConverter(dateBasicformat){
      var dateSplit = dateBasicformat.split("/");
    var date = dateSplit[0];
    var month = dateSplit[1];
    var year = dateSplit[2];
    var toDate = convertLocalDateToServerDate(year,month - 1,date, 23, 59);
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

function demoPage(){
     window.location='demo.html';
}


