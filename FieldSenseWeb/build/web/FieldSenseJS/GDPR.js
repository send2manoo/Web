/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function saveGDPR()
{
    var is_terms_condition_agreed = document.getElementById("setagb").checked;
    console.log("is_terms_condition_agreed >> " + is_terms_condition_agreed);
    if (is_terms_condition_agreed) {
        is_terms_condition_agreed = 1;
    } else {
        is_terms_condition_agreed = 2;
    }
    var userEmailAddress = fieldSenseGetCookie("userEmailAddress");
//    alert("userEmailAddress "+userEmailAddress );
    var isFirstLogin = fieldSenseGetCookie("isFirstLogin");
    var role = fieldSenseGetCookie("userRole");

    var userObject = {
        "userEmailAddress": userEmailAddress,
        "is_terms_condition_agreed": is_terms_condition_agreed,
        "is_newsletter_opt_in": 1
    };
    var jsonData = JSON.stringify(userObject);
    console.log(jsonData);

    $.ajax({
        type: "POST",
        url: fieldSenseURL + "/authenticate/update_terms_condition_agreed",
        contentType: "application/json; charset=utf-8",
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        asyn: false,
        success: function (data) {
            if (role == 0) {
                window.location.href = "stats.html";
            } else if (role == 2) {
                document.cookie = "userMode=" + 2;
                window.location.href = "dashboard.html";  // modified by manohar
            } else {
                if (isFirstLogin) {
                    document.cookie = "userMode=" + 1;
                    window.location.href = "adminUser.html";
                } else {
                    document.cookie = "userMode=" + 2;
                    window.location.href = "dashboard.html";
                }
            }
        }
    });


}

