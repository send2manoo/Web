/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var lat = 0;
var lng = 0;
var industryLength = 0;
var territoryLength = 0;
var doubleClickFlag = true;
var recentCustomers = new Array();
var industryCustomers = new Array();
var territorycustomers = new Array();
var customer = [];
var customerRecordsOffsetFlag = 0;
var isAssigneeIsImmidiateSubOrdinate = false;
var pathName;
var intervalCustomer = window.setInterval(function () {
    var userToken2 = fieldSenseGetCookie("userToken");
    try {
        if (userToken2.toString() == "undefined") {
            window.location.href = "login.html";
        } else if (loginUserId != 0) {
            loggedinUserImageData();
            loggedinUserData();
            var loc = window.location;
            pathName = loc.pathname.substring(loc.pathname.lastIndexOf('/') + 1).trim();	           
            window.clearTimeout(intervalCustomer);
        }
    }
    catch (err) {
        window.location.href = "login.html";
    }
}, 1000);
function showUser() {
    var searchText = $('#id_mobile_number').val().trim();
     $('#id_userShow').html('');
        var userTemplate = '';
//        userTemplate += '<table class="table table-striped table-bordered table-hover" id="sample_5">';
//        userTemplate += '<thead>';
//        userTemplate += '<tr>';
//        userTemplate += '<th>';
//        userTemplate += 'Full Name';
//        userTemplate += '</th>';
//        userTemplate += '<th>';
//        userTemplate += 'Account name';
//        userTemplate += '</th>';
//        userTemplate += '<th>';
//        userTemplate += 'Email-Id';
//        userTemplate += '</th>';
//        userTemplate += '</tr>';
//        userTemplate += '</thead>';
//        userTemplate += '<tbody>';
//        userTemplate += '<tr>';
//        userTemplate += '<td>"'++'"</td>';
//       
//        
//        userTemplate += '<td>';
//        userTemplate += '</td>';
//        
//        userTemplate += '<td>';
//        userTemplate += '</td>';
//        
//        userTemplate += '</tr>';
//        
//        userTemplate += '</tbody>';
//        userTemplate += '</table>';
       
        
        $.ajax({
		type: "GET",
		url: fieldSenseURL +"/stats/account/user/mobileno?searchText=" +searchText,
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
                        var sData =data.result;
                        for(var i=0;i<sData.length; i++){
                            var fname = sData[i].full_name
                           
                            var companyName = sData[i].accountName;
                            var email_id = sData[i].email_id;
                  
                         
                         userTemplate += '<table class="table table-striped table-bordered table-hover" id="sample_5">';
        userTemplate += '<thead>';
        userTemplate += '<tr>';
        userTemplate += '<th>';
        userTemplate += 'Full Name';
        userTemplate += '</th>';
        userTemplate += '<th>';
        userTemplate += 'Account name';
        userTemplate += '</th>';
        userTemplate += '<th>';
        userTemplate += 'Email-Id';
        userTemplate += '</th>';
        userTemplate += '</tr>';
        userTemplate += '</thead>';
        userTemplate += '<tbody>';
        userTemplate += '<tr>';
        userTemplate += '<td>"'+fname+'"</td>';
        userTemplate += '<td>"'+companyName+'"</td>';
        userTemplate += '<td>"'+email_id+'"</td>';
       
        userTemplate += '</tr>';
        
        userTemplate += '</tbody>';
        userTemplate += '</table>';
                            $('#id_userShow').html(userTemplate);

//load DataTable

		        $('#sample_i5').dataTable({
		            //                    "aaSorting": [[1, 'asc']],
				"bDestory":true,
				"sPaginationType": "bootstrap_full_number",
//                                "scrollY":"500px",
//                                "scrollCollapse": true,
//                                "paging": true,
		            	"aLengthMenu": [
		            		[-1, 5, 10, 15, 20,50,100],
                    			["All", 5, 10, 15, 20,50,100] // change per page values here
                    		],
		            	// set the initial value
		            	"iDisplayLength": 20,
		            	"bFilter": false,
			    	"aaSorting":[]
		        });
		
			var reportTable=$('#sample_i5').dataTable();

			reportTable.fnSetColumnVis(0,false);

		        jQuery('#sample_i5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
		        jQuery('#sample_i5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
		        jQuery('#sample_i5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown
		     //DataTable Done
			$('#pleaseWaitDialog').modal('hide');
                        }
		    }
		    
		}
       });
}

function showUser1(){

        //    document.getElementById("id_searchText").focus();
	var searchText = $('#id_mobile_number').val().trim();
            
	var start=0;
	var length=20;
	myTable=$("#sample_66").DataTable();
	if(myTable != null)myTable.destroy();
	$('#pleaseWaitDialog').modal('show');
        $('#id_userShow').html('');
	var customerShowTemplateHtml = '';
        customerShowTemplateHtml += '<table class="table table-striped table-bordered table-hover" id=" ">';
        customerShowTemplateHtml += '<thead>';
        customerShowTemplateHtml += '<tr>';
       	customerShowTemplateHtml += '<th>';
        customerShowTemplateHtml += 'Full Name';
        customerShowTemplateHtml += '</th>';
        customerShowTemplateHtml += '<th>';
        customerShowTemplateHtml += 'Company Name';
        customerShowTemplateHtml += '</th>';
        customerShowTemplateHtml += '<th>';
        customerShowTemplateHtml += 'Email';
        customerShowTemplateHtml += '</th>';
        customerShowTemplateHtml += '</tr>';
        customerShowTemplateHtml += '</thead>';
	customerShowTemplateHtml +='<tbody>';
        customerShowTemplateHtml += '<tr>';
        customerShowTemplateHtml += '<td></td>';
        customerShowTemplateHtml += '<td></td>';
        customerShowTemplateHtml += '<td></td>';
       
        customerShowTemplateHtml += '</tr>';
        customerShowTemplateHtml +='</tbody>';
        customerShowTemplateHtml +='</table>';
	$("#id_userShow").html(customerShowTemplateHtml);
	var inboxtable = $('#sample_66').dataTable({

                "bServerSide": true,
                "bDestroy": true,
                "ajax": {
   		 	"url": fieldSenseURL +"/stats/account/user/mobileno?searchText=" +searchText,
    			"type": "GET",
			headers: {
            			"userToken": userToken
        		},
                        "autoWidth": false,
			contentType: "application/json; charset=utf-8",
			crossDomain: false,
      			cache: false,
        		dataType: 'json',
        		asyn: true,
			error: function (data) {
				//$('#pleaseWaitDialog').modal('hide');
				if (data.status==200){
					fieldSenseTosterError("Some problem occured.", true);
					showUser1();
				}else{
					$('#pleaseWaitDialog').modal('hide')
					fieldSenseTosterError(data.responseJSON.errorMessage, true);
				}				
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
                 "sLengthMenu": "_MENU_ <span id ='id_totalUserRecords'></span>",
		//"sInfo": "Showing _START_ to _END_ of _TOTAL_ Mails",
              	"oPaginate": {
   		        "sPrevious": "Prev",
                        "sNext": "Next"
               		}
                },
		"aoColumnDefs": [
                        {   "bSortable": false,
                            "aTargets": [1,5] 
			                             
                	}
		],
		"aoColumns": [ // ahmed : here we populate the data in datatable that is returned using Ajax
			      { "mData": null,
				"sClass":"inbox-small-cells",
                                
				"mRender" : function(data,type,full){
                                                
						return full.full_name;
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
						return full.email_id;
					}
				}
		],
		"aaSorting": [],
		"bFilter": false,
		"preDrawCallback": function( settings ) {
    			$('#pleaseWaitDialog').modal('show');
  		},
		"fnDrawCallback": function( oSettings ) { 
					//if(oSettings.fnDisplayEnd()>0){
					//	$("#id_totalUserRecords").html("<b>"+(oSettings._iDisplayStart+1)+"</b> - <b>"+oSettings.fnDisplayEnd()+"</b> of <b>"+oSettings.fnRecordsTotal()+"</b> records");
					//}
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

//    		jQuery('#sample_5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
//                jQuery('#sample_5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
//                jQuery('#sample_5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown	


		$('#id_searchText').val("");

}
