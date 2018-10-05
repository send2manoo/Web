/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * @author Ramesh
 * @date 09-06-2014
 */
$(function(){
    var userToken2=fieldSenseGetCookie("userToken");
    if(userToken2!=null){
        var role=fieldSenseGetCookie("userRole");
        if(role==5){
            window.location.href ="dashboard.html";
        }else if(role==1){
            window.location.href ="adminUser.html"; // modified by jyoti, 04-09-2018
        }else if(role==0){
            window.location.href ="stats.html";
        }else if(role==2){
            window.location.href ="Dashboard_accounts.html";
        }else{
            loginTemplate(); 
        }
    }else{
        loginTemplate();
    }
});

var loginUserIdcookie = fieldSenseGetCookie("loginUserIdcookie");
var loginUserNamecookie = fieldSenseGetCookie("loginUserNamecookie");
var accountIdcookie = fieldSenseGetCookie("accountIdcookie");
var accountNamecookie = fieldSenseGetCookie("accountNamecookie");
var UserRolecookie = fieldSenseGetCookie("UserRolecookie");
console.log("1"+loginUserIdcookie+"2"+loginUserNamecookie+"3 "+accountIdcookie+"4 "+accountNamecookie+"5 "+UserRolecookie);
function loginTemplate(){
    deleteCookie("userToken");
    deleteCookie("userRole");
    var FSemail = $.jStorage.get("FSemail");
    var FSpwd = $.jStorage.get("FSpwd");
    var loginTemplateHtml='';
    loginTemplateHtml+='<form class="login-form" method="POST" action="javascript:teamLeadLogin();retun false;">';
    loginTemplateHtml+='<h3 class="form-title">Login </h3>';
    loginTemplateHtml+='<div class="alert alert-error display-hide">';
    loginTemplateHtml+='<button class="close" data-close="alert"></button>';
    loginTemplateHtml+='<span>';
    loginTemplateHtml+='Enter any username and password.';
    loginTemplateHtml+='</span>';
    loginTemplateHtml+='</div>';
    loginTemplateHtml+='<div class="form-group">';
    loginTemplateHtml+='<!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->';
    loginTemplateHtml+='<label class="control-label visible-ie8 visible-ie9">Email Address</label>';
    loginTemplateHtml+='<div class="input-icon">';
    loginTemplateHtml+='<i class="fa fa-user"></i>';
    if(FSemail!=null){
        loginTemplateHtml+='<input class="form-control placeholder-no-fix" type="text" autocomplete="off" placeholder="Email Address / Mobile No" name="username" id="userEmailAddress" value="'+FSemail+'"/>';
    }else{
        loginTemplateHtml+='<input class="form-control placeholder-no-fix" type="text" autocomplete="off" placeholder="Email Address / Mobile No" name="username" id="userEmailAddress"/>';
    }
    loginTemplateHtml+='</div>';
    loginTemplateHtml+='</div>';
    loginTemplateHtml+='<div class="form-group">';
    loginTemplateHtml+='<label class="control-label visible-ie8 visible-ie9">Password</label>';
    loginTemplateHtml+='<div class="input-icon">';
    loginTemplateHtml+='<i class="fa fa-lock"></i>';
    if(FSpwd!=null){
        loginTemplateHtml+='<input class="form-control placeholder-no-fix" type="password" autocomplete="off" placeholder="Password" name="password" id="userPassword" value="'+FSpwd+'"/>';
    }else{
        loginTemplateHtml+='<input class="form-control placeholder-no-fix" type="password" autocomplete="off" placeholder="Password" name="password" id="userPassword"/>';
    }
    loginTemplateHtml+='</div>';
    loginTemplateHtml+='</div>';
    loginTemplateHtml+='<div class="form-actions">';
    loginTemplateHtml+='<label class="checkbox">';
    if(FSemail!=null){
        loginTemplateHtml+='<input type="checkbox" name="remember" value="1" id="id_rememberunameandpwd"/ checked> Remember me </label>';
    }else{
        loginTemplateHtml+='<input type="checkbox" name="remember" value="1" id="id_rememberunameandpwd"/> Remember me </label>';
    }
    loginTemplateHtml+='<button type="submit" class="btn btn-info pull-right" onClick="teamLeadLogin();return false;">';
    loginTemplateHtml+='Login </button>';
    loginTemplateHtml+='</div>';
    loginTemplateHtml+='<div class="forget-password">';
    loginTemplateHtml+='<h4>Forgot your password ?</h4>';
    loginTemplateHtml+='<p>';
    loginTemplateHtml+='no worries, click <a href="'+fieldSenseWEBURL+'/forgotPassword.html" id="forget-password"> here</a>';
    loginTemplateHtml+=' to reset your password.';
    loginTemplateHtml+='</p>';
    loginTemplateHtml+='</div>';
    loginTemplateHtml+='</form>';
    $('#id_login').html(loginTemplateHtml);
    var userEmailAddress = document.getElementById("userEmailAddress");
    userEmailAddress.addEventListener("keydown", function (e) {
        if (e.keyCode === 13) {
            teamLeadLogin();
        }
    });
    var userPassword = document.getElementById("userPassword");
    userPassword.addEventListener("keydown", function (e) {
        if (e.keyCode === 13) {
            teamLeadLogin();
        }
    });
}
var count1=0;
var count2=0;
function teamLeadLogin(){
    if(count1!=count2) 
	return;

	count1++;

    var emailId=document.getElementById("userEmailAddress").value.trim();
    if(emailId.trim("").length==0){
        fieldSenseTosterError("Please enter valid email address/mobile number.",true);
        return false;
    }
    var emailPattern = /^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;
    var phonpatt=/^[0-9]/;
    if(!emailPattern.test(emailId)){
	if(phonpatt.test(emailId)){
		if(emailId.length>20){
			fieldSenseTosterError("Invalid mobile number .",true);
		}/*else if(emailId.length==10){
			emailId="91"+emailId;
		}*/
	}else{
        fieldSenseTosterError("Invalid email address/mobile number .",true);
        return false;
	}
    }
    var password=document.getElementById("userPassword").value;
    if(password.trim("").length==0){
        fieldSenseTosterError("Password cannot be blank",true);
        return false;
    }
    if(password.length < 8 || password.length > 20){
        fieldSenseTosterError("Password length should be 8 to 20 characters .",true);
        return false;
    }
    var userObject= {
        "userEmailAddress":emailId,
        "password":password
    };
    var rememberme=document.getElementById("id_rememberunameandpwd").checked;
    if(rememberme){ 
        $.jStorage.set("FSemail",emailId);
        $.jStorage.set("FSpwd",password);
    }else{
        $.jStorage.deleteKey('FSemail');
        $.jStorage.deleteKey('FSpwd');
    }
	
    var jsonData = JSON.stringify( userObject );
         clevertap.event.push("Login", {
                     
//                    "UserId": loginUserIdcookie,
//                    "User Name": loginUserNamecookie,
//                    "AccountId": accountIdcookie,
                        "Source": "Web",
                        "Account name": accountNamecookie,
                        "UserRolecookie": UserRolecookie,
                    });
    $.ajax({
        type: "POST",
        url: fieldSenseURL+"/authenticate/TL",
        contentType: "application/json; charset=utf-8",
        crossDomain: false,
        data:jsonData,
        cache: false,
        dataType: 'json',
        asyn:false,
        success: function(data) {
        
            if (data.errorCode=='0000') {
//                alert("ek bar");
                
                var authData = data.result;
//                alert(authData.gdprFlag);userEmailAddress
//                alert(authData.userEmailAddress);
                var gdprFlag = authData.is_terms_condition_agreed;
//                  alert(gdprFlag);
                var role = authData.role;
                var userToken=data.result.userToken;
                var isFirstLogin=authData.isFirstLogin;
                var EmailAddress = authData.userEmailAddress;
                var userFirstName = authData.userFirstName;
                var userLastName = authData.userLastName;
                var teamId=authData.teamId; 
//                alert("userFirstName "+userFirstName);
//                 alert("userLastName "+userLastName);
//                userFirstName  userLastName
//                alert();
                document.cookie="userToken="+userToken;
                document.cookie="userRole="+role;
                document.cookie="userEmailAddress="+EmailAddress;
                document.cookie="isFirstLogin="+isFirstLogin;
                document.cookie="userFirstName="+userFirstName;
                document.cookie="userLastName="+userLastName;
                document.cookie="teamId="+teamId;
//                alert("gdprFlag "+gdprFlag)
                if (gdprFlag == '2')
                {                  
                    window.location.href ="GDPR.html";                   
                }
                else{
                if(role==0){
                    	window.location.href ="stats.html";
                }else if(role==2 || role==7){    // modified by manohar 
			document.cookie="userMode="+2;
			window.location.href ="dashboard.html";  // modified by manohar
		} 
//                else if(role == 1 || role == 3 || role == 6 || role == 8){ // added by jyoti, 04-09-2018
//                    document.cookie="userMode="+1;
//                    window.location.href ="adminUser.html";
//                }
                else{
                    if(isFirstLogin){
                        document.cookie="userMode="+1;
                        window.location.href ="adminUser.html";
                    }else{
                        document.cookie="userMode="+2;
                        window.location.href ="dashboard.html";
                    }
                }
            }}else{
                fieldSenseTosterError(data.errorMessage,true);
            }
        }
    });
count2++;
}

