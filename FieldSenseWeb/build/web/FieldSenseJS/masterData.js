
var intervalMasterData = window.setInterval(function () {
    var userToken2 = fieldSenseGetCookie("userToken");
    try {
        if (userToken2.toString() == "undefined") {
            window.location.href = "login.html";
        } else if (loginUserId != 0) {    // modified by manohar
             if (role == 1 || role==6 || role==3 || role==8) {       // role : 0 -super user, 1 -admin, 2 -account person, 5 -on-field user 
                loggedinUserImageData();
                loggedinUserData();
                //Added By Jyoti, 22-03-2017
                var loc= window.location;
            	var pathName = loc.pathname.substring(loc.pathname.lastIndexOf('/') + 1).trim();
 	    	if (pathName=="masterDataImportPurposeCategory.html" || pathName=="masterDataImportExpenseCategory.html" ){
                    downloadSampleUsersFile();
            	}else if(pathName=="masterDataImportTerritoryCategory.html" || pathName=="masterDataImportIndustryCategory.html"){
                    downloadSampleUsersFile();
                }
                //Ended By Jyoti, 22-03-2017
            }else if(role==0){
                window.location.href = "stats.html";
            }else {
                window.location.href = "dashboard.html";
            }
            window.clearTimeout(intervalMasterData);
        }
    }
    catch (err) {
        window.location.href = "login.html";
    }
}, 1000);

//Added By Jyoti, 22-03-2017
function downloadSampleUsersFile() {
    var url = imageURLPath + "purposeCategory.csv";
    var temp = '<a href="' + url + '" style="margin-right: 775px;" download>Click here to download sample file</a>';
    $('#id_masterPurposeTemplate').html(temp);
     url = imageURLPath + "expenseCategory.csv";
    var temp1 = '<a href="' + url + '" style="margin-right: 775px;" download>Click here to download sample file</a>';
    $('#id_masterExpenseTemplate').html(temp1);
     url = imageURLPath + "territoryCategory.csv";
    var temp2 = '<a href="' + url + '" style="margin-right: 775px;" download>Click here to download sample file</a>';
    $('#id_masterTerritoryTemplate').html(temp2);
     url = imageURLPath + "industryCategory.csv";
    var temp3 = '<a href="' + url + '" style="margin-right: 775px;" download>Click here to download sample file</a>';
    $('#id_masterIndustryTemplate').html(temp3);
}

//Added By Jyoti, 28-02-2017
function visitImportPurposeCategory(){
        window.location='masterDataImportPurposeCategory.html';
}

function showPurposeTemplate() {

    	$('#id_purposeactive').addClass("active");
        $('#id_ListCategoryactive').removeClass("active");
    	$('#id_expenseCategoryactive').removeClass("active");
    	$('#id_industryCategoryactive').removeClass("active");
    	$('#id_territoryCategoryactive').removeClass("active");
    	$('.cls_activeForPurpose').attr('id', 'rep');
    	$('#id_attachPurpose').html('');
    	var showPurposeTemplate = '';
    	showPurposeTemplate += '<div class="page-content">';
    	showPurposeTemplate += '<div class="row" style="margin-top:15px;">';
    	showPurposeTemplate += '<div class="col-md-12">';
    	showPurposeTemplate += '</div>';
    	showPurposeTemplate += '</div>';
    	showPurposeTemplate += '<div class="row">';
    	showPurposeTemplate += '<div class="col-md-12">';
    	showPurposeTemplate += '<div class="portlet-body">';
        //Added By Jyoti, 28-02-2017
        showPurposeTemplate += '<button type="button" class="btn btn-info fr" style="float:left; margin-left:750px;" onclick="visitImportPurposeCategory()" ><i class="fa fa-download"></i> Import Purpose Category</button> ';
    	//Ended By Jyoti, 28-02-2017
    	showPurposeTemplate += '<button type="button" class="btn btn-info" style="float:right;" data-toggle="modal" href="#responsive" onclick="addPurposeTemplate()"><i class="fa fa-pencil"></i> Add Purpose</button>';
    	showPurposeTemplate += '<span id="id_showPurpose"></span>';
    	showPurposeTemplate += '</div>';
    	showPurposeTemplate += '</div>';
    	showPurposeTemplate += '</div>';
    	showPurposeTemplate += '<span id="id_addPurposeTemplate"></span>';
    	showPurposeTemplate += '<div id="responsive4" class="modal fade cls_editPurposeTemplate" tabindex="-1" aria-hidden="true"></div>';
    	showPurposeTemplate += '</div>';
    	$('#id_attachPurpose').html(showPurposeTemplate);
	var showPurposeListHtml = '';
       	showPurposeListHtml += '<table class="table table-striped table-bordered table-hover" id="sample_5">';
        showPurposeListHtml += '<thead>';
        showPurposeListHtml += '<tr>';
        showPurposeListHtml += '<th>';
        showPurposeListHtml += 'Purpose';
        showPurposeListHtml += '</th>';
        showPurposeListHtml += '<th>';
        showPurposeListHtml += 'Active';
        showPurposeListHtml += '</th>';
        showPurposeListHtml += '<th>';
        showPurposeListHtml += '</th>								';
        showPurposeListHtml += '</tr>';
        showPurposeListHtml += '</thead>';
        showPurposeListHtml += '<tbody></tbody></table>';
	$('#id_showPurpose').html(showPurposeListHtml);
	var start=0;
	var length=20;
	myTable=$("#sample_5").DataTable();
	if(myTable != null)myTable.destroy();
    	//$('#pleaseWaitDialog').modal('show');
	var inboxtable = $('#sample_5').dataTable({

                "bServerSide": true,
                "bDestroy": true,
                "ajax": {
   		 	"url": fieldSenseURL + "/activityPurpose/purpose/offset/",
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
                            "aTargets": [1,2] 
			                             
                	}
		],
		"aoColumns": [ // ahmed : here we populate the data in datatable that is returned using Ajax
			      { "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						return full.purpose;
					}
				},
				{ "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						var isActive = full.isActive;                 
                    				if (isActive == false) {
                        				return '<input type="checkbox" disabled >';
                    				} else if (isActive == true) {
                        				return '<input type="checkbox" checked disabled >';
                    				}
					}
				},
				{ "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
                                    // Added by jyoti 27-june-2017
                                                var purposeCategory =full.purpose;
                                                if(purposeCategory != "Unknown"){
                       					return '<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Edit" data-toggle="modal" href="#responsive4" onclick="editPurposeTemplate(\'' + full.id + '\')"><i class="fa fa-edit"></i></button><button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Delete" onclick="deletePurpose(\'' + full.id + '\',\'' + full.purpose + '\')"><i class="fa fa-trash-o"></i></button>';
                    				}else{
							return '';
						}
                                    // Ended by jyoti
                                    // Commented by jyoti 
//						return '<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Edit" data-toggle="modal" href="#responsive4" onclick="editPurposeTemplate(\'' + full.id + '\')"><i class="fa fa-edit"></i></button><button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Delete" onclick="deletePurpose(\'' + full.id + '\',\'' + full.purpose + '\')"><i class="fa fa-trash-o"></i></button>';
					}
				}
		],
		"aaSorting": [],
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



function addPurposeTemplate() {
    $('#id_addPurposeTemplate').html('');
    var addPurpose = '';
    addPurpose += '<div id="responsive" class="modal fade addPurposeTemplateClose" tabindex="-1" aria-hidden="true">';
    addPurpose += '<div class="modal-dialog modal-wide">';
    addPurpose += '<div class="modal-content">';
    addPurpose += '<div class="modal-header">';
    addPurpose += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
    addPurpose += '<h4 class="modal-title">Add Purpose Details</h4>';
    addPurpose += '</div>';
    addPurpose += '<div class="modal-body">';
    addPurpose += '<form class="form-horizontal" role="form">';
    addPurpose += '<div class="form-body">';
    addPurpose += '<div class="form-group">';
    addPurpose += '<label class="col-md-6 control-label">Purpose</label>';
    addPurpose += '<div class="col-md-6">';
    addPurpose += '<input type="fname" class="form-control" value="" id="id_purpose">';
    addPurpose += '</div>';
    addPurpose += '</div>';
    addPurpose += '<div class="form-group">';
    addPurpose += '<label class="col-md-6 control-label">Active</label>';
    addPurpose += '<div class="col-md-6">';
    addPurpose += '<input type="checkbox" checked id="id_purposeActive">';
    addPurpose += '</div>';
    addPurpose += '</div>';
    addPurpose += '</div>';
    addPurpose += '</form>';
    addPurpose += '</div>';
    addPurpose += '<div class="modal-footer">';
    addPurpose += '<button type="button" class="btn btn-info" onclick="addPurpose()">Save</button>';
    addPurpose += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
    addPurpose += '</div>';
    addPurpose += '</div>	';
    addPurpose += '</div>	';
    addPurpose += '</div>	';
    $('#id_addPurposeTemplate').html(addPurpose);
    App.initUniform();
    App.fixContentHeight();
}


