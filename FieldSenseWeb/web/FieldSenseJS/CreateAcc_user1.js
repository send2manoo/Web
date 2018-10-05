/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var userToken = 'demouserToken';
var dateOfDefaultExpiry;
var currentDate;
var currentDateFormat;
var expiryDateFormat;
function fieldSenseTosterSuccess1(message, isClosable) {
    toastr.options = {
        "closeButton": isClosable,
        "debug": false,
        "positionClass": "toast-top-center",
        "onclick": null,
        "showDuration": "1000",
        "hideDuration": "1000",
        "timeOut": "500000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    }
    toastr.success(message);
}
function dateConverter(dateBasicformat) {
    var dateSplit = dateBasicformat.split("/");
    var date = dateSplit[0];
    var month = dateSplit[1];
    var year = dateSplit[2];
    var toDate = convertLocalDateToServerDate(year, month - 1, date, 23, 59);
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
function expiryDates(month)
{
    currentDate = new Date();
    //  alert("month : "+month);
    var dd = currentDate.getDate();
    var mm = currentDate.getMonth() + 1; //January is 0!
    var yyyy = currentDate.getFullYear();

    if (dd < 10) {
        dd = '0' + dd;
    }

    if (mm < 10) {
        mm = '0' + mm;
    }

    currentDate = dd + '/' + mm + '/' + yyyy;
    mm = mm - 1;
    dateOfDefaultExpiry = new Date(yyyy, mm, dd);
    dateOfDefaultExpiry = new Date(new Date(dateOfDefaultExpiry).setMonth(dateOfDefaultExpiry.getMonth() + month));
    dd = dateOfDefaultExpiry.getDate() - 1;
    mm = dateOfDefaultExpiry.getMonth() + 1;
    yyyy = dateOfDefaultExpiry.getFullYear();
    if (dd < 10) {
        dd = '0' + dd;
    }

    if (mm < 10) {
        mm = '0' + mm;
    }
    dateOfDefaultExpiry = dd + '/' + mm + '/' + yyyy;
    console.log("currentDate : " + currentDate + "dateOfDefaultExpiry :  " + dateOfDefaultExpiry);
//    alert("inside free annual expiry");
    currentDateFormat = dateConverter(currentDate);
    expiryDateFormat = dateConverter(dateOfDefaultExpiry);

}
function setRadioValue()
{
    var gender = $("input:radio[name=gender]:checked").val();
    console.log("gender continue " + gender);
    if (gender == "1")
    {
        document.getElementById("gender_name1").value = "Male";
        console.log("if " + document.getElementById("gender_name1").value);
    } else if (gender == "0")
    {
        document.getElementById("gender_name1").value = "Female";
        console.log("else " + document.getElementById("gender_name1").value);
    }



//    console.log("region selecred :- " + $("#region_id1 option:selected").text());
//
//    var region_val = $("#region_id1 option:selected").text();
//    document.getElementById("region_name1").value = region_val;
//    console.log("region_val :" + region_val);

}

var ToasterTimeOut = true;
function createAccOnline()
{

    //  var button = $('#get_start_button');
    //  button.prop('disabled', true);
    var userlimit;
    console.log("username_name new  " + document.getElementById("username_id1").value.trim());
    var userName = document.getElementById("username_id1").value.trim();
    var companyName = document.getElementById("company_name_id1").value.trim();
    var address1 = document.getElementById("address_id1").value.trim();
//    var regionId = document.getElementById("region_id1").options[document.getElementById("region_id1").selectedIndex].value;
     var indexOfCountry = document.getElementById("country_list");
    var country = indexOfCountry.options[indexOfCountry.selectedIndex].text;
    var indexOfIndustry = document.getElementById("Industry_list");
    var Industry = indexOfIndustry.options[indexOfIndustry.selectedIndex].value;
//    alert("Industry"+Industry);
 if (country === 'India')
 {
     //state_id2
     var indexofState = document.getElementById("state_id1");
     var state=indexofState.options[indexofState.selectedIndex].value;
    // alert("state:-india* --- "+state);
     
 }else
 {
     var state = document.getElementById("state_id1").value.trim();
    // alert("state:-world --- "+state);
 }
    var city = document.getElementById("city_id1").value.trim();
    
   
    var zipCode = document.getElementById("zip_id1").value.trim();
    var isZipCode = /^\d+$/.test(zipCode);
    if (!isZipCode) {
        fieldSenseTosterError("Invalid zipcode.", true);
        return false;
    }

    var mobile = document.getElementById("mobile_id1").value.trim();
    var ismbNo1 = /^\d+$/.test(mobile);
    if (!ismbNo1) {
        fieldSenseTosterError("Invalid mobile number.", true);
        return false;
    }
//    designation_id1
    var designation = document.getElementById("designation_id1").value.trim();
    // var emailId = decoded_email;
    var emailId = document.getElementById("email_id1").value.trim();

    if (emailId.length == 0) {
        fieldSenseTosterError("Email cannot be empty", true);
        return false;
    }
    if (emailId.length > 100) {
        fieldSenseTosterError("Email address can not be more than 100 characters", true);
        return false;
    }
    var emailPattern = /^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;
    if (!emailPattern.test(emailId.trim())) {
        fieldSenseTosterError("Invalid email address .", true);
        return false;
    }
    var website = document.getElementById("website_id1").value.trim();
     var re1 = /^(?:(?:https?|ftp):\/\/)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})))(?::\d{2,5})?(?:\/\S*)?$/;
     if (!re1.test(website)) { 
    fieldSenseTosterError("Invalid website name.", true);
    return false;
}
    var password = document.getElementById("submit_form_password").value;
    var phNo1 = document.getElementById("phone_id1").value;
    var isphNo1 = /^\d+$/.test(phNo1);
    if (!isphNo1) {
        fieldSenseTosterError("Invalid phone number.", true);
        return false;
    }
    //  var designation = document.getElementById("id_designation").value.trim();
    //website_id1
    var gender = $("input:radio[name=gender]:checked").val();
    if (gender == undefined) {
        fieldSenseTosterError("Please select the gender.", true);
        return false;
    }