function verifyOTP(){
        var mobile=$("#id_mobile").val().trim();
        var otpkey=$("#id_otpkey").val().trim();
        if(otpkey==""){
            fieldSenseTosterError("Please Enter OTP",true);
            return false;
        }

	    var newPass = document.getElementById("idotp_password").value;
	    newPass=newPass.trim();
	    if(newPass.length==0){
		fieldSenseTosterError("Password cannot be empty .",true);
		return false;
	    }
	    if(newPass.length < 8 || newPass.length > 20){
		fieldSenseTosterError("New password length should be 8 to 20 characters .",true);
		return false;
	    }
	    var conPass = document.getElementById("id_cnfpassword").value;
	    conPass=conPass.trim();
	    if(newPass!=conPass){
		fieldSenseTosterError("Confirm password does not match with password .",true);
		return false;
	    }
       var data= {
			"mobile": mobile ,
			"otpkey":otpkey,
 			"newPassword":newPass
                        };
    var authObject = JSON.stringify( data );

       $.ajax({
        type: "POST",
        url: fieldSenseURL+"/password/verifyOTP",
        contentType: "application/json; charset=utf-8",
        crossDomain: false,
        data:authObject,
        cache: false,
        dataType: 'json',
        asyn:false,
        success: function(data) {
            //$('#id_btn_SubmitOTP').removeAttr('disabled');
            if (data.errorCode=='0000') {
                window.location.href ="login.html";
                fieldSenseTosterSuccess(data.errorMessage,true);
            }else{
                fieldSenseTosterError(data.errorMessage,true);
            }
        }
      });


}

