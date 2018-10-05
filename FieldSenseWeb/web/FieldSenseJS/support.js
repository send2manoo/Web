/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * @author Ramesh
 * @date 09-06-2014
 */
var userToken=fieldSenseGetCookie("userToken");
var intervalAdminUser = window.setInterval(function () {
    var userToken2 = fieldSenseGetCookie("userToken");
    try {
        if (userToken2.toString() == "undefined") {
            window.location.href = "login.html";
        } else if (loginUserId != 0) {
            if(role==0){
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

function createAcoount(){

    var companyName=document.getElementById("id_companyName").value.trim();
    if(companyName.length==0){
        fieldSenseTosterError("Company Name can't be empty .",true);
        return false;
    }
    var address1=document.getElementById("id_address1").value.trim();
    if(address1.length==0){
        fieldSenseTosterError("Address can't be empty .",true);
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
    var emailId=document.getElementById("id_email").value.trim();
    var emailPattern = /^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;
    if(!emailPattern.test(emailId)){
        fieldSenseTosterError("Invalid email address .",true);
        return false;
    }
    var password=document.getElementById("id_pwd").value;
    if(password.length < 8 || password.length > 20){
        fieldSenseTosterError("Password length should be 8 to 20 characters .",true);
        return false;
    }
    var confirmPassword=document.getElementById("id_conpwd").value.trim();
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
        "role":1,
        "active":1,
        "accountContactType":1
    }
    var accountObject={
        "companyName":companyName,
        "address1":address1,
        "city":city,
        "state":state,
        "country":country,
        "zipCode":zipCode,
        "companyPhoneNumber1":parseFloat(phNo1),
        "emailAddress":emailId,
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
                clearCreateAcconutData();
		$('#pleaseWaitDialog').modal('hide');
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
function logout(){
    $.ajax({
        type: "DELETE",
        url: fieldSenseURL+"/authenticate/0",
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
                deleteCookie("userToken");
                deleteCookie("role");
                window.location.href ="login.html";
            }else{
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}
function clearCreateAcconutData(){
    $('#id_companyName').val("");
    $('#id_address1').val("");
    $('#id_city').val("");
    $('#id_state').val("");
    $('#id_zipCode').val("");
    $("#select2_sample4").select2('val', '');
    $('#id_comPnNo1').val("");
    $('#id_email').val("");
    $('#id_pwd').val("");
    $('#id_conpwd').val("");
    $('#id_uname').val("");
    $('#id_designation').val("");
    $('input[name=id_gender]').prop('checked',false);
    App.initUniform();
    App.fixContentHeight();
}
function moveToStats(){
    window.location.href ="stats.html";
}
