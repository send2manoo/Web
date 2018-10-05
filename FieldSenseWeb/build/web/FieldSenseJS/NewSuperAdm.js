var intervalAdminUser = window.setInterval(function () {
    var userToken2 = fieldSenseGetCookie("userToken");
    try {
        if (userToken2.toString() == "undefined") {
            window.location.href = "login.html";
        } else if (loginUserId != 0) {
            if(role==0){
              	leftSideMenu();
                getAccountRegions(); // load all region , from same method set region in search filter
		statsData();
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

function statsData1(){

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
        statsDataHtml += '<tr class="odd gradeX">';
        statsDataHtml += '<td>';
        statsDataHtml += '<a data-toggle="modal" href="#acc_dtl" class="tooltips" data-placement="bottom" title="Account Details">RatanFire</a>'
        statsDataHtml += '<td>';
        statsDataHtml += '<td>';
        statsDataHtml += 'Free';
        statsDataHtml += '<td>';
         statsDataHtml += '<td>';
        statsDataHtml += '31';
        statsDataHtml += '<td>';
         statsDataHtml += '<td>';
        statsDataHtml += '40';
        statsDataHtml += '<td>';
         statsDataHtml += '<td>';
        statsDataHtml += '20 Apr 2015';
        statsDataHtml += '<td>';
         statsDataHtml += '<td>';
        statsDataHtml += '20 Apr 2015';
        statsDataHtml += '<td>';
         statsDataHtml += '<td>';
        statsDataHtml += '28 Apr 2015';
        statsDataHtml += '<td>';
         statsDataHtml += '<td>';
        statsDataHtml += '<span class="label label-sm label-danger">Inactive</span>';
        statsDataHtml += '<td>';
         statsDataHtml += '<td>';
        statsDataHtml += '10%';
        statsDataHtml += '<td>';
         statsDataHtml += '<td>';
        statsDataHtml += 'Mumbai';
        statsDataHtml += '<td>';
        
        statsDataHtml += '</tr>';
        statsDataHtml += '</tbody>';
        statsDataHtml += '</table>';
        $('#id_statsData').html(statsDataHtml);
//        var inboxtable = $('#sample_5').dataTable({
//        	"bServerSide": true,
//                "bDestroy": true,
//                "ajax": {
//   		 	"url": fieldSenseURL + "/stats/accountsData/offset/?accname="+accname+"&status="+status+"&actfreq="+actfreq+"&regionId="+regionId,
//    			"type": "GET",
//			headers: {
//            			"userToken": userToken
//        		},
//			contentType: "application/json; charset=utf-8",
//			crossDomain: false,
//      			cache: false,
//        		dataType: 'json',
//        		asyn: true,
//  		},
//		"iDisplayStart":start,
//                // set the initial value
//                "iDisplayLength": length, // It will be by default page width set by user
//                "sPaginationType": "bootstrap_full_number",
//		"bProcessing": false,
//                "aLengthMenu": [
//		 	[ 5, 10, 15, 20,50,100],
//                    	[ 5, 10, 15, 20,50,100] // change per page values here
//                 ],
//                "oLanguage": {
//                 // "sProcessing":"Loading",
//		 "sEmptyTable":"No data available in table",
//                 "sLengthMenu": "_MENU_ <span id ='id_totalCustRecords'></span>",
//		//"sInfo": "Showing _START_ to _END_ of _TOTAL_ Mails",
//              	"oPaginate": {
//   		        "sPrevious": "Prev",
//                        "sNext": "Next"
//               		}
//                },
//		"aoColumnDefs": [
//                        {   "bSortable": false,
//                            "aTargets": [08] 
//			                             
//                	}
//		],
//		"aoColumns": [ // ahmed : here we populate the data in datatable that is returned using Ajax
//			      { "mData": null,
//				"sClass":"inbox-small-cells",
//				"mRender" : function(data,type,full){
//						return full.accountName;
//					}
//				},
//				{ "mData": null,
//				"sClass":"inbox-small-cells",
//				"mRender" : function(data,type,full){
//						return full.usersCount-1;/*removed comapny default user*/
//					}
//				},
//				{ "mData": null,
//				"sClass":"inbox-small-cells",
//				"mRender" : function(data,type,full){
//						return full.usersLimit;
//					}
//				},
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
//		],
//		"aaSorting": [3,"desc"],
//		"bFilter": false,
//		"preDrawCallback": function( settings ) {
//    			$('#pleaseWaitDialog').modal('show');
//  		},
//		"fnDrawCallback": function( oSettings ) { 
//					if(oSettings.fnDisplayEnd()>0){
//						$("#id_totalCustRecords").html("<b>"+(oSettings._iDisplayStart+1)+"</b> - <b>"+oSettings.fnDisplayEnd()+"</b> of <b>"+oSettings.fnRecordsTotal()+"</b> records");
//					}
//					inboxtable.$('td').mouseover( function () {
//						var sData=$(this).text();
//						$(this).attr('title',sData);
// 			 		} );
//					App.initUniform();
//					App.fixContentHeight();
//					jQuery('.tooltips').tooltip();
//					$('#pleaseWaitDialog').modal('hide');
//		},
//                });
//                jQuery('#sample_5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
//                jQuery('#sample_5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
//                jQuery('#sample_5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown		
//}


}