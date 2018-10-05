/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var intervalProfile = window.setInterval(function(){
    var userToken2=fieldSenseGetCookie("userToken");
    try{
        if(userToken2.toString()=="undefined"){
            window.location.href = "login.html";
        }else  if(loginUserId!=0){
            loggedinUserImageData();
            loggedinUserData();
            userProfileTemplate();
	     if(role==2){
		$('#top_tabs').html('<li class="inactive"><a href="Dashboard_accounts.html">Dashboard</a></li><li class="inactive"><a href="expenses_acc.html">Expenses</a></li>');
		$("#account_pages").attr("href","Dashboard_accounts.html");
		}
            //            userPanelClick();
            window.clearTimeout(intervalProfile);
        }
    }
    catch(err){
        window.location.href = "login.html";
    }
},1000);
function userProfileTemplate(){
    $.ajax({
        type: "GET",
        url: fieldSenseURL+"/user/"+loginUserId,
        contentType: "application/json; charset=utf-8",
        headers:{
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn:false,
        success: function(data) {
            if(data.errorCode=='0000'){
                var userData=data.result;
                var firstName=userData.firstName;
                var lastName=userData.lastName;
                var gender=userData.gender;
                var role1=userData.role;
                var role=userData.role;
                var designation=userData.designation;
                if(role==1){
                    role='Admin';
                }else if(role==2){
                    role='Team Leader';
                }else if(role==3){
                    role='Team Member';
                }else if(role==4){
                    role='In-office Staff';
                }else if(role==5){
                    role='On-field Personnel';
                }
                var emailAddress=userData.emailAddress;
                var mobileNo=userData.mobileNo;
                var userProfileTemplateHtml='';
                userProfileTemplateHtml+='<div class="page-content-wrapper">';
                userProfileTemplateHtml+='<div class="page-content">';

                userProfileTemplateHtml+='<div class="row">';
                userProfileTemplateHtml+='<div class="col-md-12">';
                userProfileTemplateHtml+='<ul class="page-breadcrumb breadcrumb">';
                userProfileTemplateHtml+='<li>';
                userProfileTemplateHtml+='<i class="fa fa-group"></i>';
                userProfileTemplateHtml+='<a href="#">My Profile</a>';
                userProfileTemplateHtml+='</li>';
                userProfileTemplateHtml+='</ul>';
                userProfileTemplateHtml+='</div>';
                userProfileTemplateHtml+='</div>';
                userProfileTemplateHtml+='<div class="row">';
                userProfileTemplateHtml+='<div class="col-md-12">';
                if(userMode==1){
                    userProfileTemplateHtml+='<form role="form" class="form-horizontal" action="adminUpload_pic.html" method="POST">';
                }
                else if((userMode==2) || (userMode==3)){
                    userProfileTemplateHtml+='<form role="form" class="form-horizontal" action="upload_pic.html" method="POST">';
                }
                userProfileTemplateHtml+='<div class="form-body">';
                userProfileTemplateHtml+='<div class="form-group">';
                userProfileTemplateHtml+='<label class="col-md-3 control-label">First Name</label>';
                userProfileTemplateHtml+='<div class="col-md-4">';
                userProfileTemplateHtml+='<input type="text" class="form-control" placeholder="First Name" value="'+firstName+'" id="id_firstName">';
                userProfileTemplateHtml+='</div>';
                userProfileTemplateHtml+='</div>';
                userProfileTemplateHtml+='<div class="form-group">';
                userProfileTemplateHtml+='<label class="col-md-3 control-label">Last Name</label>';
                userProfileTemplateHtml+='<div class="col-md-4">';
                userProfileTemplateHtml+='<input type="text" class="form-control" placeholder="Last Name" value="'+lastName+'" id="id_lastName">';
                userProfileTemplateHtml+='</div>';
                userProfileTemplateHtml+='</div>';
                userProfileTemplateHtml+='<div class="form-group">';
                userProfileTemplateHtml+='<label class="col-md-3 control-label">Designation</label>';
                userProfileTemplateHtml+='<div class="col-md-4">';
                userProfileTemplateHtml+='<input type="text" class="form-control" placeholder="Designation" value="'+designation+'" id="id_designation">';
                userProfileTemplateHtml+='</div>';
                userProfileTemplateHtml+='</div>';
                userProfileTemplateHtml+='<div class="form-group">';
                userProfileTemplateHtml+='<label class="col-md-3 control-label">Photo Upload</label>';
                userProfileTemplateHtml+='<div class="col-md-4">';
                userProfileTemplateHtml+='<div class="fileupload-new thumbnail" style="width: 150px; height: 150px;">';
                userProfileTemplateHtml+='<img src="'+imageURLPath+accountId+'_'+loginUserId+'_140X140.png?'+time+'" alt="" onerror="this.src=\'assets/img/noimage.jpg\';"/>';
                userProfileTemplateHtml+='</div>';
                userProfileTemplateHtml+='<button class="btn btn-default btn-file" style="margin-top:-15px;">';
                userProfileTemplateHtml+='<i class="fa fa-paperclip"></i> Select image';
                userProfileTemplateHtml+='</button>';
                userProfileTemplateHtml+='</div>';
                userProfileTemplateHtml+='</div>';
                userProfileTemplateHtml+='<div class="form-group">';
                userProfileTemplateHtml+='<label class="col-md-3 control-label">Gender</label>';
                userProfileTemplateHtml+='<div class="col-md-4 radio-list">';
                if(gender==0){
                    userProfileTemplateHtml+='<label class="radio-inline">';
                    userProfileTemplateHtml+='<input type="radio" name="id_gender" id="optionsRadios4" value="1" > Male </label>';
                    userProfileTemplateHtml+='<label class="radio-inline">';
                    userProfileTemplateHtml+='<input type="radio" name="id_gender" id="optionsRadios5" value="0" checked> Female </label>';
                }else if(gender==1){
                    userProfileTemplateHtml+='<label class="radio-inline">';
                    userProfileTemplateHtml+='<input type="radio" name="id_gender" id="optionsRadios4" value="1" checked> Male </label>';
                    userProfileTemplateHtml+='<label class="radio-inline">';
                    userProfileTemplateHtml+='<input type="radio" name="id_gender" id="optionsRadios5" value="0" > Female </label>';
                }else{
                    userProfileTemplateHtml+='<label class="radio-inline">';
                    userProfileTemplateHtml+='<input type="radio" name="id_gender" id="optionsRadios4" value="1" > Male </label>';
                    userProfileTemplateHtml+='<label class="radio-inline">';
                    userProfileTemplateHtml+='<input type="radio" name="id_gender" id="optionsRadios5" value="0" > Female </label>';
                }
                userProfileTemplateHtml+='</div>';
                userProfileTemplateHtml+='</div>';
                userProfileTemplateHtml+='<div class="form-group">';
                userProfileTemplateHtml+='<label class="col-md-3 control-label">Email</label>';
                userProfileTemplateHtml+='<div class="col-md-4">';
                userProfileTemplateHtml+='<input type="email" class="form-control" id="inputEmail1" placeholder="Email" value="'+emailAddress+'" readonly>';
                userProfileTemplateHtml+='</div>';
                userProfileTemplateHtml+='</div>';
                userProfileTemplateHtml+='<div class="form-group">';
                userProfileTemplateHtml+='<label class="col-md-3 control-label">Mobile No.</label>';
                userProfileTemplateHtml+='<div class="col-md-4">';
                userProfileTemplateHtml+='<input type="text" class="form-control" id="id_mobileNo"  placeholder="Enter text" value="'+mobileNo+'">';
                userProfileTemplateHtml+='</div>';
                userProfileTemplateHtml+='</div>';
                userProfileTemplateHtml+='</div>';
                userProfileTemplateHtml+='<button type="button" class="btn btn-info" onClick="updateUserProfile();">Save</button>';
                userProfileTemplateHtml+='<button type="button" class="btn btn-default cls_btnspace" onClick="cancelProfileUpdate(\''+role1+'\')">Cancel</button>';
                userProfileTemplateHtml+='</form>';
                userProfileTemplateHtml+='</div>';
                userProfileTemplateHtml+='</div>';
                userProfileTemplateHtml+='</div>';
                userProfileTemplateHtml+='</div>';
                $('#id_usrprofile').html(userProfileTemplateHtml);
                $('#pleaseWaitDialog').modal('hide');
                App.init();
                TableAdvanced.init();
                UIExtendedModals.init();
                FormComponents.init();
                $('#pleaseWaitDialog').modal('hide');
            }
        }
    });
}
function updateUserProfile(){
    var firstName = document.getElementById("id_firstName").value.trim();
    if(firstName.trim().length==0){
        fieldSenseTosterError("Please enter first name",true);
        return false;
    }
    if(firstName.trim().length > 50){
        fieldSenseTosterError("First name can not be more than 50 characters",true);
        return false;
    }
    var lastName = document.getElementById("id_lastName").value.trim();
    if(lastName.trim().length==0){
        fieldSenseTosterError("Please enter last name",true);
        return false;
    }
    if(lastName.trim().length > 50){
        fieldSenseTosterError("Last name can not be more than 50 characters",true);
        return false;
    }
    var designation = document.getElementById("id_designation").value.trim();
    if(designation.trim().length==0){
        fieldSenseTosterError("Please enter designation ",true);
        return false;
    }
    if(designation.trim().length > 50){
        fieldSenseTosterError("Designation can not be more than 50 characters",true);
        return false;
    }
    var gender=$( "input:radio[name=id_gender]:checked" ).val();
    var mobileNo = document.getElementById("id_mobileNo").value.trim();
    var isphNo1 = /^\d+$/.test(mobileNo);
    if(!isphNo1){
        fieldSenseTosterError("Invalid Mobile number.",true);
        return false;
    }
    if(mobileNo.trim().length==0){
        fieldSenseTosterError("Please enter mobile number",true);
        return false;
    }
    firstName = htmlEntities(firstName);
    lastName = htmlEntities(lastName);
    designation = htmlEntities(designation);
    $('#pleaseWaitDialog').modal('show');
    var userObject={
        "id":loginUserId,
        "firstName":firstName,
        "lastName":lastName,
        "gender":gender,
        "mobileNo":mobileNo,
        "designation":designation,
	"emailAddress":$("#inputEmail1").val()
    }
    var jsonData = JSON.stringify( userObject );
    $.ajax({
        type: "PUT",
        url: fieldSenseURL+"/user/update",
        contentType: "application/json; charset=utf-8",
        headers:{
            "userToken": userToken
        },
        crossDomain: false,
        data : jsonData,
        cache: false,
        dataType: 'json',
        success: function(data) {
            if(data.errorCode=='0000'){
                var name=firstName+" "+lastName;
                $('#id_userNmaeRightCorner').html(name);
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterSuccess(data.errorMessage,true);
            }else{
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage,true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}
function cancelProfileUpdate(userRole){
    //    if(userRole==1){
    if(userMode==1){
        window.location.href ="adminUser.html";
    }else if(userMode==2){
        window.location.href ="dashboard.html";
    }
//    }
//    if((userRole==5)){
//        window.location.href ="dashboard.html";
//    }
}

