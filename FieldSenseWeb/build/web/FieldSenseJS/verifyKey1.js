/* 
 * @Added by jyoti, 19-02-2018
 */

jQuery(document).ready(function () {

    var fieldSenseURL = '../FieldSense';
    var fieldSenseWEBURL = '../FieldSenseWeb';

    var query = window.location.search.substring(1);
    var encryptedkey = query.split("=")[1];

    var accountLinkActivation = {
        "encryptedkey": encryptedkey
    };

    var jsonData = JSON.stringify(accountLinkActivation);

    $.ajax({
        type: "POST",
        url: fieldSenseURL + "/account/verifyKey",
        contentType: "application/json; charset=utf-8",
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {

//            alert(data);
//            alert("result.emailAddressl : " + data.result.emailAddress + "result.mobileNo " + data.result.mobileNo);
            if (data.errorCode == '0000') {
//                alert("inside 0000");
                var result1 = data.result;
                console.log("success .. " + result1.emailAddress);
                console.log("success .. " + result1.mobileNo);
//                localStorage.setItem("emailAddress", result1.emailAddress);
//                localStorage.setItem("mobileNo", result1.mobileNo);
//                alert("result.emailAddressl : "+result1.emailAddress +"result.mobileNo " +result1.mobileNo);
                document.getElementById('email_id1').value = result1.emailAddress;
                document.getElementById('mobile_id1').value = result1.mobileNo;
//                window.location.href = fieldSenseWEBURL + "/testingDIY.html";

            } else {
//                alert("inside else error")
                console.log("error in api..suspisious link trial " + result1.emailAddress);
                window.location.href = fieldSenseWEBURL + "/errorPage.html";
            }
        },
        error: function (data) {
            console.log("error...." + data);
            window.location.href = fieldSenseWEBURL + "/errorPage.html";
        }
    });

});