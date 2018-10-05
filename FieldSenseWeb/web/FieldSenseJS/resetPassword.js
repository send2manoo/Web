/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
$(function(){
    resetTemplate();
});

function resetTemplate(){
    var resetTemplate='';
    resetTemplate+='<form class="login-form" method="POST" action="javascript:resetPassword();return false;">';
    resetTemplate+='<h3 class="form-title">Reset Password </h3>';
    resetTemplate+='<div class="alert alert-error display-hide">';
    resetTemplate+='<button class="close" data-close="alert"></button>';
    resetTemplate+='<span>';
    resetTemplate+='Enter any username and password.';
    resetTemplate+='</span>';
    resetTemplate+='</div>';
    resetTemplate+='<div class="form-group">';
    resetTemplate+='<label class="control-label visible-ie8 visible-ie9">Password</label>';
    resetTemplate+='<div class="input-icon">';
    resetTemplate+='<i class="fa fa-lock"></i>';
    resetTemplate+='<input class="form-control placeholder-no-fix" type="password" autocomplete="off" placeholder="New Password" name="password" id="id_password"/>';
    resetTemplate+='</div>';
    resetTemplate+='</div>';
    resetTemplate+='<div class="form-group">';
    resetTemplate+='<label class="control-label visible-ie8 visible-ie9">Confirm Password</label>';
    resetTemplate+='<div class="input-icon">';
    resetTemplate+='<i class="fa fa-lock"></i>';
    resetTemplate+='<input class="form-control placeholder-no-fix" type="password" autocomplete="off" placeholder="Confirm Password" name="password" id="id_cPassword"/>';
    resetTemplate+='</div>';
    resetTemplate+='</div>';
    resetTemplate+='<div class="form-actions" style="border-bottom: 0px !important;">';
    resetTemplate+='<button type="submit" class="btn btn-info pull-right" onclick="resetPassword();return false;">Submit </button>';
    resetTemplate+='</div>';
    resetTemplate+='</form>';
    $('#id_resetTemplate').html(resetTemplate);
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
    var path=document.URL;
    var emailIdSplit = path.split("email=");
    var email = emailIdSplit[1];
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