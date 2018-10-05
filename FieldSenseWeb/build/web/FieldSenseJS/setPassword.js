/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//var fieldSenseURL="http://api.fieldsense.in";
//var fieldSenseURL='../FieldSense';
resetPasswordTemplate();
function resetPasswordTemplate(){
    var path=document.URL;
    var emailIdSplit = path.split("email=");
    var email = emailIdSplit[1];
    var resetPasswordTemplateHtml='';
    resetPasswordTemplateHtml+='<form class="login-form" method="POST" action="javascript:resetPassword();retun false;">';
    resetPasswordTemplateHtml+='<h3 class="form-title">Set Password </h3>';
    resetPasswordTemplateHtml+='<div class="alert alert-error display-hide">';
    resetPasswordTemplateHtml+='<button class="close" data-close="alert"></button>';
    resetPasswordTemplateHtml+='<span>';
    resetPasswordTemplateHtml+='Enter any username and password.';
    resetPasswordTemplateHtml+='</span>';
    resetPasswordTemplateHtml+='</div>';
    resetPasswordTemplateHtml+='<div class="form-group">';
    resetPasswordTemplateHtml+='<label class="control-label visible-ie8 visible-ie9">Email</label>';
    resetPasswordTemplateHtml+='<div class="input-icon">';
    resetPasswordTemplateHtml+='<i class="fa fa-lock"></i>';
    resetPasswordTemplateHtml+='<input class="form-control placeholder-no-fix" type="text" autocomplete="off" placeholder="Email" id="id_email" value="'+email+'" disabled/>';
    resetPasswordTemplateHtml+='</div>';
    resetPasswordTemplateHtml+='</div>';
    resetPasswordTemplateHtml+='<div class="form-group">';
    resetPasswordTemplateHtml+='<label class="control-label visible-ie8 visible-ie9">Password</label>';
    resetPasswordTemplateHtml+='<div class="input-icon">';
    resetPasswordTemplateHtml+='<i class="fa fa-lock"></i>';
    resetPasswordTemplateHtml+='<input class="form-control placeholder-no-fix" type="password" autocomplete="off" placeholder="New Password" id="id_password"/>';
    resetPasswordTemplateHtml+='</div>';
    resetPasswordTemplateHtml+='</div>';
    resetPasswordTemplateHtml+='<div class="form-group">';
    resetPasswordTemplateHtml+='<label class="control-label visible-ie8 visible-ie9">Confirm Password</label>';
    resetPasswordTemplateHtml+='<div class="input-icon">';
    resetPasswordTemplateHtml+='<i class="fa fa-lock"></i>';
    resetPasswordTemplateHtml+='<input class="form-control placeholder-no-fix" type="password" autocomplete="off" placeholder="Confirm Password" id="id_cPassword"/>';
    resetPasswordTemplateHtml+='</div>';
    resetPasswordTemplateHtml+='</div>';
    resetPasswordTemplateHtml+='<div class="form-actions" style="border-bottom: 0px !important;">';
    resetPasswordTemplateHtml+='<button type="submit" class="btn btn-info pull-right" onClick="resetPassword();return false;"> Submit </button>';
    resetPasswordTemplateHtml+='</div>';
    resetPasswordTemplateHtml+='</form>';
    $('#id_setPassword').html(resetPasswordTemplateHtml);
    var passWord = document.getElementById("id_password");
    passWord.addEventListener("keydown", function (e) {
        if (e.keyCode === 13) {
            resetPassword();
        }
    });
    var cPassword = document.getElementById("id_cPassword");
    cPassword.addEventListener("keydown", function (e) {
        if (e.keyCode === 13) {
            resetPassword();
        }
    });
    $('#id_password').focus();
}
function resetPassword(){
    var email=document.getElementById("id_email").value;
    email=email.trim();
    var emailPattern = /^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;
    if(!emailPattern.test(email)){
        fieldSenseTosterError("Invalid email address .",true);
        return false;
    }
    var newPass = document.getElementById("id_password").value;
    newPass=newPass.trim();
    if(newPass.length==0){
        fieldSenseTosterError("Password cannot be empty .",true);
        return false;
    }
    if(newPass.length < 8 || newPass.length > 20){
        fieldSenseTosterError("New password length should be 8 to 20 characters .",true);
        return false;
    }
    var conPass = document.getElementById("id_cPassword").value;
    conPass=conPass.trim();
    if(newPass!=conPass){
        fieldSenseTosterError("Confirm password does not match with password .",true);
        return false;
    }
    var resetPassword = {
        "emailId": email,
        "newPassword": newPass,
        "confirmPassword": conPass
    }
    var jsonData = JSON.stringify( resetPassword );
    $.ajax({
        type: "POST",
        url: fieldSenseURL+"/password/reset",
        contentType: "application/json; charset=utf-8",
        crossDomain: false,
        data:jsonData,
        cache: false,
        dataType: 'json',
        asyn:true,
        success: function(data) {
            if (data.errorCode=='0000') {
                window.location.href ="login.html";
                fieldSenseTosterSuccess(data.errorMessage, true);
            }else{
                fieldSenseTosterError(data.errorMessage, true);
            }
        }
    });
}
            

