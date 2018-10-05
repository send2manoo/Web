/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var lat = 0;
var lng = 0;
var recentCustomers = new Array();
var industryCustomers = new Array();
var territorycustomers = new Array();
//added by nikhil
var loginUserIdcookie = fieldSenseGetCookie("loginUserIdcookie");
var loginUserNamecookie = fieldSenseGetCookie("loginUserNamecookie");
var accountIdcookie = fieldSenseGetCookie("accountIdcookie");
var accountNamecookie = fieldSenseGetCookie("accountNamecookie");
var UserRolecookie = fieldSenseGetCookie("UserRolecookie");
//ended by nikhil
var intervalAdminCustomer = window.setInterval(function () {
    var userToken2 = fieldSenseGetCookie("userToken");
    try {
        if (userToken2.toString() == "undefined") {
            window.location.href = "login.html";
        } else if (loginUserId != 0) {
            loggedinUserImageData();
            loggedinUserData();
            showCustomers();
            leftSideMenu();
            window.clearTimeout(intervalAdminCustomer);
        }
    }
    catch (err) {
        window.location.href = "login.html";
    }
}, 1000);
function showCustomers() {
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/customer",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            var customerData = data.result;
            var customerShowTemplateHtml = '';
            customerShowTemplateHtml += '<table class="table table-striped table-bordered table-hover" id="sample_2">';
            customerShowTemplateHtml += '<thead>';
            customerShowTemplateHtml += '<tr>';
            customerShowTemplateHtml += '<th>';
            customerShowTemplateHtml += 'Customer Name';
            customerShowTemplateHtml += '</th>';
            customerShowTemplateHtml += '<th>';
            customerShowTemplateHtml += 'Phone';
            customerShowTemplateHtml += '</th>';
            customerShowTemplateHtml += '<th>';
            customerShowTemplateHtml += 'Email';
            customerShowTemplateHtml += '</th>';
            //            customerShowTemplateHtml+='<th>';
            //            customerShowTemplateHtml+='Address';
            //            customerShowTemplateHtml+='</th>';
            customerShowTemplateHtml += '<th class="hidden-xs">';
            customerShowTemplateHtml += 'Industry';
            customerShowTemplateHtml += '</th>';
            customerShowTemplateHtml += '<th class="hidden-xs">';
            customerShowTemplateHtml += 'Territory';
            customerShowTemplateHtml += '</th>';
            customerShowTemplateHtml += '<th>';
            customerShowTemplateHtml += '</th>';
            customerShowTemplateHtml += '</tr>';
            customerShowTemplateHtml += '</thead>';
            customerShowTemplateHtml += '<tbody>';
            if (data.errorCode == '0000') {
                for (var i = 0; i < customerData.length; i++) {
                    var customerId = customerData[i].id;
                    var customerName = customerData[i].customerName;
                    var phone = customerData[i].customerPhone1;
                    var email = customerData[i].customerEmail1;
                    var address = customerData[i].customerAddress1;
                    var industry = customerData[i].industry;
                    var territory = customerData[i].territory;
                    customerShowTemplateHtml += '<tr>';
                    customerShowTemplateHtml += '<td><a data-toggle="modal" href="#responsive1" onClick="showOneCustomer(' + customerId + ')">' + customerName + '</a></td>';
                    customerShowTemplateHtml += '<td>' + phone + '</td>';
                    customerShowTemplateHtml += '<td>' + email + '</td>';
                    //                    customerShowTemplateHtml+='<td>'+address+ '</td>';
                    customerShowTemplateHtml += '<td class="hidden-xs">' + industry + '</td>';
                    customerShowTemplateHtml += '<td class="hidden-xs">' + territory + '</td>';
                    customerShowTemplateHtml += '<td>';
                    //                    customerShowTemplateHtml+='<a data-toggle="modal" href="#responsive2" class="btn btn-default btn-xs purple" onclick="editCustomerDetails('+customerId+')"><i class="fa fa-edit"></i> Edit</a>';
                    //                    customerShowTemplateHtml+='<a href="#" class="btn btn-default btn-xs purple" onclick="deleteCustomer(\''+customerId+'\',\''+customerName+'\')"><i class="fa fa-trash-o"></i> Delete</a>';
                    customerShowTemplateHtml += '<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Edit" data-toggle="modal" href="#responsive2" onclick="editCustomerDetails(' + customerId + ')"><i class="fa fa-edit"></i></button>';
                    customerShowTemplateHtml += '<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Delete" onclick="deleteCustomer(\'' + customerId + '\',\'' + customerName + '\')"><i class="fa fa-trash-o"></i></button>';
                    customerShowTemplateHtml += '</td>';
                    customerShowTemplateHtml += '</tr>';
                }
            }
            customerShowTemplateHtml += '</tbody>';
            customerShowTemplateHtml += '</table>';
            $('#datatblcustomer').html(customerShowTemplateHtml);
            TableAdvanced.init();
        }
    });
}
function createCustomerTemplate() {
    lat = 0;
    lng = 0;
}
function createCustomer() {
    var customerName = document.getElementById("id_customerName").value;
    var customerPrintAs = document.getElementById("id_customerPrintAs").value;
    var customerLocationIdentifier = document.getElementById("id_customerLocation").value;
    var isHeadOffice = document.getElementById("id_isHeadOffice").checked;
    var indexOfTerritory = document.getElementById("id_territory");
    var territory = indexOfTerritory.options[indexOfTerritory.selectedIndex].text;
    var indexOfIndustry = document.getElementById("id_industry");
    var industry = indexOfIndustry.options[indexOfIndustry.selectedIndex].text;
    var address = document.getElementById("id_address").value;
    var phone1 = document.getElementById("id_phone1").value;
    var phone2 = document.getElementById("id_phone2").value;
    var phone3 = document.getElementById("id_phone3").value;
    var fax1 = document.getElementById("id_fax1").value;
    var fax2 = document.getElementById("id_fax2").value;
    var email1 = document.getElementById("id_email1").value;
    var email2 = document.getElementById("id_email2").value;
    var email3 = document.getElementById("id_email3").value;
    var website1 = document.getElementById("id_website1").value;
    var website2 = document.getElementById("id_website2").value;

    if (customerName.trim().length == 0) {
        fieldSenseTosterError("Customer Name cannot be empty", true);
        return false;
    }
    if (customerName.trim().length > 200) {
        fieldSenseTosterError("Customer name can not be more than 200 characters", true);
        return false;
    }
    if (customerPrintAs.trim().length != 0) {
        if (customerPrintAs.trim().length > 200) {
            fieldSenseTosterError("Print as can not be more than 200 characters", true);
            return false;
        }
    } else {
        customerPrintAs = customerName;
    }
    if (customerLocationIdentifier.trim().length != 0) {
        if (customerLocationIdentifier.trim().length > 200) {
            fieldSenseTosterError("Location identifier can not be more than 200 characters", true);
            return false;
        }
    }
    if (address.trim().length == 0) {
        fieldSenseTosterError("Address cannot be empty", true);
        return false;
    }
    if (address.trim().length > 200) {
        fieldSenseTosterError("Address can not be more than 200 characters", true);
        return false;
    }
    if (lat == 0) {
        fieldSenseTosterError("Please pin the address in the map.", true);
        return false;
    }
    if (lng == 0) {
        fieldSenseTosterError("Please pin the address in the map.", true);
        return false;
    }

    if (phone1.trim().length != 0) {
        if (phone1.trim().length > 10) {
            fieldSenseTosterError("Phone1 can not be more than 10 digits.", true);
            return false;
        }
    }
    if (phone2.trim().length != 0) {
        if (phone2.trim().length > 10) {
            fieldSenseTosterError("Phone2 can not be more than 10 digits.", true);
            return false;
        }
    }
    if (phone3.trim().length != 0) {
        if (phone3.trim().length > 10) {
            fieldSenseTosterError("Phone3 can not be more than 10 digits.", true);
            return false;
        }
    }
    if (fax1.trim().length != 0) {
        if (fax1.trim().length > 50) {
            fieldSenseTosterError("Fax1 can not be more than 50 digits", true);
            return false;
        }
    }
    if (fax2.trim().length != 0) {
        if (fax2.trim().length > 50) {
            fieldSenseTosterError("Fax2 can not be more than 50 digits", true);
            return false;
        }
    }
    if (email1.trim().length != 0) {
        if (email1.trim().length > 100) {
            fieldSenseTosterError("Email1 can not be more than 100 characters", true);
            return false;
        }
    }
    //    var emailPattern = /^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;
    //    if(!emailPattern.test(email1.trim())){
    //        fieldSenseTosterError("Invalid email address .",true);
    //        return false;
    //    }
    if (email2.trim().length != 0) {
        if (email2.trim().length > 100) {
            fieldSenseTosterError("Email2 can not be more than 100 characters", true);
            return false;
        }
    }
    //    if(!emailPattern.test(email2.trim())){
    //        fieldSenseTosterError("Invalid email address .",true);
    //        return false;
    //    }
    if (email3.trim().length != 0) {
        if (email3.trim().length != 0) {
            fieldSenseTosterError("Email3 can not be more than 100 characters", true);
            return false;
        }
    }
    //    if(!emailPattern.test(email3.trim())){
    //        fieldSenseTosterError("Invalid email address .",true);
    //        return false;
    //    }
    if (website1.trim().length != 0) {
        if (website1.trim().length > 200) {
            fieldSenseTosterError("Website1 can not be more than 200 characters", true);
            return false;
        }
    }
    if (website2.trim().length != 0) {
        if (website2.trim().length > 200) {
            fieldSenseTosterError("Website2 can not be more than 200 characters", true);
            return false;
        }
    }

    var customerObject = {
        "customerName": customerName,
        "customerPrintas": customerPrintAs,
        "customerLocation": customerLocationIdentifier,
        "isHeadOffice": isHeadOffice,
        "territory": territory,
        "industry": industry,
        "customerAddress1": address,
        "customerPhone1": phone1,
        "customerPhone2": phone2,
        "customerPhone3": phone3,
        "customerFax1": fax1,
        "customerFax2": fax2,
        "customerEmail1": email1,
        "customerEmail2": email2,
        "customerEmail3": email3,
        "customerWebsiteUrl1": website1,
        "customerWebsiteUrl2": website2,
        "lasknownLatitude": lat,
        "lasknownLangitude": lng,
        "customerType": 1
    }

    var jsonData = JSON.stringify(customerObject);

    $.ajax({
        type: "POST",
        url: fieldSenseURL + "/customer",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            var customerData = data.result;
            if (data.errorCode == '0000') {
                clevertap.event.push("Add Customer", {
//                    "UserId": loginUserIdcookie,
//                    "User Name": loginUserNamecookie,
//                    "AccountId": accountIdcookie,
                    "Source": "Web",
                    "Account name": accountNamecookie,
                    "UserRolecookie": UserRolecookie,
                });
                var customerId = customerData.id;
                //                $('.errorMsg').html('<div style="margin:10px 0 0 140px;"> Customer saved successfully, please add contact(s).</div>');
                //                setTimeout(function(){
                //                    $('.errorMsg').fadeOut("slow");
                //                }, 4000);
                $('#Contacts').css("display", "block");
                $('.editCust').html('<button type="submit" class="btn btn-info" onClick="editCustomer(' + customerId + ')">Save</button>');
                $('.custId').html('<button type="submit" class="btn btn-info" onclick="createContact(' + customerId + ')">Save</button>');
                fieldSenseTosterSuccess("Customer added successfully. Please add Contact(s)", true);
            } else {
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}
function createContact(customerId) {
    var firstName = document.getElementById("id_firstname").value;
    var middleName = document.getElementById("id_middlename").value;
    var lastName = document.getElementById("id_lastname").value;
    var phone1 = document.getElementById("id_phonec1").value;
    var phone2 = document.getElementById("id_phonec2").value;
    var phone3 = document.getElementById("id_phonec3").value;
    var fax1 = document.getElementById("id_faxc1").value;
    var fax2 = document.getElementById("id_faxc2").value;
    var mobile1 = document.getElementById("id_mobile1").value;
    var mobile2 = document.getElementById("id_mobile2").value;
    var email1 = document.getElementById("id_emailc1").value;
    var email2 = document.getElementById("id_emailc2").value;
    var reportsTo = document.getElementById("id_reportsTo").value;
    var assistancenm = document.getElementById("id_assistancenm").value;
    var spousenm = document.getElementById("id_spousenm").value;
    var birthdate = document.getElementById("id_birthdate").value;
    var anniversarydate = document.getElementById("id_anniversarydate").value;
    var designation = document.getElementById("id_designation").value;
    var bdate;
    var adate;
    if (birthdate.trim().length != 0) {
        var birthdateSplit = birthdate.split("-");
        var month = birthdateSplit[1];
        var date = birthdateSplit[0];
        var year = birthdateSplit[2];
        bdate = year + "-" + month + "-" + date;
    } else {
        bdate = "1800-01-01";
    }
    if (anniversarydate.trim().length != 0) {
        var anniversarydateSplit = anniversarydate.split("-");
        var m = anniversarydateSplit[1];
        var d = anniversarydateSplit[0];
        var y = anniversarydateSplit[2];
        adate = y + "-" + m + "-" + d;
    } else {
        adate = "1800-01-01";
    }

    if (firstName.trim().length == 0) {
        fieldSenseTosterError("First Name cannot be empty", true);
        return false;
    }
    if (firstName.trim().length > 50) {
        fieldSenseTosterError("First name can not be more than 50 characters", true);
        return false;
    }
    if (middleName.trim().length > 50) {
        fieldSenseTosterError("Middle name can not be more than 50 characters", true);
        return false;
    }

    if (lastName.trim().length != 0) {
        if (lastName.trim().length > 50) {
            fieldSenseTosterError("Last name can not be more than 50 characters", true);
            return false;
        }
    }
    if (phone1.trim().length != 0) {
        if (phone1.trim().length > 10) {
            fieldSenseTosterError("Phone1 can not be more than 10 digits", true);
            return false;
        }
    }
    if (phone2.trim().length != 0) {
        if (phone2.trim().length > 10) {
            fieldSenseTosterError("Phone2 can not be more than 10 digits", true);
            return false;
        }
    }
    if (phone3.trim().length != 0) {
        if (phone3.trim().length > 10) {
            fieldSenseTosterError("Phone3 can not be more than 10 digits", true);
            return false;
        }
    }
    if (fax1.trim().length != 0) {
        if (fax1.trim().length > 50) {
            fieldSenseTosterError("Fax1 can not be more than 50 digits", true);
            return false;
        }
    }
    if (fax2.trim().length != 0) {
        if (fax2.trim().length > 50) {
            fieldSenseTosterError("Fax2 can not be more than 50 digits", true);
            return false;
        }
    }
    if (mobile1.trim().length != 0) {
        if (mobile1.trim().length > 10) {
            fieldSenseTosterError("Mobile1 can not be more than 10 digits", true);
            return false;
        }
    }
    if (mobile2.trim().length != 0) {
        if (mobile2.trim().length > 10) {
            fieldSenseTosterError("Mobile2 can not be more than 10 digits", true);
            return false;
        }
    }
    if (email1.trim().length != 0) {
        if (email1.trim().length > 100) {
            fieldSenseTosterError("Email1 can not be more than 100 characters", true);
            return false;
        }
    }
    //    var emailPattern = /^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;
    //    if(!emailPattern.test(email1.trim())){
    //        fieldSenseTosterError("Invalid email address .",true);
    //        return false;
    //    }
    if (email2.trim().length != 0) {
        if (email2.trim().length > 100) {
            fieldSenseTosterError("Email2 can not be more than 100 characters", true);
            return false;
        }
    }
    //    if(!emailPattern.test(email2.trim())){
    //        fieldSenseTosterError("Invalid email address .",true);
    //        return false;
    //    }
    if (reportsTo.trim().length != 0) {
        if (reportsTo.trim().length > 100) {
            fieldSenseTosterError("Report to can not be more than 100 characters", true);
            return false;
        }
    }

    if (assistancenm.trim().length != 0) {
        if (assistancenm.trim().length > 100) {
            fieldSenseTosterError("Assistance name can not be more than 100 characters", true);
            return false;
        }
    }
    if (spousenm.trim().length != 0) {
        if (spousenm.trim().length > 100) {
            fieldSenseTosterError("Spouce name can not be more than 100 characters", true);
            return false;
        }
    }
    if (designation.trim().length != 0) {
        if (designation.trim().length > 100) {
            fieldSenseTosterError("Designation can not be more than 100 characters", true);
            return false;
        }
    }

    var customerContactObject = {
        "customerId": customerId,
        "firstName": firstName,
        "middleName": middleName,
        "lastName": lastName,
        "phone1": phone1,
        "phone2": phone2,
        "phone3": phone3,
        "fax1": fax1,
        "fax2": fax2,
        "mobile1": mobile1,
        "mobile2": mobile2,
        "email1": email1,
        "email2": email2,
        "reportTo": reportsTo,
        "assistantName": assistancenm,
        "spouseName": spousenm,
        "birthDate": bdate,
        "anniversaryDate": adate,
        "designation": designation
    }

    var jsonData = JSON.stringify(customerContactObject);

    $.ajax({
        type: "POST",
        url: fieldSenseURL + "/customerContact",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            var customerContactData = data.result;
            if (data.errorCode == '0000') {
                $('#id_firstname').val('');
                $('#id_middlename').val('');
                $('#id_lastname').val('');
                $('#id_phonec1').val('');
                $('#id_phonec2').val('');
                $('#id_phonec3').val('');
                $('#id_faxc1').val('');
                $('#id_faxc2').val('');
                $('#id_mobile1').val('');
                $('#id_mobile2').val('');
                $('#id_emailc1').val('');
                $('#id_emailc2').val('');
                $('#id_reportsTo').val('');
                $('#id_assistancenm').val('');
                $('#id_spousenm').val('');
                $('#id_birthdate').val('');
                $('#id_anniversarydate').val('');
                $('#id_designation').val('');
                fieldSenseTosterSuccess(data.errorMessage, true);
            } else {
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}
function showOneCustomer(customerId) {
    $('#custContacttmt').html('');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/customer/" + customerId,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            $('#custtmt').html('');
            var customerData = data.result;
            if (data.errorCode == '0000') {
                var customerShowTemplateHtml = '';

                var customerId = customerData.id;
                var customerName = customerData.customerName;
                var customerPrintAs = customerData.customerPrintas;
                var customerLocationIdentifier = customerData.customerLocation;
                var isHeadOffice = customerData.isHeadOffice;
                var territory = customerData.territory;
                var industry = customerData.industry;
                var address = customerData.customerAddress1;
                var phone1 = customerData.customerPhone1;
                var phone2 = customerData.customerPhone2;
                var phone3 = customerData.customerPhone3;
                var fax1 = customerData.customerFax1;
                var fax2 = customerData.customerFax2;
                var email1 = customerData.customerEmail1;
                var email2 = customerData.customerEmail2;
                var email3 = customerData.customerEmail3;
                var website1 = customerData.customerWebsiteUrl1;
                var website2 = customerData.customerWebsiteUrl2;
                customerShowTemplateHtml += '<form class="form-horizontal" role="form">';
                customerShowTemplateHtml += '<div class="form-body">';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Customer Name</label>';
                customerShowTemplateHtml += '<label class="col-md-6 label2">' + customerName + '</label>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Customer Print As</label>';
                customerShowTemplateHtml += '<label class="col-md-6 label2">' + customerPrintAs + '</label>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Customer Location Identifier</label>';
                customerShowTemplateHtml += '<label class="col-md-6 label2">' + customerLocationIdentifier + '</label>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Is Head Office</label>';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<div class="checkbox-list">';
                if (isHeadOffice == true) {
                    customerShowTemplateHtml += '<input type="checkbox" checked disabled>';
                } else {
                    customerShowTemplateHtml += '<input type="checkbox" disabled>';
                }
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Territory</label>';
                customerShowTemplateHtml += '<label class="col-md-6 label2">' + territory + '</label>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Industry</label>';
                customerShowTemplateHtml += '<label class="col-md-6 label2">' + industry + '</label>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Address</label>';
                customerShowTemplateHtml += '<label class="col-md-6 label2">' + address + '</label>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Phone1</label>';
                customerShowTemplateHtml += '<label class="col-md-6 label2">' + phone1 + '</label>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Phone2</label>';
                customerShowTemplateHtml += '<label class="col-md-6 label2">' + phone2 + '</label>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Phone3</label>';
                customerShowTemplateHtml += '<label class="col-md-6 label2">' + phone3 + '</label>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Fax1</label>';
                customerShowTemplateHtml += '<label class="col-md-6 label2">' + fax1 + '</label>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Fax2</label>';
                customerShowTemplateHtml += '<label class="col-md-6 label2">' + fax2 + '</label>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Email1</label>';
                customerShowTemplateHtml += '<label class="col-md-6 label2">' + email1 + '</label>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Email2</label>';
                customerShowTemplateHtml += '<label class="col-md-6 label2">' + email2 + '</label>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Email3</label>';
                customerShowTemplateHtml += '<label class="col-md-6 label2">' + email3 + '</label>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Website1</label>';
                customerShowTemplateHtml += '<label class="col-md-6 label2">' + website1 + '</label>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Website2</label>';
                customerShowTemplateHtml += '<label class="col-md-6 label2">' + website2 + '</label>	';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</form>';
                $('#custtmt').append(customerShowTemplateHtml);
            }
        }
    });
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/customerContact/customer/" + customerId,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            $('#contacttmt').html('');
            var contactData = data.result;
            if (data.errorCode == '0000') {
                var contactShowTemplateHtml = '';

                for (var i = 0; i < contactData.length; i++) {
                    var customerContactId = contactData[i].id;
                    var firstName = contactData[i].firstName;
                    var middleName = contactData[i].middleName;
                    var lastName = contactData[i].lastName;
                    var phone1 = contactData[i].phone1;
                    var phone2 = contactData[i].phone2;
                    var phone3 = contactData[i].phone3;
                    var fax1 = contactData[i].fax1;
                    var fax2 = contactData[i].fax2;
                    var mobile1 = contactData[i].mobile1;
                    var mobile2 = contactData[i].mobile2;
                    var email1 = contactData[i].email1;
                    var email2 = contactData[i].email2;
                    var reportsTo = contactData[i].reportTo;
                    var assistancenm = contactData[i].assistantName;
                    var spousenm = contactData[i].spouseName;
                    var birthdate = contactData[i].birthDate;
                    var anniversarydate = contactData[i].anniversaryDate;
                    var designation = contactData[i].designation;

                    contactShowTemplateHtml += '<tr>';
                    contactShowTemplateHtml += '<td class="highlight">';
                    contactShowTemplateHtml += '<a class="showSingle" target="1">' + firstName + ' ' + middleName + ' ' + lastName + '</a>';
                    contactShowTemplateHtml += '</td>';
                    contactShowTemplateHtml += '<td class="hidden-xs">' + mobile1 + '</td>';
                    contactShowTemplateHtml += '<td>' + email1 + '</td>';
                    contactShowTemplateHtml += '<td>';
                    contactShowTemplateHtml += '<a target="2" class="showSingle btn btn-default btn-xs purple" onclick="showOneCustomerContact(' + customerContactId + ')"><i class="fa fa-edit"></i> View</a>';
                    contactShowTemplateHtml += '</td>';
                    contactShowTemplateHtml += '</tr>';
                }
                $('#contacttmt').append(contactShowTemplateHtml);
            }
        }
    });
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/appointment/customer/" + customerId,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            //            $('#contacttmt').html('');
            var activityData = data.result;
            if (data.errorCode == '0000') {
                $('#activitytmt').html('');
                var activityShowTemplate = '';
                for (var i = 0; i < activityData.length; i++) {
                    var activity = activityData[i].title;
                    var purpose = activityData[i].purpose.purpose;
                    var firstName = activityData[i].customerContact.firstName;
                    var middleName = activityData[i].customerContact.middleName;
                    var lastName = activityData[i].customerContact.lastName;
                    var contact = firstName + ' ' + middleName + ' ' + lastName;
                    var dateTime = activityData[i].dateTime;
                    var sdateTime = activityData[i].sdateTime;
                    var activityDateTime = fieldSenseseForJSDate(sdateTime);
                    var status = activityData[i].status;
                    //                    var activityDateTimeSplit = activityDateTime.split(" ");
                    //                    var activityDate = activityDateTimeSplit[0];
                    //                    var activityTimeSplit = activityDateTimeSplit[3];
                    //                    var activityTimeAfterSplit = activityTimeSplit.split(":");
                    //                    var activityHourse = activityTimeAfterSplit[0];
                    //                    var activityMinutes = activityTimeAfterSplit[1];
                    //                    var activityTime;
                    //                    if(activityHourse<12){
                    //                        activityTime =  activityHourse+'.'+activityMinutes+' AM';
                    //                    }
                    //                    else if(activityHourse == 12){
                    //                        activityTime =  activityHourse+'.'+activityMinutes+' PM';
                    //                    }else{
                    //                        var activityHourses=activityHourse-12;
                    //                        activityTime =  activityHourses+'.'+activityMinutes+' PM';
                    //                    }
                    var statucIs;
                    if (status == 0) {
                        statucIs = "Pending";
                    } else if (status == 1) {
                        statucIs = "In Progress";
                    } else if (status == 2) {
                        statucIs = "Completed";
                    } else if (status == 3) {
                        statucIs = "Cancelled";
                    }
                    activityShowTemplate += '<div class="form-body">';
                    activityShowTemplate += '<div class="form-group">';
                    activityShowTemplate += '<label class=col-md-6 control-label">Activity</label>';
                    activityShowTemplate += '<label class="col-md-6 label2">' + activity + '</label>';
                    activityShowTemplate += '</div>';
                    activityShowTemplate += '<div class="form-group">';
                    activityShowTemplate += '<label class="col-md-6 control-label">Purpose</label>';
                    activityShowTemplate += '<label class="col-md-6 label2">' + purpose + '</label>';
                    activityShowTemplate += '</div>';
                    activityShowTemplate += '<div class="form-group">';
                    activityShowTemplate += '<label class="col-md-6 control-label">Contact</label>';
                    activityShowTemplate += '<label class="col-md-6 label2">' + contact + '</label>';
                    activityShowTemplate += '</div>';
                    activityShowTemplate += '<div class="form-group">';
                    activityShowTemplate += '<label class="col-md-6 control-label">Time</label>';
                    activityShowTemplate += '<label class="col-md-6 label2">' + activityDateTime + '</label>';
                    activityShowTemplate += '</div>';
                    activityShowTemplate += '<div class="form-group">';
                    activityShowTemplate += '<label class="col-md-6 control-label">Status</label>';
                    activityShowTemplate += '<label class="col-md-6 label2">' + statucIs + '</label>';
                    activityShowTemplate += '</div>';
                    activityShowTemplate += '</div>';
                }
                $('#activitytmt').append(activityShowTemplate);
            }
        }
    });

}
function showOneCustomerContact(customerContactId) {
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/customerContact/" + customerContactId,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            $('#custContacttmt').html('');
            var contactData = data.result;
            if (data.errorCode == '0000') {
                var firstName = contactData.firstName;
                var middleName = contactData.middleName;
                var lastName = contactData.lastName;
                var phone1 = contactData.phone1;
                var phone2 = contactData.phone2;
                var phone3 = contactData.phone3;
                var fax1 = contactData.fax1;
                var fax2 = contactData.fax2;
                var mobile1 = contactData.mobile1;
                var mobile2 = contactData.mobile2;
                var email1 = contactData.email1;
                var email2 = contactData.email2;
                var reportsTo = contactData.reportTo;
                var assistancenm = contactData.assistantName;
                var spousenm = contactData.spouseName;
                var birthdate = fieldSenseseDateFormateForJsonDate(contactData.birthDate);
                var anniversarydate = fieldSenseseDateFormateForJsonDate(contactData.anniversaryDate);
                var designation = contactData.designation;
                var birthdateTimeSplit = birthdate.split(" ");
                var m = birthdateTimeSplit[0];
                var d = birthdateTimeSplit[1];
                var dateSplit = d.split(",");
                var date = dateSplit[0];
                var year = birthdateTimeSplit[2];
                var month = getMonthFromString(m);
                if (month < 10) {
                    month = '0' + month;
                }
                var bdate = date + '-' + month + '-' + year;
                var anniversarydateTimeSplit = anniversarydate.split(" ");
                var mo = anniversarydateTimeSplit[0];
                var da = anniversarydateTimeSplit[1];
                var dateSplits = da.split(",");
                var dates = dateSplits[0];
                var years = anniversarydateTimeSplit[2];
                var months = getMonthFromString(mo);
                if (months < 10) {
                    months = '0' + months;
                }
                var adate = dates + '-' + months + '-' + years;
                if (bdate == "01-01-1800") {
                    bdate = '';
                }
                if (adate == "01-01-1800") {
                    adate = '';
                }
                $('#cdetails').css("display", "block");
                $('#div1').css("display", "block");
                var customerContactTemplateHtml = '';
                customerContactTemplateHtml += '<div class="form-body">';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">First Name</label>';
                customerContactTemplateHtml += '<label class="col-md-6 label2">' + firstName + '</label>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Middle Name</label>';
                customerContactTemplateHtml += '<label class="col-md-6 label2">' + middleName + '</label>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Last Name</label>';
                customerContactTemplateHtml += '<label class="col-md-6 label2">' + lastName + '</label>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Phone1</label>';
                customerContactTemplateHtml += '<label class="col-md-6 label2">' + phone1 + '</label>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Phone2</label>';
                customerContactTemplateHtml += '<label class="col-md-6 label2">' + phone2 + '</label>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Phone3</label>';
                customerContactTemplateHtml += '<label class="col-md-6 label2">' + phone3 + '</label>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Fax1</label>';
                customerContactTemplateHtml += '<label class="col-md-6 label2">' + fax1 + '</label>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Fax2</label>';
                customerContactTemplateHtml += '<label class="col-md-6 label2">' + fax1 + '</label>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Mobile1</label>';
                customerContactTemplateHtml += '<label class="col-md-6 label2">' + mobile1 + '</label>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Mobile2</label>';
                customerContactTemplateHtml += '<label class="col-md-6 label2">' + mobile2 + '</label>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Email1</label>';
                customerContactTemplateHtml += '<label class="col-md-6 label2">' + email1 + '</label>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Email2</label>';
                customerContactTemplateHtml += '<label class="col-md-6 label2">' + email2 + '</label>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Reports To</label>';
                customerContactTemplateHtml += '<label class="col-md-6 label2">' + reportsTo + '</label>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Assistant Name</label>';
                customerContactTemplateHtml += '<label class="col-md-6 label2">' + assistancenm + '</label>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Spouse Name</label>';
                customerContactTemplateHtml += '<label class="col-md-6 label2">' + spousenm + '</label>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Birth Date</label>';
                customerContactTemplateHtml += '<label class="col-md-6 label2">' + bdate + '</label>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Anniversary Date</label>';
                customerContactTemplateHtml += '<label class="col-md-6 label2">' + adate + '</label>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Designation</label>';
                customerContactTemplateHtml += '<label class="col-md-6 label2">' + designation + '</label>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '</div>';
                $('#custContacttmt').prepend(customerContactTemplateHtml);
            }
        }
    });
}
function editCustomer(customerId) {
    var customerName = document.getElementById("id_customerName").value;
    var customerPrintAs = document.getElementById("id_customerPrintAs").value;
    var customerLocationIdentifier = document.getElementById("id_customerLocation").value;
    var isHeadOffice = document.getElementById("id_isHeadOffice").checked;
    var indexOfTerritory = document.getElementById("id_territory");
    var territory = indexOfTerritory.options[indexOfTerritory.selectedIndex].text;
    var indexOfIndustry = document.getElementById("id_industry");
    var industry = indexOfIndustry.options[indexOfIndustry.selectedIndex].text;
    var address = document.getElementById("id_address").value;
    var phone1 = document.getElementById("id_phone1").value;
    var phone2 = document.getElementById("id_phone2").value;
    var phone3 = document.getElementById("id_phone3").value;
    var fax1 = document.getElementById("id_fax1").value;
    var fax2 = document.getElementById("id_fax2").value;
    var email1 = document.getElementById("id_email1").value;
    var email2 = document.getElementById("id_email2").value;
    var email3 = document.getElementById("id_email3").value;
    var website1 = document.getElementById("id_website1").value;
    var website2 = document.getElementById("id_website2").value;
    if (customerName.trim().length == 0) {
        fieldSenseTosterError("Customer Name cannot be empty", true);
        return false;
    }
    if (customerPrintAs.trim().length == 0) {
        fieldSenseTosterError("Please enter customer print as", true);
        return false;
    }
    if (customerLocationIdentifier.trim().length == 0) {
        fieldSenseTosterError("Please enter location identifier", true);
        return false;
    }
    if (address.trim().length == 0) {
        fieldSenseTosterError("Address cannot be empty", true);
        return false;
    }
    if (phone1.trim().length == 0) {
        fieldSenseTosterError("Please enter phone1", true);
        return false;
    }
    if (phone2.trim().length == 0) {
        fieldSenseTosterError("Please enter phone2", true);
        return false;
    }
    if (phone3.trim().length == 0) {
        fieldSenseTosterError("Please enter phone3", true);
        return false;
    }
    if (fax1.trim().length == 0) {
        fieldSenseTosterError("Please enter fax1", true);
        return false;
    }
    if (fax2.trim().length == 0) {
        fieldSenseTosterError("Please enter fax2", true);
        return false;
    }
    if (email1.trim().length == 0) {
        fieldSenseTosterError("Please enter email1", true);
        return false;
    }
    if (email2.trim().length == 0) {
        fieldSenseTosterError("Please enter email2", true);
        return false;
    }
    if (email3.trim().length == 0) {
        fieldSenseTosterError("Please enter email3", true);
        return false;
    }
    if (website1.trim().length == 0) {
        fieldSenseTosterError("Please enter website1", true);
        return false;
    }
    if (website2.trim().length == 0) {
        fieldSenseTosterError("Please enter website2", true);
        return false;
    }

    var customerObject = {
        "id": customerId,
        "customerName": customerName,
        "customerPrintas": customerPrintAs,
        "customerLocation": customerLocationIdentifier,
        "isHeadOffice": isHeadOffice,
        "territory": territory,
        "industry": industry,
        "customerAddress1": address,
        "customerPhone1": phone1,
        "customerPhone2": phone2,
        "customerPhone3": phone3,
        "customerFax1": fax1,
        "customerFax2": fax2,
        "customerEmail1": email1,
        "customerEmail2": email2,
        "customerEmail3": email3,
        "customerWebsiteUrl1": website1,
        "customerWebsiteUrl2": website2
    }
    var jsonData = JSON.stringify(customerObject);
    $.ajax({
        type: "PUT",
        url: fieldSenseURL + "/customer",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            var customerData = data.result;
            if (data.errorCode == '0000') {
                fieldSenseTosterSuccess(data.errorMessage, true);
                //                var customerId = customerData.id;
            } else {
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}
function clearIds() {
    $('#customertmt').html('');
    $('.editCust').html('<button type="submit" class="btn btn-info" onClick="createCustomer()">Save</button>');
    showCustomers();
    $('#Contacts').css("display", "none");
    $('#id_customerName').val('');
    $('#id_customerPrintAs').val('');
    $('#id_customerLocation').val('');
    $('#id_isHeadOffice').val('');
    $('#id_territory').val('');
    $('#id_industry').val('');
    $('#id_address').val('');
    $('#id_phone1').val('');
    $('#id_phone2').val('');
    $('#id_phone3').val('');
    $('#id_fax1').val('');
    $('#id_fax2').val('');
    $('#id_email1').val('');
    $('#id_email2').val('');
    $('#id_email3').val('');
    $('#id_website1').val('');
    $('#id_website2').val('');

    $('#id_firstname').val('');
    $('#id_middlename').val('');
    $('#id_lastname').val('');
    $('#id_phonec1').val('');
    $('#id_phonec2').val('');
    $('#id_phonec3').val('');
    $('#id_faxc1').val('');
    $('#id_faxc2').val('');
    $('#id_mobile1').val('');
    $('#id_mobile2').val('');
    $('#id_emailc1').val('');
    $('#id_emailc2').val('');
    $('#id_reportsTo').val('');
    $('#id_assistancenm').val('');
    $('#id_spousenm').val('');
    $('#id_birthdate').val('');
    $('#id_anniversarydate').val('');
    $('#id_designation').val('');
}
function editCustomerDetails(customerId) {
    $('#id_editAdd').html('<button type="submit" class="btn btn-info" onclick="createContactOnedit(' + customerId + ')">Save</button>');
    $('#id_custContact').html('');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/customer/" + customerId,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            $('#id_editCustomer').html('');
            var customerData = data.result;
            if (data.errorCode == '0000') {
                var customerShowTemplateHtml = '';

                var customerId = customerData.id;
                var customerName = customerData.customerName;
                var customerPrintAs = customerData.customerPrintas;
                var customerLocationIdentifier = customerData.customerLocation;
                var isHeadOffice = customerData.isHeadOffice;
                var territory = customerData.territory;
                var industry = customerData.industry;
                var address = customerData.customerAddress1;
                var phone1 = customerData.customerPhone1;
                var phone2 = customerData.customerPhone2;
                var phone3 = customerData.customerPhone3;
                var fax1 = customerData.customerFax1;
                var fax2 = customerData.customerFax2;
                var email1 = customerData.customerEmail1;
                var email2 = customerData.customerEmail2;
                var email3 = customerData.customerEmail3;
                var website1 = customerData.customerWebsiteUrl1;
                var website2 = customerData.customerWebsiteUrl2;

                customerShowTemplateHtml += '<form class="form-horizontal" role="form">';
                customerShowTemplateHtml += '<div class="form-body">';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Customer Name</label>';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<input type="name" class="form-control" value="' + customerName + '" id="id_customerNames">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Customer Print As</label>';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<input type="name" class="form-control" value="' + customerPrintAs + '" id="id_customerPrintAss">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Customer Location Identifier</label>';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<input type="location" class="form-control" value="' + customerLocationIdentifier + '" id="id_customerLocations">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Is Head Office</label>';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<div class="checkbox-list">';
                if (isHeadOffice == true) {
                    customerShowTemplateHtml += '<input type="checkbox" checked id="id_isHeadOffices">';
                } else {
                    customerShowTemplateHtml += '<input type="checkbox" id="id_isHeadOffices">';
                }
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Territory</label>';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<select class="form-control" id="id_territorys">';
                customerShowTemplateHtml += '<option>Select</option>';
                if (territory == "North") {
                    customerShowTemplateHtml += '<option selected>North</option>';
                } else {
                    customerShowTemplateHtml += '<option>North</option>';
                }
                if (territory == "East") {
                    customerShowTemplateHtml += '<option selected>East</option>';
                } else {
                    customerShowTemplateHtml += '<option>East</option>';
                }
                if (territory == "West") {
                    customerShowTemplateHtml += '<option selected>West</option>';
                } else {
                    customerShowTemplateHtml += '<option>West</option>';
                }
                if (territory == "South") {
                    customerShowTemplateHtml += '<option selected>South</option>';
                } else {
                    customerShowTemplateHtml += '<option>South</option>';
                }
                customerShowTemplateHtml += '</select>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Industry</label>';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<select class="form-control" id="id_industrys">';
                customerShowTemplateHtml += '<option selected>Select</option>';
                if (industry == "Financial") {
                    customerShowTemplateHtml += '<option selected>Financial</option>';
                } else {
                    customerShowTemplateHtml += '<option>Financial</option>';
                }
                if (industry == "Banking") {
                    customerShowTemplateHtml += '<option selected>Banking</option>';
                } else {
                    customerShowTemplateHtml += '<option>Banking</option>';
                }
                if (industry == "Software") {
                    customerShowTemplateHtml += '<option selected>Software</option>';
                } else {
                    customerShowTemplateHtml += '<option>Software</option>';
                }
                if (industry == "Brokerage") {
                    customerShowTemplateHtml += '<option selected>Brokerage</option>';
                } else {
                    customerShowTemplateHtml += '<option>Brokerage</option>';
                }
                if (industry == "Biotechnology") {
                    customerShowTemplateHtml += '<option selected>Biotechnology</option>';
                } else {
                    customerShowTemplateHtml += '<option>Biotechnology</option>';
                }
                if (industry == "Investment Banking") {
                    customerShowTemplateHtml += '<option selected>Investment Banking</option>';
                } else {
                    customerShowTemplateHtml += '<option>Investment Banking</option>';
                }
                if (industry == "Real Estate") {
                    customerShowTemplateHtml += '<option selected>Real Estate</option>';
                } else {
                    customerShowTemplateHtml += '<option>Real Estate</option>';
                }
                customerShowTemplateHtml += '</select>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Address</label>';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<input type="Address1" class="form-control" value="' + address + '" id="id_addresss">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Phone1</label>';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<input type="Phone1" class="form-control" value="' + phone1 + '" id="id_phones1">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Phone2</label>';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<input type="Phone2" class="form-control" value="' + phone2 + '" id="id_phones2">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Phone3</label>';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<input type="Phone3" class="form-control" value="' + phone3 + '" id="id_phones3">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Fax1</label>';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<input type="Fax1" class="form-control" value="' + fax1 + '" id="id_faxs1">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Fax2</label>';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<input type="Fax2" class="form-control" value="' + fax2 + '" id="id_faxs2">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';

                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Email1</label>';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<input type="Email1" class="form-control" value="' + email1 + '" id="id_emails1">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Email2</label>';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<input type="Email2" class="form-control" value="' + email2 + '" id="id_emails2">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Email3</label>';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<input type="Email3" class="form-control" value="' + email3 + '" id="id_emails3">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Website1</label>';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<input type="Website1" class="form-control" value="' + website1 + '" id="id_websites1">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-6 control-label">Website2</label>';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<input type="Website2" class="form-control" value="' + website2 + '" id="id_websites2">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</form>';
                customerShowTemplateHtml += '<div class="buttons">';
                customerShowTemplateHtml += '<button type="submit" class="btn btn-info" onclick="editCustomerOnEdit(' + customerId + ')">Save</button>';
                customerShowTemplateHtml += '&nbsp;';
                customerShowTemplateHtml += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
                customerShowTemplateHtml += '</div>';
                $('#id_editCustomer').append(customerShowTemplateHtml);
            }
        }
    });


    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/customerContact/customer/" + customerId,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            $('#id_editcontact').html('');
            var contactData = data.result;
            if (data.errorCode == '0000') {
                var contactShowTemplateHtml = '';

                for (var i = 0; i < contactData.length; i++) {
                    var customerContactId = contactData[i].id;
                    var firstName = contactData[i].firstName;
                    var middleName = contactData[i].middleName;
                    var lastName = contactData[i].lastName;
                    var phone1 = contactData[i].phone1;
                    var phone2 = contactData[i].phone2;
                    var phone3 = contactData[i].phone3;
                    var fax1 = contactData[i].fax1;
                    var fax2 = contactData[i].fax2;
                    var mobile1 = contactData[i].mobile1;
                    var mobile2 = contactData[i].mobile2;
                    var email1 = contactData[i].email1;
                    var email2 = contactData[i].email2;
                    var reportsTo = contactData[i].reportTo;
                    var assistancenm = contactData[i].assistantName;
                    var spousenm = contactData[i].spouseName;
                    var birthdate = contactData[i].birthDate;
                    var anniversarydate = contactData[i].anniversaryDate;
                    var designation = contactData[i].designation;
                    var fullName = firstName + ' ' + middleName + ' ' + lastName;
                    contactShowTemplateHtml += '<tr>';
                    contactShowTemplateHtml += '<td class="highlight">' + firstName + ' ' + middleName + ' ' + lastName + '</td>';
                    contactShowTemplateHtml += '<td class="hidden-xs">' + mobile1 + '</td>';
                    contactShowTemplateHtml += '<td>' + email1 + '</td>';
                    contactShowTemplateHtml += '<td>';
                    contactShowTemplateHtml += '<a target="21" class="showSingle2 btn btn-default btn-xs purple" onclick="showOneContact(' + customerContactId + ')"><i class="fa fa-edit"></i> Edit</a>';
                    contactShowTemplateHtml += '<a href="#" class="btn btn-default btn-xs purple" onclick="deleteCustomerContact(\'' + customerContactId + '\',\'' + fullName + '\',\'' + customerId + '\')"><i class="fa fa-trash-o"></i> Delete</a>';
                    contactShowTemplateHtml += '</td>';
                    contactShowTemplateHtml += '</tr>';
                }
                $('#id_editcontact').append(contactShowTemplateHtml);
            }
        }
    });
}

