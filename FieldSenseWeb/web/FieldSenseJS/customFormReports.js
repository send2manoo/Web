var intervalAdminUser = window.setInterval(function () {
    var userToken2 = fieldSenseGetCookie("userToken");
    try {
        if (userToken2.toString() == "undefined") {
            window.location.href = "login.html";
        } else if (loginUserId != 0) {
            loggedinUserImageData();
            loggedinUserData();
            teamMembers();
            dateRange();
            loadFormDetails();
            custFormNamesRep();
            window.clearTimeout(intervalAdminUser);
        }
    }
    catch (err) {
        alert(err);
        window.location.href = "login.html";
    }
}, 1000);

var formHeaders=[];
function loadFormDetails() {
 var formId= fieldSenseGetCookie("customFormIdReport");

 var showBtn= '<button type="button" class="btn btn-info" style="margin-left: 47px;" onclick="formReports('+formId+')">Show</button>';
 $("#show_report").html(showBtn);

 loadFormHeaders(formId);
} 


function teamMembers() {
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/team/allSubordinates/" + loginUserId,
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
                teamMemberTemplate += '<select class="form-control" id="id_memberName">';
                teamMemberTemplate += '<option>Select Member</option>';
		if(teamMemberData.length>1)
		teamMemberTemplate += '<option value="All">All</option>';
                for (var i = 0; i < teamMemberData.length; i++) {
                    var userId = teamMemberData[i].id;
                    var firstName = teamMemberData[i].firstName;
                    var lastName = teamMemberData[i].lastName;
                    var fullName = firstName + ' ' + lastName;
                    teamMemberTemplate += '<option value="' + userId + '">' + fullName + '</option>';
                }
                teamMemberTemplate += '</select>';
            }
            $('#id_teamMembers').html(teamMemberTemplate);
        }
    });
}

function dateRange() {
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
    var firstDay = '01/' + m + '/' + y;
    var today = d + '/' + m + '/' + y;
    var dateRangeTemplate = '';
    dateRangeTemplate += '<div class = "form-group">';
    dateRangeTemplate += '<label class = "control-label col-md-4"> Date Range </label>';
    dateRangeTemplate += '<div class = "col-md-8">';
    dateRangeTemplate += '<div class = "input-group input-large date-picker input-daterange" data - date = "20/11/2012" data-date-format="dd/mm/yyyy">';
    dateRangeTemplate += '<input type = "text" class = "form-control" name = "from" id = "id_startDate" value="' + firstDay + '">';
    dateRangeTemplate += '<span class = "input-group-addon" >to</span>';
    dateRangeTemplate += '<input type = "text" class = "form-control" name = "to" id = "id_endDate" value="' + today + '">';
    dateRangeTemplate += '</div></div></div>';
    $('#dateRange').append(dateRangeTemplate);
    $('.date-picker').datepicker({
        rtl: App.isRTL(),
        autoclose: true
    });
}