function forgotPassword(){
     clevertap.event.push("Forgot Password", {
//                    "UserId": loginUserIdcookie,
//                    "User Name": loginUserNamecookie,
//                    "AccountId": accountIdcookie,
                        "Source": "Web",
                        "Account name": accountNamecookie,
                        "UserRolecookie": UserRolecookie,
                    });
        

    var email = document.getElementById("id_email").value.trim();
 //  alert("forgot "+email)
    if(email ==""){
        fieldSenseTosterError("Please enter valid email address / mobile number.",true);
        return false;
    }

    //Added by Awaneesh for otp  
    
    if (email.indexOf("@")==-1){
        
        var mobPattern = /^[1-9]{1}[0-9]{9}$/;
        if(email.length!=10 || !mobPattern.test(email)){                 //Currently OTP is only in India so just 10 digit number
                  fieldSenseTosterError("Invalid email address / mobile number .",true);
                  return false;
        } 
        // Now email means phone number 
	//var jsonData1 = JSON.stringify( forgotPasswordObject );
             
            $("#id_btn_forgotpassword").attr("disabled","true");
	    $.ajax({
		type: "GET",
		url: fieldSenseURL+"/password/getOTP/"+email,
		contentType: "application/json; charset=utf-8",
		crossDomain: false,
		//data:jsonData1,
		cache: false,
		dataType: 'json',
		asyn:false,
		success: function(data) {
		    //$('#id_btn_forgotpassword').removeAttr('disabled');
		    if (data.errorCode=='0000') {
                        
                        
		        //fieldSenseTosterSuccess(data.errorMessage,true);
                         var otpTemplate= '<form class="forget-form"  method="post" action="javascript:verifyOTP();retun false;">';
                	    otpTemplate+='<h3>Reset Password</h3>';
                	    otpTemplate+='<p> Enter the OTP sent on your mobile number and reset the password</p>';
                	    otpTemplate+='<div class="form-group">';
                    	    otpTemplate+='<div class="input-icon">';
                            otpTemplate+='<i class="fa fa-mobile"></i>';
                            otpTemplate+='<input type="hidden" id="id_mobile" value="'+email+'"/>';
                            otpTemplate+='<input class="form-control placeholder-no-fix" type="text" autocomplete="off" placeholder="Enter OTP here" value="" name="otpkey" id="id_otpkey"/>';
                            otpTemplate+='</div>';
                            otpTemplate+='</div>';
                	    otpTemplate+='<div class="form-group">';
                    	    otpTemplate+='<div class="input-icon">';
                            otpTemplate+='<i class="fa fa-lock"></i>';
                            otpTemplate+='<input class="form-control placeholder-no-fix" type="password" autocomplete="off" placeholder="Enter Password" value="" name="password" id="idotp_password"/>';
                            otpTemplate+='</div>';
                            otpTemplate+='</div>';
                	    otpTemplate+='<div class="form-group">';
                    	    otpTemplate+='<div class="input-icon">';
                            otpTemplate+='<i class="fa fa-lock"></i>';
                            otpTemplate+='<input class="form-control placeholder-no-fix" type="password" autocomplete="off" placeholder="Confirm Password" value="" name="password" id="id_cnfpassword"/>';
                            otpTemplate+='</div>';
                	    otpTemplate+='</div>';
                            otpTemplate+=' <div class="form-actions">';
                    	    otpTemplate+='<button type="button" id="back-btn" class="btn btn-default" onclick="window.location=\'forgotPassword.html\';">';
                            otpTemplate+='<i class="m-icon-swapleft"></i> Back </button>';
                    	    otpTemplate+='<button type="submit" id="id_btn_SubmitOTP" class="btn btn-info pull-right" onclick="verifyOTP();return false;">';
                            otpTemplate+='Submit </button>';
                	    otpTemplate+='</div>';
            		    otpTemplate+='</form>';
			$('.content').html('');
                        $('.content').html(otpTemplate);


		    }else{
		        fieldSenseTosterError(data.errorMessage,true);
		    } 
		},
             	error: function(xhr,ajaxOptions,thrownError) {
                  alert(thrownError);
               }
	    });
          $("#id_btn_forgotpassword").removeAttr("disabled");
          return false;
    }
    //End By Awaneesh

    var emailPattern = /^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;

    if(!emailPattern.test(email)){
        fieldSenseTosterError("Invalid email address .",true);
        return false;
    }
    $('#id_btn_forgotpassword').attr('disabled','disabled');
    var forgotPasswordObject = {
        "emailId":email
    }
    var jsonData = JSON.stringify( forgotPasswordObject );
    $.ajax({
        type: "POST",
        url: fieldSenseURL+"/password/forgot",
        contentType: "application/json; charset=utf-8",
        crossDomain: false,
        data:jsonData,
        cache: false,
        dataType: 'json',
        asyn:false,
        success: function(data) {
            $('#id_btn_forgotpassword').removeAttr('disabled');
            if (data.errorCode=='0000') {
                fieldSenseTosterSuccess(data.errorMessage,true);
            }else{
                fieldSenseTosterError(data.errorMessage,true);
            }
        }
    });
}
var forgotPwd = document.getElementById("id_email");
if(forgotPwd!=null){
	forgotPwd.addEventListener("keydown", function (e) {
	    if (e.keyCode === 13) {
		forgotPassword();
	    }
	});
}