function showOneContact(customerContactId) {
    $('#add_contact').css("display", "none");
    $('#targetADD').css("display", "none");
    $('#id_custContact').html('');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/customerContact/" + customerContactId,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            $('#id_custContact').html('');
            var contactData = data.result;
            if (data.errorCode == '0000') {
                var firstName = contactData.firstName;
                var middleName = contactData.middleName;
                var lastName = contactData.lastName;
                var phone1 = contactData.phone1;
                var phone2 = contactData.phone2;
                var phone3 = contactData.phone3;
                var fax1 = contactData.fax1;
                var fax2 = contactData.fax2;
                var mobile1 = contactData.mobile1;
                var mobile2 = contactData.mobile2;
                var email1 = contactData.email1;
                var email2 = contactData.email2;
                var reportsTo = contactData.reportTo;
                var assistancenm = contactData.assistantName;
                var spousenm = contactData.spouseName;
                var birthdate = fieldSenseseDateFormateForJsonDate(contactData.birthDate);
                var anniversarydate = fieldSenseseDateFormateForJsonDate(contactData.anniversaryDate);
                var designation = contactData.designation;
                var birthdateTimeSplit = birthdate.split(" ");
                var m = birthdateTimeSplit[0];
                var d = birthdateTimeSplit[1];
                var dateSplit = d.split(",");
                var date = dateSplit[0];
                var year = birthdateTimeSplit[2];
                var month = getMonthFromString(m);
                if (month < 10) {
                    month = '0' + month;
                }
                var bdate = date + '-' + month + '-' + year;
                var anniversarydateTimeSplit = anniversarydate.split(" ");
                var mo = anniversarydateTimeSplit[0];
                var da = anniversarydateTimeSplit[1];
                var dateSplits = da.split(",");
                var dates = dateSplits[0];
                var years = anniversarydateTimeSplit[2];
                var months = getMonthFromString(mo);
                if (months < 10) {
                    months = '0' + months;
                }
                var adate = dates + '-' + months + '-' + years;
                if (bdate == "01-01-1800") {
                    bdate = '';
                }
                if (adate == "01-01-1800") {
                    adate = '';
                }
                $('#ed_details').css("display", "block");
                $('#div21').css("display", "block");
                var customerContactTemplateHtml = '';

                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">First Name</label>';
                customerContactTemplateHtml += '<div class="col-md-6">';
                customerContactTemplateHtml += '<input type="fname" class="form-control" value="' + firstName + '" id="id_fnm">';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Middle Name</label>';
                customerContactTemplateHtml += '<div class="col-md-6">';
                customerContactTemplateHtml += '<input type="mname" class="form-control" value="' + middleName + '" id="id_mnm">';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Last Name</label>';
                customerContactTemplateHtml += '<div class="col-md-6">';
                customerContactTemplateHtml += '<input type="lname" class="form-control" value="' + lastName + '" id="id_lnm">';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Phone1</label>';
                customerContactTemplateHtml += '<div class="col-md-6">';
                customerContactTemplateHtml += '<input type="Phone2" class="form-control" value="' + phone1 + '" id="id_ph1">';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Phone2</label>';
                customerContactTemplateHtml += '<div class="col-md-6">';
                customerContactTemplateHtml += '<input type="Phone2" class="form-control" value="' + phone2 + '" id="id_ph2">';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Phone3</label>';
                customerContactTemplateHtml += '<div class="col-md-6">';
                customerContactTemplateHtml += '<input type="Phone3" class="form-control" value="' + phone3 + '" id="id_ph3">';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Fax1</label>';
                customerContactTemplateHtml += '<div class="col-md-6">';
                customerContactTemplateHtml += '<input type="Fax1" class="form-control" value="' + fax1 + '" id="id_fx1">';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Fax2</label>';
                customerContactTemplateHtml += '<div class="col-md-6">';
                customerContactTemplateHtml += '<input type="Fax2" class="form-control" value="' + fax2 + '" id="id_fx2">';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Mobile1</label>';
                customerContactTemplateHtml += '<div class="col-md-6">';
                customerContactTemplateHtml += '<input type="Mobile1" class="form-control" value="' + mobile1 + '" id="id_mb1">';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Mobile2</label>';
                customerContactTemplateHtml += '<div class="col-md-6">';
                customerContactTemplateHtml += '<input type="Mobile2" class="form-control" value="' + mobile2 + '" id="id_mb2">';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Email1</label>';
                customerContactTemplateHtml += '<div class="col-md-6">';
                customerContactTemplateHtml += '<input type="Email1" class="form-control" value="' + email1 + '" id="id_em1">';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Email2</label>';
                customerContactTemplateHtml += '<div class="col-md-6">';
                customerContactTemplateHtml += '<input type="Email2" class="form-control" value="' + email2 + '" id="id_em2">';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Reports To</label>';
                customerContactTemplateHtml += '<div class="col-md-6">';
                customerContactTemplateHtml += '<input type="Reports To" class="form-control" value="' + reportsTo + '" id="id_rto">';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Assistant Name</label>';
                customerContactTemplateHtml += '<div class="col-md-6">';
                customerContactTemplateHtml += '<input type="Assistant Name" class="form-control" value="' + assistancenm + '" id="id_anm">';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Spouse Name</label>';
                customerContactTemplateHtml += '<div class="col-md-6">';
                customerContactTemplateHtml += '<input type="Spouse Name" class="form-control" value="' + spousenm + '" id="id_spnm">';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Birth Date</label>';
                customerContactTemplateHtml += '<div class="col-md-6">';
                customerContactTemplateHtml += '<input class="form-control form-control-inline date-picker" data-date-format="dd-mm-yyyy" size="16" type="text" value="' + bdate + '" id="id_bdate"/>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Anniversary Date</label>';
                customerContactTemplateHtml += '<div class="col-md-6">';
                customerContactTemplateHtml += '<input class="form-control form-control-inline date-picker" data-date-format="dd-mm-yyyy" size="16" type="text" value="' + adate + '" id="id_adate"/>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '<div class="form-group">';
                customerContactTemplateHtml += '<label class="col-md-6 control-label">Designation</label>';
                customerContactTemplateHtml += '<div class="col-md-6">';
                customerContactTemplateHtml += '<input type="Designation" class="form-control" value="' + designation + '" id="id_de">';
                customerContactTemplateHtml += '</div>';
                customerContactTemplateHtml += '</div>';

                customerContactTemplateHtml += '<div class="buttons">';
                customerContactTemplateHtml += '<button type="submit" class="btn btn-info" onclick="editContact(' + customerContactId + ');return false;">Save</button>';
                customerContactTemplateHtml += '&nbsp;';
                customerContactTemplateHtml += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
                customerContactTemplateHtml += '</div>';

                $('#id_custContact').append(customerContactTemplateHtml);
                //                $('#id_editbutton').append(editButton);
                $('.date-picker').datepicker({
                    rtl: App.isRTL(),
                    autoclose: true
                });
            }
        }
    });
}