//    var plan = document.getElementsByName('radio-input');
    var plan_selected = $('input[name="radio-input"]:checked').val();
//   if($('input[name="radio-input"]:checked'))
//   {
//     var userlimi = $('input[name="radio-input"]').closest("").find("input[type='text']").val();
//     console.log("userlimi :-"+userlimi);
//   }

    var plan;
    var userValidat;
    var paymentCycle;
    if (plan_selected === 'free_monthly')
    {
        plan = 'Free';
        paymentCycle='Monthly';
        expiryDates(1);
        userlimit = 25;
        userValidat = /^\d+$/.test(userlimit);
        //plan_details
        document.getElementById("plan_details").value = "Free : Monthly";
        //  console.log("currentDate :Free " + currentDate + "dateOfDefaultExpiry :Free  " + dateOfDefaultExpiry +"userlimit"+userlimit);

    } else if (plan_selected === 'free_annual')
    {
        plan = 'Free'
        paymentCycle='Annual';
        expiryDates(12);
        userlimit = 25;
//        alert("inside free annual");
        //   console.log("currentDate: free annual " + currentDate + "dateOfDefaultExpiry :free annual  " + dateOfDefaultExpiry +"userlimit"+userlimit);
        userValidat = /^\d+$/.test(userlimit);
        document.getElementById("plan_details").value = "Free : Annual";

    } else if (plan_selected === 'premium_monthly')
    {
        plan = 'Premium';
        paymentCycle='Monthly';
        expiryDates(1);
        userlimit = document.getElementById("id_userlimit_monthly").value;
        console.log("userlimit --- " + userlimit);
//        if (userlimit === "")
//        {
//            userlimit = 25;
//        }
        if (userlimit < 25)
        {
            fieldSenseTosterError("No.of Users must be greater than 25.", true);
            return false;
        }
        //   console.log("currentDate :Premium monthly " + currentDate + "dateOfDefaultExpiry :Premium  " + dateOfDefaultExpiry +"userlimit"+userlimit);
        userValidat = /^\d+$/.test(userlimit);
        document.getElementById("plan_details").value = "Premium : Monthly";

    } else if (plan_selected === 'premium_annual')
    {
        plan = 'Premium';
        paymentCycle='Annual';
        expiryDates(12);
        userlimit = document.getElementById("id_userlimit_annual").value;
//        if (userlimit === "")
//        {
//            userlimit = 25;
//        }
         if (userlimit < 25)
        {
            fieldSenseTosterError("No.of Users must be greater than 25.", true);
            return false;
        }
        // console.log("currentDate :Premium annual " + currentDate + "dateOfDefaultExpiry : annual  " + dateOfDefaultExpiry +"userlimit"+userlimit);
        userValidat = /^\d+$/.test(userlimit);
        document.getElementById("plan_details").value = "Premium : Annual";
    }
    if (!userValidat) {
        fieldSenseTosterError("Invalid No. of Users", true);
        return false;
    }
    var allowOffline_edit = 1;
    console.log("plan.value " + plan);

    console.log(companyName);
    console.log(address1);