function loadFormHeaders(id) {
    var custForm = '';

    $.ajax({
        type: "GET",
        url: fieldSenseURL+"/adminCustomForms/"+id, //change url
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: false,
        success: function (data) {
            var myData=data;
            if (data.errorCode == '0000') {
               
                $('.cls_customForm').html('');
                custForm += '<table class="table table-striped table-bordered table-hover" id="sample_i5">';
                custForm += '<thead>';
                custForm += '<tr>';
		custForm += '<th>Submitted by</th>';
		custForm += '<th>Submitted on</th>';
	
                for(var i=0;i<data.result.tableData.length;i++){
		        custForm += '<th>';
		        custForm += data.result.tableData[i].labelName;
		        custForm += '</th>';
                        formHeaders.push({"labelName":data.result.tableData[i].labelName,"fieldId":data.result.tableData[i].fieldId,"fieldType":data.result.tableData[i].fieldType});
			//Headersids.push(data.result.tableData[i].fieldId);
			//console.log(data.result.tableData[i].fieldId);
                }
                custForm += '</tr>';
                custForm += '</thead>';
                custForm += '<tbody> </tbody></table>';
            	}

             	$('.cls_customForm').html(custForm);  
             
             	//load DataTable

                $('#sample_i5').dataTable({
                    //                    "aaSorting": [[1, 'asc']],
                    "bDestory":true,
                    "aLengthMenu": [
                    [-1, 5, 10, 15, 20],
                    ["All", 5, 10, 15, 20] // change per page values here
                    ],
                    // set the initial value
                    "iDisplayLength": -1,
                    "bFilter": false,
		   "aaSorting":[]
                });
		
		var reportTable=$('#sample_i5').dataTable();
		reportTable.fnSetColumnVis(0,false);

                jQuery('#sample_i5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
                jQuery('#sample_i5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
                jQuery('#sample_i5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown
             //DataTable Done
       },
       error:function(xhr,ajaxOptions,erroThrown) {
           alert("erroThrown:"+erroThrown);
       }
       //}
       });

 }


function custFormNamesRep() {
    $.ajax({
        type: "GET",
        url: fieldSenseURL+"/adminCustomForms/all",  // change url
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: false,
        success: function (data) {
             if(data.errorCode=='0000'){              
                var addformName='SampleName';
                var formList=data.result;
		var templ="";
               var formId= fieldSenseGetCookie("customFormIdReport");
 	       $("#leftnav_form_"+formId).addClass("active");
               for(i=0;i<formList.length;i++)
                {
                templ+='<li id="leftnav_form_'+formList[i].id+'" '; 
                if(formId==formList[i].id) {
                	templ+='class="leftnav_form active">';
                }else{
                       templ+='class="leftnav_form">';
                }
                templ+='<a class="showSingle" onclick="loadCustomFormReportPage('+formList[i].id+')"> <span class="barrow"></span>'; 
		 templ+='<span class="membername" title="'+formList[i].formName+'">'+formList[i].formName+'</span></a></li>';
                }
	         $("#custFrmNameReports").html(templ);
                      }
        }
        });
 }

function formReports(id)
{
   //var exportExpense = new Array();
    $('#id_exportFormReport').html('<button type="button" class="btn btn-info fr" style="margin-right:-15px;"><i class=" fa fa-file-text"></i> Save as CSV</button>');
    var teamMember = document.getElementById("id_memberName").value;
    if (teamMember == 'Select Member') {
       	fieldSenseTosterError("Please select any member");
	return false;
    }
    var startTime = document.getElementById("id_startDate").value;
    var endTime = document.getElementById("id_endDate").value;
    var startTimeSplit = startTime.split('/');
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
    var ensdTimeSplit = endTime.split('/');
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
	var url=fieldSenseURL + "/CustomFormsReport/"+ id + "/" + teamMember + "/"+fromDate+"/"+toDate;
	if(teamMember=="All")
		url=fieldSenseURL + "/CustomFormsReport/"+ id + "/User/"+loginUserId+"/All/"+fromDate+"/"+toDate;
    //var expenseTemplate = '';
    $('#pleaseWaitDialog').modal('show');
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
               	var mainArray=[];       
		var add1=[];
		var tableData = data.result;  
		if(teamMember=="All"){
	       		for(i=0;i<tableData.length;i++) {
				var fields=tableData[i].filledData;
				add1=[];
				var val;
				var k=0;	
				add1.push(tableData[i].submitTime.time);//added to sort based on date
				add1.push(tableData[i].submittedby);
				add1.push(convertToLocalTime(tableData[i].submitTime));
				for(j=0;j<formHeaders.length;j++) {
					if(k<fields.length){
			   			if(formHeaders[j].fieldId==fields[k].field_id_fk){
				     			if(formHeaders[j].fieldType=="Checkboxes")					
								val= fields[k].field_value.replace(/\n/g,', ');
							else
				    				val= fields[k].field_value;
				     			add1.push(val);
				     			k=k+1;
						}else{
				     		add1.push("");
						}
					}else{
			     			add1.push("");
					}
			
		 		}
		 	
		 	mainArray.push(add1);
			}

			//sorting based on date
			mainArray.sort(function(a,b){
				if (a[0]> b[0])
    				return -1;
  				else if (a[0]< b[0])
    				return 1;
  				else 
   				return 0;

			});
		
			//remove after sorting
			for(var i=0;i<mainArray.length;i++){
				mainArray[i]=mainArray[i].slice(1,mainArray[i].length);
			}

			//console.log(mainArray);
		       	var myTable = $('#sample_i5').DataTable();
			myTable.fnClearTable();
			myTable.fnSort([]);
			myTable.fnAddData(mainArray);
		
			myTable.fnSetColumnVis(0,true);
			
			//tooltip
			myTable.$('td').mouseover( function () {
	   		var sData = myTable.fnGetData( this );
			$(this).attr('title',sData);
	 		} );

			$("#sample_i5").find('td').css({'max-width':'300px','overflow':'hidden','text-overflow':'ellipsis'});
		
		        // prepare CSV data
		        var excelHeader="";
		        var csvData = new Array();
			var k=0;
			excelHeader+='"Submitted by",';
			excelHeader+='"Submitted on",';
		        while(k<formHeaders.length-1) {
		        
		                excelHeader+='"'+formHeaders[k].labelName+'",';
				k=k+1;
	 
		         }
			 excelHeader+='"'+formHeaders[k].labelName+'"';
		        csvData.push(excelHeader);
			excelHeader="";
			var i=0;		
		        while(i < mainArray.length) {
				//var j=0;
				excelHeader+='"'+mainArray[i][0]+'",';
				excelHeader+='"'+mainArray[i][1]+'",';
				j=2;
				while(j<=formHeaders.length) {
					
				        excelHeader+='"'+mainArray[i][j]+'",';
					j=j+1;
				    } 
				excelHeader+='"'+mainArray[i][j]+'"';	
		              
				csvData.push(excelHeader);
				excelHeader="";
				i=i+1;
		        }
		
		       /* for (var i = 0; i < mainArray.length; i++) {
		            for (var j = 0; j < formHeaders.length; j++) {
		                csvData.push('"' + exportExpense[i][j].expenseName + '","' + exportExpense[i][j].eCategory + '","' + exportExpense[i][j].ammount + '","' + exportExpense[i][j].dateTime + '","' + exportExpense[i][j].customer + '","' + exportExpense[i][j].activity + '","' + exportExpense[i][j].st + '","' + exportExpense[i][j].approveBy + '","' + exportExpense[i][j].approveOn + '","' + exportExpense[i][j].reason + '"');
		            }
		        }*/
		        // download stuff
		        
		        if (csvData.length > 1) {
			var fileName;
				$(".leftnav_form").each(function(){
					if($(this).hasClass("active")){
						fileName=$(this).find(".membername").text();
					}
				});
		            var fileName = fileName+"_"+$("#id_memberName option:selected" ).text()+".csv";
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
		            $('#id_exportFormReport').html('');
		            link.innerHTML = '<button type="button" class="btn btn-info fr" style="margin-right:-15px;"><i class=" fa fa-file-text"></i> Save as CSV</button>';
		            document.getElementById("id_exportFormReport").appendChild(link);
		        }/*else{
		            //alert ("There is  no data to export" );
		            var link = document.createElement("a"); 
		            $('#id_exportFormReport').html('');
		            link.innerHTML = '<button type="button" class="btn btn-info fr"  onclick="alert(\'There is no data to export \')" style="margin-right:-15px;"><i class=" fa fa-file-text"></i> Save as CSV</button>';
		            document.getElementById("id_exportFormReport").appendChild(link);
		        }*/
		        
		       
		        //dtaTableForExpenseReport();
		    //                TableAdvanced.init();*/
		}else{
			for(i=0;i<tableData.length;i++) {
				var fields=tableData[i].filledData;
				add1=[];
				var val;
				var k=0;
				add1.push("");
				add1.push(convertToLocalTime(tableData[i].submitTime));
				for(j=0;j<formHeaders.length;j++) {
					if(k<fields.length){
			   			if(formHeaders[j].fieldId==fields[k].field_id_fk){
				     			if(formHeaders[j].fieldType=="Checkboxes")					
								val= fields[k].field_value.replace(/\n/g,', ');
							else
				    				val= fields[k].field_value;
				     			add1.push(val);
				     			k=k+1;
						}else{
				     		add1.push("");
						}
					}else{
			     			add1.push("");
					}
			
		 		}
		 	
		 	mainArray.push(add1);
			}

		       	var myTable = $('#sample_i5').DataTable();
			myTable.fnClearTable();
			myTable.fnSort([]);
			myTable.fnAddData(mainArray);
			myTable.fnSetColumnVis(0,false);
		
		

			//tooltip
			myTable.$('td').mouseover( function () {
	   		var sData = myTable.fnGetData( this );
			$(this).attr('title',sData);
	 		} );

			$("#sample_i5").find('td').css({'max-width':'300px','overflow':'hidden','text-overflow':'ellipsis'});
		
		        // prepare CSV data
		        var excelHeader="";
		        var csvData = new Array();
			var k=0;
			excelHeader+='"Submitted on",';
		        while(k<formHeaders.length-1) {
		        
		                excelHeader+='"'+formHeaders[k].labelName+'",';
				k=k+1;
	 
		         }
			excelHeader+='"'+formHeaders[k].labelName+'"';
		        csvData.push(excelHeader);
			excelHeader="";
			var i=0;		
		        while(i < mainArray.length) {
				excelHeader+='"'+mainArray[i][1]+'",';
				var j=2;
				while(j<=formHeaders.length) {
				        excelHeader+='"'+mainArray[i][j]+'",';
					j=j+1;
				    } 
				excelHeader+='"'+mainArray[i][j]+'"';	
		              
				csvData.push(excelHeader);
				excelHeader="";
				i=i+1;
		        }
		
		       /* for (var i = 0; i < mainArray.length; i++) {
		            for (var j = 0; j < formHeaders.length; j++) {
		                csvData.push('"' + exportExpense[i][j].expenseName + '","' + exportExpense[i][j].eCategory + '","' + exportExpense[i][j].ammount + '","' + exportExpense[i][j].dateTime + '","' + exportExpense[i][j].customer + '","' + exportExpense[i][j].activity + '","' + exportExpense[i][j].st + '","' + exportExpense[i][j].approveBy + '","' + exportExpense[i][j].approveOn + '","' + exportExpense[i][j].reason + '"');
		            }
		        }*/
		        // download stuff
		        
		        if (csvData.length > 1) {
			var fileName;
				$(".leftnav_form").each(function(){
					if($(this).hasClass("active")){
						fileName=$(this).find(".membername").text();
					}
				});
		            var fileName = fileName+"_"+$("#id_memberName option:selected" ).text()+".csv";
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
		            $('#id_exportFormReport').html('');
		            link.innerHTML = '<button type="button" class="btn btn-info fr" style="margin-right:-15px;"><i class=" fa fa-file-text"></i> Save as CSV</button>';
		            document.getElementById("id_exportFormReport").appendChild(link);
		        }/*else{
		            //alert ("There is  no data to export" );
		            var link = document.createElement("a"); 
		            $('#id_exportFormReport').html('');
		            link.innerHTML = '<button type="button" class="btn btn-info fr"  onclick="alert(\'There is no data to export \')" style="margin-right:-15px;"><i class=" fa fa-file-text"></i> Save as CSV</button>';
		            document.getElementById("id_exportFormReport").appendChild(link);
		        }*/
		        
		       
		        //dtaTableForExpenseReport();
		    //                TableAdvanced.init();*/


		}
			App.fixContentHeight();
			App.initUniform();
			$('#pleaseWaitDialog').modal('hide');
            }
        }
    });
    
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