function addPurpose() {
    var purpose = document.getElementById("id_purpose").value.trim();
    var active = document.getElementById("id_purposeActive").checked;
    if (purpose.trim().length == 0) {
        //        alert("Please enter purpose");
        fieldSenseTosterError("Please enter purpose", true);
        return false;
    }
    purpose = htmlEntities(purpose);
    $('#pleaseWaitDialog').modal('show');
    var purposeObject = {
        "purpose": purpose,
        "isActive": active,
        "createdBy": {
            "id": loginUserId
        }
    }
    var jsonData = JSON.stringify(purposeObject);

    $.ajax({
        type: "POST",
        url: fieldSenseURL + "/activityPurpose",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            if (data.errorCode == '0000') {
		//$('#pleaseWaitDialog').modal('hide');
		fieldSenseTosterSuccess(data.errorMessage, true);
                $(".addPurposeTemplateClose").modal('hide');
               	var table = $('#sample_5').DataTable();
		table.draw( false );              
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}


function editPurposeTemplate(purposeId) {
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/activityPurpose/" + purposeId,
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
                var purposeData = data.result;
                var purposeId = purposeData.id;
                var purpose = purposeData.purpose;
                var active = purposeData.isActive;
                var editPurposeTemplate = '';
                editPurposeTemplate += '<div class="modal-dialog modal-wide">';
                editPurposeTemplate += '<div class="modal-content">';
                editPurposeTemplate += '<div class="modal-header">';
                editPurposeTemplate += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
                editPurposeTemplate += '<h4 class="modal-title">Edit Purpose Details</h4>';
                editPurposeTemplate += '</div>';
                editPurposeTemplate += '<div class="modal-body">';
                editPurposeTemplate += '<form class="form-horizontal" role="form">								';
                editPurposeTemplate += '<div class="form-body">';
                editPurposeTemplate += '<div class="form-group">';
                editPurposeTemplate += '<label class="col-md-6 control-label">Purpose</label>';
                editPurposeTemplate += '<div class="col-md-6">';
                editPurposeTemplate += '<input type="fname" class="form-control" value="' + purpose + '" id="id_editPurpose">';
                editPurposeTemplate += '</div>';
                editPurposeTemplate += '</div>';
                editPurposeTemplate += '<div class="form-group">';
                editPurposeTemplate += '<label class="col-md-6 control-label">Active</label>';
                editPurposeTemplate += '<div class="col-md-6">';
                editPurposeTemplate += '<div class="checkbox-list">';
                if (active == true) {
                    editPurposeTemplate += '<input type="checkbox" checked id="id_editActive">';
                } else {
                    editPurposeTemplate += '<input type="checkbox" id="id_editActive">';
                }
                editPurposeTemplate += '</div>';
                editPurposeTemplate += '</div>';
                editPurposeTemplate += '</div>';
                editPurposeTemplate += '</div>';
                editPurposeTemplate += '</form>';
                editPurposeTemplate += '</div>';
                editPurposeTemplate += '<div class="modal-footer">';
                editPurposeTemplate += '<button type="button" class="btn btn-info" onclick="editPurpose(' + purposeId + ')">Save</button>';
                editPurposeTemplate += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
                editPurposeTemplate += '</div>';
                editPurposeTemplate += '</div>';
                editPurposeTemplate += '</div>';
                $('.cls_editPurposeTemplate').html(editPurposeTemplate);
		App.initUniform();
		App.fixContentHeight();
                $('#pleaseWaitDialog').modal('hide');
            }
        }
    });
}