//    console.log(regionId)
    console.log(city);
    console.log(state);
    console.log(country);
    console.log(zipCode);
    console.log(mobile);
    console.log(emailId);
    console.log(password);
    console.log("gender " + gender);
    console.log(userlimit);
    console.log("currentDateFormat : " + currentDateFormat);
    console.log("expiryDateFormat : " + expiryDateFormat);
    var userObject = [{
            //  "id":1,
            "firstName": userName,
             "designation": designation,
            "emailAddress": emailId,
            "password": password,
            "Re-type Password": password,
            // "designation":"",
            "mobileNo": mobile,
            "gender": gender,
            "role": 1,
//            "active": 1,
//            "accountContactType": 1
        }]
    console.log(JSON.stringify(userObject));
    var accountObject = {
        "plan": plan,
        "onlineCreation": true,
        "companyName": companyName,
        "userLimit": userlimit,
        //"regionId": regionId,
        "address1": address1,
        "city": city,
        "state": state,
        "country": country,
        "zipCode": zipCode,
        "companyPhoneNumber1": parseFloat(phNo1),
        //"emailAddress": emailId,
        "companyWebsite": website,
        "strStartDate": currentDateFormat,
        "sExpiredOn": expiryDateFormat,
//        "user": userObject
        "adminuser": userObject,
        "status": 1,
        "paymentCycle":paymentCycle,
        "industry":Industry
    }
    var userObject = {
        "userEmailAddress": emailId,
        "password": password
    };