function editCustomerOnEdit(customerId) {
    var customerName = document.getElementById("id_customerNames").value;
    var customerPrintAs = document.getElementById("id_customerPrintAss").value;
    var customerLocationIdentifier = document.getElementById("id_customerLocations").value;
    var isHeadOffice = document.getElementById("id_isHeadOffices").checked;
    var indexOfTerritory = document.getElementById("id_territorys");
    var territory = indexOfTerritory.options[indexOfTerritory.selectedIndex].text;
    var indexOfIndustry = document.getElementById("id_industrys");
    var industry = indexOfIndustry.options[indexOfIndustry.selectedIndex].text;
    var address = document.getElementById("id_addresss").value;
    var phone1 = document.getElementById("id_phones1").value;
    var phone2 = document.getElementById("id_phones2").value;
    var phone3 = document.getElementById("id_phones3").value;
    var fax1 = document.getElementById("id_faxs1").value;
    var fax2 = document.getElementById("id_faxs2").value;
    var email1 = document.getElementById("id_emails1").value;
    var email2 = document.getElementById("id_emails2").value;
    var email3 = document.getElementById("id_emails3").value;
    var website1 = document.getElementById("id_websites1").value;
    var website2 = document.getElementById("id_websites2").value;

    if (customerName.trim().length == 0) {
        fieldSenseTosterError("Customer Name cannot be empty", true);
        return false;
    }
    if (customerPrintAs.trim().length == 0) {
        fieldSenseTosterError("Please enter customer print as", true);
        return false;
    }
    if (customerLocationIdentifier.trim().length == 0) {
        fieldSenseTosterError("Please enter location identifier", true);
        return false;
    }
    if (industry == 'Select') {
        fieldSenseTosterError("Please select industry", true);
        return false;
    }
    if (address.trim().length == 0) {
        fieldSenseTosterError("Address cannot be empty", true);
        return false;
    }

    if (phone1.trim().length == 0) {
        fieldSenseTosterError("Please enter phone1", true);
        return false;
    }
    if (phone2.trim().length == 0) {
        fieldSenseTosterError("Please enter phone2", true);
        return false;
    }
    if (phone3.trim().length == 0) {
        fieldSenseTosterError("Please enter phone3", true);
        return false;
    }
    if (fax1.trim().length == 0) {
        fieldSenseTosterError("Please enter fax1", true);
        return false;
    }
    if (fax2.trim().length == 0) {
        fieldSenseTosterError("Please enter fax2", true);
        return false;
    }
    if (email1.trim().length == 0) {
        fieldSenseTosterError("Please enter email1", true);
        return false;
    }
    if (email2.trim().length == 0) {
        fieldSenseTosterError("Please enter email2", true);
        return false;
    }
    if (email3.trim().length == 0) {
        fieldSenseTosterError("Please enter email3", true);
        return false;
    }
    if (website1.trim().length == 0) {
        fieldSenseTosterError("Please enter website1", true);
        return false;
    }
    if (website2.trim().length == 0) {
        fieldSenseTosterError("Please enter website2", true);
        return false;
    }

    var customerObject = {
        "id": customerId,
        "customerName": customerName,
        "customerPrintas": customerPrintAs,
        "customerLocation": customerLocationIdentifier,
        "isHeadOffice": isHeadOffice,
        "territory": territory,
        "industry": industry,
        "customerAddress1": address,
        "customerPhone1": phone1,
        "customerPhone2": phone2,
        "customerPhone3": phone3,
        "customerFax1": fax1,
        "customerFax2": fax2,
        "customerEmail1": email1,
        "customerEmail2": email2,
        "customerEmail3": email3,
        "customerWebsiteUrl1": website1,
        "customerWebsiteUrl2": website2
    }

    var jsonData = JSON.stringify(customerObject);

    $.ajax({
        type: "PUT",
        url: fieldSenseURL + "/customer",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            var customerData = data.result;
            if (data.errorCode == '0000') {
                clevertap.event.push("Edit Customer", {
//                    "UserId": loginUserIdcookie,
//                    "User Name": loginUserNamecookie,
//                    "AccountId": accountIdcookie,
                    "Source": "Web",
                    "Account name": accountNamecookie,
                    "UserRolecookie": UserRolecookie,
                });
                fieldSenseTosterSuccess(data.errorMessage, true);
            } else {
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}

function editContact(customerContactId) {

    var firstName = document.getElementById("id_fnm").value;
    var middleName = document.getElementById("id_mnm").value;
    var lastName = document.getElementById("id_lnm").value;
    var phone1 = document.getElementById("id_ph1").value;
    var phone2 = document.getElementById("id_ph2").value;
    var phone3 = document.getElementById("id_ph3").value;
    var fax1 = document.getElementById("id_fx1").value;
    var fax2 = document.getElementById("id_fx2").value;
    var mobile1 = document.getElementById("id_mb1").value;
    var mobile2 = document.getElementById("id_mb2").value;
    var email1 = document.getElementById("id_em1").value;
    var email2 = document.getElementById("id_em2").value;
    var reportsTo = document.getElementById("id_rto").value;
    var assistancenm = document.getElementById("id_anm").value;
    var spousenm = document.getElementById("id_spnm").value;
    var birthdate = document.getElementById("id_bdate").value;
    var anniversarydate = document.getElementById("id_adate").value;
    var designation = document.getElementById("id_de").value;
    var bdate;
    var adate;
    if (birthdate.trim().length != 0) {
        var birthdateSplit = birthdate.split("-");
        var month = birthdateSplit[1];
        var date = birthdateSplit[0];
        var year = birthdateSplit[2];
        bdate = year + "-" + month + "-" + date;
    } else {
        bdate = "1800-01-01";
    }
    if (anniversarydate.trim().length != 0) {
        var anniversarydateSplit = anniversarydate.split("-");
        var m = anniversarydateSplit[1];
        var d = anniversarydateSplit[0];
        var y = anniversarydateSplit[2];
        adate = y + "-" + m + "-" + d;
    } else {
        adate = "1800-01-01";
    }

    if (firstName.trim().length == 0) {
        fieldSenseTosterError("First Name cannot be empty", true);
        return false;
    }
    if (firstName.trim().length > 50) {
        fieldSenseTosterError("First name can not be more than 50 characters", true);
        return false;
    }
    if (middleName.trim().length > 50) {
        fieldSenseTosterError("Middle name can not be more than 50 characters", true);
        return false;
    }

    if (lastName.trim().length != 0) {
        if (lastName.trim().length > 50) {
            fieldSenseTosterError("Last name can not be more than 50 characters", true);
            return false;
        }
    }
    if (phone1.trim().length != 0) {
        if (phone1.trim().length > 10) {
            fieldSenseTosterError("Phone1 can not be more than 10 digits", true);
            return false;
        }
    }
    if (phone2.trim().length != 0) {
        if (phone2.trim().length > 10) {
            fieldSenseTosterError("Phone2 can not be more than 10 digits", true);
            return false;
        }
    }
    if (phone3.trim().length != 0) {
        if (phone3.trim().length > 10) {
            fieldSenseTosterError("Phone3 can not be more than 10 digits", true);
            return false;
        }
    }
    if (fax1.trim().length != 0) {
        if (fax1.trim().length > 50) {
            fieldSenseTosterError("Fax1 can not be more than 50 digits", true);
            return false;
        }
    }
    if (fax2.trim().length != 0) {
        if (fax2.trim().length > 50) {
            fieldSenseTosterError("Fax2 can not be more than 50 digits", true);
            return false;
        }
    }
    if (mobile1.trim().length != 0) {
        if (mobile1.trim().length > 10) {
            fieldSenseTosterError("Mobile1 can not be more than 10 digits", true);
            return false;
        }
    }
    if (mobile2.trim().length != 0) {
        if (mobile2.trim().length > 10) {
            fieldSenseTosterError("Mobile2 can not be more than 10 digits", true);
            return false;
        }
    }
    if (email1.trim().length != 0) {
        if (email1.trim().length > 100) {
            fieldSenseTosterError("Email1 can not be more than 100 characters", true);
            return false;
        }
    }
    //    var emailPattern = /^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;
    //    if(!emailPattern.test(email1.trim())){
    //        fieldSenseTosterError("Invalid email address .",true);
    //        return false;
    //    }
    if (email2.trim().length != 0) {
        if (email2.trim().length > 100) {
            fieldSenseTosterError("Email2 can not be more than 100 characters", true);
            return false;
        }
    }
    //    if(!emailPattern.test(email2.trim())){
    //        fieldSenseTosterError("Invalid email address .",true);
    //        return false;
    //    }
    if (reportsTo.trim().length != 0) {
        if (reportsTo.trim().length > 100) {
            fieldSenseTosterError("Report to can not be more than 100 characters", true);
            return false;
        }
    }

    if (assistancenm.trim().length != 0) {
        if (assistancenm.trim().length > 100) {
            fieldSenseTosterError("Assistance name can not be more than 100 characters", true);
            return false;
        }
    }
    if (spousenm.trim().length != 0) {
        if (spousenm.trim().length > 100) {
            fieldSenseTosterError("Spouce name can not be more than 100 characters", true);
            return false;
        }
    }
    if (designation.trim().length != 0) {
        if (designation.trim().length > 100) {
            fieldSenseTosterError("Designation can not be more than 100 characters", true);
            return false;
        }
    }

    var customerContactObject = {
        "id": customerContactId,
        "firstName": firstName,
        "middleName": middleName,
        "lastName": lastName,
        "phone1": phone1,
        "phone2": phone2,
        "phone3": phone3,
        "fax1": fax1,
        "fax2": fax2,
        "mobile1": mobile1,
        "mobile2": mobile2,
        "email1": email1,
        "email2": email2,
        "reportTo": reportsTo,
        "assistantName": assistancenm,
        "spouseName": spousenm,
        "birthDate": bdate,
        "anniversaryDate": adate,
        "designation": designation
    }

    var jsonData = JSON.stringify(customerContactObject);

    $.ajax({
        type: "PUT",
        url: fieldSenseURL + "/customerContact",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            var customerContactData = data.result;
            if (data.errorCode == '0000') {
                fieldSenseTosterSuccess(data.errorMessage, true);
            } else {
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}

function clearId() {
    $('#customertmt').html('');
    showCustomers();
    $('#Contacts').css("display", "none");
    $('#ed_details').css("display", "none");
    $('#div21').css("display", "none");
    $('#id_fnm').val('');
    $('#id_mnm').val('');
    $('#id_lnm').val('');
    $('#id_ph1').val('');
    $('#id_ph2').val('');
    $('#id_ph3').val('');
    $('#id_fx1').val('');
    $('#id_fx2').val('');
    $('#id_mb1').val('');
    $('#id_mob2').val('');
    $('#id_em1').val('');
    $('#id_em2').val('');
    $('#id_rTo').val('');
    $('#id_anm').val('');
    $('#id_spnm').val('');
    $('#id_bdate').val('');
    $('#id_adate').val('');
    $('#id_de').val('');

    $('#fnm').val('');
    $('#mnm').val('');
    $('#lnm').val('');
    $('#ph1').val('');
    $('#ph2').val('');
    $('#ph3').val('');
    $('#fx1').val('');
    $('#fx2').val('');
    $('#mb1').val('');
    $('#mb2').val('');
    $('#em1').val('');
    $('#em2').val('');
    $('#rto').val('');
    $('#anm').val('');
    $('#spnm').val('');
    $('#bdt').val('');
    $('#adt').val('');
    $('#de').val('');

    $('#fnm').val('');
    $('#mnm').val('');
    $('#lnm').val('');
    $('#ph1').val('');
    $('#ph2').val('');
    $('#ph3').val('');
    $('#fx1').val('');
    $('#fx2').val('');
    $('#mb1').val('');
    $('#mb2').val('');
    $('#em1').val('');
    $('#em2').val('');
    $('#rto').val('');
    $('#spnm').val('');
    $('#bdt').val('');
    $('#adt').val('');
    $('#de').val('');
}

function deleteCustomer(customerId, customernm) {
    //var deletes=confirm("Are you sure you want to delete "+customernm+"");
    //if (deletes==true)
    // {
    bootbox.dialog({
        message: "Are you sure you want to delete " + customernm + " ?",
        title: "Delete Customer",
        buttons: {
            yes: {
                label: "Yes",
                className: "btn-default",
                callback: function () {
                    $('#pleaseWaitDialog').modal('show');
                    $.ajax({
                        type: "DELETE",
                        url: fieldSenseURL + "/customer/" + customerId,
                        contentType: "application/json; charset=utf-8",
                        headers: {
                            "userToken": userToken
                        },
                        crossDomain: false,
                        cache: false,
                        dataType: 'json',
                        success: function (data) {
                            clevertap.event.push("Delete customers", {
//                    "UserId": loginUserIdcookie,
//                    "User Name": loginUserNamecookie,
//                    "AccountId": accountIdcookie,
                                "Source": "Web",
                                "Account name": accountNamecookie,
                                "UserRolecookie": UserRolecookie,
                            });
                            var customerData = data.result;
                            if (data.errorCode == '0000') {
                                $('#customertmt').html('');
                                fieldSenseTosterSuccess(data.errorMessage, true);
                                showCustomers();
                            } else {
                                $('#pleaseWaitDialog').modal('hide');
                                FieldSenseInvalidToken(data.errorMessage);
                            }
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            $('#pleaseWaitDialog').modal('hide');
                            alert("in error:" + thrownError);
                        }
                    });
                }
            },
            no: {
                label: "No",
                className: "btn-default",
                callback: function () {
                    //$('#pleaseWaitDialog').modal('hide');
                    //return false;
                }
            }
        }
    });
    // }
}
function deleteCustomerContact(customerContactId, contactnm, customerId) {
    //var deletes=confirm("Are you sure you want to delete "+contactnm+"");
    // if (deletes==true)
    // {
    bootbox.dialog({
        message: "Are you sure you want to delete " + customernm + " ?",
        title: "Delete Customer",
        buttons: {
            yes: {
                label: "Yes",
                className: "btn-default",
                callback: function () {
                    $('#pleaseWaitDialog').modal('show');
                    $.ajax({
                        type: "DELETE",
                        url: fieldSenseURL + "/customerContact/" + customerContactId,
                        contentType: "application/json; charset=utf-8",
                        headers: {
                            "userToken": userToken
                        },
                        crossDomain: false,
                        cache: false,
                        dataType: 'json',
                        success: function (data) {
                            var contactData = data.result;
                            if (data.errorCode == '0000') {
                                
                                 clevertap.event.push("Delete contact", {
//                    "UserId": loginUserIdcookie,
//                    "User Name": loginUserNamecookie,
//                    "AccountId": accountIdcookie,
                                "Source": "Web",
                                "Account name": accountNamecookie,
                                "UserRolecookie": UserRolecookie,
                            });
                            
                                fieldSenseTosterSuccess(data.errorMessage, true);
                                $('#id_editcontact').html('');
                                $('#pleaseWaitDialog').modal('hide');
                                contactgrid(customerId);
                            } else {
                                FieldSenseInvalidToken(data.errorMessage);
                                $('#pleaseWaitDialog').modal('hide')
                            }
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            $('#pleaseWaitDialog').modal('hide');
                            alert("in error:" + thrownError);
                        }
                    });
                }
            },
            no: {
                label: "No",
                className: "btn-default",
                callback: function () {
                    //$('#pleaseWaitDialog').modal('hide');
                    //return false;
                }
            }
        }
    });
    //}
}

function createContactOnedit(customerId) {

    var firstName = document.getElementById("fnm").value;
    var middleName = document.getElementById("mnm").value;
    var lastName = document.getElementById("lnm").value;
    var phone1 = document.getElementById("ph1").value;
    var phone2 = document.getElementById("ph2").value;
    var phone3 = document.getElementById("ph3").value;
    var fax1 = document.getElementById("fx1").value;
    var fax2 = document.getElementById("fx2").value;
    var mobile1 = document.getElementById("mb1").value;
    var mobile2 = document.getElementById("mb2").value;
    var email1 = document.getElementById("em1").value;
    var email2 = document.getElementById("em2").value;
    var reportsTo = document.getElementById("rto").value;
    var assistancenm = document.getElementById("anm").value;
    var spousenm = document.getElementById("spnm").value;
    var birthdate = document.getElementById("bdt").value;
    var anniversarydate = document.getElementById("adt").value;
    var designation = document.getElementById("de").value;
    var bdate;
    var adate;
    if (birthdate.trim().length != 0) {
        var birthdateSplit = birthdate.split("-");
        var month = birthdateSplit[1];
        var date = birthdateSplit[0];
        var year = birthdateSplit[2];
        bdate = year + "-" + month + "-" + date;
    } else {
        bdate = "1800-01-01";
    }
    if (anniversarydate.trim().length != 0) {
        var anniversarydateSplit = anniversarydate.split("-");
        var m = anniversarydateSplit[1];
        var d = anniversarydateSplit[0];
        var y = anniversarydateSplit[2];
        adate = y + "-" + m + "-" + d;
    } else {
        adate = "1800-01-01";
    }

    if (firstName.trim().length == 0) {
        fieldSenseTosterError("First Name cannot be empty", true);
        return false;
    }
    if (firstName.trim().length > 50) {
        fieldSenseTosterError("First name can not be more than 50 characters", true);
        return false;
    }
    if (middleName.trim().length > 50) {
        fieldSenseTosterError("Middle name can not be more than 50 characters", true);
        return false;
    }
    if (lastName.trim().length != 0) {
        if (lastName.trim().length > 50) {
            fieldSenseTosterError("Last name can not be more than 50 characters", true);
            return false;
        }
    }
    if (phone1.trim().length != 0) {
        if (phone1.trim().length > 10) {
            fieldSenseTosterError("Phone1 can not be more than 10 digits", true);
            return false;
        }
    }
    if (phone2.trim().length != 0) {
        if (phone2.trim().length > 10) {
            fieldSenseTosterError("Phone2 can not be more than 10 digits", true);
            return false;
        }
    }
    if (phone3.trim().length != 0) {
        if (phone3.trim().length > 10) {
            fieldSenseTosterError("Phone3 can not be more than 10 digits", true);
            return false;
        }
    }
    if (fax1.trim().length != 0) {
        if (fax1.trim().length > 50) {
            fieldSenseTosterError("Fax1 can not be more than 50 digits", true);
            return false;
        }
    }
    if (fax2.trim().length != 0) {
        if (fax2.trim().length > 50) {
            fieldSenseTosterError("Fax2 can not be more than 50 digits", true);
            return false;
        }
    }
    if (mobile1.trim().length != 0) {
        if (mobile1.trim().length > 10) {
            fieldSenseTosterError("Mobile1 can not be more than 10 digits", true);
            return false;
        }
    }
    if (mobile2.trim().length != 0) {
        if (mobile2.trim().length > 10) {
            fieldSenseTosterError("Mobile2 can not be more than 10 digits", true);
            return false;
        }
    }
    if (email1.trim().length != 0) {
        if (email1.trim().length > 100) {
            fieldSenseTosterError("Email1 can not be more than 100 characters", true);
            return false;
        }
    }
    //    var emailPattern = /^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;
    //    if(!emailPattern.test(email1.trim())){
    //        fieldSenseTosterError("Invalid email address .",true);
    //        return false;
    //    }
    if (email2.trim().length != 0) {
        if (email2.trim().length > 100) {
            fieldSenseTosterError("Email2 can not be more than 100 characters", true);
            return false;
        }
    }
    //    if(!emailPattern.test(email2.trim())){
    //        fieldSenseTosterError("Invalid email address .",true);
    //        return false;
    //    }
    if (reportsTo.trim().length != 0) {
        if (reportsTo.trim().length > 100) {
            fieldSenseTosterError("Report to can not be more than 100 characters", true);
            return false;
        }
    }

    if (assistancenm.trim().length != 0) {
        if (assistancenm.trim().length > 100) {
            fieldSenseTosterError("Assistance name can not be more than 100 characters", true);
            return false;
        }
    }
    if (spousenm.trim().length != 0) {
        if (spousenm.trim().length > 100) {
            fieldSenseTosterError("Spouce name can not be more than 100 characters", true);
            return false;
        }
    }
    if (designation.trim().length != 0) {
        if (designation.trim().length > 100) {
            fieldSenseTosterError("Designation can not be more than 100 characters", true);
            return false;
        }
    }

    var customerContactObject = {
        "customerId": customerId,
        "firstName": firstName,
        "middleName": middleName,
        "lastName": lastName,
        "phone1": phone1,
        "phone2": phone2,
        "phone3": phone3,
        "fax1": fax1,
        "fax2": fax2,
        "mobile1": mobile1,
        "mobile2": mobile2,
        "email1": email1,
        "email2": email2,
        "reportTo": reportsTo,
        "assistantName": assistancenm,
        "spouseName": spousenm,
        "birthDate": bdate,
        "anniversaryDate": adate,
        "designation": designation
    }

    var jsonData = JSON.stringify(customerContactObject);

    $.ajax({
        type: "POST",
        url: fieldSenseURL + "/customerContact",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            var customerContactData = data.result;
            if (data.errorCode == '0000') {
                fieldSenseTosterSuccess(data.errorMessage, true);
                $('#id_editcontact').html('');
                contactgrid(customerId);
            } else {
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}

function contactgrid(customerId) {
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/customerContact/customer/" + customerId,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            $('#id_editcontact').html('');
            var contactData = data.result;
            if (data.errorCode == '0000') {
                var contactShowTemplateHtml = '';

                for (var i = 0; i < contactData.length; i++) {
                    var customerContactId = contactData[i].id;
                    var firstName = contactData[i].firstName;
                    var middleName = contactData[i].middleName;
                    var lastName = contactData[i].lastName;
                    var phone1 = contactData[i].phone1;
                    var phone2 = contactData[i].phone2;
                    var phone3 = contactData[i].phone3;
                    var fax1 = contactData[i].fax1;
                    var fax2 = contactData[i].fax2;
                    var mobile1 = contactData[i].mobile1;
                    var mobile2 = contactData[i].mobile2;
                    var email1 = contactData[i].email1;
                    var email2 = contactData[i].email2;
                    var reportsTo = contactData[i].reportTo;
                    var assistancenm = contactData[i].assistantName;
                    var spousenm = contactData[i].spouseName;
                    var birthdate = contactData[i].birthDate;
                    var anniversarydate = contactData[i].anniversaryDate;
                    var designation = contactData[i].designation;
                    var fullName = firstName + ' ' + middleName + ' ' + lastName;
                    contactShowTemplateHtml += '<tr>';
                    contactShowTemplateHtml += '<td class="highlight">' + firstName + ' ' + middleName + ' ' + lastName + '</td>';
                    contactShowTemplateHtml += '<td class="hidden-xs">' + mobile1 + '</td>';
                    contactShowTemplateHtml += '<td>' + email1 + '</td>';
                    contactShowTemplateHtml += '<td>';
                    contactShowTemplateHtml += '<a target="21" class="showSingle2 btn btn-default btn-xs purple" onclick="showOneContact(' + customerContactId + ')"><i class="fa fa-edit"></i> Edit</a>';
                    contactShowTemplateHtml += '<a href="#" class="btn btn-default btn-xs purple" onclick="deleteCustomerContact(\'' + customerContactId + '\',\'' + fullName + '\',\'' + customerId + '\')"><i class="fa fa-trash-o"></i> Delete</a>';
                    contactShowTemplateHtml += '</td>';
                    contactShowTemplateHtml += '</tr>';
                }
                $('#id_editcontact').append(contactShowTemplateHtml);
            }
        }
    });
}
function searchCustomerWithText() {
    var searchText = $('#id_searchText').val();

    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/search/customers/" + searchText,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            var customerData = data.result;
            var customerShowTemplateHtml = '';
            customerShowTemplateHtml += '<table class="table table-striped table-bordered table-hover" id="sample_2">';
            customerShowTemplateHtml += '<thead>';
            customerShowTemplateHtml += '<tr>';
            customerShowTemplateHtml += '<th>';
            customerShowTemplateHtml += 'Customer Name';
            customerShowTemplateHtml += '</th>';
            customerShowTemplateHtml += '<th>';
            customerShowTemplateHtml += 'Phone';
            customerShowTemplateHtml += '</th>';
            customerShowTemplateHtml += '<th>';
            customerShowTemplateHtml += 'Email';
            customerShowTemplateHtml += '</th>';
            //            customerShowTemplateHtml+='<th>';
            //            customerShowTemplateHtml+='Address';
            //            customerShowTemplateHtml+='</th>';
            customerShowTemplateHtml += '<th class="hidden-xs">';
            customerShowTemplateHtml += 'Industry';
            customerShowTemplateHtml += '</th>';
            customerShowTemplateHtml += '<th class="hidden-xs">';
            customerShowTemplateHtml += 'Territory';
            customerShowTemplateHtml += '</th>';
            customerShowTemplateHtml += '<th>';
            customerShowTemplateHtml += '</th>';
            customerShowTemplateHtml += '</tr>';
            customerShowTemplateHtml += '</thead>';
            customerShowTemplateHtml += '<tbody>';
            if (data.errorCode == '0000') {
                for (var i = 0; i < customerData.length; i++) {
                    var customerId = customerData[i].id;
                    var customerName = customerData[i].customerName;
                    var phone = customerData[i].customerPhone1;
                    var email = customerData[i].customerEmail1;
                    var address = customerData[i].customerAddress1;
                    var industry = customerData[i].industry;
                    var territory = customerData[i].territory;
                    customerShowTemplateHtml += '<tr>';
                    customerShowTemplateHtml += '<td><a data-toggle="modal" href="#responsive1" onClick="showOneCustomer(' + customerId + ')">' + customerName + '</a></td>';
                    customerShowTemplateHtml += '<td>' + phone + '</td>';
                    customerShowTemplateHtml += '<td>' + email + '</td>';
                    //                    customerShowTemplateHtml+='<td>'+address+ '</td>';
                    customerShowTemplateHtml += '<td class="hidden-xs">' + industry + '</td>';
                    customerShowTemplateHtml += '<td class="hidden-xs">' + territory + '</td>';
                    customerShowTemplateHtml += '<td>';
                    //                    customerShowTemplateHtml+='<a data-toggle="modal" href="#responsive2" class="btn btn-default btn-xs purple" onclick="editCustomerDetails('+customerId+')"><i class="fa fa-edit"></i> Edit</a>';
                    //                    customerShowTemplateHtml+='<a href="#" class="btn btn-default btn-xs purple" onclick="deleteCustomer(\''+customerId+'\',\''+customerName+'\')"><i class="fa fa-trash-o"></i> Delete</a>';
                    customerShowTemplateHtml += '<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Edit" data-toggle="modal" href="#responsive2" onclick="editCustomerDetails(' + customerId + ')"><i class="fa fa-edit"></i></button>';
                    customerShowTemplateHtml += '<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Delete" onclick="deleteCustomer(\'' + customerId + '\',\'' + customerName + '\')"><i class="fa fa-trash-o"></i></button>';
                    customerShowTemplateHtml += '</td>';
                    customerShowTemplateHtml += '</tr>';
                }
            }
            customerShowTemplateHtml += '</tbody>';
            customerShowTemplateHtml += '</table>';
            $('#datatblcustomer').html(customerShowTemplateHtml);
            TableAdvanced.init();
        }
    });
}

function searchCustomerWithSelection() {
    //    var recentCustomers=new Array();
    var index;
    var createdOn = document.getElementById("id_added").checked;
    if (createdOn) {
        index = recentCustomers.indexOf("cretedOn");
        if (index < 0) {
            recentCustomers.push("cretedOn");
        }
    } else {
        index = recentCustomers.indexOf("cretedOn");
        if (index > -1) {
            recentCustomers.splice(index, 1);
        }
    }
    var modifiedOn = document.getElementById("id_moified").checked;
    if (modifiedOn) {
        index = recentCustomers.indexOf("modifiedOn");
        if (index < 0) {
            recentCustomers.push("modifiedOn");
        }
    } else {
        index = recentCustomers.indexOf("modifiedOn");
        if (index > -1) {
            recentCustomers.splice(index, 1);
        }
    }
    //    var territory=new Array();
    var West = document.getElementById("id_west").checked;
    if (West) {
        index = territorycustomers.indexOf("West");
        if (index < 0) {
            territorycustomers.push("West");
        }
        //        territorycustomers.push("West") ;
    } else {
        index = territorycustomers.indexOf("West");
        if (index > -1) {
            territorycustomers.splice(index, 1);
        }
    }
    var East = document.getElementById("id_east").checked;
    if (East) {
        //        territorycustomers.push("East") ;
        index = territorycustomers.indexOf("East");
        if (index < 0) {
            territorycustomers.push("East");
        }
    } else {
        index = territorycustomers.indexOf("East");
        if (index > -1) {
            territorycustomers.splice(index, 1);
        }
    }
    var North = document.getElementById("id_north").checked;
    if (North) {
        //        territorycustomers.push("North") ;
        index = territorycustomers.indexOf("North");
        if (index < 0) {
            territorycustomers.push("North");
        }
    } else {
        index = territorycustomers.indexOf("North");
        if (index > -1) {
            territorycustomers.splice(index, 1);
        }
    }
    var South = document.getElementById("id_south").checked;
    if (South) {
        //        territorycustomers.push("South") ;
        index = territorycustomers.indexOf("South");
        if (index < 0) {
            territorycustomers.push("South");
        }
    } else {
        index = territorycustomers.indexOf("South");
        if (index > -1) {
            territorycustomers.splice(index, 1);
        }
    }
    //    var industry=new Array();
    var Financial = document.getElementById("id_financial").checked;
    if (Financial) {
        //        industryCustomers.push("Financial");
        index = industryCustomers.indexOf("Financial");
        if (index < 0) {
            industryCustomers.push("Financial");
        }
    } else {
        index = industryCustomers.indexOf("Financial");
        if (index > -1) {
            industryCustomers.splice(index, 1);
        }
    }
    var Banking = document.getElementById("id_banking").checked;
    if (Banking) {
        //        industryCustomers.push("Banking");
        index = industryCustomers.indexOf("Banking");
        if (index < 0) {
            industryCustomers.push("Banking");
        }
    } else {
        index = industryCustomers.indexOf("Banking");
        if (index > -1) {
            industryCustomers.splice(index, 1);
        }
    }
    var Software = document.getElementById("id_software").checked;
    if (Software) {
        //        industryCustomers.push("Software");
        index = industryCustomers.indexOf("Software");
        if (index < 0) {
            industryCustomers.push("Software");
        }
    } else {
        index = industryCustomers.indexOf("Software");
        if (index > -1) {
            industryCustomers.splice(index, 1);
        }
    }
    var Brokerage = document.getElementById("id_brokerage").checked;
    if (Brokerage) {
        //        industryCustomers.push("Brokerage");
        index = industryCustomers.indexOf("Brokerage");
        if (index < 0) {
            industryCustomers.push("Brokerage");
        }
    } else {
        index = industryCustomers.indexOf("Brokerage");
        if (index > -1) {
            industryCustomers.splice(index, 1);
        }
    }
    var Biotechnology = document.getElementById("id_biotech").checked;
    if (Biotechnology) {
        //        industryCustomers.push("Biotechnology");
        index = industryCustomers.indexOf("Biotechnology");
        if (index < 0) {
            industryCustomers.push("Biotechnology");
        }
    } else {
        index = industryCustomers.indexOf("Biotechnology");
        if (index > -1) {
            industryCustomers.splice(index, 1);
        }
    }
    var InvestmentBanking = document.getElementById("id_investment").checked;
    if (InvestmentBanking) {
        //        industryCustomers.push("Investment Banking");
        index = industryCustomers.indexOf("Investment Banking");
        if (index < 0) {
            industryCustomers.push("Investment Banking");
        }
    } else {
        index = industryCustomers.indexOf("Investment Banking");
        if (index > -1) {
            industryCustomers.splice(index, 1);
        }
    }
    var RealEstate = document.getElementById("id_real").checked;
    if (RealEstate) {
        //        industryCustomers.push("Real Estate");
        index = industryCustomers.indexOf("Real Estate");
        if (index < 0) {
            industryCustomers.push("Real Estate");
        }
    } else {
        index = industryCustomers.indexOf("Real Estate");
        if (index > -1) {
            industryCustomers.splice(index, 1);
        }
    }
    if (recentCustomers.length == 0) {
        $('.cls_clearRecent').css("display", "none");
    } else {
        $('.cls_clearRecent').css("display", "block");
    }
    if (territorycustomers.length == 0) {
        $('.cls_clearTerritory').css("display", "none");
    } else {
        $('.cls_clearTerritory').css("display", "block");
    }
    if (industryCustomers.length == 0) {
        $('.cls_clearIndustry').css("display", "none");
    } else {
        $('.cls_clearIndustry').css("display", "block");
    }
    var searchObject = {
        "recentItems": recentCustomers,
        "trritory": territorycustomers,
        "industry": industryCustomers
    }
    var jsonData = JSON.stringify(searchObject);
    $.ajax({
        type: "POST",
        url: fieldSenseURL + "/search/customers",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            var customerData = data.result;
            var customerShowTemplateHtml = '';
            customerShowTemplateHtml += '<table class="table table-striped table-bordered table-hover" id="sample_2">';
            customerShowTemplateHtml += '<thead>';
            customerShowTemplateHtml += '<tr>';
            customerShowTemplateHtml += '<th>';
            customerShowTemplateHtml += 'Customer Name';
            customerShowTemplateHtml += '</th>';
            customerShowTemplateHtml += '<th>';
            customerShowTemplateHtml += 'Phone';
            customerShowTemplateHtml += '</th>';
            customerShowTemplateHtml += '<th>';
            customerShowTemplateHtml += 'Email';
            customerShowTemplateHtml += '</th>';
            //            customerShowTemplateHtml+='<th>';
            //            customerShowTemplateHtml+='Address';
            //            customerShowTemplateHtml+='</th>';
            customerShowTemplateHtml += '<th class="hidden-xs">';
            customerShowTemplateHtml += 'Industry';
            customerShowTemplateHtml += '</th>';
            customerShowTemplateHtml += '<th class="hidden-xs">';
            customerShowTemplateHtml += 'Territory';
            customerShowTemplateHtml += '</th>';
            customerShowTemplateHtml += '<th>';
            customerShowTemplateHtml += '</th>';
            customerShowTemplateHtml += '</tr>';
            customerShowTemplateHtml += '</thead>';
            customerShowTemplateHtml += '<tbody>';
            if (data.errorCode == '0000') {
                for (var i = 0; i < customerData.length; i++) {
                    var customerId = customerData[i].id;
                    var customerName = customerData[i].customerName;
                    var phone = customerData[i].customerPhone1;
                    var email = customerData[i].customerEmail1;
                    var address = customerData[i].customerAddress1;
                    var industry = customerData[i].industry;
                    var territory = customerData[i].territory;
                    customerShowTemplateHtml += '<tr>';
                    customerShowTemplateHtml += '<td><a data-toggle="modal" href="#responsive1" onClick="showOneCustomer(' + customerId + ')">' + customerName + '</a></td>';
                    customerShowTemplateHtml += '<td>' + phone + '</td>';
                    customerShowTemplateHtml += '<td>' + email + '</td>';
                    //                    customerShowTemplateHtml+='<td>'+address+ '</td>';
                    customerShowTemplateHtml += '<td class="hidden-xs">' + industry + '</td>';
                    customerShowTemplateHtml += '<td class="hidden-xs">' + territory + '</td>';
                    customerShowTemplateHtml += '<td>';
                    //                    customerShowTemplateHtml+='<a data-toggle="modal" href="#responsive2" class="btn btn-default btn-xs purple" onclick="editCustomerDetails('+customerId+')"><i class="fa fa-edit"></i> Edit</a>';
                    //                    customerShowTemplateHtml+='<a href="#" class="btn btn-default btn-xs purple" onclick="deleteCustomer(\''+customerId+'\',\''+customerName+'\')"><i class="fa fa-trash-o"></i> Delete</a>';
                    customerShowTemplateHtml += '<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Edit" data-toggle="modal" href="#responsive2" onclick="editCustomerDetails(' + customerId + ')"><i class="fa fa-edit"></i></button>';
                    customerShowTemplateHtml += '<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Delete" onclick="deleteCustomer(\'' + customerId + '\',\'' + customerName + '\')"><i class="fa fa-trash-o"></i></button>';
                    customerShowTemplateHtml += '</td>';
                    customerShowTemplateHtml += '</tr>';
                }
            }
            customerShowTemplateHtml += '</tbody>';
            customerShowTemplateHtml += '</table>';
            $('#datatblcustomer').html(customerShowTemplateHtml);
            TableAdvanced.init();
        }
    });
}
function clearFilter() {
    recentCustomers = new Array();
    industryCustomers = new Array();
    territorycustomers = new Array();
    leftSideMenu();
    showCustomers();
}
function leftSideMenu() {
    var leftSideMenuTemplate = '';
    leftSideMenuTemplate += '<ul class="page-sidebar-menu">';
    leftSideMenuTemplate += '<li class="start active">';
    leftSideMenuTemplate += '<a href="#">';
    leftSideMenuTemplate += '<i class="fa fa-search"></i>';
    leftSideMenuTemplate += '<span class="title">';
    leftSideMenuTemplate += 'Filter Customers';
    leftSideMenuTemplate += '</span>';
    leftSideMenuTemplate += '</a>';
    leftSideMenuTemplate += '</li>';
    leftSideMenuTemplate += '<li class="opt">';
    leftSideMenuTemplate += '<a href="javascript:;">';
    leftSideMenuTemplate += '<i class="fa fa-cogs"></i>';
    leftSideMenuTemplate += 'Recent';
    leftSideMenuTemplate += '</a>';

    leftSideMenuTemplate += '<a href="javascript:;" class="cl cls_clearRecent" style="display:none;">';
    leftSideMenuTemplate += '<span onClick="clearRecentCustomers();">Clear</span>';
    leftSideMenuTemplate += '</a>';

    leftSideMenuTemplate += '<div multiple class="form-control multiselect">';
    leftSideMenuTemplate += '<span class="cls_addRecentCustomerFilter">';
    leftSideMenuTemplate += '<label><input type="checkbox" name="option[]" value="1" id="id_added" onChange="searchCustomerWithSelection();return false;" />Added</label>';
    leftSideMenuTemplate += '<label><input type="checkbox" name="option[]" value="2" id="id_moified" onChange="searchCustomerWithSelection();return false;"/>Modified</label>';
    leftSideMenuTemplate += '</span>';
    leftSideMenuTemplate += '</div>';
    leftSideMenuTemplate += '</li>';
    leftSideMenuTemplate += '<li class="opt">';
    leftSideMenuTemplate += '<a href="javascript:;">';
    leftSideMenuTemplate += '<i class="fa fa-cogs"></i>';
    leftSideMenuTemplate += 'Territory';
    leftSideMenuTemplate += '</a>';

    leftSideMenuTemplate += '<a href="javascript:;" class="cl cls_clearTerritory" style="display:none;">';
    leftSideMenuTemplate += '<span onClick="clearTerritoryCustomers();">Clear</span>';
    leftSideMenuTemplate += '</a>';

    leftSideMenuTemplate += '<div multiple class="form-control multiselect">';
    leftSideMenuTemplate += '<span class="cls_addTerritoryCustomerFilter">';
    leftSideMenuTemplate += '<label><input type="checkbox" name="option[]" value="1" id="id_west" onChange="searchCustomerWithSelection();return false;"/>West</label>';
    leftSideMenuTemplate += '<label><input type="checkbox" name="option[]" value="2" id="id_east" onChange="searchCustomerWithSelection();return false;"/>East</label>';
    leftSideMenuTemplate += '<label><input type="checkbox" name="option[]" value="3" id="id_north" onChange="searchCustomerWithSelection();return false;"/>North</label>';
    leftSideMenuTemplate += '<label><input type="checkbox" name="option[]" value="4" id="id_south" onChange="searchCustomerWithSelection();return false;"/>South</label>';
    leftSideMenuTemplate += '</span>';
    leftSideMenuTemplate += '</div>';
    leftSideMenuTemplate += '</li>';
    leftSideMenuTemplate += '<li class="opt">';
    leftSideMenuTemplate += '<a href="javascript:;">';
    leftSideMenuTemplate += '<i class="fa fa-cogs"></i>';
    leftSideMenuTemplate += 'Industry';
    leftSideMenuTemplate += '</a>';

    leftSideMenuTemplate += '<a href="javascript:;" class="cl cls_clearIndustry" style="display:none;">';
    leftSideMenuTemplate += '<span onClick="clearIndustryCustomers();">Clear</span>';
    leftSideMenuTemplate += '</a>';

    leftSideMenuTemplate += '<div multiple class="form-control multiselect">';
    leftSideMenuTemplate += '<span class="cls_addIndustryCustomerFilter">';
    leftSideMenuTemplate += '<label><input type="checkbox" name="option[]" value="1" id="id_financial" onChange="searchCustomerWithSelection();return false;"/>Financial</label>';
    leftSideMenuTemplate += '<label><input type="checkbox" name="option[]" value="2" id="id_banking" onChange="searchCustomerWithSelection();return false;"/>Banking</label>';
    leftSideMenuTemplate += '<label><input type="checkbox" name="option[]" value="3" id="id_software" onChange="searchCustomerWithSelection();return false;"/>Software</label>';
    leftSideMenuTemplate += '<label><input type="checkbox" name="option[]" value="4" id="id_brokerage" onChange="searchCustomerWithSelection();return false;"/>Brokerage</label>';
    leftSideMenuTemplate += '<label><input type="checkbox" name="option[]" value="5" id="id_biotech" onChange="searchCustomerWithSelection();return false;"/>Biotechnology</label>';
    leftSideMenuTemplate += '<label><input type="checkbox" name="option[]" value="6" id="id_investment" onChange="searchCustomerWithSelection();return false;"/>Investment Banking</label>';
    leftSideMenuTemplate += '<label><input type="checkbox" name="option[]" value="7" id="id_real" onChange="searchCustomerWithSelection();return false;"/>Real Estate</label>';
    leftSideMenuTemplate += '</span>';
    leftSideMenuTemplate += '</div>';
    leftSideMenuTemplate += '</li>';
    leftSideMenuTemplate += '<a href="#" class="btn btn-warning" onclick="clearFilter()">Clear Filters</a>';
    leftSideMenuTemplate += '<li class="last">';
    leftSideMenuTemplate += '</li>';
    leftSideMenuTemplate += '</ul>';
    $('#id_leftMenu').html(leftSideMenuTemplate);
    App.init();
}
function clearRecentCustomers() {
    recentCustomers = new Array();
    $('.cls_clearRecent').css("display", "none");
    var clearRecentCustomersHtml = '';
    clearRecentCustomersHtml += '<span class="cls_addRecentCustomerFilter">';
    clearRecentCustomersHtml += '<label><input type="checkbox" name="option[]" value="1" id="id_added" onChange="searchCustomerWithSelection();return false;" />Added</label>';
    clearRecentCustomersHtml += '<label><input type="checkbox" name="option[]" value="2" id="id_moified" onChange="searchCustomerWithSelection();return false;"/>Modified</label>';
    clearRecentCustomersHtml += '</span>';
    $('.cls_addRecentCustomerFilter').html(clearRecentCustomersHtml);
    App.init();
    searchCustomerWithSelection();
}
function clearIndustryCustomers() {
    industryCustomers = new Array();
    $('.cls_clearIndustry').css("display", "none");
    var clearIndustryCustomersHtml = '';
    clearIndustryCustomersHtml += '<label><input type="checkbox" name="option[]" value="1" id="id_financial" onChange="searchCustomerWithSelection();return false;"/>Financial</label>';
    clearIndustryCustomersHtml += '<label><input type="checkbox" name="option[]" value="2" id="id_banking" onChange="searchCustomerWithSelection();return false;"/>Banking</label>';
    clearIndustryCustomersHtml += '<label><input type="checkbox" name="option[]" value="3" id="id_software" onChange="searchCustomerWithSelection();return false;"/>Software</label>';
    clearIndustryCustomersHtml += '<label><input type="checkbox" name="option[]" value="4" id="id_brokerage" onChange="searchCustomerWithSelection();return false;"/>Brokerage</label>';
    clearIndustryCustomersHtml += '<label><input type="checkbox" name="option[]" value="5" id="id_biotech" onChange="searchCustomerWithSelection();return false;"/>Biotechnology</label>';
    clearIndustryCustomersHtml += '<label><input type="checkbox" name="option[]" value="6" id="id_investment" onChange="searchCustomerWithSelection();return false;"/>Investment Banking</label>';
    clearIndustryCustomersHtml += '<label><input type="checkbox" name="option[]" value="7" id="id_real" onChange="searchCustomerWithSelection();return false;"/>Real Estate</label>';
    $('.cls_addIndustryCustomerFilter').html(clearIndustryCustomersHtml);
    App.init();
    searchCustomerWithSelection();
}
function clearTerritoryCustomers() {
    territorycustomers = new Array();
    $('.cls_clearTerritory').css("display", "none");
    var clearTerritoryCustomersHtml = '';
    clearTerritoryCustomersHtml += '<label><input type="checkbox" name="option[]" value="1" id="id_west" onChange="searchCustomerWithSelection();return false;"/>West</label>';
    clearTerritoryCustomersHtml += '<label><input type="checkbox" name="option[]" value="2" id="id_east" onChange="searchCustomerWithSelection();return false;"/>East</label>';
    clearTerritoryCustomersHtml += '<label><input type="checkbox" name="option[]" value="3" id="id_north" onChange="searchCustomerWithSelection();return false;"/>North</label>';
    clearTerritoryCustomersHtml += '<label><input type="checkbox" name="option[]" value="4" id="id_south" onChange="searchCustomerWithSelection();return false;"/>South</label>';
    $('.cls_addTerritoryCustomerFilter').html(clearTerritoryCustomersHtml);
    App.init();
    searchCustomerWithSelection();
}