function editPurpose(purposeId) {
    var purpose = document.getElementById("id_editPurpose").value.trim();
    var active = document.getElementById("id_editActive").checked;
    if (purpose.trim().length == 0) {
        //        alert("Please enter purpose");
        fieldSenseTosterError("Please enter purpose", true);
        return false;
    }
    purpose = htmlEntities(purpose);
    $('#pleaseWaitDialog').modal('show');
    var purposeObject = {
        "id": purposeId,
        "purpose": purpose,
        "isActive": active
    }
    var jsonData = JSON.stringify(purposeObject);

    $.ajax({
        type: "PUT",
        url: fieldSenseURL + "/activityPurpose",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            if (data.errorCode == '0000') {
                //$('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterSuccess(data.errorMessage, true);
                $(".cls_editPurposeTemplate").modal('hide');
		var table = $('#sample_5').DataTable();
		table.draw( false );
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}


function deletePurpose(purposeId, purposeNm) {
    //var deleteU = confirm("Are you sure you want to delete " + purposeNm + "");
    //if (deleteU == true) {
	bootbox.dialog({
  		message: "Are you sure you want to delete "+purposeNm+" ?",
  		title: "Delete Purpose",
  		buttons: {
    			yes: {
      				label: "Yes",
      				className: "btn-default",
      				callback: function() {
        				$('#pleaseWaitDialog').modal('show');
        				$.ajax({
            					type: "DELETE",
            					url: fieldSenseURL + "/activityPurpose/" + purposeId,
            					contentType: "application/json; charset=utf-8",
            					headers: {
               	 					"userToken": userToken
            					},
            					crossDomain: false,
            					cache: false,
            					dataType: 'json',
            					success: function (data) {
                					if (data.errorCode == '0000') {
                   						//$('#pleaseWaitDialog').modal('hide');
								fieldSenseTosterSuccess(data.errorMessage, true);
                    						var table = $('#sample_5').DataTable();
								table.draw( false );
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
   // }
}

//Added By Jyoti, 27-02-2017
function visitImportExpenseCategory(){
        window.location='masterDataImportExpenseCategory.html';
}

function showExpenseCategoryTemplate() {

    	$('#id_expenseCategoryactive').addClass("active");
    	$('#id_purposeactive').removeClass("active");
        $('#id_ListCategoryactive').removeClass("active");
    	$('#id_industryCategoryactive').removeClass("active");
    	$('#id_territoryCategoryactive').removeClass("active");
    	$('.cls_activeForPurpose').attr('id', 'rep');
    	$('#id_attachPurpose').html('');
    	var showExpenseCategoryTemplate = '';
    	showExpenseCategoryTemplate += '<div class="page-content">';
    	showExpenseCategoryTemplate += '<div class="row" style="margin-top:15px;">';
    	showExpenseCategoryTemplate += '<div class="col-md-12">';
    	showExpenseCategoryTemplate += '</div>';
    	showExpenseCategoryTemplate += '</div>';
    	showExpenseCategoryTemplate += '<div class="row">';
    	showExpenseCategoryTemplate += '<div class="col-md-12">';
    	showExpenseCategoryTemplate += '<div class="portlet-body">';
        //Added By Jyoti, 27-02-2017
        showExpenseCategoryTemplate += '<button type="button" class="btn btn-info fr" style="float:left; margin-left:685px;" onclick="visitImportExpenseCategory()" ><i class="fa fa-download"></i> Import Expense Category</button> ';
    	//Ended By Jyoti, 27-02-2017
        showExpenseCategoryTemplate += '<button type="button" class="btn btn-info" style="float:right;" data-toggle="modal" href="#responsive" onclick="addExpenseCategoryTemplate()"><i class="fa fa-pencil"></i> Add Expense Category</button>';
    	showExpenseCategoryTemplate += '<span id="id_showExpenseCategory"></span>';
    	showExpenseCategoryTemplate += '</div>';
    	showExpenseCategoryTemplate += '</div>';
    	showExpenseCategoryTemplate += '</div>';
    	showExpenseCategoryTemplate += '<span id="id_addExpenseCategoryTemplate"></span>';
    	showExpenseCategoryTemplate += '<div id="responsive4" class="modal fade cls_editExpenseCategoryTemplate" tabindex="-1" aria-hidden="true"></div>';
    	showExpenseCategoryTemplate += '</div>';
    	$('#id_attachPurpose').html(showExpenseCategoryTemplate);
	var start=0;
	var length=20;
	myTable=$("#sample_5").DataTable();
	if(myTable != null)myTable.destroy();
    	//$('#pleaseWaitDialog').modal('show');
	var showExpenseCategoryListHtml = '';
        showExpenseCategoryListHtml += '<table class="table table-striped table-bordered table-hover" id="sample_5">';
        showExpenseCategoryListHtml += '<thead>';
        showExpenseCategoryListHtml += '<tr>';
        showExpenseCategoryListHtml += '<th>';
        showExpenseCategoryListHtml += 'Category Name';
        showExpenseCategoryListHtml += '</th>';
        showExpenseCategoryListHtml += '<th>';
        showExpenseCategoryListHtml += 'Active';
        showExpenseCategoryListHtml += '</th>';
        showExpenseCategoryListHtml += '<th>';
        showExpenseCategoryListHtml += '</th>								';
        showExpenseCategoryListHtml += '</tr>';
        showExpenseCategoryListHtml += '</thead>';
        showExpenseCategoryListHtml +='<tbody></tbody></table>';
	$('#id_showExpenseCategory').html(showExpenseCategoryListHtml);
	var inboxtable = $('#sample_5').dataTable({

                "bServerSide": true,
                "bDestroy": true,
                "ajax": {
   		 	"url": fieldSenseURL + "/expenseCategory/offset/",
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
                            "aTargets": [1,2] 
			                             
                	}
		],
		"aoColumns": [ // ahmed : here we populate the data in datatable that is returned using Ajax
			      { "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						return full.categoryName;
					}
				},
				{ "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						var isActive = full.isActive;              
                    				if (isActive == false) {
                        				return '<input type="checkbox" disabled >';
                   				} else if (isActive == true) {
                        				return '<input type="checkbox" checked disabled >';
                    				}

					}
				},
				{ "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
                                    // Added by jyoti 27-june-2017
                                                var expenseCategory =full.categoryName;
                                                if(expenseCategory != "Unknown"){
                       					return '<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Edit" data-toggle="modal" href="#responsive4" onclick="editExpenseCategoryTemplate(\'' + full.id + '\')"><i class="fa fa-edit"></i></button><button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Delete" onclick="deleteExpenseCategory(\'' + full.id + '\',\'' + full.categoryName + '\')"><i class="fa fa-trash-o"></i></button>';
                    				}else{
							return '';
						}
                                    // Ended by jyoti
//						return '<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Edit" data-toggle="modal" href="#responsive4" onclick="editExpenseCategoryTemplate(\'' + full.id + '\')"><i class="fa fa-edit"></i></button><button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Delete" onclick="deleteExpenseCategory(\'' + full.id + '\',\'' + full.categoryName + '\')"><i class="fa fa-trash-o"></i></button>';
					}
				}
		],
		"aaSorting": [],
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



function addExpenseCategoryTemplate() {
    $('#id_addExpenseCategoryTemplate').html('');
    var addExpenseCategory = '';
    addExpenseCategory += '<div id="responsive" class="modal fade addExpenseCategoryTemplateClose" tabindex="-1" aria-hidden="true">';
    addExpenseCategory += '<div class="modal-dialog modal-wide">';
    addExpenseCategory += '<div class="modal-content">';
    addExpenseCategory += '<div class="modal-header">';
    addExpenseCategory += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
    addExpenseCategory += '<h4 class="modal-title">Add Expense Category Details</h4>';
    addExpenseCategory += '</div>';
    addExpenseCategory += '<div class="modal-body">';
    addExpenseCategory += '<form class="form-horizontal" role="form">';
    addExpenseCategory += '<div class="form-body">';
    addExpenseCategory += '<div class="form-group">';
    addExpenseCategory += '<label class="col-md-6 control-label">Category Name</label>';
    addExpenseCategory += '<div class="col-md-6">';
    addExpenseCategory += '<input type="fname" class="form-control" value="" id="id_eCategory">';
    addExpenseCategory += '</div>';
    addExpenseCategory += '</div>';
    addExpenseCategory += '<div class="form-group">';
    addExpenseCategory += '<label class="col-md-6 control-label">Active</label>';
    addExpenseCategory += '<div class="col-md-6">';
    addExpenseCategory += '<input type="checkbox" checked id="id_expenseCategoryActive">';
    addExpenseCategory += '</div>';
    addExpenseCategory += '</div>';
    addExpenseCategory += '</div>';
    addExpenseCategory += '</form>';
    addExpenseCategory += '</div>';
    addExpenseCategory += '<div class="modal-footer">';
    addExpenseCategory += '<button type="button" class="btn btn-info" onclick="addExpenseCategory()">Save</button>';
    addExpenseCategory += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
    addExpenseCategory += '</div>';
    addExpenseCategory += '</div>';
    addExpenseCategory += '</div>';
    addExpenseCategory += '</div>';
    $('#id_addExpenseCategoryTemplate').html(addExpenseCategory);
    App.initUniform();
    App.fixContentHeight();;
}


function addExpenseCategory() {

    var categoryName = document.getElementById("id_eCategory").value.trim();
    var active = document.getElementById("id_expenseCategoryActive").checked;
    if (categoryName.trim().length == 0) {
        fieldSenseTosterError("Please enter expense category", true);
        return false;
    }
    if (categoryName.trim().length > 100) {
        fieldSenseTosterError("Category name cannot be more than 100 characters.", true);
        return false;
    }
    categoryName = htmlEntities(categoryName);
    $('#pleaseWaitDialog').modal('show');
    var expenseCategoryObject = {
        "categoryName": categoryName,
        "isActive": active,
        "createdBy": {
            "id": loginUserId
        }
    }
    var jsonData = JSON.stringify(expenseCategoryObject);

    $.ajax({
        type: "POST",
        url: fieldSenseURL + "/expenseCategory",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            if (data.errorCode == '0000') {
		///$('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterSuccess(data.errorMessage, true);
                $(".addExpenseCategoryTemplateClose").modal('hide');
                var table = $('#sample_5').DataTable();
		table.draw( false );             
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}


function editExpenseCategoryTemplate(eCategoryId) {
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/expenseCategory/" + eCategoryId,
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
                var eCategoryData = data.result;
                var expenseCategoryId = eCategoryData.id;
                var categoryName = eCategoryData.categoryName;
                var active = eCategoryData.isActive;
                var editExpenseCategoryTemplate = '';
                editExpenseCategoryTemplate += '<div class="modal-dialog modal-wide">';
                editExpenseCategoryTemplate += '<div class="modal-content">';
                editExpenseCategoryTemplate += '<div class="modal-header">';
                editExpenseCategoryTemplate += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
                editExpenseCategoryTemplate += '<h4 class="modal-title">Edit Expense Category Details</h4>';
                editExpenseCategoryTemplate += '</div>';
                editExpenseCategoryTemplate += '<div class="modal-body">';
                editExpenseCategoryTemplate += '<form class="form-horizontal" role="form">								';
                editExpenseCategoryTemplate += '<div class="form-body">';
                editExpenseCategoryTemplate += '<div class="form-group">';
                editExpenseCategoryTemplate += '<label class="col-md-6 control-label">Category Name</label>';
                editExpenseCategoryTemplate += '<div class="col-md-6">';
                editExpenseCategoryTemplate += '<input type="fname" class="form-control" value="' + categoryName + '" id="id_editECategory">';
                editExpenseCategoryTemplate += '</div>';
                editExpenseCategoryTemplate += '</div>';
                editExpenseCategoryTemplate += '<div class="form-group">';
                editExpenseCategoryTemplate += '<label class="col-md-6 control-label">Active</label>';
                editExpenseCategoryTemplate += '<div class="col-md-6">';
                editExpenseCategoryTemplate += '<div class="checkbox-list">';
                if (active == true) {
                    editExpenseCategoryTemplate += '<input type="checkbox" checked id="id_editECategoryActive">';
                } else {
                    editExpenseCategoryTemplate += '<input type="checkbox" id="id_editECategoryActive">';
                }
                editExpenseCategoryTemplate += '</div>';
                editExpenseCategoryTemplate += '</div>';
                editExpenseCategoryTemplate += '</div>';
                editExpenseCategoryTemplate += '</div>';
                editExpenseCategoryTemplate += '</form>';
                editExpenseCategoryTemplate += '</div>';
                editExpenseCategoryTemplate += '<div class="modal-footer">';
                editExpenseCategoryTemplate += '<button type="button" class="btn btn-info" onclick="editExpenseCategory(' + expenseCategoryId + ')">Save</button>';
                editExpenseCategoryTemplate += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
                editExpenseCategoryTemplate += '</div>';
                editExpenseCategoryTemplate += '</div>';
                editExpenseCategoryTemplate += '</div>';
                $('.cls_editExpenseCategoryTemplate').html(editExpenseCategoryTemplate);
		App.initUniform();
		App.fixContentHeight();
                $('#pleaseWaitDialog').modal('hide');
            }
        }
    });
}


function editExpenseCategory(eCategoryId) {
    var categoryName = document.getElementById("id_editECategory").value.trim();
    var active = document.getElementById("id_editECategoryActive").checked;
    if (categoryName.trim().length == 0) {
        fieldSenseTosterError("Please enter expense category", true);
        return false;
    }
    if (categoryName.trim().length > 100) {
        fieldSenseTosterError("Expense category more than 100 character is not allowed", true);
        return false;
    }
    categoryName = htmlEntities(categoryName);
    $('#pleaseWaitDialog').modal('show');
    var eCategorybject = {
        "id": eCategoryId,
        "categoryName": categoryName,
        "isActive": active
    }
    var jsonData = JSON.stringify(eCategorybject);

    $.ajax({
        type: "PUT",
        url: fieldSenseURL + "/expenseCategory",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            if (data.errorCode == '0000') {
                //$('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterSuccess(data.errorMessage, true);
                $(".cls_editExpenseCategoryTemplate").modal('hide');
                var table = $('#sample_5').DataTable();
		table.draw( false );
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}


function deleteExpenseCategory(eCategoryId, eCategory) {
    //var deleteU = confirm("Are you sure you want to delete " + eCategory + "");
    ///if (deleteU == true) {
	bootbox.dialog({
  		message: "Are you sure you want to delete "+eCategory+" ?",
  		title: "Delete Expense Category",
  		buttons: {
    			yes: {
      				label: "Yes",
      				className: "btn-default",
      				callback: function() {
        				$('#pleaseWaitDialog').modal('show');
        				$.ajax({
            					type: "DELETE",
            					url: fieldSenseURL + "/expenseCategory/" + eCategoryId,
            					contentType: "application/json; charset=utf-8",
            					headers: {
                					"userToken": userToken
            					},
            					crossDomain: false,
            					cache: false,
            					dataType: 'json',
            					success: function (data) {
                					if (data.errorCode == '0000') {
								//$('#pleaseWaitDialog').modal('hide');
							    	fieldSenseTosterSuccess(data.errorMessage, true);
							    	var table = $('#sample_5').DataTable();
								table.draw( false );
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


//Added By Jyoti, 28-02-2017
function visitImportTerritoryCategory(){
        window.location='masterDataImportTerritoryCategory.html';
}
////// Awaneesh started for territory

function showTerritoryCategoryTemplate() {

    	$('#id_territoryCategoryactive').addClass("active");
        $('#id_ListCategoryactive').removeClass("active");
    	$('#id_purposeactive').removeClass("active");
    	$('#id_industryCategoryactive').removeClass("active");
    	$('#id_expenseCategoryactive').removeClass("active");
    	$('.cls_activeForPurpose').attr('id', 'rep');
    	$('#id_attachPurpose').html('');
    	var showTerritoryCategoryTemplate = '';
    	showTerritoryCategoryTemplate += '<div class="page-content">';
    	showTerritoryCategoryTemplate += '<div class="row" style="margin-top:15px;">';
    	showTerritoryCategoryTemplate += '<div class="col-md-12">';
    	showTerritoryCategoryTemplate += '</div>';
    	showTerritoryCategoryTemplate += '</div>';
    	showTerritoryCategoryTemplate += '<div class="row">';
    	showTerritoryCategoryTemplate += '<div class="col-md-12">';
    	showTerritoryCategoryTemplate += '<div class="portlet-body">';
        //Added By Jyoti, 28-02-2017
        showTerritoryCategoryTemplate += '<button type="button" class="btn btn-info fr" style="float:left; margin-left:685px;" onclick="visitImportTerritoryCategory()" ><i class="fa fa-download"></i> Import Territory Category</button> ';
    	//Ended By Jyoti, 28-02-2017
        showTerritoryCategoryTemplate += '<button type="button" class="btn btn-info" style="float:right;" data-toggle="modal" href="#responsive" onclick="addTerritoryCategoryTemplate()"><i class="fa fa-pencil"></i> Add Territory Category</button>';
    	showTerritoryCategoryTemplate += '<span id="id_showTerritoryCategory"></span>';
    	showTerritoryCategoryTemplate += '</div>';
    	showTerritoryCategoryTemplate += '</div>';
    	showTerritoryCategoryTemplate += '</div>';
	showTerritoryCategoryTemplate += '<div class="row">';
    	showTerritoryCategoryTemplate += '<div class="col-md-12">';
	showTerritoryCategoryTemplate += '<div id="territorytree">';
    	//showTerritoryCategoryTemplate += '<div class="portlet-body">';
    	//showTerritoryCategoryTemplate += '<button type="button" class="btn btn-info" style="float:right;" data-toggle="modal" href="#responsive" onclick="addTerritoryCategoryTemplate()"><i class="fa fa-pencil"></i> Add Territory Category</button>';
    	//showTerritoryCategoryTemplate += '<span id="id_showTerritoryCategory"></span>';
    	//showTerritoryCategoryTemplate += '</div>';
    	showTerritoryCategoryTemplate += '</div>';
	showTerritoryCategoryTemplate += '</div>';
    	showTerritoryCategoryTemplate += '</div>';
    	showTerritoryCategoryTemplate += '<span id="id_addTerritoryCategoryTemplate"></span>';
    	showTerritoryCategoryTemplate += '<div id="responsive4" class="modal fade cls_editTerritoryCategoryTemplate" tabindex="-1" aria-hidden="true"></div>';
    	showTerritoryCategoryTemplate += '</div>';
	showTerritoryCategoryTemplate += '<div class="row">';
    	showTerritoryCategoryTemplate += '<div class="col-md-12" id="territorytree">';
    	//showTerritoryCategoryTemplate += '<div class="portlet-body">';
    	//showTerritoryCategoryTemplate += '<button type="button" class="btn btn-info" style="float:right;" data-toggle="modal" href="#responsive" onclick="addTerritoryCategoryTemplate()"><i class="fa fa-pencil"></i> Add Territory Category</button>';
    	//showTerritoryCategoryTemplate += '<span id="id_showTerritoryCategory"></span>';
    	//showTerritoryCategoryTemplate += '</div>';
    	showTerritoryCategoryTemplate += '</div>';
    	showTerritoryCategoryTemplate += '</div>';
    	$('#id_attachPurpose').html(showTerritoryCategoryTemplate);
	//var showTerritoryCategoryListHtml = '';
       // showTerritoryCategoryListHtml += '<div class="table table-striped table-bordered table-hover" id="territorytree">';
       /* showTerritoryCategoryListHtml += '<thead>';
        showTerritoryCategoryListHtml += '<tr>';
        showTerritoryCategoryListHtml += '<th>';
        showTerritoryCategoryListHtml += 'Category Name';
        showTerritoryCategoryListHtml += '</th>';
        showTerritoryCategoryListHtml += '<th>';
        showTerritoryCategoryListHtml += 'Active';
        showTerritoryCategoryListHtml += '</th>';
        showTerritoryCategoryListHtml += '<th>';
        showTerritoryCategoryListHtml += '</th>								';
        showTerritoryCategoryListHtml += '</tr>';
        showTerritoryCategoryListHtml += '</thead>';
        showTerritoryCategoryListHtml +='<tbody></tbody>';*/
	//showTerritoryCategoryListHtml +='</div>';
	//$('#id_showTerritoryCategory').html(showTerritoryCategoryListHtml);
	
	var cateArray=[];
	/*array.push(
		{
                "id": 1,
                "parentid": 0,
		"name": "Goldman1",
                "active": "Goldman1",
                "actions": "sdsfdsf",
               // "date": "20-22-2225"
            });
    		array.push({
                "id": 0,
                "parentid": 2,
                "name": "Goldman0",
		 "active": "Goldman0",
                "actions": "sdsfdsf",
               // "date": "<a href='#'>20-22-2222</a>"
            });
            
           array.push({
                "id": 2,
                "parentid": 29,
                "name": "Goldman2",
		 "active": '<input type="checkbox" checked disabled >',
               "actions": "sdsfdsf",
                //"date": "20-22-2233"
           });
            
            array.push({
                "id": 3,
                "parentid": 1,
                "name": "Goldman3",
		 "active": "Goldman0",
               "actions": "sdsfdsf",
                //"date": "20-22-2233"
            });
*/
	$.ajax({
        	type: "GET",
        	url: fieldSenseURL + "/territoryCategory/",
       	 	contentType: "application/json; charset=utf-8",
        	headers: {
            		"userToken": userToken
        	},
        	crossDomain: false,
        	cache: false,
        	dataType: 'json',
        	success: function (data) {
            			var territoryData = data.result;
            			if (data.errorCode == '0000') {
					for(var i=0;i<territoryData.length;i++){
						var id = territoryData[i].id;
						var parentId =  territoryData[i].categoryPositionCsv;
						var parentPos = parentId;
						if(id==parentId){
							parentId=0;
						}else{
							parentId=parentId.split(",")[1];
						}
						var categoryName = territoryData[i].categoryName;
						var active = territoryData[i].isActive;
						var existsButton = "";
						if(active==true){
							active = '<input type="checkbox" checked disabled >';
						}else{
							active = '<input type="checkbox" disabled >';
						}
						if(categoryName != "Unknown"){
							existsButton = '<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Edit" data-toggle="modal" href="#responsive4" onclick="editTerritoryCategoryTemplate(\'' + id + '\',\'' + parentPos + '\')"><i class="fa fa-edit"></i></button><button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Delete" onclick="deleteTerritoryCategory(\'' + id + '\',\'' + categoryName + '\',\'' + parentId + '\',\'' + parentPos + '\')"><i class="fa fa-trash-o"></i></button>';
						}
						cateArray.push({"id":id,"parentid":parentId,"name":categoryName,"active":active,"actions":existsButton});

					}
					var source =
            					{
                					dataType: "json",
                					dataFields: [
                    						{ name: "name", type: "string" },
                  						//{ name: "quantity", type: "number" },
                    						{ name: "id", type: "number" },
                    						{ name: "parentid", type: "number" },
                    						{ name: "active", type: "string" },
                   						// { name: "date", type: "string" },
                    						{ name: "actions", type: "string" }
                					],
               						hierarchy:
                						{
                   							keyDataField: { name: 'id' },
                    							parentDataField: { name: 'parentid' }
                						},
                					id: 'id',
                					localData: cateArray
            					};
            				var dataAdapter = new $.jqx.dataAdapter(source);
            				// create Tree Grid
            				$("#territorytree").jqxTreeGrid(
            				{
               					width: 760,
                				source: dataAdapter,
                				sortable: true,
                				//pageable: true,
                				pagerMode: 'advanced',
                				ready: function () {
                    					//$("#territorytree").jqxTreeGrid('expandRow', '2');
							App.initUniform();
							App.fixContentHeight();
							jQuery('.tooltips').tooltip();
                				},						
                				columns: [
                  					{ text: 'Category Name', dataField: "name", width: 260},
                  					{ text: 'Active', dataField: "active",  width: 250},
                  					{ text: '   ', dataField: "actions", cellsFormat: "c2" },

                				]
            				});
					$('#territorytree').on('rowCollapse',function (event){
						App.initUniform();
						App.fixContentHeight();
						jQuery('.tooltips').tooltip();
					});
					$('#territorytree').on('rowExpand',function (event){
						App.initUniform();
						App.fixContentHeight();
						jQuery('.tooltips').tooltip();
					});
					App.initUniform();
					App.fixContentHeight();
					jQuery('.tooltips').tooltip();
					$('#pleaseWaitDialog').modal('hide');
            			}else {
                			$('#pleaseWaitDialog').modal('hide');
                			fieldSenseTosterError(data.errorMessage, true);
              				//$(".cls_editUser").modal('hide');
                			FieldSenseInvalidToken(data.errorMessage);
           			}
        	},						
		error: function(xhr, ajaxOptions, thrownError){
			$('#pleaseWaitDialog').modal('hide');
			alert("in error:"+thrownError);
            	} 
    	});
	
	/*$.ajax({
        	type: "GET",
        	url: "url": fieldSenseURL + "/territoryCategory/complete/",
       	 	contentType: "application/json; charset=utf-8",
        	headers: {
            		"userToken": userToken
        	},
        	crossDomain: false,
        	cache: false,
        	dataType: 'json',
        	success: function (data) {
            		var userData = data.result;
            			if (data.errorCode == '0000') {

            			}else {
                			$('#pleaseWaitDialog').modal('hide');
                			fieldSenseTosterError(data.errorMessage, true);
              				//  $(".cls_editUser").modal('hide');
                			FieldSenseInvalidToken(data.errorMessage);
           			}
        	},						
		error: function(xhr, ajaxOptions, thrownError){
			$('#pleaseWaitDialog').modal('hide');
			alert("in error:"+thrownError);
            	} 
    });
    	//$('#pleaseWaitDialog').modal('show');
	/*var start=0;
	var length=20;
	myTable=$("#sample_5").DataTable();
	if(myTable != null)myTable.destroy();
    	//$('#pleaseWaitDialog').modal('show');
	var inboxtable = $('#sample_5').dataTable({

                "bServerSide": true,
                "bDestroy": true,
                "ajax": {
   		 	"url": fieldSenseURL + "/territoryCategory/offset/",
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
                            "aTargets": [1,2] 
			                             
                	}
		],
		"aoColumns": [ // ahmed : here we populate the data in datatable that is returned using Ajax
			      { "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						return full.categoryName;
					}
				},
				{ "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						var isActive = full.isActive;                 
                    				if (isActive == false) {
                        				return '<input type="checkbox" disabled >';
                    				} else if (isActive == true) {
                        				return '<input type="checkbox" checked disabled >';
                    				}
					}
				},
				{ "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						var territoryCategory =full.categoryName;
						 if(territoryCategory != "Unknown"){
                       					return '<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Edit" data-toggle="modal" href="#responsive4" onclick="editTerritoryCategoryTemplate(\'' + full.id + '\')"><i class="fa fa-edit"></i></button><button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Delete" onclick="deleteTerritoryCategory(\'' + full.id + '\',\'' + territoryCategory + '\')"><i class="fa fa-trash-o"></i></button>';
                    				}else{
							return '';
						}
					}
				}
		],
		"aaSorting": [],
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
*/

}

var parentTerritoryCatagories=[];
var fullTerriCategories=[];

function addTerritoryCategoryTemplate() {
    $('#id_addTerritoryCategoryTemplate').html('');
    var addTerritoryCategory = '';
    addTerritoryCategory += '<div id="responsive" class="modal fade addTerritoryCategoryTemplateClose" tabindex="-1" aria-hidden="true">';
    addTerritoryCategory += '<div class="modal-dialog modal-wide">';
    addTerritoryCategory += '<div class="modal-content">';
    addTerritoryCategory += '<div class="modal-header">';
    addTerritoryCategory += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
    addTerritoryCategory += '<h4 class="modal-title">Add Territory Category Details</h4>';
    addTerritoryCategory += '</div>';
    addTerritoryCategory += '<div class="modal-body">';
    addTerritoryCategory += '<form class="form-horizontal" role="form">';
    addTerritoryCategory += '<div class="form-body">';
    addTerritoryCategory += '<div class="form-group">';
    addTerritoryCategory += '<label class="col-md-6 control-label">Category Name</label>';
    addTerritoryCategory += '<div class="col-md-6">';
    addTerritoryCategory += '<input type="fname" class="form-control" value="" id="id_eCategory">';
    addTerritoryCategory += '</div>';
    addTerritoryCategory += '</div>';
    addTerritoryCategory += '<div class="form-group">';
    addTerritoryCategory += '<label class="col-md-6 control-label">Parent Category</label>';
    addTerritoryCategory += '<div class="col-md-6">';
    //addTerritoryCategory += '<select class="form-control" id="add_parent_category">';
    //addTerritoryCategory += '<option value="0">Select</option>';                
    //addTerritoryCategory += '</select>';
    addTerritoryCategory += '<button id="add_drilldown" class="btn btn-primay btn-block drilldown" data-toggle="dropdown"><span class="text" placeholder="Select">Test</span></button>';
    addTerritoryCategory += ' </div>';
    addTerritoryCategory += '</div>';
    addTerritoryCategory += '<div class="form-group">';
    addTerritoryCategory += '<label class="col-md-6 control-label">Active</label>';
    addTerritoryCategory += '<div class="col-md-6">';
    addTerritoryCategory += '<input type="checkbox" checked id="id_territoryCategoryActive">';
    addTerritoryCategory += '</div>';
    addTerritoryCategory += '</div>';
    addTerritoryCategory += '</div>';
    addTerritoryCategory += '</form>';
    addTerritoryCategory += '</div>';
    addTerritoryCategory += '<div class="modal-footer">';
    addTerritoryCategory += '<button type="button" class="btn btn-info" onclick="addTerritoryCategory()">Save</button>';
    addTerritoryCategory += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
    addTerritoryCategory += '</div>';
    addTerritoryCategory += '</div>';
    addTerritoryCategory += '</div>';
    addTerritoryCategory += '</div>';
    $('#id_addTerritoryCategoryTemplate').html(addTerritoryCategory);
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        	type: "GET",
        	url:  fieldSenseURL + "/territoryCategory/",
       	 	contentType: "application/json; charset=utf-8",
        	headers: {
            		"userToken": userToken
        	},
        	crossDomain: false,
        	cache: false,
        	dataType: 'json',
        	success: function (data) {
            			var territoryData = data.result;
            			if (data.errorCode == '0000'){
					parentTerritoryCatagories=[];
					fullTerriCategories=[];
					/*var parentCategoryTemplate = '';
                			parentCategoryTemplate += '<option value="0">Select</option>';
					for(var i=0;i<parentCategory.length;i++){
						var id=parentCategory[i].id;
						var parentCsv=parentCategory[i].categoryPositionCsv;
						var categoryName=parentCategory[i].categoryName;
						var hasChild=parentCategory[i].hasChild;
						parentCategoryTemplate += '<option class="child'+hasChild+'" value="'+parentCsv+'">'+categoryName+'</option>';
					}*/
					//$("#add_parent_category").html(parentCategoryTemplate);
					parentTerritoryCatagories.push({"id": 0,"categoryName": "Select","categoryPositionCsv":"0"});
					for (var i = 0; i < territoryData.length; i++) {
						if(territoryData[i].categoryName.trim()!="Unknown"){
							if(territoryData[i].parentCategory==0){
								if(territoryData[i].hasChild==0){
									parentTerritoryCatagories.push({"id": territoryData[i].id,"categoryName": territoryData[i].categoryName,"categoryPositionCsv":territoryData[i].categoryPositionCsv,"active":territoryData[i].isActive});
								}else{
									parentTerritoryCatagories.push({"id": territoryData[i].id,"categoryName": territoryData[i].categoryName,"categoryPositionCsv":territoryData[i].categoryPositionCsv,"active":territoryData[i].isActive, "list": [{"id": territoryData[i].id, "categoryName": "unknown"}]});
								}
							}else{
								fullTerriCategories.push({"parentCat":territoryData[i].parentCategory,"id":territoryData[i].id,"categoryName":territoryData[i].categoryName,"hasChild":territoryData[i].hasChild,"categoryPositionCsv":territoryData[i].categoryPositionCsv,"active":territoryData[i].isActive});
							}
						}
                    			}
					$('#add_drilldown').drilldownSelect({ appendValue: false, data: parentTerritoryCatagories});
					$('#pleaseWaitDialog').modal('hide');
    					App.initUniform();
    					App.fixContentHeight();
            			}else {
                			$('#pleaseWaitDialog').modal('hide');
                			fieldSenseTosterError(data.errorMessage, true);
              				//  $(".cls_editUser").modal('hide');
                			FieldSenseInvalidToken(data.errorMessage);
           			}
        	},						
		error: function(xhr, ajaxOptions, thrownError){
			$('#pleaseWaitDialog').modal('hide');
			alert("in error:"+thrownError);
            	} 
    })
   
}


function addTerritoryCategory() {
    var categoryName = document.getElementById("id_eCategory").value.trim();
     if (categoryName.trim().length == 0) {
        fieldSenseTosterError("Please enter territory category", true);
        return false;
    }
    if (categoryName.trim().length > 100) {
        fieldSenseTosterError("Category name cannot be more than 100 characters.", true);
        return false;
    }
    var active = document.getElementById("id_territoryCategoryActive").checked;
    var parentCategoryPos = $("#add_drilldown > span").text().trim();
    if (parentCategoryPos=="Select") {
    	 parentCategoryPos="0";
    }else{
	 parentCategoryPos = $("#add_drilldown > span > span").attr("value").trim();
    }
    var parentCategory = parentCategoryPos;  

    //var parentHasChild=$("#add_parent_category").find('option:selected').attr("class");
    //parentHasChild=parentHasChild.substring(parentHasChild.length-1);
   
    if(parentCategory.indexOf(",")!=-1){
	parentCategory=parentCategory.split(",")[0];
    }

    categoryName = htmlEntities(categoryName);
    $('#pleaseWaitDialog').modal('show');
    var territoryCategoryObject = {
        "categoryName": categoryName,
	"categoryPositionCsv":parentCategoryPos,
	"parentCategory":parentCategory,
        "isActive": active,
	//"hasChild":parentHasChild,
        "createdBy": {
            "id": loginUserId
        }
    }
    var jsonData = JSON.stringify(territoryCategoryObject);

    $.ajax({
        type: "POST",
        url: fieldSenseURL + "/territoryCategory",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            if (data.errorCode == '0000') {
		//$('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterSuccess(data.errorMessage, true);
                $(".addTerritoryCategoryTemplateClose").modal('hide');
		showTerritoryCategoryTemplate();
                //var table = $('#sample_5').DataTable();
		//table.draw( false );               
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}


function editTerritoryCategoryTemplate(eCategoryId,parentPos) {
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/territoryCategory/" + eCategoryId,
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
                var eCategoryData = data.result;
                var territoryCategoryId = eCategoryData.id;
                var categoryName = eCategoryData.categoryName.trim();
                var active = eCategoryData.isActive;
		var parentCatPosCvs = eCategoryData.categoryPositionCsv;
		var parentCatName = eCategoryData.parentCatName;
		var parentCatId="0";
		if(territoryCategoryId!=parentCatPosCvs){
			parentCatId= parentCatPosCvs.substring(parentCatPosCvs.indexOf(",")+1);
		}
                var editTerritoryCategoryTemplate = '';
                editTerritoryCategoryTemplate += '<div class="modal-dialog modal-wide">';
                editTerritoryCategoryTemplate += '<div class="modal-content">';
                editTerritoryCategoryTemplate += '<div class="modal-header">';
                editTerritoryCategoryTemplate += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
                editTerritoryCategoryTemplate += '<h4 class="modal-title">Edit Territory Category Details</h4>';
                editTerritoryCategoryTemplate += '</div>';
                editTerritoryCategoryTemplate += '<div class="modal-body">';
                editTerritoryCategoryTemplate += '<form class="form-horizontal" role="form">								';
                editTerritoryCategoryTemplate += '<div class="form-body">';
                editTerritoryCategoryTemplate += '<div class="form-group">';
                editTerritoryCategoryTemplate += '<label class="col-md-6 control-label">Category Name</label>';
                editTerritoryCategoryTemplate += '<div class="col-md-6">';
                editTerritoryCategoryTemplate += '<input type="fname" class="form-control" value="' + categoryName + '" id="id_editECategory">';
                editTerritoryCategoryTemplate += '</div>';
                editTerritoryCategoryTemplate += '</div>';
		editTerritoryCategoryTemplate += '<div class="form-group">';
    		editTerritoryCategoryTemplate += '<label class="col-md-6 control-label">Parent Category</label>';
    		editTerritoryCategoryTemplate += '<div class="col-md-6">';
    		//editTerritoryCategoryTemplate += '<select class="form-control" id="edit_parent_category">';
    		//editTerritoryCategoryTemplate += '<option value="0">Select</option>';                
    		//editTerritoryCategoryTemplate += '</select>';
		editTerritoryCategoryTemplate += '<button id="edit_drilldown" class="btn btn-primay btn-block drilldown" data-toggle="dropdown"><span class="text" placeholder="Select">Test</span></button>';
    		editTerritoryCategoryTemplate += ' </div>';
    		editTerritoryCategoryTemplate += '</div>';
                editTerritoryCategoryTemplate += '<div class="form-group">';
                editTerritoryCategoryTemplate += '<label class="col-md-6 control-label">Active</label>';
                editTerritoryCategoryTemplate += '<div class="col-md-6">';
                editTerritoryCategoryTemplate += '<div class="checkbox-list">';
                if (active == true) {
                    editTerritoryCategoryTemplate += '<input type="checkbox" checked id="id_editECategoryActive">';
                } else {
                    editTerritoryCategoryTemplate += '<input type="checkbox" id="id_editECategoryActive">';
                }
                editTerritoryCategoryTemplate += '</div>';
                editTerritoryCategoryTemplate += '</div>';
                editTerritoryCategoryTemplate += '</div>';
                editTerritoryCategoryTemplate += '</div>';
                editTerritoryCategoryTemplate += '</form>';
                editTerritoryCategoryTemplate += '</div>';
                editTerritoryCategoryTemplate += '<div class="modal-footer">';
                editTerritoryCategoryTemplate += '<button type="button" class="btn btn-info" onclick="editTerritoryCategory(\'' + territoryCategoryId +'\',\''+parentCatPosCvs+'\',\''+categoryName+'\',\''+active+'\')">Save</button>';
                editTerritoryCategoryTemplate += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
                editTerritoryCategoryTemplate += '</div>';
                editTerritoryCategoryTemplate += '</div>';
                editTerritoryCategoryTemplate += '</div>';
                $('.cls_editTerritoryCategoryTemplate').html(editTerritoryCategoryTemplate);
		$.ajax({
			type: "GET",
			url:  fieldSenseURL + "/territoryCategory/parent/"+parentCatPosCvs,
	       	 	contentType: "application/json; charset=utf-8",
			headers: {
		    		"userToken": userToken
			},
			crossDomain: false,
			cache: false,
			dataType: 'json',
			success: function (data) {
		    			var territoryData = data.result;
		    			if (data.errorCode == '0000') {
						parentTerritoryCatagories=[];
						fullTerriCategories=[];
						/*var parentCategoryTemplate = '';
		        			parentCategoryTemplate += '<option value="0">Select</option>';
						for(var i=0;i<parentCategory.length;i++){
							var id=parentCategory[i].id;
							var catPoscsv=parentCategory[i].categoryPositionCsv;
							var categoryName=parentCategory[i].categoryName;
							var hasChild=parentCategory[i].hasChild;
							parentCategoryTemplate += '<option class="child'+hasChild+'" value="'+catPoscsv+'">'+categoryName+'</option>';
						}*/
						//$("#edit_parent_category").html(parentCategoryTemplate);
						//$("#edit_parent_category").val(parentCatId);
						parentTerritoryCatagories.push({"id": 0,"categoryName": "Select","categoryPositionCsv":"0"});
						for (var i = 0; i < territoryData.length; i++) {
							if(territoryData[i].categoryName.trim()!="Unknown"){
								if(territoryData[i].parentCategory==0){
									if(territoryData[i].hasChild==0){
										parentTerritoryCatagories.push({"id": territoryData[i].id,"categoryName": territoryData[i].categoryName,"categoryPositionCsv":territoryData[i].categoryPositionCsv,"active":territoryData[i].isActive});
									}else{
										parentTerritoryCatagories.push({"id": territoryData[i].id,"categoryName": territoryData[i].categoryName,"categoryPositionCsv":territoryData[i].categoryPositionCsv,"active":territoryData[i].isActive, "list": [{"id": territoryData[i].id, "categoryName": "unknown"}]});
									}
								}else{
									fullTerriCategories.push({"parentCat":territoryData[i].parentCategory,"id":territoryData[i].id,"categoryName":territoryData[i].categoryName,"hasChild":territoryData[i].hasChild,"categoryPositionCsv":territoryData[i].categoryPositionCsv,"active":territoryData[i].isActive});
								}
							}
                    				}
						$('#edit_drilldown').drilldownSelect({ appendValue: false, data: parentTerritoryCatagories});
						if(parentCatPosCvs!=territoryCategoryId){
							$("#edit_drilldown > span").html('<span value="'+parentCatId+'">'+parentCatName+'</span>');
						}
	    					App.initUniform();
	    					App.fixContentHeight();
		    			}else {
		        			$('#pleaseWaitDialog').modal('hide');
		        			fieldSenseTosterError(data.errorMessage, true);
		      				//  $(".cls_editUser").modal('hide');
		        			FieldSenseInvalidToken(data.errorMessage);
		   			}
			},						
			error: function(xhr, ajaxOptions, thrownError){
				$('#pleaseWaitDialog').modal('hide');
				alert("in error:"+thrownError);
		    	} 
			
    		});
		//App.initUniform();
		//App.fixContentHeight();
                $('#pleaseWaitDialog').modal('hide');		
            }
        }
    });
}


function editTerritoryCategory(eCategoryId,oldCatPos,oldCatName,oldActive) {
    var categoryName = document.getElementById("id_editECategory").value.trim();
    if (categoryName.trim().length == 0) {
        fieldSenseTosterError("Please enter territory category", true);
        return false;
    }
    if (categoryName.trim().length > 100) {
        fieldSenseTosterError("Territory category more than 100 character is not allowed", true);
        return false;
    }
    var active = document.getElementById("id_editECategoryActive").checked;
   // var parentCategoryPos = $("#edit_parent_category").val().trim();
    //var parentHasChild=$("#edit_parent_category").find('option:selected').attr("class");
   // parentHasChild=parentHasChild.substring(parentHasChild.length-1);
    var parentCategoryPos = $("#edit_drilldown > span").text().trim();
    if(parentCategoryPos=="Select") {
    	 parentCategoryPos="0";
    }else{
	 parentCategoryPos = $("#edit_drilldown > span > span").attr("value").trim();
    }
    var parentCategory = parentCategoryPos;
    if(parentCategory.indexOf(",")!=-1){
	parentCategory=parentCategory.split(",")[0];
    }
    if(parentCategoryPos=="0"){
	parentCategoryPos = eCategoryId;
    }else{
	parentCategoryPos = eCategoryId+","+parentCategoryPos;
    }
    
    categoryName = htmlEntities(categoryName);
    $('#pleaseWaitDialog').modal('show');
    var eCategorybject = {
        "id": eCategoryId,
        "categoryName": categoryName,
	"categoryPositionCsv":parentCategoryPos,
	"parentCategory":parentCategory,
	//"hasChild":parentHasChild,
        "isActive": active,
	"updatedBy": {
            "id": loginUserId
        }
    }
    var jsonData = JSON.stringify(eCategorybject);

    $.ajax({
        type: "PUT",
        url: fieldSenseURL + "/territoryCategory/"+oldCatPos+"/"+oldCatName+"/"+oldActive,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            if (data.errorCode == '0000') {
		//$('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterSuccess(data.errorMessage, true);
                $(".cls_editTerritoryCategoryTemplate").modal('hide');
		showTerritoryCategoryTemplate();
                //var table = $('#sample_5').DataTable();
		//table.draw( false );
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}


function deleteTerritoryCategory(eCategoryId, eCategory,parentId,parentPosCsv) {
    //var deleteU = confirm("Are you sure you want to delete " + eCategory + "");
   /// if (deleteU == true) {

	bootbox.dialog({
  		message: "Are you sure you want to delete "+eCategory+" ?",
  		title: "Delete Territory Category",
  		buttons: {
    			yes: {
      				label: "Yes",
      				className: "btn-default",
      				callback: function() {
        				$('#pleaseWaitDialog').modal('show');
        				$.ajax({
				    		type: "DELETE",
				    		url: fieldSenseURL + "/territoryCategory/" + eCategoryId+"/"+parentPosCsv+"/"+eCategory.trim(),
				    		contentType: "application/json; charset=utf-8",
				    		headers: {
							"userToken": userToken
				    		},
				    		crossDomain: false,
				    		cache: false,
				    		dataType: 'json',
				    		success: function (data) {
							if (data.errorCode == '0000') {
							    	//$('#pleaseWaitDialog').modal('hide');
							    	fieldSenseTosterSuccess(data.errorMessage, true);
								showTerritoryCategoryTemplate();
							    	//var table = $('#sample_5').DataTable();
							   	// table.draw( false );
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

//Added By Jyoti, 27-02-2017
function visitImportIndustryCategory(){
        window.location='masterDataImportIndustryCategory.html';
}
//awaneesh for industry
function showIndustryCategoryTemplate() {

    	$('#id_industryCategoryactive').addClass("active");
        $('#id_ListCategoryactive').removeClass("active");
    	$('#id_purposeactive').removeClass("active");
    	$('#id_territoryCategoryactive').removeClass("active");
    	$('#id_expenseCategoryactive').removeClass("active");
    	$('.cls_activeForPurpose').attr('id', 'rep');
    	$('#id_attachPurpose').html('');
    	var showIndustryCategoryTemplate = '';
    	showIndustryCategoryTemplate += '<div class="page-content">';
    	showIndustryCategoryTemplate += '<div class="row" style="margin-top:15px;">';
    	showIndustryCategoryTemplate += '<div class="col-md-12">';
    	showIndustryCategoryTemplate += '</div>';
    	showIndustryCategoryTemplate += '</div>';
    	showIndustryCategoryTemplate += '<div class="row">';
    	showIndustryCategoryTemplate += '<div class="col-md-12">';
    	showIndustryCategoryTemplate += '<div class="portlet-body">';
        //Added By Jyoti, 27-02-2017
        showIndustryCategoryTemplate += '<button type="button" class="btn btn-info fr" style="float:left; margin-left:685px;" onclick="visitImportIndustryCategory()" ><i class="fa fa-download"></i> Import Industry Category</button> ';
    	//Ended By Jyoti, 27-02-2017
        showIndustryCategoryTemplate += '<button type="button" class="btn btn-info" style="float:right;" data-toggle="modal" href="#responsive" onclick="addIndustryCategoryTemplate()"><i class="fa fa-pencil"></i> Add Industry Category</button>';
    	showIndustryCategoryTemplate += '<span id="id_showIndustryCategory"></span>';
    	showIndustryCategoryTemplate += '</div>';
    	showIndustryCategoryTemplate += '</div>';
    	showIndustryCategoryTemplate += '</div>';
    	showIndustryCategoryTemplate += '<span id="id_addIndustryCategoryTemplate"></span>';
    	showIndustryCategoryTemplate += '<div id="responsive4" class="modal fade cls_editIndustryCategoryTemplate" tabindex="-1" aria-hidden="true"></div>';
    	showIndustryCategoryTemplate += '</div>';
   	$('#id_attachPurpose').html(showIndustryCategoryTemplate);
	var showIndustryCategoryListHtml = '';
        showIndustryCategoryListHtml += '<table class="table table-striped table-bordered table-hover" id="sample_5">';
        showIndustryCategoryListHtml += '<thead>';
        showIndustryCategoryListHtml += '<tr>';
        showIndustryCategoryListHtml += '<th>';
        showIndustryCategoryListHtml += 'Category Name';
        showIndustryCategoryListHtml += '</th>';
        showIndustryCategoryListHtml += '<th>';
        showIndustryCategoryListHtml += 'Active';
        showIndustryCategoryListHtml += '</th>';
        showIndustryCategoryListHtml += '<th>';
        showIndustryCategoryListHtml += '</th>								';
        showIndustryCategoryListHtml += '</tr>';
        showIndustryCategoryListHtml += '</thead>';
        showIndustryCategoryListHtml += '<tbody>';
     	showIndustryCategoryListHtml +='<tbody></tbody></table>';
	$('#id_showIndustryCategory').html(showIndustryCategoryListHtml);
    	//$('#pleaseWaitDialog').modal('show');
	var start=0;
	var length=20;
	myTable=$("#sample_5").DataTable();
	if(myTable != null)myTable.destroy();
    	//$('#pleaseWaitDialog').modal('show');
	var inboxtable = $('#sample_5').dataTable({

                "bServerSide": true,
                "bDestroy": true,
                "ajax": {
   		 	"url": fieldSenseURL + "/industryCategory/offset/",
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
                            "aTargets": [1,2] 
			                             
                	}
		],
		"aoColumns": [ // ahmed : here we populate the data in datatable that is returned using Ajax
			      { "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						return full.categoryName;
					}
				},
				{ "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						var isActive = full.isActive;                 
                    				if (isActive == false) {
                        				return '<input type="checkbox" disabled >';
                    				} else if (isActive == true) {
                        				return '<input type="checkbox" checked disabled >';
                    				}
					}
				},
				{ "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						var industryCategory =full.categoryName;
						 if(industryCategory != "Unknown"){
                       					return '<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Edit" data-toggle="modal" href="#responsive4" onclick="editIndustryCategoryTemplate(\'' + full.id + '\')"><i class="fa fa-edit"></i></button><button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Delete" onclick="deleteIndustryCategory(\'' + full.id + '\',\'' + industryCategory + '\')"><i class="fa fa-trash-o"></i></button>';
                    				}else{
							return '';
						}
					}
				}
		],
		"aaSorting": [],
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



function addIndustryCategoryTemplate() {
    $('#id_addIndustryCategoryTemplate').html('');
    var addIndustryCategory = '';
    addIndustryCategory += '<div id="responsive" class="modal fade addIndustryCategoryTemplateClose" tabindex="-1" aria-hidden="true">';
    addIndustryCategory += '<div class="modal-dialog modal-wide">';
    addIndustryCategory += '<div class="modal-content">';
    addIndustryCategory += '<div class="modal-header">';
    addIndustryCategory += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
    addIndustryCategory += '<h4 class="modal-title">Add Industry Category Details</h4>';
    addIndustryCategory += '</div>';
    addIndustryCategory += '<div class="modal-body">';
    addIndustryCategory += '<form class="form-horizontal" role="form">';
    addIndustryCategory += '<div class="form-body">';
    addIndustryCategory += '<div class="form-group">';
    addIndustryCategory += '<label class="col-md-6 control-label">Category Name</label>';
    addIndustryCategory += '<div class="col-md-6">';
    addIndustryCategory += '<input type="fname" class="form-control" value="" id="id_eCategory">';
    addIndustryCategory += '</div>';
    addIndustryCategory += '</div>';
    addIndustryCategory += '<div class="form-group">';
    addIndustryCategory += '<label class="col-md-6 control-label">Active</label>';
    addIndustryCategory += '<div class="col-md-6">';
    addIndustryCategory += '<input type="checkbox" checked id="id_industryCategoryActive">';
    addIndustryCategory += '</div>';
    addIndustryCategory += '</div>';
    addIndustryCategory += '</div>';
    addIndustryCategory += '</form>';
    addIndustryCategory += '</div>';
    addIndustryCategory += '<div class="modal-footer">';
    addIndustryCategory += '<button type="button" class="btn btn-info" onclick="addIndustryCategory()">Save</button>';
    addIndustryCategory += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
    addIndustryCategory += '</div>';
    addIndustryCategory += '</div>	';
    addIndustryCategory += '</div>	';
    addIndustryCategory += '</div>	';
    $('#id_addIndustryCategoryTemplate').html(addIndustryCategory);
    App.initUniform();
    App.fixContentHeight();
}

function addIndustryCategory() {
    var categoryName = document.getElementById("id_eCategory").value.trim();
    var active = document.getElementById("id_industryCategoryActive").checked;
    if (categoryName.trim().length == 0) {
        fieldSenseTosterError("Please enter industry category", true);
        return false;
    }
    if (categoryName.trim().length > 100) {
        fieldSenseTosterError("Category name cannot be more than 100 characters.", true);
        return false;
    }
    categoryName = htmlEntities(categoryName);
    $('#pleaseWaitDialog').modal('show');
    var industryCategoryObject = {
        "categoryName": categoryName,
        "isActive": active,
        "createdBy": {
            "id": loginUserId
        }
    }
    var jsonData = JSON.stringify(industryCategoryObject);

    $.ajax({
        type: "POST",
        url: fieldSenseURL + "/industryCategory",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            if (data.errorCode == '0000') {
		//$('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterSuccess(data.errorMessage, true);
                $(".addIndustryCategoryTemplateClose").modal('hide');
                var table = $('#sample_5').DataTable();
		table.draw( false );
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}


function editIndustryCategoryTemplate(eCategoryId) {
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/industryCategory/" + eCategoryId,
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
                var eCategoryData = data.result;
                var industryCategoryId = eCategoryData.id;
                var categoryName = eCategoryData.categoryName;
                var active = eCategoryData.isActive;
                var editIndustryCategoryTemplate = '';
                editIndustryCategoryTemplate += '<div class="modal-dialog modal-wide">';
                editIndustryCategoryTemplate += '<div class="modal-content">';
                editIndustryCategoryTemplate += '<div class="modal-header">';
                editIndustryCategoryTemplate += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
                editIndustryCategoryTemplate += '<h4 class="modal-title">Edit Industry Category Details</h4>';
                editIndustryCategoryTemplate += '</div>';
                editIndustryCategoryTemplate += '<div class="modal-body">';
                editIndustryCategoryTemplate += '<form class="form-horizontal" role="form">								';
                editIndustryCategoryTemplate += '<div class="form-body">';
                editIndustryCategoryTemplate += '<div class="form-group">';
                editIndustryCategoryTemplate += '<label class="col-md-6 control-label">Category Name</label>';
                editIndustryCategoryTemplate += '<div class="col-md-6">';
                editIndustryCategoryTemplate += '<input type="fname" class="form-control" value="' + categoryName + '" id="id_editECategory">';
                editIndustryCategoryTemplate += '</div>';
                editIndustryCategoryTemplate += '</div>';
                editIndustryCategoryTemplate += '<div class="form-group">';
                editIndustryCategoryTemplate += '<label class="col-md-6 control-label">Active</label>';
                editIndustryCategoryTemplate += '<div class="col-md-6">';
                editIndustryCategoryTemplate += '<div class="checkbox-list">';
                if (active == true) {
                    editIndustryCategoryTemplate += '<input type="checkbox" checked id="id_editECategoryActive">';
                } else {
                    editIndustryCategoryTemplate += '<input type="checkbox" id="id_editECategoryActive">';
                }
                editIndustryCategoryTemplate += '</div>';
                editIndustryCategoryTemplate += '</div>';
                editIndustryCategoryTemplate += '</div>';
                editIndustryCategoryTemplate += '</div>';
                editIndustryCategoryTemplate += '</form>';
                editIndustryCategoryTemplate += '</div>';
                editIndustryCategoryTemplate += '<div class="modal-footer">';
                editIndustryCategoryTemplate += '<button type="button" class="btn btn-info" onclick="editIndustryCategory(' + industryCategoryId + ')">Save</button>';
                editIndustryCategoryTemplate += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
                editIndustryCategoryTemplate += '</div>';
                editIndustryCategoryTemplate += '</div>';
                editIndustryCategoryTemplate += '</div>';
                $('.cls_editIndustryCategoryTemplate').html(editIndustryCategoryTemplate);
		App.initUniform();
		App.fixContentHeight();
                $('#pleaseWaitDialog').modal('hide');
            }
        }
    });
}


function editIndustryCategory(eCategoryId) {
    var categoryName = document.getElementById("id_editECategory").value.trim();
    var active = document.getElementById("id_editECategoryActive").checked;
    if (categoryName.trim().length == 0) {
        fieldSenseTosterError("Please enter industry category", true);
        return false;
    }
    if (categoryName.trim().length > 100) {
        fieldSenseTosterError("Industry category more than 100 character is not allowed", true);
        return false;
    }
    categoryName = htmlEntities(categoryName);
    $('#pleaseWaitDialog').modal('show');
    var eCategorybject = {
        "id": eCategoryId,
        "categoryName": categoryName,
        "isActive": active
    }
    var jsonData = JSON.stringify(eCategorybject);

    $.ajax({
        type: "PUT",
        url: fieldSenseURL + "/industryCategory",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            if (data.errorCode == '0000') {
		//$('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterSuccess(data.errorMessage, true);
                $(".cls_editIndustryCategoryTemplate").modal('hide');
                var table = $('#sample_5').DataTable();
		table.draw( false ); 
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}


function deleteIndustryCategory(eCategoryId, eCategory) {
    //var deleteU = confirm("Are you sure you want to delete " + eCategory + "");
    //if (deleteU == true) {
	bootbox.dialog({
  		message: "Are you sure you want to delete "+eCategory+" ?",
  		title: "Delete Industry Category",
  		buttons: {
    			yes: {
      				label: "Yes",
      				className: "btn-default",
      				callback: function() {
        				$('#pleaseWaitDialog').modal('show');
        				$.ajax({
				    		type: "DELETE",
				    		url: fieldSenseURL + "/industryCategory/" + eCategoryId,
				    		contentType: "application/json; charset=utf-8",
				    		headers: {
							"userToken": userToken
				    		},
				    		crossDomain: false,
				    		cache: false,
				    		dataType: 'json',
            					success: function (data) {
							if (data.errorCode == '0000') {
							   // $('#pleaseWaitDialog').modal('hide');
							    fieldSenseTosterSuccess(data.errorMessage, true);
							    var table = $('#sample_5').DataTable();
							    table.draw( false );
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
   // }
}
function showListCategoryTemplate()
{
        $('#id_ListCategoryactive').addClass("active");
        $('#id_purposeactive').removeClass("active");
    	$('#id_expenseCategoryactive').removeClass("active");
    	$('#id_industryCategoryactive').removeClass("active");
    	$('#id_territoryCategoryactive').removeClass("active");
        $('.cls_activeForPurpose').attr('id', 'rep');
    	$('#id_attachPurpose').html('');
        var showListTemplate = '';
    	showListTemplate += '<div class="page-content">';
    	showListTemplate += '<div class="row" style="margin-top:15px;">';
    	showListTemplate += '<div class="col-md-12">';
    	showListTemplate += '</div>';
    	showListTemplate += '</div>';
    	showListTemplate += '<div class="row">';
    	showListTemplate += '<div class="col-md-12">';
    	showListTemplate += '<div class="portlet-body">';
    	showListTemplate += '<button type="button" class="btn btn-info" style="float:right;" data-toggle="modal" href="#responsive" onclick="addListTemplate()"><i class="fa fa-pencil"></i> Add Custom List</button>';
    	showListTemplate += '<span id="id_showList"></span>';
    	showListTemplate += '</div>';
    	showListTemplate += '</div>';
    	showListTemplate += '</div>';
    	showListTemplate += '<span id="id_addListTemplate"></span>';
    	showListTemplate += '<div id="responsive4" class="modal fade cls_editListTemplate" tabindex="-1" aria-hidden="true"></div>';
    	showListTemplate += '</div>';
    	$('#id_attachPurpose').html(showListTemplate);
        var showListCategoryListHtml = '';
        showListCategoryListHtml += '<table class="table table-striped table-bordered table-hover" id="sample_5">';
        showListCategoryListHtml += '<thead>';
        showListCategoryListHtml += '<tr>';
        showListCategoryListHtml += '<th>';
        showListCategoryListHtml += 'List Name';
        showListCategoryListHtml += '</th>';
        showListCategoryListHtml += '<th>';
        showListCategoryListHtml += 'Active';
        showListCategoryListHtml += '</th>';
       
        showListCategoryListHtml += '<th>';
        showListCategoryListHtml += '</th>';
        showListCategoryListHtml += '</tr>';
        showListCategoryListHtml += '</thead>';
        showListCategoryListHtml +='<tbody></tbody></table>';
	$('#id_showList').html(showListCategoryListHtml);
        var start=0;
	var length=20;
	myTable=$("#sample_5").DataTable();
        var c;
if(myTable != null)myTable.destroy();
    	//$('#pleaseWaitDialog').modal('show');
	var inboxtable = $('#sample_5').dataTable({
        
                "bServerSide": true,
                "bDestroy": true,
                "ajax": {
   		 	"url": fieldSenseURL + "/predefinedList/offset/",
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
                            "aTargets": [1,2] 
			                             
                	}
		],
		"aoColumns": [ 
			      { "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
                                                var listName=full.listName;
						return listName;
					}
				},
				{ "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						var isActive = full.isActive;                 
                    				if (isActive === 0) {
                        				return '<input type="checkbox" disabled >';
                    				} else if (isActive === 1) {
                        				return '<input type="checkbox" checked disabled >';
                    				}
					}
				},
                               
                               
                                
				{ "mData": null,
				"sClass":"inbox-small-cells",
				"mRender" : function(data,type,full){
						
                       					return '<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Edit" data-toggle="modal" href="#responsive4" onclick="editPredefinedListTemplate(\'' + full.listId + '\')"><i class="fa fa-edit"></i></button><button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Delete" onclick="deletePredefinedList(\'' + full.listId + '\',\'' + full.listName + '\')"><i class="fa fa-trash-o"></i></button>';
                    				
					}
				}
		],
		"aaSorting": [],
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

function addListTemplate() {
    $('#id_addListTemplate').html('');
    var addList = '';
    addList += '<div id="responsive" class="modal fade addListTemplateClose" tabindex="-1" aria-hidden="true">';
    addList += '<div class="modal-dialog modal-wide">';
    addList += '<div class="modal-content">';
    addList += '<div class="modal-header">';
    addList += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
    addList += '<h4 class="modal-title">Add List Details</h4>';
    addList += '</div>';
    addList += '<div class="modal-body">';
    addList += '<form class="form-horizontal" role="form">';
    addList += '<div class="form-body">';
    addList += '<div class="form-group">';
   // addList += '<div class="form-group">';
    addList += '<label class="col-md-6 control-label">Active</label>';
    addList += '<div class="col-md-6">';
    addList += '<input type="checkbox" checked id="id_list_options">';
    addList += '</div>';
    addList += '</div>';
    //addList += '</div>';
    addList += '<div class="form-group">';
   // addList += '<div class="form-group">';
    addList += '<label class="col-md-6 control-label">List name</label>';
    addList += '<div class="col-md-6 control-label">';
    addList += '<input type="textbox" class="form-control" placeholder="Enter name of the list" id="id_ListName">';
   // addList += '</div>';
    addList += '</div>';
     addList += '</div>';
   //   addList += '<div class="form-group">';
    addList +='<div class="form-group" id="addCompValueLabel"> <label class="col-md-6 control-label">List Values</label> <div class="col-md-6"> ';
    addList +='<textarea class="form-control" id="comp_values1" rows="3" placeholder="Enter Values separated by new line"></textarea>';
    addList +='</div> </div> ';
   //  addList += '</div>';
    addList += '</form>';
    addList += '</div>';
    addList += '<div class="modal-footer">';
    addList += '<button type="button" class="btn btn-info" onclick="addList()">Save</button>';
    addList += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
    addList += '</div>';
    addList += '</div>	';
    addList += '</div>	';
    addList += '</div>	';
    $('#id_addListTemplate').html(addList);
    App.initUniform();
    App.fixContentHeight;
}

function addList()
{
  var listName=$("#id_ListName").val().trim();
   var active = document.getElementById("id_list_options").checked;
   if(active==true)
   {
       active=1;
   }else{
       active=0;
   }
  if (listName.trim().length == 0) {
        fieldSenseTosterError("Please enter List name", true);
        return false;
    }
  var options=$("#comp_values1").val().trim();
  if (options.trim().length == 0) {
        fieldSenseTosterError("Please enter options", true);
        return false;
    }
  var arr = options.split("\n");
	options="";
	for (var i = 0; i < arr.length; i++) {
  		if(arr[i].trim()!=="")
  			options += arr[i].trim()+"\n";
	}
	options= options.trim(); 
   
 	
	arr = options.split("\n");  
	var sorted_arr = arr.sort(); 
	var results = [];

	for (var i = 0; i < arr.length - 1; i++) {
    		if (sorted_arr[i + 1].trim() == sorted_arr[i].trim()) {
          		results.push(sorted_arr[i]);               
   		}
        }
	if(results.length>0) {
		fieldSenseTosterError("You have entered duplicates values:-"+results, true);
		return false;
	}
        //to convert option list into csv format
         arr = options.split("\n");
         options=arr[0];
         for (var i = 1; i < arr.length; i++) {
  		if(arr[i].trim()!=="")
  			options += ","+arr[i].trim();
	}
        listName = htmlEntities(listName);
    $('#pleaseWaitDialog').modal('show');
    var listCategoryObject = {
        "listName": listName,
        "isActive": active,
        "optionValues":options,
        "createdBy": {
            "id": loginUserId
        }
    }
    var jsonData = JSON.stringify(listCategoryObject);
       $.ajax({
        type: "POST",
        url: fieldSenseURL +"/predefinedList",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            if (data.errorCode == '0000') {
		///$('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterSuccess(data.errorMessage, true);
                $(".addListTemplateClose").modal('hide');
               var table = $('#sample_5').DataTable();
		table.draw( false );               
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}
function editPredefinedListTemplate(predefinedList) {
    $('#pleaseWaitDialog').modal('show');
    
    
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/predefinedList/" + predefinedList,
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
                var predefinedListData = data.result;
            var  listId = predefinedListData.listId;
              var listName = predefinedListData.listName;
                var active = predefinedListData.isActive;
                 var options=predefinedListData.optionValues;
               var optionList=options.split(",");
               options=optionList[0];
                for(var i=1;i<optionList.length;i++)
              {      
                  if(optionList[i].trim()!=="")		
                     options += "\n"+optionList[i].trim();
               }
                
                
                var editPredefinedList = '';
                editPredefinedList += '<div class="modal-dialog modal-wide">';
                editPredefinedList += '<div class="modal-content">';
                editPredefinedList += '<div class="modal-header">';
                editPredefinedList += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
                editPredefinedList += '<h4 class="modal-title">Edit Predefined List Category</h4>';
                editPredefinedList += '</div>';
                editPredefinedList += '<div class="modal-body">';
                editPredefinedList += '<form class="form-horizontal" role="form">';
                editPredefinedList += '<div class="form-body">';
                
                editPredefinedList += '<div class="form-group">';
                editPredefinedList += '<label class="col-md-6 control-label">Active</label>';
                editPredefinedList += '<div class="col-md-6">';
                editPredefinedList += '<div class="checkbox-list">';
                if (active == 1) {
                    editPredefinedList += '<input type="checkbox" checked id="id_editEPredefinedActive">';
                } else {
                    editPredefinedList += '<input type="checkbox" id="id_editEPredefinedActive">';
                }
                editPredefinedList += '</div>';
                editPredefinedList += '</div>';
                editPredefinedList += '</div>';
                editPredefinedList += '<div class="form-group">';
                editPredefinedList += '<label class="col-md-6 control-label">Category Name</label>';
                editPredefinedList += '<div class="col-md-6">';
                editPredefinedList += '<input type="fname" class="form-control" id="id_editEPredefinedList">';
                editPredefinedList += '</div>';
                editPredefinedList += '</div>';
               // editPredefinedList += '</div>';
               // editPredefinedList += '<div class="form-body">';
                editPredefinedList +='<div class="form-group" id="addCompValueLabel"> <label class="col-md-6 control-label">List Values</label> <div class="col-md-6"> ';
                editPredefinedList +='<textarea class="form-control" id="comp_values" rows="3" ></textarea>';
                editPredefinedList +='</div> </div> ';
                editPredefinedList += '</div>';
                editPredefinedList += '</form>';
                editPredefinedList += '</div>';
                editPredefinedList += '<div class="modal-footer">';
                editPredefinedList += '<button type="button" class="btn btn-info" onclick="editPredefinedList(' + listId + ')">Save</button>';
                editPredefinedList += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
                editPredefinedList += '</div>';
                editPredefinedList += '</div>';
                editPredefinedList += '</div>';
                 $('.cls_editListTemplate').html(editPredefinedList);
                 $('#id_editEPredefinedList').val(listName);
                $('#comp_values').val(options);
               
               
		App.initUniform();
		App.fixContentHeight();
                $('#pleaseWaitDialog').modal('hide');
                
          }
        }
    });  
}
function editPredefinedList(listId) {
    
    
    
   var listName=$("#id_editEPredefinedList").val();
   var active = document.getElementById("id_editEPredefinedActive").checked;
  
         var options=$("#comp_values").val();
    var optionList=options.split("\n");
               options=optionList[0];
                for(var i=1;i<optionList.length;i++)
              {      
                  if(optionList[i].trim()!=="")		
                     options += ","+optionList[i].trim();
               }
   if(active===true)
   {
       active=1;
        
   }
   else
   {
       
       active=0;
   }
    if (listName.trim().length === 0) {
        fieldSenseTosterError("Please enter List Name", true);
        return false;
    }
    if (listName.trim().length > 100) {
        fieldSenseTosterError("List name more than 100 character is not allowed", true);
        return false;
    }
    listName = htmlEntities(listName);
    $('#pleaseWaitDialog').modal('show');
    var ePredefinedList = {
        "listId": listId,
        "listName": listName,
        "isActive": active,
        "optionValues":options
    }
    var jsonData = JSON.stringify(ePredefinedList);

    $.ajax({
        type: "PUT",
        url: fieldSenseURL + "/predefinedList",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            if (data.errorCode == '0000') {
		//$('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterSuccess(data.errorMessage, true);
                $(".cls_editListTemplate").modal('hide');
                var table = $('#sample_5').DataTable();
		table.draw( false ); 
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
    
  
  
    
}

function deletePredefinedList(listId, listName) {
    //var deleteU = confirm("Are you sure you want to delete " + eCategory + "");
    //if (deleteU == true) {
     $.ajax({
        type: "GET",
        url: fieldSenseURL + "/predefinedList/getDeleteStatus/"+listId,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        success: function (data) {
            if (data.errorCode == '0000') {
    
	bootbox.dialog({
  		message: "Are you sure you want to delete "+listName+" ?",
  		title: "Delete Industry Category",
  		buttons: {
    			yes: {
      				label: "Yes",
      				className: "btn-default",
      				callback: function() {
        				$('#pleaseWaitDialog').modal('show');
        				$.ajax({
				    		type: "DELETE",
				    		url: fieldSenseURL + "/predefinedList/" + listId,
				    		contentType: "application/json; charset=utf-8",
				    		headers: {
							"userToken": userToken
				    		},
				    		crossDomain: false,
				    		cache: false,
				    		dataType: 'json',
            					success: function (data) {
							if (data.errorCode == '0000') {
							   // $('#pleaseWaitDialog').modal('hide');
							    fieldSenseTosterSuccess(data.errorMessage, true);
							    var table = $('#sample_5').DataTable();
							    table.draw( false );
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
          }
          else
          {
               fieldSenseTosterError("List is being used in form cannot be deleted", true);
          }
        } 
    });
    
    }