//    console.log("currentDate "+currentDate);
//    console/log("dateOfDefaultExpiry "+dateOfDefaultExpiry);
    var jsonData = JSON.stringify(accountObject);
    var jsonDataLogin = JSON.stringify(userObject);
    console.log(jsonData);
    $('#pleaseWaitDialog').modal('show');
    //  fieldSenseTosterSuccess1("Please wait your account creation is in the process.", true);
    $.ajax({
        type: "POST",
        url: fieldSenseURL + "/account",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        asyn: false,
        success: function (data) {
            //   button.prop('disabled', false);
            $('#pleaseWaitDialog').modal('hide');
            if (data.errorCode == '0000') {
                console.log("inside success");
                fieldSenseTosterSuccess(data.errorMessage + "Redirecting...", true);
                //      $('#pleaseWaitDialog').modal('hide');
                window.setTimeout(6000);
                console.log("data.result.id:- "+data.result.id);
                var accountID =data.result.id;
                  $.ajax({
                    type: "POST",
                    url: fieldSenseURL + "/authenticate/TL",
                    contentType: "application/json; charset=utf-8",
                    crossDomain: false,
                    data: jsonDataLogin,
                    cache: false,
                    dataType: 'json',
                    asyn: false,
                    success: function (data) {

                        if (data.errorCode == '0000') {
//                alert("ek bar");

                            var authData = data.result;
                            var role = authData.role;
                            var userToken = data.result.userToken;
                            var isFirstLogin = authData.isFirstLogin;
                            document.cookie = "userToken=" + userToken;
                            document.cookie = "userRole=" + role;
                            if (role == 0) {
                                window.location.href = "stats.html";
                            } else if (role == 2) {
                                document.cookie = "userMode=" + 3;
                                window.location.href = "Dashboard_accounts.html";
                            } else {
                                if (isFirstLogin) {
                                    document.cookie = "userMode=" + 1;
                                    window.location.href = "adminUser.html";
                                } else {
                                    document.cookie = "userMode=" + 2;
                                    window.location.href = "dashboard.html";
                                }
                            }
                             var accountSettingsObject = {
                    "id": accountID,
                    "allowOffline": allowOffline_edit
                }
                         var jsonData = JSON.stringify(accountSettingsObject);
                $.ajax({
                    type: "PUT",
                    url: fieldSenseURL + "/account/settings/values/" + accountID,
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
                        console.log("account setting succesfull")
                    }

                });     
                        } else {
                            fieldSenseTosterError(data.errorMessage, true);
                        }
                    }
                });
 //                var accountSettingsObject = {
//                    "id": data.result.id,
//                    "allowOffline": allowOffline_edit
//                }
//
//                var jsonData = JSON.stringify(accountSettingsObject);
//                $.ajax({
//                    type: "PUT",
//                    url: fieldSenseURL + "/account/settings/values/" + data.result.id,
//                    headers: {
//                        "userToken": userToken
//                    },
//                    contentType: "application/json; charset=utf-8",
//                    crossDomain: false,
//                    data: jsonData,
//                    cache: false,
//                    dataType: 'json',
//                    asyn: false,
//                    success: function (data) {
////                         $.ajax({
////                    type: "POST",
////                    url: fieldSenseURL + "/authenticate/TL",
////                    contentType: "application/json; charset=utf-8",
////                    crossDomain: false,
////                    data: jsonDataLogin,
////                    cache: false,
////                    dataType: 'json',
////                    asyn: false,
////                    success: function (data) {
////
////                        if (data.errorCode == '0000') {
//////                alert("ek bar");
////
////                            var authData = data.result;
////                            var role = authData.role;
////                            var userToken = data.result.userToken;
////                            var isFirstLogin = authData.isFirstLogin;
////                            document.cookie = "userToken=" + userToken;
////                            document.cookie = "userRole=" + role;
////                            if (role == 0) {
////                                window.location.href = "stats.html";
////                            } else if (role == 2) {
////                                document.cookie = "userMode=" + 3;
////                                window.location.href = "Dashboard_accounts.html";
////                            } else {
////                                if (isFirstLogin) {
////                                    document.cookie = "userMode=" + 1;
////                                    window.location.href = "adminUser.html";
////                                } else {
////                                    document.cookie = "userMode=" + 2;
////                                    window.location.href = "dashboard.html";
////                                }
////                            }
////                        } else {
////                            fieldSenseTosterError(data.errorMessage, true);
////                        }
////                    }
////                });
//                    }
//
//                });

//                $.ajax({
//                    type: "POST",
//                    url: fieldSenseURL + "/authenticate/TL",
//                    contentType: "application/json; charset=utf-8",
//                    crossDomain: false,
//                    data: jsonDataLogin,
//                    cache: false,
//                    dataType: 'json',
//                    asyn: false,
//                    success: function (data) {
//
//                        if (data.errorCode == '0000') {
////                alert("ek bar");
//
//                            var authData = data.result;
//                            var role = authData.role;
//                            var userToken = data.result.userToken;
//                            var isFirstLogin = authData.isFirstLogin;
//                            document.cookie = "userToken=" + userToken;
//                            document.cookie = "userRole=" + role;
//                            if (role == 0) {
//                                window.location.href = "stats.html";
//                            } else if (role == 2) {
//                                document.cookie = "userMode=" + 3;
//                                window.location.href = "Dashboard_accounts.html";
//                            } else {
//                                if (isFirstLogin) {
//                                    document.cookie = "userMode=" + 1;
//                                    window.location.href = "adminUser.html";
//                                } else {
//                                    document.cookie = "userMode=" + 2;
//                                    window.location.href = "dashboard.html";
//                                }
//                            }
//                        } else {
//                            fieldSenseTosterError(data.errorMessage, true);
//                        }
//                    }
//                });

                //clearFilters();
            } 
            else {
                console.log("inside success2");
                fieldSenseTosterError(data.errorMessage, true);
                $('#pleaseWaitDialog').modal('hide');
            }
        },
        error: function (xhr, status, error) {
            //        button.prop('disabled', false);
            console.log("inside success error");
            var err = eval("(" + xhr.responseText + ")");
            var message = err.fieldErrors[0].message;
            fieldSenseTosterError(message, true);
            $('#pleaseWaitDialog').modal('hide');
        }
    });

}
function redirect()
{
    window.location = "https://web.fieldsense.in/login.html";
}
 var verifiedBool;
function verify()
{
//        var OTP ="89399";otp_id1,mobile_id1
         var OTP =document.getElementById("otp_id1").value;
        var mobile_no=document.getElementById("mobile_id1").value;
        var email_add =document.getElementById("email_id1").value;
        if(OTP =="")
        {
           fieldSenseTosterError("Please enter OTP", true); 
           return false;
        }
//    console.log("currentDate "+currentDate);
//    console/log("dateOfDefaultExpiry "+dateOfDefaultExpiry);
    var jsonData = JSON.stringify(OTP);
//    alert("verified");
     $.ajax({
        type: "GET",
         url: fieldSenseURL + "/account/verify/?OTP="+OTP+"&mobile_no="+mobile_no+"&email_add="+email_add,
//         ?accname=" + accname + "&status=" + status 
// url: fieldSenseURL + "/customerContact/customer/" + OTP,
        contentType: "application/json; charset=utf-8",
          headers: {
                            "userToken": userToken
                        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn:true,
        success: function(data) {
//            alert("success");
          
            
            if(data.result == "success")
            {
                verifiedBool=true;
                fieldSenseTosterSuccess("OTP verified", true);
//                alert("OTP verified");
            }else if(data.result == "fail")
            {
                verifiedBool=false;
                fieldSenseTosterError("OTP doesn't match, please enter correct OTP", true);
//                alert("OTP doesn't match, please enter correct OTP");
            }else if(data.result == "Expired")
            {
                verifiedBool=false;
                fieldSenseTosterError("OTP Expired. Please click on Resend", true);
//                alert("OTP Expired, please resend OTP");
            }
            
           
           console.log("data.result:- "+data.result);
        }
    });
}
function verifyBool()
{
    if(verifiedBool)
//if(true)
    {
//    alert("verifyBool");
    return true;
}else
{
    fieldSenseTosterError("Please verify the OTP", true);
//    alert("Please verify the OTP");
//    throw new Error("Something went badly wrong!");
//jQuery('#continue_button').addClass('').removeClass('btn btn-info button-next');
    return false;
}
}
function resend()
{
    fieldSenseTosterSuccess("OTP sent on Registered mobile number", true);
//          alert("OTP sent on Registered mobile number");
         var mobile_no=document.getElementById("mobile_id1").value;
        var email_add =document.getElementById("email_id1").value;
//    console.log("currentDate "+currentDate);
//    console/log("dateOfDefaultExpiry "+dateOfDefaultExpiry);
//    var jsonData = JSON.stringify(OTP);
//    alert("verified");
     $.ajax({
        type: "GET",
         url: fieldSenseURL + "/account/resend/?mobile_no="+mobile_no+"&email_add="+email_add,
//         ?accname=" + accname + "&status=" + status 
// url: fieldSenseURL + "/customerContact/customer/" + OTP,
        contentType: "application/json; charset=utf-8",
          headers: {
                            "userToken": userToken
                        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn:true,
        success: function(data) {
//            alert("success");
//           alert(data.result);
        }
    });
}